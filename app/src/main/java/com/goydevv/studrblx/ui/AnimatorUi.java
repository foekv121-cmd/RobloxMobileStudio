package com.goydevv.studrblx.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.goydevv.studrblx.core.Studio;
import com.goydevv.studrblx.engine.SunAnim;
import com.goydevv.studrblx.engine.SunEase;
import com.goydevv.studrblx.engine.SunRig;
import com.goydevv.studrblx.model.CFrame;
import com.goydevv.studrblx.model.Instance;
import com.goydevv.studrblx.model.InstanceFactory;
import com.goydevv.studrblx.model.Vector3;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import kotlin.Unit;

/**
 * Keyframe animator for rigs and models, rebuilt on the feature set of the Sun Animator plugin.
 *
 * Select a Model in the Explorer and open File > Animator. Motor6D joints get a track each (their
 * Transform is animated, so a rig keeps working in real Roblox); parts that no joint moves get an
 * absolute CFrame track. Pose with the normal Move / Rotate tools, tap Key to record the pose at the
 * playhead, and drag the diamonds to retime. Every key has its own easing (13 families, 4 directions).
 * Save writes a Script named Anim_<name> into the model (data + player), Export writes a Roblox
 * KeyframeSequence, and Import reads one back.
 */
public final class AnimatorUi implements TimelineView.Host {
    private static final int MAX_KEYS = 3000;
    private static AnimatorUi current;

    private interface FieldsCb {
        void ok(String[] values);
    }

    private static final class ClipKey {
        String path;
        int offset;
        SunAnim.Key key;
    }

    private final Activity act;
    private final Studio studio;
    private final Instance root;
    private final SunRig rig;
    private SunAnim anim;
    private final ArrayDeque<SunAnim> undo = new ArrayDeque<>();
    private final ArrayDeque<SunAnim> redo = new ArrayDeque<>();
    private final List<ClipKey> clip = new ArrayList<>();
    private int cur;
    private float playPos;
    private boolean playing;
    private long lastTick;
    private boolean dirty;
    private boolean useLastEase = true;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private AlertDialog dialog;
    private static int lastX = Integer.MIN_VALUE;
    private static int lastY = Integer.MIN_VALUE;
    private TimelineView tl;
    private TextView frameLabel;
    private TextView playBtn;
    private TextView multiBtn;
    private TextView status;

    private final Runnable tick = new Runnable() {
        @Override
        public void run() {
            if (!playing) {
                return;
            }
            long now = System.nanoTime();
            float dt = (now - lastTick) / 1.0e9f;
            lastTick = now;
            playPos += dt * anim.fps;
            if (playPos >= anim.length) {
                if (anim.loop) {
                    playPos = playPos % Math.max(1, anim.length);
                } else {
                    playPos = anim.length;
                    playing = false;
                    playBtn.setText("\u25B6");
                }
            }
            cur = (int) playPos;
            rig.apply(anim, playPos);
            studio.markDirty();
            tl.setPlayFrame(cur);
            tl.keepPlayheadVisible();
            updateLabel();
            if (playing) {
                handler.postDelayed(this, 33L);
            }
        }
    };

    private AnimatorUi(Activity act, Studio studio, Instance root, SunRig rig) {
        this.act = act;
        this.studio = studio;
        this.root = root;
        this.rig = rig;
    }

    // ------------------------------------------------------------------ opening

    /** Opens the animator for the current Explorer selection. */
    public static void show(Activity act, Studio studio) {
        try {
            if (studio.isPlaying()) {
                toast(act, "Stop the playtest first. The animator edits the place.");
                return;
            }
            Instance target = null;
            for (Instance i : new ArrayList<>(studio.getSelection())) {
                if (i.isA("Model") || i.isA("BasePart") || i.isA("Folder")) {
                    target = i;
                    break;
                }
            }
            if (target == null) {
                toast(act, "Select a Model (or a Part) in the Explorer first, then open the Animator.");
                return;
            }
            if (current != null) {
                current.close();
            }
            SunRig rig = new SunRig(target);
            if (rig.isEmpty()) {
                toast(act, "That selection has no parts to animate.");
                return;
            }
            AnimatorUi a = new AnimatorUi(act, studio, target, rig);
            current = a;
            a.anim = rig.newAnim();
            a.loadFromModel(null);
            a.build();
        } catch (Throwable t) {
            toast(act, "Couldn't open the Animator: " + t);
        }
    }

    private static void toast(Activity a, String s) {
        Toast.makeText(a, s, Toast.LENGTH_LONG).show();
    }

    // ------------------------------------------------------------------ small UI helpers

    private void say(String s) {
        if (status != null) {
            status.setText(s);
        }
    }

    private TextView btn(String label, final Runnable r) {
        final Ui ui = Ui.INSTANCE;
        return ui.button(act, label, ui.getHOVER(), v -> {
            try {
                r.run();
            } catch (Throwable t) {
                say("Error: " + t);
            }
            return Unit.INSTANCE;
        });
    }

    private LinearLayout hrow(View... views) {
        LinearLayout l = new LinearLayout(act);
        l.setOrientation(LinearLayout.HORIZONTAL);
        int m = Ui.INSTANCE.dp(act, 2.0f);
        for (View v : views) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, -2, 1.0f);
            lp.setMargins(m, m, m, m);
            l.addView(v, lp);
        }
        return l;
    }

    private void menu(View anchor, Object... pairs) {
        PopupMenu pm = new PopupMenu(Ui.INSTANCE.popupContext(act), anchor);
        for (int i = 0; i + 1 < pairs.length; i += 2) {
            pm.getMenu().add(0, (Integer) pairs[i], 0, (String) pairs[i + 1]);
        }
        pm.setOnMenuItemClickListener(item -> {
            try {
                onMenu(item.getItemId());
            } catch (Throwable t) {
                say("Error: " + t);
            }
            return true;
        });
        pm.show();
    }

    private static String chk(boolean on, String label) {
        return (on ? "\u2713 " : "    ") + label;
    }

    private void fieldsDialog(String title, String[] labels, String[] defs, boolean[] numeric, final FieldsCb cb) {
        final Ui ui = Ui.INSTANCE;
        LinearLayout box = new LinearLayout(act);
        box.setOrientation(LinearLayout.VERTICAL);
        int pad = ui.dp(act, 14.0f);
        box.setPadding(pad, pad, pad, pad);
        final EditText[] ets = new EditText[labels.length];
        for (int i = 0; i < labels.length; i++) {
            box.addView(ui.text(act, labels[i], 11.0f, ui.getDIM(), false));
            ets[i] = ui.input(act, defs[i], labels[i], numeric[i], false);
            box.addView(ets[i]);
        }
        ScrollView sv = new ScrollView(act);
        sv.addView(box);
        Dialogs.INSTANCE.themed(act).setTitle(title).setView(sv)
                .setPositiveButton("OK", (d, w) -> {
                    String[] out = new String[ets.length];
                    for (int i = 0; i < out.length; i++) {
                        out[i] = ets[i].getText().toString().trim();
                    }
                    try {
                        cb.ok(out);
                    } catch (Throwable t) {
                        say("Error: " + t);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private static float num(String s, float fallback) {
        try {
            return Float.parseFloat(s);
        } catch (Throwable t) {
            return fallback;
        }
    }

    private static int inum(String s, int fallback) {
        return Math.round(num(s, fallback));
    }

    private static String f2(float v) {
        return String.format(Locale.US, "%.3f", v);
    }

    private static String safeName(String s) {
        String n = s == null ? "" : s.trim().replaceAll("[^A-Za-z0-9_ -]", "");
        if (n.length() > 30) {
            n = n.substring(0, 30);
        }
        return n.length() == 0 ? "Anim1" : n;
    }

    // ------------------------------------------------------------------ building the screen

    private void build() {
        final Ui ui = Ui.INSTANCE;
        LinearLayout content = new LinearLayout(act);
        content.setOrientation(LinearLayout.VERTICAL);
        int pad = ui.dp(act, 8.0f);
        content.setPadding(pad, pad, pad, pad);

        // menu bar
        final TextView title = ui.text(act, "\u2630 Animator \u2014 " + root.getName(), 13.0f, ui.getTEXT(), true);
        title.setPadding(0, ui.dp(act, 8.0f), ui.dp(act, 8.0f), ui.dp(act, 8.0f));
        LinearLayout bar = new LinearLayout(act);
        bar.setOrientation(LinearLayout.HORIZONTAL);
        bar.setGravity(Gravity.CENTER_VERTICAL);
        bar.addView(title, new LinearLayout.LayoutParams(0, -2, 1.0f));
        String[] names = {"File", "Edit", "Key", "Frame", "Options"};
        for (final String n : names) {
            TextView b = btn(n, null);
            final TextView bb = b;
            b.setOnClickListener(v -> openMenu(n, bb));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-2, -2);
            lp.setMargins(ui.dp(act, 2.0f), 0, ui.dp(act, 2.0f), 0);
            bar.addView(b, lp);
        }
        TextView close = btn("\u2715", this::requestClose);
        bar.addView(close, new LinearLayout.LayoutParams(-2, -2));
        content.addView(bar);

        // transport
        playBtn = btn("\u25B6", this::togglePlay);
        content.addView(hrow(
                btn("\u23EE", () -> goTo(0)),
                btn("\u25C0\u25C6", () -> jumpKey(-1)),
                playBtn,
                btn("\u25C6\u25B6", () -> jumpKey(1)),
                btn("\u23ED", () -> goTo(anim.length))));
        frameLabel = ui.text(act, "", 12.0f, ui.getTEXT(), true);
        frameLabel.setOnClickListener(v -> jumpDialog());
        frameLabel.setPadding(ui.dp(act, 6.0f), ui.dp(act, 4.0f), 0, ui.dp(act, 4.0f));
        content.addView(frameLabel);

        // timeline
        tl = new TimelineView(act);
        tl.anim = anim;
        tl.host = this;
        content.addView(tl, new LinearLayout.LayoutParams(-1, 0, 1.0f));

        // actions
        multiBtn = btn("Multi: off", this::toggleMulti);
        content.addView(hrow(
                btn("\u25C6 Key", () -> setKeys(false)),
                btn("Ease", this::easeDialog),
                btn("Value", this::valueDialog),
                btn("Delete", this::deleteKeys),
                multiBtn));
        status = ui.text(act, "Pose the parts with the normal tools, then tap Key. Long press a row to add a key there.", 11.0f, ui.getDIM(), false);
        status.setSingleLine(false);
        status.setPadding(ui.dp(act, 6.0f), ui.dp(act, 4.0f), 0, 0);
        content.addView(status);

        dialog = Dialogs.INSTANCE.themed(act).setView(content).setCancelable(false).create();
        Window w = dialog.getWindow();
        if (w != null) {
            // not modal: touches outside the panel still reach the viewport, so parts can be posed while it is open
            w.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
            w.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            DisplayMetrics dm = act.getResources().getDisplayMetrics();
            boolean landscape = dm.widthPixels > dm.heightPixels;
            int ww = Math.min(dm.widthPixels, landscape ? Math.max(ui.dp(act, 420.0f), dm.widthPixels * 58 / 100) : dm.widthPixels * 96 / 100);
            int hh = Math.min(dm.heightPixels, landscape ? Math.max(ui.dp(act, 240.0f), dm.heightPixels * 50 / 100) : Math.max(ui.dp(act, 260.0f), dm.heightPixels * 40 / 100));
            w.setLayout(ww, hh);
            WindowManager.LayoutParams lp = w.getAttributes();
            lp.gravity = Gravity.TOP | Gravity.LEFT;
            lp.x = lastX == Integer.MIN_VALUE ? (dm.widthPixels - ww) / 2 : lastX;
            lp.y = lastY == Integer.MIN_VALUE ? dm.heightPixels - hh - ui.dp(act, 8.0f) : lastY;
            lp.x = Math.max(-(ww - ui.dp(act, 60.0f)), Math.min(dm.widthPixels - ui.dp(act, 60.0f), lp.x));
            lp.y = Math.max(0, Math.min(dm.heightPixels - ui.dp(act, 40.0f), lp.y));
            w.setAttributes(lp);
            enableDrag(title);
        }
        dialog.show();
        tl.post(() -> tl.fit());
        updateLabel();
        say(rig.hasJoints()
                ? "Rig found: each Motor6D has a track. Pose a limb, then tap Key."
                : "No Motor6D joints found: each free part gets a track.");
    }

    /** Lets the user drag the whole panel by its title, so it can be moved out of the way of the viewport. */
    private void enableDrag(View handle) {
        final int[] st = new int[4];
        handle.setOnTouchListener((v, ev) -> {
            Window win = dialog == null ? null : dialog.getWindow();
            if (win == null) {
                return false;
            }
            WindowManager.LayoutParams lp = win.getAttributes();
            DisplayMetrics dm = act.getResources().getDisplayMetrics();
            int keep = Ui.INSTANCE.dp(act, 60.0f);
            switch (ev.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    st[0] = (int) ev.getRawX();
                    st[1] = (int) ev.getRawY();
                    st[2] = lp.x;
                    st[3] = lp.y;
                    return true;
                case MotionEvent.ACTION_MOVE:
                    lp.x = Math.max(-(lp.width - keep), Math.min(dm.widthPixels - keep, st[2] + (int) ev.getRawX() - st[0]));
                    lp.y = Math.max(0, Math.min(dm.heightPixels - keep, st[3] + (int) ev.getRawY() - st[1]));
                    win.setAttributes(lp);
                    lastX = lp.x;
                    lastY = lp.y;
                    return true;
                default:
                    return true;
            }
        });
    }

    private void openMenu(String which, View anchor) {
        switch (which) {
            case "File":
                menu(anchor, 1, "Save to model", 2, "Load from model\u2026", 3, "Export KeyframeSequence",
                        4, "Import KeyframeSequence", 5, "Animation settings\u2026", 6, "Close");
                break;
            case "Edit":
                menu(anchor, 10, "Undo", 11, "Redo", 12, "Copy", 13, "Paste at playhead", 14, "Delete",
                        15, "Select all", 16, "Select track keys", 17, "Clear selection");
                break;
            case "Key":
                menu(anchor, 20, "Ease\u2026", 21, "Value\u2026", 22, "Key selected track", 23, "Key all tracks",
                        24, "Key rest pose", 25, "Previous key", 26, "Next key");
                break;
            case "Frame":
                menu(anchor, 30, "Stretch\u2026", 31, "Offset\u2026", 32, "Repeat\u2026", 33, "Fit timeline",
                        34, "Jump to frame\u2026", 35, "Markers\u2026", 36, "Reverse selection");
                break;
            default:
                menu(anchor, 40, chk(tl.easeColors, "Easing colors"), 41, chk(tl.showSeconds, "Show seconds"),
                        42, chk(useLastEase, "Use last ease"));
                break;
        }
    }

    private void onMenu(int id) {
        switch (id) {
            case 1: save(); break;
            case 2: loadDialog(); break;
            case 3: exportSequence(); break;
            case 4: importSequence(); break;
            case 5: settingsDialog(); break;
            case 6: requestClose(); break;
            case 10: doUndo(); break;
            case 11: doRedo(); break;
            case 12: copyKeys(); break;
            case 13: pasteKeys(); break;
            case 14: deleteKeys(); break;
            case 15: selectAll(); break;
            case 16: selectTrackKeys(); break;
            case 17: tl.selected.clear(); tl.invalidate(); say("Selection cleared."); break;
            case 20: easeDialog(); break;
            case 21: valueDialog(); break;
            case 22: setKeys(false); break;
            case 23: setKeys(true); break;
            case 24: keyRest(); break;
            case 25: jumpKey(-1); break;
            case 26: jumpKey(1); break;
            case 30: stretchDialog(); break;
            case 31: offsetDialog(); break;
            case 32: repeatDialog(); break;
            case 33: fitTimeline(); break;
            case 34: jumpDialog(); break;
            case 35: markersDialog(); break;
            case 36: reverseKeys(); break;
            case 40: tl.easeColors = !tl.easeColors; tl.invalidate(); break;
            case 41: tl.showSeconds = !tl.showSeconds; tl.invalidate(); updateLabel(); break;
            case 42: useLastEase = !useLastEase; break;
            default: break;
        }
    }

    // ------------------------------------------------------------------ TimelineView.Host

    @Override
    public void onScrub(int frame) {
        stopPlay();
        cur = Math.max(0, frame);
        rig.apply(anim, cur);
        studio.markDirty();
        updateLabel();
    }

    @Override
    public void onSelectTrack(int index) {
        updateLabel();
    }

    @Override
    public void onSelectionChanged() {
        updateLabel();
    }

    @Override
    public void onBeginEdit() {
        pushUndo();
    }

    @Override
    public void onKeysMoved() {
        for (SunAnim.Track t : anim.tracks) {
            for (int i = t.keys.size() - 1; i >= 0; i--) {
                SunAnim.Key k = t.keys.get(i);
                if (tl.selected.contains(k)) {
                    continue;
                }
                for (SunAnim.Key s : t.keys) {
                    if (s != k && tl.selected.contains(s) && s.frame == k.frame) {
                        t.keys.remove(i);
                        break;
                    }
                }
            }
        }
        anim.normalize();
        rig.apply(anim, cur);
        tl.invalidate();
        updateLabel();
        say("Moved " + tl.selected.size() + " key(s).");
    }

    @Override
    public void onAddKeyAt(int trackIndex, int frame) {
        if (trackIndex < 0 || trackIndex >= anim.tracks.size()) {
            return;
        }
        stopPlay();
        pushUndo();
        SunAnim.Track t = anim.tracks.get(trackIndex);
        CFrame v = frame == cur ? rig.capture(t) : SunAnim.sample(t, frame);
        if (v == null) {
            v = rig.rest(t);
        }
        if (v == null) {
            say("Nothing to key on " + t.label + ".");
            return;
        }
        putKey(t, frame, v);
        anim.normalize();
        tl.selTrack = trackIndex;
        cur = frame;
        tl.setPlayFrame(frame);
        rig.apply(anim, cur);
        updateLabel();
        say("Key added on " + t.label + " at frame " + frame + ".");
    }

    // ------------------------------------------------------------------ state helpers

    private void pushUndo() {
        undo.push(anim.copy());
        while (undo.size() > 60) {
            undo.removeLast();
        }
        redo.clear();
        dirty = true;
    }

    private void rebind() {
        tl.anim = anim;
        tl.selected.clear();
        if (tl.selTrack >= anim.tracks.size()) {
            tl.selTrack = -1;
        }
        cur = Math.min(cur, Math.max(0, anim.length));
        tl.setPlayFrame(cur);
        rig.apply(anim, cur);
        studio.markDirty();
        updateLabel();
    }

    private void doUndo() {
        stopPlay();
        if (undo.isEmpty()) {
            say("Nothing to undo.");
            return;
        }
        redo.push(anim.copy());
        anim = undo.pop();
        dirty = true;
        rebind();
        say("Undone.");
    }

    private void doRedo() {
        stopPlay();
        if (redo.isEmpty()) {
            say("Nothing to redo.");
            return;
        }
        undo.push(anim.copy());
        anim = redo.pop();
        dirty = true;
        rebind();
        say("Redone.");
    }

    private void updateLabel() {
        if (frameLabel == null) {
            return;
        }
        String time = tl != null && tl.showSeconds
                ? f2(cur / (float) anim.fps) + " s"
                : "frame " + cur + " / " + anim.length;
        String sel = tl != null && !tl.selected.isEmpty() ? "   \u2022   " + tl.selected.size() + " key(s) selected" : "";
        String trk = tl != null && tl.selTrack >= 0 && tl.selTrack < anim.tracks.size()
                ? "   \u2022   " + anim.tracks.get(tl.selTrack).label : "";
        frameLabel.setText(time + "  (" + anim.fps + " fps)" + trk + sel);
    }

    private void toggleMulti() {
        tl.multi = !tl.multi;
        multiBtn.setText(tl.multi ? "Multi: on" : "Multi: off");
        say(tl.multi ? "Multi-select on: tap diamonds to add or remove them from the selection." : "Multi-select off.");
    }

    private List<SunAnim.Key> targetKeys() {
        List<SunAnim.Key> out = new ArrayList<>();
        for (SunAnim.Track t : anim.tracks) {
            for (SunAnim.Key k : t.keys) {
                if (tl.selected.contains(k)) {
                    out.add(k);
                }
            }
        }
        if (out.isEmpty() && tl.selTrack >= 0 && tl.selTrack < anim.tracks.size()) {
            SunAnim.Key k = anim.tracks.get(tl.selTrack).at(cur);
            if (k != null) {
                out.add(k);
            }
        }
        return out;
    }

    private List<SunAnim.Key> keysOrAll() {
        List<SunAnim.Key> keys = targetKeys();
        if (keys.isEmpty()) {
            for (SunAnim.Track t : anim.tracks) {
                keys.addAll(t.keys);
            }
        }
        return keys;
    }

    private void putKey(SunAnim.Track t, int frame, CFrame v) {
        SunAnim.Key k = new SunAnim.Key(frame, v);
        if (useLastEase) {
            SunAnim.Key prev = null;
            for (SunAnim.Key o : t.keys) {
                if (o.frame < frame) {
                    prev = o;
                }
            }
            SunAnim.Key same = t.at(frame);
            SunAnim.Key src = same != null ? same : prev;
            if (src != null) {
                k.ease = src.ease;
                k.dir = src.dir;
                k.p1 = src.p1;
                k.p2 = src.p2;
            }
        }
        t.put(k);
    }

    // ------------------------------------------------------------------ playback

    private void togglePlay() {
        if (playing) {
            stopPlay();
            return;
        }
        if (anim.keyCount() < 2) {
            say("Add at least two keys before playing.");
            return;
        }
        if (cur >= anim.length) {
            cur = 0;
        }
        playPos = cur;
        playing = true;
        lastTick = System.nanoTime();
        playBtn.setText("\u25A0");
        handler.post(tick);
    }

    private void stopPlay() {
        if (playing) {
            playing = false;
            handler.removeCallbacks(tick);
            if (playBtn != null) {
                playBtn.setText("\u25B6");
            }
        }
    }

    private void goTo(int f) {
        stopPlay();
        cur = Math.max(0, f);
        tl.setPlayFrame(cur);
        rig.apply(anim, cur);
        studio.markDirty();
        updateLabel();
    }

    private void jumpKey(int dir) {
        int best = -1;
        for (SunAnim.Track t : anim.tracks) {
            if (tl.selTrack >= 0 && anim.tracks.indexOf(t) != tl.selTrack) {
                continue;
            }
            for (SunAnim.Key k : t.keys) {
                if (dir < 0 && k.frame < cur && (best < 0 || k.frame > best)) {
                    best = k.frame;
                }
                if (dir > 0 && k.frame > cur && (best < 0 || k.frame < best)) {
                    best = k.frame;
                }
            }
        }
        if (best < 0) {
            say(dir < 0 ? "No earlier key." : "No later key.");
            return;
        }
        goTo(best);
        tl.keepPlayheadVisible();
    }

    private void jumpDialog() {
        fieldsDialog("Jump to frame", new String[]{"Frame"}, new String[]{String.valueOf(cur)}, new boolean[]{true},
                v -> {
                    goTo(inum(v[0], cur));
                    tl.keepPlayheadVisible();
                });
    }

    // ------------------------------------------------------------------ key editing

    private void setKeys(boolean all) {
        stopPlay();
        List<SunAnim.Track> targets = new ArrayList<>();
        if (!all && tl.selTrack >= 0 && tl.selTrack < anim.tracks.size()) {
            targets.add(anim.tracks.get(tl.selTrack));
        } else {
            targets.addAll(anim.tracks);
        }
        if (anim.keyCount() + targets.size() > MAX_KEYS) {
            say("Too many keys (limit " + MAX_KEYS + ").");
            return;
        }
        pushUndo();
        int n = 0;
        for (SunAnim.Track t : targets) {
            CFrame v = rig.capture(t);
            if (v != null) {
                putKey(t, cur, v);
                n++;
            }
        }
        anim.normalize();
        tl.invalidate();
        updateLabel();
        say("Keyed " + n + " track(s) at frame " + cur + ". Tap a track name to key just one.");
    }

    private void keyRest() {
        stopPlay();
        pushUndo();
        int n = 0;
        for (int i = 0; i < anim.tracks.size(); i++) {
            if (tl.selTrack >= 0 && i != tl.selTrack) {
                continue;
            }
            SunAnim.Track t = anim.tracks.get(i);
            CFrame v = rig.rest(t);
            if (v != null) {
                putKey(t, cur, v);
                n++;
            }
        }
        anim.normalize();
        rig.apply(anim, cur);
        tl.invalidate();
        say("Rest pose keyed on " + n + " track(s) at frame " + cur + ".");
    }

    private void deleteKeys() {
        stopPlay();
        List<SunAnim.Key> keys = targetKeys();
        if (keys.isEmpty()) {
            say("Select a key first.");
            return;
        }
        pushUndo();
        for (SunAnim.Track t : anim.tracks) {
            t.keys.removeAll(keys);
        }
        tl.selected.clear();
        rig.apply(anim, cur);
        tl.invalidate();
        updateLabel();
        say("Deleted " + keys.size() + " key(s).");
    }

    private void selectAll() {
        tl.selected.clear();
        for (SunAnim.Track t : anim.tracks) {
            tl.selected.addAll(t.keys);
        }
        tl.invalidate();
        updateLabel();
        say("Selected " + tl.selected.size() + " key(s).");
    }

    private void selectTrackKeys() {
        if (tl.selTrack < 0 || tl.selTrack >= anim.tracks.size()) {
            say("Tap a track name first.");
            return;
        }
        tl.selected.clear();
        tl.selected.addAll(anim.tracks.get(tl.selTrack).keys);
        tl.invalidate();
        updateLabel();
    }

    private void copyKeys() {
        List<SunAnim.Key> keys = targetKeys();
        if (keys.isEmpty()) {
            say("Select keys to copy.");
            return;
        }
        int min = Integer.MAX_VALUE;
        for (SunAnim.Key k : keys) {
            min = Math.min(min, k.frame);
        }
        clip.clear();
        for (SunAnim.Track t : anim.tracks) {
            for (SunAnim.Key k : t.keys) {
                if (keys.contains(k)) {
                    ClipKey c = new ClipKey();
                    c.path = t.path;
                    c.offset = k.frame - min;
                    c.key = k.copy();
                    clip.add(c);
                }
            }
        }
        say("Copied " + clip.size() + " key(s).");
    }

    private void pasteKeys() {
        if (clip.isEmpty()) {
            say("Nothing copied yet.");
            return;
        }
        stopPlay();
        pushUndo();
        tl.selected.clear();
        int n = 0;
        for (ClipKey c : clip) {
            SunAnim.Track t = anim.find(c.path);
            if (t == null) {
                continue;
            }
            SunAnim.Key k = c.key.copy();
            k.frame = cur + c.offset;
            t.put(k);
            tl.selected.add(k);
            n++;
        }
        anim.normalize();
        rig.apply(anim, cur);
        tl.invalidate();
        updateLabel();
        say("Pasted " + n + " key(s) at frame " + cur + ".");
    }

    private void reverseKeys() {
        List<SunAnim.Key> keys = keysOrAll();
        if (keys.size() < 2) {
            say("Select at least two keys to reverse.");
            return;
        }
        stopPlay();
        pushUndo();
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (SunAnim.Key k : keys) {
            min = Math.min(min, k.frame);
            max = Math.max(max, k.frame);
        }
        for (SunAnim.Key k : keys) {
            k.frame = min + max - k.frame;
        }
        anim.normalize();
        rig.apply(anim, cur);
        tl.invalidate();
        say("Reversed " + keys.size() + " key(s).");
    }

    private void stretchDialog() {
        fieldsDialog("Stretch", new String[]{"Factor (2 = twice as slow, 0.5 = twice as fast)"}, new String[]{"2"}, new boolean[]{true},
                v -> {
                    float factor = num(v[0], 1.0f);
                    if (factor <= 0.0f) {
                        say("The factor must be above 0.");
                        return;
                    }
                    List<SunAnim.Key> keys = keysOrAll();
                    if (keys.isEmpty()) {
                        say("There are no keys.");
                        return;
                    }
                    pushUndo();
                    int min = Integer.MAX_VALUE;
                    for (SunAnim.Key k : keys) {
                        min = Math.min(min, k.frame);
                    }
                    for (SunAnim.Key k : keys) {
                        k.frame = min + Math.round((k.frame - min) * factor);
                    }
                    anim.normalize();
                    rig.apply(anim, cur);
                    tl.invalidate();
                    say("Stretched " + keys.size() + " key(s) by " + factor + ".");
                });
    }

    private void offsetDialog() {
        fieldsDialog("Offset", new String[]{"Frames (negative moves earlier)"}, new String[]{"5"}, new boolean[]{true},
                v -> {
                    int delta = inum(v[0], 0);
                    List<SunAnim.Key> keys = keysOrAll();
                    if (keys.isEmpty() || delta == 0) {
                        say("Nothing to move.");
                        return;
                    }
                    pushUndo();
                    for (SunAnim.Key k : keys) {
                        k.frame = Math.max(0, k.frame + delta);
                    }
                    anim.normalize();
                    rig.apply(anim, cur);
                    tl.invalidate();
                    say("Moved " + keys.size() + " key(s) by " + delta + " frame(s).");
                });
    }

    private void repeatDialog() {
        fieldsDialog("Repeat", new String[]{"Times", "Gap between copies (frames)"}, new String[]{"2", "0"}, new boolean[]{true, true},
                v -> {
                    int times = Math.max(1, Math.min(50, inum(v[0], 1)));
                    int gap = Math.max(0, inum(v[1], 0));
                    List<SunAnim.Key> keys = keysOrAll();
                    if (keys.isEmpty()) {
                        say("There are no keys.");
                        return;
                    }
                    int min = Integer.MAX_VALUE;
                    int max = Integer.MIN_VALUE;
                    for (SunAnim.Key k : keys) {
                        min = Math.min(min, k.frame);
                        max = Math.max(max, k.frame);
                    }
                    int span = max - min + gap;
                    if (span <= 0) {
                        say("The keys need some space between them to repeat.");
                        return;
                    }
                    if (anim.keyCount() + keys.size() * times > MAX_KEYS) {
                        say("Too many keys (limit " + MAX_KEYS + ").");
                        return;
                    }
                    pushUndo();
                    for (SunAnim.Track t : anim.tracks) {
                        List<SunAnim.Key> mine = new ArrayList<>();
                        for (SunAnim.Key k : t.keys) {
                            if (keys.contains(k)) {
                                mine.add(k);
                            }
                        }
                        for (int n = 1; n <= times; n++) {
                            for (SunAnim.Key k : mine) {
                                SunAnim.Key c = k.copy();
                                c.frame = k.frame + span * n;
                                t.put(c);
                            }
                        }
                    }
                    anim.length = Math.max(anim.length, max + span * times);
                    anim.normalize();
                    tl.invalidate();
                    say("Repeated " + keys.size() + " key(s) " + times + " time(s).");
                });
    }

    private void fitTimeline() {
        pushUndo();
        anim.length = Math.max(1, anim.lastKeyFrame());
        tl.fit();
        updateLabel();
        say("Length set to " + anim.length + " frames.");
    }

    // ------------------------------------------------------------------ ease and value editors

    private void easeDialog() {
        final List<SunAnim.Key> keys = targetKeys();
        if (keys.isEmpty()) {
            say("Select a key first (tap a diamond).");
            return;
        }
        final Ui ui = Ui.INSTANCE;
        final SunAnim.Key first = keys.get(0);
        final String[] fam = {first.ease};
        final int[] dir = {first.dir};
        final float[] p = {first.p1, first.p2};
        final TimelineView.CurveView curve = new TimelineView.CurveView(act);
        final TextView famBtn = ui.text(act, "", 14.0f, ui.getTEXT(), true);
        final TextView dirBtn = ui.text(act, "", 14.0f, ui.getTEXT(), true);
        final EditText e1 = ui.input(act, f2(p[0]), "", true, false);
        final EditText e2 = ui.input(act, f2(p[1]), "", true, false);
        final TextView l1 = ui.text(act, "", 11.0f, ui.getDIM(), false);
        final TextView l2 = ui.text(act, "Period", 11.0f, ui.getDIM(), false);
        final Runnable refresh = new Runnable() {
            @Override
            public void run() {
                famBtn.setText("Style: " + fam[0] + "  \u25BE");
                boolean hasDir = SunEase.hasDirection(fam[0]);
                dirBtn.setText(hasDir ? "Direction: " + SunEase.DIRS[dir[0]] + "  \u25BE" : "Direction: (none)");
                boolean back = SunEase.isBack(fam[0]);
                boolean elastic = SunEase.isElastic(fam[0]);
                l1.setText(back ? "Overshoot" : "Amplitude");
                int vis1 = back || elastic ? View.VISIBLE : View.GONE;
                l1.setVisibility(vis1);
                e1.setVisibility(vis1);
                l2.setVisibility(elastic ? View.VISIBLE : View.GONE);
                e2.setVisibility(elastic ? View.VISIBLE : View.GONE);
                curve.set(fam[0], dir[0], num(e1.getText().toString(), p[0]), num(e2.getText().toString(), p[1]));
            }
        };
        famBtn.setOnClickListener(v -> Dialogs.INSTANCE.themed(act).setTitle("Easing style")
                .setItems(SunEase.FAMILIES, (d, which) -> {
                    fam[0] = SunEase.FAMILIES[which];
                    e1.setText(f2(SunEase.defaultP1(fam[0])));
                    e2.setText(f2(SunEase.defaultP2(fam[0])));
                    refresh.run();
                }).show());
        dirBtn.setOnClickListener(v -> {
            if (SunEase.hasDirection(fam[0])) {
                Dialogs.INSTANCE.themed(act).setTitle("Easing direction")
                        .setItems(SunEase.DIRS, (d, which) -> {
                            dir[0] = which;
                            refresh.run();
                        }).show();
            }
        });
        android.text.TextWatcher watcher = new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int a, int b, int c) {
            }

            @Override
            public void onTextChanged(CharSequence s, int a, int b, int c) {
                curve.set(fam[0], dir[0], num(e1.getText().toString(), p[0]), num(e2.getText().toString(), p[1]));
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {
            }
        };
        e1.addTextChangedListener(watcher);
        e2.addTextChangedListener(watcher);

        LinearLayout box = new LinearLayout(act);
        box.setOrientation(LinearLayout.VERTICAL);
        int pad = ui.dp(act, 14.0f);
        box.setPadding(pad, pad, pad, pad);
        box.addView(curve, new LinearLayout.LayoutParams(-1, ui.dp(act, 130.0f)));
        famBtn.setPadding(0, pad, 0, pad / 2);
        dirBtn.setPadding(0, pad / 2, 0, pad / 2);
        box.addView(famBtn);
        box.addView(dirBtn);
        box.addView(l1);
        box.addView(e1);
        box.addView(l2);
        box.addView(e2);
        ScrollView sv = new ScrollView(act);
        sv.addView(box);
        refresh.run();
        Dialogs.INSTANCE.themed(act)
                .setTitle("Ease (" + keys.size() + " key" + (keys.size() == 1 ? "" : "s") + ")")
                .setView(sv)
                .setPositiveButton("Apply", (d, w) -> {
                    pushUndo();
                    float a = num(e1.getText().toString(), SunEase.defaultP1(fam[0]));
                    float b = num(e2.getText().toString(), SunEase.defaultP2(fam[0]));
                    for (SunAnim.Key k : keys) {
                        k.ease = fam[0];
                        k.dir = SunEase.hasDirection(fam[0]) ? dir[0] : 0;
                        k.p1 = a;
                        k.p2 = Math.max(0.01f, b);
                    }
                    rig.apply(anim, cur);
                    tl.invalidate();
                    say("Easing set to " + fam[0] + (SunEase.hasDirection(fam[0]) ? " " + SunEase.DIRS[dir[0]] : "") + ".");
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void valueDialog() {
        List<SunAnim.Key> keys = targetKeys();
        if (keys.isEmpty()) {
            say("Select a key first (tap a diamond).");
            return;
        }
        final SunAnim.Key k = keys.get(0);
        Vector3 o = k.cf.toOrientation();
        double deg = 180.0d / Math.PI;
        fieldsDialog("Key value (frame " + k.frame + ")",
                new String[]{"Position X", "Position Y", "Position Z", "Rotation X (deg)", "Rotation Y (deg)", "Rotation Z (deg)"},
                new String[]{f2(k.cf.getX()), f2(k.cf.getY()), f2(k.cf.getZ()),
                    f2((float) (o.getX() * deg)), f2((float) (o.getY() * deg)), f2((float) (o.getZ() * deg))},
                new boolean[]{true, true, true, true, true, true},
                v -> {
                    pushUndo();
                    float rad = (float) (Math.PI / 180.0d);
                    k.cf = CFrame.INSTANCE.fromOrientation(num(v[3], 0) * rad, num(v[4], 0) * rad, num(v[5], 0) * rad)
                            .withPosition(new Vector3(num(v[0], 0), num(v[1], 0), num(v[2], 0)));
                    rig.apply(anim, cur);
                    studio.markDirty();
                    tl.invalidate();
                    say("Value updated.");
                });
    }

    // ------------------------------------------------------------------ markers and settings

    private void markersDialog() {
        final List<SunAnim.Marker> list = new ArrayList<>(anim.markers);
        String[] items = new String[list.size()];
        for (int i = 0; i < items.length; i++) {
            SunAnim.Marker m = list.get(i);
            items[i] = "frame " + m.frame + "  \u2022  " + m.name + (m.value.length() > 0 ? " = " + m.value : "");
        }
        AlertDialog.Builder b = Dialogs.INSTANCE.themed(act).setTitle("Markers (" + list.size() + ")")
                .setPositiveButton("Add at frame " + cur, (d, w) -> fieldsDialog("New marker",
                        new String[]{"Name", "Value"}, new String[]{"Marker", ""}, new boolean[]{false, false},
                        v -> {
                            pushUndo();
                            SunAnim.Marker m = new SunAnim.Marker(cur, v[0].length() == 0 ? "Marker" : v[0]);
                            m.value = v[1];
                            anim.markers.add(m);
                            anim.normalize();
                            tl.invalidate();
                            say("Marker added at frame " + cur + ".");
                        }))
                .setNegativeButton("Close", null);
        if (items.length > 0) {
            b.setItems(items, (d, which) -> {
                final SunAnim.Marker m = list.get(which);
                Dialogs.INSTANCE.themed(act).setTitle(m.name)
                        .setPositiveButton("Jump", (d2, w2) -> goTo(m.frame))
                        .setNeutralButton("Delete", (d2, w2) -> {
                            pushUndo();
                            anim.markers.remove(m);
                            tl.invalidate();
                            say("Marker deleted.");
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            });
        }
        b.show();
    }

    private void settingsDialog() {
        final Ui ui = Ui.INSTANCE;
        LinearLayout box = new LinearLayout(act);
        box.setOrientation(LinearLayout.VERTICAL);
        int pad = ui.dp(act, 14.0f);
        box.setPadding(pad, pad, pad, pad);
        final EditText name = ui.input(act, anim.name, "Name", false, false);
        final EditText fps = ui.input(act, String.valueOf(anim.fps), "FPS", true, false);
        final EditText len = ui.input(act, String.valueOf(anim.length), "Length", true, false);
        final CheckBox loop = new CheckBox(act);
        loop.setText("Loop");
        loop.setTextColor(ui.getTEXT());
        loop.setChecked(anim.loop);
        final int[] pr = {anim.priority};
        final TextView prBtn = ui.text(act, "Priority: " + SunAnim.PRIORITIES[pr[0]] + "  \u25BE", 14.0f, ui.getTEXT(), true);
        prBtn.setPadding(0, pad / 2, 0, pad / 2);
        prBtn.setOnClickListener(v -> Dialogs.INSTANCE.themed(act).setTitle("Priority")
                .setItems(SunAnim.PRIORITIES, (d, which) -> {
                    pr[0] = which;
                    prBtn.setText("Priority: " + SunAnim.PRIORITIES[which] + "  \u25BE");
                }).show());
        box.addView(ui.text(act, "Name", 11.0f, ui.getDIM(), false));
        box.addView(name);
        box.addView(ui.text(act, "Frames per second", 11.0f, ui.getDIM(), false));
        box.addView(fps);
        box.addView(ui.text(act, "Length (frames)", 11.0f, ui.getDIM(), false));
        box.addView(len);
        box.addView(loop);
        box.addView(prBtn);
        ScrollView sv = new ScrollView(act);
        sv.addView(box);
        Dialogs.INSTANCE.themed(act).setTitle("Animation settings").setView(sv)
                .setPositiveButton("OK", (d, w) -> {
                    pushUndo();
                    anim.name = safeName(name.getText().toString());
                    anim.fps = Math.max(1, Math.min(120, inum(fps.getText().toString(), anim.fps)));
                    anim.length = Math.max(1, inum(len.getText().toString(), anim.length));
                    anim.loop = loop.isChecked();
                    anim.priority = pr[0];
                    anim.normalize();
                    tl.invalidate();
                    updateLabel();
                    say("Settings updated.");
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // ------------------------------------------------------------------ save / load / export / import

    private Instance findScript(String name) {
        for (Instance c : root.getChildren()) {
            if (c.isA("Script") && ("Anim_" + name).equals(c.getName())) {
                return c;
            }
        }
        return null;
    }

    private List<Instance> savedScripts() {
        List<Instance> out = new ArrayList<>();
        for (Instance c : root.getChildren()) {
            if (c.isA("Script") && c.getName().startsWith("Anim_")) {
                out.add(c);
            }
        }
        return out;
    }

    /** Loads a saved animation. name == null loads the first one found. */
    private boolean loadFromModel(String name) {
        try {
            for (Instance c : savedScripts()) {
                if (name != null && !c.getName().equals("Anim_" + name)) {
                    continue;
                }
                Object src = c.get("Source");
                SunAnim loaded = src instanceof String ? SunAnim.fromScript((String) src) : null;
                if (loaded == null) {
                    continue;
                }
                if (loaded.name == null || loaded.name.length() == 0) {
                    loaded.name = c.getName().substring(5);
                }
                anim = rig.fit(loaded);
                anim.name = safeName(loaded.name);
                return true;
            }
        } catch (Throwable ignored) {
        }
        return false;
    }

    private void loadDialog() {
        final List<Instance> scripts = savedScripts();
        if (scripts.isEmpty()) {
            say("No saved animations (Anim_ scripts) in " + root.getName() + ".");
            return;
        }
        String[] names = new String[scripts.size()];
        for (int i = 0; i < names.length; i++) {
            names[i] = scripts.get(i).getName().substring(5);
        }
        Dialogs.INSTANCE.themed(act).setTitle("Load animation").setItems(names, (d, which) -> {
            stopPlay();
            pushUndo();
            SunAnim before = anim;
            if (loadFromModel(names[which])) {
                cur = 0;
                rebind();
                say("Loaded Anim_" + names[which] + ".");
            } else {
                anim = before;
                say("Couldn't read Anim_" + names[which] + ".");
            }
        }).show();
    }

    private void save() {
        stopPlay();
        anim.normalize();
        if (anim.keyCount() < 2) {
            say("Add at least two keys before saving.");
            return;
        }
        if (anim.keyCount() > MAX_KEYS) {
            say("Too many keys (limit " + MAX_KEYS + ").");
            return;
        }
        try {
            String src = anim.buildScript();
            Instance script = findScript(anim.name);
            if (script == null) {
                script = InstanceFactory.INSTANCE.create("Script");
                script.setName("Anim_" + anim.name);
                script.setParent(root, -1);
            }
            script.set("Source", src, true);
            studio.markDirty();
            dirty = false;
            say("Saved as Script \"Anim_" + anim.name + "\" inside " + root.getName() + ". It plays when the game runs. Save the place to keep it.");
        } catch (Throwable t) {
            say("Couldn't save: " + t);
        }
    }

    private void exportSequence() {
        stopPlay();
        anim.normalize();
        int joints = 0;
        int parts = 0;
        for (SunAnim.Track t : anim.tracks) {
            if (t.keys.isEmpty()) {
                continue;
            }
            if (t.kind == SunAnim.JOINT) {
                joints++;
            } else {
                parts++;
            }
        }
        if (joints == 0) {
            say("Nothing to export: a KeyframeSequence needs keys on Motor6D joint tracks.");
            return;
        }
        try {
            for (Instance c : new ArrayList<>(root.getChildren())) {
                if ("KeyframeSequence".equals(c.getClassName()) && c.getName().equals(anim.name)) {
                    c.destroy();
                }
            }
            Instance seq = anim.toKeyframeSequence();
            seq.setParent(root, -1);
            studio.markDirty();
            say("Exported KeyframeSequence \"" + anim.name + "\" into " + root.getName() + " (" + joints + " joint track(s))."
                    + (parts > 0 ? " " + parts + " part track(s) were left out." : ""));
        } catch (Throwable t) {
            say("Couldn't export: " + t);
        }
    }

    private void importSequence() {
        stopPlay();
        Instance seq = null;
        for (Instance i : new ArrayList<>(studio.getSelection())) {
            if ("KeyframeSequence".equals(i.getClassName())) {
                seq = i;
                break;
            }
        }
        if (seq == null) {
            for (Instance c : root.getChildren()) {
                if ("KeyframeSequence".equals(c.getClassName())) {
                    seq = c;
                    break;
                }
            }
        }
        if (seq == null) {
            say("Select a KeyframeSequence in the Explorer (or put one inside the model), then try again.");
            return;
        }
        pushUndo();
        int n = anim.importKeyframeSequence(seq);
        if (n == 0) {
            undo.pop();
            say("No pose in \"" + seq.getName() + "\" matches a joint of this rig.");
            return;
        }
        anim.name = safeName(seq.getName());
        rebind();
        tl.fit();
        say("Imported " + n + " joint track(s) from \"" + seq.getName() + "\".");
    }

    // ------------------------------------------------------------------ closing

    private void requestClose() {
        if (!dirty) {
            close();
            return;
        }
        Dialogs.INSTANCE.themed(act).setTitle("Close the Animator?")
                .setMessage("You have changes that are not saved to the model. The model goes back to its opening pose.")
                .setPositiveButton("Save and close", (d, w) -> {
                    save();
                    if (!dirty) {
                        close();
                    }
                })
                .setNeutralButton("Close anyway", (d, w) -> close())
                .setNegativeButton("Keep editing", null)
                .show();
    }

    private void close() {
        stopPlay();
        try {
            rig.restore();
            studio.markDirty();
        } catch (Throwable ignored) {
        }
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (Throwable ignored) {
        }
        if (current == this) {
            current = null;
        }
    }
}

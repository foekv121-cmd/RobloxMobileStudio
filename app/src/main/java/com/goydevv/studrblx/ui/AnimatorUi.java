package com.goydevv.studrblx.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.goydevv.studrblx.core.Studio;
import com.goydevv.studrblx.model.CFrame;
import com.goydevv.studrblx.model.Instance;
import com.goydevv.studrblx.model.InstanceFactory;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Keyframe animator for models and parts (not the player character).
 *
 * How it works: the user selects a Model (or a Part) in the Explorer and opens File > Animator.
 * They pose the parts with the normal Move / Rotate tools, tap "Set key here" at a time on the
 * timeline, and repeat. Scrubbing or Play previews the animation by moving the real parts.
 * "Save to model" writes a Script named Anim_<name> into the model. That script holds the
 * keyframe data and plays it with TweenService when the game runs. Saved animations are loaded
 * back from that script, so nothing else is stored anywhere.
 */
public final class AnimatorUi {
    private static final int MAX_PARTS = 40;
    private static final int MAX_KEYS = 30;
    private static final int MAX_ENTRIES = 600;
    private static final String HEADER = "--[=[ANIMATOR_DATA";
    private static AnimatorUi current;

    private final Activity act;
    private final Studio studio;
    private final Instance root;
    private final List<Instance> parts = new ArrayList<>();
    private final List<String> paths = new ArrayList<>();
    private final List<CFrame> base = new ArrayList<>();
    private final TreeMap<Float, CFrame[]> keys = new TreeMap<>();
    private int skipped;
    private float length = 2.0f;
    private boolean loop = true;
    private String animName = "Anim1";
    private float time;
    private boolean playing;
    private long lastTick;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private AlertDialog dialog;
    private TextView info;
    private TextView timeLabel;
    private TextView status;
    private TextView playBtn;
    private SeekBar bar;
    private EditText etName;
    private EditText etLength;
    private CheckBox cbLoop;

    private final Runnable tick = new Runnable() {
        @Override
        public void run() {
            if (!playing) {
                return;
            }
            long now = System.nanoTime();
            float dt = (now - lastTick) / 1.0e9f;
            lastTick = now;
            time += dt;
            if (time >= length) {
                if (loop) {
                    time = time % Math.max(0.05f, length);
                } else {
                    time = length;
                    playing = false;
                    playBtn.setText("\u25B6 Play");
                }
            }
            showTime();
            applyPose(time);
            if (playing) {
                handler.postDelayed(this, 33L);
            }
        }
    };

    private AnimatorUi(Activity act, Studio studio, Instance root) {
        this.act = act;
        this.studio = studio;
        this.root = root;
    }

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
                current.close(true);
            }
            AnimatorUi a = new AnimatorUi(act, studio, target);
            if (!a.collectParts()) {
                toast(act, "That selection has no parts to animate.");
                return;
            }
            current = a;
            a.loadSaved(null);
            if (a.keys.isEmpty()) {
                a.keys.put(0.0f, a.currentPose());
            }
            a.build();
        } catch (Throwable t) {
            toast(act, "Couldn't open the Animator: " + t);
        }
    }

    private static void toast(Activity a, String s) {
        Toast.makeText(a, s, Toast.LENGTH_LONG).show();
    }

    // ------------------------------------------------------------------ data

    private boolean collectParts() {
        List<Instance> all = new ArrayList<>();
        if (root.isA("BasePart")) {
            all.add(root);
        }
        root.descendants(all);
        Set<String> seen = new HashSet<>();
        for (Instance i : all) {
            if (!i.isA("BasePart") || !(i.get("CFrame") instanceof CFrame)) {
                continue;
            }
            String p = pathOf(i);
            if (!seen.add(p) || parts.size() >= MAX_PARTS) {
                skipped++;
                continue;
            }
            parts.add(i);
            paths.add(p);
            base.add((CFrame) i.get("CFrame"));
        }
        return !parts.isEmpty();
    }

    private String pathOf(Instance i) {
        StringBuilder sb = new StringBuilder();
        Instance cur = i;
        while (cur != null && cur != root) {
            if (sb.length() > 0) {
                sb.insert(0, "/");
            }
            sb.insert(0, cur.getName().replace("/", "_"));
            cur = cur.getParent();
        }
        return sb.toString();
    }

    private CFrame[] currentPose() {
        CFrame[] out = new CFrame[parts.size()];
        for (int i = 0; i < out.length; i++) {
            Object v = parts.get(i).get("CFrame");
            out[i] = v instanceof CFrame ? (CFrame) v : base.get(i);
        }
        return out;
    }

    private void applyPose(float t) {
        if (keys.isEmpty()) {
            return;
        }
        Map.Entry<Float, CFrame[]> lo = keys.floorEntry(t);
        Map.Entry<Float, CFrame[]> hi = keys.ceilingEntry(t);
        if (lo == null) {
            lo = hi;
        }
        if (hi == null) {
            hi = lo;
        }
        float span = hi.getKey() - lo.getKey();
        float a = span > 1.0e-4f ? (t - lo.getKey()) / span : 0.0f;
        for (int i = 0; i < parts.size(); i++) {
            CFrame c = a <= 0.0f ? lo.getValue()[i] : lo.getValue()[i].lerp(hi.getValue()[i], a);
            parts.get(i).set("CFrame", c, true);
        }
        studio.markDirty();
    }

    private void resetPose() {
        for (int i = 0; i < parts.size(); i++) {
            parts.get(i).set("CFrame", base.get(i), true);
        }
    }

    private static float snap(float t) {
        return Math.round(t * 20.0f) / 20.0f;
    }

    private float readLength() {
        float v = length;
        try {
            v = Float.parseFloat(etLength.getText().toString().trim());
        } catch (Throwable ignored) {
        }
        float last = keys.isEmpty() ? 0.0f : keys.lastKey();
        v = Math.max(Math.max(0.2f, last), Math.min(60.0f, v));
        length = v;
        etLength.setText(fmt(v));
        return v;
    }

    private static String fmt(float v) {
        return String.format(Locale.US, "%.2f", v);
    }

    private static double r4(float v) {
        if (Float.isNaN(v) || Float.isInfinite(v)) {
            return 0.0d;
        }
        return Math.round(v * 10000.0f) / 10000.0d;
    }

    private static JSONArray cfJson(CFrame c) throws Exception {
        float[] m = {c.getX(), c.getY(), c.getZ(), c.getR00(), c.getR01(), c.getR02(), c.getR10(), c.getR11(), c.getR12(), c.getR20(), c.getR21(), c.getR22()};
        JSONArray a = new JSONArray();
        for (float f : m) {
            a.put(r4(f));
        }
        return a;
    }

    private static CFrame cfFrom(JSONArray a) {
        return new CFrame((float) a.optDouble(0), (float) a.optDouble(1), (float) a.optDouble(2),
                (float) a.optDouble(3), (float) a.optDouble(4), (float) a.optDouble(5),
                (float) a.optDouble(6), (float) a.optDouble(7), (float) a.optDouble(8),
                (float) a.optDouble(9), (float) a.optDouble(10), (float) a.optDouble(11));
    }

    // ------------------------------------------------------------------ save / load

    private static String safeName(String s) {
        String n = s == null ? "" : s.trim().replaceAll("[^A-Za-z0-9_ -]", "");
        if (n.length() > 30) {
            n = n.substring(0, 30);
        }
        return n.length() == 0 ? "Anim1" : n;
    }

    private String buildSource() throws Exception {
        JSONObject o = new JSONObject();
        o.put("v", 1);
        o.put("name", animName);
        o.put("length", r4(length));
        o.put("loop", loop);
        JSONArray pj = new JSONArray();
        for (String p : paths) {
            pj.put(p);
        }
        o.put("parts", pj);
        JSONArray kj = new JSONArray();
        for (Map.Entry<Float, CFrame[]> e : keys.entrySet()) {
            JSONObject k = new JSONObject();
            k.put("t", r4(e.getKey()));
            JSONArray cfs = new JSONArray();
            for (CFrame c : e.getValue()) {
                cfs.put(cfJson(c));
            }
            k.put("cf", cfs);
            kj.put(k);
        }
        o.put("keys", kj);

        StringBuilder sb = new StringBuilder();
        sb.append(HEADER).append('\n').append(o.toString()).append("\n]=]\n");
        sb.append("-- Generated by the Mobile Studio Animator. Edit the animation there; changes made here are overwritten on save.\n");
        sb.append("local TweenService = game:GetService(\"TweenService\")\n");
        sb.append("local root = script.Parent\n");
        sb.append("local LOOP = ").append(loop ? "true" : "false").append('\n');
        sb.append("local LENGTH = ").append(r4(length)).append('\n');
        sb.append("local PARTS = {");
        for (int i = 0; i < paths.size(); i++) {
            sb.append(i == 0 ? "" : ", ").append(JSONObject.quote(paths.get(i)));
        }
        sb.append("}\nlocal KEYS = {\n");
        for (Map.Entry<Float, CFrame[]> e : keys.entrySet()) {
            sb.append("\t{t = ").append(r4(e.getKey())).append(", cf = {");
            CFrame[] arr = e.getValue();
            for (int i = 0; i < arr.length; i++) {
                JSONArray c = cfJson(arr[i]);
                sb.append(i == 0 ? "" : ", ").append('{');
                for (int j = 0; j < c.length(); j++) {
                    sb.append(j == 0 ? "" : ", ").append(c.get(j));
                }
                sb.append('}');
            }
            sb.append("}},\n");
        }
        sb.append("}\n\n");
        sb.append("local function find(path)\n"
                + "\tlocal o = root\n"
                + "\tfor name in string.gmatch(path, \"[^/]+\") do\n"
                + "\t\to = o and o:FindFirstChild(name)\n"
                + "\tend\n"
                + "\treturn o\n"
                + "end\n\n"
                + "local targets = {}\n"
                + "for i, p in ipairs(PARTS) do\n"
                + "\ttargets[i] = find(p)\n"
                + "end\n\n"
                + "local function cf(t)\n"
                + "\treturn CFrame.new(t[1], t[2], t[3], t[4], t[5], t[6], t[7], t[8], t[9], t[10], t[11], t[12])\n"
                + "end\n\n"
                + "local function run()\n"
                + "\tfor i, part in ipairs(targets) do\n"
                + "\t\tif part then part.CFrame = cf(KEYS[1].cf[i]) end\n"
                + "\tend\n"
                + "\tfor k = 1, #KEYS - 1 do\n"
                + "\t\tlocal a, b = KEYS[k], KEYS[k + 1]\n"
                + "\t\tlocal d = b.t - a.t\n"
                + "\t\tif d > 0 then\n"
                + "\t\t\tfor i, part in ipairs(targets) do\n"
                + "\t\t\t\tif part then\n"
                + "\t\t\t\t\tTweenService:Create(part, TweenInfo.new(d, Enum.EasingStyle.Linear), {CFrame = cf(b.cf[i])}):Play()\n"
                + "\t\t\t\tend\n"
                + "\t\t\tend\n"
                + "\t\t\ttask.wait(d)\n"
                + "\t\tend\n"
                + "\tend\n"
                + "\tlocal rest = LENGTH - KEYS[#KEYS].t\n"
                + "\tif rest > 0 then task.wait(rest) end\n"
                + "end\n\n"
                + "repeat run() until not LOOP\n");
        return sb.toString();
    }

    private Instance findScript(String name) {
        for (Instance c : root.getChildren()) {
            if (c.isA("Script") && ("Anim_" + name).equals(c.getName())) {
                return c;
            }
        }
        return null;
    }

    /** Loads a saved animation from root's Anim_ scripts. name == null loads the first one found. */
    private boolean loadSaved(String name) {
        try {
            Instance found = null;
            for (Instance c : root.getChildren()) {
                if (c.isA("Script") && c.getName().startsWith("Anim_")
                        && (name == null || c.getName().equals("Anim_" + name))) {
                    found = c;
                    break;
                }
            }
            if (found == null) {
                return false;
            }
            Object src = found.get("Source");
            if (!(src instanceof String)) {
                return false;
            }
            String s = (String) src;
            int a = s.indexOf(HEADER);
            int b = s.indexOf("\n]=]", a);
            if (a < 0 || b < 0) {
                return false;
            }
            JSONObject o = new JSONObject(s.substring(a + HEADER.length(), b).trim());
            JSONArray pj = o.getJSONArray("parts");
            int[] map = new int[pj.length()];
            for (int i = 0; i < map.length; i++) {
                map[i] = paths.indexOf(pj.getString(i));
            }
            TreeMap<Float, CFrame[]> loaded = new TreeMap<>();
            JSONArray kj = o.getJSONArray("keys");
            for (int k = 0; k < kj.length() && k < MAX_KEYS; k++) {
                JSONObject ko = kj.getJSONObject(k);
                CFrame[] pose = new CFrame[parts.size()];
                for (int i = 0; i < pose.length; i++) {
                    pose[i] = base.get(i);
                }
                JSONArray cfs = ko.getJSONArray("cf");
                for (int i = 0; i < cfs.length() && i < map.length; i++) {
                    if (map[i] >= 0) {
                        pose[map[i]] = cfFrom(cfs.getJSONArray(i));
                    }
                }
                loaded.put(snap((float) ko.getDouble("t")), pose);
            }
            if (loaded.isEmpty()) {
                return false;
            }
            keys.clear();
            keys.putAll(loaded);
            length = (float) o.optDouble("length", 2.0d);
            loop = o.optBoolean("loop", true);
            String n = o.optString("name", found.getName().substring(5));
            animName = safeName(n);
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    // ------------------------------------------------------------------ UI

    private void showTime() {
        timeLabel.setText(fmt(time) + " s / " + fmt(length) + " s");
        bar.setProgress(Math.round(1000.0f * time / Math.max(0.05f, length)));
    }

    private void refreshInfo() {
        StringBuilder ks = new StringBuilder();
        for (Float k : keys.keySet()) {
            ks.append(ks.length() == 0 ? "" : ", ").append(String.format(Locale.US, "%.2f", k));
        }
        int unanchored = 0;
        for (Instance p : parts) {
            if (Boolean.FALSE.equals(p.get("Anchored"))) {
                unanchored++;
            }
        }
        String extra = (skipped > 0 ? "\n" + skipped + " part(s) skipped (limit " + MAX_PARTS + " parts, or duplicate names)." : "")
                + (unanchored > 0 ? "\n" + unanchored + " part(s) are not Anchored. Anchor them so physics doesn't fight the animation." : "");
        info.setText(parts.size() + " part(s) \u2022 " + keys.size() + " keyframe(s): " + ks + extra);
    }

    private void say(String s) {
        status.setText(s);
    }

    private TextView btn(String label, final Runnable r) {
        final Ui ui = Ui.INSTANCE;
        return ui.button(act, label, ui.getHOVER(), new Function1<View, Unit>() {
            @Override
            public Unit invoke(View v) {
                try {
                    r.run();
                } catch (Throwable t) {
                    say("Error: " + t);
                }
                return Unit.INSTANCE;
            }
        });
    }

    private LinearLayout row(View... views) {
        LinearLayout l = new LinearLayout(act);
        l.setOrientation(LinearLayout.HORIZONTAL);
        for (View v : views) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, -2, 1.0f);
            lp.setMargins(Ui.INSTANCE.dp(act, 2.0f), Ui.INSTANCE.dp(act, 3.0f), Ui.INSTANCE.dp(act, 2.0f), Ui.INSTANCE.dp(act, 3.0f));
            l.addView(v, lp);
        }
        return l;
    }

    private void build() {
        final Ui ui = Ui.INSTANCE;
        LinearLayout content = new LinearLayout(act);
        content.setOrientation(LinearLayout.VERTICAL);
        int pad = ui.dp(act, 12.0f);
        content.setPadding(pad, pad, pad, pad);

        TextView title = ui.text(act, "Animator \u2014 " + root.getName(), 16.0f, ui.getTEXT(), true);
        content.addView(row(title, btn("Close", new Runnable() {
            @Override
            public void run() {
                close(true);
            }
        })));
        content.addView(ui.text(act, "Pose the parts with the normal tools, then tap Set key here. Drag the bar to preview.", 11.0f, ui.getDIM(), false));
        info = ui.text(act, "", 12.0f, ui.getTEXT(), false);
        info.setPadding(0, ui.dp(act, 6.0f), 0, ui.dp(act, 4.0f));
        content.addView(info);

        timeLabel = ui.text(act, "", 13.0f, ui.getTEXT(), true);
        content.addView(timeLabel);
        bar = new SeekBar(act);
        bar.setMax(1000);
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar s, int progress, boolean fromUser) {
                if (fromUser) {
                    time = snap(progress / 1000.0f * length);
                    timeLabel.setText(fmt(time) + " s / " + fmt(length) + " s");
                    applyPose(time);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar s) {
                stopPlay();
            }

            @Override
            public void onStopTrackingTouch(SeekBar s) {
            }
        });
        content.addView(bar);

        content.addView(row(
                btn("\u25C0 Key", new Runnable() {
                    @Override
                    public void run() {
                        Float k = keys.lowerKey(time - 0.001f);
                        goTo(k == null ? 0.0f : k);
                    }
                }),
                btn("Key \u25B6", new Runnable() {
                    @Override
                    public void run() {
                        Float k = keys.higherKey(time + 0.001f);
                        if (k != null) {
                            goTo(k);
                        }
                    }
                }),
                btn("Set key here", new Runnable() {
                    @Override
                    public void run() {
                        setKey();
                    }
                }),
                btn("Delete key", new Runnable() {
                    @Override
                    public void run() {
                        deleteKey();
                    }
                })));

        playBtn = btn("\u25B6 Play", new Runnable() {
            @Override
            public void run() {
                togglePlay();
            }
        });
        content.addView(row(playBtn, btn("Reset pose", new Runnable() {
            @Override
            public void run() {
                stopPlay();
                resetPose();
                time = 0.0f;
                showTime();
                say("Model put back to the pose it had when you opened the Animator.");
            }
        })));

        etName = ui.input(act, animName, "Animation name", false, false);
        etLength = ui.input(act, fmt(length), "Seconds", true, false);
        cbLoop = new CheckBox(act);
        cbLoop.setText("Loop");
        cbLoop.setTextColor(ui.getTEXT());
        cbLoop.setChecked(loop);
        content.addView(row(etName, etLength, cbLoop));

        content.addView(row(
                btn("Save to model", new Runnable() {
                    @Override
                    public void run() {
                        save();
                    }
                }),
                btn("Load", new Runnable() {
                    @Override
                    public void run() {
                        stopPlay();
                        String n = safeName(etName.getText().toString());
                        if (loadSaved(n)) {
                            etName.setText(animName);
                            etLength.setText(fmt(length));
                            cbLoop.setChecked(loop);
                            time = 0.0f;
                            applyPose(0.0f);
                            showTime();
                            refreshInfo();
                            say("Loaded Anim_" + n + ".");
                        } else {
                            say("No saved animation named Anim_" + n + " in this model.");
                        }
                    }
                })));

        status = ui.text(act, "Close puts the model back to its starting pose. Save first to keep your animation.", 11.0f, ui.getDIM(), false);
        status.setPadding(0, ui.dp(act, 6.0f), 0, 0);
        content.addView(status);

        ScrollView sv = new ScrollView(act);
        sv.addView(content);

        dialog = Dialogs.INSTANCE.themed(act).setView(sv).setCancelable(false).create();
        Window w = dialog.getWindow();
        if (w != null) {
            // not modal: touches outside the panel still reach the viewport, so you can move parts while it is open
            w.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
            w.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            w.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
            DisplayMetrics dm = act.getResources().getDisplayMetrics();
            w.setLayout(Math.min(dm.widthPixels, Math.max(ui.dp(act, 360.0f), dm.widthPixels * 6 / 10)),
                    Math.max(ui.dp(act, 260.0f), dm.heightPixels * 55 / 100));
        }
        dialog.show();
        showTime();
        refreshInfo();
    }

    private void goTo(float t) {
        stopPlay();
        time = snap(t);
        showTime();
        applyPose(time);
    }

    private void setKey() {
        stopPlay();
        readLength();
        float t = snap(time);
        if (!keys.containsKey(t) && keys.size() >= MAX_KEYS) {
            say("Limit reached: " + MAX_KEYS + " keyframes per animation.");
            return;
        }
        keys.put(t, currentPose());
        studio.markDirty();
        readLength();
        showTime();
        refreshInfo();
        say("Keyframe set at " + fmt(t) + " s.");
    }

    private void deleteKey() {
        stopPlay();
        float t = snap(time);
        if (t <= 0.0f) {
            say("The key at 0.00 s is the starting pose and can't be deleted. Set it again to change it.");
            return;
        }
        if (keys.remove(t) == null) {
            say("There is no keyframe at " + fmt(t) + " s. Use Key buttons to jump to one.");
            return;
        }
        refreshInfo();
        say("Deleted the keyframe at " + fmt(t) + " s.");
    }

    private void togglePlay() {
        if (playing) {
            stopPlay();
            return;
        }
        readLength();
        if (keys.size() < 2) {
            say("Add at least two keyframes (for example 0.00 s and 1.00 s) before playing.");
            return;
        }
        loop = cbLoop.isChecked();
        if (time >= length) {
            time = 0.0f;
        }
        playing = true;
        lastTick = System.nanoTime();
        playBtn.setText("\u25A0 Stop");
        handler.post(tick);
    }

    private void stopPlay() {
        if (playing) {
            playing = false;
            handler.removeCallbacks(tick);
            if (playBtn != null) {
                playBtn.setText("\u25B6 Play");
            }
        }
    }

    private void save() {
        stopPlay();
        readLength();
        loop = cbLoop.isChecked();
        animName = safeName(etName.getText().toString());
        etName.setText(animName);
        if (keys.size() < 2) {
            say("Add at least two keyframes before saving.");
            return;
        }
        if (parts.size() * keys.size() > MAX_ENTRIES) {
            say("Too big: " + parts.size() + " parts x " + keys.size() + " keys is over " + MAX_ENTRIES + ". Remove some keyframes.");
            return;
        }
        if (studio.isPlaying()) {
            say("Stop the playtest first.");
            return;
        }
        try {
            String src = buildSource();
            Instance script = findScript(animName);
            if (script == null) {
                script = InstanceFactory.INSTANCE.create("Script");
                script.setName("Anim_" + animName);
                script.setParent(root, -1);
            }
            script.set("Source", src, true);
            studio.markDirty();
            say("Saved as Script \"Anim_" + animName + "\" inside " + root.getName() + ". It plays when the game runs. Save the place to keep it.");
        } catch (Throwable t) {
            say("Couldn't save: " + t);
        }
    }

    private void close(boolean restore) {
        stopPlay();
        if (restore) {
            try {
                resetPose();
                studio.markDirty();
            } catch (Throwable ignored) {
            }
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

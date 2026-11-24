package com.goydevv.studrblx.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.OpenableColumns;
import android.text.InputType;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import com.goydevv.studrblx.core.OutputLog;
import com.goydevv.studrblx.net.AuthRequiredException;
import com.goydevv.studrblx.net.Http;
import com.goydevv.studrblx.net.RobloxApi;
import com.goydevv.studrblx.net.RobloxCloud;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import kotlin.Pair;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import org.json.JSONObject;

/**
 * Quick Publish: pick a local .rbxl / .rbxlx and send it straight to Roblox,
 * without loading it into the 3D viewport.
 *
 *  Update Game
 *    - "My experiences" lists every experience the signed-in account can edit
 *      (public AND private, plus group experiences), with icon and places.
 *    - Upload goes to Open Cloud:
 *      POST https://apis.roblox.com/universes/v1/{universeId}/places/{placeId}/versions?versionType=Published
 *      with the x-api-key header. Real upload progress is shown.
 *    - With no API key but a signed-in account, it falls back to the app's existing cookie publish route.
 *  Create New Game
 *    - .ROBLOSECURITY cookie + x-csrf-token (handled by RobloxCloud.authed).
 *
 * Success is only reported when Roblox answered with a 2xx status.
 */
public final class QuickPublish {
    /** Set by MainActivity; opens the system file picker. */
    public static ActivityResultLauncher<String[]> launcher;

    private static final Handler MAIN = new Handler(Looper.getMainLooper());
    private static final int MAX_BYTES = 100 * 1024 * 1024;
    private static Uri pickedUri;
    private static String pickedName = "";
    private static Runnable pickedListener;

    private QuickPublish() {
    }

    // ------------------------------------------------------------------ file picking

    public static void onPicked(Activity act, Uri uri) {
        if (uri == null) {
            return;
        }
        pickedUri = uri;
        pickedName = displayName(act, uri);
        OutputLog.INSTANCE.info("Quick Publish: selected " + pickedName);
        if (pickedListener != null) {
            pickedListener.run();
        }
    }

    private static String displayName(Context ctx, Uri uri) {
        try (Cursor c = ctx.getContentResolver().query(uri, new String[]{OpenableColumns.DISPLAY_NAME}, null, null, null)) {
            if (c != null && c.moveToFirst()) {
                String n = c.getString(0);
                if (n != null && n.length() > 0) {
                    return n;
                }
            }
        } catch (Exception ignored) {
        }
        String p = uri.getLastPathSegment();
        return p != null ? p : uri.toString();
    }

    private static byte[] readAll(Context ctx, Uri uri) throws IOException {
        try (InputStream in = ctx.getContentResolver().openInputStream(uri)) {
            if (in == null) {
                throw new IOException("Couldn't open the selected file.");
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream(1 << 20);
            byte[] buf = new byte[64 * 1024];
            int n;
            while ((n = in.read(buf)) != -1) {
                out.write(buf, 0, n);
                if (out.size() > MAX_BYTES) {
                    throw new IOException("File is larger than 100 MB, which Roblox won't accept.");
                }
            }
            return out.toByteArray();
        }
    }

    private static boolean looksLikePlace(byte[] b) {
        int n = Math.min(b.length, 96);
        String head = new String(b, 0, n, StandardCharsets.ISO_8859_1);
        return head.contains("<roblox");
    }

    private static boolean isXml(byte[] b) {
        int n = Math.min(b.length, 96);
        String head = new String(b, 0, n, StandardCharsets.ISO_8859_1);
        return head.contains("<roblox ") || head.contains("<roblox\n") || head.contains("<roblox xmlns");
    }

    private static long parseId(String s) {
        try {
            String t = s.trim();
            return t.length() == 0 ? 0L : Long.parseLong(t);
        } catch (NumberFormatException e) {
            return -1L;
        }
    }

    private static String describe(Throwable t) {
        StringBuilder sb = new StringBuilder();
        Throwable cur = t;
        int depth = 0;
        while (cur != null && depth < 4) {
            if (depth > 0) {
                sb.append("\n  caused by: ");
            }
            sb.append(cur.getClass().getSimpleName());
            if (cur.getMessage() != null) {
                sb.append(": ").append(cur.getMessage());
            }
            cur = cur.getCause() == cur ? null : cur.getCause();
            depth++;
        }
        if (t instanceof AuthRequiredException) {
            sb.append("\nSign in to Roblox (Account) or check the API key / .ROBLOSECURITY cookie, then try again.");
        }
        return sb.toString();
    }

    // ------------------------------------------------------------------ Open Cloud upload with real progress

    private interface Progress {
        void update(long done, long total);
    }

    private static String explainHttp(int code, String body) {
        String detail = body == null ? "" : body.trim();
        if (detail.length() > 300) {
            detail = detail.substring(0, 300) + "\u2026";
        }
        String why;
        switch (code) {
            case 400:
                why = "Roblox rejected the file (400). It may be corrupt, too large, or not a valid place file.";
                break;
            case 401:
                why = "The API key was not accepted (401). Check that it was copied correctly and hasn't expired.";
                break;
            case 403:
                why = "Permission denied (403). The API key needs the 'universe-places: write' permission for this experience, and its IP allow-list must include your current IP (or be 0.0.0.0/0).";
                break;
            case 404:
                why = "Universe or place not found (404). Check that the Place ID belongs to that experience.";
                break;
            case 409:
                why = "Roblox reported a conflict (409). Try again in a moment.";
                break;
            case 429:
                why = "Too many requests (429). Wait a minute and try again.";
                break;
            default:
                why = "Roblox returned HTTP " + code + ".";
        }
        return detail.length() > 0 ? why + "\nRoblox said: " + detail : why;
    }

    private static int uploadOpenCloud(String apiKey, long universeId, long placeId, byte[] bytes, Progress cb) throws IOException {
        String url = "https://apis.roblox.com/universes/v1/" + universeId + "/places/" + placeId + "/versions?versionType=Published";
        HttpURLConnection c = (HttpURLConnection) new URL(url).openConnection();
        try {
            c.setRequestMethod("POST");
            c.setDoOutput(true);
            c.setConnectTimeout(20000);
            c.setReadTimeout(120000);
            c.setRequestProperty("x-api-key", apiKey);
            c.setRequestProperty("Content-Type", isXml(bytes) ? "application/xml" : "application/octet-stream");
            c.setFixedLengthStreamingMode(bytes.length);
            try (OutputStream out = c.getOutputStream()) {
                int off = 0;
                while (off < bytes.length) {
                    int n = Math.min(32 * 1024, bytes.length - off);
                    out.write(bytes, off, n);
                    off += n;
                    cb.update(off, bytes.length);
                }
                out.flush();
            }
            int code = c.getResponseCode();
            InputStream is = code >= 400 ? c.getErrorStream() : c.getInputStream();
            String body = "";
            if (is != null) {
                try (InputStream in = is) {
                    ByteArrayOutputStream bo = new ByteArrayOutputStream();
                    byte[] buf = new byte[4096];
                    int n;
                    while ((n = in.read(buf)) != -1) {
                        bo.write(buf, 0, n);
                        if (bo.size() > 65536) {
                            break;
                        }
                    }
                    body = new String(bo.toByteArray(), StandardCharsets.UTF_8);
                }
            }
            if (code < 200 || code >= 300) {
                throw new IOException(explainHttp(code, body));
            }
            try {
                return new JSONObject(body).optInt("versionNumber", 0);
            } catch (Exception e) {
                return 0;
            }
        } finally {
            c.disconnect();
        }
    }

    // ------------------------------------------------------------------ small UI helpers

    private static void styleSeg(Context c, TextView t, boolean on) {
        GradientDrawable g = new GradientDrawable();
        g.setCornerRadius(Ui.INSTANCE.dp(c, 10.0f));
        g.setColor(on ? Ui.INSTANCE.getACCENT() : Ui.INSTANCE.getHOVER());
        t.setBackground(g);
        t.setTextColor(on ? 0xFFFFFFFF : Ui.INSTANCE.getDIM());
    }

    private static void styleRow(Context c, View row, boolean selected) {
        GradientDrawable g = new GradientDrawable();
        g.setCornerRadius(Ui.INSTANCE.dp(c, 10.0f));
        g.setColor(Ui.INSTANCE.getHOVER());
        g.setStroke(Ui.INSTANCE.dp(c, selected ? 2.0f : 1.0f), selected ? Ui.INSTANCE.getACCENT() : Ui.INSTANCE.getBORDER());
        row.setBackground(g);
    }

    private static TextView label(Context c, String s) {
        TextView t = Ui.INSTANCE.text(c, s, 12.0f, Ui.INSTANCE.getDIM(), true);
        t.setPadding(0, Ui.INSTANCE.dp(c, 12.0f), 0, Ui.INSTANCE.dp(c, 4.0f));
        return t;
    }

    private static TextView note(Context c, String s) {
        TextView t = Ui.INSTANCE.text(c, s, 11.0f, Ui.INSTANCE.getDIM(), false);
        t.setSingleLine(false);
        t.setPadding(0, Ui.INSTANCE.dp(c, 4.0f), 0, 0);
        return t;
    }

    private static void secret(EditText e) {
        e.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    private static LinearLayout.LayoutParams rowLp(Context c) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
        lp.topMargin = Ui.INSTANCE.dp(c, 6.0f);
        return lp;
    }

    /** Lets a list inside the dialog's ScrollView scroll on its own. */
    private static ScrollView innerScroll(Context c, LinearLayout content, int heightDp) {
        final ScrollView sv = new ScrollView(c);
        sv.addView(content);
        sv.setLayoutParams(new LinearLayout.LayoutParams(-1, Ui.INSTANCE.dp(c, (float) heightDp)));
        sv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent ev) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        return sv;
    }

    // ------------------------------------------------------------------ dialog

    public static void show(final Activity act) {
        final Ui ui = Ui.INSTANCE;
        final Context app = act.getApplicationContext();
        pickedUri = null;
        pickedName = "";

        final LinearLayout root = new LinearLayout(act);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(ui.dp(act, 18.0f), ui.dp(act, 14.0f), ui.dp(act, 18.0f), ui.dp(act, 6.0f));

        root.addView(ui.text(act, "Quick Publish", 18.0f, ui.getTEXT(), true));
        TextView sub = ui.text(act, "Send a .rbxl straight to Roblox. The file is not opened in the 3D viewport.", 12.0f, ui.getDIM(), false);
        sub.setSingleLine(false);
        sub.setPadding(0, ui.dp(act, 2.0f), 0, ui.dp(act, 8.0f));
        root.addView(sub);

        // ---- file
        root.addView(label(act, "FILE"));
        final TextView fileLabel = ui.text(act, "No file chosen", 13.0f, ui.getTEXT(), false);
        fileLabel.setSingleLine(false);
        root.addView(fileLabel);
        TextView chooseBtn = ui.button(act, "Choose .rbxl file\u2026", ui.getHOVER(), new Function1<View, Unit>() {
            @Override
            public Unit invoke(View v) {
                if (launcher == null) {
                    Toast.makeText(act, "File picker isn't ready yet.", Toast.LENGTH_SHORT).show();
                } else {
                    launcher.launch(new String[]{"*/*"});
                }
                return Unit.INSTANCE;
            }
        });
        root.addView(chooseBtn, rowLp(act));
        pickedListener = new Runnable() {
            @Override
            public void run() {
                fileLabel.setText(pickedName);
            }
        };

        // ---- mode switch
        root.addView(label(act, "MODE"));
        LinearLayout seg = new LinearLayout(act);
        seg.setOrientation(LinearLayout.HORIZONTAL);
        final TextView segUpdate = ui.text(act, "Update Game", 13.0f, ui.getTEXT(), true);
        final TextView segCreate = ui.text(act, "Create New Game", 13.0f, ui.getTEXT(), true);
        for (TextView t : new TextView[]{segUpdate, segCreate}) {
            t.setGravity(Gravity.CENTER);
            t.setPadding(0, ui.dp(act, 11.0f), 0, ui.dp(act, 11.0f));
        }
        LinearLayout.LayoutParams l1 = new LinearLayout.LayoutParams(0, -2, 1.0f);
        l1.rightMargin = ui.dp(act, 4.0f);
        seg.addView(segUpdate, l1);
        seg.addView(segCreate, new LinearLayout.LayoutParams(0, -2, 1.0f));
        root.addView(seg);

        // ================= Update Game =================
        final LinearLayout updateBox = new LinearLayout(act);
        updateBox.setOrientation(LinearLayout.VERTICAL);

        updateBox.addView(label(act, "YOUR EXPERIENCES"));
        final TextView expStatus = note(act, "Tap Load to list the experiences your signed-in account can edit (public and private).");
        updateBox.addView(expStatus);
        final LinearLayout expList = new LinearLayout(act);
        expList.setOrientation(LinearLayout.VERTICAL);
        final ScrollView expScroll = innerScroll(act, expList, 230);
        expScroll.setVisibility(View.GONE);
        updateBox.addView(expScroll, rowLp(act));
        final TextView loadBtn = ui.button(act, "Load my experiences", ui.getHOVER(), new Function1<View, Unit>() {
            @Override
            public Unit invoke(View v) {
                return Unit.INSTANCE;
            }
        });
        updateBox.addView(loadBtn, rowLp(act));

        final TextView placeLabel = label(act, "PLACE");
        placeLabel.setVisibility(View.GONE);
        updateBox.addView(placeLabel);
        final LinearLayout placeList = new LinearLayout(act);
        placeList.setOrientation(LinearLayout.VERTICAL);
        final ScrollView placeScroll = innerScroll(act, placeList, 150);
        placeScroll.setVisibility(View.GONE);
        updateBox.addView(placeScroll);

        updateBox.addView(label(act, "OPEN CLOUD API KEY (x-api-key)"));
        final EditText etKey = ui.input(act, RobloxApi.INSTANCE.apiKey(act), "API key with universe-places write", false, false);
        secret(etKey);
        updateBox.addView(etKey);
        updateBox.addView(note(act, "Needed to upload. Without a key, Quick Publish tries your signed-in account instead, which Roblox may reject."));

        final TextView manualToggle = ui.text(act, "Enter IDs manually \u25BE", 12.0f, ui.getACCENT(), true);
        manualToggle.setPadding(0, ui.dp(act, 14.0f), 0, ui.dp(act, 6.0f));
        updateBox.addView(manualToggle);
        final LinearLayout manualBox = new LinearLayout(act);
        manualBox.setOrientation(LinearLayout.VERTICAL);
        manualBox.setVisibility(View.GONE);
        final EditText etUniverse = ui.input(act, "", "Universe ID (optional, looked up if empty)", true, false);
        final EditText etPlace = ui.input(act, "", "Place ID", true, false);
        manualBox.addView(etUniverse);
        manualBox.addView(etPlace, rowLp(act));
        updateBox.addView(manualBox);
        root.addView(updateBox);

        // ================= Create New Game =================
        final LinearLayout createBox = new LinearLayout(act);
        createBox.setOrientation(LinearLayout.VERTICAL);
        createBox.addView(label(act, "NEW GAME NAME"));
        final EditText etName = ui.input(act, "", "Name of the new experience", false, false);
        createBox.addView(etName);
        createBox.addView(label(act, ".ROBLOSECURITY COOKIE"));
        final EditText etCookie = ui.input(act, RobloxApi.INSTANCE.cookie(act), "Paste your .ROBLOSECURITY value", false, false);
        secret(etCookie);
        createBox.addView(etCookie);
        createBox.addView(note(act, "Creates a new experience with your account, then uploads the file to its start place."));
        root.addView(createBox);

        final boolean[] createMode = {false};
        final Runnable applyMode = new Runnable() {
            @Override
            public void run() {
                updateBox.setVisibility(createMode[0] ? View.GONE : View.VISIBLE);
                createBox.setVisibility(createMode[0] ? View.VISIBLE : View.GONE);
                styleSeg(act, segUpdate, !createMode[0]);
                styleSeg(act, segCreate, createMode[0]);
            }
        };
        segUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createMode[0] = false;
                applyMode.run();
            }
        });
        segCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createMode[0] = true;
                applyMode.run();
            }
        });
        applyMode.run();
        manualToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean open = manualBox.getVisibility() == View.VISIBLE;
                manualBox.setVisibility(open ? View.GONE : View.VISIBLE);
                manualToggle.setText(open ? "Enter IDs manually \u25BE" : "Enter IDs manually \u25B4");
            }
        });

        // ---- progress + status
        final ProgressBar bar = new ProgressBar(act, null, android.R.attr.progressBarStyleHorizontal);
        bar.setMax(100);
        bar.setVisibility(View.GONE);
        LinearLayout.LayoutParams barLp = new LinearLayout.LayoutParams(-1, ui.dp(act, 8.0f));
        barLp.topMargin = ui.dp(act, 14.0f);
        root.addView(bar, barLp);
        final TextView status = ui.text(act, "", 12.0f, ui.getDIM(), false);
        status.setSingleLine(false);
        status.setTextIsSelectable(true);
        status.setPadding(0, ui.dp(act, 8.0f), 0, ui.dp(act, 4.0f));
        root.addView(status);

        // selection state
        final RobloxCloud.Game[] selGame = {null};
        final long[] selPlace = {0L};
        final ExecutorService iconPool = Executors.newFixedThreadPool(3);

        final Runnable[] loadPlacesFor = new Runnable[1];
        final Runnable[] loadExperiences = new Runnable[1];

        // ---- places for the chosen experience
        final RobloxCloud.Game[] placeRequest = {null};
        loadPlacesFor[0] = new Runnable() {
            @Override
            public void run() {
                final RobloxCloud.Game g = placeRequest[0];
                if (g == null) {
                    return;
                }
                placeLabel.setVisibility(View.VISIBLE);
                placeScroll.setVisibility(View.VISIBLE);
                placeList.removeAllViews();
                placeList.addView(note(act, "Loading places\u2026"));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<RobloxCloud.Place> places = null;
                        String err = null;
                        try {
                            places = RobloxCloud.INSTANCE.places(app, g);
                        } catch (Throwable t) {
                            err = describe(t);
                        }
                        final List<RobloxCloud.Place> result = places;
                        final String error = err;
                        MAIN.post(new Runnable() {
                            @Override
                            public void run() {
                                if (selGame[0] != g) {
                                    return;
                                }
                                placeList.removeAllViews();
                                List<RobloxCloud.Place> list = new ArrayList<>();
                                if (result != null) {
                                    list.addAll(result);
                                }
                                boolean hasRoot = false;
                                for (RobloxCloud.Place p : list) {
                                    if (p.getId() == g.getRootPlaceId()) {
                                        hasRoot = true;
                                    }
                                }
                                if (!hasRoot && g.getRootPlaceId() > 0) {
                                    list.add(0, new RobloxCloud.Place(g.getRootPlaceId(), "Start place", g.getUniverseId()));
                                }
                                if (list.isEmpty()) {
                                    placeList.addView(note(act, error != null ? "Couldn't load places:\n" + error : "No places found."));
                                    return;
                                }
                                final List<View> rows = new ArrayList<>();
                                for (final RobloxCloud.Place p : list) {
                                    LinearLayout row = new LinearLayout(act);
                                    row.setOrientation(LinearLayout.VERTICAL);
                                    row.setPadding(ui.dp(act, 12.0f), ui.dp(act, 8.0f), ui.dp(act, 12.0f), ui.dp(act, 8.0f));
                                    boolean isStart = p.getId() == g.getRootPlaceId();
                                    row.addView(ui.text(act, p.getName() + (isStart ? "  (start place)" : ""), 13.0f, ui.getTEXT(), true));
                                    row.addView(ui.text(act, "Place ID " + p.getId(), 11.0f, ui.getDIM(), false));
                                    styleRow(act, row, false);
                                    rows.add(row);
                                    placeList.addView(row, rowLp(act));
                                    final View rowRef = row;
                                    row.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            selPlace[0] = p.getId();
                                            for (View r : rows) {
                                                styleRow(act, r, r == rowRef);
                                            }
                                        }
                                    });
                                    if (isStart) {
                                        selPlace[0] = p.getId();
                                        styleRow(act, row, true);
                                    }
                                }
                                if (selPlace[0] == 0L && !rows.isEmpty()) {
                                    selPlace[0] = list.get(0).getId();
                                    styleRow(act, rows.get(0), true);
                                }
                            }
                        });
                    }
                }, "QuickPublish-places").start();
            }
        };

        // ---- list the user's experiences (public + private + group)
        loadExperiences[0] = new Runnable() {
            @Override
            public void run() {
                loadBtn.setEnabled(false);
                expStatus.setText("Loading your experiences\u2026");
                expStatus.setTextColor(ui.getDIM());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<RobloxCloud.Game> games = null;
                        Map<Long, String> icons = new HashMap<>();
                        String err = null;
                        try {
                            RobloxCloud.User u = RobloxCloud.INSTANCE.cachedUser(app);
                            if (u == null) {
                                u = RobloxCloud.INSTANCE.currentUser(app);
                            }
                            games = RobloxCloud.INSTANCE.games(app, u, true);
                            List<Long> ids = new ArrayList<>();
                            for (RobloxCloud.Game g : games) {
                                ids.add(Long.valueOf(g.getUniverseId()));
                            }
                            icons = RobloxCloud.INSTANCE.gameIcons(ids);
                        } catch (Throwable t) {
                            err = describe(t);
                            OutputLog.INSTANCE.error("Quick Publish: couldn't load experiences: " + err);
                        }
                        final List<RobloxCloud.Game> result = games;
                        final Map<Long, String> iconMap = icons;
                        final String error = err;
                        MAIN.post(new Runnable() {
                            @Override
                            public void run() {
                                loadBtn.setEnabled(true);
                                loadBtn.setText("Refresh experiences");
                                expList.removeAllViews();
                                if (result == null || result.isEmpty()) {
                                    expScroll.setVisibility(View.GONE);
                                    expStatus.setTextColor(error != null ? Ui.ERROR : ui.getDIM());
                                    expStatus.setText(error != null
                                            ? "Couldn't load experiences:\n" + error + "\n\nYou can still enter IDs manually below."
                                            : "No experiences found for this account. Sign in under Account, or enter IDs manually below.");
                                    return;
                                }
                                expStatus.setTextColor(ui.getDIM());
                                expStatus.setText(result.size() + " experience(s). Tap one to choose its place.");
                                expScroll.setVisibility(View.VISIBLE);
                                final List<View> rows = new ArrayList<>();
                                for (final RobloxCloud.Game g : result) {
                                    LinearLayout row = new LinearLayout(act);
                                    row.setOrientation(LinearLayout.HORIZONTAL);
                                    row.setGravity(Gravity.CENTER_VERTICAL);
                                    row.setPadding(ui.dp(act, 10.0f), ui.dp(act, 8.0f), ui.dp(act, 10.0f), ui.dp(act, 8.0f));
                                    final ImageView icon = new ImageView(act);
                                    icon.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                    GradientDrawable ph = new GradientDrawable();
                                    ph.setCornerRadius(ui.dp(act, 8.0f));
                                    ph.setColor(ui.getBORDER());
                                    icon.setBackground(ph);
                                    icon.setClipToOutline(true);
                                    row.addView(icon, new LinearLayout.LayoutParams(ui.dp(act, 44.0f), ui.dp(act, 44.0f)));
                                    LinearLayout texts = new LinearLayout(act);
                                    texts.setOrientation(LinearLayout.VERTICAL);
                                    texts.setPadding(ui.dp(act, 10.0f), 0, 0, 0);
                                    TextView nameTv = ui.text(act, g.getName(), 14.0f, ui.getTEXT(), true);
                                    texts.addView(nameTv);
                                    String privacy = g.getPrivacyType();
                                    String shown = privacy == null || privacy.length() == 0 ? "Unknown" : privacy;
                                    String owner = g.getOwner() == null || g.getOwner().length() == 0 ? "" : "  \u2022  " + g.getOwner();
                                    texts.addView(ui.text(act, shown + owner + "  \u2022  start place " + g.getRootPlaceId(), 11.0f, ui.getDIM(), false));
                                    row.addView(texts, new LinearLayout.LayoutParams(0, -2, 1.0f));
                                    styleRow(act, row, false);
                                    rows.add(row);
                                    expList.addView(row, rowLp(act));
                                    final View rowRef = row;
                                    row.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            selGame[0] = g;
                                            selPlace[0] = 0L;
                                            for (View r : rows) {
                                                styleRow(act, r, r == rowRef);
                                            }
                                            placeRequest[0] = g;
                                            loadPlacesFor[0].run();
                                        }
                                    });
                                    if (selGame[0] != null && selGame[0].getUniverseId() == g.getUniverseId()) {
                                        selGame[0] = g;
                                        styleRow(act, row, true);
                                    }
                                    final String url = iconMap.get(Long.valueOf(g.getUniverseId()));
                                    if (url != null && url.length() > 0) {
                                        iconPool.submit(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    byte[] data = Http.INSTANCE.get(url, new HashMap<String, String>(), 2_000_000);
                                                    final Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                                                    if (bmp != null) {
                                                        MAIN.post(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                icon.setImageBitmap(bmp);
                                                            }
                                                        });
                                                    }
                                                } catch (Throwable ignored) {
                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    }
                }, "QuickPublish-experiences").start();
            }
        };
        loadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadExperiences[0].run();
            }
        });

        ScrollView scroll = new ScrollView(act);
        scroll.addView(root);

        final AlertDialog dlg = Dialogs.INSTANCE.themed(act)
                .setView(scroll)
                .setPositiveButton("Publish", null)
                .setNegativeButton("Close", null)
                .create();
        dlg.setOnDismissListener(new android.content.DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(android.content.DialogInterface d) {
                pickedListener = null;
                iconPool.shutdownNow();
            }
        });
        dlg.show();
        final android.widget.Button pub = dlg.getButton(AlertDialog.BUTTON_POSITIVE);
        pub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final boolean create = createMode[0];
                final String apiKey = etKey.getText().toString().trim();
                final String cookie = etCookie.getText().toString().trim();
                final String gameName = etName.getText().toString().trim();
                final Uri uri = pickedUri;
                final String fileName = pickedName;

                // destination for Update Game: list selection wins, manual IDs are the fallback
                long u = 0L;
                long p = 0L;
                if (!create) {
                    if (selGame[0] != null && selPlace[0] > 0) {
                        u = selGame[0].getUniverseId();
                        p = selPlace[0];
                    } else {
                        u = parseId(etUniverse.getText().toString());
                        p = parseId(etPlace.getText().toString());
                    }
                }
                final long universeId = u;
                final long placeId = p;

                String problem = null;
                if (uri == null) {
                    problem = "Choose a .rbxl file first.";
                } else if (!create && selGame[0] != null && selPlace[0] <= 0) {
                    problem = "Choose which place to update.";
                } else if (!create && placeId <= 0) {
                    problem = "Pick an experience and place from the list, or enter a Place ID under \"Enter IDs manually\".";
                } else if (!create && universeId < 0) {
                    problem = "Universe ID must be a number (or leave it empty).";
                } else if (create && gameName.length() == 0) {
                    problem = "Enter a name for the new game.";
                } else if (create && cookie.length() == 0) {
                    problem = "Create New Game needs your .ROBLOSECURITY cookie.";
                }
                if (problem != null) {
                    status.setTextColor(Ui.ERROR);
                    status.setText(problem);
                    return;
                }
                pub.setEnabled(false);
                bar.setVisibility(View.VISIBLE);
                bar.setIndeterminate(true);
                status.setTextColor(ui.getDIM());
                status.setText("Reading file\u2026");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            byte[] bytes = readAll(app, uri);
                            if (!looksLikePlace(bytes)) {
                                throw new IOException("\"" + fileName + "\" doesn't look like a Roblox place file (.rbxl / .rbxlx).");
                            }
                            OutputLog.INSTANCE.info("Quick Publish: read " + fileName + " (" + (bytes.length / 1024) + " KB)");
                            if (!create) {
                                String version;
                                if (apiKey.length() > 0) {
                                    RobloxApi.INSTANCE.setApiKey(app, apiKey);
                                    long uni = universeId;
                                    if (uni <= 0) {
                                        post(status, bar, "Looking up the experience for place " + placeId + "\u2026", false, false, -1);
                                        uni = RobloxCloud.INSTANCE.universeOf(placeId);
                                        if (uni <= 0) {
                                            throw new IOException("Couldn't work out which experience place " + placeId + " belongs to. Enter the Universe ID too, or pick the experience from the list.");
                                        }
                                    }
                                    OutputLog.INSTANCE.info("Quick Publish: POST apis.roblox.com/universes/v1/" + uni + "/places/" + placeId + "/versions?versionType=Published");
                                    final String sizeTxt = (bytes.length / 1024) + " KB";
                                    int v = uploadOpenCloud(apiKey, uni, placeId, bytes, new Progress() {
                                        @Override
                                        public void update(long done, long total) {
                                            int pct = (int) (done * 100L / Math.max(1L, total));
                                            post(status, bar, "Uploading " + sizeTxt + "\u2026 " + pct + "%", false, false, pct);
                                        }
                                    });
                                    post(status, bar, "Waiting for Roblox to confirm\u2026", false, false, -1);
                                    version = v > 0 ? "version " + v : "accepted by Roblox";
                                } else {
                                    post(status, bar, "No API key, so trying your signed-in account\u2026", false, false, -1);
                                    version = RobloxCloud.INSTANCE.publish(app, universeId, placeId, bytes, true);
                                }
                                String done = "Published to place " + placeId + " (" + version + ").";
                                OutputLog.INSTANCE.info("Quick Publish: " + done);
                                post(status, bar, done, true, true, 100);
                            } else {
                                RobloxApi.INSTANCE.setCookie(app, cookie);
                                RobloxCloud.INSTANCE.resetSession();
                                post(status, bar, "Creating \"" + gameName + "\"\u2026", false, false, -1);
                                Pair<RobloxCloud.Game, RobloxCloud.Place> made = RobloxCloud.INSTANCE.createGame(app, gameName, 0L, "");
                                long newUniverse = made.getFirst().getUniverseId();
                                long newPlace = made.getSecond().getId();
                                OutputLog.INSTANCE.info("Quick Publish: created game \"" + gameName + "\" (universe " + newUniverse + ", place " + newPlace + ")");
                                post(status, bar, "Created (universe " + newUniverse + ", place " + newPlace + "). Uploading file\u2026", false, false, -1);
                                try {
                                    String res = RobloxCloud.INSTANCE.publish(app, newUniverse, newPlace, bytes, true);
                                    String done = "Created \"" + gameName + "\" and published the file (" + res + ").\nUniverse " + newUniverse + ", Place " + newPlace + ".";
                                    OutputLog.INSTANCE.info("Quick Publish: " + done);
                                    post(status, bar, done, true, true, 100);
                                } catch (Throwable up) {
                                    String msg = "The game was created (universe " + newUniverse + ", place " + newPlace + ") but uploading the file failed:\n" + describe(up)
                                            + "\n\nYou can still use Update Game with that Place ID and an API key that covers the new experience.";
                                    OutputLog.INSTANCE.error("Quick Publish: " + msg);
                                    post(status, bar, msg, true, false, 0);
                                }
                                // the new game should show up in the list
                                MAIN.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        loadExperiences[0].run();
                                    }
                                });
                            }
                        } catch (Throwable t) {
                            String msg = describe(t);
                            OutputLog.INSTANCE.error("Quick Publish failed: " + msg);
                            post(status, bar, msg, true, false, 0);
                        } finally {
                            MAIN.post(new Runnable() {
                                @Override
                                public void run() {
                                    pub.setEnabled(true);
                                }
                            });
                        }
                    }
                }, "QuickPublish").start();
            }
        });
    }

    /** percent &lt; 0 = indeterminate. */
    private static void post(final TextView status, final ProgressBar bar, final String msg, final boolean finished, final boolean ok, final int percent) {
        MAIN.post(new Runnable() {
            @Override
            public void run() {
                status.setTextColor(finished ? (ok ? Ui.PLAY : Ui.ERROR) : Ui.INSTANCE.getDIM());
                status.setText(msg);
                if (finished) {
                    bar.setIndeterminate(false);
                    bar.setProgress(ok ? 100 : 0);
                } else if (percent < 0) {
                    bar.setIndeterminate(true);
                } else {
                    bar.setIndeterminate(false);
                    bar.setProgress(percent);
                }
            }
        });
    }
}

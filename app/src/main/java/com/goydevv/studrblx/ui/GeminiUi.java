package com.goydevv.studrblx.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.goydevv.studrblx.core.Studio;
import com.goydevv.studrblx.model.Instance;
import com.goydevv.studrblx.model.InstanceFactory;
import com.goydevv.studrblx.net.GeminiClient;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kotlin.Unit;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Gemini assistant panel (File > Gemini AI). Uses the user's own free Gemini API key.
 *
 * - The key and model name are stored in this app's private preferences only (excluded from backups).
 * - Nothing is sent until the user taps Send. The first time, a notice explains what is sent to Google.
 * - The reply can be copied, or its last code block inserted as a new Script. Nothing is run or changed automatically.
 */
public final class GeminiUi {
    private static final String PREFS = "gemini_ai";
    private static final String SYSTEM = "You are Gemini, the assistant built into Roblox Mobile Studio, a phone editor for Roblox places. "
            + "Help with Luau scripting and building. Answers are read on a small phone screen, so be brief and clear. "
            + "Put code in ```lua fenced blocks. Use only real Roblox APIs, and say so when you are not sure.";
    private static final int MAX_CONTEXT = 12000;
    private static final int MAX_TURNS = 16;
    private static final Pattern CODE = Pattern.compile("```(?:lua|luau)?[ \\t]*\\r?\\n(.*?)```", Pattern.DOTALL);
    // cached daemon threads: a request stuck on a bad network can't block the next request or the connection test
    private static final ExecutorService NET = Executors.newCachedThreadPool(r -> {
        Thread t = new Thread(r, "gemini-net");
        t.setDaemon(true);
        return t;
    });
    private static final JSONArray HISTORY = new JSONArray();
    private static int lastX = Integer.MIN_VALUE;
    private static int lastY = Integer.MIN_VALUE;
    private static GeminiUi current;

    private final Activity act;
    private final Studio studio;
    private final Handler main = new Handler(Looper.getMainLooper());
    private AlertDialog dialog;
    private LinearLayout chat;
    private ScrollView chatScroll;
    private EditText input;
    private TextView sendBtn;
    private TextView attachBtn;
    private boolean attach;
    private boolean pending;
    private int reqId;
    private TextView waitingView;
    private static final int TIMEOUT_MS = 45000;
    private boolean closed;
    private String lastReply = "";

    private GeminiUi(Activity act, Studio studio) {
        this.act = act;
        this.studio = studio;
    }

    public static void show(Activity act, Studio studio) {
        try {
            if (current != null) {
                current.close();
            }
            GeminiUi g = new GeminiUi(act, studio);
            current = g;
            g.build();
        } catch (Throwable t) {
            Toast.makeText(act, "Couldn't open Gemini: " + t, Toast.LENGTH_LONG).show();
        }
    }

    // ------------------------------------------------------------------ settings storage

    private SharedPreferences prefs() {
        return act.getApplicationContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    private String key() {
        return prefs().getString("key", "");
    }

    private String model() {
        // empty means automatic: the app picks a working Gemini model itself
        String m = prefs().getString("model", "");
        return GeminiClient.validModel(m) && !GeminiClient.DEFAULT_MODEL.equals(m) ? m : "";
    }

    // ------------------------------------------------------------------ UI

    private TextView btn(String label, final Runnable r) {
        final Ui ui = Ui.INSTANCE;
        return ui.button(act, label, ui.getHOVER(), v -> {
            try {
                r.run();
            } catch (Throwable t) {
                addMessage("Error: " + t, false);
            }
            return Unit.INSTANCE;
        });
    }

    private LinearLayout row(View... views) {
        LinearLayout l = new LinearLayout(act);
        l.setOrientation(LinearLayout.HORIZONTAL);
        l.setGravity(Gravity.CENTER_VERTICAL);
        for (View v : views) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, -2, 1.0f);
            lp.setMargins(Ui.INSTANCE.dp(act, 2.0f), Ui.INSTANCE.dp(act, 2.0f), Ui.INSTANCE.dp(act, 2.0f), Ui.INSTANCE.dp(act, 2.0f));
            l.addView(v, lp);
        }
        return l;
    }

    private void build() {
        final Ui ui = Ui.INSTANCE;
        LinearLayout content = new LinearLayout(act);
        content.setOrientation(LinearLayout.VERTICAL);
        int pad = ui.dp(act, 8.0f);
        content.setPadding(pad, pad, pad, pad);

        final TextView title = ui.text(act, "\u2630 Gemini AI", 14.0f, ui.getTEXT(), true);
        title.setPadding(0, ui.dp(act, 8.0f), ui.dp(act, 8.0f), ui.dp(act, 8.0f));
        LinearLayout bar = new LinearLayout(act);
        bar.setOrientation(LinearLayout.HORIZONTAL);
        bar.setGravity(Gravity.CENTER_VERTICAL);
        bar.addView(title, new LinearLayout.LayoutParams(0, -2, 1.0f));
        TextView gear = btn("\u2699", this::settingsDialog);
        TextView close = btn("\u2715", this::close);
        for (View v : new View[]{gear, close}) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-2, -2);
            lp.setMargins(ui.dp(act, 2.0f), 0, ui.dp(act, 2.0f), 0);
            bar.addView(v, lp);
        }
        content.addView(bar);

        chat = new LinearLayout(act);
        chat.setOrientation(LinearLayout.VERTICAL);
        chatScroll = new ScrollView(act);
        chatScroll.addView(chat);
        content.addView(chatScroll, new LinearLayout.LayoutParams(-1, 0, 1.0f));

        attachBtn = btn("Script: off", this::toggleAttach);
        content.addView(row(
                attachBtn,
                btn("Insert code", this::insertCode),
                btn("Copy", this::copyReply),
                btn("Clear", this::clearChat)));

        input = ui.input(act, "", "Ask about Luau or your game\u2026", false, true);
        input.setMaxLines(4);
        sendBtn = btn("Send", this::send);
        LinearLayout inRow = new LinearLayout(act);
        inRow.setOrientation(LinearLayout.HORIZONTAL);
        inRow.setGravity(Gravity.CENTER_VERTICAL);
        inRow.addView(input, new LinearLayout.LayoutParams(0, -2, 1.0f));
        LinearLayout.LayoutParams slp = new LinearLayout.LayoutParams(-2, -2);
        slp.setMargins(ui.dp(act, 4.0f), 0, 0, 0);
        inRow.addView(sendBtn, slp);
        content.addView(inRow);

        dialog = Dialogs.INSTANCE.themed(act).setView(content).setCancelable(false).create();
        Window w = dialog.getWindow();
        if (w != null) {
            // not modal: touches outside the panel still reach the viewport
            w.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
            w.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            WindowManager.LayoutParams glp = w.getAttributes();
            glp.gravity = Gravity.TOP | Gravity.LEFT;
            w.setAttributes(glp);
        }
        dialog.show();
        layoutWindow();
        enableDrag(title);

        if (HISTORY.length() == 0) {
            addMessage(key().length() == 0
                    ? "Welcome! Tap the \u2699 button and paste a free Gemini API key (get one at aistudio.google.com/apikey), then ask me anything about Luau or your game."
                    : "Ask me about Luau or your game. Turn on \"Script\" to include the script you have selected in the Explorer.", false);
        } else {
            addMessage("Your earlier conversation is kept (" + HISTORY.length() + " messages). Tap Clear to start over.", false);
        }
    }

    private void layoutWindow() {
        Window w = dialog == null ? null : dialog.getWindow();
        if (w == null) {
            return;
        }
        Ui ui = Ui.INSTANCE;
        DisplayMetrics dm = act.getResources().getDisplayMetrics();
        boolean landscape = dm.widthPixels > dm.heightPixels;
        int ww = Math.min(dm.widthPixels, Math.max(ui.dp(act, 300.0f), dm.widthPixels * (landscape ? 44 : 96) / 100));
        int hh = Math.min(dm.heightPixels, Math.max(ui.dp(act, 240.0f), dm.heightPixels * (landscape ? 72 : 55) / 100));
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.gravity = Gravity.TOP | Gravity.LEFT;
        lp.width = ww;
        lp.height = hh;
        lp.x = lastX == Integer.MIN_VALUE ? dm.widthPixels - ww - ui.dp(act, 8.0f) : lastX;
        lp.y = lastY == Integer.MIN_VALUE ? ui.dp(act, 8.0f) : lastY;
        lp.x = Math.max(-(ww - ui.dp(act, 60.0f)), Math.min(dm.widthPixels - ui.dp(act, 60.0f), lp.x));
        lp.y = Math.max(0, Math.min(dm.heightPixels - ui.dp(act, 40.0f), lp.y));
        w.setAttributes(lp);
    }

    /** Drag the panel by its title so it can be moved off the viewport. */
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

    private TextView addMessage(String text, boolean user) {
        Ui ui = Ui.INSTANCE;
        TextView t = ui.text(act, text, 13.0f, ui.getTEXT(), false);
        t.setTextIsSelectable(true);
        int p = ui.dp(act, 8.0f);
        t.setPadding(p, p, p, p);
        GradientDrawable bg = new GradientDrawable();
        bg.setCornerRadius(ui.dp(act, 8.0f));
        bg.setColor(user ? ui.getHOVER() : 0x22FFFFFF);
        t.setBackground(bg);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
        lp.setMargins(user ? ui.dp(act, 24.0f) : 0, ui.dp(act, 3.0f), user ? 0 : ui.dp(act, 24.0f), ui.dp(act, 3.0f));
        chat.addView(t, lp);
        chatScroll.post(() -> chatScroll.fullScroll(View.FOCUS_DOWN));
        return t;
    }

    // ------------------------------------------------------------------ actions

    private void toggleAttach() {
        attach = !attach;
        attachBtn.setText(attach ? "Script: on" : "Script: off");
    }

    private Instance selectedScript() {
        for (Instance i : new ArrayList<>(studio.getSelection())) {
            if (i.isA("Script") || i.isA("LocalScript") || i.isA("ModuleScript")) {
                return i;
            }
        }
        return null;
    }

    private void send() {
        if (pending) {
            cancelRequest();
            return;
        }
        final String typed = input.getText().toString().trim();
        if (typed.length() == 0) {
            return;
        }
        if (key().length() == 0) {
            settingsDialog();
            return;
        }
        if (!prefs().getBoolean("notice", false)) {
            Dialogs.INSTANCE.themed(act).setTitle("Before you use Gemini")
                    .setMessage("Your messages, and the script you attach, are sent to Google's Gemini API using your own API key. "
                            + "On Google's free tier, Google may use what you send to improve its products, so don't send passwords or private data. "
                            + "The key stays on this phone.")
                    .setPositiveButton("I understand", (d, w) -> {
                        prefs().edit().putBoolean("notice", true).apply();
                        send();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            return;
        }
        String shown = typed;
        String sent = typed;
        if (attach) {
            Instance s = selectedScript();
            if (s == null) {
                addMessage("Select a Script, LocalScript or ModuleScript in the Explorer first, or turn \"Script\" off.", false);
                return;
            }
            Object src = s.get("Source");
            String code = src instanceof String ? (String) src : "";
            boolean cut = code.length() > MAX_CONTEXT;
            if (cut) {
                code = code.substring(0, MAX_CONTEXT);
            }
            sent = "The user's script \"" + s.getName() + "\" (" + s.getClassName() + ")"
                    + (cut ? " (cut to the first " + MAX_CONTEXT + " characters)" : "") + ":\n```lua\n" + code + "\n```\n\nRequest: " + typed;
            shown = typed + "\n[attached: " + s.getName() + "]";
        }
        input.setText("");
        addMessage(shown, true);
        waitingView = addMessage("Asking Gemini\u2026 (up to " + (TIMEOUT_MS / 1000) + " seconds)", false);
        pending = true;
        sendBtn.setText("Stop");
        try {
            HISTORY.put(turn("user", sent));
        } catch (Throwable ignored) {
        }
        final JSONArray snapshot = copyHistory();
        final String apiKey = key();
        final String mdl = model();
        final int id = ++reqId;
        // watchdog: some networks (VPN, Private DNS, firewall) never answer, and DNS has no timeout of its own
        main.postDelayed(() -> {
            if (pending && reqId == id) {
                finish("Gemini didn't answer in " + (TIMEOUT_MS / 1000) + " seconds, so I stopped waiting. "
                        + "Check your internet, VPN or Private DNS. Tap \u2699 and then Test connection to see where it fails.", false);
            }
        }, (long) TIMEOUT_MS);
        NET.execute(() -> {
            String out = "Something went wrong.";
            boolean ok = false;
            String[] tries = mdl.length() > 0 ? new String[]{mdl} : GeminiClient.FALLBACKS;
            StringBuilder tried = new StringBuilder();
            for (String m : tries) {
                tried.append(tried.length() == 0 ? "" : ", ").append(m);
                boolean next = false;
                for (int attempt = 0; attempt < 2 && !ok; attempt++) {
                    try {
                        out = GeminiClient.generate(apiKey, m, SYSTEM, snapshot);
                        ok = true;
                    } catch (GeminiClient.GeminiException e) {
                        out = e.getMessage();
                        if (e.code >= 500 && attempt == 0) {
                            try {
                                Thread.sleep(1500L);
                            } catch (InterruptedException ie) {
                                break;
                            }
                            continue;
                        }
                        next = e.tryNextModel();
                        break;
                    } catch (Throwable t) {
                        out = "Something went wrong.";
                        break;
                    }
                }
                if (ok || !next) {
                    break;
                }
            }
            if (!ok && tries.length > 1 && tried.indexOf(",") >= 0) {
                out = out + "\n(models tried: " + tried + ")";
            }
            final String text = out;
            final boolean good = ok;
            main.post(() -> {
                if (reqId == id && pending) {
                    finish(text, good);
                }
            });
        });
    }

    /** Ends the current request: shows the reply or error in the chat and re-enables Send. */
    private void finish(String text, boolean good) {
        pending = false;
        reqId++;
        if (good) {
            remember(text);
        } else {
            dropLastUser();
        }
        if (closed) {
            return;
        }
        if (waitingView != null) {
            chat.removeView(waitingView);
            waitingView = null;
        }
        sendBtn.setText("Send");
        addMessage(text, false);
        if (good) {
            lastReply = text;
        }
    }

    private void cancelRequest() {
        if (!pending) {
            return;
        }
        pending = false;
        reqId++;
        dropLastUser();
        if (waitingView != null) {
            chat.removeView(waitingView);
            waitingView = null;
        }
        sendBtn.setText("Send");
        addMessage("Stopped.", false);
    }

    private static JSONObject turn(String role, String text) throws Exception {
        return new JSONObject().put("role", role).put("parts", new JSONArray().put(new JSONObject().put("text", text)));
    }

    private static JSONArray copyHistory() {
        JSONArray a = new JSONArray();
        for (int i = 0; i < HISTORY.length(); i++) {
            a.put(HISTORY.opt(i));
        }
        return a;
    }

    private static void remember(String reply) {
        try {
            HISTORY.put(turn("model", reply));
            while (HISTORY.length() > MAX_TURNS) {
                HISTORY.remove(0);
            }
            while (HISTORY.length() > 0 && !"user".equals(HISTORY.getJSONObject(0).optString("role"))) {
                HISTORY.remove(0);
            }
        } catch (Throwable ignored) {
        }
    }

    /** A failed request shouldn't leave an unanswered question in the conversation. */
    private static void dropLastUser() {
        if (HISTORY.length() > 0) {
            HISTORY.remove(HISTORY.length() - 1);
        }
    }

    private void clearChat() {
        while (HISTORY.length() > 0) {
            HISTORY.remove(0);
        }
        lastReply = "";
        chat.removeAllViews();
        addMessage("Chat cleared.", false);
    }

    private void copyReply() {
        if (lastReply.length() == 0) {
            addMessage("There is no reply to copy yet.", false);
            return;
        }
        ClipboardManager cm = (ClipboardManager) act.getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setPrimaryClip(ClipData.newPlainText("Gemini reply", lastReply));
        Toast.makeText(act, "Reply copied.", Toast.LENGTH_SHORT).show();
    }

    private void insertCode() {
        if (studio.isPlaying()) {
            addMessage("Stop the playtest first. Inserting a script edits the place.", false);
            return;
        }
        String code = null;
        Matcher m = CODE.matcher(lastReply);
        while (m.find()) {
            code = m.group(1);
        }
        if (code == null || code.trim().length() == 0) {
            addMessage("The last reply has no ```lua code block to insert.", false);
            return;
        }
        Instance parent = null;
        for (Instance i : new ArrayList<>(studio.getSelection())) {
            if (!i.isA("Script") && !i.isA("LocalScript") && !i.isA("ModuleScript") && !i.isA("BasePart")) {
                parent = i;
                break;
            }
        }
        if (parent == null) {
            parent = studio.getModel().getWorkspace();
        }
        Instance script = InstanceFactory.INSTANCE.create("Script");
        script.setName("GeminiScript");
        script.setParent(parent, -1);
        script.set("Source", code.trim() + "\n", true);
        studio.markDirty();
        addMessage("Inserted a new Script named GeminiScript into " + parent.getName() + ". Read it before you run it.", false);
    }

    private void settingsDialog() {
        final Ui ui = Ui.INSTANCE;
        LinearLayout box = new LinearLayout(act);
        box.setOrientation(LinearLayout.VERTICAL);
        int p = ui.dp(act, 16.0f);
        box.setPadding(p, p, p, p);
        String k = key();
        box.addView(ui.text(act, k.length() == 0
                ? "No key saved. Get a free key at aistudio.google.com/apikey (sign in with a Google account), then paste it here."
                : "A key is saved (ends in " + k.substring(Math.max(0, k.length() - 4)) + "). Paste a new one to replace it.", 12.0f, ui.getDIM(), false));
        final EditText keyIn = ui.input(act, "", "API key", false, false);
        keyIn.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        box.addView(keyIn);
        box.addView(ui.text(act, "Model (optional, leave empty). The app picks a working one for you.", 12.0f, ui.getDIM(), false));
        final EditText modelIn = ui.input(act, model(), "automatic", false, false);
        box.addView(modelIn);
        final TextView result = ui.text(act, "", 12.0f, ui.getTEXT(), false);
        final int[] testId = new int[1];
        box.addView(btn("Test connection", () -> {
            String typed = keyIn.getText().toString().trim();
            final String k2 = typed.length() > 0 ? typed : key();
            if (k2.length() == 0) {
                result.setText("Paste your API key above first.");
                return;
            }
            final int mine = ++testId[0];
            result.setText("Testing\u2026");
            main.postDelayed(() -> {
                if (testId[0] == mine && "Testing\u2026".contentEquals(result.getText())) {
                    result.setText("The test timed out after 25 seconds. The phone can't reach Google: check your internet, VPN or Private DNS.");
                }
            }, 25000L);
            NET.execute(() -> {
                final String report = GeminiClient.diagnose(k2);
                main.post(() -> {
                    if (testId[0] == mine) {
                        result.setText(report);
                    }
                });
            });
        }));
        box.addView(result);
        box.addView(ui.text(act, "The key is stored only on this phone and is sent only to Google's Gemini API. Free-tier limits apply.", 11.0f, ui.getDIM(), false));
        ScrollView sv = new ScrollView(act);
        sv.addView(box);
        Dialogs.INSTANCE.themed(act).setTitle("Gemini settings").setView(sv)
                .setPositiveButton("Save", (d, w) -> {
                    SharedPreferences.Editor e = prefs().edit();
                    String nk = keyIn.getText().toString().trim();
                    if (nk.length() > 0) {
                        if (nk.length() < 20 || nk.indexOf(' ') >= 0) {
                            Toast.makeText(act, "That doesn't look like an API key.", Toast.LENGTH_LONG).show();
                            return;
                        }
                        e.putString("key", nk);
                    }
                    String nm = modelIn.getText().toString().trim();
                    if (nm.length() == 0 || !GeminiClient.validModel(nm)) {
                        e.remove("model");
                    } else {
                        e.putString("model", nm);
                    }
                    e.apply();
                    Toast.makeText(act, "Saved.", Toast.LENGTH_SHORT).show();
                })
                .setNeutralButton("Remove key", (d, w) -> {
                    prefs().edit().remove("key").apply();
                    Toast.makeText(act, "Key removed.", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void close() {
        closed = true;
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

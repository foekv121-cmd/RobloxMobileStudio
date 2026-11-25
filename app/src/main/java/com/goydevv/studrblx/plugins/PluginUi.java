package com.goydevv.studrblx.plugins;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import com.goydevv.studrblx.core.OutputLog;
import com.goydevv.studrblx.core.Studio;
import com.goydevv.studrblx.ui.Dialogs;
import com.goydevv.studrblx.ui.Ui;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/** Plugin Manager: Installed, Create, My Plugins, Examples, Settings. */
public final class PluginUi {
    /** Set by MainActivity; opens the system file picker for importing a plugin bundle. */
    public static ActivityResultLauncher<String[]> launcher;

    private static final Handler MAIN = new Handler(Looper.getMainLooper());
    private static final String[] TABS = {"Installed", "Create", "My Plugins", "Examples", "Settings"};
    private static Runnable refreshHook;
    private static Activity current;

    private PluginUi() {
    }

    // ------------------------------------------------------------------ entry points

    public static void show(final Activity act, Studio studio) {
        NativePlugins.init(act, studio);
        current = act;
        final Ui ui = Ui.INSTANCE;

        LinearLayout root = new LinearLayout(act);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(ui.dp(act, 14.0f), ui.dp(act, 12.0f), ui.dp(act, 14.0f), ui.dp(act, 4.0f));
        root.addView(ui.text(act, "Plugins", 18.0f, ui.getTEXT(), true));
        TextView sub = ui.text(act, "Native plugins for Mobile Studio (not Roblox Studio plugins).", 12.0f, ui.getDIM(), false);
        sub.setSingleLine(false);
        sub.setPadding(0, ui.dp(act, 2.0f), 0, ui.dp(act, 8.0f));
        root.addView(sub);

        final LinearLayout tabRow = new LinearLayout(act);
        tabRow.setOrientation(LinearLayout.HORIZONTAL);
        HorizontalScrollView tabScroll = new HorizontalScrollView(act);
        tabScroll.setHorizontalScrollBarEnabled(false);
        tabScroll.addView(tabRow);
        root.addView(tabScroll);

        final LinearLayout content = new LinearLayout(act);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(0, ui.dp(act, 8.0f), 0, ui.dp(act, 8.0f));
        root.addView(content);

        final int[] tab = {0};
        final NativePlugins.Plugin[] editing = {null};
        final TextView[] chips = new TextView[TABS.length];
        final Runnable[] render = new Runnable[1];
        render[0] = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < chips.length; i++) {
                    styleChip(act, chips[i], i == tab[0]);
                }
                content.removeAllViews();
                switch (tab[0]) {
                    case 0:
                        buildInstalled(act, content, false, tab, editing, render);
                        break;
                    case 1:
                        buildCreate(act, content, editing, tab, render);
                        break;
                    case 2:
                        buildInstalled(act, content, true, tab, editing, render);
                        break;
                    case 3:
                        buildCommunity(act, content, render);
                        break;
                    default:
                        buildSettings(act, content, render);
                }
            }
        };
        for (int i = 0; i < TABS.length; i++) {
            final int idx = i;
            TextView chip = ui.text(act, TABS[i], 13.0f, ui.getTEXT(), true);
            chip.setPadding(ui.dp(act, 14.0f), ui.dp(act, 10.0f), ui.dp(act, 14.0f), ui.dp(act, 10.0f));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-2, -2);
            lp.rightMargin = ui.dp(act, 6.0f);
            tabRow.addView(chip, lp);
            chips[i] = chip;
            chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (idx != 1) {
                        editing[0] = null;
                    }
                    tab[0] = idx;
                    render[0].run();
                }
            });
        }
        refreshHook = render[0];
        render[0].run();

        ScrollView sv = new ScrollView(act);
        sv.addView(root);
        AlertDialog dlg = Dialogs.INSTANCE.themed(act).setView(sv).setPositiveButton("Close", null).create();
        dlg.setOnDismissListener(new android.content.DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(android.content.DialogInterface d) {
                refreshHook = null;
            }
        });
        dlg.show();
    }

    /** Called by MainActivity when the import file picker returns. */
    public static void onFilePicked(Activity act, Uri uri) {
        if (uri == null) {
            return;
        }
        try (InputStream in = act.getContentResolver().openInputStream(uri)) {
            if (in == null) {
                throw new IOException("couldn't open the file");
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[8192];
            int n;
            while ((n = in.read(buf)) != -1) {
                out.write(buf, 0, n);
                if (out.size() > 400_000) {
                    throw new IOException("file is too large for a plugin bundle");
                }
            }
            installFromText(act, new String(out.toByteArray(), StandardCharsets.UTF_8), "");
        } catch (Throwable t) {
            toast(act, "Couldn't import: " + t.getMessage());
        }
    }

    // ------------------------------------------------------------------ shared helpers

    private static void toast(Context c, String s) {
        Toast.makeText(c, s, Toast.LENGTH_LONG).show();
    }

    private static void refresh() {
        if (refreshHook != null) {
            refreshHook.run();
        }
    }

    private static void styleChip(Context c, TextView t, boolean on) {
        GradientDrawable g = new GradientDrawable();
        g.setCornerRadius(Ui.INSTANCE.dp(c, 18.0f));
        g.setColor(on ? Ui.INSTANCE.getACCENT() : Ui.INSTANCE.getHOVER());
        t.setBackground(g);
        t.setTextColor(on ? 0xFFFFFFFF : Ui.INSTANCE.getDIM());
    }

    private static LinearLayout card(Context c) {
        LinearLayout l = new LinearLayout(c);
        l.setOrientation(LinearLayout.VERTICAL);
        l.setPadding(Ui.INSTANCE.dp(c, 12.0f), Ui.INSTANCE.dp(c, 10.0f), Ui.INSTANCE.dp(c, 12.0f), Ui.INSTANCE.dp(c, 10.0f));
        GradientDrawable g = new GradientDrawable();
        g.setCornerRadius(Ui.INSTANCE.dp(c, 12.0f));
        g.setColor(Ui.INSTANCE.getHOVER());
        g.setStroke(Ui.INSTANCE.dp(c, 1.0f), Ui.INSTANCE.getBORDER());
        l.setBackground(g);
        return l;
    }

    private static LinearLayout.LayoutParams gap(Context c) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
        lp.topMargin = Ui.INSTANCE.dp(c, 8.0f);
        return lp;
    }

    private static TextView label(Context c, String s) {
        TextView t = Ui.INSTANCE.text(c, s, 12.0f, Ui.INSTANCE.getDIM(), true);
        t.setPadding(0, Ui.INSTANCE.dp(c, 12.0f), 0, Ui.INSTANCE.dp(c, 4.0f));
        return t;
    }

    private static TextView note(Context c, String s) {
        TextView t = Ui.INSTANCE.text(c, s, 12.0f, Ui.INSTANCE.getDIM(), false);
        t.setSingleLine(false);
        t.setPadding(0, Ui.INSTANCE.dp(c, 4.0f), 0, Ui.INSTANCE.dp(c, 4.0f));
        return t;
    }

    private static TextView btn(Context c, String label, final Runnable r) {
        return Ui.INSTANCE.button(c, label, Ui.INSTANCE.getBORDER(), new Function1<View, Unit>() {
            @Override
            public Unit invoke(View v) {
                r.run();
                return Unit.INSTANCE;
            }
        });
    }

    private static TextView primary(Context c, String label, final Runnable r) {
        return Ui.INSTANCE.button(c, label, Ui.INSTANCE.getACCENT(), new Function1<View, Unit>() {
            @Override
            public Unit invoke(View v) {
                r.run();
                return Unit.INSTANCE;
            }
        });
    }

    private static LinearLayout btnRow(Context c, TextView... bs) {
        LinearLayout row = new LinearLayout(c);
        row.setOrientation(LinearLayout.HORIZONTAL);
        for (int i = 0; i < bs.length; i++) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, -2, 1.0f);
            if (i > 0) {
                lp.leftMargin = Ui.INSTANCE.dp(c, 6.0f);
            }
            row.addView(bs[i], lp);
        }
        return row;
    }

    private static String slug(String name) {
        String s = name.toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("^-+|-+$", "");
        if (s.length() > 28) {
            s = s.substring(0, 28).replaceAll("-+$", "");
        }
        if (s.length() < 3) {
            s = "plugin";
        }
        byte[] r = new byte[2];
        new SecureRandom().nextBytes(r);
        return s + "-" + String.format("%02x%02x", r[0] & 0xff, r[1] & 0xff);
    }

    // ------------------------------------------------------------------ install flow

    private static void installFromText(final Activity act, String text, String sourceUrl) {
        try {
            NativePlugins.Plugin p = NativePlugins.parseBundle(text);
            p.sourceUrl = sourceUrl;
            confirmInstall(act, p, false);
        } catch (Throwable t) {
            toast(act, "That isn't a valid plugin bundle: " + t.getMessage());
        }
    }

    private static void confirmInstall(final Activity act, final NativePlugins.Plugin p, final boolean created) {
        String err = NativePlugins.validate(p, true);
        if (err != null) {
            new AlertDialog.Builder(act).setTitle("Can't install").setMessage(err).setPositiveButton("OK", null).show();
            return;
        }
        final Ui ui = Ui.INSTANCE;
        NativePlugins.Plugin old = NativePlugins.get(p.id);
        LinearLayout root = new LinearLayout(act);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(ui.dp(act, 18.0f), ui.dp(act, 14.0f), ui.dp(act, 18.0f), ui.dp(act, 6.0f));
        root.addView(ui.text(act, p.icon + "  " + p.name, 17.0f, ui.getTEXT(), true));
        String verLine = "v" + p.version + (p.author.length() > 0 ? "  \u2022  by " + p.author : "");
        if (old != null) {
            verLine = "Update v" + old.version + " \u2192 v" + p.version + (p.author.length() > 0 ? "  \u2022  by " + p.author : "");
        }
        root.addView(ui.text(act, verLine, 12.0f, ui.getDIM(), false));
        if (p.description.length() > 0) {
            root.addView(note(act, p.description));
        }
        root.addView(label(act, p.requested.isEmpty() ? "PERMISSIONS: NONE REQUESTED" : "THIS PLUGIN WANTS TO"));
        for (String perm : p.requested) {
            TextView t = ui.text(act, "\u2022 " + NativePlugins.permTitle(perm) + " \u2014 " + NativePlugins.permDesc(perm), 12.0f, ui.getTEXT(), false);
            t.setSingleLine(false);
            t.setPadding(0, ui.dp(act, 2.0f), 0, ui.dp(act, 2.0f));
            root.addView(t);
        }
        List<String> warns = NativePlugins.warnings(p);
        if (!warns.isEmpty()) {
            root.addView(label(act, "HEADS UP"));
            for (String w : warns) {
                TextView t = ui.text(act, "\u2022 " + w, 12.0f, ui.getTEXT(), false);
                t.setSingleLine(false);
                root.addView(t);
            }
        }
        root.addView(note(act, "Plugins run in a sandbox with a time limit. You can change permissions or disable it any time."));
        ScrollView sv = new ScrollView(act);
        sv.addView(root);
        Dialogs.INSTANCE.themed(act).setView(sv)
                .setPositiveButton(old != null ? "Update & allow" : "Install & allow", new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(android.content.DialogInterface d, int w) {
                        try {
                            NativePlugins.install(p, created, true);
                            toast(act, "Installed \"" + p.name + "\"");
                            OutputLog.INSTANCE.info("Plugins: installed " + p.name + " v" + p.version);
                        } catch (Throwable t) {
                            toast(act, "Install failed: " + t.getMessage());
                        }
                        refresh();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private static String fetchText(String url) throws IOException {
        if (!url.startsWith("https://")) {
            throw new IOException("only https:// links are allowed");
        }
        HttpURLConnection c = (HttpURLConnection) new URL(url).openConnection();
        try {
            c.setConnectTimeout(15000);
            c.setReadTimeout(20000);
            c.setInstanceFollowRedirects(true);
            int code = c.getResponseCode();
            if (code != 200) {
                throw new IOException("server returned HTTP " + code);
            }
            try (InputStream in = c.getInputStream()) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buf = new byte[8192];
                int n;
                while ((n = in.read(buf)) != -1) {
                    out.write(buf, 0, n);
                    if (out.size() > 400_000) {
                        throw new IOException("bundle is larger than 400 KB");
                    }
                }
                return new String(out.toByteArray(), StandardCharsets.UTF_8);
            }
        } finally {
            c.disconnect();
        }
    }

    private static void installFromUrl(final Activity act, final String url, final boolean updateOf) {
        toast(act, "Downloading\u2026");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String text = fetchText(url.trim());
                    MAIN.post(new Runnable() {
                        @Override
                        public void run() {
                            installFromText(act, text, url.trim());
                        }
                    });
                } catch (final Throwable t) {
                    MAIN.post(new Runnable() {
                        @Override
                        public void run() {
                            toast(act, "Download failed: " + t.getMessage());
                        }
                    });
                }
            }
        }, "PluginDownload").start();
    }

    // ------------------------------------------------------------------ Installed / My Plugins

    private static void buildInstalled(final Activity act, LinearLayout content, boolean mineOnly, final int[] tab, final NativePlugins.Plugin[] editing, final Runnable[] render) {
        final Ui ui = Ui.INSTANCE;
        if (!NativePlugins.masterEnabled()) {
            content.addView(note(act, "Plugins are switched off in Plugin Settings."));
        }
        int count = 0;
        for (final NativePlugins.Plugin p : NativePlugins.list()) {
            if (mineOnly && !p.created) {
                continue;
            }
            count++;
            LinearLayout card = card(act);
            LinearLayout top = new LinearLayout(act);
            top.setOrientation(LinearLayout.HORIZONTAL);
            top.setGravity(Gravity.CENTER_VERTICAL);
            TextView icon = ui.text(act, p.icon, 24.0f, ui.getTEXT(), false);
            top.addView(icon);
            LinearLayout col = new LinearLayout(act);
            col.setOrientation(LinearLayout.VERTICAL);
            col.setPadding(ui.dp(act, 10.0f), 0, 0, 0);
            col.addView(ui.text(act, p.name, 15.0f, ui.getTEXT(), true));
            col.addView(ui.text(act, "v" + p.version + (p.author.length() > 0 ? "  \u2022  " + p.author : ""), 11.0f, ui.getDIM(), false));
            top.addView(col, new LinearLayout.LayoutParams(0, -2, 1.0f));
            card.addView(top);
            if (p.description.length() > 0) {
                card.addView(note(act, p.description));
            }
            String state = !p.enabled ? "Disabled" : NativePlugins.isRunning(p.id) ? "Enabled \u2022 running" : "Enabled \u2022 not loaded yet";
            TextView st = ui.text(act, state, 12.0f, p.enabled ? Ui.PLAY : ui.getDIM(), true);
            st.setPadding(0, ui.dp(act, 4.0f), 0, 0);
            card.addView(st);
            if (p.lastError.length() > 0) {
                TextView er = ui.text(act, "Last error: " + p.lastError, 11.0f, Ui.ERROR, false);
                er.setSingleLine(false);
                card.addView(er);
            }
            StringBuilder perms = new StringBuilder();
            for (String perm : p.requested) {
                if (perms.length() > 0) {
                    perms.append(", ");
                }
                perms.append(NativePlugins.permTitle(perm)).append(p.granted.contains(perm) ? "" : " (off)");
            }
            card.addView(note(act, "Permissions: " + (perms.length() == 0 ? "none" : perms.toString())));

            card.addView(btnRow(act,
                    btn(act, p.enabled ? "Disable" : "Enable", new Runnable() {
                        @Override
                        public void run() {
                            NativePlugins.setEnabled(p.id, !p.enabled);
                            refresh();
                        }
                    }),
                    btn(act, "Permissions", new Runnable() {
                        @Override
                        public void run() {
                            showPermissions(act, p);
                        }
                    }),
                    btn(act, "Update", new Runnable() {
                        @Override
                        public void run() {
                            if (p.sourceUrl.length() == 0) {
                                toast(act, "This plugin has no update link (it was made here or installed from a file).");
                            } else {
                                installFromUrl(act, p.sourceUrl, true);
                            }
                        }
                    })), gap(act));
            card.addView(btnRow(act,
                    btn(act, "Export", new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ClipboardManager cm = (ClipboardManager) act.getSystemService(Context.CLIPBOARD_SERVICE);
                                cm.setPrimaryClip(ClipData.newPlainText("plugin", p.toJson(false).toString(2)));
                                toast(act, "Plugin bundle copied to the clipboard (use it as a backup).");
                            } catch (Throwable t) {
                                toast(act, "Couldn't export: " + t.getMessage());
                            }
                        }
                    }),
                    btn(act, p.created ? "Edit" : "View code", new Runnable() {
                        @Override
                        public void run() {
                            editing[0] = p;
                            tab[0] = 1;
                            render[0].run();
                        }
                    }),
                    btn(act, "Uninstall", new Runnable() {
                        @Override
                        public void run() {
                            Dialogs.INSTANCE.themed(act).setTitle("Uninstall " + p.name + "?")
                                    .setMessage("The plugin and its saved files will be deleted.")
                                    .setPositiveButton("Uninstall", new android.content.DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(android.content.DialogInterface d, int w) {
                                            NativePlugins.uninstall(p.id);
                                            refresh();
                                        }
                                    }).setNegativeButton("Cancel", null).show();
                        }
                    })), gap(act));
            content.addView(card, gap(act));
        }
        if (count == 0) {
            content.addView(note(act, mineOnly
                    ? "You haven't made any plugins yet. Open the Create tab to write one."
                    : "No plugins installed. Open Create to write your own, or try the Examples tab."));
        }
        if (mineOnly) {
            content.addView(primary(act, "New plugin", new Runnable() {
                @Override
                public void run() {
                    editing[0] = null;
                    tab[0] = 1;
                    render[0].run();
                }
            }), gap(act));
        }
    }

    private static void showPermissions(final Activity act, final NativePlugins.Plugin p) {
        final Ui ui = Ui.INSTANCE;
        LinearLayout root = new LinearLayout(act);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(ui.dp(act, 18.0f), ui.dp(act, 14.0f), ui.dp(act, 18.0f), ui.dp(act, 6.0f));
        root.addView(ui.text(act, "Permissions for " + p.name, 16.0f, ui.getTEXT(), true));
        final java.util.Map<String, CheckBox> boxes = new java.util.LinkedHashMap<>();
        if (p.requested.isEmpty()) {
            root.addView(note(act, "This plugin doesn't ask for any permissions."));
        }
        for (String perm : p.requested) {
            CheckBox cb = new CheckBox(act);
            cb.setText(NativePlugins.permTitle(perm) + "\n" + NativePlugins.permDesc(perm));
            cb.setTextColor(ui.getTEXT());
            cb.setTextSize(13.0f);
            cb.setChecked(p.granted.contains(perm));
            boxes.put(perm, cb);
            root.addView(cb, gap(act));
        }
        ScrollView sv = new ScrollView(act);
        sv.addView(root);
        Dialogs.INSTANCE.themed(act).setView(sv)
                .setPositiveButton("Save", new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(android.content.DialogInterface d, int w) {
                        Set<String> g = new LinkedHashSet<>();
                        for (java.util.Map.Entry<String, CheckBox> e : boxes.entrySet()) {
                            if (e.getValue().isChecked()) {
                                g.add(e.getKey());
                            }
                        }
                        NativePlugins.setGranted(p.id, g);
                        refresh();
                    }
                }).setNegativeButton("Cancel", null).show();
    }

    // ------------------------------------------------------------------ Create / Edit

    private static void buildCreate(final Activity act, LinearLayout content, final NativePlugins.Plugin[] editing, final int[] tab, final Runnable[] render) {
        final Ui ui = Ui.INSTANCE;
        final NativePlugins.Plugin src = editing[0];
        final boolean readOnly = src != null && !src.created;
        content.addView(label(act, src == null ? "NEW PLUGIN" : readOnly ? "VIEWING (installed plugin, read only)" : "EDITING " + src.name.toUpperCase()));

        final EditText etName = ui.input(act, src == null ? "" : src.name, "Plugin name", false, false);
        final EditText etDesc = ui.input(act, src == null ? "" : src.description, "Description (max 300 characters)", false, false);
        final EditText etAuthor = ui.input(act, src == null ? "" : src.author, "Author", false, false);
        final EditText etVersion = ui.input(act, src == null ? "1.0.0" : src.version, "Version, e.g. 1.0.0", false, false);
        final EditText etIcon = ui.input(act, src == null ? "\uD83E\uDDE9" : src.icon, "Icon (one emoji)", false, false);
        content.addView(label(act, "NAME"));
        content.addView(etName);
        content.addView(label(act, "DESCRIPTION"));
        content.addView(etDesc);
        content.addView(label(act, "AUTHOR"));
        content.addView(etAuthor);
        content.addView(label(act, "VERSION"));
        content.addView(etVersion);
        content.addView(label(act, "ICON"));
        content.addView(etIcon);

        content.addView(label(act, "PERMISSIONS THIS PLUGIN NEEDS"));
        final java.util.Map<String, CheckBox> boxes = new java.util.LinkedHashMap<>();
        for (String perm : NativePlugins.PERMS) {
            CheckBox cb = new CheckBox(act);
            cb.setText(NativePlugins.permTitle(perm));
            cb.setTextColor(ui.getTEXT());
            cb.setTextSize(13.0f);
            cb.setChecked(src != null && src.requested.contains(perm));
            cb.setEnabled(!readOnly);
            boxes.put(perm, cb);
            content.addView(cb);
        }

        content.addView(label(act, "CODE (Lua)"));
        final EditText etCode = ui.input(act, src == null ? NativePlugins.SAMPLE_HELLO : src.code, "-- plugin code", false, true);
        etCode.setTypeface(Typeface.MONOSPACE);
        etCode.setTextSize(13.0f);
        etCode.setMinHeight(ui.dp(act, 220.0f));
        etCode.setGravity(Gravity.TOP | Gravity.START);
        etCode.setHorizontallyScrolling(true);
        etCode.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        etCode.setEnabled(!readOnly);
        content.addView(etCode);

        final TextView status = ui.text(act, "", 12.0f, ui.getDIM(), false);
        status.setSingleLine(false);
        status.setPadding(0, ui.dp(act, 8.0f), 0, ui.dp(act, 4.0f));

        final Runnable[] collect = new Runnable[1];
        final NativePlugins.Plugin[] built = {null};
        collect[0] = new Runnable() {
            @Override
            public void run() {
                NativePlugins.Plugin p = new NativePlugins.Plugin();
                p.name = etName.getText().toString().trim();
                p.description = etDesc.getText().toString().trim();
                p.author = etAuthor.getText().toString().trim();
                p.version = etVersion.getText().toString().trim();
                p.icon = etIcon.getText().toString().trim();
                if (p.icon.length() == 0) {
                    p.icon = "\uD83E\uDDE9";
                }
                p.code = etCode.getText().toString();
                for (java.util.Map.Entry<String, CheckBox> e : boxes.entrySet()) {
                    if (e.getValue().isChecked()) {
                        p.requested.add(e.getKey());
                    }
                }
                p.id = src != null ? src.id : slug(p.name);
                if (src != null) {
                    p.sourceUrl = src.sourceUrl;
                }
                built[0] = p;
            }
        };

        if (!readOnly) {
            content.addView(status);
            content.addView(btnRow(act,
                    primary(act, "Save", new Runnable() {
                        @Override
                        public void run() {
                            collect[0].run();
                            NativePlugins.Plugin p = built[0];
                            String err = NativePlugins.validate(p, true);
                            if (err != null) {
                                status.setTextColor(Ui.ERROR);
                                status.setText(err);
                                return;
                            }
                            try {
                                NativePlugins.install(p, true, true);
                                editing[0] = NativePlugins.get(p.id);
                                status.setTextColor(Ui.PLAY);
                                status.setText("Saved \"" + p.name + "\". It appears under Installed and My Plugins.");
                            } catch (Throwable t) {
                                status.setTextColor(Ui.ERROR);
                                status.setText("Couldn't save: " + t.getMessage());
                            }
                        }
                    }),
                    btn(act, "Test", new Runnable() {
                        @Override
                        public void run() {
                            collect[0].run();
                            NativePlugins.Plugin p = built[0];
                            try {
                                NativePlugins.test(p);
                                editing[0] = NativePlugins.get(p.id);
                                status.setTextColor(Ui.PLAY);
                                status.setText("Saved and started with all requested permissions. Check the Output panel for log lines and errors, then open File menu > Plugins to try its buttons.");
                            } catch (Throwable t) {
                                status.setTextColor(Ui.ERROR);
                                status.setText(t.getMessage());
                            }
                        }
                    })), gap(act));
        }
        content.addView(btn(act, "API reference", new Runnable() {
            @Override
            public void run() {
                showDocs(act);
            }
        }), gap(act));
    }

    private static void showDocs(Activity act) {
        final Ui ui = Ui.INSTANCE;
        TextView t = ui.text(act, NativePlugins.API_DOCS, 12.0f, ui.getTEXT(), false);
        t.setSingleLine(false);
        t.setTypeface(Typeface.MONOSPACE);
        t.setTextIsSelectable(true);
        t.setPadding(ui.dp(act, 16.0f), ui.dp(act, 12.0f), ui.dp(act, 16.0f), ui.dp(act, 12.0f));
        HorizontalScrollView h = new HorizontalScrollView(act);
        h.addView(t);
        ScrollView sv = new ScrollView(act);
        sv.addView(h);
        Dialogs.INSTANCE.themed(act).setView(sv).setPositiveButton("Close", null).show();
    }

    // ------------------------------------------------------------------ Examples (starter plugins)

    private static NativePlugins.Plugin starter(String id, String name, String desc, String icon, String code, String... perms) {
        NativePlugins.Plugin p = new NativePlugins.Plugin();
        p.id = id;
        p.name = name;
        p.description = desc;
        p.author = "Mobile Studio";
        p.icon = icon;
        p.code = code;
        for (String perm : perms) {
            p.requested.add(perm);
        }
        return p;
    }

    private static void buildCommunity(final Activity act, LinearLayout content, final Runnable[] render) {
        final Ui ui = Ui.INSTANCE;
        content.addView(note(act, "Plugins here are made by you, inside this app. These examples are small, readable plugins to learn from: install one, then open it under My Plugins to edit it."));

        content.addView(label(act, "STARTER PLUGINS"));
        final NativePlugins.Plugin hello = starter("hello-studio", "Hello Studio", "Counts the objects in your place. A tiny example to learn from.", "\uD83D\uDC4B",
                NativePlugins.SAMPLE_HELLO, "ui_toolbar", "read_project", "notify");
        final NativePlugins.Plugin rename = starter("quick-rename", "Quick Rename", "Adds a prefix to selected objects and can add a folder.", "\u270F\uFE0F",
                NativePlugins.SAMPLE_RENAME, "ui_toolbar", "selection", "modify_properties", "create_objects", "notify");
        for (final NativePlugins.Plugin sp : new NativePlugins.Plugin[]{hello, rename}) {
            LinearLayout card = card(act);
            card.addView(ui.text(act, sp.icon + "  " + sp.name, 15.0f, ui.getTEXT(), true));
            card.addView(note(act, sp.description));
            boolean installed = NativePlugins.get(sp.id) != null;
            card.addView(btn(act, installed ? "Reinstall" : "Install", new Runnable() {
                @Override
                public void run() {
                    confirmInstall(act, sp, false);
                }
            }), gap(act));
            content.addView(card, gap(act));
        }
    }

    // ------------------------------------------------------------------ Settings

    private static void buildSettings(final Activity act, LinearLayout content, final Runnable[] render) {
        final Ui ui = Ui.INSTANCE;
        content.addView(label(act, "PLUGIN SYSTEM"));
        final Switch master = new Switch(act);
        master.setText("Allow plugins to run");
        master.setTextColor(ui.getTEXT());
        master.setChecked(NativePlugins.masterEnabled());
        master.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(android.widget.CompoundButton b, boolean on) {
                NativePlugins.setMasterEnabled(on);
            }
        });
        content.addView(master, gap(act));
        final Switch auto = new Switch(act);
        auto.setText("Load enabled plugins when Studio opens");
        auto.setTextColor(ui.getTEXT());
        auto.setChecked(NativePlugins.autoLoad());
        auto.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(android.widget.CompoundButton b, boolean on) {
                NativePlugins.setAutoLoad(on);
            }
        });
        content.addView(auto, gap(act));
        content.addView(note(act, "Enabled plugins load in the background shortly after Studio opens, on their own thread, so their buttons are ready in File menu > Plugins. Turn the switch above off to stop that; nothing runs for disabled plugins."));

        content.addView(label(act, "TIME LIMIT PER RUN"));
        final int[] limits = {1000, 2000, 5000, 10000};
        final TextView[] chips = new TextView[limits.length];
        LinearLayout row = new LinearLayout(act);
        row.setOrientation(LinearLayout.HORIZONTAL);
        for (int i = 0; i < limits.length; i++) {
            final int ms = limits[i];
            TextView chip = ui.text(act, (ms / 1000) + " s", 13.0f, ui.getTEXT(), true);
            chip.setGravity(Gravity.CENTER);
            chip.setPadding(0, ui.dp(act, 10.0f), 0, ui.dp(act, 10.0f));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, -2, 1.0f);
            if (i > 0) {
                lp.leftMargin = ui.dp(act, 6.0f);
            }
            row.addView(chip, lp);
            chips[i] = chip;
            styleChip(act, chip, NativePlugins.timeLimitMs() == ms);
        }
        for (int i = 0; i < limits.length; i++) {
            final int ms = limits[i];
            chips[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NativePlugins.setTimeLimitMs(ms);
                    for (int j = 0; j < limits.length; j++) {
                        styleChip(act, chips[j], limits[j] == ms);
                    }
                }
            });
        }
        content.addView(row);
        content.addView(note(act, "A script (or a button press) that runs longer than this is stopped and the plugin is disabled. Lower is safer on slow phones."));

        content.addView(label(act, "SAFETY"));
        content.addView(btn(act, "Disable all plugins", new Runnable() {
            @Override
            public void run() {
                NativePlugins.disableAll();
                toast(act, "All plugins disabled.");
                refresh();
            }
        }));
        content.addView(btn(act, "Plugin API reference", new Runnable() {
            @Override
            public void run() {
                showDocs(act);
            }
        }), gap(act));
        content.addView(note(act, "Sandbox: plugins can't use files outside their own folder, the network, your Roblox login, API keys or other plugins. They only get the permissions you grant. This protects against mistakes and most abuse, but only install plugins from people you trust."));
    }
}

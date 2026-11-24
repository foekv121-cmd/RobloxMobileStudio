package com.goydevv.studrblx.plugins;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import com.goydevv.studrblx.core.OutputLog;
import com.goydevv.studrblx.core.Studio;
import com.goydevv.studrblx.model.ClassDb;
import com.goydevv.studrblx.model.DataModel;
import com.goydevv.studrblx.model.Instance;
import com.goydevv.studrblx.model.InstanceFactory;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONObject;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.Bit32Lib;
import org.luaj.vm2.lib.BaseLib;
import org.luaj.vm2.lib.DebugLib;
import org.luaj.vm2.lib.StringLib;
import org.luaj.vm2.lib.TableLib;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.jse.JseMathLib;

/**
 * Native plugin system for Mobile Studio. This is NOT the Roblox Studio plugin system.
 *
 * A plugin is one JSON bundle (manifest + Lua code) stored in the app's private storage.
 * Plugins run in a stripped-down Lua sandbox on their own worker thread, with an instruction
 * hook that aborts runaway scripts. Every sensitive API needs a permission the user granted.
 * No API exposes Roblox API keys, cookies, tokens or the network.
 */
public final class NativePlugins {
    public static final int API_VERSION = 1;
    public static final String[] PERMS = {
            "read_project", "selection", "create_objects", "modify_properties",
            "ui_toolbar", "ui_panel", "notify", "files"
    };
    private static final Pattern ID_RE = Pattern.compile("^[a-z0-9][a-z0-9_-]{2,39}$");
    private static final Pattern VER_RE = Pattern.compile("^\\d+(\\.\\d+){0,2}$");
    private static final Pattern FILE_RE = Pattern.compile("^[A-Za-z0-9][A-Za-z0-9._-]{0,63}$");
    private static final int MAX_CODE = 200_000;
    private static final int MAX_FILE = 256 * 1024;
    private static final int MAX_DATA_DIR = 1024 * 1024;
    private static final int MAX_BUTTONS = 5;

    public static String permTitle(String p) {
        switch (p) {
            case "read_project": return "Read project";
            case "selection": return "See selection";
            case "create_objects": return "Create objects";
            case "modify_properties": return "Modify properties";
            case "ui_toolbar": return "Add toolbar buttons";
            case "ui_panel": return "Show panels";
            case "notify": return "Show notifications";
            case "files": return "Plugin data files";
            default: return p;
        }
    }

    public static String permDesc(String p) {
        switch (p) {
            case "read_project": return "Read the place's object names, classes and simple property values.";
            case "selection": return "See which objects you currently have selected.";
            case "create_objects": return "Insert new objects (only classes from the Insert menu).";
            case "modify_properties": return "Change text, number and on/off properties of existing objects.";
            case "ui_toolbar": return "Add buttons to the Plugins section of the File menu.";
            case "ui_panel": return "Open panels with text and buttons.";
            case "notify": return "Show short pop-up messages.";
            case "files": return "Read and write small files inside the plugin's own private folder only.";
            default: return "";
        }
    }

    // ------------------------------------------------------------------ model

    public static final class Plugin {
        public String id = "";
        public String name = "";
        public String description = "";
        public String author = "";
        public String version = "1.0.0";
        public String icon = "\uD83E\uDDE9";
        public String code = "";
        public String sourceUrl = "";
        public boolean created;
        public boolean enabled;
        public final LinkedHashSet<String> requested = new LinkedHashSet<>();
        public final java.util.Set<String> granted = Collections.synchronizedSet(new LinkedHashSet<String>());
        public int faults;
        public String lastError = "";

        public JSONObject toJson(boolean withState) throws Exception {
            JSONObject o = new JSONObject();
            o.put("id", id);
            o.put("name", name);
            o.put("description", description);
            o.put("author", author);
            o.put("version", version);
            o.put("icon", icon);
            o.put("permissions", new JSONArray(new ArrayList<>(requested)));
            o.put("code", code);
            if (sourceUrl.length() > 0) {
                o.put("sourceUrl", sourceUrl);
            }
            if (withState) {
                o.put("created", created);
                o.put("enabled", enabled);
                o.put("granted", new JSONArray(new ArrayList<>(granted)));
            }
            return o;
        }

        public static Plugin fromJson(JSONObject o, boolean withState) {
            Plugin p = new Plugin();
            p.id = o.optString("id", "").trim();
            p.name = o.optString("name", "").trim();
            p.description = o.optString("description", "").trim();
            p.author = o.optString("author", "").trim();
            p.version = o.optString("version", "1.0.0").trim();
            p.icon = o.optString("icon", "\uD83E\uDDE9").trim();
            p.code = o.optString("code", "");
            p.sourceUrl = o.optString("sourceUrl", "").trim();
            JSONArray perms = o.optJSONArray("permissions");
            if (perms != null) {
                for (int i = 0; i < perms.length(); i++) {
                    p.requested.add(perms.optString(i, ""));
                }
            }
            if (withState) {
                p.created = o.optBoolean("created", false);
                p.enabled = o.optBoolean("enabled", false);
                JSONArray g = o.optJSONArray("granted");
                if (g != null) {
                    for (int i = 0; i < g.length(); i++) {
                        String s = g.optString(i, "");
                        if (p.requested.contains(s)) {
                            p.granted.add(s);
                        }
                    }
                }
            }
            return p;
        }
    }

    public static final class Button {
        public String pluginId = "";
        public String id = "";
        public String text = "";
        public String icon = "";
        LuaValue callback;
    }

    // ------------------------------------------------------------------ state

    private static final Map<String, Plugin> PLUGINS = new LinkedHashMap<>();
    private static final Map<String, PluginSandbox> RUNNING = new HashMap<>();
    private static final List<Button> BUTTONS = new CopyOnWriteArrayList<>();
    private static final Handler MAIN = new Handler(Looper.getMainLooper());
    private static ExecutorService worker;
    private static boolean loaded;
    private static boolean autoStarted;
    static Activity activity;
    static Studio studio;
    static Context app;

    private NativePlugins() {
    }

    public static synchronized void init(Activity act, Studio st) {
        activity = act;
        studio = st;
        app = act.getApplicationContext();
        if (!loaded) {
            loaded = true;
            loadFromDisk();
        }
        if (!autoStarted) {
            autoStarted = true;
            if (autoLoad() && masterEnabled()) {
                startEnabled();
            }
        }
    }

    private static synchronized ExecutorService worker() {
        if (worker == null) {
            worker = Executors.newSingleThreadExecutor(new java.util.concurrent.ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r, "NativePlugins");
                    t.setPriority(Thread.NORM_PRIORITY - 1);
                    t.setDaemon(true);
                    return t;
                }
            });
        }
        return worker;
    }

    // ------------------------------------------------------------------ settings

    private static SharedPreferences prefs() {
        return app.getSharedPreferences("native_plugins", Context.MODE_PRIVATE);
    }

    public static boolean masterEnabled() {
        return prefs().getBoolean("master", true);
    }

    public static void setMasterEnabled(boolean on) {
        prefs().edit().putBoolean("master", on).apply();
        if (!on) {
            stopAll();
        } else {
            startEnabled();
        }
    }

    public static boolean autoLoad() {
        return prefs().getBoolean("autoload", true);
    }

    public static void setAutoLoad(boolean on) {
        prefs().edit().putBoolean("autoload", on).apply();
    }

    public static int timeLimitMs() {
        return prefs().getInt("limit_ms", 2000);
    }

    public static void setTimeLimitMs(int ms) {
        prefs().edit().putInt("limit_ms", ms).apply();
    }

    // ------------------------------------------------------------------ storage

    private static File dir() {
        File d = new File(app.getFilesDir(), "native_plugins");
        if (!d.exists()) {
            d.mkdirs();
        }
        return d;
    }

    static File dataDir(String id) {
        File d = new File(dir(), id + ".data");
        if (!d.exists()) {
            d.mkdirs();
        }
        return d;
    }

    private static void loadFromDisk() {
        PLUGINS.clear();
        File[] files = dir().listFiles();
        if (files == null) {
            return;
        }
        for (File f : files) {
            if (!f.isFile() || !f.getName().endsWith(".json")) {
                continue;
            }
            try {
                String text = readFile(f, 1_000_000);
                Plugin p = Plugin.fromJson(new JSONObject(text), true);
                if (validate(p, false) == null) {
                    PLUGINS.put(p.id, p);
                } else {
                    OutputLog.INSTANCE.warn("Plugins: skipped invalid file " + f.getName());
                }
            } catch (Throwable t) {
                OutputLog.INSTANCE.warn("Plugins: couldn't read " + f.getName() + ": " + t);
            }
        }
    }

    private static String readFile(File f, int max) throws IOException {
        try (InputStream in = new FileInputStream(f)) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[8192];
            int n;
            while ((n = in.read(buf)) != -1) {
                out.write(buf, 0, n);
                if (out.size() > max) {
                    throw new IOException("file too large");
                }
            }
            return new String(out.toByteArray(), StandardCharsets.UTF_8);
        }
    }

    private static void writeFile(File f, String text) throws IOException {
        File tmp = new File(f.getParentFile(), f.getName() + ".tmp");
        try (FileOutputStream out = new FileOutputStream(tmp)) {
            out.write(text.getBytes(StandardCharsets.UTF_8));
            out.getFD().sync();
        }
        if (!tmp.renameTo(f)) {
            f.delete();
            if (!tmp.renameTo(f)) {
                throw new IOException("couldn't save " + f.getName());
            }
        }
    }

    private static void persist(Plugin p) throws Exception {
        writeFile(new File(dir(), p.id + ".json"), p.toJson(true).toString());
    }

    // ------------------------------------------------------------------ queries

    public static synchronized List<Plugin> list() {
        return new ArrayList<>(PLUGINS.values());
    }

    public static synchronized Plugin get(String id) {
        return PLUGINS.get(id);
    }

    public static boolean isRunning(String id) {
        synchronized (RUNNING) {
            return RUNNING.containsKey(id);
        }
    }

    public static List<Button> buttons() {
        return new ArrayList<>(BUTTONS);
    }

    public static String pluginName(String id) {
        Plugin p = get(id);
        return p == null ? id : p.name;
    }

    // ------------------------------------------------------------------ validation / install

    /** Returns an error message, or null when the plugin is acceptable. */
    public static String validate(Plugin p, boolean compile) {
        if (!ID_RE.matcher(p.id).matches()) {
            return "Plugin id must be 3-40 characters: lowercase letters, numbers, - or _.";
        }
        if (p.name.length() < 1 || p.name.length() > 60) {
            return "Plugin name must be 1-60 characters.";
        }
        if (p.description.length() > 300) {
            return "Description is longer than 300 characters.";
        }
        if (p.author.length() > 60) {
            return "Author is longer than 60 characters.";
        }
        if (!VER_RE.matcher(p.version).matches()) {
            return "Version must look like 1, 1.2 or 1.2.3.";
        }
        if (p.icon.length() > 8) {
            return "Icon must be a single emoji or short symbol.";
        }
        for (String perm : p.requested) {
            boolean known = false;
            for (String k : PERMS) {
                if (k.equals(perm)) {
                    known = true;
                }
            }
            if (!known) {
                return "Unknown permission \"" + perm + "\".";
            }
        }
        if (p.code.trim().length() == 0) {
            return "The plugin has no code.";
        }
        if (p.code.length() > MAX_CODE) {
            return "Plugin code is larger than 200 KB.";
        }
        String blocked = blockedApi(p.code);
        if (blocked != null) {
            return "Plugin uses \"" + blocked + "\", which is not available to plugins.";
        }
        if (compile) {
            try {
                new PluginSandbox(p).checkSyntax();
            } catch (LuaError e) {
                return "Lua error: " + e.getMessage();
            } catch (Throwable t) {
                return "Couldn't check the code: " + t;
            }
        }
        return null;
    }

    private static String blockedApi(String code) {
        String[] bad = {"luajava", "debug.", "io.popen", "os.execute", "os.remove", "os.rename", "dofile", "loadfile", "package."};
        for (String b : bad) {
            if (code.contains(b)) {
                return b;
            }
        }
        return null;
    }

    /** Things worth showing the user before they accept a plugin. */
    public static List<String> warnings(Plugin p) {
        List<String> out = new ArrayList<>();
        if (p.requested.contains("modify_properties") || p.requested.contains("create_objects")) {
            out.add("This plugin can change your project.");
        }
        if (p.requested.contains("files")) {
            out.add("This plugin stores files in its own private folder.");
        }
        String[] odd = {"while true", "string.rep", "setmetatable(_G", "_G.", "getmetatable("};
        for (String o : odd) {
            if (p.code.contains(o)) {
                out.add("Code contains \"" + o + "\" (sandboxed, but unusual).");
            }
        }
        return out;
    }

    public static Plugin parseBundle(String text) throws Exception {
        JSONObject o = new JSONObject(text.trim());
        Plugin p = Plugin.fromJson(o, false);
        return p;
    }

    /** Install or update. New permissions are NOT granted automatically beyond what the caller passes in. */
    public static synchronized void install(Plugin p, boolean created, boolean grantRequested) throws Exception {
        String err = validate(p, true);
        if (err != null) {
            throw new IllegalArgumentException(err);
        }
        Plugin old = PLUGINS.get(p.id);
        if (old != null) {
            stop(p.id);
            p.enabled = old.enabled;
            p.created = old.created || created;
            p.faults = 0;
            for (String g : old.granted) {
                if (p.requested.contains(g)) {
                    p.granted.add(g);
                }
            }
        } else {
            p.created = created;
            p.enabled = true;
        }
        if (grantRequested) {
            p.granted.addAll(p.requested);
        }
        persist(p);
        PLUGINS.put(p.id, p);
        if (p.enabled && masterEnabled()) {
            start(p.id);
        }
    }

    public static synchronized void uninstall(String id) {
        stop(id);
        PLUGINS.remove(id);
        new File(dir(), id + ".json").delete();
        File d = new File(dir(), id + ".data");
        File[] fs = d.listFiles();
        if (fs != null) {
            for (File f : fs) {
                f.delete();
            }
        }
        d.delete();
    }

    public static synchronized void setEnabled(String id, boolean on) {
        Plugin p = PLUGINS.get(id);
        if (p == null) {
            return;
        }
        p.enabled = on;
        if (on) {
            p.faults = 0;
            p.lastError = "";
        }
        try {
            persist(p);
        } catch (Throwable t) {
            OutputLog.INSTANCE.error("Plugins: couldn't save " + id + ": " + t);
        }
        if (on) {
            start(id);
        } else {
            stop(id);
        }
    }

    public static synchronized void setGranted(String id, java.util.Set<String> granted) {
        Plugin p = PLUGINS.get(id);
        if (p == null) {
            return;
        }
        p.granted.clear();
        for (String g : granted) {
            if (p.requested.contains(g)) {
                p.granted.add(g);
            }
        }
        try {
            persist(p);
        } catch (Throwable t) {
            OutputLog.INSTANCE.error("Plugins: couldn't save " + id + ": " + t);
        }
        // permissions changed: restart so the new set takes effect
        if (p.enabled && isRunning(id)) {
            stop(id);
            start(id);
        }
    }

    public static synchronized void disableAll() {
        for (Plugin p : new ArrayList<>(PLUGINS.values())) {
            p.enabled = false;
            try {
                persist(p);
            } catch (Throwable ignored) {
            }
        }
        stopAll();
    }

    // ------------------------------------------------------------------ running

    public static void startEnabled() {
        if (!masterEnabled()) {
            return;
        }
        for (Plugin p : list()) {
            if (p.enabled) {
                start(p.id);
            }
        }
    }

    public static void start(final String id) {
        if (!masterEnabled()) {
            return;
        }
        final Plugin p = get(id);
        if (p == null || !p.enabled) {
            return;
        }
        synchronized (RUNNING) {
            if (RUNNING.containsKey(id)) {
                return;
            }
            RUNNING.put(id, null);
        }
        worker().execute(new Runnable() {
            @Override
            public void run() {
                PluginSandbox sb = null;
                try {
                    sb = new PluginSandbox(p);
                    synchronized (RUNNING) {
                        RUNNING.put(id, sb);
                    }
                    sb.runMain();
                    p.faults = 0;
                    OutputLog.INSTANCE.info("[plugin " + p.name + "] loaded");
                } catch (Throwable t) {
                    boolean timeout = sb != null && sb.timedOut;
                    synchronized (RUNNING) {
                        RUNNING.remove(id);
                    }
                    fault(p, "failed to load: " + t, timeout);
                    if (p.enabled) {
                        // a plugin that can't even load is switched off so it doesn't retry forever
                        p.enabled = false;
                        try {
                            persist(p);
                        } catch (Throwable ignored) {
                        }
                        stop(id);
                        notifyUser("Plugin \"" + p.name + "\" failed to load and was disabled. See the Output panel.");
                    }
                }
            }
        });
    }

    public static void stop(String id) {
        synchronized (RUNNING) {
            RUNNING.remove(id);
        }
        for (Button b : BUTTONS) {
            if (b.pluginId.equals(id)) {
                BUTTONS.remove(b);
            }
        }
    }

    public static void stopAll() {
        List<String> ids;
        synchronized (RUNNING) {
            ids = new ArrayList<>(RUNNING.keySet());
        }
        for (String id : ids) {
            stop(id);
        }
    }

    static void fault(final Plugin p, String msg, boolean timedOut) {
        p.lastError = msg;
        p.faults++;
        OutputLog.INSTANCE.error("[plugin " + p.name + "] " + msg);
        if (timedOut || p.faults >= 3) {
            p.enabled = false;
            try {
                persist(p);
            } catch (Throwable ignored) {
            }
            stop(p.id);
            final String why = timedOut ? "ran too long" : "kept failing";
            notifyUser("Plugin \"" + p.name + "\" was disabled because it " + why + ".");
        }
    }

    static void notifyUser(final String msg) {
        MAIN.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Toast.makeText(app, msg, Toast.LENGTH_LONG).show();
                } catch (Throwable ignored) {
                }
            }
        });
    }

    public static void fire(final Button b) {
        if (!masterEnabled()) {
            return;
        }
        worker().execute(new Runnable() {
            @Override
            public void run() {
                PluginSandbox sb;
                synchronized (RUNNING) {
                    sb = RUNNING.get(b.pluginId);
                }
                Plugin p = get(b.pluginId);
                if (sb == null || p == null) {
                    return;
                }
                try {
                    sb.call(b.callback);
                    p.faults = 0;
                } catch (Throwable t) {
                    fault(p, "error in button \"" + b.text + "\": " + t.getMessage(), sb.timedOut);
                }
            }
        });
    }

    static void runCallback(final String pluginId, final LuaValue cb, final String what) {
        worker().execute(new Runnable() {
            @Override
            public void run() {
                PluginSandbox sb;
                synchronized (RUNNING) {
                    sb = RUNNING.get(pluginId);
                }
                Plugin p = get(pluginId);
                if (sb == null || p == null) {
                    return;
                }
                try {
                    sb.call(cb);
                } catch (Throwable t) {
                    fault(p, "error in " + what + ": " + t.getMessage(), sb.timedOut);
                }
            }
        });
    }

    /** Save a plugin the user is editing and run it fresh (used by the Test button). */
    public static synchronized void test(Plugin p) throws Exception {
        String err = validate(p, true);
        if (err != null) {
            throw new IllegalArgumentException(err);
        }
        stop(p.id);
        p.created = true;
        p.enabled = true;
        p.faults = 0;
        p.granted.clear();
        p.granted.addAll(p.requested);
        persist(p);
        PLUGINS.put(p.id, p);
        if (!masterEnabled()) {
            throw new IllegalStateException("Plugins are switched off in Plugin Settings.");
        }
        start(p.id);
    }

    // ------------------------------------------------------------------ threading helper

    static <T> T onMain(PluginSandbox sb, Callable<T> c) {
        long t0 = System.nanoTime();
        FutureTask<T> ft = new FutureTask<>(c);
        MAIN.post(ft);
        try {
            return ft.get(3, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            ft.cancel(false);
            throw new LuaError("the app took too long to respond");
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof LuaError) {
                throw (LuaError) cause;
            }
            throw new LuaError(cause == null ? "error" : String.valueOf(cause.getMessage() != null ? cause.getMessage() : cause));
        } catch (InterruptedException e) {
            throw new LuaError("interrupted");
        } finally {
            sb.extendDeadline(System.nanoTime() - t0);
        }
    }

    // ------------------------------------------------------------------ sandbox

    static final class PluginSandbox {
        final Plugin plugin;
        final Globals g;
        volatile long deadline;
        volatile boolean timedOut;
        private final Map<Integer, Instance> handles = new HashMap<>();
        private final IdentityHashMap<Instance, Integer> reverse = new IdentityHashMap<>();
        private int nextId = 1;
        private long lastNotify;
        private int buttonCount;

        PluginSandbox(Plugin p) {
            this.plugin = p;
            Globals globals = new Globals();
            globals.load(new BaseLib());
            globals.load(new TableLib());
            globals.load(new StringLib());
            globals.load(new JseMathLib());
            globals.load(new Bit32Lib());
            globals.load(new DebugLib());
            LoadState.install(globals);
            LuaC.install(globals);
            this.g = globals;

            for (String n : new String[]{"dofile", "loadfile", "load", "loadstring", "require", "module",
                    "collectgarbage", "getfenv", "setfenv", "package", "io", "os", "luajava"}) {
                globals.set(n, LuaValue.NIL);
            }
            LuaValue str = globals.get("string");
            if (str.istable()) {
                str.set("dump", LuaValue.NIL);
                final LuaValue realRep = str.get("rep");
                str.set("rep", new VarArgFunction() {
                    @Override
                    public Varargs invoke(Varargs a) {
                        long total = (long) a.arg1().tojstring().length() * Math.max(0, a.checkint(2));
                        if (total > 1_000_000L) {
                            throw new LuaError("string.rep result would be larger than 1 MB");
                        }
                        return realRep.invoke(a);
                    }
                });
            }

            // runaway-script guard: checked every few thousand VM instructions
            LuaValue hook = new VarArgFunction() {
                @Override
                public Varargs invoke(Varargs a) {
                    long d = deadline;
                    if (d != 0L && System.nanoTime() > d) {
                        timedOut = true;
                        throw new LuaError("plugin exceeded its time limit");
                    }
                    return NONE;
                }
            };
            globals.get("debug").get("sethook").invoke(LuaValue.varargsOf(new LuaValue[]{hook, LuaValue.valueOf(""), LuaValue.valueOf(5000)}));
            globals.set("debug", LuaValue.NIL);

            installApi();
        }

        void checkSyntax() {
            g.load(plugin.code, "=" + plugin.id);
        }

        void extendDeadline(long nanos) {
            long d = deadline;
            if (d != 0L) {
                deadline = d + nanos;
            }
        }

        private void arm() {
            deadline = System.nanoTime() + timeLimitMs() * 1_000_000L;
            timedOut = false;
        }

        void runMain() {
            LuaValue chunk = g.load(plugin.code, "=" + plugin.id);
            arm();
            try {
                chunk.call();
            } finally {
                deadline = 0L;
            }
        }

        void call(LuaValue f, LuaValue... args) {
            if (f == null || !f.isfunction()) {
                return;
            }
            arm();
            try {
                f.invoke(LuaValue.varargsOf(args));
            } finally {
                deadline = 0L;
            }
        }

        // ---- API

        private interface Impl {
            Varargs run(Varargs a) throws Exception;
        }

        private void def(LuaTable t, final String name, final String perm, final Impl impl) {
            t.set(name, new VarArgFunction() {
                @Override
                public Varargs invoke(Varargs a) {
                    if (perm != null && !plugin.granted.contains(perm)) {
                        throw new LuaError("Permission '" + perm + "' was not granted to this plugin.");
                    }
                    try {
                        return impl.run(a);
                    } catch (LuaError e) {
                        throw e;
                    } catch (Exception e) {
                        throw new LuaError(name + ": " + (e.getMessage() != null ? e.getMessage() : e.toString()));
                    }
                }
            });
        }

        private static String clip(String s, int max) {
            return s.length() > max ? s.substring(0, max) : s;
        }

        private static LuaValue lua(Object o) {
            if (o == null) {
                return LuaValue.NIL;
            }
            if (o instanceof Boolean) {
                return LuaValue.valueOf(((Boolean) o).booleanValue());
            }
            if (o instanceof Number) {
                return LuaValue.valueOf(((Number) o).doubleValue());
            }
            return LuaValue.valueOf(String.valueOf(o));
        }

        private LuaValue handle(Instance i) {
            Integer id = reverse.get(i);
            if (id == null) {
                if (handles.size() >= 5000) {
                    throw new LuaError("too many object handles in use");
                }
                id = Integer.valueOf(nextId++);
                handles.put(id, i);
                reverse.put(i, id);
            }
            LuaTable t = new LuaTable();
            t.set("id", id.intValue());
            t.set("name", LuaValue.valueOf(i.getName()));
            t.set("className", LuaValue.valueOf(i.getClassName()));
            return t;
        }

        private Instance resolve(LuaValue v) {
            LuaValue idv = v.istable() ? v.get("id") : v;
            if (!idv.isnumber()) {
                throw new LuaError("expected an object handle");
            }
            Instance i = handles.get(Integer.valueOf(idv.toint()));
            if (i == null) {
                throw new LuaError("unknown object handle " + idv.toint());
            }
            return i;
        }

        private void installApi() {
            LuaTable app = new LuaTable();
            app.set("apiVersion", API_VERSION);
            LuaTable info = new LuaTable();
            info.set("id", LuaValue.valueOf(plugin.id));
            info.set("name", LuaValue.valueOf(plugin.name));
            info.set("version", LuaValue.valueOf(plugin.version));
            app.set("plugin", info);

            final Impl logImpl = new Impl() {
                @Override
                public Varargs run(Varargs a) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 1; i <= a.narg(); i++) {
                        if (i > 1) {
                            sb.append(' ');
                        }
                        sb.append(a.arg(i).tojstring());
                    }
                    OutputLog.INSTANCE.info("[" + plugin.name + "] " + clip(sb.toString(), 2000));
                    return LuaValue.NONE;
                }
            };
            def(app, "log", null, logImpl);
            def(g, "print", null, logImpl);

            def(app, "notify", "notify", new Impl() {
                @Override
                public Varargs run(Varargs a) {
                    long now = System.currentTimeMillis();
                    if (now - lastNotify < 500) {
                        return LuaValue.NONE;
                    }
                    lastNotify = now;
                    final String msg = clip(a.checkjstring(1), 200);
                    onMain(PluginSandbox.this, new Callable<Void>() {
                        @Override
                        public Void call() {
                            Toast.makeText(NativePlugins.app, plugin.icon + " " + msg, Toast.LENGTH_SHORT).show();
                            return null;
                        }
                    });
                    return LuaValue.NONE;
                }
            });

            // ---- project
            LuaTable project = new LuaTable();
            def(project, "getInfo", "read_project", new Impl() {
                @Override
                public Varargs run(Varargs a) {
                    return onMain(PluginSandbox.this, new Callable<Varargs>() {
                        @Override
                        public Varargs call() {
                            DataModel dm = studio.getEditModel();
                            LuaTable t = new LuaTable();
                            String fn = studio.getFileName();
                            t.set("fileName", fn == null ? LuaValue.NIL : LuaValue.valueOf(fn));
                            ArrayList<Instance> all = new ArrayList<>();
                            dm.descendants(all);
                            t.set("descendantCount", all.size());
                            LuaTable services = new LuaTable();
                            int n = 1;
                            for (Instance c : dm.getChildren()) {
                                services.set(n++, LuaValue.valueOf(c.getName()));
                            }
                            t.set("services", services);
                            t.set("isPlaying", LuaValue.valueOf(studio.isPlaying()));
                            return t;
                        }
                    });
                }
            });
            def(project, "find", "read_project", new Impl() {
                @Override
                public Varargs run(Varargs a) {
                    final String path = clip(a.checkjstring(1), 400);
                    return onMain(PluginSandbox.this, new Callable<Varargs>() {
                        @Override
                        public Varargs call() {
                            DataModel dm = studio.getEditModel();
                            String[] parts = path.split("\\.");
                            Instance cur = null;
                            for (int i = 0; i < parts.length; i++) {
                                if (i == 0) {
                                    cur = dm.findService(parts[0]);
                                    if (cur == null) {
                                        cur = dm.findFirstChild(parts[0], false);
                                    }
                                } else if (cur != null) {
                                    cur = cur.findFirstChild(parts[i], false);
                                }
                                if (cur == null) {
                                    return LuaValue.NIL;
                                }
                            }
                            return cur == null ? LuaValue.NIL : handle(cur);
                        }
                    });
                }
            });
            def(project, "children", "read_project", new Impl() {
                @Override
                public Varargs run(Varargs a) {
                    final LuaValue target = a.arg1();
                    return onMain(PluginSandbox.this, new Callable<Varargs>() {
                        @Override
                        public Varargs call() {
                            Instance inst = resolve(target);
                            LuaTable t = new LuaTable();
                            int n = 1;
                            for (Instance c : inst.getChildren()) {
                                if (n > 500) {
                                    break;
                                }
                                t.set(n++, handle(c));
                            }
                            return t;
                        }
                    });
                }
            });
            app.set("project", project);

            // ---- selection
            LuaTable selection = new LuaTable();
            def(selection, "get", "selection", new Impl() {
                @Override
                public Varargs run(Varargs a) {
                    return onMain(PluginSandbox.this, new Callable<Varargs>() {
                        @Override
                        public Varargs call() {
                            LuaTable t = new LuaTable();
                            int n = 1;
                            for (Instance i : new ArrayList<>(studio.getSelection())) {
                                if (n > 200) {
                                    break;
                                }
                                t.set(n++, handle(i));
                            }
                            return t;
                        }
                    });
                }
            });
            app.set("selection", selection);

            // ---- objects
            LuaTable objects = new LuaTable();
            def(objects, "create", "create_objects", new Impl() {
                @Override
                public Varargs run(Varargs a) {
                    final String cls = a.checkjstring(1);
                    final LuaValue opts = a.arg(2);
                    return onMain(PluginSandbox.this, new Callable<Varargs>() {
                        @Override
                        public Varargs call() {
                            if (studio.isPlaying()) {
                                throw new LuaError("stop the playtest before editing the place");
                            }
                            if (!ClassDb.INSTANCE.getInsertable().contains(cls)) {
                                throw new LuaError("'" + cls + "' can't be created by plugins (it isn't an Insert-menu class)");
                            }
                            Instance inst = InstanceFactory.INSTANCE.create(cls);
                            if (opts.istable() && opts.get("name").isstring()) {
                                inst.setName(clip(opts.get("name").tojstring(), 100));
                            }
                            Instance parent;
                            if (opts.istable() && !opts.get("parent").isnil()) {
                                parent = resolve(opts.get("parent"));
                            } else {
                                parent = studio.getEditModel().getService("Workspace");
                            }
                            inst.setParent(parent, -1);
                            studio.markDirty();
                            return handle(inst);
                        }
                    });
                }
            });
            def(objects, "getProperty", "read_project", new Impl() {
                @Override
                public Varargs run(Varargs a) {
                    final LuaValue target = a.arg1();
                    final String prop = a.checkjstring(2);
                    return onMain(PluginSandbox.this, new Callable<Varargs>() {
                        @Override
                        public Varargs call() {
                            Instance inst = resolve(target);
                            Object v = inst.get(prop);
                            return lua(v);
                        }
                    });
                }
            });
            def(objects, "setProperty", "modify_properties", new Impl() {
                @Override
                public Varargs run(Varargs a) {
                    final LuaValue target = a.arg1();
                    final String prop = a.checkjstring(2);
                    final LuaValue val = a.arg(3);
                    return onMain(PluginSandbox.this, new Callable<Varargs>() {
                        @Override
                        public Varargs call() {
                            if (studio.isPlaying()) {
                                throw new LuaError("stop the playtest before editing the place");
                            }
                            Instance inst = resolve(target);
                            if ("Source".equals(prop) || "Parent".equals(prop)) {
                                throw new LuaError("'" + prop + "' can't be changed by plugins in API v" + API_VERSION);
                            }
                            if ("Name".equals(prop)) {
                                inst.setName(clip(val.checkjstring(), 100));
                                studio.markDirty();
                                return LuaValue.NONE;
                            }
                            Object existing = inst.get(prop);
                            Object next;
                            if (existing instanceof Boolean) {
                                next = Boolean.valueOf(val.toboolean());
                            } else if (existing instanceof Integer) {
                                next = Integer.valueOf((int) Math.round(val.checkdouble()));
                            } else if (existing instanceof Long) {
                                next = Long.valueOf(Math.round(val.checkdouble()));
                            } else if (existing instanceof Float) {
                                next = Float.valueOf((float) val.checkdouble());
                            } else if (existing instanceof Double) {
                                next = Double.valueOf(val.checkdouble());
                            } else if (existing instanceof String) {
                                next = clip(val.checkjstring(), 10000);
                            } else {
                                throw new LuaError("'" + prop + "' doesn't exist or isn't a text / number / on-off property (API v" + API_VERSION + " only edits those)");
                            }
                            inst.set(prop, next, true);
                            studio.markDirty();
                            return LuaValue.NONE;
                        }
                    });
                }
            });
            app.set("objects", objects);

            // ---- ui
            LuaTable ui = new LuaTable();
            def(ui, "addToolbarButton", "ui_toolbar", new Impl() {
                @Override
                public Varargs run(Varargs a) {
                    LuaValue o = a.arg1();
                    if (!o.istable() || !o.get("onClick").isfunction() || !o.get("text").isstring()) {
                        throw new LuaError("expected {text = '...', onClick = function}");
                    }
                    if (buttonCount >= MAX_BUTTONS) {
                        throw new LuaError("a plugin can add at most " + MAX_BUTTONS + " buttons");
                    }
                    Button b = new Button();
                    b.pluginId = plugin.id;
                    b.text = clip(o.get("text").tojstring(), 24);
                    b.id = o.get("id").isstring() ? clip(o.get("id").tojstring(), 40) : b.text;
                    b.icon = o.get("icon").isstring() ? clip(o.get("icon").tojstring(), 4) : "";
                    b.callback = o.get("onClick");
                    for (Button existing : BUTTONS) {
                        if (existing.pluginId.equals(b.pluginId) && existing.id.equals(b.id)) {
                            BUTTONS.remove(existing);
                            buttonCount--;
                        }
                    }
                    BUTTONS.add(b);
                    buttonCount++;
                    return LuaValue.NONE;
                }
            });
            def(ui, "showPanel", "ui_panel", new Impl() {
                @Override
                public Varargs run(Varargs a) {
                    final LuaValue o = a.arg1();
                    if (!o.istable()) {
                        throw new LuaError("expected {title = '...', text = '...', buttons = {...}}");
                    }
                    final String title = clip(o.get("title").isstring() ? o.get("title").tojstring() : plugin.name, 60);
                    final String text = clip(o.get("text").isstring() ? o.get("text").tojstring() : "", 4000);
                    final List<String> labels = new ArrayList<>();
                    final List<LuaValue> callbacks = new ArrayList<>();
                    LuaValue bs = o.get("buttons");
                    if (bs.istable()) {
                        for (int i = 1; i <= 4; i++) {
                            LuaValue b = bs.get(i);
                            if (b.istable() && b.get("text").isstring()) {
                                labels.add(clip(b.get("text").tojstring(), 30));
                                callbacks.add(b.get("onClick"));
                            }
                        }
                    }
                    onMain(PluginSandbox.this, new Callable<Void>() {
                        @Override
                        public Void call() {
                            PluginPanels.show(activity, plugin, title, text, labels, callbacks);
                            return null;
                        }
                    });
                    return LuaValue.NONE;
                }
            });
            app.set("ui", ui);

            // ---- files (private folder only)
            LuaTable files = new LuaTable();
            def(files, "list", "files", new Impl() {
                @Override
                public Varargs run(Varargs a) {
                    LuaTable t = new LuaTable();
                    File[] fs = dataDir(plugin.id).listFiles();
                    if (fs != null) {
                        int n = 1;
                        for (File f : fs) {
                            t.set(n++, LuaValue.valueOf(f.getName()));
                        }
                    }
                    return t;
                }
            });
            def(files, "read", "files", new Impl() {
                @Override
                public Varargs run(Varargs a) throws Exception {
                    File f = fileFor(a.checkjstring(1));
                    if (!f.exists()) {
                        return LuaValue.NIL;
                    }
                    return LuaValue.valueOf(readFile(f, MAX_FILE));
                }
            });
            def(files, "write", "files", new Impl() {
                @Override
                public Varargs run(Varargs a) throws Exception {
                    File f = fileFor(a.checkjstring(1));
                    String content = a.checkjstring(2);
                    if (content.getBytes(StandardCharsets.UTF_8).length > MAX_FILE) {
                        throw new LuaError("file is larger than 256 KB");
                    }
                    long used = 0;
                    File[] fs = dataDir(plugin.id).listFiles();
                    if (fs != null) {
                        for (File x : fs) {
                            if (!x.equals(f)) {
                                used += x.length();
                            }
                        }
                    }
                    if (used + content.length() > MAX_DATA_DIR) {
                        throw new LuaError("the plugin's data folder would exceed 1 MB");
                    }
                    writeFile(f, content);
                    return LuaValue.NONE;
                }
            });
            app.set("files", files);

            g.set("app", app);
        }

        private File fileFor(String name) {
            if (!FILE_RE.matcher(name).matches()) {
                throw new LuaError("invalid file name (use letters, numbers, . _ -, up to 64 characters)");
            }
            return new File(dataDir(plugin.id), name);
        }
    }

    // ------------------------------------------------------------------ docs + samples

    public static final String API_DOCS =
            "MOBILE STUDIO PLUGIN API  (v" + API_VERSION + ")\n"
            + "Plugins are Lua scripts. They run in a sandbox: no io, os, require, load, debug or network.\n"
            + "Each call needs the permission shown in [brackets]; the user grants permissions.\n"
            + "A script that runs longer than the time limit (Plugin Settings) is stopped and disabled.\n\n"
            + "GENERAL\n"
            + "app.log(...)            write to the Output panel\n"
            + "print(...)              same as app.log\n"
            + "app.plugin              {id, name, version}\n"
            + "app.apiVersion          number\n"
            + "app.notify(text)        [notify] short pop-up message\n\n"
            + "PROJECT\n"
            + "app.project.getInfo()   [read_project] {fileName, descendantCount, services = {names}, isPlaying}\n"
            + "app.project.find(path)  [read_project] handle or nil, e.g. 'Workspace.Baseplate'\n"
            + "app.project.children(h) [read_project] list of handles (max 500)\n\n"
            + "SELECTION\n"
            + "app.selection.get()     [selection] list of handles (max 200)\n\n"
            + "Handles are tables {id, name, className}. Pass a handle (or its id) to the functions below.\n\n"
            + "OBJECTS\n"
            + "app.objects.create(className, {name=, parent=handle})  [create_objects] returns handle\n"
            + "    Only classes from the Insert menu. Parent defaults to Workspace.\n"
            + "app.objects.getProperty(h, name)       [read_project] string / number / boolean\n"
            + "app.objects.setProperty(h, name, v)    [modify_properties]\n"
            + "    Only text, number and on/off properties that already exist. 'Source' and 'Parent' are blocked.\n"
            + "    Editing is refused while a playtest is running. Changes are not added to Undo yet.\n\n"
            + "UI\n"
            + "app.ui.addToolbarButton{text=, id=, icon=, onClick=function}  [ui_toolbar]\n"
            + "    Buttons appear in File menu > Plugins section. Max 5 per plugin.\n"
            + "app.ui.showPanel{title=, text=, buttons={{text=, onClick=function}}}  [ui_panel]\n\n"
            + "FILES\n"
            + "app.files.list()  app.files.read(name)  app.files.write(name, text)  [files]\n"
            + "    Private folder per plugin, file names like 'notes.txt', 256 KB per file, 1 MB total.\n\n"
            + "NOT AVAILABLE\n"
            + "Roblox web APIs, API keys, cookies, tokens, other plugins' data, the rest of the phone's storage.\n";

    public static final String SAMPLE_HELLO =
            "app.log('Hello plugin loaded')\n"
            + "app.ui.addToolbarButton{text='Count objects', onClick=function()\n"
            + "  local info = app.project.getInfo()\n"
            + "  app.notify(info.descendantCount .. ' objects in this place')\n"
            + "end}\n";

    public static final String SAMPLE_RENAME =
            "app.ui.addToolbarButton{text='Prefix selection', onClick=function()\n"
            + "  local sel = app.selection.get()\n"
            + "  if #sel == 0 then app.notify('Select something first') return end\n"
            + "  for i, h in ipairs(sel) do\n"
            + "    app.objects.setProperty(h, 'Name', 'My_' .. h.name)\n"
            + "  end\n"
            + "  app.notify('Renamed ' .. #sel .. ' object(s)')\n"
            + "end}\n"
            + "app.ui.addToolbarButton{text='Add folder', onClick=function()\n"
            + "  app.objects.create('Folder', {name='PluginFolder'})\n"
            + "  app.notify('Folder added to Workspace')\n"
            + "end}\n";
}

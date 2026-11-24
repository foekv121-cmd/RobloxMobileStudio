package com.goydevv.studrblx.core;

import android.content.SharedPreferences;
import com.goydevv.studrblx.format.RbxFile;
import com.goydevv.studrblx.lua.LuaRuntime;
import com.goydevv.studrblx.model.EnumValue;
import com.goydevv.studrblx.model.Instance;
import com.goydevv.studrblx.model.InstanceFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.collections.SetsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.io.FilesKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import kotlin.text.Regex;
import kotlin.text.StringsKt;
import org.luaj.vm2.LuaValue;

/* JADX INFO: compiled from: PluginManager.kt */
/* JADX INFO: loaded from: classes4.dex */
public final class PluginManager {
    private int loadGen;
    private final ArrayList<Instance> loaded;

    /* JADX INFO: renamed from: prefs$delegate, reason: from kotlin metadata */
    private final Lazy prefs;
    private final Studio studio;

    public PluginManager(Studio studio) {
        Intrinsics.checkNotNullParameter(studio, "studio");
        this.studio = studio;
        this.prefs = LazyKt.lazy(new Function0() { // from class: com.goydevv.studrblx.core.PluginManager$$ExternalSyntheticLambda3
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return PluginManager.this.studio.getApp().getSharedPreferences("plugins_state", 0);
            }
        });
        this.loaded = new ArrayList<>();
    }

    public final File getDir() {
        File file = new File(this.studio.getApp().getFilesDir(), "plugins");
        file.mkdirs();
        return file;
    }

    private final SharedPreferences getPrefs() {
        return (SharedPreferences) this.prefs.getValue();
    }

    public final List<File> list() {
        File[] fileArrListFiles = getDir().listFiles();
        if (fileArrListFiles != null) {
            Collection arrayList = new ArrayList();
            for (File file : fileArrListFiles) {
                if (file.isFile()) {
                    arrayList.add(file);
                }
            }
            List<File> listSortedWith = CollectionsKt.sortedWith((List) arrayList, new Comparator() { // from class: com.goydevv.studrblx.core.PluginManager$list$$inlined$sortedBy$1
                /* JADX WARN: Multi-variable type inference failed */
                @Override // java.util.Comparator
                public final int compare(Object t, Object t2) {
                    String name = ((File) t).getName();
                    Intrinsics.checkNotNullExpressionValue(name, "getName(...)");
                    String lowerCase = name.toLowerCase(Locale.ROOT);
                    Intrinsics.checkNotNullExpressionValue(lowerCase, "toLowerCase(...)");
                    String name2 = ((File) t2).getName();
                    Intrinsics.checkNotNullExpressionValue(name2, "getName(...)");
                    String lowerCase2 = name2.toLowerCase(Locale.ROOT);
                    Intrinsics.checkNotNullExpressionValue(lowerCase2, "toLowerCase(...)");
                    return ComparisonsKt.compareValues(lowerCase, lowerCase2);
                }
            });
            if (listSortedWith != null) {
                return listSortedWith;
            }
        }
        return CollectionsKt.emptyList();
    }

    public final boolean isEnabled(File f) {
        Intrinsics.checkNotNullParameter(f, "f");
        return !getPrefs().getBoolean("off:" + f.getName(), false);
    }

    public final void setEnabled(File f, boolean on) {
        Intrinsics.checkNotNullParameter(f, "f");
        getPrefs().edit().putBoolean("off:" + f.getName(), !on).apply();
    }

    public final Pair<byte[], String> detect(byte[] bytes) {
        boolean z;
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        byte[] b = RbxFile.INSTANCE.unwrap(bytes);
        if (RbxFile.INSTANCE.isBinary(b)) {
            return TuplesKt.to(b, "rbxm");
        }
        if (RbxFile.INSTANCE.isXml(b)) {
            return TuplesKt.to(b, "rbxmx");
        }
        boolean looksText = false;
        CharSequence head = new String(b, 0, Math.min(b.length, 4096), Charsets.UTF_8);
        CharSequence charSequence = head;
        int i = 0;
        while (true) {
            if (i >= charSequence.length()) {
                z = true;
                break;
            }
            if ((charSequence.charAt(i) == 0 ? (char) 1 : (char) 0) != 0) {
                z = false;
                break;
            }
            i++;
        }
        if (z) {
            CharSequence charSequence2 = head;
            int i2 = 0;
            for (int i3 = 0; i3 < charSequence2.length(); i3++) {
                char cCharAt = charSequence2.charAt(i3);
                if (((!Character.isISOControl(cCharAt) || cCharAt == '\n' || cCharAt == '\r' || cCharAt == '\t') ? (char) 0 : (char) 1) != 0) {
                    i2++;
                }
            }
            if (i2 < 4) {
                looksText = true;
            }
        }
        if (looksText && new Regex("\\b(local|function|end|return|plugin|game|print|require)\\b").containsMatchIn(head)) {
            return TuplesKt.to(b, "lua");
        }
        throw new IllegalArgumentException("This file isn't a Roblox plugin. Expected a model file (.rbxm/.rbxmx, or a .bin downloaded from Roblox) or Lua source.");
    }

    public final File install(String name, byte[] bytes) {
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        Pair<byte[], String> pairDetect = detect(bytes);
        byte[] data = pairDetect.component1();
        String ext = pairDetect.component2();
        String base = com.goydevv.studrblx.util.KtCompat.substringAfterLast(com.goydevv.studrblx.util.KtCompat.substringAfterLast(name, '/'), '\\');
        Set known = SetsKt.setOf(new String[]{"rbxm", "rbxmx", "lua", "luau", "txt", "bin", "rbxl", "rbxlx"});
        String lowerCase = StringsKt.substringAfterLast(base, '.', "").toLowerCase(Locale.ROOT);
        Intrinsics.checkNotNullExpressionValue(lowerCase, "toLowerCase(...)");
        if (known.contains(lowerCase)) {
            base = com.goydevv.studrblx.util.KtCompat.substringBeforeLast(base, '.');
        }
        String string = StringsKt.trim((CharSequence) new Regex("[^A-Za-z0-9._ -]").replace(base, "_")).toString();
        if (StringsKt.isBlank(string)) {
            string = "Plugin";
        }
        File f = new File(getDir(), StringsKt.take(string, 60) + "." + ext);
        FilesKt.writeBytes(f, data);
        setEnabled(f, true);
        return f;
    }

    public final void remove(File f) {
        Intrinsics.checkNotNullParameter(f, "f");
        f.delete();
        getPrefs().edit().remove("off:" + f.getName()).apply();
    }

    public final void loadAll() {
        ArrayList<Instance> children;
        Iterable list;
        Iterable list2 = list();
        synchronized (this.studio.getLock()) {
            Iterator<Instance> it = this.loaded.iterator();
            Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
            while (it.hasNext()) {
                Instance next = it.next();
                this.studio.getEditRuntime().fire(next, "Unloading", new LuaValue[0]);
                this.studio.getPluginMice().remove(next);
            }
            this.loaded.clear();
            this.studio.deactivatePlugin(null);
            this.studio.getPluginButtons().clear();
            this.studio.getPluginWidgets().clear();
            Instance instanceFindService = this.studio.getEditModel().findService("CoreGui");
            if (instanceFindService != null && (children = instanceFindService.getChildren()) != null && (list = CollectionsKt.toList(children)) != null) {
                Iterator it2 = list.iterator();
                while (it2.hasNext()) {
                    ((Instance) it2.next()).destroy();
                }
                Unit unit = Unit.INSTANCE;
            }
        }
        this.loadGen++;
        final int gen = this.loadGen;
        Collection arrayList = new ArrayList();
        for (Object obj : list2) {
            if (isEnabled((File) obj)) {
                arrayList.add(obj);
            }
        }
        final List queue = (List) arrayList;
        this.studio.notifyPluginButtons();
        this.studio.getMain().post(new Runnable() { // from class: com.goydevv.studrblx.core.PluginManager$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                PluginManager.loadAll$next(gen, PluginManager.this, queue, 0);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void loadAll$next(final int gen, final PluginManager this$0, final List<? extends File> list, final int i) {
        if (gen != this$0.loadGen) {
            return;
        }
        if (i >= list.size()) {
            this$0.studio.notifyPluginButtons();
            return;
        }
        this$0.load(list.get(i));
        this$0.studio.notifyPluginButtons();
        this$0.studio.getMain().post(new Runnable() { // from class: com.goydevv.studrblx.core.PluginManager$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                PluginManager.loadAll$next(gen, this$0, list, i + 1);
            }
        });
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r14v10 */
    /* JADX WARN: Type inference failed for: r14v11 */
    /* JADX WARN: Type inference failed for: r14v14 */
    public final void load(File f) {
        String second;
        Intrinsics.checkNotNullParameter(f, "f");
        try {
            final Instance instanceCreate = InstanceFactory.INSTANCE.create("Plugin");
            instanceCreate.setName(FilesKt.getNameWithoutExtension(f));
            ArrayList arrayList = new ArrayList();
            String lowerCase = FilesKt.getExtension(f).toLowerCase(Locale.ROOT);
            Intrinsics.checkNotNullExpressionValue(lowerCase, "toLowerCase(...)");
            byte[] bytes = FilesKt.readBytes(f);
            boolean z = false;
            if (SetsKt.setOf(new String[]{"lua", "luau", "txt"}).contains(lowerCase)) {
                second = "lua";
            } else {
                try {
                    second = detect(bytes).getSecond();
                } catch (Exception e) {
                    second = lowerCase;
                }
            }
            if (Intrinsics.areEqual(second, "lua")) {
                Instance instanceCreate2 = InstanceFactory.INSTANCE.create("Script");
                instanceCreate2.setName(FilesKt.getNameWithoutExtension(f));
                instanceCreate2.set("Source", new String(RbxFile.INSTANCE.unwrap(bytes), Charsets.UTF_8), false);
                instanceCreate2.attachRaw(instanceCreate);
                arrayList.add(instanceCreate2);
            } else {
                Iterator<Instance> it = RbxFile.INSTANCE.read(bytes).getRoots().iterator();
                while (it.hasNext()) {
                    it.next().attachRaw(instanceCreate);
                }
                for (Instance instance : Instance.descendants$default(instanceCreate, null, 1, null)) {
                    if (Intrinsics.areEqual(instance.get("Disabled"), (Object) true)) {
                        z = false;
                    } else if (!Intrinsics.areEqual(instance.get("Enabled"), Boolean.valueOf(z))) {
                        Object obj = instance.get("RunContext");
                        EnumValue enumValue = obj instanceof EnumValue ? (EnumValue) obj : null;
                        int value = enumValue != null ? enumValue.getValue() : (z ? 1 : 0);
                        if ((Intrinsics.areEqual(instance.getClassName(), "Script") && (value == 0 || value == 3)) || Intrinsics.areEqual(instance.getClassName(), "LocalScript")) {
                            arrayList.add(instance);
                        }
                        z = false;
                    }
                }
            }
            if (arrayList.isEmpty()) {
                OutputLog.INSTANCE.warn("Plugin " + f.getName() + " has no Scripts to run");
                return;
            }
            synchronized (this.studio.getLock()) {
                LuaRuntime editRuntime = this.studio.getEditRuntime();
                LuaValue luaValueWrap = editRuntime.wrap(instanceCreate);
                this.loaded.add(instanceCreate);
                Iterator it2 = arrayList.iterator();
                Intrinsics.checkNotNullExpressionValue(it2, "iterator(...)");
                while (it2.hasNext()) {
                    try { editRuntime.runScript((Instance) it2.next(), MapsKt.mapOf(TuplesKt.to("plugin", luaValueWrap))); } catch (Throwable _ktEx) { throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx); }
                }
                Unit unit = Unit.INSTANCE;
            }
            OutputLog.INSTANCE.info("Loaded plugin " + FilesKt.getNameWithoutExtension(f));
            this.studio.getMain().postDelayed(new Runnable() { // from class: com.goydevv.studrblx.core.PluginManager$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    PluginManager.load$lambda$14(PluginManager.this, instanceCreate);
                }
            }, 4000L);
        } catch (Exception e2) {
            OutputLog.INSTANCE.error("Failed to load plugin " + f.getName() + ": " + e2.getMessage());
        }
    }

    static final void load$lambda$14(PluginManager this$0, Instance $pluginInst) {
        Iterable pluginButtons = this$0.studio.getPluginButtons();
        boolean z = true;
        if (!(pluginButtons instanceof Collection) || !((Collection) pluginButtons).isEmpty()) {
            Iterator it = pluginButtons.iterator();
            while (it.hasNext()) {
                if (Intrinsics.areEqual(((PluginButton) it.next()).getPluginName(), $pluginInst.getName())) {
                    z = false;
                    break;
                }
            }
        }
        if (z) {
            OutputLog.INSTANCE.warn("Plugin " + $pluginInst.getName() + " hasn't added a toolbar button yet. If it uses features this app doesn't have, its errors are shown above.");
        }
    }
}

package com.goydevv.studrblx.lua;

import android.content.Context;
import com.goydevv.studrblx.core.OutputLog;
import com.goydevv.studrblx.core.Studio;
import com.goydevv.studrblx.engine.PlaySession;
import com.goydevv.studrblx.model.BinaryString;
import com.goydevv.studrblx.model.BrickColorV;
import com.goydevv.studrblx.model.BrickColors;
import com.goydevv.studrblx.model.CFrame;
import com.goydevv.studrblx.model.Color3;
import com.goydevv.studrblx.model.ContentValue;
import com.goydevv.studrblx.model.DataModel;
import com.goydevv.studrblx.model.EnumValue;
import com.goydevv.studrblx.model.Enums;
import com.goydevv.studrblx.model.Instance;
import com.goydevv.studrblx.model.OptionalCFrame;
import com.goydevv.studrblx.model.PropNames;
import com.goydevv.studrblx.model.SharedStr;
import com.goydevv.studrblx.model.TreeListener;
import com.goydevv.studrblx.model.UDim2;
import com.goydevv.studrblx.model.Vector3;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.collections.SetsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.io.CloseableKt;
import kotlin.io.FilesKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.Charsets;
import kotlin.text.Regex;
import kotlin.text.StringsKt;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.LuaBoolean;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaInteger;
import org.luaj.vm2.LuaNumber;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaThread;
import org.luaj.vm2.LuaUserdata;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Prototype;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.compiler.DumpState;
import org.luaj.vm2.lib.DebugLib;
import org.luaj.vm2.lib.jse.JsePlatform;

/* JADX INFO: compiled from: LuaRuntime.kt */
/* JADX INFO: loaded from: classes4.dex */
public final class LuaRuntime {
    private static final long WAIT_BUDGET_NS = 8000000L;
    private static volatile File luacDir;
    private static volatile String stamp;
    private final InstanceApi api;
    private final IdentityHashMap<Instance, HashMap<String, LuaValue>> callbacks;
    private final ArrayList<ChildWait> childWaits;

    /* JADX INFO: renamed from: commandEnv$delegate, reason: from kotlin metadata */
    private final Lazy commandEnv;
    private final IdentityHashMap<Object, HashMap<String, ArrayList<Connection>>> conns;
    private final LuaValue debugGetinfo;
    private final ArrayList<Pair<LuaThread, Varargs>> deferred;
    private final Set<String> directEvents;
    private final DataModel dm;
    private Map<String, ? extends LuaValue> extraEnvForModules;
    private final Globals globals;
    private final LuaValue hookFn;
    private final LuaTable instanceMeta;
    private final boolean isPlay;
    private final ConcurrentLinkedQueue<Function0<Unit>> mainQueue;
    private final IdentityHashMap<Instance, LuaValue> moduleCache;
    private final IdentityHashMap<LuaThread, ArrayList<LuaTable>> moduleEnvs;
    private PlaySession play;
    private long resumeStart;
    private final LuaValue sethook;
    private final LuaTable sharedTable;
    private final IdentityHashMap<Object, HashMap<String, ArrayList<LuaThread>>> signalWaiters;
    private boolean stopped;
    private final Studio studio;
    private double time;
    private final ArrayList<Pair<Double, Function0<Unit>>> timers;
    private final TreeListener treeListener;
    private final ArrayList<Tween> tweens;
    private final ArrayList<Waiting> waiting;
    private Instance focusedTextBox;

    public final Instance getFocusedTextBox() {
        return this.focusedTextBox;
    }

    public final void setFocusedTextBox(Instance inst) {
        this.focusedTextBox = inst;
    }
    private final WeakHashMap<Instance, LuaValue> wrappers;

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static final LuaRuntime$Companion$protoCache$1 protoCache = new LuaRuntime$Companion$protoCache$1();

    public LuaRuntime(Studio studio, DataModel dm, boolean isPlay) {
        Intrinsics.checkNotNullParameter(studio, "studio");
        Intrinsics.checkNotNullParameter(dm, "dm");
        this.studio = studio;
        this.dm = dm;
        this.isPlay = isPlay;
        Globals globalsStandardGlobals = JsePlatform.standardGlobals();
        Intrinsics.checkNotNullExpressionValue(globalsStandardGlobals, "standardGlobals(...)");
        this.globals = globalsStandardGlobals;
        this.instanceMeta = new LuaTable();
        this.api = new InstanceApi(this);
        this.waiting = new ArrayList<>();
        this.deferred = new ArrayList<>();
        this.childWaits = new ArrayList<>();
        this.conns = new IdentityHashMap<>();
        this.signalWaiters = new IdentityHashMap<>();
        this.tweens = new ArrayList<>();
        this.moduleCache = new IdentityHashMap<>();
        this.wrappers = new WeakHashMap<>();
        this.callbacks = new IdentityHashMap<>();
        this.mainQueue = new ConcurrentLinkedQueue<>();
        this.sharedTable = new LuaTable();
        this.directEvents = SetsKt.setOf(new String[]{"Heartbeat", "RenderStepped", "Stepped", "PreSimulation", "PostSimulation", "PreRender", "Changed", "AttributeChanged"});
        this.treeListener = new TreeListener() { // from class: com.goydevv.studrblx.lua.LuaRuntime$treeListener$1
            @Override // com.goydevv.studrblx.model.TreeListener
            public void childAdded(Instance parent, Instance child) {
                PlaySession play;
                Intrinsics.checkNotNullParameter(parent, "parent");
                Intrinsics.checkNotNullParameter(child, "child");
                if (LuaRuntime.this.getStopped()) {
                    return;
                }
                if (LuaRuntime.this.hasConn(parent, "ChildAdded")) {
                    LuaRuntime.this.fire(parent, "ChildAdded", LuaRuntime.this.wrap(child));
                }
                for (Instance p = parent; p != null; p = p.getParent()) {
                    if (LuaRuntime.this.hasConn(p, "DescendantAdded")) {
                        LuaRuntime.this.fire(p, "DescendantAdded", LuaRuntime.this.wrap(child));
                        for (Instance d : Instance.descendants$default(child, null, 1, null)) {
                            LuaRuntime.this.fire(p, "DescendantAdded", LuaRuntime.this.wrap(d));
                        }
                    }
                }
                LuaRuntime.this.notifyAncestry(child);
                if (!LuaRuntime.this.childWaits.isEmpty()) {
                    LuaRuntime.this.checkChildWaits();
                }
                if (!LuaRuntime.this.getIsPlay() || (play = LuaRuntime.this.getPlay()) == null) {
                    return;
                }
                play.onChildAdded(parent, child);
            }

            @Override // com.goydevv.studrblx.model.TreeListener
            public void childRemoved(Instance parent, Instance child) {
                PlaySession play;
                Intrinsics.checkNotNullParameter(parent, "parent");
                Intrinsics.checkNotNullParameter(child, "child");
                if (LuaRuntime.this.getStopped()) {
                    return;
                }
                if (LuaRuntime.this.hasConn(parent, "ChildRemoved")) {
                    LuaRuntime.this.fire(parent, "ChildRemoved", LuaRuntime.this.wrap(child));
                }
                for (Instance p = parent; p != null; p = p.getParent()) {
                    if (LuaRuntime.this.hasConn(p, "DescendantRemoving")) {
                        LuaRuntime.this.fire(p, "DescendantRemoving", LuaRuntime.this.wrap(child));
                    }
                }
                LuaRuntime.this.notifyAncestry(child);
                if (child.getDestroyed()) {
                    if (LuaRuntime.this.hasConn(child, "Destroying")) {
                        LuaRuntime.this.fire(child, "Destroying", new LuaValue[0]);
                    }
                    LuaRuntime.this.disconnectTree(child);
                }
                if (!LuaRuntime.this.getIsPlay() || (play = LuaRuntime.this.getPlay()) == null) {
                    return;
                }
                play.onChildRemoved(parent, child);
            }

            @Override // com.goydevv.studrblx.model.TreeListener
            public void propChanged(Instance inst, String name) {
                PlaySession play;
                Intrinsics.checkNotNullParameter(inst, "inst");
                Intrinsics.checkNotNullParameter(name, "name");
                if (LuaRuntime.this.getStopped()) {
                    return;
                }
                if (Intrinsics.areEqual(name, "Name") && !LuaRuntime.this.childWaits.isEmpty()) {
                    LuaRuntime.this.checkChildWaits();
                }
                if (LuaRuntime.this.getIsPlay() && Intrinsics.areEqual(name, "Disabled") && inst.isA("BaseScript") && Intrinsics.areEqual(inst.get("Disabled"), (Object) false) && (play = LuaRuntime.this.getPlay()) != null) {
                    play.maybeRunScript(inst);
                }
                HashMap m = (HashMap) LuaRuntime.this.conns.get(inst);
                if (m == null) {
                    return;
                }
                String disp = PropNames.INSTANCE.display(name);
                if (m.containsKey("Changed")) {
                    if (inst.isA("ValueBase") && Intrinsics.areEqual(disp, "Value")) {
                        LuaRuntime.this.fire(inst, "Changed", LuaRuntime.this.toLua(inst.get(name), inst, name));
                    } else {
                        LuaRuntime luaRuntime = LuaRuntime.this;
                        LuaString luaStringValueOf = LuaValue.valueOf(disp);
                        Intrinsics.checkNotNullExpressionValue(luaStringValueOf, "valueOf(...)");
                        luaRuntime.fire(inst, "Changed", luaStringValueOf);
                    }
                }
                if (m.containsKey("Changed:" + disp)) {
                    LuaRuntime.this.fire(inst, "Changed:" + disp, new LuaValue[0]);
                }
                if (Intrinsics.areEqual(disp, "CFrame")) {
                    if (m.containsKey("Changed:Position")) {
                        LuaRuntime.this.fire(inst, "Changed:Position", new LuaValue[0]);
                    }
                    if (m.containsKey("Changed:Orientation")) {
                        LuaRuntime.this.fire(inst, "Changed:Orientation", new LuaValue[0]);
                    }
                }
                if ((Intrinsics.areEqual(disp, "Health_XML") || Intrinsics.areEqual(disp, "Health")) && m.containsKey("HealthChanged")) {
                    LuaRuntime luaRuntime2 = LuaRuntime.this;
                    LuaValue[] luaValueArr = new LuaValue[1];
                    LuaRuntime luaRuntime3 = LuaRuntime.this;
                    Object obj = inst.get("Health_XML");
                    if (obj == null) {
                        obj = inst.get("Health");
                    }
                    luaValueArr[0] = LuaRuntime.toLua$default(luaRuntime3, obj, null, null, 6, null);
                    luaRuntime2.fire(inst, "HealthChanged", luaValueArr);
                }
            }
        };
        this.globals.load(new DebugLib());
        LuaValue luaValue = this.globals.get("debug").get("sethook");
        Intrinsics.checkNotNullExpressionValue(luaValue, "get(...)");
        this.sethook = luaValue;
        LuaValue luaValue2 = this.globals.get("debug").get("getinfo");
        Intrinsics.checkNotNullExpressionValue(luaValue2, "get(...)");
        this.debugGetinfo = luaValue2;
        this.hookFn = LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.LuaRuntime$$ExternalSyntheticLambda2
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaRuntime._init_$lambda$0(LuaRuntime.this, (Varargs) obj);
            }
        });
        LuaTable dbg = new LuaTable();
        LuaValue realDebug = this.globals.get("debug");
        dbg.set("traceback", realDebug.get("traceback"));
        dbg.set("profilebegin", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.LuaRuntime$$ExternalSyntheticLambda3
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaRuntime._init_$lambda$1((Varargs) obj);
            }
        }));
        dbg.set("profileend", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.LuaRuntime$$ExternalSyntheticLambda4
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaRuntime._init_$lambda$2((Varargs) obj);
            }
        }));
        dbg.set("info", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.LuaRuntime$$ExternalSyntheticLambda5
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaRuntime._init_$lambda$3((Varargs) obj);
            }
        }));
        this.globals.set("debug", dbg);
        LuaSandbox.INSTANCE.lockDown(this.globals);
        LuaThread running = this.globals.running;
        Intrinsics.checkNotNullExpressionValue(running, "running");
        setHook(running);
        LuaTypes.INSTANCE.install(this.globals);
        RobloxGlobals.INSTANCE.install(this);
        this.dm.getListeners().add(this.treeListener);
        this.timers = new ArrayList<>();
        this.commandEnv = LazyKt.lazy(new Function0() { // from class: com.goydevv.studrblx.lua.LuaRuntime$$ExternalSyntheticLambda6
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return LuaRuntime.envFor$default(LuaRuntime.this, null, null, 2, null);
            }
        });
        this.extraEnvForModules = MapsKt.emptyMap();
        this.moduleEnvs = new IdentityHashMap<>();
    }

    public final DataModel getDm() {
        return this.dm;
    }

    public final Studio getStudio() {
        return this.studio;
    }

    /* JADX INFO: renamed from: isPlay, reason: from getter */
    public final boolean getIsPlay() {
        return this.isPlay;
    }

    public final Globals getGlobals() {
        return this.globals;
    }

    public final LuaTable getInstanceMeta() {
        return this.instanceMeta;
    }

    public final InstanceApi getApi() {
        return this.api;
    }

    public final PlaySession getPlay() {
        return this.play;
    }

    public final void setPlay(PlaySession playSession) {
        this.play = playSession;
    }

    public final double getTime() {
        return this.time;
    }

    public final void setTime(double d) {
        this.time = d;
    }

    public final boolean getStopped() {
        return this.stopped;
    }

    public final void setStopped(boolean z) {
        this.stopped = z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: compiled from: LuaRuntime.kt */
    static final class Waiting {
        private final double at;
        private final LuaThread co;
        private final double start;

        public Waiting(LuaThread co, double at, double start) {
            Intrinsics.checkNotNullParameter(co, "co");
            this.co = co;
            this.at = at;
            this.start = start;
        }

        public final double getAt() {
            return this.at;
        }

        public final LuaThread getCo() {
            return this.co;
        }

        public final double getStart() {
            return this.start;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: compiled from: LuaRuntime.kt */
    static final class ChildWait {
        private final LuaThread co;
        private final double deadline;
        private final String name;
        private final Instance parent;
        private final double start;
        private boolean warned;

        public ChildWait(Instance parent, String name, LuaThread co, double deadline, double start) {
            Intrinsics.checkNotNullParameter(parent, "parent");
            Intrinsics.checkNotNullParameter(name, "name");
            Intrinsics.checkNotNullParameter(co, "co");
            this.parent = parent;
            this.name = name;
            this.co = co;
            this.deadline = deadline;
            this.start = start;
        }

        public final LuaThread getCo() {
            return this.co;
        }

        public final double getDeadline() {
            return this.deadline;
        }

        public final String getName() {
            return this.name;
        }

        public final Instance getParent() {
            return this.parent;
        }

        public final double getStart() {
            return this.start;
        }

        public final boolean getWarned() {
            return this.warned;
        }

        public final void setWarned(boolean z) {
            this.warned = z;
        }
    }

    public final ArrayList<Tween> getTweens() {
        return this.tweens;
    }

    public final IdentityHashMap<Instance, LuaValue> getModuleCache() {
        return this.moduleCache;
    }

    public final IdentityHashMap<Instance, HashMap<String, LuaValue>> getCallbacks() {
        return this.callbacks;
    }

    public final ConcurrentLinkedQueue<Function0<Unit>> getMainQueue() {
        return this.mainQueue;
    }

    public final LuaTable getSharedTable() {
        return this.sharedTable;
    }

    public final LuaValue getDebugGetinfo() {
        return this.debugGetinfo;
    }

    public final TreeListener getTreeListener() {
        return this.treeListener;
    }

    static final Varargs _init_$lambda$0(LuaRuntime this$0, Varargs varargs) {
        Intrinsics.checkNotNullParameter(varargs, "<unused var>");
        if (this$0.stopped) {
            throw new LuaError("Script stopped");
        }
        if (this$0.resumeStart != 0 && System.currentTimeMillis() - this$0.resumeStart > 10000) {
            throw new LuaError("Script timeout: exhausted allowed execution time");
        }
        LuaValue NONE = LuaValue.NONE;
        Intrinsics.checkNotNullExpressionValue(NONE, "NONE");
        return NONE;
    }

    static final Varargs _init_$lambda$1(Varargs it) {
        Intrinsics.checkNotNullParameter(it, "it");
        LuaValue NONE = LuaValue.NONE;
        Intrinsics.checkNotNullExpressionValue(NONE, "NONE");
        return NONE;
    }

    static final Varargs _init_$lambda$2(Varargs it) {
        Intrinsics.checkNotNullParameter(it, "it");
        LuaValue NONE = LuaValue.NONE;
        Intrinsics.checkNotNullExpressionValue(NONE, "NONE");
        return NONE;
    }

    static final Varargs _init_$lambda$3(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaString luaStringValueOf = LuaValue.valueOf(a.arg(1).tojstring());
        Intrinsics.checkNotNullExpressionValue(luaStringValueOf, "valueOf(...)");
        return luaStringValueOf;
    }

    public final void setHook(LuaThread co) {
        Intrinsics.checkNotNullParameter(co, "co");
        try {
            this.sethook.invoke(LuaValue.varargsOf(new LuaValue[]{co, this.hookFn, LuaValue.valueOf(""), LuaValue.valueOf(5000)}));
        } catch (Exception e) {
        }
    }

    public final void shutdown() {
        this.stopped = true;
        this.dm.getListeners().remove(this.treeListener);
        this.waiting.clear();
        this.deferred.clear();
        this.childWaits.clear();
        this.conns.clear();
        this.signalWaiters.clear();
        this.tweens.clear();
    }

    public final LuaValue wrap(Instance inst) {
        LuaUserdata luaUserdata;
        if (inst == null) {
            LuaValue NIL = LuaValue.NIL;
            Intrinsics.checkNotNullExpressionValue(NIL, "NIL");
            return NIL;
        }
        Map map = this.wrappers;
        LuaValue luaValue = (LuaValue) map.get(inst);
        if (luaValue == null) {
            luaUserdata = new LuaUserdata(inst, this.instanceMeta);
            map.put(inst, luaUserdata);
        } else {
            luaUserdata = (LuaUserdata) luaValue;
        }
        return luaUserdata;
    }

    public static /* synthetic */ LuaValue toLua$default(LuaRuntime luaRuntime, Object obj, Instance instance, String str, int i, Object obj2) {
        if ((i & 2) != 0) {
            instance = null;
        }
        if ((i & 4) != 0) {
            str = null;
        }
        return luaRuntime.toLua(obj, instance, str);
    }

    public final LuaValue toLua(Object v, Instance owner, String prop) {
        LuaValue luaValueWrap;
        LuaRuntime luaRuntime = this;
        if (v == null) {
            LuaValue NIL = LuaValue.NIL;
            Intrinsics.checkNotNullExpressionValue(NIL, "NIL");
            return NIL;
        }
        if (v instanceof LuaValue) {
            return (LuaValue) v;
        }
        if (v instanceof String) {
            LuaString luaStringValueOf = LuaValue.valueOf((String) v);
            Intrinsics.checkNotNullExpressionValue(luaStringValueOf, "valueOf(...)");
            return luaStringValueOf;
        }
        if (v instanceof Boolean) {
            LuaBoolean luaBooleanValueOf = LuaValue.valueOf(((Boolean) v).booleanValue());
            Intrinsics.checkNotNullExpressionValue(luaBooleanValueOf, "valueOf(...)");
            return luaBooleanValueOf;
        }
        if (v instanceof Integer) {
            LuaInteger luaIntegerValueOf = LuaValue.valueOf(((Number) v).intValue());
            Intrinsics.checkNotNullExpressionValue(luaIntegerValueOf, "valueOf(...)");
            return luaIntegerValueOf;
        }
        if (v instanceof Long) {
            LuaNumber luaNumberValueOf = LuaValue.valueOf(((Number) v).longValue());
            Intrinsics.checkNotNullExpressionValue(luaNumberValueOf, "valueOf(...)");
            return luaNumberValueOf;
        }
        if (v instanceof Float) {
            LuaNumber luaNumberValueOf2 = LuaValue.valueOf(((Number) v).floatValue());
            Intrinsics.checkNotNullExpressionValue(luaNumberValueOf2, "valueOf(...)");
            return luaNumberValueOf2;
        }
        if (v instanceof Double) {
            LuaNumber luaNumberValueOf3 = LuaValue.valueOf(((Number) v).doubleValue());
            Intrinsics.checkNotNullExpressionValue(luaNumberValueOf3, "valueOf(...)");
            return luaNumberValueOf3;
        }
        if (v instanceof Instance) {
            return luaRuntime.wrap((Instance) v);
        }
        if (v instanceof EnumValue) {
            String type = (owner == null || prop == null) ? null : Enums.INSTANCE.typeForProp(owner.getClassName(), prop);
            String name = Enums.INSTANCE.itemName(type, ((EnumValue) v).getValue());
            if (type != null && name != null) {
                return LuaTypes.INSTANCE.wrap(new EnumItemV(type, name, ((EnumValue) v).getValue()));
            }
            LuaInteger luaIntegerValueOf2 = LuaValue.valueOf(((EnumValue) v).getValue());
            Intrinsics.checkNotNullExpressionValue(luaIntegerValueOf2, "valueOf(...)");
            return luaIntegerValueOf2;
        }
        if (v instanceof BinaryString) {
            LuaString luaStringValueOf2 = LuaValue.valueOf(((BinaryString) v).getBytes());
            Intrinsics.checkNotNullExpressionValue(luaStringValueOf2, "valueOf(...)");
            return luaStringValueOf2;
        }
        if (v instanceof SharedStr) {
            LuaString luaStringValueOf3 = LuaValue.valueOf(((SharedStr) v).getBytes());
            Intrinsics.checkNotNullExpressionValue(luaStringValueOf3, "valueOf(...)");
            return luaStringValueOf3;
        }
        if (v instanceof OptionalCFrame) {
            CFrame cframe = ((OptionalCFrame) v).getCframe();
            if (cframe == null || (luaValueWrap = LuaTypes.INSTANCE.wrap(cframe)) == null) {
                luaValueWrap = LuaValue.NIL;
            }
            Intrinsics.checkNotNull(luaValueWrap);
            return luaValueWrap;
        }
        if (v instanceof ContentValue) {
            if (((ContentValue) v).getSourceType() == 2) {
                return luaRuntime.wrap(((ContentValue) v).getObj());
            }
            LuaString luaStringValueOf4 = LuaValue.valueOf(((ContentValue) v).getUri());
            Intrinsics.checkNotNullExpressionValue(luaStringValueOf4, "valueOf(...)");
            return luaStringValueOf4;
        }
        if (v instanceof RaycastResultV) {
            return LuaTypes.INSTANCE.wrap(v);
        }
        if (!(v instanceof List)) {
            return LuaTypes.INSTANCE.wrap(v);
        }
        LuaTable luaTable = new LuaTable();
        Iterator it = ((Iterable) v).iterator();
        while (it.hasNext()) {
            luaTable.insert(0, toLua$default(luaRuntime, it.next(), null, null, 6, null));
            luaRuntime = this;
        }
        return luaTable;
    }

    public static /* synthetic */ Object fromLua$default(LuaRuntime luaRuntime, LuaValue luaValue, Integer num, Instance instance, String str, int i, Object obj) {
        if ((i & 4) != 0) {
            instance = null;
        }
        if ((i & 8) != 0) {
            str = null;
        }
        return luaRuntime.fromLua(luaValue, num, instance, str);
    }

    public final Object fromLua(LuaValue v, Integer type, Instance inst, String prop) {
        Integer num;
        Intrinsics.checkNotNullParameter(v, "v");
        String strTypeForProp = null;
        if (v.isnil()) {
            if (type == null || type.intValue() != 30) {
                return null;
            }
            return new OptionalCFrame(null);
        }
        Object obj = v.isuserdata() ? v.touserdata() : null;
        if (type != null && type.intValue() == 2) {
            return Boolean.valueOf(v.toboolean());
        }
        if (type != null && type.intValue() == 3) {
            return Integer.valueOf(obj instanceof EnumItemV ? ((EnumItemV) obj).getValue() : (int) v.checkdouble());
        }
        if (type != null && type.intValue() == 4) {
            return Float.valueOf((float) v.checkdouble());
        }
        if (type != null && type.intValue() == 5) {
            return Double.valueOf(v.checkdouble());
        }
        if (type != null && type.intValue() == 27) {
            return Long.valueOf((long) v.checkdouble());
        }
        if (type != null && type.intValue() == 1) {
            if (v.isstring() || v.isnumber()) {
                return v.tojstring();
            }
            throw new LuaError("string expected, got " + LuaTypes.INSTANCE.typeOf(v));
        }
        if (type != null && type.intValue() == 18) {
            if (obj instanceof EnumItemV) {
                return new EnumValue(((EnumItemV) obj).getValue());
            }
            if (v.isnumber()) {
                return new EnumValue(v.toint());
            }
            if (v.isstring()) {
                if (inst != null && prop != null) {
                    strTypeForProp = Enums.INSTANCE.typeForProp(inst.getClassName(), prop);
                }
                LinkedHashMap<String, Integer> linkedHashMap = Enums.INSTANCE.getTypes().get(strTypeForProp);
                if (linkedHashMap == null || (num = linkedHashMap.get(v.tojstring())) == null) {
                    throw new LuaError("Invalid value \"" + v.tojstring() + "\" for enum " + (strTypeForProp == null ? "?" : strTypeForProp));
                }
                return new EnumValue(num.intValue());
            }
            throw new LuaError("EnumItem expected");
        }
        if (type != null && type.intValue() == 11) {
            if (obj instanceof BrickColorV) {
                return (BrickColorV) obj;
            }
            return obj instanceof Color3 ? new BrickColorV(BrickColors.INSTANCE.closest((Color3) obj)) : new BrickColorV(v.checkint());
        }
        if ((type != null && type.intValue() == 12) || (type != null && type.intValue() == 26)) {
            if (obj instanceof Color3) {
                return (Color3) obj;
            }
            if (obj instanceof BrickColorV) {
                return ((BrickColorV) obj).getColor();
            }
            throw new LuaError("Color3 expected, got " + LuaTypes.INSTANCE.typeOf(v));
        }
        if (type != null && type.intValue() == 19) {
            Instance instance = obj instanceof Instance ? (Instance) obj : null;
            if (instance != null) {
                return instance;
            }
            throw new LuaError("Instance expected, got " + LuaTypes.INSTANCE.typeOf(v));
        }
        if (type != null && type.intValue() == 30) {
            return new OptionalCFrame(obj instanceof CFrame ? (CFrame) obj : null);
        }
        if (type != null && type.intValue() == 34) {
            if (obj instanceof Instance) {
                return new ContentValue(2, "", (Instance) obj);
            }
            String str = v.tojstring();
            Intrinsics.checkNotNullExpressionValue(str, "tojstring(...)");
            return new ContentValue(1, str, null, 4, null);
        }
        if (type != null && type.intValue() == 14) {
            Vector3 vector3 = obj instanceof Vector3 ? (Vector3) obj : null;
            if (vector3 != null) {
                return vector3;
            }
            throw new LuaError("Vector3 expected, got " + LuaTypes.INSTANCE.typeOf(v));
        }
        if (type != null && type.intValue() == 16) {
            CFrame cFrame = obj instanceof CFrame ? (CFrame) obj : null;
            if (cFrame != null) {
                return cFrame;
            }
            throw new LuaError("CFrame expected, got " + LuaTypes.INSTANCE.typeOf(v));
        }
        if (type != null && type.intValue() == 7) {
            UDim2 uDim2 = obj instanceof UDim2 ? (UDim2) obj : null;
            if (uDim2 != null) {
                return uDim2;
            }
            throw new LuaError("UDim2 expected, got " + LuaTypes.INSTANCE.typeOf(v));
        }
        if (obj != null) {
            return obj instanceof EnumItemV ? new EnumValue(((EnumItemV) obj).getValue()) : obj;
        }
        if (v.isboolean()) {
            return Boolean.valueOf(v.toboolean());
        }
        if (v.isnumber()) {
            return Double.valueOf(v.todouble());
        }
        if (v.isstring()) {
            return v.tojstring();
        }
        throw new LuaError("Unable to assign value of type " + LuaTypes.INSTANCE.typeOf(v));
    }

    public final boolean hasConn(Object owner, String name) {
        ArrayList<LuaThread> arrayList;
        ArrayList<Connection> arrayList2;
        Intrinsics.checkNotNullParameter(owner, "owner");
        Intrinsics.checkNotNullParameter(name, "name");
        HashMap<String, ArrayList<Connection>> map = this.conns.get(owner);
        if (!((map == null || (arrayList2 = map.get(name)) == null || !(arrayList2.isEmpty() ^ true)) ? false : true)) {
            HashMap<String, ArrayList<LuaThread>> map2 = this.signalWaiters.get(owner);
            if (!((map2 == null || (arrayList = map2.get(name)) == null || !(arrayList.isEmpty() ^ true)) ? false : true)) {
                return false;
            }
        }
        return true;
    }

    public final Connection connect(Object owner, String name, LuaValue fn, boolean once) {
        HashMap<String, ArrayList<Connection>> map;
        ArrayList<Connection> arrayList;
        Intrinsics.checkNotNullParameter(owner, "owner");
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(fn, "fn");
        Connection c = new Connection(owner, name, fn, once);
        Map map2 = this.conns;
        HashMap<String, ArrayList<Connection>> map3 = (HashMap<String, ArrayList<Connection>>) map2.get(owner);
        if (map3 == null) {
            map = new HashMap<>();
            map2.put(owner, map);
        } else {
            map = map3;
        }
        Map map4 = map;
        ArrayList<Connection> arrayList2 = (ArrayList<Connection>) map4.get(name);
        if (arrayList2 == null) {
            arrayList = new ArrayList<>();
            map4.put(name, arrayList);
        } else {
            arrayList = arrayList2;
        }
        arrayList.add(c);
        return c;
    }

    public final void disconnect(Connection c) {
        ArrayList<Connection> arrayList;
        Intrinsics.checkNotNullParameter(c, "c");
        c.setConnected(false);
        HashMap<String, ArrayList<Connection>> map = this.conns.get(c.getOwner());
        if (map == null || (arrayList = map.get(c.getName())) == null) {
            return;
        }
        arrayList.remove(c);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void disconnectTree(Instance i) {
        this.conns.remove(i);
        this.signalWaiters.remove(i);
        this.callbacks.remove(i);
        Iterator<Instance> it = i.getChildren().iterator();
        Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
        while (it.hasNext()) {
            Instance c = it.next();
            disconnectTree(c);
        }
    }

    public final Varargs waitSignal(Object owner, String name) {
        HashMap<String, ArrayList<LuaThread>> map;
        ArrayList<LuaThread> arrayList;
        Intrinsics.checkNotNullParameter(owner, "owner");
        Intrinsics.checkNotNullParameter(name, "name");
        LuaThread co = currentCoroutine();
        if (co == null) {
            throw new LuaError("Wait can only be called from a yieldable thread");
        }
        Map map2 = this.signalWaiters;
        HashMap<String, ArrayList<LuaThread>> map3 = (HashMap<String, ArrayList<LuaThread>>) map2.get(owner);
        if (map3 == null) {
            map = new HashMap<>();
            map2.put(owner, map);
        } else {
            map = map3;
        }
        Map map4 = map;
        ArrayList<LuaThread> arrayList2 = (ArrayList<LuaThread>) map4.get(name);
        if (arrayList2 == null) {
            arrayList = new ArrayList<>();
            map4.put(name, arrayList);
        } else {
            arrayList = arrayList2;
        }
        arrayList.add(co);
        Varargs varargsYield = this.globals.yield(LuaValue.NONE);
        Intrinsics.checkNotNullExpressionValue(varargsYield, "yield(...)");
        return varargsYield;
    }

    public final void fire(Object owner, String name, LuaValue... args) {
        Intrinsics.checkNotNullParameter(owner, "owner");
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(args, "args");
        Varargs varargsVarargsOf = LuaValue.varargsOf(args);
        Intrinsics.checkNotNullExpressionValue(varargsVarargsOf, "varargsOf(...)");
        fireArgs(owner, name, varargsVarargsOf);
    }

    /* JADX WARN: Code duplicated, block: B:19:0x004d  */
    public final void fireArgs(Object owner, String name, Varargs args) {
        Intrinsics.checkNotNullParameter(owner, "owner");
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(args, "args");
        if (this.stopped) {
            return;
        }
        HashMap<String, ArrayList<Connection>> map = this.conns.get(owner);
        ArrayList<Connection> arrayList = map != null ? map.get(name) : null;
        if (arrayList != null && !arrayList.isEmpty()) {
            boolean direct = this.directEvents.contains(name) || StringsKt.startsWith(name, "Changed:", false) || StringsKt.startsWith(name, "Attr:", false);
            for (Connection c : CollectionsKt.toList(arrayList)) {
                if (c.getConnected()) {
                    if (c.getOnce()) {
                        disconnect(c);
                    }
                    LuaValue fn = c.getFn();
                    if (direct) {
                        callDirect(fn, args);
                    } else {
                        spawn(fn, args);
                    }
                }
            }
        }
        HashMap<String, ArrayList<LuaThread>> map2 = this.signalWaiters.get(owner);
        ArrayList ws = map2 != null ? map2.remove(name) : null;
        if (ws == null) {
            return;
        }
        Iterator it = ws.iterator();
        while (it.hasNext()) {
            resume((LuaThread) it.next(), args);
        }
    }

    public final LuaThread currentCoroutine() {
        LuaThread r = this.globals.running;
        if (r == null || r.isMainThread()) {
            return null;
        }
        return r;
    }

    public final Varargs callDirect(LuaValue fn, Varargs args) {
        Intrinsics.checkNotNullParameter(fn, "fn");
        Intrinsics.checkNotNullParameter(args, "args");
        long outer = this.resumeStart;
        if (outer == 0L) {
            this.resumeStart = System.currentTimeMillis();
        }
        Varargs varargsInvoke = null;
        try {
            varargsInvoke = fn.invoke(args);
        } catch (StackOverflowError e) {
            reportError("stack overflow");
        } catch (LuaError e2) {
            String message = e2.getMessage();
            if (message == null) {
                message = e2.toString();
            }
            reportError(message);
        } catch (RuntimeException e3) {
            reportError(e3.toString());
        } finally {
            this.resumeStart = outer;
        }
        return varargsInvoke;
    }

    public final LuaThread newThread(LuaValue fn) {
        Intrinsics.checkNotNullParameter(fn, "fn");
        LuaThread co = new LuaThread(this.globals, fn);
        setHook(co);
        return co;
    }

    public final LuaThread spawn(LuaValue fn, Varargs args) {
        Intrinsics.checkNotNullParameter(fn, "fn");
        Intrinsics.checkNotNullParameter(args, "args");
        LuaThread co = newThread(fn);
        resume(co, args);
        return co;
    }

    public final void resume(LuaThread co, Varargs args) {
        Intrinsics.checkNotNullParameter(co, "co");
        Intrinsics.checkNotNullParameter(args, "args");
        if (this.stopped || !Intrinsics.areEqual(co.getStatus(), "suspended")) {
            return;
        }
        long outer = this.resumeStart;
        this.resumeStart = System.currentTimeMillis();
        try {
            Varargs r = co.resume(args);
            if (!r.arg1().toboolean()) {
                LuaValue msg = r.arg(2);
                String str = msg.isnil() ? "error" : msg.tojstring();
                Intrinsics.checkNotNull(str);
                reportError(str);
            }
        } catch (Throwable e) {
            reportError(e.toString());
        } finally {
            this.resumeStart = outer;
        }
    }

    public final void reportError(String msg) {
        Intrinsics.checkNotNullParameter(msg, "msg");
        if (this.stopped) {
            return;
        }
        OutputLog.INSTANCE.error(cleanError(msg));
    }

    public final String cleanError(String m) {
        Intrinsics.checkNotNullParameter(m, "m");
        return new Regex("(^|\\s)=(?=[^=:\\n]+:\\d+)").replace(m, "$1");
    }

    public final Varargs yieldFor(double seconds) {
        LuaThread co = currentCoroutine();
        if (co == null) {
            throw new LuaError("attempt to yield across a non-yieldable boundary (use task.spawn)");
        }
        double start = this.time;
        this.waiting.add(new Waiting(co, this.time + RangesKt.coerceAtLeast(seconds, 0.0d), start));
        Varargs varargsYield = this.globals.yield(LuaValue.NONE);
        Intrinsics.checkNotNullExpressionValue(varargsYield, "yield(...)");
        return varargsYield;
    }

    public final void delay(double seconds, LuaThread co) {
        Intrinsics.checkNotNullParameter(co, "co");
        this.waiting.add(new Waiting(co, this.time + RangesKt.coerceAtLeast(seconds, 0.0d), this.time));
    }

    public final void defer(LuaThread co, Varargs args) {
        Intrinsics.checkNotNullParameter(co, "co");
        Intrinsics.checkNotNullParameter(args, "args");
        this.deferred.add(TuplesKt.to(co, args));
    }

    public final void after(double seconds, Function0<Unit> fn) {
        Intrinsics.checkNotNullParameter(fn, "fn");
        this.timers.add(TuplesKt.to(Double.valueOf(this.time + RangesKt.coerceAtLeast(seconds, 0.0d)), fn));
    }

    static final boolean cancel$lambda$13(LuaThread $co, Waiting it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return it.getCo() == $co;
    }

    public final void cancel(final LuaThread co) {
        Intrinsics.checkNotNullParameter(co, "co");
        CollectionsKt.removeAll((List) this.waiting, new Function1() { // from class: com.goydevv.studrblx.lua.LuaRuntime$$ExternalSyntheticLambda7
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return Boolean.valueOf(LuaRuntime.cancel$lambda$13(co, (LuaRuntime.Waiting) obj));
            }
        });
        CollectionsKt.removeAll((List) this.deferred, new Function1() { // from class: com.goydevv.studrblx.lua.LuaRuntime$$ExternalSyntheticLambda8
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return Boolean.valueOf(LuaRuntime.cancel$lambda$14(co, (Pair) obj));
            }
        });
    }

    static final boolean cancel$lambda$14(LuaThread $co, Pair it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return it.getFirst() == $co;
    }

    public final Varargs waitForChild(Instance parent, String name, Double timeout) {
        Intrinsics.checkNotNullParameter(parent, "parent");
        Intrinsics.checkNotNullParameter(name, "name");
        Instance instanceFindFirstChild$default = Instance.findFirstChild$default(parent, name, false, 2, null);
        if (instanceFindFirstChild$default != null) {
            return wrap(instanceFindFirstChild$default);
        }
        LuaThread co = currentCoroutine();
        if (co == null) {
            throw new LuaError("WaitForChild must be called from a yieldable thread");
        }
        this.childWaits.add(new ChildWait(parent, name, co, timeout == null ? Double.MAX_VALUE : this.time + timeout.doubleValue(), this.time));
        Varargs varargsYield = this.globals.yield(LuaValue.NONE);
        Intrinsics.checkNotNullExpressionValue(varargsYield, "yield(...)");
        return varargsYield;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void checkChildWaits() {
        Iterable iterable = this.childWaits;
        Collection arrayList = new ArrayList();
        Iterator it = iterable.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Object next = it.next();
            ChildWait childWait = (ChildWait) next;
            if (Instance.findFirstChild$default(childWait.getParent(), childWait.getName(), false, 2, null) != null) {
                arrayList.add(next);
            }
        }
        List<ChildWait> done = (List) arrayList;
        if (done.isEmpty()) {
            return;
        }
        this.childWaits.removeAll(done);
        for (ChildWait w : done) {
            this.deferred.add(TuplesKt.to(w.getCo(), wrap(Instance.findFirstChild$default(w.getParent(), w.getName(), false, 2, null))));
        }
    }

    /* JADX WARN: Code duplicated, block: B:80:0x0230  */
    public final void step(double dt) {
        List<Waiting> sorted;
        if (this.stopped) {
            return;
        }
        this.time += dt;
        while (true) {
            Function0<Unit> function0Poll = this.mainQueue.poll();
            if (function0Poll == null) {
                break;
            } else {
                function0Poll.invoke();
            }
        }
        if (!this.waiting.isEmpty()) {
            Iterable iterable = this.waiting;
            Collection arrayList = new ArrayList();
            for (Object obj : iterable) {
                if (((Waiting) obj).getAt() <= this.time) {
                    arrayList.add(obj);
                }
            }
            List due = (List) arrayList;
            if (!due.isEmpty()) {
                if (due.size() <= 1) {
                    sorted = due;
                } else {
                    sorted = CollectionsKt.sortedWith(due, new Comparator() { // from class: com.goydevv.studrblx.lua.LuaRuntime$step$$inlined$sortedBy$1
                        /* JADX WARN: Multi-variable type inference failed */
                        @Override // java.util.Comparator
                        public final int compare(Object t, Object t2) {
                            return ComparisonsKt.compareValues(Double.valueOf(((LuaRuntime.Waiting) t).getAt()), Double.valueOf(((LuaRuntime.Waiting) t2).getAt()));
                        }
                    });
                }
                Set ran = Collections.newSetFromMap(new IdentityHashMap());
                long t0 = System.nanoTime();
                for (Waiting w : sorted) {
                    Intrinsics.checkNotNull(ran);
                    if (!ran.isEmpty() && System.nanoTime() - t0 > WAIT_BUDGET_NS) {
                        break;
                    }
                    ran.add(w);
                    LuaThread co = w.getCo();
                    Varargs varargsVarargsOf = LuaValue.varargsOf(LuaValue.valueOf(this.time - w.getStart()), LuaValue.valueOf(this.time));
                    Intrinsics.checkNotNullExpressionValue(varargsVarargsOf, "varargsOf(...)");
                    resume(co, varargsVarargsOf);
                }
                ArrayList<Waiting> arrayList2 = this.waiting;
                Intrinsics.checkNotNull(ran);
                arrayList2.removeAll(ran);
            }
        }
        List sorted2 = this.timers;
        if (!sorted2.isEmpty()) {
            Iterable iterable2 = this.timers;
            Collection arrayList3 = new ArrayList();
            for (Object obj2 : iterable2) {
                if (((Number) ((Pair) obj2).getFirst()).doubleValue() <= this.time) {
                    arrayList3.add(obj2);
                }
            }
            List due2 = (List) arrayList3;
            if (!due2.isEmpty()) {
                this.timers.removeAll(CollectionsKt.toSet(due2));
                Iterator it = due2.iterator();
                while (it.hasNext()) {
                    Function0 f = (Function0) ((Pair) it.next()).component2();
                    try {
                        f.invoke();
                    } catch (LuaError e) {
                        String message = e.getMessage();
                        if (message == null) {
                            message = "error";
                        }
                        reportError(message);
                    }
                }
            }
        }
        for (int pass = 0; pass < 2 && !this.deferred.isEmpty(); pass++) {
            List<? extends Pair<?, ?>> d = CollectionsKt.toList(this.deferred);
            this.deferred.clear();
            for (Pair<?, ?> pair : d) {
                LuaThread co2 = (LuaThread) pair.component1();
                Varargs a = (Varargs) pair.component2();
                resume(co2, a);
            }
        }
        List d2 = this.childWaits;
        if (!d2.isEmpty()) {
            Iterator<ChildWait> it2 = this.childWaits.iterator();
            Intrinsics.checkNotNullExpressionValue(it2, "iterator(...)");
            ArrayList timedOut = new ArrayList();
            while (it2.hasNext()) {
                ChildWait next = it2.next();
                Intrinsics.checkNotNullExpressionValue(next, "next(...)");
                ChildWait w2 = next;
                if (!w2.getWarned() && this.time - w2.getStart() > 5.0d) {
                    if (w2.getDeadline() == Double.MAX_VALUE) {
                        w2.setWarned(true);
                        OutputLog.INSTANCE.warn("Infinite yield possible on '" + w2.getParent().fullName() + ":WaitForChild(\"" + w2.getName() + "\")'");
                    }
                }
                if (this.time >= w2.getDeadline()) {
                    it2.remove();
                    timedOut.add(w2);
                }
            }
            Iterator it3 = timedOut.iterator();
            while (it3.hasNext()) {
                LuaThread co3 = ((ChildWait) it3.next()).getCo();
                LuaValue NIL = LuaValue.NIL;
                Intrinsics.checkNotNullExpressionValue(NIL, "NIL");
                resume(co3, NIL);
            }
        }
        stepTweens((float) dt);
    }

    private final void stepTweens(float dt) {
        if (this.tweens.isEmpty()) {
            return;
        }
        for (Tween tw : CollectionsKt.toList(this.tweens)) {
            if (tw.getState() == 2 || tw.getState() == 1) {
                tw.setElapsed(tw.getElapsed() + dt);
                if (tw.getState() == 1) {
                    float delay = tw.getInfo().getDelay();
                    if (tw.getElapsed() < delay) {
                        continue;
                    }
                    tw.setState(2);
                    tw.setElapsed(tw.getElapsed() - delay);
                    Iterable iterableKeySet = tw.getGoals().keySet();
                    LinkedHashMap linkedHashMap = new LinkedHashMap(RangesKt.coerceAtLeast(MapsKt.mapCapacity(CollectionsKt.collectionSizeOrDefault(iterableKeySet, 10)), 16));
                    for (Object obj : iterableKeySet) {
                        linkedHashMap.put(obj, this.api.getK(tw.getInst(), (String) obj));
                    }
                    tw.setStart(linkedHashMap);
                }
                float dur = RangesKt.coerceAtLeast(tw.getInfo().getTime(), 1.0E-4f);
                float a = RangesKt.coerceIn(tw.getElapsed() / dur, 0.0f, 1.0f);
                if (tw.getReversing()) {
                    a = 1.0f - a;
                }
                float e = LuaTypes.INSTANCE.ease(tw.getInfo().getStyle(), tw.getInfo().getDirection(), a);
                for (Map.Entry<String, Object> entry : tw.getGoals().entrySet()) {
                    String k = entry.getKey();
                    Object goal = entry.getValue();
                    try {
                        this.api.setK(tw.getInst(), k, LuaTypes.INSTANCE.lerpValue(tw.getStart().get(k), goal, e));
                    } catch (Exception e2) {
                    }
                }
                if (tw.getElapsed() >= dur) {
                    if (tw.getInfo().getReverses() && !tw.getReversing()) {
                        tw.setReversing(true);
                        tw.setElapsed(0.0f);
                    } else if (tw.getInfo().getRepeatCount() < 0 || tw.getRepeatsDone() < tw.getInfo().getRepeatCount()) {
                        tw.setRepeatsDone(tw.getRepeatsDone() + 1);
                        tw.setReversing(false);
                        tw.setElapsed(0.0f);
                    } else {
                        tw.setState(4);
                        this.tweens.remove(tw);
                        fire(tw, "Completed", LuaTypes.INSTANCE.wrap(new EnumItemV("PlaybackState", "Completed", 4)));
                        LuaValue cb = tw.getGuiCallback();
                        if (cb != null && cb.isfunction()) {
                            try {
                                cb.call(LuaTypes.INSTANCE.wrap(new EnumItemV("TweenStatus", "Completed", 1)));
                            } catch (Exception ignored) {
                            }
                        }
                    }
                }
            }
        }
    }

    static final boolean playTween$lambda$23(Tween $tw, Tween it) {
        Intrinsics.checkNotNullParameter(it, "it");
        if (it == $tw || it.getInst() != $tw.getInst()) {
            return false;
        }
        for (String k : it.getGoals().keySet()) {
            if ($tw.getGoals().containsKey(k)) {
                return true;
            }
        }
        return false;
    }

    public final void playTween(final Tween tw) {
        Intrinsics.checkNotNullParameter(tw, "tw");
        ArrayList<Tween> cancelled = new ArrayList<>();
        for (Tween existing : this.tweens) {
            if (playTween$lambda$23(tw, existing)) {
                cancelled.add(existing);
            }
        }
        if (!cancelled.isEmpty()) {
            this.tweens.removeAll(cancelled);
            for (Tween c : cancelled) {
                c.setState(5);
                fire(c, "Completed", LuaTypes.INSTANCE.wrap(new EnumItemV("PlaybackState", "Cancelled", 5)));
                LuaValue cb = c.getGuiCallback();
                if (cb != null && cb.isfunction()) {
                    try {
                        cb.call(LuaTypes.INSTANCE.wrap(new EnumItemV("TweenStatus", "Canceled", 0)));
                    } catch (Exception ignored) {
                    }
                }
            }
        }
        if (tw.getState() == 3) {
            tw.setState(2);
            if (this.tweens.contains(tw)) {
                return;
            }
            this.tweens.add(tw);
            return;
        }
        tw.setElapsed(0.0f);
        tw.setRepeatsDone(0);
        tw.setReversing(false);
        if (tw.getInfo().getDelay() > 0.0f) {
            tw.setState(1);
        } else {
            tw.setState(2);
            Iterable iterableKeySet = tw.getGoals().keySet();
            LinkedHashMap linkedHashMap = new LinkedHashMap(RangesKt.coerceAtLeast(MapsKt.mapCapacity(CollectionsKt.collectionSizeOrDefault(iterableKeySet, 10)), 16));
            for (Object obj : iterableKeySet) {
                linkedHashMap.put(obj, this.api.getK(tw.getInst(), (String) obj));
            }
            tw.setStart(linkedHashMap);
        }
        if (!this.tweens.contains(tw)) {
            this.tweens.add(tw);
        }
    }

    public final LuaValue compile(String source, String chunkName, LuaTable env) throws Exception {
        Pair pair;
        Prototype proto;
        Intrinsics.checkNotNullParameter(source, "source");
        Intrinsics.checkNotNullParameter(chunkName, "chunkName");
        Intrinsics.checkNotNullParameter(env, "env");
        String key = chunkName + "\u0000" + source.length() + ":" + source.hashCode();
        synchronized (protoCache) {
            pair = (Pair) protoCache.get((Object) key);
        }
        Prototype cached = null;
        if (pair != null) {
            if (!Intrinsics.areEqual(pair.getFirst(), source)) {
                pair = null;
            }
            if (pair != null) {
                cached = (Prototype) pair.getSecond();
            }
        }
        if (cached == null) {
            proto = fromDisk(chunkName, source);
            if (proto == null) {
                proto = compileLuau(source, chunkName);
                toDisk(chunkName, source, proto);
            }
            Prototype prototype = proto;
            synchronized (protoCache) {
                protoCache.put(key, TuplesKt.to(source, prototype));
                Unit unit = Unit.INSTANCE;
            }
        } else {
            proto = cached;
        }
        LuaFunction luaFunctionLoad = this.globals.loader.load(proto, "=" + chunkName, env);
        Intrinsics.checkNotNullExpressionValue(luaFunctionLoad, "load(...)");
        return luaFunctionLoad;
    }

    /* JADX WARN: Code duplicated, block: B:25:0x0078  */
    private final Prototype compileLuau(String source, String chunkName) throws Exception {
        boolean limit = false;
        try {
            Globals globals = this.globals;
            byte[] bytes = LuauTranspiler.transpile$default(LuauTranspiler.INSTANCE, source, false, 2, null).getBytes(Charsets.UTF_8);
            Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
            Prototype prototypeCompilePrototype = globals.compilePrototype(new ByteArrayInputStream(bytes), "=" + chunkName);
            Intrinsics.checkNotNull(prototypeCompilePrototype);
            return prototypeCompilePrototype;
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                limit = true;
            } else if (e instanceof LuaError) {
                String message = ((LuaError) e).getMessage();
                if (message != null && StringsKt.contains((CharSequence) message, (CharSequence) "upvalue", false)) {
                    limit = true;
                } else {
                    String message2 = ((LuaError) e).getMessage();
                    if (message2 != null && StringsKt.contains((CharSequence) message2, (CharSequence) "local variables", false)) {
                        limit = true;
                    }
                }
            }
            if (!limit) {
                try {
                    byte[] rawBytes = source.getBytes(Charsets.UTF_8);
                    Prototype rawProto = this.globals.compilePrototype(new ByteArrayInputStream(rawBytes), "=" + chunkName);
                    if (rawProto != null) {
                        return rawProto;
                    }
                } catch (Throwable ignored) {
                }
                throw e;
            }
            Globals globals2 = this.globals;
            byte[] bytes2 = LuauTranspiler.INSTANCE.transpile(source, true).getBytes(Charsets.UTF_8);
            Intrinsics.checkNotNullExpressionValue(bytes2, "getBytes(...)");
            Prototype prototypeCompilePrototype2 = globals2.compilePrototype(new ByteArrayInputStream(bytes2), "=" + chunkName);
            Intrinsics.checkNotNull(prototypeCompilePrototype2);
            return prototypeCompilePrototype2;
        }
    }

    /* JADX WARN: Code duplicated, block: B:24:0x007e A[Catch: all -> 0x00c5, TryCatch #1 {all -> 0x00c5, blocks: (B:16:0x0064, B:18:0x006a, B:24:0x007e, B:25:0x0081, B:26:0x008a, B:27:0x009a, B:29:0x00a0, B:32:0x00b5), top: B:63:0x0064 }] */
    /* JADX WARN: Code duplicated, block: B:76:0x0081 A[SYNTHETIC] */
    /* JADX WARN: Multi-variable type inference failed */
    private final File diskFile(String chunkName, String source) {
        try {
        int i;
        File[] fileArr;
        boolean z = false;
        LuaRuntime luaRuntime = this;
        try {
            File dir = luacDir;
            try {
                if (dir == null) {
                    try {
                        synchronized (protoCache) {
                            int i2 = 0;
                            try {
                                File cacheDir = luaRuntime.studio.getApp().getCacheDir();
                                String str = "luac-" + INSTANCE.buildStamp(luaRuntime.studio.getApp());
                                File[] fileArrListFiles = cacheDir.listFiles();
                                if (fileArrListFiles != null) {
                                    Collection arrayList = new ArrayList();
                                    int length = fileArrListFiles.length;
                                    int i3 = 0;
                                    while (i3 < length) {
                                        File file = fileArrListFiles[i3];
                                        if (file.isDirectory()) {
                                            String name = file.getName();
                                            Intrinsics.checkNotNullExpressionValue(name, "getName(...)");
                                            i = i2;
                                            fileArr = fileArrListFiles;
                                            try {
                                                if (StringsKt.startsWith(name, "luac", false) && !Intrinsics.areEqual(file.getName(), str)) {
                                                    z = true;
                                                }
                                                if (z) {
                                                    arrayList.add(file);
                                                }
                                                i3++;
                                                fileArrListFiles = fileArr;
                                                i2 = i;
                                            } catch (Throwable th) {
                                                throw th;
                                            }
                                        } else {
                                            i = i2;
                                            fileArr = fileArrListFiles;
                                        }
                                        z = false;
                                        if (z) {
                                            arrayList.add(file);
                                        }
                                        i3++;
                                        fileArrListFiles = fileArr;
                                        i2 = i;
                                    }
                                    luaRuntime = null;
                                    for (File file2 : (List<File>) arrayList) {
                                        Intrinsics.checkNotNull(file2);
                                        FilesKt.deleteRecursively(file2);
                                    }
                                } else {
                                    luaRuntime = null;
                                }
                                dir = new File(cacheDir, str);
                                dir.mkdirs();
                                Companion companion = INSTANCE;
                                luacDir = dir;
                            } catch (Throwable th2) {
                                Throwable th = th2;
                            }
                        }
                    } catch (Exception e) {
                        return null;
                    }
                } else {
                    luaRuntime = null;
                }
                MessageDigest md = MessageDigest.getInstance("SHA-1");
                try {
                    byte[] bytes = chunkName.getBytes(Charsets.UTF_8);
                    Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
                    md.update(bytes);
                    md.update((byte) 0);
                    try {
                        byte[] bytes2 = source.getBytes(Charsets.UTF_8);
                        Intrinsics.checkNotNullExpressionValue(bytes2, "getBytes(...)");
                        md.update(bytes2);
                        byte[] bArrDigest = md.digest();
                        Intrinsics.checkNotNullExpressionValue(bArrDigest, "digest(...)");
                        return new File(dir, ArraysKt.joinToString(bArrDigest, (CharSequence) "", "", "", -1, "...", new Function1() { // from class: com.goydevv.studrblx.lua.LuaRuntime$$ExternalSyntheticLambda0
                            @Override // kotlin.jvm.functions.Function1
                            public final Object invoke(Object obj) {
                                return LuaRuntime.diskFile$lambda$34(((Byte) obj).byteValue());
                            }
                        }) + ".luac");
                    } catch (Exception e2) {
                        return null;
                    }
                } catch (Exception e3) {
                    return null;
                }
            } catch (Exception e4) {
                return null;
            }
        } catch (Exception e5) {
            return null;
        }
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    static final CharSequence diskFile$lambda$34(byte it) {
        String str = String.format("%02x", Arrays.copyOf(new Object[]{Byte.valueOf(it)}, 1));
        Intrinsics.checkNotNullExpressionValue(str, "format(...)");
        return str;
    }

    private final Prototype fromDisk(String chunkName, String source) {
        File f;
        if (source.length() >= 2000 && (f = diskFile(chunkName, source)) != null) {
            if (!f.isFile()) {
                f = null;
            }
            if (f != null) {
                try {
                    InputStream fileInputStream = new FileInputStream(f);
                    BufferedInputStream bufferedInputStream = fileInputStream instanceof BufferedInputStream ? (BufferedInputStream) fileInputStream : new BufferedInputStream(fileInputStream, 8192);
                    try {
                        Prototype prototypeUndump = LoadState.undump(bufferedInputStream, "=" + chunkName);
                        CloseableKt.closeFinally(bufferedInputStream, null);
                        return prototypeUndump;
                    } catch (Throwable th) {
                        try {
                            throw th;
                        } catch (Throwable th2) {
                            CloseableKt.closeFinally(bufferedInputStream, th);
                            throw th2;
                        }
                    }
                } catch (Exception e) {
                    f.delete();
                    return null;
                }
            }
        }
        return null;
    }

    private final void toDisk(String chunkName, String source, Prototype p) {
        File f;
        if (source.length() >= 2000 && (f = diskFile(chunkName, source)) != null) {
            try {
                File tmp = new File(f.getPath() + ".tmp");
                OutputStream fileOutputStream = new FileOutputStream(tmp);
                BufferedOutputStream bufferedOutputStream = fileOutputStream instanceof BufferedOutputStream ? (BufferedOutputStream) fileOutputStream : new BufferedOutputStream(fileOutputStream, 8192);
                try {
                    DumpState.dump(p, bufferedOutputStream, false);
                    CloseableKt.closeFinally(bufferedOutputStream, null);
                    tmp.renameTo(f);
                } catch (Throwable th) {
                    try {
                        throw th;
                    } catch (Throwable th2) {
                        CloseableKt.closeFinally(bufferedOutputStream, th);
                        throw th2;
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    /* JADX INFO: compiled from: LuaRuntime.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final String buildStamp(Context ctx) {
            String strValueOf = LuaRuntime.stamp;
            if (strValueOf == null) {
                try {
                    strValueOf = String.valueOf(ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0).lastUpdateTime);
                } catch (Exception e) {
                    strValueOf = "0";
                }
                Companion companion = LuaRuntime.INSTANCE;
                LuaRuntime.stamp = strValueOf;
            }
            return strValueOf;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ LuaTable envFor$default(LuaRuntime luaRuntime, Instance instance, Map map, int i, Object obj) {
        if ((i & 2) != 0) {
            map = MapsKt.emptyMap();
        }
        return luaRuntime.envFor(instance, map);
    }

    public final LuaTable envFor(Instance script, Map<String, ? extends LuaValue> extra) {
        Intrinsics.checkNotNullParameter(extra, "extra");
        Globals env = new Globals();
        env.debuglib = this.globals.debuglib;
        LuaTable mt = new LuaTable();
        mt.set(LuaValue.INDEX, this.globals);
        env.setmetatable(mt);
        env.set("script", wrap(script));
        for (Map.Entry<String, ? extends LuaValue> entry : extra.entrySet()) {
            String k = entry.getKey();
            LuaValue v = entry.getValue();
            env.set(k, v);
        }
        return env;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ void runScript$default(LuaRuntime luaRuntime, Instance instance, Map map, int i, Object obj) throws Exception {
        if ((i & 2) != 0) {
            map = MapsKt.emptyMap();
        }
        try { luaRuntime.runScript(instance, map); } catch (Throwable _ktEx) { throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx); }
    }

    public final void runScript(Instance script, Map<String, ? extends LuaValue> extra) throws Exception {
        Intrinsics.checkNotNullParameter(script, "script");
        Intrinsics.checkNotNullParameter(extra, "extra");
        Object obj = script.get("Source");
        String src = obj instanceof String ? (String) obj : null;
        if (src == null) {
            return;
        }
        try {
            LuaValue fn = compile(src, script.fullName(), envFor(script, extra));
            LuaValue NONE = LuaValue.NONE;
            Intrinsics.checkNotNullExpressionValue(NONE, "NONE");
            spawn(fn, NONE);
        } catch (Throwable e) {
            OutputLog outputLog = OutputLog.INSTANCE;
            String message = e.getMessage();
            if (message == null || message.length() == 0) {
                message = e.getClass().getSimpleName() + " in " + script.fullName();
            }
            outputLog.error(cleanError(message));
        }
    }

    private final LuaTable getCommandEnv() {
        return (LuaTable) this.commandEnv.getValue();
    }

    public final void runCommand(String code) throws Exception {
        Intrinsics.checkNotNullParameter(code, "code");
        try {
            LuaValue fn = compile(code, "CommandBar", getCommandEnv());
            LuaValue NONE = LuaValue.NONE;
            Intrinsics.checkNotNullExpressionValue(NONE, "NONE");
            spawn(fn, NONE);
        } catch (LuaError e) {
            OutputLog outputLog = OutputLog.INSTANCE;
            String message = e.getMessage();
            if (message == null) {
                message = "Syntax error";
            }
            outputLog.error(cleanError(message));
        }
    }

    public final LuaValue require(Instance module) throws Exception {
        ArrayList<LuaTable> arrayList;
        Intrinsics.checkNotNullParameter(module, "module");
        LuaValue luaValue = this.moduleCache.get(module);
        if (luaValue != null) {
            return luaValue;
        }
        if (!module.isA("ModuleScript")) {
            throw new LuaError("Attempted to call require with invalid argument(s).");
        }
        Instance top = module;
        while (true) {
            if ((top != null ? top.getParent() : null) == null) {
                break;
            }
            top = top.getParent();
        }
        LuaTable env = envFor(module, Intrinsics.areEqual(top != null ? top.getClassName() : null, "Plugin") ? MapsKt.plus(this.extraEnvForModules, TuplesKt.to("plugin", wrap(top))) : this.extraEnvForModules);
        Object obj = module.get("Source");
        String str = obj instanceof String ? (String) obj : null;
        if (str == null) {
            str = "";
        }
        LuaValue fn = compile(str, module.fullName(), env);
        LuaThread co = this.globals.running;
        Map map = this.moduleEnvs;
        ArrayList<LuaTable> arrayList2 = (ArrayList<LuaTable>) map.get(co);
        if (arrayList2 == null) {
            arrayList = new ArrayList<>();
            map.put(co, arrayList);
        } else {
            arrayList = arrayList2;
        }
        ArrayList<LuaTable> arrayList3 = arrayList;
        arrayList3.add(env);
        try {
            LuaValue r = fn.call();
            arrayList3.remove(arrayList3.size() - 1);
            if (arrayList3.isEmpty()) {
                this.moduleEnvs.remove(co);
            }
            if (r.isnil()) {
                throw new LuaError("Module code did not return exactly one value");
            }
            this.moduleCache.put(module, r);
            Intrinsics.checkNotNull(r);
            return r;
        } catch (Throwable th) {
            arrayList3.remove(arrayList3.size() - 1);
            if (arrayList3.isEmpty()) {
                this.moduleEnvs.remove(co);
            }
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(th);
        }
    }

    public final Map<String, ? extends LuaValue> getExtraEnvForModules() {
        return this.extraEnvForModules;
    }

    public final void setExtraEnvForModules(Map<String, ? extends LuaValue> map) {
        Intrinsics.checkNotNullParameter(map, "<set-?>");
        this.extraEnvForModules = map;
    }

    public final LuaTable loadingModuleEnv() {
        ArrayList<LuaTable> arrayList = this.moduleEnvs.get(this.globals.running);
        if (arrayList != null) {
            return (LuaTable) CollectionsKt.lastOrNull((List) arrayList);
        }
        return null;
    }

    public final void onAncestryChanged(Instance i) {
        Intrinsics.checkNotNullParameter(i, "i");
        if (hasConn(i, "AncestryChanged")) {
            fire(i, "AncestryChanged", wrap(i), wrap(i.getParent()));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void notifyAncestry(Instance i) {
        if (this.conns.isEmpty()) {
            return;
        }
        onAncestryChanged(i);
        for (Instance d : Instance.descendants$default(i, null, 1, null)) {
            if (this.conns.containsKey(d)) {
                onAncestryChanged(d);
            }
        }
    }
}

package com.goydevv.studrblx.core;

import com.goydevv.studrblx.lua.LuaRuntime;
import com.goydevv.studrblx.model.Instance;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Studio.kt */
/* JADX INFO: loaded from: classes4.dex */
public final class PluginButton {
    private final Instance inst;
    private final String pluginName;
    private final LuaRuntime rt;

    public PluginButton(Instance inst, String pluginName, LuaRuntime rt) {
        Intrinsics.checkNotNullParameter(inst, "inst");
        Intrinsics.checkNotNullParameter(pluginName, "pluginName");
        Intrinsics.checkNotNullParameter(rt, "rt");
        this.inst = inst;
        this.pluginName = pluginName;
        this.rt = rt;
    }

    public final Instance getInst() {
        return this.inst;
    }

    public final String getPluginName() {
        return this.pluginName;
    }

    public final LuaRuntime getRt() {
        return this.rt;
    }
}

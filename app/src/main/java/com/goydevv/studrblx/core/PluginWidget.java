package com.goydevv.studrblx.core;

import com.goydevv.studrblx.lua.LuaRuntime;
import com.goydevv.studrblx.model.Instance;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Studio.kt */
/* JADX INFO: loaded from: classes4.dex */
public final class PluginWidget {
    private final Instance inst;
    private final LuaRuntime rt;

    public PluginWidget(Instance inst, LuaRuntime rt) {
        Intrinsics.checkNotNullParameter(inst, "inst");
        Intrinsics.checkNotNullParameter(rt, "rt");
        this.inst = inst;
        this.rt = rt;
    }

    public final Instance getInst() {
        return this.inst;
    }

    public final LuaRuntime getRt() {
        return this.rt;
    }
}

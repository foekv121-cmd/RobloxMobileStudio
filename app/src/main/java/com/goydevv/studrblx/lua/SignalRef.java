package com.goydevv.studrblx.lua;

import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: LuaRuntime.kt */
/* JADX INFO: loaded from: classes4.dex */
public final class SignalRef {
    private final String name;
    private final Object owner;

    public SignalRef(Object owner, String name) {
        Intrinsics.checkNotNullParameter(owner, "owner");
        Intrinsics.checkNotNullParameter(name, "name");
        this.owner = owner;
        this.name = name;
    }

    public final String getName() {
        return this.name;
    }

    public final Object getOwner() {
        return this.owner;
    }
}

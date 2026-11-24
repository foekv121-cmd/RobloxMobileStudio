package com.goydevv.studrblx.lua;

import kotlin.jvm.internal.Intrinsics;
import org.luaj.vm2.LuaValue;

/* JADX INFO: compiled from: LuaRuntime.kt */
/* JADX INFO: loaded from: classes4.dex */
public final class Connection {
    private boolean connected;
    private final LuaValue fn;
    private final String name;
    private final boolean once;
    private final Object owner;

    public Connection(Object owner, String name, LuaValue fn, boolean once) {
        Intrinsics.checkNotNullParameter(owner, "owner");
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(fn, "fn");
        this.owner = owner;
        this.name = name;
        this.fn = fn;
        this.once = once;
        this.connected = true;
    }

    public final boolean getConnected() {
        return this.connected;
    }

    public final LuaValue getFn() {
        return this.fn;
    }

    public final String getName() {
        return this.name;
    }

    public final boolean getOnce() {
        return this.once;
    }

    public final Object getOwner() {
        return this.owner;
    }

    public final void setConnected(boolean z) {
        this.connected = z;
    }
}

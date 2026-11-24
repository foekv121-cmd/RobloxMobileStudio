package com.goydevv.studrblx.lua;

import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: LuaTypes.kt */
/* JADX INFO: loaded from: classes4.dex */
public final class EnumTypeV {
    private final String name;

    public EnumTypeV(String name) {
        Intrinsics.checkNotNullParameter(name, "name");
        this.name = name;
    }

    public final String getName() {
        return this.name;
    }
}

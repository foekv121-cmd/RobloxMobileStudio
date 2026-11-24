package com.goydevv.studrblx.lua;

import com.goydevv.studrblx.model.Vector3int16;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: LuaTypes.kt */
/* JADX INFO: loaded from: classes4.dex */
public final class Region3int16V {
    private final Vector3int16 max;
    private final Vector3int16 min;

    public Region3int16V(Vector3int16 min, Vector3int16 max) {
        Intrinsics.checkNotNullParameter(min, "min");
        Intrinsics.checkNotNullParameter(max, "max");
        this.min = min;
        this.max = max;
    }

    public final Vector3int16 getMax() {
        return this.max;
    }

    public final Vector3int16 getMin() {
        return this.min;
    }
}

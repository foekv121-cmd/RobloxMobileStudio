package com.goydevv.studrblx.lua;

import com.goydevv.studrblx.model.CFrame;
import com.goydevv.studrblx.model.Vector3;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: LuaTypes.kt */
/* JADX INFO: loaded from: classes4.dex */
public final class Region3V {
    private final Vector3 max;
    private final Vector3 min;

    public Region3V(Vector3 min, Vector3 max) {
        Intrinsics.checkNotNullParameter(min, "min");
        Intrinsics.checkNotNullParameter(max, "max");
        this.min = min;
        this.max = max;
    }

    public final Vector3 getMax() {
        return this.max;
    }

    public final Vector3 getMin() {
        return this.min;
    }

    public final CFrame getCframe() {
        return CFrame.INSTANCE.at(this.min.plus(this.max).div(2.0f));
    }

    public final Vector3 getSize() {
        return this.max.minus(this.min);
    }

    public String toString() {
        return getCframe().getPosition() + "; " + getSize();
    }
}

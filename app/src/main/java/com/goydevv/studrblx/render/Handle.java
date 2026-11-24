package com.goydevv.studrblx.render;

import com.goydevv.studrblx.model.Vector3;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: SceneRenderer.kt */
/* JADX INFO: loaded from: classes8.dex */
public final class Handle {
    private final Vector3 axis;
    private final Vector3 base;
    private final int index;
    private final float radius;
    private final Vector3 tip;

    public Handle(int index, Vector3 axis, Vector3 base, Vector3 tip, float radius) {
        Intrinsics.checkNotNullParameter(axis, "axis");
        Intrinsics.checkNotNullParameter(base, "base");
        Intrinsics.checkNotNullParameter(tip, "tip");
        this.index = index;
        this.axis = axis;
        this.base = base;
        this.tip = tip;
        this.radius = radius;
    }

    public final Vector3 getAxis() {
        return this.axis;
    }

    public final Vector3 getBase() {
        return this.base;
    }

    public final int getIndex() {
        return this.index;
    }

    public final float getRadius() {
        return this.radius;
    }

    public final Vector3 getTip() {
        return this.tip;
    }
}

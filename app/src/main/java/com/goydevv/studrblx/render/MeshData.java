package com.goydevv.studrblx.render;

import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: RbxMesh.kt */
/* JADX INFO: loaded from: classes8.dex */
public final class MeshData {
    private final float[] max;
    private final float[] min;
    private final float[] verts;

    public MeshData(float[] verts, float[] min, float[] max) {
        Intrinsics.checkNotNullParameter(verts, "verts");
        Intrinsics.checkNotNullParameter(min, "min");
        Intrinsics.checkNotNullParameter(max, "max");
        this.verts = verts;
        this.min = min;
        this.max = max;
    }

    public final float[] getMax() {
        return this.max;
    }

    public final float[] getMin() {
        return this.min;
    }

    public final float[] getVerts() {
        return this.verts;
    }

    public final int getTriangles() {
        return this.verts.length / 24;
    }

    public final float size(int i) {
        return this.max[i] - this.min[i];
    }

    public final float center(int i) {
        return (this.max[i] + this.min[i]) / 2.0f;
    }
}

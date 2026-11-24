package com.goydevv.studrblx.render;

import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: compiled from: Meshes.kt */
/* JADX INFO: loaded from: classes8.dex */
public final class Mesh {
    private final int count;
    private final float[] cpu;
    private final int mode;
    private final int stride;
    private final int vbo;

    public Mesh(int vbo, int count, int mode, int stride, float[] cpu) {
        this.vbo = vbo;
        this.count = count;
        this.mode = mode;
        this.stride = stride;
        this.cpu = cpu;
    }

    public /* synthetic */ Mesh(int i, int i2, int i3, int i4, float[] fArr, int i5, DefaultConstructorMarker defaultConstructorMarker) {
        this(i, i2, i3, (i5 & 8) != 0 ? 6 : i4, (i5 & 16) != 0 ? null : fArr);
    }

    public final int getCount() {
        return this.count;
    }

    public final float[] getCpu() {
        return this.cpu;
    }

    public final int getMode() {
        return this.mode;
    }

    public final int getStride() {
        return this.stride;
    }

    public final int getVbo() {
        return this.vbo;
    }
}

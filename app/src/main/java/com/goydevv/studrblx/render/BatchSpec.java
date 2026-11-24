package com.goydevv.studrblx.render;

import com.goydevv.studrblx.model.CFrame;
import com.goydevv.studrblx.model.Vector3;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: StaticBatches.kt */
/* JADX INFO: loaded from: classes8.dex */
public final class BatchSpec {
    private final float b;
    private final CFrame cf;
    private final boolean cube;
    private final boolean emissive;
    private final float g;
    private final int kind;
    private final float[] mat;
    private final float[] mesh;
    private final Vector3 offset;
    private final boolean pending;
    private final float r;
    private final Vector3 scale;
    private final int stride;
    private final Vector3 surfA;
    private final Vector3 surfB;
    private final TexBind tex;

    public BatchSpec(float[] mesh, boolean cube, CFrame cf, Vector3 scale, float r, float g, float b, boolean emissive, float[] mat, Vector3 surfA, Vector3 surfB, int kind, TexBind tex, boolean pending, int stride, Vector3 offset) {
        Intrinsics.checkNotNullParameter(mesh, "mesh");
        Intrinsics.checkNotNullParameter(cf, "cf");
        Intrinsics.checkNotNullParameter(scale, "scale");
        Intrinsics.checkNotNullParameter(mat, "mat");
        Intrinsics.checkNotNullParameter(offset, "offset");
        this.mesh = mesh;
        this.cube = cube;
        this.cf = cf;
        this.scale = scale;
        this.r = r;
        this.g = g;
        this.b = b;
        this.emissive = emissive;
        this.mat = mat;
        this.surfA = surfA;
        this.surfB = surfB;
        this.kind = kind;
        this.tex = tex;
        this.pending = pending;
        this.stride = stride;
        this.offset = offset;
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public /* synthetic */ BatchSpec(float[] fArr, boolean z, CFrame cFrame, Vector3 vector3, float f, float f2, float f3, boolean z2, float[] fArr2, Vector3 vector4, Vector3 vector5, int i, TexBind texBind, boolean z3, int i2, Vector3 vector6, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this(fArr, z, cFrame, vector3, f, f2, f3, z2, fArr2, vector4, vector5, i, texBind, z3, (i3 & 16384) == 0 ? i2 : 6, (i3 & 32768) == 0 ? vector6 : Vector3.INSTANCE.getZERO());
    }

    public final CFrame getCf() {
        return this.cf;
    }

    public final boolean getCube() {
        return this.cube;
    }

    public final float[] getMesh() {
        return this.mesh;
    }

    public final Vector3 getScale() {
        return this.scale;
    }

    public final float getB() {
        return this.b;
    }

    public final boolean getEmissive() {
        return this.emissive;
    }

    public final float getG() {
        return this.g;
    }

    public final float[] getMat() {
        return this.mat;
    }

    public final float getR() {
        return this.r;
    }

    public final int getKind() {
        return this.kind;
    }

    public final boolean getPending() {
        return this.pending;
    }

    public final Vector3 getSurfA() {
        return this.surfA;
    }

    public final Vector3 getSurfB() {
        return this.surfB;
    }

    public final TexBind getTex() {
        return this.tex;
    }

    public final int getStride() {
        return this.stride;
    }

    public final Vector3 getOffset() {
        return this.offset;
    }
}

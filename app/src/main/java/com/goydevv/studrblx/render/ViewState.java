package com.goydevv.studrblx.render;

import android.opengl.Matrix;
import com.goydevv.studrblx.model.Vector3;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Camera.kt */
/* JADX INFO: loaded from: classes8.dex */
public final class ViewState {
    private final float[] view = new float[16];
    private final float[] proj = new float[16];
    private final float[] vp = new float[16];
    private final float[] invVp = new float[16];
    private int width = 1;
    private int height = 1;
    private Vector3 camPos = Vector3.INSTANCE.getZERO();
    private float near = 0.3f;

    public final float[] getView() {
        return this.view;
    }

    public final float[] getProj() {
        return this.proj;
    }

    public final float[] getVp() {
        return this.vp;
    }

    public final float[] getInvVp() {
        return this.invVp;
    }

    public final int getWidth() {
        return this.width;
    }

    public final void setWidth(int i) {
        this.width = i;
    }

    public final int getHeight() {
        return this.height;
    }

    public final void setHeight(int i) {
        this.height = i;
    }

    public final Vector3 getCamPos() {
        return this.camPos;
    }

    public final void setCamPos(Vector3 vector3) {
        Intrinsics.checkNotNullParameter(vector3, "<set-?>");
        this.camPos = vector3;
    }

    public final float getNear() {
        return this.near;
    }

    public final void setNear(float f) {
        this.near = f;
    }

    public static /* synthetic */ void update$default(ViewState viewState, Vector3 vector3, Vector3 vector4, float f, int i, int i2, float f2, int i3, Object obj) {
        if ((i3 & 32) != 0) {
            f2 = 0.3f;
        }
        viewState.update(vector3, vector4, f, i, i2, f2);
    }

    public final void update(Vector3 eye, Vector3 target, float fov, int w, int h, float near) {
        Intrinsics.checkNotNullParameter(eye, "eye");
        Intrinsics.checkNotNullParameter(target, "target");
        this.width = Math.max(w, 1);
        this.height = Math.max(h, 1);
        this.camPos = eye;
        this.near = near;
        Vector3 up = Math.abs(target.minus(eye).getUnit().getY()) > 0.999f ? Vector3.INSTANCE.getZ() : Vector3.INSTANCE.getY();
        Matrix.setLookAtM(this.view, 0, eye.getX(), eye.getY(), eye.getZ(), target.getX(), target.getY(), target.getZ(), up.getX(), up.getY(), up.getZ());
        Matrix.perspectiveM(this.proj, 0, fov, ((float) this.width) / ((float) Math.max(this.height, 1)), near, 10000.0f);
        Matrix.multiplyMM(this.vp, 0, this.proj, 0, this.view, 0);
        Matrix.invertM(this.invVp, 0, this.vp, 0);
    }

    public final float[] project(Vector3 p) {
        Intrinsics.checkNotNullParameter(p, "p");
        float[] v = {p.getX(), p.getY(), p.getZ(), 1.0f};
        float[] o = new float[4];
        Matrix.multiplyMV(o, 0, this.vp, 0, v, 0);
        if (o[3] <= 0.01f) {
            return null;
        }
        float nx = o[0] / o[3];
        float ny = o[1] / o[3];
        return new float[]{((nx * 0.5f) + 0.5f) * this.width, (1.0f - ((ny * 0.5f) + 0.5f)) * this.height, o[2] / o[3]};
    }

    public final Pair<Vector3, Vector3> screenRay(float x, float y) {
        float nx = ((x / this.width) * 2.0f) - 1.0f;
        float ny = 1.0f - ((y / this.height) * 2.0f);
        Vector3 a = screenRay$un(this, nx, ny, -1.0f);
        Vector3 b = screenRay$un(this, nx, ny, 1.0f);
        return TuplesKt.to(this.camPos, b.minus(a).getUnit());
    }

    private static final Vector3 screenRay$un(ViewState this$0, float nx, float ny, float z) {
        float[] o = new float[4];
        Matrix.multiplyMV(o, 0, this$0.invVp, 0, new float[]{nx, ny, z, 1.0f}, 0);
        return new Vector3(o[0] / o[3], o[1] / o[3], o[2] / o[3]);
    }

    public final float distTo(Vector3 p) {
        Intrinsics.checkNotNullParameter(p, "p");
        Vector3 d = p.minus(this.camPos);
        return (float) Math.sqrt(d.dot(d));
    }
}

package com.goydevv.studrblx.render;

import com.goydevv.studrblx.model.Vector3;
import java.util.ArrayList;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Meshes.kt */
/* JADX INFO: loaded from: classes8.dex */
public final class MeshBuilder {
    private final Vector3 center;
    private final ArrayList<Float> data;

    /* JADX WARN: Multi-variable type inference failed */
    public MeshBuilder() {
        this(null, 1, (DefaultConstructorMarker) null);
    }

    public MeshBuilder(Vector3 center) {
        Intrinsics.checkNotNullParameter(center, "center");
        this.center = center;
        this.data = new ArrayList<>();
    }

    public /* synthetic */ MeshBuilder(Vector3 vector3, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? Vector3.INSTANCE.getZERO() : vector3);
    }

    public final ArrayList<Float> getData() {
        return this.data;
    }

    private final void put(Vector3 p, Vector3 n) {
        this.data.add(Float.valueOf(p.getX()));
        this.data.add(Float.valueOf(p.getY()));
        this.data.add(Float.valueOf(p.getZ()));
        this.data.add(Float.valueOf(n.getX()));
        this.data.add(Float.valueOf(n.getY()));
        this.data.add(Float.valueOf(n.getZ()));
    }

    public final void tri(Vector3 a, Vector3 b, Vector3 c) {
        Intrinsics.checkNotNullParameter(a, "a");
        Intrinsics.checkNotNullParameter(b, "b");
        Intrinsics.checkNotNullParameter(c, "c");
        Vector3 n = b.minus(a).cross(c.minus(a)).getUnit();
        Vector3 mid = a.plus(b).plus(c).div(3.0f);
        if (n.dot(mid.minus(this.center)) < 0.0f) {
            put(a, n.unaryMinus());
            put(c, n.unaryMinus());
            put(b, n.unaryMinus());
        } else {
            put(a, n);
            put(b, n);
            put(c, n);
        }
    }

    public final void quad(Vector3 a, Vector3 b, Vector3 c, Vector3 d) {
        Intrinsics.checkNotNullParameter(a, "a");
        Intrinsics.checkNotNullParameter(b, "b");
        Intrinsics.checkNotNullParameter(c, "c");
        Intrinsics.checkNotNullParameter(d, "d");
        tri(a, b, c);
        tri(a, c, d);
    }

    public final void smoothTri(Vector3 a, Vector3 na, Vector3 b, Vector3 nb, Vector3 c, Vector3 nc) {
        Intrinsics.checkNotNullParameter(a, "a");
        Intrinsics.checkNotNullParameter(na, "na");
        Intrinsics.checkNotNullParameter(b, "b");
        Intrinsics.checkNotNullParameter(nb, "nb");
        Intrinsics.checkNotNullParameter(c, "c");
        Intrinsics.checkNotNullParameter(nc, "nc");
        Vector3 fn = b.minus(a).cross(c.minus(a));
        Vector3 mid = a.plus(b).plus(c).div(3.0f);
        float fDot = fn.dot(mid.minus(this.center));
        put(a, na);
        if (fDot < 0.0f) {
            put(c, nc);
            put(b, nb);
        } else {
            put(b, nb);
            put(c, nc);
        }
    }

    public final Mesh upload() {
        float[] arr = CollectionsKt.toFloatArray(this.data);
        return Meshes.INSTANCE.upload(arr, 6, 4, arr);
    }
}

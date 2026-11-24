package com.goydevv.studrblx.engine;

import com.goydevv.studrblx.model.Vector3;
import java.util.Iterator;
import java.util.List;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Geometry.kt */
/* JADX INFO: loaded from: classes7.dex */
public class ConvexShape {
    private final Vector3 center;
    private final List<Vector3> edges;
    private final List<Vector3> normals;
    private final List<Vector3> verts;

    public ConvexShape(List<Vector3> verts, List<Vector3> normals, List<Vector3> edges) {
        Intrinsics.checkNotNullParameter(verts, "verts");
        Intrinsics.checkNotNullParameter(normals, "normals");
        Intrinsics.checkNotNullParameter(edges, "edges");
        this.verts = verts;
        this.normals = normals;
        this.edges = edges;
        Iterable iterable = this.verts;
        Vector3 zero = Vector3.INSTANCE.getZERO();
        Iterator it = iterable.iterator();
        while (it.hasNext()) {
            zero = zero.plus((Vector3) it.next());
        }
        this.center = zero.div(this.verts.size());
    }

    public final List<Vector3> getEdges() {
        return this.edges;
    }

    public final List<Vector3> getNormals() {
        return this.normals;
    }

    public final List<Vector3> getVerts() {
        return this.verts;
    }

    public final Vector3 getCenter() {
        return this.center;
    }

    public Pair<Float, Float> project(Vector3 axis) {
        Intrinsics.checkNotNullParameter(axis, "axis");
        float mn = Float.MAX_VALUE;
        float mx = -3.4028235E38f;
        for (Vector3 v : this.verts) {
            float d = v.dot(axis);
            if (d < mn) {
                mn = d;
            }
            if (d > mx) {
                mx = d;
            }
        }
        return TuplesKt.to(Float.valueOf(mn), Float.valueOf(mx));
    }

    public void project(float ax, float ay, float az, float[] out) {
        float mn = Float.MAX_VALUE;
        float mx = -3.4028235E38f;
        List<Vector3> vs = this.verts;
        int n = vs.size();
        for (int i = 0; i < n; i++) {
            Vector3 v = vs.get(i);
            float d = (v.getX() * ax) + (v.getY() * ay) + (v.getZ() * az);
            if (d < mn) {
                mn = d;
            }
            if (d > mx) {
                mx = d;
            }
        }
        out[0] = mn;
        out[1] = mx;
    }
}

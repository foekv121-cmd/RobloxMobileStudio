package com.goydevv.studrblx.render;

import android.opengl.GLES20;
import com.goydevv.studrblx.model.Vector3;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Meshes.kt */
/* JADX INFO: loaded from: classes8.dex */
public final class Meshes {
    private static final float H = 0.5f;
    public static final Meshes INSTANCE = new Meshes();

    private Meshes() {
    }

    public static /* synthetic */ Mesh upload$default(Meshes meshes, float[] fArr, int i, int i2, float[] fArr2, int i3, Object obj) {
        if ((i3 & 8) != 0) {
            fArr2 = null;
        }
        return meshes.upload(fArr, i, i2, fArr2);
    }

    public final Mesh upload(float[] arr, int stride, int mode, float[] cpu) {
        Intrinsics.checkNotNullParameter(arr, "arr");
        FloatBuffer buf = ByteBuffer.allocateDirect(arr.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        buf.put(arr).position(0);
        int[] ids = new int[1];
        GLES20.glGenBuffers(1, ids, 0);
        GLES20.glBindBuffer(34962, ids[0]);
        GLES20.glBufferData(34962, arr.length * 4, buf, 35044);
        GLES20.glBindBuffer(34962, 0);
        return new Mesh(ids[0], arr.length / stride, mode, stride, cpu);
    }

    private final Vector3 v(float x, float y, float z) {
        return new Vector3(x, y, z);
    }

    public final Mesh cube() {
        MeshBuilder b = new MeshBuilder(null, 1, null);
        Vector3[] c = new Vector3[8];
        for (int i = 0; i < 8; i++) {
            Meshes meshes = INSTANCE;
            float f = 0.5f;
            float f2 = (i & 1) != 0 ? 0.5f : -0.5f;
            float f3 = (i & 2) != 0 ? 0.5f : -0.5f;
            if ((i & 4) == 0) {
                f = -0.5f;
            }
            c[i] = meshes.v(f2, f3, f);
        }
        b.quad(c[0], c[1], c[3], c[2]);
        b.quad(c[4], c[5], c[7], c[6]);
        b.quad(c[0], c[1], c[5], c[4]);
        b.quad(c[2], c[3], c[7], c[6]);
        b.quad(c[0], c[2], c[6], c[4]);
        b.quad(c[1], c[3], c[7], c[5]);
        return b.upload();
    }

    public final Mesh wedge() {
        MeshBuilder b = new MeshBuilder(v(0.0f, -0.2f, 0.2f));
        Vector3 bl = v(-0.5f, -0.5f, -0.5f);
        Vector3 br = v(0.5f, -0.5f, -0.5f);
        Vector3 blb = v(-0.5f, -0.5f, 0.5f);
        Vector3 brb = v(0.5f, -0.5f, 0.5f);
        Vector3 tl = v(-0.5f, 0.5f, 0.5f);
        Vector3 tr = v(0.5f, 0.5f, 0.5f);
        b.quad(bl, br, brb, blb);
        b.quad(blb, brb, tr, tl);
        b.quad(bl, br, tr, tl);
        b.tri(bl, blb, tl);
        b.tri(br, brb, tr);
        return b.upload();
    }

    public final Mesh cornerWedge() {
        MeshBuilder b = new MeshBuilder(v(0.2f, -0.25f, -0.2f));
        Vector3 a = v(0.5f, 0.5f, -0.5f);
        Vector3 p0 = v(-0.5f, -0.5f, -0.5f);
        Vector3 p1 = v(0.5f, -0.5f, -0.5f);
        Vector3 p2 = v(0.5f, -0.5f, 0.5f);
        Vector3 p3 = v(-0.5f, -0.5f, 0.5f);
        b.quad(p0, p1, p2, p3);
        b.tri(p1, p2, a);
        b.tri(p0, p1, a);
        b.tri(p0, p3, a);
        b.tri(p3, p2, a);
        return b.upload();
    }

    public static /* synthetic */ Mesh sphere$default(Meshes meshes, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = 14;
        }
        if ((i3 & 2) != 0) {
            i2 = 24;
        }
        return meshes.sphere(i, i2);
    }

    public final Mesh sphere(int rings, int segs) {
        MeshBuilder b = new MeshBuilder(null, 1, null);
        for (int r = 0; r < rings; r++) {
            for (int s = 0; s < segs; s++) {
                Vector3 a = sphere$pt(rings, segs, r, s);
                Vector3 c = sphere$pt(rings, segs, r + 1, s);
                Vector3 d = sphere$pt(rings, segs, r + 1, s + 1);
                Vector3 e = sphere$pt(rings, segs, r, s + 1);
                if (r != 0) {
                    b.smoothTri(a, a.getUnit(), c, c.getUnit(), e, e.getUnit());
                }
                if (r != rings - 1) {
                    b.smoothTri(c, c.getUnit(), d, d.getUnit(), e, e.getUnit());
                }
            }
        }
        return b.upload();
    }

    private static final Vector3 sphere$pt(int $rings, int $segs, int r, int s) {
        double th = (((double) r) * 3.141592653589793d) / ((double) $rings);
        double ph = (((double) s) * 6.283185307179586d) / ((double) $segs);
        return INSTANCE.v(((float) (Math.sin(th) * Math.cos(ph))) * 0.5f, ((float) Math.cos(th)) * 0.5f, ((float) (Math.sin(th) * Math.sin(ph))) * 0.5f);
    }

    public static /* synthetic */ Mesh cylinder$default(Meshes meshes, int i, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            i = 24;
        }
        return meshes.cylinder(i);
    }

    public final Mesh cylinder(int segs) {
        MeshBuilder b = new MeshBuilder(null, 1, null);
        for (int i = 0; i < segs; i++) {
            Pair<Float, Float> pairCylinder$ring = cylinder$ring(segs, i);
            float y0 = pairCylinder$ring.component1().floatValue();
            float z0 = pairCylinder$ring.component2().floatValue();
            Pair<Float, Float> pairCylinder$ring2 = cylinder$ring(segs, i + 1);
            float y1 = pairCylinder$ring2.component1().floatValue();
            float z1 = pairCylinder$ring2.component2().floatValue();
            Vector3 n0 = v(0.0f, y0, z0).getUnit();
            Vector3 n1 = v(0.0f, y1, z1).getUnit();
            Vector3 a = v(-0.5f, y0, z0);
            Vector3 c = v(0.5f, y0, z0);
            Vector3 d = v(0.5f, y1, z1);
            Vector3 e = v(-0.5f, y1, z1);
            b.smoothTri(a, n0, c, n0, d, n1);
            b.smoothTri(a, n0, d, n1, e, n1);
            b.tri(v(0.5f, 0.0f, 0.0f), v(0.5f, y0, z0), v(0.5f, y1, z1));
            b.tri(v(-0.5f, 0.0f, 0.0f), v(-0.5f, y1, z1), v(-0.5f, y0, z0));
        }
        return b.upload();
    }

    private static final Pair<Float, Float> cylinder$ring(int $segs, int i) {
        double a = (((double) i) * 6.283185307179586d) / ((double) $segs);
        return TuplesKt.to(Float.valueOf((float) (Math.cos(a) * 0.5d)), Float.valueOf((float) (Math.sin(a) * 0.5d)));
    }

    /* JADX INFO: compiled from: Meshes.kt */
    private static final class Prof {
        private final float nr;
        private final float ny;
        private final float r;
        private final float y;

        public Prof(float r, float y, float nr, float ny) {
            this.r = r;
            this.y = y;
            this.nr = nr;
            this.ny = ny;
        }

        public final float getNr() {
            return this.nr;
        }

        public final float getNy() {
            return this.ny;
        }

        public final float getR() {
            return this.r;
        }

        public final float getY() {
            return this.y;
        }
    }

    private final Mesh revolve(List<Pair<Prof, Prof>> segments, int segs) {
        MeshBuilder b = new MeshBuilder(null, 1, null);
        for (Pair<Prof, Prof> pair : segments) {
            Prof p = pair.component1();
            Prof q = pair.component2();
            for (int i = 0; i < segs; i++) {
                double a0 = (((double) i) * 6.283185307179586d) / ((double) segs);
                double a1 = (((double) (i + 1)) * 6.283185307179586d) / ((double) segs);
                Vector3 pa = revolve$p3(p, a0);
                Vector3 pb = revolve$p3(p, a1);
                Vector3 qa = revolve$p3(q, a0);
                Vector3 qb = revolve$p3(q, a1);
                Vector3 npa = revolve$n3(p, a0);
                Vector3 npb = revolve$n3(p, a1);
                Vector3 nqa = revolve$n3(q, a0);
                Vector3 nqb = revolve$n3(q, a1);
                if (p.getR() > 1.0E-4f) {
                    b.smoothTri(pa, npa, pb, npb, qb, nqb);
                }
                if (q.getR() > 1.0E-4f) {
                    b.smoothTri(pa, npa, qb, nqb, qa, nqa);
                }
            }
        }
        return b.upload();
    }

    private static final Vector3 revolve$p3(Prof p, double a) {
        return INSTANCE.v((float) (Math.cos(a) * ((double) p.getR())), p.getY(), (float) (Math.sin(a) * ((double) p.getR())));
    }

    private static final Vector3 revolve$n3(Prof p, double a) {
        return INSTANCE.v((float) (Math.cos(a) * ((double) p.getNr())), p.getNy(), (float) (Math.sin(a) * ((double) p.getNr()))).getUnit();
    }

    public static /* synthetic */ Mesh head$default(Meshes meshes, int i, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            i = 28;
        }
        return meshes.head(i);
    }

    public final Mesh head(int segs) {
        double d;
        ArrayList pts = new ArrayList();
        float f = -0.5f;
        pts.add(new Prof(0.0f, -0.5f, 0.0f, -1.0f));
        int i = 0;
        while (true) {
            d = 1.5707963267948966d;
            double a = ((((double) i) * 1.5707963267948966d) / ((double) 6)) - 1.5707963267948966d;
            float f2 = f;
            pts.add(new Prof((0.5f - 0.2f) + ((float) (Math.cos(a) * ((double) 0.2f))), 0.2f + f2 + ((float) (Math.sin(a) * ((double) 0.2f))), (float) Math.cos(a), (float) Math.sin(a)));
            if (i == 6) {
                break;
            }
            i++;
            f = f2;
        }
        int i2 = 0;
        while (true) {
            double a2 = (((double) i2) * d) / ((double) 6);
            pts.add(new Prof((0.5f - 0.2f) + ((float) (Math.cos(a2) * ((double) 0.2f))), (0.5f - 0.2f) + ((float) (Math.sin(a2) * ((double) 0.2f))), (float) Math.cos(a2), (float) Math.sin(a2)));
            if (i2 == 6) {
                pts.add(new Prof(0.0f, 0.5f, 0.0f, 1.0f));
                return revolve(CollectionsKt.zipWithNext(pts), segs);
            }
            i2++;
            d = 1.5707963267948966d;
        }
    }

    public static /* synthetic */ Mesh cylinderY$default(Meshes meshes, int i, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            i = 24;
        }
        return meshes.cylinderY(i);
    }

    public final Mesh cylinderY(int segs) {
        return revolve(CollectionsKt.listOf(new Pair[]{TuplesKt.to(new Prof(0.0f, -0.5f, 0.0f, -1.0f), new Prof(0.5f, -0.5f, 0.0f, -1.0f)), TuplesKt.to(new Prof(0.5f, -0.5f, 1.0f, 0.0f), new Prof(0.5f, 0.5f, 1.0f, 0.0f)), TuplesKt.to(new Prof(0.5f, 0.5f, 0.0f, 1.0f), new Prof(0.0f, 0.5f, 0.0f, 1.0f))}), segs);
    }

    public final Mesh headFace() {
        double arc = Math.toRadians(150.0d);
        ArrayList data = new ArrayList();
        int i = 0;
        while (i < 16) {
            int j = 0;
            while (j < 8) {
                headFace$put(16, 8, arc, data, 0.506f, 0.36f, -0.36f, i, j);
                int j2 = j;
                headFace$put(16, 8, arc, data, 0.506f, 0.36f, -0.36f, i, j2 + 1);
                int i2 = i;
                headFace$put(16, 8, arc, data, 0.506f, 0.36f, -0.36f, i2 + 1, j2 + 1);
                headFace$put(16, 8, arc, data, 0.506f, 0.36f, -0.36f, i2, j2);
                headFace$put(16, 8, arc, data, 0.506f, 0.36f, -0.36f, i2 + 1, j2 + 1);
                headFace$put(16, 8, arc, data, 0.506f, 0.36f, -0.36f, i2 + 1, j2);
                j = j2 + 1;
                i = i2;
            }
            i++;
        }
        return upload$default(this, CollectionsKt.toFloatArray(data), 5, 4, null, 8, null);
    }

    private static final void headFace$put(int cols, int rows, double arc, ArrayList<Float> arrayList, float r, float yTop, float yBot, int i, int j) {
        float u = i / cols;
        float vv = j / rows;
        double a = ((-1.5707963267948966d) - (arc / 2.0d)) + (((double) u) * arc);
        arrayList.add(Float.valueOf((float) (Math.cos(a) * ((double) r))));
        arrayList.add(Float.valueOf(yTop + ((yBot - yTop) * vv)));
        arrayList.add(Float.valueOf((float) (Math.sin(a) * ((double) r))));
        arrayList.add(Float.valueOf(1.0f - u));
        arrayList.add(Float.valueOf((0.76f * vv) + 0.12f));
    }

    public final Mesh quad() {
        return upload$default(this, new float[]{-0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 0.5f, -0.5f, 0.0f, 1.0f, 1.0f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, -0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, -0.5f, 0.5f, 0.0f, 0.0f, 0.0f}, 5, 4, null, 8, null);
    }

    public final Mesh screenQuad() {
        return upload$default(this, new float[]{-1.0f, -1.0f, 1.0f, -1.0f, 1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f}, 2, 4, null, 8, null);
    }

    public final Mesh boxLines() {
        float[][] c = new float[8][];
        int i = 0;
        for (int i2 = 0; i2 < 8; i2++) {
            float f = 0.5f;
            float f2 = (i2 & 1) != 0 ? 0.5f : -0.5f;
            float f3 = (i2 & 2) != 0 ? 0.5f : -0.5f;
            if ((i2 & 4) == 0) {
                f = -0.5f;
            }
            c[i2] = new float[]{f2, f3, f};
        }
        int[] edges = {0, 1, 2, 3, 4, 5, 6, 7, 0, 2, 1, 3, 4, 6, 5, 7, 0, 4, 1, 5, 2, 6, 3, 7};
        float[] arr = new float[edges.length * 3];
        int i3 = 0;
        int length = edges.length;
        while (i < length) {
            ArraysKt.copyInto(c[edges[i]], arr, i3 * 3, 0, c[edges[i]].length);
            i++;
            i3++;
        }
        return upload$default(this, arr, 3, 1, null, 8, null);
    }
}

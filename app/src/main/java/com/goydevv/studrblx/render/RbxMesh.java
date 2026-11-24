package com.goydevv.studrblx.render;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.sequences.SequencesKt;
import kotlin.text.Charsets;
import kotlin.text.MatchResult;
import kotlin.text.Regex;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: RbxMesh.kt */
/* JADX INFO: loaded from: classes8.dex */
public final class RbxMesh {
    public static final RbxMesh INSTANCE = new RbxMesh();

    /* JADX INFO: compiled from: RbxMesh.kt */
    public static final class UnsupportedMesh extends Exception {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public UnsupportedMesh(String msg) {
            super(msg);
            Intrinsics.checkNotNullParameter(msg, "msg");
        }
    }

    private RbxMesh() {
    }

    public final MeshData parse(byte[] bytes) throws UnsupportedMesh {
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        String head = new String(bytes, 0, Math.min(bytes.length, 13), Charsets.ISO_8859_1);
        if (!StringsKt.startsWith(head, "version ", false)) {
            throw new UnsupportedMesh("not a mesh file");
        }
        String ver = head.substring(8, 12);
        Intrinsics.checkNotNullExpressionValue(ver, "substring(...)");
        int iIndexOf = ArraysKt.indexOf(bytes, (byte) 10);
        if (iIndexOf < 0) {
            throw new UnsupportedMesh("bad header");
        }
        int nl = iIndexOf + 1;
        if (StringsKt.startsWith(ver, "1.", false)) {
            return parseV1(new String(bytes, Charsets.ISO_8859_1), Intrinsics.areEqual(ver, "1.00"));
        }
        if (Intrinsics.areEqual(ver, "2.00")) {
            return parseV2(bytes, nl);
        }
        if (StringsKt.startsWith(ver, "3.", false)) {
            return parseV3(bytes, nl);
        }
        if (StringsKt.startsWith(ver, "4.", false) || StringsKt.startsWith(ver, "5.", false)) {
            return parseV4(bytes, nl);
        }
        if (StringsKt.startsWith(ver, "6.", false) || StringsKt.startsWith(ver, "7.", false)) {
            return parseChunked(bytes, nl);
        }
        throw new UnsupportedMesh("mesh version " + ver);
    }

    private final MeshData build(float[] pos, float[] nrm, float[] uv, int[] faces, int f0, int f1) {
        float[] mx;
        int n = pos.length / 3;
        float[] out = new float[(f1 - f0) * 24];
        float[] mn = {Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE};
        float[] mx2 = {-3.4028235E38f, -3.4028235E38f, -3.4028235E38f};
        int o = 0;
        for (int f = f0; f < f1; f++) {
            for (int k = 0; k < 3; k++) {
                int v = faces[(f * 3) + k];
                if (v >= 0 && v < n) {
                    for (int c = 0; c < 3; c++) {
                        float p = pos[(v * 3) + c];
                        out[o + c] = p;
                        if (p < mn[c]) {
                            mn[c] = p;
                        }
                        if (p > mx2[c]) {
                            mx2[c] = p;
                        }
                    }
                    int c2 = o + 3;
                    out[c2] = nrm[v * 3];
                    out[o + 4] = nrm[(v * 3) + 1];
                    out[o + 5] = nrm[(v * 3) + 2];
                    out[o + 6] = uv[v * 2];
                    out[o + 7] = uv[(v * 2) + 1];
                }
                o += 8;
            }
        }
        if (mn[0] > mx2[0]) {
            ArraysKt.fill(mn, 0.0f, 0, mn.length);
            mx = mx2;
            ArraysKt.fill(mx, 0.0f, 0, mx.length);
        } else {
            mx = mx2;
        }
        return new MeshData(out, mn, mx);
    }

    private final MeshData parseV1(String text, boolean scaleHalf) throws UnsupportedMesh {
        String string;
        Integer intOrNull;
        List lines = StringsKt.split((CharSequence) text, new char[]{'\n'}, false, 0);
        String str = (String) CollectionsKt.getOrNull(lines, 1);
        if (str == null || (string = StringsKt.trim((CharSequence) str).toString()) == null || (intOrNull = StringsKt.toIntOrNull(string)) == null) {
            throw new UnsupportedMesh("bad v1 mesh");
        }
        int faces = intOrNull.intValue();
        int i = 2;
        List nums = SequencesKt.toList(SequencesKt.map((new Regex("\\[([^\\]]*)]")).findAll(CollectionsKt.joinToString(CollectionsKt.drop(lines, 2), " ", "", "", -1, "...", null), 0), new Function1() { // from class: com.goydevv.studrblx.render.RbxMesh$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RbxMesh.parseV1$lambda$2((MatchResult) obj);
            }
        }));
        int i2 = 3;
        int count = Math.min(faces * 3, nums.size() / 3);
        float[] pos = new float[count * 3];
        float[] nrm = new float[count * 3];
        float[] uv = new float[count * 2];
        float s = scaleHalf ? 0.5f : 1.0f;
        int i3 = 0;
        while (i3 < count) {
            List p = (List) nums.get(i3 * 3);
            List nn = (List) nums.get((i3 * 3) + 1);
            List t = (List) nums.get((i3 * 3) + i);
            int c = 0;
            while (c < i2) {
                pos[(i3 * 3) + c] = ((Number) (c >= 0 && c < p.size() ? p.get(c) : Float.valueOf(0.0f))).floatValue() * s;
                nrm[(i3 * 3) + c] = ((Number) (c >= 0 && c < nn.size() ? nn.get(c) : Float.valueOf(0.0f))).floatValue();
                c++;
                i2 = 3;
            }
            int c2 = i3 * 2;
            uv[c2] = ((Number) (t.size() > 0 ? t.get(0) : Float.valueOf(0.0f))).floatValue();
            uv[(i3 * 2) + 1] = 1.0f - ((Number) (1 < t.size() ? t.get(1) : Float.valueOf(0.0f))).floatValue();
            i3++;
            i = 2;
            i2 = 3;
        }
        int[] iArr = new int[count];
        for (int i4 = 0; i4 < count; i4++) {
            iArr[i4] = i4;
        }
        return build(pos, nrm, uv, iArr, 0, count / 3);
    }

    static final List parseV1$lambda$2(MatchResult m) {
        Intrinsics.checkNotNullParameter(m, "m");
        Iterable iterableSplit$default = StringsKt.split((CharSequence) m.getGroupValues().get(1), new char[]{','}, false, 0);
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterableSplit$default, 10));
        Iterator it = iterableSplit$default.iterator();
        while (it.hasNext()) {
            Float floatOrNull = StringsKt.toFloatOrNull(StringsKt.trim((CharSequence) it.next()).toString());
            arrayList.add(Float.valueOf(floatOrNull != null ? floatOrNull.floatValue() : 0.0f));
        }
        return (List) arrayList;
    }

    private final ByteBuffer buf(byte[] b, int at) {
        ByteBuffer byteBufferOrder = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN);
        byteBufferOrder.position(at);
        return byteBufferOrder;
    }

    public final float[] smoothNormals(float[] pos, int[] faces) {
        Intrinsics.checkNotNullParameter(pos, "pos");
        Intrinsics.checkNotNullParameter(faces, "faces");
        int nv = pos.length / 3;
        float[] n = new float[pos.length];
        java.util.HashMap<Long, float[]> welded = new java.util.HashMap<>(nv);
        long[] keys = new long[nv];
        for (int v = 0; v < nv; v++) {
            long qx = ((long) Math.round(pos[v * 3] * 250.0f)) & 0x1FFFFFL;
            long qy = ((long) Math.round(pos[(v * 3) + 1] * 250.0f)) & 0x1FFFFFL;
            long qz = ((long) Math.round(pos[(v * 3) + 2] * 250.0f)) & 0x1FFFFFL;
            long key = (qx << 42) | (qy << 21) | qz;
            keys[v] = key;
            if (!welded.containsKey(key)) {
                welded.put(key, new float[3]);
            }
        }
        int f = 0;
        while (f + 2 < faces.length) {
            int a = faces[f];
            int b = faces[f + 1];
            int c = faces[f + 2];
            f += 3;
            if (a >= 0 && a < nv && b >= 0 && b < nv && c >= 0 && c < nv) {
                float ux = pos[b * 3] - pos[a * 3];
                float uy = pos[(b * 3) + 1] - pos[(a * 3) + 1];
                float uz = pos[(b * 3) + 2] - pos[(a * 3) + 2];
                float vx = pos[c * 3] - pos[a * 3];
                float vy = pos[(c * 3) + 1] - pos[(a * 3) + 1];
                float vz = pos[(c * 3) + 2] - pos[(a * 3) + 2];
                float nx = (uy * vz) - (uz * vy);
                float ny = (uz * vx) - (ux * vz);
                float nz = (ux * vy) - (uy * vx);
                float[] wa = welded.get(keys[a]);
                if (wa != null) { wa[0] += nx; wa[1] += ny; wa[2] += nz; }
                float[] wb = welded.get(keys[b]);
                if (wb != null) { wb[0] += nx; wb[1] += ny; wb[2] += nz; }
                float[] wc = welded.get(keys[c]);
                if (wc != null) { wc[0] += nx; wc[1] += ny; wc[2] += nz; }
            }
        }
        for (int v2 = 0; v2 < nv; v2++) {
            float[] acc = welded.get(keys[v2]);
            float ax = acc != null ? acc[0] : 0.0f;
            float ay = acc != null ? acc[1] : 1.0f;
            float az = acc != null ? acc[2] : 0.0f;
            float l = (float) Math.sqrt((ax * ax) + (ay * ay) + (az * az));
            if (l > 1.0E-12f) {
                n[v2 * 3] = ax / l;
                n[(v2 * 3) + 1] = ay / l;
                n[(v2 * 3) + 2] = az / l;
            } else {
                n[v2 * 3] = 0.0f;
                n[(v2 * 3) + 1] = 1.0f;
                n[(v2 * 3) + 2] = 0.0f;
            }
        }
        return n;
    }

    /* JADX INFO: compiled from: RbxMesh.kt */
    private static final class Verts {
        private final float[] nrm;
        private final float[] pos;
        private final float[] uv;

        public Verts(float[] pos, float[] nrm, float[] uv) {
            Intrinsics.checkNotNullParameter(pos, "pos");
            Intrinsics.checkNotNullParameter(nrm, "nrm");
            Intrinsics.checkNotNullParameter(uv, "uv");
            this.pos = pos;
            this.nrm = nrm;
            this.uv = uv;
        }

        public final float[] getNrm() {
            return this.nrm;
        }

        public final float[] getPos() {
            return this.pos;
        }

        public final float[] getUv() {
            return this.uv;
        }
    }

    private final Verts readVerts(ByteBuffer bb, int n, int stride) {
        float[] pos = new float[n * 3];
        float[] nrm = new float[n * 3];
        float[] uv = new float[n * 2];
        for (int i = 0; i < n; i++) {
            int start = bb.position();
            pos[i * 3] = bb.getFloat();
            pos[(i * 3) + 1] = bb.getFloat();
            pos[(i * 3) + 2] = bb.getFloat();
            nrm[i * 3] = bb.getFloat();
            nrm[(i * 3) + 1] = bb.getFloat();
            nrm[(i * 3) + 2] = bb.getFloat();
            uv[i * 2] = bb.getFloat();
            uv[(i * 2) + 1] = bb.getFloat();
            bb.position(start + stride);
        }
        return new Verts(pos, nrm, uv);
    }

    static /* synthetic */ int[] readFaces$default(RbxMesh rbxMesh, ByteBuffer byteBuffer, int i, int i2, int i3, Object obj) {
        if ((i3 & 4) != 0) {
            i2 = 12;
        }
        return rbxMesh.readFaces(byteBuffer, i, i2);
    }

    private final int[] readFaces(ByteBuffer bb, int n, int stride) {
        int[] f = new int[n * 3];
        for (int i = 0; i < n; i++) {
            int s = bb.position();
            f[i * 3] = bb.getInt();
            f[(i * 3) + 1] = bb.getInt();
            f[(i * 3) + 2] = bb.getInt();
            bb.position(s + stride);
        }
        return f;
    }

    private final MeshData parseV2(byte[] b, int at) {
        ByteBuffer bb = buf(b, at);
        int hs = bb.getShort() & 65535;
        int vs = bb.get() & 255;
        int fs = bb.get() & 255;
        int nv = bb.getInt();
        int nf = bb.getInt();
        bb.position(at + hs);
        Intrinsics.checkNotNull(bb);
        Verts v = readVerts(bb, nv, vs);
        int[] f = readFaces(bb, nf, fs);
        return build(v.getPos(), v.getNrm(), v.getUv(), f, 0, nf);
    }

    private final MeshData parseV3(byte[] b, int at) {
        ByteBuffer bb = buf(b, at);
        int hs = bb.getShort() & 65535;
        int vs = bb.get() & 255;
        int fs = bb.get() & 255;
        bb.getShort();
        int nl = bb.getShort() & 65535;
        int nv = bb.getInt();
        int nf = bb.getInt();
        bb.position(at + hs);
        Intrinsics.checkNotNull(bb);
        Verts v = readVerts(bb, nv, vs);
        int[] f = readFaces(bb, nf, fs);
        int[] lods = new int[nl];
        for (int i = 0; i < nl; i++) {
            lods[i] = bb.getInt();
        }
        int end = nl >= 2 ? RangesKt.coerceIn(lods[1], 1, nf) : nf;
        return build(v.getPos(), v.getNrm(), v.getUv(), f, nl >= 2 ? RangesKt.coerceIn(lods[0], 0, end) : 0, end);
    }

    private final MeshData parseV4(byte[] b, int at) {
        ByteBuffer bb = buf(b, at);
        int hs = bb.getShort() & 65535;
        bb.getShort();
        int nv = bb.getInt();
        int nf = bb.getInt();
        int nl = bb.getShort() & 65535;
        int nBones = bb.getShort() & 65535;
        bb.position(at + hs);
        Intrinsics.checkNotNull(bb);
        Verts v = readVerts(bb, nv, 40);
        if (nBones > 0) {
            bb.position(bb.position() + (nv * 8));
        }
        int[] f = readFaces$default(this, bb, nf, 0, 4, null);
        int[] lods = new int[nl];
        for (int i = 0; i < nl; i++) {
            lods[i] = bb.getInt();
        }
        int end = nl >= 2 ? RangesKt.coerceIn(lods[1], 1, nf) : nf;
        return build(v.getPos(), v.getNrm(), v.getUv(), f, nl >= 2 ? RangesKt.coerceIn(lods[0], 0, end) : 0, end);
    }

    /* JADX WARN: Code duplicated, block: B:45:0x00da  */
    /* JADX WARN: Code duplicated, block: B:47:0x00e3  */
    /* JADX WARN: Code duplicated, block: B:48:0x00e6  */
    /* JADX WARN: Code duplicated, block: B:50:0x00ec  */
    /* JADX WARN: Code duplicated, block: B:51:0x00f3  */
    /* JADX WARN: Code duplicated, block: B:53:0x00fc A[LOOP:1: B:52:0x00fa->B:53:0x00fc, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:55:0x0115  */
    /* JADX WARN: Code duplicated, block: B:74:0x01a5  */
    /* JADX WARN: Code duplicated, block: B:76:0x01a8  */
    /* JADX WARN: Code duplicated, block: B:77:0x01ad  */
    /* JADX WARN: Code duplicated, block: B:83:0x01c1  */
    /* JADX WARN: Code duplicated, block: B:89:0x01dc  */
    /* JADX WARN: Code duplicated, block: B:92:0x01e5  */
    private final MeshData parseChunked(byte[] b, int at) throws UnsupportedMesh {
        ByteBuffer bb = buf(b, at);
        Verts core = null;
        int[] faces = null;
        int[] lods = null;

        while (bb.remaining() >= 16) {
            byte[] nameBytes = new byte[8];
            bb.get(nameBytes);
            String name = StringsKt.trimEnd(new String(nameBytes, Charsets.ISO_8859_1), (char) 0);
            int cver = bb.getInt();
            int size = bb.getInt();
            int start = bb.position();
            if (size < 0 || start + size > b.length) {
                break;
            }
            if ("COREMESH".equals(name)) {
                if (cver == 1) {
                    int nv = bb.getInt();
                    core = readVerts(bb, nv, 40);
                    int nfChunk = bb.getInt();
                    faces = readFaces$default(this, bb, nfChunk, 0, 4, null);
                } else if (cver == 2) {
                    int len = bb.getInt();
                    try {
                        Draco.Mesh m = Draco.INSTANCE.decode(b, bb.position(), Math.min(len, size - 4));
                        int n = m.getNumPoints();
                        Draco.Attribute posAtt = m.attribute(0);
                        if (posAtt == null || posAtt.getComponents() != 3 || posAtt.getValues() == null || posAtt.getValues().length < n * 3) {
                            throw new UnsupportedMesh("Draco mesh without positions");
                        }
                        float[] pos = posAtt.getValues();
                        float[] nrm = null;
                        Draco.Attribute nrmAtt = m.attribute(1);
                        if (nrmAtt != null && nrmAtt.getComponents() == 3 && nrmAtt.getValues() != null && nrmAtt.getValues().length >= n * 3) {
                            nrm = nrmAtt.getValues();
                        }
                        if (nrm == null) {
                            nrm = smoothNormals(pos, m.getFaces());
                        } else {
                            // Check if decoded normals are degenerate/zero or flat-quantized; if so, compute smooth welded normals
                            int bad = 0;
                            for (int ni = 0; ni < n; ni++) {
                                float nx = nrm[ni * 3], ny = nrm[(ni * 3) + 1], nz = nrm[(ni * 3) + 2];
                                float l2 = (nx * nx) + (ny * ny) + (nz * nz);
                                if (l2 < 0.01f) bad++;
                            }
                            if (bad > n / 4) {
                                nrm = smoothNormals(pos, m.getFaces());
                            }
                        }
                        float[] uv;
                        Draco.Attribute uvAtt = m.attribute(3);
                        if (uvAtt != null && uvAtt.getComponents() >= 2 && uvAtt.getValues() != null && uvAtt.getValues().length >= n * uvAtt.getComponents()) {
                            if (uvAtt.getComponents() == 2) {
                                uv = uvAtt.getValues();
                            } else {
                                int c = uvAtt.getComponents();
                                float[] srcUv = uvAtt.getValues();
                                uv = new float[n * 2];
                                for (int i = 0; i < n; i++) {
                                    uv[i * 2] = srcUv[i * c];
                                    uv[(i * 2) + 1] = srcUv[(i * c) + 1];
                                }
                            }
                        } else {
                            uv = new float[n * 2];
                        }
                        core = new Verts(pos, nrm, uv);
                        faces = m.getFaces();
                    } catch (Draco.DracoException e) {
                        throw new UnsupportedMesh("Draco: " + e.getMessage());
                    }
                } else {
                    throw new UnsupportedMesh("mesh chunk version " + cver);
                }
            } else if ("LODS".equals(name)) {
                bb.getShort();
                bb.get();
                int nLods = bb.getInt();
                int[] arr = new int[nLods];
                for (int i = 0; i < nLods; i++) {
                    arr[i] = bb.getInt();
                }
                lods = arr;
            }
            bb.position(start + size);
        }

        if (core == null) {
            throw new UnsupportedMesh("mesh without geometry");
        }
        int[] f = faces != null ? faces : new int[0];
        int nf = f.length / 3;
        int startFace = 0;
        int end = nf;
        if (lods != null && lods.length >= 2 && lods[1] > lods[0] && lods[1] <= nf) {
            startFace = RangesKt.coerceIn(lods[0], 0, nf);
            end = RangesKt.coerceIn(lods[1], startFace + 1, nf);
        }
        return build(core.getPos(), core.getNrm(), core.getUv(), f, startFace, end);
    }
}

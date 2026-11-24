package com.goydevv.studrblx.format;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.Charsets;

/* JADX INFO: compiled from: CsgMesh.kt */
/* JADX INFO: loaded from: classes5.dex */
public final class CsgMesh {
    public static final CsgMesh INSTANCE = new CsgMesh();
    private static final int[] KEY = {86, 46, 110, 88, 49, 32, 48, 4, 52, 105, 12, 119, 12, 1, 94, 0, 26, 96, 55, 105, 29, 82, 43, 7, 79, 36, 89, 101, 83, 4, 122};
    private static final byte[] MAGIC;

    /* JADX INFO: compiled from: CsgMesh.kt */
    public static final class CsgException extends Exception {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public CsgException(String msg) {
            super(msg);
            Intrinsics.checkNotNullParameter(msg, "msg");
        }
    }

    private CsgMesh() {
    }

    /* JADX INFO: compiled from: CsgMesh.kt */
    public static final class Decoded {
        private final int[] colors;
        private final int[] indices;
        private final float[] nrm;
        private final float[] pos;

        public Decoded(float[] pos, float[] nrm, int[] colors, int[] indices) {
            Intrinsics.checkNotNullParameter(pos, "pos");
            Intrinsics.checkNotNullParameter(nrm, "nrm");
            Intrinsics.checkNotNullParameter(colors, "colors");
            Intrinsics.checkNotNullParameter(indices, "indices");
            this.pos = pos;
            this.nrm = nrm;
            this.colors = colors;
            this.indices = indices;
        }

        public final int[] getColors() {
            return this.colors;
        }

        public final int[] getIndices() {
            return this.indices;
        }

        public final float[] getNrm() {
            return this.nrm;
        }

        public final float[] getPos() {
            return this.pos;
        }

        public final int getVertexCount() {
            return this.pos.length / 3;
        }
    }

    static {
        byte[] bytes = "CSGMDL".getBytes(Charsets.ISO_8859_1);
        Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
        MAGIC = bytes;
    }

    private final byte xorByte(byte[] b, int i) {
        return (byte) (b[i] ^ KEY[i % KEY.length]);
    }

    private final boolean startsWithMagic(byte[] b, boolean scrambled) {
        if (b.length < 10) {
            return false;
        }
        int length = MAGIC.length;
        for (int i = 0; i < length; i++) {
            if ((scrambled ? xorByte(b, i) : b[i]) != MAGIC[i]) {
                return false;
            }
        }
        return true;
    }

    public final boolean isCsgMesh(byte[] b) {
        Intrinsics.checkNotNullParameter(b, "b");
        return startsWithMagic(b, true) || startsWithMagic(b, false);
    }

    public final Decoded decode(byte[] raw) throws CsgException {
        boolean scrambled;
        byte[] b;
        Intrinsics.checkNotNullParameter(raw, "raw");
        if (startsWithMagic(raw, false)) {
            scrambled = false;
        } else {
            scrambled = true;
            if (!startsWithMagic(raw, true)) {
                throw new CsgException("not a union mesh");
            }
        }
        byte[] header = new byte[10];
        for (int i = 0; i < 10; i++) {
            header[i] = scrambled ? INSTANCE.xorByte(raw, i) : raw[i];
        }
        int version = ByteBuffer.wrap(header, 6, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
        switch (version) {
            case 2:
            case 3:
            case 4:
                if (scrambled) {
                    int length = raw.length;
                    b = new byte[length];
                    for (int i2 = 0; i2 < length; i2++) {
                        b[i2] = INSTANCE.xorByte(raw, i2);
                    }
                } else {
                    b = raw;
                }
                return decodeList(b, version);
            case 5:
            case 6:
                return decodePacked(raw);
            default:
                throw new CsgException("union mesh version " + version + " isn't supported");
        }
    }

    private final ByteBuffer le(byte[] b, int at) {
        ByteBuffer byteBufferOrder = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN);
        byteBufferOrder.position(at);
        return byteBufferOrder;
    }

    private final void checkIndices(int[] idx, int nv) throws CsgException {
        for (int i : idx) {
            if (i < 0 || i >= nv) {
                throw new CsgException("index out of range");
            }
        }
    }

    private final int[] firstLod(int[] all, int[] lods) {
        if (lods == null || lods.length < 2) {
            return all;
        }
        int a = RangesKt.coerceIn(lods[0], 0, all.length);
        int e = RangesKt.coerceIn(lods[1], a, all.length);
        return e - a < 3 ? all : ArraysKt.copyOfRange(all, a, e - ((e - a) % 3));
    }

    private final Decoded decodeList(byte[] b, int version) throws CsgException {
        ByteBuffer bb = le(b, 42);
        int nv = bb.getInt();
        int stride = bb.getInt();
        if (nv < 0 || stride < 40 || (((long) nv) * ((long) stride)) + 50 > b.length) {
            throw new CsgException("bad vertex data");
        }
        float[] pos = new float[nv * 3];
        float[] nrm = new float[nv * 3];
        int[] col = new int[nv];
        int i = 0;
        while (true) {
            if (i >= nv) {
                break;
            }
            int s = (i * stride) + 50;
            bb.position(s);
            pos[i * 3] = bb.getFloat();
            pos[(i * 3) + 1] = bb.getFloat();
            pos[(i * 3) + 2] = bb.getFloat();
            nrm[i * 3] = bb.getFloat();
            nrm[(i * 3) + 1] = bb.getFloat();
            nrm[(i * 3) + 2] = bb.getFloat();
            int r = b[s + 24] & 255;
            int g = b[s + 25] & 255;
            int bl = b[s + 26] & 255;
            int a = b[s + 27] & 255;
            col[i] = (a << 24) | (r << 16) | (g << 8) | bl;
            i++;
        }
        int i2 = nv * stride;
        bb.position(i2 + 50);
        if (bb.remaining() < 4) {
            throw new CsgException("missing indices");
        }
        int ni = bb.getInt();
        if (ni < 0 || ((long) ni) * 4 > bb.remaining()) {
            throw new CsgException("bad index count");
        }
        int[] idx = new int[ni];
        for (int i3 = 0; i3 < ni; i3++) {
            idx[i3] = bb.getInt();
        }
        checkIndices(idx, nv);
        int[] lods = null;
        if (version >= 4 && bb.remaining() >= 4) {
            int n = bb.getInt();
            if ((1 <= n && n < 65) && bb.remaining() >= n * 4) {
                int[] iArr = new int[n];
                for (int i4 = 0; i4 < n; i4++) {
                    iArr[i4] = bb.getInt();
                }
                lods = iArr;
            }
        }
        return new Decoded(pos, nrm, col, firstLod(idx, lods));
    }

    private final Decoded decodePacked(byte[] b) throws CsgException {
        ByteBuffer bb = le(b, 10);
        int nv = decodePacked$count(bb, -1);
        if (bb.remaining() < nv * 12) {
            throw new CsgException("bad positions");
        }
        int i = nv * 3;
        float[] pos = new float[i];
        for (int i2 = 0; i2 < i; i2++) {
            pos[i2] = bb.getFloat();
        }
        decodePacked$count(bb, nv);
        int nSize = bb.getInt();
        if (nSize != nv * 6 || bb.remaining() < nSize) {
            throw new CsgException("bad normals");
        }
        int i3 = nv * 3;
        float[] nrm = new float[i3];
        for (int i4 = 0; i4 < i3; i4++) {
            nrm[i4] = ((bb.getShort() & 65535) / 32767.0f) - 1.0f;
        }
        decodePacked$count(bb, nv);
        int[] col = new int[nv];
        for (int i5 = 0; i5 < nv; i5++) {
            int r = bb.get() & 255;
            int g = bb.get() & 255;
            int bl = bb.get() & 255;
            int a = bb.get() & 255;
            col[i5] = (a << 24) | (r << 16) | (g << 8) | bl;
        }
        decodePacked$count(bb, nv);
        bb.position(bb.position() + nv);
        decodePacked$count(bb, nv);
        bb.position(bb.position() + (nv * 8));
        decodePacked$count(bb, nv);
        int tSize = bb.getInt();
        bb.position(bb.position() + tSize);
        int ni = bb.getInt();
        int iSize = bb.getInt();
        if (ni >= 0 && iSize >= 0 && iSize <= bb.remaining()) {
            int[] idx = decodeDeltaIndices(b, bb.position(), iSize, ni);
            checkIndices(idx, nv);
            bb.position(bb.position() + iSize);
            int[] lods = null;
            if (bb.remaining() >= 4) {
                int n = bb.getInt();
                if ((1 <= n && n < 65) && bb.remaining() >= n * 4) {
                    int[] iArr = new int[n];
                    for (int i6 = 0; i6 < n; i6++) {
                        iArr[i6] = bb.getInt();
                    }
                    lods = iArr;
                }
            }
            return new Decoded(pos, nrm, col, firstLod(idx, lods));
        }
        throw new CsgException("bad index data");
    }

    private static final int decodePacked$count(ByteBuffer bb, int expect) throws CsgException {
        int n = bb.getShort() & 65535;
        if (expect < 0 || n == expect) {
            return n;
        }
        throw new CsgException("inconsistent vertex count");
    }

    public final int[] decodeDeltaIndices(byte[] b, int start, int size, int count) throws CsgException {
        int v;
        Intrinsics.checkNotNullParameter(b, "b");
        int[] out = new int[count];
        int p = start;
        int end = start + size;
        int cur = 0;
        int i = 0;
        while (i < count) {
            if (p >= end) {
                throw new CsgException("index data too short");
            }
            int p2 = p + 1;
            int x = b[p] & 255;
            if (x < 128) {
                v = x >= 64 ? x - 128 : x;
            } else {
                if (p2 + 2 > end) {
                    throw new CsgException("index data too short");
                }
                v = ((x & 127) << 16) | ((b[p2] & 255) << 8) | (b[p2 + 1] & 255);
                p2 += 2;
                if (v >= 4194304) {
                    v -= 8388608;
                }
            }
            cur += v;
            out[i] = cur;
            i++;
            p = p2;
        }
        if (p != end) {
            throw new CsgException("index data has " + (end - p) + " extra bytes");
        }
        return out;
    }
}

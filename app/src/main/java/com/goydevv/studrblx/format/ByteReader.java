package com.goydevv.studrblx.format;

import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;

/* JADX INFO: compiled from: RbxBinary.kt */
/* JADX INFO: loaded from: classes5.dex */
public final class ByteReader {
    private final byte[] buf;
    private final int end;
    private int pos;

    public ByteReader(byte[] buf, int pos, int end) {
        Intrinsics.checkNotNullParameter(buf, "buf");
        this.buf = buf;
        this.pos = pos;
        this.end = end;
    }

    public /* synthetic */ ByteReader(byte[] bArr, int i, int i2, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this(bArr, (i3 & 2) != 0 ? 0 : i, (i3 & 4) != 0 ? bArr.length : i2);
    }

    public final byte[] getBuf() {
        return this.buf;
    }

    public final int getEnd() {
        return this.end;
    }

    public final int getPos() {
        return this.pos;
    }

    public final void setPos(int i) {
        this.pos = i;
    }

    public final int remaining() {
        return this.end - this.pos;
    }

    private final void need(int n) throws RbxFormatException {
        if (this.pos + n > this.end) {
            throw new RbxFormatException("Unexpected end of chunk");
        }
    }

    public final int u8() throws RbxFormatException {
        need(1);
        byte[] bArr = this.buf;
        int i = this.pos;
        this.pos = i + 1;
        return bArr[i] & 255;
    }

    public final int u16() throws RbxFormatException {
        need(2);
        int v = (this.buf[this.pos] & 255) | ((this.buf[this.pos + 1] & 255) << 8);
        this.pos += 2;
        return v;
    }

    public final int i32() throws RbxFormatException {
        need(4);
        int v = (this.buf[this.pos] & 255) | ((this.buf[this.pos + 1] & 255) << 8) | ((this.buf[this.pos + 2] & 255) << 16) | ((this.buf[this.pos + 3] & 255) << 24);
        this.pos += 4;
        return v;
    }

    public final float f32() throws RbxFormatException {
        return Float.intBitsToFloat(i32());
    }

    public final long i64() throws RbxFormatException {
        long lo = ((long) i32()) & 4294967295L;
        long hi = i32();
        return (hi << 32) | lo;
    }

    public final double f64() throws RbxFormatException {
        return Double.longBitsToDouble(i64());
    }

    public final byte[] bytes(int n) throws RbxFormatException {
        need(n);
        byte[] r = ArraysKt.copyOfRange(this.buf, this.pos, this.pos + n);
        this.pos += n;
        return r;
    }

    public final byte[] stringBytes() throws RbxFormatException {
        int n = i32();
        if (n >= 0) {
            return bytes(n);
        }
        throw new RbxFormatException("Bad string length");
    }

    public final String string() throws RbxFormatException {
        return new String(stringBytes(), Charsets.UTF_8);
    }

    public final byte[] interleaved(int n, int size) throws RbxFormatException {
        need(n * size);
        byte[] out = new byte[n * size];
        for (int b = 0; b < size; b++) {
            for (int i = 0; i < n; i++) {
                out[(i * size) + b] = this.buf[this.pos + (b * n) + i];
            }
        }
        int b2 = this.pos;
        this.pos = b2 + (n * size);
        return out;
    }

    public final int[] u32Array(int n) throws RbxFormatException {
        byte[] d = interleaved(n, 4);
        int[] iArr = new int[n];
        for (int i = 0; i < n; i++) {
            int i2 = i * 4;
            iArr[i] = ((d[i2] & 255) << 24) | ((d[i2 + 1] & 255) << 16) | ((d[i2 + 2] & 255) << 8) | (d[i2 + 3] & 255);
        }
        return iArr;
    }

    public final int[] i32Array(int n) throws RbxFormatException {
        int[] iArrU32Array = u32Array(n);
        int length = iArrU32Array.length;
        for (int i = 0; i < length; i++) {
            int i2 = iArrU32Array[i];
            iArrU32Array[i] = (i2 >>> 1) ^ (-(i2 & 1));
        }
        return iArrU32Array;
    }

    public final float[] f32Array(int n) throws RbxFormatException {
        int[] a = u32Array(n);
        float[] fArr = new float[n];
        for (int i = 0; i < n; i++) {
            int v = a[i];
            fArr[i] = Float.intBitsToFloat((v >>> 1) | (v << 31));
        }
        return fArr;
    }

    public final int[] refArray(int n) throws RbxFormatException {
        int[] a = i32Array(n);
        for (int i = 1; i < n; i++) {
            a[i] = a[i] + a[i - 1];
        }
        return a;
    }

    public final long[] i64Array(int n) throws RbxFormatException {
        byte[] d = interleaved(n, 8);
        long[] jArr = new long[n];
        for (int i = 0; i < n; i++) {
            long v = 0;
            for (int b = 0; b < 8; b++) {
                v = (v << 8) | (((long) d[(i * 8) + b]) & 255);
            }
            jArr[i] = (v >>> 1) ^ (-(1 & v));
        }
        return jArr;
    }
}

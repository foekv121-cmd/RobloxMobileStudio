package com.goydevv.studrblx.format;

import java.io.ByteArrayOutputStream;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;

/* JADX INFO: compiled from: RbxBinary.kt */
/* JADX INFO: loaded from: classes5.dex */
public final class ByteWriter {
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();

    public final ByteArrayOutputStream getOut() {
        return this.out;
    }

    public final void u8(int v) {
        this.out.write(v & 255);
    }

    public final void u16(int v) {
        u8(v);
        u8(v >> 8);
    }

    public final void i32(int v) {
        u8(v);
        u8(v >> 8);
        u8(v >> 16);
        u8(v >> 24);
    }

    public final void f32(float v) {
        i32(Float.floatToRawIntBits(v));
    }

    public final void i64(long v) {
        i32((int) v);
        i32((int) (v >>> 32));
    }

    public final void f64(double v) {
        i64(Double.doubleToRawLongBits(v));
    }

    public final void bytes(byte[] b) {
        Intrinsics.checkNotNullParameter(b, "b");
        try { this.out.write(b); } catch (java.io.IOException _e) { throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_e); }
    }

    public final void string(byte[] b) {
        Intrinsics.checkNotNullParameter(b, "b");
        i32(b.length);
        bytes(b);
    }

    public final void string(String s) {
        Intrinsics.checkNotNullParameter(s, "s");
        byte[] bytes = s.getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
        string(bytes);
    }

    public final void interleaved(byte[] d, int n, int size) {
        Intrinsics.checkNotNullParameter(d, "d");
        byte[] o = new byte[n * size];
        for (int b = 0; b < size; b++) {
            for (int i = 0; i < n; i++) {
                o[(b * n) + i] = d[(i * size) + b];
            }
        }
        try { this.out.write(o); } catch (java.io.IOException _e) { throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_e); }
    }

    public final void u32Array(int[] a) {
        Intrinsics.checkNotNullParameter(a, "a");
        byte[] d = new byte[a.length * 4];
        int length = a.length;
        for (int i = 0; i < length; i++) {
            int v = a[i];
            d[i * 4] = (byte) (v >>> 24);
            d[(i * 4) + 1] = (byte) (v >>> 16);
            d[(i * 4) + 2] = (byte) (v >>> 8);
            d[(i * 4) + 3] = (byte) v;
        }
        int i2 = a.length;
        interleaved(d, i2, 4);
    }

    public final void i32Array(int[] a) {
        Intrinsics.checkNotNullParameter(a, "a");
        int length = a.length;
        int[] iArr = new int[length];
        for (int i = 0; i < length; i++) {
            iArr[i] = (a[i] << 1) ^ (a[i] >> 31);
        }
        u32Array(iArr);
    }

    public final void f32Array(float[] a) {
        Intrinsics.checkNotNullParameter(a, "a");
        int length = a.length;
        int[] iArr = new int[length];
        for (int i = 0; i < length; i++) {
            int b = Float.floatToRawIntBits(a[i]);
            iArr[i] = (b << 1) | (b >>> 31);
        }
        u32Array(iArr);
    }

    public final void refArray(int[] a) {
        Intrinsics.checkNotNullParameter(a, "a");
        int[] d = new int[a.length];
        int prev = 0;
        int length = a.length;
        for (int i = 0; i < length; i++) {
            d[i] = a[i] - prev;
            prev = a[i];
        }
        i32Array(d);
    }

    public final void i64Array(long[] a) {
        Intrinsics.checkNotNullParameter(a, "a");
        byte[] d = new byte[a.length * 8];
        int length = a.length;
        for (int i = 0; i < length; i++) {
            long v = (a[i] << 1) ^ (a[i] >> 63);
            for (int b = 0; b < 8; b++) {
                d[(i * 8) + b] = (byte) (v >>> (56 - (b * 8)));
            }
        }
        int i2 = a.length;
        interleaved(d, i2, 8);
    }

    public final byte[] toByteArray() {
        byte[] byteArray = this.out.toByteArray();
        Intrinsics.checkNotNullExpressionValue(byteArray, "toByteArray(...)");
        return byteArray;
    }
}

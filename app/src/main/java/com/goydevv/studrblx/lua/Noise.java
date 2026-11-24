package com.goydevv.studrblx.lua;

import java.util.List;
import java.util.Random;
import kotlin.collections.CollectionsKt;
import kotlin.ranges.RangesKt;

/* JADX INFO: compiled from: RobloxGlobals.kt */
/* JADX INFO: loaded from: classes4.dex */
public final class Noise {
    public static final Noise INSTANCE = new Noise();
    private static final int[] p;

    private Noise() {
    }

    static {
        int[] iArr = new int[512];
        List listShuffled = CollectionsKt.shuffled(RangesKt.until(0, 256), new Random(1337L));
        for (int i = 0; i < 512; i++) {
            iArr[i] = ((Number) listShuffled.get(i & 255)).intValue();
        }
        p = iArr;
    }

    private final double fade(double t) {
        return t * t * t * ((((6.0d * t) - 15.0d) * t) + 10.0d);
    }

    private final double lerp(double t, double a, double b) {
        return ((b - a) * t) + a;
    }

    private final double grad(int h, double x, double y, double z) {
        double v;
        int hh = h & 15;
        double u = hh < 8 ? x : y;
        if (hh < 4) {
            v = y;
        } else {
            v = (hh == 12 || hh == 14) ? x : z;
        }
        return ((hh & 1) == 0 ? u : -u) + ((hh & 2) == 0 ? v : -v);
    }

    public final double perlin(double x0, double y0, double z0) {
        int X = ((int) Math.floor(x0)) & 255;
        int Y = ((int) Math.floor(y0)) & 255;
        int Z = ((int) Math.floor(z0)) & 255;
        double x = x0 - Math.floor(x0);
        double y = y0 - Math.floor(y0);
        double z = z0 - Math.floor(z0);
        double u = fade(x);
        double v = fade(y);
        double w = fade(z);
        int A = p[X] + Y;
        int AA = p[A] + Z;
        int AB = p[A + 1] + Z;
        int B = p[X + 1] + Y;
        int BA = p[B] + Z;
        int BB = p[B + 1] + Z;
        return lerp(w, lerp(v, lerp(u, grad(p[AA], x, y, z), grad(p[BA], x - 1.0d, y, z)), lerp(u, grad(p[AB], x, y - 1.0d, z), grad(p[BB], x - 1.0d, y - 1.0d, z))), lerp(v, lerp(u, grad(p[AA + 1], x, y, z - 1.0d), grad(p[BA + 1], x - 1.0d, y, z - 1.0d)), lerp(u, grad(p[AB + 1], x, y - 1.0d, z - 1.0d), grad(p[BB + 1], x - 1.0d, y - 1.0d, z - 1.0d))));
    }
}

package com.goydevv.studrblx.render;

import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

/* JADX INFO: compiled from: StaticBatches.kt */
/* JADX INFO: loaded from: classes8.dex */
public final class Frustum {
    private final float[] p = new float[24];

    public final void set(float[] m) {
        Intrinsics.checkNotNullParameter(m, "m");
        int k = 0;
        for (int i = 0; i < 3; i++) {
            int s = 0;
            while (s < 2) {
                float sg = s == 0 ? 1.0f : -1.0f;
                float a = m[3] + (m[i] * sg);
                float b = m[7] + (m[i + 4] * sg);
                float c = m[11] + (m[i + 8] * sg);
                float d = m[15] + (m[i + 12] * sg);
                float l = RangesKt.coerceAtLeast((float) Math.sqrt((a * a) + (b * b) + (c * c)), 1.0E-9f);
                this.p[k] = a / l;
                this.p[k + 1] = b / l;
                this.p[k + 2] = c / l;
                this.p[k + 3] = d / l;
                k += 4;
                s++;
            }
        }
    }

    public final boolean sphere(float x, float y, float z, float r) {
        for (int k = 0; k < 24; k += 4) {
            if ((this.p[k] * x) + (this.p[k + 1] * y) + (this.p[k + 2] * z) + this.p[k + 3] < (-r)) {
                return false;
            }
        }
        return true;
    }

    public final boolean box(float x0, float y0, float z0, float x1, float y1, float z1) {
        for (int k = 0; k < 24; k += 4) {
            float a = this.p[k];
            float b = this.p[k + 1];
            float c = this.p[k + 2];
            float x = a >= 0.0f ? x1 : x0;
            float y = b >= 0.0f ? y1 : y0;
            float z = c >= 0.0f ? z1 : z0;
            if ((a * x) + (b * y) + (c * z) + this.p[k + 3] < 0.0f) {
                return false;
            }
        }
        return true;
    }
}

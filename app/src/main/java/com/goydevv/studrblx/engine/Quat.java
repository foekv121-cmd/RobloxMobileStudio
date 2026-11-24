package com.goydevv.studrblx.engine;

import com.goydevv.studrblx.model.CFrame;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

/* JADX INFO: compiled from: KeyframeAnim.kt */
/* JADX INFO: loaded from: classes7.dex */
public final class Quat {
    public static final Quat INSTANCE = new Quat();

    private Quat() {
    }

    public final float[] of(CFrame cf) {
        Intrinsics.checkNotNullParameter(cf, "cf");
        float m00 = cf.getR00();
        float m01 = cf.getR01();
        float m02 = cf.getR02();
        float m10 = cf.getR10();
        float m11 = cf.getR11();
        float m12 = cf.getR12();
        float m20 = cf.getR20();
        float m21 = cf.getR21();
        float m22 = cf.getR22();
        float tr = m00 + m11 + m22;
        float[] q = new float[4];
        if (tr > 0.0f) {
            float s = ((float) Math.sqrt(tr + 1.0f)) * 2.0f;
            q[3] = 0.25f * s;
            q[0] = (m21 - m12) / s;
            q[1] = (m02 - m20) / s;
            q[2] = (m10 - m01) / s;
        } else if (m00 > m11 && m00 > m22) {
            float s2 = ((float) Math.sqrt(((m00 + 1.0f) - m11) - m22)) * 2.0f;
            q[3] = (m21 - m12) / s2;
            q[0] = 0.25f * s2;
            q[1] = (m01 + m10) / s2;
            q[2] = (m02 + m20) / s2;
        } else if (m11 > m22) {
            float s3 = ((float) Math.sqrt(((m11 + 1.0f) - m00) - m22)) * 2.0f;
            q[3] = (m02 - m20) / s3;
            q[0] = (m01 + m10) / s3;
            q[1] = 0.25f * s3;
            q[2] = (m12 + m21) / s3;
        } else {
            float s4 = ((float) Math.sqrt(((m22 + 1.0f) - m00) - m11)) * 2.0f;
            q[3] = (m10 - m01) / s4;
            q[0] = (m02 + m20) / s4;
            q[1] = (m12 + m21) / s4;
            q[2] = 0.25f * s4;
        }
        return norm(q);
    }

    private final float[] norm(float[] q) {
        float l = RangesKt.coerceAtLeast((float) Math.sqrt((q[0] * q[0]) + (q[1] * q[1]) + (q[2] * q[2]) + (q[3] * q[3])), 1.0E-9f);
        for (int i = 0; i < 4; i++) {
            q[i] = q[i] / l;
        }
        return q;
    }

    public final float[] slerp(float[] a, float[] b0, float t) {
        Intrinsics.checkNotNullParameter(a, "a");
        Intrinsics.checkNotNullParameter(b0, "b0");
        float[] b = b0;
        int i = 0;
        float d = (a[0] * b[0]) + (a[1] * b[1]) + (a[2] * b[2]) + (a[3] * b[3]);
        if (d < 0.0f) {
            b = new float[]{-b[0], -b[1], -b[2], -b[3]};
            d = -d;
        }
        if (d > 0.9995f) {
            float[] fArr = new float[4];
            while (i < 4) {
                fArr[i] = a[i] + ((b[i] - a[i]) * t);
                i++;
            }
            return norm(fArr);
        }
        float th = (float) Math.acos(RangesKt.coerceIn(d, -1.0f, 1.0f));
        float s = (float) Math.sin(th);
        float wa = ((float) Math.sin((1.0f - t) * th)) / s;
        float wb = ((float) Math.sin(t * th)) / s;
        float[] fArr2 = new float[4];
        while (i < 4) {
            fArr2[i] = (a[i] * wa) + (b[i] * wb);
            i++;
        }
        return fArr2;
    }

    public final CFrame toCFrame(float[] q, float x, float y, float z) {
        Intrinsics.checkNotNullParameter(q, "q");
        float qx = q[0];
        float qy = q[1];
        float qz = q[2];
        float qw = q[3];
        return new CFrame(x, y, z, 1.0f - (((qy * qy) + (qz * qz)) * 2.0f), 2.0f * ((qx * qy) - (qz * qw)), 2.0f * ((qx * qz) + (qy * qw)), 2.0f * ((qx * qy) + (qz * qw)), 1.0f - (((qx * qx) + (qz * qz)) * 2.0f), 2.0f * ((qy * qz) - (qx * qw)), 2.0f * ((qx * qz) - (qy * qw)), 2.0f * ((qy * qz) + (qx * qw)), 1.0f - (2.0f * ((qx * qx) + (qy * qy))));
    }

    public final CFrame lerp(CFrame a, CFrame b, float t) {
        Intrinsics.checkNotNullParameter(a, "a");
        Intrinsics.checkNotNullParameter(b, "b");
        if (t <= 0.0f) {
            return a;
        }
        if (t >= 1.0f) {
            return b;
        }
        float[] q = slerp(of(a), of(b), t);
        return toCFrame(q, a.getX() + ((b.getX() - a.getX()) * t), a.getY() + ((b.getY() - a.getY()) * t), a.getZ() + ((b.getZ() - a.getZ()) * t));
    }
}

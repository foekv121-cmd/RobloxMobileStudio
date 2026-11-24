package com.goydevv.studrblx.model;

import java.util.Arrays;
import kotlin.collections.ArraysKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

/* JADX INFO: compiled from: DataTypes.kt */
/* JADX INFO: loaded from: classes4.dex */
public final class CFrame {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static final CFrame IDENTITY = new CFrame(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 4088, null);
    private final float r00;
    private final float r01;
    private final float r02;
    private final float r10;
    private final float r11;
    private final float r12;
    private final float r20;
    private final float r21;
    private final float r22;
    private final float x;
    private final float y;
    private final float z;

    public CFrame(float x, float y, float z, float r00, float r01, float r02, float r10, float r11, float r12, float r20, float r21, float r22) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.r00 = r00;
        this.r01 = r01;
        this.r02 = r02;
        this.r10 = r10;
        this.r11 = r11;
        this.r12 = r12;
        this.r20 = r20;
        this.r21 = r21;
        this.r22 = r22;
    }

    public /* synthetic */ CFrame(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(f, f2, f3, (i & 8) != 0 ? 1.0f : f4, (i & 16) != 0 ? 0.0f : f5, (i & 32) != 0 ? 0.0f : f6, (i & 64) != 0 ? 0.0f : f7, (i & 128) != 0 ? 1.0f : f8, (i & 256) != 0 ? 0.0f : f9, (i & 512) != 0 ? 0.0f : f10, (i & 1024) != 0 ? 0.0f : f11, (i & 2048) != 0 ? 1.0f : f12);
    }

    public final float getX() {
        return this.x;
    }

    public final float getY() {
        return this.y;
    }

    public final float getZ() {
        return this.z;
    }

    public final float getR00() {
        return this.r00;
    }

    public final float getR01() {
        return this.r01;
    }

    public final float getR02() {
        return this.r02;
    }

    public final float getR10() {
        return this.r10;
    }

    public final float getR11() {
        return this.r11;
    }

    public final float getR12() {
        return this.r12;
    }

    public final float getR20() {
        return this.r20;
    }

    public final float getR21() {
        return this.r21;
    }

    public final float getR22() {
        return this.r22;
    }

    public final Vector3 getPosition() {
        return new Vector3(this.x, this.y, this.z);
    }

    public final Vector3 getRightVector() {
        return new Vector3(this.r00, this.r10, this.r20);
    }

    public final Vector3 getUpVector() {
        return new Vector3(this.r01, this.r11, this.r21);
    }

    public final Vector3 getBackVector() {
        return new Vector3(this.r02, this.r12, this.r22);
    }

    public final Vector3 getLookVector() {
        return new Vector3(-this.r02, -this.r12, -this.r22);
    }

    public final Vector3 column(int i) {
        switch (i) {
            case 0:
                return getRightVector();
            case 1:
                return getUpVector();
            default:
                return getBackVector();
        }
    }

    public final CFrame times(CFrame o) {
        Intrinsics.checkNotNullParameter(o, "o");
        float n00 = (this.r00 * o.r00) + (this.r01 * o.r10) + (this.r02 * o.r20);
        float n01 = (this.r00 * o.r01) + (this.r01 * o.r11) + (this.r02 * o.r21);
        float n02 = (this.r00 * o.r02) + (this.r01 * o.r12) + (this.r02 * o.r22);
        float n10 = (this.r10 * o.r00) + (this.r11 * o.r10) + (this.r12 * o.r20);
        float n11 = (this.r10 * o.r01) + (this.r11 * o.r11) + (this.r12 * o.r21);
        float n12 = (this.r10 * o.r02) + (this.r11 * o.r12) + (this.r12 * o.r22);
        float n20 = (this.r20 * o.r00) + (this.r21 * o.r10) + (this.r22 * o.r20);
        float n21 = (this.r20 * o.r01) + (this.r21 * o.r11) + (this.r22 * o.r21);
        float n22 = (this.r20 * o.r02) + (this.r21 * o.r12) + (this.r22 * o.r22);
        Vector3 p = times(o.getPosition());
        return new CFrame(p.getX(), p.getY(), p.getZ(), n00, n01, n02, n10, n11, n12, n20, n21, n22);
    }

    public final Vector3 times(Vector3 v) {
        Intrinsics.checkNotNullParameter(v, "v");
        return vectorToWorld(v).plus(getPosition());
    }

    public final CFrame plus(Vector3 v) {
        Intrinsics.checkNotNullParameter(v, "v");
        return new CFrame(v.getX() + this.x, v.getY() + this.y, v.getZ() + this.z, this.r00, this.r01, this.r02, this.r10, this.r11, this.r12, this.r20, this.r21, this.r22);
    }

    public final CFrame minus(Vector3 v) {
        Intrinsics.checkNotNullParameter(v, "v");
        return new CFrame(this.x - v.getX(), this.y - v.getY(), this.z - v.getZ(), this.r00, this.r01, this.r02, this.r10, this.r11, this.r12, this.r20, this.r21, this.r22);
    }

    public final Vector3 vectorToWorld(Vector3 v) {
        Intrinsics.checkNotNullParameter(v, "v");
        return new Vector3((this.r00 * v.getX()) + (this.r01 * v.getY()) + (this.r02 * v.getZ()), (this.r10 * v.getX()) + (this.r11 * v.getY()) + (this.r12 * v.getZ()), (this.r20 * v.getX()) + (this.r21 * v.getY()) + (this.r22 * v.getZ()));
    }

    public final Vector3 vectorToObject(Vector3 v) {
        Intrinsics.checkNotNullParameter(v, "v");
        return new Vector3((this.r00 * v.getX()) + (this.r10 * v.getY()) + (this.r20 * v.getZ()), (this.r01 * v.getX()) + (this.r11 * v.getY()) + (this.r21 * v.getZ()), (this.r02 * v.getX()) + (this.r12 * v.getY()) + (this.r22 * v.getZ()));
    }

    public final Vector3 pointToObject(Vector3 v) {
        Intrinsics.checkNotNullParameter(v, "v");
        return vectorToObject(v.minus(getPosition()));
    }

    public final Vector3 pointToWorld(Vector3 v) {
        Intrinsics.checkNotNullParameter(v, "v");
        return times(v);
    }

    public final CFrame inverse() {
        Vector3 p = vectorToObject(getPosition().unaryMinus());
        return new CFrame(p.getX(), p.getY(), p.getZ(), this.r00, this.r10, this.r20, this.r01, this.r11, this.r21, this.r02, this.r12, this.r22);
    }

    public final CFrame rotationOnly() {
        return new CFrame(0.0f, 0.0f, 0.0f, this.r00, this.r01, this.r02, this.r10, this.r11, this.r12, this.r20, this.r21, this.r22);
    }

    public final CFrame withPosition(Vector3 p) {
        Intrinsics.checkNotNullParameter(p, "p");
        return new CFrame(p.getX(), p.getY(), p.getZ(), this.r00, this.r01, this.r02, this.r10, this.r11, this.r12, this.r20, this.r21, this.r22);
    }

    public final Vector3 toEulerXYZ() {
        float rx;
        float rz;
        float ry = (float) Math.asin(RangesKt.coerceIn(this.r02, -1.0f, 1.0f));
        if (Math.abs(this.r02) < 0.99999f) {
            rx = (float) Math.atan2(-this.r12, this.r22);
            rz = (float) Math.atan2(-this.r01, this.r00);
        } else {
            rx = (float) Math.atan2(this.r21, this.r11);
            rz = 0.0f;
        }
        return new Vector3(rx, ry, rz);
    }

    public final Vector3 toOrientation() {
        float ry;
        float rz;
        float rx = (float) Math.asin(RangesKt.coerceIn(-this.r12, -1.0f, 1.0f));
        if (Math.abs(this.r12) < 0.99999f) {
            ry = (float) Math.atan2(this.r02, this.r22);
            rz = (float) Math.atan2(this.r10, this.r11);
        } else {
            ry = (float) Math.atan2(-this.r20, this.r00);
            rz = 0.0f;
        }
        return new Vector3(rx, ry, rz);
    }

    public final float[] toQuaternion() {
        float trace = this.r00 + this.r11 + this.r22;
        float[] q = new float[4];
        if (trace > 0.0f) {
            float s = ((float) Math.sqrt(1.0f + trace)) * 2.0f;
            q[3] = 0.25f * s;
            q[0] = (this.r21 - this.r12) / s;
            q[1] = (this.r02 - this.r20) / s;
            q[2] = (this.r10 - this.r01) / s;
        } else if (this.r00 > this.r11 && this.r00 > this.r22) {
            float s2 = ((float) Math.sqrt(((this.r00 + 1.0f) - this.r11) - this.r22)) * 2.0f;
            q[3] = (this.r21 - this.r12) / s2;
            q[0] = 0.25f * s2;
            q[1] = (this.r01 + this.r10) / s2;
            q[2] = (this.r02 + this.r20) / s2;
        } else if (this.r11 > this.r22) {
            float s3 = ((float) Math.sqrt(((this.r11 + 1.0f) - this.r00) - this.r22)) * 2.0f;
            q[3] = (this.r02 - this.r20) / s3;
            q[0] = (this.r01 + this.r10) / s3;
            q[1] = 0.25f * s3;
            q[2] = (this.r12 + this.r21) / s3;
        } else {
            float s4 = ((float) Math.sqrt(((this.r22 + 1.0f) - this.r00) - this.r11)) * 2.0f;
            q[3] = (this.r10 - this.r01) / s4;
            q[0] = (this.r02 + this.r20) / s4;
            q[1] = (this.r12 + this.r21) / s4;
            q[2] = 0.25f * s4;
        }
        return q;
    }

    public final CFrame lerp(CFrame o, float t) {
        Intrinsics.checkNotNullParameter(o, "o");
        float[] a = toQuaternion();
        float[] b = o.toQuaternion();
        float d = (a[0] * b[0]) + (a[1] * b[1]) + (a[2] * b[2]) + (a[3] * b[3]);
        if (d < 0.0f) {
            for (int i = 0; i < 4; i++) {
                b[i] = -b[i];
            }
            d = -d;
        }
        float[] q = new float[4];
        if (d > 0.9995f) {
            for (int i2 = 0; i2 < 4; i2++) {
                q[i2] = a[i2] + ((b[i2] - a[i2]) * t);
            }
        } else {
            float th = (float) Math.acos(RangesKt.coerceIn(d, -1.0f, 1.0f));
            float s = (float) Math.sin(th);
            float wa = ((float) Math.sin((1.0f - t) * th)) / s;
            float wb = ((float) Math.sin(t * th)) / s;
            for (int i3 = 0; i3 < 4; i3++) {
                q[i3] = (a[i3] * wa) + (b[i3] * wb);
            }
        }
        Vector3 p = getPosition().lerp(o.getPosition(), t);
        return INSTANCE.fromQuaternion(p, q[0], q[1], q[2], q[3]);
    }

    public final CFrame orthonormalize() {
        Vector3 r = getRightVector().getUnit();
        Vector3 u = getUpVector();
        Vector3 u2 = u.minus(r.times(r.dot(u))).getUnit();
        Vector3 b = r.cross(u2);
        return INSTANCE.fromColumns(getPosition(), r, u2, b);
    }

    public final float[] components() {
        return new float[]{this.x, this.y, this.z, this.r00, this.r01, this.r02, this.r10, this.r11, this.r12, this.r20, this.r21, this.r22};
    }

    public boolean equals(Object other) {
        return (other instanceof CFrame) && Arrays.equals(components(), ((CFrame) other).components());
    }

    public int hashCode() {
        return Arrays.hashCode(components());
    }

    public String toString() {
        return ArraysKt.joinToString(components(), (CharSequence) ", ", "", "", -1, "...", new Function1() { // from class: com.goydevv.studrblx.model.CFrame$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return DataTypesKt.fmtNum(((Float) obj).floatValue());
            }
        });
    }

    /* JADX INFO: compiled from: DataTypes.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final CFrame getIDENTITY() {
            return CFrame.IDENTITY;
        }

        public final CFrame at(Vector3 p) {
            Intrinsics.checkNotNullParameter(p, "p");
            return new CFrame(p.getX(), p.getY(), p.getZ(), 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 4088, null);
        }

        public final CFrame fromColumns(Vector3 p, Vector3 r, Vector3 u, Vector3 b) {
            Intrinsics.checkNotNullParameter(p, "p");
            Intrinsics.checkNotNullParameter(r, "r");
            Intrinsics.checkNotNullParameter(u, "u");
            Intrinsics.checkNotNullParameter(b, "b");
            return new CFrame(p.getX(), p.getY(), p.getZ(), r.getX(), u.getX(), b.getX(), r.getY(), u.getY(), b.getY(), r.getZ(), u.getZ(), b.getZ());
        }

        public final CFrame rx(float a) {
            float c = (float) Math.cos(a);
            float s = (float) Math.sin(a);
            return new CFrame(0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, c, -s, 0.0f, s, c);
        }

        public final CFrame ry(float a) {
            float c = (float) Math.cos(a);
            float s = (float) Math.sin(a);
            return new CFrame(0.0f, 0.0f, 0.0f, c, 0.0f, s, 0.0f, 1.0f, 0.0f, -s, 0.0f, c);
        }

        public final CFrame rz(float a) {
            float c = (float) Math.cos(a);
            float s = (float) Math.sin(a);
            return new CFrame(0.0f, 0.0f, 0.0f, c, -s, 0.0f, s, c, 0.0f, 0.0f, 0.0f, 1.0f);
        }

        public final CFrame angles(float x, float y, float z) {
            return rx(x).times(ry(y)).times(rz(z));
        }

        public final CFrame fromOrientation(float x, float y, float z) {
            return ry(y).times(rx(x)).times(rz(z));
        }

        public final CFrame fromAxisAngle(Vector3 axis, float angle) {
            Intrinsics.checkNotNullParameter(axis, "axis");
            Vector3 a = axis.getUnit();
            float h = angle / 2.0f;
            float s = (float) Math.sin(h);
            return fromQuaternion(Vector3.INSTANCE.getZERO(), a.getX() * s, a.getY() * s, a.getZ() * s, (float) Math.cos(h));
        }

        public final CFrame fromQuaternion(Vector3 p, float qx, float qy, float qz, float qw) {
            Intrinsics.checkNotNullParameter(p, "p");
            float n = (float) Math.sqrt((qx * qx) + (qy * qy) + (qz * qz) + (qw * qw));
            if (n < 1.0E-9f) {
                n = 1.0f;
            }
            float x = qx / n;
            float y = qy / n;
            float z = qz / n;
            float w = qw / n;
            return new CFrame(p.getX(), p.getY(), p.getZ(), 1.0f - (((y * y) + (z * z)) * 2.0f), ((x * y) - (z * w)) * 2.0f, ((x * z) + (y * w)) * 2.0f, ((x * y) + (z * w)) * 2.0f, 1.0f - (((x * x) + (z * z)) * 2.0f), ((y * z) - (x * w)) * 2.0f, ((x * z) - (y * w)) * 2.0f, ((y * z) + (x * w)) * 2.0f, 1.0f - (2.0f * ((x * x) + (y * y))));
        }

        public static /* synthetic */ CFrame lookAt$default(Companion companion, Vector3 vector3, Vector3 vector4, Vector3 vector5, int i, Object obj) {
            if ((i & 4) != 0) {
                vector5 = Vector3.INSTANCE.getY();
            }
            return companion.lookAt(vector3, vector4, vector5);
        }

        public final CFrame lookAt(Vector3 eye, Vector3 target, Vector3 up) {
            Intrinsics.checkNotNullParameter(eye, "eye");
            Intrinsics.checkNotNullParameter(target, "target");
            Intrinsics.checkNotNullParameter(up, "up");
            Vector3 look = target.minus(eye).getUnit();
            if (look.getMagnitude() < 1.0E-6f) {
                return at(eye);
            }
            Vector3 back = look.unaryMinus();
            Vector3 right = up.cross(back).getUnit();
            if (right.getMagnitude() < 1.0E-6f) {
                right = Vector3.INSTANCE.getX();
            }
            Vector3 u = back.cross(right).getUnit();
            return fromColumns(eye, right, u, back);
        }
    }
}

package com.goydevv.studrblx.model;

import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: DataTypes.kt */
/* JADX INFO: loaded from: classes4.dex */
public final /* data */ class Vector3 {
    private final float x;
    private final float y;
    private final float z;

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static final Vector3 ZERO = new Vector3(0.0f, 0.0f, 0.0f);
    private static final Vector3 ONE = new Vector3(1.0f, 1.0f, 1.0f);
    private static final Vector3 X = new Vector3(1.0f, 0.0f, 0.0f);
    private static final Vector3 Y = new Vector3(0.0f, 1.0f, 0.0f);
    private static final Vector3 Z = new Vector3(0.0f, 0.0f, 1.0f);

    public static /* synthetic */ Vector3 copy$default(Vector3 vector3, float f, float f2, float f3, int i, Object obj) {
        if ((i & 1) != 0) {
            f = vector3.x;
        }
        if ((i & 2) != 0) {
            f2 = vector3.y;
        }
        if ((i & 4) != 0) {
            f3 = vector3.z;
        }
        return vector3.copy(f, f2, f3);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final float component1() {
        return this.x;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final float component2() {
        return this.y;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final float component3() {
        return this.z;
    }

    public final Vector3 copy(float x, float y, float z) {
        return new Vector3(x, y, z);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Vector3)) {
            return false;
        }
        Vector3 vector3 = (Vector3) other;
        return Float.compare(this.x, vector3.x) == 0 && Float.compare(this.y, vector3.y) == 0 && Float.compare(this.z, vector3.z) == 0;
    }

    public int hashCode() {
        return (((Float.hashCode(this.x) * 31) + Float.hashCode(this.y)) * 31) + Float.hashCode(this.z);
    }

    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
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

    public final Vector3 plus(Vector3 o) {
        Intrinsics.checkNotNullParameter(o, "o");
        return new Vector3(this.x + o.x, this.y + o.y, this.z + o.z);
    }

    public final Vector3 minus(Vector3 o) {
        Intrinsics.checkNotNullParameter(o, "o");
        return new Vector3(this.x - o.x, this.y - o.y, this.z - o.z);
    }

    public final Vector3 times(float s) {
        return new Vector3(this.x * s, this.y * s, this.z * s);
    }

    public final Vector3 times(Vector3 o) {
        Intrinsics.checkNotNullParameter(o, "o");
        return new Vector3(this.x * o.x, this.y * o.y, this.z * o.z);
    }

    public final Vector3 div(float s) {
        return new Vector3(this.x / s, this.y / s, this.z / s);
    }

    public final Vector3 div(Vector3 o) {
        Intrinsics.checkNotNullParameter(o, "o");
        return new Vector3(this.x / o.x, this.y / o.y, this.z / o.z);
    }

    public final Vector3 unaryMinus() {
        return new Vector3(-this.x, -this.y, -this.z);
    }

    public final float dot(Vector3 o) {
        Intrinsics.checkNotNullParameter(o, "o");
        return (this.x * o.x) + (this.y * o.y) + (this.z * o.z);
    }

    public final Vector3 cross(Vector3 o) {
        Intrinsics.checkNotNullParameter(o, "o");
        return new Vector3((this.y * o.z) - (this.z * o.y), (this.z * o.x) - (this.x * o.z), (this.x * o.y) - (this.y * o.x));
    }

    public final float getMagnitude() {
        return (float) Math.sqrt((this.x * this.x) + (this.y * this.y) + (this.z * this.z));
    }

    public final Vector3 getUnit() {
        float m = getMagnitude();
        return m < 1.0E-9f ? ZERO : div(m);
    }

    public final Vector3 lerp(Vector3 o, float t) {
        Intrinsics.checkNotNullParameter(o, "o");
        return new Vector3(this.x + ((o.x - this.x) * t), this.y + ((o.y - this.y) * t), this.z + ((o.z - this.z) * t));
    }

    public final Vector3 abs() {
        return new Vector3(Math.abs(this.x), Math.abs(this.y), Math.abs(this.z));
    }

    public final Vector3 min(Vector3 o) {
        Intrinsics.checkNotNullParameter(o, "o");
        return new Vector3(Math.min(this.x, o.x), Math.min(this.y, o.y), Math.min(this.z, o.z));
    }

    public final Vector3 max(Vector3 o) {
        Intrinsics.checkNotNullParameter(o, "o");
        return new Vector3(Math.max(this.x, o.x), Math.max(this.y, o.y), Math.max(this.z, o.z));
    }

    public final float get(int i) {
        switch (i) {
            case 0:
                return this.x;
            case 1:
                return this.y;
            default:
                return this.z;
        }
    }

    public String toString() {
        return DataTypesKt.fmtNum(this.x) + ", " + DataTypesKt.fmtNum(this.y) + ", " + DataTypesKt.fmtNum(this.z);
    }

    /* JADX INFO: compiled from: DataTypes.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final Vector3 getZERO() {
            return Vector3.ZERO;
        }

        public final Vector3 getONE() {
            return Vector3.ONE;
        }

        public final Vector3 getX() {
            return Vector3.X;
        }

        public final Vector3 getY() {
            return Vector3.Y;
        }

        public final Vector3 getZ() {
            return Vector3.Z;
        }

        public final Vector3 axis(int i) {
            switch (i) {
                case 0:
                    return getX();
                case 1:
                    return getY();
                default:
                    return getZ();
            }
        }
    }
}

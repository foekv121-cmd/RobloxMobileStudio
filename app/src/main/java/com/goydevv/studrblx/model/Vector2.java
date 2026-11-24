package com.goydevv.studrblx.model;

import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: DataTypes.kt */
/* JADX INFO: loaded from: classes4.dex */
public final /* data */ class Vector2 {
    private final float x;
    private final float y;

    public static /* synthetic */ Vector2 copy$default(Vector2 vector2, float f, float f2, int i, Object obj) {
        if ((i & 1) != 0) {
            f = vector2.x;
        }
        if ((i & 2) != 0) {
            f2 = vector2.y;
        }
        return vector2.copy(f, f2);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final float component1() {
        return this.x;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final float component2() {
        return this.y;
    }

    public final Vector2 copy(float x, float y) {
        return new Vector2(x, y);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Vector2)) {
            return false;
        }
        Vector2 vector2 = (Vector2) other;
        return Float.compare(this.x, vector2.x) == 0 && Float.compare(this.y, vector2.y) == 0;
    }

    public int hashCode() {
        return (Float.hashCode(this.x) * 31) + Float.hashCode(this.y);
    }

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public final float getX() {
        return this.x;
    }

    public final float getY() {
        return this.y;
    }

    public final Vector2 plus(Vector2 o) {
        Intrinsics.checkNotNullParameter(o, "o");
        return new Vector2(this.x + o.x, this.y + o.y);
    }

    public final Vector2 minus(Vector2 o) {
        Intrinsics.checkNotNullParameter(o, "o");
        return new Vector2(this.x - o.x, this.y - o.y);
    }

    public final Vector2 times(float s) {
        return new Vector2(this.x * s, this.y * s);
    }

    public final Vector2 times(Vector2 o) {
        Intrinsics.checkNotNullParameter(o, "o");
        return new Vector2(this.x * o.x, this.y * o.y);
    }

    public final Vector2 div(float s) {
        return new Vector2(this.x / s, this.y / s);
    }

    public final Vector2 unaryMinus() {
        return new Vector2(-this.x, -this.y);
    }

    public final float getMagnitude() {
        return (float) Math.sqrt((this.x * this.x) + (this.y * this.y));
    }

    public final Vector2 getUnit() {
        float m = getMagnitude();
        return m < 1.0E-9f ? new Vector2(0.0f, 0.0f) : div(m);
    }

    public final float dot(Vector2 o) {
        Intrinsics.checkNotNullParameter(o, "o");
        return (this.x * o.x) + (this.y * o.y);
    }

    public final Vector2 lerp(Vector2 o, float t) {
        Intrinsics.checkNotNullParameter(o, "o");
        return new Vector2(this.x + ((o.x - this.x) * t), this.y + ((o.y - this.y) * t));
    }

    public String toString() {
        return DataTypesKt.fmtNum(this.x) + ", " + DataTypesKt.fmtNum(this.y);
    }
}

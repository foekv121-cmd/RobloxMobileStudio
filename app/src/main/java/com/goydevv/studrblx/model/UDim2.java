package com.goydevv.studrblx.model;

import kotlin.jvm.internal.Intrinsics;
import kotlin.math.MathKt;

/* JADX INFO: compiled from: DataTypes.kt */
/* JADX INFO: loaded from: classes4.dex */
public final /* data */ class UDim2 {
    private final int xo;
    private final float xs;
    private final int yo;
    private final float ys;

    public static /* synthetic */ UDim2 copy$default(UDim2 uDim2, float f, int i, float f2, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            f = uDim2.xs;
        }
        if ((i3 & 2) != 0) {
            i = uDim2.xo;
        }
        if ((i3 & 4) != 0) {
            f2 = uDim2.ys;
        }
        if ((i3 & 8) != 0) {
            i2 = uDim2.yo;
        }
        return uDim2.copy(f, i, f2, i2);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final float component1() {
        return this.xs;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final int component2() {
        return this.xo;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final float component3() {
        return this.ys;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final int component4() {
        return this.yo;
    }

    public final UDim2 copy(float xs, int xo, float ys, int yo) {
        return new UDim2(xs, xo, ys, yo);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof UDim2)) {
            return false;
        }
        UDim2 uDim2 = (UDim2) other;
        return Float.compare(this.xs, uDim2.xs) == 0 && this.xo == uDim2.xo && Float.compare(this.ys, uDim2.ys) == 0 && this.yo == uDim2.yo;
    }

    public int hashCode() {
        return (((((Float.hashCode(this.xs) * 31) + Integer.hashCode(this.xo)) * 31) + Float.hashCode(this.ys)) * 31) + Integer.hashCode(this.yo);
    }

    public UDim2(float xs, int xo, float ys, int yo) {
        this.xs = xs;
        this.xo = xo;
        this.ys = ys;
        this.yo = yo;
    }

    public final int getXo() {
        return this.xo;
    }

    public final float getXs() {
        return this.xs;
    }

    public final int getYo() {
        return this.yo;
    }

    public final float getYs() {
        return this.ys;
    }

    public String toString() {
        return "{" + DataTypesKt.fmtNum(this.xs) + ", " + this.xo + "}, {" + DataTypesKt.fmtNum(this.ys) + ", " + this.yo + "}";
    }

    public final UDim2 lerp(UDim2 o, float t) {
        Intrinsics.checkNotNullParameter(o, "o");
        return new UDim2(this.xs + ((o.xs - this.xs) * t), MathKt.roundToInt(this.xo + ((o.xo - this.xo) * t)), this.ys + ((o.ys - this.ys) * t), MathKt.roundToInt(this.yo + ((o.yo - this.yo) * t)));
    }
}

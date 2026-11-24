package com.goydevv.studrblx.model;


/* JADX INFO: compiled from: DataTypes.kt */
/* JADX INFO: loaded from: classes4.dex */
public final /* data */ class NumberRange {
    private final float max;
    private final float min;

    public static /* synthetic */ NumberRange copy$default(NumberRange numberRange, float f, float f2, int i, Object obj) {
        if ((i & 1) != 0) {
            f = numberRange.min;
        }
        if ((i & 2) != 0) {
            f2 = numberRange.max;
        }
        return numberRange.copy(f, f2);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final float component1() {
        return this.min;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final float component2() {
        return this.max;
    }

    public final NumberRange copy(float min, float max) {
        return new NumberRange(min, max);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof NumberRange)) {
            return false;
        }
        NumberRange numberRange = (NumberRange) other;
        return Float.compare(this.min, numberRange.min) == 0 && Float.compare(this.max, numberRange.max) == 0;
    }

    public int hashCode() {
        return (Float.hashCode(this.min) * 31) + Float.hashCode(this.max);
    }

    public NumberRange(float min, float max) {
        this.min = min;
        this.max = max;
    }

    public final float getMax() {
        return this.max;
    }

    public final float getMin() {
        return this.min;
    }

    public String toString() {
        return DataTypesKt.fmtNum(this.min) + " " + DataTypesKt.fmtNum(this.max);
    }
}

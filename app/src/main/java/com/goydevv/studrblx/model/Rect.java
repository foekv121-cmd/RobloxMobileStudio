package com.goydevv.studrblx.model;


/* JADX INFO: compiled from: DataTypes.kt */
/* JADX INFO: loaded from: classes4.dex */
public final /* data */ class Rect {
    private final float maxX;
    private final float maxY;
    private final float minX;
    private final float minY;

    public static /* synthetic */ Rect copy$default(Rect rect, float f, float f2, float f3, float f4, int i, Object obj) {
        if ((i & 1) != 0) {
            f = rect.minX;
        }
        if ((i & 2) != 0) {
            f2 = rect.minY;
        }
        if ((i & 4) != 0) {
            f3 = rect.maxX;
        }
        if ((i & 8) != 0) {
            f4 = rect.maxY;
        }
        return rect.copy(f, f2, f3, f4);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final float component1() {
        return this.minX;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final float component2() {
        return this.minY;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final float component3() {
        return this.maxX;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final float component4() {
        return this.maxY;
    }

    public final Rect copy(float minX, float minY, float maxX, float maxY) {
        return new Rect(minX, minY, maxX, maxY);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Rect)) {
            return false;
        }
        Rect rect = (Rect) other;
        return Float.compare(this.minX, rect.minX) == 0 && Float.compare(this.minY, rect.minY) == 0 && Float.compare(this.maxX, rect.maxX) == 0 && Float.compare(this.maxY, rect.maxY) == 0;
    }

    public int hashCode() {
        return (((((Float.hashCode(this.minX) * 31) + Float.hashCode(this.minY)) * 31) + Float.hashCode(this.maxX)) * 31) + Float.hashCode(this.maxY);
    }

    public Rect(float minX, float minY, float maxX, float maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    public final float getMaxX() {
        return this.maxX;
    }

    public final float getMaxY() {
        return this.maxY;
    }

    public final float getMinX() {
        return this.minX;
    }

    public final float getMinY() {
        return this.minY;
    }

    public String toString() {
        return DataTypesKt.fmtNum(this.minX) + ", " + DataTypesKt.fmtNum(this.minY) + ", " + DataTypesKt.fmtNum(this.maxX) + ", " + DataTypesKt.fmtNum(this.maxY);
    }
}

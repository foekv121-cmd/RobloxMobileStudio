package com.goydevv.studrblx.model;


/* JADX INFO: compiled from: DataTypes.kt */
/* JADX INFO: loaded from: classes4.dex */
public final /* data */ class UDim {
    private final int offset;
    private final float scale;

    public static /* synthetic */ UDim copy$default(UDim uDim, float f, int i, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            f = uDim.scale;
        }
        if ((i2 & 2) != 0) {
            i = uDim.offset;
        }
        return uDim.copy(f, i);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final float component1() {
        return this.scale;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final int component2() {
        return this.offset;
    }

    public final UDim copy(float scale, int offset) {
        return new UDim(scale, offset);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof UDim)) {
            return false;
        }
        UDim uDim = (UDim) other;
        return Float.compare(this.scale, uDim.scale) == 0 && this.offset == uDim.offset;
    }

    public int hashCode() {
        return (Float.hashCode(this.scale) * 31) + Integer.hashCode(this.offset);
    }

    public UDim(float scale, int offset) {
        this.scale = scale;
        this.offset = offset;
    }

    public final int getOffset() {
        return this.offset;
    }

    public final float getScale() {
        return this.scale;
    }

    public String toString() {
        return DataTypesKt.fmtNum(this.scale) + ", " + this.offset;
    }
}

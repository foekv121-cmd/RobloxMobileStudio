package com.goydevv.studrblx.model;


/* JADX INFO: compiled from: DataTypes.kt */
/* JADX INFO: loaded from: classes4.dex */
public final /* data */ class BrickColorV {
    private final int number;

    public static /* synthetic */ BrickColorV copy$default(BrickColorV brickColorV, int i, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            i = brickColorV.number;
        }
        return brickColorV.copy(i);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final int component1() {
        return this.number;
    }

    public final BrickColorV copy(int number) {
        return new BrickColorV(number);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        return (other instanceof BrickColorV) && this.number == ((BrickColorV) other).number;
    }

    public int hashCode() {
        return Integer.hashCode(this.number);
    }

    public BrickColorV(int number) {
        this.number = number;
    }

    public final int getNumber() {
        return this.number;
    }

    public final String getName() {
        return BrickColors.INSTANCE.name(this.number);
    }

    public final Color3 getColor() {
        return BrickColors.INSTANCE.color(this.number);
    }

    public String toString() {
        return getName();
    }
}

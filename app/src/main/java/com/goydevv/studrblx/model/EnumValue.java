package com.goydevv.studrblx.model;


/* JADX INFO: compiled from: DataTypes.kt */
/* JADX INFO: loaded from: classes4.dex */
public final /* data */ class EnumValue {
    private final int value;

    public static /* synthetic */ EnumValue copy$default(EnumValue enumValue, int i, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            i = enumValue.value;
        }
        return enumValue.copy(i);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final int component1() {
        return this.value;
    }

    public final EnumValue copy(int value) {
        return new EnumValue(value);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        return (other instanceof EnumValue) && this.value == ((EnumValue) other).value;
    }

    public int hashCode() {
        return Integer.hashCode(this.value);
    }

    public EnumValue(int value) {
        this.value = value;
    }

    public final int getValue() {
        return this.value;
    }

    public String toString() {
        return String.valueOf(this.value);
    }
}

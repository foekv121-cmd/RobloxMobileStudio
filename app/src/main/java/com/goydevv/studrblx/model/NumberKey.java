package com.goydevv.studrblx.model;


/* JADX INFO: compiled from: DataTypes.kt */
/* JADX INFO: loaded from: classes4.dex */
public final /* data */ class NumberKey {
    private final float envelope;
    private final float time;
    private final float value;

    public static /* synthetic */ NumberKey copy$default(NumberKey numberKey, float f, float f2, float f3, int i, Object obj) {
        if ((i & 1) != 0) {
            f = numberKey.time;
        }
        if ((i & 2) != 0) {
            f2 = numberKey.value;
        }
        if ((i & 4) != 0) {
            f3 = numberKey.envelope;
        }
        return numberKey.copy(f, f2, f3);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final float component1() {
        return this.time;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final float component2() {
        return this.value;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final float component3() {
        return this.envelope;
    }

    public final NumberKey copy(float time, float value, float envelope) {
        return new NumberKey(time, value, envelope);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof NumberKey)) {
            return false;
        }
        NumberKey numberKey = (NumberKey) other;
        return Float.compare(this.time, numberKey.time) == 0 && Float.compare(this.value, numberKey.value) == 0 && Float.compare(this.envelope, numberKey.envelope) == 0;
    }

    public int hashCode() {
        return (((Float.hashCode(this.time) * 31) + Float.hashCode(this.value)) * 31) + Float.hashCode(this.envelope);
    }

    public String toString() {
        return "NumberKey(time=" + this.time + ", value=" + this.value + ", envelope=" + this.envelope + ")";
    }

    public NumberKey(float time, float value, float envelope) {
        this.time = time;
        this.value = value;
        this.envelope = envelope;
    }

    public final float getEnvelope() {
        return this.envelope;
    }

    public final float getTime() {
        return this.time;
    }

    public final float getValue() {
        return this.value;
    }
}

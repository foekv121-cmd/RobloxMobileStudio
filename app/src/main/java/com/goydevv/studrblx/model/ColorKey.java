package com.goydevv.studrblx.model;

import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: DataTypes.kt */
/* JADX INFO: loaded from: classes4.dex */
public final /* data */ class ColorKey {
    private final Color3 color;
    private final float envelope;
    private final float time;

    public static /* synthetic */ ColorKey copy$default(ColorKey colorKey, float f, Color3 color3, float f2, int i, Object obj) {
        if ((i & 1) != 0) {
            f = colorKey.time;
        }
        if ((i & 2) != 0) {
            color3 = colorKey.color;
        }
        if ((i & 4) != 0) {
            f2 = colorKey.envelope;
        }
        return colorKey.copy(f, color3, f2);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final float component1() {
        return this.time;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final Color3 component2() {
        return this.color;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final float component3() {
        return this.envelope;
    }

    public final ColorKey copy(float time, Color3 color, float envelope) {
        Intrinsics.checkNotNullParameter(color, "color");
        return new ColorKey(time, color, envelope);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ColorKey)) {
            return false;
        }
        ColorKey colorKey = (ColorKey) other;
        return Float.compare(this.time, colorKey.time) == 0 && Intrinsics.areEqual(this.color, colorKey.color) && Float.compare(this.envelope, colorKey.envelope) == 0;
    }

    public int hashCode() {
        return (((Float.hashCode(this.time) * 31) + this.color.hashCode()) * 31) + Float.hashCode(this.envelope);
    }

    public String toString() {
        return "ColorKey(time=" + this.time + ", color=" + this.color + ", envelope=" + this.envelope + ")";
    }

    public ColorKey(float time, Color3 color, float envelope) {
        Intrinsics.checkNotNullParameter(color, "color");
        this.time = time;
        this.color = color;
        this.envelope = envelope;
    }

    public final Color3 getColor() {
        return this.color;
    }

    public final float getEnvelope() {
        return this.envelope;
    }

    public final float getTime() {
        return this.time;
    }
}

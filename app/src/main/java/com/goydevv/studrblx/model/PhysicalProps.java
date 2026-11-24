package com.goydevv.studrblx.model;

import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: compiled from: DataTypes.kt */
/* JADX INFO: loaded from: classes4.dex */
public final /* data */ class PhysicalProps {
    private final float acoustic;
    private final float density;
    private final float elasticity;
    private final float elasticityWeight;
    private final int flags;
    private final float friction;
    private final float frictionWeight;

    public static /* synthetic */ PhysicalProps copy$default(PhysicalProps physicalProps, int i, float f, float f2, float f3, float f4, float f5, float f6, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            i = physicalProps.flags;
        }
        if ((i2 & 2) != 0) {
            f = physicalProps.density;
        }
        if ((i2 & 4) != 0) {
            f2 = physicalProps.friction;
        }
        if ((i2 & 8) != 0) {
            f3 = physicalProps.elasticity;
        }
        if ((i2 & 16) != 0) {
            f4 = physicalProps.frictionWeight;
        }
        if ((i2 & 32) != 0) {
            f5 = physicalProps.elasticityWeight;
        }
        if ((i2 & 64) != 0) {
            f6 = physicalProps.acoustic;
        }
        float f7 = f5;
        float f8 = f6;
        float f9 = f4;
        float f10 = f2;
        return physicalProps.copy(i, f, f10, f3, f9, f7, f8);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final int component1() {
        return this.flags;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final float component2() {
        return this.density;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final float component3() {
        return this.friction;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final float component4() {
        return this.elasticity;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final float component5() {
        return this.frictionWeight;
    }

    /* JADX INFO: renamed from: component6, reason: from getter */
    public final float component6() {
        return this.elasticityWeight;
    }

    /* JADX INFO: renamed from: component7, reason: from getter */
    public final float component7() {
        return this.acoustic;
    }

    public final PhysicalProps copy(int flags, float density, float friction, float elasticity, float frictionWeight, float elasticityWeight, float acoustic) {
        return new PhysicalProps(flags, density, friction, elasticity, frictionWeight, elasticityWeight, acoustic);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof PhysicalProps)) {
            return false;
        }
        PhysicalProps physicalProps = (PhysicalProps) other;
        return this.flags == physicalProps.flags && Float.compare(this.density, physicalProps.density) == 0 && Float.compare(this.friction, physicalProps.friction) == 0 && Float.compare(this.elasticity, physicalProps.elasticity) == 0 && Float.compare(this.frictionWeight, physicalProps.frictionWeight) == 0 && Float.compare(this.elasticityWeight, physicalProps.elasticityWeight) == 0 && Float.compare(this.acoustic, physicalProps.acoustic) == 0;
    }

    public int hashCode() {
        return (((((((((((Integer.hashCode(this.flags) * 31) + Float.hashCode(this.density)) * 31) + Float.hashCode(this.friction)) * 31) + Float.hashCode(this.elasticity)) * 31) + Float.hashCode(this.frictionWeight)) * 31) + Float.hashCode(this.elasticityWeight)) * 31) + Float.hashCode(this.acoustic);
    }

    public PhysicalProps(int flags, float density, float friction, float elasticity, float frictionWeight, float elasticityWeight, float acoustic) {
        this.flags = flags;
        this.density = density;
        this.friction = friction;
        this.elasticity = elasticity;
        this.frictionWeight = frictionWeight;
        this.elasticityWeight = elasticityWeight;
        this.acoustic = acoustic;
    }

    public /* synthetic */ PhysicalProps(int i, float f, float f2, float f3, float f4, float f5, float f6, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(i, (i2 & 2) != 0 ? 0.7f : f, (i2 & 4) != 0 ? 0.3f : f2, (i2 & 8) != 0 ? 0.5f : f3, (i2 & 16) != 0 ? 1.0f : f4, (i2 & 32) != 0 ? 1.0f : f5, (i2 & 64) == 0 ? f6 : 1.0f);
    }

    public final int getFlags() {
        return this.flags;
    }

    public final float getDensity() {
        return this.density;
    }

    public final float getElasticity() {
        return this.elasticity;
    }

    public final float getFriction() {
        return this.friction;
    }

    public final float getAcoustic() {
        return this.acoustic;
    }

    public final float getElasticityWeight() {
        return this.elasticityWeight;
    }

    public final float getFrictionWeight() {
        return this.frictionWeight;
    }

    public final boolean getCustom() {
        return (this.flags & 1) != 0;
    }

    public String toString() {
        if (!getCustom()) {
            return "Default";
        }
        return DataTypesKt.fmtNum(this.density) + ", " + DataTypesKt.fmtNum(this.friction) + ", " + DataTypesKt.fmtNum(this.elasticity);
    }
}

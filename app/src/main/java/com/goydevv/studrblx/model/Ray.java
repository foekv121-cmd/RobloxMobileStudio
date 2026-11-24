package com.goydevv.studrblx.model;

import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: DataTypes.kt */
/* JADX INFO: loaded from: classes4.dex */
public final /* data */ class Ray {
    private final Vector3 direction;
    private final Vector3 origin;

    public static /* synthetic */ Ray copy$default(Ray ray, Vector3 vector3, Vector3 vector4, int i, Object obj) {
        if ((i & 1) != 0) {
            vector3 = ray.origin;
        }
        if ((i & 2) != 0) {
            vector4 = ray.direction;
        }
        return ray.copy(vector3, vector4);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final Vector3 component1() {
        return this.origin;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final Vector3 component2() {
        return this.direction;
    }

    public final Ray copy(Vector3 origin, Vector3 direction) {
        Intrinsics.checkNotNullParameter(origin, "origin");
        Intrinsics.checkNotNullParameter(direction, "direction");
        return new Ray(origin, direction);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Ray)) {
            return false;
        }
        Ray ray = (Ray) other;
        return Intrinsics.areEqual(this.origin, ray.origin) && Intrinsics.areEqual(this.direction, ray.direction);
    }

    public int hashCode() {
        return (this.origin.hashCode() * 31) + this.direction.hashCode();
    }

    public Ray(Vector3 origin, Vector3 direction) {
        Intrinsics.checkNotNullParameter(origin, "origin");
        Intrinsics.checkNotNullParameter(direction, "direction");
        this.origin = origin;
        this.direction = direction;
    }

    public final Vector3 getDirection() {
        return this.direction;
    }

    public final Vector3 getOrigin() {
        return this.origin;
    }

    public String toString() {
        return "{" + this.origin + "}, {" + this.direction + "}";
    }
}

package com.goydevv.studrblx.model;


/* JADX INFO: compiled from: DataTypes.kt */
/* JADX INFO: loaded from: classes4.dex */
public final /* data */ class Vector3int16 {
    private final int x;
    private final int y;
    private final int z;

    public static /* synthetic */ Vector3int16 copy$default(Vector3int16 vector3int16, int i, int i2, int i3, int i4, Object obj) {
        if ((i4 & 1) != 0) {
            i = vector3int16.x;
        }
        if ((i4 & 2) != 0) {
            i2 = vector3int16.y;
        }
        if ((i4 & 4) != 0) {
            i3 = vector3int16.z;
        }
        return vector3int16.copy(i, i2, i3);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final int component1() {
        return this.x;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final int component2() {
        return this.y;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final int component3() {
        return this.z;
    }

    public final Vector3int16 copy(int x, int y, int z) {
        return new Vector3int16(x, y, z);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Vector3int16)) {
            return false;
        }
        Vector3int16 vector3int16 = (Vector3int16) other;
        return this.x == vector3int16.x && this.y == vector3int16.y && this.z == vector3int16.z;
    }

    public int hashCode() {
        return (((Integer.hashCode(this.x) * 31) + Integer.hashCode(this.y)) * 31) + Integer.hashCode(this.z);
    }

    public Vector3int16(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public final int getX() {
        return this.x;
    }

    public final int getY() {
        return this.y;
    }

    public final int getZ() {
        return this.z;
    }

    public String toString() {
        return this.x + ", " + this.y + ", " + this.z;
    }
}

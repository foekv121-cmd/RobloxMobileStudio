package com.goydevv.studrblx.lua;

import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: LuaTypes.kt */
/* JADX INFO: loaded from: classes4.dex */
public final /* data */ class EnumItemV {
    private final String name;
    private final String type;
    private final int value;

    public static /* synthetic */ EnumItemV copy$default(EnumItemV enumItemV, String str, String str2, int i, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            str = enumItemV.type;
        }
        if ((i2 & 2) != 0) {
            str2 = enumItemV.name;
        }
        if ((i2 & 4) != 0) {
            i = enumItemV.value;
        }
        return enumItemV.copy(str, str2, i);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final String component1() {
        return this.type;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final String component2() {
        return this.name;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final int component3() {
        return this.value;
    }

    public final EnumItemV copy(String type, String name, int value) {
        Intrinsics.checkNotNullParameter(type, "type");
        Intrinsics.checkNotNullParameter(name, "name");
        return new EnumItemV(type, name, value);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof EnumItemV)) {
            return false;
        }
        EnumItemV enumItemV = (EnumItemV) other;
        return Intrinsics.areEqual(this.type, enumItemV.type) && Intrinsics.areEqual(this.name, enumItemV.name) && this.value == enumItemV.value;
    }

    public int hashCode() {
        return (((this.type.hashCode() * 31) + this.name.hashCode()) * 31) + Integer.hashCode(this.value);
    }

    public String toString() {
        return "EnumItemV(type=" + this.type + ", name=" + this.name + ", value=" + this.value + ")";
    }

    public EnumItemV(String type, String name, int value) {
        Intrinsics.checkNotNullParameter(type, "type");
        Intrinsics.checkNotNullParameter(name, "name");
        this.type = type;
        this.name = name;
        this.value = value;
    }

    public final String getName() {
        return this.name;
    }

    public final String getType() {
        return this.type;
    }

    public final int getValue() {
        return this.value;
    }
}

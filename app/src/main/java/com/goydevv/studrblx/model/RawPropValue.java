package com.goydevv.studrblx.model;

import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: DataTypes.kt */
/* JADX INFO: loaded from: classes4.dex */
public final class RawPropValue {
    private final int count;
    private final int index;
    private final byte[] payload;
    private final int type;

    public RawPropValue(int type, byte[] payload, int index, int count) {
        Intrinsics.checkNotNullParameter(payload, "payload");
        this.type = type;
        this.payload = payload;
        this.index = index;
        this.count = count;
    }

    public final int getCount() {
        return this.count;
    }

    public final int getIndex() {
        return this.index;
    }

    public final byte[] getPayload() {
        return this.payload;
    }

    public final int getType() {
        return this.type;
    }

    public String toString() {
        return "<unsupported type 0x" + Integer.toHexString(this.type) + ">";
    }
}

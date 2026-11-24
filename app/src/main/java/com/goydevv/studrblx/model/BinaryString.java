package com.goydevv.studrblx.model;

import java.util.Arrays;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: DataTypes.kt */
/* JADX INFO: loaded from: classes4.dex */
public final class BinaryString {
    private final byte[] bytes;

    public BinaryString(byte[] bytes) {
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        this.bytes = bytes;
    }

    public final byte[] getBytes() {
        return this.bytes;
    }

    public boolean equals(Object other) {
        return (other instanceof BinaryString) && Arrays.equals(this.bytes, ((BinaryString) other).bytes);
    }

    public int hashCode() {
        return Arrays.hashCode(this.bytes);
    }

    public String toString() {
        return "<binary " + this.bytes.length + " bytes>";
    }
}

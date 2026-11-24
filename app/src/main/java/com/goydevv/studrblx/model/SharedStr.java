package com.goydevv.studrblx.model;

import java.util.Arrays;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: DataTypes.kt */
/* JADX INFO: loaded from: classes4.dex */
public final class SharedStr {
    private final byte[] bytes;

    public SharedStr(byte[] bytes) {
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        this.bytes = bytes;
    }

    public final byte[] getBytes() {
        return this.bytes;
    }

    public boolean equals(Object other) {
        return (other instanceof SharedStr) && Arrays.equals(this.bytes, ((SharedStr) other).bytes);
    }

    public int hashCode() {
        return Arrays.hashCode(this.bytes);
    }

    public String toString() {
        return "<shared " + this.bytes.length + " bytes>";
    }
}

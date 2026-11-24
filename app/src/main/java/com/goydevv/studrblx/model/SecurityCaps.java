package com.goydevv.studrblx.model;


/* JADX INFO: compiled from: DataTypes.kt */
/* JADX INFO: loaded from: classes4.dex */
public final /* data */ class SecurityCaps {
    private final long value;

    public static /* synthetic */ SecurityCaps copy$default(SecurityCaps securityCaps, long j, int i, Object obj) {
        if ((i & 1) != 0) {
            j = securityCaps.value;
        }
        return securityCaps.copy(j);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final long component1() {
        return this.value;
    }

    public final SecurityCaps copy(long value) {
        return new SecurityCaps(value);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        return (other instanceof SecurityCaps) && this.value == ((SecurityCaps) other).value;
    }

    public int hashCode() {
        return Long.hashCode(this.value);
    }

    public String toString() {
        return "SecurityCaps(value=" + this.value + ")";
    }

    public SecurityCaps(long value) {
        this.value = value;
    }

    public final long getValue() {
        return this.value;
    }
}

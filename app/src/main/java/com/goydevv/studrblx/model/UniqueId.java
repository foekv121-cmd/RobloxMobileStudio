package com.goydevv.studrblx.model;

import java.util.Arrays;
import kotlin.collections.ArraysKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.StringCompanionObject;

/* JADX INFO: compiled from: DataTypes.kt */
/* JADX INFO: loaded from: classes4.dex */
public final class UniqueId {
    private final byte[] bytes;

    public UniqueId(byte[] bytes) {
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        this.bytes = bytes;
    }

    public final byte[] getBytes() {
        return this.bytes;
    }

    public boolean equals(Object other) {
        return (other instanceof UniqueId) && Arrays.equals(this.bytes, ((UniqueId) other).bytes);
    }

    public int hashCode() {
        return Arrays.hashCode(this.bytes);
    }

    static final CharSequence toString$lambda$0(byte it) {
        StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
        String str = String.format("%02x", Arrays.copyOf(new Object[]{Byte.valueOf(it)}, 1));
        Intrinsics.checkNotNullExpressionValue(str, "format(...)");
        return str;
    }

    public String toString() {
        return ArraysKt.joinToString(this.bytes, (CharSequence) "", "", "", -1, "...", new Function1() { // from class: com.goydevv.studrblx.model.UniqueId$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return UniqueId.toString$lambda$0(((Byte) obj).byteValue());
            }
        });
    }
}

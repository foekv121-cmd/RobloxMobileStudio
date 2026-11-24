package com.goydevv.studrblx.model;

import java.util.List;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: DataTypes.kt */
/* JADX INFO: loaded from: classes4.dex */
public final /* data */ class NumberSequence {
    private final List<NumberKey> keys;

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ NumberSequence copy$default(NumberSequence numberSequence, List list, int i, Object obj) {
        if ((i & 1) != 0) {
            list = numberSequence.keys;
        }
        return numberSequence.copy(list);
    }

    public final List<NumberKey> component1() {
        return this.keys;
    }

    public final NumberSequence copy(List<NumberKey> keys) {
        Intrinsics.checkNotNullParameter(keys, "keys");
        return new NumberSequence(keys);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        return (other instanceof NumberSequence) && Intrinsics.areEqual(this.keys, ((NumberSequence) other).keys);
    }

    public int hashCode() {
        return this.keys.hashCode();
    }

    public NumberSequence(List<NumberKey> keys) {
        Intrinsics.checkNotNullParameter(keys, "keys");
        this.keys = keys;
    }

    public final List<NumberKey> getKeys() {
        return this.keys;
    }

    static final CharSequence toString$lambda$0(NumberKey it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return DataTypesKt.fmtNum(it.getTime()) + " " + DataTypesKt.fmtNum(it.getValue()) + " " + DataTypesKt.fmtNum(it.getEnvelope());
    }

    public String toString() {
        return CollectionsKt.joinToString(this.keys, " ", "", "", -1, "...", new Function1() { // from class: com.goydevv.studrblx.model.NumberSequence$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return NumberSequence.toString$lambda$0((NumberKey) obj);
            }
        });
    }
}

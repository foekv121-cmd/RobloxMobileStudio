package com.goydevv.studrblx.model;

import java.util.List;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: DataTypes.kt */
/* JADX INFO: loaded from: classes4.dex */
public final /* data */ class ColorSequence {
    private final List<ColorKey> keys;

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ ColorSequence copy$default(ColorSequence colorSequence, List list, int i, Object obj) {
        if ((i & 1) != 0) {
            list = colorSequence.keys;
        }
        return colorSequence.copy(list);
    }

    public final List<ColorKey> component1() {
        return this.keys;
    }

    public final ColorSequence copy(List<ColorKey> keys) {
        Intrinsics.checkNotNullParameter(keys, "keys");
        return new ColorSequence(keys);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        return (other instanceof ColorSequence) && Intrinsics.areEqual(this.keys, ((ColorSequence) other).keys);
    }

    public int hashCode() {
        return this.keys.hashCode();
    }

    public ColorSequence(List<ColorKey> keys) {
        Intrinsics.checkNotNullParameter(keys, "keys");
        this.keys = keys;
    }

    public final List<ColorKey> getKeys() {
        return this.keys;
    }

    static final CharSequence toString$lambda$0(ColorKey it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return DataTypesKt.fmtNum(it.getTime()) + " (" + it.getColor() + ")";
    }

    public String toString() {
        return CollectionsKt.joinToString(this.keys, " ", "", "", -1, "...", new Function1() { // from class: com.goydevv.studrblx.model.ColorSequence$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return ColorSequence.toString$lambda$0((ColorKey) obj);
            }
        });
    }
}

package com.goydevv.studrblx.model;

import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: DataTypes.kt */
/* JADX INFO: loaded from: classes4.dex */
public final /* data */ class FontValue {
    private final String cachedFaceId;
    private final String family;
    private final int style;
    private final int weight;

    public static /* synthetic */ FontValue copy$default(FontValue fontValue, String str, int i, int i2, String str2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            str = fontValue.family;
        }
        if ((i3 & 2) != 0) {
            i = fontValue.weight;
        }
        if ((i3 & 4) != 0) {
            i2 = fontValue.style;
        }
        if ((i3 & 8) != 0) {
            str2 = fontValue.cachedFaceId;
        }
        return fontValue.copy(str, i, i2, str2);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final String component1() {
        return this.family;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final int component2() {
        return this.weight;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final int component3() {
        return this.style;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final String component4() {
        return this.cachedFaceId;
    }

    public final FontValue copy(String family, int weight, int style, String cachedFaceId) {
        Intrinsics.checkNotNullParameter(family, "family");
        Intrinsics.checkNotNullParameter(cachedFaceId, "cachedFaceId");
        return new FontValue(family, weight, style, cachedFaceId);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof FontValue)) {
            return false;
        }
        FontValue fontValue = (FontValue) other;
        return Intrinsics.areEqual(this.family, fontValue.family) && this.weight == fontValue.weight && this.style == fontValue.style && Intrinsics.areEqual(this.cachedFaceId, fontValue.cachedFaceId);
    }

    public int hashCode() {
        return (((((this.family.hashCode() * 31) + Integer.hashCode(this.weight)) * 31) + Integer.hashCode(this.style)) * 31) + this.cachedFaceId.hashCode();
    }

    public FontValue(String family, int weight, int style, String cachedFaceId) {
        Intrinsics.checkNotNullParameter(family, "family");
        Intrinsics.checkNotNullParameter(cachedFaceId, "cachedFaceId");
        this.family = family;
        this.weight = weight;
        this.style = style;
        this.cachedFaceId = cachedFaceId;
    }

    public final String getCachedFaceId() {
        return this.cachedFaceId;
    }

    public final String getFamily() {
        return this.family;
    }

    public final int getStyle() {
        return this.style;
    }

    public final int getWeight() {
        return this.weight;
    }

    public String toString() {
        return com.goydevv.studrblx.util.KtCompat.substringBefore(com.goydevv.studrblx.util.KtCompat.substringAfterLast(this.family, '/'), '.') + " (" + this.weight + ")";
    }
}

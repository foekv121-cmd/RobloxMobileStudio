package com.goydevv.studrblx.model;

import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: DataTypes.kt */
/* JADX INFO: loaded from: classes4.dex */
public final /* data */ class ContentValue {
    private final Instance obj;
    private final int sourceType;
    private final String uri;

    public static /* synthetic */ ContentValue copy$default(ContentValue contentValue, int i, String str, Instance instance, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            i = contentValue.sourceType;
        }
        if ((i2 & 2) != 0) {
            str = contentValue.uri;
        }
        if ((i2 & 4) != 0) {
            instance = contentValue.obj;
        }
        return contentValue.copy(i, str, instance);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final int component1() {
        return this.sourceType;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final String component2() {
        return this.uri;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final Instance component3() {
        return this.obj;
    }

    public final ContentValue copy(int sourceType, String uri, Instance obj) {
        Intrinsics.checkNotNullParameter(uri, "uri");
        return new ContentValue(sourceType, uri, obj);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ContentValue)) {
            return false;
        }
        ContentValue contentValue = (ContentValue) other;
        return this.sourceType == contentValue.sourceType && Intrinsics.areEqual(this.uri, contentValue.uri) && Intrinsics.areEqual(this.obj, contentValue.obj);
    }

    public int hashCode() {
        return (((Integer.hashCode(this.sourceType) * 31) + this.uri.hashCode()) * 31) + (this.obj == null ? 0 : this.obj.hashCode());
    }

    public ContentValue(int sourceType, String uri, Instance obj) {
        Intrinsics.checkNotNullParameter(uri, "uri");
        this.sourceType = sourceType;
        this.uri = uri;
        this.obj = obj;
    }

    public /* synthetic */ ContentValue(int i, String str, Instance instance, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(i, (i2 & 2) != 0 ? "" : str, (i2 & 4) != 0 ? null : instance);
    }

    public final Instance getObj() {
        return this.obj;
    }

    public final int getSourceType() {
        return this.sourceType;
    }

    public final String getUri() {
        return this.uri;
    }

    public String toString() {
        switch (this.sourceType) {
            case 1:
                return this.uri;
            case 2:
                return "Object";
            default:
                return "";
        }
    }
}

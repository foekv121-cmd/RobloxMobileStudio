package com.goydevv.studrblx.net;

import java.io.IOException;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: RobloxCloud.kt */
/* JADX INFO: loaded from: classes6.dex */
public final class PublishException extends IOException {
    private final boolean needsApiKey;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public PublishException(String msg, boolean needsApiKey) {
        super(msg);
        Intrinsics.checkNotNullParameter(msg, "msg");
        this.needsApiKey = needsApiKey;
    }

    public final boolean getNeedsApiKey() {
        return this.needsApiKey;
    }
}

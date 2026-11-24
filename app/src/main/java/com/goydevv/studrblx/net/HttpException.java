package com.goydevv.studrblx.net;

import java.io.IOException;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: RobloxApi.kt */
/* JADX INFO: loaded from: classes6.dex */
public final class HttpException extends IOException {
    private final int code;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public HttpException(int code, String msg) {
        super(msg);
        Intrinsics.checkNotNullParameter(msg, "msg");
        this.code = code;
    }

    public final int getCode() {
        return this.code;
    }
}

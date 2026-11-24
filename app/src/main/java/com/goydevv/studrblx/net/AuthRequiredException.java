package com.goydevv.studrblx.net;

import java.io.IOException;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: RobloxApi.kt */
/* JADX INFO: loaded from: classes6.dex */
public final class AuthRequiredException extends RuntimeException {
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AuthRequiredException(String msg) {
        super(msg);
        Intrinsics.checkNotNullParameter(msg, "msg");
    }
}

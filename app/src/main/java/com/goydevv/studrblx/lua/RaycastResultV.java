package com.goydevv.studrblx.lua;

import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: LuaTypes.kt */
/* JADX INFO: loaded from: classes4.dex */
public final class RaycastResultV {
    private final RayHitInfo hit;

    public RaycastResultV(RayHitInfo hit) {
        Intrinsics.checkNotNullParameter(hit, "hit");
        this.hit = hit;
    }

    public final RayHitInfo getHit() {
        return this.hit;
    }
}

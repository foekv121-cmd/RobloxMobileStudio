package com.goydevv.studrblx.engine;

import com.goydevv.studrblx.model.Instance;
import com.goydevv.studrblx.model.Vector3;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Geometry.kt */
/* JADX INFO: loaded from: classes7.dex */
public final class RayHit {
    private final Vector3 normal;
    private final Instance part;
    private final Vector3 position;
    private final float t;

    public RayHit(Instance part, float t, Vector3 position, Vector3 normal) {
        Intrinsics.checkNotNullParameter(part, "part");
        Intrinsics.checkNotNullParameter(position, "position");
        Intrinsics.checkNotNullParameter(normal, "normal");
        this.part = part;
        this.t = t;
        this.position = position;
        this.normal = normal;
    }

    public final Vector3 getNormal() {
        return this.normal;
    }

    public final Instance getPart() {
        return this.part;
    }

    public final Vector3 getPosition() {
        return this.position;
    }

    public final float getT() {
        return this.t;
    }
}

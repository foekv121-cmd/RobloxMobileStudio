package com.goydevv.studrblx.render;

import com.goydevv.studrblx.model.CFrame;
import com.goydevv.studrblx.model.Vector3;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

/* JADX INFO: compiled from: Camera.kt */
/* JADX INFO: loaded from: classes8.dex */
public final class FlyCamera {
    private float yaw;
    private Vector3 pos = new Vector3(0.0f, 20.0f, 40.0f);
    private float pitch = -0.4f;
    private float fov = 70.0f;

    public final Vector3 getPos() {
        return this.pos;
    }

    public final void setPos(Vector3 vector3) {
        Intrinsics.checkNotNullParameter(vector3, "<set-?>");
        this.pos = vector3;
    }

    public final float getYaw() {
        return this.yaw;
    }

    public final void setYaw(float f) {
        this.yaw = f;
    }

    public final float getPitch() {
        return this.pitch;
    }

    public final void setPitch(float f) {
        this.pitch = f;
    }

    public final float getFov() {
        return this.fov;
    }

    public final void setFov(float f) {
        this.fov = f;
    }

    public final Vector3 getForward() {
        return new Vector3((-((float) Math.sin(this.yaw))) * ((float) Math.cos(this.pitch)), (float) Math.sin(this.pitch), (-((float) Math.cos(this.yaw))) * ((float) Math.cos(this.pitch)));
    }

    public final Vector3 getRight() {
        return new Vector3((float) Math.cos(this.yaw), 0.0f, -((float) Math.sin(this.yaw)));
    }

    public final Vector3 getUp() {
        return getRight().cross(getForward()).getUnit();
    }

    public final CFrame cframe() {
        return CFrame.Companion.lookAt$default(CFrame.INSTANCE, this.pos, this.pos.plus(getForward()), null, 4, null);
    }

    public final void lookAt(Vector3 target) {
        Intrinsics.checkNotNullParameter(target, "target");
        Vector3 d = target.minus(this.pos).getUnit();
        this.pitch = (float) Math.asin(RangesKt.coerceIn(d.getY(), -1.0f, 1.0f));
        this.yaw = (float) Math.atan2(-d.getX(), -d.getZ());
    }

    public final void setFromCFrame(CFrame cf) {
        Intrinsics.checkNotNullParameter(cf, "cf");
        this.pos = cf.getPosition();
        lookAt(cf.getPosition().plus(cf.getLookVector()));
    }

    public final void rotate(float dYaw, float dPitch) {
        this.yaw += dYaw;
        this.pitch = RangesKt.coerceIn(this.pitch + dPitch, -1.55f, 1.55f);
    }

    public final void focusOn(Vector3 center, float radius) {
        Intrinsics.checkNotNullParameter(center, "center");
        float dist = RangesKt.coerceAtLeast(2.2f * radius, 6.0f);
        this.pos = center.minus(getForward().times(dist));
    }
}

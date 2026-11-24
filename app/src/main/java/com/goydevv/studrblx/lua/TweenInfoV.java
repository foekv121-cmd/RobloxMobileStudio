package com.goydevv.studrblx.lua;


/* JADX INFO: compiled from: LuaTypes.kt */
/* JADX INFO: loaded from: classes4.dex */
public final /* data */ class TweenInfoV {
    private final float delay;
    private final int direction;
    private final int repeatCount;
    private final boolean reverses;
    private final int style;
    private final float time;

    public static /* synthetic */ TweenInfoV copy$default(TweenInfoV tweenInfoV, float f, int i, int i2, int i3, boolean z, float f2, int i4, Object obj) {
        if ((i4 & 1) != 0) {
            f = tweenInfoV.time;
        }
        if ((i4 & 2) != 0) {
            i = tweenInfoV.style;
        }
        if ((i4 & 4) != 0) {
            i2 = tweenInfoV.direction;
        }
        if ((i4 & 8) != 0) {
            i3 = tweenInfoV.repeatCount;
        }
        if ((i4 & 16) != 0) {
            z = tweenInfoV.reverses;
        }
        if ((i4 & 32) != 0) {
            f2 = tweenInfoV.delay;
        }
        boolean z2 = z;
        float f3 = f2;
        return tweenInfoV.copy(f, i, i2, i3, z2, f3);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final float component1() {
        return this.time;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final int component2() {
        return this.style;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final int component3() {
        return this.direction;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final int component4() {
        return this.repeatCount;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final boolean component5() {
        return this.reverses;
    }

    /* JADX INFO: renamed from: component6, reason: from getter */
    public final float component6() {
        return this.delay;
    }

    public final TweenInfoV copy(float time, int style, int direction, int repeatCount, boolean reverses, float delay) {
        return new TweenInfoV(time, style, direction, repeatCount, reverses, delay);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof TweenInfoV)) {
            return false;
        }
        TweenInfoV tweenInfoV = (TweenInfoV) other;
        return Float.compare(this.time, tweenInfoV.time) == 0 && this.style == tweenInfoV.style && this.direction == tweenInfoV.direction && this.repeatCount == tweenInfoV.repeatCount && this.reverses == tweenInfoV.reverses && Float.compare(this.delay, tweenInfoV.delay) == 0;
    }

    public int hashCode() {
        return (((((((((Float.hashCode(this.time) * 31) + Integer.hashCode(this.style)) * 31) + Integer.hashCode(this.direction)) * 31) + Integer.hashCode(this.repeatCount)) * 31) + Boolean.hashCode(this.reverses)) * 31) + Float.hashCode(this.delay);
    }

    public String toString() {
        return "TweenInfoV(time=" + this.time + ", style=" + this.style + ", direction=" + this.direction + ", repeatCount=" + this.repeatCount + ", reverses=" + this.reverses + ", delay=" + this.delay + ")";
    }

    public TweenInfoV(float time, int style, int direction, int repeatCount, boolean reverses, float delay) {
        this.time = time;
        this.style = style;
        this.direction = direction;
        this.repeatCount = repeatCount;
        this.reverses = reverses;
        this.delay = delay;
    }

    public final float getDelay() {
        return this.delay;
    }

    public final int getDirection() {
        return this.direction;
    }

    public final int getRepeatCount() {
        return this.repeatCount;
    }

    public final boolean getReverses() {
        return this.reverses;
    }

    public final int getStyle() {
        return this.style;
    }

    public final float getTime() {
        return this.time;
    }
}

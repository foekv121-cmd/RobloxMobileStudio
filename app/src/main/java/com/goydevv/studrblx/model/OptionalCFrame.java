package com.goydevv.studrblx.model;

import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: DataTypes.kt */
/* JADX INFO: loaded from: classes4.dex */
public final /* data */ class OptionalCFrame {
    private final CFrame cframe;

    public static /* synthetic */ OptionalCFrame copy$default(OptionalCFrame optionalCFrame, CFrame cFrame, int i, Object obj) {
        if ((i & 1) != 0) {
            cFrame = optionalCFrame.cframe;
        }
        return optionalCFrame.copy(cFrame);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final CFrame component1() {
        return this.cframe;
    }

    public final OptionalCFrame copy(CFrame cframe) {
        return new OptionalCFrame(cframe);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        return (other instanceof OptionalCFrame) && Intrinsics.areEqual(this.cframe, ((OptionalCFrame) other).cframe);
    }

    public int hashCode() {
        if (this.cframe == null) {
            return 0;
        }
        return this.cframe.hashCode();
    }

    public OptionalCFrame(CFrame cframe) {
        this.cframe = cframe;
    }

    public final CFrame getCframe() {
        return this.cframe;
    }

    public String toString() {
        String string;
        CFrame cFrame = this.cframe;
        return (cFrame == null || (string = cFrame.toString()) == null) ? "nil" : string;
    }
}

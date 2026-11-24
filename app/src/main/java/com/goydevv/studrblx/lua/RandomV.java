package com.goydevv.studrblx.lua;

import java.util.Random;

/* JADX INFO: compiled from: LuaTypes.kt */
/* JADX INFO: loaded from: classes4.dex */
public final class RandomV {
    private final Random rng;

    public RandomV(long seed) {
        this.rng = new Random(seed);
    }

    public final Random getRng() {
        return this.rng;
    }
}

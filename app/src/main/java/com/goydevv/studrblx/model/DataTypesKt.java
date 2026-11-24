package com.goydevv.studrblx.model;

import org.luaj.vm2.LuaDouble;

/* JADX INFO: compiled from: DataTypes.kt */
/* JADX INFO: loaded from: classes4.dex */
public final class DataTypesKt {
    public static final String fmtNum(double f) {
        if (Double.isNaN(f)) {
            return LuaDouble.JSTR_NAN;
        }
        if (Double.isInfinite(f)) {
            return f > 0.0d ? LuaDouble.JSTR_POSINF : LuaDouble.JSTR_NEGINF;
        }
        double r = Math.round(f * 1000.0d) / 1000.0d;
        return (!((r > Math.rint(r) ? 1 : (r == Math.rint(r) ? 0 : -1)) == 0) || Math.abs(r) >= 1.0E15d) ? String.valueOf(r) : String.valueOf((long) r);
    }

    public static final String fmtNum(float f) {
        return fmtNum((double) f);
    }
}

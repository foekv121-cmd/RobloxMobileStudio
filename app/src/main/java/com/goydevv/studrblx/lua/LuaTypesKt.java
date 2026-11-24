package com.goydevv.studrblx.lua;

import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;

/* JADX INFO: compiled from: LuaTypes.kt */
/* JADX INFO: loaded from: classes4.dex */
public final class LuaTypesKt {
    public static final OneArgFunction fn1(final Function1<? super LuaValue, ? extends LuaValue> f) {
        Intrinsics.checkNotNullParameter(f, "f");
        return new OneArgFunction() { // from class: com.goydevv.studrblx.lua.LuaTypesKt.fn1.1
            @Override // org.luaj.vm2.lib.OneArgFunction, org.luaj.vm2.lib.LibFunction, org.luaj.vm2.LuaValue
            public LuaValue call(LuaValue a) {
                Intrinsics.checkNotNullParameter(a, "a");
                return f.invoke(a);
            }
        };
    }

    public static final TwoArgFunction fn2(final Function2<? super LuaValue, ? super LuaValue, ? extends LuaValue> f) {
        Intrinsics.checkNotNullParameter(f, "f");
        return new TwoArgFunction() { // from class: com.goydevv.studrblx.lua.LuaTypesKt.fn2.1
            @Override // org.luaj.vm2.lib.TwoArgFunction, org.luaj.vm2.lib.LibFunction, org.luaj.vm2.LuaValue
            public LuaValue call(LuaValue a, LuaValue b) {
                Intrinsics.checkNotNullParameter(a, "a");
                Intrinsics.checkNotNullParameter(b, "b");
                return f.invoke(a, b);
            }
        };
    }

    public static final VarArgFunction fnv(final Function1<? super Varargs, ? extends Varargs> f) {
        Intrinsics.checkNotNullParameter(f, "f");
        return new VarArgFunction() { // from class: com.goydevv.studrblx.lua.LuaTypesKt.fnv.1
            @Override // org.luaj.vm2.lib.VarArgFunction, org.luaj.vm2.lib.LibFunction, org.luaj.vm2.LuaValue
            public Varargs invoke(Varargs args) {
                Intrinsics.checkNotNullParameter(args, "args");
                return f.invoke(args);
            }
        };
    }

    public static final float num(LuaValue $this$num) {
        Intrinsics.checkNotNullParameter($this$num, "<this>");
        return (float) $this$num.checkdouble();
    }

    public static final float optNum(LuaValue $this$optNum, float d) {
        Intrinsics.checkNotNullParameter($this$optNum, "<this>");
        return $this$optNum.isnil() ? d : (float) $this$optNum.checkdouble();
    }

    public static final /* synthetic */ <T> T ud(LuaValue luaValue, String what) {
        Intrinsics.checkNotNullParameter(luaValue, "<this>");
        Intrinsics.checkNotNullParameter(what, "what");
        T t = luaValue.isuserdata() ? (T) luaValue.touserdata() : null;
        Intrinsics.reifiedOperationMarker(2, "T");
        if (t != null) {
            return t;
        }
        throw new LuaError(what + " expected, got " + LuaTypes.INSTANCE.typeOf(luaValue));
    }

    public static final /* synthetic */ <T> T udOrNull(LuaValue luaValue) {
        Intrinsics.checkNotNullParameter(luaValue, "<this>");
        T t = luaValue.isuserdata() ? (T) luaValue.touserdata() : null;
        Intrinsics.reifiedOperationMarker(2, "T");
        return t;
    }
}

package com.goydevv.studrblx.lua;

import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

/* JADX INFO: compiled from: LuaSandbox.kt */
/* JADX INFO: loaded from: classes4.dex */
public final class LuaSandbox {
    public static final LuaSandbox INSTANCE = new LuaSandbox();
    private static final LuaString ESC = LuaString.valueOf("\u001bLua");

    private LuaSandbox() {
    }

    public final void lockDown(Globals g) {
        Intrinsics.checkNotNullParameter(g, "g");
        for (String name : CollectionsKt.listOf(new String[]{"package", "require", "luajava", "io", "dofile", "loadfile", "module"})) {
            g.set(name, LuaValue.NIL);
        }
        LuaValue realOs = g.get("os");
        LuaTable os = new LuaTable();
        if (realOs.istable()) {
            for (String k : CollectionsKt.listOf(new String[]{"time", "clock", "date", "difftime"})) {
                os.set(k, realOs.get(k));
            }
        }
        g.set("os", os);
        LuaValue str = g.get("string");
        if (str.istable()) {
            str.set("dump", LuaValue.NIL);
        }
        LuaValue luaValue = LuaString.s_metatable;
        LuaValue shared = luaValue != null ? luaValue.get(LuaValue.INDEX) : null;
        if (shared != null && shared.istable()) {
            shared.set("dump", LuaValue.NIL);
        }
        final LuaValue realLoad = g.get("load");
        g.set("load", new VarArgFunction() { // from class: com.goydevv.studrblx.lua.LuaSandbox.lockDown.1
            @Override // org.luaj.vm2.lib.VarArgFunction, org.luaj.vm2.lib.LibFunction, org.luaj.vm2.LuaValue
            public Varargs invoke(Varargs a) {
                Intrinsics.checkNotNullParameter(a, "a");
                LuaValue chunk = a.arg1();
                if (chunk.isfunction()) {
                    StringBuilder sb = new StringBuilder();
                    int guard = 0;
                    while (true) {
                        int guard2 = guard + 1;
                        if (guard >= 100000) {
                            break;
                        }
                        LuaValue piece = chunk.call();
                        if (piece.isnil()) {
                            break;
                        }
                        if (piece.isstring()) {
                            String str2 = piece.tojstring();
                            Intrinsics.checkNotNullExpressionValue(str2, "tojstring(...)");
                            if (str2.length() == 0) {
                                break;
                            }
                        }
                        sb.append(piece.checkjstring());
                        if (sb.length() > 50000000) {
                            Varargs varargsVarargsOf = LuaValue.varargsOf(LuaValue.NIL, LuaValue.valueOf("chunk too large"));
                            Intrinsics.checkNotNullExpressionValue(varargsVarargsOf, "varargsOf(...)");
                            return varargsVarargsOf;
                        }
                        guard = guard2;
                    }
                    chunk = LuaValue.valueOf(sb.toString());
                }
                LuaSandbox luaSandbox = LuaSandbox.INSTANCE;
                Intrinsics.checkNotNull(chunk);
                if (luaSandbox.isBytecode(chunk)) {
                    Varargs varargsVarargsOf2 = LuaValue.varargsOf(LuaValue.NIL, LuaValue.valueOf("bytecode is not allowed"));
                    Intrinsics.checkNotNullExpressionValue(varargsVarargsOf2, "varargsOf(...)");
                    return varargsVarargsOf2;
                }
                Varargs varargsInvoke = realLoad.invoke(LuaValue.varargsOf(new LuaValue[]{chunk, a.arg(2), LuaValue.valueOf("t"), a.arg(4)}));
                Intrinsics.checkNotNullExpressionValue(varargsInvoke, "invoke(...)");
                return varargsInvoke;
            }
        });
    }

    public final boolean isBytecode(LuaValue v) {
        Intrinsics.checkNotNullParameter(v, "v");
        if (!v.isstring()) {
            return false;
        }
        LuaString s = v.checkstring();
        return (s.length() >= 1 && s.luaByte(0) == 27) || (s.length() >= ESC.length() && s.substring(0, ESC.length()).raweq(ESC));
    }
}

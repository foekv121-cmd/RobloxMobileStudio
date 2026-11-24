package com.goydevv.studrblx.lua;

import java.util.Iterator;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.luaj.vm2.LuaBoolean;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaInteger;
import org.luaj.vm2.LuaNumber;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

/* JADX INFO: compiled from: RobloxServices.kt */
/* JADX INFO: loaded from: classes4.dex */
public final class Json {
    public static final Json INSTANCE = new Json();

    private Json() {
    }

    public final String encode(LuaValue v) {
        Intrinsics.checkNotNullParameter(v, "v");
        return String.valueOf(toJson(v, 0));
    }

    private final Object toJson(LuaValue v, int depth) {
        try {
        if (depth > 100) {
            throw new LuaError("Cannot convert recursive table to JSON");
        }
        if (v.isnil()) {
            return JSONObject.NULL;
        }
        if (v.isboolean()) {
            return Boolean.valueOf(v.toboolean());
        }
        if (v.isnumber()) {
            double d = v.todouble();
            return (!((d > Math.floor(d) ? 1 : (d == Math.floor(d) ? 0 : -1)) == 0) || Math.abs(d) >= 1.0E15d) ? Double.valueOf(d) : Long.valueOf((long) d);
        }
        if (v.isstring()) {
            return v.tojstring();
        }
        if (v.istable()) {
            int n = v.length();
            int count = 0;
            LuaValue luaValueArg1 = LuaValue.NIL;
            while (true) {
                Varargs nx = v.next(luaValueArg1);
                luaValueArg1 = nx.arg1();
                if (luaValueArg1.isnil()) {
                    break;
                }
                count++;
            }
            if (n > 0 && n == count) {
                JSONArray jSONArray = new JSONArray();
                int i = 1;
                if (1 <= n) {
                    while (true) {
                        Json json = INSTANCE;
                        LuaValue luaValue = v.get(i);
                        Intrinsics.checkNotNullExpressionValue(luaValue, "get(...)");
                        jSONArray.put(json.toJson(luaValue, depth + 1));
                        if (i == n) {
                            break;
                        }
                        i++;
                    }
                }
                return jSONArray;
            }
            if (count == 0) {
                return new JSONArray();
            }
            JSONObject jSONObject = new JSONObject();
            LuaValue luaValueArg2 = LuaValue.NIL;
            while (true) {
                Varargs next = v.next(luaValueArg2);
                luaValueArg2 = next.arg1();
                if (luaValueArg2.isnil()) {
                    return jSONObject;
                }
                String str = luaValueArg2.tojstring();
                Json json2 = INSTANCE;
                LuaValue luaValueArg = next.arg(2);
                Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
                jSONObject.put(str, json2.toJson(luaValueArg, depth + 1));
            }
        } else {
            return JSONObject.NULL;
        }
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    public final LuaValue decode(LuaRuntime rt, String s) {
        try {
        LuaNumber luaNumberValueOf;
        Object parsed;
        Intrinsics.checkNotNullParameter(rt, "rt");
        Intrinsics.checkNotNullParameter(s, "s");
        String t = StringsKt.trim((CharSequence) s).toString();
        if (StringsKt.startsWith(t, "{", false)) {
            parsed = new JSONObject(t);
        } else {
            if (!StringsKt.startsWith(t, "[", false)) {
                if (Intrinsics.areEqual(t, "null")) {
                    LuaValue NIL = LuaValue.NIL;
                    Intrinsics.checkNotNullExpressionValue(NIL, "NIL");
                    return NIL;
                }
                if (Intrinsics.areEqual(t, "true")) {
                    LuaBoolean TRUE = LuaValue.TRUE;
                    Intrinsics.checkNotNullExpressionValue(TRUE, "TRUE");
                    return TRUE;
                }
                if (Intrinsics.areEqual(t, "false")) {
                    LuaBoolean FALSE = LuaValue.FALSE;
                    Intrinsics.checkNotNullExpressionValue(FALSE, "FALSE");
                    return FALSE;
                }
                if (StringsKt.startsWith(t, "\"", false)) {
                    LuaString luaStringValueOf = LuaValue.valueOf(new JSONArray("[" + t + "]").getString(0));
                    Intrinsics.checkNotNullExpressionValue(luaStringValueOf, "valueOf(...)");
                    return luaStringValueOf;
                }
                Double doubleOrNull = StringsKt.toDoubleOrNull(t);
                if (doubleOrNull == null || (luaNumberValueOf = LuaValue.valueOf(doubleOrNull.doubleValue())) == null) {
                    throw new LuaError("Can't parse JSON");
                }
                return luaNumberValueOf;
            }
            parsed = new JSONArray(t);
        }
        return fromJson(parsed);
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    private final LuaValue fromJson(Object o) {
        if (o == null || Intrinsics.areEqual(o, JSONObject.NULL)) {
            LuaValue NIL = LuaValue.NIL;
            Intrinsics.checkNotNullExpressionValue(NIL, "NIL");
            return NIL;
        }
        if (o instanceof Boolean) {
            LuaBoolean luaBooleanValueOf = LuaValue.valueOf(((Boolean) o).booleanValue());
            Intrinsics.checkNotNullExpressionValue(luaBooleanValueOf, "valueOf(...)");
            return luaBooleanValueOf;
        }
        if (o instanceof Integer) {
            LuaInteger luaIntegerValueOf = LuaValue.valueOf(((Number) o).intValue());
            Intrinsics.checkNotNullExpressionValue(luaIntegerValueOf, "valueOf(...)");
            return luaIntegerValueOf;
        }
        if (o instanceof Long) {
            LuaNumber luaNumberValueOf = LuaValue.valueOf(((Number) o).longValue());
            Intrinsics.checkNotNullExpressionValue(luaNumberValueOf, "valueOf(...)");
            return luaNumberValueOf;
        }
        if (o instanceof Number) {
            LuaNumber luaNumberValueOf2 = LuaValue.valueOf(((Number) o).doubleValue());
            Intrinsics.checkNotNullExpressionValue(luaNumberValueOf2, "valueOf(...)");
            return luaNumberValueOf2;
        }
        if (o instanceof String) {
            LuaString luaStringValueOf = LuaValue.valueOf((String) o);
            Intrinsics.checkNotNullExpressionValue(luaStringValueOf, "valueOf(...)");
            return luaStringValueOf;
        }
        if (o instanceof JSONArray) {
            LuaTable luaTable = new LuaTable();
            int length = ((JSONArray) o).length();
            for (int i = 0; i < length; i++) {
                luaTable.set(i + 1, INSTANCE.fromJson(((JSONArray) o).opt(i)));
            }
            return luaTable;
        }
        if (!(o instanceof JSONObject)) {
            LuaString luaStringValueOf2 = LuaValue.valueOf(o.toString());
            Intrinsics.checkNotNullExpressionValue(luaStringValueOf2, "valueOf(...)");
            return luaStringValueOf2;
        }
        LuaTable luaTable2 = new LuaTable();
        Iterator<String> itKeys = ((JSONObject) o).keys();
        Intrinsics.checkNotNullExpressionValue(itKeys, "keys(...)");
        while (itKeys.hasNext()) {
            String str = (String) itKeys.next();
            luaTable2.set(str, INSTANCE.fromJson(((JSONObject) o).opt(str)));
        }
        return luaTable2;
    }
}

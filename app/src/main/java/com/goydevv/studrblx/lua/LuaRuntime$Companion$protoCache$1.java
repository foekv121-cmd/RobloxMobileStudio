package com.goydevv.studrblx.lua;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import kotlin.Pair;
import org.luaj.vm2.Prototype;

/* JADX INFO: compiled from: LuaRuntime.kt */
/* JADX INFO: loaded from: classes4.dex */
public final class LuaRuntime$Companion$protoCache$1 extends LinkedHashMap<String, Pair<? extends String, ? extends Prototype>> {
    LuaRuntime$Companion$protoCache$1() {
        super(256, 0.75f, true);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.LinkedHashMap
    protected boolean removeEldestEntry(Map.Entry<String, Pair<? extends String, ? extends Prototype>> eldest) {
        return size() > 3000;
    }
}

package com.goydevv.studrblx.lua;

import com.goydevv.studrblx.model.Instance;
import java.util.List;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: LuaTypes.kt */
/* JADX INFO: loaded from: classes4.dex */
public final class RaycastParamsV {
    private int filterType;
    private boolean ignoreWater;
    private boolean respectCanCollide;
    private List<? extends Instance> filter = CollectionsKt.emptyList();
    private String collisionGroup = "Default";

    public final String getCollisionGroup() {
        return this.collisionGroup;
    }

    public final List<Instance> getFilter() {
        return (List<Instance>) (List<?>) this.filter;
    }

    public final int getFilterType() {
        return this.filterType;
    }

    public final boolean getIgnoreWater() {
        return this.ignoreWater;
    }

    public final boolean getRespectCanCollide() {
        return this.respectCanCollide;
    }

    public final void setCollisionGroup(String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.collisionGroup = str;
    }

    public final void setFilter(List<? extends Instance> list) {
        Intrinsics.checkNotNullParameter(list, "<set-?>");
        this.filter = list;
    }

    public final void setFilterType(int i) {
        this.filterType = i;
    }

    public final void setIgnoreWater(boolean z) {
        this.ignoreWater = z;
    }

    public final void setRespectCanCollide(boolean z) {
        this.respectCanCollide = z;
    }
}

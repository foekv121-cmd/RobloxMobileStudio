package com.goydevv.studrblx.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.collections.CollectionsKt;

/* JADX INFO: compiled from: DataTypes.kt */
/* JADX INFO: loaded from: classes4.dex */
public final /* data */ class Axes {
    private final int mask;

    public static /* synthetic */ Axes copy$default(Axes axes, int i, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            i = axes.mask;
        }
        return axes.copy(i);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final int component1() {
        return this.mask;
    }

    public final Axes copy(int mask) {
        return new Axes(mask);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        return (other instanceof Axes) && this.mask == ((Axes) other).mask;
    }

    public int hashCode() {
        return Integer.hashCode(this.mask);
    }

    public Axes(int mask) {
        this.mask = mask;
    }

    public final int getMask() {
        return this.mask;
    }

    public String toString() {
        int i = 1;
        Iterable iterableListOf = CollectionsKt.listOf(new String[]{"X", "Y", "Z"});
        Collection arrayList = new ArrayList();
        int i2 = 0;
        for (Object obj : iterableListOf) {
            int i3 = i2 + 1;
            if (i2 < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            int i4 = i;
            if (((this.mask & (i4 << i2)) != 0 ? i4 : 0) != 0) {
                arrayList.add(obj);
            }
            i2 = i3;
            i = i4;
        }
        return CollectionsKt.joinToString((List) arrayList, ", ", "", "", -1, "...", null);
    }
}

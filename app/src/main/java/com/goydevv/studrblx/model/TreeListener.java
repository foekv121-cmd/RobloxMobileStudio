package com.goydevv.studrblx.model;

import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Instance.kt */
/* JADX INFO: loaded from: classes4.dex */
public interface TreeListener {

    /* JADX INFO: compiled from: Instance.kt */
    public static final class DefaultImpls {
        @Deprecated
        public static void childAdded(TreeListener $this, Instance parent, Instance child) {
            Intrinsics.checkNotNullParameter(parent, "parent");
            Intrinsics.checkNotNullParameter(child, "child");
        }

        @Deprecated
        public static void childRemoved(TreeListener $this, Instance parent, Instance child) {
            Intrinsics.checkNotNullParameter(parent, "parent");
            Intrinsics.checkNotNullParameter(child, "child");
        }

        @Deprecated
        public static void propChanged(TreeListener $this, Instance inst, String name) {
            Intrinsics.checkNotNullParameter(inst, "inst");
            Intrinsics.checkNotNullParameter(name, "name");
        }
    }

    default void childAdded(Instance parent, Instance child) {
        Intrinsics.checkNotNullParameter(parent, "parent");
        Intrinsics.checkNotNullParameter(child, "child");
    }

    default void childRemoved(Instance parent, Instance child) {
        Intrinsics.checkNotNullParameter(parent, "parent");
        Intrinsics.checkNotNullParameter(child, "child");
    }

    default void propChanged(Instance inst, String name) {
        Intrinsics.checkNotNullParameter(inst, "inst");
        Intrinsics.checkNotNullParameter(name, "name");
    }
}

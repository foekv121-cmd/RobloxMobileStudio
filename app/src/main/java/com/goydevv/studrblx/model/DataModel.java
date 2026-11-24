package com.goydevv.studrblx.model;

import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Instance.kt */
/* JADX INFO: loaded from: classes4.dex */
public final class DataModel extends Instance {
    private final CopyOnWriteArrayList<TreeListener> listeners;
    private final LinkedHashMap<String, String> meta;

    public DataModel() {
        super("DataModel");
        this.listeners = new CopyOnWriteArrayList<>();
        this.meta = new LinkedHashMap<>();
        getProps().put("Name", "Game");
    }

    public final CopyOnWriteArrayList<TreeListener> getListeners() {
        return this.listeners;
    }

    public final LinkedHashMap<String, String> getMeta() {
        return this.meta;
    }

    public final void fireChildAdded(Instance p, Instance c) {
        Intrinsics.checkNotNullParameter(p, "p");
        Intrinsics.checkNotNullParameter(c, "c");
        Iterator<TreeListener> it = this.listeners.iterator();
        Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
        while (it.hasNext()) {
            TreeListener l = it.next();
            l.childAdded(p, c);
        }
    }

    public final void fireChildRemoved(Instance p, Instance c) {
        Intrinsics.checkNotNullParameter(p, "p");
        Intrinsics.checkNotNullParameter(c, "c");
        Iterator<TreeListener> it = this.listeners.iterator();
        Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
        while (it.hasNext()) {
            TreeListener l = it.next();
            l.childRemoved(p, c);
        }
    }

    public final void firePropChanged(Instance i, String n) {
        Intrinsics.checkNotNullParameter(i, "i");
        Intrinsics.checkNotNullParameter(n, "n");
        Iterator<TreeListener> it = this.listeners.iterator();
        Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
        while (it.hasNext()) {
            TreeListener l = it.next();
            l.propChanged(i, n);
        }
    }

    public final Instance findService(String cls) {
        Object next;
        Intrinsics.checkNotNullParameter(cls, "cls");
        Iterator it = getChildren().iterator();
        while (it.hasNext()) {
            next = it.next();
            if (Intrinsics.areEqual(((Instance) next).getClassName(), cls)) {
                return (Instance) next;
            }
        }
        next = null;
        return (Instance) next;
    }

    public final Instance getService(String cls) {
        Intrinsics.checkNotNullParameter(cls, "cls");
        Instance instanceFindService = findService(cls);
        if (instanceFindService != null) {
            return instanceFindService;
        }
        Instance s = InstanceFactory.INSTANCE.create(cls);
        s.setService(true);
        Instance.setParent$default(s, this, 0, 2, null);
        return s;
    }

    public final Instance getWorkspace() {
        return getService("Workspace");
    }

    public final DataModel cloneModel() {
        IdentityHashMap<Instance, Instance> identityHashMap = new IdentityHashMap<>();
        DataModel dm = new DataModel();
        dm.meta.putAll(this.meta);
        Instance.INSTANCE.copyInto$app(this, identityHashMap, dm);
        Instance.INSTANCE.remapRefs$app(identityHashMap);
        return dm;
    }
}

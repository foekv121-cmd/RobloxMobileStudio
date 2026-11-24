package com.goydevv.studrblx.core;

import com.goydevv.studrblx.engine.Csg;
import com.goydevv.studrblx.engine.Geometry;
import com.goydevv.studrblx.model.CFrame;
import com.goydevv.studrblx.model.Color3;
import com.goydevv.studrblx.model.EnumValue;
import com.goydevv.studrblx.model.Instance;
import com.goydevv.studrblx.model.InstanceFactory;
import com.goydevv.studrblx.model.PartData;
import com.goydevv.studrblx.model.Vector3;
import com.goydevv.studrblx.render.UnionMeshes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.WeakHashMap;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.IntIterator;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;

/* JADX INFO: compiled from: SolidModeling.kt */
/* JADX INFO: loaded from: classes4.dex */
public final class SolidModeling {
    public static final SolidModeling INSTANCE = new SolidModeling();
    private static final Map<Instance, Result> cache = Collections.synchronizedMap(new WeakHashMap());
    private static final Map<Instance, Long> failed = Collections.synchronizedMap(new WeakHashMap());

    /* JADX INFO: compiled from: SolidModeling.kt */
    public static final class Style {
        private final Color3 color;
        private final float transparency;

        public Style(Color3 color, float transparency) {
            Intrinsics.checkNotNullParameter(color, "color");
            this.color = color;
            this.transparency = transparency;
        }

        public final Color3 getColor() {
            return this.color;
        }

        public final float getTransparency() {
            return this.transparency;
        }
    }

    private SolidModeling() {
    }

    /* JADX INFO: compiled from: SolidModeling.kt */
    public static final class Result {
        private final Vector3 initialSize;
        private final Vector3 offset;
        private final List<Csg.Polygon> polys;
        private final long sig;
        private final List<Style> styles;
        private final Map<Integer, float[]> tris;

        public Result(List<Csg.Polygon> polys, Map<Integer, float[]> tris, List<Style> styles, long sig, Vector3 initialSize, Vector3 offset) {
            Intrinsics.checkNotNullParameter(polys, "polys");
            Intrinsics.checkNotNullParameter(tris, "tris");
            Intrinsics.checkNotNullParameter(styles, "styles");
            Intrinsics.checkNotNullParameter(initialSize, "initialSize");
            Intrinsics.checkNotNullParameter(offset, "offset");
            this.polys = polys;
            this.tris = tris;
            this.styles = styles;
            this.sig = sig;
            this.initialSize = initialSize;
            this.offset = offset;
        }

        public final Vector3 getInitialSize() {
            return this.initialSize;
        }

        public final Vector3 getOffset() {
            return this.offset;
        }

        public final List<Csg.Polygon> getPolys() {
            return this.polys;
        }

        public final long getSig() {
            return this.sig;
        }

        public final List<Style> getStyles() {
            return this.styles;
        }

        public final Map<Integer, float[]> getTris() {
            return this.tris;
        }
    }

    public final boolean isOperation(Instance i) {
        Intrinsics.checkNotNullParameter(i, "i");
        return i.isA("PartOperation") && !Intrinsics.areEqual(i.getClassName(), "NegateOperation");
    }

    public final List<Instance> sources(Instance op) {
        Intrinsics.checkNotNullParameter(op, "op");
        Iterable children = op.getChildren();
        Collection arrayList = new ArrayList();
        for (Object obj : children) {
            if (((Instance) obj).isA("BasePart")) {
                arrayList.add(obj);
            }
        }
        return (List) arrayList;
    }

    public final boolean canSeparate(Instance op) {
        Intrinsics.checkNotNullParameter(op, "op");
        return isOperation(op) && !sources(op).isEmpty();
    }

    private final Pair<CFrame, Vector3> reference(List<? extends Instance> srcs) {
        Pair<Vector3, Vector3> pairBoundsOf = Geometry.INSTANCE.boundsOf(srcs);
        if (pairBoundsOf == null) {
            return null;
        }
        Vector3 a = pairBoundsOf.component1();
        Vector3 b = pairBoundsOf.component2();
        return TuplesKt.to(CFrame.INSTANCE.at(a.plus(b).div(2.0f)), b.minus(a));
    }

    private final long signature(Instance op, List<? extends Instance> srcs) {
        long h = (((long) op.getClassName().hashCode()) * 31) + ((long) Boolean.hashCode(Intrinsics.areEqual(op.get("UsePartColor"), (Object) true)));
        for (Instance s : srcs) {
            h = (((((((((h * 31) + ((long) s.getClassName().hashCode())) * 31) + ((long) PartData.INSTANCE.cframe(s).hashCode())) * 31) + ((long) PartData.INSTANCE.size(s).hashCode())) * 31) + ((long) PartData.INSTANCE.shape(s))) * 31) + ((long) PartData.INSTANCE.color(s).hashCode());
            if (isOperation(s)) {
                long j = h * 31;
                Result result = result(s);
                h = j + (result != null ? result.getSig() : 0L);
            }
        }
        return Intrinsics.areEqual(op.get("UsePartColor"), (Object) true) ? (31 * h) + ((long) PartData.INSTANCE.color(op).hashCode()) : h;
    }

    public final Result result(Instance op) {
        Intrinsics.checkNotNullParameter(op, "op");
        if (!isOperation(op)) {
            return null;
        }
        List<Instance> listSources = sources(op);
        if (listSources.isEmpty()) {
            return null;
        }
        long sig = signature(op, listSources);
        Result result = cache.get(op);
        if (result != null && result.getSig() == sig) {
            return result;
        }
        Long l = failed.get(op);
        if (l != null && l.longValue() == sig) {
            return null;
        }
        try {
            Result resultCompute = compute(op, listSources, sig);
            Map<Instance, Result> cache2 = cache;
            Intrinsics.checkNotNullExpressionValue(cache2, "cache");
            cache2.put(op, resultCompute);
            return resultCompute;
        } catch (Exception e) {
            Map<Instance, Long> failed2 = failed;
            Intrinsics.checkNotNullExpressionValue(failed2, "failed");
            failed2.put(op, Long.valueOf(sig));
            OutputLog outputLog = OutputLog.INSTANCE;
            String name = op.getName();
            String message = e.getMessage();
            outputLog.warn("Couldn't build " + name + ": " + (message != null ? message : e));
            return null;
        }
    }

    private final Result compute(Instance op, List<? extends Instance> srcs, long sig) {
        ArrayList styles;
        List<Csg.Polygon> list;
        Pair<CFrame, Vector3> pairReference = reference(srcs);
        if (pairReference == null) {
            throw new IllegalStateException("no parts");
        }
        CFrame ref = pairReference.component1();
        Vector3 size = pairReference.component2();
        ArrayList styles2 = new ArrayList();
        boolean usePart = Intrinsics.areEqual(op.get("UsePartColor"), (Object) true);
        Collection arrayList = new ArrayList();
        for (Object obj : srcs) {
            if (!Intrinsics.areEqual(((Instance) obj).getClassName(), "NegateOperation")) {
                arrayList.add(obj);
            }
        }
        List positive = (List) arrayList;
        Collection arrayList2 = new ArrayList();
        for (Object obj2 : srcs) {
            if (Intrinsics.areEqual(((Instance) obj2).getClassName(), "NegateOperation")) {
                arrayList2.add(obj2);
            }
        }
        List<Instance> negative = (List) arrayList2;
        List<Csg.Polygon> listEmptyList = positive.isEmpty() ? CollectionsKt.emptyList() : compute$polysOf(styles2, usePart, op, ref, (Instance) positive.get(0));
        for (Object _sObj : CollectionsKt.drop(positive, 1)) {
            Instance s = (Instance) _sObj;
            listEmptyList = Intrinsics.areEqual(op.getClassName(), "IntersectOperation") ? Csg.INSTANCE.intersect(listEmptyList, compute$polysOf(styles2, usePart, op, ref, s)) : Csg.INSTANCE.union(listEmptyList, compute$polysOf(styles2, usePart, op, ref, s));
        }
        for (Instance n : negative) {
            listEmptyList = Csg.INSTANCE.subtract(listEmptyList, compute$polysOf(styles2, usePart, op, ref, n));
        }
        Vector3 mn = new Vector3(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
        Vector3 mx = mn.unaryMinus();
        for (Csg.Polygon p : listEmptyList) {
            for (Csg.Vertex v : p.getVertices()) {
                mn = mn.min(v.getPos());
                mx = mx.max(v.getPos());
            }
        }
        if (listEmptyList.isEmpty()) {
            mn = size.unaryMinus().div(2.0f);
            mx = size.div(2.0f);
        }
        Vector3 c = mn.plus(mx).div(2.0f);
        if (c.getMagnitude() < 1.0E-5f) {
            styles = styles2;
            list = listEmptyList;
        } else {
            Iterable<Csg.Polygon> iterable = listEmptyList;
            Collection arrayList3 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
            for (Csg.Polygon polygon : iterable) {
                Iterable vertices = polygon.getVertices();
                List negative2 = negative;
                Vector3 size2 = size;
                Collection arrayList4 = new ArrayList(CollectionsKt.collectionSizeOrDefault(vertices, 10));
                Iterable<Csg.Vertex> iterable2 = vertices;
                for (Csg.Vertex vertex : iterable2) {
                    arrayList4.add(new Csg.Vertex(vertex.getPos().minus(c), vertex.getNormal()));
                }
                arrayList3.add(new Csg.Polygon((List) arrayList4, polygon.getShared(), new Csg.Plane(polygon.getPlane().getNormal(), polygon.getPlane().getW() - polygon.getPlane().getNormal().dot(c))));
                size = size2;
                negative = negative2;
            }
            styles = styles2;
            list = (List) arrayList3;
        }
        return new Result(list, Csg.INSTANCE.toTriangles(list), styles, sig, mx.minus(mn).max(new Vector3(0.01f, 0.01f, 0.01f)), c);
    }

    private static final List<Csg.Polygon> compute$polysOf(ArrayList<Style> arrayList, boolean usePart, Instance $op, CFrame ref, Instance s) {
        int idx = arrayList.size();
        PartData partData = PartData.INSTANCE;
        arrayList.add(new Style(usePart ? partData.color($op) : partData.color(s), PartData.INSTANCE.transparency(s)));
        return Csg.INSTANCE.partPolygons(s, idx, ref, new Function1() { // from class: com.goydevv.studrblx.core.SolidModeling$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return SolidModeling.compute$polysOf$lambda$4((Instance) obj);
            }
        });
    }

    static final List compute$polysOf$lambda$4(Instance inner) {
        Intrinsics.checkNotNullParameter(inner, "inner");
        return INSTANCE.scaledPolys(inner);
    }

    private final List<Csg.Polygon> scaledPolys(Instance op) {
        Result r = result(op);
        if (r == null) {
            return storedPolys(op);
        }
        Vector3 s = scaleOf(op, r);
        if (Math.abs(s.getX() - 1.0f) < 1.0E-4f && Math.abs(s.getY() - 1.0f) < 1.0E-4f && Math.abs(s.getZ() - 1.0f) < 1.0E-4f) {
            return r.getPolys();
        }
        Iterable<Csg.Polygon> polys = r.getPolys();
        Collection arrayList = new ArrayList();
        for (Csg.Polygon polygon : polys) {
            Result r2 = r;
            Iterable vertices = polygon.getVertices();
            Collection arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(vertices, 10));
            Iterable<Csg.Vertex> iterable = vertices;
            for (Csg.Vertex vertex : iterable) {
                arrayList2.add(new Csg.Vertex(vertex.getPos().times(s), vertex.getNormal().div(s).getUnit()));
            }
            Iterable iterable2 = polys;
            List list = (List) arrayList2;
            Vector3 s2 = s;
            Csg.Plane planeFrom = Csg.Plane.INSTANCE.from(((Csg.Vertex) list.get(0)).getPos(), ((Csg.Vertex) list.get(1)).getPos(), ((Csg.Vertex) list.get(2)).getPos());
            Csg.Polygon polygon2 = planeFrom != null ? new Csg.Polygon(list, polygon.getShared(), planeFrom) : null;
            if (polygon2 != null) {
                arrayList.add(polygon2);
            }
            s = s2;
            r = r2;
            polys = (List) iterable2;
        }
        return (List) arrayList;
    }

    private final List<Csg.Polygon> storedPolys(Instance op) {
        UnionMeshes.Data d;
        Vector3 s;
        Csg.Polygon polygon;
        if (!UnionMeshes.INSTANCE.isStored(op) || (d = UnionMeshes.INSTANCE.get(op)) == null) {
            return null;
        }
        Vector3 now = PartData.INSTANCE.size(op);
        Vector3 s2 = new Vector3(storedPolys$k(now.getX(), d.getSize().getX()), storedPolys$k(now.getY(), d.getSize().getY()), storedPolys$k(now.getZ(), d.getSize().getZ()));
        Iterable<float[]> triangles = d.getTriangles();
        Collection arrayList = new ArrayList();
        for (float[] fArr : triangles) {
            UnionMeshes.Data d2 = d;
            Iterable intRange = new IntRange(0, 2);
            Vector3 now2 = now;
            Collection arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(intRange, 10));
            Iterable iterable = intRange;
            Iterator<Integer> it = iterable.iterator();
            while (it.hasNext()) {
                int iNextInt = ((IntIterator) it).nextInt();
                arrayList2.add(new Vector3(fArr[iNextInt * 3], fArr[(iNextInt * 3) + 1], fArr[(iNextInt * 3) + 2]).minus(d2.getCenter()));
            }
            Iterable iterable2 = triangles;
            Iterable iterable3 = (List) arrayList2;
            Collection arrayList3 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable3, 10));
            Iterator it2 = iterable3.iterator();
            while (it2.hasNext()) {
                arrayList3.add(((Vector3) it2.next()).times(s2));
            }
            List list = (List) arrayList3;
            Csg.Plane planeFrom = Csg.Plane.INSTANCE.from((Vector3) list.get(0), (Vector3) list.get(1), (Vector3) list.get(2));
            if (planeFrom != null) {
                List list2 = list;
                Collection arrayList4 = new ArrayList(CollectionsKt.collectionSizeOrDefault(list2, 10));
                Iterable<Vector3> iterable4 = list2;
                for (Vector3 vector3 : iterable4) {
                    Iterable iterable5 = iterable4;
                    Vector3 s3 = s2;
                    Vector3 s4 = planeFrom.getNormal();
                    arrayList4.add(new Csg.Vertex(vector3, s4));
                    iterable4 = iterable5;
                    s2 = s3;
                }
                s = s2;
                polygon = new Csg.Polygon((List) arrayList4, 0, planeFrom);
            } else {
                s = s2;
                polygon = null;
            }
            if (polygon != null) {
                arrayList.add(polygon);
            }
            d = d2;
            now = now2;
            s2 = s;
            triangles = (List) iterable2;
        }
        return (List) arrayList;
    }

    private static final float storedPolys$k(float a, float b) {
        if (b > 1.0E-4f) {
            return a / b;
        }
        return 1.0f;
    }

    public final Vector3 scaleOf(Instance op, Result r) {
        Intrinsics.checkNotNullParameter(op, "op");
        Intrinsics.checkNotNullParameter(r, "r");
        Vector3 now = PartData.INSTANCE.size(op);
        return new Vector3(scaleOf$k$17(now.getX(), r.getInitialSize().getX()), scaleOf$k$17(now.getY(), r.getInitialSize().getY()), scaleOf$k$17(now.getZ(), r.getInitialSize().getZ()));
    }

    private static final float scaleOf$k$17(float a, float b) {
        if (b > 1.0E-5f) {
            return a / b;
        }
        return 1.0f;
    }

    private final void notify(final Studio studio) {
        studio.getMain().post(new Runnable() { // from class: com.goydevv.studrblx.core.SolidModeling$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                SolidModeling.notify$lambda$19(studio);
            }
        });
    }

    static final void notify$lambda$19(Studio $studio) {
        for (Studio.Listener listener : $studio.getListeners()) {
            listener.onSelectionChanged();
            listener.onTreeChanged();
        }
    }

    private final void copyPartProps(Instance from, Instance to) {
        for (Map.Entry<String, Object> entry : from.getProps().entrySet()) {
            String k = entry.getKey();
            Object v = entry.getValue();
            if (!Intrinsics.areEqual(k, "Name")) {
                to.getProps().put(k, v);
                Integer num = from.getPropTypes().get(k);
                if (num != null) {
                    to.getPropTypes().put(k, Integer.valueOf(num.intValue()));
                }
            }
        }
        to.getProps().put("Name", from.getName());
    }

    public final int negate(Studio studio) {
        boolean z;
        Instance instanceCreate;
        Intrinsics.checkNotNullParameter(studio, "studio");
        ArrayList out = new ArrayList();
        int n = 0;
        synchronized (studio.getLock()) {
            Iterable selection = studio.getSelection();
            Collection arrayList = new ArrayList();
            Iterator it = selection.iterator();
            while (true) {
                z = false;
                if (!it.hasNext()) {
                    break;
                }
                Object next = it.next();
                Instance instance = (Instance) next;
                if ((!instance.isA("BasePart") || INSTANCE.isOperation(instance) || instance.getParent() == null) ? false : true) {
                    arrayList.add(next);
                }
            }
            List<Instance> list = (List) arrayList;
            if (list.isEmpty()) {
                return 0;
            }
            studio.getUndo().begin("Negate");
            for (Instance instance2 : list) {
                Instance parent = instance2.getParent();
                if (parent != null) {
                    int i = 0;
                    Iterator<Instance> it2 = parent.getChildren().iterator();
                    while (true) {
                        if (it2.hasNext()) {
                            if (!(((Instance) it2.next()) == instance2 ? true : z)) {
                                i++;
                            }
                        } else {
                            i = -1;
                            break;
                        }
                    }
                    if (Intrinsics.areEqual(instance2.getClassName(), "NegateOperation")) {
                        Object obj = instance2.getAttributes().get("__cls");
                        String str = obj instanceof String ? (String) obj : null;
                        if (str == null) {
                            switch (PartData.INSTANCE.shape(instance2)) {
                                case 3:
                                    str = "WedgePart";
                                    break;
                                case 4:
                                    str = "CornerWedgePart";
                                    break;
                                default:
                                    str = "Part";
                                    break;
                            }
                        }
                        instanceCreate = InstanceFactory.INSTANCE.create(str);
                        INSTANCE.copyPartProps(instance2, instanceCreate);
                        if (!Intrinsics.areEqual(str, "Part")) {
                            instanceCreate.getProps().remove("shape");
                        }
                    } else {
                        instanceCreate = InstanceFactory.INSTANCE.create("NegateOperation");
                        INSTANCE.copyPartProps(instance2, instanceCreate);
                        instanceCreate.getAttributes().put("__cls", instance2.getClassName());
                        instanceCreate.getProps().put("shape", new EnumValue(PartData.INSTANCE.shape(instance2)));
                        instanceCreate.getPropTypes().put("shape", 18);
                    }
                    Iterator it3 = CollectionsKt.toList(instance2.getChildren()).iterator();
                    while (it3.hasNext()) {
                        studio.reparent((Instance) it3.next(), instanceCreate);
                    }
                    studio.reparent(instance2, null);
                    studio.reparent(instanceCreate, parent);
                    parent.getChildren().remove(instanceCreate);
                    parent.getChildren().add(RangesKt.coerceIn(i, 0, parent.getChildren().size()), instanceCreate);
                    out.add(instanceCreate);
                    n++;
                    z = false;
                }
            }
            studio.getUndo().commit();
            studio.getSelection().clear();
            studio.getSelection().addAll(out);
            notify(studio);
            return n;
        }
    }

    /* JADX WARN: Code duplicated, block: B:204:0x00d7 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:47:0x00cd  */
    /* JADX WARN: Code duplicated, block: B:51:0x00d4 A[Catch: all -> 0x03fd, TryCatch #1 {all -> 0x03fd, blocks: (B:5:0x000e, B:6:0x0020, B:8:0x0028, B:10:0x0036, B:12:0x003e, B:26:0x0066, B:15:0x0048, B:16:0x004c, B:18:0x0052, B:27:0x006a, B:29:0x0070, B:31:0x0078, B:34:0x0085, B:35:0x0099, B:37:0x009f, B:39:0x00b5, B:41:0x00bb, B:51:0x00d4, B:53:0x00db, B:54:0x00e9, B:56:0x00ef, B:58:0x00ff, B:73:0x0137, B:61:0x010c, B:62:0x0110, B:64:0x0116, B:77:0x0147, B:79:0x0150, B:81:0x0158, B:95:0x018d, B:84:0x0163, B:85:0x0167, B:87:0x016d, B:98:0x01a8, B:100:0x01b0, B:114:0x01e2, B:119:0x01f1, B:120:0x0205, B:122:0x020b, B:124:0x021f, B:126:0x0228, B:127:0x0230, B:131:0x0239, B:135:0x0245, B:137:0x027a, B:148:0x02be, B:152:0x02dd, B:153:0x0346, B:155:0x034c, B:156:0x035a, B:160:0x0365, B:161:0x0374, B:163:0x037a, B:164:0x0384, B:166:0x038c, B:167:0x03a5, B:140:0x0289, B:141:0x028d, B:143:0x0293, B:103:0x01bb, B:104:0x01bf, B:106:0x01c5), top: B:186:0x000e }] */
    public final String combine(Studio studio, boolean intersect) {
        try {
        String str;
        boolean z;
        char c;
        String str2;
        boolean z2;
        boolean z3;
        Iterable iterable;
        boolean z4;
        int i;
        int i2 = 0;
        Intrinsics.checkNotNullParameter(studio, "studio");
        synchronized (studio.getLock()) {
            int i3 = 0;
            try {
                ArrayList arrayList = new ArrayList();
                Iterator<Instance> it = studio.getSelection().iterator();
                Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
                while (true) {
                    str = null;
                    boolean z5 = true;
                    if (!it.hasNext()) {
                        break;
                    }
                    Instance next = it.next();
                    if (next.isA("BasePart")) {
                        ArrayList arrayList2 = arrayList;
                        if (!(arrayList2 instanceof Collection) || !arrayList2.isEmpty()) {
                            Iterator it2 = arrayList2.iterator();
                            while (it2.hasNext()) {
                                if (((Instance) it2.next()) == next) {
                                    z5 = false;
                                    break;
                                }
                            }
                        }
                        if (z5) {
                            arrayList.add(next);
                        }
                    } else if (!next.getIsService() && (next.isA("Model") || Intrinsics.areEqual(next.getClassName(), "Folder"))) {
                        Iterable iterableDescendants$default = Instance.descendants$default(next, null, 1, null);
                        Collection arrayList3 = new ArrayList();
                        for (Object obj : iterableDescendants$default) {
                            Instance instance = (Instance) obj;
                            if (instance.isA("BasePart")) {
                                Instance parent = instance.getParent();
                                if (parent != null) {
                                    i = i3;
                                    int i4 = parent.isA("PartOperation") ? 1 : 0;
                                    i2 = i4 == 0 ? 1 : 0;
                                    if (i2 != 0) {
                                        arrayList3.add(obj);
                                    }
                                    i3 = i;
                                } else {
                                    i = i3;
                                }
                                if (i2 != 0) {
                                    arrayList3.add(obj);
                                }
                                i3 = i;
                            } else {
                                i = i3;
                            }
                            if (i2 != 0) {
                                arrayList3.add(obj);
                            }
                            i3 = i;
                        }
                        int i5 = i3;
                        Iterable<Instance> iterable2 = (List) arrayList3;
                        for (Instance instance2 : iterable2) {
                            ArrayList arrayList4 = arrayList;
                            if ((arrayList4 instanceof Collection) && arrayList4.isEmpty()) {
                                iterable = iterable2;
                                z4 = true;
                            } else {
                                Iterator it3 = arrayList4.iterator();
                                while (true) {
                                    if (!it3.hasNext()) {
                                        iterable = iterable2;
                                        z4 = true;
                                        break;
                                    }
                                    iterable = iterable2;
                                    if (((Instance) it3.next()) == instance2) {
                                        z4 = false;
                                        break;
                                    }
                                    iterable2 = iterable;
                                }
                            }
                            if (z4) {
                                arrayList.add(instance2);
                            }
                            iterable2 = iterable;
                        }
                        i3 = i5;
                    }
                }
                char c2 = 2;
                if (arrayList.size() < 2) {
                    ArrayList arrayList5 = arrayList;
                    if (!(arrayList5 instanceof Collection) || !arrayList5.isEmpty()) {
                        Iterator it4 = arrayList5.iterator();
                        while (true) {
                            if (!it4.hasNext()) {
                                z3 = true;
                                break;
                            }
                            if (Intrinsics.areEqual(((Instance) it4.next()).getClassName(), "NegateOperation")) {
                                z3 = false;
                                break;
                            }
                        }
                    } else {
                        z3 = true;
                    }
                    if (z3) {
                        return "Select at least two parts to " + (intersect ? "intersect" : "union") + ".";
                    }
                }
                ArrayList arrayList6 = arrayList;
                if (!(arrayList6 instanceof Collection) || !arrayList6.isEmpty()) {
                    Iterator it5 = arrayList6.iterator();
                    while (true) {
                        if (!it5.hasNext()) {
                            z = true;
                            break;
                        }
                        if (!Intrinsics.areEqual(((Instance) it5.next()).getClassName(), "NegateOperation")) {
                            z = false;
                            break;
                        }
                    }
                } else {
                    z = true;
                }
                if (z) {
                    return "Select at least one part that isn't negated.";
                }
                Pair<CFrame, Vector3> pairReference = INSTANCE.reference(arrayList);
                if (pairReference == null) {
                    return "Nothing to combine.";
                }
                CFrame cFrameComponent1 = pairReference.component1();
                Object obj2 = (Vector3) pairReference.component2();
                for (Object obj3 : arrayList) {
                    if (!Intrinsics.areEqual(((Instance) obj3).getClassName(), "NegateOperation")) {
                        Instance instance3 = (Instance) obj3;
                        Instance parent2 = instance3.getParent();
                        if (parent2 == null) {
                            parent2 = studio.getModel().getWorkspace();
                        }
                        Instance instanceCreate = InstanceFactory.INSTANCE.create(intersect ? "IntersectOperation" : "UnionOperation");
                        instanceCreate.setName(intersect ? "Intersection" : "Union");
                        instanceCreate.set("CFrame", cFrameComponent1, false);
                        instanceCreate.set("size", obj2, false);
                        instanceCreate.set("Color3uint8", PartData.INSTANCE.color(instance3), false);
                        instanceCreate.set("Material", new EnumValue(PartData.INSTANCE.material(instance3)), false);
                        ArrayList arrayList7 = arrayList;
                        if ((arrayList7 instanceof Collection) && arrayList7.isEmpty()) {
                            c = c2;
                            str2 = null;
                            z2 = false;
                        } else {
                            Iterator it6 = arrayList7.iterator();
                            while (true) {
                                if (!it6.hasNext()) {
                                    c = c2;
                                    str2 = str;
                                    z2 = false;
                                    break;
                                }
                                c = c2;
                                str2 = str;
                                if (Intrinsics.areEqual(((Instance) it6.next()).get("Anchored"), (Object) true)) {
                                    z2 = true;
                                    break;
                                }
                                c2 = c;
                                str = str2;
                            }
                        }
                        instanceCreate.set("Anchored", Boolean.valueOf(z2), false);
                        instanceCreate.set("CanCollide", Boolean.valueOf(!Intrinsics.areEqual(instance3.get("CanCollide"), (Object) false)), false);
                        instanceCreate.set("Transparency", Float.valueOf(PartData.INSTANCE.transparency(instance3)), false);
                        instanceCreate.set("UsePartColor", false, false);
                        instanceCreate.set("SmoothingAngle", Float.valueOf(0.0f), false);
                        instanceCreate.set("RenderFidelity", new EnumValue(1), false);
                        instanceCreate.set("CollisionFidelity", new EnumValue(0), false);
                        String[] strArr = new String[6];
                        strArr[0] = "TopSurface";
                        strArr[1] = "BottomSurface";
                        strArr[c] = "FrontSurface";
                        strArr[3] = "BackSurface";
                        strArr[4] = "LeftSurface";
                        strArr[5] = "RightSurface";
                        Iterator it7 = CollectionsKt.listOf(strArr).iterator();
                        while (it7.hasNext()) {
                            instanceCreate.getProps().remove((String) it7.next());
                        }
                        studio.getUndo().begin(intersect ? "Intersect" : "Union");
                        studio.reparent(instanceCreate, parent2);
                        Iterator it8 = arrayList.iterator();
                        Intrinsics.checkNotNullExpressionValue(it8, "iterator(...)");
                        while (it8.hasNext()) {
                            studio.reparent((Instance) it8.next(), instanceCreate);
                        }
                        Result result = INSTANCE.result(instanceCreate);
                        if (result != null) {
                            instanceCreate.set("CFrame", cFrameComponent1.plus(result.getOffset()), false);
                            instanceCreate.set("size", result.getInitialSize(), false);
                        }
                        studio.getUndo().commit();
                        studio.getSelection().clear();
                        studio.getSelection().add(instanceCreate);
                        if (result(instanceCreate) == null) {
                            OutputLog.INSTANCE.warn(instanceCreate.getName() + " couldn't be computed; it will show as a box.");
                        }
                        notify(studio);
                        return str2;
                    }
                }
                try {
                    throw new NoSuchElementException("Collection contains no element matching the predicate.");
                } catch (Throwable th) {
                    throw th;
                }
            } catch (Throwable th2) {
                throw th2;
            }
        }
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    /* JADX WARN: Code duplicated, block: B:149:0x005e A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:151:0x0028 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:17:0x005a A[PHI: r17
      0x005a: PHI (r17v13 boolean) = (r17v14 boolean), (r17v15 boolean) binds: [B:15:0x0055, B:11:0x004c] A[DONT_GENERATE, DONT_INLINE]] */
    public final String separate(Studio studio) {
        try {
        boolean importedUnion;
        boolean z;
        int i = 0;
        boolean importedUnion2 = false;
        Iterator it = null;
        List list = null;
        Vector3 zero;
        boolean z2;
        boolean z3 = false;
        Intrinsics.checkNotNullParameter(studio, "studio");
        ArrayList freed = new ArrayList();
        boolean importedUnion3 = false;
        synchronized (studio.getLock()) {
            int i2 = 0;
            try {
                Iterable selection = studio.getSelection();
                Collection arrayList = new ArrayList();
                for (Object obj : selection) {
                    Instance instance = (Instance) obj;
                    if (INSTANCE.isOperation(instance)) {
                        z2 = false;
                    } else {
                        z2 = false;
                        if (!Intrinsics.areEqual(instance.getClassName(), "NegateOperation")) {
                            z3 = z2;
                        }
                        if (z3) {
                            arrayList.add(obj);
                        }
                    }
                    if (instance.getParent() != null) {
                        z3 = true;
                    } else {
                        z3 = z2;
                    }
                    if (z3) {
                        arrayList.add(obj);
                    }
                }
                int i3 = 1;
                int i4 = 0;
                List list2 = (List) arrayList;
                if (list2.isEmpty()) {
                    return "Select a union, intersection or negated part to separate.";
                }
                studio.getUndo().begin("Separate");
                Iterator it2 = list2.iterator();
                while (it2.hasNext()) {
                    try {
                        Instance instance2 = (Instance) it2.next();
                        if (Intrinsics.areEqual(instance2.getClassName(), "NegateOperation")) {
                            i = i2;
                            importedUnion2 = importedUnion3;
                            it = it2;
                            list = list2;
                        } else {
                            List<Instance> listSources = INSTANCE.sources(instance2);
                            if (listSources.isEmpty()) {
                                importedUnion3 = true;
                            } else {
                                Result result = INSTANCE.result(instance2);
                                Pair<CFrame, Vector3> pairReference = INSTANCE.reference(listSources);
                                if (pairReference == null) {
                                    i = i2;
                                    importedUnion2 = importedUnion3;
                                    it = it2;
                                    list = list2;
                                } else {
                                    CFrame cFrameComponent1 = pairReference.component1();
                                    Vector3 vector3ScaleOf = result != null ? INSTANCE.scaleOf(instance2, result) : new Vector3(1.0f, 1.0f, 1.0f);
                                    CFrame cframe = PartData.INSTANCE.cframe(instance2);
                                    Instance parent = instance2.getParent();
                                    if (parent == null) {
                                        i = i2;
                                        importedUnion2 = importedUnion3;
                                        it = it2;
                                        list = list2;
                                    } else {
                                        if (result == null || (zero = result.getOffset()) == null) {
                                            zero = Vector3.INSTANCE.getZERO();
                                        }
                                        for (Instance instance3 : listSources) {
                                            int i5 = i2;
                                            importedUnion = importedUnion3;
                                            try {
                                                CFrame cFrameTimes = cFrameComponent1.inverse().times(PartData.INSTANCE.cframe(instance3));
                                                Vector3 vector3Times = cframe.times(cFrameTimes.getPosition().minus(zero).times(vector3ScaleOf));
                                                Iterator it3 = it2;
                                                CFrame cFrameTimes2 = cframe.rotationOnly().times(cFrameTimes.rotationOnly());
                                                Vector3[] vector3Arr = new Vector3[3];
                                                vector3Arr[i4] = cFrameTimes.getRightVector();
                                                vector3Arr[i3] = cFrameTimes.getUpVector();
                                                vector3Arr[2] = cFrameTimes.getBackVector();
                                                Iterable iterableListOf = CollectionsKt.listOf(vector3Arr);
                                                List list3 = list2;
                                                List<Instance> list4 = listSources;
                                                Collection arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterableListOf, 10));
                                                Iterable iterable = iterableListOf;
                                                Iterator it4 = iterable.iterator();
                                                while (it4.hasNext()) {
                                                    arrayList2.add(Float.valueOf(((Vector3) it4.next()).times(vector3ScaleOf).getMagnitude()));
                                                }
                                                List list5 = (List) arrayList2;
                                                Vector3 size = PartData.INSTANCE.size(instance3);
                                                studio.reparent(instance3, parent);
                                                studio.setProp(instance3, "CFrame", cFrameTimes2.withPosition(vector3Times));
                                                studio.setProp(instance3, instance3.getProps().containsKey("size") ? "size" : "Size", new Vector3(size.getX() * ((Number) list5.get(i4)).floatValue(), size.getY() * ((Number) list5.get(i3)).floatValue(), ((Number) list5.get(2)).floatValue() * size.getZ()));
                                                freed.add(instance3);
                                                i2 = i5;
                                                importedUnion3 = importedUnion;
                                                it2 = it3;
                                                list2 = list3;
                                                listSources = list4;
                                                i3 = 1;
                                                i4 = 0;
                                            } catch (Throwable th) {
                                            }
                                        }
                                        i = i2;
                                        importedUnion2 = importedUnion3;
                                        it = it2;
                                        list = list2;
                                        studio.reparent(instance2, null);
                                    }
                                }
                            }
                        }
                        i2 = i;
                        importedUnion3 = importedUnion2;
                        it2 = it;
                        list2 = list;
                        i3 = 1;
                        i4 = 0;
                    } catch (Throwable th2) {
                        Throwable th = th2;
                    }
                }
                importedUnion = importedUnion3;
                studio.getUndo().commit();
                Unit unit = Unit.INSTANCE;
                synchronized (studio.getLock()) {
                    Iterable selection2 = studio.getSelection();
                    Collection arrayList3 = new ArrayList();
                    for (Object obj2 : selection2) {
                        Instance instance4 = (Instance) obj2;
                        if (Intrinsics.areEqual(instance4.getClassName(), "NegateOperation") && instance4.getParent() != null) {
                            arrayList3.add(obj2);
                        }
                    }
                    List list6 = (List) arrayList3;
                    if (!list6.isEmpty()) {
                        studio.getSelection().clear();
                        studio.getSelection().addAll(list6);
                    }
                    Unit unit2 = Unit.INSTANCE;
                }
                synchronized (studio.getLock()) {
                    Iterable selection3 = studio.getSelection();
                    if (!(selection3 instanceof Collection) || !((Collection) selection3).isEmpty()) {
                        Iterator it5 = selection3.iterator();
                        while (true) {
                            if (!it5.hasNext()) {
                                z = false;
                                break;
                            }
                            if (Intrinsics.areEqual(((Instance) it5.next()).getClassName(), "NegateOperation")) {
                                z = true;
                                break;
                            }
                        }
                    } else {
                        z = false;
                    }
                }
                if (z) {
                    negate(studio);
                } else {
                    synchronized (studio.getLock()) {
                        if (!freed.isEmpty()) {
                            studio.getSelection().clear();
                            studio.getSelection().addAll(freed);
                        }
                        Unit unit3 = Unit.INSTANCE;
                    }
                }
                notify(studio);
                if (importedUnion && freed.isEmpty()) {
                    return "That union was made in Roblox Studio; the parts it was made from aren't stored in the file, so it can't be separated here.";
                }
                return null;
            } catch (Throwable th3) {
                throw th3;
            }
        }
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }
}

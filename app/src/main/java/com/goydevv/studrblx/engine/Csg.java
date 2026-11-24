package com.goydevv.studrblx.engine;

import com.goydevv.studrblx.model.CFrame;
import com.goydevv.studrblx.model.Instance;
import com.goydevv.studrblx.model.PartData;
import com.goydevv.studrblx.model.Vector3;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.IntIterator;
import kotlin.collections.MapsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

/* JADX INFO: compiled from: Csg.kt */
/* JADX INFO: loaded from: classes7.dex */
public final class Csg {
    private static final float EPS = 1.0E-4f;
    public static final Csg INSTANCE = new Csg();

    private Csg() {
    }

    /* JADX INFO: compiled from: Csg.kt */
    public static final class Vertex {
        private final Vector3 normal;
        private final Vector3 pos;

        public Vertex(Vector3 pos, Vector3 normal) {
            Intrinsics.checkNotNullParameter(pos, "pos");
            Intrinsics.checkNotNullParameter(normal, "normal");
            this.pos = pos;
            this.normal = normal;
        }

        public final Vector3 getNormal() {
            return this.normal;
        }

        public final Vector3 getPos() {
            return this.pos;
        }

        public final Vertex flipped() {
            return new Vertex(this.pos, this.normal.unaryMinus());
        }

        public final Vertex lerp(Vertex o, float t) {
            Intrinsics.checkNotNullParameter(o, "o");
            return new Vertex(this.pos.lerp(o.pos, t), this.normal.lerp(o.normal, t));
        }
    }

    /* JADX INFO: compiled from: Csg.kt */
    public static final class Plane {

        /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
        public static final Companion INSTANCE = new Companion(null);
        private final Vector3 normal;
        private final float w;

        public Plane(Vector3 normal, float w) {
            Intrinsics.checkNotNullParameter(normal, "normal");
            this.normal = normal;
            this.w = w;
        }

        public final Vector3 getNormal() {
            return this.normal;
        }

        public final float getW() {
            return this.w;
        }

        public final Plane flipped() {
            return new Plane(this.normal.unaryMinus(), -this.w);
        }

        public final void split(Polygon p, List<Polygon> coplanarFront, List<Polygon> coplanarBack, List<Polygon> front, List<Polygon> back) {
            int ty;
            Plane plane = this;
            Intrinsics.checkNotNullParameter(p, "p");
            Intrinsics.checkNotNullParameter(coplanarFront, "coplanarFront");
            Intrinsics.checkNotNullParameter(coplanarBack, "coplanarBack");
            Intrinsics.checkNotNullParameter(front, "front");
            Intrinsics.checkNotNullParameter(back, "back");
            int COPLANAR = 0;
            int FRONT = 1;
            int BACK = 2;
            int type = 0;
            int[] types = new int[p.getVertices().size()];
            Iterator it = p.getVertices().iterator();
            int i = 0;
            while (it.hasNext()) {
                int i2 = i;
                i++;
                int COPLANAR2 = COPLANAR;
                float t = plane.normal.dot(((Vertex) it.next()).getPos()) - plane.w;
                if (t < -1.0E-4f) {
                    ty = 2;
                } else {
                    ty = t > 1.0E-4f ? 1 : COPLANAR2;
                }
                type |= ty;
                types[i2] = ty;
                COPLANAR = COPLANAR2;
            }
            if (type != 0) {
                if (type != 1) {
                    if (type != 2) {
                        ArrayList f = new ArrayList();
                        ArrayList b = new ArrayList();
                        int n = p.getVertices().size();
                        int i3 = 0;
                        while (i3 < n) {
                            int j = (i3 + 1) % n;
                            int ti = types[i3];
                            int tj = types[j];
                            Vertex vi = p.getVertices().get(i3);
                            Vertex vj = p.getVertices().get(j);
                            if (ti != BACK) {
                                f.add(vi);
                            }
                            if (ti != FRONT) {
                                b.add(vi);
                            }
                            int FRONT2 = FRONT;
                            int FRONT3 = ti | tj;
                            if (FRONT3 == 3) {
                                Vertex v = vi.lerp(vj, (plane.w - plane.normal.dot(vi.getPos())) / plane.normal.dot(vj.getPos().minus(vi.getPos())));
                                f.add(v);
                                b.add(v);
                            }
                            i3++;
                            plane = this;
                            FRONT = FRONT2;
                        }
                        if (f.size() >= 3) {
                            front.add(new Polygon(f, p.getShared(), p.getPlane()));
                        }
                        if (b.size() >= 3) {
                            back.add(new Polygon(b, p.getShared(), p.getPlane()));
                            return;
                        }
                        return;
                    }
                    back.add(p);
                    return;
                }
                front.add(p);
                return;
            }
            (plane.normal.dot(p.getPlane().normal) > 0.0f ? coplanarFront : coplanarBack).add(p);
        }

        /* JADX INFO: compiled from: Csg.kt */
        public static final class Companion {
            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            private Companion() {
            }

            public final Plane from(Vector3 a, Vector3 b, Vector3 c) {
                Intrinsics.checkNotNullParameter(a, "a");
                Intrinsics.checkNotNullParameter(b, "b");
                Intrinsics.checkNotNullParameter(c, "c");
                Vector3 n = b.minus(a).cross(c.minus(a));
                if (n.getMagnitude() < 1.0E-9f) {
                    return null;
                }
                Vector3 u = n.getUnit();
                return new Plane(u, u.dot(a));
            }
        }
    }

    /* JADX INFO: compiled from: Csg.kt */
    public static final class Polygon {
        private final Plane plane;
        private final int shared;
        private final List<Vertex> vertices;

        public Polygon(List<Vertex> vertices, int shared, Plane plane) {
            Intrinsics.checkNotNullParameter(vertices, "vertices");
            Intrinsics.checkNotNullParameter(plane, "plane");
            this.vertices = vertices;
            this.shared = shared;
            this.plane = plane;
        }

        public final Plane getPlane() {
            return this.plane;
        }

        public final int getShared() {
            return this.shared;
        }

        public final List<Vertex> getVertices() {
            return this.vertices;
        }

        public final Polygon flipped() {
            Iterable iterableReversed = CollectionsKt.reversed(this.vertices);
            Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterableReversed, 10));
            Iterator it = iterableReversed.iterator();
            while (it.hasNext()) {
                arrayList.add(((Vertex) it.next()).flipped());
            }
            return new Polygon((List) arrayList, this.shared, this.plane.flipped());
        }
    }

    /* JADX INFO: compiled from: Csg.kt */
    public static final class Node {
        private Node back;
        private Node front;
        private Plane plane;
        private ArrayList<Polygon> polygons;

        /* JADX WARN: Multi-variable type inference failed */
        public Node() {
            this(null, 1, (DefaultConstructorMarker) null);
        }

        public Node(List<Polygon> list) {
            this.polygons = new ArrayList<>();
            if (list != null) {
                build(list);
            }
        }

        public /* synthetic */ Node(List list, int i, DefaultConstructorMarker defaultConstructorMarker) {
            this((i & 1) != 0 ? null : list);
        }

        public final Plane getPlane() {
            return this.plane;
        }

        public final void setPlane(Plane plane) {
            this.plane = plane;
        }

        public final Node getFront() {
            return this.front;
        }

        public final void setFront(Node node) {
            this.front = node;
        }

        public final Node getBack() {
            return this.back;
        }

        public final void setBack(Node node) {
            this.back = node;
        }

        public final ArrayList<Polygon> getPolygons() {
            return this.polygons;
        }

        public final void setPolygons(ArrayList<Polygon> arrayList) {
            Intrinsics.checkNotNullParameter(arrayList, "<set-?>");
            this.polygons = arrayList;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public final Node clone() {
            Node node = new Node((List<Polygon>) null, 1, (DefaultConstructorMarker) null);
            node.plane = this.plane;
            Node node2 = this.front;
            node.front = node2 != null ? node2.clone() : null;
            Node node3 = this.back;
            node.back = node3 != null ? node3.clone() : null;
            node.polygons = new ArrayList<>(this.polygons);
            return node;
        }

        public final void invert() {
            int size = this.polygons.size();
            for (int i = 0; i < size; i++) {
                this.polygons.set(i, this.polygons.get(i).flipped());
            }
            Plane plane = this.plane;
            this.plane = plane != null ? plane.flipped() : null;
            Node node = this.front;
            if (node != null) {
                node.invert();
            }
            Node node2 = this.back;
            if (node2 != null) {
                node2.invert();
            }
            Node t = this.front;
            this.front = this.back;
            this.back = t;
        }

        public final List<Polygon> clipPolygons(List<Polygon> polys) {
            List<Polygon> arrayListClipPolygons;
            ArrayList b;
            Intrinsics.checkNotNullParameter(polys, "polys");
            Plane pl = this.plane;
            if (pl == null) {
                return new ArrayList(polys);
            }
            ArrayList f = new ArrayList();
            ArrayList b2 = new ArrayList();
            for (Polygon p : polys) {
                pl.split(p, f, b2, f, b2);
            }
            Node node = this.front;
            if (node == null || (arrayListClipPolygons = node.clipPolygons(f)) == null) {
                arrayListClipPolygons = f;
            }
            ArrayList f2 = new ArrayList(arrayListClipPolygons);
            if (this.back != null) {
                Node node2 = this.back;
                Intrinsics.checkNotNull(node2);
                b = new ArrayList(node2.clipPolygons(b2));
            } else {
                b = new ArrayList();
            }
            f2.addAll(b);
            return f2;
        }

        public final void clipTo(Node bsp) {
            Intrinsics.checkNotNullParameter(bsp, "bsp");
            this.polygons = new ArrayList<>(bsp.clipPolygons(this.polygons));
            Node node = this.front;
            if (node != null) {
                node.clipTo(bsp);
            }
            Node node2 = this.back;
            if (node2 != null) {
                node2.clipTo(bsp);
            }
        }

        public final List<Polygon> allPolygons() {
            ArrayList out = new ArrayList(this.polygons);
            Node node = this.front;
            if (node != null) {
                out.addAll(node.allPolygons());
            }
            Node node2 = this.back;
            if (node2 != null) {
                out.addAll(node2.allPolygons());
            }
            return out;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public final void build(List<Polygon> polys) {
            Intrinsics.checkNotNullParameter(polys, "polys");
            if (polys.isEmpty()) {
                return;
            }
            Node node = this;
            List<Polygon> arrayList = polys;
            while (!arrayList.isEmpty()) {
                if (node.plane == null) {
                    node.plane = arrayList.get(0).getPlane();
                }
                ArrayList arrayList2 = new ArrayList();
                ArrayList arrayList3 = new ArrayList();
                for (Object _polyObj : arrayList) {
                Polygon polygon = (Polygon) _polyObj;
                    Plane plane = node.plane;
                    Intrinsics.checkNotNull(plane);
                    plane.split(polygon, node.polygons, node.polygons, arrayList2, arrayList3);
                }
                if (!arrayList3.isEmpty()) {
                    if (node.back == null) {
                        node.back = new Node(null, 1, null);
                    }
                    Node node2 = node.back;
                    Intrinsics.checkNotNull(node2);
                    node2.build(arrayList3);
                }
                if (arrayList2.isEmpty()) {
                    return;
                }
                if (node.front == null) {
                    node.front = new Node(null, 1, null);
                }
                Node node3 = node.front;
                Intrinsics.checkNotNull(node3);
                node = node3;
                arrayList = arrayList2;
            }
        }
    }

    public final List<Polygon> union(List<Polygon> a, List<Polygon> b) {
        Intrinsics.checkNotNullParameter(a, "a");
        Intrinsics.checkNotNullParameter(b, "b");
        Node na = new Node(a);
        Node nb = new Node(b);
        na.clipTo(nb);
        nb.clipTo(na);
        nb.invert();
        nb.clipTo(na);
        nb.invert();
        na.build(nb.allPolygons());
        return na.allPolygons();
    }

    public final List<Polygon> subtract(List<Polygon> a, List<Polygon> b) {
        Intrinsics.checkNotNullParameter(a, "a");
        Intrinsics.checkNotNullParameter(b, "b");
        Node na = new Node(a);
        Node nb = new Node(b);
        na.invert();
        na.clipTo(nb);
        nb.clipTo(na);
        nb.invert();
        nb.clipTo(na);
        nb.invert();
        na.build(nb.allPolygons());
        na.invert();
        return na.allPolygons();
    }

    public final List<Polygon> intersect(List<Polygon> a, List<Polygon> b) {
        Intrinsics.checkNotNullParameter(a, "a");
        Intrinsics.checkNotNullParameter(b, "b");
        Node na = new Node(a);
        Node nb = new Node(b);
        na.invert();
        nb.clipTo(na);
        nb.invert();
        na.clipTo(nb);
        nb.clipTo(na);
        na.build(nb.allPolygons());
        na.invert();
        return na.allPolygons();
    }

    /* JADX WARN: Multi-variable type inference failed */
    static /* synthetic */ Polygon poly$default(Csg csg, List list, int i, List list2, int i2, Object obj) {
        if ((i2 & 4) != 0) {
            list2 = null;
        }
        return csg.poly(list, i, list2);
    }

    private final Polygon poly(List<Vector3> pts, int shared, List<Vector3> smooth) {
        Vector3 normal;
        Plane pl = Plane.INSTANCE.from(pts.get(0), pts.get(1), pts.get(2));
        if (pl == null) {
            return null;
        }
        List<Vector3> list = pts;
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(list, 10));
        int i = 0;
        for (Object obj : list) {
            int i2 = i + 1;
            if (i < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            Vector3 vector3 = (Vector3) obj;
            if (smooth == null || (normal = smooth.get(i)) == null) {
                normal = pl.getNormal();
            }
            arrayList.add(new Vertex(vector3, normal));
            i = i2;
        }
        return new Polygon((List) arrayList, shared, pl);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ List<Polygon> partPolygons$default(Csg csg, Instance instance, int i, CFrame cFrame, Function1 function1, int i2, Object obj) {
        if ((i2 & 4) != 0) {
            cFrame = null;
        }
        if ((i2 & 8) != 0) {
            function1 = new Function1<Instance, Void>() { // from class: com.goydevv.studrblx.engine.Csg.partPolygons.1
                @Override // kotlin.jvm.functions.Function1
                public final Void invoke(Instance it) {
                    Intrinsics.checkNotNullParameter(it, "it");
                    return null;
                }
            };
        }
        return csg.partPolygons(instance, i, cFrame, function1);
    }

    public final List<Polygon> partPolygons(Instance p, int shared, CFrame toLocal, Function1<? super Instance, ? extends List<Polygon>> cache) {
        float r;
        int rings;
        List pts;
        CFrame cf;
        float r2;
        CFrame cFrameTimes;
        int i = shared;
        Intrinsics.checkNotNullParameter(p, "p");
        Intrinsics.checkNotNullParameter(cache, "cache");
        CFrame cf0 = PartData.INSTANCE.cframe(p);
        CFrame cf2 = (toLocal == null || (cFrameTimes = toLocal.inverse().times(cf0)) == null) ? cf0 : cFrameTimes;
        if (p.isA("PartOperation") && !Intrinsics.areEqual(p.getClassName(), "NegateOperation")) {
            Iterable iterable = (List) cache.invoke(p);
            if (iterable == null) {
                return CollectionsKt.emptyList();
            }
            Iterable iterable2 = iterable;
            Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable2, 10));
            Iterator it = iterable2.iterator();
            while (it.hasNext()) {
                arrayList.add(INSTANCE.transform((Polygon) it.next(), cf2, i));
            }
            return (List) arrayList;
        }
        Vector3 s = PartData.INSTANCE.size(p);
        Vector3 h = s.times(0.5f);
        ArrayList out = new ArrayList();
        switch (PartData.INSTANCE.shape(p)) {
            case 0:
                CFrame cf3 = cf2;
                int i2 = 3;
                float r3 = Math.min(s.getX(), Math.min(s.getY(), s.getZ())) * 0.5f;
                int rings2 = 10;
                int segs = 16;
                int i3 = 0;
                while (i3 < rings2) {
                    int j = 0;
                    while (j < segs) {
                        Vector3 a = partPolygons$pt(rings2, segs, r3, i3, j);
                        Vector3 b = partPolygons$pt(rings2, segs, r3, i3, j + 1);
                        Vector3 c = partPolygons$pt(rings2, segs, r3, i3 + 1, j + 1);
                        Vector3 d = partPolygons$pt(rings2, segs, r3, i3 + 1, j);
                        if (i3 == 0) {
                            r = r3;
                            rings = rings2;
                            Vector3[] vector3Arr = new Vector3[i2];
                            vector3Arr[0] = a;
                            vector3Arr[1] = c;
                            vector3Arr[2] = d;
                            pts = CollectionsKt.listOf(vector3Arr);
                        } else {
                            r = r3;
                            rings = rings2;
                            int i4 = i2;
                            if (i3 == rings - 1) {
                                Vector3[] vector3Arr2 = new Vector3[i4];
                                vector3Arr2[0] = a;
                                vector3Arr2[1] = b;
                                vector3Arr2[2] = c;
                                pts = CollectionsKt.listOf(vector3Arr2);
                            } else {
                                Vector3[] vector3Arr3 = new Vector3[4];
                                vector3Arr3[0] = a;
                                vector3Arr3[1] = b;
                                vector3Arr3[2] = c;
                                vector3Arr3[i4] = d;
                                pts = CollectionsKt.listOf(vector3Arr3);
                            }
                        }
                        List list = pts;
                        List pts2 = pts;
                        int segs2 = segs;
                        int i5 = i3;
                        int i6 = CollectionsKt.collectionSizeOrDefault(list, 10);
                        Collection arrayList2 = new ArrayList(i6);
                        Iterator it2 = list.iterator();
                        while (it2.hasNext()) {
                            arrayList2.add(cf3.times((Vector3) it2.next()));
                        }
                        List world = (List) arrayList2;
                        List list2 = pts2;
                        int j2 = j;
                        int j3 = CollectionsKt.collectionSizeOrDefault(list2, 10);
                        Collection arrayList3 = new ArrayList(j3);
                        Iterator it3 = list2.iterator();
                        while (it3.hasNext()) {
                            arrayList3.add(cf3.vectorToWorld(((Vector3) it3.next()).getUnit()));
                        }
                        List nrms = (List) arrayList3;
                        Plane pl = Plane.INSTANCE.from((Vector3) world.get(0), (Vector3) world.get(1), (Vector3) world.get(2));
                        if (pl == null) {
                            cf = cf3;
                        } else if (pl.getNormal().dot((Vector3) nrms.get(0)) < 0.0f) {
                            List rw = CollectionsKt.reversed(world);
                            List rn = CollectionsKt.reversed(nrms);
                            cf = cf3;
                            Plane planeFrom = Plane.INSTANCE.from((Vector3) rw.get(0), (Vector3) rw.get(1), (Vector3) rw.get(2));
                            if (planeFrom != null) {
                                List list3 = rw;
                                Collection arrayList4 = new ArrayList(CollectionsKt.collectionSizeOrDefault(list3, 10));
                                Iterable iterable3 = list3;
                                int i7 = 0;
                                int i8 = 0;
                                for (Object obj : iterable3) {
                                    int i9 = i8 + 1;
                                    if (i8 < 0) {
                                        CollectionsKt.throwIndexOverflow();
                                    }
                                    arrayList4.add(new Vertex((Vector3) obj, (Vector3) rn.get(i8)));
                                    i8 = i9;
                                }
                                Boolean.valueOf(out.add(new Polygon((List) arrayList4, i, planeFrom)));
                            }
                        } else {
                            cf = cf3;
                            List list4 = world;
                            int i10 = 0;
                            Collection arrayList5 = new ArrayList(CollectionsKt.collectionSizeOrDefault(list4, 10));
                            int i11 = 0;
                            for (Object obj2 : list4) {
                                int i12 = i11 + 1;
                                if (i11 < 0) {
                                    CollectionsKt.throwIndexOverflow();
                                }
                                arrayList5.add(new Vertex((Vector3) obj2, (Vector3) nrms.get(i11)));
                                i11 = i12;
                            }
                            Boolean.valueOf(out.add(new Polygon((List) arrayList5, i, pl)));
                        }
                        j = j2 + 1;
                        r3 = r;
                        rings2 = rings;
                        segs = segs2;
                        i3 = i5;
                        cf3 = cf;
                        i2 = 3;
                    }
                    i3++;
                    i2 = 3;
                }
                Unit unit = Unit.INSTANCE;
                break;
            case 1:
            default:
                CFrame cf4 = cf2;
                Vector3[] c2 = new Vector3[8];
                for (int i13 = 0; i13 < 8; i13++) {
                    c2[i13] = new Vector3((i13 & 1) != 0 ? h.getX() : -h.getX(), (i13 & 2) != 0 ? h.getY() : -h.getY(), (i13 & 4) != 0 ? h.getZ() : -h.getZ());
                }
                addOriented$default(this, out, CollectionsKt.listOf(new Vector3[]{c2[0], c2[2], c2[3], c2[1]}), cf4, i, Vector3.INSTANCE.getZ().unaryMinus(), false, 32, null);
                addOriented$default(this, out, CollectionsKt.listOf(new Vector3[]{c2[4], c2[5], c2[7], c2[6]}), cf4, shared, Vector3.INSTANCE.getZ(), false, 32, null);
                addOriented$default(this, out, CollectionsKt.listOf(new Vector3[]{c2[0], c2[1], c2[5], c2[4]}), cf4, shared, Vector3.INSTANCE.getY().unaryMinus(), false, 32, null);
                addOriented$default(this, out, CollectionsKt.listOf(new Vector3[]{c2[2], c2[6], c2[7], c2[3]}), cf4, shared, Vector3.INSTANCE.getY(), false, 32, null);
                addOriented$default(this, out, CollectionsKt.listOf(new Vector3[]{c2[0], c2[4], c2[6], c2[2]}), cf4, shared, Vector3.INSTANCE.getX().unaryMinus(), false, 32, null);
                addOriented$default(this, out, CollectionsKt.listOf(new Vector3[]{c2[1], c2[3], c2[7], c2[5]}), cf4, shared, Vector3.INSTANCE.getX(), false, 32, null);
                Unit unit2 = Unit.INSTANCE;
                break;
            case 2:
                CFrame cf5 = cf2;
                int i14 = 1;
                float r4 = Math.min(s.getY(), s.getZ()) * 0.5f;
                Iterable iterableUntil = RangesKt.until(0, 20);
                int i15 = 2;
                Collection arrayList6 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterableUntil, 10));
                Iterable iterable4 = iterableUntil;
                Iterator<Integer> it4 = iterable4.iterator();
                while (it4.hasNext()) {
                    Iterable iterable5 = iterable4;
                    double dNextInt = (((double) ((IntIterator) it4).nextInt()) * 6.283185307179586d) / ((double) 20);
                    arrayList6.add(new Vector3(0.0f, (float) (((double) r4) * Math.cos(dNextInt)), (float) (((double) r4) * Math.sin(dNextInt))));
                    iterable4 = iterable5;
                }
                int i16 = i14;
                List ring = (List) arrayList6;
                int k = 0;
                while (k < 20) {
                    Vector3 a2 = (Vector3) ring.get(k);
                    Vector3 b2 = (Vector3) ring.get((k + 1) % 20);
                    Vector3[] vector3Arr4 = new Vector3[4];
                    List ring2 = ring;
                    int k2 = k;
                    vector3Arr4[0] = new Vector3(-h.getX(), a2.getY(), a2.getZ());
                    vector3Arr4[i16] = new Vector3(h.getX(), a2.getY(), a2.getZ());
                    vector3Arr4[i15] = new Vector3(h.getX(), b2.getY(), b2.getZ());
                    vector3Arr4[3] = new Vector3(-h.getX(), b2.getY(), b2.getZ());
                    Iterable pts3 = CollectionsKt.listOf(vector3Arr4);
                    Vector3[] vector3Arr5 = new Vector3[4];
                    vector3Arr5[0] = new Vector3(0.0f, a2.getY(), a2.getZ()).getUnit();
                    vector3Arr5[i16] = new Vector3(0.0f, a2.getY(), a2.getZ()).getUnit();
                    vector3Arr5[i15] = new Vector3(0.0f, b2.getY(), b2.getZ()).getUnit();
                    vector3Arr5[3] = new Vector3(0.0f, b2.getY(), b2.getZ()).getUnit();
                    Iterable nrms2 = CollectionsKt.listOf(vector3Arr5);
                    Iterable iterable6 = pts3;
                    Collection arrayList7 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable6, 10));
                    Iterator it5 = iterable6.iterator();
                    while (it5.hasNext()) {
                        arrayList7.add(cf5.times((Vector3) it5.next()));
                    }
                    List world2 = (List) arrayList7;
                    Iterable iterable7 = nrms2;
                    Collection arrayList8 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable7, 10));
                    Iterator it6 = iterable7.iterator();
                    while (it6.hasNext()) {
                        arrayList8.add(cf5.vectorToWorld((Vector3) it6.next()));
                    }
                    List wn = (List) arrayList8;
                    Plane pl2 = Plane.INSTANCE.from((Vector3) world2.get(0), (Vector3) world2.get(i16), (Vector3) world2.get(i15));
                    if (pl2 == null) {
                        r2 = r4;
                    } else if (pl2.getNormal().dot((Vector3) wn.get(0)) < 0.0f) {
                        List rw2 = CollectionsKt.reversed(world2);
                        List rn2 = CollectionsKt.reversed(wn);
                        r2 = r4;
                        Plane pl3 = Plane.INSTANCE.from((Vector3) rw2.get(0), (Vector3) rw2.get(1), (Vector3) rw2.get(2));
                        if (pl3 != null) {
                            List list5 = rw2;
                            Collection arrayList9 = new ArrayList(CollectionsKt.collectionSizeOrDefault(list5, 10));
                            Iterable iterable8 = list5;
                            int i17 = 0;
                            for (Object obj3 : iterable8) {
                                int i18 = i17 + 1;
                                if (i17 < 0) {
                                    CollectionsKt.throwIndexOverflow();
                                }
                                arrayList9.add(new Vertex((Vector3) obj3, (Vector3) rn2.get(i17)));
                                i17 = i18;
                            }
                            out.add(new Polygon((List) arrayList9, i, pl3));
                        }
                    } else {
                        r2 = r4;
                        List list6 = world2;
                        int i19 = 0;
                        Collection arrayList10 = new ArrayList(CollectionsKt.collectionSizeOrDefault(list6, 10));
                        int i20 = 0;
                        for (Object obj4 : list6) {
                            int i21 = i20 + 1;
                            if (i20 < 0) {
                                CollectionsKt.throwIndexOverflow();
                            }
                            arrayList10.add(new Vertex((Vector3) obj4, (Vector3) wn.get(i20)));
                            i20 = i21;
                        }
                        out.add(new Polygon((List) arrayList10, i, pl2));
                    }
                    k = k2 + 1;
                    ring = ring2;
                    r4 = r2;
                    i15 = 2;
                    i16 = 1;
                }
                Iterable ring3 = ring;
                Iterable iterable9 = ring3;
                int i22 = 0;
                Collection arrayList11 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable9, 10));
                Iterable<Vector3> iterable10 = iterable9;
                int i23 = 0;
                for (Vector3 vector3 : iterable10) {
                    arrayList11.add(new Vector3(h.getX(), vector3.getY(), vector3.getZ()));
                }
                List capR = (List) arrayList11;
                Iterable iterable11 = ring3;
                int i24 = 0;
                Collection arrayList12 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable11, 10));
                Iterable<Vector3> iterable12 = iterable11;
                for (Vector3 vector4 : iterable12) {
                    arrayList12.add(new Vector3(-h.getX(), vector4.getY(), vector4.getZ()));
                }
                List capR2 = capR;
                List capL = CollectionsKt.reversed((List) arrayList12);
                List list7 = capR2;
                Collection arrayList13 = new ArrayList(CollectionsKt.collectionSizeOrDefault(list7, 10));
                Iterator it7 = list7.iterator();
                while (it7.hasNext()) {
                    arrayList13.add(cf5.times((Vector3) it7.next()));
                }
                List capL2 = capL;
                Polygon polygonPoly$default = poly$default(this, (List) arrayList13, shared, null, 4, null);
                if (polygonPoly$default != null) {
                    Boolean.valueOf(out.add(INSTANCE.orient(polygonPoly$default, cf5.vectorToWorld(Vector3.INSTANCE.getX()))));
                }
                List list8 = capL2;
                Collection arrayList14 = new ArrayList(CollectionsKt.collectionSizeOrDefault(list8, 10));
                Iterator it8 = list8.iterator();
                while (it8.hasNext()) {
                    arrayList14.add(cf5.times((Vector3) it8.next()));
                }
                Polygon polygonPoly$default2 = poly$default(this, (List) arrayList14, shared, null, 4, null);
                if (polygonPoly$default2 != null) {
                    Boolean.valueOf(out.add(INSTANCE.orient(polygonPoly$default2, cf5.vectorToWorld(Vector3.INSTANCE.getX().unaryMinus()))));
                }
                break;
            case 3:
                Vector3 bl = new Vector3(-h.getX(), -h.getY(), -h.getZ());
                Vector3 br = new Vector3(h.getX(), -h.getY(), -h.getZ());
                Vector3 blb = new Vector3(-h.getX(), -h.getY(), h.getZ());
                Vector3 brb = new Vector3(h.getX(), -h.getY(), h.getZ());
                Vector3 tl = new Vector3(-h.getX(), h.getY(), h.getZ());
                Vector3 tr = new Vector3(h.getX(), h.getY(), h.getZ());
                addOriented$default(this, out, CollectionsKt.listOf(new Vector3[]{bl, br, brb, blb}), cf2, shared, new Vector3(0.0f, -1.0f, 0.0f), false, 32, null);
                addOriented$default(this, out, CollectionsKt.listOf(new Vector3[]{blb, brb, tr, tl}), cf2, shared, Vector3.INSTANCE.getZ(), false, 32, null);
                addOriented$default(this, out, CollectionsKt.listOf(new Vector3[]{bl, br, tr, tl}), cf2, shared, new Vector3(0.0f, s.getZ(), -s.getY()).getUnit(), false, 32, null);
                addOriented$default(this, out, CollectionsKt.listOf(new Vector3[]{bl, blb, tl}), cf2, shared, Vector3.INSTANCE.getX().unaryMinus(), false, 32, null);
                addOriented$default(this, out, CollectionsKt.listOf(new Vector3[]{br, brb, tr}), cf2, shared, Vector3.INSTANCE.getX(), false, 32, null);
                Unit unit3 = Unit.INSTANCE;
                break;
            case 4:
                Vector3 a3 = new Vector3(h.getX(), h.getY(), -h.getZ());
                Vector3 p0 = new Vector3(-h.getX(), -h.getY(), -h.getZ());
                Vector3 p1 = new Vector3(h.getX(), -h.getY(), -h.getZ());
                Vector3 p2 = new Vector3(h.getX(), -h.getY(), h.getZ());
                Vector3 p3 = new Vector3(-h.getX(), -h.getY(), h.getZ());
                Vector3 c3 = new Vector3(h.getX() * 0.4f, (-h.getY()) * 0.5f, (-h.getZ()) * 0.4f);
                for (List<Vector3> list9 : CollectionsKt.listOf(new List[]{CollectionsKt.listOf(new Vector3[]{p0, p1, p2, p3}), CollectionsKt.listOf(new Vector3[]{p1, p2, a3}), CollectionsKt.listOf(new Vector3[]{p0, p1, a3}), CollectionsKt.listOf(new Vector3[]{p0, p3, a3}), CollectionsKt.listOf(new Vector3[]{p3, p2, a3})})) {
                    List<Vector3> list10 = list9;
                    Vector3 zero = Vector3.INSTANCE.getZERO();
                    Iterator it9 = list10.iterator();
                    while (it9.hasNext()) {
                        zero = zero.plus((Vector3) it9.next());
                    }
                    Vector3 mid = zero.div(list9.size());
                    addOriented(out, list9, cf2, i, mid.minus(c3).getUnit(), true);
                    i = shared;
                }
                Unit unit4 = Unit.INSTANCE;
                break;
        }
        return out;
    }

    private static final void partPolygons$add(int $shared, CFrame cf, ArrayList<Polygon> arrayList, Vector3... pts) {
        Csg csg = INSTANCE;
        Collection arrayList2 = new ArrayList(pts.length);
        for (Vector3 vector3 : pts) {
            arrayList2.add(cf.times(vector3));
        }
        Polygon polygonPoly$default = poly$default(csg, (List) arrayList2, $shared, null, 4, null);
        if (polygonPoly$default != null) {
            arrayList.add(polygonPoly$default);
        }
    }

    private static final Vector3 partPolygons$pt(int rings, int segs, float r, int i, int j) {
        double th = (((double) i) * 3.141592653589793d) / ((double) rings);
        double ph = (((double) j) * 6.283185307179586d) / ((double) segs);
        return new Vector3(((float) (Math.sin(th) * Math.cos(ph))) * r, ((float) Math.cos(th)) * r, ((float) (Math.sin(th) * Math.sin(ph))) * r);
    }

    private final Polygon orient(Polygon p, Vector3 outward) {
        return p.getPlane().getNormal().dot(outward) < 0.0f ? p.flipped() : p;
    }

    static /* synthetic */ void addOriented$default(Csg csg, List list, List list2, CFrame cFrame, int i, Vector3 vector3, boolean z, int i2, Object obj) {
        if ((i2 & 32) != 0) {
            z = false;
        }
        csg.addOriented(list, list2, cFrame, i, vector3, z);
    }

    private final void addOriented(List<Polygon> out, List<Vector3> local, CFrame cf, int shared, Vector3 outwardLocal, boolean approximate) {
        List<Vector3> list = local;
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(list, 10));
        Iterator it = list.iterator();
        while (it.hasNext()) {
            arrayList.add(cf.times((Vector3) it.next()));
        }
        Polygon p = poly$default(this, (List) arrayList, shared, null, 4, null);
        if (p == null) {
            return;
        }
        Vector3 o = cf.vectorToWorld(outwardLocal);
        out.add((p.getPlane().getNormal().dot(o) < 0.0f || (approximate && p.getPlane().getNormal().dot(o) < 0.0f)) ? p.flipped() : p);
    }

    private final Polygon transform(Polygon p, CFrame cf, int shared) {
        Iterable<Vertex> vertices = p.getVertices();
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(vertices, 10));
        for (Vertex vertex : vertices) {
            arrayList.add(new Vertex(cf.times(vertex.getPos()), cf.vectorToWorld(vertex.getNormal()).getUnit()));
        }
        List vs = (List) arrayList;
        Plane pl = Plane.INSTANCE.from(((Vertex) vs.get(0)).getPos(), ((Vertex) vs.get(1)).getPos(), ((Vertex) vs.get(2)).getPos());
        if (pl == null) {
            pl = p.getPlane();
        }
        return new Polygon(vs, shared, pl);
    }

    public final Map<Integer, float[]> toTriangles(List<Polygon> polys) {
        Object arrayList;
        Intrinsics.checkNotNullParameter(polys, "polys");
        Map groups = new HashMap();
        for (Polygon p : polys) {
            Map map = groups;
            Integer numValueOf = Integer.valueOf(p.getShared());
            Object obj = map.get(numValueOf);
            if (obj == null) {
                arrayList = new ArrayList();
                map.put(numValueOf, arrayList);
            } else {
                arrayList = (ArrayList) obj;
            }
            ArrayList l = (ArrayList) arrayList;
            List<Vertex> vertices = p.getVertices();
            Vector3 n = p.getPlane().getNormal();
            Iterator it = vertices.iterator();
            double dDot = 0.0d;
            while (it.hasNext()) {
                dDot += (double) ((Vertex) it.next()).getNormal().dot(n);
            }
            double dAbs = Math.abs((dDot / ((double) vertices.size())) - 1.0d);
            int i = 0;
            boolean flat = dAbs < 0.001d;
            int i2 = 1;
            int size = vertices.size() - 1;
            while (i2 < size) {
                Vertex[] vertexArr = new Vertex[3];
                vertexArr[i] = vertices.get(i);
                vertexArr[1] = vertices.get(i2);
                vertexArr[2] = vertices.get(i2 + 1);
                for (Vertex q : CollectionsKt.listOf(vertexArr)) {
                    Vector3 nn = flat ? n : q.getNormal();
                    l.add(Float.valueOf(q.getPos().getX()));
                    l.add(Float.valueOf(q.getPos().getY()));
                    l.add(Float.valueOf(q.getPos().getZ()));
                    l.add(Float.valueOf(nn.getX()));
                    l.add(Float.valueOf(nn.getY()));
                    l.add(Float.valueOf(nn.getZ()));
                    Vector3 a = n.abs();
                    if (a.getY() >= a.getX() && a.getY() >= a.getZ()) {
                        l.add(Float.valueOf(q.getPos().getX()));
                        l.add(Float.valueOf(q.getPos().getZ()));
                    } else if (a.getX() >= a.getZ()) {
                        l.add(Float.valueOf(q.getPos().getZ()));
                        l.add(Float.valueOf(q.getPos().getY()));
                    } else {
                        l.add(Float.valueOf(q.getPos().getX()));
                        l.add(Float.valueOf(q.getPos().getY()));
                    }
                }
                i2++;
                i = 0;
            }
        }
        Map map2 = groups;
        Map linkedHashMap = new LinkedHashMap(MapsKt.mapCapacity(map2.size()));
        for (Object obj2 : map2.entrySet()) {
            linkedHashMap.put(((Map.Entry) obj2).getKey(), CollectionsKt.toFloatArray((Collection) ((Map.Entry) obj2).getValue()));
        }
        return linkedHashMap;
    }
}

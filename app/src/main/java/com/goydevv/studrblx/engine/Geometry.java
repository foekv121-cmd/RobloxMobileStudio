package com.goydevv.studrblx.engine;

import com.goydevv.studrblx.model.CFrame;
import com.goydevv.studrblx.model.EnumValue;
import com.goydevv.studrblx.model.Instance;
import com.goydevv.studrblx.model.PartData;
import com.goydevv.studrblx.model.Vector3;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.IntIterator;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

/* JADX INFO: compiled from: Geometry.kt */
/* JADX INFO: loaded from: classes7.dex */
public final class Geometry {
    private static final List<Vector3> ROUND_AXES;
    public static final Geometry INSTANCE = new Geometry();
    private static final float H = 0.5f;
    private static final ThreadLocal<float[]> SAT_SCRATCH = new ThreadLocal<float[]>() {
        @Override
        protected float[] initialValue() {
            return new float[4];
        }
    };

    private Geometry() {
    }

    static {
        Iterable iterableListOf = CollectionsKt.listOf(new Vector3[]{new Vector3(1.0f, 0.0f, 0.0f), new Vector3(0.0f, 1.0f, 0.0f), new Vector3(0.0f, 0.0f, 1.0f), new Vector3(1.0f, 1.0f, 0.0f), new Vector3(1.0f, -1.0f, 0.0f), new Vector3(1.0f, 0.0f, 1.0f), new Vector3(1.0f, 0.0f, -1.0f), new Vector3(0.0f, 1.0f, 1.0f), new Vector3(0.0f, 1.0f, -1.0f), new Vector3(1.0f, 1.0f, 1.0f), new Vector3(1.0f, 1.0f, -1.0f), new Vector3(1.0f, -1.0f, 1.0f), new Vector3(-1.0f, 1.0f, 1.0f)});
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterableListOf, 10));
        Iterator it = iterableListOf.iterator();
        while (it.hasNext()) {
            arrayList.add(((Vector3) it.next()).getUnit());
        }
        ROUND_AXES = (List) arrayList;
    }

    private final List<Pair<Vector3, Float>> planes(int shape, Vector3 s) {
        float hx = s.getX() * H;
        float hy = s.getY() * H;
        float hz = s.getZ() * H;
        List<Pair<Vector3, Float>> listListOf = CollectionsKt.listOf(new Pair[]{TuplesKt.to(Vector3.INSTANCE.getX(), Float.valueOf(hx)), TuplesKt.to(Vector3.INSTANCE.getX().unaryMinus(), Float.valueOf(hx)), TuplesKt.to(Vector3.INSTANCE.getY(), Float.valueOf(hy)), TuplesKt.to(Vector3.INSTANCE.getY().unaryMinus(), Float.valueOf(hy)), TuplesKt.to(Vector3.INSTANCE.getZ(), Float.valueOf(hz)), TuplesKt.to(Vector3.INSTANCE.getZ().unaryMinus(), Float.valueOf(hz))});
        if (shape == 3) {
            Vector3 n = new Vector3(0.0f, s.getZ(), -s.getY()).getUnit();
            return CollectionsKt.listOf(new Pair[]{TuplesKt.to(Vector3.INSTANCE.getX(), Float.valueOf(hx)), TuplesKt.to(Vector3.INSTANCE.getX().unaryMinus(), Float.valueOf(hx)), TuplesKt.to(Vector3.INSTANCE.getY().unaryMinus(), Float.valueOf(hy)), TuplesKt.to(Vector3.INSTANCE.getZ(), Float.valueOf(hz)), TuplesKt.to(n, Float.valueOf(0.0f))});
        }
        if (shape == 4) {
            Vector3 leftSlopeN = new Vector3(-s.getY(), s.getX(), 0.0f).getUnit();
            Vector3 backSlopeN = new Vector3(0.0f, s.getZ(), s.getY()).getUnit();
            return CollectionsKt.listOf(new Pair[]{
                TuplesKt.to(Vector3.INSTANCE.getX(), Float.valueOf(hx)),
                TuplesKt.to(Vector3.INSTANCE.getY().unaryMinus(), Float.valueOf(hy)),
                TuplesKt.to(Vector3.INSTANCE.getZ().unaryMinus(), Float.valueOf(hz)),
                TuplesKt.to(leftSlopeN, Float.valueOf(0.0f)),
                TuplesKt.to(backSlopeN, Float.valueOf(0.0f))
            });
        }
        return listListOf;
    }

    public final Pair<Float, Vector3> rayPart(Vector3 o, Vector3 d, Instance part) {
        CollisionMeshes.Mesh meshForPart$default;
        Intrinsics.checkNotNullParameter(o, "o");
        Intrinsics.checkNotNullParameter(d, "d");
        Intrinsics.checkNotNullParameter(part, "part");
        if (CollisionMeshes.INSTANCE.hasGeometry(part) && (meshForPart$default = (android.os.Looper.getMainLooper().isCurrentThread() ? CollisionMeshes.INSTANCE.getCached(part) : CollisionMeshes.forPart$default(CollisionMeshes.INSTANCE, part, false, 2, null))) != null) {
            return CollisionMeshes.INSTANCE.ray(meshForPart$default, o, d, 1000000.0f);
        }
        CFrame cf = PartData.INSTANCE.cframe(part);
        Vector3 size = PartData.INSTANCE.size(part);
        int shape = PartData.INSTANCE.shape(part);
        Vector3 lo = cf.pointToObject(o);
        Vector3 ld = cf.vectorToObject(d);
        if (shape == 0) {
            float r = Math.min(size.getX(), Math.min(size.getY(), size.getZ())) * H;
            float b = lo.dot(ld);
            float c = lo.dot(lo) - (r * r);
            float disc = (b * b) - c;
            if (disc < 0.0f) {
                return null;
            }
            float sq = (float) Math.sqrt(disc);
            float t = (-b) - sq;
            if (t < 0.0f) {
                t = (-b) + sq;
            }
            if (t < 0.0f) {
                return null;
            }
            Vector3 hitLocal = lo.plus(ld.times(t));
            return TuplesKt.to(Float.valueOf(t), cf.vectorToWorld(hitLocal.getUnit()));
        }
        if (shape == 2) {
            float hx = size.getX() * H;
            float r = Math.min(size.getY(), size.getZ()) * H;
            float tEnterCyl = -3.4028235E38f;
            float tExitCyl = Float.MAX_VALUE;
            Vector3 enterNCyl = Vector3.INSTANCE.getX();
            if (Math.abs(ld.getX()) < 1.0E-8f) {
                if (lo.getX() < -hx || lo.getX() > hx) {
                    return null;
                }
            } else {
                float invDx = 1.0f / ld.getX();
                float t0 = (-hx - lo.getX()) * invDx;
                float t1 = (hx - lo.getX()) * invDx;
                Vector3 n0 = Vector3.INSTANCE.getX().unaryMinus();
                Vector3 n1 = Vector3.INSTANCE.getX();
                if (t0 > t1) {
                    float tmpT = t0;
                    t0 = t1;
                    t1 = tmpT;
                    Vector3 tmpN = n0;
                    n0 = n1;
                    n1 = tmpN;
                }
                if (t0 > tEnterCyl) {
                    tEnterCyl = t0;
                    enterNCyl = n0;
                }
                if (t1 < tExitCyl) {
                    tExitCyl = t1;
                }
                if (tEnterCyl > tExitCyl) {
                    return null;
                }
            }
            float a = (ld.getY() * ld.getY()) + (ld.getZ() * ld.getZ());
            float b = (lo.getY() * ld.getY()) + (lo.getZ() * ld.getZ());
            float c = (lo.getY() * lo.getY()) + (lo.getZ() * lo.getZ()) - (r * r);
            if (a < 1.0E-12f) {
                if (c > 0.0f) {
                    return null;
                }
            } else {
                float disc = (b * b) - (a * c);
                if (disc < 0.0f) {
                    return null;
                }
                float sq = (float) Math.sqrt(disc);
                float c0 = (-b - sq) / a;
                float c1 = (-b + sq) / a;
                if (c0 > tEnterCyl) {
                    tEnterCyl = c0;
                    enterNCyl = new Vector3(0.0f, lo.getY() + (ld.getY() * c0), lo.getZ() + (ld.getZ() * c0)).getUnit();
                }
                if (c1 < tExitCyl) {
                    tExitCyl = c1;
                }
                if (tEnterCyl > tExitCyl) {
                    return null;
                }
            }
            if (tExitCyl < 0.0f) {
                return null;
            }
            float tHit = tEnterCyl >= 0.0f ? tEnterCyl : 0.0f;
            return TuplesKt.to(Float.valueOf(tHit), cf.vectorToWorld(enterNCyl));
        }
        float tEnter = -3.4028235E38f;
        float tExit = Float.MAX_VALUE;
        Vector3 enterN = Vector3.INSTANCE.getY();
        for (Pair<Vector3, Float> pair : planes(shape, size)) {
            Vector3 n = pair.component1();
            float off = pair.component2().floatValue();
            float denom = n.dot(ld);
            float dist = off - n.dot(lo);
            if (Math.abs(denom) >= 1.0E-8f) {
                float t2 = dist / denom;
                if (denom < 0.0f) {
                    if (t2 > tEnter) {
                        tEnter = t2;
                        enterN = n;
                    }
                } else if (t2 < tExit) {
                    tExit = t2;
                }
                if (tEnter > tExit) {
                    return null;
                }
            } else if (dist < 0.0f) {
                return null;
            }
        }
        if (tExit < 0.0f) {
            return null;
        }
        return TuplesKt.to(Float.valueOf(tEnter >= 0.0f ? tEnter : 0.0f), cf.vectorToWorld(enterN));
    }

    public final RayHit raycast(List<? extends Instance> parts, Vector3 o, Vector3 d, float maxDist, Function1<? super Instance, Boolean> filter) {
        Pair<Float, Vector3> pairRayPart;
        Intrinsics.checkNotNullParameter(parts, "parts");
        Intrinsics.checkNotNullParameter(o, "o");
        Intrinsics.checkNotNullParameter(d, "d");
        Intrinsics.checkNotNullParameter(filter, "filter");
        RayHit best = null;
        for (Instance p : parts) {
            if (filter.invoke(p).booleanValue() && (pairRayPart = rayPart(o, d, p)) != null && pairRayPart.getFirst().floatValue() <= maxDist && (best == null || pairRayPart.getFirst().floatValue() < best.getT())) {
                best = new RayHit(p, pairRayPart.getFirst().floatValue(), o.plus(d.times(pairRayPart.getFirst().floatValue())), pairRayPart.getSecond());
            }
        }
        return best;
    }

    public final List<Vector3> corners(CFrame cf, Vector3 size) {
        Intrinsics.checkNotNullParameter(cf, "cf");
        Intrinsics.checkNotNullParameter(size, "size");
        Vector3 h = size.times(H);
        ArrayList arrayList = new ArrayList(8);
        for (int i = 0; i < 8; i++) {
            int i2 = i;
            arrayList.add(cf.times(new Vector3((i2 & 1) != 0 ? h.getX() : -h.getX(), (i2 & 2) != 0 ? h.getY() : -h.getY(), (i2 & 4) != 0 ? h.getZ() : -h.getZ())));
        }
        return arrayList;
    }

    public final Pair<Vector3, Vector3> worldAabb(CFrame cf, Vector3 size) {
        Intrinsics.checkNotNullParameter(cf, "cf");
        Intrinsics.checkNotNullParameter(size, "size");
        Vector3 h = size.times(H);
        Vector3 ex = new Vector3((Math.abs(cf.getR00()) * h.getX()) + (Math.abs(cf.getR01()) * h.getY()) + (Math.abs(cf.getR02()) * h.getZ()), (Math.abs(cf.getR10()) * h.getX()) + (Math.abs(cf.getR11()) * h.getY()) + (Math.abs(cf.getR12()) * h.getZ()), (Math.abs(cf.getR20()) * h.getX()) + (Math.abs(cf.getR21()) * h.getY()) + (Math.abs(cf.getR22()) * h.getZ()));
        Vector3 p = cf.getPosition();
        return TuplesKt.to(p.minus(ex), p.plus(ex));
    }

    public final Pair<Vector3, Vector3> partAabb(Instance p) {
        Intrinsics.checkNotNullParameter(p, "p");
        return worldAabb(PartData.INSTANCE.cframe(p), PartData.INSTANCE.size(p));
    }

    public final boolean partAabbOverlap(Instance p, Vector3 a, Vector3 b, float pad) {
        Intrinsics.checkNotNullParameter(p, "p");
        Intrinsics.checkNotNullParameter(a, "a");
        Intrinsics.checkNotNullParameter(b, "b");
        CFrame cf = PartData.INSTANCE.cframe(p);
        Vector3 size = PartData.INSTANCE.size(p);
        float hx = size.getX() * H;
        float hy = size.getY() * H;
        float hz = size.getZ() * H;
        float ex = (Math.abs(cf.getR00()) * hx) + (Math.abs(cf.getR01()) * hy) + (Math.abs(cf.getR02()) * hz);
        float ey = (Math.abs(cf.getR10()) * hx) + (Math.abs(cf.getR11()) * hy) + (Math.abs(cf.getR12()) * hz);
        float ez = (Math.abs(cf.getR20()) * hx) + (Math.abs(cf.getR21()) * hy) + (Math.abs(cf.getR22()) * hz);
        float px = cf.getX();
        float py = cf.getY();
        float pz = cf.getZ();
        return a.getX() <= px + ex + pad && a.getY() <= py + ey + pad && a.getZ() <= pz + ez + pad
            && b.getX() >= px - ex - pad && b.getY() >= py - ey - pad && b.getZ() >= pz - ez - pad;
    }

    public final Pair<Vector3, Vector3> boundsOf(List<? extends Instance> parts) {
        Intrinsics.checkNotNullParameter(parts, "parts");
        if (parts.isEmpty()) {
            return null;
        }
        Vector3 mn = new Vector3(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
        Vector3 mx = mn.unaryMinus();
        for (Instance p : parts) {
            Pair<Vector3, Vector3> pairPartAabb = partAabb(p);
            Vector3 a = pairPartAabb.component1();
            Vector3 b = pairPartAabb.component2();
            mn = mn.min(a);
            mx = mx.max(b);
        }
        return TuplesKt.to(mn, mx);
    }

    public final Pair<Vector3, Float> contact(ConvexShape shape, Vector3 lo, Vector3 hi, Instance part) {
        CollisionMeshes.Mesh meshForPart$default;
        Intrinsics.checkNotNullParameter(shape, "shape");
        Intrinsics.checkNotNullParameter(lo, "lo");
        Intrinsics.checkNotNullParameter(hi, "hi");
        Intrinsics.checkNotNullParameter(part, "part");
        if (CollisionMeshes.INSTANCE.hasGeometry(part) && (meshForPart$default = CollisionMeshes.forPart$default(CollisionMeshes.INSTANCE, part, false, 2, null)) != null) {
            return CollisionMeshes.INSTANCE.push(meshForPart$default, shape, lo, hi);
        }
        Vector3 m = sat(shape, shapeOf$default(this, part, null, 2, null));
        if (m == null) {
            return null;
        }
        return TuplesKt.to(m, Float.valueOf(partAabb(part).getSecond().getY()));
    }

    public static /* synthetic */ ConvexShape shapeOf$default(Geometry geometry, Instance instance, CFrame cFrame, int i, Object obj) {
        if ((i & 2) != 0) {
            cFrame = null;
        }
        return geometry.shapeOf(instance, cFrame);
    }

    public final ConvexShape shapeOf(Instance part, CFrame cfOverride) {
        Intrinsics.checkNotNullParameter(part, "part");
        CFrame cf = cfOverride == null ? PartData.INSTANCE.cframe(part) : cfOverride;
        Vector3 s = PartData.INSTANCE.size(part);
        Vector3 h = s.times(H);
        int i = 0;
        List ax = CollectionsKt.listOf(new Vector3[]{cf.getRightVector(), cf.getUpVector(), cf.getBackVector()});
        int shape = PartData.INSTANCE.shape(part);
        if (shape == 0) {
            Vector3 position = cf.getPosition();
            float fMin = Math.min(s.getX(), Math.min(s.getY(), s.getZ())) * H;
            Iterable iterable = ROUND_AXES;
            Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
            Iterator it = iterable.iterator();
            while (it.hasNext()) {
                arrayList.add(cf.vectorToWorld((Vector3) it.next()));
            }
            return new SphereShape(position, fMin, (List) arrayList);
        }
        if (shape != 2) {
            if (shape == 3) {
                Iterable local = CollectionsKt.listOf(new Vector3[]{new Vector3(-h.getX(), -h.getY(), -h.getZ()), new Vector3(h.getX(), -h.getY(), -h.getZ()), new Vector3(-h.getX(), -h.getY(), h.getZ()), new Vector3(h.getX(), -h.getY(), h.getZ()), new Vector3(-h.getX(), h.getY(), h.getZ()), new Vector3(h.getX(), h.getY(), h.getZ())});
                Vector3 slopeN = cf.vectorToWorld(new Vector3(0.0f, s.getZ(), -s.getY()).getUnit());
                Vector3 slopeD = cf.vectorToWorld(new Vector3(0.0f, s.getY(), s.getZ()).getUnit());
                Iterable iterable2 = local;
                Collection arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable2, 10));
                Iterator it2 = iterable2.iterator();
                while (it2.hasNext()) {
                    arrayList2.add(cf.times((Vector3) it2.next()));
                }
                return new ConvexShape((List) arrayList2, CollectionsKt.plus((Collection<? extends Vector3>) ax, slopeN), CollectionsKt.plus((Collection<? extends Vector3>) ax, slopeD));
            } else if (shape == 4) {
                float hx = h.getX();
                float hy = h.getY();
                float hz = h.getZ();
                List<Vector3> cwVerts = CollectionsKt.listOf(new Vector3[]{
                    cf.times(new Vector3(-hx, -hy, -hz)),
                    cf.times(new Vector3(hx, -hy, -hz)),
                    cf.times(new Vector3(hx, -hy, hz)),
                    cf.times(new Vector3(-hx, -hy, hz)),
                    cf.times(new Vector3(hx, hy, -hz))
                });
                List<Vector3> cwNormals = CollectionsKt.listOf(new Vector3[]{
                    cf.getRightVector(),
                    cf.getUpVector(),
                    cf.getBackVector(),
                    cf.vectorToWorld(new Vector3(-s.getY(), s.getX(), 0.0f).getUnit()),
                    cf.vectorToWorld(new Vector3(0.0f, s.getZ(), s.getY()).getUnit())
                });
                List<Vector3> cwEdges = CollectionsKt.listOf(new Vector3[]{
                    cf.getRightVector(),
                    cf.getUpVector(),
                    cf.getBackVector(),
                    cf.vectorToWorld(new Vector3(s.getX(), s.getY(), 0.0f).getUnit()),
                    cf.vectorToWorld(new Vector3(0.0f, s.getY(), -s.getZ()).getUnit()),
                    cf.vectorToWorld(new Vector3(s.getX(), s.getY(), -s.getZ()).getUnit())
                });
                return new ConvexShape(cwVerts, cwNormals, cwEdges);
            }
            return new ConvexShape(corners(cf, s), ax, ax);
        }
        float r = Math.min(s.getY(), s.getZ()) * H;
        char c = '\f';
        Iterable iterableUntil = RangesKt.until(0, 12);
        Collection arrayList3 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterableUntil, 10));
        Iterator<Integer> it3 = iterableUntil.iterator();
        while (it3.hasNext()) {
            double dNextInt = (((double) ((IntIterator) it3).nextInt()) * 6.283185307179586d) / 12.0d;
            arrayList3.add(new Vector3(0.0f, (float) (((double) r) * Math.cos(dNextInt)), (float) (((double) r) * Math.sin(dNextInt))));
        }
        int i2 = i;
        List ring = (List) arrayList3;
        List<Vector3> list = ring;
        Collection arrayList4 = new ArrayList();
        for (Vector3 vector3 : list) {
            Vector3 h2 = h;
            Vector3[] vector3Arr = new Vector3[2];
            vector3Arr[i2] = cf.times(new Vector3(-h2.getX(), vector3.getY(), vector3.getZ()));
            vector3Arr[1] = cf.times(new Vector3(h2.getX(), vector3.getY(), vector3.getZ()));
            CollectionsKt.addAll(arrayList4, CollectionsKt.listOf(vector3Arr));
            h = h2;
        }
        List verts = (List) arrayList4;
        Iterable iterableUntil2 = RangesKt.until(i2, 6);
        Collection arrayList5 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterableUntil2, 10));
        Iterable iterable3 = iterableUntil2;
        int i3 = 0;
        for (Iterator<Integer> it4 = iterable3.iterator(); it4.hasNext(); it4 = it4) {
            double dNextInt2 = ((((double) ((IntIterator) it4).nextInt()) + 0.5d) * 6.283185307179586d) / 12.0d;
            arrayList5.add(cf.vectorToWorld(new Vector3(0.0f, (float) Math.cos(dNextInt2), (float) Math.sin(dNextInt2))));
        }
        List sideN = (List) arrayList5;
        Iterable iterableUntil3 = RangesKt.until(0, 6);
        Collection arrayList6 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterableUntil3, 10));
        Iterator<Integer> it5 = iterableUntil3.iterator();
        while (it5.hasNext()) {
            int iNextInt = ((IntIterator) it5).nextInt();
            arrayList6.add(cf.vectorToWorld(((Vector3) ring.get((iNextInt + 1) % 12)).minus((Vector3) ring.get(iNextInt))).getUnit());
        }
        List sideE = (List) arrayList6;
        return new ConvexShape(verts, CollectionsKt.plus((Collection) CollectionsKt.listOf(cf.getRightVector()), (Iterable) sideN), CollectionsKt.plus((Collection) CollectionsKt.listOf(cf.getRightVector()), (Iterable) sideE));
    }

    public final ConvexShape aabbShape(Vector3 center, Vector3 half) {
        Intrinsics.checkNotNullParameter(center, "center");
        Intrinsics.checkNotNullParameter(half, "half");
        ArrayList arrayList = new ArrayList(8);
        for (int i = 0; i < 8; i++) {
            int i2 = i;
            arrayList.add(center.plus(new Vector3((i2 & 1) != 0 ? half.getX() : -half.getX(), (i2 & 2) != 0 ? half.getY() : -half.getY(), (i2 & 4) != 0 ? half.getZ() : -half.getZ())));
        }
        ArrayList v = arrayList;
        List ax = CollectionsKt.listOf(new Vector3[]{Vector3.INSTANCE.getX(), Vector3.INSTANCE.getY(), Vector3.INSTANCE.getZ()});
        return new ConvexShape(v, ax, ax);
    }

    public final Vector3 sat(ConvexShape a, ConvexShape b) {
        Intrinsics.checkNotNullParameter(a, "a");
        Intrinsics.checkNotNullParameter(b, "b");
        float[] proj = SAT_SCRATCH.get();
        float best = Float.MAX_VALUE;
        float bestX = 0.0f;
        float bestY = 1.0f;
        float bestZ = 0.0f;
        List<Vector3> aNormals = a.getNormals();
        for (int i = 0, n = aNormals.size(); i < n; i++) {
            Vector3 ax = aNormals.get(i);
            float axx = ax.getX();
            float axy = ax.getY();
            float axz = ax.getZ();
            a.project(axx, axy, axz, proj);
            float a0 = proj[0];
            float a1 = proj[1];
            b.project(axx, axy, axz, proj);
            float b0 = proj[0];
            float b1 = proj[1];
            float o = Math.min(a1 - b0, b1 - a0);
            if (o <= 0.0f) {
                return null;
            }
            if (o < best) {
                best = o;
                bestX = axx;
                bestY = axy;
                bestZ = axz;
            }
        }
        List<Vector3> bNormals = b.getNormals();
        for (int i = 0, n = bNormals.size(); i < n; i++) {
            Vector3 ax = bNormals.get(i);
            float axx = ax.getX();
            float axy = ax.getY();
            float axz = ax.getZ();
            a.project(axx, axy, axz, proj);
            float a0 = proj[0];
            float a1 = proj[1];
            b.project(axx, axy, axz, proj);
            float b0 = proj[0];
            float b1 = proj[1];
            float o = Math.min(a1 - b0, b1 - a0);
            if (o <= 0.0f) {
                return null;
            }
            if (o < best) {
                best = o;
                bestX = axx;
                bestY = axy;
                bestZ = axz;
            }
        }
        List<Vector3> aEdges = a.getEdges();
        List<Vector3> bEdges = b.getEdges();
        int na = aEdges.size();
        int nb = bEdges.size();
        for (int i = 0; i < na; i++) {
            Vector3 ea = aEdges.get(i);
            float eax = ea.getX();
            float eay = ea.getY();
            float eaz = ea.getZ();
            for (int j = 0; j < nb; j++) {
                Vector3 eb = bEdges.get(j);
                float ebx = eb.getX();
                float eby = eb.getY();
                float ebz = eb.getZ();
                float cx = (eay * ebz) - (eaz * eby);
                float cy = (eaz * ebx) - (eax * ebz);
                float cz = (eax * eby) - (eay * ebx);
                float lenSq = (cx * cx) + (cy * cy) + (cz * cz);
                if (lenSq > 1.0E-8f) {
                    float invLen = 1.0f / ((float) Math.sqrt(lenSq));
                    float axx = cx * invLen;
                    float axy = cy * invLen;
                    float axz = cz * invLen;
                    a.project(axx, axy, axz, proj);
                    float a0 = proj[0];
                    float a1 = proj[1];
                    b.project(axx, axy, axz, proj);
                    float b0 = proj[0];
                    float b1 = proj[1];
                    float o = Math.min(a1 - b0, b1 - a0);
                    if (o <= 0.0f) {
                        return null;
                    }
                    if (o < best) {
                        best = o;
                        bestX = axx;
                        bestY = axy;
                        bestZ = axz;
                    }
                }
            }
        }
        Vector3 ca = a.getCenter();
        Vector3 cb = b.getCenter();
        if (((ca.getX() - cb.getX()) * bestX) + ((ca.getY() - cb.getY()) * bestY) + ((ca.getZ() - cb.getZ()) * bestZ) < 0.0f) {
            bestX = -bestX;
            bestY = -bestY;
            bestZ = -bestZ;
        }
        return new Vector3(bestX * best, bestY * best, bestZ * best);
    }

    public static /* synthetic */ boolean aabbOverlap$default(Geometry geometry, Vector3 vector3, Vector3 vector4, Vector3 vector5, Vector3 vector6, float f, int i, Object obj) {
        if ((i & 16) != 0) {
            f = 0.0f;
        }
        return geometry.aabbOverlap(vector3, vector4, vector5, vector6, f);
    }

    public final boolean aabbOverlap(Vector3 a0, Vector3 a1, Vector3 b0, Vector3 b1, float pad) {
        Intrinsics.checkNotNullParameter(a0, "a0");
        Intrinsics.checkNotNullParameter(a1, "a1");
        Intrinsics.checkNotNullParameter(b0, "b0");
        Intrinsics.checkNotNullParameter(b1, "b1");
        return a0.getX() <= b1.getX() + pad && a1.getX() >= b0.getX() - pad && a0.getY() <= b1.getY() + pad && a1.getY() >= b0.getY() - pad && a0.getZ() <= b1.getZ() + pad && a1.getZ() >= b0.getZ() - pad;
    }

    public final float maxOf3(Vector3 v) {
        Intrinsics.checkNotNullParameter(v, "v");
        return Math.max(v.getX(), Math.max(v.getY(), v.getZ()));
    }
}

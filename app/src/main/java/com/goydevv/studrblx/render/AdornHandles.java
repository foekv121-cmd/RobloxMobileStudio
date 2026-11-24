package com.goydevv.studrblx.render;

import com.goydevv.studrblx.model.Axes;
import com.goydevv.studrblx.model.CFrame;
import com.goydevv.studrblx.model.Color3;
import com.goydevv.studrblx.model.Faces;
import com.goydevv.studrblx.model.Instance;
import com.goydevv.studrblx.model.PartData;
import com.goydevv.studrblx.model.Vector3;
import java.util.ArrayList;
import java.util.List;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

/* JADX INFO: compiled from: AdornHandles.kt */
/* JADX INFO: loaded from: classes8.dex */
public final class AdornHandles {
    public static final AdornHandles INSTANCE = new AdornHandles();
    private static final List<Vector3> normals = CollectionsKt.listOf(new Vector3[]{Vector3.INSTANCE.getX(), Vector3.INSTANCE.getY(), Vector3.INSTANCE.getZ(), Vector3.INSTANCE.getX().unaryMinus(), Vector3.INSTANCE.getY().unaryMinus(), Vector3.INSTANCE.getZ().unaryMinus()});
    private static final List<String> NORMAL_NAMES = CollectionsKt.listOf(new String[]{"Right", "Top", "Back", "Left", "Bottom", "Front"});
    private static final List<String> AXIS_NAMES = CollectionsKt.listOf(new String[]{"X", "Y", "Z"});

    private AdornHandles() {
    }

    /* JADX INFO: compiled from: AdornHandles.kt */
    public static final class Tip {
        private final Vector3 axis;
        private final Vector3 base;
        private final Color3 color;
        private final int id;
        private final float radius;
        private final Vector3 tip;

        public Tip(int id, Vector3 axis, Vector3 base, Vector3 tip, float radius, Color3 color) {
            Intrinsics.checkNotNullParameter(axis, "axis");
            Intrinsics.checkNotNullParameter(base, "base");
            Intrinsics.checkNotNullParameter(tip, "tip");
            Intrinsics.checkNotNullParameter(color, "color");
            this.id = id;
            this.axis = axis;
            this.base = base;
            this.tip = tip;
            this.radius = radius;
            this.color = color;
        }

        public final Vector3 getAxis() {
            return this.axis;
        }

        public final Vector3 getBase() {
            return this.base;
        }

        public final Color3 getColor() {
            return this.color;
        }

        public final int getId() {
            return this.id;
        }

        public final float getRadius() {
            return this.radius;
        }

        public final Vector3 getTip() {
            return this.tip;
        }
    }

    public final List<String> getNORMAL_NAMES() {
        return NORMAL_NAMES;
    }

    public final List<String> getAXIS_NAMES() {
        return AXIS_NAMES;
    }

    public final boolean isHandles(Instance a) {
        Intrinsics.checkNotNullParameter(a, "a");
        return Intrinsics.areEqual(a.getClassName(), "Handles") || Intrinsics.areEqual(a.getClassName(), "ArcHandles");
    }

    private final Instance adornee(Instance a) {
        Object obj = a.get("Adornee");
        Instance instance = obj instanceof Instance ? (Instance) obj : null;
        if (instance == null || !instance.isA("BasePart")) {
            return null;
        }
        return instance;
    }

    public final List<Tip> tips(Instance a, ViewState view) {
        Instance p;
        int ax;
        Color3 col;
        int i;
        float f;
        Color3 col2;
        Color3 color3;
        Intrinsics.checkNotNullParameter(a, "a");
        Intrinsics.checkNotNullParameter(view, "view");
        if (!Intrinsics.areEqual(a.get("Visible"), (Object) false) && (p = adornee(a)) != null) {
            CFrame cf = PartData.INSTANCE.cframe(p);
            Vector3 size = PartData.INSTANCE.size(p);
            float dist = RangesKt.coerceAtLeast(view.distTo(cf.getPosition()), 1.0f);
            float gap = 0.06f * dist;
            float r = dist * 0.018f;
            ArrayList out = new ArrayList();
            float f2 = 0.3f;
            if (Intrinsics.areEqual(a.getClassName(), "Handles")) {
                Object obj = a.get("Faces");
                Faces faces = obj instanceof Faces ? (Faces) obj : null;
                int mask = faces != null ? faces.getMask() : 63;
                Object obj2 = a.get("Color3");
                Color3 c = obj2 instanceof Color3 ? (Color3) obj2 : null;
                int i2 = 0;
                while (i2 < 6) {
                    if ((mask & (1 << i2)) == 0) {
                        i = i2;
                        f = f2;
                    } else {
                        Vector3 ln = normals.get(i2);
                        Vector3 dir = cf.vectorToWorld(ln);
                        float half = ((Math.abs(ln.getX()) * size.getX()) / 2.0f) + ((Math.abs(ln.getY()) * size.getY()) / 2.0f) + ((Math.abs(ln.getZ()) * size.getZ()) / 2.0f);
                        Vector3 base = cf.getPosition().plus(dir.times(half));
                        if (c == null) {
                            switch (i2 % 3) {
                                case 0:
                                    color3 = new Color3(0.95f, 0.25f, 0.25f);
                                    col2 = color3;
                                    break;
                                case 1:
                                    color3 = new Color3(f2, 0.9f, f2);
                                    col2 = color3;
                                    break;
                                default:
                                    col2 = new Color3(0.25f, 0.5f, 1.0f);
                                    break;
                            }
                        } else {
                            col2 = c;
                        }
                        Vector3 ln2 = base.plus(dir.times(gap));
                        f = 0.3f;
                        i = i2;
                        out.add(new Tip(i, dir, base, ln2, r, col2));
                    }
                    i2 = i + 1;
                    f2 = f;
                }
            } else {
                float f3 = 0.3f;
                float f4 = 0.9f;
                Object obj3 = a.get("Axes");
                Axes axes = obj3 instanceof Axes ? (Axes) obj3 : null;
                int mask2 = axes != null ? axes.getMask() : 7;
                float ext = (Math.max(size.getX(), Math.max(size.getY(), size.getZ())) / 2.0f) + gap;
                int ax2 = 0;
                while (ax2 < 3) {
                    if (((1 << ax2) & mask2) == 0) {
                        ax = ax2;
                    } else {
                        Vector3 dir2 = cf.vectorToWorld(normals.get(ax2));
                        switch (ax2) {
                            case 0:
                                col = new Color3(0.95f, 0.25f, 0.25f);
                                break;
                            case 1:
                                col = new Color3(f3, f4, f3);
                                break;
                            default:
                                col = new Color3(0.25f, 0.5f, 1.0f);
                                break;
                        }
                        Vector3 perp = cf.vectorToWorld(normals.get((ax2 + 1) % 3));
                        ax = ax2;
                        out.add(new Tip(ax, dir2, cf.getPosition(), cf.getPosition().plus(perp.times(ext)), r * 1.1f, col));
                    }
                    ax2 = ax + 1;
                    f3 = 0.3f;
                    f4 = 0.9f;
                }
            }
            return out;
        }
        return CollectionsKt.emptyList();
    }
}

package com.goydevv.studrblx.render;

import com.goydevv.studrblx.core.Studio;
import com.goydevv.studrblx.core.Tool;
import com.goydevv.studrblx.engine.Geometry;
import com.goydevv.studrblx.model.CFrame;
import com.goydevv.studrblx.model.Instance;
import com.goydevv.studrblx.model.PartData;
import com.goydevv.studrblx.model.Vector3;
import java.util.ArrayList;
import java.util.List;
import kotlin.Pair;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

/* JADX INFO: compiled from: SceneRenderer.kt */
/* JADX INFO: loaded from: classes8.dex */
public final class Handles {
    public static final Handles INSTANCE = new Handles();

    private Handles() {
    }

    public final HandleSet compute(Studio studio) {
        CFrame cf;
        Vector3 b;
        Intrinsics.checkNotNullParameter(studio, "studio");
        CFrame cFrame = null;
        if (!studio.isPlaying() && studio.getTool() != Tool.SELECT) {
            List<Instance> listSelectedParts = studio.selectedParts();
            if (listSelectedParts.isEmpty()) {
                return null;
            }
            boolean single = listSelectedParts.size() == 1;
            boolean useLocal = studio.getTool() != Tool.MOVE && single;
            if (useLocal) {
                cf = PartData.INSTANCE.cframe(listSelectedParts.get(0)).rotationOnly().withPosition(PartData.INSTANCE.cframe(listSelectedParts.get(0)).getPosition());
                b = PartData.INSTANCE.size(listSelectedParts.get(0)).div(2.0f);
            } else {
                Pair<Vector3, Vector3> pairBoundsOf = Geometry.INSTANCE.boundsOf(listSelectedParts);
                if (pairBoundsOf == null) {
                    return null;
                }
                Vector3 a = pairBoundsOf.component1();
                Vector3 b2 = pairBoundsOf.component2();
                cf = CFrame.INSTANCE.at(a.plus(b2).div(2.0f));
                Vector3 half = b2.minus(a).div(2.0f);
                b = half;
            }
            Vector3 center = cf.getPosition();
            float f = 1.0f;
            float dist = RangesKt.coerceAtLeast(studio.getView().distTo(center), 1.0f);
            float gap = 0.07f * dist;
            float r = dist * 0.02f;
            ArrayList list = new ArrayList();
            int i = 0;
            while (i < 6) {
                int ax = i % 3;
                float sign = i < 3 ? f : -1.0f;
                Vector3 dir = cf.column(ax).times(sign);
                CFrame cFrame2 = cFrame;
                Vector3 base = center.plus(dir.times(b.get(ax)));
                list.add(new Handle(i, dir, base, base.plus(dir.times(gap)), r));
                i++;
                cFrame = cFrame2;
                f = 1.0f;
            }
            return new HandleSet(studio.getTool(), center, list, useLocal ? cf : cFrame);
        }
        return null;
    }

    public final float[] color(int i) {
        switch (i % 3) {
            case 0:
                return new float[]{0.95f, 0.25f, 0.25f, 1.0f};
            case 1:
                return new float[]{0.3f, 0.9f, 0.3f, 1.0f};
            default:
                return new float[]{0.25f, 0.5f, 1.0f, 1.0f};
        }
    }
}

package com.goydevv.studrblx.engine;

import com.goydevv.studrblx.model.CFrame;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

/* JADX INFO: compiled from: KeyframeAnim.kt */
/* JADX INFO: loaded from: classes7.dex */
public final class AnimPlayer {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private final ArrayList<Track> tracks = new ArrayList<>();

    /* JADX INFO: compiled from: KeyframeAnim.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final int rank(int v) {
            if (v >= 1000) {
                return -1;
            }
            return v;
        }
    }

    /* JADX INFO: compiled from: KeyframeAnim.kt */
    public static final class Track {
        private final AnimClip clip;
        private boolean ended;
        private float fadeRate;
        private boolean looped;
        private int loops;
        private int priority;
        private float speed;
        private boolean stopping;
        private final String tag;
        private float targetWeight;
        private float time;
        private float weight;

        public Track(AnimClip clip, String tag, int priority) {
            Intrinsics.checkNotNullParameter(clip, "clip");
            Intrinsics.checkNotNullParameter(tag, "tag");
            this.clip = clip;
            this.tag = tag;
            this.priority = priority;
            this.speed = 1.0f;
            this.targetWeight = 1.0f;
            this.fadeRate = 10.0f;
            this.looped = this.clip.getLooped();
        }

        public final AnimClip getClip() {
            return this.clip;
        }

        public final int getPriority() {
            return this.priority;
        }

        public final String getTag() {
            return this.tag;
        }

        public final void setPriority(int i) {
            this.priority = i;
        }

        public final float getTime() {
            return this.time;
        }

        public final void setTime(float f) {
            this.time = f;
        }

        public final float getSpeed() {
            return this.speed;
        }

        public final void setSpeed(float f) {
            this.speed = f;
        }

        public final float getWeight() {
            return this.weight;
        }

        public final void setWeight(float f) {
            this.weight = f;
        }

        public final float getTargetWeight() {
            return this.targetWeight;
        }

        public final void setTargetWeight(float f) {
            this.targetWeight = f;
        }

        public final float getFadeRate() {
            return this.fadeRate;
        }

        public final void setFadeRate(float f) {
            this.fadeRate = f;
        }

        public final boolean getStopping() {
            return this.stopping;
        }

        public final void setStopping(boolean z) {
            this.stopping = z;
        }

        public final boolean getLooped() {
            return this.looped;
        }

        public final void setLooped(boolean z) {
            this.looped = z;
        }

        public final int getLoops() {
            return this.loops;
        }

        public final void setLoops(int i) {
            this.loops = i;
        }

        public final boolean getEnded() {
            return this.ended;
        }

        public final void setEnded(boolean z) {
            this.ended = z;
        }

        public final boolean getPlaying() {
            return (this.stopping || this.ended) ? false : true;
        }
    }

    public final ArrayList<Track> getTracks() {
        return this.tracks;
    }

    public static /* synthetic */ Track play$default(AnimPlayer animPlayer, AnimClip animClip, String str, float f, float f2, float f3, int i, int i2, Object obj) {
        if ((i2 & 4) != 0) {
            f = 0.1f;
        }
        float f4 = f;
        float f5 = (i2 & 8) != 0 ? 1.0f : f2;
        float f6 = (i2 & 16) != 0 ? 1.0f : f3;
        if ((i2 & 32) != 0) {
            i = animClip.getPriority();
        }
        return animPlayer.play(animClip, str, f4, f5, f6, i);
    }

    public final Track play(AnimClip clip, String tag, float fade, float weight, float speed, int priority) {
        Intrinsics.checkNotNullParameter(clip, "clip");
        Intrinsics.checkNotNullParameter(tag, "tag");
        Track t = new Track(clip, tag, priority);
        t.setTargetWeight(weight);
        t.setSpeed(speed);
        t.setFadeRate(fade <= 0.001f ? 1000.0f : 1.0f / fade);
        if (fade <= 0.001f) {
            t.setWeight(weight);
        }
        this.tracks.add(t);
        return t;
    }

    public static /* synthetic */ void stop$default(AnimPlayer animPlayer, Track track, float f, int i, Object obj) {
        if ((i & 2) != 0) {
            f = 0.1f;
        }
        animPlayer.stop(track, f);
    }

    public final void stop(Track t, float fade) {
        Intrinsics.checkNotNullParameter(t, "t");
        t.setStopping(true);
        t.setTargetWeight(0.0f);
        if (fade <= 0.001f) {
            t.setWeight(0.0f);
            t.setFadeRate(1000.0f);
        } else {
            t.setFadeRate(1.0f / fade);
        }
    }

    public final void stopTag(String tag, float fade) {
        Intrinsics.checkNotNullParameter(tag, "tag");
        Iterator<Track> it = this.tracks.iterator();
        Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
        while (it.hasNext()) {
            Track next = it.next();
            Intrinsics.checkNotNullExpressionValue(next, "next(...)");
            Track t = next;
            if (Intrinsics.areEqual(t.getTag(), tag) && !t.getStopping()) {
                stop(t, fade);
            }
        }
    }

    public final void step(float dt) {
        Iterator<Track> it = this.tracks.iterator();
        Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
        while (it.hasNext()) {
            Track next = it.next();
            Intrinsics.checkNotNullExpressionValue(next, "next(...)");
            Track t = next;
            if (!t.getEnded() || t.getLooped()) {
                t.setTime(t.getTime() + (t.getSpeed() * dt));
                float len = t.getClip().getLength();
                float time = 0.0f;
                if (len <= 0.0f || t.getTime() < len) {
                    if (t.getTime() < 0.0f) {
                        if (t.getLooped() && len > 0.0f) {
                            time = (t.getTime() % len) + len;
                        }
                        t.setTime(time);
                    }
                } else if (t.getLooped()) {
                    t.setTime(t.getTime() % len);
                    t.setLoops(t.getLoops() + 1);
                } else {
                    t.setTime(len);
                    t.setEnded(true);
                }
            }
            float k = RangesKt.coerceAtMost(t.getFadeRate() * dt, 1.0f);
            t.setWeight(t.getWeight() + ((t.getTargetWeight() - t.getWeight()) * k));
            if (Math.abs(t.getWeight() - t.getTargetWeight()) < 0.002f) {
                t.setWeight(t.getTargetWeight());
            }
            if (t.getStopping() && t.getWeight() <= 0.001f) {
                it.remove();
            }
        }
    }

    public final CFrame transform(String joint) {
        Intrinsics.checkNotNullParameter(joint, "joint");
        return transformOver(null, joint);
    }

    public final CFrame transformOver(CFrame base, String joint) {
        CFrame cf;
        Intrinsics.checkNotNullParameter(joint, "joint");
        if (this.tracks.isEmpty()) {
            return base;
        }
        CFrame result = base;
        Iterable iterable = this.tracks;
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
        Iterator it = iterable.iterator();
        while (it.hasNext()) {
            arrayList.add(Integer.valueOf(((Track) it.next()).getPriority()));
        }
        List prios = CollectionsKt.sorted(CollectionsKt.distinct((List) arrayList));
        Iterator it2 = prios.iterator();
        while (it2.hasNext()) {
            int p = ((Number) it2.next()).intValue();
            CFrame avg = null;
            float acc = 0.0f;
            Iterator<Track> it3 = this.tracks.iterator();
            Intrinsics.checkNotNullExpressionValue(it3, "iterator(...)");
            while (it3.hasNext()) {
                Track next = it3.next();
                Intrinsics.checkNotNullExpressionValue(next, "next(...)");
                Track t = next;
                if (t.getPriority() == p && t.getWeight() > 5.0E-4f && (cf = t.getClip().sample(joint, t.getTime())) != null) {
                    acc += t.getWeight();
                    avg = avg == null ? cf : Quat.INSTANCE.lerp(avg, cf, t.getWeight() / acc);
                }
            }
            if (avg != null) {
                result = Quat.INSTANCE.lerp(result == null ? CFrame.INSTANCE.getIDENTITY() : result, avg, RangesKt.coerceAtMost(acc, 1.0f));
            }
        }
        return result;
    }

    public final void clear() {
        this.tracks.clear();
    }
}

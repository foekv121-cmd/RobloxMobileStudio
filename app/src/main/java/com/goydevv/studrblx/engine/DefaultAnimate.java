package com.goydevv.studrblx.engine;

import com.goydevv.studrblx.model.CFrame;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import kotlin.TuplesKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

/* JADX INFO: compiled from: KeyframeAnim.kt */
/* JADX INFO: loaded from: classes7.dex */
public final class DefaultAnimate {
    private String current;
    private ArrayList<AnimPlayer.Track> currentTracks;
    private String emoteName;
    private AnimPlayer.Track emoteTrack;
    private int idleLoops;
    private boolean pendingJump;
    private final AnimPlayer player = new AnimPlayer();
    private final boolean r15;
    private boolean reported;
    private final Random rnd;
    private final Map<String, List<Entry>> sets;
    private AnimPlayer.Track toolTrack;

    public DefaultAnimate(boolean r15) {
        this.r15 = r15;
        this.sets = this.r15 ? MapsKt.mapOf(TuplesKt.to("idle", CollectionsKt.listOf(new Entry[]{new Entry(507766666L, 9), new Entry(507766951L, 1)})), TuplesKt.to("walk", CollectionsKt.listOf(new Entry(913402848L, 10))), TuplesKt.to("run", CollectionsKt.listOf(new Entry(913376220L, 10))), TuplesKt.to("jump", CollectionsKt.listOf(new Entry(507765000L, 10))), TuplesKt.to("fall", CollectionsKt.listOf(new Entry(507767968L, 10))), TuplesKt.to("climb", CollectionsKt.listOf(new Entry(507765644L, 10))), TuplesKt.to("sit", CollectionsKt.listOf(new Entry(2506281703L, 10))), TuplesKt.to("toolnone", CollectionsKt.listOf(new Entry(507768375L, 10))), TuplesKt.to("wave", CollectionsKt.listOf(new Entry(507770239L, 10))), TuplesKt.to("point", CollectionsKt.listOf(new Entry(507770453L, 10))), TuplesKt.to("dance", CollectionsKt.listOf(new Entry[]{new Entry(507771019L, 10), new Entry(507771955L, 10), new Entry(507772104L, 10)})), TuplesKt.to("dance2", CollectionsKt.listOf(new Entry[]{new Entry(507776043L, 10), new Entry(507776720L, 10), new Entry(507776879L, 10)})), TuplesKt.to("dance3", CollectionsKt.listOf(new Entry[]{new Entry(507777268L, 10), new Entry(507777451L, 10), new Entry(507777623L, 10)})), TuplesKt.to("laugh", CollectionsKt.listOf(new Entry(507770818L, 10))), TuplesKt.to("cheer", CollectionsKt.listOf(new Entry(507770677L, 10)))) : MapsKt.mapOf(TuplesKt.to("idle", CollectionsKt.listOf(new Entry[]{new Entry(180435571L, 9), new Entry(180435792L, 1)})), TuplesKt.to("walk", CollectionsKt.listOf(new Entry(180426354L, 10))), TuplesKt.to("run", CollectionsKt.listOf(new Entry(180426354L, 10))), TuplesKt.to("jump", CollectionsKt.listOf(new Entry(125750702L, 10))), TuplesKt.to("fall", CollectionsKt.listOf(new Entry(180436148L, 10))), TuplesKt.to("climb", CollectionsKt.listOf(new Entry(180436334L, 10))), TuplesKt.to("sit", CollectionsKt.listOf(new Entry(178130996L, 10))), TuplesKt.to("toolnone", CollectionsKt.listOf(new Entry(182393478L, 10))), TuplesKt.to("wave", CollectionsKt.listOf(new Entry(128777973L, 10))), TuplesKt.to("point", CollectionsKt.listOf(new Entry(128853357L, 10))), TuplesKt.to("dance", CollectionsKt.listOf(new Entry[]{new Entry(182435998L, 10), new Entry(182491037L, 10), new Entry(182491065L, 10)})), TuplesKt.to("dance2", CollectionsKt.listOf(new Entry[]{new Entry(182436842L, 10), new Entry(182491248L, 10), new Entry(182491277L, 10)})), TuplesKt.to("dance3", CollectionsKt.listOf(new Entry[]{new Entry(182436935L, 10), new Entry(182491368L, 10), new Entry(182491423L, 10)})), TuplesKt.to("laugh", CollectionsKt.listOf(new Entry(129423131L, 10))), TuplesKt.to("cheer", CollectionsKt.listOf(new Entry(129423030L, 10))));
        this.rnd = new Random();
        this.current = "";
        this.currentTracks = new ArrayList<>();
        // Warm core locomotion clips first (jump/fall/walk/idle) before emotes
        for (String priorityKey : new String[]{"jump", "fall", "walk", "idle", "climb", "sit"}) {
            List<Entry> list = this.sets.get(priorityKey);
            if (list != null) {
                for (Entry e : list) {
                    AnimLibrary.INSTANCE.get(e.getId());
                }
            }
        }
        Iterator<List<Entry>> it = this.sets.values().iterator();
        while (it.hasNext()) {
            for (Entry e : it.next()) {
                AnimLibrary.INSTANCE.get(e.getId());
            }
        }
    }

    public final void triggerJump() {
        this.pendingJump = true;
    }

    /* JADX INFO: compiled from: KeyframeAnim.kt */
    private static final /* data */ class Entry {
        private final long id;
        private final int weight;

        public Entry(long id, int weight) {
            this.id = id;
            this.weight = weight;
        }

        public final long getId() {
            return this.id;
        }

        public final int getWeight() {
            return this.weight;
        }
    }

    public final boolean getReady() {
        for (String key : new String[]{"idle", "walk"}) {
            List<Entry> list = this.sets.get(key);
            if (list != null) {
                for (Entry e : list) {
                    if (AnimLibrary.INSTANCE.get(e.getId()) != null) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private final AnimClip pick(String name) {
        List<Entry> list = this.sets.get(name);
        if (list == null) {
            return null;
        }
        Iterator it = list.iterator();
        int total = 0;
        while (it.hasNext()) {
            total += ((Entry) it.next()).getWeight();
        }
        int r = this.rnd.nextInt(RangesKt.coerceAtLeast(total, 1));
        for (Entry e : list) {
            r -= e.getWeight();
            if (r < 0) {
                return AnimLibrary.INSTANCE.get(e.getId());
            }
        }
        return AnimLibrary.INSTANCE.get(((Entry) CollectionsKt.first((List) list)).getId());
    }

    private final void playCore(String name, float fade) {
        if (Intrinsics.areEqual(name, this.current)) {
            return;
        }
        Iterator<AnimPlayer.Track> it = this.currentTracks.iterator();
        Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
        while (it.hasNext()) {
            AnimPlayer.Track next = it.next();
            Intrinsics.checkNotNullExpressionValue(next, "next(...)");
            this.player.stop(next, fade);
        }
        this.currentTracks = new ArrayList<>();
        this.current = name;
        if (this.r15 && Intrinsics.areEqual(name, "walk")) {
            AnimClip animClipPick = pick("walk");
            if (animClipPick != null) {
                this.currentTracks.add(AnimPlayer.play$default(this.player, animClipPick, "walk", fade, 0.0f, 0.0f, 0, 24, null));
            }
            AnimClip animClipPick2 = pick("run");
            if (animClipPick2 != null) {
                this.currentTracks.add(AnimPlayer.play$default(this.player, animClipPick2, "run", fade, 1.0E-4f, 0.0f, 0, 16, null));
                return;
            }
            return;
        }
        AnimClip clip = pick(name);
        if (clip == null && "jump".equals(name)) {
            clip = pick("fall");
        }
        if (clip == null) {
            return;
        }
        AnimPlayer.Track track = AnimPlayer.play$default(this.player, clip, name, fade, 0.0f, 0.0f, 0, 24, null);
        if ("jump".equals(name)) {
            track.setLooped(false);
            track.setSpeed(1.0f);
        } else if ("fall".equals(name) || "sit".equals(name)) {
            track.setLooped(true);
        }
        this.currentTracks.add(track);
    }

    public final void step(float dt, java.lang.String state, float speed, boolean toolEquipped, java.lang.String emote, float jumpAnimTime) {
        if (!getReady()) {
            return;
        }
        // Emote overlay handling
        if (emote != null && emote.length() > 0) {
            if (!Intrinsics.areEqual(emote, this.emoteName)) {
                if (this.emoteTrack != null) {
                    this.player.stop(this.emoteTrack, 0.15f);
                    this.emoteTrack = null;
                }
                AnimClip ec = pick(emote);
                if (ec != null) {
                    this.emoteTrack = this.player.play(ec, "emote", 0.15f, 1.0f, 1.0f, 1);
                    this.emoteTrack.setLooped(emote.startsWith("dance"));
                }
                this.emoteName = emote;
            }
        } else if (this.emoteTrack != null) {
            this.player.stop(this.emoteTrack, 0.15f);
            this.emoteTrack = null;
            this.emoteName = null;
        }

        // Tool equipped overlay (official Roblox toolnone animation)
        if (toolEquipped) {
            if (this.toolTrack == null || this.toolTrack.getStopping()) {
                AnimClip tc = pick("toolnone");
                if (tc != null) {
                    this.toolTrack = this.player.play(tc, "tool", 0.1f, 1.0f, 1.0f, 2);
                    this.toolTrack.setLooped(true);
                }
            }
        } else if (this.toolTrack != null) {
            this.player.stop(this.toolTrack, 0.1f);
            this.toolTrack = null;
        }

        // Core locomotion state machine matching official Roblox Animate LocalScript
        if ("Dead".equals(state)) {
            this.pendingJump = false;
            if (!"".equals(this.current)) {
                for (AnimPlayer.Track t : this.currentTracks) {
                    this.player.stop(t, 0.1f);
                }
                this.currentTracks.clear();
                this.current = "";
            }
        } else if (this.pendingJump || "Jumping".equals(state) || jumpAnimTime > 0.0f) {
            if (this.pendingJump) {
                this.pendingJump = false;
                this.current = "";
            }
            playCore("jump", 0.1f);
        } else if ("FreeFall".equals(state) || "Freefall".equals(state)) {
            playCore("fall", 0.1f);
        } else if ("Climbing".equals(state)) {
            playCore("climb", 0.06f);
            float cSpd = speed < 0.1f ? 0.0f : (speed / 5.0f);
            for (AnimPlayer.Track t : this.currentTracks) {
                t.setSpeed(cSpd);
            }
        } else if ("Seated".equals(state)) {
            playCore("sit", 0.08f);
        } else if ("Running".equals(state) && speed > 0.20f) {
            playCore("walk", 0.05f);
            float scale = Math.max(0.15f, speed / 14.5f);
            if (this.r15) {
                float runW = Math.max(0.0001f, Math.min(1.0f, (speed - 6.0f) / 10.0f));
                float walkW = Math.max(0.0001f, 1.0f - runW);
                for (AnimPlayer.Track t : this.currentTracks) {
                    t.setSpeed(scale);
                    if ("run".equals(t.getTag())) {
                        t.setTargetWeight(runW);
                    } else if ("walk".equals(t.getTag())) {
                        t.setTargetWeight(walkW);
                    }
                }
            } else {
                for (AnimPlayer.Track t : this.currentTracks) {
                    t.setSpeed(scale);
                }
            }
        } else {
            playCore("idle", 0.08f);
        }
        this.player.step(dt);
    }

    public final CFrame transform(String part1Name) {
        Intrinsics.checkNotNullParameter(part1Name, "part1Name");
        if (!getReady()) {
            return null;
        }
        // If current state has no loaded track yet (e.g. jump clip still downloading),
        // return null so PlaySession's instant procedural R6/R15 pose takes over with zero delay.
        if (this.currentTracks.isEmpty() && !"Dead".equals(this.current)) {
            return null;
        }
        CFrame cFrameTransform = this.player.transform(part1Name);
        return cFrameTransform == null ? CFrame.INSTANCE.getIDENTITY() : cFrameTransform;
    }
}

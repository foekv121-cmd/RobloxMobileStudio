package com.goydevv.studrblx.engine;

import com.goydevv.studrblx.model.CFrame;
import com.goydevv.studrblx.model.Instance;
import com.goydevv.studrblx.model.PartData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Connects an {@link SunAnim} to the real instances of a model.
 *
 * Motor6D joints are animated through their Transform (the part they move is placed with forward
 * kinematics: Part1 = Part0 * C0 * Transform * C1^-1). Welds follow their Part0. Parts that no joint
 * moves get a PART track with an absolute CFrame. The pose the model had when the Animator opened is
 * kept so Close can put it back.
 */
public final class SunRig {
    public static final int MAX_TRACKS = 64;
    private static final int MOTOR = 0;
    private static final int WELD = 1;
    private static final int WELD_CONSTRAINT = 2;

    private static final class Joint {
        Instance inst;
        Instance p0;
        Instance p1;
        CFrame c0 = CFrame.INSTANCE.getIDENTITY();
        CFrame c1 = CFrame.INSTANCE.getIDENTITY();
        CFrame rel = CFrame.INSTANCE.getIDENTITY();
        CFrame rest = CFrame.INSTANCE.getIDENTITY();
        int type;
        String path;
    }

    private final Instance root;
    private final List<Instance> parts = new ArrayList<>();
    private final List<Instance> freeParts = new ArrayList<>();
    private final List<Joint> fk = new ArrayList<>();
    private final Map<Instance, CFrame> base = new HashMap<>();
    private final Map<String, Instance> partByPath = new HashMap<>();
    private final Map<String, Joint> jointByPath = new HashMap<>();
    private final Map<Instance, String> pathOfPart = new HashMap<>();
    public int skipped;

    public SunRig(Instance root) {
        this.root = root;
        List<Instance> all = new ArrayList<>();
        if (root.isA("BasePart")) {
            all.add(root);
        }
        root.descendants(all);
        Set<Instance> partSet = new HashSet<>();
        for (Instance i : all) {
            if (i.isA("BasePart")) {
                parts.add(i);
                partSet.add(i);
                base.put(i, PartData.INSTANCE.cframe(i));
            }
        }
        List<Joint> joints = new ArrayList<>();
        for (Instance i : all) {
            String cn = i.getClassName();
            int type;
            if ("Motor6D".equals(cn) || "Motor".equals(cn)) {
                type = MOTOR;
            } else if ("Weld".equals(cn) || "ManualWeld".equals(cn) || "Snap".equals(cn)) {
                type = WELD;
            } else if ("WeldConstraint".equals(cn)) {
                type = WELD_CONSTRAINT;
            } else {
                continue;
            }
            Object a = i.get("Part0");
            Object b = i.get("Part1");
            if (!(a instanceof Instance) || !(b instanceof Instance) || !partSet.contains(a) || !partSet.contains(b) || a == b) {
                continue;
            }
            Joint j = new Joint();
            j.inst = i;
            j.p0 = (Instance) a;
            j.p1 = (Instance) b;
            j.type = type;
            j.path = pathOf(i);
            if (type != WELD_CONSTRAINT) {
                Object c0 = i.get("C0");
                Object c1 = i.get("C1");
                if (c0 instanceof CFrame) {
                    j.c0 = (CFrame) c0;
                }
                if (c1 instanceof CFrame) {
                    j.c1 = (CFrame) c1;
                }
            }
            j.rel = base.get(j.p0).inverse().times(base.get(j.p1));
            j.rest = base.get(j.p0).times(j.c0).inverse().times(base.get(j.p1)).times(j.c1);
            joints.add(j);
        }
        // One driving joint per part, ordered so a joint always comes after the joint that places its Part0.
        Set<Instance> driven = new HashSet<>();
        for (Joint j : joints) {
            if (!driven.contains(j.p1)) {
                driven.add(j.p1);
            }
        }
        for (Instance p : parts) {
            if (!driven.contains(p)) {
                freeParts.add(p);
            }
        }
        Set<Instance> known = new HashSet<>(freeParts);
        List<Joint> pending = new ArrayList<>(joints);
        boolean progress = true;
        while (progress && !pending.isEmpty()) {
            progress = false;
            for (int n = 0; n < pending.size(); n++) {
                Joint j = pending.get(n);
                if (known.contains(j.p0) && !known.contains(j.p1)) {
                    fk.add(j);
                    known.add(j.p1);
                    pending.remove(n);
                    n--;
                    progress = true;
                }
            }
        }
        for (Instance p : parts) {
            String path = pathOf(p);
            pathOfPart.put(p, path);
            partByPath.put(path, p);
        }
        for (Joint j : fk) {
            if (!jointByPath.containsKey(j.path)) {
                jointByPath.put(j.path, j);
            }
        }
    }

    private String pathOf(Instance i) {
        StringBuilder sb = new StringBuilder();
        Instance cur = i;
        while (cur != null && cur != root) {
            if (sb.length() > 0) {
                sb.insert(0, "/");
            }
            sb.insert(0, cur.getName().replace("/", "_"));
            cur = cur.getParent();
        }
        return sb.toString();
    }

    private static String label(String path, Instance fallback) {
        if (path.length() == 0) {
            return fallback.getName();
        }
        int slash = path.lastIndexOf('/');
        return slash >= 0 ? path.substring(slash + 1) : path;
    }

    public boolean isEmpty() {
        return parts.isEmpty();
    }

    public boolean hasJoints() {
        for (Joint j : fk) {
            if (j.type == MOTOR) {
                return true;
            }
        }
        return false;
    }

    /** An animation with one empty track for every Motor6D and every part nothing else moves. */
    public SunAnim newAnim() {
        SunAnim a = new SunAnim();
        skipped = 0;
        Set<String> seen = new HashSet<>();
        for (Instance p : freeParts) {
            String path = pathOfPart.get(p);
            if (!seen.add("p:" + path) || a.tracks.size() >= MAX_TRACKS) {
                skipped++;
                continue;
            }
            a.tracks.add(new SunAnim.Track(path, label(path, p), SunAnim.PART));
        }
        for (Joint j : fk) {
            if (j.type != MOTOR) {
                continue;
            }
            if (!seen.add("j:" + j.path) || a.tracks.size() >= MAX_TRACKS) {
                skipped++;
                continue;
            }
            SunAnim.Track t = new SunAnim.Track(j.path, label(j.path, j.inst), SunAnim.JOINT);
            t.pose = j.p1.getName();
            t.parent = j.p0.getName();
            a.tracks.add(t);
        }
        return a;
    }

    /** Makes a loaded animation fit this rig: unknown tracks are dropped, missing ones are added empty. */
    public SunAnim fit(SunAnim loaded) {
        SunAnim fresh = newAnim();
        fresh.name = loaded.name;
        fresh.fps = loaded.fps;
        fresh.length = loaded.length;
        fresh.loop = loaded.loop;
        fresh.priority = loaded.priority;
        fresh.markers.addAll(loaded.markers);
        for (SunAnim.Track t : fresh.tracks) {
            SunAnim.Track src = loaded.find(t.path);
            if (src != null && src.kind == t.kind) {
                t.keys.addAll(src.keys);
            }
        }
        fresh.normalize();
        return fresh;
    }

    private CFrame cf(Instance p) {
        return PartData.INSTANCE.cframe(p);
    }

    /** The pose the parts are in right now, as a value for this track. */
    public CFrame capture(SunAnim.Track t) {
        if (t.kind == SunAnim.PART) {
            Instance p = partByPath.get(t.path);
            return p == null ? null : cf(p);
        }
        Joint j = jointByPath.get(t.path);
        if (j == null) {
            return null;
        }
        return cf(j.p0).times(j.c0).inverse().times(cf(j.p1)).times(j.c1);
    }

    /** What the track holds when it has no keys: the pose from when the Animator opened. */
    public CFrame rest(SunAnim.Track t) {
        if (t.kind == SunAnim.PART) {
            Instance p = partByPath.get(t.path);
            return p == null ? null : base.get(p);
        }
        Joint j = jointByPath.get(t.path);
        return j == null ? null : j.rest;
    }

    /** Poses the model at a (fractional) frame. Tracks without keys stay at their opening pose. */
    public void apply(SunAnim anim, float frame) {
        Map<Instance, CFrame> world = new HashMap<>();
        for (Instance p : freeParts) {
            SunAnim.Track t = anim.find(pathOfPart.get(p));
            CFrame v = t == null ? null : SunAnim.sample(t, frame);
            world.put(p, v != null ? v : base.get(p));
        }
        for (Joint j : fk) {
            CFrame p0 = world.get(j.p0);
            if (p0 == null) {
                continue;
            }
            CFrame out;
            if (j.type == MOTOR) {
                SunAnim.Track t = anim.find(j.path);
                CFrame tr = t == null ? null : SunAnim.sample(t, frame);
                out = p0.times(j.c0).times(tr != null ? tr : j.rest).times(j.c1.inverse());
            } else if (j.type == WELD) {
                out = p0.times(j.c0).times(j.c1.inverse());
            } else {
                out = p0.times(j.rel);
            }
            world.put(j.p1, out);
        }
        for (Map.Entry<Instance, CFrame> e : world.entrySet()) {
            write(e.getKey(), e.getValue());
        }
    }

    /** Puts every part back where it was when the Animator opened. */
    public void restore() {
        for (Instance p : parts) {
            write(p, base.get(p));
        }
    }

    private void write(Instance p, CFrame v) {
        if (v != null && !v.equals(cf(p))) {
            p.set("CFrame", v, true);
        }
    }

    public Instance root() {
        return root;
    }

    public int partCount() {
        return parts.size();
    }

    public int unanchoredCount() {
        int n = 0;
        for (Instance p : freeParts) {
            if (Boolean.FALSE.equals(p.get("Anchored"))) {
                n++;
            }
        }
        return n;
    }
}

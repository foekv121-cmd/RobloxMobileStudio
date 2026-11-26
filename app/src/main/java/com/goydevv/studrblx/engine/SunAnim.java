package com.goydevv.studrblx.engine;

import com.goydevv.studrblx.model.CFrame;
import com.goydevv.studrblx.model.EnumValue;
import com.goydevv.studrblx.model.Instance;
import com.goydevv.studrblx.model.InstanceFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeSet;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * An animation made of tracks. A track is either a JOINT (a Motor6D, value = its Transform) or a PART
 * (a free part, value = its CFrame). Times are whole frames at {@link #fps}. Each key carries the easing
 * used from that key to the next one, like a Roblox Pose.
 */
public final class SunAnim {
    public static final int JOINT = 0;
    public static final int PART = 1;
    public static final String[] PRIORITIES = {"Core", "Idle", "Movement", "Action"};
    private static final int[] PRIORITY_ENUM = {1000, 0, 1, 2};
    public static final String HEADER = "--[=[ANIMATOR_DATA";

    public static final class Key {
        public int frame;
        public CFrame cf;
        public String ease = "Linear";
        public int dir;
        public float p1 = 1.0f;
        public float p2 = 0.3f;

        public Key(int frame, CFrame cf) {
            this.frame = frame;
            this.cf = cf;
        }

        public Key copy() {
            Key k = new Key(frame, cf);
            k.ease = ease;
            k.dir = dir;
            k.p1 = p1;
            k.p2 = p2;
            return k;
        }

        public void setEase(String family) {
            ease = family;
            p1 = SunEase.defaultP1(family);
            p2 = SunEase.defaultP2(family);
        }
    }

    public static final class Track {
        public String path;
        public String label;
        public int kind;
        /** JOINT only: name of the part the Motor6D moves (the Pose name) and of the part it hangs from. */
        public String pose;
        public String parent;
        public final ArrayList<Key> keys = new ArrayList<>();

        public Track(String path, String label, int kind) {
            this.path = path;
            this.label = label;
            this.kind = kind;
        }

        public Key at(int frame) {
            for (Key k : keys) {
                if (k.frame == frame) {
                    return k;
                }
            }
            return null;
        }

        /** Adds a key, replacing any key on the same frame. */
        public void put(Key key) {
            Key old = at(key.frame);
            if (old != null) {
                keys.remove(old);
            }
            keys.add(key);
            sort();
        }

        public void sort() {
            Collections.sort(keys, new Comparator<Key>() {
                @Override
                public int compare(Key a, Key b) {
                    return Integer.compare(a.frame, b.frame);
                }
            });
        }

        public Track copy() {
            Track t = new Track(path, label, kind);
            t.pose = pose;
            t.parent = parent;
            for (Key k : keys) {
                t.keys.add(k.copy());
            }
            return t;
        }
    }

    public static final class Marker {
        public int frame;
        public String name;
        public String value = "";

        public Marker(int frame, String name) {
            this.frame = frame;
            this.name = name;
        }
    }

    public String name = "Anim1";
    public int fps = 30;
    public int length = 60;
    public boolean loop = true;
    public int priority = 3;
    public final ArrayList<Track> tracks = new ArrayList<>();
    public final ArrayList<Marker> markers = new ArrayList<>();

    public SunAnim copy() {
        SunAnim a = new SunAnim();
        a.name = name;
        a.fps = fps;
        a.length = length;
        a.loop = loop;
        a.priority = priority;
        for (Track t : tracks) {
            a.tracks.add(t.copy());
        }
        for (Marker m : markers) {
            Marker c = new Marker(m.frame, m.name);
            c.value = m.value;
            a.markers.add(c);
        }
        return a;
    }

    public int lastKeyFrame() {
        int last = 0;
        for (Track t : tracks) {
            if (!t.keys.isEmpty()) {
                last = Math.max(last, t.keys.get(t.keys.size() - 1).frame);
            }
        }
        for (Marker m : markers) {
            last = Math.max(last, m.frame);
        }
        return last;
    }

    public int keyCount() {
        int n = 0;
        for (Track t : tracks) {
            n += t.keys.size();
        }
        return n;
    }

    /** Sorts every track, merges keys that landed on the same frame (the last one wins) and fixes the length. */
    public void normalize() {
        for (Track t : tracks) {
            for (Key k : t.keys) {
                if (k.frame < 0) {
                    k.frame = 0;
                }
            }
            t.sort();
            for (int i = t.keys.size() - 1; i > 0; i--) {
                if (t.keys.get(i).frame == t.keys.get(i - 1).frame) {
                    t.keys.remove(i - 1);
                }
            }
        }
        Collections.sort(markers, new Comparator<Marker>() {
            @Override
            public int compare(Marker a, Marker b) {
                return Integer.compare(a.frame, b.frame);
            }
        });
        length = Math.max(Math.max(1, length), lastKeyFrame());
    }

    public Track find(String path) {
        for (Track t : tracks) {
            if (t.path.equals(path)) {
                return t;
            }
        }
        return null;
    }

    // ------------------------------------------------------------------ sampling

    /** Value of a track at a (fractional) frame, or null when the track has no keys. */
    public static CFrame sample(Track t, float f) {
        int n = t.keys.size();
        if (n == 0) {
            return null;
        }
        Key first = t.keys.get(0);
        if (n == 1 || f <= first.frame) {
            return first.cf;
        }
        Key last = t.keys.get(n - 1);
        if (f >= last.frame) {
            return last.cf;
        }
        int i = 0;
        while (i < n - 2 && t.keys.get(i + 1).frame <= f) {
            i++;
        }
        Key a = t.keys.get(i);
        Key b = t.keys.get(i + 1);
        float span = b.frame - a.frame;
        float x = span <= 0.0f ? 1.0f : (f - a.frame) / span;
        return interp(a.cf, b.cf, SunEase.apply(a.ease, a.dir, x, a.p1, a.p2));
    }

    /** Position lerp plus quaternion slerp. e may be outside 0..1 (Back / Elastic overshoot). */
    public static CFrame interp(CFrame a, CFrame b, float e) {
        float x = a.getX() + (b.getX() - a.getX()) * e;
        float y = a.getY() + (b.getY() - a.getY()) * e;
        float z = a.getZ() + (b.getZ() - a.getZ()) * e;
        float[] qa = quat(a);
        float[] qb = quat(b);
        // relative rotation qd = conj(qa) * qb, kept on the short arc
        float[] qd = mul(new float[]{-qa[0], -qa[1], -qa[2], qa[3]}, qb);
        if (qd[3] < 0.0f) {
            qd = new float[]{-qd[0], -qd[1], -qd[2], -qd[3]};
        }
        float w = Math.max(-1.0f, Math.min(1.0f, qd[3]));
        float s = (float) Math.sqrt(Math.max(0.0f, 1.0f - w * w));
        float[] q = qa;
        if (s > 1.0e-5f) {
            float angle = 2.0f * (float) Math.acos(w);
            float half = angle * e / 2.0f;
            float sh = (float) Math.sin(half) / s;
            q = mul(qa, new float[]{qd[0] * sh, qd[1] * sh, qd[2] * sh, (float) Math.cos(half)});
        }
        return fromQuat(x, y, z, q);
    }

    private static float[] quat(CFrame c) {
        float m00 = c.getR00(), m01 = c.getR01(), m02 = c.getR02();
        float m10 = c.getR10(), m11 = c.getR11(), m12 = c.getR12();
        float m20 = c.getR20(), m21 = c.getR21(), m22 = c.getR22();
        float tr = m00 + m11 + m22;
        float x, y, z, w;
        if (tr > 0.0f) {
            float s = (float) Math.sqrt(tr + 1.0f) * 2.0f;
            w = s / 4.0f;
            x = (m21 - m12) / s;
            y = (m02 - m20) / s;
            z = (m10 - m01) / s;
        } else if (m00 > m11 && m00 > m22) {
            float s = (float) Math.sqrt(1.0f + m00 - m11 - m22) * 2.0f;
            w = (m21 - m12) / s;
            x = s / 4.0f;
            y = (m01 + m10) / s;
            z = (m02 + m20) / s;
        } else if (m11 > m22) {
            float s = (float) Math.sqrt(1.0f + m11 - m00 - m22) * 2.0f;
            w = (m02 - m20) / s;
            x = (m01 + m10) / s;
            y = s / 4.0f;
            z = (m12 + m21) / s;
        } else {
            float s = (float) Math.sqrt(1.0f + m22 - m00 - m11) * 2.0f;
            w = (m10 - m01) / s;
            x = (m02 + m20) / s;
            y = (m12 + m21) / s;
            z = s / 4.0f;
        }
        float n = (float) Math.sqrt(x * x + y * y + z * z + w * w);
        if (n < 1.0e-8f) {
            return new float[]{0, 0, 0, 1};
        }
        return new float[]{x / n, y / n, z / n, w / n};
    }

    private static float[] mul(float[] a, float[] b) {
        return new float[]{
            a[3] * b[0] + a[0] * b[3] + a[1] * b[2] - a[2] * b[1],
            a[3] * b[1] - a[0] * b[2] + a[1] * b[3] + a[2] * b[0],
            a[3] * b[2] + a[0] * b[1] - a[1] * b[0] + a[2] * b[3],
            a[3] * b[3] - a[0] * b[0] - a[1] * b[1] - a[2] * b[2]};
    }

    private static CFrame fromQuat(float px, float py, float pz, float[] q) {
        float x = q[0], y = q[1], z = q[2], w = q[3];
        float xx = x * x, yy = y * y, zz = z * z;
        float xy = x * y, xz = x * z, yz = y * z, xw = x * w, yw = y * w, zw = z * w;
        return new CFrame(px, py, pz,
                1.0f - 2.0f * (yy + zz), 2.0f * (xy - zw), 2.0f * (xz + yw),
                2.0f * (xy + zw), 1.0f - 2.0f * (xx + zz), 2.0f * (yz - xw),
                2.0f * (xz - yw), 2.0f * (yz + xw), 1.0f - 2.0f * (xx + yy));
    }

    // ------------------------------------------------------------------ JSON (stored inside the saved script)

    private static double r5(float v) {
        if (Float.isNaN(v) || Float.isInfinite(v)) {
            return 0.0d;
        }
        return Math.round(v * 100000.0f) / 100000.0d;
    }

    private static float[] comps(CFrame c) {
        return new float[]{c.getX(), c.getY(), c.getZ(), c.getR00(), c.getR01(), c.getR02(),
            c.getR10(), c.getR11(), c.getR12(), c.getR20(), c.getR21(), c.getR22()};
    }

    private static CFrame cfFrom(JSONArray a) {
        return new CFrame((float) a.optDouble(0), (float) a.optDouble(1), (float) a.optDouble(2),
                (float) a.optDouble(3), (float) a.optDouble(4), (float) a.optDouble(5),
                (float) a.optDouble(6), (float) a.optDouble(7), (float) a.optDouble(8),
                (float) a.optDouble(9), (float) a.optDouble(10), (float) a.optDouble(11));
    }

    public String toJson() throws Exception {
        JSONObject o = new JSONObject();
        o.put("v", 2);
        o.put("name", name);
        o.put("fps", fps);
        o.put("length", length);
        o.put("loop", loop);
        o.put("priority", priority);
        JSONArray ta = new JSONArray();
        for (Track t : tracks) {
            JSONObject to = new JSONObject();
            to.put("p", t.path);
            to.put("l", t.label);
            to.put("k", t.kind);
            if (t.pose != null) {
                to.put("pose", t.pose);
            }
            if (t.parent != null) {
                to.put("par", t.parent);
            }
            JSONArray ka = new JSONArray();
            for (Key k : t.keys) {
                JSONArray ko = new JSONArray();
                ko.put(k.frame);
                JSONArray cf = new JSONArray();
                for (float f : comps(k.cf)) {
                    cf.put(r5(f));
                }
                ko.put(cf);
                ko.put(k.ease);
                ko.put(k.dir);
                ko.put(r5(k.p1));
                ko.put(r5(k.p2));
                ka.put(ko);
            }
            to.put("keys", ka);
            ta.put(to);
        }
        o.put("tracks", ta);
        JSONArray ma = new JSONArray();
        for (Marker m : markers) {
            JSONArray mo = new JSONArray();
            mo.put(m.frame);
            mo.put(m.name);
            mo.put(m.value);
            ma.put(mo);
        }
        o.put("markers", ma);
        return o.toString();
    }

    /** Reads a v2 animation. Returns null when the text is not valid. */
    public static SunAnim fromJson(JSONObject o) {
        try {
            SunAnim a = new SunAnim();
            a.name = o.optString("name", "Anim1");
            a.fps = Math.max(1, Math.min(240, o.optInt("fps", 30)));
            a.length = Math.max(1, o.optInt("length", 60));
            a.loop = o.optBoolean("loop", true);
            a.priority = Math.max(0, Math.min(3, o.optInt("priority", 3)));
            JSONArray ta = o.getJSONArray("tracks");
            for (int i = 0; i < ta.length(); i++) {
                JSONObject to = ta.getJSONObject(i);
                Track t = new Track(to.getString("p"), to.optString("l", to.getString("p")), to.optInt("k", PART));
                t.pose = to.has("pose") ? to.getString("pose") : null;
                t.parent = to.has("par") ? to.getString("par") : null;
                JSONArray ka = to.getJSONArray("keys");
                for (int j = 0; j < ka.length(); j++) {
                    JSONArray ko = ka.getJSONArray(j);
                    Key k = new Key(ko.getInt(0), cfFrom(ko.getJSONArray(1)));
                    k.ease = ko.optString(2, "Linear");
                    k.dir = ko.optInt(3, 0);
                    k.p1 = (float) ko.optDouble(4, SunEase.defaultP1(k.ease));
                    k.p2 = (float) ko.optDouble(5, SunEase.defaultP2(k.ease));
                    t.keys.add(k);
                }
                a.tracks.add(t);
            }
            JSONArray ma = o.optJSONArray("markers");
            if (ma != null) {
                for (int i = 0; i < ma.length(); i++) {
                    JSONArray mo = ma.getJSONArray(i);
                    Marker m = new Marker(mo.getInt(0), mo.optString(1, "Marker"));
                    m.value = mo.optString(2, "");
                    a.markers.add(m);
                }
            }
            a.normalize();
            return a;
        } catch (Throwable t) {
            return null;
        }
    }

    /** Reads the first-generation format (seconds, absolute part CFrames, linear only). Tracks become PART tracks. */
    public static SunAnim fromLegacyJson(JSONObject o) {
        try {
            SunAnim a = new SunAnim();
            a.name = o.optString("name", "Anim1");
            a.fps = 30;
            a.loop = o.optBoolean("loop", true);
            a.length = Math.max(1, Math.round((float) o.optDouble("length", 2.0d) * a.fps));
            JSONArray pj = o.getJSONArray("parts");
            for (int i = 0; i < pj.length(); i++) {
                String p = pj.getString(i);
                int slash = p.lastIndexOf('/');
                a.tracks.add(new Track(p, slash >= 0 ? p.substring(slash + 1) : p, PART));
            }
            JSONArray kj = o.getJSONArray("keys");
            for (int k = 0; k < kj.length(); k++) {
                JSONObject ko = kj.getJSONObject(k);
                int frame = Math.round((float) ko.getDouble("t") * a.fps);
                JSONArray cfs = ko.getJSONArray("cf");
                for (int i = 0; i < cfs.length() && i < a.tracks.size(); i++) {
                    a.tracks.get(i).put(new Key(frame, cfFrom(cfs.getJSONArray(i))));
                }
            }
            a.normalize();
            return a;
        } catch (Throwable t) {
            return null;
        }
    }

    // ------------------------------------------------------------------ runtime script

    private static String luaStr(String s) {
        StringBuilder sb = new StringBuilder("\"");
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '\\' || c == '"') {
                sb.append('\\').append(c);
            } else if (c == '\n') {
                sb.append("\\n");
            } else if (c == '\r') {
                sb.append("\\r");
            } else if (c < 32) {
                sb.append(' ');
            } else {
                sb.append(c);
            }
        }
        return sb.append('"').toString();
    }

    private static String num(float v) {
        return String.format(Locale.US, "%.5f", r5(v)).replaceAll("0+$", "").replaceAll("\\.$", "");
    }

    /** The Script source: the editable data in a comment header plus a self-contained player. */
    public String buildScript() throws Exception {
        String json = toJson().replace("]=]", "] =]");
        StringBuilder sb = new StringBuilder();
        sb.append(HEADER).append('\n').append(json).append("\n]=]\n");
        sb.append("-- Generated by the Mobile Studio Animator. Edit the animation there; changes made here are overwritten on save.\n");
        sb.append("local root = script.Parent\n");
        sb.append("local FPS = ").append(fps).append('\n');
        sb.append("local LENGTH = ").append(length).append('\n');
        sb.append("local LOOP = ").append(loop ? "true" : "false").append('\n');
        sb.append("local TRACKS = {\n");
        for (Track t : tracks) {
            if (t.keys.isEmpty()) {
                continue;
            }
            sb.append("\t{path = ").append(luaStr(t.path)).append(", kind = ").append(t.kind).append(", keys = {\n");
            for (Key k : t.keys) {
                sb.append("\t\t{f = ").append(k.frame).append(", cf = {");
                float[] c = comps(k.cf);
                for (int i = 0; i < c.length; i++) {
                    sb.append(i == 0 ? "" : ", ").append(num(c[i]));
                }
                sb.append("}, e = ").append(luaStr(k.ease)).append(", d = ").append(k.dir)
                        .append(", a = ").append(num(k.p1)).append(", b = ").append(num(k.p2)).append("},\n");
            }
            sb.append("\t}},\n");
        }
        sb.append("}\n\n");
        sb.append(SunEase.luauSource()).append('\n');
        sb.append("local function find(path)\n"
                + "\tlocal o = root\n"
                + "\tfor name in string.gmatch(path, \"[^/]+\") do\n"
                + "\t\to = o and o:FindFirstChild(name)\n"
                + "\tend\n"
                + "\treturn o\n"
                + "end\n\n"
                + "local function toCF(t)\n"
                + "\treturn CFrame.new(t[1], t[2], t[3], t[4], t[5], t[6], t[7], t[8], t[9], t[10], t[11], t[12])\n"
                + "end\n\n"
                + "local targets = {}\n"
                + "for i, tr in ipairs(TRACKS) do\n"
                + "\ttargets[i] = find(tr.path)\n"
                + "\tfor _, k in ipairs(tr.keys) do k.c = toCF(k.cf) end\n"
                + "end\n\n"
                + "local function sample(tr, f)\n"
                + "\tlocal keys = tr.keys\n"
                + "\tlocal n = #keys\n"
                + "\tif n == 1 or f <= keys[1].f then return keys[1].c end\n"
                + "\tif f >= keys[n].f then return keys[n].c end\n"
                + "\tlocal i = 1\n"
                + "\twhile i < n - 1 and keys[i + 1].f <= f do i = i + 1 end\n"
                + "\tlocal a, b = keys[i], keys[i + 1]\n"
                + "\tlocal x = (f - a.f) / (b.f - a.f)\n"
                + "\treturn a.c:Lerp(b.c, ease(a.e, a.d, x, a.a, a.b))\n"
                + "end\n\n"
                + "local function apply(f)\n"
                + "\tfor i, tr in ipairs(TRACKS) do\n"
                + "\t\tlocal inst = targets[i]\n"
                + "\t\tif inst then\n"
                + "\t\t\tlocal v = sample(tr, f)\n"
                + "\t\t\tif tr.kind == 0 then inst.Transform = v else inst.CFrame = v end\n"
                + "\t\tend\n"
                + "\tend\n"
                + "end\n\n"
                + "local t = 0\n"
                + "apply(0)\n"
                + "while true do\n"
                + "\tlocal dt = task.wait()\n"
                + "\tt = t + dt * FPS\n"
                + "\tif t >= LENGTH then\n"
                + "\t\tif LOOP then\n"
                + "\t\t\tt = t % math.max(1, LENGTH)\n"
                + "\t\telse\n"
                + "\t\t\tapply(LENGTH)\n"
                + "\t\t\tbreak\n"
                + "\t\tend\n"
                + "\tend\n"
                + "\tapply(t)\n"
                + "end\n");
        return sb.toString();
    }

    /** Reads an animation back out of a script made by {@link #buildScript()} (or the first-generation animator). */
    public static SunAnim fromScript(String src) {
        try {
            int a = src.indexOf(HEADER);
            int b = src.indexOf("\n]=]", a);
            if (a < 0 || b < 0) {
                return null;
            }
            JSONObject o = new JSONObject(src.substring(a + HEADER.length(), b).trim());
            return o.optInt("v", 1) >= 2 ? fromJson(o) : fromLegacyJson(o);
        } catch (Throwable t) {
            return null;
        }
    }

    // ------------------------------------------------------------------ KeyframeSequence

    /** Roblox PoseEasingStyle: Linear 0, Constant 1, Elastic 2, Cubic 3, Bounce 4. */
    private static int robloxStyle(String fam) {
        switch (fam) {
            case "Linear":
                return 0;
            case "Constant":
                return 1;
            case "Elastic":
                return 2;
            case "Bounce":
                return 4;
            default:
                return 3;
        }
    }

    /** The in-app player reads In/Out swapped for the legacy styles, so the same swap is written here. */
    private static int robloxDir(String fam, int dir) {
        if (!SunEase.hasDirection(fam)) {
            return 0;
        }
        return dir >= 2 ? 2 : 1 - dir;
    }

    private static String familyFromRoblox(int style) {
        switch (style) {
            case 0:
                return "Linear";
            case 1:
                return "Constant";
            case 2:
                return "Elastic";
            case 4:
                return "Bounce";
            default:
                return "Cubic";
        }
    }

    private static int dirFromRoblox(String fam, int d) {
        if (!SunEase.hasDirection(fam)) {
            return 0;
        }
        return d >= 2 ? 2 : 1 - d;
    }

    /** Builds a KeyframeSequence (joint tracks only). Part tracks have no Motor6D, so they are left out. */
    public Instance toKeyframeSequence() {
        Instance seq = InstanceFactory.INSTANCE.create("KeyframeSequence");
        seq.setName(name);
        seq.set("Loop", loop, false);
        seq.set("Priority", new EnumValue(PRIORITY_ENUM[Math.max(0, Math.min(3, priority))]), false);
        List<Track> joints = new ArrayList<>();
        for (Track t : tracks) {
            if (t.kind == JOINT && t.pose != null && !t.keys.isEmpty()) {
                joints.add(t);
            }
        }
        TreeSet<Integer> frames = new TreeSet<>();
        frames.add(0);
        frames.add(Math.max(1, length));
        for (Track t : joints) {
            for (Key k : t.keys) {
                frames.add(k.frame);
            }
        }
        for (Marker m : markers) {
            frames.add(m.frame);
        }
        for (int f : frames) {
            Instance kf = InstanceFactory.INSTANCE.create("Keyframe");
            kf.setName("Keyframe");
            kf.set("Time", f / (float) fps, false);
            kf.setParent(seq, -1);
            Map<String, Instance> poses = new HashMap<>();
            for (Track t : joints) {
                Key own = t.at(f);
                CFrame v = own != null ? own.cf : sample(t, f);
                Instance p = InstanceFactory.INSTANCE.create("Pose");
                p.setName(t.pose);
                p.set("CFrame", v, false);
                Key styleKey = own != null ? own : lastKeyAtOrBefore(t, f);
                String fam = styleKey != null ? styleKey.ease : "Linear";
                int dir = styleKey != null ? styleKey.dir : 0;
                p.set("EasingStyle", new EnumValue(robloxStyle(fam)), false);
                p.set("EasingDirection", new EnumValue(robloxDir(fam, dir)), false);
                p.set("Weight", 1.0f, false);
                poses.put(t.pose, p);
            }
            Map<String, Instance> containers = new HashMap<>();
            for (Track t : joints) {
                Instance p = poses.get(t.pose);
                Instance par = t.parent == null ? null : poses.get(t.parent);
                if (par == null && t.parent != null) {
                    par = containers.get(t.parent);
                    if (par == null) {
                        par = InstanceFactory.INSTANCE.create("Pose");
                        par.setName(t.parent);
                        par.set("CFrame", CFrame.INSTANCE.getIDENTITY(), false);
                        par.set("Weight", 0.0f, false);
                        par.setParent(kf, -1);
                        containers.put(t.parent, par);
                    }
                }
                p.setParent(par != null && par != p ? par : kf, -1);
            }
            for (Marker m : markers) {
                if (m.frame == f) {
                    try {
                        Instance mk = InstanceFactory.INSTANCE.create("KeyframeMarker");
                        mk.setName(m.name);
                        mk.set("Value", m.value, false);
                        mk.setParent(kf, -1);
                    } catch (Throwable ignored) {
                    }
                }
            }
        }
        return seq;
    }

    private static Key lastKeyAtOrBefore(Track t, int f) {
        Key best = null;
        for (Key k : t.keys) {
            if (k.frame <= f) {
                best = k;
            }
        }
        return best;
    }

    /**
     * Replaces the keys of every JOINT track whose pose name is found in the sequence. Returns how many
     * tracks were filled in.
     */
    public int importKeyframeSequence(Instance seq) {
        Map<String, Track> byPose = new HashMap<>();
        for (Track t : tracks) {
            if (t.kind == JOINT && t.pose != null) {
                byPose.put(t.pose, t);
            }
        }
        Map<Track, List<Key>> found = new HashMap<>();
        int maxFrame = 0;
        for (Instance kf : seq.getChildren()) {
            if (!"Keyframe".equals(kf.getClassName())) {
                continue;
            }
            Object to = kf.get("Time");
            float time = to instanceof Number ? ((Number) to).floatValue() : 0.0f;
            int frame = Math.max(0, Math.round(time * fps));
            maxFrame = Math.max(maxFrame, frame);
            walkPoses(kf, frame, byPose, found);
            for (Instance c : kf.getChildren()) {
                if ("KeyframeMarker".equals(c.getClassName())) {
                    Object v = c.get("Value");
                    Marker m = new Marker(frame, c.getName());
                    m.value = v == null ? "" : String.valueOf(v);
                    markers.add(m);
                }
            }
        }
        for (Map.Entry<Track, List<Key>> e : found.entrySet()) {
            e.getKey().keys.clear();
            e.getKey().keys.addAll(e.getValue());
        }
        Object lp = seq.get("Loop");
        if (lp instanceof Boolean) {
            loop = (Boolean) lp;
        }
        Object pr = seq.get("Priority");
        if (pr instanceof EnumValue) {
            int v = ((EnumValue) pr).getValue();
            priority = v == 1000 ? 0 : v == 0 ? 1 : v == 1 ? 2 : 3;
        }
        length = Math.max(length, maxFrame);
        normalize();
        return found.size();
    }

    private void walkPoses(Instance parent, int frame, Map<String, Track> byPose, Map<Track, List<Key>> out) {
        for (Instance c : parent.getChildren()) {
            if (!"Pose".equals(c.getClassName())) {
                continue;
            }
            Track t = byPose.get(c.getName());
            Object cfo = c.get("CFrame");
            if (t != null && cfo instanceof CFrame) {
                Key k = new Key(frame, (CFrame) cfo);
                Object so = c.get("EasingStyle");
                Object dro = c.get("EasingDirection");
                String fam = familyFromRoblox(so instanceof EnumValue ? ((EnumValue) so).getValue() : 0);
                k.setEase(fam);
                k.dir = dirFromRoblox(fam, dro instanceof EnumValue ? ((EnumValue) dro).getValue() : 0);
                List<Key> list = out.get(t);
                if (list == null) {
                    list = new ArrayList<>();
                    out.put(t, list);
                }
                list.add(k);
            }
            walkPoses(c, frame, byPose, out);
        }
    }
}

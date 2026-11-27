package com.goydevv.studrblx.engine;

import com.goydevv.studrblx.model.Instance;
import com.goydevv.studrblx.model.InstanceFactory;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Collision rules shared by the playtest physics, PhysicsService and the Collision dialog.
 *
 * A part is solid for the player when
 *  - its own CanCollide is not false,
 *  - no Model / Folder above it has CanCollide (property or attribute) set to false  (model-level "collision off"),
 *  - the player is not in noclip, and
 *  - the player's collision group and the part's CollisionGroup are allowed to collide.
 *
 * Groups and the player settings are saved in the place as a StringValue named {@link #CONFIG} inside ServerStorage,
 * so they survive saving and reopening.
 */
public final class Collide {
    public static final String CONFIG = "__CollisionConfig";
    public static final String DEFAULT = "Default";

    private static final LinkedHashSet<String> groups = new LinkedHashSet<>();
    private static final HashSet<String> notCollidable = new HashSet<>();
    /** The player walks through everything. */
    public static volatile boolean playerNoclip;
    /** Collision group of the player's character. null = use the group of its HumanoidRootPart (normally Default). */
    public static volatile String playerGroup;

    static {
        groups.add(DEFAULT);
    }

    private Collide() {
    }

    // ------------------------------------------------------------------ groups

    private static String pairKey(String a, String b) {
        return a.compareTo(b) <= 0 ? a + "\u0001" + b : b + "\u0001" + a;
    }

    public static synchronized void reset() {
        groups.clear();
        groups.add(DEFAULT);
        notCollidable.clear();
        playerNoclip = false;
        playerGroup = null;
    }

    public static synchronized List<String> names() {
        return new ArrayList<>(groups);
    }

    public static synchronized boolean registered(String name) {
        return groups.contains(name);
    }

    public static synchronized void register(String name) {
        if (name != null && name.length() > 0 && groups.size() < 64) {
            groups.add(name);
        }
    }

    public static synchronized void unregister(String name) {
        if (DEFAULT.equals(name) || !groups.remove(name)) {
            return;
        }
        List<String> drop = new ArrayList<>();
        for (String k : notCollidable) {
            if (k.startsWith(name + "\u0001") || k.endsWith("\u0001" + name)) {
                drop.add(k);
            }
        }
        notCollidable.removeAll(drop);
        if (name.equals(playerGroup)) {
            playerGroup = null;
        }
    }

    public static synchronized void rename(String from, String to) {
        if (DEFAULT.equals(from) || to == null || to.length() == 0 || !groups.contains(from) || groups.contains(to)) {
            return;
        }
        List<String> all = new ArrayList<>(groups);
        groups.clear();
        for (String g : all) {
            groups.add(g.equals(from) ? to : g);
        }
        Set<String> moved = new HashSet<>();
        for (String k : notCollidable) {
            String[] ab = k.split("\u0001", 2);
            if (ab.length == 2) {
                moved.add(pairKey(ab[0].equals(from) ? to : ab[0], ab[1].equals(from) ? to : ab[1]));
            }
        }
        notCollidable.clear();
        notCollidable.addAll(moved);
        if (from.equals(playerGroup)) {
            playerGroup = to;
        }
    }

    public static synchronized void setCollidable(String a, String b, boolean collide) {
        register(a);
        register(b);
        if (collide) {
            notCollidable.remove(pairKey(a, b));
        } else {
            notCollidable.add(pairKey(a, b));
        }
    }

    /** Unknown group names behave like groups that collide with everything. */
    public static synchronized boolean collidable(String a, String b) {
        if (a == null || b == null) {
            return true;
        }
        return !notCollidable.contains(pairKey(a, b));
    }

    public static synchronized int idOf(String name) {
        int i = 0;
        for (String g : groups) {
            if (g.equals(name)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public static synchronized String nameOf(int id) {
        int i = 0;
        for (String g : groups) {
            if (i++ == id) {
                return g;
            }
        }
        return null;
    }

    // ------------------------------------------------------------------ rules

    public static String groupOf(Instance p) {
        Object g = p.get("CollisionGroup");
        return g instanceof String && ((String) g).length() > 0 ? (String) g : DEFAULT;
    }

    private static boolean off(Instance i) {
        if (Boolean.FALSE.equals(i.get("CanCollide"))) {
            return true;
        }
        return Boolean.FALSE.equals(i.getAttributes().get("CanCollide"));
    }

    /** CanCollide of the part itself plus "collision off" set on any Model or Folder above it. */
    public static boolean partOn(Instance p) {
        if (Boolean.FALSE.equals(p.get("CanCollide"))) {
            return false;
        }
        Instance a = p.getParent();
        for (int depth = 0; a != null && depth < 40; depth++) {
            if (off(a)) {
                return false;
            }
            a = a.getParent();
        }
        return true;
    }

    private static Instance cachedChar;
    private static String cachedCharGroup = DEFAULT;
    private static long cachedAt;

    private static String characterGroup(Instance character) {
        String forced = playerGroup;
        if (forced != null) {
            return forced;
        }
        long now = System.currentTimeMillis();
        if (character != cachedChar || now - cachedAt > 250L) {
            Instance root = character.findFirstChild("HumanoidRootPart", false);
            cachedCharGroup = root != null ? groupOf(root) : DEFAULT;
            cachedChar = character;
            cachedAt = now;
        }
        return cachedCharGroup;
    }

    private static boolean noclip(Instance character) {
        if (playerNoclip || off(character)) {
            return true;
        }
        if (Boolean.TRUE.equals(character.getAttributes().get("Noclip"))) {
            return true;
        }
        Instance hum = character.findFirstChild("Humanoid", false);
        return hum != null && Boolean.TRUE.equals(hum.getAttributes().get("Noclip"));
    }

    /** Is this (non character) part solid for the player's character? */
    public static boolean playerHits(Instance character, Instance part) {
        if (character == null) {
            return partOn(part);
        }
        if (noclip(character) || !partOn(part)) {
            return false;
        }
        return collidable(characterGroup(character), groupOf(part));
    }

    /** Do two parts collide with each other? */
    public static boolean partsHit(Instance a, Instance b) {
        return partOn(a) && partOn(b) && collidable(groupOf(a), groupOf(b));
    }

    // ------------------------------------------------------------------ saved with the place

    private static Instance configValue(Instance dm, boolean create) {
        Instance ss = create ? ((com.goydevv.studrblx.model.DataModel) dm).getService("ServerStorage")
                : ((com.goydevv.studrblx.model.DataModel) dm).findService("ServerStorage");
        if (ss == null) {
            return null;
        }
        Instance v = ss.findFirstChild(CONFIG, false);
        if (v == null && create) {
            v = InstanceFactory.INSTANCE.create("StringValue");
            v.setName(CONFIG);
            v.setParent(ss, -1);
        }
        return v;
    }

    /** Loads groups and player settings from the place. Without a saved config everything is reset to the defaults. */
    public static synchronized void loadFrom(com.goydevv.studrblx.model.DataModel dm) {
        reset();
        try {
            Instance v = configValue(dm, false);
            Object text = v == null ? null : v.get("Value");
            if (!(text instanceof String)) {
                return;
            }
            for (String line : ((String) text).split("\n")) {
                line = line.trim();
                int eq = line.indexOf('=');
                if (eq <= 0) {
                    continue;
                }
                String k = line.substring(0, eq);
                String val = line.substring(eq + 1);
                if (k.equals("noclip")) {
                    playerNoclip = val.equals("1");
                } else if (k.equals("playergroup")) {
                    playerGroup = val.length() == 0 ? null : val;
                } else if (k.equals("group")) {
                    register(val);
                } else if (k.equals("off")) {
                    String[] ab = val.split("\\|", 2);
                    if (ab.length == 2) {
                        setCollidable(ab[0], ab[1], false);
                    }
                }
            }
        } catch (Throwable ignored) {
        }
    }

    public static synchronized void saveTo(com.goydevv.studrblx.model.DataModel dm) {
        StringBuilder sb = new StringBuilder("v=1\n");
        sb.append("noclip=").append(playerNoclip ? "1" : "0").append('\n');
        sb.append("playergroup=").append(playerGroup == null ? "" : playerGroup).append('\n');
        for (String g : groups) {
            if (!DEFAULT.equals(g)) {
                sb.append("group=").append(g).append('\n');
            }
        }
        for (String k : notCollidable) {
            sb.append("off=").append(k.replace("\u0001", "|")).append('\n');
        }
        Instance v = configValue(dm, true);
        if (v != null) {
            v.set("Value", sb.toString(), true);
        }
    }
}

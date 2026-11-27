package com.goydevv.studrblx.engine;

import com.goydevv.studrblx.model.DataModel;
import com.goydevv.studrblx.model.Instance;
import com.goydevv.studrblx.net.AssetFetcher;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Custom animations for the player's character.
 *
 * Put a Folder (or Model) named PlayerAnimations anywhere in the place, for example in
 * StarterPlayer > StarterCharacterScripts, and fill it with:
 *   - KeyframeSequences named idle, walk, run, jump, fall, climb, sit (idle2, walk_2 ... are extra variants), or
 *   - Animation objects with those names (an AnimationId, or a KeyframeSequence inside), or
 *   - a Folder per state (idle, walk, ...) holding KeyframeSequences / Animations. A NumberValue named Weight
 *     inside a variant makes it more likely to be picked.
 * A Roblox style "Animate" folder (idle/walk/run folders with Animation1, Animation2 inside) works too.
 * If the pack has sub-folders named R6 and R15, the one matching the character is used.
 */
public final class CustomAnims {
    public static final String[] STATES = {"idle", "walk", "run", "jump", "fall", "climb", "sit"};
    public static final String PACK = "PlayerAnimations";

    private CustomAnims() {
    }

    /** One variant of a state. Either a ready clip, or an asset id that is downloaded on demand. */
    public static final class Clip {
        public final AnimClip clip;
        public final long id;
        public final int weight;

        Clip(AnimClip clip, long id, int weight) {
            this.clip = clip;
            this.id = id;
            this.weight = Math.max(1, weight);
        }

        public AnimClip get() {
            return clip != null ? clip : AnimLibrary.INSTANCE.get(id);
        }
    }

    public static Instance findPack(DataModel dm) {
        List<Instance> all = new ArrayList<>();
        dm.descendants(all);
        Instance starter = dm.findService("StarterPlayer");
        Instance best = null;
        for (Instance i : all) {
            String n = i.getName();
            boolean inStarter = starter != null && i.isDescendantOf(starter);
            boolean named = PACK.equalsIgnoreCase(n)
                    || (inStarter && "Animate".equalsIgnoreCase(n) && i.getChildren().size() > 0);
            if (!named) {
                continue;
            }
            if (inStarter) {
                return i;
            }
            if (best == null) {
                best = i;
            }
        }
        return best;
    }

    /** State name for an instance name: "Idle", "idle2", "walk_3" and "jumping" all give their state. */
    static String stateOf(String name) {
        String n = name.toLowerCase(Locale.US);
        for (String s : STATES) {
            if (n.startsWith(s)) {
                return s;
            }
        }
        return null;
    }

    public static Map<String, List<Clip>> find(DataModel dm, boolean r15) {
        Map<String, List<Clip>> out = new HashMap<>();
        try {
            Instance pack = findPack(dm);
            if (pack == null) {
                return out;
            }
            Instance rigSpecific = pack.findFirstChild(r15 ? "R15" : "R6", false);
            Instance other = pack.findFirstChild(r15 ? "R6" : "R15", false);
            for (Instance c : pack.getChildren()) {
                if (c == other) {
                    continue;
                }
                if (c == rigSpecific) {
                    for (Instance cc : c.getChildren()) {
                        collect(cc, out);
                    }
                } else {
                    collect(c, out);
                }
            }
        } catch (Throwable ignored) {
        }
        return out;
    }

    private static void collect(Instance c, Map<String, List<Clip>> out) {
        String state = stateOf(c.getName());
        if (state == null) {
            return;
        }
        if (c.isA("Folder") || c.isA("Configuration") || (c.isA("Model"))) {
            for (Instance v : c.getChildren()) {
                add(state, v, out);
            }
        } else {
            add(state, c, out);
        }
    }

    private static void add(String state, Instance v, Map<String, List<Clip>> out) {
        int weight = 1;
        Instance w = v.findFirstChild("Weight", false);
        if (w != null && w.get("Value") instanceof Number) {
            weight = Math.round(((Number) w.get("Value")).floatValue());
        }
        Clip clip = null;
        if ("KeyframeSequence".equals(v.getClassName())) {
            AnimClip a = AnimClip.INSTANCE.from(v);
            if (a != null) {
                clip = new Clip(a, 0L, weight);
            }
        } else if ("Animation".equals(v.getClassName())) {
            Instance seq = null;
            List<Instance> kids = new ArrayList<>();
            v.descendants(kids);
            for (Instance k : kids) {
                if ("KeyframeSequence".equals(k.getClassName())) {
                    seq = k;
                    break;
                }
            }
            AnimClip a = seq == null ? null : AnimClip.INSTANCE.from(seq);
            if (a != null) {
                clip = new Clip(a, 0L, weight);
            } else {
                Long id = AssetFetcher.INSTANCE.idOf(v.get("AnimationId"));
                if (id != null) {
                    clip = new Clip(null, id.longValue(), weight);
                }
            }
        }
        if (clip == null) {
            return;
        }
        List<Clip> list = out.get(state);
        if (list == null) {
            list = new ArrayList<>();
            out.put(state, list);
        }
        list.add(clip);
        if (clip.clip == null) {
            AnimLibrary.INSTANCE.get(clip.id);
        }
    }
}

package com.goydevv.studrblx.engine;

/**
 * Easing curves for the Animator. 13 families x 4 directions (In, Out, InOut, OutIn), the same set the
 * Sun Animator plugin offers. Every family is defined by one "in" curve; the other directions are derived
 * from it, so the Luau runtime script written by SunAnim can use identical formulas.
 */
public final class SunEase {
    public static final String[] FAMILIES = {
        "Linear", "Constant", "Sine", "Quad", "Cubic", "Quart", "Quint", "Sextic",
        "Expo", "Circ", "Back", "Bounce", "Elastic"
    };
    public static final String[] DIRS = {"In", "Out", "InOut", "OutIn"};
    /** One color per family (same order as FAMILIES), used for "easing colors" on the timeline. */
    public static final int[] COLORS = {
        0xFF1765B8, 0xFF756C3C, 0xFF00D717, 0xFFE91818, 0xFFB81785, 0xFFFD5B03, 0xFFFEC606, 0xFFAC8DE0,
        0xFF18C8C8, 0xFF8A8A8A, 0xFFFF7BAC, 0xFF7BD0FF, 0xFFB6E34B
    };
    public static final float BACK_DEFAULT = 1.70158f;
    public static final float AMP_DEFAULT = 1.0f;
    public static final float PERIOD_DEFAULT = 0.3f;

    private SunEase() {
    }

    public static int indexOf(String family) {
        for (int i = 0; i < FAMILIES.length; i++) {
            if (FAMILIES[i].equals(family)) {
                return i;
            }
        }
        return 0;
    }

    public static int color(String family) {
        return COLORS[indexOf(family)];
    }

    /** Linear and Constant have no direction. */
    public static boolean hasDirection(String family) {
        return !"Linear".equals(family) && !"Constant".equals(family);
    }

    public static boolean isBack(String family) {
        return "Back".equals(family);
    }

    public static boolean isElastic(String family) {
        return "Elastic".equals(family);
    }

    /** p1 = Back overshoot or Elastic amplitude. p2 = Elastic period. */
    public static float defaultP1(String family) {
        return "Back".equals(family) ? BACK_DEFAULT : AMP_DEFAULT;
    }

    public static float defaultP2(String family) {
        return PERIOD_DEFAULT;
    }

    /** Eases x (0..1) and returns the eased progress. Back and Elastic may leave 0..1 on purpose. */
    public static float apply(String family, int dir, float x, float p1, float p2) {
        if ("Linear".equals(family)) {
            return x;
        }
        if ("Constant".equals(family)) {
            return x >= 1.0f ? 1.0f : 0.0f;
        }
        if (x <= 0.0f) {
            return 0.0f;
        }
        if (x >= 1.0f) {
            return 1.0f;
        }
        switch (dir) {
            case 1:
                return 1.0f - in(family, 1.0f - x, p1, p2);
            case 2:
                return x < 0.5f ? in(family, 2.0f * x, p1, p2) / 2.0f : 1.0f - in(family, 2.0f - 2.0f * x, p1, p2) / 2.0f;
            case 3:
                return x < 0.5f ? (1.0f - in(family, 1.0f - 2.0f * x, p1, p2)) / 2.0f : (1.0f + in(family, 2.0f * x - 1.0f, p1, p2)) / 2.0f;
            default:
                return in(family, x, p1, p2);
        }
    }

    private static float in(String family, float x, float p1, float p2) {
        switch (family) {
            case "Sine":
                return 1.0f - (float) Math.cos(x * Math.PI / 2.0);
            case "Quad":
                return x * x;
            case "Cubic":
                return x * x * x;
            case "Quart":
                return x * x * x * x;
            case "Quint":
                return x * x * x * x * x;
            case "Sextic":
                return x * x * x * x * x * x;
            case "Expo":
                return (float) ((Math.pow(2.0, 10.0 * x) - 1.0) / 1023.0);
            case "Circ":
                return 1.0f - (float) Math.sqrt(Math.max(0.0f, 1.0f - x * x));
            case "Back":
                return x * x * ((p1 + 1.0f) * x - p1);
            case "Bounce":
                return 1.0f - bounceOut(1.0f - x);
            case "Elastic":
                return elasticIn(x, p1, p2);
            default:
                return x;
        }
    }

    private static float bounceOut(float t) {
        if (t < 1.0f / 2.75f) {
            return 7.5625f * t * t;
        }
        if (t < 2.0f / 2.75f) {
            t -= 1.5f / 2.75f;
            return 7.5625f * t * t + 0.75f;
        }
        if (t < 2.5f / 2.75f) {
            t -= 2.25f / 2.75f;
            return 7.5625f * t * t + 0.9375f;
        }
        t -= 2.625f / 2.75f;
        return 7.5625f * t * t + 0.984375f;
    }

    /** Same shape as the plugin's elastic (amplitude a, period p), for a 0..1 curve. */
    private static float elasticIn(float x, float a, float p) {
        if (p <= 0.0f) {
            p = 0.3f;
        }
        float t = x - 1.0f;
        float s;
        float f = 1.0f;
        if (a < 1.0f) {
            s = p / 4.0f;
            float t2 = Math.abs(s);
            if (a != 0.0f) {
                f = f * a;
            } else {
                f = 0.0f;
            }
            if (Math.abs(t) < t2) {
                float l = Math.abs(t) / t2;
                f = f * l + (1.0f - l);
            }
            a = 1.0f;
        } else {
            s = p / (float) (2.0 * Math.PI) * (float) Math.asin(1.0 / a);
        }
        return (float) (-f * (a * Math.pow(2.0, 10.0 * t) * Math.sin((t - s) * (2.0 * Math.PI) / p)));
    }

    /** The in-curve as Luau source, for the generated runtime script. Kept in sync with in() above. */
    public static String luauSource() {
        return "local function bounceOut(t)\n"
                + "\tif t < 1/2.75 then return 7.5625*t*t\n"
                + "\telseif t < 2/2.75 then t = t - 1.5/2.75 return 7.5625*t*t + 0.75\n"
                + "\telseif t < 2.5/2.75 then t = t - 2.25/2.75 return 7.5625*t*t + 0.9375\n"
                + "\telse t = t - 2.625/2.75 return 7.5625*t*t + 0.984375 end\n"
                + "end\n"
                + "local function elasticIn(x, a, p)\n"
                + "\tif p <= 0 then p = 0.3 end\n"
                + "\tlocal t = x - 1\n"
                + "\tlocal s, f = 0, 1\n"
                + "\tif a < 1 then\n"
                + "\t\ts = p / 4\n"
                + "\t\tf = (a ~= 0) and a or 0\n"
                + "\t\tif math.abs(t) < s then local l = math.abs(t) / s f = f * l + (1 - l) end\n"
                + "\t\ta = 1\n"
                + "\telse\n"
                + "\t\ts = p / (2 * math.pi) * math.asin(1 / a)\n"
                + "\tend\n"
                + "\treturn -f * (a * 2 ^ (10 * t) * math.sin((t - s) * (2 * math.pi) / p))\n"
                + "end\n"
                + "local function fin(fam, x, p1, p2)\n"
                + "\tif fam == \"Sine\" then return 1 - math.cos(x * math.pi / 2)\n"
                + "\telseif fam == \"Quad\" then return x ^ 2\n"
                + "\telseif fam == \"Cubic\" then return x ^ 3\n"
                + "\telseif fam == \"Quart\" then return x ^ 4\n"
                + "\telseif fam == \"Quint\" then return x ^ 5\n"
                + "\telseif fam == \"Sextic\" then return x ^ 6\n"
                + "\telseif fam == \"Expo\" then return (2 ^ (10 * x) - 1) / 1023\n"
                + "\telseif fam == \"Circ\" then return 1 - math.sqrt(math.max(0, 1 - x * x))\n"
                + "\telseif fam == \"Back\" then return x * x * ((p1 + 1) * x - p1)\n"
                + "\telseif fam == \"Bounce\" then return 1 - bounceOut(1 - x)\n"
                + "\telseif fam == \"Elastic\" then return elasticIn(x, p1, p2) end\n"
                + "\treturn x\n"
                + "end\n"
                + "local function ease(fam, dir, x, p1, p2)\n"
                + "\tif fam == \"Linear\" then return x end\n"
                + "\tif fam == \"Constant\" then return x >= 1 and 1 or 0 end\n"
                + "\tif x <= 0 then return 0 elseif x >= 1 then return 1 end\n"
                + "\tif dir == 1 then return 1 - fin(fam, 1 - x, p1, p2)\n"
                + "\telseif dir == 2 then\n"
                + "\t\tif x < 0.5 then return fin(fam, 2 * x, p1, p2) / 2 end\n"
                + "\t\treturn 1 - fin(fam, 2 - 2 * x, p1, p2) / 2\n"
                + "\telseif dir == 3 then\n"
                + "\t\tif x < 0.5 then return (1 - fin(fam, 1 - 2 * x, p1, p2)) / 2 end\n"
                + "\t\treturn (1 + fin(fam, 2 * x - 1, p1, p2)) / 2\n"
                + "\tend\n"
                + "\treturn fin(fam, x, p1, p2)\n"
                + "end\n";
    }
}

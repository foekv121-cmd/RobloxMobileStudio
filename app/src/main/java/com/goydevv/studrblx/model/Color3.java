package com.goydevv.studrblx.model;

import androidx.core.view.ViewCompat;
import java.util.Arrays;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.StringCompanionObject;
import kotlin.math.MathKt;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: DataTypes.kt */
/* JADX INFO: loaded from: classes4.dex */
public final /* data */ class Color3 {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private final float b;
    private final float g;
    private final float r;

    public static /* synthetic */ Color3 copy$default(Color3 color3, float f, float f2, float f3, int i, Object obj) {
        if ((i & 1) != 0) {
            f = color3.r;
        }
        if ((i & 2) != 0) {
            f2 = color3.g;
        }
        if ((i & 4) != 0) {
            f3 = color3.b;
        }
        return color3.copy(f, f2, f3);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final float component1() {
        return this.r;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final float component2() {
        return this.g;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final float component3() {
        return this.b;
    }

    public final Color3 copy(float r, float g, float b) {
        return new Color3(r, g, b);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Color3)) {
            return false;
        }
        Color3 color3 = (Color3) other;
        return Float.compare(this.r, color3.r) == 0 && Float.compare(this.g, color3.g) == 0 && Float.compare(this.b, color3.b) == 0;
    }

    public int hashCode() {
        return (((Float.hashCode(this.r) * 31) + Float.hashCode(this.g)) * 31) + Float.hashCode(this.b);
    }

    public Color3(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public final float getB() {
        return this.b;
    }

    public final float getG() {
        return this.g;
    }

    public final float getR() {
        return this.r;
    }

    public final int toArgb() {
        return (INSTANCE.c8(this.r) << 16) | ViewCompat.MEASURED_STATE_MASK | (INSTANCE.c8(this.g) << 8) | INSTANCE.c8(this.b);
    }

    public final Color3 lerp(Color3 o, float t) {
        Intrinsics.checkNotNullParameter(o, "o");
        return new Color3(this.r + ((o.r - this.r) * t), this.g + ((o.g - this.g) * t), this.b + ((o.b - this.b) * t));
    }

    public final String toHex() {
        StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
        String str = String.format("%02X%02X%02X", Arrays.copyOf(new Object[]{Integer.valueOf(INSTANCE.c8(this.r)), Integer.valueOf(INSTANCE.c8(this.g)), Integer.valueOf(INSTANCE.c8(this.b))}, 3));
        Intrinsics.checkNotNullExpressionValue(str, "format(...)");
        return str;
    }

    public final float[] toHSV() {
        float f;
        float mx = Math.max(this.r, Math.max(this.g, this.b));
        float mn = Math.min(this.r, Math.min(this.g, this.b));
        float d = mx - mn;
        if (d == 0.0f) {
            f = 0.0f;
        } else {
            if (mx == this.r) {
                f = ((this.g - this.b) / d) % 6.0f;
            } else {
                f = (mx > this.g ? 1 : (mx == this.g ? 0 : -1)) == 0 ? ((this.b - this.r) / d) + 2.0f : ((this.r - this.g) / d) + 4.0f;
            }
        }
        float h = f / 6.0f;
        if (h < 0.0f) {
            h += 1.0f;
        }
        return new float[]{h, (mx > 0.0f ? 1 : (mx == 0.0f ? 0 : -1)) == 0 ? 0.0f : d / mx, mx};
    }

    public String toString() {
        return INSTANCE.c8(this.r) + ", " + INSTANCE.c8(this.g) + ", " + INSTANCE.c8(this.b);
    }

    /* JADX INFO: compiled from: DataTypes.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final int c8(float f) {
            return MathKt.roundToInt(RangesKt.coerceIn(f, 0.0f, 1.0f) * 255.0f);
        }

        public final Color3 fromRGB(int r, int g, int b) {
            return new Color3(r / 255.0f, g / 255.0f, b / 255.0f);
        }

        public final Color3 fromArgb(int c) {
            return fromRGB((c >> 16) & 255, (c >> 8) & 255, c & 255);
        }

        public final Color3 fromHex(String h) {
            Integer intOrNull;
            Intrinsics.checkNotNullParameter(h, "h");
            String s = StringsKt.removePrefix(StringsKt.trim((CharSequence) h).toString(), (CharSequence) "#");
            if (s.length() == 6 && (intOrNull = StringsKt.toIntOrNull(s, 16)) != null) {
                return Color3.INSTANCE.fromArgb(intOrNull.intValue());
            }
            return null;
        }

        public final Color3 fromHSV(float h, float s, float v) {
            int i = (int) (h * 6.0f);
            float f = (6.0f * h) - i;
            float p = (1.0f - s) * v;
            float q = (1.0f - (f * s)) * v;
            float t = (1.0f - ((1.0f - f) * s)) * v;
            switch (((i % 6) + 6) % 6) {
                case 0:
                    return new Color3(v, t, p);
                case 1:
                    return new Color3(q, v, p);
                case 2:
                    return new Color3(p, v, t);
                case 3:
                    return new Color3(p, q, v);
                case 4:
                    return new Color3(t, p, v);
                default:
                    return new Color3(v, p, q);
            }
        }
    }
}

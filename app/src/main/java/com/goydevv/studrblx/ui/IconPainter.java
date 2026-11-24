package com.goydevv.studrblx.ui;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.Typeface;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: BuiltinIcons.kt */
/* JADX INFO: loaded from: classes5.dex */
public final class IconPainter {
    public static final int BLUE = -12874773;
    public static final int BROWN = -5211586;
    public static final int DARK = -11907241;
    public static final int DBLUE = -14721104;
    public static final int DGREEN = -13726889;
    public static final int GRAY_L = -5855831;
    public static final int GRAY_R = -8553343;
    public static final int GRAY_T = -2302754;
    public static final int GREEN = -11751344;
    public static final int GUI = -12605845;
    public static final int LBLUE = -7419403;
    public static final int ORANGE = -1013212;
    public static final int PAPER = -920586;
    public static final int PURPLE = -6591520;
    public static final int RED = -1750963;
    public static final int TEAL = -13913178;
    public static final int WHITE = -1;
    public static final int YELLOW = -867792;
    private final Canvas c;
    private final Paint p;

    public IconPainter(Canvas c) {
        Intrinsics.checkNotNullParameter(c, "c");
        this.c = c;
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.SUBPIXEL_TEXT_FLAG);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        this.p = paint;
    }

    public final Canvas getC() {
        return this.c;
    }

    public final int getFg() {
        return Ui.INSTANCE.getTEXT();
    }

    public static /* synthetic */ void rect$default(IconPainter iconPainter, float f, float f2, float f3, float f4, int i, float f5, int i2, Object obj) {
        if ((i2 & 32) != 0) {
            f5 = 0.0f;
        }
        iconPainter.rect(f, f2, f3, f4, i, f5);
    }

    public final void rect(float l, float t, float r, float b, int color, float rad) {
        this.p.setStyle(Paint.Style.FILL);
        this.p.setColor(color);
        this.p.setShader(null);
        if (rad > 0.0f) {
            this.c.drawRoundRect(l, t, r, b, rad, rad, this.p);
        } else {
            this.c.drawRect(l, t, r, b, this.p);
        }
    }

    public static /* synthetic */ void srect$default(IconPainter iconPainter, float f, float f2, float f3, float f4, int i, float f5, float f6, int i2, Object obj) {
        if ((i2 & 32) != 0) {
            f5 = 1.5f;
        }
        iconPainter.srect(f, f2, f3, f4, i, f5, (i2 & 64) != 0 ? 0.0f : f6);
    }

    public final void srect(float l, float t, float r, float b, int color, float w, float rad) {
        this.p.setStyle(Paint.Style.STROKE);
        this.p.setColor(color);
        this.p.setStrokeWidth(w);
        this.p.setShader(null);
        if (rad > 0.0f) {
            this.c.drawRoundRect(l, t, r, b, rad, rad, this.p);
        } else {
            this.c.drawRect(l, t, r, b, this.p);
        }
    }

    public final void box(float l, float t, float r, float b, int fillC, int strokeC, float w, float rad) {
        rect(l, t, r, b, fillC, rad);
        srect(l, t, r, b, strokeC, w, rad);
    }

    public final void circle(float cx, float cy, float r, int color) {
        this.p.setStyle(Paint.Style.FILL);
        this.p.setColor(color);
        this.p.setShader(null);
        this.c.drawCircle(cx, cy, r, this.p);
    }

    public static /* synthetic */ void ring$default(IconPainter iconPainter, float f, float f2, float f3, int i, float f4, int i2, Object obj) {
        if ((i2 & 16) != 0) {
            f4 = 1.5f;
        }
        iconPainter.ring(f, f2, f3, i, f4);
    }

    public final void ring(float cx, float cy, float r, int color, float w) {
        this.p.setStyle(Paint.Style.STROKE);
        this.p.setColor(color);
        this.p.setStrokeWidth(w);
        this.p.setShader(null);
        this.c.drawCircle(cx, cy, r, this.p);
    }

    public static /* synthetic */ void line$default(IconPainter iconPainter, float f, float f2, float f3, float f4, int i, float f5, int i2, Object obj) {
        if ((i2 & 32) != 0) {
            f5 = 1.6f;
        }
        iconPainter.line(f, f2, f3, f4, i, f5);
    }

    public final void line(float x1, float y1, float x2, float y2, int color, float w) {
        this.p.setStyle(Paint.Style.STROKE);
        this.p.setColor(color);
        this.p.setStrokeWidth(w);
        this.p.setShader(null);
        this.c.drawLine(x1, y1, x2, y2, this.p);
    }

    private final Path path(float[] xy, boolean close) {
        Path path = new Path();
        path.moveTo(xy[0], xy[1]);
        for (int i = 2; i + 1 < xy.length; i += 2) {
            path.lineTo(xy[i], xy[i + 1]);
        }
        if (close) {
            path.close();
        }
        return path;
    }

    public final void poly(int color, float... xy) {
        Intrinsics.checkNotNullParameter(xy, "xy");
        this.p.setStyle(Paint.Style.FILL);
        this.p.setColor(color);
        this.p.setShader(null);
        this.c.drawPath(path(xy, true), this.p);
    }

    public final void spoly(int color, float w, float... xy) {
        Intrinsics.checkNotNullParameter(xy, "xy");
        this.p.setStyle(Paint.Style.STROKE);
        this.p.setColor(color);
        this.p.setStrokeWidth(w);
        this.p.setShader(null);
        this.c.drawPath(path(xy, true), this.p);
    }

    public final void pline(int color, float w, float... xy) {
        Intrinsics.checkNotNullParameter(xy, "xy");
        this.p.setStyle(Paint.Style.STROKE);
        this.p.setColor(color);
        this.p.setStrokeWidth(w);
        this.p.setShader(null);
        this.c.drawPath(path(xy, false), this.p);
    }

    public final void arc(float l, float t, float r, float b, float start, float sweep, int color, float w) {
        this.p.setStyle(Paint.Style.STROKE);
        this.p.setColor(color);
        this.p.setStrokeWidth(w);
        this.p.setShader(null);
        this.c.drawArc(l, t, r, b, start, sweep, false, this.p);
    }

    public final void pie(float l, float t, float r, float b, float start, float sweep, int color) {
        this.p.setStyle(Paint.Style.FILL);
        this.p.setColor(color);
        this.p.setShader(null);
        this.c.drawArc(l, t, r, b, start, sweep, true, this.p);
    }

    public static /* synthetic */ void text$default(IconPainter iconPainter, String str, float f, float f2, float f3, int i, boolean z, int i2, Object obj) {
        if ((i2 & 32) != 0) {
            z = true;
        }
        iconPainter.text(str, f, f2, f3, i, z);
    }

    public final void text(String s, float x, float y, float size, int color, boolean bold) {
        Intrinsics.checkNotNullParameter(s, "s");
        this.p.setStyle(Paint.Style.FILL);
        this.p.setColor(color);
        this.p.setShader(null);
        this.p.setTextSize(size);
        this.p.setTextAlign(Paint.Align.CENTER);
        this.p.setTypeface(bold ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
        this.c.drawText(s, x, y - ((this.p.descent() + this.p.ascent()) / 2.0f), this.p);
    }

    public final void gradientRect(float l, float t, float r, float b, int c0, int c1, float rad) {
        this.p.setStyle(Paint.Style.FILL);
        this.p.setShader(new LinearGradient(l, t, r, t, c0, c1, Shader.TileMode.CLAMP));
        this.c.drawRoundRect(l, t, r, b, rad, rad, this.p);
        this.p.setShader(null);
    }

    public static /* synthetic */ void cube$default(IconPainter iconPainter, float f, float f2, float f3, int i, int i2, int i3, int i4, Object obj) {
        if ((i4 & 8) != 0) {
            i = GRAY_T;
        }
        int i5 = i;
        if ((i4 & 16) != 0) {
            i2 = GRAY_L;
        }
        int i6 = i2;
        if ((i4 & 32) != 0) {
            i3 = GRAY_R;
        }
        iconPainter.cube(f, f2, f3, i5, i6, i3);
    }

    public final void cube(float cx, float cy, float s, int top, int left, int right) {
        float k = 0.866f * s;
        poly(top, cx, cy - s, cx + k, cy - (s / 2.0f), cx, cy, cx - k, cy - (s / 2.0f));
        poly(left, cx - k, cy - (s / 2.0f), cx, cy, cx, cy + s, cx - k, cy + (s / 2.0f));
        poly(right, cx, cy, cx + k, cy - (s / 2.0f), cx + k, cy + (s / 2.0f), cx, cy + s);
    }

    public static /* synthetic */ void page$default(IconPainter iconPainter, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i2 = PAPER;
        }
        iconPainter.page(i, i2);
    }

    public final void page(int band, int paper) {
        poly(paper, 5.0f, 2.0f, 15.0f, 2.0f, 19.0f, 6.0f, 19.0f, 22.0f, 5.0f, 22.0f);
        poly(-4604218, 15.0f, 2.0f, 19.0f, 6.0f, 15.0f, 6.0f);
        spoly(-7498852, 1.0f, 5.0f, 2.0f, 15.0f, 2.0f, 19.0f, 6.0f, 19.0f, 22.0f, 5.0f, 22.0f);
        rect$default(this, 5.0f, 2.0f, 7.5f, 22.0f, band, 0.0f, 32, null);
    }

    public final void pageLines(int color) {
        line(9.5f, 9.0f, 16.5f, 9.0f, color, 1.4f);
        line(9.5f, 12.5f, 15.0f, 12.5f, color, 1.4f);
        line(9.5f, 16.0f, 16.5f, 16.0f, color, 1.4f);
        line(9.5f, 19.2f, 13.5f, 19.2f, color, 1.4f);
    }

    public final void person(float cx, float cy, float s, int color) {
        circle(cx, cy - (0.45f * s), 0.32f * s, color);
        Path path = new Path();
        path.moveTo(cx - (s * 0.62f), (s * 0.75f) + cy);
        path.cubicTo(cx - (s * 0.62f), (s * 0.05f) + cy, (s * 0.62f) + cx, (0.05f * s) + cy, (0.62f * s) + cx, (0.75f * s) + cy);
        path.close();
        this.p.setStyle(Paint.Style.FILL);
        this.p.setColor(color);
        this.p.setShader(null);
        this.c.drawPath(path, this.p);
    }

    public final void bolt(float cx, float cy, float s, int color) {
        poly(color, (s * 0.15f) + cx, cy - s, cx - (s * 0.55f), cy + (s * 0.12f), cx - (s * 0.02f), cy + (0.12f * s), cx - (0.2f * s), cy + s, (0.55f * s) + cx, cy - (s * 0.15f), (0.02f * s) + cx, cy - (0.15f * s));
    }

    public final void tag(int color, String letter) {
        Intrinsics.checkNotNullParameter(letter, "letter");
        poly(color, 3.0f, 7.0f, 15.0f, 7.0f, 21.0f, 12.0f, 15.0f, 17.0f, 3.0f, 17.0f);
        circle(16.5f, 12.0f, 1.2f, -1);
        text$default(this, letter, 8.8f, 12.0f, 7.0f, -1, false, 32, null);
    }

    public final void cloud(float cx, float cy, float s, int color) {
        circle(cx - (s * 0.45f), (s * 0.1f) + cy, 0.42f * s, color);
        circle((0.05f * s) + cx, cy - (0.2f * s), s * 0.55f, color);
        circle((s * 0.55f) + cx, (0.15f * s) + cy, 0.38f * s, color);
        rect$default(this, cx - (0.45f * s), cy + (0.1f * s), cx + (0.55f * s), (0.52f * s) + cy, color, 0.0f, 32, null);
    }

    public final void speaker(int color) {
        poly(color, 3.0f, 9.0f, 7.0f, 9.0f, 12.0f, 4.5f, 12.0f, 19.5f, 7.0f, 15.0f, 3.0f, 15.0f);
        arc(10.0f, 7.0f, 18.0f, 17.0f, -50.0f, 100.0f, color, 1.7f);
        arc(9.0f, 3.5f, 22.0f, 20.5f, -50.0f, 100.0f, color, 1.7f);
    }

    public final void gear(float cx, float cy, float r, int color, int hole) {
        int i = 0;
        while (i < 8) {
            this.c.save();
            this.c.rotate(i * 45.0f, cx, cy);
            int color2 = color;
            rect(cx - (r * 0.2f), cy - (1.15f * r), cx + (0.2f * r), cy - (0.6f * r), color2, r * 0.08f);
            this.c.restore();
            i++;
            color = color2;
        }
        circle(cx, cy, 0.82f * r, color);
        circle(cx, cy, 0.36f * r, hole);
    }

    public final void bulb(float cx, float cy, int color) {
        circle(cx, cy - 2.0f, 6.2f, color);
        rect(cx - 3.0f, cy + 2.5f, cx + 3.0f, cy + 7.0f, -6643544, 1.0f);
        line(cx - 2.6f, cy + 4.7f, cx + 2.6f, cy + 4.7f, -9538436, 0.8f);
    }

    public final void magnifier(int color) {
        ring(10.0f, 10.0f, 6.0f, color, 2.2f);
        line(14.5f, 14.5f, 20.5f, 20.5f, color, 3.0f);
    }

    public final void monitor(int screen) {
        rect(2.5f, 4.0f, 21.5f, 17.0f, -10853776, 1.5f);
        rect(4.0f, 5.5f, 20.0f, 15.5f, screen, 0.5f);
        rect$default(this, 10.0f, 17.0f, 14.0f, 20.0f, -10853776, 0.0f, 32, null);
        rect(7.0f, 19.5f, 17.0f, 21.0f, -10853776, 0.7f);
    }

    public final void frame(int fill, int stroke) {
        box(3.0f, 5.0f, 21.0f, 19.0f, fill, stroke, 1.4f, 1.5f);
    }

    public final void arrowHead(float x, float y, float angleDeg, float s, int color) {
        this.c.save();
        this.c.rotate(angleDeg, x, y);
        poly(color, x, y - s, (s * 0.9f) + x, (s * 0.6f) + y, x - (0.9f * s), (0.6f * s) + y);
        this.c.restore();
    }

    public final void chain(int color) {
        this.c.save();
        this.c.rotate(-40.0f, 12.0f, 12.0f);
        srect(3.5f, 9.0f, 13.5f, 15.0f, color, 2.0f, 3.0f);
        srect(10.5f, 9.0f, 20.5f, 15.0f, color, 2.0f, 3.0f);
        this.c.restore();
    }

    public final void picture(float l, float t, float r, float b) {
        rect(l, t, r, b, LBLUE, 1.0f);
        poly(-11753894, l, b, l + ((r - l) * 0.38f), t + ((b - t) * 0.45f), ((r - l) * 0.62f) + l, b);
        poly(-13661628, ((r - l) * 0.42f) + l, b, ((r - l) * 0.7f) + l, ((b - t) * 0.55f) + t, r, b);
        circle(l + ((r - l) * 0.75f), t + ((b - t) * 0.3f), (r - l) * 0.1f, -11443);
    }
}

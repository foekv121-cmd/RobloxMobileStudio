package com.goydevv.studrblx.ui;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.core.view.ViewCompat;
import kotlin.Deprecated;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: AudioBrowser.kt */
/* JADX INFO: loaded from: classes5.dex */
public final class PlayGlyph extends Drawable {
    private boolean loading;
    private final Paint p;
    private boolean playing;

    public PlayGlyph(boolean playing, boolean loading) {
        this.playing = playing;
        this.loading = loading;
        this.p = new Paint(1);
    }

    public /* synthetic */ PlayGlyph(boolean z, boolean z2, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(z, (i & 2) != 0 ? false : z2);
    }

    public final void set(boolean play, boolean load) {
        this.playing = play;
        this.loading = load;
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas c) {
        int accent;
        Intrinsics.checkNotNullParameter(c, "c");
        Rect b = getBounds();
        Intrinsics.checkNotNullExpressionValue(b, "getBounds(...)");
        float r = Math.min(b.width(), b.height()) / 2.0f;
        float cx = b.exactCenterX();
        float cy = b.exactCenterY();
        this.p.setStyle(Paint.Style.FILL);
        Paint paint = this.p;
        int i = -1;
        if (this.playing || this.loading) {
            accent = Ui.INSTANCE.getACCENT();
        } else {
            accent = Ui.INSTANCE.blend(Ui.INSTANCE.getPANEL(), Ui.INSTANCE.getLIGHT() ? ViewCompat.MEASURED_STATE_MASK : -1, 0.14f);
        }
        paint.setColor(accent);
        c.drawCircle(cx, cy, r, this.p);
        Paint paint2 = this.p;
        if (!this.playing && !this.loading && Ui.INSTANCE.getLIGHT()) {
            i = -14540254;
        }
        paint2.setColor(i);
        if (this.loading) {
            this.p.setStyle(Paint.Style.STROKE);
            this.p.setStrokeWidth(0.16f * r);
            c.drawArc(cx - (r * 0.45f), cy - (r * 0.45f), (r * 0.45f) + cx, (0.45f * r) + cy, 0.0f, 270.0f, false, this.p);
        } else {
            if (this.playing) {
                float s = r * 0.34f;
                float w = r * 0.22f;
                c.drawRect(cx - s, cy - s, (cx - s) + w, cy + s, this.p);
                c.drawRect((cx + s) - w, cy - s, cx + s, cy + s, this.p);
                return;
            }
            float s2 = 0.42f * r;
            Path path = new Path();
            path.moveTo(cx - (s2 * 0.6f), cy - s2);
            path.lineTo(cx + s2, cy);
            path.lineTo(cx - (0.6f * s2), cy + s2);
            path.close();
            c.drawPath(path, this.p);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int alpha) {
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter cf) {
    }

    @Override // android.graphics.drawable.Drawable
    @Deprecated(message = "Deprecated in Java")
    public int getOpacity() {
        return -3;
    }
}

package com.goydevv.studrblx.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.LruCache;
import kotlin.Deprecated;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: BuiltinIcons.kt */
/* JADX INFO: loaded from: classes5.dex */
public final class VectorIcon extends Drawable {
    private static final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(16 * 1024 * 1024) { // from class: com.goydevv.studrblx.ui.VectorIcon$Companion$cache$1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.util.LruCache
        public int sizeOf(String key, Bitmap value) {
            Intrinsics.checkNotNullParameter(key, "key");
            Intrinsics.checkNotNullParameter(value, "value");
            return value.getByteCount();
        }
    };
    private int alphaV;
    private final Paint bmpPaint;
    private final Function1<IconPainter, Unit> painter;

    /* JADX WARN: Multi-variable type inference failed */
    public VectorIcon(Function1<? super IconPainter, Unit> painter) {
        Intrinsics.checkNotNullParameter(painter, "painter");
        this.painter = (Function1<IconPainter, Unit>) painter;
        this.alphaV = 255;
        this.bmpPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG);
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas c) {
        Bitmap bmp;
        Intrinsics.checkNotNullParameter(c, "c");
        Rect b = getBounds();
        Intrinsics.checkNotNullExpressionValue(b, "getBounds(...)");
        int w = b.width();
        int h = b.height();
        if (w <= 0 || h <= 0) {
            return;
        }
        // 2x supersampling (capped at 256px) for razor-sharp high-DPI icons
        int sw = Math.min(w * 2, 256);
        int sh = Math.min(h * 2, 256);
        String key = System.identityHashCode(this.painter) + ":" + sw + ":" + sh + ":" + Ui.INSTANCE.getTEXT() + ":" + Ui.INSTANCE.getPANEL() + ":" + Ui.INSTANCE.getDIM();
        synchronized (cache) {
            bmp = cache.get(key);
        }
        if (bmp == null) {
            bmp = Bitmap.createBitmap(sw, sh, Bitmap.Config.ARGB_8888);
            Intrinsics.checkNotNullExpressionValue(bmp, "createBitmap(...)");
            Canvas canvas = new Canvas(bmp);
            float fMin = Math.min(sw, sh) / 24.0f;
            canvas.translate((sw - (fMin * 24.0f)) / 2.0f, (sh - (24.0f * fMin)) / 2.0f);
            canvas.scale(fMin, fMin);
            this.painter.invoke(new IconPainter(canvas));
            synchronized (cache) {
                cache.put(key, bmp);
            }
        }
        this.bmpPaint.setAlpha(this.alphaV);
        c.drawBitmap(bmp, (Rect) null, b, this.bmpPaint);
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int alpha) {
        this.alphaV = alpha;
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter cf) {
        this.bmpPaint.setColorFilter(cf);
    }

    @Override // android.graphics.drawable.Drawable
    @Deprecated(message = "Deprecated in Java")
    public int getOpacity() {
        return -3;
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return 64;
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return 64;
    }
}

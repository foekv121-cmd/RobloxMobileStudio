package com.goydevv.studrblx.engine;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

/* JADX INFO: compiled from: ClothingCompositor.kt */
/* JADX INFO: loaded from: classes7.dex */
public final class ClothingCompositor {
    public static final int LIMB_H = 284;
    public static final int LIMB_W = 264;
    private static final float PAD = 2.0f;
    public static final int TORSO_H = 272;
    public static final int TORSO_W = 388;
    public static final int TPL_H = 559;
    public static final int TPL_W = 585;
    public static final ClothingCompositor INSTANCE = new ClothingCompositor();
    private static final Limb LEFT = new Limb(308.0f, 374.0f, 440.0f, 506.0f, 308.0f, 308.0f);
    private static final Limb RIGHT = new Limb(217.0f, 19.0f, 85.0f, 151.0f, 217.0f, 217.0f);

    private ClothingCompositor() {
    }

    /* JADX INFO: compiled from: ClothingCompositor.kt */
    public static final class Limb {
        private final float back;
        private final float down;
        private final float front;
        private final float left;
        private final float right;
        private final float up;

        public Limb(float front, float left, float back, float right, float up, float down) {
            this.front = front;
            this.left = left;
            this.back = back;
            this.right = right;
            this.up = up;
            this.down = down;
        }

        public final float getBack() {
            return this.back;
        }

        public final float getDown() {
            return this.down;
        }

        public final float getFront() {
            return this.front;
        }

        public final float getLeft() {
            return this.left;
        }

        public final float getRight() {
            return this.right;
        }

        public final float getUp() {
            return this.up;
        }
    }

    public final Limb getLEFT() {
        return LEFT;
    }

    public final Limb getRIGHT() {
        return RIGHT;
    }

    public final void drawTorso(Canvas c, Bitmap tpl) {
        Intrinsics.checkNotNullParameter(c, "c");
        Intrinsics.checkNotNullParameter(tpl, "tpl");
        TplImage t = new TplImage(tpl);
        t.face(c, 231.0f, 74.0f, 128.0f, 128.0f, PAD, 74.0f);
        t.face(c, 361.0f, 74.0f, 64.0f, 128.0f, 130.0f, 74.0f);
        t.face(c, 427.0f, 74.0f, 128.0f, 128.0f, 194.0f, 74.0f);
        t.face(c, 165.0f, 74.0f, 64.0f, 128.0f, 322.0f, 74.0f);
        t.face(c, 231.0f, 8.0f, 128.0f, 64.0f, PAD, 10.0f);
        t.face(c, 231.0f, 204.0f, 128.0f, 64.0f, PAD, 202.0f);
        Float fValueOf = Float.valueOf(555.0f);
        Float fValueOf2 = Float.valueOf(427.0f);
        Pair<Float, Float> pair = TuplesKt.to(fValueOf, fValueOf2);
        Float fValueOf3 = Float.valueOf(361.0f);
        Float fValueOf4 = Float.valueOf(425.0f);
        Pair<Float, Float> pair2 = TuplesKt.to(fValueOf3, fValueOf4);
        Float fValueOf5 = Float.valueOf(231.0f);
        Float fValueOf6 = Float.valueOf(359.0f);
        Pair<Float, Float> pair3 = TuplesKt.to(fValueOf5, fValueOf6);
        Float fValueOf7 = Float.valueOf(229.0f);
        Float fValueOf8 = Float.valueOf(165.0f);
        t.cap(c, 134.0f, 206.0f, 198.0f, 238.0f, 170.0f, pair, pair2, pair3, TuplesKt.to(fValueOf7, fValueOf8));
        t.cap(c, 134.0f, 38.0f, 198.0f, 70.0f, 170.0f, TuplesKt.to(fValueOf5, fValueOf6), TuplesKt.to(fValueOf3, fValueOf4), TuplesKt.to(fValueOf, fValueOf2), TuplesKt.to(fValueOf7, fValueOf8));
    }

    public final void drawLimb(Canvas c, Bitmap tpl, Limb l) {
        Intrinsics.checkNotNullParameter(c, "c");
        Intrinsics.checkNotNullParameter(tpl, "tpl");
        Intrinsics.checkNotNullParameter(l, "l");
        TplImage t = new TplImage(tpl);
        t.face(c, l.getRight(), 355.0f, 64.0f, 112.0f, PAD, 66.0f);
        t.face(c, l.getFront(), 355.0f, 64.0f, 112.0f, 66.0f, 66.0f);
        t.face(c, l.getLeft(), 355.0f, 64.0f, 112.0f, 130.0f, 66.0f);
        t.face(c, l.getBack(), 355.0f, 64.0f, 112.0f, 194.0f, 66.0f);
        t.face(c, l.getRight(), 467.0f, 64.0f, 16.0f, PAD, 218.0f);
        t.face(c, l.getFront(), 467.0f, 64.0f, 16.0f, 66.0f, 218.0f);
        t.face(c, l.getLeft(), 467.0f, 64.0f, 16.0f, 130.0f, 218.0f);
        t.face(c, l.getBack(), 467.0f, 64.0f, 16.0f, PAD, 238.0f);
        t.face(c, l.getDown(), 485.0f, 64.0f, 64.0f, 198.0f, 218.0f);
        t.rotatedTop(c, l.getUp(), 289.0f, PAD, PAD);
        t.cap(c, PAD, 182.0f, 34.0f, 214.0f, 416.0f, TuplesKt.to(Float.valueOf(l.getBack() + 64.0f), Float.valueOf(l.getBack())), TuplesKt.to(Float.valueOf(l.getLeft() + 64.0f), Float.valueOf(l.getLeft())), TuplesKt.to(Float.valueOf(l.getFront()), Float.valueOf(l.getFront() + 64.0f)), TuplesKt.to(Float.valueOf(l.getRight()), Float.valueOf(l.getRight() + 64.0f)));
        t.cap(c, 38.0f, 182.0f, 70.0f, 214.0f, 467.0f, TuplesKt.to(Float.valueOf(l.getFront()), Float.valueOf(l.getFront() + 64.0f)), TuplesKt.to(Float.valueOf(l.getLeft()), Float.valueOf(l.getLeft() + 64.0f)), TuplesKt.to(Float.valueOf(l.getBack() + 64.0f), Float.valueOf(l.getBack())), TuplesKt.to(Float.valueOf(l.getRight() + 64.0f), Float.valueOf(l.getRight())));
    }

    /* JADX INFO: compiled from: ClothingCompositor.kt */
    private static final class TplImage {
        private final Bitmap bmp;
        private final Paint paint;
        private final float sx;
        private final float sy;

        public TplImage(Bitmap src) {
            Intrinsics.checkNotNullParameter(src, "src");
            this.sx = src.getWidth() / 585.0f;
            this.sy = src.getHeight() / 559.0f;
            this.bmp = src;
            this.paint = new Paint(3);
        }

        public final float getSx() {
            return this.sx;
        }

        public final float getSy() {
            return this.sy;
        }

        public final Bitmap getBmp() {
            return this.bmp;
        }

        public final Paint getPaint() {
            return this.paint;
        }

        public final void face(Canvas c, float x, float y, float w, float h, float dx, float dy) {
            Intrinsics.checkNotNullParameter(c, "c");
            int px = RangesKt.coerceIn((int) (this.sx * x), 0, this.bmp.getWidth() - 1);
            int py = RangesKt.coerceIn((int) (this.sy * y), 0, this.bmp.getHeight() - 1);
            int pw = RangesKt.coerceIn((int) (this.sx * w), 1, this.bmp.getWidth() - px);
            int ph = RangesKt.coerceIn((int) (this.sy * h), 1, this.bmp.getHeight() - py);
            Bitmap cut = Bitmap.createBitmap(this.bmp, px, py, pw, ph);
            Intrinsics.checkNotNullExpressionValue(cut, "createBitmap(...)");
            BitmapShader shader = new BitmapShader(cut, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            Matrix m = new Matrix();
            m.setScale(w / pw, h / ph);
            m.postTranslate(dx, dy);
            shader.setLocalMatrix(m);
            this.paint.setShader(shader);
            c.drawRect(new RectF(dx - ClothingCompositor.PAD, dy - ClothingCompositor.PAD, dx + w + ClothingCompositor.PAD, dy + h + ClothingCompositor.PAD), this.paint);
            this.paint.setShader(null);
        }

        public final void rotatedTop(Canvas c, float x, float y, float dx, float dy) {
            Intrinsics.checkNotNullParameter(c, "c");
            int px = RangesKt.coerceIn((int) (this.sx * x), 0, this.bmp.getWidth() - 1);
            int py = RangesKt.coerceIn((int) (this.sy * y), 0, this.bmp.getHeight() - 1);
            int pw = RangesKt.coerceIn((int) (this.sx * 64.0f), 1, this.bmp.getWidth() - px);
            int ph = RangesKt.coerceIn((int) (this.sy * 64.0f), 1, this.bmp.getHeight() - py);
            Bitmap cut = Bitmap.createBitmap(this.bmp, px, py, pw, ph);
            Intrinsics.checkNotNullExpressionValue(cut, "createBitmap(...)");
            BitmapShader shader = new BitmapShader(cut, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            float[] src = {0.0f, 0.0f, pw, 0.0f, 0.0f, ph};
            float[] dst = {dx, dy + 64.0f, dx, dy, dx + 64.0f, dy + 64.0f};
            Matrix m = new Matrix();
            m.setPolyToPoly(src, 0, dst, 0, 3);
            shader.setLocalMatrix(m);
            this.paint.setShader(shader);
            c.drawRect(new RectF(dx - ClothingCompositor.PAD, dy - ClothingCompositor.PAD, dx + 64.0f + ClothingCompositor.PAD, dy + 64.0f + ClothingCompositor.PAD), this.paint);
            this.paint.setShader(null);
        }

        public final void cap(Canvas c, float l, float t, float r, float b, float row, Pair<Float, Float> top, Pair<Float, Float> right, Pair<Float, Float> bottom, Pair<Float, Float> left) {
            Intrinsics.checkNotNullParameter(c, "c");
            Intrinsics.checkNotNullParameter(top, "top");
            Intrinsics.checkNotNullParameter(right, "right");
            Intrinsics.checkNotNullParameter(bottom, "bottom");
            Intrinsics.checkNotNullParameter(left, "left");
            BitmapShader shader = new BitmapShader(this.bmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            this.paint.setShader(shader);
            float cx = (l + r) / ClothingCompositor.PAD;
            float cy = (t + b) / ClothingCompositor.PAD;
            float v = this.sy * row;
            cap$tri(cx, cy, this, v, c, l, t, r, t, top.getFirst().floatValue(), top.getSecond().floatValue());
            cap$tri(cx, cy, this, v, c, r, t, r, b, right.getFirst().floatValue(), right.getSecond().floatValue());
            cap$tri(cx, cy, this, v, c, r, b, l, b, bottom.getSecond().floatValue(), bottom.getFirst().floatValue());
            cap$tri(cx, cy, this, v, c, l, b, l, t, left.getSecond().floatValue(), left.getFirst().floatValue());
            this.paint.setShader(null);
        }

        private static final void cap$tri(float cx, float cy, TplImage this$0, float v, Canvas $c, float ax, float ay, float bx, float by, float u0, float u1) {
            float mid = (u0 + u1) / ClothingCompositor.PAD;
            float[] verts = {ax, ay, bx, by, cx, cy};
            float[] texs = {this$0.sx * u0, v, this$0.sx * u1, v, this$0.sx * mid, v};
            $c.drawVertices(Canvas.VertexMode.TRIANGLES, 6, verts, 0, texs, 0, null, 0, null, 0, 0, this$0.paint);
        }
    }
}

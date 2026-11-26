package com.goydevv.studrblx.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import com.goydevv.studrblx.engine.SunAnim;
import com.goydevv.studrblx.engine.SunEase;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * The Animator timeline. Track names on the left, a frame ruler on top and one row of keyframe diamonds
 * per track. Touch: tap or drag the ruler to scrub; tap a diamond to select it (Multi mode adds to the
 * selection); drag selected diamonds to move them; long press an empty cell to add a key there; drag an
 * empty area to pan; pinch to zoom.
 */
public final class TimelineView extends View {

    public interface Host {
        /** The user moved the playhead. */
        void onScrub(int frame);

        void onSelectTrack(int index);

        void onSelectionChanged();

        /** Called once before the first change of a drag, so the host can save an undo step. */
        void onBeginEdit();

        /** A drag of keys finished. The keys already carry their new frames. */
        void onKeysMoved();

        void onAddKeyAt(int trackIndex, int frame);
    }

    public SunAnim anim;
    public Host host;
    public final Set<SunAnim.Key> selected = new HashSet<>();
    public int playFrame;
    public int selTrack = -1;
    public boolean multi;
    public boolean easeColors = true;
    public boolean showSeconds;

    private final float labelW;
    private final float rulerH;
    private final float rowH;
    private final float keyR;
    private final float hitR;
    private float ppf;
    private float scrollX;
    private float scrollY;

    private final Paint fill = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint stroke = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint text = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Path diamond = new Path();
    private final ScaleGestureDetector scale;
    private final Handler handler = new Handler(Looper.getMainLooper());

    private final int cBg;
    private final int cRow;
    private final int cRowAlt;
    private final int cText;
    private final int cDim;
    private final int cAccent;
    private final int cPanel;
    private final int cBorder;

    private static final int MODE_NONE = 0;
    private static final int MODE_SCRUB = 1;
    private static final int MODE_PAN = 2;
    private static final int MODE_KEYS = 3;
    private static final int MODE_LABEL = 4;
    private int mode = MODE_NONE;
    private float downX;
    private float downY;
    private float lastX;
    private float lastY;
    private boolean moved;
    private boolean longFired;
    private int dragDelta;
    private boolean dragStarted;
    private final Map<SunAnim.Key, Integer> origFrames = new HashMap<>();

    private final Runnable longPress = new Runnable() {
        @Override
        public void run() {
            if (mode != MODE_PAN || moved) {
                return;
            }
            int row = rowAt(downY);
            if (row >= 0 && downX >= labelW && host != null) {
                longFired = true;
                host.onAddKeyAt(row, Math.max(0, frameAt(downX)));
            }
        }
    };

    public TimelineView(Context ctx) {
        super(ctx);
        Ui ui = Ui.INSTANCE;
        labelW = ui.dp(ctx, 96.0f);
        rulerH = ui.dp(ctx, 26.0f);
        rowH = ui.dp(ctx, 34.0f);
        keyR = ui.dp(ctx, 7.0f);
        hitR = ui.dp(ctx, 16.0f);
        ppf = ui.dp(ctx, 10.0f);
        cBg = ui.getBG();
        cRow = ui.getPANEL();
        cRowAlt = ui.getROW_ALT();
        cText = ui.getTEXT();
        cDim = ui.getDIM();
        cAccent = ui.getACCENT();
        cPanel = ui.getHEADER();
        cBorder = ui.getBORDER();
        text.setTextSize(ui.dp(ctx, 11.0f));
        stroke.setStyle(Paint.Style.STROKE);
        stroke.setStrokeWidth(ui.dp(ctx, 1.5f));
        scale = new ScaleGestureDetector(ctx, new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector d) {
                float focus = d.getFocusX();
                float frameUnder = (focus - labelW + scrollX) / ppf;
                float min = Ui.INSTANCE.dp(getContext(), 2.0f);
                float max = Ui.INSTANCE.dp(getContext(), 48.0f);
                ppf = Math.max(min, Math.min(max, ppf * d.getScaleFactor()));
                scrollX = frameUnder * ppf - (focus - labelW);
                clampScroll();
                invalidate();
                return true;
            }
        });
    }

    // ------------------------------------------------------------------ public helpers

    public void setPlayFrame(int f) {
        playFrame = f;
        invalidate();
    }

    /** Scrolls sideways just enough to keep the playhead on screen (used while playing). */
    public void keepPlayheadVisible() {
        float x = playFrame * ppf - scrollX;
        float visible = getWidth() - labelW;
        if (x < 0 || x > visible) {
            scrollX = Math.max(0, playFrame * ppf - visible * 0.2f);
            clampScroll();
        }
        invalidate();
    }

    public void fit() {
        if (anim == null) {
            return;
        }
        float visible = Math.max(1.0f, getWidth() - labelW - Ui.INSTANCE.dp(getContext(), 16.0f));
        ppf = Math.max(Ui.INSTANCE.dp(getContext(), 2.0f), Math.min(Ui.INSTANCE.dp(getContext(), 48.0f), visible / Math.max(1, anim.length)));
        scrollX = 0;
        invalidate();
    }

    private void clampScroll() {
        float contentW = anim == null ? 0 : (anim.length + 60) * ppf;
        float maxX = Math.max(0.0f, contentW - (getWidth() - labelW));
        scrollX = Math.max(0.0f, Math.min(maxX, scrollX));
        float contentH = anim == null ? 0 : anim.tracks.size() * rowH;
        float maxY = Math.max(0.0f, contentH - (getHeight() - rulerH));
        scrollY = Math.max(0.0f, Math.min(maxY, scrollY));
    }

    private int frameAt(float x) {
        return Math.round((x - labelW + scrollX) / ppf);
    }

    private int rowAt(float y) {
        if (anim == null || y < rulerH) {
            return -1;
        }
        int r = (int) Math.floor((y - rulerH + scrollY) / rowH);
        return r >= 0 && r < anim.tracks.size() ? r : -1;
    }

    private SunAnim.Key keyAt(float x, float y, int[] rowOut) {
        int row = rowAt(y);
        if (row < 0) {
            return null;
        }
        rowOut[0] = row;
        SunAnim.Key best = null;
        float bestD = hitR;
        for (SunAnim.Key k : anim.tracks.get(row).keys) {
            float kx = labelW + k.frame * ppf - scrollX;
            float d = Math.abs(kx - x);
            if (d <= bestD) {
                bestD = d;
                best = k;
            }
        }
        return best;
    }

    // ------------------------------------------------------------------ drawing

    @Override
    protected void onSizeChanged(int w, int h, int ow, int oh) {
        super.onSizeChanged(w, h, ow, oh);
        clampScroll();
    }

    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);
        int w = getWidth();
        int h = getHeight();
        fill.setStyle(Paint.Style.FILL);
        fill.setColor(cBg);
        c.drawRect(0, 0, w, h, fill);
        if (anim == null) {
            return;
        }
        int fps = Math.max(1, anim.fps);

        // rows
        c.save();
        c.clipRect(labelW, rulerH, w, h);
        for (int i = 0; i < anim.tracks.size(); i++) {
            float top = rulerH + i * rowH - scrollY;
            if (top + rowH < rulerH || top > h) {
                continue;
            }
            fill.setColor(i == selTrack ? (cAccent & 0x00FFFFFF) | 0x40000000 : (i % 2 == 0 ? cRow : cRowAlt));
            c.drawRect(labelW, top, w, top + rowH, fill);
        }
        // time past the end of the animation
        float endX = labelW + anim.length * ppf - scrollX;
        if (endX < w) {
            fill.setColor(0x55000000);
            c.drawRect(Math.max(labelW, endX), rulerH, w, h, fill);
        }
        // segments and keys
        for (int i = 0; i < anim.tracks.size(); i++) {
            float top = rulerH + i * rowH - scrollY;
            if (top + rowH < rulerH || top > h) {
                continue;
            }
            float cy = top + rowH / 2.0f;
            SunAnim.Track t = anim.tracks.get(i);
            for (int j = 0; j < t.keys.size(); j++) {
                SunAnim.Key k = t.keys.get(j);
                float kx = labelW + k.frame * ppf - scrollX;
                int col = easeColors ? SunEase.color(k.ease) : cAccent;
                if (j + 1 < t.keys.size()) {
                    float nx = labelW + t.keys.get(j + 1).frame * ppf - scrollX;
                    fill.setColor((col & 0x00FFFFFF) | 0x66000000);
                    c.drawRect(kx, cy - keyR / 3.0f, nx, cy + keyR / 3.0f, fill);
                }
            }
            for (SunAnim.Key k : t.keys) {
                float kx = labelW + k.frame * ppf - scrollX;
                if (kx < labelW - keyR || kx > w + keyR) {
                    continue;
                }
                int col = easeColors ? SunEase.color(k.ease) : cAccent;
                boolean sel = selected.contains(k);
                diamond.reset();
                float r = sel ? keyR * 1.25f : keyR;
                diamond.moveTo(kx, cy - r);
                diamond.lineTo(kx + r, cy);
                diamond.lineTo(kx, cy + r);
                diamond.lineTo(kx - r, cy);
                diamond.close();
                fill.setColor(col);
                c.drawPath(diamond, fill);
                stroke.setColor(sel ? 0xFFFFFFFF : 0xAA000000);
                c.drawPath(diamond, stroke);
            }
        }
        // playhead line
        float px = labelW + playFrame * ppf - scrollX;
        if (px >= labelW && px <= w) {
            stroke.setColor(0xFFFF4040);
            c.drawLine(px, rulerH, px, h, stroke);
        }
        c.restore();

        // ruler
        fill.setColor(cPanel);
        c.drawRect(labelW, 0, w, rulerH, fill);
        c.save();
        c.clipRect(labelW, 0, w, rulerH);
        int[] steps = {1, 2, 5, 10, 15, 30, 60, 120, 300, 600};
        float minGap = Ui.INSTANCE.dp(getContext(), 44.0f);
        int step = steps[steps.length - 1];
        for (int s : steps) {
            if (s * ppf >= minGap) {
                step = s;
                break;
            }
        }
        int first = Math.max(0, (int) Math.floor(scrollX / ppf / step) * step);
        int lastF = (int) Math.ceil((scrollX + w - labelW) / ppf);
        text.setColor(cDim);
        stroke.setColor(cBorder);
        for (int f = first; f <= lastF; f += step) {
            float x = labelW + f * ppf - scrollX;
            c.drawLine(x, rulerH * 0.55f, x, rulerH, stroke);
            String lab = showSeconds ? String.format(Locale.US, "%.1fs", f / (float) fps) : String.valueOf(f);
            c.drawText(lab, x + 3, rulerH * 0.5f, text);
        }
        // markers
        fill.setColor(0xFFFFA726);
        for (SunAnim.Marker m : anim.markers) {
            float x = labelW + m.frame * ppf - scrollX;
            diamond.reset();
            diamond.moveTo(x - keyR / 1.5f, 0);
            diamond.lineTo(x + keyR / 1.5f, 0);
            diamond.lineTo(x, keyR * 1.2f);
            diamond.close();
            c.drawPath(diamond, fill);
        }
        // playhead head
        float hx = labelW + playFrame * ppf - scrollX;
        fill.setColor(0xFFFF4040);
        diamond.reset();
        diamond.moveTo(hx - keyR, rulerH * 0.55f);
        diamond.lineTo(hx + keyR, rulerH * 0.55f);
        diamond.lineTo(hx, rulerH);
        diamond.close();
        c.drawPath(diamond, fill);
        c.restore();

        // labels
        c.save();
        c.clipRect(0, 0, labelW, h);
        fill.setColor(cPanel);
        c.drawRect(0, 0, labelW, h, fill);
        for (int i = 0; i < anim.tracks.size(); i++) {
            float top = rulerH + i * rowH - scrollY;
            if (top + rowH < rulerH || top > h) {
                continue;
            }
            SunAnim.Track t = anim.tracks.get(i);
            fill.setColor(i == selTrack ? (cAccent & 0x00FFFFFF) | 0x55000000 : (i % 2 == 0 ? cRow : cRowAlt));
            c.drawRect(0, top, labelW, top + rowH, fill);
            text.setColor(i == selTrack ? cText : cDim);
            String tag = t.kind == SunAnim.JOINT ? "\u21BB " : "\u25A0 ";
            String lab = tag + t.label;
            float room = labelW - 8;
            while (lab.length() > 3 && text.measureText(lab) > room) {
                lab = lab.substring(0, lab.length() - 2);
            }
            c.drawText(lab, 4, top + rowH / 2.0f + text.getTextSize() / 3.0f, text);
        }
        c.restore();
        fill.setColor(cPanel);
        c.drawRect(0, 0, labelW, rulerH, fill);
        text.setColor(cDim);
        c.drawText(anim.tracks.size() + " tracks", 4, rulerH * 0.65f, text);
        stroke.setColor(cBorder);
        c.drawLine(labelW, 0, labelW, h, stroke);
    }

    // ------------------------------------------------------------------ touch

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (anim == null) {
            return false;
        }
        scale.onTouchEvent(e);
        if (scale.isInProgress() || e.getPointerCount() > 1) {
            cancelLong();
            mode = MODE_NONE;
            return true;
        }
        float x = e.getX();
        float y = e.getY();
        switch (e.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                downX = x;
                downY = y;
                lastX = x;
                lastY = y;
                moved = false;
                longFired = false;
                dragStarted = false;
                dragDelta = 0;
                if (y < rulerH && x >= labelW) {
                    mode = MODE_SCRUB;
                    scrub(x);
                } else if (x < labelW) {
                    mode = MODE_LABEL;
                } else {
                    int[] row = new int[1];
                    SunAnim.Key k = keyAt(x, y, row);
                    if (k != null) {
                        if (multi) {
                            if (!selected.remove(k)) {
                                selected.add(k);
                            }
                        } else if (!selected.contains(k)) {
                            selected.clear();
                            selected.add(k);
                        }
                        selTrack = row[0];
                        if (host != null) {
                            host.onSelectTrack(row[0]);
                            host.onSelectionChanged();
                        }
                        origFrames.clear();
                        for (SunAnim.Key s : selected) {
                            origFrames.put(s, s.frame);
                        }
                        mode = selected.contains(k) ? MODE_KEYS : MODE_NONE;
                        invalidate();
                    } else {
                        mode = MODE_PAN;
                        handler.postDelayed(longPress, 500L);
                    }
                }
                return true;
            case MotionEvent.ACTION_MOVE: {
                float slop = Ui.INSTANCE.dp(getContext(), 6.0f);
                if (!moved && (Math.abs(x - downX) > slop || Math.abs(y - downY) > slop)) {
                    moved = true;
                    cancelLong();
                }
                if (mode == MODE_SCRUB) {
                    scrub(x);
                } else if (mode == MODE_PAN || mode == MODE_LABEL) {
                    if (moved) {
                        scrollX -= mode == MODE_PAN ? (x - lastX) : 0.0f;
                        scrollY -= y - lastY;
                        clampScroll();
                        invalidate();
                    }
                } else if (mode == MODE_KEYS && moved) {
                    int delta = Math.round((x - downX) / ppf);
                    if (delta != dragDelta) {
                        if (!dragStarted) {
                            dragStarted = true;
                            if (host != null) {
                                host.onBeginEdit();
                            }
                        }
                        dragDelta = delta;
                        for (Map.Entry<SunAnim.Key, Integer> en : origFrames.entrySet()) {
                            en.getKey().frame = Math.max(0, en.getValue() + delta);
                        }
                        invalidate();
                    }
                }
                lastX = x;
                lastY = y;
                return true;
            }
            case MotionEvent.ACTION_UP:
                cancelLong();
                if (mode == MODE_KEYS && dragStarted) {
                    if (host != null) {
                        host.onKeysMoved();
                    }
                } else if (mode == MODE_PAN && !moved && !longFired) {
                    int row = rowAt(y);
                    if (!multi) {
                        selected.clear();
                    }
                    if (row >= 0) {
                        selTrack = row;
                        if (host != null) {
                            host.onSelectTrack(row);
                        }
                    }
                    if (host != null) {
                        host.onScrub(Math.max(0, frameAt(x)));
                        host.onSelectionChanged();
                    }
                    invalidate();
                } else if (mode == MODE_LABEL && !moved) {
                    int row = rowAt(y);
                    selTrack = row == selTrack ? -1 : row;
                    if (host != null) {
                        host.onSelectTrack(selTrack);
                    }
                    invalidate();
                }
                mode = MODE_NONE;
                return true;
            case MotionEvent.ACTION_CANCEL:
                cancelLong();
                if (mode == MODE_KEYS && dragStarted) {
                    for (Map.Entry<SunAnim.Key, Integer> en : origFrames.entrySet()) {
                        en.getKey().frame = en.getValue();
                    }
                    invalidate();
                }
                mode = MODE_NONE;
                return true;
            default:
                return true;
        }
    }

    private void scrub(float x) {
        int f = Math.max(0, frameAt(x));
        if (f != playFrame || mode == MODE_SCRUB) {
            playFrame = f;
            if (host != null) {
                host.onScrub(f);
            }
            invalidate();
        }
    }

    private void cancelLong() {
        handler.removeCallbacks(longPress);
    }

    // ------------------------------------------------------------------ ease preview

    /** A small graph of one easing curve, used by the Ease dialog. */
    public static final class CurveView extends View {
        private String family = "Linear";
        private int dir;
        private float p1 = 1.0f;
        private float p2 = 0.3f;
        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final int cBg;
        private final int cGrid;

        public CurveView(Context ctx) {
            super(ctx);
            cBg = Ui.INSTANCE.getINPUT();
            cGrid = Ui.INSTANCE.getBORDER();
            paint.setStrokeWidth(Ui.INSTANCE.dp(ctx, 2.0f));
        }

        public void set(String family, int dir, float p1, float p2) {
            this.family = family;
            this.dir = dir;
            this.p1 = p1;
            this.p2 = p2;
            invalidate();
        }

        @Override
        protected void onDraw(Canvas c) {
            super.onDraw(c);
            float w = getWidth();
            float h = getHeight();
            float pad = Ui.INSTANCE.dp(getContext(), 10.0f);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(cBg);
            c.drawRect(0, 0, w, h, paint);
            float x0 = pad;
            float x1 = w - pad;
            float yBottom = h - pad * 1.6f;
            float yTop = pad * 1.6f;
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(cGrid);
            c.drawLine(x0, yBottom, x1, yBottom, paint);
            c.drawLine(x0, yTop, x1, yTop, paint);
            paint.setColor(SunEase.color(family));
            float px = x0;
            float py = yBottom;
            int n = 60;
            for (int i = 1; i <= n; i++) {
                float x = i / (float) n;
                float v = SunEase.apply(family, dir, x, p1, p2);
                float sx = x0 + (x1 - x0) * x;
                float sy = yBottom - (yBottom - yTop) * v;
                c.drawLine(px, py, sx, sy, paint);
                px = sx;
                py = sy;
            }
        }
    }
}

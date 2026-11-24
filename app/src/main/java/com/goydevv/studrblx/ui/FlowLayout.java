package com.goydevv.studrblx.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: AudioBrowser.kt */
/* JADX INFO: loaded from: classes5.dex */
public final class FlowLayout extends ViewGroup {
    private int gap;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public FlowLayout(Context ctx) {
        super(ctx);
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        this.gap = Ui.INSTANCE.dp(ctx, 6.0f);
    }

    public final int getGap() {
        return this.gap;
    }

    public final void setGap(int i) {
        this.gap = i;
    }

    @Override // android.view.View
    protected void onMeasure(int wSpec, int hSpec) {
        int size = View.MeasureSpec.getSize(wSpec);
        if (size == 0) {
            size = Integer.MAX_VALUE;
        }
        int maxW = (size - getPaddingLeft()) - getPaddingRight();
        int x = 0;
        int y = 0;
        int lineH = 0;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View c = getChildAt(i);
            if (c.getVisibility() != 8) {
                measureChild(c, View.MeasureSpec.makeMeasureSpec(maxW, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(0, 0));
                if (x > 0 && c.getMeasuredWidth() + x > maxW) {
                    x = 0;
                    y += this.gap + lineH;
                    lineH = 0;
                }
                x += c.getMeasuredWidth() + this.gap;
                lineH = Math.max(lineH, c.getMeasuredHeight());
            }
        }
        int i2 = View.MeasureSpec.getSize(wSpec);
        setMeasuredDimension(i2, y + lineH + getPaddingTop() + getPaddingBottom());
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int maxW = ((r - l) - getPaddingLeft()) - getPaddingRight();
        int x = 0;
        int y = 0;
        int lineH = 0;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View c = getChildAt(i);
            if (c.getVisibility() != 8) {
                if (x > 0 && c.getMeasuredWidth() + x > maxW) {
                    x = 0;
                    y += this.gap + lineH;
                    lineH = 0;
                }
                c.layout(getPaddingLeft() + x, getPaddingTop() + y, getPaddingLeft() + x + c.getMeasuredWidth(), getPaddingTop() + y + c.getMeasuredHeight());
                x += c.getMeasuredWidth() + this.gap;
                lineH = Math.max(lineH, c.getMeasuredHeight());
            }
        }
    }
}

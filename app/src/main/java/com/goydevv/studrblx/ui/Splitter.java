package com.goydevv.studrblx.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Splitter.kt */
/* JADX INFO: loaded from: classes5.dex */
public final class Splitter extends View {
    private final float d;
    private boolean dragging;
    private float last;
    private final Function1<Float, Unit> onDrag;
    private final Function0<Unit> onEnd;
    private final Paint paint;
    private final boolean vertical;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    public Splitter(Context ctx, boolean vertical, Function1<? super Float, Unit> onDrag, Function0<Unit> onEnd) {
        super(ctx);
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(onDrag, "onDrag");
        Intrinsics.checkNotNullParameter(onEnd, "onEnd");
        this.vertical = vertical;
        this.onDrag = (Function1<Float, Unit>) onDrag;
        this.onEnd = onEnd;
        this.paint = new Paint();
        this.d = getResources().getDisplayMetrics().density;
        setBackgroundColor(Ui.INSTANCE.getBORDER());
    }

    public /* synthetic */ Splitter(Context context, boolean z, Function1 function1, Function0 function0, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, z, function1, (i & 8) != 0 ? new Function0() { // from class: com.goydevv.studrblx.ui.Splitter$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return Unit.INSTANCE;
            }
        } : function0);
    }

    public final boolean getVertical() {
        return this.vertical;
    }

    public final int getThickness() {
        return (int) (this.d * 12.0f);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        this.paint.setColor(this.dragging ? Ui.INSTANCE.getACCENT() : -10855846);
        if (this.dragging) {
            canvas.drawRect(0.0f, 0.0f, getWidth(), getHeight(), this.paint);
            return;
        }
        float cx = getWidth() / 2.0f;
        float cy = getHeight() / 2.0f;
        for (int i = -2; i < 3; i++) {
            if (this.vertical) {
                canvas.drawCircle(cx, (i * 5.0f * this.d) + cy, this.d * 1.3f, this.paint);
            } else {
                canvas.drawCircle((i * 5.0f * this.d) + cx, cy, this.d * 1.3f, this.paint);
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent e) {
        Intrinsics.checkNotNullParameter(e, "e");
        float pos = this.vertical ? e.getRawX() : e.getRawY();
        switch (e.getActionMasked()) {
            case 0:
                this.dragging = true;
                this.last = pos;
                ViewParent parent = getParent();
                if (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(true);
                }
                invalidate();
                return true;
            case 1:
            case 3:
                this.dragging = false;
                invalidate();
                this.onEnd.invoke();
                return true;
            case 2:
                if (this.dragging) {
                    float dlt = pos - this.last;
                    this.last = pos;
                    if (!(dlt == 0.0f)) {
                        this.onDrag.invoke(Float.valueOf(dlt));
                    }
                }
                return true;
            default:
                return true;
        }
    }
}

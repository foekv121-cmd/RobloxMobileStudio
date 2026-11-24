package com.goydevv.studrblx.ui;

import android.R;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.view.ViewCompat;
import com.goydevv.studrblx.core.Theme;
import com.goydevv.studrblx.model.Instance;
import kotlin.Deprecated;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Ui.kt */
/* JADX INFO: loaded from: classes5.dex */
public final class Ui {
    public static final int ERROR = -43691;
    public static final int INFO = -9652481;
    private static boolean LIGHT = false;
    public static final int MATCH = -1;
    public static final int PLAY = -13726889;
    public static final int STOP = -4179669;
    public static final int WARN = -23296;
    public static final int WRAP = -2;
    public static final Ui INSTANCE = new Ui();
    private static int BG = 0xFF1E2025;
    private static int PANEL = 0xFF21242A;
    private static int HEADER = 0xFF282C34;
    private static int TOPBAR = 0xFF17191E;
    private static int RIBBON = 0xFF23262D;
    private static int BORDER = 0xFF333844;
    private static int TEXT = 0xFFECEFF4;
    private static int DIM = 0xFF9AA3B2;
    private static int ACCENT = 0xFF00A2FF;
    private static int SELECT = 0xFF0E5DA8;
    private static int HOVER = -12434878;
    private static int INPUT = -14342875;
    private static int ROW_ALT = -13421773;
    private static int EDITOR_BG = -14803426;
    private static int EDITOR_GUTTER = -14342874;
    private static int EDITOR_TEXT = -2039584;
    private static int CARD = -13092808;
    private static Theme theme = Theme.INSTANCE.getDARK();

    private Ui() {
    }

    public final int getBG() {
        return BG;
    }

    public final int getPANEL() {
        return PANEL;
    }

    public final int getHEADER() {
        return HEADER;
    }

    public final int getTOPBAR() {
        return TOPBAR;
    }

    public final int getRIBBON() {
        return RIBBON;
    }

    public final int getBORDER() {
        return BORDER;
    }

    public final int getTEXT() {
        return TEXT;
    }

    public final int getDIM() {
        return DIM;
    }

    public final int getACCENT() {
        return ACCENT;
    }

    public final int getSELECT() {
        return SELECT;
    }

    public final int getHOVER() {
        return HOVER;
    }

    public final int getINPUT() {
        return INPUT;
    }

    public final int getROW_ALT() {
        return ROW_ALT;
    }

    public final int getEDITOR_BG() {
        return EDITOR_BG;
    }

    public final int getEDITOR_GUTTER() {
        return EDITOR_GUTTER;
    }

    public final int getEDITOR_TEXT() {
        return EDITOR_TEXT;
    }

    public final int getCARD() {
        return CARD;
    }

    public final boolean getLIGHT() {
        return LIGHT;
    }

    public final Theme getTheme() {
        return theme;
    }

    public final void applyTheme(Theme t) {
        Intrinsics.checkNotNullParameter(t, "t");
        theme = t;
        LIGHT = t.getLight();
        BG = t.getBg();
        PANEL = t.getPanel();
        HEADER = t.getHeader();
        TOPBAR = t.getTopbar();
        RIBBON = t.getRibbon();
        BORDER = t.getBorder();
        TEXT = t.getText();
        DIM = t.getDim();
        ACCENT = t.getAccent();
        SELECT = t.getSelect();
        HOVER = t.getHover();
        INPUT = t.getInput();
        ROW_ALT = t.getRowAlt();
        EDITOR_BG = t.getEditorBg();
        EDITOR_GUTTER = t.getEditorGutter();
        EDITOR_TEXT = t.getEditorText();
        CARD = blend(t.getPanel(), t.getLight() ? ViewCompat.MEASURED_STATE_MASK : -1, 0.06f);
    }

    public final int getSELECT_TEXT() {
        return luminance(SELECT) > 0.55f ? -15658735 : -1;
    }

    public final float luminance(int c) {
        return (((((c >> 16) & 255) * 0.299f) + (((c >> 8) & 255) * 0.587f)) + ((c & 255) * 0.114f)) / 255.0f;
    }

    private static final int blend$ch(int $a, float $t, int $b, int s) {
        return (((int) (((($a >> s) & 255) * (1.0f - $t)) + ((($b >> s) & 255) * $t))) & 255) << s;
    }

    public final int blend(int a, int b, float t) {
        return blend$ch(a, t, b, 16) | ViewCompat.MEASURED_STATE_MASK | blend$ch(a, t, b, 8) | blend$ch(a, t, b, 0);
    }

    public final int getDialogTheme() {
        return LIGHT ? android.R.style.Theme_Material_Light_Dialog_Alert : android.R.style.Theme_Material_Dialog_Alert;
    }

    public final Context popupContext(Context ctx) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        return new ContextThemeWrapper(ctx, LIGHT ? android.R.style.Theme_Material_Light : android.R.style.Theme_Material);
    }

    public final int dp(Context ctx, float v) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        return (int) TypedValue.applyDimension(1, v, ctx.getResources().getDisplayMetrics());
    }

    public final int dp(View v, float d) {
        Intrinsics.checkNotNullParameter(v, "v");
        Context context = v.getContext();
        Intrinsics.checkNotNullExpressionValue(context, "getContext(...)");
        return dp(context, d);
    }

    public static /* synthetic */ TextView text$default(Ui ui, Context context, String str, float f, int i, boolean z, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            str = "";
        }
        String str2 = str;
        if ((i2 & 4) != 0) {
            f = 13.0f;
        }
        float f2 = f;
        if ((i2 & 8) != 0) {
            i = TEXT;
        }
        int i3 = i;
        if ((i2 & 16) != 0) {
            z = false;
        }
        return ui.text(context, str2, f2, i3, z);
    }

    public final TextView text(Context ctx, String s, float size, int color, boolean bold) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(s, "s");
        TextView textView = new TextView(ctx);
        textView.setText(s);
        textView.setTextColor(color);
        textView.setTextSize(2, size);
        if (bold) {
            textView.setTypeface(textView.getTypeface(), 1);
        }
        textView.setSingleLine(true);
        return textView;
    }

    public static /* synthetic */ RippleDrawable rippleBg$default(Ui ui, Context context, int i, float f, int i2, Object obj) {
        if ((i2 & 4) != 0) {
            f = 10.0f;
        }
        return ui.rippleBg(context, i, f);
    }

    public final RippleDrawable rippleBg(Context ctx, int base, float radius) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(base);
        gradientDrawable.setCornerRadius(INSTANCE.dp(ctx, radius));
        GradientDrawable gradientDrawable2 = new GradientDrawable();
        gradientDrawable2.setColor(-1);
        gradientDrawable2.setCornerRadius(INSTANCE.dp(ctx, radius));
        return new RippleDrawable(ColorStateList.valueOf(LIGHT ? 570425344 : 872415231), gradientDrawable, gradientDrawable2);
    }

    public static /* synthetic */ TextView button$default(Ui ui, Context context, String str, int i, Function1 function1, int i2, Object obj) {
        if ((i2 & 4) != 0) {
            i = HOVER;
        }
        return ui.button(context, str, i, function1);
    }

    public final TextView button(Context ctx, String label, int base, final Function1<? super View, Unit> onClick) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(label, "label");
        Intrinsics.checkNotNullParameter(onClick, "onClick");
        TextView textViewText$default = text$default(this, ctx, label, 14.0f, 0, false, 24, null);
        textViewText$default.setGravity(17);
        textViewText$default.setMinHeight(INSTANCE.dp(ctx, 40.0f));
        int iDp = INSTANCE.dp(ctx, 14.0f);
        textViewText$default.setPadding(iDp, INSTANCE.dp(ctx, 8.0f), iDp, INSTANCE.dp(ctx, 8.0f));
        textViewText$default.setBackground(rippleBg$default(INSTANCE, ctx, base, 0.0f, 4, null));
        textViewText$default.setClickable(true);
        textViewText$default.setOnClickListener(new View.OnClickListener() { // from class: com.goydevv.studrblx.ui.Ui$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                onClick.invoke(view);
            }
        });
        return textViewText$default;
    }

    public static /* synthetic */ TextView iconTextButton$default(Ui ui, Context context, Drawable drawable, String str, int i, Function1 function1, int i2, Object obj) {
        if ((i2 & 8) != 0) {
            i = HOVER;
        }
        return ui.iconTextButton(context, drawable, str, i, function1);
    }

    public final TextView iconTextButton(Context ctx, Drawable icon, String label, int base, Function1<? super View, Unit> onClick) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(label, "label");
        Intrinsics.checkNotNullParameter(onClick, "onClick");
        TextView textViewButton = button(ctx, label, base, onClick);
        if (icon != null) {
            int iDp = INSTANCE.dp(ctx, 18.0f);
            icon.setBounds(0, 0, iDp, iDp);
            textViewButton.setCompoundDrawables(icon, null, null, null);
            textViewButton.setCompoundDrawablePadding(INSTANCE.dp(ctx, 6.0f));
        }
        return textViewButton;
    }

    public final LinearLayout toolButton(Context ctx, Drawable icon, String label, final Function1<? super View, Unit> onClick) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(icon, "icon");
        Intrinsics.checkNotNullParameter(label, "label");
        Intrinsics.checkNotNullParameter(onClick, "onClick");
        LinearLayout linearLayout = new LinearLayout(ctx);
        linearLayout.setOrientation(0);
        linearLayout.setGravity(16);
        int iDp = INSTANCE.dp(ctx, 8.0f);
        linearLayout.setPadding(iDp, INSTANCE.dp(ctx, 5.0f), iDp, INSTANCE.dp(ctx, 5.0f));
        linearLayout.setMinimumWidth(INSTANCE.dp(ctx, 60.0f));
        ImageView imageView = new ImageView(ctx);
        imageView.setImageDrawable(icon);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        linearLayout.addView(imageView, new LinearLayout.LayoutParams(INSTANCE.dp(ctx, 20.0f), INSTANCE.dp(ctx, 20.0f)));
        TextView textViewText$default = text$default(INSTANCE, ctx, label, 11.0f, TEXT, false, 16, null);
        textViewText$default.setGravity(16);
        textViewText$default.setPadding(INSTANCE.dp(ctx, 4.0f), 0, 0, 0);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        layoutParams.gravity = 16;
        Unit unit = Unit.INSTANCE;
        linearLayout.addView(textViewText$default, layoutParams);
        StateListDrawable stateListDrawable = new StateListDrawable();
        int[] iArr = {R.attr.state_selected};
        GradientDrawable gradientDrawable = new GradientDrawable();
        Ui ui3 = INSTANCE;
        gradientDrawable.setColor(SELECT);
        gradientDrawable.setCornerRadius(INSTANCE.dp(ctx, 8.0f));
        Unit unit2 = Unit.INSTANCE;
        stateListDrawable.addState(iArr, gradientDrawable);
        stateListDrawable.addState(new int[0], new ColorDrawable(0));
        linearLayout.setBackground(stateListDrawable);
        linearLayout.setForeground(rippleBg$default(INSTANCE, ctx, 0, 0.0f, 4, null));
        linearLayout.setClickable(true);
        linearLayout.setOnClickListener(new View.OnClickListener() { // from class: com.goydevv.studrblx.ui.Ui$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                onClick.invoke(view);
            }
        });
        return linearLayout;
    }

    public static /* synthetic */ EditText input$default(Ui ui, Context context, String str, String str2, boolean z, boolean z2, int i, Object obj) {
        if ((i & 2) != 0) {
            str = "";
        }
        if ((i & 4) != 0) {
            str2 = "";
        }
        if ((i & 8) != 0) {
            z = false;
        }
        if ((i & 16) != 0) {
            z2 = false;
        }
        return ui.input(context, str, str2, z, z2);
    }

    public final EditText input(Context ctx, String value, String hint, boolean numeric, boolean multiLine) {
        int i;
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(value, "value");
        Intrinsics.checkNotNullParameter(hint, "hint");
        EditText editText = new EditText(ctx);
        editText.setText(value);
        editText.setHint(hint);
        Ui ui = INSTANCE;
        editText.setTextColor(TEXT);
        Ui ui2 = INSTANCE;
        editText.setHintTextColor(DIM);
        editText.setTextSize(2, 15.0f);
        editText.setMinHeight(INSTANCE.dp(ctx, 44.0f));
        GradientDrawable gradientDrawable = new GradientDrawable();
        Ui ui3 = INSTANCE;
        gradientDrawable.setColor(INPUT);
        int iDp = INSTANCE.dp(ctx, 1.0f);
        Ui ui4 = INSTANCE;
        gradientDrawable.setStroke(iDp, BORDER);
        gradientDrawable.setCornerRadius(INSTANCE.dp(ctx, 8.0f));
        editText.setBackground(gradientDrawable);
        int iDp2 = INSTANCE.dp(ctx, 12.0f);
        editText.setPadding(iDp2, INSTANCE.dp(ctx, 10.0f), iDp2, INSTANCE.dp(ctx, 10.0f));
        if (numeric) {
            i = 12290;
        } else {
            i = multiLine ? 655361 : 524289;
        }
        editText.setInputType(i);
        if (!multiLine) {
            editText.setSingleLine(true);
        }
        return editText;
    }

    public static /* synthetic */ LinearLayout header$default(Ui ui, Context context, String str, Drawable drawable, int i, Object obj) {
        if ((i & 4) != 0) {
            drawable = null;
        }
        return ui.header(context, str, drawable);
    }

    public final LinearLayout header(Context ctx, String title, Drawable icon) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(title, "title");
        LinearLayout linearLayout = new LinearLayout(ctx);
        linearLayout.setOrientation(0);
        linearLayout.setGravity(16);
        Ui ui = INSTANCE;
        linearLayout.setBackgroundColor(HEADER);
        linearLayout.setPadding(INSTANCE.dp(ctx, 12.0f), INSTANCE.dp(ctx, 8.0f), INSTANCE.dp(ctx, 6.0f), INSTANCE.dp(ctx, 8.0f));
        if (icon != null) {
            ImageView imageView = new ImageView(ctx);
            imageView.setImageDrawable(icon);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(INSTANCE.dp(ctx, 16.0f), INSTANCE.dp(ctx, 16.0f));
            layoutParams.rightMargin = INSTANCE.dp(ctx, 6.0f);
            Unit unit = Unit.INSTANCE;
            linearLayout.addView(imageView, layoutParams);
        }
        Ui ui2 = INSTANCE;
        Ui ui3 = INSTANCE;
        linearLayout.addView(ui2.text(ctx, title, 13.0f, TEXT, true), new LinearLayout.LayoutParams(0, -2, 1.0f));
        return linearLayout;
    }

    public final TextView smallIconButton(Context ctx, String glyph, final Function1<? super View, Unit> onClick) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(glyph, "glyph");
        Intrinsics.checkNotNullParameter(onClick, "onClick");
        TextView textViewText$default = text$default(this, ctx, glyph, 13.0f, 0, false, 24, null);
        textViewText$default.setGravity(17);
        int iDp = INSTANCE.dp(ctx, 6.0f);
        textViewText$default.setPadding(iDp, INSTANCE.dp(ctx, 3.0f), iDp, INSTANCE.dp(ctx, 3.0f));
        textViewText$default.setBackground(rippleBg$default(INSTANCE, ctx, 0, 0.0f, 4, null));
        textViewText$default.setClickable(true);
        textViewText$default.setOnClickListener(new View.OnClickListener() { // from class: com.goydevv.studrblx.ui.Ui$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                onClick.invoke(view);
            }
        });
        return textViewText$default;
    }

    public static /* synthetic */ ImageView iconButton$default(Ui ui, Context context, Drawable drawable, float f, Function1 function1, int i, Object obj) {
        if ((i & 4) != 0) {
            f = 30.0f;
        }
        return ui.iconButton(context, drawable, f, function1);
    }

    public final ImageView iconButton(Context ctx, Drawable icon, float size, final Function1<? super View, Unit> onClick) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(icon, "icon");
        Intrinsics.checkNotNullParameter(onClick, "onClick");
        ImageView imageView = new ImageView(ctx);
        imageView.setImageDrawable(icon);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        int iDp = INSTANCE.dp(ctx, (size - 18.0f) / 2.0f);
        imageView.setPadding(iDp, iDp, iDp, iDp);
        imageView.setBackground(rippleBg$default(INSTANCE, ctx, 0, 0.0f, 4, null));
        imageView.setClickable(true);
        imageView.setOnClickListener(new View.OnClickListener() { // from class: com.goydevv.studrblx.ui.Ui$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                onClick.invoke(view);
            }
        });
        imageView.setLayoutParams(new LinearLayout.LayoutParams(INSTANCE.dp(ctx, size), INSTANCE.dp(ctx, size)));
        return imageView;
    }

    public static /* synthetic */ LinearLayout.LayoutParams lp$default(Ui ui, int i, int i2, float f, int i3, Object obj) {
        if ((i3 & 4) != 0) {
            f = 0.0f;
        }
        return ui.lp(i, i2, f);
    }

    public final LinearLayout.LayoutParams lp(int w, int h, float weight) {
        return new LinearLayout.LayoutParams(w, h, weight);
    }

    public static /* synthetic */ View divider$default(Ui ui, Context context, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return ui.divider(context, z);
    }

    public final View divider(Context ctx, boolean vertical) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        View view = new View(ctx);
        Ui ui = INSTANCE;
        view.setBackgroundColor(BORDER);
        int iDp = INSTANCE.dp(ctx, 1.0f);
        view.setLayoutParams(vertical ? new LinearLayout.LayoutParams(iDp, -1) : new LinearLayout.LayoutParams(-1, iDp));
        return view;
    }

    public final Drawable icon(Context ctx, Instance i) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(i, "i");
        return Icons.INSTANCE.instance(ctx, i.getClassName());
    }

    public final Drawable iconFor(Context ctx, String cls) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(cls, "cls");
        return Icons.INSTANCE.instance(ctx, cls);
    }

    /* JADX INFO: compiled from: Ui.kt */
    public static final class CheckBoxDrawable extends Drawable {
        private final boolean checked;
        private final float density;
        private final Paint p = new Paint(1);

        public CheckBoxDrawable(boolean checked, float density) {
            this.checked = checked;
            this.density = density;
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas c) {
            Intrinsics.checkNotNullParameter(c, "c");
            Rect b = getBounds();
            Intrinsics.checkNotNullExpressionValue(b, "getBounds(...)");
            float s = Math.min(b.width(), b.height());
            float l = b.left;
            float t = b.top + ((b.height() - s) / 2.0f);
            this.p.setStyle(Paint.Style.FILL);
            this.p.setColor(this.checked ? Ui.INSTANCE.getACCENT() : Ui.INSTANCE.getINPUT());
            c.drawRoundRect(l, t, l + s, t + s, this.density * 2.0f, this.density * 2.0f, this.p);
            this.p.setStyle(Paint.Style.STROKE);
            this.p.setStrokeWidth(this.density * 1.0f);
            this.p.setColor(this.checked ? Ui.INSTANCE.getACCENT() : -7697782);
            c.drawRoundRect(l, t, l + s, t + s, this.density * 2.0f, this.density * 2.0f, this.p);
            if (this.checked) {
                this.p.setColor(-1);
                this.p.setStrokeWidth(this.density * 1.8f);
                this.p.setStrokeCap(Paint.Cap.ROUND);
                Path path = new Path();
                path.moveTo((0.22f * s) + l, (0.52f * s) + t);
                path.lineTo((0.42f * s) + l, (0.72f * s) + t);
                path.lineTo((0.78f * s) + l, (0.3f * s) + t);
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

    /* JADX INFO: compiled from: Ui.kt */
    public static final class ArrowDrawable extends Drawable {
        private final int color;
        private final boolean expanded;
        private final Paint p;

        public ArrowDrawable(boolean expanded, int color) {
            this.expanded = expanded;
            this.color = color;
            Paint paint = new Paint(1);
            paint.setStyle(Paint.Style.FILL);
            this.p = paint;
        }

        public /* synthetic */ ArrowDrawable(boolean z, int i, int i2, DefaultConstructorMarker defaultConstructorMarker) {
            this(z, (i2 & 2) != 0 ? Ui.INSTANCE.getDIM() : i);
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas c) {
            Intrinsics.checkNotNullParameter(c, "c");
            Rect b = getBounds();
            Intrinsics.checkNotNullExpressionValue(b, "getBounds(...)");
            float s = Math.min(b.width(), b.height()) * 0.36f;
            float cx = b.exactCenterX();
            float cy = b.exactCenterY();
            this.p.setColor(this.color);
            Path path = new Path();
            if (this.expanded) {
                path.moveTo(cx - s, cy - (s * 0.5f));
                path.lineTo(cx + s, cy - (0.5f * s));
                path.lineTo(cx, (0.6f * s) + cy);
            } else {
                path.moveTo(cx - (s * 0.5f), cy - s);
                path.lineTo((0.6f * s) + cx, cy);
                path.lineTo(cx - (0.5f * s), cy + s);
            }
            path.close();
            c.drawPath(path, this.p);
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
}

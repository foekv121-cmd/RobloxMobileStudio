package com.goydevv.studrblx.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import kotlin.Deprecated;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.ranges.RangesKt;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: compiled from: DockLayout.kt */
/* JADX INFO: loaded from: classes5.dex */
public final class DockLayout extends FrameLayout {
    private boolean allHidden;
    private int bottomH;
    private final LinearLayout bottomRow;
    private final Splitter bottomSplit;
    private final LinearLayout centerCol;
    private final FrameLayout centerHost;
    private final float d;
    private final FrameLayout floatLayer;
    private final LinearLayout leftCol;
    private final Splitter leftSplit;
    private int leftW;
    private final LinearLayout main;
    private Function0<Unit> onChanged;
    private final DropOverlay overlay;
    private final ArrayList<Panel> panels;
    private final boolean phone;
    private final SharedPreferences prefs;
    private final LinearLayout rightCol;
    private final Splitter rightSplit;
    private int rightW;

    /* JADX INFO: compiled from: DockLayout.kt */
    public enum Zone {
        LEFT,
        RIGHT,
        BOTTOM,
        FLOAT;

        private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries(values());

        public static EnumEntries<Zone> getEntries() {
            return $ENTRIES;
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public DockLayout(Context ctx, SharedPreferences prefs) {
        super(ctx);
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(prefs, "prefs");
        this.prefs = prefs;
        this.d = getResources().getDisplayMetrics().density;
        this.panels = new ArrayList<>();
        LinearLayout linearLayout = new LinearLayout(ctx);
        linearLayout.setOrientation(0);
        this.main = linearLayout;
        LinearLayout linearLayout2 = new LinearLayout(ctx);
        linearLayout2.setOrientation(1);
        this.leftCol = linearLayout2;
        LinearLayout linearLayout3 = new LinearLayout(ctx);
        linearLayout3.setOrientation(1);
        this.rightCol = linearLayout3;
        LinearLayout linearLayout4 = new LinearLayout(ctx);
        linearLayout4.setOrientation(1);
        this.centerCol = linearLayout4;
        LinearLayout linearLayout5 = new LinearLayout(ctx);
        linearLayout5.setOrientation(0);
        this.bottomRow = linearLayout5;
        this.centerHost = new FrameLayout(ctx);
        this.floatLayer = new FrameLayout(ctx);
        this.overlay = new DropOverlay(this, ctx);
        this.phone = getResources().getConfiguration().smallestScreenWidthDp < 600;
        this.leftW = dp(this.phone ? 162.0f : 215.0f);
        this.rightW = dp(this.phone ? 178.0f : 240.0f);
        this.bottomH = dp(this.phone ? 140.0f : 190.0f);
        setBackgroundColor(Ui.INSTANCE.getBORDER());
        this.leftSplit = new Splitter(ctx, true, new Function1() { // from class: com.goydevv.studrblx.ui.DockLayout$$ExternalSyntheticLambda1
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return DockLayout._init_$lambda$5(DockLayout.this, ((Float) obj).floatValue());
            }
        }, new Function0() { // from class: com.goydevv.studrblx.ui.DockLayout$$ExternalSyntheticLambda2
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return DockLayout._init_$lambda$6(DockLayout.this);
            }
        });
        this.rightSplit = new Splitter(ctx, true, new Function1() { // from class: com.goydevv.studrblx.ui.DockLayout$$ExternalSyntheticLambda3
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return DockLayout._init_$lambda$7(DockLayout.this, ((Float) obj).floatValue());
            }
        }, new Function0() { // from class: com.goydevv.studrblx.ui.DockLayout$$ExternalSyntheticLambda4
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return DockLayout._init_$lambda$8(DockLayout.this);
            }
        });
        this.bottomSplit = new Splitter(ctx, false, new Function1() { // from class: com.goydevv.studrblx.ui.DockLayout$$ExternalSyntheticLambda5
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return DockLayout._init_$lambda$9(DockLayout.this, ((Float) obj).floatValue());
            }
        }, new Function0() { // from class: com.goydevv.studrblx.ui.DockLayout$$ExternalSyntheticLambda6
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return DockLayout._init_$lambda$10(DockLayout.this);
            }
        });
        this.main.addView(this.leftCol, new LinearLayout.LayoutParams(this.leftW, -1));
        this.main.addView(this.leftSplit, new LinearLayout.LayoutParams(this.leftSplit.getThickness(), -1));
        this.main.addView(this.centerCol, new LinearLayout.LayoutParams(0, -1, 1.0f));
        this.main.addView(this.rightSplit, new LinearLayout.LayoutParams(this.rightSplit.getThickness(), -1));
        this.main.addView(this.rightCol, new LinearLayout.LayoutParams(this.rightW, -1));
        this.centerCol.addView(this.centerHost, new LinearLayout.LayoutParams(-1, 0, 1.0f));
        this.centerCol.addView(this.bottomSplit, new LinearLayout.LayoutParams(-1, this.bottomSplit.getThickness()));
        this.centerCol.addView(this.bottomRow, new LinearLayout.LayoutParams(-1, this.bottomH));
        addView(this.main, new FrameLayout.LayoutParams(-1, -1));
        addView(this.floatLayer, new FrameLayout.LayoutParams(-1, -1));
        addView(this.overlay, new FrameLayout.LayoutParams(-1, -1));
        this.overlay.setVisibility(8);
    }

    /* JADX INFO: compiled from: DockLayout.kt */
    public final class Panel {
        private final int defOrder;
        private final boolean defVisible;
        private final Zone defZone;
        private int fh;
        private FloatFrame frame;
        private int fw;
        private int fx;
        private int fy;
        private final String id;
        private int order;
        final /* synthetic */ DockLayout this$0;
        private final String title;
        private final View view;
        private boolean visible;
        private float weight;
        private Zone zone;

        public Panel(DockLayout this$0, String id, String title, View view, Zone defZone, int defOrder, boolean defVisible) {
            Intrinsics.checkNotNullParameter(id, "id");
            Intrinsics.checkNotNullParameter(title, "title");
            Intrinsics.checkNotNullParameter(view, "view");
            Intrinsics.checkNotNullParameter(defZone, "defZone");
            this.this$0 = this$0;
            this.id = id;
            this.title = title;
            this.view = view;
            this.defZone = defZone;
            this.defOrder = defOrder;
            this.defVisible = defVisible;
            this.zone = this.defZone;
            this.order = this.defOrder;
            this.weight = 1.0f;
            this.visible = this.defVisible;
            this.fx = -1;
            this.fy = -1;
        }

        public final int getDefOrder() {
            return this.defOrder;
        }

        public final boolean getDefVisible() {
            return this.defVisible;
        }

        public final Zone getDefZone() {
            return this.defZone;
        }

        public final String getId() {
            return this.id;
        }

        public final String getTitle() {
            return this.title;
        }

        public final View getView() {
            return this.view;
        }

        public final Zone getZone() {
            return this.zone;
        }

        public final void setZone(Zone zone) {
            Intrinsics.checkNotNullParameter(zone, "<set-?>");
            this.zone = zone;
        }

        public final int getOrder() {
            return this.order;
        }

        public final void setOrder(int i) {
            this.order = i;
        }

        public final float getWeight() {
            return this.weight;
        }

        public final void setWeight(float f) {
            this.weight = f;
        }

        public final boolean getVisible() {
            return this.visible;
        }

        public final void setVisible(boolean z) {
            this.visible = z;
        }

        public final int getFh() {
            return this.fh;
        }

        public final int getFw() {
            return this.fw;
        }

        public final int getFx() {
            return this.fx;
        }

        public final int getFy() {
            return this.fy;
        }

        public final void setFh(int i) {
            this.fh = i;
        }

        public final void setFw(int i) {
            this.fw = i;
        }

        public final void setFx(int i) {
            this.fx = i;
        }

        public final void setFy(int i) {
            this.fy = i;
        }

        public final FloatFrame getFrame() {
            return this.frame;
        }

        public final void setFrame(FloatFrame floatFrame) {
            this.frame = floatFrame;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final int dp(float v) {
        return (int) (this.d * v);
    }

    public final int getLeftW() {
        return this.leftW;
    }

    public final void setLeftW(int i) {
        this.leftW = i;
    }

    public final int getRightW() {
        return this.rightW;
    }

    public final void setRightW(int i) {
        this.rightW = i;
    }

    public final int getBottomH() {
        return this.bottomH;
    }

    public final void setBottomH(int i) {
        this.bottomH = i;
    }

    public final boolean getAllHidden() {
        return this.allHidden;
    }

    public final void setAllHidden(boolean v) {
        this.allHidden = v;
        rebuild();
        Function0<Unit> function0 = this.onChanged;
        if (function0 != null) {
            function0.invoke();
        }
    }

    public final Function0<Unit> getOnChanged() {
        return this.onChanged;
    }

    public final void setOnChanged(Function0<Unit> function0) {
        this.onChanged = function0;
    }

    static final Unit _init_$lambda$5(DockLayout this$0, float dx) {
        this$0.leftW = this$0.clampW(this$0.leftW + ((int) dx));
        this$0.leftCol.setLayoutParams(new LinearLayout.LayoutParams(this$0.leftW, -1));
        return Unit.INSTANCE;
    }

    static final Unit _init_$lambda$6(DockLayout this$0) {
        try {
        this$0.save();
        return Unit.INSTANCE;
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    static final Unit _init_$lambda$7(DockLayout this$0, float dx) {
        this$0.rightW = this$0.clampW(this$0.rightW - ((int) dx));
        this$0.rightCol.setLayoutParams(new LinearLayout.LayoutParams(this$0.rightW, -1));
        return Unit.INSTANCE;
    }

    static final Unit _init_$lambda$8(DockLayout this$0) {
        try {
        this$0.save();
        return Unit.INSTANCE;
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    static final Unit _init_$lambda$10(DockLayout this$0) {
        try {
        this$0.save();
        return Unit.INSTANCE;
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    static final Unit _init_$lambda$9(DockLayout this$0, float dy) {
        this$0.bottomH = this$0.clampH(this$0.bottomH - ((int) dy));
        this$0.bottomRow.setLayoutParams(new LinearLayout.LayoutParams(-1, this$0.bottomH));
        return Unit.INSTANCE;
    }

    private final int clampW(int w) {
        return RangesKt.coerceIn(w, dp(115.0f), (int) (getResources().getDisplayMetrics().widthPixels * 0.6f));
    }

    private final int clampH(int h) {
        return RangesKt.coerceIn(h, dp(60.0f), (int) (getResources().getDisplayMetrics().heightPixels * 0.7f));
    }

    public final void setCenter(View v) {
        Intrinsics.checkNotNullParameter(v, "v");
        this.centerHost.addView(v, new FrameLayout.LayoutParams(-1, -1));
    }

    public static /* synthetic */ void addPanel$default(DockLayout dockLayout, String str, String str2, View view, Zone zone, int i, boolean z, int i2, Object obj) {
        if ((i2 & 32) != 0) {
            z = true;
        }
        dockLayout.addPanel(str, str2, view, zone, i, z);
    }

    public final void addPanel(String id, String title, View view, Zone zone, int order, boolean visible) {
        Intrinsics.checkNotNullParameter(id, "id");
        Intrinsics.checkNotNullParameter(title, "title");
        Intrinsics.checkNotNullParameter(view, "view");
        Intrinsics.checkNotNullParameter(zone, "zone");
        Panel p = new Panel(this, id, title, view, zone, order, visible);
        this.panels.add(p);
        decorateHeader(p);
    }

    public final Panel panel(String id) {
        Object next;
        Intrinsics.checkNotNullParameter(id, "id");
        Iterator it = this.panels.iterator();
        while (it.hasNext()) {
            next = it.next();
            if (Intrinsics.areEqual(((Panel) next).getId(), id)) {
                return (Panel) next;
            }
        }
        next = null;
        return (Panel) next;
    }

    public final boolean isShown(String id) {
        Intrinsics.checkNotNullParameter(id, "id");
        if (this.allHidden) {
            return false;
        }
        Panel panel = panel(id);
        return panel != null && panel.getVisible();
    }

    public final void setShown(String id, boolean show) {
        try {
        Intrinsics.checkNotNullParameter(id, "id");
        Panel p = panel(id);
        if (p == null) {
            return;
        }
        if (this.allHidden && show) {
            setAllHidden(false);
        }
        p.setVisible(show);
        rebuild();
        save();
        Function0<Unit> function0 = this.onChanged;
        if (function0 != null) {
            function0.invoke();
        }
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    public final void toggle(String id) {
        try {
        Intrinsics.checkNotNullParameter(id, "id");
        Panel panel = panel(id);
        boolean z = true;
        if ((panel != null && panel.getVisible()) && !this.allHidden) {
            z = false;
        }
        setShown(id, z);
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    public final void load() {
        Zone defZone;
        String raw = this.prefs.getString("dock_v1", null);
        if (raw == null) {
            Integer numValueOf = Integer.valueOf(this.prefs.getInt("toolboxW", -1));
            if ((numValueOf.intValue() > 0 ? 1 : 0) == 0) {
                numValueOf = null;
            }
            if (numValueOf != null) {
                this.leftW = clampW(numValueOf.intValue());
            }
            Integer numValueOf2 = Integer.valueOf(this.prefs.getInt("rightW", -1));
            if ((numValueOf2.intValue() > 0 ? 1 : 0) == 0) {
                numValueOf2 = null;
            }
            if (numValueOf2 != null) {
                this.rightW = clampW(numValueOf2.intValue());
            }
            Integer numValueOf3 = Integer.valueOf(this.prefs.getInt("outputH", -1));
            Integer num = numValueOf3.intValue() > 0 ? numValueOf3 : null;
            if (num != null) {
                this.bottomH = clampH(num.intValue());
            }
        }
        if (raw != null) {
            try {
                JSONObject o = new JSONObject(raw);
                this.leftW = o.optInt("leftW", this.leftW);
                this.rightW = o.optInt("rightW", this.rightW);
                this.bottomH = o.optInt("bottomH", this.bottomH);
                JSONArray arr = o.optJSONArray("panels");
                if (arr == null) {
                    arr = new JSONArray();
                }
                int length = arr.length();
                for (int i = 0; i < length; i++) {
                    JSONObject po = arr.getJSONObject(i);
                    String strOptString = po.optString("id");
                    Intrinsics.checkNotNullExpressionValue(strOptString, "optString(...)");
                    Panel p = panel(strOptString);
                    if (p != null) {
                        try {
                            String strOptString2 = po.optString("zone");
                            Intrinsics.checkNotNullExpressionValue(strOptString2, "optString(...)");
                            defZone = Zone.valueOf(strOptString2);
                        } catch (Exception e) {
                            defZone = p.getDefZone();
                        }
                        p.setZone(defZone);
                        p.setOrder(po.optInt("order", p.getOrder()));
                        p.setWeight(RangesKt.coerceIn((float) po.optDouble("weight", 1.0d), 0.05f, 20.0f));
                        p.setVisible(po.optBoolean("visible", p.getVisible()));
                        p.setFx(po.optInt("fx", -1));
                        p.setFy(po.optInt("fy", -1));
                        p.setFw(po.optInt("fw", 0));
                        p.setFh(po.optInt("fh", 0));
                    }
                }
            } catch (Exception e2) {
            }
        }
        rebuild();
    }

    public final void save() {
        try {
        JSONArray arr = new JSONArray();
        Iterator<Panel> it = this.panels.iterator();
        Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
        while (it.hasNext()) {
            Panel next = it.next();
            Intrinsics.checkNotNullExpressionValue(next, "next(...)");
            Panel p = next;
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("id", p.getId());
            jSONObject.put("zone", p.getZone().name());
            jSONObject.put("order", p.getOrder());
            jSONObject.put("weight", p.getWeight());
            jSONObject.put("visible", p.getVisible());
            jSONObject.put("fx", p.getFx());
            jSONObject.put("fy", p.getFy());
            jSONObject.put("fw", p.getFw());
            jSONObject.put("fh", p.getFh());
            arr.put(jSONObject);
        }
        SharedPreferences.Editor editorEdit = this.prefs.edit();
        JSONObject jSONObject2 = new JSONObject();
        jSONObject2.put("leftW", this.leftW);
        jSONObject2.put("rightW", this.rightW);
        jSONObject2.put("bottomH", this.bottomH);
        jSONObject2.put("panels", arr);
        Unit unit = Unit.INSTANCE;
        editorEdit.putString("dock_v1", jSONObject2.toString()).apply();
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    public final void reset() {
        this.prefs.edit().remove("dock_v1").apply();
        this.leftW = dp(this.phone ? 162.0f : 215.0f);
        this.rightW = dp(this.phone ? 178.0f : 240.0f);
        this.bottomH = dp(this.phone ? 140.0f : 190.0f);
        Iterator<Panel> it = this.panels.iterator();
        Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
        while (it.hasNext()) {
            Panel next = it.next();
            Intrinsics.checkNotNullExpressionValue(next, "next(...)");
            Panel p = next;
            p.setZone(p.getDefZone());
            p.setOrder(p.getDefOrder());
            p.setWeight(1.0f);
            p.setVisible(p.getDefVisible());
            p.setFx(-1);
            p.setFy(-1);
            p.setFw(0);
            p.setFh(0);
        }
        setAllHidden(false);
        rebuild();
        Function0<Unit> function0 = this.onChanged;
        if (function0 != null) {
            function0.invoke();
        }
    }

    private final void detach(View v) {
        ViewParent parent = v.getParent();
        ViewGroup viewGroup = parent instanceof ViewGroup ? (ViewGroup) parent : null;
        if (viewGroup != null) {
            viewGroup.removeView(v);
        }
    }

    public final void rebuild() {
        List vis;
        Iterator<Panel> it = this.panels.iterator();
        Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
        while (it.hasNext()) {
            Panel p = it.next();
            Intrinsics.checkNotNullExpressionValue(p, "next(...)");
            detach(p.getView());
        }
        this.leftCol.removeAllViews();
        this.rightCol.removeAllViews();
        this.bottomRow.removeAllViews();
        this.floatLayer.removeAllViews();
        if (this.allHidden) {
            vis = CollectionsKt.emptyList();
        } else {
            Iterable iterable = this.panels;
            Collection arrayList = new ArrayList();
            for (Object obj : iterable) {
                if (((Panel) obj).getVisible()) {
                    arrayList.add(obj);
                }
            }
            vis = (List) arrayList;
        }
        rebuild$fill(vis, this, this.leftCol, Zone.LEFT, false);
        rebuild$fill(vis, this, this.rightCol, Zone.RIGHT, false);
        rebuild$fill(vis, this, this.bottomRow, Zone.BOTTOM, true);
        boolean hasL = this.leftCol.getChildCount() > 0;
        boolean hasR = this.rightCol.getChildCount() > 0;
        boolean hasB = this.bottomRow.getChildCount() > 0;
        this.leftCol.setVisibility(hasL ? 0 : 8);
        this.leftSplit.setVisibility(this.leftCol.getVisibility());
        this.rightCol.setVisibility(hasR ? 0 : 8);
        this.rightSplit.setVisibility(this.rightCol.getVisibility());
        this.bottomRow.setVisibility(hasB ? 0 : 8);
        this.bottomSplit.setVisibility(this.bottomRow.getVisibility());
        this.leftCol.setLayoutParams(new LinearLayout.LayoutParams(this.leftW, -1));
        this.rightCol.setLayoutParams(new LinearLayout.LayoutParams(this.rightW, -1));
        this.bottomRow.setLayoutParams(new LinearLayout.LayoutParams(-1, this.bottomH));
        Collection arrayList2 = new ArrayList();
        for (Object obj2 : vis) {
            if (((Panel) obj2).getZone() == Zone.FLOAT) {
                arrayList2.add(obj2);
            }
        }
        for (Panel p2 : (List<Panel>) arrayList2) {
            FloatFrame f = p2.getFrame();
            if (f == null) {
                Context context = getContext();
                Intrinsics.checkNotNullExpressionValue(context, "getContext(...)");
                f = new FloatFrame(this, context, p2);
                p2.setFrame(f);
            }
            f.attach();
            if (p2.getFw() <= 0) {
                p2.setFw(dp(300.0f));
                p2.setFh(dp(360.0f));
            }
            if (p2.getFx() < 0) {
                p2.setFx((getWidth() - p2.getFw()) / 2);
                p2.setFy(dp(40.0f));
            }
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(p2.getFw(), p2.getFh());
            layoutParams.leftMargin = p2.getFx();
            layoutParams.topMargin = p2.getFy();
            Unit unit = Unit.INSTANCE;
            this.floatLayer.addView(f, layoutParams);
        }
        post(new Runnable() { // from class: com.goydevv.studrblx.ui.DockLayout$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                DockLayout.this.clampFloats();
            }
        });
    }

    private static final void rebuild$fill(List<Panel> list, final DockLayout this$0, LinearLayout col, Zone zone, boolean horizontal) {
        final LinearLayout linearLayout = col;
        final boolean z = horizontal;
        Collection arrayList = new ArrayList();
        for (Object obj : list) {
            if (((Panel) obj).getZone() == zone) {
                arrayList.add(obj);
            }
        }
        List list2 = CollectionsKt.sortedWith((List) arrayList, new Comparator() { // from class: com.goydevv.studrblx.ui.DockLayout$rebuild$fill$$inlined$sortedBy$1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.util.Comparator
            public final int compare(Object t, Object t2) {
                return ComparisonsKt.compareValues(Integer.valueOf(((DockLayout.Panel) t).getOrder()), Integer.valueOf(((DockLayout.Panel) t2).getOrder()));
            }
        });
        List list3 = list2;
        int i = 0;
        for (Object obj2 : list3) {
            int i2 = i + 1;
            if (i < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            final Panel panel = (Panel) obj2;
            int i3 = i;
            if (i3 > 0) {
                final Panel panel2 = (Panel) list2.get(i3 - 1);
                Context context = this$0.getContext();
                Intrinsics.checkNotNullExpressionValue(context, "getContext(...)");
                Splitter splitter = new Splitter(context, z, new Function1() { // from class: com.goydevv.studrblx.ui.DockLayout$$ExternalSyntheticLambda10
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj3) {
                        return DockLayout.rebuild$fill$lambda$25$lambda$23(this$0, linearLayout, panel2, panel, z, ((Float) obj3).floatValue());
                    }
                }, new Function0() { // from class: com.goydevv.studrblx.ui.DockLayout$$ExternalSyntheticLambda11
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return DockLayout.rebuild$fill$lambda$25$lambda$24(this$0);
                    }
                });
                linearLayout.addView(splitter, z ? new LinearLayout.LayoutParams(splitter.getThickness(), -1) : new LinearLayout.LayoutParams(-1, splitter.getThickness()));
            }
            linearLayout.addView(panel.getView(), z ? new LinearLayout.LayoutParams(0, -1, panel.getWeight()) : new LinearLayout.LayoutParams(-1, 0, panel.getWeight()));
            i = i2;
        }
    }

    static final Unit rebuild$fill$lambda$25$lambda$23(DockLayout this$0, LinearLayout $col, Panel $a, Panel $b, boolean $horizontal, float delta) {
        this$0.resizePair($col, $a, $b, delta, $horizontal);
        return Unit.INSTANCE;
    }

    static final Unit rebuild$fill$lambda$25$lambda$24(DockLayout this$0) {
        try {
        this$0.save();
        return Unit.INSTANCE;
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void clampFloats() {
        Iterator<Panel> it = this.panels.iterator();
        Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
        while (it.hasNext()) {
            Panel next = it.next();
            Intrinsics.checkNotNullExpressionValue(next, "next(...)");
            Panel p = next;
            FloatFrame f = p.getFrame();
            if (f != null && f.getParent() == this.floatLayer) {
                ViewGroup.LayoutParams layoutParams = f.getLayoutParams();
                Intrinsics.checkNotNull(layoutParams, "null cannot be cast to non-null type android.widget.FrameLayout.LayoutParams");
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) layoutParams;
                int w = RangesKt.coerceAtMost(lp.width, RangesKt.coerceAtLeast(getWidth(), dp(160.0f)));
                int h = RangesKt.coerceAtMost(lp.height, RangesKt.coerceAtLeast(getHeight(), dp(120.0f)));
                int x = RangesKt.coerceIn(lp.leftMargin, 0, RangesKt.coerceAtLeast(getWidth() - dp(60.0f), 0));
                int y = RangesKt.coerceIn(lp.topMargin, 0, RangesKt.coerceAtLeast(getHeight() - dp(30.0f), 0));
                if (w != lp.width || h != lp.height || x != lp.leftMargin || y != lp.topMargin) {
                    lp.width = w;
                    lp.height = h;
                    lp.leftMargin = x;
                    lp.topMargin = y;
                    f.setLayoutParams(lp);
                    p.setFx(x);
                    p.setFy(y);
                    p.setFw(w);
                    p.setFh(h);
                }
            }
        }
    }

    private final void resizePair(LinearLayout col, Panel a, Panel b, float delta, boolean horizontal) {
        View view = a.getView();
        int sizeA = horizontal ? view.getWidth() : view.getHeight();
        View view2 = b.getView();
        int sizeB = horizontal ? view2.getWidth() : view2.getHeight();
        int total = sizeA + sizeB;
        if (total <= 0) {
            return;
        }
        int min = dp(60.0f);
        float newA = RangesKt.coerceIn(sizeA + delta, min, total - min);
        float wSum = a.getWeight() + b.getWeight();
        a.setWeight((wSum * newA) / total);
        b.setWeight(wSum - a.getWeight());
        ViewGroup.LayoutParams layoutParams = a.getView().getLayoutParams();
        LinearLayout.LayoutParams layoutParams2 = layoutParams instanceof LinearLayout.LayoutParams ? (LinearLayout.LayoutParams) layoutParams : null;
        if (layoutParams2 != null) {
            layoutParams2.weight = a.getWeight();
            a.getView().setLayoutParams(layoutParams2);
        }
        ViewGroup.LayoutParams layoutParams3 = b.getView().getLayoutParams();
        LinearLayout.LayoutParams layoutParams4 = layoutParams3 instanceof LinearLayout.LayoutParams ? (LinearLayout.LayoutParams) layoutParams3 : null;
        if (layoutParams4 != null) {
            layoutParams4.weight = b.getWeight();
            b.getView().setLayoutParams(layoutParams4);
        }
        col.requestLayout();
    }

    private final ViewGroup headerOf(Panel p) {
        View view = p.getView();
        ViewGroup viewGroup = view instanceof ViewGroup ? (ViewGroup) view : null;
        View childAt = viewGroup != null ? viewGroup.getChildAt(0) : null;
        if (childAt instanceof ViewGroup) {
            return (ViewGroup) childAt;
        }
        return null;
    }

    private final void decorateHeader(final Panel p) {
        final ViewGroup h = headerOf(p);
        if (h == null) {
            return;
        }
        Context ctx = getContext();
        ImageView imageView = new ImageView(ctx);
        Icons icons = Icons.INSTANCE;
        Intrinsics.checkNotNull(ctx);
        imageView.setImageDrawable(icons.ui(ctx, "Grip", "⋮"));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dp(12.0f), dp(18.0f));
        layoutParams.rightMargin = dp(2.0f);
        Unit unit = Unit.INSTANCE;
        h.addView(imageView, 0, layoutParams);
        for (int ci = 0; ci < h.getChildCount(); ci++) {
            View ch = h.getChildAt(ci);
            if (ch instanceof TextView) {
                TextView tv = (TextView) ch;
                tv.setSingleLine(true);
                tv.setEllipsize(android.text.TextUtils.TruncateAt.END);
                tv.setMinWidth(dp(52.0f));
            }
        }
        h.addView(Ui.INSTANCE.iconButton(ctx, Icons.INSTANCE.ui(ctx, "Float", "F"), 22.0f, new Function1() { // from class: com.goydevv.studrblx.ui.DockLayout$$ExternalSyntheticLambda7
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return DockLayout.decorateHeader$lambda$34(DockLayout.this, p, (View) obj);
            }
        }));
        h.addView(Ui.INSTANCE.iconButton(ctx, Icons.INSTANCE.ui(ctx, "CloseWidget", "x"), 26.0f, new Function1() { // from class: com.goydevv.studrblx.ui.DockLayout$$ExternalSyntheticLambda8
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return DockLayout.decorateHeader$lambda$35(DockLayout.this, p, (View) obj);
            }
        }));
        final Ref.FloatRef downX = new Ref.FloatRef();
        final Ref.FloatRef downY = new Ref.FloatRef();
        final Ref.BooleanRef dragging = new Ref.BooleanRef();
        final Ref.LongRef lastTap = new Ref.LongRef();
        final Ref.IntRef startFx = new Ref.IntRef();
        final Ref.IntRef startFy = new Ref.IntRef();
        h.setOnTouchListener(new View.OnTouchListener() { // from class: com.goydevv.studrblx.ui.DockLayout$$ExternalSyntheticLambda9
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                return DockLayout.decorateHeader$lambda$36(downX, downY, dragging, startFx, p, startFy, h, DockLayout.this, lastTap, view, motionEvent);
            }
        });
    }

    static final Unit decorateHeader$lambda$34(DockLayout this$0, Panel $p, View it) {
        try {
        Intrinsics.checkNotNullParameter(it, "it");
        this$0.toggleFloat($p);
        return Unit.INSTANCE;
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    static final Unit decorateHeader$lambda$35(DockLayout this$0, Panel $p, View it) {
        try {
        Intrinsics.checkNotNullParameter(it, "it");
        this$0.setShown($p.getId(), false);
        return Unit.INSTANCE;
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    static final boolean decorateHeader$lambda$36(Ref.FloatRef $downX, Ref.FloatRef $downY, Ref.BooleanRef $dragging, Ref.IntRef $startFx, Panel $p, Ref.IntRef $startFy, ViewGroup $h, DockLayout this$0, Ref.LongRef $lastTap, View view, MotionEvent e) {
        try {
        switch (e.getActionMasked()) {
            case 0:
                $downX.element = e.getRawX();
                $downY.element = e.getRawY();
                $dragging.element = false;
                $startFx.element = $p.getFx();
                $startFy.element = $p.getFy();
                ViewParent parent = $h.getParent();
                if (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(true);
                }
                return true;
            case 1:
            case 3:
                if ($dragging.element) {
                    Pair<Float, Float> pairLocal = this$0.local(e.getRawX(), e.getRawY());
                    float x = pairLocal.component1().floatValue();
                    float y = pairLocal.component2().floatValue();
                    Target t = this$0.overlay.update($p, x, y);
                    this$0.overlay.end();
                    if (e.getActionMasked() == 1) {
                        this$0.drop($p, t);
                    }
                } else if (e.getActionMasked() == 1) {
                    long now = System.currentTimeMillis();
                    if (now - $lastTap.element < 320) {
                        this$0.toggleFloat($p);
                        $lastTap.element = 0L;
                    } else {
                        $lastTap.element = now;
                    }
                }
                return true;
            case 2:
                if (!$dragging.element && ((float) Math.hypot(e.getRawX() - $downX.element, e.getRawY() - $downY.element)) > this$0.d * 10.0f) {
                    $dragging.element = true;
                    this$0.overlay.begin($p);
                }
                if ($dragging.element) {
                    if ($p.getZone() == Zone.FLOAT) {
                        FloatFrame frame = $p.getFrame();
                        if ((frame != null ? frame.getParent() : null) == this$0.floatLayer) {
                            FloatFrame f = $p.getFrame();
                            Intrinsics.checkNotNull(f);
                            ViewGroup.LayoutParams layoutParams = f.getLayoutParams();
                            Intrinsics.checkNotNull(layoutParams, "null cannot be cast to non-null type android.widget.FrameLayout.LayoutParams");
                            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) layoutParams;
                            lp.leftMargin = (int) ($startFx.element + (e.getRawX() - $downX.element));
                            lp.topMargin = RangesKt.coerceAtLeast((int) ($startFy.element + (e.getRawY() - $downY.element)), 0);
                            f.setLayoutParams(lp);
                        }
                    }
                    Pair<Float, Float> pairLocal2 = this$0.local(e.getRawX(), e.getRawY());
                    float x2 = pairLocal2.component1().floatValue();
                    float y2 = pairLocal2.component2().floatValue();
                    this$0.overlay.update($p, x2, y2);
                }
                return true;
            default:
                return false;
        }
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    private final Pair<Float, Float> local(float rawX, float rawY) {
        int[] loc = new int[2];
        getLocationOnScreen(loc);
        return TuplesKt.to(Float.valueOf(rawX - loc[0]), Float.valueOf(rawY - loc[1]));
    }

    private final RectF rectIn(View v) {
        int[] a = new int[2];
        int[] b = new int[2];
        v.getLocationOnScreen(a);
        getLocationOnScreen(b);
        return new RectF(a[0] - b[0], a[1] - b[1], (a[0] - b[0]) + v.getWidth(), (a[1] - b[1]) + v.getHeight());
    }

    private final void toggleFloat(Panel p) {
        try {
        Integer num = null;
        if (p.getZone() == Zone.FLOAT) {
            p.setZone(p.getDefZone() == Zone.FLOAT ? Zone.RIGHT : p.getDefZone());
            ArrayList<Panel> arrayList = this.panels;
            ArrayList arrayList2 = new ArrayList();
            Iterator it = arrayList.iterator();
            while (true) {
                int i = 0;
                if (!it.hasNext()) {
                    break;
                }
                Object next = it.next();
                Panel panel = (Panel) next;
                if (panel.getZone() == p.getZone() && panel != p) {
                    i = 1;
                }
                if (i != 0) {
                    arrayList2.add(next);
                }
            }
            Iterator it2 = arrayList2.iterator();
            if (it2.hasNext()) {
                Integer numValueOf = Integer.valueOf(((Panel) it2.next()).getOrder() + 1);
                loop1: while (true) {
                    num = numValueOf;
                    do {
                        if (!it2.hasNext()) {
                            break loop1;
                        } else {
                            numValueOf = Integer.valueOf(((Panel) it2.next()).getOrder() + 1);
                        }
                    } while (num.compareTo(numValueOf) >= 0);
                }
            }
            Integer num2 = num;
            p.setOrder(num2 != null ? num2.intValue() : 0);
        } else {
            RectF rectFRectIn = p.getView().isShown() ? rectIn(p.getView()) : null;
            p.setZone(Zone.FLOAT);
            p.setFw(RangesKt.coerceIn(rectFRectIn != null ? (int) rectFRectIn.width() : dp(300.0f), dp(220.0f), dp(420.0f)));
            p.setFh(RangesKt.coerceIn(rectFRectIn != null ? (int) rectFRectIn.height() : dp(360.0f), dp(200.0f), dp(480.0f)));
            p.setFx(((int) (rectFRectIn != null ? rectFRectIn.left : 0.0f)) + dp(24.0f));
            p.setFy(((int) (rectFRectIn != null ? rectFRectIn.top : 0.0f)) + dp(24.0f));
        }
        rebuild();
        save();
        Function0<Unit> function0 = this.onChanged;
        if (function0 != null) {
            function0.invoke();
        }
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    /* JADX INFO: compiled from: DockLayout.kt */
    public static final class Target {
        private final int index;
        private final RectF preview;
        private final Zone zone;

        public Target(Zone zone, int index, RectF preview) {
            Intrinsics.checkNotNullParameter(zone, "zone");
            Intrinsics.checkNotNullParameter(preview, "preview");
            this.zone = zone;
            this.index = index;
            this.preview = preview;
        }

        public final int getIndex() {
            return this.index;
        }

        public final RectF getPreview() {
            return this.preview;
        }

        public final Zone getZone() {
            return this.zone;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:75:0x004d A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:77:0x0028 A[SYNTHETIC] */
    public final Target target(Panel p, float x, float y) {
        int iDp;
        int iDp2;
        boolean z = false;
        float edge = this.d * 44.0f;
        float w = getWidth();
        float h = getHeight();
        Iterable iterable = this.panels;
        Collection arrayList = new ArrayList();
        for (Object obj : iterable) {
            Panel panel = (Panel) obj;
            if (panel.getVisible() && !this.allHidden) {
                z = panel != p;
                if (z) {
                    arrayList.add(obj);
                }
            }
            if (z) {
                arrayList.add(obj);
            }
        }
        List vis = (List) arrayList;
        RectF rectFRectIn = null;
        RectF lRect = (this.leftCol.getVisibility() != 0 || this.leftCol.getWidth() <= 0) ? null : rectIn(this.leftCol);
        RectF rRect = (this.rightCol.getVisibility() != 0 || this.rightCol.getWidth() <= 0) ? null : rectIn(this.rightCol);
        if (this.bottomRow.getVisibility() == 0 && this.bottomRow.getHeight() > 0) {
            rectFRectIn = rectIn(this.bottomRow);
        }
        RectF bRect = rectFRectIn;
        RectF emptyL = new RectF(0.0f, 0.0f, RangesKt.coerceAtMost(this.leftW, w * 0.4f), h);
        RectF emptyR = new RectF(w - RangesKt.coerceAtMost(this.rightW, 0.4f * w), 0.0f, w, h);
        float cx0 = lRect != null ? lRect.right : 0.0f;
        float cx1 = rRect != null ? rRect.left : w;
        RectF emptyB = new RectF(cx0, h - RangesKt.coerceAtMost(this.bottomH, h * 0.5f), cx1, h);
        if (x < edge || (lRect != null && lRect.contains(x, y))) {
            return target$colTarget(vis, this, x, y, Zone.LEFT, this.leftCol, lRect, false, emptyL);
        }
        if (x > w - edge || (rRect != null && rRect.contains(x, y))) {
            return target$colTarget(vis, this, x, y, Zone.RIGHT, this.rightCol, rRect, false, emptyR);
        }
        if (y > h - edge || (bRect != null && bRect.contains(x, y))) {
            return target$colTarget(vis, this, x, y, Zone.BOTTOM, this.bottomRow, bRect, true, emptyB);
        }
        if (p.getFw() <= 0) {
            iDp = dp(300.0f);
        } else {
            iDp = p.getFw();
        }
        float fw = iDp;
        if (p.getFh() <= 0) {
            iDp2 = dp(360.0f);
        } else {
            iDp2 = p.getFh();
        }
        float fh = iDp2;
        float cx2 = x - (fw / 2.0f);
        float fh2 = x + (fw / 2.0f);
        return new Target(Zone.FLOAT, 0, new RectF(cx2, y - dp(14.0f), fh2, (y - dp(14.0f)) + fh));
    }

    private static final Target target$colTarget(List<Panel> list, DockLayout this$0, float $x, float $y, Zone zone, LinearLayout col, RectF colRect, boolean horizontal, RectF emptyRect) {
        boolean firstHalf;
        RectF pr;
        Collection arrayList = new ArrayList();
        Iterator it = list.iterator();
        while (true) {
            firstHalf = true;
            if (!it.hasNext()) {
                break;
            }
            Object next = it.next();
            firstHalf = ((Panel) next).getZone() == zone;
            if (firstHalf) {
                arrayList.add(next);
            }
        }
        List<Panel> list2 = CollectionsKt.sortedWith((List) arrayList, new Comparator() { // from class: com.goydevv.studrblx.ui.DockLayout$target$colTarget$$inlined$sortedBy$1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.util.Comparator
            public final int compare(Object t, Object t2) {
                return ComparisonsKt.compareValues(Integer.valueOf(((DockLayout.Panel) t).getOrder()), Integer.valueOf(((DockLayout.Panel) t2).getOrder()));
            }
        });
        if (list2.isEmpty() || colRect == null) {
            return new Target(zone, 0, emptyRect);
        }
        int i = 0;
        for (Panel q : list2) {
            int i2 = i;
            i++;
            RectF r = this$0.rectIn(q.getView());
            boolean inside = !horizontal ? $y < r.top || $y > r.bottom : $x < r.left || $x > r.right;
            if (inside) {
                if (!horizontal ? $y >= r.centerY() : $x >= r.centerX()) {
                    firstHalf = false;
                }
                if (horizontal) {
                    pr = firstHalf ? new RectF(r.left, r.top, r.centerX(), r.bottom) : new RectF(r.centerX(), r.top, r.right, r.bottom);
                } else {
                    float f = r.left;
                    pr = firstHalf ? new RectF(f, r.top, r.right, r.centerY()) : new RectF(f, r.centerY(), r.right, r.bottom);
                }
                return new Target(zone, firstHalf ? i2 : i2 + 1, pr);
            }
        }
        RectF last = this$0.rectIn(((Panel) CollectionsKt.last(list2)).getView());
        return new Target(zone, list2.size(), horizontal ? new RectF(last.centerX(), last.top, last.right, last.bottom) : new RectF(last.left, last.centerY(), last.right, last.bottom));
    }

    private final void drop(Panel p, Target t) {
        try {
        float fAverageOfFloat;
        if (t.getZone() != Zone.FLOAT) {
            Iterable iterable = this.panels;
            Collection arrayList = new ArrayList();
            Iterator it = iterable.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Object next = it.next();
                Panel panel = (Panel) next;
                if (panel.getZone() == t.getZone() && panel.getVisible() && panel != p) {
                    arrayList.add(next);
                }
            }
            List list = CollectionsKt.toMutableList((Collection) CollectionsKt.sortedWith((List) arrayList, new Comparator() { // from class: com.goydevv.studrblx.ui.DockLayout$drop$$inlined$sortedBy$1
                /* JADX WARN: Multi-variable type inference failed */
                @Override // java.util.Comparator
                public final int compare(Object t2, Object t3) {
                    return ComparisonsKt.compareValues(Integer.valueOf(((DockLayout.Panel) t2).getOrder()), Integer.valueOf(((DockLayout.Panel) t3).getOrder()));
                }
            }));
            int idx = RangesKt.coerceIn(t.getIndex(), 0, list.size());
            list.add(idx, p);
            int i = 0;
            for (Object obj : list) {
                int i2 = i + 1;
                if (i < 0) {
                    CollectionsKt.throwIndexOverflow();
                }
                ((Panel) obj).setOrder(i);
                i = i2;
            }
            if (list.size() > 1) {
                Collection arrayList2 = new ArrayList();
                for (Object obj2 : list) {
                    if (((Panel) obj2) != p) {
                        arrayList2.add(obj2);
                    }
                }
                Iterable iterable2 = (List) arrayList2;
                Collection arrayList3 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable2, 10));
                Iterator it2 = iterable2.iterator();
                while (it2.hasNext()) {
                    arrayList3.add(Float.valueOf(((Panel) it2.next()).getWeight()));
                }
                fAverageOfFloat = (float) CollectionsKt.averageOfFloat((List) arrayList3);
            } else {
                fAverageOfFloat = 1.0f;
            }
            p.setWeight(fAverageOfFloat);
            p.setZone(t.getZone());
        } else if (p.getZone() != Zone.FLOAT) {
            p.setZone(Zone.FLOAT);
            p.setFw((int) t.getPreview().width());
            p.setFh((int) t.getPreview().height());
            p.setFx((int) t.getPreview().left);
            p.setFy(RangesKt.coerceAtLeast((int) t.getPreview().top, 0));
        } else {
            FloatFrame frame = p.getFrame();
            ViewGroup.LayoutParams layoutParams = frame != null ? frame.getLayoutParams() : null;
            FrameLayout.LayoutParams lp = layoutParams instanceof FrameLayout.LayoutParams ? (FrameLayout.LayoutParams) layoutParams : null;
            if (lp != null) {
                p.setFx(lp.leftMargin);
                p.setFy(lp.topMargin);
            }
        }
        rebuild();
        save();
        Function0<Unit> function0 = this.onChanged;
        if (function0 != null) {
            function0.invoke();
        }
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    /* JADX INFO: compiled from: DockLayout.kt */
    public final class FloatFrame extends FrameLayout {
        private final View grip;
        private final Panel p;
        final /* synthetic */ DockLayout this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public FloatFrame(DockLayout this$0, Context ctx, Panel p) {
            super(ctx);
            Intrinsics.checkNotNullParameter(ctx, "ctx");
            Intrinsics.checkNotNullParameter(p, "p");
            this.this$0 = this$0;
            this.p = p;
            this.grip = new View(ctx);
            GradientDrawable gradientDrawable = new GradientDrawable();
            DockLayout dockLayout = this.this$0;
            gradientDrawable.setColor(Ui.INSTANCE.getPANEL());
            gradientDrawable.setStroke(dockLayout.dp(1.0f), Ui.INSTANCE.getACCENT());
            setBackground(gradientDrawable);
            setElevation(this.this$0.d * 12.0f);
            setPadding(this.this$0.dp(1.0f), this.this$0.dp(1.0f), this.this$0.dp(1.0f), this.this$0.dp(1.0f));
            this.grip.setBackground(new Drawable() { // from class: com.goydevv.studrblx.ui.DockLayout.FloatFrame.2
                private final Paint pt;

                {
                    Paint paint = new Paint(1);
                    paint.setStrokeWidth(DockLayout.this.d * 1.5f);
                    this.pt = paint;
                }

                @Override // android.graphics.drawable.Drawable
                public void draw(Canvas c) {
                    Intrinsics.checkNotNullParameter(c, "c");
                    Rect b = getBounds();
                    Intrinsics.checkNotNullExpressionValue(b, "getBounds(...)");
                    this.pt.setColor(Ui.INSTANCE.getDIM());
                    for (int i = 1; i < 4; i++) {
                        c.drawLine(b.right - ((i * 5.0f) * DockLayout.this.d), b.bottom - (DockLayout.this.d * 2.0f), b.right - (DockLayout.this.d * 2.0f), b.bottom - ((i * 5.0f) * DockLayout.this.d), this.pt);
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
            });
            final Ref.FloatRef sx = new Ref.FloatRef();
            final Ref.FloatRef sy = new Ref.FloatRef();
            final Ref.IntRef sw = new Ref.IntRef();
            final Ref.IntRef sh = new Ref.IntRef();
            View view = this.grip;
            final DockLayout dockLayout2 = this.this$0;
            view.setOnTouchListener(new View.OnTouchListener() { // from class: com.goydevv.studrblx.ui.DockLayout$FloatFrame$$ExternalSyntheticLambda0
                @Override // android.view.View.OnTouchListener
                public final boolean onTouch(View view2, MotionEvent motionEvent) {
                    return DockLayout.FloatFrame._init_$lambda$1(FloatFrame.this, sx, sy, sw, sh, dockLayout2, view2, motionEvent);
                }
            });
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        static final boolean _init_$lambda$1(FloatFrame this$0, Ref.FloatRef $sx, Ref.FloatRef $sy, Ref.IntRef $sw, Ref.IntRef $sh, DockLayout this$1, View view, MotionEvent e) {
            try {
            ViewGroup.LayoutParams layoutParams = this$0.getLayoutParams();
            Intrinsics.checkNotNull(layoutParams, "null cannot be cast to non-null type android.widget.FrameLayout.LayoutParams");
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) layoutParams;
            switch (e.getActionMasked()) {
                case 0:
                    $sx.element = e.getRawX();
                    $sy.element = e.getRawY();
                    $sw.element = lp.width;
                    $sh.element = lp.height;
                    ViewParent parent = this$0.getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                    return true;
                case 1:
                case 3:
                    this$0.p.setFw(lp.width);
                    this$0.p.setFh(lp.height);
                    this$1.save();
                    return true;
                case 2:
                    lp.width = RangesKt.coerceAtLeast((int) ($sw.element + (e.getRawX() - $sx.element)), this$1.dp(180.0f));
                    lp.height = RangesKt.coerceAtLeast((int) ($sh.element + (e.getRawY() - $sy.element)), this$1.dp(140.0f));
                    this$0.setLayoutParams(lp);
                    return true;
                default:
                    return true;
            }
        }
            catch (Throwable _ktEx) {
                throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
            }
        }

        public final void attach() {
            removeAllViews();
            addView(this.p.getView(), new FrameLayout.LayoutParams(-1, -1));
            addView(this.grip, new FrameLayout.LayoutParams(this.this$0.dp(22.0f), this.this$0.dp(22.0f), 8388693));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: compiled from: DockLayout.kt */
    final class DropOverlay extends View {
        private Target current;
        private Panel dragged;
        private final Paint fill;
        private final Paint line;
        final /* synthetic */ DockLayout this$0;

        /* JADX INFO: compiled from: DockLayout.kt */
        public static final /* synthetic */ class WhenMappings {
            public static final /* synthetic */ int[] $EnumSwitchMapping$0;

            static {
                int[] iArr = new int[Zone.values().length];
                try {
                    iArr[Zone.LEFT.ordinal()] = 1;
                } catch (NoSuchFieldError e) {
                }
                try {
                    iArr[Zone.RIGHT.ordinal()] = 2;
                } catch (NoSuchFieldError e2) {
                }
                try {
                    iArr[Zone.BOTTOM.ordinal()] = 3;
                } catch (NoSuchFieldError e3) {
                }
                try {
                    iArr[Zone.FLOAT.ordinal()] = 4;
                } catch (NoSuchFieldError e4) {
                }
                $EnumSwitchMapping$0 = iArr;
            }
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public DropOverlay(DockLayout this$0, Context ctx) {
            super(ctx);
            Intrinsics.checkNotNullParameter(ctx, "ctx");
            this.this$0 = this$0;
            this.fill = new Paint(1);
            Paint paint = new Paint(1);
            paint.setStyle(Paint.Style.STROKE);
            this.line = paint;
        }

        public final void begin(Panel p) {
            Intrinsics.checkNotNullParameter(p, "p");
            this.dragged = p;
            setVisibility(0);
            invalidate();
        }

        public final Target update(Panel p, float x, float y) {
            Intrinsics.checkNotNullParameter(p, "p");
            Target t = this.this$0.target(p, x, y);
            this.current = t;
            invalidate();
            return t;
        }

        public final void end() {
            setVisibility(8);
            this.current = null;
            this.dragged = null;
        }

        @Override // android.view.View
        protected void onDraw(Canvas c) {
            String str;
            Intrinsics.checkNotNullParameter(c, "c");
            float w = getWidth();
            float h = getHeight();
            float e = this.this$0.d * 44.0f;
            this.fill.setColor(574327787);
            c.drawRect(0.0f, 0.0f, e, h, this.fill);
            c.drawRect(w - e, 0.0f, w, h, this.fill);
            c.drawRect(e, h - e, w - e, h, this.fill);
            Target t = this.current;
            if (t == null) {
                return;
            }
            this.fill.setColor(1429965803);
            this.line.setColor(IconPainter.BLUE);
            this.line.setStrokeWidth(this.this$0.d * 2.0f);
            c.drawRect(t.getPreview(), this.fill);
            c.drawRect(t.getPreview(), this.line);
            switch (WhenMappings.$EnumSwitchMapping$0[t.getZone().ordinal()]) {
                case 1:
                    str = "Dock left";
                    break;
                case 2:
                    str = "Dock right";
                    break;
                case 3:
                    str = "Dock bottom";
                    break;
                case 4:
                    str = "Float";
                    break;
                default:
                    throw new NoWhenBranchMatchedException();
            }
            String label = str;
            this.fill.setColor(-1);
            this.fill.setTextSize(this.this$0.d * 13.0f);
            this.fill.setTextAlign(Paint.Align.CENTER);
            this.fill.setFakeBoldText(true);
            Rect r = new Rect();
            this.fill.getTextBounds(label, 0, label.length(), r);
            float cx = t.getPreview().centerX();
            float cy = t.getPreview().centerY();
            Paint bg = new Paint();
            bg.setColor(-870359120);
            c.drawRoundRect((cx - (r.width() / 2.0f)) - (this.this$0.d * 10.0f), cy - (this.this$0.d * 14.0f), (this.this$0.d * 10.0f) + (r.width() / 2.0f) + cx, cy + (this.this$0.d * 10.0f), this.this$0.d * 6.0f, this.this$0.d * 6.0f, bg);
            c.drawText(label, cx, cy + (this.this$0.d * 2.0f), this.fill);
            this.fill.setFakeBoldText(false);
        }
    }
}

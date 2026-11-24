package com.goydevv.studrblx.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.goydevv.studrblx.audio.AudioEngine;
import com.goydevv.studrblx.net.RobloxApi;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: AudioBrowser.kt */
/* JADX INFO: loaded from: classes5.dex */
public final class AudioBrowser extends LinearLayout {
    private final Adapter adapter;
    private String chart;
    private final FrameLayout content;
    private boolean discover;
    private Long draggingId;
    private boolean ended;
    private final LinearLayout filterBar;
    private int gen;
    private final List<String> genres;
    private final ArrayList<RobloxApi.AudioItem> items;
    private String keyword;
    private int lengthIdx;
    private final List<Pair<String, Pair<Integer, Integer>>> lengths;
    private boolean loading;
    private final Function1<Function0<Unit>, Unit> needSignIn;
    private final ExecutorService net;
    private final Function1<RobloxApi.AudioItem, Unit> onInsert;
    private final Function0<Unit> previewListener;
    private ProgressBar progress;
    private View results;
    private final List<String> sfx;
    private String sort;
    private final List<Pair<String, String>> sorts;
    private TextView status;
    private String tag;
    private String token;
    private String type;
    private boolean useLegacy;
    private final List<String> vibes;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    public AudioBrowser(Context ctx, Function1<? super RobloxApi.AudioItem, Unit> onInsert, Function1<? super Function0<Unit>, Unit> needSignIn) {
        super(ctx);
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(onInsert, "onInsert");
        Intrinsics.checkNotNullParameter(needSignIn, "needSignIn");
        this.onInsert = (Function1<RobloxApi.AudioItem, Unit>) onInsert;
        this.needSignIn = (Function1<Function0<Unit>, Unit>) needSignIn;
        this.net = Executors.newSingleThreadExecutor();
        this.content = new FrameLayout(ctx);
        LinearLayout linearLayout = new LinearLayout(ctx);
        linearLayout.setOrientation(0);
        linearLayout.setGravity(16);
        this.filterBar = linearLayout;
        this.items = new ArrayList<>();
        this.adapter = new Adapter();
        this.keyword = "";
        this.sort = "Relevance";
        this.chart = "None";
        this.discover = true;
        this.sorts = CollectionsKt.listOf(new Pair[]{TuplesKt.to("Relevance", "Relevance"), TuplesKt.to("Trending", "Trending"), TuplesKt.to("Top", "Top rated"), TuplesKt.to("UpdatedTime", "Most recent"), TuplesKt.to("AudioDuration", "Length")});
        this.lengths = CollectionsKt.listOf(new Pair[]{TuplesKt.to("Any length", TuplesKt.to(0, null)), TuplesKt.to("Under 10s", TuplesKt.to(0, 10)), TuplesKt.to("10s – 1 min", TuplesKt.to(10, 60)), TuplesKt.to("1 – 3 min", TuplesKt.to(60, 180)), TuplesKt.to("Over 3 min", TuplesKt.to(180, null))});
        this.genres = CollectionsKt.listOf(new String[]{"Ambient", "Classical", "Country", "Electronic", "Funk", "Hip Hop", "Jazz", "Latin", "Lo-fi", "Metal", "Orchestral", "Pop", "R&B", "Reggae", "Rock", "Synthwave", "World"});
        this.vibes = CollectionsKt.listOf(new String[]{"Calm", "Chill", "Dark", "Epic", "Funny", "Happy", "Intense", "Mysterious", "Romantic", "Sad", "Scary", "Upbeat"});
        this.sfx = CollectionsKt.listOf(new String[]{"UI", "Footsteps", "Explosions", "Weapons", "Impacts", "Whooshes", "Magic", "Nature", "Ambience", "Vehicles", "Animals", "Voices", "Sci-fi", "Horror", "Cartoon", "Water", "Fire", "Doors", "Coins", "Alarms"});
        this.previewListener = new Function0() { // from class: com.goydevv.studrblx.ui.AudioBrowser$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return AudioBrowser.previewListener$lambda$2(AudioBrowser.this);
            }
        };
        setOrientation(1);
        HorizontalScrollView horizontalScrollView = new HorizontalScrollView(ctx);
        horizontalScrollView.setHorizontalScrollBarEnabled(false);
        horizontalScrollView.addView(this.filterBar);
        addView(horizontalScrollView, Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null));
        addView(this.content, Ui.INSTANCE.lp(-1, 0, 1.0f));
        showDiscover();
    }

    public final String getKeyword() {
        return this.keyword;
    }

    static final Unit previewListener$lambda$2(final AudioBrowser this$0) {
        this$0.post(new Runnable() { // from class: com.goydevv.studrblx.ui.AudioBrowser$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                AudioBrowser.previewListener$lambda$2$lambda$1(this$0);
            }
        });
        return Unit.INSTANCE;
    }

    static final void previewListener$lambda$2$lambda$1(AudioBrowser this$0) {
        if (this$0.draggingId == null) {
            this$0.adapter.notifyDataSetChanged();
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        AudioEngine.INSTANCE.getPreviewListeners().add(this.previewListener);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        AudioEngine.INSTANCE.getPreviewListeners().remove(this.previewListener);
        super.onDetachedFromWindow();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final int dp(float v) {
        Ui ui = Ui.INSTANCE;
        Context context = getContext();
        Intrinsics.checkNotNullExpressionValue(context, "getContext(...)");
        return ui.dp(context, v);
    }

    static /* synthetic */ TextView chip$default(AudioBrowser audioBrowser, String str, boolean z, boolean z2, Function1 function1, int i, Object obj) {
        if ((i & 4) != 0) {
            z2 = false;
        }
        return audioBrowser.chip(str, z, z2, function1);
    }

    private final TextView chip(String label, boolean on, boolean dropdown, final Function1<? super View, Unit> onClick) {
        int iBlend;
        Ui ui = Ui.INSTANCE;
        Context context = getContext();
        Intrinsics.checkNotNullExpressionValue(context, "getContext(...)");
        TextView textViewText$default = Ui.text$default(ui, context, label + (dropdown ? "  ▾" : ""), 12.0f, on ? -1 : Ui.INSTANCE.getTEXT(), false, 16, null);
        textViewText$default.setPadding(dp(11.0f), dp(5.0f), dp(11.0f), dp(5.0f));
        GradientDrawable gradientDrawable = new GradientDrawable();
        Ui ui2 = Ui.INSTANCE;
        if (on) {
            iBlend = ui2.getACCENT();
        } else {
            iBlend = ui2.blend(Ui.INSTANCE.getPANEL(), Ui.INSTANCE.getLIGHT() ? -16777216 : -1, 0.08f);
        }
        gradientDrawable.setColor(iBlend);
        gradientDrawable.setCornerRadius(dp(14.0f));
        if (!on) {
            gradientDrawable.setStroke(dp(1.0f), Ui.INSTANCE.blend(Ui.INSTANCE.getPANEL(), Ui.INSTANCE.getLIGHT() ? -16777216 : -1, 0.18f));
        }
        textViewText$default.setBackground(gradientDrawable);
        textViewText$default.setOnClickListener(new View.OnClickListener() { // from class: com.goydevv.studrblx.ui.AudioBrowser$$ExternalSyntheticLambda14
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                onClick.invoke(view);
            }
        });
        return textViewText$default;
    }

    private final void buildFilters() {
        this.filterBar.removeAllViews();
        this.filterBar.setPadding(dp(6.0f), dp(4.0f), dp(6.0f), dp(4.0f));
        buildFilters$add(this, chip$default(this, "Discover", this.discover, false, new Function1() { // from class: com.goydevv.studrblx.ui.AudioBrowser$$ExternalSyntheticLambda6
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return AudioBrowser.buildFilters$lambda$8(AudioBrowser.this, (View) obj);
            }
        }, 4, null));
        String str = this.type;
        buildFilters$add(this, chip(Intrinsics.areEqual(str, "Music") ? "Music" : Intrinsics.areEqual(str, "SoundEffect") ? "Sound Effects" : "Type", this.type != null, true, new Function1() { // from class: com.goydevv.studrblx.ui.AudioBrowser$$ExternalSyntheticLambda7
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return AudioBrowser.buildFilters$lambda$10(AudioBrowser.this, (View) obj);
            }
        }));
        for (Object obj : this.sorts) {
            if (Intrinsics.areEqual(((Pair) obj).getFirst(), this.sort)) {
                buildFilters$add(this, chip("Sort: " + ((Pair) obj).getSecond(), !Intrinsics.areEqual(this.sort, "Relevance"), true, new Function1() { // from class: com.goydevv.studrblx.ui.AudioBrowser$$ExternalSyntheticLambda8
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj2) {
                        return AudioBrowser.buildFilters$lambda$14(AudioBrowser.this, (View) obj2);
                    }
                }));
                buildFilters$add(this, chip(this.lengths.get(this.lengthIdx).getFirst(), this.lengthIdx != 0, true, new Function1() { // from class: com.goydevv.studrblx.ui.AudioBrowser$$ExternalSyntheticLambda9
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj2) {
                        return AudioBrowser.buildFilters$lambda$17(AudioBrowser.this, (View) obj2);
                    }
                }));
                String str2 = this.tag;
                if (str2 != null) {
                    buildFilters$add(this, chip$default(this, str2 + "  ✕", true, false, new Function1() { // from class: com.goydevv.studrblx.ui.AudioBrowser$$ExternalSyntheticLambda10
                        @Override // kotlin.jvm.functions.Function1
                        public final Object invoke(Object obj2) {
                            return AudioBrowser.buildFilters$lambda$19$lambda$18(AudioBrowser.this, (View) obj2);
                        }
                    }, 4, null));
                }
                if (!Intrinsics.areEqual(this.chart, "None")) {
                    buildFilters$add(this, chip$default(this, "Top charts: " + chartLabel(this.chart) + "  ✕", true, false, new Function1() { // from class: com.goydevv.studrblx.ui.AudioBrowser$$ExternalSyntheticLambda11
                        @Override // kotlin.jvm.functions.Function1
                        public final Object invoke(Object obj2) {
                            return AudioBrowser.buildFilters$lambda$20(AudioBrowser.this, (View) obj2);
                        }
                    }, 4, null));
                    return;
                }
                return;
            }
        }
        throw new NoSuchElementException("Collection contains no element matching the predicate.");
    }

    private static final void buildFilters$add(AudioBrowser this$0, View v) {
        LinearLayout linearLayout = this$0.filterBar;
        LinearLayout.LayoutParams layoutParamsLp$default = Ui.lp$default(Ui.INSTANCE, -2, -2, 0.0f, 4, null);
        layoutParamsLp$default.rightMargin = this$0.dp(6.0f);
        Unit unit = Unit.INSTANCE;
        linearLayout.addView(v, layoutParamsLp$default);
    }

    static final Unit buildFilters$lambda$8(AudioBrowser this$0, View it) {
        Intrinsics.checkNotNullParameter(it, "it");
        this$0.tag = null;
        this$0.chart = "None";
        this$0.keyword = "";
        this$0.showDiscover();
        return Unit.INSTANCE;
    }

    static final Unit buildFilters$lambda$10(final AudioBrowser this$0, View v) {
        Intrinsics.checkNotNullParameter(v, "v");
        this$0.menu(v, CollectionsKt.listOf(new String[]{"All audio", "Music", "Sound Effects"}), new Function1() { // from class: com.goydevv.studrblx.ui.AudioBrowser$$ExternalSyntheticLambda13
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return AudioBrowser.buildFilters$lambda$10$lambda$9(this$0, ((Integer) obj).intValue());
            }
        });
        return Unit.INSTANCE;
    }

    static final Unit buildFilters$lambda$10$lambda$9(AudioBrowser this$0, int i) {
        String str;
        switch (i) {
            case 1:
                str = "Music";
                break;
            case 2:
                str = "SoundEffect";
                break;
            default:
                str = null;
                break;
        }
        this$0.type = str;
        this$0.showResults();
        return Unit.INSTANCE;
    }

    static final Unit buildFilters$lambda$14(final AudioBrowser this$0, View v) {
        Intrinsics.checkNotNullParameter(v, "v");
        Iterable iterable = this$0.sorts;
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
        Iterator it = iterable.iterator();
        while (it.hasNext()) {
            arrayList.add((String) ((Pair) it.next()).getSecond());
        }
        this$0.menu(v, (List) arrayList, new Function1() { // from class: com.goydevv.studrblx.ui.AudioBrowser$$ExternalSyntheticLambda22
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return AudioBrowser.buildFilters$lambda$14$lambda$13(this$0, ((Integer) obj).intValue());
            }
        });
        return Unit.INSTANCE;
    }

    static final Unit buildFilters$lambda$14$lambda$13(AudioBrowser this$0, int i) {
        this$0.sort = this$0.sorts.get(i).getFirst();
        this$0.showResults();
        return Unit.INSTANCE;
    }

    static final Unit buildFilters$lambda$17(final AudioBrowser this$0, View v) {
        Intrinsics.checkNotNullParameter(v, "v");
        Iterable iterable = this$0.lengths;
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
        Iterator it = iterable.iterator();
        while (it.hasNext()) {
            arrayList.add((String) ((Pair) it.next()).getFirst());
        }
        this$0.menu(v, (List) arrayList, new Function1() { // from class: com.goydevv.studrblx.ui.AudioBrowser$$ExternalSyntheticLambda12
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return AudioBrowser.buildFilters$lambda$17$lambda$16(this$0, ((Integer) obj).intValue());
            }
        });
        return Unit.INSTANCE;
    }

    static final Unit buildFilters$lambda$17$lambda$16(AudioBrowser this$0, int i) {
        this$0.lengthIdx = i;
        this$0.showResults();
        return Unit.INSTANCE;
    }

    static final Unit buildFilters$lambda$19$lambda$18(AudioBrowser this$0, View it) {
        Intrinsics.checkNotNullParameter(it, "it");
        this$0.tag = null;
        if (StringsKt.isBlank(this$0.keyword) && Intrinsics.areEqual(this$0.chart, "None")) {
            this$0.showDiscover();
        } else {
            this$0.showResults();
        }
        return Unit.INSTANCE;
    }

    static final Unit buildFilters$lambda$20(AudioBrowser this$0, View it) {
        Intrinsics.checkNotNullParameter(it, "it");
        this$0.chart = "None";
        if (StringsKt.isBlank(this$0.keyword) && this$0.tag == null) {
            this$0.showDiscover();
        } else {
            this$0.showResults();
        }
        return Unit.INSTANCE;
    }

    private final String chartLabel(String c) {
        switch (c.hashCode()) {
            case -1503373991:
                return !c.equals("Current") ? c : "Now";
            case 2692116:
                return c.equals("Week") ? "This week" : c;
            case 2751581:
                return c.equals("Year") ? "This year" : c;
            case 74527328:
                return c.equals("Month") ? "This month" : c;
            default:
                return c;
        }
    }

    private final void menu(View anchor, List<String> labels, final Function1<? super Integer, Unit> pick) {
        Ui ui = Ui.INSTANCE;
        Context context = getContext();
        Intrinsics.checkNotNullExpressionValue(context, "getContext(...)");
        PopupMenu m = new PopupMenu(ui.popupContext(context), anchor);
        int i = 0;
        for (Object obj : labels) {
            int i2 = i + 1;
            if (i < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            m.getMenu().add(0, i, i, (String) obj);
            i = i2;
        }
        m.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() { // from class: com.goydevv.studrblx.ui.AudioBrowser$$ExternalSyntheticLambda21
            @Override // android.widget.PopupMenu.OnMenuItemClickListener
            public final boolean onMenuItemClick(MenuItem menuItem) {
                return AudioBrowser.menu$lambda$22(pick, menuItem);
            }
        });
        m.show();
    }

    static final boolean menu$lambda$22(Function1 $pick, MenuItem it) {
        $pick.invoke(Integer.valueOf(it.getItemId()));
        return true;
    }

    public final void search(String q) {
        Intrinsics.checkNotNullParameter(q, "q");
        this.keyword = StringsKt.trim((CharSequence) q).toString();
        if (!(this.keyword.length() == 0) || this.tag != null || !Intrinsics.areEqual(this.chart, "None") || this.type != null) {
            showResults();
        } else {
            showDiscover();
        }
    }

    private final void showDiscover() {
        this.discover = true;
        buildFilters();
        this.content.removeAllViews();
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(1);
        linearLayout.setPadding(dp(10.0f), dp(4.0f), dp(10.0f), dp(16.0f));
        LinearLayout cards = new LinearLayout(getContext());
        cards.setOrientation(0);
        showDiscover$card(this, cards, "Music", "Licensed tracks for your experience", -8699932, new Function0() { // from class: com.goydevv.studrblx.ui.AudioBrowser$$ExternalSyntheticLambda15
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return AudioBrowser.showDiscover$lambda$32(AudioBrowser.this);
            }
        });
        showDiscover$card(this, cards, "Sound Effects", "UI, impacts, ambience and more", -15954066, new Function0() { // from class: com.goydevv.studrblx.ui.AudioBrowser$$ExternalSyntheticLambda16
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return AudioBrowser.showDiscover$lambda$33(AudioBrowser.this);
            }
        });
        LinearLayout.LayoutParams layoutParamsLp$default = Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null);
        layoutParamsLp$default.topMargin = dp(6.0f);
        Unit unit = Unit.INSTANCE;
        linearLayout.addView(cards, layoutParamsLp$default);
        showDiscover$section(linearLayout, this, "Top charts");
        showDiscover$flow(this, linearLayout, CollectionsKt.listOf(new String[]{"Now", "This week", "This month", "This year"}), new Function1() { // from class: com.goydevv.studrblx.ui.AudioBrowser$$ExternalSyntheticLambda17
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return AudioBrowser.showDiscover$lambda$35(AudioBrowser.this, (String) obj);
            }
        });
        showDiscover$section(linearLayout, this, "Genres");
        showDiscover$flow(this, linearLayout, this.genres, new Function1() { // from class: com.goydevv.studrblx.ui.AudioBrowser$$ExternalSyntheticLambda18
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return AudioBrowser.showDiscover$lambda$36(AudioBrowser.this, (String) obj);
            }
        });
        showDiscover$section(linearLayout, this, "Vibes");
        showDiscover$flow(this, linearLayout, this.vibes, new Function1() { // from class: com.goydevv.studrblx.ui.AudioBrowser$$ExternalSyntheticLambda19
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return AudioBrowser.showDiscover$lambda$37(AudioBrowser.this, (String) obj);
            }
        });
        showDiscover$section(linearLayout, this, "Sound effects");
        showDiscover$flow(this, linearLayout, this.sfx, new Function1() { // from class: com.goydevv.studrblx.ui.AudioBrowser$$ExternalSyntheticLambda20
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return AudioBrowser.showDiscover$lambda$38(AudioBrowser.this, (String) obj);
            }
        });
        Ui ui = Ui.INSTANCE;
        Context context = getContext();
        Intrinsics.checkNotNullExpressionValue(context, "getContext(...)");
        TextView textViewText$default = Ui.text$default(ui, context, "Audio comes from the Roblox Creator Store. Previewing and playing audio needs a signed-in account (Roblox only serves audio files to signed-in users).", 11.0f, Ui.INSTANCE.getDIM(), false, 16, null);
        textViewText$default.setSingleLine(false);
        textViewText$default.setPadding(0, dp(14.0f), 0, 0);
        linearLayout.addView(textViewText$default);
        FrameLayout frameLayout = this.content;
        ScrollView scrollView = new ScrollView(getContext());
        scrollView.addView(linearLayout);
        frameLayout.addView(scrollView);
    }

    private static final void showDiscover$section(LinearLayout box, AudioBrowser this$0, String title) {
        Ui ui = Ui.INSTANCE;
        Context context = this$0.getContext();
        Intrinsics.checkNotNullExpressionValue(context, "getContext(...)");
        TextView textViewText = ui.text(context, title, 13.0f, Ui.INSTANCE.getTEXT(), true);
        textViewText.setPadding(0, this$0.dp(12.0f), 0, this$0.dp(6.0f));
        box.addView(textViewText);
    }

    private static final void showDiscover$flow(AudioBrowser this$0, LinearLayout box, List<String> list, final Function1<? super String, Unit> function1) {
        Context context = this$0.getContext();
        Intrinsics.checkNotNullExpressionValue(context, "getContext(...)");
        FlowLayout f = new FlowLayout(context);
        for (final String l : list) {
            AudioBrowser this$1 = this$0;
            f.addView(chip$default(this$1, l, false, false, new Function1() { // from class: com.goydevv.studrblx.ui.AudioBrowser$$ExternalSyntheticLambda1
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return AudioBrowser.showDiscover$flow$lambda$25(function1, l, (View) obj);
                }
            }, 4, null));
            this$0 = this$1;
        }
        box.addView(f, Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null));
    }

    static final Unit showDiscover$flow$lambda$25(Function1 $onPick, String $l, View it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $onPick.invoke($l);
        return Unit.INSTANCE;
    }

    private static final void showDiscover$card(AudioBrowser this$0, LinearLayout cards, String label, String sub, int color, final Function0<Unit> function0) {
        LinearLayout c = new LinearLayout(this$0.getContext());
        c.setOrientation(1);
        c.setPadding(this$0.dp(12.0f), this$0.dp(12.0f), this$0.dp(12.0f), this$0.dp(12.0f));
        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TL_BR, new int[]{color, Ui.INSTANCE.blend(color, ViewCompat.MEASURED_STATE_MASK, 0.35f)});
        gradientDrawable.setCornerRadius(this$0.dp(8.0f));
        c.setBackground(gradientDrawable);
        c.setOnClickListener(new View.OnClickListener() { // from class: com.goydevv.studrblx.ui.AudioBrowser$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                function0.invoke();
            }
        });
        Ui ui = Ui.INSTANCE;
        Context context = this$0.getContext();
        Intrinsics.checkNotNullExpressionValue(context, "getContext(...)");
        c.addView(ui.text(context, label, 15.0f, -1, true));
        Ui ui2 = Ui.INSTANCE;
        Context context2 = this$0.getContext();
        Intrinsics.checkNotNullExpressionValue(context2, "getContext(...)");
        TextView textViewText$default = Ui.text$default(ui2, context2, sub, 11.0f, -570425345, false, 16, null);
        textViewText$default.setSingleLine(false);
        c.addView(textViewText$default);
        LinearLayout.LayoutParams layoutParamsLp = Ui.INSTANCE.lp(0, -2, 1.0f);
        layoutParamsLp.rightMargin = this$0.dp(8.0f);
        Unit unit = Unit.INSTANCE;
        cards.addView(c, layoutParamsLp);
    }

    static final Unit showDiscover$lambda$32(AudioBrowser this$0) {
        this$0.type = "Music";
        this$0.tag = null;
        this$0.showResults();
        return Unit.INSTANCE;
    }

    static final Unit showDiscover$lambda$33(AudioBrowser this$0) {
        this$0.type = "SoundEffect";
        this$0.tag = null;
        this$0.showResults();
        return Unit.INSTANCE;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code duplicated, block: B:15:0x002f  */
    static final Unit showDiscover$lambda$35(AudioBrowser this$0, String l) {
        String str;
        Intrinsics.checkNotNullParameter(l, "l");
        switch (l) {
            case "This month":
                str = "Month";
                break;
            case "Now":
                str = "Current";
                break;
            case "This week":
                str = "Week";
                break;
            default:
                str = "Year";
                break;
        }
        this$0.chart = str;
        this$0.type = "Music";
        this$0.tag = null;
        this$0.showResults();
        return Unit.INSTANCE;
    }

    static final Unit showDiscover$lambda$36(AudioBrowser this$0, String g) {
        Intrinsics.checkNotNullParameter(g, "g");
        this$0.tag = g;
        this$0.type = "Music";
        this$0.showResults();
        return Unit.INSTANCE;
    }

    static final Unit showDiscover$lambda$37(AudioBrowser this$0, String v) {
        Intrinsics.checkNotNullParameter(v, "v");
        this$0.tag = v;
        this$0.type = "Music";
        this$0.showResults();
        return Unit.INSTANCE;
    }

    static final Unit showDiscover$lambda$38(AudioBrowser this$0, String s) {
        Intrinsics.checkNotNullParameter(s, "s");
        this$0.tag = s;
        this$0.type = "SoundEffect";
        this$0.showResults();
        return Unit.INSTANCE;
    }

    private final void showResults() {
        this.discover = false;
        buildFilters();
        this.content.removeAllViews();
        View v = this.results;
        if (v == null) {
            v = buildResults();
            this.results = v;
        }
        ViewParent parent = v.getParent();
        ViewGroup viewGroup = parent instanceof ViewGroup ? (ViewGroup) parent : null;
        if (viewGroup != null) {
            viewGroup.removeView(v);
        }
        this.content.addView(v);
        this.gen++;
        this.items.clear();
        this.token = null;
        this.ended = false;
        this.loading = false;
        this.adapter.notifyDataSetChanged();
        loadMore();
    }

    private final View buildResults() {
        FrameLayout host = new FrameLayout(getContext());
        RecyclerView rv = new RecyclerView(getContext());
        final LinearLayoutManager lm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(lm);
        rv.setAdapter(this.adapter);
        TextView textView = null;
        rv.setItemAnimator(null);
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() { // from class: com.goydevv.studrblx.ui.AudioBrowser.buildResults.1
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrolled(RecyclerView r, int dx, int dy) {
                Intrinsics.checkNotNullParameter(r, "r");
                if (lm.findLastVisibleItemPosition() >= AudioBrowser.this.items.size() - 5) {
                    AudioBrowser.this.loadMore();
                }
            }
        });
        host.addView(rv, new FrameLayout.LayoutParams(-1, -1));
        ProgressBar progressBar = new ProgressBar(getContext());
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(8);
        this.progress = progressBar;
        ProgressBar progressBar2 = this.progress;
        if (progressBar2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("progress");
            progressBar2 = null;
        }
        host.addView(progressBar2, new FrameLayout.LayoutParams(dp(34.0f), dp(34.0f), 17));
        Ui ui = Ui.INSTANCE;
        Context context = getContext();
        Intrinsics.checkNotNullExpressionValue(context, "getContext(...)");
        TextView textViewText$default = Ui.text$default(ui, context, "", 12.0f, Ui.INSTANCE.getDIM(), false, 16, null);
        textViewText$default.setSingleLine(false);
        textViewText$default.setGravity(17);
        textViewText$default.setPadding(dp(16.0f), 0, dp(16.0f), 0);
        this.status = textViewText$default;
        TextView textView2 = this.status;
        if (textView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("status");
        } else {
            textView = textView2;
        }
        host.addView(textView, new FrameLayout.LayoutParams(-1, -2, 17));
        return host;
    }

    private final RobloxApi.AudioQuery query() {
        Pair<Integer, Integer> second = this.lengths.get(this.lengthIdx).getSecond();
        int mn = second.component1().intValue();
        Integer mx = second.component2();
        String[] strArr = new String[2];
        String str = this.keyword;
        if (StringsKt.isBlank(str)) {
            str = null;
        }
        strArr[0] = str;
        strArr[1] = this.tag;
        String words = CollectionsKt.joinToString(CollectionsKt.listOfNotNull(strArr), " ", "", "", -1, "...", null);
        return new RobloxApi.AudioQuery(words, this.type, this.sort, mn, mx, this.chart, null, 64, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void loadMore() {
        if (this.loading || this.ended) {
            return;
        }
        this.loading = true;
        final int g = this.gen;
        final RobloxApi.AudioQuery q = query();
        final String tok = this.token;
        ProgressBar progressBar = this.progress;
        TextView textView = null;
        if (progressBar == null) {
            Intrinsics.throwUninitializedPropertyAccessException("progress");
            progressBar = null;
        }
        progressBar.setVisibility(this.items.isEmpty() ? 0 : 8);
        TextView textView2 = this.status;
        if (textView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("status");
        } else {
            textView = textView2;
        }
        textView.setText("");
        this.net.execute(new Runnable() { // from class: com.goydevv.studrblx.ui.AudioBrowser$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                AudioBrowser.loadMore$lambda$46(AudioBrowser.this, q, tok, g);
            }
        });
    }

    /* JADX WARN: Code duplicated, block: B:31:0x0041 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r12v3, types: [T, com.goydevv.studrblx.net.RobloxApi$AudioPage] */
    static final void loadMore$lambda$46(final AudioBrowser audioBrowser, RobloxApi.AudioQuery audioQuery, String str, final int i) {
        RobloxApi.AudioQuery audioQuery2;
        String str2;
        Object tSearchAudioLegacy = null;
        final Ref.ObjectRef objectRef = new Ref.ObjectRef();
        final Ref.ObjectRef objectRef2 = new Ref.ObjectRef();
        try {
            if (!audioBrowser.useLegacy) {
                audioQuery2 = audioQuery;
                str2 = str;
                try {
                    tSearchAudioLegacy = RobloxApi.searchAudio$default(RobloxApi.INSTANCE, audioBrowser.getContext(), audioQuery2, str2, 0, 8, null);
                } catch (Exception e) {
                    objectRef2.element = e;
                    if (!audioBrowser.useLegacy) {
                        objectRef.element = RobloxApi.INSTANCE.searchAudioLegacy(audioQuery2.getKeyword(), str2);
                        audioBrowser.useLegacy = true;
                    }
                }
            } else {
                try {
                    audioQuery2 = audioQuery;
                    str2 = str;
                    tSearchAudioLegacy = RobloxApi.INSTANCE.searchAudioLegacy(audioQuery.getKeyword(), str);
                } catch (Exception e2) {
                    audioQuery2 = audioQuery;
                    str2 = str;
                    objectRef2.element = e2;
                    if (!audioBrowser.useLegacy) {
                        try {
                            objectRef.element = RobloxApi.INSTANCE.searchAudioLegacy(audioQuery2.getKeyword(), str2);
                            audioBrowser.useLegacy = true;
                        } catch (Exception e3) {
                        }
                    }
                }
            }
            objectRef.element = tSearchAudioLegacy;
        } catch (Exception e4) {
            audioQuery2 = audioQuery;
            str2 = str;
        }
        audioBrowser.post(new Runnable() { // from class: com.goydevv.studrblx.ui.AudioBrowser$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                AudioBrowser.loadMore$lambda$46$lambda$45(i, audioBrowser, objectRef, objectRef2);
            }
        });
    }

    /* JADX WARN: Multi-variable type inference failed */
    static final void loadMore$lambda$46$lambda$45(int $g, AudioBrowser this$0, Ref.ObjectRef $result, Ref.ObjectRef $err) {
        String message;
        if ($g != this$0.gen) {
            return;
        }
        String str = "";
        TextView textView = null;
        if ($result.element == null) {
            this$0.loading = false;
            ProgressBar progressBar = this$0.progress;
            if (progressBar == null) {
                Intrinsics.throwUninitializedPropertyAccessException("progress");
                progressBar = null;
            }
            progressBar.setVisibility(8);
            if (this$0.items.isEmpty()) {
                TextView textView2 = this$0.status;
                if (textView2 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("status");
                } else {
                    textView = textView2;
                }
                Exception exc = (Exception) $err.element;
                if (exc != null && (message = exc.getMessage()) != null) {
                    str = message;
                }
                textView.setText("Couldn't reach the Creator Store.\n" + str);
                return;
            }
            return;
        }
        Object t = $result.element;
        Intrinsics.checkNotNull(t);
        RobloxApi.AudioPage result = (RobloxApi.AudioPage) t;
        int start = this$0.items.size();
        this$0.items.addAll(result.getItems());
        this$0.adapter.notifyItemRangeInserted(start, result.getItems().size());
        this$0.token = result.getNextToken();
        this$0.ended = result.getNextToken() == null || result.getItems().isEmpty();
        this$0.loading = false;
        ProgressBar progressBar2 = this$0.progress;
        if (progressBar2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("progress");
            progressBar2 = null;
        }
        progressBar2.setVisibility(8);
        if (this$0.items.isEmpty()) {
            TextView textView3 = this$0.status;
            if (textView3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("status");
            } else {
                textView = textView3;
            }
            textView.setText("No audio found." + ((this$0.lengthIdx == 0 && this$0.type == null) ? "" : "\nTry removing a filter."));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final String fmtTime(int sec) {
        if (sec <= 0) {
            return "";
        }
        String str = String.format("%d:%02d", Arrays.copyOf(new Object[]{Integer.valueOf(sec / 60), Integer.valueOf(sec % 60)}, 2));
        Intrinsics.checkNotNullExpressionValue(str, "format(...)");
        return str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final String fmtClock(float sec) {
        int s = RangesKt.coerceAtLeast((int) sec, 0);
        String str = String.format("%d:%02d", Arrays.copyOf(new Object[]{Integer.valueOf(s / 60), Integer.valueOf(s % 60)}, 2));
        Intrinsics.checkNotNullExpressionValue(str, "format(...)");
        return str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: compiled from: AudioBrowser.kt */
    final class VH extends RecyclerView.ViewHolder {
        private final TextView dur;
        private final TextView elapsed;
        private final PlayGlyph glyph;
        private final TextView insert;
        private final ImageView play;
        private final LinearLayout row;
        private final SeekBar seek;
        private final LinearLayout seekRow;
        private final TextView sub;
        final /* synthetic */ AudioBrowser this$0;
        private final TextView title;
        private final TextView total;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public VH(AudioBrowser this$0, LinearLayout row, ImageView play, PlayGlyph glyph, TextView title, TextView sub, TextView dur, LinearLayout seekRow, SeekBar seek, TextView elapsed, TextView total, TextView insert) {
            super(row);
            Intrinsics.checkNotNullParameter(row, "row");
            Intrinsics.checkNotNullParameter(play, "play");
            Intrinsics.checkNotNullParameter(glyph, "glyph");
            Intrinsics.checkNotNullParameter(title, "title");
            Intrinsics.checkNotNullParameter(sub, "sub");
            Intrinsics.checkNotNullParameter(dur, "dur");
            Intrinsics.checkNotNullParameter(seekRow, "seekRow");
            Intrinsics.checkNotNullParameter(seek, "seek");
            Intrinsics.checkNotNullParameter(elapsed, "elapsed");
            Intrinsics.checkNotNullParameter(total, "total");
            Intrinsics.checkNotNullParameter(insert, "insert");
            this.this$0 = this$0;
            this.row = row;
            this.play = play;
            this.glyph = glyph;
            this.title = title;
            this.sub = sub;
            this.dur = dur;
            this.seekRow = seekRow;
            this.seek = seek;
            this.elapsed = elapsed;
            this.total = total;
            this.insert = insert;
        }

        public final PlayGlyph getGlyph() {
            return this.glyph;
        }

        public final ImageView getPlay() {
            return this.play;
        }

        public final LinearLayout getRow() {
            return this.row;
        }

        public final TextView getSub() {
            return this.sub;
        }

        public final TextView getTitle() {
            return this.title;
        }

        public final TextView getDur() {
            return this.dur;
        }

        public final TextView getElapsed() {
            return this.elapsed;
        }

        public final SeekBar getSeek() {
            return this.seek;
        }

        public final LinearLayout getSeekRow() {
            return this.seekRow;
        }

        public final TextView getTotal() {
            return this.total;
        }

        public final TextView getInsert() {
            return this.insert;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: compiled from: AudioBrowser.kt */
    final class Adapter extends RecyclerView.Adapter<VH> {
        public Adapter() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return AudioBrowser.this.items.size();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            Intrinsics.checkNotNullParameter(parent, "parent");
            Context ctx = parent.getContext();
            LinearLayout outer = new LinearLayout(ctx);
            AudioBrowser audioBrowser = AudioBrowser.this;
            outer.setOrientation(1);
            outer.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            outer.setPadding(audioBrowser.dp(8.0f), audioBrowser.dp(6.0f), audioBrowser.dp(8.0f), audioBrowser.dp(4.0f));
            Ui ui = Ui.INSTANCE;
            Intrinsics.checkNotNull(ctx);
            outer.setBackground(ui.rippleBg(ctx, Ui.INSTANCE.getPANEL(), 0.0f));
            LinearLayout linearLayout = new LinearLayout(ctx);
            linearLayout.setOrientation(0);
            linearLayout.setGravity(16);
            PlayGlyph glyph = new PlayGlyph(false, false, 2, null);
            ImageView imageView = new ImageView(ctx);
            AudioBrowser audioBrowser2 = AudioBrowser.this;
            imageView.setImageDrawable(glyph);
            imageView.setPadding(audioBrowser2.dp(2.0f), audioBrowser2.dp(2.0f), audioBrowser2.dp(2.0f), audioBrowser2.dp(2.0f));
            LinearLayout.LayoutParams layoutParamsLp$default = Ui.lp$default(Ui.INSTANCE, AudioBrowser.this.dp(34.0f), AudioBrowser.this.dp(34.0f), 0.0f, 4, null);
            layoutParamsLp$default.rightMargin = AudioBrowser.this.dp(10.0f);
            Unit unit = Unit.INSTANCE;
            linearLayout.addView(imageView, layoutParamsLp$default);
            LinearLayout linearLayout2 = new LinearLayout(ctx);
            linearLayout2.setOrientation(1);
            TextView textViewText = Ui.INSTANCE.text(ctx, "", 13.0f, Ui.INSTANCE.getTEXT(), true);
            textViewText.setEllipsize(TextUtils.TruncateAt.END);
            TextView textViewText$default = Ui.text$default(Ui.INSTANCE, ctx, "", 11.0f, Ui.INSTANCE.getDIM(), false, 16, null);
            textViewText$default.setEllipsize(TextUtils.TruncateAt.END);
            linearLayout2.addView(textViewText);
            linearLayout2.addView(textViewText$default);
            linearLayout.addView(linearLayout2, Ui.INSTANCE.lp(0, -2, 1.0f));
            TextView textViewText$default2 = Ui.text$default(Ui.INSTANCE, ctx, "", 11.0f, Ui.INSTANCE.getDIM(), false, 16, null);
            AudioBrowser audioBrowser3 = AudioBrowser.this;
            textViewText$default2.setPadding(audioBrowser3.dp(6.0f), 0, audioBrowser3.dp(6.0f), 0);
            linearLayout.addView(textViewText$default2);
            TextView textViewButton = Ui.INSTANCE.button(ctx, "Insert", Ui.INSTANCE.getACCENT(), new Function1() { // from class: com.goydevv.studrblx.ui.AudioBrowser$Adapter$$ExternalSyntheticLambda1
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return AudioBrowser.Adapter.onCreateViewHolder$lambda$8((View) obj);
                }
            });
            AudioBrowser audioBrowser4 = AudioBrowser.this;
            textViewButton.setTextColor(-1);
            textViewButton.setTextSize(12.0f);
            textViewButton.setPadding(audioBrowser4.dp(10.0f), audioBrowser4.dp(4.0f), audioBrowser4.dp(10.0f), audioBrowser4.dp(4.0f));
            linearLayout.addView(textViewButton);
            outer.addView(linearLayout, Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null));
            LinearLayout linearLayout3 = new LinearLayout(ctx);
            linearLayout3.setOrientation(0);
            linearLayout3.setGravity(16);
            linearLayout3.setVisibility(8);
            TextView textViewText$default3 = Ui.text$default(Ui.INSTANCE, ctx, "0:00", 11.0f, Ui.INSTANCE.getDIM(), false, 16, null);
            textViewText$default3.setGravity(GravityCompat.END);
            SeekBar seekBar = new SeekBar(ctx);
            AudioBrowser audioBrowser5 = AudioBrowser.this;
            seekBar.setMax(1000);
            seekBar.setProgressTintList(ColorStateList.valueOf(Ui.INSTANCE.getACCENT()));
            seekBar.setThumbTintList(ColorStateList.valueOf(Ui.INSTANCE.getACCENT()));
            seekBar.setPadding(audioBrowser5.dp(12.0f), 0, audioBrowser5.dp(12.0f), 0);
            TextView total = Ui.text$default(Ui.INSTANCE, ctx, "", 11.0f, Ui.INSTANCE.getDIM(), false, 16, null);
            linearLayout3.addView(textViewText$default3, Ui.lp$default(Ui.INSTANCE, AudioBrowser.this.dp(38.0f), -2, 0.0f, 4, null));
            linearLayout3.addView(seekBar, Ui.INSTANCE.lp(0, AudioBrowser.this.dp(30.0f), 1.0f));
            linearLayout3.addView(total, Ui.lp$default(Ui.INSTANCE, AudioBrowser.this.dp(38.0f), -2, 0.0f, 4, null));
            LinearLayout.LayoutParams layoutParamsLp$default2 = Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null);
            AudioBrowser audioBrowser6 = AudioBrowser.this;
            layoutParamsLp$default2.topMargin = audioBrowser6.dp(2.0f);
            layoutParamsLp$default2.leftMargin = audioBrowser6.dp(36.0f);
            Unit unit2 = Unit.INSTANCE;
            outer.addView(linearLayout3, layoutParamsLp$default2);
            return new VH(AudioBrowser.this, outer, imageView, glyph, textViewText, textViewText$default, textViewText$default2, linearLayout3, seekBar, textViewText$default3, total, textViewButton);
        }

        static final Unit onCreateViewHolder$lambda$8(View it) {
            Intrinsics.checkNotNullParameter(it, "it");
            return Unit.INSTANCE;
        }

        /* JADX WARN: Code duplicated, block: B:32:0x011e  */
        /* JADX WARN: Code duplicated, block: B:38:0x012f  */
        /* JADX WARN: Code duplicated, block: B:56:0x01e4  */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(final VH h, int position) {
            boolean z;
            boolean z2;
            Intrinsics.checkNotNullParameter(h, "h");
            Object obj = AudioBrowser.this.items.get(position);
            Intrinsics.checkNotNullExpressionValue(obj, "get(...)");
            final RobloxApi.AudioItem it = (RobloxApi.AudioItem) obj;
            String title = it.getTitle();
            if (StringsKt.isBlank(title)) {
                title = it.getName();
            }
            final String label = title;
            h.getTitle().setText(label);
            final String kind = StringsKt.contains((CharSequence) it.getType(), (CharSequence) "Music", true) ? "Music" : StringsKt.contains((CharSequence) it.getType(), (CharSequence) "Sound", true) ? "Sound effect" : "";
            String artist = it.getArtist();
            if (StringsKt.isBlank(artist)) {
                artist = it.getCreator();
            }
            final String by = artist;
            TextView sub = h.getSub();
            Iterable iterableListOf = CollectionsKt.listOf(new String[]{by, kind, it.getAlbum()});
            Collection arrayList = new ArrayList();
            for (Object obj2 : iterableListOf) {
                if (!StringsKt.isBlank((String) obj2)) {
                    arrayList.add(obj2);
                }
            }
            sub.setText(CollectionsKt.joinToString((List) arrayList, "  ·  ", "", "", -1, "...", null));
            h.getDur().setText(AudioBrowser.this.fmtTime(it.getSeconds()));
            AudioEngine.Preview p = AudioEngine.INSTANCE.getPreview();
            boolean mine = p != null && p.getId() == it.getId();
            if (mine) {
                Intrinsics.checkNotNull(p);
                if (p.getState() == AudioEngine.PreviewState.PLAYING) {
                    z = true;
                } else {
                    z = false;
                }
            } else {
                z = false;
            }
            boolean playing = z;
            if (mine) {
                Intrinsics.checkNotNull(p);
                if (p.getState() == AudioEngine.PreviewState.LOADING) {
                    z2 = true;
                } else {
                    z2 = false;
                }
            } else {
                z2 = false;
            }
            boolean load = z2;
            h.getGlyph().set(playing, load);
            h.getPlay().invalidate();
            if (mine) {
                Intrinsics.checkNotNull(p);
                if (p.getState() != AudioEngine.PreviewState.ERROR) {
                    h.getSeekRow().setVisibility(0);
                    float len = p.getLength() > 0.0f ? p.getLength() : it.getSeconds();
                    boolean ready = p.getLength() > 0.0f;
                    h.getSeek().setEnabled(ready);
                    h.getSeek().setMax(RangesKt.coerceAtLeast((int) (len * 10.0f), 1));
                    Long l = AudioBrowser.this.draggingId;
                    long id = it.getId();
                    if (l == null || l.longValue() != id) {
                        h.getSeek().setProgress((int) (p.getPosition() * 10.0f));
                        h.getElapsed().setText(AudioBrowser.this.fmtClock(p.getPosition()));
                    }
                    h.getTotal().setText(AudioBrowser.this.fmtClock(len));
                    final long id2 = it.getId();
                    SeekBar seek = h.getSeek();
                    final AudioBrowser audioBrowser = AudioBrowser.this;
                    seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.goydevv.studrblx.ui.AudioBrowser$Adapter$onBindViewHolder$2
                        @Override // android.widget.SeekBar.OnSeekBarChangeListener
                        public void onProgressChanged(SeekBar sb, int v, boolean fromUser) {
                            Intrinsics.checkNotNullParameter(sb, "sb");
                            if (fromUser) {
                                h.getElapsed().setText(audioBrowser.fmtClock(v / 10.0f));
                            }
                        }

                        @Override // android.widget.SeekBar.OnSeekBarChangeListener
                        public void onStartTrackingTouch(SeekBar sb) {
                            Intrinsics.checkNotNullParameter(sb, "sb");
                            audioBrowser.draggingId = Long.valueOf(id2);
                        }

                        @Override // android.widget.SeekBar.OnSeekBarChangeListener
                        public void onStopTrackingTouch(SeekBar sb) {
                            Intrinsics.checkNotNullParameter(sb, "sb");
                            audioBrowser.draggingId = null;
                            AudioEngine.INSTANCE.seekPreview(id2, sb.getProgress() / 10.0f);
                        }
                    });
                } else {
                    h.getSeek().setOnSeekBarChangeListener(null);
                    h.getSeekRow().setVisibility(8);
                }
            } else {
                h.getSeek().setOnSeekBarChangeListener(null);
                h.getSeekRow().setVisibility(8);
            }
            if (mine) {
                Intrinsics.checkNotNull(p);
                if (p.getState() == AudioEngine.PreviewState.ERROR) {
                    TextView sub2 = h.getSub();
                    String error = p.getError();
                    if (error == null) {
                        error = "Can't play this audio";
                    }
                    sub2.setText(error);
                }
            }
            ImageView play = h.getPlay();
            final AudioBrowser audioBrowser2 = AudioBrowser.this;
            play.setOnClickListener(new View.OnClickListener() { // from class: com.goydevv.studrblx.ui.AudioBrowser$Adapter$$ExternalSyntheticLambda3
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    audioBrowser2.needSignIn.invoke(new Function0() { // from class: com.goydevv.studrblx.ui.AudioBrowser$Adapter$$ExternalSyntheticLambda2
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return AudioBrowser.Adapter.onBindViewHolder$lambda$18$lambda$17(it);
                        }
                    });
                }
            });
            TextView insert = h.getInsert();
            final AudioBrowser audioBrowser3 = AudioBrowser.this;
            insert.setOnClickListener(new View.OnClickListener() { // from class: com.goydevv.studrblx.ui.AudioBrowser$Adapter$$ExternalSyntheticLambda4
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    audioBrowser3.onInsert.invoke(it);
                }
            });
            LinearLayout row = h.getRow();
            final AudioBrowser audioBrowser4 = AudioBrowser.this;
            row.setOnClickListener(new View.OnClickListener() { // from class: com.goydevv.studrblx.ui.AudioBrowser$Adapter$$ExternalSyntheticLambda5
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    audioBrowser4.needSignIn.invoke(new Function0() { // from class: com.goydevv.studrblx.ui.AudioBrowser$Adapter$$ExternalSyntheticLambda0
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return AudioBrowser.Adapter.onBindViewHolder$lambda$21$lambda$20(it);
                        }
                    });
                }
            });
            LinearLayout row2 = h.getRow();
            final AudioBrowser audioBrowser5 = AudioBrowser.this;
            row2.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.goydevv.studrblx.ui.AudioBrowser$Adapter$$ExternalSyntheticLambda6
                @Override // android.view.View.OnLongClickListener
                public final boolean onLongClick(View view) {
                    return AudioBrowser.Adapter.onBindViewHolder$lambda$26(audioBrowser5, label, by, kind, it, view);
                }
            });
        }

        static final Unit onBindViewHolder$lambda$18$lambda$17(RobloxApi.AudioItem $it) {
            AudioEngine.togglePreview$default(AudioEngine.INSTANCE, $it.getId(), 0.0f, 2, null);
            return Unit.INSTANCE;
        }

        static final Unit onBindViewHolder$lambda$21$lambda$20(RobloxApi.AudioItem $it) {
            AudioEngine.togglePreview$default(AudioEngine.INSTANCE, $it.getId(), 0.0f, 2, null);
            return Unit.INSTANCE;
        }

        static final boolean onBindViewHolder$lambda$26(final AudioBrowser this$0, String $label, String $by, String $kind, final RobloxApi.AudioItem $it, View view) {
            Dialogs dialogs = Dialogs.INSTANCE;
            Context context = this$0.getContext();
            Intrinsics.checkNotNullExpressionValue(context, "getContext(...)");
            AlertDialog.Builder title = dialogs.themed(context).setTitle($label);
            String str = $by;
            if (StringsKt.isBlank(str)) {
                str = "Unknown";
            }
            String str2 = $kind;
            if (StringsKt.isBlank(str2)) {
                str2 = "Audio";
            }
            String strFmtTime = this$0.fmtTime($it.getSeconds());
            if (strFmtTime.length() == 0) {
                strFmtTime = "?";
            }
            title.setMessage("Artist: " + ((Object) str) + "\nType: " + ((Object) str2) + "\nLength: " + ((Object) strFmtTime) + "\nCreator: " + $it.getCreator() + ($it.getVerified() ? " (verified)" : "") + "\nAsset ID: " + $it.getId()).setPositiveButton("Insert", new DialogInterface.OnClickListener() { // from class: com.goydevv.studrblx.ui.AudioBrowser$Adapter$$ExternalSyntheticLambda7
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    this$0.onInsert.invoke($it);
                }
            }).setNegativeButton("Close", (DialogInterface.OnClickListener) null).show();
            return true;
        }
    }
}

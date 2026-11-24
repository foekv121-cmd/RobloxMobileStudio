package com.goydevv.studrblx.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.goydevv.studrblx.core.PlaceFactory;
import com.goydevv.studrblx.core.Recents;
import com.goydevv.studrblx.core.Studio;
import com.goydevv.studrblx.net.Accounts;
import com.goydevv.studrblx.net.AuthRequiredException;
import com.goydevv.studrblx.net.ImageLoader;
import com.goydevv.studrblx.net.RobloxApi;
import com.goydevv.studrblx.net.RobloxCloud;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.Pair;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: StartScreen.kt */
/* JADX INFO: loaded from: classes5.dex */
public final class StartScreen extends LinearLayout {
    public static final String ARCHIVE = "Archive";
    public static final String EXPERIENCES = "Experiences";
    public static final String GROUPS = "Group Games";
    public static final String HOME = "Home";
    public static final String MY_GAMES = "My Games";
    public static final String RECENT = "Recent";
    public static final String SHARED = "Shared With Me";
    public static final String TEMPLATES = "Templates";

    private static final int COL_CANVAS = 0xFF0F1117;
    private static final int COL_TOPBAR = 0xFF171B27;
    private static final int COL_DIVIDER = 0xFF232A3C;
    private static final int COL_CARD = 0xFF1A1F2E;
    private static final int COL_NAV_ACTIVE = 0xFF222B45;
    private static final int COL_CTA_BLUE = 0xFF5B8DEF;
    private static final int COL_PLUS_BLUE = 0xFF5B8DEF;
    private static final int COL_SEE_ALL_BG = 0xFF141824;
    private static final int COL_SEE_ALL_BORDER = 0xFF2A3145;
    private static final int COL_SEE_ALL_TEXT = 0xFF8F99B1;
    private static final int COL_BADGE_BG = 0xFF0B0D13;
    private static final int COL_TEXT_PRIMARY = 0xFFF1F4FB;
    private static final int COL_TEXT_CARD = 0xFFD3D8E6;
    private static final int COL_TEXT_SECONDARY = 0xFF9AA3BA;
    private static final int COL_TEXT_MUTED = 0xFF6F7992;
    private static final int COL_TEXT_OWNER = 0xFF6F7992;

    private static final String PREFS_START = "start_screen_prefs";
    private static final String KEY_TOUR_DISMISSED = "tour_dismissed";

    private final LinearLayout accountChip;
    private final Function0<Unit> accountListener;
    private final Activity act;
    private final MainActions actions;
    private final FrameLayout content;
    private TextView emptyFilterView;
    private String expSubTab;
    private String filterMode;
    private int gen;
    private final ArrayList<FlowLayout> grids;
    private final Host host;
    private final HashMap<String, List<ImageView>> iconViews;
    private final Function1<String, Unit> imgListener;
    private final Handler main;
    private final LinearLayout nav;
    private final ExecutorService net;
    private String page;
    private final boolean phone;
    private String query;
    private long selectedGroup;
    private boolean sortByName;
    private final Studio studio;
    private boolean tourDismissed;

    /* JADX INFO: compiled from: StartScreen.kt */
    public interface Host {
        void closeStart();

        void duplicateRecentFile(Recents.Entry e);

        boolean hasOpenPlace();

        void newFromTemplate(PlaceFactory.Template t);

        void openFileFromStart();

        void openRecentFile(Recents.Entry e);

        void shareRecentFile(Recents.Entry e);

        void showStudioSettings();
    }

    private static final class CardMeta {
        final String badge;
        final long modifiedMs;
        final String owner;
        final String title;

        CardMeta(String title, String badge, long modifiedMs, String owner) {
            this.title = title != null ? title : "";
            this.badge = badge != null ? badge : "";
            this.modifiedMs = modifiedMs;
            this.owner = owner != null ? owner : "";
        }
    }

    private static final class RefreshDrawable extends Drawable {
        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Path arrowPath = new Path();
        private final RectF arcRect = new RectF();

        RefreshDrawable(int color) {
            this.paint.setColor(color);
            this.paint.setStyle(Paint.Style.STROKE);
            this.paint.setStrokeCap(Paint.Cap.ROUND);
            this.paint.setStrokeJoin(Paint.Join.ROUND);
        }

        @Override
        public void draw(Canvas canvas) {
            Rect b = getBounds();
            float w = b.width();
            float h = b.height();
            float s = Math.min(w, h);
            if (s <= 0.0f) {
                return;
            }
            float cx = b.exactCenterX();
            float cy = b.exactCenterY();
            float r = s * 0.34f;
            float sw = Math.max(2.0f, s * 0.10f);
            this.paint.setStrokeWidth(sw);
            this.paint.setStyle(Paint.Style.STROKE);
            this.arcRect.set(cx - r, cy - r, cx + r, cy + r);
            canvas.drawArc(this.arcRect, -55.0f, 290.0f, false, this.paint);
            double endRad = Math.toRadians(-55.0d);
            float ax = cx + (float) (Math.cos(endRad) * r);
            float ay = cy + (float) (Math.sin(endRad) * r);
            float head = s * 0.17f;
            this.arrowPath.reset();
            this.arrowPath.moveTo(ax - head * 0.85f, ay - head * 0.15f);
            this.arrowPath.lineTo(ax + head * 0.10f, ay + head * 0.15f);
            this.arrowPath.lineTo(ax - head * 0.10f, ay - head * 0.90f);
            canvas.drawPath(this.arrowPath, this.paint);
        }

        @Override
        public void setAlpha(int alpha) {
            this.paint.setAlpha(alpha);
        }

        @Override
        public void setColorFilter(ColorFilter colorFilter) {
            this.paint.setColorFilter(colorFilter);
        }

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSLUCENT;
        }
    }

    private static final class TemplateThumbDrawable extends Drawable {
        private final PlaceFactory.Template template;
        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Path path = new Path();
        private final RectF rect = new RectF();

        TemplateThumbDrawable(PlaceFactory.Template template) {
            this.template = template;
        }

        @Override
        public void draw(Canvas canvas) {
            Rect b = getBounds();
            float w = b.width();
            float h = b.height();
            if (w <= 0.0f || h <= 0.0f) {
                return;
            }
            canvas.save();
            canvas.translate(b.left, b.top);
            if (this.template == PlaceFactory.Template.BASEPLATE) {
                drawSky(canvas, w, h, 0xFF2B3447, 0xFF1E222B);
                drawIsoPlane(canvas, w, h, 0xFF5B5D69, 0xFF474954);
                drawGridLines(canvas, w, h, 0x33FFFFFF, 6);
                drawSpawnPad(canvas, w * 0.5f, h * 0.62f, w * 0.14f);
            } else if (this.template == PlaceFactory.Template.CLASSIC) {
                drawSky(canvas, w, h, 0xFF4C8CD6, 0xFF8EC5F7);
                drawIsoPlane(canvas, w, h, 0xFF635F62, 0xFF4E4B4D);
                this.paint.setShader(null);
                this.paint.setStyle(Paint.Style.FILL);
                this.paint.setColor(0x55FFFFFF);
                for (int r = 0; r < 5; r++) {
                    for (int c = 0; c < 6; c++) {
                        float px = w * (0.14f + c * 0.145f);
                        float py = h * (0.52f + r * 0.085f);
                        canvas.drawCircle(px, py, Math.max(1.5f, w * 0.014f), this.paint);
                    }
                }
                drawSpawnPad(canvas, w * 0.5f, h * 0.65f, w * 0.13f);
            } else if (this.template == PlaceFactory.Template.FLAT_TERRAIN) {
                drawSky(canvas, w, h, 0xFF3E8EE0, 0xFF8BD3F7);
                this.paint.setShader(null);
                this.paint.setStyle(Paint.Style.FILL);
                this.paint.setColor(0xFFFFE58A);
                canvas.drawCircle(w * 0.78f, h * 0.22f, w * 0.09f, this.paint);
                drawIsoPlane(canvas, w, h, 0xFF5B9A4C, 0xFF3E7233);
                this.paint.setColor(0x33A7E37A);
                for (int i = 0; i < 8; i++) {
                    float gx = w * (0.12f + i * 0.11f);
                    float gy = h * (0.56f + (i % 3) * 0.10f);
                    canvas.drawOval(gx - w * 0.05f, gy - h * 0.015f, gx + w * 0.05f, gy + h * 0.015f, this.paint);
                }
                drawSpawnPad(canvas, w * 0.5f, h * 0.68f, w * 0.12f);
            } else if (this.template == PlaceFactory.Template.OBBY) {
                drawSky(canvas, w, h, 0xFF283459, 0xFF4A6FA5);
                this.paint.setShader(null);
                this.paint.setStyle(Paint.Style.FILL);
                int[] cols = new int[]{0xFFE74C3C, 0xFFE67E22, 0xFFF1C40F, 0xFF2ECC71, 0xFF3498DB, 0xFF9B59B6};
                for (int i = 0; i < cols.length; i++) {
                    float px = w * (0.16f + (i % 2) * 0.14f + i * 0.09f);
                    float py = h * (0.76f - i * 0.085f);
                    float pw = w * 0.18f;
                    float ph = h * 0.065f;
                    this.paint.setColor(cols[i]);
                    this.rect.set(px - pw * 0.5f, py - ph * 0.5f, px + pw * 0.5f, py + ph * 0.5f);
                    canvas.drawRoundRect(this.rect, w * 0.025f, w * 0.025f, this.paint);
                }
                this.paint.setColor(0xFFF5CD30);
                this.rect.set(w * 0.62f, h * 0.18f, w * 0.90f, h * 0.30f);
                canvas.drawRoundRect(this.rect, w * 0.03f, w * 0.03f, this.paint);
            } else if (this.template == PlaceFactory.Template.PLATFORMER) {
                drawSky(canvas, w, h, 0xFF1F4E78, 0xFF56B4D3);
                this.paint.setShader(null);
                this.paint.setStyle(Paint.Style.FILL);
                float[][] islands = new float[][]{
                    {0.24f, 0.76f, 0.30f, 0.10f},
                    {0.62f, 0.58f, 0.28f, 0.09f},
                    {0.34f, 0.38f, 0.26f, 0.08f},
                    {0.72f, 0.24f, 0.24f, 0.08f}
                };
                for (int i = 0; i < islands.length; i++) {
                    float cx = w * islands[i][0];
                    float cy = h * islands[i][1];
                    float iw = w * islands[i][2];
                    float ih = h * islands[i][3];
                    this.paint.setColor(0xFF6B5B4A);
                    this.rect.set(cx - iw * 0.45f, cy, cx + iw * 0.45f, cy + ih * 0.8f);
                    canvas.drawRoundRect(this.rect, w * 0.03f, w * 0.03f, this.paint);
                    this.paint.setColor(0xFF5B9A4C);
                    this.rect.set(cx - iw * 0.5f, cy - ih * 0.4f, cx + iw * 0.5f, cy + ih * 0.35f);
                    canvas.drawRoundRect(this.rect, w * 0.03f, w * 0.03f, this.paint);
                    if (i < 3) {
                        this.paint.setColor(0xFF00D4FF);
                        this.rect.set(cx + iw * 0.06f, cy - ih * 0.65f, cx + iw * 0.34f, cy - ih * 0.35f);
                        canvas.drawRoundRect(this.rect, w * 0.02f, w * 0.02f, this.paint);
                    }
                }
            } else {
                drawSky(canvas, w, h, 0xFF23252C, 0xFF17181D);
                drawGridLines(canvas, w, h, 0x1EFFFFFF, 5);
                drawSpawnPad(canvas, w * 0.5f, h * 0.54f, w * 0.20f);
            }
            canvas.restore();
        }

        private void drawSky(Canvas canvas, float w, float h, int top, int bottom) {
            this.paint.setStyle(Paint.Style.FILL);
            this.paint.setShader(new LinearGradient(0.0f, 0.0f, 0.0f, h, top, bottom, Shader.TileMode.CLAMP));
            canvas.drawRect(0.0f, 0.0f, w, h, this.paint);
            this.paint.setShader(null);
        }

        private void drawIsoPlane(Canvas canvas, float w, float h, int topCol, int botCol) {
            this.paint.setStyle(Paint.Style.FILL);
            this.paint.setShader(new LinearGradient(0.0f, h * 0.45f, 0.0f, h, topCol, botCol, Shader.TileMode.CLAMP));
            this.path.reset();
            this.path.moveTo(0.0f, h * 0.50f);
            this.path.lineTo(w, h * 0.46f);
            this.path.lineTo(w, h);
            this.path.lineTo(0.0f, h);
            this.path.close();
            canvas.drawPath(this.path, this.paint);
            this.paint.setShader(null);
        }

        private void drawGridLines(Canvas canvas, float w, float h, int color, int count) {
            this.paint.setShader(null);
            this.paint.setStyle(Paint.Style.STROKE);
            this.paint.setColor(color);
            this.paint.setStrokeWidth(Math.max(1.0f, w * 0.008f));
            for (int i = 1; i <= count; i++) {
                float y = h * (0.48f + (0.52f * i / count));
                canvas.drawLine(0.0f, y, w, y, this.paint);
                float x = w * ((float) i / (count + 1));
                canvas.drawLine(w * 0.5f + (x - w * 0.5f) * 0.45f, h * 0.48f, x, h, this.paint);
            }
        }

        private void drawSpawnPad(Canvas canvas, float cx, float cy, float size) {
            this.paint.setShader(null);
            this.paint.setStyle(Paint.Style.FILL);
            this.paint.setColor(0xFFA3A2A5);
            this.rect.set(cx - size, cy - size * 0.42f, cx + size, cy + size * 0.42f);
            canvas.drawRoundRect(this.rect, size * 0.16f, size * 0.16f, this.paint);
            this.paint.setStyle(Paint.Style.STROKE);
            this.paint.setStrokeWidth(Math.max(1.5f, size * 0.10f));
            this.paint.setColor(0xFFEBECEE);
            canvas.drawCircle(cx, cy, size * 0.24f, this.paint);
        }

        @Override
        public void setAlpha(int alpha) {
            this.paint.setAlpha(alpha);
        }

        @Override
        public void setColorFilter(ColorFilter colorFilter) {
            this.paint.setColorFilter(colorFilter);
        }

        @Override
        public int getOpacity() {
            return PixelFormat.OPAQUE;
        }
    }

    private static final class TourCollageView extends View {
        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final RectF rect = new RectF();
        private final Path path = new Path();

        TourCollageView(Activity ctx) {
            super(ctx);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            float w = getWidth();
            float h = getHeight();
            if (w <= 0.0f || h <= 0.0f) {
                return;
            }
            float cw = w * 0.42f;
            float ch = h * 0.70f;
            drawSceneCard(canvas, w * 0.25f, h * 0.56f, cw, ch, -9.0f, 0xFF284B7A, 0xFF1A2942, 0xFF47B39C);
            drawSceneCard(canvas, w * 0.74f, h * 0.54f, cw, ch, 10.0f, 0xFF53346D, 0xFF221A33, 0xFFF5A623);
            drawSceneCard(canvas, w * 0.50f, h * 0.48f, cw * 1.08f, ch * 1.06f, -2.0f, 0xFF2F5DF5, 0xFF1C2747, 0xFF63D471);
        }

        private void drawSceneCard(Canvas canvas, float cx, float cy, float cw, float ch, float deg, int skyTop, int skyBot, int accent) {
            canvas.save();
            canvas.rotate(deg, cx, cy);
            float l = cx - cw * 0.5f;
            float t = cy - ch * 0.5f;
            float r = cx + cw * 0.5f;
            float b = cy + ch * 0.5f;
            float rad = Math.min(cw, ch) * 0.14f;

            this.paint.setStyle(Paint.Style.FILL);
            this.paint.setShader(null);
            this.paint.setColor(0x55000000);
            this.rect.set(l + 3.0f, t + 5.0f, r + 3.0f, b + 5.0f);
            canvas.drawRoundRect(this.rect, rad, rad, this.paint);

            this.paint.setShader(new LinearGradient(l, t, l, b, skyTop, skyBot, Shader.TileMode.CLAMP));
            this.rect.set(l, t, r, b);
            canvas.drawRoundRect(this.rect, rad, rad, this.paint);
            this.paint.setShader(null);

            this.path.reset();
            this.path.moveTo(l + cw * 0.12f, b - ch * 0.24f);
            this.path.lineTo(cx, cy + ch * 0.04f);
            this.path.lineTo(r - cw * 0.12f, b - ch * 0.24f);
            this.path.lineTo(cx, b - ch * 0.08f);
            this.path.close();
            this.paint.setColor(accent);
            canvas.drawPath(this.path, this.paint);

            this.paint.setColor(0xCCFFFFFF);
            this.rect.set(cx - cw * 0.16f, cy - ch * 0.18f, cx + cw * 0.16f, cy + ch * 0.02f);
            canvas.drawRoundRect(this.rect, rad * 0.35f, rad * 0.35f, this.paint);

            this.paint.setStyle(Paint.Style.STROKE);
            this.paint.setStrokeWidth(2.0f);
            this.paint.setColor(0x44FFFFFF);
            this.rect.set(l, t, r, b);
            canvas.drawRoundRect(this.rect, rad, rad, this.paint);
            canvas.restore();
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public StartScreen(Activity act, Studio studio, MainActions actions, Host host) {
        super(act);
        Intrinsics.checkNotNullParameter(act, "act");
        Intrinsics.checkNotNullParameter(studio, "studio");
        Intrinsics.checkNotNullParameter(actions, "actions");
        Intrinsics.checkNotNullParameter(host, "host");
        this.act = act;
        this.studio = studio;
        this.actions = actions;
        this.host = host;
        this.net = Executors.newFixedThreadPool(2);
        this.main = new Handler(Looper.getMainLooper());
        this.content = new FrameLayout(this.act);
        this.nav = new LinearLayout(this.act);
        this.page = HOME;
        this.expSubTab = MY_GAMES;
        this.filterMode = "All";
        this.sortByName = false;
        this.accountChip = new LinearLayout(this.act);
        this.phone = this.act.getResources().getConfiguration().smallestScreenWidthDp < 600;
        this.iconViews = new HashMap<>();
        this.grids = new ArrayList<>();
        this.query = "";
        SharedPreferences prefs = this.act.getSharedPreferences(PREFS_START, 0);
        this.tourDismissed = prefs.getBoolean(KEY_TOUR_DISMISSED, false);
        this.imgListener = new Function1() {
            @Override
            public final Object invoke(Object obj) {
                return StartScreen.imgListener$lambda$3(StartScreen.this, (String) obj);
            }
        };
        this.accountListener = new Function0() {
            @Override
            public final Object invoke() {
                return StartScreen.accountListener$lambda$5(StartScreen.this);
            }
        };

        setOrientation(VERTICAL);
        setBackgroundColor(COL_CANVAS);
        setClickable(true);

        // Compact #353535 top bar (G3, G8)
        LinearLayout topBar = new LinearLayout(this.act);
        topBar.setOrientation(HORIZONTAL);
        topBar.setGravity(Gravity.CENTER_VERTICAL);
        topBar.setBackgroundColor(COL_TOPBAR);
        topBar.setPadding(dp(8.0f), dp(4.0f), dp(8.0f), dp(4.0f));
        topBar.setMinimumHeight(dp(40.0f));

        final TextView fileBtn = Ui.INSTANCE.text(this.act, "FILE ▾", 12.5f, COL_TEXT_PRIMARY, true);
        fileBtn.setPadding(dp(10.0f), dp(6.0f), dp(10.0f), dp(6.0f));
        fileBtn.setBackground(Ui.INSTANCE.rippleBg(this.act, COL_TOPBAR, 5.0f));
        fileBtn.setClickable(true);
        fileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartScreen.this.showFileMenu(fileBtn);
            }
        });
        topBar.addView(fileBtn, Ui.lp$default(Ui.INSTANCE, -2, -2, 0.0f, 4, null));

        topBar.addView(new View(this.act), Ui.INSTANCE.lp(0, 1, 1.0f));

        ImageView refreshBtn = new ImageView(this.act);
        refreshBtn.setImageDrawable(new RefreshDrawable(COL_TEXT_PRIMARY));
        refreshBtn.setPadding(dp(7.0f), dp(7.0f), dp(7.0f), dp(7.0f));
        refreshBtn.setBackground(Ui.INSTANCE.rippleBg(this.act, COL_TOPBAR, 5.0f));
        refreshBtn.setClickable(true);
        refreshBtn.setContentDescription("Refresh");
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartScreen.this.show(StartScreen.this.page);
            }
        });
        LinearLayout.LayoutParams lpRef = Ui.lp$default(Ui.INSTANCE, dp(32.0f), dp(32.0f), 0.0f, 4, null);
        lpRef.rightMargin = dp(4.0f);
        topBar.addView(refreshBtn, lpRef);

        final TextView helpBtn = Ui.INSTANCE.text(this.act, "?", 13.5f, COL_TEXT_PRIMARY, true);
        helpBtn.setGravity(Gravity.CENTER);
        helpBtn.setBackground(Ui.INSTANCE.rippleBg(this.act, COL_TOPBAR, 16.0f));
        helpBtn.setClickable(true);
        helpBtn.setContentDescription("Help");
        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartScreen.this.showHelpMenu(helpBtn);
            }
        });
        LinearLayout.LayoutParams lpHelp = Ui.lp$default(Ui.INSTANCE, dp(30.0f), dp(30.0f), 0.0f, 4, null);
        lpHelp.rightMargin = dp(6.0f);
        topBar.addView(helpBtn, lpHelp);

        this.accountChip.setOrientation(HORIZONTAL);
        this.accountChip.setGravity(Gravity.CENTER_VERTICAL);
        this.accountChip.setPadding(dp(5.0f), dp(3.0f), dp(9.0f), dp(3.0f));
        this.accountChip.setBackground(Ui.INSTANCE.rippleBg(this.act, 0xFF1C2130, 14.0f));
        this.accountChip.setClickable(true);
        this.accountChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AccountUi.INSTANCE.show(StartScreen.this.act, StartScreen.this.studio, StartScreen.this.actions);
            }
        });
        topBar.addView(this.accountChip, Ui.lp$default(Ui.INSTANCE, -2, -2, 0.0f, 4, null));

        addView(topBar, Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null));
        addView(makeDivider(false));

        // Body: flush #1F2024 sidebar + 1px #292A2E divider + content (G2)
        LinearLayout body = new LinearLayout(this.act);
        body.setOrientation(this.phone ? VERTICAL : HORIZONTAL);
        body.setBackgroundColor(COL_CANVAS);
        this.nav.setOrientation(this.phone ? HORIZONTAL : VERTICAL);
        this.nav.setBackgroundColor(COL_CANVAS);
        if (this.phone) {
            this.nav.setPadding(dp(8.0f), dp(6.0f), dp(8.0f), dp(6.0f));
            HorizontalScrollView hsv = new HorizontalScrollView(this.act);
            hsv.setHorizontalScrollBarEnabled(false);
            hsv.setBackgroundColor(COL_CANVAS);
            hsv.addView(this.nav);
            body.addView(hsv, Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null));
            body.addView(makeDivider(false));
        } else {
            this.nav.setPadding(dp(10.0f), dp(12.0f), dp(10.0f), dp(12.0f));
            ScrollView sv = new ScrollView(this.act);
            sv.setFillViewport(true);
            sv.setBackgroundColor(COL_CANVAS);
            sv.addView(this.nav, new FrameLayout.LayoutParams(-1, -1));
            body.addView(sv, Ui.lp$default(Ui.INSTANCE, dp(208.0f), -1, 0.0f, 4, null));
            body.addView(makeDivider(true));
        }
        this.content.setBackgroundColor(COL_CANVAS);
        body.addView(this.content, Ui.INSTANCE.lp(this.phone ? -1 : 0, this.phone ? 0 : -1, 1.0f));
        addView(body, Ui.INSTANCE.lp(-1, 0, 1.0f));

        refreshAccount();
        show(HOME);
    }

    private final View makeDivider(boolean vertical) {
        View v = new View(this.act);
        v.setBackgroundColor(COL_DIVIDER);
        v.setLayoutParams(new LinearLayout.LayoutParams(vertical ? 1 : -1, vertical ? -1 : 1));
        return v;
    }

    private final void showFileMenu(View anchor) {
        PopupMenu pm = new PopupMenu(Ui.INSTANCE.popupContext(this.act), anchor);
        Menu m = pm.getMenu();
        m.add(0, 1, 0, "New File");
        m.add(0, 2, 1, "Open Local File…");
        m.add(0, 3, 2, "Open by Place ID…");
        m.add(0, 4, 3, "Studio Settings");
        m.add(0, 5, 4, "About Mobile Studio");
        if (this.host.hasOpenPlace()) {
            m.add(0, 6, 5, "Back to Place");
        }
        pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case 1:
                        StartScreen.this.host.newFromTemplate(PlaceFactory.Template.BASEPLATE);
                        return true;
                    case 2:
                        StartScreen.this.host.openFileFromStart();
                        return true;
                    case 3:
                        AccountUi.INSTANCE.openById(StartScreen.this.act, StartScreen.this.studio, StartScreen.this.actions);
                        return true;
                    case 4:
                        StartScreen.this.host.showStudioSettings();
                        return true;
                    case 5:
                        AboutDialog.INSTANCE.show(StartScreen.this.act);
                        return true;
                    case 6:
                        StartScreen.this.host.closeStart();
                        return true;
                    default:
                        return false;
                }
            }
        });
        pm.show();
    }

    private final void showHelpMenu(View anchor) {
        PopupMenu pm = new PopupMenu(Ui.INSTANCE.popupContext(this.act), anchor);
        Menu m = pm.getMenu();
        m.add(0, 1, 0, "Take a Quick Tour");
        m.add(0, 2, 1, "About Mobile Studio");
        pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == 1) {
                    StartScreen.this.tourDismissed = false;
                    StartScreen.this.act.getSharedPreferences(PREFS_START, 0).edit().putBoolean(KEY_TOUR_DISMISSED, false).apply();
                    StartScreen.this.show(HOME);
                    return true;
                }
                if (item.getItemId() == 2) {
                    AboutDialog.INSTANCE.show(StartScreen.this.act);
                    return true;
                }
                return false;
            }
        });
        pm.show();
    }

    static final Unit imgListener$lambda$3(final StartScreen this$0, final String k) {
        Intrinsics.checkNotNullParameter(k, "k");
        if (StringsKt.startsWith(k, "url:", false)) {
            this$0.main.post(new Runnable() {
                @Override
                public final void run() {
                    StartScreen.imgListener$lambda$3$lambda$2(this$0, k);
                }
            });
        }
        return Unit.INSTANCE;
    }

    static final void imgListener$lambda$3$lambda$2(StartScreen this$0, String $k) {
        Iterable<ImageView> iterable = this$0.iconViews.get($k);
        if (iterable != null) {
            for (ImageView imageView : iterable) {
                Bitmap bitmapPeekKey = ImageLoader.INSTANCE.peekKey($k);
                if (bitmapPeekKey != null) {
                    this$0.showBitmap(imageView, bitmapPeekKey);
                }
            }
        }
    }

    static final Unit accountListener$lambda$5(final StartScreen this$0) {
        this$0.main.post(new Runnable() {
            @Override
            public final void run() {
                StartScreen.accountListener$lambda$5$lambda$4(this$0);
            }
        });
        return Unit.INSTANCE;
    }

    static final void accountListener$lambda$5$lambda$4(StartScreen this$0) {
        this$0.refreshAccount();
        this$0.show(this$0.page);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final int getLinkColor() {
        return COL_CTA_BLUE;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ImageLoader.INSTANCE.getListeners().add(this.imgListener);
        Accounts.INSTANCE.getListeners().add(this.accountListener);
    }

    @Override
    protected void onDetachedFromWindow() {
        ImageLoader.INSTANCE.getListeners().remove(this.imgListener);
        Accounts.INSTANCE.getListeners().remove(this.accountListener);
        this.gen++;
        this.net.shutdown();
        super.onDetachedFromWindow();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final int dp(float v) {
        return Ui.INSTANCE.dp(this.act, v);
    }

    private final void buildNav() {
        this.nav.removeAllViews();

        // '+ New File' button with #4769E1 badge (G2)
        LinearLayout newFileRow = new LinearLayout(this.act);
        newFileRow.setOrientation(HORIZONTAL);
        newFileRow.setGravity(Gravity.CENTER_VERTICAL);
        newFileRow.setPadding(dp(10.0f), dp(8.0f), dp(12.0f), dp(8.0f));
        newFileRow.setBackground(Ui.INSTANCE.rippleBg(this.act, COL_CANVAS, 6.0f));
        newFileRow.setClickable(true);
        TextView plusBadge = new TextView(this.act);
        plusBadge.setText("+");
        plusBadge.setTextColor(0xFFFFFFFF);
        plusBadge.setTextSize(14.0f);
        plusBadge.setTypeface(Typeface.DEFAULT_BOLD);
        plusBadge.setGravity(Gravity.CENTER);
        GradientDrawable plusBg = new GradientDrawable();
        plusBg.setColor(COL_PLUS_BLUE);
        plusBg.setCornerRadius(dp(5.0f));
        plusBadge.setBackground(plusBg);
        LinearLayout.LayoutParams lpPlus = new LinearLayout.LayoutParams(dp(22.0f), dp(22.0f));
        lpPlus.rightMargin = dp(10.0f);
        newFileRow.addView(plusBadge, lpPlus);
        newFileRow.addView(Ui.INSTANCE.text(this.act, "New File", 13.5f, COL_TEXT_PRIMARY, true));
        newFileRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartScreen.this.host.newFromTemplate(PlaceFactory.Template.BASEPLATE);
            }
        });
        LinearLayout.LayoutParams lpNf = Ui.lp$default(Ui.INSTANCE, this.phone ? -2 : -1, -2, 0.0f, 4, null);
        if (this.phone) {
            lpNf.rightMargin = dp(8.0f);
        } else {
            lpNf.bottomMargin = dp(10.0f);
        }
        this.nav.addView(newFileRow, lpNf);

        // 4 flat nav items: Home, Experiences, Templates, Archive (G2)
        String[][] items = new String[][]{
            {HOME, HOME},
            {EXPERIENCES, "Place"},
            {TEMPLATES, "Part_Block"},
            {ARCHIVE, "Folder"}
        };
        for (int i = 0; i < items.length; i++) {
            final String p = items[i][0];
            String icon = items[i][1];
            addNavItem(p, icon, Intrinsics.areEqual(this.page, p), new Function0<Unit>() {
                @Override
                public Unit invoke() {
                    StartScreen.this.show(p);
                    return Unit.INSTANCE;
                }
            });
        }
        addNavItem("Open File\u2026", "Folder", false, new Function0<Unit>() {
            @Override
            public Unit invoke() {
                StartScreen.this.host.openFileFromStart();
                return Unit.INSTANCE;
            }
        });
        addNavItem("Open by Place ID\u2026", "Place", false, new Function0<Unit>() {
            @Override
            public Unit invoke() {
                AccountUi.INSTANCE.openById(StartScreen.this.act, StartScreen.this.studio, StartScreen.this.actions);
                return Unit.INSTANCE;
            }
        });
        addNavItem("Quick Publish .rbxl\u2026", "Publish", false, new Function0<Unit>() {
            @Override
            public Unit invoke() {
                QuickPublish.show(StartScreen.this.act);
                return Unit.INSTANCE;
            }
        });
    }

    private final void addNavItem(String label, String icon, boolean selected, final Function0<Unit> onClick) {
        LinearLayout v = new LinearLayout(this.act);
        v.setOrientation(HORIZONTAL);
        v.setGravity(Gravity.CENTER_VERTICAL);
        v.setPadding(dp(10.0f), dp(this.phone ? 7.0f : 9.0f), dp(14.0f), dp(this.phone ? 7.0f : 9.0f));
        v.setBackground(Ui.INSTANCE.rippleBg(this.act, selected ? COL_NAV_ACTIVE : COL_CANVAS, 6.0f));
        v.setClickable(true);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.invoke();
            }
        });
        v.setContentDescription(label);
        ImageView iv = new ImageView(this.act);
        iv.setImageDrawable(Icons.general$default(Icons.INSTANCE, this.act, icon, null, 4, null));
        LinearLayout.LayoutParams lpIcon = Ui.lp$default(Ui.INSTANCE, dp(18.0f), dp(18.0f), 0.0f, 4, null);
        lpIcon.rightMargin = dp(10.0f);
        v.addView(iv, lpIcon);
        v.addView(Ui.INSTANCE.text(this.act, label, 13.5f, selected ? COL_TEXT_PRIMARY : COL_TEXT_SECONDARY, selected));
        LinearLayout.LayoutParams lpItem;
        if (this.phone) {
            lpItem = Ui.lp$default(Ui.INSTANCE, -2, -2, 0.0f, 4, null);
            lpItem.rightMargin = dp(4.0f);
        } else {
            lpItem = Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null);
            lpItem.bottomMargin = dp(3.0f);
        }
        this.nav.addView(v, lpItem);
    }

    private final void refreshAccount() {
        this.accountChip.removeAllViews();
        ImageView img = new ImageView(this.act);
        GradientDrawable avBg = new GradientDrawable();
        avBg.setShape(GradientDrawable.OVAL);
        avBg.setColor(COL_CARD);
        img.setBackground(avBg);
        img.setClipToOutline(true);
        LinearLayout.LayoutParams lpImg = Ui.lp$default(Ui.INSTANCE, dp(22.0f), dp(22.0f), 0.0f, 4, null);
        lpImg.rightMargin = dp(6.0f);
        this.accountChip.addView(img, lpImg);
        Accounts.Account a = Accounts.INSTANCE.active(this.act);
        String label;
        if (a != null) {
            AccountUi.INSTANCE.loadHeadshot(img, a.getUserId());
            String uname = a.getName();
            if (uname == null || uname.length() == 0) {
                uname = a.getDisplayName();
            }
            if (uname == null || uname.length() == 0) {
                label = "Account";
            } else {
                label = "@" + (this.phone ? StringsKt.take(uname, 10) : uname);
            }
        } else {
            img.setImageDrawable(Icons.general$default(Icons.INSTANCE, this.act, "Account", null, 4, null));
            label = "Guest";
        }
        this.accountChip.addView(Ui.INSTANCE.text(this.act, label + " ▾", 12.0f, COL_TEXT_PRIMARY, false));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void show(String p) {
        String target = p;
        if (MY_GAMES.equals(p) || SHARED.equals(p) || GROUPS.equals(p) || RECENT.equals(p)) {
            this.expSubTab = p;
            target = EXPERIENCES;
        }
        this.page = target;
        this.gen++;
        this.grids.clear();
        this.query = "";
        this.filterMode = "All";
        this.emptyFilterView = null;
        buildNav();
        this.content.removeAllViews();
        this.iconViews.clear();
        if (HOME.equals(target)) {
            home();
        } else if (EXPERIENCES.equals(target)) {
            experiencesPage();
        } else if (TEMPLATES.equals(target)) {
            templatesPage();
        } else if (ARCHIVE.equals(target)) {
            archivePage();
        } else {
            home();
        }
    }

    private final LinearLayout scrollColumn() {
        LinearLayout col = new LinearLayout(this.act);
        col.setOrientation(VERTICAL);
        col.setBackgroundColor(COL_CANVAS);
        col.setPadding(dp(this.phone ? 14.0f : 24.0f), dp(12.0f), dp(this.phone ? 14.0f : 24.0f), dp(28.0f));
        ScrollView sv = new ScrollView(this.act);
        sv.setBackgroundColor(COL_CANVAS);
        sv.addView(col);
        this.content.addView(sv, new FrameLayout.LayoutParams(-1, -1));
        return col;
    }

    private final void heading(LinearLayout col, String text, final String seeAll) {
        LinearLayout r = new LinearLayout(this.act);
        r.setOrientation(HORIZONTAL);
        r.setGravity(Gravity.CENTER_VERTICAL);
        r.setPadding(0, dp(18.0f), 0, dp(10.0f));
        r.addView(Ui.INSTANCE.text(this.act, text, 17.0f, COL_TEXT_PRIMARY, true), Ui.INSTANCE.lp(0, -2, 1.0f));
        if (seeAll != null) {
            TextView btn = Ui.INSTANCE.text(this.act, "See All", 12.0f, COL_SEE_ALL_TEXT, true);
            btn.setPadding(dp(12.0f), dp(5.0f), dp(12.0f), dp(5.0f));
            GradientDrawable bg = new GradientDrawable();
            bg.setColor(COL_SEE_ALL_BG);
            bg.setStroke(dp(1.0f), COL_SEE_ALL_BORDER);
            bg.setCornerRadius(dp(6.0f));
            btn.setBackground(bg);
            btn.setClickable(true);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StartScreen.this.show(seeAll);
                }
            });
            r.addView(btn);
        }
        col.addView(r, Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null));
    }

    private final void pageHeader(LinearLayout col, String title, String sub, boolean search, final Function0<Unit> onRefresh) {
        LinearLayout r = new LinearLayout(this.act);
        r.setOrientation(HORIZONTAL);
        r.setGravity(Gravity.CENTER_VERTICAL);
        r.setPadding(0, dp(6.0f), 0, dp(6.0f));
        LinearLayout t = new LinearLayout(this.act);
        t.setOrientation(VERTICAL);
        t.addView(Ui.INSTANCE.text(this.act, title, 22.0f, COL_TEXT_PRIMARY, true));
        if (sub != null && sub.length() > 0) {
            TextView tvSub = Ui.INSTANCE.text(this.act, sub, 12.5f, COL_TEXT_SECONDARY, false);
            tvSub.setSingleLine(false);
            tvSub.setPadding(0, dp(2.0f), 0, 0);
            t.addView(tvSub);
        }
        r.addView(t, Ui.INSTANCE.lp(0, -2, 1.0f));
        if (onRefresh != null) {
            ImageView refreshIcon = new ImageView(this.act);
            refreshIcon.setImageDrawable(new RefreshDrawable(COL_TEXT_PRIMARY));
            refreshIcon.setPadding(dp(8.0f), dp(8.0f), dp(8.0f), dp(8.0f));
            refreshIcon.setBackground(Ui.INSTANCE.rippleBg(this.act, COL_CARD, 6.0f));
            refreshIcon.setClickable(true);
            refreshIcon.setContentDescription("Refresh");
            refreshIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRefresh.invoke();
                }
            });
            r.addView(refreshIcon, Ui.lp$default(Ui.INSTANCE, dp(34.0f), dp(34.0f), 0.0f, 4, null));
        }
        col.addView(r, Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null));
        if (search) {
            addSearchBox(col);
        }
    }

    private final void addSearchBox(LinearLayout col) {
        EditText box = Ui.input$default(Ui.INSTANCE, this.act, "", "Search", false, false, 24, null);
        box.setTextColor(COL_TEXT_PRIMARY);
        box.setHintTextColor(COL_TEXT_MUTED);
        Drawable icon = Icons.general$default(Icons.INSTANCE, this.act, "FindAll", null, 4, null);
        icon.setBounds(0, 0, dp(16.0f), dp(16.0f));
        box.setCompoundDrawables(icon, null, null, null);
        box.setCompoundDrawablePadding(dp(8.0f));
        box.setPadding(dp(12.0f), dp(8.0f), dp(12.0f), dp(8.0f));
        GradientDrawable bg = new GradientDrawable();
        bg.setColor(COL_CARD);
        bg.setStroke(dp(1.0f), COL_SEE_ALL_BORDER);
        bg.setCornerRadius(dp(6.0f));
        box.setBackground(bg);
        box.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int a, int b, int c) {
            }

            @Override
            public void onTextChanged(CharSequence s, int a, int b, int c) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String q = s != null ? StringsKt.trim((CharSequence) s.toString()).toString() : "";
                StartScreen.this.query = q != null ? q : "";
                StartScreen.this.applyFilter();
            }
        });
        LinearLayout.LayoutParams lp = Ui.lp$default(Ui.INSTANCE, this.phone ? -1 : dp(340.0f), -2, 0.0f, 4, null);
        lp.topMargin = dp(8.0f);
        lp.bottomMargin = dp(8.0f);
        col.addView(box, lp);
    }

    private final void addEmptyFilterView(LinearLayout col) {
        TextView tv = Ui.INSTANCE.text(this.act, "No matching results", 13.5f, COL_TEXT_MUTED, false);
        tv.setGravity(Gravity.CENTER);
        tv.setPadding(dp(16.0f), dp(28.0f), dp(16.0f), dp(28.0f));
        tv.setVisibility(View.GONE);
        col.addView(tv, Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null));
        this.emptyFilterView = tv;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void applyFilter() {
        String q = this.query.toLowerCase(Locale.ROOT);
        int totalCards = 0;
        int visibleCards = 0;
        for (int gi = 0; gi < this.grids.size(); gi++) {
            FlowLayout g = this.grids.get(gi);
            int childCount = g.getChildCount();
            if (childCount > 1) {
                List<View> children = new ArrayList<>(childCount);
                for (int i = 0; i < childCount; i++) {
                    children.add(g.getChildAt(i));
                }
                final boolean byName = this.sortByName;
                Collections.sort(children, new Comparator<View>() {
                    @Override
                    public int compare(View a, View b) {
                        CardMeta ma = a.getTag() instanceof CardMeta ? (CardMeta) a.getTag() : null;
                        CardMeta mb = b.getTag() instanceof CardMeta ? (CardMeta) b.getTag() : null;
                        if (ma == null || mb == null) {
                            return 0;
                        }
                        if (byName) {
                            return ma.title.compareToIgnoreCase(mb.title);
                        }
                        return Long.compare(mb.modifiedMs, ma.modifiedMs);
                    }
                });
                boolean orderChanged = false;
                for (int i = 0; i < childCount; i++) {
                    if (g.getChildAt(i) != children.get(i)) {
                        orderChanged = true;
                        break;
                    }
                }
                if (orderChanged) {
                    g.removeAllViews();
                    for (View v : children) {
                        g.addView(v);
                    }
                }
            }
            for (int i = 0; i < g.getChildCount(); i++) {
                View c = g.getChildAt(i);
                Object tag = c.getTag();
                if (tag instanceof CardMeta) {
                    totalCards++;
                    CardMeta meta = (CardMeta) tag;
                    boolean matchQuery = q.length() == 0
                            || meta.title.toLowerCase(Locale.ROOT).contains(q)
                            || meta.owner.toLowerCase(Locale.ROOT).contains(q);
                    boolean matchFilter = "All".equals(this.filterMode)
                            || meta.badge.equalsIgnoreCase(this.filterMode);
                    if (matchQuery && matchFilter) {
                        c.setVisibility(View.VISIBLE);
                        visibleCards++;
                    } else {
                        c.setVisibility(View.GONE);
                    }
                }
            }
        }
        if (this.emptyFilterView != null) {
            this.emptyFilterView.setVisibility(totalCards > 0 && visibleCards == 0 ? View.VISIBLE : View.GONE);
        }
    }

    static /* synthetic */ void message$default(StartScreen startScreen, LinearLayout linearLayout, String str, String str2, String str3, Function0 function0, int i, Object obj) {
        if ((i & 4) != 0) {
            str2 = null;
        }
        if ((i & 8) != 0) {
            str3 = "Account";
        }
        if ((i & 16) != 0) {
            function0 = null;
        }
        startScreen.message(linearLayout, str, str2, str3, function0);
    }

    private final void message(LinearLayout col, String text, String button, String icon, final Function0<Unit> onClick) {
        LinearLayout card = new LinearLayout(this.act);
        card.setOrientation(VERTICAL);
        card.setPadding(dp(16.0f), dp(14.0f), dp(16.0f), dp(14.0f));
        GradientDrawable bg = new GradientDrawable();
        bg.setColor(COL_CARD);
        bg.setCornerRadius(dp(6.0f));
        card.setBackground(bg);
        TextView tv = Ui.INSTANCE.text(this.act, text, 13.0f, COL_TEXT_CARD, false);
        tv.setSingleLine(false);
        tv.setLineSpacing(dp(2.0f), 1.0f);
        card.addView(tv);
        if (button != null && onClick != null) {
            Drawable btnIcon = "Refresh".equals(icon)
                    ? new RefreshDrawable(0xFFFFFFFF)
                    : Icons.general$default(Icons.INSTANCE, this.act, icon, null, 4, null);
            TextView btn = Ui.INSTANCE.iconTextButton(this.act, btnIcon, button, COL_CTA_BLUE, new Function1<View, Unit>() {
                @Override
                public Unit invoke(View view) {
                    onClick.invoke();
                    return Unit.INSTANCE;
                }
            });
            btn.setTextColor(0xFFFFFFFF);
            LinearLayout.LayoutParams lpBtn = Ui.lp$default(Ui.INSTANCE, -2, -2, 0.0f, 4, null);
            lpBtn.topMargin = dp(10.0f);
            card.addView(btn, lpBtn);
        }
        LinearLayout.LayoutParams lpCard = Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null);
        lpCard.topMargin = dp(6.0f);
        lpCard.bottomMargin = dp(6.0f);
        col.addView(card, lpCard);
    }

    private final void showBitmap(ImageView img, Bitmap b) {
        img.setPadding(0, 0, 0, 0);
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        img.setImageBitmap(b);
    }

    private final void bindIcon(ImageView img, String url, String fallback) {
        int pad = dp(this.phone ? 28.0f : 34.0f);
        if (url == null) {
            bindIcon$placeholder(img, pad, this, fallback);
            return;
        }
        String key = "url:" + url;
        Bitmap bmp = ImageLoader.INSTANCE.getKey(key);
        if (bmp != null) {
            showBitmap(img, bmp);
            return;
        }
        bindIcon$placeholder(img, pad, this, fallback);
        List<ImageView> list = this.iconViews.get(key);
        if (list == null) {
            list = new ArrayList<>();
            this.iconViews.put(key, list);
        }
        list.add(img);
    }

    private static final void bindIcon$placeholder(ImageView $img, int pad, StartScreen this$0, String $fallback) {
        $img.setScaleType(ImageView.ScaleType.FIT_CENTER);
        $img.setPadding(pad, pad, pad, pad);
        $img.setImageDrawable(Icons.general$default(Icons.INSTANCE, this$0.act, $fallback, null, 4, null));
    }

    // Unified card anatomy matching 82312.jpg (G5, G6, G7, G11)
    private final LinearLayout buildCard(
            String title,
            String modifiedText,
            String ownerText,
            String badgeText,
            String iconUrl,
            String fallbackIcon,
            Drawable customThumb,
            long modifiedMs,
            final Function0<Unit> onClick,
            final Function0<Unit> onMenu
    ) {
        int w = dp(this.phone ? 150.0f : 178.0f);
        int inset = dp(6.0f);
        int thumbSize = w - (inset * 2);

        LinearLayout card = new LinearLayout(this.act);
        card.setOrientation(VERTICAL);
        card.setBackground(Ui.INSTANCE.rippleBg(this.act, COL_CARD, 6.0f));
        card.setClickable(true);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.invoke();
            }
        });
        if (onMenu != null) {
            card.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onMenu.invoke();
                    return true;
                }
            });
        }
        card.setTag(new CardMeta(title, badgeText, modifiedMs, ownerText));
        card.setContentDescription(title);

        // 6dp inset 1:1 rounded thumbnail
        FrameLayout thumbBox = new FrameLayout(this.act);
        GradientDrawable thumbBg = new GradientDrawable();
        thumbBg.setColor(0xFF232429);
        thumbBg.setCornerRadius(dp(4.0f));
        thumbBox.setBackground(thumbBg);
        thumbBox.setClipToOutline(true);

        ImageView img = new ImageView(this.act);
        thumbBox.addView(img, new FrameLayout.LayoutParams(-1, -1));
        if (customThumb != null) {
            img.setPadding(0, 0, 0, 0);
            img.setScaleType(ImageView.ScaleType.FIT_XY);
            img.setImageDrawable(customThumb);
        } else {
            bindIcon(img, iconUrl, fallbackIcon);
        }

        if (badgeText != null && badgeText.length() > 0) {
            TextView badge = Ui.INSTANCE.text(this.act, badgeText, 10.0f, COL_TEXT_PRIMARY, true);
            badge.setPadding(dp(7.0f), dp(2.0f), dp(7.0f), dp(2.0f));
            GradientDrawable badgeBg = new GradientDrawable();
            badgeBg.setColor(COL_BADGE_BG);
            badgeBg.setCornerRadius(dp(10.0f));
            badge.setBackground(badgeBg);
            FrameLayout.LayoutParams lpBadge = new FrameLayout.LayoutParams(-2, -2);
            lpBadge.gravity = Gravity.TOP | Gravity.START;
            lpBadge.leftMargin = dp(6.0f);
            lpBadge.topMargin = dp(6.0f);
            thumbBox.addView(badge, lpBadge);
        }

        if (onMenu != null) {
            TextView overflow = Ui.INSTANCE.text(this.act, "⋮", 14.0f, COL_TEXT_PRIMARY, true);
            overflow.setGravity(Gravity.CENTER);
            GradientDrawable ovBg = new GradientDrawable();
            ovBg.setColor(0xCC050508);
            ovBg.setCornerRadius(dp(12.0f));
            overflow.setBackground(ovBg);
            overflow.setClickable(true);
            overflow.setContentDescription("More options");
            overflow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onMenu.invoke();
                }
            });
            FrameLayout.LayoutParams lpOv = new FrameLayout.LayoutParams(dp(24.0f), dp(24.0f));
            lpOv.gravity = Gravity.TOP | Gravity.END;
            lpOv.rightMargin = dp(6.0f);
            lpOv.topMargin = dp(6.0f);
            thumbBox.addView(overflow, lpOv);
        }

        LinearLayout.LayoutParams lpThumb = new LinearLayout.LayoutParams(thumbSize, thumbSize);
        lpThumb.setMargins(inset, inset, inset, 0);
        card.addView(thumbBox, lpThumb);

        LinearLayout info = new LinearLayout(this.act);
        info.setOrientation(VERTICAL);
        info.setPadding(dp(8.0f), dp(7.0f), dp(8.0f), dp(9.0f));

        TextView tvTitle = Ui.INSTANCE.text(this.act, title, 13.0f, COL_TEXT_CARD, true);
        tvTitle.setSingleLine(false);
        tvTitle.setMaxLines(2);
        tvTitle.setEllipsize(TextUtils.TruncateAt.END);
        info.addView(tvTitle);

        if (modifiedText != null && modifiedText.length() > 0) {
            TextView tvMod = Ui.INSTANCE.text(this.act, modifiedText, 11.0f, COL_TEXT_MUTED, false);
            tvMod.setEllipsize(TextUtils.TruncateAt.END);
            tvMod.setPadding(0, dp(3.0f), 0, 0);
            info.addView(tvMod);
        }

        if (ownerText != null && ownerText.length() > 0) {
            TextView tvOwner = Ui.INSTANCE.text(this.act, ownerText, 11.0f, COL_TEXT_OWNER, false);
            tvOwner.setEllipsize(TextUtils.TruncateAt.END);
            tvOwner.setPadding(0, dp(1.0f), 0, 0);
            info.addView(tvOwner);
        }

        card.addView(info, Ui.lp$default(Ui.INSTANCE, w, -2, 0.0f, 4, null));
        card.setLayoutParams(new LinearLayout.LayoutParams(w, -2));
        return card;
    }

    private final FlowLayout grid(LinearLayout col) {
        FlowLayout f = new FlowLayout(this.act);
        f.setGap(dp(12.0f));
        col.addView(f, Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null));
        this.grids.add(f);
        return f;
    }

    private final LinearLayout row(LinearLayout col) {
        LinearLayout r = new LinearLayout(this.act);
        r.setOrientation(HORIZONTAL);
        HorizontalScrollView hsv = new HorizontalScrollView(this.act);
        hsv.setHorizontalScrollBarEnabled(false);
        hsv.addView(r);
        col.addView(hsv, Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null));
        return r;
    }

    private final void addToRow(LinearLayout r, View v) {
        ViewGroup.LayoutParams lp0 = v.getLayoutParams();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(lp0 != null ? lp0.width : -2, -2);
        lp.rightMargin = dp(12.0f);
        r.addView(v, lp);
    }

    private final View loading(LinearLayout col) {
        ProgressBar pb = new ProgressBar(this.act);
        pb.setIndeterminate(true);
        LinearLayout.LayoutParams lp = Ui.lp$default(Ui.INSTANCE, dp(34.0f), dp(34.0f), 0.0f, 4, null);
        lp.topMargin = dp(18.0f);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        col.addView(pb, lp);
        return pb;
    }

    private final void home() {
        final LinearLayout col = scrollColumn();

        // G4: Seamless "Take a quick tour" hero banner directly on #1F2024
        if (!this.tourDismissed) {
            final FrameLayout heroWrap = new FrameLayout(this.act);
            LinearLayout heroRow = new LinearLayout(this.act);
            heroRow.setOrientation(this.phone ? VERTICAL : HORIZONTAL);
            heroRow.setGravity(Gravity.CENTER_VERTICAL);
            heroRow.setPadding(0, dp(8.0f), 0, dp(12.0f));

            LinearLayout heroText = new LinearLayout(this.act);
            heroText.setOrientation(VERTICAL);
            heroText.addView(Ui.INSTANCE.text(this.act, "Take a quick tour", 22.0f, COL_TEXT_PRIMARY, true));
            TextView heroSub = Ui.INSTANCE.text(this.act, "Learn the basics of Studio by creating your first playable experience in this quick guided tour.", 13.0f, COL_TEXT_SECONDARY, false);
            heroSub.setSingleLine(false);
            heroSub.setLineSpacing(dp(2.0f), 1.0f);
            heroSub.setPadding(0, dp(6.0f), dp(16.0f), dp(14.0f));
            heroText.addView(heroSub);

            TextView startLearn = Ui.INSTANCE.text(this.act, "Start Learning", 13.0f, 0xFFFFFFFF, true);
            startLearn.setPadding(dp(18.0f), dp(9.0f), dp(18.0f), dp(9.0f));
            GradientDrawable slBg = new GradientDrawable();
            slBg.setColor(COL_CTA_BLUE);
            slBg.setCornerRadius(dp(6.0f));
            startLearn.setBackground(slBg);
            startLearn.setClickable(true);
            startLearn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StartScreen.this.host.newFromTemplate(PlaceFactory.Template.BASEPLATE);
                }
            });
            heroText.addView(startLearn, Ui.lp$default(Ui.INSTANCE, -2, -2, 0.0f, 4, null));

            if (this.phone) {
                heroRow.addView(heroText, Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null));
            } else {
                heroRow.addView(heroText, Ui.INSTANCE.lp(0, -2, 1.0f));
            }

            TourCollageView collage = new TourCollageView(this.act);
            LinearLayout.LayoutParams lpCollage = new LinearLayout.LayoutParams(
                    this.phone ? -1 : dp(230.0f),
                    dp(this.phone ? 110.0f : 132.0f)
            );
            if (this.phone) {
                lpCollage.topMargin = dp(12.0f);
            } else {
                lpCollage.leftMargin = dp(12.0f);
            }
            heroRow.addView(collage, lpCollage);

            heroWrap.addView(heroRow, new FrameLayout.LayoutParams(-1, -2));

            TextView dismissBtn = Ui.INSTANCE.text(this.act, "✕", 14.0f, COL_TEXT_MUTED, false);
            dismissBtn.setPadding(dp(8.0f), dp(4.0f), dp(8.0f), dp(4.0f));
            dismissBtn.setClickable(true);
            dismissBtn.setContentDescription("Dismiss tour");
            dismissBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StartScreen.this.tourDismissed = true;
                    StartScreen.this.act.getSharedPreferences(PREFS_START, 0).edit().putBoolean(KEY_TOUR_DISMISSED, true).apply();
                    heroWrap.setVisibility(View.GONE);
                }
            });
            FrameLayout.LayoutParams lpDis = new FrameLayout.LayoutParams(-2, -2);
            lpDis.gravity = Gravity.TOP | Gravity.END;
            heroWrap.addView(dismissBtn, lpDis);

            LinearLayout.LayoutParams lpHero = Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null);
            lpHero.bottomMargin = dp(8.0f);
            col.addView(heroWrap, lpHero);
        }

        // Section 1: My Recent Experiences (See All -> EXPERIENCES)
        heading(col, "My Recent Experiences", EXPERIENCES);
        List<Recents.Entry> recents = recentEntries(12);
        if (recents.isEmpty()) {
            message$default(this, col, "Places and files you open show up here, so you can jump right back in.", null, null, null, 28, null);
        } else {
            LinearLayout rr = row(col);
            for (Pair<Recents.Entry, LinearLayout> p : recentCards(recents)) {
                addToRow(rr, p.component2());
            }
        }

        // Section 2: Open a Template (See All -> TEMPLATES)
        heading(col, "Open a Template", TEMPLATES);
        TextView tmplSub = Ui.INSTANCE.text(this.act, "See what's possible on Roblox in popular genres with our ready-made experiences, or start from scratch.", 12.0f, COL_TEXT_SECONDARY, false);
        tmplSub.setSingleLine(false);
        tmplSub.setPadding(0, 0, 0, dp(10.0f));
        col.addView(tmplSub, Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null));
        LinearLayout tr = row(col);
        for (PlaceFactory.Template t : PlaceFactory.Template.values()) {
            addToRow(tr, templateCard(t));
        }

        // Section 3: My Games (signed-in)
        if (!RobloxApi.INSTANCE.isGuest(this.act)) {
            heading(col, MY_GAMES, MY_GAMES);
            final LinearLayout holder = new LinearLayout(this.act);
            holder.setOrientation(VERTICAL);
            col.addView(holder, Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null));
            long uid = RobloxApi.INSTANCE.userId(this.act);
            List<RobloxCloud.Game> cached = StartCache.INSTANCE.games(this.act, uid, Kind.MINE.getKey());
            if (cached != null) {
                home$fill(holder, this, cached);
            } else {
                final View spin = loading(holder);
                final int myGen = this.gen;
                this.net.execute(new Runnable() {
                    @Override
                    public void run() {
                        StartScreen.home$lambda$77(StartScreen.this, myGen, holder, spin);
                    }
                });
            }
        }
        tip(col);
    }

    private final LinearLayout templateCard(final PlaceFactory.Template t) {
        return buildCard(
                t.getTitle(),
                t.getSubtitle(),
                "",
                "Template",
                null,
                "Place",
                new TemplateThumbDrawable(t),
                0L,
                new Function0<Unit>() {
                    @Override
                    public Unit invoke() {
                        StartScreen.this.host.newFromTemplate(t);
                        return Unit.INSTANCE;
                    }
                },
                null
        );
    }

    private static final void home$fill(LinearLayout holder, StartScreen this$0, List<RobloxCloud.Game> list) {
        holder.removeAllViews();
        if (list.isEmpty()) {
            message$default(this$0, holder, "You don't own any experiences yet. Make one here and publish it with File → Publish to Roblox As.", null, null, null, 28, null);
            return;
        }
        LinearLayout gr = this$0.row(holder);
        for (RobloxCloud.Game g : CollectionsKt.take(list, 12)) {
            this$0.addToRow(gr, this$0.gameCard(g, false));
        }
    }

    static final void home$lambda$77(final StartScreen this$0, final int $myGen, final LinearLayout $holder, final View $spin) {
        List<RobloxCloud.Game> listLoadGames;
        try {
            listLoadGames = this$0.loadGames(Kind.MINE);
        } catch (Exception e) {
            listLoadGames = null;
        }
        final List<RobloxCloud.Game> list = listLoadGames;
        this$0.main.post(new Runnable() {
            @Override
            public void run() {
                if ($myGen == this$0.gen) {
                    $holder.removeView($spin);
                    if (list != null) {
                        home$fill($holder, this$0, list);
                    } else {
                        message$default(this$0, $holder, "Couldn't load your games right now. Open Experiences to try again.", null, null, null, 28, null);
                    }
                }
            }
        });
    }

    private final void tip(LinearLayout col) {
        List<String> tips = CollectionsKt.listOf(new String[]{
            "Long-press an object in the Explorer to drag it into another parent or the Toolbox.",
            "File → Playtest Avatar lets you play as R6, R15, your own avatar or any model you save.",
            "Double-tap a panel's header to float it, or drag it to dock it on another side.",
            "Hold Ctrl or Shift (on a keyboard) and tap to select more than one part.",
            "Test → Play Here starts the playtest right where the camera is looking.",
            "The Toolbox's Creator Store tab can insert free models, meshes, audio and plugins."
        });
        LinearLayout box = new LinearLayout(this.act);
        box.setOrientation(HORIZONTAL);
        box.setGravity(Gravity.CENTER_VERTICAL);
        box.setPadding(dp(14.0f), dp(12.0f), dp(14.0f), dp(12.0f));
        GradientDrawable bg = new GradientDrawable();
        bg.setColor(COL_CARD);
        bg.setCornerRadius(dp(6.0f));
        box.setBackground(bg);
        ImageView iv = new ImageView(this.act);
        iv.setImageDrawable(Icons.general$default(Icons.INSTANCE, this.act, "Info", null, 4, null));
        LinearLayout.LayoutParams lpIcon = Ui.lp$default(Ui.INSTANCE, dp(20.0f), dp(20.0f), 0.0f, 4, null);
        lpIcon.rightMargin = dp(10.0f);
        box.addView(iv, lpIcon);
        TextView tv = Ui.INSTANCE.text(this.act, "Tip: " + tips.get((int) ((System.currentTimeMillis() / 60000) % tips.size())), 12.0f, COL_TEXT_SECONDARY, false);
        tv.setSingleLine(false);
        box.addView(tv, Ui.INSTANCE.lp(0, -2, 1.0f));
        LinearLayout.LayoutParams lpBox = Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null);
        lpBox.topMargin = dp(22.0f);
        col.addView(box, lpBox);
    }

    // G12: Experiences page with 4 sub-tabs, search bar, filter chips, sort toggle
    private final void experiencesPage() {
        final LinearLayout col = scrollColumn();
        pageHeader(col, EXPERIENCES, "Manage your cloud games, shared collaborations, group experiences, and local files.", true, new Function0<Unit>() {
            @Override
            public Unit invoke() {
                StartScreen.this.show(EXPERIENCES);
                return Unit.INSTANCE;
            }
        });

        // 4 sub-tabs: My Games, Shared With Me, Group Games, Local Files
        LinearLayout subTabRow = new LinearLayout(this.act);
        subTabRow.setOrientation(HORIZONTAL);
        String[][] tabs = new String[][]{
            {MY_GAMES, "My Games"},
            {SHARED, "Shared With Me"},
            {GROUPS, "Group Games"},
            {RECENT, "Local Files"}
        };
        for (int i = 0; i < tabs.length; i++) {
            final String tabKey = tabs[i][0];
            String tabLabel = tabs[i][1];
            boolean active = tabKey.equals(this.expSubTab);
            TextView tv = Ui.INSTANCE.text(this.act, tabLabel, 12.5f, active ? COL_TEXT_PRIMARY : COL_TEXT_SECONDARY, active);
            tv.setPadding(dp(12.0f), dp(7.0f), dp(12.0f), dp(7.0f));
            GradientDrawable tbg = new GradientDrawable();
            tbg.setColor(active ? COL_NAV_ACTIVE : COL_SEE_ALL_BG);
            tbg.setStroke(dp(1.0f), active ? COL_CTA_BLUE : COL_SEE_ALL_BORDER);
            tbg.setCornerRadius(dp(6.0f));
            tv.setBackground(tbg);
            tv.setClickable(true);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StartScreen.this.expSubTab = tabKey;
                    StartScreen.this.show(EXPERIENCES);
                }
            });
            LinearLayout.LayoutParams lpTab = Ui.lp$default(Ui.INSTANCE, -2, -2, 0.0f, 4, null);
            lpTab.rightMargin = dp(8.0f);
            subTabRow.addView(tv, lpTab);
        }
        HorizontalScrollView subScroll = new HorizontalScrollView(this.act);
        subScroll.setHorizontalScrollBarEnabled(false);
        subScroll.addView(subTabRow);
        LinearLayout.LayoutParams lpSubScroll = Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null);
        lpSubScroll.bottomMargin = dp(10.0f);
        col.addView(subScroll, lpSubScroll);

        // Filter chips (All, Public, Private, Local) + Sort toggle
        LinearLayout controlsRow = new LinearLayout(this.act);
        controlsRow.setOrientation(HORIZONTAL);
        controlsRow.setGravity(Gravity.CENTER_VERTICAL);
        final String[] filters = new String[]{"All", "Public", "Private", "Local"};
        final List<TextView> chipViews = new ArrayList<>();
        for (int i = 0; i < filters.length; i++) {
            final String f = filters[i];
            final TextView chip = Ui.INSTANCE.text(this.act, f, 11.5f, f.equals(this.filterMode) ? 0xFFFFFFFF : COL_TEXT_SECONDARY, f.equals(this.filterMode));
            chip.setPadding(dp(10.0f), dp(5.0f), dp(10.0f), dp(5.0f));
            styleFilterChip(chip, f.equals(this.filterMode));
            chip.setClickable(true);
            chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StartScreen.this.filterMode = f;
                    for (int k = 0; k < chipViews.size(); k++) {
                        boolean sel = filters[k].equals(f);
                        styleFilterChip(chipViews.get(k), sel);
                    }
                    StartScreen.this.applyFilter();
                }
            });
            LinearLayout.LayoutParams lpChip = Ui.lp$default(Ui.INSTANCE, -2, -2, 0.0f, 4, null);
            lpChip.rightMargin = dp(6.0f);
            controlsRow.addView(chip, lpChip);
            chipViews.add(chip);
        }

        final TextView sortBtn = Ui.INSTANCE.text(this.act, this.sortByName ? "Sort: Name A–Z" : "Sort: Last Modified", 11.5f, COL_SEE_ALL_TEXT, true);
        sortBtn.setPadding(dp(10.0f), dp(5.0f), dp(10.0f), dp(5.0f));
        GradientDrawable sbg = new GradientDrawable();
        sbg.setColor(COL_SEE_ALL_BG);
        sbg.setStroke(dp(1.0f), COL_SEE_ALL_BORDER);
        sbg.setCornerRadius(dp(6.0f));
        sortBtn.setBackground(sbg);
        sortBtn.setClickable(true);
        sortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartScreen.this.sortByName = !StartScreen.this.sortByName;
                sortBtn.setText(StartScreen.this.sortByName ? "Sort: Name A–Z" : "Sort: Last Modified");
                StartScreen.this.applyFilter();
            }
        });
        LinearLayout.LayoutParams lpSort = Ui.lp$default(Ui.INSTANCE, -2, -2, 0.0f, 4, null);
        lpSort.leftMargin = dp(6.0f);
        controlsRow.addView(sortBtn, lpSort);

        HorizontalScrollView ctrlScroll = new HorizontalScrollView(this.act);
        ctrlScroll.setHorizontalScrollBarEnabled(false);
        ctrlScroll.addView(controlsRow);
        LinearLayout.LayoutParams lpCtrl = Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null);
        lpCtrl.bottomMargin = dp(12.0f);
        col.addView(ctrlScroll, lpCtrl);

        if (MY_GAMES.equals(this.expSubTab)) {
            populateCloudGames(col, Kind.MINE);
        } else if (SHARED.equals(this.expSubTab)) {
            populateCloudGames(col, Kind.SHARED);
        } else if (GROUPS.equals(this.expSubTab)) {
            populateGroupGames(col);
        } else {
            List<Recents.Entry> entries = recentEntries(60);
            if (entries.isEmpty()) {
                message$default(this, col, "Places and files you open show up here.", null, null, null, 28, null);
            } else {
                FlowLayout g = grid(col);
                for (Pair<Recents.Entry, LinearLayout> p : recentCards(entries)) {
                    g.addView(p.component2());
                }
            }
        }
        addEmptyFilterView(col);
        applyFilter();
    }

    private final void styleFilterChip(TextView chip, boolean active) {
        chip.setTextColor(active ? 0xFFFFFFFF : COL_TEXT_SECONDARY);
        chip.setTypeface(Typeface.DEFAULT, active ? Typeface.BOLD : Typeface.NORMAL);
        GradientDrawable bg = new GradientDrawable();
        bg.setColor(active ? COL_CTA_BLUE : COL_SEE_ALL_BG);
        bg.setStroke(dp(1.0f), active ? COL_CTA_BLUE : COL_SEE_ALL_BORDER);
        bg.setCornerRadius(dp(14.0f));
        chip.setBackground(bg);
    }

    // G7, G12: Templates page showing all 6 templates
    private final void templatesPage() {
        LinearLayout col = scrollColumn();
        pageHeader(col, TEMPLATES, "Start from a ready-made experience or build from an empty place.", true, null);
        FlowLayout g = grid(col);
        for (PlaceFactory.Template t : PlaceFactory.Template.values()) {
            g.addView(templateCard(t));
        }
        addEmptyFilterView(col);
    }

    // G9, G12: Archive page showing locally archived recents + cloud archived experiences
    private final void archivePage() {
        LinearLayout col = scrollColumn();
        pageHeader(col, ARCHIVE, "Archived cloud experiences and locally archived files.", true, new Function0<Unit>() {
            @Override
            public Unit invoke() {
                StartScreen.this.show(ARCHIVE);
                return Unit.INSTANCE;
            }
        });

        List<Recents.Entry> localArchived = recentArchivedEntries(60);
        if (!localArchived.isEmpty()) {
            heading(col, "Archived Local Files", null);
            FlowLayout lg = grid(col);
            for (Pair<Recents.Entry, LinearLayout> p : recentCards(localArchived)) {
                lg.addView(p.component2());
            }
        }

        heading(col, "Archived Cloud Experiences", null);
        populateCloudGames(col, Kind.ARCHIVED);
        addEmptyFilterView(col);
        applyFilter();
    }

    private final List<Recents.Entry> recentEntries(int limit) {
        long myId = RobloxApi.INSTANCE.userId(this.act);
        List<Recents.Entry> list = Recents.INSTANCE.list(this.act);
        List<Recents.Entry> out = new ArrayList<>();
        for (Recents.Entry entry : list) {
            if (entry.isArchived()) {
                continue;
            }
            if (!entry.isCloud() || entry.getUserId() == 0 || entry.getUserId() == myId || RobloxApi.INSTANCE.isGuest(this.act)) {
                out.add(entry);
            }
        }
        return CollectionsKt.take(out, limit);
    }

    private final List<Recents.Entry> recentArchivedEntries(int limit) {
        long myId = RobloxApi.INSTANCE.userId(this.act);
        List<Recents.Entry> list = Recents.INSTANCE.list(this.act);
        List<Recents.Entry> out = new ArrayList<>();
        for (Recents.Entry entry : list) {
            if (!entry.isArchived()) {
                continue;
            }
            if (!entry.isCloud() || entry.getUserId() == 0 || entry.getUserId() == myId || RobloxApi.INSTANCE.isGuest(this.act)) {
                out.add(entry);
            }
        }
        return CollectionsKt.take(out, limit);
    }

    private final List<Pair<Recents.Entry, LinearLayout>> recentCards(List<Recents.Entry> list) {
        Accounts.Account activeAcc = Accounts.INSTANCE.active(this.act);
        String activeUser = activeAcc != null && activeAcc.getName() != null && activeAcc.getName().length() > 0
                ? "@" + activeAcc.getName()
                : "@Roblox";
        List<Pair<Recents.Entry, LinearLayout>> cards = new ArrayList<>(list.size());
        for (final Recents.Entry entry : list) {
            String title = StringsKt.removeSuffix(StringsKt.removeSuffix(entry.getName(), (CharSequence) ".rbxl"), (CharSequence) ".rbxlx");
            String mod = "Modified " + DateUtils.getRelativeTimeSpanString(entry.getTime());
            String owner = entry.isCloud() ? activeUser : "On this device";
            String badge = entry.isArchived() ? "Archived" : (entry.isCloud() ? "Private" : "Local");
            String iconUrl = entry.isCloud() ? StartCache.INSTANCE.getIcons().get(Long.valueOf(entry.getUniverseId())) : null;
            LinearLayout card = buildCard(
                    title,
                    mod,
                    owner,
                    badge,
                    iconUrl,
                    entry.isCloud() ? "Place" : "Open",
                    null,
                    entry.getTime(),
                    new Function0<Unit>() {
                        @Override
                        public Unit invoke() {
                            StartScreen.this.openRecent(entry);
                            return Unit.INSTANCE;
                        }
                    },
                    new Function0<Unit>() {
                        @Override
                        public Unit invoke() {
                            StartScreen.this.showRecentMenu(entry);
                            return Unit.INSTANCE;
                        }
                    }
            );
            cards.add(TuplesKt.to(entry, card));
        }

        List<Long> missingIds = new ArrayList<>();
        for (Recents.Entry entry : list) {
            if (entry.isCloud() && entry.getUniverseId() != 0 && !StartCache.INSTANCE.getIcons().containsKey(Long.valueOf(entry.getUniverseId()))) {
                if (!missingIds.contains(Long.valueOf(entry.getUniverseId()))) {
                    missingIds.add(Long.valueOf(entry.getUniverseId()));
                }
            }
        }
        if (!missingIds.isEmpty()) {
            final List<Long> ids = missingIds;
            final List<Pair<Recents.Entry, LinearLayout>> cardsRef = cards;
            final int myGen = this.gen;
            this.net.execute(new Runnable() {
                @Override
                public void run() {
                    StartScreen.recentCards$lambda$114(ids, StartScreen.this, myGen, cardsRef);
                }
            });
        }
        return cards;
    }

    // G11: Recent file overflow menu (Open, Rename, Archive/Unarchive, Duplicate, Share / Export, Remove from Recent)
    private final void showRecentMenu(final Recents.Entry e) {
        final String archiveLabel = e.isArchived() ? "Unarchive" : "Archive";
        String[] options = new String[]{
            "Open",
            "Rename",
            archiveLabel,
            "Duplicate",
            "Share / Export",
            "Remove from Recent"
        };
        Dialogs.INSTANCE.themed(this.act)
                .setTitle(e.getName())
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        switch (which) {
                            case 0:
                                StartScreen.this.openRecent(e);
                                break;
                            case 1:
                                StartScreen.this.showRenameRecentDialog(e);
                                break;
                            case 2:
                                Recents.INSTANCE.setArchived(StartScreen.this.act, e, !e.isArchived());
                                StartScreen.this.show(StartScreen.this.page);
                                break;
                            case 3:
                                if (e.isCloud()) {
                                    Toast.makeText(StartScreen.this.act, "Only local files can be duplicated.", Toast.LENGTH_SHORT).show();
                                } else {
                                    StartScreen.this.host.duplicateRecentFile(e);
                                }
                                break;
                            case 4:
                                if (e.isCloud()) {
                                    Toast.makeText(StartScreen.this.act, "Only local files can be shared.", Toast.LENGTH_SHORT).show();
                                } else {
                                    StartScreen.this.host.shareRecentFile(e);
                                }
                                break;
                            case 5:
                                Recents.INSTANCE.remove(StartScreen.this.act, e);
                                StartScreen.this.show(StartScreen.this.page);
                                break;
                            default:
                                break;
                        }
                    }
                })
                .show();
    }

    private final void showRenameRecentDialog(final Recents.Entry e) {
        final EditText input = Ui.input$default(Ui.INSTANCE, this.act, e.getName(), "File name", false, false, 24, null);
        input.setSelectAllOnFocus(true);
        LinearLayout box = new LinearLayout(this.act);
        box.setOrientation(VERTICAL);
        box.setPadding(dp(20.0f), dp(12.0f), dp(20.0f), dp(4.0f));
        box.addView(input, Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null));
        Dialogs.INSTANCE.themed(this.act)
                .setTitle("Rename")
                .setView(box)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newName = input.getText() != null ? input.getText().toString().trim() : "";
                        if (!newName.isEmpty()) {
                            Recents.INSTANCE.rename(StartScreen.this.act, e, newName);
                            StartScreen.this.show(StartScreen.this.page);
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    static final void recentCards$lambda$114(List<Long> $ids, final StartScreen this$0, final int $myGen, final List<Pair<Recents.Entry, LinearLayout>> $cards) {
        Map<Long, String> _map;
        try {
            _map = RobloxCloud.INSTANCE.gameIcons($ids);
        } catch (Exception e) {
            _map = MapsKt.emptyMap();
        }
        final Map<Long, String> icons = _map;
        StartCache.INSTANCE.getIcons().putAll(icons);
        this$0.main.post(new Runnable() {
            @Override
            public void run() {
                if ($myGen != this$0.gen) {
                    return;
                }
                for (Pair<Recents.Entry, LinearLayout> pair : $cards) {
                    Recents.Entry e = pair.component1();
                    LinearLayout v = pair.component2();
                    String url = icons.get(Long.valueOf(e.getUniverseId()));
                    if (url != null) {
                        View child0 = v.getChildAt(0);
                        if (child0 instanceof FrameLayout) {
                            View imgView = ((FrameLayout) child0).getChildAt(0);
                            if (imgView instanceof ImageView) {
                                this$0.bindIcon((ImageView) imgView, url, "Place");
                            }
                        }
                    }
                }
            }
        });
    }

    private final void openRecent(final Recents.Entry e) {
        if (e.isCloud()) {
            if (RobloxApi.INSTANCE.isGuest(this.act)) {
                AccountUi.INSTANCE.signIn(this.act, new Function0<Unit>() {
                    @Override
                    public Unit invoke() {
                        StartScreen.this.openRecent(e);
                        return Unit.INSTANCE;
                    }
                });
                return;
            }
            AccountUi.INSTANCE.openPlace(this.act, this.actions, null, new RobloxCloud.Place(e.getPlaceId(), e.getName(), e.getUniverseId()), null);
            return;
        }
        this.host.openRecentFile(e);
    }

    /* JADX INFO: Access modifiers changed from: private */
    enum Kind {
        MINE("games"),
        SHARED("shared"),
        ARCHIVED("archived");

        private final String key;
        private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries(values());

        public static EnumEntries<Kind> getEntries() {
            return $ENTRIES;
        }

        Kind(String key) {
            this.key = key;
        }

        public final String getKey() {
            return this.key;
        }
    }

    private final List<RobloxCloud.Game> loadGames(Kind kind) throws Exception {
        RobloxCloud.User user = RobloxCloud.INSTANCE.cachedUser(this.act);
        if (user == null) {
            user = RobloxCloud.INSTANCE.currentUser(this.act);
        }
        List<RobloxCloud.Game> list;
        if (kind == Kind.MINE) {
            list = RobloxCloud.INSTANCE.userGames(this.act, user);
        } else if (kind == Kind.SHARED) {
            list = RobloxCloud.INSTANCE.sharedGames(this.act, user);
        } else {
            list = RobloxCloud.INSTANCE.archivedGames(this.act, user);
        }
        List<Long> missing = new ArrayList<>();
        for (RobloxCloud.Game g : list) {
            long uid = g.getUniverseId();
            if (!StartCache.INSTANCE.getIcons().containsKey(Long.valueOf(uid))) {
                missing.add(Long.valueOf(uid));
            }
        }
        if (!missing.isEmpty()) {
            StartCache.INSTANCE.getIcons().putAll(RobloxCloud.INSTANCE.gameIcons(missing));
        }
        StartCache.INSTANCE.putGames(this.act, RobloxApi.INSTANCE.userId(this.act), list, kind.getKey());
        return list;
    }

    private static final long parseIsoDateMs(String iso) {
        if (iso == null || iso.length() < 10) {
            return 0L;
        }
        try {
            String trimmed = iso.length() >= 19 ? iso.substring(0, 19) : iso.substring(0, 10) + "T00:00:00";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date d = sdf.parse(trimmed);
            return d != null ? d.getTime() : 0L;
        } catch (Exception e) {
            return 0L;
        }
    }

    private static final String badgeForGame(RobloxCloud.Game g) {
        if (g.isArchived()) {
            return "Archived";
        }
        String p = g.getPrivacyType();
        if (p != null && p.equalsIgnoreCase("Public")) {
            return "Public";
        }
        return "Private";
    }

    static /* synthetic */ LinearLayout gameCard$default(StartScreen startScreen, RobloxCloud.Game game, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return startScreen.gameCard(game, z);
    }

    private final LinearLayout gameCard(final RobloxCloud.Game game, boolean showOwner) {
        String mod = game.getUpdated().length() > 0
                ? "Modified " + com.goydevv.studrblx.util.KtCompat.substringBefore(AccountUi.INSTANCE.prettyDate(game.getUpdated()), "  ")
                : "Modified recently";
        String owner = game.getOwner().length() > 0 ? "@" + game.getOwner() : "";
        String badge = badgeForGame(game);
        long modMs = parseIsoDateMs(game.getUpdated());
        return buildCard(
                game.getName(),
                mod,
                owner,
                badge,
                StartCache.INSTANCE.getIcons().get(Long.valueOf(game.getUniverseId())),
                "Place",
                null,
                modMs,
                new Function0<Unit>() {
                    @Override
                    public Unit invoke() {
                        AccountUi.INSTANCE.places(StartScreen.this.act, StartScreen.this.studio, StartScreen.this.actions, game, new Function2<RobloxCloud.Game, RobloxCloud.Place, Unit>() {
                            @Override
                            public Unit invoke(RobloxCloud.Game gm, RobloxCloud.Place p) {
                                Intrinsics.checkNotNullParameter(gm, "gm");
                                Intrinsics.checkNotNullParameter(p, "p");
                                AccountUi.INSTANCE.openPlace(StartScreen.this.act, StartScreen.this.actions, gm, p, null);
                                return Unit.INSTANCE;
                            }
                        });
                        return Unit.INSTANCE;
                    }
                },
                new Function0<Unit>() {
                    @Override
                    public Unit invoke() {
                        AccountUi.INSTANCE.places(StartScreen.this.act, StartScreen.this.studio, StartScreen.this.actions, game, null);
                        return Unit.INSTANCE;
                    }
                }
        );
    }

    private final void populateCloudGames(final LinearLayout col, final Kind kind) {
        final boolean showOwner = kind != Kind.MINE;
        if (RobloxApi.INSTANCE.isGuest(this.act)) {
            String msg = kind == Kind.MINE
                    ? "You're using Mobile Studio as a guest. Sign in to see the experiences you own."
                    : (kind == Kind.SHARED
                            ? "You're using Mobile Studio as a guest. Sign in to see the experiences shared with you."
                            : "You're using Mobile Studio as a guest. Sign in to see your archived cloud experiences.");
            message$default(this, col, msg, "Sign in with Roblox", null, new Function0<Unit>() {
                @Override
                public Unit invoke() {
                    AccountUi.signIn$default(AccountUi.INSTANCE, StartScreen.this.act, null, 2, null);
                    return Unit.INSTANCE;
                }
            }, 8, null);
            return;
        }
        final int myGen = this.gen;
        long uid = RobloxApi.INSTANCE.userId(this.act);
        final List<RobloxCloud.Game> cached = StartCache.INSTANCE.games(this.act, uid, kind.getKey());
        final LinearLayout holder = new LinearLayout(this.act);
        holder.setOrientation(VERTICAL);
        col.addView(holder, Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null));
        final String empty;
        if (kind == Kind.MINE) {
            empty = "You don't own any experiences yet. Create one on create.roblox.com (or File → Publish to Roblox As → Create new game) and it will show up here.";
        } else if (kind == Kind.SHARED) {
            empty = "Nothing has been shared with you yet. When someone adds you as a collaborator on their experience, it shows up here.";
        } else {
            empty = "No archived cloud experiences found.";
        }
        if (cached != null) {
            showGames(holder, cached, empty, showOwner);
        }
        final View spinner = cached == null ? loading(col) : null;
        this.net.execute(new Runnable() {
            @Override
            public void run() {
                Object result;
                try {
                    result = StartScreen.this.loadGames(kind);
                } catch (Exception e) {
                    result = ResultKt.createFailure(e);
                }
                final Object finalResult = result;
                StartScreen.this.main.post(new Runnable() {
                    @Override
                    public void run() {
                        if (myGen != StartScreen.this.gen) {
                            return;
                        }
                        if (spinner != null) {
                            col.removeView(spinner);
                        }
                        Throwable err = finalResult instanceof Result.Failure ? ((Result.Failure) finalResult).exception : null;
                        if (err == null) {
                            holder.removeAllViews();
                            Iterator<FlowLayout> it = StartScreen.this.grids.iterator();
                            while (it.hasNext()) {
                                FlowLayout fl = it.next();
                                if (fl.getParent() == null || fl.getParent() == holder) {
                                    it.remove();
                                }
                            }
                            StartScreen.this.showGames(holder, (List<RobloxCloud.Game>) finalResult, empty, showOwner);
                            StartScreen.this.applyFilter();
                            return;
                        }
                        if (cached == null) {
                            StartScreen.this.error(col, err);
                        }
                    }
                });
            }
        });
    }

    private final void error(LinearLayout col, Throwable e) {
        if (!(e instanceof AuthRequiredException)) {
            String message = e.getMessage();
            message(col, "Couldn't load this from Roblox.\n" + (message != null ? message : e), "Try again", "Refresh", new Function0<Unit>() {
                @Override
                public Unit invoke() {
                    StartScreen.this.show(StartScreen.this.page);
                    return Unit.INSTANCE;
                }
            });
        } else {
            String message2 = e.getMessage();
            if (message2 == null) {
                message2 = "Sign in again.";
            }
            message$default(this, col, message2, "Sign in", null, new Function0<Unit>() {
                @Override
                public Unit invoke() {
                    AccountUi.signIn$default(AccountUi.INSTANCE, StartScreen.this.act, null, 2, null);
                    return Unit.INSTANCE;
                }
            }, 8, null);
        }
    }

    static /* synthetic */ void showGames$default(StartScreen startScreen, LinearLayout linearLayout, List list, String str, boolean z, int i, Object obj) {
        if ((i & 8) != 0) {
            z = false;
        }
        startScreen.showGames(linearLayout, list, str, z);
    }

    private final void showGames(LinearLayout col, List<RobloxCloud.Game> list, String empty, boolean showOwner) {
        if (list.isEmpty()) {
            message$default(this, col, empty, null, null, null, 28, null);
            return;
        }
        FlowLayout g = grid(col);
        for (RobloxCloud.Game game : list) {
            g.addView(gameCard(game, showOwner));
        }
    }

    private final void populateGroupGames(final LinearLayout col) {
        if (RobloxApi.INSTANCE.isGuest(this.act)) {
            message$default(this, col, "You're using Mobile Studio as a guest. Sign in to see the group experiences you can edit.", "Sign in with Roblox", null, new Function0<Unit>() {
                @Override
                public Unit invoke() {
                    AccountUi.signIn$default(AccountUi.INSTANCE, StartScreen.this.act, null, 2, null);
                    return Unit.INSTANCE;
                }
            }, 8, null);
            return;
        }
        final int myGen = this.gen;
        final RobloxCloud.User user = RobloxCloud.INSTANCE.cachedUser(this.act);
        final long uid = RobloxApi.INSTANCE.userId(this.act);
        final GroupsView view = new GroupsView(this, col);
        final Pair<Integer, List<Pair<RobloxCloud.Group, List<RobloxCloud.Game>>>> pairGroups = StartCache.INSTANCE.groups(this.act, uid);
        if (pairGroups != null) {
            view.set(pairGroups.getSecond(), pairGroups.getFirst().intValue(), false);
        } else {
            GroupsView.status$default(view, "Loading your groups…", true, null, 4, null);
        }
        this.net.execute(new Runnable() {
            @Override
            public void run() {
                StartScreen.groupGames$lambda$150(StartScreen.this, user, pairGroups, uid, myGen, view, col);
            }
        });
    }

    static final void groupGames$lambda$150(StartScreen this$0, RobloxCloud.User $user, final Pair $cached, long $uid, final int $myGen, final GroupsView $view, final LinearLayout $col) {
        final int missing;
        final StartScreen startScreen = this$0;
        try {
            final RobloxCloud.GroupList gl = RobloxCloud.INSTANCE.allGroups(startScreen.act, $user == null ? RobloxCloud.INSTANCE.currentUser(startScreen.act) : $user);
            final List<RobloxCloud.Group> groupList = CollectionsKt.take(gl.getGroups(), 150);
            final int i = $myGen;
            final GroupsView groupsView = $view;
            startScreen.net.execute(new Runnable() {
                @Override
                public void run() {
                    StartScreen.groupGames$lambda$150$lambda$137(groupList, startScreen, i, groupsView);
                }
            });
            final ConcurrentHashMap<Long, List<RobloxCloud.Game>> found = new ConcurrentHashMap<>();
            final ConcurrentHashMap.KeySetView<Long, Boolean> failed = ConcurrentHashMap.newKeySet();
            final AtomicInteger checked = new AtomicInteger();
            final AtomicReference<Exception> authError = new AtomicReference<>();
            ExecutorService pool = Executors.newFixedThreadPool(4);
            List<Pair<RobloxCloud.Group, List<RobloxCloud.Game>>> prevList = $cached != null ? (List<Pair<RobloxCloud.Group, List<RobloxCloud.Game>>>) $cached.getSecond() : CollectionsKt.<Pair<RobloxCloud.Group, List<RobloxCloud.Game>>>emptyList();
            final Map<Long, List<RobloxCloud.Game>> previous = new LinkedHashMap<>();
            for (Pair<RobloxCloud.Group, List<RobloxCloud.Game>> pair : prevList) {
                previous.put(Long.valueOf(pair.getFirst().getId()), pair.getSecond());
            }
            for (final RobloxCloud.Group gp : groupList) {
                pool.execute(new Runnable() {
                    @Override
                    public void run() {
                        StartScreen.groupGames$lambda$150$lambda$144(authError, i, startScreen, found, gp, gl, failed, checked, groupList, previous, $cached, groupsView);
                    }
                });
            }
            pool.shutdown();
            pool.awaitTermination(4L, TimeUnit.MINUTES);
            Exception exc = authError.get();
            if (exc != null) {
                throw exc;
            }
            final List<Pair<RobloxCloud.Group, List<RobloxCloud.Game>>> snap = groupGames$lambda$150$snapshot(groupList, found, previous, failed);
            StartCache.INSTANCE.putGroups(startScreen.act, $uid, groupList.size(), snap);
            int miss = 0;
            for (RobloxCloud.Group g : groupList) {
                if (failed.contains(Long.valueOf(g.getId())) && found.get(Long.valueOf(g.getId())) == null) {
                    miss++;
                }
            }
            missing = miss;
            startScreen.main.post(new Runnable() {
                @Override
                public void run() {
                    StartScreen.groupGames$lambda$150$lambda$148($myGen, startScreen, $view, snap, groupList, missing);
                }
            });
        } catch (Exception e3) {
            final Exception e2 = e3;
            startScreen.main.post(new Runnable() {
                @Override
                public void run() {
                    StartScreen.groupGames$lambda$150$lambda$149($myGen, startScreen, $view, $cached, $col, e2);
                }
            });
        }
    }

    static final void groupGames$lambda$150$lambda$137(List<RobloxCloud.Group> $groupList, final StartScreen this$0, final int $myGen, final GroupsView $view) {
        Map<Long, String> mapEmptyMap;
        try {
            List<Long> missing = new ArrayList<>();
            for (RobloxCloud.Group gp : $groupList) {
                if (!StartCache.INSTANCE.getGroupIcons().containsKey(Long.valueOf(gp.getId()))) {
                    missing.add(Long.valueOf(gp.getId()));
                }
            }
            mapEmptyMap = RobloxCloud.INSTANCE.groupIcons(CollectionsKt.take(missing, 100));
        } catch (Exception e) {
            mapEmptyMap = MapsKt.emptyMap();
        }
        StartCache.INSTANCE.getGroupIcons().putAll(mapEmptyMap);
        if (mapEmptyMap.isEmpty()) {
            return;
        }
        this$0.main.post(new Runnable() {
            @Override
            public void run() {
                if ($myGen == this$0.gen) {
                    $view.redrawChips();
                }
            }
        });
    }

    private static final List<Pair<RobloxCloud.Group, List<RobloxCloud.Game>>> groupGames$lambda$150$snapshot(
            List<RobloxCloud.Group> list,
            ConcurrentHashMap<Long, List<RobloxCloud.Game>> found,
            Map<Long, ? extends List<RobloxCloud.Game>> prev,
            ConcurrentHashMap.KeySetView<Long, Boolean> failed
    ) {
        List<Pair<RobloxCloud.Group, List<RobloxCloud.Game>>> out = new ArrayList<>();
        for (RobloxCloud.Group group : list) {
            List<RobloxCloud.Game> games = found.get(Long.valueOf(group.getId()));
            if (games == null && failed.contains(Long.valueOf(group.getId()))) {
                games = prev.get(Long.valueOf(group.getId()));
            }
            if (games != null && !games.isEmpty()) {
                out.add(TuplesKt.to(group, games));
            }
        }
        return out;
    }

    static final void groupGames$lambda$150$lambda$144(
            AtomicReference<Exception> $authError,
            final int $myGen,
            final StartScreen this$0,
            ConcurrentHashMap<Long, List<RobloxCloud.Game>> $found,
            RobloxCloud.Group $gp,
            RobloxCloud.GroupList $gl,
            ConcurrentHashMap.KeySetView<Long, Boolean> $failed,
            AtomicInteger $checked,
            final List<RobloxCloud.Group> $groupList,
            Map<Long, List<RobloxCloud.Game>> $previous,
            final Pair $cached,
            final GroupsView $view
    ) {
        if ($authError.get() == null && $myGen == this$0.gen) {
            try {
                $found.put(Long.valueOf($gp.getId()), RobloxCloud.INSTANCE.editableGroupGames(this$0.act, $gp, $gl.getStudioListed().contains(Long.valueOf($gp.getId()))));
            } catch (AuthRequiredException e) {
                $authError.set(e);
            } catch (Exception e2) {
                $failed.add(Long.valueOf($gp.getId()));
            }
            final int n = $checked.incrementAndGet();
            final List<Pair<RobloxCloud.Group, List<RobloxCloud.Game>>> snap = groupGames$lambda$150$snapshot($groupList, $found, $previous, $failed);
            this$0.main.post(new Runnable() {
                @Override
                public void run() {
                    if ($myGen != this$0.gen) {
                        return;
                    }
                    if ($cached == null) {
                        $view.set(snap, $groupList.size(), false);
                    }
                    if (n < $groupList.size()) {
                        GroupsView.status$default($view, "Checked " + n + " of " + $groupList.size() + " groups…", true, null, 4, null);
                    }
                }
            });
        }
    }

    static final void groupGames$lambda$150$lambda$148(int $myGen, final StartScreen this$0, GroupsView $view, List<Pair<RobloxCloud.Group, List<RobloxCloud.Game>>> $result, List<RobloxCloud.Group> $groupList, int $missing) {
        if ($myGen != this$0.gen) {
            return;
        }
        $view.set($result, $groupList.size(), true);
        if ($missing > 0) {
            $view.status("Couldn't check " + $missing + " group" + ($missing == 1 ? "" : "s") + " — Roblox was busy. Tap to try again.", false, new Function0<Unit>() {
                @Override
                public Unit invoke() {
                    this$0.show(this$0.page);
                    return Unit.INSTANCE;
                }
            });
        }
    }

    static final void groupGames$lambda$150$lambda$149(int $myGen, StartScreen this$0, GroupsView $view, Pair $cached, LinearLayout $col, Exception $e) {
        if ($myGen == this$0.gen) {
            GroupsView.status$default($view, null, false, null, 4, null);
            if ($cached == null) {
                this$0.error($col, $e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    final class GroupsView {
        private final LinearLayout area;
        private final LinearLayout chips;
        private Map<Long, ? extends List<RobloxCloud.Game>> gamesOf;
        private List<RobloxCloud.Group> groups;
        private List<Long> shownGames;
        private long shownGroup;
        private final LinearLayout statusRow;
        private final ProgressBar statusSpin;
        private final TextView statusText;
        final /* synthetic */ StartScreen this$0;

        public GroupsView(StartScreen this$0, LinearLayout col) {
            Intrinsics.checkNotNullParameter(col, "col");
            this.this$0 = this$0;
            LinearLayout sr = new LinearLayout(this$0.act);
            sr.setOrientation(HORIZONTAL);
            sr.setGravity(Gravity.CENTER_VERTICAL);
            sr.setPadding(0, this$0.dp(6.0f), 0, this$0.dp(6.0f));
            this.statusRow = sr;
            TextView st = Ui.INSTANCE.text(this$0.act, "", 12.0f, COL_TEXT_SECONDARY, false);
            st.setSingleLine(false);
            this.statusText = st;
            ProgressBar pb = new ProgressBar(this$0.act);
            pb.setIndeterminate(true);
            this.statusSpin = pb;
            LinearLayout ch = new LinearLayout(this$0.act);
            ch.setOrientation(HORIZONTAL);
            this.chips = ch;
            LinearLayout ar = new LinearLayout(this$0.act);
            ar.setOrientation(VERTICAL);
            this.area = ar;
            this.groups = CollectionsKt.emptyList();
            this.gamesOf = MapsKt.emptyMap();
            this.shownGames = CollectionsKt.emptyList();

            LinearLayout.LayoutParams lpSpin = Ui.lp$default(Ui.INSTANCE, this$0.dp(16.0f), this$0.dp(16.0f), 0.0f, 4, null);
            lpSpin.rightMargin = this$0.dp(8.0f);
            this.statusRow.addView(this.statusSpin, lpSpin);
            this.statusRow.addView(this.statusText, Ui.INSTANCE.lp(0, -2, 1.0f));
            col.addView(this.statusRow, Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null));

            HorizontalScrollView hsv = new HorizontalScrollView(this$0.act);
            hsv.setHorizontalScrollBarEnabled(false);
            hsv.addView(this.chips);
            LinearLayout.LayoutParams lpHsv = Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null);
            lpHsv.topMargin = this$0.dp(4.0f);
            col.addView(hsv, lpHsv);
            col.addView(this.area, Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null));
            status$default(this, null, false, null, 4, null);
        }

        public static /* synthetic */ void status$default(GroupsView groupsView, String str, boolean z, Function0 function0, int i, Object obj) {
            if ((i & 4) != 0) {
                function0 = null;
            }
            groupsView.status(str, z, function0);
        }

        public final void status(String text, boolean spin, final Function0<Unit> onTap) {
            this.statusRow.setVisibility(text == null ? View.GONE : View.VISIBLE);
            this.statusText.setText(text != null ? text : "");
            this.statusText.setTextColor(onTap != null ? this.this$0.getLinkColor() : COL_TEXT_SECONDARY);
            if (onTap != null) {
                this.statusRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onTap.invoke();
                    }
                });
            } else {
                this.statusRow.setOnClickListener(null);
                this.statusRow.setClickable(false);
            }
            this.statusSpin.setVisibility(spin ? View.VISIBLE : View.GONE);
        }

        public final void set(List<? extends Pair<RobloxCloud.Group, ? extends List<RobloxCloud.Game>>> editable, int inGroups, boolean done) {
            Intrinsics.checkNotNullParameter(editable, "editable");
            List<RobloxCloud.Group> gs = new ArrayList<>(editable.size());
            Map<Long, List<RobloxCloud.Game>> map = new LinkedHashMap<>();
            for (Pair<RobloxCloud.Group, ? extends List<RobloxCloud.Game>> pair : editable) {
                gs.add(pair.getFirst());
                map.put(Long.valueOf(pair.getFirst().getId()), pair.getSecond());
            }
            this.groups = gs;
            this.gamesOf = map;
            if (done) {
                status$default(this, null, false, null, 4, null);
            }
            if (this.groups.isEmpty()) {
                this.chips.removeAllViews();
                if (done) {
                    this.area.removeAllViews();
                    StartScreen.message$default(this.this$0, this.area, inGroups == 0 ? "You aren't in any groups yet." : "None of your groups have experiences you can edit. Ask a group owner to give your role permission to edit group experiences.", null, null, null, 28, null);
                }
                return;
            }
            boolean foundSel = false;
            for (RobloxCloud.Group g : this.groups) {
                if (g.getId() == this.this$0.selectedGroup) {
                    foundSel = true;
                    break;
                }
            }
            if (!foundSel) {
                this.this$0.selectedGroup = this.groups.get(0).getId();
            }
            drawChips();
            List<RobloxCloud.Game> curGames = this.gamesOf.get(Long.valueOf(this.this$0.selectedGroup));
            if (curGames == null) {
                curGames = CollectionsKt.emptyList();
            }
            List<Long> now = new ArrayList<>(curGames.size());
            for (RobloxCloud.Game gm : curGames) {
                now.add(Long.valueOf(gm.getUniverseId()));
            }
            if (this.shownGroup == this.this$0.selectedGroup && Intrinsics.areEqual(now, this.shownGames)) {
                return;
            }
            for (RobloxCloud.Group g : this.groups) {
                if (g.getId() == this.this$0.selectedGroup) {
                    select(g);
                    return;
                }
            }
            throw new NoSuchElementException("Collection contains no element matching the predicate.");
        }

        public final void redrawChips() {
            if (!this.groups.isEmpty()) {
                drawChips();
            }
        }

        private final void drawChips() {
            this.chips.removeAllViews();
            for (final RobloxCloud.Group x : this.groups) {
                boolean on = x.getId() == this.this$0.selectedGroup;
                LinearLayout chip = new LinearLayout(this.this$0.act);
                chip.setOrientation(HORIZONTAL);
                chip.setGravity(Gravity.CENTER_VERTICAL);
                chip.setPadding(this.this$0.dp(6.0f), this.this$0.dp(5.0f), this.this$0.dp(12.0f), this.this$0.dp(5.0f));
                chip.setBackground(Ui.INSTANCE.rippleBg(this.this$0.act, on ? COL_CTA_BLUE : COL_CARD, 16.0f));
                chip.setClickable(true);
                chip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        GroupsView.this.select(x);
                    }
                });
                ImageView ic = new ImageView(this.this$0.act);
                GradientDrawable icBg = new GradientDrawable();
                icBg.setShape(GradientDrawable.OVAL);
                icBg.setColor(COL_CANVAS);
                ic.setBackground(icBg);
                ic.setClipToOutline(true);
                ic.setScaleType(ImageView.ScaleType.CENTER_CROP);
                String url = StartCache.INSTANCE.getGroupIcons().get(Long.valueOf(x.getId()));
                Bitmap bmp = url != null ? ImageLoader.INSTANCE.getKey("url:" + url) : null;
                if (bmp != null) {
                    ic.setImageBitmap(bmp);
                } else {
                    ic.setImageDrawable(Icons.general$default(Icons.INSTANCE, this.this$0.act, "Group", null, 4, null));
                    ic.setPadding(this.this$0.dp(4.0f), this.this$0.dp(4.0f), this.this$0.dp(4.0f), this.this$0.dp(4.0f));
                    if (url != null) {
                        String key = "url:" + url;
                        List<ImageView> list = this.this$0.iconViews.get(key);
                        if (list == null) {
                            list = new ArrayList<>();
                            this.this$0.iconViews.put(key, list);
                        }
                        list.add(ic);
                    }
                }
                LinearLayout.LayoutParams lpIc = Ui.lp$default(Ui.INSTANCE, this.this$0.dp(24.0f), this.this$0.dp(24.0f), 0.0f, 4, null);
                lpIc.rightMargin = this.this$0.dp(8.0f);
                chip.addView(ic, lpIc);
                chip.addView(Ui.INSTANCE.text(this.this$0.act, x.getName(), 12.5f, on ? 0xFFFFFFFF : COL_TEXT_CARD, on));
                LinearLayout.LayoutParams lpChip = Ui.lp$default(Ui.INSTANCE, -2, -2, 0.0f, 4, null);
                lpChip.rightMargin = this.this$0.dp(8.0f);
                lpChip.bottomMargin = this.this$0.dp(6.0f);
                this.chips.addView(chip, lpChip);
            }
        }

        public final void select(final RobloxCloud.Group gp) {
            Intrinsics.checkNotNullParameter(gp, "gp");
            this.this$0.selectedGroup = gp.getId();
            this.shownGroup = gp.getId();
            List<RobloxCloud.Game> list = this.gamesOf.get(Long.valueOf(gp.getId()));
            if (list == null) {
                list = CollectionsKt.emptyList();
            }
            final List<RobloxCloud.Game> games = list;
            List<Long> ids = new ArrayList<>(games.size());
            List<Long> missing = new ArrayList<>();
            for (RobloxCloud.Game g : games) {
                ids.add(Long.valueOf(g.getUniverseId()));
                if (!StartCache.INSTANCE.getIcons().containsKey(Long.valueOf(g.getUniverseId()))) {
                    missing.add(Long.valueOf(g.getUniverseId()));
                }
            }
            this.shownGames = ids;
            drawChips();
            this.area.removeAllViews();
            this.this$0.grids.clear();
            TextView tvRole = Ui.INSTANCE.text(
                    this.this$0.act,
                    (gp.getRole().length() > 0 ? "Your role: " + gp.getRole() + "  ·  " : "") + games.size() + " experience" + (games.size() != 1 ? "s" : ""),
                    12.0f,
                    COL_TEXT_MUTED,
                    false
            );
            tvRole.setPadding(0, this.this$0.dp(6.0f), 0, this.this$0.dp(10.0f));
            this.area.addView(tvRole);
            final LinearLayout holder = new LinearLayout(this.this$0.act);
            holder.setOrientation(VERTICAL);
            this.area.addView(holder, Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null));
            StartScreen.showGames$default(this.this$0, holder, games, "This group has no experiences you can edit.", false, 8, null);
            this.this$0.applyFilter();
            if (missing.isEmpty()) {
                return;
            }
            final List<Long> missIds = missing;
            final int myGen = this.this$0.gen;
            final StartScreen ss = this.this$0;
            this.this$0.net.execute(new Runnable() {
                @Override
                public void run() {
                    Map<Long, String> map;
                    try {
                        map = RobloxCloud.INSTANCE.gameIcons(missIds);
                    } catch (Exception e) {
                        map = MapsKt.emptyMap();
                    }
                    final Map<Long, String> icons = map;
                    StartCache.INSTANCE.getIcons().putAll(icons);
                    ss.main.post(new Runnable() {
                        @Override
                        public void run() {
                            if (myGen == ss.gen && ss.selectedGroup == gp.getId() && !icons.isEmpty()) {
                                holder.removeAllViews();
                                ss.grids.clear();
                                StartScreen.showGames$default(ss, holder, games, "This group has no experiences you can edit.", false, 8, null);
                                ss.applyFilter();
                            }
                        }
                    });
                }
            });
        }
    }
}

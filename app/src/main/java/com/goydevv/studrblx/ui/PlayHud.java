package com.goydevv.studrblx.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import com.goydevv.studrblx.core.Settings;
import com.goydevv.studrblx.core.Studio;
import com.goydevv.studrblx.engine.PlaySession;
import com.goydevv.studrblx.model.Instance;
import com.goydevv.studrblx.model.PartData;
import com.goydevv.studrblx.model.Vector3;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

/* JADX INFO: compiled from: PlayHud.kt */
/* JADX INFO: loaded from: classes5.dex */
public final class PlayHud extends FrameLayout {
    private final Typeface bold;
    private final EditText chatBar;
    private final LinearLayout chatBox;
    private final TopButton chatBtn;
    private final ChatLog chatLog;
    private boolean chatOpen;
    private int cutB;
    private int cutL;
    private int cutR;
    private int cutT;
    private final float d;
    private View expDropdown;
    private boolean inventoryOpen = true;
    private long lastChatActivity;
    private final Layer layer;
    private final FrameLayout.LayoutParams lpChatBox;
    private View menu;
    private final TopButton menuBtn;
    private final TopButton hamburgerBtn;
    private final TopButton micBtn;
    private final LinearLayout topPill;
    private boolean micMuted = true;
    private boolean playerListOpen;
    private final Typeface regular;
    private int seenChat;
    private final Studio studio;
    private boolean unreadChat;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public PlayHud(Context ctx, Studio studio) {
        super(ctx);
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(studio, "studio");
        this.studio = studio;
        this.d = getResources().getDisplayMetrics().density;
        this.bold = Fonts.INSTANCE.forFamily("BuilderSans", 700, false).getTypeface();
        this.regular = Fonts.INSTANCE.forFamily("BuilderSans", 400, false).getTypeface();
        this.layer = new Layer(this, ctx);
        this.menuBtn = new TopButton(this, ctx, 0);
        this.hamburgerBtn = new TopButton(this, ctx, 3);
        this.chatBtn = new TopButton(this, ctx, 1);
        this.micBtn = new TopButton(this, ctx, 4);
        this.topPill = new LinearLayout(ctx);
        this.chatBox = new LinearLayout(ctx);
        this.chatLog = new ChatLog(this, ctx);
        this.chatOpen = true;
        this.playerListOpen = true;
        this.seenChat = -1;
        setVisibility(8);
        addView(this.layer, new FrameLayout.LayoutParams(-1, -1));
        final int top = dp(7.0f);
        final FrameLayout.LayoutParams lpMenu = new FrameLayout.LayoutParams(dp(44.0f), dp(44.0f));
        lpMenu.leftMargin = dp(12.0f) + this.cutL;
        lpMenu.topMargin = top + this.cutT;
        addView(this.menuBtn, lpMenu);

        // Unified Experience Controls capsule pill [≡ | Chat | Mic] matching Roblox Experience Controls (82314.png)
        this.topPill.setOrientation(0);
        this.topPill.setGravity(16);
        GradientDrawable pillBg = new GradientDrawable();
        pillBg.setColor(0xE0111318);
        pillBg.setCornerRadius(dp(22.0f));
        this.topPill.setBackground(pillBg);
        this.topPill.setPadding(dp(6.0f), 0, dp(6.0f), 0);
        this.topPill.addView(this.hamburgerBtn, new LinearLayout.LayoutParams(dp(44.0f), dp(44.0f)));
        this.topPill.addView(this.chatBtn, new LinearLayout.LayoutParams(dp(44.0f), dp(44.0f)));
        this.topPill.addView(this.micBtn, new LinearLayout.LayoutParams(dp(44.0f), dp(44.0f)));
        final FrameLayout.LayoutParams lpPill = new FrameLayout.LayoutParams(-2, dp(44.0f));
        lpPill.leftMargin = dp(68.0f) + this.cutL;
        lpPill.topMargin = top + this.cutT;
        addView(this.topPill, lpPill);

        this.hamburgerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayHud.this.toggleExpDropdown();
            }
        });
        this.micBtn.setActive(false);
        this.micBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayHud.this.micMuted = !PlayHud.this.micMuted;
                PlayHud.this.micBtn.setActive(!PlayHud.this.micMuted);
                PlayHud.this.micBtn.invalidate();
            }
        });
        // Respect Android DisplayCutout / Safe Area insets
        setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
            @Override
            public android.view.WindowInsets onApplyWindowInsets(View v, android.view.WindowInsets insets) {
                int cL = 0;
                int cR = 0;
                int cT = 0;
                int cB = 0;
                if (android.os.Build.VERSION.SDK_INT >= 28 && insets != null && insets.getDisplayCutout() != null) {
                    cL = insets.getDisplayCutout().getSafeInsetLeft();
                    cR = insets.getDisplayCutout().getSafeInsetRight();
                    cT = insets.getDisplayCutout().getSafeInsetTop();
                    cB = insets.getDisplayCutout().getSafeInsetBottom();
                }
                PlayHud.this.cutL = cL;
                PlayHud.this.cutR = cR;
                PlayHud.this.cutT = cT;
                PlayHud.this.cutB = cB;
                lpMenu.leftMargin = dp(12.0f) + cL;
                lpMenu.topMargin = top + cT;
                lpPill.leftMargin = dp(68.0f) + cL;
                lpPill.topMargin = top + cT;
                PlayHud.this.lpChatBox.leftMargin = dp(12.0f) + cL;
                PlayHud.this.lpChatBox.topMargin = dp(58.0f) + cT;
                PlayHud.this.menuBtn.setLayoutParams(lpMenu);
                PlayHud.this.topPill.setLayoutParams(lpPill);
                PlayHud.this.chatBox.setLayoutParams(PlayHud.this.lpChatBox);
                PlayHud.this.layer.invalidate();
                return insets;
            }
        });
        this.menuBtn.setOnClickListener(new View.OnClickListener() { // from class: com.goydevv.studrblx.ui.PlayHud$$ExternalSyntheticLambda4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PlayHud.this.toggleMenu();
            }
        });
        this.chatBtn.setOnClickListener(new View.OnClickListener() { // from class: com.goydevv.studrblx.ui.PlayHud$$ExternalSyntheticLambda5
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PlayHud._init_$lambda$4(PlayHud.this, view);
            }
        });
        this.chatBox.setOrientation(1);
        LinearLayout linearLayout = this.chatBox;
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(1291845632);
        gradientDrawable.setCornerRadius(dp(8.0f));
        linearLayout.setBackground(gradientDrawable);
        this.chatBox.setPadding(dp(6.0f), dp(6.0f), dp(6.0f), dp(6.0f));
        this.chatBox.addView(this.chatLog, new LinearLayout.LayoutParams(-1, 0, 1.0f));
        EditText editText = new EditText(ctx);
        editText.setHint("Tap here to chat");
        editText.setHintTextColor(-1275068417);
        editText.setTextColor(-1);
        editText.setTextSize(14.0f);
        editText.setTypeface(this.regular);
        editText.setSingleLine(true);
        editText.setImeOptions(268435460);
        GradientDrawable gradientDrawable2 = new GradientDrawable();
        gradientDrawable2.setColor(1711276032);
        gradientDrawable2.setCornerRadius(dp(6.0f));
        editText.setBackground(gradientDrawable2);
        editText.setPadding(dp(10.0f), dp(7.0f), dp(10.0f), dp(7.0f));
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: com.goydevv.studrblx.ui.PlayHud$$ExternalSyntheticLambda7
            @Override // android.widget.TextView.OnEditorActionListener
            public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                return PlayHud.lambda$10$lambda$8(PlayHud.this, textView, i, keyEvent);
            }
        });
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: com.goydevv.studrblx.ui.PlayHud$$ExternalSyntheticLambda8
            @Override // android.view.View.OnFocusChangeListener
            public final void onFocusChange(View view, boolean z) {
                PlayHud.lambda$10$lambda$9(PlayHud.this, view, z);
            }
        });
        this.chatBar = editText;
        LinearLayout linearLayout2 = this.chatBox;
        EditText editText2 = this.chatBar;
        LinearLayout.LayoutParams layoutParams4 = new LinearLayout.LayoutParams(-1, -2);
        layoutParams4.topMargin = dp(4.0f);
        Unit unit4 = Unit.INSTANCE;
        linearLayout2.addView(editText2, layoutParams4);
        boolean phone = getResources().getConfiguration().smallestScreenWidthDp < 600;
        LinearLayout linearLayout3 = this.chatBox;
        this.lpChatBox = new FrameLayout.LayoutParams(dp(phone ? 270.0f : 330.0f), dp(phone ? 130.0f : 165.0f));
        this.lpChatBox.leftMargin = dp(12.0f) + this.cutL;
        this.lpChatBox.topMargin = dp(58.0f) + this.cutT;
        Unit unit5 = Unit.INSTANCE;
        addView(linearLayout3, this.lpChatBox);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final int dp(float v) {
        return (int) (this.d * v);
    }

    static final void _init_$lambda$4(PlayHud this$0, View it) {
        this$0.chatOpen = !this$0.chatOpen;
        if (this$0.chatOpen) {
            this$0.unreadChat = false;
        } else {
            this$0.hideKeyboard();
        }
        this$0.updateChatVisibility();
        this$0.lastChatActivity = System.currentTimeMillis();
    }

    static final boolean lambda$10$lambda$8(PlayHud this$0, TextView textView, int id, KeyEvent ev) {
        if (id != 4 && id != 6) {
            if (!(ev != null && ev.getKeyCode() == 66) || ev.getAction() != 0) {
                return false;
            }
        }
        this$0.send();
        return true;
    }

    static final void lambda$10$lambda$9(PlayHud this$0, View view, boolean f) {
        if (f) {
            this$0.lastChatActivity = System.currentTimeMillis();
        }
    }

    private final void send() {
        String text = this.chatBar.getText().toString();
        this.chatBar.setText("");
        if (!StringsKt.isBlank(text)) {
            synchronized (this.studio.getLock()) {
                PlaySession play = this.studio.getPlay();
                if (play != null) {
                    play.sendChat(text);
                    Unit unit = Unit.INSTANCE;
                }
            }
        }
        hideKeyboard();
        this.chatBar.clearFocus();
        this.lastChatActivity = System.currentTimeMillis();
    }

    private final void hideKeyboard() {
        Object systemService = getContext().getSystemService("input_method");
        Intrinsics.checkNotNull(systemService, "null cannot be cast to non-null type android.view.inputmethod.InputMethodManager");
        ((InputMethodManager) systemService).hideSoftInputFromWindow(this.chatBar.getWindowToken(), 0);
    }

    public final void onPlayStateChanged() {
        boolean playing = this.studio.isPlaying();
        setVisibility(playing ? 0 : 8);
        if (playing) {
            this.lastChatActivity = System.currentTimeMillis();
            this.seenChat = -1;
            this.unreadChat = false;
        } else {
            closeExpDropdown();
            closeMenu();
            hideKeyboard();
            this.chatBar.setText("");
            this.unreadChat = false;
        }
        updateChatVisibility();
    }

    private final void updateChatVisibility() {
        PlaySession p = this.studio.getPlay();
        boolean enabled = !((p == null || p.getCoreChat()) ? false : true);
        this.chatBtn.setVisibility(enabled ? 0 : 8);
        this.chatBox.setVisibility((enabled && this.chatOpen) ? 0 : 8);
        this.chatBtn.setActive(this.chatOpen);
        this.chatBtn.invalidate();
    }

    public final void onFrame() {
        PlaySession p;
        if (getVisibility() == 0 && (p = this.studio.getPlay()) != null) {
            int v = p.getChatVersion();
            if (v != this.seenChat) {
                boolean hadSeen = this.seenChat != -1;
                this.seenChat = v;
                this.lastChatActivity = System.currentTimeMillis();
                this.chatLog.reload();
                if (hadSeen && !this.chatOpen && !p.getChatLines().isEmpty()) {
                    this.unreadChat = true;
                    this.chatBtn.invalidate();
                }
            }
            boolean idle = System.currentTimeMillis() - this.lastChatActivity > 12000 && !this.chatBar.hasFocus();
            this.chatBox.setAlpha(idle ? 0.35f : 1.0f);
            if (this.chatBtn.getVisibility() != (p.getCoreChat() ? 0 : 8)) {
                updateChatVisibility();
            }
            this.layer.invalidate();
        }
    }

    public final void togglePlayerList() {
        this.playerListOpen = !this.playerListOpen;
        this.layer.invalidate();
    }

    public final boolean isInventoryOpen() {
        return this.inventoryOpen;
    }

    public final void toggleInventory() {
        this.inventoryOpen = !this.inventoryOpen;
        if (getParent() instanceof ViewportView) {
            ((ViewportView) getParent()).getControls().invalidate();
        }
    }

    public final void toggleExpDropdown() {
        if (this.expDropdown != null) {
            closeExpDropdown();
        } else {
            openExpDropdown();
        }
    }

    private final void closeExpDropdown() {
        View view = this.expDropdown;
        if (view != null) {
            removeView(view);
        }
        this.expDropdown = null;
    }

    private final void openExpDropdown() {
        closeExpDropdown();
        hideKeyboard();
        FrameLayout scrim = new FrameLayout(getContext());
        scrim.setClickable(true);
        scrim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayHud.this.closeExpDropdown();
            }
        });
        final LinearLayout card = new LinearLayout(getContext());
        card.setOrientation(1);
        card.setClickable(true);
        GradientDrawable cardBg = new GradientDrawable();
        cardBg.setColor(0xE0111318);
        cardBg.setCornerRadius(dp(12.0f));
        card.setBackground(cardBg);
        card.setPadding(0, dp(4.0f), 0, dp(4.0f));
        populateExpMainRows(card);
        FrameLayout.LayoutParams lpCard = new FrameLayout.LayoutParams(dp(188.0f), -2);
        lpCard.leftMargin = dp(68.0f) + this.cutL;
        lpCard.topMargin = dp(58.0f) + this.cutT;
        scrim.addView(card, lpCard);
        addView(scrim, new FrameLayout.LayoutParams(-1, -1));
        this.expDropdown = scrim;
    }

    private final void addExpDropdownRow(LinearLayout card, String title, String trailing, boolean showDivider, View.OnClickListener listener) {
        LinearLayout row = new LinearLayout(getContext());
        row.setOrientation(0);
        row.setGravity(16);
        row.setClickable(true);
        row.setPadding(dp(14.0f), dp(11.0f), dp(14.0f), dp(11.0f));
        TextView titleTv = label(title, 14.0f, -1, true);
        row.addView(titleTv, new LinearLayout.LayoutParams(0, -2, 1.0f));
        if (trailing != null && trailing.length() > 0) {
            TextView trailingTv = label(trailing, 13.0f, 0xB3FFFFFF, false);
            row.addView(trailingTv);
        }
        row.setOnClickListener(listener);
        card.addView(row, new LinearLayout.LayoutParams(-1, -2));
        if (showDivider) {
            View div = new View(getContext());
            div.setBackgroundColor(0x22FFFFFF);
            card.addView(div, new LinearLayout.LayoutParams(-1, 1));
        }
    }

    private final void populateExpMainRows(final LinearLayout card) {
        card.removeAllViews();
        addExpDropdownRow(card, "Leaderboard", this.playerListOpen ? "On" : "Off", true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayHud.this.togglePlayerList();
                PlayHud.this.closeExpDropdown();
            }
        });
        addExpDropdownRow(card, "Emotes", "›", true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayHud.this.populateExpEmoteRows(card);
            }
        });
        addExpDropdownRow(card, "Inventory", this.inventoryOpen ? "On" : "Off", true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayHud.this.toggleInventory();
                PlayHud.this.closeExpDropdown();
            }
        });
        addExpDropdownRow(card, "Respawn", null, true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                synchronized (PlayHud.this.studio.getLock()) {
                    PlaySession play = PlayHud.this.studio.getPlay();
                    if (play != null) {
                        play.resetCharacter();
                    }
                }
                PlayHud.this.closeExpDropdown();
            }
        });
        addExpDropdownRow(card, "Settings", null, false, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayHud.this.closeExpDropdown();
                PlayHud.this.openMenu(1);
            }
        });
    }

    private final void populateExpEmoteRows(final LinearLayout card) {
        card.removeAllViews();
        final String[][] emotes = new String[][]{
            {"Wave", "wave"},
            {"Point", "point"},
            {"Cheer", "cheer"},
            {"Dance", "dance"}
        };
        for (int i = 0; i < emotes.length; i++) {
            final String emoteKey = emotes[i][1];
            addExpDropdownRow(card, emotes[i][0], null, true, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    synchronized (PlayHud.this.studio.getLock()) {
                        PlaySession play = PlayHud.this.studio.getPlay();
                        if (play != null) {
                            play.playEmote(emoteKey);
                        }
                    }
                    PlayHud.this.closeExpDropdown();
                }
            });
        }
        addExpDropdownRow(card, "‹ Back", null, false, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayHud.this.populateExpMainRows(card);
            }
        });
    }

    public final void focusChat() {
        PlaySession play = this.studio.getPlay();
        if ((play == null || play.getCoreChat()) ? false : true) {
            return;
        }
        this.chatOpen = true;
        this.unreadChat = false;
        updateChatVisibility();
        this.chatBar.requestFocus();
        Object systemService = getContext().getSystemService("input_method");
        Intrinsics.checkNotNull(systemService, "null cannot be cast to non-null type android.view.inputmethod.InputMethodManager");
        ((InputMethodManager) systemService).showSoftInput(this.chatBar, 0);
    }

    public final boolean handleBack() {
        if (this.expDropdown != null) {
            closeExpDropdown();
            return true;
        }
        if (this.menu != null) {
            closeMenu();
            return true;
        }
        if (!this.chatBar.hasFocus()) {
            return false;
        }
        hideKeyboard();
        this.chatBar.clearFocus();
        return true;
    }

    /* JADX INFO: compiled from: PlayHud.kt */
    private final class TopButton extends View {
        private boolean active;
        private final int kind;
        private final Paint p;
        final /* synthetic */ PlayHud this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public TopButton(PlayHud this$0, Context ctx, int kind) {
            super(ctx);
            String str;
            Intrinsics.checkNotNullParameter(ctx, "ctx");
            this.this$0 = this$0;
            this.kind = kind;
            this.active = true;
            this.p = new Paint(1);
            setClickable(true);
            switch (this.kind) {
                case 0:
                    str = "Roblox menu";
                    break;
                case 1:
                    str = "Chat";
                    break;
                case 3:
                    str = "Experience controls";
                    break;
                case 4:
                    str = "Microphone";
                    break;
                default:
                    str = "Leaderboard";
                    break;
            }
            setContentDescription(str);
        }

        public final int getKind() {
            return this.kind;
        }

        public final boolean getActive() {
            return this.active;
        }

        public final void setActive(boolean z) {
            this.active = z;
        }

        @Override // android.view.View
        protected void onDraw(Canvas c) {
            Intrinsics.checkNotNullParameter(c, "c");
            float cx = getWidth() / 2.0f;
            float cy = getHeight() / 2.0f;
            float d = this.this$0.d;
            boolean inCapsule = (this.kind == 1 || this.kind == 3 || this.kind == 4);
            if (!inCapsule) {
                float r = Math.min(getWidth(), getHeight()) / 2.0f;
                this.p.setStyle(Paint.Style.FILL);
                this.p.setColor(isPressed() ? 0xEE2A2E38 : 0xE0111318);
                c.drawCircle(cx, cy, r - (d * 0.6f), this.p);
            } else if (isPressed()) {
                this.p.setStyle(Paint.Style.FILL);
                this.p.setColor(0x33FFFFFF);
                c.drawCircle(cx, cy, d * 16.0f, this.p);
            }
            this.p.setStyle(Paint.Style.FILL);
            this.p.setColor(-1);
            switch (this.kind) {
                case 0:
                    // Official Roblox tilted solid square with inner cutout (matching 82314.png)
                    c.save();
                    c.rotate(14.5f, cx, cy);
                    float s = d * 10.2f;
                    this.p.setStyle(Paint.Style.FILL);
                    this.p.setColor(-1);
                    c.drawRoundRect(cx - s, cy - s, cx + s, cy + s, d * 1.6f, d * 1.6f, this.p);
                    this.p.setColor(0xFF111318);
                    float h = d * 3.1f;
                    c.drawRect(cx - h, cy - h, cx + h, cy + h, this.p);
                    c.restore();
                    break;
                case 1:
                    // Outlined Roblox chat bubble with two interior lines (matching 82314.png)
                    this.p.setAlpha(this.active ? 255 : 155);
                    this.p.setStyle(Paint.Style.STROKE);
                    this.p.setStrokeWidth(d * 1.7f);
                    this.p.setStrokeCap(Paint.Cap.ROUND);
                    this.p.setStrokeJoin(Paint.Join.ROUND);
                    c.drawRoundRect(cx - (d * 8.2f), cy - (d * 6.8f), cx + (d * 8.2f), cy + (d * 4.6f), d * 2.5f, d * 2.5f, this.p);
                    c.drawLine(cx - (d * 4.2f), cy - (d * 2.4f), cx + (d * 4.2f), cy - (d * 2.4f), this.p);
                    c.drawLine(cx - (d * 4.2f), cy + (d * 1.0f), cx + (d * 1.5f), cy + (d * 1.0f), this.p);
                    c.drawLine(cx - (d * 2.0f), cy + (d * 4.8f), cx, cy + (d * 8.0f), this.p);
                    c.drawLine(cx, cy + (d * 8.0f), cx + (d * 2.5f), cy + (d * 4.8f), this.p);
                    this.p.setStyle(Paint.Style.FILL);
                    this.p.setAlpha(255);
                    if (this.this$0.unreadChat) {
                        float bx = cx + (d * 7.2f);
                        float by = cy - (d * 6.5f);
                        this.p.setColor(0xFFFFFFFF);
                        c.drawCircle(bx, by, d * 4.2f, this.p);
                        this.p.setColor(0xFFE53935);
                        c.drawCircle(bx, by, d * 3.0f, this.p);
                        this.p.setColor(-1);
                    }
                    break;
                case 3:
                    // Hamburger menu (3 crisp horizontal lines matching 82314.png)
                    this.p.setStyle(Paint.Style.STROKE);
                    this.p.setStrokeWidth(d * 1.85f);
                    this.p.setStrokeCap(Paint.Cap.ROUND);
                    c.drawLine(cx - (d * 7.8f), cy - (d * 5.2f), cx + (d * 7.8f), cy - (d * 5.2f), this.p);
                    c.drawLine(cx - (d * 7.8f), cy, cx + (d * 7.8f), cy, this.p);
                    c.drawLine(cx - (d * 7.8f), cy + (d * 5.2f), cx + (d * 7.8f), cy + (d * 5.2f), this.p);
                    this.p.setStyle(Paint.Style.FILL);
                    break;
                case 4:
                    // Microphone / Voice toggle with diagonal slash when muted (matching 82314.png)
                    this.p.setStyle(Paint.Style.STROKE);
                    this.p.setStrokeWidth(d * 1.65f);
                    this.p.setStrokeCap(Paint.Cap.ROUND);
                    c.drawRoundRect(cx - (d * 3.4f), cy - (d * 8.0f), cx + (d * 3.4f), cy + (d * 1.2f), d * 3.4f, d * 3.4f, this.p);
                    c.drawArc(cx - (d * 6.2f), cy - (d * 3.5f), cx + (d * 6.2f), cy + (d * 4.8f), 0.0f, 180.0f, false, this.p);
                    c.drawLine(cx, cy + (d * 4.8f), cx, cy + (d * 8.2f), this.p);
                    c.drawLine(cx - (d * 3.2f), cy + (d * 8.2f), cx + (d * 3.2f), cy + (d * 8.2f), this.p);
                    if (!this.active) {
                        c.drawLine(cx - (d * 7.2f), cy + (d * 7.2f), cx + (d * 7.2f), cy - (d * 7.2f), this.p);
                    }
                    this.p.setStyle(Paint.Style.FILL);
                    break;
                default:
                    if (!this.active) {
                        this.p.setAlpha(150);
                    }
                    c.drawCircle(cx - (this.this$0.d * 4.0f), cy - (this.this$0.d * 4.0f), this.this$0.d * 3.2f, this.p);
                    c.drawCircle((this.this$0.d * 4.5f) + cx, cy - (this.this$0.d * 3.0f), this.this$0.d * 2.8f, this.p);
                    c.drawRoundRect(cx - (this.this$0.d * 10.0f), (this.this$0.d * 0.5f) + cy, (this.this$0.d * 2.0f) + cx, cy + (this.this$0.d * 9.0f), this.this$0.d * 4.0f, this.this$0.d * 4.0f, this.p);
                    c.drawRoundRect(cx + (this.this$0.d * 0.0f), cy + (this.this$0.d * 1.5f), cx + (this.this$0.d * 10.0f), cy + (this.this$0.d * 8.0f), this.this$0.d * 3.5f, this.this$0.d * 3.5f, this.p);
                    this.p.setAlpha(255);
                    break;
            }
        }

        @Override // android.view.View
        protected void dispatchSetPressed(boolean pressed) {
            invalidate();
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent e) {
            Intrinsics.checkNotNullParameter(e, "e");
            boolean r = super.onTouchEvent(e);
            invalidate();
            return r;
        }
    }

    /* JADX INFO: compiled from: PlayHud.kt */
    private final class ChatLog extends View {
        private int lastW;
        private List<? extends StaticLayout> layouts;
        private List<PlaySession.ChatLine> lines;
        private final TextPaint shadow;
        final /* synthetic */ PlayHud this$0;
        private final TextPaint tp;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public ChatLog(PlayHud this$0, Context ctx) {
            super(ctx);
            Intrinsics.checkNotNullParameter(ctx, "ctx");
            this.this$0 = this$0;
            TextPaint textPaint = new TextPaint(1);
            PlayHud playHud = this.this$0;
            textPaint.setColor(-1);
            textPaint.setTextSize(playHud.d * 14.0f);
            textPaint.setTypeface(playHud.regular);
            this.tp = textPaint;
            TextPaint textPaint2 = new TextPaint(this.tp);
            textPaint2.setColor(-872415232);
            this.shadow = textPaint2;
            this.layouts = CollectionsKt.emptyList();
            this.lines = CollectionsKt.emptyList();
        }

        public final void reload() {
            List<PlaySession.ChatLine> list;
            ArrayList<PlaySession.ChatLine> chatLines;
            List listTakeLast;
            Object lock = this.this$0.studio.getLock();
            PlayHud playHud = this.this$0;
            synchronized (lock) {
                PlaySession play = playHud.studio.getPlay();
                list = (play == null || (chatLines = play.getChatLines()) == null || (listTakeLast = CollectionsKt.takeLast(chatLines, 60)) == null) ? null : CollectionsKt.toList(listTakeLast);
            }
            if (list == null) {
                list = CollectionsKt.emptyList();
            }
            this.lines = list;
            build();
            invalidate();
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r14v0 */
        /* JADX WARN: Type inference failed for: r14v1, types: [boolean, int] */
        /* JADX WARN: Type inference failed for: r14v8 */
        private final void build() {
            int r14;
            int w = getWidth() - this.this$0.dp(4.0f);
            if (w <= 0) {
                this.layouts = CollectionsKt.emptyList();
                return;
            }
            this.lastW = getWidth();
            Iterable<PlaySession.ChatLine> iterable = this.lines;
            Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
            for (PlaySession.ChatLine chatLine : iterable) {
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                if (chatLine.getFrom() != null) {
                    int length = spannableStringBuilder.length();
                    spannableStringBuilder.append((CharSequence) ("[" + chatLine.getFrom() + "]: "));
                    spannableStringBuilder.setSpan(new ForegroundColorSpan(chatLine.getColor()), length, spannableStringBuilder.length(), 33);
                    spannableStringBuilder.setSpan(new StyleSpan(1), length, spannableStringBuilder.length(), 33);
                    spannableStringBuilder.append((CharSequence) chatLine.getText());
                    r14 = 0;
                } else {
                    spannableStringBuilder.append((CharSequence) chatLine.getText());
                    r14 = 0;
                    spannableStringBuilder.setSpan(new ForegroundColorSpan(chatLine.getColor()), 0, spannableStringBuilder.length(), 33);
                }
                arrayList.add(StaticLayout.Builder.obtain(spannableStringBuilder, r14, spannableStringBuilder.length(), this.tp, w).setAlignment(Layout.Alignment.ALIGN_NORMAL).setIncludePad(r14 != 0).build());
            }
            this.layouts = (List) arrayList;
        }

        @Override // android.view.View
        protected void onDraw(Canvas c) {
            Intrinsics.checkNotNullParameter(c, "c");
            if (getWidth() != this.lastW) {
                build();
            }
            float y = getHeight();
            c.save();
            c.clipRect(0, 0, getWidth(), getHeight());
            for (Object _lObj : CollectionsKt.asReversed(this.layouts)) {
            StaticLayout l = (StaticLayout) _lObj;
                y -= l.getHeight() + this.this$0.dp(2.0f);
                if (l.getHeight() + y < 0.0f) {
                    break;
                }
                c.save();
                c.translate(this.this$0.dp(2.0f), y);
                l.draw(c);
                c.restore();
            }
            c.restore();
        }
    }

    /* JADX INFO: compiled from: PlayHud.kt */
    private final class Layer extends View {
        private final Paint bp;
        private final Paint fill;
        final /* synthetic */ PlayHud this$0;
        private final TextPaint tp;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public Layer(PlayHud this$0, Context ctx) {
            super(ctx);
            Intrinsics.checkNotNullParameter(ctx, "ctx");
            this.this$0 = this$0;
            this.fill = new Paint(1);
            TextPaint textPaint = new TextPaint(1);
            PlayHud playHud = this.this$0;
            textPaint.setTextSize(playHud.d * 14.0f);
            textPaint.setTypeface(playHud.regular);
            this.tp = textPaint;
            Paint paint = new Paint(1);
            PlayHud playHud2 = this.this$0;
            paint.setTextSize(playHud2.d * 13.0f);
            paint.setTypeface(playHud2.bold);
            paint.setColor(-1);
            this.bp = paint;
        }

        @Override // android.view.View
        protected void onDraw(Canvas c) {
            Intrinsics.checkNotNullParameter(c, "c");
            Object lock = this.this$0.studio.getLock();
            PlayHud playHud = this.this$0;
            synchronized (lock) {
                PlaySession play = playHud.studio.getPlay();
                if (play == null) {
                    return;
                }
                drawBubbles(c, play);
                if (play.getCorePlayerList() && playHud.playerListOpen) {
                    drawPlayerList(c, play);
                }
                if (play.getCoreHealth()) {
                    drawHealth(c, play);
                }
                if (play.getShiftLock() && !play.getFirstPerson()) {
                    drawCursor(c);
                } else if (play.getFirstPerson()) {
                    drawDot(c);
                }
                Unit unit = Unit.INSTANCE;
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r13v10 */
        /* JADX WARN: Type inference failed for: r13v3 */
        /* JADX WARN: Type inference failed for: r13v4, types: [boolean, int] */
        /* JADX WARN: Type inference failed for: r26v0 */
        /* JADX WARN: Type inference failed for: r26v1 */
        /* JADX WARN: Type inference failed for: r26v2 */
        /* JADX WARN: Type inference failed for: r26v3 */
        /* JADX WARN: Type inference failed for: r26v4 */
        /* JADX WARN: Type inference failed for: r26v5 */
        /* JADX WARN: Type inference failed for: r26v6 */
        /* JADX WARN: Type inference failed for: r26v7 */
        /* JADX WARN: Type inference failed for: r3v10, types: [android.text.StaticLayout$Builder] */
        private final void drawBubbles(Canvas c, PlaySession p) {
            ArrayList arrayList;
            if (p.getBubbles().isEmpty()) {
                return;
            }
            int iDp = this.this$0.dp(220.0f);
            ArrayList<PlaySession.Bubble> bubbles = p.getBubbles();
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            for (Object obj : bubbles) {
                Instance part = ((PlaySession.Bubble) obj).getPart();
                Object obj2 = linkedHashMap.get(part);
                if (obj2 == null) {
                    arrayList = new ArrayList();
                    linkedHashMap.put(part, arrayList);
                } else {
                    arrayList = (ArrayList) obj2;
                }
                ((List) arrayList).add(obj);
            }
            Iterator it = linkedHashMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                Instance instance = (Instance) entry.getKey();
                List list = (List) entry.getValue();
                float f = 2.0f;
                float f2 = 0.0f;
                Vector3 vector3Plus = PartData.INSTANCE.cframe(instance).getPosition().plus(new Vector3(0.0f, (PartData.INSTANCE.size(instance).getY() / 2.0f) + 1.6f, 0.0f));
                float[] fArrProject = this.this$0.studio.getView().project(vector3Plus);
                if (fArrProject != null) {
                    int i = 1065353216;
                    if (fArrProject[2] <= 1.0f) {
                        float fDistTo = this.this$0.studio.getView().distTo(vector3Plus);
                        if (fDistTo <= 100.0f) {
                            float fDp = fArrProject[1];
                            int r13 = 0;
                            int i2 = 0;
                            for (Object _bObj : CollectionsKt.asReversed(list)) {
                                PlaySession.Bubble bubble = (PlaySession.Bubble) _bObj;
                                int i3 = i2;
                                i2++;
                                float f3 = f;
                                float fCoerceIn = RangesKt.coerceIn(bubble.getAge() > 11.0f ? 12.0f - bubble.getAge() : i, f2, (float) i) * (i3 == 0 ? i : 1061997773);
                                int i4 = i;
                                this.tp.setColor((((int) (fCoerceIn * 255.0f)) << 24) | 3750717);
                                Iterator it2 = it;
                                StaticLayout staticLayoutBuild = StaticLayout.Builder.obtain(bubble.getText(), r13, bubble.getText().length(), this.tp, iDp).setAlignment(Layout.Alignment.ALIGN_CENTER).setIncludePad(r13 != 0).build();
                                Intrinsics.checkNotNullExpressionValue(staticLayoutBuild, "build(...)");
                                float fMax = 0.0f;
                                int i5 = 0;
                                int r26 = r13;
                                int lineCount = staticLayoutBuild.getLineCount();
                                while (i5 < lineCount) {
                                    fMax = Math.max(fMax, staticLayoutBuild.getLineWidth(i5));
                                    i5++;
                                }
                                List list2 = list;
                                int iDp2 = this.this$0.dp(10.0f);
                                int iDp3 = this.this$0.dp(6.0f);
                                float f4 = (iDp2 * 2) + fMax;
                                LinkedHashMap linkedHashMap2 = linkedHashMap;
                                Instance instance2 = instance;
                                Vector3 vector3 = vector3Plus;
                                float[] fArr = fArrProject;
                                float f5 = fDistTo;
                                RectF rectF = new RectF(fArrProject[r26] - (f4 / f3), (fDp - (staticLayoutBuild.getHeight() + (iDp3 * f3))) - this.this$0.dp(10.0f), fArrProject[r26] + (f4 / f3), fDp - this.this$0.dp(10.0f));
                                this.fill.setColor((((int) ((0.92f * fCoerceIn) * 255.0f)) << 24) | ViewCompat.MEASURED_SIZE_MASK);
                                c.drawRoundRect(rectF, this.this$0.dp(12.0f), this.this$0.dp(12.0f), this.fill);
                                if (i3 == 0) {
                                    Path path = new Path();
                                    PlayHud playHud = this.this$0;
                                    path.moveTo(fArr[r26] - playHud.dp(7.0f), rectF.bottom - i4);
                                    path.lineTo(fArr[r26], rectF.bottom + playHud.dp(8.0f));
                                    path.lineTo(fArr[r26] + playHud.dp(7.0f), rectF.bottom - i4);
                                    path.close();
                                    c.drawPath(path, this.fill);
                                }
                                c.save();
                                c.translate(fArr[r26] - (iDp / f3), rectF.top + iDp3);
                                staticLayoutBuild.draw(c);
                                c.restore();
                                fDp = rectF.top - this.this$0.dp(f3);
                                f = f3;
                                i = i4;
                                it = it2;
                                r13 = r26;
                                list = list2;
                                fDistTo = f5;
                                instance = instance2;
                                linkedHashMap = linkedHashMap2;
                                vector3Plus = vector3;
                                fArrProject = fArr;
                                f2 = 0.0f;
                            }
                        }
                    }
                }
            }
        }

        /* JADX WARN: Code duplicated, block: B:29:0x01df  */
        private final void drawPlayerList(Canvas c, PlaySession p) {
            String name;
            Instance player = p.getPlayer();
            if (player == null) {
                return;
            }
            List<Pair<String, String>> listLeaderstats = p.leaderstats();
            float fDp = this.this$0.dp(76.0f);
            float fDp2 = this.this$0.dp(150.0f);
            float size = (listLeaderstats.size() * fDp) + fDp2 + this.this$0.dp(16.0f);
            float fDp3 = this.this$0.dp(32.0f);
            float width = (getWidth() - size) - (this.this$0.dp(12.0f) + this.this$0.cutR);
            float fDp4 = this.this$0.dp(58.0f) + this.this$0.cutT;
            this.fill.setColor(Integer.MIN_VALUE);
            c.drawRoundRect(width, fDp4, width + size, (((!listLeaderstats.isEmpty() ? 1 : 0) + 1) * fDp3) + fDp4 + this.this$0.dp(8.0f), this.this$0.dp(8.0f), this.this$0.dp(8.0f), this.fill);
            float fDp5 = fDp4 + this.this$0.dp(4.0f);
            this.bp.setTextAlign(Paint.Align.LEFT);
            int i = 10;
            if (!listLeaderstats.isEmpty()) {
                this.bp.setColor(-3618616);
                this.bp.setTextSize(this.this$0.d * 12.0f);
                float f = 0.62f;
                c.drawText("People", width + this.this$0.dp(10.0f), (fDp3 * 0.62f) + fDp5, this.bp);
                this.bp.setTextAlign(Paint.Align.CENTER);
                List<Pair<String, String>> list = listLeaderstats;
                int i2 = 0;
                for (Object obj : list) {
                    int i3 = i2 + 1;
                    if (i2 < 0) {
                        CollectionsKt.throwIndexOverflow();
                    }
                    float f2 = f;
                    c.drawText(StringsKt.take((String) ((Pair) obj).component1(), i), width + fDp2 + (i2 * fDp) + (fDp / 2.0f), fDp5 + (fDp3 * f2), this.bp);
                    i2 = i3;
                    f = f2;
                    i = 10;
                }
                fDp5 += fDp3;
            }
            this.fill.setColor(872415231);
            c.drawRoundRect(width + this.this$0.dp(4.0f), fDp5, (width + size) - this.this$0.dp(4.0f), fDp5 + fDp3, this.this$0.dp(6.0f), this.this$0.dp(6.0f), this.fill);
            this.fill.setColor(p.nameColor(player.getName()));
            c.drawCircle(width + this.this$0.dp(20.0f), (fDp3 / 2.0f) + fDp5, this.this$0.dp(10.0f), this.fill);
            this.bp.setColor(-1);
            this.bp.setTextSize(this.this$0.d * 14.0f);
            this.bp.setTextAlign(Paint.Align.LEFT);
            Object obj2 = player.get("DisplayName");
            String str = obj2 instanceof String ? (String) obj2 : null;
            if (str != null) {
                String name2 = str;
                if (name2.length() == 0) {
                    name2 = player.getName();
                }
                name = name2;
                if (name == null) {
                    name = player.getName();
                }
            } else {
                name = player.getName();
            }
            float f3 = 0.65f;
            c.drawText(StringsKt.take(name, 16), width + this.this$0.dp(36.0f), (fDp3 * 0.65f) + fDp5, this.bp);
            this.bp.setTextAlign(Paint.Align.CENTER);
            List<Pair<String, String>> list2 = listLeaderstats;
            int i4 = 0;
            for (Object obj3 : list2) {
                int i5 = i4 + 1;
                if (i4 < 0) {
                    CollectionsKt.throwIndexOverflow();
                }
                float f4 = f3;
                float f5 = fDp5;
                c.drawText(StringsKt.take((String) ((Pair) obj3).component2(), 10), width + fDp2 + (i4 * fDp) + (fDp / 2.0f), f5 + (fDp3 * f4), this.bp);
                i4 = i5;
                f3 = f4;
                fDp5 = f5;
            }
        }

        /* JADX WARN: Code duplicated, block: B:28:0x005f  */
        private final void drawHealth(Canvas c, PlaySession p) {
            Instance hum;
            float max;
            int i;
            Instance character = p.getCharacter();
            if (character == null || (hum = character.findFirstChildOfClass("Humanoid")) == null) {
                return;
            }
            Object obj = hum.get("Health_XML");
            if (obj == null) {
                obj = hum.get("Health");
            }
            Number number = obj instanceof Number ? (Number) obj : null;
            if (number == null) {
                return;
            }
            float hp = number.floatValue();
            Object obj2 = hum.get("MaxHealth");
            Number number2 = obj2 instanceof Number ? (Number) obj2 : null;
            if (number2 != null) {
                Float fValueOf = Float.valueOf(number2.floatValue());
                Float f = (fValueOf.floatValue() > 0.0f ? 1 : (fValueOf.floatValue() == 0.0f ? 0 : -1)) > 0 ? fValueOf : null;
                if (f != null) {
                    max = f.floatValue();
                } else {
                    max = 100.0f;
                }
            } else {
                max = 100.0f;
            }
            if (hp >= max) {
                return;
            }
            float f2 = RangesKt.coerceIn(hp / max, 0.0f, 1.0f);
            float w = this.this$0.dp(120.0f);
            float h = this.this$0.dp(8.0f);
            float x = (getWidth() - w) - (this.this$0.dp(12.0f) + this.this$0.cutR);
            float y = this.this$0.dp(20.0f) + this.this$0.cutT;
            this.fill.setColor(Integer.MIN_VALUE);
            c.drawRoundRect(x, y, x + w, y + h, h / 2.0f, h / 2.0f, this.fill);
            Paint paint = this.fill;
            if (f2 > 0.5f) {
                i = -13905573;
            } else {
                i = f2 > 0.25f ? -670440 : IconPainter.RED;
            }
            paint.setColor(i);
            if (f2 > 0.0f) {
                c.drawRoundRect(x, y, x + (w * f2), y + h, h / 2.0f, h / 2.0f, this.fill);
            }
        }

        private final void drawCursor(Canvas c) {
            float cx = (getWidth() / 2.0f) + 0.0f;
            float cy = getHeight() / 2.0f;
            this.fill.setStyle(Paint.Style.STROKE);
            this.fill.setStrokeWidth(this.this$0.d * 2.0f);
            this.fill.setColor(-419430401);
            c.drawCircle(cx, cy, this.this$0.d * 9.0f, this.fill);
            this.fill.setStyle(Paint.Style.FILL);
            c.drawCircle(cx, cy, this.this$0.d * 2.0f, this.fill);
        }

        private final void drawDot(Canvas c) {
            this.fill.setStyle(Paint.Style.FILL);
            this.fill.setColor(-855638017);
            c.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f, this.this$0.d * 2.5f, this.fill);
        }
    }

    public final void toggleMenu() {
        if (this.menu != null) {
            closeMenu();
        } else {
            openMenu();
        }
    }

    private final void closeMenu() {
        View view = this.menu;
        if (view != null) {
            removeView(view);
        }
        this.menu = null;
    }

    private final TextView label(String s, float size, int color, boolean b) {
        TextView textView = new TextView(getContext());
        textView.setText(s);
        textView.setTextSize(size);
        textView.setTextColor(color);
        textView.setTypeface(b ? this.bold : this.regular);
        return textView;
    }

    static /* synthetic */ TextView label$default(PlayHud playHud, String str, float f, int i, boolean z, int i2, Object obj) {
        if ((i2 & 4) != 0) {
            i = -1;
        }
        if ((i2 & 8) != 0) {
            z = false;
        }
        return playHud.label(str, f, i, z);
    }

    private final TextView pill(String s, boolean primary, final Function0<Unit> onClick) {
        TextView textViewLabel = label(s, 15.0f, primary ? ViewCompat.MEASURED_STATE_MASK : -1, true);
        textViewLabel.setGravity(17);
        textViewLabel.setPadding(dp(18.0f), dp(11.0f), dp(18.0f), dp(11.0f));
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(primary ? -1 : 872415231);
        gradientDrawable.setCornerRadius(dp(8.0f));
        textViewLabel.setBackground(gradientDrawable);
        textViewLabel.setClickable(true);
        textViewLabel.setOnClickListener(new View.OnClickListener() { // from class: com.goydevv.studrblx.ui.PlayHud$$ExternalSyntheticLambda19
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                onClick.invoke();
            }
        });
        return textViewLabel;
    }

    private final void openMenu() {
        openMenu(0);
    }

    private final void openMenu(int initialTab) {
        closeExpDropdown();
        hideKeyboard();
        final FrameLayout root = new FrameLayout(getContext());
        root.setBackgroundColor(-1291845632);
        boolean z = true;
        root.setClickable(true);
        LinearLayout panel = new LinearLayout(getContext());
        panel.setOrientation(1);
        panel.setPadding(dp(20.0f), dp(14.0f), dp(20.0f), dp(16.0f));
        LinearLayout tabs = new LinearLayout(getContext());
        tabs.setOrientation(0);
        tabs.setGravity(17);
        final FrameLayout content = new FrameLayout(getContext());
        List tabNames = CollectionsKt.listOf(new String[]{"People", "Settings", "Help"});
        final ArrayList tabViews = new ArrayList();
        int i = 0;
        for (Object obj : tabNames) {
            int i2 = i + 1;
            if (i < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            TextView textViewLabel = label((String) obj, 15.0f, -1, z);
            textViewLabel.setPadding(dp(18.0f), dp(9.0f), dp(18.0f), dp(9.0f));
            final int tabIdx = i;
            textViewLabel.setOnClickListener(new View.OnClickListener() { // from class: com.goydevv.studrblx.ui.PlayHud$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    PlayHud.openMenu$select(tabViews, content, PlayHud.this, tabIdx);
                }
            });
            tabViews.add(textViewLabel);
            tabs.addView(textViewLabel);
            i = i2;
            z = true;
        }
        panel.addView(tabs, new LinearLayout.LayoutParams(-1, -2));
        View view = new View(getContext());
        view.setBackgroundColor(872415231);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, dp(1.0f));
        layoutParams.topMargin = dp(8.0f);
        layoutParams.bottomMargin = dp(8.0f);
        Unit unit = Unit.INSTANCE;
        panel.addView(view, layoutParams);
        panel.addView(content, new LinearLayout.LayoutParams(-1, 0, 1.0f));
        LinearLayout bottom = new LinearLayout(getContext());
        bottom.setOrientation(0);
        bottom.setGravity(17);
        TextView textViewPill = pill("Reset Character", false, new Function0() { // from class: com.goydevv.studrblx.ui.PlayHud$$ExternalSyntheticLambda1
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return PlayHud.openMenu$lambda$32(PlayHud.this, root);
            }
        });
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(0, -2, 1.0f);
        layoutParams2.rightMargin = dp(8.0f);
        Unit unit2 = Unit.INSTANCE;
        bottom.addView(textViewPill, layoutParams2);
        TextView textViewPill2 = pill("Leave", false, new Function0() { // from class: com.goydevv.studrblx.ui.PlayHud$$ExternalSyntheticLambda2
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return PlayHud.openMenu$lambda$35(PlayHud.this, root);
            }
        });
        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(0, -2, 1.0f);
        layoutParams3.rightMargin = dp(8.0f);
        Unit unit3 = Unit.INSTANCE;
        bottom.addView(textViewPill2, layoutParams3);
        bottom.addView(pill("Resume", true, new Function0() { // from class: com.goydevv.studrblx.ui.PlayHud$$ExternalSyntheticLambda3
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return PlayHud.openMenu$lambda$37(PlayHud.this);
            }
        }), new LinearLayout.LayoutParams(0, -2, 1.0f));
        LinearLayout.LayoutParams layoutParams4 = new LinearLayout.LayoutParams(-1, -2);
        layoutParams4.topMargin = dp(10.0f);
        Unit unit4 = Unit.INSTANCE;
        panel.addView(bottom, layoutParams4);
        int maxW = dp(560.0f);
        root.addView(panel, new FrameLayout.LayoutParams(Math.min(maxW, RangesKt.coerceAtLeast((int) (getWidth() * 0.94f), dp(280.0f))), (int) (getHeight() * 0.9f), 17));
        addView(root, new FrameLayout.LayoutParams(-1, -1));
        this.menu = root;
        openMenu$select(tabViews, content, this, initialTab);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void openMenu$select(ArrayList<TextView> arrayList, FrameLayout content, PlayHud this$0, int i) {
        View viewPeoplePage;
        GradientDrawable gradientDrawable;
        int i2 = 0;
        for (Object obj : arrayList) {
            int i3 = i2 + 1;
            if (i2 < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            TextView textView = (TextView) obj;
            textView.setTextColor(i == i2 ? -1 : -1711276033);
            if (i == i2) {
                GradientDrawable gradientDrawable2 = new GradientDrawable();
                gradientDrawable2.setColor(654311423);
                gradientDrawable2.setCornerRadius(this$0.dp(8.0f));
                gradientDrawable = gradientDrawable2;
            } else {
                gradientDrawable = null;
            }
            textView.setBackground(gradientDrawable);
            i2 = i3;
        }
        content.removeAllViews();
        switch (i) {
            case 0:
                viewPeoplePage = this$0.peoplePage();
                break;
            case 1:
                viewPeoplePage = this$0.settingsPage();
                break;
            default:
                viewPeoplePage = this$0.helpPage();
                break;
        }
        content.addView(viewPeoplePage);
    }

    static final Unit openMenu$lambda$32(final PlayHud this$0, FrameLayout $root) {
        this$0.confirmPage($root, "Reset Character?", "Are you sure you want to reset your character?", "Reset", new Function0() { // from class: com.goydevv.studrblx.ui.PlayHud$$ExternalSyntheticLambda21
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return PlayHud.openMenu$lambda$32$lambda$31(this$0);
            }
        });
        return Unit.INSTANCE;
    }

    static final Unit openMenu$lambda$32$lambda$31(PlayHud this$0) {
        synchronized (this$0.studio.getLock()) {
            PlaySession play = this$0.studio.getPlay();
            if (play != null) {
                play.resetCharacter();
                Unit unit = Unit.INSTANCE;
            }
        }
        this$0.closeMenu();
        return Unit.INSTANCE;
    }

    static final Unit openMenu$lambda$35(final PlayHud this$0, FrameLayout $root) {
        this$0.confirmPage($root, "Leave Experience?", "Are you sure you want to leave the experience?", "Leave", new Function0() { // from class: com.goydevv.studrblx.ui.PlayHud$$ExternalSyntheticLambda20
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return PlayHud.openMenu$lambda$35$lambda$34(this$0);
            }
        });
        return Unit.INSTANCE;
    }

    static final Unit openMenu$lambda$35$lambda$34(PlayHud this$0) {
        this$0.closeMenu();
        this$0.studio.stopPlay();
        return Unit.INSTANCE;
    }

    static final Unit openMenu$lambda$37(PlayHud this$0) {
        this$0.closeMenu();
        return Unit.INSTANCE;
    }

    private final void confirmPage(final FrameLayout root, String title, String msg, String ok, final Function0<Unit> onOk) {
        final FrameLayout frameLayout = new FrameLayout(getContext());
        frameLayout.setBackgroundColor(-872415232);
        frameLayout.setClickable(true);
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(1);
        linearLayout.setGravity(1);
        linearLayout.setPadding(dp(24.0f), dp(20.0f), dp(24.0f), dp(20.0f));
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(-14473945);
        gradientDrawable.setCornerRadius(dp(12.0f));
        linearLayout.setBackground(gradientDrawable);
        TextView textViewLabel$default = label$default(this, title, 20.0f, 0, true, 4, null);
        textViewLabel$default.setGravity(17);
        linearLayout.addView(textViewLabel$default);
        TextView textViewLabel$default2 = label$default(this, msg, 15.0f, -3092272, false, 8, null);
        textViewLabel$default2.setGravity(17);
        textViewLabel$default2.setPadding(0, dp(10.0f), 0, dp(16.0f));
        linearLayout.addView(textViewLabel$default2);
        LinearLayout row = new LinearLayout(getContext());
        row.setOrientation(0);
        TextView textViewPill = pill(ok, true, new Function0() { // from class: com.goydevv.studrblx.ui.PlayHud$$ExternalSyntheticLambda16
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return PlayHud.confirmPage$lambda$45(root, frameLayout, onOk);
            }
        });
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, -2, 1.0f);
        layoutParams.rightMargin = dp(8.0f);
        Unit unit = Unit.INSTANCE;
        row.addView(textViewPill, layoutParams);
        row.addView(pill("Don't " + ok, false, new Function0() { // from class: com.goydevv.studrblx.ui.PlayHud$$ExternalSyntheticLambda17
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return PlayHud.confirmPage$lambda$47(root, frameLayout);
            }
        }), new LinearLayout.LayoutParams(0, -2, 1.0f));
        linearLayout.addView(row, new LinearLayout.LayoutParams(-1, -2));
        frameLayout.addView(linearLayout, new FrameLayout.LayoutParams(Math.min(dp(420.0f), (int) (getWidth() * 0.9f)), -2, 17));
        root.addView(frameLayout, new FrameLayout.LayoutParams(-1, -1));
    }

    static final Unit confirmPage$lambda$45(FrameLayout $root, FrameLayout $shade, Function0 $onOk) {
        $root.removeView($shade);
        $onOk.invoke();
        return Unit.INSTANCE;
    }

    static final Unit confirmPage$lambda$47(FrameLayout $root, FrameLayout $shade) {
        $root.removeView($shade);
        return Unit.INSTANCE;
    }

    /* JADX WARN: Code duplicated, block: B:20:0x00e3  */
    private final View peoplePage() {
        String name;
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(1);
        PlaySession p = this.studio.getPlay();
        Instance pl = p != null ? p.getPlayer() : null;
        if (pl != null) {
            LinearLayout linearLayout2 = new LinearLayout(getContext());
            linearLayout2.setOrientation(0);
            linearLayout2.setGravity(16);
            linearLayout2.setPadding(dp(12.0f), dp(10.0f), dp(12.0f), dp(10.0f));
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setColor(452984831);
            gradientDrawable.setCornerRadius(dp(8.0f));
            linearLayout2.setBackground(gradientDrawable);
            View view = new View(getContext());
            GradientDrawable gradientDrawable2 = new GradientDrawable();
            gradientDrawable2.setShape(1);
            gradientDrawable2.setColor(p.nameColor(pl.getName()));
            view.setBackground(gradientDrawable2);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dp(36.0f), dp(36.0f));
            layoutParams.rightMargin = dp(12.0f);
            Unit unit = Unit.INSTANCE;
            linearLayout2.addView(view, layoutParams);
            LinearLayout linearLayout3 = new LinearLayout(getContext());
            linearLayout3.setOrientation(1);
            Object obj = pl.get("DisplayName");
            String str = obj instanceof String ? (String) obj : null;
            if (str != null) {
                String name2 = str;
                if (name2.length() == 0) {
                    name2 = pl.getName();
                }
                name = name2;
                if (name == null) {
                    name = pl.getName();
                }
            } else {
                name = pl.getName();
            }
            linearLayout3.addView(label$default(this, name, 16.0f, 0, true, 4, null));
            linearLayout3.addView(label$default(this, "@" + pl.getName() + "  ·  You", 13.0f, -1275068417, false, 8, null));
            linearLayout2.addView(linearLayout3);
            linearLayout.addView(linearLayout2);
        }
        TextView textViewLabel$default = label$default(this, "Playtest in Mobile Studio: only you are in this server.", 13.0f, -1711276033, false, 8, null);
        textViewLabel$default.setPadding(dp(4.0f), dp(12.0f), dp(4.0f), 0);
        linearLayout.addView(textViewLabel$default);
        ScrollView scrollView = new ScrollView(getContext());
        scrollView.addView(linearLayout);
        return scrollView;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final View settingsPage() {
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(1);
        PlaySession play = this.studio.getPlay();
        boolean objArr = ((play != null && !play.getShiftLockAllowed()) && Settings.INSTANCE.getShiftLockSwitch()) ? false : true;
        Instance instanceFindService = this.studio.getModel().findService("StarterPlayer");
        if (!(instanceFindService != null ? Intrinsics.areEqual(instanceFindService.get("EnableMouseLockOption"), (Object) false) : false)) {
            settingsPage$row(this, linearLayout, "Shift Lock Switch", settingsPage$selector(this, CollectionsKt.listOf(new String[]{"Off", "On"}), Settings.INSTANCE.getShiftLockSwitch() ? 1 : 0, new Function1() { // from class: com.goydevv.studrblx.ui.PlayHud$$ExternalSyntheticLambda9
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return PlayHud.settingsPage$lambda$68(PlayHud.this, ((Integer) obj).intValue());
                }
            }));
        }
        if (objArr == false) {
            linearLayout.addView(label$default(this, "This experience turned shift lock off.", 12.0f, -1711276033, false, 8, null));
        }
        settingsPage$row(this, linearLayout, "Camera Mode", settingsPage$selector(this, CollectionsKt.listOf(new String[]{"Classic", "Follow"}), !Intrinsics.areEqual(Settings.INSTANCE.getCameraMode(), "Classic") ? 1 : 0, new Function1() { // from class: com.goydevv.studrblx.ui.PlayHud$$ExternalSyntheticLambda10
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return PlayHud.settingsPage$lambda$69(((Integer) obj).intValue());
            }
        }));
        settingsPage$row(this, linearLayout, "Movement Mode", settingsPage$selector(this, CollectionsKt.listOf(new String[]{"Dynamic Thumbstick", "Thumbstick"}), Intrinsics.areEqual(Settings.INSTANCE.getMovementMode(), "Thumbstick") ? 1 : 0, new Function1() { // from class: com.goydevv.studrblx.ui.PlayHud$$ExternalSyntheticLambda11
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return PlayHud.settingsPage$lambda$70(((Integer) obj).intValue());
            }
        }));
        settingsPage$row(this, linearLayout, "Camera Sensitivity", settingsPage$slider(this, Settings.INSTANCE.getPlayCameraSensitivity(), 0.2f, 4.0f, new Function1() { // from class: com.goydevv.studrblx.ui.PlayHud$$ExternalSyntheticLambda12
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return PlayHud.settingsPage$lambda$71(((Float) obj).floatValue());
            }
        }));
        settingsPage$row(this, linearLayout, "Volume", settingsPage$slider(this, Settings.INSTANCE.getVolume(), 0.0f, 10.0f, new Function1() { // from class: com.goydevv.studrblx.ui.PlayHud$$ExternalSyntheticLambda13
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return PlayHud.settingsPage$lambda$72(((Float) obj).floatValue());
            }
        }));
        settingsPage$row(this, linearLayout, "Graphics Quality", settingsPage$slider(this, Settings.INSTANCE.getGraphicsQuality(), 1.0f, 10.0f, new Function1() { // from class: com.goydevv.studrblx.ui.PlayHud$$ExternalSyntheticLambda14
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return PlayHud.settingsPage$lambda$73(PlayHud.this, ((Float) obj).floatValue());
            }
        }));
        settingsPage$row(this, linearLayout, "Performance Stats", settingsPage$selector(this, CollectionsKt.listOf(new String[]{"Off", "On"}), Settings.INSTANCE.getShowFps() ? 1 : 0, new Function1() { // from class: com.goydevv.studrblx.ui.PlayHud$$ExternalSyntheticLambda15
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return PlayHud.settingsPage$lambda$74(((Integer) obj).intValue());
            }
        }));
        ScrollView scrollView = new ScrollView(getContext());
        scrollView.addView(linearLayout);
        return scrollView;
    }

    private static final void settingsPage$row(PlayHud this$0, LinearLayout box, String name, View control) {
        LinearLayout r = new LinearLayout(this$0.getContext());
        r.setOrientation(0);
        r.setGravity(16);
        r.setPadding(this$0.dp(6.0f), this$0.dp(8.0f), this$0.dp(6.0f), this$0.dp(8.0f));
        r.addView(label$default(this$0, name, 15.0f, 0, false, 12, null), new LinearLayout.LayoutParams(0, -2, 1.0f));
        r.addView(control);
        box.addView(r);
        View view = new View(this$0.getContext());
        view.setBackgroundColor(452984831);
        box.addView(view, new LinearLayout.LayoutParams(-1, 1));
    }

    private static final View settingsPage$selector(PlayHud this$0, List<String> list, int current, Function1<? super Integer, Unit> function1) {
        Ref.IntRef idx = new Ref.IntRef();
        idx.element = current;
        LinearLayout l = new LinearLayout(this$0.getContext());
        l.setOrientation(0);
        l.setGravity(16);
        TextView t = label$default(this$0, list.get(idx.element), 14.0f, 0, true, 4, null);
        t.setGravity(17);
        t.setMinWidth(this$0.dp(130.0f));
        l.addView(settingsPage$selector$arrow(this$0, idx, list, t, function1, "‹", -1));
        l.addView(t);
        l.addView(settingsPage$selector$arrow(this$0, idx, list, t, function1, "›", 1));
        return l;
    }

    private static final TextView settingsPage$selector$arrow(PlayHud this$0, final Ref.IntRef idx, final List<String> list, final TextView t, final Function1<? super Integer, Unit> function1, String s, final int delta) {
        TextView textViewLabel$default = label$default(this$0, s, 18.0f, 0, true, 4, null);
        textViewLabel$default.setPadding(this$0.dp(10.0f), this$0.dp(2.0f), this$0.dp(10.0f), this$0.dp(2.0f));
        textViewLabel$default.setClickable(true);
        textViewLabel$default.setOnClickListener(new View.OnClickListener() { // from class: com.goydevv.studrblx.ui.PlayHud$$ExternalSyntheticLambda18
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PlayHud.settingsPage$selector$arrow$lambda$64$lambda$63(idx, delta, list, t, function1, view);
            }
        });
        return textViewLabel$default;
    }

    static final void settingsPage$selector$arrow$lambda$64$lambda$63(Ref.IntRef $idx, int $delta, List $options, TextView $t, Function1 $onSet, View it) {
        $idx.element = Math.floorMod($idx.element + $delta, $options.size());
        $t.setText((CharSequence) $options.get($idx.element));
        $onSet.invoke(Integer.valueOf($idx.element));
    }

    private static final View settingsPage$slider(PlayHud this$0, float value, final float min, final float max, final Function1<? super Float, Unit> function1) {
        SeekBar seekBar = new SeekBar(this$0.getContext());
        seekBar.setMax(100);
        seekBar.setProgress(RangesKt.coerceIn((int) (((value - min) / (max - min)) * 100.0f), 0, 100));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.goydevv.studrblx.ui.PlayHud$settingsPage$slider$1$1
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar s, int pr, boolean fromUser) {
                if (fromUser) {
                    function1.invoke(Float.valueOf(min + (((max - min) * pr) / 100.0f)));
                }
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar s) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar s) {
            }
        });
        seekBar.setLayoutParams(new LinearLayout.LayoutParams(this$0.dp(170.0f), -2));
        return seekBar;
    }

    static final Unit settingsPage$lambda$68(PlayHud this$0, int it) {
        Settings.INSTANCE.setShiftLockSwitch(it == 1);
        if (it == 0) {
            synchronized (this$0.studio.getLock()) {
                PlaySession play = this$0.studio.getPlay();
                if (play != null && play.getShiftLock()) {
                    play.toggleShiftLock();
                }
                Unit unit = Unit.INSTANCE;
            }
        }
        return Unit.INSTANCE;
    }

    static final Unit settingsPage$lambda$69(int it) {
        Settings.INSTANCE.setCameraMode(it == 0 ? "Classic" : "Follow");
        return Unit.INSTANCE;
    }

    static final Unit settingsPage$lambda$70(int it) {
        Settings.INSTANCE.setMovementMode(it == 1 ? "Thumbstick" : "Dynamic Thumbstick");
        return Unit.INSTANCE;
    }

    static final Unit settingsPage$lambda$71(float it) {
        Settings.INSTANCE.setPlayCameraSensitivity(it);
        return Unit.INSTANCE;
    }

    static final Unit settingsPage$lambda$72(float it) {
        Settings.INSTANCE.setVolume((int) it);
        return Unit.INSTANCE;
    }

    static final Unit settingsPage$lambda$73(PlayHud this$0, float it) {
        Settings.INSTANCE.setGraphicsQuality(RangesKt.coerceIn((int) it, 1, 10));
        return Unit.INSTANCE;
    }

    static final Unit settingsPage$lambda$74(int it) {
        Settings.INSTANCE.setShowFps(it == 1);
        return Unit.INSTANCE;
    }

    private final View helpPage() {
        LinearLayout box = new LinearLayout(getContext());
        box.setOrientation(1);
        box.setPadding(dp(6.0f), dp(4.0f), dp(6.0f), dp(4.0f));
        for (Pair pair : CollectionsKt.listOf(new Pair[]{TuplesKt.to("Move", "Drag the thumbstick (bottom left). Keyboard: WASD or arrow keys"), TuplesKt.to("Look around", "Drag anywhere else on the screen. Keyboard: ← → to turn"), TuplesKt.to("Zoom", "Pinch with two fingers. Keyboard: I / O"), TuplesKt.to("Jump", "Jump button (bottom right). Keyboard: Space"), TuplesKt.to("Shift lock", "Lock button left of jump. Keyboard: Left/Right Shift"), TuplesKt.to("Chat", "Chat button (top left). Keyboard: /"), TuplesKt.to("Leaderboard", "Experience controls (≡) → Leaderboard. Keyboard: Tab"), TuplesKt.to("Tools", "Tap a slot at the bottom. Keyboard: 1–0, Backspace to unequip"), TuplesKt.to("Menu", "Roblox button (top left). Keyboard: Esc")})) {
            String k = (String) pair.component1();
            String v = (String) pair.component2();
            TextView textViewLabel$default = label$default(this, k, 15.0f, 0, true, 4, null);
            textViewLabel$default.setPadding(0, dp(6.0f), 0, 0);
            box.addView(textViewLabel$default);
            box.addView(label$default(this, v, 13.0f, -1275068417, false, 8, null));
        }
        ScrollView scrollView = new ScrollView(getContext());
        scrollView.addView(box);
        return scrollView;
    }
}

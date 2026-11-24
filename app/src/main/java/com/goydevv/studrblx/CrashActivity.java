package com.goydevv.studrblx;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import kotlin.io.FilesKt;

public final class CrashActivity extends Activity {
    public static final String EXTRA_TRACE = "crash_trace";
    public static final String EXTRA_SAVED_PATH = "crash_saved_path";
    private static final int REQ_EXPORT_TXT = 9042;
    private String fullTrace = "Unknown crash";

    private Button makePillButton(String label, int bgColor, float d) {
        Button btn = new Button(this);
        btn.setText(label);
        btn.setAllCaps(false);
        btn.setTextColor(0xFFFFFFFF);
        btn.setTextSize(13.0f);
        btn.setTypeface(Typeface.DEFAULT_BOLD);
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(bgColor);
        gd.setCornerRadius(10 * d);
        btn.setBackground(gd);
        int hPad = (int) (14 * d);
        int vPad = (int) (8 * d);
        btn.setPadding(hPad, vPad, hPad, vPad);
        btn.setMinHeight(0);
        btn.setMinimumHeight(0);
        return btn;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent() != null && getIntent().hasExtra(EXTRA_TRACE)) {
            String s = getIntent().getStringExtra(EXTRA_TRACE);
            if (s != null && s.length() > 0) fullTrace = s;
        }
        // Fallback: if Intent trace was missing/truncated, read from internal last_crash.txt
        try {
            File f = new File(getFilesDir(), StudioApp.LAST_CRASH_FILE);
            if (f.isFile() && f.length() > 0) {
                if ("Unknown crash".equals(fullTrace)) {
                    fullTrace = new String(FilesKt.readBytes(f), StandardCharsets.UTF_8);
                }
                // Clear pending crash marker now that CrashActivity is actually shown on screen
                f.delete();
            }
        } catch (Throwable ignored) {
        }

        String savedPath = getIntent() != null ? getIntent().getStringExtra(EXTRA_SAVED_PATH) : null;

        float d = getResources().getDisplayMetrics().density;
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(0xFF14161B);
        int pad = (int) (14 * d);
        root.setPadding(pad, pad, pad, pad);

        LinearLayout top = new LinearLayout(this);
        top.setOrientation(LinearLayout.HORIZONTAL);
        top.setGravity(Gravity.CENTER_VERTICAL);

        TextView title = new TextView(this);
        title.setText("StudioRblx Crash Detector");
        title.setTextColor(0xFFFF6B6B);
        title.setTextSize(17.0f);
        title.setTypeface(Typeface.DEFAULT_BOLD);
        LinearLayout.LayoutParams tlp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        top.addView(title, tlp);

        Button exportBtn = makePillButton("Export .txt", 0xFF1E9E5A, d);
        exportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
                Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TITLE, "StudioRblx_crash_" + stamp + ".txt");
                try {
                    startActivityForResult(intent, REQ_EXPORT_TXT);
                } catch (Exception ex) {
                    Toast.makeText(CrashActivity.this, "Export failed: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        LinearLayout.LayoutParams blp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        blp.rightMargin = (int) (8 * d);
        top.addView(exportBtn, blp);

        Button copyBtn = makePillButton("Copy Log", 0xFF0095F6, d);
        copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                if (cm != null) {
                    cm.setPrimaryClip(ClipData.newPlainText("StudioRblx Crash Log", fullTrace));
                    Toast.makeText(CrashActivity.this, "Copied full crash log", Toast.LENGTH_SHORT).show();
                }
            }
        });
        LinearLayout.LayoutParams blp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        blp2.rightMargin = (int) (8 * d);
        top.addView(copyBtn, blp2);

        Button restartBtn = makePillButton("Restart Studio", 0xFF333946, d);
        restartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CrashActivity.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });
        top.addView(restartBtn);
        root.addView(top);

        if (savedPath != null && savedPath.length() > 0) {
            TextView sub = new TextView(this);
            sub.setText("Auto-saved: " + savedPath);
            sub.setTextColor(0xFF9AA3B2);
            sub.setTextSize(11.0f);
            sub.setPadding(0, (int) (6 * d), 0, 0);
            root.addView(sub);
        }

        ScrollView sv = new ScrollView(this);
        GradientDrawable logBg = new GradientDrawable();
        logBg.setColor(0xFF0E1015);
        logBg.setCornerRadius(10 * d);
        logBg.setStroke((int) d, 0xFF282E3A);
        sv.setBackground(logBg);
        LinearLayout.LayoutParams slp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1.0f);
        slp.topMargin = (int) (10 * d);

        HorizontalScrollView hsv = new HorizontalScrollView(this);
        TextView logView = new TextView(this);
        logView.setText(fullTrace);
        logView.setTextColor(0xFFE6EAF2);
        logView.setTextSize(11.5f);
        logView.setTypeface(Typeface.MONOSPACE);
        logView.setTextIsSelectable(true);
        int lpad = (int) (12 * d);
        logView.setPadding(lpad, lpad, lpad, lpad);
        hsv.addView(logView);
        sv.addView(hsv);
        root.addView(sv, slp);

        setContentView(root);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_EXPORT_TXT && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try (OutputStream os = getContentResolver().openOutputStream(uri)) {
                if (os != null) {
                    os.write(fullTrace.getBytes(StandardCharsets.UTF_8));
                    os.flush();
                    Toast.makeText(this, "Exported crash log .txt!", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception ex) {
                Toast.makeText(this, "Could not write file: " + ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}

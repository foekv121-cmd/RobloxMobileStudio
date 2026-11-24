package com.goydevv.studrblx.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.OpenableColumns;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import com.goydevv.studrblx.core.OutputLog;
import com.goydevv.studrblx.net.AuthRequiredException;
import com.goydevv.studrblx.net.RobloxApi;
import com.goydevv.studrblx.net.RobloxCloud;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import kotlin.Pair;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/**
 * Quick Publish: pick a local .rbxl / .rbxlx and send it straight to Roblox,
 * without loading it into the 3D viewport.
 *  - Update Game:     Open Cloud  POST /universes/v1/{universeId}/places/{placeId}/versions?versionType=Published  (x-api-key)
 *  - Create New Game: .ROBLOSECURITY cookie + x-csrf-token (handled by RobloxCloud.authed)
 */
public final class QuickPublish {
    /** Set by MainActivity; opens the system file picker. */
    public static ActivityResultLauncher<String[]> launcher;

    private static final Handler MAIN = new Handler(Looper.getMainLooper());
    private static final int MAX_BYTES = 100 * 1024 * 1024;
    private static Uri pickedUri;
    private static String pickedName = "";
    private static Runnable pickedListener;

    private QuickPublish() {
    }

    public static void onPicked(Activity act, Uri uri) {
        if (uri == null) {
            return;
        }
        pickedUri = uri;
        pickedName = displayName(act, uri);
        OutputLog.INSTANCE.info("Quick Publish: selected " + pickedName);
        if (pickedListener != null) {
            pickedListener.run();
        }
    }

    private static String displayName(Context ctx, Uri uri) {
        try (Cursor c = ctx.getContentResolver().query(uri, new String[]{OpenableColumns.DISPLAY_NAME}, null, null, null)) {
            if (c != null && c.moveToFirst()) {
                String n = c.getString(0);
                if (n != null && n.length() > 0) {
                    return n;
                }
            }
        } catch (Exception ignored) {
        }
        String p = uri.getLastPathSegment();
        return p != null ? p : uri.toString();
    }

    private static byte[] readAll(Context ctx, Uri uri) throws IOException {
        try (InputStream in = ctx.getContentResolver().openInputStream(uri)) {
            if (in == null) {
                throw new IOException("Couldn't open the selected file.");
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream(1 << 20);
            byte[] buf = new byte[64 * 1024];
            int n;
            while ((n = in.read(buf)) != -1) {
                out.write(buf, 0, n);
                if (out.size() > MAX_BYTES) {
                    throw new IOException("File is larger than 100 MB, which Roblox won't accept.");
                }
            }
            return out.toByteArray();
        }
    }

    private static boolean looksLikePlace(byte[] b) {
        int n = Math.min(b.length, 96);
        String head = new String(b, 0, n, java.nio.charset.StandardCharsets.ISO_8859_1);
        return head.contains("<roblox");
    }

    private static long parseId(String s) {
        try {
            String t = s.trim();
            return t.length() == 0 ? 0L : Long.parseLong(t);
        } catch (NumberFormatException e) {
            return -1L;
        }
    }

    private static String describe(Throwable t) {
        StringBuilder sb = new StringBuilder();
        Throwable cur = t;
        int depth = 0;
        while (cur != null && depth < 4) {
            if (depth > 0) {
                sb.append("\n  caused by: ");
            }
            sb.append(cur.getClass().getSimpleName());
            if (cur.getMessage() != null) {
                sb.append(": ").append(cur.getMessage());
            }
            cur = cur.getCause() == cur ? null : cur.getCause();
            depth++;
        }
        if (t instanceof AuthRequiredException) {
            sb.append("\nCheck the API key / .ROBLOSECURITY cookie and try again.");
        }
        return sb.toString();
    }

    private static void styleSeg(Context c, TextView t, boolean on) {
        GradientDrawable g = new GradientDrawable();
        g.setCornerRadius(Ui.INSTANCE.dp(c, 10.0f));
        g.setColor(on ? Ui.INSTANCE.getACCENT() : Ui.INSTANCE.getHOVER());
        t.setBackground(g);
        t.setTextColor(on ? 0xFFFFFFFF : Ui.INSTANCE.getDIM());
    }

    private static TextView label(Context c, String s) {
        TextView t = Ui.INSTANCE.text(c, s, 12.0f, Ui.INSTANCE.getDIM(), true);
        t.setPadding(0, Ui.INSTANCE.dp(c, 12.0f), 0, Ui.INSTANCE.dp(c, 4.0f));
        return t;
    }

    private static void secret(EditText e) {
        e.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    public static void show(final Activity act) {
        final Ui ui = Ui.INSTANCE;
        pickedUri = null;
        pickedName = "";

        final LinearLayout root = new LinearLayout(act);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(ui.dp(act, 18.0f), ui.dp(act, 14.0f), ui.dp(act, 18.0f), ui.dp(act, 6.0f));

        root.addView(ui.text(act, "Quick Publish", 18.0f, ui.getTEXT(), true));
        TextView sub = ui.text(act, "Send a .rbxl straight to Roblox. The file is not opened in the 3D viewport.", 12.0f, ui.getDIM(), false);
        sub.setSingleLine(false);
        sub.setPadding(0, ui.dp(act, 2.0f), 0, ui.dp(act, 8.0f));
        root.addView(sub);

        // File picker row
        root.addView(label(act, "FILE"));
        final TextView fileLabel = ui.text(act, "No file chosen", 13.0f, ui.getTEXT(), false);
        fileLabel.setSingleLine(false);
        root.addView(fileLabel);
        TextView chooseBtn = ui.button(act, "Choose .rbxl file\u2026", ui.getHOVER(), new Function1<View, Unit>() {
            @Override
            public Unit invoke(View v) {
                if (launcher == null) {
                    Toast.makeText(act, "File picker isn't ready yet.", Toast.LENGTH_SHORT).show();
                } else {
                    launcher.launch(new String[]{"*/*"});
                }
                return Unit.INSTANCE;
            }
        });
        LinearLayout.LayoutParams chooseLp = new LinearLayout.LayoutParams(-1, -2);
        chooseLp.topMargin = ui.dp(act, 6.0f);
        root.addView(chooseBtn, chooseLp);
        pickedListener = new Runnable() {
            @Override
            public void run() {
                fileLabel.setText(pickedName);
            }
        };

        // Mode switch
        root.addView(label(act, "MODE"));
        LinearLayout seg = new LinearLayout(act);
        seg.setOrientation(LinearLayout.HORIZONTAL);
        final TextView segUpdate = ui.text(act, "Update Game", 13.0f, ui.getTEXT(), true);
        final TextView segCreate = ui.text(act, "Create New Game", 13.0f, ui.getTEXT(), true);
        for (TextView t : new TextView[]{segUpdate, segCreate}) {
            t.setGravity(Gravity.CENTER);
            t.setPadding(0, ui.dp(act, 11.0f), 0, ui.dp(act, 11.0f));
        }
        LinearLayout.LayoutParams l1 = new LinearLayout.LayoutParams(0, -2, 1.0f);
        l1.rightMargin = ui.dp(act, 4.0f);
        seg.addView(segUpdate, l1);
        seg.addView(segCreate, new LinearLayout.LayoutParams(0, -2, 1.0f));
        root.addView(seg);

        // Update Game fields
        final LinearLayout updateBox = new LinearLayout(act);
        updateBox.setOrientation(LinearLayout.VERTICAL);
        updateBox.addView(label(act, "UNIVERSE ID (OPTIONAL)"));
        final EditText etUniverse = ui.input(act, "", "Looked up automatically if empty", true, false);
        updateBox.addView(etUniverse);
        updateBox.addView(label(act, "PLACE ID"));
        final EditText etPlace = ui.input(act, "", "Place ID to overwrite", true, false);
        updateBox.addView(etPlace);
        updateBox.addView(label(act, "OPEN CLOUD API KEY (x-api-key)"));
        final EditText etKey = ui.input(act, RobloxApi.INSTANCE.apiKey(act), "API key with universe-places write", false, false);
        secret(etKey);
        updateBox.addView(etKey);
        root.addView(updateBox);

        // Create New Game fields
        final LinearLayout createBox = new LinearLayout(act);
        createBox.setOrientation(LinearLayout.VERTICAL);
        createBox.addView(label(act, "NEW GAME NAME"));
        final EditText etName = ui.input(act, "", "Name of the new experience", false, false);
        createBox.addView(etName);
        createBox.addView(label(act, ".ROBLOSECURITY COOKIE"));
        final EditText etCookie = ui.input(act, RobloxApi.INSTANCE.cookie(act), "Paste your .ROBLOSECURITY value", false, false);
        secret(etCookie);
        createBox.addView(etCookie);
        TextView createNote = ui.text(act, "Creates a new experience with your account, then uploads the file to its start place.", 11.0f, ui.getDIM(), false);
        createNote.setSingleLine(false);
        createNote.setPadding(0, ui.dp(act, 6.0f), 0, 0);
        createBox.addView(createNote);
        root.addView(createBox);

        final boolean[] createMode = {false};
        final Runnable applyMode = new Runnable() {
            @Override
            public void run() {
                updateBox.setVisibility(createMode[0] ? View.GONE : View.VISIBLE);
                createBox.setVisibility(createMode[0] ? View.VISIBLE : View.GONE);
                styleSeg(act, segUpdate, !createMode[0]);
                styleSeg(act, segCreate, createMode[0]);
            }
        };
        segUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createMode[0] = false;
                applyMode.run();
            }
        });
        segCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createMode[0] = true;
                applyMode.run();
            }
        });
        applyMode.run();

        final TextView status = ui.text(act, "", 12.0f, ui.getDIM(), false);
        status.setSingleLine(false);
        status.setTextIsSelectable(true);
        status.setPadding(0, ui.dp(act, 12.0f), 0, ui.dp(act, 4.0f));
        root.addView(status);

        ScrollView scroll = new ScrollView(act);
        scroll.addView(root);

        final AlertDialog dlg = Dialogs.INSTANCE.themed(act)
                .setView(scroll)
                .setPositiveButton("Publish", null)
                .setNegativeButton("Close", null)
                .create();
        dlg.setOnDismissListener(new android.content.DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(android.content.DialogInterface d) {
                pickedListener = null;
            }
        });
        dlg.show();
        final android.widget.Button pub = dlg.getButton(AlertDialog.BUTTON_POSITIVE);
        pub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final boolean create = createMode[0];
                final long universeId = parseId(etUniverse.getText().toString());
                final long placeId = parseId(etPlace.getText().toString());
                final String apiKey = etKey.getText().toString().trim();
                final String cookie = etCookie.getText().toString().trim();
                final String gameName = etName.getText().toString().trim();
                final Uri uri = pickedUri;
                final String fileName = pickedName;
                String problem = null;
                if (uri == null) {
                    problem = "Choose a .rbxl file first.";
                } else if (!create && placeId <= 0) {
                    problem = "Enter the Place ID to update.";
                } else if (!create && universeId < 0) {
                    problem = "Universe ID must be a number (or leave it empty).";
                } else if (!create && apiKey.length() == 0) {
                    problem = "Update Game needs an Open Cloud API key.";
                } else if (create && gameName.length() == 0) {
                    problem = "Enter a name for the new game.";
                } else if (create && cookie.length() == 0) {
                    problem = "Create New Game needs your .ROBLOSECURITY cookie.";
                }
                if (problem != null) {
                    status.setTextColor(Ui.ERROR);
                    status.setText(problem);
                    return;
                }
                pub.setEnabled(false);
                status.setTextColor(ui.getDIM());
                status.setText("Reading file\u2026");
                final Context app = act.getApplicationContext();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            byte[] bytes = readAll(app, uri);
                            if (!looksLikePlace(bytes)) {
                                throw new IOException("\"" + fileName + "\" doesn't look like a Roblox place file (.rbxl / .rbxlx).");
                            }
                            OutputLog.INSTANCE.info("Quick Publish: read " + fileName + " (" + (bytes.length / 1024) + " KB)");
                            if (!create) {
                                RobloxApi.INSTANCE.setApiKey(app, apiKey);
                                post(status, "Uploading to place " + placeId + "\u2026", false, false);
                                OutputLog.INSTANCE.info("Quick Publish: POST apis.roblox.com/universes/v1/" + (universeId > 0 ? universeId : "<auto>") + "/places/" + placeId + "/versions?versionType=Published");
                                String res = RobloxCloud.INSTANCE.publish(app, universeId, placeId, bytes, true);
                                String done = "Published to place " + placeId + " (" + res + ").";
                                OutputLog.INSTANCE.info("Quick Publish: " + done);
                                post(status, done, true, true);
                            } else {
                                RobloxApi.INSTANCE.setCookie(app, cookie);
                                RobloxCloud.INSTANCE.resetSession();
                                post(status, "Creating \"" + gameName + "\"\u2026", false, false);
                                Pair<RobloxCloud.Game, RobloxCloud.Place> made = RobloxCloud.INSTANCE.createGame(app, gameName, 0L, "");
                                long newUniverse = made.getFirst().getUniverseId();
                                long newPlace = made.getSecond().getId();
                                OutputLog.INSTANCE.info("Quick Publish: created game \"" + gameName + "\" (universe " + newUniverse + ", place " + newPlace + ")");
                                post(status, "Created (universe " + newUniverse + ", place " + newPlace + "). Uploading file\u2026", false, false);
                                try {
                                    String res = RobloxCloud.INSTANCE.publish(app, newUniverse, newPlace, bytes, true);
                                    String done = "Created \"" + gameName + "\" and published the file (" + res + ").\nUniverse " + newUniverse + ", Place " + newPlace + ".";
                                    OutputLog.INSTANCE.info("Quick Publish: " + done);
                                    post(status, done, true, true);
                                } catch (Throwable up) {
                                    String msg = "The game was created (universe " + newUniverse + ", place " + newPlace + ") but uploading the file failed:\n" + describe(up)
                                            + "\n\nYou can still use Update Game with that Place ID and an API key that covers the new experience.";
                                    OutputLog.INSTANCE.error("Quick Publish: " + msg);
                                    post(status, msg, true, false);
                                }
                            }
                        } catch (Throwable t) {
                            String msg = describe(t);
                            OutputLog.INSTANCE.error("Quick Publish failed: " + msg);
                            post(status, msg, true, false);
                        } finally {
                            MAIN.post(new Runnable() {
                                @Override
                                public void run() {
                                    pub.setEnabled(true);
                                }
                            });
                        }
                    }
                }, "QuickPublish").start();
            }
        });
    }

    private static void post(final TextView status, final String msg, final boolean finished, final boolean ok) {
        MAIN.post(new Runnable() {
            @Override
            public void run() {
                status.setTextColor(finished ? (ok ? Ui.PLAY : Ui.ERROR) : Ui.INSTANCE.getDIM());
                status.setText(msg);
            }
        });
    }
}

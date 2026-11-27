package com.goydevv.studrblx.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.goydevv.studrblx.core.Studio;
import com.goydevv.studrblx.engine.Collide;
import com.goydevv.studrblx.model.Instance;
import java.util.ArrayList;
import java.util.List;
import kotlin.Unit;

/**
 * Collision tools:
 *  - turn collision off or on for the selected models / parts (every part inside),
 *  - let the player walk through everything (noclip) or put the player in a collision group,
 *  - create collision groups, choose which groups collide, and put the selection into a group.
 * Everything is saved in the place (ServerStorage > __CollisionConfig) and used by the playtest.
 */
public final class CollisionUi {
    private final Activity act;
    private final Studio studio;
    private AlertDialog hub;

    private CollisionUi(Activity act, Studio studio) {
        this.act = act;
        this.studio = studio;
    }

    public static void show(Activity act, Studio studio) {
        try {
            if (studio.isPlaying()) {
                Toast.makeText(act, "Stop the playtest first, then change collisions.", Toast.LENGTH_LONG).show();
                return;
            }
            Collide.loadFrom(studio.getEditModel());
            new CollisionUi(act, studio).open();
        } catch (Throwable t) {
            Toast.makeText(act, "Couldn't open collisions: " + t, Toast.LENGTH_LONG).show();
        }
    }

    private void say(String s) {
        Toast.makeText(act, s, Toast.LENGTH_SHORT).show();
    }

    private List<Instance> selection() {
        return new ArrayList<>(studio.getSelection());
    }

    private void save() {
        Collide.saveTo(studio.getEditModel());
        studio.markDirty();
    }

    /** Every BasePart in the selection, including inside models. */
    private List<Instance> partsOf(List<Instance> sel) {
        List<Instance> parts = new ArrayList<>();
        for (Instance s : sel) {
            List<Instance> all = new ArrayList<>();
            all.add(s);
            s.descendants(all);
            for (Instance i : all) {
                if (i.isA("BasePart") && !parts.contains(i)) {
                    parts.add(i);
                }
            }
        }
        return parts;
    }

    private void setSelectionCollide(boolean on) {
        List<Instance> sel = selection();
        if (sel.isEmpty()) {
            say("Select a model or a part in the Explorer first.");
            return;
        }
        List<Instance> parts = partsOf(sel);
        studio.getUndo().begin(on ? "Collision on" : "Collision off");
        try {
            for (Instance p : parts) {
                p.set("CanCollide", on, true);
            }
            for (Instance s : sel) {
                // models and folders carry the flag too, so parts added to them later follow
                if (!s.isA("BasePart")) {
                    if (on) {
                        s.getAttributes().remove("CanCollide");
                    } else {
                        s.getAttributes().put("CanCollide", Boolean.FALSE);
                    }
                }
            }
        } finally {
            studio.getUndo().commit();
        }
        studio.markDirty();
        say((on ? "Collision ON for " : "Collision OFF for ") + parts.size() + " part(s).");
        refreshHub();
    }

    private void assignGroup() {
        final List<Instance> sel = selection();
        if (sel.isEmpty()) {
            say("Select a model or a part in the Explorer first.");
            return;
        }
        final List<String> names = Collide.names();
        Dialogs.INSTANCE.themed(act).setTitle("Put selection in group")
                .setItems(names.toArray(new String[0]), (d, which) -> {
                    List<Instance> parts = partsOf(sel);
                    studio.getUndo().begin("Collision group");
                    try {
                        for (Instance p : parts) {
                            p.set("CollisionGroup", names.get(which), true);
                        }
                    } finally {
                        studio.getUndo().commit();
                    }
                    studio.markDirty();
                    say(parts.size() + " part(s) now in \"" + names.get(which) + "\".");
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void playerGroupDialog() {
        final List<String> names = Collide.names();
        String[] items = new String[names.size() + 1];
        items[0] = "(automatic: the group of HumanoidRootPart)";
        for (int i = 0; i < names.size(); i++) {
            items[i + 1] = names.get(i);
        }
        Dialogs.INSTANCE.themed(act).setTitle("Player's collision group")
                .setItems(items, (d, which) -> {
                    Collide.playerGroup = which == 0 ? null : names.get(which - 1);
                    save();
                    refreshHub();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // ------------------------------------------------------------------ groups editor

    private void groupsDialog() {
        final List<String> names = Collide.names();
        String[] items = new String[names.size()];
        for (int i = 0; i < items.length; i++) {
            items[i] = names.get(i) + (Collide.DEFAULT.equals(names.get(i)) ? "  (built in)" : "");
        }
        Dialogs.INSTANCE.themed(act).setTitle("Collision groups")
                .setItems(items, (d, which) -> editGroup(names.get(which)))
                .setPositiveButton("New group", (d, w) -> {
                    final android.widget.EditText et = Ui.INSTANCE.input(act, "", "Group name", false, false);
                    LinearLayout box = new LinearLayout(act);
                    int pad = Ui.INSTANCE.dp(act, 14.0f);
                    box.setPadding(pad, pad, pad, pad);
                    box.addView(et, new LinearLayout.LayoutParams(-1, -2));
                    Dialogs.INSTANCE.themed(act).setTitle("New collision group").setView(box)
                            .setPositiveButton("Create", (d2, w2) -> {
                                String n = et.getText().toString().trim();
                                if (n.length() == 0) {
                                    return;
                                }
                                Collide.register(n);
                                save();
                                editGroup(n);
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                })
                .setNegativeButton("Back", null)
                .show();
    }

    private void editGroup(final String group) {
        final List<String> all = Collide.names();
        final String[] names = all.toArray(new String[0]);
        final boolean[] checked = new boolean[names.length];
        for (int i = 0; i < names.length; i++) {
            checked[i] = Collide.collidable(group, names[i]);
        }
        AlertDialog.Builder b = Dialogs.INSTANCE.themed(act)
                .setTitle("\"" + group + "\" collides with:")
                .setMultiChoiceItems(names, checked, (d, which, isChecked) -> checked[which] = isChecked)
                .setPositiveButton("Save", (d, w) -> {
                    for (int i = 0; i < names.length; i++) {
                        Collide.setCollidable(group, names[i], checked[i]);
                    }
                    save();
                    say("Saved. Unticked groups pass through \"" + group + "\".");
                    groupsDialog();
                })
                .setNegativeButton("Cancel", null);
        if (!Collide.DEFAULT.equals(group)) {
            b.setNeutralButton("Delete group", (d, w) -> {
                Collide.unregister(group);
                save();
                say("Deleted \"" + group + "\". Parts in it behave like Default.");
                groupsDialog();
            });
        }
        b.show();
    }

    // ------------------------------------------------------------------ hub

    private TextView info;
    private CheckBox noclip;
    private TextView playerGroupBtn;

    private void refreshHub() {
        if (info == null) {
            return;
        }
        int n = studio.getSelection().size();
        info.setText(n == 0 ? "Nothing selected" : n + " selected (" + partsOf(selection()).size() + " part(s) inside)");
        noclip.setChecked(Collide.playerNoclip);
        playerGroupBtn.setText("Player's group: " + (Collide.playerGroup == null ? "automatic" : Collide.playerGroup));
    }

    private TextView btn(String label, final Runnable r) {
        final Ui ui = Ui.INSTANCE;
        return ui.button(act, label, ui.getHOVER(), v -> {
            try {
                r.run();
            } catch (Throwable t) {
                say("Error: " + t);
            }
            return Unit.INSTANCE;
        });
    }

    private void open() {
        final Ui ui = Ui.INSTANCE;
        LinearLayout box = new LinearLayout(act);
        box.setOrientation(LinearLayout.VERTICAL);
        int pad = ui.dp(act, 14.0f);
        box.setPadding(pad, pad, pad, pad);

        box.addView(ui.text(act, "Models and parts", 12.0f, ui.getDIM(), true));
        info = ui.text(act, "", 12.0f, ui.getTEXT(), false);
        info.setPadding(0, ui.dp(act, 4.0f), 0, ui.dp(act, 6.0f));
        box.addView(info);
        LinearLayout row1 = new LinearLayout(act);
        row1.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, -2, 1.0f);
        lp.setMargins(0, 0, ui.dp(act, 6.0f), 0);
        row1.addView(btn("Collision OFF", () -> setSelectionCollide(false)), lp);
        row1.addView(btn("Collision ON", () -> setSelectionCollide(true)), new LinearLayout.LayoutParams(0, -2, 1.0f));
        box.addView(row1);
        TextView hint = ui.text(act, "Select a model in the Explorer, then tap. Everything inside it stops (or starts) blocking the player.", 11.0f, ui.getDIM(), false);
        hint.setSingleLine(false);
        hint.setPadding(0, ui.dp(act, 4.0f), 0, ui.dp(act, 14.0f));
        box.addView(hint);

        box.addView(ui.text(act, "Player", 12.0f, ui.getDIM(), true));
        noclip = new CheckBox(act);
        noclip.setText("Player walks through everything (noclip)");
        noclip.setTextColor(ui.getTEXT());
        noclip.setOnClickListener((View v) -> {
            Collide.playerNoclip = noclip.isChecked();
            save();
        });
        box.addView(noclip);
        playerGroupBtn = btn("Player's group", this::playerGroupDialog);
        box.addView(playerGroupBtn);
        TextView hint2 = ui.text(act, "Scripts can also use character:SetAttribute(\"Noclip\", true) or put the player in a group with PhysicsService.", 11.0f, ui.getDIM(), false);
        hint2.setSingleLine(false);
        hint2.setPadding(0, ui.dp(act, 4.0f), 0, ui.dp(act, 14.0f));
        box.addView(hint2);

        box.addView(ui.text(act, "Collision groups", 12.0f, ui.getDIM(), true));
        LinearLayout row2 = new LinearLayout(act);
        row2.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(0, -2, 1.0f);
        lp2.setMargins(0, ui.dp(act, 4.0f), ui.dp(act, 6.0f), 0);
        row2.addView(btn("Edit groups", this::groupsDialog), lp2);
        LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(0, -2, 1.0f);
        lp3.setMargins(0, ui.dp(act, 4.0f), 0, 0);
        row2.addView(btn("Put selection in group", this::assignGroup), lp3);
        box.addView(row2);

        ScrollView sv = new ScrollView(act);
        sv.addView(box);
        hub = Dialogs.INSTANCE.themed(act).setTitle("Collision").setView(sv)
                .setPositiveButton("Done", null)
                .create();
        hub.show();
        refreshHub();
    }
}

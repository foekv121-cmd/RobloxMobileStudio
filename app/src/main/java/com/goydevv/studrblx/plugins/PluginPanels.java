package com.goydevv.studrblx.plugins;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.goydevv.studrblx.ui.Dialogs;
import com.goydevv.studrblx.ui.Ui;
import java.util.List;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import org.luaj.vm2.LuaValue;

/** Sheet shown by app.ui.showPanel. Buttons run the plugin's callbacks on the plugin worker thread. */
final class PluginPanels {
    private PluginPanels() {
    }

    static void show(final Activity act, final NativePlugins.Plugin p, String title, String text, List<String> labels, final List<LuaValue> callbacks) {
        if (act == null || act.isFinishing()) {
            return;
        }
        final Ui ui = Ui.INSTANCE;
        LinearLayout root = new LinearLayout(act);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(ui.dp(act, 18.0f), ui.dp(act, 14.0f), ui.dp(act, 18.0f), ui.dp(act, 6.0f));
        root.addView(ui.text(act, p.icon + "  " + title, 17.0f, ui.getTEXT(), true));
        TextView by = ui.text(act, "Plugin: " + p.name, 11.0f, ui.getDIM(), false);
        by.setPadding(0, ui.dp(act, 2.0f), 0, ui.dp(act, 10.0f));
        root.addView(by);
        TextView body = ui.text(act, text, 13.0f, ui.getTEXT(), false);
        body.setSingleLine(false);
        body.setTextIsSelectable(true);
        if (text != null && text.length() > 0) {
            root.addView(body);
        }
        for (int i = 0; i < labels.size(); i++) {
            final int idx = i;
            final String label = labels.get(i);
            TextView b = ui.button(act, label, ui.getHOVER(), new Function1<View, Unit>() {
                @Override
                public Unit invoke(View v) {
                    NativePlugins.runCallback(p.id, callbacks.get(idx), "panel button \"" + label + "\"");
                    return Unit.INSTANCE;
                }
            });
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
            lp.topMargin = ui.dp(act, 8.0f);
            root.addView(b, lp);
        }
        ScrollView sv = new ScrollView(act);
        sv.addView(root);
        Dialogs.INSTANCE.themed(act).setView(sv).setPositiveButton("Close", null).show();
    }
}

package com.goydevv.studrblx.ui;

import android.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.core.view.ViewCompat;
import com.goydevv.studrblx.model.BrickColorV;
import com.goydevv.studrblx.model.BrickColors;
import com.goydevv.studrblx.model.CFrame;
import com.goydevv.studrblx.model.ClassDb;
import com.goydevv.studrblx.model.Color3;
import com.goydevv.studrblx.model.DataTypesKt;
import com.goydevv.studrblx.model.EnumValue;
import com.goydevv.studrblx.model.Enums;
import com.goydevv.studrblx.model.NumberRange;
import com.goydevv.studrblx.model.UDim;
import com.goydevv.studrblx.model.UDim2;
import com.goydevv.studrblx.model.Vector2;
import com.goydevv.studrblx.model.Vector3;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: Dialogs.kt */
/* JADX INFO: loaded from: classes5.dex */
public final class Dialogs {
    public static final Dialogs INSTANCE = new Dialogs();

    private Dialogs() {
    }

    private final AlertDialog.Builder theme(final Context ctx) {
        return new AlertDialog.Builder(ctx, Ui.INSTANCE.getDialogTheme()) {
            @Override
            public AlertDialog create() {
                AlertDialog d = super.create();
                d.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface di) {
                        try {
                            android.view.Window w = d.getWindow();
                            if (w != null) {
                                GradientDrawable bg = new GradientDrawable();
                                bg.setColor(Ui.INSTANCE.getHEADER());
                                bg.setCornerRadius(Ui.INSTANCE.dp(ctx, 20.0f));
                                bg.setStroke(Ui.INSTANCE.dp(ctx, 1.0f), Ui.INSTANCE.getBORDER());
                                w.setBackgroundDrawable(bg);
                            }
                            android.widget.Button pos = d.getButton(DialogInterface.BUTTON_POSITIVE);
                            if (pos != null) pos.setTextColor(Ui.INSTANCE.getACCENT());
                            android.widget.Button neg = d.getButton(DialogInterface.BUTTON_NEGATIVE);
                            if (neg != null) neg.setTextColor(Ui.INSTANCE.getDIM());
                            android.widget.Button neu = d.getButton(DialogInterface.BUTTON_NEUTRAL);
                            if (neu != null) neu.setTextColor(Ui.INSTANCE.getACCENT());
                        } catch (Throwable ignored) {
                        }
                    }
                });
                return d;
            }
        };
    }

    /* JADX WARN: Type inference failed for: r5v3, types: [com.goydevv.studrblx.ui.Dialogs$pickClass$adapter$1] */
    public final void pickClass(final Context ctx, String title, final Function1<? super String, Unit> onPick) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(title, "title");
        Intrinsics.checkNotNullParameter(onPick, "onPick");
        LinearLayout linearLayout = new LinearLayout(ctx);
        linearLayout.setOrientation(1);
        linearLayout.setPadding(Ui.INSTANCE.dp(ctx, 12.0f), Ui.INSTANCE.dp(ctx, 8.0f), Ui.INSTANCE.dp(ctx, 12.0f), 0);
        EditText search = Ui.input$default(Ui.INSTANCE, ctx, "", "Search objects", false, false, 24, null);
        linearLayout.addView(search);
        final List<String> insertable = ClassDb.INSTANCE.getInsertable();
        final ArrayList shown = new ArrayList(insertable);
        ListView lv = new ListView(ctx);
        final ArrayAdapter<String> r5 = new ArrayAdapter<String>(ctx, R.layout.simple_list_item_1, shown) { // from class: com.goydevv.studrblx.ui.Dialogs$pickClass$adapter$1
            final /* synthetic */ Context $ctx = ctx;

            @Override // android.widget.ArrayAdapter, android.widget.Adapter
            public View getView(int position, View convertView, ViewGroup parent) {
                Intrinsics.checkNotNullParameter(parent, "parent");
                View view = super.getView(position, convertView, parent);
                Intrinsics.checkNotNull(view, "null cannot be cast to non-null type android.widget.TextView");
                TextView v = (TextView) view;
                String item = getItem(position);
                Intrinsics.checkNotNull(item);
                String cls = item;
                v.setText(cls);
                v.setTextColor(Ui.INSTANCE.getTEXT());
                int s = Ui.INSTANCE.dp(this.$ctx, 18.0f);
                Drawable drawableIconFor = Ui.INSTANCE.iconFor(this.$ctx, cls);
                drawableIconFor.setBounds(0, 0, s, s);
                v.setCompoundDrawables(drawableIconFor, null, null, null);
                v.setCompoundDrawablePadding(Ui.INSTANCE.dp(this.$ctx, 10.0f));
                return v;
            }
        };
        lv.setAdapter((ListAdapter) r5);
        linearLayout.addView(lv, Ui.lp$default(Ui.INSTANCE, -1, Ui.INSTANCE.dp(ctx, 380.0f), 0.0f, 4, null));
        final AlertDialog dlg = theme(ctx).setTitle(title).setView(linearLayout).setNegativeButton("Cancel", (DialogInterface.OnClickListener) null).create();
        search.addTextChangedListener(new TextWatcher() { // from class: com.goydevv.studrblx.ui.Dialogs.pickClass.1
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence s, int a, int b, int c) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence s, int a, int b, int c) {
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable s) {
                String q = StringsKt.trim((CharSequence) String.valueOf(s)).toString().toLowerCase(Locale.ROOT);
                String str = "toLowerCase(...)";
                Intrinsics.checkNotNullExpressionValue(q, "toLowerCase(...)");
                shown.clear();
                ArrayList<String> arrayList = shown;
                Iterable iterable = insertable;
                Collection arrayList2 = new ArrayList();
                for (Object obj : iterable) {
                    String lowerCase = ((String) obj).toLowerCase(Locale.ROOT);
                    Intrinsics.checkNotNullExpressionValue(lowerCase, str);
                    String q2 = q;
                    String str2 = str;
                    if (StringsKt.contains((CharSequence) lowerCase, (CharSequence) q, false)) {
                        arrayList2.add(obj);
                    }
                    q = q2;
                    str = str2;
                }
                arrayList.addAll((List) arrayList2);
                r5.notifyDataSetChanged();
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.goydevv.studrblx.ui.Dialogs$$ExternalSyntheticLambda1
            @Override // android.widget.AdapterView.OnItemClickListener
            public final void onItemClick(AdapterView adapterView, View view, int i, long j) {
                Dialogs.pickClass$lambda$1(onPick, shown, dlg, adapterView, view, i, j);
            }
        });
        dlg.show();
    }

    static final void pickClass$lambda$1(Function1 $onPick, ArrayList $shown, AlertDialog $dlg, AdapterView adapterView, View view, int pos, long j) {
        Object obj = $shown.get(pos);
        Intrinsics.checkNotNullExpressionValue(obj, "get(...)");
        $onPick.invoke(obj);
        $dlg.dismiss();
    }

    private final Pair<LinearLayout, EditText> numField(Context ctx, String label, float v) {
        LinearLayout row = new LinearLayout(ctx);
        row.setOrientation(0);
        row.setGravity(16);
        row.addView(Ui.text$default(Ui.INSTANCE, ctx, label, 13.0f, Ui.INSTANCE.getDIM(), false, 16, null), Ui.lp$default(Ui.INSTANCE, Ui.INSTANCE.dp(ctx, 28.0f), -2, 0.0f, 4, null));
        EditText e = Ui.input$default(Ui.INSTANCE, ctx, DataTypesKt.fmtNum(v), null, true, false, 20, null);
        row.addView(e, Ui.INSTANCE.lp(0, -2, 1.0f));
        return TuplesKt.to(row, e);
    }

    private final float f(EditText e, float d) {
        Float floatOrNull = StringsKt.toFloatOrNull(StringsKt.trim((CharSequence) e.getText().toString()).toString());
        return floatOrNull != null ? floatOrNull.floatValue() : d;
    }

    static /* synthetic */ float f$default(Dialogs dialogs, EditText editText, float f, int i, Object obj) {
        if ((i & 2) != 0) {
            f = 0.0f;
        }
        return dialogs.f(editText, f);
    }

    /* JADX WARN: Code duplicated, block: B:126:0x070b  */
    /* JADX WARN: Code duplicated, block: B:131:0x0716  */
    /* JADX WARN: Code duplicated, block: B:132:0x071a  */
    /* JADX WARN: Code duplicated, block: B:134:0x071d  */
    /* JADX WARN: Code duplicated, block: B:137:0x072d  */
    public final void editValue(Context ctx, String name, final Object value, int type, String enumType, final Function1<Object, Unit> onSet) {
        CharSequence charSequence;
        Function0 result = null;
        boolean z;
        boolean multi = false;
        String str;
        EditText e = null;
        boolean z2;
        boolean z3 = false;
        List items = null;
        String[] names;
        Set<Map.Entry<String, Integer>> setEntrySet;
        Context ctx2 = ctx;
        Intrinsics.checkNotNullParameter(ctx2, "ctx");
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(onSet, "onSet");
        if (enumType != null && ((value instanceof EnumValue) || type == 18)) {
            LinkedHashMap<String, Integer> linkedHashMap = Enums.INSTANCE.getTypes().get(enumType);
            if (linkedHashMap == null || (setEntrySet = linkedHashMap.entrySet()) == null || (items = CollectionsKt.toList(setEntrySet)) == null) {
                items = CollectionsKt.emptyList();
            }
            if (!items.isEmpty()) {
                final List _items = items;
                Iterable iterable = items;
                Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
                Iterator it = iterable.iterator();
                while (it.hasNext()) {
                    arrayList.add((String) ((Map.Entry) it.next()).getKey());
                }
                String[] names2 = (String[]) ((List) arrayList).toArray(new String[0]);
                int cur = 0;
                Iterator it2 = items.iterator();
                while (it2.hasNext()) {
                    names = names2;
                    Object value2 = ((Map.Entry) it2.next()).getValue();
                    EnumValue enumValue = value instanceof EnumValue ? (EnumValue) value : null;
                    if (Intrinsics.areEqual(value2, enumValue != null ? Integer.valueOf(enumValue.getValue()) : null)) {
                        theme(ctx).setTitle(name + " (" + enumType + ")").setSingleChoiceItems(names, cur, new DialogInterface.OnClickListener() { // from class: com.goydevv.studrblx.ui.Dialogs$$ExternalSyntheticLambda4
                            @Override // android.content.DialogInterface.OnClickListener
                            public final void onClick(DialogInterface dialogInterface, int i) {
                                Dialogs.editValue$lambda$5(onSet, _items, dialogInterface, i);
                            }
                        }).setNegativeButton("Cancel", (DialogInterface.OnClickListener) null).show();
                        return;
                    } else {
                        cur++;
                        names2 = names;
                    }
                }
                names = names2;
                cur = -1;
                theme(ctx).setTitle(name + " (" + enumType + ")").setSingleChoiceItems(names, cur, new DialogInterface.OnClickListener() { // from class: com.goydevv.studrblx.ui.Dialogs$$ExternalSyntheticLambda4
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i) {
                        Dialogs.editValue$lambda$5(onSet, _items, dialogInterface, i);
                    }
                }).setNegativeButton("Cancel", (DialogInterface.OnClickListener) null).show();
                return;
            }
        }
        if (value instanceof Color3) {
            colorPicker(ctx2, name, (Color3) value, onSet);
            return;
        }
        if (value instanceof BrickColorV) {
            colorPicker(ctx2, name, ((BrickColorV) value).getColor(), new Function1() { // from class: com.goydevv.studrblx.ui.Dialogs$$ExternalSyntheticLambda7
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return Dialogs.editValue$lambda$6(onSet, obj);
                }
            });
            return;
        }
        LinearLayout linearLayout = new LinearLayout(ctx2);
        linearLayout.setOrientation(1);
        linearLayout.setPadding(Ui.INSTANCE.dp(ctx2, 16.0f), Ui.INSTANCE.dp(ctx2, 8.0f), Ui.INSTANCE.dp(ctx2, 16.0f), 0);
        if (value instanceof Vector3) {
            Iterable<Pair> iterableListOf = CollectionsKt.listOf(new Pair[]{TuplesKt.to("X", Float.valueOf(((Vector3) value).getX())), TuplesKt.to("Y", Float.valueOf(((Vector3) value).getY())), TuplesKt.to("Z", Float.valueOf(((Vector3) value).getZ()))});
            Collection arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterableListOf, 10));
            for (Pair pair : iterableListOf) {
                Iterable iterable2 = iterableListOf;
                Pair<LinearLayout, EditText> pairNumField = INSTANCE.numField(ctx2, (String) pair.component1(), ((Number) pair.component2()).floatValue());
                linearLayout.addView(pairNumField.getFirst());
                arrayList2.add(pairNumField.getSecond());
                iterableListOf = iterable2;
            }
            final List fields = (List) arrayList2;
            result = new Function0() { // from class: com.goydevv.studrblx.ui.Dialogs$$ExternalSyntheticLambda8
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return Dialogs.editValue$lambda$10(fields);
                }
            };
            charSequence = "Cancel";
        } else if (value instanceof Vector2) {
            Iterable<Pair> iterableListOf2 = CollectionsKt.listOf(new Pair[]{TuplesKt.to("X", Float.valueOf(((Vector2) value).getX())), TuplesKt.to("Y", Float.valueOf(((Vector2) value).getY()))});
            int i = 0;
            Collection arrayList3 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterableListOf2, 10));
            for (Pair pair2 : iterableListOf2) {
                int i2 = i;
                Pair<LinearLayout, EditText> pairNumField2 = INSTANCE.numField(ctx2, (String) pair2.component1(), ((Number) pair2.component2()).floatValue());
                linearLayout.addView(pairNumField2.getFirst());
                arrayList3.add(pairNumField2.getSecond());
                i = i2;
            }
            final List fields2 = (List) arrayList3;
            result = new Function0() { // from class: com.goydevv.studrblx.ui.Dialogs$$ExternalSyntheticLambda9
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return Dialogs.editValue$lambda$13(fields2);
                }
            };
            charSequence = "Cancel";
        } else if (value instanceof UDim2) {
            Iterable<Pair> iterableListOf3 = CollectionsKt.listOf(new Pair[]{TuplesKt.to("XS", Float.valueOf(((UDim2) value).getXs())), TuplesKt.to("XO", Float.valueOf(((UDim2) value).getXo())), TuplesKt.to("YS", Float.valueOf(((UDim2) value).getYs())), TuplesKt.to("YO", Float.valueOf(((UDim2) value).getYo()))});
            int i3 = 0;
            Collection arrayList4 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterableListOf3, 10));
            for (Pair pair3 : iterableListOf3) {
                int i4 = i3;
                Pair<LinearLayout, EditText> pairNumField3 = INSTANCE.numField(ctx2, (String) pair3.component1(), ((Number) pair3.component2()).floatValue());
                linearLayout.addView(pairNumField3.getFirst());
                arrayList4.add(pairNumField3.getSecond());
                i3 = i4;
            }
            final List fields3 = (List) arrayList4;
            result = new Function0() { // from class: com.goydevv.studrblx.ui.Dialogs$$ExternalSyntheticLambda10
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return Dialogs.editValue$lambda$16(fields3);
                }
            };
            charSequence = "Cancel";
        } else if (value instanceof UDim) {
            Iterable<Pair> iterableListOf4 = CollectionsKt.listOf(new Pair[]{TuplesKt.to("S", Float.valueOf(((UDim) value).getScale())), TuplesKt.to("O", Float.valueOf(((UDim) value).getOffset()))});
            int i5 = 0;
            Collection arrayList5 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterableListOf4, 10));
            for (Pair pair4 : iterableListOf4) {
                int i6 = i5;
                Pair<LinearLayout, EditText> pairNumField4 = INSTANCE.numField(ctx2, (String) pair4.component1(), ((Number) pair4.component2()).floatValue());
                linearLayout.addView(pairNumField4.getFirst());
                arrayList5.add(pairNumField4.getSecond());
                i5 = i6;
            }
            final List fields4 = (List) arrayList5;
            result = new Function0() { // from class: com.goydevv.studrblx.ui.Dialogs$$ExternalSyntheticLambda11
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return Dialogs.editValue$lambda$19(fields4);
                }
            };
            charSequence = "Cancel";
        } else if (value instanceof CFrame) {
            Vector3 o = ((CFrame) value).toOrientation().times(57.295776f);
            charSequence = "Cancel";
            linearLayout.addView(Ui.text$default(Ui.INSTANCE, ctx2, "Position", 12.0f, Ui.INSTANCE.getDIM(), false, 16, null));
            Iterable<Pair> iterableListOf5 = CollectionsKt.listOf(new Pair[]{TuplesKt.to("X", Float.valueOf(((CFrame) value).getX())), TuplesKt.to("Y", Float.valueOf(((CFrame) value).getY())), TuplesKt.to("Z", Float.valueOf(((CFrame) value).getZ()))});
            int i7 = 0;
            Collection arrayList6 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterableListOf5, 10));
            for (Pair pair5 : iterableListOf5) {
                String str2 = (String) pair5.component1();
                Iterable iterable3 = iterableListOf5;
                float fFloatValue = ((Number) pair5.component2()).floatValue();
                int i8 = i7;
                Pair<LinearLayout, EditText> pairNumField5 = INSTANCE.numField(ctx2, str2, fFloatValue);
                linearLayout.addView(pairNumField5.getFirst());
                arrayList6.add(pairNumField5.getSecond());
                i7 = i8;
                iterableListOf5 = iterable3;
            }
            final List p = (List) arrayList6;
            linearLayout.addView(Ui.text$default(Ui.INSTANCE, ctx2, "Orientation (degrees)", 12.0f, Ui.INSTANCE.getDIM(), false, 16, null));
            Iterable<Pair> iterableListOf6 = CollectionsKt.listOf(new Pair[]{TuplesKt.to("X", Float.valueOf(o.getX())), TuplesKt.to("Y", Float.valueOf(o.getY())), TuplesKt.to("Z", Float.valueOf(o.getZ()))});
            Collection arrayList7 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterableListOf6, 10));
            for (Pair pair6 : iterableListOf6) {
                Pair<LinearLayout, EditText> pairNumField6 = INSTANCE.numField(ctx2, (String) pair6.component1(), ((Number) pair6.component2()).floatValue());
                linearLayout.addView(pairNumField6.getFirst());
                arrayList7.add(pairNumField6.getSecond());
            }
            final List r = (List) arrayList7;
            result = new Function0() { // from class: com.goydevv.studrblx.ui.Dialogs$$ExternalSyntheticLambda12
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    List list = r;
                    List list2 = p;
                    return CFrame.INSTANCE.fromOrientation(Dialogs.f$default(Dialogs.INSTANCE, (EditText) list.get(0), 0.0f, 2, null) * 0.017453292f, Dialogs.f$default(Dialogs.INSTANCE, (EditText) list.get(1), 0.0f, 2, null) * 0.017453292f, Dialogs.f$default(Dialogs.INSTANCE, (EditText) list.get(2), 0.0f, 2, null) * 0.017453292f).withPosition(new Vector3(Dialogs.f$default(Dialogs.INSTANCE, (EditText) list2.get(0), 0.0f, 2, null), Dialogs.f$default(Dialogs.INSTANCE, (EditText) list2.get(1), 0.0f, 2, null), Dialogs.f$default(Dialogs.INSTANCE, (EditText) list2.get(2), 0.0f, 2, null)));
                }
            };
        } else {
            charSequence = "Cancel";
            if (value instanceof NumberRange) {
                Iterable<Pair> iterableListOf7 = CollectionsKt.listOf(new Pair[]{TuplesKt.to("Min", Float.valueOf(((NumberRange) value).getMin())), TuplesKt.to("Max", Float.valueOf(((NumberRange) value).getMax()))});
                Collection arrayList8 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterableListOf7, 10));
                for (Pair pair7 : iterableListOf7) {
                    Pair<LinearLayout, EditText> pairNumField7 = INSTANCE.numField(ctx2, (String) pair7.component1(), ((Number) pair7.component2()).floatValue());
                    linearLayout.addView(pairNumField7.getFirst());
                    arrayList8.add(pairNumField7.getSecond());
                }
                final List fields5 = (List) arrayList8;
                result = new Function0() { // from class: com.goydevv.studrblx.ui.Dialogs$$ExternalSyntheticLambda13
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return Dialogs.editValue$lambda$27(fields5);
                    }
                };
            } else if (value instanceof EnumValue) {
                final EditText e2 = Ui.input$default(Ui.INSTANCE, ctx2, String.valueOf(((EnumValue) value).getValue()), null, true, false, 20, null);
                linearLayout.addView(e2);
                result = new Function0() { // from class: com.goydevv.studrblx.ui.Dialogs$$ExternalSyntheticLambda14
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return Dialogs.editValue$lambda$28(e2);
                    }
                };
                ctx2 = ctx;
            } else if ((value instanceof Float) || (value instanceof Double) || (value instanceof Integer) || (value instanceof Long)) {
                ctx2 = ctx;
                final EditText e3 = Ui.input$default(Ui.INSTANCE, ctx2, value instanceof Float ? DataTypesKt.fmtNum(((Number) value).floatValue()) : value instanceof Double ? DataTypesKt.fmtNum(((Number) value).doubleValue()) : value.toString(), null, true, false, 20, null);
                linearLayout.addView(e3);
                result = new Function0() { // from class: com.goydevv.studrblx.ui.Dialogs$$ExternalSyntheticLambda15
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return Dialogs.editValue$lambda$29(e3, value);
                    }
                };
            } else {
                if (!(value instanceof String) && value != null) {
                    theme(ctx).setTitle(name).setMessage("Editing " + value.getClass().getSimpleName() + " values isn't supported yet.\n\n" + value).setPositiveButton("OK", (DialogInterface.OnClickListener) null).show();
                    return;
                }
                if (Intrinsics.areEqual(name, "Text") || Intrinsics.areEqual(name, "Value")) {
                    z = true;
                } else {
                    String str3 = value instanceof String ? (String) value : null;
                    if (str3 != null) {
                        z2 = false;
                        z = true;
                        if (StringsKt.contains((CharSequence) str3, '\n', false)) {
                            z3 = true;
                        }
                        if (!z3) {
                            multi = z2;
                        }
                        Ui ui = Ui.INSTANCE;
                        if (value instanceof String) {
                            str = (String) value;
                        } else {
                            str = null;
                        }
                        if (str == null) {
                            str = "";
                        }
                        e = Ui.input$default(ui, ctx, str, null, false, multi, 12, null);
                        if (multi) {
                            e.setMinLines(3);
                        }
                        final EditText _e = e;
                    linearLayout.addView(e);
                        result = new Function0() { // from class: com.goydevv.studrblx.ui.Dialogs$$ExternalSyntheticLambda5
                            @Override // kotlin.jvm.functions.Function0
                            public final Object invoke() {
                                return _e.getText().toString();
                            }
                        };
                        ctx2 = ctx;
                    } else {
                        z = true;
                        z2 = false;
                    }
                    z3 = z2;
                    if (!z3) {
                        multi = z2;
                    }
                    Ui ui2 = Ui.INSTANCE;
                    if (value instanceof String) {
                        str = (String) value;
                    } else {
                        str = null;
                    }
                    if (str == null) {
                        str = "";
                    }
                    e = Ui.input$default(ui2, ctx, str, null, false, multi, 12, null);
                    if (multi) {
                        e.setMinLines(3);
                    }
                    final EditText _e = e;
                    linearLayout.addView(e);
                    result = new Function0() { // from class: com.goydevv.studrblx.ui.Dialogs$$ExternalSyntheticLambda5
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return _e.getText().toString();
                        }
                    };
                    ctx2 = ctx;
                }
                multi = z;
                Ui ui3 = Ui.INSTANCE;
                if (value instanceof String) {
                    str = (String) value;
                } else {
                    str = null;
                }
                if (str == null) {
                    str = "";
                }
                e = Ui.input$default(ui3, ctx, str, null, false, multi, 12, null);
                if (multi) {
                    e.setMinLines(3);
                }
                final EditText _e = e;
                    linearLayout.addView(e);
                result = new Function0() { // from class: com.goydevv.studrblx.ui.Dialogs$$ExternalSyntheticLambda5
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return _e.getText().toString();
                    }
                };
                ctx2 = ctx;
            }
        }
        AlertDialog.Builder title = theme(ctx).setTitle(name);
        ScrollView scrollView = new ScrollView(ctx2);
        scrollView.addView(linearLayout);
        final Function0 _result = result;
        AlertDialog dlg = title.setView(scrollView).setPositiveButton("Set", new DialogInterface.OnClickListener() { // from class: com.goydevv.studrblx.ui.Dialogs$$ExternalSyntheticLambda6
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i9) {
                onSet.invoke(_result.invoke());
            }
        }).setNegativeButton(charSequence, (DialogInterface.OnClickListener) null).create();
        dlg.show();
    }

    static final void editValue$lambda$5(Function1 $onSet, List $items, DialogInterface d, int which) {
        Object value = ((Map.Entry) $items.get(which)).getValue();
        Intrinsics.checkNotNullExpressionValue(value, "<get-value>(...)");
        $onSet.invoke(new EnumValue(((Number) value).intValue()));
        d.dismiss();
    }

    static final Unit editValue$lambda$6(Function1 $onSet, Object c) {
        BrickColors brickColors = BrickColors.INSTANCE;
        Intrinsics.checkNotNull(c, "null cannot be cast to non-null type com.goydevv.studrblx.model.Color3");
        $onSet.invoke(new BrickColorV(brickColors.closest((Color3) c)));
        return Unit.INSTANCE;
    }

    static final Vector3 editValue$lambda$10(List $fields) {
        return new Vector3(f$default(INSTANCE, (EditText) $fields.get(0), 0.0f, 2, null), f$default(INSTANCE, (EditText) $fields.get(1), 0.0f, 2, null), f$default(INSTANCE, (EditText) $fields.get(2), 0.0f, 2, null));
    }

    static final Vector2 editValue$lambda$13(List $fields) {
        return new Vector2(f$default(INSTANCE, (EditText) $fields.get(0), 0.0f, 2, null), f$default(INSTANCE, (EditText) $fields.get(1), 0.0f, 2, null));
    }

    static final UDim2 editValue$lambda$16(List $fields) {
        return new UDim2(f$default(INSTANCE, (EditText) $fields.get(0), 0.0f, 2, null), (int) f$default(INSTANCE, (EditText) $fields.get(1), 0.0f, 2, null), f$default(INSTANCE, (EditText) $fields.get(2), 0.0f, 2, null), (int) f$default(INSTANCE, (EditText) $fields.get(3), 0.0f, 2, null));
    }

    static final UDim editValue$lambda$19(List $fields) {
        return new UDim(f$default(INSTANCE, (EditText) $fields.get(0), 0.0f, 2, null), (int) f$default(INSTANCE, (EditText) $fields.get(1), 0.0f, 2, null));
    }

    static final NumberRange editValue$lambda$27(List $fields) {
        return new NumberRange(f$default(INSTANCE, (EditText) $fields.get(0), 0.0f, 2, null), f$default(INSTANCE, (EditText) $fields.get(1), 0.0f, 2, null));
    }

    static final EnumValue editValue$lambda$28(EditText $e) {
        return new EnumValue((int) f$default(INSTANCE, $e, 0.0f, 2, null));
    }

    static final Object editValue$lambda$29(EditText $e, Object $value) {
        Double doubleOrNull = StringsKt.toDoubleOrNull(StringsKt.trim((CharSequence) $e.getText().toString()).toString());
        double d = doubleOrNull != null ? doubleOrNull.doubleValue() : 0.0d;
        if ($value instanceof Float) {
            return Float.valueOf((float) d);
        }
        if ($value instanceof Double) {
            return Double.valueOf(d);
        }
        return $value instanceof Integer ? Integer.valueOf((int) d) : Long.valueOf((long) d);
    }

    public final void colorPicker(final Context ctx, String title, Color3 initial, final Function1<Object, Unit> onSet) {
        Context ctx2 = ctx;
        Intrinsics.checkNotNullParameter(ctx2, "ctx");
        Intrinsics.checkNotNullParameter(title, "title");
        Intrinsics.checkNotNullParameter(initial, "initial");
        Intrinsics.checkNotNullParameter(onSet, "onSet");
        Ref.IntRef r = new Ref.IntRef();
        r.element = (int) (initial.getR() * 255.0f);
        Ref.IntRef g = new Ref.IntRef();
        g.element = (int) (initial.getG() * 255.0f);
        Ref.IntRef b = new Ref.IntRef();
        b.element = (int) (initial.getB() * 255.0f);
        LinearLayout linearLayout = new LinearLayout(ctx2);
        linearLayout.setOrientation(1);
        linearLayout.setPadding(Ui.INSTANCE.dp(ctx2, 16.0f), Ui.INSTANCE.dp(ctx2, 8.0f), Ui.INSTANCE.dp(ctx2, 16.0f), 0);
        int i = 0;
        int i2 = 1;
        View preview = new View(ctx2);
        final EditText hex = Ui.input$default(Ui.INSTANCE, ctx2, initial.toHex(), "Hex", false, false, 24, null);
        linearLayout.addView(preview, Ui.lp$default(Ui.INSTANCE, -1, Ui.INSTANCE.dp(ctx2, 40.0f), 0.0f, 4, null));
        ArrayList bars = new ArrayList();
        char c = 2;
        int i3 = 0;
        for (String label : CollectionsKt.listOf(new String[]{"R", "G", "B"})) {
            final int i4 = i3;
            int i5 = i3 + 1;
            LinearLayout linearLayout2 = new LinearLayout(ctx2);
            linearLayout2.setOrientation(i);
            linearLayout2.setGravity(16);
            ArrayList bars2 = bars;
            final View preview2 = preview;
            linearLayout2.addView(Ui.text$default(Ui.INSTANCE, ctx2, label, 13.0f, Ui.INSTANCE.getDIM(), false, 16, null), Ui.lp$default(Ui.INSTANCE, Ui.INSTANCE.dp(ctx2, 20.0f), -2, 0.0f, 4, null));
            SeekBar seekBar = new SeekBar(ctx2);
            seekBar.setMax(255);
            seekBar.setProgress(((Number) CollectionsKt.listOf(new Integer[]{Integer.valueOf(r.element), Integer.valueOf(g.element), Integer.valueOf(b.element)}).get(i4)).intValue());
            final Ref.IntRef r2 = r;
            final Context context = ctx2;
            final Ref.IntRef g2 = g;
            final Ref.IntRef b2 = b;
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.goydevv.studrblx.ui.Dialogs.colorPicker.1
                @Override // android.widget.SeekBar.OnSeekBarChangeListener
                public void onProgressChanged(SeekBar s, int p, boolean fromUser) {
                    Ref.IntRef intRef;
                    if (fromUser) {
                        switch (i4) {
                            case 0:
                                intRef = r2;
                                break;
                            case 1:
                                intRef = g2;
                                break;
                            default:
                                intRef = b2;
                                break;
                        }
                        intRef.element = p;
                        Dialogs.colorPicker$refresh$default(preview2, hex, r2, g2, b2, context, false, 64, null);
                    }
                }

                @Override // android.widget.SeekBar.OnSeekBarChangeListener
                public void onStartTrackingTouch(SeekBar s) {
                }

                @Override // android.widget.SeekBar.OnSeekBarChangeListener
                public void onStopTrackingTouch(SeekBar s) {
                }
            });
            bars2.add(seekBar);
            linearLayout2.addView(seekBar, Ui.INSTANCE.lp(0, -2, 1.0f));
            linearLayout.addView(linearLayout2);
            r = r2;
            g = g2;
            b = b2;
            preview = preview2;
            bars = bars2;
            c = 2;
            i3 = i5;
            i = 0;
            ctx2 = ctx;
        }
        final ArrayList bars3 = bars;
        final Ref.IntRef r3 = r;
        final Ref.IntRef g3 = g;
        final Ref.IntRef b3 = b;
        final View preview3 = preview;
        linearLayout.addView(hex);
        TextWatcher textWatcher = new TextWatcher() { // from class: com.goydevv.studrblx.ui.Dialogs.colorPicker.2
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence s, int a, int bb, int c2) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence s, int a, int bb, int c2) {
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable s) {
                Color3 c2 = Color3.INSTANCE.fromHex(String.valueOf(s));
                if (c2 == null) {
                    return;
                }
                int nr = (int) ((c2.getR() * 255.0f) + 0.5f);
                int ng = (int) ((c2.getG() * 255.0f) + 0.5f);
                int nb = (int) ((c2.getB() * 255.0f) + 0.5f);
                if (nr == r3.element && ng == g3.element && nb == b3.element) {
                    return;
                }
                r3.element = nr;
                g3.element = ng;
                b3.element = nb;
                ((SeekBar) bars3.get(0)).setProgress(r3.element);
                ((SeekBar) bars3.get(1)).setProgress(g3.element);
                ((SeekBar) bars3.get(2)).setProgress(b3.element);
                Dialogs.colorPicker$refresh(preview3, hex, r3, g3, b3, ctx, false);
            }
        };
        Ref.IntRef g4 = g3;
        Ref.IntRef b4 = b3;
        EditText hex2 = hex;
        hex2.addTextChangedListener(textWatcher);
        Context context2 = ctx;
        TextView textViewText$default = Ui.text$default(Ui.INSTANCE, context2, "BrickColor palette", 12.0f, Ui.INSTANCE.getDIM(), false, 16, null);
        textViewText$default.setPadding(0, Ui.INSTANCE.dp(context2, 8.0f), 0, Ui.INSTANCE.dp(context2, 4.0f));
        linearLayout.addView(textViewText$default);
        GridLayout gridLayout = new GridLayout(context2);
        gridLayout.setColumnCount(11);
        Iterator<Integer> it = BrickColors.INSTANCE.getAll().iterator();
        while (it.hasNext()) {
            int n = it.next().intValue();
            final Color3 c2 = BrickColors.INSTANCE.color(n);
            View view = new View(context2);
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setColor(c2.toArgb());
            final Ref.IntRef g5 = g4;
            gradientDrawable.setStroke(i2, ViewCompat.MEASURED_STATE_MASK);
            view.setBackground(gradientDrawable);
            final Ref.IntRef r4 = r;
            final Context context3 = context2;
            final EditText hex3 = hex2;
            final View preview4 = preview;
            final Ref.IntRef b5 = b4;
            context2 = context3;
            view.setOnClickListener(new View.OnClickListener() { // from class: com.goydevv.studrblx.ui.Dialogs$$ExternalSyntheticLambda2
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    Dialogs.colorPicker$lambda$41$lambda$40(r4, c2, g5, b5, bars3, preview4, hex3, context3, view2);
                }
            });
            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
            layoutParams.width = Ui.INSTANCE.dp(context2, 22.0f);
            layoutParams.height = Ui.INSTANCE.dp(context2, 22.0f);
            layoutParams.setMargins(2, 2, 2, 2);
            Unit unit = Unit.INSTANCE;
            gridLayout.addView(view, layoutParams);
            r = r4;
            b4 = b5;
            preview = preview4;
            hex2 = hex3;
            i2 = 1;
            g4 = g5;
        }
        final Ref.IntRef r5 = r;
        final Ref.IntRef g6 = g4;
        final Ref.IntRef b6 = b4;
        linearLayout.addView(gridLayout);
        Context context4 = context2;
        colorPicker$refresh$default(preview, hex2, r5, g6, b6, context4, false, 64, null);
        AlertDialog.Builder title2 = theme(ctx).setTitle(title);
        ScrollView scrollView = new ScrollView(context4);
        scrollView.addView(linearLayout);
        title2.setView(scrollView).setPositiveButton("Set", new DialogInterface.OnClickListener() { // from class: com.goydevv.studrblx.ui.Dialogs$$ExternalSyntheticLambda3
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i6) {
                onSet.invoke(Color3.INSTANCE.fromRGB(r5.element, g6.element, b6.element));
            }
        }).setNegativeButton("Cancel", (DialogInterface.OnClickListener) null).show();
    }

    static /* synthetic */ void colorPicker$refresh$default(View view, EditText editText, Ref.IntRef intRef, Ref.IntRef intRef2, Ref.IntRef intRef3, Context context, boolean z, int i, Object obj) {
        if ((i & 64) != 0) {
            z = true;
        }
        colorPicker$refresh(view, editText, intRef, intRef2, intRef3, context, z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void colorPicker$refresh(View preview, EditText hex, Ref.IntRef r, Ref.IntRef g, Ref.IntRef b, Context $ctx, boolean updateHex) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(Color3.INSTANCE.fromRGB(r.element, g.element, b.element).toArgb());
        gradientDrawable.setCornerRadius(Ui.INSTANCE.dp($ctx, 4.0f));
        preview.setBackground(gradientDrawable);
        if (updateHex) {
            hex.setText(Color3.INSTANCE.fromRGB(r.element, g.element, b.element).toHex());
        }
    }

    static final void colorPicker$lambda$41$lambda$40(Ref.IntRef $r, Color3 $c, Ref.IntRef $g, Ref.IntRef $b, ArrayList $bars, View $preview, EditText $hex, Context $ctx, View it) {
        $r.element = (int) (($c.getR() * 255.0f) + 0.5f);
        $g.element = (int) (($c.getG() * 255.0f) + 0.5f);
        $b.element = (int) (($c.getB() * 255.0f) + 0.5f);
        ((SeekBar) $bars.get(0)).setProgress($r.element);
        ((SeekBar) $bars.get(1)).setProgress($g.element);
        ((SeekBar) $bars.get(2)).setProgress($b.element);
        colorPicker$refresh$default($preview, $hex, $r, $g, $b, $ctx, false, 64, null);
    }

    public final void confirm(Context ctx, String title, String msg, String ok, final Function0<Unit> onOk) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(title, "title");
        Intrinsics.checkNotNullParameter(msg, "msg");
        Intrinsics.checkNotNullParameter(ok, "ok");
        Intrinsics.checkNotNullParameter(onOk, "onOk");
        theme(ctx).setTitle(title).setMessage(msg).setPositiveButton(ok, new DialogInterface.OnClickListener() { // from class: com.goydevv.studrblx.ui.Dialogs$$ExternalSyntheticLambda16
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                onOk.invoke();
            }
        }).setNegativeButton("Cancel", (DialogInterface.OnClickListener) null).show();
    }

    public final void prompt(Context ctx, String title, String initial, final Function1<? super String, Unit> onOk) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(title, "title");
        Intrinsics.checkNotNullParameter(initial, "initial");
        Intrinsics.checkNotNullParameter(onOk, "onOk");
        final EditText e = Ui.input$default(Ui.INSTANCE, ctx, initial, null, false, false, 28, null);
        LinearLayout linearLayout = new LinearLayout(ctx);
        linearLayout.setPadding(Ui.INSTANCE.dp(ctx, 16.0f), Ui.INSTANCE.dp(ctx, 8.0f), Ui.INSTANCE.dp(ctx, 16.0f), 0);
        linearLayout.addView(e, Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null));
        theme(ctx).setTitle(title).setView(linearLayout).setPositiveButton("OK", new DialogInterface.OnClickListener() { // from class: com.goydevv.studrblx.ui.Dialogs$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                onOk.invoke(e.getText().toString());
            }
        }).setNegativeButton("Cancel", (DialogInterface.OnClickListener) null).show();
    }

    public final AlertDialog.Builder themed(Context ctx) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        return theme(ctx);
    }
}

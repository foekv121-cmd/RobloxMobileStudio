package com.goydevv.studrblx.lua;

import com.goydevv.studrblx.model.Color3;
import java.util.Map;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.MapsKt;
import kotlin.collections.SetsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;
import org.luaj.vm2.LuaBoolean;
import org.luaj.vm2.LuaInteger;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

/* JADX INFO: compiled from: RobloxGlobals.kt */
/* JADX INFO: loaded from: classes4.dex */
public final class StudioSettings {
    public static final StudioSettings INSTANCE = new StudioSettings();
    private static final Map<String, Color3> palette = MapsKt.mapOf(TuplesKt.to("MainBackground", INSTANCE.rgb(46, 46, 46)), TuplesKt.to("Titlebar", INSTANCE.rgb(53, 53, 53)), TuplesKt.to("TitlebarText", INSTANCE.rgb(170, 170, 170)), TuplesKt.to("Dropdown", INSTANCE.rgb(46, 46, 46)), TuplesKt.to("Tooltip", INSTANCE.rgb(66, 66, 66)), TuplesKt.to("Notification", INSTANCE.rgb(66, 66, 66)), TuplesKt.to("ScrollBar", INSTANCE.rgb(56, 56, 56)), TuplesKt.to("ScrollBarBackground", INSTANCE.rgb(41, 41, 41)), TuplesKt.to("TabBar", INSTANCE.rgb(46, 46, 46)), TuplesKt.to("Tab", INSTANCE.rgb(53, 53, 53)), TuplesKt.to("FilterButtonDefault", INSTANCE.rgb(46, 46, 46)), TuplesKt.to("FilterButtonHover", INSTANCE.rgb(66, 66, 66)), TuplesKt.to("FilterButtonChecked", INSTANCE.rgb(30, 30, 30)), TuplesKt.to("FilterButtonAccent", INSTANCE.rgb(53, 181, 255)), TuplesKt.to("FilterButtonBorder", INSTANCE.rgb(26, 26, 26)), TuplesKt.to("RibbonTab", INSTANCE.rgb(53, 53, 53)), TuplesKt.to("RibbonTabTopBar", INSTANCE.rgb(53, 181, 255)), TuplesKt.to("RibbonButton", INSTANCE.rgb(37, 37, 37)), TuplesKt.to("Button", INSTANCE.rgb(60, 60, 60)), TuplesKt.to("ButtonBorder", INSTANCE.rgb(34, 34, 34)), TuplesKt.to("ButtonText", INSTANCE.rgb(204, 204, 204)), TuplesKt.to("MainButton", INSTANCE.rgb(0, 162, 255)), TuplesKt.to("DialogButton", INSTANCE.rgb(60, 60, 60)), TuplesKt.to("DialogButtonText", INSTANCE.rgb(204, 204, 204)), TuplesKt.to("DialogButtonBorder", INSTANCE.rgb(34, 34, 34)), TuplesKt.to("DialogMainButton", INSTANCE.rgb(0, 162, 255)), TuplesKt.to("DialogMainButtonText", INSTANCE.rgb(255, 255, 255)), TuplesKt.to("InputFieldBackground", INSTANCE.rgb(37, 37, 37)), TuplesKt.to("InputFieldBorder", INSTANCE.rgb(26, 26, 26)), TuplesKt.to("CheckedFieldBackground", INSTANCE.rgb(37, 37, 37)), TuplesKt.to("CheckedFieldBorder", INSTANCE.rgb(34, 34, 34)), TuplesKt.to("CheckedFieldIndicator", INSTANCE.rgb(53, 181, 255)), TuplesKt.to("HeaderSection", INSTANCE.rgb(53, 53, 53)), TuplesKt.to("Midlight", INSTANCE.rgb(34, 34, 34)), TuplesKt.to("Mid", INSTANCE.rgb(34, 34, 34)), TuplesKt.to("Dark", INSTANCE.rgb(34, 34, 34)), TuplesKt.to("Light", INSTANCE.rgb(66, 66, 66)), TuplesKt.to("Shadow", INSTANCE.rgb(26, 26, 26)), TuplesKt.to("Item", INSTANCE.rgb(46, 46, 46)), TuplesKt.to("TableItem", INSTANCE.rgb(46, 46, 46)), TuplesKt.to("Separator", INSTANCE.rgb(34, 34, 34)), TuplesKt.to("Border", INSTANCE.rgb(26, 26, 26)), TuplesKt.to("MainText", INSTANCE.rgb(204, 204, 204)), TuplesKt.to("SubText", INSTANCE.rgb(170, 170, 170)), TuplesKt.to("DimmedText", INSTANCE.rgb(102, 102, 102)), TuplesKt.to("BrightText", INSTANCE.rgb(229, 229, 229)), TuplesKt.to("InfoText", INSTANCE.rgb(170, 170, 170)), TuplesKt.to("LinkText", INSTANCE.rgb(53, 181, 255)), TuplesKt.to("WarningText", INSTANCE.rgb(255, 142, 60)), TuplesKt.to("ErrorText", INSTANCE.rgb(255, 68, 68)), TuplesKt.to("SensitiveText", INSTANCE.rgb(177, 138, 255)), TuplesKt.to("ScriptBackground", INSTANCE.rgb(37, 37, 37)), TuplesKt.to("ScriptText", INSTANCE.rgb(204, 204, 204)), TuplesKt.to("ScriptSelectionBackground", INSTANCE.rgb(11, 90, 175)), TuplesKt.to("StatusBar", INSTANCE.rgb(46, 46, 46)), TuplesKt.to("CurrentMarker", INSTANCE.rgb(66, 66, 66)), TuplesKt.to("ColorPickerFrame", INSTANCE.rgb(34, 34, 34)), TuplesKt.to("GameSettingsTableItem", INSTANCE.rgb(53, 53, 53)), TuplesKt.to("GameSettingsTooltip", INSTANCE.rgb(66, 66, 66)), TuplesKt.to("EmulatorBar", INSTANCE.rgb(41, 41, 41)), TuplesKt.to("EmulatorDropDown", INSTANCE.rgb(37, 37, 37)), TuplesKt.to("ViewPortBackground", INSTANCE.rgb(37, 37, 37)), TuplesKt.to("AttributeCog", INSTANCE.rgb(170, 170, 170)));

    private StudioSettings() {
    }

    private final Color3 rgb(int r, int g, int b) {
        return Color3.INSTANCE.fromRGB(r, g, b);
    }

    private final Color3 shade(Color3 c, float k) {
        return new Color3(RangesKt.coerceIn(c.getR() * k, 0.0f, 1.0f), RangesKt.coerceIn(c.getG() * k, 0.0f, 1.0f), RangesKt.coerceIn(c.getB() * k, 0.0f, 1.0f));
    }

    public final Color3 color(String styleName, String modifier) {
        Intrinsics.checkNotNullParameter(styleName, "styleName");
        Intrinsics.checkNotNullParameter(modifier, "modifier");
        Color3 base = palette.get(styleName);
        if (base == null) {
            if (StringsKt.contains((CharSequence) styleName, (CharSequence) "Text", false)) {
                base = rgb(204, 204, 204);
            } else {
                base = (StringsKt.contains((CharSequence) styleName, (CharSequence) "Border", false) || StringsKt.contains((CharSequence) styleName, (CharSequence) "Separator", false)) ? rgb(26, 26, 26) : rgb(46, 46, 46);
            }
        }
        boolean text = StringsKt.contains((CharSequence) styleName, (CharSequence) "Text", false);
        switch (modifier.hashCode()) {
            case 69916956:
                return (modifier.equals("Hover") && !text) ? shade(base, 1.18f) : base;
            case 335584924:
                if (modifier.equals("Disabled")) {
                    return text ? rgb(85, 85, 85) : shade(base, 0.9f);
                }
                return base;
            case 1256216251:
                if (!modifier.equals("Selected")) {
                    return base;
                }
                if (text) {
                    return rgb(255, 255, 255);
                }
                return SetsKt.setOf(new String[]{"Item", "TableItem", "Button", "RibbonButton"}).contains(styleName) ? rgb(11, 90, 175) : shade(base, 1.25f);
            case 1346388994:
                if (modifier.equals("Pressed") && !text) {
                    return shade(base, 0.85f);
                }
                return base;
            default:
                return base;
        }
    }

    private final String enumName(LuaValue v) {
        String name;
        EnumItemV enumItemV = null;
        if (v.isuserdata()) {
            Object obj = v.touserdata();
            if (obj instanceof EnumItemV) {
                enumItemV = (EnumItemV) obj;
            }
        }
        if (enumItemV != null && (name = enumItemV.getName()) != null) {
            return name;
        }
        if (!v.isstring()) {
            return "";
        }
        String str = v.tojstring();
        Intrinsics.checkNotNullExpressionValue(str, "tojstring(...)");
        return str;
    }

    public final LuaTable create(final LuaRuntime rt) {
        Intrinsics.checkNotNullParameter(rt, "rt");
        LuaTable theme = new LuaTable();
        theme.set("Name", "Dark");
        theme.set("GetColor", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.StudioSettings$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return StudioSettings.create$lambda$1((Varargs) obj);
            }
        }));
        LuaTable luaTable = new LuaTable();
        luaTable.set("__type", "Instance");
        theme.setmetatable(luaTable);
        final LuaTable studio = new LuaTable();
        studio.set("Theme", theme);
        studio.set("ThemeChanged", rt.getApi().signal(studio, "ThemeChanged"));
        studio.set("GetPropertyChangedSignal", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.StudioSettings$$ExternalSyntheticLambda1
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return StudioSettings.create$lambda$3(rt, studio, (Varargs) obj);
            }
        }));
        studio.set("Grid Size", 1);
        studio.set("Script Editor Color Preset", LuaTypes.INSTANCE.wrap(new EnumItemV("StudioScriptEditorColorPresets", "RobloxDefault", 0)));
        LuaTable root = new LuaTable();
        root.set("Studio", studio);
        root.set("GetFFlag", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.StudioSettings$$ExternalSyntheticLambda2
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return StudioSettings.create$lambda$4((Varargs) obj);
            }
        }));
        root.set("GetFVariable", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.StudioSettings$$ExternalSyntheticLambda3
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return StudioSettings.create$lambda$5((Varargs) obj);
            }
        }));
        root.set("GetFInt", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.StudioSettings$$ExternalSyntheticLambda4
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return StudioSettings.create$lambda$6((Varargs) obj);
            }
        }));
        LuaTable luaTable2 = new LuaTable();
        luaTable2.set("QualityLevel", LuaTypes.INSTANCE.wrap(new EnumItemV("QualityLevel", "Automatic", 0)));
        Unit unit = Unit.INSTANCE;
        root.set("Rendering", luaTable2);
        LuaTable lazy = new LuaTable();
        lazy.set(LuaValue.INDEX, LuaTypesKt.fn2(new Function2() { // from class: com.goydevv.studrblx.lua.StudioSettings$$ExternalSyntheticLambda5
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return StudioSettings.create$lambda$9((LuaValue) obj, (LuaValue) obj2);
            }
        }));
        root.setmetatable(lazy);
        return root;
    }

    static final Varargs create$lambda$1(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaTypes luaTypes = LuaTypes.INSTANCE;
        StudioSettings studioSettings = INSTANCE;
        StudioSettings studioSettings2 = INSTANCE;
        LuaValue luaValueArg = a.arg(2);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        String strEnumName = studioSettings2.enumName(luaValueArg);
        StudioSettings studioSettings3 = INSTANCE;
        LuaValue luaValueArg2 = a.arg(3);
        Intrinsics.checkNotNullExpressionValue(luaValueArg2, "arg(...)");
        String strEnumName2 = studioSettings3.enumName(luaValueArg2);
        if (strEnumName2.length() == 0) {
            strEnumName2 = "Default";
        }
        return luaTypes.wrap(studioSettings.color(strEnumName, strEnumName2));
    }

    static final Varargs create$lambda$3(LuaRuntime $rt, LuaTable $studio, Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        return $rt.getApi().signal($studio, "Changed:" + a.arg(2).tojstring());
    }

    static final Varargs create$lambda$4(Varargs it) {
        Intrinsics.checkNotNullParameter(it, "it");
        LuaBoolean FALSE = LuaValue.FALSE;
        Intrinsics.checkNotNullExpressionValue(FALSE, "FALSE");
        return FALSE;
    }

    static final Varargs create$lambda$5(Varargs it) {
        Intrinsics.checkNotNullParameter(it, "it");
        LuaString luaStringValueOf = LuaValue.valueOf("");
        Intrinsics.checkNotNullExpressionValue(luaStringValueOf, "valueOf(...)");
        return luaStringValueOf;
    }

    static final Varargs create$lambda$6(Varargs it) {
        Intrinsics.checkNotNullParameter(it, "it");
        LuaInteger luaIntegerValueOf = LuaValue.valueOf(0);
        Intrinsics.checkNotNullExpressionValue(luaIntegerValueOf, "valueOf(...)");
        return luaIntegerValueOf;
    }

    static final LuaValue create$lambda$9(LuaValue t, LuaValue k) {
        Intrinsics.checkNotNullParameter(t, "t");
        Intrinsics.checkNotNullParameter(k, "k");
        LuaTable luaTable = new LuaTable();
        ((LuaTable) t).rawset(k, luaTable);
        return luaTable;
    }
}

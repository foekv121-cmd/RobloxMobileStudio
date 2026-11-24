package com.goydevv.studrblx.model;

import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

/* JADX INFO: compiled from: Instance.kt */
/* JADX INFO: loaded from: classes4.dex */
public final class PropNames {
    private static final Map<String, String> fromDisplay;
    public static final PropNames INSTANCE = new PropNames();
    private static final Map<String, String> toDisplay = MapsKt.mapOf(TuplesKt.to("size", "Size"), TuplesKt.to("Color3uint8", "Color"), TuplesKt.to("shape", "Shape"), TuplesKt.to("formFactorRaw", "FormFactor"), TuplesKt.to("Health_XML", "Health"), TuplesKt.to("MaterialVariantSerialized", "MaterialVariant"), TuplesKt.to("xmlRead_MaxDistance_3", "RollOffMaxDistance"), TuplesKt.to("xmlRead_MinDistance_3", "RollOffMinDistance"), TuplesKt.to("BackgroundColor", "BackgroundBrickColor"), TuplesKt.to("Font_Serialized", "FontFace"), TuplesKt.to("Use2022MaterialsXml", "Use2022Materials"));

    private PropNames() {
    }

    static {
        Iterable<Map.Entry<String, String>> iterableEntrySet = toDisplay.entrySet();
        Map linkedHashMap = new LinkedHashMap(RangesKt.coerceAtLeast(MapsKt.mapCapacity(CollectionsKt.collectionSizeOrDefault(iterableEntrySet, 10)), 16));
        for (Map.Entry<String, String> entry : iterableEntrySet) {
            Pair pair = TuplesKt.to(entry.getValue(), entry.getKey());
            linkedHashMap.put(pair.getFirst(), pair.getSecond());
        }
        fromDisplay = linkedHashMap;
    }

    public final String display(String n) {
        Intrinsics.checkNotNullParameter(n, "n");
        String str = toDisplay.get(n);
        return str == null ? n : str;
    }

    public final String serialized(String n) {
        Intrinsics.checkNotNullParameter(n, "n");
        String str = fromDisplay.get(n);
        return str == null ? n : str;
    }

    public final String resolve(Instance inst, String displayName) {
        String s;
        Intrinsics.checkNotNullParameter(inst, "inst");
        Intrinsics.checkNotNullParameter(displayName, "displayName");
        return (inst.getProps().containsKey(displayName) || (s = fromDisplay.get(displayName)) == null || !inst.getProps().containsKey(s)) ? displayName : s;
    }
}

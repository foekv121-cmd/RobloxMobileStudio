package com.goydevv.studrblx.format;

import com.goydevv.studrblx.model.Axes;
import com.goydevv.studrblx.model.BinaryString;
import com.goydevv.studrblx.model.BrickColorV;
import com.goydevv.studrblx.model.CFrame;
import com.goydevv.studrblx.model.Color3;
import com.goydevv.studrblx.model.ColorKey;
import com.goydevv.studrblx.model.ColorSequence;
import com.goydevv.studrblx.model.ContentValue;
import com.goydevv.studrblx.model.EnumValue;
import com.goydevv.studrblx.model.Faces;
import com.goydevv.studrblx.model.FontValue;
import com.goydevv.studrblx.model.Instance;
import com.goydevv.studrblx.model.NumberKey;
import com.goydevv.studrblx.model.NumberRange;
import com.goydevv.studrblx.model.NumberSequence;
import com.goydevv.studrblx.model.OptionalCFrame;
import com.goydevv.studrblx.model.PhysicalProps;
import com.goydevv.studrblx.model.PropType;
import com.goydevv.studrblx.model.RawPropValue;
import com.goydevv.studrblx.model.Ray;
import com.goydevv.studrblx.model.Rect;
import com.goydevv.studrblx.model.SecurityCaps;
import com.goydevv.studrblx.model.SharedStr;
import com.goydevv.studrblx.model.UDim;
import com.goydevv.studrblx.model.UDim2;
import com.goydevv.studrblx.model.UniqueId;
import com.goydevv.studrblx.model.Vector2;
import com.goydevv.studrblx.model.Vector3;
import com.goydevv.studrblx.model.Vector3int16;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.IndexedValue;
import kotlin.collections.MapsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.Charsets;

/* JADX INFO: compiled from: RbxBinary.kt */
/* JADX INFO: loaded from: classes5.dex */
public final class RbxBinaryWriter {
    public static final RbxBinaryWriter INSTANCE = new RbxBinaryWriter();

    private RbxBinaryWriter() {
    }

    /* JADX WARN: Code duplicated, block: B:140:0x046f A[LOOP:19: B:124:0x0421->B:140:0x046f, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:147:0x0486  */
    /* JADX WARN: Code duplicated, block: B:175:0x0552  */
    /* JADX WARN: Code duplicated, block: B:177:0x0562  */
    /* JADX WARN: Code duplicated, block: B:178:0x056d  */
    /* JADX WARN: Code duplicated, block: B:180:0x0579  */
    /* JADX WARN: Code duplicated, block: B:181:0x0582  */
    /* JADX WARN: Code duplicated, block: B:250:0x046d A[SYNTHETIC] */
    public final byte[] write(List<? extends Instance> roots, Map<String, String> meta) {
        Integer num;
        RawPropValue rawPropValue;
        LinkedHashSet linkedHashSet;
        IdentityHashMap<Instance, Integer> identityHashMap;
        boolean z = false;
        boolean z2;
        int i = 0;
        Integer num2;
        Object next;
        int i2 = 0;
        int iIntValue = 0;
        List<Instance> list;
        Collection arrayList;
        Iterable iterable;
        Object objM315default;
        boolean z3;
        Object arrayList2;
        Intrinsics.checkNotNullParameter(roots, "roots");
        Intrinsics.checkNotNullParameter(meta, "meta");
        ArrayList all = new ArrayList();
        Iterator it = roots.iterator();
        while (it.hasNext()) {
            write$collect(all, (Instance) it.next());
        }
        IdentityHashMap<Instance, Integer> identityHashMap2 = new IdentityHashMap<>();
        HashSet used = new HashSet();
        Iterator it2 = all.iterator();
        Intrinsics.checkNotNullExpressionValue(it2, "iterator(...)");
        while (it2.hasNext()) {
            Instance i3 = (Instance) it2.next();
            if (i3.getLoadedRef() >= 0 && used.add(Integer.valueOf(i3.getLoadedRef()))) {
                identityHashMap2.put(i3, Integer.valueOf(i3.getLoadedRef()));
            }
        }
        Integer num3 = used.isEmpty() ? null : (Integer) java.util.Collections.max(used);
        int next2 = (num3 != null ? num3.intValue() : -1) + 1;
        Iterator it3 = all.iterator();
        Intrinsics.checkNotNullExpressionValue(it3, "iterator(...)");
        int next3 = next2;
        while (it3.hasNext()) {
            Instance i4 = (Instance) it3.next();
            if (!identityHashMap2.containsKey(i4)) {
                identityHashMap2.put(i4, Integer.valueOf(next3));
                next3++;
            }
        }
        Map linkedHashMap = new LinkedHashMap();
        for (Object obj : all) {
            String className = ((Instance) obj).getClassName();
            Object obj2 = linkedHashMap.get(className);
            if (obj2 == null) {
                arrayList2 = new ArrayList();
                linkedHashMap.put(className, arrayList2);
            } else {
                arrayList2 = (ArrayList) obj2;
            }
            ((List) arrayList2).add(obj);
        }
        SortedMap byClass = MapsKt.toSortedMap(linkedHashMap);
        Set setKeySet = byClass.keySet();
        Intrinsics.checkNotNullExpressionValue(setKeySet, "<get-keys>(...)");
        List classNames = CollectionsKt.toList(setKeySet);
        LinkedHashMap sstr = new LinkedHashMap();
        Iterator it4 = all.iterator();
        Intrinsics.checkNotNullExpressionValue(it4, "iterator(...)");
        while (it4.hasNext()) {
            for (Object v : ((Instance) it4.next()).getProps().values()) {
                if (v instanceof SharedStr) {
                    String key = Base64.getEncoder().encodeToString(((SharedStr) v).getBytes());
                    if (!sstr.containsKey(key)) {
                        sstr.put(key, TuplesKt.to(Integer.valueOf(sstr.size()), ((SharedStr) v).getBytes()));
                    }
                }
            }
        }
        ByteWriter file = new ByteWriter();
        byte[] bytes = "<roblox!".getBytes(Charsets.ISO_8859_1);
        Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
        file.bytes(bytes);
        file.bytes(new byte[]{-119, -1, 13, 10, 26, 10});
        file.u16(0);
        file.i32(classNames.size());
        file.i32(all.size());
        file.bytes(new byte[8]);
        if (!meta.isEmpty()) {
            ByteWriter w = new ByteWriter();
            w.i32(meta.size());
            for (Map.Entry<String, String> entry : meta.entrySet()) {
                String k = entry.getKey();
                String v2 = entry.getValue();
                w.string(k);
                w.string(v2);
            }
            write$chunk(file, "META", w.toByteArray());
        }
        if (!sstr.isEmpty()) {
            ByteWriter w2 = new ByteWriter();
            w2.i32(0);
            w2.i32(sstr.size());
            Iterator it5 = sstr.entrySet().iterator();
            while (it5.hasNext()) {
                Pair p = (Pair) ((Map.Entry) it5.next()).getValue();
                byte[] bArrDigest; try { bArrDigest = MessageDigest.getInstance("MD5").digest((byte[]) p.getSecond()); } catch (java.security.NoSuchAlgorithmException _e) { throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_e); }
                Intrinsics.checkNotNullExpressionValue(bArrDigest, "digest(...)");
                w2.bytes(bArrDigest);
                w2.string((byte[]) p.getSecond());
            }
            write$chunk(file, "SSTR", w2.toByteArray());
        }
        List list2 = classNames;
        int i5 = 0;
        int i6 = 0;
        Iterator it6 = list2.iterator();
        while (it6.hasNext()) {
            Object next4 = it6.next();
            int i7 = i6 + 1;
            if (i6 < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            String str = (String) next4;
            Object obj3 = byClass.get(str);
            Intrinsics.checkNotNull(obj3);
            HashSet used2 = used;
            List list3 = (List) obj3;
            ByteWriter byteWriter = new ByteWriter();
            Iterable iterable2 = list2;
            byteWriter.i32(i6);
            Intrinsics.checkNotNull(str);
            byteWriter.string(str);
            int i8 = i5;
            List list4 = list3;
            if (!(list4 instanceof Collection) || !list4.isEmpty()) {
                Iterator it7 = list4.iterator();
                while (true) {
                    if (!it7.hasNext()) {
                        z3 = false;
                        break;
                    }
                    if (((Instance) it7.next()).getIsService()) {
                        z3 = true;
                        break;
                    }
                }
            } else {
                z3 = false;
            }
            byteWriter.u8(z3 ? 1 : 0);
            byteWriter.i32(list3.size());
            int size = list3.size();
            boolean z4 = z3;
            int[] iArr = new int[size];
            Iterator it8 = it6;
            int i9 = 0;
            while (i9 < size) {
                int i10 = size;
                Integer num4 = identityHashMap2.get(list3.get(i9));
                Intrinsics.checkNotNull(num4);
                iArr[i9] = num4.intValue();
                i9++;
                size = i10;
            }
            byteWriter.refArray(iArr);
            if (z4) {
                int size2 = list3.size();
                int i11 = 0;
                while (i11 < size2) {
                    byteWriter.u8(1);
                    i11++;
                }
            }
            write$chunk(file, "INST", byteWriter.toByteArray());
            i6 = i7;
            list2 = (List) iterable2;
            used = used2;
            i5 = i8;
            it6 = it8;
        }
        int i12 = 0;
        for (Object obj4 : classNames) {
            int i13 = i12 + 1;
            if (i12 < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            String str2 = (String) obj4;
            Object obj5 = byClass.get(str2);
            Intrinsics.checkNotNull(obj5);
            List list5 = (List) obj5;
            LinkedHashSet linkedHashSet2 = new LinkedHashSet();
            String str3 = "Name";
            linkedHashSet2.add("Name");
            Iterator it9 = list5.iterator();
            while (it9.hasNext()) {
                linkedHashSet2.addAll(((Instance) it9.next()).getProps().keySet());
            }
            Iterator it10 = linkedHashSet2.iterator();
            Intrinsics.checkNotNullExpressionValue(it10, "iterator(...)");
            while (it10.hasNext()) {
                Object next5 = it10.next();
                Intrinsics.checkNotNullExpressionValue(next5, "next(...)");
                String str4 = (String) next5;
                Iterator it11 = list5.iterator();
                while (true) {
                    if (!it11.hasNext()) {
                        rawPropValue = null;
                        break;
                    }
                    Object obj6 = ((Instance) it11.next()).getProps().get(str4);
                    rawPropValue = obj6 instanceof RawPropValue ? (RawPropValue) obj6 : null;
                    if (rawPropValue != null) {
                        break;
                    }
                }
                if (rawPropValue != null) {
                    RawPropValue rawPropValue2 = rawPropValue;
                    if (rawPropValue.getCount() == list5.size()) {
                        Iterable iterableWithIndex = CollectionsKt.withIndex(list5);
                        linkedHashSet = linkedHashSet2;
                        if ((iterableWithIndex instanceof Collection) && ((Collection) iterableWithIndex).isEmpty()) {
                            identityHashMap = identityHashMap2;
                            z2 = true;
                        } else {
                            Iterator it12 = iterableWithIndex.iterator();
                            while (true) {
                                if (!it12.hasNext()) {
                                    identityHashMap = identityHashMap2;
                                    z2 = true;
                                    break;
                                }
                                IndexedValue indexedValue = (IndexedValue) it12.next();
                                Iterable iterable3 = iterableWithIndex;
                                int index = indexedValue.getIndex();
                                Iterator it13 = it12;
                                Object obj7 = ((Instance) indexedValue.component2()).getProps().get(str4);
                                identityHashMap = identityHashMap2;
                                RawPropValue rawPropValue3 = obj7 instanceof RawPropValue ? (RawPropValue) obj7 : null;
                                if (rawPropValue3 != null) {
                                    RawPropValue rawPropValue4 = rawPropValue3;
                                    if (rawPropValue3.getPayload() == rawPropValue2.getPayload() && rawPropValue4.getIndex() == index) {
                                        i = 1;
                                    }
                                    if (i == 0) {
                                        z2 = false;
                                        break;
                                    }
                                    iterableWithIndex = iterable3;
                                    it12 = it13;
                                    identityHashMap2 = identityHashMap;
                                }
                                i = 0;
                                if (i == 0) {
                                    z2 = false;
                                    break;
                                }
                                iterableWithIndex = iterable3;
                                it12 = it13;
                                identityHashMap2 = identityHashMap;
                            }
                        }
                        z = z2;
                        if (z) {
                            ByteWriter byteWriter2 = new ByteWriter();
                            byteWriter2.i32(i12);
                            byteWriter2.string(str4);
                            byteWriter2.u8(rawPropValue2.getType());
                            byteWriter2.bytes(rawPropValue2.getPayload());
                            write$chunk(file, "PROP", byteWriter2.toByteArray());
                        }
                        linkedHashSet2 = linkedHashSet;
                        identityHashMap2 = identityHashMap;
                    } else {
                        linkedHashSet = linkedHashSet2;
                        identityHashMap = identityHashMap2;
                    }
                    if (z) {
                        ByteWriter byteWriter3 = new ByteWriter();
                        byteWriter3.i32(i12);
                        byteWriter3.string(str4);
                        byteWriter3.u8(rawPropValue2.getType());
                        byteWriter3.bytes(rawPropValue2.getPayload());
                        write$chunk(file, "PROP", byteWriter3.toByteArray());
                    }
                    linkedHashSet2 = linkedHashSet;
                    identityHashMap2 = identityHashMap;
                } else {
                    LinkedHashSet linkedHashSet3 = linkedHashSet2;
                    IdentityHashMap<Instance, Integer> identityHashMap3 = identityHashMap2;
                    Iterator it14 = list5.iterator();
                    do {
                        if (!it14.hasNext()) {
                            num2 = null;
                            break;
                        }
                        num2 = ((Instance) it14.next()).getPropTypes().get(str4);
                    } while (num2 == null);
                    if (num2 != null) {
                        iIntValue = num2.intValue();
                    } else {
                        List list6 = list5;
                        Iterator it15 = list6.iterator();
                        while (true) {
                            if (!it15.hasNext()) {
                                next = null;
                                break;
                            }
                            next = it15.next();
                            Iterable iterable4 = list6;
                            if (((Instance) next).getProps().containsKey(str4)) {
                                break;
                            }
                            list6 = (List) iterable4;
                        }
                        Instance instance = (Instance) next;
                        Integer numValueOf = instance != null ? Integer.valueOf(PropType.INSTANCE.infer(instance.getProps().get(str4))) : null;
                        if (numValueOf != null) {
                            iIntValue = numValueOf.intValue();
                        } else {
                            i2 = 1;
                        }
                        list = list5;
                        arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(list, 10));
                        for (Instance instance2 : list) {
                            if (Intrinsics.areEqual(str4, str3)) {
                                iterable = list;
                                objM315default = instance2.getName();
                            } else {
                                iterable = list;
                                if (instance2.getProps().containsKey(str4)) {
                                    objM315default = instance2.getProps().get(str4);
                                } else {
                                    objM315default = PropType.INSTANCE.m315default(i2);
                                }
                            }
                            arrayList.add(objM315default);
                            list = (List) iterable;
                        }
                        List list7 = (List) arrayList;
                        ByteWriter byteWriter4 = new ByteWriter();
                        byteWriter4.i32(i12);
                        byteWriter4.string(str4);
                        byteWriter4.u8(i2);
                        identityHashMap2 = identityHashMap3;
                        INSTANCE.writeValues(byteWriter4, i2, list7, identityHashMap2, sstr);
                        write$chunk(file, "PROP", byteWriter4.toByteArray());
                        linkedHashSet2 = linkedHashSet3;
                    }
                    i2 = iIntValue;
                    list = list5;
                    arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(list, 10));
                    for (Object _instObj : list) {
                        Instance instance2 = (Instance) _instObj;
                        if (Intrinsics.areEqual(str4, str3)) {
                            iterable = list;
                            objM315default = instance2.getName();
                        } else {
                            iterable = list;
                            if (instance2.getProps().containsKey(str4)) {
                                objM315default = instance2.getProps().get(str4);
                            } else {
                                objM315default = PropType.INSTANCE.m315default(i2);
                            }
                        }
                        arrayList.add(objM315default);
                        list = (List) iterable;
                    }
                    List list8 = (List) arrayList;
                    ByteWriter byteWriter5 = new ByteWriter();
                    byteWriter5.i32(i12);
                    byteWriter5.string(str4);
                    byteWriter5.u8(i2);
                    identityHashMap2 = identityHashMap3;
                    INSTANCE.writeValues(byteWriter5, i2, list8, identityHashMap2, sstr);
                    write$chunk(file, "PROP", byteWriter5.toByteArray());
                    linkedHashSet2 = linkedHashSet3;
                }
            }
            i12 = i13;
        }
        ByteWriter byteWriter6 = new ByteWriter();
        byteWriter6.u8(0);
        byteWriter6.i32(all.size());
        int size3 = all.size();
        int[] iArr2 = new int[size3];
        for (int i14 = 0; i14 < size3; i14++) {
            Integer num5 = identityHashMap2.get(all.get(i14));
            Intrinsics.checkNotNull(num5);
            iArr2[i14] = num5.intValue();
        }
        byteWriter6.refArray(iArr2);
        int size4 = all.size();
        int[] iArr3 = new int[size4];
        for (int i15 = 0; i15 < size4; i15++) {
            Instance parent = ((Instance) all.get(i15)).getParent();
            iArr3[i15] = (parent == null || (num = identityHashMap2.get(parent)) == null) ? -1 : num.intValue();
        }
        byteWriter6.refArray(iArr3);
        write$chunk(file, "PRNT", byteWriter6.toByteArray());
        Unit unit = Unit.INSTANCE;
        byte[] bytes2 = "</roblox>".getBytes(Charsets.ISO_8859_1);
        Intrinsics.checkNotNullExpressionValue(bytes2, "getBytes(...)");
        write$chunk(file, "END", bytes2);
        return file.toByteArray();
    }

    private static final void write$collect(ArrayList<Instance> arrayList, Instance i) {
        if (i.getArchivable()) {
            arrayList.add(i);
            Iterator<Instance> it = i.getChildren().iterator();
            Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
            while (it.hasNext()) {
                Instance c = it.next();
                write$collect(arrayList, c);
            }
        }
    }

    private static final void write$chunk(ByteWriter file, String name, byte[] body) {
        byte[] bytes = name.getBytes(Charsets.ISO_8859_1);
        Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
        byte[] nb = Arrays.copyOf(bytes, 4);
        Intrinsics.checkNotNullExpressionValue(nb, "copyOf(...)");
        file.bytes(nb);
        file.i32(0);
        file.i32(body.length);
        file.i32(0);
        file.bytes(body);
    }

    private final float f(Object v) {
        Number number = v instanceof Number ? (Number) v : null;
        if (number != null) {
            return number.floatValue();
        }
        return 0.0f;
    }

    private final int i(Object v) {
        if (v instanceof Number) {
            return ((Number) v).intValue();
        }
        if (v instanceof EnumValue) {
            return ((EnumValue) v).getValue();
        }
        if (v instanceof BrickColorV) {
            return ((BrickColorV) v).getNumber();
        }
        return 0;
    }

    private final void writeCFrames(ByteWriter w, List<CFrame> cfs) {
        int i;
        Iterator<CFrame> it = cfs.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            CFrame c = it.next();
            w.u8(0);
            w.f32(c.getR00());
            w.f32(c.getR01());
            w.f32(c.getR02());
            w.f32(c.getR10());
            w.f32(c.getR11());
            w.f32(c.getR12());
            w.f32(c.getR20());
            w.f32(c.getR21());
            w.f32(c.getR22());
        }
        int size = cfs.size();
        float[] fArr = new float[size];
        for (int i2 = 0; i2 < size; i2++) {
            fArr[i2] = cfs.get(i2).getX();
        }
        w.f32Array(fArr);
        int size2 = cfs.size();
        float[] fArr2 = new float[size2];
        for (int i3 = 0; i3 < size2; i3++) {
            fArr2[i3] = cfs.get(i3).getY();
        }
        w.f32Array(fArr2);
        int size3 = cfs.size();
        float[] fArr3 = new float[size3];
        for (i = 0; i < size3; i++) {
            fArr3[i] = cfs.get(i).getZ();
        }
        w.f32Array(fArr3);
    }

    private final void writeValues(ByteWriter w, int type, List<? extends Object> vals, IdentityHashMap<Instance, Integer> ref, Map<String, Pair<Integer, byte[]>> sstr) {
        String string;
        int i;
        Integer num;
        Pair<Integer, byte[]> pair;
        byte[] bytes;
        List<? extends Object> list;
        String string2;
        int size = vals.size();
        boolean z = true;
        switch (type) {
            case 1:
                for (Object obj : vals) {
                    if (obj instanceof BinaryString) {
                        w.string(((BinaryString) obj).getBytes());
                    } else if (obj instanceof ContentValue) {
                        w.string(((ContentValue) obj).getUri());
                    } else {
                        if (obj == null || (string = obj.toString()) == null) {
                            string = "";
                        }
                        w.string(string);
                    }
                }
                Unit unit = Unit.INSTANCE;
                break;
            case 2:
                Iterator<? extends Object> it = vals.iterator();
                while (it.hasNext()) {
                    w.u8(Intrinsics.areEqual(it.next(), (Object) true) ? 1 : 0);
                }
                Unit unit2 = Unit.INSTANCE;
                break;
            case 3:
                int[] iArr = new int[size];
                for (int i2 = 0; i2 < size; i2++) {
                    iArr[i2] = INSTANCE.i(vals.get(i2));
                }
                w.i32Array(iArr);
                Unit unit3 = Unit.INSTANCE;
                break;
            case 4:
                float[] fArr = new float[size];
                for (int i3 = 0; i3 < size; i3++) {
                    fArr[i3] = INSTANCE.f(vals.get(i3));
                }
                w.f32Array(fArr);
                Unit unit4 = Unit.INSTANCE;
                break;
            case 5:
                for (Object obj2 : vals) {
                    Number number = obj2 instanceof Number ? (Number) obj2 : null;
                    w.f64(number != null ? number.doubleValue() : 0.0d);
                }
                Unit unit5 = Unit.INSTANCE;
                break;
            case 6:
                List<? extends Object> list2 = vals;
                ArrayList arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(list2, 10));
                for (Object obj3 : list2) {
                    UDim uDim = obj3 instanceof UDim ? (UDim) obj3 : null;
                    if (uDim == null) {
                        uDim = new UDim(0.0f, 0);
                    }
                    arrayList.add(uDim);
                }
                ArrayList arrayList2 = arrayList;
                float[] fArr2 = new float[size];
                for (int i4 = 0; i4 < size; i4++) {
                    fArr2[i4] = ((UDim) arrayList2.get(i4)).getScale();
                }
                w.f32Array(fArr2);
                int[] iArr2 = new int[size];
                for (int i5 = 0; i5 < size; i5++) {
                    iArr2[i5] = ((UDim) arrayList2.get(i5)).getOffset();
                }
                w.i32Array(iArr2);
                Unit unit6 = Unit.INSTANCE;
                break;
            case 7:
                List<? extends Object> list3 = vals;
                ArrayList arrayList3 = new ArrayList(CollectionsKt.collectionSizeOrDefault(list3, 10));
                for (Object obj4 : list3) {
                    UDim2 uDim2 = obj4 instanceof UDim2 ? (UDim2) obj4 : null;
                    if (uDim2 == null) {
                        uDim2 = new UDim2(0.0f, 0, 0.0f, 0);
                    }
                    arrayList3.add(uDim2);
                }
                ArrayList arrayList4 = arrayList3;
                float[] fArr3 = new float[size];
                for (int i6 = 0; i6 < size; i6++) {
                    fArr3[i6] = ((UDim2) arrayList4.get(i6)).getXs();
                }
                w.f32Array(fArr3);
                float[] fArr4 = new float[size];
                for (int i7 = 0; i7 < size; i7++) {
                    fArr4[i7] = ((UDim2) arrayList4.get(i7)).getYs();
                }
                w.f32Array(fArr4);
                int[] iArr3 = new int[size];
                for (int i8 = 0; i8 < size; i8++) {
                    iArr3[i8] = ((UDim2) arrayList4.get(i8)).getXo();
                }
                w.i32Array(iArr3);
                int[] iArr4 = new int[size];
                for (int i9 = 0; i9 < size; i9++) {
                    iArr4[i9] = ((UDim2) arrayList4.get(i9)).getYo();
                }
                w.i32Array(iArr4);
                Unit unit7 = Unit.INSTANCE;
                break;
            case 8:
                for (Object obj5 : vals) {
                    Ray ray = obj5 instanceof Ray ? (Ray) obj5 : null;
                    if (ray == null) {
                        ray = new Ray(Vector3.INSTANCE.getZERO(), Vector3.INSTANCE.getZERO());
                    }
                    w.f32(ray.getOrigin().getX());
                    w.f32(ray.getOrigin().getY());
                    w.f32(ray.getOrigin().getZ());
                    w.f32(ray.getDirection().getX());
                    w.f32(ray.getDirection().getY());
                    w.f32(ray.getDirection().getZ());
                }
                Unit unit8 = Unit.INSTANCE;
                break;
            case 9:
                for (Object obj6 : vals) {
                    Faces faces = obj6 instanceof Faces ? (Faces) obj6 : null;
                    w.u8(faces != null ? faces.getMask() : 0);
                }
                Unit unit9 = Unit.INSTANCE;
                break;
            case 10:
                for (Object obj7 : vals) {
                    Axes axes = obj7 instanceof Axes ? (Axes) obj7 : null;
                    w.u8(axes != null ? axes.getMask() : 0);
                }
                Unit unit10 = Unit.INSTANCE;
                break;
            case 11:
                int[] iArr5 = new int[size];
                for (int i10 = 0; i10 < size; i10++) {
                    Object obj8 = vals.get(i10);
                    BrickColorV brickColorV = obj8 instanceof BrickColorV ? (BrickColorV) obj8 : null;
                    if (brickColorV != null) {
                        i = brickColorV.getNumber();
                    } else {
                        i = INSTANCE.i(vals.get(i10));
                        if (i == 0) {
                            i = 194;
                        }
                    }
                    iArr5[i10] = i;
                }
                w.u32Array(iArr5);
                Unit unit11 = Unit.INSTANCE;
                break;
            case 12:
                List<? extends Object> list4 = vals;
                ArrayList arrayList5 = new ArrayList(CollectionsKt.collectionSizeOrDefault(list4, 10));
                for (Object obj9 : list4) {
                    Color3 color3 = obj9 instanceof Color3 ? (Color3) obj9 : null;
                    if (color3 == null) {
                        color3 = new Color3(0.0f, 0.0f, 0.0f);
                    }
                    arrayList5.add(color3);
                }
                ArrayList arrayList6 = arrayList5;
                float[] fArr5 = new float[size];
                for (int i11 = 0; i11 < size; i11++) {
                    fArr5[i11] = ((Color3) arrayList6.get(i11)).getR();
                }
                w.f32Array(fArr5);
                float[] fArr6 = new float[size];
                for (int i12 = 0; i12 < size; i12++) {
                    fArr6[i12] = ((Color3) arrayList6.get(i12)).getG();
                }
                w.f32Array(fArr6);
                float[] fArr7 = new float[size];
                for (int i13 = 0; i13 < size; i13++) {
                    fArr7[i13] = ((Color3) arrayList6.get(i13)).getB();
                }
                w.f32Array(fArr7);
                Unit unit12 = Unit.INSTANCE;
                break;
            case 13:
                List<? extends Object> list5 = vals;
                ArrayList arrayList7 = new ArrayList(CollectionsKt.collectionSizeOrDefault(list5, 10));
                for (Object obj10 : list5) {
                    Vector2 vector2 = obj10 instanceof Vector2 ? (Vector2) obj10 : null;
                    if (vector2 == null) {
                        vector2 = new Vector2(0.0f, 0.0f);
                    }
                    arrayList7.add(vector2);
                }
                ArrayList arrayList8 = arrayList7;
                float[] fArr8 = new float[size];
                for (int i14 = 0; i14 < size; i14++) {
                    fArr8[i14] = ((Vector2) arrayList8.get(i14)).getX();
                }
                w.f32Array(fArr8);
                float[] fArr9 = new float[size];
                for (int i15 = 0; i15 < size; i15++) {
                    fArr9[i15] = ((Vector2) arrayList8.get(i15)).getY();
                }
                w.f32Array(fArr9);
                Unit unit13 = Unit.INSTANCE;
                break;
            case 14:
                List<? extends Object> list6 = vals;
                ArrayList arrayList9 = new ArrayList(CollectionsKt.collectionSizeOrDefault(list6, 10));
                for (Object obj11 : list6) {
                    Vector3 zero = obj11 instanceof Vector3 ? (Vector3) obj11 : null;
                    if (zero == null) {
                        zero = Vector3.INSTANCE.getZERO();
                    }
                    arrayList9.add(zero);
                }
                ArrayList arrayList10 = arrayList9;
                float[] fArr10 = new float[size];
                for (int i16 = 0; i16 < size; i16++) {
                    fArr10[i16] = ((Vector3) arrayList10.get(i16)).getX();
                }
                w.f32Array(fArr10);
                float[] fArr11 = new float[size];
                for (int i17 = 0; i17 < size; i17++) {
                    fArr11[i17] = ((Vector3) arrayList10.get(i17)).getY();
                }
                w.f32Array(fArr11);
                float[] fArr12 = new float[size];
                for (int i18 = 0; i18 < size; i18++) {
                    fArr12[i18] = ((Vector3) arrayList10.get(i18)).getZ();
                }
                w.f32Array(fArr12);
                Unit unit14 = Unit.INSTANCE;
                break;
            case 15:
            case 17:
            case 29:
            default:
                for (Object obj12 : vals) {
                    if (obj12 == null || (string2 = obj12.toString()) == null) {
                        string2 = "";
                    }
                    w.string(string2);
                }
                Unit unit15 = Unit.INSTANCE;
                break;
            case 16:
                List<? extends Object> list7 = vals;
                ArrayList arrayList11 = new ArrayList(CollectionsKt.collectionSizeOrDefault(list7, 10));
                for (Object obj13 : list7) {
                    CFrame identity = obj13 instanceof CFrame ? (CFrame) obj13 : null;
                    if (identity == null) {
                        identity = CFrame.INSTANCE.getIDENTITY();
                    }
                    arrayList11.add(identity);
                }
                writeCFrames(w, arrayList11);
                Unit unit16 = Unit.INSTANCE;
                break;
            case 18:
                int[] iArr6 = new int[size];
                for (int i19 = 0; i19 < size; i19++) {
                    iArr6[i19] = INSTANCE.i(vals.get(i19));
                }
                w.u32Array(iArr6);
                Unit unit17 = Unit.INSTANCE;
                break;
            case 19:
                int[] iArr7 = new int[size];
                for (int i20 = 0; i20 < size; i20++) {
                    Object obj14 = vals.get(i20);
                    Instance instance = obj14 instanceof Instance ? (Instance) obj14 : null;
                    iArr7[i20] = (instance == null || (num = ref.get(instance)) == null) ? -1 : num.intValue();
                }
                w.refArray(iArr7);
                Unit unit18 = Unit.INSTANCE;
                break;
            case 20:
                for (Object obj15 : vals) {
                    Vector3int16 vector3int16 = obj15 instanceof Vector3int16 ? (Vector3int16) obj15 : null;
                    if (vector3int16 == null) {
                        vector3int16 = new Vector3int16(0, 0, 0);
                    }
                    w.u16(vector3int16.getX());
                    w.u16(vector3int16.getY());
                    w.u16(vector3int16.getZ());
                }
                Unit unit19 = Unit.INSTANCE;
                break;
            case 21:
                for (Object obj16 : vals) {
                    NumberSequence numberSequence = obj16 instanceof NumberSequence ? (NumberSequence) obj16 : null;
                    if (numberSequence == null) {
                        Object objM315default = PropType.INSTANCE.m315default(type);
                        Intrinsics.checkNotNull(objM315default, "null cannot be cast to non-null type com.goydevv.studrblx.model.NumberSequence");
                        numberSequence = (NumberSequence) objM315default;
                    }
                    w.i32(numberSequence.getKeys().size());
                    for (NumberKey numberKey : numberSequence.getKeys()) {
                        w.f32(numberKey.getTime());
                        w.f32(numberKey.getValue());
                        w.f32(numberKey.getEnvelope());
                    }
                }
                Unit unit20 = Unit.INSTANCE;
                break;
            case 22:
                for (Object obj17 : vals) {
                    ColorSequence colorSequence = obj17 instanceof ColorSequence ? (ColorSequence) obj17 : null;
                    if (colorSequence == null) {
                        Object objM315default2 = PropType.INSTANCE.m315default(type);
                        Intrinsics.checkNotNull(objM315default2, "null cannot be cast to non-null type com.goydevv.studrblx.model.ColorSequence");
                        colorSequence = (ColorSequence) objM315default2;
                    }
                    w.i32(colorSequence.getKeys().size());
                    for (ColorKey colorKey : colorSequence.getKeys()) {
                        w.f32(colorKey.getTime());
                        w.f32(colorKey.getColor().getR());
                        w.f32(colorKey.getColor().getG());
                        w.f32(colorKey.getColor().getB());
                        w.f32(colorKey.getEnvelope());
                    }
                }
                Unit unit21 = Unit.INSTANCE;
                break;
            case 23:
                for (Object obj18 : vals) {
                    NumberRange numberRange = obj18 instanceof NumberRange ? (NumberRange) obj18 : null;
                    if (numberRange == null) {
                        numberRange = new NumberRange(0.0f, 0.0f);
                    }
                    w.f32(numberRange.getMin());
                    w.f32(numberRange.getMax());
                }
                Unit unit22 = Unit.INSTANCE;
                break;
            case 24:
                List<? extends Object> list8 = vals;
                int i21 = 0;
                ArrayList arrayList12 = new ArrayList(CollectionsKt.collectionSizeOrDefault(list8, 10));
                for (Object obj19 : list8) {
                    List<? extends Object> list9 = list8;
                    Rect rect = obj19 instanceof Rect ? (Rect) obj19 : null;
                    if (rect == null) {
                        rect = new Rect(0.0f, 0.0f, 0.0f, 0.0f);
                    }
                    arrayList12.add(rect);
                    list8 = list9;
                }
                ArrayList arrayList13 = arrayList12;
                float[] fArr13 = new float[size];
                for (int i22 = 0; i22 < size; i22++) {
                    fArr13[i22] = ((Rect) arrayList13.get(i22)).getMinX();
                }
                w.f32Array(fArr13);
                float[] fArr14 = new float[size];
                for (int i23 = 0; i23 < size; i23++) {
                    fArr14[i23] = ((Rect) arrayList13.get(i23)).getMinY();
                }
                w.f32Array(fArr14);
                float[] fArr15 = new float[size];
                for (int i24 = 0; i24 < size; i24++) {
                    fArr15[i24] = ((Rect) arrayList13.get(i24)).getMaxX();
                }
                w.f32Array(fArr15);
                float[] fArr16 = new float[size];
                for (int i25 = 0; i25 < size; i25++) {
                    fArr16[i25] = ((Rect) arrayList13.get(i25)).getMaxY();
                }
                w.f32Array(fArr16);
                Unit unit23 = Unit.INSTANCE;
                break;
            case 25:
                for (Object obj20 : vals) {
                    PhysicalProps physicalProps = obj20 instanceof PhysicalProps ? (PhysicalProps) obj20 : null;
                    if (physicalProps == null) {
                        physicalProps = new PhysicalProps(0, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 126, null);
                    }
                    w.u8(physicalProps.getFlags());
                    if ((physicalProps.getFlags() & 1) != 0) {
                        w.f32(physicalProps.getDensity());
                        w.f32(physicalProps.getFriction());
                        w.f32(physicalProps.getElasticity());
                        w.f32(physicalProps.getFrictionWeight());
                        w.f32(physicalProps.getElasticityWeight());
                        if ((physicalProps.getFlags() & 2) != 0) {
                            w.f32(physicalProps.getAcoustic());
                        }
                    }
                }
                Unit unit24 = Unit.INSTANCE;
                break;
            case 26:
                List<? extends Object> list10 = vals;
                ArrayList arrayList14 = new ArrayList(CollectionsKt.collectionSizeOrDefault(list10, 10));
                for (Object obj21 : list10) {
                    Color3 color4 = obj21 instanceof Color3 ? (Color3) obj21 : null;
                    if (color4 == null) {
                        color4 = new Color3(0.0f, 0.0f, 0.0f);
                    }
                    arrayList14.add(color4);
                }
                ArrayList arrayList15 = arrayList14;
                byte[] bArr = new byte[size];
                for (int i26 = 0; i26 < size; i26++) {
                    bArr[i26] = writeValues$b(((Color3) arrayList15.get(i26)).getR());
                }
                w.bytes(bArr);
                byte[] bArr2 = new byte[size];
                for (int i27 = 0; i27 < size; i27++) {
                    bArr2[i27] = writeValues$b(((Color3) arrayList15.get(i27)).getG());
                }
                w.bytes(bArr2);
                byte[] bArr3 = new byte[size];
                for (int i28 = 0; i28 < size; i28++) {
                    bArr3[i28] = writeValues$b(((Color3) arrayList15.get(i28)).getB());
                }
                w.bytes(bArr3);
                Unit unit25 = Unit.INSTANCE;
                break;
            case 27:
                long[] jArr = new long[size];
                for (int i29 = 0; i29 < size; i29++) {
                    Object obj22 = vals.get(i29);
                    Number number2 = obj22 instanceof Number ? (Number) obj22 : null;
                    jArr[i29] = number2 != null ? number2.longValue() : 0L;
                }
                w.i64Array(jArr);
                Unit unit26 = Unit.INSTANCE;
                break;
            case 28:
                int[] iArr8 = new int[size];
                for (int i30 = 0; i30 < size; i30++) {
                    Object obj23 = vals.get(i30);
                    SharedStr sharedStr = obj23 instanceof SharedStr ? (SharedStr) obj23 : null;
                    iArr8[i30] = (sharedStr == null || (pair = sstr.get(Base64.getEncoder().encodeToString(sharedStr.getBytes()))) == null) ? 0 : pair.getFirst().intValue();
                }
                w.u32Array(iArr8);
                Unit unit27 = Unit.INSTANCE;
                break;
            case 30:
                List<? extends Object> list11 = vals;
                ArrayList arrayList16 = new ArrayList(CollectionsKt.collectionSizeOrDefault(list11, 10));
                for (Object obj24 : list11) {
                    OptionalCFrame optionalCFrame = obj24 instanceof OptionalCFrame ? (OptionalCFrame) obj24 : null;
                    if (optionalCFrame == null) {
                        optionalCFrame = new OptionalCFrame(null);
                    }
                    arrayList16.add(optionalCFrame);
                }
                ArrayList arrayList17 = arrayList16;
                w.u8(16);
                ArrayList arrayList18 = arrayList17;
                ArrayList arrayList19 = new ArrayList(CollectionsKt.collectionSizeOrDefault(arrayList18, 10));
                Iterator it2 = arrayList18.iterator();
                while (it2.hasNext()) {
                    CFrame cframe = ((OptionalCFrame) it2.next()).getCframe();
                    if (cframe == null) {
                        cframe = CFrame.INSTANCE.getIDENTITY();
                    }
                    arrayList19.add(cframe);
                }
                writeCFrames(w, arrayList19);
                w.u8(2);
                Iterator it3 = arrayList17.iterator();
                while (it3.hasNext()) {
                    w.u8(((OptionalCFrame) it3.next()).getCframe() != null ? 1 : 0);
                }
                Unit unit28 = Unit.INSTANCE;
                break;
            case 31:
                byte[] bArr4 = new byte[size * 16];
                List<? extends Object> list12 = vals;
                int i31 = 0;
                int i32 = 0;
                for (Object obj25 : list12) {
                    int i33 = i32 + 1;
                    if (i32 < 0) {
                        CollectionsKt.throwIndexOverflow();
                    }
                    UniqueId uniqueId = obj25 instanceof UniqueId ? (UniqueId) obj25 : null;
                    if (uniqueId != null && (bytes = uniqueId.getBytes()) != null) {
                        ArraysKt.copyInto(bytes, bArr4, i32 * 16, 0, 16);
                    }
                    i32 = i33;
                }
                w.interleaved(bArr4, size, 16);
                Unit unit29 = Unit.INSTANCE;
                break;
            case 32:
                for (Object obj26 : vals) {
                    FontValue fontValue = obj26 instanceof FontValue ? (FontValue) obj26 : null;
                    if (fontValue == null) {
                        Object objM315default3 = PropType.INSTANCE.m315default(type);
                        Intrinsics.checkNotNull(objM315default3, "null cannot be cast to non-null type com.goydevv.studrblx.model.FontValue");
                        fontValue = (FontValue) objM315default3;
                    }
                    w.string(fontValue.getFamily());
                    w.u16(fontValue.getWeight());
                    w.u8(fontValue.getStyle());
                    w.string(fontValue.getCachedFaceId());
                }
                Unit unit30 = Unit.INSTANCE;
                break;
            case 33:
                byte[] bArr5 = new byte[size * 8];
                List<? extends Object> list13 = vals;
                int i34 = 0;
                int i35 = 0;
                for (Object obj27 : list13) {
                    int i36 = i35 + 1;
                    if (i35 < 0) {
                        CollectionsKt.throwIndexOverflow();
                    }
                    SecurityCaps securityCaps = obj27 instanceof SecurityCaps ? (SecurityCaps) obj27 : null;
                    long value = securityCaps != null ? securityCaps.getValue() : 0L;
                    int i37 = 0;
                    while (true) {
                        list = list13;
                        if (i37 < 8) {
                            bArr5[(i35 * 8) + i37] = (byte) (value >>> (56 - (i37 * 8)));
                            i37++;
                            list13 = list;
                        } else {
                            break;
                        }
                    }
                    i35 = i36;
                    list13 = list;
                }
                w.interleaved(bArr5, size, 8);
                Unit unit31 = Unit.INSTANCE;
                break;
            case 34:
                List<? extends Object> list14 = vals;
                ArrayList arrayList20 = new ArrayList(CollectionsKt.collectionSizeOrDefault(list14, 10));
                for (Object obj28 : list14) {
                    ContentValue contentValue = obj28 instanceof ContentValue ? (ContentValue) obj28 : null;
                    if (contentValue == null) {
                        String str = obj28 instanceof String ? (String) obj28 : null;
                        if (str != null) {
                            String str2 = str;
                            contentValue = str2.length() == 0 ? new ContentValue(0, null, null, 6, null) : new ContentValue(1, str2, null, 4, null);
                        } else {
                            contentValue = null;
                        }
                        if (contentValue == null) {
                            contentValue = new ContentValue(0, null, null, 6, null);
                        }
                    }
                    arrayList20.add(contentValue);
                }
                ArrayList arrayList21 = arrayList20;
                int[] iArr9 = new int[size];
                for (int i38 = 0; i38 < size; i38++) {
                    iArr9[i38] = (((ContentValue) arrayList21.get(i38)).getSourceType() == 2 && ((ContentValue) arrayList21.get(i38)).getObj() == null) ? 0 : ((ContentValue) arrayList21.get(i38)).getSourceType();
                }
                w.i32Array(iArr9);
                ArrayList arrayList22 = new ArrayList();
                for (Object obj29 : arrayList21) {
                    if (((ContentValue) obj29).getSourceType() == 1) {
                        arrayList22.add(obj29);
                    }
                }
                ArrayList arrayList23 = arrayList22;
                w.i32(arrayList23.size());
                Iterator it4 = arrayList23.iterator();
                while (it4.hasNext()) {
                    w.string(((ContentValue) it4.next()).getUri());
                }
                ArrayList arrayList24 = new ArrayList();
                for (Object obj30 : arrayList21) {
                    ContentValue contentValue2 = (ContentValue) obj30;
                    boolean z2 = z;
                    ArrayList arrayList25 = arrayList23;
                    if ((contentValue2.getSourceType() != 2 || contentValue2.getObj() == null) ? false : z2) {
                        arrayList24.add(obj30);
                    }
                    arrayList23 = arrayList25;
                    z = z2;
                }
                ArrayList arrayList26 = arrayList24;
                w.i32(arrayList26.size());
                int size2 = arrayList26.size();
                int[] iArr10 = new int[size2];
                for (int i39 = 0; i39 < size2; i39++) {
                    Instance obj31 = ((ContentValue) arrayList26.get(i39)).getObj();
                    Intrinsics.checkNotNull(obj31);
                    Integer num2 = ref.get(obj31);
                    iArr10[i39] = num2 != null ? num2.intValue() : -1;
                }
                w.refArray(iArr10);
                w.i32(0);
                Unit unit32 = Unit.INSTANCE;
                break;
        }
    }

    private static final byte writeValues$b(float x) {
        return (byte) ((RangesKt.coerceIn(x, 0.0f, 1.0f) * 255.0f) + 0.5f);
    }
}

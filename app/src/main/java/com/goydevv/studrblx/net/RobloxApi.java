package com.goydevv.studrblx.net;

import android.content.Context;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.IntIterator;
import kotlin.collections.MapsKt;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;
import kotlin.io.ByteStreamsKt;
import kotlin.io.CloseableKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.Charsets;
import kotlin.text.MatchResult;
import kotlin.text.Regex;
import kotlin.text.RegexOption;
import kotlin.text.StringsKt;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: compiled from: RobloxApi.kt */
/* JADX INFO: loaded from: classes6.dex */
public final class RobloxApi {
    public static final RobloxApi INSTANCE = new RobloxApi();
    private static final String PREFS = "roblox_auth";

    /* JADX INFO: compiled from: RobloxApi.kt */
    public static final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[Category.values().length];
            try {
                iArr[Category.MODELS.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[Category.DECALS.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[Category.AUDIO.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[Category.MESHES.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[Category.PLUGINS.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    /* JADX INFO: compiled from: RobloxApi.kt */
    public static final class Item {
        private final int animations;
        private final String category;
        private final String created;
        private final String creator;
        private final long creatorId;
        private final boolean creatorIsGroup;
        private final String description;
        private final int downVotes;
        private final boolean endorsed;
        private final boolean hasScripts;
        private final long id;
        private final int meshParts;
        private final String name;
        private final long priceCents;
        private final int scriptCount;
        private final int sounds;
        private final int triangles;
        private final int upVotes;
        private final String updated;
        private final boolean verified;

        public Item(long id, String name, String creator, boolean verified, String description, int upVotes, int downVotes, boolean hasScripts, boolean endorsed, long priceCents, int scriptCount, int meshParts, int triangles, int animations, int sounds, String created, String updated, long creatorId, boolean creatorIsGroup, String category) {
            Intrinsics.checkNotNullParameter(name, "name");
            Intrinsics.checkNotNullParameter(creator, "creator");
            Intrinsics.checkNotNullParameter(description, "description");
            Intrinsics.checkNotNullParameter(created, "created");
            Intrinsics.checkNotNullParameter(updated, "updated");
            Intrinsics.checkNotNullParameter(category, "category");
            this.id = id;
            this.name = name;
            this.creator = creator;
            this.verified = verified;
            this.description = description;
            this.upVotes = upVotes;
            this.downVotes = downVotes;
            this.hasScripts = hasScripts;
            this.endorsed = endorsed;
            this.priceCents = priceCents;
            this.scriptCount = scriptCount;
            this.meshParts = meshParts;
            this.triangles = triangles;
            this.animations = animations;
            this.sounds = sounds;
            this.created = created;
            this.updated = updated;
            this.creatorId = creatorId;
            this.creatorIsGroup = creatorIsGroup;
            this.category = category;
        }

        public /* synthetic */ Item(long j, String str, String str2, boolean z, String str3, int i, int i2, boolean z2, boolean z3, long j2, int i3, int i4, int i5, int i6, int i7, String str4, String str5, long j3, boolean z4, String str6, int i8, DefaultConstructorMarker defaultConstructorMarker) {
            this(j, str, str2, z, str3, i, i2, z2, z3, (i8 & 512) != 0 ? 0L : j2, (i8 & 1024) != 0 ? 0 : i3, (i8 & 2048) != 0 ? 0 : i4, (i8 & 4096) != 0 ? 0 : i5, (i8 & 8192) != 0 ? 0 : i6, (i8 & 16384) != 0 ? 0 : i7, (32768 & i8) != 0 ? "" : str4, (65536 & i8) != 0 ? "" : str5, (131072 & i8) != 0 ? 0L : j3, (262144 & i8) != 0 ? false : z4, (i8 & 524288) != 0 ? "" : str6);
        }

        public final String getCreator() {
            return this.creator;
        }

        public final String getDescription() {
            return this.description;
        }

        public final long getId() {
            return this.id;
        }

        public final String getName() {
            return this.name;
        }

        public final boolean getVerified() {
            return this.verified;
        }

        public final int getDownVotes() {
            return this.downVotes;
        }

        public final boolean getEndorsed() {
            return this.endorsed;
        }

        public final boolean getHasScripts() {
            return this.hasScripts;
        }

        public final int getUpVotes() {
            return this.upVotes;
        }

        public final int getMeshParts() {
            return this.meshParts;
        }

        public final long getPriceCents() {
            return this.priceCents;
        }

        public final int getScriptCount() {
            return this.scriptCount;
        }

        public final int getTriangles() {
            return this.triangles;
        }

        public final int getAnimations() {
            return this.animations;
        }

        public final String getCreated() {
            return this.created;
        }

        public final int getSounds() {
            return this.sounds;
        }

        public final String getUpdated() {
            return this.updated;
        }

        public final String getCategory() {
            return this.category;
        }

        public final long getCreatorId() {
            return this.creatorId;
        }

        public final boolean getCreatorIsGroup() {
            return this.creatorIsGroup;
        }

        public final boolean isPaid() {
            return this.priceCents > 0;
        }

        public final String getPriceText() {
            if (this.priceCents <= 0) {
                return "Free";
            }
            String str = String.format("%d.%02d", Arrays.copyOf(new Object[]{Long.valueOf(this.priceCents / 100), Long.valueOf(this.priceCents % 100)}, 2));
            Intrinsics.checkNotNullExpressionValue(str, "format(...)");
            return "$" + str;
        }

        public final int getLikePercent() {
            if (this.upVotes + this.downVotes > 0) {
                return (this.upVotes * 100) / (this.upVotes + this.downVotes);
            }
            return -1;
        }
    }

    private RobloxApi() {
    }

    public final String storeUrl(long id) {
        return "https://create.roblox.com/store/asset/" + id;
    }

    /* JADX INFO: compiled from: RobloxApi.kt */
    public static final class Page {
        private final List<Long> ids;
        private final String nextCursor;
        private final int total;

        public Page(List<Long> ids, String nextCursor, int total) {
            Intrinsics.checkNotNullParameter(ids, "ids");
            this.ids = ids;
            this.nextCursor = nextCursor;
            this.total = total;
        }

        public final List<Long> getIds() {
            return this.ids;
        }

        public final String getNextCursor() {
            return this.nextCursor;
        }

        public final int getTotal() {
            return this.total;
        }
    }

    /* JADX INFO: compiled from: RobloxApi.kt */
    public enum Price {
        ANY("Any price"),
        FREE("Free"),
        PAID("Paid");

        private final String label;
        private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries(values());

        public static EnumEntries<Price> getEntries() {
            return $ENTRIES;
        }

        Price(String label) {
            this.label = label;
        }

        public final String getLabel() {
            return this.label;
        }
    }

    private final String v2Category(Category c) {
        switch (WhenMappings.$EnumSwitchMapping$0[c.ordinal()]) {
            case 1:
                return "Model";
            case 2:
                return "Decal";
            case 3:
                return "Audio";
            case 4:
                return "MeshPart";
            case 5:
                return "Plugin";
            default:
                throw new NoWhenBranchMatchedException();
        }
    }

    public static /* synthetic */ Page searchPriced$default(RobloxApi robloxApi, String str, Category category, Price price, String str2, int i, int i2, Object obj) {
        if ((i2 & 16) != 0) {
            i = 30;
        }
        return robloxApi.searchPriced(str, category, price, str2, i);
    }

    /* JADX WARN: Code duplicated, block: B:31:0x0106  */
    public final Page searchPriced(String keyword, Category category, Price price, String cursor, int num) {
        Long lValueOf;
        JSONObject jSONObjectOptJSONObject;
        Intrinsics.checkNotNullParameter(keyword, "keyword");
        Intrinsics.checkNotNullParameter(category, "category");
        Intrinsics.checkNotNullParameter(price, "price");
        if (price == Price.ANY) {
            return search(keyword, category, cursor, num);
        }
        StringBuilder sb = new StringBuilder("https://apis.roblox.com/toolbox-service/v2/assets:search?searchCategoryType=" + v2Category(category) + "&maxPageSize=" + num);
        if (!StringsKt.isBlank(keyword)) {
            sb.append("&query=").append(enc(StringsKt.trim((CharSequence) keyword).toString()));
        }
        sb.append(price == Price.FREE ? "&maxPriceCents=0" : "&minPriceCents=1");
        if (cursor != null) {
            sb.append("&pageToken=").append(enc(cursor));
        }
        Http http = Http.INSTANCE;
        String string = sb.toString();
        Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        JSONObject j = Http.json$default(http, string, null, 2, null);
        JSONArray arr = j.optJSONArray("creatorStoreAssets");
        if (arr == null) {
            arr = new JSONArray();
        }
        Iterable iterableUntil = RangesKt.until(0, arr.length());
        Collection arrayList = new ArrayList();
        Iterator<Integer> it = iterableUntil.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            JSONObject jSONObjectOptJSONObject2 = arr.optJSONObject(((IntIterator) it).nextInt());
            if (jSONObjectOptJSONObject2 == null || (jSONObjectOptJSONObject = jSONObjectOptJSONObject2.optJSONObject("asset")) == null) {
                lValueOf = null;
            } else {
                lValueOf = Long.valueOf(jSONObjectOptJSONObject.optLong("id"));
                if (!(lValueOf.longValue() != 0)) {
                    lValueOf = null;
                }
            }
            if (lValueOf != null) {
                arrayList.add(lValueOf);
            }
        }
        List ids = (List) arrayList;
        String strOptString = j.optString("nextPageToken");
        Intrinsics.checkNotNull(strOptString);
        return new Page(ids, ((!(strOptString.length() > 0) || Intrinsics.areEqual(strOptString, "null")) ? 0 : 1) != 0 ? strOptString : null, j.optInt("totalResults"));
    }

    private final long cents(JSONObject q) {
        if (q == null) {
            return 0L;
        }
        long s = q.optLong("significand");
        int e = q.optInt("exponent");
        if (s == 0) {
            return 0L;
        }
        double v = s * Math.pow(10.0d, e + 2);
        return Math.round(v);
    }

    public final List<Long> previewImages(long id) {
        JSONArray arr;
        JSONObject jSONObjectOptJSONObject;
        try {
            try {
                JSONObject j = Http.json$default(Http.INSTANCE, "https://apis.roblox.com/toolbox-service/v2/assets/" + id, null, 2, null);
                JSONObject jSONObjectOptJSONObject2 = j.optJSONObject("asset");
                if (jSONObjectOptJSONObject2 == null || (jSONObjectOptJSONObject = jSONObjectOptJSONObject2.optJSONObject("previewAssets")) == null || (arr = jSONObjectOptJSONObject.optJSONArray("imagePreviewAssets")) == null) {
                    arr = new JSONArray();
                }
                Iterable iterableUntil = RangesKt.until(0, arr.length());
                Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterableUntil, 10));
                Iterator<Integer> it = iterableUntil.iterator();
                while (it.hasNext()) {
                    arrayList.add(Long.valueOf(arr.optLong(((IntIterator) it).nextInt())));
                }
                Collection arrayList2 = new ArrayList();
                for (Object obj : (List) arrayList) {
                    if (((Number) obj).longValue() != 0) {
                        arrayList2.add(obj);
                    }
                }
                return (List) arrayList2;
            } catch (Exception e) {
                return CollectionsKt.emptyList();
            }
        } catch (Exception e2) {
            return CollectionsKt.emptyList();
        }
    }

    /* JADX INFO: compiled from: RobloxApi.kt */
    public enum Category {
        MODELS(10, "Models"),
        DECALS(13, "Decals"),
        AUDIO(3, "Audio"),
        MESHES(40, "Meshes"),
        PLUGINS(38, "Plugins");

        private final int id;
        private final String label;
        private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries(values());

        public static EnumEntries<Category> getEntries() {
            return $ENTRIES;
        }

        Category(int id, String label) {
            this.id = id;
            this.label = label;
        }

        public final int getId() {
            return this.id;
        }

        public final String getLabel() {
            return this.label;
        }
    }

    private final String enc(String s) {
        try { return URLEncoder.encode(s, "UTF-8"); } catch (java.io.UnsupportedEncodingException _e) { throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_e); }
    }

    public static /* synthetic */ Page search$default(RobloxApi robloxApi, String str, Category category, String str2, int i, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            category = Category.MODELS;
        }
        if ((i2 & 4) != 0) {
            str2 = null;
        }
        if ((i2 & 8) != 0) {
            i = 30;
        }
        return robloxApi.search(str, category, str2, i);
    }

    public final Page search(String keyword, Category category, String cursor, int num) {
        try {
        Intrinsics.checkNotNullParameter(keyword, "keyword");
        Intrinsics.checkNotNullParameter(category, "category");
        StringBuilder sb = new StringBuilder("https://apis.roblox.com/toolbox-service/v1/marketplace/" + category.getId() + "?num=" + num + "&includeOnlyVerifiedCreators=false");
        if (!StringsKt.isBlank(keyword)) {
            sb.append("&keyword=").append(enc(StringsKt.trim((CharSequence) keyword).toString()));
        }
        if (cursor != null) {
            sb.append("&cursor=").append(enc(cursor));
        }
        Http http = Http.INSTANCE;
        String string = sb.toString();
        Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        JSONObject j = Http.json$default(http, string, null, 2, null);
        JSONArray data = j.optJSONArray("data");
        if (data == null) {
            data = new JSONArray();
        }
        Iterable iterableUntil = RangesKt.until(0, data.length());
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterableUntil, 10));
        Iterator<Integer> it = iterableUntil.iterator();
        while (it.hasNext()) {
            arrayList.add(Long.valueOf(data.getJSONObject(((IntIterator) it).nextInt()).getLong("id")));
        }
        List ids = (List) arrayList;
        String strOptString = j.optString("nextPageCursor");
        Intrinsics.checkNotNull(strOptString);
        return new Page(ids, ((!(strOptString.length() > 0) || Intrinsics.areEqual(strOptString, "null")) ? 0 : 1) != 0 ? strOptString : null, j.optInt("totalResults"));
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    /* JADX WARN: Code duplicated, block: B:51:0x010e  */
    /* JADX WARN: Code duplicated, block: B:52:0x0117  */
    /* JADX WARN: Code duplicated, block: B:54:0x011b  */
    /* JADX WARN: Code duplicated, block: B:55:0x0124  */
    /* JADX WARN: Code duplicated, block: B:58:0x0132 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:59:0x0134  */
    /* JADX WARN: Code duplicated, block: B:60:0x0139  */
    /* JADX WARN: Code duplicated, block: B:62:0x013d  */
    /* JADX WARN: Code duplicated, block: B:63:0x0140  */
    /* JADX WARN: Code duplicated, block: B:66:0x014d  */
    /* JADX WARN: Code duplicated, block: B:67:0x0152  */
    /* JADX WARN: Code duplicated, block: B:70:0x015c  */
    /* JADX WARN: Code duplicated, block: B:71:0x0165  */
    /* JADX WARN: Code duplicated, block: B:76:0x017a  */
    /* JADX WARN: Code duplicated, block: B:78:0x017e  */
    /* JADX WARN: Code duplicated, block: B:79:0x0187  */
    /* JADX WARN: Code duplicated, block: B:81:0x018b  */
    /* JADX WARN: Code duplicated, block: B:82:0x0194  */
    /* JADX WARN: Code duplicated, block: B:85:0x01aa  */
    /* JADX WARN: Code duplicated, block: B:88:0x01b2  */
    /* JADX WARN: Code duplicated, block: B:90:0x01bd  */
    /* JADX WARN: Code duplicated, block: B:91:0x01c0  */
    public final List<Item> details(List<Long> ids) {
        try {
        long jCents;
        JSONObject jSONObjectOptJSONObject;
        String strOptString;
        String str;
        boolean z = false;
        int iOptInt;
        int iOptInt2;
        boolean z2;
        int iOptInt3;
        int iOptInt4;
        int iOptInt5;
        int iOptInt6;
        int iOptInt7;
        String strOptString2;
        String str2;
        char c;
        boolean z3 = false;
        JSONObject jSONObjectOptJSONObject2;
        int iOptInt8;
        Intrinsics.checkNotNullParameter(ids, "ids");
        if (ids.isEmpty()) {
            return CollectionsKt.emptyList();
        }
        JSONObject jSONObject = null;
        char c2 = 2;
        JSONObject j = Http.json$default(Http.INSTANCE, "https://apis.roblox.com/toolbox-service/v1/items/details?assetIds=" + CollectionsKt.joinToString(ids, ",", "", "", -1, "...", null), null, 2, null);
        JSONArray data = j.optJSONArray("data");
        if (data == null) {
            data = new JSONArray();
        }
        HashMap byId = new HashMap();
        int i = 0;
        int length = data.length();
        while (i < length) {
            JSONObject o = data.getJSONObject(i);
            JSONObject a = o.optJSONObject("asset");
            if (a == null) {
                c = c2;
            } else {
                JSONObject cr = o.optJSONObject("creator");
                JSONObject v = o.optJSONObject("voting");
                JSONObject tech = a.optJSONObject("modelTechnicalDetails");
                JSONObject counts = tech != null ? tech.optJSONObject("instanceCounts") : jSONObject;
                JSONObject prod = o.optJSONObject("fiatProduct");
                if (prod == null) {
                    prod = o.optJSONObject("creatorStoreProduct");
                }
                if (prod != null && prod.optBoolean("isFree")) {
                    jCents = 0;
                } else {
                    jCents = cents((prod == null || (jSONObjectOptJSONObject = prod.optJSONObject("purchasePrice")) == null) ? null : jSONObjectOptJSONObject.optJSONObject("quantity"));
                }
                long price = jCents;
                long j2 = a.getLong("id");
                String strOptString3 = a.optString("name");
                Intrinsics.checkNotNullExpressionValue(strOptString3, "optString(...)");
                if (cr == null || (strOptString = cr.optString("name")) == null) {
                    strOptString = "";
                }
                String str3 = strOptString;
                if (cr != null) {
                    str = strOptString3;
                    if (cr.optBoolean("isVerifiedCreator")) {
                        z = true;
                    }
                    String strOptString4 = a.optString("description");
                    Intrinsics.checkNotNullExpressionValue(strOptString4, "optString(...)");
                    if (v != null) {
                        iOptInt = v.optInt("upVotes");
                    } else {
                        iOptInt = 0;
                    }
                    if (v != null) {
                        iOptInt2 = v.optInt("downVotes");
                    } else {
                        iOptInt2 = 0;
                    }
                    if (a.optBoolean("hasScripts")) {
                        z2 = true;
                    } else {
                        if (counts != null) {
                            iOptInt8 = counts.optInt("script");
                        } else {
                            iOptInt8 = 0;
                        }
                        if (iOptInt8 <= 0) {
                            z2 = false;
                        } else {
                            z2 = true;
                        }
                    }
                    boolean zOptBoolean = a.optBoolean("isEndorsed");
                    if (counts != null) {
                        iOptInt3 = counts.optInt("script");
                    } else {
                        iOptInt3 = 0;
                    }
                    int iOptInt9 = a.optInt("scriptCount", iOptInt3);
                    if (counts != null) {
                        iOptInt4 = counts.optInt("meshPart");
                    } else {
                        iOptInt4 = 0;
                    }
                    if (tech == null || (jSONObjectOptJSONObject2 = tech.optJSONObject("objectMeshSummary")) == null) {
                        iOptInt5 = 0;
                    } else {
                        iOptInt5 = jSONObjectOptJSONObject2.optInt("triangles");
                    }
                    if (counts != null) {
                        iOptInt6 = counts.optInt("animation");
                    } else {
                        iOptInt6 = 0;
                    }
                    if (counts != null) {
                        iOptInt7 = counts.optInt("audio");
                    } else {
                        iOptInt7 = 0;
                    }
                    strOptString2 = a.optString("createdUtc");
                    Intrinsics.checkNotNullExpressionValue(strOptString2, "optString(...)");
                    String strOptString5 = a.optString("updatedUtc");
                    Intrinsics.checkNotNullExpressionValue(strOptString5, "optString(...)");
                    long jOptLong = cr != null ? cr.optLong("id") : 0L;
                    if (cr != null) {
                        str2 = strOptString2;
                        c = 2;
                        if (cr.optInt("type") == 2) {
                            z3 = true;
                        }
                        String strOptString6 = a.optString("categoryPath");
                        Intrinsics.checkNotNullExpressionValue(strOptString6, "optString(...)");
                        Item item = new Item(j2, str, str3, z, strOptString4, iOptInt, iOptInt2, z2, zOptBoolean, price, iOptInt9, iOptInt4, iOptInt5, iOptInt6, iOptInt7, str2, strOptString5, jOptLong, z3, strOptString6);
                        byId.put(Long.valueOf(item.getId()), item);
                    } else {
                        str2 = strOptString2;
                        c = 2;
                    }
                    z3 = false;
                    String strOptString7 = a.optString("categoryPath");
                    Intrinsics.checkNotNullExpressionValue(strOptString7, "optString(...)");
                    Item item2 = new Item(j2, str, str3, z, strOptString4, iOptInt, iOptInt2, z2, zOptBoolean, price, iOptInt9, iOptInt4, iOptInt5, iOptInt6, iOptInt7, str2, strOptString5, jOptLong, z3, strOptString7);
                    byId.put(Long.valueOf(item2.getId()), item2);
                } else {
                    str = strOptString3;
                }
                z = false;
                String strOptString8 = a.optString("description");
                Intrinsics.checkNotNullExpressionValue(strOptString8, "optString(...)");
                if (v != null) {
                    iOptInt = v.optInt("upVotes");
                } else {
                    iOptInt = 0;
                }
                if (v != null) {
                    iOptInt2 = v.optInt("downVotes");
                } else {
                    iOptInt2 = 0;
                }
                if (a.optBoolean("hasScripts")) {
                    z2 = true;
                } else {
                    if (counts != null) {
                        iOptInt8 = counts.optInt("script");
                    } else {
                        iOptInt8 = 0;
                    }
                    if (iOptInt8 <= 0) {
                        z2 = false;
                    } else {
                        z2 = true;
                    }
                }
                boolean zOptBoolean2 = a.optBoolean("isEndorsed");
                if (counts != null) {
                    iOptInt3 = counts.optInt("script");
                } else {
                    iOptInt3 = 0;
                }
                int iOptInt10 = a.optInt("scriptCount", iOptInt3);
                if (counts != null) {
                    iOptInt4 = counts.optInt("meshPart");
                } else {
                    iOptInt4 = 0;
                }
                if (tech != null) {
                    iOptInt5 = 0;
                } else {
                    iOptInt5 = 0;
                }
                if (counts != null) {
                    iOptInt6 = counts.optInt("animation");
                } else {
                    iOptInt6 = 0;
                }
                if (counts != null) {
                    iOptInt7 = counts.optInt("audio");
                } else {
                    iOptInt7 = 0;
                }
                strOptString2 = a.optString("createdUtc");
                Intrinsics.checkNotNullExpressionValue(strOptString2, "optString(...)");
                String strOptString9 = a.optString("updatedUtc");
                Intrinsics.checkNotNullExpressionValue(strOptString9, "optString(...)");
                long jOptLong2 = cr != null ? cr.optLong("id") : 0L;
                if (cr != null) {
                    str2 = strOptString2;
                    c = 2;
                    if (cr.optInt("type") == 2) {
                        z3 = true;
                    }
                    String strOptString10 = a.optString("categoryPath");
                    Intrinsics.checkNotNullExpressionValue(strOptString10, "optString(...)");
                    Item item3 = new Item(j2, str, str3, z, strOptString8, iOptInt, iOptInt2, z2, zOptBoolean2, price, iOptInt10, iOptInt4, iOptInt5, iOptInt6, iOptInt7, str2, strOptString9, jOptLong2, z3, strOptString10);
                    byId.put(Long.valueOf(item3.getId()), item3);
                } else {
                    str2 = strOptString2;
                    c = 2;
                }
                z3 = false;
                String strOptString11 = a.optString("categoryPath");
                Intrinsics.checkNotNullExpressionValue(strOptString11, "optString(...)");
                Item item4 = new Item(j2, str, str3, z, strOptString8, iOptInt, iOptInt2, z2, zOptBoolean2, price, iOptInt10, iOptInt4, iOptInt5, iOptInt6, iOptInt7, str2, strOptString9, jOptLong2, z3, strOptString11);
                byId.put(Long.valueOf(item4.getId()), item4);
            }
            i++;
            c2 = c;
            jSONObject = null;
        }
        List<Long> list = ids;
        Collection arrayList = new ArrayList();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Iterable iterable = list;
            Item item5 = (Item) byId.get(Long.valueOf(((Number) it.next()).longValue()));
            if (item5 != null) {
                arrayList.add(item5);
            }
            list = (List) iterable;
        }
        return (List) arrayList;
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    public static /* synthetic */ Map<Long, String> thumbnailUrls$default(RobloxApi robloxApi, List list, String str, int i, Object obj) {
        if ((i & 2) != 0) {
            str = "150x150";
        }
        return robloxApi.thumbnailUrls(list, str);
    }

    public final Map<Long, String> thumbnailUrls(List<Long> ids, String size) {
        try {
        Intrinsics.checkNotNullParameter(ids, "ids");
        Intrinsics.checkNotNullParameter(size, "size");
        JSONObject j = Http.json$default(Http.INSTANCE, "https://thumbnails.roblox.com/v1/assets?assetIds=" + CollectionsKt.joinToString(ids, ",", "", "", -1, "...", null) + "&size=" + size + "&format=Png&isCircular=false", null, 2, null);
        HashMap out = new HashMap();
        JSONArray data = j.optJSONArray("data");
        if (data == null) {
            return out;
        }
        int length = data.length();
        for (int i = 0; i < length; i++) {
            JSONObject o = data.getJSONObject(i);
            long id = o.optLong("targetId");
            String strOptString = o.optString("state");
            if (Intrinsics.areEqual(strOptString, "Completed")) {
                out.put(Long.valueOf(id), o.optString("imageUrl"));
            } else if (Intrinsics.areEqual(strOptString, "Pending")) {
                out.put(Long.valueOf(id), "");
            }
        }
        return out;
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    public final String apiKey(Context ctx) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        String string = ctx.getSharedPreferences(PREFS, 0).getString("apiKey", "");
        return string != null ? string : "";
    }

    public final String cookie(Context ctx) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        String string = ctx.getSharedPreferences(PREFS, 0).getString("cookie", "");
        return string != null ? string : "";
    }

    public final void setCredentials(Context ctx, String apiKey, String cookie) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(apiKey, "apiKey");
        Intrinsics.checkNotNullParameter(cookie, "cookie");
        ctx.getSharedPreferences(PREFS, 0).edit().putString("apiKey", StringsKt.trim((CharSequence) apiKey).toString()).putString("cookie", StringsKt.trim((CharSequence) StringsKt.removePrefix(StringsKt.trim((CharSequence) cookie).toString(), (CharSequence) ".ROBLOSECURITY=")).toString()).apply();
    }

    public final void setCookie(Context ctx, String cookie) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(cookie, "cookie");
        ctx.getSharedPreferences(PREFS, 0).edit().putString("cookie", StringsKt.trim((CharSequence) StringsKt.removePrefix(StringsKt.trim((CharSequence) cookie).toString(), (CharSequence) ".ROBLOSECURITY=")).toString()).apply();
    }

    public final boolean isGuest(Context ctx) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        return cookie(ctx).length() == 0;
    }

    public final void setApiKey(Context ctx, String key) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(key, "key");
        ctx.getSharedPreferences(PREFS, 0).edit().putString("apiKey", StringsKt.trim((CharSequence) key).toString()).apply();
    }

    public final boolean hasCredentials(Context ctx) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        if (apiKey(ctx).length() > 0) {
            return true;
        }
        return cookie(ctx).length() > 0;
    }

    public final String userName(Context ctx) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        String string = ctx.getSharedPreferences(PREFS, 0).getString("userName", "");
        return string != null ? string : "";
    }

    public final String displayName(Context ctx) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        String string = ctx.getSharedPreferences(PREFS, 0).getString("displayName", "");
        return string != null ? string : "";
    }

    public final long userId(Context ctx) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        return ctx.getSharedPreferences(PREFS, 0).getLong("userId", 0L);
    }

    public static /* synthetic */ void setUser$default(RobloxApi robloxApi, Context context, long j, String str, String str2, int i, Object obj) {
        if ((i & 8) != 0) {
            str2 = "";
        }
        robloxApi.setUser(context, j, str, str2);
    }

    public final void setUser(Context ctx, long id, String name, String displayName) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(displayName, "displayName");
        ctx.getSharedPreferences(PREFS, 0).edit().putLong("userId", id).putString("userName", name).putString("displayName", displayName).apply();
    }

    /* JADX INFO: compiled from: RobloxApi.kt */
    public static final class AudioQuery {
        private final String artist;
        private final String chart;
        private final String keyword;
        private final Integer maxSeconds;
        private final int minSeconds;
        private final String sort;
        private final String type;

        public AudioQuery() {
            this(null, null, null, 0, null, null, null, 127, null);
        }

        public AudioQuery(String keyword, String type, String sort, int minSeconds, Integer maxSeconds, String chart, String artist) {
            Intrinsics.checkNotNullParameter(keyword, "keyword");
            Intrinsics.checkNotNullParameter(sort, "sort");
            Intrinsics.checkNotNullParameter(chart, "chart");
            Intrinsics.checkNotNullParameter(artist, "artist");
            this.keyword = keyword;
            this.type = type;
            this.sort = sort;
            this.minSeconds = minSeconds;
            this.maxSeconds = maxSeconds;
            this.chart = chart;
            this.artist = artist;
        }

        public /* synthetic */ AudioQuery(String str, String str2, String str3, int i, Integer num, String str4, String str5, int i2, DefaultConstructorMarker defaultConstructorMarker) {
            this((i2 & 1) != 0 ? "" : str, (i2 & 2) != 0 ? null : str2, (i2 & 4) != 0 ? "Relevance" : str3, (i2 & 8) != 0 ? 0 : i, (i2 & 16) != 0 ? null : num, (i2 & 32) != 0 ? "None" : str4, (i2 & 64) != 0 ? "" : str5);
        }

        public final String getKeyword() {
            return this.keyword;
        }

        public final String getType() {
            return this.type;
        }

        public final String getSort() {
            return this.sort;
        }

        public final int getMinSeconds() {
            return this.minSeconds;
        }

        public final Integer getMaxSeconds() {
            return this.maxSeconds;
        }

        public final String getChart() {
            return this.chart;
        }

        public final String getArtist() {
            return this.artist;
        }
    }

    /* JADX INFO: compiled from: RobloxApi.kt */
    public static final class AudioItem {
        private final String album;
        private final String artist;
        private final String creator;
        private final long id;
        private final String name;
        private final int seconds;
        private final String title;
        private final String type;
        private final boolean verified;

        public AudioItem(long id, String name, String artist, String title, int seconds, String type, String creator, boolean verified, String album) {
            Intrinsics.checkNotNullParameter(name, "name");
            Intrinsics.checkNotNullParameter(artist, "artist");
            Intrinsics.checkNotNullParameter(title, "title");
            Intrinsics.checkNotNullParameter(type, "type");
            Intrinsics.checkNotNullParameter(creator, "creator");
            Intrinsics.checkNotNullParameter(album, "album");
            this.id = id;
            this.name = name;
            this.artist = artist;
            this.title = title;
            this.seconds = seconds;
            this.type = type;
            this.creator = creator;
            this.verified = verified;
            this.album = album;
        }

        public final String getArtist() {
            return this.artist;
        }

        public final long getId() {
            return this.id;
        }

        public final String getName() {
            return this.name;
        }

        public final int getSeconds() {
            return this.seconds;
        }

        public final String getTitle() {
            return this.title;
        }

        public final String getType() {
            return this.type;
        }

        public final String getAlbum() {
            return this.album;
        }

        public final String getCreator() {
            return this.creator;
        }

        public final boolean getVerified() {
            return this.verified;
        }
    }

    /* JADX INFO: compiled from: RobloxApi.kt */
    public static final class AudioPage {
        private final List<AudioItem> items;
        private final String nextToken;

        public AudioPage(List<AudioItem> items, String nextToken) {
            Intrinsics.checkNotNullParameter(items, "items");
            this.items = items;
            this.nextToken = nextToken;
        }

        public final List<AudioItem> getItems() {
            return this.items;
        }

        public final String getNextToken() {
            return this.nextToken;
        }
    }

    public static /* synthetic */ AudioPage searchAudio$default(RobloxApi robloxApi, Context context, AudioQuery audioQuery, String str, int i, int i2, Object obj) {
        if ((i2 & 8) != 0) {
            i = 30;
        }
        return robloxApi.searchAudio(context, audioQuery, str, i);
    }

    /* JADX WARN: Code duplicated, block: B:86:0x0256  */
    public final AudioPage searchAudio(Context ctx, AudioQuery q, String pageToken, int pageSize) {
        try {
        String c;
        String str;
        boolean z;
        boolean z2 = false;
        boolean z3 = false;
        String strOptString;
        Intrinsics.checkNotNullParameter(q, "q");
        StringBuilder sb = new StringBuilder("https://apis.roblox.com/toolbox-service/v2/assets:search?searchCategoryType=Audio&maxPageSize=" + pageSize + "&includeOnlyVerifiedCreators=false");
        if (!StringsKt.isBlank(q.getKeyword())) {
            sb.append("&query=").append(enc(StringsKt.trim((CharSequence) q.getKeyword()).toString()));
        }
        if (q.getType() != null) {
            sb.append("&audioTypes=").append(q.getType());
        }
        if (!Intrinsics.areEqual(q.getSort(), "Relevance")) {
            sb.append("&sortCategory=").append(q.getSort()).append("&sortDirection=").append(Intrinsics.areEqual(q.getSort(), "AudioDuration") ? "Ascending" : "Descending");
        }
        if (q.getMinSeconds() > 0) {
            sb.append("&audioMinDurationSeconds=").append(q.getMinSeconds());
        }
        Integer maxSeconds = q.getMaxSeconds();
        if (maxSeconds != null) {
            sb.append("&audioMaxDurationSeconds=").append(maxSeconds.intValue());
        }
        if (!Intrinsics.areEqual(q.getChart(), "None")) {
            sb.append("&musicChartType=").append(q.getChart()).append("&includeTopCharts=true");
        }
        if (!StringsKt.isBlank(q.getArtist())) {
            sb.append("&audioArtist=").append(enc(q.getArtist()));
        }
        if (pageToken != null) {
            sb.append("&pageToken=").append(enc(pageToken));
        }
        String str2 = "";
        if (ctx == null || (c = INSTANCE.cookie(ctx)) == null) {
            c = "";
        }
        Map headers = c.length() > 0 ? MapsKt.mapOf(TuplesKt.to("Cookie", ".ROBLOSECURITY=" + c)) : MapsKt.emptyMap();
        Http http = Http.INSTANCE;
        String string = sb.toString();
        Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        JSONObject j = new JSONObject(new String(Http.get$default(http, string, headers, 0, 4, null), Charsets.UTF_8));
        JSONArray arr = j.optJSONArray("creatorStoreAssets");
        if (arr == null && (arr = j.optJSONArray("assets")) == null && (arr = j.optJSONArray("data")) == null) {
            arr = new JSONArray();
        }
        ArrayList out = new ArrayList();
        int i = 0;
        int length = arr.length();
        while (i < length) {
            JSONObject o = arr.optJSONObject(i);
            if (o == null) {
                str = str2;
            } else {
                JSONObject a = o.optJSONObject("asset");
                if (a == null) {
                    a = o;
                }
                long id = a.optLong("id");
                if (id != 0) {
                    JSONObject ad = a.optJSONObject("audioDetails");
                    if (ad == null && (ad = a.optJSONObject("audio")) == null) {
                        ad = a;
                    }
                    JSONObject jSONObjectOptJSONObject = o.optJSONObject("creator");
                    if (jSONObjectOptJSONObject == null) {
                        jSONObjectOptJSONObject = a.optJSONObject("creator");
                    }
                    JSONObject cr = jSONObjectOptJSONObject;
                    int secs = ad.optInt("durationSeconds", ad.optInt("duration", a.optInt("durationSeconds")));
                    String type = ad.optString("audioType", a.optString("audioType", a.optString("assetSubType", str2)));
                    String strOptString2 = a.optString("name");
                    Intrinsics.checkNotNullExpressionValue(strOptString2, "optString(...)");
                    str = str2;
                    String strOptString3 = ad.optString("artist", a.optString("artist"));
                    Intrinsics.checkNotNullExpressionValue(strOptString3, "optString(...)");
                    String strOptString4 = ad.optString("title", a.optString("title"));
                    Intrinsics.checkNotNullExpressionValue(strOptString4, "optString(...)");
                    Intrinsics.checkNotNull(type);
                    String str3 = (cr == null || (strOptString = cr.optString("name")) == null) ? str : strOptString;
                    if (cr != null && cr.optBoolean("verified")) {
                        z = true;
                    } else {
                        if (cr != null) {
                            z = true;
                            z3 = cr.optBoolean("isVerifiedCreator");
                            if (!z3) {
                                z2 = false;
                            }
                            String strOptString5 = ad.optString("album", a.optString("album"));
                            Intrinsics.checkNotNullExpressionValue(strOptString5, "optString(...)");
                            out.add(new AudioItem(id, strOptString2, strOptString3, strOptString4, secs, type, str3, z2, strOptString5));
                        } else {
                            z = true;
                        }
                        if (!z3) {
                            z2 = false;
                        }
                        String strOptString6 = ad.optString("album", a.optString("album"));
                        Intrinsics.checkNotNullExpressionValue(strOptString6, "optString(...)");
                        out.add(new AudioItem(id, strOptString2, strOptString3, strOptString4, secs, type, str3, z2, strOptString6));
                    }
                    z2 = z;
                    String strOptString7 = ad.optString("album", a.optString("album"));
                    Intrinsics.checkNotNullExpressionValue(strOptString7, "optString(...)");
                    out.add(new AudioItem(id, strOptString2, strOptString3, strOptString4, secs, type, str3, z2, strOptString7));
                } else {
                    str = str2;
                }
            }
            i++;
            str2 = str;
        }
        String next = j.optString("nextPageToken");
        Intrinsics.checkNotNull(next);
        if (!((next.length() > 0) && !Intrinsics.areEqual(next, "null"))) {
            next = null;
        }
        return new AudioPage(out, next);
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    public final AudioPage searchAudioLegacy(String keyword, String cursor) {
        Intrinsics.checkNotNullParameter(keyword, "keyword");
        Page page = search$default(this, keyword, Category.AUDIO, cursor, 0, 8, null);
        Iterable iterableChunked = CollectionsKt.chunked(page.getIds(), 30);
        Collection arrayList = new ArrayList();
        Iterator it = iterableChunked.iterator();
        while (it.hasNext()) {
            CollectionsKt.addAll(arrayList, INSTANCE.details((List) it.next()));
        }
        Iterable details = (List) arrayList;
        Iterable<Item> iterable = details;
        Collection arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
        for (Item item : iterable) {
            arrayList2.add(new AudioItem(item.getId(), item.getName(), item.getCreator(), item.getName(), 0, "", item.getCreator(), item.getVerified(), ""));
        }
        return new AudioPage((List) arrayList2, page.getNextCursor());
    }

    /* JADX INFO: compiled from: RobloxApi.kt */
    public static final class InvItem {
        private final String created;
        private final long id;
        private final String name;

        public InvItem(long id, String name, String created) {
            Intrinsics.checkNotNullParameter(name, "name");
            Intrinsics.checkNotNullParameter(created, "created");
            this.id = id;
            this.name = name;
            this.created = created;
        }

        public final String getCreated() {
            return this.created;
        }

        public final long getId() {
            return this.id;
        }

        public final String getName() {
            return this.name;
        }
    }

    /* JADX WARN: Code duplicated, block: B:14:0x005d  */
    public final Pair<List<InvItem>, String> inventory(Context ctx, long userId, int assetType, String cursor) throws AuthRequiredException, HttpException {
        try {
        String str;
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        String c = cookie(ctx);
        Map headers = c.length() > 0 ? MapsKt.mapOf(TuplesKt.to("Cookie", ".ROBLOSECURITY=" + c)) : MapsKt.emptyMap();
        if (cursor != null) {
            str = "&cursor=" + INSTANCE.enc(cursor);
            if (str == null) {
                str = "";
            }
        } else {
            str = "";
        }
        String url = "https://inventory.roblox.com/v2/users/" + userId + "/inventory/" + assetType + "?limit=100&sortOrder=Desc" + str;
        Http.Response r = Http.call$default(Http.INSTANCE, url, null, headers, null, null, 0, 58, null);
        if (r.getCode() == 401) {
            throw new AuthRequiredException("Sign in again to see your inventory.");
        }
        if (!r.getOk()) {
            throw new HttpException(r.getCode(), Http.INSTANCE.describe(r.getCode(), r.getBody()));
        }
        JSONObject j = new JSONObject(r.getText());
        JSONArray arr = j.optJSONArray("data");
        if (arr == null) {
            arr = new JSONArray();
        }
        ArrayList out = new ArrayList();
        int i = 0;
        int length = arr.length();
        while (i < length) {
            JSONObject o = arr.optJSONObject(i);
            if (o != null) {
                long id = o.optLong("assetId", o.optLong("id"));
                if (id != 0) {
                    String strOptString = o.optString("assetName", o.optString("name", "Asset " + id));
                    Intrinsics.checkNotNullExpressionValue(strOptString, "optString(...)");
                    String strOptString2 = o.optString("created");
                    Intrinsics.checkNotNullExpressionValue(strOptString2, "optString(...)");
                    out.add(new InvItem(id, strOptString, strOptString2));
                }
            }
            i++;
        }
        String strOptString3 = j.optString("nextPageCursor");
        Intrinsics.checkNotNull(strOptString3);
        if (!((strOptString3.length() > 0) && !Intrinsics.areEqual(strOptString3, "null"))) {
            strOptString3 = null;
        }
        return TuplesKt.to(out, strOptString3);
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    public final Long parseAssetId(String text) {
        List<String> groupValues;
        String str;
        Intrinsics.checkNotNullParameter(text, "text");
        String t = StringsKt.trim((CharSequence) text).toString();
        Long longOrNull = StringsKt.toLongOrNull(t);
        if (longOrNull != null) {
            return Long.valueOf(longOrNull.longValue());
        }
        Regex rx = new Regex("(?:[?&]id=|/asset/|/library/|/catalog/|/store/asset/|/plugins/|/models/|rbxassetid://)(\\d+)", RegexOption.IGNORE_CASE);
        MatchResult matchResultFind$default = (rx).find(t, 0);
        if (matchResultFind$default == null || (groupValues = matchResultFind$default.getGroupValues()) == null || (str = groupValues.get(1)) == null) {
            return null;
        }
        return StringsKt.toLongOrNull(str);
    }

    public static /* synthetic */ byte[] downloadAsset$default(RobloxApi robloxApi, Context context, long j, Integer num, int i, Object obj) {
        if ((i & 4) != 0) {
            num = null;
        }
        try { return robloxApi.downloadAsset(context, j, num); } catch (Throwable _ktEx) { throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx); }
    }

    /* JADX WARN: Code duplicated, block: B:56:0x016a  */
    /* JADX WARN: Code duplicated, block: B:58:0x016d A[Catch: Exception -> 0x01ac, TryCatch #1 {Exception -> 0x01ac, blocks: (B:46:0x014d, B:48:0x0153, B:51:0x015a, B:53:0x0162, B:58:0x016d, B:60:0x0182), top: B:109:0x014d }] */
    /* JADX WARN: Code duplicated, block: B:60:0x0182 A[Catch: Exception -> 0x01ac, TRY_LEAVE, TryCatch #1 {Exception -> 0x01ac, blocks: (B:46:0x014d, B:48:0x0153, B:51:0x015a, B:53:0x0162, B:58:0x016d, B:60:0x0182), top: B:109:0x014d }] */
    /* JADX WARN: Code duplicated, block: B:7:0x003b  */
    public final byte[] downloadAsset(Context ctx, long id, Integer version) throws Exception {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        String key = apiKey(ctx);
        String cookie = cookie(ctx);
        String vQuery = version != null ? ("&version=" + version.intValue()) : "";
        String vPath = version != null ? ("/version/" + version.intValue()) : "";
        Exception lastError = null;

        // 1. Open Cloud Asset Delivery API (if API key configured)
        if (key.length() > 0) {
            try {
                String path = "assetId/" + id + vPath;
                JSONObject j = Http.INSTANCE.json("https://apis.roblox.com/asset-delivery-api/v1/" + path, MapsKt.mapOf(TuplesKt.to("x-api-key", key)));
                String loc = j.optString("location");
                if (loc != null && loc.length() > 0) {
                    return ungzip(Http.get$default(Http.INSTANCE, loc, null, 0, 6, null));
                }
                lastError = new IOException("No download location returned");
            } catch (Exception e) {
                lastError = e;
            }
            if (lastError != null) {
                com.goydevv.studrblx.core.OutputLog.INSTANCE.warn("Asset " + id + ": Open Cloud API key route failed: " + lastError);
            }
        }

        // 2. Authenticated Roblox AssetDelivery v2 -> CDN location -> v1 fallback
        if (cookie.length() > 0) {
            Map<String, String> headers = new java.util.LinkedHashMap<>();
            headers.put("Cookie", ".ROBLOSECURITY=" + cookie);
            headers.put("Roblox-Browser-Asset-Request", "true");
            headers.put("User-Agent", "Roblox/WinInet");
            try {
                JSONObject j2 = Http.INSTANCE.json("https://assetdelivery.roblox.com/v2/assetId/" + id + vPath, headers);
                JSONArray locs = j2.optJSONArray("locations");
                if (locs != null && locs.length() > 0) {
                    JSONObject first = locs.optJSONObject(0);
                    String loc2 = first != null ? first.optString("location") : null;
                    if (loc2 != null && loc2.length() > 0) {
                        return ungzip(Http.get$default(Http.INSTANCE, loc2, null, 0, 6, null));
                    }
                }
            } catch (Exception e) {
                lastError = e;
                com.goydevv.studrblx.core.OutputLog.INSTANCE.warn("Asset " + id + ": cookie route (assetdelivery v2) failed: " + e);
            }
            try {
                return ungzip(Http.get$default(Http.INSTANCE, "https://assetdelivery.roblox.com/v1/asset/?id=" + id + vQuery, headers, 0, 4, null));
            } catch (Exception e) {
                lastError = e;
                com.goydevv.studrblx.core.OutputLog.INSTANCE.warn("Asset " + id + ": cookie route (assetdelivery v1) failed: " + e);
            }
        }

        // 3. Public / unauthenticated Roblox AssetDelivery v2 -> v1 fallback (for public textures, baseplate, animations, meshes)
        try {
            Map<String, String> pubHeaders = MapsKt.mapOf(TuplesKt.to("Roblox-Browser-Asset-Request", "true"), TuplesKt.to("User-Agent", "Roblox/WinInet"));
            JSONObject jPub = Http.INSTANCE.json("https://assetdelivery.roblox.com/v2/assetId/" + id + vPath, pubHeaders);
            JSONArray locsPub = jPub.optJSONArray("locations");
            if (locsPub != null && locsPub.length() > 0) {
                JSONObject first = locsPub.optJSONObject(0);
                String locPub = first != null ? first.optString("location") : null;
                if (locPub != null && locPub.length() > 0) {
                    return ungzip(Http.get$default(Http.INSTANCE, locPub, null, 0, 6, null));
                }
            }
        } catch (Exception e) {
            if (lastError == null) lastError = e;
        }
        try {
            return ungzip(Http.get$default(Http.INSTANCE, "https://assetdelivery.roblox.com/v1/asset/?id=" + id + vQuery, null, 0, 6, null));
        } catch (HttpException e6) {
            if (cookie.length() == 0 && key.length() == 0 && (e6.getCode() == 401 || e6.getCode() == 403)) {
                throw new AuthRequiredException("Roblox requires sign-in to download this asset.");
            }
            lastError = e6;
        } catch (Exception e7) {
            lastError = e7;
        }

        if ((lastError instanceof HttpException) && (((HttpException) lastError).getCode() == 401 || ((HttpException) lastError).getCode() == 403)) {
            throw new AuthRequiredException("Roblox rejected credentials (HTTP " + ((HttpException) lastError).getCode() + "). Try signing in again.");
        }
        if (lastError != null) {
            throw lastError;
        }
        throw new IOException("Download failed for asset " + id);
    }

    public final byte[] ungzip(byte[] b) throws IOException {
        Intrinsics.checkNotNullParameter(b, "b");
        if (b.length > 2 && b[0] == 31 && b[1] == -117) {
            GZIPInputStream gZIPInputStream = new GZIPInputStream(new ByteArrayInputStream(b));
            try {
                byte[] bytes = ByteStreamsKt.readBytes(gZIPInputStream);
                CloseableKt.closeFinally(gZIPInputStream, null);
                return bytes;
            } catch (Throwable th) {
                try {
                    throw th;
                } catch (Throwable th2) {
                    CloseableKt.closeFinally(gZIPInputStream, th);
                    throw th2;
                }
            }
        }
        return b;
    }
}

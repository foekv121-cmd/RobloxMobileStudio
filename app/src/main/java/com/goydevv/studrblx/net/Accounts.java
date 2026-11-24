package com.goydevv.studrblx.net;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: compiled from: Accounts.kt */
/* JADX INFO: loaded from: classes6.dex */
public final class Accounts {
    private static final String PREFS = "roblox_auth";
    public static final Accounts INSTANCE = new Accounts();
    private static final CopyOnWriteArrayList<Function0<Unit>> listeners = new CopyOnWriteArrayList<>();

    /* JADX INFO: compiled from: Accounts.kt */
    public static final class Account {
        private final String cookie;
        private final String displayName;
        private final String name;
        private final long userId;

        public Account(long userId, String name, String displayName, String cookie) {
            Intrinsics.checkNotNullParameter(name, "name");
            Intrinsics.checkNotNullParameter(displayName, "displayName");
            Intrinsics.checkNotNullParameter(cookie, "cookie");
            this.userId = userId;
            this.name = name;
            this.displayName = displayName;
            this.cookie = cookie;
        }

        public final String getCookie() {
            return this.cookie;
        }

        public final String getDisplayName() {
            return this.displayName;
        }

        public final String getName() {
            return this.name;
        }

        public final long getUserId() {
            return this.userId;
        }
    }

    private Accounts() {
    }

    public final CopyOnWriteArrayList<Function0<Unit>> getListeners() {
        return listeners;
    }

    private final SharedPreferences prefs(Context ctx) {
        return ctx.getApplicationContext().getSharedPreferences(PREFS, 0);
    }

    /* JADX WARN: Code duplicated, block: B:33:0x00cd  */
    public final List<Account> list(Context ctx) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        String raw = prefs(ctx).getString("accounts", null);
        ArrayList out = new ArrayList();
        if (raw != null) {
            try {
                JSONArray a = new JSONArray(raw);
                int length = a.length();
                for (int i = 0; i < length; i++) {
                    JSONObject o = a.getJSONObject(i);
                    long jOptLong = o.optLong("id");
                    String strOptString = o.optString("name");
                    Intrinsics.checkNotNullExpressionValue(strOptString, "optString(...)");
                    String strOptString2 = o.optString("displayName");
                    Intrinsics.checkNotNullExpressionValue(strOptString2, "optString(...)");
                    String strOptString3 = o.optString("cookie");
                    Intrinsics.checkNotNullExpressionValue(strOptString3, "optString(...)");
                    out.add(new Account(jOptLong, strOptString, strOptString2, strOptString3));
                }
            } catch (Exception e) {
            }
        }
        String legacy = RobloxApi.INSTANCE.cookie(ctx);
        long legacyId = RobloxApi.INSTANCE.userId(ctx);
        boolean z = true;
        if ((legacy.length() > 0) && legacyId != 0) {
            ArrayList arrayList = out;
            if (!(arrayList instanceof Collection) || !arrayList.isEmpty()) {
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    if (((Account) it.next()).getUserId() == legacyId) {
                        z = false;
                        break;
                    }
                }
            }
            if (z) {
                out.add(0, new Account(legacyId, RobloxApi.INSTANCE.userName(ctx), RobloxApi.INSTANCE.displayName(ctx), legacy));
                save(ctx, out);
            }
        }
        return out;
    }

    private final void save(Context ctx, List<Account> list) {
        try {
        JSONArray a = new JSONArray();
        for (Account x : list) {
            a.put(new JSONObject().put("id", x.getUserId()).put("name", x.getName()).put("displayName", x.getDisplayName()).put("cookie", x.getCookie()));
        }
        prefs(ctx).edit().putString("accounts", a.toString()).apply();
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    public final void upsert(Context ctx, Account acc) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(acc, "acc");
        Iterable list = list(ctx);
        Collection arrayList = new ArrayList();
        Iterator it = list.iterator();
        while (true) {
            if (!it.hasNext()) {
                List<Account> mutableList = CollectionsKt.toMutableList(arrayList);
                mutableList.add(0, acc);
                save(ctx, mutableList);
                notifyChanged();
                return;
            }
            Object next = it.next();
            if (((Account) next).getUserId() != acc.getUserId()) {
                arrayList.add(next);
            }
        }
    }

    public final void remove(Context ctx, long userId) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Iterable list = list(ctx);
        Collection arrayList = new ArrayList();
        for (Object obj : list) {
            if (((Account) obj).getUserId() != userId) {
                arrayList.add(obj);
            }
        }
        save(ctx, (List) arrayList);
        notifyChanged();
    }

    public final Account active(Context ctx) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        String c = RobloxApi.INSTANCE.cookie(ctx);
        Object obj = null;
        if (c.length() == 0) {
            return null;
        }
        long id = RobloxApi.INSTANCE.userId(ctx);
        for (Object obj2 : list(ctx)) {
            if (((Account) obj2).getUserId() == id) {
                obj = obj2;
                break;
            }
        }
        Account account = (Account) obj;
        return account == null ? new Account(id, RobloxApi.INSTANCE.userName(ctx), RobloxApi.INSTANCE.displayName(ctx), c) : account;
    }

    public final void switchTo(Context ctx, Account acc) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(acc, "acc");
        RobloxApi.INSTANCE.setCookie(ctx, acc.getCookie());
        RobloxApi.INSTANCE.setUser(ctx, acc.getUserId(), acc.getName(), acc.getDisplayName());
        RobloxCloud.INSTANCE.resetSession();
        notifyChanged();
    }

    public final void useGuest(Context ctx) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        RobloxApi.INSTANCE.setCookie(ctx, "");
        RobloxApi.INSTANCE.setUser(ctx, 0L, "", "");
        RobloxCloud.INSTANCE.resetSession();
        notifyChanged();
    }

    public final void notifyChanged() {
        Iterator it = listeners.iterator();
        while (it.hasNext()) {
            ((Function0) it.next()).invoke();
        }
    }
}

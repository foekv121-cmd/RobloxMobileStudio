package com.goydevv.studrblx.net;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: WebPermissions.kt */
/* JADX INFO: loaded from: classes6.dex */
public final class WebPermissions {
    private static final String PREFS = "web_permissions";
    private static volatile Function3<? super String, ? super String, ? super Function1<? super Boolean, Unit>, Unit> asker;
    public static final WebPermissions INSTANCE = new WebPermissions();
    private static final ConcurrentHashMap<String, CountDownLatch> pending = new ConcurrentHashMap<>();

    private WebPermissions() {
    }

    public final Function3<String, String, Function1<? super Boolean, Unit>, Unit> getAsker() {
        return (Function3<String, String, Function1<? super Boolean, Unit>, Unit>) asker;
    }

    public final void setAsker(Function3<? super String, ? super String, ? super Function1<? super Boolean, Unit>, Unit> function3) {
        asker = function3;
    }

    private final SharedPreferences prefs(Context ctx) {
        return ctx.getApplicationContext().getSharedPreferences(PREFS, 0);
    }

    public final boolean isRobloxHost(String host) {
        Intrinsics.checkNotNullParameter(host, "host");
        String lowerCase = host.toLowerCase(Locale.ROOT);
        Intrinsics.checkNotNullExpressionValue(lowerCase, "toLowerCase(...)");
        String h = StringsKt.trimEnd(lowerCase, '.');
        Iterable<String> iterableListOf = CollectionsKt.listOf(new String[]{"roblox.com", "rbxcdn.com", "robloxlabs.com", "rbx.com", "roblox.cn", "roblox.qq.com", "robloxdev.com"});
        if ((iterableListOf instanceof Collection) && ((Collection) iterableListOf).isEmpty()) {
            return false;
        }
        for (String str : iterableListOf) {
            if (Intrinsics.areEqual(h, str) || StringsKt.endsWith(h, new StringBuilder().append(".").append(str).toString(), false)) {
                return true;
            }
        }
        return false;
    }

    public final Boolean decision(Context ctx, String host) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(host, "host");
        SharedPreferences p = prefs(ctx);
        if (p.contains(host)) {
            return Boolean.valueOf(p.getBoolean(host, false));
        }
        return null;
    }

    public final Map<String, Boolean> remembered(Context ctx) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Map<String, ?> all = prefs(ctx).getAll();
        Intrinsics.checkNotNullExpressionValue(all, "getAll(...)");
        Collection arrayList = new ArrayList();
        for (Map.Entry<String, ?> entry : all.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            Pair pair = null;
            Boolean bool = value instanceof Boolean ? (Boolean) value : null;
            if (bool != null) {
                pair = TuplesKt.to(key, Boolean.valueOf(bool.booleanValue()));
            }
            if (pair != null) {
                arrayList.add(pair);
            }
        }
        return MapsKt.toMap((List) arrayList);
    }

    public final void forgetAll(Context ctx) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        prefs(ctx).edit().clear().apply();
    }

    public final boolean allow(final Context ctx, final String host, String who) throws InterruptedException {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(host, "host");
        Intrinsics.checkNotNullParameter(who, "who");
        if (isRobloxHost(host)) {
            return false;
        }
        Boolean boolDecision = decision(ctx, host);
        if (boolDecision != null) {
            return boolDecision.booleanValue();
        }
        Function3<? super String, ? super String, ? super Function1<? super Boolean, Unit>, Unit> function3 = asker;
        if (function3 == null) {
            return false;
        }
        final Ref.BooleanRef mine = new Ref.BooleanRef();
        ConcurrentHashMap<String, CountDownLatch> concurrentHashMap = pending;
        final Function1 function1 = new Function1() { // from class: com.goydevv.studrblx.net.WebPermissions$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return WebPermissions.allow$lambda$4(mine, (String) obj);
            }
        };
        CountDownLatch countDownLatchComputeIfAbsent = concurrentHashMap.computeIfAbsent(host, new Function() { // from class: com.goydevv.studrblx.net.WebPermissions$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return WebPermissions.allow$lambda$5(function1, obj);
            }
        });
        Intrinsics.checkNotNullExpressionValue(countDownLatchComputeIfAbsent, "computeIfAbsent(...)");
        final CountDownLatch latch = countDownLatchComputeIfAbsent;
        if (mine.element) {
            try {
                ((Function3) function3).invoke(host, who, new Function1() { // from class: com.goydevv.studrblx.net.WebPermissions$$ExternalSyntheticLambda2
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj) {
                        return WebPermissions.allow$lambda$6(ctx, host, latch, (Boolean) obj);
                    }
                });
            } catch (Exception e) {
                latch.countDown();
            }
        }
        latch.await(2L, TimeUnit.MINUTES);
        if (mine.element) {
            pending.remove(host);
        }
        return Intrinsics.areEqual((Object) decision(ctx, host), (Object) true);
    }

    static final CountDownLatch allow$lambda$4(Ref.BooleanRef $mine, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $mine.element = true;
        return new CountDownLatch(1);
    }

    static final CountDownLatch allow$lambda$5(Function1 $tmp0, Object p0) {
        return (CountDownLatch) $tmp0.invoke(p0);
    }

    static final Unit allow$lambda$6(Context $ctx, String $host, CountDownLatch $latch, Boolean ok) {
        if (ok != null) {
            INSTANCE.prefs($ctx).edit().putBoolean($host, ok.booleanValue()).commit();
        }
        $latch.countDown();
        return Unit.INSTANCE;
    }
}

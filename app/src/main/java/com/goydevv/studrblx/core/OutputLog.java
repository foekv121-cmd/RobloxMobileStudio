package com.goydevv.studrblx.core;

import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: OutputLog.kt */
/* JADX INFO: loaded from: classes4.dex */
public final class OutputLog {
    public static final OutputLog INSTANCE = new OutputLog();
    private static final ArrayList<Entry> entries = new ArrayList<>();
    private static final CopyOnWriteArrayList<Function1<Entry, Unit>> listeners = new CopyOnWriteArrayList<>();

    /* JADX INFO: compiled from: OutputLog.kt */
    public enum Level {
        PRINT,
        INFO,
        WARN,
        ERROR;

        private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries(values());

        public static EnumEntries<Level> getEntries() {
            return $ENTRIES;
        }
    }

    private OutputLog() {
    }

    /* JADX INFO: compiled from: OutputLog.kt */
    public static final class Entry {
        private final Level level;
        private final String text;
        private final long time;

        public Entry(long time, Level level, String text) {
            Intrinsics.checkNotNullParameter(level, "level");
            Intrinsics.checkNotNullParameter(text, "text");
            this.time = time;
            this.level = level;
            this.text = text;
        }

        public final Level getLevel() {
            return this.level;
        }

        public final String getText() {
            return this.text;
        }

        public final long getTime() {
            return this.time;
        }
    }

    public final CopyOnWriteArrayList<Function1<Entry, Unit>> getListeners() {
        return listeners;
    }

    public final List<Entry> snapshot() {
        List<Entry> list;
        synchronized (entries) {
            list = CollectionsKt.toList(entries);
        }
        return list;
    }

    public final void add(Level level, String text) {
        Intrinsics.checkNotNullParameter(level, "level");
        Intrinsics.checkNotNullParameter(text, "text");
        Entry e = new Entry(System.currentTimeMillis(), level, text);
        synchronized (entries) {
            entries.add(e);
            if (entries.size() > 3000) {
                entries.subList(0, 500).clear();
            }
            Unit unit = Unit.INSTANCE;
        }
        try {
            Log.i("MobileStudio", "[" + level + "] " + text);
        } catch (Throwable th) {
        }
        Iterator<Function1<Entry, Unit>> it = listeners.iterator();
        Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
        while (it.hasNext()) {
            it.next().invoke(e);
        }
    }

    public final void clear() {
        synchronized (entries) {
            entries.clear();
            Unit unit = Unit.INSTANCE;
        }
        Iterator<Function1<Entry, Unit>> it = listeners.iterator();
        Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
        while (it.hasNext()) {
            it.next().invoke(null);
        }
    }

    public final void print(String s) {
        Intrinsics.checkNotNullParameter(s, "s");
        add(Level.PRINT, s);
    }

    public final void info(String s) {
        Intrinsics.checkNotNullParameter(s, "s");
        add(Level.INFO, s);
    }

    public final void warn(String s) {
        Intrinsics.checkNotNullParameter(s, "s");
        add(Level.WARN, s);
    }

    public final void error(String s) {
        Intrinsics.checkNotNullParameter(s, "s");
        add(Level.ERROR, s);
    }
}

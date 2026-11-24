package com.goydevv.studrblx.util;

import kotlin.text.StringsKt;

public final class KtCompat {
    private KtCompat() {}

    public static String substringBefore(String s, char delim) {
        return StringsKt.substringBefore(s, delim, s);
    }

    public static String substringBefore(String s, String delim) {
        return StringsKt.substringBefore(s, delim, s);
    }

    public static String substringBeforeLast(String s, char delim) {
        return StringsKt.substringBeforeLast(s, delim, s);
    }

    public static String substringBeforeLast(String s, String delim) {
        return StringsKt.substringBeforeLast(s, delim, s);
    }

    public static String substringAfter(String s, char delim) {
        return StringsKt.substringAfter(s, delim, s);
    }

    public static String substringAfter(String s, String delim) {
        return StringsKt.substringAfter(s, delim, s);
    }

    public static String substringAfterLast(String s, char delim) {
        return StringsKt.substringAfterLast(s, delim, s);
    }

    public static String substringAfterLast(String s, String delim) {
        return StringsKt.substringAfterLast(s, delim, s);
    }

    @SuppressWarnings("unchecked")
    public static <E extends Throwable> RuntimeException sneakyThrow(Throwable t) throws E {
        throw (E) t;
    }
}

package com.goydevv.studrblx.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.collections.SetsKt;
import kotlin.io.CloseableKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import kotlin.text.StringsKt;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: compiled from: RobloxApi.kt */
/* JADX INFO: loaded from: classes6.dex */
public final class Http {
    private static final String UA = "MobileStudio/1.0 (Android)";
    public static final Http INSTANCE = new Http();
    private static final Set<String> authHeaders = SetsKt.setOf(new String[]{"cookie", "x-api-key", "x-csrf-token"});

    private Http() {
    }

    /* JADX INFO: compiled from: RobloxApi.kt */
    public static final class Response {
        private final byte[] body;
        private final int code;
        private final Map<String, List<String>> headers;

        /* JADX WARN: Multi-variable type inference failed */
        public Response(int code, byte[] body, Map<String, ? extends List<String>> headers) {
            Intrinsics.checkNotNullParameter(body, "body");
            Intrinsics.checkNotNullParameter(headers, "headers");
            this.code = code;
            this.body = body;
            this.headers = (Map<String, List<String>>) (Map<?, ?>) headers;
        }

        public final byte[] getBody() {
            return this.body;
        }

        public final int getCode() {
            return this.code;
        }

        public final String header(String name) {
            Object next;
            List list;
            boolean z;
            Intrinsics.checkNotNullParameter(name, "name");
            Iterator it = this.headers.entrySet().iterator();
            do {
                if (it.hasNext()) {
                    next = it.next();
                    String str = (String) ((Map.Entry) next).getKey();
                    z = false;
                    if (str != null && StringsKt.equals(str, name, true)) {
                        z = true;
                    }
                } else {
                    next = null;
                    break;
                }
            } while (!z);
            Map.Entry entry = (Map.Entry) next;
            if (entry == null || (list = (List) entry.getValue()) == null) {
                return null;
            }
            return (String) CollectionsKt.firstOrNull(list);
        }

        public final String getText() {
            return new String(this.body, Charsets.UTF_8);
        }

        public final boolean getOk() {
            int i = this.code;
            return 200 <= i && i < 300;
        }
    }

    private final boolean isRoblox(String u) {
        try {
            URL url = new URL(u);
            if (!StringsKt.equals(url.getProtocol(), "https", true)) {
                return false;
            }
            String host = url.getHost();
            Intrinsics.checkNotNullExpressionValue(host, "getHost(...)");
            String lowerCase = host.toLowerCase(Locale.ROOT);
            Intrinsics.checkNotNullExpressionValue(lowerCase, "toLowerCase(...)");
            return Intrinsics.areEqual(lowerCase, "roblox.com") || StringsKt.endsWith(lowerCase, ".roblox.com", false);
        } catch (Exception e) {
            return false;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ Response call$default(Http http, String str, String str2, Map map, byte[] bArr, String str3, int i, int i2, Object obj) {
        String str4;
        int i3;
        if ((i2 & 2) != 0) {
            str2 = "GET";
        }
        String str5 = str2;
        if ((i2 & 4) != 0) {
            map = MapsKt.emptyMap();
        }
        Map map2 = map;
        byte[] bArr2 = (i2 & 8) != 0 ? null : bArr;
        if ((i2 & 16) == 0) {
            str4 = str3;
        } else {
            str4 = null;
        }
        if ((i2 & 32) == 0) {
            i3 = i;
        } else {
            i3 = 67108864;
        }
        try { return http.call(str, str5, map2, bArr2, str4, i3); } catch (Throwable _ktEx) { throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx); }
    }

    public final Response call(String url, String method, Map<String, String> headers, byte[] body, String contentType, int maxBytes) throws IOException {
        Map<String, String> linkedHashMap;
        HttpURLConnection c;
        int code;
        Throwable th;
        Http http = this;
        Intrinsics.checkNotNullParameter(url, "url");
        Intrinsics.checkNotNullParameter(method, "method");
        Intrinsics.checkNotNullParameter(headers, "headers");
        if (http.isRoblox(url)) {
            linkedHashMap = headers;
        } else {
            LinkedHashMap linkedHashMap2 = new LinkedHashMap();
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                String key = entry.getKey();
                Set<String> set = authHeaders;
                String lowerCase = key.toLowerCase(Locale.ROOT);
                Intrinsics.checkNotNullExpressionValue(lowerCase, "toLowerCase(...)");
                if (!set.contains(lowerCase)) {
                    linkedHashMap2.put(entry.getKey(), entry.getValue());
                }
            }
            linkedHashMap = linkedHashMap2;
        }
        byte[] bArr = body;
        String m = method;
        int redirects = 0;
        String u = url;
        while (true) {
            URLConnection uRLConnectionOpenConnection = new URL(u).openConnection();
            Intrinsics.checkNotNull(uRLConnectionOpenConnection, "null cannot be cast to non-null type java.net.HttpURLConnection");
            c = (HttpURLConnection) uRLConnectionOpenConnection;
            c.setUseCaches(false);
            c.setInstanceFollowRedirects(false);
            c.setConnectTimeout(15000);
            c.setReadTimeout(bArr != null ? 120000 : 30000);
            c.setRequestMethod(m);
            c.setRequestProperty("User-Agent", UA);
            c.setRequestProperty("Accept-Encoding", "gzip");
            for (Map.Entry<String, String> entry2 : linkedHashMap.entrySet()) {
                String k = entry2.getKey();
                String v = entry2.getValue();
                c.setRequestProperty(k, v);
            }
            if (bArr != null) {
                c.setDoOutput(true);
                c.setRequestProperty("Content-Type", contentType == null ? "application/json" : contentType);
                c.setFixedLengthStreamingMode(bArr.length);
                OutputStream outputStream = c.getOutputStream();
                try {
                    outputStream.write(bArr);
                    Unit unit = Unit.INSTANCE;
                    CloseableKt.closeFinally(outputStream, null);
                } catch (Throwable th2) {
                    try {
                        throw th2;
                    } catch (Throwable th3) {
                        CloseableKt.closeFinally(outputStream, th2);
                        throw th3;
                    }
                }
            } else if (Intrinsics.areEqual(m, "POST")) {
                c.setDoOutput(true);
                c.setFixedLengthStreamingMode(0);
                c.getOutputStream().close();
            }
            code = c.getResponseCode();
            if (!(300 <= code && code < 400) || redirects >= 6) {
                break;
            }
            String loc = c.getHeaderField("Location");
            if (loc == null) {
                throw new HttpException(code, "Redirect without location");
            }
            c.disconnect();
            String string = new URL(new URL(u), loc).toString();
            Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
            u = string;
            if (!http.isRoblox(u)) {
                LinkedHashMap linkedHashMap3 = new LinkedHashMap();
                for (Map.Entry<String, String> entry3 : linkedHashMap.entrySet()) {
                    String key2 = entry3.getKey();
                    Set<String> set2 = authHeaders;
                    String lowerCase2 = key2.toLowerCase(Locale.ROOT);
                    Intrinsics.checkNotNullExpressionValue(lowerCase2, "toLowerCase(...)");
                    if (!set2.contains(lowerCase2)) {
                        linkedHashMap3.put(entry3.getKey(), entry3.getValue());
                    }
                }
                linkedHashMap = linkedHashMap3;
            }
            if (code == 303) {
                m = "GET";
                bArr = null;
            }
            redirects++;
            http = this;
        }
        InputStream stream = code >= 400 ? c.getErrorStream() : c.getInputStream();
        Map<String, List<String>> headerFields = c.getHeaderFields();
        if (headerFields == null) {
            headerFields = MapsKt.emptyMap();
        }
        Map<String, List<String>> map = headerFields;
        if (stream == null) {
            c.disconnect();
            return new Response(code, new byte[0], map);
        }
        InputStream raw = StringsKt.equals("gzip", c.getContentEncoding(), true) ? new GZIPInputStream(stream) : stream;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream inputStream = raw;
        try {
            InputStream inputStream2 = inputStream;
            try {
                byte[] bArr2 = new byte[16384];
                while (true) {
                    int i = inputStream2.read(bArr2);
                    try {
                        if (i < 0) {
                            Unit unit2 = Unit.INSTANCE;
                            CloseableKt.closeFinally(inputStream, null);
                            c.disconnect();
                            byte[] byteArray = out.toByteArray();
                            Intrinsics.checkNotNullExpressionValue(byteArray, "toByteArray(...)");
                            return new Response(code, byteArray, map);
                        }
                        InputStream inputStream3 = inputStream2;
                        out.write(bArr2, 0, i);
                        byte[] bArr3 = bArr2;
                        if (out.size() > maxBytes) {
                            throw new IOException("Response too large");
                        }
                        inputStream2 = inputStream3;
                        bArr2 = bArr3;
                    } catch (Throwable th4) {
                        th = th4;
                        try {
                            throw th;
                        } catch (Throwable th5) {
                            CloseableKt.closeFinally(inputStream, th);
                            throw th5;
                        }
                    }
                }
            } catch (Throwable th6) {
                throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(th6);
            }
        } catch (Throwable th7) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(th7);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ Pair request$default(Http http, String str, String str2, Map map, String str3, int i, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            str2 = "GET";
        }
        String str4 = str2;
        if ((i2 & 4) != 0) {
            map = MapsKt.emptyMap();
        }
        Map map2 = map;
        if ((i2 & 8) != 0) {
            str3 = null;
        }
        String str5 = str3;
        if ((i2 & 16) != 0) {
            i = 67108864;
        }
        try { return http.request(str, str4, map2, str5, i); } catch (Throwable _ktEx) { throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx); }
    }

    public final Pair<Integer, byte[]> request(String url, String method, Map<String, String> headers, String body, int maxBytes) throws IOException {
        byte[] bArr;
        Intrinsics.checkNotNullParameter(url, "url");
        Intrinsics.checkNotNullParameter(method, "method");
        Intrinsics.checkNotNullParameter(headers, "headers");
        if (body != null) {
            byte[] bytes = body.getBytes(Charsets.UTF_8);
            Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
            bArr = bytes;
        } else {
            bArr = null;
        }
        Response r = call(url, method, headers, bArr, body != null ? "application/json" : null, maxBytes);
        return TuplesKt.to(Integer.valueOf(r.getCode()), r.getBody());
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ byte[] get$default(Http http, String str, Map map, int i, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            map = MapsKt.emptyMap();
        }
        if ((i2 & 4) != 0) {
            i = 67108864;
        }
        try { return http.get(str, map, i); } catch (Throwable _ktEx) { throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx); }
    }

    public final byte[] get(String url, Map<String, String> headers, int maxBytes) throws HttpException {
        Intrinsics.checkNotNullParameter(url, "url");
        Intrinsics.checkNotNullParameter(headers, "headers");
        Pair pairRequest$default = request$default(this, url, null, headers, null, maxBytes, 10, null);
        int code = ((Number) pairRequest$default.component1()).intValue();
        byte[] bytes = (byte[]) pairRequest$default.component2();
        boolean z = false;
        if (200 <= code && code < 300) {
            z = true;
        }
        if (!z) {
            throw new HttpException(code, describe(code, bytes));
        }
        return bytes;
    }

    public final String describe(int code, byte[] body) {
        String reason;
        String msg;
        JSONObject jSONObjectOptJSONObject;
        String strOptString;
        Intrinsics.checkNotNullParameter(body, "body");
        String text = StringsKt.trim((CharSequence) new String(body, Charsets.UTF_8)).toString();
        if (code == 400) {
            reason = "bad request";
        } else if (code == 401) {
            reason = "not signed in / not authorized";
        } else if (code == 403) {
            reason = "permission denied";
        } else if (code == 404) {
            reason = "not found";
        } else if (code == 409) {
            reason = "conflict";
        } else if (code == 413) {
            reason = "file too large";
        } else if (code == 429) {
            reason = "too many requests, try again in a minute";
        } else {
            reason = 500 <= code && code < 600 ? "Roblox server error" : "";
        }
        if (StringsKt.startsWith(text, "<", false)) {
            msg = "";
        } else if (StringsKt.startsWith(text, "{", false)) {
            try {
                JSONObject j = new JSONObject(text);
                String strOptString2 = j.optString("message");
                if (strOptString2.length() == 0) {
                    JSONArray jSONArrayOptJSONArray = j.optJSONArray("errors");
                    strOptString2 = (jSONArrayOptJSONArray == null || (jSONObjectOptJSONObject = jSONArrayOptJSONArray.optJSONObject(0)) == null || (strOptString = jSONObjectOptJSONObject.optString("message")) == null) ? "" : strOptString;
                }
                String strOptString3 = strOptString2;
                if (strOptString3.length() == 0) {
                    strOptString3 = j.optString("error");
                }
                msg = strOptString3;
            } catch (Exception e) {
                msg = StringsKt.take(text, 160);
            }
        } else {
            msg = StringsKt.take(text, 160);
        }
        String str = reason.length() > 0 ? " (" + reason + ")" : "";
        Intrinsics.checkNotNull(msg);
        return "HTTP " + code + str + (msg.length() > 0 ? ": " + msg : "");
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ JSONObject json$default(Http http, String str, Map map, int i, Object obj) {
        if ((i & 2) != 0) {
            map = MapsKt.emptyMap();
        }
        return http.json(str, map);
    }

    public final JSONObject json(String url, Map<String, String> headers) {
        try {
        Intrinsics.checkNotNullParameter(url, "url");
        Intrinsics.checkNotNullParameter(headers, "headers");
        return new JSONObject(new String(get$default(this, url, headers, 0, 4, null), Charsets.UTF_8));
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }
}

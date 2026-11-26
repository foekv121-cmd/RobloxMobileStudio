package com.goydevv.studrblx.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Minimal client for Google's Gemini API (generateContent).
 *
 * The user's own API key is sent only to generativelanguage.googleapis.com, in the x-goog-api-key header
 * (never in the URL). It is not logged and is not reachable from plugins.
 */
public final class GeminiClient {
    public static final String DEFAULT_MODEL = "gemini-2.5-flash";
    /** Tried in order when no model is chosen: if Google retires one, the next is used automatically. */
    public static final String[] FALLBACKS = {DEFAULT_MODEL, "gemini-flash-latest", "gemini-2.0-flash"};
    private static final String HOST = "https://generativelanguage.googleapis.com/v1beta/models/";
    private static final Pattern MODEL_OK = Pattern.compile("[A-Za-z0-9._-]{1,60}");
    private static final int MAX_BYTES = 2 * 1024 * 1024;

    /** An error with a message that is safe and helpful to show to the user. */
    public static final class GeminiException extends Exception {
        public final int code;

        public GeminiException(int code, String message) {
            super(message);
            this.code = code;
        }

        /** True for errors another model may not have: model missing, free quota used up, or Google overloaded. */
        public boolean tryNextModel() {
            return code == 404 || code == 429 || code >= 500;
        }
    }

    private GeminiClient() {
    }

    public static boolean validModel(String model) {
        return model != null && MODEL_OK.matcher(model).matches();
    }

    /** Sends the conversation and returns the model's text reply. Blocks, so call it off the main thread. */
    public static String generate(String apiKey, String model, String system, JSONArray contents) throws GeminiException {
        if (apiKey == null || apiKey.trim().length() < 10) {
            throw new GeminiException(0, "Add your Gemini API key first (the gear button).");
        }
        if (!validModel(model)) {
            throw new GeminiException(0, "The model name looks wrong. Use something like " + DEFAULT_MODEL + ".");
        }
        HttpURLConnection c = null;
        try {
            JSONObject body = new JSONObject();
            body.put("systemInstruction", new JSONObject().put("parts", new JSONArray().put(new JSONObject().put("text", system))));
            body.put("contents", contents);
            body.put("generationConfig", new JSONObject().put("temperature", 0.4).put("maxOutputTokens", 8192));
            byte[] payload = body.toString().getBytes(StandardCharsets.UTF_8);

            c = (HttpURLConnection) new URL(HOST + model + ":generateContent").openConnection();
            c.setInstanceFollowRedirects(false);
            c.setUseCaches(false);
            c.setConnectTimeout(15000);
            c.setReadTimeout(60000);
            c.setRequestMethod("POST");
            c.setRequestProperty("Content-Type", "application/json");
            c.setRequestProperty("x-goog-api-key", apiKey.trim());
            c.setDoOutput(true);
            c.setFixedLengthStreamingMode(payload.length);
            try (OutputStream out = c.getOutputStream()) {
                out.write(payload);
            }
            int code = c.getResponseCode();
            InputStream in = code >= 400 ? c.getErrorStream() : c.getInputStream();
            String text = in == null ? "" : read(in);
            if (code >= 400) {
                throw new GeminiException(code, describe(code, text));
            }
            return parse(text);
        } catch (GeminiException e) {
            throw e;
        } catch (java.net.UnknownHostException | java.net.SocketTimeoutException | java.net.ConnectException e) {
            throw new GeminiException(0, "Couldn't reach Gemini. Check your internet connection and try again.");
        } catch (Exception e) {
            throw new GeminiException(0, "Gemini request failed: " + e.getClass().getSimpleName());
        } finally {
            if (c != null) {
                c.disconnect();
            }
        }
    }

    /**
     * Checks the connection step by step and returns a readable report: DNS, reaching Google, and whether the key works.
     * Blocks, so call it off the main thread. The key is never included in the report.
     */
    public static String diagnose(String apiKey) {
        StringBuilder sb = new StringBuilder();
        long t0 = System.currentTimeMillis();
        try {
            java.net.InetAddress.getByName("generativelanguage.googleapis.com");
            sb.append("1. Finding Google: OK (").append(System.currentTimeMillis() - t0).append(" ms)\n");
        } catch (Exception e) {
            return sb.append("1. Finding Google: FAILED (").append(e.getClass().getSimpleName())
                    .append("). The phone can't look up Google. Check your internet, VPN or Private DNS.").toString();
        }
        HttpURLConnection c = null;
        try {
            t0 = System.currentTimeMillis();
            c = (HttpURLConnection) new URL("https://generativelanguage.googleapis.com/v1beta/models?pageSize=1").openConnection();
            c.setInstanceFollowRedirects(false);
            c.setConnectTimeout(10000);
            c.setReadTimeout(15000);
            c.setRequestProperty("x-goog-api-key", apiKey.trim());
            int code = c.getResponseCode();
            InputStream in = code >= 400 ? c.getErrorStream() : c.getInputStream();
            String body = in == null ? "" : read(in);
            sb.append("2. Reaching Google: OK, answered ").append(code).append(" (").append(System.currentTimeMillis() - t0).append(" ms)\n");
            sb.append("3. Your key: ").append(code == 200 ? "works." : describe(code, body));
        } catch (Exception e) {
            sb.append("2. Reaching Google: FAILED (").append(e.getClass().getSimpleName())
                    .append("). The phone found Google but couldn't connect. Check your internet or VPN.");
        } finally {
            if (c != null) {
                c.disconnect();
            }
        }
        return sb.toString();
    }

    private static String read(InputStream in) throws IOException {
        try (InputStream is = in) {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            byte[] buf = new byte[8192];
            int n;
            while ((n = is.read(buf)) > 0) {
                bo.write(buf, 0, n);
                if (bo.size() > MAX_BYTES) {
                    throw new IOException("response too large");
                }
            }
            return new String(bo.toByteArray(), StandardCharsets.UTF_8);
        }
    }

    private static String describe(int code, String body) {
        String api = "";
        try {
            JSONObject e = new JSONObject(body).optJSONObject("error");
            if (e != null) {
                api = e.optString("message", "");
            }
        } catch (Exception ignored) {
        }
        String lower = api.toLowerCase(java.util.Locale.ROOT);
        if (code == 400 && (lower.contains("api key") || lower.contains("api_key"))) {
            return "Google says the API key isn't valid. Check it in settings (the gear button).";
        }
        if (code == 400 || code == 404) {
            return code == 404
                    ? "Gemini couldn't find that model. In settings, clear the model box so the app picks one itself."
                    : "Gemini rejected the request" + (api.length() > 0 ? ": " + clip(api) : ".");
        }
        if (code == 401 || code == 403) {
            return "Gemini refused this key (" + code + "). Make sure the key is active and the Gemini API is allowed for it.";
        }
        if (code == 429) {
            return "Free limit reached. Wait a minute and try again, or try a lighter model in settings.";
        }
        if (code >= 500) {
            return "Gemini is having trouble (error " + code + ")" + (api.length() > 0 ? ": " + clip(api) : ".")
                    + " This is on Google's side and is usually temporary.";
        }
        return "Gemini error " + code + (api.length() > 0 ? ": " + clip(api) : ".");
    }

    private static String clip(String s) {
        return s.length() > 200 ? s.substring(0, 200) + "\u2026" : s;
    }

    private static String parse(String text) throws GeminiException {
        try {
            JSONObject o = new JSONObject(text);
            JSONArray cands = o.optJSONArray("candidates");
            if (cands == null || cands.length() == 0) {
                JSONObject fb = o.optJSONObject("promptFeedback");
                String why = fb == null ? "" : fb.optString("blockReason", "");
                throw new GeminiException(0, why.length() > 0
                        ? "Gemini blocked that request (" + why + "). Try rewording it."
                        : "Gemini returned no answer.");
            }
            JSONObject cand = cands.getJSONObject(0);
            StringBuilder sb = new StringBuilder();
            JSONObject content = cand.optJSONObject("content");
            JSONArray parts = content == null ? null : content.optJSONArray("parts");
            if (parts != null) {
                for (int i = 0; i < parts.length(); i++) {
                    sb.append(parts.getJSONObject(i).optString("text", ""));
                }
            }
            String out = sb.toString().trim();
            if (out.length() == 0) {
                throw new GeminiException(0, "Gemini returned no text (reason: " + cand.optString("finishReason", "unknown") + ").");
            }
            return out;
        } catch (GeminiException e) {
            throw e;
        } catch (Exception e) {
            throw new GeminiException(0, "Couldn't read Gemini's answer.");
        }
    }
}

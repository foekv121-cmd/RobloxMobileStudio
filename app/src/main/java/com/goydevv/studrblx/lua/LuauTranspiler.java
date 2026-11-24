package com.goydevv.studrblx.lua;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.SetsKt;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: LuauTranspiler.kt */
/* JADX INFO: loaded from: classes4.dex */
public final class LuauTranspiler {
    private static volatile String lastFailure;
    public static final LuauTranspiler INSTANCE = new LuauTranspiler();
    private static final Set<String> keywords = SetsKt.setOf(new String[]{"and", "break", "do", "else", "elseif", "end", "false", "for", "function", "goto", "if", "in", "local", "nil", "not", "or", "repeat", "return", "then", "true", "until", "while"});
    private static final List<String> ops = CollectionsKt.listOf(new String[]{"...", "..=", "//=", "..", "==", "~=", "<=", ">=", "//", "::", "->", "+=", "-=", "*=", "/=", "%=", "^=", "+", "-", "*", "/", "%", "^", "#", "&", "|", "~", "<", ">", "=", "(", ")", "{", "}", "[", "]", ";", ":", ",", ".", "?", "@", "!"});

    /* JADX INFO: compiled from: LuauTranspiler.kt */
    private enum K {
        NAME,
        KEYWORD,
        NUMBER,
        STRING,
        OP,
        EOF;

        private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries(values());

        public static EnumEntries<K> getEntries() {
            return $ENTRIES;
        }
    }

    /* JADX INFO: compiled from: LuauTranspiler.kt */
    private static final class ParseError extends Exception {
    }

    private LuauTranspiler() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: compiled from: LuauTranspiler.kt */
    static final class Tok {
        private final K k;
        private String out;
        private String prefix;
        private String suffix;
        private final String text;
        private final String trivia;

        public Tok(K k, String text, String trivia) {
            Intrinsics.checkNotNullParameter(k, "k");
            Intrinsics.checkNotNullParameter(text, "text");
            Intrinsics.checkNotNullParameter(trivia, "trivia");
            this.k = k;
            this.text = text;
            this.trivia = trivia;
            this.out = this.text;
            this.prefix = "";
            this.suffix = "";
        }

        public final K getK() {
            return this.k;
        }

        public final String getText() {
            return this.text;
        }

        public final String getTrivia() {
            return this.trivia;
        }

        public final String getOut() {
            return this.out;
        }

        public final void setOut(String str) {
            Intrinsics.checkNotNullParameter(str, "<set-?>");
            this.out = str;
        }

        public final String getPrefix() {
            return this.prefix;
        }

        public final void setPrefix(String str) {
            Intrinsics.checkNotNullParameter(str, "<set-?>");
            this.prefix = str;
        }

        public final String getSuffix() {
            return this.suffix;
        }

        public final void setSuffix(String str) {
            Intrinsics.checkNotNullParameter(str, "<set-?>");
            this.suffix = str;
        }

        public final boolean isOp(String s) {
            Intrinsics.checkNotNullParameter(s, "s");
            return this.k == K.OP && Intrinsics.areEqual(this.text, s);
        }

        public final boolean isKw(String s) {
            Intrinsics.checkNotNullParameter(s, "s");
            return this.k == K.KEYWORD && Intrinsics.areEqual(this.text, s);
        }

        public final boolean isName(String s) {
            Intrinsics.checkNotNullParameter(s, "s");
            return this.k == K.NAME && Intrinsics.areEqual(this.text, s);
        }
    }

    public final String getLastFailure() {
        return lastFailure;
    }

    public final void setLastFailure(String str) {
        lastFailure = str;
    }

    public static /* synthetic */ String transpile$default(LuauTranspiler luauTranspiler, String str, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        try { return luauTranspiler.transpile(str, z); } catch (Throwable _ktEx) { throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx); }
    }

    public final String transpile(String src, boolean hoistTopLocals) throws Exception {
        String str;
        Exception e;
        Intrinsics.checkNotNullParameter(src, "src");
        Parser parser = null;
        ArrayList<Tok> arrayListLex = null;
        try {
            arrayListLex = lex(src);
            try {
                parser = new Parser(arrayListLex, hoistTopLocals);
                parser.run();
                StringBuilder sb = new StringBuilder(src.length() + 64);
                Iterator<Tok> it = arrayListLex.iterator();
                Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
                while (it.hasNext()) {
                    Tok next = it.next();
                    Intrinsics.checkNotNullExpressionValue(next, "next(...)");
                    Tok t = next;
                    sb.append(t.getTrivia());
                    sb.append(t.getPrefix());
                    sb.append(t.getOut());
                    sb.append(t.getSuffix());
                }
                String string = sb.toString();
                Intrinsics.checkNotNull(string);
                return string;
            } catch (Exception e2) {
                e = e2;
                if (!(e instanceof ParseError) && !(e instanceof IndexOutOfBoundsException)) {
                    throw e;
                }
                ArrayList<Tok> arrayList = arrayListLex;
                int pp = parser != null ? parser.getP() : 0;
                if (arrayList == null) {
                    str = null;
                } else {
                    int i = 0;
                    for (Tok tok : (List<Tok>) CollectionsKt.take(arrayList, pp)) {
                        CharSequence trivia = tok.getTrivia();
                        int i2 = 0;
                        int i3 = 0;
                        while (true) {
                            if (i3 >= trivia.length()) {
                                break;
                            }
                            if (trivia.charAt(i3) == '\n') {
                                i2++;
                            }
                            i3++;
                        }
                        CharSequence text = tok.getText();
                        int i4 = 0;
                        int i5 = 0;
                        while (i5 < text.length()) {
                            CharSequence charSequence = text;
                            if ((text.charAt(i5) == '\n' ? (char) 1 : (char) 0) != 0) {
                                i4++;
                            }
                            i5++;
                            text = charSequence;
                        }
                        i += i2 + i4;
                    }
                    int line = i + 1;
                    List<Tok> listSubList = arrayList.subList(RangesKt.coerceAtLeast(pp - 6, 0), RangesKt.coerceAtMost(pp + 4, arrayList.size()));
                    Intrinsics.checkNotNullExpressionValue(listSubList, "subList(...)");
                    str = "line " + line + " near: " + CollectionsKt.joinToString(listSubList, " ", "", "", -1, "...", new Function1() { // from class: com.goydevv.studrblx.lua.LuauTranspiler$$ExternalSyntheticLambda0
                        @Override // kotlin.jvm.functions.Function1
                        public final Object invoke(Object obj) {
                            return LuauTranspiler.transpile$lambda$3((LuauTranspiler.Tok) obj);
                        }
                    });
                }
                lastFailure = str;
                return src;
            }
        } catch (Exception e3) {
            return src;
        }
    }

    static final CharSequence transpile$lambda$3(Tok it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return it.getText();
    }

    private final String relevel(String text, String prefix) {
        String body0 = StringsKt.removePrefix(text, (CharSequence) prefix);
        if (!StringsKt.startsWith(body0, "[[", false) || !StringsKt.endsWith(body0, "]]", false)) {
            return text;
        }
        String inner = body0.substring(2, body0.length() - 2);
        Intrinsics.checkNotNullExpressionValue(inner, "substring(...)");
        if (!StringsKt.contains((CharSequence) inner, (CharSequence) "[[", false)) {
            return text;
        }
        int lvl = 1;
        while (StringsKt.contains((CharSequence) inner, (CharSequence) ("]" + StringsKt.repeat("=", lvl) + "]"), false)) {
            lvl++;
        }
        String eq = StringsKt.repeat("=", lvl);
        return prefix + "[" + eq + "[" + inner + "]" + eq + "]";
    }

    /* JADX WARN: Code duplicated, block: B:145:0x02c7  */
    /* JADX WARN: Code duplicated, block: B:146:0x02ca  */
    /* JADX WARN: Code duplicated, block: B:148:0x007a A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:32:0x008c  */
    /* JADX WARN: Code duplicated, block: B:66:0x0158  */
    private static String rewriteStringEscapes(String lit) {
        int len = lit.length();
        StringBuilder sb = new StringBuilder(len);
        int i = 0;
        while (i < len) {
            char ch = lit.charAt(i);
            if (ch == '\\' && i + 1 < len) {
                char nxt = lit.charAt(i + 1);
                if (nxt == '\\') {
                    sb.append("\\\\");
                    i += 2;
                } else if (nxt == 'z') {
                    i += 2;
                    while (i < len - 1) {
                        char wc = lit.charAt(i);
                        if (wc == ' ' || wc == '\t' || wc == '\n' || wc == '\r' || wc == '\f' || wc == 0x0B) {
                            i++;
                        } else {
                            break;
                        }
                    }
                } else if (nxt == 'u' && i + 2 < len && lit.charAt(i + 2) == '{') {
                    int close = lit.indexOf('}', i + 3);
                    if (close > i + 3) {
                        String hex = lit.substring(i + 3, close);
                        try {
                            int cp = Integer.parseInt(hex, 16);
                            byte[] utf8Bytes = new String(Character.toChars(cp)).getBytes(kotlin.text.Charsets.UTF_8);
                            for (byte b : utf8Bytes) {
                                sb.append(String.format(java.util.Locale.US, "\\%03d", b & 0xFF));
                            }
                            i = close + 1;
                            continue;
                        } catch (Exception ignored) {
                        }
                    }
                    sb.append(ch).append(nxt);
                    i += 2;
                } else {
                    sb.append(ch).append(nxt);
                    i += 2;
                }
            } else {
                sb.append(ch);
                i++;
            }
        }
        return sb.toString();
    }

    private final ArrayList<Tok> lex(String s) {
        ArrayList<Tok> arrayList = new ArrayList<>();
        int lvl = 0;
        int n = s.length();
        StringBuilder triv = new StringBuilder();
        while (true) {
            int i2 = lvl;
            while (i2 < n) {
                char c2 = s.charAt(i2);
                if (c2 == '\t' || c2 == '\n' || c2 == '\f' || c2 == '\r' || c2 == ' ') {
                    triv.append(c2);
                    i2++;
                } else if (c2 == '-' && i2 + 1 < n && s.charAt(i2 + 1) == '-') {
                    int lvl2 = lex$longBracketLevel(n, s, i2 + 2);
                    int e;
                    if (lvl2 >= 0) {
                        e = lex$skipLong(s, n, i2 + 2, lvl2);
                    } else {
                        int idx = StringsKt.indexOf((CharSequence) s, '\n', i2, false);
                        e = idx < 0 ? n : idx;
                    }
                    String comment = s.substring(i2, e);
                    Intrinsics.checkNotNullExpressionValue(comment, "substring(...)");
                    triv.append(lvl2 == 0 ? relevel(comment, "--") : comment);
                    i2 = e;
                } else {
                    break;
                }
            }
            if (i2 >= n) {
                String trailing = triv.toString();
                Intrinsics.checkNotNullExpressionValue(trailing, "toString(...)");
                arrayList.add(new Tok(K.EOF, "", trailing));
                return arrayList;
            }
            String tr = triv.toString();
            Intrinsics.checkNotNullExpressionValue(tr, "toString(...)");
            triv.setLength(0);
            char c = s.charAt(i2);
            if (Character.isLetter(c) || c == '_') {
                lvl = i2;
                while (lvl < n && (Character.isLetterOrDigit(s.charAt(lvl)) || s.charAt(lvl) == '_')) {
                    lvl++;
                }
                String w = s.substring(i2, lvl);
                Intrinsics.checkNotNullExpressionValue(w, "substring(...)");
                arrayList.add(new Tok(keywords.contains(w) ? K.KEYWORD : K.NAME, w, tr));
            } else if (Character.isDigit(c) || (c == '.' && i2 + 1 < n && Character.isDigit(s.charAt(i2 + 1)))) {
                int j = i2;
                if (c == '0' && i2 + 1 < n && (s.charAt(i2 + 1) == 'x' || s.charAt(i2 + 1) == 'X' || s.charAt(i2 + 1) == 'b' || s.charAt(i2 + 1) == 'B')) {
                    j += 2;
                    while (j < n && (Character.isLetterOrDigit(s.charAt(j)) || s.charAt(j) == '_')) {
                        j++;
                    }
                } else {
                    while (j < n && (Character.isDigit(s.charAt(j)) || s.charAt(j) == '.' || s.charAt(j) == '_')) {
                        j++;
                    }
                    if (j < n && (s.charAt(j) == 'e' || s.charAt(j) == 'E')) {
                        j++;
                        if (j < n && (s.charAt(j) == '+' || s.charAt(j) == '-')) {
                            j++;
                        }
                        while (j < n && (Character.isDigit(s.charAt(j)) || s.charAt(j) == '_')) {
                            j++;
                        }
                    }
                }
                String rawNum = s.substring(i2, j);
                Intrinsics.checkNotNullExpressionValue(rawNum, "substring(...)");
                String num = StringsKt.replace(rawNum, "_", "", false);
                if (StringsKt.startsWith(num, "0b", false) || StringsKt.startsWith(num, "0B", false)) {
                    String binPart = num.substring(2);
                    Intrinsics.checkNotNullExpressionValue(binPart, "substring(...)");
                    Long longOrNull = StringsKt.toLongOrNull(binPart, 2);
                    num = String.valueOf(longOrNull != null ? longOrNull.longValue() : 0L);
                }
                Tok t = new Tok(K.NUMBER, rawNum, tr);
                t.setOut(num);
                arrayList.add(t);
                lvl = j;
            } else if (c == '"' || c == '\'') {
                int j2 = i2 + 1;
                while (j2 < n && s.charAt(j2) != c) {
                    if (s.charAt(j2) == '\\') {
                        if (j2 + 1 < n && s.charAt(j2 + 1) == 'z') {
                            j2 += 2;
                            while (j2 < n) {
                                char wc = s.charAt(j2);
                                if (wc == ' ' || wc == '\t' || wc == '\n' || wc == '\r' || wc == '\f' || wc == 0x0B) {
                                    j2++;
                                } else {
                                    break;
                                }
                            }
                        } else {
                            j2 += 2;
                        }
                    } else if (s.charAt(j2) == '\n') {
                        break;
                    } else {
                        j2++;
                    }
                }
                lvl = Math.min(j2 + 1, n);
                String strLit = s.substring(i2, lvl);
                Intrinsics.checkNotNullExpressionValue(strLit, "substring(...)");
                Tok strTok = new Tok(K.STRING, strLit, tr);
                if (strLit.indexOf("\\u{") >= 0 || strLit.indexOf("\\z") >= 0) {
                    strTok.setOut(rewriteStringEscapes(strLit));
                }
                arrayList.add(strTok);
            } else if (c == '[' && lex$longBracketLevel(n, s, i2) >= 0) {
                int lvl3 = lex$longBracketLevel(n, s, i2);
                int e2 = lex$skipLong(s, n, i2, lvl3);
                String longStr = s.substring(i2, e2);
                Intrinsics.checkNotNullExpressionValue(longStr, "substring(...)");
                Tok t2 = new Tok(K.STRING, longStr, tr);
                if (lvl3 == 0) {
                    t2.setOut(relevel(t2.getText(), ""));
                }
                arrayList.add(t2);
                lvl = e2;
            } else if (c == '`') {
                Pair<Integer, String> pairInterpolated = interpolated(s, i2);
                int e3 = pairInterpolated.component1().intValue();
                String converted = pairInterpolated.component2();
                String interpStr = s.substring(i2, e3);
                Intrinsics.checkNotNullExpressionValue(interpStr, "substring(...)");
                Tok t3 = new Tok(K.STRING, interpStr, tr);
                t3.setOut(converted);
                arrayList.add(t3);
                lvl = e3;
            } else {
                String op = null;
                for (Object obj2 : ops) {
                    if (StringsKt.startsWith(s, (String) obj2, i2, false)) {
                        op = (String) obj2;
                        break;
                    }
                }
                if (op == null) {
                    op = String.valueOf(c);
                }
                arrayList.add(new Tok(K.OP, op, tr));
                lvl = i2 + Math.max(op.length(), 1);
            }
        }
    }

    private static final int lex$longBracketLevel(int n, String $s, int at) {
        if (at >= n || $s.charAt(at) != '[') {
            return -1;
        }
        int j = at + 1;
        int lvl = 0;
        while (j < n && $s.charAt(j) == '=') {
            lvl++;
            j++;
        }
        if (j >= n || $s.charAt(j) != '[') {
            return -1;
        }
        return lvl;
    }

    private static final int lex$skipLong(String $s, int n, int at, int lvl) {
        String close = "]" + StringsKt.repeat("=", lvl) + "]";
        int e = StringsKt.indexOf((CharSequence) $s, close, at + lvl + 2, false);
        return e < 0 ? n : close.length() + e;
    }

    private final Pair<Integer, String> interpolated(String s, int start) {
        ArrayList parts = new ArrayList();
        StringBuilder lit = new StringBuilder();
        int i = start + 1;
        while (i < s.length() && s.charAt(i) != '`') {
            char c = s.charAt(i);
            if (c == '\\' && i + 1 < s.length()) {
                char nx = s.charAt(i + 1);
                switch (nx) {
                    case '\n':
                        lit.append("\\n");
                        break;
                    case '`':
                    case '{':
                    case '}':
                        lit.append(nx);
                        break;
                    default:
                        lit.append('\\');
                        lit.append(nx);
                        break;
                }
                i += 2;
            } else if (c == '{') {
                interpolated$flush(lit, parts);
                int depth = 1;
                int j = i + 1;
                while (j < s.length() && depth > 0) {
                    switch (s.charAt(j)) {
                        case '\"':
                        case '\'':
                            char q = s.charAt(j);
                            while (true) {
                                j++;
                                if (j >= s.length() || s.charAt(j) == q) {
                                    break;
                                } else if (s.charAt(j) == '\\') {
                                    j++;
                                }
                            }
                            break;
                        case '{':
                            depth++;
                            break;
                        case '}':
                            depth--;
                            break;
                    }
                    if (depth > 0) {
                        j++;
                    }
                }
                String expr = s.substring(i + 1, Math.min(j, s.length()));
                Intrinsics.checkNotNullExpressionValue(expr, "substring(...)");
                parts.add("tostring(" + transpileExpr(expr) + ")");
                i = j + 1;
            } else if (c == '\"') {
                lit.append("\\\"");
                i++;
            } else if (c == '\n') {
                lit.append("\\n");
                i++;
            } else {
                lit.append(c);
                i++;
            }
        }
        interpolated$flush(lit, parts);
        int end = Math.min(i + 1, s.length());
        String body = parts.isEmpty() ? "\"\"" : CollectionsKt.joinToString(parts, " .. ", "", "", -1, "...", null);
        return TuplesKt.to(Integer.valueOf(end), "(" + body + ")");
    }

    private static final void interpolated$flush(StringBuilder lit, ArrayList<String> arrayList) {
        if (lit.length() > 0) {
            arrayList.add("\"" + ((Object) lit) + "\"");
            lit.setLength(0);
        }
    }

    private final String transpileExpr(String e) {
        String r = transpile$default(this, "return " + e, false, 2, null);
        if (!StringsKt.startsWith(r, "return ", false)) {
            return e;
        }
        String strSubstring = r.substring(7);
        Intrinsics.checkNotNullExpressionValue(strSubstring, "substring(...)");
        return strSubstring;
    }

    /* JADX INFO: compiled from: LuauTranspiler.kt */
    private static final class LoopCtx {
        private final int id;
        private boolean usesContinue;

        public LoopCtx(int id) {
            this.id = id;
        }

        public final int getId() {
            return this.id;
        }

        public final boolean getUsesContinue() {
            return this.usesContinue;
        }

        public final void setUsesContinue(boolean z) {
            this.usesContinue = z;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: compiled from: LuauTranspiler.kt */
    static final class Parser {
        private int depth;
        private final boolean hoist;
        private int labelCounter;
        private final ArrayList<LoopCtx> loops;
        private int p;
        private final List<Tok> t;

        public Parser(List<Tok> t, boolean hoist) {
            Intrinsics.checkNotNullParameter(t, "t");
            this.t = t;
            this.hoist = hoist;
            this.loops = new ArrayList<>();
        }

        public /* synthetic */ Parser(List list, boolean z, int i, DefaultConstructorMarker defaultConstructorMarker) {
            this(list, (i & 2) != 0 ? false : z);
        }

        public final boolean getHoist() {
            return this.hoist;
        }

        public final List<Tok> getT() {
            return this.t;
        }

        public final int getP() {
            return this.p;
        }

        public final void setP(int i) {
            this.p = i;
        }

        public final int getLabelCounter() {
            return this.labelCounter;
        }

        public final void setLabelCounter(int i) {
            this.labelCounter = i;
        }

        public final int getDepth() {
            return this.depth;
        }

        public final void setDepth(int i) {
            this.depth = i;
        }

        public final ArrayList<LoopCtx> getLoops() {
            return this.loops;
        }

        public static /* synthetic */ Tok peek$default(Parser parser, int i, int i2, Object obj) {
            if ((i2 & 1) != 0) {
                i = 0;
            }
            return parser.peek(i);
        }

        public final Tok peek(int o) {
            return this.t.get(Math.min(this.p + o, this.t.size() - 1));
        }

        public final Tok next() {
            Tok tok = this.t.get(this.p);
            if (this.p < this.t.size() - 1) {
                this.p++;
            }
            return tok;
        }

        public final boolean atEof() {
            return peek$default(this, 0, 1, null).getK() == K.EOF;
        }

        public final Tok expectOp(String s) throws ParseError {
            Intrinsics.checkNotNullParameter(s, "s");
            if (peek$default(this, 0, 1, null).isOp(s)) {
                return next();
            }
            throw new ParseError();
        }

        public final Tok expectKw(String s) throws ParseError {
            Intrinsics.checkNotNullParameter(s, "s");
            if (peek$default(this, 0, 1, null).isKw(s)) {
                return next();
            }
            throw new ParseError();
        }

        public final Tok expectName() throws ParseError {
            if (peek$default(this, 0, 1, null).getK() == K.NAME) {
                return next();
            }
            throw new ParseError();
        }

        public final void drop(int from, int to) throws IOException {
            for (int i = from; i < to; i++) {
                Tok tk = this.t.get(i);
                CharSequence text = tk.getText();
                Appendable sb = new StringBuilder();
                int length = text.length();
                for (int i2 = 0; i2 < length; i2++) {
                    char cCharAt = text.charAt(i2);
                    if (cCharAt == '\n') {
                        sb.append(cCharAt);
                    }
                }
                tk.setOut(((StringBuilder) sb).toString());
                tk.setPrefix("");
                tk.setSuffix("");
            }
        }

        public final void run() throws IOException, ParseError {
            block();
            if (!atEof()) {
                throw new ParseError();
            }
        }

        public final boolean blockEnd() {
            Tok k = peek$default(this, 0, 1, null);
            return k.getK() == K.EOF || k.isKw("end") || k.isKw("else") || k.isKw("elseif") || k.isKw("until");
        }

        public final void block() throws IOException, ParseError {
            this.depth++;
            while (!blockEnd()) {
                if (peek$default(this, 0, 1, null).isKw("return")) {
                    next();
                    if (!blockEnd() && !peek$default(this, 0, 1, null).isOp(";")) {
                        exprList();
                    }
                    if (peek$default(this, 0, 1, null).isOp(";")) {
                        next();
                    }
                } else {
                    statement();
                }
            }
            this.depth--;
        }

        public final Tok loopBody(String endKw) throws IOException, ParseError {
            Intrinsics.checkNotNullParameter(endKw, "endKw");
            this.labelCounter++;
            LoopCtx ctx = new LoopCtx(this.labelCounter);
            this.loops.add(ctx);
            int bodyStart = this.p;
            block();
            this.loops.remove(this.loops.size() - 1);
            Tok e = expectKw(endKw);
            if (ctx.getUsesContinue()) {
                if (Intrinsics.areEqual(endKw, "end")) {
                    this.t.get(bodyStart).setPrefix("do " + this.t.get(bodyStart).getPrefix());
                    e.setPrefix(e.getPrefix() + " end ::__continue" + ctx.getId() + ":: ");
                } else {
                    e.setPrefix("::__continue" + ctx.getId() + ":: ");
                }
            }
            return e;
        }

        public final void statement() throws IOException, ParseError {
            while (peek$default(this, 0, 1, null).isOp("@")) {
                int attrStart = this.p;
                next();
                if (peek$default(this, 0, 1, null).isOp("[")) {
                    skipBalanced("[", "]");
                } else if (peek$default(this, 0, 1, null).getK() == K.NAME || peek$default(this, 0, 1, null).getK() == K.KEYWORD) {
                    next();
                    if (peek$default(this, 0, 1, null).isOp("(")) {
                        skipBalanced("(", ")");
                    }
                }
                drop(attrStart, this.p);
            }
            if (blockEnd()) {
                return;
            }
            Tok k = peek$default(this, 0, 1, null);
            if (!k.isOp(";")) {
                if (k.isKw("if")) {
                    next();
                    expr();
                    expectKw("then");
                    block();
                    while (peek$default(this, 0, 1, null).isKw("elseif")) {
                        next();
                        expr();
                        expectKw("then");
                        block();
                    }
                    if (peek$default(this, 0, 1, null).isKw("else")) {
                        next();
                        block();
                    }
                    expectKw("end");
                    return;
                }
                if (!k.isKw("while")) {
                    if (!k.isKw("do")) {
                        if (k.isKw("for")) {
                            next();
                            nameWithType();
                            if (peek$default(this, 0, 1, null).isOp("=")) {
                                next();
                                exprList();
                            } else {
                                while (peek$default(this, 0, 1, null).isOp(",")) {
                                    next();
                                    nameWithType();
                                }
                                expectKw("in");
                                int first = this.p;
                                expr();
                                if (peek$default(this, 0, 1, null).isOp(",")) {
                                    while (peek$default(this, 0, 1, null).isOp(",")) {
                                        next();
                                        expr();
                                    }
                                } else {
                                    this.t.get(first).setPrefix("__iter(" + this.t.get(first).getPrefix());
                                    Tok tok = this.t.get(this.p - 1);
                                    tok.setSuffix(tok.getSuffix() + ")");
                                }
                            }
                            expectKw("do");
                            loopBody("end");
                            return;
                        }
                        if (k.isKw("repeat")) {
                            next();
                            loopBody("until");
                            expr();
                            Unit unit = Unit.INSTANCE;
                            return;
                        }
                        if (k.isKw("function")) {
                            next();
                            expectName();
                            while (true) {
                                if (peek$default(this, 0, 1, null).isOp(".") || peek$default(this, 0, 1, null).isOp(":")) {
                                    next();
                                    expectName();
                                } else {
                                    funcBody();
                                    Unit unit2 = Unit.INSTANCE;
                                    return;
                                }
                            }
                        } else {
                            if (k.isKw("local")) {
                                Tok localTok = next();
                                boolean hoistThis = this.hoist && this.depth == 1;
                                if (hoistThis) {
                                    localTok.setOut("");
                                }
                                if (peek$default(this, 0, 1, null).isKw("function")) {
                                    next();
                                    expectName();
                                    funcBody();
                                } else {
                                    localName();
                                    while (peek$default(this, 0, 1, null).isOp(",")) {
                                        next();
                                        localName();
                                    }
                                    if (peek$default(this, 0, 1, null).isOp("=")) {
                                        next();
                                        exprList();
                                    } else if (hoistThis) {
                                        Tok tok2 = this.t.get(this.p - 1);
                                        tok2.setSuffix(tok2.getSuffix() + " = nil");
                                    }
                                }
                                Unit unit3 = Unit.INSTANCE;
                                return;
                            }
                            if (!k.isKw("break")) {
                                if (!k.isKw("goto")) {
                                    if (k.isOp("::")) {
                                        next();
                                        expectName();
                                        expectOp("::");
                                        return;
                                    }
                                    if (k.isName("continue") && isStatementWord()) {
                                        Tok tok3 = next();
                                        LoopCtx ctx = (LoopCtx) CollectionsKt.lastOrNull((List) this.loops);
                                        if (ctx == null) {
                                            throw new ParseError();
                                        }
                                        ctx.setUsesContinue(true);
                                        tok3.setOut("goto __continue" + ctx.getId());
                                        Unit unit4 = Unit.INSTANCE;
                                        return;
                                    }
                                    if (!k.isName("type") || peek(1).getK() != K.NAME || (!peek(2).isOp("=") && !peek(2).isOp("<"))) {
                                        if (!k.isName("export") || !peek(1).isName("type")) {
                                            exprStatement();
                                            Unit unit5 = Unit.INSTANCE;
                                            return;
                                        } else {
                                            typeDecl(this.p);
                                            Unit unit6 = Unit.INSTANCE;
                                            return;
                                        }
                                    }
                                    typeDecl(this.p);
                                    Unit unit7 = Unit.INSTANCE;
                                    return;
                                }
                                next();
                                expectName();
                                return;
                            }
                            next();
                        }
                    } else {
                        next();
                        block();
                        expectKw("end");
                    }
                } else {
                    next();
                    expr();
                    expectKw("do");
                    loopBody("end");
                }
            } else {
                next();
            }
        }

        public final boolean isStatementWord() {
            Tok n = peek(1);
            return (n.isOp("(") || n.isOp("=") || n.isOp(".") || n.isOp("[") || n.isOp(":") || n.isOp(",") || n.getK() == K.STRING || n.isOp("{") || n.isOp("+=") || n.isOp("-=")) ? false : true;
        }

        public final void typeDecl(int start) throws IOException, ParseError {
            if (peek$default(this, 0, 1, null).isName("export")) {
                next();
            }
            next();
            expectName();
            if (peek$default(this, 0, 1, null).isOp("<")) {
                skipAngle();
            }
            expectOp("=");
            type();
            drop(start, this.p);
        }

        public final void nameWithType() throws IOException, ParseError {
            expectName();
            if (peek$default(this, 0, 1, null).isOp(":")) {
                int s = this.p;
                next();
                type();
                drop(s, this.p);
            }
        }

        public final void localName() throws IOException, ParseError {
            expectName();
            if (peek$default(this, 0, 1, null).isOp("<")) {
                int s = this.p;
                skipAngle();
                drop(s, this.p);
            }
            if (peek$default(this, 0, 1, null).isOp(":")) {
                int s2 = this.p;
                next();
                type();
                drop(s2, this.p);
            }
        }

        public final void funcBody() throws IOException, ParseError {
            if (peek$default(this, 0, 1, null).isOp("<")) {
                int s = this.p;
                skipAngle();
                drop(s, this.p);
            }
            expectOp("(");
            while (!peek$default(this, 0, 1, null).isOp(")")) {
                if (peek$default(this, 0, 1, null).isOp("...")) {
                    next();
                    if (peek$default(this, 0, 1, null).isOp(":")) {
                        int s2 = this.p;
                        next();
                        type();
                        drop(s2, this.p);
                    }
                } else {
                    nameWithType();
                }
                if (!peek$default(this, 0, 1, null).isOp(",")) {
                    break;
                } else {
                    next();
                }
            }
            expectOp(")");
            if (peek$default(this, 0, 1, null).isOp(":")) {
                int s3 = this.p;
                next();
                returnType();
                drop(s3, this.p);
            }
            this.loops.add(null);
            block();
            this.loops.remove(this.loops.size() - 1);
            expectKw("end");
        }

        public final void skipAngle() throws ParseError {
            expectOp("<");
            int depth = 1;
            while (depth > 0 && !atEof()) {
                Tok k = next();
                if (k.isOp("<")) {
                    depth++;
                } else if (k.isOp(">")) {
                    depth--;
                } else if (k.isOp(">=")) {
                    depth--;
                    if (depth == 0) {
                        int idx = this.p - 1;
                        Tok gt = new Tok(K.OP, ">", k.getTrivia());
                        Tok eq = new Tok(K.OP, "=", "");
                        this.t.set(idx, gt);
                        this.t.add(idx + 1, eq);
                    }
                }
            }
        }

        public final void skipBalanced(String open, String close) throws ParseError {
            Intrinsics.checkNotNullParameter(open, "open");
            Intrinsics.checkNotNullParameter(close, "close");
            expectOp(open);
            int depth = 1;
            while (depth > 0 && !atEof()) {
                Tok k = next();
                if (k.isOp(open)) {
                    depth++;
                } else if (k.isOp(close)) {
                    depth--;
                }
            }
        }

        public final void returnType() throws IOException, ParseError {
            if (peek$default(this, 0, 1, null).isOp("(")) {
                skipBalanced("(", ")");
                if (peek$default(this, 0, 1, null).isOp("->")) {
                    next();
                    type();
                }
                while (peek$default(this, 0, 1, null).isOp("?")) {
                    next();
                }
                while (true) {
                    if (!peek$default(this, 0, 1, null).isOp("|") && !peek$default(this, 0, 1, null).isOp("&")) {
                        return;
                    }
                    next();
                    simpleType();
                }
            } else {
                type();
            }
        }

        public final void type() throws IOException, ParseError {
            if (peek$default(this, 0, 1, null).isOp("|") || peek$default(this, 0, 1, null).isOp("&")) {
                next();
            }
            simpleType();
            while (true) {
                if (peek$default(this, 0, 1, null).isOp("|") || peek$default(this, 0, 1, null).isOp("&")) {
                    next();
                    simpleType();
                } else {
                    return;
                }
            }
        }

        public final void simpleType() throws IOException, ParseError {
            Tok k = peek$default(this, 0, 1, null);
            if (k.isOp("<")) {
                skipAngle();
                simpleType();
            } else if (k.isOp("(")) {
                skipBalanced("(", ")");
                if (peek$default(this, 0, 1, null).isOp("->")) {
                    next();
                    type();
                }
            } else if (k.isOp("{")) {
                skipBalanced("{", "}");
            } else if (k.isName("typeof")) {
                next();
                skipBalanced("(", ")");
            } else if (k.isOp("...")) {
                next();
                simpleType();
            } else if (k.getK() == K.NAME || k.isKw("nil") || k.isKw("true") || k.isKw("false") || k.getK() == K.STRING) {
                next();
                while (peek$default(this, 0, 1, null).isOp(".")) {
                    next();
                    expectName();
                }
                if (peek$default(this, 0, 1, null).isOp("<")) {
                    skipAngle();
                }
                if (peek$default(this, 0, 1, null).isOp("...")) {
                    next();
                }
            } else {
                throw new ParseError();
            }
            while (peek$default(this, 0, 1, null).isOp("?")) {
                next();
            }
        }

        public final void exprList() throws IOException, ParseError {
            while (true) {
                expr();
                if (!peek$default(this, 0, 1, null).isOp(",")) {
                    return;
                } else {
                    next();
                }
            }
        }

        public final void exprStatement() throws IOException, ParseError {
            int start = this.p;
            suffixedExpr();
            Tok k = peek$default(this, 0, 1, null);
            if (k.isOp("=") || k.isOp(",")) {
                while (peek$default(this, 0, 1, null).isOp(",")) {
                    next();
                    suffixedExpr();
                }
                expectOp("=");
                exprList();
                return;
            }
            if (k.getK() == K.OP && k.getText().length() >= 2 && StringsKt.endsWith(k.getText(), "=", false) && !SetsKt.setOf(new String[]{"==", "~=", "<=", ">="}).contains(k.getText())) {
                Tok opTok = next();
                String base = StringsKt.dropLast(opTok.getText(), 1);
                String lhs = CollectionsKt.joinToString(RangesKt.until(start, this.p - 1), " ", "", "", -1, "...", new Function1() { // from class: com.goydevv.studrblx.lua.LuauTranspiler$Parser$$ExternalSyntheticLambda0
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj) {
                        Tok tok = Parser.this.t.get(((Integer) obj).intValue());
                        return tok.getPrefix() + tok.getOut() + tok.getSuffix();
                    }
                });
                int rhsStart = this.p;
                expr();
                Tok last = this.t.get(this.p - 1);
                if (Intrinsics.areEqual(base, "//")) {
                    opTok.setOut("= math.floor(" + lhs + " / (");
                    last.setSuffix(last.getSuffix() + "))");
                } else {
                    opTok.setOut("= " + lhs + " " + base + " (");
                    last.setSuffix(last.getSuffix() + ")");
                }
                if (rhsStart == this.p) {
                    throw new ParseError();
                }
            }
        }

        public final void primaryExpr() throws IOException, ParseError {
            Tok k = peek$default(this, 0, 1, null);
            if (k.getK() != K.NAME) {
                if (!k.isOp("(")) {
                    throw new ParseError();
                }
                next();
                expr();
                expectOp(")");
                return;
            }
            next();
        }

        public final void suffixedExpr() throws IOException, ParseError {
            primaryExpr();
            while (true) {
                Tok k = peek$default(this, 0, 1, null);
                if (k.isOp(".")) {
                    next();
                    if (peek$default(this, 0, 1, null).getK() != K.NAME && peek$default(this, 0, 1, null).getK() != K.KEYWORD) {
                        throw new ParseError();
                    }
                    next();
                } else if (k.isOp("[")) {
                    next();
                    expr();
                    expectOp("]");
                } else if (k.isOp(":")) {
                    next();
                    expectName();
                    callArgs();
                } else if (k.isOp("(") || k.isOp("{") || k.getK() == K.STRING) {
                    callArgs();
                } else {
                    return;
                }
            }
        }

        public final void callArgs() throws IOException, ParseError {
            Tok k = peek$default(this, 0, 1, null);
            if (k.getK() != K.STRING) {
                if (!k.isOp("{")) {
                    if (k.isOp("(")) {
                        next();
                        if (!peek$default(this, 0, 1, null).isOp(")")) {
                            exprList();
                        }
                        expectOp(")");
                        return;
                    }
                    throw new ParseError();
                }
                table();
                Unit unit = Unit.INSTANCE;
                return;
            }
            next();
        }

        public final void table() throws IOException, ParseError {
            expectOp("{");
            while (!peek$default(this, 0, 1, null).isOp("}")) {
                if (peek$default(this, 0, 1, null).isOp("[")) {
                    next();
                    expr();
                    expectOp("]");
                    expectOp("=");
                    expr();
                } else if (peek$default(this, 0, 1, null).getK() == K.NAME && peek(1).isOp("=")) {
                    next();
                    next();
                    expr();
                } else {
                    expr();
                }
                if (!peek$default(this, 0, 1, null).isOp(",") && !peek$default(this, 0, 1, null).isOp(";")) {
                    break;
                } else {
                    next();
                }
            }
            expectOp("}");
        }

        public final void simpleExpr() throws IOException, ParseError {
            Tok k = peek$default(this, 0, 1, null);
            if (k.getK() == K.NUMBER || k.getK() == K.STRING || k.isKw("nil") || k.isKw("true") || k.isKw("false") || k.isOp("...")) {
                next();
            } else if (k.isOp("{")) {
                table();
            } else if (k.isKw("function")) {
                next();
                funcBody();
            } else if (k.isKw("if")) {
                ifExpr();
            } else {
                suffixedExpr();
            }
            while (peek$default(this, 0, 1, null).isOp("::")) {
                int s = this.p;
                next();
                type();
                drop(s, this.p);
            }
        }

        public final void ifExpr() throws IOException, ParseError {
            Tok ifTok = next();
            ifTok.setOut("(function() if");
            expr();
            expectKw("then").setOut("then return");
            expr();
            while (peek$default(this, 0, 1, null).isKw("elseif")) {
                next();
                expr();
                expectKw("then").setOut("then return");
                expr();
            }
            expectKw("else").setOut("else return");
            expr();
            Tok tok = this.t.get(this.p - 1);
            tok.setSuffix(tok.getSuffix() + " end end)()");
        }

        /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
        private final Pair<Integer, Integer> binPrio(Tok k) {
            int i;
            if (k.getK() == K.KEYWORD) {
                String text = k.getText();
                if (Intrinsics.areEqual(text, "or")) {
                    i = 1;
                } else {
                    if (!Intrinsics.areEqual(text, "and")) {
                        return null;
                    }
                    i = 2;
                }
                return TuplesKt.to(Integer.valueOf(i), Integer.valueOf(i));
            }
            if (k.getK() != K.OP) {
                return null;
            }
            String text2 = k.getText();
            switch (text2.hashCode()) {
                case 37:
                    if (!text2.equals("%")) {
                        return null;
                    }
                    return TuplesKt.to(11, 11);
                case 42:
                    if (!text2.equals("*")) {
                        return null;
                    }
                    return TuplesKt.to(11, 11);
                case 43:
                    if (!text2.equals("+")) {
                        return null;
                    }
                    return TuplesKt.to(10, 10);
                case 45:
                    if (!text2.equals("-")) {
                        return null;
                    }
                    return TuplesKt.to(10, 10);
                case 47:
                    if (!text2.equals("/")) {
                        return null;
                    }
                    return TuplesKt.to(11, 11);
                case 60:
                    if (!text2.equals("<")) {
                        return null;
                    }
                    return TuplesKt.to(3, 3);
                case 62:
                    if (!text2.equals(">")) {
                        return null;
                    }
                    return TuplesKt.to(3, 3);
                case 94:
                    if (text2.equals("^")) {
                        return TuplesKt.to(14, 13);
                    }
                    return null;
                case 1472:
                    if (text2.equals("..")) {
                        return TuplesKt.to(9, 8);
                    }
                    return null;
                case 1504:
                    if (!text2.equals("//")) {
                        return null;
                    }
                    return TuplesKt.to(11, 11);
                case 1921:
                    if (!text2.equals("<=")) {
                        return null;
                    }
                    return TuplesKt.to(3, 3);
                case 1952:
                    if (!text2.equals("==")) {
                        return null;
                    }
                    return TuplesKt.to(3, 3);
                case 1983:
                    if (!text2.equals(">=")) {
                        return null;
                    }
                    return TuplesKt.to(3, 3);
                case 3967:
                    if (!text2.equals("~=")) {
                        return null;
                    }
                    return TuplesKt.to(3, 3);
                default:
                    return null;
            }
        }

        public final void expr() throws IOException, ParseError {
            subExpr(0);
        }

        public final void subExpr(int limit) throws IOException, ParseError {
            int start = this.p;
            Tok k = peek$default(this, 0, 1, null);
            if (k.isKw("not") || k.isOp("-") || k.isOp("#")) {
                next();
                subExpr(12);
            } else {
                simpleExpr();
            }
            while (true) {
                Tok op = peek$default(this, 0, 1, null);
                Pair<Integer, Integer> pairBinPrio = binPrio(op);
                if (pairBinPrio != null && pairBinPrio.getFirst().intValue() > limit) {
                    next();
                    subExpr(pairBinPrio.getSecond().intValue());
                    if (op.isOp("//")) {
                        op.setOut("/");
                        this.t.get(start).setPrefix("math.floor(" + this.t.get(start).getPrefix());
                        Tok tok = this.t.get(this.p - 1);
                        tok.setSuffix(tok.getSuffix() + ")");
                    }
                } else {
                    return;
                }
            }
        }
    }
}

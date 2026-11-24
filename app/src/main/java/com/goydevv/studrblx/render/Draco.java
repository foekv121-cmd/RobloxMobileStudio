package com.goydevv.studrblx.render;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

/* JADX INFO: compiled from: Draco.kt */
/* JADX INFO: loaded from: classes8.dex */
public final class Draco {
    public static final int COLOR = 2;
    public static final int GENERIC = 4;
    public static final Draco INSTANCE = new Draco();
    private static final int KIND_GENERIC = 0;
    private static final int KIND_INTEGER = 1;
    private static final int KIND_NORMALS = 3;
    private static final int KIND_QUANTIZED = 2;
    public static final int NORMAL = 1;
    public static final int POSITION = 0;
    public static final int TEX_COORD = 3;

    private Draco() {
    }

    /* JADX INFO: compiled from: Draco.kt */
    public static final class DracoException extends Exception {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public DracoException(String msg) {
            super(msg);
            Intrinsics.checkNotNullParameter(msg, "msg");
        }
    }

    /* JADX INFO: compiled from: Draco.kt */
    public static final class Attribute {
        private final int components;
        private final int dataType;
        private final boolean normalized;
        private final int type;
        private final long uniqueId;
        private float[] values = new float[0];

        public Attribute(int type, int dataType, int components, boolean normalized, long uniqueId) {
            this.type = type;
            this.dataType = dataType;
            this.components = components;
            this.normalized = normalized;
            this.uniqueId = uniqueId;
        }

        public final int getComponents() {
            return this.components;
        }

        public final int getDataType() {
            return this.dataType;
        }

        public final boolean getNormalized() {
            return this.normalized;
        }

        public final int getType() {
            return this.type;
        }

        public final long getUniqueId() {
            return this.uniqueId;
        }

        public final float[] getValues() {
            return this.values;
        }

        public final void setValues(float[] fArr) {
            Intrinsics.checkNotNullParameter(fArr, "<set-?>");
            this.values = fArr;
        }
    }

    /* JADX INFO: compiled from: Draco.kt */
    public static final class Mesh {
        private final List<Attribute> attributes;
        private final int[] faces;
        private final int numPoints;

        public Mesh(int numPoints, int[] faces, List<Attribute> attributes) {
            Intrinsics.checkNotNullParameter(faces, "faces");
            Intrinsics.checkNotNullParameter(attributes, "attributes");
            this.numPoints = numPoints;
            this.faces = faces;
            this.attributes = attributes;
        }

        public final List<Attribute> getAttributes() {
            return this.attributes;
        }

        public final int[] getFaces() {
            return this.faces;
        }

        public final int getNumPoints() {
            return this.numPoints;
        }

        public final Attribute attribute(int type) {
            Object next;
            Iterator it = this.attributes.iterator();
            while (it.hasNext()) {
                next = it.next();
                if (((Attribute) next).getType() == type) {
                    return (Attribute) next;
                }
            }
            next = null;
            return (Attribute) next;
        }
    }

    public final boolean isDraco(byte[] b, int off) {
        Intrinsics.checkNotNullParameter(b, "b");
        return b.length >= off + 5 && b[off] == 68 && b[off + 1] == 82 && b[off + 2] == 65 && b[off + 3] == 67 && b[off + 4] == 79;
    }

    /* JADX INFO: compiled from: Draco.kt */
    private static final class Reader {
        private final byte[] b;
        private final int end;
        private int pos;

        public Reader(byte[] b, int pos, int end) {
            Intrinsics.checkNotNullParameter(b, "b");
            this.b = b;
            this.pos = pos;
            this.end = end;
        }

        public final byte[] getB() {
            return this.b;
        }

        public final int getEnd() {
            return this.end;
        }

        public final int getPos() {
            return this.pos;
        }

        public final void setPos(int i) {
            this.pos = i;
        }

        public final int getRemaining() {
            return this.end - this.pos;
        }

        public final void need(int n) throws DracoException {
            if (n < 0 || this.pos + n > this.end) {
                throw new DracoException("unexpected end of data");
            }
        }

        public final int u8() throws DracoException {
            need(1);
            byte[] bArr = this.b;
            int i = this.pos;
            this.pos = i + 1;
            return bArr[i] & 255;
        }

        public final int i8() throws DracoException {
            need(1);
            byte[] bArr = this.b;
            int i = this.pos;
            this.pos = i + 1;
            return bArr[i];
        }

        public final int u16() throws DracoException {
            need(2);
            int v = (this.b[this.pos] & 255) | ((this.b[this.pos + 1] & 255) << 8);
            this.pos += 2;
            return v;
        }

        public final int i32() throws DracoException {
            need(4);
            int v = (this.b[this.pos] & 255) | ((this.b[this.pos + 1] & 255) << 8) | ((this.b[this.pos + 2] & 255) << 16) | ((this.b[this.pos + 3] & 255) << 24);
            this.pos += 4;
            return v;
        }

        public final float f32() throws DracoException {
            return Float.intBitsToFloat(i32());
        }

        public final long varint() throws DracoException {
            long result = 0;
            int shift = 0;
            do {
                int v = u8();
                result |= ((long) (v & 127)) << shift;
                if ((v & 128) == 0) {
                    return result;
                }
                shift += 7;
            } while (shift <= 63);
            throw new DracoException("bad varint");
        }

        public final int varintInt() throws DracoException {
            long v = varint();
            if (v < 0 || v > 2147483647L) {
                throw new DracoException("value out of range");
            }
            return (int) v;
        }

        public final void skip(int n) throws DracoException {
            need(n);
            this.pos += n;
        }
    }

    /* JADX INFO: compiled from: Draco.kt */
    private static final class BitReader {
        private final byte[] b;
        private long bit;
        private final int end;
        private final int start;

        public BitReader(byte[] b, int start, int end) {
            Intrinsics.checkNotNullParameter(b, "b");
            this.b = b;
            this.start = start;
            this.end = end;
        }

        public final byte[] getB() {
            return this.b;
        }

        public final int getEnd() {
            return this.end;
        }

        public final int getStart() {
            return this.start;
        }

        public final long getBit() {
            return this.bit;
        }

        public final void setBit(long j) {
            this.bit = j;
        }

        public final int get(int n) {
            int v = 0;
            for (int i = 0; i < n; i++) {
                int i2 = this.start + ((int) (this.bit >>> 3));
                int x = i2 < this.end ? (this.b[i2] >>> ((int) (this.bit & 7))) & 1 : 0;
                this.bit++;
                v |= x << i;
            }
            return v;
        }

        public final int bytesUsed() {
            return (int) ((this.bit + 7) / 8);
        }
    }

    /* JADX INFO: compiled from: Draco.kt */
    private static final class RAns {
        private byte[] buf;
        private final int[] cum;
        private final long lBase;
        private final int[] lut;
        private int offset;
        private final int precision;
        private final int[] prob;
        private int start;
        private long state;

        public RAns(int precisionBits, int[] probs) throws DracoException {
            Intrinsics.checkNotNullParameter(probs, "probs");
            this.precision = 1 << precisionBits;
            this.lBase = ((long) this.precision) * 4;
            this.lut = new int[this.precision];
            int[] iArrCopyOf = Arrays.copyOf(probs, probs.length);
            Intrinsics.checkNotNullExpressionValue(iArrCopyOf, "copyOf(...)");
            this.prob = iArrCopyOf;
            this.cum = new int[probs.length];
            this.buf = new byte[0];
            long c = 0;
            int act = 0;
            int length = probs.length;
            for (int i = 0; i < length; i++) {
                this.cum[i] = (int) c;
                c += ((long) probs[i]) & 4294967295L;
                if (c > this.precision) {
                    throw new DracoException("bad probability table");
                }
                int i2 = (int) c;
                for (int j = act; j < i2; j++) {
                    this.lut[j] = i;
                }
                act = (int) c;
            }
            int i3 = this.precision;
            if (c != i3) {
                throw new DracoException("bad probability table");
            }
        }

        public final void init(byte[] b, int s, int len) throws DracoException {
            Intrinsics.checkNotNullParameter(b, "b");
            if (len < 1) {
                throw new DracoException("empty rANS data");
            }
            this.buf = b;
            this.start = s;
            int last = b[(s + len) - 1] & 255;
            switch (last >>> 6) {
                case 0:
                    this.offset = len - 1;
                    this.state = last & 63;
                    break;
                case 1:
                    if (len < 2) {
                        throw new DracoException("bad rANS data");
                    }
                    this.offset = len - 2;
                    this.state = init$le(b, s, len - 2, 2) & 16383;
                    break;
                case 2:
                    if (len < 3) {
                        throw new DracoException("bad rANS data");
                    }
                    this.offset = len - 3;
                    this.state = init$le(b, s, len - 3, 3) & 4194303;
                    break;
                default:
                    if (len >= 4) {
                        this.offset = len - 4;
                        this.state = init$le(b, s, len - 4, 4) & 1073741823L;
                    } else {
                        throw new DracoException("bad rANS data");
                    }
                    break;
            }
            this.state += this.lBase;
            if (this.state >= this.lBase * 256) {
                throw new DracoException("bad rANS state");
            }
        }

        private static final long init$le(byte[] $b, int $s, int at, int n) {
            long v = 0;
            for (int k = 0; k < n; k++) {
                v |= (((long) $b[($s + at) + k]) & 255) << (k * 8);
            }
            return v;
        }

        public final int read() {
            while (this.state < this.lBase && this.offset > 0) {
                long j = this.state * 256;
                byte[] bArr = this.buf;
                int i = this.start;
                this.offset--;
                this.state = j + (((long) bArr[i + this.offset]) & 255);
            }
            long quo = this.state / ((long) this.precision);
            int rem = (int) (this.state % ((long) this.precision));
            int sym = this.lut[rem];
            this.state = (((((long) this.prob[sym]) & 4294967295L) * quo) + ((long) rem)) - ((long) this.cum[sym]);
            return sym;
        }
    }

    /* JADX INFO: compiled from: Draco.kt */
    private static final class SymbolDecoder {
        private final RAns ans;
        private final int numSymbols;

        public SymbolDecoder(int numSymbols, RAns ans) {
            this.numSymbols = numSymbols;
            this.ans = ans;
        }

        public final RAns getAns() {
            return this.ans;
        }

        public final int getNumSymbols() {
            return this.numSymbols;
        }

        public final void start(Reader r) throws DracoException {
            Intrinsics.checkNotNullParameter(r, "r");
            long bytes = r.varint();
            if (bytes < 0 || bytes > r.getRemaining()) {
                throw new DracoException("bad rANS size");
            }
            int n = (int) bytes;
            RAns rAns = this.ans;
            if (rAns == null) {
                throw new DracoException("no symbols");
            }
            rAns.init(r.getB(), r.getPos(), n);
            r.setPos(r.getPos() + n);
        }

        public final int read() {
            RAns rAns = this.ans;
            Intrinsics.checkNotNull(rAns);
            return rAns.read();
        }
    }

    private final int precisionFor(int bits) {
        return RangesKt.coerceIn((bits * 3) / 2, 12, 20);
    }

    private final SymbolDecoder createSymbolDecoder(Reader r, int uniqueSymbolBits) throws DracoException {
        int num = r.varintInt();
        if (num / 64 > r.getRemaining()) {
            throw new DracoException("too many symbols");
        }
        if (num == 0) {
            return new SymbolDecoder(0, null);
        }
        int[] probs = new int[num];
        int i = 0;
        while (i < num) {
            int data = r.u8();
            int token = data & 3;
            if (token == 3) {
                int run = data >>> 2;
                if (i + run >= num) {
                    throw new DracoException("bad probability run");
                }
                i += run + 1;
            } else {
                int p = data >>> 2;
                for (int k = 0; k < token; k++) {
                    p |= r.u8() << (((k + 1) * 8) - 2);
                }
                probs[i] = p;
                i++;
            }
        }
        return new SymbolDecoder(num, new RAns(precisionFor(uniqueSymbolBits), probs));
    }

    private final int[] decodeSymbols(int n, int comps, Reader r) throws DracoException {
        int[] out = new int[n];
        if (n == 0) {
            return out;
        }
        switch (r.u8()) {
            case 0:
                SymbolDecoder tags = createSymbolDecoder(r, 5);
                tags.start(r);
                if (tags.getNumSymbols() == 0) {
                    throw new DracoException("no tag symbols");
                }
                BitReader bits = new BitReader(r.getB(), r.getPos(), r.getEnd());
                int id = 0;
                int i = 0;
                while (i < n) {
                    int len = tags.read();
                    if (len > 32) {
                        throw new DracoException("bad tag");
                    }
                    int j = 0;
                    while (j < comps && id < n) {
                        out[id] = bits.get(len);
                        j++;
                        id++;
                    }
                    i += comps;
                }
                r.setPos(r.getPos() + bits.bytesUsed());
                if (r.getPos() > r.getEnd()) {
                    throw new DracoException("unexpected end of data");
                }
                return out;
            case 1:
                int maxBits = r.u8();
                if (maxBits < 1 || maxBits > 18) {
                    throw new DracoException("bad symbol bit length " + maxBits);
                }
                SymbolDecoder dec = createSymbolDecoder(r, maxBits);
                if (dec.getNumSymbols() == 0) {
                    throw new DracoException("no symbols");
                }
                dec.start(r);
                for (int i2 = 0; i2 < n; i2++) {
                    out[i2] = dec.read();
                }
                return out;
            default:
                throw new DracoException("unknown symbol coding");
        }
    }

    /* JADX INFO: compiled from: Draco.kt */
    private static final class Octa {
        private final int center;
        private final int maxQ;
        private final int maxValue;
        private final float scale;

        public Octa(int bits) throws DracoException {
            if (bits < 2 || bits > 30) {
                throw new DracoException("bad normal quantization");
            }
            this.maxQ = (1 << bits) - 1;
            this.maxValue = this.maxQ - 1;
            this.center = this.maxValue / 2;
            this.scale = 2.0f / this.maxValue;
        }

        public final int getMaxQ() {
            return this.maxQ;
        }

        public final int getMaxValue() {
            return this.maxValue;
        }

        public final int getCenter() {
            return this.center;
        }

        public final float getScale() {
            return this.scale;
        }

        public final boolean isInDiamond(int s, int t) {
            return ((long) Math.abs(s)) + ((long) Math.abs(t)) <= ((long) this.center);
        }

        public final void invertDiamond(int[] p) {
            int signS;
            int signT;
            int us;
            int ut;
            Intrinsics.checkNotNullParameter(p, "p");
            int s = p[0];
            int t = p[1];
            if (s >= 0 && t >= 0) {
                signS = 1;
                signT = 1;
            } else if (s > 0 || t > 0) {
                signS = s > 0 ? 1 : -1;
                signT = t > 0 ? 1 : -1;
            } else {
                signS = -1;
                signT = -1;
            }
            int cs = this.center * signS;
            int ct = this.center * signT;
            int us2 = (s + s) - cs;
            int ut2 = (t + t) - ct;
            if (signS * signT >= 0) {
                us = -ut2;
                ut = -us2;
            } else {
                us = ut2;
                ut = us2;
            }
            p[0] = (us + cs) / 2;
            p[1] = (ut + ct) / 2;
        }

        public final int modMax(int x) {
            if (x > this.center) {
                return x - this.maxQ;
            }
            return x < (-this.center) ? this.maxQ + x : x;
        }

        public final void toUnit(int si, int ti, float[] out, int o) {
            Intrinsics.checkNotNullParameter(out, "out");
            float y = (si * this.scale) - 1.0f;
            float z = (ti * this.scale) - 1.0f;
            float x = (1.0f - Math.abs(y)) - Math.abs(z);
            float xo = (-x) < 0.0f ? 0.0f : -x;
            float y2 = y + (y < 0.0f ? xo : -xo);
            float z2 = z + (z < 0.0f ? xo : -xo);
            float n2 = (x * x) + (y2 * y2) + (z2 * z2);
            if (n2 >= 1.0E-6f) {
                float d = 1.0f / ((float) Math.sqrt(n2));
                out[o] = x * d;
                out[o + 1] = y2 * d;
                out[o + 2] = z2 * d;
                return;
            }
            out[o] = 0.0f;
            out[o + 1] = 0.0f;
            out[o + 2] = 0.0f;
        }
    }

    private final int rotationCount(int x, int y) {
        if (x == 0) {
            if (y == 0) {
                return 0;
            }
            return y > 0 ? 3 : 1;
        }
        if (x > 0) {
            return y >= 0 ? 2 : 1;
        }
        return y <= 0 ? 0 : 3;
    }

    private final void rotate(int[] p, int count) {
        int x = p[0];
        int y = p[1];
        switch (count) {
            case 1:
                p[0] = y;
                p[1] = -x;
                break;
            case 2:
                p[0] = -x;
                p[1] = -y;
                break;
            case 3:
                p[0] = -y;
                p[1] = x;
                break;
        }
    }

    /* JADX INFO: compiled from: Draco.kt */
    private static final class AttDecoder {
        private final Attribute att;
        private final int kind;
        private float[] minValues;
        private int octBits;
        private int[] portable;
        private int qBits;
        private float range;

        public AttDecoder(Attribute att, int kind) {
            Intrinsics.checkNotNullParameter(att, "att");
            this.att = att;
            this.kind = kind;
            this.portable = new int[0];
            this.minValues = new float[0];
        }

        public final Attribute getAtt() {
            return this.att;
        }

        public final int getKind() {
            return this.kind;
        }

        public final int[] getPortable() {
            return this.portable;
        }

        public final void setPortable(int[] iArr) {
            Intrinsics.checkNotNullParameter(iArr, "<set-?>");
            this.portable = iArr;
        }

        public final float[] getMinValues() {
            return this.minValues;
        }

        public final void setMinValues(float[] fArr) {
            Intrinsics.checkNotNullParameter(fArr, "<set-?>");
            this.minValues = fArr;
        }

        public final float getRange() {
            return this.range;
        }

        public final void setRange(float f) {
            this.range = f;
        }

        public final int getQBits() {
            return this.qBits;
        }

        public final void setQBits(int i) {
            this.qBits = i;
        }

        public final int getOctBits() {
            return this.octBits;
        }

        public final void setOctBits(int i) {
            this.octBits = i;
        }
    }

    private final int typeLength(int dt) throws DracoException {
        switch (dt) {
            case 1:
            case 2:
            case 11:
                return 1;
            case 3:
            case 4:
                return 2;
            case 5:
            case 6:
            case 9:
                return 4;
            case 7:
            case 8:
            case 10:
                return 8;
            default:
                throw new DracoException("bad data type " + dt);
        }
    }

    private final float readTyped(Reader r, int dt) throws DracoException {
        switch (dt) {
            case 1:
                return r.i8();
            case 2:
            case 11:
                return r.u8();
            case 3:
                return (short) r.u16();
            case 4:
                return r.u16();
            case 5:
                return r.i32();
            case 6:
                return ((long) r.i32()) & 4294967295L;
            case 7:
            case 8:
                long lo = ((long) r.i32()) & 4294967295L;
                long hi = r.i32();
                return (hi << 32) | lo;
            case 9:
                return r.f32();
            case 10:
                long lo2 = ((long) r.i32()) & 4294967295L;
                long hi2 = r.i32();
                return (float) Double.longBitsToDouble((hi2 << 32) | lo2);
            default:
                throw new DracoException("bad data type " + dt);
        }
    }

    /* JADX WARN: Code duplicated, block: B:38:0x009a  */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x0093, code lost:
    
        r8 = true;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r10v6 */
    /* JADX WARN: Type inference failed for: r10v7 */
    /* JADX WARN: Type inference failed for: r10v9 */
    /* JADX WARN: Type inference failed for: r16v6 */
    /* JADX WARN: Type inference failed for: r16v8 */
    /* JADX WARN: Type inference failed for: r16v9 */
    /* JADX WARN: Type inference failed for: r17v0 */
    /* JADX WARN: Type inference failed for: r17v3 */
    /* JADX WARN: Type inference failed for: r17v4 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private final void decodePortable(AttDecoder d, int n, Reader r) throws DracoException {
        Attribute att = d.getAtt();
        int kind = d.getKind();
        if (kind == KIND_GENERIC) {
            int comps = att.getComponents();
            int dt = att.getDataType();
            float[] out = new float[n * comps];
            for (int i = 0; i < out.length; i++) {
                out[i] = readTyped(r, dt);
            }
            att.setValues(out);
            return;
        }

        int comps = (kind == KIND_NORMALS) ? 2 : att.getComponents();
        int total = n * comps;
        if (total <= 0) {
            d.setPortable(new int[0]);
            return;
        }

        int predScheme = r.i8();
        int transformType = 0;
        if (predScheme != -2) {
            if (predScheme != 0) {
                throw new DracoException("unsupported prediction scheme " + predScheme);
            }
            transformType = r.i8();
        }

        int compressed = r.u8();
        int[] values = new int[total];
        if (compressed != 0) {
            int[] syms = decodeSymbols(total, comps, r);
            boolean signed;
            if (predScheme != -2) {
                signed = true;
            } else if (kind == KIND_INTEGER) {
                int dt = att.getDataType();
                signed = (dt == 1 || dt == 3 || dt == 5 || dt == 7);
            } else {
                signed = false;
            }
            if (signed) {
                for (int i = 0; i < total; i++) {
                    int s = syms[i];
                    values[i] = (s >>> 1) ^ -(s & 1);
                }
            } else {
                System.arraycopy(syms, 0, values, 0, total);
            }
        } else {
            int byteSize = r.u8();
            if (byteSize == 1) {
                for (int i = 0; i < total; i++) {
                    values[i] = r.i8();
                }
            } else if (byteSize == 2) {
                for (int i = 0; i < total; i++) {
                    values[i] = (short) r.u16();
                }
            } else if (byteSize == 4) {
                for (int i = 0; i < total; i++) {
                    values[i] = r.i32();
                }
            } else {
                throw new DracoException("bad uncompressed int size " + byteSize);
            }
        }

        if (predScheme == 0) {
            if (transformType == 0) {
                // PREDICTION_TRANSFORM_DELTA
                for (int i = comps; i < total; i++) {
                    values[i] = values[i] + values[i - comps];
                }
            } else if (transformType == 1) {
                // PREDICTION_TRANSFORM_WRAP
                int minVal = r.i32();
                int maxVal = r.i32();
                long maxDif = 1L + ((long) maxVal) - ((long) minVal);
                for (int i = 0; i < total; i++) {
                    int pred = (i < comps) ? 0 : values[i - comps];
                    if (pred > maxVal) pred = maxVal;
                    else if (pred < minVal) pred = minVal;
                    long v = ((long) pred) + ((long) values[i]);
                    if (v > maxVal) v -= maxDif;
                    else if (v < minVal) v += maxDif;
                    values[i] = (int) v;
                }
            } else if (transformType == 2 || transformType == 3) {
                // PREDICTION_TRANSFORM_NORMAL_OCTAHEDRON (2) / CANONICALIZED (3)
                int maxQuantizedValue = r.i32();
                r.i32(); // center_value (stored in bitstream)
                int bits = 32 - Integer.numberOfLeadingZeros(Math.max(maxQuantizedValue, 3));
                Octa oct = new Octa(bits);
                int center = oct.getCenter();
                int[] predPt = new int[2];
                int[] origPt = new int[2];
                for (int i = 0; i < total; i += 2) {
                    int p0 = (i == 0) ? 0 : values[i - 2];
                    int p1 = (i == 0) ? 0 : values[i - 1];
                    int c0 = values[i];
                    int c1 = values[i + 1];
                    if (transformType == 3) {
                        predPt[0] = p0 - center;
                        predPt[1] = p1 - center;
                        boolean inDiamond = oct.isInDiamond(predPt[0], predPt[1]);
                        if (!inDiamond) {
                            oct.invertDiamond(predPt);
                        }
                        int rot = rotationCount(predPt[0], predPt[1]);
                        rotate(predPt, rot);
                        origPt[0] = oct.modMax(predPt[0] + c0);
                        origPt[1] = oct.modMax(predPt[1] + c1);
                        rotate(origPt, (4 - rot) & 3);
                        if (!inDiamond) {
                            oct.invertDiamond(origPt);
                        }
                        values[i] = origPt[0] + center;
                        values[i + 1] = origPt[1] + center;
                    } else {
                        predPt[0] = p0 - center;
                        predPt[1] = p1 - center;
                        boolean inDiamond = oct.isInDiamond(predPt[0], predPt[1]);
                        if (!inDiamond) {
                            oct.invertDiamond(predPt);
                        }
                        origPt[0] = oct.modMax(predPt[0] + c0);
                        origPt[1] = oct.modMax(predPt[1] + c1);
                        if (!inDiamond) {
                            oct.invertDiamond(origPt);
                        }
                        values[i] = origPt[0] + center;
                        values[i + 1] = origPt[1] + center;
                    }
                }
            } else {
                throw new DracoException("unsupported prediction transform " + transformType);
            }
        }

        d.setPortable(values);
    }

    private final void decodeTransformData(AttDecoder d, Reader r) throws DracoException {
        switch (d.getKind()) {
            case 2:
                int components = d.getAtt().getComponents();
                float[] fArr = new float[components];
                for (int i = 0; i < components; i++) {
                    fArr[i] = r.f32();
                }
                d.setMinValues(fArr);
                d.setRange(r.f32());
                d.setQBits(r.u8());
                if (d.getQBits() < 1 || d.getQBits() > 30) {
                    throw new DracoException("bad quantization");
                }
                return;
            case 3:
                d.setOctBits(r.u8());
                return;
            default:
                return;
        }
    }

    private final void finish(AttDecoder d, int n) {
        Attribute a = d.getAtt();
        int i = 0;
        switch (d.getKind()) {
            case 1:
                int length = d.getPortable().length;
                float[] fArr = new float[length];
                while (i < length) {
                    fArr[i] = d.getPortable()[i];
                    i++;
                }
                a.setValues(fArr);
                break;
            case 2:
                int maxQ = (1 << d.getQBits()) - 1;
                float delta = d.getRange() / maxQ;
                int c = a.getComponents();
                int i2 = n * c;
                float[] fArr2 = new float[i2];
                while (i < i2) {
                    fArr2[i] = (d.getPortable()[i] * delta) + d.getMinValues()[i % c];
                    i++;
                }
                a.setValues(fArr2);
                break;
            case 3:
                Octa oct; try { oct = new Octa(d.getOctBits()); } catch (DracoException _e) { throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_e); }
                float[] out = new float[n * 3];
                for (int i3 = 0; i3 < n; i3++) {
                    oct.toUnit(d.getPortable()[i3 * 2], d.getPortable()[(i3 * 2) + 1], out, i3 * 3);
                }
                a.setValues(out);
                break;
        }
    }

    static /* synthetic */ void skipMetadata$default(Draco draco, Reader reader, int i, int i2, Object obj) throws DracoException {
        if ((i2 & 2) != 0) {
            i = 0;
        }
        draco.skipMetadata(reader, i);
    }

    private final void skipMetadata(Reader r, int depth) throws DracoException {
        if (depth > 32) {
            throw new DracoException("metadata too deep");
        }
        int entries = r.varintInt();
        for (int i = 0; i < entries; i++) {
            r.skip(r.u8());
            r.skip(r.varintInt());
        }
        int subs = r.varintInt();
        for (int i2 = 0; i2 < subs; i2++) {
            r.skip(r.u8());
            INSTANCE.skipMetadata(r, depth + 1);
        }
    }

    public static /* synthetic */ Mesh decode$default(Draco draco, byte[] bArr, int i, int i2, int i3, Object obj) throws DracoException {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = bArr.length - i;
        }
        return draco.decode(bArr, i, i2);
    }

    public final Mesh decode(byte[] bytes, int off, int len) throws DracoException {
        int numFaces;
        int numPoints;
        int iI32;
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        Reader r = new Reader(bytes, off, off + len);
        if (!isDraco(bytes, off)) {
            throw new DracoException("not Draco data");
        }
        char c = 5;
        r.skip(5);
        int major = r.u8();
        int minor = r.u8();
        int version = (major * 256) + minor;
        if (major != 2) {
            throw new DracoException("Draco version " + major + "." + minor + " isn't supported");
        }
        if (r.u8() != 1) {
            throw new DracoException("not a triangle mesh");
        }
        int method = r.u8();
        if (method != 0) {
            throw new DracoException("Draco edgebreaker meshes aren't supported");
        }
        int flags = r.u16();
        if ((32768 & flags) != 0) {
            int attMeta = r.varintInt();
            int i = 0;
            while (i < attMeta) {
                r.varint();
                skipMetadata$default(INSTANCE, r, 0, 2, null);
                i++;
            }
            skipMetadata$default(this, r, 0, 2, null);
        }
        if (version < 514) {
            numFaces = r.i32();
            numPoints = r.i32();
        } else {
            numFaces = r.varintInt();
            numPoints = r.varintInt();
        }
        if (numFaces < 0 || numFaces > 20000000) {
            throw new DracoException("bad face count");
        }
        if (numPoints < 0) {
            throw new DracoException("bad point count");
        }
        int[] faces = new int[numFaces * 3];
        if (r.u8() == 0) {
            int[] enc = decodeSymbols(numFaces * 3, 1, r);
            int length = enc.length;
            int last = 0;
            for (int i2 = 0; i2 < length; i2++) {
                int e = enc[i2];
                int diff = e >>> 1;
                if ((e & 1) != 0) {
                    if (diff > last) {
                        throw new DracoException("bad index");
                    }
                    diff = -diff;
                }
                last += diff;
                faces[i2] = last;
            }
        } else {
            int length2 = faces.length;
            for (int i3 = 0; i3 < length2; i3++) {
                if (numPoints < 256) {
                    iI32 = r.u8();
                } else if (numPoints < 65536) {
                    iI32 = r.u16();
                } else {
                    iI32 = (numPoints >= 2097152 || version < 514) ? r.i32() : r.varintInt();
                }
                faces[i3] = iI32;
            }
        }
        for (int f : faces) {
            if (f < 0 || f >= numPoints) {
                throw new DracoException("face index out of range");
            }
        }
        ArrayList groups = new ArrayList();
        int nDec = r.u8();
        int g = 0;
        while (g < nDec) {
            int nAtt = version < 512 ? r.i32() : r.varintInt();
            if (nAtt <= 0 || nAtt > r.getRemaining() * 5) {
                throw new DracoException("bad attribute count");
            }
            ArrayList atts = new ArrayList();
            int k = 0;
            while (k < nAtt) {
                int type = r.u8();
                int k2 = k;
                int dt = r.u8();
                int comps = r.u8();
                int norm = r.u8();
                if (dt != 0 && dt <= 11 && comps != 0) {
                    long uid = version < 259 ? r.u16() : r.varint();
                    atts.add(new Attribute(type, dt, comps, norm > 0, uid));
                    k = k2 + 1;
                    continue;
                }
                throw new DracoException("bad attribute");
            }
            int numFaces3 = numFaces;
            int version2 = version;
            int nDec2 = nDec;
            ArrayList arrayList = atts;
            int i4 = 0;
            Collection arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(arrayList, 10));
            Iterable<Attribute> iterable = arrayList;
            for (Attribute attribute : iterable) {
                Iterable iterable2 = arrayList;
                int i5 = i4;
                int iU8 = r.u8();
                Iterable iterable3 = iterable;
                if (iU8 > 3) {
                    throw new DracoException("unknown attribute decoder " + iU8);
                }
                if ((iU8 == 2 || iU8 == 3) && attribute.getDataType() != 9) {
                    throw new DracoException("bad attribute type");
                }
                if (iU8 == 3 && attribute.getComponents() != 3) {
                    throw new DracoException("bad normal attribute");
                }
                arrayList2.add(new AttDecoder(attribute, iU8));
                arrayList = (ArrayList) iterable2;
                i4 = i5;
                iterable = iterable3;
            }
            groups.add((List) arrayList2);
            g++;
            numFaces = numFaces3;
            version = version2;
            nDec = nDec2;
        }
        Iterator it = groups.iterator();
        Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
        while (it.hasNext()) {
            Object next = it.next();
            Intrinsics.checkNotNullExpressionValue(next, "next(...)");
            List<AttDecoder> decs = (List) next;
            for (AttDecoder d : decs) {
                decodePortable(d, numPoints, r);
            }
            for (AttDecoder d2 : decs) {
                decodeTransformData(d2, r);
            }
            for (AttDecoder d3 : decs) {
                finish(d3, numPoints);
            }
        }
        Iterable iterableFlatten = CollectionsKt.flatten(groups);
        Collection arrayList3 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterableFlatten, 10));
        Iterator it2 = iterableFlatten.iterator();
        while (it2.hasNext()) {
            arrayList3.add(((AttDecoder) it2.next()).getAtt());
        }
        return new Mesh(numPoints, faces, (List) arrayList3);
    }
}

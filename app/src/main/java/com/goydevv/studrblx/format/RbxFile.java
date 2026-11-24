package com.goydevv.studrblx.format;

import androidx.recyclerview.widget.ItemTouchHelper;
import com.goydevv.studrblx.model.Instance;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;
import kotlin.io.ByteStreamsKt;
import kotlin.io.CloseableKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import kotlin.text.StringsKt;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: compiled from: RbxBinary.kt */
/* JADX INFO: loaded from: classes5.dex */
public final class RbxFile {
    public static final RbxFile INSTANCE = new RbxFile();

    private RbxFile() {
    }

    public final boolean isBinary(byte[] b) {
        Intrinsics.checkNotNullParameter(b, "b");
        return b.length > 14 && Intrinsics.areEqual(new String(b, 0, 8, Charsets.ISO_8859_1), "<roblox!");
    }

    public final boolean isXml(byte[] b) {
        Intrinsics.checkNotNullParameter(b, "b");
        String head = StringsKt.trimStart(new String(b, 0, Math.min(b.length, ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION), Charsets.ISO_8859_1), (char) 65279, ' ', '\n', '\r', '\t', (char) 239, (char) 187, (char) 191);
        return StringsKt.startsWith(head, "<roblox", false) || StringsKt.startsWith(head, "<?xml", false);
    }

    public final byte[] unwrap(byte[] b) {
        Intrinsics.checkNotNullParameter(b, "b");
        if (b.length <= 2 || b[0] != 31 || b[1] != -117) {
            return b;
        }
        GZIPInputStream gZIPInputStream; try { gZIPInputStream = new GZIPInputStream(new ByteArrayInputStream(b)); } catch (java.io.IOException _e) { throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_e); }
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

    public final RbxDocument read(byte[] b0) throws RbxFormatException, XmlPullParserException, IOException {
        RbxDocument doc;
        Intrinsics.checkNotNullParameter(b0, "b0");
        byte[] b = unwrap(b0);
        if (isBinary(b)) {
            doc = new RbxBinaryReader(b).read();
        } else {
            if (!isXml(b)) {
                throw new RbxFormatException("Not a Roblox place or model file");
            }
            doc = RbxXmlReader.INSTANCE.read(b);
        }
        normalize(doc);
        return doc;
    }

    private final void normalize(RbxDocument doc) {
        for (Instance r : doc.getRoots()) {
            if (Intrinsics.areEqual(r.getClassName(), "MaterialService")) {
                Object plain = r.getProps().get("Use2022Materials");
                if (r.getProps().containsKey("Use2022Materials")) {
                    r.getProps().remove("Use2022Materials");
                    r.getPropTypes().remove("Use2022Materials");
                    if (!r.getProps().containsKey("Use2022MaterialsXml") && (plain instanceof Boolean)) {
                        r.getProps().put("Use2022MaterialsXml", plain);
                        r.getPropTypes().put("Use2022MaterialsXml", 2);
                    }
                }
            }
        }
    }

    public final boolean isPlace(RbxDocument doc) {
        Intrinsics.checkNotNullParameter(doc, "doc");
        Iterable roots = doc.getRoots();
        if ((roots instanceof Collection) && ((Collection) roots).isEmpty()) {
            return false;
        }
        Iterator it = roots.iterator();
        while (it.hasNext()) {
            if (Intrinsics.areEqual(((Instance) it.next()).getClassName(), "Workspace")) {
                return true;
            }
        }
        return false;
    }
}

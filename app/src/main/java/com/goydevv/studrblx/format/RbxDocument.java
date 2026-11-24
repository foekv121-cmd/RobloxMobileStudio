package com.goydevv.studrblx.format;

import com.goydevv.studrblx.model.Instance;
import java.util.List;
import java.util.Map;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: RbxBinary.kt */
/* JADX INFO: loaded from: classes5.dex */
public final class RbxDocument {
    private final Map<String, String> meta;
    private final List<Instance> roots;
    private final List<String> warnings;

    /* JADX WARN: Multi-variable type inference failed */
    public RbxDocument(List<? extends Instance> roots, Map<String, String> meta, List<String> warnings) {
        Intrinsics.checkNotNullParameter(roots, "roots");
        Intrinsics.checkNotNullParameter(meta, "meta");
        Intrinsics.checkNotNullParameter(warnings, "warnings");
        this.roots = (List<Instance>) roots;
        this.meta = meta;
        this.warnings = warnings;
    }

    public final Map<String, String> getMeta() {
        return this.meta;
    }

    public final List<Instance> getRoots() {
        return this.roots;
    }

    public final List<String> getWarnings() {
        return this.warnings;
    }
}

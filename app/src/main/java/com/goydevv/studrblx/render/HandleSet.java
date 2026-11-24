package com.goydevv.studrblx.render;

import com.goydevv.studrblx.core.Tool;
import com.goydevv.studrblx.model.CFrame;
import com.goydevv.studrblx.model.Vector3;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: SceneRenderer.kt */
/* JADX INFO: loaded from: classes8.dex */
public final class HandleSet {
    private final Vector3 center;
    private final List<Handle> handles;
    private final CFrame local;
    private final Tool tool;

    public HandleSet(Tool tool, Vector3 center, List<Handle> handles, CFrame local) {
        Intrinsics.checkNotNullParameter(tool, "tool");
        Intrinsics.checkNotNullParameter(center, "center");
        Intrinsics.checkNotNullParameter(handles, "handles");
        this.tool = tool;
        this.center = center;
        this.handles = handles;
        this.local = local;
    }

    public final Vector3 getCenter() {
        return this.center;
    }

    public final List<Handle> getHandles() {
        return this.handles;
    }

    public final CFrame getLocal() {
        return this.local;
    }

    public final Tool getTool() {
        return this.tool;
    }
}

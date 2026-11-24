package com.goydevv.studrblx.engine;

import com.goydevv.studrblx.format.RbxDocument;
import com.goydevv.studrblx.format.RbxFile;
import com.goydevv.studrblx.model.Instance;
import com.goydevv.studrblx.net.AssetFetcher;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: KeyframeAnim.kt */
/* JADX INFO: loaded from: classes7.dex */
public final class AnimLibrary {
    public static final AnimLibrary INSTANCE = new AnimLibrary();
    private static final ConcurrentHashMap<Long, AnimClip> clips = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap.KeySetView<Long, Boolean> failed = ConcurrentHashMap.newKeySet();
    private static final ConcurrentHashMap.KeySetView<Long, Boolean> pending = ConcurrentHashMap.newKeySet();
    private static final ExecutorService pool = Executors.newFixedThreadPool(4);

    private AnimLibrary() {
    }

    private static final ConcurrentHashMap<Long, String> lastLog = new ConcurrentHashMap<>();
    private static volatile long lastRetry = System.currentTimeMillis();

    static void logOnce(long id, String msg) {
        String prev = lastLog.put(Long.valueOf(id), msg);
        if (!msg.equals(prev)) {
            com.goydevv.studrblx.core.OutputLog.INSTANCE.error(msg);
        }
    }

    public final AnimClip get(final long id) {
        long nowMs = System.currentTimeMillis();
        if (nowMs - lastRetry > 60000L) {
            lastRetry = nowMs;
            failed.clear();
        }
        AnimClip animClip = clips.get(Long.valueOf(id));
        if (animClip != null) {
            return animClip;
        }
        if (failed.contains(Long.valueOf(id)) || !pending.add(Long.valueOf(id))) {
            return null;
        }
        pool.execute(new Runnable() { // from class: com.goydevv.studrblx.engine.AnimLibrary$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                AnimLibrary.get$lambda$4(id);
            }
        });
        return null;
    }

    static final void get$lambda$4(long $id) {
        Object next;
        try {
            try {
                byte[] bytes = AssetFetcher.INSTANCE.load($id);
                RbxDocument doc = RbxFile.INSTANCE.read(bytes);
                Iterable roots = doc.getRoots();
                Collection arrayList = new ArrayList();
                Iterator it = roots.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    Instance instance = (Instance) it.next();
                    CollectionsKt.addAll(arrayList, CollectionsKt.plus((Collection) CollectionsKt.listOf(instance), (Iterable) Instance.descendants$default(instance, null, 1, null)));
                }
                Iterator it2 = ((List) arrayList).iterator();
                do {
                    if (!it2.hasNext()) {
                        next = null;
                        break;
                    }
                    next = it2.next();
                } while (!Intrinsics.areEqual(((Instance) next).getClassName(), "KeyframeSequence"));
                Instance seq = (Instance) next;
                AnimClip clip = seq != null ? AnimClip.INSTANCE.from(seq) : null;
                if (clip != null) {
                    clips.put(Long.valueOf($id), clip);
                } else {
                    failed.add(Long.valueOf($id));
                    java.util.LinkedHashSet<String> found = new java.util.LinkedHashSet<>();
                    for (Object o : (List) arrayList) {
                        found.add(((Instance) o).getClassName());
                    }
                    java.io.File badFile = AssetFetcher.INSTANCE.cachedFile($id);
                    if (badFile != null) {
                        badFile.delete();
                    }
                    AnimLibrary.logOnce($id, "Animation " + $id + " downloaded (" + bytes.length + " bytes) but has no KeyframeSequence. Found: " + found + ". It may be a CurveAnimation or not an animation asset.");
                }
            } catch (Exception e) {
                failed.add(Long.valueOf($id));
                java.io.File badFile2 = AssetFetcher.INSTANCE.cachedFile($id);
                if (badFile2 != null) {
                    badFile2.delete();
                }
                String why = e.getClass().getSimpleName() + ": " + e.getMessage();
                if (e instanceof com.goydevv.studrblx.net.AuthRequiredException) {
                    why += " - add an Open Cloud API key (asset-delivery read) or a .ROBLOSECURITY cookie in Roblox Account settings.";
                }
                AnimLibrary.logOnce($id, "Animation " + $id + " failed to load: " + why);
            }
        } finally {
            pending.remove(Long.valueOf($id));
        }
    }

    public final boolean failed(long id) {
        return failed.contains(Long.valueOf(id));
    }

    public final AnimClip fromInstance(Instance seq) {
        Intrinsics.checkNotNullParameter(seq, "seq");
        return AnimClip.INSTANCE.from(seq);
    }
}

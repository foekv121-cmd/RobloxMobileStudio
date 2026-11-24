package com.goydevv.studrblx.core;

import com.goydevv.studrblx.lua.LuaRuntime;
import com.goydevv.studrblx.model.DataModel;
import com.goydevv.studrblx.model.Instance;
import java.util.ArrayList;
import java.util.List;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Unit;
import kotlin.collections.ArrayDeque;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaValue;

/* JADX INFO: compiled from: UndoManager.kt */
/* JADX INFO: loaded from: classes4.dex */
public final class UndoManager {
    private ArrayList<Op> batch;
    private String batchLabel;
    private final ArrayDeque<Step> redoStack;
    private final Studio studio;
    private boolean suspended;
    private final ArrayDeque<Step> undoStack;

    /* JADX INFO: compiled from: UndoManager.kt */
    public static abstract class Op {
        public /* synthetic */ Op(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX INFO: compiled from: UndoManager.kt */
        public static final class Prop extends Op {
            private final boolean hadOld;
            private final Instance inst;
            private final String name;
            private final Object newValue;
            private final Object old;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public Prop(Instance inst, String name, Object old, Object obj, boolean hadOld) {
                super(null);
                Intrinsics.checkNotNullParameter(inst, "inst");
                Intrinsics.checkNotNullParameter(name, "name");
                this.inst = inst;
                this.name = name;
                this.old = old;
                this.newValue = obj;
                this.hadOld = hadOld;
            }

            public final boolean getHadOld() {
                return this.hadOld;
            }

            public final Instance getInst() {
                return this.inst;
            }

            public final String getName() {
                return this.name;
            }

            public final Object getNew() {
                return this.newValue;
            }

            public final Object getOld() {
                return this.old;
            }
        }

        private Op() {
        }

        /* JADX INFO: compiled from: UndoManager.kt */
        public static final class Parent extends Op {
            private final Instance inst;
            private final Instance newParent;
            private final int oldIndex;
            private final Instance oldParent;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public Parent(Instance inst, Instance oldParent, int oldIndex, Instance newParent) {
                super(null);
                Intrinsics.checkNotNullParameter(inst, "inst");
                this.inst = inst;
                this.oldParent = oldParent;
                this.oldIndex = oldIndex;
                this.newParent = newParent;
            }

            public final Instance getInst() {
                return this.inst;
            }

            public final Instance getNewParent() {
                return this.newParent;
            }

            public final int getOldIndex() {
                return this.oldIndex;
            }

            public final Instance getOldParent() {
                return this.oldParent;
            }
        }
    }

    public UndoManager(Studio studio) {
        Intrinsics.checkNotNullParameter(studio, "studio");
        this.studio = studio;
        this.undoStack = new ArrayDeque<>();
        this.redoStack = new ArrayDeque<>();
        this.batchLabel = "";
    }

    /* JADX INFO: compiled from: UndoManager.kt */
    private static final class Step {
        private final String label;
        private final List<Op> ops;

        /* JADX WARN: Multi-variable type inference failed */
        public Step(String label, List<? extends Op> ops) {
            Intrinsics.checkNotNullParameter(label, "label");
            Intrinsics.checkNotNullParameter(ops, "ops");
            this.label = label;
            this.ops = (List<Op>) (List<?>) ops;
        }

        public final String getLabel() {
            return this.label;
        }

        public final List<Op> getOps() {
            return this.ops;
        }
    }

    public final boolean getSuspended() {
        return this.suspended;
    }

    public final void setSuspended(boolean z) {
        this.suspended = z;
    }

    public final boolean getCanUndo() {
        return !this.undoStack.isEmpty();
    }

    public final boolean getCanRedo() {
        return !this.redoStack.isEmpty();
    }

    public final void begin(String label) {
        Intrinsics.checkNotNullParameter(label, "label");
        if (this.batch == null) {
            this.batch = new ArrayList<>();
            this.batchLabel = label;
        }
    }

    public final void commit() {
        ArrayList<Op> arrayList = this.batch;
        if (arrayList == null) {
            return;
        }
        this.batch = null;
        if (!arrayList.isEmpty()) {
            push(new Step(this.batchLabel, arrayList));
        }
    }

    public final void record(Op op) {
        Intrinsics.checkNotNullParameter(op, "op");
        if (this.suspended || this.studio.isPlaying()) {
            return;
        }
        ArrayList<Op> arrayList = this.batch;
        if (arrayList != null) {
            arrayList.add(op);
        } else {
            push(new Step("Edit", CollectionsKt.listOf(op)));
        }
    }

    private final void push(Step s) {
        this.undoStack.addLast(s);
        if (this.undoStack.size() > 200) {
            this.undoStack.removeFirst();
        }
        this.redoStack.clear();
        this.studio.markDirty();
    }

    public final void clear() {
        this.undoStack.clear();
        this.redoStack.clear();
        this.batch = null;
    }

    public final void undo() {
        Step s;
        synchronized (this.studio.getLock()) {
            s = this.undoStack.removeLastOrNull();
        }
        if (s == null) {
            return;
        }
        apply(s, true);
        this.redoStack.addLast(s);
        OutputLog.INSTANCE.info("Undo: " + s.getLabel());
        notifyPlugins("OnUndo", s.getLabel());
    }

    public final void redo() {
        Step s;
        synchronized (this.studio.getLock()) {
            s = this.redoStack.removeLastOrNull();
        }
        if (s == null) {
            return;
        }
        apply(s, false);
        this.undoStack.addLast(s);
        OutputLog.INSTANCE.info("Redo: " + s.getLabel());
        notifyPlugins("OnRedo", s.getLabel());
    }

    private final void notifyPlugins(String event, String label) {
        synchronized (this.studio.getLock()) {
            Instance instanceFindService = this.studio.getEditModel().findService("ChangeHistoryService");
            if (instanceFindService == null) {
                return;
            }
            try {
                LuaRuntime editRuntime = this.studio.getEditRuntime();
                LuaString luaStringValueOf = LuaValue.valueOf(label);
                Intrinsics.checkNotNullExpressionValue(luaStringValueOf, "valueOf(...)");
                editRuntime.fire(instanceFindService, event, luaStringValueOf);
            } catch (Exception e) {
            }
            Unit unit = Unit.INSTANCE;
        }
    }

    private final void apply(Step s, boolean reverse) {
        synchronized (this.studio.getLock()) {
            this.suspended = true;
            try {
                for (Op op : reverse ? CollectionsKt.asReversed(s.getOps()) : s.getOps()) {
                    if (op instanceof Op.Prop) {
                        if (reverse) {
                            if (((Op.Prop) op).getHadOld()) {
                                Instance.set$default(((Op.Prop) op).getInst(), ((Op.Prop) op).getName(), ((Op.Prop) op).getOld(), false, 4, null);
                            } else {
                                ((Op.Prop) op).getInst().getProps().remove(((Op.Prop) op).getName());
                                DataModel dataModelRoot = ((Op.Prop) op).getInst().root();
                                if (dataModelRoot != null) {
                                    dataModelRoot.firePropChanged(((Op.Prop) op).getInst(), ((Op.Prop) op).getName());
                                }
                            }
                        } else {
                            Instance.set$default(((Op.Prop) op).getInst(), ((Op.Prop) op).getName(), ((Op.Prop) op).getNew(), false, 4, null);
                        }
                    } else {
                        if (!(op instanceof Op.Parent)) {
                            throw new NoWhenBranchMatchedException();
                        }
                        if (reverse) {
                            ((Op.Parent) op).getInst().setDestroyed(false);
                            ((Op.Parent) op).getInst().setParent(((Op.Parent) op).getOldParent(), ((Op.Parent) op).getOldIndex());
                        } else {
                            Instance.setParent$default(((Op.Parent) op).getInst(), ((Op.Parent) op).getNewParent(), 0, 2, null);
                        }
                    }
                }
                this.suspended = false;
                Unit unit = Unit.INSTANCE;
            } catch (Throwable th) {
                this.suspended = false;
                throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(th);
            }
        }
        this.studio.markDirty();
        this.studio.pruneSelection();
    }
}

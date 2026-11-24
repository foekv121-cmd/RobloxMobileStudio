package com.goydevv.studrblx.render;


/* JADX INFO: compiled from: StaticBatches.kt */
/* JADX INFO: loaded from: classes8.dex */
public final /* data */ class TexBind {
    private final int color;
    private final int normal;
    private final float scale;

    public static /* synthetic */ TexBind copy$default(TexBind texBind, int i, int i2, float f, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = texBind.color;
        }
        if ((i3 & 2) != 0) {
            i2 = texBind.normal;
        }
        if ((i3 & 4) != 0) {
            f = texBind.scale;
        }
        return texBind.copy(i, i2, f);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final int component1() {
        return this.color;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final int component2() {
        return this.normal;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final float component3() {
        return this.scale;
    }

    public final TexBind copy(int color, int normal, float scale) {
        return new TexBind(color, normal, scale);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof TexBind)) {
            return false;
        }
        TexBind texBind = (TexBind) other;
        return this.color == texBind.color && this.normal == texBind.normal && Float.compare(this.scale, texBind.scale) == 0;
    }

    public int hashCode() {
        return (((Integer.hashCode(this.color) * 31) + Integer.hashCode(this.normal)) * 31) + Float.hashCode(this.scale);
    }

    public String toString() {
        return "TexBind(color=" + this.color + ", normal=" + this.normal + ", scale=" + this.scale + ")";
    }

    public TexBind(int color, int normal, float scale) {
        this.color = color;
        this.normal = normal;
        this.scale = scale;
    }

    public final int getColor() {
        return this.color;
    }

    public final int getNormal() {
        return this.normal;
    }

    public final float getScale() {
        return this.scale;
    }
}

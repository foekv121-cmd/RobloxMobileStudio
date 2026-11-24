package com.goydevv.studrblx.net;

import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: RobloxCloud.kt */
/* JADX INFO: loaded from: classes6.dex */
public final /* data */ class CloudPlace {
    private final String name;
    private final long placeId;
    private final long universeId;

    public static /* synthetic */ CloudPlace copy$default(CloudPlace cloudPlace, long j, long j2, String str, int i, Object obj) {
        if ((i & 1) != 0) {
            j = cloudPlace.universeId;
        }
        long j3 = j;
        if ((i & 2) != 0) {
            j2 = cloudPlace.placeId;
        }
        long j4 = j2;
        if ((i & 4) != 0) {
            str = cloudPlace.name;
        }
        return cloudPlace.copy(j3, j4, str);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final long component1() {
        return this.universeId;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final long component2() {
        return this.placeId;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final String component3() {
        return this.name;
    }

    public final CloudPlace copy(long universeId, long placeId, String name) {
        Intrinsics.checkNotNullParameter(name, "name");
        return new CloudPlace(universeId, placeId, name);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CloudPlace)) {
            return false;
        }
        CloudPlace cloudPlace = (CloudPlace) other;
        return this.universeId == cloudPlace.universeId && this.placeId == cloudPlace.placeId && Intrinsics.areEqual(this.name, cloudPlace.name);
    }

    public int hashCode() {
        return (((Long.hashCode(this.universeId) * 31) + Long.hashCode(this.placeId)) * 31) + this.name.hashCode();
    }

    public String toString() {
        return "CloudPlace(universeId=" + this.universeId + ", placeId=" + this.placeId + ", name=" + this.name + ")";
    }

    public CloudPlace(long universeId, long placeId, String name) {
        Intrinsics.checkNotNullParameter(name, "name");
        this.universeId = universeId;
        this.placeId = placeId;
        this.name = name;
    }

    public final String getName() {
        return this.name;
    }

    public final long getPlaceId() {
        return this.placeId;
    }

    public final long getUniverseId() {
        return this.universeId;
    }
}

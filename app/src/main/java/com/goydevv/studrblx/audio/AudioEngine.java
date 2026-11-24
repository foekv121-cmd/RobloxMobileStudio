package com.goydevv.studrblx.audio;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import com.goydevv.studrblx.core.OutputLog;
import com.goydevv.studrblx.core.Settings;
import com.goydevv.studrblx.core.Studio;
import com.goydevv.studrblx.engine.PlaySession;
import com.goydevv.studrblx.model.CFrame;
import com.goydevv.studrblx.model.EnumValue;
import com.goydevv.studrblx.model.Instance;
import com.goydevv.studrblx.model.PartData;
import com.goydevv.studrblx.model.Vector3;
import com.goydevv.studrblx.net.AssetFetcher;
import com.goydevv.studrblx.render.FlyCamera;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Unit;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

/* JADX INFO: compiled from: AudioEngine.kt */
/* JADX INFO: loaded from: classes3.dex */
public final class AudioEngine {
    public static final AudioEngine INSTANCE = new AudioEngine();
    private static final int MAX_VOICES = 28;
    private static final Handler h;
    private static final HashSet<Long> missing;
    private static volatile Function2<? super Instance, ? super Integer, Unit> onDidLoop;
    private static volatile Function1<? super Instance, Unit> onEnded;
    private static volatile Function1<? super Instance, Unit> onLoaded;
    private static volatile Preview preview;
    private static final CopyOnWriteArrayList<Function0<Unit>> previewListeners;
    private static MediaPlayer previewPlayer;
    private static final HandlerThread thread;
    private static final CopyOnWriteArrayList<Instance> transientSounds;
    private static final IdentityHashMap<Instance, Voice> voices;

    private static volatile AudioManager audioManager;
    private static volatile AudioFocusRequest audioFocusRequest;
    private static volatile boolean hasAudioFocus;
    private static volatile boolean appPaused;
    private static volatile float duckFactor = 1.0f;

    private static final AudioManager.OnAudioFocusChangeListener focusListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                duckFactor = 1.0f;
                hasAudioFocus = true;
                if (!appPaused) {
                    INSTANCE.resumeAllVoicesOnHandler();
                }
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                duckFactor = 0.25f;
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                duckFactor = 0.0f;
                INSTANCE.pauseAllVoicesOnHandler();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                hasAudioFocus = false;
                duckFactor = 0.0f;
                INSTANCE.pauseAllVoicesOnHandler();
            }
        }
    };

    /* JADX INFO: compiled from: AudioEngine.kt */
    public enum PreviewState {
        LOADING,
        PLAYING,
        PAUSED,
        STOPPED,
        ERROR;

        private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries(values());

        public static EnumEntries<PreviewState> getEntries() {
            return $ENTRIES;
        }
    }

    /* JADX INFO: compiled from: AudioEngine.kt */
    public static final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[PreviewState.values().length];
            try {
                iArr[PreviewState.PLAYING.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[PreviewState.PAUSED.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[PreviewState.STOPPED.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[PreviewState.LOADING.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[PreviewState.ERROR.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    private static final class ListenerPose {
        final Vector3 pos;
        final Vector3 right;

        ListenerPose(Vector3 pos, Vector3 right) {
            this.pos = pos;
            this.right = right;
        }
    }

    private AudioEngine() {
    }

    static {
        HandlerThread handlerThread = new HandlerThread("audio");
        handlerThread.start();
        thread = handlerThread;
        h = new Handler(thread.getLooper());
        voices = new IdentityHashMap<>();
        missing = new HashSet<>();
        transientSounds = new CopyOnWriteArrayList<>();
        previewListeners = new CopyOnWriteArrayList<>();
    }

    public final void init(Context ctx) {
        if (ctx == null) {
            return;
        }
        try {
            Object svc = ctx.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            if (svc instanceof AudioManager) {
                audioManager = (AudioManager) svc;
            }
        } catch (Throwable ignored) {
        }
    }

    public final void requestAudioFocus() {
        AudioManager am = audioManager;
        if (am == null || hasAudioFocus) {
            return;
        }
        try {
            int res;
            if (Build.VERSION.SDK_INT >= 26) {
                AudioAttributes attrs = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build();
                AudioFocusRequest req = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                        .setAudioAttributes(attrs)
                        .setOnAudioFocusChangeListener(focusListener)
                        .build();
                audioFocusRequest = req;
                res = am.requestAudioFocus(req);
            } else {
                res = am.requestAudioFocus(focusListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
            }
            if (res == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                hasAudioFocus = true;
                duckFactor = 1.0f;
            }
        } catch (Throwable ignored) {
        }
    }

    public final void abandonAudioFocus() {
        AudioManager am = audioManager;
        if (am == null || !hasAudioFocus) {
            return;
        }
        try {
            if (Build.VERSION.SDK_INT >= 26 && audioFocusRequest != null) {
                am.abandonAudioFocusRequest(audioFocusRequest);
            } else {
                am.abandonAudioFocus(focusListener);
            }
        } catch (Throwable ignored) {
        }
        hasAudioFocus = false;
        duckFactor = 1.0f;
    }

    public final void onAppPause() {
        appPaused = true;
        pauseAllVoicesOnHandler();
        h.post(new Runnable() {
            @Override
            public void run() {
                try {
                    MediaPlayer pm = previewPlayer;
                    if (pm != null && pm.isPlaying()) {
                        pm.pause();
                    }
                } catch (Throwable ignored) {
                }
            }
        });
        abandonAudioFocus();
    }

    public final void onAppResume() {
        appPaused = false;
        if (!voices.isEmpty()) {
            requestAudioFocus();
            resumeAllVoicesOnHandler();
        }
    }

    public final void onAppDestroy() {
        appPaused = true;
        stopAll();
        stopPreview();
        abandonAudioFocus();
    }

    private final void pauseAllVoicesOnHandler() {
        final ArrayList<Voice> snap = new ArrayList<>(voices.values());
        h.post(new Runnable() {
            @Override
            public void run() {
                for (Voice v : snap) {
                    if (v == null || v.getReleased()) continue;
                    try {
                        MediaPlayer mp = v.getMp();
                        if (mp != null && v.getPrepared() && mp.isPlaying()) {
                            mp.pause();
                        }
                        v.setLoudness(0.0f);
                    } catch (Throwable ignored) {
                    }
                }
            }
        });
    }

    private final void resumeAllVoicesOnHandler() {
        final ArrayList<Voice> snap = new ArrayList<>(voices.values());
        h.post(new Runnable() {
            @Override
            public void run() {
                if (appPaused) return;
                for (Voice v : snap) {
                    if (v == null || v.getReleased() || v.getEnded() || !v.getWantPlay()) continue;
                    if (v.getLastSpeed() <= 0.001f) continue;
                    try {
                        MediaPlayer mp = v.getMp();
                        if (mp != null && v.getPrepared() && !mp.isPlaying()) {
                            mp.start();
                            INSTANCE.setSpeed(v, mp, v.getLastSpeed());
                            INSTANCE.tick(v);
                        }
                    } catch (Throwable ignored) {
                    }
                }
            }
        });
    }

    public final void enqueueTransientSound(Instance s) {
        if (s != null) {
            transientSounds.add(s);
        }
    }

    public final void removeTransientSound(Instance s) {
        if (s != null) {
            transientSounds.remove(s);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: compiled from: AudioEngine.kt */
    static final class Voice {
        private volatile float appliedSpeed;
        private volatile boolean ended;
        private volatile boolean failed;
        private final long id;
        private volatile float lastL;
        private double lastPushedPos;
        private volatile float lastR;
        private volatile float lastSpeed;
        private volatile double length;
        private volatile boolean loadedNotified;
        private volatile int loopCount;
        private volatile boolean looped;
        private volatile float loudness;
        private volatile MediaPlayer mp;
        private volatile int notifiedLoopCount;
        private volatile double pendingSeek;
        private volatile double position;
        private volatile boolean prepared;
        private volatile boolean released;
        private double seek;
        private int seekSeq;
        private int seq;
        private final Instance sound;
        private volatile boolean ticking;
        private volatile boolean wantPlay;

        public Voice(Instance sound, long id) {
            Intrinsics.checkNotNullParameter(sound, "sound");
            this.sound = sound;
            this.id = id;
            this.lastL = -1.0f;
            this.lastR = -1.0f;
            this.lastSpeed = -1.0f;
            this.appliedSpeed = 1.0f;
            this.seek = -1.0d;
            this.pendingSeek = -1.0d;
            Object obj = this.sound.getAttributes().get("__playSeq");
            Integer num = obj instanceof Integer ? (Integer) obj : null;
            this.seq = num != null ? num.intValue() : 0;
            Object sobj = this.sound.getAttributes().get("__seekSeq");
            Integer snum = sobj instanceof Integer ? (Integer) sobj : null;
            this.seekSeq = snum != null ? snum.intValue() : 0;
        }

        public final long getId() {
            return this.id;
        }

        public final Instance getSound() {
            return this.sound;
        }

        public final MediaPlayer getMp() {
            return this.mp;
        }

        public final void setMp(MediaPlayer mediaPlayer) {
            this.mp = mediaPlayer;
        }

        public final boolean getPrepared() {
            return this.prepared;
        }

        public final void setPrepared(boolean z) {
            this.prepared = z;
        }

        public final boolean getReleased() {
            return this.released;
        }

        public final void setReleased(boolean z) {
            this.released = z;
        }

        public final boolean getLooped() {
            return this.looped;
        }

        public final void setLooped(boolean z) {
            this.looped = z;
        }

        public final boolean getTicking() {
            return this.ticking;
        }

        public final void setTicking(boolean z) {
            this.ticking = z;
        }

        public final float getLastL() {
            return this.lastL;
        }

        public final float getLastR() {
            return this.lastR;
        }

        public final float getLastSpeed() {
            return this.lastSpeed;
        }

        public final float getAppliedSpeed() {
            return this.appliedSpeed;
        }

        public final void setAppliedSpeed(float f) {
            this.appliedSpeed = f;
        }

        public final float getLoudness() {
            return this.loudness;
        }

        public final void setLoudness(float f) {
            this.loudness = f;
        }

        public final boolean getWantPlay() {
            return this.wantPlay;
        }

        public final void setLastL(float f) {
            this.lastL = f;
        }

        public final void setLastR(float f) {
            this.lastR = f;
        }

        public final void setLastSpeed(float f) {
            this.lastSpeed = f;
        }

        public final void setWantPlay(boolean z) {
            this.wantPlay = z;
        }

        public final double getPosition() {
            return this.position;
        }

        public final void setPosition(double d) {
            this.position = d;
        }

        public final double getLength() {
            return this.length;
        }

        public final void setLength(double d) {
            this.length = d;
        }

        public final boolean getEnded() {
            return this.ended;
        }

        public final void setEnded(boolean z) {
            this.ended = z;
        }

        public final boolean getLoadedNotified() {
            return this.loadedNotified;
        }

        public final void setLoadedNotified(boolean z) {
            this.loadedNotified = z;
        }

        public final boolean getFailed() {
            return this.failed;
        }

        public final void setFailed(boolean z) {
            this.failed = z;
        }

        public final double getLastPushedPos() {
            return this.lastPushedPos;
        }

        public final void setLastPushedPos(double d) {
            this.lastPushedPos = d;
        }

        public final double getSeek() {
            return this.seek;
        }

        public final void setSeek(double d) {
            this.seek = d;
        }

        public final double getPendingSeek() {
            return this.pendingSeek;
        }

        public final void setPendingSeek(double d) {
            this.pendingSeek = d;
        }

        public final double consumePendingSeek() {
            double s = this.pendingSeek;
            if (s >= 0.0d) {
                this.pendingSeek = -1.0d;
            }
            return s;
        }

        public final int getSeq() {
            return this.seq;
        }

        public final void setSeq(int i) {
            this.seq = i;
        }

        public final int getSeekSeq() {
            return this.seekSeq;
        }

        public final void setSeekSeq(int i) {
            this.seekSeq = i;
        }

        public final int getLoopCount() {
            return this.loopCount;
        }

        public final int incrementLoopCount() {
            int next = this.loopCount + 1;
            this.loopCount = next;
            return next;
        }

        public final void resetLoopCount() {
            this.loopCount = 0;
            this.notifiedLoopCount = 0;
        }

        public final int getNotifiedLoopCount() {
            return this.notifiedLoopCount;
        }

        public final void setNotifiedLoopCount(int i) {
            this.notifiedLoopCount = i;
        }
    }

    public final Function1<Instance, Unit> getOnLoaded() {
        return (Function1<Instance, Unit>) onLoaded;
    }

    public final void setOnLoaded(Function1<? super Instance, Unit> function1) {
        onLoaded = function1;
    }

    public final Function1<Instance, Unit> getOnEnded() {
        return (Function1<Instance, Unit>) onEnded;
    }

    public final void setOnEnded(Function1<? super Instance, Unit> function1) {
        onEnded = function1;
    }

    public final Function2<Instance, Integer, Unit> getOnDidLoop() {
        return (Function2<Instance, Integer, Unit>) onDidLoop;
    }

    public final void setOnDidLoop(Function2<? super Instance, ? super Integer, Unit> function2) {
        onDidLoop = function2;
    }

    private final Long soundId(Instance s) {
        return AssetFetcher.INSTANCE.idOf(s.get("SoundId"));
    }

    private final float num(Instance i, String k, float d) {
        Object obj = i != null ? i.get(k) : null;
        Number number = obj instanceof Number ? (Number) obj : null;
        return number != null ? number.floatValue() : d;
    }

    private final float computeRolloffGain(float d, float mn, float mx, int mode, float rollScale) {
        if (d <= mn) {
            return 1.0f;
        }
        if (d >= mx) {
            return 0.0f;
        }
        float denom = Math.max(mx - mn, 0.0001f);
        float lin = Math.max(0.0f, 1.0f - ((d - mn) / denom));
        float rs = Math.max(rollScale, 0.0001f);
        float inv = mn / (mn + ((d - mn) * rs));
        float gain;
        switch (mode) {
            case 1: // Linear
                gain = lin;
                break;
            case 2: // LinearSquare
                gain = lin * lin;
                break;
            case 3: // InverseTapered
                gain = Math.min(inv, lin * lin);
                break;
            case 0: // Inverse
            default:
                gain = inv;
                break;
        }
        return RangesKt.coerceIn(gain, 0.0f, 1.0f);
    }

    private final float computeSoundGroupGain(Instance sound) {
        Object sgObj = sound.get("SoundGroup");
        if (!(sgObj instanceof Instance)) {
            return 1.0f;
        }
        float gain = 1.0f;
        Set<Instance> visited = Collections.newSetFromMap(new IdentityHashMap<Instance, Boolean>());
        Instance cur = (Instance) sgObj;
        int depth = 0;
        while (cur != null && depth < 16 && visited.add(cur)) {
            gain *= RangesKt.coerceIn(num(cur, "Volume", 0.5f), 0.0f, 10.0f);
            Instance parent = cur.getParent();
            if (parent != null && "SoundGroup".equals(parent.getClassName())) {
                cur = parent;
            } else {
                cur = null;
            }
            depth++;
        }
        return gain;
    }

    private final ListenerPose resolveListener(Instance soundService, FlyCamera cam) {
        Vector3 defaultPos = cam.getPos();
        Vector3 defaultRight = cam.getRight();
        if (soundService == null) {
            return new ListenerPose(defaultPos, defaultRight);
        }
        Object ltObj = soundService.getAttributes().get("__listenerType");
        int lt = (ltObj instanceof Integer) ? ((Integer) ltObj).intValue() : 0;
        Object lv = soundService.getAttributes().get("__listenerValue");
        if (lt == 1 && lv instanceof CFrame) {
            CFrame cf = (CFrame) lv;
            return new ListenerPose(cf.getPosition(), cf.getRightVector());
        }
        if ((lt == 2 || lt == 3) && lv instanceof Instance) {
            Instance obj = (Instance) lv;
            CFrame objCf = null;
            if (obj.isA("BasePart")) {
                objCf = PartData.INSTANCE.cframe(obj);
            } else if ("Attachment".equals(obj.getClassName())) {
                Instance bp = obj.getParent();
                if (bp != null && bp.isA("BasePart")) {
                    Object cfObj = obj.get("CFrame");
                    CFrame localCf = (cfObj instanceof CFrame) ? (CFrame) cfObj : CFrame.INSTANCE.getIDENTITY();
                    objCf = PartData.INSTANCE.cframe(bp).times(localCf);
                }
            } else {
                Object cfObj = obj.get("CFrame");
                if (cfObj instanceof CFrame) {
                    objCf = (CFrame) cfObj;
                }
            }
            if (objCf != null) {
                if (lt == 2) {
                    return new ListenerPose(objCf.getPosition(), defaultRight);
                }
                return new ListenerPose(objCf.getPosition(), objCf.getRightVector());
            }
        }
        return new ListenerPose(defaultPos, defaultRight);
    }

    private final float computePcmLoudness(byte[] pcm, int len) {
        if (pcm == null || len < 2) {
            return 0.0f;
        }
        int samples = len / 2;
        double sumSq = 0.0d;
        for (int i = 0; i + 1 < len; i += 2) {
            short sample = (short) ((pcm[i] & 0xFF) | (pcm[i + 1] << 8));
            double norm = sample / 32768.0d;
            sumSq += norm * norm;
        }
        double rms = Math.sqrt(sumSq / samples);
        return (float) Math.min(1000.0d, Math.max(0.0d, rms * 1000.0d));
    }

    private final Vector3 resolveEmitterPosition(Instance s) {
        Instance parent = s.getParent();
        if (parent != null) {
            if (parent.isA("BasePart")) {
                return PartData.INSTANCE.cframe(parent).getPosition();
            }
            if ("Attachment".equals(parent.getClassName())) {
                Instance bp = parent.getParent();
                if (bp != null && bp.isA("BasePart")) {
                    Object cfObj = parent.get("CFrame");
                    Vector3 localPos = (cfObj instanceof CFrame) ? ((CFrame) cfObj).getPosition() : Vector3.INSTANCE.getZERO();
                    return PartData.INSTANCE.cframe(bp).times(localPos);
                }
            }
        }
        Object lastPos = s.getAttributes().get("__lastEmitterPos");
        if (lastPos instanceof Vector3) {
            return (Vector3) lastPos;
        }
        return null;
    }

    public final void update(Studio studio) {
        Intrinsics.checkNotNullParameter(studio, "studio");
        PlaySession play = studio.getPlay();
        if (play == null) {
            if (!voices.isEmpty() || !transientSounds.isEmpty()) {
                stopAll();
            }
            return;
        }
        List<Instance> audible = studio.audibleSounds();
        ArrayList<Instance> allSounds = new ArrayList<>(audible.size() + transientSounds.size());
        Set<Instance> seen = Collections.newSetFromMap(new IdentityHashMap<Instance, Boolean>());
        for (Instance s : audible) {
            if (s != null && seen.add(s)) {
                allSounds.add(s);
            }
        }
        for (Instance ts : transientSounds) {
            if (ts != null && seen.add(ts)) {
                allSounds.add(ts);
            }
        }

        Set<Instance> alive = Collections.newSetFromMap(new IdentityHashMap<Instance, Boolean>());
        Instance ss = studio.getModel().findService("SoundService");
        ListenerPose listener = resolveListener(ss, studio.getCamera());
        float rollScale = ss != null ? RangesKt.coerceIn(num(ss, "RolloffScale", 1.0f), 0.0f, 10.0f) : 1.0f;
        float master = (Settings.INSTANCE.getVolume() / 10.0f) * duckFactor;

        for (Instance s : allSounds) {
            boolean isTransient = Boolean.TRUE.equals(s.getAttributes().get("__oneShot"))
                    || Boolean.TRUE.equals(s.getAttributes().get("__playOnRemoveActive"))
                    || transientSounds.contains(s);
            if (s.getParent() == null && !isTransient) {
                continue;
            }
            alive.add(s);
            boolean playing = Intrinsics.areEqual(s.get("Playing"), Boolean.TRUE)
                    || Intrinsics.areEqual(s.get("IsPlaying"), Boolean.TRUE);
            Voice v = voices.get(s);
            Long id = soundId(s);
            if (v != null && (id == null || v.getId() != id.longValue())) {
                release(v);
                voices.remove(s);
                v = null;
            }

            if (!playing) {
                if (v != null) {
                    if (v.getWantPlay()) {
                        v.setWantPlay(false);
                        v.setLoudness(0.0f);
                        final boolean stopped = Boolean.TRUE.equals(s.getAttributes().get("__stopFlag"))
                                || num(s, "TimePosition", 0.0f) == 0.0f;
                        if (stopped) {
                            v.setPosition(0.0d);
                            v.setLastPushedPos(0.0d);
                            v.resetLoopCount();
                        }
                        final Voice vvPause = v;
                        h.post(new Runnable() {
                            @Override
                            public void run() {
                                AudioEngine.update$lambda$2(vvPause, stopped);
                            }
                        });
                    }
                    Object ssqObj = s.getAttributes().get("__seekSeq");
                    int sseq = (ssqObj instanceof Integer) ? ((Integer) ssqObj).intValue() : 0;
                    if (sseq != v.getSeekSeq()) {
                        v.setSeekSeq(sseq);
                        final double tp = Math.max(0.0d, (double) num(s, "TimePosition", 0.0f));
                        v.setPendingSeek(tp);
                        v.setPosition(tp);
                        v.setLastPushedPos(tp);
                        final Voice vvSeek = v;
                        h.post(new Runnable() {
                            @Override
                            public void run() {
                                AudioEngine.seekVoiceOnHandler(vvSeek, tp, false);
                            }
                        });
                    }
                }
                s.set("PlaybackLoudness", Float.valueOf(0.0f), false);
                continue;
            }

            if (id == null) {
                continue;
            }

            if (!appPaused && !hasAudioFocus) {
                requestAudioFocus();
            }

            if (v == null) {
                if (voices.size() >= MAX_VOICES) {
                    continue;
                }
                boolean cached = AssetFetcher.INSTANCE.isCached(id.longValue());
                if (!cached) {
                    AssetFetcher.INSTANCE.request(id.longValue());
                    if (!missing.contains(id) && AssetFetcher.INSTANCE.lastError(id.longValue()) != null) {
                        missing.add(id);
                        OutputLog.INSTANCE.warn("Couldn't load sound " + s.fullName() + " (rbxassetid://" + id + "): " + AssetFetcher.INSTANCE.lastError(id.longValue()));
                    }
                    continue;
                }
                Voice created = new Voice(s, id.longValue());
                voices.put(s, created);
                Object stp = s.getAttributes().remove("__scriptTimePos");
                double startPos = (stp instanceof Number) ? Math.max(0.0d, ((Number) stp).doubleValue()) : Math.max(0.0d, (double) num(s, "TimePosition", 0.0f));
                created.setSeek(startPos);
                if (startPos > 0.0d) {
                    created.setPendingSeek(startPos);
                }
                create(created);
                v = created;
            }

            if (v.getFailed()) {
                if (isTransient) {
                    transientSounds.remove(s);
                }
                continue;
            }

            float vol = RangesKt.coerceIn(num(s, "Volume", 0.5f), 0.0f, 10.0f);
            vol *= computeSoundGroupGain(s);
            vol *= master;

            float left = 1.0f;
            float right = 1.0f;
            Vector3 emitter = resolveEmitterPosition(s);
            if (emitter != null) {
                Vector3 dir = emitter.minus(listener.pos);
                float d = dir.getMagnitude();
                float mn = RangesKt.coerceAtLeast(num(s, "RollOffMinDistance", num(s, "xmlRead_MinDistance_3", 10.0f)), 0.1f);
                float mx = RangesKt.coerceAtLeast(num(s, "RollOffMaxDistance", num(s, "xmlRead_MaxDistance_3", 10000.0f)), mn + 0.01f);
                Object rmObj = s.get("RollOffMode");
                int mode = 0;
                if (rmObj instanceof EnumValue) {
                    mode = ((EnumValue) rmObj).getValue();
                } else if (rmObj instanceof Number) {
                    mode = ((Number) rmObj).intValue();
                }
                vol *= computeRolloffGain(d, mn, mx, mode, rollScale);
                if (d >= 0.5f) {
                    float pan = RangesKt.coerceIn(dir.getUnit().dot(listener.right), -1.0f, 1.0f) * 0.7f;
                    left = 1.0f - Math.max(0.0f, pan);
                    right = 1.0f + Math.min(0.0f, pan);
                }
            }

            float l = RangesKt.coerceIn(vol * left, 0.0f, 1.0f);
            float r = RangesKt.coerceIn(vol * right, 0.0f, 1.0f);
            float speed = RangesKt.coerceIn(num(s, "PlaybackSpeed", num(s, "Pitch", 1.0f)), 0.0f, 20.0f);
            boolean loop = Intrinsics.areEqual(s.get("Looped"), Boolean.TRUE);

            Object seqObj = s.getAttributes().get("__playSeq");
            int seq = (seqObj instanceof Integer) ? ((Integer) seqObj).intValue() : 0;
            if (seq != v.getSeq()) {
                v.setSeq(seq);
                v.setEnded(false);
                v.resetLoopCount();
                Object stpObj = s.getAttributes().remove("__scriptTimePos");
                final double targetSeek = (stpObj instanceof Number) ? Math.max(0.0d, ((Number) stpObj).doubleValue()) : 0.0d;
                v.setPendingSeek(targetSeek);
                v.setPosition(targetSeek);
                v.setLastPushedPos(targetSeek);
                final Voice vvPlay = v;
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        AudioEngine.seekVoiceOnHandler(vvPlay, targetSeek, true);
                    }
                });
            }

            Object ssqObj = s.getAttributes().get("__seekSeq");
            int sseq = (ssqObj instanceof Integer) ? ((Integer) ssqObj).intValue() : 0;
            if (sseq != v.getSeekSeq()) {
                v.setSeekSeq(sseq);
                final double tp = Math.max(0.0d, (double) num(s, "TimePosition", 0.0f));
                s.getAttributes().remove("__scriptTimePos");
                v.setPendingSeek(tp);
                v.setPosition(tp);
                v.setLastPushedPos(tp);
                final Voice vvSeek = v;
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        AudioEngine.seekVoiceOnHandler(vvSeek, tp, false);
                    }
                });
            }

            boolean needStart = !v.getWantPlay();
            boolean volDirty = Math.abs(l - v.getLastL()) > 0.01f || Math.abs(r - v.getLastR()) > 0.01f;
            boolean speedDirty = Math.abs(speed - v.getLastSpeed()) > 0.001f;
            boolean loopDirty = loop != v.getLooped();
            if (needStart || volDirty || speedDirty || loopDirty) {
                final boolean restart = needStart;
                v.setWantPlay(true);
                v.setLastL(l);
                v.setLastR(r);
                v.setLastSpeed(speed);
                v.setLooped(loop);
                final Voice vvApply = v;
                final float lApply = l;
                final float rApply = r;
                final float speedApply = speed;
                final boolean loopApply = loop;
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        AudioEngine.INSTANCE.apply(vvApply, lApply, rApply, speedApply, loopApply, restart);
                    }
                });
            }

            if (v.getLength() > 0.0d && !v.getLoadedNotified()) {
                v.setLoadedNotified(true);
                s.set("TimeLength", Double.valueOf(v.getLength()), false);
                s.set("IsLoaded", true, false);
                Function1<? super Instance, Unit> loadedCb = onLoaded;
                if (loadedCb != null) {
                    loadedCb.invoke(s);
                }
            }

            int lc = v.getLoopCount();
            if (lc > v.getNotifiedLoopCount()) {
                v.setNotifiedLoopCount(lc);
                s.getAttributes().put("__loopCount", Integer.valueOf(lc));
                Function2<? super Instance, ? super Integer, Unit> loopCb = onDidLoop;
                if (loopCb != null) {
                    loopCb.invoke(s, Integer.valueOf(lc));
                }
            }

            if (v.getWantPlay() && v.getPendingSeek() < 0.0d && Math.abs(v.getPosition() - v.getLastPushedPos()) > 0.1d) {
                v.setLastPushedPos(v.getPosition());
                s.set("TimePosition", Double.valueOf(v.getPosition()), false);
            }

            s.set("PlaybackLoudness", Float.valueOf(v.getLoudness()), false);

            if (v.getEnded()) {
                v.setEnded(false);
                v.setWantPlay(false);
                v.setLoudness(0.0f);
                s.set("TimePosition", Double.valueOf(0.0d), false);
                s.set("PlaybackLoudness", Float.valueOf(0.0f), false);
                s.set("IsPlaying", Boolean.FALSE, false);
                s.set("IsPaused", Boolean.FALSE, false);
                Instance.set$default(s, "Playing", Boolean.FALSE, false, 4, null);
                if (isTransient) {
                    transientSounds.remove(s);
                }
                Function1<? super Instance, Unit> endedCb = onEnded;
                if (endedCb != null) {
                    endedCb.invoke(s);
                }
            }
        }

        Iterator<Map.Entry<Instance, Voice>> it = voices.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Instance, Voice> entry = it.next();
            if (!alive.contains(entry.getKey())) {
                Voice value = entry.getValue();
                if (value != null) {
                    release(value);
                }
                it.remove();
            }
        }
        if (voices.isEmpty() && hasAudioFocus) {
            abandonAudioFocus();
        }
    }

    static final void update$lambda$2(Voice $vv, boolean $stopped) {
        try {
            if ($vv.getReleased()) {
                return;
            }
            MediaPlayer mp = $vv.getMp();
            if (mp != null) {
                if (mp.isPlaying()) {
                    mp.pause();
                }
                if ($stopped && $vv.getPrepared()) {
                    mp.seekTo(0);
                }
            }
            $vv.setLoudness(0.0f);
        } catch (Exception ignored) {
        }
    }

    static final void seekVoiceOnHandler(Voice $vv, double seconds, boolean ensureStart) {
        try {
            if ($vv.getReleased() || !$vv.getPrepared()) {
                return;
            }
            MediaPlayer mp = $vv.getMp();
            if (mp == null) {
                return;
            }
            int ms = Math.max(0, (int) (seconds * 1000.0d));
            mp.seekTo(ms);
            $vv.consumePendingSeek();
            $vv.setPosition(seconds);
            if (ensureStart && $vv.getWantPlay() && !appPaused && $vv.getLastSpeed() > 0.001f && !mp.isPlaying()) {
                mp.start();
                INSTANCE.setSpeed($vv, mp, $vv.getLastSpeed());
                INSTANCE.tick($vv);
            }
        } catch (Exception ignored) {
        }
    }

    private final void create(final Voice v) {
        h.post(new Runnable() {
            @Override
            public void run() {
                AudioEngine.create$lambda$12(v);
            }
        });
    }

    static final void create$lambda$12(final Voice $v) {
        if ($v.getReleased()) {
            return;
        }
        try {
            String strCacheFile = AssetFetcher.INSTANCE.cacheFile($v.getId());
            if (strCacheFile == null) {
                return;
            }
            File f = INSTANCE.prepareAudioFile($v.getId(), new File(strCacheFile));
            MediaPlayer mp = new MediaPlayer();
            mp.setAudioAttributes(new AudioAttributes.Builder().setUsage(14).setContentType(4).build());
            try (java.io.FileInputStream fis = new java.io.FileInputStream(f)) {
                mp.setDataSource(fis.getFD());
            }
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    AudioEngine.create$lambda$12$lambda$9($v, mediaPlayer);
                }
            });
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    AudioEngine.create$lambda$12$lambda$10($v, mediaPlayer);
                }
            });
            mp.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
                    return AudioEngine.create$lambda$12$lambda$11($v, mediaPlayer, i, i2);
                }
            });
            $v.setMp(mp);
            mp.prepareAsync();
        } catch (Exception e) {
            $v.setFailed(true);
            OutputLog.INSTANCE.warn("Sound rbxassetid://" + $v.getId() + ": " + e.getMessage());
        }
    }

    static final void create$lambda$12$lambda$9(Voice $v, MediaPlayer it) {
        if ($v.getReleased()) {
            try {
                it.release();
            } catch (Exception ignored) {
            }
            $v.setMp(null);
            return;
        }
        $v.setPrepared(true);
        int dur = it.getDuration();
        if (dur > 0) {
            $v.setLength(((double) dur) / 1000.0d);
        }
        it.setLooping(false);
        double pSeek = $v.consumePendingSeek();
        double initialSeek = pSeek >= 0.0d ? pSeek : $v.getSeek();
        if (initialSeek > 0.0d) {
            try {
                it.seekTo((int) (initialSeek * 1000.0d));
                $v.setPosition(initialSeek);
            } catch (Exception ignored) {
            }
        }
        if ($v.getWantPlay() && !appPaused) {
            it.setVolume(RangesKt.coerceAtLeast($v.getLastL(), 0.0f), RangesKt.coerceAtLeast($v.getLastR(), 0.0f));
            if ($v.getLastSpeed() > 0.001f) {
                it.start();
                INSTANCE.setSpeed($v, it, $v.getLastSpeed());
                INSTANCE.tick($v);
            }
        }
    }

    static final void create$lambda$12$lambda$10(Voice $v, MediaPlayer it) {
        if ($v.getReleased()) {
            return;
        }
        if ($v.getLooped() && $v.getWantPlay()) {
            $v.incrementLoopCount();
            $v.setPosition(0.0d);
            try {
                it.seekTo(0);
                if (!appPaused && $v.getLastSpeed() > 0.001f) {
                    it.start();
                    INSTANCE.setSpeed($v, it, $v.getLastSpeed());
                    INSTANCE.tick($v);
                }
            } catch (Exception ignored) {
            }
            return;
        }
        $v.setEnded(true);
        $v.setWantPlay(false);
        $v.setLoudness(0.0f);
        $v.setPosition(0.0d);
    }

    static final boolean create$lambda$12$lambda$11(final Voice $v, MediaPlayer mediaPlayer, int what, int extra) {
        try {
            if (mediaPlayer != null) mediaPlayer.release();
        } catch (Throwable ignored) {
        }
        $v.setMp(null);
        if ($v.getReleased()) {
            return true;
        }
        String cf = AssetFetcher.INSTANCE.cacheFile($v.getId());
        if (cf != null) {
            final File af = INSTANCE.prepareAudioFile($v.getId(), new File(cf));
            if (af.isFile() && af.length() > 32) {
                $v.setPrepared(true);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (!INSTANCE.playWithMediaCodec($v, af)) {
                            if ($v.getId() == 9114222045L) {
                                File syn = INSTANCE.writeSynthJumpWav(new File(af.getParentFile(), "9114222045_synth.wav"));
                                if (syn.isFile() && INSTANCE.playWithMediaCodec($v, syn)) return;
                            }
                            if (!$v.getReleased()) {
                                $v.setFailed(true);
                                OutputLog.INSTANCE.warn("Sound rbxassetid://" + $v.getId() + " unsupported audio codec");
                            }
                        }
                    }
                }, "AudioFallback-" + $v.getId()).start();
                return true;
            }
        }
        $v.setFailed(true);
        OutputLog.INSTANCE.warn("Sound rbxassetid://" + $v.getId() + " can't be played on this device (" + what + "/" + extra + ")");
        return true;
    }

    private final File writeSynthJumpWav(File out) {
        try {
            if (out.isFile() && out.length() > 128) return out;
            int sr = 22050;
            int n = (int) (sr * 0.14f);
            byte[] wav = new byte[44 + n * 2];
            int dataLen = n * 2;
            int totalLen = dataLen + 36;
            wav[0]='R'; wav[1]='I'; wav[2]='F'; wav[3]='F';
            wav[4]=(byte)(totalLen & 0xFF); wav[5]=(byte)((totalLen>>8)&0xFF); wav[6]=(byte)((totalLen>>16)&0xFF); wav[7]=(byte)((totalLen>>24)&0xFF);
            wav[8]='W'; wav[9]='A'; wav[10]='V'; wav[11]='E';
            wav[12]='f'; wav[13]='m'; wav[14]='t'; wav[15]=' ';
            wav[16]=16; wav[20]=1; wav[22]=1;
            wav[24]=(byte)(sr & 0xFF); wav[25]=(byte)((sr>>8)&0xFF);
            int br = sr * 2;
            wav[28]=(byte)(br & 0xFF); wav[29]=(byte)((br>>8)&0xFF); wav[30]=(byte)((br>>16)&0xFF);
            wav[32]=2; wav[34]=16;
            wav[36]='d'; wav[37]='a'; wav[38]='t'; wav[39]='a';
            wav[40]=(byte)(dataLen & 0xFF); wav[41]=(byte)((dataLen>>8)&0xFF); wav[42]=(byte)((dataLen>>16)&0xFF); wav[43]=(byte)((dataLen>>24)&0xFF);
            double phase = 0.0;
            for (int i = 0; i < n; i++) {
                double t = (double) i / (double) n;
                double freq = 260.0 + 340.0 * t;
                phase += (2.0 * Math.PI * freq) / sr;
                double env = Math.sin(Math.PI * Math.min(1.0, t * 5.0)) * Math.pow(1.0 - t, 1.4);
                short smp = (short) (Math.sin(phase) * env * 12000.0);
                wav[44 + i * 2] = (byte) (smp & 0xFF);
                wav[45 + i * 2] = (byte) ((smp >> 8) & 0xFF);
            }
            try (java.io.FileOutputStream fos = new java.io.FileOutputStream(out)) {
                fos.write(wav);
            }
            return out;
        } catch (Throwable t) {
            return out;
        }
    }

    private final File prepareAudioFile(long id, File rawFile) {
        try {
            if (rawFile == null || !rawFile.isFile()) return rawFile;
            File normDir = new File(rawFile.getParentFile(), "audio_norm");
            if (!normDir.exists()) normDir.mkdirs();
            String[] exts = {".ogg", ".mp3", ".wav", ".flac", ".m4a"};
            for (String ex : exts) {
                File cached = new File(normDir, id + ex);
                if (cached.isFile() && cached.length() > 32) return cached;
            }
            byte[] data;
            try (java.io.FileInputStream in = new java.io.FileInputStream(rawFile)) {
                java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
                byte[] buf = new byte[16384];
                int n;
                while ((n = in.read(buf)) > 0) bos.write(buf, 0, n);
                data = bos.toByteArray();
            }
            if (data.length >= 2 && (data[0] & 0xFF) == 0x1F && (data[1] & 0xFF) == 0x8B) {
                try (java.util.zip.GZIPInputStream gz = new java.util.zip.GZIPInputStream(new java.io.ByteArrayInputStream(data))) {
                    java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
                    byte[] buf = new byte[16384];
                    int n;
                    while ((n = gz.read(buf)) > 0) bos.write(buf, 0, n);
                    data = bos.toByteArray();
                } catch (Throwable ignored) {
                }
            }
            // Reject HTML/JSON/XML error payloads from Roblox CDN so they are never cached as audio
            if (data.length < 32 || data[0] == '{' || data[0] == '<' || (data[0] == '[' && data[1] == '{')) {
                if (id == 9114222045L) {
                    return writeSynthJumpWav(new File(normDir, id + ".wav"));
                }
                return rawFile;
            }
            int offset = 0;
            String ext = ".ogg";
            int scanLim = Math.min(data.length - 4, 4096);
            boolean matched = false;
            for (int i = 0; i <= scanLim; i++) {
                int b0 = data[i] & 0xFF, b1 = data[i + 1] & 0xFF, b2 = data[i + 2] & 0xFF, b3 = data[i + 3] & 0xFF;
                if (b0 == 'O' && b1 == 'g' && b2 == 'g' && b3 == 'S') {
                    offset = i; ext = ".ogg"; matched = true; break;
                }
                if (b0 == 'R' && b1 == 'I' && b2 == 'F' && b3 == 'F' && i + 11 < data.length && data[i + 8] == 'W' && data[i + 9] == 'A' && data[i + 10] == 'V' && data[i + 11] == 'E') {
                    offset = i; ext = ".wav"; matched = true; break;
                }
                if (b0 == 'I' && b1 == 'D' && b2 == '3') {
                    offset = i; ext = ".mp3"; matched = true; break;
                }
                if (b0 == 'f' && b1 == 'L' && b2 == 'a' && b3 == 'C') {
                    offset = i; ext = ".flac"; matched = true; break;
                }
                if (i >= 4 && b0 == 'f' && b1 == 't' && b2 == 'y' && b3 == 'p') {
                    offset = i - 4; ext = ".m4a"; matched = true; break;
                }
            }
            if (!matched && data.length >= 2 && (data[0] & 0xFF) == 0xFF && ((data[1] & 0xE0) == 0xE0)) {
                ext = ".mp3";
            }
            File out = new File(normDir, id + ext);
            File tmp = new File(normDir, id + ext + ".tmp");
            try (java.io.FileOutputStream fos = new java.io.FileOutputStream(tmp)) {
                fos.write(data, offset, data.length - offset);
            }
            tmp.renameTo(out);
            return out.isFile() ? out : rawFile;
        } catch (Throwable t) {
            return rawFile;
        }
    }

    private final boolean playWithMediaCodec(Voice v, File audioFile) {
        android.media.MediaExtractor extractor = null;
        android.media.MediaCodec codec = null;
        android.media.AudioTrack track = null;
        try {
            extractor = new android.media.MediaExtractor();
            try (java.io.FileInputStream fis = new java.io.FileInputStream(audioFile)) {
                extractor.setDataSource(fis.getFD());
            }
            int trackIdx = -1;
            android.media.MediaFormat fmt = null;
            for (int i = 0; i < extractor.getTrackCount(); i++) {
                android.media.MediaFormat f = extractor.getTrackFormat(i);
                String mime = f.getString(android.media.MediaFormat.KEY_MIME);
                if (mime != null && mime.startsWith("audio/")) {
                    trackIdx = i;
                    fmt = f;
                    break;
                }
            }
            if (trackIdx < 0 || fmt == null) return false;
            extractor.selectTrack(trackIdx);
            String mime = fmt.getString(android.media.MediaFormat.KEY_MIME);
            int sampleRate = fmt.containsKey(android.media.MediaFormat.KEY_SAMPLE_RATE) ? fmt.getInteger(android.media.MediaFormat.KEY_SAMPLE_RATE) : 44100;
            int channels = fmt.containsKey(android.media.MediaFormat.KEY_CHANNEL_COUNT) ? fmt.getInteger(android.media.MediaFormat.KEY_CHANNEL_COUNT) : 2;
            if (fmt.containsKey(android.media.MediaFormat.KEY_DURATION)) {
                v.setLength(fmt.getLong(android.media.MediaFormat.KEY_DURATION) / 1000000.0d);
            }
            double initSeek = v.consumePendingSeek();
            if (initSeek < 0.0d && v.getSeek() > 0.0d) {
                initSeek = v.getSeek();
            }
            if (initSeek > 0.0d) {
                extractor.seekTo((long) (initSeek * 1000000.0d), android.media.MediaExtractor.SEEK_TO_CLOSEST_SYNC);
            }
            int chMask = channels == 1 ? android.media.AudioFormat.CHANNEL_OUT_MONO : android.media.AudioFormat.CHANNEL_OUT_STEREO;
            int minBuf = android.media.AudioTrack.getMinBufferSize(sampleRate, chMask, android.media.AudioFormat.ENCODING_PCM_16BIT);
            track = new android.media.AudioTrack.Builder()
                    .setAudioAttributes(new AudioAttributes.Builder().setUsage(14).setContentType(4).build())
                    .setAudioFormat(new android.media.AudioFormat.Builder().setEncoding(android.media.AudioFormat.ENCODING_PCM_16BIT).setSampleRate(sampleRate).setChannelMask(chMask).build())
                    .setBufferSizeInBytes(Math.max(minBuf * 2, 8192))
                    .build();
            float initL = Math.max(0.0f, v.getLastL());
            float initR = Math.max(0.0f, v.getLastR());
            track.setStereoVolume(initL, initR);
            track.play();
            boolean trackPaused = false;
            codec = android.media.MediaCodec.createDecoderByType(mime);
            codec.configure(fmt, null, null, 0);
            codec.start();
            android.media.MediaCodec.BufferInfo info = new android.media.MediaCodec.BufferInfo();
            boolean sawInputEOS = false;
            boolean sawOutputEOS = false;
            while (!v.getReleased() && !v.getFailed()) {
                if (!v.getWantPlay() || appPaused || v.getLastSpeed() <= 0.001f) {
                    if (!trackPaused) {
                        try { track.pause(); } catch (Throwable ignored) {}
                        trackPaused = true;
                    }
                    v.setLoudness(0.0f);
                    try { Thread.sleep(30L); } catch (InterruptedException ignored) {}
                    continue;
                }
                if (trackPaused) {
                    try { track.play(); } catch (Throwable ignored) {}
                    trackPaused = false;
                }
                double pSeek = v.consumePendingSeek();
                if (pSeek >= 0.0d) {
                    extractor.seekTo((long) (pSeek * 1000000.0d), android.media.MediaExtractor.SEEK_TO_CLOSEST_SYNC);
                    codec.flush();
                    sawInputEOS = false;
                    sawOutputEOS = false;
                    v.setPosition(pSeek);
                }
                track.setStereoVolume(Math.max(0.0f, v.getLastL()), Math.max(0.0f, v.getLastR()));
                if (!sawInputEOS) {
                    int inIdx = codec.dequeueInputBuffer(10000L);
                    if (inIdx >= 0) {
                        java.nio.ByteBuffer inBuf = codec.getInputBuffer(inIdx);
                        int sampleSize = extractor.readSampleData(inBuf, 0);
                        if (sampleSize < 0) {
                            codec.queueInputBuffer(inIdx, 0, 0, 0L, android.media.MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                            sawInputEOS = true;
                        } else {
                            codec.queueInputBuffer(inIdx, 0, sampleSize, extractor.getSampleTime(), 0);
                            extractor.advance();
                        }
                    }
                }
                int outIdx = codec.dequeueOutputBuffer(info, 10000L);
                if (outIdx >= 0) {
                    java.nio.ByteBuffer outBuf = codec.getOutputBuffer(outIdx);
                    if (outBuf != null && info.size > 0) {
                        byte[] pcm = new byte[info.size];
                        outBuf.position(info.offset);
                        outBuf.get(pcm);
                        track.write(pcm, 0, pcm.length);
                        v.setPosition(info.presentationTimeUs / 1000000.0d);
                        v.setLoudness(computePcmLoudness(pcm, pcm.length));
                    }
                    codec.releaseOutputBuffer(outIdx, false);
                    if ((info.flags & android.media.MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                        sawOutputEOS = true;
                    }
                }
                if (sawOutputEOS) {
                    if (v.getLooped() && v.getWantPlay() && !v.getReleased()) {
                        v.incrementLoopCount();
                        extractor.seekTo(0L, android.media.MediaExtractor.SEEK_TO_CLOSEST_SYNC);
                        codec.flush();
                        sawInputEOS = false;
                        sawOutputEOS = false;
                        v.setPosition(0.0d);
                    } else {
                        break;
                    }
                }
            }
            if (!v.getReleased()) {
                v.setEnded(true);
                v.setWantPlay(false);
                v.setLoudness(0.0f);
                v.setPosition(0.0d);
            }
            return true;
        } catch (Throwable t) {
            return false;
        } finally {
            v.setLoudness(0.0f);
            try { if (codec != null) { codec.stop(); codec.release(); } } catch (Throwable ignored) {}
            try { if (extractor != null) extractor.release(); } catch (Throwable ignored) {}
            try { if (track != null) { track.stop(); track.release(); } } catch (Throwable ignored) {}
        }
    }

    private final void setSpeed(Voice v, MediaPlayer mp, float speed) {
        if (speed <= 0.001f) {
            try {
                if (mp.isPlaying()) {
                    mp.pause();
                }
            } catch (Exception ignored) {
            }
            v.setLoudness(0.0f);
            return;
        }
        float clamped = RangesKt.coerceIn(speed, 0.05f, 8.0f);
        if (Math.abs(clamped - v.getAppliedSpeed()) <= 0.001f) {
            return;
        }
        try {
            if (mp.isPlaying()) {
                mp.setPlaybackParams(mp.getPlaybackParams().setSpeed(clamped).setPitch(clamped));
                v.setAppliedSpeed(clamped);
            }
        } catch (Exception ignored) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void apply(Voice v, float l, float r, float speed, boolean loop, boolean restart) {
        MediaPlayer mp = v.getMp();
        if (mp == null || !v.getPrepared() || v.getReleased()) {
            return;
        }
        try {
            mp.setVolume(l, r);
            mp.setLooping(false);
            if (appPaused || speed <= 0.001f) {
                if (mp.isPlaying()) {
                    mp.pause();
                }
                v.setLoudness(0.0f);
                return;
            }
            if (restart || !mp.isPlaying()) {
                double pSeek = v.consumePendingSeek();
                if (pSeek >= 0.0d) {
                    mp.seekTo((int) (pSeek * 1000.0d));
                }
                mp.start();
                tick(v);
            }
            setSpeed(v, mp, speed);
        } catch (Exception ignored) {
        }
    }

    private final void tick(final Voice v) {
        if (v.getTicking() || v.getReleased()) {
            return;
        }
        v.setTicking(true);
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                MediaPlayer mp = v.getMp();
                if (v.getReleased() || mp == null) {
                    v.setTicking(false);
                    v.setLoudness(0.0f);
                    return;
                }
                try {
                    if (v.getPrepared() && mp.isPlaying()) {
                        v.setPosition(((double) mp.getCurrentPosition()) / 1000.0d);
                        v.setLoudness(Math.min(1000.0f, ((v.getLastL() + v.getLastR()) * 0.5f) * 350.0f));
                    } else {
                        v.setLoudness(0.0f);
                    }
                    if (v.getWantPlay() && !v.getReleased() && !v.getEnded()) {
                        AudioEngine.h.postDelayed(this, 100L);
                    } else {
                        v.setTicking(false);
                    }
                } catch (Exception e) {
                    v.setTicking(false);
                    v.setLoudness(0.0f);
                }
            }
        }, 100L);
    }

    private final void release(final Voice v) {
        v.setReleased(true);
        v.setWantPlay(false);
        v.setLoudness(0.0f);
        h.post(new Runnable() {
            @Override
            public void run() {
                AudioEngine.release$lambda$13(v);
            }
        });
    }

    static final void release$lambda$13(Voice $v) {
        try {
            MediaPlayer mp = $v.getMp();
            if (mp != null) {
                mp.release();
            }
        } catch (Exception ignored) {
        }
        $v.setMp(null);
    }

    public final void stopAll() {
        for (Voice v : voices.values()) {
            if (v != null) {
                release(v);
            }
        }
        voices.clear();
        missing.clear();
        transientSounds.clear();
        abandonAudioFocus();
    }

    /* JADX INFO: compiled from: AudioEngine.kt */
    public static final class Preview {
        private volatile String error;
        private final long id;
        private volatile float length;
        private volatile float position;
        private volatile PreviewState state = PreviewState.LOADING;

        public Preview(long id) {
            this.id = id;
        }

        public final long getId() {
            return this.id;
        }

        public final PreviewState getState() {
            return this.state;
        }

        public final void setState(PreviewState previewState) {
            Intrinsics.checkNotNullParameter(previewState, "<set-?>");
            this.state = previewState;
        }

        public final float getPosition() {
            return this.position;
        }

        public final void setPosition(float f) {
            this.position = f;
        }

        public final float getLength() {
            return this.length;
        }

        public final void setLength(float f) {
            this.length = f;
        }

        public final String getError() {
            return this.error;
        }

        public final void setError(String str) {
            this.error = str;
        }
    }

    public final Preview getPreview() {
        return preview;
    }

    public final CopyOnWriteArrayList<Function0<Unit>> getPreviewListeners() {
        return previewListeners;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void notifyPreview() {
        Iterator it = previewListeners.iterator();
        while (it.hasNext()) {
            ((Function0) it.next()).invoke();
        }
    }

    public static /* synthetic */ void togglePreview$default(AudioEngine audioEngine, long j, float f, int i, Object obj) {
        if ((i & 2) != 0) {
            f = 0.5f;
        }
        audioEngine.togglePreview(j, f);
    }

    public final void togglePreview(final long id, final float volume) {
        Preview cur = preview;
        if (cur != null && cur.getId() == id) {
            switch (WhenMappings.$EnumSwitchMapping$0[cur.getState().ordinal()]) {
                case 1:
                    pausePreview();
                    return;
                case 2:
                    resumePreview();
                    return;
                case 3:
                    if (cur.getLength() > 0.0f) {
                        resumePreview();
                        return;
                    }
                    break;
                case 4:
                    stopPreview();
                    return;
                case 5:
                    break;
                default:
                    throw new NoWhenBranchMatchedException();
            }
        }
        stopPreview();
        final Preview p = new Preview(id);
        preview = p;
        notifyPreview();
        new Thread(new Runnable() { // from class: com.goydevv.studrblx.audio.AudioEngine$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                AudioEngine.togglePreview$lambda$19(id, p, volume);
            }
        }).start();
    }

    static final void togglePreview$lambda$19(final long $id, final Preview $p, final float $volume) {
        try {
            AssetFetcher.INSTANCE.load($id);
            h.post(new Runnable() { // from class: com.goydevv.studrblx.audio.AudioEngine$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    AudioEngine.togglePreview$lambda$19$lambda$18($p, $id, $volume);
                }
            });
        } catch (Exception e) {
            $p.setState(PreviewState.ERROR);
            String message = e.getMessage();
            if (message == null) {
                message = "Couldn't download the audio";
            }
            $p.setError(message);
            INSTANCE.notifyPreview();
        }
    }

    static final void togglePreview$lambda$19$lambda$18(final Preview $p, long $id, final float $volume) {
        AudioEngine audioEngine = INSTANCE;
        if (preview != $p) {
            return;
        }
        try {
            MediaPlayer mp = new MediaPlayer();
            mp.setAudioAttributes(new AudioAttributes.Builder().setUsage(1).setContentType(2).build());
            String strCacheFile = AssetFetcher.INSTANCE.cacheFile($id);
            Intrinsics.checkNotNull(strCacheFile);
            File pf = INSTANCE.prepareAudioFile($id, new File(strCacheFile));
            try (java.io.FileInputStream pfis = new java.io.FileInputStream(pf)) {
                mp.setDataSource(pfis.getFD());
            }
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() { // from class: com.goydevv.studrblx.audio.AudioEngine$$ExternalSyntheticLambda1
                @Override // android.media.MediaPlayer.OnPreparedListener
                public final void onPrepared(MediaPlayer mediaPlayer) {
                    AudioEngine.togglePreview$lambda$19$lambda$18$lambda$15($p, $volume, mediaPlayer);
                }
            });
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { // from class: com.goydevv.studrblx.audio.AudioEngine$$ExternalSyntheticLambda2
                @Override // android.media.MediaPlayer.OnCompletionListener
                public final void onCompletion(MediaPlayer mediaPlayer) {
                    AudioEngine.togglePreview$lambda$19$lambda$18$lambda$16($p, mediaPlayer);
                }
            });
            mp.setOnErrorListener(new MediaPlayer.OnErrorListener() { // from class: com.goydevv.studrblx.audio.AudioEngine$$ExternalSyntheticLambda3
                @Override // android.media.MediaPlayer.OnErrorListener
                public final boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
                    return AudioEngine.togglePreview$lambda$19$lambda$18$lambda$17($p, mediaPlayer, i, i2);
                }
            });
            AudioEngine audioEngine2 = INSTANCE;
            previewPlayer = mp;
            mp.prepareAsync();
        } catch (Exception e) {
            $p.setState(PreviewState.ERROR);
            $p.setError(e.getMessage());
            INSTANCE.notifyPreview();
        }
    }

    static final void togglePreview$lambda$19$lambda$18$lambda$15(Preview $p, float $volume, MediaPlayer it) {
        AudioEngine audioEngine = INSTANCE;
        if (preview != $p) {
            it.release();
            return;
        }
        $p.setLength(it.getDuration() / 1000.0f);
        float vol = RangesKt.coerceIn(2.0f * $volume, 0.05f, 1.0f);
        it.setVolume(vol, vol);
        it.start();
        $p.setState(PreviewState.PLAYING);
        INSTANCE.notifyPreview();
        AudioEngine audioEngine2 = INSTANCE;
        Intrinsics.checkNotNull(it);
        audioEngine2.tick($p, it);
    }

    static final void togglePreview$lambda$19$lambda$18$lambda$16(Preview $p, MediaPlayer it) {
        $p.setState(PreviewState.STOPPED);
        $p.setPosition(0.0f);
        INSTANCE.notifyPreview();
    }

    static final boolean togglePreview$lambda$19$lambda$18$lambda$17(Preview $p, MediaPlayer mediaPlayer, int i, int i2) {
        $p.setState(PreviewState.ERROR);
        $p.setError("This audio can't be played on this device");
        INSTANCE.notifyPreview();
        return true;
    }

    private final void tick(final Preview p, final MediaPlayer mp) {
        h.postDelayed(new Runnable() { // from class: com.goydevv.studrblx.audio.AudioEngine.tick.2
            @Override // java.lang.Runnable
            public void run() {
                if (AudioEngine.INSTANCE.getPreview() != p || AudioEngine.previewPlayer != mp || p.getState() != PreviewState.PLAYING) {
                    return;
                }
                try {
                    p.setPosition(mp.getCurrentPosition() / 1000.0f);
                    AudioEngine.INSTANCE.notifyPreview();
                    AudioEngine.h.postDelayed(this, 150L);
                } catch (Exception e) {
                }
            }
        }, 150L);
    }

    public final void pausePreview() {
        final Preview p = preview;
        if (p == null) {
            return;
        }
        h.post(new Runnable() { // from class: com.goydevv.studrblx.audio.AudioEngine$$ExternalSyntheticLambda16
            @Override // java.lang.Runnable
            public final void run() {
                AudioEngine.pausePreview$lambda$20(p);
            }
        });
    }

    static final void pausePreview$lambda$20(Preview $p) {
        MediaPlayer mp = previewPlayer;
        if (mp == null) {
            return;
        }
        AudioEngine audioEngine = INSTANCE;
        if (preview != $p || $p.getState() != PreviewState.PLAYING) {
            return;
        }
        try {
            mp.pause();
            $p.setPosition(mp.getCurrentPosition() / 1000.0f);
        } catch (Exception e) {
        }
        $p.setState(PreviewState.PAUSED);
        INSTANCE.notifyPreview();
    }

    public final void resumePreview() {
        final Preview p = preview;
        if (p == null) {
            return;
        }
        h.post(new Runnable() { // from class: com.goydevv.studrblx.audio.AudioEngine$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                AudioEngine.resumePreview$lambda$21(p);
            }
        });
    }

    static final void resumePreview$lambda$21(Preview $p) {
        MediaPlayer mp = previewPlayer;
        if (mp == null) {
            return;
        }
        AudioEngine audioEngine = INSTANCE;
        if (preview != $p) {
            return;
        }
        try {
            if ($p.getState() == PreviewState.STOPPED) {
                mp.seekTo(0);
                $p.setPosition(0.0f);
            }
            mp.start();
            $p.setState(PreviewState.PLAYING);
            INSTANCE.notifyPreview();
            INSTANCE.tick($p, mp);
        } catch (Exception e) {
        }
    }

    public final void seekPreview(long id, final float seconds) {
        final Preview p = preview;
        if (p != null) {
            if (p.getId() != id) {
                return;
            }
            h.post(new Runnable() { // from class: com.goydevv.studrblx.audio.AudioEngine$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    AudioEngine.seekPreview$lambda$23(p, seconds);
                }
            });
        }
    }

    static final void seekPreview$lambda$23(Preview $p, float $seconds) {
        MediaPlayer mp = previewPlayer;
        if (mp == null) {
            return;
        }
        AudioEngine audioEngine = INSTANCE;
        if (preview != $p || $p.getLength() <= 0.0f) {
            return;
        }
        float t = RangesKt.coerceIn($seconds, 0.0f, $p.getLength());
        try {
            mp.seekTo((int) (1000.0f * t));
            $p.setPosition(t);
            if ($p.getState() == PreviewState.STOPPED) {
                $p.setState(PreviewState.PAUSED);
            }
            INSTANCE.notifyPreview();
        } catch (Exception e) {
        }
    }

    public final void stopPreview() {
        Preview p = preview;
        preview = null;
        if (p != null) {
            p.setState(PreviewState.STOPPED);
        }
        h.post(new Runnable() { // from class: com.goydevv.studrblx.audio.AudioEngine$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                AudioEngine.stopPreview$lambda$24();
            }
        });
        notifyPreview();
    }

    static final void stopPreview$lambda$24() {
        try {
            MediaPlayer mediaPlayer = previewPlayer;
            if (mediaPlayer != null) {
                mediaPlayer.release();
            }
        } catch (Exception e) {
        }
        AudioEngine audioEngine = INSTANCE;
        previewPlayer = null;
    }
}

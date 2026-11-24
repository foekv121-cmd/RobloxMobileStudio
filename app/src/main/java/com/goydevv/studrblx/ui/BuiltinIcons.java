package com.goydevv.studrblx.ui;

import android.graphics.drawable.Drawable;
import com.goydevv.studrblx.model.ClassDb;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import kotlin.Pair;
import kotlin.Triple;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: BuiltinIcons.kt */
/* JADX INFO: loaded from: classes5.dex */
public final class BuiltinIcons {
    private static final int W = -1;
    private static final Map<String, Function1<IconPainter, Unit>> generalIcons;
    private static final Map<String, Function1<IconPainter, Unit>> instanceIcons;
    private static final Map<String, Function1<IconPainter, Unit>> uiIcons;
    public static final BuiltinIcons INSTANCE = new BuiltinIcons();
    private static final Function1<IconPainter, Unit> script = new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda1
        @Override // kotlin.jvm.functions.Function1
        public final Object invoke(Object obj) {
            return BuiltinIcons.script$lambda$0((IconPainter) obj);
        }
    };
    private static final Function1<IconPainter, Unit> localScript = new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda112
        @Override // kotlin.jvm.functions.Function1
        public final Object invoke(Object obj) {
            return BuiltinIcons.localScript$lambda$1((IconPainter) obj);
        }
    };
    private static final Function1<IconPainter, Unit> moduleScript = new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda125
        @Override // kotlin.jvm.functions.Function1
        public final Object invoke(Object obj) {
            return BuiltinIcons.moduleScript$lambda$2((IconPainter) obj);
        }
    };
    private static final Function1<IconPainter, Unit> part = new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda136
        @Override // kotlin.jvm.functions.Function1
        public final Object invoke(Object obj) {
            return BuiltinIcons.part$lambda$3((IconPainter) obj);
        }
    };
    private static final Function1<IconPainter, Unit> model = new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda147
        @Override // kotlin.jvm.functions.Function1
        public final Object invoke(Object obj) {
            return BuiltinIcons.model$lambda$4((IconPainter) obj);
        }
    };
    private static final Function1<IconPainter, Unit> folder = new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda158
        @Override // kotlin.jvm.functions.Function1
        public final Object invoke(Object obj) {
            return BuiltinIcons.folder$lambda$5((IconPainter) obj);
        }
    };
    private static final Function1<IconPainter, Unit> person = new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda169
        @Override // kotlin.jvm.functions.Function1
        public final Object invoke(Object obj) {
            return BuiltinIcons.person$lambda$6((IconPainter) obj);
        }
    };
    private static final Function1<IconPainter, Unit> humanoid = new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda180
        @Override // kotlin.jvm.functions.Function1
        public final Object invoke(Object obj) {
            return BuiltinIcons.humanoid$lambda$7((IconPainter) obj);
        }
    };
    private static final Function1<IconPainter, Unit> gui = new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda191
        @Override // kotlin.jvm.functions.Function1
        public final Object invoke(Object obj) {
            return BuiltinIcons.gui$lambda$8((IconPainter) obj);
        }
    };
    private static final Function2<Integer, String, Function1<IconPainter, Unit>> valueTag = new Function2() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda202
        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(Object obj, Object obj2) {
            return BuiltinIcons.valueTag$lambda$10(((Integer) obj).intValue(), (String) obj2);
        }
    };
    private static final Function1<IconPainter, Unit> constraint = new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda12
        @Override // kotlin.jvm.functions.Function1
        public final Object invoke(Object obj) {
            return BuiltinIcons.constraint$lambda$11((IconPainter) obj);
        }
    };
    private static final Function1<IconPainter, Unit> weld = new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda23
        @Override // kotlin.jvm.functions.Function1
        public final Object invoke(Object obj) {
            return BuiltinIcons.weld$lambda$12((IconPainter) obj);
        }
    };
    private static final Function1<IconPainter, Unit> light = new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda34
        @Override // kotlin.jvm.functions.Function1
        public final Object invoke(Object obj) {
            return BuiltinIcons.light$lambda$13((IconPainter) obj);
        }
    };

    private BuiltinIcons() {
    }

    static {
        HashMap map = new HashMap();
        map.put("Part", part);
        map.put("BasePart", part);
        map.put("FormFactorPart", part);
        map.put("Platform", part);
        map.put("WedgePart", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda45
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$14((IconPainter) obj);
            }
        });
        map.put("CornerWedgePart", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda56
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$15((IconPainter) obj);
            }
        });
        map.put("TrussPart", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda67
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$16((IconPainter) obj);
            }
        });
        map.put("SpawnLocation", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda78
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$17((IconPainter) obj);
            }
        });
        map.put("Seat", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda89
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$18((IconPainter) obj);
            }
        });
        map.put("MeshPart", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda100
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$19((IconPainter) obj);
            }
        });
        map.put("UnionOperation", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda111
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$20((IconPainter) obj);
            }
        });
        map.put("PartOperation", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda115
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$21((IconPainter) obj);
            }
        });
        map.put("IntersectOperation", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda116
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$22((IconPainter) obj);
            }
        });
        map.put("NegateOperation", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda117
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$23((IconPainter) obj);
            }
        });
        map.put("Terrain", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda118
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$24((IconPainter) obj);
            }
        });
        map.put("Model", model);
        map.put("WorldModel", model);
        map.put("Status", model);
        map.put("Folder", folder);
        map.put("StarterPlayerScripts", folder);
        map.put("StarterCharacterScripts", folder);
        map.put("Configuration", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda119
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$25((IconPainter) obj);
            }
        });
        map.put("Workspace", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda120
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$26((IconPainter) obj);
            }
        });
        map.put("Camera", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda121
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$27((IconPainter) obj);
            }
        });
        map.put("Players", person);
        map.put("Player", person);
        map.put("StarterPlayer", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda122
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$28((IconPainter) obj);
            }
        });
        map.put("Lighting", light);
        map.put("PointLight", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda123
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$29((IconPainter) obj);
            }
        });
        map.put("SpotLight", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda124
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$30((IconPainter) obj);
            }
        });
        map.put("SurfaceLight", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda126
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$31((IconPainter) obj);
            }
        });
        map.put("MaterialService", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda127
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$32((IconPainter) obj);
            }
        });
        map.put("ReplicatedFirst", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda128
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$33((IconPainter) obj);
            }
        });
        map.put("ReplicatedStorage", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda129
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$34((IconPainter) obj);
            }
        });
        map.put("ServerStorage", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda130
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$35((IconPainter) obj);
            }
        });
        map.put("ServerScriptService", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda131
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$36((IconPainter) obj);
            }
        });
        map.put("StarterGui", gui);
        map.put("PlayerGui", gui);
        map.put("CoreGui", gui);
        map.put("StarterPack", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda132
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$37((IconPainter) obj);
            }
        });
        map.put("Backpack", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda133
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$38((IconPainter) obj);
            }
        });
        map.put("PlayerScripts", localScript);
        map.put("Teams", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda134
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$39((IconPainter) obj);
            }
        });
        map.put("Team", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda135
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$40((IconPainter) obj);
            }
        });
        map.put("SoundService", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda137
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$41((IconPainter) obj);
            }
        });
        map.put("Sound", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda138
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$42((IconPainter) obj);
            }
        });
        map.put("SoundGroup", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda139
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$43((IconPainter) obj);
            }
        });
        map.put("Chat", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda140
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$44((IconPainter) obj);
            }
        });
        map.put("TextChatService", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda141
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$45((IconPainter) obj);
            }
        });
        map.put("LocalizationService", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda142
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$46((IconPainter) obj);
            }
        });
        map.put("TestService", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda143
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$47((IconPainter) obj);
            }
        });
        map.put("Script", script);
        map.put("LocalScript", localScript);
        map.put("ModuleScript", moduleScript);
        map.put("LuaSourceContainer", script);
        map.put("BaseScript", script);
        map.put("ScreenGui", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda144
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$48((IconPainter) obj);
            }
        });
        map.put("BillboardGui", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda145
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$49((IconPainter) obj);
            }
        });
        map.put("SurfaceGui", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda146
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$50((IconPainter) obj);
            }
        });
        map.put("Frame", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda148
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$51((IconPainter) obj);
            }
        });
        map.put("TextLabel", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda149
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$52((IconPainter) obj);
            }
        });
        map.put("TextButton", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda150
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$53((IconPainter) obj);
            }
        });
        map.put("TextBox", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda151
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$54((IconPainter) obj);
            }
        });
        map.put("ImageLabel", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda152
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$55((IconPainter) obj);
            }
        });
        map.put("ImageButton", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda153
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$56((IconPainter) obj);
            }
        });
        map.put("ScrollingFrame", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda154
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$57((IconPainter) obj);
            }
        });
        map.put("ViewportFrame", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda155
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$58((IconPainter) obj);
            }
        });
        map.put("CanvasGroup", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda156
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$59((IconPainter) obj);
            }
        });
        map.put("VideoFrame", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda157
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$60((IconPainter) obj);
            }
        });
        map.put("UICorner", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda159
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$61((IconPainter) obj);
            }
        });
        map.put("UIStroke", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda160
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$62((IconPainter) obj);
            }
        });
        map.put("UIGradient", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda161
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$63((IconPainter) obj);
            }
        });
        map.put("UIPadding", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda162
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$64((IconPainter) obj);
            }
        });
        map.put("UIListLayout", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda163
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$65((IconPainter) obj);
            }
        });
        map.put("UIGridLayout", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda164
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$66((IconPainter) obj);
            }
        });
        map.put("UITableLayout", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda165
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$67((IconPainter) obj);
            }
        });
        map.put("UIPageLayout", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda166
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$68((IconPainter) obj);
            }
        });
        map.put("UIScale", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda167
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$69((IconPainter) obj);
            }
        });
        map.put("UIAspectRatioConstraint", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda168
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$70((IconPainter) obj);
            }
        });
        map.put("UISizeConstraint", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda170
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$71((IconPainter) obj);
            }
        });
        map.put("UITextSizeConstraint", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda171
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$72((IconPainter) obj);
            }
        });
        map.put("Decal", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda172
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$73((IconPainter) obj);
            }
        });
        map.put("Texture", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda173
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$74((IconPainter) obj);
            }
        });
        map.put("SpecialMesh", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda174
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$75((IconPainter) obj);
            }
        });
        map.put("BlockMesh", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda175
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$76((IconPainter) obj);
            }
        });
        map.put("CylinderMesh", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda176
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$77((IconPainter) obj);
            }
        });
        map.put("Humanoid", humanoid);
        map.put("HumanoidDescription", humanoid);
        map.put("Animator", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda177
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$78((IconPainter) obj);
            }
        });
        map.put("AnimationController", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda178
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$79((IconPainter) obj);
            }
        });
        map.put("Animation", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda179
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$80((IconPainter) obj);
            }
        });
        map.put("KeyframeSequence", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda181
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$81((IconPainter) obj);
            }
        });
        map.put("Tool", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda182
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$82((IconPainter) obj);
            }
        });
        map.put("HopperBin", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda183
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$83((IconPainter) obj);
            }
        });
        map.put("Accessory", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda184
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$84((IconPainter) obj);
            }
        });
        map.put("Hat", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda185
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$85((IconPainter) obj);
            }
        });
        map.put("Shirt", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda186
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$86((IconPainter) obj);
            }
        });
        map.put("ShirtGraphic", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda187
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$87((IconPainter) obj);
            }
        });
        map.put("Pants", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda188
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$88((IconPainter) obj);
            }
        });
        map.put("BodyColors", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda189
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$89((IconPainter) obj);
            }
        });
        map.put("Attachment", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda190
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$90((IconPainter) obj);
            }
        });
        map.put("Bone", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda192
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$91((IconPainter) obj);
            }
        });
        map.put("Weld", weld);
        map.put("ManualWeld", weld);
        map.put("Snap", weld);
        map.put("Glue", weld);
        map.put("WeldConstraint", weld);
        map.put("NoCollisionConstraint", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda193
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$92((IconPainter) obj);
            }
        });
        map.put("Motor", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda194
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$93((IconPainter) obj);
            }
        });
        map.put("Motor6D", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda195
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$94((IconPainter) obj);
            }
        });
        map.put("Constraint", constraint);
        map.put("ClickDetector", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda196
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$95((IconPainter) obj);
            }
        });
        map.put("ProximityPrompt", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda197
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$96((IconPainter) obj);
            }
        });
        map.put("ParticleEmitter", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda198
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$97((IconPainter) obj);
            }
        });
        map.put("Fire", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda199
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$98((IconPainter) obj);
            }
        });
        map.put("Smoke", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda200
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$99((IconPainter) obj);
            }
        });
        map.put("Sparkles", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda201
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$100((IconPainter) obj);
            }
        });
        map.put("Trail", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda2
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$101((IconPainter) obj);
            }
        });
        map.put("Beam", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda3
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$102((IconPainter) obj);
            }
        });
        map.put("Explosion", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda4
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$103((IconPainter) obj);
            }
        });
        map.put("Highlight", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda5
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$104((IconPainter) obj);
            }
        });
        map.put("ForceField", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda6
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$105((IconPainter) obj);
            }
        });
        map.put("SelectionBox", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda7
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$106((IconPainter) obj);
            }
        });
        map.put("Sky", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda8
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$107((IconPainter) obj);
            }
        });
        map.put("Clouds", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda9
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$108((IconPainter) obj);
            }
        });
        map.put("Atmosphere", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda10
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$109((IconPainter) obj);
            }
        });
        map.put("BloomEffect", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda11
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$110((IconPainter) obj);
            }
        });
        map.put("BlurEffect", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda13
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$111((IconPainter) obj);
            }
        });
        map.put("ColorCorrectionEffect", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda14
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$112((IconPainter) obj);
            }
        });
        map.put("SunRaysEffect", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda15
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$113((IconPainter) obj);
            }
        });
        map.put("DepthOfFieldEffect", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda16
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$114((IconPainter) obj);
            }
        });
        Function2<Integer, String, Function1<IconPainter, Unit>> function2 = valueTag;
        Integer numValueOf = Integer.valueOf(IconPainter.PURPLE);
        map.put("StringValue", function2.invoke(numValueOf, "S"));
        map.put("IntValue", valueTag.invoke(numValueOf, "#"));
        map.put("NumberValue", valueTag.invoke(numValueOf, "#"));
        map.put("BoolValue", valueTag.invoke(numValueOf, "B"));
        map.put("ObjectValue", valueTag.invoke(numValueOf, "O"));
        map.put("Vector3Value", valueTag.invoke(numValueOf, "V"));
        map.put("CFrameValue", valueTag.invoke(numValueOf, "C"));
        Function2<Integer, String, Function1<IconPainter, Unit>> function3 = valueTag;
        Integer numValueOf2 = Integer.valueOf(IconPainter.RED);
        map.put("Color3Value", function3.invoke(numValueOf2, "C"));
        map.put("BrickColorValue", valueTag.invoke(numValueOf2, "B"));
        map.put("RayValue", valueTag.invoke(numValueOf, "R"));
        map.put("ValueBase", valueTag.invoke(numValueOf, "V"));
        map.put("RemoteEvent", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda17
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$115((IconPainter) obj);
            }
        });
        map.put("UnreliableRemoteEvent", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda18
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$116((IconPainter) obj);
            }
        });
        map.put("RemoteFunction", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda19
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$117((IconPainter) obj);
            }
        });
        map.put("BindableEvent", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda20
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$118((IconPainter) obj);
            }
        });
        map.put("BindableFunction", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda21
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$119((IconPainter) obj);
            }
        });
        map.put("Plugin", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda22
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$120((IconPainter) obj);
            }
        });
        map.put("Dialog", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda24
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$121((IconPainter) obj);
            }
        });
        map.put("DialogChoice", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda25
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$122((IconPainter) obj);
            }
        });
        map.put("Selection", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda26
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$123((IconPainter) obj);
            }
        });
        map.put("DataModel", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda27
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$124((IconPainter) obj);
            }
        });
        map.put("Message", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda28
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$125((IconPainter) obj);
            }
        });
        map.put("Hint", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda29
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$126((IconPainter) obj);
            }
        });
        map.put("VehicleSeat", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda30
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.instanceIcons$lambda$128$lambda$127((IconPainter) obj);
            }
        });
        instanceIcons = map;
        HashMap map2 = new HashMap();
        map2.put("Select", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda31
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$130((IconPainter) obj);
            }
        });
        map2.put("Move", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda32
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$131((IconPainter) obj);
            }
        });
        map2.put("Scale", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda33
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$132((IconPainter) obj);
            }
        });
        map2.put("Rotate", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda35
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$133((IconPainter) obj);
            }
        });
        map2.put("Part_Block", part);
        map2.put("Part", part);
        map2.put("Part_Sphere", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda36
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$134((IconPainter) obj);
            }
        });
        map2.put("Part_Cylinder", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda37
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$135((IconPainter) obj);
            }
        });
        map2.put("Part_Wedge", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda38
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$136((IconPainter) obj);
            }
        });
        map2.put("Part_CornerWedge", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda39
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$137((IconPainter) obj);
            }
        });
        Function1<IconPainter, Unit> function1 = instanceIcons.get("SpawnLocation");
        Intrinsics.checkNotNull(function1);
        map2.put("Spawn", function1);
        map2.put("InsertObject", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda40
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$138((IconPainter) obj);
            }
        });
        map2.put("Anchor", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda41
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$139((IconPainter) obj);
            }
        });
        map2.put("Group", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda42
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$140((IconPainter) obj);
            }
        });
        map2.put("Union", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda43
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$141((IconPainter) obj);
            }
        });
        map2.put("Intersect", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda44
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$142((IconPainter) obj);
            }
        });
        map2.put("Negate", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda46
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$143((IconPainter) obj);
            }
        });
        map2.put("Separate", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda47
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$144((IconPainter) obj);
            }
        });
        map2.put("BoxSelect", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda48
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$145((IconPainter) obj);
            }
        });
        map2.put("Ungroup", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda49
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$146((IconPainter) obj);
            }
        });
        map2.put("Duplicate", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda50
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$147((IconPainter) obj);
            }
        });
        map2.put("Delete", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda51
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$148((IconPainter) obj);
            }
        });
        map2.put("ZoomTo", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda52
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$149((IconPainter) obj);
            }
        });
        map2.put("FindAll", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda53
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$150((IconPainter) obj);
            }
        });
        map2.put("Move_P", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda54
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$151((IconPainter) obj);
            }
        });
        map2.put("Rotate_P", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda55
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$152((IconPainter) obj);
            }
        });
        map2.put("Color", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda57
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$153((IconPainter) obj);
            }
        });
        map2.put("Material", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda58
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$154((IconPainter) obj);
            }
        });
        map2.put("Surface", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda59
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$155((IconPainter) obj);
            }
        });
        map2.put("Cleanup", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda60
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$156((IconPainter) obj);
            }
        });
        map2.put("CommandBar", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda61
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$157((IconPainter) obj);
            }
        });
        map2.put("Explorer", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda62
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$158((IconPainter) obj);
            }
        });
        map2.put("Properties", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda63
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$159((IconPainter) obj);
            }
        });
        map2.put("Toolbox", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda64
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$160((IconPainter) obj);
            }
        });
        map2.put("Output", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda65
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$161((IconPainter) obj);
            }
        });
        map2.put("SwitchWindows", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda66
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$162((IconPainter) obj);
            }
        });
        map2.put("Effects", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda68
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$163((IconPainter) obj);
            }
        });
        map2.put("ViewSelector", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda69
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$164((IconPainter) obj);
            }
        });
        map2.put("Fwd", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda70
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$165((IconPainter) obj);
            }
        });
        map2.put("Fly", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda71
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$166((IconPainter) obj);
            }
        });
        map2.put("ManagePlugins", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda72
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$167((IconPainter) obj);
            }
        });
        map2.put("PluginsFolder", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda73
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$168((IconPainter) obj);
            }
        });
        map2.put("Place", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda74
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$169((IconPainter) obj);
            }
        });
        map2.put("Settings", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda75
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$170((IconPainter) obj);
            }
        });
        map2.put("Open", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda76
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$171((IconPainter) obj);
            }
        });
        map2.put("Save", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda77
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$172((IconPainter) obj);
            }
        });
        map2.put("Back", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda79
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$173((IconPainter) obj);
            }
        });
        map2.put("History", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda80
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$174((IconPainter) obj);
            }
        });
        map2.put("Download", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda81
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$175((IconPainter) obj);
            }
        });
        map2.put("Publish", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda82
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$176((IconPainter) obj);
            }
        });
        map2.put("Cloud", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda83
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$177((IconPainter) obj);
            }
        });
        map2.put("Account", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda84
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$178((IconPainter) obj);
            }
        });
        map2.put(StartScreen.HOME, new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda85
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$179((IconPainter) obj);
            }
        });
        map2.put("Shared", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda86
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$180((IconPainter) obj);
            }
        });
        map2.put("Info", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda87
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$181((IconPainter) obj);
            }
        });
        map2.put("Video", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda88
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$182((IconPainter) obj);
            }
        });
        map2.put("Theme", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda90
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$183((IconPainter) obj);
            }
        });
        map2.put("HideWindows", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda91
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$184((IconPainter) obj);
            }
        });
        map2.put("ShowWindows", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda92
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$185((IconPainter) obj);
            }
        });
        map2.put("RunScript", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda93
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$186((IconPainter) obj);
            }
        });
        map2.put("Play", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda94
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$187((IconPainter) obj);
            }
        });
        map2.put("Stop", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda95
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$188((IconPainter) obj);
            }
        });
        map2.put("Undo", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda96
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$189((IconPainter) obj);
            }
        });
        map2.put("Redo", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda97
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$190((IconPainter) obj);
            }
        });
        map2.put("SelectAll", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda98
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$191((IconPainter) obj);
            }
        });
        map2.put("CollapseAll", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda99
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$192((IconPainter) obj);
            }
        });
        map2.put("ScriptAnalysis", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda101
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$193((IconPainter) obj);
            }
        });
        map2.put("Audio", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda102
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$194((IconPainter) obj);
            }
        });
        Function1<IconPainter, Unit> function4 = instanceIcons.get("MeshPart");
        Intrinsics.checkNotNull(function4);
        map2.put("Mesh", function4);
        Function1<IconPainter, Unit> function5 = instanceIcons.get("Decal");
        Intrinsics.checkNotNull(function5);
        map2.put("Decal", function5);
        map2.put("Model", model);
        Function1<IconPainter, Unit> function6 = instanceIcons.get("Plugin");
        Intrinsics.checkNotNull(function6);
        map2.put("Plugin", function6);
        map2.put("Link", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda103
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$195((IconPainter) obj);
            }
        });
        map2.put("Pin", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda104
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$196((IconPainter) obj);
            }
        });
        map2.put("Copy", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda105
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$197((IconPainter) obj);
            }
        });
        map2.put("Cut", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda106
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$198((IconPainter) obj);
            }
        });
        map2.put("Paste", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda107
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$199((IconPainter) obj);
            }
        });
        map2.put("Lock", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda108
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$200((IconPainter) obj);
            }
        });
        map2.put("Collisions", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda109
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.generalIcons$lambda$202$lambda$201((IconPainter) obj);
            }
        });
        generalIcons = map2;
        HashMap map3 = new HashMap();
        map3.put("CloseWidget", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda110
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.uiIcons$lambda$207$lambda$204((IconPainter) obj);
            }
        });
        map3.put("Float", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda113
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.uiIcons$lambda$207$lambda$205((IconPainter) obj);
            }
        });
        map3.put("Grip", new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda114
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.uiIcons$lambda$207$lambda$206((IconPainter) obj);
            }
        });
        uiIcons = map3;
    }

    static final Unit script$lambda$0(IconPainter iconPainter) {
        Intrinsics.checkNotNullParameter(iconPainter, "<this>");
        IconPainter.page$default(iconPainter, IconPainter.BLUE, 0, 2, null);
        iconPainter.pageLines(-10711082);
        return Unit.INSTANCE;
    }

    static final Unit localScript$lambda$1(IconPainter iconPainter) {
        Intrinsics.checkNotNullParameter(iconPainter, "<this>");
        IconPainter.page$default(iconPainter, IconPainter.TEAL, 0, 2, null);
        iconPainter.pageLines(-8409152);
        iconPainter.person(15.0f, 16.5f, 6.0f, IconPainter.TEAL);
        return Unit.INSTANCE;
    }

    static final Unit moduleScript$lambda$2(IconPainter iconPainter) {
        Intrinsics.checkNotNullParameter(iconPainter, "<this>");
        IconPainter.page$default(iconPainter, IconPainter.PURPLE, 0, 2, null);
        iconPainter.pline(IconPainter.PURPLE, 1.6f, 12.0f, 8.0f, 10.0f, 10.0f, 10.0f, 12.0f, 9.0f, 13.0f, 10.0f, 14.0f, 10.0f, 16.0f, 12.0f, 18.0f);
        iconPainter.pline(IconPainter.PURPLE, 1.6f, 14.5f, 8.0f, 16.5f, 10.0f, 16.5f, 12.0f, 17.5f, 13.0f, 16.5f, 14.0f, 16.5f, 16.0f, 14.5f, 18.0f);
        return Unit.INSTANCE;
    }

    static final Unit part$lambda$3(IconPainter iconPainter) {
        Intrinsics.checkNotNullParameter(iconPainter, "<this>");
        IconPainter.cube$default(iconPainter, 12.0f, 12.0f, 9.0f, 0, 0, 0, 56, null);
        return Unit.INSTANCE;
    }

    static final Unit model$lambda$4(IconPainter iconPainter) {
        Intrinsics.checkNotNullParameter(iconPainter, "<this>");
        IconPainter.cube$default(iconPainter, 7.5f, 8.5f, 5.0f, 0, 0, 0, 56, null);
        IconPainter.cube$default(iconPainter, 16.5f, 8.5f, 5.0f, 0, 0, 0, 56, null);
        IconPainter.cube$default(iconPainter, 12.0f, 16.5f, 5.0f, 0, 0, 0, 56, null);
        return Unit.INSTANCE;
    }

    static final Unit folder$lambda$5(IconPainter iconPainter) {
        Intrinsics.checkNotNullParameter(iconPainter, "<this>");
        iconPainter.poly(-2513632, 2.0f, 6.0f, 9.0f, 6.0f, 11.0f, 8.0f, 22.0f, 8.0f, 22.0f, 20.0f, 2.0f, 20.0f);
        iconPainter.rect(2.0f, 9.5f, 22.0f, 20.0f, IconPainter.YELLOW, 1.0f);
        return Unit.INSTANCE;
    }

    static final Unit person$lambda$6(IconPainter iconPainter) {
        Intrinsics.checkNotNullParameter(iconPainter, "<this>");
        iconPainter.person(12.0f, 13.0f, 10.0f, IconPainter.BLUE);
        return Unit.INSTANCE;
    }

    static final Unit humanoid$lambda$7(IconPainter iconPainter) {
        Intrinsics.checkNotNullParameter(iconPainter, "<this>");
        iconPainter.person(12.0f, 13.0f, 10.0f, IconPainter.GREEN);
        return Unit.INSTANCE;
    }

    static final Unit gui$lambda$8(IconPainter iconPainter) {
        Intrinsics.checkNotNullParameter(iconPainter, "<this>");
        iconPainter.rect(3.0f, 4.0f, 21.0f, 20.0f, -10853776, 1.5f);
        iconPainter.rect(4.5f, 8.0f, 19.5f, 18.5f, IconPainter.LBLUE, 0.5f);
        iconPainter.rect(4.5f, 5.3f, 8.0f, 6.8f, IconPainter.RED, 0.5f);
        return Unit.INSTANCE;
    }

    static final Function1 valueTag$lambda$10(final int col, final String l) {
        Intrinsics.checkNotNullParameter(l, "l");
        return new Function1() { // from class: com.goydevv.studrblx.ui.BuiltinIcons$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BuiltinIcons.valueTag$lambda$10$lambda$9(col, l, (IconPainter) obj);
            }
        };
    }

    static final Unit valueTag$lambda$10$lambda$9(int $col, String $l, IconPainter iconPainter) {
        Intrinsics.checkNotNullParameter(iconPainter, "<this>");
        iconPainter.tag($col, $l);
        return Unit.INSTANCE;
    }

    static final Unit constraint$lambda$11(IconPainter iconPainter) {
        Intrinsics.checkNotNullParameter(iconPainter, "<this>");
        iconPainter.chain(IconPainter.ORANGE);
        return Unit.INSTANCE;
    }

    static final Unit weld$lambda$12(IconPainter iconPainter) {
        Intrinsics.checkNotNullParameter(iconPainter, "<this>");
        iconPainter.chain(-7498852);
        return Unit.INSTANCE;
    }

    static final Unit light$lambda$13(IconPainter iconPainter) {
        Intrinsics.checkNotNullParameter(iconPainter, "<this>");
        iconPainter.bulb(12.0f, 11.0f, IconPainter.YELLOW);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$14(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.poly(IconPainter.GRAY_L, 3.0f, 20.0f, 21.0f, 20.0f, 21.0f, 5.0f);
        put.poly(IconPainter.GRAY_T, 3.0f, 20.0f, 21.0f, 5.0f, 21.0f, 8.5f, 6.5f, 20.0f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$15(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.poly(IconPainter.GRAY_L, 3.0f, 20.0f, 12.0f, 3.0f, 12.0f, 20.0f);
        put.poly(IconPainter.GRAY_R, 12.0f, 3.0f, 21.0f, 20.0f, 12.0f, 20.0f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$16(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        IconPainter.srect$default(put, 5.0f, 2.0f, 19.0f, 22.0f, -7498852, 2.0f, 0.0f, 64, null);
        for (int i = 0; i < 3; i++) {
            float y = (i * 6.66f) + 2.0f;
            put.line(5.0f, y, 19.0f, y + 6.66f, -7498852, 1.5f);
            put.line(19.0f, y, 5.0f, y + 6.66f, -7498852, 1.5f);
        }
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$17(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.poly(IconPainter.GRAY_T, 12.0f, 10.0f, 22.0f, 14.5f, 12.0f, 19.0f, 2.0f, 14.5f);
        put.poly(IconPainter.GRAY_L, 2.0f, 14.5f, 12.0f, 19.0f, 12.0f, 21.5f, 2.0f, 17.0f);
        put.poly(IconPainter.GRAY_R, 12.0f, 19.0f, 22.0f, 14.5f, 22.0f, 17.0f, 12.0f, 21.5f);
        put.pline(IconPainter.GREEN, 2.4f, 8.0f, 8.0f, 12.0f, 4.0f, 16.0f, 8.0f);
        put.line(12.0f, 4.0f, 12.0f, 12.0f, IconPainter.GREEN, 2.4f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$18(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.rect(5.0f, 3.0f, 9.0f, 15.0f, IconPainter.BROWN, 1.0f);
        put.rect(5.0f, 12.0f, 19.0f, 15.5f, IconPainter.BROWN, 1.0f);
        put.line(6.5f, 15.0f, 6.5f, 21.0f, -8760792, 2.0f);
        put.line(17.5f, 15.0f, 17.5f, 21.0f, -8760792, 2.0f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$19(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.poly(-8407312, 12.0f, 3.0f, 21.0f, 19.0f, 3.0f, 19.0f);
        put.spoly(IconPainter.DBLUE, 1.3f, 12.0f, 3.0f, 21.0f, 19.0f, 3.0f, 19.0f);
        put.pline(IconPainter.DBLUE, 1.1f, 7.5f, 11.0f, 12.0f, 19.0f, 16.5f, 11.0f, 7.5f, 11.0f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$20(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.rect(3.0f, 9.0f, 14.0f, 20.0f, -8599169, 1.5f);
        put.circle(15.0f, 9.5f, 6.5f, IconPainter.GREEN);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$21(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.rect(3.0f, 9.0f, 14.0f, 20.0f, -8599169, 1.5f);
        put.circle(15.0f, 9.5f, 6.5f, IconPainter.GREEN);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$22(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.srect(3.0f, 9.0f, 14.0f, 20.0f, IconPainter.GREEN, 1.4f, 1.5f);
        put.ring(15.0f, 9.5f, 6.5f, IconPainter.GREEN, 1.4f);
        put.pie(8.5f, 3.0f, 21.5f, 16.0f, 90.0f, 90.0f, IconPainter.GREEN);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$23(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.srect(3.0f, 9.0f, 14.0f, 20.0f, IconPainter.RED, 1.4f, 1.5f);
        put.circle(15.0f, 9.5f, 6.5f, -1013107);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$24(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.poly(-7706043, 1.0f, 20.0f, 9.0f, 6.0f, 15.0f, 15.0f, 18.0f, 10.0f, 23.0f, 20.0f);
        put.poly(IconPainter.GREEN, 1.0f, 20.0f, 5.0f, 13.0f, 9.0f, 16.0f, 13.0f, 13.0f, 18.0f, 17.0f, 23.0f, 20.0f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$25(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.gear(12.0f, 12.0f, 8.0f, -7498852, Ui.INSTANCE.getPANEL());
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$26(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.circle(12.0f, 12.0f, 9.5f, IconPainter.BLUE);
        put.poly(IconPainter.GREEN, 7.0f, 6.0f, 11.0f, 5.0f, 12.5f, 8.0f, 10.0f, 11.0f, 11.0f, 14.0f, 8.0f, 16.0f, 6.0f, 12.0f, 5.0f, 9.0f);
        put.poly(IconPainter.GREEN, 15.0f, 11.0f, 19.0f, 10.0f, 20.0f, 14.0f, 17.0f, 18.0f, 15.0f, 15.0f);
        put.ring(12.0f, 12.0f, 9.5f, IconPainter.DBLUE, 1.2f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$27(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.rect(3.0f, 7.0f, 21.0f, 19.0f, IconPainter.DARK, 2.0f);
        put.rect(8.0f, 4.5f, 13.0f, 7.5f, IconPainter.DARK, 1.0f);
        put.circle(12.0f, 13.0f, 4.3f, -6306064);
        put.ring(12.0f, 13.0f, 4.3f, -3091238, 1.3f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$28(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.person(10.0f, 13.0f, 9.0f, IconPainter.BLUE);
        put.poly(IconPainter.GREEN, 16.0f, 13.0f, 22.0f, 16.5f, 16.0f, 20.0f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$29(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.circle(12.0f, 11.0f, 9.0f, 871547440);
        put.bulb(12.0f, 11.0f, IconPainter.YELLOW);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$30(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.poly(1727185456, 9.0f, 6.0f, 15.0f, 6.0f, 21.0f, 22.0f, 3.0f, 22.0f);
        put.rect(8.0f, 2.0f, 16.0f, 7.0f, -7498852, 1.0f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$31(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.rect(4.0f, 3.0f, 20.0f, 7.0f, -7498852, 1.0f);
        Iterator it = CollectionsKt.listOf(new Float[]{Float.valueOf(7.0f), Float.valueOf(12.0f), Float.valueOf(17.0f)}).iterator();
        while (it.hasNext()) {
            float x = ((Number) it.next()).floatValue();
            put.line(x, 9.0f, x, 20.0f, IconPainter.YELLOW, 1.8f);
        }
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$32(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.circle(12.0f, 12.0f, 9.0f, -5197643);
        put.pie(3.0f, 3.0f, 21.0f, 21.0f, -45.0f, 180.0f, IconPainter.GRAY_R);
        put.circle(9.0f, 9.0f, 2.0f, 1728053247);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$33(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.rect(3.0f, 8.0f, 17.0f, 20.0f, IconPainter.TEAL, 1.5f);
        put.rect(3.0f, 8.0f, 17.0f, 11.0f, -14709630, 1.0f);
        put.pline(IconPainter.BLUE, 2.2f, 14.0f, 3.5f, 21.0f, 3.5f);
        put.arrowHead(21.0f, 3.5f, 90.0f, 2.2f, IconPainter.BLUE);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$34(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.rect(3.0f, 7.0f, 21.0f, 20.0f, IconPainter.TEAL, 1.5f);
        put.rect(2.0f, 5.0f, 22.0f, 9.0f, -14709630, 1.0f);
        put.rect(9.0f, 11.0f, 15.0f, 13.0f, -1, 0.8f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$35(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        for (int i = 0; i < 3; i++) {
            float y = 3.5f + (i * 6.2f);
            put.rect(3.0f, y, 21.0f, y + 5.0f, IconPainter.DARK, 1.2f);
            put.circle(18.0f, y + 2.5f, 1.0f, IconPainter.GREEN);
            put.line(6.0f, y + 2.5f, 12.0f, y + 2.5f, -7498852, 1.2f);
        }
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$36(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        for (int i = 0; i < 2; i++) {
            float y = 3.5f + (i * 6.2f);
            put.rect(2.0f, y, 15.0f, y + 5.0f, IconPainter.DARK, 1.2f);
            put.circle(12.5f, 2.5f + y, 1.0f, IconPainter.GREEN);
        }
        put.rect(11.0f, 10.0f, 22.0f, 22.0f, IconPainter.PAPER, 1.0f);
        IconPainter.rect$default(put, 11.0f, 10.0f, 13.0f, 22.0f, IconPainter.BLUE, 0.0f, 32, null);
        put.line(15.0f, 14.0f, 20.0f, 14.0f, IconPainter.BLUE, 1.2f);
        put.line(15.0f, 17.5f, 19.0f, 17.5f, IconPainter.BLUE, 1.2f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$37(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.rect(5.0f, 7.0f, 19.0f, 21.0f, IconPainter.BROWN, 3.0f);
        put.arc(8.0f, 2.5f, 16.0f, 10.0f, 180.0f, 180.0f, -8760792, 1.8f);
        put.rect(5.0f, 11.0f, 19.0f, 15.0f, -7446740, 1.0f);
        put.rect(10.5f, 12.0f, 13.5f, 14.0f, IconPainter.YELLOW, 0.5f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$38(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.rect(5.0f, 7.0f, 19.0f, 21.0f, IconPainter.BROWN, 3.0f);
        put.arc(8.0f, 2.5f, 16.0f, 10.0f, 180.0f, 180.0f, -8760792, 1.8f);
        put.rect(5.0f, 11.0f, 19.0f, 15.0f, -7446740, 1.0f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$39(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.line(5.0f, 3.0f, 5.0f, 21.0f, IconPainter.DARK, 1.8f);
        put.poly(IconPainter.RED, 5.5f, 4.0f, 19.0f, 7.0f, 5.5f, 11.0f);
        put.poly(IconPainter.BLUE, 5.5f, 11.0f, 19.0f, 14.0f, 5.5f, 17.0f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$40(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.line(5.0f, 3.0f, 5.0f, 21.0f, IconPainter.DARK, 1.8f);
        put.poly(IconPainter.BLUE, 5.5f, 4.0f, 20.0f, 8.5f, 5.5f, 13.0f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$41(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.speaker(IconPainter.BLUE);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$42(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.speaker(IconPainter.BLUE);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$43(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.speaker(IconPainter.TEAL);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$44(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.rect(2.5f, 4.0f, 21.5f, 16.0f, IconPainter.BLUE, 4.0f);
        put.poly(IconPainter.BLUE, 6.0f, 15.0f, 5.0f, 21.0f, 11.0f, 15.5f);
        Iterator it = CollectionsKt.listOf(new Float[]{Float.valueOf(8.0f), Float.valueOf(12.0f), Float.valueOf(16.0f)}).iterator();
        while (it.hasNext()) {
            float x = ((Number) it.next()).floatValue();
            put.circle(x, 10.0f, 1.4f, -1);
        }
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$45(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.rect(2.5f, 4.0f, 21.5f, 16.0f, IconPainter.BLUE, 4.0f);
        put.poly(IconPainter.BLUE, 6.0f, 15.0f, 5.0f, 21.0f, 11.0f, 15.5f);
        Iterator it = CollectionsKt.listOf(new Float[]{Float.valueOf(8.0f), Float.valueOf(12.0f), Float.valueOf(16.0f)}).iterator();
        while (it.hasNext()) {
            float x = ((Number) it.next()).floatValue();
            put.circle(x, 10.0f, 1.4f, -1);
        }
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$46(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.circle(12.0f, 12.0f, 9.5f, IconPainter.TEAL);
        IconPainter.text$default(put, "A", 12.0f, 12.0f, 11.0f, -1, false, 32, null);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$47(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.circle(12.0f, 12.0f, 9.5f, IconPainter.GREEN);
        put.pline(-1, 2.4f, 7.5f, 12.5f, 10.8f, 15.8f, 16.8f, 8.5f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$48(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.monitor(IconPainter.LBLUE);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$49(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.rect(3.0f, 3.0f, 21.0f, 13.0f, IconPainter.GUI, 1.5f);
        put.line(12.0f, 13.0f, 12.0f, 21.0f, IconPainter.DARK, 2.0f);
        put.line(6.0f, 7.0f, 18.0f, 7.0f, -1, 1.3f);
        put.line(6.0f, 10.0f, 14.0f, 10.0f, -1, 1.3f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$50(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.poly(IconPainter.GUI, 4.0f, 7.0f, 20.0f, 3.0f, 20.0f, 17.0f, 4.0f, 21.0f);
        put.line(7.0f, 10.0f, 17.0f, 7.5f, -1, 1.3f);
        put.line(7.0f, 13.5f, 14.0f, 12.0f, -1, 1.3f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$51(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.frame(-4660536, IconPainter.GUI);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$52(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.frame(-4660536, IconPainter.GUI);
        IconPainter.text$default(put, "T", 12.0f, 12.0f, 11.0f, -13726889, false, 32, null);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$53(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.rect(2.5f, 6.0f, 21.5f, 18.0f, IconPainter.GUI, 4.0f);
        IconPainter.text$default(put, "T", 12.0f, 12.0f, 10.0f, -1, false, 32, null);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$54(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.frame(-1, IconPainter.GUI);
        put.line(12.0f, 8.0f, 12.0f, 16.0f, IconPainter.DARK, 1.5f);
        put.line(10.0f, 8.0f, 14.0f, 8.0f, IconPainter.DARK, 1.2f);
        put.line(10.0f, 16.0f, 14.0f, 16.0f, IconPainter.DARK, 1.2f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$55(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.srect(3.0f, 5.0f, 21.0f, 19.0f, IconPainter.GUI, 1.4f, 1.5f);
        put.picture(4.5f, 6.5f, 19.5f, 17.5f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$56(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.rect(2.5f, 4.5f, 21.5f, 19.5f, IconPainter.GUI, 4.0f);
        put.picture(5.0f, 7.0f, 19.0f, 17.0f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$57(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.frame(-4660536, IconPainter.GUI);
        put.rect(17.0f, 7.0f, 19.5f, 12.0f, -13726889, 1.0f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$58(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.frame(-4660536, IconPainter.GUI);
        IconPainter.cube$default(put, 12.0f, 11.5f, 5.0f, 0, 0, 0, 56, null);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$59(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.box(7.0f, 3.0f, 21.0f, 15.0f, -4660536, IconPainter.GUI, (192 & 64) != 0 ? 1.3f : 0.0f, (192 & 128) != 0 ? 1.5f : 0.0f);
        put.box(3.0f, 9.0f, 17.0f, 21.0f, -2691105, IconPainter.GUI, (192 & 64) != 0 ? 1.3f : 0.0f, (192 & 128) != 0 ? 1.5f : 0.0f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$60(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.frame(IconPainter.DARK, IconPainter.GUI);
        put.poly(-1, 10.0f, 8.5f, 16.0f, 12.0f, 10.0f, 15.5f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$61(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.pline(IconPainter.GUI, 2.6f, 5.0f, 20.0f, 5.0f, 12.0f);
        put.arc(5.0f, 5.0f, 19.0f, 19.0f, 180.0f, 90.0f, IconPainter.GUI, 2.6f);
        put.line(12.0f, 5.0f, 20.0f, 5.0f, IconPainter.GUI, 2.6f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$62(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.srect(4.0f, 6.0f, 20.0f, 18.0f, IconPainter.GUI, 3.0f, 2.0f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$63(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.gradientRect(3.0f, 6.0f, 21.0f, 18.0f, IconPainter.DBLUE, IconPainter.RED, (64 & 64) != 0 ? 1.5f : 0.0f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$64(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.srect(3.0f, 4.0f, 21.0f, 20.0f, IconPainter.GUI, 1.4f, 1.0f);
        put.rect(7.0f, 8.0f, 17.0f, 16.0f, -4660536, 1.0f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$65(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        for (int i = 0; i < 3; i++) {
            put.rect(4.0f, (i * 5.5f) + 4.5f, 20.0f, (i * 5.5f) + 8.5f, IconPainter.GUI, 1.0f);
        }
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$66(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                put.rect((i * 8.5f) + 4.0f, (j * 8.5f) + 4.0f, (i * 8.5f) + 11.5f, (j * 8.5f) + 11.5f, IconPainter.GUI, 1.0f);
            }
        }
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$67(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        IconPainter.srect$default(put, 3.0f, 4.0f, 21.0f, 20.0f, IconPainter.GUI, 1.4f, 0.0f, 64, null);
        put.line(3.0f, 9.5f, 21.0f, 9.5f, IconPainter.GUI, 1.2f);
        put.line(3.0f, 15.0f, 21.0f, 15.0f, IconPainter.GUI, 1.2f);
        put.line(10.0f, 4.0f, 10.0f, 20.0f, IconPainter.GUI, 1.2f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$68(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.box(7.0f, 3.0f, 20.0f, 17.0f, -2691105, IconPainter.GUI, (192 & 64) != 0 ? 1.3f : 0.0f, (192 & 128) != 0 ? 1.5f : 0.0f);
        put.box(4.0f, 7.0f, 17.0f, 21.0f, -4660536, IconPainter.GUI, (192 & 64) != 0 ? 1.3f : 0.0f, (192 & 128) != 0 ? 1.5f : 0.0f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$69(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.srect(3.0f, 3.0f, 21.0f, 21.0f, IconPainter.GUI, 1.4f, 1.0f);
        put.line(7.0f, 17.0f, 17.0f, 7.0f, -13726889, 2.0f);
        put.arrowHead(17.0f, 7.0f, 45.0f, 2.5f, -13726889);
        put.arrowHead(7.0f, 17.0f, 225.0f, 2.5f, -13726889);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$70(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.srect(3.0f, 6.0f, 21.0f, 18.0f, IconPainter.GUI, 1.6f, 1.0f);
        IconPainter.text$default(put, "16:9", 12.0f, 12.0f, 6.5f, -13726889, false, 32, null);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$71(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.srect(3.0f, 6.0f, 21.0f, 18.0f, IconPainter.GUI, 1.6f, 1.0f);
        put.line(6.0f, 12.0f, 18.0f, 12.0f, -13726889, 1.6f);
        put.arrowHead(6.0f, 12.0f, 270.0f, 2.3f, -13726889);
        put.arrowHead(18.0f, 12.0f, 90.0f, 2.3f, -13726889);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$72(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.srect(3.0f, 6.0f, 21.0f, 18.0f, IconPainter.GUI, 1.6f, 1.0f);
        IconPainter.text$default(put, "Tt", 12.0f, 12.0f, 8.0f, -13726889, false, 32, null);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$73(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.poly(-5197643, 3.0f, 5.0f, 21.0f, 3.0f, 21.0f, 21.0f, 3.0f, 19.0f);
        put.picture(5.0f, 6.0f, 19.0f, 18.0f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$74(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                IconPainter.rect$default(put, (i * 6.0f) + 3.0f, (j * 6.0f) + 3.0f, (i * 6.0f) + 9.0f, (j * 6.0f) + 9.0f, (i + j) % 2 == 0 ? IconPainter.DARK : IconPainter.GRAY_T, 0.0f, 32, null);
            }
        }
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$75(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.spoly(IconPainter.DBLUE, 1.4f, 12.0f, 3.0f, 21.0f, 19.0f, 3.0f, 19.0f);
        put.pline(IconPainter.DBLUE, 1.1f, 7.5f, 11.0f, 12.0f, 19.0f, 16.5f, 11.0f, 7.5f, 11.0f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$76(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.spoly(IconPainter.DBLUE, 1.4f, 12.0f, 3.0f, 20.0f, 7.5f, 20.0f, 16.5f, 12.0f, 21.0f, 4.0f, 16.5f, 4.0f, 7.5f);
        put.pline(IconPainter.DBLUE, 1.1f, 4.0f, 7.5f, 12.0f, 12.0f, 20.0f, 7.5f);
        put.line(12.0f, 12.0f, 12.0f, 21.0f, IconPainter.DBLUE, 1.1f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$77(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.arc(5.0f, 3.0f, 19.0f, 9.0f, 0.0f, 360.0f, IconPainter.DBLUE, 1.4f);
        put.line(5.0f, 6.0f, 5.0f, 18.0f, IconPainter.DBLUE, 1.4f);
        put.line(19.0f, 6.0f, 19.0f, 18.0f, IconPainter.DBLUE, 1.4f);
        put.arc(5.0f, 15.0f, 19.0f, 21.0f, 0.0f, 180.0f, IconPainter.DBLUE, 1.4f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$78(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.circle(14.0f, 4.5f, 2.3f, IconPainter.PURPLE);
        put.pline(IconPainter.PURPLE, 2.2f, 13.0f, 8.0f, 11.0f, 14.0f, 6.0f, 18.0f);
        put.pline(IconPainter.PURPLE, 2.2f, 11.0f, 14.0f, 15.0f, 17.0f, 15.0f, 22.0f);
        put.pline(IconPainter.PURPLE, 2.2f, 7.0f, 10.0f, 12.5f, 8.0f, 18.0f, 11.0f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$79(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.circle(14.0f, 4.5f, 2.3f, IconPainter.PURPLE);
        put.pline(IconPainter.PURPLE, 2.2f, 13.0f, 8.0f, 11.0f, 14.0f, 6.0f, 18.0f);
        put.pline(IconPainter.PURPLE, 2.2f, 11.0f, 14.0f, 15.0f, 17.0f, 15.0f, 22.0f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$80(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.rect(3.0f, 5.0f, 21.0f, 19.0f, IconPainter.PURPLE, 1.5f);
        Iterator it = CollectionsKt.listOf(new Float[]{Float.valueOf(5.0f), Float.valueOf(9.0f), Float.valueOf(13.0f), Float.valueOf(17.0f)}).iterator();
        while (it.hasNext()) {
            float x = ((Number) it.next()).floatValue();
            put.rect(x, 6.0f, x + 2.0f, 8.0f, -1, 0.4f);
            put.rect(x, 16.0f, x + 2.0f, 18.0f, -1, 0.4f);
        }
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$81(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.rect(3.0f, 5.0f, 21.0f, 19.0f, IconPainter.PURPLE, 1.5f);
        Iterator it = CollectionsKt.listOf(new Float[]{Float.valueOf(7.0f), Float.valueOf(12.0f), Float.valueOf(17.0f)}).iterator();
        while (it.hasNext()) {
            float x = ((Number) it.next()).floatValue();
            put.poly(-1, x, 9.0f, x + 2.5f, 12.0f, x, 15.0f, x - 2.5f, 12.0f);
        }
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$82(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.rect(4.0f, 3.0f, 16.0f, 9.0f, -7498852, 1.2f);
        put.line(10.0f, 9.0f, 10.0f, 21.0f, IconPainter.BROWN, 3.0f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$83(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.rect(4.0f, 3.0f, 16.0f, 9.0f, -7498852, 1.2f);
        put.line(10.0f, 9.0f, 10.0f, 21.0f, IconPainter.BROWN, 3.0f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$84(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.rect(2.0f, 15.0f, 22.0f, 18.0f, IconPainter.DARK, 1.5f);
        put.rect(6.0f, 5.0f, 18.0f, 16.0f, IconPainter.DARK, 1.5f);
        IconPainter.rect$default(put, 6.0f, 12.0f, 18.0f, 14.0f, IconPainter.RED, 0.0f, 32, null);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$85(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.rect(2.0f, 15.0f, 22.0f, 18.0f, IconPainter.DARK, 1.5f);
        put.rect(6.0f, 5.0f, 18.0f, 16.0f, IconPainter.DARK, 1.5f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$86(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.poly(IconPainter.BLUE, 8.0f, 3.0f, 16.0f, 3.0f, 22.0f, 8.0f, 19.0f, 11.0f, 17.0f, 9.0f, 17.0f, 21.0f, 7.0f, 21.0f, 7.0f, 9.0f, 5.0f, 11.0f, 2.0f, 8.0f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$87(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.poly(IconPainter.TEAL, 8.0f, 3.0f, 16.0f, 3.0f, 22.0f, 8.0f, 19.0f, 11.0f, 17.0f, 9.0f, 17.0f, 21.0f, 7.0f, 21.0f, 7.0f, 9.0f, 5.0f, 11.0f, 2.0f, 8.0f);
        put.circle(12.0f, 12.0f, 2.5f, -1);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$88(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.poly(-11904384, 6.0f, 3.0f, 18.0f, 3.0f, 20.0f, 21.0f, 14.0f, 21.0f, 12.0f, 9.0f, 10.0f, 21.0f, 4.0f, 21.0f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$89(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.circle(12.0f, 12.0f, 9.5f, -1518160);
        put.circle(8.0f, 9.0f, 2.0f, IconPainter.RED);
        put.circle(13.0f, 7.0f, 2.0f, IconPainter.YELLOW);
        put.circle(17.0f, 11.0f, 2.0f, IconPainter.BLUE);
        put.circle(9.0f, 15.0f, 2.0f, IconPainter.GREEN);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$90(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.ring(12.0f, 12.0f, 7.0f, IconPainter.GREEN, 2.2f);
        put.circle(12.0f, 12.0f, 2.6f, IconPainter.GREEN);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$91(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.line(6.0f, 18.0f, 18.0f, 6.0f, -1518160, 3.0f);
        put.circle(6.0f, 18.0f, 2.5f, -1518160);
        put.circle(18.0f, 6.0f, 2.5f, -1518160);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$92(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.chain(IconPainter.RED);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$93(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.circle(7.0f, 12.0f, 4.0f, -7498852);
        put.circle(17.0f, 12.0f, 4.0f, -7498852);
        put.line(7.0f, 12.0f, 17.0f, 12.0f, IconPainter.DARK, 2.5f);
        put.circle(17.0f, 12.0f, 1.5f, IconPainter.YELLOW);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$94(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.circle(7.0f, 12.0f, 4.0f, -7498852);
        put.circle(17.0f, 12.0f, 4.0f, -7498852);
        put.line(7.0f, 12.0f, 17.0f, 12.0f, IconPainter.DARK, 2.5f);
        put.circle(17.0f, 12.0f, 1.5f, IconPainter.YELLOW);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$95(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.poly(IconPainter.DARK, 7.0f, 4.0f, 7.0f, 19.0f, 10.5f, 15.5f, 13.0f, 21.0f, 15.5f, 20.0f, 13.0f, 14.5f, 18.0f, 14.5f);
        put.line(13.0f, 3.0f, 14.0f, 6.0f, IconPainter.ORANGE, 1.6f);
        put.line(17.0f, 6.0f, 15.0f, 8.0f, IconPainter.ORANGE, 1.6f);
        put.line(18.0f, 10.5f, 15.5f, 10.5f, IconPainter.ORANGE, 1.6f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$96(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.rect(3.0f, 3.0f, 21.0f, 21.0f, IconPainter.DARK, 4.0f);
        put.srect(5.5f, 5.5f, 18.5f, 18.5f, -1, 1.4f, 3.0f);
        IconPainter.text$default(put, "E", 12.0f, 12.0f, 9.0f, -1, false, 32, null);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$97(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        Float fValueOf = Float.valueOf(9.0f);
        Float fValueOf2 = Float.valueOf(15.0f);
        for (Triple triple : CollectionsKt.listOf(new Triple[]{new Triple(Float.valueOf(6.0f), Float.valueOf(7.0f), Float.valueOf(2.0f)), new Triple(Float.valueOf(14.0f), Float.valueOf(5.0f), Float.valueOf(1.4f)), new Triple(Float.valueOf(18.0f), Float.valueOf(12.0f), Float.valueOf(2.4f)), new Triple(fValueOf, fValueOf2, Float.valueOf(2.6f)), new Triple(fValueOf2, Float.valueOf(19.0f), Float.valueOf(1.6f))})) {
            float x = ((Number) triple.component1()).floatValue();
            float y = ((Number) triple.component2()).floatValue();
            float r = ((Number) triple.component3()).floatValue();
            put.circle(x, y, r, IconPainter.ORANGE);
        }
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$98(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.poly(IconPainter.ORANGE, 12.0f, 2.0f, 18.0f, 11.0f, 18.0f, 16.0f, 15.0f, 21.0f, 9.0f, 21.0f, 6.0f, 16.0f, 6.0f, 11.0f, 9.0f, 13.0f);
        put.poly(IconPainter.YELLOW, 12.0f, 9.0f, 15.0f, 15.0f, 14.0f, 19.0f, 10.0f, 19.0f, 9.0f, 15.0f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$99(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.cloud(12.0f, 12.0f, 8.0f, -6643544);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$100(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.poly(IconPainter.YELLOW, 12.0f, 2.0f, 14.0f, 10.0f, 22.0f, 12.0f, 14.0f, 14.0f, 12.0f, 22.0f, 10.0f, 14.0f, 2.0f, 12.0f, 10.0f, 10.0f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$101(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.arc(2.0f, 6.0f, 14.0f, 18.0f, 180.0f, 180.0f, IconPainter.PURPLE, 2.4f);
        put.arc(12.0f, 6.0f, 22.0f, 18.0f, 0.0f, 180.0f, IconPainter.PURPLE, 2.4f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$102(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.circle(4.0f, 18.0f, 2.5f, IconPainter.TEAL);
        put.circle(20.0f, 6.0f, 2.5f, IconPainter.TEAL);
        put.pline(-8397613, 3.0f, 4.0f, 18.0f, 10.0f, 9.0f, 14.0f, 15.0f, 20.0f, 6.0f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$103(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.poly(IconPainter.ORANGE, 12.0f, 1.0f, 14.5f, 8.0f, 22.0f, 6.0f, 16.5f, 12.0f, 22.0f, 18.0f, 14.5f, 16.0f, 12.0f, 23.0f, 9.5f, 16.0f, 2.0f, 18.0f, 7.5f, 12.0f, 2.0f, 6.0f, 9.5f, 8.0f);
        put.circle(12.0f, 12.0f, 3.5f, IconPainter.YELLOW);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$104(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.rect(5.0f, 5.0f, 19.0f, 19.0f, 1441089613, 2.0f);
        put.srect(5.0f, 5.0f, 19.0f, 19.0f, IconPainter.RED, 2.0f, 2.0f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$105(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.spoly(IconPainter.BLUE, 2.0f, 12.0f, 2.0f, 20.5f, 7.0f, 20.5f, 17.0f, 12.0f, 22.0f, 3.5f, 17.0f, 3.5f, 7.0f);
        put.poly(1144753131, 12.0f, 2.0f, 20.5f, 7.0f, 20.5f, 17.0f, 12.0f, 22.0f, 3.5f, 17.0f, 3.5f, 7.0f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$106(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        IconPainter.srect$default(put, 4.0f, 4.0f, 20.0f, 20.0f, IconPainter.BLUE, 1.8f, 0.0f, 64, null);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$107(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.circle(16.0f, 8.0f, 4.5f, IconPainter.YELLOW);
        put.cloud(10.0f, 14.0f, 7.0f, -1);
        put.cloud(10.0f, 14.0f, 6.0f, -2299915);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$108(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.cloud(12.0f, 13.0f, 8.0f, -4667430);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$109(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.arc(2.0f, 8.0f, 22.0f, 28.0f, 180.0f, 180.0f, IconPainter.LBLUE, 2.4f);
        put.arc(5.0f, 11.0f, 19.0f, 25.0f, 180.0f, 180.0f, IconPainter.BLUE, 2.4f);
        put.arc(8.0f, 14.0f, 16.0f, 22.0f, 180.0f, 180.0f, IconPainter.DBLUE, 2.4f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$110(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.circle(12.0f, 12.0f, 8.0f, 1442829133);
        put.circle(12.0f, 12.0f, 4.5f, IconPainter.YELLOW);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$111(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.circle(12.0f, 12.0f, 8.0f, 1144753131);
        put.circle(12.0f, 12.0f, 5.0f, -2009363477);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$112(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.pie(3.0f, 3.0f, 21.0f, 21.0f, 0.0f, 120.0f, IconPainter.RED);
        put.pie(3.0f, 3.0f, 21.0f, 21.0f, 120.0f, 120.0f, IconPainter.GREEN);
        put.pie(3.0f, 3.0f, 21.0f, 21.0f, 240.0f, 120.0f, IconPainter.BLUE);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$113(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.circle(12.0f, 12.0f, 4.0f, IconPainter.YELLOW);
        for (int i = 0; i < 8; i++) {
            double a = Math.toRadians(((double) i) * 45.0d);
            put.line((((float) Math.cos(a)) * 6.0f) + 12.0f, (((float) Math.sin(a)) * 6.0f) + 12.0f, (((float) Math.cos(a)) * 9.5f) + 12.0f, (((float) Math.sin(a)) * 9.5f) + 12.0f, IconPainter.YELLOW, 1.6f);
        }
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$114(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.ring(12.0f, 12.0f, 8.0f, IconPainter.DARK, 2.0f);
        put.ring(12.0f, 12.0f, 4.0f, IconPainter.BLUE, 2.0f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$115(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.rect(3.0f, 3.0f, 21.0f, 21.0f, IconPainter.ORANGE, 4.0f);
        put.bolt(12.0f, 12.0f, 7.0f, -1);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$116(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.rect(3.0f, 3.0f, 21.0f, 21.0f, -3573206, 4.0f);
        put.bolt(12.0f, 12.0f, 7.0f, -1);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$117(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.rect(3.0f, 3.0f, 21.0f, 21.0f, IconPainter.PURPLE, 4.0f);
        IconPainter.text$default(put, "f", 12.0f, 12.0f, 13.0f, -1, false, 32, null);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$118(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.rect(3.0f, 3.0f, 21.0f, 21.0f, IconPainter.GREEN, 4.0f);
        put.bolt(12.0f, 12.0f, 7.0f, -1);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$119(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.rect(3.0f, 3.0f, 21.0f, 21.0f, IconPainter.GREEN, 4.0f);
        IconPainter.text$default(put, "f", 12.0f, 12.0f, 13.0f, -1, false, 32, null);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$120(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.rect(4.0f, 7.0f, 18.0f, 21.0f, IconPainter.BLUE, 2.0f);
        put.circle(11.0f, 6.5f, 3.0f, IconPainter.BLUE);
        put.circle(18.5f, 14.0f, 3.0f, IconPainter.BLUE);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$121(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.rect(2.0f, 3.0f, 15.0f, 12.0f, IconPainter.BLUE, 3.0f);
        put.rect(9.0f, 10.0f, 22.0f, 19.0f, IconPainter.TEAL, 3.0f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$122(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.rect(4.0f, 6.0f, 20.0f, 18.0f, IconPainter.TEAL, 3.0f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$123(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.poly(IconPainter.DARK, 7.0f, 4.0f, 7.0f, 19.0f, 10.5f, 15.5f, 13.0f, 21.0f, 15.5f, 20.0f, 13.0f, 14.5f, 18.0f, 14.5f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$124(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.circle(12.0f, 12.0f, 9.5f, IconPainter.BLUE);
        put.ring(12.0f, 12.0f, 9.5f, IconPainter.DBLUE, 1.2f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$125(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.rect(3.0f, 6.0f, 21.0f, 18.0f, IconPainter.DARK, 2.0f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$126(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.rect(3.0f, 9.0f, 21.0f, 15.0f, IconPainter.DARK, 1.0f);
        return Unit.INSTANCE;
    }

    static final Unit instanceIcons$lambda$128$lambda$127(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.rect(5.0f, 3.0f, 9.0f, 15.0f, IconPainter.DARK, 1.0f);
        put.rect(5.0f, 12.0f, 19.0f, 15.5f, IconPainter.DARK, 1.0f);
        put.circle(7.0f, 19.0f, 2.5f, IconPainter.DARK);
        put.circle(17.0f, 19.0f, 2.5f, IconPainter.DARK);
        return Unit.INSTANCE;
    }

    public final Drawable instance(String cls) {
        Intrinsics.checkNotNullParameter(cls, "cls");
        String c = cls;
        int guard = 0;
        while (c != null && guard < 12) {
            guard++;
            Function1<IconPainter, Unit> function1 = instanceIcons.get(c);
            if (function1 != null) {
                return new VectorIcon(function1);
            }
            if (Intrinsics.areEqual(c, "Instance")) {
                break;
            }
            if (StringsKt.endsWith(c, "Constraint", false) && ClassDb.INSTANCE.isA(c, "Constraint")) {
                return new VectorIcon(constraint);
            }
            c = ClassDb.INSTANCE.superclass(c);
        }
        return null;
    }

    static final Unit generalIcons$lambda$202$lambda$130(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.poly(put.getFg(), 6.0f, 2.5f, 6.0f, 19.5f, 10.0f, 15.5f, 13.0f, 22.0f, 16.0f, 20.8f, 13.0f, 14.5f, 18.5f, 14.5f);
        put.spoly(Ui.INSTANCE.getPANEL(), 1.0f, 6.0f, 2.5f, 6.0f, 19.5f, 10.0f, 15.5f, 13.0f, 22.0f, 16.0f, 20.8f, 13.0f, 14.5f, 18.5f, 14.5f);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$131(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.line(12.0f, 3.0f, 12.0f, 21.0f, put.getFg(), 2.0f);
        put.line(3.0f, 12.0f, 21.0f, 12.0f, put.getFg(), 2.0f);
        put.arrowHead(12.0f, 3.0f, 0.0f, 3.0f, IconPainter.GREEN);
        put.arrowHead(12.0f, 21.0f, 180.0f, 3.0f, IconPainter.GREEN);
        put.arrowHead(3.0f, 12.0f, 270.0f, 3.0f, IconPainter.RED);
        put.arrowHead(21.0f, 12.0f, 90.0f, 3.0f, IconPainter.RED);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$132(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.srect(3.0f, 9.0f, 15.0f, 21.0f, put.getFg(), 1.8f, 1.0f);
        put.line(10.0f, 14.0f, 20.0f, 4.0f, IconPainter.BLUE, 2.0f);
        put.arrowHead(20.5f, 3.5f, 45.0f, 3.0f, IconPainter.BLUE);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$133(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.arc(4.0f, 4.0f, 20.0f, 20.0f, 200.0f, 280.0f, put.getFg(), 2.2f);
        put.arrowHead(5.0f, 9.5f, 200.0f, 3.2f, IconPainter.BLUE);
        put.circle(12.0f, 12.0f, 2.0f, IconPainter.BLUE);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$134(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.circle(12.0f, 12.0f, 9.0f, IconPainter.GRAY_L);
        put.circle(9.5f, 9.0f, 4.5f, IconPainter.GRAY_T);
        put.circle(13.5f, 14.5f, 4.5f, 570425344);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$135(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        IconPainter.rect$default(put, 5.0f, 7.0f, 19.0f, 18.0f, IconPainter.GRAY_L, 0.0f, 32, null);
        put.arc(5.0f, 15.0f, 19.0f, 21.0f, 0.0f, 180.0f, IconPainter.GRAY_R, 0.1f);
        put.pie(5.0f, 15.0f, 19.0f, 21.0f, 0.0f, 180.0f, IconPainter.GRAY_L);
        put.pie(5.0f, 4.0f, 19.0f, 10.0f, 0.0f, 360.0f, IconPainter.GRAY_T);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$136(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.poly(IconPainter.GRAY_L, 3.0f, 20.0f, 21.0f, 20.0f, 21.0f, 5.0f);
        put.poly(IconPainter.GRAY_T, 3.0f, 20.0f, 21.0f, 5.0f, 21.0f, 8.5f, 6.5f, 20.0f);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$137(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.poly(IconPainter.GRAY_L, 3.0f, 20.0f, 12.0f, 3.0f, 12.0f, 20.0f);
        put.poly(IconPainter.GRAY_R, 12.0f, 3.0f, 21.0f, 20.0f, 12.0f, 20.0f);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$138(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.srect(3.0f, 3.0f, 21.0f, 21.0f, put.getFg(), 1.6f, 3.0f);
        put.line(12.0f, 7.0f, 12.0f, 17.0f, IconPainter.GREEN, 2.4f);
        put.line(7.0f, 12.0f, 17.0f, 12.0f, IconPainter.GREEN, 2.4f);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$139(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.ring(12.0f, 5.0f, 2.2f, put.getFg(), 1.8f);
        put.line(12.0f, 7.0f, 12.0f, 20.0f, put.getFg(), 2.0f);
        put.line(8.0f, 10.0f, 16.0f, 10.0f, put.getFg(), 2.0f);
        put.arc(4.0f, 10.0f, 20.0f, 21.0f, 20.0f, 140.0f, put.getFg(), 2.0f);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$140(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.srect(2.5f, 2.5f, 21.5f, 21.5f, IconPainter.BLUE, 1.4f, 2.0f);
        IconPainter.cube$default(put, 8.5f, 10.0f, 4.5f, 0, 0, 0, 56, null);
        IconPainter.cube$default(put, 15.5f, 13.5f, 4.5f, 0, 0, 0, 56, null);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$141(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.rect(3.0f, 7.0f, 15.0f, 19.0f, IconPainter.BLUE, 1.5f);
        put.circle(15.0f, 10.0f, 6.5f, IconPainter.BLUE);
        put.circle(15.0f, 10.0f, 4.6f, IconPainter.LBLUE);
        put.rect(5.0f, 9.0f, 13.0f, 17.0f, IconPainter.LBLUE, 1.0f);
        IconPainter.rect$default(put, 9.0f, 9.0f, 15.0f, 15.0f, IconPainter.LBLUE, 0.0f, 32, null);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$142(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.srect(3.0f, 7.0f, 15.0f, 19.0f, IconPainter.GRAY_L, 1.4f, 1.5f);
        put.srect(9.0f, 3.0f, 21.0f, 15.0f, IconPainter.GRAY_L, 1.4f, 1.5f);
        put.rect(9.0f, 7.0f, 15.0f, 15.0f, IconPainter.BLUE, 0.5f);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$143(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.rect(3.0f, 7.0f, 15.0f, 19.0f, IconPainter.GRAY_L, 1.5f);
        put.circle(15.0f, 10.0f, 6.5f, -1611124);
        put.circle(15.0f, 10.0f, 4.6f, -744520);
        put.line(12.0f, 10.0f, 18.0f, 10.0f, -1, 1.8f);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$144(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.rect(2.5f, 9.0f, 11.0f, 18.0f, IconPainter.BLUE, 1.5f);
        put.circle(17.0f, 8.0f, 5.0f, IconPainter.BLUE);
        put.line(9.0f, 6.0f, 14.0f, 13.0f, IconPainter.GRAY_L, 1.4f);
        put.pline(IconPainter.GRAY_L, 1.4f, 4.0f, 21.0f, 12.0f, 21.0f);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$145(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.srect(3.0f, 5.0f, 21.0f, 19.0f, IconPainter.BLUE, 1.5f, 1.0f);
        put.rect(7.0f, 9.0f, 13.0f, 15.0f, IconPainter.GRAY_L, 1.0f);
        put.pline(-1, 1.6f, 14.0f, 13.0f, 19.0f, 18.0f);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$146(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        IconPainter.cube$default(put, 7.0f, 9.0f, 4.5f, 0, 0, 0, 56, null);
        IconPainter.cube$default(put, 17.0f, 15.0f, 4.5f, 0, 0, 0, 56, null);
        put.line(4.0f, 20.0f, 20.0f, 4.0f, IconPainter.RED, 1.6f);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$147(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.box(3.0f, 3.0f, 15.0f, 15.0f, IconPainter.GRAY_T, IconPainter.GRAY_R, (192 & 64) != 0 ? 1.3f : 0.0f, (192 & 128) != 0 ? 1.5f : 0.0f);
        put.box(9.0f, 9.0f, 21.0f, 21.0f, IconPainter.LBLUE, IconPainter.DBLUE, (192 & 64) != 0 ? 1.3f : 0.0f, (192 & 128) != 0 ? 1.5f : 0.0f);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$148(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.rect(6.0f, 7.0f, 18.0f, 21.0f, IconPainter.RED, 1.5f);
        put.rect(4.0f, 4.5f, 20.0f, 7.0f, IconPainter.RED, 1.0f);
        put.rect(9.5f, 2.5f, 14.5f, 4.5f, IconPainter.RED, 0.8f);
        Iterator it = CollectionsKt.listOf(new Float[]{Float.valueOf(9.5f), Float.valueOf(12.0f), Float.valueOf(14.5f)}).iterator();
        while (it.hasNext()) {
            float x = ((Number) it.next()).floatValue();
            put.line(x, 10.0f, x, 18.0f, -1, 1.2f);
        }
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$149(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.magnifier(put.getFg());
        put.circle(10.0f, 10.0f, 2.0f, IconPainter.BLUE);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$150(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.magnifier(put.getFg());
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$151(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        for (int i = 0; i < 4; i++) {
            put.line((i * 5.33f) + 4.0f, 3.0f, (i * 5.33f) + 4.0f, 21.0f, Ui.INSTANCE.getDIM(), 1.0f);
            put.line(3.0f, (i * 5.33f) + 4.0f, 21.0f, (i * 5.33f) + 4.0f, Ui.INSTANCE.getDIM(), 1.0f);
        }
        put.circle(14.7f, 9.3f, 2.6f, IconPainter.BLUE);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$152(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.line(4.0f, 20.0f, 20.0f, 20.0f, put.getFg(), 1.8f);
        put.line(4.0f, 20.0f, 16.0f, 6.0f, put.getFg(), 1.8f);
        put.arc(-4.0f, 12.0f, 12.0f, 28.0f, -50.0f, 50.0f, IconPainter.BLUE, 2.0f);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$153(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.circle(12.0f, 12.0f, 9.5f, -1518160);
        put.circle(8.0f, 8.0f, 2.2f, IconPainter.RED);
        put.circle(13.5f, 6.5f, 2.2f, IconPainter.YELLOW);
        put.circle(17.5f, 11.0f, 2.2f, IconPainter.BLUE);
        put.circle(8.0f, 14.0f, 2.2f, IconPainter.GREEN);
        put.circle(14.0f, 16.5f, 2.5f, Ui.INSTANCE.getPANEL());
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$154(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.circle(12.0f, 12.0f, 9.5f, -6522284);
        for (int i = 0; i < 4; i++) {
            put.arc((i * 1.5f) + 3.0f, (i * 3.0f) + 6.0f, 21.0f - (i * 1.5f), 30.0f, 225.0f, 90.0f, -8758728, 1.3f);
        }
        put.circle(8.5f, 8.5f, 2.5f, 1157627903);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$155(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        IconPainter.cube$default(put, 12.0f, 12.0f, 9.0f, 0, 0, 0, 56, null);
        Float fValueOf = Float.valueOf(12.0f);
        Float fValueOf2 = Float.valueOf(8.5f);
        Float fValueOf3 = Float.valueOf(8.3f);
        for (Pair pair : CollectionsKt.listOf(new Pair[]{TuplesKt.to(fValueOf, Float.valueOf(6.5f)), TuplesKt.to(fValueOf2, fValueOf3), TuplesKt.to(Float.valueOf(15.5f), fValueOf3), TuplesKt.to(fValueOf, Float.valueOf(10.0f))})) {
            float x = ((Number) pair.component1()).floatValue();
            float y = ((Number) pair.component2()).floatValue();
            put.circle(x, y, 1.1f, IconPainter.GRAY_R);
        }
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$156(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.poly(IconPainter.RED, 4.0f, 15.0f, 13.0f, 6.0f, 20.0f, 13.0f, 11.0f, 22.0f);
        put.poly(-673863, 4.0f, 15.0f, 8.0f, 11.0f, 15.0f, 18.0f, 11.0f, 22.0f);
        put.line(2.0f, 22.0f, 22.0f, 22.0f, put.getFg(), 1.4f);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$157(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.rect(2.0f, 4.0f, 22.0f, 20.0f, IconPainter.DARK, 2.0f);
        put.pline(IconPainter.GREEN, 1.8f, 6.0f, 9.0f, 9.0f, 12.0f, 6.0f, 15.0f);
        put.line(11.0f, 15.0f, 16.0f, 15.0f, -1, 1.8f);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$158(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.rect(3.0f, 3.0f, 9.0f, 8.0f, IconPainter.YELLOW, 1.0f);
        put.line(6.0f, 8.0f, 6.0f, 18.5f, put.getFg(), 1.4f);
        put.line(6.0f, 12.5f, 11.0f, 12.5f, put.getFg(), 1.4f);
        put.line(6.0f, 18.5f, 11.0f, 18.5f, put.getFg(), 1.4f);
        put.rect(11.0f, 10.0f, 20.0f, 15.0f, IconPainter.BLUE, 1.0f);
        put.rect(11.0f, 16.0f, 20.0f, 21.0f, IconPainter.GRAY_L, 1.0f);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$159(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        int i = 0;
        while (i < 4) {
            float y = 4.5f + (i * 5.0f);
            put.line(3.0f, y, 9.0f, y, put.getFg(), 1.5f);
            put.rect(11.0f, y - 1.6f, 21.0f, y + 1.6f, i == 1 ? IconPainter.BLUE : IconPainter.GRAY_L, 1.0f);
            i++;
        }
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$160(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.rect(2.0f, 8.0f, 22.0f, 20.0f, IconPainter.RED, 2.0f);
        put.arc(8.0f, 3.0f, 16.0f, 11.0f, 180.0f, 180.0f, put.getFg(), 2.0f);
        IconPainter.rect$default(put, 2.0f, 12.0f, 22.0f, 13.5f, -4900804, 0.0f, 32, null);
        put.rect(10.0f, 11.0f, 14.0f, 15.0f, IconPainter.YELLOW, 0.8f);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$161(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.rect(2.0f, 3.0f, 22.0f, 21.0f, IconPainter.DARK, 2.0f);
        put.line(5.0f, 8.0f, 15.0f, 8.0f, -1, 1.4f);
        put.line(5.0f, 12.0f, 18.0f, 12.0f, IconPainter.YELLOW, 1.4f);
        put.line(5.0f, 16.0f, 12.0f, 16.0f, -34182, 1.4f);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$162(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.srect(2.5f, 3.5f, 21.5f, 20.5f, put.getFg(), 1.5f, 1.5f);
        put.line(9.0f, 3.5f, 9.0f, 20.5f, put.getFg(), 1.2f);
        put.line(9.0f, 12.0f, 21.5f, 12.0f, put.getFg(), 1.2f);
        put.rect(2.5f, 3.5f, 9.0f, 20.5f, 1429965803, 1.5f);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$163(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.circle(12.0f, 12.0f, 4.5f, IconPainter.YELLOW);
        for (int i = 0; i < 8; i++) {
            double a = Math.toRadians(((double) i) * 45.0d);
            put.line((((float) Math.cos(a)) * 7.0f) + 12.0f, (((float) Math.sin(a)) * 7.0f) + 12.0f, (((float) Math.cos(a)) * 9.8f) + 12.0f, (((float) Math.sin(a)) * 9.8f) + 12.0f, IconPainter.YELLOW, 1.8f);
        }
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$164(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.cube(12.0f, 12.0f, 8.0f, IconPainter.LBLUE, IconPainter.BLUE, IconPainter.DBLUE);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$165(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.poly(IconPainter.BLUE, 3.0f, 5.0f, 12.0f, 12.0f, 3.0f, 19.0f);
        put.poly(IconPainter.BLUE, 12.0f, 5.0f, 21.0f, 12.0f, 12.0f, 19.0f);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$166(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.arc(3.0f, 5.0f, 21.0f, 23.0f, 180.0f, 180.0f, put.getFg(), 2.2f);
        put.line(12.0f, 14.0f, 17.0f, 8.0f, IconPainter.RED, 2.2f);
        put.circle(12.0f, 14.0f, 1.8f, put.getFg());
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$167(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.rect(4.0f, 7.0f, 18.0f, 21.0f, IconPainter.BLUE, 2.0f);
        put.circle(11.0f, 6.5f, 3.0f, IconPainter.BLUE);
        put.circle(18.5f, 14.0f, 3.0f, IconPainter.BLUE);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$168(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.arc(4.0f, 4.0f, 20.0f, 20.0f, 30.0f, 300.0f, put.getFg(), 2.2f);
        put.arrowHead(18.8f, 8.0f, 160.0f, 3.0f, put.getFg());
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$169(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.rect(3.0f, 3.0f, 21.0f, 21.0f, IconPainter.GREEN, 2.0f);
        put.poly(-13726889, 5.0f, 19.0f, 10.0f, 9.0f, 14.0f, 15.0f, 16.0f, 12.0f, 19.0f, 19.0f);
        put.circle(16.0f, 7.0f, 2.0f, IconPainter.YELLOW);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$170(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.gear(12.0f, 12.0f, 8.0f, put.getFg(), Ui.INSTANCE.getPANEL());
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$171(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.poly(-2513632, 2.0f, 5.0f, 9.0f, 5.0f, 11.0f, 7.0f, 20.0f, 7.0f, 20.0f, 10.0f, 2.0f, 10.0f);
        put.poly(IconPainter.YELLOW, 2.0f, 20.0f, 5.0f, 10.0f, 23.0f, 10.0f, 20.0f, 20.0f);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$172(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.rect(3.0f, 3.0f, 21.0f, 21.0f, IconPainter.BLUE, 2.0f);
        put.rect(7.0f, 3.0f, 17.0f, 9.0f, -1, 0.5f);
        IconPainter.rect$default(put, 13.5f, 4.0f, 15.5f, 8.0f, IconPainter.BLUE, 0.0f, 32, null);
        put.rect(6.0f, 13.0f, 18.0f, 21.0f, IconPainter.PAPER, 0.5f);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$173(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.line(20.0f, 12.0f, 5.0f, 12.0f, put.getFg(), 2.2f);
        put.arrowHead(4.0f, 12.0f, 270.0f, 3.5f, put.getFg());
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$174(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.arc(3.0f, 3.0f, 21.0f, 21.0f, 120.0f, 300.0f, put.getFg(), 2.0f);
        put.arrowHead(4.5f, 16.0f, 200.0f, 3.0f, put.getFg());
        put.pline(put.getFg(), 1.8f, 12.0f, 7.0f, 12.0f, 12.0f, 15.5f, 14.0f);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$175(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.line(12.0f, 3.0f, 12.0f, 15.0f, IconPainter.BLUE, 2.4f);
        put.arrowHead(12.0f, 16.0f, 180.0f, 4.0f, IconPainter.BLUE);
        put.pline(put.getFg(), 2.0f, 3.0f, 15.0f, 3.0f, 21.0f, 21.0f, 21.0f, 21.0f, 15.0f);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$176(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.cloud(12.0f, 13.0f, 8.5f, IconPainter.BLUE);
        put.line(12.0f, 19.0f, 12.0f, 10.0f, -1, 2.2f);
        put.arrowHead(12.0f, 9.0f, 0.0f, 3.5f, -1);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$177(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.cloud(12.0f, 13.0f, 8.5f, IconPainter.BLUE);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$178(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.circle(12.0f, 12.0f, 10.0f, IconPainter.BLUE);
        put.person(12.0f, 13.5f, 9.0f, -1);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$179(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.poly(IconPainter.BLUE, 2.5f, 11.5f, 12.0f, 3.0f, 21.5f, 11.5f);
        put.rect(5.0f, 11.0f, 19.0f, 21.0f, IconPainter.LBLUE, 1.0f);
        put.rect(10.0f, 14.5f, 14.0f, 21.0f, IconPainter.DBLUE, 0.6f);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$180(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.circle(8.5f, 12.0f, 7.0f, IconPainter.TEAL);
        put.person(8.5f, 13.0f, 6.5f, -1);
        put.circle(15.5f, 12.0f, 7.0f, IconPainter.BLUE);
        put.person(15.5f, 13.0f, 6.5f, -1);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$181(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.circle(12.0f, 12.0f, 10.0f, IconPainter.BLUE);
        put.circle(12.0f, 7.2f, 1.5f, -1);
        put.rect(10.8f, 10.0f, 13.2f, 17.5f, -1, 0.8f);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$182(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.rect(2.0f, 5.0f, 22.0f, 19.0f, IconPainter.RED, 5.0f);
        put.poly(-1, 10.0f, 8.5f, 16.0f, 12.0f, 10.0f, 15.5f);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$183(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.circle(12.0f, 12.0f, 9.0f, put.getFg());
        put.pie(3.0f, 3.0f, 21.0f, 21.0f, -90.0f, 180.0f, Ui.INSTANCE.getPANEL());
        put.ring(12.0f, 12.0f, 9.0f, put.getFg(), 1.4f);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$184(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.srect(3.0f, 3.0f, 21.0f, 21.0f, put.getFg(), 1.4f, 2.0f);
        put.pline(IconPainter.BLUE, 2.0f, 6.0f, 10.0f, 6.0f, 6.0f, 10.0f, 6.0f);
        put.pline(IconPainter.BLUE, 2.0f, 14.0f, 6.0f, 18.0f, 6.0f, 18.0f, 10.0f);
        put.pline(IconPainter.BLUE, 2.0f, 6.0f, 14.0f, 6.0f, 18.0f, 10.0f, 18.0f);
        put.pline(IconPainter.BLUE, 2.0f, 14.0f, 18.0f, 18.0f, 18.0f, 18.0f, 14.0f);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$185(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.srect(3.0f, 3.0f, 21.0f, 21.0f, put.getFg(), 1.4f, 2.0f);
        put.rect(3.0f, 3.0f, 8.0f, 21.0f, 1715178475, 2.0f);
        put.rect(16.0f, 3.0f, 21.0f, 21.0f, 1715178475, 2.0f);
        IconPainter.rect$default(put, 8.0f, 15.0f, 16.0f, 21.0f, 1715178475, 0.0f, 32, null);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$186(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.poly(IconPainter.GREEN, 6.0f, 4.0f, 20.0f, 12.0f, 6.0f, 20.0f);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$187(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.poly(IconPainter.GREEN, 6.0f, 3.5f, 20.0f, 12.0f, 6.0f, 20.5f);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$188(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.rect(5.0f, 5.0f, 19.0f, 19.0f, IconPainter.RED, 2.0f);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$189(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.arc(5.0f, 6.0f, 20.0f, 21.0f, 180.0f, 230.0f, put.getFg(), 2.2f);
        put.arrowHead(5.5f, 11.5f, 0.0f, 3.2f, put.getFg());
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$190(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.arc(4.0f, 6.0f, 19.0f, 21.0f, 130.0f, 230.0f, put.getFg(), 2.2f);
        put.arrowHead(18.5f, 11.5f, 0.0f, 3.2f, put.getFg());
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$191(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.srect(3.0f, 3.0f, 13.0f, 13.0f, put.getFg(), 1.5f, 1.5f);
        put.srect(11.0f, 11.0f, 21.0f, 21.0f, IconPainter.BLUE, 1.5f, 1.5f);
        put.pline(IconPainter.BLUE, 1.6f, 13.5f, 16.0f, 15.5f, 18.0f, 18.5f, 14.0f);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$192(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.srect(3.0f, 3.0f, 21.0f, 21.0f, put.getFg(), 1.5f, 2.0f);
        put.line(7.0f, 12.0f, 17.0f, 12.0f, put.getFg(), 2.2f);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$193(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        IconPainter.page$default(put, IconPainter.GREEN, 0, 2, null);
        put.pline(IconPainter.GREEN, 2.2f, 9.5f, 13.0f, 12.0f, 16.0f, 17.0f, 9.5f);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$194(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.speaker(IconPainter.BLUE);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$195(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.chain(put.getFg());
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$196(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.circle(12.0f, 8.0f, 5.0f, IconPainter.RED);
        put.line(12.0f, 12.0f, 12.0f, 21.0f, put.getFg(), 1.8f);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$197(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.srect(3.0f, 3.0f, 14.0f, 16.0f, put.getFg(), 1.5f, 1.5f);
        put.rect(9.0f, 8.0f, 21.0f, 21.0f, IconPainter.LBLUE, 1.5f);
        put.srect(9.0f, 8.0f, 21.0f, 21.0f, IconPainter.BLUE, 1.4f, 1.5f);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$198(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.ring(7.0f, 17.5f, 3.2f, put.getFg(), 1.8f);
        put.ring(17.0f, 17.5f, 3.2f, put.getFg(), 1.8f);
        put.line(9.0f, 15.0f, 17.0f, 3.0f, put.getFg(), 1.8f);
        put.line(15.0f, 15.0f, 7.0f, 3.0f, put.getFg(), 1.8f);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$199(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.rect(4.0f, 4.0f, 20.0f, 22.0f, -4617650, 2.0f);
        put.rect(8.5f, 2.5f, 15.5f, 6.5f, IconPainter.GRAY_L, 1.0f);
        put.rect(7.0f, 8.0f, 17.0f, 20.0f, IconPainter.PAPER, 1.0f);
        put.line(9.0f, 12.0f, 15.0f, 12.0f, IconPainter.GRAY_R, 1.2f);
        put.line(9.0f, 15.5f, 15.0f, 15.5f, IconPainter.GRAY_R, 1.2f);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$200(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.arc(7.0f, 3.0f, 17.0f, 15.0f, 180.0f, 180.0f, put.getFg(), 2.2f);
        put.line(7.0f, 9.0f, 7.0f, 11.0f, put.getFg(), 2.2f);
        put.line(17.0f, 9.0f, 17.0f, 11.0f, put.getFg(), 2.2f);
        put.rect(4.5f, 10.5f, 19.5f, 21.0f, IconPainter.YELLOW, 2.0f);
        put.circle(12.0f, 15.5f, 1.8f, IconPainter.DARK);
        return Unit.INSTANCE;
    }

    static final Unit generalIcons$lambda$202$lambda$201(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.box(2.5f, 9.0f, 11.5f, 18.0f, IconPainter.GRAY_T, IconPainter.GRAY_R, (192 & 64) != 0 ? 1.3f : 0.0f, (192 & 128) != 0 ? 1.5f : 0.0f);
        put.box(12.5f, 6.0f, 21.5f, 15.0f, IconPainter.LBLUE, IconPainter.DBLUE, (192 & 64) != 0 ? 1.3f : 0.0f, (192 & 128) != 0 ? 1.5f : 0.0f);
        put.poly(IconPainter.YELLOW, 12.0f, 2.0f, 13.5f, 6.5f, 10.5f, 6.5f);
        return Unit.INSTANCE;
    }

    public final Drawable general(String name) {
        Intrinsics.checkNotNullParameter(name, "name");
        Function1<IconPainter, Unit> function1 = generalIcons.get(name);
        return function1 != null ? new VectorIcon(function1) : null;
    }

    static final Unit uiIcons$lambda$207$lambda$204(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.line(6.0f, 6.0f, 18.0f, 18.0f, put.getFg(), 2.0f);
        put.line(18.0f, 6.0f, 6.0f, 18.0f, put.getFg(), 2.0f);
        return Unit.INSTANCE;
    }

    static final Unit uiIcons$lambda$207$lambda$205(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        put.srect(4.0f, 7.0f, 17.0f, 20.0f, put.getFg(), 1.5f, 1.5f);
        put.srect(8.0f, 3.0f, 21.0f, 16.0f, IconPainter.BLUE, 1.5f, 1.5f);
        return Unit.INSTANCE;
    }

    static final Unit uiIcons$lambda$207$lambda$206(IconPainter put) {
        Intrinsics.checkNotNullParameter(put, "$this$put");
        Iterator it = CollectionsKt.listOf(new Float[]{Float.valueOf(9.0f), Float.valueOf(15.0f)}).iterator();
        while (it.hasNext()) {
            float x = ((Number) it.next()).floatValue();
            Iterator it2 = CollectionsKt.listOf(new Float[]{Float.valueOf(6.0f), Float.valueOf(12.0f), Float.valueOf(18.0f)}).iterator();
            while (it2.hasNext()) {
                float y = ((Number) it2.next()).floatValue();
                put.circle(x, y, 1.6f, Ui.INSTANCE.getDIM());
            }
        }
        return Unit.INSTANCE;
    }

    public final Drawable ui(String name) {
        Intrinsics.checkNotNullParameter(name, "name");
        Function1<IconPainter, Unit> function1 = uiIcons.get(name);
        return function1 != null ? new VectorIcon(function1) : null;
    }
}

package com.goydevv.studrblx.ui;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.constraintlayout.widget.Constraints;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.goydevv.studrblx.audio.AudioEngine;
import com.goydevv.studrblx.core.Studio;
import com.goydevv.studrblx.lua.Attributes;
import com.goydevv.studrblx.model.BrickColorV;
import com.goydevv.studrblx.model.BrickColors;
import com.goydevv.studrblx.model.CFrame;
import com.goydevv.studrblx.model.Color3;
import com.goydevv.studrblx.model.DataTypesKt;
import com.goydevv.studrblx.model.EnumValue;
import com.goydevv.studrblx.model.Enums;
import com.goydevv.studrblx.model.Instance;
import com.goydevv.studrblx.model.PartData;
import com.goydevv.studrblx.model.PropNames;
import com.goydevv.studrblx.model.PropType;
import com.goydevv.studrblx.model.Vector3;
import com.goydevv.studrblx.net.AssetFetcher;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.SetsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: PropertiesPanel.kt */
/* JADX INFO: loaded from: classes5.dex */
public final class PropertiesPanel extends LinearLayout implements Studio.Listener {
    private static final Map<String, String> CATEGORY;
    private static final float DEG = 57.29578f;
    private static final float RAD = 0.017453292f;
    private static final Set<String> skip;
    private static final List<String> surfaceProps;
    private final Adapter adapter;
    private final HashSet<String> collapsed;
    private final RecyclerView list;
    private final Function0<Unit> previewListener;
    private List<Row> rows;
    private final Studio studio;
    private final TextView title;
    private static final List<String> ORDER = CollectionsKt.listOf(new String[]{"Appearance", "Data", "Text", "Image", "Behavior", "Transform", "Pivot", "Collision", "Part", "Surface", "Assembly", "Playback", "Emitter", "Character", "Health", "Movement", "Rigging", "Camera", "Controls", "Mobile", "Shape", "Emission", "Motion", "Particles", "Light", "Scripting", "Model", "Physics", "Attachments", "Constraints", "Input", "Layout", "State", "Teams", "Game", "Other"});

    @Override // com.goydevv.studrblx.core.Studio.Listener
    public void onDirtyChanged() {
        Studio.Listener.super.onDirtyChanged();
    }

    @Override // com.goydevv.studrblx.core.Studio.Listener
    public void onPlayStateChanged() {
        Studio.Listener.super.onPlayStateChanged();
    }

    @Override // com.goydevv.studrblx.core.Studio.Listener
    public void onPluginsChanged() {
        Studio.Listener.super.onPluginsChanged();
    }

    @Override // com.goydevv.studrblx.core.Studio.Listener
    public void onToolChanged() {
        Studio.Listener.super.onToolChanged();
    }

    @Override // com.goydevv.studrblx.core.Studio.Listener
    public void onTreeChanged() {
        Studio.Listener.super.onTreeChanged();
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public PropertiesPanel(Context ctx, Studio studio) {
        super(ctx);
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(studio, "studio");
        this.studio = studio;
        this.rows = CollectionsKt.emptyList();
        this.list = new RecyclerView(ctx);
        this.adapter = new Adapter();
        this.collapsed = new HashSet<>();
        this.previewListener = new Function0() { // from class: com.goydevv.studrblx.ui.PropertiesPanel$$ExternalSyntheticLambda5
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return PropertiesPanel.previewListener$lambda$1(PropertiesPanel.this);
            }
        };
        setOrientation(1);
        setBackgroundColor(Ui.INSTANCE.getPANEL());
        LinearLayout h = Ui.INSTANCE.header(ctx, "Properties", Icons.general$default(Icons.INSTANCE, ctx, "Properties", null, 4, null));
        this.title = Ui.text$default(Ui.INSTANCE, ctx, "", 11.0f, Ui.INSTANCE.getDIM(), false, 16, null);
        h.addView(this.title);
        addView(h, Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null));
        this.list.setLayoutManager(new LinearLayoutManager(ctx));
        this.list.setAdapter(this.adapter);
        this.list.setItemAnimator(null);
        addView(this.list, Ui.INSTANCE.lp(-1, 0, 1.0f));
        rebuild();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: compiled from: PropertiesPanel.kt */
    static final class Row {
        private final Function0<Unit> action;
        private final String display;
        private final boolean editable;
        private final String prop;
        private final String title;
        private final int type;
        private final Object value;

        public Row(String title, String prop, String display, Object value, int type, boolean editable, Function0<Unit> function0) {
            Intrinsics.checkNotNullParameter(title, "title");
            Intrinsics.checkNotNullParameter(display, "display");
            this.title = title;
            this.prop = prop;
            this.display = display;
            this.value = value;
            this.type = type;
            this.editable = editable;
            this.action = function0;
        }

        public /* synthetic */ Row(String str, String str2, String str3, Object obj, int i, boolean z, Function0 function0, int i2, DefaultConstructorMarker defaultConstructorMarker) {
            this(str, str2, str3, obj, i, z, (i2 & 64) != 0 ? null : function0);
        }

        public final Function0<Unit> getAction() {
            return this.action;
        }

        public final String getDisplay() {
            return this.display;
        }

        public final boolean getEditable() {
            return this.editable;
        }

        public final String getProp() {
            return this.prop;
        }

        public final String getTitle() {
            return this.title;
        }

        public final int getType() {
            return this.type;
        }

        public final Object getValue() {
            return this.value;
        }
    }

    static final Unit previewListener$lambda$1(final PropertiesPanel this$0) {
        this$0.post(new Runnable() { // from class: com.goydevv.studrblx.ui.PropertiesPanel$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this$0.adapter.notifyDataSetChanged();
            }
        });
        return Unit.INSTANCE;
    }

    static {
        HashMap map = new HashMap();
        CATEGORY$lambda$14$c(map, "Appearance", "BrickColor", "CastShadow", "Color", "Material", "MaterialVariant", "Reflectance", "Transparency", "BackgroundColor3", "BackgroundTransparency", "BorderColor3", "BorderMode", "BorderSizePixel", "Visible", "Texture", "Face", "Color3", "ZIndex", "TextureID", "DoubleSided", "RenderFidelity", "SmoothingAngle", "UsePartColor", "FillColor", "FillTransparency", "OutlineColor", "OutlineTransparency", "DepthMode", "LineThickness", "SurfaceColor3", "SurfaceTransparency", "StudsPerTileU", "StudsPerTileV", "OffsetStudsU", "OffsetStudsV", "LightInfluence", "Brightness", "Shadows", "Range", "Angle", "Thickness", "LineJoinMode", "CornerRadius", "Rotation", "Offset", "VertexColor", "Scale", "MeshType", "TextureId");
        CATEGORY$lambda$14$c(map, "Data", "ClassName", "Name", "Parent", "Source", "LinkedSource", "SoundId", "MeshId", "AnimationId", "Value", "Adornee", "PrimaryPart", "Part0", "Part1", "Attachment0", "Attachment1", "LayoutOrder", "SoundGroup", "TimeLength", "DisplayName", "Tags", "PlaceholderText", "Title", "Graphic", "ShirtTemplate", "PantsTemplate", "Tool", "ToolTip");
        CATEGORY$lambda$14$c(map, "Text", "Font", "FontFace", "LineHeight", "MaxVisibleGraphemes", "RichText", "Text", "TextColor3", "TextScaled", "TextSize", "TextStrokeColor3", "TextStrokeTransparency", "TextTransparency", "TextTruncate", "TextWrapped", "TextXAlignment", "TextYAlignment", "TextEditable", "ClearTextOnFocus", "MultiLine", "PlaceholderColor3", "ShowNativeInput");
        CATEGORY$lambda$14$c(map, "Image", "Image", "ImageColor3", "ImageRectOffset", "ImageRectSize", "ImageTransparency", "ResampleMode", "ScaleType", "SliceCenter", "SliceScale", "TileSize", "HoverImage", "PressedImage");
        CATEGORY$lambda$14$c(map, "Behavior", "Archivable", "Locked", "Active", "AutoButtonColor", "ClipsDescendants", "Draggable", "Interactable", "Selectable", "Disabled", "Enabled", "RunContext", "ResetOnSpawn", "IgnoreGuiInset", "ZIndexBehavior", "DisplayOrder", "AlwaysOnTop", "Modal", "Selected", "AutoLocalize", "ScreenInsets", "SafeAreaCompatibility", "CanBeDropped", "ManualActivationOnly", "RequiresHandle", "MaxActivationDistance", "HoldDuration", "KeyboardKeyCode", "ActionText", "ObjectText", "RequiresLineOfSight", "Exclusivity", "Style");
        CATEGORY$lambda$14$c(map, "Transform", "Size", "Position", "Orientation", "CFrame", "AnchorPoint", "SizeConstraint", "AutomaticSize");
        CATEGORY$lambda$14$c(map, "Pivot", "PivotOffset", "WorldPivot", "Pivot Offset", "Origin");
        CATEGORY$lambda$14$c(map, "Collision", "CanCollide", "CanQuery", "CanTouch", "CollisionGroup", "CollisionGroupId", "CollisionFidelity", "AudioCanCollide");
        CATEGORY$lambda$14$c(map, "Part", "Anchored", "CustomPhysicalProperties", "Massless", "RootPriority", "Shape", "EnableFluidForces", "FormFactor");
        CATEGORY$lambda$14$c(map, "Surface", "BackSurface", "BottomSurface", "FrontSurface", "LeftSurface", "RightSurface", "TopSurface", "BackSurfaceInput", "BottomSurfaceInput", "FrontSurfaceInput", "LeftSurfaceInput", "RightSurfaceInput", "TopSurfaceInput");
        CATEGORY$lambda$14$c(map, "Assembly", "AssemblyLinearVelocity", "AssemblyAngularVelocity", "Velocity", "RotVelocity");
        CATEGORY$lambda$14$c(map, "Playback", "Looped", "PlaybackSpeed", "Playing", "TimePosition", "Volume", "PlayOnRemove", "PlaybackRegionsEnabled", "Pitch");
        CATEGORY$lambda$14$c(map, "Emitter", "RollOffMaxDistance", "RollOffMinDistance", "RollOffMode", "EmitterSize", "MaxDistance", "MinDistance");
        CATEGORY$lambda$14$c(map, "Health", "Health", "MaxHealth", "BreakJointsOnDeath", "HealthDisplayDistance", "HealthDisplayType");
        CATEGORY$lambda$14$c(map, "Movement", "WalkSpeed", "HipHeight", "MaxSlopeAngle", "AutoRotate", "AutoJumpEnabled", "JumpPower", "JumpHeight", "UseJumpPower", "PlatformStand", "Sit");
        CATEGORY$lambda$14$c(map, "Rigging", "RigType", "RequiresNeck", "AutomaticScalingEnabled");
        CATEGORY$lambda$14$c(map, "Camera", "CameraType", "FieldOfView", "FieldOfViewMode", "CameraSubject", "Focus", "HeadLocked", "HeadScale", "DiagonalFieldOfView", "MaxAxisFieldOfView");
        CATEGORY$lambda$14$c(map, "Emission", "Rate", "Lifetime", "Speed", "SpreadAngle", "EmissionDirection", "Drag", "Acceleration", "LockedToPart", "VelocityInheritance", "ZOffset", "Squash", "TimeScale");
        CATEGORY$lambda$14$c(map, "Light", "Ambient", "OutdoorAmbient", "ClockTime", "GeographicLatitude", "TimeOfDay", "ExposureCompensation", "GlobalShadows", "FogColor", "FogEnd", "FogStart", "EnvironmentDiffuseScale", "EnvironmentSpecularScale", "ColorShift_Bottom", "ColorShift_Top", "Technology", "ShadowSoftness");
        CATEGORY$lambda$14$c(map, "Game", "Gravity", "FallenPartsDestroyHeight", "StreamingEnabled", "RespawnTime", "MaxPlayers", "CharacterAutoLoads");
        CATEGORY$lambda$14$c(map, "Teams", "TeamColor", "AutoAssignable", "Neutral", "AllowTeamChangeOnTouch", "Duration");
        CATEGORY$lambda$14$c(map, "Layout", "Padding", "FillDirection", "HorizontalAlignment", "VerticalAlignment", "SortOrder", "CellSize", "CellPadding", "PaddingTop", "PaddingBottom", "PaddingLeft", "PaddingRight", "AspectRatio", "AspectType", "DominantAxis", "MinSize", "MaxSize", "Wraps");
        CATEGORY$lambda$14$c(map, "Model", "LevelOfDetail", "ModelStreamingMode", "WorldPivotData");
        CATEGORY = map;
        surfaceProps = CollectionsKt.listOf(new String[]{"TopSurface", "BottomSurface", "FrontSurface", "BackSurface", "LeftSurface", "RightSurface"});
        skip = SetsKt.setOf(new String[]{"AttributesSerialize", "Tags", "SourceAssetId", "HistoryId", "UniqueId", "ScriptGuid", "Capabilities", "DefinesCapabilities", "MeshContent", "TextureContent", "PhysicsData", "PhysicalConfigData", "ChildData", "MeshData", "InitialSize", "ModelMeshData", "ModelMeshSize", "ModelMeshCFrame", "LevelOfDetail"});
    }

    private static final void CATEGORY$lambda$14$c(HashMap<String, String> map, String cat, String... names) {
        for (String n : names) {
            map.put(n, cat);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        AudioEngine.INSTANCE.getPreviewListeners().add(this.previewListener);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        AudioEngine.INSTANCE.getPreviewListeners().remove(this.previewListener);
        super.onDetachedFromWindow();
    }

    @Override // com.goydevv.studrblx.core.Studio.Listener
    public void onSelectionChanged() {
        rebuild();
    }

    @Override // com.goydevv.studrblx.core.Studio.Listener
    public void onPropsChanged() {
        rebuild();
    }

    @Override // com.goydevv.studrblx.core.Studio.Listener
    public void onModelReplaced() {
        rebuild();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:11:0x0026, code lost:
    
        if (r8.equals("Rotation") != false) goto L16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x002f, code lost:
    
        if (r8.equals("Size") == false) goto L23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0038, code lost:
    
        if (r7.isA("GuiObject") != false) goto L21;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0040, code lost:
    
        if (r7.isA("BasePart") != false) goto L21;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0043, code lost:
    
        return "Transform";
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:?, code lost:
    
        return "Data";
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x001d, code lost:
    
        if (r8.equals("Position") == false) goto L23;
     */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private final java.lang.String categoryOf(com.goydevv.studrblx.model.Instance inst, java.lang.String name, int type) {
        if (name == null) return "Data";
        switch (name) {
            case "Position": case "Size": case "CFrame": case "Orientation": case "Rotation": case "AnchorPoint":
                return "Transform";
            case "Color": case "Color3": case "BrickColor": case "Material": case "Transparency":
            case "Reflectance": case "Visible": case "CastShadow": case "BackgroundColor3":
            case "BackgroundTransparency": case "BorderColor3": case "BorderSizePixel":
            case "ImageColor3": case "ImageTransparency": case "Image":
                return "Appearance";
            case "Text": case "TextColor3": case "TextTransparency": case "TextSize":
            case "TextScaled": case "TextWrapped": case "Font": case "FontFace":
            case "TextXAlignment": case "TextYAlignment": case "RichText":
                return "Text";
            case "Anchored": case "CanCollide": case "CanTouch": case "CanQuery":
            case "Locked": case "Massless": case "Enabled": case "Disabled": case "Active":
                return "Behavior";
            default:
                return "Data";
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r14v80 */
    /* JADX WARN: Type inference failed for: r16v0 */
    /* JADX WARN: Type inference failed for: r16v1, types: [boolean] */
    /* JADX WARN: Type inference failed for: r16v18 */
    /* JADX WARN: Type inference failed for: r16v19 */
    /* JADX WARN: Type inference failed for: r16v20 */
    /* JADX WARN: Type inference failed for: r16v4 */
    /* JADX WARN: Type inference failed for: r16v5, types: [boolean] */
    /* JADX WARN: Type inference failed for: r16v6 */
    /* JADX WARN: Type inference failed for: r16v7 */
    /* JADX WARN: Type inference failed for: r22v0 */
    /* JADX WARN: Type inference failed for: r22v1, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r22v6 */
    /* JADX WARN: Type inference failed for: r22v7 */
    public final void rebuild() {
        StringBuilder sbAppend;
        String str;
        int r22;
        int r16;
        boolean z;
        Vector3 zero;
        final Long lIdOf;
        ArrayList arrayList = new ArrayList();
        synchronized (this.studio.getLock()) {
            final Instance instance = (Instance) CollectionsKt.firstOrNull((List) this.studio.getSelection());
            if (instance == null) {
                this.title.setText("");
            } else {
                TextView textView = this.title;
                boolean z2 = true;
                if (this.studio.getSelection().size() > 1) {
                    sbAppend = new StringBuilder().append(this.studio.getSelection().size());
                    str = " selected  ";
                } else {
                    sbAppend = new StringBuilder().append(instance.getClassName()).append(" \"").append(instance.getName());
                    str = "\"  ";
                }
                textView.setText(sbAppend.append(str).toString());
                LinkedHashMap linkedHashMap = new LinkedHashMap();
                HashSet hashSet = new HashSet();
                boolean zIsA = instance.isA("BasePart");
                if (!Intrinsics.areEqual(instance.getClassName(), "Sound") || (lIdOf = AssetFetcher.INSTANCE.idOf(instance.get("SoundId"))) == null) {
                    r22 = 0;
                } else {
                    AudioEngine.Preview preview = AudioEngine.INSTANCE.getPreview();
                    r22 = 0;
                    rebuild$lambda$9$add(linkedHashMap, "Data", new Row("", "@Preview", "Preview", preview != null && (preview.getId() > lIdOf.longValue() ? 1 : (preview.getId() == lIdOf.longValue() ? 0 : -1)) == 0 && (preview.getState() == AudioEngine.PreviewState.PLAYING || preview.getState() == AudioEngine.PreviewState.LOADING) ? "■ Stop" : "▶ Play", 1, true, new Function0() { // from class: com.goydevv.studrblx.ui.PropertiesPanel$$ExternalSyntheticLambda2
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return PropertiesPanel.rebuild$lambda$9$lambda$4(lIdOf, instance);
                        }
                    }));
                }
                rebuild$lambda$9$add(linkedHashMap, "Data", new Row("", "Name", "Name", instance.getName(), 1, !instance.getIsService(), null, 64, null));
                hashSet.add("Name");
                rebuild$lambda$9$add(linkedHashMap, "Data", new Row("", "ClassName", "ClassName", instance.getClassName(), 1, false, null, 64, null));
                rebuild$lambda$9$add(linkedHashMap, "Data", new Row("", "Parent", "Parent", instance.getParent(), 19, false, null, 64, null));
                rebuild$lambda$9$add(linkedHashMap, "Behavior", new Row("", "@Archivable", "Archivable", Boolean.valueOf(instance.getArchivable()), 2, !instance.getIsService(), null, 64, null));
                if (instance.isA("LuaSourceContainer")) {
                    rebuild$lambda$9$add(linkedHashMap, "Data", new Row("", "@Source", "Source", "Open in editor…", 1, true, null, 64, null));
                    hashSet.add("Source");
                }
                if (!zIsA) {
                    r16 = 1;
                } else {
                    CFrame cframe = PartData.INSTANCE.cframe(instance);
                    rebuild$lambda$9$add(linkedHashMap, "Transform", new Row("", "@Position", "Position", cframe.getPosition(), 14, true, null, 64, null));
                    rebuild$lambda$9$add(linkedHashMap, "Transform", new Row("", "@Orientation", "Orientation", cframe.toOrientation().times(DEG), 14, true, null, 64, null));
                    rebuild$lambda$9$add(linkedHashMap, "Transform", new Row("", instance.getProps().containsKey("size") ? "size" : "Size", "Size", PartData.INSTANCE.size(instance), 14, true, null, 64, null));
                    String[] strArr = new String[5];
                    strArr[r22] = "CFrame";
                    strArr[1] = "size";
                    strArr[2] = "Size";
                    strArr[3] = "Position";
                    strArr[4] = "Orientation";
                    CollectionsKt.addAll(hashSet, CollectionsKt.listOf(strArr));
                    Color3 color3Color = PartData.INSTANCE.color(instance);
                    String str2 = "";
                    String str3 = "@BrickColor";
                    String str4 = "BrickColor";
                    Object obj = instance.get("BrickColor");
                    BrickColorV brickColorV = obj instanceof BrickColorV ? (BrickColorV) obj : null;
                    if (brickColorV == null) {
                        brickColorV = new BrickColorV(BrickColors.INSTANCE.closest(color3Color));
                    }
                    rebuild$lambda$9$add(linkedHashMap, "Appearance", new Row(str2, str3, str4, brickColorV, 11, true, null, 64, null));
                    rebuild$lambda$9$add(linkedHashMap, "Appearance", new Row("", instance.getProps().containsKey("Color") ? "Color" : "Color3uint8", "Color", color3Color, 26, true, null, 64, null));
                    String str5 = "";
                    String str6 = "Material";
                    String str7 = "Material";
                    Object enumValue = instance.get("Material");
                    if (enumValue == null) {
                        enumValue = new EnumValue(256);
                    }
                    rebuild$lambda$9$add(linkedHashMap, "Appearance", new Row(str5, str6, str7, enumValue, 18, true, null, 64, null));
                    String str8 = instance.getProps().containsKey("MaterialVariantSerialized") ? "MaterialVariantSerialized" : "MaterialVariant";
                    String str9 = "";
                    String str10 = "MaterialVariant";
                    Object obj2 = instance.get(str8);
                    if (obj2 == null) {
                        obj2 = "";
                    }
                    rebuild$lambda$9$add(linkedHashMap, "Appearance", new Row(str9, str8, str10, obj2, 1, true, null, 64, null));
                    String str11 = "";
                    String str12 = "Reflectance";
                    String str13 = "Reflectance";
                    Object objValueOf = instance.get("Reflectance");
                    if (objValueOf == null) {
                        objValueOf = Float.valueOf(0.0f);
                    }
                    rebuild$lambda$9$add(linkedHashMap, "Appearance", new Row(str11, str12, str13, objValueOf, 4, true, null, 64, null));
                    String str14 = "";
                    String str15 = "Transparency";
                    String str16 = "Transparency";
                    Object objValueOf2 = instance.get("Transparency");
                    if (objValueOf2 == null) {
                        objValueOf2 = Float.valueOf(0.0f);
                    }
                    rebuild$lambda$9$add(linkedHashMap, "Appearance", new Row(str14, str15, str16, objValueOf2, 4, true, null, 64, null));
                    String str17 = "";
                    String str18 = "CastShadow";
                    String str19 = "CastShadow";
                    Object obj3 = instance.get("CastShadow");
                    if (obj3 == null) {
                        obj3 = true;
                    }
                    rebuild$lambda$9$add(linkedHashMap, "Appearance", new Row(str17, str18, str19, obj3, 2, true, null, 64, null));
                    String[] strArr2 = new String[9];
                    strArr2[r22] = "BrickColor";
                    strArr2[1] = "Color";
                    strArr2[2] = "Color3uint8";
                    strArr2[3] = "Material";
                    strArr2[4] = "MaterialVariant";
                    strArr2[5] = "MaterialVariantSerialized";
                    strArr2[6] = "Reflectance";
                    strArr2[7] = "Transparency";
                    strArr2[8] = "CastShadow";
                    CollectionsKt.addAll(hashSet, CollectionsKt.listOf(strArr2));
                    String str20 = "";
                    String str21 = "Locked";
                    String str22 = "Locked";
                    Object objValueOf3 = instance.get("Locked");
                    if (objValueOf3 == null) {
                        objValueOf3 = Boolean.valueOf(r22 != 0);
                    }
                    rebuild$lambda$9$add(linkedHashMap, "Behavior", new Row(str20, str21, str22, objValueOf3, 2, true, null, 64, null));
                    String str23 = "";
                    String str24 = "CanCollide";
                    String str25 = "CanCollide";
                    Object obj4 = instance.get("CanCollide");
                    if (obj4 == null) {
                        obj4 = true;
                    }
                    rebuild$lambda$9$add(linkedHashMap, "Collision", new Row(str23, str24, str25, obj4, 2, true, null, 64, null));
                    String str26 = "";
                    String str27 = "CanQuery";
                    String str28 = "CanQuery";
                    Object obj5 = instance.get("CanQuery");
                    if (obj5 == null) {
                        obj5 = true;
                    }
                    rebuild$lambda$9$add(linkedHashMap, "Collision", new Row(str26, str27, str28, obj5, 2, true, null, 64, null));
                    String str29 = "";
                    String str30 = "CanTouch";
                    String str31 = "CanTouch";
                    Object obj6 = instance.get("CanTouch");
                    if (obj6 == null) {
                        obj6 = true;
                    }
                    rebuild$lambda$9$add(linkedHashMap, "Collision", new Row(str29, str30, str31, obj6, 2, true, null, 64, null));
                    String str32 = "";
                    String str33 = "CollisionGroup";
                    String str34 = "CollisionGroup";
                    Object obj7 = instance.get("CollisionGroup");
                    if (obj7 == null) {
                        obj7 = "Default";
                    }
                    rebuild$lambda$9$add(linkedHashMap, "Collision", new Row(str32, str33, str34, obj7, 1, true, null, 64, null));
                    String str35 = "";
                    String str36 = "Anchored";
                    String str37 = "Anchored";
                    Object objValueOf4 = instance.get("Anchored");
                    if (objValueOf4 == null) {
                        objValueOf4 = Boolean.valueOf(r22 != 0);
                    }
                    rebuild$lambda$9$add(linkedHashMap, "Part", new Row(str35, str36, str37, objValueOf4, 2, true, null, 64, null));
                    String str38 = "";
                    String str39 = "Massless";
                    String str40 = "Massless";
                    Object objValueOf5 = instance.get("Massless");
                    if (objValueOf5 == null) {
                        objValueOf5 = Boolean.valueOf(r22 != 0);
                    }
                    rebuild$lambda$9$add(linkedHashMap, "Part", new Row(str38, str39, str40, objValueOf5, 2, true, null, 64, null));
                    String str41 = "";
                    String str42 = "RootPriority";
                    String str43 = "RootPriority";
                    Object objValueOf6 = instance.get("RootPriority");
                    if (objValueOf6 == null) {
                        objValueOf6 = Integer.valueOf((int) r22);
                    }
                    rebuild$lambda$9$add(linkedHashMap, "Part", new Row(str41, str42, str43, objValueOf6, 3, true, null, 64, null));
                    if (Intrinsics.areEqual(instance.getClassName(), "Part") || Intrinsics.areEqual(instance.getClassName(), "SpawnLocation") || Intrinsics.areEqual(instance.getClassName(), "Seat")) {
                        String str44 = "";
                        String str45 = instance.getProps().containsKey("Shape") ? "Shape" : "shape";
                        String str46 = "Shape";
                        Object enumValue2 = instance.get("shape");
                        if (enumValue2 == null && (enumValue2 = instance.get("Shape")) == null) {
                            enumValue2 = new EnumValue(1);
                        }
                        rebuild$lambda$9$add(linkedHashMap, "Part", new Row(str44, str45, str46, enumValue2, 18, true, null, 64, null));
                    }
                    String[] strArr3 = new String[10];
                    strArr3[r22] = "Locked";
                    strArr3[1] = "CanCollide";
                    strArr3[2] = "CanQuery";
                    strArr3[3] = "CanTouch";
                    strArr3[4] = "CollisionGroup";
                    strArr3[5] = "Anchored";
                    strArr3[6] = "Massless";
                    strArr3[7] = "RootPriority";
                    strArr3[8] = "shape";
                    strArr3[9] = "Shape";
                    CollectionsKt.addAll(hashSet, CollectionsKt.listOf(strArr3));
                    if ((instance.isA("PartOperation") && !Intrinsics.areEqual(instance.getClassName(), "NegateOperation")) || Intrinsics.areEqual(instance.getClassName(), "MeshPart")) {
                        String str47 = "";
                        String str48 = "CollisionFidelity";
                        String str49 = "CollisionFidelity";
                        Object enumValue3 = instance.get("CollisionFidelity");
                        if (enumValue3 == null) {
                            enumValue3 = new EnumValue(r22);
                        }
                        rebuild$lambda$9$add(linkedHashMap, "Collision", new Row(str47, str48, str49, enumValue3, 18, true, null, 64, null));
                        String str50 = "";
                        String str51 = "RenderFidelity";
                        String str52 = "RenderFidelity";
                        Object enumValue4 = instance.get("RenderFidelity");
                        if (enumValue4 == null) {
                            enumValue4 = new EnumValue(0);
                        }
                        rebuild$lambda$9$add(linkedHashMap, "Appearance", new Row(str50, str51, str52, enumValue4, 18, true, null, 64, null));
                        CollectionsKt.addAll(hashSet, CollectionsKt.listOf(new String[]{"CollisionFidelity", "RenderFidelity"}));
                    }
                    if (instance.isA("PartOperation") || Intrinsics.areEqual(instance.getClassName(), "MeshPart") || Intrinsics.areEqual(instance.getClassName(), "Terrain")) {
                        z = true;
                    } else {
                        for (String str53 : surfaceProps) {
                            String str54 = "";
                            Object enumValue5 = instance.get(str53);
                            if (enumValue5 == null) {
                                enumValue5 = new EnumValue(0);
                            }
                            rebuild$lambda$9$add(linkedHashMap, "Surface", new Row(str54, str53, str53, enumValue5, 18, true, null, 64, null));
                        }
                        z = z2;
                    }
                    CollectionsKt.addAll(hashSet, surfaceProps);
                    String str55 = "";
                    String str56 = "PivotOffset";
                    String str57 = "Pivot Offset";
                    Object obj8 = instance.get("PivotOffset");
                    CFrame cFrame = obj8 instanceof CFrame ? (CFrame) obj8 : null;
                    if (cFrame == null || (zero = cFrame.getPosition()) == null) {
                        zero = Vector3.INSTANCE.getZERO();
                    }
                    rebuild$lambda$9$add(linkedHashMap, "Pivot", new Row(str55, str56, str57, zero, 14, true, null, 64, null));
                    hashSet.add("PivotOffset");
                    r16 = z ? 1 : 0;
                }
                for (Map.Entry<String, Object> entry : instance.getProps().entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    if (!hashSet.contains(key) && !skip.contains(key) && (!StringsKt.startsWith(key, "xmlRead", false) || value != null)) {
                        Integer num = instance.getPropTypes().get(key);
                        int iIntValue = num != null ? num.intValue() : PropType.INSTANCE.infer(value);
                        if (!PropType.INSTANCE.hidden(iIntValue, value)) {
                            String strDisplay = PropNames.INSTANCE.display(key);
                            if (!hashSet.contains(strDisplay)) {
                                rebuild$lambda$9$add(linkedHashMap, categoryOf(instance, strDisplay, iIntValue), new Row("", key, strDisplay, value, iIntValue, true, null, 64, null));
                            }
                        }
                    }
                }
                List<String> list = ORDER;
                Set setKeySet = linkedHashMap.keySet();
                Intrinsics.checkNotNullExpressionValue(setKeySet, "<get-keys>(...)");
                ArrayList arrayList2 = new ArrayList();
                for (Object obj9 : setKeySet) {
                    if (!ORDER.contains((String) obj9)) {
                        arrayList2.add(obj9);
                    }
                }
                for (String str58 : (List<String>) CollectionsKt.plus((Collection) list, (Iterable) arrayList2)) {
                    ArrayList arrayList3 = (ArrayList) linkedHashMap.get(str58);
                    if (arrayList3 != null && !arrayList3.isEmpty()) {
                        arrayList.add(new Row(str58, null, str58, null, 0, false, null, 64, null));
                        if (!this.collapsed.contains(str58)) {
                            Function1[] function1Arr = new Function1[2];
                            function1Arr[0] = new Function1() { // from class: com.goydevv.studrblx.ui.PropertiesPanel$$ExternalSyntheticLambda3
                                @Override // kotlin.jvm.functions.Function1
                                public final Object invoke(Object obj10) {
                                    return PropertiesPanel.rebuild$lambda$9$lambda$6((PropertiesPanel.Row) obj10);
                                }
                            };
                            function1Arr[r16] = new Function1() { // from class: com.goydevv.studrblx.ui.PropertiesPanel$$ExternalSyntheticLambda4
                                @Override // kotlin.jvm.functions.Function1
                                public final Object invoke(Object obj10) {
                                    return PropertiesPanel.rebuild$lambda$9$lambda$7((PropertiesPanel.Row) obj10);
                                }
                            };
                            arrayList.addAll(CollectionsKt.sortedWith(arrayList3, ComparisonsKt.compareBy(function1Arr)));
                        }
                    }
                }
                if (!instance.getAttributes().isEmpty() || instance.getProps().containsKey("AttributesSerialize")) {
                    Attributes.INSTANCE.load(instance);
                    if (!instance.getAttributes().isEmpty()) {
                        arrayList.add(new Row("Attributes", null, "Attributes", null, 0, false, null, 64, null));
                        if (!this.collapsed.contains("Attributes")) {
                            Set<Map.Entry<String, Object>> setEntrySet = instance.getAttributes().entrySet();
                            Intrinsics.checkNotNullExpressionValue(setEntrySet, "<get-entries>(...)");
                            for (Object obj10 : CollectionsKt.sortedWith(setEntrySet, new Comparator() { // from class: com.goydevv.studrblx.ui.PropertiesPanel$rebuild$lambda$9$$inlined$sortedBy$1
                                @Override // java.util.Comparator
                                public final int compare(Object t, Object t2) {
                                    String lowerCase = ((String) ((Map.Entry) t).getKey()).toLowerCase(Locale.ROOT);
                                    Intrinsics.checkNotNullExpressionValue(lowerCase, "toLowerCase(...)");
                                    String lowerCase2 = ((String) ((Map.Entry) t2).getKey()).toLowerCase(Locale.ROOT);
                                    Intrinsics.checkNotNullExpressionValue(lowerCase2, "toLowerCase(...)");
                                    return ComparisonsKt.compareValues(lowerCase, lowerCase2);
                                }
                            })) {
                                Intrinsics.checkNotNullExpressionValue(obj10, "next(...)");
                                Map.Entry entry2 = (Map.Entry) obj10;
                                Object key2 = entry2.getKey();
                                Intrinsics.checkNotNullExpressionValue(key2, "component1(...)");
                                String str59 = (String) key2;
                                Object value2 = entry2.getValue();
                                if (!StringsKt.startsWith(str59, "__", false)) {
                                    arrayList.add(new Row("", "@attr:" + str59, str59, value2, PropType.INSTANCE.infer(value2), false, null, 64, null));
                                }
                            }
                        }
                    }
                }
            }
            Unit unit = Unit.INSTANCE;
        }
        this.rows = arrayList;
        this.adapter.notifyDataSetChanged();
    }

    private static final boolean rebuild$lambda$9$add(LinkedHashMap<String, ArrayList<Row>> linkedHashMap, String cat, Row r) {
        ArrayList<Row> arrayList;
        LinkedHashMap<String, ArrayList<Row>> linkedHashMap2 = linkedHashMap;
        ArrayList<Row> arrayList2 = linkedHashMap2.get(cat);
        if (arrayList2 == null) {
            arrayList = new ArrayList<>();
            linkedHashMap2.put(cat, arrayList);
        } else {
            arrayList = arrayList2;
        }
        return arrayList.add(r);
    }

    static final Unit rebuild$lambda$9$lambda$4(Long $id, Instance $inst) {
        AudioEngine audioEngine = AudioEngine.INSTANCE;
        long jLongValue = $id.longValue();
        Object obj = $inst.get("Volume");
        Number number = obj instanceof Number ? (Number) obj : null;
        audioEngine.togglePreview(jLongValue, number != null ? number.floatValue() : 0.5f);
        return Unit.INSTANCE;
    }

    static final Comparable rebuild$lambda$9$lambda$6(Row it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return Integer.valueOf(Intrinsics.areEqual(it.getProp(), "@Preview") ? 0 : 1);
    }

    static final Comparable rebuild$lambda$9$lambda$7(Row it) {
        Intrinsics.checkNotNullParameter(it, "it");
        String lowerCase = it.getDisplay().toLowerCase(Locale.ROOT);
        Intrinsics.checkNotNullExpressionValue(lowerCase, "toLowerCase(...)");
        return lowerCase;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final String valueText(Row r, Instance inst) {
        String className;
        Object v = r.getValue();
        if (v == null) {
            return r.getType() != 19 ? "nil" : "";
        }
        if (v instanceof Instance) {
            return ((Instance) v).getName();
        }
        if (!(v instanceof EnumValue)) {
            if (v instanceof BrickColorV) {
                return ((BrickColorV) v).getName();
            }
            if (v instanceof Color3) {
                return "[" + ((int) (((Color3) v).getR() * 255.0f)) + ", " + ((int) (((Color3) v).getG() * 255.0f)) + ", " + ((int) (((Color3) v).getB() * 255.0f)) + "]";
            }
            if (v instanceof Float) {
                return DataTypesKt.fmtNum(((Number) v).floatValue());
            }
            if (v instanceof Double) {
                return DataTypesKt.fmtNum(((Number) v).doubleValue());
            }
            if (v instanceof Vector3) {
                return DataTypesKt.fmtNum(((Vector3) v).getX()) + ", " + DataTypesKt.fmtNum(((Vector3) v).getY()) + ", " + DataTypesKt.fmtNum(((Vector3) v).getZ());
            }
            if (v instanceof CFrame) {
                return ((CFrame) v).getPosition().toString();
            }
            return v instanceof String ? StringsKt.take(StringsKt.replace((String) v, '\n', ' ', false), 120) : v.toString();
        }
        Enums enums = Enums.INSTANCE;
        Enums enums2 = Enums.INSTANCE;
        if (inst == null || (className = inst.getClassName()) == null) {
            className = "";
        }
        String prop = r.getProp();
        String strItemName = enums.itemName(enums2.typeForProp(className, prop != null ? prop : ""), ((EnumValue) v).getValue());
        if (strItemName != null) {
            return strItemName;
        }
        String strItemName2 = Intrinsics.areEqual(r.getDisplay(), "Material") ? Enums.INSTANCE.itemName("Material", ((EnumValue) v).getValue()) : null;
        if (strItemName2 != null) {
            return strItemName2;
        }
        String strItemName3 = StringsKt.endsWith(r.getDisplay(), "Surface", false) ? Enums.INSTANCE.itemName("SurfaceType", ((EnumValue) v).getValue()) : null;
        if (strItemName3 != null) {
            return strItemName3;
        }
        String strItemName4 = Intrinsics.areEqual(r.getDisplay(), "Shape") ? Enums.INSTANCE.itemName("PartType", ((EnumValue) v).getValue()) : null;
        return strItemName4 == null ? String.valueOf(((EnumValue) v).getValue()) : strItemName4;
    }

    /* JADX WARN: Code duplicated, block: B:107:0x01ee A[ADDED_TO_REGION, REMOVE, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:120:0x022f A[ADDED_TO_REGION, REMOVE, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:122:0x023b A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:139:0x0104 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:141:0x00ec A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:142:0x010e A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:153:0x003f A[ADDED_TO_REGION, REMOVE, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:160:0x003f A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:39:0x00f8  */
    /* JADX WARN: Code duplicated, block: B:40:0x00fb  */
    /* JADX WARN: Code duplicated, block: B:74:0x01d6 A[Catch: all -> 0x026c, TryCatch #0 {, blocks: (B:8:0x000f, B:9:0x003f, B:11:0x0045, B:12:0x0051, B:72:0x01cc, B:74:0x01d6, B:76:0x01e4, B:79:0x01ee, B:81:0x01f9, B:83:0x01fd, B:84:0x0208, B:86:0x0210, B:88:0x0214, B:89:0x021f, B:91:0x0227, B:93:0x022f, B:95:0x023b, B:97:0x023f, B:14:0x0056, B:16:0x005e, B:18:0x0066, B:19:0x0080, B:21:0x0088, B:23:0x0090, B:27:0x00a9, B:29:0x00c9, B:30:0x00d2, B:35:0x00e4, B:37:0x00ec, B:41:0x00fd, B:42:0x0104, B:44:0x010e, B:33:0x00dc, B:45:0x0115, B:47:0x011d, B:49:0x0123, B:50:0x012c, B:52:0x0134, B:54:0x013c, B:55:0x0174, B:57:0x017c, B:59:0x0182, B:60:0x0190, B:63:0x0199, B:65:0x01a1, B:67:0x01af, B:70:0x01b5, B:71:0x01bb, B:98:0x0256), top: B:105:0x000f }] */
    /* JADX WARN: Code duplicated, block: B:81:0x01f9 A[Catch: all -> 0x026c, TryCatch #0 {, blocks: (B:8:0x000f, B:9:0x003f, B:11:0x0045, B:12:0x0051, B:72:0x01cc, B:74:0x01d6, B:76:0x01e4, B:79:0x01ee, B:81:0x01f9, B:83:0x01fd, B:84:0x0208, B:86:0x0210, B:88:0x0214, B:89:0x021f, B:91:0x0227, B:93:0x022f, B:95:0x023b, B:97:0x023f, B:14:0x0056, B:16:0x005e, B:18:0x0066, B:19:0x0080, B:21:0x0088, B:23:0x0090, B:27:0x00a9, B:29:0x00c9, B:30:0x00d2, B:35:0x00e4, B:37:0x00ec, B:41:0x00fd, B:42:0x0104, B:44:0x010e, B:33:0x00dc, B:45:0x0115, B:47:0x011d, B:49:0x0123, B:50:0x012c, B:52:0x0134, B:54:0x013c, B:55:0x0174, B:57:0x017c, B:59:0x0182, B:60:0x0190, B:63:0x0199, B:65:0x01a1, B:67:0x01af, B:70:0x01b5, B:71:0x01bb, B:98:0x0256), top: B:105:0x000f }] */
    /* JADX WARN: Code duplicated, block: B:86:0x0210 A[Catch: all -> 0x026c, TryCatch #0 {, blocks: (B:8:0x000f, B:9:0x003f, B:11:0x0045, B:12:0x0051, B:72:0x01cc, B:74:0x01d6, B:76:0x01e4, B:79:0x01ee, B:81:0x01f9, B:83:0x01fd, B:84:0x0208, B:86:0x0210, B:88:0x0214, B:89:0x021f, B:91:0x0227, B:93:0x022f, B:95:0x023b, B:97:0x023f, B:14:0x0056, B:16:0x005e, B:18:0x0066, B:19:0x0080, B:21:0x0088, B:23:0x0090, B:27:0x00a9, B:29:0x00c9, B:30:0x00d2, B:35:0x00e4, B:37:0x00ec, B:41:0x00fd, B:42:0x0104, B:44:0x010e, B:33:0x00dc, B:45:0x0115, B:47:0x011d, B:49:0x0123, B:50:0x012c, B:52:0x0134, B:54:0x013c, B:55:0x0174, B:57:0x017c, B:59:0x0182, B:60:0x0190, B:63:0x0199, B:65:0x01a1, B:67:0x01af, B:70:0x01b5, B:71:0x01bb, B:98:0x0256), top: B:105:0x000f }] */
    /* JADX WARN: Code duplicated, block: B:91:0x0227 A[Catch: all -> 0x026c, TryCatch #0 {, blocks: (B:8:0x000f, B:9:0x003f, B:11:0x0045, B:12:0x0051, B:72:0x01cc, B:74:0x01d6, B:76:0x01e4, B:79:0x01ee, B:81:0x01f9, B:83:0x01fd, B:84:0x0208, B:86:0x0210, B:88:0x0214, B:89:0x021f, B:91:0x0227, B:93:0x022f, B:95:0x023b, B:97:0x023f, B:14:0x0056, B:16:0x005e, B:18:0x0066, B:19:0x0080, B:21:0x0088, B:23:0x0090, B:27:0x00a9, B:29:0x00c9, B:30:0x00d2, B:35:0x00e4, B:37:0x00ec, B:41:0x00fd, B:42:0x0104, B:44:0x010e, B:33:0x00dc, B:45:0x0115, B:47:0x011d, B:49:0x0123, B:50:0x012c, B:52:0x0134, B:54:0x013c, B:55:0x0174, B:57:0x017c, B:59:0x0182, B:60:0x0190, B:63:0x0199, B:65:0x01a1, B:67:0x01af, B:70:0x01b5, B:71:0x01bb, B:98:0x0256), top: B:105:0x000f }] */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    private final void applyEdit(Row r, Object newValue) {
        String str;
        String prop = r.getProp();
        if (prop == null) {
            return;
        }
        synchronized (this.studio.getLock()) {
            this.studio.getUndo().begin("Change " + r.getDisplay());
            for (Instance instance : CollectionsKt.toList(this.studio.getSelection())) {
                switch (prop.hashCode()) {
                    case -1598813355:
                        if (prop.equals("PivotOffset")) {
                            if (instance.isA("BasePart")) {
                                Studio studio = this.studio;
                                Object obj = instance.get("PivotOffset");
                                CFrame identity = obj instanceof CFrame ? (CFrame) obj : null;
                                if (identity == null) {
                                    identity = CFrame.INSTANCE.getIDENTITY();
                                }
                                Intrinsics.checkNotNull(newValue, "null cannot be cast to non-null type com.goydevv.studrblx.model.Vector3");
                                studio.setProp(instance, "PivotOffset", identity.withPosition((Vector3) newValue));
                            }
                        } else if (!instance.getProps().containsKey(prop) || instance == CollectionsKt.first((List) this.studio.getSelection()) || instance.isA("BasePart")) {
                            this.studio.setProp(instance, prop, newValue);
                            if (r.getType() == 26 && (newValue instanceof Color3)) {
                                instance.getPropTypes().putIfAbsent(prop, 26);
                            }
                            if (r.getType() == 18 && (newValue instanceof EnumValue)) {
                                instance.getPropTypes().putIfAbsent(prop, 18);
                            }
                            if (!Intrinsics.areEqual(prop, "Color3uint8") || Intrinsics.areEqual(prop, "Color")) {
                                if (!instance.getProps().containsKey("BrickColor") && (newValue instanceof Color3)) {
                                    this.studio.setProp(instance, "BrickColor", new BrickColorV(BrickColors.INSTANCE.closest((Color3) newValue)));
                                }
                            }
                        }
                        break;
                    case -1446934467:
                        if (!prop.equals("@Archivable")) {
                            if (!instance.getProps().containsKey(prop)) {
                            }
                            this.studio.setProp(instance, prop, newValue);
                            if (r.getType() == 26) {
                                instance.getPropTypes().putIfAbsent(prop, 26);
                            }
                            if (r.getType() == 18) {
                                instance.getPropTypes().putIfAbsent(prop, 18);
                            }
                            if (!Intrinsics.areEqual(prop, "Color3uint8")) {
                            }
                            if (!instance.getProps().containsKey("BrickColor")) {
                            }
                        } else if (!instance.getIsService()) {
                            instance.setArchivable(Intrinsics.areEqual(newValue, (Object) true));
                        }
                        break;
                    case -100272784:
                        if (!prop.equals("@Orientation")) {
                            if (!instance.getProps().containsKey(prop)) {
                            }
                            this.studio.setProp(instance, prop, newValue);
                            if (r.getType() == 26) {
                                instance.getPropTypes().putIfAbsent(prop, 26);
                            }
                            if (r.getType() == 18) {
                                instance.getPropTypes().putIfAbsent(prop, 18);
                            }
                            if (!Intrinsics.areEqual(prop, "Color3uint8")) {
                            }
                            if (!instance.getProps().containsKey("BrickColor")) {
                            }
                        } else if (instance.isA("BasePart")) {
                            Intrinsics.checkNotNull(newValue, "null cannot be cast to non-null type com.goydevv.studrblx.model.Vector3");
                            Vector3 vector3Times = ((Vector3) newValue).times(RAD);
                            this.studio.setProp(instance, "CFrame", CFrame.INSTANCE.fromOrientation(vector3Times.getX(), vector3Times.getY(), vector3Times.getZ()).withPosition(PartData.INSTANCE.cframe(instance).getPosition()));
                        }
                        break;
                    case 2420395:
                        if (!prop.equals("Name")) {
                            if (!instance.getProps().containsKey(prop)) {
                            }
                            this.studio.setProp(instance, prop, newValue);
                            if (r.getType() == 26) {
                                instance.getPropTypes().putIfAbsent(prop, 26);
                            }
                            if (r.getType() == 18) {
                                instance.getPropTypes().putIfAbsent(prop, 18);
                            }
                            if (!Intrinsics.areEqual(prop, "Color3uint8")) {
                            }
                            if (!instance.getProps().containsKey("BrickColor")) {
                            }
                        } else if (!instance.getIsService()) {
                            this.studio.setProp(instance, "Name", newValue);
                        }
                        break;
                    case 2577441:
                        if (!prop.equals("Size")) {
                            if (!instance.getProps().containsKey(prop)) {
                            }
                            this.studio.setProp(instance, prop, newValue);
                            if (r.getType() == 26) {
                                instance.getPropTypes().putIfAbsent(prop, 26);
                            }
                            if (r.getType() == 18) {
                                instance.getPropTypes().putIfAbsent(prop, 18);
                            }
                            if (!Intrinsics.areEqual(prop, "Color3uint8")) {
                            }
                            if (!instance.getProps().containsKey("BrickColor")) {
                            }
                        } else if (instance.isA("BasePart")) {
                            if (instance.getProps().containsKey("size")) {
                                str = "size";
                            } else {
                                str = "Size";
                            }
                            this.studio.setProp(instance, str, newValue);
                        } else if (instance.getProps().containsKey(prop)) {
                            this.studio.setProp(instance, prop, newValue);
                        }
                        break;
                    case 3530753:
                        if (!prop.equals("size")) {
                            if (!instance.getProps().containsKey(prop)) {
                            }
                            this.studio.setProp(instance, prop, newValue);
                            if (r.getType() == 26) {
                                instance.getPropTypes().putIfAbsent(prop, 26);
                            }
                            if (r.getType() == 18) {
                                instance.getPropTypes().putIfAbsent(prop, 18);
                            }
                            if (!Intrinsics.areEqual(prop, "Color3uint8")) {
                            }
                            if (!instance.getProps().containsKey("BrickColor")) {
                            }
                        } else if (instance.isA("BasePart")) {
                            if (instance.getProps().containsKey("size")) {
                                str = "size";
                            } else {
                                str = "Size";
                            }
                            this.studio.setProp(instance, str, newValue);
                        } else if (instance.getProps().containsKey(prop)) {
                            this.studio.setProp(instance, prop, newValue);
                        }
                        break;
                    case 91513858:
                        if (!prop.equals("@BrickColor")) {
                            if (!instance.getProps().containsKey(prop)) {
                            }
                            this.studio.setProp(instance, prop, newValue);
                            if (r.getType() == 26) {
                                instance.getPropTypes().putIfAbsent(prop, 26);
                            }
                            if (r.getType() == 18) {
                                instance.getPropTypes().putIfAbsent(prop, 18);
                            }
                            if (!Intrinsics.areEqual(prop, "Color3uint8")) {
                            }
                            if (!instance.getProps().containsKey("BrickColor")) {
                            }
                        } else if (instance.isA("BasePart")) {
                            Intrinsics.checkNotNull(newValue, "null cannot be cast to non-null type com.goydevv.studrblx.model.BrickColorV");
                            BrickColorV brickColorV = (BrickColorV) newValue;
                            String str2 = instance.getProps().containsKey("Color") ? "Color" : "Color3uint8";
                            this.studio.setProp(instance, str2, brickColorV.getColor());
                            instance.getPropTypes().putIfAbsent(str2, 26);
                            if (instance.getProps().containsKey("BrickColor")) {
                                this.studio.setProp(instance, "BrickColor", brickColorV);
                            }
                        }
                        break;
                    case 1099480457:
                        if (!prop.equals("@Position")) {
                            if (!instance.getProps().containsKey(prop)) {
                            }
                            this.studio.setProp(instance, prop, newValue);
                            if (r.getType() == 26) {
                                instance.getPropTypes().putIfAbsent(prop, 26);
                            }
                            if (r.getType() == 18) {
                                instance.getPropTypes().putIfAbsent(prop, 18);
                            }
                            if (!Intrinsics.areEqual(prop, "Color3uint8")) {
                            }
                            if (!instance.getProps().containsKey("BrickColor")) {
                            }
                        } else if (instance.isA("BasePart")) {
                            Studio studio2 = this.studio;
                            CFrame cframe = PartData.INSTANCE.cframe(instance);
                            Intrinsics.checkNotNull(newValue, "null cannot be cast to non-null type com.goydevv.studrblx.model.Vector3");
                            studio2.setProp(instance, "CFrame", cframe.withPosition((Vector3) newValue));
                        }
                        break;
                    default:
                        if (!instance.getProps().containsKey(prop)) {
                        }
                        this.studio.setProp(instance, prop, newValue);
                        if (r.getType() == 26) {
                            instance.getPropTypes().putIfAbsent(prop, 26);
                        }
                        if (r.getType() == 18) {
                            instance.getPropTypes().putIfAbsent(prop, 18);
                        }
                        if (!Intrinsics.areEqual(prop, "Color3uint8")) {
                        }
                        if (!instance.getProps().containsKey("BrickColor")) {
                        }
                        break;
                }
            }
            this.studio.getUndo().commit();
            this.studio.markDirty();
            Unit unit = Unit.INSTANCE;
        }
        rebuild();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void edit(final Row r) {
        Function0<Unit> action = r.getAction();
        if (action != null) {
            action.invoke();
            rebuild();
            return;
        }
        Instance inst = (Instance) CollectionsKt.firstOrNull((List) this.studio.getSelection());
        if (inst == null) {
            return;
        }
        if (Intrinsics.areEqual(r.getProp(), "@Source")) {
            this.studio.openScript(inst);
            return;
        }
        if (r.getType() == 19) {
            Object value = r.getValue();
            Instance instance = value instanceof Instance ? (Instance) value : null;
            if (instance != null) {
                this.studio.select(CollectionsKt.listOf(instance));
                return;
            }
            return;
        }
        if (r.getEditable()) {
            if (r.getType() == 2) {
                applyEdit(r, Boolean.valueOf(!Intrinsics.areEqual(r.getValue(), (Object) true)));
                return;
            }
            String strTypeForProp = "Material";
            if (!Intrinsics.areEqual(r.getDisplay(), "Material")) {
                if (StringsKt.endsWith(r.getDisplay(), "Surface", false) && inst.isA("BasePart")) {
                    strTypeForProp = "SurfaceType";
                } else if (Intrinsics.areEqual(r.getDisplay(), "Shape") && inst.isA("BasePart")) {
                    strTypeForProp = "PartType";
                } else {
                    Enums enums = Enums.INSTANCE;
                    String className = inst.getClassName();
                    String prop = r.getProp();
                    if (prop == null) {
                        prop = "";
                    }
                    strTypeForProp = enums.typeForProp(className, prop);
                }
            }
            String enumType = strTypeForProp;
            Dialogs dialogs = Dialogs.INSTANCE;
            Context context = getContext();
            Intrinsics.checkNotNullExpressionValue(context, "getContext(...)");
            dialogs.editValue(context, r.getDisplay(), r.getValue(), r.getType(), enumType, new Function1() { // from class: com.goydevv.studrblx.ui.PropertiesPanel$$ExternalSyntheticLambda0
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return PropertiesPanel.edit$lambda$13(PropertiesPanel.this, r, obj);
                }
            });
        }
    }

    static final Unit edit$lambda$13(PropertiesPanel this$0, Row $r, Object it) {
        this$0.applyEdit($r, it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: compiled from: PropertiesPanel.kt */
    private final class VH extends RecyclerView.ViewHolder {
        private final TextView name;
        private final LinearLayout row;
        private final View swatch;
        final /* synthetic */ PropertiesPanel this$0;
        private final TextView value;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public VH(PropertiesPanel this$0, LinearLayout row, TextView name, View swatch, TextView value) {
            super(row);
            Intrinsics.checkNotNullParameter(row, "row");
            Intrinsics.checkNotNullParameter(name, "name");
            Intrinsics.checkNotNullParameter(swatch, "swatch");
            Intrinsics.checkNotNullParameter(value, "value");
            this.this$0 = this$0;
            this.row = row;
            this.name = name;
            this.swatch = swatch;
            this.value = value;
        }

        public final TextView getName() {
            return this.name;
        }

        public final LinearLayout getRow() {
            return this.row;
        }

        public final View getSwatch() {
            return this.swatch;
        }

        public final TextView getValue() {
            return this.value;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: compiled from: PropertiesPanel.kt */
    final class Adapter extends RecyclerView.Adapter<VH> {
        public Adapter() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return PropertiesPanel.this.rows.size();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            Intrinsics.checkNotNullParameter(parent, "parent");
            Context ctx = parent.getContext();
            LinearLayout linearLayout = new LinearLayout(ctx);
            linearLayout.setOrientation(0);
            linearLayout.setGravity(16);
            Ui ui = Ui.INSTANCE;
            Intrinsics.checkNotNull(ctx);
            linearLayout.setMinimumHeight(ui.dp(ctx, 36.0f));
            linearLayout.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            TextView textViewText$default = Ui.text$default(Ui.INSTANCE, ctx, "", 12.0f, Ui.INSTANCE.getTEXT(), false, 16, null);
            textViewText$default.setPadding(Ui.INSTANCE.dp(ctx, 22.0f), 0, Ui.INSTANCE.dp(ctx, 4.0f), 0);
            textViewText$default.setEllipsize(TextUtils.TruncateAt.END);
            View sw = new View(ctx);
            TextView textViewText$default2 = Ui.text$default(Ui.INSTANCE, ctx, "", 12.0f, Ui.INSTANCE.getTEXT(), false, 16, null);
            textViewText$default2.setPadding(Ui.INSTANCE.dp(ctx, 6.0f), 0, Ui.INSTANCE.dp(ctx, 6.0f), 0);
            textViewText$default2.setEllipsize(TextUtils.TruncateAt.END);
            linearLayout.addView(textViewText$default, new LinearLayout.LayoutParams(0, -2, 0.45f));
            linearLayout.addView(Ui.INSTANCE.divider(ctx, true));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Ui.INSTANCE.dp(ctx, 14.0f), Ui.INSTANCE.dp(ctx, 14.0f));
            layoutParams.leftMargin = Ui.INSTANCE.dp(ctx, 6.0f);
            Unit unit = Unit.INSTANCE;
            linearLayout.addView(sw, layoutParams);
            linearLayout.addView(textViewText$default2, new LinearLayout.LayoutParams(0, -2, 0.55f));
            return new VH(PropertiesPanel.this, linearLayout, textViewText$default, sw, textViewText$default2);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(VH h, int position) {
            Color3 color;
            Intrinsics.checkNotNullParameter(h, "h");
            final Row r = (Row) PropertiesPanel.this.rows.get(position);
            Context ctx = h.getRow().getContext();
            Ui ui = Ui.INSTANCE;
            Intrinsics.checkNotNull(ctx);
            int dsz = ui.dp(ctx, 14.0f);
            if (r.getProp() == null) {
                h.getName().setText(r.getTitle());
                h.getName().setPadding(Ui.INSTANCE.dp(ctx, 6.0f), 0, Ui.INSTANCE.dp(ctx, 4.0f), 0);
                TextView name = h.getName();
                Ui.ArrowDrawable arrowDrawable = new Ui.ArrowDrawable(!PropertiesPanel.this.collapsed.contains(r.getTitle()), Ui.INSTANCE.getTEXT());
                arrowDrawable.setBounds(0, 0, dsz, dsz);
                name.setCompoundDrawables(arrowDrawable, null, null, null);
                h.getName().setCompoundDrawablePadding(Ui.INSTANCE.dp(ctx, 4.0f));
                h.getValue().setCompoundDrawables(null, null, null, null);
                h.getName().setTextColor(Ui.INSTANCE.getTEXT());
                h.getName().setTypeface(null, 1);
                h.getName().setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1.0f));
                h.getValue().setVisibility(8);
                h.getSwatch().setVisibility(8);
                h.getRow().getChildAt(1).setVisibility(8);
                h.getRow().setBackgroundColor(Ui.INSTANCE.getHEADER());
                LinearLayout row = h.getRow();
                final PropertiesPanel propertiesPanel = PropertiesPanel.this;
                row.setOnClickListener(new View.OnClickListener() { // from class: com.goydevv.studrblx.ui.PropertiesPanel$Adapter$$ExternalSyntheticLambda0
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        PropertiesPanel.Adapter.onBindViewHolder$lambda$5(propertiesPanel, r, view);
                    }
                });
                return;
            }
            h.getRow().getChildAt(1).setVisibility(0);
            h.getName().setPadding(Ui.INSTANCE.dp(ctx, 22.0f), 0, Ui.INSTANCE.dp(ctx, 4.0f), 0);
            h.getName().setLayoutParams(new LinearLayout.LayoutParams(0, -2, 0.45f));
            h.getName().setTypeface(null, 0);
            h.getName().setCompoundDrawables(null, null, null, null);
            h.getName().setText(r.getDisplay());
            h.getName().setTextColor((r.getEditable() || r.getAction() != null) ? Ui.INSTANCE.getTEXT() : Ui.INSTANCE.getDIM());
            h.getValue().setVisibility(0);
            h.getRow().setBackgroundColor(Ui.INSTANCE.getPANEL());
            Instance inst = (Instance) CollectionsKt.firstOrNull((List) PropertiesPanel.this.studio.getSelection());
            Object v = r.getValue();
            if (v instanceof Color3) {
                color = (Color3) v;
            } else {
                color = v instanceof BrickColorV ? ((BrickColorV) v).getColor() : null;
            }
            if (color != null) {
                h.getSwatch().setVisibility(0);
                View swatch = h.getSwatch();
                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setColor(color.toArgb());
                gradientDrawable.setStroke(Ui.INSTANCE.dp(ctx, 1.0f), -7829368);
                swatch.setBackground(gradientDrawable);
            } else {
                h.getSwatch().setVisibility(8);
            }
            if (r.getType() == 2) {
                h.getValue().setText("");
                TextView value = h.getValue();
                Ui.CheckBoxDrawable checkBoxDrawable = new Ui.CheckBoxDrawable(Intrinsics.areEqual(r.getValue(), (Object) true), ctx.getResources().getDisplayMetrics().density);
                checkBoxDrawable.setBounds(0, 0, dsz, dsz);
                value.setCompoundDrawables(checkBoxDrawable, null, null, null);
                h.getValue().setCompoundDrawablePadding(Ui.INSTANCE.dp(ctx, 6.0f));
            } else {
                h.getValue().setCompoundDrawables(null, null, null, null);
                h.getValue().setText(PropertiesPanel.this.valueText(r, inst));
            }
            TextView value2 = h.getValue();
            Function0<Unit> action = r.getAction();
            int text = Ui.INFO;
            if (action == null && r.getType() != 19) {
                text = Ui.INSTANCE.getTEXT();
            }
            value2.setTextColor(text);
            LinearLayout row2 = h.getRow();
            final PropertiesPanel propertiesPanel2 = PropertiesPanel.this;
            row2.setOnClickListener(new View.OnClickListener() { // from class: com.goydevv.studrblx.ui.PropertiesPanel$Adapter$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    propertiesPanel2.edit(r);
                }
            });
        }

        static final void onBindViewHolder$lambda$5(PropertiesPanel this$0, Row $r, View it) {
            if (!this$0.collapsed.add($r.getTitle())) {
                this$0.collapsed.remove($r.getTitle());
            }
            this$0.rebuild();
        }
    }
}

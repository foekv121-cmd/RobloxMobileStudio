package com.goydevv.studrblx.ui;

import com.goydevv.studrblx.model.CFrame;
import com.goydevv.studrblx.model.Color3;
import com.goydevv.studrblx.model.EnumValue;
import com.goydevv.studrblx.model.Instance;
import com.goydevv.studrblx.model.InstanceFactory;
import com.goydevv.studrblx.model.UDim2;
import com.goydevv.studrblx.model.Vector2;
import com.goydevv.studrblx.model.Vector3;
import java.util.List;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: ToolboxPanel.kt */
/* JADX INFO: loaded from: classes5.dex */
public final class Samples {
    public static final Samples INSTANCE = new Samples();
    private static final List<Sample> all = CollectionsKt.listOf(new Sample[]{new Sample("Part", "Kill Brick", new Function0() { // from class: com.goydevv.studrblx.ui.Samples$$ExternalSyntheticLambda0
        @Override // kotlin.jvm.functions.Function0
        public final Object invoke() {
            return Samples.all$lambda$1();
        }
    }), new Sample("Script", "Spinner", new Function0() { // from class: com.goydevv.studrblx.ui.Samples$$ExternalSyntheticLambda1
        @Override // kotlin.jvm.functions.Function0
        public final Object invoke() {
            return Samples.all$lambda$3();
        }
    }), new Sample("SpawnLocation", "Jump Pad", new Function0() { // from class: com.goydevv.studrblx.ui.Samples$$ExternalSyntheticLambda2
        @Override // kotlin.jvm.functions.Function0
        public final Object invoke() {
            return Samples.all$lambda$5();
        }
    }), new Sample("ClickDetector", "Click Color", new Function0() { // from class: com.goydevv.studrblx.ui.Samples$$ExternalSyntheticLambda3
        @Override // kotlin.jvm.functions.Function0
        public final Object invoke() {
            return Samples.all$lambda$7();
        }
    }), new Sample("ProximityPrompt", "Tween Door", new Function0() { // from class: com.goydevv.studrblx.ui.Samples$$ExternalSyntheticLambda4
        @Override // kotlin.jvm.functions.Function0
        public final Object invoke() {
            return Samples.all$lambda$9();
        }
    }), new Sample("ScreenGui", "Click Counter GUI", new Function0() { // from class: com.goydevv.studrblx.ui.Samples$$ExternalSyntheticLambda5
        @Override // kotlin.jvm.functions.Function0
        public final Object invoke() {
            return Samples.all$lambda$10();
        }
    }), new Sample("Folder", "Falling Blocks", new Function0() { // from class: com.goydevv.studrblx.ui.Samples$$ExternalSyntheticLambda6
        @Override // kotlin.jvm.functions.Function0
        public final Object invoke() {
            return Samples.all$lambda$11();
        }
    })});

    /* JADX INFO: compiled from: ToolboxPanel.kt */
    public static final class Sample {
        private final Function0<Instance> build;
        private final String icon;
        private final String name;

        /* JADX WARN: Multi-variable type inference failed */
        public Sample(String icon, String name, Function0<? extends Instance> build) {
            Intrinsics.checkNotNullParameter(icon, "icon");
            Intrinsics.checkNotNullParameter(name, "name");
            Intrinsics.checkNotNullParameter(build, "build");
            this.icon = icon;
            this.name = name;
            this.build = (Function0<Instance>) build;
        }

        public final Function0<Instance> getBuild() {
            return this.build;
        }

        public final String getIcon() {
            return this.icon;
        }

        public final String getName() {
            return this.name;
        }
    }

    private Samples() {
    }

    static /* synthetic */ Instance part$default(Samples samples, String str, Vector3 vector3, Color3 color3, boolean z, int i, Object obj) {
        if ((i & 8) != 0) {
            z = false;
        }
        return samples.part(str, vector3, color3, z);
    }

    private final Instance part(String name, Vector3 size, Color3 color, boolean neon) {
        Instance p = InstanceFactory.INSTANCE.create("Part");
        p.setName(name);
        p.set("size", size, false);
        p.set("Color3uint8", color, false);
        p.set("Anchored", true, false);
        if (neon) {
            p.set("Material", new EnumValue(288), false);
        }
        return p;
    }

    static /* synthetic */ void script$default(Samples samples, Instance instance, String str, String str2, String str3, int i, Object obj) {
        if ((i & 8) != 0) {
            str3 = "Script";
        }
        samples.script(instance, str, str2, str3);
    }

    private final void script(Instance parent, String name, String src, String cls) {
        Instance s = InstanceFactory.INSTANCE.create(cls);
        s.setName(name);
        s.set("Source", StringsKt.trimIndent(src) + "\n", false);
        s.attachRaw(parent);
    }

    public final List<Sample> getAll() {
        return all;
    }

    static final Instance all$lambda$1() {
        Instance instancePart = INSTANCE.part("KillBrick", new Vector3(8.0f, 1.0f, 8.0f), new Color3(1.0f, 0.1f, 0.1f), true);
        script$default(INSTANCE, instancePart, "KillScript", "\n                    local part = script.Parent\n                    part.Touched:Connect(function(hit)\n                        local humanoid = hit.Parent:FindFirstChildOfClass(\"Humanoid\")\n                        if humanoid then\n                            humanoid.Health = 0\n                        end\n                    end)\n                ", null, 8, null);
        return instancePart;
    }

    static final Instance all$lambda$3() {
        Instance instancePart$default = part$default(INSTANCE, "Spinner", new Vector3(12.0f, 1.0f, 2.0f), Color3.INSTANCE.fromRGB(255, 176, 0), false, 8, null);
        script$default(INSTANCE, instancePart$default, "Spin", "\n                    local RunService = game:GetService(\"RunService\")\n                    local part = script.Parent\n                    local speed = 2 -- radians per second\n                    RunService.Heartbeat:Connect(function(dt)\n                        part.CFrame = part.CFrame * CFrame.Angles(0, speed * dt, 0)\n                    end)\n                ", null, 8, null);
        return instancePart$default;
    }

    static final Instance all$lambda$5() {
        Instance instancePart = INSTANCE.part("JumpPad", new Vector3(6.0f, 0.5f, 6.0f), Color3.INSTANCE.fromRGB(0, 255, 120), true);
        script$default(INSTANCE, instancePart, "Launch", "\n                    local pad = script.Parent\n                    local debounce = {}\n                    pad.Touched:Connect(function(hit)\n                        local character = hit.Parent\n                        local root = character and character:FindFirstChild(\"HumanoidRootPart\")\n                        if root and not debounce[character] then\n                            debounce[character] = true\n                            root.AssemblyLinearVelocity = Vector3.new(0, 120, 0)\n                            task.wait(0.5)\n                            debounce[character] = nil\n                        end\n                    end)\n                ", null, 8, null);
        return instancePart;
    }

    static final Instance all$lambda$7() {
        Instance instancePart$default = part$default(INSTANCE, "ColorButton", new Vector3(4.0f, 4.0f, 4.0f), Color3.INSTANCE.fromRGB(13, 105, 172), false, 8, null);
        InstanceFactory.INSTANCE.create("ClickDetector").attachRaw(instancePart$default);
        script$default(INSTANCE, instancePart$default, "OnClick", "\n                    local part = script.Parent\n                    part.ClickDetector.MouseClick:Connect(function(player)\n                        part.Color = Color3.fromHSV(math.random(), 0.8, 1)\n                        print(player.Name .. \" changed the color!\")\n                    end)\n                ", null, 8, null);
        return instancePart$default;
    }

    static final Instance all$lambda$9() {
        Instance instancePart$default = part$default(INSTANCE, "Door", new Vector3(6.0f, 10.0f, 1.0f), Color3.INSTANCE.fromRGB(124, 92, 70), false, 8, null);
        Instance instanceCreate = InstanceFactory.INSTANCE.create("ProximityPrompt");
        instanceCreate.set("ActionText", "Open", false);
        instanceCreate.attachRaw(instancePart$default);
        script$default(INSTANCE, instancePart$default, "DoorScript", "\n                    local TweenService = game:GetService(\"TweenService\")\n                    local door = script.Parent\n                    local closed = door.CFrame\n                    local open = closed * CFrame.new(0, 9, 0)\n                    local isOpen = false\n                    local info = TweenInfo.new(0.6, Enum.EasingStyle.Quad, Enum.EasingDirection.Out)\n\n                    door.ProximityPrompt.Triggered:Connect(function()\n                        isOpen = not isOpen\n                        door.ProximityPrompt.ActionText = if isOpen then \"Close\" else \"Open\"\n                        TweenService:Create(door, info, { CFrame = if isOpen then open else closed }):Play()\n                    end)\n                ", null, 8, null);
        return instancePart$default;
    }

    static final Instance all$lambda$10() {
        Instance gui = InstanceFactory.INSTANCE.create("ScreenGui");
        gui.setName("ClickCounter");
        Instance btn = InstanceFactory.INSTANCE.create("TextButton");
        btn.setName("Button");
        btn.set("Size", new UDim2(0.0f, 220, 0.0f, 60), false);
        btn.set("Position", new UDim2(0.5f, 0, 0.0f, 20), false);
        btn.set("AnchorPoint", new Vector2(0.5f, 0.0f), false);
        btn.set("BackgroundColor3", Color3.INSTANCE.fromRGB(14, 122, 206), false);
        btn.set("TextColor3", new Color3(1.0f, 1.0f, 1.0f), false);
        btn.set("TextSize", Float.valueOf(22.0f), false);
        btn.set("Font", new EnumValue(19), false);
        btn.set("Text", "Clicks: 0", false);
        btn.attachRaw(gui);
        InstanceFactory.INSTANCE.create("UICorner").attachRaw(btn);
        INSTANCE.script(btn, "Counter", "\n                local button = script.Parent\n                local clicks = 0\n                button.MouseButton1Click:Connect(function()\n                    clicks += 1\n                    button.Text = `Clicks: {clicks}`\n                end)\n            ", "LocalScript");
        return gui;
    }

    static final Instance all$lambda$11() {
        Instance m = InstanceFactory.INSTANCE.create("Folder");
        m.setName("FallingBlocks");
        for (int i = 0; i < 6; i++) {
            Instance p = part$default(INSTANCE, "Block" + i, new Vector3(2.0f, 2.0f, 2.0f), Color3.INSTANCE.fromHSV(i / 6.0f, 0.7f, 1.0f), false, 8, null);
            p.set("Anchored", false, false);
            p.set("CFrame", new CFrame((i % 3) * 3.0f, 10.0f + (i * 3.0f), (i / 3) * 3.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 4088, null), false);
            p.attachRaw(m);
        }
        return m;
    }
}

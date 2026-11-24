<div align="center">

# StudioRblx

**A native Android 3D Roblox-compatible Studio editor & playtest engine.**

Build, inspect, script, import `.rbxl` / `.rbxlx` / `.rbxm` worlds, and playtest with official Roblox R15/R6 animations, avatar accessories, and materials — directly on Android.

[![Platform](https://img.shields.io/badge/Platform-Android%207.0%2B%20(API%2024--35)-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/)
[![Package](https://img.shields.io/badge/Package-com.goydevv.studrblx-00A2FF?style=for-the-badge)](https://github.com/GoyDevv/MobileStudio)
[![Graphics](https://img.shields.io/badge/Graphics-OpenGL%20ES%202.0%2F3.0-5586A4?style=for-the-badge&logo=opengl&logoColor=white)](https://www.khronos.org/opengles/)
[![JDK](https://img.shields.io/badge/JDK-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://adoptium.net/)

</div>

## Overview

**StudioRblx** (`com.goydevv.studrblx`) is an Android-native 3D world editor and playtest runtime engineered around Roblox's place format, Studio ribbon/dock workflow, and live playtesting loop. Open existing `.rbxl`, `.rbxlx`, and `.rbxm` files, inspect and edit instances in real time, write Luau scripts with autocomplete, and jump straight into playtesting with your Roblox avatar.

## Core Capabilities

| Subsystem | What It Does |
| :--- | :--- |
| **3D Viewport & Rendering** | OpenGL ES 2.0/3.0 renderer with shadow mapping, PBR/classic Roblox material atlases (`modern` & `pre2022`), skyboxes, frustum culling, static batching, CSG unions, and **Draco-compressed Roblox v6/v7 meshes**. |
| **World & Asset Formats** | Native binary `.rbxl` / `.rbxm` (`LZ4` + shared strings) and XML `.rbxlx` readers/writers with full instance hierarchy preservation. |
| **Avatar & Animation Engine** | Loads your Roblox account or custom user avatar (R6 & R15), body colors, clothing templates, and modern UGC accessories, driven by official Roblox keyframe animations (`DefaultAnimate` + `AnimPlayer`) with instant zero-delay R6 jumping. |
| **Luau Scripting & Editor** | Built-in Luau transpiler/runtime (`Instance`, `Vector3`, `CFrame`, `TweenService`, `RunService`, `UserInputService`, `Players`, `Lighting`, `Sound`, `ProximityPrompt`) paired with a syntax-highlighted `ScriptEditor` and autocomplete. |
| **Studio Workspace UI** | Dockable Explorer, Properties, Toolbox (Creator Store), Output console, Script Editor, Transform gizmos (Select / Move / Scale / Rotate with grid snap), Undo/Redo, and mobile Playtest HUD. |
| **Persistent Crash Detector** | Isolated `:crash` process (`CrashActivity`) backed by continuous rolling `session_log.txt` capture and Android 11–15 `ApplicationExitInfo` inspection so Java, native GLES, ANR, and OOM crashes are always detected on next launch with one-tap `.txt` export. |

## Project Structure

```text
MobileStudio/
├── .github/workflows/build-debug.yml   # GitHub Actions CI (JDK 17 + public keystore signing)
├── app/
│   ├── build.gradle.kts                # com.goydevv.studrblx (compileSdk 35, minSdk 24)
│   └── src/main/
│       ├── AndroidManifest.xml         # MainActivity + isolated :crash CrashActivity
│       ├── assets/
│       │   ├── fonts/                  # 40+ bundled Roblox-compatible fonts
│       │   └── materials/              # Modern & pre-2022 Roblox PBR/surface textures
│       └── java/com/goydevv/studrblx/
│           ├── core/                   # Studio state, UndoManager, OutputLog, SolidModeling
│           ├── engine/                 # PlaySession, Avatar, DefaultAnimate, AnimPlayer, CSG
│           ├── format/                 # RbxBinaryReader/Writer, RbxXmlReader, Lz4, CsgMesh
│           ├── lua/                    # LuauTranspiler, LuaRuntime, RobloxServices, InstanceApi
│           ├── net/                    # RobloxApi, AssetFetcher, RobloxCloud, ImageLoader
│           ├── render/                 # SceneRenderer, RbxMesh, Draco, MaterialTextures
│           └── ui/                     # DockLayout, Explorer, Properties, ScriptEditor, PlayHud
├── gradle.properties
└── README.md
```

## Building from Source

### Requirements
- **JDK 17** (Eclipse Temurin recommended)
- **Android SDK** with Platform `35` (`compileSdk = 35`, `targetSdk = 35`, `minSdk = 24`)
- **Gradle 8.11+** (via `./gradlew`)

### Local Build

```bash
chmod +x ./gradlew
./gradlew assembleDebug
```

The built APK is output to:

```text
app/build/outputs/apk/debug/app-debug.apk
```

> **Note for Linux / Desktop builds:** If your `gradle.properties` contains a Termux/Android `android.aapt2FromMavenOverride=/usr/bin/aapt2` line, remove it before building on Linux/macOS/Windows so Android Gradle Plugin uses the standard Maven AAPT2 binary.

### GitHub Actions CI

Push to `main` or `master` (or trigger **Build Debug APK** manually from the Actions tab). The included `.github/workflows/build-debug.yml` automatically:
1. Checks out the repo (`actions/checkout@v6`) and configures JDK 17 (`actions/setup-java@v5`).
2. Strips any mobile-only `android.aapt2FromMavenOverride` entry from `gradle.properties`.
3. Builds `assembleDebug`, signs & `zipalign`s the APK with `signing/studrblx-public.jks` (or GitHub Secrets if configured), verifies the signature with `apksigner`, and uploads `StudioRblx-debug-apk`.

## Crash Logs & Diagnostics

If StudioRblx encounters an unexpected error:
- The **Crash Detector** opens immediately (or automatically on next launch via `last_crash.txt` and Android `ApplicationExitInfo` for native/ANR/OOM exits).
- Tap **Export `.txt`** to save the full stack trace, session breadcrumbs, Studio output, and logcat tail to your device storage.
- Automatic copies are also stored in `Android/data/com.goydevv.studrblx/files/crash_logs/`.

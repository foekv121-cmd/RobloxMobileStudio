# Player animations, collision groups and noclip

## Custom player animations
1. Animator: pose a rig, key it, then **File > Use as player animation...** and pick idle / walk / run / jump / fall / climb / sit.
   This puts a KeyframeSequence in `StarterPlayer > StarterCharacterScripts > PlayerAnimations`.
2. Playtest: the player now uses your animation for that state. States you didn't set keep the Roblox default.
3. **File > Export player animations (.rbxm)** saves the PlayerAnimations folder as a file. Drop that .rbxm into any place
   (or import it) and the same animations are used there.

Pack rules (you can also build it by hand): a Folder or Model named `PlayerAnimations` anywhere in the place with
- KeyframeSequences named idle, walk, run, jump, fall, climb, sit (idle2, walk_2 ... are extra variants), or
- Animation objects with those names (AnimationId, or a KeyframeSequence inside), or
- a Folder per state holding KeyframeSequences/Animations (a NumberValue "Weight" inside one makes it likelier).
Sub-folders named `R6` / `R15` are picked by the character's rig type. A Roblox style `Animate` folder inside StarterPlayer works too.

## Collision (quick menu > Collision...)
- **Collision OFF / ON** for the selected model or parts: every part inside changes, and the model remembers it.
- **Player walks through everything (noclip)** and **Player's group**.
- **Collision groups**: make groups, tick which groups collide, put the selection in a group.
  Everything is saved in the place (ServerStorage > __CollisionConfig).
- Scripts: `character:SetAttribute("Noclip", true)`, `model:SetAttribute("CanCollide", false)`, and the real
  `PhysicsService` API (RegisterCollisionGroup, CollisionGroupSetCollidable, SetPartCollisionGroup, ...) now work.
- Not covered: RaycastParams.CollisionGroup (rays still only respect CanCollide).

## Animations and UI that come from a .rbxm
- **Animation that can't be downloaded.** An `Animation` object normally only holds an `rbxassetid://` link, and Roblox won't let the app
  download animations you don't own without a login (Roblox Account settings: Open Cloud key or cookie). When that happens, or the
  Animation has no id, the app now plays the **KeyframeSequence with the same name** from your place (for example the one that came in the same .rbxm),
  and says so in Output. Put an exported KeyframeSequence in your place and name it like the Animation.
- Still not supported: CurveAnimation assets, and playing a KeyframeSequence that no Animation object refers to.
- **UI kits.** A Folder or Model that holds ScreenGuis (and no parts) now has each ScreenGui moved to StarterGui on import, because a ScreenGui
  only shows from there. Other things in that folder stay in Workspace.
- Not supported by the UI renderer: ViewportFrame and VideoFrame. SurfaceGui isn't handled by the screen overlay.

# Animator (v4, Sun Animator style)

Select a Model in the Explorer, then File > Animator.

- **Tracks**: every Motor6D gets a joint track (its Transform is animated). Parts that no joint moves get a part track (absolute CFrame). Welds follow their Part0.
- **Pose + Key**: pose with Move/Rotate, tap Key. Tap a track name first to key only that track. Long press a row to add a key at that frame.
- **Timeline**: tap/drag the ruler to scrub, drag diamonds to retime, Multi for multi-select, pinch to zoom, drag empty space to pan.
- **Easing**: per key, 13 styles (Linear, Constant, Sine, Quad, Cubic, Quart, Quint, Sextic, Expo, Circ, Back, Bounce, Elastic) x In/Out/InOut/OutIn, with Overshoot / Amplitude / Period.
- **Menus**: File (save, load, export/import KeyframeSequence, settings), Edit (undo/redo, copy/paste, select), Key, Frame (stretch, offset, repeat, fit, markers, reverse), Options.
- **Save to model** writes `Script "Anim_<name>"` (editable data + a player that sets Motor6D.Transform / part CFrame). Old v1 `Anim_` scripts still load.
- **Export KeyframeSequence** writes a Roblox KeyframeSequence (joint tracks only) into the model; **Import** reads one back.

Code: `engine/SunEase`, `engine/SunAnim`, `engine/SunRig`, `ui/TimelineView`, `ui/AnimatorUi`.

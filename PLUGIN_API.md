# Mobile Studio Plugin API

```
MOBILE STUDIO PLUGIN API  (v1)
Plugins are Lua scripts. They run in a sandbox: no io, os, require, load, debug or network.
Each call needs the permission shown in [brackets]; the user grants permissions.
A script that runs longer than the time limit (Plugin Settings) is stopped and disabled.

GENERAL
app.log(...)            write to the Output panel
print(...)              same as app.log
app.plugin              {id, name, version}
app.apiVersion          number
app.notify(text)        [notify] short pop-up message

PROJECT
app.project.getInfo()   [read_project] {fileName, descendantCount, services = {names}, isPlaying}
app.project.find(path)  [read_project] handle or nil, e.g. 'Workspace.Baseplate'
app.project.children(h) [read_project] list of handles (max 500)

SELECTION
app.selection.get()     [selection] list of handles (max 200)

Handles are tables {id, name, className}. Pass a handle (or its id) to the functions below.

OBJECTS
app.objects.create(className, {name=, parent=handle})  [create_objects] returns handle
    Only classes from the Insert menu. Parent defaults to Workspace.
app.objects.getProperty(h, name)       [read_project] string / number / boolean
app.objects.setProperty(h, name, v)    [modify_properties]
    Only text, number and on/off properties that already exist. 'Source' and 'Parent' are blocked.
    Editing is refused while a playtest is running. Changes are not added to Undo yet.

UI
app.ui.addToolbarButton{text=, id=, icon=, onClick=function}  [ui_toolbar]
    Buttons appear in File menu > Plugins section. Max 5 per plugin.
app.ui.addMenu{title=, icon=, items={{text=, onClick=function}}}  [ui_toolbar]
    One entry in File menu > Plugins that opens a list of up to 8 actions. Counts toward the 5 buttons.
app.ui.showPanel{title=, text=, buttons={{text=, onClick=function}}}  [ui_panel]

FILES
app.files.list()  app.files.read(name)  app.files.write(name, text)  [files]
    Private folder per plugin, file names like 'notes.txt', 256 KB per file, 1 MB total.

NOT AVAILABLE
Roblox web APIs, API keys, cookies, tokens, other plugins' data, the rest of the phone's storage.
```

PLUGIN FILES (.rmsp)
A plugin is saved as a .rmsp file (Roblox Mobile Studio Plugin): a JSON text file with the
plugin's name, description, author, version, icon, requested permissions and Lua code.
Export: Plugins > My Plugins > Export .rmsp.   Import: Plugins > Import > Choose .rmsp file.
Imported plugins are validated and you review the permissions before they install.

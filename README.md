# crayonscript-intellij-plugin

![Build](https://github.com/CrayonScript/crayonscript-intellij-plugin/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/16582-crayonscript.svg)](https://plugins.jetbrains.com/plugin/16582-crayonscript)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/16582-crayonscript.svg)](https://plugins.jetbrains.com/plugin/16582-crayonscript)

## Template ToDo list
- [x] Create a new [IntelliJ Platform Plugin Template][template] project.
- [x] Verify the [pluginGroup](/gradle.properties), [plugin ID](/src/main/resources/META-INF/plugin.xml) and [sources package](/src/main/kotlin).
- [x] Review the [Legal Agreements](https://plugins.jetbrains.com/docs/marketplace/legal-agreements.html).
- [x] [Publish a plugin manually](https://plugins.jetbrains.com/docs/intellij/publishing-plugin.html?from=IJPluginTemplate) for the first time.
- [x] Set the Plugin ID in the above README badges.
- [x] Set the [Deployment Token](https://plugins.jetbrains.com/docs/marketplace/plugin-upload.html).
- [x] Click the <kbd>Watch</kbd> button on the top of the [IntelliJ Platform Plugin Template][template] to be notified about releases containing new features and fixes.


## Plugin Overview

<!-- Plugin description -->

CrayonScript is a visual programming language to develop games, stories, animations and other applications in Unity.

A CrayonScript application is visually made up of Blocks.

Blocks connect together like a Jigsaw puzzle to create a workflow.

An application is a collection of workflows that are stitched together. 

Every application has at least one workflow called the "Main" workflow.

The "Main" workflow run when the application runs in Unity Editor or on a device.

Blocks have "Commands" that run as part of the workflow.

CrayonScript workflows are easy to visualize since they are laid out as visual blocks in the IntelliJ IDE.

They are also debuggable, which means that you can pause the application within a Block and view the internal state of the application.

Your entire application "Code" can now be visualized developed and debugged in an intuitive layout.

### Designing a Game

As a Unity 3D game designer, you would continue to design game assets within Unity using the powerful Unity Editor.

For example, you would continue to design Scenes, Animators, Particle Effects, Materials, Textures, Models, Shaders, Audio etc. within Unity.

CrayonScript will take care of all aspects of programming the game.

In other words, you no longer need to worry about writing and managing C# scripts, or understand internals of MonoBehaviour, Coroutines, Yields etc.
   
The CrayonScript IDE will provide you with the Blocks necessary to build the game flow and associate the game flow with the Unity Assets and Data.

Please see the section on [CrayonScript Blocks](#crayonscript-blocks) for more info on Blocks.

<!-- Plugin description end -->

### CrayonScript Blocks

There are different types of blocks:

1. Control: Control Blocks are used to control the workflow such as If Else, and Loops
2. Grouping: Grouping Blocks group other blocks in series or in parallel. Blocks in series run one after another. Blocks in parallel run together.
3. Animator: Animator blocks run animations. Animations are defined in the Unity Editor. 
   Animator blocks have associated data objects which associates the animator with the animation asset, duration override etc.
   Animator commands would typically be the animation to run.
4. Audio: Audio blocks manage Audio assets.
   Audio blocks have associated data objects which associates the audio block with the wav file, audio duration, duck, delay behavior etc.
   Audio commands would be "StartAudio", "StopAudio"
5. Text: Text blocks manage text elements in the game.
   The "SetText" command sets the text.
6. Button: Button blocks manage button elements in the game. 
   The "OnClick" command handles single click.
   The "OnDoubleClick" command handles double clicks.
7. PopUp: PopUp blocks manage popups in the game. 
   PopUp blocks have associated configured popup data to set up the title, alignment and click through behavior, surfacing details such as delay, max shows etc.
   The "Show" shows the popup.
   The "Hide" hides the popup.
   The "OnClick" handles the on click behavior
   The "OnClose" handles the on close behavior.
8. GameObject: GameObject blocks manage game object properties in the game.
   For example, associated commands would be "Show", "Hide", "Disable", "Enable", "OnClick", "OnTouch", "OnLongTouch", "OnMultiTouch", "OnDrag"
9. GameControl: Game Control Blocks are specialized blocks that manage game control and are specialized for the game domain. 
   They have associated Unity game objects and associated game data. 
   The associated game data is externally managed as tables and imported into the game. 
   For example, a BetLevelBlock is a GameControl block that has associated BetLevel game data and associated BetLevel game object.
   The BetLevelBlock would have "Increase" and "Decrease" commands.
   Similarly, a ReelBlock is a GameControl block that manages the reel game object and reel data in a slots game.
   The ReelBlock would have "StartSpin", "ProcessOutcome", "StopSpin" and other related commands.
   A WinBoxBlock would have a "RollUp" command to rollup the wins, a "Hide" command to hide the win box.

## Installation

- Using IDE built-in plugin system:
  
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "crayonscript-intellij-plugin"</kbd> >
  <kbd>Install Plugin</kbd>
  
- Manually:

  Download the [latest release](https://github.com/CrayonScript/crayonscript-intellij-plugin/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template

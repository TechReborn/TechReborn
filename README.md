# TechReborn

TechReborn is a ressurection of GregTech 4 for modern Minecraft. It brings most of GT4's features and behaviors to Minecraft, with lots of tweaks, and a few editions. It also aims to be more compatible with other mods than GregTech, and has the ultimate goal of becoming a standalone mod, compatible with but not dependant on IC2.

TechReborn is currently in a beta state.

We have permission to use Greg's textures, see here :https://i.imgur.com/YQEMrq5.png?1

[![Build Status](http://modmuss50.me:8080/buildStatus/icon?job=TechReborn)](http://modmuss50.me:8080/job/TechReborn/)

## Debugging the coremod

If you need to debugg the coremod methord and method striping code you must use a custom launch argument to tell forge about the Loading plugin, this is not needed when not running in a none developer environment.

`-Dfml.coreMods.load=techreborn.asm.LoadingPlugin`

Add that to your JVM argments in the run configurations in your chosen IDE. This is not manditory but it is only needed if you want to debug the asm core mod code.


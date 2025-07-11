# PaperFixes
A collection of bug and performance fixes from CraftBukkit, Spigot, and Paper in a Forge mod.\
This is a performance mod, and bugs and incompatibilities could occur. Please open a GitHub issue if you find any.

## Current Fixes and Features
See [DETAILS.md](https://github.com/Elephant1214/PaperFixes/blob/main/DETAILS.md)

## Known Incompatibilities
- The improved tick loop is not compatible with the `mixin.bugfix.slow_tps_catchup` option from
  [VintageFix](https://github.com/embeddedt/VintageFix) (removed as of commit [ecbc3e1](https://github.com/embeddedt/VintageFix/commit/ecbc3e193c7fc9bee85577fa5e9f362c6249d82a)) and the tick loop changes from [Forged Carpet](https://github.com/DeadlyMC/forged-carpet) and likely from any other mod.
- Bad core mods can cause re-entry errors and crash the game as soon as it starts. This is not PaperFixes' fault and is unavoidable because these mods are loading game classes too early. The only solution is to replace them with a fork that fixes the problems.\
  An incomplete list of problematic core mods and solutions:
    - [Quark](https://modrinth.com/mod/quark): Replace with [Quark: RoTN Edition](https://www.curseforge.com/minecraft/mc-mods/quark-rotn-edition) (Thanks to sahih_international for finding this)

# PaperFixes
A collection of bug and performance fixes from CraftBukkit, Spigot, and Paper in a Forge mod.\
Keep in mind that this mod is very work-in-progress and bugs and incompatibilities could occur,
please open an issue if you find any.

## Current Fixes

### Common (Integrated & Dedicated Server)
- Chest open and close sounds are no longer processed in the tile entity tick loop
- Invalid mob spawners are removed when detected
- [MC-54738](https://bugs.mojang.com/browse/MC-54738)
- Explosions no longer process dead entities
- Block density is cached so that expensive lookup operations aren't done every time there's an explosion
- [MC-80966](https://bugs.mojang.com/browse/MC-80966)
- The outbound packet queue for a player is cleared on disconnect
- [MC-133373](https://bugs.mojang.com/browse/MC-133373)
- [MC-98153](https://bugs.mojang.com/browse/MC-98153)
- The game tick look has been significantly improved and catches up on missed ticks instead of skipping them.
  There are two options, always try to keep the TPS at 19 or higher (default) and no sleep on lag.
- The new TPS command appears to be a bit inaccurate at the moment.
  The improved tick loop comes with a new command for viewing TPS, `/tps`.
  The command provides a quick overview of *average* TPS from the last five seconds,
  one minute, five minutes, and fifteen minutes.
- Null (invalid or broken) tile entities that could exist when a world is having its entities updated are removed when detected
- The ability to toggle [spawn chunks](https://minecraft.fandom.com/wiki/Spawn_chunk) if they're not being used. Read below to find out why you might not want them enabled.
    - Spawn chunks are a 25x25 area of chunks around spawn that is always loaded by the game.
    - A smaller 19x19 chunk area is constantly fully ticked, except [random ticks](https://minecraft.fandom.com/wiki/Tick#Random_tick), as if a player were in it. 
    - This is the equivalent of three and a half players' worth of ticked chunks if you use the default ten-chunk (10x10 area) view distance.

## Incompatibilities
- The improved tick loop is not compatible with the `mixin.bugfix.slow_tps_catchup` option from
  [VintageFix](https://www.curseforge.com/minecraft/mc-mods/vintagefix) as they both overwrite the same method.
- While working on a mod pack which included PaperFixes, there were odd chunk loading issues where chunks wouldn't load
  at all no matter what I did.
  I don't know what the actual cause of this is since it does not occur with the two mods alone,
  but should you come across it, disabling `mixin.chunk_access` from VintageFix appears to fix it.
- You should disable the fix for MC-80966 if you're using any mods that already fix it or fix the entire lighting engine such as Phosphor, Herperus, or Alfheim.
  This should only apply to 0.4.1-BETA and below, as it was fixed after that version. You should be using the latest version anyway, though.

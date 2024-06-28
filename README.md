# PaperFixes
A collection of bug and performance fixes from CraftBukkit, Spigot, and Paper in a Forge mod.\
Remember that this mod is a work-in-progress and bugs and incompatibilities could occur, please open an issue if you find any.\
The mod is on 1.x.x because it's stable and I haven't seen any new incompatibilities.

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
- The improved tick loop comes with a new command for viewing TPS, `/tps`.
  The command provides a quick overview of *average* TPS from the last five seconds,
  one minute, five minutes, and fifteen minutes.
  This command appears to be inaccurate for some unknown reason, and I would generally recommend Spark for accurate TPS,
  tick usage, and system information.
- Null (invalid or broken) tile entities that could exist when a world is having its entities updated are removed when detected
- The ability to toggle [spawn chunks](https://minecraft.fandom.com/wiki/Spawn_chunk) if they're not being used. Read below to find out why you might not want them enabled.
    - Spawn chunks are a 25x25 area of chunks around spawn that is always loaded by the game.
    - A smaller 19x19 chunk area is constantly fully ticked, except [random ticks](https://minecraft.fandom.com/wiki/Tick#Random_tick), as if a player were in it. 
    - This is the equivalent of three and a half players' worth of ticked chunks if you use the default ten-chunk (10x10 area) view distance.
- Entities now use a shared Random instance. Before, the game created a new Random instance for each entity, which is just unnecessary memory usage. This also removes the rand.setSeed call from EntitySquid since it's pointless and would also just cause an error to be printed every time a squid spawns.
- The entity data manager now uses a Int2ObjectOpenHashMap for entries which has a smaller memory footprint and is significantly faster.
- The game will no longer attempt to add null block info to a crash report.
- Region files now have their headers loaded entirely instead of the previous method of in chunks which hurt performance instead of helping it.
- Instead of closing every region in the region file cache when it reaches 256 regions, the game will close the least accessed region.
- The chunk provider caches the last accessed chunk, which can make a big difference in chunk loading and world generation as the game accesses the same chunks multiple times in a row.
- getCanSpawnHere in EntityWaterMob has been corrected and actually checks if the entity can spawn in the given area instead of just returning true.
- Enchantments on items are sorted by their IDs to fix issues with the game thinking items that are the exact same are different.
- The chunk loader's saving has been greatly improved, removing a random 10ms sleep call and using a queue instead of a hash map for proper queueing and popping instead of throwing iterators everywhere. This fixes slowdowns when many chunks are queued to be saved. If you toggle enableIoThreadSleep on, the thread will only sleep for 2ms between instead of the 10ms from before, but will use more memory again.

## Incompatibilities
- The improved tick loop is not compatible with the `mixin.bugfix.slow_tps_catchup` option from
  [VintageFix](https://www.curseforge.com/minecraft/mc-mods/vintagefix) as they both overwrite the same method.

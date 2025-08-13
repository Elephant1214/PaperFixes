# PaperFixes

A collection of bug and performance fixes from CraftBukkit, Spigot, and Paper in a Forge mod.\
This is a performance mod, and bugs and incompatibilities could occur. Please open a GitHub issue if you find any.

## Current Fixes and Features

<details>
<summary>Bugfixes</summary>
  <ul>
    <li><code>avoidItemMergeForFullStacks</code> - Skips merging checks for full item stacks before bounding box check.</li>
    <li><code>clearPacketQueue</code> - Clears player packet queue on disconnect to prevent memory leaks.</li>
    <li><code>explosionsIgnoreDeadEntities</code> - Excludes dead entities and untargetable players from explosions.</li>
    <li><code>fixMc54738</code> (<a href="https://bugs.mojang.com/browse/MC/issues/MC-54738">MC-54738</a>) - Limits biome weight variable to fix terrain spikes.</li>
    <li><code>fixMc80966</code> (<a href="https://bugs.mojang.com/browse/MC/issues/MC-80966">MC-80966</a>) - Ensures empty subchunks send data to clients.</li>
    <li><code>fixMc98153</code> (<a href="https://bugs.mojang.com/browse/MC/issues/MC-98153">MC-98153</a>) - Prevents rubber-banding and suffocation in nether portals.</li>
    <li><code>fixMc133373</code> (<a href="https://bugs.mojang.com/browse/MC/issues/MC-133373">MC-133373</a>) - Resets NaN attribute values.</li>
    <li><code>fixShulkerDispenseCrash</code> - Prevents crash when placing non-empty shulker box via dispenser at height limit.</li>
    <li><code>fixShulkerDupe</code> - Fixes duplication from shulker box tile entity not being removed.</li>
    <li><code>fixWaterMobSpawnCheck</code> - Ensures water mobs spawn inside water with space.</li>
    <li><code>dontOffloadBeaconColorUpdate</code> - Avoids unnecessary thread hopping for beacon updates.</li>
    <li><code>handleNullTileCrashes</code> - Logs null tile entities instead of causing more problems.</li>
    <li><code>removeInvalidMobSpawners</code> - Removes broken mob spawners that don't exist.</li>
    <li><code>sortEnchantments</code> - Sorts enchantments by ID to ensure identical items are treated the same.</li>
  </ul>
</details>

<details>
<summary>Client</summary>
<ul>
  <li>
    <code>cacheLastChunk</code> - Because Minecraft accesses the same chunk from the chunk provider multiple times in one frame, we can cache the last one accessed. This doesn’t usually have a big effect on the client, but flying around and loading chunks can be a little faster when many operations are done per chunk.
  </li>
  <li>
    <code>fastWorldBorder</code> - Disabled on the client (including the internal server) by default because animations for border movement don’t work correctly, causing the border to snap to the end position when the countdown ends. If you don’t care about this, you can enable world border caching for the client and internal server.
  </li>
</ul>
</details>

<details>
<summary>Features</summary>
<ul>
  <li>
    <code>spawnChunkGamerule</code> - Enables the <code>spawnChunkRadius</code> gamerule.  
    Lets you define a spawn chunk radius for your world or server, similar to modern Minecraft, but with a few differences. In Minecraft 1.12, chunk tickets didn’t exist, so spawn chunks are a fixed 16×16 section always loaded and ticked. Entities inside are only processed in a smaller 12×12 area, because entity AI requires a 5×5 loaded area. PaperFixes uses a consistent diameter by centering on the chunk containing spawn, unlike vanilla which can make it 17×17 if spawn is centered in a chunk.  
    Setting this gamerule to <code>0</code> completely disables spawn chunks.
  </li>
  <li>
    <code>spawnChunkRadius</code> - Sets the default spawn chunk radius for the above feature. Applies only to new worlds or worlds not yet run with PaperFixes 2.0.0-beta.1 or newer. Values can range from 0 to 32, where 0 disables spawn chunks entirely and 32 gives a 64-chunk diameter.
  </li>
  <li>
    <code>improvedTickLoop</code> - Measures time in nanoseconds instead of milliseconds for better accuracy, uses <code>LockSupport.parkNanos</code> for precise sleeps, and adapts sleep time depending on tick length. Runs ticks as fast as possible when the server is more than 2.5 seconds behind, spinning instead of sleeping near the start of the next tick to avoid oversleeping.
  </li>
  <li>
    <code>tickLoopSpinTime</code> - Controls how long (in nanoseconds) the server tick loop will spin instead of sleeping. Also affects when the loop stops running scheduled tasks during what would normally be sleep time.
  </li>
  <li>
    <code>runTasksDuringSleep</code> - Allows the tick loop to run scheduled tasks instead of idling during sleep periods.
  </li>
  <li>
    <code>fastWorldBorder</code> - Caches the corners of the world border to make calculations faster instead of recomputing them each time.
  </li>
</ul>
</details>

<details>
<summary>Performance</summary>
<ul>
  <li>
    <code>ioThreadSleep</code> - Controls how long the world save thread sleeps. By default in PaperFixes, it doesn’t sleep at all to avoid rare memory and saving issues. If enabled, it sleeps for 2 ms instead of the usual 10 ms. Best left disabled unless you have a specific reason.
  </li>
  <li>
    <code>cacheBlockDensities</code> - Caches block density values so explosions don’t need to re-check the same block data repeatedly.
  </li>
  <li>
    <code>cacheLastChunk</code> - Caches the last chunk accessed from the chunk provider within a tick, avoiding repeated lookups.
  </li>
  <li>
    <code>compactLut</code> - Uses a smaller, faster lookup table for sine and cosine calculations. Thanks to jellysquid3 and ruViolence.
  </li>
  <li>
    <code>fastChests</code> - Runs chest open/close animations only when needed, instead of every tick.
  </li>
  <li>
    <code>smartRegionRead</code> - Reads the entire region file header at once for faster world loading and fewer potential issues.
  </li>
  <li>
    <code>fastEntityDataMap</code> - Stores and looks up entity data with a faster hash map.
  </li>
  <li>
    <code>optimizePathfinding</code> - Improves pathfinding by skipping repeated or impossible paths.
  </li>
  <li>
    <code>optimizedTaskQueue</code> - Uses a faster queue implementation for scheduled tasks.
  </li>
  <li>
    <code>pathingChunkCache</code> - Tracks the chunk an entity is moving through with a quick 1D cache. Thanks to jellysquid3 and ruViolence.
  </li>
  <li>
    <code>pathNodeCache</code> - Saves pathing data so the same blocks aren’t recalculated repeatedly. Thanks to jellysquid3 and ruViolence.
  </li>
  <li>
    <code>queueChunkSaving</code> - Saves chunks gradually over time instead of all at once, reducing server pause time during saves.
  </li>
  <li>
    <code>trimRegionCache</code> - Unloads only the least-used regions instead of all at once, preventing immediate reloading of active regions.
  </li>
  <li>
    <code>sharedRandomForEntities</code> - Uses a shared random number generator for all entities rather than creating a new one for each.
  </li>
</ul>
</details>

## Known Incompatibilities

- The improved tick loop is not compatible with the `mixin.bugfix.slow_tps_catchup` option from
  [VintageFix](https://github.com/embeddedt/VintageFix) (removed as of
  commit [ecbc3e1](https://github.com/embeddedt/VintageFix/commit/ecbc3e193c7fc9bee85577fa5e9f362c6249d82a)) and the
  tick loop changes from [Forged Carpet](https://github.com/DeadlyMC/forged-carpet) and likely from any other mod.
- Bad core mods can cause re-entry errors and crash the game as soon as it starts. This is not PaperFixes' fault and is
  unavoidable because these mods are loading game classes too early. The only solution is to replace them with a fork
  that fixes the problems.\
  An incomplete list of problematic core mods and solutions:
    - [Quark](https://modrinth.com/mod/quark): Replace
      with [Quark: RoTN Edition](https://www.curseforge.com/minecraft/mc-mods/quark-rotn-edition) (Thanks to
      sahih_international for finding this)

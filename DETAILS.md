# Bugfixes
## `avoidItemMergeForFullStacks`
Entities with full item stacks will try to merge with other item stacks even if they are full, and don't check if
they're full until after the (expensive) bounding box check. This simply skips to item stack merge check
altogether if the item stack is already full.
## `clearPacketQueue`
Minecraft 1.12 does not clear a player's packet queue when they disconnect, which causes a memory leak and can slowly
build up with many players leaving and rejoining. This ensures that a player's packet queue DOES get cleared after they
leave the server.
## `explosionsIgnoreDeadEntities`
By default, explosions do not filter out entities that are already dead. This option uses a different built-in method
for finding affected entities which allows entities that cannot be targeted
(i.e., players in spectator and creative mod) and dead entities to be excluded.
## `fixMc54738` ([MC-54738](https://bugs.mojang.com/browse/MC/issues/MC-54738))
Fixes different biome weights creating spikes of terrain the world by limiting a world generation variable to
-1.8. This is more of a bandage fix for the spikes that appear, I might get to actually fixing the world generation
problem that causes it some day.
## `fixMc80966` ([MC-80966](https://bugs.mojang.com/browse/MC/issues/MC-80966))
Fixes empty subchunks not sending data to clients when they are empty. The lack of data causes the lighting in the
subchunks to simply not exist on the client even if the subchunk is lit on the server.
## `fixMc98153` ([MC-98153](https://bugs.mojang.com/browse/MC/issues/MC-98153))
When using nether portals, players are rubber-banded to their source location in the target dimension by the anti-cheat,
then teleported to the actual portal in the target dimensions. This can make players suffocate temporarily or make them
catch on fire. This fixes the game trying to correct the player's location by updating their tracked position after the
teleport packet is sent.
## `fixMc133373` ([MC-133373](https://bugs.mojang.com/browse/MC/issues/MC-133373))
Checks for and resets NaN (Not-a-Number) values in attributes, which can cause, among other issues, players to become
stuck in a perpetual "dead but alive" state.
## `fixShulkerDispenseCrash`
Placing a dispenser at the top of the world facing up and putting a shulker box with any amount of items inside of it
(cannot be empty), then attempting to place the shulker box by activating that dispenser will cause the server to
instantly crash. This fixes the shulker dispense logic and stops updates from continuing if the shulker fails to place,
fixing the crash and putting the shulker back into the dispenser's inventory.
## `fixShulkerDupe`
Fixes shulker-based duplication glitches that are caused by Mojang removing shulker box block but not the tile entity.
## `fixWaterMobSpawnCheck`
Fixes the spawn check for water mobs since the vanilla "check" doesn't actually check anything. The new check
essentially ensures that a water mob actually has room to spawn and is actually spawning inside of water. If you are
experiencing spawning problems with water mobs from other mods, this fix is likely the culprit, and you should disable
it.
## `dontOffloadBeaconColorUpdate`
Prevents beacon color updates from being moved to the game's HTTP thread for calculations, then back to the main thread
as a scheduled task for updating the actual block data. This doesn't do anything to help game performance and wastes
time on needless scheduling
## `handleNullTileCrashes`
Tile entities can occasionally be null (or non-existent) when the game crashes. The game tries to forward this to the
crash report handler and can cause even more crashes. Instead of that, this fix logs the problem and skips passing any
null tile entities to the crash report handler.
## `removeInvalidMobSpawners`
Sometimes, the game can produce mob spawners that don't actually exist or are broken. This simply removes them if that
ever happens.
## `sortEnchantments`
Enchantments can be finicky, and sometimes the game will see two identical items differently if the enchantments are in
a different order. This fix sorts enchantments by their IDs to fix the problem.
# Client
Toggles for client only bug fixes, features, and performance fixes.
## `cacheLastChunk`
Because Minecraft accesses the same chunk from the chunk provider multiple time in one frame, we can take advantage of
caching the last one that was accessed in a field. This usually doesn't do a ton on a client, but flying around and
loading chunks is a bit little faster when lots of operations are being done for each chunk.
## `fastWorldBorder`
The fast world border is disabled on the client (includes the internal server because of how the game works) by default
because the animations for border movement do not work correctly, and the border snaps to the end position as the
countdown ends. If you don't care about this, you can enable world border caching for the client and internal server
using this.
# Features
## `spawnChunkGamerule`
Enables the `spawnChunkRadius` gamerule.\
This feature allows you to define a spawn chunk radius for your world or server, similar to how it works in modern
versions. However, PaperFixes' implementation is a bit different. In Minecraft 1.12, chunk tickets did not exist, so
spawn chunks are a fixed 16x16 section of chunks that are always fully loaded and ticked. Within this area, entities are
only processed in a smaller 12x12 region. This is because entities require a 5x5 area around them to be loaded in order
for their AI to tick. Also, unlike vanilla behavior, PaperFixes uses a consistent diameter for the spawn chunk area by
using the center of the chunk that spawn is in as the starting point for spawn. In vanilla, the spawn chunk area can be
an extra chunk wide (17x17) if the world spawn is at the center of a chunk.\
\
Setting the gamerule to `0` completely disables spawn chunks in a world.
## `spawnChunkRadius`
Sets the default spawn chunk radius for the above feature. This applies to ONLY new worlds and worlds that have not
previously been run with PaperFixes 2.0.0-beta.1 or newer. The range of values is 0 to 32, where 0 is disables spawn
chunks entirely, and 32 would give you a diameter of 64.
## `improvedTickLoop`
Enables the improved tick loop. This measures time in nanoseconds instead of milliseconds for better accuracy, and uses
LockSupport.parkNanos for more accurate sleep times. It also computes the sleep time depending on the amount of time
that a tick took and run ticks as fast as possible skipping sleep entirely when the server falls more than 2.5 seconds
behind. The tick loop also spins instead of sleeping towards the begging of the next tick to avoid oversleeping and
account for the undersleeping caused by parkNanos.
## `tickLoopSpinTime`
Modifies the amount of time that the server tick loop will spin (in nanoseconds) instead of sleeping. This is also
modifies when the loop will stop running scheduled tasks when it would normally be sleeping.
## `runTasksDuringSleep`
Allows the tick loop to run scheduled tasks instead of doing nothing sleeping.
## `fastWorldBorder`
Caches the corners of the world border to make calculations faster, instead of recomputing the corners every time.
# Performance
## `ioThreadSleep`
Controls how long the world save thread sleeps. By default, with PaperFixes, it doesn't sleep at all to avoid rare
memory and saving problems. If enabled, it sleeps for 2ms instead of the usual 10ms.\
It's best to keep this disabled unless you know what you're doing or were told to enable it.
## `cacheBlockDensities`
Caches the density of blocks so that the same block information doesn't have to be accessed when something explodes.
## `cacheLastChunk`
Because Minecraft accesses the same chunk from the chunk provider multiple time in one tick, we can take advantage of
caching the last one that was accessed in a field.
## `compactLut`
Uses a quicker, smaller table to calculate sine and cosine values. Thanks to jellysquid3 and ruViolence for this.
## `fastChests`
Runs chest open and close animations only when needed to save processing time, instead of checking every game tick.
## `smartRegionRead`
Reads the whole region header at once, speeding up world loading and making problems less likely to happen.
## `fastEntityDataMap`
Uses a faster hash map to store and look up entity data.
## `optimizePathfinding`
Speeds up pathfinding by ignoring repeated or impossible paths.
## `optimizedTaskQueue`
Uses a quicker queue to handle scheduled tasks.
## `pathingChunkCache`
Uses a faster 1D cache to keep track of the chunk an entity is moving through. Thanks to jellysquid3 and ruViolence for
this.
## `pathNodeCache`
Saves pathing info so that the game doesn't recalculate the same blocks over and over again. Thanks to jellysquid3 and
ruViolence for this.
## `queueChunkSaving`
Queues chunks to be saved over time instead of trying to save all of them at once. This reduces the amount of time the
server pause to save the game.
## `trimRegionCache`
Unloads only the least used regions instead of all of them so that the server doesn't have to immediately load half the
regions that were in use again.
## `sharedRandomForEntities`
Uses a shared random number generator for all entities instead of making a new one for each entity.

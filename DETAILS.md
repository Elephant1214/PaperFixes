# Features (WIP)
## `enableSpawnChunkGamerule`
Values: [0, 32]\
Enables the `spawnChunkRadius` gamerule.\
This feature allows you to change the spawn chunk radius to your liking using a gamerule so that it can be set in each world.\
Spawn chunks can be disabled entirely by setting the value of the gamerule to `0`.\
If you would like to have similar behavior to newer versions of the game, setting the gamerule to `9` will make the spawn chunk area 19x19.
### Technical Explanation
In vanilla 1.12, "spawn chunks" are made up of a 16x16 chunk area around spawn in the overworld which are constantly ticked. They are loaded and ticked whether a player is present or not.\
Entities only function within a 12x12 area of spawn chunks because they require a 5x5 area of ticked chunks to be considered active.\
\
This option also affects how spawn chunks work.
Normally in 1.12, the game checks if a chunk contains any of 128 blocks (eight chunks) from the spawn point.\
This can lead to the area that spawn chunks cover being 17x17 instead of 16x16 if your spawn point happens to be in the center of a chunk.\
Instead of being inconsistent, PaperFixes treats the center of the chunk that spawn is in as the center of the spawn chunk area.
Thus, the spawn chunk area is always 17x17 chunks if the radius is set to `8`.
## `defaultSpawnChunkRadius`
This option defines the default radius for the `spawnChunkRadius` gamerule.
This value is applied to all worlds that have not already been created or have not run with PaperFixes previously.
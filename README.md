# PaperFixes
A collection of bug and performance fixes from CraftBukkit, Spigot, and Paper in a Forge mod.
## Current Fixes
### Server
- Chest open and close sounds are no longer processed in the tile entity tick loop
### Common (Integrated & Dedicated Server)
- Invalid mob spawners are removed when detected
- Explosions no longer process dead entities
- Block density is cached so that expensive lookup operations aren't done every time there's an explosion
- [MC-80966](https://bugs.mojang.com/browse/MC-80966)
- Outbound packet queues are cleared when a player disconnects
- [MC-133373](https://bugs.mojang.com/browse/MC-133373)
- [MC-98153](https://bugs.mojang.com/browse/MC-98153)
- Handling for null tile entities that could exist when a world is having its entities updated
- The ability to toggle [spawn chunks](https://minecraft.fandom.com/wiki/Spawn_chunk) if they're not being used. Read below to find out why you might not want them to be enabled.
    - Spawn chunks are made up of a 25x25 chunk area around spawn that is always loaded by the game.
    - A smaller 19x19 chunk area is constantly fully ticked, except [random ticks](https://minecraft.fandom.com/wiki/Tick#Random_tick), as if a player were in it. This is the equivalent of three and a half players worth of ticked chunks if you're using the default ten chunk (10x10 area) view distance.
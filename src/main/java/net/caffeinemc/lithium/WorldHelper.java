package net.caffeinemc.lithium;

public final class WorldHelper {
    public static boolean areNeighborsWithinSameChunk(int x, int y, int z) {
        int localX = x & 15;
        int localY = y & 15;
        int localZ = z & 15;

        return localX > 0 && localY > 0 && localZ > 0 && localX < 15 && localY < 15 && localZ < 15;
    }
}

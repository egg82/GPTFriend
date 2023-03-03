package me.egg82.gpt.utils;

import org.bukkit.ChunkSnapshot;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Checksum;

public class ChunkUtil {
    private ChunkUtil() { }

    public static @NotNull Location getChunkBlockLocation(@NotNull Location chunkLocation, int listIndex) {
        int min = chunkLocation.getWorld().getMinHeight();
        int chunkBlockX = (chunkLocation.getBlockX() >> 4) << 4;
        int chunkBlockZ = (chunkLocation.getBlockZ() >> 4) << 4;
        int startZ = listIndex % 2;
        int startX = listIndex / 2;
        int startY = listIndex / 4;
        return new Location(chunkLocation.getWorld(), chunkBlockX + startX, min + startY, chunkBlockZ + startZ);
    }

    public static @NotNull List<@NotNull Location> getBlockLocations(@NotNull Location chunkLocation, int listIndex) {
        int min = chunkLocation.getWorld().getMinHeight();
        int chunkBlockX = (chunkLocation.getBlockX() >> 4) << 4;
        int chunkBlockZ = (chunkLocation.getBlockZ() >> 4) << 4;
        int startZ = listIndex % 2;
        int startX = listIndex / 2;
        int startY = listIndex / 4;

        List<Location> retVal = new ArrayList<>();
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                for (int z = 0; z < 8; z++) {
                    retVal.add(new Location(chunkLocation.getWorld(), chunkBlockX + startX + x, min + startY + y, chunkBlockZ + startZ + z));
                }
            }
        }

        return retVal;
    }

    private static long checksumBlock(@NotNull Checksum checksum, @NotNull ChunkSnapshot snapshot, int translateX, int translateY, int translateZ, int yMin) {
        checksum.reset();

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                for (int z = 0; z < 8; z++) {
                    checksum.update(
                            snapshot.getBlockData(
                                    translateX * 8 + x,
                                    translateY * 8 + yMin + y, // Shift y
                                    translateZ * 8 + z
                            ).getAsString(true).getBytes(StandardCharsets.UTF_8)
                    );
                }
            }
        }

        return checksum.getValue();
    }
}

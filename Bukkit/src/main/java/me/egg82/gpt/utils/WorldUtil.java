package me.egg82.gpt.utils;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WorldUtil {
    private WorldUtil() { }

    public static Block decodeBlockPos(World world, long l) {
        long x = (l >> 38);
        long y = l << 52 >> 52;
        long z = l << 26 >> 38;
        return world.getBlockAt((int) x, (int) y, (int) z);
    }

    public static @Nullable World getWorld(byte index) {
        List<World> worlds = Bukkit.getWorlds();
        return index < worlds.size() ? worlds.get(index) : null;
    }

    public static byte getWorldIndex(@NotNull World world) {
        List<World> worlds = Bukkit.getWorlds();
        int size = worlds.size();
        for (int i = 0; i < size; i++) {
            World w = worlds.get(i);
            if (world.equals(w)) {
                return (byte) i;
            }
        }
        return -1;
    }
}

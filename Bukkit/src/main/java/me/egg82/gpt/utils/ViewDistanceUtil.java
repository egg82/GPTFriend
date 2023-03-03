package me.egg82.gpt.utils;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ViewDistanceUtil {
    private static boolean hasViewdistanceMethods;
    private static boolean initialized;

    public static int get(@NotNull Player player) {
        if (!initialized) {
            try {
                player.getViewDistance();
                hasViewdistanceMethods = true;
            } catch (Exception e) {
                hasViewdistanceMethods = false;
            }
            initialized = true;
        }
        return hasViewdistanceMethods ? player.getViewDistance() : player.getWorld().getViewDistance();
    }

    public static boolean hasViewdistanceMethods() {
        return hasViewdistanceMethods;
    }
}

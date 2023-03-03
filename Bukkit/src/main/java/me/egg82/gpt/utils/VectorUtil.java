package me.egg82.gpt.utils;

import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class VectorUtil {
    private VectorUtil() { }

    public static boolean isFinite(@NotNull Vector vec) {
        try {
            NumberConversions.checkFinite(vec.getX(), "x not finite");
            NumberConversions.checkFinite(vec.getY(), "y not finite");
            NumberConversions.checkFinite(vec.getZ(), "z not finite");
        } catch (IllegalArgumentException ignored) {
            return false;
        }

        return true;
    }
}

package me.egg82.gpt.utils;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class LocationUtil {
    private static final double MOVE_THRESHOLD_SQUARED = 8 * 8;

    private LocationUtil() { }

    public static boolean isTeleport(@NotNull Location oldLocation, @NotNull Location newLocation) {
        return !newLocation.getWorld().equals(oldLocation.getWorld()) || newLocation.distanceSquared(oldLocation) > MOVE_THRESHOLD_SQUARED;
    }

    public static boolean isInRange(@NotNull Location location, @NotNull Location from, int viewDistance) {
        return location.getWorld().equals(from.getWorld())
                && distanceSquared(location.getX(), location.getZ(), from.getX(), from.getZ()) <= getViewDistanceBlocksSquared(viewDistance);
    }

    public static boolean isInRange(@NotNull Location location, @NotNull Location from, double distance) {
        return location.getWorld().equals(from.getWorld())
                && distanceSquared(location.getX(), location.getZ(), from.getX(), from.getZ()) <= distance * distance;
    }

    public static int getViewDistanceBlocksSquared(int viewDistance) {
        int blockDistance = viewDistance << 4;
        return blockDistance * blockDistance;
    }

    public static double distanceSquared(@NotNull Location location, @NotNull Location from) {
        return location.getWorld().equals(from.getWorld()) ? distanceSquared(
                location.getX(),
                location.getZ(),
                from.getX(),
                from.getZ()
        ) : -1.0d;
    }

    public static double distanceSquared(double x1, double z1, double x2, double z2) {
        double xDist = x2 - x1;
        double zDist = z2 - z1;
        return xDist * xDist + zDist * zDist;
    }

    public static @NotNull Location getRandomPointAround(@NotNull Location loc, double radius, boolean includeY) {
        double angle = Math.random() * Math.PI * 2.0d;
        double sin = Math.sin(angle);
        return new Location(loc.getWorld(), loc.getX() + radius * Math.cos(angle), (includeY) ? loc.getY() + radius * sin * sin : loc.getY(), loc.getZ() + radius * sin);
    }

    public static @NotNull Location getLocationInFront(@NotNull Location loc, double distance, boolean includeY) {
        double angle = loc.getYaw();

        angle += 90.0d;

        while (angle < 0.0d) {
            angle += 360.0d;
        }
        while (angle > 360.0d) {
            angle -= 360.0d;
        }

        angle = angle * Math.PI / 180.0d;
        double sin = Math.sin(angle);

        return new Location(loc.getWorld(), loc.getX() + distance * Math.cos(angle), (includeY) ? loc.getY() + distance * sin * sin : loc.getY(), loc.getZ() + distance * sin);
    }
}

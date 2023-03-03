package me.egg82.gpt.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BlockUtil {
    private BlockUtil() { }

    public static boolean isBlockEqual(@NotNull Location one, @NotNull Location two) {
        return
                one.getBlockX() == two.getBlockX()
                        && one.getBlockY() == two.getBlockY()
                        && one.getBlockZ() == two.getBlockZ();
    }

    public static boolean canIgnite(@NotNull Material type) {
        return !type.isSolid() && !type.name().contains("WATER") && !type.name().contains("LAVA");
    }

    public static @NotNull List<@NotNull Block> getBlocks(@NotNull Location center, int xRadius, int yRadius, int zRadius) {
        int minX = center.getBlockX() - xRadius;
        int maxX = center.getBlockX() + xRadius;
        int minY = center.getBlockY() - yRadius;
        int maxY = center.getBlockY() + yRadius;
        int minZ = center.getBlockZ() - zRadius;
        int maxZ = center.getBlockZ() + zRadius;

        Location currentLocation = new Location(center.getWorld(), 0.0d, 0.0d, 0.0d);
        List<Block> blocks = new ArrayList<>();

        for (int x = minX; x <= maxX; x++) {
            currentLocation.setX(x);
            for (int z = minZ; z <= maxZ; z++) {
                currentLocation.setZ(z);
                for (int y = minY; y <= maxY; y++) {
                    currentLocation.setY(y);
                    blocks.add(currentLocation.getBlock());
                }
            }
        }

        return blocks;
    }

    public static @NotNull Block getHighestSolidBlock(@NotNull Location l) {
        // We don't want to modify the original Location
        l = l.clone();
        // getType is a bit expensive
        Material type = l.getBlock().getType();

        if (!type.isSolid()) {
            // The block isn't solid, so we scan downwards to find the last non-solid block
            // Stop at 0 so we don't get stuck in an infinite loop
            while (l.getY() > 0 && !type.isSolid()) {
                // Apparently adding negatives is faster than subtracting (citation needed)
                l.add(0.0d, -1.0d, 0.0d);
                type = l.getBlock().getType();
            }
            // We don't care if 0 is the "highest" solid block because technically that's correct
            return l.getBlock();
        }

        // The block is solid, so we need to scan upwards to find the first non-solid block
        while (l.getY() < l.getWorld().getMaxHeight() && type.isSolid()) {
            l.add(0.0d, 1.0d, 0.0d);
            type = l.getBlock().getType();
        }
        // We don't care if maxHeight is the "highest" solid block because technically that's correct
        // If the block isn't solid, subtract 1 from it
        return type.isSolid() ? l.getBlock() : l.add(0.0d, -1.0d, 0.0d).getBlock();
    }

    public static @NotNull Set<@NotNull Block> getHalfCircleAround(@NotNull Location loc, double radius, int numPoints, int maxHeight) {
        Set<Block> retVal = new HashSet<>();
        double piSlice = Math.PI / numPoints;

        double angle = loc.getYaw();

        while (angle < 0.0d) {
            angle += 360.0d;
        }
        while (angle > 360.0d) {
            angle -= 360.0d;
        }

        angle = angle * Math.PI / 180.0d;

        for (int i = 0; i < numPoints; i++) {
            double newAngle = angle + piSlice * i;
            Block b = getHighestSolidBlock(new Location(loc.getWorld(), loc.getX() + radius * Math.cos(newAngle), loc.getY(), loc.getZ() + radius * Math.sin(newAngle)));
            if (Math.abs(b.getLocation().getY() - loc.getY()) <= maxHeight) {
                retVal.add(b);
            }
        }

        return retVal;
    }

    public static @NotNull BlockFace getFacingDirection(double yaw, boolean cardinal) {
        yaw += 180.0d;

        while (yaw < 0.0d) {
            yaw += 360.0d;
        }
        while (yaw > 360.0d) {
            yaw -= 360.0d;
        }

        if (cardinal) {
            if (yaw >= 315.0d || yaw < 45.0d) {
                return BlockFace.NORTH;
            } else if (yaw >= 45.0d && yaw < 135.0d) {
                return BlockFace.EAST;
            } else if (yaw >= 135.0d && yaw < 225.0d) {
                return BlockFace.SOUTH;
            }

            return BlockFace.WEST;
        }

        if (yaw >= 348.75d || yaw < 11.25d) {
            return BlockFace.NORTH;
        } else if (yaw >= 11.25d && yaw < 33.75d) {
            return BlockFace.NORTH_NORTH_EAST;
        } else if (yaw >= 33.75d && yaw < 56.25d) {
            return BlockFace.NORTH_EAST;
        } else if (yaw >= 56.25d && yaw < 78.75d) {
            return BlockFace.EAST_NORTH_EAST;
        } else if (yaw >= 78.75d && yaw < 101.25d) {
            return BlockFace.EAST;
        } else if (yaw >= 101.25d && yaw < 123.75d) {
            return BlockFace.EAST_SOUTH_EAST;
        } else if (yaw >= 123.75d && yaw < 146.25d) {
            return BlockFace.SOUTH_EAST;
        } else if (yaw >= 146.25d && yaw < 168.75d) {
            return BlockFace.SOUTH_SOUTH_EAST;
        } else if (yaw >= 168.75d && yaw < 191.25d) {
            return BlockFace.SOUTH;
        } else if (yaw >= 191.25d && yaw < 213.75d) {
            return BlockFace.SOUTH_SOUTH_WEST;
        } else if (yaw >= 213.75d && yaw < 236.25d) {
            return BlockFace.SOUTH_WEST;
        } else if (yaw >= 236.25d && yaw < 258.75d) {
            return BlockFace.WEST_SOUTH_WEST;
        } else if (yaw >= 258.75d && yaw < 281.25d) {
            return BlockFace.WEST;
        } else if (yaw >= 281.25d && yaw < 303.75d) {
            return BlockFace.WEST_NORTH_WEST;
        } else if (yaw >= 303.75d && yaw < 326.25d) {
            return BlockFace.NORTH_WEST;
        }

        return BlockFace.NORTH_NORTH_WEST;
    }
}

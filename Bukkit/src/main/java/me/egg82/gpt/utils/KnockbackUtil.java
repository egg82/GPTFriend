package me.egg82.gpt.utils;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class KnockbackUtil {
    private KnockbackUtil() { }

    private static final Random RANDOM = new Random();

    public static void knockback(@NotNull Location from, @NotNull Location location, @NotNull LivingEntity entity) {
        double velocityX = from.getX() - location.getX();
        double velocityZ;
        for (velocityZ = from.getZ() - location.getZ(); velocityX * velocityX + velocityZ * velocityZ < 1.0E-4D; velocityZ = (RANDOM.nextDouble() - RANDOM.nextDouble()) * 0.01D) {
            velocityX = (RANDOM.nextDouble() - RANDOM.nextDouble()) * 0.01D;
        }
        entity.setHurtDirection((float) (Math.atan2(velocityZ, velocityX) * 57.2957763671875D - location.getYaw()));
        entity.knockback(0.4f, velocityX, velocityZ);
    }
}

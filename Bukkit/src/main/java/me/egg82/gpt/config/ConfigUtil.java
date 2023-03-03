package me.egg82.gpt.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;

public class ConfigUtil {
    private static ConfigurationNode config = null;
    private static CachedConfig cachedConfig = null;

    private ConfigUtil() { }

    public static void setConfiguration(@Nullable ConfigurationNode config, @Nullable CachedConfig cachedConfig) {
        ConfigUtil.config = config;
        ConfigUtil.cachedConfig = cachedConfig;
    }

    public static @NotNull ConfigurationNode getConfig() {
        ConfigurationNode c = config; // Thread-safe reference
        if (c == null) {
            throw new IllegalStateException("Config could not be fetched.");
        }
        return c;
    }

    public static @NotNull CachedConfig getCachedConfig() {
        CachedConfig c = cachedConfig; // Thread-safe reference
        if (c == null) {
            throw new IllegalStateException("Cached config could not be fetched.");
        }
        return c;
    }

    public static boolean getSilentOrFalse() {
        ConfigurationNode c = config; // Thread-safe reference
        return c != null && c.node("silent").getBoolean(false);
    }

    public static boolean getDebugOrFalse() {
        ConfigurationNode c = config; // Thread-safe reference
        return c != null && c.node("debug").getBoolean(false);
    }
}

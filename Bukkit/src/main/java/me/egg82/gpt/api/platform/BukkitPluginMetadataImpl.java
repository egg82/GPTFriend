package me.egg82.gpt.api.platform;

import me.egg82.gpt.platform.AbstractPluginMetadata;
import org.jetbrains.annotations.NotNull;

public class BukkitPluginMetadataImpl extends AbstractPluginMetadata {
    private final String pluginVersion;

    public BukkitPluginMetadataImpl(@NotNull String pluginVersion) {
        this.pluginVersion = pluginVersion;
    }

    @Override
    public @NotNull String getVersion() { return pluginVersion; }
}

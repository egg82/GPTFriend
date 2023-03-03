package me.egg82.gpt.core;

import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import io.papermc.paper.plugin.configuration.PluginMeta;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;

public class BukkitPluginProviderContext implements PluginProviderContext {
    private final PluginDescriptionFile description;
    private final Path dataDirectory;
    private final ComponentLogger logger;

    public BukkitPluginProviderContext(@NotNull PluginDescriptionFile description, @NotNull File dataDirectory, @NotNull ComponentLogger logger) {
        this.description = description;
        this.dataDirectory = dataDirectory.toPath();
        this.logger = logger;
    }

    @Override
    public @NotNull PluginMeta getConfiguration() {
        return description;
    }

    @Override
    public @NotNull Path getDataDirectory() {
        return dataDirectory;
    }

    @Override
    public @NotNull ComponentLogger getLogger() {
        return logger;
    }
}

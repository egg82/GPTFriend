package me.egg82.gpt.platform;

import me.egg82.gpt.api.model.platform.PluginMetadata;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractPluginMetadata implements PluginMetadata {
    private static final String API_VERSION = "${api.version}";

    @Override
    public final @NotNull String getApiVersion() { return API_VERSION; }
}

package me.egg82.gpt.api.platform;

import me.egg82.gpt.api.model.platform.PluginMetadata;
import org.jetbrains.annotations.NotNull;

public class NullPluginMetadataImpl implements PluginMetadata {
    public NullPluginMetadataImpl() { }

    @Override
    public @NotNull String getVersion() { return "0.0.0"; }

    @Override
    public @NotNull String getApiVersion() { return "0.0.0"; }

    @Override
    public String toString() { return "NullPluginMetadataImpl{}"; }
}

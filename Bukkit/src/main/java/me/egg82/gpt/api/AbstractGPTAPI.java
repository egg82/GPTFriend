package me.egg82.gpt.api;

import me.egg82.gpt.api.model.platform.PluginMetadata;
import me.egg82.gpt.api.platform.NullPluginMetadataImpl;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractGPTAPI implements GPTAPI {
    protected PluginMetadata pluginMetadata = new NullPluginMetadataImpl();

    protected AbstractGPTAPI() { }

    @Override
    public @NotNull PluginMetadata getPluginMetadata() {
        return pluginMetadata;
    }
}

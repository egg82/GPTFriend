package me.egg82.gpt.api;

import me.egg82.gpt.api.model.map.MapManager;
import me.egg82.gpt.api.model.map.NullMapManagerImpl;
import me.egg82.gpt.api.model.platform.PluginMetadata;
import me.egg82.gpt.api.platform.NullPluginMetadataImpl;
import org.jetbrains.annotations.NotNull;

public class NullGPTAPIImpl implements GPTAPI {
    private MapManager mapManager = new NullMapManagerImpl();
    private PluginMetadata pluginMetadata = new NullPluginMetadataImpl();

    public NullGPTAPIImpl() { }

    public void setMapManager(@NotNull MapManager mapManager) { this.mapManager = mapManager; }

    public void setPluginMetadata(@NotNull PluginMetadata pluginMetadata) { this.pluginMetadata = pluginMetadata; }

    @Override
    public @NotNull MapManager getMapManager() { return mapManager; }

    @Override
    public @NotNull PluginMetadata getPluginMetadata() { return pluginMetadata; }

    @Override
    public String toString() {
        return "NullGPTAPIImpl{" +
                "mapManager=" + mapManager +
                ", pluginMetadata=" + pluginMetadata +
                '}';
    }
}

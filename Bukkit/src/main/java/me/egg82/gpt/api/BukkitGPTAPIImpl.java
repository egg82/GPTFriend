package me.egg82.gpt.api;

import me.egg82.gpt.api.model.map.MapManager;
import me.egg82.gpt.api.model.map.NullMapManagerImpl;
import me.egg82.gpt.api.model.platform.PluginMetadata;
import org.jetbrains.annotations.NotNull;

public class BukkitGPTAPIImpl extends AbstractGPTAPI {
    private MapManager mapManager = new NullMapManagerImpl();

    @Override
    public @NotNull MapManager getMapManager() {
        return mapManager;
    }

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private final BukkitGPTAPIImpl values = new BukkitGPTAPIImpl();

        private Builder() { }

        public @NotNull BukkitGPTAPIImpl.Builder mapManager(@NotNull MapManager value) {
            this.values.mapManager = value;
            return this;
        }

        public @NotNull BukkitGPTAPIImpl.Builder pluginMetadata(@NotNull PluginMetadata value) {
            this.values.pluginMetadata = value;
            return this;
        }

        public @NotNull BukkitGPTAPIImpl build() { return values; }
    }
}

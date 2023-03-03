package me.egg82.gpt.api.model.platform;

import org.jetbrains.annotations.NotNull;

/**
 * Provides information about the GPTFriend plugin.
 */
public interface PluginMetadata {
    /**
     * Gets the plugin version.
     *
     * @return the version of the plugin running on the platform
     */
    @NotNull String getVersion();

    /**
     * Gets the API version.
     *
     * @return the version of the API running on the platform
     */
    @NotNull String getApiVersion();
}

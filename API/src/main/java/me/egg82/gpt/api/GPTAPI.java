package me.egg82.gpt.api;

import me.egg82.gpt.api.model.map.MapManager;
import me.egg82.gpt.api.model.platform.PluginMetadata;
import org.jetbrains.annotations.NotNull;

/**
 * The GPTFriend API.
 *
 * <p>The API allows other plugins on the server to read and modify GPTFriend
 * data, change behaviour of the plugin, listen to certain events, and integrate
 * GPTFriend into other plugins and systems.</p>
 *
 * <p>This interface represents the base of the API package. All functions are
 * accessed via this interface.</p>
 *
 * <p>To start using the API, you need to obtain an instance of this interface.
 * These are registered by the GPTFriend plugin.</p>
 *
 * <p>An instance can be obtained from the static singleton accessor in
 * {@link GPTAPIProvider}.</p>
 *
 * <p>A good portion of this API was taken from LuckPerms, if not at least inspired by it.
 * License available <a href="https://github.com/lucko/LuckPerms/blob/master/LICENSE.txt">here</a></p>
 */
public interface GPTAPI {
    /**
     * Gets the {@link MapManager}, responsible for managing
     * map plugin integration.
     *
     * <p>This manager can be used to add, remove, and update
     * icons and layers on the hooked map.</p>
     *
     * @return the map manager
     */
    @NotNull MapManager getMapManager();

    /**
     * Gets the {@link PluginMetadata}, responsible for providing metadata about
     * the GPTFriend plugin currently running.
     *
     * @return the plugin metadata
     */
    @NotNull PluginMetadata getPluginMetadata();
}

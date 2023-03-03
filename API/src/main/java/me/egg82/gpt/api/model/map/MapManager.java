package me.egg82.gpt.api.model.map;

import me.egg82.gpt.api.model.player.PlayerSkin;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.image.BufferedImage;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/**
 * Represents the object responsible for managing map plugin interactions.
 *
 * <p>All blocking methods return {@link CompletableFuture}s, which will be
 * populated with the result once the data has been loaded/saved asynchronously.
 * Care should be taken when using such methods to ensure that the main server
 * thread is not blocked.</p>
 *
 * <p>Methods such as {@link CompletableFuture#get()} and equivalent should
 * <strong>not</strong> be called on the main server thread. If you need to use
 * the result of these operations on the main server thread, register a
 * callback using {@link CompletableFuture#thenAcceptAsync(Consumer, Executor)}.</p>
 */
public interface MapManager {
    /**
     * Creates a player skin with the texture base64 and an optional signature.
     *
     * @param base64 the base64 texture of the skin data
     * @param signature the signature of the base64 string, optional
     *
     * @return a {@link PlayerSkin} object
     */
    @NotNull PlayerSkin createSkin(@NotNull String base64, @Nullable String signature);

    /**
     * Adds an icon to the registry with the specified name and adjusts the icon's size if required
     *
     * @param name the icon name
     * @param image the icon image
     * @param size the new width &amp; height of the icon
     *
     * @return true if the icon was successfully registered, false if the icon name already exists
     */
    default boolean registerIcon(@NotNull String name, @NotNull BufferedImage image, int size) { return registerIcon(name, image, size, size); }

    /**
     * Adds an icon to the registry with the specified name
     *
     * @param name the icon name
     * @param image the icon image
     *
     * @return true if the icon was successfully registered, false if the icon name already exists
     */
    default boolean registerIcon(@NotNull String name, @NotNull BufferedImage image) { return registerIcon(name, image, image.getWidth(), image.getHeight()); }

    /**
     * Adds an icon to the registry with the specified name and adjusts the icon's width and height if required
     *
     * @param name the icon name
     * @param image the icon image
     * @param width the new width of the icon
     * @param height the new height of the icon
     *
     * @return true if the icon was successfully registered, false if the icon name already exists
     */
    boolean registerIcon(@NotNull String name, @NotNull BufferedImage image, int width, int height);

    /**
     * Removes an icon from the registry
     *
     * @param name the icon name
     *
     * @return true if the icon was successfully unregistered, false if the icon name does not exist
     */
    boolean unregisterIcon(@NotNull String name);

    /**
     * Returns a {@link BufferedImage} icon from the given name, or null if the icon name was not registered
     *
     * @param name the icon name
     *
     * @return a buffered image icon, or null if the icon name was not registered
     */
    @Nullable BufferedImage getIcon(@NotNull String name);

    /**
     * Returns a 16x16 {@link BufferedImage} icon representing the given {@link LivingEntity}
     *
     * <p>If the entity is a player, will return a BufferedImage of
     * the player's head. Otherwise, will return a BufferedImage
     * representation of the entity. If a representation could
     * not be found, returns an empty BufferedImage.</p>
     *
     * <p>This method caches player heads and entity representations,
     * but will create a new BufferedImage each time it is called.</p>
     *
     * @param entity the entity to iconify
     * @param effects a set of visual effects to apply to the icon, or null for none
     *
     * @return a {@link CompletableFuture} wrapping the resulting icon
     */
    default @NotNull CompletableFuture<@NotNull BufferedImage> createIcon(
            @NotNull LivingEntity entity,
            @Nullable Set<@NotNull MapEffect> effects
    ) { return createIcon(entity, 16, effects); }

    /**
     * Returns a {@link BufferedImage} icon representing the given {@link LivingEntity}
     *
     * <p>If the entity is a player, will return a BufferedImage of
     * the player's head. Otherwise, will return a BufferedImage
     * representation of the entity. If a representation could
     * not be found, returns an empty BufferedImage.</p>
     *
     * <p>This method caches player heads and entity representations,
     * but will create a new BufferedImage each time it is called.</p>
     *
     * @param entity the entity to iconify
     * @param size the size of the generated icon
     *
     * @return a {@link CompletableFuture} wrapping the resulting icon
     */
    default @NotNull CompletableFuture<@NotNull BufferedImage> createIcon(@NotNull LivingEntity entity, int size) {
        return createIcon(
                entity,
                size,
                null
        );
    }

    /**
     * Returns a {@link BufferedImage} icon representing the given {@link LivingEntity}
     *
     * <p>If the entity is a player, will return a BufferedImage of
     * the player's head. Otherwise, will return a BufferedImage
     * representation of the entity. If a representation could
     * not be found, returns an empty BufferedImage.</p>
     *
     * <p>This method caches player heads and entity representations,
     * but will create a new BufferedImage each time it is called.</p>
     *
     * @param entity the entity to iconify
     *
     * @return a {@link CompletableFuture} wrapping the resulting icon
     */
    default @NotNull CompletableFuture<@NotNull BufferedImage> createIcon(@NotNull LivingEntity entity) { return createIcon(entity, 16, null); }

    /**
     * Returns a {@link BufferedImage} icon representing the given {@link LivingEntity}
     *
     * <p>If the entity is a player, will return a BufferedImage of
     * the player's head. Otherwise, will return a BufferedImage
     * representation of the entity. If a representation could
     * not be found, returns an empty BufferedImage.</p>
     *
     * <p>This method caches player heads and entity representations,
     * but will create a new BufferedImage each time it is called.</p>
     *
     * @param entity the entity to iconify
     * @param size the size of the generated icon
     * @param effects a set of visual effects to apply to the icon, or null for none
     *
     * @return a {@link CompletableFuture} wrapping the resulting icon
     */
    @NotNull CompletableFuture<@NotNull BufferedImage> createIcon(@NotNull LivingEntity entity, int size, @Nullable Set<@NotNull MapEffect> effects);

    /**
     * Returns a unique icon name that can be used for registration
     *
     * @param entity the entity to iconify
     *
     * @return a unique string which describes the parameters passed in
     */
    default @NotNull String getUniqueIconName(@NotNull LivingEntity entity) { return getUniqueIconName(entity, 16, null); }

    /**
     * Returns a unique icon name that can be used for registration
     *
     * @param entity the entity to iconify
     * @param effects a set of visual effects to apply to the icon, or null for none
     *
     * @return a unique string which describes the parameters passed in
     */
    default @NotNull String getUniqueIconName(@NotNull LivingEntity entity, @Nullable Set<@NotNull MapEffect> effects) {
        return getUniqueIconName(
                entity,
                16,
                effects
        );
    }

    /**
     * Returns a unique icon name that can be used for registration
     *
     * @param entity the entity to iconify
     * @param size the size of the generated icon
     *
     * @return a unique string which describes the parameters passed in
     */
    default @NotNull String getUniqueIconName(@NotNull LivingEntity entity, int size) { return getUniqueIconName(entity, size, null); }

    /**
     * Returns a unique icon name that can be used for registration
     *
     * @param entity the entity to iconify
     * @param size the size of the generated icon
     * @param effects a set of visual effects to apply to the icon, or null for none
     *
     * @return a unique string which describes the parameters passed in
     */
    @NotNull String getUniqueIconName(@NotNull LivingEntity entity, int size, @Nullable Set<@NotNull MapEffect> effects);

    /**
     * Adds a toggleable layer to the map with the specified name
     *
     * @param name the layer name
     * @param visibleName the layer name, as is shown on the map provider's graphical interface
     * @param world the world this layer is attached to
     *
     * @return true if the layer was successfully registered, false if the layer name already exists
     */
    boolean registerLayer(@NotNull String name, @NotNull String visibleName, @NotNull World world);

    /**
     * Removes a layer from the map
     *
     * @param name the layer name
     *
     * @return true if the layer was successfully unregistered, false if the layer name does not exist
     */
    boolean unregisterLayer(@NotNull String name);

    /**
     * Returns the registered layer's attached world, or null if the layer name does not exist
     *
     * @param name the layer name
     *
     * @return a world, or null if the layer name was not registered
     */
    @Nullable World getLayerWorld(@NotNull String name);

    /**
     * Adds a marker to the map with the specified name, at the specified location
     *
     * @param name the marker name
     * @param iconName the icon name, as specified by {@link #registerIcon(String, BufferedImage, int, int)}
     * @param layerName the layer name, as specified by {@link #registerLayer(String, String, World)}
     * @param location the initial location of the marker
     *
     * @return true if the marker was successfully added, false if the marker name already exists
     */
    default boolean addMarker(@NotNull String name, @NotNull String iconName, @NotNull String layerName, @NotNull Location location) {
        return addMarker(
                name,
                iconName,
                layerName,
                location,
                null,
                null
        );
    }

    /**
     * Adds a marker to the map with the specified name, at the specified location, with the specified tooltip
     *
     * @param name the marker name
     * @param iconName the icon name, as specified by {@link #registerIcon(String, BufferedImage, int, int)}
     * @param layerName the layer name, as specified by {@link #registerLayer(String, String, World)}
     * @param location the initial location of the marker
     * @param hoverTooltip the hover tooltip of the marker on the map provider's graphical interface, or null if none
     * @param clickTooltip the click tooltip of the marker on the map provider's graphical interface, or null if none
     *
     * @return true if the marker was successfully added, false if the marker name already exists
     */
    boolean addMarker(
            @NotNull String name,
            @NotNull String iconName,
            @NotNull String layerName,
            @NotNull Location location,
            @Nullable String hoverTooltip,
            @Nullable String clickTooltip
    );

    /**
     * Removes a marker from the map
     *
     * @param name the marker name
     *
     * @return true if the marker was successfully removed, false if the marker name does not exist
     */
    boolean removeMarker(@NotNull String name);

    /**
     * Updates a marker on the map with a new location
     *
     * @param name the marker name
     * @param location the new location of the marker
     *
     * @return true if the marker location was set, false if the marker name does not exist
     */
    default boolean updateMarker(@NotNull String name, @NotNull Location location) { return updateMarker(name, null, location); }

    /**
     * Updates a marker on the map with a new layer and location
     *
     * @param name the marker name
     * @param layerName the new layer name, or null for the same layer
     * @param location the new location of the marker
     *
     * @return true if the marker location was set, false if the marker name does not exist
     */
    boolean updateMarker(@NotNull String name, @Nullable String layerName, @NotNull Location location);

    /**
     * Returns the marker's current location, or null if the marker name does not exist
     *
     * @param name the marker name
     *
     * @return a location, or null if the marker name does not exist
     */
    @Nullable Location getMarkerLocation(@NotNull String name);

    /**
     * Returns true if this manager is attached to a map plugin, false if not
     *
     * @return true if a valid map plugin is being used, false if not
     */
    boolean isValid();
}

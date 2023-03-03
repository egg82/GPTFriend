package me.egg82.gpt.hooks;

import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.image.BufferedImage;

public interface MapHandler {
    boolean registerIcon(@NotNull String name, @NotNull BufferedImage image);

    boolean unregisterIcon(@NotNull String name);

    @Nullable BufferedImage getIcon(@NotNull String name);

    boolean registerLayer(@NotNull String name, @NotNull String visibleName, @NotNull World world);

    boolean unregisterLayer(@NotNull String name);

    @Nullable World getLayerWorld(@NotNull String name);

    boolean addMarker(
            @NotNull String name,
            @NotNull String iconName,
            @NotNull String layerName,
            @NotNull Location location,
            @Nullable String hoverTooltip,
            @Nullable String clickTooltip
    );

    boolean removeMarker(@NotNull String name);

    boolean updateMarker(@NotNull String name, @Nullable String layerName, @NotNull Location location);

    @Nullable Location getMarkerLocation(@NotNull String name);
}

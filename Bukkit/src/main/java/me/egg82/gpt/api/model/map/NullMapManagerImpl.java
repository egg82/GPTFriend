package me.egg82.gpt.api.model.map;

import me.egg82.gpt.api.model.player.NullPlayerSkinImpl;
import me.egg82.gpt.api.model.player.PlayerSkin;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.image.BufferedImage;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class NullMapManagerImpl implements MapManager {
    public NullMapManagerImpl() { }

    @Override
    public @NotNull PlayerSkin createSkin(@NotNull String base64, @Nullable String signature) {
        return new NullPlayerSkinImpl();
    }

    @Override
    public boolean registerIcon(@NotNull String name, @NotNull BufferedImage image, int width, int height) { return false; }

    @Override
    public boolean unregisterIcon(@NotNull String name) { return false; }

    @Override
    public @Nullable BufferedImage getIcon(@NotNull String name) { return null; }

    @Override
    public @NotNull CompletableFuture<@NotNull BufferedImage> createIcon(
            @NotNull LivingEntity entity,
            int size,
            @Nullable Set<@NotNull MapEffect> effects
    ) { return CompletableFuture.completedFuture(new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB)); }

    @Override
    public @NotNull String getUniqueIconName(
            @NotNull LivingEntity entity,
            int size,
            @Nullable Set<@NotNull MapEffect> effects
    ) { return ""; }

    @Override
    public boolean registerLayer(@NotNull String name, @NotNull String visibleName, @NotNull World world) { return false; }

    @Override
    public boolean unregisterLayer(@NotNull String name) { return false; }

    @Override
    public @Nullable World getLayerWorld(@NotNull String name) { return null; }

    @Override
    public boolean addMarker(
            @NotNull String name,
            @NotNull String iconName,
            @NotNull String layerName,
            @NotNull Location location,
            @Nullable String hoverTooltip,
            @Nullable String clickTooltip
    ) { return false; }

    @Override
    public boolean removeMarker(@NotNull String name) { return false; }

    @Override
    public boolean updateMarker(@NotNull String name, @Nullable String layerName, @NotNull Location location) { return false; }

    @Override
    public @Nullable Location getMarkerLocation(@NotNull String name) { return null; }

    @Override
    public boolean isValid() { return false; }

    @Override
    public String toString() { return "NullMapManagerImpl{}"; }
}

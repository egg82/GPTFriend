package me.egg82.gpt.api.model.map;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import me.egg82.gpt.api.model.player.PlayerSkin;
import me.egg82.gpt.api.model.player.PlayerSkinImpl;
import me.egg82.gpt.config.ConfigUtil;
import me.egg82.gpt.hooks.MapHandler;
import me.egg82.gpt.hooks.PluginHook;
import me.egg82.gpt.hooks.PluginHooks;
import me.egg82.gpt.logging.GELFLogger;
import me.egg82.gpt.utils.HeadUtil;
import me.egg82.gpt.utils.SkinUtil;
import me.egg82.gpt.utils.TimeUtil;
import me.egg82.gpt.web.WebRequest;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class BukkitMapManagerImpl implements MapManager {
    private final Logger logger = new GELFLogger(LoggerFactory.getLogger(getClass()));

    private final LoadingCache<@NotNull String, @Nullable BufferedImage> images = Caffeine
            .newBuilder()
            .expireAfterAccess(1L, TimeUnit.HOURS)
            .build(this::getImage);

    public BukkitMapManagerImpl() { }

    @Override
    public @NotNull PlayerSkin createSkin(@NotNull String base64, @Nullable String signature) {
        return new PlayerSkinImpl(base64, signature);
    }

    @Override
    public boolean registerIcon(@NotNull String name, @NotNull BufferedImage image, int width, int height) {
        MapHandler mapHandler = getMapHandler();
        if (mapHandler == null) {
            return false;
        }
        return mapHandler.registerIcon(name, resize(image, width, height));
    }

    @Override
    public boolean unregisterIcon(@NotNull String name) {
        MapHandler mapHandler = getMapHandler();
        if (mapHandler == null) {
            return false;
        }
        return mapHandler.unregisterIcon(name);
    }

    @Override
    public @Nullable BufferedImage getIcon(@NotNull String name) {
        MapHandler mapHandler = getMapHandler();
        if (mapHandler == null) {
            return null;
        }
        return mapHandler.getIcon(name);
    }

    @Override
    public @NotNull CompletableFuture<@NotNull BufferedImage> createIcon(
            @NotNull LivingEntity entity,
            int size,
            @Nullable Set<@NotNull MapEffect> effects
    ) {
        return CompletableFuture.supplyAsync(() -> {
            int finalSize = size;

            String url;
            if (entity instanceof Player p) {
                url = SkinUtil.getSkin(p).getSkinUrl();
            } else {
                url = HeadUtil.getEntitySkin(entity).getSkinUrl();

                if (entity.getType() == EntityType.GIANT) {
                    finalSize *= 2;
                } else if (entity.getType() == EntityType.SLIME) {
                    double val = (double) (((Slime) entity).getSize() - 4) / 8.0d; // Largest is 256, where 1 2 and 4 spawn naturally - 8 is a large slime, so 16 should result in 2x size
                    finalSize *= 1 + val;
                } else if (entity.getType() == EntityType.MAGMA_CUBE) {
                    double val = (double) (((MagmaCube) entity).getSize() - 4) / 8.0d; // Largest is 256, where 1 2 and 4 spawn naturally - 8 is a large slime, so 16 should result in 2x size
                    finalSize *= 1 + val;
                }
            }

            BufferedImage base = images.get(url);
            if (base == null) {
                logger.error("Could not get image for skin URL {}", url);
                return createBlank(finalSize, finalSize);
            }
            return addEffects(resize(base, finalSize, finalSize), effects);
        });
    }

    @Override
    public @NotNull String getUniqueIconName(
            @NotNull LivingEntity entity,
            int size,
            @Nullable Set<@NotNull MapEffect> effects
    ) {
        if (effects == null || effects.isEmpty()) {
            return (entity instanceof Player ? entity.getUniqueId() : entity.getType().name()) + "." + size;
        }

        StringBuilder builder = new StringBuilder();
        if (entity.getType() == EntityType.PLAYER) {
            builder.append(entity.getUniqueId());
        } else {
            builder.append(entity.getType().name());
        }
        builder.append('.');
        List<MapEffect> e = new ArrayList<>(effects);
        Collections.sort(e); // Ensure consistent name
        for (MapEffect effect : e) {
            if (
                    (effect == MapEffect.GLOWING && ConfigUtil.getConfig().node("map", "effects", "glowing").getBoolean(true))
                            || (effect == MapEffect.INVISIBLE && ConfigUtil.getConfig().node("map", "effects", "invisibility").getBoolean(true))
                            || (effect == MapEffect.ON_FIRE && ConfigUtil.getConfig().node("map", "effects", "fire").getBoolean(true))
            ) {
                builder.append(effect.name());
                builder.append('-');
            }
            // We don't handle other effects in icons
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append('.');
        builder.append(size);

        return builder.toString();
    }

    @Override
    public boolean registerLayer(@NotNull String name, @NotNull String visibleName, @NotNull World world) {
        MapHandler mapHandler = getMapHandler();
        if (mapHandler == null) {
            return false;
        }
        return mapHandler.registerLayer(name, visibleName, world);
    }

    @Override
    public boolean unregisterLayer(@NotNull String name) {
        MapHandler mapHandler = getMapHandler();
        if (mapHandler == null) {
            return false;
        }
        return mapHandler.unregisterLayer(name);
    }

    @Override
    public @Nullable World getLayerWorld(@NotNull String name) {
        MapHandler mapHandler = getMapHandler();
        if (mapHandler == null) {
            return null;
        }
        return mapHandler.getLayerWorld(name);
    }

    @Override
    public boolean addMarker(
            @NotNull String name,
            @NotNull String iconName,
            @NotNull String layerName,
            @NotNull Location location,
            @Nullable String hoverTooltip,
            @Nullable String clickTooltip
    ) {
        MapHandler mapHandler = getMapHandler();
        if (mapHandler == null) {
            return false;
        }
        return mapHandler.addMarker(name, iconName, layerName, location, hoverTooltip, clickTooltip);
    }

    @Override
    public boolean removeMarker(@NotNull String name) {
        MapHandler mapHandler = getMapHandler();
        if (mapHandler == null) {
            return false;
        }
        return mapHandler.removeMarker(name);
    }

    @Override
    public boolean updateMarker(@NotNull String name, @Nullable String layerName, @NotNull Location location) {
        MapHandler mapHandler = getMapHandler();
        if (mapHandler == null) {
            return false;
        }
        return mapHandler.updateMarker(name, layerName, location.clone());
    }

    @Override
    public @Nullable Location getMarkerLocation(@NotNull String name) {
        MapHandler mapHandler = getMapHandler();
        if (mapHandler == null) {
            return null;
        }
        Location retVal = mapHandler.getMarkerLocation(name);
        return retVal != null ? retVal.clone() : null;
    }

    @Override
    public boolean isValid() { return getMapHandler() != null; }

    private @NotNull BufferedImage resize(@NotNull BufferedImage image, int width, int height) {
        if (image.getWidth() == width && image.getHeight() == height) {
            return image;
        }

        BufferedImage retVal = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = retVal.createGraphics(); // same as getGraphics, except returns a G2D
        g.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR
        ); // Best for upscaling **PIXEL ART**: https://stackoverflow.com/questions/24745147/java-resize-image-without-losing-quality
        g.drawImage(image, 0, 0, width, height, null);
        g.dispose();

        return retVal;
    }

    private @NotNull BufferedImage createBlank(int width, int height) { return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB); }

    private @NotNull BufferedImage addEffects(@NotNull BufferedImage image, @Nullable Set<@NotNull MapEffect> effects) {
        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage retVal = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = retVal.createGraphics(); // same as getGraphics, except returns a G2D
        if (effects != null && effects.contains(MapEffect.INVISIBLE) && ConfigUtil.getConfig().node("map", "effects", "invisibility").getBoolean(true)) {
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.55f));
        }
        g.drawImage(image, 0, 0, width, height, null);
        g.dispose();
        if (effects != null && effects.contains(MapEffect.ON_FIRE) && ConfigUtil.getConfig().node("map", "effects", "fire").getBoolean(true)) {
            Color tint = new Color(128, 0, 0);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    Color pixel = new Color(retVal.getRGB(x, y), true);
                    int r = (pixel.getRed() + tint.getRed()) / 2;
                    int gg = (pixel.getGreen() + tint.getGreen()) / 2;
                    int b = (pixel.getBlue() + tint.getBlue()) / 2;
                    int a = pixel.getAlpha();
                    int rgba = (a << 24) | (r << 16) | (gg << 8) | b;
                    retVal.setRGB(x, y, rgba);
                }
            }
        }
        if (effects != null && effects.contains(MapEffect.GLOWING) && ConfigUtil.getConfig().node("map", "effects", "glowing").getBoolean(true)) {
            g = retVal.createGraphics(); // same as getGraphics, except returns a G2D
            g.setColor(Color.WHITE);
            g.setStroke(new BasicStroke(width / 8.0f));
            g.drawLine(0, 0, 0, height); // top left -> bottom left
            g.drawLine(0, 0, width, 0); // top left -> top right
            g.drawLine(width, height, 0, height); // bottom right -> bottom left
            g.drawLine(width, height, width, 0); // bottom right -> top right
            g.dispose();
        }

        return retVal;
    }

    private @Nullable BufferedImage getImage(@NotNull String url) throws IOException {
        // Network lookup
        HttpURLConnection conn = WebRequest.builder(new URL(url))
                .timeout(new TimeUtil.Time(2500L, TimeUnit.MILLISECONDS))
                .userAgent("egg82/GPTFriend")
                .header("Accept", "image/png")
                .build()
                .getConnection();
        int status = conn.getResponseCode();

        if (status == 404) {
            logger.warn("Could not get skin data for URL {}", url);
            // No data exists
            return null;
        } else if (status == 200) {
            logger.debug("Got skin data for URL {}", url);
            return combineLayers(ImageIO.read(WebRequest.getInputStream(conn)));
        }
        return null;
    }

    private @NotNull BufferedImage combineLayers(@NotNull BufferedImage image) {
        BufferedImage retVal = new BufferedImage(8, 8, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = retVal.createGraphics(); // same as getGraphics, except returns a G2D
        g.drawImage(image.getSubimage(8, 8, 8, 8), 0, 0, 8, 8, null); // Layer 1
        g.drawImage(image.getSubimage(40, 8, 8, 8), 0, 0, 8, 8, null); // Layer 2
        g.dispose();

        return retVal;
    }

    private @Nullable MapHandler getMapHandler() {
        for (PluginHook hook : PluginHooks.getHooks()) {
            if (hook instanceof MapHandler h) {
                logger.debug("Using map handler {}", h.getClass().getSimpleName());
                return h;
            }
        }
        logger.debug("Using null map handler");
        return null;
    }

    @Override
    public String toString() { return "BukkitMapManagerImpl{}"; }
}

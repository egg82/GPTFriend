package me.egg82.gpt.hooks;

import it.unimi.dsi.fastutil.objects.Object2ByteArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ByteMap;
import me.egg82.gpt.core.Pair;
import me.egg82.gpt.logging.GELFLogger;
import me.egg82.gpt.utils.WorldUtil;
import ninja.egg82.events.BukkitEvents;
import ninja.egg82.events.TestStage;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventPriority;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.jpenilla.squaremap.api.*;
import xyz.jpenilla.squaremap.api.marker.*;

import java.awt.image.BufferedImage;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SquaremapHook implements MapHandler, PluginHook {
    private final Logger logger = new GELFLogger(LoggerFactory.getLogger(getClass()));

    public static void create(@NotNull Plugin plugin, @NotNull Plugin squaremap) {
        if (!squaremap.isEnabled()) {
            BukkitEvents.subscribe(plugin, PluginEnableEvent.class, EventPriority.MONITOR)
                    .expireIf(e -> e.getPlugin().getName().equals("squaremap"), TestStage.POST_HANDLE)
                    .filter(e -> e.getPlugin().getName().equals("squaremap"))
                    .handler(e -> hook = new SquaremapHook());
            return;
        }
        hook = new SquaremapHook();
    }

    private static SquaremapHook hook = null;

    public static @Nullable SquaremapHook get() { return hook; }

    private SquaremapHook() {
        logger.debug("Loading {}", getClass().getName());
        PluginHooks.getHooks().add(this);
    }

    @Override
    public void unload() {
        logger.debug("Unloading {}", getClass().getName());

        Squaremap map = SquaremapProvider.get();

        markerRegistryLock.writeLock().lock();
        try {
            for (Map.Entry<String, Pair<@NotNull String, @NotNull Location>> kvp : markers.entrySet()) {
                byte index = layers.getByte(kvp.getValue());
                World world = WorldUtil.getWorld(index);
                if (world == null) {
                    logger.warn("Could not load world at index {}", index);
                    continue;
                }

                Key key = Key.of(kvp.getKey());
                Key layerKey = Key.of(kvp.getValue().getT1());
                map.getWorldIfEnabled(BukkitAdapter.worldIdentifier(world)).ifPresent(w -> {
                    if (!w.layerRegistry().hasEntry(layerKey)) {
                        logger.warn("Attempted to remove a marker on a non-existent layer {} for world {}", layerKey.getKey(), world.getName());
                        return;
                    }
                    LayerProvider layer = w.layerRegistry().get(layerKey);
                    if (layer instanceof SimpleLayerProvider l) {
                        if (!l.hasMarker(key)) {
                            logger.warn("Attempted to remove a non-existent marker {} for layer {} for world {}", key.getKey(), layerKey.getKey(), world.getName());
                            return;
                        }
                        l.removeMarker(key);
                        logger.debug("Removed marker {} for layer {} for world {}", key.getKey(), layerKey.getKey(), world.getName());
                    }
                });
            }
            markers.clear();
        } finally {
            markerRegistryLock.writeLock().unlock();
        }

        layerRegistryLock.writeLock().lock();
        try {
            for (Object2ByteMap.Entry<String> kvp : layers.object2ByteEntrySet()) {
                World world = WorldUtil.getWorld(kvp.getByteValue());
                if (world == null) {
                    logger.warn("Could not load world at index {}", kvp.getByteValue());
                    continue;
                }

                Key key = Key.of(kvp.getKey());
                map.getWorldIfEnabled(BukkitAdapter.worldIdentifier(world)).ifPresent(w -> {
                    if (!w.layerRegistry().hasEntry(key)) {
                        logger.warn("Attempted to unregister a non-existent layer {} for world {}", key.getKey(), world.getName());
                        return;
                    }
                    LayerProvider layer = w.layerRegistry().get(key);
                    if (layer instanceof SimpleLayerProvider l) {
                        l.clearMarkers();
                    }
                    w.layerRegistry().unregister(key);
                    logger.debug("Unregistered layer {} for world {}", key.getKey(), world.getName());
                });
            }
            layers.clear();
        } finally {
            layerRegistryLock.writeLock().unlock();
        }

        iconRegistryLock.writeLock().lock();
        try {
            for (Map.Entry<String, BufferedImage> kvp : icons.entrySet()) {
                Key key = Key.of(kvp.getKey());
                if (!SquaremapProvider.get().iconRegistry().hasEntry(key)) {
                    logger.warn("Attempted to unregister a non-existent icon {}", key.getKey());
                    continue;
                }
                SquaremapProvider.get().iconRegistry().unregister(key);
                logger.debug("Unregistered icon {}", key.getKey());
            }
            icons.clear();
        } finally {
            iconRegistryLock.writeLock().unlock();
        }
    }

    @Override
    public void lightUnload() { logger.debug("Unloading (light) {}", getClass().getName()); }

    @Override
    public void lightReload() { logger.debug("Reloading (light) {}", getClass().getName()); }

    private final ReadWriteLock iconRegistryLock = new ReentrantReadWriteLock();
    private final Map<String, BufferedImage> icons = new HashMap<>();
    private final ReadWriteLock layerRegistryLock = new ReentrantReadWriteLock();
    private final Object2ByteMap<String> layers = new Object2ByteArrayMap<>();
    private final AtomicInteger layerIndex = new AtomicInteger(200);
    private final ReadWriteLock markerRegistryLock = new ReentrantReadWriteLock();
    private final Map<String, Pair<@NotNull String, @NotNull Location>> markers = new HashMap<>();

    @Override
    public boolean registerIcon(@NotNull String name, @NotNull BufferedImage image) {
        logger.debug("Attempting to register icon {}", name);

        Key key = Key.of(name);
        iconRegistryLock.writeLock().lock();
        try {
            if (SquaremapProvider.get().iconRegistry().hasEntry(key)) {
                logger.warn("Attempted to re-register an existing icon {}", key.getKey());
                return false;
            }
            icons.put(name, image);
            SquaremapProvider.get().iconRegistry().register(key, image);
            logger.debug("Registered icon {}", key.getKey());
        } finally {
            iconRegistryLock.writeLock().unlock();
        }
        return true;
    }

    @Override
    public boolean unregisterIcon(@NotNull String name) {
        logger.debug("Attempting to unregister icon {}", name);

        Key key = Key.of(name);
        iconRegistryLock.writeLock().lock();
        try {
            if (!SquaremapProvider.get().iconRegistry().hasEntry(key)) {
                logger.warn("Attempted to unregister a non-existent icon {}", key.getKey());
                icons.remove(name);
                return false;
            }
            SquaremapProvider.get().iconRegistry().unregister(key);
            icons.remove(name);
            logger.debug("Unregistered icon {}", key.getKey());
        } finally {
            iconRegistryLock.writeLock().unlock();
        }
        return true;
    }

    @Override
    public @Nullable BufferedImage getIcon(@NotNull String name) {
        iconRegistryLock.readLock().lock();
        try {
            return icons.get(name);
        } finally {
            iconRegistryLock.readLock().unlock();
        }
    }

    @Override
    public boolean registerLayer(@NotNull String name, @NotNull String visibleName, @NotNull World world) {
        logger.debug("Attempting to register layer {}", name);

        Key key = Key.of(name);
        int index = layerIndex.getAndIncrement();
        SimpleLayerProvider layer = SimpleLayerProvider.builder(visibleName)
                .showControls(true)
                .defaultHidden(false)
                .layerPriority(index)
                .zIndex(index)
                .build();

        layerRegistryLock.writeLock().lock();
        try {
            Optional<MapWorld> w = SquaremapProvider.get().getWorldIfEnabled(BukkitAdapter.worldIdentifier(world));
            if (w.isPresent()) {
                if (w.get().layerRegistry().hasEntry(key)) {
                    logger.warn("Attempted to re-register an existing layer {} for world {}", key.getKey(), world.getName());
                    return false;
                }
                layers.put(name, WorldUtil.getWorldIndex(world));
                w.get().layerRegistry().register(key, layer);
                logger.debug("Registered layer {} for world {}", key.getKey(), world.getName());
            }
        } finally {
            layerRegistryLock.writeLock().unlock();
        }
        return true;
    }

    @Override
    public boolean unregisterLayer(@NotNull String name) {
        logger.debug("Attempting to unregister layer {}", name);

        Key key = Key.of(name);
        layerRegistryLock.writeLock().lock();
        try {
            byte index = layers.removeByte(name);
            World world = WorldUtil.getWorld(index);
            if (world == null) {
                logger.warn("Could not load world at index {}", index);
                return false;
            }

            Optional<MapWorld> w = SquaremapProvider.get().getWorldIfEnabled(BukkitAdapter.worldIdentifier(world));
            if (w.isPresent()) {
                if (!w.get().layerRegistry().hasEntry(key)) {
                    logger.warn("Attempted to unregister a non-existent layer {} for world {}", key.getKey(), world.getName());
                    return false;
                }
                LayerProvider layer = w.get().layerRegistry().get(key);
                if (layer instanceof SimpleLayerProvider l) {
                    l.clearMarkers();
                }
                markerRegistryLock.writeLock().lock();
                try {
                    Set<String> removed = new HashSet<>();
                    for (Map.Entry<String, Pair<@NotNull String, @NotNull Location>> kvp : markers.entrySet()) {
                        if (name.equals(kvp.getValue().getT1())) {
                            removed.add(kvp.getKey());
                        }
                    }
                    for (String k : removed) {
                        markers.remove(k);
                        logger.debug("Removed marker {} for layer {} for world {}", k, key.getKey(), world.getName());
                    }
                } finally {
                    markerRegistryLock.writeLock().unlock();
                }
                w.get().layerRegistry().unregister(key);
                logger.debug("Unregistered layer {} for world {}", key.getKey(), world.getName());
            }
        } finally {
            layerRegistryLock.writeLock().unlock();
        }
        return true;
    }

    @Override
    public @Nullable World getLayerWorld(@NotNull String name) {
        layerRegistryLock.readLock().lock();
        try {
            byte index = layers.getByte(name);
            World world = WorldUtil.getWorld(index);
            if (world == null) {
                logger.warn("Could not load world at index {}", index);
            }
            return world;
        } finally {
            layerRegistryLock.readLock().unlock();
        }
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
        logger.debug("Attempting to add marker {}", name);

        BufferedImage icon;
        iconRegistryLock.readLock().lock();
        try {
            icon = icons.get(iconName);
            if (icon == null) {
                logger.error("Attempted to add marker {} without an existing icon {}", name, iconName);
                return false;
            }
        } finally {
            iconRegistryLock.readLock().unlock();
        }

        markerRegistryLock.writeLock().lock();
        try {
            if (markers.containsKey(name)) {
                logger.warn("Attempted to re-add an existing marker {}", name);
                return false;
            }

            layerRegistryLock.readLock().lock();
            try {
                byte index = layers.getByte(layerName);
                World world = WorldUtil.getWorld(index);
                if (world == null) {
                    logger.warn("Could not load world at index {}", index);
                    return false;
                }

                Key key = Key.of(name);
                Optional<MapWorld> w = SquaremapProvider.get().getWorldIfEnabled(BukkitAdapter.worldIdentifier(world));
                if (w.isPresent()) {
                    Key layerKey = Key.of(layerName);
                    LayerProvider layer = w.get().layerRegistry().get(layerKey);
                    if (layer instanceof SimpleLayerProvider l) {
                        if (l.hasMarker(key)) {
                            logger.warn("Attempted to re-register an existing marker {} for layer {} for world {}", key.getKey(), layerKey.getKey(), world.getName());
                            return false;
                        }
                        Icon i = Marker.icon(Point.of(location.getX(), location.getZ()), Key.of(iconName), icon.getWidth(), icon.getHeight());
                        i.markerOptions(MarkerOptions.builder().hoverTooltip(hoverTooltip).clickTooltip(clickTooltip).build());
                        l.addMarker(key, i);
                    } else {
                        logger.warn("Attempted to add a marker {} for non-simple layer {} for world {}", key.getKey(), layerKey.getKey(), world.getName());
                        return false;
                    }
                }
            } finally {
                layerRegistryLock.readLock().unlock();
            }
            markers.put(name, new Pair<>(layerName, location));
        } finally {
            markerRegistryLock.writeLock().unlock();
        }
        return true;
    }

    @Override
    public boolean removeMarker(@NotNull String name) {
        logger.debug("Attempting to remove marker {}", name);

        markerRegistryLock.writeLock().lock();
        try {
            Pair<@NotNull String, @NotNull Location> marker = markers.remove(name);
            if (marker == null) {
                logger.warn("Attempted to remove a non-existent marker {}", name);
                return false;
            }

            layerRegistryLock.readLock().lock();
            try {
                byte index = layers.getByte(marker.getT1());
                World world = WorldUtil.getWorld(index);
                if (world == null) {
                    logger.warn("Could not load world at index {}", index);
                    return false;
                }

                Key key = Key.of(name);
                Optional<MapWorld> w = SquaremapProvider.get().getWorldIfEnabled(BukkitAdapter.worldIdentifier(world));
                if (w.isPresent()) {
                    Key layerKey = Key.of(marker.getT1());
                    LayerProvider layer = w.get().layerRegistry().get(layerKey);
                    if (layer instanceof SimpleLayerProvider l) {
                        if (l.removeMarker(key) == null) {
                            logger.warn("Attempted to remove a non-existent marker {} for layer {} for world {}", key.getKey(), layerKey.getKey(), world.getName());
                            return false;
                        }
                    } else {
                        logger.warn("Attempted to remove a marker {} for non-simple layer {} for world {}", key.getKey(), layerKey.getKey(), world.getName());
                        return false;
                    }
                }
            } finally {
                layerRegistryLock.readLock().unlock();
            }
        } finally {
            markerRegistryLock.writeLock().unlock();
        }
        return true;
    }

    @Override
    public boolean updateMarker(@NotNull String name, @Nullable String layerName, @NotNull Location location) {
        logger.debug("Attempting to update marker {}", name);

        markerRegistryLock.writeLock().lock();
        try {
            Pair<@NotNull String, @NotNull Location> marker = markers.get(name);
            if (marker == null) {
                logger.warn("Attempted to update a non-existent marker {}", name);
                return false;
            }

            layerRegistryLock.readLock().lock();
            try {
                byte index = layers.getByte(marker.getT1());
                World world = WorldUtil.getWorld(index);
                if (world == null) {
                    logger.warn("Could not load world at index {}", index);
                    return false;
                }

                Key key = Key.of(name);
                Marker m = null;
                Optional<MapWorld> w = SquaremapProvider.get().getWorldIfEnabled(BukkitAdapter.worldIdentifier(world));
                if (w.isPresent()) {
                    Key layerKey = Key.of(marker.getT1());
                    LayerProvider layer = w.get().layerRegistry().get(layerKey);
                    if (layer instanceof SimpleLayerProvider l) {
                        m = l.removeMarker(key);
                        if (m == null) {
                            logger.warn("Attempted to update a non-existent marker {} for layer {} for world {}", key.getKey(), layerKey.getKey(), world.getName());
                            return false;
                        }
                        if (m instanceof Icon i) {
                            i.point(Point.of(location.getX(), location.getZ()));
                        } else if (m instanceof Circle c) {
                            c.center(Point.of(location.getX(), location.getZ()));
                        } else if (m instanceof Ellipse e) {
                            e.center(Point.of(location.getX(), location.getZ()));
                        }
                        if (layerName == null || marker.getT1().equals(layerName)) {
                            l.addMarker(key, m);
                        }
                        logger.debug("Updated marker {} for layer {} for world {}", key.getKey(), layerKey.getKey(), world.getName());
                    } else {
                        logger.warn("Attempted to update a marker {} for non-simple layer {} for world {}", key.getKey(), layerKey.getKey(), world.getName());
                        return false;
                    }
                }

                if (m == null) {
                    logger.warn("Attempted to update a non-existent marker {} for layer {} for world {}", key.getKey(), marker.getT1(), world.getName());
                    return false;
                }

                if (layerName != null && !marker.getT1().equals(layerName)) {
                    World oldWorld = world;
                    index = layers.getByte(layerName);
                    world = WorldUtil.getWorld(index);
                    if (world == null) {
                        logger.warn("Could not load world at index {}", index);
                        return false;
                    }

                    w = SquaremapProvider.get().getWorldIfEnabled(BukkitAdapter.worldIdentifier(world));
                    if (w.isPresent()) {
                        Key layerKey = Key.of(layerName);
                        LayerProvider layer = w.get().layerRegistry().get(layerKey);
                        if (layer instanceof SimpleLayerProvider l) {
                            l.addMarker(key, m);
                            logger.debug(
                                    "Updated marker {} fom layer {} in world {} to layer {} in world {}",
                                    key.getKey(),
                                    marker.getT1(),
                                    oldWorld.getName(),
                                    layerKey.getKey(),
                                    world.getName()
                            );
                        } else {
                            logger.warn("Attempted to update a marker {} for non-simple layer {} for world {}", key.getKey(), layerKey.getKey(), world.getName());
                            return false;
                        }
                    }
                }
            } finally {
                layerRegistryLock.readLock().unlock();
            }
            markers.put(name, new Pair<>(layerName != null ? layerName : marker.getT1(), location));
        } finally {
            markerRegistryLock.writeLock().unlock();
        }
        return true;
    }

    @Override
    public @Nullable Location getMarkerLocation(@NotNull String name) {
        markerRegistryLock.readLock().lock();
        try {
            Pair<@NotNull String, @NotNull Location> retVal = markers.get(name);
            return retVal != null ? retVal.getT2() : null;
        } finally {
            markerRegistryLock.readLock().unlock();
        }
    }
}

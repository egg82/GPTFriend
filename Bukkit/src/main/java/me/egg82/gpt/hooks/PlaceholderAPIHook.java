package me.egg82.gpt.hooks;

import me.clip.placeholderapi.PlaceholderAPI;
import me.egg82.gpt.logging.GELFLogger;
import ninja.egg82.events.BukkitEvents;
import ninja.egg82.events.TestStage;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlaceholderAPIHook implements PluginHook {
    private final Logger logger = new GELFLogger(LoggerFactory.getLogger(getClass()));

    public static void create(@NotNull Plugin plugin, @NotNull Plugin placeholderapi) {
        if (!placeholderapi.isEnabled()) {
            BukkitEvents.subscribe(plugin, PluginEnableEvent.class, EventPriority.MONITOR)
                    .expireIf(e -> e.getPlugin().getName().equals("PlaceholderAPI"), TestStage.POST_HANDLE)
                    .filter(e -> e.getPlugin().getName().equals("PlaceholderAPI"))
                    .handler(e -> hook = new PlaceholderAPIHook());
            return;
        }
        hook = new PlaceholderAPIHook();
    }

    private static PlaceholderAPIHook hook = null;

    public static @Nullable PlaceholderAPIHook get() { return hook; }

    private PlaceholderAPIHook() {
        logger.debug("Loading {}", getClass().getName());
        PluginHooks.getHooks().add(this);
    }

    @Override
    public void unload() { logger.debug("Unloading {}", getClass().getName()); }

    @Override
    public void lightUnload() { logger.debug("Unloading (light) {}", getClass().getName()); }

    @Override
    public void lightReload() { logger.debug("Reloading (light) {}", getClass().getName()); }

    public @NotNull String withPlaceholders(@NotNull String input) { return PlaceholderAPI.setPlaceholders(null, input); }

    public @NotNull String withPlaceholders(@NotNull Player player, @NotNull String input) { return PlaceholderAPI.setPlaceholders(player, input); }

    public @NotNull String withPlaceholders(@NotNull OfflinePlayer player, @NotNull String input) { return PlaceholderAPI.setPlaceholders(player, input); }
}

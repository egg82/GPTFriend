package me.egg82.gpt.hooks;

import me.egg82.gpt.logging.GELFLogger;
import net.luckperms.api.LuckPermsProvider;
import ninja.egg82.events.BukkitEvents;
import ninja.egg82.events.TestStage;
import org.bukkit.event.EventPriority;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class LuckPermsHook implements PermissionsHandler, PluginHook {
    private final Logger logger = new GELFLogger(LoggerFactory.getLogger(getClass()));

    public static void create(@NotNull Plugin plugin, @NotNull Plugin luckPerms) {
        if (!luckPerms.isEnabled()) {
            BukkitEvents.subscribe(plugin, PluginEnableEvent.class, EventPriority.MONITOR)
                    .expireIf(e -> e.getPlugin().getName().equals("LuckPerms"), TestStage.POST_HANDLE)
                    .filter(e -> e.getPlugin().getName().equals("LuckPerms"))
                    .handler(e -> hook = new LuckPermsHook());
            return;
        }
        hook = new LuckPermsHook();
    }

    private static LuckPermsHook hook = null;

    public static @Nullable LuckPermsHook get() { return hook; }

    private LuckPermsHook() {
        logger.debug("Loading {}", getClass().getName());
        PluginHooks.getHooks().add(this);
    }

    @Override
    public void unload() { logger.debug("Unloading {}", getClass().getName()); }

    @Override
    public void lightUnload() { logger.debug("Unloading (light) {}", getClass().getName()); }

    @Override
    public void lightReload() { logger.debug("Reloading (light) {}", getClass().getName()); }

    @Override
    public boolean hasPermission(@NotNull UUID uuid, @NotNull String permission) { return hasPermissionAsync(uuid, permission).join(); }

    @Override
    public @NotNull CompletableFuture<@NotNull Boolean> hasPermissionAsync(@NotNull UUID uuid, @NotNull String permission) {
        return LuckPermsProvider.get()
                .getUserManager()
                .loadUser(uuid)
                .thenApply(user -> user.getCachedData().getPermissionData().checkPermission(permission).asBoolean());
    }
}

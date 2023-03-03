package me.egg82.gpt.hooks;

import dev.cubxity.plugins.metrics.api.UnifiedMetricsProvider;
import dev.cubxity.plugins.metrics.api.metric.MetricsManager;
import ninja.egg82.events.BukkitEvents;
import ninja.egg82.events.TestStage;
import org.bukkit.event.EventPriority;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractUnifiedMetricsHook implements StatsHandler, PluginHook {
    public static void create(@NotNull Plugin plugin, @NotNull Plugin unifiedmetrics) {
        if (!unifiedmetrics.isEnabled()) {
            BukkitEvents.subscribe(plugin, PluginEnableEvent.class, EventPriority.MONITOR)
                    .expireIf(e -> e.getPlugin().getName().equals("UnifiedMetrics"), TestStage.POST_HANDLE)
                    .filter(e -> e.getPlugin().getName().equals("UnifiedMetrics"))
                    .handler(e -> hook = new UnifiedMetricsHookImpl());
            return;
        }
        hook = new UnifiedMetricsHookImpl();

        MetricsManager manager = UnifiedMetricsProvider.get().getMetricsManager();
        manager.registerCollection(hook.asyncCollection);
        manager.registerCollection(hook.syncCollection);
    }

    protected AbstractUnifiedMetricsHook() { }

    private static UnifiedMetricsHookImpl hook = null;

    // While loading this class (which depends on dev.cubxity.plugins.metrics.api.*), all dependencies have to be present.
    public static @Nullable UnifiedMetricsHookImpl get() { return hook; }
}

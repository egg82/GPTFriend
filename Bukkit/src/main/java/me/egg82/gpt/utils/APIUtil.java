package me.egg82.gpt.utils;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import me.egg82.gpt.api.APIRegistrationUtil;
import me.egg82.gpt.api.BukkitGPTAPIImpl;
import me.egg82.gpt.api.GPTAPI;
import me.egg82.gpt.api.event.api.GPTAPIDisableEvent;
import me.egg82.gpt.api.event.api.GPTAPILoadedEvent;
import me.egg82.gpt.api.model.map.BukkitMapManagerImpl;
import me.egg82.gpt.api.platform.BukkitPluginMetadataImpl;
import me.egg82.gpt.config.ConfigurationFileUtil;
import me.egg82.gpt.hooks.PluginHook;
import me.egg82.gpt.hooks.PluginHooks;
import me.egg82.gpt.logging.GELFLogger;
import me.egg82.gpt.tasks.AbstractTask;
import ninja.egg82.events.BukkitEvents;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class APIUtil {
    private static final Logger LOGGER = new GELFLogger(LoggerFactory.getLogger(APIUtil.class));

    public static final IntList tasks = new IntArrayList();

    private APIUtil() { }

    private static @NotNull GPTAPI createAPI(@NotNull Plugin plugin) {
        return BukkitGPTAPIImpl.builder()
                .mapManager(new BukkitMapManagerImpl())
                .pluginMetadata(new BukkitPluginMetadataImpl(plugin.getDescription().getVersion()))
                .build();
    }

    public static void loadConfigs(@NotNull Plugin plugin) {
        ConfigurationFileUtil.reloadConfig(plugin.getDataFolder(), plugin.getServer().getConsoleSender());
    }

    public static @NotNull GPTAPI loadApi(@NotNull Plugin plugin, boolean startup) {
        GPTAPI api = createAPI(plugin);
        APIRegistrationUtil.register(api);

        loadTasks(plugin);

        BukkitEvents.callAsync(plugin, new GPTAPILoadedEvent(api, true));

        return api;
    }

    private static void loadTasks(@NotNull Plugin plugin) {
        /*addTask(plugin, new CallingCurseTask(plugin, tasks), 0L, 40L);
        addTask(plugin, new GrogginessCurseTask(plugin, tasks), 0L, 30L);
        addTask(plugin, new NightCurseTask(plugin, tasks), 0L, 20L);
        addTask(plugin, new VoidCurseTask(plugin, tasks), 0L, 100L);
        addTask(plugin, new WitherCurseTask(plugin, tasks), 0L, 100L);*/
    }

    private static void addTask(@NotNull Plugin plugin, @NotNull AbstractTask task, long period) {
        addTask(plugin, task, 1L, period);
    }

    private static void addTask(@NotNull Plugin plugin, @NotNull AbstractTask task, long delay, long period) {
        tasks.add(Bukkit.getScheduler().runTaskTimer(plugin, task, delay, period).getTaskId());
    }

    public static void unloadApi(@NotNull GPTAPI api, boolean shutdown) {
        if (!shutdown) {
            for (PluginHook hook : PluginHooks.getHooks()) {
                hook.lightUnload();
            }
        }

        for (int task : tasks) {
            Bukkit.getScheduler().cancelTask(task);
        }
        tasks.clear();

        BukkitEvents.call(new GPTAPIDisableEvent(api, !Bukkit.isPrimaryThread()));

        APIRegistrationUtil.deregister();
    }
}

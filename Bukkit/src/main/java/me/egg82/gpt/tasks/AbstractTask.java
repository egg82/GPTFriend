package me.egg82.gpt.tasks;

import it.unimi.dsi.fastutil.ints.IntList;
import me.egg82.gpt.logging.GELFLogger;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractTask implements Runnable {
    protected final Logger logger = new GELFLogger(LoggerFactory.getLogger(getClass()));

    protected final Plugin plugin;
    protected final IntList tasks;

    protected AbstractTask(@NotNull Plugin plugin, @NotNull IntList tasks) {
        this.plugin = plugin;
        this.tasks = tasks;
    }
}

package me.egg82.gpt;

import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import me.egg82.gpt.logging.GELFLogger;
import me.egg82.gpt.logging.GELFLoggerUtil;
import me.egg82.gpt.utils.ServerIDUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class BukkitBootstrap implements PluginBootstrap {
    private final Logger logger = new GELFLogger(LoggerFactory.getLogger(getClass()));

    @Override
    public void bootstrap(@NotNull PluginProviderContext context) {
        // java.lang.NullPointerException: Cannot invoke "org.bukkit.Server.getVersion()" because "org.bukkit.Bukkit.server" is null
        //GELFLoggerUtil.setData("GPTFriend", ServerIDUtil.getId(new File(context.getDataDirectory().toFile(), "stats-id.txt")), context.getConfiguration().getVersion(), "Bukkit", Bukkit.getVersion());
    }

    @Override
    public @NotNull JavaPlugin createPlugin(@NotNull PluginProviderContext context) {
        GELFLoggerUtil.setData("GPTFriend", ServerIDUtil.getId(new File(context.getDataDirectory().toFile(), "stats-id.txt")), context.getConfiguration().getVersion(), "Bukkit", Bukkit.getVersion());
        return new GPTFriend(context);
    }
}

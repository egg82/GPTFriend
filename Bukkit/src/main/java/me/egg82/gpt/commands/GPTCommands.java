package me.egg82.gpt.commands;

import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.minecraft.extras.MinecraftHelp;
import cloud.commandframework.paper.PaperCommandManager;
import me.egg82.gpt.commands.internal.ReloadCommand;
import me.egg82.gpt.config.ConfigUtil;
import me.egg82.gpt.utils.ComponentUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;

public class GPTCommands extends CommandHolder {
    public GPTCommands(@NotNull Plugin plugin, @NotNull PaperCommandManager<CommandSender> commandManager) {
        super(commandManager);

        ConfigurationNode config = ConfigUtil.getConfig();
        String[] baseAliases = getAliases(config, "base");

        commands.add(
                commandManager.commandBuilder("gptfriend", baseAliases)
                        .literal(
                                "reload",
                                ComponentUtil.translateDescription("gpt.command.reload.desc"),
                                getAliases(config, "reload")
                        )
                        .permission(ConfigUtil.getConfig().node("permissions", "admin").getString("gpt.admin"))
                        .handler(new ReloadCommand(commandManager, plugin))
                        .build()
        );

        MinecraftHelp<CommandSender> help = new MinecraftHelp<>(
                "/gptfriend help",
                s -> s,
                commandManager
        );

        commandManager.command(
                commandManager.commandBuilder("gptfriend", baseAliases)
                        .literal(
                                "help",
                                ComponentUtil.translateDescription("gpt.command.help.desc"),
                                getAliases(config, "help")
                        )
                        .argument(StringArgument.optional("query", StringArgument.StringMode.GREEDY))
                        .handler(context -> help.queryCommands(context.getOrDefault("query", ""), context.getSender()))
        );

        registerAll();
    }

    private boolean isVanished(@NotNull Player player) {
        for (MetadataValue meta : player.getMetadata("vanished")) {
            if (meta.asBoolean()) {
                return true;
            }
        }
        return false;
    }
}

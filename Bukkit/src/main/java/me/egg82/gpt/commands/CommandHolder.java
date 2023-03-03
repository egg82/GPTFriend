package me.egg82.gpt.commands;

import cloud.commandframework.Command;
import cloud.commandframework.paper.PaperCommandManager;
import me.egg82.gpt.logging.GELFLogger;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class CommandHolder {
    protected final Logger logger = new GELFLogger(LoggerFactory.getLogger(getClass()));

    protected final List<Command<CommandSender>> commands = new ArrayList<>();

    protected final PaperCommandManager<CommandSender> commandManager;

    protected CommandHolder(@NotNull PaperCommandManager<CommandSender> commandManager) {
        this.commandManager = commandManager;
    }

    public final int numCommands() { return commands.size(); }

    public final void cancel() {
        for (Command<CommandSender> command : commands) {
            // TODO: Deregister command
        }
    }

    protected final void registerAll() {
        for (Command<CommandSender> command : commands) {
            commandManager.command(command);
        }
    }

    protected final @NotNull String @NotNull [] getAliases(@NotNull ConfigurationNode config, @NotNull String command) {
        Set<String> retVal;
        try {
            retVal = new HashSet<>(!config.node("aliases", command).empty() ? config.node("aliases", command).getList(String.class) : new ArrayList<>());
        } catch (SerializationException ex) {
            logger.error("{}: {}", ex.getClass().getName(), ex.getMessage(), ex);
            retVal = new HashSet<>();
        }
        retVal.removeIf(action -> action == null || action.isEmpty());

        return retVal.toArray(new String[0]);
    }
}

package me.egg82.gpt.commands.internal;

import cloud.commandframework.execution.CommandExecutionHandler;
import cloud.commandframework.paper.PaperCommandManager;
import me.egg82.gpt.logging.GELFLogger;
import me.egg82.gpt.services.lookup.PlayerInfo;
import me.egg82.gpt.services.lookup.PlayerLookup;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractCommand implements CommandExecutionHandler<CommandSender> {
    protected final Logger logger = new GELFLogger(LoggerFactory.getLogger(getClass()));

    protected final PaperCommandManager<CommandSender> commandManager;

    protected AbstractCommand(@NotNull PaperCommandManager<CommandSender> commandManager) {
        this.commandManager = commandManager;
    }

    protected final @NotNull CompletableFuture<@NotNull UUID> fetchUuid(@NotNull String name) { return PlayerLookup.get(name).thenApply(PlayerInfo::getUUID); }
}

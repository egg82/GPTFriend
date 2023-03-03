package me.egg82.gpt.commands.internal;

import cloud.commandframework.context.CommandContext;
import cloud.commandframework.paper.PaperCommandManager;
import me.egg82.gpt.api.GPTAPI;
import me.egg82.gpt.api.GPTAPIProvider;
import me.egg82.gpt.api.event.api.GPTAPIDisableEvent;
import me.egg82.gpt.utils.APIUtil;
import me.egg82.gpt.utils.ComponentUtil;
import ninja.egg82.events.BukkitEvents;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand extends AbstractCommand {
    private final Plugin plugin;

    public ReloadCommand(@NotNull PaperCommandManager<CommandSender> commandManager, @NotNull Plugin plugin) {
        super(commandManager);
        this.plugin = plugin;
    }

    @Override
    public void execute(@NotNull CommandContext<CommandSender> context) {
        commandManager.taskRecipe().begin(context)
                .synchronous(c -> {
                    context.getSender().sendMessage(ComponentUtil.decorate("gpt.command.reload.begin"));
                    return c;
                })
                .asynchronous(c -> {
                    GPTAPI api = GPTAPIProvider.getInstance();

                    GPTAPIDisableEvent event = new GPTAPIDisableEvent(api, true);
                    BukkitEvents.call(event);

                    APIUtil.unloadApi(api, false);
                    try {
                        Thread.sleep(1500L); // Sleep (an additional) 1.5 sec, wait for other plugins to gracefully handle this API shutting down
                    } catch (InterruptedException ignored) {
                        Thread.currentThread().interrupt();
                    }
                    APIUtil.loadApi(plugin, false);
                })
                .synchronous(c -> context.getSender().sendMessage(ComponentUtil.decorate("gpt.command.reload.end")))
                .execute();
    }
}

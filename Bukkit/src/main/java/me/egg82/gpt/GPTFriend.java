package me.egg82.gpt;

import cloud.commandframework.bukkit.BukkitCommandManager;
import cloud.commandframework.bukkit.CloudBukkitCapabilities;
import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator;
import cloud.commandframework.minecraft.extras.MinecraftExceptionHandler;
import cloud.commandframework.paper.PaperCommandManager;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import me.egg82.gpt.api.GPTAPI;
import me.egg82.gpt.api.GPTAPIProvider;
import me.egg82.gpt.commands.CommandHolder;
import me.egg82.gpt.commands.GPTCommands;
import me.egg82.gpt.commands.processor.SubstringCommandSuggestionProcessor;
import me.egg82.gpt.config.ConfigUtil;
import me.egg82.gpt.config.ConfigurationFileUtil;
import me.egg82.gpt.core.BukkitPluginProviderContext;
import me.egg82.gpt.events.ChatEvents;
import me.egg82.gpt.events.EventHolder;
import me.egg82.gpt.hooks.*;
import me.egg82.gpt.locale.I18NTranslationRegistry;
import me.egg82.gpt.locale.LanguageFileUtil;
import me.egg82.gpt.logging.GELFLogger;
import me.egg82.gpt.logging.GELFLoggerUtil;
import me.egg82.gpt.utils.APIUtil;
import me.egg82.gpt.utils.ColorUtil;
import me.egg82.gpt.utils.ComponentUtil;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationRegistry;
import ninja.egg82.events.BukkitEventSubscriber;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class GPTFriend extends JavaPlugin {
    private static final Logger logger = new GELFLogger(LoggerFactory.getLogger(GPTFriend.class));

    private final List<@NotNull CommandHolder> commandHolders = new ArrayList<>();
    private final List<@NotNull EventHolder> eventHolders = new ArrayList<>();
    private final List<@NotNull BukkitEventSubscriber<?>> events = new ArrayList<>();

    private final PluginProviderContext pluginContext;
    private final TranslationRegistry registry = new I18NTranslationRegistry();

    private final MinecraftExceptionHandler<CommandSender> commandExceptionHandler = new MinecraftExceptionHandler<>();
    private PaperCommandManager<CommandSender> commandManager;

    protected GPTFriend(
            @NotNull final JavaPluginLoader loader,
            @NotNull final PluginDescriptionFile description,
            @NotNull final File dataFolder,
            @NotNull final File file
    ) {
        super(loader, description, dataFolder, file);
        this.pluginContext = new BukkitPluginProviderContext(description, dataFolder, ComponentLogger.logger(getClass()));
    }

    public GPTFriend(@NotNull PluginProviderContext pluginContext) {
        this.pluginContext = pluginContext;
    }

    public void onLoad() {
        try {
            // Empty
        } catch (Throwable ex) {
            logger.error("{}: {}", ex.getClass().getName(), ex.getMessage(), ex);
            if (getAllowErrorStats()) {
                GELFLoggerUtil.send(3, ex.getClass().getName() + ": " + ex.getMessage(), ex);
            }
            throw new RuntimeException(ex);
        }
    }

    public void onEnable() {
        try {
            loadTranslations();

            APIUtil.loadConfigs(this);
            registry.defaultLocale(ConfigUtil.getCachedConfig().getLanguage());

            enableCapabilities();

            GPTAPI api = APIUtil.loadApi(this, true);

            loadHooks();
            loadCommands();
            loadEvents();

            int numCommands = 0;
            for (CommandHolder commandHolder : commandHolders) {
                numCommands += commandHolder.numCommands();
            }

            int numEvents = events.size();
            for (EventHolder eventHolder : eventHolders) {
                numEvents += eventHolder.numEvents();
            }

            Audience console = this.getServer().getConsoleSender();

            console.sendMessage(ComponentUtil.decorate("gpt.general.enable_message"));

            console.sendMessage(
                    ComponentUtil.decorate("gpt.general.load_message",
                            Component.text(pluginContext.getConfiguration().getVersion()),
                            Component.text(api.getPluginMetadata().getApiVersion()),
                            Component.text(numCommands),
                            Component.text(numEvents),
                            Component.text(APIUtil.tasks.size())
                    )
            );
        } catch (Throwable ex) {
            logger.error("{}: {}", ex.getClass().getName(), ex.getMessage(), ex);
            if (getAllowErrorStats()) {
                GELFLoggerUtil.send(3, ex.getClass().getName() + ": " + ex.getMessage(), ex);
            }
            throw new RuntimeException(ex);
        }
    }

    public void onDisable() {
        try {
            for (CommandHolder commandHolder : commandHolders) {
                commandHolder.cancel();
            }
            commandHolders.clear();
            //commandManager.deleteRootCommand("gptfriend"); // TODO: This causes GPTFriend commands to no longer appear if reloading through BileTools, etc

            for (EventHolder eventHolder : eventHolders) {
                eventHolder.cancel();
            }
            eventHolders.clear();
            for (BukkitEventSubscriber<?> event : events) {
                event.cancel();
            }
            events.clear();

            unloadHooks();

            APIUtil.unloadApi(GPTAPIProvider.getInstance(), true);

            this.getServer().getConsoleSender().sendMessage(ComponentUtil.decorate("gpt.general.disable_message"));
        } catch (Throwable ex) {
            logger.error("{}: {}", ex.getClass().getName(), ex.getMessage(), ex);
            if (getAllowErrorStats()) {
                GELFLoggerUtil.send(3, ex.getClass().getName() + ": " + ex.getMessage(), ex);
            }
            throw new RuntimeException(ex);
        }
    }

    private boolean getAllowErrorStats() {
        try {
            return ConfigUtil.getConfig().node("stats", "errors").getBoolean(true);
        } catch (IllegalStateException ignored) {
            try {
                return ConfigurationFileUtil.getAllowErrorStats(getDataFolder());
            } catch (Exception ignored2) {
                return false;
            }
        }
    }

    private void loadTranslations() {
        GlobalTranslator.translator().addSource(registry);

        for (Locale locale : Locale.getAvailableLocales()) {
            try {
                ResourceBundle bundle = LanguageFileUtil.getLanguage(pluginContext.getDataDirectory().toFile(), locale, "gpt.lang.version");
                if (bundle != null) {
                    registry.registerAll(locale, bundle, true);
                }
            } catch (IOException ignored) { }
        }
    }

    private void enableCapabilities() {
        try {
            this.commandManager = new PaperCommandManager<>(
                    this,
                    AsynchronousCommandExecutionCoordinator.<CommandSender>builder().build(),
                    s -> s,
                    s -> s
            ); // Needs to be onEnable, fails otherwise
        } catch (Exception ex) {
            logger.error("Could not create the Cloud command manager.", ex);
            logger.error("Exception: {}", ex.getMessage());
            logger.error("Stacktrace:");
            ex.printStackTrace();
            return;
        }

        this.commandExceptionHandler
                .withInvalidSyntaxHandler()
                .withHandler(MinecraftExceptionHandler.ExceptionType.INVALID_SYNTAX, (sender, ex) -> {
                    logger.error("{}: {}", ex.getClass().getName(), ex.getMessage(), ex);
                    return ComponentUtil.translate("gpt.error.command.invalid_syntax",
                            Component.text(ex.getMessage().substring(ex.getMessage().indexOf('.') + 1))
                    ).color(ColorUtil.DARK_RED);
                })
                .withInvalidSenderHandler()
                .withHandler(
                        MinecraftExceptionHandler.ExceptionType.INVALID_SENDER,
                        (sender, ex) -> ComponentUtil.translate("gpt.error.command.invalid_sender",
                                Component.text(ex.getMessage())
                        ).color(ColorUtil.DARK_RED)
                )
                .withNoPermissionHandler()
                .withHandler(
                        MinecraftExceptionHandler.ExceptionType.NO_PERMISSION,
                        (sender, ex) -> ComponentUtil.translate("gpt.error.command.no_permission",
                                Component.text(ex.getMessage())
                        ).color(ColorUtil.DARK_RED)
                )
                .withArgumentParsingHandler()
                .withHandler(MinecraftExceptionHandler.ExceptionType.ARGUMENT_PARSING, (sender, ex) -> {
                    logger.error("{}: {}", ex.getClass().getName(), ex.getMessage(), ex);
                    return ComponentUtil.translate("gpt.error.command.invalid_args",
                            Component.text(ex.getMessage().substring(ex.getMessage().indexOf('.') + 1))
                    ).color(ColorUtil.DARK_RED);
                })
                .withCommandExecutionHandler()
                .withHandler(MinecraftExceptionHandler.ExceptionType.COMMAND_EXECUTION, (sender, ex) -> {
                    logger.error("{}: {}", ex.getClass().getName(), ex.getMessage(), ex);
                    return ComponentUtil.translate("gpt.error.internal").color(ColorUtil.DARK_RED);
                })
                .withDecorator(component -> ComponentUtil.translate("gpt.general.decorator")
                        .append(Component.text(" "))
                        .append(component)
                )
                .apply(commandManager, s -> s);

        Audience console = this.getServer().getConsoleSender();

        if (commandManager.hasCapability(CloudBukkitCapabilities.BRIGADIER)) {
            try {
                commandManager.registerBrigadier();
                console.sendMessage(
                        ComponentUtil.decorate("gpt.general.enable_hook",
                                Component.text("Brigadier")
                        ).color(ColorUtil.GREEN)
                );
            } catch (BukkitCommandManager.BrigadierFailureException ex) {
                logger.error("{}: {}", ex.getClass().getName(), ex.getMessage(), ex);
                console.sendMessage(
                        ComponentUtil.decorate("gpt.general.no_hook",
                                Component.text("Brigadier")
                        ).color(ColorUtil.YELLOW)
                );
            }
        } else {
            console.sendMessage(
                    ComponentUtil.decorate("gpt.general.no_hook",
                            Component.text("Brigadier")
                    ).color(ColorUtil.YELLOW)
            );
        }
        if (commandManager.hasCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
            commandManager.registerAsynchronousCompletions();
            console.sendMessage(
                    ComponentUtil.decorate("gpt.general.enable_hook",
                            Component.text("Async Paper")
                    ).color(ColorUtil.GREEN)
            );
        } else {
            console.sendMessage(
                    ComponentUtil.decorate("gpt.general.no_hook",
                            Component.text("Async Paper")
                    ).color(ColorUtil.YELLOW)
            );
        }

        commandManager.commandSuggestionProcessor(new SubstringCommandSuggestionProcessor<>());
    }

    private void loadCommands() {
        commandHolders.add(new GPTCommands(this, commandManager));
    }

    private void loadEvents() {
        eventHolders.add(new ChatEvents(this));
    }

    private void loadHooks() {
        Audience console = this.getServer().getConsoleSender();
        PluginManager manager = this.getServer().getPluginManager();

        Plugin citizens;
        if ((citizens = manager.getPlugin("Citizens")) != null) {
            console.sendMessage(
                    ComponentUtil.decorate("gpt.general.enable_hook",
                            Component.text("Citizens")
                    ).color(ColorUtil.GREEN)
            );
            CitizensHook.create(this, citizens);
        } else {
            console.sendMessage(
                    ComponentUtil.decorate("gpt.general.no_hook",
                            Component.text("Citizens")
                    ).color(ColorUtil.DARK_RED)
            );
        }

        Plugin placeholderapi;
        if ((placeholderapi = manager.getPlugin("PlaceholderAPI")) != null) {
            console.sendMessage(
                    ComponentUtil.decorate("gpt.general.enable_hook",
                            Component.text("PlaceholderAPI")
                    ).color(ColorUtil.GREEN)
            );
            PlaceholderAPIHook.create(this, placeholderapi);
        } else {
            console.sendMessage(
                    ComponentUtil.decorate("gpt.general.no_hook",
                            Component.text("PlaceholderAPI")
                    ).color(ColorUtil.YELLOW)
            );
        }

        Plugin unifiedmetrics;
        if ((unifiedmetrics = manager.getPlugin("UnifiedMetrics")) != null) {
            console.sendMessage(
                    ComponentUtil.decorate("gpt.general.enable_hook",
                            Component.text("UnifiedMetrics")
                    ).color(ColorUtil.GREEN)
            );
            AbstractUnifiedMetricsHook.create(this, unifiedmetrics);
        } else {
            console.sendMessage(
                    ComponentUtil.decorate("gpt.general.no_hook",
                            Component.text("UnifiedMetrics")
                    ).color(ColorUtil.YELLOW)
            );
        }

        Plugin squaremap;
        if ((squaremap = manager.getPlugin("squaremap")) != null) {
            console.sendMessage(
                    ComponentUtil.decorate("gpt.general.enable_hook",
                            Component.text("squaremap")
                    ).color(ColorUtil.GREEN)
            );
            SquaremapHook.create(this, squaremap);
        } else {
            console.sendMessage(
                    ComponentUtil.decorate("gpt.general.no_hook",
                            Component.text("squaremap")
                    ).color(ColorUtil.YELLOW)
            );
        }

        Plugin luckPerms;
        if ((luckPerms = manager.getPlugin("LuckPerms")) != null) {
            console.sendMessage(
                    ComponentUtil.decorate("gpt.general.enable_hook",
                            Component.text("LuckPerms")
                    ).color(ColorUtil.GREEN)
            );
            LuckPermsHook.create(this, luckPerms);
        } else {
            console.sendMessage(
                    ComponentUtil.decorate("gpt.general.no_hook",
                            Component.text("LuckPerms")
                    ).color(ColorUtil.YELLOW)
            );
        }
    }

    private void unloadHooks() {
        PluginHooks.getHooks().removeIf(h -> {
            h.unload();
            return true;
        });
    }
}

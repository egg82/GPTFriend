package me.egg82.gpt.config;

import me.egg82.gpt.logging.GELFLogger;
import me.egg82.gpt.logging.GELFLoggerUtil;
import me.egg82.gpt.utils.ColorUtil;
import me.egg82.gpt.utils.ComponentUtil;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Locale;

public class ConfigurationFileUtil {
    private static final Logger logger = new GELFLogger(LoggerFactory.getLogger(ConfigurationFileUtil.class));

    private ConfigurationFileUtil() { }

    public static boolean getAllowErrorStats(@NotNull File dataDirectory) {
        ConfigurationNode config;
        try {
            config = getConfigSimple("config.yml", new File(dataDirectory, "config.yml"), null);
        } catch (IOException ex) {
            logger.error("{}: {}", ex.getClass().getName(), ex.getMessage(), ex);
            return false;
        }

        return config.node("stats", "errors").getBoolean(true);
    }

    public static void reloadConfig(@NotNull File dataDirectory, @NotNull Audience console) {
        ConfigurationNode config;
        try {
            config = getConfig("config.yml", new File(dataDirectory, "config.yml"), console);
        } catch (IOException ex) {
            logger.error("{}: {}", ex.getClass().getName(), ex.getMessage(), ex);
            return;
        }

        GELFLoggerUtil.doSendErrors(config.node("stats", "errors").getBoolean(true));

        boolean silent = config.node("silent").getBoolean(false);
        if (silent) {
            console.sendMessage(ComponentUtil.decorate("gpt.config.silent"));
        }

        boolean debug = config.node("debug").getBoolean(false);
        if (!silent && debug) {
            console.sendMessage(ComponentUtil.decorate("gpt.config.debug"));
        }

        Locale language = getLanguage(config, silent, debug, console);

        if (!silent && debug) {
            // Print misc settings
        }

        CachedConfig cachedConfig = CachedConfig.builder()
                .language(language)
                .build();

        if (!silent && debug) {
            console.sendMessage(
                    ComponentUtil.decorate("gpt.config.admin_node",
                                           Component.text(config.node("permissions", "admin").getString("gpt.admin"))
                    )
            );

            console.sendMessage(
                    ComponentUtil.decorate("gpt.config.map.effects.glowing",
                                           Component.text(String.valueOf(config.node("map", "effects", "glowing").getBoolean(true)))
                    )
            );
            console.sendMessage(
                    ComponentUtil.decorate("gpt.config.map.effects.invisibility",
                                           Component.text(String.valueOf(config.node("map", "effects", "invisibility").getBoolean(true)))
                    )
            );
            console.sendMessage(
                    ComponentUtil.decorate("gpt.config.map.effects.fire",
                                           Component.text(String.valueOf(config.node("map", "effects", "fire").getBoolean(true)))
                    )
            );
        }

        ConfigUtil.setConfiguration(config, cachedConfig);
    }

    private static @NotNull Locale getLanguage(@NotNull ConfigurationNode config, boolean silent, boolean debug, @NotNull Audience console) {
        String configLanguage = config.node("lang").getString("en-US");
        Locale retVal = null;
        for (Locale locale : Locale.getAvailableLocales()) {
            String l = locale.getCountry() == null || locale.getCountry().isEmpty() ? locale.getLanguage() : locale.getLanguage() + "-" + locale.getCountry();
            if (locale.getLanguage().equalsIgnoreCase(configLanguage) || l.equalsIgnoreCase(configLanguage)) {
                retVal = locale;
                break;
            }
            if (locale.getCountry() != null && !locale.getCountry().isEmpty()) {
                l = locale.getLanguage() + "_" + locale.getCountry();
                if (locale.getLanguage().equalsIgnoreCase(configLanguage) || l.equalsIgnoreCase(configLanguage)) {
                    retVal = locale;
                    break;
                }
            }
        }

        if (retVal == null) {
            retVal = Locale.US;
            if (!silent) {
                console.sendMessage(
                        ComponentUtil.decorate("gpt.config.invalid_locale",
                                               Component.text(configLanguage),
                                               Component.text(retVal.getCountry() == null || retVal.getCountry().isEmpty() ? retVal.getLanguage() : retVal.getLanguage() + "-" + retVal.getCountry())
                        ).color(ColorUtil.YELLOW)
                );
            }
        }
        if (!silent && debug) {
            console.sendMessage(
                    ComponentUtil.decorate("gpt.config.console_locale",
                                           Component.text(retVal.getCountry() == null || retVal.getCountry().isEmpty() ? retVal.getLanguage() : retVal.getLanguage() + "-" + retVal.getCountry())
                    )
            );
        }

        return retVal;
    }

    public static @Nullable File getFile(@NotNull String path, @NotNull File parent) {
        path = path.trim();
        try {
            if (!path.isEmpty() && Path.of(path).isAbsolute()) {
                return new File(parent, path);
            } else {
                return path.isEmpty() ? null : new File(path);
            }
        } catch (InvalidPathException ex) {
            return null;
        }
    }

    private static @NotNull CommentedConfigurationNode getConfigSimple(
            @NotNull String resourcePath,
            @NotNull File fileOnDisk,
            @Nullable Audience console
    ) throws IOException {
        File parentDir = fileOnDisk.getParentFile();
        if (parentDir.exists() && !parentDir.isDirectory()) {
            Files.walk(parentDir.toPath())
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
        if (!parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                throw new IOException("Could not create parent directory structure.");
            }
        }
        if (fileOnDisk.exists() && fileOnDisk.isDirectory()) {
            Files.walk(fileOnDisk.toPath())
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }

        if (!fileOnDisk.exists()) {
            try (InputStream inStream = ConfigurationFileUtil.class.getResourceAsStream("/" + resourcePath)) {
                if (inStream != null) {
                    try (FileOutputStream outStream = new FileOutputStream(fileOnDisk)) {
                        int read;
                        byte[] buffer = new byte[4096];
                        while ((read = inStream.read(buffer, 0, buffer.length)) > 0) {
                            outStream.write(buffer, 0, read);
                        }
                    }
                }
            }
        }

        ConfigurationLoader<CommentedConfigurationNode> loader = YamlConfigurationLoader.builder().nodeStyle(NodeStyle.BLOCK).indent(2).file(fileOnDisk).build();
        return loader.load(
                ConfigurationOptions.defaults().header(PlainTextComponentSerializer.plainText().serialize(ComponentUtil.translate("gpt.config.comments_gone")))
        );
    }

    private static @NotNull CommentedConfigurationNode getConfig(
            @NotNull String resourcePath,
            @NotNull File fileOnDisk,
            @NotNull Audience console
    ) throws IOException {
        ConfigurationLoader<CommentedConfigurationNode> loader = YamlConfigurationLoader.builder().nodeStyle(NodeStyle.BLOCK).indent(2).file(fileOnDisk).build();
        CommentedConfigurationNode config = getConfigSimple(resourcePath, fileOnDisk, console);
        ConfigurationVersionUtil.conformVersion(loader, config, fileOnDisk);
        return config;
    }
}

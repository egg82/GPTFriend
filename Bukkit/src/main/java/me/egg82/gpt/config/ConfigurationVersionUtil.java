package me.egg82.gpt.config;

import com.google.common.io.Files;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.loader.ConfigurationLoader;

import java.io.File;
import java.io.IOException;

public class ConfigurationVersionUtil {
    private ConfigurationVersionUtil() { }

    public static void conformVersion(
            @NotNull ConfigurationLoader<CommentedConfigurationNode> loader,
            @NotNull CommentedConfigurationNode config,
            @NotNull File fileOnDisk
    ) throws IOException {
        double oldVersion = config.node("version").getDouble(1.0d);

        /*if (config.node("version").getDouble(1.0d) == 1.0d) {
            to11(config);
        }
        if (config.node("version").getDouble() == 1.1d) {
            to12(config);
        }*/

        if (config.node("version").getDouble() != oldVersion) {
            File backupFile = new File(fileOnDisk.getParent(), fileOnDisk.getName() + ".bak");
            if (backupFile.exists()) {
                java.nio.file.Files.delete(backupFile.toPath());
            }

            Files.copy(fileOnDisk, backupFile);
            loader.save(config);
        }
    }

    /*private static void to11(@NotNull CommentedConfigurationNode config) throws SerializationException {
        // Add ignore
        config.node("enchant-chance").set(0.0d);

        // Version
        config.node("version").set(1.1d);
    }

    private static void to12(@NotNull CommentedConfigurationNode config) throws SerializationException {
        // Add unbreaking bypass(-bypass?)
        config.node("bypass-unbreaking").set(Boolean.TRUE);

        // Add particles
        config.node("particles").set(Boolean.TRUE);

        // Add loot chances
        config.node("loot-chance", "enchant").set(0.00045d);
        config.node("loot-chance", "curse").set(0.00126d);

        // Version
        config.node("version").set(1.2d);
    }*/
}

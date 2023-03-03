package me.egg82.gpt.utils;

import me.egg82.gpt.logging.GELFLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.UUID;

public class ServerIDUtil {
    private static final Logger logger = new GELFLogger(LoggerFactory.getLogger(ServerIDUtil.class));

    private ServerIDUtil() { }

    public static @NotNull UUID getId(@NotNull File idFile) {
        UUID retVal;

        try {
            retVal = readId(idFile);
        } catch (IOException ex) {
            logger.error("{}: {}", ex.getClass().getName(), ex.getMessage(), ex);
            retVal = null;
        }

        if (retVal == null) {
            retVal = UUID.randomUUID();
            try {
                writeId(idFile, retVal);
            } catch (IOException ex) {
                logger.error("{}: {}", ex.getClass().getName(), ex.getMessage(), ex);
            }
        }

        return retVal;
    }

    private static @Nullable UUID readId(@NotNull File idFile) throws IOException {
        if (!idFile.exists() || (idFile.exists() && idFile.isDirectory())) {
            return null;
        }

        StringBuilder builder = new StringBuilder();
        try (FileReader reader = new FileReader(idFile); BufferedReader in = new BufferedReader(reader)) {
            String line;
            while ((line = in.readLine()) != null) {
                builder.append(line).append(System.lineSeparator());
            }
        }
        String retVal = builder.toString().trim();

        try {
            return UUID.fromString(retVal);
        } catch (IllegalArgumentException ignored) {
        }

        return null;
    }

    private static void writeId(@NotNull File idFile, @NotNull UUID id) throws IOException {
        File parent = idFile.getParentFile();
        if (parent.exists() && !parent.isDirectory()) {
            Files.delete(parent.toPath());
        }
        if (!parent.exists() && !parent.mkdirs()) {
            throw new IOException("Could not create parent directory structure.");
        }

        if (idFile.exists() && idFile.isDirectory()) {
            Files.walk(idFile.toPath())
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
        if (!idFile.exists() && !idFile.createNewFile()) {
            throw new IOException("Could not create parent directory structure.");
        }

        try (FileWriter out = new FileWriter(idFile)) {
            out.write(id + System.lineSeparator());
        }
    }
}

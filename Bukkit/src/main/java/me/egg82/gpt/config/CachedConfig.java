package me.egg82.gpt.config;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class CachedConfig {
    private CachedConfig() { }

    private Locale language = Locale.US;
    public @NotNull Locale getLanguage() { return language; }

    public static @NotNull CachedConfig.Builder builder() { return new Builder(); }

    public static class Builder {
        private final CachedConfig values = new CachedConfig();

        private Builder() { }

        public @NotNull CachedConfig.Builder language(@NotNull Locale value) {
            values.language = value;
            return this;
        }

        public @NotNull CachedConfig build() { return values; }
    }
}

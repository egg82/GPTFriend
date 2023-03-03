package me.egg82.gpt.api;

import org.jetbrains.annotations.NotNull;

/**
 * Provides static access to the {@link GPTAPI} service.
 */
public class GPTAPIProvider {
    private GPTAPIProvider() { }

    private static GPTAPI instance = null;

    /**
     * Return an instance of the {@link GPTAPI} service.
     *
     * @return The current {@link GPTAPI} service
     *
     * @throws IllegalStateException if not yet loaded
     */
    public static @NotNull GPTAPI getInstance() {
        GPTAPI i = instance;
        if (i == null) {
            throw new IllegalStateException("me.egg82.gpt.GPTAPI is not loaded.");
        }
        return i;
    }

    private static void register(@NotNull GPTAPI instance) { GPTAPIProvider.instance = instance; }

    private static void deregister() { GPTAPIProvider.instance = null; }
}

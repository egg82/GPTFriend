package me.egg82.gpt.api.event.api;

import me.egg82.gpt.api.GPTAPI;
import me.egg82.gpt.api.event.GPTEvent;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when the API is about to be disabled.
 */
public final class GPTAPIDisableEvent extends GPTEvent {
    private static final HandlerList handlers = new HandlerList();

    /**
     * Create a new GPTAPIDisableEvent
     *
     * @param api   the API instance this event was dispatched from
     * @param async true if the event is async, false if not
     */
    public GPTAPIDisableEvent(@NotNull GPTAPI api, boolean async) {
        super(api, async);
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static @NotNull HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public String toString() {
        return "GPTAPIDisableEvent{}";
    }
}

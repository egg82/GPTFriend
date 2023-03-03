package me.egg82.gpt.api.event.api;

import me.egg82.gpt.api.GPTAPI;
import me.egg82.gpt.api.event.GPTEvent;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called after the API has been loaded and the
 * plugin is ready to start sending events.
 *
 * <p>The API is ready after GPTFriend loads; however,
 * delaying this event gives other plugins a
 * chance to register handlers and custom
 * messages as they load.</p>
 */
public final class GPTAPILoadedEvent extends GPTEvent {
    private static final HandlerList handlers = new HandlerList();

    /**
     * Create a new GPTAPILoadedEvent
     *
     * @param api   the API instance this event was dispatched from
     * @param async true if the event is async, false if not
     */
    public GPTAPILoadedEvent(@NotNull GPTAPI api, boolean async) {
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
        return "GPTAPILoadedEvent{}";
    }
}

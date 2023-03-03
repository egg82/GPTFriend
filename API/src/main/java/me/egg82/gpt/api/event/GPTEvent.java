package me.egg82.gpt.api.event;

import me.egg82.gpt.api.GPTAPI;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

/**
 * A superclass for all GPTFriend events.
 */
public abstract class GPTEvent extends Event {
    protected final GPTAPI api;
    private final Class<? extends GPTEvent> clazz;

    /**
     * Create a new GPTEvent
     * @param api the API instance this event was dispatched from
     * @param async true if the event is async, false if not
     */
    protected GPTEvent(@NotNull GPTAPI api, boolean async) {
        super(async);
        this.api = api;
        this.clazz = getClass();
    }

    /**
     * Get the API instance this event was dispatched from
     *
     * @return the api instance
     */
    public final @NotNull GPTAPI getApi() { return api; }

    /**
     * Gets the type of the event.
     *
     * @return the type of the event
     */
    public final @NotNull Class<? extends GPTEvent> getEventType() { return clazz; }
}

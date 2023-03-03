package me.egg82.gpt.events;

import me.egg82.gpt.logging.GELFLogger;
import ninja.egg82.events.BukkitEventSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class EventHolder {
    protected final Logger logger = new GELFLogger(LoggerFactory.getLogger(getClass()));

    protected final List<BukkitEventSubscriber<?>> events = new ArrayList<>();

    public final int numEvents() { return events.size(); }

    public final void cancel() {
        for (BukkitEventSubscriber<?> event : events) {
            event.cancel();
        }
    }
}

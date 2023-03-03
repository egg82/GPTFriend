package me.egg82.gpt.locale;

import me.egg82.gpt.core.Pair;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.translation.TranslationRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class I18NTranslationRegistry implements TranslationRegistry {
    private final Key name = Key.key("gpt.locale", "registry");

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Map<@NotNull Pair<@NotNull String, @NotNull Locale>, MessageFormat> map = new HashMap<>();
    private final Set<@NotNull String> keys = new HashSet<>();

    public I18NTranslationRegistry() { }

    @Override
    public boolean contains(@NotNull String key) {
        lock.readLock().lock();
        try {
            return keys.contains(key);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public @NotNull Key name() {
        return name;
    }

    @Override
    public @Nullable MessageFormat translate(@NotNull String key, @NotNull Locale locale) {
        lock.readLock().lock();
        try {
            return map.get(new Pair<>(key, locale));
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void defaultLocale(@NotNull Locale locale) { }

    @Override
    public void register(@NotNull String key, @NotNull Locale locale, @NotNull MessageFormat format) {
        lock.writeLock().lock();
        try {
            map.put(new Pair<>(key, locale), format);
            keys.add(key);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void unregister(@NotNull String key) {
        lock.writeLock().lock();
        try {
            keys.remove(key);

            Set<Pair<@NotNull String, @NotNull Locale>> remove = new HashSet<>();
            for (Pair<@NotNull String, @NotNull Locale> p : map.keySet()) {
                if (key.equals(p.getT1())) {
                    remove.add(p);
                }
            }
            for (Pair<@NotNull String, @NotNull Locale> p : remove) {
                map.remove(p);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }
}

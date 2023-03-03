package me.egg82.gpt.core;

import me.egg82.gpt.logging.GELFLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class EnumFilter {
    private static final Logger logger = new GELFLogger(LoggerFactory.getLogger(EnumFilter.class));

    public static <T> Builder<T> builder(Class<T> clazz) { return new Builder<>(clazz); }

    public static class Builder<T> {
        private final Class<T> clazz;
        private final List<T> current;

        private Builder(Class<T> clazz) {
            if (clazz == null) {
                throw new IllegalArgumentException("clazz cannot be null.");
            }

            this.clazz = clazz;

            T[] enums = (clazz.isEnum()) ? clazz.getEnumConstants() : getStaticFields(clazz);
            current = new ArrayList<>(Arrays.asList((T[]) Arrays.copyOf(enums, enums.length, ((T[]) Array.newInstance(clazz, 0)).getClass())));
        }

        public Builder<T> whitelist(String substring) {
            if (substring == null) {
                throw new IllegalArgumentException("substring cannot be null.");
            }

            substring = substring.toLowerCase();

            for (Iterator<T> i = current.iterator(); i.hasNext();) {
                T s = i.next();
                if (!s.toString().toLowerCase().contains(substring)) {
                    i.remove();
                }
            }

            return this;
        }

        public Builder<T> blacklist(String substring) {
            if (substring == null) {
                throw new IllegalArgumentException("substring cannot be null.");
            }

            substring = substring.toLowerCase();

            for (Iterator<T> i = current.iterator(); i.hasNext();) {
                T s = i.next();
                if (s.toString().toLowerCase().contains(substring)) {
                    i.remove();
                }
            }

            return this;
        }

        public T[] build() {
            T[] retVal = (T[]) Array.newInstance(clazz, current.size());
            for (int i = 0; i < current.size(); i++) {
                retVal[i] = current.get(i);
            }

            return retVal;
        }

        private T[] getStaticFields(Class<?> clazz) {
            if (clazz == null) {
                throw new IllegalArgumentException("clazz cannot be null.");
            }

            Field[] fields = clazz.getDeclaredFields();
            ArrayList<Object> returns = new ArrayList<>();

            for (int i = 0; i < fields.length; i++) {
                if (!Modifier.isPrivate(fields[i].getModifiers())) {
                    try {
                        returns.add(fields[i].get(null));
                    } catch (IllegalAccessException ex) {
                        logger.warn(ex.getMessage(), ex);
                    }
                }
            }

            return returns.toArray((T[]) Array.newInstance(clazz, 0));
        }
    }
}

package me.egg82.gpt.utils;

import java.util.UUID;

public class UUIDUtil {
    private UUIDUtil() { }

    public static final UUID EMPTY_UUID = new UUID(0L, 0L);

    public static final String EMPTY_UUID_STRING = EMPTY_UUID.toString();
}

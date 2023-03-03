package me.egg82.gpt.api.model.map;

import org.jetbrains.annotations.Nullable;

/**
 * The effect to give an entity in the {@link MapManager}
 */
public enum MapEffect {
    /**
     * Default, no effect
     */
    NONE((byte) 0x00),
    /**
     * Is on fire
     */
    ON_FIRE((byte) 0x01),
    /**
     * Is crouching
     */
    CROUCHING((byte) 0x02),
    /**
     * Unused (previously riding)
     */
    UNUSED((byte) 0x04),
    /**
     * Is sprinting
     */
    SPRINTING((byte) 0x08),
    /**
     * Is swimming
     */
    SWIMMING((byte) 0x10),
    /**
     * Is invisible
     */
    INVISIBLE((byte) 0x20),
    /**
     * has glowing effect
     */
    GLOWING((byte) 0x40),
    /**
     * Is flying with an elytra
     */
    ELYTRA_FLYING((byte) 0x80);

    private static final MapEffect[] VALUES = values();
    private final byte effect;

    MapEffect(byte effect) {
        this.effect = effect;
    }

    public byte getEffect() { return effect; }

    public static MapEffect[] getValues() {
        return VALUES;
    }

    public static @Nullable MapEffect getValue(byte effect) {
        for (MapEffect s : VALUES) {
            if (s.effect == effect) {
                return s;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "MapEffect{" +
                "effect=" + effect +
                '}';
    }
}

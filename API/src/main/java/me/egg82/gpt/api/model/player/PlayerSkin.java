package me.egg82.gpt.api.model.player;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

/**
 * A player skin which contains information necessary to
 * fetch, load, and use skins on {@link Player} instances.
 */
public interface PlayerSkin {
    /**
     * Get the skin's texture base64.
     *
     * @return the skin's texture base64
     */
    @NotNull String getBase64();

    /**
     * Get the skin's base64 signature.
     *
     * <p>Returns null if no signature is available.</p>
     *
     * @return the skin's base64 signature
     */
    @Nullable String getSignature();

    /**
     * Get the skin's JSON data.
     *
     * @return the skin's JSON data
     */
    @NotNull String getJson();

    /**
     * Get the skin's creation timestamp.
     *
     * @return the skin's creation timestamp
     */
    @NotNull Instant getTimestamp();

    /**
     * Get the skin's original profile ID.
     *
     * @return the skin's original profile ID
     */
    @NotNull String getProfileId();

    /**
     * Get the skin's original profile name.
     *
     * @return the skin's original profile name
     */
    @NotNull String getProfileName();

    /**
     * Get the skin's Mojang URL.
     *
     * @return the skin's Mojang URL
     */
    @NotNull String getSkinUrl();
}

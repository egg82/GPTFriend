package me.egg82.gpt.api.model.player;

import me.egg82.gpt.utils.UUIDUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

public class NullPlayerSkinImpl implements PlayerSkin {
    @Override
    public @NotNull String getBase64() { return ""; }

    @Override
    public @Nullable String getSignature() { return null; }

    @Override
    public @NotNull String getJson() { return "{}"; }

    @Override
    public @NotNull Instant getTimestamp() { return Instant.now(); }

    @Override
    public @NotNull String getProfileId() { return UUIDUtil.EMPTY_UUID_STRING; }

    @Override
    public @NotNull String getProfileName() { return ""; }

    @Override
    public @NotNull String getSkinUrl() { return ""; }

    @Override
    public String toString() { return "NullPlayerSkinImpl{}"; }
}

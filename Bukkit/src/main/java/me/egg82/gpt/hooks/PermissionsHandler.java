package me.egg82.gpt.hooks;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface PermissionsHandler {
    boolean hasPermission(@NotNull UUID uuid, @NotNull String permission);

    @NotNull CompletableFuture<@NotNull Boolean> hasPermissionAsync(@NotNull UUID uuid, @NotNull String permission);
}

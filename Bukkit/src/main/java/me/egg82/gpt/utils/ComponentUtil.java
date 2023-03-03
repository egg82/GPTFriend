package me.egg82.gpt.utils;

import cloud.commandframework.minecraft.extras.RichDescription;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class ComponentUtil {
    private ComponentUtil() { }

    public static boolean isText(@Nullable Component component, @NotNull String text) {
        if (component instanceof TextComponent textComponent) {
            return textComponent.content().equals(text);
        }
        return false;
    }

    public static @NotNull Component decorate(@NotNull String key) {
        return Component.translatable("gpt.general.decorator").append(Component.text(" ")).append(
                Component.translatable(key)
        );
    }

    public static @NotNull Component decorate(@NotNull String key, @NotNull Component @NotNull ... args) {
        return Component.translatable("gpt.general.decorator").append(Component.text(" ")).append(
                Component.translatable(key)
                        .args(args)
        );
    }

    public static @NotNull Component decorate(@NotNull String key, @NotNull Collection<@NotNull Component> args) {
        return decorate(key, args.toArray(new Component[0]));
    }

    public static @NotNull Component translate(@NotNull String key) {
        return Component.translatable(key);
    }

    public static @NotNull Component translate(@NotNull String key, @NotNull Component @NotNull ... args) {
        return Component.translatable(key).args(args);
    }

    public static @NotNull Component translate(@NotNull String key, @NotNull Collection<@NotNull Component> args) {
        return translate(key, args.toArray(new Component[0]));
    }

    public static @NotNull RichDescription translateDescription(@NotNull String key) {
        return RichDescription.translatable(key);
    }

    public static @NotNull RichDescription translateDescription(@NotNull String key, @NotNull Component @NotNull ... args) {
        return RichDescription.translatable(key, args);
    }

    public static @NotNull RichDescription translateDescription(@NotNull String key, @NotNull Collection<@NotNull Component> args) {
        return translateDescription(key, args.toArray(new Component[0]));
    }
}

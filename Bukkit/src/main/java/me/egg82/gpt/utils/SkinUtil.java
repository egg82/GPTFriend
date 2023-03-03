package me.egg82.gpt.utils;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import me.egg82.gpt.api.model.player.PlayerSkin;
import me.egg82.gpt.api.model.player.PlayerSkinImpl;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SkinUtil {
    private SkinUtil() { }

    public static @NotNull PlayerSkin getSkin(@NotNull Player player) {
        PlayerSkin skin = null;
        PlayerProfile profile = player.getPlayerProfile();
        for (ProfileProperty property : profile.getProperties()) {
            if ("textures".equals(property.getName())) {
                skin = new PlayerSkinImpl(property.getValue(), property.getSignature());
                break;
            }
        }

        if (skin == null) {
            throw new IllegalStateException("Player skin was not found for online player " + player.getName() + " (" + player.getUniqueId() + ")");
        }
        return skin;
    }
}

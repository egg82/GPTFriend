package me.egg82.gpt.utils;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import me.egg82.gpt.api.model.player.PlayerSkin;
import me.egg82.gpt.api.model.player.PlayerSkinImpl;
import me.egg82.gpt.logging.GELFLogger;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class HeadUtil {
    private static final Logger logger = new GELFLogger(LoggerFactory.getLogger(HeadUtil.class));

    private static final LoadingCache<@NotNull String, @NotNull PlayerSkin> skins = Caffeine
            .newBuilder()
            .expireAfterAccess(1L, TimeUnit.DAYS)
            .build(PlayerSkinImpl::new);

    private HeadUtil() { }

    public static @NotNull ItemStack getSkull(@NotNull PlayerSkin skin) {
        return getSkull(skin, 1);
    }

    public static @NotNull ItemStack getSkull(@NotNull PlayerSkin skin, int amount) {
        ItemStack retVal = new ItemStack(Material.PLAYER_HEAD, amount);
        //retVal.setDurability((short) 3); // TODO: PLAYER_HEAD should cover skull "durability", but check anyway

        ItemMeta meta = retVal.getItemMeta();
        if (meta == null) {
            meta = Bukkit.getItemFactory().getItemMeta(Material.PLAYER_HEAD);
        }
        meta.displayName(Component.text(skin.getProfileName()));

        PlayerProfile profile = Bukkit.createProfile(UUID.fromString(skin.getProfileId()), skin.getProfileName());
        profile.setProperty(new ProfileProperty("textures", skin.getBase64())); // TODO: Check that skull skins work

        SkullMeta skull = (SkullMeta) meta;
        skull.setPlayerProfile(profile);

        retVal.setItemMeta(meta);
        return retVal;
    }

    public static @NotNull PlayerSkin getEntitySkin(@NotNull LivingEntity entity) {
        // TODO: ALL the LivingEntities!
        switch (entity.getType()) {
            case PLAYER -> {
                return skins.get(SkinUtil.getSkin((Player) entity).getBase64()); // TODO: Check that player heads work
            }
            case AXOLOTL -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZThhOGEyZDdjY2YwYzM3NDZlMjNhYjU0OTEwNzBlMDkyM2YwNWIyMzVmOWEyZjVkNTNkMzg0MzUzODUzYmRkYyJ9fX0="); // https://minecraft-heads.com/custom-heads/animals/40439-axolotl
            }
            case BAT -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjY4MWE3MmRhNzI2M2NhOWFlZjA2NjU0MmVjY2E3YTE4MGM0MGUzMjhjMDQ2M2ZjYjExNGNiM2I4MzA1NzU1MiJ9fX0="); // https://minecraft-heads.com/custom-heads/animals/47787-bat
            }
            case BEE -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTg3NDc4N2UzNjE1OWU0ZDc0ZGI1ZTI1YmFiYTk4N2I2NjVkY2M4OTBiZTlmMjYyYmIwY2JjZjVkMDFiODJiNiJ9fX0="); // https://minecraft-heads.com/custom-heads/animals/31260-bee
            }
            case BLAZE -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjIwNjU3ZTI0YjU2ZTFiMmY4ZmMyMTlkYTFkZTc4OGMwYzI0ZjM2Mzg4YjFhNDA5ZDBjZDJkOGRiYTQ0YWEzYiJ9fX0="); // https://minecraft-heads.com/custom-heads/monsters/47778-blaze
            }
            case CAT -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTU2NmRlMzRkMDRkMTRiMzlmY2IxNWRhYWE3YjU0N2ZiMjM4ZTdkMzE4N2I0MjRkNDI2OTU4YWRjNTE0MDMzIn19fQ=="); // https://minecraft-heads.com/custom-heads/animals/49705-cat
            }
            case CAVE_SPIDER -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjA0ZDVmY2IyODlmZTY1YjY3ODY2ODJlMWM3MzZjM2Y3YjE2ZjM5ZDk0MGUzZDJmNDFjZjAwNDA3MDRjNjI4MiJ9fX0="); // https://minecraft-heads.com/custom-heads/monsters/26009-cave-spider
            }
            case CHICKEN -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjVlZWFlNTI0NThjMjY0OTdiNmMxOGI1MWVlOTNhNzM2ZjJjNTEzNjRmZTc2ODNiYmYxNDA1OTJlZWM2ZSJ9fX0="); // https://minecraft-heads.com/custom-heads/animals/17928-chicken
            }
            case COD -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzg5MmQ3ZGQ2YWFkZjM1Zjg2ZGEyN2ZiNjNkYTRlZGRhMjExZGY5NmQyODI5ZjY5MTQ2MmE0ZmIxY2FiMCJ9fX0="); // https://minecraft-heads.com/custom-heads/animals/17898-cod-fish
            }
            case COW -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmU4NDU2MTU1MTQyY2JlNGU2MTM1M2ZmYmFmZjMwNGQzZDljNGJjOTI0N2ZjMjdiOTJlMzNlNmUyNjA2N2VkZCJ9fX0="); // https://minecraft-heads.com/custom-heads/animals/49706-cow
            }
            case CREEPER -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODMxYmEzOWExNmNhNDQyY2FiOTM1MDQwZGRjNzI2ZWRhZDlkZDI3NmVjZmFmNmZlZTk5NzY3NjhiYjI3NmJjMCJ9fX0="); // https://minecraft-heads.com/custom-heads/monsters/16171-creeper
            }
            case DOLPHIN -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGU5Njg4Yjk1MGQ4ODBiNTViN2FhMmNmY2Q3NmU1YTBmYTk0YWFjNmQxNmY3OGU4MzNmNzQ0M2VhMjlmZWQzIn19fQ=="); // https://minecraft-heads.com/custom-heads/animals/16799-dolphin
            }
            case DONKEY -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzIwMTI0NWZjZGM2Y2M0MjY5NGQ4ZTY3ZTU2NjI4MmUwN2JjMDBhMGEzOTdmYzcyNjYwOWMyNzg5ZmQ5ZTZiMSJ9fX0="); // https://minecraft-heads.com/custom-heads/animals/47914-donkey
            }
            case DROWNED -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzg0ZGY3OWM0OTEwNGIxOThjZGFkNmQ5OWZkMGQwYmNmMTUzMWM5MmQ0YWI2MjY5ZTQwYjdkM2NiYmI4ZTk4YyJ9fX0="); // https://minecraft-heads.com/custom-heads/humanoid/21521-drowned
            }
            case ELDER_GUARDIAN -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWM3OTc0ODJhMTRiZmNiODc3MjU3Y2IyY2ZmMWI2ZTZhOGI4NDEzMzM2ZmZiNGMyOWE2MTM5Mjc4YjQzNmIifX19"); // https://minecraft-heads.com/custom-heads/monsters/326-elder-guardian
            }
            case ENDER_DRAGON -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmZjZGFlNTg2YjUyNDAzYjkyYjE4NTdlZTQzMzFiYWM2MzZhZjA4YmFiOTJiYTU3NTBhNTRhODMzMzFhNjM1MyJ9fX0="); // https://minecraft-heads.com/custom-heads/monsters/36338-ender-dragon
            }
            case ENDERMAN -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTZjMGIzNmQ1M2ZmZjY5YTQ5YzdkNmYzOTMyZjJiMGZlOTQ4ZTAzMjIyNmQ1ZTgwNDVlYzU4NDA4YTM2ZTk1MSJ9fX0="); // https://minecraft-heads.com/custom-heads/monsters/23778-enderman
            }
            case ENDERMITE -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWJjN2I5ZDM2ZmI5MmI2YmYyOTJiZTczZDMyYzZjNWIwZWNjMjViNDQzMjNhNTQxZmFlMWYxZTY3ZTM5M2EzZSJ9fX0="); // https://minecraft-heads.com/custom-heads/monsters/18427-endermite
            }
            case EVOKER -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDk1NDEzNWRjODIyMTM5NzhkYjQ3ODc3OGFlMTIxMzU5MWI5M2QyMjhkMzZkZDU0ZjFlYTFkYTQ4ZTdjYmE2In19fQ=="); // https://minecraft-heads.com/custom-heads/humanoid/3862-evoker
            }
            case FOX -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDg5NTRhNDJlNjllMDg4MWFlNmQyNGQ0MjgxNDU5YzE0NGEwZDVhOTY4YWVkMzVkNmQzZDczYTNjNjVkMjZhIn19fQ=="); // https://minecraft-heads.com/custom-heads/animals/26328-fox
            }
            case GHAST -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGI2YTcyMTM4ZDY5ZmJiZDJmZWEzZmEyNTFjYWJkODcxNTJlNGYxYzk3ZTVmOTg2YmY2ODU1NzFkYjNjYzAifX19"); // https://minecraft-heads.com/custom-heads/monsters/321-ghast
            }
            case GIANT -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTZmYzg1NGJiODRjZjRiNzY5NzI5Nzk3M2UwMmI3OWJjMTA2OTg0NjBiNTFhNjM5YzYwZTVlNDE3NzM0ZTExIn19fQ=="); // https://minecraft-heads.com/custom-heads/humanoid/4187-zombie
            }
            case GLOW_SQUID -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTIwOWUxMWU2MWM3MjdjZmE2ZGMxMjg0NTFlNDEyZjMwMWQxMjcxNzNjNWU1MmEwYjFhOWEyNDM5ZDc2YjRlIn19fQ=="); // https://minecraft-heads.com/custom-heads/animals/49018-glow-squid
            }
            case GOAT -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTY2MjMzNmQ4YWUwOTI0MDdlNThmN2NjODBkMjBmMjBlNzY1MDM1N2E0NTRjZTE2ZTMzMDc2MTlhMDExMDY0OCJ9fX0="); // https://minecraft-heads.com/custom-heads/animals/43601-goat
            }
            case GUARDIAN -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDk1MjkwZTA5MGMyMzg4MzJiZDc4NjBmYzAzMzk0OGM0ZDAzMTM1MzUzM2FjOGY2NzA5ODgyM2I3ZjY2N2YxYyJ9fX0="); // https://minecraft-heads.com/custom-heads/monsters/26819-guardian
            }
            case HOGLIN -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWJiOWJjMGYwMWRiZDc2MmEwOGQ5ZTc3YzA4MDY5ZWQ3Yzk1MzY0YWEzMGNhMTA3MjIwODU2MWI3MzBlOGQ3NSJ9fX0="); // https://minecraft-heads.com/custom-heads/animals/34783-hoglin
            }
            case HORSE -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmU3OGM0NzYyNjc0ZGRlOGIxYTVhMWU4NzNiMzNmMjhlMTNlN2MxMDJiMTkzZjY4MzU0OWIzOGRjNzBlMCJ9fX0="); // https://minecraft-heads.com/custom-heads/animals/635-horse
            }
            case HUSK -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzA5NjE2NGY4MTk1MGE1Y2MwZTMzZTg3OTk5Zjk4Y2RlNzkyNTE3ZjRkN2Y5OWE2NDdhOWFlZGFiMjNhZTU4In19fQ=="); // https://minecraft-heads.com/custom-heads/humanoid/41306-husk
            }
            case ILLUSIONER -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmYyODgyZGQwOTcyM2U0N2MwYWI5NjYzZWFiMDgzZDZhNTk2OTI3MzcwNjExMGM4MjkxMGU2MWJmOGE4ZjA3ZSJ9fX0="); // https://minecraft-heads.com/custom-heads/humanoid/23766-illusioner
            }
            case IRON_GOLEM -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODkwOTFkNzllYTBmNTllZjdlZjk0ZDdiYmE2ZTVmMTdmMmY3ZDQ1NzJjNDRmOTBmNzZjNDgxOWE3MTQifX19"); // https://minecraft-heads.com/custom-heads/humanoid/341-iron-golem
            }
            case LLAMA -> {
                // TODO: llama colors
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzJiMWVjZmY3N2ZmZTNiNTAzYzMwYTU0OGViMjNhMWEwOGZhMjZmZDY3Y2RmZjM4OTg1NWQ3NDkyMTM2OCJ9fX0="); // https://minecraft-heads.com/custom-heads/animals/3929-llama-brown
            }
            case MAGMA_CUBE -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzg5NTdkNTAyM2M5MzdjNGM0MWFhMjQxMmQ0MzQxMGJkYTIzY2Y3OWE5ZjZhYjM2Yjc2ZmVmMmQ3YzQyOSJ9fX0="); // https://minecraft-heads.com/custom-heads/monsters/323-magma-cube
            }
            case MULE -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTA0ODZhNzQyZTdkZGEwYmFlNjFjZTJmNTVmYTEzNTI3ZjFjM2IzMzRjNTdjMDM0YmI0Y2YxMzJmYjVmNWYifX19"); // https://minecraft-heads.com/custom-heads/animals/3918-mule
            }
            case MUSHROOM_COW -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDBiYzYxYjk3NTdhN2I4M2UwM2NkMjUwN2EyMTU3OTEzYzJjZjAxNmU3YzA5NmE0ZDZjZjFmZTFiOGRiIn19fQ=="); // https://minecraft-heads.com/custom-heads/animals/339-mooshroom
            }
            case OCELOT -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTFmMDdlM2YyZTVmMjU2YmZhZGU2NjZhOGRlMWI1ZDMwMjUyYzk1ZTk4ZjhhOGVjYzZlM2M3YjdmNjcwOTUifX19"); // https://minecraft-heads.com/custom-heads/animals/634-ocelot
            }
            case PANDA -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGNhMDk2ZWVhNTA2MzAxYmVhNmQ0YjE3ZWUxNjA1NjI1YTZmNTA4MmM3MWY3NGE2MzljYzk0MDQzOWY0NzE2NiJ9fX0="); // https://minecraft-heads.com/custom-heads/animals/23596-panda
            }
            case PARROT -> {
                // TODO: Parrot colors
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTRiYThkNjZmZWNiMTk5MmU5NGI4Njg3ZDZhYjRhNTMyMGFiNzU5NGFjMTk0YTI2MTVlZDRkZjgxOGVkYmMzIn19fQ=="); // https://minecraft-heads.com/custom-heads/animals/6534-parrot-red
            }
            case PHANTOM -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzQ2ODMwZGE1ZjgzYTNhYWVkODM4YTk5MTU2YWQ3ODFhNzg5Y2ZjZjEzZTI1YmVlZjdmNTRhODZlNGZhNCJ9fX0="); // https://minecraft-heads.com/custom-heads/monsters/17931-phantom
            }
            case PIG -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjIxNjY4ZWY3Y2I3OWRkOWMyMmNlM2QxZjNmNGNiNmUyNTU5ODkzYjZkZjRhNDY5NTE0ZTY2N2MxNmFhNCJ9fX0="); // https://minecraft-heads.com/custom-heads/animals/337-pig
            }
            case PIGLIN -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDNjZTAyMzc0N2M3ODI5YjUxYWI5Y2Q2NmU3OGEyMDM0ZDhmOTg4OTIyMzQyYTQ0ZWU1NmI3YWI1YzVmNTA1YSJ9fX0="); // https://minecraft-heads.com/custom-heads/humanoid/34885-piglin
            }
            case PIGLIN_BRUTE -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2UzMDBlOTAyNzM0OWM0OTA3NDk3NDM4YmFjMjllM2E0Yzg3YTg0OGM1MGIzNGMyMTI0MjcyN2I1N2Y0ZTFjZiJ9fX0="); // https://minecraft-heads.com/custom-heads/humanoid/38372-piglin-brute
            }
            case PILLAGER -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGFlZTZiYjM3Y2JmYzkyYjBkODZkYjVhZGE0NzkwYzY0ZmY0NDY4ZDY4Yjg0OTQyZmRlMDQ0MDVlOGVmNTMzMyJ9fX0="); // https://minecraft-heads.com/custom-heads/humanoid/25149-pillager
            }
            case POLAR_BEAR -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzRmZTkyNjkyMmZiYjQwNmYzNDNiMzRhMTBiYjk4OTkyY2VlNDQxMDEzN2QzZjg4MDk5NDI3YjIyZGUzYWI5MCJ9fX0="); // https://minecraft-heads.com/custom-heads/animals/39560-polar-bear
            }
            case PUFFERFISH -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTcxNTI4NzZiYzNhOTZkZDJhMjI5OTI0NWVkYjNiZWVmNjQ3YzhhNTZhYzg4NTNhNjg3YzNlN2I1ZDhiYiJ9fX0="); // https://minecraft-heads.com/custom-heads/animals/17900-pufferfish
            }
            case RABBIT -> {
                // TODO: Rabbit colors
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmZlY2M2YjVlNmVhNWNlZDc0YzQ2ZTc2MjdiZTNmMDgyNjMyN2ZiYTI2Mzg2YzZjYzc4NjMzNzJlOWJjIn19fQ=="); // https://minecraft-heads.com/custom-heads/animals/3933-rabbit
            }
            case RAVAGER -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWNiOWYxMzlmOTQ4OWQ4NmU0MTBhMDZkOGNiYzY3MGM4MDI4MTM3NTA4ZTNlNGJlZjYxMmZlMzJlZGQ2MDE5MyJ9fX0="); // https://minecraft-heads.com/custom-heads/monsters/23765-ravager
            }
            case SALMON -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWZiZGYxMDMxN2E3YTYzMTVjM2NiYWY3ZWRjNTkwZjBkOTc3MTRiZDY5OWM4NjE1YTI0ZjcyYmI4YTliYyJ9fX0="); // https://minecraft-heads.com/custom-heads/animals/17714-salmon
            }
            case SHEEP -> {
                // TODO: Sheep colors
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjMxZjljY2M2YjNlMzJlY2YxM2I4YTExYWMyOWNkMzNkMThjOTVmYzczZGI4YTY2YzVkNjU3Y2NiOGJlNzAifX19"); // https://minecraft-heads.com/custom-heads/animals/334-sheep-white
            }
            case SHULKER -> {
                // TODO: Shulker colors
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWU3MzgzMmUyNzJmODg0NGM0NzY4NDZiYzQyNGEzNDMyZmI2OThjNThlNmVmMmE5ODcxYzdkMjlhZWVhNyJ9fX0="); // https://minecraft-heads.com/custom-heads/monsters/6400-shulker-purple
            }
            case SILVERFISH -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGE5MWRhYjgzOTFhZjVmZGE1NGFjZDJjMGIxOGZiZDgxOWI4NjVlMWE4ZjFkNjIzODEzZmE3NjFlOTI0NTQwIn19fQ=="); // https://minecraft-heads.com/custom-heads/monsters/3936-silverfish
            }
            case SKELETON -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzAxMjY4ZTljNDkyZGExZjBkODgyNzFjYjQ5MmE0YjMwMjM5NWY1MTVhN2JiZjc3ZjRhMjBiOTVmYzAyZWIyIn19fQ=="); // https://minecraft-heads.com/custom-heads/humanoid/8188-skeleton
            }
            case SKELETON_HORSE -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDdlZmZjZTM1MTMyYzg2ZmY3MmJjYWU3N2RmYmIxZDIyNTg3ZTk0ZGYzY2JjMjU3MGVkMTdjZjg5NzNhIn19fQ=="); // https://minecraft-heads.com/custom-heads/animals/6013-skeleton-horse
            }
            case SLIME -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmIxMzEzM2E4ZmI0ZWYwMGI3MWVmOWJhYjYzOWE2NmZiYzdkNWNmZmNjMTkwYzFkZjc0YmYyMTYxZGZkM2VjNyJ9fX0="); // https://minecraft-heads.com/custom-heads/monsters/39474-slime
            }
            case SNOWMAN -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWZkZmQxZjc1MzhjMDQwMjU4YmU3YTkxNDQ2ZGE4OWVkODQ1Y2M1ZWY3MjhlYjVlNjkwNTQzMzc4ZmNmNCJ9fX0="); // https://minecraft-heads.com/custom-heads/monsters/4378-snow-golem
            }
            case SPIDER -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzg3YTk2YThjMjNiODNiMzJhNzNkZjA1MWY2Yjg0YzJlZjI0ZDI1YmE0MTkwZGJlNzRmMTExMzg2MjliNWFlZiJ9fX0="); // https://minecraft-heads.com/custom-heads/monsters/26010-spider
            }
            case SQUID -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDg3MDU2MjRkYWEyOTU2YWE0NTk1NmM4MWJhYjVmNGZkYjJjNzRhNTk2MDUxZTI0MTkyMDM5YWVhM2E4YjgifX19"); // https://minecraft-heads.com/custom-heads/animals/12237-squid
            }
            case STRAY -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzhkZGY3NmU1NTVkZDVjNGFhOGEwYTVmYzU4NDUyMGNkNjNkNDg5YzI1M2RlOTY5ZjdmMjJmODVhOWEyZDU2In19fQ=="); // https://minecraft-heads.com/custom-heads/humanoid/3244-stray
            }
            case STRIDER -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMThhOWFkZjc4MGVjN2RkNDYyNWM5YzA3NzkwNTJlNmExNWE0NTE4NjY2MjM1MTFlNGM4MmU5NjU1NzE0YjNjMSJ9fX0="); // https://minecraft-heads.com/custom-heads/animals/35431-strider
            }
            case WITHER_SKELETON -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzk1M2I2YzY4NDQ4ZTdlNmI2YmY4ZmIyNzNkNzIwM2FjZDhlMWJlMTllODE0ODFlYWQ1MWY0NWRlNTlhOCJ9fX0="); // https://minecraft-heads.com/custom-heads/humanoid/5972-wither-skeleton
            }
            case ZOGLIN -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTY3ZTE4NjAyZTAzMDM1YWQ2ODk2N2NlMDkwMjM1ZDg5OTY2NjNmYjllYTQ3NTc4ZDNhN2ViYmM0MmE1Y2NmOSJ9fX0="); // https://minecraft-heads.com/custom-heads/animals/35932-zoglin
            }
            case ZOMBIE -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTZmYzg1NGJiODRjZjRiNzY5NzI5Nzk3M2UwMmI3OWJjMTA2OTg0NjBiNTFhNjM5YzYwZTVlNDE3NzM0ZTExIn19fQ=="); // https://minecraft-heads.com/custom-heads/humanoid/4187-zombie
            }
            case ZOMBIE_HORSE -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDIyOTUwZjJkM2VmZGRiMThkZTg2ZjhmNTVhYzUxOGRjZTczZjEyYTZlMGY4NjM2ZDU1MWQ4ZWI0ODBjZWVjIn19fQ=="); // https://minecraft-heads.com/custom-heads/animals/2913-zombie-horse
            }
            case ZOMBIE_VILLAGER -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTYxZjE5ZmZkOGFlNDI1NzkyYzRiMTgxNzU2YWRmZjhkNDgxNzRhZWVmNThhMGYzMjdhMjhjNzQyYzcyNDQyIn19fQ=="); // https://minecraft-heads.com/custom-heads/humanoid/4136-zombie-villager
            }
            case ZOMBIFIED_PIGLIN -> {
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2VhYmFlY2M1ZmFlNWE4YTQ5Yzg4NjNmZjQ4MzFhYWEyODQxOThmMWEyMzk4ODkwYzc2NWUwYThkZTE4ZGE4YyJ9fX0="); // https://minecraft-heads.com/custom-heads/monsters/35073-zombified-piglin
            }
            default -> {
                logger.warn("Could not get skin for entity type {}", entity.getType().name());
                return skins.get(
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDBlZTI4YjNkZjBkMDI1MGUyNDE2ZTJhNjJkN2RkY2Y5ZjJjOWNjODIzNjkwNDQ2OWZhMWY5MWYyYTk1OTVmZiJ9fX0="); // https://minecraft-heads.com/custom-heads/alphabet/45319-redstone-block-exclamation-mark
            }
        }
    }
}

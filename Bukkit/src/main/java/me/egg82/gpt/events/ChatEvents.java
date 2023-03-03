package me.egg82.gpt.events;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.egg82.gpt.hooks.traits.GPTTrait;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import ninja.egg82.events.BukkitEventFilters;
import ninja.egg82.events.BukkitEvents;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class ChatEvents extends EventHolder {
    private static final PlainTextComponentSerializer SERIALIZER = PlainTextComponentSerializer.plainText();

    private final Plugin plugin;

    public ChatEvents(@NotNull Plugin plugin) {
        this.plugin = plugin;

        events.add(
                BukkitEvents.subscribe(plugin, AsyncChatEvent.class, EventPriority.MONITOR)
                        .filter(BukkitEventFilters.ignoreCancelled())
                        .handler(this::asyncChat)
        );
    }

    private void asyncChat(@NotNull AsyncChatEvent event) {
        Player player = event.getPlayer();
        String playerName = SERIALIZER.serialize(player.displayName());
        String message = SERIALIZER.serialize(event.message());

        System.out.println("Player: " + playerName);
        System.out.println("Message: " + message);

        Bukkit.getScheduler().runTask(plugin, () -> {
            for (NPC npc : CitizensAPI.getNPCRegistry()) {
                if (npc.hasTrait(GPTTrait.class)) {
                    String response = npc.getTraitNullable(GPTTrait.class).respond(playerName, message);
                    System.out.println("Response: " + response);
                    player.sendMessage(Component.text(response));
                }
            }
        });
    }
}

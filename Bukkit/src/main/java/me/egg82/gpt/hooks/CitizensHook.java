package me.egg82.gpt.hooks;

import me.egg82.gpt.hooks.traits.GPTTrait;
import me.egg82.gpt.logging.GELFLogger;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.TraitInfo;
import ninja.egg82.events.BukkitEvents;
import ninja.egg82.events.TestStage;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventPriority;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CitizensHook implements NPCHandler, PluginHook {
    private final Logger logger = new GELFLogger(LoggerFactory.getLogger(getClass()));

    public static void create(@NotNull Plugin plugin, @NotNull Plugin citizens) {
        if (!citizens.isEnabled()) {
            BukkitEvents.subscribe(plugin, PluginEnableEvent.class, EventPriority.MONITOR)
                    .expireIf(e -> e.getPlugin().getName().equals("Citizens"), TestStage.POST_HANDLE)
                    .filter(e -> e.getPlugin().getName().equals("Citizens"))
                    .handler(e -> hook = new CitizensHook());
            return;
        }
        hook = new CitizensHook();
    }

    private static CitizensHook hook = null;

    public static @Nullable CitizensHook get() { return hook; }

    private final TraitInfo gptTrait;

    private final NPC test;

    private CitizensHook() {
        logger.debug("Loading {}", getClass().getName());

        gptTrait = TraitInfo.create(GPTTrait.class);
        CitizensAPI.getTraitFactory().registerTrait(gptTrait);

        test = getNPC("Robert");

        PluginHooks.getHooks().add(this);
    }

    private @NotNull NPC getNPC(@NotNull String name) {
        for (NPC npc : CitizensAPI.getNPCRegistry()) {
            if (npc.getName().equals(name)) {
                return npc;
            }
        }

        NPC retVal = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, name);
        retVal.getOrAddTrait(GPTTrait.class);
        retVal.spawn(Bukkit.getWorlds().get(0).getSpawnLocation());
        return retVal;
    }

    @Override
    public void unload() {
        logger.debug("Unloading {}", getClass().getName());

        test.despawn();

        CitizensAPI.getTraitFactory().deregisterTrait(gptTrait);
    }

    @Override
    public void lightUnload() {
        logger.debug("Unloading (light) {}", getClass().getName());

        CitizensAPI.getTraitFactory().deregisterTrait(gptTrait);
    }

    @Override
    public void lightReload() {
        logger.debug("Reloading (light) {}", getClass().getName());

        CitizensAPI.getTraitFactory().registerTrait(gptTrait);
    }
}

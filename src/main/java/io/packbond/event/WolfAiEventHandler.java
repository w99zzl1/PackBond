package io.packbond.event;

import io.packbond.PackBondMod;
import io.packbond.ai.WolfAiHooks;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Mod.EventBusSubscriber(modid = PackBondMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class WolfAiEventHandler {

    private static final Set<UUID> INITIALIZED_WOLVES = ConcurrentHashMap.newKeySet();

    private WolfAiEventHandler() {
    }

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinWorldEvent event) {
        if (!(event.getEntity() instanceof WolfEntity)) {
            return;
        }

        WolfEntity wolf = (WolfEntity) event.getEntity();
        WolfAiHooks.applyPathMalus(wolf);
        WolfAiHooks.replaceNodeProcessor(wolf);
    }

    @SubscribeEvent
    public static void onWolfFirstTick(LivingEvent.LivingUpdateEvent event) {
        if (!(event.getEntityLiving() instanceof WolfEntity)) {
            return;
        }

        WolfEntity wolf = (WolfEntity) event.getEntityLiving();
        if (wolf.level.isClientSide()) {
            return;
        }

        UUID id = wolf.getUUID();
        if (!INITIALIZED_WOLVES.add(id)) {
            return;
        }
        WolfAiHooks.replaceLeapGoal(wolf);
        WolfAiHooks.replaceMeleeGoal(wolf);
    }

    @SubscribeEvent
    public static void onWolfDeath(net.minecraftforge.event.entity.living.LivingDeathEvent event) {
        if (event.getEntityLiving() instanceof WolfEntity) {
            INITIALIZED_WOLVES.remove(event.getEntityLiving().getUUID());
        }
    }

    @SubscribeEvent
    public static void onWolfLeaveWorld(net.minecraftforge.event.entity.EntityLeaveWorldEvent event) {
        if (event.getEntity() instanceof WolfEntity) {
            INITIALIZED_WOLVES.remove(event.getEntity().getUUID());
        }
    }
}

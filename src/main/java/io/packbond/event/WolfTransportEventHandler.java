package io.packbond.event;

import io.packbond.PackBondMod;
import io.packbond.transport.WolfTransportService;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PackBondMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class WolfTransportEventHandler {

    private WolfTransportEventHandler() {
    }

    @SubscribeEvent
    public static void onBoneRightClick(PlayerInteractEvent.RightClickItem event) {
        WolfTransportService.handleBoneRightClick(event);
    }

    @SubscribeEvent
    public static void onChunkUnwatch(ChunkWatchEvent.UnWatch event) {
        WolfTransportService.handleChunkUnwatch(event);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        WolfTransportService.handlePlayerTick(event);
    }
}

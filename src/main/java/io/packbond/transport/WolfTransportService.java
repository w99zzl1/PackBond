package io.packbond.transport;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.ChunkWatchEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class WolfTransportService {

    private static int tickCounter = 0;
    private static final ConcurrentHashMap<UUID, List<CompoundNBT>> AUTO_TELEPORT_QUEUE = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<UUID, List<CompoundNBT>> SITTING_WHISTLE_QUEUE = new ConcurrentHashMap<>();

    private WolfTransportService() {
    }

    public static void handleBoneRightClick(PlayerInteractEvent.RightClickItem event) {
        if (event.getWorld().isClientSide()) {
            return;
        }

        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        ItemStack heldItem = event.getItemStack();
        if (heldItem.getItem() != Items.BONE) {
            return;
        }

        ServerWorld world = (ServerWorld) event.getWorld();
        int teleportedCount = 0;

        for (WolfEntity wolf : world.getEntitiesOfClass(WolfEntity.class, player.getBoundingBox().inflate(128.0D))) {
            if (wolf.isAlive() && wolf.isTame() && wolf.getOwner() == player) {
                wolf.fallDistance = 0.0F;
                wolf.setDeltaMovement(0, 0, 0);
                wolf.moveTo(player.getX() + 0.5D, player.getY(), player.getZ() + 0.5D);
                teleportedCount++;
            }
        }

        teleportedCount += spawnQueuedDogs(AUTO_TELEPORT_QUEUE.remove(player.getUUID()), world, player);
        teleportedCount += spawnQueuedDogs(SITTING_WHISTLE_QUEUE.remove(player.getUUID()), world, player);

        if (teleportedCount <= 0) {
            return;
        }

        String locale = player.getLanguage() != null ? player.getLanguage().toLowerCase() : "en_us";
        String messageText = locale.startsWith("ru")
            ? "[Зов Кости] Вы позвали стаю. Призвано собак: " + teleportedCount
            : "[Bone Call] You called the pack. Wolves summoned: " + teleportedCount;

        player.sendMessage(new StringTextComponent(messageText).withStyle(TextFormatting.AQUA), player.getUUID());
        event.setCancellationResult(ActionResultType.SUCCESS);
        event.setCanceled(true);
    }

    public static void handleChunkUnwatch(ChunkWatchEvent.UnWatch event) {
        ServerPlayerEntity player = event.getPlayer();
        ServerWorld world = event.getWorld();
        Chunk chunk = world.getChunk(event.getPos().x, event.getPos().z);
        if (chunk == null) {
            return;
        }

        for (Collection<Entity> entityList : chunk.getEntitySections()) {
            for (Entity entity : new ArrayList<>(entityList)) {
                if (!(entity instanceof WolfEntity)) {
                    continue;
                }

                WolfEntity wolf = (WolfEntity) entity;
                if (!wolf.isAlive() || !wolf.isTame() || wolf.getOwner() != player) {
                    continue;
                }

                CompoundNBT dogTag = new CompoundNBT();
                wolf.saveWithoutId(dogTag);
                dogTag.putString("id", EntityType.WOLF.getRegistryName().toString());

                if (wolf.isInSittingPose()) {
                    queueDog(SITTING_WHISTLE_QUEUE, player.getUUID(), dogTag);
                } else {
                    queueDog(AUTO_TELEPORT_QUEUE, player.getUUID(), dogTag);
                }

                wolf.remove();
            }
        }
    }

    public static void handlePlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.player instanceof ServerPlayerEntity)) {
            return;
        }

        tickCounter++;
        if (tickCounter % 40 != 0) {
            return;
        }

        ServerPlayerEntity player = (ServerPlayerEntity) event.player;
        ServerWorld world = player.getLevel();
        List<CompoundNBT> dogTags = AUTO_TELEPORT_QUEUE.get(player.getUUID());
        if (dogTags == null || dogTags.isEmpty()) {
            return;
        }

        if (player.isFallFlying()) {
            return;
        }

        BlockPos playerPos = new BlockPos(player.getX(), player.getY(), player.getZ());
        if (!player.isOnGround() || world.isEmptyBlock(playerPos.below())) {
            return;
        }

        spawnQueuedDogs(dogTags, world, player);
        AUTO_TELEPORT_QUEUE.remove(player.getUUID());
    }

    private static void queueDog(ConcurrentHashMap<UUID, List<CompoundNBT>> queue, UUID ownerId, CompoundNBT dogTag) {
        queue.computeIfAbsent(ownerId, key -> new ArrayList<>()).add(dogTag);
    }

    private static int spawnQueuedDogs(List<CompoundNBT> dogTags, ServerWorld world, ServerPlayerEntity player) {
        if (dogTags == null || dogTags.isEmpty()) {
            return 0;
        }

        int count = 0;
        for (CompoundNBT dogTag : dogTags) {
            count += spawnDogFromNBT(dogTag, world, player);
        }
        return count;
    }

    private static int spawnDogFromNBT(CompoundNBT dogTag, ServerWorld world, ServerPlayerEntity player) {
        Entity entity = EntityType.loadEntityRecursive(dogTag, world, newWolf -> {
            newWolf.moveTo(player.getX() + 0.5D, player.getY(), player.getZ() + 0.5D, newWolf.yRot, newWolf.xRot);
            return newWolf;
        });

        if (!(entity instanceof WolfEntity)) {
            return 0;
        }

        WolfEntity wolf = (WolfEntity) entity;
        wolf.fallDistance = 0.0F;
        wolf.setDeltaMovement(0, 0, 0);
        world.addFreshEntity(wolf);
        return 1;
    }
}

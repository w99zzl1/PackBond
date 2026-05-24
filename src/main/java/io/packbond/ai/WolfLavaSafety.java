package io.packbond.ai;

import net.minecraft.entity.Entity;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockReader;

public final class WolfLavaSafety {

    private WolfLavaSafety() {
    }

    public static boolean isLava(IBlockReader world, BlockPos pos) {
        return world.getFluidState(pos).is(FluidTags.LAVA);
    }

    public static boolean isEntityInLava(Entity entity) {
        BlockPos pos = entity.blockPosition();
        return isLava(entity.level, pos)
            || isLava(entity.level, pos.above())
            || isLava(entity.level, pos.below());
    }

    public static boolean hasLavaBetween(Entity from, Entity to) {
        double dx = to.getX() - from.getX();
        double dy = to.getY() - from.getY();
        double dz = to.getZ() - from.getZ();
        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
        int steps = Math.max(1, MathHelper.ceil(distance * 2.0D));

        for (int i = 1; i <= steps; i++) {
            double t = (double) i / (double) steps;
            BlockPos pos = new BlockPos(
                MathHelper.lerp(t, from.getX(), to.getX()),
                MathHelper.lerp(t, from.getY(), to.getY()),
                MathHelper.lerp(t, from.getZ(), to.getZ())
            );

            if (isLava(from.level, pos)
                || isLava(from.level, pos.above())
                || isLava(from.level, pos.below())) {
                return true;
            }
        }

        return false;
    }
}

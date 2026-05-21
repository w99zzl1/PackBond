package io.packbond.ai;

import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class LavaSafeNodeProcessor extends WalkNodeProcessor {

    @Override
    public PathNodeType getBlockPathType(IBlockReader world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        if (WolfLavaSafety.isLava(world, pos)
            || WolfLavaSafety.isLava(world, pos.above())
            || WolfLavaSafety.isLava(world, pos.below())) {
            return PathNodeType.DAMAGE_OTHER;
        }

        return super.getBlockPathType(world, x, y, z);
    }

    @Override
    public PathNodeType getBlockPathType(IBlockReader world, int x, int y, int z,
                                         net.minecraft.entity.MobEntity entity,
                                         int sizeX, int sizeY, int sizeZ,
                                         boolean canBreakDoors, boolean canEnterDoors) {
        PathNodeType type = super.getBlockPathType(
            world, x, y, z, entity, sizeX, sizeY, sizeZ, canBreakDoors, canEnterDoors
        );

        BlockPos pos = new BlockPos(x, y, z);
        for (int dx = 0; dx < sizeX; dx++) {
            for (int dz = 0; dz < sizeZ; dz++) {
                for (int dy = 0; dy < sizeY; dy++) {
                    BlockPos check = pos.offset(dx, dy, dz);
                    if (WolfLavaSafety.isLava(world, check)
                        || WolfLavaSafety.isLava(world, check.above())
                        || WolfLavaSafety.isLava(world, check.below())) {
                        return PathNodeType.DAMAGE_OTHER;
                    }
                }
            }
        }

        return type;
    }
}

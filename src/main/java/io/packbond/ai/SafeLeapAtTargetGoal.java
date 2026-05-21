package io.packbond.ai;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.util.math.BlockPos;

public class SafeLeapAtTargetGoal extends LeapAtTargetGoal {

    private final WolfEntity wolf;

    public SafeLeapAtTargetGoal(WolfEntity wolf, float yMotion) {
        super(wolf, yMotion);
        this.wolf = wolf;
    }

    @Override
    public boolean canUse() {
        if (!super.canUse()) {
            return false;
        }

        LivingEntity target = wolf.getTarget();
        if (target == null) {
            return false;
        }

        double dx = target.getX() - wolf.getX();
        double dz = target.getZ() - wolf.getZ();
        double len = Math.sqrt(dx * dx + dz * dz);
        if (len < 0.001D) {
            return false;
        }

        dx /= len;
        dz /= len;

        for (int i = 1; i <= 4; i++) {
            BlockPos pos = new BlockPos(
                wolf.getX() + dx * i,
                wolf.getY(),
                wolf.getZ() + dz * i
            );

            if (WolfLavaSafety.isLava(wolf.level, pos)
                || WolfLavaSafety.isLava(wolf.level, pos.above())
                || WolfLavaSafety.isLava(wolf.level, pos.below())) {
                return false;
            }
        }

        return true;
    }
}

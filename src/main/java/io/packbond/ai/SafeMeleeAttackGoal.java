package io.packbond.ai;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.pathfinding.Path;

public class SafeMeleeAttackGoal extends MeleeAttackGoal {

    private final WolfEntity wolf;

    public SafeMeleeAttackGoal(WolfEntity wolf, double speed, boolean longMemory) {
        super(wolf, speed, longMemory);
        this.wolf = wolf;
    }

    @Override
    public boolean canUse() {
        LivingEntity target = wolf.getTarget();
        if (target == null || WolfLavaSafety.isEntityInLava(target)) {
            return false;
        }

        if (!super.canUse()) {
            return false;
        }

        if (!WolfLavaSafety.hasLavaBetween(wolf, target)) {
            return true;
        }

        Path detour = wolf.getNavigation().createPath(target, 0);
        return detour != null;
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = wolf.getTarget();
        return target != null
            && !WolfLavaSafety.isEntityInLava(target)
            && super.canContinueToUse();
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity target, double distToTargetSqr) {
        if (WolfLavaSafety.isEntityInLava(target) || WolfLavaSafety.hasLavaBetween(wolf, target)) {
            return;
        }

        super.checkAndPerformAttack(target, distToTargetSqr);
    }
}

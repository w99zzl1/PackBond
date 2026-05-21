package io.packbond.ai;

import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.util.math.MathHelper;

import java.lang.reflect.Field;
import java.util.Set;

public final class WolfAiHooks {

    private WolfAiHooks() {
    }

    public static void applyPathMalus(WolfEntity wolf) {
        wolf.setPathfindingMalus(PathNodeType.LAVA, -9999.0F);
        wolf.setPathfindingMalus(PathNodeType.DANGER_FIRE, -9999.0F);
        wolf.setPathfindingMalus(PathNodeType.DAMAGE_FIRE, -9999.0F);
        wolf.setPathfindingMalus(PathNodeType.DAMAGE_OTHER, -9999.0F);
    }

    public static void replaceNodeProcessor(WolfEntity wolf) {
        try {
            Field nodeProcessorField = resolvePrivateField(PathNavigator.class, "nodeEvaluator", "field_179695_a");
            Field pathFinderField = resolvePrivateField(PathNavigator.class, "pathFinder", "field_179681_j");

            WalkNodeProcessor processor = new LavaSafeNodeProcessor();
            processor.setCanPassDoors(true);

            int range = MathHelper.floor(wolf.getAttributeValue(Attributes.FOLLOW_RANGE) * 16.0D);
            nodeProcessorField.set(wolf.getNavigation(), processor);
            pathFinderField.set(wolf.getNavigation(), new PathFinder(processor, range));
        } catch (Exception e) {
            System.err.println("[packbond] ERROR in replaceNodeProcessor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void replaceLeapGoal(WolfEntity wolf) {
        try {
            Set<PrioritizedGoal> goals = getAvailableGoals(wolf.goalSelector);
            PrioritizedGoal leapEntry = goals.stream()
                .filter(goal -> goal.getGoal() instanceof LeapAtTargetGoal)
                .findFirst()
                .orElse(null);

            if (leapEntry == null) {
                return;
            }

            int priority = leapEntry.getPriority();
            goals.remove(leapEntry);
            wolf.goalSelector.addGoal(priority, new SafeLeapAtTargetGoal(wolf, 0.4F));
        } catch (Exception e) {
            System.err.println("[packbond] ERROR in replaceLeapGoal: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void replaceMeleeGoal(WolfEntity wolf) {
        try {
            Set<PrioritizedGoal> goals = getAvailableGoals(wolf.goalSelector);
            PrioritizedGoal meleeEntry = goals.stream()
                .filter(goal -> goal.getGoal() instanceof MeleeAttackGoal)
                .findFirst()
                .orElse(null);

            if (meleeEntry == null) {
                return;
            }

            MeleeAttackGoal oldGoal = (MeleeAttackGoal) meleeEntry.getGoal();
            double speed = resolvePrivateField(MeleeAttackGoal.class, "speedModifier", "field_75440_e").getDouble(oldGoal);
            boolean longMemory = resolvePrivateField(MeleeAttackGoal.class, "followingTargetEvenIfNotSeen", "field_75437_f")
                .getBoolean(oldGoal);

            int priority = meleeEntry.getPriority();
            goals.remove(meleeEntry);
            wolf.goalSelector.addGoal(priority, new SafeMeleeAttackGoal(wolf, speed, longMemory));
        } catch (Exception e) {
            System.err.println("[packbond] ERROR in replaceMeleeGoal: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private static Set<PrioritizedGoal> getAvailableGoals(GoalSelector goalSelector) throws ReflectiveOperationException {
        Field availableGoalsField = resolvePrivateField(GoalSelector.class, "availableGoals", "field_220892_d");
        return (Set<PrioritizedGoal>) availableGoalsField.get(goalSelector);
    }

    private static Field resolvePrivateField(Class<?> owner, String... names) throws NoSuchFieldException {
        for (String name : names) {
            try {
                Field field = owner.getDeclaredField(name);
                field.setAccessible(true);
                return field;
            } catch (NoSuchFieldException ignored) {
            }
        }

        throw new NoSuchFieldException(owner.getName());
    }
}

package net.darkhax.primordialharvest.common.impl.entity.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class AvoidBlockGoal extends Goal {

    private final PathfinderMob mob;
    protected final PathNavigation pathNav;
    private final Predicate<BlockState> shouldAvoid;
    private final int distance;

    @Nullable
    private Vec3 toAvoid;

    @Nullable
    private Path escapePath;

    public AvoidBlockGoal(PathfinderMob mob, Predicate<BlockState> shouldAvoid, int distance) {
        this.mob = mob;
        this.pathNav = mob.getNavigation();
        this.shouldAvoid = shouldAvoid;
        this.distance = distance;
    }

    @Override
    public boolean canUse() {
        this.toAvoid = BlockPos.findClosestMatch(this.mob.blockPosition(), distance, distance, p -> shouldAvoid.test(mob.level().getBlockState(p))).map(BlockPos::getCenter).orElse(null);
        if (this.toAvoid != null) {
            final Vec3 destination = DefaultRandomPos.getPosAway(this.mob, 16, 7, this.toAvoid);
            if (destination != null && toAvoid.distanceToSqr(destination) > this.mob.distanceToSqr(toAvoid)) {
                this.escapePath = this.pathNav.createPath(destination.x, destination.y, destination.z, 0);
                return this.escapePath != null;
            }
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return !this.pathNav.isDone();
    }

    @Override
    public void start() {
        this.pathNav.moveTo(this.escapePath, 1.5f);
        this.mob.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 1));
    }

    @Override
    public void stop() {
        this.toAvoid = null;
    }

    @Override
    public void tick() {
        this.mob.getNavigation().setSpeedModifier(1.5f);
        this.mob.setTarget(null);
    }
}

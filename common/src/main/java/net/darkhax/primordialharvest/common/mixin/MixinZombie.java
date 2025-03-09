package net.darkhax.primordialharvest.common.mixin;

import net.darkhax.primordialharvest.common.impl.blocks.SpireshuckBlock;
import net.darkhax.primordialharvest.common.impl.entity.ai.AvoidBlockGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Zombie.class)
public class MixinZombie extends Monster {

    protected MixinZombie() {
        // no-op
        super(null, null);
    }

    @Inject(method = "registerGoals", at = @At("HEAD"))
    private void registerGoals(CallbackInfo ci) {
        this.goalSelector.addGoal(1, new AvoidBlockGoal(this, SpireshuckBlock::repelsMobs, 6));
    }
}
package net.darkhax.primordialharvest.common.mixin;

import net.darkhax.primordialharvest.common.impl.Helper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class MixinItemEntity extends Entity {

    public MixinItemEntity() {
        // no-op
        super(null, null);
    }

    @Inject(method = "tick()V", at = @At("RETURN"))
    private void onTick(CallbackInfo ci) {
        if (!this.isRemoved() && !this.level().isClientSide && this.tickCount % 20 == 0) {
            Helper.checkReagents((ItemEntity) (Object) this);
        }
    }
}

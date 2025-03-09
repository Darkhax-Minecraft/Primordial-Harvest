package net.darkhax.primordialharvest.common.impl.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class RumblepeaFruitItem extends Item {

    public RumblepeaFruitItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity user) {
        final ItemStack used = super.finishUsingItem(stack, level, user);
        if (!level.isClientSide && user instanceof ServerPlayer player) {
            level.explode(user, user.getX(), user.getY(), user.getZ(), 6f, Level.ExplosionInteraction.TRIGGER);
        }
        return used;
    }
}
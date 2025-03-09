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

public class SpireshuckFruitItem extends Item {

    public SpireshuckFruitItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity user) {
        final ItemStack used = super.finishUsingItem(stack, level, user);
        if (!level.isClientSide && user instanceof ServerPlayer player) {
            player.getStats().setValue(player, Stats.CUSTOM.get(Stats.TIME_SINCE_REST), 0);
            player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 600, 1));
            level.playSound(null, player.blockPosition(), SoundEvents.PHANTOM_HURT, SoundSource.PLAYERS, 1f, 0f);
        }
        return used;
    }
}
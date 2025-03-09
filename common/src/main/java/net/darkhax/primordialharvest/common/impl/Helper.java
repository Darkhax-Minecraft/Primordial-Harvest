package net.darkhax.primordialharvest.common.impl;

import net.darkhax.bookshelf.common.api.function.TriFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class Helper {

    public static TriFunction<BlockState, BlockGetter, BlockPos, Float> GET_GROWTH_SPEED;

    public static void dropLoot(Level level, BlockPos pos, ResourceKey<LootTable> tableId, Player player, ItemStack tool) {
        if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
            final LootParams params = new LootParams.Builder(serverLevel)
                    .withParameter(LootContextParams.BLOCK_STATE, level.getBlockState(pos))
                    .withParameter(LootContextParams.ORIGIN, pos.getCenter())
                    .withParameter(LootContextParams.TOOL, tool)
                    .withParameter(LootContextParams.THIS_ENTITY, player)
                    .withLuck(player.getLuck())
                    .create(LootContextParamSets.BLOCK);
            final LootTable table = level.getServer().reloadableRegistries().getLootTable(tableId);
            for (ItemStack stack : table.getRandomItems(params)) {
                Block.popResource(level, pos, stack);
            }
        }
    }

    public static void shrink(ItemEntity entity) {
        entity.getItem().shrink(1);
        if (entity.getItem().isEmpty()) {
            entity.discard();
        }
    }

    public static void checkReagents(ItemEntity entity) {
        if (entity.getItem().is(Content.PHILOSOPHER_REAGENTS)) {
            final BlockPos position = entity.blockPosition();
            final BlockState container = entity.level().getBlockState(position);
            if (container.is(Blocks.WATER_CAULDRON)) {
                final int level = container.getValue(BlockStateProperties.LEVEL_CAULDRON);
                if (level > 0) {
                    final List<ItemEntity> items = entity.level().getEntitiesOfClass(ItemEntity.class, new AABB(position), EntitySelector.ENTITY_STILL_ALIVE);
                    if (items.size() >= 3) {
                        ItemEntity sulfur = null;
                        ItemEntity mercury = null;
                        ItemEntity salt = null;
                        for (ItemEntity item : items) {
                            if (sulfur != null && mercury != null && salt != null) {
                                break;
                            }
                            final ItemStack stack = item.getItem();
                            if (sulfur == null && stack.is(Content.RUMBLEPEA_FRUIT.get())) {
                                sulfur = item;
                            }
                            else if (mercury == null && stack.is(Content.SPIRESHUCK_FRUIT.get())) {
                                mercury = item;
                            }
                            else if (salt == null && stack.is(Content.PALEOGOURD_FRUIT.get())) {
                                salt = item;
                            }
                        }
                        if (sulfur != null && mercury != null && salt != null) {
                            shrink(sulfur);
                            shrink(mercury);
                            shrink(salt);
                            entity.level().setBlock(position, Content.BLOOD_CAULDRON_BLOCK.get().defaultBlockState().setValue(BlockStateProperties.LEVEL_CAULDRON, level), 2);
                        }
                    }
                }
            }
        }
    }
}
package net.darkhax.primordialharvest.common.impl;

import net.darkhax.bookshelf.common.api.data.loot.PoolTarget;
import net.darkhax.bookshelf.common.api.function.CachedSupplier;
import net.darkhax.bookshelf.common.api.registry.ContentProvider;
import net.darkhax.bookshelf.common.api.registry.adapters.GameRegistryAdapter;
import net.darkhax.bookshelf.common.impl.registry.adapter.BlockRegistryAdapter;
import net.darkhax.bookshelf.common.impl.registry.adapter.BlockRenderTypeAdapter;
import net.darkhax.bookshelf.common.impl.registry.adapter.CreativeModeTabAdapter;
import net.darkhax.bookshelf.common.impl.registry.adapter.LootPoolAdditionAdapter;
import net.darkhax.primordialharvest.common.impl.blocks.BloodCauldronBlock;
import net.darkhax.primordialharvest.common.impl.blocks.PaleogourdBlock;
import net.darkhax.primordialharvest.common.impl.blocks.RumblepeaBlock;
import net.darkhax.primordialharvest.common.impl.blocks.SpireshuckBlock;
import net.darkhax.primordialharvest.common.impl.item.RumblepeaFruitItem;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Holder;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.TagKey;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.function.Supplier;

public class Content implements ContentProvider {

    public static final ResourceLocation PALEOGOURD_BLOCK_ID = PrimordialHarvest.id("paleogourd");
    public static final Supplier<Block> PALEOGOURD_BLOCK = CachedSupplier.of(BuiltInRegistries.BLOCK, PALEOGOURD_BLOCK_ID);
    public static final ResourceLocation PALEOGOURD_SEED_ID = PrimordialHarvest.id("paleogourd_seeds");
    public static final Supplier<Item> PALEOGOURD_SEED = CachedSupplier.of(BuiltInRegistries.ITEM, PALEOGOURD_SEED_ID);
    public static final ResourceLocation PALEOGOURD_FRUIT_ID = PrimordialHarvest.id("paleogourd");
    public static final Supplier<Item> PALEOGOURD_FRUIT = CachedSupplier.of(BuiltInRegistries.ITEM, PALEOGOURD_FRUIT_ID);

    public static final ResourceLocation SPIRESHUCK_BLOCK_ID = PrimordialHarvest.id("spireshuck");
    public static final Supplier<Block> SPIRESHUCK_BLOCK = CachedSupplier.of(BuiltInRegistries.BLOCK, SPIRESHUCK_BLOCK_ID);
    public static final ResourceLocation SPIRESHUCK_SEED_ID = PrimordialHarvest.id("spireshuck_seeds");
    public static final Supplier<Item> SPIRESHUCK_SEED = CachedSupplier.of(BuiltInRegistries.ITEM, SPIRESHUCK_SEED_ID);
    public static final ResourceLocation SPIRESHUCK_FRUIT_ID = PrimordialHarvest.id("spireshuck");
    public static final Supplier<Item> SPIRESHUCK_FRUIT = CachedSupplier.of(BuiltInRegistries.ITEM, SPIRESHUCK_FRUIT_ID);

    public static final ResourceLocation RUMBLEPEA_BLOCK_ID = PrimordialHarvest.id("rumblepea");
    public static final Supplier<Block> RUMBLEPEA_BLOCK = CachedSupplier.of(BuiltInRegistries.BLOCK, RUMBLEPEA_BLOCK_ID);
    public static final ResourceLocation RUMBLEPEA_SEED_ID = PrimordialHarvest.id("rumblepea_seeds");
    public static final Supplier<Item> RUMBLEPEA_SEED = CachedSupplier.of(BuiltInRegistries.ITEM, RUMBLEPEA_SEED_ID);
    public static final ResourceLocation RUMBLEPEA_FRUIT_ID = PrimordialHarvest.id("rumblepea");
    public static final Supplier<Item> RUMBLEPEA_FRUIT = CachedSupplier.of(BuiltInRegistries.ITEM, RUMBLEPEA_FRUIT_ID);

    public static final ResourceLocation BLOOD_CAULDRON_BLOCK_ID = PrimordialHarvest.id("blood_cauldron");
    public static final CauldronInteraction.InteractionMap INTERACTIONS = CauldronInteraction.newInteractionMap(Content.BLOOD_CAULDRON_BLOCK_ID.toString());
    public static final Supplier<Block> BLOOD_CAULDRON_BLOCK = CachedSupplier.of(BuiltInRegistries.BLOCK, BLOOD_CAULDRON_BLOCK_ID);

    public static final TagKey<Item> PHILOSOPHER_REAGENTS = TagKey.create(Registries.ITEM, PrimordialHarvest.id("philosopher_reagents"));

    @Override
    public String namespace() {
        return PrimordialHarvest.MOD_ID;
    }

    @Override
    public void defineCreativeTabs(CreativeModeTabAdapter registry) {
        registry.add("items", () -> PALEOGOURD_FRUIT.get().getDefaultInstance(), (params, out) -> {
            out.accept(PALEOGOURD_SEED.get());
            out.accept(PALEOGOURD_FRUIT.get());
            out.accept(SPIRESHUCK_SEED.get());
            out.accept(SPIRESHUCK_FRUIT.get());
            out.accept(RUMBLEPEA_SEED.get());
            out.accept(RUMBLEPEA_FRUIT.get());
        });
    }

    @Override
    public void defineBlocks(BlockRegistryAdapter registry) {
        registry.add(PALEOGOURD_BLOCK_ID.getPath(), new PaleogourdBlock());
        registry.add(SPIRESHUCK_BLOCK_ID.getPath(), new SpireshuckBlock());
        registry.add(RUMBLEPEA_BLOCK_ID.getPath(), new RumblepeaBlock());
        registry.add(BLOOD_CAULDRON_BLOCK_ID.getPath(), new BloodCauldronBlock());
    }

    @Override
    public void defineItems(GameRegistryAdapter<Item> registry) {
        registry.add(PALEOGOURD_SEED_ID.getPath(), new ItemNameBlockItem(PALEOGOURD_BLOCK.get(), new Item.Properties()));
        registry.add(PALEOGOURD_FRUIT_ID.getPath(), new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.6f).effect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 300), 0.1f).build())));
        registry.add(SPIRESHUCK_SEED_ID.getPath(), new ItemNameBlockItem(SPIRESHUCK_BLOCK.get(), new Item.Properties()));
        registry.add(SPIRESHUCK_FRUIT_ID.getPath(), new RumblepeaFruitItem(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.2f).build())));
        registry.add(RUMBLEPEA_SEED_ID.getPath(), new ItemNameBlockItem(RUMBLEPEA_BLOCK.get(), new Item.Properties()));
        registry.add(RUMBLEPEA_FRUIT_ID.getPath(), new RumblepeaFruitItem(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.4f).build())));
        addBloodRecipe(Items.COPPER_INGOT, new ItemStack(Items.GOLD_INGOT));
    }

    @Override
    public void definePotions(GameRegistryAdapter<Potion> registry) {
        registry.add(PrimordialHarvest.MOD_ID + ".resistance", new Potion(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 1800)));
        registry.add(PrimordialHarvest.MOD_ID + ".long_resistance", new Potion(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 4800)));
        registry.add(PrimordialHarvest.MOD_ID + ".strong_resistance", new Potion(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 1800, 1)));
    }

    @Override
    public void defineBrews(PotionBrewing.Builder registry) {
        final Holder<Potion> resistance = BuiltInRegistries.POTION.getHolderOrThrow(ResourceKey.create(Registries.POTION, PrimordialHarvest.id(PrimordialHarvest.MOD_ID + ".resistance")));
        final Holder<Potion> long_resistance = BuiltInRegistries.POTION.getHolderOrThrow(ResourceKey.create(Registries.POTION, PrimordialHarvest.id(PrimordialHarvest.MOD_ID + ".long_resistance")));
        final Holder<Potion> strong_resistance = BuiltInRegistries.POTION.getHolderOrThrow(ResourceKey.create(Registries.POTION, PrimordialHarvest.id(PrimordialHarvest.MOD_ID + ".strong_resistance")));
        registry.addStartMix(PALEOGOURD_FRUIT.get(), resistance);
        registry.addMix(resistance, Items.REDSTONE, long_resistance);
        registry.addMix(resistance, Items.GLOWSTONE_DUST, strong_resistance);
        registry.addStartMix(SPIRESHUCK_FRUIT.get(), Potions.WEAKNESS);
    }

    @Override
    public void defineLootPoolAdditions(LootPoolAdditionAdapter registry) {
        registry.add(PALEOGOURD_SEED_ID.getPath(), PoolTarget.SNIFFER_DIGGING, PALEOGOURD_SEED.get(), 1);
        registry.add(SPIRESHUCK_SEED_ID.getPath(), PoolTarget.SNIFFER_DIGGING, SPIRESHUCK_SEED.get(), 1);
        registry.add(RUMBLEPEA_SEED_ID.getPath(), PoolTarget.SNIFFER_DIGGING, RUMBLEPEA_SEED.get(), 1);
    }

    @Override
    public void defineBlockRenderTypes(BlockRenderTypeAdapter registry) {
        registry.add(PALEOGOURD_BLOCK.get(), RenderType.cutout());
        registry.add(SPIRESHUCK_BLOCK.get(), RenderType.cutout());
        registry.add(RUMBLEPEA_BLOCK.get(), RenderType.cutout());
        registry.add(BLOOD_CAULDRON_BLOCK.get(), RenderType.cutout());
    }

    public static void addBloodRecipe(Item input, ItemStack output) {
        INTERACTIONS.map().put(input, (state, level, pos, player, hand, inputStack) -> {
            if (!level.isClientSide) {
                if (player != null) {
                    player.awardStat(Stats.USE_CAULDRON);
                    player.awardStat(Stats.ITEM_USED.get(inputStack.getItem()));
                }
                inputStack.shrink(1);
                Block.popResource(level, pos, output.copy());
                LayeredCauldronBlock.lowerFillLevel(state, level, pos);
                level.playSound(null, pos, SoundEvents.GENERIC_SPLASH, SoundSource.BLOCKS, 1f, 1f);
                level.gameEvent(null, GameEvent.SPLASH, pos);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        });
    }
}
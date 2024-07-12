package mod.syconn.swe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import mod.syconn.swe.blockentities.*;
import mod.syconn.swe.blocks.*;
import mod.syconn.swe.fluids.BaseFluidType;
import mod.syconn.swe.fluids.O2Fluid;
import mod.syconn.swe.items.*;
import mod.syconn.swe.world.container.CollectorMenu;
import mod.syconn.swe.world.container.DisperserMenu;
import mod.syconn.swe.world.container.PipeMenu;
import mod.syconn.swe.world.container.TankMenu;
import mod.syconn.swe.world.crafting.DyedParachuteRecipe;
import mod.syconn.swe.world.crafting.RefillingCanisterRecipe;
import mod.syconn.swe.world.data.attachments.SpaceSuit;
import mod.syconn.swe.world.data.components.CanisterComponent;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathType;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.*;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static mod.syconn.swe.Main.MODID;
import static mod.syconn.swe.fluids.BaseFluidType.*;
import static net.minecraft.world.level.block.state.BlockBehaviour.simpleCodec;

public class Registration {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS = DeferredRegister.create(BuiltInRegistries.ARMOR_MATERIAL, MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, MODID);
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(BuiltInRegistries.MENU, MODID);
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, MODID);
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, Main.MODID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(BuiltInRegistries.FLUID, Main.MODID);
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MODID);
    public static final DeferredRegister.DataComponents COMPONENTS = DeferredRegister.createDataComponents(MODID);
    public static final DeferredRegister<MapCodec<? extends Block>> BLOCK_TYPES = DeferredRegister.create(BuiltInRegistries.BLOCK_TYPE, MODID);

    public static Supplier<FluidType> O2_FLUID_TYPE = FLUID_TYPES.register("oxygen", () -> new BaseFluidType(O2_STILL_RL, O2_FLOWING_RL, O2_OVERLAY_RL, -1, new Vector3f(68f / 255f, 149f / 255f, 168f / 255f), FluidType.Properties.create().descriptionId("swe.fluid.o2").canSwim(true).canExtinguish(false).canDrown(false)
            .pathType(PathType.WATER).sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY).sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH).lightLevel(1).density(15).viscosity(5)
    ));

    public static final Supplier<FlowingFluid> O2 = FLUIDS.register("oxygen", () -> new BaseFlowingFluid.Source(O2Fluid.PROPERTIES));
    public static final Supplier<FlowingFluid> O2_FLOWING = FLUIDS.register("oxygen_flowing", () -> new BaseFlowingFluid.Flowing(O2Fluid.PROPERTIES));

    public static final DeferredItem<Parachute> PARACHUTE = ITEMS.register("parachute", Parachute::new);
    public static final DeferredItem<SpaceArmor> SPACE_HELMET = ITEMS.register("space_helmet", () -> new SpaceArmor(ArmorItem.Type.HELMET));
    public static final DeferredItem<SpaceArmor> SPACE_CHESTPLATE = ITEMS.register("space_chestplate", () -> new SpaceArmor(ArmorItem.Type.CHESTPLATE));
    public static final DeferredItem<SpaceArmor> SPACE_LEGGINGS = ITEMS.register("space_leggings", () -> new SpaceArmor(ArmorItem.Type.LEGGINGS));
    public static final DeferredItem<SpaceArmor> SPACE_BOOTS = ITEMS.register("space_boots", () -> new SpaceArmor(ArmorItem.Type.BOOTS));
    public static final DeferredItem<Canister> CANISTER = ITEMS.register("canister", () -> new Canister(Rarity.UNCOMMON));
    public static final DeferredItem<Canister> AUTO_REFILL_CANISTER = ITEMS.register("auto_fill_canister", AutoRefillCanister::new);
    public static final DeferredItem<Wrench> WRENCH = ITEMS.register("wrench", Wrench::new);
    public static final DeferredItem<UpgradeItem> IRON_UPGRADE = ITEMS.register("iron_upgrade", () -> new UpgradeItem(new Item.Properties().stacksTo(1), 2));
    public static final DeferredItem<UpgradeItem> GOLD_UPGRADE = ITEMS.register("gold_upgrade", () -> new UpgradeItem(new Item.Properties().stacksTo(1), 5));
    public static final DeferredItem<UpgradeItem> DIAMOND_UPGRADE = ITEMS.register("diamond_upgrade", () -> new UpgradeItem(new Item.Properties().stacksTo(1), 10));
    public static final DeferredItem<UpgradeItem> EMERALD_UPGRADE = ITEMS.register("emerald_upgrade", () -> new UpgradeItem(new Item.Properties().stacksTo(1), 15));
    public static final DeferredItem<UpgradeItem> NETHERITE_UPGRADE = ITEMS.register("netherite_upgrade", () -> new UpgradeItem(new Item.Properties().stacksTo(1).fireResistant(), 25));
    public static final DeferredItem<BucketItem> O2_BUCKET = ITEMS.register("o2_fluid_bucket", () -> new BucketItem(O2.get(), new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));

    public static final DeferredBlock<OxygenCollector> OXYGEN_COLLECTOR = registerBlockAndItem("oxygen_collector", () -> new OxygenCollector(Blocks.IRON_BLOCK.properties().requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL)));
    public static final DeferredBlock<DispersibleAirBlock> OXYGEN_DISPERSIBLE = registerBlockAndItem("oxygen_dispersible", () -> new DispersibleAirBlock(Blocks.AIR.properties().noCollission().noLootTable().air().isViewBlocking((state, level, pos) -> false)));
    public static final DeferredBlock<CanisterFiller> CANISTER_FILLER = registerBlockAndItem("canister_filler", () -> new CanisterFiller(Blocks.IRON_BLOCK.properties().requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL)));
    public static final DeferredBlock<OxygenDisperser> OXYGEN_DISPERSER = registerBlockAndItem("oxygen_disperser", () -> new OxygenDisperser(Blocks.IRON_BLOCK.properties().requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL)));
    public static final DeferredBlock<FluidPipe> FLUID_PIPE = registerBlockAndItem("fluid_pipe", () -> new FluidPipe(Blocks.IRON_BLOCK.properties().noOcclusion().dynamicShape()));
    public static final DeferredBlock<Block> FLUID_TANK = registerBlockAndItem("fluid_tank", () -> new FluidTank(Blocks.IRON_BLOCK.properties().requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL).noOcclusion()));
    public static final DeferredBlock<LiquidBlock> O2_FLUID_BLOCK = BLOCKS.register("oxygen", () -> new LiquidBlock(O2.get(), Blocks.LAVA.properties().lightLevel(blockstate -> 0)));

    public static final DeferredHolder<MapCodec<? extends Block>, MapCodec<CanisterFiller>> CANISTER_FILLER_CODEC = BLOCK_TYPES.register("canister_filler", () -> simpleCodec(CanisterFiller::new));
    public static final DeferredHolder<MapCodec<? extends Block>, MapCodec<FluidTank>> FLUID_TANK_CODEC = BLOCK_TYPES.register("fluid_tank", () -> simpleCodec(FluidTank::new));
    public static final DeferredHolder<MapCodec<? extends Block>, MapCodec<OxygenCollector>> OXYGEN_COLLECTOR_CODEC = BLOCK_TYPES.register("oxygen_collector", () -> simpleCodec(OxygenCollector::new));
    public static final DeferredHolder<MapCodec<? extends Block>, MapCodec<OxygenDisperser>> OXYGEN_DISPERSER_CODEC = BLOCK_TYPES.register("oxygen_disperser", () -> simpleCodec(OxygenDisperser::new));
    public static final DeferredHolder<MapCodec<? extends Block>, MapCodec<DispersibleAirBlock>> OXYGEN_CODEC = BLOCK_TYPES.register("oxygen_dispersible", () -> simpleCodec(DispersibleAirBlock::new));
    public static final DeferredHolder<MapCodec<? extends Block>, MapCodec<FluidPipe>> FLUID_PIPE_CODEC = BLOCK_TYPES.register("fluid_pipe", () -> simpleCodec(FluidPipe::new));

    public static final Holder<ArmorMaterial> SPACE_SUIT_MATERIAL = ARMOR_MATERIALS.register("space_suit", () -> new ArmorMaterial(SpaceArmor.DEFENSE, 20,
                    SoundEvents.ARMOR_EQUIP_GENERIC, () -> Ingredient.of(Tags.Items.INGOTS_IRON), List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(MODID, "space_suit"))),0,0));

    public static final Supplier<BlockEntityType<PipeBlockEntity>> PIPE = BLOCK_ENTITIES.register("pipe", () -> BlockEntityType.Builder.of(PipeBlockEntity::new, FLUID_PIPE.get()).build(null));
    public static final Supplier<BlockEntityType<TankBlockEntity>> TANK = BLOCK_ENTITIES.register("tank", () -> BlockEntityType.Builder.of(TankBlockEntity::new, FLUID_TANK.get()).build(null));
    public static final Supplier<BlockEntityType<CanisterFillerBlockEntity>> FILLER = BLOCK_ENTITIES.register("filler", () -> BlockEntityType.Builder.of(CanisterFillerBlockEntity::new, CANISTER_FILLER.get()).build(null));
    public static final Supplier<BlockEntityType<DisperserBlockEntity>> DISPERSER = BLOCK_ENTITIES.register("disperser", () -> BlockEntityType.Builder.of(DisperserBlockEntity::new, OXYGEN_DISPERSER.get()).build(null));
    public static final Supplier<BlockEntityType<AirBlockEntity>> AIR = BLOCK_ENTITIES.register("air", () -> BlockEntityType.Builder.of(AirBlockEntity::new, OXYGEN_DISPERSIBLE.get()).build(null));
    public static final Supplier<BlockEntityType<CollectorBlockEntity>> COLLECTOR = BLOCK_ENTITIES.register("collector", () -> BlockEntityType.Builder.of(CollectorBlockEntity::new, OXYGEN_COLLECTOR.get()).build(null));

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> NEXUS_TAB = TABS.register("nexus", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + MODID)).icon(() -> SPACE_HELMET.get().getDefaultInstance()).build());

    public static final Supplier<MenuType<TankMenu>> TANK_MENU = MENUS.register("tank", () -> IMenuTypeExtension.create((windowId, inv, data) -> new TankMenu(windowId, inv, data.readBlockPos())));
    public static final Supplier<MenuType<PipeMenu>> PIPE_MENU = MENUS.register("pipe_menu", () -> IMenuTypeExtension.create((windowId, inv, data) -> new PipeMenu(windowId, inv, data.readBlockPos())));
    public static final Supplier<MenuType<DisperserMenu>> DISPERSER_MENU = MENUS.register("disperser_menu", () -> IMenuTypeExtension.create((windowId, inv, data) -> new DisperserMenu(windowId, inv, data.readBlockPos())));
    public static final Supplier<MenuType<CollectorMenu>> COLLECTOR_MENU = MENUS.register("collector_menu", () -> IMenuTypeExtension.create((windowId, inv, data) -> new CollectorMenu(windowId, inv, data.readBlockPos())));

    public static final Supplier<AttachmentType<SpaceSuit>> SPACE_SUIT = ATTACHMENT_TYPES.register("space_suit", () -> AttachmentType.serializable(SpaceSuit::new).build());

    public static final Supplier<SimpleCraftingRecipeSerializer<DyedParachuteRecipe>> PARACHUTE_RECIPE = RECIPE_SERIALIZERS.register("parachute_recipe", () -> new SimpleCraftingRecipeSerializer<>(DyedParachuteRecipe::new));
    public static final Supplier<SimpleCraftingRecipeSerializer<RefillingCanisterRecipe>> REFILLING_CANISTER = RECIPE_SERIALIZERS.register("refilling_canister_recipe", () -> new SimpleCraftingRecipeSerializer<>(RefillingCanisterRecipe::new));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CanisterComponent>> CANISTER_COMPONENT = COMPONENTS.registerComponentType("canister", builder -> builder.persistent(CanisterComponent.BASIC_CODEC).networkSynchronized(CanisterComponent.BASIC_STREAM_CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> MODE_COMPONENT = COMPONENTS.registerComponentType("exporter", builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL));

    public static final ResourceLocation MOON = ResourceLocation.fromNamespaceAndPath(Main.MODID, "moon");

    public static final ResourceKey<DamageType> ANOXIA = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(Main.MODID, "anoxia"));
    public static final ResourceKey<Level> MOON_KEY = ResourceKey.create(Registries.DIMENSION, MOON);

    public static final TagKey<Item> GLASS = ItemTags.create(ResourceLocation.fromNamespaceAndPath(Main.MODID, "glass"));
    public static final TagKey<Block> O2_PRODUCING = BlockTags.create(ResourceLocation.fromNamespaceAndPath(Main.MODID, "o2_producing"));
    public static final TagKey<Block> INFINBURN_MOON = BlockTags.create(ResourceLocation.fromNamespaceAndPath(Main.MODID, "infinburn_moon"));
    public static final TagKey<Fluid> OXYGEN = FluidTags.create(Main.loc("oxygen"));

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, DISPERSER.get(), (o, v) -> o.getFluidHandler());
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, COLLECTOR.get(), (o, v) -> o.getFluidHandler());
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, TANK.get(), (o, v) -> o.getFluidHandler());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, TANK.get(), (o, v) -> o.getItemHandler());
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, PIPE.get(), (o, v) -> o.getFluidHandler());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, PIPE.get(), (o, v) -> o.getItemHandler());
    }

    private static <BLOCK extends Block> DeferredBlock<BLOCK> registerBlockAndItem(String id, Supplier<BLOCK> blockSupplier) {
        return registerBlockAndItem(id, blockSupplier, block1 -> new BlockItem(block1, new Item.Properties()));
    }

    private static <BLOCK extends Block, ITEM extends BlockItem> DeferredBlock<BLOCK> registerBlockAndItem(String name, Supplier<BLOCK> blockFactory, Function<? super BLOCK,ITEM> itemFactory) {
        DeferredBlock<BLOCK> block = BLOCKS.register(name, blockFactory);
        DeferredHolder<Item, ITEM> item = ITEMS.register(name, () -> itemFactory.apply(block.get()));
        return block;
    }

    public static void addCreative(BuildCreativeModeTabContentsEvent e) {
        if (e.getTabKey() == NEXUS_TAB.getKey()) {
            for (DyeColor c : DyeColor.values()) e.accept(DyedItemColor.applyDyes(new ItemStack(PARACHUTE.get()), List.of(DyeItem.byColor(c))));
            List<ItemStack> delayed = new ArrayList<>();
            delayed.add(Canister.create(8000, 8000, Fluids.LAVA, CANISTER.get()));
            delayed.add(Canister.create(8000, 8000, Fluids.LAVA, AUTO_REFILL_CANISTER.get()));
            delayed.add(Canister.create(8000, 8000, Fluids.WATER, CANISTER.get()));
            delayed.add(Canister.create(8000, 8000, Fluids.WATER, AUTO_REFILL_CANISTER.get()));
            for (DeferredHolder<Item, ? extends Item> i : ITEMS.getEntries()){
                if (i.get() instanceof BlockItem bi && bi.getBlock() instanceof FluidPipe) continue;
                if (i.get() instanceof Parachute || i.get() instanceof Canister) continue;
                if (i.get() instanceof BlockItem bi && bi.getBlock() instanceof DispersibleAirBlock) continue;
                if (i.get() instanceof BucketItem b) {
                    delayed.add(Canister.create(8000, 8000, b.content, CANISTER.get()));
                    delayed.add(Canister.create(8000, 8000, b.content, AUTO_REFILL_CANISTER.get()));
                }
                e.accept(i.get());
            }
            delayed.add(Canister.create(0, 8000, Fluids.EMPTY, CANISTER.get()));
            delayed.add(Canister.create(0, 8000, Fluids.EMPTY, AUTO_REFILL_CANISTER.get()));
            delayed.forEach(e::accept);
        }
    }
}
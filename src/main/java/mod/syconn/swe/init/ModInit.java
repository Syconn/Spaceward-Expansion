package mod.syconn.swe.init;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.*;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import mod.syconn.swe.block.*;
import mod.syconn.swe.item.*;
import mod.syconn.swe.util.Dyeable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static mod.syconn.swe.Main.MODID;

public class ModInit {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);

    public static final RegistryObject<Parachute> PARACHUTE = ITEMS.register("parachute", Parachute::new);
    public static final RegistryObject<SpaceArmor> SPACE_HELMET = ITEMS.register("space_helmet", () -> new SpaceArmor(ArmorItem.Type.HELMET, new Item.Properties().defaultDurability(200)));
    public static final RegistryObject<SpaceArmor> SPACE_CHESTPLATE = ITEMS.register("space_chestplate", () -> new SpaceArmor(ArmorItem.Type.CHESTPLATE, new Item.Properties().defaultDurability(200)));
    public static final RegistryObject<SpaceArmor> SPACE_LEGGINGS = ITEMS.register("space_leggings", () -> new SpaceArmor(ArmorItem.Type.LEGGINGS, new Item.Properties().defaultDurability(200)));
    public static final RegistryObject<SpaceArmor> SPACE_BOOTS = ITEMS.register("space_boots", () -> new SpaceArmor(ArmorItem.Type.BOOTS, new Item.Properties().defaultDurability(200)));
    public static final RegistryObject<Canister> CANISTER = ITEMS.register("canister", () -> new Canister(Rarity.UNCOMMON));
    public static final RegistryObject<Canister> AUTO_REFILL_CANISTER = ITEMS.register("auto_fill_canister", AutoRefillCanister::new);
    public static final RegistryObject<Wrench> WRENCH = ITEMS.register("wrench", Wrench::new);
    public static final RegistryObject<UpgradeItem> IRON_UPGRADE = ITEMS.register("iron_upgrade", () -> new UpgradeItem(new Item.Properties().stacksTo(1), 2));
    public static final RegistryObject<UpgradeItem> GOLD_UPGRADE = ITEMS.register("gold_upgrade", () -> new UpgradeItem(new Item.Properties().stacksTo(1), 5));
    public static final RegistryObject<UpgradeItem> DIAMOND_UPGRADE = ITEMS.register("diamond_upgrade", () -> new UpgradeItem(new Item.Properties().stacksTo(1), 10));
    public static final RegistryObject<UpgradeItem> EMERALD_UPGRADE = ITEMS.register("emerald_upgrade", () -> new UpgradeItem(new Item.Properties().stacksTo(1), 15));
    public static final RegistryObject<UpgradeItem> NETHERITE_UPGRADE = ITEMS.register("netherite_upgrade", () -> new UpgradeItem(new Item.Properties().stacksTo(1).fireResistant(), 25));
    public static final RegistryObject<BucketItem> O2_BUCKET = ITEMS.register("o2_fluid_bucket", () -> new BucketItem(ModFluids.SOURCE_O2_FLUID, new Item.Properties().stacksTo(1)));

    public static final RegistryObject<OxygenCollector> OXYGEN_COLLECTOR = register("oxygen_collector", OxygenCollector::new);
    public static final RegistryObject<DispersibleAirBlock> OXYGEN = register("oxygen", DispersibleAirBlock::new);
    public static final RegistryObject<CanisterFiller> CANISTER_FILLER = register("canister_filler", CanisterFiller::new);
    public static final RegistryObject<OxygenDisperser> OXYGEN_DISPERSER = register("oxygen_disperser", OxygenDisperser::new);
    public static final RegistryObject<FluidPipe> FLUID_PIPE = register("fluid_pipe", FluidPipe::new);
    public static final RegistryObject<Block> FLUID_TANK = register("fluid_tank", FluidTank::new);
    public static final RegistryObject<LiquidBlock> O2_FLUID_BLOCK = BLOCKS.register("o2_fluid_block", () -> new LiquidBlock(ModFluids.SOURCE_O2_FLUID, BlockBehaviour.Properties.copy(Blocks.WATER)));

    private static <T extends Block> RegistryObject<T> register(String id, Supplier<T> blockSupplier)
    {
        return register(id, blockSupplier, block1 -> new BlockItem(block1, new Item.Properties()));
    }

    private static <T extends Block> RegistryObject<T> register(String id, Supplier<T> blockSupplier, @Nullable Function<T, BlockItem> supplier)
    {
        RegistryObject<T> registryObject = BLOCKS.register(id, blockSupplier);
        if(supplier != null)
        {
            ITEMS.register(id, () -> supplier.apply(registryObject.get()));
        }
        return registryObject;
    }

    public static void addItems(CreativeModeTab.Output e){
        for (DyeColor c : DyeColor.values()){
            ItemStack s = new ItemStack(ModInit.PARACHUTE.get());
            Dyeable.setColor(s, c.getFireworkColor());
            e.accept(s);
        }
        List<ItemStack> delayed = new ArrayList<>();
        delayed.add(Canister.create(8000, 8000, Fluids.LAVA, ModInit.CANISTER.get()));
        delayed.add(Canister.create(8000, 8000, Fluids.LAVA, ModInit.AUTO_REFILL_CANISTER.get()));
        delayed.add(Canister.create(8000, 8000, Fluids.WATER, ModInit.CANISTER.get()));
        delayed.add(Canister.create(8000, 8000, Fluids.WATER, ModInit.AUTO_REFILL_CANISTER.get()));
        for (RegistryObject<Item> i : ModInit.ITEMS.getEntries()){
            if (i.get() instanceof Parachute || i.get() instanceof Canister) continue;
            if (i.get() instanceof BlockItem bi && bi.getBlock() instanceof DispersibleAirBlock) continue;
            if (i.get() instanceof BucketItem b) {
                delayed.add(Canister.create(8000, 8000, b.getFluid(), ModInit.CANISTER.get()));
                delayed.add(Canister.create(8000, 8000, b.getFluid(), ModInit.AUTO_REFILL_CANISTER.get()));
            }
            e.accept(i.get());
        }
        delayed.add(Canister.create(0, 8000, Fluids.EMPTY, ModInit.CANISTER.get()));
        delayed.add(Canister.create(0, 8000, Fluids.EMPTY, ModInit.AUTO_REFILL_CANISTER.get()));
        delayed.forEach(e::accept);
    }

    public static boolean always(BlockState p_50775_, BlockGetter p_50776_, BlockPos p_50777_) {
        return true;
    }

    public static boolean never(BlockState p_50806_, BlockGetter p_50807_, BlockPos p_50808_) {
        return false;
    }
}

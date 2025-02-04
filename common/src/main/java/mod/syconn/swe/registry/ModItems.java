package mod.syconn.swe.registry;

import dev.architectury.fluid.FluidStack;
import dev.architectury.registry.CreativeTabOutput;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import mod.syconn.swe.Constants;
import mod.syconn.swe.common.items.*;
import mod.syconn.swe.util.FluidUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.*;
import net.minecraft.world.level.material.Fluids;

import java.util.List;
import java.util.function.Supplier;

import static mod.syconn.swe.Constants.MOD;
import static mod.syconn.swe.registry.ModBlocks.*;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MOD, Registries.ITEM);
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(MOD, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<Parachute> PARACHUTE = register("parachute", Parachute::new);
    public static final RegistrySupplier<SpaceArmor> SPACE_HELMET = register("space_helmet", () -> new SpaceArmor(ArmorItem.Type.HELMET));
    public static final RegistrySupplier<SpaceArmor> SPACE_CHESTPLATE = register("space_chestplate", () -> new SpaceArmor(ArmorItem.Type.CHESTPLATE));
    public static final RegistrySupplier<SpaceArmor> SPACE_LEGGINGS = register("space_leggings", () -> new SpaceArmor(ArmorItem.Type.LEGGINGS));
    public static final RegistrySupplier<SpaceArmor> SPACE_BOOTS = register("space_boots", () -> new SpaceArmor(ArmorItem.Type.BOOTS));
    public static final RegistrySupplier<Canister> CANISTER = register("canister", () -> new Canister(Rarity.UNCOMMON));
    public static final RegistrySupplier<Canister> AUTO_REFILL_CANISTER = register("auto_fill_canister", AutoRefillCanister::new);
    public static final RegistrySupplier<UpgradeItem> IRON_UPGRADE = register("iron_upgrade", () -> new UpgradeItem(new Item.Properties().stacksTo(1), 2));
    public static final RegistrySupplier<UpgradeItem> GOLD_UPGRADE = register("gold_upgrade", () -> new UpgradeItem(new Item.Properties().stacksTo(1), 5));
    public static final RegistrySupplier<UpgradeItem> DIAMOND_UPGRADE = register("diamond_upgrade", () -> new UpgradeItem(new Item.Properties().stacksTo(1), 10));
    public static final RegistrySupplier<UpgradeItem> EMERALD_UPGRADE = register("emerald_upgrade", () -> new UpgradeItem(new Item.Properties().stacksTo(1), 15));
    public static final RegistrySupplier<UpgradeItem> NETHERITE_UPGRADE = register("netherite_upgrade", () -> new UpgradeItem(new Item.Properties().stacksTo(1).fireResistant(), 25));
    public static final RegistrySupplier<CreativeModeTab> TAB = TABS.register(Constants.withId("nexus"), () -> CreativeTabRegistry.create(Component.translatable("itemGroup." + MOD), () -> SPACE_HELMET.get().getDefaultInstance()));

    public static void addCreative(FeatureFlagSet flags, CreativeTabOutput output, boolean canUseGameMasterBlocks) {
        for (DyeColor c : DyeColor.values()) output.accept(DyeableLeatherItem.dyeArmor(new ItemStack(PARACHUTE.get()), List.of(DyeItem.byColor(c))));
        output.accept(SPACE_HELMET.get());
        output.accept(SPACE_CHESTPLATE.get());
        output.accept(SPACE_LEGGINGS.get());
        output.accept(SPACE_BOOTS.get());
        output.accept(IRON_UPGRADE.get());
        output.accept(GOLD_UPGRADE.get());
        output.accept(DIAMOND_UPGRADE.get());
        output.accept(EMERALD_UPGRADE.get());
        output.accept(NETHERITE_UPGRADE.get());
        output.accept(FluidUtil.createFluidItem(CANISTER.get(), FluidStack.empty()));
        output.accept(FluidUtil.createFluidItem(CANISTER.get(), FluidStack.create(Fluids.LAVA, 8000)));
        output.accept(FluidUtil.createFluidItem(CANISTER.get(), FluidStack.create(Fluids.WATER, 8000)));
        output.accept(FluidUtil.createFluidItem(CANISTER.get(), FluidStack.create(ModFluids.O2.get(), 8000)));
        output.accept(FluidUtil.createFluidItem(AUTO_REFILL_CANISTER.get(), FluidStack.empty()));
        output.accept(FluidUtil.createFluidItem(AUTO_REFILL_CANISTER.get(), FluidStack.create(Fluids.LAVA, 8000)));
        output.accept(FluidUtil.createFluidItem(AUTO_REFILL_CANISTER.get(), FluidStack.create(Fluids.WATER, 8000)));
        output.accept(FluidUtil.createFluidItem(AUTO_REFILL_CANISTER.get(), FluidStack.create(ModFluids.O2.get(), 8000)));
        output.accept(OXYGEN_COLLECTOR.get());
        output.accept(FLUID_TANK.get());
        output.accept(CANISTER_FILLER.get());
        output.accept(FLUID_PIPE.get());
    }

    private static <T extends Item> RegistrySupplier<T> register(String id, Supplier<T> itemSupplier) {
        return ITEMS.register(Constants.withId(id), itemSupplier);
    }
}

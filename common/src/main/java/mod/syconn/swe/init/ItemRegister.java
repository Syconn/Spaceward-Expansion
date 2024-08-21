package mod.syconn.swe.init;

import mod.syconn.swe.Constants;
import mod.syconn.swe.extra.platform.Services;
import mod.syconn.swe.items.*;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.material.Fluids;

import java.util.List;
import java.util.function.Supplier;

import static mod.syconn.swe.init.BlockRegister.*;

public class ItemRegister {

    public static final Supplier<Parachute> PARACHUTE = register("parachute", Parachute::new);
    public static final Supplier<SpaceArmor> SPACE_HELMET = register("space_helmet", () -> new SpaceArmor(ArmorItem.Type.HELMET));
    public static final Supplier<SpaceArmor> SPACE_CHESTPLATE = register("space_chestplate", () -> new SpaceArmor(ArmorItem.Type.CHESTPLATE));
    public static final Supplier<SpaceArmor> SPACE_LEGGINGS = register("space_leggings", () -> new SpaceArmor(ArmorItem.Type.LEGGINGS));
    public static final Supplier<SpaceArmor> SPACE_BOOTS = register("space_boots", () -> new SpaceArmor(ArmorItem.Type.BOOTS));
    public static final Supplier<Canister> CANISTER = register("canister", () -> new Canister(Rarity.UNCOMMON));
    public static final Supplier<Canister> AUTO_REFILL_CANISTER = register("auto_fill_canister", AutoRefillCanister::new);
    public static final Supplier<UpgradeItem> IRON_UPGRADE = register("iron_upgrade", () -> new UpgradeItem(new Item.Properties().stacksTo(1), 2));
    public static final Supplier<UpgradeItem> GOLD_UPGRADE = register("gold_upgrade", () -> new UpgradeItem(new Item.Properties().stacksTo(1), 5));
    public static final Supplier<UpgradeItem> DIAMOND_UPGRADE = register("diamond_upgrade", () -> new UpgradeItem(new Item.Properties().stacksTo(1), 10));
    public static final Supplier<UpgradeItem> EMERALD_UPGRADE = register("emerald_upgrade", () -> new UpgradeItem(new Item.Properties().stacksTo(1), 15));
    public static final Supplier<UpgradeItem> NETHERITE_UPGRADE = register("netherite_upgrade", () -> new UpgradeItem(new Item.Properties().stacksTo(1).fireResistant(), 25));
    public static final Supplier<BucketItem> LIQUID_OXYGEN_BUCKET = register("o2_fluid_bucket", () -> new BucketItem(FluidRegister.O2.get(), new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));

    public static final Supplier<CreativeModeTab> TAB = Services.REGISTRAR.registerCreativeModeTab("nexus", () -> Services.REGISTRAR.newCreativeTabBuilder()
            .title(Component.translatable("itemGroup." + Constants.MOD_ID)).icon(() -> SPACE_HELMET.get().getDefaultInstance()).displayItems(ItemRegister::addCreative).build());

    public static void addCreative(CreativeModeTab.ItemDisplayParameters pParameters, CreativeModeTab.Output pOutput) {
        for (DyeColor c : DyeColor.values()) pOutput.accept(DyedItemColor.applyDyes(new ItemStack(PARACHUTE.get()), List.of(DyeItem.byColor(c))));
        pOutput.accept(SPACE_HELMET.get());
        pOutput.accept(SPACE_CHESTPLATE.get());
        pOutput.accept(SPACE_LEGGINGS.get());
        pOutput.accept(SPACE_BOOTS.get());
        pOutput.accept(IRON_UPGRADE.get());
        pOutput.accept(GOLD_UPGRADE.get());
        pOutput.accept(DIAMOND_UPGRADE.get());
        pOutput.accept(EMERALD_UPGRADE.get());
        pOutput.accept(NETHERITE_UPGRADE.get());
//        pOutput.accept(LIQUID_OXYGEN_BUCKET.get());
        pOutput.accept(Canister.create(0, 8000, Fluids.EMPTY, CANISTER.get()));
        pOutput.accept(Canister.create(8000, 8000, Fluids.LAVA, CANISTER.get()));
        pOutput.accept(Canister.create(8000, 8000, Fluids.WATER, CANISTER.get()));
        pOutput.accept(Canister.create(8000, 8000, FluidRegister.O2.get(), CANISTER.get()));
        pOutput.accept(Canister.create(0, 8000, Fluids.EMPTY, AUTO_REFILL_CANISTER.get()));
        pOutput.accept(Canister.create(8000, 8000, Fluids.LAVA, AUTO_REFILL_CANISTER.get()));
        pOutput.accept(Canister.create(8000, 8000, Fluids.WATER, AUTO_REFILL_CANISTER.get()));
        pOutput.accept(Canister.create(8000, 8000, FluidRegister.O2.get(), AUTO_REFILL_CANISTER.get()));
        pOutput.accept(OXYGEN_COLLECTOR.get());
        pOutput.accept(FLUID_TANK.get());
        pOutput.accept(CANISTER_FILLER.get());
        pOutput.accept(FLUID_PIPE.get());
    }

    public static void init() {}

    private static <T extends Item> Supplier<T> register(String id, Supplier<T> item) {
        return Services.REGISTRAR.registerItem(id, item);
    }
}

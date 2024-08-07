package mod.syconn.swe.init;

import mod.syconn.swe.Constants;
import mod.syconn.swe.items.*;
import mod.syconn.swe.platform.Services;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluids;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

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
//    public static final Supplier<BucketItem> O2_BUCKET = register("o2_fluid_bucket", () -> new BucketItem(O2.get(), new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));

    public static final Holder<ArmorMaterial> SPACE_SUIT_MATERIAL = registerArmor("space_suit", () -> new ArmorMaterial(SpaceArmor.DEFENSE, 20,
            SoundEvents.ARMOR_EQUIP_GENERIC, () -> Ingredient.of(Items.IRON_INGOT), List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "space_suit"))),0,0));

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
        List<ItemStack> delayed = new ArrayList<>();
        delayed.add(Canister.create(8000, 8000, Fluids.LAVA, CANISTER.get()));
        delayed.add(Canister.create(8000, 8000, Fluids.LAVA, AUTO_REFILL_CANISTER.get()));
        delayed.add(Canister.create(8000, 8000, Fluids.WATER, CANISTER.get()));
        delayed.add(Canister.create(8000, 8000, Fluids.WATER, AUTO_REFILL_CANISTER.get()));
        delayed.add(Canister.create(0, 8000, Fluids.EMPTY, CANISTER.get()));
        delayed.add(Canister.create(0, 8000, Fluids.EMPTY, AUTO_REFILL_CANISTER.get()));
        delayed.forEach(pOutput::accept);
    }

    public static void init() {}

    private static <T extends Item> Supplier<T> register(String id, Supplier<T> item) {
        return Services.REGISTRAR.registerItem(id, item);
    }

    private static <T extends ArmorMaterial> Holder<T> registerArmor(String id, Supplier<T> material) {
        return Services.REGISTRAR.registerArmorMaterial(id, material);
    }
}

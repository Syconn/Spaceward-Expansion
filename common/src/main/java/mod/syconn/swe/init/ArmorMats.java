package mod.syconn.swe.init;

import mod.syconn.swe.Constants;
import mod.syconn.swe.extra.platform.Services;
import mod.syconn.swe.items.SpaceArmor;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;
import java.util.function.Supplier;

public class ArmorMats {

    public static final Holder<ArmorMaterial> SPACE_SUIT = registerArmor("space_suit", () -> new ArmorMaterial(SpaceArmor.DEFENSE, 20,
            SoundEvents.ARMOR_EQUIP_GENERIC, () -> Ingredient.of(Items.IRON_INGOT), List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "space_suit"))),0,0));

    public static void init() {}

    private static <T extends ArmorMaterial> Holder<T> registerArmor(String id, Supplier<T> material) {
        return Services.REGISTRAR.registerArmorMaterial(id, material);
    }
}

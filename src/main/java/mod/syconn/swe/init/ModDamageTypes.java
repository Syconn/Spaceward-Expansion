package mod.syconn.swe.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import mod.syconn.swe.Main;

public class ModDamageTypes {

    public static final ResourceKey<DamageType> ANOXIA = register("anoxia");

    private static ResourceKey<DamageType> register(String name)
    {
        return ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(Main.MODID, name));
    }
}

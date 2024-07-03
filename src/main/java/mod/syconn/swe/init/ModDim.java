package mod.syconn.swe.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import mod.syconn.swe.Main;

public class ModDim {

    public static final ResourceLocation MOON = new ResourceLocation(Main.MODID, "moon");
    public static final ResourceKey<Level> MOON_KEY = ResourceKey.create(Registries.DIMENSION, MOON);

    public static boolean onMoon(Entity e){
        return e.level.dimension().location().equals(MOON);
    }

    public static boolean on(Entity e, ResourceKey<Level> level){
        return e.level.dimension().equals(level);
    }
}

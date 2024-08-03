package mod.syconn.swe2.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import static mod.syconn.swe2.Registration.MOON;

public class DimensionHelper {

    public static boolean onMoon(Entity e){
        return e.level().dimension().location().equals(MOON);
    }

    public static boolean on(Entity e, ResourceKey<Level> level){
        return e.level().dimension().equals(level);
    }
}

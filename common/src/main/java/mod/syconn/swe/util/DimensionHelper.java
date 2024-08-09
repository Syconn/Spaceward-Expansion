package mod.syconn.swe.util;

import mod.syconn.swe.Constants;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class DimensionHelper {

    public static boolean onMoon(Entity e){
        return e.level().dimension().location().equals(Constants.MOON);
    }

    public static boolean on(Entity e, ResourceKey<Level> level){
        return e.level().dimension().equals(level);
    }
}

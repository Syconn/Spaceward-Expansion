package mod.syconn.swe2.world.dimensions;

import mod.syconn.swe2.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;

public class PlanetTraveler {

    public static DimensionTransition changePlanet(ServerLevel pLevel, Entity pEntity) {
        ResourceKey<Level> resourcekey = pLevel.dimension() == Level.OVERWORLD ? Registration.MOON_KEY : Level.OVERWORLD;
        ServerLevel serverlevel = pLevel.getServer().getLevel(resourcekey);
        if (serverlevel == null) return null;
        BlockPos blockpos = serverlevel.getSharedSpawnPos().offset(0, 30, 0);
        return new DimensionTransition(serverlevel, blockpos.getCenter(), pEntity.getDeltaMovement(), pEntity.getYRot(), pEntity.getXRot(), DimensionTransition.DO_NOTHING);
    }
}
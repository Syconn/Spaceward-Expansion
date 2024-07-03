package mod.syconn.swe.util.worldgen.dimension;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.util.ITeleporter;

import java.util.function.Function;

public class DimChanger implements ITeleporter {

    public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity)
    {
        return repositionEntity.apply(false); // TODO MAY NEED TO FIX TO GET TO PROPER PLACEMENT IN THE AIR
    }
}

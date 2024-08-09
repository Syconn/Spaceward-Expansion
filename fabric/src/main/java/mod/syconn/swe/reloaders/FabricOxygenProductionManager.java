package mod.syconn.swe.reloaders;

import mod.syconn.swe.Constants;
import mod.syconn.swe.common.dimensions.OxygenProductionManager;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;

public class FabricOxygenProductionManager extends OxygenProductionManager implements IdentifiableResourceReloadListener {

    public ResourceLocation getFabricId() {
        return Constants.loc("oxygen_production");
    }
}

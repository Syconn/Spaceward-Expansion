package mod.syconn.swe.reloaders;

import mod.syconn.swe.Constants;
import mod.syconn.swe.common.dimensions.PlanetManager;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;

public class FabricPlanetManager extends PlanetManager implements IdentifiableResourceReloadListener {

    public ResourceLocation getFabricId() {
        return Constants.loc("planet_settings");
    }
}

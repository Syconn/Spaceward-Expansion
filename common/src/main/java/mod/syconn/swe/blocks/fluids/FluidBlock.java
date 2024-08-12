package mod.syconn.swe.blocks.fluids;

import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;

public class FluidBlock extends LiquidBlock {

    public FluidBlock(FlowingFluid fluid, Properties properties) {
        super(fluid, properties);
    }
}

package mod.syconn.swe.common.blockentities;

import dev.architectury.fluid.FluidStack;
import mod.syconn.swe.registry.ModBlockEntities;
import mod.syconn.swe.registry.ModFluids;
import mod.syconn.swe.registry.ModTags;
import mod.syconn.swe.server.reloaders.OxygenProductionManager;
import mod.syconn.swe.server.reloaders.PlanetManager;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class CollectorBE extends AbstractTankBE {

    private int ticks = 0;
    private int rate = 0;

    public CollectorBE(BlockPos pos, BlockState state) {
        super(ModBlockEntities.COLLECTOR.get(), pos, state, 8000, 250);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, CollectorBE e) {
        e.ticks++;
        if (e.ticks >= 20) {
            e.ticks = 0;
            double total = 0;
            for (BlockPos blockPos : BlockPos.betweenClosed(pos.offset(11, 0, 11), pos.offset(-11, 11, -11)))
                if (level.getBlockState(blockPos).is(ModTags.Blocks.O2_PRODUCING)) total += OxygenProductionManager.getValue(level.getBlockState(blockPos));
            if (PlanetManager.getSettings(level.dimension()).breathable()) total += 186;
            e.tank.push(FluidStack.create(ModFluids.O2.get(), (int) total), false);
            e.rate = (int) total;
        }
        e.tank.handlePull(level, pos);
        e.tank.handlePush(level, pos);
        e.markDirty();
    }

    public void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt("rate", rate);
    }

    public void load(CompoundTag pTag) {
        super.load(pTag);
        rate = pTag.getInt("rate");
    }
}

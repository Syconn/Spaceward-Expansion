package mod.syconn.swe.fluids;

import mod.syconn.swe.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.FluidType;

import java.util.Optional;

public abstract class O2Fluid extends FlowingFluid {

    public Fluid getFlowing() {
        return Registration.O2_FLOWING.get();
    }

    public Fluid getSource() {
        return Registration.O2.get();
    }

    public Item getBucket() {
        return Registration.O2_BUCKET.get();
    }

    public Optional<SoundEvent> getPickupSound() {
        return Optional.of(SoundEvents.BUCKET_FILL);
    }

    protected ParticleOptions getDripParticle() {
        return ParticleTypes.DRIPPING_WATER;
    }

    protected boolean canConvertToSource(Level pLevel) {
        return false;
    }

    protected void beforeDestroyingBlock(LevelAccessor pLevel, BlockPos pPos, BlockState pState) {
        BlockEntity blockentity = pState.hasBlockEntity() ? pLevel.getBlockEntity(pPos) : null;
        Block.dropResources(pState, pLevel, pPos, blockentity);
    }

    protected int getSlopeFindDistance(LevelReader pLevel) {
        return 4;
    }

    protected int getDropOff(LevelReader pLevel) {
        return 1;
    }

    protected boolean canBeReplacedWith(FluidState pState, BlockGetter pLevel, BlockPos pPos, Fluid pFluid, Direction pDirection) {
        return pDirection == Direction.DOWN && !pFluid.is(FluidTags.WATER);
    }

    public int getTickDelay(LevelReader pLevel) {
        return 5;
    }

    protected float getExplosionResistance() {
        return 100.0F;
    }

    protected BlockState createLegacyBlock(FluidState pState) {
        return Registration.O2_FLUID_BLOCK.get().defaultBlockState().setValue(LiquidBlock.LEVEL, Integer.valueOf(getLegacyLevel(pState)));
    }

    public boolean isSource(FluidState pState) {
        return false;
    }

    public FluidType getFluidType() {
        return Registration.O2_FLUID_TYPE.get();
    }

    public boolean isSame(Fluid pFluid) {
        return pFluid == Registration.O2_FLOWING || pFluid == Registration.O2;
    }

    public static class Flowing extends O2Fluid {
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> pBuilder) {
            super.createFluidStateDefinition(pBuilder);
            pBuilder.add(LEVEL);
        }

        public int getAmount(FluidState pState) {
            return pState.getValue(LEVEL);
        }

        public boolean isSource(FluidState pState) {
            return false;
        }
    }

    public static class Source extends O2Fluid {
        public int getAmount(FluidState pState) {
            return 8;
        }

        public boolean isSource(FluidState pState) {
            return true;
        }
    }
}

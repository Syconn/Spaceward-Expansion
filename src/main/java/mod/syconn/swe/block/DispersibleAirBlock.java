package mod.syconn.swe.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import mod.syconn.swe.common.be.AirBlockEntity;
import mod.syconn.swe.init.ModBlockEntity;
import mod.syconn.swe.init.ModInit;
import mod.syconn.swe.util.config.Config;

public class DispersibleAirBlock extends BaseEntityBlock {

    public DispersibleAirBlock() {
        super(Properties.of(Material.AIR).noCollission().noLootTable().air().isViewBlocking(ModInit::never));
    }

    public VoxelShape getShape(BlockState p_48760_, BlockGetter p_48761_, BlockPos p_48762_, CollisionContext p_48763_) {
        return Shapes.empty();
    }

    public VoxelShape getVisualShape(BlockState p_48735_, BlockGetter p_48736_, BlockPos p_48737_, CollisionContext p_48738_) {
        return Shapes.empty();
    }

    public boolean skipRendering(BlockState p_53972_, BlockState p_53973_, Direction p_53974_) {
        return p_53973_.is(this) || super.skipRendering(p_53972_, p_53973_, p_53974_);
    }

    public float getShadeBrightness(BlockState p_48731_, BlockGetter p_48732_, BlockPos p_48733_) {
        return 1.0F;
    }

    public boolean propagatesSkylightDown(BlockState p_48740_, BlockGetter p_48741_, BlockPos p_48742_) {
        return true;
    }

    public BlockState updateShape(BlockState p_60541_, Direction p_60542_, BlockState p_60543_, LevelAccessor level, BlockPos p_60545_, BlockPos p_60546_) {
        if (!level.isClientSide()) level.getBlockEntity(p_60545_, ModBlockEntity.AIR.get()).get().blockUpdate();
        return super.updateShape(p_60541_, p_60542_, p_60543_, level, p_60545_, p_60546_);
    }

    public RenderShape getRenderShape(BlockState state) {
        if (Config.showOxygen.get()) return RenderShape.MODEL;
        return RenderShape.INVISIBLE;
    }

    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return !p_153212_.isClientSide ? createTickerHelper(p_153214_, ModBlockEntity.AIR.get(), AirBlockEntity::serverTick) : null;
    }

    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new AirBlockEntity(p_153215_, p_153216_);
    }
}

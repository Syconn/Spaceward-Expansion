package mod.syconn.swe.common.blocks;

import mod.syconn.swe.common.blockentities.CanisterFillerBlockEntity;
import mod.syconn.swe.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CanisterFillerBlock extends FluidBaseTopperBlock {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public CanisterFillerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) return InteractionResult.SUCCESS;
        ItemStack heldItem = player.getItemInHand(hand);
        if (level.getBlockEntity(pos) instanceof CanisterFillerBlockEntity ce) {
            if (heldItem.isEmpty()) {
                player.setItemInHand(hand, ce.removeCanister());
                return InteractionResult.CONSUME;
            } else if (ce.addCanister(heldItem)) {
                player.getItemInHand(hand).shrink(1);
                return InteractionResult.CONSUME;
            }
        }
        return InteractionResult.PASS;
    }

    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return !p_153212_.isClientSide ? createTickerHelper(p_153214_, ModBlockEntities.FILLER.get(), CanisterFillerBlockEntity::serverTick) : null;
    }

    public BlockState getStateForPlacement(BlockPlaceContext p_49820_) {
        return super.getStateForPlacement(p_49820_).setValue(FACING, p_49820_.getHorizontalDirection());
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        p_49915_.add(FACING);
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Block.box(1, 0, 1, 15, 14, 15);
    }

    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CanisterFillerBlockEntity(pos, state);
    }
}

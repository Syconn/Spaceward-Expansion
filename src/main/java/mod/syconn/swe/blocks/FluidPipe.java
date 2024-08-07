package mod.syconn.swe.blocks;


import com.mojang.serialization.MapCodec;
import mod.syconn.swe.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import mod.syconn.swe.blockentities.PipeBlockEntity;
import mod.syconn.swe.util.data.PipeModule;

// TODO CONVERT TO NEW PIPE NETWORK SYSTEM
@Deprecated(forRemoval = true)
public class FluidPipe extends FluidTransportBlock {

    public FluidPipe(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(ENABLED, true).setValue(NORTH, Boolean.FALSE).setValue(EAST, Boolean.FALSE).setValue(SOUTH, Boolean.FALSE).setValue(WEST, Boolean.FALSE).setValue(UP, Boolean.FALSE).setValue(DOWN, Boolean.FALSE).setValue(FLUID_TYPE, Boolean.FALSE));
    }

    public RenderShape getRenderShape(BlockState p_51307_) {
        return RenderShape.MODEL;
    }

    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext ctx) {
        return new PipeModule(state).getShape();
    }

    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        if (!ctx.getPlayer().level().isClientSide) return PipeModule.getStateForPlacement(super.getStateForPlacement(ctx), ctx.getClickedPos(), ctx.getLevel());
        return super.getStateForPlacement(ctx);
    }

    public BlockState updateShape(BlockState state, Direction dir, BlockState p_60543_, LevelAccessor level, BlockPos pos, BlockPos p_60546_) {
        if (level instanceof Level) PipeModule.updateBE((Level) level, level.getBlockEntity(pos, Registration.PIPE.get()).get()); // TODO WONT WORK MAYBE
        return PipeModule.getStateForPlacement(state, pos, level);
    }

    public void onRemove(BlockState p_60515_, Level l, BlockPos pos, BlockState p_60518_, boolean p_60519_) {
        if (p_60515_.hasBlockEntity() && (!p_60515_.is(p_60518_.getBlock()) || !p_60518_.hasBlockEntity()) && l.getBlockEntity(pos) instanceof PipeBlockEntity pe) pe.updateStates();
        if (!p_60515_.is(p_60518_.getBlock())) {
            BlockEntity blockentity = l.getBlockEntity(pos);
            if (blockentity instanceof PipeBlockEntity pe) {
                for (int i = 0; i < pe.getItems().getSlots(); i++) {
                    Containers.dropItemStack(l, pos.getX(), pos.getY(), pos.getZ(), pe.getItems().getStackInSlot(i));
                }
                l.updateNeighbourForOutputSignal(pos, this);
            }

            super.onRemove(p_60515_, l, pos, p_60518_, p_60519_);
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_53334_) {
        p_53334_.add(NORTH, EAST, WEST, SOUTH, UP, DOWN, FLUID_TYPE, ENABLED);
    }

    public InteractionResult use(BlockState state, Level l, BlockPos pos, Player p, InteractionHand hand, BlockHitResult result) {
        ItemStack stack = p.getItemInHand(hand);
        if (l.isClientSide) return InteractionResult.SUCCESS;
        else {
            BlockEntity blockentity = l.getBlockEntity(pos);
            if (p.getItemInHand(hand).getItem() == Registration.WRENCH.get() && blockentity instanceof PipeBlockEntity be) {
                double x = result.getLocation().x - pos.getX();
                double y = result.getLocation().y - pos.getY();
                double z = result.getLocation().z - pos.getZ();
                if (y > 0.7) {
                    if (Boolean.TRUE.equals(stack.get(Registration.MODE_COMPONENT)))
                        be.setImporter(new BlockPos(pos.relative(Direction.UP)));
                    else be.setExporter(new BlockPos(pos.relative(Direction.UP)));
                } else if (y < 0.3) {
                    if (Boolean.TRUE.equals(stack.get(Registration.MODE_COMPONENT)))
                        be.setImporter(new BlockPos(pos.relative(Direction.DOWN)));
                    else be.setExporter(new BlockPos(pos.relative(Direction.DOWN)));
                } else if (z < 0.69 && z > 0.3) {
                    if (x > 0.65) {
                        if (Boolean.TRUE.equals(stack.get(Registration.MODE_COMPONENT)))
                            be.setImporter(new BlockPos(pos.relative(Direction.EAST)));
                        else be.setExporter(new BlockPos(pos.relative(Direction.EAST)));
                    } else if (x < 0.31) {
                        if (Boolean.TRUE.equals(stack.get(Registration.MODE_COMPONENT)))
                            be.setImporter(new BlockPos(pos.relative(Direction.WEST)));
                        else be.setExporter(new BlockPos(pos.relative(Direction.WEST)));
                    }
                } else if (x < 0.69 && x > 0.3) {
                    if (z > 0.65) {
                        if (Boolean.TRUE.equals(stack.get(Registration.MODE_COMPONENT)))
                            be.setImporter(new BlockPos(pos.relative(Direction.SOUTH)));
                        else be.setExporter(new BlockPos(pos.relative(Direction.SOUTH)));
                    } else if (z < 0.31) {
                        if (Boolean.TRUE.equals(stack.get(Registration.MODE_COMPONENT)))
                            be.setImporter(new BlockPos(pos.relative(Direction.NORTH)));
                        else be.setExporter(new BlockPos(pos.relative(Direction.NORTH)));
                    }
                }
            }
            else if (blockentity instanceof PipeBlockEntity be && !be.getSystem().getPoints().isEmpty()) {
                p.openMenu((MenuProvider) blockentity, pos);
            }
        }
        return InteractionResult.PASS;
    }

    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PipeBlockEntity(pos, state);
    }

    protected MapCodec<? extends BaseEntityBlock> codec() {
        return Registration.FLUID_PIPE_CODEC.value();
    }

    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153182_, BlockState p_153183_, BlockEntityType<T> p_153184_) {
        return !p_153182_.isClientSide ? createTickerHelper(p_153184_, Registration.PIPE.get(), PipeBlockEntity::serverTick) : null;
    }
}

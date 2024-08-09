package mod.syconn.swe.api.blocks;

import mod.syconn.api.blockEntity.AbstractPipeBE;
import mod.syconn.api.util.PipeConnectionTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.ticks.ScheduledTick;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

import static mod.syconn.api.util.PipeConnectionTypes.*;

public abstract class AbstractPipeBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {

    public static final EnumProperty<PipeConnectionTypes> NORTH = EnumProperty.create("north", PipeConnectionTypes.class);
    public static final EnumProperty<PipeConnectionTypes> SOUTH = EnumProperty.create("south", PipeConnectionTypes.class);
    public static final EnumProperty<PipeConnectionTypes> WEST = EnumProperty.create("west", PipeConnectionTypes.class);
    public static final EnumProperty<PipeConnectionTypes> EAST = EnumProperty.create("east", PipeConnectionTypes.class);
    public static final EnumProperty<PipeConnectionTypes> UP = EnumProperty.create("up", PipeConnectionTypes.class);
    public static final EnumProperty<PipeConnectionTypes> DOWN = EnumProperty.create("down", PipeConnectionTypes.class);

    private static VoxelShape[] shapeCache = null;
    private static final VoxelShape SHAPE_CABLE_NORTH = Shapes.box(.3, .3, 0, .7, .7, .3);
    private static final VoxelShape SHAPE_CABLE_SOUTH = Shapes.box(.3, .3, .7, .7, .7, 1);
    private static final VoxelShape SHAPE_CABLE_WEST = Shapes.box(0, .3, .3, .3, .7, .7);
    private static final VoxelShape SHAPE_CABLE_EAST = Shapes.box(.7, .3, .3, 1, .7, .7);
    private static final VoxelShape SHAPE_CABLE_UP = Shapes.box(.3, .5, .3, .7, 1, .7);
    private static final VoxelShape SHAPE_CABLE_DOWN = Shapes.box(.3, 0, .3, .7, .7, .7);

    private static final VoxelShape SHAPE_BLOCK_NORTH = Shapes.box(.2, .2, 0, .8, .8, .1);
    private static final VoxelShape SHAPE_BLOCK_SOUTH = Shapes.box(.2, .2, .9, .8, .8, 1);
    private static final VoxelShape SHAPE_BLOCK_WEST = Shapes.box(0, .2, .2, .1, .8, .8);
    private static final VoxelShape SHAPE_BLOCK_EAST = Shapes.box(.9, .2, .2, 1, .8, .8);
    private static final VoxelShape SHAPE_BLOCK_UP = Shapes.box(.2, .9, .2, .8, 1, .8);
    private static final VoxelShape SHAPE_BLOCK_DOWN = Shapes.box(.2, 0, .2, .8, .1, .8);

    public AbstractPipeBlock(Properties properties) {
        super(properties);
        makeShapes();
        registerDefaultState(defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, false));
    }

    private int calculateShapeIndex(PipeConnectionTypes north, PipeConnectionTypes south, PipeConnectionTypes west, PipeConnectionTypes east, PipeConnectionTypes up, PipeConnectionTypes down) {
        int l = PipeConnectionTypes.values().length;
        return ((((south.ordinal() * l + north.ordinal()) * l + west.ordinal()) * l + east.ordinal()) * l + up.ordinal()) * l + down.ordinal();
    }

    private void makeShapes() {
        if (shapeCache == null) {
            int length = PipeConnectionTypes.values().length;
            shapeCache = new VoxelShape[length * length * length * length * length * length];
            for (PipeConnectionTypes up : PipeConnectionTypes.values()) {
                for (PipeConnectionTypes down : PipeConnectionTypes.values()) {
                    for (PipeConnectionTypes north : PipeConnectionTypes.values()) {
                        for (PipeConnectionTypes south : PipeConnectionTypes.values()) {
                            for (PipeConnectionTypes east : PipeConnectionTypes.values()) {
                                for (PipeConnectionTypes west : PipeConnectionTypes.values()) {
                                    int idx = calculateShapeIndex(north, south, west, east, up, down);
                                    shapeCache[idx] = makeShape(north, south, west, east, up, down);
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    private VoxelShape makeShape(PipeConnectionTypes north, PipeConnectionTypes south, PipeConnectionTypes west, PipeConnectionTypes east, PipeConnectionTypes up, PipeConnectionTypes down) {
        VoxelShape shape = Shapes.box(.3, .3, .3, .7, .7, .7);
        shape = combineShape(shape, north, SHAPE_CABLE_NORTH, SHAPE_BLOCK_NORTH);
        shape = combineShape(shape, south, SHAPE_CABLE_SOUTH, SHAPE_BLOCK_SOUTH);
        shape = combineShape(shape, west, SHAPE_CABLE_WEST, SHAPE_BLOCK_WEST);
        shape = combineShape(shape, east, SHAPE_CABLE_EAST, SHAPE_BLOCK_EAST);
        shape = combineShape(shape, up, SHAPE_CABLE_UP, SHAPE_BLOCK_UP);
        shape = combineShape(shape, down, SHAPE_CABLE_DOWN, SHAPE_BLOCK_DOWN);
        return shape;
    }

    private VoxelShape combineShape(VoxelShape shape, PipeConnectionTypes ConnectionType, VoxelShape cableShape, VoxelShape blockShape) {
        if (ConnectionType == CABLE) return Shapes.join(shape, cableShape, BooleanOp.OR);
//        else if (ConnectionType == INPUT || ConnectionType == OUTPUT) return Shapes.join(shape, Shapes.join(blockShape, cableShape, BooleanOp.OR), BooleanOp.OR);
        else if (ConnectionType == BLOCK) return Shapes.join(shape, Shapes.join(blockShape, cableShape, BooleanOp.OR), BooleanOp.OR);
        else return shape;
    }

    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter world, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        PipeConnectionTypes north = getConnectorType(world, pos, Direction.NORTH);
        PipeConnectionTypes south = getConnectorType(world, pos, Direction.SOUTH);
        PipeConnectionTypes west = getConnectorType(world, pos, Direction.WEST);
        PipeConnectionTypes east = getConnectorType(world, pos, Direction.EAST);
        PipeConnectionTypes up = getConnectorType(world, pos, Direction.UP);
        PipeConnectionTypes down = getConnectorType(world, pos, Direction.DOWN);
        int index = calculateShapeIndex(north, south, west, east, up, down);
        return shapeCache[index];
    }

    public BlockState updateShape(BlockState state, @Nonnull Direction direction, @Nonnull BlockState neighbourState, @Nonnull LevelAccessor world, @Nonnull BlockPos current, @Nonnull BlockPos offset) {
        if (state.getValue(BlockStateProperties.WATERLOGGED))
            world.getFluidTicks().schedule(new ScheduledTick<>(Fluids.WATER, current, Fluids.WATER.getTickDelay(world), 0L));
        return updateState(world, current, state, direction);
    }

    public void setPlacedBy(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nullable LivingEntity placer, @Nonnull ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof AbstractPipeBE cable) cable.markDirty();
        BlockState blockState = calculateState(level, pos, state);
        if (state != blockState) level.setBlockAndUpdate(pos, blockState);
    }

    protected PipeConnectionTypes getConnectorType(BlockGetter world, BlockPos connectorPos, Direction facing) {
        return getConnectorType(world, connectorPos, connectorPos.relative(facing), facing);
    }

    protected abstract PipeConnectionTypes getConnectorType(BlockGetter level, BlockPos thisPos, BlockPos connectionPos, Direction facing);

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BlockStateProperties.WATERLOGGED, NORTH, SOUTH, EAST, WEST, UP, DOWN);
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        return calculateState(world, pos, defaultBlockState()).setValue(BlockStateProperties.WATERLOGGED, world.getFluidState(pos).getType() == Fluids.WATER);
    }

    protected BlockState calculateState(LevelAccessor world, BlockPos pos, BlockState state) {
        PipeConnectionTypes north = getConnectorType(world, pos, Direction.NORTH);
        PipeConnectionTypes south = getConnectorType(world, pos, Direction.SOUTH);
        PipeConnectionTypes west = getConnectorType(world, pos, Direction.WEST);
        PipeConnectionTypes east = getConnectorType(world, pos, Direction.EAST);
        PipeConnectionTypes up = getConnectorType(world, pos, Direction.UP);
        PipeConnectionTypes down = getConnectorType(world, pos, Direction.DOWN);
        return state.setValue(NORTH, north).setValue(SOUTH, south).setValue(WEST, west).setValue(EAST, east).setValue(UP, up).setValue(DOWN, down);
    }

    protected BlockState updateState(LevelAccessor world, BlockPos pos, BlockState state, Direction direction) {
        return state.setValue(fromDirection(direction), getConnectorType(world, pos, direction));
    }

    public FluidState getFluidState(BlockState state) {
        return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    protected RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    public static EnumProperty<PipeConnectionTypes> fromDirection(Direction direction) {
        return switch (direction) {
            case DOWN -> DOWN;
            case UP -> UP;
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            case WEST -> WEST;
            case EAST -> EAST;
        };
    }
}


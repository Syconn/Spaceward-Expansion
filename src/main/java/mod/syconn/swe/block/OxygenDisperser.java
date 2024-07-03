package mod.syconn.swe.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.network.NetworkHooks;
import mod.syconn.swe.common.be.DisperserBlockEntity;
import mod.syconn.swe.init.ModBlockEntity;
import mod.syconn.swe.init.ModInit;
import mod.syconn.swe.util.FluidHelper;
import mod.syconn.swe.util.data.AirBubblesSavedData;

import java.util.UUID;

public class OxygenDisperser extends FluidBaseBlock {

    // TODO BUG CAN SEE OUT OF FLUID TANK
    
    public OxygenDisperser() {
        super(Properties.of(Material.METAL, MaterialColor.METAL).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL));
    }

    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    public InteractionResult use(BlockState p_60503_, Level p_60504_, BlockPos p_60505_, Player p_60506_, InteractionHand p_60507_, BlockHitResult p_60508_) {
        if (p_60504_.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            if(FluidUtil.interactWithFluidHandler(p_60506_, p_60507_, p_60504_, p_60505_, null)) {
                return InteractionResult.SUCCESS;
            }
            else if (FluidHelper.interactWithFluidHandler(p_60506_.getItemInHand(p_60507_), p_60504_, p_60505_, null)) {
                return InteractionResult.SUCCESS;
            }
            BlockEntity blockentity = p_60504_.getBlockEntity(p_60505_);
            if (blockentity instanceof DisperserBlockEntity) {
                NetworkHooks.openScreen((ServerPlayer) p_60506_, (MenuProvider) blockentity, p_60505_);
            }
            return InteractionResult.CONSUME;
        }
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return Block.box(2, 0, 2, 14, 12, 14);
    }

    public void onRemove(BlockState p_60515_, Level p_60516_, BlockPos p_60517_, BlockState p_60518_, boolean p_60519_) {
        if (p_60515_.hasBlockEntity() && (!p_60515_.is(p_60518_.getBlock()) || !p_60518_.hasBlockEntity()) && p_60516_.getBlockEntity(p_60517_) instanceof DisperserBlockEntity de) {
            AirBubblesSavedData.get().remove(p_60516_.dimension(), de.getUUID());
            for (BlockPos pos : de.list) {
                p_60516_.removeBlock(pos, false);
            }
        }
        super.onRemove(p_60515_, p_60516_, p_60517_, p_60518_, p_60519_);
    }

    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof DisperserBlockEntity be) {
            be.setUUID(UUID.randomUUID());
        }
    }

    public void tick(BlockState p_222945_, ServerLevel p_222946_, BlockPos p_222947_, RandomSource p_222948_) {
        if (p_222946_.getBlockEntity(p_222947_) instanceof DisperserBlockEntity de) de.failed(false);
    }

    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return !p_153212_.isClientSide ? createTickerHelper(p_153214_, ModBlockEntity.DISPERSER.get(), DisperserBlockEntity::serverTick) : null;
    }

    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new DisperserBlockEntity(p_153215_, p_153216_);
    }

    public static void addBlock(Level l, BlockPos target, BlockPos source, int distance){
        if (l.getBlockState(target).isAir() && !(l.getBlockState(target).getBlock() instanceof DispersibleAirBlock) && l.getBlockEntity(source, ModBlockEntity.DISPERSER.get()).isPresent()) {
            l.setBlock(target, ModInit.OXYGEN.get().defaultBlockState(), 2);
            l.getBlockEntity(source, ModBlockEntity.DISPERSER.get()).get().list.add(target);
            if (l.getBlockEntity(target, ModBlockEntity.AIR.get()).isPresent()) {
                l.getBlockEntity(target, ModBlockEntity.AIR.get()).get().setup(distance, source);
            }
        }
    }

    public static int maxFill(Level l, BlockPos pos) {
        return l.getBlockEntity(pos, ModBlockEntity.DISPERSER.get()).get().maxFill;
    }
}
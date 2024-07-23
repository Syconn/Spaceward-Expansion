package mod.syconn.swe.blocks;

import com.mojang.serialization.MapCodec;
import mod.syconn.swe.Registration;
import mod.syconn.swe.blockentities.DisperserBE;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import mod.syconn.swe.util.FluidHelper;
import mod.syconn.swe.world.data.savedData.AirBubblesSavedData;
import net.neoforged.neoforge.fluids.FluidUtil;

import java.util.UUID;

public class OxygenDisperser extends FluidBaseBlock {
    
    public OxygenDisperser(Properties properties) {
        super(properties);
    }

    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    protected ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if (!pLevel.isClientSide && FluidUtil.interactWithFluidHandler(pPlayer, pHand, pLevel, pPos, pHitResult.getDirection())) return ItemInteractionResult.CONSUME;
        return super.useItemOn(pStack, pState, pLevel, pPos, pPlayer, pHand, pHitResult);
    }

    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHitResult) {
        if (pLevel.isClientSide) return InteractionResult.SUCCESS;
        BlockEntity blockentity = pLevel.getBlockEntity(pPos);
        if (blockentity instanceof DisperserBE) {
            pPlayer.openMenu((MenuProvider) blockentity, pPos);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return Block.box(2, 0, 2, 14, 12, 14);
    }

    public void onRemove(BlockState p_60515_, Level p_60516_, BlockPos p_60517_, BlockState p_60518_, boolean p_60519_) {
        if (p_60515_.hasBlockEntity() && (!p_60515_.is(p_60518_.getBlock()) || !p_60518_.hasBlockEntity()) && p_60516_.getBlockEntity(p_60517_) instanceof DisperserBE de) {
            AirBubblesSavedData.get().remove(p_60516_.dimension(), de.getUUID());
            for (BlockPos pos : de.list) p_60516_.removeBlock(pos, false);
        }
        super.onRemove(p_60515_, p_60516_, p_60517_, p_60518_, p_60519_);
    }

    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof DisperserBE be) be.setUUID(UUID.randomUUID());
    }

    public void tick(BlockState p_222945_, ServerLevel p_222946_, BlockPos p_222947_, RandomSource p_222948_) {
        if (p_222946_.getBlockEntity(p_222947_) instanceof DisperserBE de) de.failed(false);
    }

    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return !p_153212_.isClientSide ? createTickerHelper(p_153214_, Registration.DISPERSER.get(), DisperserBE::serverTick) : null;
    }

    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new DisperserBE(p_153215_, p_153216_);
    }

    protected MapCodec<? extends BaseEntityBlock> codec() {
        return Registration.OXYGEN_DISPERSER_CODEC.value();
    }

    public static void addBlock(Level l, BlockPos target, BlockPos source, int distance){
        if (l.getBlockState(target).isAir() && !(l.getBlockState(target).getBlock() instanceof DispersibleAirBlock) && l.getBlockEntity(source, Registration.DISPERSER.get()).isPresent()) {
            l.setBlock(target, Registration.OXYGEN_DISPERSIBLE.get().defaultBlockState(), 2);
            l.getBlockEntity(source, Registration.DISPERSER.get()).get().list.add(target);
            if (l.getBlockEntity(target, Registration.AIR.get()).isPresent()) l.getBlockEntity(target, Registration.AIR.get()).get().setup(distance, source);
        }
    }

    public static int maxFill(Level l, BlockPos pos) {
        if (l.getBlockEntity(pos, Registration.DISPERSER.get()).isPresent()) return l.getBlockEntity(pos, Registration.DISPERSER.get()).get().maxFill;
        else return 20;
    }
}
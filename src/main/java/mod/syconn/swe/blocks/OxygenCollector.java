package mod.syconn.swe.blocks;

import com.mojang.serialization.MapCodec;
import mod.syconn.swe.Registration;
import mod.syconn.swe.blockentities.CollectorBlockEntity;
import mod.syconn.swe.util.FluidHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.fluids.FluidUtil;

public class OxygenCollector extends FluidBaseBlock {

    public OxygenCollector(Properties properties) {
        super(properties);
    }

    protected ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if (!pLevel.isClientSide) {
            if (FluidUtil.interactWithFluidHandler(pPlayer, pHand, pLevel, pPos, pHitResult.getDirection())) return ItemInteractionResult.CONSUME;
            else if (FluidHelper.interactWithFluidHandler(pPlayer.getItemInHand(pHand), pLevel, pPos, null)) return ItemInteractionResult.CONSUME;
        }
        return super.useItemOn(pStack, pState, pLevel, pPos, pPlayer, pHand, pHitResult);
    }

    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHitResult) {
        if (pLevel.isClientSide) return InteractionResult.SUCCESS;
        BlockEntity blockentity = pLevel.getBlockEntity(pPos);
        if (blockentity instanceof CollectorBlockEntity) {
            pPlayer.openMenu((MenuProvider) blockentity, pPos);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return !p_153212_.isClientSide ? createTickerHelper(p_153214_, Registration.COLLECTOR.get(), CollectorBlockEntity::tick) : null;
    }

    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new CollectorBlockEntity(p_153215_, p_153216_);
    }

    protected MapCodec<? extends BaseEntityBlock> codec() {
        return Registration.OXYGEN_COLLECTOR_CODEC.value();
    }
}

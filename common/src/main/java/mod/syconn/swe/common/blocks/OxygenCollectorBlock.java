package mod.syconn.swe.common.blocks;

import com.mojang.serialization.MapCodec;
import mod.syconn.swe.common.blockentities.CollectorBE;
import mod.syconn.swe.core.ModBlockEntities;
import mod.syconn.swe.core.ModBlocks;
import mod.syconn.swe.network.Network;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class OxygenCollectorBlock extends FluidBaseBlock {

    public OxygenCollectorBlock(Properties properties) {
        super(properties);
    }

    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHitResult) {
        if (pLevel.isClientSide) return InteractionResult.SUCCESS;
        if (pPlayer instanceof ServerPlayer sp && pLevel.getBlockEntity(pPos) instanceof CollectorBE collectorBE) {
            Network.openMenuWithData(sp, collectorBE, new PositionMenuData(pPos));
            return InteractionResult.CONSUME;
        }
        return InteractionResult.FAIL;
    }

    protected ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if (Services.FLUID_HANDLER.has(pStack) && Services.FLUID_HELPER.interactWithBlock(pLevel, pPos, pHitResult, pPlayer, pHand)) return ItemInteractionResult.CONSUME;
        return super.useItemOn(pStack, pState, pLevel, pPos, pPlayer, pHand, pHitResult);
    }

    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return !p_153212_.isClientSide ? createTickerHelper(p_153214_, ModBlockEntities.COLLECTOR.get(), CollectorBE::tick) : null;
    }

    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CollectorBE(pos, state);
    }

    protected MapCodec<? extends BaseEntityBlock> codec() {
        return ModBlocks.OXYGEN_COLLECTOR_CODEC.get();
    }
}

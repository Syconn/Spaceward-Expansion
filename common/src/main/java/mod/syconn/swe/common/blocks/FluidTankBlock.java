package mod.syconn.swe.common.blocks;

import dev.architectury.registry.menu.ExtendedMenuProvider;
import dev.architectury.registry.menu.MenuRegistry;
import mod.syconn.swe.common.blockentities.TankBE;
import mod.syconn.swe.common.items.FluidHolderItem;
import mod.syconn.swe.core.ModBlockEntities;
import mod.syconn.swe.core.ModMenus;
import mod.syconn.swe.util.FluidUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class FluidTankBlock extends BaseEntityBlock {

    public FluidTankBlock(Properties properties) {
        super(properties);
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) return InteractionResult.SUCCESS;
        if (FluidHolderItem.hasFluidHolder(player.getItemInHand(hand)) && FluidUtil.performPlayerTransfer(level, pos, hit, player, hand)) return InteractionResult.SUCCESS;
        if (player instanceof ServerPlayer serverPlayer && level.getBlockEntity(pos) instanceof TankBE) {
            MenuRegistry.openExtendedMenu(serverPlayer, new ExtendedMenuProvider() {
                public void saveExtraData(FriendlyByteBuf buf) {
                    buf.writeBlockPos(pos);
                }

                public Component getDisplayName() {
                    return Component.literal("Fluid Tank");
                }

                public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
                    return ModMenus.TANK_MENU.get().create(i, inventory);
                }
            });
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level l, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return createTickerHelper(p_153214_, ModBlockEntities.TANK.get(), TankBE::serverTick);
    }

    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TankBE(pos, state);
    }

    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    public int getAnalogOutputSignal(BlockState state, Level l, BlockPos pos) {
        if (l.getBlockEntity(pos) instanceof TankBE te) {
            double o = (double) (te.getFluidHolder().getFluidStack().getAmount()) / te.getFluidHolder().getCapacity();
            return (int) (o * 15);
        }
        return 0;
    }
}

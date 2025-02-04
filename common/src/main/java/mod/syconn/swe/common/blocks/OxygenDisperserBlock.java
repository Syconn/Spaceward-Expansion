package mod.syconn.swe.common.blocks;

import dev.architectury.registry.menu.ExtendedMenuProvider;
import dev.architectury.registry.menu.MenuRegistry;
import mod.syconn.swe.common.blockentities.DisperserBE;
import mod.syconn.swe.common.items.FluidHolderItem;
import mod.syconn.swe.registry.ModBlockEntities;
import mod.syconn.swe.registry.ModBlocks;
import mod.syconn.swe.registry.ModMenus;
import mod.syconn.swe.server.savedData.AirBubblesSavedData;
import mod.syconn.swe.util.FluidUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
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
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class OxygenDisperserBlock extends BaseEntityBlock {
    
    public OxygenDisperserBlock(Properties properties) {
        super(properties);
    }

    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) return InteractionResult.SUCCESS;
        if (FluidHolderItem.hasFluidHolder(player.getItemInHand(hand)) && FluidUtil.performPlayerTransfer(level, pos, hit, player, hand)) return InteractionResult.SUCCESS;
        if (player instanceof ServerPlayer serverPlayer && level.getBlockEntity(pos) instanceof DisperserBE) {
            MenuRegistry.openExtendedMenu(serverPlayer, new ExtendedMenuProvider() {
                public void saveExtraData(FriendlyByteBuf buf) {
                    buf.writeBlockPos(pos);
                }

                public Component getDisplayName() {
                    return Component.literal("Oxygen Disperser");
                }

                public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
                    return ModMenus.DISPERSER_MENU.get().create(i, inventory);
                }
            });
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return Block.box(2, 0, 2, 14, 12, 14);
    }

    public void onRemove(BlockState p_60515_, Level p_60516_, BlockPos p_60517_, BlockState p_60518_, boolean p_60519_) {
        if (p_60515_.hasBlockEntity() && (!p_60515_.is(p_60518_.getBlock()) || !p_60518_.hasBlockEntity()) && p_60516_.getBlockEntity(p_60517_) instanceof DisperserBE de) {
            if (p_60516_ instanceof ServerLevel sl) AirBubblesSavedData.get(sl).remove(p_60516_.dimension(), de.getUUID());
            for (BlockPos pos : de.list) p_60516_.removeBlock(pos, false);
        }
        super.onRemove(p_60515_, p_60516_, p_60517_, p_60518_, p_60519_);
    }

    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof DisperserBE be) be.setUUID(UUID.randomUUID());
    }

    public void tick(BlockState state, ServerLevel p_222946_, BlockPos p_222947_, RandomSource p_222948_) {
        if (p_222946_.getBlockEntity(p_222947_) instanceof DisperserBE de) de.failed(false);
    }

    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return !p_153212_.isClientSide ? createTickerHelper(p_153214_, ModBlockEntities.DISPERSER.get(), DisperserBE::serverTick) : null;
    }

    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new DisperserBE(p_153215_, p_153216_);
    }

    public static void addBlock(Level l, BlockPos target, BlockPos source, int distance){
        if (l.getBlockState(target).isAir() && !(l.getBlockState(target).getBlock() instanceof DispersedAirBlock) && l.getBlockEntity(source, ModBlockEntities.DISPERSER.get()).isPresent()) {
            l.setBlock(target, ModBlocks.DISPERSED_OXYGEN.get().defaultBlockState(), 2);
            l.getBlockEntity(source, ModBlockEntities.DISPERSER.get()).get().list.add(target);
            if (l.getBlockEntity(target, ModBlockEntities.AIR.get()).isPresent()) l.getBlockEntity(target, ModBlockEntities.AIR.get()).get().setup(distance, source);
        }
    }

    public static int maxFill(Level l, BlockPos pos) {
        if (l.getBlockEntity(pos, ModBlockEntities.DISPERSER.get()).isPresent()) return l.getBlockEntity(pos, ModBlockEntities.DISPERSER.get()).get().maxFill;
        else return 20;
    }
}
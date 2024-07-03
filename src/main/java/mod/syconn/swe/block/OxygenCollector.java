package mod.syconn.swe.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import mod.syconn.swe.common.be.CollectorBlockEntity;
import mod.syconn.swe.init.ModBlockEntity;

public class OxygenCollector extends FluidBaseBlock {

    public OxygenCollector() {
        super(Properties.of(Material.METAL, MaterialColor.METAL).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL));
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player p, InteractionHand hand, BlockHitResult result) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            BlockEntity blockentity = level.getBlockEntity(pos);
            if (blockentity instanceof CollectorBlockEntity) {
                NetworkHooks.openScreen((ServerPlayer) p, (MenuProvider) blockentity, pos);
            }
            return InteractionResult.CONSUME;
        }
    }

    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return !p_153212_.isClientSide ? createTickerHelper(p_153214_, ModBlockEntity.COLLECTOR.get(), CollectorBlockEntity::tick) : null;
    }

    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new CollectorBlockEntity(p_153215_, p_153216_);
    }
}

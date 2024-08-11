package mod.syconn.swe;

import mod.syconn.swe.capability.APICapabilities;
import mod.syconn.swe.data.savedData.PipeNetworks;
import mod.syconn.swe.common.CommonHandler;
import mod.syconn.swe.data.capability.SpaceSuitProvider;
import mod.syconn.swe.init.BlockEntityRegister;
import mod.syconn.swe.extra.Events;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ForgeCommon {

    @SubscribeEvent
    public static void entityTickEvent(LivingEvent.LivingTickEvent event){
        if (event.getEntity() instanceof LivingEntity livingEntity) CommonHandler.entityTickEvent(new Events.LivingEntityEvent(livingEntity));
    }

    @SubscribeEvent
    public static void fallDamageEvent(LivingFallEvent event) {
        Events.LivingFallEvent result = CommonHandler.livingFallEvent(new Events.LivingFallEvent(event.getEntity(), event.getDistance(), event.getDamageMultiplier(), false));
        event.setDistance(result.distance());
        event.setDamageMultiplier(result.damageMultiplier());
        event.setCanceled(result.cancel());
    }

    @SubscribeEvent
    public static void attachBlockEntityCapability(AttachCapabilitiesEvent<BlockEntity> event) {
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, BlockEntityRegister.COLLECTOR.get(), (o, v) -> o.getFluidHandler());
        event.registerBlockEntity(APICapabilities.FluidHandler.BLOCK, BlockEntityRegister.COLLECTOR.get(), (o, v) -> o.getFluidHandler());
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, BlockEntityRegister.TANK.get(), (o, v) -> o.getFluidHandler());
        event.registerBlockEntity(APICapabilities.FluidHandler.BLOCK, BlockEntityRegister.TANK.get(), (o, v) -> o.getFluidHandler());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BlockEntityRegister.TANK.get(), (o, v) -> o.getItemHandler());
    }

    @SubscribeEvent
    public static void attachPlayerCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player && !event.getObject().getCapability(SpaceSuitProvider.SPACE_SUIT).isPresent())
            event.addCapability(Constants.loc("space_suit"), new SpaceSuitProvider());
    }

    @SubscribeEvent
    public static void clonePlayer(PlayerEvent.Clone event) { // TODO FIGURE OUT
//        if (event.isWasDeath()) event.getEntity().getCapability(SpaceSuitProvider.SPACE_SUIT).ifPresent(); = event.getOriginal().getCapability(SpaceSuitProvider.SPACE_SUIT);
    }

//    @SubscribeEvent
//    public static void registerCapability(RegisterCapabilitiesEvent event) { // TODO Figure out a better work around
//        event.register(SpaceSuit.class);
//    }

    public static void levelTickEvent(TickEvent.LevelTickEvent.Pre event) {
        PipeNetworks.tickNetworks(new Events.LevelTick(event.level));
    }

    public static void playerTickEvent(TickEvent.PlayerTickEvent.Post event) {
        CommonHandler.playerTickEvent(new Events.PlayerEvent(event.player));
    }

    public static void playerJoined(PlayerEvent.PlayerLoggedInEvent event) {
        CommonHandler.playerJoined(new Events.PlayerEvent(event.getEntity()));
    }

    public static void playerLeft(PlayerEvent.PlayerLoggedOutEvent event) {
        CommonHandler.playerLeft(new Events.PlayerEvent(event.getEntity()));
    }

    public static void playerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        CommonHandler.playerChangedDimension(new Events.PlayerEvent(event.getEntity()));
    }
}

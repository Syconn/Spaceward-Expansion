package mod.syconn.swe;

import mod.syconn.swe.api.client.debug.PipeNetworkRenderer;
import mod.syconn.swe.api.world.data.capability.APICapabilities;
import mod.syconn.swe.common.CommonHandler;
import mod.syconn.swe.init.BlockEntityRegister;
import mod.syconn.swe.items.Parachute;
import mod.syconn.swe.items.SpaceArmor;
import mod.syconn.swe.items.extras.EquipmentItem;
import mod.syconn.swe.network.Channel;
import mod.syconn.swe.util.Events;
import mod.syconn.swe.util.DimensionHelper;
import mod.syconn.swe.common.data.attachments.SpaceSuit;
import mod.syconn.swe.common.dimensions.PlanetManager;
import mod.syconn.swe.common.dimensions.PlanetTraveler;
import mod.syconn.swe.common.inventory.ExtendedPlayerInventory;
import mod.syconn.swe.wrapper.ItemFluidHandlerWrapper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.portal.DimensionTransition;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import static mod.syconn.swe.init.ComponentRegister.FLUID_COMPONENT;
import static mod.syconn.swe.init.ItemRegister.AUTO_REFILL_CANISTER;
import static mod.syconn.swe.init.ItemRegister.CANISTER;

public class NeoCommon {

    @SubscribeEvent
    public static void entityTickEvent(EntityTickEvent.Pre event){
        if (event.getEntity() instanceof LivingEntity livingEntity) CommonHandler.entityTickEvent(new Events.LivingEntityEvent(livingEntity));
    }

    @SubscribeEvent
    public static void playerTickEvent(PlayerTickEvent.Pre event) {
        CommonHandler.playerTickEvent(new Events.PlayerEvent(event.getEntity()));
    }

    @SubscribeEvent
    public static void livingFallEvent(LivingFallEvent event) {
        Events.LivingFallEvent result = CommonHandler.livingFallEvent(new Events.LivingFallEvent(event.getEntity(), event.getDistance(), event.getDamageMultiplier(), false));
        event.setDistance(result.distance());
        event.setDamageMultiplier(result.damageMultiplier());
        event.setCanceled(result.cancel());
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(Capabilities.FluidHandler.ITEM, (stack, ctx) -> new ItemFluidHandlerWrapper(FLUID_COMPONENT, stack, 8000), CANISTER.get(), AUTO_REFILL_CANISTER.get());

        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, BlockEntityRegister.COLLECTOR.get(), (o, v) -> o.getFluidHandler());
        event.registerBlockEntity(APICapabilities.FluidHandler.BLOCK, BlockEntityRegister.COLLECTOR.get(), (o, v) -> o.getFluidHandler());
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, BlockEntityRegister.TANK.get(), (o, v) -> o.getFluidHandler());
        event.registerBlockEntity(APICapabilities.FluidHandler.BLOCK, BlockEntityRegister.TANK.get(), (o, v) -> o.getFluidHandler());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BlockEntityRegister.TANK.get(), (o, v) -> o.getItemHandler());
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

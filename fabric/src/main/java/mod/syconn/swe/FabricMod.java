package mod.syconn.swe;

import mod.syconn.swe.common.CommonHandler;
import mod.syconn.swe.events.EntityEvents;
import mod.syconn.swe.events.PlayerEvents;
import mod.syconn.swe.extra.core.Events;
import mod.syconn.swe.extra.data.savedData.PipeNetworks;
import mod.syconn.swe.init.BlockEntityRegister;
import mod.syconn.swe.init.ComponentRegister;
import mod.syconn.swe.items.Canister;
import mod.syconn.swe.network.Network;
import mod.syconn.swe.reloaders.FabricOxygenProductionManager;
import mod.syconn.swe.reloaders.FabricPlanetManager;
import mod.syconn.swe.wrappers.BlockFluidWrapper;
import mod.syconn.swe.wrappers.ComponentFluidWrapper;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.minecraft.server.packs.PackType;

public class FabricMod implements ModInitializer {
    
    public void onInitialize() {
        FluidStorage.GENERAL_COMBINED_PROVIDER.register(context -> {
            if (context.getItemVariant().getItem() instanceof Canister) return new ComponentFluidWrapper(ComponentRegister.FLUID_COMPONENT, context, 8000);
            return null;
        });
        FluidStorage.SIDED.registerForBlockEntities((block, context) -> new BlockFluidWrapper(),
                BlockEntityRegister.TANK.get(), BlockEntityRegister.COLLECTOR.get(), BlockEntityRegister.DISPERSER.get());
        // TODO FIGURE OUT CONFIG
        EntityEvents.FALL_EVENT.register(((livingEntity, distance, damageMultiplier, cancelled) -> CommonHandler.livingFallEvent(new Events.LivingFallEvent(livingEntity, distance, damageMultiplier, cancelled))));
        EntityEvents.ENTITY_TICK.register(entity -> CommonHandler.entityTickEvent(new Events.LivingEntityEvent(entity)));
        PlayerEvents.PLAYER_TICK.register(player -> CommonHandler.playerJoined(new Events.PlayerEvent(player)));
        PlayerEvents.PLAYER_JOIN.register(player -> CommonHandler.playerLeft(new Events.PlayerEvent(player)));
        PlayerEvents.PLAYER_DISCONNECT.register(player -> CommonHandler.playerTickEvent(new Events.PlayerEvent(player)));
        ServerTickEvents.END_WORLD_TICK.register(serverLevel -> PipeNetworks.tickNetworks(new Events.LevelTick(serverLevel)));
        ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register((player, origin, destination) -> CommonHandler.playerChangedDimension(new Events.PlayerEvent(player)));

        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new FabricPlanetManager());
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new FabricOxygenProductionManager());

        SpaceMod.init();
        Network.S2CPayloads();
    }
}

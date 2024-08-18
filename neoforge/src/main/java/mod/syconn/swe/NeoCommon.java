package mod.syconn.swe;

import mod.syconn.swe.common.CommonHandler;
import mod.syconn.swe.extra.core.Events;
import mod.syconn.swe.extra.data.savedData.PipeNetworks;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public class NeoCommon {

    @SubscribeEvent
    public static void entityTickEvent(EntityTickEvent.Pre event){
        if (event.getEntity() instanceof LivingEntity livingEntity) CommonHandler.entityTickEvent(new Events.LivingEntityEvent(livingEntity));
    }

    @SubscribeEvent
    public static void livingFallEvent(LivingFallEvent event) {
        Events.LivingFallEvent result = CommonHandler.livingFallEvent(new Events.LivingFallEvent(event.getEntity(), event.getDistance(), event.getDamageMultiplier(), false));
        event.setDistance(result.distance());
        event.setDamageMultiplier(result.damageMultiplier());
        event.setCanceled(result.cancel());
    }

    @SubscribeEvent
    public static void levelTickEvent(LevelTickEvent.Pre event) {
        PipeNetworks.tickNetworks(new Events.LevelTick(event.getLevel()));
    }

    @SubscribeEvent
    public static void playerTickEvent(PlayerTickEvent.Pre event) {
        CommonHandler.playerTickEvent(new Events.PlayerEvent(event.getEntity()));
    }

    @SubscribeEvent
    public static void playerJoined(PlayerEvent.PlayerLoggedInEvent event) {
        CommonHandler.playerJoined(new Events.PlayerEvent(event.getEntity()));
    }

    @SubscribeEvent
    public static void playerLeft(PlayerEvent.PlayerLoggedOutEvent event) {
        CommonHandler.playerLeft(new Events.PlayerEvent(event.getEntity()));
    }

    @SubscribeEvent
    public static void playerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        CommonHandler.playerChangedDimension(new Events.PlayerEvent(event.getEntity()));
    }
}

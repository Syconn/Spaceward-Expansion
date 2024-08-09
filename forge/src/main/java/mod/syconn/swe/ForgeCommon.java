package mod.syconn.swe;

import mod.syconn.swe.common.CommonHandler;
import mod.syconn.swe.util.Events;
import net.minecraft.world.entity.LivingEntity;
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
    public static void playerTickEvent(TickEvent.PlayerTickEvent.Post event) {
        CommonHandler.playerTickEvent(new Events.PlayerEvent(event.player));
    }

    @SubscribeEvent
    public static void fallDamageEvent(LivingFallEvent event) {
        Events.LivingFallEvent result = CommonHandler.livingFallEvent(new Events.LivingFallEvent(event.getEntity(), event.getDistance(), event.getDamageMultiplier(), false));
        event.setDistance(result.distance());
        event.setDamageMultiplier(result.damageMultiplier());
        event.setCanceled(result.cancel());
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

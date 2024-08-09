package mod.syconn.swe.events;

import mod.syconn.swe.util.Events;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class EntityEvents {

    public static Event<LivingEntityFallCallback> FALL_EVENT = EventFactory.createArrayBacked(LivingEntityFallCallback.class, (listeners) ->
        (livingEntity, distance, damageMultiplier, cancelled) -> {
            for (LivingEntityFallCallback listener : listeners) return listener.fall(livingEntity, distance, damageMultiplier, cancelled);
            return new Events.LivingFallEvent(livingEntity, distance, damageMultiplier, cancelled);
    });

    public static Event<EntityTickCallback> ENTITY_TICK = EventFactory.createArrayBacked(EntityTickCallback.class, listener -> entity -> true);

    public static Event<PlayerTickCallback> PLAYER_TICK = EventFactory.createArrayBacked(PlayerTickCallback.class, listener -> entity -> true);

    public interface LivingEntityFallCallback {
        Events.LivingFallEvent fall(LivingEntity livingEntity, float distance, float damageMultiplier, boolean cancelled);
    }

    public interface EntityTickCallback {
        boolean tick(LivingEntity entity);
    }

    public interface PlayerTickCallback {
        boolean tick(Player player);
    }
}

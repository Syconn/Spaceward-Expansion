package mod.syconn.swe.events;

import mod.syconn.swe.extra.Events;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.LivingEntity;

public class EntityEvents {

    public static Event<LivingEntityFallCallback> FALL_EVENT = EventFactory.createArrayBacked(LivingEntityFallCallback.class, (listeners) ->
        (livingEntity, distance, damageMultiplier, cancelled) -> {
            for (LivingEntityFallCallback listener : listeners) return listener.fall(livingEntity, distance, damageMultiplier, cancelled);
            return new Events.LivingFallEvent(livingEntity, distance, damageMultiplier, cancelled);
    });

    public static Event<EntityTickCallback> ENTITY_TICK = EventFactory.createArrayBacked(EntityTickCallback.class, listeners -> (entity) -> {
        for (EntityTickCallback callback : listeners) callback.tick(entity);
    });

    public interface LivingEntityFallCallback {
        Events.LivingFallEvent fall(LivingEntity livingEntity, float distance, float damageMultiplier, boolean cancelled);
    }

    public interface EntityTickCallback {
        void tick(LivingEntity entity);
    }
}

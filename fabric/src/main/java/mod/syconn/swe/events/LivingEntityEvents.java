package mod.syconn.swe.events;

import mod.syconn.swe.util.Events;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.LivingEntity;

public class LivingEntityEvents {

    public static Event<LivingEntityFallCallback> FALL_EVENT = EventFactory.createArrayBacked(LivingEntityFallCallback.class, (listeners) ->
        (livingEntity, distance, damageMultiplier, cancelled) -> {
            for (LivingEntityFallCallback listener : listeners) return listener.fall(livingEntity, distance, damageMultiplier, cancelled);
            return new Events.LivingFallEvent(livingEntity, distance, damageMultiplier, cancelled);
        });

    public interface LivingEntityFallCallback {
        Events.LivingFallEvent fall(LivingEntity livingEntity, float distance, float damageMultiplier, boolean cancelled);
    }
}

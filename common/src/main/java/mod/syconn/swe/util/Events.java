package mod.syconn.swe.util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class Events {

    public record LivingEntityEvent(LivingEntity livingEntity) {}

    public record PlayerEvent (Player player) {}

    public record LivingFallEvent(LivingEntity entity, float distance, float damageMultiplier, boolean cancel) {}
}

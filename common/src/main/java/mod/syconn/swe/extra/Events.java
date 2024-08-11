package mod.syconn.swe.extra;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class Events {

    public record LivingEntityEvent(LivingEntity livingEntity) {}

    public record PlayerEvent (Player player) {}

    public record LivingFallEvent(LivingEntity entity, float distance, float damageMultiplier, boolean cancel) {}

    public record LevelTick(Level level) {}
}

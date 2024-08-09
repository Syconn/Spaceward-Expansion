package mod.syconn.swe.mixin;

import mod.syconn.swe.events.EntityEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {

    @Inject(method = "tickNonPassenger", at = @At("HEAD"))
    public void tickEntities(Entity entity, CallbackInfo ci) {
        if (entity instanceof LivingEntity le) EntityEvents.ENTITY_TICK.invoker().tick(le);
    }
}

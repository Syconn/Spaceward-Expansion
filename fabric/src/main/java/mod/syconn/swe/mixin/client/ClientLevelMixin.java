package mod.syconn.swe.mixin.client;

import mod.syconn.swe.events.EntityEvents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public class ClientLevelMixin {

    @Inject(method = "tickNonPassenger", at = @At("HEAD"))
    public void tickEntities(Entity entity, CallbackInfo ci) {
        if (entity instanceof LivingEntity le) EntityEvents.ENTITY_TICK.invoker().tick(le);
    }
}

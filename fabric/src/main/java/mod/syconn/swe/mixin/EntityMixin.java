package mod.syconn.swe.mixin;

import mod.syconn.swe.events.EntityEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(method = "rideTick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        if (((Entity) (Object) this) instanceof LivingEntity le) EntityEvents.ENTITY_TICK.invoker().tick(le);
    }
}

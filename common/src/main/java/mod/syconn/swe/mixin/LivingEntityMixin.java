package mod.syconn.swe.mixin;

import mod.syconn.swe.common.items.Parachute;
import mod.syconn.swe.core.ModAttributes;
import mod.syconn.swe.util.DimensionHelper;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow public abstract double getAttributeValue(Attribute attribute);

    @ModifyConstant(method = "travel", constant = @Constant(doubleValue = 0.08))
    private double swe_modifyGravity(double d) {
        return getAttributeValue(ModAttributes.GRAVITY.get());
    }

    @ModifyConstant(method = "travel", constant = @Constant(doubleValue = 0.01))
    private double swe_modifySlowFallingGravity(double d) {
        return getAttributeValue(ModAttributes.GRAVITY.get()) * 0.01;
    }

    @Inject(method = "calculateFallDamage", at = @At("HEAD"), cancellable = true)
    protected void swe_modifyFallDamage(float fallDistance, float damageMultiplier, CallbackInfoReturnable<Integer> cir) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        if (livingEntity.getType().is(EntityTypeTags.FALL_DAMAGE_IMMUNE) || Parachute.hasParachute(livingEntity) ||
                DimensionHelper.onMoon(livingEntity) && fallDistance < 6.5D) cir.setReturnValue(0);
        else {
            MobEffectInstance mobEffectInstance = livingEntity.getEffect(MobEffects.JUMP);
            float f = mobEffectInstance == null ? 0.0F : (float)(mobEffectInstance.getAmplifier() + 1);
            cir.setReturnValue(Mth.ceil((fallDistance - 3.0F - f) * damageMultiplier));
        }
    }
}

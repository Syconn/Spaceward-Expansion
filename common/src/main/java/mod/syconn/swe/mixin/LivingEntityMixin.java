package mod.syconn.swe.mixin;

import mod.syconn.swe.common.data.SpaceGearData;
import mod.syconn.swe.core.ModAttributes;
import mod.syconn.swe.core.ModTags;
import mod.syconn.swe.server.reloaders.PlanetManager;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
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
        if (livingEntity.getType().is(EntityTypeTags.FALL_DAMAGE_IMMUNE) || SpaceGearData.get(livingEntity).parachute() ||
                livingEntity.level().dimension().location().equals(ModTags.Planets.MOON) && fallDistance < 6.5D) cir.setReturnValue(0);
        else {
            MobEffectInstance mobEffectInstance = livingEntity.getEffect(MobEffects.JUMP);
            float f = mobEffectInstance == null ? 0.0F : (float)(mobEffectInstance.getAmplifier() + 1);
            cir.setReturnValue(Mth.ceil((fallDistance - 3.0F - f) * damageMultiplier));
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void swe_livingTick(CallbackInfo ci) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        AttributeInstance gravity = livingEntity.getAttribute(ModAttributes.GRAVITY.get());
        double g = PlanetManager.getSettings(livingEntity.level().dimension()).gravity();
        if (gravity.getValue() != g) gravity.setBaseValue(g);
        if (SpaceGearData.get(livingEntity).parachute()) gravity.setBaseValue(g / 12.0);
    }
}

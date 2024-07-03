package mod.syconn.swe.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import mod.syconn.swe.init.ModCapabilities;

@Mixin(PlayerModel.class)
public class PlayerRenderer<T extends LivingEntity> {

    @Inject(at = @At("TAIL"), method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V")
    private void test(T entity, float p_103396_, float p_103397_, float p_103398_, float p_103399_, float p_103400_, CallbackInfo info) {
        if(!(entity instanceof Player player))
            return;

        PlayerModel<T> model = (PlayerModel<T>) (Object) this;

        if (!Minecraft.getInstance().options.getCameraType().isFirstPerson()) {
            player.getCapability(ModCapabilities.SPACE_SUIT).ifPresent(iSpaceSuit -> {
                if (iSpaceSuit.chuteAnim().chuteAnimation() && iSpaceSuit.parachute()){
                    double seg = 160.0 / iSpaceSuit.chuteAnim().maxAnimLen();
                    model.rightArm.zRot = (float) Math.toRadians(seg * iSpaceSuit.chuteAnim().animLen());
                    model.leftArm.zRot = (float) Math.toRadians(-seg * iSpaceSuit.chuteAnim().animLen());
                }
                else if (iSpaceSuit.parachute()){
                    model.rightArm.zRot = (float) Math.toRadians(160);
                    model.leftArm.zRot = (float) Math.toRadians(-160);
                }
            });
            copyArmAngles(model);
        }
    }

    private void copyArmAngles(PlayerModel<T> model){
        copyModelAngles(model.rightArm, model.rightSleeve);
        copyModelAngles(model.leftArm, model.leftSleeve);
    }

    private static void copyModelAngles(ModelPart source, ModelPart target)
    {
        target.xRot = source.xRot;
        target.yRot = source.yRot;
        target.zRot = source.zRot;
    }
}

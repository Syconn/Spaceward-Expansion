package mod.syconn.swe.mixin;

import mod.syconn.swe.extra.data.attachment.SpaceSuit;
import mod.syconn.swe.init.DataAttachments;
import mod.syconn.swe.extra.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("MissingUnique")
@Mixin(PlayerModel.class)
public class PlayerRenderer<T extends LivingEntity> {

    @SuppressWarnings("all")
    @Inject(at = @At("TAIL"), method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V")
    private void test(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch, CallbackInfo info) {
        if(!(pEntity instanceof Player player))
            return;

        PlayerModel<T> model = (PlayerModel<T>) (Object) this;
        if (!Minecraft.getInstance().options.getCameraType().isFirstPerson()) {
            SpaceSuit spaceSuit = Services.ATTACHED_DATA.get(DataAttachments.SPACE_SUIT, player);
            if (spaceSuit.chuteAnim().chuteAnimation() && spaceSuit.parachute()) {
                double seg = 160.0 / spaceSuit.chuteAnim().maxAnimLen();
                model.rightArm.zRot = (float) Math.toRadians(seg * spaceSuit.chuteAnim().animLen());
                model.leftArm.zRot = (float) Math.toRadians(-seg * spaceSuit.chuteAnim().animLen());
            } else if (spaceSuit.parachute()) {
                model.rightArm.zRot = (float) Math.toRadians(160);
                model.leftArm.zRot = (float) Math.toRadians(-160);
            }
            spacewardExpansion$copyArmAngles(model);
        }
    }

    private void spacewardExpansion$copyArmAngles(PlayerModel<T> model){
        spacewardExpansion$copyModelAngles(model.rightArm, model.rightSleeve);
        spacewardExpansion$copyModelAngles(model.leftArm, model.leftSleeve);
    }

    private static void spacewardExpansion$copyModelAngles(ModelPart source, ModelPart target) {
        target.xRot = source.xRot;
        target.yRot = source.yRot;
        target.zRot = source.zRot;
    }
}

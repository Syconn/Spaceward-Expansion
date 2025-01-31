package mod.syconn.swe.client.renders.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import mod.syconn.swe.Constants;
import mod.syconn.swe.client.model.ChuteModel;
import mod.syconn.swe.client.model.ParachuteModel;
import mod.syconn.swe.client.model.TankModel;
import mod.syconn.swe.common.data.SpaceGearData;
import mod.syconn.swe.common.items.Canister;
import mod.syconn.swe.common.items.FluidHolderItem;
import mod.syconn.swe.common.items.Parachute;
import mod.syconn.swe.common.items.SpaceArmor;
import mod.syconn.swe.core.ModItems;
import mod.syconn.swe.util.RenderUtil;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class SpaceSuitLayer<P extends Player, M extends PlayerModel<P>> extends RenderLayer<P, M> {

    private final ParachuteModel pm;
    private final ChuteModel cm;
    private final TankModel tm;

    public SpaceSuitLayer(RenderLayerParent<P, M> pRenderer, EntityModelSet e) {
        super(pRenderer);
        pm = new ParachuteModel(e.bakeLayer(ParachuteModel.LAYER_LOCATION));
        cm = new ChuteModel(e.bakeLayer(ChuteModel.LAYER_LOCATION));
        tm = new TankModel(e.bakeLayer(TankModel.LAYER_LOCATION));
    }

    public void render(PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, P pPlayer, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        if (SpaceArmor.wearingSpaceSuit(pPlayer)) {
            SpaceGearData spaceGear = SpaceGearData.get(pPlayer);
            ItemStack itemstack = spaceGear.hasEquipment(SpaceArmor.PARACHUTE, ModItems.PARACHUTE.get()) ? spaceGear.getEquipment(SpaceArmor.PARACHUTE) : pPlayer.getItemBySlot(EquipmentSlot.CHEST);
            if (itemstack.getItem() instanceof Parachute parachute) {
                int[] color = RenderUtil.getRGBA(parachute.getColor(itemstack));

                pPoseStack.pushPose();
                pPoseStack.translate(0.0F, -0.80F, 0.2F);
                VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(pBufferSource, RenderType.armorCutoutNoCull(Constants.withId("textures/entity/layers/parachute.png")), false, itemstack.hasFoil());
                this.pm.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, color[0], color[1], color[2], color[3]);
                pPoseStack.popPose();

                if (spaceGear.parachute()) {
                    pPoseStack.pushPose();
//                    double seg = -0.69F / suit.chuteAnim().maxAnimLen(); TODO ANIMATIONS
//                    pPoseStack.translate(0.0F, -0.11 + seg * suit.chuteAnim().animLen(), 0.2F);
                    VertexConsumer v2 = ItemRenderer.getArmorFoilBuffer(pBufferSource, RenderType.armorCutoutNoCull(Constants.withId("textures/entity/layers/chute.png")), false, itemstack.hasFoil());
                    this.cm.renderToBuffer(pPoseStack, v2, pPackedLight, OverlayTexture.NO_OVERLAY, color[0], color[1], color[2], 1.0f);
                    pPoseStack.popPose();
                }
            }

            itemstack = spaceGear.getEquipment(SpaceArmor.TANK);
            if (itemstack != null && itemstack.getItem() instanceof Canister canister) {
                FluidHolderItem fluidHolder = canister.getOnClient(itemstack);
                if (fluidHolder != null) {
                    float[][] color = RenderUtil.getRGBA(canister.getBarColor(itemstack), canister.getOutlineColor());

                    pPoseStack.pushPose();
                    pPoseStack.translate(0F, -0.80F, 0.3F);
                    pPoseStack.mulPose(Axis.YP.rotationDegrees(180F));
                    VertexConsumer v2 = pBufferSource.getBuffer(RenderType.entityTranslucentCull(Constants.withId("textures/entity/layers/tank.png")));
                    tm.fluidScaling((float) fluidHolder.getFluidStack().getAmount() / canister.getCapacity());
                    tm.render(pPoseStack, v2, pPackedLight, OverlayTexture.NO_OVERLAY, color[0], color[1], color[2], color[3]);
                    pPoseStack.popPose();
                }
            }
        }
    }
}

package mod.syconn.swe.client.renders.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import mod.syconn.swe.Constants;
import mod.syconn.swe.client.model.ChuteModel;
import mod.syconn.swe.client.model.ParachuteModel;
import mod.syconn.swe.client.model.TankModel;
import mod.syconn.swe.common.container.slot.EquipmentItemSlot;
import mod.syconn.swe.common.inventory.ExtendedPlayerInventory;
import mod.syconn.swe.extra.core.FluidHandlerItem;
import mod.syconn.swe.extra.data.attachment.SpaceSuit;
import mod.syconn.swe.extra.platform.Services;
import mod.syconn.swe.items.Canister;
import mod.syconn.swe.items.Parachute;
import mod.syconn.swe.items.SpaceArmor;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;

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
        if (SpaceArmor.hasFullKit(pPlayer)) {
            ItemStack itemstack = SpaceArmor.hasParachute(pPlayer) ? ((ExtendedPlayerInventory) pPlayer.getInventory()).getItemBySlot(EquipmentItemSlot.SpaceSlot.PARACHUTE) : pPlayer.getItemBySlot(EquipmentSlot.CHEST);
            if (itemstack.getItem() instanceof Parachute) {
                int i = DyedItemColor.getOrDefault(itemstack, -1);
                pPoseStack.pushPose();
                pPoseStack.translate(0.0F, -0.80F, 0.2F);
                VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(pBufferSource, RenderType.armorCutoutNoCull(Constants.loc("textures/entity/layers/parachute.png")), itemstack.hasFoil());
                this.pm.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, FastColor.ARGB32.color(-1, i));
                pPoseStack.popPose();
                SpaceSuit suit = Services.ATTACHED_DATA.get(DataAttachments.SPACE_SUIT, pPlayer);
                if (suit.parachute()) {
                    pPoseStack.pushPose();
                    double seg = -0.69F / suit.chuteAnim().maxAnimLen();
                    pPoseStack.translate(0.0F, -0.11 + seg * suit.chuteAnim().animLen(), 0.2F);
                    VertexConsumer v2 = ItemRenderer.getArmorFoilBuffer(pBufferSource, RenderType.armorCutoutNoCull(Constants.loc("textures/entity/layers/chute.png")), itemstack.hasFoil());
                    this.cm.renderToBuffer(pPoseStack, v2, pPackedLight, OverlayTexture.NO_OVERLAY, FastColor.ARGB32.color(-1, i));
                    pPoseStack.popPose();
                }
            }

            itemstack = SpaceArmor.getGear(EquipmentItemSlot.SpaceSlot.TANK, pPlayer);
            if (itemstack != null && itemstack.getItem() instanceof Canister canister && Services.FLUID_HANDLER.has(itemstack)) {
                FluidHandlerItem handler = Services.FLUID_HANDLER.get(itemstack);
                int i = canister.getBarColor(itemstack);
                int i2 = canister.getOutlineColor();
                pPoseStack.pushPose();
                pPoseStack.translate(0F, -0.80F, 0.3F);
                pPoseStack.mulPose(Axis.YP.rotationDegrees(180F));
                VertexConsumer v2 = pBufferSource.getBuffer(RenderType.entityTranslucentCull(Constants.loc("textures/entity/layers/tank.png")));
                tm.fluidScaling((float) handler.getFluidHolder().getAmount() / handler.getTankCapacity());
                tm.render(pPoseStack, v2, pPackedLight, OverlayTexture.NO_OVERLAY, new int[]{i, i2});
                pPoseStack.popPose();
            }
        }
    }
}

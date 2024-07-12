package mod.syconn.swe.client.renders.entity.layer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import mod.syconn.swe.Main;
import mod.syconn.swe.Registration;
import mod.syconn.swe.client.RenderUtil;
import mod.syconn.swe.client.model.ChuteModel;
import mod.syconn.swe.client.model.ParachuteModel;
import mod.syconn.swe.client.model.TankModel;
import mod.syconn.swe.items.Canister;
import mod.syconn.swe.items.Parachute;
import mod.syconn.swe.items.SpaceArmor;
import mod.syconn.swe.util.ColorUtil;
import mod.syconn.swe.util.Helper;
import mod.syconn.swe.util.data.SpaceSlot;
import mod.syconn.swe.world.data.attachments.SpaceSuit;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.GameRenderer;
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
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;

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

    public void render(PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, P pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        if (SpaceArmor.hasFullKit(pLivingEntity)) {
            ItemStack itemstack = SpaceArmor.hasParachute(pLivingEntity) ? Helper.inventory(pLivingEntity).getItemBySlot(SpaceSlot.PARACHUTE) : pLivingEntity.getItemBySlot(EquipmentSlot.CHEST);
            if (itemstack.getItem() instanceof Parachute) {
                int i = DyedItemColor.getOrDefault(itemstack, -1);
                pPoseStack.pushPose();
                pPoseStack.translate(0.0F, -0.80F, 0.2F);
                VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(pBufferSource, RenderType.armorCutoutNoCull(Main.loc("textures/entity/layers/parachute.png")), itemstack.hasFoil());
                this.pm.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, FastColor.ARGB32.color(-1, i));
                pPoseStack.popPose();
                SpaceSuit suit = pLivingEntity.getData(Registration.SPACE_SUIT);
                if (suit.parachute()) {
                    pPoseStack.pushPose();
                    double seg = -0.69F / suit.chuteAnim().maxAnimLen();
                    pPoseStack.translate(0.0F, -0.11 + seg * suit.chuteAnim().animLen(), 0.2F);
                    VertexConsumer v2 = ItemRenderer.getArmorFoilBuffer(pBufferSource, RenderType.armorCutoutNoCull(Main.loc("textures/entity/layers/chute.png")), itemstack.hasFoil());
                    this.cm.renderToBuffer(pPoseStack, v2, pPackedLight, OverlayTexture.NO_OVERLAY, FastColor.ARGB32.color(-1, i));
                    pPoseStack.popPose();
                }
            }

            itemstack = SpaceArmor.getGear(SpaceSlot.TANK, pLivingEntity);
            if (itemstack != null && itemstack.getItem() instanceof Canister canister) {
                int i = RenderUtil.getFluidColor(Canister.get(itemstack).fluidType());
                int i2 = canister.getOutlineColor();
                pPoseStack.pushPose();
                pPoseStack.translate(0F, -0.80F, 0.3F);
                pPoseStack.mulPose(Axis.YP.rotationDegrees(180F));
                VertexConsumer v2 = ItemRenderer.getArmorFoilBuffer(pBufferSource, RenderType.armorCutoutNoCull(Main.loc("textures/entity/layers/tank.png")), itemstack.hasFoil());
                tm.render(pPoseStack, v2, pPackedLight, OverlayTexture.NO_OVERLAY, new int[]{i, i2});
                pPoseStack.popPose();
            }
        }
    }
}

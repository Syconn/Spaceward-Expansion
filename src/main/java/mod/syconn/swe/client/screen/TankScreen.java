package mod.syconn.swe.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import mod.syconn.swe.Main;
import mod.syconn.swe.common.container.TankMenu;

public class TankScreen extends AbstractContainerScreen<TankMenu> {

    private static final ResourceLocation BG = new ResourceLocation(Main.MODID, "textures/gui/tank.png");

    public TankScreen(TankMenu p_97741_, Inventory p_97742_, Component p_97743_) {
        super(p_97741_, p_97742_, p_97743_);
    }

    @Override
    protected void renderLabels(PoseStack p_97808_, int p_97809_, int p_97810_) {
        drawString(p_97808_, font, menu.getBE().getFluidTank().getFluidAmount() + "mb", this.titleLabelX + 90, this.titleLabelY, 4210752);
    }

    @Override
    public void render(PoseStack p_97795_, int p_97796_, int p_97797_, float p_97798_) {
        super.render(p_97795_, p_97796_, p_97797_, p_97798_);
        this.renderTooltip(p_97795_, p_97796_, p_97797_);
    }

    @Override
    protected void renderBg(PoseStack pose, float p_97790_, int x, int y) {
        RenderSystem.setShaderTexture(0, BG);
        blit(pose, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        FluidState state = menu.getBE().getFluidTank().getFluidInTank(0).getFluid().defaultFluidState();
        if (IClientFluidTypeExtensions.of(state).getStillTexture() != null) {
            RenderSystem.setShaderTexture(0, menu.getBE().getGuiTexture());
            int i = IClientFluidTypeExtensions.of(state).getTintColor();
            int u = (int) ((double) (menu.getBE().getFluidTank().getFluidAmount()) / menu.getBE().getFluidTank().getCapacity() * 70);
            RenderSystem.setShaderColor((float)(i >> 16 & 255) / 255.0F, (float)(i >> 8 & 255) / 255.0F, (float)(i & 255) / 255.0F, 255.0F);
            blit(pose, leftPos + 34, topPos + 8 + (70 - u), 0, 70, 34, u);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }

        RenderSystem.setShaderTexture(0, BG);
        blit(pose, leftPos + 34, topPos + 8, 176, 0, 6, 70);

        if (leftPos + 34 <= x && x <= leftPos + 67 && topPos + 8 <= y && y <= topPos + 77 && !state.is(Fluids.EMPTY)) {
            this.renderTooltip(pose, menu.getBE().getFluidTank().getFluidInTank(0).getDisplayName(), x, y);
        }
    }
}

package mod.syconn.swe.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.DyeColor;
import net.minecraftforge.client.gui.widget.ExtendedButton;
import mod.syconn.swe.Main;
import mod.syconn.swe.client.screen.widget.ArrowButton;
import mod.syconn.swe.client.screen.widget.TabButton;
import mod.syconn.swe.world.container.PipeMenu;
import mod.syconn.swe.network.Network;
import mod.syconn.swe.network.messages.MessageChange;
import mod.syconn.swe.network.messages.MessageClickArrow;
import mod.syconn.swe.network.messages.MessageClickTab;
import mod.syconn.swe.util.Helper;
import mod.syconn.swe.util.data.FluidPointSystem;

public class PipeScreen extends AbstractContainerScreen<PipeMenu> {

    private final ResourceLocation BACKGROUND = new ResourceLocation(Main.MODID, "textures/gui/fluid_pipe.png");

    private TabButton[] tabs;
    private ExtendedButton flipper;
    private Inventory inv;
    private Component component;
    private final FluidPointSystem system = menu.getBE().getSystem();
    private FluidPointSystem.FluidPoint fluidPoint;

    public PipeScreen(PipeMenu menu, Inventory p_97742_, Component p_97743_) {
        super(menu, p_97742_, p_97743_);
        fluidPoint = menu.getBE().selectedTab();
        this.inv = p_97742_;
        this.component = p_97743_;
    }

    protected void init() {
        super.init();
        int sz = system.getPoints().size();
        tabs = new TabButton[sz];
        for (int i = 0; i < sz; i++) {
            TabButton.State state = i == 0 ? TabButton.State.LEFT : i == 5 ? TabButton.State.RIGHT : TabButton.State.MIDDLE;
            String name = system.getPoints().get(i).d().getName();
            tabs[i] = addRenderableWidget(new TabButton(leftPos + (28 + 5) * i, topPos, state, name, null, system.getPoints().get(i).equals(fluidPoint), this::tabClicked));
        }
        addRenderableWidget(new ArrowButton(this.leftPos + 29, this.topPos + 20, ArrowButton.Type.UP, this::arrowClick));
        addRenderableWidget(new ArrowButton(this.leftPos + 29, this.topPos + 60, ArrowButton.Type.DOWN, this::arrowClick));
        flipper = addRenderableWidget(new ExtendedButton(this.leftPos + 56, this.topPos + 33, 60, 20, Component.literal(fluidPoint.toString()), (b) -> Network.getPlayChannel().sendToServer(new MessageChange(menu.getBE().getBlockPos()))));
    }

    public void render(PoseStack p_97795_, int p_97796_, int p_97797_, float p_97798_) {
        fluidPoint = menu.getBE().selectedTab();
        flipper.setMessage(Component.literal(fluidPoint.toString()));
        this.renderBackground(p_97795_);
        super.render(p_97795_, p_97796_, p_97797_, p_97798_);
        renderTooltip(p_97795_, p_97796_, p_97797_);
    }

    protected void renderBg(PoseStack pose, float p_97788_, int p_97789_, int p_97790_) {
        RenderSystem.setShaderTexture(0, BACKGROUND);
        blit(pose, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        if (menu.getBE().selectedTab() != null) {
            blit(pose, this.leftPos + 151, this.topPos + 12, 176, 8, 18, 18);
            blit(pose, this.leftPos + 151, this.topPos + 34, 176, 8, 18, 18);
            blit(pose, this.leftPos + 151, this.topPos + 56, 176, 8, 18, 18);
            blit(pose, this.leftPos + 23, this.topPos + 31, 194, 8, 23, 24);

            drawCenteredString(pose, font, String.valueOf(fluidPoint.priority()), this.leftPos + 34, this.topPos + 39, DyeColor.WHITE.getTextColor());
        }
    }

    private void tabClicked(Button b){
        if (b instanceof TabButton tab) {
            Network.getPlayChannel().sendToServer(new MessageClickTab(menu.getBE().getBlockPos(), Helper.dirFromName(tab.getName())));
        }
    }

    private void arrowClick(Button b){
        if (b instanceof ArrowButton tab) {
            Network.getPlayChannel().sendToServer(new MessageClickArrow(menu.getBE().getBlockPos(), tab.getType() == ArrowButton.Type.UP ? 1 : -1));
        }
    }
}

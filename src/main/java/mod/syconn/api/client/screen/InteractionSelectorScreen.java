package mod.syconn.api.client.screen;

import mod.syconn.api.Constants;
import mod.syconn.api.client.screen.widget.SpriteButton;
import mod.syconn.api.world.capability.IFluidHandlerInteractable;
import mod.syconn.api.world.capability.InteractableFluidTank;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

import java.awt.*;

public abstract class InteractionSelectorScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {

    private final ResourceLocation sideMenu = ResourceLocation.fromNamespaceAndPath(Constants.ID, "textures/gui/interaction_selector.png");
    private final InteractableFluidTank tank;
    private final Point[] buttonPoints = {new Point(30, 80), new Point(30, 30), new Point(30, 55),
            new Point(30, 5), new Point(5, 55), new Point(55, 55)};
    private final Interactables[] interactables = new Interactables[7];
    private final SpriteButton[] interactionButtons = new SpriteButton[6];
    private SpriteButton openMenuButton;
    private boolean sideMenuActive = false;

    public InteractionSelectorScreen(T pMenu, Inventory pPlayerInventory, Component pTitle, InteractableFluidTank tank) {
        super(pMenu, pPlayerInventory, pTitle);
        this.tank = tank;
    }

    protected void init() {
        super.init();
        int boxSize = 24;
        for (Direction direction : Direction.values()) {
            Interactables interaction = Interactables.fromInteraction(tank.getSideInteraction(direction));
            Point point = buttonPoints[direction.get3DDataValue()];
            addRenderableWidget(interactionButtons[direction.get3DDataValue()] = new SpriteButton(leftPos + getMenuX() + point.x, topPos + getMenuY() + point.y, boxSize, boxSize, Component.literal(direction.toString().substring(0, 1).toUpperCase()), Component.literal(interaction.msg), sideMenu, interaction.xLoc, interaction.yLoc,
                    pButton -> interactionButton(direction.get3DDataValue())));
            interactionButtons[direction.get3DDataValue()].setInteractable(false);
            interactables[direction.get3DDataValue()] = interaction;
        }
        addRenderableWidget(openMenuButton = new SpriteButton(getSpriteX(), getSpriteY(), boxSize, boxSize, Component.empty(), Component.empty(), sideMenu, Interactables.ACTIVE.xLoc, Interactables.ACTIVE.yLoc, this::openButton));
        interactables[6] = Interactables.ACTIVE;
    }

    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        if (sideMenuActive) {
            pGuiGraphics.blit(sideMenu, leftPos + getMenuX(), topPos + getMenuY(), 0, 0, 84, 109);
            for (Direction direction : Direction.values()) interactionButtons[direction.get3DDataValue()].setInteractable(true);
        } else {
            for (Direction direction : Direction.values()) interactionButtons[direction.get3DDataValue()].setInteractable(false);
        }
    }

    private void interactionButton(int i) {
        setSpriteButton(interactionButtons[i], interactables[i] = interactables[i].rotate());
        sendPacket(interactables[i], Direction.from3DDataValue(i));
    }

    private void openButton(AbstractButton button) {
        sideMenuActive = !sideMenuActive;
        setSpriteButton(openMenuButton, interactables[6] = interactables[6].rotate()); // TODO MAY NEED TO MOVE IT
    }

    private void setSpriteButton(SpriteButton button, Interactables interaction) {
        button.setSprite(interaction.xLoc, interaction.yLoc);
        button.setHoverInfo(Component.literal(interaction.msg));
    }

    protected abstract int getMenuX();
    protected abstract int getMenuY();
    protected abstract int getSpriteX();
    protected abstract int getSpriteY();
    protected abstract void sendPacket(Interactables interactable, Direction direction);

    protected enum Interactables {
        PUSH(232, 26, "Push Fluids", IFluidHandlerInteractable.Interaction.PUSH),
        PULL(206, 26, "Pull Fluids", IFluidHandlerInteractable.Interaction.PULL),
        BOTH(180, 26, "Push & Pull Fluids", IFluidHandlerInteractable.Interaction.BOTH),
        NONE(180, 0, "None", IFluidHandlerInteractable.Interaction.NONE),
        ACTIVE(206, 0, "", null),
        INACTIVE(232, 0, "", null);

        final int xLoc, yLoc;
        final String msg;
        final IFluidHandlerInteractable.Interaction interaction;

        Interactables(int xLoc, int yLoc, String msg, IFluidHandlerInteractable.Interaction interaction) {
            this.xLoc = xLoc;
            this.yLoc = yLoc;
            this.msg = msg;
            this.interaction = interaction;
        }

        Interactables rotate() {
            return switch (this) {
                case PUSH -> PULL;
                case PULL -> BOTH;
                case BOTH -> NONE;
                case NONE -> PUSH;
                case ACTIVE -> INACTIVE;
                case INACTIVE -> ACTIVE;
            };
        }

        public IFluidHandlerInteractable.Interaction getInteraction() {
            return interaction;
        }

        static Interactables fromInteraction(IFluidHandlerInteractable.Interaction interaction) {
            for (Interactables interactable : Interactables.values()) {
                if (interactable.interaction == interaction) return interactable;
            }
            return NONE;
        }
    }
}

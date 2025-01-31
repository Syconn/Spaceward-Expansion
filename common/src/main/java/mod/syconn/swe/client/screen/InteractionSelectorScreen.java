package mod.syconn.swe.client.screen;

import mod.syconn.swe.Constants;
import mod.syconn.swe.client.screen.widgets.SpriteButton;
import mod.syconn.swe.util.InteractableFluidTankBlock;
import mod.syconn.swe.util.InteractionalFluidHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;

import java.awt.*;
import java.util.List;

public abstract class InteractionSelectorScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {

    private final ResourceLocation sideMenu = Constants.withId("textures/gui/interaction_selector.png");
    private final InteractableFluidTankBlock tank;
    private final Point[] buttonPoints = {new Point(30, 80), new Point(30, 30), new Point(30, 55),
            new Point(30, 5), new Point(5, 55), new Point(55, 55)};
    private final Interactables[] interactables = new Interactables[7];
    private final SpriteButton[] interactionButtons = new SpriteButton[6];
    private SpriteButton openMenuButton;
    private boolean sideMenuActive = false;
    private final Level level;
    private final BlockPos pos;

    public InteractionSelectorScreen(T pMenu, Inventory pPlayerInventory, Component pTitle, InteractableFluidTankBlock tank, BlockPos pos) {
        super(pMenu, pPlayerInventory, pTitle);
        this.tank = tank;
        this.level = pPlayerInventory.player.level();
        this.pos = pos;
    }

    protected void init() {
        super.init();
        int boxSize = 24;
        for (Direction direction : Direction.values()) {
            Interactables interaction = Interactables.fromInteraction(tank.getSideInteraction(direction));
            Point point = buttonPoints[direction.get3DDataValue()];
            List<Component> info = List.of(Component.literal(interaction.msg), Component.literal("Block: ").append(level.getBlockState(pos.relative(direction)).getBlock().getName()));
            addRenderableWidget(interactionButtons[direction.get3DDataValue()] = new SpriteButton(leftPos + getMenuX() + point.x, topPos + getMenuY() + point.y, boxSize, boxSize, Component.literal(direction.toString().substring(0, 1).toUpperCase()), info, sideMenu, interaction.xLoc, interaction.yLoc,
                    pButton -> interactionButton(direction.get3DDataValue())));
            interactionButtons[direction.get3DDataValue()].setInteractable(false);
            interactables[direction.get3DDataValue()] = interaction;
        }
        addRenderableWidget(openMenuButton = new SpriteButton(getSpriteX(), getSpriteY(), boxSize, boxSize, Component.empty(), List.of(Component.empty()), sideMenu, Interactables.ACTIVE.xLoc, Interactables.ACTIVE.yLoc, this::openButton));
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
        setSpriteButton(interactionButtons[i], interactables[i] = interactables[i].rotate(), i);
        sendPacket(interactables[i], Direction.from3DDataValue(i));
    }

    private void openButton(AbstractButton button) {
        sideMenuActive = !sideMenuActive;
        setSpriteButton(openMenuButton, interactables[6] = interactables[6].rotate(), 6);
    }

    private void setSpriteButton(SpriteButton button, Interactables interaction, int dir) {
        button.setSprite(interaction.xLoc, interaction.yLoc);
        if (dir != 6) button.setHoverInfo(List.of(Component.literal(interaction.msg), Component.literal("Block: " + level.getBlockState(pos.relative(Direction.from3DDataValue(dir))).getBlock().getName())));
        else button.setHoverInfo(List.of(Component.literal(interaction.msg)));
    }

    protected abstract int getMenuX();
    protected abstract int getMenuY();
    protected abstract int getSpriteX();
    protected abstract int getSpriteY();
    protected abstract void sendPacket(Interactables interactable, Direction direction);

    protected enum Interactables {
        PUSH(232, 26, "Push Fluids", InteractionalFluidHandler.Interaction.PUSH),
        PULL(206, 26, "Pull Fluids", InteractionalFluidHandler.Interaction.PULL),
        BOTH(180, 26, "Push & Pull Fluids", InteractionalFluidHandler.Interaction.BOTH),
        NONE(180, 0, "None", InteractionalFluidHandler.Interaction.NONE),
        ACTIVE(206, 0, "", null),
        INACTIVE(232, 0, "", null);

        final int xLoc, yLoc;
        final String msg;
        final InteractionalFluidHandler.Interaction interaction;

        Interactables(int xLoc, int yLoc, String msg, InteractionalFluidHandler.Interaction interaction) {
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

        public InteractionalFluidHandler.Interaction getInteraction() {
            return interaction;
        }

        static Interactables fromInteraction(InteractionalFluidHandler.Interaction interaction) {
            for (Interactables interactable : Interactables.values()) {
                if (interactable.interaction == interaction) return interactable;
            }
            return NONE;
        }
    }
}

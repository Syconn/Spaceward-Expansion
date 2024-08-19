package mod.syconn.swe.client.screen;

import mod.syconn.swe.Constants;
import mod.syconn.swe.blockentities.FluidPipeBE;
import mod.syconn.swe.client.screen.widgets.SpriteButton;
import mod.syconn.swe.extra.PipePatterns;
import mod.syconn.swe.network.Network;
import mod.syconn.swe.network.messages.ServerBoundUpdatePipeState;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class FluidPipeScreen extends Screen {

    private final ResourceLocation SM = Constants.loc("textures/gui/interaction_selector.png");
    private final ResourceLocation BG = Constants.loc("textures/gui/fluid_pipe.png");
    private final SpriteButton[] interactionButtons = new SpriteButton[6];
    private final Interaction[] interactions = new Interaction[6];
    private final int imageWidth = 176, imageHeight = 85;
    private final FluidPipeBE pipe;

    public FluidPipeScreen(FluidPipeBE pipe) {
        super(Component.literal("Fluid Pipe Screen"));
        this.pipe = pipe;
    }

    protected void init() {
        int leftPos = (this.width - this.imageWidth) / 2;
        int topPos = (this.height - this.imageHeight) / 2;
        int boxSize = 24;
        int i = 0;
        for (Direction direction : Direction.values()) {
            PipePatterns.PipeConnectionTypes type = pipe.getConnectionType(direction);
            interactions[direction.get3DDataValue()] = Interaction.NONE;
            if (type.isInteractionPoint()) {
                Interaction interaction = Interaction.fromPipeConnection(type);
                int xMove = i < 4 ? i * (boxSize + 18) : (i - 3) * (boxSize + 18);
                int yMove = i < 4 ? 0 : boxSize + 14;
                addRenderableWidget(interactionButtons[direction.get3DDataValue()] = new SpriteButton(leftPos + xMove + 13, topPos + yMove + 11, boxSize, boxSize, Component.literal(direction.toString().substring(0, 1).toUpperCase()), List.of(Component.literal(interaction.msg)), SM, interaction.xLoc, interaction.yLoc,
                        pButton -> interactionButton(direction.get3DDataValue())));
                interactions[direction.get3DDataValue()] = interaction;
                i++;
            }
        }
    }

    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        int leftPos = (this.width - this.imageWidth) / 2;
        int topPos = (this.height - this.imageHeight) / 2;

        pGuiGraphics.blit(BG, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }

    public void renderBackground(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {}

    private void interactionButton(int i) {
        setSpriteButton(interactionButtons[i], interactions[i] = interactions[i].rotate());
    }

    private void setSpriteButton(SpriteButton button, Interaction interaction) {
        button.setSprite(interaction.xLoc, interaction.yLoc);
        button.setHoverInfo(List.of(Component.literal(interaction.msg)));
    }

    public void onClose() {
        super.onClose();
        for (Direction direction : Direction.values()) {
            if (interactions[direction.get3DDataValue()] != Interaction.NONE) Network.sendToServer(new ServerBoundUpdatePipeState(pipe.getBlockPos(), Direction.from3DDataValue(direction.get3DDataValue()), interactions[direction.get3DDataValue()].type));
        }
    }

    protected enum Interaction {
        IMPORT(232, 26, "Input Interface", PipePatterns.PipeConnectionTypes.INPUT),
        EXPORT(206, 26, "Export Interface", PipePatterns.PipeConnectionTypes.OUTPUT),
        BOTH(180, 26, "Input & Export Interface", PipePatterns.PipeConnectionTypes.BOTH),
        BLOCK(180, 0, "Block Interface", PipePatterns.PipeConnectionTypes.BLOCK),
        NONE(0, 0, "", PipePatterns.PipeConnectionTypes.NONE);

        final int xLoc, yLoc;
        final String msg;
        final PipePatterns.PipeConnectionTypes type;

        Interaction(int xLoc, int yLoc, String msg, PipePatterns.PipeConnectionTypes type) {
            this.xLoc = xLoc;
            this.yLoc = yLoc;
            this.msg = msg;
            this.type = type;
        }

        Interaction rotate() {
            return switch (this) {
                case IMPORT -> EXPORT;
                case EXPORT -> BOTH;
                case BOTH -> BLOCK;
                case BLOCK -> IMPORT;
                case NONE -> NONE;
            };
        }

        static Interaction fromPipeConnection(PipePatterns.PipeConnectionTypes type) {
            for (Interaction interaction : values()) if (interaction.type == type) return interaction;
            return BLOCK;
        }
    }
}

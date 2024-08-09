package mod.syconn.swe.api.client.model;

import mod.syconn.api.blocks.AbstractPipeBlock;
import mod.syconn.api.util.PipeConnectionTypes;
import mod.syconn.api.util.PipePatterns;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.model.IDynamicBakedModel;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static mod.syconn.api.util.PipeConnectionTypes.*;
import static mod.syconn.api.util.PipePatterns.SpriteIdx.*;
import static mod.syconn.api.util.BakedModelHelper.*;

// Origional Code Credits: https://www.mcjty.eu/docs/1.20/ep5
public class PipeBakedModel implements IDynamicBakedModel {

    private final IGeometryBakingContext context;
    private final boolean facade;
    private final double size;
    private final String[] locations;
    private TextureAtlasSprite spriteConnector;
    private TextureAtlasSprite spriteNoneCable;
    private TextureAtlasSprite spriteNormalCable;
    private TextureAtlasSprite spriteEndCable;
    private TextureAtlasSprite spriteCornerCable;
    private TextureAtlasSprite spriteThreeCable;
    private TextureAtlasSprite spriteCrossCable;
    private TextureAtlasSprite spriteSideBlock;
    private TextureAtlasSprite spriteSideImport;
    private TextureAtlasSprite spriteSideExport;
    private TextureAtlasSprite spriteSideBoth;

    static { // For all possible patterns we define the sprite to use and the rotation
        PipePatterns.PATTERNS.put(PipePatterns.Pattern.of(false, false, false, false), PipePatterns.QuadSetting.of(SPRITE_NONE, 0));
        PipePatterns.PATTERNS.put(PipePatterns.Pattern.of(true, false, false, false), PipePatterns.QuadSetting.of(SPRITE_END, 3));
        PipePatterns.PATTERNS.put(PipePatterns.Pattern.of(false, true, false, false), PipePatterns.QuadSetting.of(SPRITE_END, 0));
        PipePatterns.PATTERNS.put(PipePatterns.Pattern.of(false, false, true, false), PipePatterns.QuadSetting.of(SPRITE_END, 1));
        PipePatterns.PATTERNS.put(PipePatterns.Pattern.of(false, false, false, true), PipePatterns.QuadSetting.of(SPRITE_END, 2));
        PipePatterns.PATTERNS.put(PipePatterns.Pattern.of(true, true, false, false), PipePatterns.QuadSetting.of(SPRITE_CORNER, 0));
        PipePatterns.PATTERNS.put(PipePatterns.Pattern.of(false, true, true, false), PipePatterns.QuadSetting.of(SPRITE_CORNER, 1));
        PipePatterns.PATTERNS.put(PipePatterns.Pattern.of(false, false, true, true), PipePatterns.QuadSetting.of(SPRITE_CORNER, 2));
        PipePatterns.PATTERNS.put(PipePatterns.Pattern.of(true, false, false, true), PipePatterns.QuadSetting.of(SPRITE_CORNER, 3));
        PipePatterns.PATTERNS.put(PipePatterns.Pattern.of(false, true, false, true), PipePatterns.QuadSetting.of(SPRITE_STRAIGHT, 0));
        PipePatterns.PATTERNS.put(PipePatterns.Pattern.of(true, false, true, false), PipePatterns.QuadSetting.of(SPRITE_STRAIGHT, 1));
        PipePatterns.PATTERNS.put(PipePatterns.Pattern.of(true, true, true, false), PipePatterns.QuadSetting.of(SPRITE_THREE, 0));
        PipePatterns.PATTERNS.put(PipePatterns.Pattern.of(false, true, true, true), PipePatterns.QuadSetting.of(SPRITE_THREE, 1));
        PipePatterns.PATTERNS.put(PipePatterns.Pattern.of(true, false, true, true), PipePatterns.QuadSetting.of(SPRITE_THREE, 2));
        PipePatterns.PATTERNS.put(PipePatterns.Pattern.of(true, true, false, true), PipePatterns.QuadSetting.of(SPRITE_THREE, 3));
        PipePatterns.PATTERNS.put(PipePatterns.Pattern.of(true, true, true, true), PipePatterns.QuadSetting.of(SPRITE_CROSS, 0));
    }

    public PipeBakedModel(IGeometryBakingContext context, boolean facade, double size, String[] locations) {
        this.context = context;
        this.facade = facade;
        this.size = size;
        this.locations = locations;
    }

    private void initTextures() {
        if (spriteConnector == null) {
            spriteConnector = getTexture(0);
            spriteNormalCable = getTexture(1);
            spriteNoneCable = getTexture(2);
            spriteEndCable = getTexture(3);
            spriteCornerCable = getTexture(4);
            spriteThreeCable = getTexture(5);
            spriteCrossCable = getTexture(6);
            spriteSideBlock = getTexture(7);
            spriteSideImport = getTexture(8);
            spriteSideExport = getTexture(9);
            spriteSideBoth = getTexture(10);
        }
    }

    private TextureAtlasSprite getTexture(int i) {
        return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(Main.loc(locations[i]));
    }

    private TextureAtlasSprite getSpriteNormal(PipePatterns.SpriteIdx idx) {
        initTextures();
        return switch (idx) {
            case SPRITE_NONE -> spriteNoneCable;
            case SPRITE_END -> spriteEndCable;
            case SPRITE_STRAIGHT -> spriteNormalCable;
            case SPRITE_CORNER -> spriteCornerCable;
            case SPRITE_THREE -> spriteThreeCable;
            case SPRITE_CROSS -> spriteCrossCable;
        };
    }

    public boolean usesBlockLight() {
        return false;
    }

    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData extraData, @Nullable RenderType layer) {
        initTextures();
        List<BakedQuad> quads = new ArrayList<>();

        if (side == null) {
            PipeConnectionTypes north, south, west, east, up, down;
            if (state != null) {
                north = state.getValue(AbstractPipeBlock.NORTH);
                south = state.getValue(AbstractPipeBlock.SOUTH);
                west = state.getValue(AbstractPipeBlock.WEST);
                east = state.getValue(AbstractPipeBlock.EAST);
                up = state.getValue(AbstractPipeBlock.UP);
                down = state.getValue(AbstractPipeBlock.DOWN);
            } else {
                if (facade) {
                    quads.add(quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteSideBlock));
                    quads.add(quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteSideBlock));
                    quads.add(quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteSideBlock));
                    quads.add(quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteSideBlock));
                    quads.add(quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteSideBlock));
                    quads.add(quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteSideBlock));
                    return quads;
                }
                north = south = west = east = up = down = NONE;
            }

            TextureAtlasSprite spriteCable = spriteNormalCable;
            Function<PipePatterns.SpriteIdx, TextureAtlasSprite> spriteGetter = this::getSpriteNormal;
            double o = size;      // Thickness of the cable. .0 would be full block, .5 is infinitely thin.
            double p = .1;      // Thickness of the connector as it is put on the connecting block
            double q = .2;      // The wideness of the connector

            if (up == CABLE) {
                quads.add(quad(v(1 - o, 1, o), v(1 - o, 1, 1 - o), v(1 - o, 1 - o, 1 - o), v(1 - o, 1 - o, o), spriteCable));
                quads.add(quad(v(o, 1, 1 - o), v(o, 1, o), v(o, 1 - o, o), v(o, 1 - o, 1 - o), spriteCable));
                quads.add(quad(v(o, 1, o), v(1 - o, 1, o), v(1 - o, 1 - o, o), v(o, 1 - o, o), spriteCable));
                quads.add(quad(v(o, 1 - o, 1 - o), v(1 - o, 1 - o, 1 - o), v(1 - o, 1, 1 - o), v(o, 1, 1 - o), spriteCable));
            } else if (up.isInteractionPoint()) {
                TextureAtlasSprite modeSprite = up == INPUT ? spriteSideImport : up == OUTPUT ? spriteSideExport : up == BOTH ? spriteSideBoth : spriteSideBlock;
                quads.add(quad(v(1 - o, 1 - p, o), v(1 - o, 1 - p, 1 - o), v(1 - o, 1 - o, 1 - o), v(1 - o, 1 - o, o), modeSprite, 1));
                quads.add(quad(v(o, 1 - p, 1 - o), v(o, 1 - p, o), v(o, 1 - o, o), v(o, 1 - o, 1 - o), modeSprite, 1));
                quads.add(quad(v(o, 1 - p, o), v(1 - o, 1 - p, o), v(1 - o, 1 - o, o), v(o, 1 - o, o), modeSprite, 1));
                quads.add(quad(v(o, 1 - o, 1 - o), v(1 - o, 1 - o, 1 - o), v(1 - o, 1 - p, 1 - o), v(o, 1 - p, 1 - o), modeSprite, 3));

                quads.add(quad(v(1 - q, 1 - p, q), v(1 - q, 1, q), v(1 - q, 1, 1 - q), v(1 - q, 1 - p, 1 - q), spriteSideBlock));
                quads.add(quad(v(q, 1 - p, 1 - q), v(q, 1, 1 - q), v(q, 1, q), v(q, 1 - p, q), spriteSideBlock));
                quads.add(quad(v(q, 1, q), v(1 - q, 1, q), v(1 - q, 1 - p, q), v(q, 1 - p, q), spriteSideBlock));
                quads.add(quad(v(q, 1 - p, 1 - q), v(1 - q, 1 - p, 1 - q), v(1 - q, 1, 1 - q), v(q, 1, 1 - q), spriteSideBlock));

                quads.add(quad(v(q, 1 - p, q), v(1 - q, 1 - p, q), v(1 - q, 1 - p, 1 - q), v(q, 1 - p, 1 - q), spriteConnector));
                quads.add(quad(v(q, 1, q), v(q, 1, 1 - q), v(1 - q, 1, 1 - q), v(1 - q, 1, q), spriteConnector));
            } else {
                PipePatterns.QuadSetting pattern = PipePatterns.findPattern(west, south, east, north);
                quads.add(quad(v(o, 1 - o, 1 - o), v(1 - o, 1 - o, 1 - o), v(1 - o, 1 - o, o), v(o, 1 - o, o), spriteGetter.apply(pattern.sprite()), pattern.rotation()));
            }

            if (down == CABLE) {
                quads.add(quad(v(1 - o, o, o), v(1 - o, o, 1 - o), v(1 - o, 0, 1 - o), v(1 - o, 0, o), spriteCable));
                quads.add(quad(v(o, o, 1 - o), v(o, o, o), v(o, 0, o), v(o, 0, 1 - o), spriteCable));
                quads.add(quad(v(o, o, o), v(1 - o, o, o), v(1 - o, 0, o), v(o, 0, o), spriteCable));
                quads.add(quad(v(o, 0, 1 - o), v(1 - o, 0, 1 - o), v(1 - o, o, 1 - o), v(o, o, 1 - o), spriteCable));
            } else if (down.isInteractionPoint()) {
                TextureAtlasSprite modeSprite = down == INPUT ? spriteSideImport : down == OUTPUT ? spriteSideExport : down == BOTH ? spriteSideBoth : spriteSideBlock;
                quads.add(quad(v(1 - o, o, o), v(1 - o, o, 1 - o), v(1 - o, p, 1 - o), v(1 - o, p, o), modeSprite, 3));
                quads.add(quad(v(o, o, 1 - o), v(o, o, o), v(o, p, o), v(o, p, 1 - o), modeSprite, 3));
                quads.add(quad(v(o, o, o), v(1 - o, o, o), v(1 - o, p, o), v(o, p, o), modeSprite, 3));
                quads.add(quad(v(o, p, 1 - o), v(1 - o, p, 1 - o), v(1 - o, o, 1 - o), v(o, o, 1 - o), modeSprite, 1));

                quads.add(quad(v(1 - q, 0, q), v(1 - q, p, q), v(1 - q, p, 1 - q), v(1 - q, 0, 1 - q), spriteSideBlock));
                quads.add(quad(v(q, 0, 1 - q), v(q, p, 1 - q), v(q, p, q), v(q, 0, q), spriteSideBlock));
                quads.add(quad(v(q, p, q), v(1 - q, p, q), v(1 - q, 0, q), v(q, 0, q), spriteSideBlock));
                quads.add(quad(v(q, 0, 1 - q), v(1 - q, 0, 1 - q), v(1 - q, p, 1 - q), v(q, p, 1 - q), spriteSideBlock));

                quads.add(quad(v(q, p, 1 - q), v(1 - q, p, 1 - q), v(1 - q, p, q), v(q, p, q), spriteConnector));
                quads.add(quad(v(q, 0, 1 - q), v(q, 0, q), v(1 - q, 0, q), v(1 - q, 0, 1 - q), spriteConnector));
            } else {
                PipePatterns.QuadSetting pattern = PipePatterns.findPattern(west, north, east, south);
                quads.add(quad(v(o, o, o), v(1 - o, o, o), v(1 - o, o, 1 - o), v(o, o, 1 - o), spriteGetter.apply(pattern.sprite()), pattern.rotation()));
            }

            if (east == CABLE) {
                quads.add(quad(v(1, 1 - o, 1 - o), v(1, 1 - o, o), v(1 - o, 1 - o, o), v(1 - o, 1 - o, 1 - o), spriteCable));
                quads.add(quad(v(1, o, o), v(1, o, 1 - o), v(1 - o, o, 1 - o), v(1 - o, o, o), spriteCable));
                quads.add(quad(v(1, 1 - o, o), v(1, o, o), v(1 - o, o, o), v(1 - o, 1 - o, o), spriteCable));
                quads.add(quad(v(1, o, 1 - o), v(1, 1 - o, 1 - o), v(1 - o, 1 - o, 1 - o), v(1 - o, o, 1 - o), spriteCable));
            } else if (east.isInteractionPoint()) {
                TextureAtlasSprite modeSprite = east == INPUT ? spriteSideImport : east == OUTPUT ? spriteSideExport : east == BOTH ? spriteSideBoth : spriteSideBlock;
                quads.add(quad(v(1 - p, 1 - o, 1 - o), v(1 - p, 1 - o, o), v(1 - o, 1 - o, o), v(1 - o, 1 - o, 1 - o), modeSprite, 1));
                quads.add(quad(v(1 - p, o, o), v(1 - p, o, 1 - o), v(1 - o, o, 1 - o), v(1 - o, o, o), modeSprite, 1));
                quads.add(quad(v(1 - p, 1 - o, o), v(1 - p, o, o), v(1 - o, o, o), v(1 - o, 1 - o, o), modeSprite, 1));
                quads.add(quad(v(1 - p, o, 1 - o), v(1 - p, 1 - o, 1 - o), v(1 - o, 1 - o, 1 - o), v(1 - o, o, 1 - o), modeSprite, 1));

                quads.add(quad(v(1 - p, 1 - q, 1 - q), v(1, 1 - q, 1 - q), v(1, 1 - q, q), v(1 - p, 1 - q, q), spriteSideBlock));
                quads.add(quad(v(1 - p, q, q), v(1, q, q), v(1, q, 1 - q), v(1 - p, q, 1 - q), spriteSideBlock));
                quads.add(quad(v(1 - p, 1 - q, q), v(1, 1 - q, q), v(1, q, q), v(1 - p, q, q), spriteSideBlock));
                quads.add(quad(v(1 - p, q, 1 - q), v(1, q, 1 - q), v(1, 1 - q, 1 - q), v(1 - p, 1 - q, 1 - q), spriteSideBlock));

                quads.add(quad(v(1 - p, q, 1 - q), v(1 - p, 1 - q, 1 - q), v(1 - p, 1 - q, q), v(1 - p, q, q), spriteConnector));
                quads.add(quad(v(1, q, 1 - q), v(1, q, q), v(1, 1 - q, q), v(1, 1 - q, 1 - q), spriteConnector));
            } else {
                PipePatterns.QuadSetting pattern = PipePatterns.findPattern(down, north, up, south);
                quads.add(quad(v(1 - o, o, o), v(1 - o, 1 - o, o), v(1 - o, 1 - o, 1 - o), v(1 - o, o, 1 - o), spriteGetter.apply(pattern.sprite()), pattern.rotation()));
            }

            if (west == CABLE) {
                quads.add(quad(v(o, 1 - o, 1 - o), v(o, 1 - o, o), v(0, 1 - o, o), v(0, 1 - o, 1 - o), spriteCable));
                quads.add(quad(v(o, o, o), v(o, o, 1 - o), v(0, o, 1 - o), v(0, o, o), spriteCable));
                quads.add(quad(v(o, 1 - o, o), v(o, o, o), v(0, o, o), v(0, 1 - o, o), spriteCable));
                quads.add(quad(v(o, o, 1 - o), v(o, 1 - o, 1 - o), v(0, 1 - o, 1 - o), v(0, o, 1 - o), spriteCable));
            } else if (west.isInteractionPoint()) {
                TextureAtlasSprite modeSprite = west == INPUT ? spriteSideImport : west == OUTPUT ? spriteSideExport : west == BOTH ? spriteSideBoth : spriteSideBlock;
                quads.add(quad(v(o, 1 - o, 1 - o), v(o, 1 - o, o), v(p, 1 - o, o), v(p, 1 - o, 1 - o), modeSprite, 3));
                quads.add(quad(v(o, o, o), v(o, o, 1 - o), v(p, o, 1 - o), v(p, o, o), modeSprite, 3));
                quads.add(quad(v(o, 1 - o, o), v(o, o, o), v(p, o, o), v(p, 1 - o, o), modeSprite, 3));
                quads.add(quad(v(o, o, 1 - o), v(o, 1 - o, 1 - o), v(p, 1 - o, 1 - o), v(p, o, 1 - o), modeSprite, 3));

                quads.add(quad(v(0, 1 - q, 1 - q), v(p, 1 - q, 1 - q), v(p, 1 - q, q), v(0, 1 - q, q), spriteSideBlock));
                quads.add(quad(v(0, q, q), v(p, q, q), v(p, q, 1 - q), v(0, q, 1 - q), spriteSideBlock));
                quads.add(quad(v(0, 1 - q, q), v(p, 1 - q, q), v(p, q, q), v(0, q, q), spriteSideBlock));
                quads.add(quad(v(0, q, 1 - q), v(p, q, 1 - q), v(p, 1 - q, 1 - q), v(0, 1 - q, 1 - q), spriteSideBlock));

                quads.add(quad(v(p, q, q), v(p, 1 - q, q), v(p, 1 - q, 1 - q), v(p, q, 1 - q), spriteConnector));
                quads.add(quad(v(0, q, q), v(0, q, 1 - q), v(0, 1 - q, 1 - q), v(0, 1 - q, q), spriteConnector));
            } else {
                PipePatterns.QuadSetting pattern = PipePatterns.findPattern(down, south, up, north);
                quads.add(quad(v(o, o, 1 - o), v(o, 1 - o, 1 - o), v(o, 1 - o, o), v(o, o, o), spriteGetter.apply(pattern.sprite()), pattern.rotation()));
            }

            if (north == CABLE) {
                quads.add(quad(v(o, 1 - o, o), v(1 - o, 1 - o, o), v(1 - o, 1 - o, 0), v(o, 1 - o, 0), spriteCable));
                quads.add(quad(v(o, o, 0), v(1 - o, o, 0), v(1 - o, o, o), v(o, o, o), spriteCable));
                quads.add(quad(v(1 - o, o, 0), v(1 - o, 1 - o, 0), v(1 - o, 1 - o, o), v(1 - o, o, o), spriteCable));
                quads.add(quad(v(o, o, o), v(o, 1 - o, o), v(o, 1 - o, 0), v(o, o, 0), spriteCable));
            } else if (north.isInteractionPoint()) {
                TextureAtlasSprite modeSprite = north == INPUT ? spriteSideImport : north == OUTPUT ? spriteSideExport : north == BOTH ? spriteSideBoth : spriteSideBlock;
                quads.add(quad(v(o, 1 - o, o), v(1 - o, 1 - o, o), v(1 - o, 1 - o, p), v(o, 1 - o, p), modeSprite, 3));
                quads.add(quad(v(o, o, p), v(1 - o, o, p), v(1 - o, o, o), v(o, o, o), modeSprite, 3));
                quads.add(quad(v(1 - o, o, p), v(1 - o, 1 - o, p), v(1 - o, 1 - o, o), v(1 - o, o, o), modeSprite, 3));
                quads.add(quad(v(o, o, o), v(o, 1 - o, o), v(o, 1 - o, p), v(o, o, p), modeSprite, 3));

                quads.add(quad(v(q, 1 - q, p), v(1 - q, 1 - q, p), v(1 - q, 1 - q, 0), v(q, 1 - q, 0), spriteSideBlock));
                quads.add(quad(v(q, q, 0), v(1 - q, q, 0), v(1 - q, q, p), v(q, q, p), spriteSideBlock));
                quads.add(quad(v(1 - q, q, 0), v(1 - q, 1 - q, 0), v(1 - q, 1 - q, p), v(1 - q, q, p), spriteSideBlock));
                quads.add(quad(v(q, q, p), v(q, 1 - q, p), v(q, 1 - q, 0), v(q, q, 0), spriteSideBlock));

                quads.add(quad(v(q, q, p), v(1 - q, q, p), v(1 - q, 1 - q, p), v(q, 1 - q, p), spriteConnector));
                quads.add(quad(v(q, q, 0), v(q, 1 - q, 0), v(1 - q, 1 - q, 0), v(1 - q, q, 0), spriteConnector));
            } else {
                PipePatterns.QuadSetting pattern = PipePatterns.findPattern(west, up, east, down);
                quads.add(quad(v(o, 1 - o, o), v(1 - o, 1 - o, o), v(1 - o, o, o), v(o, o, o), spriteGetter.apply(pattern.sprite()), pattern.rotation()));
            }

            if (south == CABLE) {
                quads.add(quad(v(o, 1 - o, 1), v(1 - o, 1 - o, 1), v(1 - o, 1 - o, 1 - o), v(o, 1 - o, 1 - o), spriteCable));
                quads.add(quad(v(o, o, 1 - o), v(1 - o, o, 1 - o), v(1 - o, o, 1), v(o, o, 1), spriteCable));
                quads.add(quad(v(1 - o, o, 1 - o), v(1 - o, 1 - o, 1 - o), v(1 - o, 1 - o, 1), v(1 - o, o, 1), spriteCable));
                quads.add(quad(v(o, o, 1), v(o, 1 - o, 1), v(o, 1 - o, 1 - o), v(o, o, 1 - o), spriteCable));
            } else if (south.isInteractionPoint()) {
                TextureAtlasSprite modeSprite = south == INPUT ? spriteSideImport : south == OUTPUT ? spriteSideExport : south == BOTH ? spriteSideBoth : spriteSideBlock;
                quads.add(quad(v(o, 1 - o, 1 - p), v(1 - o, 1 - o, 1 - p), v(1 - o, 1 - o, 1 - o), v(o, 1 - o, 1 - o), modeSprite, 1));
                quads.add(quad(v(o, o, 1 - o), v(1 - o, o, 1 - o), v(1 - o, o, 1 - p), v(o, o, 1 - p), modeSprite, 1));
                quads.add(quad(v(1 - o, o, 1 - o), v(1 - o, 1 - o, 1 - o), v(1 - o, 1 - o, 1 - p), v(1 - o, o, 1 - p), modeSprite, 1));
                quads.add(quad(v(o, o, 1 - p), v(o, 1 - o, 1 - p), v(o, 1 - o, 1 - o), v(o, o, 1 - o), modeSprite, 1));

                quads.add(quad(v(q, 1 - q, 1), v(1 - q, 1 - q, 1), v(1 - q, 1 - q, 1 - p), v(q, 1 - q, 1 - p), spriteSideBlock));
                quads.add(quad(v(q, q, 1 - p), v(1 - q, q, 1 - p), v(1 - q, q, 1), v(q, q, 1), spriteSideBlock));
                quads.add(quad(v(1 - q, q, 1 - p), v(1 - q, 1 - q, 1 - p), v(1 - q, 1 - q, 1), v(1 - q, q, 1), spriteSideBlock));
                quads.add(quad(v(q, q, 1), v(q, 1 - q, 1), v(q, 1 - q, 1 - p), v(q, q, 1 - p), spriteSideBlock));

                quads.add(quad(v(q, 1 - q, 1 - p), v(1 - q, 1 - q, 1 - p), v(1 - q, q, 1 - p), v(q, q, 1 - p), spriteConnector));
                quads.add(quad(v(q, 1 - q, 1), v(q, q, 1), v(1 - q, q, 1), v(1 - q, 1 - q, 1), spriteConnector));
            } else {
                PipePatterns.QuadSetting pattern = PipePatterns.findPattern(west, down, east, up);
                quads.add(quad(v(o, o, 1 - o), v(1 - o, o, 1 - o), v(1 - o, 1 - o, 1 - o), v(o, 1 - o, 1 - o), spriteGetter.apply(pattern.sprite()), pattern.rotation()));
            }
        }
        return quads;
    }

    public boolean useAmbientOcclusion() {
        return true;
    }

    public boolean isGui3d() {
        return false;
    }

    public boolean isCustomRenderer() {
        return false;
    }

    public ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state, @NotNull RandomSource rand, @NotNull ModelData data) {
        return ChunkRenderTypeSet.of(RenderType.cutoutMipped());
    }

    public TextureAtlasSprite getParticleIcon() {
        initTextures();
        return spriteNormalCable == null ? Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(MissingTextureAtlasSprite.getLocation()) : spriteNormalCable;
    }

    public ItemTransforms getTransforms() {
        return context.getTransforms();
    }

    public ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY;
    }
}
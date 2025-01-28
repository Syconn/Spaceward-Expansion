package mod.syconn.swe.util;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;

public class PipePatterns {

    public static final Map<Pattern, QuadSetting> PATTERNS = new HashMap<>();

    public static QuadSetting findPattern(PipeConnectionTypes s1, PipeConnectionTypes s2, PipeConnectionTypes s3, PipeConnectionTypes s4) {
        return PATTERNS.get(new Pattern(s1 != PipeConnectionTypes.NONE, s2 != PipeConnectionTypes.NONE, s3 != PipeConnectionTypes.NONE, s4 != PipeConnectionTypes.NONE));
    }

    public enum SpriteIdx {
        SPRITE_NONE,
        SPRITE_END,
        SPRITE_STRAIGHT,
        SPRITE_CORNER,
        SPRITE_THREE,
        SPRITE_CROSS
    }

    public record QuadSetting(SpriteIdx sprite, int rotation) {
        public static QuadSetting of(SpriteIdx sprite, int rotation) {
            return new QuadSetting(sprite, rotation);
        }
    }

    public record Pattern(boolean s1, boolean s2, boolean s3, boolean s4) {
        public static Pattern of(boolean s1, boolean s2, boolean s3, boolean s4) {
            return new Pattern(s1, s2, s3, s4);
        }
    }

    public enum PipeConnectionTypes implements StringRepresentable {
        INPUT,
        OUTPUT,
        BOTH,
        CABLE,
        BLOCK,
        NONE;

        public boolean isInteractionPoint() {
            return this == INPUT || this == OUTPUT || this == BOTH || this == BLOCK;
        }

        public boolean isImport() {
            return this == INPUT || this == BOTH;
        }

        public boolean isExport() {
            return this == OUTPUT || this == BOTH;
        }

        public @NotNull String getSerializedName() {
            return name().toLowerCase();
        }
    }
}

package mod.syconn.swe.extra.core;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.joml.Matrix4f;

public class Events {

    public record LivingEntityEvent(LivingEntity livingEntity) {}

    public record PlayerEvent (Player player) {}

    public record LivingFallEvent(LivingEntity entity, float distance, float damageMultiplier, boolean cancel) {}

    public record LevelTick(Level level) {}

    public record LevelRenderStage(PoseStack poseStack, Matrix4f modelViewMatrix, Matrix4f projectionMatrix) {}
}

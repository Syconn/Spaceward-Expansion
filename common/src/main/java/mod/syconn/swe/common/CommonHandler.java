package mod.syconn.swe.common;

import mod.syconn.swe.client.renders.debug.PipeDebugRenderer;
import mod.syconn.swe.common.dimensions.PlanetManager;
import mod.syconn.swe.common.dimensions.PlanetTraveler;
import mod.syconn.swe.common.inventory.ExtendedPlayerInventory;
import mod.syconn.swe.extra.EquipmentItem;
import mod.syconn.swe.extra.core.Events;
import mod.syconn.swe.extra.data.attachment.SpaceSuit;
import mod.syconn.swe.extra.helpers.DimensionHelper;
import mod.syconn.swe.extra.platform.Services;
import mod.syconn.swe.init.DataAttachments;
import mod.syconn.swe.items.Parachute;
import mod.syconn.swe.items.SpaceArmor;
import mod.syconn.swe.network.Network;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;

public class CommonHandler {

    public static void entityTickEvent(Events.LivingEntityEvent event) {
        LivingEntity livingEntity = event.livingEntity();
        if (livingEntity == null || livingEntity.level() == null) return;

        AttributeInstance gravity = livingEntity.getAttribute(Attributes.GRAVITY);
        if (gravity == null) return;

        var settings = PlanetManager.getSettings(livingEntity.level().dimension());
        if (settings == null) return;

        double g = settings.gravity();
        boolean hasParachute = false;

        if (livingEntity instanceof Player player) {
            SpaceSuit suit = Services.ATTACHED_DATA.get(DataAttachments.SPACE_SUIT, player);
            hasParachute = suit != null && suit.parachute();
        }

        double expectedGravity = hasParachute ? g / 12.0 : g;
        if (gravity.getBaseValue() != expectedGravity) gravity.setBaseValue(expectedGravity);
    }

    public static void playerTickEvent(Events.PlayerEvent event) {
        Player player = event.player();
        if (player == null || player.level() == null) return;

        if (player instanceof ServerPlayer serverPlayer) {
            ServerLevel serverLevel = serverPlayer.serverLevel();
            if (serverLevel != null && serverPlayer.getY() >= 400) {
                DimensionTransition transition = PlanetTraveler.changePlanet(serverLevel, serverPlayer);
                if (transition != null) {
                    ServerLevel newLevel = transition.newLevel();
                    if (serverLevel.getServer().isLevelEnabled(newLevel)
                            && (newLevel.dimension().equals(serverLevel.dimension())
                            || serverPlayer.canChangeDimensions(serverLevel, newLevel))) {
                        serverPlayer.changeDimension(transition);
                    }
                }
            }

            SpaceSuit suit = Services.ATTACHED_DATA.get(DataAttachments.SPACE_SUIT, serverPlayer);
            if (suit != null) {
                boolean hasParachute = false;
                ItemStack chest = serverPlayer.getInventory().armor.get(2);
                if (!chest.isEmpty() && chest.getItem() instanceof Parachute) {
                    hasParachute = true;
                } else if (SpaceArmor.hasParachute(serverPlayer)) {
                    hasParachute = true;
                }

                if (hasParachute) {
                    if (serverPlayer.fallDistance > 2 && !suit.parachute()) {
                        suit.parachute(true, serverPlayer);
                    } else if (serverPlayer.fallDistance == 0) {
                        suit.parachute(false, serverPlayer);
                    }
                } else {
                    suit.parachute(false, serverPlayer);
                }

                var settings = PlanetManager.getSettings(serverPlayer.level().dimension());
                if (settings != null && !settings.breathable() && !serverPlayer.isCreative()) {
                    suit.decreaseO2(serverPlayer);
                    if (suit.O2() <= -30) {
                        suit.setO2(0, serverPlayer);
                        serverPlayer.hurt(serverPlayer.level().damageSources().campfire(), 4.0F);
                    }
                }

                Services.ATTACHED_DATA.set(DataAttachments.SPACE_SUIT, suit, serverPlayer);
            }
        }

        if (player.getInventory() instanceof ExtendedPlayerInventory inventory && SpaceArmor.hasFullKit(player)) {
            inventory.getSpaceUtil().forEach(stack -> {
                if (stack != null && !stack.isEmpty() && stack.getItem() instanceof EquipmentItem eq) {
                    eq.onEquipmentTick(stack, player.level(), player);
                }
            });
        }
    }

    public static Events.LivingFallEvent livingFallEvent(Events.LivingFallEvent event) {
        LivingEntity entity = event.entity();
        if (!(entity instanceof Player player)) return event;

        SpaceSuit suit = Services.ATTACHED_DATA.get(DataAttachments.SPACE_SUIT, player);
        if (suit != null && suit.parachute()) {
            return new Events.LivingFallEvent(player, 0, 0, true);
        }

        if (DimensionHelper.onMoon(player)) {
            float distance = event.distance();
            if (distance < 6.5F) {
                return new Events.LivingFallEvent(player, 0, 0, true);
            } else {
                return new Events.LivingFallEvent(player, distance - 4.0F, 0.16F, true);
            }
        }

        return event;
    }

    public static void playerJoined(Events.PlayerEvent event) {
        if (event.player() instanceof ServerPlayer sp) {
            Network.sendToPlayer(PipeDebugRenderer.playerJoined(event), sp);
        }
    }

    public static void playerLeft(Events.PlayerEvent event) {
        if (event.player() instanceof ServerPlayer sp) {
            Network.sendToPlayer(PipeDebugRenderer.playerLeft(event), sp);
        }
    }

    public static void playerChangedDimension(Events.PlayerEvent event) {
        if (event.player() instanceof ServerPlayer sp) {
            Network.sendToPlayer(PipeDebugRenderer.playerChangedDimension(event), sp);
        }
    }
}

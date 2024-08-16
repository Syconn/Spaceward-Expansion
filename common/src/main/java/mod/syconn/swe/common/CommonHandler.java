package mod.syconn.swe.common;

import mod.syconn.swe.client.renders.debug.PipeDebugRenderer;
import mod.syconn.swe.common.dimensions.PlanetManager;
import mod.syconn.swe.common.dimensions.PlanetTraveler;
import mod.syconn.swe.common.inventory.ExtendedPlayerInventory;
import mod.syconn.swe.extra.data.attachment.SpaceSuit;
import mod.syconn.swe.extra.platform.Services;
import mod.syconn.swe.init.DataAttachments;
import mod.syconn.swe.items.Parachute;
import mod.syconn.swe.items.SpaceArmor;
import mod.syconn.swe.extra.EquipmentItem;
import mod.syconn.swe.extra.core.Events;
import mod.syconn.swe.extra.helpers.DimensionHelper;
import mod.syconn.swe.network.Network;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.portal.DimensionTransition;

public class CommonHandler {

    public static void entityTickEvent(Events.LivingEntityEvent event){
        LivingEntity livingEntity = event.livingEntity();
        AttributeInstance gravity = livingEntity.getAttribute(Attributes.GRAVITY);
        double g = PlanetManager.getSettings(livingEntity.level().dimension()).gravity();
        if (gravity.getValue() != g) gravity.setBaseValue(g);
        if (event.livingEntity() instanceof Player p && Services.ATTACHED_DATA.get(DataAttachments.SPACE_SUIT, p).parachute()) gravity.setBaseValue(g / 12.0);
    }

    public static void playerTickEvent(Events.PlayerEvent event) {
        Player player = event.player();
        if (player instanceof ServerPlayer p){
            if (p.level() instanceof ServerLevel serverlevel && p.getY() >= 400) { // TODO Config.spaceHeight.get()
                DimensionTransition dimensiontransition = PlanetTraveler.changePlanet(serverlevel, p); // SPAWING IN AIR
                if (dimensiontransition != null) {
                    ServerLevel serverlevel1 = dimensiontransition.newLevel();
                    if (serverlevel.getServer().isLevelEnabled(serverlevel1) && (serverlevel1.dimension() == serverlevel.dimension() || p.canChangeDimensions(serverlevel, serverlevel1))) {
                        p.changeDimension(dimensiontransition);
                    }
                }
            }

            SpaceSuit suit = Services.ATTACHED_DATA.get(DataAttachments.SPACE_SUIT, p);
            if (p.getInventory().armor.get(2).getItem() instanceof Parachute || SpaceArmor.hasParachute(p)){
                if (p.fallDistance > 2 && !suit.parachute()) suit.parachute(true, p);
                else if (p.fallDistance == 0) suit.parachute(false, p);
            } else suit.parachute(false, p);
            if (!PlanetManager.getSettings(p.level().dimension()).breathable() && !p.isCreative()) {
                suit.decreaseO2(p);
                if (suit.O2() <= -30) {
                    suit.setO2(0, p);
                    p.hurt(p.level().damageSources().campfire(), 4.0F);
                }
            }
            Services.ATTACHED_DATA.set(DataAttachments.SPACE_SUIT, suit, p);
        }

        if (player.getInventory() instanceof ExtendedPlayerInventory i && SpaceArmor.hasFullKit(player)) i.getSpaceUtil().forEach(stack -> { if (stack.getItem() instanceof EquipmentItem eq) eq.onEquipmentTick(stack, player.level(), player); });
    }

    public static Events.LivingFallEvent livingFallEvent(Events.LivingFallEvent event) {
        if (event.entity() instanceof Player p && Services.ATTACHED_DATA.has(DataAttachments.SPACE_SUIT, p) && Services.ATTACHED_DATA.get(DataAttachments.SPACE_SUIT, p).parachute()) return new Events.LivingFallEvent(event.entity(), 0, 0, true);
        if (DimensionHelper.onMoon(event.entity())) {
            if (event.distance() < 6.5D) return new Events.LivingFallEvent(event.entity(), 0, 0, true);;
            return new Events.LivingFallEvent(event.entity(), event.distance() - 4.0f, 0.16f, true);
        }
        return event;
    }

    public static void playerJoined(Events.PlayerEvent event) {
        if (event.player() instanceof ServerPlayer sp) Network.sendToPlayer(PipeDebugRenderer.playerJoined(event), sp);
    }

    public static void playerLeft(Events.PlayerEvent event) {
        if (event.player() instanceof ServerPlayer sp) Network.sendToPlayer(PipeDebugRenderer.playerLeft(event), sp);
    }

    public static void playerChangedDimension(Events.PlayerEvent event) {
        if (event.player() instanceof ServerPlayer sp) Network.sendToPlayer(PipeDebugRenderer.playerChangedDimension(event), sp);
    }
}
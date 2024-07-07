package mod.syconn.swe.world;

import mod.syconn.swe.Config;
import mod.syconn.swe.Registration;
import mod.syconn.swe.items.Parachute;
import mod.syconn.swe.items.SpaceArmor;
import mod.syconn.swe.items.extras.EquipmentItem;
import mod.syconn.swe.util.DimensionHelper;
import mod.syconn.swe.util.data.AirBubblesSavedData;
import mod.syconn.swe.world.data.attachments.SpaceSuit;
import mod.syconn.swe.world.dimensions.DimSettingsManager;
import mod.syconn.swe.world.inventory.ExtendedPlayerInventory;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public class CommonHandler {

    @SubscribeEvent
    public void entityTickEvent(EntityTickEvent.Pre event){
        if (event.getEntity() instanceof LivingEntity livingEntity) {
            AttributeInstance gravity = livingEntity.getAttribute(Attributes.GRAVITY);
            double g = DimSettingsManager.getSettings(livingEntity.level().dimension()).gravity();
            if (gravity.getValue() != g) gravity.setBaseValue(g);
            if (livingEntity.getData(Registration.SPACE_SUIT).parachute()) gravity.setBaseValue(g / 12.0);
        }
    }

    @SubscribeEvent
    public void playerTickEvent(PlayerTickEvent.Pre event){
        if (event.getEntity() instanceof ServerPlayer p){
            if (p.getY() >= Config.spaceHeight.get() && DimensionHelper.on(p, Level.OVERWORLD)) {
                ServerLevel serverlevel = ((ServerLevel)p.level()).getServer().getLevel(Registration.MOON_KEY);
                if (serverlevel == null) return;
                p.changeDimension(new DimensionTransition(serverlevel, event.getEntity(), DimensionTransition.DO_NOTHING));
            }
        }
        Player p = event.getEntity();
        SpaceSuit suit = p.getData(Registration.SPACE_SUIT);
        if (p.getInventory().armor.get(2).getItem() instanceof Parachute || SpaceArmor.hasParachute(p)){
            if (p.fallDistance > 2 && !suit.parachute()) suit.parachute(true);
            else if (p.fallDistance == 0) suit.parachute(false);
        } else suit.parachute(false);
        if (!AirBubblesSavedData.get().breathable(p.level().dimension(), p.getOnPos()) && !p.isCreative()){
            suit.decreaseO2(p);
            if (suit.O2() <= -30) {
                suit.setO2(0);
                p.hurt(p.level().damageSources().source(Registration.ANOXIA), 2.0F);
            }
        }
        p.setData(Registration.SPACE_SUIT, suit);
        if (p.getInventory() instanceof ExtendedPlayerInventory i) i.getSpaceUtil().forEach(stack -> { if (stack.getItem() instanceof EquipmentItem eq) eq.onEquipmentTick(stack, p.level(), p); });
    }

    @SubscribeEvent
    public void fallDamageEvent(LivingFallEvent event) {
        if (event.getEntity().hasData(Registration.SPACE_SUIT) && event.getEntity().getData(Registration.SPACE_SUIT).parachute())
            event.setCanceled(true);
        if (DimensionHelper.onMoon(event.getEntity())) {
            if (event.getDistance() < 6.5D) event.setCanceled(true);
            event.setDistance(event.getDistance() - 4.0f);
            event.setDamageMultiplier(0.16f);
        }
    }
}
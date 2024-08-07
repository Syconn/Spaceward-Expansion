package mod.syconn.swe.world;

import mod.syconn.swe.Config;
import mod.syconn.swe.Registration;
import mod.syconn.swe.items.Parachute;
import mod.syconn.swe.items.SpaceArmor;
import mod.syconn.swe.items.extras.EquipmentItem;
import mod.syconn.swe.util.DimensionHelper;
import mod.syconn.swe.world.data.attachments.SpaceSuit;
import mod.syconn.swe.world.dimensions.PlanetManager;
import mod.syconn.swe.world.dimensions.PlanetTraveler;
import mod.syconn.swe.world.inventory.ExtendedPlayerInventory;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.portal.DimensionTransition;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public class CommonHandler {

    public static void init(final FMLCommonSetupEvent event) {
        NeoForge.EVENT_BUS.register(CommonHandler.class);
    }

    @SubscribeEvent
    public static void entityTickEvent(EntityTickEvent.Pre event){
        if (event.getEntity() instanceof LivingEntity livingEntity) {
            AttributeInstance gravity = livingEntity.getAttribute(Attributes.GRAVITY);
            double g = PlanetManager.getSettings(livingEntity.level().dimension()).gravity();
            if (gravity.getValue() != g) gravity.setBaseValue(g);
            if (livingEntity.getData(Registration.SPACE_SUIT).parachute()) gravity.setBaseValue(g / 12.0);
        }
    }

    @SubscribeEvent
    public static void playerTickEvent(PlayerTickEvent.Pre event) {
        Player player = event.getEntity();
        if (player instanceof ServerPlayer p){
            if (p.level() instanceof ServerLevel serverlevel && p.getY() >= Config.spaceHeight.get()) { // TODO REDO
                DimensionTransition dimensiontransition = PlanetTraveler.changePlanet(serverlevel, p); // SPAWING IN AIR
                if (dimensiontransition != null) {
                    ServerLevel serverlevel1 = dimensiontransition.newLevel();
                    if (serverlevel.getServer().isLevelEnabled(serverlevel1) && (serverlevel1.dimension() == serverlevel.dimension() || p.canChangeDimensions(serverlevel, serverlevel1))) {
                        p.changeDimension(dimensiontransition);
                    }
                }
            }

            SpaceSuit suit = p.getData(Registration.SPACE_SUIT);
            if (p.getInventory().armor.get(2).getItem() instanceof Parachute || SpaceArmor.hasParachute(p)){
                if (p.fallDistance > 2 && !suit.parachute()) suit.parachute(true, p);
                else if (p.fallDistance == 0) suit.parachute(false, p);
            } else suit.parachute(false, p);
            if (!PlanetManager.getSettings(p.level().dimension()).breathable() && !p.isCreative()) {
                suit.decreaseO2(p);
                if (suit.O2() <= -30) {
                    suit.setO2(0, p);
                    p.hurt(p.level().damageSources().source(Registration.ANOXIA), 4.0F);
                }
            }
            p.setData(Registration.SPACE_SUIT, suit);
        }

        // TODO MAY BE OK ON BOTH SIDES
        if (player.getInventory() instanceof ExtendedPlayerInventory i && SpaceArmor.hasFullKit(player)) i.getSpaceUtil().forEach(stack -> { if (stack.getItem() instanceof EquipmentItem eq) eq.onEquipmentTick(stack, player.level(), player); });
    }

    @SubscribeEvent
    public static void fallDamageEvent(LivingFallEvent event) {
        if (event.getEntity().hasData(Registration.SPACE_SUIT) && event.getEntity().getData(Registration.SPACE_SUIT).parachute()) event.setCanceled(true);
        if (DimensionHelper.onMoon(event.getEntity())) {
            if (event.getDistance() < 6.5D) event.setCanceled(true);
            event.setDistance(event.getDistance() - 4.0f);
            event.setDamageMultiplier(0.16f);
        }
    }
}
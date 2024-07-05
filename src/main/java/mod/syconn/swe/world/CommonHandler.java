package mod.syconn.swe.world;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import mod.syconn.swe.Main;
import mod.syconn.swe.world.dimensions.DimSettingsManager;
import mod.syconn.swe.world.inventory.ExtendedPlayerInventory;
import mod.syconn.swe.init.ModCapabilities;
import mod.syconn.swe.items.Parachute;
import mod.syconn.swe.items.SpaceArmor;
import mod.syconn.swe.items.extras.EquipmentItem;
import mod.syconn.swe.util.data.AirBubblesSavedData;
import mod.syconn.swe.util.worldgen.dimension.DimChanger;

@Mod.EventBusSubscriber(modid = Main.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonHandler {

    public CommonHandler() {
        MinecraftForge.EVENT_BUS.register(new ModCapabilities());
    }

    @SubscribeEvent
    public static void entityTickEvent(LivingEvent.LivingTickEvent e){
        if (e.getEntity().isAlive()) {
            AttributeInstance gravity = e.getEntity().getAttribute(ForgeMod.ENTITY_GRAVITY.get());
            double g = DimSettingsManager.getSettings(e.getEntity().level.dimension()).gravity();
            if (gravity.getValue() != g) gravity.setBaseValue(g);
            e.getEntity().getCapability(ModCapabilities.SPACE_SUIT).ifPresent(iSpaceSuit -> { if (iSpaceSuit.parachute()) gravity.setBaseValue(g / 12.0); });
        }
    }

    @SubscribeEvent
    public static void playerTickEvent(TickEvent.PlayerTickEvent e){
        if (e.player instanceof ServerPlayer p){
            if (p.getY() >= 400 && ModDim.on(p, Level.OVERWORLD)){
                ServerLevel serverlevel = ((ServerLevel)p.level).getServer().getLevel(ModDim.MOON_KEY);
                if (serverlevel == null) return;
                p.changeDimension(serverlevel, new DimChanger());
            }
        }
        Player p = e.player;
        p.getCapability(ModCapabilities.SPACE_SUIT).ifPresent(ss -> {
            if (p.getInventory().armor.get(2).getItem() instanceof Parachute || SpaceArmor.hasParachute(p)){
                if (p.fallDistance > 2 && !ss.parachute()) ss.parachute(true);
                else if (p.fallDistance == 0) ss.parachute(false);
            } else ss.parachute(false);
        });
        if (!AirBubblesSavedData.get().breathable(p.level.dimension(), p.getOnPos())){
            if (!p.isCreative()) {
                p.getCapability(ModCapabilities.SPACE_SUIT).ifPresent(ss -> {
                    ss.decreaseO2(p);
                    if (ss.O2() <= -30) {
                        ss.setO2(0);
                        p.hurt(p.level.damageSources().source(ModDamageTypes.ANOXIA), 2.0F);
                    }
                });
            }
        }
        if (p.getInventory() instanceof ExtendedPlayerInventory i) {
            i.getSpaceUtil().forEach(stack -> { if (stack.getItem() instanceof EquipmentItem eq) eq.onEquipmentTick(stack, p.level, p); });
        }
    }

    @SubscribeEvent
    public static void fallDamageEvent(LivingFallEvent e){
        e.getEntity().getCapability(ModCapabilities.SPACE_SUIT).ifPresent(ss -> { if (ss.parachute()) e.setCanceled(true); });
        if (ModDim.onMoon(e.getEntity())){
            if (e.getDistance() < 6.5D) e.setCanceled(true);
            e.setDistance(e.getDistance() - 4.0f);
            e.setDamageMultiplier(0.16f);
        }
    }
}

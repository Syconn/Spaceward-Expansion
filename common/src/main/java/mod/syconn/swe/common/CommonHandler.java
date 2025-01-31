package mod.syconn.swe.common;

import mod.syconn.swe.client.renders.debug.PipeDebugRenderer;
import mod.syconn.swe.common.data.SpaceGearData;
import mod.syconn.swe.common.data.SpacePlayerData;
import mod.syconn.swe.common.items.Parachute;
import mod.syconn.swe.common.items.SpaceArmor;
import mod.syconn.swe.core.ModDamageSources;
import mod.syconn.swe.core.ModTags;
import mod.syconn.swe.network.Network;
import mod.syconn.swe.network.messages.MessageSyncPersistentData;
import mod.syconn.swe.server.reloaders.PlanetManager;
import mod.syconn.swe.util.PersistentData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class CommonHandler {

    public static void playerTickEvent(Player player) {
        if (player instanceof ServerPlayer sp) {
            if (sp.getY() >= 400 && sp.canChangeDimensions()) { // TODO Config this
                ServerLevel serverLevel = sp.serverLevel().getServer().getLevel(sp.level().dimension() == ModTags.Planets.MOON ? Level.OVERWORLD : ModTags.Planets.MOON);
                if (serverLevel != null) sp.changeDimension(serverLevel);
            }

            SpaceGearData.update(player, spaceGear -> {
                if (spaceGear.getInventory().getItem(SpaceArmor.PARACHUTE).getItem() instanceof Parachute || spaceGear.parachute()){
                    if (sp.fallDistance > 2 && !spaceGear.parachute()) spaceGear.setParachute(true);
                    else if (sp.fallDistance == 0) spaceGear.setParachute(false);
                } else spaceGear.setParachute(false);
                return spaceGear;
            });

            SpacePlayerData data = SpacePlayerData.from(player);
            if (!PlanetManager.getSettings(sp.level().dimension()).breathable() && !sp.isCreative()) {
                data.lowerOxygen();
                if (data.getOxygen() <= -30) {
                    data.setOxygen(0);
                    sp.hurt(ModDamageSources.anoxia(player.level()), 4.0F);
                }
            }
            SpacePlayerData.set(player, data);
        }

        if (SpaceArmor.wearingSpaceSuit(player)) SpaceGearData.get(player).tick(player);
    }

    public static void playerJoined(Player player) {
        if (player instanceof ServerPlayer sp) {
            Network.CHANNEL.sendToPlayer(sp, new MessageSyncPersistentData(((PersistentData) player).getPersistentData()));
            Network.CHANNEL.sendToPlayer(sp, PipeDebugRenderer.playerJoined(sp));
        }
    }

    public static void playerQuit(Player player) {
        if (player instanceof ServerPlayer sp) Network.CHANNEL.sendToPlayer(sp, PipeDebugRenderer.playerLeft());
    }

    public static void playerChangedDimension(ServerPlayer player, ResourceKey<Level> oldLevel, ResourceKey<Level> newLevel) {
        Network.CHANNEL.sendToPlayer(player, PipeDebugRenderer.playerChangedDimension(player));
    }
}
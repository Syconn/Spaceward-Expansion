package mod.syconn.swe.common.data;

import mod.syconn.swe.util.PersistentData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public class SpacePlayerData {

    private int oxygen;
    private final int maxOxygen;

    public SpacePlayerData(int oxygen, int maxOxygen) {
        this.oxygen = oxygen;
        this.maxOxygen = maxOxygen;
    }

    public SpacePlayerData(CompoundTag tag) {
        this.oxygen = tag.getInt("oxygen");
        this.maxOxygen = tag.getInt("maxOxygen");
    }

    public int getOxygen() {
        return oxygen;
    }

    public int getMaxOxygen() {
        return maxOxygen;
    }

    public SpacePlayerData lowerOxygen() {
        this.oxygen -= 1;
        return this;
    }

    public void setOxygen(int oxygen) {
        this.oxygen = oxygen;
    }

    public CompoundTag writeTag(CompoundTag tag) {
        tag.putInt("oxygen", oxygen);
        tag.putInt("maxOxygen", maxOxygen);
        return tag;
    }

    private static SpacePlayerData instance() {
        return new SpacePlayerData(100, 100);
    }

    public static SpacePlayerData from(Player player) {
        if (((PersistentData) player).getPersistentData().contains("spaceData"))
            return new SpacePlayerData((CompoundTag) ((PersistentData) player).getPersistentData().get("spaceData"));
        return instance();
    }

    public static void set(Player player, SpacePlayerData spaceData) {
        ((PersistentData) player).updatePersistentData(player, spaceData::writeTag);
    }
}

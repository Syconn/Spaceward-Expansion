package mod.syconn.swe.common.data;

import mod.syconn.swe.common.items.EquipmentItem;
import mod.syconn.swe.registry.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Function;

public class SpaceGearData {

    private final SimpleContainer inventory = new SimpleContainer(2);
    private boolean parachute;

    public SpaceGearData(boolean parachute) {
        this.parachute = parachute;
    }

    public SpaceGearData(CompoundTag tag) {
        this.parachute = tag.getBoolean("parachute");
        if (tag.contains("inventory", 9)) this.inventory.fromTag(tag.getList("inventory", 10));
    }

    public boolean parachute() {
        return parachute;
    }

    public SpaceGearData setParachute(boolean parachute) {
        this.parachute = parachute;
        return this;
    }

    public SimpleContainer getInventory() {
        return inventory;
    }

    public ItemStack getEquipment(int slot) {
        return inventory.getItem(slot);
    }

    public boolean hasEquipment(int slot, Item item) {
        return getEquipment(slot).is(item);
    }

    public void tick(Player player) {
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            if (inventory.getItem(slot).getItem() instanceof EquipmentItem eq) eq.equipmentTick(inventory.getItem(slot), player);
        }
    }

    public CompoundTag write(){
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("parachute", parachute);
        tag.put("inventory", this.inventory.createTag());
        return tag;
    }

    private static SpaceGearData instance() {
        return new SpaceGearData(false);
    }

    public static SpaceGearData get(LivingEntity entity) {
        SpaceGearData spaceGear = instance();
        if (entity.getItemBySlot(EquipmentSlot.CHEST).is(ModItems.SPACE_CHESTPLATE.get())) {
            if (entity.getItemBySlot(EquipmentSlot.CHEST).getOrCreateTag().contains("spaceGear"))
                return new SpaceGearData((CompoundTag) entity.getItemBySlot(EquipmentSlot.CHEST).getOrCreateTag().get("spaceGear"));
            else entity.getItemBySlot(EquipmentSlot.CHEST).getOrCreateTag().put("spaceGear", spaceGear.write());
        }
        return spaceGear;
    }

    public static void set(Player player, SpaceGearData spaceGear) {
        player.getItemBySlot(EquipmentSlot.CHEST).getOrCreateTag().put("spaceGear", spaceGear.write());
    }

    public static void update(Player player, Function<SpaceGearData, SpaceGearData> function) { // TODO MAY NOT GET SYNCED
        set(player, function.apply(get(player)));
    }
}

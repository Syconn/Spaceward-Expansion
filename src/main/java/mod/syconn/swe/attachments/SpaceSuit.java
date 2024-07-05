package mod.syconn.swe.attachments;

import mod.syconn.swe.items.Canister;
import mod.syconn.swe.items.SpaceArmor;
import mod.syconn.swe.items.extras.EquipmentItem;
import mod.syconn.swe.util.Animator;
import mod.syconn.swe.util.data.AirBubblesSavedData;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class SpaceSuit implements INBTSerializable<CompoundTag> { // TODO SYNC WITH CLIENT

    private static final String PARACHUTE_NBT = "parachute";
    private static final String CHUTE_NBT = "animchute";
    private static final String OXYGEN_NBT = "oxygen";
    private boolean parachute;
    private Animator chute = new Animator(100);
    private int oxygen = maxO2();
    private NonNullList<ItemStack> stacks = NonNullList.withSize(2, ItemStack.EMPTY);

    public boolean parachute() {
        return parachute;
    }

    public Animator chuteAnim() {
        return chute;
    }

    public void parachute(boolean p) {
        if (p) chute.start();
        parachute = p;
    }

    public NonNullList<ItemStack> getInv() {
        return stacks;
    }

    public int O2() {
        return oxygen;
    }

    public void decreaseO2(Player p) {
        int i = EnchantmentHelper.getRespiration(p);
        ItemStack stack = getStackInSlot(0);
        if ((SpaceArmor.hasFullKit(p) && stack.getItem() instanceof Canister c && c.getCapacity(stack) > 0) || AirBubblesSavedData.get().breathable(p.level.dimension(), p.getOnPos().above(1))) {
            if (oxygen < maxO2()) oxygen++;
        } else setO2(i > 0 && new Random().nextInt(i + 1) > 0 ? O2() : O2() - 1);
    }

    public int maxO2() {
        return 300;
    }

    public void setO2(int o2) {
        oxygen = o2;
    }

    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag t = new CompoundTag();
        t.putBoolean(PARACHUTE_NBT, parachute);
        t.put(CHUTE_NBT, chute.serializeNBT());
        t.putInt(OXYGEN_NBT, oxygen);

        ListTag nbtTagList = new ListTag();
        for (int i = 0; i < stacks.size(); i++) {
            if (!stacks.get(i).isEmpty()) {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putInt("Slot", i);
                nbtTagList.add(stacks.get(i).save(provider, itemTag));
            }
        }
        t.put("Items", nbtTagList);
        t.putInt("Size", stacks.size());
        return t;
    }

    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        parachute = nbt.getBoolean(PARACHUTE_NBT);
        chute = new Animator(nbt.getCompound(CHUTE_NBT));
        oxygen = nbt.getInt(OXYGEN_NBT);
        setSize(nbt.contains("Size", Tag.TAG_INT) ? nbt.getInt("Size") : stacks.size());
        ListTag tagList = nbt.getList("Items", Tag.TAG_COMPOUND);
        for (int i = 0; i < tagList.size(); i++) {
            CompoundTag itemTags = tagList.getCompound(i);
            int slot = itemTags.getInt("Slot");
            if (slot >= 0 && slot < stacks.size()) ItemStack.parse(provider, itemTags).ifPresent(stack -> stacks.set(slot, stack));
        }
    }

    public int getSlots() {
        return stacks.size();
    }

    public ItemStack getStackInSlot(int slot) {
        validateSlotIndex(slot);
        return this.stacks.get(slot);
    }

    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) return ItemStack.EMPTY;
        if (!isItemValid(slot, stack)) return stack;

        validateSlotIndex(slot);
        ItemStack existing = this.stacks.get(slot);
        int limit = getStackLimit(slot, stack);
        if (!existing.isEmpty()) {
            if (!ItemStack.isSameItemSameComponents(stack, existing)) return stack;
            limit -= existing.getCount();
        }
        if (limit <= 0) return stack;

        boolean reachedLimit = stack.getCount() > limit;
        if (!simulate) {
            if (existing.isEmpty()) this.stacks.set(slot, reachedLimit ? stack.copyWithCount(limit) : stack);
            else existing.grow(reachedLimit ? limit : stack.getCount());
        }
        return reachedLimit ? stack.copyWithCount(stack.getCount() - limit) : ItemStack.EMPTY;
    }

    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (amount == 0) return ItemStack.EMPTY;

        validateSlotIndex(slot);

        ItemStack existing = this.stacks.get(slot);

        if (existing.isEmpty()) return ItemStack.EMPTY;

        int toExtract = Math.min(amount, existing.getMaxStackSize());
        if (existing.getCount() <= toExtract) {
            if (!simulate) {
                this.stacks.set(slot, ItemStack.EMPTY);
                return existing;
            }
            else return existing.copy();
        }
        else {
            if (!simulate) this.stacks.set(slot, existing.copyWithCount(existing.getCount() - toExtract));
            return existing.copyWithCount(toExtract);
        }
    }

    public int getSlotLimit(int slot) {
        return 64;
    }

    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return stack.getItem() instanceof EquipmentItem;
    }

    public void setSize(int size) {
        stacks = NonNullList.withSize(size, ItemStack.EMPTY);
    }

    protected int getStackLimit(int slot, @NotNull ItemStack stack) {
        return Math.min(getSlotLimit(slot), stack.getMaxStackSize());
    }

    protected void validateSlotIndex(int slot) {
        if (slot < 0 || slot >= stacks.size()) throw new RuntimeException("Slot " + slot + " not in valid range - [0," + stacks.size() + ")");
    }

    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        stacks.set(slot, stack);
    }
}



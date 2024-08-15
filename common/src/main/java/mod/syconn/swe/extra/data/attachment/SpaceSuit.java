package mod.syconn.swe.extra.data.attachment;

import mod.syconn.swe.common.dimensions.PlanetManager;
import mod.syconn.swe.common.inventory.ExtendedPlayerInventory;
import mod.syconn.swe.extra.core.FluidHandlerItem;
import mod.syconn.swe.extra.helpers.AnimatorHelper;
import mod.syconn.swe.extra.platform.Services;
import mod.syconn.swe.items.SpaceArmor;
import mod.syconn.swe.network.Network;
import mod.syconn.swe.network.messages.BiBoundUpdateSpaceSuit;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;

import java.util.Random;

public class SpaceSuit implements IAttachmentType<SpaceSuit> {

    private boolean parachute;
    private AnimatorHelper chute = new AnimatorHelper(20);
    private int oxygen = maxO2();
    private SimpleContainer container = new SimpleContainer(2);

    public boolean parachute() {
        return parachute;
    }

    public AnimatorHelper chuteAnim() {
        return chute;
    }

    public void parachute(boolean activate, Player player) {
        if (activate) chute.start();
        parachute = activate;
        sync(player);
    }

    public int O2() {
        return oxygen;
    }

    public void decreaseO2(Player p) {
        if (p.getInventory() instanceof ExtendedPlayerInventory ext && Services.FLUID_HANDLER.has(ext.getSpaceUtil().getFirst())) {
            FluidHandlerItem handler = Services.FLUID_HANDLER.get(ext.getSpaceUtil().getFirst());
            if ((SpaceArmor.hasFullKit(p) && handler != null && !handler.getFluid().isEmpty() || PlanetManager.getSettings(p).breathable())) {
                if (oxygen < maxO2()) setO2(oxygen + 1, p);
            } else setO2(new Random().nextInt(2) > 0 ? O2() : O2() - 1, p);
        }
    }

    public int maxO2() {
        return 300;
    }

    public void setO2(int o2, Player player) {
        oxygen = o2;
        sync(player);
    }

    public CompoundTag serializeNBT(HolderLookup.Provider provider) { // TODO
        CompoundTag t = new CompoundTag();
        t.putBoolean("parachute", parachute);
        t.put("animchute", chute.serializeNBT());
        t.putInt("oxygen", oxygen);
        ContainerHelper.saveAllItems(t, container.getItems(), provider);
        return t;
    }

    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) { // TODO
        parachute = nbt.getBoolean("parachute");
        chute = new AnimatorHelper(nbt.getCompound("animchute"));
        oxygen = nbt.getInt("oxygen");
        ContainerHelper.loadAllItems(nbt, container.getItems(), provider);
    }

//    public int getSlots() { TODO UNUSED REMOVE
//        return stacks.size();
//    }
//
//    public ItemStack getStackInSlot(int slot) {
//        return this.stacks.get(slot);
//    }
//
//    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
//        if (stack.isEmpty()) return ItemStack.EMPTY;
//        if (!isItemValid(slot, stack)) return stack;
//        ItemStack existing = this.stacks.get(slot);
//        int limit = 64;
//        if (!existing.isEmpty()) {
//            if (!ItemStack.isSameItemSameComponents(stack, existing)) return stack;
//            limit -= existing.getCount();
//        }
//        if (limit <= 0) return stack;
//
//        boolean reachedLimit = stack.getCount() > limit;
//        if (!simulate) {
//            if (existing.isEmpty()) this.stacks.set(slot, reachedLimit ? stack.copyWithCount(limit) : stack);
//            else existing.grow(reachedLimit ? limit : stack.getCount());
//        }
//        return reachedLimit ? stack.copyWithCount(stack.getCount() - limit) : ItemStack.EMPTY;
//    }
//
//    public ItemStack extractItem(int slot, int amount, boolean simulate) {
//        if (amount == 0) return ItemStack.EMPTY;
//        ItemStack existing = this.stacks.get(slot);
//        if (existing.isEmpty()) return ItemStack.EMPTY;
//        int toExtract = Math.min(amount, existing.getMaxStackSize());
//        if (existing.getCount() <= toExtract) {
//            if (!simulate) {
//                this.stacks.set(slot, ItemStack.EMPTY);
//                return existing;
//            }
//            else return existing.copy();
//        }
//        else {
//            if (!simulate) this.stacks.set(slot, existing.copyWithCount(existing.getCount() - toExtract));
//            return existing.copyWithCount(toExtract);
//        }
//    }
//
//    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
//        return stack.getItem() instanceof EquipmentItem;
//    }
//
//    public void setSize(int size) {
//        stacks = NonNullList.withSize(size, ItemStack.EMPTY);
//    }
//
//    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
//        stacks.set(slot, stack);
//    }

    private CompoundTag writeSyncedData() {
        CompoundTag t = new CompoundTag();
        t.putBoolean("parachute", parachute);
        t.put("animchute", chute.serializeNBT());
        t.putInt("oxygen", oxygen);
        return t;
    }

    public SpaceSuit readSyncedData(SpaceSuit suit, CompoundTag nbt) {
        suit.parachute = nbt.getBoolean("parachute");
        suit.chute = new AnimatorHelper(nbt.getCompound("animchute"));
        suit.oxygen = nbt.getInt("oxygen");
        return suit;
    }

    private void sync(Player player) {
        BiBoundUpdateSpaceSuit packet = new BiBoundUpdateSpaceSuit(writeSyncedData());
        if (player instanceof ServerPlayer serverPlayer) Network.sendToPlayer(packet, serverPlayer);
        else Network.sendToServer(packet);
    }
}




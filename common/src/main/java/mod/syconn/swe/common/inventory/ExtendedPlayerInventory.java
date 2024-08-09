package mod.syconn.swe.common.inventory;

import com.google.common.collect.ImmutableList;
import mod.syconn.swe.Registration;
import mod.syconn.swe.items.SpaceArmor;
import mod.syconn.swe.items.extras.EquipmentItem;
import mod.syconn.swe.common.container.slot.EquipmentItemSlot;
import mod.syconn.swe.common.data.attachments.SpaceSuit;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Predicate;

public class ExtendedPlayerInventory extends Inventory {

    private final NonNullList<ItemStack> space_utilities;
    private final List<NonNullList<ItemStack>> allInventories;

    private final SpaceSuit suit;

    public ExtendedPlayerInventory(Player p) {
        super(p);
        suit = p.getData(Registration.SPACE_SUIT);
        space_utilities = suit.getInv();
        allInventories = ImmutableList.of(this.items, this.armor, this.offhand, this.space_utilities);
    }

    public void setChanged() {
        super.setChanged();
    }

    public NonNullList<ItemStack> getSpaceUtil()
    {
        return suit.getInv();
    }

    public ItemStack getItemBySlot(EquipmentItemSlot.SpaceSlot s){
        if (s == EquipmentItemSlot.SpaceSlot.TANK)
            return space_utilities.get(0);
        else return space_utilities.get(1);
    }

    public ItemStack removeItem(int index, int count) {
        NonNullList<ItemStack> targetInventory = null;
        for(NonNullList<ItemStack> inventory : this.allInventories) {
            if(index < inventory.size()) {
                targetInventory = inventory;
                break;
            }
            index -= inventory.size();
        }
        return targetInventory != null && !targetInventory.get(index).isEmpty() ? ContainerHelper.removeItem(targetInventory, index, count) : ItemStack.EMPTY;
    }

    public void removeItem(ItemStack stack) {
        for(NonNullList<ItemStack> inventory : this.allInventories) {
            for(int i = 0; i < inventory.size(); ++i) {
                if(inventory.get(i) == stack) {
                    inventory.set(i, ItemStack.EMPTY);
                    break;
                }
            }
        }
    }

    public ItemStack removeItemNoUpdate(int index) {
        NonNullList<ItemStack> targetInventory = null;
        for(NonNullList<ItemStack> inventory : this.allInventories) {
            if(index < inventory.size()) {
                targetInventory = inventory;
                break;
            }
            index -= inventory.size();
        }

        if(targetInventory != null && !targetInventory.get(index).isEmpty()) {
            ItemStack stack = targetInventory.get(index);
            targetInventory.set(index, ItemStack.EMPTY);
            return stack;
        }
        else return ItemStack.EMPTY;
    }

    public void setItem(int index, ItemStack stack) {
        NonNullList<ItemStack> targetInventory = null;
        for(NonNullList<ItemStack> inventory : this.allInventories) {
            if(index < inventory.size()) {
                targetInventory = inventory;
                break;
            }
            index -= inventory.size();
        }
        if(targetInventory != null) targetInventory.set(index, stack);
    }

    public ItemStack getItem(int index) {
        List<ItemStack> list = null;
        for(NonNullList<ItemStack> inventory : this.allInventories) {
            if(index < inventory.size()) {
                list = inventory;
                break;
            }
            index -= inventory.size();
        }
        return list == null ? ItemStack.EMPTY : list.get(index);
    }

    public ListTag save(ListTag pListTag) {
        ListTag listTag = super.save(pListTag);
        for (int k = 0; k < this.space_utilities.size(); k++) {
            if (!this.space_utilities.get(k).isEmpty()) {
                CompoundTag compound = new CompoundTag();
                compound.putByte("Slot", (byte) (k + 200));
                listTag.add(this.space_utilities.get(k).save(this.player.registryAccess(), compound));
            }
        }
        return listTag;
    }

    public void load(ListTag pListTag) {
        super.load(pListTag);
        this.space_utilities.clear();

        for (int i = 0; i < pListTag.size(); i++) {
            CompoundTag compoundtag = pListTag.getCompound(i);
            int j = compoundtag.getByte("Slot") & 255;
            ItemStack itemstack = ItemStack.parse(this.player.registryAccess(), compoundtag).orElse(ItemStack.EMPTY);
            if (j >= 200 && j < this.space_utilities.size() + 200) {
                this.space_utilities.set(j - 200, itemstack);
            }
        }
    }

    public int getContainerSize() {
        return super.getContainerSize() + this.space_utilities.size();
    }

    public boolean isEmpty() {
        for(ItemStack stack : this.space_utilities)
            if(!stack.isEmpty()) return false;
        return super.isEmpty();
    }

    public boolean contains(ItemStack pStack) {
        for (List<ItemStack> list : this.allInventories)
            for (ItemStack itemstack : list)
                if (!itemstack.isEmpty() && ItemStack.isSameItemSameComponents(itemstack, pStack)) return true;
        return false;
    }

    public boolean contains(Predicate<ItemStack> pPredicate) {
        for (List<ItemStack> list : this.allInventories)
            for (ItemStack itemstack : list)
                if (pPredicate.test(itemstack)) return true;
        return false;
    }

    public void clearContent() {
        for(List<ItemStack> list : this.allInventories) list.clear();
    }

    public void dropAll() {
        for(List<ItemStack> list : this.allInventories) {
            for(int i = 0; i < list.size(); ++i) {
                ItemStack itemstack = list.get(i);
                if(!itemstack.isEmpty()) {
                    this.player.drop(itemstack, true, false);
                    list.set(i, ItemStack.EMPTY);
                }
            }
        }
    }

    public void tick() {
        super.tick();
        if (SpaceArmor.hasFullKit(player)) {
            space_utilities.forEach(e -> {
                if (e.getItem() instanceof EquipmentItem eq) {
                    eq.onEquipmentTick(e, player.level(), player);
                }
            });
        }
    }
}

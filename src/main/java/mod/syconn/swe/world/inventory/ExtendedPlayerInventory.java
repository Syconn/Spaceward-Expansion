package mod.syconn.swe.world.inventory;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.NonNullList;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import mod.syconn.swe.init.ModCapabilities;
import mod.syconn.swe.items.extras.EquipmentItem;
import mod.syconn.swe.util.data.SpaceSlot;

import java.util.Iterator;
import java.util.List;

public class ExtendedPlayerInventory extends Inventory {

    private final NonNullList<ItemStack> space_utilities;
    private final List<NonNullList<ItemStack>> allInventories;

    private final ISpaceSuit suit;

    public ExtendedPlayerInventory(Player p) {
        super(p);
        suit = p.getCapability(ModCapabilities.SPACE_SUIT).resolve().get();
        space_utilities = suit.getInv();
        allInventories = ImmutableList.of(this.items, this.armor, this.offhand, this.space_utilities);
    }

    public NonNullList<ItemStack> getSpaceUtil()
    {
        return suit.getInv();
    }

    public ItemStack getItemBySlot(SpaceSlot s){
        if (s == SpaceSlot.TANK)
            return space_utilities.get(0);
        else return space_utilities.get(1);
    }

    @Override
    public ItemStack removeItem(int index, int count)
    {
        NonNullList<ItemStack> targetInventory = null;
        for(NonNullList<ItemStack> inventory : this.allInventories)
        {
            if(index < inventory.size())
            {
                targetInventory = inventory;
                break;
            }
            index -= inventory.size();
        }
        return targetInventory != null && !targetInventory.get(index).isEmpty() ? ContainerHelper.removeItem(targetInventory, index, count) : ItemStack.EMPTY;
    }

    @Override
    public void removeItem(ItemStack stack)
    {
        for(NonNullList<ItemStack> inventory : this.allInventories)
        {
            for(int i = 0; i < inventory.size(); ++i)
            {
                if(inventory.get(i) == stack)
                {
                    inventory.set(i, ItemStack.EMPTY);
                    break;
                }
            }
        }
    }

    @Override
    public ItemStack removeItemNoUpdate(int index)
    {
        NonNullList<ItemStack> targetInventory = null;
        for(NonNullList<ItemStack> inventory : this.allInventories)
        {
            if(index < inventory.size())
            {
                targetInventory = inventory;
                break;
            }
            index -= inventory.size();
        }

        if(targetInventory != null && !targetInventory.get(index).isEmpty())
        {
            ItemStack stack = targetInventory.get(index);
            targetInventory.set(index, ItemStack.EMPTY);
            return stack;
        }
        else
        {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void setItem(int index, ItemStack stack)
    {
        NonNullList<ItemStack> targetInventory = null;
        for(NonNullList<ItemStack> inventory : this.allInventories)
        {
            if(index < inventory.size())
            {
                targetInventory = inventory;
                break;
            }
            index -= inventory.size();
        }
        if(targetInventory != null)
        {
            targetInventory.set(index, stack);
        }
    }

    @Override
    public ItemStack getItem(int index)
    {
        List<ItemStack> list = null;
        for(NonNullList<ItemStack> inventory : this.allInventories)
        {
            if(index < inventory.size())
            {
                list = inventory;
                break;
            }
            index -= inventory.size();
        }
        return list == null ? ItemStack.EMPTY : list.get(index);
    }

//    @Override
//    public ListTag save(ListTag list)
//    {
//        list = super.save(list);
//        for(int i = 0; i < this.space_utilities.size(); i++)
//        {
//            if(!this.space_utilities.get(i).isEmpty())
//            {
//                CompoundTag compound = new CompoundTag();
//                compound.putByte("Slot", (byte) (i + 200));
//                this.space_utilities.get(i).save(compound);
//                list.add(compound);
//            }
//        }
//        return list;
//    }
//
//    @Override
//    public void load(ListTag list)
//    {
//        super.load(list);
//        for(int i = 0; i < list.size(); ++i)
//        {
//            CompoundTag compound = list.getCompound(i);
//            int slot = compound.getByte("Slot") & 255;
//            ItemStack stack = ItemStack.of(compound);
//            if(!stack.isEmpty())
//            {
//                if(slot >= 200 && slot < this.space_utilities.size() + 200)
//                {
//                    this.space_utilities.set(slot - 200, stack);
//                }
//            }
//        }
//    }

    @Override
    public int getContainerSize()
    {
        return super.getContainerSize() + this.space_utilities.size();
    }

    @Override
    public boolean isEmpty()
    {
        for(ItemStack stack : this.space_utilities)
        {
            if(!stack.isEmpty())
            {
                return false;
            }
        }
        return super.isEmpty();
    }

    @Override
    public boolean contains(ItemStack targetStack)
    {
        for(NonNullList<ItemStack> inventory : this.allInventories)
        {
            Iterator iterator = inventory.iterator();
            while(true)
            {
                if(!iterator.hasNext())
                {
                    return false;
                }
                ItemStack stack = (ItemStack) iterator.next();
                if(!stack.isEmpty() && stack.sameItem(targetStack))
                {
                    break;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void clearContent()
    {
        for(List<ItemStack> list : this.allInventories)
        {
            list.clear();
        }
    }

    @Override
    public void dropAll()
    {
        for(List<ItemStack> list : this.allInventories)
        {
            for(int i = 0; i < list.size(); ++i)
            {
                ItemStack itemstack = list.get(i);
                if(!itemstack.isEmpty())
                {
                    this.player.drop(itemstack, true, false);
                    list.set(i, ItemStack.EMPTY);
                }
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        space_utilities.forEach(e -> {
            if (e.getItem() instanceof EquipmentItem eq) {
                eq.onEquipmentTick(e, player.level, player);
            }
        });
    }
}

package mod.syconn.swe.capabilities;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandlerModifiable;
import mod.syconn.swe.util.Animator;

@AutoRegisterCapability
public interface ISpaceSuit extends INBTSerializable<CompoundTag>, IItemHandlerModifiable {

    boolean parachute();
    Animator chuteAnim();
    int O2();
    int maxO2();
    void setO2(int o2);
    void decreaseO2(Player p);
    void parachute(boolean p);
    NonNullList<ItemStack> getInv();
}

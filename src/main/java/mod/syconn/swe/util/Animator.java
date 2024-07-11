package mod.syconn.swe.util;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

public class Animator implements INBTSerializable<CompoundTag> {

    private int manAnim;
    private int anim = 0;
    private int inc = 1;

    public Animator(HolderLookup.Provider provider, CompoundTag nbt) {
        this.deserializeNBT(provider, nbt);
    }

    public Animator(int manAnim) {
        this.manAnim = manAnim;
    }

    public boolean chuteAnimation() {
        boolean t = anim > 0;
        if (t) anim -= inc;
        return t;
    }

    public void start(){
        anim = manAnim;
    }

    public int maxAnimLen() {
        return manAnim;
    }

    public int animLen() {
        return manAnim - anim;
    }

    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("max", manAnim);
        nbt.putInt("anim", anim);
        nbt.putInt("inc", inc);
        return nbt;
    }

    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        manAnim = nbt.getInt("max");
        anim = nbt.getInt("anim");
        inc = nbt.getInt("inc");
    }
}
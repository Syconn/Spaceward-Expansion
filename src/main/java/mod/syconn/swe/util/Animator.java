package mod.syconn.swe.util;

import net.minecraft.nbt.CompoundTag;

public class Animator {

    private int manAnim;
    private int anim = 0;
    private int inc = 1;

    public Animator(CompoundTag nbt) {
        this.deserializeNBT(nbt);
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

    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("max", manAnim);
        nbt.putInt("anim", anim);
        nbt.putInt("inc", inc);
        return nbt;
    }

    public void deserializeNBT(CompoundTag nbt) {
        manAnim = nbt.getInt("max");
        anim = nbt.getInt("anim");
        inc = nbt.getInt("inc");
    }
}
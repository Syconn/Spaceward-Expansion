package mod.syconn.swe.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public class Animator implements INBTSerializable<CompoundTag> {

    private int manAnim;
    private int anim = 0;
    private int inc = 1;

    public Animator(CompoundTag t) {
        this.deserializeNBT(t);
    }

    public Animator(int manAnim) {
        this.manAnim = manAnim;
    }

    public Animator(int manAnim, int inc) {
        this.manAnim = manAnim;
        this.inc = inc;
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

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag t = new CompoundTag();
        t.putInt("max", manAnim);
        t.putInt("anim", anim);
        t.putInt("inc", inc);
        return t;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        manAnim = nbt.getInt("max");
        anim = nbt.getInt("anim");
        inc = nbt.getInt("inc");
    }
}

package mod.syconn.swe.extra.helpers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;

public class AnimatorHelper {

    public static Codec<AnimatorHelper> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("maxAnim").forGetter(AnimatorHelper::maxAnimLen),
            Codec.INT.fieldOf("anim").forGetter(AnimatorHelper::getAnim),
            Codec.INT.fieldOf("inc").forGetter(AnimatorHelper::getInc)
    ).apply(instance, AnimatorHelper::new));

    private int manAnim;
    private int anim = 0;
    private int inc = 1;

    public AnimatorHelper(CompoundTag nbt) {
        this.deserializeNBT(nbt);
    }

    public AnimatorHelper(int manAnim) {
        this.manAnim = manAnim;
    }

    public AnimatorHelper(int manAnim, int anim, int inc) {
        this.manAnim = manAnim;
        this.anim = anim;
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

    private int getAnim() {
        return anim;
    }

    private int getInc() {
        return inc;
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
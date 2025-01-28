package mod.syconn.swe.mixin;

import mod.syconn.swe.Constants;
import mod.syconn.swe.util.PersistentData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

@Mixin(Player.class)
public class PlayerPersistentDataMixin implements PersistentData {

    @Unique
    private CompoundTag persistentData;

    public CompoundTag getPersistentData() {
        if (this.persistentData == null) this.persistentData = new CompoundTag();
        return persistentData;
    }

    public void syncPersistentData(CompoundTag tag) {
        this.persistentData = tag;
    }

    public void updatePersistentData(Player player, Function<CompoundTag, CompoundTag> function) {
        this.persistentData = function.apply(getPersistentData());
        sync(this, player);
    }

    @Inject(at = @At("HEAD"), method = "addAdditionalSaveData")
    protected void addAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        if (persistentData != null) compound.put(Constants.MOD + ":persistentData", persistentData);
    }

    @Inject(at = @At("HEAD"), method = "readAdditionalSaveData")
    protected void readAdditionalSaveData(CompoundTag nbt, CallbackInfo info) {
        if (nbt.contains(Constants.MOD + ":persistentData")) persistentData = nbt.getCompound(Constants.MOD + ":persistentData");
    }
}

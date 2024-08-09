package mod.syconn.swe.init;

import mod.syconn.swe.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.Level;

public class DamageSources {

    private static final ResourceKey<DamageType> ANOXIA = ResourceKey.create(Registries.DAMAGE_TYPE, Constants.loc("anoxia"));

    public static DamageSource anoxia(Level level) {
        return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ANOXIA));
    }
}

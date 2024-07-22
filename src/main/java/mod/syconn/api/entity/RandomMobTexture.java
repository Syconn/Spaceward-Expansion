package mod.syconn.api.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;

import java.util.Random;

public abstract class RandomMobTexture extends PathfinderMob {
    protected static final EntityDataAccessor<Integer> SAVED_TEXTURE = SynchedEntityData.defineId(RandomMobTexture.class, EntityDataSerializers.INT);

    protected RandomMobTexture(EntityType<? extends PathfinderMob> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
        this.getEntityData().set(SAVED_TEXTURE, new Random().nextInt(getTextures().length - 1));
    }

    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        pBuilder.define(SAVED_TEXTURE, 0);
    }

    public ResourceLocation getTexture(){
        return getTextures()[this.getEntityData().get(SAVED_TEXTURE)];
    }

    public abstract ResourceLocation[] getTextures();
}

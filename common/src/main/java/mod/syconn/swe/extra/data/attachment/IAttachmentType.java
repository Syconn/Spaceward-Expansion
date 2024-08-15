package mod.syconn.swe.extra.data.attachment;

import com.mojang.serialization.Codec;

public interface IAttachmentType<T> {

    Codec<T> codec();
//    void set(T copy); TODO MAYBE NEED FOR FORGE

    default boolean copyOnDeath() {
        return true;
    }
}

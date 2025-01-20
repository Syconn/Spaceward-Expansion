package mod.syconn.swe.util.core;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public interface IMenuData<T> {

    StreamCodec<RegistryFriendlyByteBuf, T> codec();
}

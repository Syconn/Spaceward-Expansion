package mod.syconn.swe.services;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import mod.syconn.swe.data.attachment.IAttachmentType;
import mod.syconn.swe.data.attachment.SpaceSuit;
import mod.syconn.swe.data.capability.SpaceSuitProvider;
import mod.syconn.swe.platform.services.IAttachedData;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;

import java.util.function.Function;
import java.util.function.Supplier;

public class ForgeAttachedData implements IAttachedData { // TODO HARD CODED CURRENTLY

    private static final Object2ObjectArrayMap<Class<?>, Capability<?>> registrar = new Object2ObjectArrayMap<>();

    public <T extends IAttachmentType<T>> Class<T> registerType(String id, Class<T> typeClass, Supplier<T> typeSupplier) {
        registrar.put(SpaceSuit.class, SpaceSuitProvider.SPACE_SUIT);
        return typeClass;
    }

    public <T extends IAttachmentType<T>> T getPlayer(Class<T> typeClass, Player player) {
        return player.getCapability(getCapability(typeClass)).orElse(null);
    }

    public <T extends IAttachmentType<T>> void setPlayer(Class<T> typeClass, T data, Player player) {
        player.getCapability(getCapability(typeClass)).map(cap -> data);
    }

    public <T extends IAttachmentType<T>> void updatePlayer(Class<T> typeClass, Function<T, T> action, Player player) {
        player.getCapability(getCapability(typeClass)).map(action::apply);
    }

    @SuppressWarnings("unchecked")
    private <T> Capability<T> getCapability(Class<T> typeClass) {
        Capability<T> typeSupplier = (Capability<T>) registrar.get(typeClass);
        if (typeSupplier == null) throw new IllegalArgumentException("Unregistered forge capability: " + typeClass.getName());
        return typeSupplier;
    }
}

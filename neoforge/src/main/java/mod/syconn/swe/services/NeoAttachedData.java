package mod.syconn.swe.services;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import mod.syconn.swe.data.attachment.IAttachmentType;
import mod.syconn.swe.platform.services.IAttachedData;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.AttachmentType;
import java.util.function.Function;
import java.util.function.Supplier;

import static mod.syconn.swe.NeoMod.ATTACHMENT_TYPES;

public class NeoAttachedData implements IAttachedData {

    private static final Object2ObjectArrayMap<Class<?>, AttachmentType<?>> registrar = new Object2ObjectArrayMap<>();

    public <T extends IAttachmentType<T>> Class<T> registerType(String id, Class<T> typeClass, Supplier<T> typeSupplier) {
        Supplier<AttachmentType<T>> type = ATTACHMENT_TYPES.register(id, () ->
                typeSupplier.get().copyOnDeath() ? AttachmentType.builder(typeSupplier).serialize(typeSupplier.get().codec()).copyOnDeath().build() : AttachmentType.builder(typeSupplier).serialize(typeSupplier.get().codec()).build());
        registrar.put(typeClass, type.get());
        return typeClass;
    }

    public <T extends IAttachmentType<T>> T getPlayer(Class<T> typeClass, Player player) {
        return player.getData(getType(typeClass));
    }

    public <T extends IAttachmentType<T>> void setPlayer(Class<T> typeClass, T data, Player player) {
        player.setData(getType(typeClass), data);
    }

    public <T extends IAttachmentType<T>> void updatePlayer(Class<T> typeClass, Function<T, T> action, Player player) {
        player.setData(getType(typeClass), action.apply(getPlayer(typeClass, player)));
    }

    @SuppressWarnings("unchecked")
    private <T> AttachmentType<T> getType(Class<T> typeClass) {
        AttachmentType<T> typeSupplier = (AttachmentType<T>) registrar.get(typeClass);
        if(typeSupplier == null) throw new IllegalArgumentException("Unregistered attachment: " + typeClass.getName());
        return typeSupplier;
    }
}

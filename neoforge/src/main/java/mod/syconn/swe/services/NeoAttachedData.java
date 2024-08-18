package mod.syconn.swe.services;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import mod.syconn.swe.extra.data.attachment.IAttachmentType;
import mod.syconn.swe.extra.platform.services.IAttachedData;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static mod.syconn.swe.NeoMod.ATTACHMENT_TYPES;

public class NeoAttachedData implements IAttachedData {

    private static final Map<Class<?>, Supplier<AttachmentType<?>>> registrar = new Object2ObjectArrayMap<>();

    public <T extends IAttachmentType<T>> Class<T> registerType(String id, Class<T> typeClass, Supplier<T> typeSupplier) {
        DeferredHolder<AttachmentType<?>, AttachmentType<?>> type = ATTACHMENT_TYPES.register(id, () ->
                typeSupplier.get().copyOnDeath() ? AttachmentType.builder(typeSupplier).serialize(typeSupplier.get().codec()).copyOnDeath().build() : AttachmentType.builder(typeSupplier).serialize(typeSupplier.get().codec()).build());
        registrar.put(typeClass, type);
        return typeClass;
    }

    public <T extends IAttachmentType<T>> T get(Class<T> typeClass, Player entity) {
        return entity.getData(getType(typeClass));
    }

    public <T extends IAttachmentType<T>> void set(Class<T> typeClass, T data, Player entity) {
        entity.setData(getType(typeClass), data);
    }

    public <T extends IAttachmentType<T>> void update(Class<T> typeClass, Function<T, T> action, Player entity) {
        entity.setData(getType(typeClass), action.apply(get(typeClass, entity)));
    }

    public <T extends IAttachmentType<T>> boolean has(Class<T> typeClass, Player player) {
        return player.hasData(getType(typeClass));
    }

//    @SuppressWarnings("unchecked")
    private <T> AttachmentType<T> getType(Class<T> typeClass) {
        AttachmentType<T> typeSupplier = (AttachmentType<T>) registrar.get(typeClass).get();
        if(typeSupplier == null) throw new IllegalArgumentException("Unregistered attachment: " + typeClass.getName());
        return typeSupplier;
    }
}

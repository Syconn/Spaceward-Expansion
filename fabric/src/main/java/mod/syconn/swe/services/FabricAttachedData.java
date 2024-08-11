package mod.syconn.swe.services;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import mod.syconn.swe.Constants;
import mod.syconn.swe.data.attachment.IAttachmentType;
import mod.syconn.swe.platform.services.IAttachedData;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.world.entity.player.Player;
import java.util.function.Function;
import java.util.function.Supplier;

public class FabricAttachedData implements IAttachedData {

    private static final Object2ObjectArrayMap<Class<?>, AttachmentType<?>> registrar = new Object2ObjectArrayMap<>();

    public <T extends IAttachmentType<T>> Class<T> registerType(String id, Class<T> typeClass, Supplier<T> typeSupplier) {
        AttachmentType<T> type = !typeSupplier.get().copyOnDeath() ? AttachmentRegistry.<T>builder().persistent(typeSupplier.get().codec()).initializer(typeSupplier).buildAndRegister(Constants.loc(id)) :
                AttachmentRegistry.<T>builder().persistent(typeSupplier.get().codec()).initializer(typeSupplier).copyOnDeath().buildAndRegister(Constants.loc(id));
        registrar.put(typeClass, type);
        return typeClass;
    }

    public <T extends IAttachmentType<T>> T getPlayer(Class<T> typeClass, Player player) {
        return player.getAttached(getType(typeClass));
    }

    public <T extends IAttachmentType<T>> void setPlayer(Class<T> typeClass, T data, Player player) {
        player.setAttached(getType(typeClass), data);
    }

    public <T extends IAttachmentType<T>> void updatePlayer(Class<T> typeClass, Function<T, T> action, Player player) {
        player.setAttached(getType(typeClass), action.apply(getPlayer(typeClass, player)));
    }

    @SuppressWarnings("unchecked")
    private <T> AttachmentType<T> getType(Class<T> typeClass) {
        AttachmentType<T> typeSupplier = (AttachmentType<T>) registrar.get(typeClass);
        if(typeSupplier == null) throw new IllegalArgumentException("Unregistered attachment: " + typeClass.getName());
        return typeSupplier;
    }
}

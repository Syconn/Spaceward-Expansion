package mod.syconn.swe.extra.platform.services;

import mod.syconn.swe.extra.data.attachment.IAttachmentType;
import net.minecraft.world.entity.player.Player;

import java.util.function.Function;
import java.util.function.Supplier;

public interface IAttachedData {

    <T extends IAttachmentType<T>> Class<T> registerType(String id, Class<T> typeClass, Supplier<T> typeSupplier);
    <T extends IAttachmentType<T>> T get(Class<T> typeClass, Player player);
    <T extends IAttachmentType<T>> void set(Class<T> typeClass, T data, Player player);
    <T extends IAttachmentType<T>> void update(Class<T> typeClass, Function<T, T> action, Player player);
    <T extends IAttachmentType<T>> boolean has(Class<T> typeClass, Player player);
}

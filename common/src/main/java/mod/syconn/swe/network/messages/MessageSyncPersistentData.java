package mod.syconn.swe.network.messages;

import dev.architectury.networking.NetworkManager;
import mod.syconn.swe.util.PersistentData;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Supplier;

public class MessageSyncPersistentData {
    private final CompoundTag persistentData;

    public MessageSyncPersistentData(CompoundTag persistentData) {
        this.persistentData = persistentData;
    }

    public MessageSyncPersistentData(FriendlyByteBuf buf) {
        this(buf.readNbt());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeNbt(this.persistentData);
    }

    public void apply(Supplier<NetworkManager.PacketContext> context) {
        context.get().queue(() -> {
            if (context.get().getPlayer() == null) ((PersistentData) Minecraft.getInstance().player).syncPersistentData(this.persistentData);
            else ((PersistentData) context.get().getPlayer()).syncPersistentData(this.persistentData);
        });
    }
}

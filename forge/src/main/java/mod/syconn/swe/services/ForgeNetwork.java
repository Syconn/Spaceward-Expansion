package mod.syconn.swe.services;

import mod.syconn.swe.Constants;
import mod.syconn.swe.extra.core.IMenuData;
import mod.syconn.swe.extra.platform.services.INetwork;
import mod.syconn.swe.network.Network;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;

import java.util.OptionalInt;

public class ForgeNetwork implements INetwork {

    public static final SimpleChannel CHANNEL = ChannelBuilder.named(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "network")).networkProtocolVersion(1).simpleChannel();

    public <D extends IMenuData<D>> OptionalInt openMenuWithData(ServerPlayer player, MenuProvider provider, D data) {
        AbstractContainerMenu oldMenu = player.containerMenu;
        player.openMenu(provider, buf -> data.codec().encode(RegistryFriendlyByteBuf.decorator(player.level().registryAccess()).apply(buf), data));
        AbstractContainerMenu newMenu = player.containerMenu;
        return oldMenu != newMenu ? OptionalInt.of(player.containerCounter) : OptionalInt.empty();
    }

    public void sendToServer(Object payload) {
        CHANNEL.send(payload, PacketDistributor.SERVER.noArg());
    }

    public void sendToClient(Object payload, ServerPlayer serverPlayer) {
        CHANNEL.send(payload, PacketDistributor.PLAYER.with(serverPlayer));
    }

    public <T> void registerPlayBiDirectional(Network.PlayMessage<T> message) {

    }

    public <T> void registerPlayS2C(Network.PlayMessage<T> message) {
        CHANNEL.messageBuilder(message.msgClass(), NetworkDirection.PLAY_TO_CLIENT).codec(message.forgeCodec()).consumerMainThread((payload, context) -> message.handler().accept(payload, context.getSender())).add();
    }

    public <T> void registerPlayC2S(Network.PlayMessage<T> message) {
        CHANNEL.messageBuilder(message.msgClass(), NetworkDirection.PLAY_TO_SERVER).codec(message.forgeCodec()).consumerMainThread((payload, context) -> message.handler().accept(payload, context.getSender())).add();
    }

    public static void setupNetwork(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            Network.C2SPayloads();
            Network.S2CPayloads();
        });
    }
}

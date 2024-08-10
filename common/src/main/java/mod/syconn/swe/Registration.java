package mod.syconn.swe;

import mod.syconn.swe.common.data.attachments.SpaceSuit;

import java.util.function.Supplier;

public class Registration {

    //TODO LOOK AT FRAMEWORK
    public static final Supplier<AttachmentType<SpaceSuit>> SPACE_SUIT = ATTACHMENT_TYPES.register("space_suit", () -> AttachmentType.serializable(SpaceSuit::new).build());

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, COLLECTOR.get(), (o, v) -> o.getFluidHandler());
        event.registerBlockEntity(APICapabilities.FluidHandler.BLOCK, COLLECTOR.get(), (o, v) -> o.getFluidHandler());
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, TANK.get(), (o, v) -> o.getFluidHandler());
        event.registerBlockEntity(APICapabilities.FluidHandler.BLOCK, TANK.get(), (o, v) -> o.getFluidHandler());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, TANK.get(), (o, v) -> o.getItemHandler());
    }
}
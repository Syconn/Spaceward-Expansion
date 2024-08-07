package mod.syconn.swe.init;

import mod.syconn.swe.data.components.FluidComponent;
import mod.syconn.swe.platform.Services;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.component.DataComponentType;

import java.util.function.Supplier;

public class ComponentRegister {

    public static final Supplier<DataComponentType<FluidComponent>> FLUID_COMPONENT = register("fluid_component",
            () -> DataComponentType.<FluidComponent>builder().networkSynchronized(FluidComponent.STREAM_CODEC).persistent(FluidComponent.CODEC).build());

    public static void init() {}

    private static <T> Supplier<DataComponentType<T>> register(String id, Supplier<DataComponentType<T>> componentSupplier) {
        return Services.REGISTRAR.registerDataComponent(id, componentSupplier);
    }
}

package mod.syconn.swe.init;

import mod.syconn.swe.extra.data.components.FluidHolderComponent;
import mod.syconn.swe.extra.platform.Services;
import net.minecraft.core.component.DataComponentType;

import java.util.function.Supplier;

public class ComponentRegister {

    public static final Supplier<DataComponentType<FluidHolderComponent>> FLUID_HOLDER_COMPONENT = register("fluid_component",
            () -> DataComponentType.<FluidHolderComponent>builder().networkSynchronized(FluidHolderComponent.STREAM_CODEC).persistent(FluidHolderComponent.CODEC).build());

    public static void init() {}

    private static <T> Supplier<DataComponentType<T>> register(String id, Supplier<DataComponentType<T>> componentSupplier) {
        return Services.REGISTRAR.registerDataComponent(id, componentSupplier);
    }
}

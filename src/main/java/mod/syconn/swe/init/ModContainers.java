package mod.syconn.swe.init;

import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import mod.syconn.swe.Main;
import mod.syconn.swe.common.be.CollectorBlockEntity;
import mod.syconn.swe.common.be.DisperserBlockEntity;
import mod.syconn.swe.common.be.PipeBlockEntity;
import mod.syconn.swe.common.be.TankBlockEntity;
import mod.syconn.swe.common.container.CollectorMenu;
import mod.syconn.swe.common.container.DisperserMenu;
import mod.syconn.swe.common.container.PipeMenu;
import mod.syconn.swe.common.container.TankMenu;

public class ModContainers {

    public static final DeferredRegister<MenuType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Main.MODID);

    public static final RegistryObject<MenuType<TankMenu>> TANK_MENU = register("tank", (IContainerFactory<TankMenu>) (windowId, playerInventory, data) -> {
        TankBlockEntity menu = (TankBlockEntity) playerInventory.player.level.getBlockEntity(data.readBlockPos());
        return new TankMenu(windowId, playerInventory, menu);
    });
    public static final RegistryObject<MenuType<PipeMenu>> PIPE_MENU = register("pipe_menu", (IContainerFactory<PipeMenu>) (windowId, playerInventory, data) -> {
        PipeBlockEntity menu = (PipeBlockEntity) playerInventory.player.level.getBlockEntity(data.readBlockPos());
        return new PipeMenu(windowId, playerInventory, menu);
    });

    public static final RegistryObject<MenuType<DisperserMenu>> DISPERSER_MENU = register("disperser_menu", (IContainerFactory<DisperserMenu>) (windowId, playerInventory, data) -> {
        DisperserBlockEntity menu = (DisperserBlockEntity) playerInventory.player.level.getBlockEntity(data.readBlockPos());
        return new DisperserMenu(windowId, playerInventory, menu);
    });

    public static final RegistryObject<MenuType<CollectorMenu>> COLLECTOR_MENU = register("collector_menu", (IContainerFactory<CollectorMenu>) (windowId, playerInventory, data) -> {
        CollectorBlockEntity menu = (CollectorBlockEntity) playerInventory.player.level.getBlockEntity(data.readBlockPos());
        return new CollectorMenu(windowId, playerInventory, menu);
    });

    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> register(String id, MenuType.MenuSupplier<T> factory)
    {
        return REGISTER.register(id, () -> new MenuType<>(factory, FeatureFlagSet.of()));
    }
}

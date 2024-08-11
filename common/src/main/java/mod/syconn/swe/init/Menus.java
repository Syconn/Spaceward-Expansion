package mod.syconn.swe.init;

import mod.syconn.swe.common.container.CollectorMenu;
import mod.syconn.swe.common.container.DisperserMenu;
import mod.syconn.swe.common.container.TankMenu;
import mod.syconn.swe.data.menu.PositionMenuData;
import mod.syconn.swe.platform.Services;
import mod.syconn.swe.extra.IMenuData;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.apache.commons.lang3.function.TriFunction;

import java.util.function.Supplier;

public class Menus {

    public static final Supplier<MenuType<TankMenu>> TANK_MENU = register("tank_menu", PositionMenuData.STREAM_CODEC, TankMenu::new);
    public static final Supplier<MenuType<DisperserMenu>> DISPERSER_MENU = register("disperser_menu", PositionMenuData.STREAM_CODEC, DisperserMenu::new);
    public static final Supplier<MenuType<CollectorMenu>> COLLECTOR_MENU = register("collector_menu", PositionMenuData.STREAM_CODEC, CollectorMenu::new);

    public static void init() {}

    private static <T extends AbstractContainerMenu, D extends IMenuData<D>> Supplier<MenuType<T>> register(String id, StreamCodec<RegistryFriendlyByteBuf, D> codec, TriFunction<Integer, Inventory, D, T> function) {
        return Services.REGISTRAR.registerMenuTypeWithData(id, codec, function);
    }
}

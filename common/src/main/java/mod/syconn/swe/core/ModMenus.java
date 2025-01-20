package mod.syconn.swe.core;

import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import mod.syconn.swe.client.screen.CollectorScreen;
import mod.syconn.swe.client.screen.DisperserScreen;
import mod.syconn.swe.client.screen.TankScreen;
import mod.syconn.swe.common.container.CollectorMenu;
import mod.syconn.swe.common.container.DisperserMenu;
import mod.syconn.swe.common.container.TankMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import java.util.function.Supplier;

import static mod.syconn.swe.Constants.MOD;

public class ModMenus {

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(MOD, Registries.MENU);

    public static final Supplier<MenuType<TankMenu>> TANK_MENU = MENUS.register("tank_menu", () -> MenuRegistry.ofExtended(TankMenu::new));
    public static final Supplier<MenuType<DisperserMenu>> DISPERSER_MENU = MENUS.register("disperser_menu", () -> MenuRegistry.ofExtended(DisperserMenu::new));
    public static final Supplier<MenuType<CollectorMenu>> COLLECTOR_MENU = MENUS.register("collector_menu", () -> MenuRegistry.ofExtended(CollectorMenu::new));

    public static void registerScreens(Minecraft minecraft) {
        MenuRegistry.registerScreenFactory(TANK_MENU.get(), TankScreen::new);
        MenuRegistry.registerScreenFactory(DISPERSER_MENU.get(), DisperserScreen::new);
        MenuRegistry.registerScreenFactory(COLLECTOR_MENU.get(), CollectorScreen::new);
    }
}

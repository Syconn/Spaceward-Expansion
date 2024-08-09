package mod.syconn.swe;

import mod.syconn.api.world.data.capability.APICapabilities;
import mod.syconn.swe.common.container.CollectorMenu;
import mod.syconn.swe.common.container.DisperserMenu;
import mod.syconn.swe.common.container.TankMenu;
import mod.syconn.swe.common.crafting.DyedParachuteRecipe;
import mod.syconn.swe.common.crafting.RefillingCanisterRecipe;
import mod.syconn.swe.common.data.attachments.SpaceSuit;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

import java.util.function.Supplier;

public class Registration {

    //TODO LOOK AT FRAMEWORK
    public static final Supplier<MenuType<TankMenu>> TANK_MENU = MENUS.register("tank", () -> IMenuTypeExtension.create((windowId, inv, data) -> new TankMenu(windowId, inv, data.readBlockPos())));
    public static final Supplier<MenuType<DisperserMenu>> DISPERSER_MENU = MENUS.register("disperser_menu", () -> IMenuTypeExtension.create((windowId, inv, data) -> new DisperserMenu(windowId, inv, data.readBlockPos())));
    public static final Supplier<MenuType<CollectorMenu>> COLLECTOR_MENU = MENUS.register("collector_menu", () -> IMenuTypeExtension.create((windowId, inv, data) -> new CollectorMenu(windowId, inv, data.readBlockPos())));

    public static final Supplier<AttachmentType<SpaceSuit>> SPACE_SUIT = ATTACHMENT_TYPES.register("space_suit", () -> AttachmentType.serializable(SpaceSuit::new).build());

    public static final Supplier<SimpleCraftingRecipeSerializer<DyedParachuteRecipe>> PARACHUTE_RECIPE = RECIPE_SERIALIZERS.register("parachute_recipe", () -> new SimpleCraftingRecipeSerializer<>(DyedParachuteRecipe::new));
    public static final Supplier<SimpleCraftingRecipeSerializer<RefillingCanisterRecipe>> REFILLING_CANISTER = RECIPE_SERIALIZERS.register("refilling_canister_recipe", () -> new SimpleCraftingRecipeSerializer<>(RefillingCanisterRecipe::new));


    public static void registerCapabilities(RegisterCapabilitiesEvent event) {

        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, COLLECTOR.get(), (o, v) -> o.getFluidHandler());
        event.registerBlockEntity(APICapabilities.FluidHandler.BLOCK, COLLECTOR.get(), (o, v) -> o.getFluidHandler());
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, TANK.get(), (o, v) -> o.getFluidHandler());
        event.registerBlockEntity(APICapabilities.FluidHandler.BLOCK, TANK.get(), (o, v) -> o.getFluidHandler());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, TANK.get(), (o, v) -> o.getItemHandler());
    }
}
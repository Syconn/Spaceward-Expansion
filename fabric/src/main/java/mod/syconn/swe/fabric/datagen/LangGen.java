package mod.syconn.swe.fabric.datagen;

import mod.syconn.swe.core.ModBlocks;
import mod.syconn.swe.core.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

public class LangGen extends FabricLanguageProvider {

    public LangGen(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    public void generateTranslations(TranslationBuilder builder) {
        builder.add(ModItems.PARACHUTE.get(), "Parachute");
        builder.add(ModItems.SPACE_HELMET.get(), "Space Helmet");
        builder.add(ModItems.SPACE_BOOTS.get(), "Space Boots");
        builder.add(ModItems.SPACE_CHESTPLATE.get(), "Space Chestplate");
        builder.add(ModItems.SPACE_LEGGINGS.get(), "Space Leggings");
        builder.add(ModItems.CANISTER.get(), "Canister");
        builder.add(ModItems.DIAMOND_UPGRADE.get(), "Diamond Upgrade");
        builder.add(ModItems.GOLD_UPGRADE.get(), "Gold Upgrade");
        builder.add(ModItems.IRON_UPGRADE.get(), "Iron Upgrade");
        builder.add(ModItems.EMERALD_UPGRADE.get(), "Emerald Upgrade");
        builder.add(ModItems.NETHERITE_UPGRADE.get(), "Netherite Upgrade");
        builder.add(ModItems.AUTO_REFILL_CANISTER.get(), "Auto Refilling Canister");

        builder.add(ModBlocks.FLUID_TANK.get(), "Fluid Tank");
        builder.add(ModBlocks.OXYGEN_DISPERSER.get(), "Oxygen Disperser");
        builder.add(ModBlocks.CANISTER_FILLER.get(), "Canister Filler");
        builder.add(ModBlocks.OXYGEN_COLLECTOR.get(), "Oxygen Collector");
        builder.add(ModBlocks.FLUID_PIPE.get(), "Fluid Pipe");

        builder.add("itemGroup.swe", "Spaceward Expansion");
        builder.add("swe.fluid.o2", "Oxygen");

        builder.add("death.attack.anoxia", "%1$s ran out of Oxygen");
        builder.add("death.attack.anoxia.player", "%1$s ran out of Oxygen while trying to escape %2$s");
    }
}

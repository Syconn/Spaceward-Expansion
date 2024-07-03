package mod.syconn.swe.client.datagen;

import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;
import mod.syconn.swe.Main;
import mod.syconn.swe.init.ModInit;

public class LangGen extends LanguageProvider {

    public LangGen(PackOutput output) {
        super(output, Main.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(ModInit.PARACHUTE.get(), "Parachute");
        add(ModInit.SPACE_HELMET.get(), "Space Helmet");
        add(ModInit.SPACE_BOOTS.get(), "Space Boots");
        add(ModInit.SPACE_CHESTPLATE.get(), "Space Chestplate");
        add(ModInit.SPACE_LEGGINGS.get(), "Space Leggings");
        add(ModInit.CANISTER.get(), "Canister");
        add(ModInit.WRENCH.get(), "Wrench");
        add(ModInit.DIAMOND_UPGRADE.get(), "Diamond Upgrade");
        add(ModInit.GOLD_UPGRADE.get(), "Gold Upgrade");
        add(ModInit.IRON_UPGRADE.get(), "Iron Upgrade");
        add(ModInit.EMERALD_UPGRADE.get(), "Emerald Upgrade");
        add(ModInit.NETHERITE_UPGRADE.get(), "Netherite Upgrade");
        add(ModInit.O2_BUCKET.get(), "O2 Bucket");
        add(ModInit.AUTO_REFILL_CANISTER.get(), "Auto Refilling Canister");

        add(ModInit.FLUID_PIPE.get(), "Fluid Pipe");
        add(ModInit.FLUID_TANK.get(), "Fluid Tank");
        add(ModInit.OXYGEN_DISPERSER.get(), "Oxygen Disperser");
        add(ModInit.CANISTER_FILLER.get(), "Canister Filler");
        add(ModInit.OXYGEN_COLLECTOR.get(), "Oxygen Collector");
      
        add("itemGroup.space", "Spaceward Expansion");
        add("swe.fluid.o2", "Oxygen");
    }
}

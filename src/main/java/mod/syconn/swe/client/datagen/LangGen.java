package mod.syconn.swe.client.datagen;

import mod.syconn.swe.Registration;
import net.minecraft.data.PackOutput;
import mod.syconn.swe.Main;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class LangGen extends LanguageProvider {

    public LangGen(PackOutput output) {
        super(output, Main.MODID, "en_us");
    }

    protected void addTranslations() {
        add(Registration.PARACHUTE.get(), "Parachute");
        add(Registration.SPACE_HELMET.get(), "Space Helmet");
        add(Registration.SPACE_BOOTS.get(), "Space Boots");
        add(Registration.SPACE_CHESTPLATE.get(), "Space Chestplate");
        add(Registration.SPACE_LEGGINGS.get(), "Space Leggings");
        add(Registration.CANISTER.get(), "Canister");
        add(Registration.WRENCH.get(), "Wrench");
        add(Registration.DIAMOND_UPGRADE.get(), "Diamond Upgrade");
        add(Registration.GOLD_UPGRADE.get(), "Gold Upgrade");
        add(Registration.IRON_UPGRADE.get(), "Iron Upgrade");
        add(Registration.EMERALD_UPGRADE.get(), "Emerald Upgrade");
        add(Registration.NETHERITE_UPGRADE.get(), "Netherite Upgrade");
        add(Registration.O2_BUCKET.get(), "O2 Bucket");
        add(Registration.AUTO_REFILL_CANISTER.get(), "Auto Refilling Canister");

        add(Registration.FLUID_PIPE.get(), "Fluid Pipe");
        add(Registration.FLUID_TANK.get(), "Fluid Tank");
        add(Registration.OXYGEN_DISPERSER.get(), "Oxygen Disperser");
        add(Registration.CANISTER_FILLER.get(), "Canister Filler");
        add(Registration.OXYGEN_COLLECTOR.get(), "Oxygen Collector");
      
        add("itemGroup.space", "Spaceward Expansion");
        add("swe.fluid.o2", "Oxygen");
    }
}

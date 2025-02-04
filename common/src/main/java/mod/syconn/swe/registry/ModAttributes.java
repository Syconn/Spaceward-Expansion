package mod.syconn.swe.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

import static mod.syconn.swe.Constants.MOD;

public class ModAttributes {

    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(MOD, Registries.ATTRIBUTE);

    public static final RegistrySupplier<Attribute> GRAVITY = ATTRIBUTES.register("gravity", () -> new RangedAttribute(MOD + "gravity", 0.08D, -8.0D, 8.0D).setSyncable(true));

}

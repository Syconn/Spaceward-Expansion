package mod.syconn.swe.api.client;

import mod.syconn.swe.api.blockEntity.BaseFluidPipeBE;
import mod.syconn.swe.api.client.screen.FluidPipeScreen;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientHooks {

    public static void openPipeScreen(BaseFluidPipeBE pipeBE) {
        Minecraft.getInstance().setScreen(new FluidPipeScreen(pipeBE));
    }
}

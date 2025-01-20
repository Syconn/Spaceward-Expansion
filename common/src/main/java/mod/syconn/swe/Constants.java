package mod.syconn.swe;

import net.minecraft.resources.ResourceLocation;

public class Constants {

	public static final String MOD = "swe";

	public static final ResourceLocation MOON = withId("moon");

	public static ResourceLocation withId(String path) {
		return new ResourceLocation(MOD, path);
	}
}
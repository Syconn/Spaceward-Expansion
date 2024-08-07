package mod.syconn.swe;

import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {

	public static final String MOD_ID = "swe";
	public static final String MOD_NAME = "Spaceward Expansion";
	public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

	public static ResourceLocation loc(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}
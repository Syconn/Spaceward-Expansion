package mod.syconn.swe.init;

import mod.syconn.swe.data.attachment.IAttachmentType;
import mod.syconn.swe.data.attachment.SpaceSuit;
import mod.syconn.swe.platform.Services;

import java.util.function.Supplier;

public class DataAttachments {
    
    public static Class<SpaceSuit> SPACE_SUIT = register("space_suit", SpaceSuit.class, SpaceSuit::new);

    public static void init() {}

    private static <T extends IAttachmentType<T>> Class<T> register(String id, Class<T> typeClass, Supplier<T> type) {
        return Services.ATTACHED_DATA.registerType(id, typeClass, type);
    }
}

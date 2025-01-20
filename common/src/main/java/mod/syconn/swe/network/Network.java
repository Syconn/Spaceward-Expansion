package mod.syconn.swe.network;

import dev.architectury.networking.NetworkChannel;
import mod.syconn.swe.Constants;

public class Network {
    public static NetworkChannel NETWORK = NetworkChannel.create(Constants.withId("network"));

    public static void init() {

    }
}

package mod.syconn.swe.util.data;

public enum SpaceSlot {

    TANK("empty_canister"),
    PARACHUTE("empty_parachute");

    String loc;

    SpaceSlot(String loc) {
        this.loc = loc;
    }

    public String getLoc() {
        return loc;
    }
}

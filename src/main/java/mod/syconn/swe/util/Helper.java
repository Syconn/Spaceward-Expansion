package mod.syconn.swe.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import mod.syconn.swe.world.inventory.ExtendedPlayerInventory;

import java.util.List;

public class Helper {

    public static ExtendedPlayerInventory inventory(Player p){
        return ((ExtendedPlayerInventory) p.getInventory());
    }

    public static int exportFromDirection(Direction d){
        return switch (d){
            case DOWN, EAST, UP -> 0;
            case NORTH -> 90;
            case SOUTH -> 270;
            case WEST -> 180;
        };
    }

    public static double[] exportPosFromDir(Direction d, boolean i){
        if (i) return switch (d){
                default -> new double[]{0, 0, 0};
                case UP -> new double[]{1.5, 1.7, 0};
                case DOWN -> new double[]{-0.5, -0.7, 0};
            };
        return switch (d){
            default -> new double[]{0, 0, 0};
            case UP -> new double[]{1.5, 1, 0};
            case DOWN -> new double[]{-0.5, 0, 0};
        };
    }

    public static Direction dirToBlockPos(BlockPos pos1, BlockPos pos2){
        for (Direction d : Direction.values()){
            if (pos2.equals(pos1.relative(d))) return d;
        }
        return Direction.NORTH;
    }

    public static Direction dirFromName(String name) {
        for (Direction d : Direction.values()){
            if (d.getName().equalsIgnoreCase(name)){
                return d;
            }
        }
        return null;
    }

    @SafeVarargs
    @SuppressWarnings("Unchecked")
    public static <E> List<E> combineLists(List<E>... lists) {
        List<E> list = lists[0];
        for (int i = 1; i < lists.length; i++) {
            list.addAll(lists[i]);
        }
        return list;
    }

    @SafeVarargs
    @SuppressWarnings("Unchecked")
    public static <E> List<E> removeFromList(List<E> list, List<E>... lists) {
        for (int i = 1; i < lists.length; i++) {
            list.removeAll(lists[i]);
        }
        return list;
    }
}

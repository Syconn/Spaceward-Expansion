package mod.syconn.swe.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class Helper {

//    public static ExtendedPlayerInventory inventory(Player p){
//        return ((ExtendedPlayerInventory) p.getInventory());
//    }

    public static int exportFromDirection(Direction d){
        return switch (d){
            case Direction.DOWN, Direction.EAST, Direction.UP -> 0;
            case Direction.NORTH -> 90;
            case Direction.SOUTH -> 270;
            case Direction.WEST -> 180;
        };
    }

    public static double[] exportPosFromDir(Direction d, boolean i){
        if (i) return switch (d){
                default -> new double[]{0, 0, 0};
                case Direction.UP -> new double[]{1.5, 1.7, 0};
                case Direction.DOWN -> new double[]{-0.5, -0.7, 0};
            };
        return switch (d){
            default -> new double[]{0, 0, 0};
            case Direction.UP -> new double[]{1.5, 1, 0};
            case Direction.DOWN -> new double[]{-0.5, 0, 0};
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
}

package mod.syconn.swe.util.data;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import mod.syconn.swe.blockentities.PipeBlockEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FluidPointSystem {

    private final Map<Direction, FluidPoint> system;

    public FluidPointSystem() {
        system = new HashMap<>();
        for (Direction d : Direction.values()) {
            system.put(d, FluidPoint.Empty());
        }
    }

    public FluidPointSystem(CompoundTag tag) {
        this.system = read(tag);
    }

    public void setEmpty(Direction d) {
        system.put(d, FluidPoint.Empty());
    }

    public boolean update(Direction d, FluidPoint point) {
        if (system.get(d).equals(FluidPoint.Empty()) || system.get(d).exporter != point.exporter){
            system.put(d, point);
            return true;
        } else setEmpty(d);
        return false;
    }

    public void increment(Direction d, int inc) {
        system.put(d, system.get(d).increment(inc));
    }

    public void flip(Direction d) {
        system.put(d, system.get(d).flip());
    }

    public FluidPoint getPoint(Direction d){
        if (d == null) return null;
        return system.get(d);
    }

    public List<FluidPoint> getPoints(){
        List<FluidPoint> fluidPoints = new ArrayList<>();
        for (Direction d : Direction.values()) {
            if (!system.get(d).isEmpty()) fluidPoints.add(system.get(d));
        }
        return fluidPoints;
    }

    public List<FluidPoint> getImports(){
        List<FluidPoint> fluidPoints = new ArrayList<>();
        for (Direction d : Direction.values()) {
            if (!system.get(d).isEmpty() && system.get(d).exporter) fluidPoints.add(system.get(d));
        }
        return fluidPoints;
    }

    public List<FluidPoint> getExports(){
        List<FluidPoint> fluidPoints = new ArrayList<>();
        for (Direction d : Direction.values()) {
            if (!system.get(d).isEmpty() && !system.get(d).exporter) {
                fluidPoints.add(system.get(d));
            }
        }
        return fluidPoints;
    }

    public void handleBlockUpdate(LevelAccessor level){
        for (Direction d : Direction.values()) {
            FluidPoint point = system.get(d);
            if (!point.equals(FluidPoint.Empty())) {
                if (level.getBlockEntity(point.pos) == null){
                    system.put(d, FluidPoint.Empty());
                } else if (!level.getBlockEntity(point.pos).getCapability(ForgeCapabilities.FLUID_HANDLER).isPresent() || level.getBlockEntity(point.pos) instanceof PipeBlockEntity) {
                    system.put(d, FluidPoint.Empty());
                }
            }
        }
    }

    public CompoundTag write() {
        CompoundTag tag = new CompoundTag();
        for (Direction d : Direction.values()){
            tag.put(d.getName(), system.get(d).write());
        }
        return tag;
    }

    private Map<Direction, FluidPoint> read(CompoundTag tag) {
        Map<Direction, FluidPoint> points = new HashMap<>();
        for (Direction d : Direction.values()){
            points.put(d, FluidPoint.read(tag.getCompound(d.getName())));
        }
        return points;
    }

    public String toString() {
        return system.toString();
    }

    public record FluidPoint(BlockPos pos, Direction d, boolean exporter, int priority){
        public static FluidPoint Empty() { return new FluidPoint(BlockPos.ZERO, Direction.NORTH, true, -10); }
        public boolean handlesExport() {
            return exporter && !this.equals(Empty());
        }

        public FluidPoint increment(int inc) { return new FluidPoint(pos, d, exporter, Mth.clamp(priority + inc, -10, 10)); }
        public FluidPoint flip() { return new FluidPoint(pos, d, !exporter, priority); }
        public boolean isEmpty() { return pos.equals(BlockPos.ZERO); }
        public boolean equals(Object obj) { return obj instanceof FluidPoint fp && pos.equals(fp.pos) && d.equals(fp.d) && exporter == fp.exporter && priority == fp.priority; }
        public CompoundTag write() {
            CompoundTag tag = new CompoundTag();
            tag.put("pos", NbtUtils.writeBlockPos(pos));
            tag.putInt("dir", d.get3DDataValue());
            tag.putBoolean("exporter", exporter);
            tag.putInt("priority", priority);
            return tag;
        }

        public String toString() {
            return exporter ? "Importer" : "Exporter";
        }

        public static FluidPoint read(CompoundTag tag) {
            return new FluidPoint(NbtUtils.readBlockPos(tag.getCompound("pos")), Direction.from3DDataValue(tag.getInt("dir")), tag.getBoolean("exporter"), tag.getInt("priority"));
        }
    }
}
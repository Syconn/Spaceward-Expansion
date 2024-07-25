package mod.syconn.api.world.data;

import mod.syconn.api.blocks.AbstractPipeBlock;
import mod.syconn.api.util.NbtHelper;
import mod.syconn.api.util.PipeConnectionTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PipeNetwork {

    private final UUID networkID;
    private final PipeExecutor executor;
    private final List<BlockPos> pipes;

    public PipeNetwork(UUID networkID, Level level, BlockPos firstPipe) {
        this.networkID = networkID;
        this.executor = new PipeExecutor(this);
        this.pipes = new ArrayList<>();
    }

    public PipeNetwork(@NotNull CompoundTag tag, HolderLookup.Provider provider) {
        this.networkID = tag.getUUID("uuid");
        this.executor = new PipeExecutor(this, tag.getCompound("executor"), provider);
        this.pipes = NbtHelper.readPositionList(tag.getCompound("pipes"));
    }

    public boolean addPipe(BlockState state, BlockPos pos) {
        if (state.getBlock() instanceof AbstractPipeBlock) {
            pipes.add(pos);
            addToExecutor(state, pos);
            return true;
        }
        return false;
    }

    public void updatePipe(BlockState state, BlockPos pos) {
        if (pipes.contains(pos) && state.getBlock() instanceof AbstractPipeBlock) {
            executor.resetInteractionPoint(pos);
            addToExecutor(state, pos);
        }
    }

    private void addToExecutor(BlockState state, BlockPos pos) {
        if (state.getValue(AbstractPipeBlock.UP).isInteractionPoint()) executor.addInteractionPoint(pos, Direction.UP, state.getValue(AbstractPipeBlock.UP));
        if (state.getValue(AbstractPipeBlock.DOWN).isInteractionPoint()) executor.addInteractionPoint(pos, Direction.DOWN, state.getValue(AbstractPipeBlock.DOWN));
        if (state.getValue(AbstractPipeBlock.NORTH).isInteractionPoint()) executor.addInteractionPoint(pos, Direction.NORTH, state.getValue(AbstractPipeBlock.NORTH));
        if (state.getValue(AbstractPipeBlock.SOUTH).isInteractionPoint()) executor.addInteractionPoint(pos, Direction.SOUTH, state.getValue(AbstractPipeBlock.SOUTH));
        if (state.getValue(AbstractPipeBlock.EAST).isInteractionPoint()) executor.addInteractionPoint(pos, Direction.EAST, state.getValue(AbstractPipeBlock.EAST));
        if (state.getValue(AbstractPipeBlock.WEST).isInteractionPoint()) executor.addInteractionPoint(pos, Direction.WEST, state.getValue(AbstractPipeBlock.WEST));
    }

    public boolean removePipe(BlockState state, BlockPos pos) {
        if (state.getBlock() instanceof AbstractPipeBlock) {
            pipes.remove(pos);
            executor.resetInteractionPoint(pos);
        }
        return pipes.isEmpty();
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("uuid", networkID);
        tag.put("executor", executor.serializeNBT());
        tag.put("pipes", NbtHelper.writePositionList(pipes));
        return tag;
    }

    public static PipeNetwork deserializeNBT(@NotNull CompoundTag tag, HolderLookup.Provider provider) {
        return new PipeNetwork(tag, provider);
    }

    static class PipeExecutor {
        private final PipeNetwork network;
        private final Map<BlockPos, List<Direction>> imports = new HashMap<>();
        private final Map<BlockPos, List<Direction>> exports = new HashMap<>();
        private final List<Task> tasks = new ArrayList<>();
        private Task lastTask = null;
        private int activeTask = 0;

        public PipeExecutor(PipeNetwork network) {
            this.network = network;
        }

        public PipeExecutor(PipeNetwork network, @NotNull CompoundTag nbt, HolderLookup.Provider provider) {
            this.network = network;
        }

        public void addInteractionPoint(BlockPos pos, Direction direction, PipeConnectionTypes type) {
            if (network.pipes.contains(pos)) {
                if (type.isImport()) addImport(pos, direction);
                if (type.isExport()) addExport(pos, direction);
            }
        }

        public void resetInteractionPoint(BlockPos pos) {
            imports.remove(pos);
            exports.remove(pos);
        }

        public void generateTasks() { // MAY BE TO INTENSIVE

        }

        public void runTasks(Level level, int transferRate) {
            if (!tasks.isEmpty()) {
                Task currentTask = lastTask == null || activeTask > tasks.size() ? tasks.get(0) : tasks.get(activeTask);
                if (currentTask != null) {
                    Task.TaskResult result = currentTask.run(level, lastTask, transferRate);
                    if (result.skip()) activeTask++;
                    if (result.failedLine()) tasks.set(activeTask, generateTask(currentTask.startPos, currentTask.endPos));
                    if (result.failed()) tasks.remove(currentTask);
                    lastTask = currentTask;
                }
            }
        }

        private void addImport(BlockPos pos, Direction direction) {
            if (imports.containsKey(pos) && !imports.get(pos).contains(direction)) imports.get(pos).add(direction);
            else if (imports.containsKey(pos)) imports.put(pos, Arrays.asList(direction));
        }

        private void addExport(BlockPos pos, Direction direction) {
            if (exports.containsKey(pos) && !exports.get(pos).contains(direction)) exports.get(pos).add(direction);
            else if (exports.containsKey(pos)) exports.put(pos, Arrays.asList(direction));
        }

        private Task generateTask(BlockPos start, BlockPos stop) {
            return null;
        }

        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            return tag;
        }
    }

    static class Task {
        private final BlockPos startPos;
        private final Direction startDirection;
        private final BlockPos endPos;
        private final Direction endDirection;
        private final List<BlockPos> directions;
        private TaskResult result;

        public Task(BlockPos startPos, Direction startDirection, BlockPos endPos, Direction endDirection, List<BlockPos> directions) {
            this.startPos = startPos;
            this.startDirection = startDirection;
            this.endPos = endPos;
            this.endDirection = endDirection;
            this.directions = directions;
        }

        public Task(@NotNull CompoundTag tag) {
            this.startPos = NbtUtils.readBlockPos(tag, "startpos").get();
            this.startDirection = Direction.from3DDataValue(tag.getInt("startdirection"));
            this.endPos = NbtUtils.readBlockPos(tag, "endpos").get();
            this.endDirection = Direction.from3DDataValue(tag.getInt("enddirection"));
            this.directions = NbtHelper.readPositionList(tag.getCompound("directions"));
            this.result = TaskResult.fromNumber(tag.getInt("result"));
        }

        private TaskResult setResultT(TaskResult result) {
            this.result = result;
            return result;
        }

        public TaskResult run(Level level, @Nullable Task lastRunTask, int transferRate) {
            IFluidHandler startHandler = level.getCapability(Capabilities.FluidHandler.BLOCK, startPos.relative(startDirection), startDirection.getOpposite());
            IFluidHandler endHandler = level.getCapability(Capabilities.FluidHandler.BLOCK, endPos.relative(endDirection), endDirection.getOpposite());
            if (startHandler == null || endHandler == null) return setResultT(TaskResult.FAILED);
            if (!endHandler.isFluidValid(0, startHandler.getFluidInTank(0)) || startHandler.getFluidInTank(0).isEmpty()
                    || endHandler.getFluidInTank(0).getAmount() >= endHandler.getTankCapacity(0)) return setResultT(TaskResult.SKIP);

            if (lastRunTask != this) {
                endLastTask(level, lastRunTask);
                for (BlockPos pos : directions) {
                    BlockState state = level.getBlockState(pos);
                    if (!(state.getBlock() instanceof AbstractPipeBlock)) return setResultT(TaskResult.FAILED_LINE);
                    // TODO SET FLUID LINE FLUID DISPLAY
                }
            }

            int fill = endHandler.fill(startHandler.getFluidInTank(0).copyWithAmount(Math.min(transferRate, startHandler.getFluidInTank(0).getAmount())), IFluidHandler.FluidAction.SIMULATE);
            startHandler.drain(endHandler.fill(startHandler.getFluidInTank(0).copyWithAmount(fill), IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
            return fill > 0 ? setResultT(TaskResult.SUCCESS) : setResultT(TaskResult.SKIP);
        }

        public void endLastTask(Level level, Task lastRunTask) {
            for (BlockPos pos : lastRunTask.directions) {
                BlockState state = level.getBlockState(pos);
                if (!(state.getBlock() instanceof AbstractPipeBlock));
                    // TODO RESET FLUID LINE FLUID DISPLAY
            }
        }

        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            tag.put("startpos", NbtUtils.writeBlockPos(startPos));
            tag.putInt("startdirection", startDirection.get3DDataValue());
            tag.put("endpos", NbtUtils.writeBlockPos(endPos));
            tag.putInt("enddirection", endDirection.get3DDataValue());
            tag.put("directions", NbtHelper.writePositionList(directions));
            tag.putInt("result", result.number);
            return tag;
        }

        enum TaskResult {
            SKIP(0), // cant start task, skip task
            SUCCESS(1), // successful run, rerun task
            FAILED_LINE(2), // task failed -r line, remake task
            FAILED(3); // task failed, remove task

            final int number;

            TaskResult(int number) {
                this.number = number;
            }

            boolean skip() {
                return this == SKIP;
            }

            boolean failedLine() {
                return this == FAILED_LINE;
            }

            boolean failed() {
                return this == FAILED;
            }

            static TaskResult fromNumber(int number) {
                for (TaskResult result : values()) if (result.number == number) return result;
                return SKIP;
            }
        }
    }
}

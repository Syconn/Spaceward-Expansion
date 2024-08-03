package mod.syconn.swe2.api.world.data;

import mod.syconn.swe2.api.blockEntity.BaseFluidPipeBE;
import mod.syconn.swe2.api.blocks.AbstractPipeBlock;
import mod.syconn.swe2.api.util.NbtHelper;
import mod.syconn.swe2.api.util.PipeConnectionTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
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
        addPipe(level.getBlockState(firstPipe), firstPipe);
    }

    public PipeNetwork(UUID networkID, Level level, List<BlockPos> pipes) {
        this.networkID = networkID;
        this.executor = new PipeExecutor(this);
        this.pipes = new ArrayList<>();
        addAllPipes(level, pipes);
    }

    public PipeNetwork(@NotNull CompoundTag tag) {
        this.networkID = tag.getUUID("uuid");
        this.executor = new PipeExecutor(this, tag.getCompound("executor"));
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

    public void addAllPipes(Level level, List<BlockPos> pipes) {
        pipes.forEach(pos -> addPipe(level.getBlockState(pos), pos));
    }

    public boolean removePipe(BlockPos pos) {
        pipes.remove(pos);
        executor.resetInteractionPoint(pos);
        return pipes.isEmpty();
    }

    public void updatePipe(Level level, BlockPos pos) {
        if (pipes.contains(pos) && level.getBlockState(pos).getBlock() instanceof AbstractPipeBlock) {
            executor.resetInteractionPoint(pos);
            addToExecutor(level.getBlockState(pos), pos);
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

    public void tick(ServerLevel level) {
        executor.runTasks(level, 250);
    }

    public List<BlockPos> getPipes() {
        return pipes;
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("uuid", networkID);
        tag.put("executor", executor.serializeNBT());
        tag.put("pipes", NbtHelper.writePositionList(pipes));
        return tag;
    }

    public static PipeNetwork deserializeNBT(@NotNull CompoundTag tag) {
        return new PipeNetwork(tag);
    }

    static class PipeExecutor {
        private final PipeNetwork network;
        private final Map<BlockPos, List<Direction>> imports;
        private final Map<BlockPos, List<Direction>> exports;
        private final List<BlockPos> interactionPoint;
        private final List<Task> tasks;
        private int activeTask = 0;

        public PipeExecutor(PipeNetwork network) {
            this.network = network;
            imports = new HashMap<>();
            exports = new HashMap<>();
            interactionPoint = new ArrayList<>();
            tasks = new ArrayList<>();
        }

        public PipeExecutor(PipeNetwork network, CompoundTag tag) {
            this.network = network;
            Map<BlockPos, List<Direction>> imports = new HashMap<>();
            tag.getList("imports", Tag.TAG_COMPOUND).forEach(nbt -> {
                CompoundTag data = (CompoundTag) nbt;
                imports.put(NbtUtils.readBlockPos(data, "pos").get(), NbtHelper.readDirectionList(data.getCompound("directions")));
            });
            this.imports = imports;
            Map<BlockPos, List<Direction>> exports = new HashMap<>();
            tag.getList("exports", Tag.TAG_COMPOUND).forEach(nbt -> {
                CompoundTag data = (CompoundTag) nbt;
                exports.put(NbtUtils.readBlockPos(data, "pos").get(), NbtHelper.readDirectionList(data.getCompound("directions")));
            });
            this.exports = exports;
            this.interactionPoint = NbtHelper.readPositionList(tag.getCompound("interaction_point"));
            List<Task> tasks = new ArrayList<>();
            tag.getList("tasks", Tag.TAG_COMPOUND).forEach(nbt -> tasks.add(new Task((CompoundTag) nbt)));
            this.tasks = tasks;
            this.activeTask = tag.getInt("active_task");
        }

        public void addInteractionPoint(BlockPos pos, Direction direction, PipeConnectionTypes type) {
            if (network.pipes.contains(pos)) {
                if (type.isInteractionPoint() && !interactionPoint.contains(pos)) interactionPoint.add(pos);
                if (type.isImport()) addImport(pos, direction);
                if (type.isExport()) addExport(pos, direction);
                generatePositionalTask(pos, direction, type);
            }
        }

        public void resetInteractionPoint(BlockPos pos) {
            interactionPoint.remove(pos);
            imports.remove(pos);
            exports.remove(pos);
            removeTasksForPosition(pos, PipeConnectionTypes.BOTH);
        }

        private void generatePositionalTask(BlockPos pos, Direction direction, PipeConnectionTypes type) {
            removeTasksForPosition(pos, type);
            if (type.isExport()) {
                for (Map.Entry<BlockPos, List<Direction>> importEntry : imports.entrySet()) {
                    for (Direction importDirection : importEntry.getValue()) {
                        if (pos.equals(importEntry.getKey()) && direction.equals(importDirection)) continue;
                        tasks.add(new Task(pos, direction, importEntry.getKey(), importDirection, network.pipes));
                    }
                }
            }
            if (type.isImport()) {
                for (Map.Entry<BlockPos, List<Direction>> exportEntry : exports.entrySet()) {
                    for (Direction exportDirection : exportEntry.getValue()) {
                        if (pos.equals(exportEntry.getKey()) && direction.equals(exportDirection)) continue;
                        tasks.add(new Task(exportEntry.getKey(), exportDirection, pos, direction, network.pipes));
                    }
                }
            }
        }

        public void runTasks(Level level, int transferRate) {
            if (!tasks.isEmpty()) {
                if (activeTask >= tasks.size()) activeTask = 0;
                Task currentTask = tasks.get(activeTask);
                Task.TaskResult result = currentTask.run(level, transferRate);
                if (result.skip()) {
                    resetPipeFluid(level, currentTask);
                    activeTask++;
                }
                else if (result.failedLine()) tasks.set(activeTask, generateSpecificTask(currentTask.startPos, currentTask.startDirection, currentTask.endPos, currentTask.endDirection));
                else if (result.failed()) tasks.remove(currentTask);
            }
        }

        private Task generateSpecificTask(BlockPos startPosition, Direction startDirection, BlockPos stopPosition, Direction stopDirection) {
            return new Task(startPosition, startDirection, stopPosition, stopDirection, network.pipes);
        }

        private void removeTasksForPosition(BlockPos pos, PipeConnectionTypes type) {
            List<Task> removeTask = new ArrayList<>();
            tasks.forEach(task -> { if (task.hasPoint(pos, type)) removeTask.add(task); });
            removeTask.forEach(tasks::remove);
        }

        private void addImport(BlockPos pos, Direction direction) {
            if (imports.containsKey(pos) && !imports.get(pos).contains(direction)) imports.get(pos).add(direction);
            else if (!imports.containsKey(pos)) imports.put(pos, Collections.singletonList(direction));
        }

        private void addExport(BlockPos pos, Direction direction) {
            if (exports.containsKey(pos) && !exports.get(pos).contains(direction)) exports.get(pos).add(direction);
            else if (!exports.containsKey(pos)) exports.put(pos, Collections.singletonList(direction));
        }

        private void resetPipeFluid(Level level, Task currentTask) {
            if (level.getBlockEntity(currentTask.startPos) instanceof BaseFluidPipeBE pipeBE && pipeBE.hasFluid()) {
                for (BlockPos pos : currentTask.directions) if (level.getBlockEntity(pos) instanceof BaseFluidPipeBE pipeBE2) pipeBE2.setFluid(Fluids.EMPTY);
            }
        }

        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            ListTag importList = new ListTag();
            imports.forEach(((pos, directions) -> {
                CompoundTag data = new CompoundTag();
                data.put("pos", NbtUtils.writeBlockPos(pos));
                data.put("directions", NbtHelper.writeDirectionList(directions));
                importList.add(data);
            }));
            tag.put("imports", importList);
            ListTag exportList = new ListTag();
            imports.forEach(((pos, directions) -> {
                CompoundTag data = new CompoundTag();
                data.put("pos", NbtUtils.writeBlockPos(pos));
                data.put("directions", NbtHelper.writeDirectionList(directions));
                exportList.add(data);
            }));
            tag.put("exports", exportList);
            tag.put("interaction_point", NbtHelper.writePositionList(interactionPoint));
            ListTag taskList = new ListTag();
            tasks.forEach(task -> taskList.add(task.serializeNBT()));
            tag.put("tasks", taskList);
            tag.putInt("active_task", this.activeTask);
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
            if (tag.contains("result")) this.result = TaskResult.fromNumber(tag.getInt("result"));
        }

        private TaskResult setResultT(TaskResult result) {
            this.result = result;
            return result;
        }

        public TaskResult run(Level level, int transferRate) {
            IFluidHandler startHandler = level.getCapability(Capabilities.FluidHandler.BLOCK, startPos.relative(startDirection), startDirection.getOpposite());
            IFluidHandler endHandler = level.getCapability(Capabilities.FluidHandler.BLOCK, endPos.relative(endDirection), endDirection.getOpposite());
            if (startHandler == null || endHandler == null) return setResultT(TaskResult.FAILED);
            if (!endHandler.isFluidValid(0, startHandler.getFluidInTank(0)) || startHandler.getFluidInTank(0).isEmpty() || endHandler.getFluidInTank(0).getAmount() >= endHandler.getTankCapacity(0)) return setResultT(TaskResult.SKIP);
            if (level.getBlockEntity(startPos) instanceof BaseFluidPipeBE pipeBE && !pipeBE.getFluid().is(startHandler.getFluidInTank(0).getFluid())) {
                for (BlockPos pos : directions) {
                    if (level.getBlockEntity(pos) instanceof BaseFluidPipeBE pipeBE2) pipeBE2.setFluid(startHandler.getFluidInTank(0).getFluid());
                    else return setResultT(TaskResult.FAILED_LINE);
                }
            }
            int fill = endHandler.fill(startHandler.getFluidInTank(0).copyWithAmount(Math.min(transferRate, startHandler.getFluidInTank(0).getAmount())), IFluidHandler.FluidAction.SIMULATE);
            startHandler.drain(endHandler.fill(startHandler.getFluidInTank(0).copyWithAmount(fill), IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
            return fill > 0 ? setResultT(TaskResult.SUCCESS) : setResultT(TaskResult.SKIP);
        }

        public boolean hasPoint(BlockPos pos, PipeConnectionTypes type) {
            return startPos.equals(pos) && type.isImport() || endPos.equals(pos) && type.isExport();
        }

        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            tag.put("startpos", NbtUtils.writeBlockPos(startPos));
            tag.putInt("startdirection", startDirection.get3DDataValue());
            tag.put("endpos", NbtUtils.writeBlockPos(endPos));
            tag.putInt("enddirection", endDirection.get3DDataValue());
            tag.put("directions", NbtHelper.writePositionList(directions));
            if (result != null) tag.putInt("result", result.number);
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

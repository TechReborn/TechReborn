package techreborn.blockentity.machine.tier0.block;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import reborncore.common.screen.builder.BlockEntityScreenHandlerBuilder;

public interface BlockProcessor {

	ProcessingStatus onTick(World world, BlockPos positionInFront);
	ProcessingStatus getStatusEnum();

	int getCurrentTickTime();
	int getTickTime();

	void readNbt(NbtCompound tag);
	void writeNbt(NbtCompound tag);
	BlockEntityScreenHandlerBuilder syncNbt(BlockEntityScreenHandlerBuilder builder);

	default int getProgress() {
		return (int) (((double) getCurrentTickTime() / (double) getTickTime()) * 100);
	}
}

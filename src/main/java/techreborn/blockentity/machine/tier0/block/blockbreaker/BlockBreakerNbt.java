package techreborn.blockentity.machine.tier0.block.blockbreaker;

import net.minecraft.nbt.NbtCompound;
import reborncore.client.screen.builder.BlockEntityScreenHandlerBuilder;
import techreborn.blockentity.machine.tier0.block.ProcessingStatus;

/**
 * <b>Class handling Nbt values of the Block Breaker</b>
 * <br>
 * Inherited by the {@link BlockBreakerProcessor} for keeping its values in sync when saving/loading a map
 *
 * @author SimonFlapse
 * @see BlockBreakerProcessor
 */
class BlockBreakerNbt {
	protected int breakTime;
	protected int currentBreakTime;
	protected ProcessingStatus status = BlockBreakerStatus.IDLE;

	public void writeNbt(NbtCompound tag) {
		tag.putInt("breakTime", this.breakTime);
		tag.putInt("currentBreakTime", this.currentBreakTime);
		tag.putInt("blockBreakerStatus", getStatus());
	}

	public void readNbt(NbtCompound tag) {
		this.breakTime = tag.getInt("breakTime");
		this.currentBreakTime = tag.getInt("currentBreakTime");
		setStatus(tag.getInt("blockBreakerStatus"));
	}

	public BlockEntityScreenHandlerBuilder syncNbt(BlockEntityScreenHandlerBuilder builder) {
		return builder.sync(this::getBreakTime, this::setBreakTime)
			.sync(this::getCurrentBreakTime, this::setCurrentBreakTime)
			.sync(this::getStatus, this::setStatus);
	}

	protected int getBreakTime() {
		return breakTime;
	}

	protected void setBreakTime(int breakTime) {
		this.breakTime = breakTime;
	}

	protected int getCurrentBreakTime() {
		return currentBreakTime;
	}

	protected void setCurrentBreakTime(int currentBreakTime) {
		this.currentBreakTime = currentBreakTime;
	}

	protected int getStatus() {
		return status.getStatusCode();
	}

	protected void setStatus(int status) {
		this.status = BlockBreakerStatus.values()[status];
	}
}

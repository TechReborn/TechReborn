package techreborn.blockentity.machine.tier0.block.blockplacer;

import net.minecraft.nbt.NbtCompound;
import reborncore.client.screen.builder.BlockEntityScreenHandlerBuilder;
import techreborn.blockentity.machine.tier0.block.ProcessingStatus;

/**
 * <b>Class handling Nbt values of the Block Placer</b>
 * <br>
 * Inherited by the {@link BlockPlacerProcessor} for keeping its values in sync when saving/loading a map
 *
 * @author SimonFlapse
 * @see BlockPlacerProcessor
 */
class BlockPlacerNbt {
	protected int placeTime;
	protected int currentPlaceTime;
	protected ProcessingStatus status = BlockPlacerStatus.IDLE;

	public void writeNbt(NbtCompound tag) {
		tag.putInt("placeTime", this.placeTime);
		tag.putInt("currentPlaceTime", this.currentPlaceTime);
		tag.putInt("blockPlacerStatus", getStatus());
	}

	public void readNbt(NbtCompound tag) {
		this.placeTime = tag.getInt("placeTime");
		this.currentPlaceTime = tag.getInt("currentPlaceTime");
		setStatus(tag.getInt("blockPlacerStatus"));
	}

	public BlockEntityScreenHandlerBuilder syncNbt(BlockEntityScreenHandlerBuilder builder) {
		return builder.sync(this::getPlaceTime, this::setPlaceTime)
			.sync(this::getCurrentPlaceTime, this::setCurrentPlaceTime)
			.sync(this::getStatus, this::setStatus);
	}

	protected int getPlaceTime() {
		return placeTime;
	}

	protected void setPlaceTime(int placeTime) {
		this.placeTime = placeTime;
	}

	protected int getCurrentPlaceTime() {
		return currentPlaceTime;
	}

	protected void setCurrentPlaceTime(int currentPlaceTime) {
		this.currentPlaceTime = currentPlaceTime;
	}

	protected int getStatus() {
		return status.getStatusCode();
	}

	protected void setStatus(int status) {
		this.status = BlockPlacerStatus.values()[status];
	}
}

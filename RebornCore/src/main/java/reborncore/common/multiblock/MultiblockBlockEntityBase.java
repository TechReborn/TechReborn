/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2021 TeamReborn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package reborncore.common.multiblock;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import reborncore.RebornCore;
import reborncore.api.blockentity.UnloadHandler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Base logic class for Multiblock-connected {@link BlockEntity} entities. Most multiblock
 * machines should derive from this and implement their game logic in certain
 * abstract methods.
 */
public abstract class MultiblockBlockEntityBase extends IMultiblockPart implements BlockEntityTicker, UnloadHandler {
	private MultiblockControllerBase controller;
	private boolean visited;

	private boolean saveMultiblockData;
	private NbtCompound cachedMultiblockData;
	//private boolean paused;

	public MultiblockBlockEntityBase(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		controller = null;
		visited = false;
		saveMultiblockData = false;
		//paused = false;
		cachedMultiblockData = null;
	}

	// /// Multiblock Connection Base Logic
	@Override
	public Set<MultiblockControllerBase> attachToNeighbors() {
		Set<MultiblockControllerBase> controllers = null;
		MultiblockControllerBase bestController = null;

		// Look for a compatible controller in our neighboring parts.
		IMultiblockPart[] partsToCheck = getNeighboringParts();
		for (IMultiblockPart neighborPart : partsToCheck) {
			if (neighborPart.isConnected()) {
				MultiblockControllerBase candidate = neighborPart.getMultiblockController();
				if (!candidate.getClass().equals(this.getMultiblockControllerType())) {
					// Skip multiblocks with incompatible types
					continue;
				}

				if (controllers == null) {
					controllers = new HashSet<>();
					bestController = candidate;
				} else if (!controllers.contains(candidate) && candidate.shouldConsume(bestController)) {
					bestController = candidate;
				}

				controllers.add(candidate);
			}
		}

		// If we've located a valid neighboring controller, attach to it.
		if (bestController != null) {
			// attachBlock will call onAttached, which will set the controller.
			this.controller = bestController;
			bestController.attachBlock(this);
		}

		return controllers;
	}

	@Override
	public void assertDetached() {
		if (this.controller != null) {
			RebornCore.LOGGER.info(
				String.format("[assert] Part @ (%d, %d, %d) should be detached already, but detected that it was not. This is not a fatal error, and will be repaired, but is unusual.",
					getPos().getX(), getPos().getY(), getPos().getZ()));
			this.controller = null;
		}
	}

	// /// Overrides from base BlockEntity methods

	@Override
	public void readNbt(NbtCompound data) {
		super.readNbt(data);

		// We can't directly initialize a multiblock controller yet, so we cache
		// the data here until
		// we receive a "validate()" call, which creates the controller and hands
		// off the cached data.
		if (data.contains("multiblockData")) {
			this.cachedMultiblockData = data.getCompound("multiblockData");
		}
	}

	@Override
	public void writeNbt(NbtCompound data) {
		super.writeNbt(data);

		if (isMultiblockSaveDelegate() && isConnected()) {
			NbtCompound multiblockData = new NbtCompound();
			this.controller.write(multiblockData);
			data.put("multiblockData", multiblockData);
		}
	}

	@Override
	public void markRemoved() {
		detachSelf(false);
		super.markRemoved();
	}

	/**
	 * Called from Minecraft's {@link BlockEntity} entity loop, after all {@link BlockEntity} entities have
	 * been ticked, as the chunk in which this {@link BlockEntity} entity is contained is
	 * unloading.
	 */
	@Override
	public void onUnload() {
		detachSelf(true);
	}

	/**
	 * <p>
	 *  This is called when a block is being marked as valid by the chunk, but
	 *  has not yet fully been placed into the world's {@link BlockEntity} cache.
	 * </p>
	 *
	 * <p>
	 *  {@code this.worldObj}, {@code xCoord}, {@code yCoord} and {@code zCoord}
	 *  have been initialized, but any attempts to read data about the world can
	 *  cause infinite loops - if you call {@code getBlockEntity} on this
	 *  {@link BlockEntity}'s coordinate from within {@code validate()}, you will
	 *  blow your call stack.
	 * </p>
	 *
	 * <p>TL;DR: Here there be dragons.</p>
	 */
	@Override
	public void cancelRemoval() {
		super.cancelRemoval();
		MultiblockRegistry.onPartAdded(this.getWorld(), this);
	}

	// Network Communication
	@Override
	public BlockEntityUpdateS2CPacket toUpdatePacket() {
		NbtCompound packetData = new NbtCompound();
		encodeDescriptionPacket(packetData);
		return BlockEntityUpdateS2CPacket.create(this);
	}

	// /// Things to override in most implementations (IMultiblockPart)

	/**
	 * Override this to easily modify the description packet's data without
	 * having to worry about sending the packet itself. Decode this data in
	 * {@link #decodeDescriptionPacket}.
	 *
	 * @param packetData {@link NbtCompound} An NBT compound tag into which
	 *                   you should write your custom description data.
	 */
	protected void encodeDescriptionPacket(NbtCompound packetData) {
		if (this.isMultiblockSaveDelegate() && isConnected()) {
			NbtCompound tag = new NbtCompound();
			getMultiblockController().formatDescriptionPacket(tag);
			packetData.put("multiblockData", tag);
		}
	}

	/**
	 * Override this to easily read in data from a {@link BlockEntity}'s description
	 * packet. Encoded in {@link #encodeDescriptionPacket}.
	 *
	 * @param packetData {@link NbtCompound} The NBT data from the {@link BlockEntity}
	 *                   entity's description packet.
	 */
	protected void decodeDescriptionPacket(NbtCompound packetData) {
		if (packetData.contains("multiblockData")) {
			NbtCompound tag = packetData.getCompound("multiblockData");
			if (isConnected()) {
				getMultiblockController().decodeDescriptionPacket(tag);
			} else {
				// This part hasn't been added to a machine yet, so cache the data.
				this.cachedMultiblockData = tag;
			}
		}
	}

	@Override
	public boolean hasMultiblockSaveData() {
		return this.cachedMultiblockData != null;
	}

	@Override
	public NbtCompound getMultiblockSaveData() {
		return this.cachedMultiblockData;
	}

	@Override
	public void onMultiblockDataAssimilated() {
		this.cachedMultiblockData = null;
	}

	// /// Game logic callbacks (IMultiblockPart)

	@Override
	public abstract void onMachineAssembled(MultiblockControllerBase multiblockControllerBase);

	@Override
	public abstract void onMachineBroken();

	@Override
	public abstract void onMachineActivated();

	@Override
	public abstract void onMachineDeactivated();

	// /// Miscellaneous multiblock-assembly callbacks and support methods
	// (IMultiblockPart)

	@Override
	public boolean isConnected() {
		return (controller != null);
	}

	@Override
	public MultiblockControllerBase getMultiblockController() {
		return controller;
	}

	@Override
	public BlockPos getWorldLocation() {
		return this.getPos();
	}

	@Override
	public void becomeMultiblockSaveDelegate() {
		this.saveMultiblockData = true;
	}

	@Override
	public void forfeitMultiblockSaveDelegate() {
		this.saveMultiblockData = false;
	}

	@Override
	public boolean isMultiblockSaveDelegate() {
		return this.saveMultiblockData;
	}

	@Override
	public void setUnvisited() {
		this.visited = false;
	}

	@Override
	public void setVisited() {
		this.visited = true;
	}

	@Override
	public boolean isVisited() {
		return this.visited;
	}

	@Override
	public void onAssimilated(MultiblockControllerBase newController) {
		assert (this.controller != newController);
		this.controller = newController;
	}

	@Override
	public void onAttached(MultiblockControllerBase newController) {
		this.controller = newController;
	}

	@Override
	public void onDetached(MultiblockControllerBase oldController) {
		this.controller = null;
	}

	@Override
	public abstract MultiblockControllerBase createNewMultiblock();

	@Override
	public IMultiblockPart[] getNeighboringParts() {
		BlockEntity te;
		List<IMultiblockPart> neighborParts = new ArrayList<>();
		BlockPos neighborPosition, partPosition = this.getWorldLocation();

		for (Direction facing : Direction.values()) {

			neighborPosition = partPosition.offset(facing);
			te = this.world.getBlockEntity(neighborPosition);

			if (te instanceof IMultiblockPart) {
				neighborParts.add((IMultiblockPart) te);
			}
		}

		return neighborParts.toArray(new IMultiblockPart[0]);
	}

	@Override
	public void onOrphaned(MultiblockControllerBase controller, int oldSize, int newSize) {
		this.markDirty();
		getWorld().markDirty(getPos());
	}

	// /// Helper functions for notifying neighboring blocks
	protected void notifyNeighborsOfBlockChange() {
		world.updateNeighborsAlways(getPos(), getCachedState().getBlock());
	}

	protected void notifyNeighborsOfBlockEntityChange() {
		world.updateNeighborsAlways(getPos(), getCachedState().getBlock());
	}

	// /// Private/Protected Logic Helpers

	/**
	 * Detaches this block from its controller. Calls {@link MultiblockControllerBase#detachBlock}
	 * and clears the controller member.
	 */
	protected void detachSelf(boolean chunkUnloading) {
		if (this.controller != null) {
			// Clean part out of controller
			this.controller.detachBlock(this, chunkUnloading);

			// The above should call onDetached, but, just in case...
			this.controller = null;
		}

		// Clean part out of lists in the registry
		MultiblockRegistry.onPartRemovedFromWorld(getWorld(), this);
	}
}

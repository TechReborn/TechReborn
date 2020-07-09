/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 TechReborn
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

package techreborn.multiblocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import reborncore.common.multiblock.IMultiblockPart;
import reborncore.common.multiblock.MultiblockControllerBase;
import reborncore.common.multiblock.MultiblockValidationException;
import reborncore.common.multiblock.rectangular.RectangularMultiblockBlockEntityBase;
import reborncore.common.multiblock.rectangular.RectangularMultiblockControllerBase;
import techreborn.blocks.misc.BlockMachineCasing;

public class MultiBlockCasing extends RectangularMultiblockControllerBase {

	public boolean hasLava;
	public boolean isStar = false;
	public int height = 0;

	public MultiBlockCasing(World world) {
		super(world);
	}

	public String getInfo() {
		String value = "Intact";
		try {
			isMachineWhole();
		} catch (MultiblockValidationException e) {
			e.printStackTrace();
			value = e.getLocalizedMessage();
		}
		return value;
	}

	/**
	 * @return True if the machine is "whole" and should be assembled. False
	 * otherwise.
	 */
	@Override
	protected void isMachineWhole() throws MultiblockValidationException {
		if (connectedParts.size() < getMinimumNumberOfBlocksForAssembledMachine()) {
			throw new MultiblockValidationException("Machine is too small.");
		}

		BlockPos maximumCoord = getMaximumCoord();
		BlockPos minimumCoord = getMinimumCoord();

		// Quickly check for exceeded dimensions
		int deltaX = maximumCoord.getX() - minimumCoord.getX() + 1;
		int deltaY = maximumCoord.getY() - minimumCoord.getY() + 1;
		int deltaZ = maximumCoord.getZ() - minimumCoord.getZ() + 1;

		int maxX = getMaximumXSize();
		int maxY = getMaximumYSize();
		int maxZ = getMaximumZSize();
		int minX = getMinimumXSize();
		int minY = getMinimumYSize();
		int minZ = getMinimumZSize();

		if (maxX > 0 && deltaX > maxX) {
			throw new MultiblockValidationException(
					String.format("Machine is too large, it may be at most %d blocks in the X dimension", maxX));
		}
		if (maxY > 0 && deltaY > maxY) {
			throw new MultiblockValidationException(
					String.format("Machine is too large, it may be at most %d blocks in the Y dimension", maxY));
		}
		if (maxZ > 0 && deltaZ > maxZ) {
			throw new MultiblockValidationException(
					String.format("Machine is too large, it may be at most %d blocks in the Z dimension", maxZ));
		}
		if (deltaX < minX) {
			throw new MultiblockValidationException(
					String.format("Machine is too small, it must be at least %d blocks in the X dimension", minX));
		}
		if (deltaY < minY) {
			throw new MultiblockValidationException(
					String.format("Machine is too small, it must be at least %d blocks in the Y dimension", minY));
		}
		if (deltaZ < minZ) {
			throw new MultiblockValidationException(
					String.format("Machine is too small, it must be at least %d blocks in the Z dimension", minZ));
		}
		height = deltaY;

		// if(checkIfStarShape(minimumCoord.x, minimumCoord.y, minimumCoord.z)){
		// isStar = true;
		// return;
		// } else {
		// isStar = false;
		// }

		if (deltaY < minY) {
			throw new MultiblockValidationException(
					String.format("Machine is too small, it must be at least %d blocks in the Y dimension", minY));
		}

		// Now we run a simple check on each block within that volume.
		// Any block deviating = NO DEAL SIR
		BlockEntity te;
		RectangularMultiblockBlockEntityBase part;
		Class<? extends RectangularMultiblockControllerBase> myClass = this.getClass();

		for (int x = minimumCoord.getX(); x <= maximumCoord.getX(); x++) {
			for (int y = minimumCoord.getY(); y <= maximumCoord.getY(); y++) {
				for (int z = minimumCoord.getZ(); z <= maximumCoord.getZ(); z++) {
					// Okay, figure out what sort of block this should be.

					te = this.worldObj.getBlockEntity(new BlockPos(x, y, z));
					if (te instanceof RectangularMultiblockBlockEntityBase) {
						part = (RectangularMultiblockBlockEntityBase) te;

						// Ensure this part should actually be allowed within a
						// cube of this controller's type
						if (!myClass.equals(part.getMultiblockControllerType())) {
							throw new MultiblockValidationException(
									String.format("Part @ %d, %d, %d is incompatible with machines of type %s", x, y, z,
											myClass.getSimpleName()));
						}
					} else {
						// This is permitted so that we can incorporate certain
						// non-multiblock parts inside interiors
						part = null;
					}

					// Validate block type against both part-level and
					// material-level validators.
					int extremes = 0;
					if (x == minimumCoord.getX()) {
						extremes++;
					}
					if (y == minimumCoord.getY()) {
						extremes++;
					}
					if (z == minimumCoord.getZ()) {
						extremes++;
					}

					if (x == maximumCoord.getX()) {
						extremes++;
					}
					if (y == maximumCoord.getY()) {
						extremes++;
					}
					if (z == maximumCoord.getZ()) {
						extremes++;
					}

					if (extremes >= 2) {
						if (part != null) {
							part.isGoodForFrame();
						} else {
							isBlockGoodForFrame(this.worldObj, x, y, z);
						}
					} else if (extremes == 1) {
						if (y == maximumCoord.getY()) {
							if (part != null) {
								part.isGoodForTop();
							} else {
								isBlockGoodForTop(this.worldObj, x, y, z);
							}
						} else if (y == minimumCoord.getY()) {
							if (part != null) {
								part.isGoodForBottom();
							} else {
								isBlockGoodForBottom(this.worldObj, x, y, z);
							}
						} else {
							// Side
							if (part != null) {
								part.isGoodForSides();
							} else {
								isBlockGoodForSides(this.worldObj, x, y, z);
							}
						}
					} else {
						if (part != null) {
							part.isGoodForInterior();
						} else {
							isBlockGoodForInterior(this.worldObj, x, y, z);
						}
					}
				}
			}
		}
	}

	@Override
	public void onAttachedPartWithMultiblockData(IMultiblockPart part, CompoundTag data) {

	}

	@Override
	protected void onBlockAdded(IMultiblockPart newPart) {
	}

	@Override
	protected void onBlockRemoved(IMultiblockPart oldPart) {

	}

	@Override
	protected void onMachineAssembled() {

	}

	@Override
	protected void onMachineRestored() {

	}

	@Override
	protected void onMachinePaused() {

	}

	@Override
	protected void onMachineDisassembled() {

	}

	@Override
	protected int getMinimumNumberOfBlocksForAssembledMachine() {
		return 1;
	}

	@Override
	protected int getMaximumXSize() {
		return 3;
	}

	@Override
	protected int getMaximumZSize() {
		return 3;
	}

	@Override
	protected int getMaximumYSize() {
		return 4;
	}

	@Override
	protected int getMinimumXSize() {
		return 3;
	}

	@Override
	protected int getMinimumYSize() {
		return 3;
	}

	@Override
	protected int getMinimumZSize() {
		return 3;
	}

	@Override
	protected void onAssimilate(MultiblockControllerBase assimilated) {

	}

	@Override
	protected void onAssimilated(MultiblockControllerBase assimilator) {

	}

	@Override
	protected boolean updateServer() {
		return true;
	}

	@Override
	protected void updateClient() {

	}

	@Override
	public void write(CompoundTag data) {

	}

	@Override
	public void read(CompoundTag data) {

	}

	@Override
	public void formatDescriptionPacket(CompoundTag data) {

	}

	@Override
	public void decodeDescriptionPacket(CompoundTag data) {

	}

	@Override
	protected void isBlockGoodForInterior(World world, int x, int y, int z) throws MultiblockValidationException {
		BlockState state = world.getBlockState(new BlockPos(x, y, z));

		if (state.getMaterial().equals(Material.AIR)) {

		} else if (state.getMaterial().equals(Material.LAVA)) {
			hasLava = true;
		} else {
			super.isBlockGoodForInterior(world, x, y, z);
		}
	}

	@Override
	protected void isBlockGoodForFrame(World world, int x, int y, int z) throws MultiblockValidationException {
		Block block = world.getBlockState(new BlockPos(x, y, z)).getBlock();
		if (block instanceof BlockMachineCasing) {

		} else {
			super.isBlockGoodForFrame(world, x, y, z);
		}
	}

}

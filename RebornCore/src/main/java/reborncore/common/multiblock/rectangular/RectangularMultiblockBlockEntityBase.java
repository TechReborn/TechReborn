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

package reborncore.common.multiblock.rectangular;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import reborncore.common.multiblock.MultiblockControllerBase;
import reborncore.common.multiblock.MultiblockBlockEntityBase;
import reborncore.common.multiblock.MultiblockValidationException;

public abstract class RectangularMultiblockBlockEntityBase extends MultiblockBlockEntityBase {

	PartPosition position;
	Direction outwards;

	public RectangularMultiblockBlockEntityBase(BlockEntityType<?> blockEntityType) {
		super(blockEntityType);

		position = PartPosition.Unknown;
		outwards = null;
	}

	// Positional Data
	public Direction getOutwardsDir() {
		return outwards;
	}

	public PartPosition getPartPosition() {
		return position;
	}

	// Handlers from MultiblockBlockEntityBase
	@Override
	public void onAttached(MultiblockControllerBase newController) {
		super.onAttached(newController);
		recalculateOutwardsDirection(newController.getMinimumCoord(), newController.getMaximumCoord());
	}

	@Override
	public void onMachineAssembled(MultiblockControllerBase controller) {
		BlockPos maxCoord = controller.getMaximumCoord();
		BlockPos minCoord = controller.getMinimumCoord();

		// Discover where I am on the reactor
		recalculateOutwardsDirection(minCoord, maxCoord);
	}

	@Override
	public void onMachineBroken() {
		position = PartPosition.Unknown;
		outwards = null;
	}

	// Positional helpers
	public void recalculateOutwardsDirection(BlockPos minCoord, BlockPos maxCoord) {
		outwards = null;
		position = PartPosition.Unknown;

		int facesMatching = 0;
		if (maxCoord.getX() == this.getPos().getX() || minCoord.getX() == this.getPos().getX()) {
			facesMatching++;
		}
		if (maxCoord.getY() == this.getPos().getY() || minCoord.getY() == this.getPos().getY()) {
			facesMatching++;
		}
		if (maxCoord.getZ() == this.getPos().getZ() || minCoord.getZ() == this.getPos().getZ()) {
			facesMatching++;
		}

		if (facesMatching <= 0) {
			position = PartPosition.Interior;
		} else if (facesMatching >= 3) {
			position = PartPosition.FrameCorner;
		} else if (facesMatching == 2) {
			position = PartPosition.Frame;
		} else {
			// 1 face matches
			if (maxCoord.getX() == this.getPos().getX()) {
				position = PartPosition.EastFace;
				outwards = Direction.EAST;
			} else if (minCoord.getX() == this.getPos().getX()) {
				position = PartPosition.WestFace;
				outwards = Direction.WEST;
			} else if (maxCoord.getZ() == this.getPos().getZ()) {
				position = PartPosition.SouthFace;
				outwards = Direction.SOUTH;
			} else if (minCoord.getZ() == this.getPos().getZ()) {
				position = PartPosition.NorthFace;
				outwards = Direction.NORTH;
			} else if (maxCoord.getY() == this.getPos().getY()) {
				position = PartPosition.TopFace;
				outwards = Direction.UP;
			} else {
				position = PartPosition.BottomFace;
				outwards = Direction.DOWN;
			}
		}
	}

	// /// Validation Helpers (IMultiblockPart)
	public abstract void isGoodForFrame() throws MultiblockValidationException;

	public abstract void isGoodForSides() throws MultiblockValidationException;

	public abstract void isGoodForTop() throws MultiblockValidationException;

	public abstract void isGoodForBottom() throws MultiblockValidationException;

	public abstract void isGoodForInterior() throws MultiblockValidationException;
}

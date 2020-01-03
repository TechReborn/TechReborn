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

package techreborn.blockentity.machine.multiblock.casing;

import reborncore.common.multiblock.MultiblockControllerBase;
import reborncore.common.multiblock.rectangular.RectangularMultiblockBlockEntityBase;
import techreborn.init.TRBlockEntities;
import techreborn.multiblocks.MultiBlockCasing;

public class MachineCasingBlockEntity extends RectangularMultiblockBlockEntityBase {

	public MachineCasingBlockEntity() {
		super(TRBlockEntities.MACHINE_CASINGS);
	}

	@Override
	public void onMachineActivated() {

	}

	@Override
	public void onMachineDeactivated() {

	}

	@Override
	public MultiblockControllerBase createNewMultiblock() {
		return new MultiBlockCasing(world);
	}

	@Override
	public Class<? extends MultiblockControllerBase> getMultiblockControllerType() {
		return MultiBlockCasing.class;
	}

	@Override
	public void isGoodForFrame() {

	}

	@Override
	public void isGoodForSides() {

	}

	@Override
	public void isGoodForTop() {

	}

	@Override
	public void isGoodForBottom() {

	}

	@Override
	public void isGoodForInterior() {

	}

	@Override
	public MultiBlockCasing getMultiblockController() {
		return (MultiBlockCasing) super.getMultiblockController();
	}

	@Override
	public void tick() {

	}
}

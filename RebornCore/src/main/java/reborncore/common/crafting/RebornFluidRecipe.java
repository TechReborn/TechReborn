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

package reborncore.common.crafting;

import net.minecraft.block.entity.BlockEntity;
import reborncore.common.fluid.container.FluidInstance;
import reborncore.common.util.Tank;

public interface RebornFluidRecipe extends RebornRecipe {
	FluidInstance fluid();
	Tank getTank(BlockEntity be);

	@Override
	default boolean canCraft(BlockEntity be) {
		final FluidInstance tankFluid = getTank(be).getFluidInstance();
		if (fluid().isEmpty()) {
			return true;
		}
		if (tankFluid.isEmpty()) {
			return false;
		}
		if (tankFluid.fluid().equals(fluid().fluid())) {
			if (tankFluid.getAmount().equalOrMoreThan(fluid().getAmount())) {
				return true;
			}
		}
		return false;
	}

	@Override
	default boolean onCraft(BlockEntity be) {
		final FluidInstance tankFluid = getTank(be).getFluidInstance();
		if (fluid().isEmpty()) {
			return true;
		}
		if (tankFluid.isEmpty()) {
			return false;
		}
		if (tankFluid.fluid().equals(fluid().fluid())) {
			if (tankFluid.getAmount().equalOrMoreThan(fluid().getAmount())) {
				getTank(be).modifyFluid(fluid -> fluid.subtractAmount(fluid().getAmount()));
				return true;
			}
		}
		return false;
	}
}

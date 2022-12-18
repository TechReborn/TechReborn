/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2022 TechReborn
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

package techreborn.datagen.recipes.machine

import net.minecraft.fluid.Fluid
import net.minecraft.fluid.Fluids
import reborncore.common.crafting.RebornFluidRecipe
import reborncore.common.crafting.RebornRecipeType

abstract class MachineRecipeWithFluidJsonFactory<R extends RebornFluidRecipe> extends MachineRecipeJsonFactory<R> {
	protected Fluid fluid = Fluids.WATER // default
	protected long fluidAmount = 1000L // default in millibuckets

	def fluid(Fluid fluid) {
		this.fluid = fluid
		return this
	}

	def fluidAmount(long fluidAmount) {
		this.fluidAmount = fluidAmount
		return this
	}

	protected MachineRecipeWithFluidJsonFactory(RebornRecipeType<R> type) {
		super(type)
	}

	@Override
	protected void validate() {
		super.validate()

		if (fluidAmount < 0) {
			throw new IllegalStateException("recipe has no valid fluid value specified")
		}
		if (fluid == null)
			throw new IllegalStateException("recipe has no valid fluid type specified")
	}
}

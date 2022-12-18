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

package techreborn.datagen.recipes.machine.blast_furnace

import net.minecraft.util.Identifier
import techreborn.api.recipe.recipes.BlastFurnaceRecipe
import techreborn.datagen.recipes.machine.MachineRecipeJsonFactory
import techreborn.init.ModRecipes

class BlastFurnaceRecipeJsonFactory extends MachineRecipeJsonFactory<BlastFurnaceRecipe> {
	private int heat = -1

	def heat(int heat) {
		this.heat = heat
		return this
	}

	protected BlastFurnaceRecipeJsonFactory() {
		super(ModRecipes.BLAST_FURNACE)
	}

	static BlastFurnaceRecipeJsonFactory create() {
		return new BlastFurnaceRecipeJsonFactory()
	}

	static BlastFurnaceRecipeJsonFactory createBlastFurnace(@DelegatesTo(value = BlastFurnaceRecipeJsonFactory.class, strategy = Closure.DELEGATE_FIRST) Closure closure) {
		def factory = new BlastFurnaceRecipeJsonFactory()
		closure.setDelegate(factory)
		closure.call(factory)
		return factory
	}

	@Override
	protected BlastFurnaceRecipe createRecipe(Identifier identifier) {
		return new BlastFurnaceRecipe(type, identifier, ingredients, outputs, power, time, heat)
	}

	@Override
	protected void validate() {
		super.validate()

		if (heat < 0) {
			throw new IllegalStateException("recipe has no heat value")
		}
	}
}

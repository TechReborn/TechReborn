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

package techreborn.datagen.recipes.machine.fluid_replicator

import net.minecraft.registry.Registries
import net.minecraft.util.Identifier
import reborncore.common.fluid.FluidValue
import reborncore.common.fluid.container.FluidInstance
import techreborn.api.recipe.recipes.FluidReplicatorRecipe
import techreborn.datagen.recipes.TechRebornRecipesProvider
import techreborn.datagen.recipes.machine.MachineRecipeWithFluidJsonFactory
import techreborn.init.ModRecipes

class FluidReplicatorRecipeJsonFactory extends MachineRecipeWithFluidJsonFactory<FluidReplicatorRecipe> {
	private FluidReplicatorRecipeJsonFactory(TechRebornRecipesProvider provider) {
		super(ModRecipes.FLUID_REPLICATOR, provider)
	}

	static FluidReplicatorRecipeJsonFactory create(TechRebornRecipesProvider provider) {
		return new FluidReplicatorRecipeJsonFactory(provider)
	}

	static FluidReplicatorRecipeJsonFactory createFluidReplicator(TechRebornRecipesProvider provider, @DelegatesTo(value = FluidReplicatorRecipeJsonFactory.class, strategy = Closure.DELEGATE_FIRST) Closure closure) {
		def factory = new FluidReplicatorRecipeJsonFactory(provider)
		closure.setDelegate(factory)
		closure.call(factory)
		return factory
	}

	@Override
	protected FluidReplicatorRecipe createRecipe() {
		return new FluidReplicatorRecipe(ModRecipes.FLUID_REPLICATOR, ingredients, outputs, power, time, new FluidInstance(fluid, FluidValue.fromMillibuckets(fluidAmount)))
	}

	@Override
	def getIdentifier() {
		def outputId = Registries.FLUID.getId(fluid)
		return Identifier.of("techreborn", "${type.name().path}/${outputId.path}${getSourceAppendix()}")
	}
}

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

package techreborn.api.generator;


import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import reborncore.common.fluid.FluidUtils;

public record FluidGeneratorRecipe(Fluid fluid, int energyPerMb,
								EFluidGenerator generatorType) implements Recipe<Inventory> {

	public int getEnergyPerBucket() {
		return energyPerMb * 1000;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FluidGeneratorRecipe other = (FluidGeneratorRecipe) obj;
		if (energyPerMb != other.energyPerMb)
			return false;
		if (fluid == null) {
			if (other.fluid != null)
				return false;
		} else if (!FluidUtils.fluidEquals(other.fluid, fluid))
			return false;
		return generatorType == other.generatorType;
	}

	@Override
	public boolean matches(Inventory inventory, World world) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ItemStack craft(Inventory inventory, DynamicRegistryManager registryManager) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean fits(int width, int height) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ItemStack getOutput(DynamicRegistryManager registryManager) {
		return ItemStack.EMPTY;
	}

	@Override
	public Identifier getId() {
		return GeneratorRecipeHelper.recipeIds.computeIfAbsent(
			this,
			recipe -> {
				String path = Registries.FLUID.getId(recipe.fluid).getPath();
				return recipe.generatorType.getId().withSuffixedPath("/" + path);
			}
		);
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return generatorType.getSerializer();
	}

	@Override
	public RecipeType<?> getType() {
		return generatorType.getType();
	}
}

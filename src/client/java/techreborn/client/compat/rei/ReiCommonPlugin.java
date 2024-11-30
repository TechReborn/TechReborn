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

package techreborn.client.compat.rei;

import dev.architectury.event.CompoundEventResult;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.comparison.ItemComparatorRegistry;
import me.shedaniel.rei.api.common.fluid.FluidSupportProvider;
import me.shedaniel.rei.api.common.plugins.REICommonPlugin;
import me.shedaniel.rei.api.common.registry.display.ServerDisplayRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.crafting.RecipeManager;
import reborncore.common.fluid.container.ItemFluidInfo;
import techreborn.TechReborn;
import techreborn.client.compat.rei.fluidgenerator.FluidGeneratorRecipeDisplay;
import techreborn.client.compat.rei.fluidreplicator.FluidReplicatorRecipeDisplay;
import techreborn.client.compat.rei.rollingmachine.RollingMachineDisplay;
import techreborn.init.ModRecipes;
import techreborn.init.TRContent;
import techreborn.recipe.recipes.FluidGeneratorRecipe;
import techreborn.recipe.recipes.FluidReplicatorRecipe;
import techreborn.recipe.recipes.RollingMachineRecipe;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class ReiCommonPlugin implements REICommonPlugin {
	@Override
	public void registerFluidSupport(FluidSupportProvider support) {
		support.register(stack -> {
			ItemStack itemStack = stack.getValue();
			if (itemStack.getItem() instanceof ItemFluidInfo) {
				Fluid fluid = ((ItemFluidInfo) itemStack.getItem()).getFluid(itemStack);
				if (fluid != null)
					return CompoundEventResult.interruptTrue(Stream.of(EntryStacks.of(fluid)));
			}
			return CompoundEventResult.pass();
		});
	}

	@Override
	public void registerItemComparators(ItemComparatorRegistry registry) {
		registry.registerComponents(TRContent.CELL);
	}

	@Override
	public void registerDisplays(ServerDisplayRegistry registry) {
		final Map<RecipeType<FluidGeneratorRecipe>, TRContent.Machine> fluidGenRecipes = Map.of(
			ModRecipes.THERMAL_GENERATOR, TRContent.Machine.THERMAL_GENERATOR,
			ModRecipes.GAS_GENERATOR, TRContent.Machine.GAS_TURBINE,
			ModRecipes.DIESEL_GENERATOR, TRContent.Machine.DIESEL_GENERATOR,
			ModRecipes.SEMI_FLUID_GENERATOR, TRContent.Machine.SEMI_FLUID_GENERATOR,
			ModRecipes.PLASMA_GENERATOR, TRContent.Machine.PLASMA_GENERATOR
		);

		RecipeManager.getRecipeTypes("techreborn")
			.stream()
			.filter(recipeType -> !fluidGenRecipes.containsKey(recipeType))
			.forEach(rebornRecipeType -> registerMachineRecipe(registry, rebornRecipeType));

		fluidGenRecipes.forEach((recipeType, machine) -> registerFluidGeneratorDisplays(registry, recipeType, machine));
	}

	private void registerFluidGeneratorDisplays(ServerDisplayRegistry registry, RecipeType<FluidGeneratorRecipe> generator, TRContent.Machine machine) {
		Identifier identifier = Identifier.of(TechReborn.MOD_ID, machine.name);
		registry.beginRecipeFiller(FluidGeneratorRecipe.class)
			.filterType(generator)
			.fill(recipe -> new FluidGeneratorRecipeDisplay(recipe.value(), identifier));
	}

	private void registerMachineRecipe(ServerDisplayRegistry registry, RecipeType<?> recipeType) {
		if (recipeType == ModRecipes.RECYCLER) {
			return;
		}

		Function<RecipeEntry<RebornRecipe>, Display> recipeDisplay = MachineRecipeDisplay::new;

		if (recipeType == ModRecipes.ROLLING_MACHINE) {
			recipeDisplay = r -> {
				RollingMachineRecipe rollingMachineRecipe = (RollingMachineRecipe) r.value();
				return new RollingMachineDisplay(new RecipeEntry<>(r.id(), rollingMachineRecipe.getShapedRecipe()));
			};
		}

		if (recipeType == ModRecipes.FLUID_REPLICATOR) {
			recipeDisplay = r -> {
				FluidReplicatorRecipe recipe = (FluidReplicatorRecipe) r.value();
				return new FluidReplicatorRecipeDisplay(new RecipeEntry<>(r.id(), recipe));
			};
		}

		registry.beginRecipeFiller(RebornRecipe.class)
			.filter(recipeEntry -> recipeEntry.value().getType() == recipeType)
			.fill(recipeDisplay);
	}
}

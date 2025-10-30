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

package techreborn.compat.rei;

import dev.architectury.event.CompoundEventResult;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.entry.comparison.ComparisonContext;
import me.shedaniel.rei.api.common.entry.comparison.ItemComparatorRegistry;
import me.shedaniel.rei.api.common.entry.type.EntryTypeRegistry;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import me.shedaniel.rei.api.common.fluid.FluidSupportProvider;
import me.shedaniel.rei.api.common.plugins.REICommonPlugin;
import me.shedaniel.rei.api.common.registry.display.ServerDisplayRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.plugin.client.entry.ItemEntryDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.crafting.RecipeManager;
import reborncore.common.fluid.container.ItemFluidInfo;
import techreborn.TechReborn;
import techreborn.init.ModRecipes;
import techreborn.init.TRContent;
import techreborn.recipe.recipes.FluidGeneratorRecipe;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class ReiCommonPlugin implements REICommonPlugin {
	@Override
	public double getPriority() {
		return 1;
	}

	@Override
	public void registerEntryTypes(EntryTypeRegistry registry) {
		registry.register(VanillaEntryTypes.ITEM, new ItemEntryDefinition() {
			@Override
			public boolean equals(ItemStack left, ItemStack right, ComparisonContext context) {
				if (context != ComparisonContext.FUZZY
					|| !(left.getItem() instanceof ItemFluidInfo leftInfo)
					|| !(right.getItem() instanceof ItemFluidInfo rightInfo)) {
					return super.equals(left, right, context);
				}
				Fluid leftFluid = leftInfo.getFluid(left);
				if (leftFluid == rightInfo.getFluid(right)) {
					return leftFluid != Fluids.EMPTY || leftInfo == rightInfo;
				} else {
					return false;
				}
			}
		});
	}

	@Override
	public void registerFluidSupport(FluidSupportProvider support) {
		support.register(stack -> {
			ItemStack itemStack = stack.getValue();
			if (itemStack.getItem() instanceof ItemFluidInfo fluidInfo) {
				Fluid fluid = fluidInfo.getFluid(itemStack);
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
		ResourceLocation identifier = ResourceLocation.fromNamespaceAndPath(TechReborn.MOD_ID, machine.name);
		registry.beginRecipeFiller(FluidGeneratorRecipe.class)
			.filterType(generator)
			.fill(recipe -> new FluidGeneratorRecipeDisplay(recipe.value(), identifier));
	}

	private void registerMachineRecipe(ServerDisplayRegistry registry, RecipeType<?> recipeType) {
		if (recipeType == ModRecipes.RECYCLER) {
			return;
		}

		Function<RecipeHolder<RebornRecipe>, Display> recipeDisplay = MachineRecipeDisplay::new;

		if (recipeType == ModRecipes.ROLLING_MACHINE) {
			recipeDisplay = RollingMachineDisplay::new;
		}

		if (recipeType == ModRecipes.FLUID_REPLICATOR) {
			recipeDisplay = FluidReplicatorRecipeDisplay::new;
		}

		registry.beginRecipeFiller(RebornRecipe.class)
			.filter(recipeEntry -> recipeEntry.value().getType() == recipeType)
			.fill(recipeDisplay);
	}

	@Override
	public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
		registry.register(ResourceLocation.fromNamespaceAndPath(TechReborn.MOD_ID, "machine"), MachineRecipeDisplay.SERIALIZER);
		registry.register(ResourceLocation.fromNamespaceAndPath(TechReborn.MOD_ID, "rolling_machine"), RollingMachineDisplay.SERIALIZER);
		registry.register(ResourceLocation.fromNamespaceAndPath(TechReborn.MOD_ID, "fluid_generator"), FluidGeneratorRecipeDisplay.SERIALIZER);
		registry.register(ResourceLocation.fromNamespaceAndPath(TechReborn.MOD_ID, "fluid_replicator"), FluidReplicatorRecipeDisplay.SERIALIZER);
	}
}

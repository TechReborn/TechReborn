/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
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

package techreborn.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import reborncore.common.util.ItemUtils;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import techreborn.api.reactor.FusionReactorRecipe;
import techreborn.api.reactor.FusionReactorRecipeHelper;

import java.util.ArrayList;
import java.util.List;

@ZenClass("mods.techreborn.fusionReactor")
public class CTFusionReactor {

	@ZenMethod
	@ZenDocumentation("IIngredient topInput, IIngredient bottomInput, IItemStack output, int startEU, int euTick, int tickTime")
	public static void addRecipe(IIngredient topInput, IIngredient bottomInput, IItemStack output, int startEU, int euTick, int tickTime) {
		FusionReactorRecipe reactorRecipe = new FusionReactorRecipe((ItemStack) CraftTweakerCompat.toObject(topInput), (ItemStack) CraftTweakerCompat.toObject(bottomInput), CraftTweakerCompat.toStack(output), startEU, euTick, tickTime);
		CraftTweakerAPI.apply(new Add(reactorRecipe));
	}

	@ZenMethod
	@ZenDocumentation("IIngredient iIngredient")
	public static void removeTopInputRecipe(IIngredient iIngredient) {
		CraftTweakerAPI.apply(new RemoveTopInput(iIngredient));
	}

	@ZenMethod
	@ZenDocumentation("IIngredient iIngredient")
	public static void removeBottomInputRecipe(IIngredient iIngredient) {
		CraftTweakerAPI.apply(new RemoveBottomInput(iIngredient));
	}

	@ZenMethod
	@ZenDocumentation("IItemStack output")
	public static void removeRecipe(IItemStack output) {
		CraftTweakerAPI.apply(new Remove(CraftTweakerCompat.toStack(output)));
	}
	
	@ZenMethod
	public static void removeAll() {
		CraftTweakerAPI.apply(new RemoveAll());
	}
	
	private static class Add implements IAction {
		private final FusionReactorRecipe recipe;

		public Add(FusionReactorRecipe recipe) {
			this.recipe = recipe;
		}

		@Override
		public void apply() {
			FusionReactorRecipeHelper.registerRecipe(recipe);
		}

		@Override
		public String describe() {
			return "Adding Fusion Reactor recipe for " + recipe.getOutput().getDisplayName();
		}
	}

	private static class Remove implements IAction {
		private final ItemStack output;
		List<FusionReactorRecipe> removedRecipes = new ArrayList<FusionReactorRecipe>();

		public Remove(ItemStack output) {
			this.output = output;
		}

		@Override
		public void apply() {
			for (FusionReactorRecipe recipeType : FusionReactorRecipeHelper.reactorRecipes) {
				boolean matchNBT = output.hasTagCompound();
				if (ItemUtils.isItemEqual(recipeType.getOutput(), output, true, matchNBT)) {
					removedRecipes.add(recipeType);
					FusionReactorRecipeHelper.reactorRecipes.remove(recipeType);
					break;
				}
			}
		}

		@Override
		public String describe() {
			return "Removing Fusion Reactor recipe for " + output.getDisplayName();
		}

	}

	private static class RemoveTopInput implements IAction {
		private final IIngredient output;
		List<FusionReactorRecipe> removedRecipes = new ArrayList<FusionReactorRecipe>();

		public RemoveTopInput(IIngredient output) {
			this.output = output;
		}

		@Override
		public void apply() {
			for (FusionReactorRecipe recipeType : FusionReactorRecipeHelper.reactorRecipes) {
				if (output.matches(CraftTweakerMC.getIItemStack(recipeType.getTopInput()))) {
					removedRecipes.add(recipeType);
					FusionReactorRecipeHelper.reactorRecipes.remove(recipeType);
					break;
				}
			}
		}

		@Override
		public String describe() {
			return "Removing Fusion Reactor recipe";
		}
	}

	private static class RemoveBottomInput implements IAction {
		private final IIngredient output;
		List<FusionReactorRecipe> removedRecipes = new ArrayList<FusionReactorRecipe>();

		public RemoveBottomInput(IIngredient output) {
			this.output = output;
		}

		@Override
		public void apply() {
			for (FusionReactorRecipe recipeType : FusionReactorRecipeHelper.reactorRecipes) {
				if (output.matches(CraftTweakerMC.getIItemStack(recipeType.getBottomInput()))) {
					removedRecipes.add(recipeType);
					FusionReactorRecipeHelper.reactorRecipes.remove(recipeType);
					break;
				}
			}
		}

		@Override
		public String describe() {
			return "Removing Fusion Reactor recipe";
		}
	}
	
	private static class RemoveAll implements IAction {

		@Override
		public void apply() {
				FusionReactorRecipeHelper.reactorRecipes.clear();
		}

		@Override
		public String describe() {
			return "Removing all Fusion Reactor recipes";
		}
	}
}

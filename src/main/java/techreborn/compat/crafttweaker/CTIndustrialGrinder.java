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
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import techreborn.api.Reference;
import techreborn.api.recipe.machines.IndustrialGrinderRecipe;

/**
 *  Craftweaker class to change Industrial Grinder recipes.
 */
@ZenClass("mods.techreborn.industrialGrinder")
public class CTIndustrialGrinder extends CTGeneric {

	/**
	 *  Add recipe for Industrial Grinder
	 *  @param output1 ItemStack First recipe output
	 *  @param output2 ItemStack Second recipe output
	 *  @param output3 ItemStack Third recipe output
	 *  @param output4 ItemStack Fourth recipe output
	 *  @param input1 First recipe input
	 *  @param input2 Second recipe input. Not used )
	 *  @param ticktime Amount of ticks to complete crafting
	 *  @param euTick Amount of EU per tick consumed during crafting
	 *  @return RecipeSettings RecipeSettings for this recipe
	 */
	@ZenMethod
	@ZenDocumentation("IItemStack output1, IItemStack output2, IItemStack output3, IItemStack output4, IIngredient input1, IIngredient input2, int ticktime, int euTick")
	public static RecipeSettings addRecipe(IItemStack output1, IItemStack output2, IItemStack output3, IItemStack output4, IIngredient input1, IIngredient input2, int ticktime, int euTick) {
		return addRecipe(output1, output2, output3, output4, input1, input2, null, ticktime, euTick);
	}

	/**
	 *  Add recipe for Industrial Grinder
	 *  @param output1 ItemStack First recipe output
	 *  @param output2 ItemStack Second recipe output
	 *  @param output3 ItemStack Third recipe output
	 *  @param output4 ItemStack Fourth recipe output
	 *  @param input1 First recipe input
	 *  @param input2 Second recipe input. Not used )
	 *  @param fluid LiquidStack Liquid used for grinding
	 *  @param ticktime Amount of ticks to complete crafting
	 *  @param euTick Amount of EU per tick consumed during crafting
	 *  @return RecipeSettings RecipeSettings for this recipe
	 */
	@ZenMethod
	@ZenDocumentation("IItemStack output1, IItemStack output2, IItemStack output3, IItemStack output4, IIngredient input1, IIngredient input2, ILiquidStack fluid, int ticktime, int euTick")
	public static RecipeSettings addRecipe(IItemStack output1, IItemStack output2, IItemStack output3, IItemStack output4, IIngredient input1, IIngredient input2, ILiquidStack fluid, int ticktime, int euTick) {
		Object oInput1 = CraftTweakerCompat.toObject(input1);
		
		// There is only one input slot in Industrial Grinder
		//ItemStack oInput2 = (ItemStack) CraftTweakerCompat.toObject(input2);

		FluidStack fluidStack = null;
		if (fluid != null) {
			fluidStack = CraftTweakerCompat.toFluidStack(fluid);
		}

		IndustrialGrinderRecipe r = new IndustrialGrinderRecipe(oInput1, fluidStack, CraftTweakerCompat.toStack(output1), CraftTweakerCompat.toStack(output2), CraftTweakerCompat.toStack(output3), CraftTweakerCompat.toStack(output4), ticktime, euTick);
		addRecipe(r);
		return new RecipeSettings(r);
	}

	/**
	 *  Remove recipe for Industrial Grinder based on input ingredient
	 *  @param iIngredient Recipe input for which we should remove recipe
	 */
	@ZenMethod
	@ZenDocumentation("IIngredient iIngredient")
	public static void removeInputRecipe(IIngredient iIngredient) { CraftTweakerAPI.apply(new RemoveInput(iIngredient, getMachineName())); }

	/**
	 *  Remove recipe for Industrial Grinder based on output
	 *  @param output Recipe output for which we should remove recipe
	 */
	@ZenMethod
	@ZenDocumentation("IItemStack output")
	public static void removeRecipe(IItemStack output) {
		CraftTweakerAPI.apply(new Remove(CraftTweakerCompat.toStack(output), getMachineName()));
	}
	
	@ZenMethod
	public static void removeAll(){
		CraftTweakerAPI.apply(new RemoveAll(getMachineName()));
	}

	/**
	 *  Get reference machine name
	 *  @return String Reference name for Industrial Grinder
	 */
	public static String getMachineName() {
		return Reference.INDUSTRIAL_GRINDER_RECIPE;
	}
}

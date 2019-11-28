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

package techreborn.compat.jei;

import net.minecraft.item.ItemStack;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import reborncore.api.praescriptum.ingredients.input.FluidStackInputIngredient;
import reborncore.api.praescriptum.ingredients.input.InputIngredient;
import reborncore.api.praescriptum.ingredients.input.ItemStackInputIngredient;
import reborncore.api.praescriptum.ingredients.input.OreDictionaryInputIngredient;
import reborncore.api.praescriptum.ingredients.output.FluidStackOutputIngredient;
import reborncore.api.praescriptum.ingredients.output.ItemStackOutputIngredient;
import reborncore.api.praescriptum.recipes.Recipe;
import reborncore.common.util.ItemUtils;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author estebes
 */
public abstract class RecipeWrapper implements IRecipeWrapper {
    public RecipeWrapper(Recipe recipe) {
        this.recipe = recipe;

        // inputs
        for (InputIngredient<?> inputIngredient : recipe.getInputIngredients()) {
            if (inputIngredient instanceof ItemStackInputIngredient) { // map ItemStacks
                itemInputs.add(Collections.singletonList((ItemStack) inputIngredient.ingredient));
            } else if (inputIngredient instanceof OreDictionaryInputIngredient) { // map OreDictionary entries
                List<ItemStack> temp = new ArrayList<>();
                for (ItemStack stack : OreDictionary.getOres((String) inputIngredient.ingredient))
                    temp.add(copyWithSize(stack, inputIngredient.getCount()));
                itemInputs.add(temp);
            } else if (inputIngredient instanceof FluidStackInputIngredient) { // map FluidStacks
                fluidInputs.add(Collections.singletonList((FluidStack) inputIngredient.ingredient));
            }
        }

        // outputs
        itemOutputs.addAll(Arrays.asList(recipe.getItemOutputs()));

        fluidOutputs.addAll(Arrays.asList(recipe.getFluidOutputs()));
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputLists(VanillaTypes.ITEM, itemInputs);
        ingredients.setInputLists(VanillaTypes.FLUID, fluidInputs);
        ingredients.setOutputs(VanillaTypes.ITEM, itemOutputs);
        ingredients.setOutputs(VanillaTypes.FLUID, fluidOutputs);
    }

    public static ItemStack copyWithSize(ItemStack stack, int size) {
        if (ItemUtils.isEmpty(stack)) return ItemStack.EMPTY;

        return ItemUtils.setSize(stack.copy(), size);
    }

    // Fields >>
    protected final Recipe recipe;

    protected final List<List<ItemStack>> itemInputs = new ArrayList<>();
    protected final List<List<FluidStack>> fluidInputs = new ArrayList<>();
    protected final List<ItemStack> itemOutputs = new ArrayList<>();
    protected final List<FluidStack> fluidOutputs = new ArrayList<>();
    // << Fields
}

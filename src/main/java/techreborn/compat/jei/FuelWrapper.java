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

import reborncore.api.praescriptum.fuels.Fuel;
import reborncore.api.praescriptum.ingredients.input.FluidStackInputIngredient;
import reborncore.api.praescriptum.ingredients.input.ItemStackInputIngredient;
import reborncore.api.praescriptum.ingredients.input.OreDictionaryInputIngredient;
import reborncore.common.util.ItemUtils;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;

/**
 * @author estebes
 */
public abstract class FuelWrapper implements IRecipeWrapper {
    public FuelWrapper(Fuel fuel) {
        this.fuel = fuel;

        // inputs
        fuel.getInputIngredients().stream()
                .filter(entry -> entry instanceof ItemStackInputIngredient)
                .map(entry -> ImmutableList.of((ItemStack) entry.ingredient))
                .collect(Collectors.toCollection(() -> itemInputs)); // map ItemStacks

        fuel.getInputIngredients().stream()
                .filter(entry -> entry instanceof OreDictionaryInputIngredient)
                .map(entry -> OreDictionary.getOres((String) entry.ingredient).stream()
                        .map(stack -> copyWithSize(stack, entry.getCount())).collect(Collectors.toList()))
                .collect(Collectors.toCollection(() -> itemInputs)); // map OreDictionary entries

        fuel.getInputIngredients().stream()
                .filter(entry -> entry instanceof FluidStackInputIngredient)
                .map(entry -> ImmutableList.of(copyWithSize((FluidStack) entry.ingredient, entry.getCount())))
                .collect(Collectors.toCollection(() -> fluidInputs)); // map FluidStacks
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputLists(VanillaTypes.ITEM, itemInputs);
        ingredients.setInputLists(VanillaTypes.FLUID, fluidInputs);
    }

    public static ItemStack copyWithSize(ItemStack itemStack, int size) {
        if (ItemUtils.isEmpty(itemStack)) return ItemStack.EMPTY;

        return ItemUtils.setSize(itemStack.copy(), size);
    }

    public static FluidStack copyWithSize(FluidStack fluidStack, int size) {
        return new FluidStack(fluidStack.getFluid(), size);
    }

    // Fields >>
    protected final Fuel fuel;

    protected final List<List<ItemStack>> itemInputs = new ArrayList<>();
    protected final List<List<FluidStack>> fluidInputs = new ArrayList<>();
    // << Fields
}

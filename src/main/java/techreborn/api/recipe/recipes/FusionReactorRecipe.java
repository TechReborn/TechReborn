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

package techreborn.api.recipe.recipes;

import com.google.gson.JsonObject;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.crafting.RebornRecipeType;
import reborncore.common.crafting.ingredient.RebornIngredient;
import techreborn.blockentity.machine.multiblock.FusionControlComputerBlockEntity;

/**
 * @author drcrazy
 */
public class FusionReactorRecipe extends RebornRecipe {

	private int startE;
	private int minSize;

	public FusionReactorRecipe(RebornRecipeType<?> type, Identifier name) {
		super(type, name);
	}

	public FusionReactorRecipe(RebornRecipeType<?> type, Identifier name, DefaultedList<RebornIngredient> ingredients, DefaultedList<ItemStack> outputs, int power, int time, int startE, int minSize) {
		super(type, name, ingredients, outputs, power, time);
		this.startE = startE;
		this.minSize = minSize;
	}

	public int getStartEnergy() {
		return startE;
	}

	@Override
	public void deserialize(JsonObject jsonObject) {
		super.deserialize(jsonObject);
		startE = JsonHelper.getInt(jsonObject, "start-power");
		minSize = JsonHelper.getInt(jsonObject, "min-size");
	}

	@Override
	public void serialize(JsonObject jsonObject) {
		super.serialize(jsonObject);
		jsonObject.addProperty("start-power", startE);
		jsonObject.addProperty("min-size", minSize);
	}

	@Override
	public boolean canCraft(final BlockEntity blockEntity) {
		if (blockEntity instanceof FusionControlComputerBlockEntity) {
			final FusionControlComputerBlockEntity reactor = (FusionControlComputerBlockEntity) blockEntity;
			return reactor.getSize() >= minSize;
		}
		return false;
	}

}

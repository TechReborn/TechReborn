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
import com.google.gson.JsonSyntaxException;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.crafting.RebornRecipeType;
import reborncore.common.recipes.RecipeCrafter;
import techreborn.blockentity.machine.multiblock.IndustrialBlastFurnaceBlockEntity;

import java.util.List;

public class BlastFurnaceRecipe extends RebornRecipe {

	private int heat;
	private boolean partialReturns;
	private List<ItemStack> partialOutputs;

	public BlastFurnaceRecipe(RebornRecipeType<?> type, Identifier name) {
		super(type, name);
	}

	public int getHeat() {
		return heat;
	}

	@Override
	public void deserialize(JsonObject jsonObject) {
		super.deserialize(jsonObject);
		heat = JsonHelper.getInt(jsonObject, "heat");
		try {
			partialReturns = JsonHelper.getBoolean(jsonObject, "partialReturns");
		}
		catch (JsonSyntaxException e) {
			partialReturns = false;
		}
	}

	@Override
	public void serialize(JsonObject jsonObject) {
		super.serialize(jsonObject);
		jsonObject.addProperty("heat", heat);
		jsonObject.addProperty("partialReturns", partialReturns);
	}

	@Override
	public boolean canCraft(final BlockEntity blockEntity) {
		if (blockEntity instanceof final IndustrialBlastFurnaceBlockEntity blastFurnace) {
			return blastFurnace.getHeat() >= heat;
		}
		return false;
	}

	@Override
	public boolean onCraft(BlockEntity be) {
		if (!partialReturns) {
			return super.onCraft(be);
		}
		if (be instanceof IndustrialBlastFurnaceBlockEntity blastFurnace){
			RecipeCrafter crafter = blastFurnace.getRecipeCrafter();
			// Thanks to https://github.com/LauchInterceptor for this part
			for (int inputSlot : crafter.inputSlots) {
				ItemStack inputStack = crafter.inventory.getStack(inputSlot);
				if (inputStack.isDamageable()) {
					float durabilityPercentage = 1 - ((float)inputStack.getDamage()) / inputStack.getMaxDamage();
					partialOutputs = this.getOutputs().stream().map(itemStack -> {
						ItemStack copied = itemStack.copy();
						if (copied.getCount() > 1) {
							copied.setCount((int) Math.floor(copied.getCount() * durabilityPercentage));
						}
						return copied;
					}).toList();
				}
			}
		}
		return true;
	}

	@Override
	public List<ItemStack> getOutputs() {
		if (!partialReturns || partialOutputs == null) {
			return super.getOutputs();
		}
		return partialOutputs;

	}
}

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

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.crafting.RebornRecipeType;
import reborncore.common.crafting.ingredient.RebornIngredient;
import techreborn.blockentity.machine.multiblock.FusionControlComputerBlockEntity;
import techreborn.init.TRContent;

import java.util.List;

/**
 * @author drcrazy
 */
public class FusionReactorRecipe extends RebornRecipe {
	private final int startE;
	private final int minSize;

	public FusionReactorRecipe(RebornRecipeType<?> type, Identifier name, List<RebornIngredient> ingredients, List<ItemStack> outputs, int power, int time, int startE, int minSize) {
		super(type, name, ingredients, outputs, power, time);
		this.startE = startE;
		this.minSize = minSize;
	}

	@Override
	public ItemStack createIcon() {
		return new ItemStack(TRContent.Machine.FUSION_CONTROL_COMPUTER);
	}

	public int getStartEnergy() {
		return startE;
	}

	public int getMinSize() {
		return minSize;
	}

	@Override
	public boolean canCraft(final BlockEntity blockEntity) {
		if (blockEntity instanceof final FusionControlComputerBlockEntity reactor) {
			return reactor.getSize() >= minSize;
		}
		return false;
	}

}

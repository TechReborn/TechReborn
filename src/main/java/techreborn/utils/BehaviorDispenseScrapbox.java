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

package techreborn.utils;

import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import reborncore.common.crafting.RebornRecipe;
import techreborn.config.TechRebornConfig;
import techreborn.init.ModRecipes;

import java.util.List;
import java.util.Random;

public class BehaviorDispenseScrapbox extends ItemDispenserBehavior {

	@Override
	protected ItemStack dispenseSilently(BlockPointer source, ItemStack stack) {
		if (TechRebornConfig.dispenseScrapboxes) {
			List<RebornRecipe> scrapboxRecipeList = ModRecipes.SCRAPBOX.getRecipes(source.getWorld());
			int random = new Random().nextInt(scrapboxRecipeList.size());
			ItemStack out = scrapboxRecipeList.get(random).getOutputs().get(0);
			stack.split(1);

			DispenserBlockEntity blockEntity = source.getBlockEntity();
			Direction enumfacing = blockEntity.getWorld().getBlockState(new BlockPos(source.getX(), source.getY(), source.getZ())).get(DispenserBlock.FACING);
			Position iposition = DispenserBlock.getOutputLocation(source);
			spawnItem(source.getWorld(), out, 6, enumfacing, iposition);
		}
		return stack;
	}

}

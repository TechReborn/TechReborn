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

package techreborn.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.util.WorldUtils;
import techreborn.TechReborn;
import techreborn.init.ModRecipes;

import java.util.List;

public class ScrapBoxItem extends Item {

	public ScrapBoxItem() {
		super(new Item.Settings().group(TechReborn.ITEMGROUP));
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getMainHandStack();
		if (!world.isClient) {
			List<RebornRecipe> scrapboxRecipeList = ModRecipes.SCRAPBOX.getRecipes(world);
			int random = world.random.nextInt(scrapboxRecipeList.size());
			ItemStack out = scrapboxRecipeList.get(random).getOutputs().get(0);
			WorldUtils.dropItem(out, world, player.getBlockPos());
			stack.decrement(1);
		}
		return new TypedActionResult<>(ActionResult.SUCCESS, stack);
	}
}

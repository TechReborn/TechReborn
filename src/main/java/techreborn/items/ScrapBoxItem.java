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

import reborncore.common.crafting.RecipeUtils;
import reborncore.common.util.WorldUtils;
import techreborn.init.ModRecipes;
import techreborn.init.TRContent;
import techreborn.init.TRItemSettings;
import techreborn.recipe.recipes.ScrapBoxRecipe;

import java.util.List;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ScrapBoxItem extends Item {

	public ScrapBoxItem(String name) {
		super(TRItemSettings.item(name));
	}

	@Override
	public InteractionResult use(Level world, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (stack.is(TRContent.SCRAP_BOX)) {
			if (world.isClientSide()) {
				return InteractionResult.SUCCESS;
			}
			List<ScrapBoxRecipe> scrapboxRecipeList = RecipeUtils.getRecipes(world, ModRecipes.SCRAPBOX);
			int random = world.getRandom().nextInt(scrapboxRecipeList.size());
			ItemStack out = scrapboxRecipeList.get(random).outputs().get(0);
			WorldUtils.dropItem(out, world, player.blockPosition());
			ItemStack copy = stack.copy();
			copy.shrink(1);
			return InteractionResult.SUCCESS.heldItemTransformedTo(copy);
		}
		return InteractionResult.PASS;
	}
}

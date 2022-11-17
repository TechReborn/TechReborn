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
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import reborncore.common.util.WorldUtils;
import techreborn.TechReborn;

import java.util.List;

public class ScrapBoxItem extends Item {

	public ScrapBoxItem() {
		super(new Item.Settings().group(TechReborn.ITEMGROUP));
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getMainHandStack();
		if (!world.isClient) {
			LootContext.Builder builder = (new LootContext.Builder((ServerWorld)world)).parameter(LootContextParameters.ORIGIN, player.getPos()).parameter(LootContextParameters.THIS_ENTITY, player).random(world.random).luck(player.getLuck());
			LootTable lootTable = world.getServer().getLootManager().getTable(new Identifier(TechReborn.MOD_ID, "chests/scrapbox"));
			List<ItemStack> list = lootTable.generateLoot(builder.build(LootContextTypes.CHEST));
			if (list == null || list.isEmpty()) {
				return new TypedActionResult<>(ActionResult.FAIL, stack);
			}
			ItemStack out = list.get(0);
			WorldUtils.dropItem(out, world, player.getBlockPos());
			stack.decrement(1);
		}
		return new TypedActionResult<>(ActionResult.SUCCESS, stack);
	}
}

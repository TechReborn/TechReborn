/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 TechReborn
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

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import techreborn.api.ScrapboxList;
import techreborn.client.TechRebornCreativeTabMisc;

public class ItemScrapBox extends ItemTR {

	public ItemScrapBox() {
		setUnlocalizedName("techreborn.scrapbox");
		setCreativeTab(TechRebornCreativeTabMisc.instance);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player,
	                                                EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if (!world.isRemote) {
			int random = world.rand.nextInt(ScrapboxList.stacks.size());
			ItemStack out = ScrapboxList.stacks.get(random).copy();
			float xOffset = world.rand.nextFloat() * 0.8F + 0.1F;
			float yOffset = world.rand.nextFloat() * 0.8F + 0.1F;
			float zOffset = world.rand.nextFloat() * 0.8F + 0.1F;
			EntityItem entityitem = new EntityItem(world, player.getPosition().getX() + xOffset,
				player.getPosition().getY() + yOffset, player.getPosition().getZ() + zOffset, out);

			entityitem.setPickupDelay(20);
			world.spawnEntity(entityitem);
			stack.shrink(1);
		}
		return new ActionResult<>(EnumActionResult.SUCCESS, stack);
	}
}

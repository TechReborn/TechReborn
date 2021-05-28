/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2021 TeamReborn
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

package reborncore.common.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;

import java.util.Locale;

public class TorchHelper {

	public static ActionResult placeTorch(ItemUsageContext itemUsageContext) {
		PlayerEntity player = itemUsageContext.getPlayer();
		if (player == null) {
			return ActionResult.FAIL;
		}

		for (int i = 0; i < player.inventory.main.size(); i++) {
			ItemStack torchStack = player.inventory.getStack(i);
			if (torchStack.isEmpty() || !torchStack.getTranslationKey().toLowerCase(Locale.ROOT).contains("torch")) {
				continue;
			}
			if (!(torchStack.getItem() instanceof BlockItem)) {
				continue;
			}

			int oldSize = torchStack.getCount();
			ItemUsageContext context = new ItemUsageContextCustomStack(itemUsageContext.getWorld(), player, itemUsageContext.getHand(), torchStack, new BlockHitResult(itemUsageContext.getHitPos(), itemUsageContext.getSide(), itemUsageContext.getBlockPos(), true));
			ActionResult result = torchStack.useOnBlock(context);
			if (player.isCreative()) {
				torchStack.setCount(oldSize);
			} else if (torchStack.getCount() <= 0) {
				player.inventory.setStack(i, ItemStack.EMPTY);
			}
			if (result == ActionResult.SUCCESS) {
				return ActionResult.SUCCESS;
			}
		}
		return ActionResult.FAIL;
	}
}

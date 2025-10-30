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

import java.util.Locale;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.BlockHitResult;

public class TorchHelper {

	public static InteractionResult placeTorch(UseOnContext itemUsageContext) {
		Player player = itemUsageContext.getPlayer();
		if (player == null) {
			return InteractionResult.FAIL;
		}

		for (int i = 0; i < Inventory.INVENTORY_SIZE; i++) {
			ItemStack torchStack = player.getInventory().getItem(i);
			if (torchStack.isEmpty() || !torchStack.getItem().getDescriptionId().toLowerCase(Locale.ROOT).contains("torch")) {
				continue;
			}
			if (!(torchStack.getItem() instanceof BlockItem)) {
				continue;
			}

			int oldSize = torchStack.getCount();
			UseOnContext context = new ItemUsageContextCustomStack(itemUsageContext.getLevel(), player, itemUsageContext.getHand(), torchStack, new BlockHitResult(itemUsageContext.getClickLocation(), itemUsageContext.getClickedFace(), itemUsageContext.getClickedPos(), true));
			InteractionResult result = torchStack.useOn(context);
			if (player.isCreative()) {
				torchStack.setCount(oldSize);
			} else if (torchStack.getCount() <= 0) {
				player.getInventory().setItem(i, ItemStack.EMPTY);
			}
			if (result == InteractionResult.SUCCESS) {
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.FAIL;
	}
}

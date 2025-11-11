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

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import reborncore.api.blockentity.IUpgradeable;

public class ItemHandlerUtils {

	public static void dropContainedItems(Level world, BlockPos pos) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity == null) {
			return;
		}
		if (blockEntity instanceof Container inventory) {
			dropItemHandler(world, pos, inventory);
		}
		if (blockEntity instanceof IUpgradeable) {
			dropItemHandler(world, pos, ((IUpgradeable) blockEntity).getUpgradeInventory());
		}
	}

	public static void dropItemHandler(Level world, BlockPos pos, Container inventory) {
		for (int i = 0; i < inventory.getContainerSize(); i++) {
			ItemStack itemStack = inventory.getItem(i);
			if (itemStack.isEmpty()) {
				continue;
			}
			if (itemStack.getCount() > 0) {
				if (itemStack.getItem() instanceof BlockItem) {
					if (((BlockItem) itemStack.getItem()).getBlock() instanceof LiquidBlock) {
						continue;
					}
				}
			}
			Containers.dropItemStack(world, pos.getX(), pos.getY(),
					pos.getZ(), itemStack);
		}
	}
}

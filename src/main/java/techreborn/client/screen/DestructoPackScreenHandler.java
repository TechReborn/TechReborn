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

package techreborn.client.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import reborncore.client.gui.slots.SlotFilteredVoid;
import reborncore.common.util.RebornInventory;
import techreborn.init.TRContent;

public class DestructoPackScreenHandler extends ScreenHandler {

	private final PlayerEntity player;
	private final RebornInventory<?> inv;

	public DestructoPackScreenHandler(int syncID, PlayerEntity player) {
		super(null, syncID);
		this.player = player;
		inv = new RebornInventory<>(1, "destructopack", 64, null);
		buildContainer();
	}

	@Override
	public boolean canUse(PlayerEntity arg0) {
		return true;
	}

	private void buildContainer() {
		this.addSlot(
				new SlotFilteredVoid(inv, 0, 80, 36, new ItemStack[]{TRContent.Parts.MACHINE_PARTS.getStack()}));
		int i;

		for (i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (i = 0; i < 9; ++i) {
			this.addSlot(new Slot(player.inventory, i, 8 + i * 18, 142));
		}
	}
}

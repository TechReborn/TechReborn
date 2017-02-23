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

package techreborn.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import techreborn.tiles.TileDigitalChest;

public class GuiDigitalChest extends GuiBase {

	TileDigitalChest digitalChest;

	public GuiDigitalChest(final EntityPlayer player, final TileDigitalChest digitalChest) {
		super(player, digitalChest, digitalChest.createContainer(player));
		this.digitalChest = digitalChest;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float f, final int mouseX, final int mouseY) {
		super.drawGuiContainerBackgroundLayer(f, mouseX, mouseY);
		final Layer layer = Layer.BACKGROUND;

		this.drawSlot(80, 24, layer);
		this.drawSlot(80, 64, layer);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		final Layer layer = Layer.FOREGROUND;

		if (this.digitalChest.storedItem != ItemStack.EMPTY && this.digitalChest.getStackInSlot(1) != null) {
			this.builder.drawBigBlueBar(this, 31, 43,
					this.digitalChest.storedItem.getCount() + this.digitalChest.getStackInSlot(1).getCount(),
					this.digitalChest.maxCapacity, mouseX - this.guiLeft, mouseY - this.guiTop, "Stored", layer);
		}
		if (this.digitalChest.storedItem == ItemStack.EMPTY && this.digitalChest.getStackInSlot(1) != null) {
			this.builder.drawBigBlueBar(this, 31, 43, this.digitalChest.getStackInSlot(1).getCount(),
					this.digitalChest.maxCapacity, mouseX - this.guiLeft, mouseY - this.guiTop, "Stored", layer);
		}
	}
}

/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
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
import reborncore.client.gui.builder.GuiBase;
import techreborn.tiles.TileQuantumChest;

public class GuiQuantumChest extends GuiBase {

	TileQuantumChest quantumChest;

	public GuiQuantumChest(final EntityPlayer player, final TileQuantumChest quantumChest) {
		super(player, quantumChest, quantumChest.createContainer(player));
		this.quantumChest = quantumChest;
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

		if (!this.quantumChest.storedItem.isEmpty() && this.quantumChest.getStackInSlot(1) != null) {
			this.builder.drawBigBlueBar(this, 31, 43, this.quantumChest.storedItem.getCount() + this.quantumChest.getStackInSlot(1).getCount(), this.quantumChest.maxCapacity, mouseX - this.guiLeft, mouseY - this.guiTop, "Stored", layer);
		}
		if (this.quantumChest.storedItem.isEmpty() && this.quantumChest.getStackInSlot(1) != null) {
			this.builder.drawBigBlueBar(this, 31, 43, this.quantumChest.getStackInSlot(1).getCount(), this.quantumChest.maxCapacity, mouseX - this.guiLeft, mouseY - this.guiTop, "Stored", layer);
		}
	}
}

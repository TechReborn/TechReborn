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

import net.minecraft.entity.player.PlayerEntity;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.gui.builder.GuiBase;
import reborncore.client.gui.guibuilder.GuiBuilder;
import techreborn.blockentity.machine.tier3.IndustrialCentrifugeBlockEntity;

public class GuiCentrifuge extends GuiBase<BuiltContainer> {

	IndustrialCentrifugeBlockEntity blockEntity;

	public GuiCentrifuge(int syncID, final PlayerEntity player, final IndustrialCentrifugeBlockEntity blockEntity) {
		super(player, blockEntity, blockEntity.createContainer(syncID, player));
		this.blockEntity = blockEntity;
	}

	@Override
	protected void drawBackground(final float f, final int mouseX, final int mouseY) {
		super.drawBackground(f, mouseX, mouseY);
		final Layer layer = Layer.BACKGROUND;

		drawSlot(8, 72, layer);

		drawSlot(40, 34, layer);
		drawSlot(40, 54, layer);

		drawSlot(82, 44, layer);
		drawSlot(101, 25, layer);
		drawSlot(120, 44, layer);
		drawSlot(101, 63, layer);

		builder.drawJEIButton(this, 158, 5, layer);
	}

	@Override
	protected void drawForeground(final int mouseX, final int mouseY) {
		super.drawForeground(mouseX, mouseY);
		final Layer layer = Layer.FOREGROUND;

		builder.drawProgressBar(this, blockEntity.getProgressScaled(100), 100, 61, 47, mouseX, mouseY, GuiBuilder.ProgressDirection.RIGHT, layer);
		builder.drawMultiEnergyBar(this, 9, 19, (int) blockEntity.getEnergy(), (int) blockEntity.getMaxPower(), mouseX, mouseY, 0, layer);
	}
}

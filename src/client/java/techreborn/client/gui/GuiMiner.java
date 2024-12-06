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

package techreborn.client.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import reborncore.client.gui.GuiBase;
import reborncore.common.screen.BuiltScreenHandler;
import techreborn.blockentity.machine.tier2.MinerBlockEntity;

public class GuiMiner extends GuiBase<BuiltScreenHandler> {

	private final MinerBlockEntity blockEntity;

	public GuiMiner(int syncID, final PlayerEntity player, final MinerBlockEntity blockEntity) {
		super(player, blockEntity, blockEntity.createScreenHandler(syncID, player));
		this.blockEntity = blockEntity;
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	protected void drawBackground(DrawContext drawContext, final float partialTicks, final int mouseX, final int mouseY) {
		super.drawBackground(drawContext, partialTicks, mouseX, mouseY);
		final Layer layer = Layer.BACKGROUND;

		if (hideGuiElements()) return;

		int offsetX = 36;
		int offsetY = 21;


		drawSlot(drawContext, 8, 72, layer); // Battery slots
		drawSlot(drawContext, offsetX, offsetY, layer); // Mining Tool Slot
		drawSlot(drawContext, offsetX, offsetY + 36, layer); // Prospecting Tool Slot
		for (int i = 0; i< MinerBlockEntity.MINING_PIPE_INVENTORY_SIZE; i++){
			drawSlot(drawContext, offsetX + 21, offsetY + i * 18, layer);
		}

		for (int row=0; row < 3; row++){
			for (int column=0; column < 4; column++) {
				drawSlot(drawContext,offsetX + 50 + 18 * column, offsetY + 18 * row, layer);
			}
		}

		drawText(drawContext, blockEntity.getState().getStatus().getDisplayText(), offsetX, offsetY + 18 * 3 + 3, 0x404040, layer);

	}

	@Override
	protected void drawForeground(DrawContext drawContext, final int mouseX, final int mouseY) {
		super.drawForeground(drawContext, mouseX, mouseY);
		final Layer layer = Layer.FOREGROUND;

		builder.drawMultiEnergyBar(drawContext, this, 9, 19, (int) blockEntity.getEnergy(), (int) blockEntity.getMaxStoredPower(), mouseX, mouseY, 0, layer);
	}
}

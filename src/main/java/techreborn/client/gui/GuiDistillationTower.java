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

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import reborncore.client.gui.builder.GuiBase;
import reborncore.client.gui.builder.widget.GuiButtonExtended;
import reborncore.client.gui.guibuilder.GuiBuilder;
import reborncore.client.screen.builder.BuiltScreenHandler;
import techreborn.blockentity.machine.multiblock.DistillationTowerBlockEntity;

public class GuiDistillationTower extends GuiBase<BuiltScreenHandler> {

	private final DistillationTowerBlockEntity blockEntity;

	public GuiDistillationTower(int syncID, final PlayerEntity player, final DistillationTowerBlockEntity blockEntity) {
		super(player, blockEntity, blockEntity.createScreenHandler(syncID, player));
		this.blockEntity = blockEntity;
	}

	@Override
	protected void drawBackground(MatrixStack matrixStack, final float f, final int mouseX, final int mouseY) {
		super.drawBackground(matrixStack, f, mouseX, mouseY);
		final GuiBase.Layer layer = Layer.BACKGROUND;
		// Battery slot
		drawSlot(matrixStack, 8, 72, layer);
		// Input slots
		drawSlot(matrixStack, 35, 27, layer);
		drawSlot(matrixStack, 35, 47, layer);
		// Four output slots
		drawOutputSlotBar(matrixStack, 78, 36, 4, layer);
		// JEI Button
		builder.drawJEIButton(matrixStack, this, 158, 5, layer);
	}

	@Override
	protected void drawForeground(MatrixStack matrixStack, final int mouseX, final int mouseY) {
		super.drawForeground(matrixStack, mouseX, mouseY);
		final GuiBase.Layer layer = Layer.FOREGROUND;

		builder.drawProgressBar(matrixStack, this, blockEntity.getProgressScaled(100), 100, 55, 40, mouseX, mouseY, GuiBuilder.ProgressDirection.RIGHT, layer);
		if (blockEntity.isMultiblockValid()) {
			addHologramButton(6, 4, 212, layer).clickHandler(this::onClick);
			builder.drawHologramButton(matrixStack, this, 6, 4, mouseX, mouseY, layer);
		} else {
			builder.drawMultiblockMissingBar(matrixStack, this, layer);
			addHologramButton(76, 56, 212, layer).clickHandler(this::onClick);
			builder.drawHologramButton(matrixStack, this, 76, 56, mouseX, mouseY, layer);
		}
		builder.drawMultiEnergyBar(matrixStack, this, 9, 19, (int) blockEntity.getEnergy(), (int) blockEntity.getMaxStoredPower(), mouseX, mouseY, 0, layer);
	}

	public void onClick(GuiButtonExtended button, Double x, Double y) {
		blockEntity.renderMultiblock ^= !hideGuiElements();
	}
}

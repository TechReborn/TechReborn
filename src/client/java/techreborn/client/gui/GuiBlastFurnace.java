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
import reborncore.client.gui.widget.GuiButtonExtended;
import reborncore.client.gui.GuiBuilder;
import reborncore.common.screen.BuiltScreenHandler;
import techreborn.blockentity.machine.multiblock.IndustrialBlastFurnaceBlockEntity;

public class GuiBlastFurnace extends GuiBase<BuiltScreenHandler> {

	private final IndustrialBlastFurnaceBlockEntity blockEntity;
	boolean hasMultiBlock;

	public GuiBlastFurnace(int syncID, final PlayerEntity player, final IndustrialBlastFurnaceBlockEntity blockEntity) {
		super(player, blockEntity, blockEntity.createScreenHandler(syncID, player));
		this.blockEntity = blockEntity;
	}

	@Override
	public void init() {
		super.init();
		this.hasMultiBlock = this.blockEntity.getCachedHeat() != 0;
	}

	@Override
	protected void drawBackground(DrawContext drawContext, final float f, final int mouseX, final int mouseY) {
		super.drawBackground(drawContext, f, mouseX, mouseY);
		this.hasMultiBlock = this.blockEntity.getCachedHeat() != 0;

		final GuiBase.Layer layer = Layer.BACKGROUND;

		drawSlot(drawContext, 8, 72, layer);

		drawSlot(drawContext, 50, 27, layer);
		drawSlot(drawContext, 50, 47, layer);
		drawOutputSlotBar(drawContext, 92, 36, 2, layer);

		if (hasMultiBlock) {
			builder.drawHologramButton(drawContext, this, 6, 4, mouseX, mouseY, layer);
		}
	}

	@Override
	protected void drawForeground(DrawContext drawContext, final int mouseX, final int mouseY) {
		super.drawForeground(drawContext, mouseX, mouseY);
		this.hasMultiBlock = blockEntity.getCachedHeat() != 0;
		final GuiBase.Layer layer = GuiBase.Layer.FOREGROUND;

		builder.drawProgressBar(drawContext, this, blockEntity.getProgressScaled(100), 100, 71, 40, mouseX, mouseY, GuiBuilder.ProgressDirection.RIGHT, layer);

		builder.drawBigHeatBar(drawContext, this, 31, 71, blockEntity.getCachedHeat(), 3230, layer);
		if (hasMultiBlock) {
			addHologramButton(4, 4, 212, layer).clickHandler(this::onClick);
		} else {
			builder.drawMultiblockMissingBar(drawContext, this, layer);
			addHologramButton(76, 56, 212, layer).clickHandler(this::onClick);
			builder.drawHologramButton(drawContext, this, 76, 56, mouseX, mouseY, layer);
		}
		builder.drawMultiEnergyBar(drawContext, this, 9, 19, (int) blockEntity.getEnergy(), (int) blockEntity.getMaxStoredPower(), mouseX, mouseY, 0, layer);
	}

	public void onClick(GuiButtonExtended button, Double x, Double y) {
		blockEntity.renderMultiblock ^= !hideGuiElements();
	}
}

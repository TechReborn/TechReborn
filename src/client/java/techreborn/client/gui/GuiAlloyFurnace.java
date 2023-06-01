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
import reborncore.client.gui.GuiBuilder;
import reborncore.common.screen.BuiltScreenHandler;
import techreborn.blockentity.machine.iron.IronAlloyFurnaceBlockEntity;

public class GuiAlloyFurnace extends GuiBase<BuiltScreenHandler> {

	IronAlloyFurnaceBlockEntity blockEntity;

	public GuiAlloyFurnace(int syncID, PlayerEntity player, IronAlloyFurnaceBlockEntity alloyFurnace) {
		super(player, alloyFurnace, alloyFurnace.createScreenHandler(syncID, player));
		this.blockEntity = alloyFurnace;
	}

	@Override
	protected void drawBackground(DrawContext drawContext, float lastFrameDuration, int mouseX, int mouseY) {
		super.drawBackground(drawContext, lastFrameDuration, mouseX, mouseY);
		GuiBase.Layer layer = GuiBase.Layer.BACKGROUND;

		// Input slots
		drawSlot(drawContext, 47, 17, layer);
		drawSlot(drawContext, 65, 17, layer);
		// Fuel slot
		drawSlot(drawContext, 56, 53, layer);

		drawOutputSlot(drawContext, 116, 35, layer);
	}

	@Override
	protected void drawForeground(DrawContext drawContext, int mouseX, int mouseY) {
		super.drawForeground(drawContext, mouseX, mouseY);
		GuiBase.Layer layer = GuiBase.Layer.FOREGROUND;

		builder.drawProgressBar(drawContext, this, blockEntity.getProgressScaled(100), 100, 85, 36, mouseX, mouseY, GuiBuilder.ProgressDirection.RIGHT, layer);
		builder.drawBurnBar(drawContext, this, blockEntity.getBurnTimeRemainingScaled(100), 100, 56, 36, mouseX, mouseY, layer);
	}
}

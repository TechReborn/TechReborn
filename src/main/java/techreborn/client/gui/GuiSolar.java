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
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import reborncore.client.gui.builder.GuiBase;
import reborncore.client.screen.builder.BuiltScreenHandler;
import techreborn.blockentity.generator.SolarPanelBlockEntity;

public class GuiSolar extends GuiBase<BuiltScreenHandler> {

	SolarPanelBlockEntity blockEntity;

	public GuiSolar(int syncID, PlayerEntity player, SolarPanelBlockEntity panel) {
		super(player, panel, panel.createScreenHandler(syncID, player));
		this.blockEntity = panel;
	}

	@Override
	protected void drawForeground(MatrixStack matrixStack, int mouseX, int mouseY) {
		super.drawForeground(matrixStack, mouseX, mouseY);
		final GuiBase.Layer layer = GuiBase.Layer.FOREGROUND;

		builder.drawMultiEnergyBar(matrixStack, this, 156, 19, (int) blockEntity.getEnergy(), (int) blockEntity.getMaxStoredPower(), mouseX, mouseY, 0, layer);

		switch (blockEntity.getSunState()) {
			case SolarPanelBlockEntity.DAYGEN:
				builder.drawText(matrixStack, this, new TranslatableText("techreborn.message.daygen"), 10, 20, 15129632);
				break;
			case SolarPanelBlockEntity.NIGHTGEN:
				builder.drawText(matrixStack, this, new TranslatableText("techreborn.message.nightgen"), 10, 20, 7566195);
				break;
			case SolarPanelBlockEntity.ZEROGEN:
				builder.drawText(matrixStack, this, new TranslatableText("techreborn.message.zerogen"), 10, 20, 12066591);
				break;
		}

		builder.drawText(matrixStack, this, new LiteralText("Generating: " + blockEntity.getGenerationRate() + " E/t"), 10, 30, 0);

	}
}

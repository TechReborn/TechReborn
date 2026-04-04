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

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import reborncore.client.gui.GuiBase;
import reborncore.common.screen.BuiltScreenHandler;
import techreborn.blockentity.generator.SolarPanelBlockEntity;

public class GuiSolar extends GuiBase<BuiltScreenHandler> {

	final SolarPanelBlockEntity blockEntity;

	public GuiSolar(int syncID, Player player, SolarPanelBlockEntity panel) {
		super(player, panel, panel.createScreenHandler(syncID, player));
		this.blockEntity = panel;
	}

	@Override
	protected void extractLabels(GuiGraphicsExtractor drawContext, int mouseX, int mouseY) {
		super.extractLabels(drawContext, mouseX, mouseY);
		final GuiBase.Layer layer = GuiBase.Layer.FOREGROUND;

		builder.drawMultiEnergyBar(drawContext, this, 156, 19, (int) blockEntity.getEnergy(), (int) blockEntity.getMaxStoredPower(), mouseX, mouseY, 0, layer);

		if (!blockEntity.isGenerating()) {
			builder.drawText(drawContext, this, Component.translatable("techreborn.message.panel_blocked"), 10, 20, 0xffb81f1f);
		}

		builder.drawText(drawContext, this, Component.literal("Generating: " + blockEntity.getGenerationRate() + " E/t"), 10, 30, 0xff000000);

	}
}

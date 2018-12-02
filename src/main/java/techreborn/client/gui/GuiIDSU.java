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

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import reborncore.client.gui.builder.GuiBase;
import reborncore.common.powerSystem.PowerSystem;
import techreborn.tiles.idsu.TileInterdimensionalSU;

public class GuiIDSU extends GuiBase {

	TileInterdimensionalSU idsu;

	public GuiIDSU(EntityPlayer player, TileInterdimensionalSU tileIDSU) {
		super(player, tileIDSU, tileIDSU.createContainer(player));
		idsu = tileIDSU;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float f, final int mouseX, final int mouseY) {
		super.drawGuiContainerBackgroundLayer(f, mouseX, mouseY);
		final Layer layer = Layer.BACKGROUND;

		this.drawSlot(62, 45, layer);
		this.drawSlot(98, 45, layer);
		this.drawArmourSlots(8, 18, layer);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		final Layer layer = Layer.FOREGROUND;

		GlStateManager.pushMatrix();
		GlStateManager.scale(0.6, 0.6, 1);
		this.drawCentredString(PowerSystem.getLocaliszedPowerFormattedNoSuffix((int) this.idsu.getEnergy()) + "/" + PowerSystem.getLocaliszedPowerFormattedNoSuffix((int) this.idsu.getMaxPower()) + " " + PowerSystem.getDisplayPower().abbreviation, 35, 0, 58, layer);
		GlStateManager.popMatrix();

		this.builder.drawMultiEnergyBar(this, 81, 28, (int) this.idsu.getEnergy(), (int) this.idsu.getMaxPower(), mouseX, mouseY, 0, layer);
	}

}

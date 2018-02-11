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

package techreborn.client.gui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import reborncore.common.powerSystem.PowerSystem;
import techreborn.client.gui.GuiBase;

/**
 * Created by Prospector
 */
public class GuiButtonPowerBar extends GuiButton {

	GuiBase.Layer layer;
	GuiBase gui;

	public GuiButtonPowerBar(int buttonId, int x, int y, GuiBase gui, GuiBase.Layer layer) {
		super(buttonId, x, y, 12, 48, "");
		this.layer = layer;
		this.gui = gui;
	}

	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {

		if (layer == GuiBase.Layer.FOREGROUND) {
			mouseX -= gui.getGuiLeft();
			mouseY -= gui.getGuiTop();
		}

		if (this.enabled && this.visible && mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height) {
			PowerSystem.bumpPowerConfig();
			return true;
		}
		return false;
	}

	@Override
	public void drawButtonForegroundLayer(int mouseX, int mouseY) {

	}

	@Override
	public void drawButton(Minecraft p_191745_1_, int p_191745_2_, int p_191745_3_, float p_191745_4_) {

	}
}

/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2026 TechReborn
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

package techreborn.client.compat.jei.gui.render;

import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import reborncore.client.gui.GuiBase;
import reborncore.client.gui.GuiBuilder;
import reborncore.common.crafting.RebornRecipe;


public class EnergyDisplayDrawable implements IDrawable {
	private final RebornRecipe recipe;
	public EnergyDisplayDrawable(RebornRecipe recipe) {
		this.recipe = recipe;
	}

	@Override
	public int getWidth() {
		return 14;
	}

	@Override
	public int getHeight() {
		return 50;
	}

	@Override
	public void draw(GuiGraphicsExtractor guiGraphics, int x, int y) {
		GuiBuilder.INSTANCE.drawMultiEnergyBar(guiGraphics, JeiRenderTarget.INSTANCE, x, y, recipe.power(), recipe.power(), -1, -1, 0, GuiBase.Layer.FOREGROUND);
	}

	public static boolean isMouseOver(int x, int y, double mouseX, double mouseY) {
		return mouseX >= x && mouseX < x + 14 && mouseY >= y && mouseY < y + 50;
	}

}

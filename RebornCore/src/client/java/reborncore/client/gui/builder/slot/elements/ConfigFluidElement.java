/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2021 TeamReborn
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

package reborncore.client.gui.builder.slot.elements;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import reborncore.client.gui.builder.GuiBase;
import reborncore.common.util.Tank;

import java.util.ArrayList;
import java.util.List;

public class ConfigFluidElement extends ElementBase {
	SlotType type;
	Tank tank;
	public List<ElementBase> elements = new ArrayList<>();
	boolean filter = false;

	public ConfigFluidElement(Tank tank, SlotType type, int x, int y, GuiBase<?> gui) {
		super(x, y, type.getButtonSprite());
		this.type = type;
		this.tank = tank;

		FluidConfigPopupElement popupElement;

		elements.add(popupElement = new FluidConfigPopupElement(x - 22, y - 22, this));
		elements.add(new ButtonElement(x + 37, y - 25, Sprite.EXIT_BUTTON, gui::closeSelectedTab));

		elements.add(new CheckBoxElement(Text.translatable("reborncore.gui.fluidconfig.pullin"), x - 26, y + 42,
			checkBoxElement -> gui.getMachine().fluidConfiguration.autoInput(),
			() -> popupElement.updateCheckBox("input", gui)));
		elements.add(new CheckBoxElement(Text.translatable("reborncore.gui.fluidconfig.pumpout"), x - 26, y + 57,
			checkBoxElement -> gui.getMachine().fluidConfiguration.autoOutput(),
			() -> popupElement.updateCheckBox("output", gui)));
	}

	@Override
	public int getWidth() {
		return 85;
	}

	@Override
	public int getHeight() {
		return 105 + (filter ? 15 : 0);
	}

	@Override
	public void draw(DrawContext drawContext, GuiBase<?> gui, int mouseX, int mouseY) {
		super.draw(drawContext, gui, mouseX, mouseY);
		if (isMouseWithinRect(gui, mouseX, mouseY)) {
			drawSprite(drawContext, gui, type.getButtonHoverOverlay(), getX(), getY());
		}
		elements.forEach(elementBase -> elementBase.draw(drawContext, gui, mouseX, mouseY));
	}

	public SlotType getType() {
		return type;
	}
}

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

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;
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
		elements.add(new ButtonElement(x + 37, y - 25, Sprite.EXIT_BUTTON).addReleaseAction((element, gui1, provider, mouseX, mouseY) -> {
			gui.closeSelectedTab();
			return true;
		}));

		elements.add(new CheckBoxElement(new TranslatableText("reborncore.gui.fluidconfig.pullin"), 0xFFFFFFFF, x - 26, y + 42, "input", 0, Sprite.LIGHT_CHECK_BOX, gui.getMachine(),
				checkBoxElement -> checkBoxElement.machineBase.fluidConfiguration.autoInput()).addPressAction((element, gui12, provider, mouseX, mouseY) -> {
			popupElement.updateCheckBox((CheckBoxElement) element, "input", gui12);
			return true;
		}));
		elements.add(new CheckBoxElement(new TranslatableText("reborncore.gui.fluidconfig.pumpout"), 0xFFFFFFFF, x - 26, y + 57, "output", 0, Sprite.LIGHT_CHECK_BOX, gui.getMachine(),
				checkBoxElement -> checkBoxElement.machineBase.fluidConfiguration.autoOutput()).addPressAction((element, gui13, provider, mouseX, mouseY) -> {
			popupElement.updateCheckBox((CheckBoxElement) element, "output", gui13);
			return true;
		}));

		setWidth(85);
		setHeight(105 + (filter ? 15 : 0));
	}

	@Override
	public void draw(MatrixStack matrixStack, GuiBase<?> gui) {
		super.draw(matrixStack, gui);
		if (isHovering) {
			drawSprite(matrixStack, gui, type.getButtonHoverOverlay(), x, y);
		}
		elements.forEach(elementBase -> elementBase.draw(matrixStack, gui));
	}

	public SlotType getType() {
		return type;
	}
}
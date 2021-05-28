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
import net.minecraft.text.Text;
import reborncore.client.gui.builder.GuiBase;
import reborncore.common.blockentity.MachineBaseBlockEntity;

import java.util.function.Predicate;

public class CheckBoxElement extends ElementBase {
	public Text label;
	public String type;
	public int labelColor, slotID;
	public MachineBaseBlockEntity machineBase;
	Predicate<CheckBoxElement> ticked;

	private final Sprite.CheckBox checkBoxSprite;

	public CheckBoxElement(Text label, int labelColor, int x, int y, String type, int slotID, Sprite.CheckBox checkBoxSprite, MachineBaseBlockEntity machineBase, Predicate<CheckBoxElement> ticked) {
		super(x, y, checkBoxSprite.getNormal());
		this.checkBoxSprite = checkBoxSprite;
		this.type = type;
		this.slotID = slotID;
		this.machineBase = machineBase;
		this.label = label;
		this.labelColor = labelColor;
		this.ticked = ticked;
		if (ticked.test(this)) {
			container.setSprite(0, checkBoxSprite.getTicked());
		} else {
			container.setSprite(0, checkBoxSprite.getNormal());
		}
		this.addPressAction((element, gui, provider, mouseX, mouseY) -> {
			if (ticked.test(this)) {
				element.container.setSprite(0, checkBoxSprite.getTicked());
			} else {
				element.container.setSprite(0, checkBoxSprite.getNormal());
			}
			return true;
		});
	}

	@Override
	public void draw(MatrixStack matrixStack, GuiBase<?> gui) {
		//	super.draw(gui);
		ISprite sprite = checkBoxSprite.getNormal();
		if (ticked.test(this)) {
			sprite = checkBoxSprite.getTicked();
		}
		drawSprite(matrixStack, gui, sprite, x, y);
		drawText(matrixStack, gui, label, x + checkBoxSprite.getNormal().width + 5, ((y + getHeight(gui.getMachine()) / 2) - (gui.getTextRenderer().fontHeight / 2)), labelColor);
	}

}

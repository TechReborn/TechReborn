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

package techreborn.client.gui.slot.elements;

import reborncore.common.tile.TileLegacyMachineBase;
import techreborn.client.gui.GuiBase;

public class CheckBoxElement extends ElementBase {
	public String label, type;
	public int labelColor, slotID;
	TileLegacyMachineBase machineBase;

	private Sprite.CheckBox checkBoxSprite;

	public CheckBoxElement(String label, int labelColor, int x, int y, String type, int slotID, Sprite.CheckBox checkBoxSprite, TileLegacyMachineBase machineBase) {
		super(x, y, checkBoxSprite.getNormal());
		this.checkBoxSprite = checkBoxSprite;
		this.type = type;
		this.slotID = slotID;
		this.machineBase = machineBase;
		this.label = label;
		this.labelColor = labelColor;
		if (isTicked()) {
			container.setSprite(0, checkBoxSprite.getTicked());
		} else {
			container.setSprite(0, checkBoxSprite.getNormal());
		}
		this.addPressAction((element, gui, provider, mouseX, mouseY) -> {
			if (isTicked()) {
				element.container.setSprite(0, checkBoxSprite.getTicked());
			} else {
				element.container.setSprite(0, checkBoxSprite.getNormal());
			}
			return true;
		});
	}

	@Override
	public void draw(GuiBase gui) {
	//	super.draw(gui);
		ISprite sprite = checkBoxSprite.getNormal();
		if(isTicked()){
			sprite = checkBoxSprite.getTicked();
		}
		drawSprite(gui, sprite, x, y );
		drawString(gui, label, x + checkBoxSprite.getNormal().width + 5, ((y + getHeight(gui.getMachine()) / 2) - (gui.mc.fontRenderer.FONT_HEIGHT / 2)), labelColor);
	}

	public boolean isTicked() {
		if(type.equalsIgnoreCase("output")){
			return machineBase.slotConfiguration.getSlotDetails(slotID).autoOutput();
		}
		if(type.equalsIgnoreCase("filter")){
			return machineBase.slotConfiguration.getSlotDetails(slotID).filter();
		}
		return machineBase.slotConfiguration.getSlotDetails(slotID).autoInput();
	}

}

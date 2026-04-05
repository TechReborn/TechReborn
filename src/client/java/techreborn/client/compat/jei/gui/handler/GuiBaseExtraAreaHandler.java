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

package techreborn.client.compat.jei.gui.handler;

import java.util.ArrayList;
import java.util.List;

import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import net.minecraft.client.renderer.Rect2i;
import reborncore.api.blockentity.IUpgradeable;
import reborncore.client.gui.GuiBase;
import reborncore.client.gui.config.GuiTab;
import reborncore.client.gui.config.SlotConfigGui;
import reborncore.client.gui.config.elements.ConfigSlotElement;

public class GuiBaseExtraAreaHandler implements IGuiContainerHandler<GuiBase<?>> {

	@Override
	public List<Rect2i> getGuiExtraAreas(GuiBase<?> guiBase) {
		List<Rect2i> extraAreas = new ArrayList<>(2);
		int height = 0;
		if(guiBase.tryAddUpgrades() && guiBase.be instanceof IUpgradeable upgradeable) {
			if(upgradeable.canBeUpgraded()) {
				height = 80;
			}
		}
		for(GuiTab slot : guiBase.getTabs()) {
			if(slot.enabled()) {
				height += 24;
			}
		}
		if(height > 0) {
			int width = 20;
			extraAreas.add(new Rect2i(guiBase.getGuiLeft() - width, guiBase.getGuiTop() + 8, width, height));
		}
		if(guiBase.getSelectedTab() instanceof SlotConfigGui slotConfigGui) {
			ConfigSlotElement element = slotConfigGui.getSelectedSlot();
			if(element != null) {
				int slotX = element.getX() + guiBase.getGuiLeft() - 50;
				if(element.getWidth() + slotX > guiBase.getScreenWidth()) {
					int slotY = element.getY() + guiBase.getGuiTop() + 25;
					int exclusionX = guiBase.getScreenWidth() + guiBase.getGuiLeft();
					int exclusionY = slotY + guiBase.getGuiTop() - (element.getHeight()/2);
					int exclusionWidth = element.getWidth() + slotX - guiBase.getScreenWidth() + 15;
					extraAreas.add(new Rect2i(exclusionX, exclusionY, exclusionWidth, element.getHeight()));
				}
			}
		}
		return extraAreas;
	}
}

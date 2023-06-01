/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2022 TechReborn
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

package techreborn.client.compat.rei;

import com.google.common.collect.Lists;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.registry.screen.ExclusionZonesProvider;
import reborncore.client.gui.GuiBase;
import reborncore.client.gui.config.SlotConfigGui;
import reborncore.client.gui.config.elements.ConfigSlotElement;

import java.util.Collection;
import java.util.Collections;

public class SlotConfigExclusionZones implements ExclusionZonesProvider<GuiBase<?>> {

	@Override
	public Collection<Rectangle> provide(GuiBase screen) {
		if (!(screen.getSelectedTab() instanceof SlotConfigGui slotConfigGui)){
			return Collections.emptyList();
		}

		final ConfigSlotElement element = slotConfigGui.getSelectedSlot();

		if (element == null) {
			return Collections.emptyList();
		}

		int slotX = element.getX() + screen.getGuiLeft() -50;
		if (element.getWidth() + slotX > screen.getScreenWidth() ) {
			int slotY = element.getY() + screen.getGuiTop() + 25;
			int exclusionX = screen.getScreenWidth() + screen.getGuiLeft();
			int exclusionY = slotY + screen.getGuiTop() - (element.getHeight()/2);
			int exclusionWidth = element.getWidth() + slotX - screen.getScreenWidth() + 15;
			return Lists.newArrayList(new Rectangle(exclusionX, exclusionY, exclusionWidth, element.getHeight()));
		}

		return Collections.emptyList();
	}
}

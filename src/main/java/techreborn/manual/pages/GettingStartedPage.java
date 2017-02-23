/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 TechReborn
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

package techreborn.manual.pages;

import net.minecraft.client.gui.GuiButton;
import techreborn.items.ItemParts;
import techreborn.items.ItemPlates;
import techreborn.manual.PageCollection;
import techreborn.manual.Reference;
import techreborn.manual.util.ButtonUtil;
import techreborn.manual.util.GuiButtonItemTexture;

import java.awt.*;

public class GettingStartedPage extends TitledPage {
	public GettingStartedPage(String name, PageCollection collection) {
		super(name, false, collection, Reference.GETTINGSTARTED_KEY, Color.white.getRGB());
	}

	@Override
	public void initGui() {
		buttonList.clear();
		ButtonUtil.addBackButton(0, width / 2 - 60, height / 2 + 64, buttonList);
		buttonList.add(new GuiButtonItemTexture(1, getXMin() + 20, getYMin() + 20, 0, 46, 100, 20,
			ItemParts.getPartByName("rubberSap"), Reference.pageNames.GETTINGRUBBER_PAGE,
			ttl(Reference.GETTINGRUBBER_KEY)));
		buttonList.add(new GuiButtonItemTexture(2, getXMin() + 20, getYMin() + 40, 0, 46, 100, 20,
			ItemPlates.getPlateByName("iron"), Reference.pageNames.CRAFTINGPLATES_PAGE,
			ttl(Reference.CRAFTINGPLATES_KEY)));
	}

	@Override
	public void actionPerformed(GuiButton button) {
		if (button.id == 0)
			collection.changeActivePage(Reference.pageNames.CONTENTS_PAGE);
		if (button.id == 1)
			collection.changeActivePage(Reference.pageNames.GETTINGRUBBER_PAGE);
		if (button.id == 2)
			collection.changeActivePage(Reference.pageNames.CRAFTINGPLATES_PAGE);

	}
}

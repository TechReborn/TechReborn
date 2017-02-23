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
import net.minecraft.item.ItemStack;
import techreborn.init.ModBlocks;
import techreborn.manual.PageCollection;
import techreborn.manual.Reference;
import techreborn.manual.util.ButtonUtil;
import techreborn.manual.util.GuiButtonItemTexture;

import java.awt.*;

public class GeneratingPowerPage extends TitledPage {
	public GeneratingPowerPage(String name, PageCollection collection) {
		super(name, false, collection, Reference.GENERATINGPOWER_KEY, Color.white.getRGB());
	}

	@Override
	public void initGui() {
		buttonList.clear();
		ButtonUtil.addBackButton(0, width / 2 - 60, height / 2 + 64, buttonList);
		buttonList.add(new GuiButtonItemTexture(1, getXMin() + 20, getYMin() + 20, 0, 46, 100, 20,
			new ItemStack(ModBlocks.SOLID_FUEL_GENEREATOR), ModBlocks.SOLID_FUEL_GENEREATOR.getUnlocalizedName(),
			ttl(ModBlocks.SOLID_FUEL_GENEREATOR.getLocalizedName())));
		buttonList.add(new GuiButtonItemTexture(2, getXMin() + 20, getYMin() + 40, 0, 46, 100, 20,
			new ItemStack(ModBlocks.THERMAL_GENERATOR), ModBlocks.THERMAL_GENERATOR.getUnlocalizedName(),
			ttl(ModBlocks.THERMAL_GENERATOR.getLocalizedName())));
		buttonList.add(new GuiButtonItemTexture(3, getXMin() + 20, getYMin() + 60, 0, 46, 100, 20,
			new ItemStack(ModBlocks.SOLAR_PANEL), ModBlocks.SOLAR_PANEL.getUnlocalizedName(),
			ttl(ModBlocks.SOLAR_PANEL.getLocalizedName())));
		buttonList.add(new GuiButtonItemTexture(4, getXMin() + 20, getYMin() + 80, 0, 46, 100, 20,
			new ItemStack(ModBlocks.HEAT_GENERATOR), ModBlocks.HEAT_GENERATOR.getUnlocalizedName(),
			ttl(ModBlocks.HEAT_GENERATOR.getLocalizedName())));
		buttonList.add(new GuiButtonItemTexture(5, getXMin() + 20, getYMin() + 100, 0, 46, 100, 20,
			new ItemStack(ModBlocks.LIGHTNING_ROD), ModBlocks.LIGHTNING_ROD.getUnlocalizedName(),
			ttl(ModBlocks.LIGHTNING_ROD.getLocalizedName())));
	}

	@Override
	public void actionPerformed(GuiButton button) {
		if (button.id == 0)
			collection.changeActivePage(Reference.pageNames.CONTENTS_PAGE);
		if (button.id == 1)
			collection.changeActivePage(ModBlocks.SOLID_FUEL_GENEREATOR.getLocalizedName());
		if (button.id == 2)
			collection.changeActivePage(ModBlocks.THERMAL_GENERATOR.getLocalizedName());
		if (button.id == 3)
			collection.changeActivePage(ModBlocks.SOLAR_PANEL.getLocalizedName());
		if (button.id == 4)
			collection.changeActivePage(ModBlocks.HEAT_GENERATOR.getLocalizedName());
		if (button.id == 5)
			collection.changeActivePage(ModBlocks.LIGHTNING_ROD.getLocalizedName());
	}
}

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

public class AdvancedMachines extends TitledPage {
	public AdvancedMachines(String name, PageCollection collection) {
		super(name, false, collection, Reference.ADVANCEDMACHINES_KEY, Color.white.getRGB());
	}

	@Override
	public void initGui() {
		buttonList.clear();
		ButtonUtil.addBackButton(0, width / 2 - 60, height / 2 + 64, buttonList);
		buttonList.add(new GuiButtonItemTexture(1, getXMin() + 10, getYMin() + 20, 0, 46, 100, 20,
			new ItemStack(ModBlocks.INDUSTRIAL_BLAST_FURNACE), ModBlocks.INDUSTRIAL_BLAST_FURNACE.getUnlocalizedName(),
			ttl(ModBlocks.INDUSTRIAL_BLAST_FURNACE.getLocalizedName())));
		buttonList.add(new GuiButtonItemTexture(2, getXMin() + 10, getYMin() + 40, 0, 46, 100, 20,
			new ItemStack(ModBlocks.INDUSTRIAL_SAWMILL), ModBlocks.INDUSTRIAL_SAWMILL.getUnlocalizedName(),
			ttl(ModBlocks.INDUSTRIAL_SAWMILL.getLocalizedName())));
		buttonList.add(new GuiButtonItemTexture(3, getXMin() + 10, getYMin() + 60, 0, 46, 100, 20,
			new ItemStack(ModBlocks.INDUSTRIAL_ELECTROLYZER), ModBlocks.INDUSTRIAL_ELECTROLYZER.getUnlocalizedName(),
			ttl(ModBlocks.INDUSTRIAL_ELECTROLYZER.getLocalizedName())));
		buttonList.add(new GuiButtonItemTexture(4, getXMin() + 10, getYMin() + 80, 0, 46, 100, 20,
			new ItemStack(ModBlocks.INDUSTRIAL_GRINDER), ModBlocks.INDUSTRIAL_GRINDER.getUnlocalizedName(),
			ttl(ModBlocks.INDUSTRIAL_GRINDER.getLocalizedName())));
		buttonList.add(new GuiButtonItemTexture(5, getXMin() + 10, getYMin() + 100, 0, 46, 100, 20,
			new ItemStack(ModBlocks.IMPLOSION_COMPRESSOR), ModBlocks.IMPLOSION_COMPRESSOR.getUnlocalizedName(),
			ttl(ModBlocks.IMPLOSION_COMPRESSOR.getLocalizedName())));
		buttonList.add(new GuiButtonItemTexture(6, getXMin() + 10, getYMin() + 120, 0, 46, 100, 20,
			new ItemStack(ModBlocks.INDUSTRIAL_CENTRIFUGE), ModBlocks.INDUSTRIAL_CENTRIFUGE.getUnlocalizedName(),
			ttl(ModBlocks.INDUSTRIAL_CENTRIFUGE.getLocalizedName())));
	}

	@Override
	public void actionPerformed(GuiButton button) {
		if (button.id == 0)
			collection.changeActivePage(Reference.pageNames.CONTENTS_PAGE);
		if (button.id == 1)
			collection.changeActivePage(ModBlocks.INDUSTRIAL_BLAST_FURNACE.getLocalizedName());
		if (button.id == 2)
			collection.changeActivePage(ModBlocks.INDUSTRIAL_SAWMILL.getLocalizedName());
		if (button.id == 3)
			collection.changeActivePage(ModBlocks.INDUSTRIAL_ELECTROLYZER.getLocalizedName());
		if (button.id == 4)
			collection.changeActivePage(ModBlocks.INDUSTRIAL_GRINDER.getLocalizedName());
		if (button.id == 5)
			collection.changeActivePage(ModBlocks.IMPLOSION_COMPRESSOR.getLocalizedName());
		if (button.id == 6)
			collection.changeActivePage(ModBlocks.INDUSTRIAL_CENTRIFUGE.getLocalizedName());
	}
}

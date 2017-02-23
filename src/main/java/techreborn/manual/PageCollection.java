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

package techreborn.manual;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import techreborn.manual.pages.BasePage;

import java.io.IOException;
import java.util.List;

public class PageCollection extends Gui {

	public final List<BasePage> pages = Lists.newArrayList();
	protected int x;
	protected int y;
	private String ACTIVE_PAGE = Reference.pageNames.CONTENTS_PAGE;

	public PageCollection() {
		this.x = 0;
		this.y = 0;
	}

	public void addPage(BasePage page) {
		pages.add(page);
	}

	public BasePage getPageByName(String name) {
		for (BasePage component : pages) {
			if (component.getReferenceName().equals(ACTIVE_PAGE)) {
				return component;
			}
		}
		return null;
	}

	public BasePage getActivePage() {
		for (BasePage component : pages) {
			if (component.getReferenceName().equals(ACTIVE_PAGE)) {
				return component;
			}
		}
		return null;
	}

	public final void drawScreen(Minecraft minecraft, int offsetX, int offsetY, int mouseX, int mouseY) {
		if (getActivePage() == null)
			return;
		getActivePage().drawScreen(minecraft, offsetX, offsetY, mouseX, mouseY);
	}

	public final void renderBackgroundLayer(Minecraft minecraft, int offsetX, int offsetY, int mouseX, int mouseY) {
		if (getActivePage() == null)
			return;
		getActivePage().renderBackgroundLayer(minecraft, offsetX, offsetY, mouseX, mouseY);
	}

	public void changeActivePage(String newPage) {
		ACTIVE_PAGE = newPage;
		if (getActivePage() == null)
			return;
		getActivePage().setWorldAndResolution(Minecraft.getMinecraft(), x, y);
	}

	public void setWorldAndResolution(Minecraft minecraft, int x, int y) {
		if (getActivePage() == null)
			return;
		getActivePage().setWorldAndResolution(minecraft, x, y);
		this.x = x;
		this.y = y;
	}

	protected void actionPerformed(GuiButton button) {
		if (getActivePage() == null)
			return;
		getActivePage().actionPerformed(button);
	}

	// protected void mouseMovedOrUp(int par1, int par2, int par3) {
	// if (getActivePage() == null) return;
	// getActivePage().mo(par1, par2, par3);
	// }

	protected void mouseClicked(int par1, int par2, int par3) throws IOException {
		if (getActivePage() == null)
			return;
		getActivePage().mouseClicked(par1, par2, par3);
	}

}

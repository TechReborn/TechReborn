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

package techreborn.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import techreborn.items.tools.ItemTechManual;

import java.awt.*;

public class GuiManual extends GuiScreen {

	ItemTechManual manual;
	EntityPlayer player;

	private static final ResourceLocation texture = new ResourceLocation("techreborn", "textures/gui/manual.png");
	int guiWidth = 207;
	int guiHeight = 195;
	private static final String text1 = I18n.format("techreborn.manual.wiki");
	private static final String text2 = I18n.format("techreborn.manual.discord");


	public GuiManual(EntityPlayer player) {
		this.player = player;
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void initGui() {
		this.buttonList.add(new GuiButton(1, (width / 2 - 30), (height / 2 - (guiHeight / 4)) + 17, 60, 20, I18n.format("techreborn.manual.wikibtn")));
		this.buttonList.add(new GuiButton(2, (width / 2 - 30), (height / 2) + 22, 60, 20, I18n.format("techreborn.manual.discordbtn")));
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		this.mc.getTextureManager().bindTexture(GuiManual.texture);
		int centerX = (width / 2) - guiWidth / 2;
		int centerY = (height / 2) - guiHeight / 2;
		drawTexturedModalRect(centerX, centerY, 0, 0, guiWidth, guiHeight);
		this.fontRenderer.drawString(text1, ((width / 2) - this.fontRenderer.getStringWidth(text1) / 2), height / 2 - (guiHeight / 4), 4210752);
		this.fontRenderer.drawString(text2, ((width / 2) - this.fontRenderer.getStringWidth(text2) / 2), height / 2 + 5, 4210752);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		switch (button.id) {
			case 1:
				this.mc.displayGuiScreen(new GuiConfirmOpenLink(this, "http://wiki.techreborn.ovh", 1, false));
				break;
			case 2:
				this.mc.displayGuiScreen(new GuiConfirmOpenLink(this, "http://discord.gg/0tCDWb77cvetwm0e", 2, false));
				break;
		}
	}

	@Override
	public void confirmClicked(boolean result, int id) {
		switch(id) {
			case 1:
				if(result == true) {
					try {
						Desktop.getDesktop().browse(new java.net.URI("http://wiki.techreborn.ovh"));
					} catch (Exception e) {
						System.err.print(e);
					}
				}else {
					this.mc.displayGuiScreen(this);
				}
				break;
			case 2:
				if(result == true) {
					try {
						Desktop.getDesktop().browse(new java.net.URI("http://discord.gg/0tCDWb77cvetwm0e"));
					} catch (Exception e) {
						System.err.print(e);
					}
				}else {
					this.mc.displayGuiScreen(this);
				}
				break;
		}
		}
	}



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

package techreborn.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import reborncore.common.powerSystem.PowerSystem;
import techreborn.client.container.ContainerLESU;
import techreborn.tiles.lesu.TileLapotronicSU;

import java.awt.*;

public class GuiLESU extends GuiContainer {

	private static final ResourceLocation texture = new ResourceLocation("techreborn", "textures/gui/aesu.png");

	TileLapotronicSU aesu;

	ContainerLESU containerLesu;

	public GuiLESU(EntityPlayer player, TileLapotronicSU tileaesu) {
		super(new ContainerLESU(tileaesu, player));
		this.xSize = 176;
		this.ySize = 197;
		aesu = tileaesu;
		this.containerLesu = (ContainerLESU) this.inventorySlots;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(texture);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
	}

	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
		this.fontRenderer.drawString(I18n.translateToLocal("tile.techreborn:lapotronic_su.name"), 40, 10,
			Color.WHITE.getRGB());
		this.fontRenderer.drawString(PowerSystem.getLocaliszedPower(containerLesu.euOut) + "/t", 10, 20,
			Color.WHITE.getRGB());
		this.fontRenderer.drawString(PowerSystem.getLocaliszedPower(containerLesu.storedEu), 10, 30,
			Color.WHITE.getRGB());
		this.fontRenderer.drawString(PowerSystem.getLocaliszedPower(containerLesu.euChange) + " change", 10, 40,
			Color.WHITE.getRGB());
		this.fontRenderer.drawString(containerLesu.connectedBlocks + " blocks", 10, 50, Color.WHITE.getRGB());
		this.fontRenderer.drawString(PowerSystem.getLocaliszedPower(containerLesu.euStorage) + " max", 10, 60,
			Color.WHITE.getRGB());
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

}

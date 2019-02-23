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

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import reborncore.client.gui.builder.GuiBase;
import techreborn.init.TRContent;
import techreborn.tiles.machine.iron.TileIronFurnace;

public class GuiIronFurnace extends GuiBase {

	public static final ResourceLocation texture = new ResourceLocation("minecraft",
		"textures/gui/container/furnace.png");

	TileIronFurnace furnace;

	public GuiIronFurnace(final EntityPlayer player, final TileIronFurnace furnace) {
		super(player, furnace,  furnace.createContainer(player));
		this.xSize = 176;
		this.ySize = 167;
		this.furnace = furnace;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		drawDefaultBackground();
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		builder.drawSlotTab(this, guiLeft - 24, guiTop + 6, new ItemStack(TRContent.WRENCH));
		mc.getTextureManager().bindTexture(GuiIronFurnace.texture);
		final int k = (this.width - xSize) / 2;
		final int l = (this.height - ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);

		int j = 0;

		j = furnace.gaugeProgressScaled(24);
		if (j > 0) {
			drawTexturedModalRect(k + 78, l + 35, 176, 14, j + 1, 16);
		}

		j = furnace.gaugeFuelScaled(12);
		if (j > 0) {
			drawTexturedModalRect(k + 57, l + 36 + 12 - j, 176, 12 - j, 14, j + 2);
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		final String name = I18n.format("tile.techreborn.iron_furnace.name");
		fontRenderer.drawString(name, xSize / 2 - fontRenderer.getStringWidth(name) / 2, 6, 4210752);
		fontRenderer.drawString(I18n.format("container.inventory", new Object[0]), 8, ySize - 96 + 2, 4210752);

		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		super.render(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}

}

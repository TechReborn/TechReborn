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
import techreborn.tiles.multiblock.TileImplosionCompressor;

public class GuiImplosionCompressor extends GuiContainer {

	public static final ResourceLocation texture = new ResourceLocation("techreborn",
		"textures/gui/implosion_compressor.png");

	TileImplosionCompressor compressor;

	public GuiImplosionCompressor(final EntityPlayer player, final TileImplosionCompressor compressor) {
		super(compressor.createContainer(player));
		this.xSize = 176;
		this.ySize = 167;
		this.compressor = compressor;
	}

	@Override
	public void initGui() {
		final int k = (this.width - this.xSize) / 2;
		final int l = (this.height - this.ySize) / 2;
		super.initGui();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float p_146976_1_, final int p_146976_2_, final int p_146976_3_) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(GuiImplosionCompressor.texture);
		final int k = (this.width - this.xSize) / 2;
		final int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

		if (!this.compressor.getMutliBlock()) {
			// GuiDraw.drawTooltipBox(k + 30, l + 50 + 12 - 0, 114, 10);
			this.fontRendererObj.drawString(I18n.translateToLocal("techreborn.message.missingmultiblock"), k + 38, l + 52 + 12, -1);
		}

		this.mc.getTextureManager().bindTexture(GuiImplosionCompressor.texture);
		int j = this.compressor.getProgressScaled(24);
		if (j > 0) {
			this.drawTexturedModalRect(k + 61, l + 37, 176, 14, j + 1, 16);
		}

		j = this.compressor.getEnergyScaled(12);
		if (j > 0) {
			this.drawTexturedModalRect(k + 14, l + 36 + 12 - j, 176, 12 - j, 14, j + 2);
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int p_146979_1_, final int p_146979_2_) {
		final String name = I18n.translateToLocal("tile.techreborn.implosioncompressor.name");
		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
		this.fontRendererObj.drawString(I18n.translateToLocalFormatted("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
	}

}

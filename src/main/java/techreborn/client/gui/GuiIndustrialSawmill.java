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
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fluids.FluidStack;
import techreborn.tiles.multiblock.TileIndustrialSawmill;

public class GuiIndustrialSawmill extends GuiContainer {

	public static final ResourceLocation texture = new ResourceLocation("techreborn",
		"textures/gui/industrial_sawmill.png");

	TileIndustrialSawmill sawmill;

	public GuiIndustrialSawmill(final EntityPlayer player, final TileIndustrialSawmill sawmill) {
		super(sawmill.createContainer(player));
		this.xSize = 176;
		this.ySize = 167;
		this.sawmill = sawmill;
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
		this.mc.getTextureManager().bindTexture(GuiIndustrialSawmill.texture);
		final int k = (this.width - this.xSize) / 2;
		final int l = (this.height - this.ySize) / 2;

		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

		final int progress = this.sawmill.getProgressScaled(24);
		this.drawTexturedModalRect(k + 56, l + 38, 176, 14, progress - 1, 11);

		final int energy = 13 - (int) (this.sawmill.getEnergy() / this.sawmill.getMaxPower() * 13F);
		this.drawTexturedModalRect(k + 36, l + 66 + energy, 179, 1 + energy, 7, 13 - energy);

		if (!this.sawmill.tank.isEmpty()) {
			this.drawFluid(this.sawmill.tank.getFluid(), k + 11, l + 66, 12, 47, this.sawmill.tank.getCapacity());

			final int j = this.sawmill.getEnergyScaled(12);
			if (j > 0) {
				this.drawTexturedModalRect(k + 33, l + 65 + 12 - j, 176, 12 - j, 14, j + 2);
			}

			if (!this.sawmill.getMutliBlock()) {
				//GuiUtil.drawTooltipBox(k + 30, l + 50 + 12, 114, 10);
				this.fontRenderer.drawString(I18n.translateToLocal("techreborn.message.missingmultiblock"), k + 38,
					l + 52 + 12, -1);
			}
		}
	}

	public void drawFluid(final FluidStack fluid, final int x, final int y, final int width, final int height, final int maxCapacity) {
		this.mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		final ResourceLocation still = fluid.getFluid().getStill(fluid);
		final TextureAtlasSprite sprite = this.mc.getTextureMapBlocks().getAtlasSprite(still.toString());

		final int drawHeight = (int) (fluid.amount / (maxCapacity * 1F) * height);
		final int iconHeight = sprite.getIconHeight();
		int offsetHeight = drawHeight;

		int iteration = 0;
		while (offsetHeight != 0) {
			final int curHeight = offsetHeight < iconHeight ? offsetHeight : iconHeight;
			this.drawTexturedModalRect(x, y - offsetHeight, sprite, width, curHeight);
			offsetHeight -= curHeight;
			iteration++;
			if (iteration > 50)
				break;
		}

	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int p_146979_1_, final int p_146979_2_) {
		final String name = I18n.translateToLocal("tile.techreborn:industrial_sawmill.name");
		this.fontRenderer.drawString(name, this.xSize / 2 - this.fontRenderer.getStringWidth(name) / 2, 6, 4210752);
		this.fontRenderer.drawString(I18n.translateToLocalFormatted("container.inventory"), 58, this.ySize - 96 + 2, 4210752);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}
}

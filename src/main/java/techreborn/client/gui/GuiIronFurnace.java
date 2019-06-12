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

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import reborncore.client.gui.builder.GuiBase;
import techreborn.init.TRContent;
import techreborn.tiles.machine.iron.TileIronFurnace;

public class GuiIronFurnace extends GuiBase {

	public static final Identifier texture = new Identifier("minecraft",
		"textures/gui/container/furnace.png");

	TileIronFurnace furnace;

	public GuiIronFurnace(final PlayerEntity player, final TileIronFurnace furnace) {
		super(player, furnace,  furnace.createContainer(player));
		this.containerWidth = 176;
		this.containerHeight = 167;
		this.furnace = furnace;
	}

	@Override
	protected void drawBackground(float partialTicks, int mouseX, int mouseY) {
		renderBackground();
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		builder.drawSlotTab(this, left - 24, top + 6, new ItemStack(TRContent.WRENCH));
		minecraft.getTextureManager().bindTexture(GuiIronFurnace.texture);
		final int k = (this.width - containerWidth) / 2;
		final int l = (this.height - containerHeight) / 2;
		blit(k, l, 0, 0, containerWidth, containerHeight);

		int j = 0;

		j = furnace.gaugeProgressScaled(24);
		if (j > 0) {
			blit(k + 78, l + 35, 176, 14, j + 1, 16);
		}

		j = furnace.gaugeFuelScaled(12);
		if (j > 0) {
			blit(k + 57, l + 36 + 12 - j, 176, 12 - j, 14, j + 2);
		}
	}

	@Override
	protected void drawForeground(int mouseX, int mouseY) {
		final String name = I18n.translate("tile.techreborn.iron_furnace.name");
		font.draw(name, containerWidth / 2 - font.getStringWidth(name) / 2, 6, 4210752);
		font.draw(I18n.translate("container.inventory", new Object[0]), 8, containerHeight - 96 + 2, 4210752);

		super.drawForeground(mouseX, mouseY);
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		super.render(mouseX, mouseY, partialTicks);
		drawMouseoverTooltip(mouseX, mouseY);
	}

}

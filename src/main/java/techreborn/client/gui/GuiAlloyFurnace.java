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
import net.minecraft.client.gui.screen.ingame.AbstractContainerScreen;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import techreborn.tiles.machine.iron.TileIronAlloyFurnace;

public class GuiAlloyFurnace extends AbstractContainerScreen<BuiltContainer> {

	private static final Identifier texture = new Identifier("techreborn",
		"textures/gui/alloy_furnace.png");

	TileIronAlloyFurnace alloyfurnace;

	public GuiAlloyFurnace(final PlayerEntity player, final TileIronAlloyFurnace alloyFurnace) {
		super(alloyFurnace.createContainer(player), player.inventory, new LiteralText("techreborn.alloy_furnace"));
		this.containerWidth = 176;
		this.containerHeight = 167;
		this.alloyfurnace = alloyFurnace;
	}

	@Override
	protected void drawBackground(final float p_146976_1_, final int p_146976_2_, final int p_146976_3_) {
		this.renderBackground();
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bindTexture(GuiAlloyFurnace.texture);
		final int k = (this.width - this.containerWidth) / 2;
		final int l = (this.height - this.containerHeight) / 2;
		this.blit(k, l, 0, 0, this.containerWidth, this.containerHeight);

		if (this.alloyfurnace.isBurning()) {
			int i1 = this.alloyfurnace.getBurnTimeRemainingScaled(13);
			this.blit(k + 56, l + 36 + 12 - i1, 176, 12 - i1, 14, i1 + 1);
			i1 = this.alloyfurnace.getCookProgressScaled(24);
			this.blit(k + 79, l + 34, 176, 14, i1 + 1, 16);
		}
	}

	@Override
	protected void drawForeground(final int p_146979_1_, final int p_146979_2_) {
		final String name = I18n.translate("tile.techreborn.iron_alloy_furnace.name");
		this.font.draw(name, this.containerWidth / 2 - this.font.getStringWidth(name) / 2, 6,
			4210752);
		this.font.draw(I18n.translate("container.inventory", new Object[0]), 8,
			this.containerHeight - 96 + 2, 4210752);
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		super.render(mouseX, mouseY, partialTicks);
		this.drawMouseoverTooltip(mouseX, mouseY);
	}
}

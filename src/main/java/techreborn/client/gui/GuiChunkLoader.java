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
import net.minecraft.client.gui.widget.ButtonWidget;
import reborncore.common.util.StringUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.gui.builder.widget.GuiButtonSimple;
import techreborn.blockentity.machine.tier3.ChunkLoaderBlockEntity;

public class GuiChunkLoader extends AbstractContainerScreen<BuiltContainer> {

	private static final Identifier texture = new Identifier("techreborn",
		"textures/gui/industrial_chunkloader.png");
	ChunkLoaderBlockEntity chunkloader;
	private ButtonWidget plusOneButton;
	private ButtonWidget plusTenButton;
	private ButtonWidget minusOneButton;
	private ButtonWidget minusTenButton;

	public GuiChunkLoader(int syncID, final PlayerEntity player, final ChunkLoaderBlockEntity chunkLoader) {
		super(chunkLoader.createContainer(syncID, player), player.inventory, new LiteralText("techreborn.chunkloader"));
		this.containerWidth = 176;
		this.containerHeight = 167;
		this.chunkloader = chunkLoader;
	}

	@Override
	public void init() {
		super.init();
		this.left = this.width / 2 - this.containerWidth / 2;
		this.top = this.height / 2 - this.containerHeight / 2;
		this.plusOneButton = new GuiButtonSimple(this.left + 5, this.top + 37, 40, 20, "+1", buttonWidget -> {});
		this.plusTenButton = new GuiButtonSimple(this.left + 45, this.top + 37, 40, 20, "+10", buttonWidget -> {});

		this.minusOneButton = new GuiButtonSimple(this.left + 90, this.top + 37, 40, 20, "-1", buttonWidget -> {});
		this.minusTenButton = new GuiButtonSimple(this.left + 130, this.top + 37, 40, 20, "-10", buttonWidget -> {});

		this.buttons.add(this.plusOneButton);
		this.buttons.add(this.plusTenButton);
		this.buttons.add(this.minusOneButton);
		this.buttons.add(this.minusTenButton);
	}

	@Override
	protected void drawBackground(final float p_146976_1_, final int p_146976_2_, final int p_146976_3_) {
		this.renderBackground();
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		minecraft.getTextureManager().bindTexture(GuiChunkLoader.texture);
		final int k = (this.width - this.containerWidth) / 2;
		final int l = (this.height - this.containerHeight) / 2;
		this.blit(k, l, 0, 0, this.containerWidth, this.containerHeight);
	}

	@Override
	protected void drawForeground(final int p_146979_1_, final int p_146979_2_) {
		final String name = StringUtils.t("block.techreborn.chunk_loader");
		this.font.draw(name, this.containerWidth / 2 - this.font.getStringWidth(name) / 2, 6,
			4210752);
		this.font.draw(StringUtils.t("container.inventory", new Object[0]), 8,
			this.containerHeight - 96 + 2, 4210752);
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		super.render(mouseX, mouseY, partialTicks);
		this.drawMouseoverTooltip(mouseX, mouseY);
	}

}

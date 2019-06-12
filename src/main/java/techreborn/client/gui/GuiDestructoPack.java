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
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.AbstractContainerScreen;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.container.Container;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.Identifier;

public class GuiDestructoPack extends AbstractContainerScreen {

	private static final Identifier texture = new Identifier("techreborn",
		"textures/gui/destructopack.png");

	public GuiDestructoPack(Container container) {
		super(container, MinecraftClient.getInstance().player.inventory, new TextComponent("techreborn.destructopack"));
		this.containerWidth = 176;
		this.containerHeight = 166;
	}

	@Override
	protected void drawBackground(float arg0, int arg1, int arg2) {
		this.renderBackground();
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bindTexture(texture);
		blit(left, top, 0, 0, 176, 166);
	}

	@Override
	protected void drawForeground(int arg0, int arg1) {
		String name = I18n.translate("item.techreborn.part.destructoPack.name");
		font.draw(name, containerWidth / 2 - font.getStringWidth(name) / 2, 5, 4210752);
		this.font.draw(I18n.translate("container.inventory"), 8, this.containerHeight - 96 + 2,
			4210752);
		super.drawForeground(arg0, arg1);
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		super.render(mouseX, mouseY, partialTicks);
		this.drawMouseoverTooltip(mouseX, mouseY);
	}
}

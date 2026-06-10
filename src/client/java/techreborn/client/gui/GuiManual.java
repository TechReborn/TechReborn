/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 TechReborn
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

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Util;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import reborncore.client.gui.Theme;
import reborncore.client.gui.ThemeManager;
import techreborn.config.TechRebornConfig;
import techreborn.packets.serverbound.RefundPayload;

public class GuiManual extends Screen {

	private static final Identifier MANUAL_TEXTURE = Identifier.fromNamespaceAndPath("techreborn", "textures/gui/manual.png");
	final int guiWidth = 207;
	final int guiHeight = 195;
	private static final Component text1 = Component.translatable("techreborn.manual.wiki");
	private static final Component text2 = Component.translatable("techreborn.manual.discord");
	private static final Component text3 = Component.translatable("techreborn.manual.refund");

	private final Theme theme;

	public GuiManual() {
		super(Component.literal("gui.manual"));
		this.theme = ThemeManager.getTheme();
	}

	@Override
	public void init() {
		super.init();
		int y = (height / 2) - guiHeight / 2;

		addRenderableWidget(
			Button.builder(Component.translatable("techreborn.manual.wikibtn"), button -> {
				openLink("https://wiki.techreborn.ovh");
			}).bounds((width / 2 - 30), y + 60, 60, 20).build()
		);

		addRenderableWidget(
			Button.builder(Component.translatable("techreborn.manual.discordbtn"), button -> {
				openLink("https://discord.gg/teamreborn");
			}).bounds((width / 2 - 30), y + 110, 60, 20).build()
		);

		if (TechRebornConfig.allowManualRefund.get()) {
			addRenderableWidget(
				Button.builder(Component.translatable("techreborn.manual.refundbtn"), button -> {
					ClientPlayNetworking.send(new RefundPayload());
					minecraft.setScreen(null);
				}).bounds((width / 2 - 30), y + 160, 60, 20).build()
			);
		}
	}

	private void openLink(String url) {
		minecraft.setScreen(new ConfirmLinkScreen(t -> {
			if (t) {
				Util.getPlatform().openUri(url);
			}
			this.minecraft.setScreen(this);
		}, url, false));
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor drawContext, int mouseX, int mouseY, float delta) {
		super.extractBackground(drawContext, mouseX, mouseY, delta);
		int centerX = (width / 2) - guiWidth / 2;
		int centerY = (height / 2) - guiHeight / 2;
		drawContext.blit(RenderPipelines.GUI_TEXTURED, MANUAL_TEXTURE, centerX, centerY, 0, 0, guiWidth, guiHeight, 256, 256);
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor drawContext, int mouseX, int mouseY, float partialTicks) {
		super.extractRenderState(drawContext, mouseX, mouseY, partialTicks);

		int centerY = (height / 2) - guiHeight / 2;

		drawContext.text(font, text1, (width / 2) - font.width(text1) / 2, centerY + 40, theme.titleColor().rgba(), false);
		drawContext.text(font, text2, (width / 2) - font.width(text2) / 2, centerY + 90, theme.titleColor().rgba(), false);
		if (TechRebornConfig.allowManualRefund.get()) {
			drawContext.text(font, text3, (width / 2) - font.width(text3) / 2, centerY + 140, theme.titleColor().rgba(), false);
		}
	}
}

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
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import reborncore.client.gui.Theme;
import reborncore.client.gui.ThemeManager;
import techreborn.config.TechRebornConfig;
import techreborn.packets.serverbound.RefundPayload;

public class GuiManual extends Screen {

	private static final Identifier MANUAL_TEXTURE = Identifier.of("techreborn", "textures/gui/manual.png");
	final int guiWidth = 207;
	final int guiHeight = 195;
	private static final Text text1 = Text.translatable("techreborn.manual.wiki");
	private static final Text text2 = Text.translatable("techreborn.manual.discord");
	private static final Text text3 = Text.translatable("techreborn.manual.refund");

	private final Theme theme;

	public GuiManual() {
		super(Text.literal("gui.manual"));
		this.theme = ThemeManager.getTheme();
	}

	@Override
	public void init() {
		super.init();
		int y = (height / 2) - guiHeight / 2;

		addDrawableChild(
			ButtonWidget.builder(Text.translatable("techreborn.manual.wikibtn"), button -> {
				openLink("https://wiki.techreborn.ovh");
			}).dimensions((width / 2 - 30), y + 60, 60, 20).build()
		);

		addDrawableChild(
			ButtonWidget.builder(Text.translatable("techreborn.manual.discordbtn"), button -> {
				openLink("https://discord.gg/teamreborn");
			}).dimensions((width / 2 - 30), y + 110, 60, 20).build()
		);

		if (TechRebornConfig.allowManualRefund) {
			addDrawableChild(
				ButtonWidget.builder(Text.translatable("techreborn.manual.refundbtn"), button -> {
					ClientPlayNetworking.send(new RefundPayload());
					client.setScreen(null);
				}).dimensions((width / 2 - 30), y + 160, 60, 20).build()
			);
		}
	}

	private void openLink(String url) {
		client.setScreen(new ConfirmLinkScreen(t -> {
			if (t) {
				Util.getOperatingSystem().open(url);
			}
			this.client.setScreen(this);
		}, url, false));
	}

	@Override
	public void renderBackground(DrawContext drawContext, int mouseX, int mouseY, float delta) {
		super.renderBackground(drawContext, mouseX, mouseY, delta);
		int centerX = (width / 2) - guiWidth / 2;
		int centerY = (height / 2) - guiHeight / 2;
		drawContext.drawTexture(MANUAL_TEXTURE, centerX, centerY, 0, 0, guiWidth, guiHeight);
	}

	@Override
	public void render(DrawContext drawContext, int mouseX, int mouseY, float partialTicks) {
		super.render(drawContext, mouseX, mouseY, partialTicks);

		int centerY = (height / 2) - guiHeight / 2;

		drawContext.drawText(textRenderer, text1, (width / 2) - textRenderer.getWidth(text1) / 2, centerY + 40, theme.titleColor().rgba(), false);
		drawContext.drawText(textRenderer, text2, (width / 2) - textRenderer.getWidth(text2) / 2, centerY + 90, theme.titleColor().rgba(), false);
		if (TechRebornConfig.allowManualRefund) {
			drawContext.drawText(textRenderer, text3, (width / 2) - textRenderer.getWidth(text3) / 2, centerY + 140, theme.titleColor().rgba(), false);
		}
	}
}

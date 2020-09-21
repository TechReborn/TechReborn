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

import net.minecraft.client.gui.screen.ConfirmChatLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import reborncore.client.gui.builder.widget.GuiButtonExtended;
import reborncore.common.network.NetworkManager;
import techreborn.config.TechRebornConfig;
import techreborn.items.ManualItem;
import techreborn.packets.ServerboundPackets;

public class GuiManual extends Screen {

	ManualItem manual;
	PlayerEntity player;

	private static final Identifier texture = new Identifier("techreborn", "textures/gui/manual.png");
	int guiWidth = 207;
	int guiHeight = 195;
	private static final Text text1 = new TranslatableText("techreborn.manual.wiki");
	private static final Text text2 = new TranslatableText("techreborn.manual.discord");
	private static final Text text3 = new TranslatableText("techreborn.manual.refund");

	public GuiManual(PlayerEntity player) {
		super(new LiteralText("gui.manual"));
		this.player = player;
	}

	@Override
	public void init() {
		int y = (height / 2) - guiHeight / 2;
		y += 40;
		addButton(new GuiButtonExtended((width / 2 - 30), y + 10, 60, 20, new TranslatableText("techreborn.manual.wikibtn"), var1 -> client.openScreen(new ConfirmChatLinkScreen(t -> {
			if (t) {
				Util.getOperatingSystem().open("http://wiki.techreborn.ovh");
			}
			this.client.openScreen(this);
		}, "http://wiki.techreborn.ovh", false))));
		addButton(new GuiButtonExtended((width / 2 - 30), y + 60, 60, 20, new TranslatableText("techreborn.manual.discordbtn"), var1 -> client.openScreen(new ConfirmChatLinkScreen(t -> {
			if (t) {
				Util.getOperatingSystem().open("https://discord.gg/teamreborn");
			}
			this.client.openScreen(this);
		}, "https://discord.gg/teamreborn", false))));
		if (TechRebornConfig.allowManualRefund) {
			addButton(new GuiButtonExtended((width / 2 - 30), y + 110, 60, 20, new TranslatableText("techreborn.manual.refundbtn"), var1 -> {
				NetworkManager.sendToServer(ServerboundPackets.createRefundPacket());
				this.client.openScreen(null);
			}));
		}
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		renderBackground(matrixStack);
		client.getTextureManager().bindTexture(GuiManual.texture);
		int centerX = (width / 2) - guiWidth / 2;
		int centerY = (height / 2) - guiHeight / 2;
		drawTexture(matrixStack, centerX, centerY, 0, 0, guiWidth, guiHeight);
		textRenderer.draw(matrixStack, text1, ((width / 2) - textRenderer.getWidth(text1) / 2), centerY + 40, 4210752);
		textRenderer.draw(matrixStack, text2, ((width / 2) - textRenderer.getWidth(text2) / 2), centerY + 90, 4210752);
		if (TechRebornConfig.allowManualRefund) {
			textRenderer.draw(matrixStack, text3, ((width / 2) - textRenderer.getWidth(text3) / 2), centerY + 140, 4210752);
		}
		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}
}



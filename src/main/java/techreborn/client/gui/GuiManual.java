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

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.Identifier;
import techreborn.items.ItemManual;


public class GuiManual extends Screen {

	ItemManual manual;
	PlayerEntity player;

	private static final Identifier texture = new Identifier("techreborn", "textures/gui/manual.png");
	int guiWidth = 207;
	int guiHeight = 195;
	private static final String text1 = I18n.translate("techreborn.manual.wiki");
	private static final String text2 = I18n.translate("techreborn.manual.discord");
	private static final String text3 = I18n.translate("techreborn.manual.refund");

	public GuiManual(PlayerEntity player) {
		super(new TextComponent("gui.manual"));
		this.player = player;
	}

	@Override
	public void init() {
		int y = height / 4;
		//		buttons.add(new GuiButtonExtended((width / 2 - 30), y + 10, 60, 20, I18n.translate("techreborn.manual.wikibtn")));
		//		buttons.add(new GuiButtonExtended((width / 2 - 30), y + 60, 60, 20, I18n.translate("techreborn.manual.discordbtn")));
		//		if(ItemManual.allowRefund){
		//			buttons.add(new GuiButtonExtended((width / 2 - 30), y + 110, 60, 20, I18n.translate("techreborn.manual.refundbtn")));
		//		}
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		renderBackground();
		minecraft.getTextureManager().bindTexture(GuiManual.texture);
		int centerX = (width / 2) - guiWidth / 2;
		int centerY = (height / 2) - guiHeight / 2;
		blit(centerX, centerY, 0, 0, guiWidth, guiHeight);
		int y = height / 4;
		font.draw(text1, ((width / 2) - font.getStringWidth(text1) / 2), height / 2 - (guiHeight / 4), 4210752);
		font.draw(text2, ((width / 2) - font.getStringWidth(text2) / 2), height / 2 + 5, 4210752);
		if (ItemManual.allowRefund) {
			font.draw(text3, ((width / 2) - font.getStringWidth(text3) / 2), y + 100, 4210752);
		}
		super.render(mouseX, mouseY, partialTicks);
	}
}

//	public void onClick(GuiButtonExtended button, Double mouseX, Double mouseY){
//		switch (button.id) {
//			case 1:
//				minecraft.openScreen(new ConfirmChatLinkScreen(this, "http://wiki.techreborn.ovh", 1, false));
//				break;
//			case 2:
//				this.minecraft.openScreen(new ConfirmChatLinkScreen(this, "https://discord.gg/teamreborn", 2, false));
//				break;
//			case 3:
//				minecraft.openScreen(null);
//				NetworkManager.sendToServer(ServerboundPackets.createRefundPacket());
//				break;
//		}
//	}
//
//	@Override
//	public void confirmResult(boolean result, int id) {
//		switch(id) {
//			case 1:
//				if(result == true) {
//					try {
//						Desktop.getDesktop().browse(new java.net.URI("http://wiki.techreborn.ovh"));
//					} catch (Exception e) {
//						System.err.print(e);
//					}
//				}else {
//					minecraft.openScreen(this);
//				}
//				break;
//			case 2:
//				if(result == true) {
//					try {
//						Desktop.getDesktop().browse(new java.net.URI("https://discord.gg/teamreborn"));
//					} catch (Exception e) {
//						System.err.print(e);
//					}
//				}else {
//					minecraft.openScreen(this);
//				}
//				break;
//		}
//		}
//	}



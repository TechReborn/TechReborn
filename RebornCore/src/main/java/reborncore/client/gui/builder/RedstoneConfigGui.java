/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2021 TeamReborn
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

package reborncore.client.gui.builder;

import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.Packet;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import reborncore.client.RenderUtil;
import reborncore.client.gui.guibuilder.GuiBuilder;
import reborncore.common.blockentity.RedstoneConfiguration;
import reborncore.common.network.IdentifiedPacket;
import reborncore.common.network.NetworkManager;
import reborncore.common.network.ServerBoundPackets;

import java.util.Locale;

public class RedstoneConfigGui {

	public static void draw(MatrixStack matrixStack, GuiBase<?> guiBase, int mouseX, int mouseY) {
		if (guiBase.getMachine() == null) return;
		RedstoneConfiguration configuration = guiBase.getMachine().getRedstoneConfiguration();
		GuiBuilder builder = guiBase.builder;
		ItemRenderer itemRenderer = guiBase.getMinecraft().getItemRenderer();

		int x = 10;
		int y = 100;

		int i = 0;
		int spread = configuration.getElements().size() == 3 ? 27 : 18;
		for (RedstoneConfiguration.Element element : configuration.getElements()) {
			itemRenderer.renderInGuiWithOverrides(element.getIcon(), x - 3, y + (i * spread) - 5);

			guiBase.getTextRenderer().draw(matrixStack, new TranslatableText("reborncore.gui.fluidconfig." + element.getName()), x + 15, y + (i * spread), -1);

			boolean hovered = withinBounds(guiBase, mouseX, mouseY, x + 92, y + (i * spread) - 2, 63, 15);
			int color = hovered ? 0xFF8b8b8b : 0x668b8b8b;
			RenderUtil.drawGradientRect(0, x + 91, y + (i * spread) - 2, x + 93 + 65, y + (i * spread) + 10, color, color);

			Text name = new TranslatableText("reborncore.gui.fluidconfig." + configuration.getState(element).name().toLowerCase(Locale.ROOT));
			guiBase.drawCentredText(matrixStack, name, y + (i * spread), -1, x + 37, GuiBase.Layer.FOREGROUND);
			//guiBase.getTextRenderer().drawWithShadow(name, x + 92, y + (i * spread), -1);
			i++;
		}

	}

	public static boolean mouseClicked(GuiBase<?> guiBase, double mouseX, double mouseY, int mouseButton) {
		if (guiBase.getMachine() == null) return false;
		RedstoneConfiguration configuration = guiBase.getMachine().getRedstoneConfiguration();

		int x = 10;
		int y = 100;

		int i = 0;
		int spread = configuration.getElements().size() == 3 ? 27 : 18;
		for (RedstoneConfiguration.Element element : configuration.getElements()) {
			if (withinBounds(guiBase, (int) mouseX, (int) mouseY, x + 91, y + (i * spread) - 2, 63, 15)) {
				RedstoneConfiguration.State currentState = configuration.getState(element);
				int ns = currentState.ordinal() + 1;
				if (ns >= RedstoneConfiguration.State.values().length) {
					ns = 0;
				}
				RedstoneConfiguration.State nextState = RedstoneConfiguration.State.values()[ns];
				IdentifiedPacket packet = ServerBoundPackets.createPacketSetRedstoneSate(guiBase.getMachine().getPos(), element, nextState);
				NetworkManager.sendToServer(packet);
				return true;
			}
			i++;
		}
		return false;
	}

	private static boolean withinBounds(GuiBase<?> guiBase, int mouseX, int mouseY, int x, int y, int width, int height) {
		mouseX -= guiBase.getGuiLeft();
		mouseY -= guiBase.getGuiTop();
		return (mouseX > x && mouseX < x + width) && (mouseY > y && mouseY < y + height);
	}

}

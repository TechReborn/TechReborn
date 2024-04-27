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

package reborncore.client.gui.config;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import reborncore.client.gui.GuiBase;
import reborncore.common.blockentity.RedstoneConfiguration;
import reborncore.common.network.IdentifiedPacket;
import reborncore.common.network.ServerBoundPackets;
import reborncore.common.network.serverbound.SetRedstoneStatePayload;

import java.util.Locale;

public class RedstoneConfigGui extends GuiTab {
	public RedstoneConfigGui(GuiBase<?> guiBase) {
		super(guiBase);
	}

	@Override
	public String name() {
		return "reborncore.gui.tooltip.config_redstone";
	}

	@Override
	public boolean enabled() {
		return true;
	}

	@Override
	public ItemStack stack() {
		return new ItemStack(Items.REDSTONE);
	}

	@Override
	public void draw(DrawContext drawContext, int mouseX, int mouseY) {
		if (guiBase.getMachine() == null) return;
		RedstoneConfiguration configuration = guiBase.getMachine().getRedstoneConfiguration();

		int x = 10;
		int y = 100;

		int i = 0;
		int spread = configuration.getElements().size() == 3 ? 27 : 18;
		for (RedstoneConfiguration.Element element : configuration.getElements()) {
			drawContext.drawItem(element.getIcon(), x - 3, y + (i * spread) - 5);
			drawContext.drawText(guiBase.getTextRenderer(), Text.translatable("reborncore.gui.fluidconfig." + element.getName()), x + 15, y + (i * spread), -1, false);

			boolean hovered = withinBounds(guiBase, mouseX, mouseY, x + 92, y + (i * spread) - 2, 63, 15);
			int color = hovered ? 0xFF8b8b8b : 0x668b8b8b;
			drawContext.fill(x + 91, y + (i * spread) - 2, x + 93 + 65, y + (i * spread) + 10, color);

			Text name = Text.translatable("reborncore.gui.fluidconfig." + configuration.getState(element).name().toLowerCase(Locale.ROOT));
			guiBase.drawCentredText(drawContext, name, y + (i * spread), -1, x + 37, GuiBase.Layer.FOREGROUND);
			i++;
		}
	}

	@Override
	public boolean click(double mouseX, double mouseY, int mouseButton) {
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
				ClientPlayNetworking.send(new SetRedstoneStatePayload(guiBase.getMachine().getPos(), element.getName(), nextState));
				return true;
			}
			i++;
		}
		return false;
	}

	@Override
	public boolean hideGuiElements() {
		return false;
	}

	private boolean withinBounds(GuiBase<?> guiBase, int mouseX, int mouseY, int x, int y, int width, int height) {
		mouseX -= guiBase.getGuiLeft();
		mouseY -= guiBase.getGuiTop();
		return (mouseX > x && mouseX < x + width) && (mouseY > y && mouseY < y + height);
	}
}

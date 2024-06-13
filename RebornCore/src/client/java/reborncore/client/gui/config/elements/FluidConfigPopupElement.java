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

package reborncore.client.gui.config.elements;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.Direction;
import reborncore.RebornCore;
import reborncore.client.gui.GuiBase;
import reborncore.client.gui.GuiSprites;
import reborncore.common.blockentity.FluidConfiguration;
import reborncore.common.network.ServerBoundPackets;
import reborncore.common.network.serverbound.FluidConfigSavePayload;
import reborncore.common.network.serverbound.FluidIoSavePayload;
import reborncore.common.util.Color;

public class FluidConfigPopupElement extends AbstractConfigPopupElement {
	ConfigFluidElement fluidElement;

	public FluidConfigPopupElement(int x, int y, ConfigFluidElement fluidElement) {
		super(x, y, GuiSprites.SLOT_CONFIG_POPUP);
		this.fluidElement = fluidElement;
	}

	@Override
	protected void cycleConfig(Direction side, GuiBase<?> guiBase) {
		FluidConfiguration.FluidConfig config = guiBase.getMachine().fluidConfiguration.getSideDetail(side);

		FluidConfiguration.ExtractConfig fluidIO = config.getIoConfig().getNext();
		FluidConfiguration.FluidConfig newConfig = new FluidConfiguration.FluidConfig(side, fluidIO);

		ClientPlayNetworking.send(new FluidConfigSavePayload(guiBase.be.getPos(), newConfig));
	}

	public void updateCheckBox(String type, GuiBase<?> guiBase) {
		FluidConfiguration configHolder = guiBase.getMachine().fluidConfiguration;
		boolean input = configHolder.autoInput();
		boolean output = configHolder.autoOutput();
		if (type.equalsIgnoreCase("input")) {
			input = !configHolder.autoInput();
		}
		if (type.equalsIgnoreCase("output")) {
			output = !configHolder.autoOutput();
		}

		ClientPlayNetworking.send(new FluidIoSavePayload(guiBase.be.getPos(), input, output));
	}

	@Override
	protected void drawSateColor(DrawContext drawContext, GuiBase<?> gui, Direction side, int inx, int iny) {
		iny += 4;
		int sx = inx + getX() + gui.getGuiLeft();
		int sy = iny + getY() + gui.getGuiTop();
		FluidConfiguration fluidConfiguration = gui.getMachine().fluidConfiguration;
		if (fluidConfiguration == null) {
			RebornCore.LOGGER.debug("Hmm, this isn't supposed to happen");
			return;
		}
		FluidConfiguration.FluidConfig fluidConfig = fluidConfiguration.getSideDetail(side);
		Color color = switch (fluidConfig.getIoConfig()) {
			case NONE -> new Color(0, 0, 0, 0);
			case INPUT -> new Color(0, 0, 255, 128);
			case OUTPUT -> new Color(255, 69, 0, 128);
			case ALL -> new Color(52, 255, 30, 128);
		};
		drawContext.fill(sx, sy, sx + 18, sy + 18, color.getColor());
	}
}

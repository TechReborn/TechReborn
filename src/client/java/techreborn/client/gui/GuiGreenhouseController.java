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

import reborncore.client.gui.GuiBase;
import reborncore.client.gui.widget.GuiButtonExtended;
import reborncore.common.screen.BuiltScreenHandler;
import techreborn.blockentity.machine.tier1.GreenhouseControllerBlockEntity;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class GuiGreenhouseController extends GuiBase<BuiltScreenHandler> {

	private final GreenhouseControllerBlockEntity blockEntity;

	public GuiGreenhouseController(int syncID, final Player player, final GreenhouseControllerBlockEntity blockEntity) {
		super(player, blockEntity, blockEntity.createScreenHandler(syncID, player));
		this.blockEntity = blockEntity;
	}

	@Override
	protected void renderBg(GuiGraphics drawContext, final float f, final int mouseX, final int mouseY) {
		super.renderBg(drawContext, f, mouseX, mouseY);
		final GuiBase.Layer layer = GuiBase.Layer.BACKGROUND;

		drawSlot(drawContext, 8, 72, layer);

		int gridYPos = 22;
		drawSlot(drawContext, 30, gridYPos, layer);
		drawSlot(drawContext, 48, gridYPos, layer);
		drawSlot(drawContext, 30, gridYPos + 18, layer);
		drawSlot(drawContext, 48, gridYPos + 18, layer);
		drawSlot(drawContext, 30, gridYPos + 36, layer);
		drawSlot(drawContext, 48, gridYPos + 36, layer);

		if (!blockEntity.isShapeValid()) {
			drawContext.blit(RenderPipelines.GUI_TEXTURED, ResourceLocation.fromNamespaceAndPath("techreborn", "textures/item/part/digital_display.png"), leftPos + 68, topPos + 22, 0, 0, 16, 16, 16, 16);
			if (isPointInRect(68, 22, 16, 16, mouseX, mouseY)) {
				List<Component> list = Arrays.stream(I18n.get("techreborn.tooltip.greenhouse.upgrade_available")
						.split("\\r?\\n"))
						.map(Component::literal)
						.collect(Collectors.toList());

				drawContext.setComponentTooltipForNextFrame(getFont(), list, mouseX, mouseY);
			}
		}

	}

	@Override
	protected void renderLabels(GuiGraphics drawContext, final int mouseX, final int mouseY) {
		super.renderLabels(drawContext, mouseX, mouseY);
		final GuiBase.Layer layer = GuiBase.Layer.FOREGROUND;

		addHologramButton(90, 24, 212, layer).clickHandler(this::onClick);
		builder.drawHologramButton(drawContext, this, 90, 24, mouseX, mouseY, layer);

		builder.drawMultiEnergyBar(drawContext, this, 9, 19, (int) blockEntity.getEnergy(), (int) blockEntity.getMaxStoredPower(), mouseX, mouseY, 0, layer);
	}

	public void onClick(GuiButtonExtended button, Double x, Double y) {
		blockEntity.renderMultiblock ^= !hideGuiElements();
	}
}

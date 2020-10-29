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

import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import reborncore.client.gui.builder.GuiBase;
import reborncore.client.gui.builder.widget.GuiButtonExtended;
import reborncore.client.screen.builder.BuiltScreenHandler;
import techreborn.blockentity.machine.tier1.GreenhouseControllerBlockEntity;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GuiGreenhouseController extends GuiBase<BuiltScreenHandler> {

	private final GreenhouseControllerBlockEntity blockEntity;

	public GuiGreenhouseController(int syncID, final PlayerEntity player, final GreenhouseControllerBlockEntity blockEntity) {
		super(player, blockEntity, blockEntity.createScreenHandler(syncID, player));
		this.blockEntity = blockEntity;
	}

	@Override
	protected void drawBackground(MatrixStack matrixStack, final float f, final int mouseX, final int mouseY) {
		super.drawBackground(matrixStack, f, mouseX, mouseY);
		final GuiBase.Layer layer = GuiBase.Layer.BACKGROUND;

		drawSlot(matrixStack, 8, 72, layer);

		int gridYPos = 22;
		drawSlot(matrixStack, 30, gridYPos, layer);
		drawSlot(matrixStack, 48, gridYPos, layer);
		drawSlot(matrixStack, 30, gridYPos + 18, layer);
		drawSlot(matrixStack, 48, gridYPos + 18, layer);
		drawSlot(matrixStack, 30, gridYPos + 36, layer);
		drawSlot(matrixStack, 48, gridYPos + 36, layer);

		if (!blockEntity.isMultiblockValid()) {
			getMinecraft().getTextureManager().bindTexture(new Identifier("techreborn", "textures/item/part/digital_display.png"));
			drawTexture(matrixStack, x + 68, y + 22, 0, 0, 16, 16, 16, 16);
			if (isPointInRect(68, 22, 16, 16, mouseX, mouseY)) {
				List<Text> list = Arrays.stream(I18n.translate("techreborn.tooltip.greenhouse.upgrade_available")
						.split("\\r?\\n"))
						.map(LiteralText::new)
						.collect(Collectors.toList());

				matrixStack.push();
				renderTooltip(matrixStack, list, mouseX, mouseY);
				matrixStack.pop();
			}
		}

	}

	@Override
	protected void drawForeground(MatrixStack matrixStack, final int mouseX, final int mouseY) {
		super.drawForeground(matrixStack, mouseX, mouseY);
		final GuiBase.Layer layer = GuiBase.Layer.FOREGROUND;

		addHologramButton(90, 24, 212, layer).clickHandler(this::onClick);
		builder.drawHologramButton(matrixStack, this, 90, 24, mouseX, mouseY, layer);

		if (!blockEntity.isMultiblockValid()) {
			if (isPointInRect(68, 22, 16, 16, mouseX, mouseY)) {
				List<Text> list = Arrays.stream(I18n.translate("techreborn.tooltip.greenhouse.upgrade_available")
						.split("\\r?\\n"))
						.map(LiteralText::new)
						.collect(Collectors.toList());

				matrixStack.push();
				renderTooltip(matrixStack, list, mouseX - getGuiLeft(), mouseY - getGuiTop());
				matrixStack.pop();
			}
		}

		builder.drawMultiEnergyBar(matrixStack, this, 9, 19, (int) blockEntity.getEnergy(), (int) blockEntity.getMaxStoredPower(), mouseX, mouseY, 0, layer);
	}

	public void onClick(GuiButtonExtended button, Double x, Double y) {
		blockEntity.renderMultiblock ^= !hideGuiElements();
	}
}
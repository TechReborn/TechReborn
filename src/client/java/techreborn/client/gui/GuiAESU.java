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

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import reborncore.client.ClientNetworkManager;
import reborncore.client.gui.GuiBase;
import reborncore.client.gui.widget.GuiButtonUpDown;
import reborncore.client.gui.widget.GuiButtonUpDown.UpDownButtonType;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.screen.BuiltScreenHandler;
import techreborn.blockentity.storage.energy.AdjustableSUBlockEntity;
import techreborn.packets.ServerboundPackets;

public class GuiAESU extends GuiBase<BuiltScreenHandler> {

	final AdjustableSUBlockEntity blockEntity;

	public GuiAESU(int syncID, final PlayerEntity player, final AdjustableSUBlockEntity aesu) {
		super(player, aesu, aesu.createScreenHandler(syncID, player));
		this.blockEntity = aesu;
	}

	@Override
	public void init() {
		super.init();
		addDrawableChild(new GuiButtonUpDown(x + 121, y + 79, this, b -> onClick(256), UpDownButtonType.FASTFORWARD));
		addDrawableChild(new GuiButtonUpDown(x + 121 + 12, y + 79, this, b -> onClick(64), UpDownButtonType.FORWARD));
		addDrawableChild(new GuiButtonUpDown(x + 121 + 24, y + 79, this, b -> onClick(-64), UpDownButtonType.REWIND));
		addDrawableChild(new GuiButtonUpDown(x + 121 + 36, y + 79, this, b -> onClick(-256), UpDownButtonType.FASTREWIND));
	}

	@Override
	protected void drawBackground(DrawContext drawContext, final float f, final int mouseX, final int mouseY) {
		super.drawBackground(drawContext, f, mouseX, mouseY);
		final Layer layer = Layer.BACKGROUND;

		this.drawSlot(drawContext, 62, 45, layer);
		this.drawSlot(drawContext, 98, 45, layer);
		this.drawArmourSlots(drawContext, 8, 18, layer);
		this.builder.drawEnergyOutput(drawContext, this, 155, 61, this.blockEntity.getCurrentOutput(), layer);
	}

	@Override
	protected void drawForeground(DrawContext drawContext, final int mouseX, final int mouseY) {
		super.drawForeground(drawContext, mouseX, mouseY);
		final Layer layer = Layer.FOREGROUND;

		if (!hideGuiElements()) {
			final MatrixStack matrices = drawContext.getMatrices();
			matrices.push();
			matrices.scale(0.6f, 0.6f, 1.0f);
			Text text = Text.literal(PowerSystem.getLocalizedPowerNoSuffix(blockEntity.getEnergy()))
					.append("/")
					.append(PowerSystem.getLocalizedPowerNoSuffix(blockEntity.getMaxStoredPower()))
					.append(" ")
					.append(PowerSystem.ABBREVIATION);

			drawCentredText(drawContext, text, 35, 0, 58, layer);
			matrices.pop();
		}

		builder.drawMultiEnergyBar(drawContext, this, 81, 28, (int) blockEntity.getEnergy(), (int) blockEntity.getMaxStoredPower(), mouseX, mouseY, 0, layer);
	}

	public void onClick(int amount) {
		ClientNetworkManager.sendToServer(ServerboundPackets.createPacketAesu(amount, hasShiftDown(), hasControlDown(), blockEntity));
	}
}

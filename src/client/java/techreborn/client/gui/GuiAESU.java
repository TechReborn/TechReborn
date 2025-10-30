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
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.joml.Matrix3x2fStack;
import reborncore.client.gui.GuiBase;
import reborncore.client.gui.widget.GuiButtonUpDown;
import reborncore.client.gui.widget.GuiButtonUpDown.UpDownButtonType;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.screen.BuiltScreenHandler;
import techreborn.blockentity.storage.energy.AdjustableSUBlockEntity;
import techreborn.packets.serverbound.AESUConfigPayload;

public class GuiAESU extends GuiBase<BuiltScreenHandler> {

	final AdjustableSUBlockEntity blockEntity;

	public GuiAESU(int syncID, final Player player, final AdjustableSUBlockEntity aesu) {
		super(player, aesu, aesu.createScreenHandler(syncID, player));
		this.blockEntity = aesu;
	}

	@Override
	public void init() {
		super.init();
		addRenderableWidget(new GuiButtonUpDown(leftPos + 121, topPos + 79, this, b -> onClick(256), UpDownButtonType.FASTFORWARD));
		addRenderableWidget(new GuiButtonUpDown(leftPos + 121 + 12, topPos + 79, this, b -> onClick(64), UpDownButtonType.FORWARD));
		addRenderableWidget(new GuiButtonUpDown(leftPos + 121 + 24, topPos + 79, this, b -> onClick(-64), UpDownButtonType.REWIND));
		addRenderableWidget(new GuiButtonUpDown(leftPos + 121 + 36, topPos + 79, this, b -> onClick(-256), UpDownButtonType.FASTREWIND));
	}

	@Override
	protected void renderBg(GuiGraphics drawContext, final float f, final int mouseX, final int mouseY) {
		super.renderBg(drawContext, f, mouseX, mouseY);
		final Layer layer = Layer.BACKGROUND;

		this.drawSlot(drawContext, 62, 45, layer);
		this.drawSlot(drawContext, 98, 45, layer);
		this.drawArmourSlots(drawContext, 8, 18, layer);
		this.builder.drawEnergyOutput(drawContext, this, 155, 61, this.blockEntity.getCurrentOutput(), layer);
	}

	@Override
	protected void renderLabels(GuiGraphics drawContext, final int mouseX, final int mouseY) {
		super.renderLabels(drawContext, mouseX, mouseY);
		final Layer layer = Layer.FOREGROUND;

		if (!hideGuiElements()) {
			final Matrix3x2fStack matrices = drawContext.pose();
			matrices.pushMatrix();
			matrices.scale(0.6f, 0.6f);
			Component text = Component.literal(PowerSystem.getLocalizedPowerNoSuffix(blockEntity.getEnergy()))
					.append("/")
					.append(PowerSystem.getLocalizedPowerNoSuffix(blockEntity.getMaxStoredPower()))
					.append(" ")
					.append(PowerSystem.ABBREVIATION);

			drawCentredText(drawContext, text, 35, 0xff000000, 58, layer);
			matrices.popMatrix();
		}

		builder.drawMultiEnergyBar(drawContext, this, 81, 28, (int) blockEntity.getEnergy(), (int) blockEntity.getMaxStoredPower(), mouseX, mouseY, 0, layer);
	}

	public void onClick(int amount) {
		ClientPlayNetworking.send(new AESUConfigPayload(blockEntity.getBlockPos(), amount, hasShiftDown(), hasControlDown()));
	}
}

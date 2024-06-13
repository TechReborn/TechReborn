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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import reborncore.client.gui.GuiBase;
import reborncore.client.gui.widget.GuiButtonUpDown;
import reborncore.common.screen.BuiltScreenHandler;
import techreborn.blockentity.machine.tier1.PlayerDetectorBlockEntity;
import techreborn.packets.serverbound.DetectorRadiusPayload;

public class GuiPlayerDetector extends GuiBase<BuiltScreenHandler> {

	final PlayerDetectorBlockEntity blockEntity;

	public GuiPlayerDetector(int syncID, final PlayerEntity player, final PlayerDetectorBlockEntity blockEntity) {
		super(player, blockEntity, blockEntity.createScreenHandler(syncID, player));
		this.blockEntity = blockEntity;
	}

	private void onClick(int amount) {
		ClientPlayNetworking.send(new DetectorRadiusPayload(blockEntity.getPos(), amount));
	}

	@Override
	public void init() {
		super.init();

		addDrawableChild(new GuiButtonUpDown(x + 64, y + 40, this, b -> onClick(16), GuiButtonUpDown.UpDownButtonType.FASTFORWARD));
		addDrawableChild(new GuiButtonUpDown(x + 64 + 12, y + 40, this, b -> onClick(1), GuiButtonUpDown.UpDownButtonType.FORWARD));
		addDrawableChild(new GuiButtonUpDown(x + 64 + 24, y + 40, this, b -> onClick(-1), GuiButtonUpDown.UpDownButtonType.REWIND));
		addDrawableChild(new GuiButtonUpDown(x + 64 + 36, y + 40, this, b -> onClick(-16), GuiButtonUpDown.UpDownButtonType.FASTREWIND));
	}

	@Override
	protected void drawBackground(DrawContext drawContext, float partialTicks, int mouseX, int mouseY) {
		super.drawBackground(drawContext, partialTicks, mouseX, mouseY);
		final Layer layer = Layer.BACKGROUND;

		if (hideGuiElements()) return;

		Text text = Text.literal("Radius: ").append(String.valueOf(blockEntity.getCurrentRadius()));
		drawCentredText(drawContext, text, 25, 4210752, layer);
	}

	@Override
	protected void drawForeground(DrawContext drawContext, final int mouseX, final int mouseY) {
		super.drawForeground(drawContext, mouseX, mouseY);
		final Layer layer = Layer.FOREGROUND;

		builder.drawMultiEnergyBar(drawContext, this, 9, 19, (int) blockEntity.getEnergy(), (int) blockEntity.getMaxStoredPower(), mouseX, mouseY, 0, layer);
	}

}

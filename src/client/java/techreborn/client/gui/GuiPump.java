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
import techreborn.blockentity.machine.tier2.PumpBlockEntity;
import techreborn.packets.ServerboundPackets;
import techreborn.packets.serverbound.PumpDepthPayload;
import techreborn.packets.serverbound.PumpRangePayload;

public class GuiPump extends GuiBase<BuiltScreenHandler> {

	private final PumpBlockEntity blockEntity;

	public GuiPump(int syncID, final PlayerEntity player, final PumpBlockEntity blockEntity) {
		super(player, blockEntity, blockEntity.createScreenHandler(syncID, player));
		this.blockEntity = blockEntity;
	}

	@Override
	public void init() {
		super.init();

		addDrawableChild(new GuiButtonUpDown(x + 84, y + 30, this, b -> onClickDepth(10), GuiButtonUpDown.UpDownButtonType.FASTFORWARD));
		addDrawableChild(new GuiButtonUpDown(x + 84 + 12, y + 30, this, b -> onClickDepth(1), GuiButtonUpDown.UpDownButtonType.FORWARD));
		addDrawableChild(new GuiButtonUpDown(x + 84 + 24, y + 30, this, b -> onClickDepth(-1), GuiButtonUpDown.UpDownButtonType.REWIND));
		addDrawableChild(new GuiButtonUpDown(x + 84 + 36, y + 30, this, b -> onClickDepth(-10), GuiButtonUpDown.UpDownButtonType.FASTREWIND));

		addDrawableChild(new GuiButtonUpDown(x + 84, y + 55, this, b -> onClick(10), GuiButtonUpDown.UpDownButtonType.FASTFORWARD));
		addDrawableChild(new GuiButtonUpDown(x + 84 + 12, y + 55, this, b -> onClick(1), GuiButtonUpDown.UpDownButtonType.FORWARD));
		addDrawableChild(new GuiButtonUpDown(x + 84 + 24, y + 55, this, b -> onClick(-1), GuiButtonUpDown.UpDownButtonType.REWIND));
		addDrawableChild(new GuiButtonUpDown(x + 84 + 36, y + 55, this, b -> onClick(-10), GuiButtonUpDown.UpDownButtonType.FASTREWIND));
	}

	private void onClickDepth(int amount) {
		ClientPlayNetworking.send(new PumpDepthPayload(blockEntity.getPos(), amount));
	}

	private void onClick(int amount) {
		ClientPlayNetworking.send(new PumpRangePayload(blockEntity.getPos(), amount));
	}

	@Override
	protected void drawBackground(DrawContext drawContext, final float partialTicks, final int mouseX, final int mouseY) {
		super.drawBackground(drawContext, partialTicks, mouseX, mouseY);
		final Layer layer = Layer.BACKGROUND;

		if (hideGuiElements()) return;

		drawSlot(drawContext, 8, 72, layer); // Battery slot

		drawText(drawContext,
			Text.translatable("gui.techreborn.pump.depth", Integer.toString(PumpBlockEntity.MIN_DEPTH), Integer.toString(PumpBlockEntity.MAX_DEPTH))
				.append(Integer.toString(blockEntity.getDepth())),
			80, 20, 0x404040, layer);
		drawText(drawContext,
			Text.translatable("gui.techreborn.pump.range", Integer.toString(PumpBlockEntity.MIN_RANGE), Integer.toString(PumpBlockEntity.MAX_RANGE))
				.append(Integer.toString(blockEntity.getRange())),
			80, 45, 0x404040, layer);

		if (blockEntity.getExhausted()) {
			drawText(drawContext, Text.translatable("gui.techreborn.pump.exhausted"), 80, 75, 0x800000, layer);
		}
	}

	@Override
	protected void drawForeground(DrawContext drawContext, final int mouseX, final int mouseY) {
		super.drawForeground(drawContext, mouseX, mouseY);
		final Layer layer = Layer.FOREGROUND;

		builder.drawTank(drawContext, this, 33, 25, mouseX, mouseY, blockEntity.getTank().getFluidInstance(), blockEntity.getTank().getFluidValueCapacity(), blockEntity.getTank().isEmpty(), layer);
		builder.drawMultiEnergyBar(drawContext, this, 9, 19, (int) blockEntity.getEnergy(), (int) blockEntity.getMaxStoredPower(), mouseX, mouseY, 0, layer);
	}
}

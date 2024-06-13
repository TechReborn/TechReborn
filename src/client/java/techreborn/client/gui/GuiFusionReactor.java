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
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.apache.commons.lang3.tuple.Pair;
import reborncore.client.gui.GuiBase;
import reborncore.client.gui.GuiBuilder;
import reborncore.client.gui.widget.GuiButtonExtended;
import reborncore.client.gui.widget.GuiButtonUpDown;
import reborncore.client.gui.widget.GuiButtonUpDown.UpDownButtonType;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.util.Color;
import reborncore.common.util.Torus;
import techreborn.blockentity.machine.multiblock.FusionControlComputerBlockEntity;
import techreborn.packets.ServerboundPackets;
import techreborn.packets.serverbound.FusionControlSizePayload;

import java.util.Optional;

public class GuiFusionReactor extends GuiBase<BuiltScreenHandler> {

	private final FusionControlComputerBlockEntity blockEntity;

	public GuiFusionReactor(int syncID, final PlayerEntity player, final FusionControlComputerBlockEntity blockEntity) {
		super(player, blockEntity, blockEntity.createScreenHandler(syncID, player));
		this.blockEntity = blockEntity;
	}

	@Override
	public void init() {
		super.init();
		addDrawableChild(new GuiButtonUpDown(x + 121, y + 79, this, (ButtonWidget buttonWidget) -> sendSizeChange(5), UpDownButtonType.FASTFORWARD));
		addDrawableChild(new GuiButtonUpDown(x + 121 + 12, y + 79, this, (ButtonWidget buttonWidget) -> sendSizeChange(1), UpDownButtonType.FORWARD));
		addDrawableChild(new GuiButtonUpDown(x + 121 + 24, y + 79, this, (ButtonWidget buttonWidget) -> sendSizeChange(-1), UpDownButtonType.REWIND));
		addDrawableChild(new GuiButtonUpDown(x + 121 + 36, y + 79, this, (ButtonWidget buttonWidget) -> sendSizeChange(-5), UpDownButtonType.FASTREWIND));
	}

	@Override
	protected void drawBackground(DrawContext drawContext, final float partialTicks, final int mouseX, final int mouseY) {
		super.drawBackground(drawContext, partialTicks, mouseX, mouseY);
		final GuiBase.Layer layer = GuiBase.Layer.BACKGROUND;

		drawSlot(drawContext, 34, 47, layer);
		drawSlot(drawContext, 126, 47, layer);
		drawOutputSlot(drawContext, 80, 47, layer);

		if (blockEntity.isMultiblockValid()) {
			builder.drawHologramButton(drawContext, this, 6, 4, mouseX, mouseY, layer);
		}

	}

	@Override
	protected void drawForeground(DrawContext drawContext, final int mouseX, final int mouseY) {
		super.drawForeground(drawContext, mouseX, mouseY);
		final GuiBase.Layer layer = GuiBase.Layer.FOREGROUND;

		builder.drawProgressBar(drawContext, this, blockEntity.getProgressScaled(100), 100, 55, 51, mouseX, mouseY, GuiBuilder.ProgressDirection.RIGHT, layer);
		builder.drawProgressBar(drawContext, this, blockEntity.getProgressScaled(100), 100, 105, 51, mouseX, mouseY, GuiBuilder.ProgressDirection.LEFT, layer);
		if (blockEntity.isMultiblockValid()) {
			addHologramButton(6, 4, 212, layer).clickHandler(this::hologramToggle);
			drawCentredText(drawContext, blockEntity.getStateText(), 20, Color.BLUE.darker().getColor(), layer);
			if (blockEntity.state == 2) {
				drawCentredText(drawContext, Text.literal(PowerSystem.getLocalizedPower(blockEntity.getPowerChange())).append("/t"), 30, Color.GREEN.darker().getColor(), layer);
			}
		} else {
			builder.drawMultiblockMissingBar(drawContext, this, layer);
			addHologramButton(76, 56, 212, layer).clickHandler(this::hologramToggle);
			builder.drawHologramButton(drawContext, this, 76, 56, mouseX, mouseY, layer);

			Optional<Pair<Integer, Integer>> stackSize = getCoilStackCount();
			if (stackSize.isPresent()) {
				if (stackSize.get().getLeft() > 0) {

					drawCentredText(drawContext,
							Text.literal("Required Coils: ")
									.append(String.valueOf(stackSize.get().getLeft()))
									.append("x64 +")
									.append(String.valueOf(stackSize.get().getRight()))
							, 25, 0xFFFFFF, layer);
				} else {
					drawCentredText(drawContext, Text.literal("Required Coils: ").append(String.valueOf(stackSize.get().getRight())), 25, 0xFFFFFF, layer);
				}

			}
		}
		drawContext.drawText(this.textRenderer, Text.literal("Size: ").append(String.valueOf(blockEntity.size)), 83, 81, 0xFFFFFF, true);
		drawContext.drawText(this.textRenderer, Text.literal(String.valueOf(blockEntity.getPowerMultiplier())).append("x"), 10, 81, 0xFFFFFF, true);

		builder.drawMultiEnergyBar(drawContext, this, 9, 19, this.blockEntity.getEnergy(), this.blockEntity.getMaxStoredPower(), mouseX, mouseY, 0, layer);
	}

	public void hologramToggle(GuiButtonExtended button, double x, double y) {
		blockEntity.renderMultiblock ^= !hideGuiElements();
	}

	private void sendSizeChange(int sizeDelta) {
		ClientPlayNetworking.send(new FusionControlSizePayload(blockEntity.getPos(), sizeDelta));
	}

	public Optional<Pair<Integer, Integer>> getCoilStackCount() {
		if (!Torus.getTorusSizeCache().containsKey(blockEntity.size)) {
			return Optional.empty();
		}
		int count = Torus.getTorusSizeCache().get(blockEntity.size);
		return Optional.of(Pair.of(count / 64, count % 64));
	}
}

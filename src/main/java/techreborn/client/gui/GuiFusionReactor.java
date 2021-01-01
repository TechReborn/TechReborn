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

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import org.apache.commons.lang3.tuple.Pair;
import reborncore.client.gui.builder.GuiBase;
import reborncore.client.gui.builder.widget.GuiButtonExtended;
import reborncore.client.gui.builder.widget.GuiButtonUpDown;
import reborncore.client.gui.builder.widget.GuiButtonUpDown.UpDownButtonType;
import reborncore.client.gui.guibuilder.GuiBuilder;
import reborncore.client.screen.builder.BuiltScreenHandler;
import reborncore.common.network.NetworkManager;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.util.Color;
import reborncore.common.util.Torus;
import techreborn.blockentity.machine.multiblock.FusionControlComputerBlockEntity;
import techreborn.packets.ServerboundPackets;

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
		addButton(new GuiButtonUpDown(x + 121, y + 79, this, (ButtonWidget buttonWidget) -> sendSizeChange(5), UpDownButtonType.FASTFORWARD));
		addButton(new GuiButtonUpDown(x + 121 + 12, y + 79, this, (ButtonWidget buttonWidget) -> sendSizeChange(1), UpDownButtonType.FORWARD));
		addButton(new GuiButtonUpDown(x + 121 + 24, y + 79, this, (ButtonWidget buttonWidget) -> sendSizeChange(-5), UpDownButtonType.REWIND));
		addButton(new GuiButtonUpDown(x + 121 + 36, y + 79, this, (ButtonWidget buttonWidget) -> sendSizeChange(-1), UpDownButtonType.FASTREWIND));
	}

	@Override
	protected void drawBackground(MatrixStack matrixStack, final float partialTicks, final int mouseX, final int mouseY) {
		super.drawBackground(matrixStack, partialTicks, mouseX, mouseY);
		final GuiBase.Layer layer = GuiBase.Layer.BACKGROUND;

		drawSlot(matrixStack, 34, 47, layer);
		drawSlot(matrixStack, 126, 47, layer);
		drawOutputSlot(matrixStack, 80, 47, layer);

		builder.drawJEIButton(matrixStack, this, 158, 5, layer);
		if (blockEntity.isMultiblockValid()) {
			builder.drawHologramButton(matrixStack, this, 6, 4, mouseX, mouseY, layer);
		}

	}

	@Override
	protected void drawForeground(MatrixStack matrixStack, final int mouseX, final int mouseY) {
		super.drawForeground(matrixStack, mouseX, mouseY);
		final GuiBase.Layer layer = GuiBase.Layer.FOREGROUND;

		builder.drawProgressBar(matrixStack, this, blockEntity.getProgressScaled(100), 100, 55, 51, mouseX, mouseY, GuiBuilder.ProgressDirection.RIGHT, layer);
		builder.drawProgressBar(matrixStack, this, blockEntity.getProgressScaled(100), 100, 105, 51, mouseX, mouseY, GuiBuilder.ProgressDirection.LEFT, layer);
		if (blockEntity.isMultiblockValid()) {
			addHologramButton(6, 4, 212, layer).clickHandler(this::hologramToggle);
			drawCentredText(matrixStack, blockEntity.getStateText(), 20, Color.BLUE.darker().getColor(), layer);
			if (blockEntity.state == 2) {
				drawCentredText(matrixStack, new LiteralText(PowerSystem.getLocalizedPower(blockEntity.getPowerChange())).append("/t"), 30, Color.GREEN.darker().getColor(), layer);
			}
		} else {
			builder.drawMultiblockMissingBar(matrixStack, this, layer);
			addHologramButton(76, 56, 212, layer).clickHandler(this::hologramToggle);
			builder.drawHologramButton(matrixStack, this, 76, 56, mouseX, mouseY, layer);

			Optional<Pair<Integer, Integer>> stackSize = getCoilStackCount();
			if (stackSize.isPresent()) {
				if (stackSize.get().getLeft() > 0) {

					drawCentredText(matrixStack,
							new LiteralText("Required Coils: ")
									.append(String.valueOf(stackSize.get().getLeft()))
									.append("x64 +")
									.append(String.valueOf(stackSize.get().getRight()))
							, 25, 0xFFFFFF, layer);
				} else {
					drawCentredText(matrixStack, new LiteralText("Required Coils: ").append(String.valueOf(stackSize.get().getRight())), 25, 0xFFFFFF, layer);
				}

			}
		}
		drawTextWithShadow(matrixStack, client.textRenderer, new LiteralText("Size: ").append(String.valueOf(blockEntity.size)), 83, 81, 0xFFFFFF);
		drawTextWithShadow(matrixStack, client.textRenderer, new LiteralText(String.valueOf(blockEntity.getPowerMultiplier())).append("x"), 10, 81, 0xFFFFFF);

		builder.drawMultiEnergyBar(matrixStack, this, 9, 19, (int) this.blockEntity.getEnergy(), (int) this.blockEntity.getMaxStoredPower(), mouseX, mouseY, 0, layer);
	}

	public void hologramToggle(GuiButtonExtended button, double x, double y) {
		blockEntity.renderMultiblock ^= !hideGuiElements();
	}

	private void sendSizeChange(int sizeDelta) {
		NetworkManager.sendToServer(ServerboundPackets.createPacketFusionControlSize(sizeDelta, blockEntity.getPos()));
	}

	public Optional<Pair<Integer, Integer>> getCoilStackCount() {
		if (!Torus.getTorusSizeCache().containsKey(blockEntity.size)) {
			return Optional.empty();
		}
		int count = Torus.getTorusSizeCache().get(blockEntity.size);
		return Optional.of(Pair.of(count / 64, count % 64));
	}
}

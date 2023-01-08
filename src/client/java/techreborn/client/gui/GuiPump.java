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

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import reborncore.client.ClientNetworkManager;
import reborncore.client.gui.builder.GuiBase;
import reborncore.client.gui.builder.widget.GuiButtonUpDown;
import reborncore.common.screen.BuiltScreenHandler;
import techreborn.blockentity.machine.tier2.PumpBlockEntity;
import techreborn.packets.ServerboundPackets;

public class GuiPump extends GuiBase<BuiltScreenHandler> {

	static final String DEPTH_LABEL = "Depth (" + PumpBlockEntity.MIN_DEPTH + ".." + PumpBlockEntity.MAX_DEPTH + "):";
	static final String RANGE_LABEL = "Range (" + PumpBlockEntity.MIN_RANGE + ".." + PumpBlockEntity.MAX_RANGE + "):";
	private final PumpBlockEntity blockEntity;

	public GuiPump(int syncID, final PlayerEntity player, final PumpBlockEntity blockEntity) {
		super(player, blockEntity, blockEntity.createScreenHandler(syncID, player));
		this.blockEntity = blockEntity;
	}

	@Override
	public void init() {
		super.init();

		addDrawableChild(new GuiButtonUpDown(x + 84, y + 35, this, b -> onClickDepth(10), GuiButtonUpDown.UpDownButtonType.FASTFORWARD));
		addDrawableChild(new GuiButtonUpDown(x + 84 + 12, y + 35, this, b -> onClickDepth(1), GuiButtonUpDown.UpDownButtonType.FORWARD));
		addDrawableChild(new GuiButtonUpDown(x + 84 + 24, y + 35, this, b -> onClickDepth(-1), GuiButtonUpDown.UpDownButtonType.REWIND));
		addDrawableChild(new GuiButtonUpDown(x + 84 + 36, y + 35, this, b -> onClickDepth(-10), GuiButtonUpDown.UpDownButtonType.FASTREWIND));

		addDrawableChild(new GuiButtonUpDown(x + 84, y + 65, this, b -> onClick(10), GuiButtonUpDown.UpDownButtonType.FASTFORWARD));
		addDrawableChild(new GuiButtonUpDown(x + 84 + 12, y + 65, this, b -> onClick(1), GuiButtonUpDown.UpDownButtonType.FORWARD));
		addDrawableChild(new GuiButtonUpDown(x + 84 + 24, y + 65, this, b -> onClick(-1), GuiButtonUpDown.UpDownButtonType.REWIND));
		addDrawableChild(new GuiButtonUpDown(x + 84 + 36, y + 65, this, b -> onClick(-10), GuiButtonUpDown.UpDownButtonType.FASTREWIND));
	}

	private void onClickDepth(int amount) {
		ClientNetworkManager.sendToServer(ServerboundPackets.createPacketPumpDepth(amount, blockEntity));
	}

	private void onClick(int amount) {
		ClientNetworkManager.sendToServer(ServerboundPackets.createPacketPumpRange(amount, blockEntity));
	}

	@Override
	protected void drawBackground(MatrixStack matrixStack, final float partialTicks, final int mouseX, final int mouseY) {
		super.drawBackground(matrixStack, partialTicks, mouseX, mouseY);
		final Layer layer = Layer.BACKGROUND;

		if (hideGuiElements()) return;

		drawSlot(matrixStack, 8, 72, layer); // Battery slot

		Text depthText = Text.literal(DEPTH_LABEL).append(Integer.toString(blockEntity.getDepth()));
		Text rangeText = Text.literal(RANGE_LABEL).append(Integer.toString(blockEntity.getRange()));
		drawText(matrixStack, depthText, 80, 25, 4210752, layer);
		drawText(matrixStack, rangeText, 80, 55, 4210752, layer);
	}

	@Override
	protected void drawForeground(MatrixStack matrixStack, final int mouseX, final int mouseY) {
		super.drawForeground(matrixStack, mouseX, mouseY);
		final Layer layer = Layer.FOREGROUND;

		builder.drawTank(matrixStack, this, 33, 25, mouseX, mouseY, blockEntity.getTank().getFluidInstance(), blockEntity.getTank().getFluidValueCapacity(), blockEntity.getTank().isEmpty(), layer);
		builder.drawMultiEnergyBar(matrixStack, this, 9, 19, (int) blockEntity.getEnergy(), (int) blockEntity.getMaxStoredPower(), mouseX, mouseY, 0, layer);
	}
}

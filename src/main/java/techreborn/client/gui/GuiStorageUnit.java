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
import net.minecraft.text.TranslatableText;
import reborncore.client.gui.builder.GuiBase;
import reborncore.client.screen.builder.BuiltScreenHandler;
import reborncore.common.network.NetworkManager;
import techreborn.blockentity.storage.item.StorageUnitBaseBlockEntity;
import techreborn.packets.ServerboundPackets;

public class GuiStorageUnit extends GuiBase<BuiltScreenHandler> {

	StorageUnitBaseBlockEntity storageEntity;

	public GuiStorageUnit(int syncID, final PlayerEntity player, final StorageUnitBaseBlockEntity storageEntity) {
		super(player, storageEntity, storageEntity.createScreenHandler(syncID, player));
		this.storageEntity = storageEntity;
	}

	@Override
	protected void drawBackground(MatrixStack matrixStack, final float f, final int mouseX, final int mouseY) {
		super.drawBackground(matrixStack, f, mouseX, mouseY);
		final Layer layer = Layer.BACKGROUND;

		// Draw slots
		drawSlot(matrixStack, 100, 53, layer);
		drawSlot(matrixStack, 140, 53, layer);

		builder.drawLockButton(matrixStack, this, 150, 4, mouseX, mouseY, layer, storageEntity.isLocked());
	}

	@Override
	protected void drawForeground(MatrixStack matrixStack, final int mouseX, final int mouseY) {
		super.drawForeground(matrixStack, mouseX, mouseY);

		// Draw in/out labels
		builder.drawText(matrixStack, this, new TranslatableText("gui.techreborn.unit.in"), 100, 43, 4210752);
		builder.drawText(matrixStack, this, new TranslatableText("gui.techreborn.unit.out"), 140, 43, 4210752);


		int storedAmount = storageEntity.storedAmount;

		if (storedAmount == 0 && !storageEntity.isLocked()) {
			textRenderer.draw(matrixStack, new TranslatableText("techreborn.tooltip.unit.empty"), 10, 20, 4210752);
		} else {
			textRenderer.draw(matrixStack, new TranslatableText("gui.techreborn.storage.store"), 10, 20, 4210752);
			textRenderer.draw(matrixStack, storageEntity.getDisplayedStack().getName(), 10, 30, 4210752);

			textRenderer.draw(matrixStack, new TranslatableText("gui.techreborn.storage.amount"), 10, 50, 4210752);
			textRenderer.draw(matrixStack, String.valueOf(storedAmount), 10, 60, 4210752);

			String percentFilled = String.valueOf((int) ((double) storedAmount / (double) storageEntity.getMaxCapacity() * 100));

			textRenderer.draw(matrixStack, new TranslatableText("gui.techreborn.unit.used").append(percentFilled + "%"), 10, 70, 4210752);

			textRenderer.draw(matrixStack, new TranslatableText("gui.techreborn.unit.wrenchtip"), 10, 80, 16711680);
		}
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		if (isPointInRect(150, 4, 20, 12, mouseX, mouseY) && storageEntity.canModifyLocking()) {
			NetworkManager.sendToServer(ServerboundPackets.createPacketStorageUnitLock(storageEntity, !storageEntity.isLocked()));
			return true;
		}
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}
}

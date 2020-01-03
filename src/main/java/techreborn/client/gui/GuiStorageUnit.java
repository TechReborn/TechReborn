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

import net.minecraft.entity.player.PlayerEntity;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.gui.builder.GuiBase;
import reborncore.common.util.StringUtils;
import techreborn.blockentity.storage.item.StorageUnitBaseBlockEntity;

public class GuiStorageUnit extends GuiBase<BuiltContainer> {

	StorageUnitBaseBlockEntity storageEntity;

	public GuiStorageUnit(int syncID, final PlayerEntity player, final StorageUnitBaseBlockEntity storageEntity) {
		super(player, storageEntity, storageEntity.createContainer(syncID, player));
		this.storageEntity = storageEntity;
	}

	@Override
	protected void drawBackground(final float f, final int mouseX, final int mouseY) {
		super.drawBackground(f, mouseX, mouseY);
		final Layer layer = Layer.BACKGROUND;

		drawString(StringUtils.t("gui.techreborn.unit.in"), 100, 43, 4210752, layer);
		drawSlot(100, 53, layer);

		drawString(StringUtils.t("gui.techreborn.unit.out"), 140, 43, 4210752, layer);
		drawSlot(140, 53, layer);
	}

	@Override
	protected void drawForeground(final int mouseX, final int mouseY) {
		super.drawForeground(mouseX, mouseY);

		if (storageEntity.isEmpty()) {
			font.draw(StringUtils.t("techreborn.tooltip.unit.empty"), 10, 20, 4210752);
		} else {
			font.draw(StringUtils.t("gui.techreborn.storage.store"), 10, 20, 4210752);
			font.draw(storageEntity.getStoredStack().getName().asString(), 10, 30, 4210752);


			font.draw(StringUtils.t("gui.techreborn.storage.amount"), 10, 50, 4210752);
			font.draw(String.valueOf(storageEntity.getCurrentCapacity()), 10, 60, 4210752);

			String percentFilled = String.valueOf((int) ((double) storageEntity.getCurrentCapacity() / (double) storageEntity.getMaxCapacity() * 100));

			font.draw(StringUtils.t("gui.techreborn.unit.used") + percentFilled + "%", 10, 70, 4210752);

			font.draw(StringUtils.t("gui.techreborn.unit.wrenchtip"), 10, 80, 16711680);
		}
	}
}

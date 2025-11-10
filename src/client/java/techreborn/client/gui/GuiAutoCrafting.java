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
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import reborncore.client.gui.GuiBase;
import reborncore.client.gui.GuiBuilder;
import reborncore.common.screen.BuiltScreenHandler;
import techreborn.blockentity.machine.tier1.AutoCraftingTableBlockEntity;
import techreborn.packets.serverbound.AutoCraftingLockPayload;

/**
 * Created by modmuss50 on 20/06/2017.
 */
public class GuiAutoCrafting extends GuiBase<BuiltScreenHandler> {
	final AutoCraftingTableBlockEntity blockEntityAutoCraftingTable;

	public GuiAutoCrafting(int syncID, Player player, AutoCraftingTableBlockEntity blockEntity) {
		super(player, blockEntity, blockEntity.createScreenHandler(syncID, player));
		this.blockEntityAutoCraftingTable = blockEntity;
	}

	@Override
	protected void renderLabels(GuiGraphics drawContext, int mouseX, int mouseY) {
		super.renderLabels(drawContext, mouseX, mouseY);
		final Layer layer = Layer.FOREGROUND;

		builder.drawProgressBar(drawContext, this, blockEntityAutoCraftingTable.getProgress(), blockEntityAutoCraftingTable.getMaxProgress(), 120, 44, mouseX, mouseY, GuiBuilder.ProgressDirection.RIGHT, layer);
		builder.drawMultiEnergyBar(drawContext, this, 9, 26, (int) blockEntityAutoCraftingTable.getEnergy(), (int) blockEntityAutoCraftingTable.getMaxStoredPower(), mouseX, mouseY, 0, layer);
	}

	@Override
	protected void renderBg(GuiGraphics drawContext, final float f, int mouseX, int mouseY) {
		super.renderBg(drawContext, f, mouseX, mouseY);
		final Layer layer = Layer.BACKGROUND;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				drawSlot(drawContext, 28 + (i * 18), 25 + (j * 18), layer);
			}
		}
		drawOutputSlot(drawContext, 95, 42, layer);
		drawOutputSlot(drawContext, 145, 42, layer);
		drawOutputSlot(drawContext, 145, 70, layer);

		ItemStack result = blockEntityAutoCraftingTable.getOutputPreview();
		if (result != null) {
			int x = 95 + getGuiLeft();
			int y = 42 + getGuiTop();
			drawContext.renderItem(result, x, y);
			drawContext.renderItemDecorations(getFont(), result, x, y, null);
		}

		builder.drawLockButton(drawContext, this, 145, 4, mouseX, mouseY, layer, blockEntityAutoCraftingTable.locked);
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent mouse, boolean doubled) {
		if (isPointInRect(145, 4, 20, 12, mouse.x(), mouse.y())) {
			ClientPlayNetworking.send(new AutoCraftingLockPayload(blockEntityAutoCraftingTable.getBlockPos(), !blockEntityAutoCraftingTable.locked));
			return true;
		}
		return super.mouseClicked(mouse, doubled);
	}
}

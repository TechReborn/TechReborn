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
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapState;
import net.minecraft.text.Text;
import reborncore.client.gui.GuiBase;
import reborncore.common.screen.BuiltScreenHandler;
import techreborn.blockentity.machine.tier3.QuantumCartographerBlockEntity;

public class GuiQuantumCartographer extends GuiBase<BuiltScreenHandler> {
	private final QuantumCartographerBlockEntity cartographerBlockEntity;

	public GuiQuantumCartographer(int syncID, final PlayerEntity player, final QuantumCartographerBlockEntity blockEntity) {
		super(player, blockEntity, blockEntity.createScreenHandler(syncID, player));
		this.cartographerBlockEntity = blockEntity;
	}

	@Override
	public void init() {
		super.init();

		ButtonWidget scan = ButtonWidget.builder(Text.literal("Scan"), new ButtonWidget.PressAction() {
			@Override
			public void onPress(ButtonWidget button) {

			}
		})
			.width(50)
			.position(x + 150, y + 60)
			.build();

		addSelectableChild(scan);
	}

	@Override
	protected void drawBackground(DrawContext drawContext, float lastFrameDuration, int mouseX, int mouseY) {
		super.drawBackground(drawContext, lastFrameDuration, mouseX, mouseY);
		final Layer layer = Layer.BACKGROUND;

		drawSlot(drawContext, 8, 72, layer);

		final ItemStack stack = cartographerBlockEntity.getInventory().getStack(QuantumCartographerBlockEntity.MAP_SLOT);

		if (!QuantumCartographerBlockEntity.isValidMapStack(stack)) {
			return;
		}

		Integer mapId = FilledMapItem.getMapId(stack);
		MapState mapState = FilledMapItem.getMapState(mapId, this.client.world);

		if (mapId == null || mapState == null) {
			return;
		}


		int mapSize = (1 << mapState.scale) * 128;
		int mapX = x + 92;
		int mapY = y + 15;
		int mapDisplaySize = 75;
		int mapScaledSize = mapSize / mapDisplaySize;
		drawContext.fill(mapX + 1, mapY + 1, mapX + mapDisplaySize + 1, mapY + mapDisplaySize + 1, 0xFFFFFFFF);
		drawMap(drawContext, mapId, mapState, mapX, mapY, 0.595F);

		drawContext.getMatrices().push();
		drawContext.getMatrices().translate(x + 28, y + 20, 1F);
		drawContext.getMatrices().scale(0.75F, 0.75F, 1F);
		int textColor = 4210752;

		drawContext.drawText(textRenderer, "Size: %dx%d".formatted(mapSize, mapSize), 0, 0, textColor, false);
		drawContext.drawText(textRenderer, "Scan: %dx%d".formatted(cartographerBlockEntity.getScanOffsetX(), cartographerBlockEntity.getScanOffsetZ()), 0, 10, textColor, false);
		drawContext.drawText(textRenderer, "Power: %d/t".formatted(QuantumCartographerBlockEntity.powerUsage(mapState)), 0, 20, textColor, false);

		drawContext.getMatrices().pop();

	}

	@Override
	protected void drawForeground(DrawContext drawContext, final int mouseX, final int mouseY) {
		super.drawForeground(drawContext, mouseX, mouseY);
		final Layer layer = Layer.FOREGROUND;
		builder.drawMultiEnergyBar(drawContext, this, 9, 19, (int) cartographerBlockEntity.getEnergy(), (int) cartographerBlockEntity.getMaxStoredPower(), mouseX, mouseY, 0, layer);
	}

	private void drawMap(DrawContext context, int mapId, MapState mapState, int x, int y, float scale) {
		context.getMatrices().push();
		context.getMatrices().translate(x, y, 1.0F);
		context.getMatrices().scale(scale, scale, 1.0F);
		this.client.gameRenderer.getMapRenderer().draw(context.getMatrices(), context.getVertexConsumers(), mapId, mapState, true, 15728880);
		context.draw();
		context.getMatrices().pop();
	}
}

/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2026 TechReborn
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

import java.util.List;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.entity.player.Player;

import reborncore.client.gui.GuiBase;
import reborncore.client.gui.config.GuiTab;
import reborncore.client.gui.widget.GuiButtonExtended;
import reborncore.common.screen.BuiltScreenHandler;
import techreborn.blockentity.generator.nuclear.NuclearReactorBlockEntity;

/**
 * GUI screen for the Nuclear Reactor.
 * Displays a dynamic grid of slots based on connected reactor chambers.
 * Base reactor has 3 columns (18 slots), each chamber adds 1 column (6 slots).
 *
 * Note: This GUI uses a custom layout that doesn't fit the standard RebornCore GUI.
 * We override extractBackground to draw a custom larger background and disable automatic
 * player slot drawing.
 */
public class GuiNuclearReactor extends GuiBase<BuiltScreenHandler> {

	private final NuclearReactorBlockEntity blockEntity;

	// Grid layout constants
	private static final int GRID_X = 8;
	private static final int GRID_Y = 18;
	private static final int SLOT_SIZE = 18;

	// Player inventory position (below heat bar)
	private static final int PLAYER_INV_Y = 146;

	public GuiNuclearReactor(int syncID, Player player, NuclearReactorBlockEntity blockEntity) {
		super(player, blockEntity, blockEntity.createScreenHandler(syncID, player), 176, 228);
		this.blockEntity = blockEntity;
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor drawContext, int mouseX, int mouseY, float lastFrameDuration) {
		// Note: we override the entire background drawing to fit the custom layout
		// of the reactor. Draw background for the side where we generally have
		// upgrades, but in this case we will have the energy bar and the multiblock
		// hologram button.
		builder.drawDefaultBackground(drawContext, leftPos - 25, topPos + 6, 28, 75);
		// Draw main background, larger than usual to fit the reactor grid and player inventory.
		builder.drawDefaultBackground(drawContext, leftPos, topPos, 176, 228);

		final Layer layer = Layer.BACKGROUND;

		int availableColumns = blockEntity.getReactorSize();

		// Draw reactor grid slots
		for (int row = 0; row < NuclearReactorBlockEntity.GRID_HEIGHT; row++) {
			for (int col = 0; col < NuclearReactorBlockEntity.GRID_WIDTH; col++) {
				int x = GRID_X + col * SLOT_SIZE;
				int y = GRID_Y + row * SLOT_SIZE;

				if (col < availableColumns) {
					drawSlot(drawContext, x, y, layer);
				} else {
					drawLockedSlot(drawContext, x, y);
				}
			}
		}

		// Draw player inventory slots manually at custom position
		int playerInvX = leftPos + 8;
		int playerInvY = topPos + PLAYER_INV_Y;

		// Main inventory (3 rows of 9)
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 9; col++) {
				builder.drawSlot(drawContext, playerInvX + col * 18 - 1, playerInvY + row * 18 - 1);
			}
		}

		// Hotbar (1 row of 9)
		int hotbarY = playerInvY + 58;
		for (int col = 0; col < 9; col++) {
			builder.drawSlot(drawContext, playerInvX + col * 18 - 1, hotbarY - 1);
		}
	}

	/**
	 * Draw a locked slot indicator for unavailable reactor slots.
	 * Shows an X mark over a grayed-out slot.
	 */
	private void drawLockedSlot(GuiGraphicsExtractor drawContext, int x, int y) {
		int absX = leftPos + x;
		int absY = topPos + y;

		// Draw base slot
		builder.drawSlot(drawContext, absX - 1, absY - 1);

		// Draw X mark
		int color = 0xFF555555;
		// Top-left to bottom-right diagonal
		for (int i = 2; i < 13; i++) {
			drawContext.fill(absX + i, absY + i, absX + i + 2, absY + i + 2, color);
		}
		// Top-right to bottom-left diagonal
		for (int i = 2; i < 13; i++) {
			drawContext.fill(absX + 14 - i, absY + i, absX + 16 - i, absY + i + 2, color);
		}
	}

	@Override
	protected void extractLabels(GuiGraphicsExtractor drawContext, int mouseX, int mouseY) {
		// Draw title
		drawTitle(drawContext);

		final Layer layer = Layer.FOREGROUND;

		int hullHeat = blockEntity.getHeat();
		int maxHullHeat = blockEntity.getMaxHeat();

		// Draw hologram button at the top right
		addHologramButton(-21, 10, 212, layer).clickHandler(this::onHologramButtonClick);
		builder.drawHologramButton(drawContext, this, -21, 10, mouseX, mouseY, layer);

		// Draw heat bar in the center
		builder.drawBigHeatBar(drawContext, this, 31, 126, hullHeat, maxHullHeat, layer);

		// Draw energy bar on the left side
		builder.drawMultiEnergyBar(drawContext, this, -18, 24, (int) blockEntity.getEnergy(), (int) blockEntity.getMaxStoredPower(), mouseX, mouseY, 0, layer);
	}

	private void onHologramButtonClick(GuiButtonExtended button, Double x, Double y) {
		blockEntity.renderMultiblock ^= !hideGuiElements();
	}

	@Override
	public boolean tryAddUpgrades() {
		return false; // Reactor doesn't have upgrade slots
	}

	@Override
	public List<GuiTab> getTabs() {
		return List.of(); // Reactor doesn't have tabs
	}
}

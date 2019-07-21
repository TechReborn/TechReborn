/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
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

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;
import reborncore.client.gui.builder.GuiBase;
import reborncore.client.gui.guibuilder.GuiBuilder;
import reborncore.common.network.NetworkManager;
import techreborn.packets.ServerboundPackets;
import techreborn.tiles.machine.tier1.TileAutoCraftingTable;

/**
 * Created by modmuss50 on 20/06/2017.
 */
public class GuiAutoCrafting extends GuiBase {

	static final Identifier RECIPE_BOOK_TEXTURE = new Identifier("textures/gui/recipe_book.png");
	boolean showGui = true;
	TileAutoCraftingTable tileAutoCraftingTable;

	public GuiAutoCrafting(PlayerEntity player, TileAutoCraftingTable tile) {
		super(player, tile, tile.createContainer(player));
		this.tileAutoCraftingTable = tile;
	}

	public void renderItemStack(ItemStack stack, int x, int y) {
		MinecraftClient.getInstance().getItemRenderer().renderGuiItem(stack, x, y);
	}

	@Override
	protected void drawForeground(int mouseX, int mouseY) {
		super.drawForeground(mouseX, mouseY);
		Recipe recipe = tileAutoCraftingTable.getIRecipe();
		if (recipe != null) {
			renderItemStack(recipe.getOutput(), 95, 42);
		}
		final Layer layer = Layer.FOREGROUND;
		
		builder.drawProgressBar(this, tileAutoCraftingTable.getProgress(), tileAutoCraftingTable.getMaxProgress(), 120, 44, mouseX, mouseY, GuiBuilder.ProgressDirection.RIGHT, layer);
		builder.drawMultiEnergyBar(this, 9, 26, (int) tileAutoCraftingTable.getEnergy(), (int) tileAutoCraftingTable.getMaxPower(), mouseX, mouseY, 0, layer);
	}

	@Override
	protected void drawBackground(final float f, int mouseX, int mouseY) {
		super.drawBackground(f, mouseX, mouseY);
		final Layer layer = Layer.BACKGROUND;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				drawSlot(28 + (i * 18), 25 + (j * 18), layer);
			}
		}
		drawOutputSlot(145, 42, layer);
		drawOutputSlot(95, 42, layer);
		drawString("Inventory", 8, 82, 4210752, layer);

		builder.drawLockButton(this, 145, 4, mouseX, mouseY, layer, tileAutoCraftingTable.locked);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		if (isPointInRect(145, 4, 20, 12, mouseX, mouseY)) {
			NetworkManager.sendToServer(ServerboundPackets.createPacketAutoCraftingTableLock(tileAutoCraftingTable, !tileAutoCraftingTable.locked));
			return true;
		}
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}
}

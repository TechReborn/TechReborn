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

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import reborncore.client.gui.builder.GuiBase;
import reborncore.client.gui.guibuilder.GuiBuilder;
import reborncore.common.network.NetworkManager;
import techreborn.packets.PacketAutoCraftingTableLock;
import techreborn.tiles.machine.tier1.TileAutoCraftingTable;

import java.io.IOException;

import static net.minecraft.item.ItemStack.EMPTY;

/**
 * Created by modmuss50 on 20/06/2017.
 */
public class GuiAutoCrafting extends GuiBase {

	static final ResourceLocation RECIPE_BOOK_TEXTURE = new ResourceLocation("textures/gui/recipe_book.png");
	boolean showGui = true;
	TileAutoCraftingTable tileAutoCraftingTable;

	public GuiAutoCrafting(EntityPlayer player, TileAutoCraftingTable tile) {
		super(player, tile, tile.createContainer(player));
		this.tileAutoCraftingTable = tile;
	}

	public void renderItemStack(ItemStack stack, int x, int y) {
		if (stack != EMPTY) {
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			RenderHelper.enableGUIStandardItemLighting();

			RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
			itemRenderer.renderItemAndEffectIntoGUI(stack, x, y);

			GlStateManager.disableLighting();

			GlStateManager.pushMatrix();
			GlStateManager.disableDepth();
			GlStateManager.color(1, 1, 1, 1F / 3F);
			drawRect(x - 4, y- 4, x + 20, y + 20, -2139062144);
			GlStateManager.enableDepth();
			GlStateManager.color(1F, 1F, 1F, 1F);
			GlStateManager.enableBlend();
			GlStateManager.popMatrix();

		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		IRecipe recipe = tileAutoCraftingTable.getIRecipe();
		if (recipe != null) {
			renderItemStack(recipe.getRecipeOutput(), 95, 42);
		}
		final Layer layer = Layer.FOREGROUND;
		
		builder.drawProgressBar(this, tileAutoCraftingTable.getProgress(), tileAutoCraftingTable.getMaxProgress(), 120, 44, mouseX, mouseY, GuiBuilder.ProgressDirection.RIGHT, layer);
		builder.drawMultiEnergyBar(this, 9, 26, (int) tileAutoCraftingTable.getEnergy(), (int) tileAutoCraftingTable.getMaxPower(), mouseX, mouseY, 0, layer);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float f, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(f, mouseX, mouseY);
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
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (isPointInRect(145 + getGuiLeft(), 4 + getGuiTop(), 20, 12, mouseX, mouseY)) {
			NetworkManager.sendToServer(new PacketAutoCraftingTableLock(tileAutoCraftingTable, !tileAutoCraftingTable.locked));
			return;
		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
}

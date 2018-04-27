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

package techreborn.client.gui.autocrafting;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapedOreRecipe;
import org.lwjgl.opengl.GL11;
import reborncore.common.network.NetworkManager;
import techreborn.client.gui.GuiBase;
import techreborn.client.gui.TRBuilder;
import techreborn.packets.PacketSetRecipe;
import techreborn.tiles.tier1.TileAutoCraftingTable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static net.minecraft.item.ItemStack.EMPTY;

/**
 * Created by modmuss50 on 20/06/2017.
 */
public class GuiAutoCrafting extends GuiBase {

	static final ResourceLocation RECIPE_BOOK_TEXTURE = new ResourceLocation("textures/gui/recipe_book.png");
	GuiAutoCraftingRecipeSlector recipeSlector = new GuiAutoCraftingRecipeSlector();
	boolean showGui = true;
	InventoryCrafting dummyInv;
	TileAutoCraftingTable tileAutoCraftingTable;

	public GuiAutoCrafting(EntityPlayer player, TileAutoCraftingTable tile) {
		super(player, tile, tile.createContainer(player));
		this.tileAutoCraftingTable = tile;
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		recipeSlector.tick();
	}

	public void renderItemStack(ItemStack stack, int x, int y) {
		if (stack != EMPTY) {
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			RenderHelper.enableGUIStandardItemLighting();

			RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
			itemRenderer.renderItemAndEffectIntoGUI(stack, x, y);

			GL11.glDisable(GL11.GL_LIGHTING);
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
		this.builder.drawMultiEnergyBar(this, 9, 26, (int) this.tileAutoCraftingTable.getEnergy(), (int) this.tileAutoCraftingTable.getMaxPower(), mouseX, mouseY, 0, layer);
		this.builder.drawProgressBar(this, tileAutoCraftingTable.getProgress(), tileAutoCraftingTable.getMaxProgress(), 120, 44, mouseX, mouseY, TRBuilder.ProgressDirection.RIGHT, layer);

		int mX = mouseX - getGuiLeft();
		int mY = mouseY - getGuiTop();

		if (recipe != null && !tileAutoCraftingTable.customRecipe) {
			if (builder.isInRect(91, 66, 23, 23, mX, mY)) {
				List<String> list = new ArrayList<>();
				list.add("Click to clear");
				net.minecraftforge.fml.client.config.GuiUtils.drawHoveringText(list, mX, mY, width, height, -1, mc.fontRenderer);
				GlStateManager.disableLighting();
				GlStateManager.color(1, 1, 1, 1);
			}
		}
	}

	//Based of vanilla code
	public void renderRecipe(IRecipe recipe, int x, int y) {
		RenderHelper.enableGUIStandardItemLighting();
		GlStateManager.enableAlpha();
		mc.getTextureManager().bindTexture(RECIPE_BOOK_TEXTURE);

		this.drawTexturedModalRect(x, y, 152, 78, 24, 24);

		int recipeWidth = 3;
		int recipeHeight = 3;
		if (recipe instanceof ShapedRecipes) {
			ShapedRecipes shapedrecipes = (ShapedRecipes) recipe;
			recipeWidth = shapedrecipes.getWidth();
			recipeHeight = shapedrecipes.getHeight();
		}
		if (recipe instanceof ShapedOreRecipe) {
			ShapedOreRecipe shapedrecipes = (ShapedOreRecipe) recipe;
			recipeWidth = shapedrecipes.getRecipeWidth();
			recipeHeight = shapedrecipes.getRecipeHeight();
		}
		Iterator<Ingredient> ingredients = recipe.getIngredients().iterator();
		for (int rHeight = 0; rHeight < recipeHeight; ++rHeight) {
			int j1 = 3 + rHeight * 7;
			for (int rWidth = 0; rWidth < recipeWidth; ++rWidth) {
				if (ingredients.hasNext()) {
					ItemStack[] aitemstack = ingredients.next().getMatchingStacks();
					if (aitemstack.length != 0) {
						int l1 = 3 + rWidth * 7;
						GlStateManager.pushMatrix();
						int i2 = (int) ((float) (x + l1) / 0.42F - 3.0F);
						int j2 = (int) ((float) (y + j1) / 0.42F - 3.0F);
						GlStateManager.scale(0.42F, 0.42F, 1.0F);
						GlStateManager.enableLighting();
						mc.getRenderItem().renderItemAndEffectIntoGUI(aitemstack[0], i2, j2);
						GlStateManager.disableLighting();
						GlStateManager.popMatrix();
					}
				}
			}
		}
		GlStateManager.disableAlpha();
		RenderHelper.disableStandardItemLighting();
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

		IRecipe recipe = tileAutoCraftingTable.getIRecipe();
		if (recipe != null && !tileAutoCraftingTable.customRecipe) {
			renderRecipe(recipe, guiLeft + 91, 66 + guiTop);
		}
	}

	@Override
	public void initGui() {
		super.initGui();
		recipeSlector.setGuiAutoCrafting(this);
		dummyInv = new InventoryCrafting(new Container() {
			@Override
			public boolean canInteractWith(EntityPlayer playerIn) {
				return false;
			}
		}, 3, 3);
		for (int i = 0; i < 9; i++) {
			dummyInv.setInventorySlotContents(i, ItemStack.EMPTY);
		}
		this.recipeSlector.func_194303_a(this.width, this.height, Minecraft.getMinecraft(), false, dummyInv);
		this.guiLeft = this.recipeSlector.updateScreenPosition(false, this.width, this.xSize);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if (showGui) {
			this.recipeSlector.render(mouseX, mouseY, 0.1F);
			super.drawScreen(mouseX, mouseY, partialTicks);
			this.recipeSlector.renderGhostRecipe(this.guiLeft, this.guiTop, false, partialTicks);
		} else {
			super.drawScreen(mouseX, mouseY, partialTicks);
		}
		this.recipeSlector.renderTooltip(this.guiLeft, this.guiTop, mouseX, mouseY);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (!this.recipeSlector.mouseClicked(mouseX, mouseY, mouseButton)) {
			super.mouseClicked(mouseX, mouseY, mouseButton);
		}
		int mX = mouseX - getGuiLeft();
		int mY = mouseY - getGuiTop();

		mc.getTextureManager().bindTexture(TRBuilder.GUI_SHEET);
		if (builder.isInRect(91, 66, 23, 23, mX, mY)) {
			setRecipe(null, true);
		}
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (!this.recipeSlector.keyPressed(typedChar, keyCode)) {
			super.keyTyped(typedChar, keyCode);
		}
	}

	@Override
	protected void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type) {
		super.handleMouseClick(slotIn, slotId, mouseButton, type);
		this.recipeSlector.slotClicked(slotIn);
	}

	@Override
	public void onGuiClosed() {
		this.recipeSlector.removed();
		super.onGuiClosed();
	}

	public void setRecipe(IRecipe recipe, boolean custom) {
		tileAutoCraftingTable.setCurrentRecipe(recipe, custom);
		NetworkManager.sendToServer(new PacketSetRecipe(tileAutoCraftingTable, recipe, custom));
	}

}

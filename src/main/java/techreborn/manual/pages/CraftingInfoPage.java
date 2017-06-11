/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 TechReborn
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

package techreborn.manual.pages;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import org.lwjgl.opengl.GL11;
import techreborn.manual.PageCollection;
import techreborn.manual.util.ButtonUtil;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CraftingInfoPage extends TitledPage {
	public ItemStack result;
	public String imageprefix = "techreborn:textures/manual/elements/";
	public String backpage;
	private boolean isSmelting = false;
	private ItemStack[] recipe = new ItemStack[9];
	private boolean hasRecipe = false;
	private String rawDescription;
	private List<String> formattedDescription;
	private float descriptionScale = 0.66f;

	public CraftingInfoPage(String name, PageCollection collection, ItemStack itemStack, String unlocalizedDescription,
	                        String backPage) {
		super(name, true, collection, itemStack.getUnlocalizedName() + ".name", Color.white.getRGB());
		this.result = itemStack;
		this.recipe = getFirstRecipeForItem(itemStack);
		this.backpage = backPage;
		for (ItemStack stack : recipe)
			if (stack != null)
				hasRecipe = true;
		if (unlocalizedDescription == "")
			rawDescription = ttl(itemStack.getUnlocalizedName() + ".description");
		else
			rawDescription = ttl(unlocalizedDescription);
	}

	@Override
	public void initGui() {
		buttonList.clear();
		ButtonUtil.addBackButton(0, width / 2 - 60, height / 2 + 64, buttonList);
	}

	@Override
	public void renderBackgroundLayer(Minecraft minecraft, int offsetX, int offsetY, int mouseX, int mouseY) {
		super.renderBackgroundLayer(minecraft, offsetX, offsetY, mouseX, mouseY);
		GL11.glPushMatrix();
		if (isSmelting) {
			renderImage(offsetX + 15, offsetY + 10, "furnacerecipe");
		} else {
			if (hasRecipe) {
				renderImage(offsetX + (offsetX / 2), offsetY + 10, "craftingtable");
			} else {
				drawString(fontRendererObj, "No Crafting Recipe", offsetX + 40, offsetY + 22, Color.black.getRGB());
			}
		}
		GL11.glPopMatrix();

	}

	public void drawScreen(Minecraft minecraft, int offsetX, int offsetY, int mouseX, int mouseY) {
		super.drawScreen(minecraft, offsetX, offsetY, mouseX, mouseY);
		int relativeMouseX = mouseX + offsetX;
		int relativeMouseY = mouseY + offsetY;
		int gridOffsetX = isSmelting ? 85 : 71;
		int gridOffsetY = 18;
		int itemBoxSize = 18;
		addDescription(minecraft, offsetX + 8, offsetY);

		ItemStack tooltip = null;
		int i = 0;
		for (ItemStack input : recipe) {
			if (input != null) {
				int row = (i % 3);
				int column = i / 3;
				int itemX = offsetX + gridOffsetX + (row * itemBoxSize) - 54;
				int itemY = offsetY + gridOffsetY + (column * itemBoxSize) + 2;
				drawItemStack(input, itemX, itemY, "");
				if (relativeMouseX > itemX - 2 && relativeMouseX < itemX - 2 + itemBoxSize && relativeMouseY > itemY - 2
					&& relativeMouseY < itemY - 2 + itemBoxSize) {
					tooltip = input;
				}
			}
			i++;
		}
		int itemX = offsetX + (isSmelting ? 92 : 112);
		int itemY = offsetY + (isSmelting ? 40 : 38);
		if (!hasRecipe) {
			itemX = offsetX + 20;
			itemY = offsetY + 18;
		}

		drawItemStack(result, itemX, itemY, "");
		if (relativeMouseX > itemX - 2 && relativeMouseX < itemX - 2 + itemBoxSize && relativeMouseY > itemY - 2
			&& relativeMouseY < itemY - 2 + itemBoxSize) {
			tooltip = result;
		}
		if (tooltip != null) {
			drawItemStackTooltip(tooltip, relativeMouseX, relativeMouseY);
		}
	}

	public void renderImage(int offsetX, int offsetY, String imagename) {
		TextureManager render = Minecraft.getMinecraft().renderEngine;
		render.bindTexture(new ResourceLocation(imageprefix + imagename + ".png"));

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		drawTexturedModalRect(offsetX + 16, offsetY + 9, 0, 0, 116, 54);
		GL11.glDisable(GL11.GL_BLEND);
	}

	public void addDescription(Minecraft minecraft, int offsetX, int offsetY) {
		GL11.glPushMatrix();
		if (hasRecipe)
			GL11.glTranslated(offsetX + 5, offsetY + 75, 1);
		else
			GL11.glTranslated(offsetX + 5, offsetY + 40, 1);
		GL11.glScalef(descriptionScale, descriptionScale, descriptionScale);
		int offset = 0;
		for (String s : getFormattedText(fontRendererObj)) {
			if (s == null)
				break;
			if (s.contains("\\%") && s.substring(0, 2).equals("\\%")) {
				s = s.substring(2);
				offset += fontRendererObj.FONT_HEIGHT / 2;
			}
			fontRendererObj.drawString(s, 0, offset, Color.black.getRGB());
			offset += fontRendererObj.FONT_HEIGHT;
		}
		GL11.glPopMatrix();
	}

	@SuppressWarnings("unchecked")
	public List<String> getFormattedText(FontRenderer fr) {
		if (formattedDescription == null) {
			formattedDescription = new ArrayList<>();

			if (Strings.isNullOrEmpty(rawDescription)) {
				formattedDescription = ImmutableList.of();
				return formattedDescription;
			}
			if (!rawDescription.contains("\\n")) {
				formattedDescription = ImmutableList.copyOf(fr.listFormattedStringToWidth(rawDescription, 370));
				return formattedDescription;
			}

			List<String> segments = new ArrayList();
			String raw = rawDescription;

			int escape = 0;
			while (raw.contains("\\n")) {
				segments.add(raw.substring(0, raw.indexOf("\\n")));
				raw = raw.substring(raw.indexOf("\\n") + 2);
				if (!raw.contains("\\n"))
					segments.add(raw);

				escape++;
				if (escape > 100) {
					break;
				}
			}

			for (String s : segments)
				formattedDescription.addAll(ImmutableList.copyOf(fr.listFormattedStringToWidth(s, 370)));
		}
		return formattedDescription;
	}

	protected void drawItemStackTooltip(ItemStack stack, int x, int y) {
		final Minecraft mc = Minecraft.getMinecraft();
		FontRenderer font = MoreObjects.firstNonNull(stack.getItem().getFontRenderer(stack), mc.fontRendererObj);

		@SuppressWarnings("unchecked")
		List<String> list = stack.getTooltip(mc.player, mc.gameSettings.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL);

		List<String> colored = Lists.newArrayListWithCapacity(list.size());
		colored.add(stack.getRarity().rarityColor + list.get(0));
		for (String line : list)
			colored.add(TextFormatting.GRAY + line);

		if (colored.size() >= 2)
			colored.remove(1);
		drawHoveringText(colored, x, y, font);
	}

	private void drawItemStack(ItemStack par1ItemStack, int par2, int par3, String par4Str) {
		GL11.glTranslatef(0.0F, 0.0F, 32.0F);
		this.zLevel = 200.0F;
		RenderHelper.enableGUIStandardItemLighting();
		GL11.glColor3f(1f, 1f, 1f);
		GL11.glEnable(GL11.GL_NORMALIZE);
		FontRenderer font = null;
		if (par1ItemStack != null)
			font = par1ItemStack.getItem().getFontRenderer(par1ItemStack);
		if (font == null)
			font = Minecraft.getMinecraft().fontRendererObj;
		renderItemStack(par1ItemStack, par2, par3);
		this.zLevel = 0.0F;
	}

	public void renderItemStack(ItemStack stack, int x, int y) {
		if (stack != null) {
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			RenderHelper.enableGUIStandardItemLighting();

			RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
			itemRenderer.renderItemAndEffectIntoGUI(stack, x, y);

			GL11.glDisable(GL11.GL_LIGHTING);
		}
	}

	@SuppressWarnings("unchecked")
	private ItemStack[] getFirstRecipeForItem(ItemStack resultingItem) {
		ItemStack[] recipeItems = new ItemStack[9];
		for (IRecipe recipe : CraftingManager.field_193380_a) {
			if (recipe == null)
				continue;

			ItemStack result = recipe.getRecipeOutput();
			if (result == ItemStack.EMPTY || !result.isItemEqual(resultingItem))
				continue;

			NonNullList<Ingredient> input = recipe.func_192400_c();
			if (input.isEmpty())
				continue;

			for (int i = 0; i < input.size(); i++)
				recipeItems[i] = convertToStack(input.get(i));
			break;

		}

		Iterator iterator = FurnaceRecipes.instance().getSmeltingList().entrySet().iterator();
		Map.Entry entry;

		while (iterator.hasNext()) {
			entry = (Map.Entry) iterator.next();
			if (entry.getKey() instanceof ItemStack && ((ItemStack) entry.getValue()).isItemEqual(result)) {
				isSmelting = true;
				recipeItems[0] = (ItemStack) entry.getKey();
			}
		}

		return recipeItems;
	}

	protected ItemStack convertToStack(Ingredient ingredient) {
		ItemStack entry = ItemStack.EMPTY;
		entry = ingredient.func_193365_a()[0];
		if (entry == null || entry.isEmpty())
			return null;
		entry = entry.copy();
		if (entry.getItemDamage() == OreDictionary.WILDCARD_VALUE)
			entry.setItemDamage(0);
		return entry;
	}

	private Object[] getShapedOreRecipe(ShapedOreRecipe recipe) {
		try {
			Field field = ShapedOreRecipe.class.getDeclaredField("width");
			if (field != null) {
				field.setAccessible(true);
				int width = field.getInt(recipe);
				NonNullList<Ingredient> input = recipe.func_192400_c();
				Object[] grid = new Object[9];
				for (int i = 0, offset = 0, y = 0; y < 3; y++) {
					for (int x = 0; x < 3; x++, i++) {
						if (x < width && offset < input.size()) {
							grid[i] = input.get(offset);
							offset++;
						} else {
							grid[i] = null;
						}
					}
				}
				return grid;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void actionPerformed(GuiButton button) {
		if (button.id == 0)
			collection.changeActivePage(backpage);
	}
}

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

package techreborn.compat.compat.jei.rollingMachine;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import reborncore.common.util.StringUtils;
import techreborn.compat.compat.jei.RecipeCategoryUids;
import techreborn.lib.ModInfo;

import javax.annotation.Nonnull;

public class RollingMachineRecipeCategory implements IRecipeCategory<RollingMachineRecipeWrapper> {
	private static final int[] INPUT_SLOTS = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
	private static final int[] OUTPUT_SLOTS = { 10 };

	private final IDrawable background;
	private final IDrawableAnimated progress;
	private final ICraftingGridHelper craftingGridHelper;
	private final String title;

	public static final ResourceLocation texture = new ResourceLocation("techreborn", "textures/gui/rolling_machine.png");

	public RollingMachineRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.createDrawable(texture, 29, 16, 116, 54);
		title = StringUtils.t("tile.techreborn:rolling_machine.name");

		IDrawableStatic progressStatic = guiHelper.createDrawable(texture, 176, 14, 20, 18);
		progress = guiHelper.createAnimatedDrawable(progressStatic, 250, IDrawableAnimated.StartDirection.LEFT, false);

		craftingGridHelper = guiHelper.createCraftingGridHelper(INPUT_SLOTS[0], OUTPUT_SLOTS[0]);
	}

	@Nonnull
	@Override
	public String getUid() {
		return RecipeCategoryUids.ROLLING_MACHINE;
	}

	@Nonnull
	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getModName() {
		return ModInfo.MOD_NAME;
	}

	@Nonnull
	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public void drawExtras(@Nonnull Minecraft minecraft) {
		progress.draw(minecraft, 62, 18);
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull RollingMachineRecipeWrapper recipeWrapper,
			@Nonnull IIngredients ingredients) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		for (int l = 0; l < 3; l++) {
			for (int k1 = 0; k1 < 3; k1++) {
				int i = k1 + l * 3;
				guiItemStacks.init(INPUT_SLOTS[i], true, k1 * 18, l * 18);
			}
		}
		guiItemStacks.init(OUTPUT_SLOTS[0], false, 94, 18);

		craftingGridHelper.setInputs(guiItemStacks, ingredients.getInputs(ItemStack.class));
		guiItemStacks.set(OUTPUT_SLOTS[0], ingredients.getOutputs(ItemStack.class).get(0));
	}
}

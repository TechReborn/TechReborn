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

package techreborn.compat.rei.fluidreplicator;

import me.shedaniel.math.api.Point;
import me.shedaniel.math.api.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeCategory;
import me.shedaniel.rei.gui.widget.*;
import me.shedaniel.rei.impl.ScreenHelper;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import reborncore.common.crafting.RebornRecipeType;
import reborncore.common.util.StringUtils;
import techreborn.api.recipe.recipes.FluidReplicatorRecipe;
import techreborn.compat.rei.ReiPlugin;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class FluidReplicatorRecipeCategory implements RecipeCategory<FluidReplicatorRecipeDisplay> {
	
	private final RebornRecipeType<FluidReplicatorRecipe> rebornRecipeType;
	
	public FluidReplicatorRecipeCategory(RebornRecipeType<FluidReplicatorRecipe> recipeType) {
		this.rebornRecipeType = recipeType;
		
	}
	
	@Override
	public Identifier getIdentifier() {
		return rebornRecipeType.getName();
	}
	
	@Override
	public String getCategoryName() {
		return StringUtils.t(rebornRecipeType.getName().toString());
	}
	
	@Override
	public EntryStack getLogo() {
		return EntryStack.create(ReiPlugin.iconMap.getOrDefault(rebornRecipeType, () -> Items.DIAMOND_SHOVEL));
	}
	
	@Override
	public List<Widget> setupDisplay(Supplier<FluidReplicatorRecipeDisplay> recipeDisplaySupplier, Rectangle bounds) {
		FluidReplicatorRecipeDisplay machineRecipe = recipeDisplaySupplier.get();
		
		Point startPoint = new Point(bounds.getCenterX() - 41, bounds.getCenterY() - 13);
		
		List<Widget> widgets = new LinkedList<>();
		widgets.add(new RecipeBaseWidget(bounds));
		widgets.add(RecipeArrowWidget.create(new Point(startPoint.x + 24, startPoint.y + 1), true).time(machineRecipe.getTime() * 50.0));
		
		int i = 0;
		for (List<EntryStack> inputs : machineRecipe.getInputEntries()) {
			widgets.add(EntryWidget.create(startPoint.x + 1, startPoint.y + 1 + (i++ * 20)).entries(inputs).markIsInput());
		}
		
		Text energyPerTick = new TranslatableText("techreborn.jei.recipe.running.cost", "E", machineRecipe.getEnergy());
		LabelWidget costLabel;
		widgets.add(costLabel = LabelWidget.create(new Point(startPoint.x + 1, startPoint.y + 1 + (i++ * 20)), energyPerTick.asFormattedString()));
		costLabel.setHasShadows(false);
		costLabel.setDefaultColor(ScreenHelper.isDarkModeEnabled() ? 0xFFBBBBBB : 0xFF404040);
		
		if (!machineRecipe.getOutputEntries().isEmpty())
			widgets.add(EntryWidget.create(startPoint.x + 61, startPoint.y + 1).entries(machineRecipe.getOutputEntries()).markIsOutput());
		
		return widgets;
	}
	
}

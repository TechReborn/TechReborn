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

package techreborn.compat.rei;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeCategory;
import me.shedaniel.rei.api.widgets.Label;
import me.shedaniel.rei.api.widgets.Widgets;
import me.shedaniel.rei.gui.entries.RecipeEntry;
import me.shedaniel.rei.gui.entries.SimpleRecipeEntry;
import me.shedaniel.rei.gui.widget.Widget;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.crafting.RebornRecipeType;
import reborncore.common.util.StringUtils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MachineRecipeCategory<R extends RebornRecipe> implements RecipeCategory<MachineRecipeDisplay<R>> {
	
	private final RebornRecipeType<R> rebornRecipeType;
	private int recipeLines;
	
	MachineRecipeCategory(RebornRecipeType<R> rebornRecipeType) {
		this(rebornRecipeType, 2);
	}
	
	MachineRecipeCategory(RebornRecipeType<R> rebornRecipeType, int lines) {
		this.rebornRecipeType = rebornRecipeType;
		this.recipeLines = lines;
	}
	
	@Override
	public Identifier getIdentifier() {
		return rebornRecipeType.getName();
	}
	
	@Override
	public String getCategoryName() {
		return new TranslatableText(rebornRecipeType.getName().toString()).asString();
	}
	
	@Override
	public EntryStack getLogo() {
		return EntryStack.create(ReiPlugin.iconMap.getOrDefault(rebornRecipeType, () -> Items.DIAMOND_SHOVEL));
	}
	
	@Override
	public RecipeEntry getSimpleRenderer(MachineRecipeDisplay<R> recipe) {
		return SimpleRecipeEntry.create(Collections.singletonList(recipe.getInputEntries().get(0)), recipe.getOutputEntries());
	}
	
	@Override
	public List<Widget> setupDisplay(MachineRecipeDisplay<R> machineRecipe, Rectangle bounds) {
		Point startPoint = new Point(bounds.getCenterX() - 41, bounds.getCenterY() - recipeLines * 12 - 1);
		
		List<Widget> widgets = new LinkedList<>();
		widgets.add(Widgets.createRecipeBase(bounds));
		widgets.add(Widgets.createArrow(new Point(startPoint.x + 26, startPoint.y + 1)).animationDurationTicks(machineRecipe.getTime()));
		
		int i = 0;
		for (List<EntryStack> inputs : machineRecipe.getInputEntries()) {
			widgets.add(Widgets.createSlot(new Point(startPoint.x + 1, startPoint.y + 1 + (i++ * 20))).entries(inputs).markInput());
		}
		
		Text energyPerTick = new TranslatableText("techreborn.jei.recipe.running.cost", "E", machineRecipe.getEnergy());
		Label costLabel;
		widgets.add(costLabel = Widgets.createLabel(new Point(startPoint.x + 1, startPoint.y + 1 + (i++ * 20)), energyPerTick));
		costLabel.shadow(false);
		costLabel.color(0xFF404040, 0xFFBBBBBB);
		
		i = 0;
		for (EntryStack outputs : machineRecipe.getOutputEntries()) {
			widgets.add(Widgets.createSlot(new Point(startPoint.x + 61, startPoint.y + 1 + (i++ * 20))).entry(outputs).markInput());
		}
		
		int heat = machineRecipe.getHeat();
		if (heat > 0) {
			Text neededHeat = new LiteralText(String.valueOf(heat)).append(" ").append(new TranslatableText("techreborn.jei.recipe.heat"));
			Label heatLabel;
			widgets.add(heatLabel = Widgets.createLabel(new Point(startPoint.x + 61, startPoint.y + 1 + (i++ * 20)), neededHeat));
			heatLabel.shadow(false);
			heatLabel.color(0xFF404040, 0xFFBBBBBB);
		}
		
		return widgets;
	}
	
	@Override
	public int getDisplayHeight() {
		if (recipeLines == 1) {
			return 37;
		} else if (recipeLines == 3) {
			return 80;
		} else if (recipeLines == 4) {
			return 105;
		}
		return 60;
	}
	
}

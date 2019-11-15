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

package techreborn.compat.rei;

import me.shedaniel.math.api.Point;
import me.shedaniel.math.api.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeCategory;
import me.shedaniel.rei.gui.entries.RecipeEntry;
import me.shedaniel.rei.gui.entries.SimpleRecipeEntry;
import me.shedaniel.rei.gui.widget.*;
import me.shedaniel.rei.impl.ScreenHelper;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.crafting.RebornRecipeType;
import reborncore.common.util.StringUtils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

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
		return StringUtils.t(rebornRecipeType.getName().toString());
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
	public List<Widget> setupDisplay(Supplier<MachineRecipeDisplay<R>> recipeDisplaySupplier, Rectangle bounds) {

		MachineRecipeDisplay<R> machineRecipe = recipeDisplaySupplier.get();

		Point startPoint = new Point( bounds.getCenterX() - 41, bounds.getCenterY() - recipeLines*12 -1);

        List<Widget> widgets = new LinkedList<>();
        widgets.add(new RecipeBaseWidget(bounds));
        widgets.add(new RecipeArrowWidget(startPoint.x + 24, startPoint.y + 1, true));

		int i = 0;
		for (List<EntryStack> inputs : machineRecipe.getInputEntries()){
			widgets.add(EntryWidget.create(startPoint.x + 1, startPoint.y + 1 + (i++ * 20)).entries(inputs));
		}
		
		Text energyPerTick = new TranslatableText("techreborn.jei.recipe.running.cost", "E", machineRecipe.getEnergy());
		LabelWidget costLabel;
		widgets.add(costLabel = new LabelWidget(startPoint.x + 1, startPoint.y + 1 + (i++ * 20), energyPerTick.asFormattedString()));
		costLabel.setHasShadows(false);
		costLabel.setDefaultColor(ScreenHelper.isDarkModeEnabled() ? 0xFFBBBBBB : 0xFF404040);

		i = 0;
		for (EntryStack outputs : machineRecipe.getOutputEntries()){
			widgets.add(EntryWidget.create(startPoint.x + 61, startPoint.y + 1 + (i++ * 20)).entry(outputs));
		}
		
		int heat = machineRecipe.getHeat();
		if (heat > 0) {
			String neededHeat = heat + " " + StringUtils.t("techreborn.jei.recipe.heat");
			LabelWidget heatLabel;
			widgets.add(heatLabel = new LabelWidget(startPoint.x + 61, startPoint.y + 1 + (i++ * 20), neededHeat));
			heatLabel.setHasShadows(false);
			heatLabel.setDefaultColor(ScreenHelper.isDarkModeEnabled() ? 0xFFBBBBBB : 0xFF404040);
		}

		return widgets;
	}

	@Override
	public int getDisplayHeight() {
		if (recipeLines == 1) {
			return 37;
		}
		else if (recipeLines == 3) {
			return 80;
		}
		else if (recipeLines == 4) {
			return 105;
		}
		return 60;
	}

}

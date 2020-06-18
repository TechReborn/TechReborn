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

package techreborn.compat.rei.fluidgenerator;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeCategory;
import me.shedaniel.rei.api.widgets.Label;
import me.shedaniel.rei.api.widgets.Widgets;
import me.shedaniel.rei.gui.widget.Widget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import techreborn.TechReborn;
import techreborn.init.TRContent;

import java.util.LinkedList;
import java.util.List;

public class FluidGeneratorRecipeCategory implements RecipeCategory<FluidGeneratorRecipeDisplay> {
	
	private TRContent.Machine generator;
	private Identifier identifier;
	
	public FluidGeneratorRecipeCategory(TRContent.Machine generator) {
		this.generator = generator;
		this.identifier = new Identifier(TechReborn.MOD_ID, generator.name);
	}
	
	@Override
	public Identifier getIdentifier() {
		return identifier;
	}
	
	@Override
	public String getCategoryName() {
		return I18n.translate(identifier.toString());
	}
	
	@Override
	public EntryStack getLogo() {
		return EntryStack.create(generator);
	}
	
	@Override
	public List<Widget> setupDisplay(FluidGeneratorRecipeDisplay machineRecipe, Rectangle bounds) {
		Point startPoint = new Point(bounds.getCenterX() - 41, bounds.getCenterY() - 13);
		
		List<Widget> widgets = new LinkedList<>();
		widgets.add(Widgets.createRecipeBase(bounds));
		widgets.add(Widgets.createArrow(new Point(startPoint.x + 26, startPoint.y + 1)).animationDurationMS(1000.0));
		
		for (List<EntryStack> inputs : machineRecipe.getInputEntries()) {
			widgets.add(Widgets.createSlot(new Point(startPoint.x + 1, startPoint.y + 1)).entries(inputs).markInput());
		}
		
		Text energyPerTick = new TranslatableText("techreborn.jei.recipe.generator.total", machineRecipe.getTotalEnergy());
		Label costLabel;
		widgets.add(costLabel = Widgets.createLabel(new Point(bounds.getCenterX(), startPoint.y + 20), energyPerTick));
		costLabel.shadow(false);
		costLabel.color(0xFF404040, 0xFFBBBBBB);
		
		return widgets;
	}
	
	@Override
	public int getDisplayHeight() {
		return 37;
	}
	
}

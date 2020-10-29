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

package techreborn.compat.rei.machine;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.widgets.Widgets;
import me.shedaniel.rei.gui.widget.Widget;
import net.minecraft.text.TranslatableText;
import reborncore.client.gui.guibuilder.GuiBuilder;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.crafting.RebornRecipeType;
import techreborn.compat.rei.MachineRecipeDisplay;
import techreborn.compat.rei.ReiPlugin;

import java.text.DecimalFormat;
import java.util.List;

public class AssemblingMachineCategory<R extends RebornRecipe> extends AbstractEnergyConsumingMachineCategory<R> {
	public AssemblingMachineCategory(RebornRecipeType<R> rebornRecipeType) {
		super(rebornRecipeType);
	}

	@Override
	public List<Widget> setupDisplay(MachineRecipeDisplay<R> recipeDisplay, Rectangle bounds) {
		List<Widget> widgets = super.setupDisplay(recipeDisplay, bounds);
		widgets.add(Widgets.createSlot(new Point(bounds.x + 55 - 9, bounds.y + 35 - 19)).entries(getInput(recipeDisplay, 0)).markInput());
		widgets.add(Widgets.createSlot(new Point(bounds.x + 55 - 9, bounds.y + 55 - 19)).entries(getInput(recipeDisplay, 1)).markInput());
		widgets.add(Widgets.createResultSlotBackground(new Point(bounds.x + 101 - 9, bounds.y + 45 - 19)));
		widgets.add(Widgets.createSlot(new Point(bounds.x + 101 - 9, bounds.y + 45 - 19)).entries(getOutput(recipeDisplay, 0)).disableBackground().markOutput());
		widgets.add(ReiPlugin.createProgressBar(bounds.x + 76 - 9, bounds.y + 48 - 19, recipeDisplay.getTime() * 50, GuiBuilder.ProgressDirection.RIGHT));

		widgets.add(Widgets.createLabel(new Point(bounds.getMaxX() - 5, bounds.y + 5), new TranslatableText("techreborn.jei.recipe.processing.time.3", new DecimalFormat("###.##").format(recipeDisplay.getTime() / 20.0)))
				.shadow(false)
				.rightAligned()
				.color(0xFF404040, 0xFFBBBBBB)
		);
		return widgets;
	}
}

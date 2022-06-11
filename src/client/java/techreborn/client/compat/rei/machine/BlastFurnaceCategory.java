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

package techreborn.client.compat.rei.machine;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import net.minecraft.text.Text;
import reborncore.client.gui.guibuilder.GuiBuilder;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.crafting.RebornRecipeType;
import techreborn.client.compat.rei.MachineRecipeDisplay;
import techreborn.client.compat.rei.ReiPlugin;

import java.text.DecimalFormat;
import java.util.List;

public class BlastFurnaceCategory<R extends RebornRecipe> extends AbstractEnergyConsumingMachineCategory<R> {
	public BlastFurnaceCategory(RebornRecipeType<R> rebornRecipeType) {
		super(rebornRecipeType);
	}

	@Override
	public List<Widget> setupDisplay(MachineRecipeDisplay<R> recipeDisplay, Rectangle bounds) {
		List<Widget> widgets = super.setupDisplay(recipeDisplay, bounds);
		widgets.add(Widgets.createSlot(new Point(bounds.x + 55 - 17, bounds.y + 35 - 19)).entries(getInput(recipeDisplay, 0)).markInput());
		widgets.add(Widgets.createSlot(new Point(bounds.x + 55 - 17, bounds.y + 55 - 19)).entries(getInput(recipeDisplay, 1)).markInput());
		widgets.add(Widgets.createSlotBase(new Rectangle(bounds.x + 101 - 17 - 5, bounds.y + 45 - 19 - 5, 46, 26)));
		widgets.add(Widgets.createSlot(new Point(bounds.x + 101 - 17, bounds.y + 45 - 19)).entries(getOutput(recipeDisplay, 0)).disableBackground().markOutput());
		widgets.add(Widgets.createSlot(new Point(bounds.x + 101 + 20 - 17, bounds.y + 45 - 19)).entries(getOutput(recipeDisplay, 1)).disableBackground().markOutput());
		widgets.add(ReiPlugin.createProgressBar(bounds.x + 76 - 17, bounds.y + 48 - 19, recipeDisplay.getTime() * 50, GuiBuilder.ProgressDirection.RIGHT));

		Text neededHeat = Text.literal(String.valueOf(recipeDisplay.getHeat())).append(" ").append(Text.translatable("techreborn.jei.recipe.heat"));
		widgets.add(Widgets.createLabel(new Point(bounds.getMaxX() - 5, bounds.y + 5), neededHeat)
				.shadow(false)
				.rightAligned()
				.color(0xFF404040, 0xFFBBBBBB)
		);

		widgets.add(Widgets.createLabel(new Point(bounds.x + 24, bounds.y + 5), Text.translatable("techreborn.jei.recipe.processing.time.3", new DecimalFormat("###.##").format(recipeDisplay.getTime() / 20.0)))
				.shadow(false)
				.leftAligned()
				.color(0xFF404040, 0xFFBBBBBB)
		);
		return widgets;
	}
}

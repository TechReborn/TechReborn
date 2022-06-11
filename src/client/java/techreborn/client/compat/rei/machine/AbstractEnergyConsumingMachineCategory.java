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

import com.google.common.collect.Lists;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.ClientHelper;
import me.shedaniel.rei.api.client.gui.widgets.Tooltip;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.crafting.RebornRecipeType;
import techreborn.client.compat.rei.MachineRecipeDisplay;
import techreborn.client.compat.rei.ReiPlugin;

import java.util.List;

public abstract class AbstractEnergyConsumingMachineCategory<R extends RebornRecipe> extends AbstractMachineCategory<R> {
	public AbstractEnergyConsumingMachineCategory(RebornRecipeType<R> rebornRecipeType) {
		super(rebornRecipeType);
	}

	@Override
	public List<Widget> setupDisplay(MachineRecipeDisplay<R> recipeDisplay, Rectangle bounds) {
		List<Widget> widgets = Lists.newArrayList();
		widgets.add(Widgets.createRecipeBase(bounds));
		widgets.add(ReiPlugin.createEnergyDisplay(new Rectangle(bounds.x + 8, bounds.y + 8, 14, 50), recipeDisplay.getEnergy(), ReiPlugin.EntryAnimation.downwards(5000), tooltipContext -> {
			List<Text> list = Lists.newArrayList();
			list.add(Text.of("Energy"));
			list.add(Text.translatable("techreborn.jei.recipe.running.cost", "E", recipeDisplay.getEnergy()).formatted(Formatting.GRAY));
			list.add(Text.of(""));
			list.add(ClientHelper.getInstance().getFormattedModFromIdentifier(new Identifier("techreborn", "")));
			return Tooltip.create(tooltipContext.getPoint(), list);
		}));
		return widgets;
	}
}

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

package techreborn.client.compat.rei.fluidgenerator;

import com.google.common.collect.Lists;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.ClientHelper;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Tooltip;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import reborncore.client.gui.guibuilder.GuiBuilder;
import techreborn.TechReborn;
import techreborn.client.compat.rei.ReiPlugin;
import techreborn.init.TRContent;

import java.util.List;

public class FluidGeneratorRecipeCategory implements DisplayCategory<FluidGeneratorRecipeDisplay> {

	private final TRContent.Machine generator;
	private final CategoryIdentifier<? extends FluidGeneratorRecipeDisplay> identifier;

	public FluidGeneratorRecipeCategory(TRContent.Machine generator) {
		this.generator = generator;
		this.identifier = CategoryIdentifier.of(TechReborn.MOD_ID, generator.name);
	}

	@Override
	public CategoryIdentifier<? extends FluidGeneratorRecipeDisplay> getCategoryIdentifier() {
		return identifier;
	}

	@Override
	public Text getTitle() {
		return Text.translatable(identifier.toString());
	}

	@Override
	public Renderer getIcon() {
		return EntryStacks.of(generator);
	}

	@Override
	public List<Widget> setupDisplay(FluidGeneratorRecipeDisplay recipeDisplay, Rectangle bounds) {
		List<Widget> widgets = Lists.newArrayList();
		widgets.add(Widgets.createRecipeBase(bounds));
		widgets.add(ReiPlugin.createEnergyDisplay(new Rectangle(bounds.x + 108, bounds.y + 8, 14, 50), recipeDisplay.getTotalEnergy(), ReiPlugin.EntryAnimation.upwards(5000), tooltipContext -> {
			List<Text> list = Lists.newArrayList();
			list.add(Text.of("Energy"));
			list.add(Text.translatable("techreborn.jei.recipe.generator.total", recipeDisplay.getTotalEnergy()).formatted(Formatting.GRAY));
			list.add(Text.of(""));
			list.add(ClientHelper.getInstance().getFormattedModFromIdentifier(new Identifier("techreborn", "")));
			return Tooltip.create(tooltipContext.getPoint(), list);
		}));
		widgets.add(ReiPlugin.createFluidDisplay(new Rectangle(bounds.x + 16, bounds.y + 8, 16, 50), recipeDisplay.getInputEntries().get(0).get(0).cast(), ReiPlugin.EntryAnimation.downwards(5000)));
		widgets.add(ReiPlugin.createProgressBar(bounds.x + 76 - 16, bounds.y + 48 - 19, 5000, GuiBuilder.ProgressDirection.RIGHT));
		return widgets;
	}
}

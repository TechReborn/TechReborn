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

package techreborn.client.compat.rei.rollingmachine;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.ClientHelper;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Slot;
import me.shedaniel.rei.api.client.gui.widgets.Tooltip;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.InputIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import reborncore.client.gui.GuiBuilder;
import techreborn.recipe.recipes.RollingMachineRecipe;
import techreborn.client.compat.rei.ReiPlugin;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class RollingMachineCategory implements DisplayCategory<RollingMachineDisplay> {

	private final RecipeType<RollingMachineRecipe> recipeType;

	public RollingMachineCategory(RecipeType<RollingMachineRecipe> recipeType) {
		this.recipeType = recipeType;
	}

	private Identifier id() {
		return Registries.RECIPE_TYPE.getId(recipeType);
	}


	@Override
	public CategoryIdentifier<? extends RollingMachineDisplay> getCategoryIdentifier() {
		return CategoryIdentifier.of(id());
	}

	@Override
	public Text getTitle() {
		return Text.translatable(id().toString());
	}

	@Override
	public Renderer getIcon() {
		return EntryStacks.of(ReiPlugin.iconMap.getOrDefault(recipeType, () -> Items.DIAMOND_SHOVEL));
	}

	@Override
	public List<Widget> setupDisplay(RollingMachineDisplay display, Rectangle bounds) {
		Point startPoint = new Point(bounds.getCenterX() - 58, bounds.getCenterY() - 27);
		List<Widget> widgets = new ArrayList<>();
		widgets.add(Widgets.createRecipeBase(bounds));
		widgets.add(ReiPlugin.createEnergyDisplay(new Rectangle(bounds.x + 8, bounds.y + 8, 14, 50), display.getEnergy(), ReiPlugin.EntryAnimation.downwards(5000), tooltipContext -> {
			List<Text> list = new ArrayList<>();
			list.add(Text.translatable("techreborn.jei.recipe.energy"));
			list.add(Text.translatable("techreborn.jei.recipe.running.cost", "E", display.getEnergy()).formatted(Formatting.GRAY));
			list.add(Text.translatable("techreborn.jei.recipe.generator.total", display.getEnergy() * display.getTime()).formatted(Formatting.GRAY));
			list.add(Text.of(""));
			list.add(ClientHelper.getInstance().getFormattedModFromIdentifier(Identifier.of("techreborn", "")));
			return Tooltip.create(tooltipContext.getPoint(), list);
		}));
		widgets.add(ReiPlugin.createProgressBar(startPoint.x + 68, startPoint.y + 22, display.getTime() * 50, GuiBuilder.ProgressDirection.RIGHT));
		widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 95, startPoint.y + 19)));
		List<InputIngredient<EntryStack<?>>> input = display.getInputIngredients(3, 3);
		List<Slot> slots = new ArrayList<>();
		for (int y = 0; y < 3; y++)
			for (int x = 0; x < 3; x++)
				slots.add(Widgets.createSlot(new Point(startPoint.x + 10 + x * 18, startPoint.y + 1 + y * 18)).markInput());
		for (InputIngredient<EntryStack<?>> ingredient : input) {
			slots.get(ingredient.getIndex()).entries(ingredient.get());
		}
		widgets.addAll(slots);
		widgets.add(Widgets.createSlot(new Point(startPoint.x + 95, startPoint.y + 19)).entries(display.getOutputEntries().get(0)).disableBackground().markOutput());
		widgets.add(Widgets.createLabel(new Point(bounds.getMaxX() - 5, bounds.y + 5), Text.translatable("techreborn.jei.recipe.processing.time.3", new DecimalFormat("###.##").format(display.getTime() / 20.0)))
			.shadow(false)
			.rightAligned()
			.color(0xFF404040, 0xFFBBBBBB)
		);
		return widgets;
	}
}

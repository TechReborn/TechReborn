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

package techreborn.client.compat.rei.fluidreplicator;

import com.google.common.collect.Lists;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.ClientHelper;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Tooltip;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import reborncore.client.gui.guibuilder.GuiBuilder;
import reborncore.common.crafting.RebornRecipeType;
import techreborn.api.recipe.recipes.FluidReplicatorRecipe;
import techreborn.client.compat.rei.ReiPlugin;

import java.text.DecimalFormat;
import java.util.List;

public class FluidReplicatorRecipeCategory implements DisplayCategory<FluidReplicatorRecipeDisplay> {

	private final RebornRecipeType<FluidReplicatorRecipe> rebornRecipeType;

	public FluidReplicatorRecipeCategory(RebornRecipeType<FluidReplicatorRecipe> recipeType) {
		this.rebornRecipeType = recipeType;
	}

	@Override
	public CategoryIdentifier<? extends FluidReplicatorRecipeDisplay> getCategoryIdentifier() {
		return CategoryIdentifier.of(rebornRecipeType.name());
	}

	@Override
	public Text getTitle() {
		return Text.translatable(rebornRecipeType.name().toString());
	}

	@Override
	public Renderer getIcon() {
		return EntryStacks.of(ReiPlugin.iconMap.getOrDefault(rebornRecipeType, () -> Items.DIAMOND_SHOVEL));
	}

	@Override
	public List<Widget> setupDisplay(FluidReplicatorRecipeDisplay recipeDisplay, Rectangle bounds) {
		Point startPoint = new Point(bounds.getCenterX() - 41, bounds.getCenterY() - 13);

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

		widgets.add(Widgets.createSlot(new Point(bounds.x + 46, bounds.y + 26)).entries(recipeDisplay.getInputEntries().get(0)).markInput());
		widgets.add(ReiPlugin.createProgressBar(bounds.x + 46 + 21, bounds.y + 30, recipeDisplay.getTime() * 50, GuiBuilder.ProgressDirection.RIGHT));
		widgets.add(ReiPlugin.createFluidDisplay(new Rectangle(bounds.x + 46 + 46, bounds.y + 8, 16, 50), recipeDisplay.getOutputEntries().get(0).get(0).cast(), ReiPlugin.EntryAnimation.upwards(5000)));

		widgets.add(Widgets.createLabel(new Point(bounds.x + 24, bounds.y + 5), Text.translatable("techreborn.jei.recipe.processing.time.3", new DecimalFormat("###.##").format(recipeDisplay.getTime() / 20.0)))
				.shadow(false)
				.leftAligned()
				.color(0xFF404040, 0xFFBBBBBB)
		);

		return widgets;
	}

}

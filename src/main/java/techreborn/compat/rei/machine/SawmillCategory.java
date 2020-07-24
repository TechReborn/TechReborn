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

public class SawmillCategory<R extends RebornRecipe> extends AbstractEnergyConsumingMachineCategory<R> {
	public SawmillCategory(RebornRecipeType<R> rebornRecipeType) {
		super(rebornRecipeType);
	}

	@Override
	public List<Widget> setupDisplay(MachineRecipeDisplay<R> recipeDisplay, Rectangle bounds) {
		List<Widget> widgets = super.setupDisplay(recipeDisplay, bounds);
		widgets.add(Widgets.createSlot(new Point(bounds.x + 55, bounds.y + 26)).entries(getInput(recipeDisplay, 0)).markInput());
		widgets.add(Widgets.createSlot(new Point(bounds.x + 55 + 46, bounds.y + 26 - 18)).entries(getOutput(recipeDisplay, 0)).markOutput());
		widgets.add(Widgets.createSlot(new Point(bounds.x + 55 + 46, bounds.y + 26)).entries(getOutput(recipeDisplay, 1)).markOutput());
		widgets.add(Widgets.createSlot(new Point(bounds.x + 55 + 46, bounds.y + 26 + 18)).entries(getOutput(recipeDisplay, 2)).markOutput());
		widgets.add(ReiPlugin.createProgressBar(bounds.x + 55 + 21, bounds.y + 30, recipeDisplay.getTime() * 50, GuiBuilder.ProgressDirection.RIGHT));
		widgets.add(ReiPlugin.createFluidDisplay(new Rectangle(bounds.x + 55 - 26, bounds.y + 8, 16, 50), getInput(recipeDisplay, 1).get(0), ReiPlugin.EntryAnimation.downwards(5000)));

		widgets.add(Widgets.createLabel(new Point(bounds.x + 51, bounds.y + 5), new TranslatableText("techreborn.jei.recipe.processing.time.3", new DecimalFormat("###.##").format(recipeDisplay.getTime() / 20.0)))
				.shadow(false)
				.leftAligned()
				.color(0xFF404040, 0xFFBBBBBB)
		);
		return widgets;
	}
}

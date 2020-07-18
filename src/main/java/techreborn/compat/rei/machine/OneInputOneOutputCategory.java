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

public class OneInputOneOutputCategory<R extends RebornRecipe> extends AbstractEnergyConsumingMachineCategory<R> {
	public OneInputOneOutputCategory(RebornRecipeType<R> rebornRecipeType) {
		super(rebornRecipeType);
	}

	@Override
	public List<Widget> setupDisplay(MachineRecipeDisplay<R> recipeDisplay, Rectangle bounds) {
		List<Widget> widgets = super.setupDisplay(recipeDisplay, bounds);
		widgets.add(Widgets.createSlot(new Point(bounds.x + 46, bounds.y + 26)).entries(getInput(recipeDisplay, 0)).markInput());
		widgets.add(Widgets.createResultSlotBackground(new Point(bounds.x + 46 + 46, bounds.y + 26)));
		widgets.add(Widgets.createSlot(new Point(bounds.x + 46 + 46, bounds.y + 26)).entries(getOutput(recipeDisplay, 0)).disableBackground().markOutput());
		widgets.add(ReiPlugin.createProgressBar(bounds.x + 46 + 21, bounds.y + 30, recipeDisplay.getTime() * 50, GuiBuilder.ProgressDirection.RIGHT));

		widgets.add(Widgets.createLabel(new Point(bounds.getMaxX() - 5, bounds.y + 5), new TranslatableText("techreborn.jei.recipe.processing.time.3", new DecimalFormat("###.##").format(recipeDisplay.getTime() / 20.0)))
				.shadow(false)
				.rightAligned()
				.color(0xFF404040, 0xFFBBBBBB)
		);
		return widgets;
	}
}

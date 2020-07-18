package techreborn.compat.rei.machine;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.widgets.Widgets;
import me.shedaniel.rei.gui.widget.Widget;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import reborncore.client.gui.guibuilder.GuiBuilder;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.crafting.RebornRecipeType;
import techreborn.compat.rei.MachineRecipeDisplay;
import techreborn.compat.rei.ReiPlugin;

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

		Text neededHeat = new LiteralText(String.valueOf(recipeDisplay.getHeat())).append(" ").append(new TranslatableText("techreborn.jei.recipe.heat"));
		widgets.add(Widgets.createLabel(new Point(bounds.getMaxX() - 5, bounds.y + 5), neededHeat)
				.shadow(false)
				.rightAligned()
				.color(0xFF404040, 0xFFBBBBBB)
		);

		widgets.add(Widgets.createLabel(new Point(bounds.x + 24, bounds.y + 5), new TranslatableText("techreborn.jei.recipe.processing.time.3", new DecimalFormat("###.##").format(recipeDisplay.getTime() / 20.0)))
				.shadow(false)
				.leftAligned()
				.color(0xFF404040, 0xFFBBBBBB)
		);
		return widgets;
	}
}

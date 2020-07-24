package techreborn.compat.rei.machine;

import com.google.common.collect.Lists;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.ClientHelper;
import me.shedaniel.rei.api.widgets.Tooltip;
import me.shedaniel.rei.api.widgets.Widgets;
import me.shedaniel.rei.gui.widget.Widget;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.crafting.RebornRecipeType;
import techreborn.compat.rei.MachineRecipeDisplay;
import techreborn.compat.rei.ReiPlugin;

import java.util.List;

public abstract class AbstractEnergyConsumingMachineCategory<R extends RebornRecipe> extends AbstractMachineCategory<R> {
	public AbstractEnergyConsumingMachineCategory(RebornRecipeType<R> rebornRecipeType) {
		super(rebornRecipeType);
	}

	@Override
	public List<Widget> setupDisplay(MachineRecipeDisplay<R> recipeDisplay, Rectangle bounds) {
		List<Widget> widgets = Lists.newArrayList();
		widgets.add(Widgets.createRecipeBase(bounds));
		widgets.add(ReiPlugin.createEnergyDisplay(new Rectangle(bounds.x + 8, bounds.y + 8, 14, 50), recipeDisplay.getEnergy(), ReiPlugin.EntryAnimation.downwards(5000), point -> {
			List<Text> list = Lists.newArrayList();
			list.add(Text.method_30163("Energy"));
			list.add(new TranslatableText("techreborn.jei.recipe.running.cost", "E", recipeDisplay.getEnergy()).formatted(Formatting.GRAY));
			list.add(Text.method_30163(""));
			list.add(ClientHelper.getInstance().getFormattedModFromIdentifier(new Identifier("techreborn", "")));
			return Tooltip.create(point, list);
		}));
		return widgets;
	}
}

package techreborn.rei;

import me.shedaniel.rei.api.RecipeCategory;
import me.shedaniel.rei.api.Renderable;
import me.shedaniel.rei.api.Renderer;
import me.shedaniel.rei.gui.renderables.RecipeRenderer;
import me.shedaniel.rei.gui.widget.RecipeBaseWidget;
import me.shedaniel.rei.gui.widget.SlotWidget;
import me.shedaniel.rei.gui.widget.Widget;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.crafting.RebornRecipeType;
import reborncore.common.util.StringUtils;

import java.awt.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class MachineRecipeCategory<R extends RebornRecipe> implements RecipeCategory<MachineRecipeDisplay<R>> {

	private final RebornRecipeType<R> rebornRecipeType;

	public MachineRecipeCategory(RebornRecipeType<R> rebornRecipeType) {
		this.rebornRecipeType = rebornRecipeType;
	}

	@Override
	public Identifier getIdentifier() {
		return rebornRecipeType.getName();
	}

	@Override
	public String getCategoryName() {
		return StringUtils.t(rebornRecipeType.getName().toString());
	}

	@Override
	public Renderer getIcon() {
		return Renderable.fromItemStack(new ItemStack(ReiPlugin.iconMap.getOrDefault(rebornRecipeType, () -> Items.DIAMOND_SHOVEL).asItem()));
	}

	@Override
	public RecipeRenderer getSimpleRenderer(MachineRecipeDisplay<R> recipe) {
		return Renderable.fromRecipe(() -> Collections.singletonList(recipe.getInput().get(0)), recipe::getOutput);
	}

	@Override
	public List<Widget> setupDisplay(Supplier<MachineRecipeDisplay<R>> recipeDisplaySupplier, Rectangle bounds) {

		MachineRecipeDisplay<R> machineRecipe = recipeDisplaySupplier.get();

		Point startPoint = new Point((int) bounds.getCenterX() - 41, (int) bounds.getCenterY() - 27);
		List<Widget> widgets = new LinkedList<>();
		widgets.add(new RecipeBaseWidget(bounds));

		int i = 0;
		for (List<ItemStack> inputs : machineRecipe.getInput()){
			widgets.add(new SlotWidget(startPoint.x + 1, startPoint.y + 1 + (i++ * 20), inputs, true, true, true));
		}

		i = 0;
		for (ItemStack outputs : machineRecipe.getOutput()){
			widgets.add(new SlotWidget(startPoint.x + 61, startPoint.y + 1 + (i++ * 20), Collections.singletonList(outputs), true, true, true));
		}

		return widgets;
	}
}

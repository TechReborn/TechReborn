package techreborn.compat.rei.fluidreplicator;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

import me.shedaniel.math.api.Point;
import me.shedaniel.math.api.Rectangle;
import me.shedaniel.rei.api.RecipeCategory;
import me.shedaniel.rei.api.Renderer;
import me.shedaniel.rei.gui.widget.LabelWidget;
import me.shedaniel.rei.gui.widget.RecipeArrowWidget;
import me.shedaniel.rei.gui.widget.RecipeBaseWidget;
import me.shedaniel.rei.gui.widget.SlotWidget;
import me.shedaniel.rei.gui.widget.Widget;
import me.shedaniel.rei.impl.ScreenHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import reborncore.common.crafting.RebornRecipeType;
import reborncore.common.fluid.container.FluidInstance;
import reborncore.common.util.StringUtils;
import techreborn.api.recipe.recipes.FluidReplicatorRecipe;
import techreborn.compat.rei.ReiPlugin;

public class FluidReplicatorRecipeCategory implements RecipeCategory<FluidReplicatorRecipeDisplay> {
	
	private final RebornRecipeType<FluidReplicatorRecipe> rebornRecipeType;
	
	public FluidReplicatorRecipeCategory(RebornRecipeType<FluidReplicatorRecipe> recipeType) {
		this.rebornRecipeType = recipeType;
		
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
		return Renderer.fromItemStack(new ItemStack(ReiPlugin.iconMap.getOrDefault(rebornRecipeType, () -> Items.DIAMOND_SHOVEL).asItem()));
	}
	
	@Override
	public List<Widget> setupDisplay(Supplier<FluidReplicatorRecipeDisplay> recipeDisplaySupplier, Rectangle bounds) {

		FluidReplicatorRecipeDisplay machineRecipe = recipeDisplaySupplier.get();

		Point startPoint = new Point( bounds.getCenterX() - 41, bounds.getCenterY() - 13);

        List<Widget> widgets = new LinkedList<>();
        widgets.add(new RecipeBaseWidget(bounds));
        widgets.add(new RecipeArrowWidget(startPoint.x + 24, startPoint.y + 1, true));

		int i = 0;
		for (List<ItemStack> inputs : machineRecipe.getInput()){
			widgets.add(new SlotWidget(startPoint.x + 1, startPoint.y + 1 + (i++ * 20), Renderer.fromItemStacks(inputs), true, true, true));
		}
		
		Text energyPerTick = new TranslatableText("techreborn.jei.recipe.running.cost", "E", machineRecipe.getEnergy());
		widgets.add(new LabelWidget(startPoint.x + 1, startPoint.y + 1 + (i++ * 20), energyPerTick.asFormattedString()){
			@Override
			public void render(int mouseX, int mouseY, float delta) {
				font.draw(text, x - font.getStringWidth(text) / 2, y, ScreenHelper.isDarkModeEnabled() ? 0xFFBBBBBB : 0xFF404040);
			}
		});

		FluidInstance fluidInstance = machineRecipe.getFluidInstance();
		if (fluidInstance != null) {
			widgets.add(new SlotWidget(startPoint.x + 61, startPoint.y + 1, Renderer.fromFluid(fluidInstance.getFluid()), true, true, true));
		}
	
		return widgets;
	}

}

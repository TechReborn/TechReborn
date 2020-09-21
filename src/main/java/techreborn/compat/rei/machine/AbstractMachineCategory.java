package techreborn.compat.rei.machine;

import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeCategory;
import me.shedaniel.rei.gui.entries.RecipeEntry;
import me.shedaniel.rei.gui.entries.SimpleRecipeEntry;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.crafting.RebornRecipeType;
import techreborn.compat.rei.MachineRecipeDisplay;
import techreborn.compat.rei.ReiPlugin;

import java.util.Collections;
import java.util.List;

public abstract class AbstractMachineCategory<R extends RebornRecipe> implements RecipeCategory<MachineRecipeDisplay<R>> {
	private final RebornRecipeType<R> rebornRecipeType;

	public AbstractMachineCategory(RebornRecipeType<R> rebornRecipeType) {
		this.rebornRecipeType = rebornRecipeType;
	}

	@Override
	public Identifier getIdentifier() {
		return rebornRecipeType.getName();
	}

	@Override
	public String getCategoryName() {
		return I18n.translate(rebornRecipeType.getName().toString());
	}

	@Override
	public EntryStack getLogo() {
		return EntryStack.create(ReiPlugin.iconMap.getOrDefault(rebornRecipeType, () -> Items.DIAMOND_SHOVEL));
	}

	@Override
	public RecipeEntry getSimpleRenderer(MachineRecipeDisplay<R> recipe) {
		return SimpleRecipeEntry.create(Collections.singletonList(recipe.getInputEntries().get(0)), recipe.getOutputEntries());
	}

	public List<EntryStack> getInput(MachineRecipeDisplay<R> recipeDisplay, int index) {
		List<List<EntryStack>> inputs = recipeDisplay.getInputEntries();
		return inputs.size() > index ? inputs.get(index) : Collections.emptyList();
	}

	public List<EntryStack> getOutput(MachineRecipeDisplay<R> recipeDisplay, int index) {
		List<EntryStack> outputs = recipeDisplay.getOutputEntries();
		return outputs.size() > index ? Collections.singletonList(outputs.get(index)) : Collections.emptyList();
	}
}

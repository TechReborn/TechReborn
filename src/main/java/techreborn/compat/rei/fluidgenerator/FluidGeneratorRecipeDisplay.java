package techreborn.compat.rei.fluidgenerator;

import com.google.common.collect.Lists;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeDisplay;
import net.minecraft.util.Identifier;
import techreborn.api.generator.FluidGeneratorRecipe;

import java.util.Collections;
import java.util.List;

public class FluidGeneratorRecipeDisplay implements RecipeDisplay {
	
	private List<List<EntryStack>> inputs;
	private Identifier category;
	private int totalEnergy;
	
	public FluidGeneratorRecipeDisplay(FluidGeneratorRecipe recipe, Identifier category) {
		this.category = category;
		this.inputs = Lists.newArrayList();
		this.totalEnergy = recipe.getEnergyPerBucket();
		inputs.add(Collections.singletonList(EntryStack.create(recipe.getFluid(), 1000)));
	}
	
	@Override
	public List<List<EntryStack>> getInputEntries() {
		return inputs;
	}
	
	@Override
	public List<EntryStack> getOutputEntries() {
		return Lists.newArrayList();
	}
	
	@Override
	public Identifier getRecipeCategory() {
		return category;
	}
	
	public int getTotalEnergy() {
		return totalEnergy;
	}
	
}

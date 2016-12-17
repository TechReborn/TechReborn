package techreborn.api.generator;

import com.google.common.collect.Sets;

import java.util.HashSet;
import java.util.Optional;

import net.minecraftforge.fluids.Fluid;

public class FluidGeneratorRecipeList {
	private HashSet<FluidGeneratorRecipe> recipes;
	
	public FluidGeneratorRecipeList(FluidGeneratorRecipe... recipes)
	{
		this.recipes = Sets.newHashSet(recipes);
	}
	
	public boolean addRecipe(FluidGeneratorRecipe fluidGeneratorRecipe) {
		if(!this.getRecipeForFluid(fluidGeneratorRecipe.getFluid()).isPresent())
			return this.getRecipes().add(fluidGeneratorRecipe);
		return false;
	}
	
	public boolean removeRecipe(FluidGeneratorRecipe fluidGeneratorRecipe)
	{
		return this.getRecipes().remove(fluidGeneratorRecipe);
	}
	
	public Optional<FluidGeneratorRecipe> getRecipeForFluid(Fluid fluid)
	{
		return this.recipes.stream().filter(recipe -> recipe.getFluid().equals(fluid)).findAny();
	}

	public HashSet<FluidGeneratorRecipe> getRecipes() {
		return recipes;
	}

	public void setRecipes(HashSet<FluidGeneratorRecipe> recipes) {
		this.recipes = recipes;
	}

	@Override
	public String toString() {
		return "FluidGeneratorRecipeList [recipes=" + recipes + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((recipes == null) ? 0 : recipes.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FluidGeneratorRecipeList other = (FluidGeneratorRecipeList) obj;
		if (recipes == null) {
			if (other.recipes != null)
				return false;
		} else if (!recipes.equals(other.recipes))
			return false;
		return true;
	}
}

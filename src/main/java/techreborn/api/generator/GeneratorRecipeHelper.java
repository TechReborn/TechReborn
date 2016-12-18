package techreborn.api.generator;

import java.util.EnumMap;

import net.minecraftforge.fluids.Fluid;

public class GeneratorRecipeHelper {

	/**
	 * This EnumMap store all the energy recipes for the fluid generators. Each
	 * value of the EFluidGenerator enum is linked to an object holding a set of
	 * FluidGeneratorRecipe.
	 */
	public static EnumMap<EFluidGenerator, FluidGeneratorRecipeList> fluidRecipes = new EnumMap<>(
			EFluidGenerator.class);

	/**
	 * Register a Fluid energy recipe.
	 * 
	 * @param generatorType
	 *            A value of the EFluidGenerator type in which the fluid is
	 *            allowed to be consumed.
	 * @param fluidType
	 * @param energyPerMb
	 *            Represent the energy / MILLI_BUCKET the fluid will produce.
	 *            Some generators use this value to alter their fluid decay
	 *            speed to match their maximum energy output.
	 */
	public static void registerFluidRecipe(EFluidGenerator generatorType, Fluid fluidType, int energyPerMb) {
		fluidRecipes.putIfAbsent(generatorType, new FluidGeneratorRecipeList());
		fluidRecipes.get(generatorType).addRecipe(new FluidGeneratorRecipe(fluidType, energyPerMb, generatorType));
	}

	/**
	 * 
	 * @param generatorType
	 *            A value of the EFluidGenerator type in which the fluid is
	 *            allowed to be consumed.
	 * @return An object holding a set of availables recipes for this type of
	 *         FluidGenerator.
	 */
	public static FluidGeneratorRecipeList getFluidRecipesForGenerator(EFluidGenerator generatorType) {
		return fluidRecipes.get(generatorType);
	}
}

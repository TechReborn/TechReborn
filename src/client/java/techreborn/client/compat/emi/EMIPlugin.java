package techreborn.client.compat.emi;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.EmiRecipeSorting;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.util.Identifier;
import reborncore.common.crafting.RebornRecipe;
import techreborn.TechReborn;
import techreborn.client.compat.emi.machine.OneInputOneOutputRecipe;
import techreborn.init.ModRecipes;
import techreborn.init.TRContent;

public class EMIPlugin implements EmiPlugin {
	public static final EmiRecipeCategory COMPRESSOR_CATEGORY =
		new EmiRecipeCategory(
			Identifier.of(TechReborn.MOD_ID, TRContent.Machine.COMPRESSOR.name),
			EmiStack.of(TRContent.Machine.COMPRESSOR),
			EmiStack.of(TRContent.Machine.COMPRESSOR),
			EmiRecipeSorting.compareOutputThenInput()
		);

	@Override
	public void register(EmiRegistry registry) {
		registry.addCategory(COMPRESSOR_CATEGORY);

		registry.addWorkstation(COMPRESSOR_CATEGORY, EmiStack.of(TRContent.Machine.COMPRESSOR));
		for (RecipeEntry<RebornRecipe> recipe : registry.getRecipeManager().listAllOfType(ModRecipes.COMPRESSOR)) {
			registry.addRecipe(new OneInputOneOutputRecipe<>(recipe, COMPRESSOR_CATEGORY));
		}
	}
}

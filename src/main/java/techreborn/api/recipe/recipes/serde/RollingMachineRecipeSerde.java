package techreborn.api.recipe.recipes.serde;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import reborncore.common.crafting.RebornRecipeType;
import reborncore.common.crafting.ingredient.RebornIngredient;
import reborncore.common.crafting.serde.RebornRecipeSerde;
import techreborn.api.recipe.recipes.RollingMachineRecipe;

import java.util.Collections;
import java.util.List;

public class RollingMachineRecipeSerde extends RebornRecipeSerde<RollingMachineRecipe> {
	@Override
	protected RollingMachineRecipe fromJson(JsonObject jsonObject, RebornRecipeType<RollingMachineRecipe> type, Identifier name, List<RebornIngredient> ingredients, List<ItemStack> outputs, int power, int time) {
		final JsonObject shapedRecipeJson = JsonHelper.getObject(jsonObject, "shaped");
		final ShapedRecipe shapedRecipe = RecipeSerializer.SHAPED.read(name, shapedRecipeJson);
		return new RollingMachineRecipe(type, name, ingredients, outputs, power, time, shapedRecipe, shapedRecipeJson);
	}

	@Override
	public void collectJsonData(RollingMachineRecipe recipe, JsonObject jsonObject) {
		jsonObject.add("shaped", recipe.getShapedRecipeJson());
	}

	@Override
	protected List<RebornIngredient> getIngredients(JsonObject jsonObject) {
		// Inputs are handled by the shaped recipe.
		return Collections.emptyList();
	}

	@Override
	protected List<ItemStack> getOutputs(JsonObject jsonObject) {
		// Outputs are handled by the shaped recipe.
		return Collections.emptyList();
	}

	@Override
	protected void writeIngredients(RollingMachineRecipe recipe, JsonObject jsonObject) {
	}

	@Override
	protected void writeOutputs(RollingMachineRecipe recipe, JsonObject jsonObject) {
	}
}

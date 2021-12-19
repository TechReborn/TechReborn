package techreborn.api.recipe.recipes;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import reborncore.common.crafting.RebornRecipeType;
import reborncore.common.crafting.ingredient.RebornIngredient;
import reborncore.common.crafting.serde.RebornRecipeSerde;

import java.util.List;

public class RollingMachineRecipeSerde extends RebornRecipeSerde<RollingMachineRecipe> {
	@Override
	protected RollingMachineRecipe fromJson(JsonObject jsonObject, RebornRecipeType<RollingMachineRecipe> type, Identifier name, List<RebornIngredient> ingredients, List<ItemStack> outputs, int power, int time) {
		final JsonObject json = JsonHelper.getObject(jsonObject, "shaped");
		final ShapedRecipe shapedRecipe = RecipeSerializer.SHAPED.read(name, json);
		return new RollingMachineRecipe(type, name, ingredients, outputs, power, time, shapedRecipe, json);
	}

	@Override
	public void toJson(RollingMachineRecipe recipe, JsonObject jsonObject) {
		super.toJson(recipe, jsonObject);

		if (recipe.getShapedRecipeJson() != null) {
			jsonObject.add("shaped", recipe.getShapedRecipeJson());
		}
	}

	@Override
	public RollingMachineRecipe createDummy(RebornRecipeType<RollingMachineRecipe> type, Identifier name) {
		return new RollingMachineRecipe(type, name);
	}
}

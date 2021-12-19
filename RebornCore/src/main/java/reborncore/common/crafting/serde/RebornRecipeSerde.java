package reborncore.common.crafting.serde;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.crafting.RebornRecipeType;
import reborncore.common.crafting.ingredient.RebornIngredient;

import java.util.List;

public abstract class RebornRecipeSerde<R extends RebornRecipe> extends AbstractRecipeSerde<R> {
	public static final RebornRecipeSerde<RebornRecipe> BASIC = new RebornRecipeSerde<>() {
		@Override
		protected RebornRecipe fromJson(JsonObject jsonObject, RebornRecipeType<RebornRecipe> type, Identifier name, List<RebornIngredient> ingredients, List<ItemStack> outputs, int power, int time) {
			return new RebornRecipe(type, name, ingredients, outputs, power, time);
		}

		@Override
		public RebornRecipe createDummy(RebornRecipeType<RebornRecipe> type, Identifier name) {
			return new RebornRecipe(type, name);
		}
	};

	protected abstract R fromJson(JsonObject jsonObject, RebornRecipeType<R> type, Identifier name, List<RebornIngredient> ingredients, List<ItemStack> outputs, int power, int time);

	@Override
	public final R fromJson(JsonObject jsonObject, RebornRecipeType<R> type, Identifier name) {
		if (jsonObject.has("dummy")) {
			return createDummy(type, name);
		}

		final int power = getPower(jsonObject);
		final int time = getTime(jsonObject);
		final List<RebornIngredient> ingredients = getIngredients(jsonObject);
		final List<ItemStack> outputs = getOutputs(jsonObject);

		return fromJson(jsonObject, type, name, ingredients, outputs, power, time);
	}

	@Override
	public void toJson(R recipe, JsonObject jsonObject) {
		if (recipe.isDummy()) {
			jsonObject.addProperty("dummy", true);
			return;
		}

		writePower(recipe, jsonObject);
		writeTime(recipe, jsonObject);
		writeIngredients(recipe, jsonObject);
		writeOutputs(recipe, jsonObject);
	}
}

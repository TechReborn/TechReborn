package reborncore.common.crafting.serde;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.crafting.RebornRecipeType;
import reborncore.common.crafting.RecipeUtils;
import reborncore.common.crafting.ingredient.IngredientManager;
import reborncore.common.crafting.ingredient.RebornIngredient;
import reborncore.common.util.DefaultedListCollector;
import reborncore.common.util.serialization.SerializationUtil;

import java.util.List;

public abstract class AbstractRecipeSerde<R extends RebornRecipe> implements RecipeSerde<R> {
	@Override
	public void writeToPacket(PacketByteBuf buffer, R recipe) {
		final JsonObject jsonObject = new JsonObject();
		toJson(recipe, jsonObject);

		final String jsonStr = SerializationUtil.GSON_FLAT.toJson(jsonObject);

		buffer.writeInt(jsonStr.length());
		buffer.writeString(jsonStr);

		recipe.serialize(buffer);
	}

	@Override
	public R fromPacket(PacketByteBuf buffer, RebornRecipeType<R> type) {
		final Identifier identifier = new Identifier(buffer.readString(buffer.readInt()));
		final String input = buffer.readString(buffer.readInt());
		final JsonObject jsonObject = SerializationUtil.GSON_FLAT.fromJson(input, JsonObject.class);

		final R r = fromJson(jsonObject, type, identifier);
		r.deserialize(buffer);

		return r;
	}

	protected int getPower(JsonObject jsonObject) {
		return JsonHelper.getInt(jsonObject, "power");
	}

	protected int getTime(JsonObject jsonObject) {
		return JsonHelper.getInt(jsonObject, "time");
	}

	protected List<RebornIngredient> getIngredients(JsonObject jsonObject) {
		return SerializationUtil.stream(JsonHelper.getArray(jsonObject, "ingredients"))
				.map(IngredientManager::deserialize)
				.collect(DefaultedListCollector.toList());
	}

	protected List<ItemStack> getOutputs(JsonObject jsonObject) {
		final JsonArray resultsJson = JsonHelper.getArray(jsonObject, "results");
		return RecipeUtils.deserializeItems(resultsJson);
	}

	protected void writeIngredients(R recipe, JsonObject jsonObject) {
		final JsonArray ingredientsArray = new JsonArray();
		recipe.getRebornIngredients().stream().map(RebornIngredient::witeToJson).forEach(ingredientsArray::add);
		jsonObject.add("ingredients", ingredientsArray);
	}

	protected void writeOutputs(R recipe, JsonObject jsonObject) {
		final JsonArray resultsArray = new JsonArray();

		for (ItemStack stack : recipe.getOutputs()) {
			final JsonObject stackObject = new JsonObject();
			stackObject.addProperty("item", Registry.ITEM.getId(stack.getItem()).toString());

			if (stack.getCount() > 1) {
				stackObject.addProperty("count", stack.getCount());
			}

			if (stack.hasNbt()) {
				stackObject.add("nbt", Dynamic.convert(NbtOps.INSTANCE, JsonOps.INSTANCE, stack.getNbt()));
			}
			resultsArray.add(stackObject);
		}

		jsonObject.add("results", resultsArray);
	}

	protected void writePower(R recipe, JsonObject jsonObject) {
		jsonObject.addProperty("power", recipe.getPower());
	}

	protected void writeTime(R recipe, JsonObject jsonObject) {
		jsonObject.addProperty("time", recipe.getTime());
	}
}

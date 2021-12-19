package reborncore.common.crafting.serde;

import com.google.gson.JsonObject;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;
import reborncore.common.crafting.RebornFluidRecipe;
import reborncore.common.crafting.RebornRecipeType;
import reborncore.common.crafting.ingredient.RebornIngredient;
import reborncore.common.fluid.FluidValue;
import reborncore.common.fluid.container.FluidInstance;

import java.util.List;

public abstract class RebornFluidRecipeSerde<R extends RebornFluidRecipe> extends RebornRecipeSerde<R> {
	protected abstract R fromJson(JsonObject jsonObject, RebornRecipeType<R> type, Identifier name, List<RebornIngredient> ingredients, List<ItemStack> outputs, int power, int time, @Nullable FluidInstance fluidInstance);

	@Override
	protected final R fromJson(JsonObject jsonObject, RebornRecipeType<R> type, Identifier name, List<RebornIngredient> ingredients, List<ItemStack> outputs, int power, int time) {
		final JsonObject tank = JsonHelper.getObject(jsonObject, "tank");
		final Identifier identifier = new Identifier(JsonHelper.getString(tank, "fluid"));
		final Fluid fluid = Registry.FLUID.get(identifier);

		FluidValue value = FluidValue.BUCKET;
		if(tank.has("amount")){
			value = FluidValue.parseFluidValue(tank.get("amount"));
		}

		FluidInstance fluidInstance = new FluidInstance(fluid, value);

		return fromJson(jsonObject, type, name, ingredients, outputs, power, time, fluidInstance);
	}

	@Override
	public void toJson(R recipe, JsonObject jsonObject) {
		super.toJson(recipe, jsonObject);

		final JsonObject tankObject = new JsonObject();
		tankObject.addProperty("fluid", Registry.FLUID.getId(recipe.getFluidInstance().getFluid()).toString());
		tankObject.addProperty("value", recipe.getFluidInstance().getAmount().getRawValue());

		jsonObject.add("tank", tankObject);
	}
}

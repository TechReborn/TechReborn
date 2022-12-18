/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 TeamReborn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package reborncore.common.crafting.serde;

import com.google.gson.JsonObject;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.jetbrains.annotations.NotNull;
import reborncore.common.crafting.RebornFluidRecipe;
import reborncore.common.crafting.RebornRecipeType;
import reborncore.common.crafting.ingredient.RebornIngredient;
import reborncore.common.fluid.FluidValue;
import reborncore.common.fluid.container.FluidInstance;

import java.util.List;

public abstract class RebornFluidRecipeSerde<R extends RebornFluidRecipe> extends RebornRecipeSerde<R> {
	protected abstract R fromJson(JsonObject jsonObject, RebornRecipeType<R> type, Identifier name, List<RebornIngredient> ingredients, List<ItemStack> outputs, int power, int time, @NotNull FluidInstance fluidInstance);

	@Override
	protected final R fromJson(JsonObject jsonObject, RebornRecipeType<R> type, Identifier name, List<RebornIngredient> ingredients, List<ItemStack> outputs, int power, int time) {
		final JsonObject tank = JsonHelper.getObject(jsonObject, "tank");
		final Identifier identifier = new Identifier(JsonHelper.getString(tank, "fluid"));
		final Fluid fluid = Registries.FLUID.get(identifier);

		FluidValue value = FluidValue.BUCKET;
		if (tank.has("amount")){
			value = FluidValue.parseFluidValue(tank.get("amount"));
		}

		FluidInstance fluidInstance = new FluidInstance(fluid, value);

		return fromJson(jsonObject, type, name, ingredients, outputs, power, time, fluidInstance);
	}

	@Override
	public void collectJsonData(R recipe, JsonObject jsonObject, boolean networkSync) {
		final JsonObject tankObject = new JsonObject();
		tankObject.addProperty("fluid", Registries.FLUID.getId(recipe.getFluidInstance().getFluid()).toString());

		var amountObject = new JsonObject();
		amountObject.addProperty("droplets", recipe.getFluidInstance().getAmount().getRawValue());
		tankObject.add("amount", amountObject);

		jsonObject.add("tank", tankObject);
	}

	public static <R extends RebornFluidRecipe> RebornFluidRecipeSerde<R> create(SimpleFluidRecipeFactory<R> factory) {
		return new RebornFluidRecipeSerde<>() {
			@Override
			protected R fromJson(JsonObject jsonObject, RebornRecipeType<R> type, Identifier name, List<RebornIngredient> ingredients, List<ItemStack> outputs, int power, int time, @NotNull FluidInstance fluidInstance) {
				return factory.create(type, name, ingredients, outputs, power, time, fluidInstance);
			}
		};
	}

	public interface SimpleFluidRecipeFactory<R extends RebornFluidRecipe> {
		R create(RebornRecipeType<R> type, Identifier name, List<RebornIngredient> ingredients, List<ItemStack> outputs, int power, int time, @NotNull FluidInstance fluidInstance);
	}
}

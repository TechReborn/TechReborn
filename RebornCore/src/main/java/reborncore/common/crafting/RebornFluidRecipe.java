/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2021 TeamReborn
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

package reborncore.common.crafting;

import com.google.gson.JsonObject;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;
import reborncore.common.crafting.ingredient.RebornIngredient;
import reborncore.common.fluid.FluidValue;
import reborncore.common.fluid.container.FluidInstance;
import net.minecraft.util.collection.DefaultedList;
import reborncore.common.util.Tank;

import org.jetbrains.annotations.NotNull;

public abstract class RebornFluidRecipe extends RebornRecipe {

	@NotNull
	private FluidInstance fluidInstance = FluidInstance.EMPTY;

	public RebornFluidRecipe(RebornRecipeType<?> type, Identifier name) {
		super(type, name);
	}

	public RebornFluidRecipe(RebornRecipeType<?> type, Identifier name, DefaultedList<RebornIngredient> ingredients, DefaultedList<ItemStack> outputs, int power, int time) {
		super(type, name, ingredients, outputs, power, time);
	}

	public RebornFluidRecipe(RebornRecipeType<?> type, Identifier name, DefaultedList<RebornIngredient> ingredients, DefaultedList<ItemStack> outputs, int power, int time, FluidInstance fluidInstance) {
		this(type, name, ingredients, outputs, power, time);
		this.fluidInstance = fluidInstance;
	}

	@Override
	public void deserialize(JsonObject jsonObject) {
		super.deserialize(jsonObject);
		if(jsonObject.has("tank")){
			JsonObject tank = jsonObject.get("tank").getAsJsonObject();

			Identifier identifier = new Identifier(JsonHelper.getString(tank, "fluid"));
			Fluid fluid = Registry.FLUID.get(identifier);

			FluidValue value = FluidValue.BUCKET;
			if(tank.has("amount")){
				value = FluidValue.parseFluidValue(tank.get("amount"));
			}

			fluidInstance = new FluidInstance(fluid, value);
		}
	}

	@Override
	public void serialize(JsonObject jsonObject) {
		super.serialize(jsonObject);

		JsonObject tankObject = new JsonObject();
		tankObject.addProperty("fluid", Registry.FLUID.getId(fluidInstance.getFluid()).toString());
		tankObject.addProperty("value", fluidInstance.getAmount().getRawValue());

		jsonObject.add("tank", tankObject);
	}

	public abstract Tank getTank(BlockEntity be);

	@Override
	public boolean canCraft(BlockEntity be) {
		final FluidInstance recipeFluid = fluidInstance;
		final FluidInstance tankFluid = getTank(be).getFluidInstance();
		if (fluidInstance.isEmpty()) {
			return true;
		}
		if (tankFluid.isEmpty()) {
			return false;
		}
		if (tankFluid.getFluid().equals(recipeFluid.getFluid())) {
			if (tankFluid.getAmount().equalOrMoreThan(recipeFluid.getAmount())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onCraft(BlockEntity be) {
		final FluidInstance recipeFluid = fluidInstance;
		final FluidInstance tankFluid = getTank(be).getFluidInstance();
		if (fluidInstance.isEmpty()) {
			return true;
		}
		if (tankFluid.isEmpty()) {
			return false;
		}
		if (tankFluid.getFluid().equals(recipeFluid.getFluid())) {
			if (tankFluid.getAmount().equalOrMoreThan(recipeFluid.getAmount())) {
				tankFluid.subtractAmount(recipeFluid.getAmount());
				return true;
			}
		}
		return false;
	}

	@NotNull
	public FluidInstance getFluidInstance() {
		return fluidInstance;
	}
}

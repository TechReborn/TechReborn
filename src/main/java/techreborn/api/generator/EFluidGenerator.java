/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 TechReborn
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

package techreborn.api.generator;

import com.google.gson.JsonObject;
import net.minecraft.fluid.Fluid;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.jetbrains.annotations.NotNull;

import static techreborn.TechReborn.MOD_ID;

public enum EFluidGenerator {
	THERMAL("TechReborn.ThermalGenerator", "thermal_generator"),
	GAS("TechReborn.GasGenerator", "gas_generator"),
	DIESEL("TechReborn.DieselGenerator", "diesel_generator"),
	SEMIFLUID("TechReborn.SemifluidGenerator", "semi_fluid_generator"),
	PLASMA("TechReborn.PlasmaGenerator", "plasma_generator");

	@NotNull
	private final String recipeID;
	private final Identifier id;
	private final RecipeType<FluidGeneratorRecipe> type;
	private final Serializer serializer;

	EFluidGenerator(@NotNull String recipeID, String type) {
		this.recipeID = recipeID;
		this.id = Identifier.of(MOD_ID, type);
		this.type = new RecipeType<>() {
			@Override
			public String toString() {
				return type;
			}
		};
		this.serializer = new Serializer(this);
	}

	public static void register() {
		for (EFluidGenerator generator : EFluidGenerator.values()) {
			Registry.register(Registries.RECIPE_TYPE, generator.id, generator.type);
			Registry.register(Registries.RECIPE_SERIALIZER, generator.id, generator.serializer);
		}
	}

	@NotNull
	public String getRecipeID() {
		return recipeID;
	}

	@NotNull
	public Identifier getId() {
		return id;
	}

	public RecipeType<FluidGeneratorRecipe> getType() {
		return type;
	}

	public Serializer getSerializer() {
		return serializer;
	}

	public static class Serializer implements RecipeSerializer<FluidGeneratorRecipe> {
		private final EFluidGenerator type;
		public Serializer(EFluidGenerator type) {
			this.type = type;
		}

		@Override
		public FluidGeneratorRecipe read(Identifier id, JsonObject json) {
			Fluid fluid = Registries.FLUID.get(new Identifier(JsonHelper.getString(json, "fluid")));
			int power = JsonHelper.getInt(json, "power");
			FluidGeneratorRecipe recipe = new FluidGeneratorRecipe(fluid, power, type);
			GeneratorRecipeHelper.recipeIds.put(recipe, id);
			return recipe;
		}

		@Override
		public FluidGeneratorRecipe read(Identifier id, PacketByteBuf buf) {
			Fluid fluid = Registries.FLUID.get(buf.readInt());
			int power = buf.readInt();
			FluidGeneratorRecipe recipe = new FluidGeneratorRecipe(fluid, power, type);
			GeneratorRecipeHelper.recipeIds.put(recipe, id);
			return recipe;
		}

		@Override
		public void write(PacketByteBuf buf, FluidGeneratorRecipe recipe) {
			buf.writeInt(Registries.FLUID.getRawId(recipe.fluid()));
			buf.writeInt(recipe.energyPerMb());
		}
	}
}

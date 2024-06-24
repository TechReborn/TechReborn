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

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Function;

public class RecipeManager {
	public static RecipeType<RebornRecipe> newRecipeType(Identifier name) {
		return newRecipeType(name, RebornRecipe.CODEC, RebornRecipe.PACKET_CODEC);
	}

	public static <R extends RebornRecipe> RecipeType<R> newRecipeType(Identifier name, Function<RecipeType<R>, MapCodec<R>> codec, Function<RecipeType<R>, PacketCodec<RegistryByteBuf, R>> packetCodec) {
		RecipeType<R> type = new RecipeType<R>() {
			@Override
			public String toString() {
				return name.toString();
			}
		};
		Registry.register(Registries.RECIPE_TYPE, name, type);

		record Serializer<R extends RebornRecipe>(MapCodec<R> codec, PacketCodec<RegistryByteBuf, R> packetCodec) implements RecipeSerializer<R> {}
		Serializer<R> serializer = new Serializer<>(codec.apply(type), packetCodec.apply(type));
		Registry.register(Registries.RECIPE_SERIALIZER, name, serializer);

		return type;
	}

	public static List<RecipeType<?>> getRecipeTypes(String namespace) {
		//noinspection unchecked
		return Registries.RECIPE_TYPE.getKeys().stream()
			.filter(key -> key.getValue().getNamespace().startsWith(namespace))
			.map((Function<RegistryKey<RecipeType<?>>, RecipeType<?>>) key -> Registries.RECIPE_TYPE.get(key.getValue()))
			.toList();
	}
}

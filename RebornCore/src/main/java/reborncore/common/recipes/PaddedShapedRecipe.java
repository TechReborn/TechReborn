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

package reborncore.common.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.chars.CharArraySet;
import it.unimi.dsi.fastutil.chars.CharSet;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RawShapedRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PaddedShapedRecipe extends ShapedRecipe {
	public static final Identifier ID = new Identifier("reborncore", "padded");
	public static final RecipeSerializer<PaddedShapedRecipe> PADDED = Registry.register(Registries.RECIPE_SERIALIZER, ID, new Serializer());

	final RawShapedRecipe raw;
	final ItemStack result;

	public PaddedShapedRecipe(String group, CraftingRecipeCategory category, RawShapedRecipe raw, ItemStack result, boolean showNotification) {
		super(group, category, raw, result, showNotification);
		this.raw = raw;
		this.result = result;
	}

	public static RawShapedRecipe create(Map<Character, Ingredient> key, List<String> pattern) {
		RawShapedRecipe.Data data = new RawShapedRecipe.Data(key, pattern);
		return fromData(data).getOrThrow();
	}

	// Basically a copy of ShapedRecipe.fromData
	private static DataResult<RawShapedRecipe> fromData(RawShapedRecipe.Data data) {
		String[] strings = data.pattern().toArray(String[]::new);
		int width = strings[0].length();
		int height = strings.length;

		DefaultedList<Ingredient> ingredients = DefaultedList.ofSize(width * height, Ingredient.EMPTY);
		CharSet charSet = new CharArraySet(data.key().keySet());

		for(int i = 0; i < strings.length; ++i) {
			String string = strings[i];

			for(int l = 0; l < string.length(); ++l) {
				char c = string.charAt(l);
				Ingredient ingredient = c == ' ' ? Ingredient.EMPTY : data.key().get(c);
				if (ingredient == null) {
					return DataResult.error(() -> "Pattern references symbol '" + c + "' but it's not defined in the key");
				}

				charSet.remove(c);
				ingredients.set(l + width * i, ingredient);
			}
		}

		if (!charSet.isEmpty()) {
			return DataResult.error(() -> "Key defines symbols that aren't used in pattern: " + charSet);
		}

		return DataResult.success(new RawShapedRecipe(width, height, ingredients, Optional.of(data)));
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return PADDED;
	}

	public RawShapedRecipe getRaw() {
		return raw;
	}

	public ItemStack getResult() {
		return result;
	}

	private static class Serializer implements RecipeSerializer<PaddedShapedRecipe> {

		public static final MapCodec<PaddedShapedRecipe> CODEC = RecordCodecBuilder.mapCodec(
			instance -> instance.group(Codec.STRING.optionalFieldOf("group", "").forGetter(PaddedShapedRecipe::getGroup),
					CraftingRecipeCategory.CODEC.fieldOf("category").orElse(CraftingRecipeCategory.MISC).forGetter(PaddedShapedRecipe::getCategory),
					RawShapedRecipe.CODEC.forGetter(PaddedShapedRecipe::getRaw),
					ItemStack.VALIDATED_CODEC.fieldOf("result").forGetter(PaddedShapedRecipe::getResult),
					Codec.BOOL.optionalFieldOf("show_notification", true).forGetter(PaddedShapedRecipe::showNotification))
				.apply(instance, PaddedShapedRecipe::new));

		public static final PacketCodec<RegistryByteBuf, PaddedShapedRecipe> PACKET_CODEC = PacketCodec.ofStatic(PaddedShapedRecipe.Serializer::write, PaddedShapedRecipe.Serializer::read);

		@Override
		public MapCodec<PaddedShapedRecipe> codec() {
			return CODEC;
		}

		@Override
		public PacketCodec<RegistryByteBuf, PaddedShapedRecipe> packetCodec() {
			return PACKET_CODEC;
		}

		private static PaddedShapedRecipe read(RegistryByteBuf buf) {
			String string = buf.readString();
			CraftingRecipeCategory craftingRecipeCategory = buf.readEnumConstant(CraftingRecipeCategory.class);
			RawShapedRecipe rawShapedRecipe = RawShapedRecipe.PACKET_CODEC.decode(buf);
			ItemStack itemStack = ItemStack.PACKET_CODEC.decode(buf);
			boolean showNotification = buf.readBoolean();
			return new PaddedShapedRecipe(string, craftingRecipeCategory, rawShapedRecipe, itemStack, showNotification);
		}

		private static void write(RegistryByteBuf buf, PaddedShapedRecipe recipe) {
			buf.writeString(recipe.getGroup());
			buf.writeEnumConstant(recipe.getCategory());
			RawShapedRecipe.PACKET_CODEC.encode(buf, recipe.raw);
			ItemStack.PACKET_CODEC.encode(buf, recipe.result);
			buf.writeBoolean(recipe.showNotification());
		}
	}
}

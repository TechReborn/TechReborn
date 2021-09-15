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

package reborncore.common.crafting.ingredient;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.Lazy;
import net.minecraft.util.registry.Registry;
import reborncore.common.fluid.container.ItemFluidInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FluidIngredient extends RebornIngredient {

	private final Fluid fluid;
	private final Optional<List<Item>> holders;
	private final Optional<Integer> count;

	private final Lazy<List<ItemStack>> previewStacks;
	private final Lazy<Ingredient> previewIngredient;

	public FluidIngredient(Fluid fluid, Optional<List<Item>> holders, Optional<Integer> count) {
		super(IngredientManager.FLUID_RECIPE_TYPE);
		this.fluid = fluid;
		this.holders = holders;
		this.count = count;

		previewStacks = new Lazy<>(() -> Registry.ITEM.stream()
				.filter(item -> item instanceof ItemFluidInfo)
				.filter(item -> !holders.isPresent() || holders.get().stream().anyMatch(i -> i == item))
				.map(item -> ((ItemFluidInfo) item).getFull(fluid))
				.peek(stack -> stack.setCount(count.orElse(1)))
				.collect(Collectors.toList()));

		previewIngredient = new Lazy<>(() -> Ingredient.ofStacks(previewStacks.get().stream()));
	}

	public static RebornIngredient deserialize(JsonObject json) {
		Identifier identifier = new Identifier(JsonHelper.getString(json, "fluid"));
		Fluid fluid = Registry.FLUID.get(identifier);
		if (fluid == Fluids.EMPTY) {
			throw new JsonParseException("Fluid could not be found: " + JsonHelper.getString(json, "fluid"));
		}

		Optional<List<Item>> holders = Optional.empty();

		if (json.has("holder")) {
			if (json.get("holder").isJsonPrimitive()) {
				String ident = JsonHelper.getString(json, "holder");
				Item item = Registry.ITEM.get(new Identifier(ident));
				if (item == Items.AIR) {
					throw new JsonParseException("could not find item:" + ident);
				}
				holders = Optional.of(Collections.singletonList(item));
			} else {
				JsonArray jsonArray = json.getAsJsonArray("holder");
				List<Item> itemList = new ArrayList<>();
				for (int i = 0; i < jsonArray.size(); i++) {
					String ident = jsonArray.get(i).getAsString();
					Item item = Registry.ITEM.get(new Identifier(ident));
					if (item == Items.AIR) {
						throw new JsonParseException("could not find item:" + ident);
					}
					itemList.add(item);
				}
				holders = Optional.of(itemList);
			}
		}

		Optional<Integer> count = Optional.empty();

		if (json.has("count")) {
			count = Optional.of(json.get("count").getAsInt());
		}

		return new FluidIngredient(fluid, holders, count);
	}

	@Override
	public boolean test(ItemStack itemStack) {
		if (holders.isPresent() && holders.get().stream().noneMatch(item -> itemStack.getItem() == item)) {
			return false;
		}
		if (count.isPresent() && itemStack.getCount() < count.get()) {
			return false;
		}
		if (itemStack.getItem() instanceof ItemFluidInfo) {
			return ((ItemFluidInfo) itemStack.getItem()).getFluid(itemStack) == fluid;
		}
		return false;
	}

	@Override
	public Ingredient getPreview() {
		return previewIngredient.get();
	}

	@Override
	public List<ItemStack> getPreviewStacks() {
		return previewStacks.get();
	}

	@Override
	public JsonObject toJson() {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("fluid", Registry.FLUID.getId(fluid).toString());
		if (holders.isPresent()) {
			List<Item> holderList = holders.get();
			if (holderList.size() == 1) {
				jsonObject.addProperty("holder", Registry.ITEM.getId(holderList.get(0)).toString());
			} else {
				JsonArray holderArray = new JsonArray();
				holderList.forEach(item -> holderArray.add(new JsonPrimitive(Registry.ITEM.getId(item).toString())));
				jsonObject.add("holder", holderArray);
			}
		}
		count.ifPresent(integer -> jsonObject.addProperty("count", integer));
		return jsonObject;
	}

	@Override
	public int getCount() {
		return count.orElse(1);
	}
}

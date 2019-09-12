/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
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

package techreborn.api.recipe.recipes;

import com.google.common.collect.Iterables;
import com.google.gson.JsonObject;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.crafting.RebornRecipeType;
import reborncore.common.crafting.ingredient.RebornIngredient;

import java.util.Collections;
import java.util.List;

public class RollingMachineRecipe extends RebornRecipe {

	private ShapedRecipe shapedRecipe;
	private JsonObject shaped;

	public RollingMachineRecipe(RebornRecipeType<?> type, Identifier name) {
		super(type, name);
	}

	@Override
	public void deserialize(JsonObject jsonObject) {
		JsonObject json = JsonHelper.getObject(jsonObject, "shaped");
		shapedRecipe = RecipeSerializer.SHAPED.read(getId(), json);
		shaped = withoutTags(json);
	}

	@Override
	public void serialize(JsonObject jsonObject) {
		jsonObject.add("shaped", shaped);
	}

	//This needs to be done to remove the item tags, and replace them with just items for when syncing to the server
	private JsonObject withoutTags(JsonObject jsonObject) {
		JsonObject key = jsonObject.get("key").getAsJsonObject();
		key.entrySet().forEach(entry -> {
			if(entry.getValue().isJsonObject()){
				JsonObject value = entry.getValue().getAsJsonObject();
				if(value.has("tag")){

					Identifier tag = new Identifier(value.get("tag").getAsString());
					Tag<Item> itemTag = ItemTags.getContainer().get(tag);
					Item item = Iterables.get(itemTag.values(), 0);

					//Remove the tag, and replace it with a basic s
					value.remove("tag");
					value.addProperty("item", Registry.ITEM.getId(item).toString());
				}
			}
		});
		return jsonObject;
	}

	@Override
	public DefaultedList<RebornIngredient> getRebornIngredients() {
		return DefaultedList.of();
	}

	@Override
	public List<ItemStack> getOutputs() {
		return Collections.singletonList(shapedRecipe.getOutput());
	}

	@Override
	public ItemStack getOutput() {
		return shapedRecipe.getOutput();
	}

	@Override
	public DefaultedList<Ingredient> getPreviewInputs() {
		return shapedRecipe.getPreviewInputs();
	}

	@Override
	public boolean matches(Inventory inv, World worldIn) {
		return shapedRecipe.matches((CraftingInventory) inv, worldIn);
	}

	@Override
	public ItemStack craft(Inventory inv) {
		return shapedRecipe.craft((CraftingInventory) inv);
	}

	@Override
	public boolean fits(int width, int height) {
		return shapedRecipe.fits(width, height);
	}

	public ShapedRecipe getShapedRecipe() {
		return shapedRecipe;
	}
}

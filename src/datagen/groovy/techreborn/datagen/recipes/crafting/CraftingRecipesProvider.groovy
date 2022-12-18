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

package techreborn.datagen.recipes.crafting

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder
import net.minecraft.data.server.recipe.SingleItemRecipeJsonBuilder
import net.minecraft.item.ItemConvertible
import net.minecraft.item.Items
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.registry.RegistryWrapper
import net.minecraft.util.Identifier
import reborncore.common.recipes.PaddedShapedRecipeJsonBuilder
import techreborn.TechReborn
import techreborn.datagen.recipes.TechRebornRecipesProvider
import techreborn.init.TRContent

import java.util.concurrent.CompletableFuture
import java.util.function.Function

class CraftingRecipesProvider extends TechRebornRecipesProvider {
	CraftingRecipesProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture)
	}

	@Override
	void generateRecipes() {
		// add dust from small dust and vice versa recipes
		TRContent.SmallDusts.getSD2DMap().each { input, output ->
			offerMonoShapelessRecipe(input, 4, output, 1, "small", "crafting_table/dust/")
			offerMonoShapelessRecipe(output, 1, input, 4, "dust", "crafting_table/small_dust/")
		}
		// add storage block from raw metal and vice versa recipes
		TRContent.RawMetals.getRM2SBMap().each { input, output ->
			offerMonoShapelessRecipe(input, 9, output, 1, null, "crafting_table/storage_block/", "raw_" + getInputPath(output))
			offerMonoShapelessRecipe(output, 1, input, 9, "block", "crafting_table/raw/")
		}
		// add storage block from gem and vice versa recipes
		TRContent.Gems.getG2SBMap().each { input, output ->
			offerMonoShapelessRecipe(input, 9, output, 1, null, "crafting_table/storage_block/", )
			offerMonoShapelessRecipe(output, 1, input, 9, "block", "crafting_table/gem/")
		}
		// add storage block from ingot and vice versa recipes
		TRContent.Ingots.getI2SBMap().each { input, output ->
			offerMonoShapelessRecipe(input, 9, output, 1, null, "crafting_table/storage_block/")
			offerMonoShapelessRecipe(output, 1, input, 9, "block", "crafting_table/ingot/")
		}
		// add ingot from nugget and vice versa recipes
		TRContent.Nuggets.getN2IMap().each { input, output ->
			offerMonoShapelessRecipe(input, 9, output, 1, "nugget", input.isOfGem() ? "crafting_table/gem/" : "crafting_table/ingot/")
			offerMonoShapelessRecipe(output, 1, input, 9, null, "crafting_table/nugget/")
		}
		// add slabs, stairs and walls
		TRContent.StorageBlocks.values().each {block ->
			offerSlabRecipe(block.asTag(), block.getSlabBlock(), "crafting_table/storage_block/")
			offerSlabRecipeStonecutter(block.asTag(), block.getSlabBlock(), "crafting_table/storage_block/")
			offerStairsRecipe(block.asTag(), block.getStairsBlock(), "crafting_table/storage_block/")
			offerStairsRecipeStonecutter(block.asTag(), block.getStairsBlock(), "crafting_table/storage_block/")
			offerWallRecipe(block.asTag(), block.getWallBlock(), "crafting_table/storage_block/")
			offerWallRecipeStonecutter(block.asTag(), block.getWallBlock(), "crafting_table/storage_block/")
		}
		generateToolRecipes()
		generateArmorRecipes()
		generateUuMatterRecipes()
	}

	def generateToolRecipes() {
		// add axes
		[
				(TRContent.Ingots.BRONZE.asTag()): TRContent.BRONZE_AXE,
				(TRContent.Gems.PERIDOT)         : TRContent.PERIDOT_AXE,
				(TRContent.Gems.RUBY)            : TRContent.RUBY_AXE,
				(TRContent.Gems.SAPPHIRE)        : TRContent.SAPPHIRE_AXE
		].each { material, axe ->
			offerAxeRecipe(material, axe, "crafting_table/tool/")
		}
		// add hoes
		[
				(TRContent.Ingots.BRONZE.asTag()): TRContent.BRONZE_HOE,
				(TRContent.Gems.PERIDOT)         : TRContent.PERIDOT_HOE,
				(TRContent.Gems.RUBY)            : TRContent.RUBY_HOE,
				(TRContent.Gems.SAPPHIRE)        : TRContent.SAPPHIRE_HOE
		].each { material, hoe ->
			offerHoeRecipe(material, hoe, "crafting_table/tool/")
		}
		// add pickaxes
		[
				(TRContent.Ingots.BRONZE.asTag()): TRContent.BRONZE_PICKAXE,
				(TRContent.Gems.PERIDOT)         : TRContent.PERIDOT_PICKAXE,
				(TRContent.Gems.RUBY)            : TRContent.RUBY_PICKAXE,
				(TRContent.Gems.SAPPHIRE)        : TRContent.SAPPHIRE_PICKAXE
		].each { material, pickaxe ->
			offerPickaxeRecipe(material, pickaxe, "crafting_table/tool/")
		}
		// add shovels
		[
				(TRContent.Ingots.BRONZE.asTag()): TRContent.BRONZE_SPADE,
				(TRContent.Gems.PERIDOT)         : TRContent.PERIDOT_SPADE,
				(TRContent.Gems.RUBY)            : TRContent.RUBY_SPADE,
				(TRContent.Gems.SAPPHIRE)        : TRContent.SAPPHIRE_SPADE
		].each { material, shovel ->
			offerShovelRecipe(material, shovel, "crafting_table/tool/", "spade")
		}
		// add swords
		[
				(TRContent.Ingots.BRONZE.asTag()): TRContent.BRONZE_SWORD,
				(TRContent.Gems.PERIDOT)         : TRContent.PERIDOT_SWORD,
				(TRContent.Gems.RUBY)            : TRContent.RUBY_SWORD,
				(TRContent.Gems.SAPPHIRE)        : TRContent.SAPPHIRE_SWORD
		].each { material, sword ->
			offerSwordRecipe(material, sword, "crafting_table/tool/")
		}
	}

	def generateArmorRecipes() {
		// add boots
		[
				(TRContent.Ingots.BRONZE.asTag()): TRContent.BRONZE_BOOTS,
				(TRContent.Gems.PERIDOT)         : TRContent.PERIDOT_BOOTS,
				(TRContent.Gems.RUBY)            : TRContent.RUBY_BOOTS,
				(TRContent.Gems.SAPPHIRE)        : TRContent.SAPPHIRE_BOOTS,
				(TRContent.Ingots.SILVER)        : TRContent.SILVER_BOOTS,
				(TRContent.Ingots.STEEL)         : TRContent.STEEL_BOOTS
		].each { material, boots ->
			offerBootsRecipe(material, boots, "crafting_table/armor/")
		}
		// add chestplate
		[
				(TRContent.Ingots.BRONZE.asTag()): TRContent.BRONZE_CHESTPLATE,
				(TRContent.Gems.PERIDOT)         : TRContent.PERIDOT_CHESTPLATE,
				(TRContent.Gems.RUBY)            : TRContent.RUBY_CHESTPLATE,
				(TRContent.Gems.SAPPHIRE)        : TRContent.SAPPHIRE_CHESTPLATE,
				(TRContent.Ingots.SILVER)        : TRContent.SILVER_CHESTPLATE,
				(TRContent.Ingots.STEEL)         : TRContent.STEEL_CHESTPLATE
		].each { material, chestplate ->
			offerChestplateRecipe(material, chestplate, "crafting_table/armor/")
		}
		// add helmets
		[
				(TRContent.Ingots.BRONZE.asTag()): TRContent.BRONZE_HELMET,
				(TRContent.Gems.PERIDOT)         : TRContent.PERIDOT_HELMET,
				(TRContent.Gems.RUBY)            : TRContent.RUBY_HELMET,
				(TRContent.Gems.SAPPHIRE)        : TRContent.SAPPHIRE_HELMET,
				(TRContent.Ingots.SILVER)        : TRContent.SILVER_HELMET,
				(TRContent.Ingots.STEEL)         : TRContent.STEEL_HELMET
		].each { material, helmet ->
			offerHelmetRecipe(material, helmet, "crafting_table/armor/")
		}
		// add leggings
		[
				(TRContent.Ingots.BRONZE.asTag()): TRContent.BRONZE_LEGGINGS,
				(TRContent.Gems.PERIDOT)         : TRContent.PERIDOT_LEGGINGS,
				(TRContent.Gems.RUBY)            : TRContent.RUBY_LEGGINGS,
				(TRContent.Gems.SAPPHIRE)        : TRContent.SAPPHIRE_LEGGINGS,
				(TRContent.Ingots.SILVER)        : TRContent.SILVER_LEGGINGS,
				(TRContent.Ingots.STEEL)         : TRContent.STEEL_LEGGINGS
		].each { material, leggings ->
			offerLeggingsRecipe(material, leggings, "crafting_table/armor/")
		}
	}

	def generateUuMatterRecipes() {
		String rootDir = "crafting_table/uu_matter/"
		String dir
		// dusts
		dir = rootDir + "dust/"
		createPureUuMatterPaddedRecipe(RecipeCategory.MISC, TRContent.Dusts.ALUMINUM)
			.pattern("UUU")
			.pattern("U  ")
			.pattern("   ")
			.offerTo(this.exporter, new Identifier(TechReborn.MOD_ID, recipeNameString(dir, null, TRContent.Dusts.ALUMINUM)))
		createPureUuMatterPaddedRecipe(RecipeCategory.MISC, TRContent.Dusts.CHROME)
			.pattern("UUU")
			.pattern("UU ")
			.pattern(" U ")
			.offerTo(this.exporter, new Identifier(TechReborn.MOD_ID, recipeNameString(dir, null, TRContent.Dusts.CHROME)))
		createPureUuMatterPaddedRecipe(RecipeCategory.MISC, TRContent.Dusts.PLATINUM)
			.pattern("UUU")
			.pattern("UU ")
			.pattern("   ")
			.offerTo(this.exporter, new Identifier(TechReborn.MOD_ID, recipeNameString(dir, null, TRContent.Dusts.PLATINUM)))
		createPureUuMatterPaddedRecipe(RecipeCategory.MISC, TRContent.Dusts.TITANIUM)
			.pattern("UUU")
			.pattern("U U")
			.pattern("   ")
			.offerTo(this.exporter, new Identifier(TechReborn.MOD_ID, recipeNameString(dir, null, TRContent.Dusts.TITANIUM)))
		// nuggets
		dir = rootDir + "nugget/"
		createPureUuMatterPaddedRecipe(RecipeCategory.MISC, TRContent.Nuggets.NETHERITE)
			.pattern("UUU")
			.pattern("UUU")
			.pattern("UU ")
			.offerTo(this.exporter, new Identifier(TechReborn.MOD_ID, recipeNameString(dir, null, TRContent.Nuggets.NETHERITE)))
		// raw ores
		dir = rootDir + "raw/"
		createPureUuMatterPaddedRecipe(RecipeCategory.MISC, Items.RAW_COPPER)
			.pattern("U  ")
			.pattern("   ")
			.pattern(" U ")
			.offerTo(this.exporter, new Identifier(TechReborn.MOD_ID, recipeNameString(dir, null, Items.RAW_COPPER)))
		createPureUuMatterPaddedRecipe(RecipeCategory.MISC, TRContent.RawMetals.LEAD)
			.pattern("   ")
			.pattern("U  ")
			.pattern("U  ")
			.offerTo(this.exporter, new Identifier(TechReborn.MOD_ID, recipeNameString(dir, null, TRContent.RawMetals.LEAD)))
		createPureUuMatterPaddedRecipe(RecipeCategory.MISC, TRContent.RawMetals.TIN)
			.pattern("   ")
			.pattern(" U ")
			.pattern("  U")
			.offerTo(this.exporter, new Identifier(TechReborn.MOD_ID, recipeNameString(dir, null, TRContent.RawMetals.TIN)))
		createPureUuMatterPaddedRecipe(RecipeCategory.MISC, TRContent.RawMetals.TUNGSTEN)
			.pattern("UUU")
			.pattern("UUU")
			.pattern("   ")
			.offerTo(this.exporter, new Identifier(TechReborn.MOD_ID, recipeNameString(dir, null, TRContent.RawMetals.TUNGSTEN)))
	}

	def static recipeNameString(String prefix, def input, def output, String source = null, String result = null) {
		StringBuilder s = new StringBuilder()
		s.append(prefix)
		if (result == null)
			s.append(getInputPath(output))
		else
			s.append(result)
		if (source != null) {
			s.append("_from_")
			s.append(source)
		}
		return s.toString()
	}

	def offerMonoShapelessRecipe(def input, int inputSize, ItemConvertible output, int outputSize, String source, prefix = "", String result = null, RecipeCategory category = RecipeCategory.MISC) {
		new ShapelessRecipeJsonBuilder(category, output, outputSize).input(createIngredient(input), inputSize)
				.criterion(getCriterionName(input), getCriterionConditions(input))
				.offerTo(this.exporter, new Identifier(TechReborn.MOD_ID, recipeNameString(prefix, input, output, source, result)))
	}

	def static materialTypeString(String prefix, def material, String type, Function<?, String> modifier) {
		StringBuilder s = new StringBuilder()
		s.append(prefix)
		s.append(modifier.apply(material))
		s.append('_')
		s.append(type)
		return s.toString()
	}

	def static createMonoShapeRecipe(def input, ItemConvertible output, char character, int outputAmount = 1, RecipeCategory category = RecipeCategory.MISC) {
		return new ShapedRecipeJsonBuilder(category, output, outputAmount)
				.input(character, createIngredient(input))
				.criterion(getCriterionName(input), getCriterionConditions(input))
	}

	def static createDuoShapeRecipe(def input1, def input2, ItemConvertible output, char char1, char char2, boolean crit1 = true, boolean crit2 = false, RecipeCategory category = RecipeCategory.MISC) {
		ShapedRecipeJsonBuilder factory = ShapedRecipeJsonBuilder.create(category, output)
				.input(char1, createIngredient(input1))
				.input(char2, createIngredient(input2))
		if (crit1)
			factory = factory.criterion(getCriterionName(input1), getCriterionConditions(input1))
		if (crit2)
			factory = factory.criterion(getCriterionName(input2), getCriterionConditions(input2))
		return factory
	}

	def static createStonecutterRecipe(def input, ItemConvertible output, int outputAmount = 1, RecipeCategory category = RecipeCategory.MISC) {
		return new SingleItemRecipeJsonBuilder(category, RecipeSerializer.STONECUTTING, createIngredient(input), output, outputAmount)
				.criterion(getCriterionName(input), getCriterionConditions(input))
	}

	def offerSlabRecipe(def material, ItemConvertible output, prefix = "") {
		createMonoShapeRecipe(material, output, 'X' as char, 6)
				.pattern("XXX")
				.offerTo(this.exporter, new Identifier(TechReborn.MOD_ID, materialTypeString(prefix, material, "slab", TechRebornRecipesProvider::getName)))
	}

	def offerSlabRecipeStonecutter(def material, ItemConvertible output, prefix = "") {
		createStonecutterRecipe(material, output, 2)
				.offerTo(this.exporter, new Identifier(TechReborn.MOD_ID, materialTypeString(prefix, material, "slab", TechRebornRecipesProvider::getName) + "_stonecutter"))
	}

	def offerStairsRecipe(def material, ItemConvertible output, prefix = "") {
		createMonoShapeRecipe(material, output, 'X' as char, 4)
				.pattern("X  ")
				.pattern("XX ")
				.pattern("XXX")
				.offerTo(this.exporter, new Identifier(TechReborn.MOD_ID, materialTypeString(prefix, material, "stairs", TechRebornRecipesProvider::getName)))
	}

	def offerStairsRecipeStonecutter(def material, ItemConvertible output, prefix = "") {
		createStonecutterRecipe(material, output)
				.offerTo(this.exporter, new Identifier(TechReborn.MOD_ID, materialTypeString(prefix, material, "stairs", TechRebornRecipesProvider::getName) + "_stonecutter"))
	}

	def offerWallRecipe(def material, ItemConvertible output, prefix = "") {
		createMonoShapeRecipe(material, output, 'X' as char, 6)
				.pattern("XXX")
				.pattern("XXX")
				.offerTo(this.exporter, new Identifier(TechReborn.MOD_ID, materialTypeString(prefix, material, "wall", TechRebornRecipesProvider::getName)))
	}

	def offerWallRecipeStonecutter(def material, ItemConvertible output, prefix = "") {
		createStonecutterRecipe(material, output)
				.offerTo(this.exporter, new Identifier(TechReborn.MOD_ID, materialTypeString(prefix, material, "wall", TechRebornRecipesProvider::getName) + "_stonecutter"))
	}

	def offerAxeRecipe(def material, ItemConvertible output, prefix = "", String type = "axe") {
		createDuoShapeRecipe(material, Items.STICK, output,
				'X' as char, '#' as char)
				.pattern("XX")
				.pattern("X#")
				.pattern(" #")
				.offerTo(this.exporter, new Identifier(TechReborn.MOD_ID, materialTypeString(prefix, material, type, TechRebornRecipesProvider::getNamePart1)))
	}

	def offerHoeRecipe(def material, ItemConvertible output, prefix = "", String type = "hoe") {
		createDuoShapeRecipe(material, Items.STICK, output,
				'X' as char, '#' as char)
				.pattern("XX")
				.pattern(" #")
				.pattern(" #")
				.offerTo(this.exporter, new Identifier(TechReborn.MOD_ID, materialTypeString(prefix, material, type, TechRebornRecipesProvider::getNamePart1)))
	}

	def offerPickaxeRecipe(def material, ItemConvertible output, prefix = "", String type = "pickaxe") {
		createDuoShapeRecipe(material, Items.STICK, output,
				'X' as char, '#' as char)
				.pattern("XXX")
				.pattern(" # ")
				.pattern(" # ")
				.offerTo(this.exporter, new Identifier(TechReborn.MOD_ID, materialTypeString(prefix, material, type, TechRebornRecipesProvider::getNamePart1)))
	}

	def offerShovelRecipe(def material, ItemConvertible output, prefix = "", String type = "shovel") {
		createDuoShapeRecipe(material, Items.STICK, output,
				'X' as char, '#' as char)
				.pattern("X")
				.pattern("#")
				.pattern("#")
				.offerTo(this.exporter, new Identifier(TechReborn.MOD_ID, materialTypeString(prefix, material, type, TechRebornRecipesProvider::getNamePart1)))
	}

	def offerSwordRecipe(def material, ItemConvertible output, prefix = "", String type = "sword") {
		createDuoShapeRecipe(material, Items.STICK, output,
				'X' as char, '#' as char)
				.pattern("X")
				.pattern("X")
				.pattern("#")
				.offerTo(this.exporter, new Identifier(TechReborn.MOD_ID, materialTypeString(prefix, material, type, TechRebornRecipesProvider::getNamePart1)))
	}

	def offerBootsRecipe(def material, ItemConvertible output, prefix = "", String type = "boots") {
		createMonoShapeRecipe(material, output, 'X' as char)
				.pattern("X X")
				.pattern("X X")
				.offerTo(this.exporter, new Identifier(TechReborn.MOD_ID, materialTypeString(prefix, material, type, TechRebornRecipesProvider::getNamePart1)))
	}

	def offerChestplateRecipe(def material, ItemConvertible output, prefix = "", String type = "chestplate") {
		createMonoShapeRecipe(material, output, 'X' as char)
				.pattern("X X")
				.pattern("XXX")
				.pattern("XXX")
				.offerTo(this.exporter, new Identifier(TechReborn.MOD_ID, materialTypeString(prefix, material, type, TechRebornRecipesProvider::getNamePart1)))
	}

	def offerHelmetRecipe(def material, ItemConvertible output, prefix = "", String type = "helmet") {
		createMonoShapeRecipe(material, output, 'X' as char)
				.pattern("XXX")
				.pattern("X X")
				.offerTo(this.exporter, new Identifier(TechReborn.MOD_ID, materialTypeString(prefix, material, type, TechRebornRecipesProvider::getNamePart1)))
	}

	def offerLeggingsRecipe(def material, ItemConvertible output, prefix = "", String type = "leggings") {
		createMonoShapeRecipe(material, output, 'X' as char)
				.pattern("XXX")
				.pattern("X X")
				.pattern("X X")
				.offerTo(this.exporter, new Identifier(TechReborn.MOD_ID, materialTypeString(prefix, material, type, TechRebornRecipesProvider::getNamePart1)))
	}

	def static createPureUuMatterPaddedRecipe(RecipeCategory category, ItemConvertible output) {
		var input = TRContent.Parts.UU_MATTER
		return PaddedShapedRecipeJsonBuilder.create(category, output, 1)
			.input('U' as char, createIngredient(input))
			.criterion(getCriterionName(input), getCriterionConditions(input))
	}

}

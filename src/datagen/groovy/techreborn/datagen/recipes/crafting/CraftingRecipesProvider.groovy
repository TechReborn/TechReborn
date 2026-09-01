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

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.data.recipes.ShapelessRecipeBuilder
import net.minecraft.data.recipes.SingleItemRecipeBuilder
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.level.ItemLike
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.StonecutterRecipe
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.resources.ResourceKey
import net.minecraft.core.registries.Registries
import net.minecraft.core.HolderLookup
import net.minecraft.resources.Identifier
import techreborn.TechReborn
import techreborn.datagen.recipes.TechRebornRecipesProvider
import techreborn.init.TRContent

import java.util.concurrent.CompletableFuture
import java.util.function.Function

class CraftingRecipesProvider extends TechRebornRecipesProvider {
	CraftingRecipesProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
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
		generateMisc()
	}

	def generateToolRecipes() {
		// add axes
		[
				(TRContent.Ingots.BRONZE.asTag()): TRContent.BRONZE_AXE,
				(TRContent.Gems.PERIDOT)         : TRContent.PERIDOT_AXE,
				(TRContent.Gems.RUBY)            : TRContent.RUBY_AXE,
				(TRContent.Gems.SAPPHIRE)        : TRContent.SAPPHIRE_AXE
		].each { material, axe ->
			offerAxeRecipe(material, axe, "crafting_table/tool")
		}
		// add hoes
		[
				(TRContent.Ingots.BRONZE.asTag()): TRContent.BRONZE_HOE,
				(TRContent.Gems.PERIDOT)         : TRContent.PERIDOT_HOE,
				(TRContent.Gems.RUBY)            : TRContent.RUBY_HOE,
				(TRContent.Gems.SAPPHIRE)        : TRContent.SAPPHIRE_HOE
		].each { material, hoe ->
			offerHoeRecipe(material, hoe, "crafting_table/tool")
		}
		// add pickaxes
		[
				(TRContent.Ingots.BRONZE.asTag()): TRContent.BRONZE_PICKAXE,
				(TRContent.Gems.PERIDOT)         : TRContent.PERIDOT_PICKAXE,
				(TRContent.Gems.RUBY)            : TRContent.RUBY_PICKAXE,
				(TRContent.Gems.SAPPHIRE)        : TRContent.SAPPHIRE_PICKAXE
		].each { material, pickaxe ->
			offerPickaxeRecipe(material, pickaxe, "crafting_table/tool")
		}
		// add shovels
		[
				(TRContent.Ingots.BRONZE.asTag()): TRContent.BRONZE_SPADE,
				(TRContent.Gems.PERIDOT)         : TRContent.PERIDOT_SPADE,
				(TRContent.Gems.RUBY)            : TRContent.RUBY_SPADE,
				(TRContent.Gems.SAPPHIRE)        : TRContent.SAPPHIRE_SPADE
		].each { material, shovel ->
			offerShovelRecipe(material, shovel, "crafting_table/tool", "spade")
		}
		// add swords
		[
				(TRContent.Ingots.BRONZE.asTag()): TRContent.BRONZE_SWORD,
				(TRContent.Gems.PERIDOT)         : TRContent.PERIDOT_SWORD,
				(TRContent.Gems.RUBY)            : TRContent.RUBY_SWORD,
				(TRContent.Gems.SAPPHIRE)        : TRContent.SAPPHIRE_SWORD
		].each { material, sword ->
			offerSwordRecipe(material, sword, "crafting_table/tool")
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
			offerBootsRecipe(material, boots, "crafting_table/armor")
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
			offerChestplateRecipe(material, chestplate, "crafting_table/armor")
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
			offerHelmetRecipe(material, helmet, "crafting_table/armor")
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
			offerLeggingsRecipe(material, leggings, "crafting_table/armor")
		}
	}

	def generateUuMatterRecipes() {
		String rootDir = "crafting_table/uu_matter/"
		String dir
		// dusts
		dir = rootDir + "dust/"
		createMonoShapeRecipe(TRContent.Parts.UU_MATTER, TRContent.Dusts.ALUMINUM, 'U' as char)
			.pattern("UUU")
			.pattern("U  ")
			.pattern("   ")
			.save(this.exporter, getRecipeKey(recipeNameString(dir, null, TRContent.Dusts.ALUMINUM)))
		createMonoShapeRecipe(TRContent.Parts.UU_MATTER, TRContent.Dusts.CHROME, 'U' as char)
			.pattern("UUU")
			.pattern("UU ")
			.pattern(" U ")
			.save(this.exporter, getRecipeKey(recipeNameString(dir, null, TRContent.Dusts.CHROME)))
		createMonoShapeRecipe(TRContent.Parts.UU_MATTER, TRContent.Dusts.PLATINUM, 'U' as char)
			.pattern("UUU")
			.pattern("UU ")
			.pattern("   ")
			.save(this.exporter, getRecipeKey(recipeNameString(dir, null, TRContent.Dusts.PLATINUM)))
		createMonoShapeRecipe(TRContent.Parts.UU_MATTER, TRContent.Dusts.TITANIUM, 'U' as char)
			.pattern("UUU")
			.pattern("U U")
			.pattern("   ")
			.save(this.exporter, getRecipeKey(recipeNameString(dir, null, TRContent.Dusts.TITANIUM)))
		// nuggets
		dir = rootDir + "nugget/"
		createMonoShapeRecipe(TRContent.Parts.UU_MATTER, TRContent.Nuggets.NETHERITE, 'U' as char)
			.pattern("UUU")
			.pattern("UUU")
			.pattern("UU ")
			.save(this.exporter, getRecipeKey(recipeNameString(dir, null, TRContent.Nuggets.NETHERITE)))
		// raw ores
		dir = rootDir + "raw/"
		createMonoShapeRecipe(TRContent.Parts.UU_MATTER, Items.RAW_COPPER, 'U' as char)
			.pattern("U  ")
			.pattern("   ")
			.pattern(" U ")
			.save(this.exporter, getRecipeKey(recipeNameString(dir, null, Items.RAW_COPPER)))
		createMonoShapeRecipe(TRContent.Parts.UU_MATTER, TRContent.RawMetals.LEAD, 'U' as char)
			.pattern("   ")
			.pattern("U  ")
			.pattern("U  ")
			.save(this.exporter, getRecipeKey(recipeNameString(dir, null, TRContent.RawMetals.LEAD)))
		createMonoShapeRecipe(TRContent.Parts.UU_MATTER, TRContent.RawMetals.TIN, 'U' as char)
			.pattern("   ")
			.pattern(" U ")
			.pattern("  U")
			.save(this.exporter, getRecipeKey(recipeNameString(dir, null, TRContent.RawMetals.TIN)))
		createMonoShapeRecipe(TRContent.Parts.UU_MATTER, TRContent.RawMetals.TUNGSTEN, 'U' as char)
			.pattern("UUU")
			.pattern("UUU")
			.pattern("   ")
			.save(this.exporter, getRecipeKey(recipeNameString(dir, null, TRContent.RawMetals.TUNGSTEN)))
		createMonoShapeRecipe(TRContent.Parts.UU_MATTER, TRContent.RawMetals.URANIUM, 'U' as char)
			.pattern("UUU")
			.pattern("U  ")
			.pattern("  U")
			.save(this.exporter, getRecipeKey(recipeNameString(dir, null, TRContent.RawMetals.URANIUM)))
		//ores
		dir = rootDir + "ore/"
		createDuoShapeRecipe(TRContent.Parts.UU_MATTER, Items.STONE, Items.COAL_ORE,
			'U' as char, 'S' as char)
			.pattern("U  ")
			.pattern(" U ")
			.pattern(" S ")
			.save(this.exporter, getRecipeKey(recipeNameString(dir, null, Items.COAL_ORE)))
		createDuoShapeRecipe(TRContent.Parts.UU_MATTER, Items.DEEPSLATE, Items.DEEPSLATE_COAL_ORE,
			'U' as char, 'D' as char)
			.pattern("U  ")
			.pattern(" U ")
			.pattern(" D ")
			.save(this.exporter, getRecipeKey(recipeNameString(dir, null, Items.DEEPSLATE_COAL_ORE)))
		createDuoShapeRecipe(TRContent.Parts.UU_MATTER, Items.STONE, Items.IRON_ORE,
			'U' as char, 'S' as char)
			.pattern("U U")
			.pattern("   ")
			.pattern(" S ")
			.save(this.exporter, getRecipeKey(recipeNameString(dir, null, Items.IRON_ORE)))
		createDuoShapeRecipe(TRContent.Parts.UU_MATTER, Items.DEEPSLATE, Items.DEEPSLATE_IRON_ORE,
			'U' as char, 'D' as char)
			.pattern("U U")
			.pattern("   ")
			.pattern(" D ")
			.save(this.exporter, getRecipeKey(recipeNameString(dir, null, Items.DEEPSLATE_IRON_ORE)))
		createDuoShapeRecipe(TRContent.Parts.UU_MATTER, Items.STONE, Items.COPPER_ORE,
			'U' as char, 'S' as char)
			.pattern("UU ")
			.pattern("   ")
			.pattern(" S ")
			.save(this.exporter, getRecipeKey(recipeNameString(dir, null, Items.COPPER_ORE)))
		createDuoShapeRecipe(TRContent.Parts.UU_MATTER, Items.DEEPSLATE, Items.DEEPSLATE_COPPER_ORE,
			'U' as char, 'D' as char)
			.pattern("UU ")
			.pattern("   ")
			.pattern(" D ")
			.save(this.exporter, getRecipeKey(recipeNameString(dir, null, Items.DEEPSLATE_COPPER_ORE)))
		createDuoShapeRecipe(TRContent.Parts.UU_MATTER, Items.STONE, Items.GOLD_ORE,
			'U' as char, 'S' as char)
			.pattern("   ")
			.pattern("UUU")
			.pattern(" S ")
			.save(this.exporter, getRecipeKey(recipeNameString(dir, null, Items.GOLD_ORE)))
		createDuoShapeRecipe(TRContent.Parts.UU_MATTER, Items.DEEPSLATE, Items.DEEPSLATE_GOLD_ORE,
			'U' as char, 'D' as char)
			.pattern("   ")
			.pattern("UUU")
			.pattern(" D ")
			.save(this.exporter, getRecipeKey(recipeNameString(dir, null, Items.DEEPSLATE_GOLD_ORE)))
		createDuoShapeRecipe(TRContent.Parts.UU_MATTER, Items.STONE, Items.REDSTONE_ORE,
			'U' as char, 'S' as char)
			.pattern("   ")
			.pattern(" U ")
			.pattern("USU")
			.save(this.exporter, getRecipeKey(recipeNameString(dir, null, Items.REDSTONE_ORE)))
		createDuoShapeRecipe(TRContent.Parts.UU_MATTER, Items.DEEPSLATE, Items.DEEPSLATE_REDSTONE_ORE,
			'U' as char, 'D' as char)
			.pattern("   ")
			.pattern(" U ")
			.pattern("UDU")
			.save(this.exporter, getRecipeKey(recipeNameString(dir, null, Items.DEEPSLATE_REDSTONE_ORE)))
		createDuoShapeRecipe(TRContent.Parts.UU_MATTER, Items.STONE, Items.EMERALD_ORE,
			'U' as char, 'S' as char)
			.pattern("U  ")
			.pattern("U U")
			.pattern(" S ")
			.save(this.exporter, getRecipeKey(recipeNameString(dir, null, Items.EMERALD_ORE)))
		createDuoShapeRecipe(TRContent.Parts.UU_MATTER, Items.DEEPSLATE, Items.DEEPSLATE_EMERALD_ORE,
			'U' as char, 'D' as char)
			.pattern("U  ")
			.pattern("U U")
			.pattern(" D ")
			.save(this.exporter, getRecipeKey(recipeNameString(dir, null, Items.DEEPSLATE_EMERALD_ORE)))
		createDuoShapeRecipe(TRContent.Parts.UU_MATTER, Items.STONE, Items.LAPIS_ORE,
			'U' as char, 'S' as char)
			.pattern("U  ")
			.pattern("U  ")
			.pattern(" S ")
			.save(this.exporter, getRecipeKey(recipeNameString(dir, null, Items.LAPIS_ORE)))
		createDuoShapeRecipe(TRContent.Parts.UU_MATTER, Items.DEEPSLATE, Items.DEEPSLATE_LAPIS_ORE,
			'U' as char, 'D' as char)
			.pattern("U  ")
			.pattern("U  ")
			.pattern(" D ")
			.save(this.exporter, getRecipeKey(recipeNameString(dir, null, Items.DEEPSLATE_LAPIS_ORE)))
		createDuoShapeRecipe(TRContent.Parts.UU_MATTER, Items.STONE, Items.DIAMOND_ORE,
			'U' as char, 'S' as char)
			.pattern("UU ")
			.pattern("UUU")
			.pattern("US ")
			.save(this.exporter, getRecipeKey(recipeNameString(dir, null, Items.DIAMOND_ORE)))
		createDuoShapeRecipe(TRContent.Parts.UU_MATTER, Items.DEEPSLATE, Items.DEEPSLATE_DIAMOND_ORE,
			'U' as char, 'D' as char)
			.pattern("UU ")
			.pattern("UUU")
			.pattern("UD ")
			.save(this.exporter, getRecipeKey(recipeNameString(dir, null, Items.DEEPSLATE_DIAMOND_ORE)))
		createDuoShapeRecipe(TRContent.Parts.UU_MATTER, Items.NETHERRACK, Items.NETHER_GOLD_ORE,
			'U' as char, 'N' as char)
			.pattern("   ")
			.pattern("UUU")
			.pattern(" N ")
			.save(this.exporter, getRecipeKey(recipeNameString(dir, null, Items.NETHER_GOLD_ORE)))
		createDuoShapeRecipe(TRContent.Parts.UU_MATTER, Items.NETHERRACK, Items.NETHER_QUARTZ_ORE,
			'U' as char, 'N' as char)
			.pattern(" U ")
			.pattern(" U ")
			.pattern(" N ")
			.save(this.exporter, getRecipeKey(recipeNameString(dir, null, Items.NETHER_QUARTZ_ORE)))
		createDuoShapeRecipe(TRContent.Parts.UU_MATTER, Items.NETHERRACK, Items.ANCIENT_DEBRIS,
			'U' as char, 'N' as char)
			.pattern("UUU")
			.pattern("UUU")
			.pattern(" N ")
			.save(this.exporter, getRecipeKey(recipeNameString(dir, null, Items.ANCIENT_DEBRIS)))
		createDuoShapeRecipe(TRContent.Parts.UU_MATTER, Items.STONE, TRContent.Ores.TIN,
			'U' as char, 'S' as char)
			.pattern("U  ")
			.pattern("  U")
			.pattern(" S ")
			.save(this.exporter, getRecipeKey(recipeNameString(dir, null, TRContent.Ores.TIN)))
		createDuoShapeRecipe(TRContent.Parts.UU_MATTER, Items.DEEPSLATE, TRContent.Ores.DEEPSLATE_TIN,
			'U' as char, 'D' as char)
			.pattern("U  ")
			.pattern("  U")
			.pattern(" D ")
			.save(this.exporter, getRecipeKey(recipeNameString(dir, null, TRContent.Ores.DEEPSLATE_TIN)))
		createDuoShapeRecipe(TRContent.Parts.UU_MATTER, Items.STONE, TRContent.Ores.LEAD,
			'U' as char, 'S' as char)
			.pattern(" UU")
			.pattern("   ")
			.pattern(" S ")
			.save(this.exporter, getRecipeKey(recipeNameString(dir, null, TRContent.Ores.LEAD)))
		createDuoShapeRecipe(TRContent.Parts.UU_MATTER, Items.DEEPSLATE, TRContent.Ores.DEEPSLATE_LEAD,
			'U' as char, 'D' as char)
			.pattern(" UU")
			.pattern("   ")
			.pattern(" D ")
			.save(this.exporter, getRecipeKey(recipeNameString(dir, null, TRContent.Ores.DEEPSLATE_LEAD)))
		createDuoShapeRecipe(TRContent.Parts.UU_MATTER, Items.STONE, TRContent.Ores.SILVER,
			'U' as char, 'S' as char)
			.pattern("  U")
			.pattern("  U")
			.pattern(" S ")
			.save(this.exporter, getRecipeKey(recipeNameString(dir, null, TRContent.Ores.SILVER)))
		createDuoShapeRecipe(TRContent.Parts.UU_MATTER, Items.DEEPSLATE, TRContent.Ores.DEEPSLATE_SILVER,
			'U' as char, 'D' as char)
			.pattern("  U")
			.pattern("  U")
			.pattern(" D ")
			.save(this.exporter, getRecipeKey(recipeNameString(dir, null, TRContent.Ores.DEEPSLATE_SILVER)))
		createDuoShapeRecipe(TRContent.Parts.UU_MATTER, Items.STONE, TRContent.Ores.GALENA,
			'U' as char, 'S' as char)
			.pattern(" U ")
			.pattern("UU ")
			.pattern(" S ")
			.save(this.exporter, getRecipeKey(recipeNameString(dir, null, TRContent.Ores.GALENA)))
		createDuoShapeRecipe(TRContent.Parts.UU_MATTER, Items.DEEPSLATE, TRContent.Ores.DEEPSLATE_GALENA,
			'U' as char, 'D' as char)
			.pattern(" U ")
			.pattern("UU ")
			.pattern(" D ")
			.save(this.exporter, getRecipeKey(recipeNameString(dir, null, TRContent.Ores.DEEPSLATE_GALENA)))
		createDuoShapeRecipe(TRContent.Parts.UU_MATTER, Items.STONE, TRContent.Ores.BAUXITE,
			'U' as char, 'S' as char)
			.pattern(" UU")
			.pattern("  U")
			.pattern(" S ")
			.save(this.exporter, getRecipeKey(recipeNameString(dir, null, TRContent.Ores.BAUXITE)))
		createDuoShapeRecipe(TRContent.Parts.UU_MATTER, Items.DEEPSLATE, TRContent.Ores.DEEPSLATE_BAUXITE,
			'U' as char, 'D' as char)
			.pattern(" UU")
			.pattern("  U")
			.pattern(" D ")
			.save(this.exporter, getRecipeKey(recipeNameString(dir, null, TRContent.Ores.DEEPSLATE_BAUXITE)))
		createDuoShapeRecipe(TRContent.Parts.UU_MATTER, Items.STONE, TRContent.Ores.RUBY,
			'U' as char, 'S' as char)
			.pattern("UU ")
			.pattern("U  ")
			.pattern(" S ")
			.save(this.exporter, getRecipeKey(recipeNameString(dir, null, TRContent.Ores.RUBY)))
		createDuoShapeRecipe(TRContent.Parts.UU_MATTER, Items.DEEPSLATE, TRContent.Ores.DEEPSLATE_RUBY,
			'U' as char, 'D' as char)
			.pattern("UU ")
			.pattern("U  ")
			.pattern(" D ")
			.save(this.exporter, getRecipeKey(recipeNameString(dir, null, TRContent.Ores.DEEPSLATE_RUBY)))
		createDuoShapeRecipe(TRContent.Parts.UU_MATTER, Items.STONE, TRContent.Ores.SAPPHIRE,
			'U' as char, 'S' as char)
			.pattern(" U ")
			.pattern(" UU")
			.pattern(" S ")
			.save(this.exporter, getRecipeKey(recipeNameString(dir, null, TRContent.Ores.SAPPHIRE)))
		createDuoShapeRecipe(TRContent.Parts.UU_MATTER, Items.DEEPSLATE, TRContent.Ores.DEEPSLATE_SAPPHIRE,
			'U' as char, 'D' as char)
			.pattern(" U ")
			.pattern(" UU")
			.pattern(" D ")
			.save(this.exporter, getRecipeKey(recipeNameString(dir, null, TRContent.Ores.DEEPSLATE_SAPPHIRE)))
		createDuoShapeRecipe(TRContent.Parts.UU_MATTER, Items.STONE, TRContent.Ores.IRIDIUM,
			'U' as char, 'S' as char)
			.pattern("UUU")
			.pattern("UU ")
			.pattern("US ")
			.save(this.exporter, getRecipeKey(recipeNameString(dir, null, TRContent.Ores.IRIDIUM)))
		createDuoShapeRecipe(TRContent.Parts.UU_MATTER, Items.DEEPSLATE, TRContent.Ores.DEEPSLATE_IRIDIUM,
			'U' as char, 'D' as char)
			.pattern("UUU")
			.pattern("UU ")
			.pattern("UD ")
			.save(this.exporter, getRecipeKey(recipeNameString(dir, null, TRContent.Ores.DEEPSLATE_IRIDIUM)))
		createDuoShapeRecipe(TRContent.Parts.UU_MATTER, Items.STONE, TRContent.Ores.URANIUM,
			'U' as char, 'S' as char)
			.pattern("UU ")
			.pattern(" U ")
			.pattern(" SU")
			.save(this.exporter, getRecipeKey(recipeNameString(dir, null, TRContent.Ores.URANIUM)))
		createDuoShapeRecipe(TRContent.Parts.UU_MATTER, Items.DEEPSLATE, TRContent.Ores.DEEPSLATE_URANIUM,
			'U' as char, 'D' as char)
			.pattern("UU ")
			.pattern(" U ")
			.pattern(" DU")
			.save(this.exporter, getRecipeKey(recipeNameString(dir, null, TRContent.Ores.DEEPSLATE_URANIUM)))
		createDuoShapeRecipe(TRContent.Parts.UU_MATTER, Items.NETHERRACK, TRContent.Ores.PYRITE,
			'U' as char, 'N' as char)
			.pattern("U U")
			.pattern("   ")
			.pattern(" N ")
			.save(this.exporter, getRecipeKey(recipeNameString(dir, null, TRContent.Ores.PYRITE)))
		createDuoShapeRecipe(TRContent.Parts.UU_MATTER, Items.NETHERRACK, TRContent.Ores.SPHALERITE,
			'U' as char, 'N' as char)
			.pattern("  U")
			.pattern(" U ")
			.pattern(" NU")
			.save(this.exporter, getRecipeKey(recipeNameString(dir, null, TRContent.Ores.SPHALERITE)))
		createDuoShapeRecipe(TRContent.Parts.UU_MATTER, Items.END_STONE, TRContent.Ores.PERIDOT,
			'U' as char, 'E' as char)
			.pattern("U  ")
			.pattern(" UU")
			.pattern(" E ")
			.save(this.exporter, getRecipeKey(recipeNameString(dir, null, TRContent.Ores.PERIDOT)))
		createDuoShapeRecipe(TRContent.Parts.UU_MATTER, Items.END_STONE, TRContent.Ores.SHELDONITE,
			'U' as char, 'E' as char)
			.pattern(" U ")
			.pattern(" UU")
			.pattern("UEU")
			.save(this.exporter, getRecipeKey(recipeNameString(dir, null, TRContent.Ores.SHELDONITE)))
		createDuoShapeRecipe(TRContent.Parts.UU_MATTER, Items.END_STONE, TRContent.Ores.SODALITE,
			'U' as char, 'E' as char)
			.pattern("  U")
			.pattern("UU ")
			.pattern(" E ")
			.save(this.exporter, getRecipeKey(recipeNameString(dir, null, TRContent.Ores.SODALITE)))
		createDuoShapeRecipe(TRContent.Parts.UU_MATTER, Items.END_STONE, TRContent.Ores.TUNGSTEN,
			'U' as char, 'E' as char)
			.pattern(" U ")
			.pattern("UU ")
			.pattern("UEU")
			.save(this.exporter, getRecipeKey(recipeNameString(dir, null, TRContent.Ores.TUNGSTEN)))
	}

	void generateMisc() {
		createDuoShapeRecipe(Items.DIAMOND, TRContent.Nuggets.NETHERITE, TRContent.Parts.TEMPLATE_TEMPLATE,
			'D' as char, 'N' as char)
			.pattern("NDN")
			.pattern("DDD")
			.pattern("NDN")
			.save(this.exporter, getRecipeKey("crafting_table/parts/"+TRContent.Parts.TEMPLATE_TEMPLATE.name))
		ShapelessRecipeBuilder.shapeless(itemLookup, RecipeCategory.MISC, new ItemStackTemplate(Items.RESIN_CLUMP, 2))
			.requires(TRContent.Parts.SAP, 2)
			.requires(Items.SLIME_BALL)
			.unlockedBy("has_sap", getCriterionConditions(TRContent.Parts.SAP))
			.save(this.exporter, getRecipeKey("crafting_table/parts/resin_clump"))
		ShapelessRecipeBuilder.shapeless(itemLookup, RecipeCategory.MISC, new ItemStackTemplate(TRContent.Parts.SAP.asItem(), 8))
			.requires(Items.RESIN_CLUMP, 4)
			.requires(Items.WATER_BUCKET)
			.unlockedBy("has_resin_clump", getCriterionConditions(Items.RESIN_CLUMP))
			.save(this.exporter, getRecipeKey("crafting_table/parts/sap"))
		createMonoShapeRecipe(TRContent.Parts.SCRAP, TRContent.SCRAP_BOX,
			'S' as char)
			.pattern("SSS")
			.pattern("SSS")
			.pattern("SSS")
			.save(this.exporter, getRecipeKey("crafting_table/scrap_box"))
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

	def static getRecipeKey(String name) {
		return ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath(TechReborn.MOD_ID, name))
	}

	def offerMonoShapelessRecipe(def input, int inputSize, ItemLike output, int outputSize, String source, prefix = "", String result = null, RecipeCategory category = RecipeCategory.MISC) {
		ShapelessRecipeBuilder.shapeless(itemLookup, category, output, outputSize).requires(createIngredient(input), inputSize)
				.unlockedBy(getCriterionName(input), getCriterionConditions(input))
				.save(this.exporter, getRecipeKey(recipeNameString(prefix, input, output, source, result)))
	}

	def static materialTypeString(String prefix, def material, String type, Function<?, String> modifier) {
		StringBuilder s = new StringBuilder()
		s.append(prefix)
		s.append(modifier.apply(material))
		s.append('_')
		s.append(type)
		return s.toString()
	}

	def createMonoShapeRecipe(def input, ItemLike output, char character, int outputAmount = 1, RecipeCategory category = RecipeCategory.MISC) {
		return ShapedRecipeBuilder.shaped(itemLookup, category, output, outputAmount)
				.define(character, createIngredient(input))
				.unlockedBy(getCriterionName(input), getCriterionConditions(input))
	}

	def createDuoShapeRecipe(def input1, def input2, ItemLike output, char char1, char char2, boolean crit1 = true, boolean crit2 = false, RecipeCategory category = RecipeCategory.MISC) {
		ShapedRecipeBuilder factory = ShapedRecipeBuilder.shaped(itemLookup, category, output)
				.define(char1, createIngredient(input1))
				.define(char2, createIngredient(input2))
		if (crit1)
			factory = factory.unlockedBy(getCriterionName(input1), getCriterionConditions(input1))
		if (crit2)
			factory = factory.unlockedBy(getCriterionName(input2), getCriterionConditions(input2))
		return factory
	}

	def createStonecutterRecipe(def input, ItemLike output, int outputAmount = 1, RecipeCategory category = RecipeCategory.MISC) {
		return new SingleItemRecipeBuilder(category, StonecutterRecipe.&new, createIngredient(input), output, outputAmount)
				.unlockedBy(getCriterionName(input), getCriterionConditions(input))
	}

	def offerSlabRecipe(def material, ItemLike output, prefix = "") {
		createMonoShapeRecipe(material, output, 'X' as char, 6)
				.pattern("XXX")
				.save(this.exporter, getRecipeKey(materialTypeString(prefix, material, "slab", TechRebornRecipesProvider::getName)))
	}

	def offerSlabRecipeStonecutter(def material, ItemLike output, prefix = "") {
		createStonecutterRecipe(material, output, 2)
				.save(this.exporter, getRecipeKey(materialTypeString(prefix, material, "slab", TechRebornRecipesProvider::getName) + "_stonecutter"))
	}

	def offerStairsRecipe(def material, ItemLike output, prefix = "") {
		createMonoShapeRecipe(material, output, 'X' as char, 4)
				.pattern("X  ")
				.pattern("XX ")
				.pattern("XXX")
				.save(this.exporter, getRecipeKey(materialTypeString(prefix, material, "stairs", TechRebornRecipesProvider::getName)))
	}

	def offerStairsRecipeStonecutter(def material, ItemLike output, prefix = "") {
		createStonecutterRecipe(material, output)
				.save(this.exporter, getRecipeKey(materialTypeString(prefix, material, "stairs", TechRebornRecipesProvider::getName) + "_stonecutter"))
	}

	def offerWallRecipe(def material, ItemLike output, prefix = "") {
		createMonoShapeRecipe(material, output, 'X' as char, 6)
				.pattern("XXX")
				.pattern("XXX")
				.save(this.exporter, getRecipeKey(materialTypeString(prefix, material, "wall", TechRebornRecipesProvider::getName)))
	}

	def offerWallRecipeStonecutter(def material, ItemLike output, prefix = "") {
		createStonecutterRecipe(material, output)
				.save(this.exporter, getRecipeKey(materialTypeString(prefix, material, "wall", TechRebornRecipesProvider::getName) + "_stonecutter"))
	}

	def offerAxeRecipe(def material, ItemLike output, prefix = "", String type = "axe") {
		createDuoShapeRecipe(material, Items.STICK, output,
				'X' as char, '#' as char)
				.pattern("XX")
				.pattern("X#")
				.pattern(" #")
				.save(this.exporter, getRecipeKey(materialTypeString(prefix, material, type, TechRebornRecipesProvider::getNamePart1)))
	}

	def offerHoeRecipe(def material, ItemLike output, prefix = "", String type = "hoe") {
		createDuoShapeRecipe(material, Items.STICK, output,
				'X' as char, '#' as char)
				.pattern("XX")
				.pattern(" #")
				.pattern(" #")
				.save(this.exporter, getRecipeKey(materialTypeString(prefix, material, type, TechRebornRecipesProvider::getNamePart1)))
	}

	def offerPickaxeRecipe(def material, ItemLike output, prefix = "", String type = "pickaxe") {
		createDuoShapeRecipe(material, Items.STICK, output,
				'X' as char, '#' as char)
				.pattern("XXX")
				.pattern(" # ")
				.pattern(" # ")
				.save(this.exporter, getRecipeKey(materialTypeString(prefix, material, type, TechRebornRecipesProvider::getNamePart1)))
	}

	def offerShovelRecipe(def material, ItemLike output, prefix = "", String type = "shovel") {
		createDuoShapeRecipe(material, Items.STICK, output,
				'X' as char, '#' as char)
				.pattern("X")
				.pattern("#")
				.pattern("#")
				.save(this.exporter, getRecipeKey(materialTypeString(prefix, material, type, TechRebornRecipesProvider::getNamePart1)))
	}

	def offerSwordRecipe(def material, ItemLike output, prefix = "", String type = "sword") {
		createDuoShapeRecipe(material, Items.STICK, output,
				'X' as char, '#' as char)
				.pattern("X")
				.pattern("X")
				.pattern("#")
				.save(this.exporter, getRecipeKey(materialTypeString(prefix, material, type, TechRebornRecipesProvider::getNamePart1)))
	}

	def offerBootsRecipe(def material, ItemLike output, prefix = "", String type = "boots") {
		createMonoShapeRecipe(material, output, 'X' as char)
				.pattern("X X")
				.pattern("X X")
				.save(this.exporter, getRecipeKey(materialTypeString(prefix, material, type, TechRebornRecipesProvider::getNamePart1)))
	}

	def offerChestplateRecipe(def material, ItemLike output, prefix = "", String type = "chestplate") {
		createMonoShapeRecipe(material, output, 'X' as char)
				.pattern("X X")
				.pattern("XXX")
				.pattern("XXX")
				.save(this.exporter, getRecipeKey(materialTypeString(prefix, material, type, TechRebornRecipesProvider::getNamePart1)))
	}

	def offerHelmetRecipe(def material, ItemLike output, prefix = "", String type = "helmet") {
		createMonoShapeRecipe(material, output, 'X' as char)
				.pattern("XXX")
				.pattern("X X")
				.save(this.exporter, getRecipeKey(materialTypeString(prefix, material, type, TechRebornRecipesProvider::getNamePart1)))
	}

	def offerLeggingsRecipe(def material, ItemLike output, prefix = "", String type = "leggings") {
		createMonoShapeRecipe(material, output, 'X' as char)
				.pattern("XXX")
				.pattern("X X")
				.pattern("X X")
				.save(this.exporter, getRecipeKey(materialTypeString(prefix, material, type, TechRebornRecipesProvider::getNamePart1)))
	}

}

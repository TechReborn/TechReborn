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
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
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
		generateArmorRecipes()
		generateBatteryRecipes()
		generateCableRecipes()
		generateIngotRecipes()
//		generateMachineRecipes()
		generateMachineBlockRecipes()
//		generateMiscBlockRecipes()
		generatePartRecipes()
//		generateSolarPanelRecipes()
//		generateStorageBlockRecipes()
		generateToolRecipes()
//		generateUnitRecipes()
//		generateUpgradeRecipes()
		generateUuMatterRecipes()
	}
	def generateArmorRecipes() {
		String rootDir = "crafting_table/armor/"
		// add boots
		[
				(TRContent.Ingots.BRONZE.asTag()): TRContent.BRONZE_BOOTS,
				(TRContent.Gems.PERIDOT)         : TRContent.PERIDOT_BOOTS,
				(TRContent.Gems.RUBY)            : TRContent.RUBY_BOOTS,
				(TRContent.Gems.SAPPHIRE)        : TRContent.SAPPHIRE_BOOTS,
				(TRContent.Ingots.SILVER)        : TRContent.SILVER_BOOTS,
				(TRContent.Ingots.STEEL)         : TRContent.STEEL_BOOTS
		].each { material, boots ->
			offerBootsRecipe(material, boots, rootDir)
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
			offerChestplateRecipe(material, chestplate, rootDir)
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
			offerHelmetRecipe(material, helmet, rootDir)
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
			offerLeggingsRecipe(material, leggings, rootDir)
		}
		//trinkets
		ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.MISC, TRContent.LITHIUM_ION_BATPACK)
			.pattern("BCB")
			.pattern("BPB")
			.pattern("B B")
			.define('C' as Character, TRContent.Parts.ADVANCED_CIRCUIT)
			.define('P' as Character, TRContent.Plates.ALUMINUM.asTag())
			.define('B' as Character, TRContent.LITHIUM_ION_BATTERY)
			.unlockedBy("has_lithium_ion_battery", getCriterionConditions(TRContent.LITHIUM_ION_BATTERY))
			.save(this.exporter, getRecipeKey(rootDir+"lithium_ion_batpack"))
		ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.MISC, TRContent.LAPOTRONIC_ORBPACK)
			.pattern("FOF")
			.pattern("SPS")
			.pattern("FIF")
			.define('F' as Character, TRContent.Parts.ENERGY_FLOW_CHIP)
			.define('O' as Character, TRContent.LAPOTRONIC_ORB)
			.define('S' as Character, TRContent.Parts.SUPERCONDUCTOR)
			.define('P' as Character, TRContent.LITHIUM_ION_BATPACK)
			.define('I' as Character, TRContent.Ingots.IRIDIUM.asTag())
			.unlockedBy("has_lapotronic_orb", getCriterionConditions(TRContent.LAPOTRONIC_ORB))
			.save(this.exporter, getRecipeKey(rootDir+"lapotronic_orbpack"))
		ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.MISC, TRContent.CLOAKING_DEVICE)
			.pattern("CIC")
			.pattern("IOI")
			.pattern("CIC")
			.define('C' as Character, TRContent.Ingots.CHROME.asTag())
			.define('I' as Character, TRContent.Plates.IRIDIUM_ALLOY.asTag())
			.define('O' as Character, TRContent.LAPOTRONIC_ORB)
			.unlockedBy("has_lapotronic_orb", getCriterionConditions(TRContent.LAPOTRONIC_ORB))
			.save(this.exporter, getRecipeKey(rootDir+"cloaking_device"))
		//nano
		ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.MISC, TRContent.NANO_HELMET)
			.pattern("CEC")
			.pattern("C C")
			.define('C' as Character, TRContent.Plates.CARBON.asTag())
			.define('E' as Character, TRContent.ENERGY_CRYSTAL)
			.unlockedBy("has_energy_crystal", getCriterionConditions(TRContent.ENERGY_CRYSTAL))
			.save(this.exporter, getRecipeKey(rootDir+"nano_helmet"))
		ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.MISC, TRContent.NANO_CHESTPLATE)
			.pattern("C C")
			.pattern("CEC")
			.pattern("CAC")
			.define('C' as Character, TRContent.Plates.CARBON.asTag())
			.define('A' as Character, TRContent.Parts.ADVANCED_CIRCUIT)
			.define('E' as Character, TRContent.ENERGY_CRYSTAL)
			.unlockedBy("has_energy_crystal", getCriterionConditions(TRContent.ENERGY_CRYSTAL))
			.save(this.exporter, getRecipeKey(rootDir+"nano_chestplate"))
		ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.MISC, TRContent.NANO_LEGGINGS)
			.pattern("CAC")
			.pattern("E E")
			.pattern("C C")
			.define('C' as Character, TRContent.Plates.CARBON.asTag())
			.define('A' as Character, TRContent.Parts.ADVANCED_CIRCUIT)
			.define('E' as Character, TRContent.ENERGY_CRYSTAL)
			.unlockedBy("has_energy_crystal", getCriterionConditions(TRContent.ENERGY_CRYSTAL))
			.save(this.exporter, getRecipeKey(rootDir+"nano_leggings"))
		ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.MISC, TRContent.NANO_BOOTS)
			.pattern("E E")
			.pattern("C C")
			.define('C' as Character, TRContent.Plates.CARBON.asTag())
			.define('E' as Character, TRContent.ENERGY_CRYSTAL)
			.unlockedBy("has_energy_crystal", getCriterionConditions(TRContent.ENERGY_CRYSTAL))
			.save(this.exporter, getRecipeKey(rootDir+"nano_boots"))
		//quantum
		ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.MISC, TRContent.QUANTUM_HELMET)
			.pattern("DLD")
			.pattern("S S")
			.define('S' as Character, TRContent.Parts.SUPERCONDUCTOR)
			.define('L' as Character, TRContent.LAPOTRON_CRYSTAL)
			.define('D' as Character, TRContent.Parts.DATA_STORAGE_CHIP)
			.unlockedBy("has_lapotron_crystal", getCriterionConditions(TRContent.LAPOTRON_CRYSTAL))
			.save(this.exporter, getRecipeKey(rootDir+"quantum_helmet"))
		ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.MISC, TRContent.QUANTUM_CHESTPLATE)
			.pattern("P P")
			.pattern("SLS")
			.pattern("DID")
			.define('P' as Character, TRContent.Plates.TUNGSTENSTEEL.asTag())
			.define('S' as Character, TRContent.Parts.SUPERCONDUCTOR)
			.define('L' as Character, TRContent.LAPOTRON_CRYSTAL)
			.define('D' as Character, TRContent.Parts.DATA_STORAGE_CHIP)
			.define('I' as Character, TRContent.NuclearReactorComponents.IRIDIUM_NEUTRON_REFLECTOR)
			.unlockedBy("has_lapotron_crystal", getCriterionConditions(TRContent.LAPOTRON_CRYSTAL))
			.save(this.exporter, getRecipeKey(rootDir+"quantum_chestplate"))
		ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.MISC, TRContent.QUANTUM_LEGGINGS)
			.pattern("DLD")
			.pattern("S S")
			.pattern("P P")
			.define('P' as Character, TRContent.Plates.TUNGSTENSTEEL.asTag())
			.define('S' as Character, TRContent.Parts.SUPERCONDUCTOR)
			.define('L' as Character, TRContent.LAPOTRON_CRYSTAL)
			.define('D' as Character, TRContent.Parts.DATA_STORAGE_CHIP)
			.unlockedBy("has_lapotron_crystal", getCriterionConditions(TRContent.LAPOTRON_CRYSTAL))
			.save(this.exporter, getRecipeKey(rootDir+"quantum_leggings"))
		ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.MISC, TRContent.QUANTUM_BOOTS)
			.pattern("L L")
			.pattern("D D")
			.pattern("S S")
			.define('S' as Character, TRContent.Parts.SUPERCONDUCTOR)
			.define('L' as Character, TRContent.LAPOTRON_CRYSTAL)
			.define('D' as Character, TRContent.Parts.DATA_STORAGE_CHIP)
			.unlockedBy("has_lapotron_crystal", getCriterionConditions(TRContent.LAPOTRON_CRYSTAL))
			.save(this.exporter, getRecipeKey(rootDir+"quantum_boots"))
	}
	def generateBatteryRecipes(){
		String rootDir = "crafting_table/battery/"
		ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.MISC, TRContent.RED_CELL_BATTERY)
			.pattern(" W ")
			.pattern("TRT")
			.pattern("TRT")
			.define('W' as Character, TRContent.Cables.INSULATED_COPPER)
			.define('R' as Character, Items.REDSTONE)
			.define('T' as Character, TRContent.Ingots.LEAD.asTag())
			.unlockedBy("has_insulated_copper_cable", getCriterionConditions(TRContent.Cables.INSULATED_COPPER))
			.save(this.exporter, getRecipeKey(rootDir+"red_cell_battery"))
		ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.MISC, TRContent.LAPOTRON_CRYSTAL)
			.pattern("LCL")
			.pattern("LEL")
			.pattern("LCL")
			.define('L' as Character, TRContent.Plates.LAZURITE.asTag())
			.define('C' as Character, TRContent.Parts.INDUSTRIAL_CIRCUIT)
			.define('E' as Character, TRContent.ENERGY_CRYSTAL)
			.unlockedBy("has_industrial_circuit", getCriterionConditions(TRContent.Parts.INDUSTRIAL_CIRCUIT))
			.save(this.exporter, getRecipeKey(rootDir+"lapotron_crystal"))
		ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.MISC, TRContent.LAPOTRONIC_ORB)
			.pattern("LLL")
			.pattern("LPL")
			.pattern("LLL")
			.define('L' as Character, TRContent.LAPOTRON_CRYSTAL)
			.define('P' as Character, TRContent.Plates.IRIDIUM_ALLOY.asTag())
			.unlockedBy("has_lapotron_crystal", getCriterionConditions(TRContent.LAPOTRON_CRYSTAL))
			.save(this.exporter, getRecipeKey(rootDir+"lapotronic_orb"))
	}
	def generateCableRecipes(){
		String rootDir = "crafting_table/cable/"
		//low
		ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.MISC, TRContent.Cables.TIN,9)
			.pattern("III")
			.define('I' as Character, TRContent.Ingots.TIN.asTag())
			.unlockedBy("has_tin_ingot", getCriterionConditions(TRContent.Ingots.TIN.asTag()))
			.save(this.exporter, getRecipeKey(rootDir+"tin_cable"))
		//medium
		ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.MISC, TRContent.Cables.COPPER,6)
			.pattern("III")
			.define('I' as Character, Items.COPPER_INGOT)
			.unlockedBy("has_copper_ingot", getCriterionConditions(Items.COPPER_INGOT))
			.save(this.exporter, getRecipeKey(rootDir+"copper_cable"))
		ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.MISC, TRContent.Cables.INSULATED_COPPER,6)
			.pattern("RRR")
			.pattern("III")
			.pattern("RRR")
			.define('I' as Character, Items.COPPER_INGOT)
			.define('R' as Character, TRContent.Parts.RUBBER)
			.unlockedBy("has_copper_ingot", getCriterionConditions(Items.COPPER_INGOT))
			.save(this.exporter, getRecipeKey(rootDir+"insulated_copper_cable"))
		ShapelessRecipeBuilder.shapeless(itemLookup, RecipeCategory.MISC, TRContent.Cables.INSULATED_COPPER)
			.requires(TRContent.Parts.RUBBER)
			.requires(TRContent.Cables.COPPER)
			.unlockedBy("has_copper_cable", getCriterionConditions(TRContent.Cables.COPPER))
			.save(this.exporter, getRecipeKey(rootDir+"insulated_copper_cable_shapeless"))
		//high
		ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.MISC, TRContent.Cables.GOLD,12)
			.pattern("III")
			.define('I' as Character, Items.GOLD_INGOT)
			.unlockedBy("has_gold_ingot", getCriterionConditions(Items.GOLD_INGOT))
			.save(this.exporter, getRecipeKey(rootDir+"gold_cable"))
		ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.MISC, TRContent.Cables.INSULATED_GOLD,4)
			.pattern("RRR")
			.pattern("RIR")
			.pattern("RRR")
			.define('I' as Character, Items.GOLD_INGOT)
			.define('R' as Character, TRContent.Parts.RUBBER)
			.unlockedBy("has_gold_ingot", getCriterionConditions(Items.GOLD_INGOT))
			.save(this.exporter, getRecipeKey(rootDir+"insulated_gold_cable"))
		ShapelessRecipeBuilder.shapeless(itemLookup, RecipeCategory.MISC, TRContent.Cables.INSULATED_GOLD)
			.requires(TRContent.Parts.RUBBER,2)
			.requires(TRContent.Cables.GOLD)
			.unlockedBy("has_gold_cable", getCriterionConditions(TRContent.Cables.GOLD))
			.save(this.exporter, getRecipeKey(rootDir+"insulated_gold_cable_shapeless"))
		//extreme
		ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.MISC, TRContent.Cables.HV,12)
			.pattern("III")
			.define('I' as Character, TRContent.Ingots.REFINED_IRON.asTag())
			.unlockedBy("has_refined_iron_ingot", getCriterionConditions(TRContent.Ingots.REFINED_IRON.asTag()))
			.save(this.exporter, getRecipeKey(rootDir+"hv_cable"))
		ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.MISC, TRContent.Cables.INSULATED_HV,4)
			.pattern("RRR")
			.pattern("RIR")
			.pattern("RRR")
			.define('I' as Character, TRContent.Ingots.REFINED_IRON.asTag())
			.define('R' as Character, TRContent.Parts.RUBBER)
			.unlockedBy("has_refined_iron_ingot", getCriterionConditions(TRContent.Ingots.REFINED_IRON.asTag()))
			.save(this.exporter, getRecipeKey(rootDir+"insulated_hv_cable"))
		ShapelessRecipeBuilder.shapeless(itemLookup, RecipeCategory.MISC, TRContent.Cables.INSULATED_HV)
			.requires(TRContent.Parts.RUBBER,2)
			.requires(TRContent.Ingots.REFINED_IRON.asTag())
			.unlockedBy("has_hv_cable", getCriterionConditions(TRContent.Ingots.REFINED_IRON.asTag()))
			.save(this.exporter, getRecipeKey(rootDir+"insulated_hv_cable_shapeless"))
		//insane
		ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.MISC, TRContent.Cables.GLASSFIBER)
			.pattern("GGG")
			.pattern("EDE")
			.pattern("GGG")
			.define('G' as Character, Items.GLASS)
			.define('E' as Character, TRContent.Dusts.ELECTRUM)
			.define('D' as Character, TRContent.Dusts.DIAMOND)
			.unlockedBy("has_electrum_dust", getCriterionConditions(TRContent.Dusts.ELECTRUM.asTag()))
			.save(this.exporter, getRecipeKey(rootDir+"glassfiber_cable"))
		//infinite
		ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.MISC, TRContent.Cables.SUPERCONDUCTOR)
			.pattern("MFM")
			.pattern("SSS")
			.pattern("MFM")
			.define('M' as Character, TRContent.MachineBlocks.ADVANCED.frame)
			.define('F' as Character, TRContent.Parts.ENERGY_FLOW_CHIP)
			.define('S' as Character, TRContent.Parts.SUPERCONDUCTOR)
			.unlockedBy("has_lapotron_crystal", getCriterionConditions(TRContent.LAPOTRON_CRYSTAL))
			.save(this.exporter, getRecipeKey(rootDir+"superconductor_cable"))
	}
	def generateIngotRecipes(){
		//x2
		mixed_metal_ingot(TRContent.Ingots.REFINED_IRON.asTag(),TRContent.Ingots.BRONZE.asTag(),TRContent.Ingots.TIN.asTag(),2,"iron1")
		mixed_metal_ingot(TRContent.Ingots.REFINED_IRON.asTag(),TRContent.Ingots.BRASS.asTag(),TRContent.Ingots.TIN.asTag(),2,"iron2")
		mixed_metal_ingot(TRContent.Ingots.REFINED_IRON.asTag(),TRContent.Ingots.BRONZE.asTag(),TRContent.Ingots.ZINC.asTag(),2,"iron3")
		mixed_metal_ingot(TRContent.Ingots.REFINED_IRON.asTag(),TRContent.Ingots.BRASS.asTag(),TRContent.Ingots.ZINC.asTag(),2,"iron4")
		//x3
		mixed_metal_ingot(TRContent.Ingots.REFINED_IRON.asTag(),TRContent.Ingots.BRONZE.asTag(),TRContent.Ingots.ALUMINUM.asTag(),3,"iron5")
		mixed_metal_ingot(TRContent.Ingots.REFINED_IRON.asTag(),TRContent.Ingots.BRASS.asTag(),TRContent.Ingots.ALUMINUM.asTag(),3,"iron6")
		mixed_metal_ingot(TRContent.Ingots.NICKEL.asTag(),TRContent.Ingots.BRONZE.asTag(),TRContent.Ingots.TIN.asTag(),3,"nickel1")
		mixed_metal_ingot(TRContent.Ingots.NICKEL.asTag(),TRContent.Ingots.BRASS.asTag(),TRContent.Ingots.TIN.asTag(),3,"nickel2")
		mixed_metal_ingot(TRContent.Ingots.NICKEL.asTag(),TRContent.Ingots.BRONZE.asTag(),TRContent.Ingots.ZINC.asTag(),3,"nickel3")
		mixed_metal_ingot(TRContent.Ingots.NICKEL.asTag(),TRContent.Ingots.BRASS.asTag(),TRContent.Ingots.ZINC.asTag(),3,"nickel4")
		//x4
		mixed_metal_ingot(TRContent.Ingots.NICKEL.asTag(),TRContent.Ingots.BRONZE.asTag(),TRContent.Ingots.ALUMINUM.asTag(),4,"nickel5")
		mixed_metal_ingot(TRContent.Ingots.NICKEL.asTag(),TRContent.Ingots.BRASS.asTag(),TRContent.Ingots.ALUMINUM.asTag(),4,"nickel6")
		mixed_metal_ingot(TRContent.Ingots.INVAR.asTag(),TRContent.Ingots.BRONZE.asTag(),TRContent.Ingots.TIN.asTag(),4,"invar1")
		mixed_metal_ingot(TRContent.Ingots.INVAR.asTag(),TRContent.Ingots.BRASS.asTag(),TRContent.Ingots.TIN.asTag(),4,"invar2")
		mixed_metal_ingot(TRContent.Ingots.INVAR.asTag(),TRContent.Ingots.BRONZE.asTag(),TRContent.Ingots.ZINC.asTag(),4,"invar3")
		mixed_metal_ingot(TRContent.Ingots.INVAR.asTag(),TRContent.Ingots.BRASS.asTag(),TRContent.Ingots.ZINC.asTag(),4,"invar4")
		//x5
		mixed_metal_ingot(TRContent.Ingots.INVAR.asTag(),TRContent.Ingots.BRONZE.asTag(),TRContent.Ingots.ALUMINUM.asTag(),5,"invar5")
		mixed_metal_ingot(TRContent.Ingots.INVAR.asTag(),TRContent.Ingots.BRASS.asTag(),TRContent.Ingots.ALUMINUM.asTag(),5,"invar6")
		mixed_metal_ingot(TRContent.Ingots.TITANIUM.asTag(),TRContent.Ingots.BRONZE.asTag(),TRContent.Ingots.TIN.asTag(),5,"titanium1")
		mixed_metal_ingot(TRContent.Ingots.TITANIUM.asTag(),TRContent.Ingots.BRASS.asTag(),TRContent.Ingots.TIN.asTag(),5,"titanium2")
		mixed_metal_ingot(TRContent.Ingots.TITANIUM.asTag(),TRContent.Ingots.BRONZE.asTag(),TRContent.Ingots.ZINC.asTag(),5,"titanium3")
		mixed_metal_ingot(TRContent.Ingots.TITANIUM.asTag(),TRContent.Ingots.BRASS.asTag(),TRContent.Ingots.ZINC.asTag(),5,"titanium4")
		mixed_metal_ingot(TRContent.Ingots.TUNGSTEN.asTag(),TRContent.Ingots.BRONZE.asTag(),TRContent.Ingots.TIN.asTag(),5,"tungsten1")
		mixed_metal_ingot(TRContent.Ingots.TUNGSTEN.asTag(),TRContent.Ingots.BRASS.asTag(),TRContent.Ingots.TIN.asTag(),5,"tungsten2")
		mixed_metal_ingot(TRContent.Ingots.TUNGSTEN.asTag(),TRContent.Ingots.BRONZE.asTag(),TRContent.Ingots.ZINC.asTag(),5,"tungsten3")
		mixed_metal_ingot(TRContent.Ingots.TUNGSTEN.asTag(),TRContent.Ingots.BRASS.asTag(),TRContent.Ingots.ZINC.asTag(),5,"tungsten4")
		//x6
		mixed_metal_ingot(TRContent.Ingots.TITANIUM.asTag(),TRContent.Ingots.BRONZE.asTag(),TRContent.Ingots.ALUMINUM.asTag(),6,"titanium5")
		mixed_metal_ingot(TRContent.Ingots.TITANIUM.asTag(),TRContent.Ingots.BRASS.asTag(),TRContent.Ingots.ALUMINUM.asTag(),6,"titanium6")
		mixed_metal_ingot(TRContent.Ingots.TUNGSTEN.asTag(),TRContent.Ingots.BRONZE.asTag(),TRContent.Ingots.ALUMINUM.asTag(),6,"tungsten5")
		mixed_metal_ingot(TRContent.Ingots.TUNGSTEN.asTag(),TRContent.Ingots.BRASS.asTag(),TRContent.Ingots.ALUMINUM.asTag(),6,"tungsten6")
		//x8
		mixed_metal_ingot(TRContent.Ingots.TUNGSTENSTEEL.asTag(),TRContent.Ingots.BRONZE.asTag(),TRContent.Ingots.TIN.asTag(),8,"tungsten_steel1")
		mixed_metal_ingot(TRContent.Ingots.TUNGSTENSTEEL.asTag(),TRContent.Ingots.BRASS.asTag(),TRContent.Ingots.TIN.asTag(),8,"tungsten_steel2")
		mixed_metal_ingot(TRContent.Ingots.TUNGSTENSTEEL.asTag(),TRContent.Ingots.BRONZE.asTag(),TRContent.Ingots.ZINC.asTag(),8,"tungsten_steel3")
		mixed_metal_ingot(TRContent.Ingots.TUNGSTENSTEEL.asTag(),TRContent.Ingots.BRASS.asTag(),TRContent.Ingots.ZINC.asTag(),8,"tungsten_steel4")
		//x9
		mixed_metal_ingot(TRContent.Ingots.TUNGSTENSTEEL.asTag(),TRContent.Ingots.BRONZE.asTag(),TRContent.Ingots.ALUMINUM.asTag(),9,"tungsten_steel5")
		mixed_metal_ingot(TRContent.Ingots.TUNGSTENSTEEL.asTag(),TRContent.Ingots.BRASS.asTag(),TRContent.Ingots.ALUMINUM.asTag(),9,"tungsten_steel6")

		String rootDir = "crafting_table/ingot/"
		ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.MISC, TRContent.Ingots.INDUSTRIAL_ALLOY)
			.pattern("SCS")
			.pattern("IMI")
			.pattern("SCS")
			.define('C' as Character, TRContent.Ingots.CHROME.asTag())
			.define('I' as Character, TRContent.Ingots.INVAR.asTag())
			.define('M' as Character, TRContent.SmallDusts.MANGANESE.asTag())
			.define('S' as Character, TRContent.Plates.STEEL.asTag())
			.unlockedBy("has_steel_ingot", getCriterionConditions(TRContent.Ingots.STEEL.asTag()))
			.save(this.exporter, getRecipeKey(rootDir+"industrial_alloy_ingot"))
		ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.MISC, TRContent.Ingots.IRIDIUM_ALLOY)
			.pattern("IAI")
			.pattern("ADA")
			.pattern("IAI")
			.define('A' as Character, TRContent.Plates.ADVANCED_ALLOY.asTag())
			.define('D' as Character, TRContent.Dusts.DIAMOND.asTag())
			.define('I' as Character, TRContent.Plates.IRIDIUM.asTag())
			.unlockedBy("has_iridium_ingot", getCriterionConditions(TRContent.Ingots.IRIDIUM.asTag()))
			.save(this.exporter, getRecipeKey(rootDir+"iridium_alloy_ingot"))
	}
	def generateMachineBlockRecipes(){
		String rootDir = "crafting_table/machine_block/"
		//basic:frame
		createMonoShapeRecipe(TRContent.Ingots.REFINED_IRON.asTag(), TRContent.MachineBlocks.BASIC.frame,
			'R' as char)
			.pattern("RRR")
			.pattern("R R")
			.pattern("RRR")
			.save(this.exporter, getRecipeKey(rootDir+"basic_machine_frame_refined_iron"))
		createMonoShapeRecipe(TRContent.Ingots.ALUMINUM.asTag(), TRContent.MachineBlocks.BASIC.frame,
			'A' as char)
			.pattern("AAA")
			.pattern("A A")
			.pattern("AAA")
			.save(this.exporter, getRecipeKey(rootDir+"basic_machine_frame_aluminum"))
		//basic:casing
		ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.MISC, TRContent.MachineBlocks.BASIC.casing)
			.pattern("CAC")
			.define('A' as Character, TRContent.MachineBlocks.BASIC.frame)
			.define('C' as Character, TRContent.Parts.ELECTRONIC_CIRCUIT)
			.unlockedBy("has_basic_machine_frame", getCriterionConditions(TRContent.MachineBlocks.BASIC.frame))
			.save(this.exporter, getRecipeKey(rootDir+"basic_machine_casing"))
		//advanced:frame
		ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.MISC, TRContent.MachineBlocks.ADVANCED.frame)
			.pattern(" C ")
			.pattern("AMA")
			.pattern(" C ")
			.define('A' as Character, TRContent.Plates.ADVANCED_ALLOY.asTag())
			.define('C' as Character, TRContent.Plates.CARBON.asTag())
			.define('M' as Character, TRContent.MachineBlocks.BASIC.frame)
			.unlockedBy("has_basic_machine_frame", getCriterionConditions(TRContent.MachineBlocks.BASIC.frame))
			.save(this.exporter, getRecipeKey(rootDir+"advanced_machine_frame"))
		//advanced:casing
		ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.MISC, TRContent.MachineBlocks.ADVANCED.casing)
			.pattern(" R ")
			.pattern("CAC")
			.pattern(" R ")
			.define('A' as Character, TRContent.MachineBlocks.ADVANCED.frame)
			.define('R' as Character, TRContent.Plates.STEEL.asTag())
			.define('C' as Character, TRContent.Parts.ADVANCED_CIRCUIT)
			.unlockedBy("has_steel_ingot", getCriterionConditions(TRContent.Ingots.STEEL.asTag()))
			.save(this.exporter, getRecipeKey(rootDir+"advanced_machine_casing"))
		ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.MISC, TRContent.MachineBlocks.ADVANCED.casing)
			.pattern(" R ")
			.pattern("CAC")
			.pattern(" R ")
			.define('A' as Character, TRContent.MachineBlocks.BASIC.casing)
			.define('R' as Character, TRContent.Plates.STEEL.asTag())
			.define('C' as Character, TRContent.Parts.ADVANCED_CIRCUIT)
			.unlockedBy("has_steel_ingot", getCriterionConditions(TRContent.Ingots.STEEL.asTag()))
			.save(this.exporter, getRecipeKey(rootDir+"advanced_machine_casing_alt"))
		//industrial:frame
		ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.MISC, TRContent.MachineBlocks.INDUSTRIAL.frame)
			.pattern("CTC")
			.pattern("TBT")
			.pattern("CTC")
			.define('B' as Character, TRContent.MachineBlocks.ADVANCED.frame)
			.define('C' as Character, TRContent.Plates.CHROME.asTag())
			.define('T' as Character, TRContent.Plates.TITANIUM.asTag())
			.unlockedBy("has_titanium_ingot", getCriterionConditions(TRContent.Ingots.TITANIUM.asTag()))
			.save(this.exporter, getRecipeKey(rootDir+"industrial_machine_frame"))
		//industrial:casing
		ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.MISC, TRContent.MachineBlocks.INDUSTRIAL.casing)
			.pattern(" R ")
			.pattern("CAC")
			.pattern(" R ")
			.define('A' as Character, TRContent.MachineBlocks.INDUSTRIAL.frame)
			.define('R' as Character, TRContent.Plates.CHROME.asTag())
			.define('C' as Character, TRContent.Parts.DATA_STORAGE_CORE)
			.unlockedBy("has_chromium_ingot", getCriterionConditions(TRContent.Ingots.CHROME.asTag()))
			.save(this.exporter, getRecipeKey(rootDir+"industrial_machine_casing"))
		ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.MISC, TRContent.MachineBlocks.INDUSTRIAL.casing)
			.pattern(" R ")
			.pattern("CAC")
			.pattern(" R ")
			.define('A' as Character, TRContent.MachineBlocks.ADVANCED.casing)
			.define('R' as Character, TRContent.Plates.CHROME.asTag())
			.define('C' as Character, TRContent.Parts.DATA_STORAGE_CORE)
			.unlockedBy("has_chromium_ingot", getCriterionConditions(TRContent.Ingots.CHROME.asTag()))
			.save(this.exporter, getRecipeKey(rootDir+"industrial_machine_casing_alt"))
	}
	def generatePartRecipes(){
		String rootDir = "crafting_table/parts/"
		//guidebook
		ShapelessRecipeBuilder.shapeless(itemLookup, RecipeCategory.MISC, new ItemStackTemplate(TRContent.MANUAL))
			.requires(Items.BOOK)
			.requires(TRContent.Ingots.REFINED_IRON.asTag())
			.unlockedBy("has_refined_iron_ingot", getCriterionConditions(TRContent.Ingots.REFINED_IRON.asTag()))
			.save(this.exporter, getRecipeKey(rootDir+"manual"))
		//rubber
		ShapelessRecipeBuilder.shapeless(itemLookup, RecipeCategory.MISC, new ItemStackTemplate(Items.RESIN_CLUMP, 2))
			.requires(TRContent.Parts.SAP, 2)
			.requires(Items.SLIME_BALL)
			.unlockedBy("has_sap", getCriterionConditions(TRContent.Parts.SAP))
			.save(this.exporter, getRecipeKey(rootDir+"resin_clump"))
		ShapelessRecipeBuilder.shapeless(itemLookup, RecipeCategory.MISC, new ItemStackTemplate(TRContent.Parts.SAP.asItem(), 8))
			.requires(Items.RESIN_CLUMP, 4)
			.requires(Items.WATER_BUCKET)
			.unlockedBy("has_resin_clump", getCriterionConditions(Items.RESIN_CLUMP))
			.save(this.exporter, getRecipeKey(rootDir+"sap"))
		//carbon
		ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.MISC, TRContent.Parts.CARBON_FIBER)
			.pattern(" C ")
			.pattern("C C")
			.pattern(" C ")
			.define('C' as Character, TRContent.Dusts.COAL)
			.unlockedBy("has_coal", getCriterionConditions(Items.COAL))
			.save(this.exporter, getRecipeKey(rootDir+"carbon_fiber"))
		ShapelessRecipeBuilder.shapeless(itemLookup, RecipeCategory.MISC, TRContent.Parts.CARBON_MESH)
			.requires(TRContent.Parts.CARBON_FIBER,2)
			.unlockedBy("has_coal", getCriterionConditions(Items.COAL))
			.save(this.exporter, getRecipeKey(rootDir+"carbon_mesh"))
		//circuit
		ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.MISC, TRContent.Parts.ELECTRONIC_CIRCUIT)
			.pattern("WWW")
			.pattern("SRS")
			.pattern("WWW")
			.define('R' as Character, TRContent.Ingots.REFINED_IRON.asTag())
			.define('W' as Character, TRContent.Cables.INSULATED_COPPER)
			.define('S' as Character, Items.REDSTONE)
			.unlockedBy("has_refined_iron_ingot", getCriterionConditions(TRContent.Ingots.REFINED_IRON.asTag()))
			.save(this.exporter, getRecipeKey(rootDir+"electronic_circuit"))
		ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.MISC, TRContent.Parts.ADVANCED_CIRCUIT)
			.pattern("RGR")
			.pattern("LCL")
			.pattern("RGR")
			.define('R' as Character, Items.REDSTONE)
			.define('C' as Character, TRContent.Parts.ELECTRONIC_CIRCUIT)
			.define('G' as Character, Items.GLOWSTONE_DUST)
			.define('L' as Character, Items.LAPIS_LAZULI)
			.unlockedBy("has_electronic_circuit", getCriterionConditions(TRContent.Parts.ELECTRONIC_CIRCUIT))
			.save(this.exporter, getRecipeKey(rootDir+"advanced_circuit"))
		//display
		ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.MISC, TRContent.Parts.BASIC_DISPLAY)
			.pattern("ADA")
			.pattern("DCD")
			.pattern("AGA")
			.define('A' as Character, TRContent.Plates.REFINED_IRON.asTag())
			.define('G' as Character, TRContent.Parts.ELECTRONIC_CIRCUIT)
			.define('C' as Character, Items.GLASS_PANE)
			.define('D' as Character, Items.DYE.black())
			.unlockedBy("has_refined_iron_ingot", getCriterionConditions(TRContent.Ingots.REFINED_IRON.asTag()))
			.save(this.exporter, getRecipeKey(rootDir+"basic_display"))
		ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.MISC, TRContent.Parts.DIGITAL_DISPLAY)
			.pattern("ADA")
			.pattern("DCD")
			.pattern("AGA")
			.define('A' as Character, TRContent.Plates.ALUMINUM.asTag())
			.define('G' as Character, TRContent.Parts.ELECTRONIC_CIRCUIT)
			.define('C' as Character, Items.GLASS_PANE)
			.define('D' as Character, Items.DYE.black())
			.unlockedBy("has_aluminum_ingot", getCriterionConditions(TRContent.Ingots.ALUMINUM.asTag()))
			.save(this.exporter, getRecipeKey(rootDir+"digital_display"))
		//cell
		createMonoShapeRecipe(TRContent.Ingots.TIN.asTag(), TRContent.Cells.EMPTY,
			'T' as char,4)
			.pattern(" T ")
			.pattern("T T")
			.pattern(" T ")
			.save(this.exporter, getRecipeKey("crafting_table/cell_tin"))
		createMonoShapeRecipe(TRContent.Ingots.ALUMINUM.asTag(), TRContent.Cells.EMPTY,
			'A' as char,4)
			.pattern(" A ")
			.pattern("A A")
			.pattern(" A ")
			.save(this.exporter, getRecipeKey("crafting_table/cell_aluminum"))
		//scrap
		ShapelessRecipeBuilder.shapeless(itemLookup, RecipeCategory.MISC, new ItemStackTemplate(TRContent.SCRAP_BOX))
			.requires(TRContent.Parts.SCRAP, 9)
			.unlockedBy("has_scrap", getCriterionConditions(TRContent.Parts.SCRAP))
			.save(this.exporter, getRecipeKey("crafting_table/scrap_box"))
		//template
		createDuoShapeRecipe(Items.DIAMOND, TRContent.Nuggets.NETHERITE.asTag(), TRContent.Parts.TEMPLATE_TEMPLATE,
			'D' as char, 'N' as char)
			.pattern("NDN")
			.pattern("DDD")
			.pattern("NDN")
			.save(this.exporter, getRecipeKey(rootDir+TRContent.Parts.TEMPLATE_TEMPLATE.name))
		//vanilla
		ShapelessRecipeBuilder.shapeless(itemLookup, RecipeCategory.MISC, Items.GUNPOWDER,2)
			.requires(TRContent.Dusts.CHARCOAL.asTag())
			.requires(TRContent.Dusts.SULFUR.asTag())
			.requires(TRContent.Dusts.SALTPETER.asTag())
			.requires(TRContent.Dusts.SALTPETER.asTag())
			.unlockedBy("has_saltpeter_dust", getCriterionConditions(TRContent.Dusts.SALTPETER.asTag()))
			.save(this.exporter, getRecipeKey("crafting_table/tech_reborn_gunpowder"))
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

	def mixed_metal_ingot(TagKey main_metal,TagKey copper_alloy,TagKey base_metal,int count,String random){
		String rootDir = "crafting_table/ingot/"
		ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.MISC, TRContent.Ingots.MIXED_METAL,count)
			.pattern("MMM")
			.pattern("CCC")
			.pattern("BBB")
			.define('M' as Character, main_metal)
			.define('C' as Character, copper_alloy)
			.define('B' as Character, base_metal)
			.unlockedBy("has_bronze_ingot", getCriterionConditions(TRContent.Ingots.BRONZE.asTag()))
			.save(this.exporter, getRecipeKey(rootDir+"mixed_metal_ingot_"+random))
	}

}

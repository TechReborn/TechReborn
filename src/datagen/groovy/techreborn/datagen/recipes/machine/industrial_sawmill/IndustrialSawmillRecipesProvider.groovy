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

package techreborn.datagen.recipes.machine.industrial_sawmill

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.ItemTags
import net.minecraft.resource.featuretoggle.FeatureFlags
import techreborn.datagen.recipes.TechRebornRecipesProvider
import techreborn.init.TRContent

import java.util.concurrent.CompletableFuture

class IndustrialSawmillRecipesProvider extends TechRebornRecipesProvider {

	IndustrialSawmillRecipesProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture)
	}

	@Override
	void generateRecipes() {
		[
			(ItemTags.ACACIA_LOGS): Items.ACACIA_PLANKS,
			(ItemTags.BIRCH_LOGS): Items.BIRCH_PLANKS,
			(ItemTags.DARK_OAK_LOGS): Items.DARK_OAK_PLANKS,
			(ItemTags.JUNGLE_LOGS): Items.JUNGLE_PLANKS,
			(ItemTags.OAK_LOGS): Items.OAK_PLANKS,
			(ItemTags.SPRUCE_LOGS): Items.SPRUCE_PLANKS,
			(ItemTags.CRIMSON_STEMS): Items.CRIMSON_PLANKS,
			(ItemTags.WARPED_STEMS): Items.WARPED_PLANKS,
			(ItemTags.MANGROVE_LOGS): Items.MANGROVE_PLANKS,
			(TRContent.ItemTags.RUBBER_LOGS): TRContent.RUBBER_PLANKS
		].each {logs, planks ->
			offerIndustrialSawmillRecipe {
				ingredients logs
				outputs new ItemStack(planks,4), new ItemStack(TRContent.Dusts.SAW, 3)
				power 40
				time 200
				fluidAmount 1000 // in millibuckets
			}
		}
		offerIndustrialSawmillRecipe {
			ingredients ItemTags.BAMBOO_BLOCKS
			outputs new ItemStack(Items.BAMBOO_PLANKS,2), new ItemStack(TRContent.Dusts.SAW, 1)
			power 40
			time 100
			fluidAmount 500 // in millibuckets
			feature FeatureFlags.UPDATE_1_20
		}
		[
			(Items.ACACIA_STAIRS): Items.ACACIA_SLAB,
			(Items.BIRCH_STAIRS): Items.BIRCH_SLAB,
			(Items.DARK_OAK_STAIRS): Items.DARK_OAK_SLAB,
			(Items.JUNGLE_STAIRS): Items.JUNGLE_SLAB,
			(Items.OAK_STAIRS): Items.OAK_SLAB,
			(Items.SPRUCE_STAIRS): Items.SPRUCE_SLAB,
			(Items.CRIMSON_STAIRS): Items.CRIMSON_SLAB,
			(Items.WARPED_STAIRS): Items.WARPED_SLAB,
			(Items.MANGROVE_STAIRS): Items.MANGROVE_SLAB,
			(TRContent.RUBBER_STAIR): TRContent.RUBBER_SLAB
		].each { stairs, slab ->
			offerIndustrialSawmillRecipe {
				ingredients stairs
				outputs slab, new ItemStack(TRContent.Dusts.SAW, 2)
				power 30
				time 100
				fluidAmount 250 // in millibuckets
				source "stairs"
				criterion getCriterionName(stairs), getCriterionConditions(stairs)
			}
		}
		[
			(Items.BAMBOO_STAIRS): Items.BAMBOO_SLAB,
			(Items.BAMBOO_MOSAIC_STAIRS): Items.BAMBOO_MOSAIC_SLAB
		].each { stairs, slab ->
			offerIndustrialSawmillRecipe {
				ingredients stairs
				outputs slab, new ItemStack(TRContent.Dusts.SAW, 2)
				power 30
				time 100
				fluidAmount 250 // in millibuckets
				source "stairs"
				criterion getCriterionName(stairs), getCriterionConditions(stairs)
				feature FeatureFlags.UPDATE_1_20
			}
		}
		[
			(Items.ACACIA_SLAB): Items.ACACIA_PRESSURE_PLATE,
			(Items.BIRCH_SLAB): Items.BIRCH_PRESSURE_PLATE,
			(Items.DARK_OAK_SLAB): Items.DARK_OAK_PRESSURE_PLATE,
			(Items.JUNGLE_SLAB): Items.JUNGLE_PRESSURE_PLATE,
			(Items.OAK_SLAB): Items.OAK_PRESSURE_PLATE,
			(Items.SPRUCE_SLAB): Items.SPRUCE_PRESSURE_PLATE,
			(Items.CRIMSON_SLAB): Items.CRIMSON_PRESSURE_PLATE,
			(Items.WARPED_SLAB): Items.WARPED_PRESSURE_PLATE,
			(Items.MANGROVE_SLAB): Items.MANGROVE_PRESSURE_PLATE,
			(TRContent.RUBBER_SLAB): TRContent.RUBBER_PRESSURE_PLATE
		].each { slab, plate ->
			offerIndustrialSawmillRecipe {
				ingredients slab
				outputs new ItemStack(plate, 2), new ItemStack(TRContent.Dusts.SAW, 2)
				power 30
				time 200
				fluidAmount 250 // in millibuckets
				source "slab"
				criterion getCriterionName(slab), getCriterionConditions(slab)
			}
		}
		[
			(Items.BAMBOO_SLAB): Items.BAMBOO_PRESSURE_PLATE,
			(Items.BAMBOO_MOSAIC_SLAB): Items.BAMBOO_PRESSURE_PLATE
		].each { slab, plate ->
			offerIndustrialSawmillRecipe {
				ingredients slab
				outputs new ItemStack(plate, 2), new ItemStack(TRContent.Dusts.SAW, 2)
				power 30
				time 200
				fluidAmount 250 // in millibuckets
				source ((slab == Items.BAMBOO_MOSAIC_SLAB ? "mosaic_" : "") + "slab")
				criterion getCriterionName(slab), getCriterionConditions(slab)
				feature FeatureFlags.UPDATE_1_20
			}
		}
		[
			(ItemTags.PLANKS): 8,
			// stairs would be 6, slabs 4
			// but we don't add them, of course, because the recipes for these were added above
			(ItemTags.WOODEN_PRESSURE_PLATES): 1,
			(ItemTags.WOODEN_TRAPDOORS): 1, // not 2 because they often have holes
			(ItemTags.WOODEN_FENCES): 1,
			(ItemTags.WOODEN_DOORS): 3,
			(ItemTags.SIGNS): 1
		].each { item, count ->
			offerIndustrialSawmillRecipe {
				ingredients item
				outputs new ItemStack(TRContent.Dusts.SAW, count)
				power 30
				time 200
				fluidAmount 125*count // in millibuckets
				source item.id().path
				criterion getCriterionName(item), getCriterionConditions(item)
			}
		}
		[
			(Items.BOWL) : 2,
			(Items.MANGROVE_ROOTS) : 2,
			(TRContent.TREE_TAP) : 3,
			(Items.WOODEN_SHOVEL) : 2,
			(Items.WOODEN_SWORD) : 2,
			(Items.WOODEN_HOE) : 3,
			(Items.WOODEN_AXE) : 5,
			(Items.WOODEN_PICKAXE): 5
		].each {item, count ->
			offerIndustrialSawmillRecipe {
				ingredients item
				outputs new ItemStack(TRContent.SmallDusts.SAW, count)
				power 30
				time 100
				fluidAmount 100 // in millibuckets
				source item.toString()
				criterion getCriterionName(item), getCriterionConditions(item)
			}
		}
		offerIndustrialSawmillRecipe {
			ingredients ItemTags.WOODEN_BUTTONS
			outputs new ItemStack(TRContent.SmallDusts.SAW, 1)
			power 20
			time 50
			fluidAmount 50 // in millibuckets
			source "wooden_buttons"
			criterion getCriterionName(ItemTags.WOODEN_BUTTONS), getCriterionConditions(ItemTags.WOODEN_BUTTONS)
		}
	}
}

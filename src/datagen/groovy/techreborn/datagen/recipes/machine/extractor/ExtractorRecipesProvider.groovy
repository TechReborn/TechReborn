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

package techreborn.datagen.recipes.machine.extractor

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput
import net.minecraft.world.level.ItemLike
import net.minecraft.world.item.Items
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.HolderLookup
import net.minecraft.tags.ItemTags
import techreborn.datagen.recipes.TechRebornRecipesProvider
import techreborn.init.TRContent

import java.util.concurrent.CompletableFuture

class ExtractorRecipesProvider extends TechRebornRecipesProvider {
	ExtractorRecipesProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture)
	}

	@Override
	void generateRecipes() {
		generateDoubleDyes()
		generateQuadrupleDyes()
		generateDyesFromCoralBlock()
		generateDyesFromSmallCoral()
		generateDyesFromFroglight()
		generateDyesFromMisc()
		generateMisc()
		generateFluidExtraction()
	}

	// ONLY for doubling vanilla single dye recipes
	void generateDoubleDyes() {
		[
			(Items.INK_SAC) : Items.DYE.black(),
			(Items.WITHER_ROSE) : Items.DYE.black(),
			(Items.CORNFLOWER) :  Items.DYE.blue(),
			(Items.LAPIS_LAZULI) : Items.DYE.blue(),
			(Items.COCOA_BEANS) : Items.DYE.brown(),
			(Items.CLOSED_EYEBLOSSOM) : Items.DYE.gray(),
			(Items.BLUE_ORCHID) : Items.DYE.lightBlue(),
			(Items.AZURE_BLUET) : Items.DYE.lightGray(),
			(Items.OXEYE_DAISY) : Items.DYE.lightGray(),
			(Items.WHITE_TULIP) : Items.DYE.lightGray(),
			(Items.ALLIUM) : Items.DYE.magenta(),
			(Items.OPEN_EYEBLOSSOM) : Items.DYE.orange(),
			(Items.ORANGE_TULIP) : Items.DYE.orange(),
			(Items.TORCHFLOWER) : Items.DYE.orange(),
			(Items.PINK_TULIP) : Items.DYE.pink(),
			(Items.PINK_PETALS) : Items.DYE.pink(),
			(Items.POPPY) : Items.DYE.red(),
			(Items.RED_TULIP) : Items.DYE.red(),
			(Items.BONE_MEAL) : Items.DYE.white(),
			(Items.LILY_OF_THE_VALLEY) : Items.DYE.white(),
			(Items.DANDELION) : Items.DYE.yellow()
		].each { item, dye ->
			offerExtractorRecipe {
				ingredients item
				outputs stack(dye, 2)
				source item
				power 10
				time 300
				criterion getCriterionName(item), getCriterionConditions(item)
			}
		}
	}

	// ONLY for doubling vanilla double dye recipes
	void generateQuadrupleDyes() {
		[
			(Items.PITCHER_PLANT) : Items.DYE.cyan(),
			(Items.LILAC) : Items.DYE.magenta(),
			(Items.PEONY) : Items.DYE.pink(),
			(Items.ROSE_BUSH) : Items.DYE.red(),
			(Items.SUNFLOWER) : Items.DYE.yellow()
		].each { item, dye ->
			offerExtractorRecipe {
				ingredients item
				outputs stack(dye, 4)
				source item
				power 10
				time 300
				criterion getCriterionName(item), getCriterionConditions(item)
			}
		}
	}
	void generateDyesFromCoralBlock() {
		[
			(Items.TUBE_CORAL_BLOCK) : Items.DYE.blue(),
			(Items.BRAIN_CORAL_BLOCK) : Items.DYE.pink(),
			(Items.BUBBLE_CORAL_BLOCK) : Items.DYE.purple(),
			(Items.FIRE_CORAL_BLOCK) : Items.DYE.red(),
			(Items.HORN_CORAL_BLOCK) : Items.DYE.yellow(),
			(Items.DEAD_TUBE_CORAL_BLOCK) : Items.DYE.gray(),
			(Items.DEAD_BRAIN_CORAL_BLOCK) : Items.DYE.gray(),
			(Items.DEAD_BUBBLE_CORAL_BLOCK) : Items.DYE.gray(),
			(Items.DEAD_FIRE_CORAL_BLOCK) : Items.DYE.gray(),
			(Items.DEAD_HORN_CORAL_BLOCK) : Items.DYE.gray()
		].each { item, dye ->
			offerExtractorRecipe {
				ingredients item
				outputs stack(dye, 5)
				source item
				power 10
				time 400
				criterion getCriterionName(item), getCriterionConditions(item)
			}
		}
	}

	void generateDyesFromSmallCoral() {
		[
			(Items.TUBE_CORAL) : Items.DYE.blue(),
			(Items.TUBE_CORAL_FAN) : Items.DYE.blue(),
			(Items.BRAIN_CORAL) : Items.DYE.pink(),
			(Items.BRAIN_CORAL_FAN) : Items.DYE.pink(),
			(Items.BUBBLE_CORAL) : Items.DYE.purple(),
			(Items.BUBBLE_CORAL_FAN) : Items.DYE.purple(),
			(Items.FIRE_CORAL) : Items.DYE.red(),
			(Items.FIRE_CORAL_FAN) : Items.DYE.red(),
			(Items.HORN_CORAL) : Items.DYE.yellow(),
			(Items.HORN_CORAL_FAN) : Items.DYE.yellow(),
			(Items.DEAD_TUBE_CORAL) : Items.DYE.gray(),
			(Items.DEAD_TUBE_CORAL_FAN) : Items.DYE.gray(),
			(Items.DEAD_BRAIN_CORAL) : Items.DYE.gray(),
			(Items.DEAD_BRAIN_CORAL_FAN) : Items.DYE.gray(),
			(Items.DEAD_BUBBLE_CORAL) : Items.DYE.gray(),
			(Items.DEAD_BUBBLE_CORAL_FAN) : Items.DYE.gray(),
			(Items.DEAD_FIRE_CORAL) : Items.DYE.gray(),
			(Items.DEAD_FIRE_CORAL_FAN) : Items.DYE.gray(),
			(Items.DEAD_HORN_CORAL) : Items.DYE.gray(),
			(Items.DEAD_HORN_CORAL_FAN) : Items.DYE.gray()
		].each { item, dye ->
			offerExtractorRecipe {
				ingredients item
				outputs dye
				source item
				power 10
				time 200
				criterion getCriterionName(item), getCriterionConditions(item)
			}
		}

	}

	void generateDyesFromFroglight() {
		[
			(Items.OCHRE_FROGLIGHT) : Items.DYE.yellow(),
			(Items.VERDANT_FROGLIGHT) : Items.DYE.green(),
			(Items.PEARLESCENT_FROGLIGHT) : Items.DYE.purple()
		].each { item, dye ->
			offerExtractorRecipe {
				ingredients stack(item, 3)
				outputs dye
				source item
				power 10
				time 300
				criterion getCriterionName(item), getCriterionConditions(item)
			}
		}
	}

	void generateDyesFromMisc() {
		[
			(Items.PRISMARINE_SHARD) : Items.DYE.cyan(),
			(TRContent.Parts.PLANTBALL) : Items.DYE.green()
		].each { item, dye ->
			offerExtractorRecipe {
				ingredients item
				outputs dye
				source BuiltInRegistries.ITEM.getKey(item.asItem()).path
				power 10
				time 300
				criterion getCriterionName(item), getCriterionConditions(item)
			}
		}
		[
			(Items.SWEET_BERRIES) : Items.DYE.red(),
			(Items.GLOW_BERRIES) : Items.DYE.orange()
		].each { item, dye ->
			offerExtractorRecipe {
				ingredients stack(item, 4)
				outputs dye
				source item
				power 10
				time 300
				criterion getCriterionName(item), getCriterionConditions(item)
			}
		}
		offerExtractorRecipe {
			ingredients stack(Items.CARROT, 3)
			outputs Items.DYE.orange()
			source Items.CARROT
			power 10
			time 300
			criterion getCriterionName(Items.CARROT), getCriterionConditions(Items.CARROT)
		}
		offerExtractorRecipe {
			ingredients Items.BEETROOT
			outputs stack(Items.DYE.red(), 2)
			source Items.BEETROOT
			power 10
			time 300
			criterion getCriterionName(Items.BEETROOT), getCriterionConditions(Items.BEETROOT)
		}
		offerExtractorRecipe {
			ingredients Items.SHULKER_SHELL
			outputs stack(Items.DYE.purple(), 4)
			source Items.SHULKER_SHELL
			power 10
			time 300
			criterion getCriterionName(Items.SHULKER_SHELL), getCriterionConditions(Items.SHULKER_SHELL)
		}
	}

	void generateMisc() {
		offerExtractorRecipe {
			ingredients Items.CONDUIT
			outputs Items.HEART_OF_THE_SEA
			source Items.CONDUIT
			power 10
			time 1000
			criterion getCriterionName(Items.CONDUIT), getCriterionConditions(Items.CONDUIT)
		}
		[
			(Items.COD) : 64,
			(Items.PUFFERFISH) : 32
		].each { input, amount ->
			offerExtractorRecipe {
				ingredients stack(input, amount)
				outputs TRContent.Parts.SPONGE_PIECE
				source input.asItem()
				power 10
				time 1000
				criterion getCriterionName(input), getCriterionConditions(input)
			}
		}
		[
			(Items.CHERRY_LEAVES) : Items.PINK_PETALS,
			(Items.CLAY) : Items.CLAY_BALL
		].each { input, output ->
			offerExtractorRecipe {
				ingredients input
				outputs stack(output, 4)
				source input.asItem()
				power 10
				time 300
				criterion getCriterionName(input), getCriterionConditions(input)
			}
		}
		[
			(TRContent.Parts.SAP) : TRContent.Parts.RUBBER,
			(Items.ARMOR_STAND) : Items.STICK
		].each { input, output ->
			offerExtractorRecipe {
				ingredients input
				outputs stack(output, 3)
				source input.asItem()
				power 10
				time 300
				criterion getCriterionName(TRContent.Parts.SAP), getCriterionConditions(TRContent.Parts.SAP)
			}
		}
		[
			(Items.TNT) : Items.GUNPOWDER,
			(Items.ELYTRA) : Items.PHANTOM_MEMBRANE,
			(Items.SLIME_BALL) : TRContent.Parts.RUBBER,
			(Items.DEAD_BUSH) : Items.STICK
		].each { input, output ->
			offerExtractorRecipe {
				ingredients input
				outputs stack(output, 2)
				source input
				power 10
				time 300
				criterion getCriterionName(input), getCriterionConditions(input)
			}
		}
		[
			(TRContent.RUBBER_SAPLING) : TRContent.Parts.RUBBER,
			(Items.STICKY_PISTON) : Items.SLIME_BALL,
			(Items.BOW) : Items.STRING,
			(Items.CROSSBOW) : Items.STRING,
			(Items.FERN) : Items.WHEAT_SEEDS,
			(Items.SHORT_GRASS) : Items.WHEAT_SEEDS
		].each { input, output ->
			offerExtractorRecipe {
				ingredients input
				outputs output
				source input as ItemLike
				power 10
				time 300
				criterion getCriterionName(input), getCriterionConditions(input)
			}
		}
		[
			Items.BOOK,
			Items.ENCHANTED_BOOK,
			Items.WRITABLE_BOOK,
			Items.WRITTEN_BOOK,
			TRContent.MANUAL
		].each {item ->
			offerExtractorRecipe {
				ingredients item
				outputs stack(Items.PAPER, 2)
				source item
				power 10
				time 200
				criterion getCriterionName(item), getCriterionConditions(item)
			}
		}
		offerExtractorRecipe {
			ingredients Items.SCULK_CATALYST
			outputs Items.SCULK
			source Items.SCULK_CATALYST
			power 10
			time 200
			criterion getCriterionName(Items.SCULK_CATALYST), getCriterionConditions(Items.SCULK_CATALYST)
		}
		[
			(Items.GRAVEL) : Items.FLINT,
			(Items.MANGROVE_ROOTS) : Items.HANGING_ROOTS,
			(Items.MUDDY_MANGROVE_ROOTS) : Items.MANGROVE_ROOTS
		].each {input, output ->
			offerExtractorRecipe {
				ingredients input
				outputs output
				source input
				power 2
				time 200
				criterion getCriterionName(input), getCriterionConditions(input)
			}
		}
		offerExtractorRecipe {
			ingredients stack(Items.SUGAR_CANE, 2)
			outputs stack(Items.SUGAR, 3)
			source Items.SUGAR_CANE
			power 2
			time 200
			criterion getCriterionName(Items.SUGAR_CANE), getCriterionConditions(Items.SUGAR_CANE)
		}
		offerExtractorRecipe {
			ingredients TRContent.PAINTING_TOOL
			outputs Items.STRING
			source TRContent.PAINTING_TOOL
			power 10
			time 150
			criterion getCriterionName(TRContent.PAINTING_TOOL), getCriterionConditions(TRContent.PAINTING_TOOL)
		}
		offerExtractorRecipe {
			power 10
			time 300
			ingredient {
				tag(TRContent.ItemTags.RUBBER_LOGS)
			}
			outputs TRContent.Parts.RUBBER
			source("log")
			criterion getCriterionName(TRContent.ItemTags.RUBBER_LOGS), getCriterionConditions(TRContent.ItemTags.RUBBER_LOGS)
		}
		offerExtractorRecipe {
			power 10
			time 300
			ingredient {
				tag(ItemTags.BANNERS)
			}
			outputs stack(Items.STRING, 5)
			source("banner")
			criterion getCriterionName(ItemTags.BANNERS), getCriterionConditions(ItemTags.BANNERS)
		}
		offerExtractorRecipe {
			power 10
			time 300
			ingredient {
				tag(ItemTags.BEDS)
			}
			outputs stack(Items.STRING, 3)
			source("bed")
			criterion getCriterionName(ItemTags.BEDS), getCriterionConditions(ItemTags.BEDS)
		}
	}

	void generateFluidExtraction() {
		final int exPower = 10
		final int exTime = 150
		// vanilla buckets
		[
			Items.MILK_BUCKET,
			Items.LAVA_BUCKET,
			Items.POWDER_SNOW_BUCKET,
			Items.WATER_BUCKET
		].each {bucket ->
			offerExtractorRecipe {
				ingredients bucket
				outputs Items.BUCKET
				source bucket
				power exPower
				time exTime
				criterion getCriterionName(bucket), getCriterionConditions(bucket)
			}
		}
		// vanilla bottles with toast
		[
			Items.EXPERIENCE_BOTTLE,
			Items.HONEY_BOTTLE,
			Items.LINGERING_POTION,
			Items.POTION,
			Items.SPLASH_POTION
		].each {bottle ->
			offerExtractorRecipe {
				ingredients bottle
				outputs Items.GLASS_BOTTLE
				source bottle
				power exPower
				time exTime
				criterion getCriterionName(bottle), getCriterionConditions(bottle)
			}
		}
		// cells
		TRContent.Cells.values().findAll { it != TRContent.Cells.EMPTY }.each { cell ->
			offerExtractorRecipe {
				ingredients cell.asItem()
				outputs TRContent.CELL
				source cell.asItem()
				power exPower
				time exTime
				criterion getCriterionName(cell.asItem()), getCriterionConditions(cell.asItem())
			}
		}
	}

}

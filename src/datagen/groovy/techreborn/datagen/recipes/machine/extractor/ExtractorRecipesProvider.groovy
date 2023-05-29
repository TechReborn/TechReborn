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

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.registry.RegistryWrapper
import net.minecraft.resource.featuretoggle.FeatureFlags
import techreborn.datagen.recipes.TechRebornRecipesProvider
import techreborn.init.TRContent

import java.util.concurrent.CompletableFuture

class ExtractorRecipesProvider extends TechRebornRecipesProvider {
	ExtractorRecipesProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
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
			(Items.INK_SAC) : Items.BLACK_DYE,
			(Items.WITHER_ROSE) : Items.BLACK_DYE,
			(Items.CORNFLOWER) :  Items.BLUE_DYE,
			(Items.LAPIS_LAZULI) : Items.BLUE_DYE,
			(Items.COCOA_BEANS) : Items.BROWN_DYE,
			(Items.BLUE_ORCHID) : Items.LIGHT_BLUE_DYE,
			(Items.AZURE_BLUET) : Items.LIGHT_GRAY_DYE,
			(Items.OXEYE_DAISY) : Items.LIGHT_GRAY_DYE,
			(Items.WHITE_TULIP) : Items.LIGHT_GRAY_DYE,
			(Items.ALLIUM) : Items.MAGENTA_DYE,
			(Items.ORANGE_TULIP) : Items.ORANGE_DYE,
			(Items.TORCHFLOWER) : Items.ORANGE_DYE,
			(Items.PINK_TULIP) : Items.PINK_DYE,
			(Items.PINK_PETALS) : Items.PINK_DYE,
			(Items.POPPY) : Items.RED_DYE,
			(Items.RED_TULIP) : Items.RED_DYE,
			(Items.BONE_MEAL) : Items.WHITE_DYE,
			(Items.LILY_OF_THE_VALLEY) : Items.WHITE_DYE,
			(Items.DANDELION) : Items.YELLOW_DYE
		].each { item, dye ->
			offerExtractorRecipe {
				ingredients item
				outputs stack(dye, 2)
				source item.toString()
				power 10
				time 300
				criterion getCriterionName(item), getCriterionConditions(item)
			}
		}
	}

	// ONLY for doubling vanilla double dye recipes
	void generateQuadrupleDyes() {
		[
			(Items.PITCHER_PLANT) : Items.CYAN_DYE,
			(Items.LILAC) : Items.MAGENTA_DYE,
			(Items.PEONY) : Items.PINK_DYE,
			(Items.ROSE_BUSH) : Items.RED_DYE,
			(Items.SUNFLOWER) : Items.YELLOW_DYE
		].each { item, dye ->
			offerExtractorRecipe {
				ingredients item
				outputs stack(dye, 4)
				source item.toString()
				power 10
				time 300
				criterion getCriterionName(item), getCriterionConditions(item)
			}
		}
	}
	void generateDyesFromCoralBlock() {
		[
			(Items.TUBE_CORAL_BLOCK) : Items.BLUE_DYE,
			(Items.BRAIN_CORAL_BLOCK) : Items.PINK_DYE,
			(Items.BUBBLE_CORAL_BLOCK) : Items.PURPLE_DYE,
			(Items.FIRE_CORAL_BLOCK) : Items.RED_DYE,
			(Items.HORN_CORAL_BLOCK) : Items.YELLOW_DYE,
			(Items.DEAD_TUBE_CORAL_BLOCK) : Items.GRAY_DYE,
			(Items.DEAD_BRAIN_CORAL_BLOCK) : Items.GRAY_DYE,
			(Items.DEAD_BUBBLE_CORAL_BLOCK) : Items.GRAY_DYE,
			(Items.DEAD_FIRE_CORAL_BLOCK) : Items.GRAY_DYE,
			(Items.DEAD_HORN_CORAL_BLOCK) : Items.GRAY_DYE
		].each { item, dye ->
			offerExtractorRecipe {
				ingredients item
				outputs stack(dye, 5)
				source item.toString()
				power 10
				time 400
				criterion getCriterionName(item), getCriterionConditions(item)
			}
		}
	}

	void generateDyesFromSmallCoral() {
		[
			(Items.TUBE_CORAL) : Items.BLUE_DYE,
			(Items.TUBE_CORAL_FAN) : Items.BLUE_DYE,
			(Items.BRAIN_CORAL) : Items.PINK_DYE,
			(Items.BRAIN_CORAL_FAN) : Items.PINK_DYE,
			(Items.BUBBLE_CORAL) : Items.PURPLE_DYE,
			(Items.BUBBLE_CORAL_FAN) : Items.PURPLE_DYE,
			(Items.FIRE_CORAL) : Items.RED_DYE,
			(Items.FIRE_CORAL_FAN) : Items.RED_DYE,
			(Items.HORN_CORAL) : Items.YELLOW_DYE,
			(Items.HORN_CORAL_FAN) : Items.YELLOW_DYE,
			(Items.DEAD_TUBE_CORAL) : Items.GRAY_DYE,
			(Items.DEAD_TUBE_CORAL_FAN) : Items.GRAY_DYE,
			(Items.DEAD_BRAIN_CORAL) : Items.GRAY_DYE,
			(Items.DEAD_BRAIN_CORAL_FAN) : Items.GRAY_DYE,
			(Items.DEAD_BUBBLE_CORAL) : Items.GRAY_DYE,
			(Items.DEAD_BUBBLE_CORAL_FAN) : Items.GRAY_DYE,
			(Items.DEAD_FIRE_CORAL) : Items.GRAY_DYE,
			(Items.DEAD_FIRE_CORAL_FAN) : Items.GRAY_DYE,
			(Items.DEAD_HORN_CORAL) : Items.GRAY_DYE,
			(Items.DEAD_HORN_CORAL_FAN) : Items.GRAY_DYE
		].each { item, dye ->
			offerExtractorRecipe {
				ingredients item
				outputs dye
				source item.toString()
				power 10
				time 200
				criterion getCriterionName(item), getCriterionConditions(item)
			}
		}

	}

	void generateDyesFromFroglight() {
		[
			(Items.OCHRE_FROGLIGHT) : Items.YELLOW_DYE,
			(Items.VERDANT_FROGLIGHT) : Items.GREEN_DYE,
			(Items.PEARLESCENT_FROGLIGHT) : Items.PURPLE_DYE
		].each { item, dye ->
			offerExtractorRecipe {
				ingredients stack(item, 3)
				outputs dye
				source item.toString()
				power 10
				time 300
				criterion getCriterionName(item), getCriterionConditions(item)
			}
		}
	}

	void generateDyesFromMisc() {
		[
			(Items.PRISMARINE_SHARD) : Items.CYAN_DYE,
			(TRContent.Parts.PLANTBALL) : Items.GREEN_DYE
		].each { item, dye ->
			offerExtractorRecipe {
				ingredients item
				outputs dye
				source item.asItem().toString()
				power 10
				time 300
				criterion getCriterionName(item), getCriterionConditions(item)
			}
		}
		[
			(Items.SWEET_BERRIES) : Items.RED_DYE,
			(Items.GLOW_BERRIES) : Items.ORANGE_DYE
		].each { item, dye ->
			offerExtractorRecipe {
				ingredients stack(item, 4)
				outputs dye
				source item.toString()
				power 10
				time 300
				criterion getCriterionName(item), getCriterionConditions(item)
			}
		}
		offerExtractorRecipe {
			ingredients stack(Items.CARROT, 3)
			outputs Items.ORANGE_DYE
			source Items.CARROT.toString()
			power 10
			time 300
			criterion getCriterionName(Items.CARROT), getCriterionConditions(Items.CARROT)
		}
		offerExtractorRecipe {
			ingredients Items.BEETROOT
			outputs stack(Items.RED_DYE, 2)
			source Items.BEETROOT.toString()
			power 10
			time 300
			criterion getCriterionName(Items.BEETROOT), getCriterionConditions(Items.BEETROOT)
		}
		offerExtractorRecipe {
			ingredients Items.SHULKER_SHELL
			outputs stack(Items.PURPLE_DYE, 4)
			source Items.SHULKER_SHELL.toString()
			power 10
			time 300
			criterion getCriterionName(Items.SHULKER_SHELL), getCriterionConditions(Items.SHULKER_SHELL)
		}
	}

	void generateMisc() {
		offerExtractorRecipe {
			ingredients Items.CONDUIT
			outputs Items.HEART_OF_THE_SEA
			source Items.CONDUIT.toString()
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
				source input.asItem().toString()
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
				source input.asItem().toString()
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
				source input.asItem().toString()
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
				source input.asItem().toString()
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
			(Items.GRASS) : Items.WHEAT_SEEDS
		].each { input, output ->
			offerExtractorRecipe {
				ingredients input
				outputs output
				source input.asItem().toString()
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
				source item.toString()
				power 10
				time 200
				criterion getCriterionName(item), getCriterionConditions(item)
			}
		}
		offerExtractorRecipe {
			ingredients Items.SCULK_CATALYST
			outputs Items.SCULK
			source Items.SCULK_CATALYST.toString()
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
				source input.toString()
				power 2
				time 200
				criterion getCriterionName(input), getCriterionConditions(input)
			}
		}
		offerExtractorRecipe {
			ingredients stack(Items.SUGAR_CANE, 2)
			outputs stack(Items.SUGAR, 3)
			source Items.SUGAR_CANE.toString()
			power 2
			time 200
			criterion getCriterionName(Items.SUGAR_CANE), getCriterionConditions(Items.SUGAR_CANE)
		}
		offerExtractorRecipe {
			ingredients TRContent.PAINTING_TOOL
			outputs Items.STRING
			source TRContent.PAINTING_TOOL.toString()
			power 10
			time 150
			criterion getCriterionName(TRContent.PAINTING_TOOL), getCriterionConditions(TRContent.PAINTING_TOOL)
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
				source bucket.toString()
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
				source bottle.toString()
				power exPower
				time exTime
				criterion getCriterionName(bottle), getCriterionConditions(bottle)
			}
		}
		// cells
		offerExtractorRecipe {
			ingredients TRContent.CELL
			outputs TRContent.CELL
			power exPower
			time exTime
			criterion getCriterionName(TRContent.CELL), getCriterionConditions(TRContent.CELL)
		}
	}

}

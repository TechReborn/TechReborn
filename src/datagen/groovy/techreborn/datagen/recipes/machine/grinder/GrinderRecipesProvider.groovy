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

package techreborn.datagen.recipes.machine.grinder

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.item.Items
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.ItemTags
import techreborn.datagen.TRConventionalTags
import techreborn.datagen.compat.Ae2
import techreborn.datagen.recipes.TechRebornRecipesProvider
import techreborn.init.TRContent

import java.util.concurrent.CompletableFuture

class GrinderRecipesProvider extends TechRebornRecipesProvider {
	GrinderRecipesProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture)
	}

	@Override
	void generateRecipes() {
		generateVanillaRawMetals()
		generateTRRawMetals()
		generateVanillaGems()
		generateTRGems()
		generateTRIngots()
		generateTrimTemplates()
		generateSand()
		generateRedSand()
		generateConcretePowder()
		generateSawdust()
		generateMisc()
	}

	void generateVanillaRawMetals() {
		[
			(Items.RAW_IRON)  : (TRConventionalTags.IRON_ORES),
			(Items.RAW_COPPER): (TRConventionalTags.COPPER_ORES),
			(Items.RAW_GOLD)  : (TRConventionalTags.GOLD_ORES)
		].each { raw, oreTag ->
			offerGrinderRecipe {
				ingredients oreTag
				outputs stack(raw, 2)
				power 2
				time 270
				criterion getCriterionName(oreTag), getCriterionConditions(oreTag)
			}
		}
	}

	void generateTRRawMetals() {
		TRContent.RawMetals.getRM2OBMap().each { raw, ore ->
			if (!ore.isIndustrial())
				offerGrinderRecipe {
					ingredients ore.asTag()
					outputs stack(raw, 2)
					power 2
					time 270
					criterion getCriterionName(ore.asTag()), getCriterionConditions(ore.asTag())
				}
		}
	}

	void generateTRGems() {
		TRContent.Gems.getG2DMap().each {gem, dust ->
			offerGrinderRecipe {
				ingredients gem.asTag()
				outputs dust
				power 2
				time 200
				criterion getCriterionName(gem.asTag()), getCriterionConditions(gem.asTag())
			}
			if (gem.getOre() != null)
				offerGrinderRecipe {
					ingredients gem.getOre().asTag()
					outputs stack(dust,2)
					power 2
					time 220
					source "ore"
					criterion getCriterionName(gem.getOre().asTag()), getCriterionConditions(gem.getOre().asTag())
				}
			if (gem.getStorageBlock() != null)
				offerGrinderRecipe {
					ingredients gem.getStorageBlock().asTag()
					outputs stack(dust,9)
					power 2
					time 1500
					source "block"
					criterion getCriterionName(gem.getStorageBlock().asTag()), getCriterionConditions(gem.getStorageBlock().asTag())
				}
		}
	}

	void generateVanillaGems() {
		offerGrinderRecipe {
			power 2
			time 200
			ingredients tag("c:coal_ores")
			outputs stack(Items.COAL, 2)
		}
		offerGrinderRecipe {
			power 2
			time 230
			ingredients stack(Items.COAL)
			outputs stack(TRContent.Dusts.COAL)
		}
		offerGrinderRecipe {
			power 2
			time 400
			ingredients stack( Items.COAL_BLOCK)
			outputs stack(TRContent.Dusts.COAL, 9)
		}
		offerGrinderRecipe {
			power 4
			time 230
			ingredients stack(Items.CHARCOAL)
			outputs stack(TRContent.Dusts.CHARCOAL)
		}
		offerGrinderRecipe {
			power 4
			time 270
			ingredients tag("c:redstone_ores")
			outputs stack(Items.REDSTONE, 8)
		}
		offerGrinderRecipe {
			power 4
			time 270
			ingredients stack(Items.NETHER_QUARTZ_ORE)
			outputs stack(Items.QUARTZ, 2)
		}
		offerGrinderRecipe {
			power 4
			time 270
			ingredients stack(Items.QUARTZ)
			outputs stack(TRContent.Dusts.QUARTZ)
		}
		offerGrinderRecipe {
			power 4
			time 1080
			ingredients stack(Items.QUARTZ_BLOCK)
			outputs stack(TRContent.Dusts.QUARTZ, 4)
		}
		offerGrinderRecipe {
			power 2
			time 200
			ingredients tag("c:lapis_ores")
			outputs stack(Items.LAPIS_LAZULI, 10)
		}
		offerGrinderRecipe {
			power 4
			time 270
			ingredients tag("c:emerald_ores")
			outputs stack(Items.EMERALD)
		}
		offerGrinderRecipe {
			power 4
			time 270
			ingredients stack(Items.EMERALD)
			outputs stack(TRContent.Dusts.EMERALD)
		}
		offerGrinderRecipe {
			power 4
			time 270
			ingredients tag("c:diamond_ores")
			outputs stack(Items.DIAMOND)
		}
		offerGrinderRecipe {
			power 4
			time 270
			ingredients stack(Items.DIAMOND)
			outputs stack(TRContent.Dusts.DIAMOND)
		}
	}

	void generateTRIngots() {
		TRContent.Ingots.getI2DMap().each {ingot, dust ->
			offerGrinderRecipe {
				ingredients ingot.asTag()
				outputs dust
				power 5
				time 200
				criterion getCriterionName(ingot.asTag()), getCriterionConditions(ingot.asTag())
			}
			if (ingot.getStorageBlock() != null)
				offerGrinderRecipe {
					ingredients ingot.getStorageBlock().asTag()
					outputs stack(dust,9)
					power 5
					time 1500
					source "block"
					criterion getCriterionName(ingot.getStorageBlock().asTag()), getCriterionConditions(ingot.getStorageBlock().asTag())
				}
		}
	}

	void generateTrimTemplates() {
		offerGrinderRecipe {
			power 5
			time 200
			ingredients tag("minecraft:trim_templates")
			outputs stack(TRContent.SmallDusts.DIAMOND, 5)
		}
		offerGrinderRecipe {
			power 5
			time 200
			ingredients TRContent.Parts.TEMPLATE_TEMPLATE
			outputs stack(TRContent.SmallDusts.DIAMOND, 5)
		}
	}

	void generateSand() {
		[
			(Items.SANDSTONE): 4,
			(Items.SMOOTH_SANDSTONE): 4,
			(Items.CUT_SANDSTONE) : 4,
			(Items.CHISELED_SANDSTONE) :4,
			(Items.SANDSTONE_STAIRS) : 3,
			(Items.SMOOTH_SANDSTONE_STAIRS) : 3,
			(Items.SANDSTONE_WALL) : 3,
			(Items.SANDSTONE_SLAB) : 2,
			(Items.CUT_SANDSTONE_SLAB) : 2,
			(Items.SMOOTH_SANDSTONE_SLAB) : 2,
		].each {item, count ->
			offerGrinderRecipe {
				ingredients item
				outputs stack(Items.SAND, count)
				power count
				time 200
				source item.toString()
				criterion getCriterionName(item), getCriterionConditions(item)
			}
		}
	}

	void generateRedSand() {
		[
			(Items.RED_SANDSTONE): 4,
			(Items.SMOOTH_RED_SANDSTONE): 4,
			(Items.CUT_RED_SANDSTONE) : 4,
			(Items.CHISELED_RED_SANDSTONE) :4,
			(Items.RED_SANDSTONE_STAIRS) : 3,
			(Items.SMOOTH_RED_SANDSTONE_STAIRS) : 3,
			(Items.RED_SANDSTONE_WALL) : 3,
			(Items.RED_SANDSTONE_SLAB) : 2,
			(Items.CUT_RED_SANDSTONE_SLAB) : 2,
			(Items.SMOOTH_RED_SANDSTONE_SLAB) : 2,
		].each {item, count ->
			offerGrinderRecipe {
				ingredients item
				outputs stack(Items.RED_SAND, count)
				power count
				time 200
				source item.toString()
				criterion getCriterionName(item), getCriterionConditions(item)
			}
		}
	}

	void generateConcretePowder() {
		[
			(Items.BLACK_CONCRETE): Items.BLACK_CONCRETE_POWDER,
			(Items.BLUE_CONCRETE): Items.BLUE_CONCRETE_POWDER,
			(Items.BROWN_CONCRETE): Items.BROWN_CONCRETE_POWDER,
			(Items.CYAN_CONCRETE): Items.CYAN_CONCRETE_POWDER,
			(Items.GRAY_CONCRETE): Items.GRAY_CONCRETE_POWDER,
			(Items.GREEN_CONCRETE): Items.GREEN_CONCRETE_POWDER,
			(Items.LIGHT_BLUE_CONCRETE): Items.LIGHT_BLUE_CONCRETE_POWDER,
			(Items.LIGHT_GRAY_CONCRETE): Items.LIGHT_GRAY_CONCRETE_POWDER,
			(Items.LIME_CONCRETE): Items.LIME_CONCRETE_POWDER,
			(Items.MAGENTA_CONCRETE): Items.MAGENTA_CONCRETE_POWDER,
			(Items.ORANGE_CONCRETE): Items.ORANGE_CONCRETE_POWDER,
			(Items.PINK_CONCRETE): Items.PINK_CONCRETE_POWDER,
			(Items.PURPLE_CONCRETE): Items.PURPLE_CONCRETE_POWDER,
			(Items.RED_CONCRETE): Items.RED_CONCRETE_POWDER,
			(Items.WHITE_CONCRETE): Items.WHITE_CONCRETE_POWDER,
			(Items.YELLOW_CONCRETE): Items.YELLOW_CONCRETE_POWDER
		].each {concrete, concretePowder ->
			offerGrinderRecipe {
				ingredients concrete
				outputs concretePowder
				source "concrete"
				power 4
				time 200
				criterion getCriterionName(concrete), getCriterionConditions(concrete)
			}
		}
	}

	void generateSawdust() {
		// designed to be a fourth of what ind. sawmill gives, i.e. the same, just in small dust
		[
			(ItemTags.PLANKS)                : 8,
			(ItemTags.WOODEN_STAIRS)         : 6,
			(ItemTags.WOODEN_SLABS)          : 4,
			(ItemTags.WOODEN_PRESSURE_PLATES): 1,
			(ItemTags.WOODEN_TRAPDOORS)      : 1,
			(ItemTags.WOODEN_FENCES)         : 1,
			(ItemTags.WOODEN_DOORS)          : 3,
			(ItemTags.SIGNS)                 : 1
		].each { item, count ->
			offerGrinderRecipe {
				ingredients item
				outputs stack(TRContent.SmallDusts.SAW, count)
				power 3
				time 180
				source item.id().path
				criterion getCriterionName(item), getCriterionConditions(item)
			}
		}
		// wooden buttons and most wooden tools are not eligible because they give too less
		[
			(Items.WOODEN_PICKAXE) : 1,
			(Items.WOODEN_AXE) : 1
		].each { item, count ->
			offerGrinderRecipe {
				ingredients item
				outputs stack(TRContent.SmallDusts.SAW, count)
				power 3
				time 180
				source item.toString()
				criterion getCriterionName(item), getCriterionConditions(item)
			}
		}
	}

	void generateMisc() {
		offerGrinderRecipe {
			power 2
			time 220
			ingredients stack("minecraft:glowstone")
			outputs stack("minecraft:glowstone_dust", 4)
		}
		offerGrinderRecipe {
			power 4
			time 200
			ingredients stack("minecraft:netherrack")
			outputs stack("techreborn:netherrack_dust")
		}
		offerGrinderRecipe {
			power 2
			time 180
			ingredients stack("minecraft:gravel")
			outputs stack("minecraft:sand")
		}
		offerGrinderRecipe {
			power 2
			time 220
			ingredients tag("c:sphalerite_ores")
			outputs stack("techreborn:sphalerite_dust", 2)
		}
		offerGrinderRecipe {
			power 2
			time 100
			ingredients tag("techreborn:calcite_small_dust_material")
			outputs stack("techreborn:calcite_small_dust")
		}
		offerGrinderRecipe {
			power 2
			time 300
			ingredients stack("minecraft:shroomlight")
			outputs stack("techreborn:glowstone_small_dust", 2)
		}
		offerGrinderRecipe {
			power 2
			time 170
			ingredients stack("minecraft:bone")
			outputs stack("minecraft:bone_meal", 6)
		}
		offerGrinderRecipe {
			power 2
			time 180
			ingredients tag("techreborn:gravel_material")
			outputs stack("minecraft:gravel")
		}
		offerGrinderRecipe {
			power 3
			time 300
			ingredients tag("minecraft:wool")
			outputs stack("minecraft:string", 4)
		}
		offerGrinderRecipe {
			power 2
			time 400
			ingredients tag("c:froglights")
			outputs stack("minecraft:prismarine_crystals", 2)
		}
		offerGrinderRecipe {
			power 2
			time 400
			ingredients stack("minecraft:sea_lantern")
			outputs stack("minecraft:prismarine_crystals", 4)
		}
		offerGrinderRecipe {
			power 2
			time 180
			ingredients stack("minecraft:clay_ball")
			outputs stack("techreborn:clay_dust")
		}
		offerGrinderRecipe {
			power 4
			time 270
			ingredients tag("c:pyrite_ores")
			outputs stack("techreborn:pyrite_dust", 2)
		}
		offerGrinderRecipe {
			power 4
			time 200
			ingredients stack("minecraft:blaze_rod")
			outputs stack("minecraft:blaze_powder", 4)
		}
		offerGrinderRecipe {
			power 2
			time 150
			ingredients stack("minecraft:glow_berries", 4)
			outputs stack("techreborn:glowstone_small_dust")
		}
		offerGrinderRecipe {
			power 4
			time 270
			ingredients stack("minecraft:andesite")
			outputs stack("techreborn:andesite_dust", 2)
		}
		offerGrinderRecipe {
			power 2
			time 270
			ingredients tag("c:cinnabar_ores")
			outputs stack("techreborn:cinnabar_dust", 2)
		}
		offerGrinderRecipe {
			power 4
			time 220
			ingredients tag("c:sodalite_ores")
			outputs stack("techreborn:sodalite_dust", 2)
		}
		offerGrinderRecipe {
			power 4
			time 200
			ingredients stack("minecraft:ender_pearl")
			outputs stack("techreborn:ender_pearl_dust", 2)
		}
		offerGrinderRecipe {
			power 4
			time 200
			ingredients stack("minecraft:ender_eye")
			outputs stack("techreborn:ender_eye_dust", 2)
		}
		offerGrinderRecipe {
			power 4
			time 200
			ingredients tag("c:sponges")
			outputs stack("techreborn:sponge_piece", 5)
		}
		offerGrinderRecipe {
			power 4
			time 200
			ingredients stack("minecraft:crimson_nylium")
			outputs stack("techreborn:netherrack_dust")
		}
		offerGrinderRecipe {
			power 2
			time 400
			ingredients tag("techreborn:calcite_dust_material")
			outputs stack("techreborn:calcite_dust")
		}
		offerGrinderRecipe {
			power 4
			time 270
			ingredients stack("minecraft:amethyst_block")
			outputs stack("techreborn:amethyst_dust", 2)
		}
		offerGrinderRecipe {
			power 2
			time 500
			ingredients stack("minecraft:conduit")
			outputs stack("techreborn:calcite_dust", 2)
		}
		offerGrinderRecipe {
			power 4
			time 270
			ingredients tag("c:bauxite_ores")
			outputs stack("techreborn:bauxite_dust", 2)
		}
		offerGrinderRecipe {
			power 4
			time 200
			ingredients stack("minecraft:end_stone")
			outputs stack("techreborn:endstone_dust")
		}
		offerGrinderRecipe {
			power 4
			time 200
			ingredients stack("minecraft:ancient_debris")
			outputs stack("minecraft:netherite_scrap", 2)
		}
		offerGrinderRecipe {
			power 2
			time 1440
			ingredients stack("minecraft:diorite")
			outputs stack("techreborn:diorite_dust")
		}
		offerGrinderRecipe {
			power 2
			time 270
			ingredients tag("c:galena_ores")
			outputs stack("techreborn:galena_dust", 2)
		}
		offerGrinderRecipe {
			power 2
			time 220
			ingredients tag("c:sulfur_ores")
			outputs stack("techreborn:sulfur_dust", 2)
		}
		offerGrinderRecipe {
			power 2
			time 200
			ingredients tag("c:sulfurs")
			outputs stack("techreborn:sulfur_dust")
		}
		offerGrinderRecipe {
			power 2
			time 400
			ingredients stack("minecraft:prismarine_bricks")
			outputs stack("minecraft:prismarine_shard", 7)
		}
		offerGrinderRecipe {
			power 2
			time 180
			ingredients tag("c:limestone")
			outputs stack("techreborn:marble_dust")
		}
		offerGrinderRecipe {
			power 4
			time 270
			ingredients stack("minecraft:granite")
			outputs stack("techreborn:granite_dust", 2)
		}
		offerGrinderRecipe {
			power 2
			time 180
			ingredients tag("c:marble")
			outputs stack("techreborn:marble_dust")
		}
		offerGrinderRecipe {
			power 6
			time 400
			ingredients stack("minecraft:obsidian")
			outputs stack("techreborn:obsidian_dust", 4)
		}
		offerGrinderRecipe {
			power 4
			time 200
			ingredients stack("minecraft:warped_nylium")
			outputs stack("techreborn:netherrack_dust")
		}
		offerGrinderRecipe {
			power 2
			time 400
			ingredients stack("minecraft:prismarine")
			outputs stack("minecraft:prismarine_shard", 3)
		}
		offerGrinderRecipe {
			power 2
			time 270
			ingredients stack("minecraft:flint")
			outputs stack("techreborn:flint_dust")
		}
		offerGrinderRecipe {
			power 2
			time 180
			ingredients tag("c:basalt")
			outputs stack("techreborn:basalt_dust")
		}
		offerGrinderRecipe {
			power 2
			time 100
			ingredients Items.HEART_POTTERY_SHERD
			outputs Items.HEARTBREAK_POTTERY_SHERD
		}
		offerGrinderRecipe {
			power 2
			time 300
			ingredient{
				tag(TRConventionalTags.CERTUS_QUARTZ, 2)
			}
			outputs Ae2.certusQuartzDust
			condition DefaultResourceConditions.allModsLoaded("ae2")
		}
		offerGrinderRecipe {
			power 2
			time 300
			ingredients TRConventionalTags.CERTUS_QUARTZ_ORES
			outputs stack(Ae2.certusQuartzDust, 5)
			condition DefaultResourceConditions.allModsLoaded("ae2")
		}
		offerGrinderRecipe {
			power 2
			time 300
			ingredients stack(Ae2.fluixCrystal, 2)
			outputs Ae2.fluixDust
			condition DefaultResourceConditions.allModsLoaded("ae2")
		}
	}
}

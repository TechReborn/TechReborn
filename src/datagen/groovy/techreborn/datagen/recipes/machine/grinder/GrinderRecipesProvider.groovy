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
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.ItemTags
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
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
		// vanilla gems
		// TODO vanilla gems + storage blocks (Redstone, glowstone, lapis, emerald, diamond)
		generateTRGems()
		// vanilla ingots
		// TODO vanilla ingots + storage blocks (iron, copper, gold)
		generateTRIngots()
		generateSand()
		generateRedSand()
		generateConcretePowder()
		generateSawdust()
	}

	void generateVanillaRawMetals() {
		[
			(Items.RAW_IRON)  : (TagKey.of(RegistryKeys.ITEM, new Identifier("c", "iron_ores"))),
			(Items.RAW_COPPER): (TagKey.of(RegistryKeys.ITEM, new Identifier("c", "copper_ores"))),
			(Items.RAW_GOLD)  : (TagKey.of(RegistryKeys.ITEM, new Identifier("c", "gold_ores")))
		].each { raw, oreTag ->
			offerGrinderRecipe {
				ingredients oreTag
				outputs new ItemStack(raw, 2)
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
					outputs new ItemStack(raw, 2)
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
					outputs new ItemStack(dust,2)
					power 2
					time 220
					source "ore"
					criterion getCriterionName(gem.getOre().asTag()), getCriterionConditions(gem.getOre().asTag())
				}
			if (gem.getStorageBlock() != null)
				offerGrinderRecipe {
					ingredients gem.getStorageBlock().asTag()
					outputs new ItemStack(dust,9)
					power 2
					time 1500
					source "block"
					criterion getCriterionName(gem.getStorageBlock().asTag()), getCriterionConditions(gem.getStorageBlock().asTag())
				}
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
					outputs new ItemStack(dust,9)
					power 5
					time 1500
					source "block"
					criterion getCriterionName(ingot.getStorageBlock().asTag()), getCriterionConditions(ingot.getStorageBlock().asTag())
				}
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
				outputs new ItemStack(Items.SAND, count)
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
				outputs new ItemStack(Items.RED_SAND, count)
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
				outputs new ItemStack(TRContent.SmallDusts.SAW, count)
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
				outputs new ItemStack(TRContent.SmallDusts.SAW, count)
				power 3
				time 180
				source item.toString()
				criterion getCriterionName(item), getCriterionConditions(item)
			}
		}
	}
}

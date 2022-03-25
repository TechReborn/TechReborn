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

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.tag.TagKey
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import techreborn.datagen.recipes.TechRebornRecipesProvider
import techreborn.init.TRContent

class GrinderRecipesProvider extends TechRebornRecipesProvider {
	GrinderRecipesProvider(FabricDataGenerator dataGenerator) {
		super(dataGenerator)
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
	}

	void generateVanillaRawMetals() {
		[
			(Items.RAW_IRON)  : (TagKey.of(Registry.ITEM_KEY, new Identifier("c", "iron_ores"))),
			(Items.RAW_COPPER): (TagKey.of(Registry.ITEM_KEY, new Identifier("c", "copper_ores"))),
			(Items.RAW_GOLD)  : (TagKey.of(Registry.ITEM_KEY, new Identifier("c", "gold_ores")))
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
}
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

package techreborn.datagen.recipes.machine.assembling_machine

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.ItemTags
import techreborn.datagen.recipes.TechRebornRecipesProvider
import techreborn.init.TRContent

import java.util.concurrent.CompletableFuture

class AssemblingMachineRecipesProvider extends TechRebornRecipesProvider {

	AssemblingMachineRecipesProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture)
	}

	@Override
	void generateRecipes() {
		generateBed()
		generateSmithingTemplates()
		generateMiscTR()
		generateMiscVanilla()
	}

	void generateBed() {
		[
			(Items.BLACK_WOOL): Items.BLACK_BED,
			(Items.BLUE_WOOL): Items.BLUE_BED,
			(Items.BROWN_WOOL): Items.BROWN_BED,
			(Items.CYAN_WOOL): Items.CYAN_BED,
			(Items.GRAY_WOOL): Items.GRAY_BED,
			(Items.GREEN_WOOL): Items.GREEN_BED,
			(Items.LIGHT_BLUE_WOOL): Items.LIGHT_BLUE_BED,
			(Items.LIGHT_GRAY_WOOL): Items.LIGHT_GRAY_BED,
			(Items.LIME_WOOL): Items.LIME_BED,
			(Items.MAGENTA_WOOL): Items.MAGENTA_BED,
			(Items.ORANGE_WOOL): Items.ORANGE_BED,
			(Items.PINK_WOOL): Items.PINK_BED,
			(Items.PURPLE_WOOL): Items.PURPLE_BED,
			(Items.RED_WOOL): Items.RED_BED,
			(Items.WHITE_DYE): Items.WHITE_BED,
			(Items.YELLOW_DYE): Items.YELLOW_BED
		].each {wool, bed ->
			offerAssemblingMachineRecipe {
				ingredients stack(wool, 2), ItemTags.PLANKS
				outputs bed
				source "wool"
				power 25
				time 250
				criterion getCriterionName(wool), getCriterionConditions(wool)
			}
		}
	}

	void generateSmithingTemplates() {
		[
			(Items.COBBLESTONE) : Items.COAST_ARMOR_TRIM_SMITHING_TEMPLATE,
			(Items.SANDSTONE) : Items.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE,
			(Items.END_STONE_BRICKS) : Items.EYE_ARMOR_TRIM_SMITHING_TEMPLATE,
			(Items.NETHERRACK) : Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE,
			(Items.NETHER_BRICKS) : Items.RIB_ARMOR_TRIM_SMITHING_TEMPLATE,
			(Items.SMOOTH_STONE) : Items.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE,
			(Items.SCULK_SHRIEKER) : Items.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE,
			(Items.GILDED_BLACKSTONE) : Items.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE,
			(Items.PURPUR_BLOCK) : Items.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE,
			(Items.PRISMARINE_BRICKS) : Items.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE,
			(Items.STONE) : Items.VEX_ARMOR_TRIM_SMITHING_TEMPLATE,
			(Items.DEEPSLATE) : Items.WARD_ARMOR_TRIM_SMITHING_TEMPLATE,
			(Items.MOSSY_COBBLESTONE) : Items.WILD_ARMOR_TRIM_SMITHING_TEMPLATE
		].each {material, template ->
			offerAssemblingMachineRecipe {
				ingredients stack(material, 2), TRContent.Parts.TEMPLATE_TEMPLATE
				outputs template
				power 40
				time 1500
				criterion getCriterionName(material), getCriterionConditions(material)
			}
		}
		[
			(Items.TERRACOTTA) : Items.HOST_ARMOR_TRIM_SMITHING_TEMPLATE,
			(Items.BROWN_TERRACOTTA) : Items.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE,
			(Items.GRAY_TERRACOTTA) : Items.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE,
			(Items.LIGHT_GRAY_TERRACOTTA) : Items.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE
		].each {material, template ->
			offerAssemblingMachineRecipe {
				ingredients stack(material, 2), TRContent.Parts.TEMPLATE_TEMPLATE
				outputs template
				power 40
				time 1500
				criterion getCriterionName(material), getCriterionConditions(material)
			}
		}
	}

	void generateMiscTR() {
		offerAssemblingMachineRecipe {
			ingredients TRContent.Plates.PLATINUM.asTag(), TRContent.Parts.ADVANCED_CIRCUIT
			outputs TRContent.Parts.INDUSTRIAL_CIRCUIT
			power 20
			time 200
			criterion getCriterionName(TRContent.Plates.PLATINUM.asTag()), getCriterionConditions(TRContent.Plates.PLATINUM.asTag())
		}
		offerAssemblingMachineRecipe {
			ingredients TRContent.Parts.DATA_STORAGE_CORE, TRContent.Parts.INDUSTRIAL_CIRCUIT
			outputs TRContent.Parts.DATA_STORAGE_CHIP
			power 20
			time 200
			criterion getCriterionName(TRContent.Parts.DATA_STORAGE_CORE), getCriterionConditions(TRContent.Parts.DATA_STORAGE_CORE)
		}
		offerAssemblingMachineRecipe {
			ingredients TRContent.LAPOTRON_CRYSTAL, TRContent.Parts.INDUSTRIAL_CIRCUIT
			outputs TRContent.Parts.ENERGY_FLOW_CHIP
			power 20
			time 200
			criterion getCriterionName(TRContent.LAPOTRON_CRYSTAL), getCriterionConditions(TRContent.LAPOTRON_CRYSTAL)
		}
		offerAssemblingMachineRecipe {
			ingredients stack(TRContent.Parts.SYNTHETIC_REDSTONE_CRYSTAL, 2), TRContent.Plates.SILICON.asTag()
			outputs TRContent.ENERGY_CRYSTAL
			power 40
			time 200
			criterion getCriterionName(TRContent.Parts.SYNTHETIC_REDSTONE_CRYSTAL), getCriterionConditions(TRContent.Parts.SYNTHETIC_REDSTONE_CRYSTAL)
		}
		offerAssemblingMachineRecipe {
			ingredients TRContent.Machine.SOLID_FUEL_GENERATOR, TRContent.Plates.MAGNALIUM
			outputs TRContent.Machine.WIND_MILL
			power 20
			time 700
			criterion getCriterionName(TRContent.Plates.MAGNALIUM), getCriterionConditions(TRContent.Plates.MAGNALIUM)
		}
	}

	void generateMiscVanilla() {
		offerAssemblingMachineRecipe {
			ingredients stack(Items.PHANTOM_MEMBRANE, 16), stack(Items.END_ROD, 3)
			outputs Items.ELYTRA
			power 20
			time 500
			criterion getCriterionName(Items.END_ROD), getCriterionConditions(Items.END_ROD)
		}
		offerAssemblingMachineRecipe {
			ingredients stack(Items.TORCH, 10), stack(Items.IRON_INGOT, 8)
			outputs stack(Items.LANTERN, 10)
			power 20
			time 200
			criterion getCriterionName(Items.TORCH), getCriterionConditions(Items.TORCH)
		}
		offerAssemblingMachineRecipe {
			ingredients stack(Items.SOUL_TORCH, 10), stack(Items.IRON_INGOT, 8)
			outputs stack(Items.SOUL_LANTERN, 10)
			power 20
			time 200
			criterion getCriterionName(Items.SOUL_TORCH), getCriterionConditions(Items.SOUL_TORCH)
		}
	}

}

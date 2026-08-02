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

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput
import net.minecraft.world.item.Items
import net.minecraft.core.HolderLookup
import net.minecraft.tags.ItemTags
import techreborn.datagen.TRConventionalTags
import techreborn.datagen.recipes.TechRebornRecipesProvider
import techreborn.init.ModFluids
import techreborn.init.TRContent

import java.util.concurrent.CompletableFuture

class AssemblingMachineRecipesProvider extends TechRebornRecipesProvider {

	AssemblingMachineRecipesProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
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
			(Items.WOOL.black()): Items.BED.black(),
			(Items.WOOL.blue()): Items.BED.blue(),
			(Items.WOOL.brown()): Items.BED.brown(),
			(Items.WOOL.cyan()): Items.BED.cyan(),
			(Items.WOOL.gray()): Items.BED.gray(),
			(Items.WOOL.green()): Items.BED.green(),
			(Items.WOOL.lightBlue()): Items.BED.lightBlue(),
			(Items.WOOL.lightGray()): Items.BED.lightGray(),
			(Items.WOOL.lime()): Items.BED.lime(),
			(Items.WOOL.magenta()): Items.BED.magenta(),
			(Items.WOOL.orange()): Items.BED.orange(),
			(Items.WOOL.pink()): Items.BED.pink(),
			(Items.WOOL.purple()): Items.BED.purple(),
			(Items.WOOL.red()): Items.BED.red(),
			(Items.WOOL.white()): Items.BED.white(),
			(Items.WOOL.yellow()): Items.BED.yellow()
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
			(Items.COPPER_BLOCK.weathering().unaffected()) : Items.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE,
			(Items.COBBLESTONE) : Items.COAST_ARMOR_TRIM_SMITHING_TEMPLATE,
			(Items.SANDSTONE) : Items.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE,
			(Items.END_STONE_BRICKS) : Items.EYE_ARMOR_TRIM_SMITHING_TEMPLATE,
			(Items.BREEZE_ROD) : Items.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE,
			(Items.NETHERRACK) : Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE,
			(Items.NETHER_BRICKS) : Items.RIB_ARMOR_TRIM_SMITHING_TEMPLATE,
			(Items.SMOOTH_STONE) : Items.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE,
			(Items.SCULK_SHRIEKER) : Items.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE,
			(Items.CHISELED_POLISHED_BLACKSTONE) : Items.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE,
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
			(Items.DYED_TERRACOTTA.brown()) : Items.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE,
			(Items.DYED_TERRACOTTA.gray()) : Items.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE,
			(Items.DYED_TERRACOTTA.lightGray()) : Items.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE
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
		offerAssemblingMachineRecipe {
			ingredients TRConventionalTags.SILICON_PLATES
			ingredient {
				tag(TRConventionalTags.ELECTRUM_PLATES, 2)
			}
			outputs TRContent.Parts.ADVANCED_CIRCUIT
			power 20
			time 200
		}
		offerAssemblingMachineRecipe {
			ingredients TRContent.Parts.ELECTRONIC_CIRCUIT
			ingredient {
				tag(TRConventionalTags.PERIDOT_PLATES, 2)
			}
			outputs TRContent.Parts.DATA_STORAGE_CORE
			power 20
			time 200
		}
		offerAssemblingMachineRecipe {
			ingredients TRContent.Parts.ELECTRONIC_CIRCUIT, TRConventionalTags.EMERALD_PLATES
			outputs TRContent.Parts.DATA_STORAGE_CORE
			power 20
			time 200
		}
		offerAssemblingMachineRecipe {
			ingredients TRConventionalTags.SILICON_PLATES
			ingredient {
				tag(TRConventionalTags.COPPER_PLATES, 2)
			}
			outputs TRContent.Parts.ELECTRONIC_CIRCUIT
			power 20
			time 200
		}
		offerAssemblingMachineRecipe {
			ingredient {
				tag(TRConventionalTags.ALUMINUM_PLATES, 2)
			}
			ingredient {
				stack cellStack(ModFluids.LITHIUM, 2)
			}
			outputs TRContent.LITHIUM_ION_BATTERY
			power 20
			time 200
		}
		offerAssemblingMachineRecipe {
			ingredients Items.CRAFTING_TABLE, TRContent.Parts.ADVANCED_CIRCUIT
			outputs Items.CRAFTER
			power 20
			time 200
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

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

package techreborn.datagen.recipes.machine.blast_furnace

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.item.Items
import net.minecraft.registry.RegistryWrapper
import techreborn.datagen.TRConventionalTags
import techreborn.datagen.recipes.TechRebornRecipesProvider
import techreborn.init.ModFluids
import techreborn.init.TRContent

import java.util.concurrent.CompletableFuture

class BlastFurnaceRecipesProvider extends TechRebornRecipesProvider {

	public final int ARMOR_POWER = 128
	public final int ARMOR_TIME = 140
	public final int ARMOR_HEAT = 1000
	public final int TOOL_POWER = ARMOR_POWER
	public final int TOOL_TIME = ARMOR_TIME
	public final int TOOL_HEAT = ARMOR_HEAT

	BlastFurnaceRecipesProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture)
	}

	@Override
	void generateRecipes() {
		generateBoots()
		generateChestplate()
		generateHelmet()
		generateLeggings()
		generateHorseArmor()
		generateSword()
		generateShovel()
		generateHoe()
		generateAxe()
		generatePickaxe()
		generateGlassFromGlassPane()
		generateAnvil()
		generateSherds()
		generateMisc()
	}

	void generateBoots() {
		final int count = 2
		[
				(Items.GOLDEN_BOOTS)       : stack(Items.GOLD_INGOT, count),
				(Items.IRON_BOOTS)         : stack(Items.IRON_INGOT, count),
				(TRContent.BRONZE_BOOTS)   : stack(TRContent.Ingots.BRONZE, count),
				(TRContent.SILVER_BOOTS)   : stack(TRContent.Ingots.SILVER, count),
				(TRContent.STEEL_BOOTS)    : stack(TRContent.Ingots.STEEL, count)
		].each {boots, materialStack ->
			offerBlastFurnaceRecipe {
				ingredients boots, Items.SAND
				outputs materialStack, TRContent.Dusts.DARK_ASHES
				power ARMOR_POWER
				time ARMOR_TIME
				heat ARMOR_HEAT
				source "boots"
				criterion getCriterionName(boots), getCriterionConditions(boots)
			}
		}
	}

	void generateChestplate() {
		final int count = 5
		[
				(Items.GOLDEN_CHESTPLATE)       : stack(Items.GOLD_INGOT, count),
				(Items.IRON_CHESTPLATE)         : stack(Items.IRON_INGOT, count),
				(TRContent.BRONZE_CHESTPLATE)   : stack(TRContent.Ingots.BRONZE, count),
				(TRContent.SILVER_CHESTPLATE)   : stack(TRContent.Ingots.SILVER, count),
				(TRContent.STEEL_CHESTPLATE)    : stack(TRContent.Ingots.STEEL, count)
		].each {chestplate, materialStack ->
			offerBlastFurnaceRecipe {
				ingredients chestplate, Items.SAND
				outputs materialStack, TRContent.Dusts.DARK_ASHES
				power ARMOR_POWER
				time ARMOR_TIME
				heat ARMOR_HEAT
				source "chestplate"
				criterion getCriterionName(chestplate), getCriterionConditions(chestplate)
			}
		}
	}

	void generateHelmet() {
		final int count = 3
		[
				(Items.GOLDEN_HELMET)       : stack(Items.GOLD_INGOT, count),
				(Items.IRON_HELMET)         : stack(Items.IRON_INGOT, count),
				(TRContent.BRONZE_HELMET)   : stack(TRContent.Ingots.BRONZE, count),
				(TRContent.SILVER_HELMET)   : stack(TRContent.Ingots.SILVER, count),
				(TRContent.STEEL_HELMET)    : stack(TRContent.Ingots.STEEL, count)
		].each {helmet, materialStack ->
			offerBlastFurnaceRecipe {
				ingredients helmet, Items.SAND
				outputs materialStack, TRContent.Dusts.DARK_ASHES
				power ARMOR_POWER
				time ARMOR_TIME
				heat ARMOR_HEAT
				source "helmet"
				criterion getCriterionName(helmet), getCriterionConditions(helmet)
			}
		}
	}

	void generateLeggings() {
		final int count = 4
		[
			(Items.GOLDEN_LEGGINGS)       : stack(Items.GOLD_INGOT, count),
			(Items.IRON_LEGGINGS)         : stack(Items.IRON_INGOT, count),
			(TRContent.BRONZE_LEGGINGS)   : stack(TRContent.Ingots.BRONZE, count),
			(TRContent.SILVER_LEGGINGS)   : stack(TRContent.Ingots.SILVER, count),
			(TRContent.STEEL_LEGGINGS)    : stack(TRContent.Ingots.STEEL, count)
		].each {leggings, materialStack ->
			offerBlastFurnaceRecipe {
				ingredients leggings, Items.SAND
				outputs materialStack, TRContent.Dusts.DARK_ASHES
				power ARMOR_POWER
				time ARMOR_TIME
				heat ARMOR_HEAT
				source "leggings"
				criterion getCriterionName(leggings), getCriterionConditions(leggings)
			}
		}
	}

	void generateHorseArmor() {
		final int count = 4
		[
			(Items.GOLDEN_HORSE_ARMOR) : stack(Items.GOLD_INGOT, count),
			(Items.IRON_HORSE_ARMOR) : stack(Items.IRON_INGOT, count)
		].each {horseArmor, materialStack ->
			offerBlastFurnaceRecipe {
				ingredients horseArmor, Items.SAND
				outputs materialStack, TRContent.Dusts.DARK_ASHES
				power ARMOR_POWER
				time ARMOR_TIME
				heat ARMOR_HEAT
				source "horse_armor"
				criterion getCriterionName(horseArmor), getCriterionConditions(horseArmor)
			}
		}
	}

	void generateSword() {
		final int count = 5
		[
			(Items.GOLDEN_SWORD)       : stack(Items.GOLD_NUGGET, count),
			(Items.IRON_SWORD)         : stack(Items.IRON_NUGGET, count),
			(TRContent.BRONZE_SWORD)   : stack(TRContent.Nuggets.BRONZE, count)
		].each {sword, materialStack ->
			offerBlastFurnaceRecipe {
				ingredients sword, Items.SAND
				outputs materialStack, TRContent.Dusts.DARK_ASHES
				power TOOL_POWER
				time TOOL_TIME
				heat TOOL_HEAT
				source "sword"
				criterion getCriterionName(sword), getCriterionConditions(sword)
			}
		}
	}

	void generateShovel() {
		final int count = 3
		[
			(Items.GOLDEN_SHOVEL)       : stack(Items.GOLD_NUGGET, count),
			(Items.IRON_SHOVEL)         : stack(Items.IRON_NUGGET, count),
			(TRContent.BRONZE_SPADE)    : stack(TRContent.Nuggets.BRONZE, count)
		].each {shovel, materialStack ->
			offerBlastFurnaceRecipe {
				ingredients shovel, Items.SAND
				outputs materialStack, TRContent.Dusts.DARK_ASHES
				power TOOL_POWER
				time TOOL_TIME
				heat TOOL_HEAT
				source "shovel"
				criterion getCriterionName(shovel), getCriterionConditions(shovel)
			}
		}
	}

	void generateHoe() {
		final int count = 5
		[
			(Items.GOLDEN_HOE)       : stack(Items.GOLD_NUGGET, count),
			(Items.IRON_HOE)         : stack(Items.IRON_NUGGET, count),
			(TRContent.BRONZE_HOE)   : stack(TRContent.Nuggets.BRONZE, count)
		].each {hoe, materialStack ->
			offerBlastFurnaceRecipe {
				ingredients hoe, Items.SAND
				outputs materialStack, TRContent.Dusts.DARK_ASHES
				power TOOL_POWER
				time TOOL_TIME
				heat TOOL_HEAT
				source "hoe"
				criterion getCriterionName(hoe), getCriterionConditions(hoe)
			}
		}
	}

	void generateAxe() {
		final int count = 1
		[
			(Items.GOLDEN_AXE)       : stack(Items.GOLD_INGOT, count),
			(Items.IRON_AXE)         : stack(Items.IRON_INGOT, count),
			(TRContent.BRONZE_AXE)   : stack(TRContent.Ingots.BRONZE, count)
		].each {axe, materialStack ->
			offerBlastFurnaceRecipe {
				ingredients axe, Items.SAND
				outputs materialStack, TRContent.Dusts.DARK_ASHES
				power TOOL_POWER
				time TOOL_TIME
				heat TOOL_HEAT
				source "axe"
				criterion getCriterionName(axe), getCriterionConditions(axe)
			}
		}
	}

	void generatePickaxe() {
		final int count = 1
		[
			(Items.GOLDEN_PICKAXE)       : stack(Items.GOLD_INGOT, count),
			(Items.IRON_PICKAXE)         : stack(Items.IRON_INGOT, count),
			(TRContent.BRONZE_PICKAXE)   : stack(TRContent.Ingots.BRONZE, count)
		].each {pickaxe, materialStack ->
			offerBlastFurnaceRecipe {
				ingredients pickaxe, Items.SAND
				outputs materialStack, TRContent.Dusts.DARK_ASHES
				power TOOL_POWER
				time TOOL_TIME
				heat TOOL_HEAT
				source "pickaxe"
				criterion getCriterionName(pickaxe), getCriterionConditions(pickaxe)
			}
		}
	}

	void generateGlassFromGlassPane() {
		[
			(Items.GLASS_PANE) : Items.GLASS,
			(Items.BLACK_STAINED_GLASS_PANE) : Items.BLACK_STAINED_GLASS,
			(Items.BLUE_STAINED_GLASS_PANE) : Items.BLACK_STAINED_GLASS,
			(Items.BROWN_STAINED_GLASS_PANE) : Items.BROWN_STAINED_GLASS,
			(Items.CYAN_STAINED_GLASS_PANE) : Items.CYAN_STAINED_GLASS,
			(Items.GRAY_STAINED_GLASS_PANE) : Items.GRAY_STAINED_GLASS,
			(Items.GREEN_STAINED_GLASS_PANE) : Items.GREEN_STAINED_GLASS,
			(Items.LIGHT_BLUE_STAINED_GLASS_PANE) : Items.LIGHT_BLUE_STAINED_GLASS,
			(Items.LIGHT_GRAY_STAINED_GLASS_PANE) : Items.LIGHT_GRAY_STAINED_GLASS,
			(Items.LIME_STAINED_GLASS_PANE) : Items.LIME_STAINED_GLASS,
			(Items.MAGENTA_STAINED_GLASS_PANE) : Items.MAGENTA_STAINED_GLASS,
			(Items.ORANGE_STAINED_GLASS_PANE) : Items.ORANGE_STAINED_GLASS,
			(Items.PINK_STAINED_GLASS_PANE) : Items.PINK_STAINED_GLASS,
			(Items.PURPLE_STAINED_GLASS_PANE) : Items.PURPLE_STAINED_GLASS,
			(Items.RED_STAINED_GLASS_PANE) : Items.RED_STAINED_GLASS,
			(Items.WHITE_STAINED_GLASS_PANE) : Items.WHITE_STAINED_GLASS,
			(Items.YELLOW_STAINED_GLASS_PANE) : Items.YELLOW_STAINED_GLASS
		].each {(pane,glass) ->
			offerBlastFurnaceRecipe {
				ingredients stack(pane, 10)
				outputs stack(glass, 3)
				power 128
				time 150
				heat 1000
				source "glass_pane"
				criterion getCriterionName(pane), getCriterionConditions(pane)
			}
		}
	}

	void generateAnvil() {
		[
			(Items.ANVIL) : 12,
			(Items.CHIPPED_ANVIL) : 9,
			(Items.DAMAGED_ANVIL) : 6,
		].each {(anvil,amount) ->
			offerBlastFurnaceRecipe {
				ingredients anvil, stack(Items.SAND, 2)
				outputs stack(Items.IRON_INGOT, amount), stack(TRContent.Dusts.DARK_ASHES, 2)
				power 128
				time 300
				heat 1500
				source "anvil_gives_"+amount
				criterion getCriterionName(anvil), getCriterionConditions(anvil)
			}
		}
	}

	void generateSherds() {
		[
			(Items.FISHING_ROD) : Items.ANGLER_POTTERY_SHERD,
			(Items.BOW) : Items.ARCHER_POTTERY_SHERD,
			(Items.TOTEM_OF_UNDYING) : Items.ARMS_UP_POTTERY_SHERD,
			(Items.IRON_SWORD) : Items.BLADE_POTTERY_SHERD,
			(Items.GLASS_BOTTLE) : Items.BREWER_POTTERY_SHERD,
			(Items.CAMPFIRE) : Items.BURN_POTTERY_SHERD,
			(Items.GUNPOWDER) : Items.DANGER_POTTERY_SHERD,
			(Items.MAP) : Items.EXPLORER_POTTERY_SHERD,
			(Items.EMERALD) : Items.FRIEND_POTTERY_SHERD,
			(Items.HEART_OF_THE_SEA) : Items.HEART_POTTERY_SHERD,
			// Items.HEARTBREAK_POTTERY_SHERD is special, uses grinder
			(Items.BONE) : Items.HOWL_POTTERY_SHERD,
			(Items.IRON_PICKAXE) : Items.MINER_POTTERY_SHERD,
			(Items.RECOVERY_COMPASS) : Items.MOURNER_POTTERY_SHERD,
			(Items.CHEST) : Items.PLENTY_POTTERY_SHERD,
			(Items.DIAMOND) : Items.PRIZE_POTTERY_SHERD,
			(Items.WHEAT) : Items.SHEAF_POTTERY_SHERD,
			(Items.IRON_DOOR) : Items.SHELTER_POTTERY_SHERD,
			(Items.SKELETON_SKULL) : Items.SKULL_POTTERY_SHERD,
			(Items.TURTLE_SCUTE) : Items.SNORT_POTTERY_SHERD,
			(Items.ARMADILLO_SCUTE) : Items.SNORT_POTTERY_SHERD
		].each {material, sherd ->
			offerBlastFurnaceRecipe {
				ingredients stack(Items.CLAY_BALL, 4), material
				outputs sherd
				power 128
				time 100
				heat 1000
				criterion getCriterionName(material), getCriterionConditions(material)
			}
		}
	}

	void generateMisc() {
		offerBlastFurnaceRecipe {
			power 128
			time 200
			heat 1700
			ingredient {
				tag(TRConventionalTags.ALUMINUM_DUSTS)
			}
			outputs TRContent.Ingots.ALUMINUM
		}
		offerBlastFurnaceRecipe {
			power 128
			time 100
			heat 3000
			ingredients Items.COBBLESTONE
			outputs Items.BLACKSTONE
		}
		offerBlastFurnaceRecipe {
			power 128
			time 800
			heat 1700
			ingredients TRContent.Dusts.CHROME
			outputs TRContent.Ingots.CHROME
		}
		offerBlastFurnaceRecipe {
			power 128
			time 800
			heat 1700
			ingredients stack(TRContent.SmallDusts.CHROME, 4)
			outputs TRContent.Ingots.CHROME
			source("small_dust")
		}
		offerBlastFurnaceRecipe {
			power 128
			time 2000
			heat 3000
			ingredients TRConventionalTags.STEEL_INGOTS, TRConventionalTags.TUNGSTEN_INGOTS
			outputs TRContent.Ingots.HOT_TUNGSTENSTEEL, stack(TRContent.Dusts.DARK_ASHES, 4)
		}
		offerBlastFurnaceRecipe {
			power 128
			time 800
			heat 2000
			ingredients TRContent.RawMetals.IRIDIUM
			outputs TRContent.Ingots.IRIDIUM
		}
		offerBlastFurnaceRecipe {
			power 128
			time 200
			heat 1000
			ingredients stack(Items.IRON_BARS, 5)
			outputs Items.IRON_INGOT
			source("from_bars")
		}
		offerBlastFurnaceRecipe {
			power 128
			time 140
			heat 1000
			ingredients stack(Items.RAIL, 12), Items.SAND
			outputs stack(Items.IRON_INGOT, 3), TRContent.Dusts.DARK_ASHES
			source("from_rails")
		}
		offerBlastFurnaceRecipe {
			power 128
			time 20
			heat 1500
			ingredients stack(TRContent.Dusts.GALENA, 2)
			outputs TRContent.Ingots.LEAD, TRContent.Ingots.SILVER
		}
		offerBlastFurnaceRecipe {
			power 128
			time 20
			heat 1500
			ingredients stack(TRContent.SmallDusts.GALENA, 8)
			outputs TRContent.Ingots.LEAD, TRContent.Ingots.SILVER
			source("galena_small_dust")
		}
		offerBlastFurnaceRecipe {
			power 128
			time 100
			heat 1000
			ingredients TRConventionalTags.IRON_ORES
			ingredient {
				fluid(ModFluids.CALCIUM_CARBONATE, TRContent.CELL)
			}
			outputs stack(TRContent.Ingots.REFINED_IRON, 3), TRContent.CELL
		}
		offerBlastFurnaceRecipe {
			power 128
			time 100
			heat 1500
			ingredients TRConventionalTags.PYRITE_ORES
			ingredient {
				fluid(ModFluids.CALCIUM_CARBONATE, TRContent.CELL)
			}
			outputs stack(TRContent.Ingots.REFINED_IRON, 2), TRContent.CELL
			source("pyrite_ore")
		}
		offerBlastFurnaceRecipe {
			power 128
			time 100
			heat 1000
			ingredients TRConventionalTags.STEEL_DUSTS
			outputs TRContent.Ingots.STEEL
		}
		offerBlastFurnaceRecipe {
			power 128
			time 100
			heat 1000
			ingredients stack(TRContent.SmallDusts.STEEL, 4)
			outputs TRContent.Ingots.STEEL
			source("small_dust")
		}
		offerBlastFurnaceRecipe {
			power 128
			time 3600
			heat 1500
			ingredients TRConventionalTags.TITANIUM_DUSTS
			outputs TRContent.Ingots.TITANIUM
		}
		offerBlastFurnaceRecipe {
			power 128
			time 3600
			heat 1500
			ingredients stack(TRContent.SmallDusts.TITANIUM, 4)
			outputs TRContent.Ingots.TITANIUM
			source("small_dust")
		}
		offerBlastFurnaceRecipe {
			power 128
			time 2000
			heat 2500
			ingredients TRConventionalTags.RAW_TUNGSTEN_ORES
			outputs TRContent.Ingots.TUNGSTEN
		}
		offerBlastFurnaceRecipe {
			power 128
			time 2000
			heat 2500
			ingredients stack(TRContent.SmallDusts.TUNGSTEN, 4)
			outputs TRContent.Ingots.TUNGSTEN
			source("small_dust")
		}
		offerBlastFurnaceRecipe {
			power 128
			time 1000
			heat 1500
			ingredients stack(TRContent.Dusts.QUARTZ, 2)
			ingredient {
				fluid(ModFluids.CARBON, TRContent.CELL, 4)
			}
			outputs cellStack(ModFluids.SILICON, 2), cellStack(ModFluids.COMPRESSED_AIR, 2)
			id "blast_furnace/silicon_cell"
		}
		offerBlastFurnaceRecipe {
			power 128
			time 1000
			heat 1500
			ingredient {
				fluid(ModFluids.SILICON, TRContent.CELL, 2)
			}
			outputs TRContent.Plates.SILICON, stack(TRContent.CELL, 2)
		}
		offerBlastFurnaceRecipe {
			power 128
			time 1000
			heat 1500
			ingredients stack(Items.RED_SAND, 8)
			ingredient {
				fluid(ModFluids.CARBON, TRContent.CELL, 5)
			}
			outputs stack(Items.SOUL_SAND, 8)
		}
		offerBlastFurnaceRecipe {
			power 128
			time 500
			heat 1000
			ingredients TRContent.Ingots.REFINED_IRON
			ingredient {
				tag(TRConventionalTags.COAL_DUSTS, 4)
			}
			outputs TRContent.Ingots.STEEL, stack(TRContent.Dusts.DARK_ASHES, 2)
			source("refined_iron")
		}
		offerBlastFurnaceRecipe {
			power 128
			time 500
			heat 1000
			ingredients TRContent.Ingots.REFINED_IRON
			ingredient {
				fluid(ModFluids.CARBON, TRContent.CELL, 2)
			}
			outputs TRContent.Ingots.STEEL, stack(TRContent.CELL, 2)
			source("refined_iron_and_carbon")
		}
	}
}

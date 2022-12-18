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
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.registry.RegistryWrapper
import techreborn.datagen.recipes.TechRebornRecipesProvider
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
	}

	void generateBoots() {
		final int count = 2
		[
				(Items.GOLDEN_BOOTS)       : new ItemStack(Items.GOLD_INGOT, count),
				(Items.IRON_BOOTS)         : new ItemStack(Items.IRON_INGOT, count),
				(TRContent.BRONZE_BOOTS)   : new ItemStack(TRContent.Ingots.BRONZE, count),
				(TRContent.SILVER_BOOTS)   : new ItemStack(TRContent.Ingots.SILVER, count),
				(TRContent.STEEL_BOOTS)    : new ItemStack(TRContent.Ingots.STEEL, count)
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
				(Items.GOLDEN_CHESTPLATE)       : new ItemStack(Items.GOLD_INGOT, count),
				(Items.IRON_CHESTPLATE)         : new ItemStack(Items.IRON_INGOT, count),
				(TRContent.BRONZE_CHESTPLATE)   : new ItemStack(TRContent.Ingots.BRONZE, count),
				(TRContent.SILVER_CHESTPLATE)   : new ItemStack(TRContent.Ingots.SILVER, count),
				(TRContent.STEEL_CHESTPLATE)    : new ItemStack(TRContent.Ingots.STEEL, count)
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
				(Items.GOLDEN_HELMET)       : new ItemStack(Items.GOLD_INGOT, count),
				(Items.IRON_HELMET)         : new ItemStack(Items.IRON_INGOT, count),
				(TRContent.BRONZE_HELMET)   : new ItemStack(TRContent.Ingots.BRONZE, count),
				(TRContent.SILVER_HELMET)   : new ItemStack(TRContent.Ingots.SILVER, count),
				(TRContent.STEEL_HELMET)    : new ItemStack(TRContent.Ingots.STEEL, count)
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
			(Items.GOLDEN_LEGGINGS)       : new ItemStack(Items.GOLD_INGOT, count),
			(Items.IRON_LEGGINGS)         : new ItemStack(Items.IRON_INGOT, count),
			(TRContent.BRONZE_LEGGINGS)   : new ItemStack(TRContent.Ingots.BRONZE, count),
			(TRContent.SILVER_LEGGINGS)   : new ItemStack(TRContent.Ingots.SILVER, count),
			(TRContent.STEEL_LEGGINGS)    : new ItemStack(TRContent.Ingots.STEEL, count)
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
			(Items.GOLDEN_HORSE_ARMOR) : new ItemStack(Items.GOLD_INGOT, count),
			(Items.IRON_HORSE_ARMOR) : new ItemStack(Items.IRON_INGOT, count)
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
			(Items.GOLDEN_SWORD)       : new ItemStack(Items.GOLD_NUGGET, count),
			(Items.IRON_SWORD)         : new ItemStack(Items.IRON_NUGGET, count),
			(TRContent.BRONZE_SWORD)   : new ItemStack(TRContent.Nuggets.BRONZE, count)
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
			(Items.GOLDEN_SHOVEL)       : new ItemStack(Items.GOLD_NUGGET, count),
			(Items.IRON_SHOVEL)         : new ItemStack(Items.IRON_NUGGET, count),
			(TRContent.BRONZE_SPADE)    : new ItemStack(TRContent.Nuggets.BRONZE, count)
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
			(Items.GOLDEN_HOE)       : new ItemStack(Items.GOLD_NUGGET, count),
			(Items.IRON_HOE)         : new ItemStack(Items.IRON_NUGGET, count),
			(TRContent.BRONZE_HOE)   : new ItemStack(TRContent.Nuggets.BRONZE, count)
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
			(Items.GOLDEN_AXE)       : new ItemStack(Items.GOLD_INGOT, count),
			(Items.IRON_AXE)         : new ItemStack(Items.IRON_INGOT, count),
			(TRContent.BRONZE_AXE)   : new ItemStack(TRContent.Ingots.BRONZE, count)
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
			(Items.GOLDEN_PICKAXE)       : new ItemStack(Items.GOLD_INGOT, count),
			(Items.IRON_PICKAXE)         : new ItemStack(Items.IRON_INGOT, count),
			(TRContent.BRONZE_PICKAXE)   : new ItemStack(TRContent.Ingots.BRONZE, count)
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
				ingredients new ItemStack(pane, 10)
				outputs new ItemStack(glass, 3)
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
				ingredients anvil, new ItemStack(Items.SAND, 2)
				outputs new ItemStack(Items.IRON_INGOT, amount), new ItemStack(TRContent.Dusts.DARK_ASHES, 2)
				power 128
				time 300
				heat 1500
				source "anvil_gives_"+amount
				criterion getCriterionName(anvil), getCriterionConditions(anvil)
			}
		}
	}
}

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

package techreborn.datagen.recipes.machine.industrial_grinder

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.registry.RegistryWrapper
import techreborn.datagen.recipes.TechRebornRecipesProvider
import techreborn.init.ModFluids
import techreborn.init.TRContent

import java.util.concurrent.CompletableFuture

class IndustrialGrinderRecipesProvider extends TechRebornRecipesProvider {

	public final int ARMOR_POWER = 128
	public final int ARMOR_TIME = 140
	public final long ARMOR_FLUID_AMOUNT = 1000L // in millibuckets
	public final int TOOL_POWER = ARMOR_POWER
	public final int TOOL_TIME = ARMOR_TIME
	public final long TOOL_FLUID_AMOUNT = 500L // in millibuckets
	var dustMap = TRContent.SmallDusts.SD2DMap

	IndustrialGrinderRecipesProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
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
	}

	void generateBoots() {
		final int count = 2
		[
			(Items.DIAMOND_BOOTS)      : TRContent.SmallDusts.DIAMOND,
			(TRContent.PERIDOT_BOOTS)  : TRContent.SmallDusts.PERIDOT,
			(TRContent.RUBY_BOOTS)     : TRContent.SmallDusts.RUBY,
			(TRContent.SAPPHIRE_BOOTS) : TRContent.SmallDusts.SAPPHIRE
		].each {boots, smallDust ->
			offerIndustrialGrinderRecipe {
				ingredients boots
				outputs new ItemStack(dustMap.get(smallDust), count-1), new ItemStack(smallDust, 2)
				power ARMOR_POWER
				time ARMOR_TIME
				fluidAmount ARMOR_FLUID_AMOUNT
				source "boots_with_water"
				criterion getCriterionName(boots), getCriterionConditions(boots)
			}
			offerIndustrialGrinderRecipe {
				ingredients boots
				outputs new ItemStack(dustMap.get(smallDust), count+1), smallDust
				power ARMOR_POWER
				time ARMOR_TIME
				fluidAmount ARMOR_FLUID_AMOUNT
				fluid ModFluids.MERCURY.getFluid()
				source "boots_with_mercury"
				criterion getCriterionName(boots), getCriterionConditions(boots)
			}
		}
	}

	void generateChestplate() {
		final int count = 5
		[
			(Items.DIAMOND_CHESTPLATE)      : TRContent.SmallDusts.DIAMOND,
			(TRContent.PERIDOT_CHESTPLATE)  : TRContent.SmallDusts.PERIDOT,
			(TRContent.RUBY_CHESTPLATE)     : TRContent.SmallDusts.RUBY,
			(TRContent.SAPPHIRE_CHESTPLATE) : TRContent.SmallDusts.SAPPHIRE
		].each {chestplate, smallDust ->
			offerIndustrialGrinderRecipe {
				ingredients chestplate
				outputs new ItemStack(dustMap.get(smallDust), count-1), new ItemStack(smallDust, 2)
				power ARMOR_POWER
				time ARMOR_TIME
				fluidAmount ARMOR_FLUID_AMOUNT
				source "chestplate_with_water"
				criterion getCriterionName(chestplate), getCriterionConditions(chestplate)
			}
			offerIndustrialGrinderRecipe {
				ingredients chestplate
				outputs new ItemStack(dustMap.get(smallDust), count+1), smallDust
				power ARMOR_POWER
				time ARMOR_TIME
				fluidAmount ARMOR_FLUID_AMOUNT
				fluid ModFluids.MERCURY.getFluid()
				source "chestplate_with_mercury"
				criterion getCriterionName(chestplate), getCriterionConditions(chestplate)
			}
		}
	}

	void generateHelmet() {
		final int count = 3
		[
			(Items.DIAMOND_HELMET)      : TRContent.SmallDusts.DIAMOND,
			(TRContent.PERIDOT_HELMET)  : TRContent.SmallDusts.PERIDOT,
			(TRContent.RUBY_HELMET)     : TRContent.SmallDusts.RUBY,
			(TRContent.SAPPHIRE_HELMET) : TRContent.SmallDusts.SAPPHIRE
		].each {helmet, smallDust ->
			offerIndustrialGrinderRecipe {
				ingredients helmet
				outputs new ItemStack(dustMap.get(smallDust), count-1), new ItemStack(smallDust, 2)
				power ARMOR_POWER
				time ARMOR_TIME
				fluidAmount ARMOR_FLUID_AMOUNT
				source "helmet_with_water"
				criterion getCriterionName(helmet), getCriterionConditions(helmet)
			}
			offerIndustrialGrinderRecipe {
				ingredients helmet
				outputs new ItemStack(dustMap.get(smallDust), count+1), smallDust
				power ARMOR_POWER
				time ARMOR_TIME
				fluidAmount ARMOR_FLUID_AMOUNT
				fluid ModFluids.MERCURY.getFluid()
				source "helmet_with_mercury"
				criterion getCriterionName(helmet), getCriterionConditions(helmet)
			}
		}
	}

	void generateLeggings() {
		final int count = 4
		[
			(Items.DIAMOND_LEGGINGS)      : TRContent.SmallDusts.DIAMOND,
			(TRContent.PERIDOT_LEGGINGS)  : TRContent.SmallDusts.PERIDOT,
			(TRContent.RUBY_LEGGINGS)     : TRContent.SmallDusts.RUBY,
			(TRContent.SAPPHIRE_LEGGINGS) : TRContent.SmallDusts.SAPPHIRE
		].each {leggings, smallDust ->
			offerIndustrialGrinderRecipe {
				ingredients leggings
				outputs new ItemStack(dustMap.get(smallDust), count-1), new ItemStack(smallDust, 2)
				power ARMOR_POWER
				time ARMOR_TIME
				fluidAmount ARMOR_FLUID_AMOUNT
				source "leggings_with_water"
				criterion getCriterionName(leggings), getCriterionConditions(leggings)
			}
			offerIndustrialGrinderRecipe {
				ingredients leggings
				outputs new ItemStack(dustMap.get(smallDust), count+1), smallDust
				power ARMOR_POWER
				time ARMOR_TIME
				fluidAmount ARMOR_FLUID_AMOUNT
				fluid ModFluids.MERCURY.getFluid()
				source "leggings_with_mercury"
				criterion getCriterionName(leggings), getCriterionConditions(leggings)
			}
		}
	}

	void generateHorseArmor() {
		final int count = 4
		[
			(Items.DIAMOND_HORSE_ARMOR) : TRContent.SmallDusts.DIAMOND
		].each {horseArmor, smallDust ->
			offerIndustrialGrinderRecipe {
				ingredients horseArmor
				outputs new ItemStack(dustMap.get(smallDust), count-1), new ItemStack(smallDust, 2)
				power ARMOR_POWER
				time ARMOR_TIME
				fluidAmount ARMOR_FLUID_AMOUNT
				source "horse_armor_with_water"
				criterion getCriterionName(horseArmor), getCriterionConditions(horseArmor)
			}
			offerIndustrialGrinderRecipe {
				ingredients horseArmor
				outputs new ItemStack(dustMap.get(smallDust), count+1), smallDust
				power ARMOR_POWER
				time ARMOR_TIME
				fluidAmount ARMOR_FLUID_AMOUNT
				fluid ModFluids.MERCURY.getFluid()
				source "horse_armor_with_mercury"
				criterion getCriterionName(horseArmor), getCriterionConditions(horseArmor)
			}
		}
	}

	void generateSword() {
		[
			(Items.DIAMOND_SWORD)      : TRContent.SmallDusts.DIAMOND,
			(TRContent.PERIDOT_SWORD)  : TRContent.SmallDusts.PERIDOT,
			(TRContent.RUBY_SWORD)     : TRContent.SmallDusts.RUBY,
			(TRContent.SAPPHIRE_SWORD) : TRContent.SmallDusts.SAPPHIRE
		].each {sword, smallDust ->
			offerIndustrialGrinderRecipe {
				ingredients sword
				outputs new ItemStack(smallDust, 2), TRContent.SmallDusts.SAW
				power TOOL_POWER
				time TOOL_TIME
				fluidAmount TOOL_FLUID_AMOUNT
				source "sword_with_water"
				criterion getCriterionName(sword), getCriterionConditions(sword)
			}
			offerIndustrialGrinderRecipe {
				ingredients sword
				outputs dustMap.get(smallDust), smallDust, TRContent.SmallDusts.SAW
				power TOOL_POWER
				time TOOL_TIME
				fluidAmount TOOL_FLUID_AMOUNT
				fluid ModFluids.MERCURY.getFluid()
				source "sword_with_mercury"
				criterion getCriterionName(sword), getCriterionConditions(sword)
			}
		}
	}

	void generateShovel() {
		[
			(Items.DIAMOND_SHOVEL)      : TRContent.SmallDusts.DIAMOND,
			(TRContent.PERIDOT_SPADE)   : TRContent.SmallDusts.PERIDOT,
			(TRContent.RUBY_SPADE)      : TRContent.SmallDusts.RUBY,
			(TRContent.SAPPHIRE_SPADE)  : TRContent.SmallDusts.SAPPHIRE
		].each {shovel, smallDust ->
			offerIndustrialGrinderRecipe {
				ingredients shovel
				outputs smallDust, TRContent.SmallDusts.SAW
				power TOOL_POWER
				time TOOL_TIME
				fluidAmount TOOL_FLUID_AMOUNT
				source "shovel_with_water"
				criterion getCriterionName(shovel), getCriterionConditions(shovel)
			}
			offerIndustrialGrinderRecipe {
				ingredients shovel
				outputs new ItemStack(smallDust, 2), TRContent.SmallDusts.SAW
				power TOOL_POWER
				time TOOL_TIME
				fluidAmount TOOL_FLUID_AMOUNT
				fluid ModFluids.MERCURY.getFluid()
				source "shovel_with_mercury"
				criterion getCriterionName(shovel), getCriterionConditions(shovel)
			}
		}
	}

	void generateHoe() {
		[
			(Items.DIAMOND_HOE)      : TRContent.SmallDusts.DIAMOND,
			(TRContent.PERIDOT_HOE)  : TRContent.SmallDusts.PERIDOT,
			(TRContent.RUBY_HOE)     : TRContent.SmallDusts.RUBY,
			(TRContent.SAPPHIRE_HOE) : TRContent.SmallDusts.SAPPHIRE
		].each {hoe, smallDust ->
			offerIndustrialGrinderRecipe {
				ingredients hoe
				outputs new ItemStack(smallDust, 2), TRContent.SmallDusts.SAW
				power TOOL_POWER
				time TOOL_TIME
				fluidAmount TOOL_FLUID_AMOUNT
				source "hoe_with_water"
				criterion getCriterionName(hoe), getCriterionConditions(hoe)
			}
			offerIndustrialGrinderRecipe {
				ingredients hoe
				outputs dustMap.get(smallDust), smallDust, TRContent.SmallDusts.SAW
				power TOOL_POWER
				time TOOL_TIME
				fluidAmount TOOL_FLUID_AMOUNT
				fluid ModFluids.MERCURY.getFluid()
				source "hoe_with_mercury"
				criterion getCriterionName(hoe), getCriterionConditions(hoe)
			}
		}
	}

	void generateAxe() {
		[
			(Items.DIAMOND_AXE)      : TRContent.SmallDusts.DIAMOND,
			(TRContent.PERIDOT_AXE)  : TRContent.SmallDusts.PERIDOT,
			(TRContent.RUBY_AXE)     : TRContent.SmallDusts.RUBY,
			(TRContent.SAPPHIRE_AXE) : TRContent.SmallDusts.SAPPHIRE
		].each {axe, smallDust ->
			offerIndustrialGrinderRecipe {
				ingredients axe
				outputs new ItemStack(smallDust, 3), TRContent.SmallDusts.SAW
				power TOOL_POWER
				time TOOL_TIME
				fluidAmount TOOL_FLUID_AMOUNT
				source "axe_with_water"
				criterion getCriterionName(axe), getCriterionConditions(axe)
			}
			offerIndustrialGrinderRecipe {
				ingredients axe
				outputs dustMap.get(smallDust), new ItemStack(smallDust, 3), TRContent.SmallDusts.SAW
				power TOOL_POWER
				time TOOL_TIME
				fluidAmount TOOL_FLUID_AMOUNT
				fluid ModFluids.MERCURY.getFluid()
				source "axe_with_mercury"
				criterion getCriterionName(axe), getCriterionConditions(axe)
			}
		}
	}

	void generatePickaxe() {
		[
			(Items.DIAMOND_PICKAXE)      : TRContent.SmallDusts.DIAMOND,
			(TRContent.PERIDOT_PICKAXE)  : TRContent.SmallDusts.PERIDOT,
			(TRContent.RUBY_PICKAXE)     : TRContent.SmallDusts.RUBY,
			(TRContent.SAPPHIRE_PICKAXE) : TRContent.SmallDusts.SAPPHIRE
		].each {pickaxe, smallDust ->
			offerIndustrialGrinderRecipe {
				ingredients pickaxe
				outputs new ItemStack(smallDust, 3), TRContent.SmallDusts.SAW
				power TOOL_POWER
				time TOOL_TIME
				fluidAmount TOOL_FLUID_AMOUNT
				source "pickaxe_with_water"
				criterion getCriterionName(pickaxe), getCriterionConditions(pickaxe)
			}
			offerIndustrialGrinderRecipe {
				ingredients pickaxe
				outputs dustMap.get(smallDust), new ItemStack(smallDust, 3), TRContent.SmallDusts.SAW
				power TOOL_POWER
				time TOOL_TIME
				fluidAmount TOOL_FLUID_AMOUNT
				fluid ModFluids.MERCURY.getFluid()
				source "pickaxe_with_mercury"
				criterion getCriterionName(pickaxe), getCriterionConditions(pickaxe)
			}
		}
	}

}

/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
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

package techreborn.init.recipes;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import reborncore.api.recipe.RecipeHandler;

import techreborn.api.recipe.machines.IndustrialGrinderRecipe;
import techreborn.blocks.BlockOre;
import techreborn.init.ModFluids;

import java.security.InvalidParameterException;

/**
 * @author Prospector, estebes
 */
public class IndustrialGrinderRecipes extends RecipeMethods {
	// Fields >>
	static FluidStack WATER = new FluidStack(FluidRegistry.WATER, 1000);
	static FluidStack MERCURY = new FluidStack(ModFluids.MERCURY, 1000);
	static FluidStack SODIUM_PERSULFATE = new FluidStack(ModFluids.SODIUMPERSULFATE, 1000);
	// << Fields

	public static void init() {
		// Vanilla ores >>
		// Coal
		register(getOre("oreCoal"), WATER, 100, 128, getStack(Items.COAL, 1), getMaterial("coal", Type.DUST),getMaterial("thorium", Type.SMALL_DUST));

		// Iron
		register(getOre("oreIron"), WATER, 100, 128, getMaterial("iron", 2, Type.DUST), getMaterial("tin", Type.SMALL_DUST), getMaterial("nickel", Type.SMALL_DUST));

		// Gold
		register(getOre("oreGold"), WATER, 100, 128, getMaterial("gold", 2, Type.DUST), getMaterial("copper", Type.SMALL_DUST), getMaterial("nickel", Type.SMALL_DUST));
		register(getOre("oreGold"), MERCURY, 100, 128, getMaterial("gold", 3, Type.DUST), getMaterial("copper", Type.SMALL_DUST), getMaterial("nickel", Type.SMALL_DUST));
		register(getOre("oreGold"), SODIUM_PERSULFATE, 100, 128, getMaterial("gold", 2, Type.DUST), getMaterial("copper", Type.DUST), getMaterial("nickel", Type.SMALL_DUST));
		// << Vanilla ores


		


		
		register(getOre("oreLapis"), WATER, 100, 64, getStack(Items.DYE, 12, 4), getMaterial("lazurite", 3, Type.DUST));
		
		register(getOre("oreRedstone"), WATER, 100, 64, getStack(Items.REDSTONE, 10), getMaterial("glowstone", 2, Type.SMALL_DUST));

		register(getOre("oreDiamond"), WATER, 100, 64, getStack(Items.DIAMOND), getMaterial("diamond", 6, Type.SMALL_DUST), getMaterial("coal", Type.DUST));
		register(getOre("oreDiamond"), MERCURY, 100, 64, getStack(Items.DIAMOND, 2), getMaterial("diamond", 3, Type.SMALL_DUST));

		register(getOre("oreEmerald"), WATER, 100, 64, getStack(Items.EMERALD), getMaterial("emerald", 6, Type.SMALL_DUST));
		register(getOre("oreEmerald"), MERCURY, 100, 64, getStack(Items.EMERALD, 2), getMaterial("emerald", 3, Type.SMALL_DUST));
		
		//TR ores
		register(getOre("oreCopper"), WATER, 100, 64, getMaterial("copper", 2, Type.DUST), getMaterial("gold", Type.SMALL_DUST), getMaterial("nickel", Type.SMALL_DUST));
		register(getOre("oreCopper"), SODIUM_PERSULFATE, 100, 64, getMaterial("copper", 3, Type.DUST), getMaterial("gold", Type.SMALL_DUST), getMaterial("nickel", Type.SMALL_DUST));
		register(getOre("oreCopper"), MERCURY, 100, 64, getMaterial("copper", 3, Type.DUST), getMaterial("gold", Type.DUST));

		register(getOre("oreTin"), WATER, 100, 64, getMaterial("tin", 2, Type.DUST), getMaterial("iron", Type.SMALL_DUST), getMaterial("zinc", Type.SMALL_DUST));
		register(getOre("oreTin"), SODIUM_PERSULFATE, 100, 64, getMaterial("tin", 2, Type.DUST), getMaterial("iron", Type.SMALL_DUST), getMaterial("zinc", Type.DUST));
		
		register(getOre("oreLead"), WATER, 100, 64, getMaterial("lead", 2, Type.DUST), getMaterial("galena", 2, Type.SMALL_DUST));
		
		register(getOre("oreSilver"), WATER, 100, 64, getMaterial("silver", 2, Type.DUST), getMaterial("galena", 2, Type.SMALL_DUST));

		register(getOre("oreGalena"), WATER, 100, 64, getMaterial("galena", 2, Type.DUST), getMaterial("sulfur", Type.DUST));
		register(getOre("oreGalena"), MERCURY, 100, 64, getMaterial("galena", 2, Type.DUST), getMaterial("sulfur", Type.DUST), getMaterial("silver", Type.DUST));
		
		register(BlockOre.getOreByName("bauxite"), WATER, 100, 64, getMaterial("bauxite", 4, Type.DUST), getMaterial("aluminum", Type.DUST));

		// Iridium
		register(getOre("oreIridium"), WATER, 100, 128, getMaterial("iridium", 1, Type.DUST), getMaterial("iridium", 6, Type.SMALL_DUST),
			getMaterial("platinum", 2, Type.SMALL_DUST));
		register(getOre("oreIridium"), SODIUM_PERSULFATE, 100, 128, getMaterial("iridium", 1, Type.DUST), getMaterial("iridium", 6, Type.SMALL_DUST),
			getMaterial("platinum", 2, Type.DUST));

		if (oresExist("oreUranium")) {
			register(getOre("oreUranium"), WATER, 100, 128, getMaterial("uranium", 2, Type.DUST), getMaterial("plutonium", 2, Type.SMALL_DUST), getMaterial("thorium", 1, Type.DUST));
		}
		
//		if (oresExist("oreUranium", "uran238", "smallUran235")) {
//			register(getOre("oreUranium"), WATER, 100, 64, getOre("uran238", 8), getOre("smallUran235", 2));
//		}

		if (oresExist("orePitchblende", "uran238", "uran235")) {
			register(getOre("orePitchblende"), WATER, 100, 64, getOre("uran238", 8), getOre("smallUran235", 2));
		}

		register(getOre("oreRuby"), WATER, 100, 64, getMaterial("ruby", Type.GEM), getMaterial("ruby", 6, Type.SMALL_DUST), getMaterial("red_garnet", 2, Type.SMALL_DUST));
		
		register(getOre("oreSapphire"), WATER, 100, 64, getMaterial("sapphire", Type.GEM), getMaterial("sapphire", 6, Type.SMALL_DUST), getMaterial("peridot", 2, Type.SMALL_DUST));
		
		register(getOre("oreQuartz"), WATER, 100, 128, getStack(Items.QUARTZ, 4), getMaterial("netherrack", 1, Type.DUST));
		
		register(getOre("orePyrite"), WATER, 100, 64, getMaterial("pyrite", 5, Type.DUST), getMaterial("sulfur", 2, Type.DUST));
		
		register(getOre("oreCinnabar"), WATER, 100, 64, getMaterial("cinnabar", 5, Type.DUST), getMaterial("redstone", 2, Type.SMALL_DUST), getMaterial("glowstone", Type.SMALL_DUST));

		register(getOre("oreSphalerite"), WATER, 100, 64, getMaterial("sphalerite", 5, Type.DUST), getMaterial("zinc", Type.DUST), getMaterial("yellow_garnet", Type.SMALL_DUST));
		register(getOre("oreSphalerite"), SODIUM_PERSULFATE, 100, 64, getMaterial("sphalerite", 5, Type.DUST), getMaterial("zinc", 3, Type.DUST), getMaterial("yellow_garnet", Type.SMALL_DUST));

		register(getOre("oreTungsten"), WATER, 100, 64, getMaterial("tungsten", 2, Type.DUST), getMaterial("iron", 3, Type.SMALL_DUST), getMaterial("manganese", 3, Type.SMALL_DUST));

		register(getOre("oreSheldonite"), WATER, 100, 64, getMaterial("platinum", 2, Type.DUST), getMaterial("nickel", Type.DUST), getMaterial("iridium", 1, Type.SMALL_DUST));
		register(getOre("oreSheldonite"), MERCURY, 100, 64, getMaterial("platinum", 3, Type.DUST), getMaterial("nickel", Type.DUST), getMaterial("iridium", 1, Type.SMALL_DUST));

		register(getOre("orePeridot"), WATER, 100, 64, getMaterial("peridot", Type.GEM), getMaterial("peridot", 6, Type.SMALL_DUST), getMaterial("emerald", 2, Type.SMALL_DUST));
		
		register(getOre("oreSodalite"), WATER, 100, 64, getMaterial("sodalite", 12, Type.DUST), getMaterial("aluminum", 3, Type.DUST));

		if (oresExist("oreApatite", "gemApatite")) {
			register(getOre("oreApatite"), WATER, 100, 64, getOre("gemApatite", 8), getMaterial("phosphorous", 2, Type.SMALL_DUST));
		}

		if (oresExist("oreNickel")) {
			register(getOre("oreNickel"), WATER, 100, 64, getMaterial("nickel", 2, Type.DUST), getMaterial("iron", Type.SMALL_DUST), getMaterial("platinum", Type.SMALL_DUST));
			register(getOre("oreNickel"), MERCURY, 100, 64, getMaterial("nickel", 3, Type.DUST), getMaterial("iron", Type.SMALL_DUST), getMaterial("platinum", Type.DUST));
			register(getOre("oreNickel"), SODIUM_PERSULFATE, 100, 64, getMaterial("nickel", 3, Type.DUST), getMaterial("iron", Type.SMALL_DUST), getMaterial("platinum", Type.SMALL_DUST));
		}

		if (oresExist("oreZinc")) {
			register(getOre("oreZinc"), WATER, 100, 64, getMaterial("zinc", 2, Type.DUST), getMaterial("iron", 2, Type.SMALL_DUST), getMaterial("tin", Type.SMALL_DUST));
			register(getOre("oreZinc"), SODIUM_PERSULFATE, 100, 64, getMaterial("zinc", 2, Type.DUST), getMaterial("iron", Type.DUST), getMaterial("tin", Type.SMALL_DUST));
		}

		if (oresExist("oreAmethyst", "gemAmethyst")) {
			register(getOre("oreAmethyst"), WATER, 100, 64, getOre("gemAmethyst", 2));
		}

		if (oresExist("oreTopaz", "gemTopaz")) {
			register(getOre("oreTopaz"), WATER, 100, 64, getOre("gemTopaz", 2));
		}

		if (oresExist("oreTanzanite", "gemTanzanite")) {
			register(getOre("oreTanzanite"), WATER, 100, 64, getOre("gemTanzanite", 2));
		}

		if (oresExist("oreMalachite", "gemMalachite")) {
			register(getOre("oreMalachite"), WATER, 100, 64, getOre("gemMalachite", 2));
		}

		if (oresExist("oreAluminum")) {
			register(getOre("oreAluminum"), WATER, 100, 64, getMaterial("aluminum", 2, Type.DUST), getMaterial("bauxite", 2, Type.SMALL_DUST));
		}

		if (oresExist("oreArdite", "dustArdite")) {
			register(getOre("oreArdite"), WATER, 100, 64, getOre("dustArdite", 2), getMaterial("sulfur", 2, Type.SMALL_DUST));
		}

		if (oresExist("oreCobalt", "dustCobalt")) {
			register(getOre("oreCobalt"), WATER, 100, 64, getOre("dustCobalt", 2), getMaterial("sulfur", 2, Type.SMALL_DUST));
		}

		if (oresExist("oreOsmium", "dustOsmium")) {
			register(getOre("oreOsmium"), WATER, 100, 64, getOre("dustOsmium", 2), getMaterial("platinum", 2, Type.SMALL_DUST), getMaterial("iron", 1, Type.SMALL_DUST));
		}

		if (oresExist("oreTeslatite", "dustTeslatite")) {
			register(getOre("oreTeslatite"), WATER, 100, 64, getOre("dustTeslatite", 2), getMaterial("redstone", 3, Type.SMALL_DUST));
		}

		if (oresExist("oreSulfur")) {
			register(getOre("oreSulfur"), WATER, 100, 64, getMaterial("sulfur", 2, Type.DUST), getMaterial("sulfur", Type.SMALL_DUST));
		}

		if (oresExist("oreSaltpeter")) {
			register(getOre("oreSaltpeter"), WATER, 100, 64, getMaterial("saltpeter", 2, Type.DUST), getMaterial("saltpeter", Type.SMALL_DUST));
		}

		register(getStack(Blocks.NETHERRACK, 16), WATER, 1600, 64, getMaterial("netherrack", 16, Type.DUST), getStack(Items.GOLD_NUGGET));
		register(getStack(Blocks.NETHERRACK, 8), MERCURY, 800, 64, getMaterial("netherrack", 8, Type.DUST), getStack(Items.GOLD_NUGGET));
		
		register(new ItemStack(Blocks.END_STONE), WATER, 100, 64, getMaterial("endstone", 2, Type.DUST));
		register(new ItemStack(Blocks.END_BRICKS), WATER, 100, 64, getMaterial("endstone", 4, Type.DUST));

	}

	static void register(ItemStack output, FluidStack fluid, int ticks, int euPerTick, ItemStack... inputs) {
		if (inputs.length == 3)
			RecipeHandler.addRecipe(new IndustrialGrinderRecipe(output,
				fluid, inputs[0], inputs[1], inputs[2], null, ticks, euPerTick));
		else if (inputs.length == 2)
			RecipeHandler.addRecipe(new IndustrialGrinderRecipe(output,
				fluid, inputs[0], inputs[1], null, null, ticks, euPerTick));
		else if (inputs.length == 1)
			RecipeHandler.addRecipe(new IndustrialGrinderRecipe(output,
				fluid, inputs[0], null, null, null, ticks, euPerTick));
		else if (inputs.length == 4) {
			RecipeHandler.addRecipe(new IndustrialGrinderRecipe(output,
				fluid, inputs[0], inputs[1], inputs[2], inputs[3], ticks, euPerTick));
		} else {
			throw new InvalidParameterException("Invalid industrial grinder inputs: " + inputs);
		}
	}
}

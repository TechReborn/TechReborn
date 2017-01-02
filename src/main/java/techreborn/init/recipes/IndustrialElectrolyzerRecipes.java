package techreborn.init.recipes;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import reborncore.api.recipe.RecipeHandler;
import techreborn.api.recipe.machines.IndustrialElectrolyzerRecipe;
import techreborn.items.DynamicCell;
import techreborn.items.ItemCells;

import java.security.InvalidParameterException;

/**
 * Created by Prospector
 */
public class IndustrialElectrolyzerRecipes extends RecipeMethods {
	public static void init() {
		register(getMaterial("electrolyzedwater", 6, Type.CELL), 760, 120, getMaterial("hydrogen", 4, Type.CELL), getMaterial("compressedair", Type.CELL));
		register(getMaterial("water", Type.CELL), 20, 50, getMaterial("electrolyzedwater", Type.CELL));
		register(new ItemStack(Items.DYE, 3, 15), 20, 106, false, getMaterial("calcium", Type.CELL));
		register(new ItemStack(Items.SUGAR, 32), 200, 32, getMaterial("carbon", 2, Type.CELL), getMaterial("water", 5, Type.CELL));
		register(new ItemStack(Items.BLAZE_POWDER, 4), 300, 25, getMaterial("dark_ashes", Type.DUST), getMaterial("sulfur", Type.DUST));
		register(new ItemStack(Blocks.SAND, 16), 1000, 25, getMaterial("silicon", Type.CELL), getMaterial("compressedair", Type.CELL));
		register(getOre("dustClay", 8), 200, 50, getMaterial("lithium", Type.CELL), getMaterial("silicon", 2, Type.CELL), getMaterial("aluminum", 2, Type.DUST), getMaterial("sodium", 2, Type.CELL));
		register(getOre("dustCoal"), 40, 50, getMaterial("carbon", 2, Type.CELL));
		register(getOre("dustCharcoal"), 20, 50, getMaterial("carbon", Type.CELL));
		register(getOre("dustEnderPearl", 16), 1300, 50, getMaterial("nitrogen", 5, Type.CELL), getMaterial("berylium", 1, Type.CELL), getMaterial("potassium", 4, Type.CELL), getMaterial("chlorite", 6, Type.CELL));
		register(getOre("dustLazurite", 29), 1460, 100, getMaterial("aluminum", 3, Type.DUST), getMaterial("silicon", 3, Type.CELL), getMaterial("calcium", 3, Type.CELL), getMaterial("sodium", 4, Type.CELL));
		register(getOre("dustPyrite", 3), 120, 128, getMaterial("iron", Type.DUST), getMaterial("sulfur", 2, Type.DUST));
		register(getOre("dustCalcite", 10), 700, 80, getMaterial("calcium", 2, Type.CELL), getMaterial("carbon", 2, Type.CELL), getMaterial("compressedair", 3, Type.CELL));
		register(getOre("dustSodalite", 23), 1340, 90, getMaterial("sodium", 4, Type.CELL), getMaterial("aluminum", 3, Type.DUST), getMaterial("silicon", 3, Type.CELL), getMaterial("chlorite", Type.CELL));
		register(getOre("dustFlint", 8), 1000, 5, getMaterial("silicon", Type.CELL), getMaterial("compressedair", Type.CELL));
		register(getOre("dustSaltpeter", 10), 40, 110, getMaterial("potassium", 2, Type.CELL), getMaterial("nitrogen", 2, Type.CELL), getMaterial("compressedair", 3, Type.CELL));
		register(getOre("dustCinnabar", 2), 100, 128, getMaterial("mercury", Type.CELL), getMaterial("sulfur", Type.DUST));
		register(getOre("dustSphalerite", 2), 140, 100, getMaterial("zinc", Type.DUST), getMaterial("sulfur", Type.DUST));
		register(getOre("dustBauxite", 12), 2000, 128, getMaterial("aluminum", 8, Type.DUST), getMaterial("titanium", 2, Type.SMALL_DUST), getMaterial("hydrogen", 5, Type.CELL), getMaterial("compressedair", 3, Type.CELL));
		register(getOre("dustTungsten"), 20, 50, getMaterial("wolframium", Type.CELL));
		register(getOre("dustRuby", 9), 500, 50, getMaterial("aluminum", 2, Type.DUST), getMaterial("chrome", Type.DUST), getMaterial("compressedair", 3, Type.CELL));
		register(getOre("dustSapphire", 8), 400, 50, getMaterial("aluminum", 2, Type.DUST), getMaterial("compressedair", 3, Type.CELL));
		register(getOre("dustEmerald", 29), 600, 50, getMaterial("aluminum", 2, Type.DUST), getMaterial("berylium", 3, Type.CELL), getMaterial("silicon", 6, Type.CELL), getMaterial("compressedair", 9, Type.CELL));
		register(getOre("dustPeridot", 9), 600, 60, getMaterial("magnesium", 2, Type.DUST), getMaterial("iron", 2, Type.DUST), getMaterial("silicon", Type.CELL), getMaterial("compressedair", 2, Type.CELL));
		register(getOre("dustGalena", 2), 1000, 120, getMaterial("silver", 3, Type.SMALL_DUST), getMaterial("lead", 3, Type.SMALL_DUST), getMaterial("sulfur", 2, Type.SMALL_DUST));
		register(getOre("dustObsidian", 4), 500, 5, getMaterial("magnesium", 2, Type.SMALL_DUST), getMaterial("iron", 2, Type.SMALL_DUST), getMaterial("silicon", Type.CELL), getMaterial("compressedair", 2, Type.CELL));
		register(getOre("dustPyrope", 20), 1780, 50, getMaterial("magnesium", 3, Type.DUST), getMaterial("aluminum", 2, Type.DUST), getMaterial("silicon", 3, Type.CELL), getMaterial("compressedair", 6, Type.CELL));
		register(getOre("dustAlmandine", 20), 1640, 50, getMaterial("iron", 3, Type.DUST), getMaterial("aluminum", 2, Type.DUST), getMaterial("silicon", 3, Type.CELL), getMaterial("compressedair", 6, Type.CELL));
		register(getOre("dustSpessartine", 20), 1800, 50, getMaterial("aluminum", 2, Type.DUST), getMaterial("manganese", 3, Type.DUST), getMaterial("silicon", 3, Type.CELL), getMaterial("compressedair", 6, Type.CELL));
		register(getOre("dustAndradite", 20), 1280, 50, getMaterial("calcium", 3, Type.CELL), getMaterial("iron", 2, Type.DUST), getMaterial("silicon", 3, Type.CELL), getMaterial("compressedair", 6, Type.CELL));
		register(getOre("dustGrossular", 20), 204, 50, getMaterial("calcium", 3, Type.CELL), getMaterial("aluminum", 2, Type.DUST), getMaterial("silicon", 3, Type.CELL), getMaterial("compressedair", 6, Type.CELL));
		register(getOre("dustUvarovite", 20), 2200, 50, getMaterial("calcium", 3, Type.CELL), getMaterial("chrome", 2, Type.DUST), getMaterial("silicon", 3, Type.CELL), getMaterial("compressedair", 6, Type.CELL));
		register(getOre("dustAshes", 2), 20, 50, getMaterial("carbon", Type.CELL));
		register(ItemCells.getCellByName("methane", 5), 140, 50, getMaterial("hydrogen", 4, Type.CELL), getMaterial("carbon", Type.CELL));
		register(ItemCells.getCellByName("sulfuricacid", 7), 40, 100, getMaterial("hydrogen", 2, Type.CELL), getMaterial("sulfur", Type.CELL), getMaterial("compressedair", 2, Type.CELL));
	}

	static void register(ItemStack input, int ticks, int euPerTick, boolean oreDict, ItemStack... outputs) {
		ItemStack output1;
		ItemStack output2 = null;
		ItemStack output3 = null;
		ItemStack output4 = null;

		if (outputs.length == 3) {
			output1 = outputs[0];
			output2 = outputs[1];
			output3 = outputs[2];
		} else if (outputs.length == 2) {
			output1 = outputs[0];
			output2 = outputs[1];
		} else if (outputs.length == 1) {
			output1 = outputs[0];
		} else if (outputs.length == 4) {
			output1 = outputs[0];
			output2 = outputs[1];
			output3 = outputs[2];
			output4 = outputs[3];
		} else {
			throw new InvalidParameterException("Invalid industrial electrolyzer outputs: " + outputs);
		}

		int cellCount = 0;
		for (ItemStack stack : outputs) {
			if (stack.getItem() instanceof DynamicCell) {
				cellCount += stack.getCount();
			}

		}

		if (input.getItem() instanceof DynamicCell) {
			int inputCount = input.getCount();
			if (cellCount < inputCount) {
				if (output2 != null) {
					output2 = DynamicCell.getEmptyCell(inputCount - cellCount);
				} else if (output3 != null) {
					output3 = DynamicCell.getEmptyCell(inputCount - cellCount);
				} else if (output4 != null) {
					output4 = DynamicCell.getEmptyCell(inputCount - cellCount);
				}
			}
			cellCount -= inputCount;
		}

		if (cellCount < 0) {
			cellCount = 0;
		}

		ItemStack cells = null;
		if (cellCount > 0) {
			if (cellCount > 64) {
				throw new InvalidParameterException("Invalid industrial electrolyzer outputs: " + outputs + "(Recipe requires > 64 cells)");
			}
			cells = DynamicCell.getEmptyCell(cellCount);
		}
		RecipeHandler.addRecipe(new IndustrialElectrolyzerRecipe(input, cells, output1, output2, output3, output4, ticks, euPerTick, oreDict));
	}

	static void register(ItemStack input, int ticks, int euPerTick, ItemStack... outputs) {
		register(input, ticks, euPerTick, true, outputs);
	}
}

package techreborn.init.recipes;

import net.minecraft.item.ItemStack;
import reborncore.api.recipe.RecipeHandler;
import techreborn.api.recipe.machines.IndustrialElectrolyzerRecipe;
import techreborn.items.DynamicCell;

import java.security.InvalidParameterException;

/**
 * Created by Prospector
 */
public class IndustrialElectrolyzerRecipes extends RecipeMethods {
	public static void init() {
		register(getOre("dustBauxite", 12), 100, 128, getMaterial("aluminum", 8, Type.DUST), getMaterial("titanium", 2, Type.SMALL_DUST), getMaterial("hydrogen", 5, Type.CELL));
	}

	static void register(ItemStack input, int ticks, int euPerTick, ItemStack... outputs) {
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

		ItemStack cells = null;
		if (cellCount > 0) {
			if (cellCount > 64) {
				throw new InvalidParameterException("Invalid industrial electrolyzer outputs: " + outputs + "(Recipe requires > 64 cells)");
			}
			cells = DynamicCell.getEmptyCell(cellCount);
		}
		RecipeHandler.addRecipe(new IndustrialElectrolyzerRecipe(input, cells, output1, output2, output3, output4, ticks, euPerTick));
	}
}

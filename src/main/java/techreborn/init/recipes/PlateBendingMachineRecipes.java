package techreborn.init.recipes;

import net.minecraft.item.ItemStack;
import reborncore.api.recipe.RecipeHandler;
import reborncore.common.util.OreUtil;
import techreborn.api.recipe.machines.PlateBendingMachineRecipe;
import techreborn.items.ingredients.ItemIngots;
import techreborn.items.ingredients.ItemPlates;

import java.security.InvalidParameterException;

public class PlateBendingMachineRecipes extends RecipeMethods {

	public static void init() {
		// Advanced Alloy
		register(ItemIngots.getIngotByName("advanced_alloy"), ItemPlates.getPlateByName("advanced_alloy"), 100, 8);

		// Refined Iron
		register("ingotRefinedIron", ItemPlates.getPlateByName("RefinedIron")); // Refined Iron

		// Ingot -> Plate
		for (String entry : OreUtil.oreNames) {
			if (entry.equals("iridium")) continue;

			if (!OreUtil.hasPlate(entry)) continue;

			ItemStack plate;

			try {
				plate = ItemPlates.getPlateByName(entry, 1);
			} catch (InvalidParameterException exception) {
				plate = OreUtil.getStackFromName("plate" + OreUtil.capitalizeFirstLetter(entry));
			}

			if (plate.isEmpty()) continue;

			if (OreUtil.hasIngot(entry))
				register(OreUtil.getStackFromName("ingot" + OreUtil.capitalizeFirstLetter(entry)), plate);
		}
	}

	static void register(Object input, ItemStack output) {
		register(input,  output, 40, 25);
	}

	static void register(Object input, ItemStack output, int tickTime, int euPerTick) {
		RecipeHandler.addRecipe(new PlateBendingMachineRecipe(input, output, tickTime, euPerTick));
	}
}

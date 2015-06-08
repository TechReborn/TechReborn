package techreborn.compat.recipes;

import buildcraft.BuildCraftBuilders;
import ic2.api.item.IC2Items;
import net.minecraft.item.ItemStack;
import techreborn.util.CraftingHelper;
import techreborn.util.RecipeRemover;

public class RecipesBuildcraft {

	public static void init()
	{
		removeRecipes();
		addRecipies();
	}

	public static void removeRecipes()
	{
		RecipeRemover.removeAnyRecipe(new ItemStack(
				BuildCraftBuilders.quarryBlock));
	}

	public static void addRecipies()
	{
		//Quarry
		CraftingHelper.addShapedOreRecipe(
				new ItemStack (BuildCraftBuilders.quarryBlock),
				new Object[]
				{ 
					"IAI", "GIG", "DED",
					'I', "gearIron", 
					'G', "gearGold",
					'D', "gearDiamond",
					'A', IC2Items.getItem("advancedCircuit"),
					'E', IC2Items.getItem("diamondDrill") 
				}
	       );
	}

}

package techreborn.compat.recipes;

import buildcraft.builders.BlockQuarry;
import buildcraft.core.Version;
import cpw.mods.fml.common.Loader;
import ic2.api.item.IC2Items;
import net.minecraft.item.ItemStack;
import techreborn.util.CraftingHelper;
import techreborn.util.LogHelper;
import techreborn.util.RecipeRemover;

public class RecipesBuildcraft {

	public static BlockQuarry quarryBlock;

	public static void init() {
		try {
			String itemClass = "buildcraft.BuildCraftBuilders";
			if (!Version.getVersion().startsWith("7")) {//Buildcraft 6
				if (Loader.isModLoaded("BuildCraft|Factory")) {
					itemClass = "buildcraft.BuildCraftFactory";
				}
			} else if (!Loader.isModLoaded("Buildcraft|Builders")) { //Buildcraft 7
				return;
			}
			Object obj = Class.forName(itemClass).getField("quarryBlock").get(null);
			if (obj instanceof BlockQuarry) {
				quarryBlock = (BlockQuarry) obj;
			}
		} catch (Exception ex) {
			LogHelper.fatal("Could not retrieve quarry block from Buildcraft! This is a fatal error!");
			ex.printStackTrace();
		}
		removeRecipes();
		addRecipies();
	}

	public static void removeRecipes() {
		RecipeRemover.removeAnyRecipe(new ItemStack(
				quarryBlock));
	}

	public static void addRecipies() {
		//Quarry
		CraftingHelper.addShapedOreRecipe(
				new ItemStack(quarryBlock),
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

package techreborn.compat.recipes;

import buildcraft.core.Version;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import ic2.api.item.IC2Items;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import techreborn.compat.ICompatModule;
import techreborn.util.CraftingHelper;
import techreborn.util.LogHelper;
import techreborn.util.RecipeRemover;

public class RecipesBuildcraft implements ICompatModule {

	public static Block quarryBlock;

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

	@Override
	public void preInit(FMLPreInitializationEvent event) {

	}

	@Override
	public void init(FMLInitializationEvent event) {

	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		LogHelper.info("Trying to change the quarry recipe");
		try {
			String itemClass = "buildcraft.BuildCraftBuilders";
			if (!Version.getVersion().startsWith("7")) {//Buildcraft 6
				if (Loader.isModLoaded("BuildCraft|Factory")) {
					itemClass = "buildcraft.BuildCraftFactory";
				}
			} else if (!Version.getVersion().startsWith("7") && !Loader.isModLoaded("BuildCraft|Builders")) { //Buildcraft 7
				LogHelper.info("Buildcraft not found");
				return;
			}
			Object obj = Class.forName(itemClass).getField("quarryBlock").get(null);
			if (obj instanceof Block) {
				quarryBlock = (Block) obj;
				LogHelper.info("Found Quarry Block from buildcraft at " + itemClass + ":quarryBlock");
			} else {
				LogHelper.fatal("Could not retrieve quarry block from Buildcraft! This is a fatal error!");
				return;
			}
		} catch (Exception ex) {
			LogHelper.fatal("Could not retrieve quarry block from Buildcraft! This is a fatal error!");
			ex.printStackTrace();
			return;
		}
		removeRecipes();
		addRecipies();
	}

	@Override
	public void serverStarting(FMLServerStartingEvent event) {

	}
}

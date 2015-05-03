package techreborn.compat.recipes;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import ic2.api.item.IC2Items;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;
import techreborn.init.ModItems;
import techreborn.util.CraftingHelper;
import techreborn.util.LogHelper;
import techreborn.util.RecipeRemover;

public class RecipesIC2 {
	public static ConfigTechReborn config;
	
	public static void init()
	{
		removeIc2Recipes();
		addShappedIc2Recipes();
	}
	
	public static void removeIc2Recipes()
	{
		if (config.ExpensiveMacerator);
		RecipeRemover.removeAnyRecipe(IC2Items.getItem("macerator"));
		if (config.ExpensiveDrill);
		RecipeRemover.removeAnyRecipe(IC2Items.getItem("miningDrill"));
		if (config.ExpensiveDiamondDrill);
		RecipeRemover.removeAnyRecipe(IC2Items.getItem("diamondDrill"));
		if (config.ExpensiveSolar);
		RecipeRemover.removeAnyRecipe(IC2Items.getItem("solarPanel"));

		LogHelper.info("IC2 Recipes Removed");
	}
	
	public static void addShappedIc2Recipes()
	{
		if (config.ExpensiveMacerator);
		CraftingHelper.addShapedOreRecipe(IC2Items.getItem("macerator"),
				new Object[]
				{ "FDF", "DMD", "FCF", 
					'F', Items.flint, 
					'D', Items.diamond,
					'M', IC2Items.getItem("machine"), 
					'C',IC2Items.getItem("electronicCircuit") });
		
		if (config.ExpensiveDrill);
		CraftingHelper.addShapedOreRecipe(IC2Items.getItem("miningDrill"),
				new Object[]
				{ " S ", "SCS", "SBS", 
					'S', "ingotSteel", 
					'B',IC2Items.getItem("reBattery"), 
					'C',IC2Items.getItem("electronicCircuit") });
		
		if (config.ExpensiveDiamondDrill);
		CraftingHelper.addShapedOreRecipe(IC2Items.getItem("diamondDrill"),
				new Object[]
				{ " D ", "DBD", "TCT", 
					'D', "gemDiamond", 
					'T', "ingotTitanium",
					'B', IC2Items.getItem("miningDrill"), 
					'C',IC2Items.getItem("advancedCircuit") });
		
		if (config.ExpensiveSolar);
		CraftingHelper.addShapedOreRecipe(IC2Items.getItem("solarPanel"),
				new Object[]
				{ "PPP", "SZS", "CGC", 
					'P', "paneGlass", 
					'S', new ItemStack(ModItems.parts, 1, 1), 
					'Z', IC2Items.getItem("carbonPlate"), 
					'G', IC2Items.getItem("generator"), 
					'C',IC2Items.getItem("electronicCircuit") });

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.thermalGenerator),
				new Object[]
				{ "III", "IHI", "CGC", 
					'I', "ingotInvar", 
					'H', IC2Items.getItem("reinforcedGlass"), 
					'C', IC2Items.getItem("electronicCircuit"), 
					'G', IC2Items.getItem("geothermalGenerator") });
		
		LogHelper.info("Added Expensive IC2 Recipes");
	}
	
	public static void addTechreboenRecipes()
	{
		CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.parts, 4, 6),
				new Object[]
				{ "EEE", "EAE", "EEE", 
					'E', "gemEmerald", 
					'A', IC2Items.getItem("electronicCircuit") });

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.parts, 1, 7),
				new Object[]
				{ "AGA", "RPB", "ASA", 
					'A', "ingotAluminium", 
					'G', "dyeGreen",
					'R', "dyeRed", 
					'P', "paneGlass", 
					'B', "dyeBlue", 
					'S',Items.glowstone_dust, });

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.parts, 4, 8),
				new Object[]
				{ "DSD", "S S", "DSD", 
					'D', "dustDiamond", 
					'S', "ingotSteel" });

		CraftingHelper.addShapedOreRecipe(
				new ItemStack(ModItems.parts, 16, 13),
				new Object[]
				{ "CSC", "SCS", "CSC", 
					'S', "ingotSteel", 
					'C',IC2Items.getItem("electronicCircuit") });

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.parts, 2, 14),
				new Object[]
				{ "TST", "SBS", "TST", 
					'S', "ingotSteel", 
					'T', "ingotTungsten",
					'B', "blockSteel" });

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.parts, 1, 15),
				new Object[]
				{ "AAA", "AMA", "AAA", 
					'A', "ingotAluminium", 
					'M', new ItemStack(ModItems.parts, 1, 13) });

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.parts, 1, 16),
				new Object[]
				{ "AAA", "AMA", "AAA", 
					'A', "ingotBronze", 
					'M', new ItemStack(ModItems.parts, 1, 13) });

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.parts, 1, 17),
				new Object[]
				{ "AAA", "AMA", "AAA", 
					'A', "ingotSteel",
					'M', new ItemStack(ModItems.parts, 1, 13) });

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.parts, 1, 18),
				new Object[]
				{ "AAA", "AMA", "AAA", 
					'A', "ingotTitanium", 
					'M', new ItemStack(ModItems.parts, 1, 13) });

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.parts, 1, 19),
				new Object[]
				{ "AAA", "AMA", "AAA", 
					'A', "ingotBrass", 
					'M', new ItemStack(ModItems.parts, 1, 13) });
	}

}

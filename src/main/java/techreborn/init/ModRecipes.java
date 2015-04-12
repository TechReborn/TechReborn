package techreborn.init;

import ic2.api.item.IC2Items;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import techreborn.api.CentrifugeRecipie;
import techreborn.api.TechRebornAPI;
import techreborn.util.CraftingHelper;
import techreborn.util.LogHelper;
import techreborn.util.RecipeRemover;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModRecipes {
		
	public static void init()
	{
		removeIc2Recipes();
		addShaplessRecipes();
		addShappedRecipes();
		addSmeltingRecipes();
		addMachineRecipes();
	}
	
	public static void removeIc2Recipes()
	{
		RecipeRemover.removeAnyRecipe(IC2Items.getItem("macerator"));
		RecipeRemover.removeAnyRecipe(IC2Items.getItem("miningDrill"));
		RecipeRemover.removeAnyRecipe(IC2Items.getItem("diamondDrill"));
		RecipeRemover.removeAnyRecipe(IC2Items.getItem("solarPanel"));



		LogHelper.info("IC2 Recipes Removed");
	}
	
	public static void addShappedRecipes()
	{
		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.thermalGenerator),
				new Object[]{"III", "IHI", "CGC", 
				'I', "ingotInvar", 
				'H', IC2Items.getItem("reinforcedGlass"),
				'C', IC2Items.getItem("electronicCircuit"), 
				'G', IC2Items.getItem("geothermalGenerator")});
		
		CraftingHelper.addShapedOreRecipe(IC2Items.getItem("macerator"),
				new Object[]{"FDF", "DMD", "FCF", 
				'F', Items.flint, 
				'D', Items.diamond,
				'M', IC2Items.getItem("machine"), 
				'C', IC2Items.getItem("electronicCircuit")});
		
		CraftingHelper.addShapedOreRecipe(IC2Items.getItem("miningDrill"),
				new Object[]{" S ", "SCS", "SBS", 
				'S', "ingotSteel", 
				'B', IC2Items.getItem("reBattery"), 
				'C', IC2Items.getItem("electronicCircuit")});
		
		CraftingHelper.addShapedOreRecipe(IC2Items.getItem("diamondDrill"),
				new Object[]{" D ", "DBD", "TCT", 
				'D', "gemDiamond",
				'T', "ingotTitanium",
				'B', IC2Items.getItem("miningDrill"), 
				'C', IC2Items.getItem("advancedCircuit")});
		
		CraftingHelper.addShapedOreRecipe(IC2Items.getItem("solarPanel"),
				new Object[]{"PPP", "SZS", "CGC", 
				'P', "paneGlass",
				'S', new ItemStack(ModItems.parts,1,1),
				'Z', IC2Items.getItem("carbonPlate"),
				'G', IC2Items.getItem("generator"),
				'C', IC2Items.getItem("electronicCircuit")});
		
		CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.parts,4,6),
				new Object[]{"EEE", "EAE", "EEE", 
				'E', "gemEmerald",
				'A', IC2Items.getItem("electronicCircuit")});
		
		CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.parts,1,7),
				new Object[]{"AGA", "RPB", "ASA", 
				'A', "ingotAluminium",
				'G', "dyeGreen",
				'R', "dyeRed",
				'P', "paneGlass",
				'B', "dyeBlue",
				'S', Items.glowstone_dust,});
		
		CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.parts,4,8),
				new Object[]{"DSD", "S S", "DSD", 
				'D', "dustDiamond",
				'S', "ingotSteel"});
		
		CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.parts,16,13),
				new Object[]{"CSC", "SCS", "CSC", 
				'S', "ingotSteel",
				'C', IC2Items.getItem("electronicCircuit")});
		
		CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.parts,2,14),
				new Object[]{"TST", "SBS", "TST", 
				'S', "ingotSteel",
				'T', "ingotTungsten",
				'C', "blockSteel"});
		
		CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.parts,1,15),
				new Object[]{"AAA", "AMA", "AAA", 
				'A', "ingotAluminium",
				'M', new ItemStack(ModItems.parts,1,13)});
		
		CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.parts,1,16),
				new Object[]{"AAA", "AMA", "AAA", 
				'A', "ingotBronze",
				'M', new ItemStack(ModItems.parts,1,13)});
		
		CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.parts,1,17),
				new Object[]{"AAA", "AMA", "AAA", 
				'A', "ingotSteel",
				'M', new ItemStack(ModItems.parts,1,13)});
		
		CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.parts,1,18),
				new Object[]{"AAA", "AMA", "AAA", 
				'A', "ingotTitanium",
				'M', new ItemStack(ModItems.parts,1,13)});
		
		CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.parts,1,19),
				new Object[]{"AAA", "AMA", "AAA", 
				'A', "ingotBrass",
				'M', new ItemStack(ModItems.parts,1,13)});
		
		LogHelper.info("Shapped Recipes Added");
	}
	
	public static void addShaplessRecipes()
	{
		LogHelper.info("Shapless Recipes Added");
	}
	
	public static void addSmeltingRecipes()
	{
		GameRegistry.addSmelting(new ItemStack(ModItems.dusts,1 ,27), new ItemStack(Items.iron_ingot), 1F);
		GameRegistry.addSmelting(new ItemStack(ModItems.dusts,1 ,23), new ItemStack(Items.gold_ingot), 1F);
		GameRegistry.addSmelting(new ItemStack(ModItems.dusts,1 ,14), IC2Items.getItem("copperIngot"), 1F);
		GameRegistry.addSmelting(new ItemStack(ModItems.dusts,1 ,51), IC2Items.getItem("tinIngot"), 1F);
		GameRegistry.addSmelting(new ItemStack(ModItems.dusts,1 ,7), IC2Items.getItem("bronzeIngot"), 1F);
		GameRegistry.addSmelting(new ItemStack(ModItems.dusts,1 ,29), IC2Items.getItem("leadIngot"), 1F);
		GameRegistry.addSmelting(new ItemStack(ModItems.dusts,1 ,45), IC2Items.getItem("silverIngot"), 1F);

		LogHelper.info("Smelting Recipes Added");
	}
	
	public static void addMachineRecipes()
	{
		TechRebornAPI.registerCentrifugeRecipe(new CentrifugeRecipie(Items.apple, 4, Items.beef, Items.baked_potato, null, null, 120));
		LogHelper.info("Machine Recipes Added");
	}

}

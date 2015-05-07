package techreborn.compat.recipes;

import cpw.mods.fml.common.Mod;
import net.minecraft.init.Blocks;
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
                            'I', "plateInvar",
                            'H', IC2Items.getItem("reinforcedGlass"),
                            'C', "circuitBasic",
                            'G', IC2Items.getItem("geothermalGenerator") });

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.thermalGenerator),
                new Object[]
                        { "AAA", "AHA", "CGC",
                                'A', "plateAluminum",
                                'H', IC2Items.getItem("reinforcedGlass"),
                                'C', "circuitBasic",
                                'G', IC2Items.getItem("geothermalGenerator") });

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Semifluidgenerator),
                new Object[]
                        { "III", "IHI", "CGC",
                                'I', "plateIron",
                                'H', IC2Items.getItem("reinforcedGlass"),
                                'C', "circuitBasic",
                                'G', IC2Items.getItem("generator") });

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Semifluidgenerator),
                new Object[]
                        { "AAA", "AHA", "CGC",
                                'A', "plateAluminum",
                                'H', IC2Items.getItem("reinforcedGlass"),
                                'C', "circuitBasic",
                                'G', IC2Items.getItem("generator") });

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.DieselGenerator),
                new Object[]
                        { "III", "I I", "CGC",
                                'I', "plateIron",
                                'C', "circuitBasic",
                                'G', IC2Items.getItem("generator") });

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.DieselGenerator),
                new Object[]
                        { "AAA", "A A", "CGC",
                                'A', "plateAluminum",
                                'C', "circuitBasic",
                                'G', IC2Items.getItem("generator") });

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.MagicalAbsorber),
                new Object[]
                        { "CSC", "IBI", "CAC",
                                'C', "circuitMaster",
                                'S', "craftingSuperconductor",
                                'B', Blocks.beacon,
                                'A', ModBlocks.Magicenergeyconverter,
                                'I', IC2Items.getItem("iridiumPlate") });

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Magicenergeyconverter),
                new Object[]
                        { "CTC", "PBP", "CLC",
                                'C', "circuitAdvanced",
                                'P', "platePlatinum",
                                'B', Blocks.beacon,
                                'L', IC2Items.getItem("lapotronCrystal"),
                                'T', IC2Items.getItem("teleporter") });

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Dragoneggenergysiphoner),
                new Object[]
                        { "CTC", "ISI", "CBC",
                                'I', IC2Items.getItem("iridiumPlate"),
                                'C', "circuitMaster",
                                'B', "batteryUltimate",
                                'S', ModBlocks.Supercondensator,
                                'T', IC2Items.getItem("teleporter") });

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.centrifuge),
                new Object[]
                        { "SCS", "BEB", "SCS",
                                'S', "plateSteel",
                                'C', "circuitAdvanced",
                                'B', IC2Items.getItem("advancedMachine"),
                                'E', IC2Items.getItem("extractor") });

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.IndustrialElectrolyzer),
                new Object[]
                        { "SXS", "CEC", "SMS",
                                'S', "plateSteel",
                                'C', "circuitAdvanced",
                                'X', IC2Items.getItem("extractor"),
                                'E', IC2Items.getItem("electrolyzer"),
                                'M', IC2Items.getItem("magnetizer") });

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.BlastFurnace),
                new Object[]
                        { "CHC", "HBH", "FHF",
                                'H', new ItemStack(ModItems.parts, 1, 17),
                                'C', "circuitAdvanced",
                                'B', IC2Items.getItem("advancedMachine"),
                                'F', IC2Items.getItem("inductionFurnace") });

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Grinder),
                new Object[]
                        { "ECP", "GGG", "CBC",
                                'E', ModBlocks.IndustrialElectrolyzer,
                                'P', IC2Items.getItem("pump"),
                                'C', "circuitAdvanced",
                                'B', IC2Items.getItem("advancedMachine"),
                                'G', "craftingGrinder" });

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.ImplosionCompressor),
                new Object[]
                        { "ABA", "CPC", "ABA",
                                'A', IC2Items.getItem("advancedAlloy"),
                                'C', "circuitAdvanced",
                                'B', IC2Items.getItem("advancedMachine"),
                                'P', IC2Items.getItem("compressor") });

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.VacuumFreezer),
                new Object[]
                        { "SPS", "CGC", "SPS",
                                'S', "plateSteel",
                                'C', "circuitAdvanced",
                                'G', IC2Items.getItem("reinforcedGlass"),
                                'P', IC2Items.getItem("pump") });

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Distillationtower),
                new Object[]
                        { "CMC", "PBP", "EME",
                                'E', ModBlocks.IndustrialElectrolyzer,
                                'M', "circuitMaster",
                                'B', IC2Items.getItem("advancedMachine"),
                                'C', ModBlocks.centrifuge,
                                'P', IC2Items.getItem("pump") });

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.AlloyFurnace),
                new Object[]
                        { "III", "F F", "III",
                                'I', "plateIron",
                                'F', IC2Items.getItem("ironFurnace") });

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.AlloySmelter),
                new Object[]
                        { "IHI", "CFC", "IHI",
                                'I', "plateInvar",
                                'C', "circuitBasic",
                                'H', new ItemStack(ModItems.parts, 1, 17),
                                'F', ModBlocks.AlloyFurnace });

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.AssemblyMachine),
                new Object[]
                        { "CPC", "SBS", "CSC",
                                'S', "plateSteel",
                                'C', "circuitBasic",
                                'B', IC2Items.getItem("machine"),
                                'P', "craftingPiston" });

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.ChemicalReactor),
                new Object[]
                        { "IMI", "CPC", "IEI",
                                'I', "plateInvar",
                                'C', "circuitAdvanced",
                                'M', IC2Items.getItem("magnetizer"),
                                'P', IC2Items.getItem("compressor"),
                                'E', IC2Items.getItem("extractor") });

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.ChemicalReactor),
                new Object[]
                        { "AMA", "CPC", "AEA",
                                'A', "plateAluminum",
                                'C', "circuitAdvanced",
                                'M', IC2Items.getItem("magnetizer"),
                                'P', IC2Items.getItem("compressor"),
                                'E', IC2Items.getItem("extractor") });

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.lathe),
                new Object[]
                        { "SLS", "GBG", "SCS",
                                'S', "plateSteel",
                                'C', "circuitAdvanced",
                                'G', "gearSteel",
                                'B', IC2Items.getItem("advancedMachine"),
                                'L', IC2Items.getItem("LathingTool") });

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.platecuttingmachine),
                new Object[]
                        { "SCS", "GDG", "SBS",
                                'S', "plateSteel",
                                'C', "circuitAdvanced",
                                'G', "gearSteel",
                                'B', IC2Items.getItem("advancedMachine"),
                                'D', new ItemStack(ModItems.parts, 1, 9) });

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.RollingMachine),
                new Object[]
                        { "PCP", "MBM", "PCP",
                                'P', "craftingPiston",
                                'C', "circuitAdvanced",
                                'M', IC2Items.getItem("compressor"),
                                'B', IC2Items.getItem("machine") });

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.ElectricCraftingTable),
                new Object[]
                        { "ITI", "IBI", "ICI",
                                'I', "plateIron",
                                'C', "circuitAdvanced",
                                'T', "crafterWood",
                                'B', IC2Items.getItem("machine") });

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.ElectricCraftingTable),
                new Object[]
                        { "ATA", "ABA", "ACA",
                                'A', "plateAluminum",
                                'C', "circuitAdvanced",
                                'T', "crafterWood",
                                'B', IC2Items.getItem("machine") });

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.ChunkLoader),
                new Object[]
                        { "SCS", "CMC", "SCS",
                                'S', "plateSteel",
                                'C', "circuitMaster",
                                'M', new ItemStack(ModItems.parts, 1, 39) });

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Lesu),
                new Object[]
                        { " L ", "CBC", " M ",
                                'L', IC2Items.getItem("lvTransformer"),
                                'C', "circuitAdvanced",
                                'M', IC2Items.getItem("mvTransformer"),
                                'B', ModBlocks.LesuStorage });

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.LesuStorage),
                new Object[]
                        { "LLL", "LCL", "LLL",
                                'L', "blockLapis",
                                'C', "circuitBasic" });

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Woodenshelf),
                new Object[]
                        { "WWW", "A A", "WWW",
                                'W', "plankWood",
                                'A', "plateAluminum" });

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Metalshelf),
                new Object[]
                        { "III", "A A", "III",
                                'I', "plateIron",
                                'A', "plateAluminum" });

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.HighAdvancedMachineBlock),
                new Object[]
                        { "CTC", "TBT", "CTC",
                                'C', "plateChrome",
                                'T', "plateTitanium",
                                'B', IC2Items.getItem("advancedMachine") });

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.MachineCasing, 4, 0),
                new Object[]
                        { "III", "CBC", "III",
                                'I', "plateIron",
                                'C', "circuitBasic",
                                'B', IC2Items.getItem("machine") });

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.MachineCasing, 4, 1),
                new Object[]
                        { "SSS", "CBC", "SSS",
                                'S', "plateSteel",
                                'C', "circuitAdvanced",
                                'B', IC2Items.getItem("advancedMachine") });

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.MachineCasing, 4, 2),
                new Object[]
                        { "HHH", "CBC", "HHH",
                                'H', "plateChrome",
                                'C', "circuitElite",
                                'B', ModBlocks.HighAdvancedMachineBlock });



		
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

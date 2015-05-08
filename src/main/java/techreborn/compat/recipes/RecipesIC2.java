package techreborn.compat.recipes;

import cpw.mods.fml.common.Mod;
import ic2.api.recipe.RecipeInputOreDict;
import ic2.api.recipe.Recipes;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import ic2.api.item.IC2Items;
import net.minecraft.nbt.NBTTagCompound;
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
        addTechRebornRecipes();
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
                        {"FDF", "DMD", "FCF",
                                'F', Items.flint,
                                'D', Items.diamond,
                                'M', IC2Items.getItem("machine"),
                                'C', IC2Items.getItem("electronicCircuit")});
		
		if (config.ExpensiveDrill);
		CraftingHelper.addShapedOreRecipe(IC2Items.getItem("miningDrill"),
                new Object[]
                        {" S ", "SCS", "SBS",
                                'S', "ingotSteel",
                                'B', IC2Items.getItem("reBattery"),
                                'C', IC2Items.getItem("electronicCircuit")});
		
		if (config.ExpensiveDiamondDrill);
		CraftingHelper.addShapedOreRecipe(IC2Items.getItem("diamondDrill"),
                new Object[]
                        {" D ", "DBD", "TCT",
                                'D', "gemDiamond",
                                'T', "ingotTitanium",
                                'B', IC2Items.getItem("miningDrill"),
                                'C', IC2Items.getItem("advancedCircuit")});
		
		if (config.ExpensiveSolar);
		CraftingHelper.addShapedOreRecipe(IC2Items.getItem("solarPanel"),
                new Object[]
                        {"PPP", "SZS", "CGC",
                                'P', "paneGlass",
                                'S', new ItemStack(ModItems.parts, 1, 1),
                                'Z', IC2Items.getItem("carbonPlate"),
                                'G', IC2Items.getItem("generator"),
                                'C', IC2Items.getItem("electronicCircuit")});

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.thermalGenerator),
                new Object[]
                        {"III", "IHI", "CGC",
                                'I', "plateInvar",
                                'H', IC2Items.getItem("reinforcedGlass"),
                                'C', "circuitBasic",
                                'G', IC2Items.getItem("geothermalGenerator")});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.thermalGenerator),
                new Object[]
                        {"AAA", "AHA", "CGC",
                                'A', "plateAluminum",
                                'H', IC2Items.getItem("reinforcedGlass"),
                                'C', "circuitBasic",
                                'G', IC2Items.getItem("geothermalGenerator")});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Semifluidgenerator),
                new Object[]
                        {"III", "IHI", "CGC",
                                'I', "plateIron",
                                'H', IC2Items.getItem("reinforcedGlass"),
                                'C', "circuitBasic",
                                'G', IC2Items.getItem("generator")});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Semifluidgenerator),
                new Object[]
                        {"AAA", "AHA", "CGC",
                                'A', "plateAluminum",
                                'H', IC2Items.getItem("reinforcedGlass"),
                                'C', "circuitBasic",
                                'G', IC2Items.getItem("generator")});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.DieselGenerator),
                new Object[]
                        {"III", "I I", "CGC",
                                'I', "plateIron",
                                'C', "circuitBasic",
                                'G', IC2Items.getItem("generator")});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.DieselGenerator),
                new Object[]
                        {"AAA", "A A", "CGC",
                                'A', "plateAluminum",
                                'C', "circuitBasic",
                                'G', IC2Items.getItem("generator")});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.MagicalAbsorber),
                new Object[]
                        {"CSC", "IBI", "CAC",
                                'C', "circuitMaster",
                                'S', "craftingSuperconductor",
                                'B', Blocks.beacon,
                                'A', ModBlocks.Magicenergeyconverter,
                                'I', IC2Items.getItem("iridiumPlate")});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Magicenergeyconverter),
                new Object[]
                        {"CTC", "PBP", "CLC",
                                'C', "circuitAdvanced",
                                'P', "platePlatinum",
                                'B', Blocks.beacon,
                                'L', IC2Items.getItem("lapotronCrystal"),
                                'T', IC2Items.getItem("teleporter")});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Dragoneggenergysiphoner),
                new Object[]
                        {"CTC", "ISI", "CBC",
                                'I', IC2Items.getItem("iridiumPlate"),
                                'C', "circuitMaster",
                                'B', "batteryUltimate",
                                'S', ModBlocks.Supercondensator,
                                'T', IC2Items.getItem("teleporter")});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.centrifuge),
                new Object[]
                        {"SCS", "BEB", "SCS",
                                'S', "plateSteel",
                                'C', "circuitAdvanced",
                                'B', IC2Items.getItem("advancedMachine"),
                                'E', IC2Items.getItem("extractor")});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.IndustrialElectrolyzer),
                new Object[]
                        {"SXS", "CEC", "SMS",
                                'S', "plateSteel",
                                'C', "circuitAdvanced",
                                'X', IC2Items.getItem("extractor"),
                                'E', IC2Items.getItem("electrolyzer"),
                                'M', IC2Items.getItem("magnetizer")});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.BlastFurnace),
                new Object[]
                        {"CHC", "HBH", "FHF",
                                'H', new ItemStack(ModItems.parts, 1, 17),
                                'C', "circuitAdvanced",
                                'B', IC2Items.getItem("advancedMachine"),
                                'F', IC2Items.getItem("inductionFurnace")});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Grinder),
                new Object[]
                        {"ECP", "GGG", "CBC",
                                'E', ModBlocks.IndustrialElectrolyzer,
                                'P', IC2Items.getItem("pump"),
                                'C', "circuitAdvanced",
                                'B', IC2Items.getItem("advancedMachine"),
                                'G', "craftingGrinder"});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.ImplosionCompressor),
                new Object[]
                        {"ABA", "CPC", "ABA",
                                'A', IC2Items.getItem("advancedAlloy"),
                                'C', "circuitAdvanced",
                                'B', IC2Items.getItem("advancedMachine"),
                                'P', IC2Items.getItem("compressor")});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.VacuumFreezer),
                new Object[]
                        {"SPS", "CGC", "SPS",
                                'S', "plateSteel",
                                'C', "circuitAdvanced",
                                'G', IC2Items.getItem("reinforcedGlass"),
                                'P', IC2Items.getItem("pump")});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Distillationtower),
                new Object[]
                        {"CMC", "PBP", "EME",
                                'E', ModBlocks.IndustrialElectrolyzer,
                                'M', "circuitMaster",
                                'B', IC2Items.getItem("advancedMachine"),
                                'C', ModBlocks.centrifuge,
                                'P', IC2Items.getItem("pump")});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.AlloyFurnace),
                new Object[]
                        {"III", "F F", "III",
                                'I', "plateIron",
                                'F', IC2Items.getItem("ironFurnace")});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.AlloySmelter),
                new Object[]
                        {"IHI", "CFC", "IHI",
                                'I', "plateInvar",
                                'C', "circuitBasic",
                                'H', new ItemStack(ModItems.parts, 1, 17),
                                'F', ModBlocks.AlloyFurnace});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.AssemblyMachine),
                new Object[]
                        {"CPC", "SBS", "CSC",
                                'S', "plateSteel",
                                'C', "circuitBasic",
                                'B', IC2Items.getItem("machine"),
                                'P', "craftingPiston"});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.ChemicalReactor),
                new Object[]
                        {"IMI", "CPC", "IEI",
                                'I', "plateInvar",
                                'C', "circuitAdvanced",
                                'M', IC2Items.getItem("magnetizer"),
                                'P', IC2Items.getItem("compressor"),
                                'E', IC2Items.getItem("extractor")});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.ChemicalReactor),
                new Object[]
                        {"AMA", "CPC", "AEA",
                                'A', "plateAluminum",
                                'C', "circuitAdvanced",
                                'M', IC2Items.getItem("magnetizer"),
                                'P', IC2Items.getItem("compressor"),
                                'E', IC2Items.getItem("extractor")});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.lathe),
                new Object[]
                        {"SLS", "GBG", "SCS",
                                'S', "plateSteel",
                                'C', "circuitAdvanced",
                                'G', "gearSteel",
                                'B', IC2Items.getItem("advancedMachine"),
                                'L', IC2Items.getItem("LathingTool")});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.platecuttingmachine),
                new Object[]
                        {"SCS", "GDG", "SBS",
                                'S', "plateSteel",
                                'C', "circuitAdvanced",
                                'G', "gearSteel",
                                'B', IC2Items.getItem("advancedMachine"),
                                'D', new ItemStack(ModItems.parts, 1, 9)});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.RollingMachine),
                new Object[]
                        {"PCP", "MBM", "PCP",
                                'P', "craftingPiston",
                                'C', "circuitAdvanced",
                                'M', IC2Items.getItem("compressor"),
                                'B', IC2Items.getItem("machine")});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.ElectricCraftingTable),
                new Object[]
                        {"ITI", "IBI", "ICI",
                                'I', "plateIron",
                                'C', "circuitAdvanced",
                                'T', "crafterWood",
                                'B', IC2Items.getItem("machine")});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.ElectricCraftingTable),
                new Object[]
                        {"ATA", "ABA", "ACA",
                                'A', "plateAluminum",
                                'C', "circuitAdvanced",
                                'T', "crafterWood",
                                'B', IC2Items.getItem("machine")});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.ChunkLoader),
                new Object[]
                        {"SCS", "CMC", "SCS",
                                'S', "plateSteel",
                                'C', "circuitMaster",
                                'M', new ItemStack(ModItems.parts, 1, 39)});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Lesu),
                new Object[]
                        {" L ", "CBC", " M ",
                                'L', IC2Items.getItem("lvTransformer"),
                                'C', "circuitAdvanced",
                                'M', IC2Items.getItem("mvTransformer"),
                                'B', ModBlocks.LesuStorage});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.LesuStorage),
                new Object[]
                        {"LLL", "LCL", "LLL",
                                'L', "blockLapis",
                                'C', "circuitBasic"});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Woodenshelf),
                new Object[]
                        {"WWW", "A A", "WWW",
                                'W', "plankWood",
                                'A', "plateAluminum"});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Metalshelf),
                new Object[]
                        {"III", "A A", "III",
                                'I', "plateIron",
                                'A', "plateAluminum"});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.HighAdvancedMachineBlock),
                new Object[]
                        {"CTC", "TBT", "CTC",
                                'C', "plateChrome",
                                'T', "plateTitanium",
                                'B', IC2Items.getItem("advancedMachine")});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.MachineCasing, 4, 0),
                new Object[]
                        {"III", "CBC", "III",
                                'I', "plateIron",
                                'C', "circuitBasic",
                                'B', IC2Items.getItem("machine")});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.MachineCasing, 4, 1),
                new Object[]
                        {"SSS", "CBC", "SSS",
                                'S', "plateSteel",
                                'C', "circuitAdvanced",
                                'B', IC2Items.getItem("advancedMachine")});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.MachineCasing, 4, 2),
                new Object[]
                        {"HHH", "CBC", "HHH",
                                'H', "plateChrome",
                                'C', "circuitElite",
                                'B', ModBlocks.HighAdvancedMachineBlock});



		
		LogHelper.info("Added Expensive IC2 Recipes");
	}
	
	public static void addTechRebornRecipes()
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
                        {"AAA", "AMA", "AAA",
                                'A', "ingotBrass",
                                'M', new ItemStack(ModItems.parts, 1, 13)});

        //Macerator
        Recipes.macerator.addRecipe(new RecipeInputOreDict("oreAluminm"), null, new ItemStack(ModItems.crushedOre, 2, 0));
        Recipes.macerator.addRecipe(new RecipeInputOreDict("oreArdite"), null, new ItemStack(ModItems.crushedOre, 2, 1));
        Recipes.macerator.addRecipe(new RecipeInputOreDict("oreBauxite"), null, new ItemStack(ModItems.crushedOre, 2, 2));
        Recipes.macerator.addRecipe(new RecipeInputOreDict("oreCadmium"), null, new ItemStack(ModItems.crushedOre, 2, 3));
        Recipes.macerator.addRecipe(new RecipeInputOreDict("oreCinnabar"), null, new ItemStack(ModItems.crushedOre, 2, 4));
        Recipes.macerator.addRecipe(new RecipeInputOreDict("oreCobalt"), null, new ItemStack(ModItems.crushedOre, 2, 5));
        Recipes.macerator.addRecipe(new RecipeInputOreDict("oreDarkIron"), null, new ItemStack(ModItems.crushedOre, 2, 6));
        Recipes.macerator.addRecipe(new RecipeInputOreDict("oreIndium"), null, new ItemStack(ModItems.crushedOre, 2, 7));
        Recipes.macerator.addRecipe(new RecipeInputOreDict("oreIridium"), null, new ItemStack(ModItems.crushedOre, 2, 8));
        Recipes.macerator.addRecipe(new RecipeInputOreDict("oreNickel"), null, new ItemStack(ModItems.crushedOre, 2, 9));
        Recipes.macerator.addRecipe(new RecipeInputOreDict("oreOsmium"), null, new ItemStack(ModItems.crushedOre, 2, 10));
        Recipes.macerator.addRecipe(new RecipeInputOreDict("orePlatinum"), null, new ItemStack(ModItems.crushedOre, 2, 11));
        Recipes.macerator.addRecipe(new RecipeInputOreDict("orePyrite"), null, new ItemStack(ModItems.crushedOre, 2, 12));
        Recipes.macerator.addRecipe(new RecipeInputOreDict("oreSphalerite"), null, new ItemStack(ModItems.crushedOre, 2, 13));
        Recipes.macerator.addRecipe(new RecipeInputOreDict("oreTetrahedrite"), null, new ItemStack(ModItems.crushedOre, 2, 14));
        Recipes.macerator.addRecipe(new RecipeInputOreDict("oreTungsten"), null, new ItemStack(ModItems.crushedOre, 2, 15));
        Recipes.macerator.addRecipe(new RecipeInputOreDict("oreGalena"), null, new ItemStack(ModItems.crushedOre, 2, 16));

        Recipes.macerator.addRecipe(new RecipeInputOreDict("oreRedstone"), null, new ItemStack(Items.redstone, 10));
        Recipes.macerator.addRecipe(new RecipeInputOreDict("oreLapis"), null, new ItemStack(ModItems.dusts, 12, 40));
        Recipes.macerator.addRecipe(new RecipeInputOreDict("oreDiamond"), null, new ItemStack(ModItems.dusts, 2, 25));
        Recipes.macerator.addRecipe(new RecipeInputOreDict("oreEmerald"), null, new ItemStack(ModItems.dusts, 2, 27));
        Recipes.macerator.addRecipe(new RecipeInputOreDict("oreRuby"), null, new ItemStack(ModItems.dusts, 2, 64));
        Recipes.macerator.addRecipe(new RecipeInputOreDict("oreSapphire"), null, new ItemStack(ModItems.dusts, 2, 66));
        Recipes.macerator.addRecipe(new RecipeInputOreDict("orePeridot"), null, new ItemStack(ModItems.dusts, 2, 56));
        Recipes.macerator.addRecipe(new RecipeInputOreDict("oreSulfur"), null, new ItemStack(ModItems.dusts, 8, 73));
        Recipes.macerator.addRecipe(new RecipeInputOreDict("oreSaltpeter"), null, new ItemStack(ModItems.dusts, 8, 65));
        Recipes.macerator.addRecipe(new RecipeInputOreDict("oreTeslatite"), null, new ItemStack(ModItems.dusts, 10, 75));
        Recipes.macerator.addRecipe(new RecipeInputOreDict("oreMithril"), null, new ItemStack(ModItems.dusts, 2, 50));
        Recipes.macerator.addRecipe(new RecipeInputOreDict("oreVinteum"), null, new ItemStack(ModItems.dusts, 2, 81));
        Recipes.macerator.addRecipe(new RecipeInputOreDict("limestone"), null, new ItemStack(ModItems.dusts, 1, 43));
        Recipes.macerator.addRecipe(new RecipeInputOreDict("stoneNetherrack"), null, new ItemStack(ModItems.dusts, 2, 51));
        Recipes.macerator.addRecipe(new RecipeInputOreDict("stoneEndstone"), null, new ItemStack(ModItems.dusts, 2, 51));
        Recipes.macerator.addRecipe(new RecipeInputOreDict("stoneRedrock"), null, new ItemStack(ModItems.dusts, 2, 63));
        Recipes.macerator.addRecipe(new RecipeInputOreDict("oreMagnetite"), null, new ItemStack(ModItems.dusts, 2, 46));
        Recipes.macerator.addRecipe(new RecipeInputOreDict("oreLodestone"), null, new ItemStack(ModItems.dusts, 2, 44));
        Recipes.macerator.addRecipe(new RecipeInputOreDict("oreTellurium"), null, new ItemStack(ModItems.dusts, 2, 74));
        Recipes.macerator.addRecipe(new RecipeInputOreDict("oreSilicon"), null, new ItemStack(ModItems.dusts, 2, 67));
        Recipes.macerator.addRecipe(new RecipeInputOreDict("oreVoidstone"), null, new ItemStack(ModItems.dusts, 2, 82));
        Recipes.macerator.addRecipe(new RecipeInputOreDict("oreCalcite"), null, new ItemStack(ModItems.dusts, 2, 14));
        Recipes.macerator.addRecipe(new RecipeInputOreDict("oreSodalite"), null, new ItemStack(ModItems.dusts, 12, 69));
        Recipes.macerator.addRecipe(new RecipeInputOreDict("oreGraphite"), null, new ItemStack(ModItems.dusts, 12, 33));

        //Ore Washing Plant
        NBTTagCompound liquidAmount = new NBTTagCompound();
        liquidAmount.setInteger("amount", 1000);
        Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedAluminum"), liquidAmount, new ItemStack(ModItems.purifiedCrushedOre, 1, 0), new ItemStack(ModItems.tinyDusts, 2, 2), IC2Items.getItem("stoneDust"));
        Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedArdite"), liquidAmount, new ItemStack(ModItems.purifiedCrushedOre, 1, 1), new ItemStack(ModItems.tinyDusts, 2, 6), IC2Items.getItem("stoneDust"));
        Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedBauxite"), liquidAmount, new ItemStack(ModItems.purifiedCrushedOre, 1, 2), new ItemStack(ModItems.tinyDusts, 2, 9), IC2Items.getItem("stoneDust"));
        Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedCadmium"), liquidAmount, new ItemStack(ModItems.purifiedCrushedOre, 1, 3), new ItemStack(ModItems.tinyDusts, 2, 13), IC2Items.getItem("stoneDust"));
        Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedCinnabar"), liquidAmount, new ItemStack(ModItems.purifiedCrushedOre, 1, 4), new ItemStack(ModItems.tinyDusts, 2, 17), IC2Items.getItem("stoneDust"));
        Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedCobalt"), liquidAmount, new ItemStack(ModItems.purifiedCrushedOre, 1, 5), new ItemStack(ModItems.tinyDusts, 2, 20), IC2Items.getItem("stoneDust"));
        Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedDarkIron"), liquidAmount, new ItemStack(ModItems.purifiedCrushedOre, 1, 6), new ItemStack(ModItems.tinyDusts, 2, 24), IC2Items.getItem("stoneDust"));
        Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedIndium"), liquidAmount, new ItemStack(ModItems.purifiedCrushedOre, 1, 7), new ItemStack(ModItems.tinyDusts, 2, 37), IC2Items.getItem("stoneDust"));
        Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedIridium"), liquidAmount, new ItemStack(ModItems.purifiedCrushedOre, 1, 8), new ItemStack(ModItems.tinyDusts, 2, 39), IC2Items.getItem("stoneDust"));
        Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedNickel"), liquidAmount, new ItemStack(ModItems.purifiedCrushedOre, 1, 9), new ItemStack(ModItems.tinyDusts, 2, 55), IC2Items.getItem("stoneDust"));
        Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedOsmium"), liquidAmount, new ItemStack(ModItems.purifiedCrushedOre, 1, 10), new ItemStack(ModItems.tinyDusts, 2, 57), IC2Items.getItem("stoneDust"));
        Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedPlatinum"), liquidAmount, new ItemStack(ModItems.purifiedCrushedOre, 1, 11), new ItemStack(ModItems.tinyDusts, 2, 60), IC2Items.getItem("stoneDust"));
        Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedPyrite"), liquidAmount, new ItemStack(ModItems.purifiedCrushedOre, 1, 12), new ItemStack(ModItems.tinyDusts, 2, 62), IC2Items.getItem("stoneDust"));
        Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedSphalerite"), liquidAmount, new ItemStack(ModItems.purifiedCrushedOre, 1, 13), new ItemStack(ModItems.tinyDusts, 2, 74), IC2Items.getItem("stoneDust"));
        Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedTetrahedrite"), liquidAmount, new ItemStack(ModItems.purifiedCrushedOre, 1, 14), new ItemStack(ModItems.tinyDusts, 2, 79), IC2Items.getItem("stoneDust"));
        Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedTungsten"), liquidAmount, new ItemStack(ModItems.purifiedCrushedOre, 1, 15), new ItemStack(ModItems.tinyDusts, 2, 82), IC2Items.getItem("stoneDust"));
        Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedGalena"), liquidAmount, new ItemStack(ModItems.purifiedCrushedOre, 1, 16), new ItemStack(ModItems.tinyDusts, 2, 88), IC2Items.getItem("stoneDust"));

    }

}

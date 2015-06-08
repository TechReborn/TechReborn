package techreborn.compat.recipes;

import ic2.api.item.IC2Items;
import ic2.api.recipe.RecipeInputOreDict;
import ic2.api.recipe.Recipes;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import techreborn.api.recipe.RecipeHandler;
import techreborn.api.recipe.machines.AssemblingMachineRecipe;
import techreborn.api.recipe.machines.CentrifugeRecipe;
import techreborn.api.recipe.machines.GrinderRecipe;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;
import techreborn.init.ModFluids;
import techreborn.init.ModItems;
import techreborn.items.*;
import techreborn.util.CraftingHelper;
import techreborn.util.LogHelper;
import techreborn.util.RecipeRemover;
import techreborn.items.ItemParts;
import techreborn.items.ItemIngots;
import techreborn.items.ItemDusts;

public class RecipesIC2 {

    public static void init() {
        removeIc2Recipes();
        addShappedIc2Recipes();
        addShapedTrRecipes();
        addTRMaceratorRecipes();
        addTROreWashingRecipes();
        addTRThermalCentrifugeRecipes();
        addMetalFormerRecipes();
        addAssemblingMachineRecipes();
        addIndustrialCentrifugeRecipes();
        addIndustrialGrinderRecipes();
    }

    public static void removeIc2Recipes() {
        if (ConfigTechReborn.ExpensiveMacerator)
	        RecipeRemover.removeAnyRecipe(IC2Items.getItem("macerator"));
        if (ConfigTechReborn.ExpensiveDrill)
	        RecipeRemover.removeAnyRecipe(IC2Items.getItem("miningDrill"));
        if (ConfigTechReborn.ExpensiveDiamondDrill)
	        RecipeRemover.removeAnyRecipe(IC2Items.getItem("diamondDrill"));
        if (ConfigTechReborn.ExpensiveSolar)
	        RecipeRemover.removeAnyRecipe(IC2Items.getItem("solarPanel"));

        LogHelper.info("IC2 Recipes Removed");
    }

    public static void addShappedIc2Recipes() {
		if (ConfigTechReborn.ExpensiveMacerator)
			CraftingHelper.addShapedOreRecipe(IC2Items.getItem("macerator"),
				"FDF", "DMD", "FCF",
					'F', Items.flint,
					'D', Items.diamond,
					'M', IC2Items.getItem("machine"),
					'C', IC2Items.getItem("electronicCircuit"));

		if (ConfigTechReborn.ExpensiveDrill)
			CraftingHelper.addShapedOreRecipe(IC2Items.getItem("miningDrill"),
				" S ", "SCS", "SBS",
					'S', "ingotSteel",
					'B', IC2Items.getItem("reBattery"),
					'C', IC2Items.getItem("electronicCircuit"));

		if (ConfigTechReborn.ExpensiveDiamondDrill)
			CraftingHelper.addShapedOreRecipe(IC2Items.getItem("diamondDrill"),
				" D ", "DBD", "TCT",
					'D', "gemDiamond",
					'T', "ingotTitanium",
					'B', IC2Items.getItem("miningDrill"),
					'C', IC2Items.getItem("advancedCircuit"));

		if (ConfigTechReborn.ExpensiveSolar)
			CraftingHelper.addShapedOreRecipe(IC2Items.getItem("solarPanel"),
				"PPP", "SZS", "CGC",
					'P', "paneGlass",
					'S', new ItemStack(ModItems.parts, 1, 1),
					'Z', IC2Items.getItem("carbonPlate"),
					'G', IC2Items.getItem("generator"),
					'C', IC2Items.getItem("electronicCircuit"));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.thermalGenerator),
			"III", "IHI", "CGC",
				'I', "plateInvar",
				'H', IC2Items.getItem("reinforcedGlass"),
				'C', "circuitBasic",
				'G', IC2Items.getItem("geothermalGenerator"));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.thermalGenerator),
			"AAA", "AHA", "CGC",
				'A', "plateAluminum",
				'H', IC2Items.getItem("reinforcedGlass"),
				'C', "circuitBasic",
				'G', IC2Items.getItem("geothermalGenerator"));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Gasturbine),
			"IAI", "WGW", "IAI",
				'I', "plateInvar",
				'A', IC2Items.getItem("advancedCircuit"),
				'W', IC2Items.getItem("windMill"),
				'G', IC2Items.getItem("reinforcedGlass"));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Gasturbine),
			"IAI", "WGW", "IAI",
				'I', "plateAluminum",
				'A', IC2Items.getItem("advancedCircuit"),
				'W', IC2Items.getItem("windMill"),
				'G', IC2Items.getItem("reinforcedGlass"));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Semifluidgenerator),
			"III", "IHI", "CGC",
				'I', "plateIron",
				'H', IC2Items.getItem("reinforcedGlass"),
				'C', "circuitBasic",
				'G', IC2Items.getItem("generator"));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Semifluidgenerator),
			"AAA", "AHA", "CGC",
				'A', "plateAluminum",
				'H', IC2Items.getItem("reinforcedGlass"),
				'C', "circuitBasic",
				'G', IC2Items.getItem("generator"));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.DieselGenerator),
			"III", "I I", "CGC",
				'I', "plateIron",
				'C', "circuitBasic",
				'G', IC2Items.getItem("generator"));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.DieselGenerator),
			"AAA", "A A", "CGC",
				'A', "plateAluminum",
				'C', "circuitBasic",
				'G', IC2Items.getItem("generator"));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.MagicalAbsorber),
			"CSC", "IBI", "CAC",
				'C', "circuitMaster",
				'S', "craftingSuperconductor",
				'B', Blocks.beacon,
				'A', ModBlocks.Magicenergeyconverter,
				'I', IC2Items.getItem("iridiumPlate"));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Magicenergeyconverter),
			"CTC", "PBP", "CLC",
				'C', "circuitAdvanced",
				'P', "platePlatinum",
				'B', Blocks.beacon,
				'L', IC2Items.getItem("lapotronCrystal"),
				'T', IC2Items.getItem("teleporter"));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Dragoneggenergysiphoner),
			"CTC", "ISI", "CBC",
				'I', IC2Items.getItem("iridiumPlate"),
				'C', "circuitMaster",
				'B', "batteryUltimate",
				'S', ModBlocks.Supercondensator,
				'T', IC2Items.getItem("teleporter"));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.centrifuge),
			"SCS", "BEB", "SCS",
				'S', "plateSteel",
				'C', "circuitAdvanced",
				'B', IC2Items.getItem("advancedMachine"),
				'E', IC2Items.getItem("extractor"));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.IndustrialElectrolyzer),
			"SXS", "CEC", "SMS",
				'S', "plateSteel",
				'C', "circuitAdvanced",
				'X', IC2Items.getItem("extractor"),
				'E', IC2Items.getItem("electrolyzer"),
				'M', IC2Items.getItem("magnetizer"));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.BlastFurnace),
			"CHC", "HBH", "FHF",
				'H', new ItemStack(ModItems.parts, 1, 17),
				'C', "circuitAdvanced",
				'B', IC2Items.getItem("advancedMachine"),
				'F', IC2Items.getItem("inductionFurnace"));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Grinder),
			"ECP", "GGG", "CBC",
				'E', ModBlocks.IndustrialElectrolyzer,
				'P', IC2Items.getItem("pump"),
				'C', "circuitAdvanced",
				'B', IC2Items.getItem("advancedMachine"),
				'G', "craftingGrinder");

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.ImplosionCompressor),
			"ABA", "CPC", "ABA",
				'A', IC2Items.getItem("advancedAlloy"),
				'C', "circuitAdvanced",
				'B', IC2Items.getItem("advancedMachine"),
				'P', IC2Items.getItem("compressor"));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.VacuumFreezer),
			"SPS", "CGC", "SPS",
				'S', "plateSteel",
				'C', "circuitAdvanced",
				'G', IC2Items.getItem("reinforcedGlass"),
				'P', IC2Items.getItem("pump"));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Distillationtower),
			"CMC", "PBP", "EME",
				'E', ModBlocks.IndustrialElectrolyzer,
				'M', "circuitMaster",
				'B', IC2Items.getItem("advancedMachine"),
				'C', ModBlocks.centrifuge,
				'P', IC2Items.getItem("pump"));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.AlloyFurnace),
			"III", "F F", "III",
				'I', "plateIron",
				'F', IC2Items.getItem("ironFurnace"));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.AlloySmelter),
			"IHI", "CFC", "IHI",
				'I', "plateInvar",
				'C', "circuitBasic",
				'H', new ItemStack(ModItems.parts, 1, 17),
				'F', ModBlocks.AlloyFurnace);

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.AssemblyMachine),
			"CPC", "SBS", "CSC",
				'S', "plateSteel",
				'C', "circuitBasic",
				'B', IC2Items.getItem("machine"),
				'P', "craftingPiston");

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.ChemicalReactor),
			"IMI", "CPC", "IEI",
				'I', "plateInvar",
				'C', "circuitAdvanced",
				'M', IC2Items.getItem("magnetizer"),
				'P', IC2Items.getItem("compressor"),
				'E', IC2Items.getItem("extractor"));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.ChemicalReactor),
			"AMA", "CPC", "AEA",
				'A', "plateAluminum",
				'C', "circuitAdvanced",
				'M', IC2Items.getItem("magnetizer"),
				'P', IC2Items.getItem("compressor"),
				'E', IC2Items.getItem("extractor"));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.lathe),
			"SLS", "GBG", "SCS",
				'S', "plateSteel",
				'C', "circuitAdvanced",
				'G', "gearSteel",
				'B', IC2Items.getItem("advancedMachine"),
				'L', IC2Items.getItem("LathingTool"));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.platecuttingmachine),
			"SCS", "GDG", "SBS",
				'S', "plateSteel",
				'C', "circuitAdvanced",
				'G', "gearSteel",
				'B', IC2Items.getItem("advancedMachine"),
				'D', new ItemStack(ModItems.parts, 1, 9));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.RollingMachine),
			"PCP", "MBM", "PCP",
				'P', "craftingPiston",
				'C', "circuitAdvanced",
				'M', IC2Items.getItem("compressor"),
				'B', IC2Items.getItem("machine"));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.ElectricCraftingTable),
			"ITI", "IBI", "ICI",
				'I', "plateIron",
				'C', "circuitAdvanced",
				'T', "crafterWood",
				'B', IC2Items.getItem("machine"));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.ElectricCraftingTable),
			"ATA", "ABA", "ACA",
				'A', "plateAluminum",
				'C', "circuitAdvanced",
				'T', "crafterWood",
				'B', IC2Items.getItem("machine"));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.ChunkLoader),
			"SCS", "CMC", "SCS",
				'S', "plateSteel",
				'C', "circuitMaster",
				'M', new ItemStack(ModItems.parts, 1, 39));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Lesu),
			" L ", "CBC", " M ",
				'L', IC2Items.getItem("lvTransformer"),
				'C', "circuitAdvanced",
				'M', IC2Items.getItem("mvTransformer"),
				'B', ModBlocks.LesuStorage);

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.LesuStorage),
			"LLL", "LCL", "LLL",
				'L', "blockLapis",
				'C', "circuitBasic");

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Woodenshelf),
			"WWW", "A A", "WWW",
				'W', "plankWood",
				'A', "plateAluminum");

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Metalshelf),
			"III", "A A", "III",
				'I', "plateIron",
				'A', "plateAluminum");

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.HighAdvancedMachineBlock),
			"CTC", "TBT", "CTC",
				'C', "plateChrome",
				'T', "plateTitanium",
				'B', IC2Items.getItem("advancedMachine"));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.MachineCasing, 4, 0),
			"III", "CBC", "III",
				'I', "plateIron",
				'C', "circuitBasic",
				'B', IC2Items.getItem("machine"));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.MachineCasing, 4, 1),
			"SSS", "CBC", "SSS",
				'S', "plateSteel",
				'C', "circuitAdvanced",
				'B', IC2Items.getItem("advancedMachine"));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.MachineCasing, 4, 2),
			"HHH", "CBC", "HHH",
				'H', "plateChrome",
				'C', "circuitElite",
				'B', ModBlocks.HighAdvancedMachineBlock);

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.quantumChest),
			"DCD", "ATA", "DQD",
				'D', ItemParts.getPartByName("dataOrb"),
				'C', ItemParts.getPartByName("computerMonitor"),
				'A', ModBlocks.HighAdvancedMachineBlock,
				'Q', ModBlocks.digitalChest,
				'T', IC2Items.getItem("teleporter"));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.ComputerCube),
			"DME", "MAM", "EMD",
				'E', ItemParts.getPartByName("energyFlowCircuit"),
				'D', ItemParts.getPartByName("dataOrb"),
				'M', ItemParts.getPartByName("computerMonitor"),
				'A', IC2Items.getItem("advancedMachine"));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.lapotronicOrb),
			"LLL", "LPL", "LLL",
				'L', IC2Items.getItem("lapotronCrystal"),
				'P', IC2Items.getItem("iridiumPlate"));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.lapotronicOrb),
			"LLL", "LPL", "LLL",
				'L', IC2Items.getItem("lapotronCrystal"),
				'P', IC2Items.getItem("iridiumPlate"));

		CraftingHelper.addShapedOreRecipe(ItemParts.getPartByName("tungstenGrindingHead", 2),
			"TST", "SBS", "TST",
				'T', "plateTungsten",
				'S', "plateSteel",
				'B', "blockSteel");

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.MatterFabricator),
			"ETE", "AOA", "ETE",
				'E', ItemParts.getPartByName("energyFlowCircuit"),
				'T', IC2Items.getItem("teleporter"),
				'A', ModBlocks.HighAdvancedMachineBlock,
				'O', ModItems.lapotronicOrb);

		CraftingHelper.addShapedOreRecipe(ItemParts.getPartByName("iridiumAlloyIngot"),
			"IAI", "ADA", "IAI",
				'I', ItemIngots.getIngotByName("iridium"),
				'D', ItemDusts.getDustByName("diamond"),
				'A', IC2Items.getItem("advancedAlloy"));

		CraftingHelper.addShapedOreRecipe(ItemParts.getPartByName("energyFlowCircuit", 4),
			"ATA", "LIL", "ATA",
				'T', "plateTungsten",
				'I', IC2Items.getItem("iridiumPlate"),
				'A', IC2Items.getItem("advancedCircuit"),
				'L', IC2Items.getItem("lapotronCrystal"));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Supercondensator),
			"EOE", "SAS", "EOE",
				'E', ItemParts.getPartByName("energyFlowCircuit"),
				'O', ModItems.lapotronicOrb,
				'S', ItemParts.getPartByName("superconductor"),
				'A', ModBlocks.HighAdvancedMachineBlock);

		CraftingHelper.addShapedOreRecipe(ItemParts.getPartByName("superconductor", 4),
			"CCC", "TIT", "EEE",
				'E', ItemParts.getPartByName("energyFlowCircuit"),
				'C', ItemParts.getPartByName("heliumCoolantSimple"),
				'T', "ingotTungsten",
				'I', IC2Items.getItem("iridiumPlate"));

		CraftingHelper.addShapedOreRecipe(ItemParts.getPartByName("diamondSawBlade"),
			"DSD", "S S", "DSD",
				'S', "plateSteel",
				'D', ItemDusts.getDustByName("diamond"));

		LogHelper.info("Added Expensive IC2 Recipes");
	}

    public static void addShapedTrRecipes()
    {
        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.quantumTank),
			"EPE", "PCP", "EPE",
				'P', "platePlatinum",
				'E', "circuitMaster",
				'C', ModBlocks.quantumChest);

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.digitalChest), 
			"PPP", "PDP", "PCP",
				'P', "plateAluminum",
				'D', ItemParts.getPartByName("dataOrb"),
				'C', ItemParts.getPartByName("computerMonitor"));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.digitalChest),
			"PPP", "PDP", "PCP",
				'P', "plateSteel",
				'D', ItemParts.getPartByName("dataOrb"),
				'C', ItemParts.getPartByName("computerMonitor"));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.parts, 1, 40),
 			"PLP", "RGB", "PYP",
				'P', "plateAluminum",
				'L', "dyeLime",
				'R', "dyeRed",
				'G', "paneGlass",
				'B', "dyeBlue",
				'Y', Items.glowstone_dust);

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.parts, 4, 6),
 			"EEE", "EAE", "EEE",
				'E', "gemEmerald",
				'A', IC2Items.getItem("electronicCircuit"));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.parts, 1, 7),
 			"AGA", "RPB", "ASA",
				'A', "ingotAluminium",
				'G', "dyeGreen",
				'R', "dyeRed",
				'P', "paneGlass",
				'B', "dyeBlue",
				'S', Items.glowstone_dust);

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.parts, 4, 8),
 			"DSD", "S S", "DSD",
				'D', "dustDiamond",
				'S', "ingotSteel");

        CraftingHelper.addShapedOreRecipe(
                new ItemStack(ModItems.parts, 16, 13),
 			"CSC", "SCS", "CSC",
				'S', "ingotSteel",
				'C',IC2Items.getItem("electronicCircuit"));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.parts, 2, 14),
 			"TST", "SBS", "TST",
				'S', "ingotSteel",
				'T', "ingotTungsten",
				'B', "blockSteel");

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.parts, 1, 15),
 			"AAA", "AMA", "AAA",
				'A', "ingotAluminium",
				'M', new ItemStack(ModItems.parts, 1, 13));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.parts, 1, 16),
 			"AAA", "AMA", "AAA",
				'A', "ingotBronze",
				'M', new ItemStack(ModItems.parts, 1, 13));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.parts, 1, 17),
 			"AAA", "AMA", "AAA",
				'A', "ingotSteel",
				'M', new ItemStack(ModItems.parts, 1, 13));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.parts, 1, 18),
 			"AAA", "AMA", "AAA",
				'A', "ingotTitanium",
				'M', new ItemStack(ModItems.parts, 1, 13));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.parts, 1, 19),
			"AAA", "AMA", "AAA",
				'A', "ingotBrass",
				'M', new ItemStack(ModItems.parts, 1, 13));
    }

    public static void addTRMaceratorRecipes() {
        //Macerator

        if(OreDictionary.doesOreNameExist("oreAluminum")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreAluminum"), null, ItemCrushedOre.getCrushedOreByName("Aluminum", 2));
        }
        if(OreDictionary.doesOreNameExist("oreArdite")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreArdite"), null, ItemCrushedOre.getCrushedOreByName("Ardite", 2));
        }
        if(OreDictionary.doesOreNameExist("oreBauxite")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreBauxite"), null, ItemCrushedOre.getCrushedOreByName("Bauxite", 2));
        }
        if(OreDictionary.doesOreNameExist("oreCadmium")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreCadmium"), null, ItemCrushedOre.getCrushedOreByName("Cadmium", 2));
        }
        if(OreDictionary.doesOreNameExist("oreCinnabar")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreCinnabar"), null, ItemCrushedOre.getCrushedOreByName("Cinnabar", 2));
        }
        if(OreDictionary.doesOreNameExist("oreCobalt")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreCobalt"), null, ItemCrushedOre.getCrushedOreByName("Cobalt", 2));
        }
        if(OreDictionary.doesOreNameExist("oreDarkIron")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreDarkIron"), null, ItemCrushedOre.getCrushedOreByName("DarkIron", 2));
        }
        if(OreDictionary.doesOreNameExist("oreIndium")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreIndium"), null, ItemCrushedOre.getCrushedOreByName("Indium", 2));
        }
        if(OreDictionary.doesOreNameExist("oreIridium")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreIridium"), null, ItemCrushedOre.getCrushedOreByName("Iridium", 2));
        }
        if(OreDictionary.doesOreNameExist("oreNickel")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreNickel"), null, ItemCrushedOre.getCrushedOreByName("Nickel", 2));
        }
        if(OreDictionary.doesOreNameExist("orePlatinum")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("orePlatinum"), null, ItemCrushedOre.getCrushedOreByName("Platinum", 2));
        }
        if(OreDictionary.doesOreNameExist("orePyrite")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("orePyrite"), null, ItemCrushedOre.getCrushedOreByName("Pyrite", 2));
        }
        if(OreDictionary.doesOreNameExist("oreSphalerite")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreSphalerite"), null, ItemCrushedOre.getCrushedOreByName("Sphalerite", 2));
        }
        if(OreDictionary.doesOreNameExist("oreTetrahedrite")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreTetrahedrite"), null, ItemCrushedOre.getCrushedOreByName("Tetrahedrite", 2));
        }
        if(OreDictionary.doesOreNameExist("oreTungsten")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreTungsten"), null, ItemCrushedOre.getCrushedOreByName("Tungsten", 2));
        }
        if(OreDictionary.doesOreNameExist("oreGalena")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreGalena"), null, ItemCrushedOre.getCrushedOreByName("Galena", 2));
        }


        if(OreDictionary.doesOreNameExist("oreRedstone")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreRedstone"), null, new ItemStack(Items.redstone, 10));
        }
        if(OreDictionary.doesOreNameExist("oreLapis")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreLapis"), null, ItemDusts.getDustByName("lapis", 12));
        }
        if(OreDictionary.doesOreNameExist("oreDiamond")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreDiamond"), null, ItemDusts.getDustByName("diamond", 2));
        }
        if(OreDictionary.doesOreNameExist("oreEmerald")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreEmerald"), null, ItemDusts.getDustByName("emerald", 2));
        }
        if(OreDictionary.doesOreNameExist("oreRuby")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreRuby"), null, ItemDusts.getDustByName("ruby", 2));
        }
        if(OreDictionary.doesOreNameExist("oreSapphire")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreSapphire"), null, ItemDusts.getDustByName("sapphire", 2));
        }
        if(OreDictionary.doesOreNameExist("orePeridot")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("orePeridot"), null, ItemDusts.getDustByName("peridot", 2));
        }
        if(OreDictionary.doesOreNameExist("oreSulfur")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreSulfur"), null, ItemDusts.getDustByName("sulfur", 2));
        }
        if(OreDictionary.doesOreNameExist("oreSaltpeter")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreSaltpeter"), null, ItemDusts.getDustByName("saltpeter", 2));
        }
        if(OreDictionary.doesOreNameExist("oreTeslatite")) {
            ItemStack teslatiteStack = OreDictionary.getOres("dustTeslatite").get(0);
            teslatiteStack.stackSize = 10;
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreTeslatite"), null, teslatiteStack);
        }
        if(OreDictionary.doesOreNameExist("oreMithril")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreMithril"), null, ItemDusts.getDustByName("mithril", 2));
        }
        if(OreDictionary.doesOreNameExist("oreVinteum")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreVinteum"), null, ItemDusts.getDustByName("vinteum", 2));
        }
        if(OreDictionary.doesOreNameExist("limestone")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("limestone"), null, ItemDusts.getDustByName("limestone", 2));
        }
        if(OreDictionary.doesOreNameExist("stoneNetherrack")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("stoneNetherrack"), null, ItemDusts.getDustByName("netherrack", 2));
        }
        if(OreDictionary.doesOreNameExist("stoneEndstone")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("stoneEndstone"), null, ItemDusts.getDustByName("endstone", 2));
        }
        if(OreDictionary.doesOreNameExist("stoneRedrock")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("stoneRedrock"), null, ItemDusts.getDustByName("redrock", 2));
        }
        if(OreDictionary.doesOreNameExist("oreMagnetite")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreMagnetite"), null, ItemDusts.getDustByName("magnetite", 2));
        }
        if(OreDictionary.doesOreNameExist("oreLodestone")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreLodestone"), null, ItemDusts.getDustByName("lodestone", 2));
        }
        if(OreDictionary.doesOreNameExist("oreTellurium")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreTellurium"), null, ItemDusts.getDustByName("tellurium", 2));
        }
        if(OreDictionary.doesOreNameExist("oreSilicon")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreSilicon"), null, ItemDusts.getDustByName("silicon", 2));
        }
        if(OreDictionary.doesOreNameExist("oreVoidstone")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreVoidstone"), null, ItemDusts.getDustByName("voidstone", 2));
        }
        if(OreDictionary.doesOreNameExist("oreCalcite")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreCalcite"), null, ItemDusts.getDustByName("calcite", 2));
        }
        if(OreDictionary.doesOreNameExist("oreSodalite")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreSodalite"), null, ItemDusts.getDustByName("sodalite", 2));
        }
        if(OreDictionary.doesOreNameExist("oreGraphite")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreGraphite"), null, ItemDusts.getDustByName("graphite", 2));
        }
        if(OreDictionary.doesOreNameExist("blockMarble")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("blockMarble"), null, ItemDusts.getDustByName("marble", 2));
        }
        if(OreDictionary.doesOreNameExist("blockBasalt")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("blockBasalt"), null, ItemDusts.getDustByName("basalt", 2));
        }
    }

    public static void addTROreWashingRecipes() {
        //Ore Washing Plant
        NBTTagCompound liquidAmount = new NBTTagCompound();
        liquidAmount.setInteger("amount", 1000);
        Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedAluminum"), liquidAmount, ItemPurifiedCrushedOre.getPurifiedCrushedOreByName("Aluminum", 1), ItemDustTiny.getTinyDustByName("Aluminum", 2), IC2Items.getItem("stoneDust"));
        Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedArdite"), liquidAmount, ItemPurifiedCrushedOre.getPurifiedCrushedOreByName("Ardite", 1), ItemDustTiny.getTinyDustByName("Ardite", 2), IC2Items.getItem("stoneDust"));
        Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedBauxite"), liquidAmount, ItemPurifiedCrushedOre.getPurifiedCrushedOreByName("Bauxite", 1), ItemDustTiny.getTinyDustByName("Bauxite", 2), IC2Items.getItem("stoneDust"));
        Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedCadmium"), liquidAmount, ItemPurifiedCrushedOre.getPurifiedCrushedOreByName("Cadmium", 1), ItemDustTiny.getTinyDustByName("Cadmium", 2), IC2Items.getItem("stoneDust"));
        Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedCinnabar"), liquidAmount, ItemPurifiedCrushedOre.getPurifiedCrushedOreByName("Cinnabar", 1), ItemDustTiny.getTinyDustByName("Cinnabar", 2), IC2Items.getItem("stoneDust"));
        Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedCobalt"), liquidAmount, ItemPurifiedCrushedOre.getPurifiedCrushedOreByName("Cobalt", 1), ItemDustTiny.getTinyDustByName("Cobalt", 2), IC2Items.getItem("stoneDust"));
        Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedDarkIron"), liquidAmount, ItemPurifiedCrushedOre.getPurifiedCrushedOreByName("DarkIron", 1), ItemDustTiny.getTinyDustByName("DarkIron", 2), IC2Items.getItem("stoneDust"));
        Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedIndium"), liquidAmount, ItemPurifiedCrushedOre.getPurifiedCrushedOreByName("Indium", 1), ItemDustTiny.getTinyDustByName("Indium", 2), IC2Items.getItem("stoneDust"));
        Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedIridium"), liquidAmount, ItemPurifiedCrushedOre.getPurifiedCrushedOreByName("Iridium", 1), ItemDustTiny.getTinyDustByName("Iridium", 2), IC2Items.getItem("stoneDust"));
        Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedNickel"), liquidAmount, ItemPurifiedCrushedOre.getPurifiedCrushedOreByName("Nickel", 1), ItemDustTiny.getTinyDustByName("Nickel", 2), IC2Items.getItem("stoneDust"));
        Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedOsmium"), liquidAmount, ItemPurifiedCrushedOre.getPurifiedCrushedOreByName("Osmium", 1), ItemDustTiny.getTinyDustByName("Osmium", 2), IC2Items.getItem("stoneDust"));
        Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedPlatinum"), liquidAmount, ItemPurifiedCrushedOre.getPurifiedCrushedOreByName("Platinum", 1), ItemDustTiny.getTinyDustByName("Platinum", 2), IC2Items.getItem("stoneDust"));
        Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedPyrite"), liquidAmount, ItemPurifiedCrushedOre.getPurifiedCrushedOreByName("Pyrite", 1), ItemDustTiny.getTinyDustByName("Pyrite", 2), IC2Items.getItem("stoneDust"));
        Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedSphalerite"), liquidAmount, ItemPurifiedCrushedOre.getPurifiedCrushedOreByName("Sphalerite", 1), ItemDustTiny.getTinyDustByName("Sphalerite", 2), IC2Items.getItem("stoneDust"));
        Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedTetrahedrite"), liquidAmount, ItemPurifiedCrushedOre.getPurifiedCrushedOreByName("Tetrahedrite", 1), ItemDustTiny.getTinyDustByName("Tetrahedrite", 2), IC2Items.getItem("stoneDust"));
        Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedTungsten"), liquidAmount, ItemPurifiedCrushedOre.getPurifiedCrushedOreByName("Tungsten", 1), ItemDustTiny.getTinyDustByName("Tungsten", 2), IC2Items.getItem("stoneDust"));
        Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedGalena"), liquidAmount, ItemPurifiedCrushedOre.getPurifiedCrushedOreByName("Galena", 1), ItemDustTiny.getTinyDustByName("Galena", 2), IC2Items.getItem("stoneDust"));
    }

    public static void addTRThermalCentrifugeRecipes() {
        //Thermal Centrifuge

        //Heat Values
        NBTTagCompound aluminumHeat = new NBTTagCompound();
        aluminumHeat.setInteger("minHeat", 2000);
        NBTTagCompound arditeHeat = new NBTTagCompound();
        arditeHeat.setInteger("minHeat", 3000);
        NBTTagCompound bauxiteHeat = new NBTTagCompound();
        bauxiteHeat.setInteger("minHeat", 2500);
        NBTTagCompound cadmiumHeat = new NBTTagCompound();
        cadmiumHeat.setInteger("minHeat", 1500);
        NBTTagCompound cinnabarHeat = new NBTTagCompound();
        cinnabarHeat.setInteger("minHeat", 1500);
        NBTTagCompound cobaltHeat = new NBTTagCompound();
        cobaltHeat.setInteger("minHeat", 3000);
        NBTTagCompound darkIronHeat = new NBTTagCompound();
        darkIronHeat.setInteger("minHeat", 2500);
        NBTTagCompound indiumHeat = new NBTTagCompound();
        indiumHeat.setInteger("minHeat", 2000);
        NBTTagCompound iridiumHeat = new NBTTagCompound();
        iridiumHeat.setInteger("minHeat", 4000);
        NBTTagCompound nickelHeat = new NBTTagCompound();
        nickelHeat.setInteger("minHeat", 2000);
        NBTTagCompound osmiumHeat = new NBTTagCompound();
        osmiumHeat.setInteger("minHeat", 2000);
        NBTTagCompound platinumHeat = new NBTTagCompound();
        platinumHeat.setInteger("minHeat", 3000);
        NBTTagCompound pyriteHeat = new NBTTagCompound();
        pyriteHeat.setInteger("minHeat", 1500);
        NBTTagCompound sphaleriteHeat = new NBTTagCompound();
        sphaleriteHeat.setInteger("minHeat", 1500);
        NBTTagCompound tetrahedriteHeat = new NBTTagCompound();
        tetrahedriteHeat.setInteger("minHeat", 500);
        NBTTagCompound tungstenHeat = new NBTTagCompound();
        tungstenHeat.setInteger("minHeat", 2000);
        NBTTagCompound galenaHeat = new NBTTagCompound();
        galenaHeat.setInteger("minHeat", 2500);

        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedAluminum"), aluminumHeat, ItemDustTiny.getTinyDustByName("Bauxite", 1), ItemDusts.getDustByName("aluminum", 1), IC2Items.getItem("stoneDust"));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedArdite"), arditeHeat, ItemDustTiny.getTinyDustByName("Ardite", 1), ItemDusts.getDustByName("ardite", 1), IC2Items.getItem("stoneDust"));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedBauxite"), bauxiteHeat, ItemDustTiny.getTinyDustByName("Aluminum", 1), ItemDusts.getDustByName("bauxite", 1), IC2Items.getItem("stoneDust"));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedCadmium"), cadmiumHeat, ItemDustTiny.getTinyDustByName("Cadmium", 1), ItemDusts.getDustByName("cadmium", 1), IC2Items.getItem("stoneDust"));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedCinnabar"), cinnabarHeat, ItemDustTiny.getTinyDustByName("Redstone", 1), ItemDusts.getDustByName("cinnabar", 1), IC2Items.getItem("stoneDust"));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedCobalt"), cobaltHeat, ItemDustTiny.getTinyDustByName("Cobalt", 1), ItemDusts.getDustByName("cobalt", 1), IC2Items.getItem("stoneDust"));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedDarkIron"), darkIronHeat, ItemDustTiny.getTinyDustByName("Iron", 1), ItemDusts.getDustByName("darkIron", 1), IC2Items.getItem("stoneDust"));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedIndium"), indiumHeat, ItemDustTiny.getTinyDustByName("Indium", 1), ItemDusts.getDustByName("indium", 1), IC2Items.getItem("stoneDust"));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedIridium"), iridiumHeat, ItemDustTiny.getTinyDustByName("Platinum", 1), ItemDusts.getDustByName("iridium", 1), IC2Items.getItem("stoneDust"));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedNickel"), nickelHeat, ItemDustTiny.getTinyDustByName("Iron", 1), ItemDusts.getDustByName("nickel", 1), IC2Items.getItem("stoneDust"));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedOsmium"), osmiumHeat, ItemDustTiny.getTinyDustByName("Osmium", 1), ItemDusts.getDustByName("osmium", 1), IC2Items.getItem("stoneDust"));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPlatinum"), platinumHeat, ItemDustTiny.getTinyDustByName("Iridium", 1), ItemDusts.getDustByName("platinum", 1), IC2Items.getItem("stoneDust"));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPyrite"), pyriteHeat, ItemDustTiny.getTinyDustByName("Sulfur", 1), ItemDusts.getDustByName("pyrite", 1), IC2Items.getItem("stoneDust"));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedSphalerite"), sphaleriteHeat, ItemDustTiny.getTinyDustByName("Zinc", 1), ItemDusts.getDustByName("sphalerite", 1), IC2Items.getItem("stoneDust"));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedTetrahedrite"), tetrahedriteHeat, ItemDustTiny.getTinyDustByName("Antimony", 1), ItemDusts.getDustByName("tetrahedrite", 1), IC2Items.getItem("stoneDust"));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedTungsten"), tungstenHeat, ItemDustTiny.getTinyDustByName("Manganese", 1), ItemDusts.getDustByName("tungsten", 1), IC2Items.getItem("stoneDust"));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedGalena"), galenaHeat, ItemDustTiny.getTinyDustByName("Sulfur", 1), ItemDusts.getDustByName("galena", 1), IC2Items.getItem("stoneDust"));

        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedAluminum"), aluminumHeat, ItemDustTiny.getTinyDustByName("Bauxite", 1), ItemDusts.getDustByName("aluminum", 1));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedArdite"), arditeHeat, ItemDustTiny.getTinyDustByName("Ardite", 1), ItemDusts.getDustByName("ardite", 1));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedBauxite"), bauxiteHeat, ItemDustTiny.getTinyDustByName("Aluminum", 1), ItemDusts.getDustByName("bauxite", 1));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedCadmium"), cadmiumHeat, ItemDustTiny.getTinyDustByName("Cadmium", 1), ItemDusts.getDustByName("cadmium", 1));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedCinnabar"), cinnabarHeat, ItemDustTiny.getTinyDustByName("Redstone", 1), ItemDusts.getDustByName("cinnabar", 1));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedCobalt"), cobaltHeat, ItemDustTiny.getTinyDustByName("Cobalt", 1), ItemDusts.getDustByName("cobalt", 1));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedDarkIron"), darkIronHeat, ItemDustTiny.getTinyDustByName("Iron", 1), ItemDusts.getDustByName("darkIron", 1));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedIndium"), indiumHeat, ItemDustTiny.getTinyDustByName("Indium", 1), ItemDusts.getDustByName("indium", 1));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedIridium"), iridiumHeat, ItemDustTiny.getTinyDustByName("Platinum", 1), ItemDusts.getDustByName("iridium", 1));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedNickel"), nickelHeat, ItemDustTiny.getTinyDustByName("Iron", 1), ItemDusts.getDustByName("nickel", 1));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedOsmium"), osmiumHeat, ItemDustTiny.getTinyDustByName("Osmium", 1), ItemDusts.getDustByName("osmium", 1));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedPlatinum"), platinumHeat, ItemDustTiny.getTinyDustByName("Iridium", 1), ItemDusts.getDustByName("platinum", 1));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedPyrite"), pyriteHeat, ItemDustTiny.getTinyDustByName("Sulfur", 1), ItemDusts.getDustByName("pyrite", 1));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedSphalerite"), sphaleriteHeat, ItemDustTiny.getTinyDustByName("Zinc", 1), ItemDusts.getDustByName("sphalerite", 1));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedTetrahedrite"), tetrahedriteHeat, ItemDustTiny.getTinyDustByName("Antimony", 1), ItemDusts.getDustByName("tetrahedrite", 1));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedTungsten"), tungstenHeat, ItemDustTiny.getTinyDustByName("Manganese", 1), ItemDusts.getDustByName("tungsten", 1));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedGalena"), galenaHeat, ItemDustTiny.getTinyDustByName("Sulfur", 1), ItemDusts.getDustByName("galena", 1));
    }

    public static void addMetalFormerRecipes() {
        //Metal Former
        NBTTagCompound mode = new NBTTagCompound();
        mode.setInteger("mode", 1);
        Recipes.metalformerRolling.addRecipe(new RecipeInputOreDict("ingotAluminum"), mode, ItemPlates.getPlateByName("aluminum"));
        Recipes.metalformerRolling.addRecipe(new RecipeInputOreDict("ingotBatteryAlloy"), mode, ItemPlates.getPlateByName("batteryAlloy"));
        Recipes.metalformerRolling.addRecipe(new RecipeInputOreDict("ingotBrass"), mode, ItemPlates.getPlateByName("brass"));
        Recipes.metalformerRolling.addRecipe(new RecipeInputOreDict("ingotChrome"), mode, ItemPlates.getPlateByName("chrome"));
        Recipes.metalformerRolling.addRecipe(new RecipeInputOreDict("ingotElectrum"), mode, ItemPlates.getPlateByName("electrum"));
        Recipes.metalformerRolling.addRecipe(new RecipeInputOreDict("ingotInvar"), mode, ItemPlates.getPlateByName("invar"));
        Recipes.metalformerRolling.addRecipe(new RecipeInputOreDict("ingotIridium"), mode, ItemPlates.getPlateByName("iridium"));
        Recipes.metalformerRolling.addRecipe(new RecipeInputOreDict("ingotMagnalium"), mode, ItemPlates.getPlateByName("magnalium"));
        Recipes.metalformerRolling.addRecipe(new RecipeInputOreDict("ingotNickel"), mode, ItemPlates.getPlateByName("nickel"));
        Recipes.metalformerRolling.addRecipe(new RecipeInputOreDict("ingotOsmium"), mode, ItemPlates.getPlateByName("osmium"));
        Recipes.metalformerRolling.addRecipe(new RecipeInputOreDict("ingotPlatinum"), mode, ItemPlates.getPlateByName("platinum"));
        Recipes.metalformerRolling.addRecipe(new RecipeInputOreDict("ingotSilver"), mode, ItemPlates.getPlateByName("silver"));
        Recipes.metalformerRolling.addRecipe(new RecipeInputOreDict("ingotTitanium"), mode, ItemPlates.getPlateByName("titanium"));
        Recipes.metalformerRolling.addRecipe(new RecipeInputOreDict("ingotTungsten"), mode, ItemPlates.getPlateByName("tungsten"));
        Recipes.metalformerRolling.addRecipe(new RecipeInputOreDict("ingotTungstensteel"), mode, ItemPlates.getPlateByName("tungstensteel"));
        Recipes.metalformerRolling.addRecipe(new RecipeInputOreDict("ingotZinc"), mode, ItemPlates.getPlateByName("zinc"));
        Recipes.metalformerRolling.addRecipe(new RecipeInputOreDict("ingotRedAlloy"), mode, ItemPlates.getPlateByName("redstone"));
        Recipes.metalformerRolling.addRecipe(new RecipeInputOreDict("ingotBlueAlloy"), mode, ItemPlates.getPlateByName("teslatite"));
    }

    public static void addAssemblingMachineRecipes() {
        //Ender Eye
        RecipeHandler.addRecipe(new AssemblingMachineRecipe(new ItemStack(Items.ender_pearl, 1), new ItemStack(Items.blaze_powder), new ItemStack(Items.ender_eye), 120, 5));
        RecipeHandler.addRecipe(new AssemblingMachineRecipe(new ItemStack(Items.ender_pearl, 6), new ItemStack(Items.blaze_rod), new ItemStack(Items.ender_eye, 6), 120, 5));

        //Redstone Lamp
        RecipeHandler.addRecipe(new AssemblingMachineRecipe(new ItemStack(Items.redstone, 4), new ItemStack(Items.glowstone_dust, 4), new ItemStack(Blocks.redstone_lamp), 120, 5));

        //Note Block
        RecipeHandler.addRecipe(new AssemblingMachineRecipe(new ItemStack(Blocks.planks, 8), new ItemStack(Items.redstone, 1), new ItemStack(Blocks.noteblock), 120, 5));

        //Jukebox
        RecipeHandler.addRecipe(new AssemblingMachineRecipe(new ItemStack(Items.diamond, 1), new ItemStack(Blocks.planks, 8), new ItemStack(Blocks.jukebox), 120, 5));

        //Clock
        RecipeHandler.addRecipe(new AssemblingMachineRecipe(new ItemStack(Items.redstone, 1), new ItemStack(Items.gold_ingot, 4), new ItemStack(Items.clock), 120, 5));

        //Compass
        RecipeHandler.addRecipe(new AssemblingMachineRecipe(new ItemStack(Items.redstone, 1), new ItemStack(Items.iron_ingot, 4), new ItemStack(Items.clock), 120, 5));

        //Lead
        RecipeHandler.addRecipe(new AssemblingMachineRecipe(new ItemStack(Items.string, 1), new ItemStack(Items.slime_ball, 1), new ItemStack(Items.lead, 2), 120, 5));

        //Circuit Parts
        RecipeHandler.addRecipe(new AssemblingMachineRecipe(new ItemStack(Items.glowstone_dust), ItemDusts.getDustByName("lazurite", 1), ItemParts.getPartByName("advancedCircuitParts", 2), 120, 5));
        RecipeHandler.addRecipe(new AssemblingMachineRecipe(new ItemStack(Items.glowstone_dust), ItemDusts.getDustByName("lapis", 1), ItemParts.getPartByName("advancedCircuitParts", 2), 120, 5));

        //Electronic Circuit
        RecipeHandler.addRecipe(new AssemblingMachineRecipe(ItemParts.getPartByName("basicCircuitBoard", 1), new ItemStack(IC2Items.getItem("insulatedCopperCableItem").getItem(), 3), IC2Items.getItem("electronicCircuit"), 120, 5));

        //Advanced Circuit
        RecipeHandler.addRecipe(new AssemblingMachineRecipe(ItemParts.getPartByName("advancedCircuitBoard", 1), ItemParts.getPartByName("advancedCircuitParts", 2), IC2Items.getItem("advancedCircuit"), 120, 5));

        //Energy Flow Circuit
        RecipeHandler.addRecipe(new AssemblingMachineRecipe(ItemParts.getPartByName("processorCircuitBoard", 1), new ItemStack(IC2Items.getItem("lapotronCrystal").getItem(), 1, OreDictionary.WILDCARD_VALUE), new ItemStack(ModItems.parts, 1, 4), 120, 5));

        //Data Control Circuit
        RecipeHandler.addRecipe(new AssemblingMachineRecipe(ItemParts.getPartByName("processorCircuitBoard", 1), ItemParts.getPartByName("dataStorageCircuit", 1), ItemParts.getPartByName("dataControlCircuit", 1), 120, 5));

        //Data Storage Circuit
        RecipeHandler.addRecipe(new AssemblingMachineRecipe(ItemPlates.getPlateByName("emerald", 8), IC2Items.getItem("advancedCircuit"), ItemParts.getPartByName("dataStorageCircuit", 1), 120, 5));
        RecipeHandler.addRecipe(new AssemblingMachineRecipe(ItemPlates.getPlateByName("peridot", 8), IC2Items.getItem("advancedCircuit"), ItemParts.getPartByName("dataStorageCircuit", 1), 120, 5));

        //Data Orb
        RecipeHandler.addRecipe(new AssemblingMachineRecipe(ItemParts.getPartByName("dataControlCircuit", 1), ItemParts.getPartByName("dataStorageCircuit", 8), ItemParts.getPartByName("dataOrb"), 120, 5));

        //Basic Circuit Board
        RecipeHandler.addRecipe(new AssemblingMachineRecipe(ItemPlates.getPlateByName("aluminum", 1), ItemPlates.getPlateByName("electrum", 2), ItemParts.getPartByName("basicCircuitBoard", 2), 120, 5));
        RecipeHandler.addRecipe(new AssemblingMachineRecipe(ItemPlates.getPlateByName("iron", 1), ItemPlates.getPlateByName("electrum", 2), ItemParts.getPartByName("basicCircuitBoard", 2), 120, 5));

        //Advanced Circuit Board
        RecipeHandler.addRecipe(new AssemblingMachineRecipe(ItemPlates.getPlateByName("electrum", 2), IC2Items.getItem("electronicCircuit"), ItemParts.getPartByName("advancedCircuitBoard", 1), 120, 5));
        RecipeHandler.addRecipe(new AssemblingMachineRecipe(ItemPlates.getPlateByName("electrum", 4), ItemPlates.getPlateByName("silicon", 1), ItemParts.getPartByName("advancedCircuitBoard", 2), 120, 5));

        //Processor Circuit Board
        RecipeHandler.addRecipe(new AssemblingMachineRecipe(ItemPlates.getPlateByName("platinum", 1), IC2Items.getItem("advancedCircuit"), ItemParts.getPartByName("processorCircuitBoard", 1), 120, 5));

        //Frequency Transmitter
        RecipeHandler.addRecipe(new AssemblingMachineRecipe(IC2Items.getItem("electronicCircuit"), IC2Items.getItem("insulatedCopperCableItem"), IC2Items.getItem("frequencyTransmitter"), 120, 5));

        //Wind Mill
        RecipeHandler.addRecipe(new AssemblingMachineRecipe(ItemPlates.getPlateByName("magnalium", 2), IC2Items.getItem("generator"), IC2Items.getItem("windMill"), 120, 5));
        RecipeHandler.addRecipe(new AssemblingMachineRecipe(new ItemStack(IC2Items.getItem("carbonPlate").getItem(), 4), IC2Items.getItem("generator"), IC2Items.getItem("windMill"), 120, 5));

        //Water Mill
        RecipeHandler.addRecipe(new AssemblingMachineRecipe(ItemPlates.getPlateByName("aluminum", 4), IC2Items.getItem("generator"), IC2Items.getItem("waterMill"), 120, 5));

        //Industrial TNT
        RecipeHandler.addRecipe(new AssemblingMachineRecipe(ItemDusts.getDustByName("flint", 5), new ItemStack(Blocks.tnt), new ItemStack(IC2Items.getItem("industrialTnt").getItem(), 5), 120, 5));
    }

    public static void addIndustrialCentrifugeRecipes() {
        //Plantball/Bio Chaff
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Blocks.grass, 16), null, new ItemStack(IC2Items.getItem("biochaff").getItem(), 8), new ItemStack(IC2Items.getItem("plantBall").getItem(), 8), new ItemStack(Items.clay_ball), new ItemStack(Blocks.sand, 8), 2500, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Blocks.dirt, 16), null, new ItemStack(IC2Items.getItem("biochaff").getItem(), 4), new ItemStack(IC2Items.getItem("plantBall").getItem(), 4), new ItemStack(Items.clay_ball), new ItemStack(Blocks.sand, 8), 2500, 5));

        //Methane
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.mushroom_stew, 16), IC2Items.getItem("cell"), ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.apple, 32), IC2Items.getItem("cell"), ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.porkchop, 12), IC2Items.getItem("cell"), ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.cooked_porkchop, 16), IC2Items.getItem("cell"), ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.bread, 64), IC2Items.getItem("cell"), ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.fish, 12), IC2Items.getItem("cell"), ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.cooked_fished, 16), IC2Items.getItem("cell"), ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.beef, 12), IC2Items.getItem("cell"), ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.cooked_beef, 16), IC2Items.getItem("cell"), ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Blocks.pumpkin, 16), IC2Items.getItem("cell"), ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.speckled_melon, 1), IC2Items.getItem("cell"), ItemCells.getCellByName("methane", 1), new ItemStack(Items.gold_nugget, 6), null, null, 5000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.spider_eye, 32), IC2Items.getItem("cell"), ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.chicken, 12), IC2Items.getItem("cell"), ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.cooked_chicken, 16), IC2Items.getItem("cell"), ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.rotten_flesh, 16), IC2Items.getItem("cell"), ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.melon, 64), IC2Items.getItem("cell"), ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.cookie, 64), IC2Items.getItem("cell"), ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.cake, 8), IC2Items.getItem("cell"), ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.golden_carrot, 1), IC2Items.getItem("cell"), ItemCells.getCellByName("methane", 1), new ItemStack(Items.gold_nugget, 6), null, null, 5000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.carrot, 16), IC2Items.getItem("cell"), ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.baked_potato, 24), IC2Items.getItem("cell"), ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.potato, 16), IC2Items.getItem("cell"), ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.poisonous_potato, 12), IC2Items.getItem("cell"), ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.nether_wart, 1), IC2Items.getItem("cell"), ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(IC2Items.getItem("terraWart").getItem(), 16), IC2Items.getItem("cell"), ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Blocks.brown_mushroom, 1), IC2Items.getItem("cell"), ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Blocks.red_mushroom, 1), IC2Items.getItem("cell"), ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));

        //Rubber Wood Yields
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(IC2Items.getItem("rubberWood").getItem(), 15), new ItemStack(IC2Items.getItem("cell").getItem(), 5), new ItemStack(IC2Items.getItem("resin").getItem(), 8), new ItemStack(IC2Items.getItem("plantBall").getItem(), 6), ItemCells.getCellByName("methane", 1), ItemCells.getCellByName("carbon", 4), 5000, 5));

        //Soul Sand Byproducts
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Blocks.soul_sand, 16), IC2Items.getItem("cell"), ItemCells.getCellByName("oil", 1), ItemDusts.getDustByName("saltpeter", 4), ItemDusts.getDustByName("coal", 1), new ItemStack(Blocks.sand, 10), 2500, 5));

        //Mycelium Byproducts
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Blocks.mycelium, 8), null, new ItemStack(Blocks.brown_mushroom, 2), new ItemStack(Blocks.red_mushroom, 2), new ItemStack(Items.clay_ball, 1), new ItemStack(Blocks.sand, 4), 1640, 5));

        //Ice
        RecipeHandler.addRecipe(new CentrifugeRecipe(ItemCells.getCellByName("ice", 1), null, new ItemStack(Blocks.ice, 1), IC2Items.getItem("cell"), null, null, 40, 5));

        //Blaze Powder Byproducts
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.blaze_powder), null, ItemDusts.getDustByName("darkAshes", 1), ItemDusts.getDustByName("sulfur", 1), null, null, 1240, 5));

        //Magma Cream Products
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.magma_cream, 1), null, new ItemStack(Items.blaze_powder, 1), new ItemStack(Items.slime_ball, 1), null, null, 2500, 5));

        //Dust Byproducts
        RecipeHandler.addRecipe(new CentrifugeRecipe(ItemDusts.getDustByName("platinum", 1), null, ItemDustTiny.getTinyDustByName("Iridium", 1), ItemDustsSmall.getSmallDustByName("Nickel", 1), null, null, 3000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(ItemDusts.getDustByName("electrum", 2), null, ItemDusts.getDustByName("silver", 1), ItemDusts.getDustByName("gold", 1), null, null, 2400, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(ItemDusts.getDustByName("invar", 3), null, ItemDusts.getDustByName("iron", 2), ItemDusts.getDustByName("nickel", 1), null, null, 1340, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(ItemDusts.getDustByName("marble", 8), null, ItemDusts.getDustByName("magnesium", 1), ItemDusts.getDustByName("calcite", 7), null, null, 1280, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(ItemDusts.getDustByName("redrock", 4), null, ItemDusts.getDustByName("calcite", 2), ItemDusts.getDustByName("flint", 1), ItemDusts.getDustByName("clay", 1), null, 640, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(ItemDusts.getDustByName("basalt", 16), null, ItemDusts.getDustByName("peridot", 1), ItemDusts.getDustByName("calcite", 3), ItemDusts.getDustByName("magnesium", 8), ItemDusts.getDustByName("darkAshes", 4), 2680, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.glowstone_dust, 16), IC2Items.getItem("cell"), new ItemStack(Items.redstone, 8), ItemDusts.getDustByName("gold", 8), ItemCells.getCellByName("helium", 1), null, 25000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(ItemDusts.getDustByName("yellowGarnet", 16), null, ItemDusts.getDustByName("andradite", 5), ItemDusts.getDustByName("grossular", 8), ItemDusts.getDustByName("uvarovite", 3), null, 2940, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(ItemDusts.getDustByName("redGarnet", 16), null, ItemDusts.getDustByName("pyrope", 3), ItemDusts.getDustByName("almandine", 5), ItemDusts.getDustByName("spessartine", 8), null, 2940, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(ItemDusts.getDustByName("phosphorous", 5), new ItemStack(IC2Items.getItem("cell").getItem(), 3), ItemCells.getCellByName("calcium", 3), null, null, null, 1280, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(ItemDusts.getDustByName("darkAshes", 2), null, ItemDusts.getDustByName("ashes", 2), null, null, null, 240, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(ItemDusts.getDustByName("ashes", 1), IC2Items.getItem("cell"), ItemCells.getCellByName("carbon"), null, null, null, 80, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.redstone, 10), new ItemStack(IC2Items.getItem("cell").getItem(), 4), ItemCells.getCellByName("silicon", 1), ItemDusts.getDustByName("pyrite", 3), ItemDusts.getDustByName("ruby", 1), ItemCells.getCellByName("mercury", 3), 6800, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(ItemDusts.getDustByName("manyullyn", 2), null, ItemDusts.getDustByName("cobalt", 1), ItemDusts.getDustByName("ardite", 1), null, null, 1240, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(ItemDusts.getDustByName("nichrome", 5), null, ItemDusts.getDustByName("nickel", 4), ItemDusts.getDustByName("chrome", 1), null, null, 2240, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(ItemDusts.getDustByName("cupronickel", 2), null, ItemDusts.getDustByName("copper", 1), ItemDusts.getDustByName("nickel", 1), null, null, 960, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(ItemDusts.getDustByName("kanthal", 3), null, ItemDusts.getDustByName("iron", 1), ItemDusts.getDustByName("aluminum", 1), ItemDusts.getDustByName("chrome", 1), null, 1040, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(ItemDusts.getDustByName("endstone", 16), new ItemStack(IC2Items.getItem("cell").getItem(), 2), ItemCells.getCellByName("helium3", 1), ItemCells.getCellByName("helium"), ItemDustTiny.getTinyDustByName("Tungsten", 1), new ItemStack(Blocks.sand, 12), 4800, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(ItemDusts.getDustByName("cinnabar", 2), IC2Items.getItem("cell"), ItemCells.getCellByName("mercury", 1), ItemDusts.getDustByName("sulfur", 1), null, null, 80, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(ItemDusts.getDustByName("brass", 4), null, ItemDusts.getDustByName("zinc", 1), ItemDusts.getDustByName("copper", 3), null, null, 2000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(ItemDusts.getDustByName("aluminumBrass", 4), null, ItemDusts.getDustByName("aluminum", 1), ItemDusts.getDustByName("copper", 3), null, null, 2000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(ItemDusts.getDustByName("bronze", 4), null, ItemDusts.getDustByName("tin", 1), ItemDusts.getDustByName("copper", 3), null, null, 2420, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(ItemDusts.getDustByName("flint", 1), null, IC2Items.getItem("silicondioxideDust"), null, null, null, 160, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(ItemDusts.getDustByName("netherrack", 16), null, new ItemStack(Items.redstone, 1), ItemDusts.getDustByName("sulfur", 4), ItemDusts.getDustByName("basalt", 1), new ItemStack(Items.gold_nugget, 1), 2400, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(ItemDusts.getDustByName("enderEye", 1), null, ItemDusts.getDustByName("enderPearl", 1), new ItemStack(Items.blaze_powder, 1), null, null, 1280, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(ItemDusts.getDustByName("tetrahedrite", 8), null, ItemDusts.getDustByName("copper", 3), ItemDusts.getDustByName("antimony", 1), ItemDusts.getDustByName("sulfur", 3), ItemDusts.getDustByName("iron", 1), 3640, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(ItemDusts.getDustByName("lapis", 16), null, ItemDusts.getDustByName("lazurite", 12), ItemDusts.getDustByName("sodalite", 2), ItemDusts.getDustByName("pyrite", 7), ItemDusts.getDustByName("calcite", 1), 3580, 5));

        //Deuterium/Tritium
        RecipeHandler.addRecipe(new CentrifugeRecipe(ItemCells.getCellByName("helium", 16), null, ItemCells.getCellByName("deuterium", 1), new ItemStack(IC2Items.getItem("cell").getItem(), 15), null, null, 10000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(ItemCells.getCellByName("deuterium", 4), null, ItemCells.getCellByName("tritium", 1), new ItemStack(IC2Items.getItem("cell").getItem(), 3), null, null, 3000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(ItemCells.getCellByName("hydrogen", 4), null, ItemCells.getCellByName("deuterium", 1), new ItemStack(IC2Items.getItem("cell").getItem(), 3), null, null, 3000, 5));

        //Lava Cell Byproducts
        ItemStack lavaCells = IC2Items.getItem("lavaCell");
        lavaCells.stackSize = 8;
        RecipeHandler.addRecipe(new CentrifugeRecipe(lavaCells, null, ItemNuggets.getNuggetByName("electrum", 4), ItemIngots.getIngotByName("copper", 2), ItemDustTiny.getTinyDustByName("Tungsten", 1), ItemIngots.getIngotByName("tin", 17), 6000, 5));
    }

    public static void addIndustrialGrinderRecipes() {
        //Coal Ore
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.coal_ore, 1), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(Items.coal, 1), ItemDustsSmall.getSmallDustByName("Coal", 6), ItemDustsSmall.getSmallDustByName("Coal", 2), null, 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.coal_ore, 1), IC2Items.getItem("waterCell"), null, new ItemStack(Items.coal, 1), ItemDustsSmall.getSmallDustByName("Coal", 6), ItemDustsSmall.getSmallDustByName("Coal", 2), IC2Items.getItem("cell"), 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.coal_ore, 1), new ItemStack(Items.water_bucket), null, new ItemStack(Items.coal, 1), ItemDustsSmall.getSmallDustByName("Coal", 6), ItemDustsSmall.getSmallDustByName("Coal", 2), new ItemStack(Items.bucket), 100, 120));

        //Iron Ore
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.iron_ore, 1), null, new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("iron", 2), ItemDustsSmall.getSmallDustByName("Nickel", 1), ItemDustsSmall.getSmallDustByName("Tin", 1), null, 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.iron_ore, 1), IC2Items.getItem("waterCell"), null, ItemDusts.getDustByName("iron", 2), ItemDustsSmall.getSmallDustByName("Nickel", 1), ItemDustsSmall.getSmallDustByName("Tin", 1), IC2Items.getItem("cell"), 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.iron_ore, 1), new ItemStack(Items.water_bucket), null, ItemDusts.getDustByName("iron", 2), ItemDustsSmall.getSmallDustByName("Nickel", 1), ItemDustsSmall.getSmallDustByName("Tin", 1), new ItemStack(Items.bucket), 100, 120));

        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.iron_ore, 1), null, new FluidStack(ModFluids.fluidSodiumpersulfate, 1000), ItemDusts.getDustByName("iron", 2), ItemDusts.getDustByName("nickel", 1), ItemDustsSmall.getSmallDustByName("Tin", 1), null, 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.iron_ore, 1), ItemCells.getCellByName("sodiumPersulfate", 1), null, ItemDusts.getDustByName("iron", 2), ItemDusts.getDustByName("nickel", 1), ItemDustsSmall.getSmallDustByName("Tin", 1), IC2Items.getItem("cell"), 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.iron_ore, 1), new ItemStack(ModItems.bucketSodiumpersulfate), null, ItemDusts.getDustByName("iron", 2), ItemDusts.getDustByName("nickel", 1), ItemDustsSmall.getSmallDustByName("Tin", 1), new ItemStack(Items.bucket), 100, 120));

        //Gold Ore
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.gold_ore, 1), null, new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("gold", 2), ItemDustsSmall.getSmallDustByName("Copper", 1), ItemDustsSmall.getSmallDustByName("Nickel", 1), null, 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.gold_ore, 1), IC2Items.getItem("waterCell"), null, ItemDusts.getDustByName("gold", 2), ItemDustsSmall.getSmallDustByName("Copper", 1), ItemDustsSmall.getSmallDustByName("Nickel", 1), IC2Items.getItem("cell"), 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.gold_ore, 1), new ItemStack(Items.water_bucket), null, ItemDusts.getDustByName("gold", 2), ItemDustsSmall.getSmallDustByName("Copper", 1), ItemDustsSmall.getSmallDustByName("Nickel", 1), new ItemStack(Items.bucket), 100, 120));

        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.gold_ore, 1), null, new FluidStack(ModFluids.fluidSodiumpersulfate, 1000), ItemDusts.getDustByName("gold", 2), ItemDusts.getDustByName("copper", 1), ItemDustsSmall.getSmallDustByName("Nickel", 1), null, 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.gold_ore, 1), ItemCells.getCellByName("sodiumPersulfate", 1), null, ItemDusts.getDustByName("gold", 2), ItemDusts.getDustByName("copper", 1), ItemDustsSmall.getSmallDustByName("Nickel", 1), IC2Items.getItem("cell"), 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.gold_ore, 1), new ItemStack(ModItems.bucketSodiumpersulfate), null, ItemDusts.getDustByName("gold", 2), ItemDusts.getDustByName("copper", 1), ItemDustsSmall.getSmallDustByName("Nickel", 1), new ItemStack(Items.bucket), 100, 120));

        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.gold_ore, 1), null, new FluidStack(ModFluids.fluidMercury, 1000), ItemDusts.getDustByName("gold", 3), ItemDustsSmall.getSmallDustByName("Copper", 1), ItemDustsSmall.getSmallDustByName("Nickel", 1), null, 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.gold_ore, 1), ItemCells.getCellByName("mercury", 1), null, ItemDusts.getDustByName("gold", 3), ItemDustsSmall.getSmallDustByName("Copper", 1), ItemDustsSmall.getSmallDustByName("Nickel", 1), IC2Items.getItem("cell"), 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.gold_ore, 1), new ItemStack(ModItems.bucketMercury), null, ItemDusts.getDustByName("gold", 3), ItemDustsSmall.getSmallDustByName("Copper", 1), ItemDustsSmall.getSmallDustByName("Nickel", 1), new ItemStack(Items.bucket), 100, 120));

        //Diamond Ore
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.diamond_ore, 1), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(Items.diamond, 1), ItemDustsSmall.getSmallDustByName("Diamond", 6), ItemDustsSmall.getSmallDustByName("Coal", 2), null, 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.diamond_ore, 1), IC2Items.getItem("waterCell"), null, new ItemStack(Items.diamond, 1), ItemDustsSmall.getSmallDustByName("Diamond", 6), ItemDustsSmall.getSmallDustByName("Coal", 2), IC2Items.getItem("cell"), 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.diamond_ore, 1), new ItemStack(Items.water_bucket), null, new ItemStack(Items.diamond, 1), ItemDustsSmall.getSmallDustByName("Diamond", 6), ItemDustsSmall.getSmallDustByName("Coal", 2), new ItemStack(Items.bucket), 100, 120));

        //Emerald Ore
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.emerald_ore, 1), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(Items.emerald, 1), ItemDustsSmall.getSmallDustByName("Emerald", 6), ItemDustsSmall.getSmallDustByName("Aluminum", 2), null, 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.emerald_ore, 1), IC2Items.getItem("waterCell"), null, new ItemStack(Items.emerald, 1), ItemDustsSmall.getSmallDustByName("Emerald", 6), ItemDustsSmall.getSmallDustByName("Aluminum", 2), IC2Items.getItem("cell"), 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.emerald_ore, 1), new ItemStack(Items.water_bucket), null, new ItemStack(Items.emerald, 1), ItemDustsSmall.getSmallDustByName("Emerald", 6), ItemDustsSmall.getSmallDustByName("Aluminum", 2), new ItemStack(Items.bucket), 100, 120));

        //Redstone
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.redstone_ore, 1), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(Items.redstone, 10), ItemDustsSmall.getSmallDustByName("Cinnabar", 1), ItemDustsSmall.getSmallDustByName("Glowstone", 1), null, 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.redstone_ore, 1), IC2Items.getItem("waterCell"), null, new ItemStack(Items.redstone, 10), ItemDustsSmall.getSmallDustByName("Cinnabar", 1), ItemDustsSmall.getSmallDustByName("Glowstone", 1), IC2Items.getItem("cell"), 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.redstone_ore, 1), new ItemStack(Items.water_bucket), null, new ItemStack(Items.redstone, 10), ItemDustsSmall.getSmallDustByName("Cinnabar", 1), ItemDustsSmall.getSmallDustByName("Glowstone", 1), new ItemStack(Items.bucket), 100, 120));

        //Lapis Lazuli Ore
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.lapis_ore, 1), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(Items.dye, 6, 4), ItemDustsSmall.getSmallDustByName("Lapis", 36), ItemDustsSmall.getSmallDustByName("Lazurite", 8), null, 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.lapis_ore, 1), IC2Items.getItem("waterCell"), null, new ItemStack(Items.dye, 6, 4), ItemDustsSmall.getSmallDustByName("Lapis", 36), ItemDustsSmall.getSmallDustByName("Lazurite", 8), IC2Items.getItem("cell"), 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.lapis_ore, 1), new ItemStack(Items.water_bucket), null, new ItemStack(Items.dye, 6, 4), ItemDustsSmall.getSmallDustByName("Lapis", 36), ItemDustsSmall.getSmallDustByName("Lazurite", 8), new ItemStack(Items.bucket), 100, 120));

        //Copper Ore
        if(OreDictionary.doesOreNameExist("oreCopper")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("oreCopper").get(0);
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("copper", 2), ItemDustsSmall.getSmallDustByName("Gold", 1), ItemDustsSmall.getSmallDustByName("Nickel", 1), null, 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, ItemDusts.getDustByName("copper", 2), ItemDustsSmall.getSmallDustByName("Gold", 1), ItemDustsSmall.getSmallDustByName("Nickel", 1), IC2Items.getItem("cell"), 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, ItemDusts.getDustByName("copper", 2), ItemDustsSmall.getSmallDustByName("Gold", 1), ItemDustsSmall.getSmallDustByName("Nickel", 1), new ItemStack(Items.bucket), 100, 120));

                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(ModFluids.fluidSodiumpersulfate, 1000), ItemDusts.getDustByName("copper", 2), ItemDusts.getDustByName("gold", 1), ItemDustsSmall.getSmallDustByName("Nickel", 1), null, 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, ItemCells.getCellByName("sodiumPersulfate", 1), null, ItemDusts.getDustByName("copper", 2), ItemDusts.getDustByName("gold", 1), ItemDustsSmall.getSmallDustByName("Nickel", 1), IC2Items.getItem("cell"), 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(ModItems.bucketSodiumpersulfate), null, ItemDusts.getDustByName("copper", 2), ItemDusts.getDustByName("gold", 1), ItemDustsSmall.getSmallDustByName("Nickel", 1), new ItemStack(Items.bucket), 100, 120));

                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(ModFluids.fluidMercury, 1000), ItemDusts.getDustByName("copper", 2), ItemDustsSmall.getSmallDustByName("Gold", 1), ItemDusts.getDustByName("nickel", 1), null, 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, ItemCells.getCellByName("mercury", 1), null, ItemDusts.getDustByName("copper", 2), ItemDustsSmall.getSmallDustByName("Gold", 1), ItemDusts.getDustByName("nickel", 1), IC2Items.getItem("cell"), 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, ItemCells.getCellByName("mercury", 1), null, ItemDusts.getDustByName("copper", 2), ItemDustsSmall.getSmallDustByName("Gold", 1), ItemDusts.getDustByName("nickel", 1), new ItemStack(Items.bucket), 100, 120));
            }
            catch (Exception e) {
                LogHelper.info("Failed to Load Grinder Recipe for Copper Ore");
            }
        }

        //Tin Ore
        if(OreDictionary.doesOreNameExist("oreTin")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("oreTin").get(0);
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("tin", 2), ItemDustsSmall.getSmallDustByName("Iron", 1), ItemDustsSmall.getSmallDustByName("Zinc", 1), null, 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, ItemDusts.getDustByName("tin", 2), ItemDustsSmall.getSmallDustByName("Iron", 1), ItemDustsSmall.getSmallDustByName("Zinc", 1), IC2Items.getItem("cell"), 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, ItemDusts.getDustByName("tin", 2), ItemDustsSmall.getSmallDustByName("Iron", 1), ItemDustsSmall.getSmallDustByName("Zinc", 1), new ItemStack(Items.bucket), 100, 120));

                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(ModFluids.fluidSodiumpersulfate, 1000), ItemDusts.getDustByName("tin", 2), ItemDustsSmall.getSmallDustByName("Iron", 1), ItemDusts.getDustByName("zinc", 1), null, 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, ItemCells.getCellByName("sodiumPersulfate", 1), null, ItemDusts.getDustByName("tin", 2), ItemDustsSmall.getSmallDustByName("Iron", 1), ItemDusts.getDustByName("zinc", 1), IC2Items.getItem("cell"), 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(ModItems.bucketSodiumpersulfate), null, ItemDusts.getDustByName("tin", 2), ItemDustsSmall.getSmallDustByName("Iron", 1), ItemDusts.getDustByName("zinc", 1), new ItemStack(Items.bucket), 100, 120));
            }
            catch (Exception e) {
                LogHelper.info("Failed to Load Grinder Recipe for Tin Ore");
            }
        }

        //Nickel Ore
        if(OreDictionary.doesOreNameExist("oreNickel")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("oreNickel").get(0);
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("nickel", 2), ItemDustsSmall.getSmallDustByName("Iron", 1), ItemDustsSmall.getSmallDustByName("Platinum", 1), null, 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, ItemDusts.getDustByName("nickel", 2), ItemDustsSmall.getSmallDustByName("Iron", 1), ItemDustsSmall.getSmallDustByName("Platinum", 1), IC2Items.getItem("cell"), 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, ItemDusts.getDustByName("nickel", 2), ItemDustsSmall.getSmallDustByName("Iron", 1), ItemDustsSmall.getSmallDustByName("Platinum", 1), new ItemStack(Items.bucket), 100, 120));

                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(ModFluids.fluidSodiumpersulfate, 1000), ItemDusts.getDustByName("nickel", 3), ItemDustsSmall.getSmallDustByName("Iron", 1), ItemDustsSmall.getSmallDustByName("Platinum", 1), null, 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, ItemCells.getCellByName("sodiumPersulfate", 1), null, ItemDusts.getDustByName("nickel", 3), ItemDustsSmall.getSmallDustByName("Iron", 1), ItemDustsSmall.getSmallDustByName("Platinum", 1), IC2Items.getItem("cell"), 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(ModItems.bucketSodiumpersulfate), null, ItemDusts.getDustByName("nickel", 3), ItemDustsSmall.getSmallDustByName("Iron", 1), ItemDustsSmall.getSmallDustByName("Platinum", 1), new ItemStack(Items.bucket), 100, 120));

                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(ModFluids.fluidMercury, 1000), ItemDusts.getDustByName("nickel", 2), ItemDustsSmall.getSmallDustByName("Iron", 1), ItemDusts.getDustByName("platinum", 1), null, 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, ItemCells.getCellByName("mercury", 1), null, ItemDusts.getDustByName("nickel", 2), ItemDustsSmall.getSmallDustByName("Iron", 1), ItemDusts.getDustByName("platinum", 1), IC2Items.getItem("cell"), 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(ModItems.bucketMercury), null, ItemDusts.getDustByName("nickel", 2), ItemDustsSmall.getSmallDustByName("Iron", 1), ItemDusts.getDustByName("platinum", 1), new ItemStack(Items.bucket), 100, 120));
            }
            catch (Exception e) {
                LogHelper.info("Failed to Load Grinder Recipe for Nickel Ore");
            }
        }

        //Zinc Ore
        if(OreDictionary.doesOreNameExist("oreZinc")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("oreZinc").get(0);
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("zinc", 2), ItemDustsSmall.getSmallDustByName("Iron", 1), ItemDustsSmall.getSmallDustByName("Tin", 1), null, 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, ItemDusts.getDustByName("zinc", 2), ItemDustsSmall.getSmallDustByName("Iron", 1), ItemDustsSmall.getSmallDustByName("Tin", 1), IC2Items.getItem("cell"), 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, ItemDusts.getDustByName("zinc", 2), ItemDustsSmall.getSmallDustByName("Iron", 1), ItemDustsSmall.getSmallDustByName("Tin", 1), new ItemStack(Items.bucket), 100, 120));

                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(ModFluids.fluidSodiumpersulfate, 1000), ItemDusts.getDustByName("zinc", 2), ItemDustsSmall.getSmallDustByName("Iron", 1), ItemDusts.getDustByName("iron", 1), null, 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, ItemCells.getCellByName("sodiumPersulfate", 1), null, ItemDusts.getDustByName("zinc", 2), ItemDustsSmall.getSmallDustByName("Iron", 1), ItemDusts.getDustByName("iron", 1), IC2Items.getItem("cell"), 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(ModItems.bucketSodiumpersulfate), null, ItemDusts.getDustByName("zinc", 2), ItemDustsSmall.getSmallDustByName("Iron", 1), ItemDusts.getDustByName("iron", 1), new ItemStack(Items.bucket), 100, 120));
            }
            catch (Exception e) {
                LogHelper.info("Failed to Load Grinder Recipe for Zinc Ore");
            }
        }

        //Silver Ore
        if(OreDictionary.doesOreNameExist("oreSilver")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("oreSilver").get(0);
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("silver", 2), ItemDustsSmall.getSmallDustByName("Lead", 1), ItemDustsSmall.getSmallDustByName("Sulfur", 1), null, 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, ItemDusts.getDustByName("silver", 2), ItemDustsSmall.getSmallDustByName("Lead", 1), ItemDustsSmall.getSmallDustByName("Sulfur", 1), IC2Items.getItem("cell"), 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, ItemDusts.getDustByName("silver", 2), ItemDustsSmall.getSmallDustByName("Lead", 1), ItemDustsSmall.getSmallDustByName("Sulfur", 1), new ItemStack(Items.bucket), 100, 120));

                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(ModFluids.fluidMercury, 1000), ItemDusts.getDustByName("silver", 3), ItemDustsSmall.getSmallDustByName("Lead", 1), ItemDustsSmall.getSmallDustByName("Sulfur", 1), null, 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, ItemCells.getCellByName("mercury", 1), null, ItemDusts.getDustByName("silver", 3), ItemDustsSmall.getSmallDustByName("Lead", 1), ItemDustsSmall.getSmallDustByName("Sulfur", 1), IC2Items.getItem("cell"), 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(ModItems.bucketMercury), null, ItemDusts.getDustByName("silver", 3), ItemDustsSmall.getSmallDustByName("Lead", 1), ItemDustsSmall.getSmallDustByName("Sulfur", 1), new ItemStack(Items.bucket), 100, 120));
            }
            catch (Exception e) {
                LogHelper.info("Failed to Load Grinder Recipe for Silver Ore");
            }
        }

        //Lead Ore
        if(OreDictionary.doesOreNameExist("oreLead")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("oreLead").get(0);
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("lead", 2), ItemDustsSmall.getSmallDustByName("Silver", 1), ItemDustsSmall.getSmallDustByName("Sulfur", 1), null, 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, ItemDusts.getDustByName("lead", 2), ItemDustsSmall.getSmallDustByName("Silver", 1), ItemDustsSmall.getSmallDustByName("Sulfur", 1), IC2Items.getItem("cell"), 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, ItemDusts.getDustByName("lead", 2), ItemDustsSmall.getSmallDustByName("Silver", 1), ItemDustsSmall.getSmallDustByName("Sulfur", 1), new ItemStack(Items.bucket), 100, 120));

                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(ModFluids.fluidMercury, 1000), ItemDusts.getDustByName("lead", 2), ItemDusts.getDustByName("silver", 1), ItemDustsSmall.getSmallDustByName("Sulfur", 1), null, 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, ItemCells.getCellByName("mercury", 1), null, ItemDusts.getDustByName("lead", 2), ItemDusts.getDustByName("silver", 1), ItemDustsSmall.getSmallDustByName("Sulfur", 1), IC2Items.getItem("cell"), 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(ModItems.bucketMercury), null, ItemDusts.getDustByName("lead", 2), ItemDusts.getDustByName("silver", 1), ItemDustsSmall.getSmallDustByName("Sulfur", 1), new ItemStack(Items.bucket), 100, 120));
            }
            catch (Exception e) {
                LogHelper.info("Failed to Load Grinder Recipe for Lead Ore");
            }
        }

        //Uranium Ore
        if(OreDictionary.doesOreNameExist("oreUranium")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("oreUranium").get(0);
                ItemStack uranium238Stack = IC2Items.getItem("Uran238");
                uranium238Stack.stackSize = 8;
                ItemStack uranium235Stack = IC2Items.getItem("smallUran235");
                uranium235Stack.stackSize = 2;
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), uranium238Stack, uranium235Stack, null, null, 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, uranium238Stack, uranium235Stack, null, IC2Items.getItem("cell"), 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, uranium238Stack, uranium235Stack, null, new ItemStack(Items.bucket), 100, 120));
            }
            catch (Exception e) {
                LogHelper.info("Failed to Load Grinder Recipe for Uranium Ore");
            }
        }

        //Pitchblende Ore
        if(OreDictionary.doesOreNameExist("orePitchblende")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("orePitchblende").get(0);
                ItemStack uranium238Stack = IC2Items.getItem("Uran238");
                uranium238Stack.stackSize = 8;
                ItemStack uranium235Stack = IC2Items.getItem("smallUran235");
                uranium235Stack.stackSize = 2;
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), uranium238Stack, uranium235Stack, null, null, 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, uranium238Stack, uranium235Stack, null, IC2Items.getItem("cell"), 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, uranium238Stack, uranium235Stack, null, new ItemStack(Items.bucket), 100, 120));
            }
            catch (Exception e) {
                LogHelper.info("Failed to Load Grinder Recipe for Uranium Ore");
            }
        }

        //Aluminum Ore
        if(OreDictionary.doesOreNameExist("oreAluminum")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("oreAluminum").get(0);
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("aluminum", 2), ItemDustsSmall.getSmallDustByName("Bauxite", 1), ItemDustsSmall.getSmallDustByName("Bauxite", 1), null, 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, ItemDusts.getDustByName("aluminum", 2), ItemDustsSmall.getSmallDustByName("Bauxite", 1), ItemDustsSmall.getSmallDustByName("Bauxite", 1), IC2Items.getItem("cell"), 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, ItemDusts.getDustByName("aluminum", 2), ItemDustsSmall.getSmallDustByName("Bauxite", 1), ItemDustsSmall.getSmallDustByName("Bauxite", 1), new ItemStack(Items.bucket), 100, 120));
            }
            catch (Exception e) {
                LogHelper.info("Failed to Load Grinder Recipe for Lead Ore");
            }
        }

        //Ardite Ore
        if(OreDictionary.doesOreNameExist("oreArdite")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("oreArdite").get(0);
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("ardite", 2), ItemDustsSmall.getSmallDustByName("Ardite", 1), ItemDustsSmall.getSmallDustByName("Ardite", 1), null, 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, ItemDusts.getDustByName("ardite", 2), ItemDustsSmall.getSmallDustByName("Ardite", 1), ItemDustsSmall.getSmallDustByName("Ardite", 1), IC2Items.getItem("cell"), 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, ItemDusts.getDustByName("ardite", 2), ItemDustsSmall.getSmallDustByName("Ardite", 1), ItemDustsSmall.getSmallDustByName("Ardite", 1), new ItemStack(Items.bucket), 100, 120));
            }
            catch (Exception e) {
                LogHelper.info("Failed to Load Grinder Recipe for Ardite Ore");
            }
        }

        //Cobalt Ore
        if(OreDictionary.doesOreNameExist("oreCobalt")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("oreCobalt").get(0);
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("cobalt", 2), ItemDustsSmall.getSmallDustByName("Cobalt", 1), ItemDustsSmall.getSmallDustByName("Cobalt", 1), null, 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, ItemDusts.getDustByName("cobalt", 2), ItemDustsSmall.getSmallDustByName("Cobalt", 1), ItemDustsSmall.getSmallDustByName("Cobalt", 1), IC2Items.getItem("cell"), 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, ItemDusts.getDustByName("cobalt", 2), ItemDustsSmall.getSmallDustByName("Cobalt", 1), ItemDustsSmall.getSmallDustByName("Cobalt", 1), new ItemStack(Items.bucket), 100, 120));
            }
            catch (Exception e) {
                LogHelper.info("Failed to Load Grinder Recipe for Cobalt Ore");
            }
        }

        //Dark Iron Ore
        if(OreDictionary.doesOreNameExist("oreDarkIron")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("oreDarkIron").get(0);
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("darkIron", 2), ItemDustsSmall.getSmallDustByName("DarkIron", 1), ItemDustsSmall.getSmallDustByName("Iron", 1), null, 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, ItemDusts.getDustByName("darkIron", 2), ItemDustsSmall.getSmallDustByName("DarkIron", 1), ItemDustsSmall.getSmallDustByName("Iron", 1), IC2Items.getItem("cell"), 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, ItemDusts.getDustByName("darkIron", 2), ItemDustsSmall.getSmallDustByName("DarkIron", 1), ItemDustsSmall.getSmallDustByName("Iron", 1), new ItemStack(Items.bucket), 100, 120));
            }
            catch (Exception e) {
                LogHelper.info("Failed to Load Grinder Recipe for Dark Iron Ore");
            }
        }

        //Cadmium Ore
        if(OreDictionary.doesOreNameExist("oreCadmium")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("oreCadmium").get(0);
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("cadmium", 2), ItemDustsSmall.getSmallDustByName("Cadmium", 1), ItemDustsSmall.getSmallDustByName("Cadmium", 1), null, 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, ItemDusts.getDustByName("cadmium", 2), ItemDustsSmall.getSmallDustByName("Cadmium", 1), ItemDustsSmall.getSmallDustByName("Cadmium", 1), IC2Items.getItem("cell"), 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, ItemDusts.getDustByName("cadmium", 2), ItemDustsSmall.getSmallDustByName("Cadmium", 1), ItemDustsSmall.getSmallDustByName("Cadmium", 1), new ItemStack(Items.bucket), 100, 120));
            }
            catch (Exception e) {
                LogHelper.info("Failed to Load Grinder Recipe for Cadmium Ore");
            }
        }

        //Indium Ore
        if(OreDictionary.doesOreNameExist("oreIndium")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("oreIndium").get(0);
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("indium", 2), ItemDustsSmall.getSmallDustByName("Indium", 1), ItemDustsSmall.getSmallDustByName("Indium", 1), null, 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, ItemDusts.getDustByName("indium", 2), ItemDustsSmall.getSmallDustByName("Indium", 1), ItemDustsSmall.getSmallDustByName("Indium", 1), IC2Items.getItem("cell"), 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, ItemDusts.getDustByName("indium", 2), ItemDustsSmall.getSmallDustByName("Indium", 1), ItemDustsSmall.getSmallDustByName("Indium", 1), new ItemStack(Items.bucket), 100, 120));
            } catch (Exception e) {
                LogHelper.info("Failed to Load Grinder Recipe for Indium Ore");
            }
        }

        //Calcite Ore
        if(OreDictionary.doesOreNameExist("oreCalcite") && OreDictionary.doesOreNameExist("gemCalcite")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("oreCalcite").get(0);
                ItemStack gemStack = OreDictionary.getOres("gemCalcite").get(0);
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), gemStack, ItemDustsSmall.getSmallDustByName("Calcite", 6), null, null, 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, gemStack, ItemDustsSmall.getSmallDustByName("Calcite", 6), null, IC2Items.getItem("cell"), 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, gemStack, ItemDustsSmall.getSmallDustByName("Calcite", 6), null, new ItemStack(Items.bucket), 100, 120));
            }
            catch (Exception e) {
                LogHelper.info("Failed to Load Grinder Recipe for Calcite Ore");
            }
        }

        //Magnetite Ore
        if(OreDictionary.doesOreNameExist("oreMagnetite") && OreDictionary.doesOreNameExist("chunkMagnetite")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("oreMagnetite").get(0);
                ItemStack chunkStack = OreDictionary.getOres("chunkMagnetite").get(0);
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), chunkStack, ItemDustsSmall.getSmallDustByName("Magnetite", 6), null, null, 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, chunkStack, ItemDustsSmall.getSmallDustByName("Magnetite", 6), null, IC2Items.getItem("cell"), 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, chunkStack, ItemDustsSmall.getSmallDustByName("Magnetite", 6), null, new ItemStack(Items.bucket), 100, 120));
            }
            catch (Exception e) {
                LogHelper.info("Failed to Load Grinder Recipe for Magnetite Ore");
            }
        }

        //Graphite Ore
        if(OreDictionary.doesOreNameExist("oreGraphite") && OreDictionary.doesOreNameExist("chunkGraphite")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("oreGraphite").get(0);
                ItemStack chunkStack = OreDictionary.getOres("chunkGraphite").get(0);
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), chunkStack, ItemDustsSmall.getSmallDustByName("Graphite", 6), null, null, 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, chunkStack, ItemDustsSmall.getSmallDustByName("Graphite", 6), null, IC2Items.getItem("cell"), 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, chunkStack, ItemDustsSmall.getSmallDustByName("Graphite", 6), null, new ItemStack(Items.bucket), 100, 120));
            }
            catch (Exception e) {
                LogHelper.info("Failed to Load Grinder Recipe for Graphite Ore");
            }
        }

        //Osmium Ore
        if(OreDictionary.doesOreNameExist("oreOsmium")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("oreOsmium").get(0);
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("osmium", 2), ItemDustsSmall.getSmallDustByName("Osmium", 1), ItemDustsSmall.getSmallDustByName("Osmium", 1), null, 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, ItemDusts.getDustByName("osmium", 2), ItemDustsSmall.getSmallDustByName("Osmium", 1), ItemDustsSmall.getSmallDustByName("Osmium", 1), IC2Items.getItem("cell"), 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, ItemDusts.getDustByName("osmium", 2), ItemDustsSmall.getSmallDustByName("Osmium", 1), ItemDustsSmall.getSmallDustByName("Osmium", 1), new ItemStack(Items.bucket), 100, 120));
            }
            catch (Exception e) {
                LogHelper.info("Failed to Load Grinder Recipe for Osmium Ore");
            }
        }

        //Teslatite Ore
        if(OreDictionary.doesOreNameExist("oreTeslatite") && OreDictionary.doesOreNameExist("dustTeslatite")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("oreTeslatite").get(0);
                ItemStack dustStack = OreDictionary.getOres("dustTeslatite").get(0);
                dustStack.stackSize = 10;
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), dustStack, ItemDustsSmall.getSmallDustByName("Sodalite", 1), ItemDustsSmall.getSmallDustByName("Glowstone", 1), null, 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, dustStack, ItemDustsSmall.getSmallDustByName("Sodalite", 1), ItemDustsSmall.getSmallDustByName("Glowstone", 1), IC2Items.getItem("cell"), 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, dustStack, ItemDustsSmall.getSmallDustByName("Sodalite", 1), ItemDustsSmall.getSmallDustByName("Glowstone", 1), new ItemStack(Items.bucket), 100, 120));
            }
            catch (Exception e) {
                LogHelper.info("Failed to Load Grinder Recipe for Teslatite Ore");
            }
        }

        //Sulfur Ore
        if(OreDictionary.doesOreNameExist("oreSulfur")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("oreSulfur").get(0);
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("sulfur", 2), ItemDustsSmall.getSmallDustByName("Sulfur", 1), ItemDustsSmall.getSmallDustByName("Sulfur", 1), null, 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, ItemDusts.getDustByName("sulfur", 2), ItemDustsSmall.getSmallDustByName("Sulfur", 1), ItemDustsSmall.getSmallDustByName("Sulfur", 1), IC2Items.getItem("cell"), 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, ItemDusts.getDustByName("sulfur", 2), ItemDustsSmall.getSmallDustByName("Sulfur", 1), ItemDustsSmall.getSmallDustByName("Sulfur", 1), new ItemStack(Items.bucket), 100, 120));
            }
            catch (Exception e) {
                LogHelper.info("Failed to Load Grinder Recipe for Sulfur Ore");
            }
        }

        //Saltpeter Ore
        if(OreDictionary.doesOreNameExist("oreSaltpeter")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("oreSaltpeter").get(0);
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("saltpeter", 2), ItemDustsSmall.getSmallDustByName("Saltpeter", 1), ItemDustsSmall.getSmallDustByName("Saltpeter", 1), null, 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, ItemDusts.getDustByName("saltpeter", 2), ItemDustsSmall.getSmallDustByName("Saltpeter", 1), ItemDustsSmall.getSmallDustByName("Saltpeter", 1), IC2Items.getItem("cell"), 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, ItemDusts.getDustByName("saltpeter", 2), ItemDustsSmall.getSmallDustByName("Saltpeter", 1), ItemDustsSmall.getSmallDustByName("Saltpeter", 1), new ItemStack(Items.bucket), 100, 120));
            }
            catch (Exception e) {
                LogHelper.info("Failed to Load Grinder Recipe for Saltpeter Ore");
            }
        }

        //Apatite Ore
        if(OreDictionary.doesOreNameExist("oreApatite") & OreDictionary.doesOreNameExist("gemApatite")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("oreApatite").get(0);
                ItemStack gemStack = OreDictionary.getOres("gemApatite").get(0);
                gemStack.stackSize = 6;
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), gemStack, gemStack, ItemDustsSmall.getSmallDustByName("Phosphorous", 4), null, 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, gemStack, gemStack, ItemDustsSmall.getSmallDustByName("Phosphorous", 4), IC2Items.getItem("cell"), 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, gemStack, gemStack, ItemDustsSmall.getSmallDustByName("Phosphorous", 4), new ItemStack(Items.bucket), 100, 120));
            }
            catch (Exception e) {
                LogHelper.info("Failed to Load Grinder Recipe for Apatite Ore");
            }
        }

        //Nether Quartz Ore
        if(OreDictionary.doesOreNameExist("dustNetherQuartz")) {
            try {
                ItemStack dustStack = OreDictionary.getOres("dustNetherQuartz").get(0);
                dustStack.stackSize = 4;
                RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.quartz_ore, 1), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(Items.quartz, 2), dustStack, ItemDustsSmall.getSmallDustByName("Netherrack", 2), null, 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.quartz_ore, 1), IC2Items.getItem("waterCell"), null, new ItemStack(Items.quartz, 2), dustStack, ItemDustsSmall.getSmallDustByName("Netherrack", 2), IC2Items.getItem("cell"), 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.quartz_ore, 1), new ItemStack(Items.water_bucket), null, new ItemStack(Items.quartz, 2), dustStack, ItemDustsSmall.getSmallDustByName("Netherrack", 2), new ItemStack(Items.bucket), 100, 120));
            }
            catch (Exception e) {
                LogHelper.info("Failed to Load Grinder Recipe for Nether Quartz Ore");
            }
        }

        //Certus Quartz Ore
        if(OreDictionary.doesOreNameExist("oreCertusQuartz")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("oreCertusQuartz").get(0);
                ItemStack gemStack = OreDictionary.getOres("crystalCertusQuartz").get(0);
                ItemStack dustStack = OreDictionary.getOres("dustCertusQuartz").get(0);
                dustStack.stackSize = 2;
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), gemStack, dustStack, null, null, 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, gemStack, dustStack, null, IC2Items.getItem("cell"), 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, gemStack, dustStack, null, new ItemStack(Items.bucket), 100, 120));
            }
            catch (Exception e) {
                LogHelper.info("Failed to Load Grinder Recipe for Certus Quartz Ore");
            }
        }

        //Charged Certus Quartz Ore
        if(OreDictionary.doesOreNameExist("oreChargedCertusQuartz")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("oreChargedCertusQuartz").get(0);
                ItemStack gemStack = OreDictionary.getOres("crystalChargedCertusQuartz").get(0);
                ItemStack dustStack = OreDictionary.getOres("dustCertusQuartz").get(0);
                dustStack.stackSize = 2;
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), gemStack, dustStack, null, null, 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, gemStack, dustStack, null, IC2Items.getItem("cell"), 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, gemStack, dustStack, null, new ItemStack(Items.bucket), 100, 120));
            }
            catch (Exception e) {
                LogHelper.info("Failed to Load Grinder Recipe for Charged Certus Quartz Ore");
            }
        }

        //Amethyst Ore
        if(OreDictionary.doesOreNameExist("oreAmethyst") && OreDictionary.doesOreNameExist("gemAmethyst")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("oreAmethyst").get(0);
                ItemStack gemStack = OreDictionary.getOres("gemAmethyst").get(0);
                gemStack.stackSize = 2;
                ItemStack dustStack = OreDictionary.getOres("gemAmethyst").get(0);
                dustStack.stackSize = 1;
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), gemStack, dustStack, null, null, 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, gemStack, dustStack, null, IC2Items.getItem("cell"), 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, gemStack, dustStack, null, new ItemStack(Items.bucket), 100, 120));
            }
            catch (Exception e) {
                LogHelper.info("Failed to Load Grinder Recipe for Certus Quartz Ore");
            }
        }

        //Topaz Ore
        if(OreDictionary.doesOreNameExist("oreTopaz") && OreDictionary.doesOreNameExist("gemTopaz")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("oreTopaz").get(0);
                ItemStack gemStack = OreDictionary.getOres("gemTopaz").get(0);
                gemStack.stackSize = 2;
                ItemStack dustStack = OreDictionary.getOres("gemTopaz").get(0);
                dustStack.stackSize = 1;
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), gemStack, dustStack, null, null, 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, gemStack, dustStack, null, IC2Items.getItem("cell"), 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, gemStack, dustStack, null, new ItemStack(Items.bucket), 100, 120));
            }
            catch (Exception e) {
                LogHelper.info("Failed to Load Grinder Recipe for Topaz Ore");
            }
        }

        //Tanzanite Ore
        if(OreDictionary.doesOreNameExist("oreTanzanite") && OreDictionary.doesOreNameExist("gemTanzanite")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("oreTanzanite").get(0);
                ItemStack gemStack = OreDictionary.getOres("gemTanzanite").get(0);
                gemStack.stackSize = 2;
                ItemStack dustStack = OreDictionary.getOres("gemTanzanite").get(0);
                dustStack.stackSize = 1;
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), gemStack, dustStack, null, null, 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, gemStack, dustStack, null, IC2Items.getItem("cell"), 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, gemStack, dustStack, null, new ItemStack(Items.bucket), 100, 120));
            }
            catch (Exception e) {
                LogHelper.info("Failed to Load Grinder Recipe for Tanzanite Ore");
            }
        }

        //Malachite Ore
        if(OreDictionary.doesOreNameExist("oreMalachite") && OreDictionary.doesOreNameExist("gemMalachite")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("oreMalachite").get(0);
                ItemStack gemStack = OreDictionary.getOres("gemMalachite").get(0);
                gemStack.stackSize = 2;
                ItemStack dustStack = OreDictionary.getOres("gemMalachite").get(0);
                dustStack.stackSize = 1;
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), gemStack, dustStack, null, null, 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, gemStack, dustStack, null, IC2Items.getItem("cell"), 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, gemStack, dustStack, null, new ItemStack(Items.bucket), 100, 120));
            }
            catch (Exception e) {
                LogHelper.info("Failed to Load Grinder Recipe for Malachite Ore");
            }
        }

        //Galena Ore
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 0), null, new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("galena", 2), ItemDustsSmall.getSmallDustByName("Sulfur", 1), ItemDustsSmall.getSmallDustByName("Silver", 1), null, 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 0), IC2Items.getItem("waterCell"), null, ItemDusts.getDustByName("galena", 2), ItemDustsSmall.getSmallDustByName("Sulfur", 1), ItemDustsSmall.getSmallDustByName("Silver", 1), IC2Items.getItem("cell"), 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 0), new ItemStack(Items.water_bucket), null, ItemDusts.getDustByName("galena", 2), ItemDustsSmall.getSmallDustByName("Sulfur", 1), ItemDustsSmall.getSmallDustByName("Silver", 1), new ItemStack(Items.bucket), 100, 120));

        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 0), null, new FluidStack(ModFluids.fluidMercury, 1000), ItemDusts.getDustByName("galena", 2), ItemDustsSmall.getSmallDustByName("Sulfur", 1), ItemDusts.getDustByName("silver", 1), null, 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 0), ItemCells.getCellByName("mercury", 1), null, ItemDusts.getDustByName("galena", 2), ItemDustsSmall.getSmallDustByName("Sulfur", 1), ItemDusts.getDustByName("silver", 1), IC2Items.getItem("cell"), 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 0), new ItemStack(ModItems.bucketMercury), null, ItemDusts.getDustByName("galena", 2), ItemDustsSmall.getSmallDustByName("Sulfur", 1), ItemDusts.getDustByName("silver", 1), new ItemStack(Items.bucket), 100, 120));

        //Iridium Ore
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 1), null, new FluidStack(FluidRegistry.WATER, 1000), IC2Items.getItem("iridiumOre"), ItemDustsSmall.getSmallDustByName("Iridium", 6), ItemDustsSmall.getSmallDustByName("Platinum", 2), null, 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 1), IC2Items.getItem("waterCell"), null, IC2Items.getItem("iridiumOre"), ItemDustsSmall.getSmallDustByName("Iridium", 6), ItemDustsSmall.getSmallDustByName("Platinum", 2), IC2Items.getItem("cell"), 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 1), new ItemStack(Items.water_bucket), null, IC2Items.getItem("iridiumOre"), ItemDustsSmall.getSmallDustByName("Iridium", 6), ItemDustsSmall.getSmallDustByName("Platinum", 2), new ItemStack(Items.bucket), 100, 120));

        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 1), null, new FluidStack(ModFluids.fluidMercury, 1000), IC2Items.getItem("iridiumOre"), ItemDustsSmall.getSmallDustByName("Iridium", 6), ItemDusts.getDustByName("platinum", 2), null, 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 1), ItemCells.getCellByName("mercury", 1), null, IC2Items.getItem("iridiumOre"), ItemDustsSmall.getSmallDustByName("Iridium", 6), ItemDusts.getDustByName("platinum", 2), IC2Items.getItem("cell"), 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 1), new ItemStack(ModItems.bucketMercury), null, IC2Items.getItem("iridiumOre"), ItemDustsSmall.getSmallDustByName("Iridium", 6), ItemDusts.getDustByName("platinum", 2), new ItemStack(Items.bucket), 100, 120));

        //Ruby Ore
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 2), null, new FluidStack(FluidRegistry.WATER, 1000), ItemGems.getGemByName("ruby", 1), ItemDustsSmall.getSmallDustByName("Ruby", 6), ItemDustsSmall.getSmallDustByName("Chrome", 2), null, 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 2), IC2Items.getItem("waterCell"), null, ItemGems.getGemByName("ruby", 1), ItemDustsSmall.getSmallDustByName("Ruby", 6), ItemDustsSmall.getSmallDustByName("Chrome", 2), IC2Items.getItem("cell"), 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 2), new ItemStack(Items.water_bucket), null, ItemGems.getGemByName("ruby", 1), ItemDustsSmall.getSmallDustByName("Ruby", 6), ItemDustsSmall.getSmallDustByName("Chrome", 2), new ItemStack(Items.bucket), 100, 120));

        //Sapphire Ore
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 3), null, new FluidStack(FluidRegistry.WATER, 1000), ItemGems.getGemByName("sapphire", 1), ItemDustsSmall.getSmallDustByName("Sapphire", 6), ItemDustsSmall.getSmallDustByName("Aluminum", 2), null, 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 3), IC2Items.getItem("waterCell"), null, ItemGems.getGemByName("sapphire", 1), ItemDustsSmall.getSmallDustByName("Sapphire", 6), ItemDustsSmall.getSmallDustByName("Aluminum", 2), IC2Items.getItem("cell"), 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 3), new ItemStack(Items.water_bucket), null, ItemGems.getGemByName("sapphire", 1), ItemDustsSmall.getSmallDustByName("Sapphire", 6), ItemDustsSmall.getSmallDustByName("Aluminum", 2), new ItemStack(Items.bucket), 100, 120));

        //Bauxite Ore
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 4), null, new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("bauxite", 2), ItemDustsSmall.getSmallDustByName("Grossular", 4), ItemDustsSmall.getSmallDustByName("Titanium", 4), null, 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 4), IC2Items.getItem("waterCell"), null, ItemDusts.getDustByName("bauxite", 2), ItemDustsSmall.getSmallDustByName("Grossular", 4), ItemDustsSmall.getSmallDustByName("Titanium", 4), IC2Items.getItem("cell"), 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 4), new ItemStack(Items.water_bucket), null, ItemDusts.getDustByName("bauxite", 2), ItemDustsSmall.getSmallDustByName("Grossular", 4), ItemDustsSmall.getSmallDustByName("Titanium", 4), new ItemStack(Items.bucket), 100, 120));

        //Pyrite Ore
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 5), null, new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("pyrite", 2), ItemDustsSmall.getSmallDustByName("Sulfur", 1), ItemDustsSmall.getSmallDustByName("Phosphorous", 1), null, 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 5), IC2Items.getItem("waterCell"), null, ItemDusts.getDustByName("pyrite", 2), ItemDustsSmall.getSmallDustByName("Sulfur", 1), ItemDustsSmall.getSmallDustByName("Phosphorous", 1), IC2Items.getItem("cell"), 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 5), new ItemStack(Items.water_bucket), null, ItemDusts.getDustByName("pyrite", 2), ItemDustsSmall.getSmallDustByName("Sulfur", 1), ItemDustsSmall.getSmallDustByName("Phosphorous", 1), new ItemStack(Items.bucket), 100, 120));

        //Cinnabar Ore
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 6), null, new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("cinnabar", 2), ItemDustsSmall.getSmallDustByName("Redstone", 1), ItemDustsSmall.getSmallDustByName("Glowstone", 1), null, 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 6), IC2Items.getItem("waterCell"), null, ItemDusts.getDustByName("cinnabar", 2), ItemDustsSmall.getSmallDustByName("Redstone", 1), ItemDustsSmall.getSmallDustByName("Glowstone", 1), IC2Items.getItem("cell"), 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 6), new ItemStack(Items.water_bucket), null, ItemDusts.getDustByName("cinnabar", 2), ItemDustsSmall.getSmallDustByName("Redstone", 1), ItemDustsSmall.getSmallDustByName("Glowstone", 1), new ItemStack(Items.bucket), 100, 120));

        //Sphalerite Ore
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 7), null, new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("sphalerite", 2), ItemDustsSmall.getSmallDustByName("Zinc", 1), ItemDustsSmall.getSmallDustByName("YellowGarnet", 1), null, 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 7), IC2Items.getItem("waterCell"), null, ItemDusts.getDustByName("sphalerite", 2), ItemDustsSmall.getSmallDustByName("Zinc", 1), ItemDustsSmall.getSmallDustByName("YellowGarnet", 1), IC2Items.getItem("cell"), 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 7), new ItemStack(Items.water_bucket), null, ItemDusts.getDustByName("sphalerite", 2), ItemDustsSmall.getSmallDustByName("Zinc", 1), ItemDustsSmall.getSmallDustByName("YellowGarnet", 1), new ItemStack(Items.bucket), 100, 120));

        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 7), null, new FluidStack(ModFluids.fluidSodiumpersulfate, 1000), ItemDusts.getDustByName("sphalerite", 2), ItemDusts.getDustByName("zinc", 1), ItemDustsSmall.getSmallDustByName("YellowGarnet", 1), null, 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 7), ItemCells.getCellByName("sodiumPersulfate", 1), null, ItemDusts.getDustByName("sphalerite", 2), ItemDusts.getDustByName("zinc", 1), ItemDustsSmall.getSmallDustByName("YellowGarnet", 1), IC2Items.getItem("cell"), 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 7), new ItemStack(ModItems.bucketSodiumpersulfate), null, ItemDusts.getDustByName("sphalerite", 2), ItemDusts.getDustByName("zinc", 1), ItemDustsSmall.getSmallDustByName("YellowGarnet", 1), new ItemStack(Items.bucket), 100, 120));

        //Tungsten Ore
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 8), null, new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("tungsten", 2), ItemDustsSmall.getSmallDustByName("Manganese", 1), ItemDustsSmall.getSmallDustByName("Silver", 1), null, 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 8), IC2Items.getItem("waterCell"), null, ItemDusts.getDustByName("tungsten", 2), ItemDustsSmall.getSmallDustByName("Manganese", 1), ItemDustsSmall.getSmallDustByName("Silver", 1), IC2Items.getItem("cell"), 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 8), new ItemStack(Items.water_bucket), null, ItemDusts.getDustByName("tungsten", 2), ItemDustsSmall.getSmallDustByName("Manganese", 1), ItemDustsSmall.getSmallDustByName("Silver", 1), new ItemStack(Items.bucket), 100, 120));

        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 8), null, new FluidStack(ModFluids.fluidMercury, 1000), ItemDusts.getDustByName("tungsten", 2), ItemDustsSmall.getSmallDustByName("Manganese", 1), ItemDusts.getDustByName("silver", 2), null, 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 8), ItemCells.getCellByName("mercury", 1), null, ItemDusts.getDustByName("tungsten", 2), ItemDustsSmall.getSmallDustByName("Manganese", 1), ItemDusts.getDustByName("silver", 2), IC2Items.getItem("cell"), 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 8), new ItemStack(ModItems.bucketMercury), null, ItemDusts.getDustByName("tungsten", 2), ItemDustsSmall.getSmallDustByName("Manganese", 1), ItemDusts.getDustByName("silver", 2), new ItemStack(Items.bucket), 100, 120));

        //Sheldonite Ore
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 9), null, new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("platinum", 2), ItemDustsSmall.getSmallDustByName("Iridium", 1), ItemDustsSmall.getSmallDustByName("Iridium", 1), null, 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 9), IC2Items.getItem("waterCell"), null, ItemDusts.getDustByName("platinum", 2), ItemDustsSmall.getSmallDustByName("Iridium", 1), ItemDustsSmall.getSmallDustByName("Iridium", 1), IC2Items.getItem("cell"), 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 9), new ItemStack(Items.water_bucket), null, ItemDusts.getDustByName("platinum", 2), ItemDustsSmall.getSmallDustByName("Iridium", 1), ItemDustsSmall.getSmallDustByName("Iridium", 1), new ItemStack(Items.bucket), 100, 120));

        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 9), null, new FluidStack(ModFluids.fluidMercury, 1000), ItemDusts.getDustByName("platinum", 3), ItemDustsSmall.getSmallDustByName("Iridium", 1), ItemDustsSmall.getSmallDustByName("Iridium", 1), null, 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 9), ItemCells.getCellByName("mercury", 1), null, ItemDusts.getDustByName("platinum", 3), ItemDustsSmall.getSmallDustByName("Iridium", 1), ItemDustsSmall.getSmallDustByName("Iridium", 1), IC2Items.getItem("cell"), 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 9), new ItemStack(ModItems.bucketMercury), null, ItemDusts.getDustByName("platinum", 3), ItemDustsSmall.getSmallDustByName("Iridium", 1), ItemDustsSmall.getSmallDustByName("Iridium", 1), new ItemStack(Items.bucket), 100, 120));

        //Peridot Ore
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 10), null, new FluidStack(FluidRegistry.WATER, 1000), ItemGems.getGemByName("peridot", 1), ItemDustsSmall.getSmallDustByName("Peridot", 6), ItemDustsSmall.getSmallDustByName("Pyrope", 2), null, 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 10), IC2Items.getItem("waterCell"), null, ItemGems.getGemByName("peridot", 1), ItemDustsSmall.getSmallDustByName("Peridot", 6), ItemDustsSmall.getSmallDustByName("Pyrope", 2), IC2Items.getItem("cell"), 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 10), new ItemStack(Items.water_bucket), null, ItemGems.getGemByName("peridot", 1), ItemDustsSmall.getSmallDustByName("Peridot", 6), ItemDustsSmall.getSmallDustByName("Pyrope", 2), new ItemStack(Items.bucket), 100, 120));

        //Sodalite Ore
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 11), null, new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("sodalite", 12), ItemDustsSmall.getSmallDustByName("Lazurite", 4), ItemDustsSmall.getSmallDustByName("Lapis", 4), null, 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 11), IC2Items.getItem("waterCell"), null, ItemDusts.getDustByName("sodalite", 12), ItemDustsSmall.getSmallDustByName("Lazurite", 4), ItemDustsSmall.getSmallDustByName("Lapis", 4), IC2Items.getItem("cell"), 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 11), new ItemStack(Items.water_bucket), null, ItemDusts.getDustByName("sodalite", 12), ItemDustsSmall.getSmallDustByName("Lazurite", 4), ItemDustsSmall.getSmallDustByName("Lapis", 4), new ItemStack(Items.bucket), 100, 120));

        //Tetrahedrite Ore
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 12), null, new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("tetrahedrite", 2), ItemDustsSmall.getSmallDustByName("Antimony", 1), ItemDustsSmall.getSmallDustByName("Zinc", 1), null, 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 12), IC2Items.getItem("waterCell"), null, ItemDusts.getDustByName("tetrahedrite", 2), ItemDustsSmall.getSmallDustByName("Antimony", 1), ItemDustsSmall.getSmallDustByName("Zinc", 1), IC2Items.getItem("cell"), 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 12), new ItemStack(Items.water_bucket), null, ItemDusts.getDustByName("tetrahedrite", 2), ItemDustsSmall.getSmallDustByName("Antimony", 1), ItemDustsSmall.getSmallDustByName("Zinc", 1), new ItemStack(Items.bucket), 100, 120));

        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 12), null, new FluidStack(ModFluids.fluidSodiumpersulfate, 1000), ItemDusts.getDustByName("tetrahedrite", 3), ItemDustsSmall.getSmallDustByName("Antimony", 1), ItemDustsSmall.getSmallDustByName("Zinc", 1), null, 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 12), ItemCells.getCellByName("sodiumPersulfate", 1), null, ItemDusts.getDustByName("tetrahedrite", 3), ItemDustsSmall.getSmallDustByName("Antimony", 1), ItemDustsSmall.getSmallDustByName("Zinc", 1), IC2Items.getItem("cell"), 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 12), new ItemStack(ModItems.bucketSodiumpersulfate), null, ItemDusts.getDustByName("tetrahedrite", 3), ItemDustsSmall.getSmallDustByName("Antimony", 1), ItemDustsSmall.getSmallDustByName("Zinc", 1), new ItemStack(Items.bucket), 100, 120));
    }
}

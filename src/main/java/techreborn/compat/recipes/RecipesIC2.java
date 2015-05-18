package techreborn.compat.recipes;

import cpw.mods.fml.common.Mod;
import ic2.api.recipe.RecipeInputOreDict;
import ic2.api.recipe.Recipes;
import ic2.core.IC2;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import ic2.api.item.IC2Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;
import techreborn.api.recipe.RecipeHanderer;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;
import techreborn.init.ModItems;
import techreborn.recipes.AssemblingMachineRecipe;
import techreborn.util.CraftingHelper;
import techreborn.util.LogHelper;
import techreborn.util.RecipeRemover;

public class RecipesIC2 {
	public static ConfigTechReborn config;
	
	public static void init()
	{
		removeIc2Recipes();
        addShappedIc2Recipes();
        addShapedTrRecipes();
        addTRMaceratorRecipes();
        addTROreWashingRecipes();
        addTRThermalCentrifugeRecipes();
        addAssemblingMachineRecipes();
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

	public static void addShapedTrRecipes()
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

    }

    public static void addTRMaceratorRecipes() {
        //Macerator

        if(OreDictionary.doesOreNameExist("oreAluminum")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreAluminum"), null, new ItemStack(ModItems.crushedOre, 2, 0));
        }
        if(OreDictionary.doesOreNameExist("oreArdite")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreArdite"), null, new ItemStack(ModItems.crushedOre, 2, 1));
        }
        if(OreDictionary.doesOreNameExist("oreBauxite")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreBauxite"), null, new ItemStack(ModItems.crushedOre, 2, 2));
        }
        if(OreDictionary.doesOreNameExist("oreCadmium")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreCadmium"), null, new ItemStack(ModItems.crushedOre, 2, 3));
        }
        if(OreDictionary.doesOreNameExist("oreCinnabar")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreCinnabar"), null, new ItemStack(ModItems.crushedOre, 2, 4));
        }
        if(OreDictionary.doesOreNameExist("oreCobalt")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreCobalt"), null, new ItemStack(ModItems.crushedOre, 2, 5));
        }
        if(OreDictionary.doesOreNameExist("oreDarkIron")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreDarkIron"), null, new ItemStack(ModItems.crushedOre, 2, 6));
        }
        if(OreDictionary.doesOreNameExist("oreIndium")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreIndium"), null, new ItemStack(ModItems.crushedOre, 2, 7));
        }
        if(OreDictionary.doesOreNameExist("oreIridium")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreIridium"), null, new ItemStack(ModItems.crushedOre, 2, 8));
        }
        if(OreDictionary.doesOreNameExist("oreNickel")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreNickel"), null, new ItemStack(ModItems.crushedOre, 2, 9));
        }
        if(OreDictionary.doesOreNameExist("oreOsmium")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreOsmium"), null, new ItemStack(ModItems.crushedOre, 2, 10));
        }
        if(OreDictionary.doesOreNameExist("orePlatinum")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("orePlatinum"), null, new ItemStack(ModItems.crushedOre, 2, 11));
        }
        if(OreDictionary.doesOreNameExist("orePyrite")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("orePyrite"), null, new ItemStack(ModItems.crushedOre, 2, 12));
        }
        if(OreDictionary.doesOreNameExist("oreSphalerite")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreSphalerite"), null, new ItemStack(ModItems.crushedOre, 2, 13));
        }
        if(OreDictionary.doesOreNameExist("oreTetrahedrite")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreTetrahedrite"), null, new ItemStack(ModItems.crushedOre, 2, 14));
        }
        if(OreDictionary.doesOreNameExist("oreTungsten")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreTungsten"), null, new ItemStack(ModItems.crushedOre, 2, 15));
        }
        if(OreDictionary.doesOreNameExist("oreGalena")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreGalena"), null, new ItemStack(ModItems.crushedOre, 2, 16));
        }


        if(OreDictionary.doesOreNameExist("oreRedstone")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreRedstone"), null, new ItemStack(Items.redstone, 10));
        }
        if(OreDictionary.doesOreNameExist("oreLapis")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreLapis"), null, new ItemStack(ModItems.dusts, 12, 40));
        }
        if(OreDictionary.doesOreNameExist("oreDiamond")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreDiamond"), null, new ItemStack(ModItems.dusts, 2, 25));
        }
        if(OreDictionary.doesOreNameExist("oreEmerald")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreEmerald"), null, new ItemStack(ModItems.dusts, 2, 27));
        }
        if(OreDictionary.doesOreNameExist("oreRuby")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreRuby"), null, new ItemStack(ModItems.dusts, 2, 64));
        }
        if(OreDictionary.doesOreNameExist("oreSapphire")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreSapphire"), null, new ItemStack(ModItems.dusts, 2, 66));
        }
        if(OreDictionary.doesOreNameExist("orePeridot")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("orePeridot"), null, new ItemStack(ModItems.dusts, 2, 56));
        }
        if(OreDictionary.doesOreNameExist("oreSulfur")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreSulfur"), null, new ItemStack(ModItems.dusts, 8, 73));
        }
        if(OreDictionary.doesOreNameExist("oreSaltpeter")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreSaltpeter"), null, new ItemStack(ModItems.dusts, 8, 65));
        }
        if(OreDictionary.doesOreNameExist("oreTeslatite")) {
            ItemStack teslatiteStack = OreDictionary.getOres("dustTeslatite").get(0);
            teslatiteStack.stackSize = 10;
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreTeslatite"), null, teslatiteStack);
        }
        if(OreDictionary.doesOreNameExist("oreMithril")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreMithril"), null, new ItemStack(ModItems.dusts, 2, 50));
        }
        if(OreDictionary.doesOreNameExist("oreVinteum")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreVinteum"), null, new ItemStack(ModItems.dusts, 2, 81));
        }
        if(OreDictionary.doesOreNameExist("limestone")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("limestone"), null, new ItemStack(ModItems.dusts, 1, 43));
        }
        if(OreDictionary.doesOreNameExist("stoneNetherrack")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("stoneNetherrack"), null, new ItemStack(ModItems.dusts, 2, 51));
        }
        if(OreDictionary.doesOreNameExist("stoneEndstone")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("stoneEndstone"), null, new ItemStack(ModItems.dusts, 2, 51));
        }
        if(OreDictionary.doesOreNameExist("stoneRedrock")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("stoneRedrock"), null, new ItemStack(ModItems.dusts, 2, 63));
        }
        if(OreDictionary.doesOreNameExist("oreMagnetite")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreMagnetite"), null, new ItemStack(ModItems.dusts, 2, 46));
        }
        if(OreDictionary.doesOreNameExist("oreLodestone")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreLodestone"), null, new ItemStack(ModItems.dusts, 2, 44));
        }
        if(OreDictionary.doesOreNameExist("oreTellurium")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreTellurium"), null, new ItemStack(ModItems.dusts, 2, 74));
        }
        if(OreDictionary.doesOreNameExist("oreSilicon")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreSilicon"), null, new ItemStack(ModItems.dusts, 2, 67));
        }
        if(OreDictionary.doesOreNameExist("oreVoidstone")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreVoidstone"), null, new ItemStack(ModItems.dusts, 2, 82));
        }
        if(OreDictionary.doesOreNameExist("oreCalcite")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreCalcite"), null, new ItemStack(ModItems.dusts, 2, 14));
        }
        if(OreDictionary.doesOreNameExist("oreSodalite")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreSodalite"), null, new ItemStack(ModItems.dusts, 12, 69));
        }
        if(OreDictionary.doesOreNameExist("oreGraphite")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreGraphite"), null, new ItemStack(ModItems.dusts, 12, 33));
        }
        if(OreDictionary.doesOreNameExist("blockMarble")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("blockMarble"), null, new ItemStack(ModItems.dusts, 12, 49));
        }
        if(OreDictionary.doesOreNameExist("blockBasalt")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("blockBasalt"), null, new ItemStack(ModItems.dusts, 12, 8));
        }
    }

    public static void addTROreWashingRecipes() {
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

        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedAluminum"), aluminumHeat, new ItemStack(ModItems.tinyDusts, 1, 9), new ItemStack(ModItems.dusts, 1, 2), IC2Items.getItem("stoneDust"));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedArdite"), arditeHeat, new ItemStack(ModItems.tinyDusts, 1, 6), new ItemStack(ModItems.dusts, 1, 6), IC2Items.getItem("stoneDust"));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedBauxite"), bauxiteHeat, new ItemStack(ModItems.tinyDusts, 1, 2), new ItemStack(ModItems.dusts, 1, 9), IC2Items.getItem("stoneDust"));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedCadmium"), cadmiumHeat, new ItemStack(ModItems.tinyDusts, 1, 13), new ItemStack(ModItems.dusts, 1, 13), IC2Items.getItem("stoneDust"));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedCinnabar"), cinnabarHeat, new ItemStack(ModItems.tinyDusts, 1, 66), new ItemStack(ModItems.dusts, 1, 17), IC2Items.getItem("stoneDust"));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedCobalt"), cobaltHeat, new ItemStack(ModItems.tinyDusts, 1, 20), new ItemStack(ModItems.dusts, 1, 20), IC2Items.getItem("stoneDust"));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedDarkIron"), darkIronHeat, new ItemStack(ModItems.tinyDusts, 1, 40), new ItemStack(ModItems.dusts, 1, 24), IC2Items.getItem("stoneDust"));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedIndium"), indiumHeat, new ItemStack(ModItems.tinyDusts, 1, 37), new ItemStack(ModItems.dusts, 1, 35), IC2Items.getItem("stoneDust"));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedIridium"), iridiumHeat, new ItemStack(ModItems.tinyDusts, 1, 60), new ItemStack(ModItems.dusts, 1, 37), IC2Items.getItem("stoneDust"));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedNickel"), nickelHeat, new ItemStack(ModItems.tinyDusts, 1, 40), new ItemStack(ModItems.dusts, 1, 53), IC2Items.getItem("stoneDust"));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedOsmium"), osmiumHeat, new ItemStack(ModItems.tinyDusts, 1, 57), new ItemStack(ModItems.dusts, 1, 55), IC2Items.getItem("stoneDust"));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPlatinum"), platinumHeat, new ItemStack(ModItems.tinyDusts, 1, 39), new ItemStack(ModItems.dusts, 1, 58), IC2Items.getItem("stoneDust"));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPyrite"), pyriteHeat, new ItemStack(ModItems.tinyDusts, 1, 76), new ItemStack(ModItems.dusts, 1, 60), IC2Items.getItem("stoneDust"));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedSphalerite"), sphaleriteHeat, new ItemStack(ModItems.tinyDusts, 1, 87), new ItemStack(ModItems.dusts, 1, 71), IC2Items.getItem("stoneDust"));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedTetrahedrite"), tetrahedriteHeat, new ItemStack(ModItems.tinyDusts, 1, 5), new ItemStack(ModItems.dusts, 1, 76), IC2Items.getItem("stoneDust"));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedTungsten"), tungstenHeat, new ItemStack(ModItems.tinyDusts, 1, 49), new ItemStack(ModItems.dusts, 1, 79), IC2Items.getItem("stoneDust"));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedGalena"), galenaHeat, new ItemStack(ModItems.tinyDusts, 1, 76), new ItemStack(ModItems.dusts, 1, 85), IC2Items.getItem("stoneDust"));

        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedAluminum"), aluminumHeat, new ItemStack(ModItems.tinyDusts, 1, 9), new ItemStack(ModItems.dusts, 1, 2));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedArdite"), arditeHeat, new ItemStack(ModItems.tinyDusts, 1, 6), new ItemStack(ModItems.dusts, 1, 6));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedBauxite"), bauxiteHeat, new ItemStack(ModItems.tinyDusts, 1, 2), new ItemStack(ModItems.dusts, 1, 9));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedCadmium"), cadmiumHeat, new ItemStack(ModItems.tinyDusts, 1, 13), new ItemStack(ModItems.dusts, 1, 13));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedCinnabar"), cinnabarHeat, new ItemStack(ModItems.tinyDusts, 1, 66), new ItemStack(ModItems.dusts, 1, 17));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedCobalt"), cobaltHeat, new ItemStack(ModItems.tinyDusts, 1, 20), new ItemStack(ModItems.dusts, 1, 20));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedDarkIron"), darkIronHeat, new ItemStack(ModItems.tinyDusts, 1, 40), new ItemStack(ModItems.dusts, 1, 24));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedIndium"), indiumHeat, new ItemStack(ModItems.tinyDusts, 1, 37), new ItemStack(ModItems.dusts, 1, 35));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedIridium"), iridiumHeat, new ItemStack(ModItems.tinyDusts, 1, 60), new ItemStack(ModItems.dusts, 1, 37));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedNickel"), nickelHeat, new ItemStack(ModItems.tinyDusts, 1, 40), new ItemStack(ModItems.dusts, 1, 53));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedOsmium"), osmiumHeat, new ItemStack(ModItems.tinyDusts, 1, 57), new ItemStack(ModItems.dusts, 1, 55));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedPlatinum"), platinumHeat, new ItemStack(ModItems.tinyDusts, 1, 39), new ItemStack(ModItems.dusts, 1, 58));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedPyrite"), pyriteHeat, new ItemStack(ModItems.tinyDusts, 1, 76), new ItemStack(ModItems.dusts, 1, 60));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedSphalerite"), sphaleriteHeat, new ItemStack(ModItems.tinyDusts, 1, 87), new ItemStack(ModItems.dusts, 1, 71));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedTetrahedrite"), tetrahedriteHeat, new ItemStack(ModItems.tinyDusts, 1, 5), new ItemStack(ModItems.dusts, 1, 76));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedTungsten"), tungstenHeat, new ItemStack(ModItems.tinyDusts, 1, 49), new ItemStack(ModItems.dusts, 1, 79));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedGalena"), galenaHeat, new ItemStack(ModItems.tinyDusts, 1, 76), new ItemStack(ModItems.dusts, 1, 85));
    }

    public static void addAssemblingMachineRecipes() {
        //Ender Eye
        RecipeHanderer.addRecipe(new AssemblingMachineRecipe(new ItemStack(Items.ender_pearl, 1), new ItemStack(Items.blaze_powder), new ItemStack(Items.ender_eye), 120, 5));
        RecipeHanderer.addRecipe(new AssemblingMachineRecipe(new ItemStack(Items.ender_pearl, 6), new ItemStack(Items.blaze_rod), new ItemStack(Items.ender_eye, 6), 120, 5));

        //Redstone Lamp
        RecipeHanderer.addRecipe(new AssemblingMachineRecipe(new ItemStack(Items.redstone, 4), new ItemStack(Items.glowstone_dust, 4), new ItemStack(Blocks.redstone_lamp), 120, 5));

        //Note Block
        RecipeHanderer.addRecipe(new AssemblingMachineRecipe(new ItemStack(Blocks.planks, 8), new ItemStack(Items.redstone, 1), new ItemStack(Blocks.noteblock), 120, 5));

        //Jukebox
        RecipeHanderer.addRecipe(new AssemblingMachineRecipe(new ItemStack(Items.diamond, 1), new ItemStack(Blocks.planks, 8), new ItemStack(Blocks.jukebox), 120, 5));

        //Clock
        RecipeHanderer.addRecipe(new AssemblingMachineRecipe(new ItemStack(Items.redstone, 1), new ItemStack(Items.gold_ingot, 4), new ItemStack(Items.clock), 120, 5));

        //Compass
        RecipeHanderer.addRecipe(new AssemblingMachineRecipe(new ItemStack(Items.redstone, 1), new ItemStack(Items.iron_ingot, 4), new ItemStack(Items.clock), 120, 5));

        //Lead
        RecipeHanderer.addRecipe(new AssemblingMachineRecipe(new ItemStack(Items.string, 1), new ItemStack(Items.slime_ball, 1), new ItemStack(Items.lead, 2), 120, 5));

        //Circuit Parts
        RecipeHanderer.addRecipe(new AssemblingMachineRecipe(new ItemStack(Items.glowstone_dust), new ItemStack(ModItems.dusts, 1, 41), new ItemStack(ModItems.parts, 1, 0), 120, 5));
        RecipeHanderer.addRecipe(new AssemblingMachineRecipe(new ItemStack(Items.glowstone_dust), new ItemStack(ModItems.dusts, 1, 40), new ItemStack(ModItems.parts, 1, 0), 120, 5));

        //Electronic Circuit
        RecipeHanderer.addRecipe(new AssemblingMachineRecipe(new ItemStack(ModItems.parts, 1, 1), new ItemStack(IC2Items.getItem("insulatedCopperCableItem").getItem(), 3), IC2Items.getItem("electronicCircuit"), 120, 5));

        //Advanced Circuit
        RecipeHanderer.addRecipe(new AssemblingMachineRecipe(new ItemStack(ModItems.parts, 1, 2), new ItemStack(ModItems.parts, 2, 0), IC2Items.getItem("advancedCircuit"), 120, 5));

        //Energy Flow Circuit
        RecipeHanderer.addRecipe(new AssemblingMachineRecipe(new ItemStack(ModItems.parts, 1, 3), new ItemStack(IC2Items.getItem("lapotronCrystal").getItem(), 1, OreDictionary.WILDCARD_VALUE), new ItemStack(ModItems.parts, 1, 4), 120, 5));

        //Data Control Circuit
        RecipeHanderer.addRecipe(new AssemblingMachineRecipe(new ItemStack(ModItems.parts, 1, 3), new ItemStack(ModItems.parts, 1, 7), new ItemStack(ModItems.parts, 1, 5), 120, 5));

        //Data Storage Circuit
        RecipeHanderer.addRecipe(new AssemblingMachineRecipe(new ItemStack(ModItems.plate, 8, 9), IC2Items.getItem("advancedCircuit"), new ItemStack(ModItems.parts, 1, 7), 120, 5));
        RecipeHanderer.addRecipe(new AssemblingMachineRecipe(new ItemStack(ModItems.plate, 8, 20), IC2Items.getItem("advancedCircuit"), new ItemStack(ModItems.parts, 1, 7), 120, 5));

        //Data Orb
        RecipeHanderer.addRecipe(new AssemblingMachineRecipe(new ItemStack(ModItems.parts, 1, 5), new ItemStack(ModItems.parts, 8, 7), new ItemStack(ModItems.parts, 1, 6), 120, 5));

        //Basic Circuit Board
        RecipeHanderer.addRecipe(new AssemblingMachineRecipe(new ItemStack(ModItems.plate, 1, 0), new ItemStack(ModItems.plate, 2, 34), new ItemStack(ModItems.parts, 2, 1), 120, 5));
        RecipeHanderer.addRecipe(new AssemblingMachineRecipe(new ItemStack(ModItems.plate, 1, 13), new ItemStack(ModItems.plate, 2, 34), new ItemStack(ModItems.parts, 2, 1), 120, 5));

        //Advanced Circuit Board
        RecipeHanderer.addRecipe(new AssemblingMachineRecipe(new ItemStack(ModItems.plate, 2, 34), IC2Items.getItem("electronicCircuit"), new ItemStack(ModItems.parts, 1, 2), 120, 5));
        RecipeHanderer.addRecipe(new AssemblingMachineRecipe(new ItemStack(ModItems.plate, 4, 34), new ItemStack(ModItems.plate, 1, 26), new ItemStack(ModItems.parts, 2, 2), 120, 5));

        //Processor Circuit Board
        RecipeHanderer.addRecipe(new AssemblingMachineRecipe(new ItemStack(ModItems.plate, 1, 21), IC2Items.getItem("advancedCircuit"), new ItemStack(ModItems.parts, 1, 3), 120, 5));

        //Frequency Transmitter
        RecipeHanderer.addRecipe(new AssemblingMachineRecipe(IC2Items.getItem("electronicCircuit"), IC2Items.getItem("insulatedCopperCableItem"), IC2Items.getItem("frequencyTransmitter"), 120, 5));

        //Wind Mill
        RecipeHanderer.addRecipe(new AssemblingMachineRecipe(new ItemStack(ModItems.plate, 2, 16), IC2Items.getItem("generator"), IC2Items.getItem("windMill"), 120, 5));
        RecipeHanderer.addRecipe(new AssemblingMachineRecipe(new ItemStack(IC2Items.getItem("carbonPlate").getItem(), 4), IC2Items.getItem("generator"), IC2Items.getItem("windMill"), 120, 5));

        //Water Mill
        RecipeHanderer.addRecipe(new AssemblingMachineRecipe(new ItemStack(ModItems.plate, 4, 0), IC2Items.getItem("generator"), IC2Items.getItem("waterMill"), 120, 5));

        //Industrial TNT
        RecipeHanderer.addRecipe(new AssemblingMachineRecipe(new ItemStack(ModItems.dusts, 5, 31), new ItemStack(Blocks.tnt), new ItemStack(IC2Items.getItem("industrialTnt").getItem(), 5), 120, 5));

    }
}

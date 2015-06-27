package techreborn.compat.recipes;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import ic2.api.item.IC2Items;
import ic2.api.recipe.RecipeInputOreDict;
import ic2.api.recipe.Recipes;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;
import techreborn.compat.ICompatModule;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;
import techreborn.init.ModItems;
import techreborn.items.ItemCrushedOre;
import techreborn.items.ItemDustsTiny;
import techreborn.items.ItemDusts;
import techreborn.items.ItemIngots;
import techreborn.items.ItemParts;
import techreborn.items.ItemPlates;
import techreborn.items.ItemPurifiedCrushedOre;
import techreborn.util.CraftingHelper;
import techreborn.util.LogHelper;
import techreborn.util.RecipeRemover;

public class RecipesIC2 implements ICompatModule {

	@Override
	public void preInit(FMLPreInitializationEvent event) {

	}

	@Override
	public void init(FMLInitializationEvent event) {
		removeIc2Recipes();
		addShappedIc2Recipes();
		addTRMaceratorRecipes();
		addTROreWashingRecipes();
		addTRThermalCentrifugeRecipes();
		addMetalFormerRecipes();
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {

	}

	@Override
	public void serverStarting(FMLServerStartingEvent event) {

	}

	static void removeIc2Recipes() {
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

	static void addShappedIc2Recipes() {
		Item drill = IC2Items.getItem("miningDrill").getItem();
		ItemStack drillStack = new ItemStack(drill, 1, OreDictionary.WILDCARD_VALUE); 
		
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
					'B', drillStack,
					'C', IC2Items.getItem("advancedCircuit"));

		if (ConfigTechReborn.ExpensiveSolar)
			CraftingHelper.addShapedOreRecipe(IC2Items.getItem("solarPanel"),
					"PPP", "SZS", "CGC",
					'P', "paneGlass",
					'S', new ItemStack(ModItems.parts, 1, 1),
					'Z', IC2Items.getItem("carbonPlate"),
					'G', IC2Items.getItem("generator"),
					'C', IC2Items.getItem("electronicCircuit"));


		CraftingHelper.addShapedOreRecipe(ItemParts.getPartByName("iridiumAlloyIngot"),
				"IAI", "ADA", "IAI",
				'I', ItemIngots.getIngotByName("iridium"),
				'D', ItemDusts.getDustByName("diamond"),
				'A', IC2Items.getItem("advancedAlloy"));

		LogHelper.info("Added Expensive IC2 Recipes");
	}

	static void addTRMaceratorRecipes() {
		//Macerator

		if (OreDictionary.doesOreNameExist("oreAluminum")) {
			Recipes.macerator.addRecipe(new RecipeInputOreDict("oreAluminum"), null, ItemCrushedOre.getCrushedOreByName("Aluminum", 2));
		}
		if (OreDictionary.doesOreNameExist("oreArdite")) {
			Recipes.macerator.addRecipe(new RecipeInputOreDict("oreArdite"), null, ItemCrushedOre.getCrushedOreByName("Ardite", 2));
		}
		if (OreDictionary.doesOreNameExist("oreBauxite")) {
			Recipes.macerator.addRecipe(new RecipeInputOreDict("oreBauxite"), null, ItemCrushedOre.getCrushedOreByName("Bauxite", 2));
		}
		if (OreDictionary.doesOreNameExist("oreCadmium")) {
			Recipes.macerator.addRecipe(new RecipeInputOreDict("oreCadmium"), null, ItemCrushedOre.getCrushedOreByName("Cadmium", 2));
		}
		if (OreDictionary.doesOreNameExist("oreCinnabar")) {
			Recipes.macerator.addRecipe(new RecipeInputOreDict("oreCinnabar"), null, ItemCrushedOre.getCrushedOreByName("Cinnabar", 2));
		}
		if (OreDictionary.doesOreNameExist("oreCobalt")) {
			Recipes.macerator.addRecipe(new RecipeInputOreDict("oreCobalt"), null, ItemCrushedOre.getCrushedOreByName("Cobalt", 2));
		}
		if (OreDictionary.doesOreNameExist("oreDarkIron")) {
			Recipes.macerator.addRecipe(new RecipeInputOreDict("oreDarkIron"), null, ItemCrushedOre.getCrushedOreByName("DarkIron", 2));
		}
		if (OreDictionary.doesOreNameExist("oreIndium")) {
			Recipes.macerator.addRecipe(new RecipeInputOreDict("oreIndium"), null, ItemCrushedOre.getCrushedOreByName("Indium", 2));
		}
		if (OreDictionary.doesOreNameExist("oreIridium")) {
			Recipes.macerator.addRecipe(new RecipeInputOreDict("oreIridium"), null, ItemCrushedOre.getCrushedOreByName("Iridium", 2));
		}
		if (OreDictionary.doesOreNameExist("oreNickel")) {
			Recipes.macerator.addRecipe(new RecipeInputOreDict("oreNickel"), null, ItemCrushedOre.getCrushedOreByName("Nickel", 2));
		}
		if (OreDictionary.doesOreNameExist("orePlatinum")) {
			Recipes.macerator.addRecipe(new RecipeInputOreDict("orePlatinum"), null, ItemCrushedOre.getCrushedOreByName("Platinum", 2));
		}
		if (OreDictionary.doesOreNameExist("orePyrite")) {
			Recipes.macerator.addRecipe(new RecipeInputOreDict("orePyrite"), null, ItemCrushedOre.getCrushedOreByName("Pyrite", 2));
		}
		if (OreDictionary.doesOreNameExist("oreSphalerite")) {
			Recipes.macerator.addRecipe(new RecipeInputOreDict("oreSphalerite"), null, ItemCrushedOre.getCrushedOreByName("Sphalerite", 2));
		}
		if (OreDictionary.doesOreNameExist("oreTetrahedrite")) {
			Recipes.macerator.addRecipe(new RecipeInputOreDict("oreTetrahedrite"), null, ItemCrushedOre.getCrushedOreByName("Tetrahedrite", 2));
		}
		if (OreDictionary.doesOreNameExist("oreTungsten")) {
			Recipes.macerator.addRecipe(new RecipeInputOreDict("oreTungsten"), null, ItemCrushedOre.getCrushedOreByName("Tungsten", 2));
		}
		if (OreDictionary.doesOreNameExist("oreGalena")) {
			Recipes.macerator.addRecipe(new RecipeInputOreDict("oreGalena"), null, ItemCrushedOre.getCrushedOreByName("Galena", 2));
		}


		if (OreDictionary.doesOreNameExist("oreRedstone")) {
			Recipes.macerator.addRecipe(new RecipeInputOreDict("oreRedstone"), null, new ItemStack(Items.redstone, 10));
		}
		if (OreDictionary.doesOreNameExist("oreLapis")) {
			Recipes.macerator.addRecipe(new RecipeInputOreDict("oreLapis"), null, ItemDusts.getDustByName("lapis", 12));
		}
		if (OreDictionary.doesOreNameExist("oreDiamond")) {
			Recipes.macerator.addRecipe(new RecipeInputOreDict("oreDiamond"), null, ItemDusts.getDustByName("diamond", 2));
		}
		if (OreDictionary.doesOreNameExist("oreEmerald")) {
			Recipes.macerator.addRecipe(new RecipeInputOreDict("oreEmerald"), null, ItemDusts.getDustByName("emerald", 2));
		}
		if (OreDictionary.doesOreNameExist("oreRuby")) {
			Recipes.macerator.addRecipe(new RecipeInputOreDict("oreRuby"), null, ItemDusts.getDustByName("ruby", 2));
		}
		if (OreDictionary.doesOreNameExist("oreSapphire")) {
			Recipes.macerator.addRecipe(new RecipeInputOreDict("oreSapphire"), null, ItemDusts.getDustByName("sapphire", 2));
		}
		if (OreDictionary.doesOreNameExist("orePeridot")) {
			Recipes.macerator.addRecipe(new RecipeInputOreDict("orePeridot"), null, ItemDusts.getDustByName("peridot", 2));
		}
		if (OreDictionary.doesOreNameExist("oreSulfur")) {
			Recipes.macerator.addRecipe(new RecipeInputOreDict("oreSulfur"), null, ItemDusts.getDustByName("sulfur", 2));
		}
		if (OreDictionary.doesOreNameExist("oreSaltpeter")) {
			Recipes.macerator.addRecipe(new RecipeInputOreDict("oreSaltpeter"), null, ItemDusts.getDustByName("saltpeter", 2));
		}
		if (OreDictionary.doesOreNameExist("oreTeslatite")) {
			ItemStack teslatiteStack = OreDictionary.getOres("dustTeslatite").get(0);
			teslatiteStack.stackSize = 10;
			Recipes.macerator.addRecipe(new RecipeInputOreDict("oreTeslatite"), null, teslatiteStack);
		}
		if (OreDictionary.doesOreNameExist("oreMithril")) {
			Recipes.macerator.addRecipe(new RecipeInputOreDict("oreMithril"), null, ItemDusts.getDustByName("mithril", 2));
		}
		if (OreDictionary.doesOreNameExist("oreVinteum")) {
			Recipes.macerator.addRecipe(new RecipeInputOreDict("oreVinteum"), null, ItemDusts.getDustByName("vinteum", 2));
		}
		if (OreDictionary.doesOreNameExist("limestone")) {
			Recipes.macerator.addRecipe(new RecipeInputOreDict("limestone"), null, ItemDusts.getDustByName("limestone", 2));
		}
		if (OreDictionary.doesOreNameExist("stoneNetherrack")) {
			Recipes.macerator.addRecipe(new RecipeInputOreDict("stoneNetherrack"), null, ItemDusts.getDustByName("netherrack", 2));
		}
		if (OreDictionary.doesOreNameExist("stoneEndstone")) {
			Recipes.macerator.addRecipe(new RecipeInputOreDict("stoneEndstone"), null, ItemDusts.getDustByName("endstone", 2));
		}
		if (OreDictionary.doesOreNameExist("stoneRedrock")) {
			Recipes.macerator.addRecipe(new RecipeInputOreDict("stoneRedrock"), null, ItemDusts.getDustByName("redrock", 2));
		}
		if (OreDictionary.doesOreNameExist("oreMagnetite")) {
			Recipes.macerator.addRecipe(new RecipeInputOreDict("oreMagnetite"), null, ItemDusts.getDustByName("magnetite", 2));
		}
		if (OreDictionary.doesOreNameExist("oreLodestone")) {
			Recipes.macerator.addRecipe(new RecipeInputOreDict("oreLodestone"), null, ItemDusts.getDustByName("lodestone", 2));
		}
		if (OreDictionary.doesOreNameExist("oreTellurium")) {
			Recipes.macerator.addRecipe(new RecipeInputOreDict("oreTellurium"), null, ItemDusts.getDustByName("tellurium", 2));
		}
		if (OreDictionary.doesOreNameExist("oreSilicon")) {
			Recipes.macerator.addRecipe(new RecipeInputOreDict("oreSilicon"), null, ItemDusts.getDustByName("silicon", 2));
		}
		if (OreDictionary.doesOreNameExist("oreVoidstone")) {
			Recipes.macerator.addRecipe(new RecipeInputOreDict("oreVoidstone"), null, ItemDusts.getDustByName("voidstone", 2));
		}
		if (OreDictionary.doesOreNameExist("oreCalcite")) {
			Recipes.macerator.addRecipe(new RecipeInputOreDict("oreCalcite"), null, ItemDusts.getDustByName("calcite", 2));
		}
		if (OreDictionary.doesOreNameExist("oreSodalite")) {
			Recipes.macerator.addRecipe(new RecipeInputOreDict("oreSodalite"), null, ItemDusts.getDustByName("sodalite", 2));
		}
		if (OreDictionary.doesOreNameExist("oreGraphite")) {
			Recipes.macerator.addRecipe(new RecipeInputOreDict("oreGraphite"), null, ItemDusts.getDustByName("graphite", 2));
		}
		if (OreDictionary.doesOreNameExist("blockMarble")) {
			Recipes.macerator.addRecipe(new RecipeInputOreDict("blockMarble"), null, ItemDusts.getDustByName("marble", 2));
		}
		if (OreDictionary.doesOreNameExist("blockBasalt")) {
			Recipes.macerator.addRecipe(new RecipeInputOreDict("blockBasalt"), null, ItemDusts.getDustByName("basalt", 2));
		}
	}

	static void addTROreWashingRecipes() {
		//Ore Washing Plant
		NBTTagCompound liquidAmount = new NBTTagCompound();
		liquidAmount.setInteger("amount", 1000);
		Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedAluminum"), liquidAmount, ItemPurifiedCrushedOre.getPurifiedCrushedOreByName("Aluminum", 1), ItemDustsTiny.getTinyDustByName("Aluminum", 2), IC2Items.getItem("stoneDust"));
		Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedArdite"), liquidAmount, ItemPurifiedCrushedOre.getPurifiedCrushedOreByName("Ardite", 1), ItemDustsTiny.getTinyDustByName("Ardite", 2), IC2Items.getItem("stoneDust"));
		Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedBauxite"), liquidAmount, ItemPurifiedCrushedOre.getPurifiedCrushedOreByName("Bauxite", 1), ItemDustsTiny.getTinyDustByName("Bauxite", 2), IC2Items.getItem("stoneDust"));
		Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedCadmium"), liquidAmount, ItemPurifiedCrushedOre.getPurifiedCrushedOreByName("Cadmium", 1), ItemDustsTiny.getTinyDustByName("Cadmium", 2), IC2Items.getItem("stoneDust"));
		Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedCinnabar"), liquidAmount, ItemPurifiedCrushedOre.getPurifiedCrushedOreByName("Cinnabar", 1), ItemDustsTiny.getTinyDustByName("Cinnabar", 2), IC2Items.getItem("stoneDust"));
		Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedCobalt"), liquidAmount, ItemPurifiedCrushedOre.getPurifiedCrushedOreByName("Cobalt", 1), ItemDustsTiny.getTinyDustByName("Cobalt", 2), IC2Items.getItem("stoneDust"));
		Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedDarkIron"), liquidAmount, ItemPurifiedCrushedOre.getPurifiedCrushedOreByName("DarkIron", 1), ItemDustsTiny.getTinyDustByName("DarkIron", 2), IC2Items.getItem("stoneDust"));
		Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedIndium"), liquidAmount, ItemPurifiedCrushedOre.getPurifiedCrushedOreByName("Indium", 1), ItemDustsTiny.getTinyDustByName("Indium", 2), IC2Items.getItem("stoneDust"));
		Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedNickel"), liquidAmount, ItemPurifiedCrushedOre.getPurifiedCrushedOreByName("Nickel", 1), ItemDustsTiny.getTinyDustByName("Nickel", 2), IC2Items.getItem("stoneDust"));
		Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedOsmium"), liquidAmount, ItemPurifiedCrushedOre.getPurifiedCrushedOreByName("Osmium", 1), ItemDustsTiny.getTinyDustByName("Osmium", 2), IC2Items.getItem("stoneDust"));
		Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedPyrite"), liquidAmount, ItemPurifiedCrushedOre.getPurifiedCrushedOreByName("Pyrite", 1), ItemDustsTiny.getTinyDustByName("Pyrite", 2), IC2Items.getItem("stoneDust"));
		Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedSphalerite"), liquidAmount, ItemPurifiedCrushedOre.getPurifiedCrushedOreByName("Sphalerite", 1), ItemDustsTiny.getTinyDustByName("Sphalerite", 2), IC2Items.getItem("stoneDust"));
		Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedTetrahedrite"), liquidAmount, ItemPurifiedCrushedOre.getPurifiedCrushedOreByName("Tetrahedrite", 1), ItemDustsTiny.getTinyDustByName("Tetrahedrite", 2), IC2Items.getItem("stoneDust"));
		Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedGalena"), liquidAmount, ItemPurifiedCrushedOre.getPurifiedCrushedOreByName("Galena", 1), ItemDustsTiny.getTinyDustByName("Galena", 2), IC2Items.getItem("stoneDust"));

		if(!Loader.isModLoaded("aobd"))
		{
			Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedPlatinum"), liquidAmount, ItemPurifiedCrushedOre.getPurifiedCrushedOreByName("Platinum", 1), ItemDustsTiny.getTinyDustByName("Platinum", 2), IC2Items.getItem("stoneDust"));
			Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedIridium"), liquidAmount, ItemPurifiedCrushedOre.getPurifiedCrushedOreByName("Iridium", 1), ItemDustsTiny.getTinyDustByName("Iridium", 2), IC2Items.getItem("stoneDust"));
			Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedTungsten"), liquidAmount, ItemPurifiedCrushedOre.getPurifiedCrushedOreByName("Tungsten", 1), ItemDustsTiny.getTinyDustByName("Tungsten", 2), IC2Items.getItem("stoneDust"));
		}
	}

	static void addTRThermalCentrifugeRecipes() {
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

		Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedAluminum"), aluminumHeat, ItemDustsTiny.getTinyDustByName("Bauxite", 1), ItemDusts.getDustByName("aluminum", 1), IC2Items.getItem("stoneDust"));
		Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedArdite"), arditeHeat, ItemDustsTiny.getTinyDustByName("Ardite", 1), ItemDusts.getDustByName("ardite", 1), IC2Items.getItem("stoneDust"));
		Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedBauxite"), bauxiteHeat, ItemDustsTiny.getTinyDustByName("Aluminum", 1), ItemDusts.getDustByName("bauxite", 1), IC2Items.getItem("stoneDust"));
		Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedCadmium"), cadmiumHeat, ItemDustsTiny.getTinyDustByName("Cadmium", 1), ItemDusts.getDustByName("cadmium", 1), IC2Items.getItem("stoneDust"));
		Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedCinnabar"), cinnabarHeat, ItemDustsTiny.getTinyDustByName("Redstone", 1), ItemDusts.getDustByName("cinnabar", 1), IC2Items.getItem("stoneDust"));
		Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedCobalt"), cobaltHeat, ItemDustsTiny.getTinyDustByName("Cobalt", 1), ItemDusts.getDustByName("cobalt", 1), IC2Items.getItem("stoneDust"));
		Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedDarkIron"), darkIronHeat, ItemDustsTiny.getTinyDustByName("Iron", 1), ItemDusts.getDustByName("darkIron", 1), IC2Items.getItem("stoneDust"));
		Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedIndium"), indiumHeat, ItemDustsTiny.getTinyDustByName("Indium", 1), ItemDusts.getDustByName("indium", 1), IC2Items.getItem("stoneDust"));
		Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedNickel"), nickelHeat, ItemDustsTiny.getTinyDustByName("Iron", 1), ItemDusts.getDustByName("nickel", 1), IC2Items.getItem("stoneDust"));
		Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedOsmium"), osmiumHeat, ItemDustsTiny.getTinyDustByName("Osmium", 1), ItemDusts.getDustByName("osmium", 1), IC2Items.getItem("stoneDust"));
		Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPyrite"), pyriteHeat, ItemDustsTiny.getTinyDustByName("Sulfur", 1), ItemDusts.getDustByName("pyrite", 1), IC2Items.getItem("stoneDust"));
		Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedSphalerite"), sphaleriteHeat, ItemDustsTiny.getTinyDustByName("Zinc", 1), ItemDusts.getDustByName("sphalerite", 1), IC2Items.getItem("stoneDust"));
		Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedTetrahedrite"), tetrahedriteHeat, ItemDustsTiny.getTinyDustByName("Antimony", 1), ItemDusts.getDustByName("tetrahedrite", 1), IC2Items.getItem("stoneDust"));
		Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedGalena"), galenaHeat, ItemDustsTiny.getTinyDustByName("Sulfur", 1), ItemDusts.getDustByName("galena", 1), IC2Items.getItem("stoneDust"));

		Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedAluminum"), aluminumHeat, ItemDustsTiny.getTinyDustByName("Bauxite", 1), ItemDusts.getDustByName("aluminum", 1));
		Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedArdite"), arditeHeat, ItemDustsTiny.getTinyDustByName("Ardite", 1), ItemDusts.getDustByName("ardite", 1));
		Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedBauxite"), bauxiteHeat, ItemDustsTiny.getTinyDustByName("Aluminum", 1), ItemDusts.getDustByName("bauxite", 1));
		Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedCadmium"), cadmiumHeat, ItemDustsTiny.getTinyDustByName("Cadmium", 1), ItemDusts.getDustByName("cadmium", 1));
		Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedCinnabar"), cinnabarHeat, ItemDustsTiny.getTinyDustByName("Redstone", 1), ItemDusts.getDustByName("cinnabar", 1));
		Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedCobalt"), cobaltHeat, ItemDustsTiny.getTinyDustByName("Cobalt", 1), ItemDusts.getDustByName("cobalt", 1));
		Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedDarkIron"), darkIronHeat, ItemDustsTiny.getTinyDustByName("Iron", 1), ItemDusts.getDustByName("darkIron", 1));
		Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedIndium"), indiumHeat, ItemDustsTiny.getTinyDustByName("Indium", 1), ItemDusts.getDustByName("indium", 1));
		Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedNickel"), nickelHeat, ItemDustsTiny.getTinyDustByName("Iron", 1), ItemDusts.getDustByName("nickel", 1));
		Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedOsmium"), osmiumHeat, ItemDustsTiny.getTinyDustByName("Osmium", 1), ItemDusts.getDustByName("osmium", 1));
		Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedPyrite"), pyriteHeat, ItemDustsTiny.getTinyDustByName("Sulfur", 1), ItemDusts.getDustByName("pyrite", 1));
		Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedSphalerite"), sphaleriteHeat, ItemDustsTiny.getTinyDustByName("Zinc", 1), ItemDusts.getDustByName("sphalerite", 1));
		Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedTetrahedrite"), tetrahedriteHeat, ItemDustsTiny.getTinyDustByName("Antimony", 1), ItemDusts.getDustByName("tetrahedrite", 1));
		Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedGalena"), galenaHeat, ItemDustsTiny.getTinyDustByName("Sulfur", 1), ItemDusts.getDustByName("galena", 1));

		if(!Loader.isModLoaded("aobd"))
		{
			Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedIridium"), iridiumHeat, ItemDustsTiny.getTinyDustByName("Platinum", 1), ItemDusts.getDustByName("iridium", 1), IC2Items.getItem("stoneDust"));
			Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPlatinum"), platinumHeat, ItemDustsTiny.getTinyDustByName("Iridium", 1), ItemDusts.getDustByName("platinum", 1), IC2Items.getItem("stoneDust"));
			Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedTungsten"), tungstenHeat, ItemDustsTiny.getTinyDustByName("Manganese", 1), ItemDusts.getDustByName("tungsten", 1), IC2Items.getItem("stoneDust"));

			Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedIridium"), iridiumHeat, ItemDustsTiny.getTinyDustByName("Platinum", 1), ItemDusts.getDustByName("iridium", 1));
			Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedPlatinum"), platinumHeat, ItemDustsTiny.getTinyDustByName("Iridium", 1), ItemDusts.getDustByName("platinum", 1));
			Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedTungsten"), tungstenHeat, ItemDustsTiny.getTinyDustByName("Manganese", 1), ItemDusts.getDustByName("tungsten", 1));
		}
	}

	static void addMetalFormerRecipes() {
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
}

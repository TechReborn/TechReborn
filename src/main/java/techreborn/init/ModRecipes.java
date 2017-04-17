package techreborn.init;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.OreDictionary;
import reborncore.api.recipe.RecipeHandler;
import reborncore.common.util.CraftingHelper;
import reborncore.common.util.ItemUtils;
import reborncore.common.util.OreUtil;
import reborncore.common.util.StringUtils;
import techreborn.Core;
import techreborn.api.reactor.FusionReactorRecipe;
import techreborn.api.reactor.FusionReactorRecipeHelper;
import techreborn.api.recipe.machines.*;
import techreborn.blocks.BlockMachineFrame;
import techreborn.blocks.BlockOre;
import techreborn.compat.CompatManager;
import techreborn.config.ConfigTechReborn;
import techreborn.init.recipes.*;
import techreborn.items.*;

import java.util.Iterator;
import java.util.Map;

import static techreborn.utils.OreDictUtils.getDictData;
import static techreborn.utils.OreDictUtils.getDictOreOrEmpty;
import static techreborn.utils.OreDictUtils.isDictPrefixed;
import static techreborn.utils.OreDictUtils.joinDictName;

public class ModRecipes {
	public static ConfigTechReborn config;

	public static void init() {
		//Gonna rescan to make sure we have an uptodate list
		OreUtil.scanForOres();
		//Done again incase we loaded before QuantumStorage
		CompatManager.isQuantumStorageLoaded = Loader.isModLoaded("quantumstorage");

		CraftingTableRecipes.init();
		SmeltingRecipes.init();
		ExtractorRecipes.init();
		RollingMachineRecipes.init();
		FluidGeneratorRecipes.init();
		IndustrialGrinderRecipes.init();
		IndustrialCentrifugeRecipes.init();
		IndustrialElectrolyzerRecipes.init();
		ImplosionCompressorRecipes.init();
		ScrapboxRecipes.init();

		addGeneralShapedRecipes();
		addMachineRecipes();

		addAlloySmelterRecipes();
		addChemicalReactorRecipes();

		addBlastFurnaceRecipes();
		addVacuumFreezerRecipes();

		addReactorRecipes();
		addIc2Recipes();
		addGrinderRecipes();
		addCompressorRecipes();
	}

	public static void postInit(){
		if(ConfigTechReborn.disableRailcraftSteelNuggetRecipe){
			Iterator iterator = FurnaceRecipes.instance().getSmeltingList().entrySet().iterator();
			Map.Entry entry;
			while (iterator.hasNext()) {
				entry = (Map.Entry) iterator.next();
				if (entry.getValue() instanceof ItemStack && entry.getKey() instanceof ItemStack) {
					ItemStack input = (ItemStack) entry.getKey();
					ItemStack output = (ItemStack) entry.getValue();
					if(ItemUtils.isInputEqual("nuggetSteel", output, true , true, false) && ItemUtils.isInputEqual("nuggetIron", input, true , true, false)){
						Core.logHelper.info("Removing a steelnugget smelting recipe");
						iterator.remove();
					}
				}
			}
		}
		IndustrialSawmillRecipes.init();
	}

	private static void addCompressorRecipes() {
		RecipeHandler.addRecipe(new CompressorRecipe(ItemIngots.getIngotByName("advanced_alloy"),
			ItemPlates.getPlateByName("advanced_alloy"), 400, 20));
		RecipeHandler.addRecipe(
			new CompressorRecipe(IC2Duplicates.CARBON_MESH.getStackBasedOnConfig(), ItemPlates.getPlateByName("carbon"), 400,
				2));

		for (String ore : OreUtil.oreNames) {
			if (OreUtil.doesOreExistAndValid("plate" + OreUtil.capitalizeFirstLetter(ore)) && OreUtil.doesOreExistAndValid("ingot" + OreUtil.capitalizeFirstLetter(ore))) {

				RecipeHandler.addRecipe(
					new CompressorRecipe(OreUtil.getStackFromName("ingot" + OreUtil.capitalizeFirstLetter(ore), 9), OreUtil.getStackFromName("plate" + OreUtil.capitalizeFirstLetter(ore), 1), 300,
						4));
			}
			if (OreUtil.doesOreExistAndValid("plate" + OreUtil.capitalizeFirstLetter(ore)) && OreUtil.doesOreExistAndValid("gem" + OreUtil.capitalizeFirstLetter(ore))) {

				RecipeHandler.addRecipe(
					new CompressorRecipe(OreUtil.getStackFromName("gem" + OreUtil.capitalizeFirstLetter(ore), 9), OreUtil.getStackFromName("plate" + OreUtil.capitalizeFirstLetter(ore), 1), 300,
						4));
			}

			if (OreUtil.hasPlate(ore) && OreUtil.hasBlock(ore)) {
				RecipeHandler.addRecipe(
					new CompressorRecipe(OreUtil.getStackFromName("block" + OreUtil.capitalizeFirstLetter(ore), 1), OreUtil.getStackFromName("plate" + OreUtil.capitalizeFirstLetter(ore), 1), 300,
						4));
			}
		}
		RecipeHandler.addRecipe(
			new CompressorRecipe(OreUtil.getStackFromName("plankWood", 1), OreUtil.getStackFromName("plateWood", 1), 300,
				4));
		RecipeHandler.addRecipe(
			new CompressorRecipe(OreUtil.getStackFromName("dustLazurite", 8), OreUtil.getStackFromName("plateLazurite", 1), 300,
				4));
	}

	static void addGrinderRecipes() {

		// Vanilla
		int eutick = 2;
		int ticktime = 300;

		RecipeHandler.addRecipe(new GrinderRecipe(
			new ItemStack(Items.BONE),
			new ItemStack(Items.DYE, 6, 15),
			170, 19));

		RecipeHandler.addRecipe(new GrinderRecipe(
			new ItemStack(Blocks.COBBLESTONE),
			new ItemStack(Blocks.SAND),
			230, 23));

		RecipeHandler.addRecipe(new GrinderRecipe(
			new ItemStack(Blocks.GRAVEL),
			new ItemStack(Items.FLINT),
			200, 20));

		RecipeHandler.addRecipe(new GrinderRecipe(
			new ItemStack(Blocks.GLOWSTONE),
			ItemDusts.getDustByName("glowstone", 4), 220, 21));

		RecipeHandler.addRecipe(new GrinderRecipe(
			new ItemStack(Blocks.NETHERRACK),
			ItemDusts.getDustByName("netherrack"),
			300, 27));

		RecipeHandler.addRecipe(new GrinderRecipe(
			new ItemStack(Items.COAL),
			ItemDusts.getDustByName("coal"),
			300, 27));

		RecipeHandler.addRecipe(new GrinderRecipe(
			new ItemStack(net.minecraft.init.Items.CLAY_BALL),
			ItemDusts.getDustByName("clay"),
			200, 18));

		for (String oreDictionaryName : OreDictionary.getOreNames()) {
			if (isDictPrefixed(oreDictionaryName, "ore", "gem", "ingot")) {
				ItemStack oreStack = getDictOreOrEmpty(oreDictionaryName, 1);
				String[] data = getDictData(oreDictionaryName);

				//High-level ores shouldn't grind here
				if (data[0].equals("ore") && (
					data[1].equals("tungsten") ||
						data[1].equals("titanium") ||
						data[1].equals("aluminium") ||
						data[1].equals("iridium") ||
						data[1].equals("saltpeter")) ||
					oreStack == ItemStack.EMPTY)
					continue;

				boolean ore = data[0].equals("ore");
				Core.logHelper.debug("Ore: " + data[1]);
				ItemStack dust = getDictOreOrEmpty(joinDictName("dust", data[1]), ore ? 2 : 1);
				if (dust == ItemStack.EMPTY || dust.getItem() == null) {
					continue;
				}
				dust = dust.copy();
				if (ore) {
					dust.setCount(2);
				}
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, dust, ore ? 270 : 200, ore ? 31 : 22));
			}
		}

		RecipeHandler.addRecipe(new GrinderRecipe(
			new ItemStack(Items.COAL),
			ItemDusts.getDustByName("coal"),
			120, 10));

		RecipeHandler.addRecipe(new GrinderRecipe(
			new ItemStack(Blocks.END_STONE),
			ItemDusts.getDustByName("endstone"),
			300, 16));

	}

	static void addReactorRecipes() {
		FusionReactorRecipeHelper.registerRecipe(
			new FusionReactorRecipe(ItemCells.getCellByName("helium3"), ItemCells.getCellByName("deuterium"),
				ItemCells.getCellByName("heliumplasma"), 40000000, 32768, 1024));
		FusionReactorRecipeHelper.registerRecipe(
			new FusionReactorRecipe(ItemCells.getCellByName("tritium"), ItemCells.getCellByName("deuterium"),
				ItemCells.getCellByName("helium3"), 60000000, 32768, 2048));
		FusionReactorRecipeHelper.registerRecipe(
			new FusionReactorRecipe(ItemCells.getCellByName("wolframium"), ItemCells.getCellByName("Berylium"),
				ItemDusts.getDustByName("platinum"), 80000000, -2048, 1024));
		FusionReactorRecipeHelper.registerRecipe(
			new FusionReactorRecipe(ItemCells.getCellByName("wolframium"), ItemCells.getCellByName("lithium"),
				BlockOre.getOreByName("iridium"), 90000000, -2048, 1024));
	}

	static void addGeneralShapedRecipes() {
		CraftingHelper
			.addShapedOreRecipe(new ItemStack(ModItems.CLOAKING_DEVICE), "CIC", "IOI", "CIC", 'C', "ingotChrome",
				'I', "plateIridium", 'O', new ItemStack(ModItems.LAPOTRONIC_ORB));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.TREE_TAP), " S ", "PPP", "P  ", 'S', "stickWood", 'P',
			"plankWood");

		CraftingHelper
			.addShapedOreRecipe(new ItemStack(ModItems.ROCK_CUTTER), "DT ", "DT ", "DCB", 'D', "dustDiamond", 'T',
				"ingotTitanium", 'C', "circuitBasic", 'B', new ItemStack(ModItems.RE_BATTERY));

		for (String part : ItemParts.types) {
			if (part.endsWith("Gear")) {
				CraftingHelper.addShapedOreRecipe(ItemParts.getPartByName(part), " O ", "OIO", " O ", 'I',
					new ItemStack(Items.IRON_INGOT), 'O',
					"ingot" + StringUtils.toFirstCapital(part.replace("Gear", "")));
			}
		}

		CraftingHelper.addShapedOreRecipe(ItemParts.getPartByName("heliumCoolantSimple"), " T ", "TCT", " T ", 'T',
			"ingotTin", 'C', ItemCells.getCellByName("helium", 1));

		CraftingHelper.addShapedOreRecipe(ItemParts.getPartByName("HeliumCoolantTriple"), "TTT", "CCC", "TTT", 'T',
			"ingotTin", 'C', ItemParts.getPartByName("heliumCoolantSimple"));

		CraftingHelper
			.addShapedOreRecipe(ItemParts.getPartByName("HeliumCoolantSix"), "THT", "TCT", "THT", 'T', "ingotTin",
				'C', "ingotCopper", 'H', ItemParts.getPartByName("HeliumCoolantTriple"));

		CraftingHelper
			.addShapedOreRecipe(ItemParts.getPartByName("NaKCoolantTriple"), "TTT", "CCC", "TTT", 'T', "ingotTin",
				'C', ItemParts.getPartByName("NaKCoolantSimple"));

		CraftingHelper
			.addShapedOreRecipe(ItemParts.getPartByName("NaKCoolantSix"), "THT", "TCT", "THT", 'T', "ingotTin", 'C',
				"ingotCopper", 'H', ItemParts.getPartByName("NaKCoolantTriple"));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.ADJUSTABLE_SU), "LLL", "LCL", "LLL", 'L',
			new ItemStack(ModItems.LAPOTRONIC_ORB), 'C', new ItemStack(ModItems.ENERGY_CRYSTAL));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.INTERDIMENSIONAL_SU), "PAP", "ACA", "PAP", 'P',
			"plateIridium", 'C', new ItemStack(Blocks.ENDER_CHEST), 'A',
			new ItemStack(ModBlocks.ADJUSTABLE_SU));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.FUSION_CONTROL_COMPUTER), "CCC", "PTP", "CCC", 'P',
			new ItemStack(ModItems.ENERGY_CRYSTAL), 'T', new ItemStack(ModBlocks.FUSION_COIL), 'C',
			"circuitMaster");

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.LIGHTNING_ROD), "CAC", "ACA", "CAC", 'A',
			new ItemStack(ModBlocks.MACHINE_CASINGS, 1, 2), 'S', ItemParts.getPartByName("superConductor"), 'C',
			"circuitMaster");

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.FUSION_COIL), "CSC", "NAN", "CRC", 'A',
			new ItemStack(ModBlocks.MACHINE_CASINGS, 1, 2), 'N', ItemParts.getPartByName("nichromeHeatingCoil"), 'C',
			"circuitMaster", 'S', ItemParts.getPartByName("superConductor"), 'R',
			ItemParts.getPartByName("iridiumNeutronReflector"));

		CraftingHelper.addShapedOreRecipe(ItemParts.getPartByName("iridiumNeutronReflector"), "PPP", "PIP", "PPP", 'P',
			ItemParts.getPartByName("thickNeutronReflector"), 'I', "ingotIridium");

		CraftingHelper.addShapedOreRecipe(ItemParts.getPartByName("thickNeutronReflector"), " P ", "PCP", " P ", 'P',
			ItemParts.getPartByName("neutronReflector"), 'C', ItemCells.getCellByName("Berylium"));

		CraftingHelper
			.addShapedOreRecipe(ItemParts.getPartByName("neutronReflector"), "TCT", "CPC", "TCT", 'T', "dustTin",
				'C', "dustCoal", 'P', "plateCopper");

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.SCRAP_BOX), "SSS", "SSS", "SSS", 'S',
			ItemParts.getPartByName("scrap"));

		if (!IC2Duplicates.deduplicate()) {
			CraftingHelper.addShapedOreRecipe(ItemUpgrades.getUpgradeByName("Overclock"), "TTT", "WCW", 'T',
				ItemParts.getPartByName("CoolantSimple"), 'W', IC2Duplicates.CABLE_ICOPPER.getStackBasedOnConfig(),
				'C', "circuitBasic");

			CraftingHelper.addShapedOreRecipe(ItemUpgrades.getUpgradeByName("Overclock", 2), " T ", "WCW", 'T',
				ItemParts.getPartByName("heliumCoolantSimple"), 'W',
				IC2Duplicates.CABLE_ICOPPER.getStackBasedOnConfig(), 'C',
				"circuitBasic");

			CraftingHelper.addShapedOreRecipe(ItemUpgrades.getUpgradeByName("Overclock", 2), " T ", "WCW", 'T',
				ItemParts.getPartByName("NaKCoolantSimple"), 'W',
				IC2Duplicates.CABLE_ICOPPER.getStackBasedOnConfig(), 'C',
				"circuitBasic");
		}

		CraftingHelper.addShapedOreRecipe(ItemUpgrades.getUpgradeByName("transformer"), "GGG", "WTW", "GCG", 'G',
			"blockGlass", 'W', IC2Duplicates.CABLE_IGOLD.getStackBasedOnConfig(), 'C',
			"circuitBasic", 'T', IC2Duplicates.MVT.getStackBasedOnConfig());

		CraftingHelper.addShapedOreRecipe(ItemUpgrades.getUpgradeByName("energy_storage"), "PPP", "WBW", "PCP", 'P',
			"plankWood", 'W', IC2Duplicates.CABLE_ICOPPER.getStackBasedOnConfig(), 'C',
			"circuitBasic", 'B', ModItems.RE_BATTERY);

		CraftingHelper
			.addShapedOreRecipe(ItemParts.getPartByName("CoolantSimple"), " T ", "TWT", " T ", 'T', "ingotTin", 'W',
				"containerWater");

		CraftingHelper
			.addShapedOreRecipe(ItemParts.getPartByName("CoolantTriple"), "TTT", "CCC", "TTT", 'T', "ingotTin", 'C',
				ItemParts.getPartByName("CoolantSimple"));

		CraftingHelper
			.addShapedOreRecipe(ItemParts.getPartByName("CoolantSix"), "TCT", "TPT", "TCT", 'T', "ingotTin", 'C',
				ItemParts.getPartByName("CoolantTriple"), 'P', "plateCopper");

		CraftingHelper
			.addShapedOreRecipe(ItemParts.getPartByName("NaKCoolantSimple"), "TST", "PCP", "TST", 'T', "ingotTin",
				'C', ItemParts.getPartByName("CoolantSimple"), 'S', ItemCells.getCellByName("sodium"), 'P',
				ItemCells.getCellByName("potassium"));

		CraftingHelper
			.addShapedOreRecipe(ItemParts.getPartByName("NaKCoolantSimple"), "TPT", "SCS", "TPT", 'T', "ingotTin",
				'C', ItemParts.getPartByName("CoolantSimple"), 'S', ItemCells.getCellByName("sodium"), 'P',
				ItemCells.getCellByName("potassium"));

		CraftingHelper
			.addShapedOreRecipe(ItemParts.getPartByName("dataControlCircuit"), "ADA", "DID", "ADA", 'I', "ingotIridium",
				'A', ItemParts.getPartByName("advancedCircuit"), 'D', ItemParts.getPartByName("dataStorageCircuit"));

		CraftingHelper
			.addShapedOreRecipe(ItemParts.getPartByName("dataOrb"), "DDD", "DSD", "DDD",
				'D', ItemParts.getPartByName("dataStorageCircuit"), 'S', ItemParts.getPartByName("dataStorageCircuit"));

		CraftingHelper
			.addShapedOreRecipe(new ItemStack(ModItems.ELECTRIC_TREE_TAP), "TB", "  ",
				'T', new ItemStack(ModItems.TREE_TAP), 'B', new ItemStack(ModItems.RE_BATTERY));

		CraftingHelper
			.addShapedOreRecipe(new ItemStack(ModItems.NANOSABER), "DC ", "DC ", "GLG",
				'L', new ItemStack(ModItems.LAPOTRONIC_CRYSTAL), 'C', "plateCarbon", 'D', "plateDiamond",
				'G', ItemDustsSmall.getSmallDustByName("glowstone"));

		CraftingHelper.addShapedOreRecipe(ItemParts.getPartByName("diamondGrindingHead", 2), "TST", "SBS", "TST", 'T',
			"plateDiamond", 'S', "plateSteel", 'B', "blockDiamond");

		CraftingHelper.addShapedOreRecipe(ItemParts.getPartByName("coolantSimple", 2), " T ", "TWT", " T ", 'T',
			"ingotTin", 'W', new ItemStack(Items.WATER_BUCKET));

		Core.logHelper.info("Shapped Recipes Added");
	}

	static void addMachineRecipes() {
		if (!CompatManager.isQuantumStorageLoaded) {
			CraftingHelper
				.addShapedOreRecipe(new ItemStack(ModBlocks.QUANTUM_TANK), "EPE", "PCP", "EPE", 'P', "ingotPlatinum",
					'E', "circuitAdvanced", 'C', ModBlocks.QUANTUM_CHEST);
		}

		CraftingHelper
			.addShapedOreRecipe(new ItemStack(ModBlocks.DIGITAL_CHEST), "PPP", "PDP", "PCP", 'P', "plateAluminum",
				'D', ItemParts.getPartByName("dataOrb"), 'C', ItemParts.getPartByName("computerMonitor"));

		CraftingHelper
			.addShapedOreRecipe(new ItemStack(ModBlocks.DIGITAL_CHEST), "PPP", "PDP", "PCP", 'P', "plateSteel", 'D',
				ItemParts.getPartByName("dataOrb"), 'C', ItemParts.getPartByName("computerMonitor"));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.ALLOY_SMELTER), "XCX", "FMF", "XXX", 'C',
			"circuitBasic", 'F', IC2Duplicates.ELECTRICAL_FURNACE.getStackBasedOnConfig(), 'M',
			BlockMachineFrame.getFrameByName("machine", 1));

		CraftingHelper
			.addShapedOreRecipe(new ItemStack(ModBlocks.LSU_STORAGE_BLOCK), "LLL", "LCL", "LLL", 'L', "blockLapis", 'C',
				"circuitBasic");

		RecipeHandler.addRecipe(new VacuumFreezerRecipe(ItemIngots.getIngotByName("hot_tungstensteel"),
			ItemIngots.getIngotByName("tungstensteel"), 440, 128));

		RecipeHandler.addRecipe(new VacuumFreezerRecipe(ItemCells.getCellByName("heliumplasma"),
			ItemCells.getCellByName("helium"), 440, 128));

		RecipeHandler.addRecipe(
			new VacuumFreezerRecipe(ItemCells.getCellByName("water"),
				ItemCells.getCellByName("cell"), 60, 128));
	}

	static void addVacuumFreezerRecipes() {
		RecipeHandler.addRecipe(new VacuumFreezerRecipe(
			new ItemStack(Blocks.ICE, 2),
			new ItemStack(Blocks.PACKED_ICE),
			60, 100
		));

		RecipeHandler.addRecipe(new VacuumFreezerRecipe(
			ItemIngots.getIngotByName("hot_tungstensteel"),
			ItemIngots.getIngotByName("tungstensteel"),
			440, 120));

		RecipeHandler.addRecipe(new VacuumFreezerRecipe(
			ItemCells.getCellByName("heliumplasma"),
			ItemCells.getCellByName("helium"),
			440, 128));

		RecipeHandler.addRecipe(
			new VacuumFreezerRecipe(
				ItemCells.getCellByName("water"),
				ItemCells.getCellByName("cell"),
				60, 87));
	}

	static void addAlloySmelterRecipes() {

		// Bronze
		RecipeHandler.addRecipe(
			new AlloySmelterRecipe(ItemIngots.getIngotByName("copper", 3), ItemIngots.getIngotByName("tin", 1),
				ItemIngots.getIngotByName("bronze", 4), 200, 16));
		RecipeHandler.addRecipe(
			new AlloySmelterRecipe(ItemIngots.getIngotByName("copper", 3), ItemDusts.getDustByName("tin", 1),
				ItemIngots.getIngotByName("bronze", 4), 200, 16));
		RecipeHandler.addRecipe(
			new AlloySmelterRecipe(ItemDusts.getDustByName("copper", 3), ItemIngots.getIngotByName("tin", 1),
				ItemIngots.getIngotByName("bronze", 4), 200, 16));
		RecipeHandler.addRecipe(
			new AlloySmelterRecipe(ItemDusts.getDustByName("copper", 3), ItemDusts.getDustByName("tin", 1),
				ItemIngots.getIngotByName("bronze", 4), 200, 16));

		// Electrum
		RecipeHandler.addRecipe(
			new AlloySmelterRecipe(new ItemStack(Items.GOLD_INGOT, 1), ItemIngots.getIngotByName("silver", 1),
				ItemIngots.getIngotByName("electrum", 2), 200, 16));
		RecipeHandler.addRecipe(
			new AlloySmelterRecipe(new ItemStack(Items.GOLD_INGOT, 1), ItemDusts.getDustByName("silver", 1),
				ItemIngots.getIngotByName("electrum", 2), 200, 16));
		RecipeHandler.addRecipe(
			new AlloySmelterRecipe(ItemDusts.getDustByName("gold", 1), ItemIngots.getIngotByName("silver", 1),
				ItemIngots.getIngotByName("electrum", 2), 200, 16));
		RecipeHandler.addRecipe(
			new AlloySmelterRecipe(ItemDusts.getDustByName("gold", 1), ItemDusts.getDustByName("silver", 1),
				ItemIngots.getIngotByName("electrum", 2), 200, 16));

		// Invar
		RecipeHandler.addRecipe(
			new AlloySmelterRecipe(new ItemStack(Items.IRON_INGOT, 2), ItemIngots.getIngotByName("nickel", 1),
				ItemIngots.getIngotByName("invar", 3), 200, 16));
		RecipeHandler.addRecipe(
			new AlloySmelterRecipe(new ItemStack(Items.IRON_INGOT, 2), ItemDusts.getDustByName("nickel", 1),
				ItemIngots.getIngotByName("invar", 3), 200, 16));
		RecipeHandler.addRecipe(
			new AlloySmelterRecipe(ItemDusts.getDustByName("iron", 2), ItemIngots.getIngotByName("nickel", 1),
				ItemIngots.getIngotByName("invar", 3), 200, 16));
		RecipeHandler.addRecipe(
			new AlloySmelterRecipe(ItemDusts.getDustByName("iron", 2), ItemDusts.getDustByName("nickel", 1),
				ItemIngots.getIngotByName("invar", 3), 200, 16));

		// Brass
		if (OreUtil.doesOreExistAndValid("ingotBrass")) {
			ItemStack brassStack = OreDictionary.getOres("ingotBrass").get(0);
			brassStack.setCount(4);
			RecipeHandler.addRecipe(
				new AlloySmelterRecipe(ItemIngots.getIngotByName("copper", 3), ItemIngots.getIngotByName("zinc", 1),
					brassStack, 200, 16));
			RecipeHandler.addRecipe(
				new AlloySmelterRecipe(ItemIngots.getIngotByName("copper", 3), ItemDusts.getDustByName("zinc", 1),
					brassStack, 200, 16));
			RecipeHandler.addRecipe(
				new AlloySmelterRecipe(ItemDusts.getDustByName("copper", 3), ItemIngots.getIngotByName("zinc", 1),
					brassStack, 200, 16));
			RecipeHandler.addRecipe(
				new AlloySmelterRecipe(ItemDusts.getDustByName("copper", 3), ItemDusts.getDustByName("zinc", 1),
					brassStack, 200, 16));
		}

		// Red Alloy
		if (OreUtil.doesOreExistAndValid("ingotRedAlloy")) {
			ItemStack redAlloyStack = OreDictionary.getOres("ingotRedAlloy").get(0);
			redAlloyStack.setCount(1);
			RecipeHandler.addRecipe(
				new AlloySmelterRecipe(new ItemStack(Items.REDSTONE, 4), ItemIngots.getIngotByName("copper", 1),
					redAlloyStack, 200, 16));
			RecipeHandler.addRecipe(
				new AlloySmelterRecipe(new ItemStack(Items.REDSTONE, 4), new ItemStack(Items.IRON_INGOT, 1),
					redAlloyStack, 200, 16));
		}

		// Blue Alloy
		if (OreUtil.doesOreExistAndValid("ingotBlueAlloy")) {
			ItemStack blueAlloyStack = OreDictionary.getOres("ingotBlueAlloy").get(0);
			blueAlloyStack.setCount(1);
			RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemDusts.getDustByName("teslatite", 4),
				ItemIngots.getIngotByName("silver", 1), blueAlloyStack, 200, 16));
		}

		// Blue Alloy
		if (OreUtil.doesOreExistAndValid("ingotPurpleAlloy") && OreUtil.doesOreExistAndValid("dustInfusedTeslatite")) {
			ItemStack purpleAlloyStack = OreDictionary.getOres("ingotPurpleAlloy").get(0);
			purpleAlloyStack.setCount(1);
			ItemStack infusedTeslatiteStack = OreDictionary.getOres("ingotPurpleAlloy").get(0);
			infusedTeslatiteStack.setCount(8);
			RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemIngots.getIngotByName("redAlloy", 1),
				ItemIngots.getIngotByName("blueAlloy", 1), purpleAlloyStack, 200, 16));
			RecipeHandler.addRecipe(
				new AlloySmelterRecipe(new ItemStack(Items.GOLD_INGOT, 1), infusedTeslatiteStack, purpleAlloyStack,
					200, 16));
		}

		// Aluminum Brass
		if (OreUtil.doesOreExistAndValid("ingotAluminumBrass")) {
			ItemStack aluminumBrassStack = OreDictionary.getOres("ingotAluminumBrass").get(0);
			aluminumBrassStack.setCount(4);
			RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemIngots.getIngotByName("copper", 3),
				ItemIngots.getIngotByName("aluminum", 1), aluminumBrassStack, 200, 16));
			RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemIngots.getIngotByName("copper", 3),
				ItemDusts.getDustByName("aluminum", 1), aluminumBrassStack, 200, 16));
			RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemDusts.getDustByName("copper", 3),
				ItemIngots.getIngotByName("aluminum", 1), aluminumBrassStack, 200, 16));
			RecipeHandler.addRecipe(
				new AlloySmelterRecipe(ItemDusts.getDustByName("copper", 3), ItemDusts.getDustByName("aluminum", 1),
					aluminumBrassStack, 200, 16));
		}

		// Manyullyn
		if (OreUtil.doesOreExistAndValid("ingotManyullyn") && OreUtil.doesOreExistAndValid("ingotCobalt") && OreUtil
			.doesOreExistAndValid("ingotArdite")) {
			ItemStack manyullynStack = OreDictionary.getOres("ingotManyullyn").get(0);
			manyullynStack.setCount(1);
			ItemStack cobaltStack = OreDictionary.getOres("ingotCobalt").get(0);
			cobaltStack.setCount(1);
			ItemStack arditeStack = OreDictionary.getOres("ingotArdite").get(0);
			arditeStack.setCount(1);
			RecipeHandler.addRecipe(new AlloySmelterRecipe(cobaltStack, arditeStack, manyullynStack, 200, 16));
		}

		// Conductive Iron
		if (OreUtil.doesOreExistAndValid("ingotConductiveIron")) {
			ItemStack conductiveIronStack = OreDictionary.getOres("ingotConductiveIron").get(0);
			conductiveIronStack.setCount(1);
			RecipeHandler.addRecipe(
				new AlloySmelterRecipe(new ItemStack(Items.REDSTONE, 1), new ItemStack(Items.IRON_INGOT, 1),
					conductiveIronStack, 200, 16));
		}

		// Redstone Alloy
		if (OreUtil.doesOreExistAndValid("ingotRedstoneAlloy") && OreUtil.doesOreExistAndValid("itemSilicon")) {
			ItemStack redstoneAlloyStack = OreDictionary.getOres("ingotRedstoneAlloy").get(0);
			redstoneAlloyStack.setCount(1);
			ItemStack siliconStack = OreDictionary.getOres("itemSilicon").get(0);
			siliconStack.setCount(1);
			RecipeHandler.addRecipe(
				new AlloySmelterRecipe(new ItemStack(Items.REDSTONE, 1), siliconStack, redstoneAlloyStack, 200,
					16));
		}

		// Pulsating Iron
		if (OreUtil.doesOreExistAndValid("ingotPhasedIron")) {
			ItemStack pulsatingIronStack = OreDictionary.getOres("ingotPhasedIron").get(0);
			pulsatingIronStack.setCount(1);
			RecipeHandler.addRecipe(
				new AlloySmelterRecipe(new ItemStack(Items.IRON_INGOT, 1), new ItemStack(Items.ENDER_PEARL, 1),
					pulsatingIronStack, 200, 16));
			RecipeHandler.addRecipe(
				new AlloySmelterRecipe(new ItemStack(Items.IRON_INGOT, 1), ItemDusts.getDustByName("ender_pearl", 1),
					pulsatingIronStack, 200, 16));
		}

		// Vibrant Alloy
		if (OreUtil.doesOreExistAndValid("ingotEnergeticAlloy") && OreUtil.doesOreExistAndValid("ingotPhasedGold")) {
			ItemStack energeticAlloyStack = OreDictionary.getOres("ingotEnergeticAlloy").get(0);
			energeticAlloyStack.setCount(1);
			ItemStack vibrantAlloyStack = OreDictionary.getOres("ingotPhasedGold").get(0);
			vibrantAlloyStack.setCount(1);
			RecipeHandler.addRecipe(
				new AlloySmelterRecipe(energeticAlloyStack, new ItemStack(Items.ENDER_PEARL, 1), vibrantAlloyStack,
					200, 16));
			RecipeHandler.addRecipe(
				new AlloySmelterRecipe(energeticAlloyStack, ItemDusts.getDustByName("ender_pearl", 1),
					vibrantAlloyStack, 200, 16));
		}

		// Soularium
		if (OreUtil.doesOreExistAndValid("ingotSoularium")) {
			ItemStack soulariumStack = OreDictionary.getOres("ingotSoularium").get(0);
			soulariumStack.setCount(1);
			RecipeHandler.addRecipe(
				new AlloySmelterRecipe(new ItemStack(Blocks.SOUL_SAND, 1), new ItemStack(Items.GOLD_INGOT, 1),
					soulariumStack, 200, 16));
		}

	}

	static void addBlastFurnaceRecipes() {
		RecipeHandler.addRecipe(
			new BlastFurnaceRecipe(ItemDusts.getDustByName("titanium"), null, ItemIngots.getIngotByName("titanium"),
				null, 3600, 120, 1500));
		RecipeHandler.addRecipe(new BlastFurnaceRecipe(ItemDustsSmall.getSmallDustByName("titanium", 4), null,
			ItemIngots.getIngotByName("titanium"), null, 3600, 120, 1500));
		RecipeHandler.addRecipe(
			new BlastFurnaceRecipe(ItemDusts.getDustByName("aluminum"), null, ItemIngots.getIngotByName("aluminum"),
				null, 2200, 120, 1700));
		RecipeHandler.addRecipe(new BlastFurnaceRecipe(ItemDustsSmall.getSmallDustByName("aluminum", 4), null,
			ItemIngots.getIngotByName("aluminum"), null, 2200, 120, 1700));
		RecipeHandler.addRecipe(
			new BlastFurnaceRecipe(ItemDusts.getDustByName("tungsten"), null, ItemIngots.getIngotByName("tungsten"),
				null, 18000, 120, 2500));
		RecipeHandler.addRecipe(new BlastFurnaceRecipe(ItemDustsSmall.getSmallDustByName("tungsten", 4), null,
			ItemIngots.getIngotByName("tungsten"), null, 18000, 120, 2500));
		RecipeHandler.addRecipe(
			new BlastFurnaceRecipe(ItemDusts.getDustByName("chrome"), null, ItemIngots.getIngotByName("chrome"),
				null, 4420, 120, 1700));
		RecipeHandler.addRecipe(new BlastFurnaceRecipe(ItemDustsSmall.getSmallDustByName("chrome", 4), null,
			ItemIngots.getIngotByName("chrome"), null, 4420, 120, 1700));
		RecipeHandler.addRecipe(
			new BlastFurnaceRecipe(ItemDusts.getDustByName("steel"), null, ItemIngots.getIngotByName("steel"), null,
				2800, 120, 1000));
		RecipeHandler.addRecipe(new BlastFurnaceRecipe(ItemDustsSmall.getSmallDustByName("steel", 4), null,
			ItemIngots.getIngotByName("steel"), null, 2800, 120, 1000));

		RecipeHandler.addRecipe(
			new BlastFurnaceRecipe(ItemDusts.getDustByName("galena", 2), null, ItemIngots.getIngotByName("silver"),
				ItemIngots.getIngotByName("lead"), 80, 120, 1500));

		RecipeHandler.addRecipe(
			new BlastFurnaceRecipe(new ItemStack(Items.IRON_INGOT), ItemDusts.getDustByName("coal", 2),
				ItemIngots.getIngotByName("steel"), ItemDusts.getDustByName("dark_ashes", 2), 500, 120, 1000));

		RecipeHandler.addRecipe(
			new BlastFurnaceRecipe(ItemIngots.getIngotByName("tungsten"), ItemIngots.getIngotByName("steel"),
				ItemIngots.getIngotByName("hot_tungstensteel"), ItemDusts.getDustByName("dark_ashes", 4), 500,
				500, 3000));

		RecipeHandler.addRecipe(
			new BlastFurnaceRecipe(new ItemStack(Blocks.IRON_ORE), ItemDusts.getDustByName("calcite"),
				new ItemStack(Items.IRON_INGOT, 3), ItemDusts.getDustByName("dark_ashes"), 140, 120, 1000));

		RecipeHandler.addRecipe(
			new BlastFurnaceRecipe(BlockOre.getOreByName("Pyrite"), ItemDusts.getDustByName("calcite"),
				new ItemStack(Items.IRON_INGOT, 2), ItemDusts.getDustByName("dark_ashes"), 140, 120, 1000));
	}

	static void addChemicalReactorRecipes() {
		RecipeHandler.addRecipe(
			new ChemicalReactorRecipe(ItemCells.getCellByName("calcium", 1), ItemCells.getCellByName("carbon", 1),
				ItemCells.getCellByName("calciumCarbonate", 2), 240, 30));
		RecipeHandler.addRecipe(
			new ChemicalReactorRecipe(new ItemStack(Items.GOLD_NUGGET, 8), new ItemStack(Items.MELON, 1),
				new ItemStack(Items.SPECKLED_MELON, 1), 40, 30));
		RecipeHandler.addRecipe(
			new ChemicalReactorRecipe(ItemCells.getCellByName("nitrogen", 1), ItemCells.getCellByName("carbon", 1),
				ItemCells.getCellByName("nitrocarbon", 2), 1500, 30));
		RecipeHandler.addRecipe(
			new ChemicalReactorRecipe(ItemCells.getCellByName("carbon", 1), ItemCells.getCellByName("hydrogen", 4),
				ItemCells.getCellByName("methane", 5), 3500, 30));
		RecipeHandler.addRecipe(
			new ChemicalReactorRecipe(ItemCells.getCellByName("sulfur", 1), ItemCells.getCellByName("sodium", 1),
				ItemCells.getCellByName("sodiumSulfide", 2), 100, 30));
		RecipeHandler.addRecipe(
			new ChemicalReactorRecipe(new ItemStack(Items.BLAZE_POWDER, 1), new ItemStack(Items.ENDER_PEARL, 1),
				new ItemStack(Items.ENDER_EYE, 1), 40, 30));
		RecipeHandler.addRecipe(
			new ChemicalReactorRecipe(new ItemStack(Items.GOLD_NUGGET, 8), new ItemStack(Items.CARROT, 1),
				new ItemStack(Items.GOLDEN_CARROT, 1), 40, 30));
		RecipeHandler.addRecipe(
			new ChemicalReactorRecipe(ItemCells.getCellByName("glyceryl", 1), ItemCells.getCellByName("diesel", 4),
				ItemCells.getCellByName("nitroDiesel", 5), 1000, 30));
		RecipeHandler.addRecipe(
			new ChemicalReactorRecipe(new ItemStack(Items.GOLD_INGOT, 8), new ItemStack(Items.APPLE, 1),
				new ItemStack(Items.GOLDEN_APPLE, 1), 40, 30));
		RecipeHandler.addRecipe(
			new ChemicalReactorRecipe(new ItemStack(Blocks.GOLD_BLOCK, 8), new ItemStack(Items.APPLE, 1),
				new ItemStack(Items.GOLDEN_APPLE, 1, 1), 40, 30));
		RecipeHandler.addRecipe(
			new ChemicalReactorRecipe(new ItemStack(Items.BLAZE_POWDER, 1), new ItemStack(Items.SLIME_BALL, 1),
				new ItemStack(Items.MAGMA_CREAM, 1), 40, 30));
	}

	static void addIc2Recipes() {
		CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.MANUAL), IC2Duplicates.REFINED_IRON.getStackBasedOnConfig(),
			Items.BOOK);

		CraftingHelper
			.addShapedOreRecipe(ItemParts.getPartByName("machineParts", 16), "CSC", "SCS", "CSC", 'S', "ingotSteel",
				'C', "circuitBasic");

		CraftingHelper.addShapedOreRecipe(ItemParts.getPartByName("energyFlowCircuit", 4), "ATA", "LIL", "ATA", 'T',
			"ingotTungsten", 'I', "plateIridium", 'A', "circuitAdvanced", 'L',
			"lapotronCrystal");

		CraftingHelper.addShapedOreRecipe(ItemParts.getPartByName("superconductor", 4), "CCC", "TIT", "EEE", 'E',
			"circuitMaster", 'C', ItemParts.getPartByName("heliumCoolantSimple"), 'T',
			"ingotTungsten", 'I', "plateIridium");

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.LAPOTRONIC_ORB), "LLL", "LPL", "LLL", 'L',
			"lapotronCrystal", 'P', "plateIridium");

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.INDUSTRIAL_SAWMILL), "PAP", "SSS", "ACA", 'P',
			IC2Duplicates.REFINED_IRON.getStackBasedOnConfig(), 'A', "circuitAdvanced",
			'S', ItemParts.getPartByName("diamondSawBlade"), 'C',
			"machineBlockAdvanced");

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.CHARGE_O_MAT), "ETE", "COC", "EAD", 'E',
			"circuitMaster", 'T', ModItems.ENERGY_CRYSTAL, 'C', Blocks.CHEST, 'O',
			ModItems.LAPOTRONIC_ORB, 'A', "machineBlockAdvanced");

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.MATTER_FABRICATOR), "ETE", "AOA", "ETE", 'E',
			"circuitMaster", 'T', IC2Duplicates.EXTRACTOR.getStackBasedOnConfig(), 'A',
			BlockMachineFrame.getFrameByName("highlyAdvancedMachine", 1), 'O', ModItems.LAPOTRONIC_ORB);

		CraftingHelper
			.addShapedOreRecipe(new ItemStack(ModBlocks.HEAT_GENERATOR), "III", "IHI", "CGC", 'I', "plateIron", 'H',
				new ItemStack(Blocks.IRON_BARS), 'C', "circuitBasic", 'G', IC2Duplicates.GENERATOR.getStackBasedOnConfig());

		CraftingHelper
			.addShapedOreRecipe(new ItemStack(ModBlocks.GAS_TURBINE), "IAI", "WGW", "IAI", 'I', "ingotInvar", 'A',
				"circuitAdvanced", 'W',
				getOre("ic2Windmill"), 'G',
				getOre("glassReinforced"));

		CraftingHelper
			.addShapedOreRecipe(new ItemStack(ModBlocks.GAS_TURBINE), "IAI", "WGW", "IAI", 'I', "ingotAluminum", 'A',
				"circuitAdvanced", 'W',
				getOre("ic2Windmill"), 'G',
				getOre("glassReinforced"));

		CraftingHelper
			.addShapedOreRecipe(new ItemStack(ModBlocks.SEMIFLUID_GENERATOR), "III", "IHI", "CGC", 'I', "plateIron",
				'H', ModBlocks.REINFORCED_GLASS, 'C', "circuitBasic", 'G',
				IC2Duplicates.GENERATOR.getStackBasedOnConfig());

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.SEMIFLUID_GENERATOR), "AAA", "AHA", "CGC", 'A',
			"plateAluminum", 'H', ModBlocks.REINFORCED_GLASS, 'C', "circuitBasic", 'G',
			IC2Duplicates.GENERATOR.getStackBasedOnConfig());

		CraftingHelper
			.addShapedOreRecipe(new ItemStack(ModBlocks.DIESEL_GENERATOR), "III", "I I", "CGC", 'I', IC2Duplicates.REFINED_IRON.getStackBasedOnConfig(),
				'C', "circuitBasic", 'G', IC2Duplicates.GENERATOR.getStackBasedOnConfig());

		CraftingHelper
			.addShapedOreRecipe(new ItemStack(ModBlocks.DIESEL_GENERATOR), "AAA", "A A", "CGC", 'A', "ingotAluminum",
				'C', "circuitBasic", 'G', IC2Duplicates.GENERATOR.getStackBasedOnConfig());

		// CraftingHelper.addShapedOreRecipe(new
		// ItemStack(ModBlocks.magicalAbsorber),
		// "CSC", "IBI", "CAC",
		// 'C', "circuitMaster",
		// 'S', "craftingSuperconductor",
		// 'B', Blocks.beacon,
		// 'A', ModBlocks.magicEnergeyConverter,
		// 'I', "plateIridium");
		//
		// CraftingHelper.addShapedOreRecipe(new
		// ItemStack(ModBlocks.magicEnergeyConverter),
		// "CTC", "PBP", "CLC",
		// 'C', "circuitAdvanced",
		// 'P', "platePlatinum",
		// 'B', Blocks.beacon,
		// 'L', "lapotronCrystal",
		// 'T', TechRebornAPI.recipeCompact.getItem("teleporter"));

		//		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.dragonEggEnergySiphoner), "CTC", "ISI", "CBC", 'I',
		//			"plateIridium", 'C', "circuitBasic",
		//			'B', ModItems.lithiumBattery, 'S', ModBlocks.Supercondensator, 'T', ModBlocks.extractor);

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.INDUSTRIAL_BLAST_FURNACE), "CHC", "HBH", "FHF", 'H',
			ItemParts.getPartByName("cupronickelHeatingCoil"), 'C', "circuitAdvanced", 'B',
			BlockMachineFrame.getFrameByName("advancedMachine", 1), 'F', IC2Duplicates.ELECTRICAL_FURNACE.getStackBasedOnConfig());

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.INDUSTRIAL_GRINDER), "ECG", "HHH", "CBC", 'E',
			ModBlocks.INDUSTRIAL_ELECTROLYZER, 'H', "craftingGrinder", 'C',
			"circuitAdvanced", 'B', "machineBlockAdvanced",
			'G', IC2Duplicates.GRINDER.getStackBasedOnConfig());

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.IMPLOSION_COMPRESSOR), "ABA", "CPC", "ABA", 'A',
			ItemIngots.getIngotByName("advancedAlloy"), 'C', "circuitAdvanced", 'B',
			BlockMachineFrame.getFrameByName("advancedMachine", 1), 'P', IC2Duplicates.COMPRESSOR.getStackBasedOnConfig());

		CraftingHelper
			.addShapedOreRecipe(new ItemStack(ModBlocks.VACUUM_FREEZER), "SPS", "CGC", "SPS", 'S', "plateSteel", 'C',
				"circuitAdvanced", 'G', ModBlocks.REINFORCED_GLASS, 'P',
				IC2Duplicates.EXTRACTOR.getStackBasedOnConfig());

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.DISTILLATION_TOWER), "CMC", "PBP", "EME", 'E',
			ModBlocks.INDUSTRIAL_ELECTROLYZER, 'M', "circuitMaster", 'B',
			"machineBlockAdvanced", 'C', ModBlocks.INDUSTRIAL_CENTRIFUGE, 'P',
			IC2Duplicates.EXTRACTOR.getStackBasedOnConfig());

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.IRON_ALLOY_FURNACE), "III", "F F", "III", 'I',
			ItemIngots.getIngotByName("refined_iron"), 'F', new ItemStack(ModBlocks.IRON_FURNACE));
		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.IRON_ALLOY_FURNACE), "III", "F F", "III", 'I',
			IC2Duplicates.REFINED_IRON.getStackBasedOnConfig(), 'F', IC2Duplicates.IRON_FURNACE.getStackBasedOnConfig());

		CraftingHelper
			.addShapedOreRecipe(new ItemStack(ModBlocks.CHEMICAL_REACTOR), "IMI", "CPC", "IEI", 'I', "ingotInvar",
				'C', "circuitAdvanced", 'M', IC2Duplicates.EXTRACTOR.getStackBasedOnConfig(), 'P',
				IC2Duplicates.COMPRESSOR.getStackBasedOnConfig(), 'E', IC2Duplicates.EXTRACTOR.getStackBasedOnConfig());

		CraftingHelper
			.addShapedOreRecipe(new ItemStack(ModBlocks.ROLLING_MACHINE), "PCP", "MBM", "PCP", 'P', Blocks.PISTON,
				'C', "circuitAdvanced", 'M', IC2Duplicates.COMPRESSOR.getStackBasedOnConfig(), 'B',
				"machineBlockBasic");

		// CraftingHelper.addShapedOreRecipe(new
		// ItemStack(ModBlocks.electricCraftingTable),
		// "ITI", "IBI", "ICI",
		// 'I', "plateIron",
		// 'C', "circuitAdvanced",
		// 'T', "crafterWood",
		// 'B', "machineBlockBasic");

		// CraftingHelper.addShapedOreRecipe(new
		// ItemStack(ModBlocks.electricCraftingTable),
		// "ATA", "ABA", "ACA",
		// 'A', "plateAluminum",
		// 'C', "circuitAdvanced",
		// 'T', "crafterWood",
		// 'B', "machineBlockBasic");

		// CraftingHelper.addShapedOreRecipe(new
		// ItemStack(ModBlocks.chunkLoader),
		// "SCS", "CMC", "SCS",
		// 'S', "plateSteel",
		// 'C', "circuitMaster",
		// 'M', new ItemStack(ModItems.parts, 1, 39));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.LAPOTRONIC_SU), " L ", "CBC", " M ", 'L', IC2Duplicates.LVT.getStackBasedOnConfig(), 'C',
			"circuitAdvanced", 'M', IC2Duplicates.MVT.getStackBasedOnConfig(), 'B', ModBlocks.LSU_STORAGE_BLOCK);

		CraftingHelper
			.addShapedOreRecipe(BlockMachineFrame.getFrameByName("highlyAdvancedMachine", 1), "CTC", "TBT", "CTC",
				'C', "ingotChrome", 'T', "ingotTitanium", 'B',
				"machineBlockAdvanced");

		CraftingHelper
			.addShapedOreRecipe(new ItemStack(ModBlocks.MACHINE_CASINGS, 4, 0), "III", "CBC", "III", 'I', "plateIron",
				'C', "circuitBasic", 'B', "machineBlockBasic");

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.MACHINE_CASINGS, 4, 1), "SSS", "CBC", "SSS", 'S',
			"plateSteel", 'C', "circuitAdvanced", 'B', "machineBlockAdvanced");

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.MACHINE_CASINGS, 4, 2), "HHH", "CBC", "HHH", 'H',
			"ingotChrome", 'C', "circuitElite", 'B', BlockMachineFrame.getFrameByName("highlyAdvancedMachine", 1));

		if (!CompatManager.isQuantumStorageLoaded) {
			CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.QUANTUM_CHEST), "DCD", "ATA", "DQD", 'D',
				ItemParts.getPartByName("dataOrb"), 'C', ItemParts.getPartByName("computerMonitor"), 'A',
				BlockMachineFrame.getFrameByName("highlyAdvancedMachine", 1), 'Q', ModBlocks.DIGITAL_CHEST, 'T',
				IC2Duplicates.COMPRESSOR.getStackBasedOnConfig());
		}

		//		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.PlasmaGenerator), "PPP", "PTP", "CGC", 'P',
		//			ItemPlates.getPlateByName("tungstensteel"), 'T', IC2Duplicates.HVT.getStackBasedOnConfig(),
		//			'G', IC2Duplicates.GENERATOR.getStackBasedOnConfig(), 'C',
		//			"circuitMaster");

		// Smetling
		CraftingHelper
			.addSmelting(ItemDusts.getDustByName("copper", 1), getOre("ingotCopper"),
				1F);
		CraftingHelper
			.addSmelting(ItemDusts.getDustByName("tin", 1), ItemIngots.getIngotByName("tin"), 1F);
		CraftingHelper
			.addSmelting(ItemDusts.getDustByName("bronze", 1), ItemIngots.getIngotByName("bronze"),
				1F);
		CraftingHelper
			.addSmelting(ItemDusts.getDustByName("lead", 1), ItemIngots.getIngotByName("lead"), 1F);
		CraftingHelper
			.addSmelting(ItemDusts.getDustByName("silver", 1), ItemIngots.getIngotByName("silver"),
				1F);

		// UU
		if (ConfigTechReborn.UUrecipesIridiamOre)
			CraftingHelper
				.addShapedOreRecipe((OreDictionary.getOres("oreIridium").get(0)), "UUU", " U ", "UUU", 'U',
					ModItems.UU_MATTER);

		// Chemical Reactor
		RecipeHandler.addRecipe(new ChemicalReactorRecipe(ItemDusts.getDustByName("calcite", 1), null,
			new ItemStack(OreDictionary.getOres("fertilizer").get(0).getItem(), 1), 100, 30));
		RecipeHandler.addRecipe(new ChemicalReactorRecipe(ItemDusts.getDustByName("calcite", 1),
			ItemDusts.getDustByName("phosphorous", 1),
			new ItemStack(OreDictionary.getOres("fertilizer").get(0).getItem(), 3), 100, 30));
		RecipeHandler.addRecipe(new ChemicalReactorRecipe(ItemCells.getCellByName("sodiumSulfide", 1),
			ItemCells.getCellByName("empty"), ItemCells.getCellByName("sodiumPersulfate", 2), 2000,
			30));
		RecipeHandler.addRecipe(new ChemicalReactorRecipe(ItemCells.getCellByName("nitrocarbon", 1),
			ItemCells.getCellByName("water"), ItemCells.getCellByName("glyceryl", 2), 580, 30));

		RecipeHandler.addRecipe(
			new ChemicalReactorRecipe(ItemDusts.getDustByName("calcite", 1), ItemDusts.getDustByName("sulfur", 1),
				new ItemStack(OreDictionary.getOres("fertilizer").get(0).getItem(), 2), 100, 30));

		ItemStack waterCells = ItemCells.getCellByName("water").copy();
		waterCells.setCount(2);

		RecipeHandler.addRecipe(new ChemicalReactorRecipe(ItemCells.getCellByName("sulfur", 1), waterCells,
			ItemCells.getCellByName("sulfuricAcid", 3), 1140, 30));

		ItemStack waterCells2 = ItemCells.getCellByName("water").copy();
		waterCells2.setCount(5);

		RecipeHandler.addRecipe(new ChemicalReactorRecipe(ItemCells.getCellByName("hydrogen", 4),
			ItemCells.getCellByName("empty"), waterCells2, 10, 30));

		RecipeHandler.addRecipe(new ChemicalReactorRecipe(ItemCells.getCellByName("nitrogen", 1),
			ItemCells.getCellByName("empty"), ItemCells.getCellByName("nitrogenDioxide", 2), 1240,
			30));

		if (ConfigTechReborn.ExpensiveMacerator && !IC2Duplicates.deduplicate())
			CraftingHelper
				.addShapedOreRecipe(getOre("ic2Macerator"), "FDF", "DMD", "FCF", 'F',
					Items.FLINT, 'D', Items.DIAMOND, 'M', "machineBlockBasic", 'C',
					"circuitBasic");

		if (ConfigTechReborn.ExpensiveSolar && !IC2Duplicates.deduplicate())
			CraftingHelper
				.addShapedOreRecipe(IC2Duplicates.SOLAR_PANEL.getStackBasedOnConfig(), "PPP", "SZS", "CGC", 'P',
					"paneGlass", 'S', "platelazurite", 'Z',
					"plateCarbon", 'G',
					IC2Duplicates.GENERATOR.getStackBasedOnConfig(), 'C',
					"circuitBasic");

		CraftingHelper.addShapedOreRecipe(ItemIngots.getIngotByName("iridium_alloy"), "IAI", "ADA", "IAI", 'I',
			"ingotIridium", 'D', ItemDusts.getDustByName("diamond"), 'A',
			"plateAdvancedAlloy");

		CraftingHelper
			.addShapedOreRecipe(new ItemStack(ModItems.LITHIUM_BATTERY_PACK, 1, OreDictionary.WILDCARD_VALUE), "BCB",
				"BPB", "B B", 'B', new ItemStack(ModItems.LITHIUM_BATTERY), 'P', "plateAluminum", 'C',
				"circuitAdvanced");

		CraftingHelper
			.addShapedOreRecipe(new ItemStack(ModItems.LITHIUM_BATTERY, 1, OreDictionary.WILDCARD_VALUE), " C ",
				"PFP", "PFP", 'F', ItemCells.getCellByName("lithium"), 'P', "plateAluminum", 'C',
				IC2Duplicates.CABLE_IGOLD.getStackBasedOnConfig());

		CraftingHelper
			.addShapedOreRecipe(new ItemStack(ModItems.LAPOTRONIC_ORB_PACK, 1, OreDictionary.WILDCARD_VALUE), "FOF", "SPS",
				"FIF", 'F', "circuitMaster", 'O',
				new ItemStack(ModItems.LAPOTRONIC_ORB), 'S', ItemParts.getPartByName("superConductor"), 'I',
				"ingotIridium", 'P', new ItemStack(ModItems.LITHIUM_BATTERY_PACK));
	}

	public static ItemStack getBucketWithFluid(Fluid fluid) {
		return UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, fluid);
	}

	public static ItemStack getOre(String name) {
		if (OreDictionary.getOres(name).isEmpty()) {
			return new ItemStack(ModItems.MISSING_RECIPE_PLACEHOLDER);
		}
		return OreDictionary.getOres(name).get(0).copy();
	}

}

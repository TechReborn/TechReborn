package techreborn.init;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.ArrayUtils;
import techreborn.api.TechRebornAPI;
import techreborn.api.recipe.RecipeHandler;
import techreborn.api.recipe.machines.AlloySmelterRecipe;
import techreborn.api.recipe.machines.AssemblingMachineRecipe;
import techreborn.api.recipe.machines.BlastFurnaceRecipe;
import techreborn.api.recipe.machines.CentrifugeRecipe;
import techreborn.api.recipe.machines.ChemicalReactorRecipe;
import techreborn.api.recipe.machines.GrinderRecipe;
import techreborn.api.recipe.machines.IndustrialElectrolyzerRecipe;
import techreborn.api.recipe.machines.IndustrialSawmillRecipe;
import techreborn.api.recipe.machines.LatheRecipe;
import techreborn.api.recipe.machines.PlateCuttingMachineRecipe;
import techreborn.blocks.BlockMachineFrame;
import techreborn.blocks.BlockOre;
import techreborn.blocks.BlockStorage;
import techreborn.blocks.BlockStorage2;
import techreborn.config.ConfigTechReborn;
import techreborn.farm.FarmTree;
import techreborn.items.ItemCells;
import techreborn.items.ItemDusts;
import techreborn.items.ItemDustsSmall;
import techreborn.items.ItemDustsTiny;
import techreborn.items.ItemGems;
import techreborn.items.ItemIngots;
import techreborn.items.ItemParts;
import techreborn.items.ItemPlates;
import techreborn.items.ItemRods;
import techreborn.util.CraftingHelper;
import techreborn.util.LogHelper;

import java.security.InvalidParameterException;

public class ModRecipes {
	public static ConfigTechReborn config;

	public static void init() {
		addShapelessRecipes();
		addGeneralShapedRecipes();
		addHammerRecipes();
		addMachineRecipes();

		addSmeltingRecipes();
		addUUrecipes();

		addAlloySmelterRecipes();
		addLatheRecipes();
		addPlateCuttingMachineRecipes();
		addAssemblingMachineRecipes();
		addIndustrialCentrifugeRecipes();
		addChemicalReactorRecipes();
		addIndustrialElectrolyzerRecipes();

		addIndustrialSawmillRecipes();
		addBlastFurnaceRecipes();
		addIndustrialGrinderRecipes();
		addImplosionCompressorRecipes();
		addLogs();
	}

	static void addGeneralShapedRecipes() {

		// Storage Blocks
		for (String name : ArrayUtils.addAll(BlockStorage.types, BlockStorage2.types)) {
			CraftingHelper.addShapedOreRecipe(BlockStorage.getStorageBlockByName(name),
					"AAA", "AAA", "AAA",
					'A', "ingot" + name.substring(0, 1).toUpperCase() + name.substring(1));
		}

		CraftingHelper.addShapedOreRecipe(BlockStorage.getStorageBlockByName("sapphire"),
				"AAA", "AAA", "AAA",
				'A', "gemSapphire");

		CraftingHelper.addShapedOreRecipe(BlockStorage.getStorageBlockByName("ruby"),
				"AAA", "AAA", "AAA",
				'A', "gemRuby");


		CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.parts, 1, 40),
				"PLP", "RGB", "PYP",
				'P', "plateAluminum",
				'L', "dyeLime",
				'R', "dyeRed",
				'G', "paneGlass",
				'B', "dyeBlue",
				'Y', Items.glowstone_dust);

		CraftingHelper.addShapedOreRecipe(ItemParts.getPartByName("dataStorageCircuit"),
				"EEE", "ECE", "EEE",
				'E', new ItemStack(Items.emerald),
				'C', ItemParts.getPartByName("basicCircuitBoard"));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.parts, 4, 8),
				"DSD", "S S", "DSD",
				'D', "dustDiamond",
				'S', "ingotSteel");


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

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.parts, 1, 18),
				"AAA", "AMA", "AAA",
				'A', "ingotTitanium",
				'M', new ItemStack(ModItems.parts, 1, 13));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.parts, 1, 19),
				"AAA", "AMA", "AAA",
				'A', "ingotBrass",
				'M', new ItemStack(ModItems.parts, 1, 13));


		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Supercondensator),
				"EOE", "SAS", "EOE",
				'E', ItemParts.getPartByName("energyFlowCircuit"),
				'O', ModItems.lapotronicOrb,
				'S', ItemParts.getPartByName("superconductor"),
				'A', ModBlocks.HighAdvancedMachineBlock);

		CraftingHelper.addShapedOreRecipe(ItemParts.getPartByName("diamondSawBlade"),
				"DSD", "S S", "DSD",
				'S', "plateSteel",
				'D', "dustDiamond");

		CraftingHelper.addShapedOreRecipe(ItemParts.getPartByName("tungstenGrindingHead", 2),
				"TST", "SBS", "TST",
				'T', "plateTungsten",
				'S', "plateSteel",
				'B', "blockSteel");

		CraftingHelper.addShapedOreRecipe(ItemParts.getPartByName("destructoPack"),
				"CIC", "IBI", "CIC",
				'C', ItemParts.getPartByName("basicCircuitBoard"),
				'I', "ingotAluminum",
				'B', new ItemStack(Items.lava_bucket)
		);

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.cloakingDevice),
				"CIC", "IOI", "CIC",
				'C', "ingotChrome",
				'I', "plateIridium",
				'O', new ItemStack(ModItems.lapotronicOrb)
		);

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.rockCutter),
				"DT ", "DT ", "DCB",
				'D', ItemParts.getPartByName("rockCutterBlade"),
				'T', "ingotTitanium",
				'C', ItemParts.getPartByName("basicCircuitBoard"),
				'B', new ItemStack(Items.diamond)
		);

		CraftingHelper.addShapedOreRecipe(ItemParts.getPartByName("rockCutterBlade"),
				"SDS", "SDS", "SDS",
				'D', new ItemStack(Items.diamond),
				'S', "ingotSteel"
		);

		for (String part : ItemParts.types) {
			if (part.endsWith("Gear")) {
				CraftingHelper.addShapedOreRecipe(ItemParts.getPartByName(part),
						" O ", "OIO", " O ",
						'I', new ItemStack(Items.iron_ingot),
						'O', "ingot" + capitalizeFirstLetter(part.replace("Gear", ""))
				);
			}
		}

		LogHelper.info("Shapped Recipes Added");
	}

	public static String capitalizeFirstLetter(String original) {
		if (original.length() == 0)
			return original;
		return original.substring(0, 1).toUpperCase() + original.substring(1);
	}

	static void addShapelessRecipes() {

		for (String name : ArrayUtils.addAll(BlockStorage.types, BlockStorage2.types)) {
			ItemStack item = null;
			try {
				item = ItemIngots.getIngotByName(name, 9);
			} catch (InvalidParameterException e) {
				try {
					item = ItemGems.getGemByName(name, 9);
				} catch (InvalidParameterException e2) {
					continue;
				}
			}

			if (item == null) {
				continue;
			}

			GameRegistry.addShapelessRecipe(BlockStorage.getStorageBlockByName(name), item, item, item, item, item, item, item, item, item);
			GameRegistry.addShapelessRecipe(item, BlockStorage.getStorageBlockByName(name, 9));

		}

		for (String name : ItemDustsSmall.types) {
			GameRegistry.addShapelessRecipe(ItemDustsSmall.getSmallDustByName(name, 4), ItemDusts.getDustByName(name));
			GameRegistry.addShapelessRecipe(ItemDustsTiny.getTinyDustByName(name, 9), ItemDusts.getDustByName(name));
			GameRegistry.addShapelessRecipe(ItemDusts.getDustByName(name, 1), ItemDustsSmall.getSmallDustByName(name), ItemDustsSmall.getSmallDustByName(name), ItemDustsSmall.getSmallDustByName(name), ItemDustsSmall.getSmallDustByName(name));
			GameRegistry.addShapelessRecipe(ItemDusts.getDustByName(name, 1), ItemDustsTiny.getTinyDustByName(name), ItemDustsTiny.getTinyDustByName(name), ItemDustsTiny.getTinyDustByName(name), ItemDustsTiny.getTinyDustByName(name), ItemDustsTiny.getTinyDustByName(name), ItemDustsTiny.getTinyDustByName(name), ItemDustsTiny.getTinyDustByName(name), ItemDustsTiny.getTinyDustByName(name), ItemDustsTiny.getTinyDustByName(name));
		}


//		CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.gems, 9, 1), "blockSapphire");
//		CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.gems, 9, 0), "blockRuby");
//		CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.gems, 9, 2), "blockGreenSapphire");

		LogHelper.info("Shapless Recipes Added");
	}

	static void addMachineRecipes() {
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

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.AlloySmelter),
				"IHI", "CFC", "IHI",
				'I', "plateInvar",
				'C', "circuitBasic",
				'H', new ItemStack(ModItems.parts, 1, 17),
				'F', ModBlocks.AlloyFurnace);

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


		TechRebornAPI.addRollingMachinceRecipe(ItemParts.getPartByName("cupronickelHeatingCoil"),
				"NCN", "C C", "NCN",
				'N', ItemIngots.getIngotByName("cupronickel"),
				'C', ItemIngots.getIngotByName("copper"));

	}

	static void addSmeltingRecipes() {
		GameRegistry.addSmelting(ItemDusts.getDustByName("iron", 1), new ItemStack(Items.iron_ingot), 1F);
		GameRegistry.addSmelting(ItemDusts.getDustByName("gold", 1), new ItemStack(Items.gold_ingot), 1F);

		LogHelper.info("Smelting Recipes Added");
	}

	static void addHammerRecipes() {
		ItemStack hammerIron = new ItemStack(ModItems.hammerIron, 1, OreDictionary.WILDCARD_VALUE);
		ItemStack hammerDiamond = new ItemStack(ModItems.hammerDiamond, 1, OreDictionary.WILDCARD_VALUE);

	}

	static void addAlloySmelterRecipes() {
		//Bronze
		RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemIngots.getIngotByName("copper", 3), ItemIngots.getIngotByName("tin", 1), ItemIngots.getIngotByName("bronze", 4), 200, 16));
		RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemIngots.getIngotByName("copper", 3), ItemDusts.getDustByName("tin", 1), ItemIngots.getIngotByName("bronze", 4), 200, 16));
		RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemDusts.getDustByName("copper", 3), ItemIngots.getIngotByName("tin", 1), ItemIngots.getIngotByName("bronze", 4), 200, 16));
		RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemDusts.getDustByName("copper", 3), ItemDusts.getDustByName("tin", 1), ItemIngots.getIngotByName("bronze", 4), 200, 16));

		//Electrum
		RecipeHandler.addRecipe(new AlloySmelterRecipe(new ItemStack(Items.gold_ingot, 1), ItemIngots.getIngotByName("silver", 1), ItemIngots.getIngotByName("electrum", 2), 200, 16));
		RecipeHandler.addRecipe(new AlloySmelterRecipe(new ItemStack(Items.gold_ingot, 1), ItemDusts.getDustByName("silver", 1), ItemIngots.getIngotByName("electrum", 2), 200, 16));
		RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemDusts.getDustByName("gold", 1), ItemIngots.getIngotByName("silver", 1), ItemIngots.getIngotByName("electrum", 2), 200, 16));
		RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemDusts.getDustByName("gold", 1), ItemDusts.getDustByName("silver", 1), ItemIngots.getIngotByName("electrum", 2), 200, 16));

		//Invar
		RecipeHandler.addRecipe(new AlloySmelterRecipe(new ItemStack(Items.iron_ingot, 2), ItemIngots.getIngotByName("nickel", 1), ItemIngots.getIngotByName("invar", 3), 200, 16));
		RecipeHandler.addRecipe(new AlloySmelterRecipe(new ItemStack(Items.iron_ingot, 2), ItemDusts.getDustByName("nickel", 1), ItemIngots.getIngotByName("invar", 3), 200, 16));
		RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemDusts.getDustByName("iron", 2), ItemIngots.getIngotByName("nickel", 1), ItemIngots.getIngotByName("invar", 3), 200, 16));
		RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemDusts.getDustByName("iron", 2), ItemDusts.getDustByName("nickel", 1), ItemIngots.getIngotByName("invar", 3), 200, 16));

		//Cupronickel
		RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemIngots.getIngotByName("copper", 1), ItemIngots.getIngotByName("nickel", 1), ItemIngots.getIngotByName("cupronickel", 2), 200, 16));
		RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemIngots.getIngotByName("copper", 1), ItemDusts.getDustByName("nickel", 1), ItemIngots.getIngotByName("cupronickel", 2), 200, 16));
		RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemDusts.getDustByName("copper", 1), ItemIngots.getIngotByName("nickel", 1), ItemIngots.getIngotByName("cupronickel", 2), 200, 16));
		RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemDusts.getDustByName("copper", 1), ItemDusts.getDustByName("nickel", 1), ItemIngots.getIngotByName("cupronickel", 2), 200, 16));

		//Nichrome
		RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemIngots.getIngotByName("chrome", 1), ItemIngots.getIngotByName("nickel", 4), ItemIngots.getIngotByName("nichrome", 5), 200, 16));
		RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemIngots.getIngotByName("chrome", 1), ItemDusts.getDustByName("nickel", 4), ItemIngots.getIngotByName("nichrome", 5), 200, 16));
		RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemDusts.getDustByName("chrome", 1), ItemIngots.getIngotByName("nickel", 4), ItemIngots.getIngotByName("nichrome", 5), 200, 16));
		RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemDusts.getDustByName("chrome", 1), ItemDusts.getDustByName("nickel", 4), ItemIngots.getIngotByName("nichrome", 5), 200, 16));

		//Magnalium
		RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemDusts.getDustByName("magnesium", 1), ItemIngots.getIngotByName("aluminum", 4), ItemIngots.getIngotByName("magnalium", 3), 200, 16));
		RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemDusts.getDustByName("magnesium", 1), ItemDusts.getDustByName("aluminum", 4), ItemIngots.getIngotByName("magnalium", 3), 200, 16));

		//Battery Alloy
		RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemIngots.getIngotByName("lead", 4), ItemIngots.getIngotByName("antimony", 1), ItemIngots.getIngotByName("batteryAlloy", 5), 200, 16));
		RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemIngots.getIngotByName("lead", 4), ItemDusts.getDustByName("antimony", 1), ItemIngots.getIngotByName("batteryAlloy", 5), 200, 16));
		RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemDusts.getDustByName("lead", 4), ItemIngots.getIngotByName("antimony", 1), ItemIngots.getIngotByName("batteryAlloy", 5), 200, 16));
		RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemDusts.getDustByName("lead", 4), ItemDusts.getDustByName("antimony", 1), ItemIngots.getIngotByName("batteryAlloy", 5), 200, 16));

		//Brass
		if (OreDictionary.doesOreNameExist("ingotBrass")) {
			ItemStack brassStack = OreDictionary.getOres("ingotBrass").get(0);
			brassStack.stackSize = 4;
			RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemIngots.getIngotByName("copper", 3), ItemIngots.getIngotByName("zinc", 1), brassStack, 200, 16));
			RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemIngots.getIngotByName("copper", 3), ItemDusts.getDustByName("zinc", 1), brassStack, 200, 16));
			RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemDusts.getDustByName("copper", 3), ItemIngots.getIngotByName("zinc", 1), brassStack, 200, 16));
			RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemDusts.getDustByName("copper", 3), ItemDusts.getDustByName("zinc", 1), brassStack, 200, 16));
		}

		//Red Alloy
		if (OreDictionary.doesOreNameExist("ingotRedAlloy")) {
			ItemStack redAlloyStack = OreDictionary.getOres("ingotRedAlloy").get(0);
			redAlloyStack.stackSize = 1;
			RecipeHandler.addRecipe(new AlloySmelterRecipe(new ItemStack(Items.redstone, 4), ItemIngots.getIngotByName("copper", 1), redAlloyStack, 200, 16));
			RecipeHandler.addRecipe(new AlloySmelterRecipe(new ItemStack(Items.redstone, 4), new ItemStack(Items.iron_ingot, 1), redAlloyStack, 200, 16));
		}

		//Blue Alloy
		if (OreDictionary.doesOreNameExist("ingotBlueAlloy")) {
			ItemStack blueAlloyStack = OreDictionary.getOres("ingotBlueAlloy").get(0);
			blueAlloyStack.stackSize = 1;
			RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemDusts.getDustByName("teslatite", 4), ItemIngots.getIngotByName("silver", 1), blueAlloyStack, 200, 16));
		}

		//Blue Alloy
		if (OreDictionary.doesOreNameExist("ingotPurpleAlloy") && OreDictionary.doesOreNameExist("dustInfusedTeslatite")) {
			ItemStack purpleAlloyStack = OreDictionary.getOres("ingotPurpleAlloy").get(0);
			purpleAlloyStack.stackSize = 1;
			ItemStack infusedTeslatiteStack = OreDictionary.getOres("ingotPurpleAlloy").get(0);
			infusedTeslatiteStack.stackSize = 8;
			RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemIngots.getIngotByName("redAlloy", 1), ItemIngots.getIngotByName("blueAlloy", 1), purpleAlloyStack, 200, 16));
			RecipeHandler.addRecipe(new AlloySmelterRecipe(new ItemStack(Items.gold_ingot, 1), infusedTeslatiteStack, purpleAlloyStack, 200, 16));
		}

		//Aluminum Brass
		if (OreDictionary.doesOreNameExist("ingotAluminumBrass")) {
			ItemStack aluminumBrassStack = OreDictionary.getOres("ingotAluminumBrass").get(0);
			aluminumBrassStack.stackSize = 4;
			RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemIngots.getIngotByName("copper", 3), ItemIngots.getIngotByName("aluminum", 1), aluminumBrassStack, 200, 16));
			RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemIngots.getIngotByName("copper", 3), ItemDusts.getDustByName("aluminum", 1), aluminumBrassStack, 200, 16));
			RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemDusts.getDustByName("copper", 3), ItemIngots.getIngotByName("aluminum", 1), aluminumBrassStack, 200, 16));
			RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemDusts.getDustByName("copper", 3), ItemDusts.getDustByName("aluminum", 1), aluminumBrassStack, 200, 16));
		}

		//Manyullyn
		if (OreDictionary.doesOreNameExist("ingotManyullyn") && OreDictionary.doesOreNameExist("ingotCobalt") && OreDictionary.doesOreNameExist("ingotArdite")) {
			ItemStack manyullynStack = OreDictionary.getOres("ingotManyullyn").get(0);
			manyullynStack.stackSize = 1;
			ItemStack cobaltStack = OreDictionary.getOres("ingotCobalt").get(0);
			cobaltStack.stackSize = 1;
			ItemStack arditeStack = OreDictionary.getOres("ingotArdite").get(0);
			arditeStack.stackSize = 1;
			RecipeHandler.addRecipe(new AlloySmelterRecipe(cobaltStack, arditeStack, manyullynStack, 200, 16));
			RecipeHandler.addRecipe(new AlloySmelterRecipe(cobaltStack, ItemDusts.getDustByName("ardite", 1), manyullynStack, 200, 16));
			RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemDusts.getDustByName("cobalt", 1), arditeStack, manyullynStack, 200, 16));
			RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemDusts.getDustByName("cobalt", 1), ItemDusts.getDustByName("ardite", 1), manyullynStack, 200, 16));
		}

		//Conductive Iron
		if (OreDictionary.doesOreNameExist("ingotConductiveIron")) {
			ItemStack conductiveIronStack = OreDictionary.getOres("ingotConductiveIron").get(0);
			conductiveIronStack.stackSize = 1;
			RecipeHandler.addRecipe(new AlloySmelterRecipe(new ItemStack(Items.redstone, 1), new ItemStack(Items.iron_ingot, 1), conductiveIronStack, 200, 16));
		}

		//Redstone Alloy
		if (OreDictionary.doesOreNameExist("ingotRedstoneAlloy") && OreDictionary.doesOreNameExist("itemSilicon")) {
			ItemStack redstoneAlloyStack = OreDictionary.getOres("ingotRedstoneAlloy").get(0);
			redstoneAlloyStack.stackSize = 1;
			ItemStack siliconStack = OreDictionary.getOres("itemSilicon").get(0);
			siliconStack.stackSize = 1;
			RecipeHandler.addRecipe(new AlloySmelterRecipe(new ItemStack(Items.redstone, 1), siliconStack, redstoneAlloyStack, 200, 16));
		}

		//Pulsating Iron
		if (OreDictionary.doesOreNameExist("ingotPhasedIron")) {
			ItemStack pulsatingIronStack = OreDictionary.getOres("ingotPhasedIron").get(0);
			pulsatingIronStack.stackSize = 1;
			RecipeHandler.addRecipe(new AlloySmelterRecipe(new ItemStack(Items.iron_ingot, 1), new ItemStack(Items.ender_pearl, 1), pulsatingIronStack, 200, 16));
			RecipeHandler.addRecipe(new AlloySmelterRecipe(new ItemStack(Items.iron_ingot, 1), ItemDusts.getDustByName("enderPearl", 1), pulsatingIronStack, 200, 16));
		}

		//Vibrant Alloy
		if (OreDictionary.doesOreNameExist("ingotEnergeticAlloy") && OreDictionary.doesOreNameExist("ingotPhasedGold")) {
			ItemStack energeticAlloyStack = OreDictionary.getOres("ingotEnergeticAlloy").get(0);
			energeticAlloyStack.stackSize = 1;
			ItemStack vibrantAlloyStack = OreDictionary.getOres("ingotPhasedGold").get(0);
			vibrantAlloyStack.stackSize = 1;
			RecipeHandler.addRecipe(new AlloySmelterRecipe(energeticAlloyStack, new ItemStack(Items.ender_pearl, 1), vibrantAlloyStack, 200, 16));
			RecipeHandler.addRecipe(new AlloySmelterRecipe(energeticAlloyStack, ItemDusts.getDustByName("enderPearl", 1), vibrantAlloyStack, 200, 16));
		}

		//Soularium
		if (OreDictionary.doesOreNameExist("ingotSoularium")) {
			ItemStack soulariumStack = OreDictionary.getOres("ingotSoularium").get(0);
			soulariumStack.stackSize = 1;
			RecipeHandler.addRecipe(new AlloySmelterRecipe(new ItemStack(Blocks.soul_sand, 1), new ItemStack(Items.gold_ingot, 1), soulariumStack, 200, 16));
		}

	}

	static void addLatheRecipes() {
		//Metal Rods
		RecipeHandler.addRecipe(new LatheRecipe(ItemIngots.getIngotByName("brass", 1), ItemRods.getRodByName("brass", 1), 300, 16));
		RecipeHandler.addRecipe(new LatheRecipe(ItemIngots.getIngotByName("bronze", 1), ItemRods.getRodByName("bronze", 1), 380, 16));
		RecipeHandler.addRecipe(new LatheRecipe(ItemIngots.getIngotByName("copper", 1), ItemRods.getRodByName("copper", 1), 300, 16));
		RecipeHandler.addRecipe(new LatheRecipe(ItemIngots.getIngotByName("electrum", 1), ItemRods.getRodByName("electrum", 1), 740, 16));
		RecipeHandler.addRecipe(new LatheRecipe(new ItemStack(Items.gold_ingot), ItemRods.getRodByName("gold", 1), 980, 16));
		RecipeHandler.addRecipe(new LatheRecipe(ItemIngots.getIngotByName("invar", 1), ItemRods.getRodByName("invar", 1), 280, 16));
		RecipeHandler.addRecipe(new LatheRecipe(ItemIngots.getIngotByName("iridium", 1), ItemRods.getRodByName("iridium", 1), 960, 16));
		RecipeHandler.addRecipe(new LatheRecipe(new ItemStack(Items.iron_ingot), ItemRods.getRodByName("iron", 1), 280, 16));
		RecipeHandler.addRecipe(new LatheRecipe(ItemIngots.getIngotByName("lead", 1), ItemRods.getRodByName("lead", 1), 1020, 16));
		RecipeHandler.addRecipe(new LatheRecipe(ItemIngots.getIngotByName("nickel", 1), ItemRods.getRodByName("nickel", 1), 280, 16));
		RecipeHandler.addRecipe(new LatheRecipe(ItemIngots.getIngotByName("platinum", 1), ItemRods.getRodByName("platinum", 1), 960, 16));
		RecipeHandler.addRecipe(new LatheRecipe(ItemIngots.getIngotByName("silver", 1), ItemRods.getRodByName("silver", 1), 520, 16));
		RecipeHandler.addRecipe(new LatheRecipe(ItemIngots.getIngotByName("steel", 1), ItemRods.getRodByName("steel", 1), 280, 16));
		RecipeHandler.addRecipe(new LatheRecipe(ItemIngots.getIngotByName("tin", 1), ItemRods.getRodByName("tin", 1), 580, 16));
		RecipeHandler.addRecipe(new LatheRecipe(ItemIngots.getIngotByName("titanium", 1), ItemRods.getRodByName("titanium", 1), 240, 16));
		RecipeHandler.addRecipe(new LatheRecipe(ItemIngots.getIngotByName("tungstensteel", 1), ItemRods.getRodByName("tungstensteel", 1), 580, 16));

		//Laser Focus
		RecipeHandler.addRecipe(new LatheRecipe(ItemPlates.getPlateByName("ruby", 1), ItemParts.getPartByName("laserFocus", 1), 10, 16));
		RecipeHandler.addRecipe(new LatheRecipe(ItemPlates.getPlateByName("redGarnet", 1), ItemParts.getPartByName("laserFocus", 1), 10, 16));
	}

	static void addPlateCuttingMachineRecipes() {
		//Storage Blocks
		if (OreDictionary.doesOreNameExist("blockAluminum")) {
			ItemStack blockStack = OreDictionary.getOres("blockAluminum").get(0);
			RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("aluminum", 9), 200, 16));
		}
		if (OreDictionary.doesOreNameExist("blockBrass")) {
			ItemStack blockStack = OreDictionary.getOres("blockBrass").get(0);
			RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("brass", 9), 200, 116));
		}
		if (OreDictionary.doesOreNameExist("blockBronze")) {
			ItemStack blockStack = OreDictionary.getOres("blockBronze").get(0);
			RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("bronze", 9), 200, 16));
		}
		if (OreDictionary.doesOreNameExist("blockCoal")) {
			ItemStack blockStack = OreDictionary.getOres("blockCoal").get(0);
			RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("carbon", 9), 200, 16));
		}
		if (OreDictionary.doesOreNameExist("blockChrome")) {
			ItemStack blockStack = OreDictionary.getOres("blockChrome").get(0);
			RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("chrome", 9), 200, 16));
		}
		if (OreDictionary.doesOreNameExist("blockCopper")) {
			ItemStack blockStack = OreDictionary.getOres("blockCopper").get(0);
			RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("copper", 9), 200, 16));
		}
		if (OreDictionary.doesOreNameExist("blockDiamond")) {
			ItemStack blockStack = OreDictionary.getOres("blockDiamond").get(0);
			RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("diamond", 9), 200, 16));
		}
		if (OreDictionary.doesOreNameExist("blockElectrum")) {
			ItemStack blockStack = OreDictionary.getOres("blockElectrum").get(0);
			RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("electrum", 9), 200, 16));
		}
		if (OreDictionary.doesOreNameExist("blockEmerald")) {
			ItemStack blockStack = OreDictionary.getOres("blockEmerald").get(0);
			RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("emerald", 9), 200, 16));
		}
		if (OreDictionary.doesOreNameExist("blockGold")) {
			ItemStack blockStack = OreDictionary.getOres("blockGold").get(0);
			RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("gold", 9), 200, 16));
		}
		if (OreDictionary.doesOreNameExist("blockInvar")) {
			ItemStack blockStack = OreDictionary.getOres("blockInvar").get(0);
			RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("invar", 9), 200, 16));
		}
		if (OreDictionary.doesOreNameExist("blockIridium")) {
			ItemStack blockStack = OreDictionary.getOres("blockIridium").get(0);
			RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("iridium", 9), 200, 16));
		}
		if (OreDictionary.doesOreNameExist("blockIron")) {
			ItemStack blockStack = OreDictionary.getOres("blockIron").get(0);
			RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("iron", 9), 200, 16));
		}
		if (OreDictionary.doesOreNameExist("blockLapis")) {
			ItemStack blockStack = OreDictionary.getOres("blockLapis").get(0);
			RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("lapis", 9), 200, 16));
		}
		if (OreDictionary.doesOreNameExist("blockLead")) {
			ItemStack blockStack = OreDictionary.getOres("blockLead").get(0);
			RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("lead", 9), 200, 16));
		}
		if (OreDictionary.doesOreNameExist("blockNickel")) {
			ItemStack blockStack = OreDictionary.getOres("blockNickel").get(0);
			RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("nickel", 9), 200, 16));
		}
		if (OreDictionary.doesOreNameExist("blockOsmium")) {
			ItemStack blockStack = OreDictionary.getOres("blockOsmium").get(0);
			RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("osmium", 9), 200, 16));
		}
		if (OreDictionary.doesOreNameExist("blockPeridot")) {
			ItemStack blockStack = OreDictionary.getOres("blockPeridot").get(0);
			RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("peridot", 9), 200, 16));
		}
		if (OreDictionary.doesOreNameExist("blockPlatinum")) {
			ItemStack blockStack = OreDictionary.getOres("blockPlatinum").get(0);
			RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("platinum", 9), 200, 16));
		}
		if (OreDictionary.doesOreNameExist("blockRedGarnet")) {
			ItemStack blockStack = OreDictionary.getOres("blockRedGarnet").get(0);
			RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("redGarnet", 9), 200, 16));
		}
		if (OreDictionary.doesOreNameExist("BlockRedstone")) {
			ItemStack blockStack = OreDictionary.getOres("blockRedstone").get(0);
			RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("redstone", 9), 200, 16));
		}
		if (OreDictionary.doesOreNameExist("blockRuby")) {
			ItemStack blockStack = OreDictionary.getOres("blockRuby").get(0);
			RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("ruby", 9), 200, 16));
		}
		if (OreDictionary.doesOreNameExist("blockSapphire")) {
			ItemStack blockStack = OreDictionary.getOres("blockSapphire").get(0);
			RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("sapphire", 9), 200, 16));
		}
		if (OreDictionary.doesOreNameExist("blockSilver")) {
			ItemStack blockStack = OreDictionary.getOres("blockSilver").get(0);
			RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("silver", 9), 200, 16));
		}
		if (OreDictionary.doesOreNameExist("blockSteel")) {
			ItemStack blockStack = OreDictionary.getOres("blockSteel").get(0);
			RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("steel", 9), 200, 16));
		}
		if (OreDictionary.doesOreNameExist("blockTeslatite")) {
			ItemStack blockStack = OreDictionary.getOres("blockTeslatite").get(0);
			RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("teslatite", 9), 200, 16));
		}
		if (OreDictionary.doesOreNameExist("blockTin")) {
			ItemStack blockStack = OreDictionary.getOres("blockTin").get(0);
			RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("tin", 9), 200, 16));
		}
		if (OreDictionary.doesOreNameExist("blockTitanium")) {
			ItemStack blockStack = OreDictionary.getOres("blockTitanium").get(0);
			RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("titanium", 9), 200, 16));
		}
		if (OreDictionary.doesOreNameExist("blockTungsten")) {
			ItemStack blockStack = OreDictionary.getOres("blockTungsten").get(0);
			RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("tungsten", 9), 200, 16));
		}
		if (OreDictionary.doesOreNameExist("blockTungstensteel")) {
			ItemStack blockStack = OreDictionary.getOres("blockTungstensteel").get(0);
			RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("tungstensteel", 9), 200, 16));
		}
		if (OreDictionary.doesOreNameExist("blockYellowGarnet")) {
			ItemStack blockStack = OreDictionary.getOres("blockYellowGarnet").get(0);
			RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("yellowGarnet", 9), 200, 16));
		}
		if (OreDictionary.doesOreNameExist("blockZinc")) {
			ItemStack blockStack = OreDictionary.getOres("blockZinc").get(0);
			RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("zinc", 9), 200, 16));
		}

		//Obsidian
		RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(new ItemStack(Blocks.obsidian), ItemPlates.getPlateByName("obsidian", 9), 100, 4));
	}

	static void addIndustrialSawmillRecipes() {
		ItemStack pulpStack = OreDictionary.getOres("pulpWood").get(0);
		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(Blocks.log, 1, 0), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(Blocks.planks, 6, 0), pulpStack, null, 200, 30, false));
		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(Blocks.log, 1, 0), new ItemStack(Items.water_bucket), null, new ItemStack(Blocks.planks, 6, 0), pulpStack, new ItemStack(Items.bucket), 200, 30, false));

		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(Blocks.log, 1, 1), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(Blocks.planks, 6, 1), pulpStack, null, 200, 30, false));
		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(Blocks.log, 1, 1), new ItemStack(Items.water_bucket), null, new ItemStack(Blocks.planks, 6, 1), pulpStack, new ItemStack(Items.bucket), 200, 30, false));

		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(Blocks.log, 1, 2), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(Blocks.planks, 6, 2), pulpStack, null, 200, 30, false));
		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(Blocks.log, 1, 2), new ItemStack(Items.water_bucket), null, new ItemStack(Blocks.planks, 6, 2), pulpStack, new ItemStack(Items.bucket), 200, 30, false));

		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(Blocks.log, 1, 3), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(Blocks.planks, 6, 3), pulpStack, null, 200, 30, false));
		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(Blocks.log, 1, 3), new ItemStack(Items.water_bucket), null, new ItemStack(Blocks.planks, 6, 3), pulpStack, new ItemStack(Items.bucket), 200, 30, false));

		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(Blocks.log2, 1, 0), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(Blocks.planks, 6, 4), pulpStack, null, 200, 30, false));
		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(Blocks.log2, 1, 0), new ItemStack(Items.water_bucket), null, new ItemStack(Blocks.planks, 6, 4), pulpStack, new ItemStack(Items.bucket), 200, 30, false));

		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(Blocks.log2, 1, 1), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(Blocks.planks, 6, 5), pulpStack, null, 200, 30, false));
		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(Blocks.log2, 1, 1), new ItemStack(Items.water_bucket), null, new ItemStack(Blocks.planks, 6, 5), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	}

	static void addBlastFurnaceRecipes() {
		RecipeHandler.addRecipe(new BlastFurnaceRecipe(ItemDusts.getDustByName("titanium"), null, ItemIngots.getIngotByName("titanium"), null, 3600, 120, 1500));
		RecipeHandler.addRecipe(new BlastFurnaceRecipe(ItemDustsSmall.getSmallDustByName("titanium", 4), null, ItemIngots.getIngotByName("titanium"), null, 3600, 120, 1500));
		RecipeHandler.addRecipe(new BlastFurnaceRecipe(ItemDustsTiny.getTinyDustByName("titanium", 9), null, ItemIngots.getIngotByName("titanium"), null, 3600, 120, 1500));
		RecipeHandler.addRecipe(new BlastFurnaceRecipe(ItemDusts.getDustByName("aluminum"), null, ItemIngots.getIngotByName("aluminum"), null, 2200, 120, 1700));
		RecipeHandler.addRecipe(new BlastFurnaceRecipe(ItemDustsSmall.getSmallDustByName("aluminum", 4), null, ItemIngots.getIngotByName("aluminum"), null, 2200, 120, 1700));
		RecipeHandler.addRecipe(new BlastFurnaceRecipe(ItemDustsTiny.getTinyDustByName("aluminum", 9), null, ItemIngots.getIngotByName("aluminum"), null, 2200, 120, 1700));
		RecipeHandler.addRecipe(new BlastFurnaceRecipe(ItemDusts.getDustByName("kanthal"), null, ItemIngots.getIngotByName("kanthal"), null, 5500, 120, 2500));
		RecipeHandler.addRecipe(new BlastFurnaceRecipe(ItemDustsSmall.getSmallDustByName("kanthal", 4), null, ItemIngots.getIngotByName("kanthal"), null, 5500, 120, 2500));
		RecipeHandler.addRecipe(new BlastFurnaceRecipe(ItemDustsTiny.getTinyDustByName("kanthal", 9), null, ItemIngots.getIngotByName("kanthal"), null, 5500, 120, 2500));
		RecipeHandler.addRecipe(new BlastFurnaceRecipe(ItemDusts.getDustByName("tungsten"), null, ItemIngots.getIngotByName("tungsten"), null, 18000, 120, 2500));
		RecipeHandler.addRecipe(new BlastFurnaceRecipe(ItemDustsSmall.getSmallDustByName("tungsten", 4), null, ItemIngots.getIngotByName("tungsten"), null, 18000, 120, 2500));
		RecipeHandler.addRecipe(new BlastFurnaceRecipe(ItemDustsTiny.getTinyDustByName("tungsten", 9), null, ItemIngots.getIngotByName("tungsten"), null, 18000, 120, 2500));
		RecipeHandler.addRecipe(new BlastFurnaceRecipe(ItemDusts.getDustByName("chrome"), null, ItemIngots.getIngotByName("chrome"), null, 4420, 120, 1700));
		RecipeHandler.addRecipe(new BlastFurnaceRecipe(ItemDustsSmall.getSmallDustByName("chrome", 4), null, ItemIngots.getIngotByName("chrome"), null, 4420, 120, 1700));
		RecipeHandler.addRecipe(new BlastFurnaceRecipe(ItemDustsTiny.getTinyDustByName("chrome", 9), null, ItemIngots.getIngotByName("chrome"), null, 4420, 120, 1700));
		RecipeHandler.addRecipe(new BlastFurnaceRecipe(ItemDusts.getDustByName("steel"), null, ItemIngots.getIngotByName("steel"), null, 2800, 120, 1000));
		RecipeHandler.addRecipe(new BlastFurnaceRecipe(ItemDustsSmall.getSmallDustByName("steel", 4), null, ItemIngots.getIngotByName("steel"), null, 2800, 120, 1000));
		RecipeHandler.addRecipe(new BlastFurnaceRecipe(ItemDustsTiny.getTinyDustByName("steel", 9), null, ItemIngots.getIngotByName("steel"), null, 2800, 120, 1000));

		RecipeHandler.addRecipe(new BlastFurnaceRecipe(ItemDusts.getDustByName("galena", 2), null, ItemIngots.getIngotByName("silver"), ItemIngots.getIngotByName("lead"), 80, 120, 1500));

		RecipeHandler.addRecipe(new BlastFurnaceRecipe(new ItemStack(Items.iron_ingot), ItemDusts.getDustByName("coal", 2), ItemIngots.getIngotByName("steel"), ItemDusts.getDustByName("darkAshes", 2), 500, 120, 1000));

		RecipeHandler.addRecipe(new BlastFurnaceRecipe(ItemIngots.getIngotByName("tungsten"), ItemIngots.getIngotByName("steel"), ItemIngots.getIngotByName("hotTungstensteel"), ItemDusts.getDustByName("darkAshes", 4), 500, 500, 3000));

		RecipeHandler.addRecipe(new BlastFurnaceRecipe(new ItemStack(Blocks.iron_ore), ItemDusts.getDustByName("calcite"), new ItemStack(Items.iron_ingot, 3), ItemDusts.getDustByName("darkAshes"), 140, 120, 1000));

		RecipeHandler.addRecipe(new BlastFurnaceRecipe(BlockOre.getOreByName("Pyrite"), ItemDusts.getDustByName("calcite"), new ItemStack(Items.iron_ingot, 2), ItemDusts.getDustByName("darkAshes"), 140, 120, 1000));
	}

	static void addUUrecipes() {


		if (ConfigTechReborn.UUrecipesWood)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Blocks.log, 8),
					" U ",
					"   ",
					"   ",
					'U', ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesStone)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Blocks.stone, 16),
					"   ",
					" U ",
					"   ",
					'U', ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesSnowBlock)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Blocks.snow, 16),
					"U U",
					"   ",
					"   ",
					'U', ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesGrass)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Blocks.grass, 16),
					"   ",
					"U  ",
					"U  ",
					'U', ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesObsidian)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Blocks.obsidian, 12),
					"U U",
					"U U",
					"   ",
					'U', ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesGlass)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Blocks.glass, 32),
					" U ",
					"U U",
					" U ",
					'U', ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesWater)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Blocks.water, 1),
					"   ",
					" U ",
					" U ",
					'U', ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesLava)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Blocks.lava, 1),
					" U ",
					" U ",
					" U ",
					'U', ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesCocoa)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Items.dye, 32, 3),
					"UU ",
					"  U",
					"UU ",
					'U', ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesGlowstoneBlock)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Blocks.glowstone, 8),
					" U ",
					"U U",
					"UUU",
					'U', ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesCactus)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Blocks.cactus, 48),
					" U ",
					"UUU",
					"U U",
					'U', ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesSugarCane)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Items.reeds, 48),
					"U U",
					"U U",
					"U U",
					'U', ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesVine)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Blocks.vine, 24),
					"U  ",
					"U  ",
					"U  ",
					'U', ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesSnowBall)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Items.snowball, 16),
					"   ",
					"   ",
					"UUU",
					'U', ModItems.uuMatter);

		CraftingHelper.addShapedOreRecipe(new ItemStack(Items.clay_ball, 48),
				"UU ",
				"U  ",
				"UU ",
				'U', ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipeslilypad)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Blocks.waterlily, 64),
					"U U",
					" U ",
					" U ",
					'U', ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesGunpowder)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Items.gunpowder, 15),
					"UUU",
					"U  ",
					"UUU",
					'U', ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesBone)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Items.bone, 32),
					"U  ",
					"UU ",
					"U  ",
					'U', ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesFeather)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Items.feather, 32),
					" U ",
					" U ",
					"U U",
					'U', ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesInk)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Items.dye, 48),
					" UU",
					" UU",
					" U ",
					'U', ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesEnderPearl)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Items.ender_pearl, 1),
					"UUU",
					"U U",
					" U ",
					'U', ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesCoal)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Items.coal, 5),
					"  U",
					"U  ",
					"  U",
					'U', ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesIronOre)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Blocks.iron_ore, 2),
					"U U",
					" U ",
					"U U",
					'U', ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesGoldOre)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Blocks.gold_ore, 2),
					" U ",
					"UUU",
					" U ",
					'U', ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesRedStone)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Items.redstone, 24),
					"   ",
					" U ",
					"UUU",
					'U', ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesLapis)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Items.dye, 9, 4),
					" U ",
					" U ",
					" UU",
					'U', ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesEmeraldOre)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Blocks.emerald_ore, 1),
					"UU ",
					"U U",
					" UU",
					'U', ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesEmerald)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Items.emerald, 2),
					"UUU",
					"UUU",
					" U ",
					'U', ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesDiamond)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Items.diamond, 1),
					"UUU",
					"UUU",
					"UUU",
					'U', ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesTinDust)
			CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.dusts, 10, 77),
					"   ",
					"U U",
					"  U",
					'U', ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesCopperDust)
			CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.dusts, 10, 21),
					"  U",
					"U U",
					"   ",
					'U', ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesLeadDust)
			CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.dusts, 14, 42),
					"UUU",
					"UUU",
					"U  ",
					'U', ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesPlatinumDust)
			CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.dusts, 1, 58),
					"  U",
					"UUU",
					"UUU",
					'U', ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesTungstenDust)
			CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.dusts, 1, 79),
					"U  ",
					"UUU",
					"UUU",
					'U', ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesTitaniumDust)
			CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.dusts, 2, 78),
					"UUU",
					" U ",
					" U ",
					'U', ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesAluminumDust)
			CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.dusts, 16, 2),
					" U ",
					" U ",
					"UUU",
					'U', ModItems.uuMatter);


		if (ConfigTechReborn.HideUuRecipes)
			hideUUrecipes();

	}

	static void hideUUrecipes() {
		//TODO
	}

	static void addAssemblingMachineRecipes() {
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

		//Data Control Circuit
		RecipeHandler.addRecipe(new AssemblingMachineRecipe(ItemParts.getPartByName("processorCircuitBoard", 1), ItemParts.getPartByName("dataStorageCircuit", 1), ItemParts.getPartByName("dataControlCircuit", 1), 120, 5));

		//Data Orb
		RecipeHandler.addRecipe(new AssemblingMachineRecipe(ItemParts.getPartByName("dataControlCircuit", 1), ItemParts.getPartByName("dataStorageCircuit", 8), ItemParts.getPartByName("dataOrb"), 120, 5));

		//Basic Circuit Board
		RecipeHandler.addRecipe(new AssemblingMachineRecipe(ItemPlates.getPlateByName("aluminum", 1), ItemPlates.getPlateByName("electrum", 2), ItemParts.getPartByName("basicCircuitBoard", 2), 120, 5));
		RecipeHandler.addRecipe(new AssemblingMachineRecipe(ItemPlates.getPlateByName("iron", 1), ItemPlates.getPlateByName("electrum", 2), ItemParts.getPartByName("basicCircuitBoard", 2), 120, 5));


		for (String type : BlockMachineFrame.types) {
			RecipeHandler.addRecipe(new AssemblingMachineRecipe(ItemParts.getPartByName("machineParts", 1), ItemPlates.getPlateByName(type, 8), BlockMachineFrame.getFrameByName(type, 1), 400, 8));
		}

	}

	static void addIndustrialCentrifugeRecipes() {

		//Mycelium Byproducts
		RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Blocks.mycelium, 8), null, new ItemStack(Blocks.brown_mushroom, 2), new ItemStack(Blocks.red_mushroom, 2), new ItemStack(Items.clay_ball, 1), new ItemStack(Blocks.sand, 4), 1640, 5));

		//Blaze Powder Byproducts
		RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.blaze_powder), null, ItemDusts.getDustByName("darkAshes", 1), ItemDusts.getDustByName("sulfur", 1), null, null, 1240, 5));

		//Magma Cream Products
		RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.magma_cream, 1), null, new ItemStack(Items.blaze_powder, 1), new ItemStack(Items.slime_ball, 1), null, null, 2500, 5));

		//Dust Byproducts
		RecipeHandler.addRecipe(new CentrifugeRecipe(ItemDusts.getDustByName("platinum", 1), null, ItemDustsTiny.getTinyDustByName("Iridium", 1), ItemDustsSmall.getSmallDustByName("Nickel", 1), null, null, 3000, 5));
		RecipeHandler.addRecipe(new CentrifugeRecipe(ItemDusts.getDustByName("electrum", 2), null, ItemDusts.getDustByName("silver", 1), ItemDusts.getDustByName("gold", 1), null, null, 2400, 5));
		RecipeHandler.addRecipe(new CentrifugeRecipe(ItemDusts.getDustByName("invar", 3), null, ItemDusts.getDustByName("iron", 2), ItemDusts.getDustByName("nickel", 1), null, null, 1340, 5));
		RecipeHandler.addRecipe(new CentrifugeRecipe(ItemDusts.getDustByName("marble", 8), null, ItemDusts.getDustByName("magnesium", 1), ItemDusts.getDustByName("calcite", 7), null, null, 1280, 5));
		RecipeHandler.addRecipe(new CentrifugeRecipe(ItemDusts.getDustByName("redrock", 4), null, ItemDusts.getDustByName("calcite", 2), ItemDusts.getDustByName("flint", 1), ItemDusts.getDustByName("clay", 1), null, 640, 5));
		RecipeHandler.addRecipe(new CentrifugeRecipe(ItemDusts.getDustByName("basalt", 16), null, ItemDusts.getDustByName("peridot", 1), ItemDusts.getDustByName("calcite", 3), ItemDusts.getDustByName("magnesium", 8), ItemDusts.getDustByName("darkAshes", 4), 2680, 5));
		RecipeHandler.addRecipe(new CentrifugeRecipe(ItemDusts.getDustByName("yellowGarnet", 16), null, ItemDusts.getDustByName("andradite", 5), ItemDusts.getDustByName("grossular", 8), ItemDusts.getDustByName("uvarovite", 3), null, 2940, 5));
		RecipeHandler.addRecipe(new CentrifugeRecipe(ItemDusts.getDustByName("redGarnet", 16), null, ItemDusts.getDustByName("pyrope", 3), ItemDusts.getDustByName("almandine", 5), ItemDusts.getDustByName("spessartine", 8), null, 2940, 5));
		RecipeHandler.addRecipe(new CentrifugeRecipe(ItemDusts.getDustByName("darkAshes", 2), null, ItemDusts.getDustByName("ashes", 2), null, null, null, 240, 5));
		RecipeHandler.addRecipe(new CentrifugeRecipe(ItemDusts.getDustByName("manyullyn", 2), null, ItemDusts.getDustByName("cobalt", 1), ItemDusts.getDustByName("ardite", 1), null, null, 1240, 5));
		RecipeHandler.addRecipe(new CentrifugeRecipe(ItemDusts.getDustByName("nichrome", 5), null, ItemDusts.getDustByName("nickel", 4), ItemDusts.getDustByName("chrome", 1), null, null, 2240, 5));
		RecipeHandler.addRecipe(new CentrifugeRecipe(ItemDusts.getDustByName("cupronickel", 2), null, ItemDusts.getDustByName("copper", 1), ItemDusts.getDustByName("nickel", 1), null, null, 960, 5));
		RecipeHandler.addRecipe(new CentrifugeRecipe(ItemDusts.getDustByName("kanthal", 3), null, ItemDusts.getDustByName("iron", 1), ItemDusts.getDustByName("aluminum", 1), ItemDusts.getDustByName("chrome", 1), null, 1040, 5));
		RecipeHandler.addRecipe(new CentrifugeRecipe(ItemDusts.getDustByName("brass", 4), null, ItemDusts.getDustByName("zinc", 1), ItemDusts.getDustByName("copper", 3), null, null, 2000, 5));
		RecipeHandler.addRecipe(new CentrifugeRecipe(ItemDusts.getDustByName("aluminumBrass", 4), null, ItemDusts.getDustByName("aluminum", 1), ItemDusts.getDustByName("copper", 3), null, null, 2000, 5));
		RecipeHandler.addRecipe(new CentrifugeRecipe(ItemDusts.getDustByName("bronze", 4), null, ItemDusts.getDustByName("tin", 1), ItemDusts.getDustByName("copper", 3), null, null, 2420, 5));
		RecipeHandler.addRecipe(new CentrifugeRecipe(ItemDusts.getDustByName("netherrack", 16), null, new ItemStack(Items.redstone, 1), ItemDusts.getDustByName("sulfur", 4), ItemDusts.getDustByName("basalt", 1), new ItemStack(Items.gold_nugget, 1), 2400, 5));
		RecipeHandler.addRecipe(new CentrifugeRecipe(ItemDusts.getDustByName("enderEye", 1), null, ItemDusts.getDustByName("enderPearl", 1), new ItemStack(Items.blaze_powder, 1), null, null, 1280, 5));
		RecipeHandler.addRecipe(new CentrifugeRecipe(ItemDusts.getDustByName("tetrahedrite", 8), null, ItemDusts.getDustByName("copper", 3), ItemDusts.getDustByName("antimony", 1), ItemDusts.getDustByName("sulfur", 3), ItemDusts.getDustByName("iron", 1), 3640, 5));
		RecipeHandler.addRecipe(new CentrifugeRecipe(ItemDusts.getDustByName("lapis", 16), null, ItemDusts.getDustByName("lazurite", 12), ItemDusts.getDustByName("sodalite", 2), ItemDusts.getDustByName("pyrite", 7), ItemDusts.getDustByName("calcite", 1), 3580, 5));

	}

	static void addIndustrialGrinderRecipes() {
		//Coal Ore
		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.coal_ore, 1), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(Items.coal, 1), ItemDustsSmall.getSmallDustByName("Coal", 6), ItemDustsSmall.getSmallDustByName("Coal", 2), null, 100, 120));
		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.coal_ore, 1), new ItemStack(Items.water_bucket), null, new ItemStack(Items.coal, 1), ItemDustsSmall.getSmallDustByName("Coal", 6), ItemDustsSmall.getSmallDustByName("Coal", 2), new ItemStack(Items.bucket), 100, 120));

		//Iron Ore
		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.iron_ore, 1), null, new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("iron", 2), ItemDustsSmall.getSmallDustByName("Nickel", 1), ItemDustsSmall.getSmallDustByName("Tin", 1), null, 100, 120));
		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.iron_ore, 1), new ItemStack(Items.water_bucket), null, ItemDusts.getDustByName("iron", 2), ItemDustsSmall.getSmallDustByName("Nickel", 1), ItemDustsSmall.getSmallDustByName("Tin", 1), new ItemStack(Items.bucket), 100, 120));

		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.iron_ore, 1), null, new FluidStack(ModFluids.fluidSodiumpersulfate, 1000), ItemDusts.getDustByName("iron", 2), ItemDusts.getDustByName("nickel", 1), ItemDustsSmall.getSmallDustByName("Tin", 1), null, 100, 120));
		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.iron_ore, 1), new ItemStack(ModItems.bucketSodiumpersulfate), null, ItemDusts.getDustByName("iron", 2), ItemDusts.getDustByName("nickel", 1), ItemDustsSmall.getSmallDustByName("Tin", 1), new ItemStack(Items.bucket), 100, 120));

		//Gold Ore
		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.gold_ore, 1), null, new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("gold", 2), ItemDustsSmall.getSmallDustByName("Copper", 1), ItemDustsSmall.getSmallDustByName("Nickel", 1), null, 100, 120));
		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.gold_ore, 1), new ItemStack(Items.water_bucket), null, ItemDusts.getDustByName("gold", 2), ItemDustsSmall.getSmallDustByName("Copper", 1), ItemDustsSmall.getSmallDustByName("Nickel", 1), new ItemStack(Items.bucket), 100, 120));

		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.gold_ore, 1), null, new FluidStack(ModFluids.fluidSodiumpersulfate, 1000), ItemDusts.getDustByName("gold", 2), ItemDusts.getDustByName("copper", 1), ItemDustsSmall.getSmallDustByName("Nickel", 1), null, 100, 120));
		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.gold_ore, 1), new ItemStack(ModItems.bucketSodiumpersulfate), null, ItemDusts.getDustByName("gold", 2), ItemDusts.getDustByName("copper", 1), ItemDustsSmall.getSmallDustByName("Nickel", 1), new ItemStack(Items.bucket), 100, 120));

		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.gold_ore, 1), null, new FluidStack(ModFluids.fluidMercury, 1000), ItemDusts.getDustByName("gold", 3), ItemDustsSmall.getSmallDustByName("Copper", 1), ItemDustsSmall.getSmallDustByName("Nickel", 1), null, 100, 120));
		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.gold_ore, 1), new ItemStack(ModItems.bucketMercury), null, ItemDusts.getDustByName("gold", 3), ItemDustsSmall.getSmallDustByName("Copper", 1), ItemDustsSmall.getSmallDustByName("Nickel", 1), new ItemStack(Items.bucket), 100, 120));

		//Diamond Ore
		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.diamond_ore, 1), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(Items.diamond, 1), ItemDustsSmall.getSmallDustByName("Diamond", 6), ItemDustsSmall.getSmallDustByName("Coal", 2), null, 100, 120));
		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.diamond_ore, 1), new ItemStack(Items.water_bucket), null, new ItemStack(Items.diamond, 1), ItemDustsSmall.getSmallDustByName("Diamond", 6), ItemDustsSmall.getSmallDustByName("Coal", 2), new ItemStack(Items.bucket), 100, 120));

		//Emerald Ore
		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.emerald_ore, 1), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(Items.emerald, 1), ItemDustsSmall.getSmallDustByName("Emerald", 6), ItemDustsSmall.getSmallDustByName("Aluminum", 2), null, 100, 120));
		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.emerald_ore, 1), new ItemStack(Items.water_bucket), null, new ItemStack(Items.emerald, 1), ItemDustsSmall.getSmallDustByName("Emerald", 6), ItemDustsSmall.getSmallDustByName("Aluminum", 2), new ItemStack(Items.bucket), 100, 120));

		//Redstone
		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.redstone_ore, 1), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(Items.redstone, 10), ItemDustsSmall.getSmallDustByName("Cinnabar", 1), ItemDustsSmall.getSmallDustByName("Glowstone", 1), null, 100, 120));
		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.redstone_ore, 1), new ItemStack(Items.water_bucket), null, new ItemStack(Items.redstone, 10), ItemDustsSmall.getSmallDustByName("Cinnabar", 1), ItemDustsSmall.getSmallDustByName("Glowstone", 1), new ItemStack(Items.bucket), 100, 120));

		//Lapis Lazuli Ore
		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.lapis_ore, 1), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(Items.dye, 6, 4), ItemDustsSmall.getSmallDustByName("Lapis", 36), ItemDustsSmall.getSmallDustByName("Lazurite", 8), null, 100, 120));
		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.lapis_ore, 1), new ItemStack(Items.water_bucket), null, new ItemStack(Items.dye, 6, 4), ItemDustsSmall.getSmallDustByName("Lapis", 36), ItemDustsSmall.getSmallDustByName("Lazurite", 8), new ItemStack(Items.bucket), 100, 120));

		//Copper Ore
		if (OreDictionary.doesOreNameExist("oreCopper")) {
			try {
				ItemStack oreStack = OreDictionary.getOres("oreCopper").get(0);
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("copper", 2), ItemDustsSmall.getSmallDustByName("Gold", 1), ItemDustsSmall.getSmallDustByName("Nickel", 1), null, 100, 120));
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, ItemDusts.getDustByName("copper", 2), ItemDustsSmall.getSmallDustByName("Gold", 1), ItemDustsSmall.getSmallDustByName("Nickel", 1), new ItemStack(Items.bucket), 100, 120));

				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(ModFluids.fluidSodiumpersulfate, 1000), ItemDusts.getDustByName("copper", 2), ItemDusts.getDustByName("gold", 1), ItemDustsSmall.getSmallDustByName("Nickel", 1), null, 100, 120));
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(ModItems.bucketSodiumpersulfate), null, ItemDusts.getDustByName("copper", 2), ItemDusts.getDustByName("gold", 1), ItemDustsSmall.getSmallDustByName("Nickel", 1), new ItemStack(Items.bucket), 100, 120));

				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(ModFluids.fluidMercury, 1000), ItemDusts.getDustByName("copper", 2), ItemDustsSmall.getSmallDustByName("Gold", 1), ItemDusts.getDustByName("nickel", 1), null, 100, 120));
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, ItemCells.getCellByName("mercury", 1), null, ItemDusts.getDustByName("copper", 2), ItemDustsSmall.getSmallDustByName("Gold", 1), ItemDusts.getDustByName("nickel", 1), new ItemStack(Items.bucket), 100, 120));
			} catch (Exception e) {
				LogHelper.info("Failed to Load Grinder Recipe for Copper Ore");
			}
		}

		//Tin Ore
		if (OreDictionary.doesOreNameExist("oreTin")) {
			try {
				ItemStack oreStack = OreDictionary.getOres("oreTin").get(0);
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("tin", 2), ItemDustsSmall.getSmallDustByName("Iron", 1), ItemDustsSmall.getSmallDustByName("Zinc", 1), null, 100, 120));
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, ItemDusts.getDustByName("tin", 2), ItemDustsSmall.getSmallDustByName("Iron", 1), ItemDustsSmall.getSmallDustByName("Zinc", 1), new ItemStack(Items.bucket), 100, 120));

				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(ModFluids.fluidSodiumpersulfate, 1000), ItemDusts.getDustByName("tin", 2), ItemDustsSmall.getSmallDustByName("Iron", 1), ItemDusts.getDustByName("zinc", 1), null, 100, 120));
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(ModItems.bucketSodiumpersulfate), null, ItemDusts.getDustByName("tin", 2), ItemDustsSmall.getSmallDustByName("Iron", 1), ItemDusts.getDustByName("zinc", 1), new ItemStack(Items.bucket), 100, 120));
			} catch (Exception e) {
				LogHelper.info("Failed to Load Grinder Recipe for Tin Ore");
			}
		}

		//Nickel Ore
		if (OreDictionary.doesOreNameExist("oreNickel")) {
			try {
				ItemStack oreStack = OreDictionary.getOres("oreNickel").get(0);
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("nickel", 2), ItemDustsSmall.getSmallDustByName("Iron", 1), ItemDustsSmall.getSmallDustByName("Platinum", 1), null, 100, 120));
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, ItemDusts.getDustByName("nickel", 2), ItemDustsSmall.getSmallDustByName("Iron", 1), ItemDustsSmall.getSmallDustByName("Platinum", 1), new ItemStack(Items.bucket), 100, 120));

				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(ModFluids.fluidSodiumpersulfate, 1000), ItemDusts.getDustByName("nickel", 3), ItemDustsSmall.getSmallDustByName("Iron", 1), ItemDustsSmall.getSmallDustByName("Platinum", 1), null, 100, 120));
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(ModItems.bucketSodiumpersulfate), null, ItemDusts.getDustByName("nickel", 3), ItemDustsSmall.getSmallDustByName("Iron", 1), ItemDustsSmall.getSmallDustByName("Platinum", 1), new ItemStack(Items.bucket), 100, 120));

				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(ModFluids.fluidMercury, 1000), ItemDusts.getDustByName("nickel", 2), ItemDustsSmall.getSmallDustByName("Iron", 1), ItemDusts.getDustByName("platinum", 1), null, 100, 120));
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(ModItems.bucketMercury), null, ItemDusts.getDustByName("nickel", 2), ItemDustsSmall.getSmallDustByName("Iron", 1), ItemDusts.getDustByName("platinum", 1), new ItemStack(Items.bucket), 100, 120));
			} catch (Exception e) {
				LogHelper.info("Failed to Load Grinder Recipe for Nickel Ore");
			}
		}

		//Zinc Ore
		if (OreDictionary.doesOreNameExist("oreZinc")) {
			try {
				ItemStack oreStack = OreDictionary.getOres("oreZinc").get(0);
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("zinc", 2), ItemDustsSmall.getSmallDustByName("Iron", 1), ItemDustsSmall.getSmallDustByName("Tin", 1), null, 100, 120));
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, ItemDusts.getDustByName("zinc", 2), ItemDustsSmall.getSmallDustByName("Iron", 1), ItemDustsSmall.getSmallDustByName("Tin", 1), new ItemStack(Items.bucket), 100, 120));

				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(ModFluids.fluidSodiumpersulfate, 1000), ItemDusts.getDustByName("zinc", 2), ItemDustsSmall.getSmallDustByName("Iron", 1), ItemDusts.getDustByName("iron", 1), null, 100, 120));
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(ModItems.bucketSodiumpersulfate), null, ItemDusts.getDustByName("zinc", 2), ItemDustsSmall.getSmallDustByName("Iron", 1), ItemDusts.getDustByName("iron", 1), new ItemStack(Items.bucket), 100, 120));
			} catch (Exception e) {
				LogHelper.info("Failed to Load Grinder Recipe for Zinc Ore");
			}
		}

		//Silver Ore
		if (OreDictionary.doesOreNameExist("oreSilver")) {
			try {
				ItemStack oreStack = OreDictionary.getOres("oreSilver").get(0);
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("silver", 2), ItemDustsSmall.getSmallDustByName("Lead", 1), ItemDustsSmall.getSmallDustByName("Sulfur", 1), null, 100, 120));
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, ItemDusts.getDustByName("silver", 2), ItemDustsSmall.getSmallDustByName("Lead", 1), ItemDustsSmall.getSmallDustByName("Sulfur", 1), new ItemStack(Items.bucket), 100, 120));

				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(ModFluids.fluidMercury, 1000), ItemDusts.getDustByName("silver", 3), ItemDustsSmall.getSmallDustByName("Lead", 1), ItemDustsSmall.getSmallDustByName("Sulfur", 1), null, 100, 120));
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(ModItems.bucketMercury), null, ItemDusts.getDustByName("silver", 3), ItemDustsSmall.getSmallDustByName("Lead", 1), ItemDustsSmall.getSmallDustByName("Sulfur", 1), new ItemStack(Items.bucket), 100, 120));
			} catch (Exception e) {
				LogHelper.info("Failed to Load Grinder Recipe for Silver Ore");
			}
		}

		//Lead Ore
		if (OreDictionary.doesOreNameExist("oreLead")) {
			try {
				ItemStack oreStack = OreDictionary.getOres("oreLead").get(0);
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("lead", 2), ItemDustsSmall.getSmallDustByName("Silver", 1), ItemDustsSmall.getSmallDustByName("Sulfur", 1), null, 100, 120));
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, ItemDusts.getDustByName("lead", 2), ItemDustsSmall.getSmallDustByName("Silver", 1), ItemDustsSmall.getSmallDustByName("Sulfur", 1), new ItemStack(Items.bucket), 100, 120));

				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(ModFluids.fluidMercury, 1000), ItemDusts.getDustByName("lead", 2), ItemDusts.getDustByName("silver", 1), ItemDustsSmall.getSmallDustByName("Sulfur", 1), null, 100, 120));
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(ModItems.bucketMercury), null, ItemDusts.getDustByName("lead", 2), ItemDusts.getDustByName("silver", 1), ItemDustsSmall.getSmallDustByName("Sulfur", 1), new ItemStack(Items.bucket), 100, 120));
			} catch (Exception e) {
				LogHelper.info("Failed to Load Grinder Recipe for Lead Ore");
			}
		}


		//Aluminum Ore
		if (OreDictionary.doesOreNameExist("oreAluminum")) {
			try {
				ItemStack oreStack = OreDictionary.getOres("oreAluminum").get(0);
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("aluminum", 2), ItemDustsSmall.getSmallDustByName("Bauxite", 1), ItemDustsSmall.getSmallDustByName("Bauxite", 1), null, 100, 120));
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, ItemDusts.getDustByName("aluminum", 2), ItemDustsSmall.getSmallDustByName("Bauxite", 1), ItemDustsSmall.getSmallDustByName("Bauxite", 1), new ItemStack(Items.bucket), 100, 120));
			} catch (Exception e) {
				LogHelper.info("Failed to Load Grinder Recipe for Lead Ore");
			}
		}

		//Ardite Ore
		if (OreDictionary.doesOreNameExist("oreArdite")) {
			try {
				ItemStack oreStack = OreDictionary.getOres("oreArdite").get(0);
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("ardite", 2), ItemDustsSmall.getSmallDustByName("Ardite", 1), ItemDustsSmall.getSmallDustByName("Ardite", 1), null, 100, 120));
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, ItemDusts.getDustByName("ardite", 2), ItemDustsSmall.getSmallDustByName("Ardite", 1), ItemDustsSmall.getSmallDustByName("Ardite", 1), new ItemStack(Items.bucket), 100, 120));
			} catch (Exception e) {
				LogHelper.info("Failed to Load Grinder Recipe for Ardite Ore");
			}
		}

		//Cobalt Ore
		if (OreDictionary.doesOreNameExist("oreCobalt")) {
			try {
				ItemStack oreStack = OreDictionary.getOres("oreCobalt").get(0);
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("cobalt", 2), ItemDustsSmall.getSmallDustByName("Cobalt", 1), ItemDustsSmall.getSmallDustByName("Cobalt", 1), null, 100, 120));
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, ItemDusts.getDustByName("cobalt", 2), ItemDustsSmall.getSmallDustByName("Cobalt", 1), ItemDustsSmall.getSmallDustByName("Cobalt", 1), new ItemStack(Items.bucket), 100, 120));
			} catch (Exception e) {
				LogHelper.info("Failed to Load Grinder Recipe for Cobalt Ore");
			}
		}

		//Dark Iron Ore
		if (OreDictionary.doesOreNameExist("oreDarkIron")) {
			try {
				ItemStack oreStack = OreDictionary.getOres("oreDarkIron").get(0);
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("darkIron", 2), ItemDustsSmall.getSmallDustByName("DarkIron", 1), ItemDustsSmall.getSmallDustByName("Iron", 1), null, 100, 120));
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, ItemDusts.getDustByName("darkIron", 2), ItemDustsSmall.getSmallDustByName("DarkIron", 1), ItemDustsSmall.getSmallDustByName("Iron", 1), new ItemStack(Items.bucket), 100, 120));
			} catch (Exception e) {
				LogHelper.info("Failed to Load Grinder Recipe for Dark Iron Ore");
			}
		}

		//Cadmium Ore
		if (OreDictionary.doesOreNameExist("oreCadmium")) {
			try {
				ItemStack oreStack = OreDictionary.getOres("oreCadmium").get(0);
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("cadmium", 2), ItemDustsSmall.getSmallDustByName("Cadmium", 1), ItemDustsSmall.getSmallDustByName("Cadmium", 1), null, 100, 120));
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, ItemDusts.getDustByName("cadmium", 2), ItemDustsSmall.getSmallDustByName("Cadmium", 1), ItemDustsSmall.getSmallDustByName("Cadmium", 1), new ItemStack(Items.bucket), 100, 120));
			} catch (Exception e) {
				LogHelper.info("Failed to Load Grinder Recipe for Cadmium Ore");
			}
		}

		//Indium Ore
		if (OreDictionary.doesOreNameExist("oreIndium")) {
			try {
				ItemStack oreStack = OreDictionary.getOres("oreIndium").get(0);
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("indium", 2), ItemDustsSmall.getSmallDustByName("Indium", 1), ItemDustsSmall.getSmallDustByName("Indium", 1), null, 100, 120));
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, ItemDusts.getDustByName("indium", 2), ItemDustsSmall.getSmallDustByName("Indium", 1), ItemDustsSmall.getSmallDustByName("Indium", 1), new ItemStack(Items.bucket), 100, 120));
			} catch (Exception e) {
				LogHelper.info("Failed to Load Grinder Recipe for Indium Ore");
			}
		}

		//Calcite Ore
		if (OreDictionary.doesOreNameExist("oreCalcite") && OreDictionary.doesOreNameExist("gemCalcite")) {
			try {
				ItemStack oreStack = OreDictionary.getOres("oreCalcite").get(0);
				ItemStack gemStack = OreDictionary.getOres("gemCalcite").get(0);
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), gemStack, ItemDustsSmall.getSmallDustByName("Calcite", 6), null, null, 100, 120));
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, gemStack, ItemDustsSmall.getSmallDustByName("Calcite", 6), null, new ItemStack(Items.bucket), 100, 120));
			} catch (Exception e) {
				LogHelper.info("Failed to Load Grinder Recipe for Calcite Ore");
			}
		}

		//Magnetite Ore
		if (OreDictionary.doesOreNameExist("oreMagnetite") && OreDictionary.doesOreNameExist("chunkMagnetite")) {
			try {
				ItemStack oreStack = OreDictionary.getOres("oreMagnetite").get(0);
				ItemStack chunkStack = OreDictionary.getOres("chunkMagnetite").get(0);
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), chunkStack, ItemDustsSmall.getSmallDustByName("Magnetite", 6), null, null, 100, 120));
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, chunkStack, ItemDustsSmall.getSmallDustByName("Magnetite", 6), null, new ItemStack(Items.bucket), 100, 120));
			} catch (Exception e) {
				LogHelper.info("Failed to Load Grinder Recipe for Magnetite Ore");
			}
		}

		//Graphite Ore
		if (OreDictionary.doesOreNameExist("oreGraphite") && OreDictionary.doesOreNameExist("chunkGraphite")) {
			try {
				ItemStack oreStack = OreDictionary.getOres("oreGraphite").get(0);
				ItemStack chunkStack = OreDictionary.getOres("chunkGraphite").get(0);
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), chunkStack, ItemDustsSmall.getSmallDustByName("Graphite", 6), null, null, 100, 120));
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, chunkStack, ItemDustsSmall.getSmallDustByName("Graphite", 6), null, new ItemStack(Items.bucket), 100, 120));
			} catch (Exception e) {
				LogHelper.info("Failed to Load Grinder Recipe for Graphite Ore");
			}
		}

		//Osmium Ore
		if (OreDictionary.doesOreNameExist("oreOsmium")) {
			try {
				ItemStack oreStack = OreDictionary.getOres("oreOsmium").get(0);
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("osmium", 2), ItemDustsSmall.getSmallDustByName("Osmium", 1), ItemDustsSmall.getSmallDustByName("Osmium", 1), null, 100, 120));
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, ItemDusts.getDustByName("osmium", 2), ItemDustsSmall.getSmallDustByName("Osmium", 1), ItemDustsSmall.getSmallDustByName("Osmium", 1), new ItemStack(Items.bucket), 100, 120));
			} catch (Exception e) {
				LogHelper.info("Failed to Load Grinder Recipe for Osmium Ore");
			}
		}

		//Teslatite Ore
		if (OreDictionary.doesOreNameExist("oreTeslatite") && OreDictionary.doesOreNameExist("dustTeslatite")) {
			try {
				ItemStack oreStack = OreDictionary.getOres("oreTeslatite").get(0);
				ItemStack dustStack = OreDictionary.getOres("dustTeslatite").get(0);
				dustStack.stackSize = 10;
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), dustStack, ItemDustsSmall.getSmallDustByName("Sodalite", 1), ItemDustsSmall.getSmallDustByName("Glowstone", 1), null, 100, 120));
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, dustStack, ItemDustsSmall.getSmallDustByName("Sodalite", 1), ItemDustsSmall.getSmallDustByName("Glowstone", 1), new ItemStack(Items.bucket), 100, 120));
			} catch (Exception e) {
				LogHelper.info("Failed to Load Grinder Recipe for Teslatite Ore");
			}
		}

		//Sulfur Ore
		if (OreDictionary.doesOreNameExist("oreSulfur")) {
			try {
				ItemStack oreStack = OreDictionary.getOres("oreSulfur").get(0);
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("sulfur", 2), ItemDustsSmall.getSmallDustByName("Sulfur", 1), ItemDustsSmall.getSmallDustByName("Sulfur", 1), null, 100, 120));
			} catch (Exception e) {
				LogHelper.info("Failed to Load Grinder Recipe for Sulfur Ore");
			}
		}

		//Saltpeter Ore
		if (OreDictionary.doesOreNameExist("oreSaltpeter")) {
			try {
				ItemStack oreStack = OreDictionary.getOres("oreSaltpeter").get(0);
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("saltpeter", 2), ItemDustsSmall.getSmallDustByName("Saltpeter", 1), ItemDustsSmall.getSmallDustByName("Saltpeter", 1), null, 100, 120));
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, ItemDusts.getDustByName("saltpeter", 2), ItemDustsSmall.getSmallDustByName("Saltpeter", 1), ItemDustsSmall.getSmallDustByName("Saltpeter", 1), new ItemStack(Items.bucket), 100, 120));
			} catch (Exception e) {
				LogHelper.info("Failed to Load Grinder Recipe for Saltpeter Ore");
			}
		}

		//Apatite Ore
		if (OreDictionary.doesOreNameExist("oreApatite") & OreDictionary.doesOreNameExist("gemApatite")) {
			try {
				ItemStack oreStack = OreDictionary.getOres("oreApatite").get(0);
				ItemStack gemStack = OreDictionary.getOres("gemApatite").get(0);
				gemStack.stackSize = 6;
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), gemStack, gemStack, ItemDustsSmall.getSmallDustByName("Phosphorous", 4), null, 100, 120));
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, gemStack, gemStack, ItemDustsSmall.getSmallDustByName("Phosphorous", 4), new ItemStack(Items.bucket), 100, 120));
			} catch (Exception e) {
				LogHelper.info("Failed to Load Grinder Recipe for Apatite Ore");
			}
		}

		//Nether Quartz Ore
		if (OreDictionary.doesOreNameExist("dustNetherQuartz")) {
			try {
				ItemStack dustStack = OreDictionary.getOres("dustNetherQuartz").get(0);
				dustStack.stackSize = 4;
				RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.quartz_ore, 1), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(Items.quartz, 2), dustStack, ItemDustsSmall.getSmallDustByName("Netherrack", 2), null, 100, 120));
				RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.quartz_ore, 1), new ItemStack(Items.water_bucket), null, new ItemStack(Items.quartz, 2), dustStack, ItemDustsSmall.getSmallDustByName("Netherrack", 2), new ItemStack(Items.bucket), 100, 120));
			} catch (Exception e) {
				LogHelper.info("Failed to Load Grinder Recipe for Nether Quartz Ore");
			}
		}

		//Certus Quartz Ore
		if (OreDictionary.doesOreNameExist("oreCertusQuartz")) {
			try {
				ItemStack oreStack = OreDictionary.getOres("oreCertusQuartz").get(0);
				ItemStack gemStack = OreDictionary.getOres("crystalCertusQuartz").get(0);
				ItemStack dustStack = OreDictionary.getOres("dustCertusQuartz").get(0);
				dustStack.stackSize = 2;
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), gemStack, dustStack, null, null, 100, 120));
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, gemStack, dustStack, null, new ItemStack(Items.bucket), 100, 120));
			} catch (Exception e) {
				LogHelper.info("Failed to Load Grinder Recipe for Certus Quartz Ore");
			}
		}

		//Charged Certus Quartz Ore
		if (OreDictionary.doesOreNameExist("oreChargedCertusQuartz")) {
			try {
				ItemStack oreStack = OreDictionary.getOres("oreChargedCertusQuartz").get(0);
				ItemStack gemStack = OreDictionary.getOres("crystalChargedCertusQuartz").get(0);
				ItemStack dustStack = OreDictionary.getOres("dustCertusQuartz").get(0);
				dustStack.stackSize = 2;
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), gemStack, dustStack, null, null, 100, 120));
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, gemStack, dustStack, null, new ItemStack(Items.bucket), 100, 120));
			} catch (Exception e) {
				LogHelper.info("Failed to Load Grinder Recipe for Charged Certus Quartz Ore");
			}
		}

		//Amethyst Ore
		if (OreDictionary.doesOreNameExist("oreAmethyst") && OreDictionary.doesOreNameExist("gemAmethyst")) {
			try {
				ItemStack oreStack = OreDictionary.getOres("oreAmethyst").get(0);
				ItemStack gemStack = OreDictionary.getOres("gemAmethyst").get(0);
				gemStack.stackSize = 2;
				ItemStack dustStack = OreDictionary.getOres("gemAmethyst").get(0);
				dustStack.stackSize = 1;
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), gemStack, dustStack, null, null, 100, 120));
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, gemStack, dustStack, null, new ItemStack(Items.bucket), 100, 120));
			} catch (Exception e) {
				LogHelper.info("Failed to Load Grinder Recipe for Certus Quartz Ore");
			}
		}

		//Topaz Ore
		if (OreDictionary.doesOreNameExist("oreTopaz") && OreDictionary.doesOreNameExist("gemTopaz")) {
			try {
				ItemStack oreStack = OreDictionary.getOres("oreTopaz").get(0);
				ItemStack gemStack = OreDictionary.getOres("gemTopaz").get(0);
				gemStack.stackSize = 2;
				ItemStack dustStack = OreDictionary.getOres("gemTopaz").get(0);
				dustStack.stackSize = 1;
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), gemStack, dustStack, null, null, 100, 120));
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, gemStack, dustStack, null, new ItemStack(Items.bucket), 100, 120));
			} catch (Exception e) {
				LogHelper.info("Failed to Load Grinder Recipe for Topaz Ore");
			}
		}

		//Tanzanite Ore
		if (OreDictionary.doesOreNameExist("oreTanzanite") && OreDictionary.doesOreNameExist("gemTanzanite")) {
			try {
				ItemStack oreStack = OreDictionary.getOres("oreTanzanite").get(0);
				ItemStack gemStack = OreDictionary.getOres("gemTanzanite").get(0);
				gemStack.stackSize = 2;
				ItemStack dustStack = OreDictionary.getOres("gemTanzanite").get(0);
				dustStack.stackSize = 1;
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), gemStack, dustStack, null, null, 100, 120));
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, gemStack, dustStack, null, new ItemStack(Items.bucket), 100, 120));
			} catch (Exception e) {
				LogHelper.info("Failed to Load Grinder Recipe for Tanzanite Ore");
			}
		}

		//Malachite Ore
		if (OreDictionary.doesOreNameExist("oreMalachite") && OreDictionary.doesOreNameExist("gemMalachite")) {
			try {
				ItemStack oreStack = OreDictionary.getOres("oreMalachite").get(0);
				ItemStack gemStack = OreDictionary.getOres("gemMalachite").get(0);
				gemStack.stackSize = 2;
				ItemStack dustStack = OreDictionary.getOres("gemMalachite").get(0);
				dustStack.stackSize = 1;
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), gemStack, dustStack, null, null, 100, 120));
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, gemStack, dustStack, null, new ItemStack(Items.bucket), 100, 120));
			} catch (Exception e) {
				LogHelper.info("Failed to Load Grinder Recipe for Malachite Ore");
			}
		}

		//Galena Ore
		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 0), null, new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("galena", 2), ItemDustsSmall.getSmallDustByName("Sulfur", 1), ItemDustsSmall.getSmallDustByName("Silver", 1), null, 100, 120));
		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 0), new ItemStack(Items.water_bucket), null, ItemDusts.getDustByName("galena", 2), ItemDustsSmall.getSmallDustByName("Sulfur", 1), ItemDustsSmall.getSmallDustByName("Silver", 1), new ItemStack(Items.bucket), 100, 120));

		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 0), null, new FluidStack(ModFluids.fluidMercury, 1000), ItemDusts.getDustByName("galena", 2), ItemDustsSmall.getSmallDustByName("Sulfur", 1), ItemDusts.getDustByName("silver", 1), null, 100, 120));
		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 0), new ItemStack(ModItems.bucketMercury), null, ItemDusts.getDustByName("galena", 2), ItemDustsSmall.getSmallDustByName("Sulfur", 1), ItemDusts.getDustByName("silver", 1), new ItemStack(Items.bucket), 100, 120));


		//Ruby Ore
		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 2), null, new FluidStack(FluidRegistry.WATER, 1000), ItemGems.getGemByName("ruby", 1), ItemDustsSmall.getSmallDustByName("Ruby", 6), ItemDustsSmall.getSmallDustByName("Chrome", 2), null, 100, 120));
		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 2), new ItemStack(Items.water_bucket), null, ItemGems.getGemByName("ruby", 1), ItemDustsSmall.getSmallDustByName("Ruby", 6), ItemDustsSmall.getSmallDustByName("Chrome", 2), new ItemStack(Items.bucket), 100, 120));

		//Sapphire Ore
		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 3), null, new FluidStack(FluidRegistry.WATER, 1000), ItemGems.getGemByName("sapphire", 1), ItemDustsSmall.getSmallDustByName("Sapphire", 6), ItemDustsSmall.getSmallDustByName("Aluminum", 2), null, 100, 120));
		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 3), new ItemStack(Items.water_bucket), null, ItemGems.getGemByName("sapphire", 1), ItemDustsSmall.getSmallDustByName("Sapphire", 6), ItemDustsSmall.getSmallDustByName("Aluminum", 2), new ItemStack(Items.bucket), 100, 120));

		//Bauxite Ore
		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 4), null, new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("bauxite", 2), ItemDustsSmall.getSmallDustByName("Grossular", 4), ItemDustsSmall.getSmallDustByName("Titanium", 4), null, 100, 120));
		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 4), new ItemStack(Items.water_bucket), null, ItemDusts.getDustByName("bauxite", 2), ItemDustsSmall.getSmallDustByName("Grossular", 4), ItemDustsSmall.getSmallDustByName("Titanium", 4), new ItemStack(Items.bucket), 100, 120));

		//Pyrite Ore
		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 5), null, new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("pyrite", 2), ItemDustsSmall.getSmallDustByName("Sulfur", 1), ItemDustsSmall.getSmallDustByName("Phosphorous", 1), null, 100, 120));
		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 5), new ItemStack(Items.water_bucket), null, ItemDusts.getDustByName("pyrite", 2), ItemDustsSmall.getSmallDustByName("Sulfur", 1), ItemDustsSmall.getSmallDustByName("Phosphorous", 1), new ItemStack(Items.bucket), 100, 120));

		//Cinnabar Ore
		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 6), null, new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("cinnabar", 2), ItemDustsSmall.getSmallDustByName("Redstone", 1), ItemDustsSmall.getSmallDustByName("Glowstone", 1), null, 100, 120));
		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 6), new ItemStack(Items.water_bucket), null, ItemDusts.getDustByName("cinnabar", 2), ItemDustsSmall.getSmallDustByName("Redstone", 1), ItemDustsSmall.getSmallDustByName("Glowstone", 1), new ItemStack(Items.bucket), 100, 120));

		//Sphalerite Ore
		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 7), null, new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("sphalerite", 2), ItemDustsSmall.getSmallDustByName("Zinc", 1), ItemDustsSmall.getSmallDustByName("YellowGarnet", 1), null, 100, 120));
		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 7), new ItemStack(Items.water_bucket), null, ItemDusts.getDustByName("sphalerite", 2), ItemDustsSmall.getSmallDustByName("Zinc", 1), ItemDustsSmall.getSmallDustByName("YellowGarnet", 1), new ItemStack(Items.bucket), 100, 120));

		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 7), null, new FluidStack(ModFluids.fluidSodiumpersulfate, 1000), ItemDusts.getDustByName("sphalerite", 2), ItemDusts.getDustByName("zinc", 1), ItemDustsSmall.getSmallDustByName("YellowGarnet", 1), null, 100, 120));
		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 7), new ItemStack(ModItems.bucketSodiumpersulfate), null, ItemDusts.getDustByName("sphalerite", 2), ItemDusts.getDustByName("zinc", 1), ItemDustsSmall.getSmallDustByName("YellowGarnet", 1), new ItemStack(Items.bucket), 100, 120));

		//Tungsten Ore
		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 8), null, new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("tungsten", 2), ItemDustsSmall.getSmallDustByName("Manganese", 1), ItemDustsSmall.getSmallDustByName("Silver", 1), null, 100, 120));
		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 8), new ItemStack(Items.water_bucket), null, ItemDusts.getDustByName("tungsten", 2), ItemDustsSmall.getSmallDustByName("Manganese", 1), ItemDustsSmall.getSmallDustByName("Silver", 1), new ItemStack(Items.bucket), 100, 120));

		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 8), null, new FluidStack(ModFluids.fluidMercury, 1000), ItemDusts.getDustByName("tungsten", 2), ItemDustsSmall.getSmallDustByName("Manganese", 1), ItemDusts.getDustByName("silver", 2), null, 100, 120));
		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 8), new ItemStack(ModItems.bucketMercury), null, ItemDusts.getDustByName("tungsten", 2), ItemDustsSmall.getSmallDustByName("Manganese", 1), ItemDusts.getDustByName("silver", 2), new ItemStack(Items.bucket), 100, 120));

		//Sheldonite Ore
		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 9), null, new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("platinum", 2), ItemDustsSmall.getSmallDustByName("Iridium", 1), ItemDustsSmall.getSmallDustByName("Iridium", 1), null, 100, 120));
		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 9), new ItemStack(Items.water_bucket), null, ItemDusts.getDustByName("platinum", 2), ItemDustsSmall.getSmallDustByName("Iridium", 1), ItemDustsSmall.getSmallDustByName("Iridium", 1), new ItemStack(Items.bucket), 100, 120));

		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 9), null, new FluidStack(ModFluids.fluidMercury, 1000), ItemDusts.getDustByName("platinum", 3), ItemDustsSmall.getSmallDustByName("Iridium", 1), ItemDustsSmall.getSmallDustByName("Iridium", 1), null, 100, 120));
		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 9), new ItemStack(ModItems.bucketMercury), null, ItemDusts.getDustByName("platinum", 3), ItemDustsSmall.getSmallDustByName("Iridium", 1), ItemDustsSmall.getSmallDustByName("Iridium", 1), new ItemStack(Items.bucket), 100, 120));

		//Peridot Ore
		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 10), null, new FluidStack(FluidRegistry.WATER, 1000), ItemGems.getGemByName("peridot", 1), ItemDustsSmall.getSmallDustByName("Peridot", 6), ItemDustsSmall.getSmallDustByName("Pyrope", 2), null, 100, 120));
		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 10), new ItemStack(Items.water_bucket), null, ItemGems.getGemByName("peridot", 1), ItemDustsSmall.getSmallDustByName("Peridot", 6), ItemDustsSmall.getSmallDustByName("Pyrope", 2), new ItemStack(Items.bucket), 100, 120));

		//Sodalite Ore
		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 11), null, new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("sodalite", 12), ItemDustsSmall.getSmallDustByName("Lazurite", 4), ItemDustsSmall.getSmallDustByName("Lapis", 4), null, 100, 120));
		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 11), new ItemStack(Items.water_bucket), null, ItemDusts.getDustByName("sodalite", 12), ItemDustsSmall.getSmallDustByName("Lazurite", 4), ItemDustsSmall.getSmallDustByName("Lapis", 4), new ItemStack(Items.bucket), 100, 120));

		//Tetrahedrite Ore
		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 12), null, new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("tetrahedrite", 2), ItemDustsSmall.getSmallDustByName("Antimony", 1), ItemDustsSmall.getSmallDustByName("Zinc", 1), null, 100, 120));
		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 12), new ItemStack(Items.water_bucket), null, ItemDusts.getDustByName("tetrahedrite", 2), ItemDustsSmall.getSmallDustByName("Antimony", 1), ItemDustsSmall.getSmallDustByName("Zinc", 1), new ItemStack(Items.bucket), 100, 120));

		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 12), null, new FluidStack(ModFluids.fluidSodiumpersulfate, 1000), ItemDusts.getDustByName("tetrahedrite", 3), ItemDustsSmall.getSmallDustByName("Antimony", 1), ItemDustsSmall.getSmallDustByName("Zinc", 1), null, 100, 120));
		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 12), new ItemStack(ModItems.bucketSodiumpersulfate), null, ItemDusts.getDustByName("tetrahedrite", 3), ItemDustsSmall.getSmallDustByName("Antimony", 1), ItemDustsSmall.getSmallDustByName("Zinc", 1), new ItemStack(Items.bucket), 100, 120));
	}

	static void addImplosionCompressorRecipes() {
	}

	static void addChemicalReactorRecipes() {
		RecipeHandler.addRecipe(new ChemicalReactorRecipe(ItemCells.getCellByName("calcium", 1), ItemCells.getCellByName("carbon", 1), ItemCells.getCellByName("calciumCarbonate", 2), 240, 30));
		RecipeHandler.addRecipe(new ChemicalReactorRecipe(new ItemStack(Items.gold_nugget, 8), new ItemStack(Items.melon, 1), new ItemStack(Items.speckled_melon, 1), 40, 30));
		RecipeHandler.addRecipe(new ChemicalReactorRecipe(ItemCells.getCellByName("nitrogen", 1), ItemCells.getCellByName("carbon", 1), ItemCells.getCellByName("nitrocarbon", 2), 1500, 30));
		RecipeHandler.addRecipe(new ChemicalReactorRecipe(ItemCells.getCellByName("carbon", 1), ItemCells.getCellByName("hydrogen", 4), ItemCells.getCellByName("methane", 5), 3500, 30));
		RecipeHandler.addRecipe(new ChemicalReactorRecipe(ItemCells.getCellByName("sulfur", 1), ItemCells.getCellByName("sodium", 1), ItemCells.getCellByName("sodiumSulfide", 2), 100, 30));
		RecipeHandler.addRecipe(new ChemicalReactorRecipe(new ItemStack(Items.blaze_powder, 1), new ItemStack(Items.ender_pearl, 1), new ItemStack(Items.ender_eye, 1), 40, 30));
		RecipeHandler.addRecipe(new ChemicalReactorRecipe(new ItemStack(Items.gold_nugget, 8), new ItemStack(Items.carrot, 1), new ItemStack(Items.golden_carrot, 1), 40, 30));
		RecipeHandler.addRecipe(new ChemicalReactorRecipe(ItemCells.getCellByName("glyceryl", 1), ItemCells.getCellByName("diesel", 4), ItemCells.getCellByName("nitroDiesel", 5), 1000, 30));
		RecipeHandler.addRecipe(new ChemicalReactorRecipe(new ItemStack(Items.gold_ingot, 8), new ItemStack(Items.apple, 1), new ItemStack(Items.golden_apple, 1), 40, 30));
		RecipeHandler.addRecipe(new ChemicalReactorRecipe(new ItemStack(Blocks.gold_block, 8), new ItemStack(Items.apple, 1), new ItemStack(Items.golden_apple, 1, 1), 40, 30));
		RecipeHandler.addRecipe(new ChemicalReactorRecipe(new ItemStack(Items.blaze_powder, 1), new ItemStack(Items.slime_ball, 1), new ItemStack(Items.magma_cream, 1), 40, 30));
	}

	static void addIndustrialElectrolyzerRecipes() {


		RecipeHandler.addRecipe(new IndustrialElectrolyzerRecipe(
				ItemCells.getCellByName("nitrocarbon", 2),
				null,
				ItemCells.getCellByName("nitrogen"),
				ItemCells.getCellByName("carbon"),
				null,
				null,
				80, 60
		));


	}

	static void addLogs() {
		FarmTree.harvestableLogs.add(Blocks.log);
		FarmTree.harvestableLogs.add(Blocks.log2);
	}
}

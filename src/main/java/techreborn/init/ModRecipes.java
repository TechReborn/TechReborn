package techreborn.init;

import ic2.api.item.IC2Items;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import techreborn.api.BlastFurnaceRecipe;
import techreborn.api.TechRebornAPI;
import techreborn.api.recipe.RecipeHandler;
import techreborn.api.recipe.machines.AlloySmelterRecipe;
import techreborn.api.recipe.machines.AssemblingMachineRecipe;
import techreborn.api.recipe.machines.CentrifugeRecipe;
import techreborn.api.recipe.machines.ChemicalReactorRecipe;
import techreborn.api.recipe.machines.GrinderRecipe;
import techreborn.api.recipe.machines.ImplosionCompressorRecipe;
import techreborn.api.recipe.machines.IndustrialSawmillRecipe;
import techreborn.api.recipe.machines.LatheRecipe;
import techreborn.api.recipe.machines.PlateCuttingMachineRecipe;
import techreborn.config.ConfigTechReborn;
import techreborn.items.*;
import techreborn.util.CraftingHelper;
import techreborn.util.LogHelper;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModRecipes {
	public static ConfigTechReborn config;

	public static void init() {
		addShaplessRecipes();
		addShappedRecipes();
		addSmeltingRecipes();
		addMachineRecipes();
        addAlloySmelterRecipes();
        addLatheRecipes();
        addPlateCuttingMachineRecipes();
		addUUrecipes();
		addHammerRecipes();
	}

	public static void addShappedRecipes() {
		// Storage Blocks
		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.storage, 1, 0),
				 "AAA", "AAA", "AAA",
					'A', "ingotSilver");

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.storage, 1, 1),
				 "AAA", "AAA", "AAA",
					'A', "ingotAluminium");

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.storage, 1, 2),
				 "AAA", "AAA", "AAA",
					'A', "ingotTitanium");

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.storage, 1, 3),
				 "AAA", "AAA", "AAA",
					'A', "gemSapphire");

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.storage, 1, 4),
				 "AAA", "AAA", "AAA",
					'A', "gemRuby");

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.storage, 1, 5),
				 "AAA", "AAA", "AAA",
					'A', "gemGreenSapphire");

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.storage, 1, 6),
				 "AAA", "AAA", "AAA",
					'A', "ingotChrome");

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.storage, 1, 7),
				 "AAA", "AAA", "AAA",
					'A', "ingotElectrum");

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.storage, 1, 8),
				 "AAA", "AAA", "AAA",
					'A', "ingotTungsten");

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.storage, 1, 9),
				"AAA", "AAA", "AAA",
					'A', "ingotLead");

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.storage, 1, 10),
			"AAA", "AAA", "AAA",
				'A', "ingotZinc");

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.storage, 1, 11),
			"AAA", "AAA", "AAA",
				'A', "ingotBrass");

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.storage, 1, 12),
			"AAA", "AAA", "AAA",
				'A', "ingotSteel");

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.storage, 1, 13),
			"AAA", "AAA", "AAA",
				'A', "ingotPlatinum");

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.storage, 1, 14),
			"AAA", "AAA", "AAA",
				'A', "ingotNickel");

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.storage, 1, 15),
			"AAA", "AAA", "AAA",
				'A', "ingotInvar");

		LogHelper.info("Shapped Recipes Added");
	}

	public static void addShaplessRecipes() {
		CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.ingots, 9, 4), "blockSilver");
		CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.ingots, 9, 5), "blockAluminium");
		CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.ingots, 9, 6), "blockTitanium");
		CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.gems, 9, 1), "blockSapphire");
		CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.gems, 9, 0), "blockRuby");
		CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.gems, 9, 2), "blockGreenSapphire");
		CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.ingots, 9, 7), "blockChrome");
		CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.ingots, 9, 8), "blockElectrum");
		CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.ingots, 9, 9), "blockTungsten");
		CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.ingots, 9, 10), "blockLead");
		CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.ingots, 9, 11), "blockZinc");
		CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.ingots, 9, 12), "blockBrass");
		CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.ingots, 9, 13), "blockSteel");
		CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.ingots, 9, 14), "blockPlatinum");
		CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.ingots, 9, 15), "blockNickel");
		CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.ingots, 9, 16), "blockInvar");
		CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.rockCutter, 1, 27), Items.apple);

		LogHelper.info("Shapless Recipes Added");
	}

	public static void addSmeltingRecipes() {
		GameRegistry.addSmelting(new ItemStack(ModItems.dusts, 1, 27), new ItemStack(Items.iron_ingot), 1F);
		GameRegistry.addSmelting(new ItemStack(ModItems.dusts, 1, 23), new ItemStack(Items.gold_ingot), 1F);
		GameRegistry.addSmelting(new ItemStack(ModItems.dusts, 1, 14), IC2Items.getItem("copperIngot"), 1F);
		GameRegistry.addSmelting(new ItemStack(ModItems.dusts, 1, 51), IC2Items.getItem("tinIngot"), 1F);
		GameRegistry.addSmelting(new ItemStack(ModItems.dusts, 1, 7), IC2Items.getItem("bronzeIngot"), 1F);
		GameRegistry.addSmelting(new ItemStack(ModItems.dusts, 1, 29), IC2Items.getItem("leadIngot"), 1F);
		GameRegistry.addSmelting(new ItemStack(ModItems.dusts, 1, 45), IC2Items.getItem("silverIngot"), 1F);

		LogHelper.info("Smelting Recipes Added");
	}

	public static void addMachineRecipes() {

		TechRebornAPI.addRollingMachinceRecipe(new ItemStack(Blocks.furnace, 4), "ccc", "c c", "ccc", 'c', Blocks.cobblestone);
		TechRebornAPI.registerBlastFurnaceRecipe(new BlastFurnaceRecipe(new ItemStack(Items.apple), new ItemStack(Items.ender_pearl), new ItemStack(Items.golden_apple), new ItemStack(Items.diamond), 120, 1000));

		RecipeHandler.addRecipe(new ImplosionCompressorRecipe(new ItemStack(Blocks.netherrack, 4), new ItemStack(Blocks.diamond_block, 1), new ItemStack(ModItems.bucketTritium), null, 120, 5));

		RecipeHandler.addRecipe(new AssemblingMachineRecipe(new ItemStack(Items.coal), new ItemStack(Blocks.sand), new ItemStack(Items.diamond), 120, 5));
		RecipeHandler.addRecipe(new LatheRecipe(new ItemStack(Items.coal), new ItemStack(Items.diamond), 120, 5));
		RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(new ItemStack(Items.coal), new ItemStack(Items.diamond), 120, 5));
		//TODO
		RecipeHandler.addRecipe(new ChemicalReactorRecipe(new ItemStack(Items.coal), new ItemStack(Blocks.sand), new ItemStack(Items.diamond), 120, 5));

		RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 1), null, new FluidStack(ModFluids.fluidMercury, 500), IC2Items.getItem("iridiumOre"), new ItemStack(ModItems.smallDusts, 6, 39), new ItemStack(ModItems.dusts, 6, 58), null, 400, 5));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 1), new ItemStack(ModItems.cells, 1, 16), null, IC2Items.getItem("iridiumOre"), new ItemStack(ModItems.smallDusts, 6, 39), new ItemStack(ModItems.dusts, 6, 58), IC2Items.getItem("cell"), 400, 5));

		RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.coal), null, new ItemStack(Items.diamond), new ItemStack(Items.emerald), new ItemStack(Items.apple), new ItemStack(Items.arrow), 1, 10));

		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(Blocks.log, 1), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(Blocks.planks, 6, 0), ItemDusts.getDustByName("ashes", 1), null, 100, 10));

		LogHelper.info("Machine Recipes Added");
	}

	public static void addHammerRecipes(){
		ItemStack hammerIron = new ItemStack(ModItems.hammerIron, 1, OreDictionary.WILDCARD_VALUE);
		ItemStack hammerDiamond = new ItemStack(ModItems.hammerDiamond, 1, OreDictionary.WILDCARD_VALUE);
		
		// :( I cant do this 
//	    List<ItemStack> anyhammer = Arrays.asList(hammerIron, hammerDiamond);

		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.plate, 1, 13), hammerIron, new ItemStack(Items.iron_ingot));

	}

	public static void addAlloySmelterRecipes(){
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
        if(OreDictionary.doesOreNameExist("ingotBrass")) {
            ItemStack brassStack = OreDictionary.getOres("ingotBrass").get(0);
            brassStack.stackSize = 4;
            RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemIngots.getIngotByName("copper", 3), ItemIngots.getIngotByName("zinc", 1), brassStack, 200, 16));
            RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemIngots.getIngotByName("copper", 3), ItemDusts.getDustByName("zinc", 1), brassStack, 200, 16));
            RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemDusts.getDustByName("copper", 3), ItemIngots.getIngotByName("zinc", 1), brassStack, 200, 16));
            RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemDusts.getDustByName("copper", 3), ItemDusts.getDustByName("zinc", 1), brassStack, 200, 16));
        }

        //Red Alloy
        if(OreDictionary.doesOreNameExist("ingotRedAlloy")) {
            ItemStack redAlloyStack = OreDictionary.getOres("ingotRedAlloy").get(0);
            redAlloyStack.stackSize = 1;
            RecipeHandler.addRecipe(new AlloySmelterRecipe(new ItemStack(Items.redstone, 4), ItemIngots.getIngotByName("copper", 1), redAlloyStack, 200, 16));
            RecipeHandler.addRecipe(new AlloySmelterRecipe(new ItemStack(Items.redstone, 4), new ItemStack(Items.iron_ingot, 1), redAlloyStack, 200, 16));
        }

        //Blue Alloy
        if(OreDictionary.doesOreNameExist("ingotBlueAlloy")) {
            ItemStack blueAlloyStack = OreDictionary.getOres("ingotBlueAlloy").get(0);
            blueAlloyStack.stackSize = 1;
            RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemDusts.getDustByName("teslatite", 4), ItemIngots.getIngotByName("silver", 1), blueAlloyStack, 200, 16));
        }

        //Blue Alloy
        if(OreDictionary.doesOreNameExist("ingotPurpleAlloy") && OreDictionary.doesOreNameExist("dustInfusedTeslatite")) {
            ItemStack purpleAlloyStack = OreDictionary.getOres("ingotPurpleAlloy").get(0);
            purpleAlloyStack.stackSize = 1;
            ItemStack infusedTeslatiteStack = OreDictionary.getOres("ingotPurpleAlloy").get(0);
            infusedTeslatiteStack.stackSize = 8;
            RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemIngots.getIngotByName("redAlloy", 1), ItemIngots.getIngotByName("blueAlloy", 1), purpleAlloyStack, 200, 16));
            RecipeHandler.addRecipe(new AlloySmelterRecipe(new ItemStack(Items.gold_ingot, 1), infusedTeslatiteStack, purpleAlloyStack, 200, 16));
        }

        //Aluminum Brass
        if(OreDictionary.doesOreNameExist("ingotAluminumBrass")) {
            ItemStack aluminumBrassStack = OreDictionary.getOres("ingotAluminumBrass").get(0);
            aluminumBrassStack.stackSize = 4;
            RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemIngots.getIngotByName("copper", 3), ItemIngots.getIngotByName("aluminum", 1), aluminumBrassStack, 200, 16));
            RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemIngots.getIngotByName("copper", 3), ItemDusts.getDustByName("aluminum", 1), aluminumBrassStack, 200, 16));
            RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemDusts.getDustByName("copper", 3), ItemIngots.getIngotByName("aluminum", 1), aluminumBrassStack, 200, 16));
            RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemDusts.getDustByName("copper", 3), ItemDusts.getDustByName("aluminum", 1), aluminumBrassStack, 200, 16));
        }

        //Manyullyn
        if(OreDictionary.doesOreNameExist("ingotManyullyn") && OreDictionary.doesOreNameExist("ingotCobalt") && OreDictionary.doesOreNameExist("ingotArdite")) {
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
        if(OreDictionary.doesOreNameExist("ingotConductiveIron")) {
            ItemStack conductiveIronStack = OreDictionary.getOres("ingotConductiveIron").get(0);
            conductiveIronStack.stackSize = 1;
            RecipeHandler.addRecipe(new AlloySmelterRecipe(new ItemStack(Items.redstone, 1), new ItemStack(Items.iron_ingot, 1), conductiveIronStack, 200, 16));
        }

        //Redstone Alloy
        if(OreDictionary.doesOreNameExist("ingotRedstoneAlloy") && OreDictionary.doesOreNameExist("itemSilicon")) {
            ItemStack redstoneAlloyStack = OreDictionary.getOres("ingotRedstoneAlloy").get(0);
            redstoneAlloyStack.stackSize = 1;
            ItemStack siliconStack = OreDictionary.getOres("itemSilicon").get(0);
            siliconStack.stackSize = 1;
            RecipeHandler.addRecipe(new AlloySmelterRecipe(new ItemStack(Items.redstone, 1), siliconStack, redstoneAlloyStack, 200, 16));
        }

        //Pulsating Iron
        if(OreDictionary.doesOreNameExist("ingotPhasedIron")) {
            ItemStack pulsatingIronStack = OreDictionary.getOres("ingotPhasedIron").get(0);
            pulsatingIronStack.stackSize = 1;
            RecipeHandler.addRecipe(new AlloySmelterRecipe(new ItemStack(Items.iron_ingot, 1), new ItemStack(Items.ender_pearl, 1), pulsatingIronStack, 200, 16));
            RecipeHandler.addRecipe(new AlloySmelterRecipe(new ItemStack(Items.iron_ingot, 1), ItemDusts.getDustByName("enderPearl", 1), pulsatingIronStack, 200, 16));
        }

        //Vibrant Alloy
        if(OreDictionary.doesOreNameExist("ingotEnergeticAlloy") && OreDictionary.doesOreNameExist("ingotPhasedGold")) {
            ItemStack energeticAlloyStack = OreDictionary.getOres("ingotEnergeticAlloy").get(0);
            energeticAlloyStack.stackSize = 1;
            ItemStack vibrantAlloyStack = OreDictionary.getOres("ingotPhasedGold").get(0);
            vibrantAlloyStack.stackSize = 1;
            RecipeHandler.addRecipe(new AlloySmelterRecipe(energeticAlloyStack, new ItemStack(Items.ender_pearl, 1), vibrantAlloyStack, 200, 16));
            RecipeHandler.addRecipe(new AlloySmelterRecipe(energeticAlloyStack, ItemDusts.getDustByName("enderPearl", 1), vibrantAlloyStack, 200, 16));
        }

        //Soularium
        if(OreDictionary.doesOreNameExist("ingotSoularium")) {
            ItemStack soulariumStack = OreDictionary.getOres("ingotSoularium").get(0);
            soulariumStack.stackSize = 1;
            RecipeHandler.addRecipe(new AlloySmelterRecipe(new ItemStack(Blocks.soul_sand, 1), new ItemStack(Items.gold_ingot, 1), soulariumStack, 200, 16));
        }

    }

	public static void addLatheRecipes() {
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

    public static void addPlateCuttingMachineRecipes() {
        //Storage Blocks
        if(OreDictionary.doesOreNameExist("blockAluminum")) {
            ItemStack blockStack = OreDictionary.getOres("blockAluminum").get(0);
            RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("aluminum", 9), 200, 16));
        }
        if(OreDictionary.doesOreNameExist("blockBrass")) {
            ItemStack blockStack = OreDictionary.getOres("blockBrass").get(0);
            RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("brass", 9), 200, 116));
        }
        if(OreDictionary.doesOreNameExist("blockBronze")) {
            ItemStack blockStack = OreDictionary.getOres("blockBronze").get(0);
            RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("bronze", 9), 200, 16));
        }
        if(OreDictionary.doesOreNameExist("blockCoal")) {
            ItemStack blockStack = OreDictionary.getOres("blockCoal").get(0);
            RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("carbon", 9), 200, 16));
        }
        if(OreDictionary.doesOreNameExist("blockChrome")) {
            ItemStack blockStack = OreDictionary.getOres("blockChrome").get(0);
            RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("chrome", 9), 200, 16));
        }
        if(OreDictionary.doesOreNameExist("blockCopper")) {
            ItemStack blockStack = OreDictionary.getOres("blockCopper").get(0);
            RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("copper", 9), 200, 16));
        }
        if(OreDictionary.doesOreNameExist("blockDiamond")) {
            ItemStack blockStack = OreDictionary.getOres("blockDiamond").get(0);
            RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("diamond", 9), 200, 16));
        }
        if(OreDictionary.doesOreNameExist("blockElectrum")) {
            ItemStack blockStack = OreDictionary.getOres("blockElectrum").get(0);
            RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("electrum", 9), 200, 16));
        }
        if(OreDictionary.doesOreNameExist("blockEmerald")) {
            ItemStack blockStack = OreDictionary.getOres("blockEmerald").get(0);
            RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("emerald", 9), 200, 16));
        }
        if(OreDictionary.doesOreNameExist("blockGold")) {
            ItemStack blockStack = OreDictionary.getOres("blockGold").get(0);
            RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("gold", 9), 200, 16));
        }
        if(OreDictionary.doesOreNameExist("blockInvar")) {
            ItemStack blockStack = OreDictionary.getOres("blockInvar").get(0);
            RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("invar", 9), 200, 16));
        }
        if(OreDictionary.doesOreNameExist("blockIridium")) {
            ItemStack blockStack = OreDictionary.getOres("blockIridium").get(0);
            RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("iridium", 9), 200, 16));
        }
        if(OreDictionary.doesOreNameExist("blockIron")) {
            ItemStack blockStack = OreDictionary.getOres("blockIron").get(0);
            RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("iron", 9), 200, 16));
        }
        if(OreDictionary.doesOreNameExist("blockLapis")) {
            ItemStack blockStack = OreDictionary.getOres("blockLapis").get(0);
            RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("lapis", 9), 200, 16));
        }
        if(OreDictionary.doesOreNameExist("blockLead")) {
            ItemStack blockStack = OreDictionary.getOres("blockLead").get(0);
            RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("lead", 9), 200, 16));
        }
        if(OreDictionary.doesOreNameExist("blockNickel")) {
            ItemStack blockStack = OreDictionary.getOres("blockNickel").get(0);
            RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("nickel", 9), 200, 16));
        }
        if(OreDictionary.doesOreNameExist("blockOsmium")) {
            ItemStack blockStack = OreDictionary.getOres("blockOsmium").get(0);
            RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("osmium", 9), 200, 16));
        }
        if(OreDictionary.doesOreNameExist("blockPeridot")) {
            ItemStack blockStack = OreDictionary.getOres("blockPeridot").get(0);
            RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("peridot", 9), 200, 16));
        }
        if(OreDictionary.doesOreNameExist("blockPlatinum")) {
            ItemStack blockStack = OreDictionary.getOres("blockPlatinum").get(0);
            RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("platinum", 9), 200, 16));
        }
        if(OreDictionary.doesOreNameExist("blockRedGarnet")) {
            ItemStack blockStack = OreDictionary.getOres("blockRedGarnet").get(0);
            RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("redGarnet", 9), 200, 16));
        }
        if(OreDictionary.doesOreNameExist("BlockRedstone")) {
            ItemStack blockStack = OreDictionary.getOres("blockRedstone").get(0);
            RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("redstone", 9), 200, 16));
        }
        if(OreDictionary.doesOreNameExist("blockRuby")) {
            ItemStack blockStack = OreDictionary.getOres("blockRuby").get(0);
            RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("ruby", 9), 200, 16));
        }
        if(OreDictionary.doesOreNameExist("blockSapphire")) {
            ItemStack blockStack = OreDictionary.getOres("blockSapphire").get(0);
            RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("sapphire", 9), 200, 16));
        }
        if(OreDictionary.doesOreNameExist("blockSilver")) {
            ItemStack blockStack = OreDictionary.getOres("blockSilver").get(0);
            RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("silver", 9), 200, 16));
        }
        if(OreDictionary.doesOreNameExist("blockSteel")) {
            ItemStack blockStack = OreDictionary.getOres("blockSteel").get(0);
            RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("steel", 9), 200, 16));
        }
        if(OreDictionary.doesOreNameExist("blockTeslatite")) {
            ItemStack blockStack = OreDictionary.getOres("blockTeslatite").get(0);
            RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("teslatite", 9), 200, 16));
        }
        if(OreDictionary.doesOreNameExist("blockTin")) {
            ItemStack blockStack = OreDictionary.getOres("blockTin").get(0);
            RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("tin", 9), 200, 16));
        }
        if(OreDictionary.doesOreNameExist("blockTitanium")) {
            ItemStack blockStack = OreDictionary.getOres("blockTitanium").get(0);
            RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("titanium", 9), 200, 16));
        }
        if(OreDictionary.doesOreNameExist("blockTungsten")) {
            ItemStack blockStack = OreDictionary.getOres("blockTungsten").get(0);
            RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("tungsten", 9), 200, 16));
        }
        if(OreDictionary.doesOreNameExist("blockTungstensteel")) {
            ItemStack blockStack = OreDictionary.getOres("blockTungstensteel").get(0);
            RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("tungstensteel", 9), 200, 16));
        }
        if(OreDictionary.doesOreNameExist("blockYellowGarnet")) {
            ItemStack blockStack = OreDictionary.getOres("blockYellowGarnet").get(0);
            RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("yellowGarnet", 9), 200, 16));
        }
        if(OreDictionary.doesOreNameExist("blockZinc")) {
            ItemStack blockStack = OreDictionary.getOres("blockZinc").get(0);
            RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(blockStack, ItemPlates.getPlateByName("zinc", 9), 200, 16));
        }

        //Obsidian
        RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(new ItemStack(Blocks.obsidian), ItemPlates.getPlateByName("obsidian", 9), 100, 4));
    }
	
	public static void addUUrecipes() {
		if(ConfigTechReborn.UUrecipesIridiamOre)
			CraftingHelper.addShapedOreRecipe((IC2Items.getItem("iridiumOre")),
					"UUU",
					" U ",
					"UUU",
					'U', ModItems.uuMatter);

		if(ConfigTechReborn.UUrecipesWood)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Blocks.log, 8),
				" U ",
				"   ",
				"   ",
					'U', ModItems.uuMatter);

		if(ConfigTechReborn.UUrecipesStone)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Blocks.stone, 16),
				"   ",
				" U ",
				"   ",
					'U', ModItems.uuMatter);

		if(ConfigTechReborn.UUrecipesSnowBlock)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Blocks.snow, 16),
				"U U",
				"   ",
				"   ",
					'U', ModItems.uuMatter);

		if(ConfigTechReborn.UUrecipesGrass)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Blocks.grass, 16),
				"   ",
				"U  ",
				"U  ",
					'U', ModItems.uuMatter);

		if(ConfigTechReborn.UUrecipesObsidian)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Blocks.obsidian, 12),
				"U U",
				"U U",
				"   ",
					'U', ModItems.uuMatter);

		if(ConfigTechReborn.UUrecipesGlass)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Blocks.glass, 32),
				" U ",
				"U U",
				" U ",
					'U', ModItems.uuMatter);

		if(ConfigTechReborn.UUrecipesWater)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Blocks.water, 1),
				"   ",
				" U ",
				" U ",
					'U', ModItems.uuMatter);

		if(ConfigTechReborn.UUrecipesLava)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Blocks.lava, 1),
				" U ",
				" U ",
				" U ",
					'U', ModItems.uuMatter);

		if(ConfigTechReborn.UUrecipesCocoa)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Items.dye, 32, 3),
				"UU ",
				"  U",
				"UU ",
					'U', ModItems.uuMatter);

		if(ConfigTechReborn.UUrecipesGlowstoneBlock)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Blocks.glowstone, 8),
				" U ",
				"U U",
				"UUU",
					'U', ModItems.uuMatter);

		if(ConfigTechReborn.UUrecipesCactus)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Blocks.cactus, 48),
				" U ",
				"UUU",
				"U U",
					'U', ModItems.uuMatter);

		if(ConfigTechReborn.UUrecipesSugarCane)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Items.reeds, 48),
				"U U",
				"U U",
				"U U",
					'U', ModItems.uuMatter);

		if(ConfigTechReborn.UUrecipesVine)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Blocks.vine, 24),
				"U  ",
				"U  ",
				"U  ",
					'U', ModItems.uuMatter);

		if(ConfigTechReborn.UUrecipesSnowBall)
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

		if(ConfigTechReborn.UUrecipeslilypad)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Blocks.waterlily, 64),
				"U U",
				" U ",
				" U ",
					'U', ModItems.uuMatter);

		if(ConfigTechReborn.UUrecipesGunpowder)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Items.gunpowder, 15),
				"UUU",
				"U  ",
				"UUU",
					'U', ModItems.uuMatter);

		if(ConfigTechReborn.UUrecipesBone)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Items.bone, 32),
				"U  ",
				"UU ",
				"U  ",
					'U', ModItems.uuMatter);

		if(ConfigTechReborn.UUrecipesFeather)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Items.feather, 32),
				" U ",
				" U ",
				"U U",
					'U', ModItems.uuMatter);

		if(ConfigTechReborn.UUrecipesInk)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Items.dye, 48),
				" UU",
				" UU",
				" U ",
					'U', ModItems.uuMatter);

		if(ConfigTechReborn.UUrecipesEnderPearl)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Items.ender_pearl, 1),
				"UUU",
				"U U",
				" U ",
					'U', ModItems.uuMatter);

		if(ConfigTechReborn.UUrecipesCoal)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Items.coal, 5),
				"  U",
				"U  ",
				"  U",
					'U', ModItems.uuMatter);

		if(ConfigTechReborn.UUrecipesIronOre)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Blocks.iron_ore, 2),
				"U U",
				" U ",
				"U U",
					'U', ModItems.uuMatter);

		if(ConfigTechReborn.UUrecipesGoldOre)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Blocks.gold_ore, 2),
				" U ",
				"UUU",
				" U ",
					'U', ModItems.uuMatter);

		if(ConfigTechReborn.UUrecipesRedStone)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Items.redstone, 24),
				"   ",
				" U ",
				"UUU",
					'U', ModItems.uuMatter);

		if(ConfigTechReborn.UUrecipesLapis)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Items.dye, 9 , 4),
				" U ",
				" U ",
				" UU",
					'U', ModItems.uuMatter);

		if(ConfigTechReborn.UUrecipesEmeraldOre)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Blocks.emerald_ore, 1),
				"UU ",
				"U U",
				" UU",
					'U', ModItems.uuMatter);

		if(ConfigTechReborn.UUrecipesEmerald)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Items.emerald, 2),
				"UUU",
				"UUU",
				" U ",
					'U', ModItems.uuMatter);

		if(ConfigTechReborn.UUrecipesDiamond)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Items.diamond, 1),
				"UUU",
				"UUU",
				"UUU",
					'U', ModItems.uuMatter);

		if(ConfigTechReborn.UUrecipesTinDust)
			CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.dusts, 10, 77),
				"   ",
				"U U",
				"  U",
					'U', ModItems.uuMatter);

		if(ConfigTechReborn.UUrecipesCopperDust)
			CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.dusts, 10, 21),
				"  U",
				"U U",
				"   ",
					'U', ModItems.uuMatter);

		if(ConfigTechReborn.UUrecipesLeadDust)
			CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.dusts, 14, 42),
				"UUU",
				"UUU",
				"U  ",
					'U', ModItems.uuMatter);

		if(ConfigTechReborn.UUrecipesPlatinumDust)
			CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.dusts, 1, 58),
				"  U",
				"UUU",
				"UUU",
					'U', ModItems.uuMatter);

		if(ConfigTechReborn.UUrecipesTungstenDust)
			CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.dusts, 1, 79),
				"U  ",
				"UUU",
				"UUU",
					'U', ModItems.uuMatter);

		if(ConfigTechReborn.UUrecipesTitaniumDust)
			CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.dusts, 2, 78),
				"UUU",
				" U ",
				" U ",
					'U', ModItems.uuMatter);

		if(ConfigTechReborn.UUrecipesAluminumDust)
			CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.dusts, 16, 2),
				" U ",
				" U ",
				"UUU",
					'U', ModItems.uuMatter);

		
		if(ConfigTechReborn.HideUuRecipes)
			hideUUrecipes();
		
	}
	
	public static void hideUUrecipes() {
		//TODO
	}

}

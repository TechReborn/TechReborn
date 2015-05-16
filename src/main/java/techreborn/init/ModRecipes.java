package techreborn.init;

import cpw.mods.fml.common.registry.GameRegistry;
import ic2.api.item.IC2Items;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import techreborn.api.BlastFurnaceRecipe;
import techreborn.api.CentrifugeRecipie;
import techreborn.api.TechRebornAPI;
import techreborn.api.recipe.RecipeHanderer;
import techreborn.compat.nei.recipes.ChemicalReactorRecipeHandler;
import techreborn.config.ConfigTechReborn;
import techreborn.recipes.AlloySmelterRecipe;
import techreborn.recipes.AssemblingMachineRecipe;
import techreborn.recipes.ChemicalReactorRecipe;
import techreborn.recipes.GrinderRecipe;
import techreborn.recipes.ImplosionCompressorRecipe;
import techreborn.recipes.IndustrialSawmillRecipe;
import techreborn.recipes.LatheRecipe;
import techreborn.recipes.PlateCuttingMachineRecipe;
import techreborn.util.CraftingHelper;
import techreborn.util.LogHelper;

public class ModRecipes {
	public static ConfigTechReborn config;

	public static void init()
	{
		addShaplessRecipes();
		addShappedRecipes();
		addSmeltingRecipes();
		addMachineRecipes();
        addAlloySmelterRecipes();
		addUUrecipes();
	}

	public static void addShappedRecipes()
	{
		// Storage Blocks
		CraftingHelper.addShapedOreRecipe(
				new ItemStack(ModBlocks.storage, 1, 0),
				new Object[]
				{ "AAA", "AAA", "AAA", 
					'A', "ingotSilver", });

		CraftingHelper.addShapedOreRecipe(
				new ItemStack(ModBlocks.storage, 1, 1), 
				new Object[]
				{ "AAA", "AAA", "AAA", 
					'A', "ingotAluminium", });

		CraftingHelper.addShapedOreRecipe(
				new ItemStack(ModBlocks.storage, 1, 2), 
				new Object[]
				{ "AAA", "AAA", "AAA", 
					'A', "ingotTitanium", });

		CraftingHelper.addShapedOreRecipe(
				new ItemStack(ModBlocks.storage, 1, 3), 
				new Object[]
				{ "AAA", "AAA", "AAA", 
					'A', "gemSapphire", });

		CraftingHelper.addShapedOreRecipe(
				new ItemStack(ModBlocks.storage, 1, 4),
				new Object[]
				{ "AAA", "AAA", "AAA", 
					'A', "gemRuby", });

		CraftingHelper.addShapedOreRecipe(
				new ItemStack(ModBlocks.storage, 1, 5), 
				new Object[]
				{ "AAA", "AAA", "AAA", 
					'A', "gemGreenSapphire", });

		CraftingHelper.addShapedOreRecipe(
				new ItemStack(ModBlocks.storage, 1, 6), 
				new Object[]
				{ "AAA", "AAA", "AAA", 
					'A', "ingotChrome", });

		CraftingHelper.addShapedOreRecipe(
				new ItemStack(ModBlocks.storage, 1, 7), 
				new Object[]
				{ "AAA", "AAA", "AAA", 
					'A', "ingotElectrum", });

		CraftingHelper.addShapedOreRecipe(
				new ItemStack(ModBlocks.storage, 1, 8), 
				new Object[]
				{ "AAA", "AAA", "AAA", 
					'A', "ingotTungsten", });

		CraftingHelper.addShapedOreRecipe(
				new ItemStack(ModBlocks.storage, 1, 9), 
				new Object[]
				{ "AAA", "AAA", "AAA", 
					'A', "ingotLead", });

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.storage, 1, 10),
				new Object[]
				{ "AAA", "AAA", "AAA", 
			      'A', "ingotZinc", });

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.storage, 1, 11),
				new Object[]
			    { "AAA", "AAA", "AAA", 
			      'A', "ingotBrass", });

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.storage, 1, 12),
				new Object[]
				{ "AAA", "AAA", "AAA", 
				  'A', "ingotSteel", });

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.storage, 1, 13), 
				new Object[]
			    { "AAA", "AAA", "AAA", 
			      'A', "ingotPlatinum", });

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.storage, 1, 14),
				new Object[]
			    { "AAA", "AAA", "AAA", 
			      'A', "ingotNickel", });

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.storage, 1, 15), 
				new Object[]
			    { "AAA", "AAA", "AAA", 
			      'A', "ingotInvar", });

		LogHelper.info("Shapped Recipes Added");
	}

	public static void addShaplessRecipes()
	{
		CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.ingots, 9,
				4), "blockSilver");
		CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.ingots, 9,
                5), "blockAluminium");
		CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.ingots, 9,
                6), "blockTitanium");
		CraftingHelper.addShapelessOreRecipe(
                new ItemStack(ModItems.gems, 9, 1), "blockSapphire");
		CraftingHelper.addShapelessOreRecipe(
                new ItemStack(ModItems.gems, 9, 0), "blockRuby");
		CraftingHelper.addShapelessOreRecipe(
                new ItemStack(ModItems.gems, 9, 2), "blockGreenSapphire");
		CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.ingots, 9,
                7), "blockChrome");
		CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.ingots, 9,
                8), "blockElectrum");
		CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.ingots, 9,
                9), "blockTungsten");
		CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.ingots, 9,
                10), "blockLead");
		CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.ingots, 9,
                11), "blockZinc");
		CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.ingots, 9,
                12), "blockBrass");
		CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.ingots, 9,
                13), "blockSteel");
		CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.ingots, 9,
                14), "blockPlatinum");
		CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.ingots, 9,
                15), "blockNickel");
		CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.ingots, 9,
                16), "blockInvar");
		CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.rockCutter,
                1, 27), Items.apple);

		LogHelper.info("Shapless Recipes Added");
	}

	public static void addSmeltingRecipes()
	{
		GameRegistry.addSmelting(new ItemStack(ModItems.dusts, 1, 27),
                new ItemStack(Items.iron_ingot), 1F);
		GameRegistry.addSmelting(new ItemStack(ModItems.dusts, 1, 23),
                new ItemStack(Items.gold_ingot), 1F);
		GameRegistry.addSmelting(new ItemStack(ModItems.dusts, 1, 14),
                IC2Items.getItem("copperIngot"), 1F);
		GameRegistry.addSmelting(new ItemStack(ModItems.dusts, 1, 51),
                IC2Items.getItem("tinIngot"), 1F);
		GameRegistry.addSmelting(new ItemStack(ModItems.dusts, 1, 7),
                IC2Items.getItem("bronzeIngot"), 1F);
		GameRegistry.addSmelting(new ItemStack(ModItems.dusts, 1, 29),
                IC2Items.getItem("leadIngot"), 1F);
		GameRegistry.addSmelting(new ItemStack(ModItems.dusts, 1, 45),
                IC2Items.getItem("silverIngot"), 1F);

		LogHelper.info("Smelting Recipes Added");
	}

	public static void addMachineRecipes()
	{
		TechRebornAPI.registerCentrifugeRecipe(new CentrifugeRecipie(
				Items.apple, 4, Items.beef, Items.baked_potato, null, null,
				120, 4));
		TechRebornAPI.registerCentrifugeRecipe(new CentrifugeRecipie(
				Items.nether_star, 1, Items.diamond, Items.emerald, Items.bed,
				Items.cake, 500, 8));
		TechRebornAPI.addRollingMachinceRecipe(
				new ItemStack(Blocks.furnace, 4), "ccc", "c c", "ccc", 'c',
				Blocks.cobblestone);
		TechRebornAPI.registerBlastFurnaceRecipe(new BlastFurnaceRecipe(new ItemStack(Items.apple), new ItemStack(Items.ender_pearl), new ItemStack(Items.golden_apple), new ItemStack(Items.diamond), 120, 1000));

		RecipeHanderer.addRecipe(new ImplosionCompressorRecipe(new ItemStack(Blocks.end_stone, 4), IC2Items.getItem("copperIngot"), new ItemStack(Items.brewing_stand), new ItemStack(Items.carrot), 120, 5));

		RecipeHanderer.addRecipe(new AlloySmelterRecipe(new ItemStack(Items.coal), new ItemStack(Blocks.sand), new ItemStack(Items.diamond), 120, 5));
		RecipeHanderer.addRecipe(new AssemblingMachineRecipe(new ItemStack(Items.coal), new ItemStack(Blocks.sand), new ItemStack(Items.diamond), 120, 5));
		RecipeHanderer.addRecipe(new LatheRecipe(new ItemStack(Items.coal), new ItemStack(Items.diamond), 120, 5));
		RecipeHanderer.addRecipe(new IndustrialSawmillRecipe(new ItemStack(Items.coal), new ItemStack(Blocks.sand), new ItemStack(Items.diamond), new ItemStack(Items.diamond), new ItemStack(Items.diamond), 120, 5));
		RecipeHanderer.addRecipe(new PlateCuttingMachineRecipe(new ItemStack(Items.coal), new ItemStack(Items.diamond), 120, 5));
		//TODO
		RecipeHanderer.addRecipe(new ChemicalReactorRecipe(new ItemStack(Items.coal), new ItemStack(Blocks.sand), new ItemStack(Items.diamond), 120, 5));

		RecipeHanderer.addRecipe(new GrinderRecipe(new ItemStack(Items.diamond), new FluidStack(ModFluids.fluidMercury, 500), new ItemStack(Blocks.brick_block), null, null, null, 400, 5));

		LogHelper.info("Machine Recipes Added");
	}

	public static void addAlloySmelterRecipes(){
        //Bronze
        RecipeHanderer.addRecipe(new AlloySmelterRecipe(new ItemStack(ModItems.ingots, 3, 9), new ItemStack(ModItems.ingots, 1, 26), new ItemStack(ModItems.ingots, 4, 6), 200, 16));
        RecipeHanderer.addRecipe(new AlloySmelterRecipe(new ItemStack(ModItems.ingots, 3, 9), new ItemStack(ModItems.dusts, 1, 77), new ItemStack(ModItems.ingots, 4, 6), 200, 16));
        RecipeHanderer.addRecipe(new AlloySmelterRecipe(new ItemStack(ModItems.dusts, 3, 21), new ItemStack(ModItems.ingots, 1, 26), new ItemStack(ModItems.ingots, 4, 6), 200, 16));
        RecipeHanderer.addRecipe(new AlloySmelterRecipe(new ItemStack(ModItems.dusts, 3, 21), new ItemStack(ModItems.dusts, 1, 77), new ItemStack(ModItems.ingots, 4, 6), 200, 16));

        //Electrum
        RecipeHanderer.addRecipe(new AlloySmelterRecipe(new ItemStack(Items.gold_ingot, 1), new ItemStack(ModItems.ingots, 1, 23), new ItemStack(ModItems.ingots, 2, 11), 200, 16));
        RecipeHanderer.addRecipe(new AlloySmelterRecipe(new ItemStack(Items.gold_ingot, 1), new ItemStack(ModItems.dusts, 1, 68), new ItemStack(ModItems.ingots, 2, 11), 200, 16));
        RecipeHanderer.addRecipe(new AlloySmelterRecipe(new ItemStack(ModItems.dusts, 3, 32), new ItemStack(ModItems.ingots, 1, 26), new ItemStack(ModItems.ingots, 2, 11), 200, 16));
        RecipeHanderer.addRecipe(new AlloySmelterRecipe(new ItemStack(ModItems.dusts, 3, 32), new ItemStack(ModItems.dusts, 1, 68), new ItemStack(ModItems.ingots, 2, 11), 200, 16));

        //Invar
        RecipeHanderer.addRecipe(new AlloySmelterRecipe(new ItemStack(Items.iron_ingot, 2), new ItemStack(ModItems.ingots, 1, 20), new ItemStack(ModItems.ingots, 3, 13), 200, 16));
        RecipeHanderer.addRecipe(new AlloySmelterRecipe(new ItemStack(Items.iron_ingot, 2), new ItemStack(ModItems.dusts, 1, 53), new ItemStack(ModItems.ingots, 3, 13), 200, 16));
        RecipeHanderer.addRecipe(new AlloySmelterRecipe(new ItemStack(ModItems.dusts, 2, 38), new ItemStack(ModItems.ingots, 1, 20), new ItemStack(ModItems.ingots, 3, 13), 200, 16));
        RecipeHanderer.addRecipe(new AlloySmelterRecipe(new ItemStack(ModItems.dusts, 2, 38), new ItemStack(ModItems.dusts, 1, 53), new ItemStack(ModItems.ingots, 3, 13), 200, 16));

        //Cupronickel
        RecipeHanderer.addRecipe(new AlloySmelterRecipe(new ItemStack(ModItems.ingots, 1, 9), new ItemStack(ModItems.ingots, 1, 20), new ItemStack(ModItems.ingots, 2, 10), 200, 16));
        RecipeHanderer.addRecipe(new AlloySmelterRecipe(new ItemStack(ModItems.ingots, 1, 9), new ItemStack(ModItems.dusts, 1, 53), new ItemStack(ModItems.ingots, 2, 10), 200, 16));
        RecipeHanderer.addRecipe(new AlloySmelterRecipe(new ItemStack(ModItems.dusts, 1, 21), new ItemStack(ModItems.ingots, 1, 20), new ItemStack(ModItems.ingots, 2, 10), 200, 16));
        RecipeHanderer.addRecipe(new AlloySmelterRecipe(new ItemStack(ModItems.dusts, 1, 21), new ItemStack(ModItems.dusts, 1, 53), new ItemStack(ModItems.ingots, 2, 10), 200, 16));

        //Nichrome
        RecipeHanderer.addRecipe(new AlloySmelterRecipe(new ItemStack(ModItems.ingots, 1, 8), new ItemStack(ModItems.ingots, 4, 20), new ItemStack(ModItems.ingots, 5, 19), 200, 16));
        RecipeHanderer.addRecipe(new AlloySmelterRecipe(new ItemStack(ModItems.ingots, 1, 8), new ItemStack(ModItems.dusts, 4, 53), new ItemStack(ModItems.ingots, 5, 19), 200, 16));
        RecipeHanderer.addRecipe(new AlloySmelterRecipe(new ItemStack(ModItems.dusts, 1, 16), new ItemStack(ModItems.ingots, 4, 20), new ItemStack(ModItems.ingots, 5, 19), 200, 16));
        RecipeHanderer.addRecipe(new AlloySmelterRecipe(new ItemStack(ModItems.dusts, 1, 16), new ItemStack(ModItems.dusts, 4, 53), new ItemStack(ModItems.ingots, 5, 19), 200, 16));

        //Magnalium
        RecipeHanderer.addRecipe(new AlloySmelterRecipe(new ItemStack(ModItems.dusts, 1, 45), new ItemStack(ModItems.ingots, 4, 0), new ItemStack(ModItems.ingots, 3, 18), 200, 16));
        RecipeHanderer.addRecipe(new AlloySmelterRecipe(new ItemStack(ModItems.dusts, 1, 45), new ItemStack(ModItems.dusts, 4, 2), new ItemStack(ModItems.ingots, 3, 18), 200, 16));

        //Battery Alloy
        RecipeHanderer.addRecipe(new AlloySmelterRecipe(new ItemStack(ModItems.ingots, 4, 16), new ItemStack(ModItems.ingots, 1, 1), new ItemStack(ModItems.ingots, 5, 2), 200, 16));
        RecipeHanderer.addRecipe(new AlloySmelterRecipe(new ItemStack(ModItems.ingots, 4, 16), new ItemStack(ModItems.dusts, 1, 5), new ItemStack(ModItems.ingots, 5, 2), 200, 16));
        RecipeHanderer.addRecipe(new AlloySmelterRecipe(new ItemStack(ModItems.dusts, 4, 42), new ItemStack(ModItems.ingots, 1, 1), new ItemStack(ModItems.ingots, 5, 2), 200, 16));
        RecipeHanderer.addRecipe(new AlloySmelterRecipe(new ItemStack(ModItems.dusts, 4, 42), new ItemStack(ModItems.dusts, 1, 5), new ItemStack(ModItems.ingots, 5, 2), 200, 16));

        //Brass
        if(OreDictionary.doesOreNameExist("ingotBrass")) {
            ItemStack brassStack = OreDictionary.getOres("ingotBrass").get(0);
            brassStack.stackSize = 4;
            RecipeHanderer.addRecipe(new AlloySmelterRecipe(new ItemStack(ModItems.ingots, 3, 9), new ItemStack(ModItems.ingots, 1, 31), brassStack, 200, 16));
            RecipeHanderer.addRecipe(new AlloySmelterRecipe(new ItemStack(ModItems.ingots, 3, 9), new ItemStack(ModItems.dusts, 1, 84), brassStack, 200, 16));
            RecipeHanderer.addRecipe(new AlloySmelterRecipe(new ItemStack(ModItems.dusts, 3, 21), new ItemStack(ModItems.ingots, 1, 31), brassStack, 200, 16));
            RecipeHanderer.addRecipe(new AlloySmelterRecipe(new ItemStack(ModItems.dusts, 3, 21), new ItemStack(ModItems.dusts, 1, 84), brassStack, 200, 16));
        }

        //Red Alloy
        if(OreDictionary.doesOreNameExist("ingotRedAlloy")) {
            ItemStack redAlloyStack = OreDictionary.getOres("ingotRedAlloy").get(0);
            redAlloyStack.stackSize = 1;
            RecipeHanderer.addRecipe(new AlloySmelterRecipe(new ItemStack(Items.redstone, 4), new ItemStack(ModItems.ingots, 1, 9), redAlloyStack, 200, 16));
            RecipeHanderer.addRecipe(new AlloySmelterRecipe(new ItemStack(Items.redstone, 4), new ItemStack(Items.iron_ingot, 1), redAlloyStack, 200, 16));
        }

        //Blue Alloy
        if(OreDictionary.doesOreNameExist("ingotBlueAlloy")) {
            ItemStack blueAlloyStack = OreDictionary.getOres("ingotBlueAlloy").get(0);
            blueAlloyStack.stackSize = 1;
            RecipeHanderer.addRecipe(new AlloySmelterRecipe(new ItemStack(ModItems.dusts, 4, 75), new ItemStack(ModItems.ingots, 1, 23), blueAlloyStack, 200, 16));
        }

        //Blue Alloy
        if(OreDictionary.doesOreNameExist("ingotPurpleAlloy") && OreDictionary.doesOreNameExist("dustInfusedTeslatite")) {
            ItemStack purpleAlloyStack = OreDictionary.getOres("ingotPurpleAlloy").get(0);
            purpleAlloyStack.stackSize = 1;
            ItemStack infusedTeslatiteStack = OreDictionary.getOres("ingotPurpleAlloy").get(0);
            infusedTeslatiteStack.stackSize = 8;
            RecipeHanderer.addRecipe(new AlloySmelterRecipe(new ItemStack(ModItems.ingots, 1, 3), new ItemStack(ModItems.ingots, 1, 4), purpleAlloyStack, 200, 16));
            RecipeHanderer.addRecipe(new AlloySmelterRecipe(new ItemStack(Items.gold_ingot, 1), infusedTeslatiteStack, purpleAlloyStack, 200, 16));
        }

        //Aluminum Brass
        if(OreDictionary.doesOreNameExist("ingotAluminumBrass")) {
            ItemStack aluminumBrassStack = OreDictionary.getOres("ingotAluminumBrass").get(0);
            aluminumBrassStack.stackSize = 4;
            RecipeHanderer.addRecipe(new AlloySmelterRecipe(new ItemStack(ModItems.ingots, 3, 9), new ItemStack(ModItems.ingots, 1, 0), aluminumBrassStack, 200, 16));
            RecipeHanderer.addRecipe(new AlloySmelterRecipe(new ItemStack(ModItems.ingots, 3, 9), new ItemStack(ModItems.dusts, 1, 2), aluminumBrassStack, 200, 16));
            RecipeHanderer.addRecipe(new AlloySmelterRecipe(new ItemStack(ModItems.dusts, 3, 21), new ItemStack(ModItems.ingots, 1, 0), aluminumBrassStack, 200, 16));
            RecipeHanderer.addRecipe(new AlloySmelterRecipe(new ItemStack(ModItems.dusts, 3, 21), new ItemStack(ModItems.dusts, 1, 2), aluminumBrassStack, 200, 16));
        }

        //Manyullyn
        if(OreDictionary.doesOreNameExist("ingotManyullyn") && OreDictionary.doesOreNameExist("ingotCobalt") && OreDictionary.doesOreNameExist("ingotArdite")) {
            ItemStack manyullynStack = OreDictionary.getOres("ingotManyullyn").get(0);
            manyullynStack.stackSize = 1;
            ItemStack cobaltStack = OreDictionary.getOres("ingotCobalt").get(0);
            cobaltStack.stackSize = 1;
            ItemStack arditeStack = OreDictionary.getOres("ingotArdite").get(0);
            arditeStack.stackSize = 1;
            RecipeHanderer.addRecipe(new AlloySmelterRecipe(cobaltStack, arditeStack, manyullynStack, 200, 16));
            RecipeHanderer.addRecipe(new AlloySmelterRecipe(cobaltStack, new ItemStack(ModItems.dusts, 1, 6), manyullynStack, 200, 16));
            RecipeHanderer.addRecipe(new AlloySmelterRecipe(new ItemStack(ModItems.dusts, 1, 20), arditeStack, manyullynStack, 200, 16));
            RecipeHanderer.addRecipe(new AlloySmelterRecipe(new ItemStack(ModItems.dusts, 1, 20), new ItemStack(ModItems.dusts, 1, 6), manyullynStack, 200, 16));
        }

        //Conductive Iron
        if(OreDictionary.doesOreNameExist("ingotConductiveIron")) {
            ItemStack conductiveIronStack = OreDictionary.getOres("ingotConductiveIron").get(0);
            conductiveIronStack.stackSize = 1;
            RecipeHanderer.addRecipe(new AlloySmelterRecipe(new ItemStack(Items.redstone, 1), new ItemStack(Items.iron_ingot, 1), conductiveIronStack, 200, 16));
        }

        //Redstone Alloy
        if(OreDictionary.doesOreNameExist("ingotRedstoneAlloy") && OreDictionary.doesOreNameExist("itemSilicon")) {
            ItemStack redstoneAlloyStack = OreDictionary.getOres("ingotRedstoneAlloy").get(0);
            redstoneAlloyStack.stackSize = 1;
            ItemStack siliconStack = OreDictionary.getOres("itemSilicon").get(0);
            siliconStack.stackSize = 1;
            RecipeHanderer.addRecipe(new AlloySmelterRecipe(new ItemStack(Items.redstone, 1), siliconStack, redstoneAlloyStack, 200, 16));
        }

        //Pulsating Iron
        if(OreDictionary.doesOreNameExist("ingotPhasedIron")) {
            ItemStack pulsatingIronStack = OreDictionary.getOres("ingotPhasedIron").get(0);
            pulsatingIronStack.stackSize = 1;
            RecipeHanderer.addRecipe(new AlloySmelterRecipe(new ItemStack(Items.iron_ingot, 1), new ItemStack(Items.ender_pearl, 1), pulsatingIronStack, 200, 16));
            RecipeHanderer.addRecipe(new AlloySmelterRecipe(new ItemStack(Items.iron_ingot, 1), new ItemStack(ModItems.dusts, 1, 29), pulsatingIronStack, 200, 16));
        }

        //Vibrant Alloy
        if(OreDictionary.doesOreNameExist("ingotEnergeticAlloy") && OreDictionary.doesOreNameExist("ingotPhasedGold")) {
            ItemStack energeticAlloyStack = OreDictionary.getOres("ingotEnergeticAlloy").get(0);
            energeticAlloyStack.stackSize = 1;
            ItemStack vibrantAlloyStack = OreDictionary.getOres("ingotPhasedGold").get(0);
            vibrantAlloyStack.stackSize = 1;
            RecipeHanderer.addRecipe(new AlloySmelterRecipe(energeticAlloyStack, new ItemStack(Items.ender_pearl, 1), vibrantAlloyStack, 200, 16));
            RecipeHanderer.addRecipe(new AlloySmelterRecipe(energeticAlloyStack, new ItemStack(ModItems.dusts, 1, 29), vibrantAlloyStack, 200, 16));
        }

        //Soularium
        if(OreDictionary.doesOreNameExist("ingotSoularium")) {
            ItemStack soulariumStack = OreDictionary.getOres("ingotSoularium").get(0);
            soulariumStack.stackSize = 1;
            RecipeHanderer.addRecipe(new AlloySmelterRecipe(new ItemStack(Blocks.soul_sand, 1), new ItemStack(Items.gold_ingot, 1), soulariumStack, 200, 16));
        }

    }
	
	public static void addUUrecipes()
	{
		if(config.UUrecipesIridiamOre);
		CraftingHelper.addShapedOreRecipe((IC2Items.getItem("iridiumOre")),
			new Object[]
			{ 
				"UUU", 
				" U ", 
				"UUU", 
				'U', ModItems.uuMatter 
			});
		if(config.UUrecipesWood);
		CraftingHelper.addShapedOreRecipe(new ItemStack(Blocks.log, 8),
				new Object[]
				{ 
				 	" U ", 
				 	"   ", 
				 	"   ", 
					'U', ModItems.uuMatter 
				});
		if(config.UUrecipesStone);
		CraftingHelper.addShapedOreRecipe(new ItemStack(Blocks.stone, 16),
				new Object[]
				{ 
				 	"   ", 
				 	" U ", 
				 	"   ", 
					'U', ModItems.uuMatter 
				});
		if(config.UUrecipesSnowBlock);
		CraftingHelper.addShapedOreRecipe(new ItemStack(Blocks.snow, 16),
				new Object[]
				{ 
				 	"U U", 
				 	"   ", 
				 	"   ", 
					'U', ModItems.uuMatter 
				});
		if(config.UUrecipesGrass);
		CraftingHelper.addShapedOreRecipe(new ItemStack(Blocks.grass, 16),
				new Object[]
				{ 
				 	"   ", 
				 	"U  ", 
				 	"U  ", 
					'U', ModItems.uuMatter 
				});
		if(config.UUrecipesObsidian);
		CraftingHelper.addShapedOreRecipe(new ItemStack(Blocks.obsidian, 12),
				new Object[]
				{ 
				 	"U U",
				 	"U U", 
				 	"   ", 
					'U', ModItems.uuMatter 
				});
		if(config.UUrecipesGlass);
		CraftingHelper.addShapedOreRecipe(new ItemStack(Blocks.glass, 32),
				new Object[]
				{ 
				 	" U ",
				 	"U U", 
				 	" U ", 
					'U', ModItems.uuMatter 
				});
		if(config.UUrecipesWater);
		CraftingHelper.addShapedOreRecipe(new ItemStack(Blocks.water, 1),
				new Object[]
				{ 
				 	"   ",
				 	" U ", 
				 	" U ", 
					'U', ModItems.uuMatter 
				});
		if(config.UUrecipesLava);
		CraftingHelper.addShapedOreRecipe(new ItemStack(Blocks.lava, 1),
				new Object[]
				{ 
				 	" U ",
				 	" U ", 
				 	" U ", 
					'U', ModItems.uuMatter 
				});
		if(config.UUrecipesCocoa);
		CraftingHelper.addShapedOreRecipe(new ItemStack(Items.dye, 32, 3),
				new Object[]
				{ 
				 	"UU ",
				 	"  U", 
				 	"UU ", 
					'U', ModItems.uuMatter 
				});
		if(config.UUrecipesGlowstoneBlock);
		CraftingHelper.addShapedOreRecipe(new ItemStack(Blocks.glowstone, 8),
				new Object[]
				{ 
				 	" U ",
				 	"U U", 
				 	"UUU", 
					'U', ModItems.uuMatter 
				});
		if(config.UUrecipesCactus);
		CraftingHelper.addShapedOreRecipe(new ItemStack(Blocks.cactus, 48),
				new Object[]
				{ 
				 	" U ",
				 	"UUU", 
				 	"U U", 
					'U', ModItems.uuMatter 
				});
		if(config.UUrecipesSugarCane);
		CraftingHelper.addShapedOreRecipe(new ItemStack(Items.reeds, 48),
				new Object[]
				{ 
				 	"U U",
				 	"U U", 
				 	"U U", 
					'U', ModItems.uuMatter 
				});
		if(config.UUrecipesVine);
		CraftingHelper.addShapedOreRecipe(new ItemStack(Blocks.vine, 24),
				new Object[]
				{ 
				 	"U  ",
				 	"U  ", 
				 	"U  ", 
					'U', ModItems.uuMatter 
				});
		if(config.UUrecipesSnowBall);
		CraftingHelper.addShapedOreRecipe(new ItemStack(Items.snowball, 16),
				new Object[]
				{ 
				 	"   ",
				 	"   ", 
				 	"UUU", 
					'U', ModItems.uuMatter 
				});
		
		CraftingHelper.addShapedOreRecipe(new ItemStack(Items.clay_ball, 48),
				new Object[]
				{ 
				 	"UU ",
				 	"U  ", 
				 	"UU ", 
					'U', ModItems.uuMatter 
				});
		
		if(config.UUrecipeslilypad);
		CraftingHelper.addShapedOreRecipe(new ItemStack(Blocks.waterlily, 64),
				new Object[]
				{ 
				 	"U U",
				 	" U ", 
				 	" U ", 
					'U', ModItems.uuMatter 
				});
		
		if(config.UUrecipesGunpowder);
		CraftingHelper.addShapedOreRecipe(new ItemStack(Items.gunpowder, 15),
				new Object[]
				{ 
				 	"UUU",
				 	"U  ", 
				 	"UUU", 
					'U', ModItems.uuMatter 
				});
		
		if(config.UUrecipesBone);
		CraftingHelper.addShapedOreRecipe(new ItemStack(Items.bone, 32),
				new Object[]
				{ 
				 	"U  ",
				 	"UU ", 
				 	"U  ", 
					'U', ModItems.uuMatter 
				});
		
		if(config.UUrecipesFeather);
		CraftingHelper.addShapedOreRecipe(new ItemStack(Items.feather, 32),
				new Object[]
				{ 
				 	" U ",
				 	" U ", 
				 	"U U", 
					'U', ModItems.uuMatter 
				});
		
		if(config.UUrecipesInk);
		CraftingHelper.addShapedOreRecipe(new ItemStack(Items.dye, 48),
				new Object[]
				{ 
				 	" UU",
				 	" UU", 
				 	" U ", 
					'U', ModItems.uuMatter 
				});
		
		if(config.UUrecipesEnderPearl);
		CraftingHelper.addShapedOreRecipe(new ItemStack(Items.ender_pearl, 1),
				new Object[]
				{ 
				 	"UUU",
				 	"U U", 
				 	" U ", 
					'U', ModItems.uuMatter 
				});
		
		if(config.UUrecipesCoal);
		CraftingHelper.addShapedOreRecipe(new ItemStack(Items.coal, 5),
				new Object[]
				{ 
				 	"  U",
				 	"U  ", 
				 	"  U", 
					'U', ModItems.uuMatter 
				});
		
		if(config.UUrecipesIronOre);
		CraftingHelper.addShapedOreRecipe(new ItemStack(Blocks.iron_ore, 2),
				new Object[]
				{ 
				 	"U U",
				 	" U ", 
				 	"U U", 
					'U', ModItems.uuMatter 
				});
		
		if(config.UUrecipesGoldOre);
		CraftingHelper.addShapedOreRecipe(new ItemStack(Blocks.gold_ore, 2),
				new Object[]
				{ 
				 	" U ",
				 	"UUU", 
				 	" U ", 
					'U', ModItems.uuMatter 
				});
		
		if(config.UUrecipesRedStone);
		CraftingHelper.addShapedOreRecipe(new ItemStack(Items.redstone, 24),
				new Object[]
				{ 
				 	"   ",
				 	" U ", 
				 	"UUU", 
					'U', ModItems.uuMatter 
				});
		
		if(config.UUrecipesLapis);
		CraftingHelper.addShapedOreRecipe(new ItemStack(Items.dye, 9 , 4),
				new Object[]
				{ 
				 	" U ",
				 	" U ", 
				 	" UU", 
					'U', ModItems.uuMatter 
				});
		
		if(config.UUrecipesEmeraldOre);
		CraftingHelper.addShapedOreRecipe(new ItemStack(Blocks.emerald_ore, 1),
				new Object[]
				{ 
				 	"UU ",
				 	"U U", 
				 	" UU", 
					'U', ModItems.uuMatter 
				});
		if(config.UUrecipesEmerald);
		CraftingHelper.addShapedOreRecipe(new ItemStack(Items.emerald, 2),
				new Object[]
				{ 
				 	"UUU",
				 	"UUU", 
				 	" U ", 
					'U', ModItems.uuMatter 
				});
		if(config.UUrecipesDiamond);
		CraftingHelper.addShapedOreRecipe(new ItemStack(Items.diamond, 1),
				new Object[]
				{ 
				 	"UUU",
				 	"UUU", 
				 	"UUU", 
					'U', ModItems.uuMatter 
				});
		
		if(config.UUrecipesTinDust);
		CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.dusts, 10, 77),
				new Object[]
				{ 
				 	"   ",
				 	"U U", 
				 	"  U", 
					'U', ModItems.uuMatter 
				});
		
		if(config.UUrecipesCopperDust);
		CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.dusts, 10, 21),
				new Object[]
				{ 
				 	"  U",
				 	"U U", 
				 	"   ", 
					'U', ModItems.uuMatter 
				});
		
		if(config.UUrecipesLeadDust);
		CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.dusts, 14, 42),
				new Object[]
				{ 
				 	"UUU",
				 	"UUU", 
				 	"U  ", 
					'U', ModItems.uuMatter 
				});
		
		if(config.UUrecipesPlatinumDust);
		CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.dusts, 1, 58),
				new Object[]
				{ 
				 	"  U",
				 	"UUU", 
				 	"UUU", 
					'U', ModItems.uuMatter 
				});
		
		if(config.UUrecipesTungstenDust);
		CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.dusts, 1, 79),
				new Object[]
				{ 
				 	"U  ",
				 	"UUU", 
				 	"UUU", 
					'U', ModItems.uuMatter 
				});
		
		if(config.UUrecipesTitaniumDust);
		CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.dusts, 2, 78),
				new Object[]
				{ 
				 	"UUU",
				 	" U ", 
				 	" U ", 
					'U', ModItems.uuMatter 
				});
		
		if(config.UUrecipesAluminumDust);
		CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.dusts, 16, 2),
				new Object[]
				{ 
				 	" U ",
				 	" U ", 
				 	"UUU", 
					'U', ModItems.uuMatter 
				});

		
		if(config.HideUuRecipes);
			hideUUrecipes();
		
	}
	
	public static void hideUUrecipes()
	{
		//TODO
	}

}

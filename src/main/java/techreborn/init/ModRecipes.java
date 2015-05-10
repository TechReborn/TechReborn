package techreborn.init;

import cpw.mods.fml.common.registry.GameRegistry;
import ic2.api.item.IC2Items;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import techreborn.api.BlastFurnaceRecipe;
import techreborn.api.CentrifugeRecipie;
import techreborn.api.TechRebornAPI;
import techreborn.api.recipe.RecipeHanderer;
import techreborn.config.ConfigTechReborn;
import techreborn.recipes.AlloySmelterRecipe;
import techreborn.recipes.AssemblingMachineRecipe;
import techreborn.recipes.ImplosionCompressorRecipe;
import techreborn.recipes.LatheRecipe;
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
		//TODO BORKEN 
//		RecipeHanderer.addRecipe(new LatheRecipe(new ItemStack(Items.coal), new ItemStack(Items.diamond), 120, 5));

		LogHelper.info("Machine Recipes Added");
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
		
		if(config.HideUuRecipes);
			hideUUrecipes();
		
	}
	
	public static void hideUUrecipes()
	{
		//TODO
	}

}

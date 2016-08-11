package techreborn.init;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.ArrayUtils;
import reborncore.api.recipe.RecipeHandler;
import reborncore.common.util.CraftingHelper;
import reborncore.common.util.OreUtil;
import reborncore.common.util.StringUtils;
import techreborn.Core;
import techreborn.api.ScrapboxList;
import techreborn.api.TechRebornAPI;
import techreborn.api.reactor.FusionReactorRecipe;
import techreborn.api.reactor.FusionReactorRecipeHelper;
import techreborn.api.recipe.RecyclerRecipe;
import techreborn.api.recipe.ScrapboxRecipe;
import techreborn.api.recipe.machines.*;
import techreborn.blocks.*;
import techreborn.config.ConfigTechReborn;
import techreborn.items.*;
import techreborn.parts.powerCables.ItemStandaloneCables;
import static techreborn.utils.OreDictUtils.*;
import techreborn.utils.RecipeUtils;
import techreborn.utils.StackWIPHandler;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ModRecipes
{
	public static ConfigTechReborn config;
	public static ItemStack batteryStack = new ItemStack(ModItems.reBattery, 1, OreDictionary.WILDCARD_VALUE);
	public static ItemStack crystalStack = new ItemStack(ModItems.energyCrystal, 1, OreDictionary.WILDCARD_VALUE);
	public static ItemStack lapcrystalStack = new ItemStack(ModItems.lapotronCrystal, 1, OreDictionary.WILDCARD_VALUE);
	public static ItemStack dyes = new ItemStack(Items.DYE, 1, OreDictionary.WILDCARD_VALUE);

	public static Item diamondDrill = ModItems.diamondDrill;
	public static ItemStack diamondDrillStack = new ItemStack(diamondDrill, 1, OreDictionary.WILDCARD_VALUE);

	public static Item ironChainsaw = ModItems.ironChainsaw;
	public static ItemStack ironChainsawStack = new ItemStack(ironChainsaw, 1, OreDictionary.WILDCARD_VALUE);

	public static Item diamondChainsaw = ModItems.diamondChainsaw;
	public static ItemStack diamondChainsawStack = new ItemStack(diamondChainsaw, 1, OreDictionary.WILDCARD_VALUE);

	public static Item steelJackhammer = ModItems.steelJackhammer;
	public static ItemStack steelJackhammerStack = new ItemStack(steelJackhammer, 1, OreDictionary.WILDCARD_VALUE);

	public static Item diamondJackhammer = ModItems.diamondJackhammer;
	public static ItemStack diamondJackhammerStack = new ItemStack(diamondJackhammer, 1, OreDictionary.WILDCARD_VALUE);

	public static void init()
	{
		addShapelessRecipes();
		addGeneralShapedRecipes();
		addMachineRecipes();

		addSmeltingRecipes();
		addUUrecipes();

		addAlloySmelterRecipes();
		addPlateCuttingMachineRecipes();
		addIndustrialCentrifugeRecipes();
		addChemicalReactorRecipes();
		addIndustrialElectrolyzerRecipes();

		addBlastFurnaceRecipes();
		addIndustrialGrinderRecipes();
		addImplosionCompressorRecipes();
        addVacuumFreezerRecipes();

		addReactorRecipes();
		addIc2Recipes();
		addGrinderRecipes();
		// addHammerRecipes();
		addIc2ReplacementReicpes();
		addExtractorRecipes();
		addCompressorRecipes();
		addWireRecipes();
		addScrapBoxloot();
	}

	static void addScrapBoxloot()
	{
		ScrapboxList.addItemStackToList(new ItemStack(Items.DIAMOND));
		ScrapboxList.addItemStackToList(new ItemStack(Items.STICK));
		ScrapboxList.addItemStackToList(new ItemStack(Items.COAL));
		ScrapboxList.addItemStackToList(new ItemStack(Items.APPLE));
		ScrapboxList.addItemStackToList(new ItemStack(Items.BAKED_POTATO));
		ScrapboxList.addItemStackToList(new ItemStack(Items.BLAZE_POWDER));
		ScrapboxList.addItemStackToList(new ItemStack(Items.WHEAT));
		ScrapboxList.addItemStackToList(new ItemStack(Items.CARROT));
		ScrapboxList.addItemStackToList(new ItemStack(Items.BOAT));
		ScrapboxList.addItemStackToList(new ItemStack(Items.ACACIA_BOAT));
		ScrapboxList.addItemStackToList(new ItemStack(Items.BIRCH_BOAT));
		ScrapboxList.addItemStackToList(new ItemStack(Items.DARK_OAK_BOAT));
		ScrapboxList.addItemStackToList(new ItemStack(Items.JUNGLE_BOAT));
		ScrapboxList.addItemStackToList(new ItemStack(Items.SPRUCE_BOAT));
		ScrapboxList.addItemStackToList(new ItemStack(Items.BLAZE_ROD));
		ScrapboxList.addItemStackToList(new ItemStack(Items.COMPASS));
		ScrapboxList.addItemStackToList(new ItemStack(Items.MAP));
		ScrapboxList.addItemStackToList(new ItemStack(Items.LEATHER_LEGGINGS));
		ScrapboxList.addItemStackToList(new ItemStack(Items.BOW));
		ScrapboxList.addItemStackToList(new ItemStack(Items.COOKED_CHICKEN));
		ScrapboxList.addItemStackToList(new ItemStack(Items.CAKE));
		ScrapboxList.addItemStackToList(new ItemStack(Items.ACACIA_DOOR));
		ScrapboxList.addItemStackToList(new ItemStack(Items.DARK_OAK_DOOR));
		ScrapboxList.addItemStackToList(new ItemStack(Items.BIRCH_DOOR));
		ScrapboxList.addItemStackToList(new ItemStack(Items.JUNGLE_DOOR));
		ScrapboxList.addItemStackToList(new ItemStack(Items.OAK_DOOR));
		ScrapboxList.addItemStackToList(new ItemStack(Items.SPRUCE_DOOR));
		ScrapboxList.addItemStackToList(new ItemStack(Items.WOODEN_AXE));
		ScrapboxList.addItemStackToList(new ItemStack(Items.WOODEN_HOE));
		ScrapboxList.addItemStackToList(new ItemStack(Items.WOODEN_PICKAXE));
		ScrapboxList.addItemStackToList(new ItemStack(Items.WOODEN_SHOVEL));
		ScrapboxList.addItemStackToList(new ItemStack(Items.WOODEN_SWORD));
		ScrapboxList.addItemStackToList(new ItemStack(Items.BED));
		ScrapboxList.addItemStackToList(new ItemStack(Items.SKULL, 1, 0));
		ScrapboxList.addItemStackToList(new ItemStack(Items.SKULL, 1, 2));
		ScrapboxList.addItemStackToList(new ItemStack(Items.SKULL, 1, 4));
		for (int i = 0; i < StackWIPHandler.devHeads.size(); i++)
			ScrapboxList.addItemStackToList(StackWIPHandler.devHeads.get(i));
		ScrapboxList.addItemStackToList(new ItemStack(Items.DYE, 1, 3));
		ScrapboxList.addItemStackToList(new ItemStack(Items.GLOWSTONE_DUST));
		ScrapboxList.addItemStackToList(new ItemStack(Items.STRING));
		ScrapboxList.addItemStackToList(new ItemStack(Items.MINECART));
		ScrapboxList.addItemStackToList(new ItemStack(Items.CHEST_MINECART));
		ScrapboxList.addItemStackToList(new ItemStack(Items.HOPPER_MINECART));
		ScrapboxList.addItemStackToList(new ItemStack(Items.PRISMARINE_SHARD));
		ScrapboxList.addItemStackToList(new ItemStack(Items.SHEARS));
		ScrapboxList.addItemStackToList(new ItemStack(Items.EXPERIENCE_BOTTLE));
		ScrapboxList.addItemStackToList(new ItemStack(Items.BONE));
		ScrapboxList.addItemStackToList(new ItemStack(Items.BOWL));
		ScrapboxList.addItemStackToList(new ItemStack(Items.BRICK));
		ScrapboxList.addItemStackToList(new ItemStack(Items.FISHING_ROD));
		ScrapboxList.addItemStackToList(new ItemStack(Items.BOOK));
		ScrapboxList.addItemStackToList(new ItemStack(Items.PAPER));
		ScrapboxList.addItemStackToList(new ItemStack(Items.SUGAR));
		ScrapboxList.addItemStackToList(new ItemStack(Items.REEDS));
		ScrapboxList.addItemStackToList(new ItemStack(Items.SPIDER_EYE));
		ScrapboxList.addItemStackToList(new ItemStack(Items.SLIME_BALL));
		ScrapboxList.addItemStackToList(new ItemStack(Items.ROTTEN_FLESH));
		ScrapboxList.addItemStackToList(new ItemStack(Items.SIGN));
		ScrapboxList.addItemStackToList(new ItemStack(Items.WRITABLE_BOOK));
		ScrapboxList.addItemStackToList(new ItemStack(Items.COOKED_BEEF));
		ScrapboxList.addItemStackToList(new ItemStack(Items.NAME_TAG));
		ScrapboxList.addItemStackToList(new ItemStack(Items.SADDLE));
		ScrapboxList.addItemStackToList(new ItemStack(Items.REDSTONE));
		ScrapboxList.addItemStackToList(new ItemStack(Items.GUNPOWDER));
		ScrapboxList.addItemStackToList(new ItemStack(Items.RABBIT_HIDE));
		ScrapboxList.addItemStackToList(new ItemStack(Items.RABBIT_FOOT));
		ScrapboxList.addItemStackToList(new ItemStack(Items.APPLE));
		ScrapboxList.addItemStackToList(new ItemStack(Items.GOLDEN_APPLE));
		ScrapboxList.addItemStackToList(new ItemStack(Items.GOLD_NUGGET));

		ScrapboxList.addItemStackToList(ItemCells.getCellByName("empty"));
		ScrapboxList.addItemStackToList(ItemCells.getCellByName("water"));
		ScrapboxList.addItemStackToList(ItemParts.getPartByName("scrap"));
		ScrapboxList.addItemStackToList(ItemParts.getPartByName("rubber"));

		ScrapboxList.addItemStackToList(new ItemStack(Blocks.TRAPDOOR));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.STONE_BUTTON));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.WOODEN_BUTTON));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.ACACIA_FENCE));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.ACACIA_FENCE_GATE));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.BIRCH_FENCE));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.BIRCH_FENCE_GATE));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.DARK_OAK_FENCE));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.DARK_OAK_FENCE_GATE));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.JUNGLE_FENCE));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.JUNGLE_FENCE_GATE));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.NETHER_BRICK_FENCE));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.OAK_FENCE));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.OAK_FENCE_GATE));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.SPRUCE_FENCE));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.SPRUCE_FENCE_GATE));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.BRICK_BLOCK));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.CRAFTING_TABLE));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.PUMPKIN));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.NETHERRACK));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.GRASS));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.DIRT, 1, 0));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.DIRT, 1, 1));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.SAND, 1, 0));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.SAND, 1, 1));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.GLOWSTONE));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.GRAVEL));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.HARDENED_CLAY));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.GLASS));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.GLASS_PANE));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.CACTUS));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.TALLGRASS, 1, 0));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.TALLGRASS, 1, 1));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.DEADBUSH));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.CHEST));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.TNT));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.RAIL));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.DETECTOR_RAIL));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.GOLDEN_RAIL));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.ACTIVATOR_RAIL));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.YELLOW_FLOWER));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.RED_FLOWER, 1, 0));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.RED_FLOWER, 1, 1));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.RED_FLOWER, 1, 2));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.RED_FLOWER, 1, 3));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.RED_FLOWER, 1, 4));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.RED_FLOWER, 1, 5));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.RED_FLOWER, 1, 6));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.RED_FLOWER, 1, 7));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.RED_FLOWER, 1, 8));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.BROWN_MUSHROOM));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.RED_MUSHROOM));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.BROWN_MUSHROOM_BLOCK));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.RED_MUSHROOM_BLOCK));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.SAPLING, 1, 0));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.SAPLING, 1, 1));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.SAPLING, 1, 2));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.SAPLING, 1, 3));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.SAPLING, 1, 4));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.SAPLING, 1, 5));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.LEAVES, 1, 0));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.LEAVES, 1, 1));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.LEAVES, 1, 2));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.LEAVES, 1, 3));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.LEAVES2, 1, 0));
		ScrapboxList.addItemStackToList(new ItemStack(Blocks.LEAVES2, 1, 1));

		ScrapboxList.addItemStackToList(new ItemStack(ModBlocks.rubberSapling));

		for (String i : ItemDusts.types)
		{
			if (i.equals(ModItems.META_PLACEHOLDER))
			{
				continue;
			}
			ScrapboxList.addItemStackToList(ItemDusts.getDustByName(i));
		}

		for (String i : ItemNuggets.types)
			ScrapboxList.addItemStackToList(ItemNuggets.getNuggetByName(i));

		for (String i : ItemGems.types)
			ScrapboxList.addItemStackToList(ItemGems.getGemByName(i));

		registerDyable(Blocks.CARPET);
		registerDyable(Blocks.STAINED_GLASS);
		registerDyable(Blocks.STAINED_GLASS_PANE);
		registerDyable(Blocks.STAINED_HARDENED_CLAY);

		for (int i = 0; i < ScrapboxList.stacks.size(); i++)
		{
			RecipeHandler.addRecipe(new ScrapboxRecipe(ScrapboxList.stacks.get(i)));
		}

		boolean showAllItems = false;

		if (showAllItems)
		{
			//This is bad, laggy and slow
			List<Item> items = Lists
					.newArrayList(Iterables.filter(Item.REGISTRY, item -> item.getRegistryName() != null));
			Collections.sort(items,
					(i1, i2) -> i1.getRegistryName().toString().compareTo(i2.getRegistryName().toString()));

			for (Item item : items)
			{
				List<ItemStack> stacks = new ArrayList<>();
				if (item.getHasSubtypes())
				{
					for (int i = 0; i < item.getMaxDamage(); i++)
					{
						stacks.add(new ItemStack(item, 1, i));
					}
				} else
				{
					stacks.add(new ItemStack(item, 1, 0));
				}
				for (ItemStack stack : stacks)
				{
					RecipeHandler.addRecipe(new RecyclerRecipe(stack));
				}
			}
		} else
		{
			for (int i = 0; i < ScrapboxList.stacks.size(); i++)
			{
				RecipeHandler.addRecipe(new RecyclerRecipe(ScrapboxList.stacks.get(i)));
			}
		}

	}

	static void registerMetadataItem(ItemStack item)
	{
		for (int i = 0; i < item.getItem().getMaxDamage(); i++)
		{
			ScrapboxList.addItemStackToList(new ItemStack(item.getItem(), 1, i));
		}
	}

	static void registerDyable(Block block)
	{
		for (int i = 0; i < 16; i++)
			ScrapboxList.stacks.add(new ItemStack(block, 1, i));
	}

	static void addWireRecipes()
	{
		CraftingHelper.addShapedOreRecipe(ItemStandaloneCables.getCableByName("copper", 6), "CCC", 'C', "ingotCopper");
		CraftingHelper.addShapedOreRecipe(ItemStandaloneCables.getCableByName("tin", 9), "CCC", 'C', "ingotTin");
		CraftingHelper.addShapedOreRecipe(ItemStandaloneCables.getCableByName("gold", 12), "CCC", 'C', "ingotGold");
		CraftingHelper
				.addShapedOreRecipe(ItemStandaloneCables.getCableByName("hv", 12), "CCC", 'C', "ingotRefinedIron");

		CraftingHelper
				.addShapedOreRecipe(ItemStandaloneCables.getCableByName("glassfiber", 4), "GGG", "SDS", "GGG", 'G',
						"blockGlass", 'S', "dustRedstone", 'D', "diamondTR");

		CraftingHelper
				.addShapedOreRecipe(ItemStandaloneCables.getCableByName("glassfiber", 6), "GGG", "SDS", "GGG", 'G',
						"blockGlass", 'S', "dustRedstone", 'D', "gemRuby");

		CraftingHelper
				.addShapedOreRecipe(ItemStandaloneCables.getCableByName("glassfiber", 6), "GGG", "SDS", "GGG", 'G',
						"blockGlass", 'S', "ingotSilver", 'D', "diamondTR");
		CraftingHelper
				.addShapedOreRecipe(ItemStandaloneCables.getCableByName("glassfiber", 8), "GGG", "SDS", "GGG", 'G',
						"blockGlass", 'S', "ingotElectrum", 'D', "diamondTR");

		CraftingHelper.addShapelessOreRecipe(ItemStandaloneCables.getCableByName("insulatedcopper"), "materialRubber",
				ItemStandaloneCables.getCableByName("copper"));
		CraftingHelper.addShapelessOreRecipe(ItemStandaloneCables.getCableByName("insulatedgold"), "materialRubber",
				"materialRubber", ItemStandaloneCables.getCableByName("gold"));
		CraftingHelper.addShapelessOreRecipe(ItemStandaloneCables.getCableByName("insulatedhv"), "materialRubber",
				"materialRubber", ItemStandaloneCables.getCableByName("hv"));

		CraftingHelper
				.addShapedOreRecipe(ItemStandaloneCables.getCableByName("insulatedcopper", 6), "RRR", "III", "RRR", 'R',
						"materialRubber", 'I', "ingotCopper");
		CraftingHelper
				.addShapedOreRecipe(ItemStandaloneCables.getCableByName("insulatedgold", 4), "RRR", "RIR", "RRR", 'R',
						"materialRubber", 'I', "ingotGold");
		CraftingHelper
				.addShapedOreRecipe(ItemStandaloneCables.getCableByName("insulatedhv", 4), "RRR", "RIR", "RRR", 'R',
						"materialRubber", 'I', "ingotRefinedIron");
	}

	private static void addCompressorRecipes()
	{
		RecipeHandler.addRecipe(new CompressorRecipe(ItemIngots.getIngotByName("advancedAlloy"),
				ItemPlates.getPlateByName("advancedAlloy"), 400, 20));
		RecipeHandler.addRecipe(
				new CompressorRecipe(ItemParts.getPartByName("carbonmesh"), ItemPlates.getPlateByName("carbon"), 400,
						2));

		RecipeHandler.addRecipe(
				new CompressorRecipe(new ItemStack(Items.IRON_INGOT), ItemPlates.getPlateByName("iron"), 400, 2));
		RecipeHandler.addRecipe(
				new CompressorRecipe(ItemIngots.getIngotByName("copper"), ItemPlates.getPlateByName("copper"), 400,
						2));
		RecipeHandler.addRecipe(
				new CompressorRecipe(ItemIngots.getIngotByName("tin"), ItemPlates.getPlateByName("tin"), 400, 2));
		RecipeHandler.addRecipe(
				new CompressorRecipe(ItemIngots.getIngotByName("aluminum"), ItemPlates.getPlateByName("aluminum"), 400,
						2));
		RecipeHandler.addRecipe(
				new CompressorRecipe(ItemIngots.getIngotByName("brass"), ItemPlates.getPlateByName("brass"), 400, 2));
		RecipeHandler.addRecipe(
				new CompressorRecipe(ItemIngots.getIngotByName("bronze"), ItemPlates.getPlateByName("bronze"), 400,
						2));
		RecipeHandler.addRecipe(
				new CompressorRecipe(ItemIngots.getIngotByName("lead"), ItemPlates.getPlateByName("lead"), 400, 2));
		RecipeHandler.addRecipe(
				new CompressorRecipe(ItemIngots.getIngotByName("silver"), ItemPlates.getPlateByName("silver"), 400,
						2));
	}

	static void addExtractorRecipes()
	{
		RecipeHandler.addRecipe(
				new ExtractorRecipe(ItemParts.getPartByName("rubberSap"),
						ItemParts.getPartByName("rubber", 3), 400, 2));
		RecipeHandler.addRecipe(
				new ExtractorRecipe(new ItemStack(ModBlocks.rubberLog),
						ItemParts.getPartByName("rubber"), 400, 2, true));
	}

	static void addIc2ReplacementReicpes()
	{
		// TODO: Replace item pump with block
		/*CraftingHelper.addShapedOreRecipe(ItemParts.getPartByName("pump"), "CEC", "CMC", "PTP", 'C',
				ItemCells.getCellByName("empty"), 'T', new ItemStack(ModItems.treeTap), 'M', "machineBlockBasic", 'P',
				new ItemStack(Blocks.iron_bars), 'E', "circuitBasic");

		// TODO: Replace item teleporter with block
		CraftingHelper
				.addShapedOreRecipe(ItemParts.getPartByName("teleporter"), "CTC", "WMW", "CDC", 'C', "circuitAdvanced",
						'T', new ItemStack(ModItems.frequencyTransmitter), 'M', "machineBlockAdvanced", 'W',
						ItemStandaloneCables.getCableByName("glassfiber"), 'D', "gemDiamond", 'E', "circuitBasic");
		*/
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
                new ItemStack(Blocks.NETHERRACK),
                ItemDusts.getDustByName("netherrack"),
                300, 27));
						
		for(String oreDictionaryName : OreDictionary.getOreNames()) {
            if(isDictPrefixed(oreDictionaryName, "ore", "gem", "ingot")) {
                ItemStack oreStack = getDictOreOrNull(oreDictionaryName, 1);
                String[] data = getDictData(oreDictionaryName);

                //High-level ores shouldn't grind here
                if(data[0].equals("ore") && (
                        data[1].equals("tungsten") ||
                        data[1].equals("titanium") ||
                        data[1].equals("aluminium") ||
                        data[1].equals("iridium")  ||
						data[1].equals("saltpeter")) ||
						oreStack == null)
                    continue;

                boolean ore = data[0].equals("ore");
				Core.logHelper.info("Ore: " + data[1]);
                ItemStack dust = getDictOreOrNull(joinDictName("dust", data[1]), ore ? 2 : 1);
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, dust, ore ? 270 : 200, ore ? 31 : 22));
            }
        }

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

		// Storage Blocks
		for (String name : ArrayUtils.addAll(BlockStorage.types, BlockStorage2.types))
		{
			CraftingHelper.addShapedOreRecipe(BlockStorage.getStorageBlockByName(name), "AAA", "AAA", "AAA", 'A',
					"ingot" + name.substring(0, 1).toUpperCase() + name.substring(1));
		}
		CraftingHelper
				.addShapedOreRecipe(ItemCells.getCellByName("empty", 16, false), " T ", "T T", " T ", 'T', "ingotTin");

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.ironFence, 6), "   ", "RRR", "RRR", 'R',
				ItemIngots.getIngotByName("refinedIron"));

		if(ConfigTechReborn.enableGemArmorAndTools){
			addGemToolRecipes(new ItemStack(ModItems.rubySword), new ItemStack(ModItems.rubyPickaxe),
					new ItemStack(ModItems.rubyAxe), new ItemStack(ModItems.rubyHoe), new ItemStack(ModItems.rubySpade),
					new ItemStack(ModItems.rubyHelmet), new ItemStack(ModItems.rubyChestplate),
					new ItemStack(ModItems.rubyLeggings), new ItemStack(ModItems.rubyBoots), "gemRuby");

			addGemToolRecipes(new ItemStack(ModItems.sapphireSword), new ItemStack(ModItems.sapphirePickaxe),
					new ItemStack(ModItems.sapphireAxe), new ItemStack(ModItems.sapphireHoe),
					new ItemStack(ModItems.sapphireSpade), new ItemStack(ModItems.sapphireHelmet),
					new ItemStack(ModItems.sapphireChestplate), new ItemStack(ModItems.sapphireLeggings),
					new ItemStack(ModItems.sapphireBoots), "gemSapphire");

			addGemToolRecipes(new ItemStack(ModItems.peridotSword), new ItemStack(ModItems.peridotPickaxe),
					new ItemStack(ModItems.peridotAxe), new ItemStack(ModItems.peridotHoe),
					new ItemStack(ModItems.peridotSpade), new ItemStack(ModItems.peridotHelmet),
					new ItemStack(ModItems.peridotChestplate), new ItemStack(ModItems.peridotLeggings),
					new ItemStack(ModItems.peridotBoots), "gemPeridot");

			addGemToolRecipes(new ItemStack(ModItems.bronzeSword), new ItemStack(ModItems.bronzePickaxe),
					new ItemStack(ModItems.bronzeAxe), new ItemStack(ModItems.bronzeHoe),
					new ItemStack(ModItems.bronzeSpade), new ItemStack(ModItems.bronzeHelmet),
					new ItemStack(ModItems.bronzeChestplate), new ItemStack(ModItems.bronzeLeggings),
					new ItemStack(ModItems.bronzeBoots), "ingotBronze");
		}

		CraftingHelper
				.addShapedOreRecipe(new ItemStack(ModItems.ironChainsaw), " SS", "SCS", "BS ", 'S', "ingotSteel", 'B',
						"reBattery", 'C',
						"circuitBasic");

		CraftingHelper
				.addShapedOreRecipe(new ItemStack(ModItems.diamondChainsaw), " DD", "TBD", "CT ", 'T', "ingotTitanium",
						'B', ironChainsawStack, 'C', "circuitAdvanced", 'D',
						"diamondTR");

		CraftingHelper
				.addShapedOreRecipe(new ItemStack(ModItems.steelJackhammer), "SBS", "SCS", " S ", 'S', "ingotSteel",
						'B', "reBattery", 'C',
						"circuitBasic");

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.diamondJackhammer), "DCD", "TBT", " D ", 'T',
				"ingotTitanium", 'B', steelJackhammerStack, 'C', "circuitAdvanced",
				'D', "diamondTR");

		CraftingHelper.addShapelessOreRecipe(ItemParts.getPartByName("carbonfiber"), ItemDusts.getDustByName("coal"),
				ItemDusts.getDustByName("coal"), ItemDusts.getDustByName("coal"), ItemDusts.getDustByName("coal"));

		CraftingHelper.addShapelessOreRecipe(ItemParts.getPartByName("carbonfiber"), ItemCells.getCellByName("carbon"),
				ItemCells.getCellByName("carbon"), ItemCells.getCellByName("carbon"), ItemCells.getCellByName("carbon"),
				ItemCells.getCellByName("carbon"), ItemCells.getCellByName("carbon"), ItemCells.getCellByName("carbon"),
				ItemCells.getCellByName("carbon"), ItemCells.getCellByName("carbon"));

		CraftingHelper
				.addShapelessOreRecipe(ItemParts.getPartByName("carbonmesh"), ItemParts.getPartByName("carbonfiber"),
						ItemParts.getPartByName("carbonfiber"));

		CraftingHelper
				.addShapedOreRecipe(ItemParts.getPartByName("computerMonitor"), "ADA", "DGD", "ADA", 'D', dyes, 'A',
						"ingotAluminum", 'G', Blocks.GLASS_PANE);

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.reinforcedglass, 7), "GAG", "GGG", "GAG", 'A',
				ItemIngots.getIngotByName("advancedAlloy"), 'G', Blocks.GLASS);

		CraftingHelper
				.addShapedOreRecipe(new ItemStack(ModBlocks.windMill, 2), "IXI", "XGX", "IXI", 'I', "ingotIron", 'G',
						ModBlocks.Generator);

		CraftingHelper
				.addShapedOreRecipe(new ItemStack(ModBlocks.waterMill), "SWS", "WGW", "SWS", 'S', Items.STICK, 'W',
						"plankWood", 'G', ModBlocks.Generator);

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.hvt), "XHX", "XMX", "XHX", 'M', ModBlocks.mvt, 'H',
				ItemStandaloneCables.getCableByName("insulatedhv"));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.mvt), "XGX", "XMX", "XGX", 'M',
				BlockMachineFrame.getFrameByName("machine", 1), 'G',
				ItemStandaloneCables.getCableByName("insulatedgold"));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.lvt), "PWP", "CCC", "PPP", 'P', "plankWood", 'C',
				"ingotCopper", 'W', ItemStandaloneCables.getCableByName("insulatedcopper"));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.MachineCasing, 4, 0), "RRR", "CAC", "RRR", 'R',
				ItemIngots.getIngotByName("refinedIron"), 'C', "circuitBasic", 'A',
				BlockMachineFrame.getFrameByName("machine", 1));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.MachineCasing, 4, 1), "RRR", "CAC", "RRR", 'R',
				"ingotSteel", 'C', "circuitAdvanced", 'A',
				BlockMachineFrame.getFrameByName("advancedMachine", 1));

		CraftingHelper.addShapedOreRecipe(ItemParts.getPartByName("dataOrb"), "DDD", "DID", "DDD", 'D',
				"circuitData", 'I', "circuitElite");

		CraftingHelper.addShapedOreRecipe(ItemParts.getPartByName("dataControlCircuit", 4), "CDC", "DID", "CDC", 'I',
				ItemPlates.getPlateByName("iridium"), 'D', "circuitData", 'C', "circuitAdvanced");

		CraftingHelper
				.addShapedOreRecipe(new ItemStack(ModBlocks.thermalGenerator), "III", "IRI", "CGC", 'I', "ingotInvar",
						'R', ModBlocks.reinforcedglass, 'G', ModBlocks.Generator, 'C',
						"circuitBasic");

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.recycler), "XEX", "DCD", "GDG", 'D', Blocks.DIRT, 'C',
				ModBlocks.Compressor, 'G', Items.GLOWSTONE_DUST, 'E', "circuitBasic");

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.batBox), "WCW", "BBB", "WWW", 'W', "plankWood", 'B',
				batteryStack, 'C', ItemStandaloneCables.getCableByName("insulatedcopper"));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.mfe), "GEG", "EME", "GEG", 'M',
				BlockMachineFrame.getFrameByName("machine", 1), 'E', crystalStack, 'G',
				ItemStandaloneCables.getCableByName("insulatedgold"));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.mfsu), "LAL", "LML", "LOL", 'A',
				"circuitAdvanced", 'L', lapcrystalStack, 'M', new ItemStack(ModBlocks.mfe),
				'O', BlockMachineFrame.getFrameByName("advancedMachine", 1));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.IndustrialElectrolyzer), "RER", "CEC", "RER", 'R',
				ItemIngots.getIngotByName("refinediron"), 'E', new ItemStack(ModBlocks.Extractor), 'C',
				"circuitAdvanced");

		// Mixed Metal Ingot Recipes :P

		CraftingHelper.addShapedOreRecipe(ItemIngots.getIngotByName("mixedmetal", 2), "RRR", "BBB", "TTT", 'R',
				"ingotRefinedIron", 'B', "ingotBronze", 'T', "ingotTin");

		CraftingHelper.addShapedOreRecipe(ItemIngots.getIngotByName("mixedmetal", 2), "RRR", "BBB", "TTT", 'R',
				"ingotRefinedIron", 'B', "ingotBronze", 'T', "ingotZinc");

		CraftingHelper.addShapedOreRecipe(ItemIngots.getIngotByName("mixedmetal", 2), "RRR", "BBB", "TTT", 'R',
				"ingotRefinedIron", 'B', "ingotBrass", 'T', "ingotTin");

		CraftingHelper.addShapedOreRecipe(ItemIngots.getIngotByName("mixedmetal", 2), "RRR", "BBB", "TTT", 'R',
				"ingotRefinedIron", 'B', "ingotBrass", 'T', "ingotZinc");

		CraftingHelper
				.addShapedOreRecipe(ItemIngots.getIngotByName("mixedmetal", 3), "RRR", "BBB", "TTT", 'R', "ingotNickel",
						'B', "ingotBronze", 'T', "ingotTin");

		CraftingHelper
				.addShapedOreRecipe(ItemIngots.getIngotByName("mixedmetal", 3), "RRR", "BBB", "TTT", 'R', "ingotNickel",
						'B', "ingotBronze", 'T', "ingotZinc");

		CraftingHelper
				.addShapedOreRecipe(ItemIngots.getIngotByName("mixedmetal", 3), "RRR", "BBB", "TTT", 'R', "ingotNickel",
						'B', "ingotBrass", 'T', "ingotTin");

		CraftingHelper
				.addShapedOreRecipe(ItemIngots.getIngotByName("mixedmetal", 3), "RRR", "BBB", "TTT", 'R', "ingotNickel",
						'B', "ingotBrass", 'T', "ingotZinc");

		CraftingHelper
				.addShapedOreRecipe(ItemIngots.getIngotByName("mixedmetal", 4), "RRR", "BBB", "TTT", 'R', "ingotNickel",
						'B', "ingotBronze", 'T', "ingotAluminum");

		CraftingHelper
				.addShapedOreRecipe(ItemIngots.getIngotByName("mixedmetal", 4), "RRR", "BBB", "TTT", 'R', "ingotNickel",
						'B', "ingotBrass", 'T', "ingotAluminum");

		CraftingHelper
				.addShapedOreRecipe(ItemIngots.getIngotByName("mixedmetal", 4), "RRR", "BBB", "TTT", 'R', "ingotInvar",
						'B', "ingotBronze", 'T', "ingotTin");

		CraftingHelper
				.addShapedOreRecipe(ItemIngots.getIngotByName("mixedmetal", 4), "RRR", "BBB", "TTT", 'R', "ingotInvar",
						'B', "ingotBronze", 'T', "ingotZinc");

		CraftingHelper
				.addShapedOreRecipe(ItemIngots.getIngotByName("mixedmetal", 4), "RRR", "BBB", "TTT", 'R', "ingotInvar",
						'B', "ingotBrass", 'T', "ingotTin");

		CraftingHelper
				.addShapedOreRecipe(ItemIngots.getIngotByName("mixedmetal", 4), "RRR", "BBB", "TTT", 'R', "ingotInvar",
						'B', "ingotBrass", 'T', "ingotZinc");

		CraftingHelper
				.addShapedOreRecipe(ItemIngots.getIngotByName("mixedmetal", 5), "RRR", "BBB", "TTT", 'R', "ingotInvar",
						'B', "ingotBronze", 'T', "ingotAluminum");

		CraftingHelper
				.addShapedOreRecipe(ItemIngots.getIngotByName("mixedmetal", 5), "RRR", "BBB", "TTT", 'R', "ingotInvar",
						'B', "ingotBrass", 'T', "ingotAluminum");

		CraftingHelper.addShapedOreRecipe(ItemIngots.getIngotByName("mixedmetal", 5), "RRR", "BBB", "TTT", 'R',
				"ingotTitanium", 'B', "ingotBronze", 'T', "ingotTin");

		CraftingHelper.addShapedOreRecipe(ItemIngots.getIngotByName("mixedmetal", 5), "RRR", "BBB", "TTT", 'R',
				"ingotTitanium", 'B', "ingotBronze", 'T', "ingotZinc");

		CraftingHelper.addShapedOreRecipe(ItemIngots.getIngotByName("mixedmetal", 5), "RRR", "BBB", "TTT", 'R',
				"ingotTitanium", 'B', "ingotBrass", 'T', "ingotTin");

		CraftingHelper.addShapedOreRecipe(ItemIngots.getIngotByName("mixedmetal", 5), "RRR", "BBB", "TTT", 'R',
				"ingotTitanium", 'B', "ingotBrass", 'T', "ingotZinc");

		CraftingHelper.addShapedOreRecipe(ItemIngots.getIngotByName("mixedmetal", 6), "RRR", "BBB", "TTT", 'R',
				"ingotTitanium", 'B', "ingotBronze", 'T', "ingotAluminum");

		CraftingHelper.addShapedOreRecipe(ItemIngots.getIngotByName("mixedmetal", 6), "RRR", "BBB", "TTT", 'R',
				"ingotTitanium", 'B', "ingotBrass", 'T', "ingotAluminum");

		CraftingHelper.addShapedOreRecipe(ItemIngots.getIngotByName("mixedmetal", 5), "RRR", "BBB", "TTT", 'R',
				"ingotTungsten", 'B', "ingotBronze", 'T', "ingotTin");

		CraftingHelper.addShapedOreRecipe(ItemIngots.getIngotByName("mixedmetal", 5), "RRR", "BBB", "TTT", 'R',
				"ingotTungsten", 'B', "ingotBronze", 'T', "ingotZinc");

		CraftingHelper.addShapedOreRecipe(ItemIngots.getIngotByName("mixedmetal", 5), "RRR", "BBB", "TTT", 'R',
				"ingotTungsten", 'B', "ingotBrass", 'T', "ingotTin");

		CraftingHelper.addShapedOreRecipe(ItemIngots.getIngotByName("mixedmetal", 5), "RRR", "BBB", "TTT", 'R',
				"ingotTungsten", 'B', "ingotBrass", 'T', "ingotZinc");

		CraftingHelper.addShapedOreRecipe(ItemIngots.getIngotByName("mixedmetal", 6), "RRR", "BBB", "TTT", 'R',
				"ingotTungsten", 'B', "ingotBronze", 'T', "ingotAluminum");

		CraftingHelper.addShapedOreRecipe(ItemIngots.getIngotByName("mixedmetal", 6), "RRR", "BBB", "TTT", 'R',
				"ingotTungsten", 'B', "ingotBrass", 'T', "ingotAluminum");

		CraftingHelper.addShapedOreRecipe(ItemIngots.getIngotByName("mixedmetal", 8), "RRR", "BBB", "TTT", 'R',
				"ingotTungstensteel", 'B', "ingotBronze", 'T', "ingotTin");

		CraftingHelper.addShapedOreRecipe(ItemIngots.getIngotByName("mixedmetal", 8), "RRR", "BBB", "TTT", 'R',
				"ingotTungstensteel", 'B', "ingotBronze", 'T', "ingotZinc");

		CraftingHelper.addShapedOreRecipe(ItemIngots.getIngotByName("mixedmetal", 8), "RRR", "BBB", "TTT", 'R',
				"ingotTungstensteel", 'B', "ingotBrass", 'T', "ingotTin");

		CraftingHelper.addShapedOreRecipe(ItemIngots.getIngotByName("mixedmetal", 8), "RRR", "BBB", "TTT", 'R',
				"ingotTungstensteel", 'B', "ingotBrass", 'T', "ingotZinc");

		CraftingHelper.addShapedOreRecipe(ItemIngots.getIngotByName("mixedmetal", 9), "RRR", "BBB", "TTT", 'R',
				"ingotTungstensteel", 'B', "ingotBronze", 'T', "ingotAluminum");

		CraftingHelper.addShapedOreRecipe(ItemIngots.getIngotByName("mixedmetal", 9), "RRR", "BBB", "TTT", 'R',
				"ingotTungstensteel", 'B', "ingotBrass", 'T', "ingotAluminum");

		CraftingHelper
				.addShapedOreRecipe(new ItemStack(ModBlocks.Compressor), "SXS", "SCS", "SMS", 'C', "circuitBasic", 'M',
						BlockMachineFrame.getFrameByName("machine", 1), 'S', Blocks.STONE);

		CraftingHelper
				.addShapedOreRecipe(new ItemStack(ModBlocks.ElectricFurnace), "XCX", "RFR", "XXX", 'C', "circuitBasic",
						'F', new ItemStack(ModBlocks.ironFurnace), 'R', Items.REDSTONE);

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.ironFurnace), "III", "IXI", "III", 'I', "ingotIron");

		CraftingHelper
				.addShapedOreRecipe(new ItemStack(ModBlocks.ironFurnace), "XIX", "IXI", "IFI", 'I', "ingotIron", 'F',
						Blocks.FURNACE);

		CraftingHelper.addShapedOreRecipe(ItemParts.getPartByName("electronicCircuit"), "WWW", "SRS", "WWW", 'R',
				"ingotRefinedIron", 'S', Items.REDSTONE, 'W', ItemStandaloneCables.getCableByName("insulatedcopper"));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.reBattery), "XWX", "TRT", "TRT", 'T', "ingotTin", 'R',
				Items.REDSTONE, 'W', ItemStandaloneCables.getCableByName("insulatedcopper"));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.wrench), "BAB", "BBB", "ABA", 'B', "ingotBronze");

		CraftingHelper
				.addShapedOreRecipe(new ItemStack(ModBlocks.Extractor), "TMT", "TCT", "XXX", 'T', ModItems.treeTap, 'M',
						BlockMachineFrame.getFrameByName("machine", 1), 'C',
						"circuitBasic");

		CraftingHelper
				.addShapedOreRecipe(new ItemStack(ModBlocks.centrifuge), "RCR", "AEA", "RCR", 'R', "ingotRefinedIron",
						'E', new ItemStack(ModBlocks.Extractor), 'A', "machineBlockAdvanced", 'C', "circuitBasic");

		CraftingHelper.addShapedOreRecipe(ItemParts.getPartByName("advancedCircuit"), "RGR", "LCL", "RGR", 'R',
				Items.REDSTONE, 'G', Items.GLOWSTONE_DUST, 'L', "dyeBlue", 'C', "circuitBasic");

		CraftingHelper
				.addShapedOreRecipe(new ItemStack(ModItems.energyCrystal), "RRR", "RDR", "RRR", 'R', Items.REDSTONE,
						'D', Items.DIAMOND);

		CraftingHelper
				.addShapedOreRecipe(new ItemStack(ModItems.lapotronCrystal), "LCL", "LEL", "LCL", 'L', "dyeBlue", 'E',
						"energyCrystal", 'C',"circuitBasic");

		CraftingHelper.addShapelessOreRecipe(new ItemStack(ModBlocks.Generator), batteryStack,
				BlockMachineFrame.getFrameByName("machine", 1), Blocks.FURNACE);

		CraftingHelper.addShapedOreRecipe(BlockMachineFrame.getFrameByName("machine", 1), "AAA", "AXA", "AAA", 'A',
				ItemIngots.getIngotByName("refinediron"));

		CraftingHelper
				.addShapedOreRecipe(BlockMachineFrame.getFrameByName("advancedMachine", 1), "XCX", "AMA", "XCX", 'A',
						ItemIngots.getIngotByName("advancedAlloy"), 'C', ItemPlates.getPlateByName("carbon"), 'M',
						BlockMachineFrame.getFrameByName("machine", 1));

		CraftingHelper.addShapedOreRecipe(ItemParts.getPartByName("dataStorageCircuit", 8), "EEE", "ECE", "EEE", 'E',
				new ItemStack(Items.EMERALD), 'C', "circuitBasic");

		CraftingHelper
				.addShapedOreRecipe(new ItemStack(ModItems.parts, 4, 8), "DSD", "S S", "DSD", 'D', "dustDiamond", 'S',
						"ingotSteel");

		CraftingHelper
				.addShapedOreRecipe(new ItemStack(ModItems.parts, 1, 15), "AAA", "AMA", "AAA", 'A', "ingotAluminium",
						'M', new ItemStack(ModItems.parts, 1, 13));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Supercondensator), "EOE", "SAS", "EOE", 'E',
				"circuitMaster", 'O', ModItems.lapotronicOrb, 'S',
				ItemParts.getPartByName("superconductor"), 'A',
				BlockMachineFrame.getFrameByName("highlyAdvancedMachine", 1));

		CraftingHelper
				.addShapedOreRecipe(ItemParts.getPartByName("diamondSawBlade"), "DSD", "S S", "DSD", 'S', "plateSteel",
						'D', "dustDiamond");

		CraftingHelper.addShapedOreRecipe(ItemParts.getPartByName("tungstenGrindingHead", 2), "TST", "SBS", "TST", 'T',
				"plateTungsten", 'S', "plateSteel", 'B', "blockSteel");

		/* TODO: Make destructopack seperate item
		CraftingHelper.addShapedOreRecipe(ItemParts.getPartByName("destructoPack"), "CIC", "IBI", "CIC", 'C',
				"circuitAdvanced", 'I', "ingotAluminum", 'B',
				new ItemStack(Items.lava_bucket));

		CraftingHelper.addShapedOreRecipe(ItemParts.getPartByName("destructoPack"), "CIC", "IBI", "CIC", 'C',
				"circuitAdvanced", 'I', "ingotRefinedIron", 'B',
				new ItemStack(Items.lava_bucket));
		*/

		CraftingHelper
				.addShapedOreRecipe(new ItemStack(ModItems.cloakingDevice), "CIC", "IOI", "CIC", 'C', "ingotChrome",
						'I', "plateIridium", 'O', new ItemStack(ModItems.lapotronicOrb));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.treeTap), " S ", "PPP", "P  ", 'S', "stickWood", 'P',
				"plankWood");

		CraftingHelper
				.addShapedOreRecipe(new ItemStack(ModItems.rockCutter), "DT ", "DT ", "DCB", 'D', "dustDiamond", 'T',
						"ingotTitanium", 'C', "circuitBasic", 'B', batteryStack);

		for (String part : ItemParts.types)
		{
			if (part.endsWith("Gear"))
			{
				CraftingHelper.addShapedOreRecipe(ItemParts.getPartByName(part), " O ", "OIO", " O ", 'I',
						new ItemStack(Items.IRON_INGOT), 'O',
						"ingot" + StringUtils.toFirstCapital(part.replace("Gear", "")));
			}
		}

		CraftingHelper.addShapedOreRecipe(ItemParts.getPartByName("nichromeHeatingCoil"), " N ", "NCN", " N ", 'N',
				"ingotNickel", 'C', "ingotChrome");

		CraftingHelper.addShapedOreRecipe(ItemParts.getPartByName("kanthalHeatingCoil"), "III", "CAA", "AAA", 'I',
				"ingotSteel", 'C', "ingotChrome", 'A', "ingotAluminum");

		CraftingHelper.addShapedOreRecipe(ItemParts.getPartByName("heliumCoolantSimple"), " T ", "TCT", " T ", 'T',
				"ingotTin", 'C', ItemCells.getCellByName("helium", 1, false));

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

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Aesu), "LLL", "LCL", "LLL", 'L',
				new ItemStack(ModItems.lapotronicOrb), 'C', new ItemStack(ModBlocks.ComputerCube));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Idsu), "PAP", "ACA", "PAP", 'P',
				ItemPlates.getPlateByName("iridium"), 'C', new ItemStack(Blocks.ENDER_CHEST), 'A',
				new ItemStack(ModBlocks.Aesu));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.FusionControlComputer), "CCC", "PTP", "CCC", 'P',
				new ItemStack(ModBlocks.ComputerCube), 'T', new ItemStack(ModBlocks.FusionCoil), 'C',
				"circuitMaster");

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.LightningRod), "CAC", "ACA", "CAC", 'A',
				new ItemStack(ModBlocks.MachineCasing, 1, 2), 'S', ItemParts.getPartByName("superConductor"), 'C',
				"circuitMaster");

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.FusionCoil), "CSC", "NAN", "CRC", 'A',
				new ItemStack(ModBlocks.MachineCasing, 1, 2), 'N', ItemParts.getPartByName("nichromeHeatingCoil"), 'C',
				"circuitMaster", 'S', ItemParts.getPartByName("superConductor"), 'R',
				ItemParts.getPartByName("iridiumNeutronReflector"));

		CraftingHelper.addShapedOreRecipe(ItemParts.getPartByName("iridiumNeutronReflector"), "PPP", "PIP", "PPP", 'P',
				ItemParts.getPartByName("thickNeutronReflector"), 'I', "ingotIridium");

		CraftingHelper.addShapedOreRecipe(ItemParts.getPartByName("thickNeutronReflector"), " P ", "PCP", " P ", 'P',
				ItemParts.getPartByName("neutronReflector"), 'C', ItemCells.getCellByName("Berylium"));

		CraftingHelper
				.addShapedOreRecipe(ItemParts.getPartByName("neutronReflector"), "TCT", "CPC", "TCT", 'T', "dustTin",
						'C', "dustCoal", 'P', "plateCopper");

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.scrapBox), "SSS", "SSS", "SSS", 'S',
				ItemParts.getPartByName("scrap"));

		CraftingHelper.addShapedOreRecipe(ItemUpgrades.getUpgradeByName("Overclock"), "TTT", "WCW", 'T',
				ItemParts.getPartByName("CoolantSimple"), 'W', ItemStandaloneCables.getCableByName("insulatedcopper"),
				'C', "circuitBasic");

		CraftingHelper.addShapedOreRecipe(ItemUpgrades.getUpgradeByName("Overclock", 2), " T ", "WCW", 'T',
				ItemParts.getPartByName("heliumCoolantSimple"), 'W',
				ItemStandaloneCables.getCableByName("insulatedcopper"), 'C',
				"circuitBasic");

		CraftingHelper.addShapedOreRecipe(ItemUpgrades.getUpgradeByName("Overclock", 2), " T ", "WCW", 'T',
				ItemParts.getPartByName("NaKCoolantSimple"), 'W',
				ItemStandaloneCables.getCableByName("insulatedcopper"), 'C',
				"circuitBasic");

		CraftingHelper.addShapedOreRecipe(ItemUpgrades.getUpgradeByName("Transformer"), "GGG", "WTW", "GCG", 'G',
				"blockGlass", 'W', ItemStandaloneCables.getCableByName("insulatedgold"), 'C',
				"circuitBasic", 'T', ModBlocks.mvt);

		CraftingHelper.addShapedOreRecipe(ItemUpgrades.getUpgradeByName("EnergyStorage"), "PPP", "WBW", "PCP", 'P',
				"plankWood", 'W', ItemStandaloneCables.getCableByName("insulatedcopper"), 'C',
				"circuitBasic", 'B', ModItems.reBattery);

		CraftingHelper
				.addShapedOreRecipe(ItemParts.getPartByName("CoolantSimple"), " T ", "TWT", " T ", 'T', "ingotTin", 'W',
						"containerWater");

		CraftingHelper
				.addShapedOreRecipe(ItemParts.getPartByName("CoolantTriple"), "TTT", "CCC", "TTT", 'T', "ingotTin", 'C',
						ItemParts.getPartByName("CoolantSimple"));

		CraftingHelper
				.addShapedOreRecipe(ItemParts.getPartByName("CoolantSix"), "TCT", "TPT", "TCT", 'T', "ingotTin", 'C',
						ItemParts.getPartByName("CoolantTriple"), 'P', ItemPlates.getPlateByName("copper"));

		CraftingHelper
				.addShapedOreRecipe(ItemParts.getPartByName("NaKCoolantSimple"), "TST", "PCP", "TST", 'T', "ingotTin",
						'C', ItemParts.getPartByName("CoolantSimple"), 'S', ItemCells.getCellByName("sodium"), 'P',
						ItemCells.getCellByName("potassium"));

		CraftingHelper
				.addShapedOreRecipe(ItemParts.getPartByName("NaKCoolantSimple"), "TPT", "SCS", "TPT", 'T', "ingotTin",
						'C', ItemParts.getPartByName("CoolantSimple"), 'S', ItemCells.getCellByName("sodium"), 'P',
						ItemCells.getCellByName("potassium"));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.advancedDrill), "ODO", "AOA", 'O',
				ItemUpgrades.getUpgradeByName("Overclock"), 'D', diamondDrillStack, 'A', "circuitAdvanced");
		CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.advancedChainsaw), "ODO", "AOA", 'O',
				ItemUpgrades.getUpgradeByName("Overclock"), 'D', diamondChainsawStack, 'A', "circuitAdvanced");
		CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.advancedJackhammer), "ODO", "AOA", 'O',
				ItemUpgrades.getUpgradeByName("Overclock"), 'D', diamondJackhammerStack, 'A', "circuitAdvanced");

		Core.logHelper.info("Shapped Recipes Added");
	}

	static void addShapelessRecipes()
	{
		for (String name : ArrayUtils.addAll(BlockStorage.types, BlockStorage2.types))
		{
			ItemStack item = null;
			try
			{
				item = ItemIngots.getIngotByName(name, 9);
			} catch (InvalidParameterException e)
			{
				try
				{
					item = ItemGems.getGemByName(name, 9);
				} catch (InvalidParameterException e2)
				{
					continue;
				}
			}

			if (item == null)
			{
				continue;
			}

			CraftingHelper
					.addShapelessRecipe(BlockStorage.getStorageBlockByName(name), item, item, item, item, item, item,
							item, item, item);
			CraftingHelper.addShapelessRecipe(item, BlockStorage.getStorageBlockByName(name, 9));
		}

		CraftingHelper.addShapelessRecipe(new ItemStack(ModBlocks.rubberPlanks, 4), ModBlocks.rubberLog);
		CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.frequencyTransmitter),
				ItemStandaloneCables.getCableByName("insulatedcopper"), "circuitBasic");

		for (String name : ItemDustsSmall.types)
		{
			if (name.equals(ModItems.META_PLACEHOLDER))
			{
				continue;
			}
			CraftingHelper
					.addShapelessRecipe(ItemDustsSmall.getSmallDustByName(name, 4), ItemDusts.getDustByName(name));
			CraftingHelper.addShapelessRecipe(ItemDusts.getDustByName(name, 1), ItemDustsSmall.getSmallDustByName(name),
					ItemDustsSmall.getSmallDustByName(name), ItemDustsSmall.getSmallDustByName(name),
					ItemDustsSmall.getSmallDustByName(name));
		}

		Core.logHelper.info("Shapeless Recipes Added");
	}

	static void addMachineRecipes()
	{
		CraftingHelper
				.addShapedOreRecipe(new ItemStack(ModBlocks.quantumTank), "EPE", "PCP", "EPE", 'P', "ingotPlatinum",
						'E', "circuitAdvanced", 'C', ModBlocks.quantumChest);

		CraftingHelper
				.addShapedOreRecipe(new ItemStack(ModBlocks.digitalChest), "PPP", "PDP", "PCP", 'P', "plateAluminum",
						'D', ItemParts.getPartByName("dataOrb"), 'C', ItemParts.getPartByName("computerMonitor"));

		CraftingHelper
				.addShapedOreRecipe(new ItemStack(ModBlocks.digitalChest), "PPP", "PDP", "PCP", 'P', "plateSteel", 'D',
						ItemParts.getPartByName("dataOrb"), 'C', ItemParts.getPartByName("computerMonitor"));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.AlloySmelter), "XCX", "FMF", "XXX", 'C',
				"circuitBasic", 'F', new ItemStack(ModBlocks.ElectricFurnace), 'M',
				BlockMachineFrame.getFrameByName("machine", 1));

		CraftingHelper
				.addShapedOreRecipe(new ItemStack(ModBlocks.LesuStorage), "LLL", "LCL", "LLL", 'L', "blockLapis", 'C',
						"circuitBasic");

		TechRebornAPI
				.addRollingOreMachinceRecipe(ItemParts.getPartByName("cupronickelHeatingCoil"), "NCN", "C C", "NCN",
						'N', "ingotCupronickel", 'C', "ingotCopper");

		RecipeHandler.addRecipe(new VacuumFreezerRecipe(ItemIngots.getIngotByName("hotTungstensteel"),
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
                ItemIngots.getIngotByName("hotTungstensteel"),
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

	static void addSmeltingRecipes() {
		CraftingHelper.addSmelting(ItemDusts.getDustByName("iron", 1), new ItemStack(Items.IRON_INGOT), 1F);
		CraftingHelper.addSmelting(ItemDusts.getDustByName("gold", 1), new ItemStack(Items.GOLD_INGOT), 1F);
		CraftingHelper.addSmelting(ItemParts.getPartByName("rubberSap"), ItemParts.getPartByName("rubber"), 1F);
		CraftingHelper.addSmelting(new ItemStack(Items.IRON_INGOT), ItemIngots.getIngotByName("refinediron"), 1F);
		CraftingHelper.addSmelting(BlockOre2.getOreByName("copper"), ItemIngots.getIngotByName("copper"), 1F);
		CraftingHelper.addSmelting(BlockOre2.getOreByName("tin"), ItemIngots.getIngotByName("tin"), 1F);
		CraftingHelper.addSmelting(BlockOre.getOreByName("Silver"), ItemIngots.getIngotByName("silver"), 1F);
		CraftingHelper.addSmelting(BlockOre.getOreByName("Lead"), ItemIngots.getIngotByName("lead"), 1F);
		CraftingHelper.addSmelting(BlockOre.getOreByName("Sheldonite"), ItemIngots.getIngotByName("platinum"), 1F);
		CraftingHelper
				.addSmelting(ItemIngots.getIngotByName("mixedMetal"), ItemIngots.getIngotByName("advancedAlloy"), 1F);
		CraftingHelper.addSmelting(ItemDusts.getDustByName("nickel", 1), ItemIngots.getIngotByName("nickel"), 1F);
		CraftingHelper.addSmelting(ItemDusts.getDustByName("platinum", 1), ItemIngots.getIngotByName("platinum"), 1F);
		CraftingHelper.addSmelting(ItemDusts.getDustByName("zinc", 1), ItemIngots.getIngotByName("zinc"), 1F);
		Core.logHelper.info("Smelting Recipes Added");
	}

	static void addAlloySmelterRecipes()
	{

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
		if (OreUtil.doesOreExistAndValid("ingotBrass"))
		{
			ItemStack brassStack = OreDictionary.getOres("ingotBrass").get(0);
			brassStack.stackSize = 4;
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
		if (OreUtil.doesOreExistAndValid("ingotRedAlloy"))
		{
			ItemStack redAlloyStack = OreDictionary.getOres("ingotRedAlloy").get(0);
			redAlloyStack.stackSize = 1;
			RecipeHandler.addRecipe(
					new AlloySmelterRecipe(new ItemStack(Items.REDSTONE, 4), ItemIngots.getIngotByName("copper", 1),
							redAlloyStack, 200, 16));
			RecipeHandler.addRecipe(
					new AlloySmelterRecipe(new ItemStack(Items.REDSTONE, 4), new ItemStack(Items.IRON_INGOT, 1),
							redAlloyStack, 200, 16));
		}

		// Blue Alloy
		if (OreUtil.doesOreExistAndValid("ingotBlueAlloy"))
		{
			ItemStack blueAlloyStack = OreDictionary.getOres("ingotBlueAlloy").get(0);
			blueAlloyStack.stackSize = 1;
			RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemDusts.getDustByName("teslatite", 4),
					ItemIngots.getIngotByName("silver", 1), blueAlloyStack, 200, 16));
		}

		// Blue Alloy
		if (OreUtil.doesOreExistAndValid("ingotPurpleAlloy") && OreUtil.doesOreExistAndValid("dustInfusedTeslatite"))
		{
			ItemStack purpleAlloyStack = OreDictionary.getOres("ingotPurpleAlloy").get(0);
			purpleAlloyStack.stackSize = 1;
			ItemStack infusedTeslatiteStack = OreDictionary.getOres("ingotPurpleAlloy").get(0);
			infusedTeslatiteStack.stackSize = 8;
			RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemIngots.getIngotByName("redAlloy", 1),
					ItemIngots.getIngotByName("blueAlloy", 1), purpleAlloyStack, 200, 16));
			RecipeHandler.addRecipe(
					new AlloySmelterRecipe(new ItemStack(Items.GOLD_INGOT, 1), infusedTeslatiteStack, purpleAlloyStack,
							200, 16));
		}

		// Aluminum Brass
		if (OreUtil.doesOreExistAndValid("ingotAluminumBrass"))
		{
			ItemStack aluminumBrassStack = OreDictionary.getOres("ingotAluminumBrass").get(0);
			aluminumBrassStack.stackSize = 4;
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
				.doesOreExistAndValid("ingotArdite"))
		{
			ItemStack manyullynStack = OreDictionary.getOres("ingotManyullyn").get(0);
			manyullynStack.stackSize = 1;
			ItemStack cobaltStack = OreDictionary.getOres("ingotCobalt").get(0);
			cobaltStack.stackSize = 1;
			ItemStack arditeStack = OreDictionary.getOres("ingotArdite").get(0);
			arditeStack.stackSize = 1;
			RecipeHandler.addRecipe(new AlloySmelterRecipe(cobaltStack, arditeStack, manyullynStack, 200, 16));
			RecipeHandler.addRecipe(
					new AlloySmelterRecipe(cobaltStack, ItemDusts.getDustByName("ardite", 1), manyullynStack, 200, 16));
			RecipeHandler.addRecipe(
					new AlloySmelterRecipe(ItemDusts.getDustByName("cobalt", 1), arditeStack, manyullynStack, 200, 16));
			RecipeHandler.addRecipe(
					new AlloySmelterRecipe(ItemDusts.getDustByName("cobalt", 1), ItemDusts.getDustByName("ardite", 1),
							manyullynStack, 200, 16));
		}

		// Conductive Iron
		if (OreUtil.doesOreExistAndValid("ingotConductiveIron"))
		{
			ItemStack conductiveIronStack = OreDictionary.getOres("ingotConductiveIron").get(0);
			conductiveIronStack.stackSize = 1;
			RecipeHandler.addRecipe(
					new AlloySmelterRecipe(new ItemStack(Items.REDSTONE, 1), new ItemStack(Items.IRON_INGOT, 1),
							conductiveIronStack, 200, 16));
		}

		// Redstone Alloy
		if (OreUtil.doesOreExistAndValid("ingotRedstoneAlloy") && OreUtil.doesOreExistAndValid("itemSilicon"))
		{
			ItemStack redstoneAlloyStack = OreDictionary.getOres("ingotRedstoneAlloy").get(0);
			redstoneAlloyStack.stackSize = 1;
			ItemStack siliconStack = OreDictionary.getOres("itemSilicon").get(0);
			siliconStack.stackSize = 1;
			RecipeHandler.addRecipe(
					new AlloySmelterRecipe(new ItemStack(Items.REDSTONE, 1), siliconStack, redstoneAlloyStack, 200,
							16));
		}

		// Pulsating Iron
		if (OreUtil.doesOreExistAndValid("ingotPhasedIron"))
		{
			ItemStack pulsatingIronStack = OreDictionary.getOres("ingotPhasedIron").get(0);
			pulsatingIronStack.stackSize = 1;
			RecipeHandler.addRecipe(
					new AlloySmelterRecipe(new ItemStack(Items.IRON_INGOT, 1), new ItemStack(Items.ENDER_PEARL, 1),
							pulsatingIronStack, 200, 16));
			RecipeHandler.addRecipe(
					new AlloySmelterRecipe(new ItemStack(Items.IRON_INGOT, 1), ItemDusts.getDustByName("enderPearl", 1),
							pulsatingIronStack, 200, 16));
		}

		// Vibrant Alloy
		if (OreUtil.doesOreExistAndValid("ingotEnergeticAlloy") && OreUtil.doesOreExistAndValid("ingotPhasedGold"))
		{
			ItemStack energeticAlloyStack = OreDictionary.getOres("ingotEnergeticAlloy").get(0);
			energeticAlloyStack.stackSize = 1;
			ItemStack vibrantAlloyStack = OreDictionary.getOres("ingotPhasedGold").get(0);
			vibrantAlloyStack.stackSize = 1;
			RecipeHandler.addRecipe(
					new AlloySmelterRecipe(energeticAlloyStack, new ItemStack(Items.ENDER_PEARL, 1), vibrantAlloyStack,
							200, 16));
			RecipeHandler.addRecipe(
					new AlloySmelterRecipe(energeticAlloyStack, ItemDusts.getDustByName("enderPearl", 1),
							vibrantAlloyStack, 200, 16));
		}

		// Soularium
		if (OreUtil.doesOreExistAndValid("ingotSoularium"))
		{
			ItemStack soulariumStack = OreDictionary.getOres("ingotSoularium").get(0);
			soulariumStack.stackSize = 1;
			RecipeHandler.addRecipe(
					new AlloySmelterRecipe(new ItemStack(Blocks.SOUL_SAND, 1), new ItemStack(Items.GOLD_INGOT, 1),
							soulariumStack, 200, 16));
		}

	}

	static void addPlateCuttingMachineRecipes()
	{

		for (String ore : OreUtil.oreNames)
		{
			if (OreUtil.hasBlock(ore) && OreUtil.hasPlate(ore))
			{
				RecipeHandler.addRecipe(new PlateCuttingMachineRecipe(
						OreUtil.getStackFromName("block" + StringUtils.toFirstCapital(ore)),
						OreUtil.getStackFromName("plate" + StringUtils.toFirstCapital(ore), 9), 200, 16));
			}
		}

		// Obsidian
		RecipeHandler.addRecipe(
				new PlateCuttingMachineRecipe(new ItemStack(Blocks.OBSIDIAN), ItemPlates.getPlateByName("obsidian", 9),
						100, 4));
	}

	static void addBlastFurnaceRecipes()
	{
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
						ItemIngots.getIngotByName("steel"), ItemDusts.getDustByName("darkAshes", 2), 500, 120, 1000));

		RecipeHandler.addRecipe(
				new BlastFurnaceRecipe(ItemIngots.getIngotByName("tungsten"), ItemIngots.getIngotByName("steel"),
						ItemIngots.getIngotByName("hotTungstensteel"), ItemDusts.getDustByName("darkAshes", 4), 500,
						500, 3000));

		RecipeHandler.addRecipe(
				new BlastFurnaceRecipe(new ItemStack(Blocks.IRON_ORE), ItemDusts.getDustByName("calcite"),
						new ItemStack(Items.IRON_INGOT, 3), ItemDusts.getDustByName("darkAshes"), 140, 120, 1000));

		RecipeHandler.addRecipe(
				new BlastFurnaceRecipe(BlockOre.getOreByName("Pyrite"), ItemDusts.getDustByName("calcite"),
						new ItemStack(Items.IRON_INGOT, 2), ItemDusts.getDustByName("darkAshes"), 140, 120, 1000));
	}

	static void addUUrecipes()
	{

		if (ConfigTechReborn.UUrecipesWood)
			CraftingHelper
					.addShapedOreRecipe(new ItemStack(Blocks.LOG, 8), " U ", "   ", "   ", 'U', ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesStone)
			CraftingHelper
					.addShapedOreRecipe(new ItemStack(Blocks.STONE, 16), "   ", " U ", "   ", 'U', ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesSnowBlock)
			CraftingHelper
					.addShapedOreRecipe(new ItemStack(Blocks.SNOW, 16), "U U", "   ", "   ", 'U', ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesGrass)
			CraftingHelper
					.addShapedOreRecipe(new ItemStack(Blocks.GRASS, 16), "   ", "U  ", "U  ", 'U', ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesObsidian)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Blocks.OBSIDIAN, 12), "U U", "U U", "   ", 'U',
					ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesGlass)
			CraftingHelper
					.addShapedOreRecipe(new ItemStack(Blocks.GLASS, 32), " U ", "U U", " U ", 'U', ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesCocoa)
			CraftingHelper
					.addShapedOreRecipe(new ItemStack(Items.DYE, 32, 3), "UU ", "  U", "UU ", 'U', ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesGlowstoneBlock)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Blocks.GLOWSTONE, 8), " U ", "U U", "UUU", 'U',
					ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesCactus)
			CraftingHelper
					.addShapedOreRecipe(new ItemStack(Blocks.CACTUS, 48), " U ", "UUU", "U U", 'U', ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesSugarCane)
			CraftingHelper
					.addShapedOreRecipe(new ItemStack(Items.REEDS, 48), "U U", "U U", "U U", 'U', ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesVine)
			CraftingHelper
					.addShapedOreRecipe(new ItemStack(Blocks.VINE, 24), "U  ", "U  ", "U  ", 'U', ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesSnowBall)
			CraftingHelper
					.addShapedOreRecipe(new ItemStack(Items.SNOWBALL, 16), "   ", "   ", "UUU", 'U', ModItems.uuMatter);

		CraftingHelper
				.addShapedOreRecipe(new ItemStack(Items.CLAY_BALL, 48), "UU ", "U  ", "UU ", 'U', ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipeslilypad)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Blocks.WATERLILY, 64), "U U", " U ", " U ", 'U',
					ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesGunpowder)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Items.GUNPOWDER, 15), "UUU", "U  ", "UUU", 'U',
					ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesBone)
			CraftingHelper
					.addShapedOreRecipe(new ItemStack(Items.BONE, 32), "U  ", "UU ", "U  ", 'U', ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesFeather)
			CraftingHelper
					.addShapedOreRecipe(new ItemStack(Items.FEATHER, 32), " U ", " U ", "U U", 'U', ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesInk)
			CraftingHelper
					.addShapedOreRecipe(new ItemStack(Items.DYE, 48), " UU", " UU", " U ", 'U', ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesEnderPearl)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Items.ENDER_PEARL, 1), "UUU", "U U", " U ", 'U',
					ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesCoal)
			CraftingHelper
					.addShapedOreRecipe(new ItemStack(Items.COAL, 5), "  U", "U  ", "  U", 'U', ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesIronOre)
			CraftingHelper
					.addShapedOreRecipe(new ItemStack(Blocks.IRON_ORE, 2), "U U", " U ", "U U", 'U', ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesGoldOre)
			CraftingHelper
					.addShapedOreRecipe(new ItemStack(Blocks.GOLD_ORE, 2), " U ", "UUU", " U ", 'U', ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesRedStone)
			CraftingHelper
					.addShapedOreRecipe(new ItemStack(Items.REDSTONE, 24), "   ", " U ", "UUU", 'U', ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesLapis)
			CraftingHelper
					.addShapedOreRecipe(new ItemStack(Items.DYE, 9, 4), " U ", " U ", " UU", 'U', ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesEmeraldOre)
			CraftingHelper.addShapedOreRecipe(new ItemStack(Blocks.EMERALD_ORE, 1), "UU ", "U U", " UU", 'U',
					ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesEmerald)
			CraftingHelper
					.addShapedOreRecipe(new ItemStack(Items.EMERALD, 2), "UUU", "UUU", " U ", 'U', ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesDiamond)
			CraftingHelper
					.addShapedOreRecipe(new ItemStack(Items.DIAMOND, 1), "UUU", "UUU", "UUU", 'U', ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesTinDust)
			CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.dusts, 10, 77), "   ", "U U", "  U", 'U',
					ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesCopperDust)
			CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.dusts, 10, 21), "  U", "U U", "   ", 'U',
					ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesLeadDust)
			CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.dusts, 14, 42), "UUU", "UUU", "U  ", 'U',
					ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesPlatinumDust)
			CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.dusts, 1, 58), "  U", "UUU", "UUU", 'U',
					ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesTungstenDust)
			CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.dusts, 1, 79), "U  ", "UUU", "UUU", 'U',
					ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesTitaniumDust)
			CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.dusts, 2, 78), "UUU", " U ", " U ", 'U',
					ModItems.uuMatter);

		if (ConfigTechReborn.UUrecipesAluminumDust)
			CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.dusts, 16, 2), " U ", " U ", "UUU", 'U',
					ModItems.uuMatter);

		if (ConfigTechReborn.HideUuRecipes)
			hideUUrecipes();

	}

	static void hideUUrecipes()
	{
		// TODO
	}

	static void addIndustrialCentrifugeRecipes()
	{

		// Mycelium Byproducts
		RecipeHandler.addRecipe(
				new CentrifugeRecipe(new ItemStack(Blocks.MYCELIUM, 8), null, new ItemStack(Blocks.BROWN_MUSHROOM, 2),
						new ItemStack(Blocks.RED_MUSHROOM, 2), new ItemStack(Items.CLAY_BALL, 1),
						new ItemStack(Blocks.SAND, 4), 1640, 5));

		// Blaze Powder Byproducts
		RecipeHandler.addRecipe(
				new CentrifugeRecipe(new ItemStack(Items.BLAZE_POWDER), null, ItemDusts.getDustByName("darkAshes", 1),
						ItemDusts.getDustByName("sulfur", 1), null, null, 1240, 5));

		// Magma Cream Products
		RecipeHandler.addRecipe(
				new CentrifugeRecipe(new ItemStack(Items.MAGMA_CREAM, 1), null, new ItemStack(Items.BLAZE_POWDER, 1),
						new ItemStack(Items.SLIME_BALL, 1), null, null, 2500, 5));

		// Dust Byproducts
		// RecipeHandler.addRecipe(new
		// CentrifugeRecipe(ItemDusts.getDustByName("platinum", 1), null,
		// ItemDustsTiny.getTinyDustByName("Iridium", 1),
		// ItemDustsSmall.getSmallDustByName("Nickel", 1), null, null, 3000,
		// 5));
		RecipeHandler.addRecipe(
				new CentrifugeRecipe(ItemDusts.getDustByName("electrum", 2), null, ItemDusts.getDustByName("silver", 1),
						ItemDusts.getDustByName("gold", 1), null, null, 2400, 5));
		RecipeHandler.addRecipe(
				new CentrifugeRecipe(ItemDusts.getDustByName("invar", 3), null, ItemDusts.getDustByName("iron", 2),
						ItemDusts.getDustByName("nickel", 1), null, null, 1340, 5));
		RecipeHandler.addRecipe(new CentrifugeRecipe(ItemDusts.getDustByName("marble", 8), null,
				ItemDusts.getDustByName("magnesium", 1), ItemDusts.getDustByName("calcite", 7), null, null, 1280, 5));
		//		Deprecated
		//		RecipeHandler.addRecipe(
		//				new CentrifugeRecipe(ItemDusts.getDustByName("redrock", 4), null, ItemDusts.getDustByName("calcite", 2),
		//						ItemDusts.getDustByName("flint", 1), ItemDusts.getDustByName("clay", 1), null, 640, 5));
		RecipeHandler.addRecipe(
				new CentrifugeRecipe(ItemDusts.getDustByName("basalt", 16), null, ItemDusts.getDustByName("peridot", 1),
						ItemDusts.getDustByName("calcite", 3), ItemDusts.getDustByName("magnesium", 8),
						ItemDusts.getDustByName("darkAshes", 4), 2680, 5));
		RecipeHandler.addRecipe(new CentrifugeRecipe(ItemDusts.getDustByName("yellowGarnet", 16), null,
				ItemDusts.getDustByName("andradite", 5), ItemDusts.getDustByName("grossular", 8),
				ItemDusts.getDustByName("uvarovite", 3), null, 2940, 5));
		RecipeHandler.addRecipe(new CentrifugeRecipe(ItemDusts.getDustByName("redGarnet", 16), null,
				ItemDusts.getDustByName("pyrope", 3), ItemDusts.getDustByName("almandine", 5),
				ItemDusts.getDustByName("spessartine", 8), null, 2940, 5));
		RecipeHandler.addRecipe(
				new CentrifugeRecipe(ItemDusts.getDustByName("darkAshes", 2), null, ItemDusts.getDustByName("ashes", 2),
						null, null, null, 240, 5));
		RecipeHandler.addRecipe(
				new CentrifugeRecipe(ItemDusts.getDustByName("brass", 4), null, ItemDusts.getDustByName("zinc", 1),
						ItemDusts.getDustByName("copper", 3), null, null, 2000, 5));
		RecipeHandler.addRecipe(
				new CentrifugeRecipe(ItemDusts.getDustByName("bronze", 4), null, ItemDusts.getDustByName("tin", 1),
						ItemDusts.getDustByName("copper", 3), null, null, 2420, 5));
		RecipeHandler.addRecipe(
				new CentrifugeRecipe(ItemDusts.getDustByName("netherrack", 16), null, new ItemStack(Items.REDSTONE, 1),
						ItemDusts.getDustByName("sulfur", 4), ItemDusts.getDustByName("basalt", 1),
						new ItemStack(Items.GOLD_NUGGET, 1), 2400, 5));
		RecipeHandler.addRecipe(new CentrifugeRecipe(ItemDusts.getDustByName("enderEye", 1), null,
				ItemDusts.getDustByName("enderPearl", 1), new ItemStack(Items.BLAZE_POWDER, 1), null, null, 1280, 5));

		RecipeHandler.addRecipe(
				new CentrifugeRecipe(new ItemStack(Items.GLOWSTONE_DUST, 16), RecipeUtils.getEmptyCell(1),
						ItemCells.getCellByName("helium", 1, false), ItemDusts.getDustByName("gold", 8),
						new ItemStack(Items.REDSTONE), null, 25000, 20));
	}

	static void addIndustrialGrinderRecipes()
	{
		for (String ore : OreUtil.oreNames)
		{
			if (OreUtil.hasIngot(ore) && OreUtil.hasDustSmall(ore) && OreUtil.hasBlock(ore))
			{
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(OreUtil.getStackFromName("block" + StringUtils.toFirstCapital(ore)),
								new FluidStack(FluidRegistry.WATER, 1000),
								OreUtil.getStackFromName("ingot" + StringUtils.toFirstCapital(ore)),
								OreUtil.getStackFromName("dustSmall" + StringUtils.toFirstCapital(ore), 6),
								OreUtil.getStackFromName("dustSmall" + StringUtils.toFirstCapital(ore), 2), null, 100,
								120));
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(OreUtil.getStackFromName("block" + StringUtils.toFirstCapital(ore)),
								new FluidStack(FluidRegistry.WATER, 1000),
								OreUtil.getStackFromName("ingot" + StringUtils.toFirstCapital(ore)),
								OreUtil.getStackFromName("dustSmall" + StringUtils.toFirstCapital(ore), 6),
								OreUtil.getStackFromName("dustSmall" + StringUtils.toFirstCapital(ore), 2),
								new ItemStack(Items.BUCKET), 100, 120));
			}
		}

		// Copper Ore
		if (OreUtil.doesOreExistAndValid("oreCopper"))
		{
			try
			{
				ItemStack oreStack = OreDictionary.getOres("oreCopper").get(0);

				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack, new FluidStack(FluidRegistry.WATER, 1000),
								ItemDusts.getDustByName("copper", 2), ItemDustsSmall.getSmallDustByName("Gold", 1),
								ItemDustsSmall.getSmallDustByName("Nickel", 1), null, 100, 120));

				RecipeHandler.addRecipe(new IndustrialGrinderRecipe(oreStack,
						new FluidStack(ModFluids.fluidSodiumpersulfate, 1000), ItemDusts.getDustByName("copper", 2),
						ItemDusts.getDustByName("gold", 1), ItemDustsSmall.getSmallDustByName("Nickel", 1), null, 100,
						120));

				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack, new FluidStack(ModFluids.fluidMercury, 1000),
								ItemDusts.getDustByName("copper", 2), ItemDustsSmall.getSmallDustByName("Gold", 1),
								ItemDusts.getDustByName("nickel", 1), null, 100, 120));

			} catch (Exception e)
			{
				Core.logHelper.info("Failed to Load Grinder Recipe for Copper Ore");
			}
		}

		// Tin Ore
		if (OreUtil.doesOreExistAndValid("oreTin"))
		{
			try
			{
				ItemStack oreStack = OreDictionary.getOres("oreTin").get(0);
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack, new FluidStack(FluidRegistry.WATER, 1000),
								ItemDusts.getDustByName("tin", 2), ItemDustsSmall.getSmallDustByName("Iron", 1),
								ItemDustsSmall.getSmallDustByName("Zinc", 1), null, 100, 120));
				RecipeHandler.addRecipe(new IndustrialGrinderRecipe(oreStack, new FluidStack(FluidRegistry.WATER, 1000),
						ItemDusts.getDustByName("tin", 2), ItemDustsSmall.getSmallDustByName("Iron", 1),
						ItemDustsSmall.getSmallDustByName("Zinc", 1), new ItemStack(Items.BUCKET), 100, 120));

				RecipeHandler.addRecipe(new IndustrialGrinderRecipe(oreStack,
						new FluidStack(ModFluids.fluidSodiumpersulfate, 1000), ItemDusts.getDustByName("tin", 2),
						ItemDustsSmall.getSmallDustByName("Iron", 1), ItemDusts.getDustByName("zinc", 1), null, 100,
						120));

			} catch (Exception e)
			{
				Core.logHelper.info("Failed to Load Grinder Recipe for Tin Ore");
			}
		}

		// Nickel Ore
		if (OreUtil.doesOreExistAndValid("oreNickel"))
		{
			try
			{
				ItemStack oreStack = OreDictionary.getOres("oreNickel").get(0);
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack, new FluidStack(FluidRegistry.WATER, 1000),
								ItemDusts.getDustByName("nickel", 2), ItemDustsSmall.getSmallDustByName("Iron", 1),
								ItemDustsSmall.getSmallDustByName("Platinum", 1), null, 100, 120));
				RecipeHandler.addRecipe(new IndustrialGrinderRecipe(oreStack, new FluidStack(FluidRegistry.WATER, 1000),
						ItemDusts.getDustByName("nickel", 2), ItemDustsSmall.getSmallDustByName("Iron", 1),
						ItemDustsSmall.getSmallDustByName("Platinum", 1), new ItemStack(Items.BUCKET), 100, 120));

				RecipeHandler.addRecipe(new IndustrialGrinderRecipe(oreStack,
						new FluidStack(ModFluids.fluidSodiumpersulfate, 1000), ItemDusts.getDustByName("nickel", 3),
						ItemDustsSmall.getSmallDustByName("Iron", 1), ItemDustsSmall.getSmallDustByName("Platinum", 1),
						null, 100, 120));

				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack, new FluidStack(ModFluids.fluidMercury, 1000),
								ItemDusts.getDustByName("nickel", 2), ItemDustsSmall.getSmallDustByName("Iron", 1),
								ItemDusts.getDustByName("platinum", 1), null, 100, 120));
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack, new FluidStack(ModFluids.fluidMercury, 1000),
								ItemDusts.getDustByName("nickel", 2), ItemDustsSmall.getSmallDustByName("Iron", 1),
								ItemDusts.getDustByName("platinum", 1), new ItemStack(Items.BUCKET), 100, 120));
			} catch (Exception e)
			{
				Core.logHelper.info("Failed to Load Grinder Recipe for Nickel Ore");
			}
		}

		// Zinc Ore
		if (OreUtil.doesOreExistAndValid("oreZinc"))
		{
			try
			{
				ItemStack oreStack = OreDictionary.getOres("oreZinc").get(0);
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack, new FluidStack(FluidRegistry.WATER, 1000),
								ItemDusts.getDustByName("zinc", 2), ItemDustsSmall.getSmallDustByName("Iron", 1),
								ItemDustsSmall.getSmallDustByName("Tin", 1), null, 100, 120));
				RecipeHandler.addRecipe(new IndustrialGrinderRecipe(oreStack, new FluidStack(FluidRegistry.WATER, 1000),
						ItemDusts.getDustByName("zinc", 2), ItemDustsSmall.getSmallDustByName("Iron", 1),
						ItemDustsSmall.getSmallDustByName("Tin", 1), new ItemStack(Items.BUCKET), 100, 120));

				RecipeHandler.addRecipe(new IndustrialGrinderRecipe(oreStack,
						new FluidStack(ModFluids.fluidSodiumpersulfate, 1000), ItemDusts.getDustByName("zinc", 2),
						ItemDustsSmall.getSmallDustByName("Iron", 1), ItemDusts.getDustByName("iron", 1), null, 100,
						120));

			} catch (Exception e)
			{
				Core.logHelper.info("Failed to Load Grinder Recipe for Zinc Ore");
			}
		}

		// Silver Ore
		if (OreUtil.doesOreExistAndValid("oreSilver"))
		{
			try
			{
				ItemStack oreStack = OreDictionary.getOres("oreSilver").get(0);
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack, new FluidStack(FluidRegistry.WATER, 1000),
								ItemDusts.getDustByName("silver", 2), ItemDustsSmall.getSmallDustByName("Lead", 1),
								ItemDustsSmall.getSmallDustByName("Sulfur", 1), null, 100, 120));
				RecipeHandler.addRecipe(new IndustrialGrinderRecipe(oreStack, new FluidStack(FluidRegistry.WATER, 1000),
						ItemDusts.getDustByName("silver", 2), ItemDustsSmall.getSmallDustByName("Lead", 1),
						ItemDustsSmall.getSmallDustByName("Sulfur", 1), new ItemStack(Items.BUCKET), 100, 120));

				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack, new FluidStack(ModFluids.fluidMercury, 1000),
								ItemDusts.getDustByName("silver", 3), ItemDustsSmall.getSmallDustByName("Lead", 1),
								ItemDustsSmall.getSmallDustByName("Sulfur", 1), null, 100, 120));
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack, new FluidStack(ModFluids.fluidMercury, 1000),
								ItemDusts.getDustByName("silver", 3), ItemDustsSmall.getSmallDustByName("Lead", 1),
								ItemDustsSmall.getSmallDustByName("Sulfur", 1), new ItemStack(Items.BUCKET), 100, 120));
			} catch (Exception e)
			{
				Core.logHelper.info("Failed to Load Grinder Recipe for Silver Ore");
			}
		}

		// Lead Ore
		if (OreUtil.doesOreExistAndValid("oreLead"))
		{
			try
			{
				ItemStack oreStack = OreDictionary.getOres("oreLead").get(0);
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack, new FluidStack(FluidRegistry.WATER, 1000),
								ItemDusts.getDustByName("lead", 2), ItemDustsSmall.getSmallDustByName("Silver", 1),
								ItemDustsSmall.getSmallDustByName("Sulfur", 1), null, 100, 120));
				RecipeHandler.addRecipe(new IndustrialGrinderRecipe(oreStack, new FluidStack(FluidRegistry.WATER, 1000),
						ItemDusts.getDustByName("lead", 2), ItemDustsSmall.getSmallDustByName("Silver", 1),
						ItemDustsSmall.getSmallDustByName("Sulfur", 1), new ItemStack(Items.BUCKET), 100, 120));

				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack, new FluidStack(ModFluids.fluidMercury, 1000),
								ItemDusts.getDustByName("lead", 2), ItemDusts.getDustByName("silver", 1),
								ItemDustsSmall.getSmallDustByName("Sulfur", 1), null, 100, 120));
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack, new FluidStack(ModFluids.fluidMercury, 1000),
								ItemDusts.getDustByName("lead", 2), ItemDusts.getDustByName("silver", 1),
								ItemDustsSmall.getSmallDustByName("Sulfur", 1), new ItemStack(Items.BUCKET), 100, 120));
			} catch (Exception e)
			{
				Core.logHelper.info("Failed to Load Grinder Recipe for Lead Ore");
			}
		}

		// Apatite Ore
		if (OreUtil.doesOreExistAndValid("oreApatite") & OreUtil.doesOreExistAndValid("gemApatite"))
		{
			try
			{
				ItemStack oreStack = OreDictionary.getOres("oreApatite").get(0);
				ItemStack gemStack = OreDictionary.getOres("gemApatite").get(0);
				gemStack.stackSize = 6;
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack, new FluidStack(FluidRegistry.WATER, 1000), gemStack,
								gemStack, ItemDustsSmall.getSmallDustByName("Phosphorous", 4), null, 100, 120));
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack, new FluidStack(FluidRegistry.WATER, 1000), gemStack,
								gemStack, ItemDustsSmall.getSmallDustByName("Phosphorous", 4),
								new ItemStack(Items.BUCKET), 100, 120));
			} catch (Exception e)
			{
				Core.logHelper.info("Failed to Load Grinder Recipe for Apatite Ore");
			}
		}

		// Nether Quartz Ore
		if (OreUtil.doesOreExistAndValid("dustNetherQuartz"))
		{
			try
			{
				ItemStack dustStack = OreDictionary.getOres("dustNetherQuartz").get(0);
				dustStack.stackSize = 4;
				RecipeHandler.addRecipe(new IndustrialGrinderRecipe(new ItemStack(Blocks.QUARTZ_ORE, 1),
						new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(Items.QUARTZ, 2), dustStack,
						ItemDustsSmall.getSmallDustByName("Netherrack", 2), null, 100, 120));

			} catch (Exception e)
			{
				Core.logHelper.info("Failed to Load Grinder Recipe for Nether Quartz Ore");
			}
		}

		// Certus Quartz Ore
		if (OreUtil.doesOreExistAndValid("oreCertusQuartz"))
		{
			try
			{
				ItemStack oreStack = OreDictionary.getOres("oreCertusQuartz").get(0);
				ItemStack gemStack = OreDictionary.getOres("crystalCertusQuartz").get(0);
				ItemStack dustStack = OreDictionary.getOres("dustCertusQuartz").get(0);
				dustStack.stackSize = 2;
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack, new FluidStack(FluidRegistry.WATER, 1000), gemStack,
								dustStack, null, null, 100, 120));
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack, new FluidStack(FluidRegistry.WATER, 1000), gemStack,
								dustStack, null, new ItemStack(Items.BUCKET), 100, 120));
			} catch (Exception e)
			{
				Core.logHelper.info("Failed to Load Grinder Recipe for Certus Quartz Ore");
			}
		}

		// Charged Certus Quartz Ore
		if (OreUtil.doesOreExistAndValid("oreChargedCertusQuartz"))
		{
			try
			{
				ItemStack oreStack = OreDictionary.getOres("oreChargedCertusQuartz").get(0);
				ItemStack gemStack = OreDictionary.getOres("crystalChargedCertusQuartz").get(0);
				ItemStack dustStack = OreDictionary.getOres("dustCertusQuartz").get(0);
				dustStack.stackSize = 2;
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack, new FluidStack(FluidRegistry.WATER, 1000), gemStack,
								dustStack, null, null, 100, 120));
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack, new FluidStack(FluidRegistry.WATER, 1000), gemStack,
								dustStack, null, new ItemStack(Items.BUCKET), 100, 120));
			} catch (Exception e)
			{
				Core.logHelper.info("Failed to Load Grinder Recipe for Charged Certus Quartz Ore");
			}
		}

		// Amethyst Ore
		if (OreUtil.doesOreExistAndValid("oreAmethyst") && OreUtil.doesOreExistAndValid("gemAmethyst"))
		{
			try
			{
				ItemStack oreStack = OreDictionary.getOres("oreAmethyst").get(0);
				ItemStack gemStack = OreDictionary.getOres("gemAmethyst").get(0);
				gemStack.stackSize = 2;
				ItemStack dustStack = OreDictionary.getOres("gemAmethyst").get(0);
				dustStack.stackSize = 1;
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack, new FluidStack(FluidRegistry.WATER, 1000), gemStack,
								dustStack, null, null, 100, 120));
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack, new FluidStack(FluidRegistry.WATER, 1000), gemStack,
								dustStack, null, new ItemStack(Items.BUCKET), 100, 120));
			} catch (Exception e)
			{
				Core.logHelper.info("Failed to Load Grinder Recipe for Certus Quartz Ore");
			}
		}

		// Topaz Ore
		if (OreUtil.doesOreExistAndValid("oreTopaz") && OreUtil.doesOreExistAndValid("gemTopaz"))
		{
			try
			{
				ItemStack oreStack = OreDictionary.getOres("oreTopaz").get(0);
				ItemStack gemStack = OreDictionary.getOres("gemTopaz").get(0);
				gemStack.stackSize = 2;
				ItemStack dustStack = OreDictionary.getOres("gemTopaz").get(0);
				dustStack.stackSize = 1;
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack, new FluidStack(FluidRegistry.WATER, 1000), gemStack,
								dustStack, null, null, 100, 120));
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack, new FluidStack(FluidRegistry.WATER, 1000), gemStack,
								dustStack, null, new ItemStack(Items.BUCKET), 100, 120));
			} catch (Exception e)
			{
				Core.logHelper.info("Failed to Load Grinder Recipe for Topaz Ore");
			}
		}

		// Tanzanite Ore
		if (OreUtil.doesOreExistAndValid("oreTanzanite") && OreUtil.doesOreExistAndValid("gemTanzanite"))
		{
			try
			{
				ItemStack oreStack = OreDictionary.getOres("oreTanzanite").get(0);
				ItemStack gemStack = OreDictionary.getOres("gemTanzanite").get(0);
				gemStack.stackSize = 2;
				ItemStack dustStack = OreDictionary.getOres("gemTanzanite").get(0);
				dustStack.stackSize = 1;
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack, new FluidStack(FluidRegistry.WATER, 1000), gemStack,
								dustStack, null, null, 100, 120));
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack, new FluidStack(FluidRegistry.WATER, 1000), gemStack,
								dustStack, null, new ItemStack(Items.BUCKET), 100, 120));
			} catch (Exception e)
			{
				Core.logHelper.info("Failed to Load Grinder Recipe for Tanzanite Ore");
			}
		}

		// Malachite Ore
		if (OreUtil.doesOreExistAndValid("oreMalachite") && OreUtil.doesOreExistAndValid("gemMalachite"))
		{
			try
			{
				ItemStack oreStack = OreDictionary.getOres("oreMalachite").get(0);
				ItemStack gemStack = OreDictionary.getOres("gemMalachite").get(0);
				gemStack.stackSize = 2;
				ItemStack dustStack = OreDictionary.getOres("gemMalachite").get(0);
				dustStack.stackSize = 1;
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack, new FluidStack(FluidRegistry.WATER, 1000), gemStack,
								dustStack, null, null, 100, 120));
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack, new FluidStack(FluidRegistry.WATER, 1000), gemStack,
								dustStack, null, new ItemStack(Items.BUCKET), 100, 120));
			} catch (Exception e)
			{
				Core.logHelper.info("Failed to Load Grinder Recipe for Malachite Ore");
			}
		}

		// Galena Ore
		RecipeHandler.addRecipe(new IndustrialGrinderRecipe(new ItemStack(ModBlocks.ore, 1, 0), 
                new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("galena", 2),
				ItemDustsSmall.getSmallDustByName("Sulfur", 1), ItemDustsSmall.getSmallDustByName("Silver", 1), null,
				100, 120));

		RecipeHandler.addRecipe(new IndustrialGrinderRecipe(new ItemStack(ModBlocks.ore, 1, 0),
                new FluidStack(ModFluids.fluidMercury, 1000), ItemDusts.getDustByName("galena", 2),
				ItemDustsSmall.getSmallDustByName("Sulfur", 1), ItemDusts.getDustByName("silver", 1), null, 100, 120));


		// Ruby Ore
		RecipeHandler.addRecipe(new IndustrialGrinderRecipe(new ItemStack(ModBlocks.ore, 1, 2), 
                new FluidStack(FluidRegistry.WATER, 1000), ItemGems.getGemByName("ruby", 1),
				ItemDustsSmall.getSmallDustByName("Ruby", 6), ItemDustsSmall.getSmallDustByName("Chrome", 2), null, 100,
				120));

		// Sapphire Ore
		RecipeHandler.addRecipe(new IndustrialGrinderRecipe(new ItemStack(ModBlocks.ore, 1, 3),
				new FluidStack(FluidRegistry.WATER, 1000), ItemGems.getGemByName("sapphire", 1),
				ItemDustsSmall.getSmallDustByName("Sapphire", 6), ItemDustsSmall.getSmallDustByName("Aluminum", 2),
				null, 100, 120));

		// Bauxite Ore
		RecipeHandler.addRecipe(new IndustrialGrinderRecipe(new ItemStack(ModBlocks.ore, 1, 4),
				new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("bauxite", 2),
				ItemDustsSmall.getSmallDustByName("Grossular", 4), ItemDustsSmall.getSmallDustByName("Titanium", 4),
				null, 100, 120));

		// Pyrite Ore
		RecipeHandler.addRecipe(new IndustrialGrinderRecipe(new ItemStack(ModBlocks.ore, 1, 5),
				new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("pyrite", 2),
				ItemDustsSmall.getSmallDustByName("Sulfur", 1), ItemDustsSmall.getSmallDustByName("Phosphorous", 1),
				null, 100, 120));

		// Cinnabar Ore
		RecipeHandler.addRecipe(new IndustrialGrinderRecipe(new ItemStack(ModBlocks.ore, 1, 6),
				new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("cinnabar", 2),
				ItemDustsSmall.getSmallDustByName("Redstone", 1), ItemDustsSmall.getSmallDustByName("Glowstone", 1),
				null, 100, 120));

		// Sphalerite Ore
		RecipeHandler.addRecipe(new IndustrialGrinderRecipe(new ItemStack(ModBlocks.ore, 1, 7),
				new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("sphalerite", 2),
				ItemDustsSmall.getSmallDustByName("Zinc", 1), ItemDustsSmall.getSmallDustByName("YellowGarnet", 1),
				null, 100, 120));

		RecipeHandler.addRecipe(new IndustrialGrinderRecipe(new ItemStack(ModBlocks.ore, 1, 7),
				new FluidStack(ModFluids.fluidSodiumpersulfate, 1000), ItemDusts.getDustByName("sphalerite", 2),
				ItemDusts.getDustByName("zinc", 1), ItemDustsSmall.getSmallDustByName("YellowGarnet", 1), null, 100,
				120));

		// Tungsten Ore
		RecipeHandler.addRecipe(
				new IndustrialGrinderRecipe(new ItemStack(ModBlocks.ore, 1, 8), new FluidStack(FluidRegistry.WATER, 1000),
						ItemDusts.getDustByName("tungsten", 2), ItemDustsSmall.getSmallDustByName("Manganese", 1),
						ItemDustsSmall.getSmallDustByName("Silver", 1), null, 100, 120));

		RecipeHandler.addRecipe(new IndustrialGrinderRecipe(new ItemStack(ModBlocks.ore, 1, 8),
				new FluidStack(ModFluids.fluidMercury, 1000), ItemDusts.getDustByName("tungsten", 2),
				ItemDustsSmall.getSmallDustByName("Manganese", 1), ItemDusts.getDustByName("silver", 2), null, 100,
				120));


		// Sheldonite Ore
		RecipeHandler.addRecipe(new IndustrialGrinderRecipe(new ItemStack(ModBlocks.ore, 1, 9),
				new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("platinum", 2),
				ItemNuggets.getNuggetByName("iridium", 2), ItemDusts.getDustByName("nickel", 1), null, 100, 120));

		RecipeHandler.addRecipe(new IndustrialGrinderRecipe(new ItemStack(ModBlocks.ore, 1, 9),
				new FluidStack(ModFluids.fluidMercury, 1000), ItemDusts.getDustByName("platinum", 3),
				ItemNuggets.getNuggetByName("iridium", 2), ItemDusts.getDustByName("nickel", 1), null, 100, 120));

		// Peridot Ore
		RecipeHandler.addRecipe(new IndustrialGrinderRecipe(new ItemStack(ModBlocks.ore, 1, 10),
				new FluidStack(FluidRegistry.WATER, 1000), ItemGems.getGemByName("peridot", 1),
				ItemDustsSmall.getSmallDustByName("Peridot", 6), ItemDustsSmall.getSmallDustByName("Pyrope", 2), null,
				100, 120));
		// Sodalite Ore
		RecipeHandler.addRecipe(new IndustrialGrinderRecipe(new ItemStack(ModBlocks.ore, 1, 11),
				new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("sodalite", 12),
				ItemDusts.getDustByName("aluminum", 3), null, null, 100, 120));
	}

	static void addImplosionCompressorRecipes()
	{
	}

	static void addChemicalReactorRecipes()
	{
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

	static void addIndustrialElectrolyzerRecipes()
	{

		RecipeHandler.addRecipe(new IndustrialElectrolyzerRecipe(ItemCells.getCellByName("nitrocarbon", 2), null,
				ItemCells.getCellByName("nitrogen"), ItemCells.getCellByName("carbon"), null, null, 80, 60));

		RecipeHandler.addRecipe(new IndustrialElectrolyzerRecipe(ItemDusts.getDustByName("pyrite", 3), null,
				ItemDusts.getDustByName("iron"), ItemDusts.getDustByName("sulfur", 2), null, null, 120, 128));

		RecipeHandler.addRecipe(new IndustrialElectrolyzerRecipe(ItemDusts.getDustByName("sphalerite", 2), null,
				ItemDusts.getDustByName("zinc"), ItemDusts.getDustByName("sulfur"), null, null, 150, 100));

	}

	static void addIc2Recipes()
	{
		CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.manual), ItemIngots.getIngotByName("refinedIron"),
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

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.lapotronicOrb), "LLL", "LPL", "LLL", 'L',
				"lapotronCrystal", 'P', "plateIridium");

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.industrialSawmill), "PAP", "SSS", "ACA", 'P',
				ItemIngots.getIngotByName("refinedIron"), 'A', "circuitAdvanced",
				'S', ItemParts.getPartByName("diamondSawBlade"), 'C',
				"machineBlockAdvanced");

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.ComputerCube), "DME", "MAM", "EMD", 'E',
				"circuitMaster", 'D', ItemParts.getPartByName("dataOrb"), 'M',
				ItemParts.getPartByName("computerMonitor"), 'A',
				"machineBlockAdvanced");

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.chargeBench), "ETE", "COC", "EAD", 'E',
				"circuitMaster", 'T', ModBlocks.ComputerCube, 'C', Blocks.CHEST, 'O',
				ModItems.lapotronicOrb, 'A', "machineBlockAdvanced");

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.MatterFabricator), "ETE", "AOA", "ETE", 'E',
				"circuitMaster", 'T', ModBlocks.Extractor, 'A',
				BlockMachineFrame.getFrameByName("highlyAdvancedMachine", 1), 'O', ModItems.lapotronicOrb);

		CraftingHelper
				.addShapedOreRecipe(new ItemStack(ModBlocks.heatGenerator), "III", "IHI", "CGC", 'I', "plateIron", 'H',
						new ItemStack(Blocks.IRON_BARS), 'C', "circuitBasic", 'G', ModBlocks.Generator);

		CraftingHelper
				.addShapedOreRecipe(new ItemStack(ModBlocks.Gasturbine), "IAI", "WGW", "IAI", 'I', "ingotInvar", 'A',
						"circuitAdvanced", 'W',
						getOre("ic2Windmill"), 'G',
						getOre("glassReinforced"));

		CraftingHelper
				.addShapedOreRecipe(new ItemStack(ModBlocks.Gasturbine), "IAI", "WGW", "IAI", 'I', "ingotAluminum", 'A',
						"circuitAdvanced", 'W',
						getOre("ic2Windmill"), 'G',
						getOre("glassReinforced"));

		CraftingHelper
				.addShapedOreRecipe(new ItemStack(ModBlocks.Semifluidgenerator), "III", "IHI", "CGC", 'I', "plateIron",
						'H', ModBlocks.reinforcedglass, 'C', "circuitBasic", 'G',
						ModBlocks.Generator);

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Semifluidgenerator), "AAA", "AHA", "CGC", 'A',
				"plateAluminum", 'H', ModBlocks.reinforcedglass, 'C', "circuitBasic", 'G',
				ModBlocks.Generator);

		CraftingHelper
				.addShapedOreRecipe(new ItemStack(ModBlocks.DieselGenerator), "III", "I I", "CGC", 'I', "refinedIron",
						'C', "circuitBasic", 'G', ModBlocks.Generator);

		CraftingHelper
				.addShapedOreRecipe(new ItemStack(ModBlocks.DieselGenerator), "AAA", "A A", "CGC", 'A', "ingotAluminum",
						'C', "circuitBasic", 'G', ModBlocks.Generator);

		// CraftingHelper.addShapedOreRecipe(new
		// ItemStack(ModBlocks.MagicalAbsorber),
		// "CSC", "IBI", "CAC",
		// 'C', "circuitMaster",
		// 'S', "craftingSuperconductor",
		// 'B', Blocks.beacon,
		// 'A', ModBlocks.Magicenergeyconverter,
		// 'I', "plateIridium");
		//
		// CraftingHelper.addShapedOreRecipe(new
		// ItemStack(ModBlocks.Magicenergeyconverter),
		// "CTC", "PBP", "CLC",
		// 'C', "circuitAdvanced",
		// 'P', "platePlatinum",
		// 'B', Blocks.beacon,
		// 'L', "lapotronCrystal",
		// 'T', TechRebornAPI.recipeCompact.getItem("teleporter"));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Dragoneggenergysiphoner), "CTC", "ISI", "CBC", 'I',
				"plateIridium", 'C', "circuitBasic",
				'B', ModItems.lithiumBattery, 'S', ModBlocks.Supercondensator, 'T', ModBlocks.Extractor);

		CraftingHelper
				.addShapedOreRecipe(new ItemStack(ModBlocks.centrifuge), "SCS", "BEB", "SCS", 'S', "plateSteel", 'C',
						"circuitAdvanced", 'B', "machineBlockAdvanced", 'E',
						getOre("ic2Extractor"));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.BlastFurnace), "CHC", "HBH", "FHF", 'H',
				new ItemStack(ModItems.parts, 1, 17), 'C', "circuitAdvanced", 'B',
				BlockMachineFrame.getFrameByName("advancedMachine", 1), 'F', ModBlocks.ElectricFurnace);

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.IndustrialGrinder), "ECP", "GGG", "CBC", 'E',
				ModBlocks.IndustrialElectrolyzer, 'P', ModBlocks.Extractor, 'C',
				"circuitAdvanced", 'B', "machineBlockAdvanced",
				'G', ModBlocks.Grinder);

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.ImplosionCompressor), "ABA", "CPC", "ABA", 'A',
				ItemIngots.getIngotByName("advancedAlloy"), 'C', "circuitAdvanced", 'B',
				BlockMachineFrame.getFrameByName("advancedMachine", 1), 'P', ModBlocks.Compressor);

		CraftingHelper
				.addShapedOreRecipe(new ItemStack(ModBlocks.VacuumFreezer), "SPS", "CGC", "SPS", 'S', "plateSteel", 'C',
						"circuitAdvanced", 'G', ModBlocks.reinforcedglass, 'P',
						ModBlocks.Extractor);

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Distillationtower), "CMC", "PBP", "EME", 'E',
				ModBlocks.IndustrialElectrolyzer, 'M', "circuitMaster", 'B',
				"machineBlockAdvanced", 'C', ModBlocks.centrifuge, 'P',
				ModBlocks.Extractor);

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.AlloyFurnace), "III", "F F", "III", 'I',
				ItemIngots.getIngotByName("refinediron"), 'F', new ItemStack(ModBlocks.ironFurnace));

		CraftingHelper
				.addShapedOreRecipe(new ItemStack(ModBlocks.ChemicalReactor), "IMI", "CPC", "IEI", 'I', "ingotInvar",
						'C', "circuitAdvanced", 'M', ModBlocks.Extractor, 'P',
						ModBlocks.Compressor, 'E', ModBlocks.Extractor);

		CraftingHelper
				.addShapedOreRecipe(new ItemStack(ModBlocks.RollingMachine), "PCP", "MBM", "PCP", 'P', Blocks.PISTON,
						'C', "circuitAdvanced", 'M', ModBlocks.Compressor, 'B',
						BlockMachineFrame.getFrameByName("machine", 1));

		// CraftingHelper.addShapedOreRecipe(new
		// ItemStack(ModBlocks.ElectricCraftingTable),
		// "ITI", "IBI", "ICI",
		// 'I', "plateIron",
		// 'C', "circuitAdvanced",
		// 'T', "crafterWood",
		// 'B', "machineBlockBasic");

		// CraftingHelper.addShapedOreRecipe(new
		// ItemStack(ModBlocks.ElectricCraftingTable),
		// "ATA", "ABA", "ACA",
		// 'A', "plateAluminum",
		// 'C', "circuitAdvanced",
		// 'T', "crafterWood",
		// 'B', "machineBlockBasic");

		// CraftingHelper.addShapedOreRecipe(new
		// ItemStack(ModBlocks.ChunkLoader),
		// "SCS", "CMC", "SCS",
		// 'S', "plateSteel",
		// 'C', "circuitMaster",
		// 'M', new ItemStack(ModItems.parts, 1, 39));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Lesu), " L ", "CBC", " M ", 'L', ModBlocks.lvt, 'C',
				"circuitAdvanced", 'M', ModBlocks.mvt, 'B', ModBlocks.LesuStorage);

		CraftingHelper
				.addShapedOreRecipe(BlockMachineFrame.getFrameByName("highlyAdvancedMachine", 1), "CTC", "TBT", "CTC",
						'C', "ingotChrome", 'T', "ingotTitanium", 'B',
						"machineBlockAdvanced");

		CraftingHelper
				.addShapedOreRecipe(new ItemStack(ModBlocks.MachineCasing, 4, 0), "III", "CBC", "III", 'I', "plateIron",
						'C', "circuitBasic", 'B', "machineBlockBasic");

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.MachineCasing, 4, 1), "SSS", "CBC", "SSS", 'S',
				"plateSteel", 'C', "circuitAdvanced", 'B', "machineBlockAdvanced");

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.MachineCasing, 4, 2), "HHH", "CBC", "HHH", 'H',
				"ingotChrome", 'C', "circuitElite", 'B', BlockMachineFrame.getFrameByName("highlyAdvancedMachine", 1));

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.quantumChest), "DCD", "ATA", "DQD", 'D',
				ItemParts.getPartByName("dataOrb"), 'C', ItemParts.getPartByName("computerMonitor"), 'A',
				BlockMachineFrame.getFrameByName("highlyAdvancedMachine", 1), 'Q', ModBlocks.digitalChest, 'T',
				ModBlocks.Compressor);

		CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.PlasmaGenerator), "PPP", "PTP", "CGC", 'P',
				ItemPlates.getPlateByName("tungstensteel"), 'T', getOre("hvTransformer"),
				'G', "ic2Generator", 'C',
				"circuitMaster");

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
							ModItems.uuMatter);

		// Blast Furnace
		RecipeHandler.addRecipe(new BlastFurnaceRecipe(ItemCells.getCellByName("silicon", 2), null,
				ItemPlates.getPlateByName("silicon"), ItemCells.getCellByName("empty", 2), 1000, 120, 1500));

		// CentrifugeRecipes

		// Plantball/Bio Chaff
		// FIX with ic2
		// RecipeHandler.addRecipe(new CentrifugeRecipe(new
		// ItemStack(Blocks.grass, 16), null, new
		// ItemStack(TechRebornAPI.recipeCompact.getItem("biochaff").getItem(),
		// 8), new
		// ItemStack(TechRebornAPI.recipeCompact.getItem("plantBall").getItem(),
		// 8), new ItemStack(Items.clay_ball), new ItemStack(Blocks.sand, 8),
		// 2500, 5));
		// RecipeHandler.addRecipe(new CentrifugeRecipe(new
		// ItemStack(Blocks.dirt, 16), null, new
		// ItemStack(TechRebornAPI.recipeCompact.getItem("biochaff").getItem(),
		// 4), new
		// ItemStack(TechRebornAPI.recipeCompact.getItem("plantBall").getItem(),
		// 4), new ItemStack(Items.clay_ball), new ItemStack(Blocks.sand, 8),
		// 2500, 5));

		// Methane
		RecipeHandler.addRecipe(
				new CentrifugeRecipe(new ItemStack(Items.MUSHROOM_STEW, 16), ItemCells.getCellByName("empty"),
						ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
		RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.APPLE, 32), ItemCells.getCellByName("empty"),
				ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
		RecipeHandler.addRecipe(
				new CentrifugeRecipe(new ItemStack(Items.PORKCHOP, 12), ItemCells.getCellByName("empty"),
						ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
		RecipeHandler.addRecipe(
				new CentrifugeRecipe(new ItemStack(Items.COOKED_PORKCHOP, 16), ItemCells.getCellByName("empty"),
						ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
		RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.BREAD, 64), ItemCells.getCellByName("empty"),
				ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
		RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.FISH, 12), ItemCells.getCellByName("empty"),
				ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
		RecipeHandler.addRecipe(
				new CentrifugeRecipe(new ItemStack(Items.COOKED_FISH, 16), ItemCells.getCellByName("empty"),
						ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
		RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.BEEF, 12), ItemCells.getCellByName("empty"),
				ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
		RecipeHandler.addRecipe(
				new CentrifugeRecipe(new ItemStack(Items.COOKED_BEEF, 16), ItemCells.getCellByName("empty"),
						ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
		RecipeHandler.addRecipe(
				new CentrifugeRecipe(new ItemStack(Blocks.PUMPKIN, 16), ItemCells.getCellByName("empty"),
						ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
		RecipeHandler.addRecipe(
				new CentrifugeRecipe(new ItemStack(Items.SPECKLED_MELON, 1), ItemCells.getCellByName("empty"),
						ItemCells.getCellByName("methane", 1), new ItemStack(Items.GOLD_NUGGET, 6), null, null, 5000,
						5));
		RecipeHandler.addRecipe(
				new CentrifugeRecipe(new ItemStack(Items.SPIDER_EYE, 32), ItemCells.getCellByName("empty"),
						ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
		RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.CHICKEN, 12), ItemCells.getCellByName("empty"),
				ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
		RecipeHandler.addRecipe(
				new CentrifugeRecipe(new ItemStack(Items.COOKED_CHICKEN, 16), ItemCells.getCellByName("empty"),
						ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
		RecipeHandler.addRecipe(
				new CentrifugeRecipe(new ItemStack(Items.ROTTEN_FLESH, 16), ItemCells.getCellByName("empty"),
						ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
		RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.MELON, 64), ItemCells.getCellByName("empty"),
				ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
		RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.COOKIE, 64), ItemCells.getCellByName("empty"),
				ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
		RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.CAKE, 8), ItemCells.getCellByName("empty"),
				ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
		RecipeHandler.addRecipe(
				new CentrifugeRecipe(new ItemStack(Items.GOLDEN_CARROT, 1), ItemCells.getCellByName("empty"),
						ItemCells.getCellByName("methane", 1), new ItemStack(Items.GOLD_NUGGET, 6), null, null, 5000,
						5));
		RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.CARROT, 16), ItemCells.getCellByName("empty"),
				ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
		RecipeHandler.addRecipe(
				new CentrifugeRecipe(new ItemStack(Items.BAKED_POTATO, 24), ItemCells.getCellByName("empty"),
						ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
		RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.POTATO, 16), ItemCells.getCellByName("empty"),
				ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
		RecipeHandler.addRecipe(
				new CentrifugeRecipe(new ItemStack(Items.POISONOUS_POTATO, 12), ItemCells.getCellByName("empty"),
						ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
		RecipeHandler.addRecipe(
				new CentrifugeRecipe(new ItemStack(Items.NETHER_WART, 1), ItemCells.getCellByName("empty"),
						ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
		// Fix with ic2
		// RecipeHandler.addRecipe(new CentrifugeRecipe(new
		// ItemStack(TechRebornAPI.recipeCompact.getItem("terraWart").getItem(),
		// 16), ItemCells.getCellByName("empty"),
		// ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
		RecipeHandler.addRecipe(
				new CentrifugeRecipe(new ItemStack(Blocks.BROWN_MUSHROOM, 1), ItemCells.getCellByName("empty"),
						ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
		RecipeHandler.addRecipe(
				new CentrifugeRecipe(new ItemStack(Blocks.RED_MUSHROOM, 1), ItemCells.getCellByName("empty"),
						ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));

		// Rubber Wood Yields
		RecipeHandler.addRecipe(
				new CentrifugeRecipe(new ItemStack(getOre("rubberWood").getItem(), 16),
						ItemCells.getCellByName("empty", 5), ItemParts.getPartByName("rubber", 8),
						new ItemStack(Blocks.SAPLING, 6), ItemCells.getCellByName("methane", 1),
						ItemCells.getCellByName("carbon", 4), 5000, 5, true));

		// Soul Sand Byproducts
		RecipeHandler.addRecipe(
				new CentrifugeRecipe(new ItemStack(Blocks.SOUL_SAND, 16), ItemCells.getCellByName("empty"),
						ItemCells.getCellByName("oil", 1), ItemDusts.getDustByName("saltpeter", 4),
						ItemDusts.getDustByName("coal", 1), new ItemStack(Blocks.SAND, 10), 2500, 5));

		// Dust Byproducts

		RecipeHandler.addRecipe(
				new CentrifugeRecipe(new ItemStack(Items.GLOWSTONE_DUST, 16), ItemCells.getCellByName("empty"),
						new ItemStack(Items.REDSTONE, 8), ItemDusts.getDustByName("gold", 8),
						ItemCells.getCellByName("helium", 1), null, 25000, 5));
		RecipeHandler.addRecipe(
				new CentrifugeRecipe(ItemDusts.getDustByName("phosphorous", 5), ItemCells.getCellByName("empty", 3),
						ItemCells.getCellByName("calcium", 3), null, null, null, 1280, 5));
		RecipeHandler.addRecipe(
				new CentrifugeRecipe(ItemDusts.getDustByName("ashes", 1), ItemCells.getCellByName("empty"),
						ItemCells.getCellByName("carbon"), null, null, null, 80, 5));
		RecipeHandler.addRecipe(
				new CentrifugeRecipe(new ItemStack(Items.REDSTONE, 10), ItemCells.getCellByName("empty", 4),
						ItemCells.getCellByName("silicon", 1), ItemDusts.getDustByName("pyrite", 3),
						ItemDusts.getDustByName("ruby", 1), ItemCells.getCellByName("mercury", 3), 6800, 5));
		RecipeHandler.addRecipe(
				new CentrifugeRecipe(ItemDusts.getDustByName("endstone", 16), ItemCells.getCellByName("empty", 2),
						ItemCells.getCellByName("helium3", 1), ItemCells.getCellByName("helium"),
						ItemDustsSmall.getSmallDustByName("Tungsten", 1), new ItemStack(Blocks.SAND, 12), 4800, 5));
		RecipeHandler.addRecipe(
				new CentrifugeRecipe(ItemDusts.getDustByName("cinnabar", 2), ItemCells.getCellByName("empty"),
						ItemCells.getCellByName("mercury", 1), ItemDusts.getDustByName("sulfur", 1), null, null, 80,
						5));

		// Deuterium/Tritium
		RecipeHandler.addRecipe(
				new CentrifugeRecipe(ItemCells.getCellByName("helium", 16), null, ItemCells.getCellByName("helium3", 1),
						ItemCells.getCellByName("empty", 15), null, null, 10000, 5));
		RecipeHandler.addRecipe(new CentrifugeRecipe(ItemCells.getCellByName("deuterium", 4), null,
				ItemCells.getCellByName("tritium", 1), ItemCells.getCellByName("empty", 3), null, null, 3000, 5));
		RecipeHandler.addRecipe(new CentrifugeRecipe(ItemCells.getCellByName("hydrogen", 4), null,
				ItemCells.getCellByName("deuterium", 1), ItemCells.getCellByName("empty", 3), null, null, 3000, 5));

		// Lava Cell Byproducts
		ItemStack lavaCells = ItemCells.getCellByName("lava");
		lavaCells.stackSize = 8;
		RecipeHandler.addRecipe(new CentrifugeRecipe(lavaCells, null, ItemNuggets.getNuggetByName("electrum", 4),
				ItemIngots.getIngotByName("copper", 2), ItemDustsSmall.getSmallDustByName("Tungsten", 1),
				ItemIngots.getIngotByName("tin", 2), 6000, 5));

		// IndustrialGrinderRecipes
		RecipeHandler.addRecipe(new IndustrialGrinderRecipe(new ItemStack(Blocks.COAL_ORE, 1),
				new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(Items.COAL, 1),
				ItemDustsSmall.getSmallDustByName("Coal", 6), ItemDustsSmall.getSmallDustByName("Coal", 2),
				null, 100, 120));
		RecipeHandler.addRecipe(new IndustrialGrinderRecipe(new ItemStack(Blocks.IRON_ORE, 1),
				new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("iron", 2),
				ItemDustsSmall.getSmallDustByName("Nickel", 1), ItemDustsSmall.getSmallDustByName("Tin", 1),
				null, 100, 120));
		RecipeHandler.addRecipe(new IndustrialGrinderRecipe(new ItemStack(Blocks.GOLD_ORE, 1),
				new FluidStack(FluidRegistry.WATER, 1000), ItemDusts.getDustByName("gold", 2),
				ItemDustsSmall.getSmallDustByName("Copper", 1), ItemDustsSmall.getSmallDustByName("Nickel", 1),
				null, 100, 120));
		RecipeHandler.addRecipe(new IndustrialGrinderRecipe(new ItemStack(Blocks.IRON_ORE, 1),
                new FluidStack(ModFluids.fluidSodiumpersulfate, 1000), ItemDusts.getDustByName("iron", 2),
				ItemDusts.getDustByName("nickel", 1), ItemDustsSmall.getSmallDustByName("Tin", 1),
				null, 100, 120));
		RecipeHandler.addRecipe(new IndustrialGrinderRecipe(new ItemStack(Blocks.GOLD_ORE, 1),
                new FluidStack(ModFluids.fluidSodiumpersulfate, 1000), ItemDusts.getDustByName("gold", 2),
				ItemDusts.getDustByName("copper", 1), ItemDustsSmall.getSmallDustByName("Nickel", 1),
				null, 100, 120));
		RecipeHandler.addRecipe(
				new IndustrialGrinderRecipe(new ItemStack(Blocks.GOLD_ORE, 1), new FluidStack(ModFluids.fluidMercury, 1000),
                        ItemDusts.getDustByName("gold", 3), ItemDustsSmall.getSmallDustByName("Copper", 1),
						ItemDustsSmall.getSmallDustByName("Nickel", 1), null, 100, 120));

        RecipeHandler.addRecipe(new IndustrialGrinderRecipe(new ItemStack(Blocks.DIAMOND_ORE, 1),
				new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(Items.DIAMOND, 1),
				ItemDustsSmall.getSmallDustByName("Diamond", 6), ItemDustsSmall.getSmallDustByName("Coal", 2),
				null, 100, 120));
		RecipeHandler.addRecipe(new IndustrialGrinderRecipe(new ItemStack(Blocks.EMERALD_ORE, 1),
				new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(Items.EMERALD, 1),
				ItemDustsSmall.getSmallDustByName("Emerald", 6), ItemDustsSmall.getSmallDustByName("Aluminum", 2),
				null, 100, 120));
		RecipeHandler.addRecipe(new IndustrialGrinderRecipe(new ItemStack(Blocks.REDSTONE_ORE, 1),
				new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(Items.REDSTONE, 10),
				ItemDustsSmall.getSmallDustByName("Cinnabar", 1), ItemDustsSmall.getSmallDustByName("Glowstone", 1),
				null, 100, 120));
		RecipeHandler.addRecipe(new IndustrialGrinderRecipe(new ItemStack(Blocks.LAPIS_ORE, 1),
				new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(Items.DYE, 6, 4),
				ItemDustsSmall.getSmallDustByName("Lazurite", 3), null, null, 100, 120));

		// Copper Ore
		if (OreUtil.doesOreExistAndValid("oreCopper"))
		{
			try
			{
				ItemStack oreStack = OreDictionary.getOres("oreCopper").get(0);
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack,new FluidStack(FluidRegistry.WATER, 1000),
								ItemDusts.getDustByName("copper", 2), ItemDustsSmall.getSmallDustByName("Gold", 1),
								ItemDustsSmall.getSmallDustByName("Nickel", 1), ItemCells.getCellByName("empty"), 100,
								120));

				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack, new FluidStack(ModFluids.fluidSodiumpersulfate, 1000),
								ItemDusts.getDustByName("copper", 2), ItemDusts.getDustByName("gold", 1),
								ItemDustsSmall.getSmallDustByName("Nickel", 1), null, 100,
								120));

				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack, new FluidStack(ModFluids.fluidMercury, 1000),
								ItemDusts.getDustByName("copper", 2), ItemDustsSmall.getSmallDustByName("Gold", 1),
								ItemDusts.getDustByName("nickel", 1), null, 100, 120));
			} catch (Exception e)
			{
				Core.logHelper.info("Failed to Load Grinder Recipe for Copper Ore");
			}
		}

		// Tin Ore
		if (OreUtil.doesOreExistAndValid("oreTin"))
		{
			try
			{
				ItemStack oreStack = OreDictionary.getOres("oreTin").get(0);
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack,new FluidStack(FluidRegistry.WATER, 1000),
								ItemDusts.getDustByName("tin", 2), ItemDustsSmall.getSmallDustByName("Iron", 1),
								ItemDustsSmall.getSmallDustByName("Zinc", 1), null, 100,
								120));
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack, new FluidStack(ModFluids.fluidSodiumpersulfate, 1000),
								ItemDusts.getDustByName("tin", 2), ItemDustsSmall.getSmallDustByName("Iron", 1),
								ItemDusts.getDustByName("zinc", 1), null, 100, 120));
			} catch (Exception e)
			{
				Core.logHelper.info("Failed to Load Grinder Recipe for Tin Ore");
			}
		}

		// Nickel Ore
		if (OreUtil.doesOreExistAndValid("oreNickel"))
		{
			try
			{
				ItemStack oreStack = OreDictionary.getOres("oreNickel").get(0);
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack,new FluidStack(FluidRegistry.WATER, 1000),
								ItemDusts.getDustByName("nickel", 2), ItemDustsSmall.getSmallDustByName("Iron", 1),
								ItemDustsSmall.getSmallDustByName("Platinum", 1), ItemCells.getCellByName("empty"), 100,
								120));
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack, new FluidStack(ModFluids.fluidSodiumpersulfate, 1000),
								ItemDusts.getDustByName("nickel", 3), ItemDustsSmall.getSmallDustByName("Iron", 1),
								ItemDustsSmall.getSmallDustByName("Platinum", 1), null, 100,
								120));
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack, new FluidStack(ModFluids.fluidMercury, 1000),
								ItemDusts.getDustByName("nickel", 2), ItemDustsSmall.getSmallDustByName("Iron", 1),
								ItemDusts.getDustByName("platinum", 1), null, 100, 120));
			} catch (Exception e)
			{
				Core.logHelper.info("Failed to Load Grinder Recipe for Nickel Ore");
			}
		}

		// Zinc Ore
		if (OreUtil.doesOreExistAndValid("oreZinc"))
		{
			try
			{
				ItemStack oreStack = OreDictionary.getOres("oreZinc").get(0);
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack,new FluidStack(FluidRegistry.WATER, 1000),
								ItemDusts.getDustByName("zinc", 2), ItemDustsSmall.getSmallDustByName("Iron", 1),
								ItemDustsSmall.getSmallDustByName("Tin", 1), null, 100,
								120));
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack, new FluidStack(ModFluids.fluidSodiumpersulfate, 1000),
								ItemDusts.getDustByName("zinc", 2), ItemDustsSmall.getSmallDustByName("Iron", 1),
								ItemDusts.getDustByName("iron", 1), null, 100, 120));
			} catch (Exception e)
			{
				Core.logHelper.info("Failed to Load Grinder Recipe for Zinc Ore");
			}
		}

		// Silver Ore
		if (OreUtil.doesOreExistAndValid("oreSilver"))
		{
			try
			{
				ItemStack oreStack = OreDictionary.getOres("oreSilver").get(0);
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack,new FluidStack(FluidRegistry.WATER, 1000),
								ItemDusts.getDustByName("silver", 2), ItemDustsSmall.getSmallDustByName("Lead", 1),
								ItemDustsSmall.getSmallDustByName("Sulfur", 1), null, 100,
								120));
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack, new FluidStack(ModFluids.fluidMercury, 1000),
								ItemDusts.getDustByName("silver", 3), ItemDustsSmall.getSmallDustByName("Lead", 1),
								ItemDustsSmall.getSmallDustByName("Sulfur", 1), null, 100,
								120));
			} catch (Exception e)
			{
				Core.logHelper.info("Failed to Load Grinder Recipe for Silver Ore");
			}
		}

		// Lead Ore
		if (OreUtil.doesOreExistAndValid("oreLead"))
		{
			try
			{
				ItemStack oreStack = OreDictionary.getOres("oreLead").get(0);
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack,new FluidStack(FluidRegistry.WATER, 1000),
								ItemDusts.getDustByName("lead", 2), ItemDustsSmall.getSmallDustByName("Silver", 1),
								ItemDustsSmall.getSmallDustByName("Sulfur", 1), null, 100,
								120));
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack, new FluidStack(ModFluids.fluidMercury, 1000),
								ItemDusts.getDustByName("lead", 2), ItemDusts.getDustByName("silver", 1),
								ItemDustsSmall.getSmallDustByName("Sulfur", 1), null, 100,
								120));
			} catch (Exception e)
			{
				Core.logHelper.info("Failed to Load Grinder Recipe for Lead Ore");
			}
		}

		// Uranium Ore
		if (OreUtil.doesOreExistAndValid("oreUranium"))
		{
			try
			{
				ItemStack oreStack = OreDictionary.getOres("oreUranium").get(0);
				ItemStack uranium238Stack = getOre("uran238");
				uranium238Stack.stackSize = 8;
				ItemStack uranium235Stack = getOre("smallUran235");
				uranium235Stack.stackSize = 2;
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack, new FluidStack(FluidRegistry.WATER, 1000),
								uranium238Stack, uranium235Stack, null, null, 100, 120));
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack,new FluidStack(FluidRegistry.WATER, 1000),
								uranium238Stack, uranium235Stack, null, ItemCells.getCellByName("empty"), 100, 120));
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack, new FluidStack(FluidRegistry.WATER, 1000), uranium238Stack,
								uranium235Stack, null, new ItemStack(Items.BUCKET), 100, 120));
			} catch (Exception e)
			{
				Core.logHelper.info("Failed to Load Grinder Recipe for Uranium Ore");
			}
		}

		// Pitchblende Ore
		if (OreUtil.doesOreExistAndValid("orePitchblende"))
		{
			try
			{
				ItemStack oreStack = OreDictionary.getOres("orePitchblende").get(0);
				ItemStack uranium238Stack = getOre("uran238");
				uranium238Stack.stackSize = 8;
				ItemStack uranium235Stack = getOre("uran235");
				uranium235Stack.stackSize = 2;
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack, new FluidStack(FluidRegistry.WATER, 1000),
								uranium238Stack, uranium235Stack, null, null, 100, 120));
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack,new FluidStack(FluidRegistry.WATER, 1000),
								uranium238Stack, uranium235Stack, null, ItemCells.getCellByName("empty"), 100, 120));
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack, new FluidStack(FluidRegistry.WATER, 1000), uranium238Stack,
								uranium235Stack, null, new ItemStack(Items.BUCKET), 100, 120));
			} catch (Exception e)
			{
				Core.logHelper.info("Failed to Load Grinder Recipe for Uranium Ore");
			}
		}

		// Aluminum Ore
		if (OreUtil.doesOreExistAndValid("oreAluminum"))
		{
			try
			{
				ItemStack oreStack = OreDictionary.getOres("oreAluminum").get(0);
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack,new FluidStack(FluidRegistry.WATER, 1000),
								ItemDusts.getDustByName("aluminum", 2), ItemDustsSmall.getSmallDustByName("Bauxite", 1),
								ItemDustsSmall.getSmallDustByName("Bauxite", 1), ItemCells.getCellByName("empty"), 100,
								120));
			} catch (Exception e)
			{
				Core.logHelper.info("Failed to Load Grinder Recipe for Lead Ore");
			}
		}

		// Ardite Ore
		if (OreUtil.doesOreExistAndValid("oreArdite"))
		{
			try
			{
				ItemStack oreStack = OreDictionary.getOres("oreArdite").get(0);
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack,new FluidStack(FluidRegistry.WATER, 1000),
								ItemDusts.getDustByName("ardite", 2), ItemDustsSmall.getSmallDustByName("Ardite", 1),
								ItemDustsSmall.getSmallDustByName("Ardite", 1), ItemCells.getCellByName("empty"), 100,
								120));
			} catch (Exception e)
			{
				Core.logHelper.info("Failed to Load Grinder Recipe for Ardite Ore");
			}
		}

		// Cobalt Ore
		if (OreUtil.doesOreExistAndValid("oreCobalt"))
		{
			try
			{
				ItemStack oreStack = OreDictionary.getOres("oreCobalt").get(0);
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack,new FluidStack(FluidRegistry.WATER, 1000),
								ItemDusts.getDustByName("cobalt", 2), ItemDustsSmall.getSmallDustByName("Cobalt", 1),
								ItemDustsSmall.getSmallDustByName("Cobalt", 1), ItemCells.getCellByName("empty"), 100,
								120));
			} catch (Exception e)
			{
				Core.logHelper.info("Failed to Load Grinder Recipe for Cobalt Ore");
			}
		}

		// Dark Iron Ore
		if (OreUtil.doesOreExistAndValid("oreDarkIron"))
		{
			try
			{
				ItemStack oreStack = OreDictionary.getOres("oreDarkIron").get(0);
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack,new FluidStack(FluidRegistry.WATER, 1000),
								ItemDusts.getDustByName("darkIron", 2),
								ItemDustsSmall.getSmallDustByName("DarkIron", 1),
								ItemDustsSmall.getSmallDustByName("Iron", 1), ItemCells.getCellByName("empty"), 100,
								120));
			} catch (Exception e)
			{
				Core.logHelper.info("Failed to Load Grinder Recipe for Dark Iron Ore");
			}
		}

		// Cadmium Ore
		if (OreUtil.doesOreExistAndValid("oreCadmium"))
		{
			try
			{
				ItemStack oreStack = OreDictionary.getOres("oreCadmium").get(0);
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack,new FluidStack(FluidRegistry.WATER, 1000),
								ItemDusts.getDustByName("cadmium", 2), ItemDustsSmall.getSmallDustByName("Cadmium", 1),
								ItemDustsSmall.getSmallDustByName("Cadmium", 1), ItemCells.getCellByName("empty"), 100,
								120));
			} catch (Exception e)
			{
				Core.logHelper.info("Failed to Load Grinder Recipe for Cadmium Ore");
			}
		}

		// Indium Ore
		if (OreUtil.doesOreExistAndValid("oreIndium"))
		{
			try
			{
				ItemStack oreStack = OreDictionary.getOres("oreIndium").get(0);
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack,new FluidStack(FluidRegistry.WATER, 1000),
								ItemDusts.getDustByName("indium", 2), ItemDustsSmall.getSmallDustByName("Indium", 1),
								ItemDustsSmall.getSmallDustByName("Indium", 1), ItemCells.getCellByName("empty"), 100,
								120));
			} catch (Exception e)
			{
				Core.logHelper.info("Failed to Load Grinder Recipe for Indium Ore");
			}
		}

		// Calcite Ore
		if (OreUtil.doesOreExistAndValid("oreCalcite") && OreUtil.doesOreExistAndValid("gemCalcite"))
		{
			try
			{
				ItemStack oreStack = OreDictionary.getOres("oreCalcite").get(0);
				ItemStack gemStack = OreDictionary.getOres("gemCalcite").get(0);
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack,new FluidStack(FluidRegistry.WATER, 1000),
								gemStack, ItemDustsSmall.getSmallDustByName("Calcite", 6), null,
								ItemCells.getCellByName("empty"), 100, 120));
			} catch (Exception e)
			{
				Core.logHelper.info("Failed to Load Grinder Recipe for Calcite Ore");
			}
		}

		// Magnetite Ore
		if (OreUtil.doesOreExistAndValid("oreMagnetite") && OreUtil.doesOreExistAndValid("chunkMagnetite"))
		{
			try
			{
				ItemStack oreStack = OreDictionary.getOres("oreMagnetite").get(0);
				ItemStack chunkStack = OreDictionary.getOres("chunkMagnetite").get(0);
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack,new FluidStack(FluidRegistry.WATER, 1000),
								chunkStack, ItemDustsSmall.getSmallDustByName("Magnetite", 6), null,
								ItemCells.getCellByName("empty"), 100, 120));
			} catch (Exception e)
			{
				Core.logHelper.info("Failed to Load Grinder Recipe for Magnetite Ore");
			}
		}

		// Graphite Ore
		if (OreUtil.doesOreExistAndValid("oreGraphite") && OreUtil.doesOreExistAndValid("chunkGraphite"))
		{
			try
			{
				ItemStack oreStack = OreDictionary.getOres("oreGraphite").get(0);
				ItemStack chunkStack = OreDictionary.getOres("chunkGraphite").get(0);
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack,new FluidStack(FluidRegistry.WATER, 1000),
								chunkStack, ItemDustsSmall.getSmallDustByName("Graphite", 6), null,
								ItemCells.getCellByName("empty"), 100, 120));
			} catch (Exception e)
			{
				Core.logHelper.info("Failed to Load Grinder Recipe for Graphite Ore");
			}
		}

		// Osmium Ore
		if (OreUtil.doesOreExistAndValid("oreOsmium"))
		{
			try
			{
				ItemStack oreStack = OreDictionary.getOres("oreOsmium").get(0);
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack,new FluidStack(FluidRegistry.WATER, 1000),
								ItemDusts.getDustByName("osmium", 2), ItemDustsSmall.getSmallDustByName("Osmium", 1),
								ItemDustsSmall.getSmallDustByName("Osmium", 1), ItemCells.getCellByName("empty"), 100,
								120));
			} catch (Exception e)
			{
				Core.logHelper.info("Failed to Load Grinder Recipe for Osmium Ore");
			}
		}

		// Teslatite Ore
		if (OreUtil.doesOreExistAndValid("oreTeslatite") && OreUtil.doesOreExistAndValid("dustTeslatite"))
		{
			try
			{
				ItemStack oreStack = OreDictionary.getOres("oreTeslatite").get(0);
				ItemStack dustStack = OreDictionary.getOres("dustTeslatite").get(0);
				dustStack.stackSize = 10;
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack,new FluidStack(FluidRegistry.WATER, 1000),
								dustStack, ItemDustsSmall.getSmallDustByName("Sodalite", 1),
								ItemDustsSmall.getSmallDustByName("Glowstone", 1), ItemCells.getCellByName("empty"),
								100, 120));
			} catch (Exception e)
			{
				Core.logHelper.info("Failed to Load Grinder Recipe for Teslatite Ore");
			}
		}

		// Sulfur Ore
		if (OreUtil.doesOreExistAndValid("oreSulfur"))
		{
			try
			{
				ItemStack oreStack = OreDictionary.getOres("oreSulfur").get(0);
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack,new FluidStack(FluidRegistry.WATER, 1000),
								ItemDusts.getDustByName("sulfur", 2), ItemDustsSmall.getSmallDustByName("Sulfur", 1),
								ItemDustsSmall.getSmallDustByName("Sulfur", 1), ItemCells.getCellByName("empty"), 100,
								120));
			} catch (Exception e)
			{
				Core.logHelper.info("Failed to Load Grinder Recipe for Sulfur Ore");
			}
		}

		// Saltpeter Ore
		if (OreUtil.doesOreExistAndValid("oreSaltpeter"))
		{
			try
			{
				ItemStack oreStack = OreDictionary.getOres("oreSaltpeter").get(0);
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack,new FluidStack(FluidRegistry.WATER, 1000),
								ItemDusts.getDustByName("saltpeter", 2),
								ItemDustsSmall.getSmallDustByName("Saltpeter", 1),
								ItemDustsSmall.getSmallDustByName("Saltpeter", 1), ItemCells.getCellByName("empty"),
								100, 120));
			} catch (Exception e)
			{
				Core.logHelper.info("Failed to Load Grinder Recipe for Saltpeter Ore");
			}
		}

		// Apatite Ore
		if (OreUtil.doesOreExistAndValid("oreApatite") & OreUtil.doesOreExistAndValid("gemApatite"))
		{
			try
			{
				ItemStack oreStack = OreDictionary.getOres("oreApatite").get(0);
				ItemStack gemStack = OreDictionary.getOres("gemApatite").get(0);
				gemStack.stackSize = 6;
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack,new FluidStack(FluidRegistry.WATER, 1000),
								gemStack, gemStack, ItemDustsSmall.getSmallDustByName("Phosphorous", 4),
								ItemCells.getCellByName("empty"), 100, 120));
			} catch (Exception e)
			{
				Core.logHelper.info("Failed to Load Grinder Recipe for Apatite Ore");
			}
		}

		// Nether Quartz Ore
		if (OreUtil.doesOreExistAndValid("dustNetherQuartz"))
		{
			try
			{
				ItemStack dustStack = OreDictionary.getOres("dustNetherQuartz").get(0);
				dustStack.stackSize = 4;
				RecipeHandler.addRecipe(new IndustrialGrinderRecipe(new ItemStack(Blocks.QUARTZ_ORE, 1),
						new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(Items.QUARTZ, 2),
						dustStack, ItemDustsSmall.getSmallDustByName("Netherrack", 2), ItemCells.getCellByName("empty"),
						100, 120));
			} catch (Exception e)
			{
				Core.logHelper.info("Failed to Load Grinder Recipe for Nether Quartz Ore");
			}
		}

		// Certus Quartz Ore
		if (OreUtil.doesOreExistAndValid("oreCertusQuartz"))
		{
			try
			{
				ItemStack oreStack = OreDictionary.getOres("oreCertusQuartz").get(0);
				ItemStack gemStack = OreDictionary.getOres("crystalCertusQuartz").get(0);
				ItemStack dustStack = OreDictionary.getOres("dustCertusQuartz").get(0);
				dustStack.stackSize = 2;
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack,new FluidStack(FluidRegistry.WATER, 1000),
								gemStack, dustStack, null, ItemCells.getCellByName("empty"), 100, 120));
			} catch (Exception e)
			{
				Core.logHelper.info("Failed to Load Grinder Recipe for Certus Quartz Ore");
			}
		}

		// Charged Certus Quartz Ore
		if (OreUtil.doesOreExistAndValid("oreChargedCertusQuartz"))
		{
			try
			{
				ItemStack oreStack = OreDictionary.getOres("oreChargedCertusQuartz").get(0);
				ItemStack gemStack = OreDictionary.getOres("crystalChargedCertusQuartz").get(0);
				ItemStack dustStack = OreDictionary.getOres("dustCertusQuartz").get(0);
				dustStack.stackSize = 2;
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack,new FluidStack(FluidRegistry.WATER, 1000),
								gemStack, dustStack, null, ItemCells.getCellByName("empty"), 100, 120));
			} catch (Exception e)
			{
				Core.logHelper.info("Failed to Load Grinder Recipe for Charged Certus Quartz Ore");
			}
		}

		// Amethyst Ore
		if (OreUtil.doesOreExistAndValid("oreAmethyst") && OreUtil.doesOreExistAndValid("gemAmethyst"))
		{
			try
			{
				ItemStack oreStack = OreDictionary.getOres("oreAmethyst").get(0);
				ItemStack gemStack = OreDictionary.getOres("gemAmethyst").get(0);
				gemStack.stackSize = 2;
				ItemStack dustStack = OreDictionary.getOres("gemAmethyst").get(0);
				dustStack.stackSize = 1;
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack,new FluidStack(FluidRegistry.WATER, 1000),
								gemStack, dustStack, null, ItemCells.getCellByName("empty"), 100, 120));
			} catch (Exception e)
			{
				Core.logHelper.info("Failed to Load Grinder Recipe for Certus Quartz Ore");
			}
		}

		// Topaz Ore
		if (OreUtil.doesOreExistAndValid("oreTopaz") && OreUtil.doesOreExistAndValid("gemTopaz"))
		{
			try
			{
				ItemStack oreStack = OreDictionary.getOres("oreTopaz").get(0);
				ItemStack gemStack = OreDictionary.getOres("gemTopaz").get(0);
				gemStack.stackSize = 2;
				ItemStack dustStack = OreDictionary.getOres("gemTopaz").get(0);
				dustStack.stackSize = 1;
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack,
                                new FluidStack(FluidRegistry.WATER, 1000),
								gemStack, dustStack,
                                null, null, 100, 120));
			} catch (Exception e)
			{
				Core.logHelper.info("Failed to Load Grinder Recipe for Topaz Ore");
			}
		}

		// Tanzanite Ore
		if (OreUtil.doesOreExistAndValid("oreTanzanite") && OreUtil.doesOreExistAndValid("gemTanzanite"))
		{
			try
			{
				ItemStack oreStack = OreDictionary.getOres("oreTanzanite").get(0);
				ItemStack gemStack = OreDictionary.getOres("gemTanzanite").get(0);
				gemStack.stackSize = 2;
				ItemStack dustStack = OreDictionary.getOres("gemTanzanite").get(0);
				dustStack.stackSize = 1;
				RecipeHandler.addRecipe(
						new IndustrialGrinderRecipe(oreStack,
                                new FluidStack(FluidRegistry.WATER, 1000),
								gemStack, dustStack, 
                                null, null, 100, 120));
			} catch (Exception e)
			{
				Core.logHelper.info("Failed to Load Grinder Recipe for Tanzanite Ore");
			}
		}

		// Malachite Ore
		if (OreUtil.doesOreExistAndValid("oreMalachite") && OreUtil.doesOreExistAndValid("gemMalachite"))
		{
			try
			{
				ItemStack oreStack = OreDictionary.getOres("oreMalachite").get(0);
				ItemStack gemStack = OreDictionary.getOres("gemMalachite").get(0);
				gemStack.stackSize = 2;
				ItemStack dustStack = OreDictionary.getOres("gemMalachite").get(0);
				dustStack.stackSize = 1;

				RecipeHandler.addRecipe(new IndustrialGrinderRecipe(oreStack, 
                        new FluidStack(FluidRegistry.WATER, 1000),
                        gemStack, dustStack, 
                        null, null, 100, 120));
                
			} catch (Exception e)
			{
				Core.logHelper.info("Failed to Load Grinder Recipe for Malachite Ore");
			}
		}

		// Implosion Compressor

		RecipeHandler.addRecipe(new ImplosionCompressorRecipe(ItemIngots.getIngotByName("iridiumAlloy"),
				new ItemStack(Blocks.TNT, 8),
				OreDictionary.getOres("plateIridium").get(0).copy(), ItemDusts.getDustByName("darkAshes", 4), 20, 30));
		RecipeHandler.addRecipe(new ImplosionCompressorRecipe(ItemDusts.getDustByName("diamond", 4),
				new ItemStack(Blocks.TNT, 32),
				new ItemStack(Items.DIAMOND, 3),
				ItemDusts.getDustByName("darkAshes", 16), 20, 30));
		RecipeHandler.addRecipe(new ImplosionCompressorRecipe(ItemDusts.getDustByName("emerald", 4),
				new ItemStack(Blocks.TNT, 24),
				new ItemStack(Items.EMERALD, 3), ItemDusts.getDustByName("darkAshes", 12), 20, 30));
		RecipeHandler.addRecipe(new ImplosionCompressorRecipe(ItemDusts.getDustByName("sapphire", 4),
				new ItemStack(Blocks.TNT, 24),
				ItemGems.getGemByName("sapphire", 3), ItemDusts.getDustByName("darkAshes", 12), 20, 30));
		RecipeHandler.addRecipe(new ImplosionCompressorRecipe(ItemDusts.getDustByName("ruby", 4),
				new ItemStack(Blocks.TNT, 24),
				ItemGems.getGemByName("ruby", 3), ItemDusts.getDustByName("darkAshes", 12), 20, 30));
		RecipeHandler.addRecipe(new ImplosionCompressorRecipe(ItemDusts.getDustByName("yellowGarnet", 4),
				new ItemStack(Blocks.TNT, 24),
				ItemGems.getGemByName("yellowGarnet", 3), ItemDusts.getDustByName("darkAshes", 12), 20, 30));
		RecipeHandler.addRecipe(new ImplosionCompressorRecipe(ItemDusts.getDustByName("redGarnet", 4),
				new ItemStack(Blocks.TNT, 24),
				ItemGems.getGemByName("redGarnet", 3), ItemDusts.getDustByName("darkAshes", 12), 20, 30));
		RecipeHandler.addRecipe(new ImplosionCompressorRecipe(ItemDusts.getDustByName("peridot", 4),
				new ItemStack(Blocks.TNT, 24),
				ItemGems.getGemByName("peridot", 3), ItemDusts.getDustByName("darkAshes", 12), 20, 30));

		// Grinder

		RecipeHandler.addRecipe(new IndustrialGrinderRecipe(
                new ItemStack(ModBlocks.ore, 1, 0),
                new FluidStack(ModFluids.fluidMercury, 1000), 
                ItemDusts.getDustByName("galena", 2),
				ItemDustsSmall.getSmallDustByName("Sulfur", 1), 
                ItemDustsSmall.getSmallDustByName("Silver", 1),
				null, 100, 120));

		RecipeHandler.addRecipe(new IndustrialGrinderRecipe(
                new ItemStack(ModBlocks.ore, 1, 2),
                new FluidStack(FluidRegistry.WATER, 1000), 
                ItemGems.getGemByName("ruby", 1),
				ItemDustsSmall.getSmallDustByName("Ruby", 6), 
                ItemDustsSmall.getSmallDustByName("Chrome", 2),
				null, 100, 120));

		RecipeHandler.addRecipe(new IndustrialGrinderRecipe(
                new ItemStack(ModBlocks.ore, 1, 3),
                new FluidStack(FluidRegistry.WATER, 1000),
                ItemGems.getGemByName("sapphire", 1),
				ItemDustsSmall.getSmallDustByName("Sapphire", 6),
                ItemDustsSmall.getSmallDustByName("Aluminum", 2),
				null, 100, 120));

		RecipeHandler.addRecipe(new IndustrialGrinderRecipe(
                new ItemStack(ModBlocks.ore, 1, 4),
                new FluidStack(FluidRegistry.WATER, 1000),
                ItemDusts.getDustByName("bauxite", 2),
				ItemDustsSmall.getSmallDustByName("Grossular", 4),
                ItemDustsSmall.getSmallDustByName("Titanium", 4),
				null, 100, 120));

		RecipeHandler.addRecipe(new IndustrialGrinderRecipe(
                new ItemStack(ModBlocks.ore, 1, 5),
                new FluidStack(FluidRegistry.WATER, 1000),
                ItemDusts.getDustByName("pyrite", 2),
				ItemDustsSmall.getSmallDustByName("Sulfur", 1),
                ItemDustsSmall.getSmallDustByName("Phosphorous", 1),
				null, 100, 120));

		RecipeHandler.addRecipe(new IndustrialGrinderRecipe(
                new ItemStack(ModBlocks.ore, 1, 6),
                new FluidStack(FluidRegistry.WATER, 1000),
                ItemDusts.getDustByName("cinnabar", 2),
				ItemDustsSmall.getSmallDustByName("Redstone", 1),
                ItemDustsSmall.getSmallDustByName("Glowstone", 1),
				null, 100, 120));

		RecipeHandler.addRecipe(new IndustrialGrinderRecipe(
                new ItemStack(ModBlocks.ore, 1, 7),
                new FluidStack(FluidRegistry.WATER, 1000),
                ItemDusts.getDustByName("sphalerite", 2),
				ItemDustsSmall.getSmallDustByName("Zinc", 1),
                ItemDustsSmall.getSmallDustByName("YellowGarnet", 1),
				null, 100, 120));

		RecipeHandler.addRecipe(new IndustrialGrinderRecipe(
                new ItemStack(ModBlocks.ore, 1, 7),
                new FluidStack(ModFluids.fluidSodiumpersulfate, 1000),
                ItemDusts.getDustByName("sphalerite", 2),
				ItemDusts.getDustByName("zinc", 1),
                ItemDustsSmall.getSmallDustByName("YellowGarnet", 1),
				null, 100, 120));

		RecipeHandler.addRecipe(new IndustrialGrinderRecipe(
                new ItemStack(ModBlocks.ore, 1, 8),
                new FluidStack(FluidRegistry.WATER, 1000),
                ItemDusts.getDustByName("tungsten", 2),
				ItemDustsSmall.getSmallDustByName("Manganese", 1),
                ItemDustsSmall.getSmallDustByName("Silver", 1),
				null, 100, 120));

		RecipeHandler.addRecipe(
				new IndustrialGrinderRecipe(
                        new ItemStack(ModBlocks.ore, 1, 8),
                        new FluidStack(ModFluids.fluidMercury, 1000),
                        ItemDusts.getDustByName("tungsten", 2),
                        ItemDustsSmall.getSmallDustByName("Manganese", 2),
						ItemDusts.getDustByName("silver", 3),
                        null, 100, 120));

		RecipeHandler.addRecipe(new IndustrialGrinderRecipe(
                new ItemStack(ModBlocks.ore, 1, 9),
                new FluidStack(FluidRegistry.WATER, 1000),
                ItemDusts.getDustByName("platinum", 2),
				ItemDusts.getDustByName("nickel", 1),
                ItemNuggets.getNuggetByName("iridium", 2),
				null, 100, 120));

        RecipeHandler.addRecipe(
				new IndustrialGrinderRecipe(
                        new ItemStack(ModBlocks.ore, 1, 9),
                        new FluidStack(ModFluids.fluidMercury, 1000),
                        ItemDusts.getDustByName("platinum", 3),
                        ItemDusts.getDustByName("nickel", 1),
						ItemNuggets.getNuggetByName("iridium", 3),
                        null, 100, 120));

		RecipeHandler.addRecipe(new IndustrialGrinderRecipe(
                new ItemStack(ModBlocks.ore, 1, 10),
                new FluidStack(FluidRegistry.WATER, 1000),
                ItemGems.getGemByName("peridot", 1),
				ItemDustsSmall.getSmallDustByName("Peridot", 6),
                ItemDustsSmall.getSmallDustByName("Pyrope", 2),
				null, 100, 120));

		RecipeHandler.addRecipe(new IndustrialGrinderRecipe(
                new ItemStack(ModBlocks.ore, 1, 11),
                new FluidStack(FluidRegistry.WATER, 1000),
                ItemDusts.getDustByName("sodalite", 12),
				ItemDustsSmall.getSmallDustByName("aluminum", 3),
                null, null, 100, 120));

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
		
        ItemStack waterCells =ItemCells.getCellByName("water").copy();
		waterCells.stackSize = 2;
		
        RecipeHandler.addRecipe(new ChemicalReactorRecipe(ItemCells.getCellByName("sulfur", 1), waterCells,
				ItemCells.getCellByName("sulfuricAcid", 3), 1140, 30));
		
        ItemStack waterCells2 =ItemCells.getCellByName("water").copy();
		waterCells2.stackSize = 5;
		
        RecipeHandler.addRecipe(new ChemicalReactorRecipe(ItemCells.getCellByName("hydrogen", 4),
				ItemCells.getCellByName("empty"), waterCells2, 10, 30));
		
        RecipeHandler.addRecipe(new ChemicalReactorRecipe(ItemCells.getCellByName("nitrogen", 1),
				ItemCells.getCellByName("empty"), ItemCells.getCellByName("nitrogenDioxide", 2), 1240,
				30));

		// IndustrialElectrolyzer

		RecipeHandler.addRecipe(new IndustrialElectrolyzerRecipe(ItemCells.getCellByName("sulfuricAcid", 7), null,
				ItemCells.getCellByName("hydrogen", 2), ItemDusts.getDustByName("sulfur"),
				ItemCells.getCellByName("empty", 2), ItemCells.getCellByName("empty", 3), 400, 90));

		RecipeHandler.addRecipe(
				new IndustrialElectrolyzerRecipe(ItemDusts.getDustByName("ruby", 9), ItemCells.getCellByName("empty"),
						ItemDusts.getDustByName("aluminum", 2), ItemCells.getCellByName("empty", 1),
						ItemDusts.getDustByName("chrome", 1), null, 140, 90));

		RecipeHandler.addRecipe(new IndustrialElectrolyzerRecipe(ItemDusts.getDustByName("sapphire", 8),
				ItemCells.getCellByName("empty"), ItemDusts.getDustByName("aluminum", 2),
				ItemCells.getCellByName("empty"), null, null, 100, 60));

		RecipeHandler.addRecipe(new IndustrialElectrolyzerRecipe(ItemCells.getCellByName("sodiumSulfide", 2), null,
				ItemCells.getCellByName("sodium", 1), ItemDusts.getDustByName("sulfur", 1), null,
				ItemCells.getCellByName("empty"), 200, 60));

		RecipeHandler.addRecipe(new IndustrialElectrolyzerRecipe(ItemDusts.getDustByName("peridot", 5),
				ItemCells.getCellByName("empty"), ItemDusts.getDustByName("aluminum", 2),
				ItemCells.getCellByName("empty"), null, null, 100, 60));

		RecipeHandler.addRecipe(new IndustrialElectrolyzerRecipe(ItemDusts.getDustByName("emerald", 29),
				ItemCells.getCellByName("empty", 18), ItemCells.getCellByName("berylium", 3),
				ItemDusts.getDustByName("aluminum", 2), ItemCells.getCellByName("silicon", 6),
				ItemCells.getCellByName("empty", 9), 520, 120));

		RecipeHandler.addRecipe(
				new IndustrialElectrolyzerRecipe(new ItemStack(Items.DYE, 3, 15), ItemCells.getCellByName("empty", 1),
						null, ItemCells.getCellByName("calcium", 1), null, null, 20, 106));

		RecipeHandler.addRecipe(new IndustrialElectrolyzerRecipe(ItemCells.getCellByName("glyceryl", 20), null,
				ItemCells.getCellByName("carbon", 3), ItemCells.getCellByName("hydrogen", 5),
				ItemCells.getCellByName("nitrogen", 3), ItemCells.getCellByName("empty", 9), 800, 90));

		RecipeHandler.addRecipe(new IndustrialElectrolyzerRecipe(ItemDusts.getDustByName("peridot", 9),
				ItemCells.getCellByName("empty", 4), ItemDusts.getDustByName("magnesium", 2),
				ItemDusts.getDustByName("iron"), ItemCells.getCellByName("silicon", 2),
				ItemCells.getCellByName("empty", 2), 200, 120));

		RecipeHandler.addRecipe(new IndustrialElectrolyzerRecipe(ItemCells.getCellByName("calciumCarbonate", 5), null,
				ItemCells.getCellByName("carbon"), ItemCells.getCellByName("calcium"),
				ItemCells.getCellByName("empty", 1), ItemCells.getCellByName("empty", 2), 400, 90));

		RecipeHandler.addRecipe(new IndustrialElectrolyzerRecipe(ItemCells.getCellByName("sodiumPersulfate", 6), null,
				ItemCells.getCellByName("sodium"), ItemDusts.getDustByName("sulfur"),
				ItemCells.getCellByName("empty", 2), ItemCells.getCellByName("empty", 3), 420, 90));

		RecipeHandler.addRecipe(new IndustrialElectrolyzerRecipe(ItemDusts.getDustByName("pyrope", 20),
				ItemCells.getCellByName("empty", 9), ItemDusts.getDustByName("aluminum", 2),
				ItemDusts.getDustByName("magnesium", 3), ItemCells.getCellByName("silicon", 3),
				ItemCells.getCellByName("empty", 6), 400, 120));

		ItemStack sand = new ItemStack(Blocks.SAND);
		sand.stackSize = 16;

		RecipeHandler.addRecipe(new IndustrialElectrolyzerRecipe(sand, ItemCells.getCellByName("empty", 2),
				ItemCells.getCellByName("silicon", 1), ItemCells.getCellByName("empty"), null, null, 1000, 25));

		RecipeHandler.addRecipe(new IndustrialElectrolyzerRecipe(ItemDusts.getDustByName("almandine", 20),
				ItemCells.getCellByName("empty", 9), ItemDusts.getDustByName("aluminum", 2),
				ItemDusts.getDustByName("iron", 3), ItemCells.getCellByName("silicon", 3),
				ItemCells.getCellByName("empty", 6), 480, 120));

		RecipeHandler.addRecipe(new IndustrialElectrolyzerRecipe(ItemDusts.getDustByName("spessartine", 20),
				ItemCells.getCellByName("empty", 9), ItemDusts.getDustByName("aluminum", 2),
				ItemDusts.getDustByName("manganese", 3), ItemCells.getCellByName("silicon", 3),
				ItemCells.getCellByName("empty", 6), 480, 120));

		RecipeHandler.addRecipe(new IndustrialElectrolyzerRecipe(ItemDusts.getDustByName("andradite", 20),
				ItemCells.getCellByName("empty", 12), ItemCells.getCellByName("calcium", 3),
				ItemDusts.getDustByName("iron", 2), ItemCells.getCellByName("silicon", 3),
				ItemCells.getCellByName("empty", 6), 480, 120));

		RecipeHandler.addRecipe(new IndustrialElectrolyzerRecipe(ItemDusts.getDustByName("grossular", 20),
				ItemCells.getCellByName("empty", 12), ItemCells.getCellByName("calcium", 3),
				ItemDusts.getDustByName("aluminum", 2), ItemCells.getCellByName("silicon", 3),
				ItemCells.getCellByName("empty", 6), 440, 120));

		RecipeHandler.addRecipe(new IndustrialElectrolyzerRecipe(ItemDusts.getDustByName("Uvarovite", 20),
				ItemCells.getCellByName("empty", 12), ItemCells.getCellByName("calcium", 3),
				ItemDusts.getDustByName("chrome", 2), ItemCells.getCellByName("silicon", 3),
				ItemCells.getCellByName("empty", 6), 480, 120));

//		RecipeHandler.addRecipe(new IndustrialElectrolyzerRecipe(ItemCells.getCellByName("empty", 6), null,
//				ItemCells.getCellByName("hydrogen", 4), ItemCells.getCellByName("empty", 5),
//				ItemCells.getCellByName("empty", 1), null, 100, 30));

		RecipeHandler.addRecipe(new IndustrialElectrolyzerRecipe(ItemDusts.getDustByName("darkAshes"),
				ItemCells.getCellByName("empty", 2), ItemCells.getCellByName("carbon", 2), null, null, null, 20, 30));

		if (OreUtil.doesOreExistAndValid("dustSalt"))
		{
			ItemStack salt = OreDictionary.getOres("dustSalt").get(0);
			salt.stackSize = 2;
			RecipeHandler.addRecipe(new IndustrialElectrolyzerRecipe(salt, ItemCells.getCellByName("empty", 2),
					ItemCells.getCellByName("sodium"), ItemCells.getCellByName("chlorine"), null, null, 40, 60));
		}

		Item drill = OreDictionary.getOres("drillBasic").get(0).getItem();
		ItemStack drillStack = new ItemStack(drill, 1, OreDictionary.WILDCARD_VALUE);

		if (ConfigTechReborn.ExpensiveMacerator)
			CraftingHelper
					.addShapedOreRecipe(getOre("ic2Macerator"), "FDF", "DMD", "FCF", 'F',
							Items.FLINT, 'D', Items.DIAMOND, 'M', "machineBlockBasic", 'C',
							"circuitBasic");

		if (ConfigTechReborn.ExpensiveDrill)
			CraftingHelper
					.addShapedOreRecipe(OreDictionary.getOres("drillBasic").get(0).copy(), " S ", "SCS", "SBS", 'S',
							"ingotSteel", 'B', "reBattery", 'C',
							"circuitBasic");

		if (ConfigTechReborn.ExpensiveDiamondDrill)
			CraftingHelper
					.addShapedOreRecipe(OreDictionary.getOres("drillDiamond").get(0).copy(), " D ", "DBD", "TCT", 'D',
							"diamondTR", 'T', "ingotTitanium", 'B', drillStack, 'C',
							"circuitAdvanced");

		if (ConfigTechReborn.ExpensiveSolar)
			CraftingHelper
					.addShapedOreRecipe(OreDictionary.getOres("ic2SolarPanel").get(0).copy(), "PPP", "SZS", "CGC", 'P',
							"paneGlass", 'S', ItemPlates.getPlateByName("silicon"), 'Z',
							"plateCarbon", 'G',
							"ic2Generator", 'C',
							"circuitBasic");

		CraftingHelper.addShapedOreRecipe(ItemIngots.getIngotByName("iridiumAlloy"), "IAI", "ADA", "IAI", 'I',
				"ingotIridium", 'D', ItemDusts.getDustByName("diamond"), 'A',
				"plateAdvancedAlloy");

		CraftingHelper
				.addShapedOreRecipe(new ItemStack(ModItems.lithiumBatpack, 1, OreDictionary.WILDCARD_VALUE), "BCB",
						"BPB", "B B", 'B', new ItemStack(ModItems.lithiumBattery), 'P', "plateAluminum", 'C',
						"circuitAdvanced");

		CraftingHelper
				.addShapedOreRecipe(new ItemStack(ModItems.lithiumBattery, 1, OreDictionary.WILDCARD_VALUE), " C ",
						"PFP", "PFP", 'F', ItemCells.getCellByName("lithium"), 'P', "plateAluminum", 'C',
						"insulatedGoldCableItem");

		CraftingHelper
				.addShapedOreRecipe(new ItemStack(ModItems.lapotronpack, 1, OreDictionary.WILDCARD_VALUE), "FOF", "SPS",
						"FIF", 'F', "circuitMaster", 'O',
						new ItemStack(ModItems.lapotronicOrb), 'S', ItemParts.getPartByName("superConductor"), 'I',
						"ingotIridium", 'P', new ItemStack(ModItems.lithiumBatpack));
	}

	static void addGemToolRecipes(ItemStack gemsword, ItemStack gempick, ItemStack gemaxe, ItemStack gemHoe,
								  ItemStack gemspade, ItemStack gemhelmet, ItemStack gemchestplate, ItemStack gemleggings, ItemStack gemboots,
								  String gem)
	{
		CraftingHelper.addShapedOreRecipe(gemsword, " G ", " G ", " S ", 'S', Items.STICK, 'G', gem);

		CraftingHelper.addShapedOreRecipe(gempick, "GGG", " S ", " S ", 'S', Items.STICK, 'G', gem);

		CraftingHelper.addShapedOreRecipe(gemaxe, " GG", " SG", " S ", 'S', Items.STICK, 'G', gem);

		CraftingHelper.addShapedOreRecipe(gemHoe, " GG", " S ", " S ", 'S', Items.STICK, 'G', gem);

		CraftingHelper.addShapedOreRecipe(gemspade, " G ", " S ", " S ", 'S', Items.STICK, 'G', gem);

		CraftingHelper.addShapedOreRecipe(gemhelmet, "GGG", "G G", "   ", 'G', gem);

		CraftingHelper.addShapedOreRecipe(gemchestplate, "G G", "GGG", "GGG", 'G', gem);

		CraftingHelper.addShapedOreRecipe(gemleggings, "GGG", "G G", "G G", 'G', gem);

		CraftingHelper.addShapedOreRecipe(gemboots, "   ", "G G", "G G", 'G', gem);
	}

	public static ItemStack getBucketWithFluid(Fluid fluid)
	{
		return UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, fluid);
	}

	public static ItemStack getOre(String name) {
		if(OreDictionary.getOres(name).isEmpty()){
			return new ItemStack(ModItems.missingRecipe);
		}
		return OreDictionary.getOres(name).get(0).copy();
	}

}
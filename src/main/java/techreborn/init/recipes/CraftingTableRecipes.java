package techreborn.init.recipes;

import com.google.common.base.CaseFormat;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.ArrayUtils;
import reborncore.common.util.CraftingHelper;
import techreborn.blocks.BlockStorage;
import techreborn.blocks.BlockStorage2;
import techreborn.config.ConfigTechReborn;
import techreborn.init.IC2Duplicates;
import techreborn.init.ModBlocks;
import techreborn.init.ModItems;
import techreborn.items.*;

import java.security.InvalidParameterException;

/**
 * Created by Prospector
 */
public class CraftingTableRecipes extends RecipeMethods {
	public static void init() {
		registerCompressionRecipes();
		registerShapeless(BlockStorage2.getStorageBlockByName("iridium_reinforced_stone", 1), new ItemStack(Blocks.STONE), ItemIngots.getIngotByName("iridium"));
		registerShapeless(BlockStorage2.getStorageBlockByName("iridium_reinforced_tungstensteel", 1), BlockStorage2.getStorageBlockByName("tungstensteel", 1), ItemIngots.getIngotByName("iridium"));
		registerShapeless(BlockStorage2.getStorageBlockByName("iridium_reinforced_tungstensteel", 1), BlockStorage2.getStorageBlockByName("iridium_reinforced_stone", 1), ItemIngots.getIngotByName("tungstensteel"));

		registerShapeless(new ItemStack(ModBlocks.RUBBER_PLANKS, 4), new ItemStack(ModBlocks.RUBBER_LOG));

		registerShapeless(new ItemStack(ModItems.FREQUENCY_TRANSMITTER), IC2Duplicates.CABLE_ICOPPER.getStackBasedOnConfig(), "circuitBasic");

		registerShaped(DynamicCell.getEmptyCell(16), " T ", "T T", " T ", 'T', "ingotTin");
		registerShaped(new ItemStack(ModBlocks.REFINED_IRON_FENCE), "RRR", "RRR", 'R', IC2Duplicates.REFINED_IRON.getStackBasedOnConfig());

		registerShaped(new ItemStack(ModItems.STEEL_DRILL), " S ", "SCS", "SBS", 'S', "ingotSteel", 'C', "circuitBasic", 'B', "reBattery");
		registerShaped(new ItemStack(ModItems.DIAMOND_DRILL), " D ", "DCD", "TST", 'D', "gemDiamond", 'C', "circuitAdvanced", 'S', new ItemStack(ModItems.STEEL_DRILL, 1, OreDictionary.WILDCARD_VALUE), 'T', "ingotTitanium");
		registerShaped(new ItemStack(ModItems.ADVANCED_DRILL), " I ", "NCN", "OAO", 'I', "ingotIridium", 'N', "nuggetIridium", 'A', new ItemStack(ModItems.DIAMOND_DRILL, 1, OreDictionary.WILDCARD_VALUE), 'C', "circuitMaster", 'O', ItemUpgrades.getUpgradeByName("overclock"));

		registerShaped(new ItemStack(ModItems.STEEL_CHAINSAW), " SS", "SCS", "BS ", 'S', "ingotSteel", 'C', "circuitBasic", 'B', "reBattery");
		registerShaped(new ItemStack(ModItems.DIAMOND_CHAINSAW), " DD", "TCD", "ST ", 'D', "gemDiamond", 'C', "circuitAdvanced", 'S', new ItemStack(ModItems.STEEL_CHAINSAW, 1, OreDictionary.WILDCARD_VALUE), 'T', "ingotTitanium");
		registerShaped(new ItemStack(ModItems.ADVANCED_CHAINSAW), " NI", "OCN", "DO ", 'I', "ingotIridium", 'N', "nuggetIridium", 'D', new ItemStack(ModItems.DIAMOND_CHAINSAW, 1, OreDictionary.WILDCARD_VALUE), 'C', "circuitMaster", 'O', ItemUpgrades.getUpgradeByName("overclock"));

		registerShaped(new ItemStack(ModItems.STEEL_JACKHAMMER), "SBS", "SCS", " S ", 'S', "ingotSteel", 'C', "circuitBasic", 'B', "reBattery");
		registerShaped(new ItemStack(ModItems.DIAMOND_JACKHAMMER), "DSD", "TCT", " D ", 'D', "gemDiamond", 'C', "circuitAdvanced", 'S', new ItemStack(ModItems.STEEL_JACKHAMMER, 1, OreDictionary.WILDCARD_VALUE), 'T', "ingotTitanium");
		registerShaped(new ItemStack(ModItems.ADVANCED_JACKHAMMER), "NDN", "OCO", " I ", 'I', "ingotIridium", 'N', "nuggetIridium", 'D', new ItemStack(ModItems.DIAMOND_DRILL, 1, OreDictionary.WILDCARD_VALUE), 'C', "circuitMaster", 'O', ItemUpgrades.getUpgradeByName("overclock"));

		if (ConfigTechReborn.enableGemArmorAndTools) {
			addToolAndArmourRecipes(new ItemStack(ModItems.RUBY_SWORD), new ItemStack(ModItems.RUBY_PICKAXE),
				new ItemStack(ModItems.RUBY_AXE), new ItemStack(ModItems.RUBY_HOE), new ItemStack(ModItems.RUBY_SPADE),
				new ItemStack(ModItems.RUBY_HELMET), new ItemStack(ModItems.RUBY_CHESTPLATE),
				new ItemStack(ModItems.RUBY_LEGGINGS), new ItemStack(ModItems.RUBY_BOOTS), "gemRuby");

			addToolAndArmourRecipes(new ItemStack(ModItems.SAPPHIRE_SWORD), new ItemStack(ModItems.SAPPHIRE_PICKAXE),
				new ItemStack(ModItems.SAPPHIRE_AXE), new ItemStack(ModItems.SAPPHIRE_HOE),
				new ItemStack(ModItems.SAPPHIRE_SPADE), new ItemStack(ModItems.SAPPHIRE_HELMET),
				new ItemStack(ModItems.SAPPHIRE_CHSTPLATE), new ItemStack(ModItems.SAPPHIRE_LEGGINGS),
				new ItemStack(ModItems.SAPPHIRE_BOOTS), "gemSapphire");

			addToolAndArmourRecipes(new ItemStack(ModItems.PERIDOT_SWORD), new ItemStack(ModItems.PERIDOT_PICKAXE),
				new ItemStack(ModItems.PERIDOT_AXE), new ItemStack(ModItems.PERIDOT_HOE),
				new ItemStack(ModItems.PERIDOT_SAPPHIRE), new ItemStack(ModItems.PERIDOT_HELMET),
				new ItemStack(ModItems.PERIDOT_CHESTPLATE), new ItemStack(ModItems.PERIDOT_LEGGINGS),
				new ItemStack(ModItems.PERIDOT_BOOTS), "gemPeridot");

			addToolAndArmourRecipes(new ItemStack(ModItems.BRONZE_SWORD), new ItemStack(ModItems.BRONZE_PICKAXE),
				new ItemStack(ModItems.BRONZE_AXE), new ItemStack(ModItems.BRONZE_HOE),
				new ItemStack(ModItems.BRONZE_SPADE), new ItemStack(ModItems.BRONZE_HELMET),
				new ItemStack(ModItems.BRONZE_CHESTPLATE), new ItemStack(ModItems.BRONZE_LEGGINGS),
				new ItemStack(ModItems.BRONZE_BOOTS), "ingotBronze");
		}

		if (!IC2Duplicates.deduplicate()) {
			registerShaped(getMaterial("copper", 6, Type.CABLE), "CCC", 'C', "ingotCopper");
			registerShaped(getMaterial("tin", 9, Type.CABLE), "TTT", 'T', "ingotTin");
			registerShaped(getMaterial("gold", 12, Type.CABLE), "GGG", 'G', "ingotGold");
			registerShaped(getMaterial("hv", 12, Type.CABLE), "RRR", 'R', "ingotRefinedIron");

			registerShaped(getMaterial("insulatedcopper", 6, Type.CABLE), "RRR", "CCC", "RRR", 'R', "itemRubber", 'C', "ingotCopper");
			registerShaped(getMaterial("insulatedcopper", 6, Type.CABLE), "CRC", "CRC", "CRC", 'R', "itemRubber", 'C', "ingotCopper");
			registerShapeless(getMaterial("insulatedcopper", Type.CABLE), "itemRubber", getMaterial("copper", Type.CABLE));

			registerShaped(getMaterial("insulatedgold", 4, Type.CABLE), "RRR", "RGR", "RRR", 'R', "itemRubber", 'G', "ingotGold");
			registerShapeless(getMaterial("insulatedgold", Type.CABLE), "itemRubber", "itemRubber", getMaterial("gold", Type.CABLE));

			registerShaped(getMaterial("insulatedhv", 4, Type.CABLE), "RRR", "RIR", "RRR", 'R', "itemRubber", 'I', "ingotRefinedIron");
			registerShapeless(getMaterial("insulatedhv", Type.CABLE), "itemRubber", "itemRubber", getMaterial("hv", Type.CABLE));

			registerShaped(getMaterial("glassfiber", 4, Type.CABLE), "GGG", "RDR", "GGG", 'R', "dustRedstone", 'D', "gemDiamond", 'G', "blockGlass");
			registerShaped(getMaterial("glassfiber", 4, Type.CABLE), "GGG", "RDR", "GGG", 'R', "dustRedstone", 'D', "dustDiamond", 'G', "blockGlass");

			registerShaped(getMaterial("glassfiber", 3, Type.CABLE), "GGG", "RDR", "GGG", 'R', "dustRedstone", 'D', "gemRuby", 'G', "blockGlass");
			registerShaped(getMaterial("glassfiber", 3, Type.CABLE), "GGG", "RDR", "GGG", 'R', "dustRedstone", 'D', "dustRuby", 'G', "blockGlass");

			registerShaped(getMaterial("glassfiber", 6, Type.CABLE), "GGG", "RDR", "GGG", 'R', "ingotSilver", 'D', "gemDiamond", 'G', "blockGlass");
			registerShaped(getMaterial("glassfiber", 6, Type.CABLE), "GGG", "RDR", "GGG", 'R', "ingotSilver", 'D', "dustDiamond", 'G', "blockGlass");

			registerShaped(getMaterial("glassfiber", 8, Type.CABLE), "GGG", "RDR", "GGG", 'R', "ingotElectrum", 'D', "gemDiamond", 'G', "blockGlass");
			registerShaped(getMaterial("glassfiber", 8, Type.CABLE), "GGG", "RDR", "GGG", 'R', "ingotElectrum", 'D', "dustDiamond", 'G', "blockGlass");
		}

		if (ConfigTechReborn.UUrecipesWood)
			registerShaped(new ItemStack(Blocks.LOG, 8), " U ", "   ", "   ", 'U', ModItems.UU_MATTER);

		if (ConfigTechReborn.UUrecipesStone)
			registerShaped(new ItemStack(Blocks.STONE, 16), "   ", " U ", "   ", 'U', ModItems.UU_MATTER);

		if (ConfigTechReborn.UUrecipesSnowBlock)
			registerShaped(new ItemStack(Blocks.SNOW, 16), "U U", "   ", "   ", 'U', ModItems.UU_MATTER);

		if (ConfigTechReborn.UUrecipesGrass)
			registerShaped(new ItemStack(Blocks.GRASS, 16), "   ", "U  ", "U  ", 'U', ModItems.UU_MATTER);

		if (ConfigTechReborn.UUrecipesObsidian)
			registerShaped(new ItemStack(Blocks.OBSIDIAN, 12), "U U", "U U", "   ", 'U', ModItems.UU_MATTER);

		if (ConfigTechReborn.UUrecipesGlass)
			registerShaped(new ItemStack(Blocks.GLASS, 32), " U ", "U U", " U ", 'U', ModItems.UU_MATTER);

		if (ConfigTechReborn.UUrecipesCocoa)
			registerShaped(new ItemStack(Items.DYE, 32, 3), "UU ", "  U", "UU ", 'U', ModItems.UU_MATTER);

		if (ConfigTechReborn.UUrecipesGlowstoneBlock)
			registerShaped(new ItemStack(Blocks.GLOWSTONE, 8), " U ", "U U", "UUU", 'U', ModItems.UU_MATTER);

		if (ConfigTechReborn.UUrecipesCactus)
			registerShaped(new ItemStack(Blocks.CACTUS, 48), " U ", "UUU", "U U", 'U', ModItems.UU_MATTER);

		if (ConfigTechReborn.UUrecipesSugarCane)
			registerShaped(new ItemStack(Items.REEDS, 48), "U U", "U U", "U U", 'U', ModItems.UU_MATTER);

		if (ConfigTechReborn.UUrecipesVine)
			registerShaped(new ItemStack(Blocks.VINE, 24), "U  ", "U  ", "U  ", 'U', ModItems.UU_MATTER);

		if (ConfigTechReborn.UUrecipesSnowBall)
			registerShaped(new ItemStack(Items.SNOWBALL, 16), "   ", "   ", "UUU", 'U', ModItems.UU_MATTER);
		registerShaped(new ItemStack(Items.CLAY_BALL, 48), "UU ", "U  ", "UU ", 'U', ModItems.UU_MATTER);

		if (ConfigTechReborn.UUrecipeslilypad)
			registerShaped(new ItemStack(Blocks.WATERLILY, 64), "U U", " U ", " U ", 'U', ModItems.UU_MATTER);

		if (ConfigTechReborn.UUrecipesGunpowder)
			registerShaped(new ItemStack(Items.GUNPOWDER, 15), "UUU", "U  ", "UUU", 'U', ModItems.UU_MATTER);

		if (ConfigTechReborn.UUrecipesBone)
			registerShaped(new ItemStack(Items.BONE, 32), "U  ", "UU ", "U  ", 'U', ModItems.UU_MATTER);

		if (ConfigTechReborn.UUrecipesFeather)
			registerShaped(new ItemStack(Items.FEATHER, 32), " U ", " U ", "U U", 'U', ModItems.UU_MATTER);

		if (ConfigTechReborn.UUrecipesInk)
			registerShaped(new ItemStack(Items.DYE, 48), " UU", " UU", " U ", 'U', ModItems.UU_MATTER);

		if (ConfigTechReborn.UUrecipesEnderPearl)
			registerShaped(new ItemStack(Items.ENDER_PEARL, 1), "UUU", "U U", " U ", 'U', ModItems.UU_MATTER);

		if (ConfigTechReborn.UUrecipesCoal)
			registerShaped(new ItemStack(Items.COAL, 5), "  U", "U  ", "  U", 'U', ModItems.UU_MATTER);

		if (ConfigTechReborn.UUrecipesIronOre)
			registerShaped(new ItemStack(Blocks.IRON_ORE, 2), "U U", " U ", "U U", 'U', ModItems.UU_MATTER);

		if (ConfigTechReborn.UUrecipesGoldOre)
			registerShaped(new ItemStack(Blocks.GOLD_ORE, 2), " U ", "UUU", " U ", 'U', ModItems.UU_MATTER);

		if (ConfigTechReborn.UUrecipesRedStone)
			registerShaped(new ItemStack(Items.REDSTONE, 24), "   ", " U ", "UUU", 'U', ModItems.UU_MATTER);

		if (ConfigTechReborn.UUrecipesLapis)
			registerShaped(new ItemStack(Items.DYE, 9, 4), " U ", " U ", " UU", 'U', ModItems.UU_MATTER);

		if (ConfigTechReborn.UUrecipesEmeraldOre)
			registerShaped(new ItemStack(Blocks.EMERALD_ORE, 1), "UU ", "U U", " UU", 'U', ModItems.UU_MATTER);

		if (ConfigTechReborn.UUrecipesEmerald)
			registerShaped(new ItemStack(Items.EMERALD, 2), "UUU", "UUU", " U ", 'U', ModItems.UU_MATTER);

		if (ConfigTechReborn.UUrecipesDiamond)
			registerShaped(new ItemStack(Items.DIAMOND, 1), "UUU", "UUU", "UUU", 'U', ModItems.UU_MATTER);

		if (ConfigTechReborn.UUrecipesTinDust)
			registerShaped(getMaterial("tin", 10, Type.DUST), "   ", "U U", "  U", 'U', ModItems.UU_MATTER);

		if (ConfigTechReborn.UUrecipesCopperDust)
			registerShaped(getMaterial("copper", 10, Type.DUST), "  U", "U U", "   ", 'U', ModItems.UU_MATTER);

		if (ConfigTechReborn.UUrecipesLeadDust)
			registerShaped(getMaterial("lead", 14, Type.DUST), "UUU", "UUU", "U  ", 'U', ModItems.UU_MATTER);

		if (ConfigTechReborn.UUrecipesPlatinumDust)
			registerShaped(getMaterial("platinum", Type.DUST), "  U", "UUU", "UUU", 'U', ModItems.UU_MATTER);

		if (ConfigTechReborn.UUrecipesTungstenDust)
			registerShaped(getMaterial("tungsten", Type.DUST), "U  ", "UUU", "UUU", 'U', ModItems.UU_MATTER);

		if (ConfigTechReborn.UUrecipesTitaniumDust)
			registerShaped(getMaterial("titanium", 2, Type.DUST), "UUU", " U ", " U ", 'U', ModItems.UU_MATTER);

		if (ConfigTechReborn.UUrecipesAluminumDust)
			registerShaped(getMaterial("aluminum", 16, Type.DUST), " U ", " U ", "UUU", 'U', ModItems.UU_MATTER);
	}

	static void registerCompressionRecipes() {
		for (String name : ArrayUtils.addAll(BlockStorage.types, BlockStorage2.types)) {
			ItemStack item = ItemStack.EMPTY;
			try {
				item = ItemIngots.getIngotByName(name, 9);
			} catch (InvalidParameterException e) {
				try {
					item = ItemGems.getGemByName(name, 9);
				} catch (InvalidParameterException e2) {
					continue;
				}
			}

			if (!item.isEmpty()) {
				registerShaped(BlockStorage.getStorageBlockByName(name), "III", "III", "III", 'I', item);
				registerShapeless(item, BlockStorage.getStorageBlockByName(name, 9));
			}
		}

		for (String name : ArrayUtils.addAll(BlockStorage.types, BlockStorage2.types)) {
			registerShaped(BlockStorage.getStorageBlockByName(name), "AAA", "AAA", "AAA", 'A',
				"ingot" + name.substring(0, 1).toUpperCase() + name.substring(1));
			registerShaped(BlockStorage.getStorageBlockByName(name), "AAA", "AAA", "AAA", 'A',
				"gem" + name.substring(0, 1).toUpperCase() + name.substring(1));
		}

		for (String name : ItemDustsSmall.types) {
			if (name.equals(ModItems.META_PLACEHOLDER)) {
				continue;
			}
			registerShapeless(ItemDustsSmall.getSmallDustByName(name, 4), ItemDusts.getDustByName(name));
			registerShapeless(ItemDusts.getDustByName(name), ItemDustsSmall.getSmallDustByName(name),
				ItemDustsSmall.getSmallDustByName(name), ItemDustsSmall.getSmallDustByName(name),
				ItemDustsSmall.getSmallDustByName(name));
		}

		for (String nuggets : ItemNuggets.types) {
			if (nuggets.equals(ModItems.META_PLACEHOLDER) || nuggets.equalsIgnoreCase("diamond"))
				continue;
			registerShapeless(ItemNuggets.getNuggetByName(nuggets, 9), CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "ingot_" + nuggets));
			registerShaped(ItemIngots.getIngotByName(nuggets), "NNN", "NNN", "NNN", 'N', CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "nugget_" + nuggets));
		}

		registerShapeless(ItemNuggets.getNuggetByName("diamond", 9), "gemDiamond");
		registerShaped(new ItemStack(Items.DIAMOND), "NNN", "NNN", "NNN", 'N', "nuggetDiamond");
	}

	static void registerMixedMetalIngotRecipes() {
		if (!IC2Duplicates.deduplicate()) {
			registerShaped(ItemIngots.getIngotByName("mixed_metal", 2), "RRR", "BBB", "TTT", 'R',
				"ingotRefinedIron", 'B', "ingotBronze", 'T', "ingotTin");

			registerShaped(ItemIngots.getIngotByName("mixed_metal", 2), "RRR", "BBB", "TTT", 'R',
				"ingotRefinedIron", 'B', "ingotBronze", 'T', "ingotZinc");

			registerShaped(ItemIngots.getIngotByName("mixed_metal", 2), "RRR", "BBB", "TTT", 'R',
				"ingotRefinedIron", 'B', "ingotBrass", 'T', "ingotTin");

			registerShaped(ItemIngots.getIngotByName("mixed_metal", 2), "RRR", "BBB", "TTT", 'R',
				"ingotRefinedIron", 'B', "ingotBrass", 'T', "ingotZinc");

			registerShaped(ItemIngots.getIngotByName("mixed_metal", 3), "RRR", "BBB", "TTT", 'R', "ingotNickel",
				'B', "ingotBronze", 'T', "ingotTin");

			registerShaped(ItemIngots.getIngotByName("mixed_metal", 3), "RRR", "BBB", "TTT", 'R', "ingotNickel",
				'B', "ingotBronze", 'T', "ingotZinc");

			registerShaped(ItemIngots.getIngotByName("mixed_metal", 3), "RRR", "BBB", "TTT", 'R', "ingotNickel",
				'B', "ingotBrass", 'T', "ingotTin");

			registerShaped(ItemIngots.getIngotByName("mixed_metal", 3), "RRR", "BBB", "TTT", 'R', "ingotNickel",
				'B', "ingotBrass", 'T', "ingotZinc");

			registerShaped(ItemIngots.getIngotByName("mixed_metal", 4), "RRR", "BBB", "TTT", 'R', "ingotNickel",
				'B', "ingotBronze", 'T', "ingotAluminum");

			registerShaped(ItemIngots.getIngotByName("mixed_metal", 4), "RRR", "BBB", "TTT", 'R', "ingotNickel",
				'B', "ingotBrass", 'T', "ingotAluminum");

			registerShaped(ItemIngots.getIngotByName("mixed_metal", 4), "RRR", "BBB", "TTT", 'R', "ingotInvar",
				'B', "ingotBronze", 'T', "ingotTin");

			registerShaped(ItemIngots.getIngotByName("mixed_metal", 4), "RRR", "BBB", "TTT", 'R', "ingotInvar",
				'B', "ingotBronze", 'T', "ingotZinc");

			registerShaped(ItemIngots.getIngotByName("mixed_metal", 4), "RRR", "BBB", "TTT", 'R', "ingotInvar",
				'B', "ingotBrass", 'T', "ingotTin");

			registerShaped(ItemIngots.getIngotByName("mixed_metal", 4), "RRR", "BBB", "TTT", 'R', "ingotInvar",
				'B', "ingotBrass", 'T', "ingotZinc");

			registerShaped(ItemIngots.getIngotByName("mixed_metal", 5), "RRR", "BBB", "TTT", 'R', "ingotInvar",
				'B', "ingotBronze", 'T', "ingotAluminum");

			registerShaped(ItemIngots.getIngotByName("mixed_metal", 5), "RRR", "BBB", "TTT", 'R', "ingotInvar",
				'B', "ingotBrass", 'T', "ingotAluminum");

			registerShaped(ItemIngots.getIngotByName("mixed_metal", 5), "RRR", "BBB", "TTT", 'R',
				"ingotTitanium", 'B', "ingotBronze", 'T', "ingotTin");

			registerShaped(ItemIngots.getIngotByName("mixed_metal", 5), "RRR", "BBB", "TTT", 'R',
				"ingotTitanium", 'B', "ingotBronze", 'T', "ingotZinc");

			registerShaped(ItemIngots.getIngotByName("mixed_metal", 5), "RRR", "BBB", "TTT", 'R',
				"ingotTitanium", 'B', "ingotBrass", 'T', "ingotTin");

			registerShaped(ItemIngots.getIngotByName("mixed_metal", 5), "RRR", "BBB", "TTT", 'R',
				"ingotTitanium", 'B', "ingotBrass", 'T', "ingotZinc");

			registerShaped(ItemIngots.getIngotByName("mixed_metal", 6), "RRR", "BBB", "TTT", 'R',
				"ingotTitanium", 'B', "ingotBronze", 'T', "ingotAluminum");

			registerShaped(ItemIngots.getIngotByName("mixed_metal", 6), "RRR", "BBB", "TTT", 'R',
				"ingotTitanium", 'B', "ingotBrass", 'T', "ingotAluminum");

			registerShaped(ItemIngots.getIngotByName("mixed_metal", 5), "RRR", "BBB", "TTT", 'R',
				"ingotTungsten", 'B', "ingotBronze", 'T', "ingotTin");

			registerShaped(ItemIngots.getIngotByName("mixed_metal", 5), "RRR", "BBB", "TTT", 'R',
				"ingotTungsten", 'B', "ingotBronze", 'T', "ingotZinc");

			registerShaped(ItemIngots.getIngotByName("mixed_metal", 5), "RRR", "BBB", "TTT", 'R',
				"ingotTungsten", 'B', "ingotBrass", 'T', "ingotTin");

			registerShaped(ItemIngots.getIngotByName("mixed_metal", 5), "RRR", "BBB", "TTT", 'R',
				"ingotTungsten", 'B', "ingotBrass", 'T', "ingotZinc");

			registerShaped(ItemIngots.getIngotByName("mixed_metal", 6), "RRR", "BBB", "TTT", 'R',
				"ingotTungsten", 'B', "ingotBronze", 'T', "ingotAluminum");

			registerShaped(ItemIngots.getIngotByName("mixed_metal", 6), "RRR", "BBB", "TTT", 'R',
				"ingotTungsten", 'B', "ingotBrass", 'T', "ingotAluminum");

			registerShaped(ItemIngots.getIngotByName("mixed_metal", 8), "RRR", "BBB", "TTT", 'R',
				"ingotTungstensteel", 'B', "ingotBronze", 'T', "ingotTin");

			registerShaped(ItemIngots.getIngotByName("mixed_metal", 8), "RRR", "BBB", "TTT", 'R',
				"ingotTungstensteel", 'B', "ingotBronze", 'T', "ingotZinc");

			registerShaped(ItemIngots.getIngotByName("mixed_metal", 8), "RRR", "BBB", "TTT", 'R',
				"ingotTungstensteel", 'B', "ingotBrass", 'T', "ingotTin");

			registerShaped(ItemIngots.getIngotByName("mixed_metal", 8), "RRR", "BBB", "TTT", 'R',
				"ingotTungstensteel", 'B', "ingotBrass", 'T', "ingotZinc");

			registerShaped(ItemIngots.getIngotByName("mixed_metal", 9), "RRR", "BBB", "TTT", 'R',
				"ingotTungstensteel", 'B', "ingotBronze", 'T', "ingotAluminum");

			registerShaped(ItemIngots.getIngotByName("mixed_metal", 9), "RRR", "BBB", "TTT", 'R',
				"ingotTungstensteel", 'B', "ingotBrass", 'T', "ingotAluminum");
		}
	}

	static void registerShaped(ItemStack output, Object... inputs) {
		CraftingHelper.addShapedOreRecipe(output, inputs);
	}

	static void registerShapeless(ItemStack output, Object... inputs) {
		CraftingHelper.addShapelessOreRecipe(output, inputs);
	}

	static void addToolAndArmourRecipes(ItemStack sword,
	                                    ItemStack pickaxe,
	                                    ItemStack axe,
	                                    ItemStack hoe,
	                                    ItemStack spade,
	                                    ItemStack helmet,
	                                    ItemStack chestplate,
	                                    ItemStack leggings,
	                                    ItemStack boots,
	                                    String material) {
		registerShaped(sword, "G", "G", "S", 'S', Items.STICK, 'G', material);
		registerShaped(pickaxe, "GGG", " S ", " S ", 'S', Items.STICK, 'G', material);
		registerShaped(axe, "GG", "GS", " S", 'S', Items.STICK, 'G', material);
		registerShaped(hoe, "GG", " S", " S", 'S', Items.STICK, 'G', material);
		registerShaped(spade, "G", "S", "S", 'S', Items.STICK, 'G', material);
		registerShaped(helmet, "GGG", "G G", 'G', material);
		registerShaped(chestplate, "G G", "GGG", "GGG", 'G', material);
		registerShaped(leggings, "GGG", "G G", "G G", 'G', material);
		registerShaped(boots, "G G", "G G", 'G', material);
	}
}

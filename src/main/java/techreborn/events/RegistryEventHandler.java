/**
 * 
 */
package techreborn.events;

import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import reborncore.RebornRegistry;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.util.BucketHandler;
import techreborn.TechReborn;
import techreborn.api.Reference;
import techreborn.blocks.BlockComputerCube;
import techreborn.blocks.BlockNuke;
import techreborn.blocks.BlockRefinedIronFence;
import techreborn.blocks.BlockReinforcedGlass;
import techreborn.blocks.BlockRubberLeaves;
import techreborn.blocks.BlockRubberLog;
import techreborn.blocks.BlockRubberPlank;
import techreborn.blocks.BlockRubberPlankSlab;
import techreborn.blocks.BlockRubberPlankStair;
import techreborn.blocks.BlockRubberSapling;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModRecipes;
import techreborn.init.TRArmorMaterial;
import techreborn.init.TRContent;
import techreborn.init.TRContent.Cables;
import techreborn.init.TRContent.Dusts;
import techreborn.init.TRContent.Gems;
import techreborn.init.TRContent.Ingots;
import techreborn.init.TRContent.Machine;
import techreborn.init.TRContent.MachineBlocks;
import techreborn.init.TRContent.Nuggets;
import techreborn.init.TRContent.Ores;
import techreborn.init.TRContent.Parts;
import techreborn.init.TRContent.Plates;
import techreborn.init.TRContent.SmallDusts;
import techreborn.init.TRContent.SolarPanels;
import techreborn.init.TRContent.StorageBlocks;
import techreborn.init.TRContent.Upgrades;
import techreborn.init.TRToolTeir;
import techreborn.itemblocks.ItemBlockRubberSapling;
import techreborn.items.DynamicCell;
import techreborn.items.ItemFrequencyTransmitter;
import techreborn.items.ItemManual;
import techreborn.items.ItemScrapBox;
import techreborn.items.armor.ItemCloakingDevice;
import techreborn.items.armor.ItemLapotronicOrbpack;
import techreborn.items.armor.ItemLithiumIonBatpack;
import techreborn.items.armor.ItemTRArmour;
import techreborn.items.battery.ItemEnergyCrystal;
import techreborn.items.battery.ItemLapotronCrystal;
import techreborn.items.battery.ItemLapotronicOrb;
import techreborn.items.battery.ItemLithiumIonBattery;
import techreborn.items.battery.ItemRedCellBattery;
import techreborn.items.tool.ItemDebugTool;
import techreborn.items.tool.ItemTreeTap;
import techreborn.items.tool.ItemWrench;
import techreborn.items.tool.advanced.ItemAdvancedChainsaw;
import techreborn.items.tool.advanced.ItemAdvancedDrill;
import techreborn.items.tool.advanced.ItemAdvancedJackhammer;
import techreborn.items.tool.advanced.ItemRockCutter;
import techreborn.items.tool.basic.ItemBasicChainsaw;
import techreborn.items.tool.basic.ItemBasicDrill;
import techreborn.items.tool.basic.ItemBasicJackhammer;
import techreborn.items.tool.basic.ItemElectricTreetap;
import techreborn.items.tool.industrial.ItemIndustrialChainsaw;
import techreborn.items.tool.industrial.ItemIndustrialDrill;
import techreborn.items.tool.industrial.ItemIndustrialJackhammer;
import techreborn.items.tool.industrial.ItemNanosaber;
import techreborn.items.tool.industrial.ItemOmniTool;
import techreborn.items.tool.vanilla.ItemTRAxe;
import techreborn.items.tool.vanilla.ItemTRHoe;
import techreborn.items.tool.vanilla.ItemTRSpade;
import techreborn.items.tool.vanilla.ItemTRSword;
import techreborn.utils.InitUtils;

/**
 * @author drcrazy
 *
 */

@Mod.EventBusSubscriber(modid = TechReborn.MOD_ID)
public class RegistryEventHandler {

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		Arrays.stream(Ores.values()).forEach(value -> RebornRegistry.registerBlock(value.block, new Item.Properties()));
		Arrays.stream(StorageBlocks.values()).forEach(value -> RebornRegistry.registerBlock(value.block, new Item.Properties()));
		Arrays.stream(MachineBlocks.values()).forEach(value -> {
			RebornRegistry.registerBlock(value.frame, new Item.Properties());
			RebornRegistry.registerBlock(value.casing, new Item.Properties());
		});
		Arrays.stream(SolarPanels.values()).forEach(value -> RebornRegistry.registerBlock(value.block, new Item.Properties()));
		Arrays.stream(Cables.values()).forEach(value -> RebornRegistry.registerBlock(value.block, new Item.Properties()));
		Arrays.stream(Machine.values()).forEach(value -> RebornRegistry.registerBlock(value.block, new Item.Properties()));

		// Misc. blocks
		RebornRegistry.registerBlock(TRContent.COMPUTER_CUBE = InitUtils.setup(new BlockComputerCube(), "computer_cube"), new Item.Properties());
		RebornRegistry.registerBlock(TRContent.NUKE = InitUtils.setup(new BlockNuke(), "nuke"), new Item.Properties());
		RebornRegistry.registerBlock(TRContent.REFINED_IRON_FENCE = InitUtils.setup(new BlockRefinedIronFence(), "refined_iron_fence"), new Item.Properties());
		RebornRegistry.registerBlock(TRContent.REINFORCED_GLASS = InitUtils.setup(new BlockReinforcedGlass(), "reinforced_glass"), new Item.Properties());
		RebornRegistry.registerBlock(TRContent.RUBBER_LEAVES = InitUtils.setup(new BlockRubberLeaves(), "rubber_leaves"), new Item.Properties());
		RebornRegistry.registerBlock(TRContent.RUBBER_LOG = InitUtils.setup(new BlockRubberLog(), "rubber_log"), new Item.Properties());
		RebornRegistry.registerBlock(TRContent.RUBBER_PLANKS = InitUtils.setup(new BlockRubberPlank(), "rubber_planks"), new Item.Properties());
		RebornRegistry.registerBlock(TRContent.RUBBER_SAPLING = InitUtils.setup(new BlockRubberSapling(), "rubber_sapling"),
				ItemBlockRubberSapling.class, 
				"rubber_sapling");
		RebornRegistry.registerBlockNoItem(TRContent.RUBBER_LOG_SLAB_HALF = InitUtils.setup(new BlockRubberPlankSlab.BlockHalf("rubber_plank"), "rubber_plank_slab"));
		RebornRegistry.registerBlock(TRContent.RUBBER_LOG_SLAB_DOUBLE = InitUtils.setup(new BlockRubberPlankSlab.BlockDouble("rubber_plank", TRContent.RUBBER_LOG_SLAB_HALF), "rubber_plank_double_slab"),
					new ItemSlab(TRContent.RUBBER_LOG_SLAB_HALF, (BlockSlab) TRContent.RUBBER_LOG_SLAB_HALF, (BlockSlab) TRContent.RUBBER_LOG_SLAB_DOUBLE),
					"rubber_plank_double_slab");
		RebornRegistry.registerBlock(TRContent.RUBBER_LOG_STAIR = InitUtils.setup(new BlockRubberPlankStair(TRContent.RUBBER_LOG.getDefaultState(), "rubber_plank"),
					"rubber_plank_stair"), new Item.Properties());

		TechReborn.LOGGER.debug("TechReborns Blocks Loaded");
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		Arrays.stream(Ingots.values()).forEach(value -> RebornRegistry.registerItem(value.item));
		Arrays.stream(Nuggets.values()).forEach(value -> RebornRegistry.registerItem(value.item));
		Arrays.stream(Gems.values()).forEach(value -> RebornRegistry.registerItem(value.item));
		Arrays.stream(Dusts.values()).forEach(value -> RebornRegistry.registerItem(value.item));
		Arrays.stream(SmallDusts.values()).forEach(value -> RebornRegistry.registerItem(value.item));
		Arrays.stream(Plates.values()).forEach(value -> RebornRegistry.registerItem(value.item));
		Arrays.stream(Parts.values()).forEach(value -> RebornRegistry.registerItem(value.item));
		Arrays.stream(Upgrades.values()).forEach(value -> RebornRegistry.registerItem(value.item));
		
		// Gem armor & tools
		if (ConfigTechReborn.enableGemArmorAndTools) {
			//Todo: repair with tags
			RebornRegistry.registerItem(TRContent.BRONZE_SWORD = InitUtils.setup(new ItemTRSword(TRToolTeir.BRONZE, "ingotBronze"), "bronze_sword"));
			RebornRegistry.registerItem(TRContent.BRONZE_PICKAXE = InitUtils.setup(new ItemTRSword(TRToolTeir.BRONZE, "ingotBronze"), "bronze_pickaxe"));
			RebornRegistry.registerItem(TRContent.BRONZE_SPADE = InitUtils.setup(new ItemTRSpade(TRToolTeir.BRONZE, "ingotBronze"), "bronze_spade"));
			RebornRegistry.registerItem(TRContent.BRONZE_AXE = InitUtils.setup(new ItemTRAxe(TRToolTeir.BRONZE, "ingotBronze"), "bronze_axe"));
			RebornRegistry.registerItem(TRContent.BRONZE_HOE = InitUtils.setup(new ItemTRHoe(TRToolTeir.BRONZE, "ingotBronze"), "bronze_hoe"));

			RebornRegistry.registerItem(TRContent.BRONZE_HELMET = InitUtils.setup(new ItemTRArmour(TRArmorMaterial.BRONZE, EntityEquipmentSlot.HEAD, "ingotBronze"), "bronze_helmet"));
			RebornRegistry.registerItem(TRContent.BRONZE_CHESTPLATE = InitUtils.setup(new ItemTRArmour(TRArmorMaterial.BRONZE, EntityEquipmentSlot.CHEST, "ingotBronze"), "bronze_chestplate"));
			RebornRegistry.registerItem(TRContent.BRONZE_LEGGINGS = InitUtils.setup(new ItemTRArmour(TRArmorMaterial.BRONZE, EntityEquipmentSlot.LEGS, "ingotBronze"), "bronze_leggings"));
			RebornRegistry.registerItem(TRContent.BRONZE_BOOTS = InitUtils.setup(new ItemTRArmour(TRArmorMaterial.BRONZE, EntityEquipmentSlot.FEET, "ingotBronze"), "bronze_boots"));

			RebornRegistry.registerItem(TRContent.RUBY_SWORD = InitUtils.setup(new ItemTRSword(TRToolTeir.RUBY, "gemRuby"), "ruby_sword"));
			RebornRegistry.registerItem(TRContent.RUBY_PICKAXE = InitUtils.setup(new ItemTRSword(TRToolTeir.RUBY, "gemRuby"), "ruby_pickaxe"));
			RebornRegistry.registerItem(TRContent.RUBY_SPADE = InitUtils.setup(new ItemTRSpade(TRToolTeir.RUBY, "gemRuby"), "ruby_spade"));
			RebornRegistry.registerItem(TRContent.RUBY_AXE = InitUtils.setup(new ItemTRAxe(TRToolTeir.RUBY, "gemRuby"), "ruby_axe"));
			RebornRegistry.registerItem(TRContent.RUBY_HOE = InitUtils.setup(new ItemTRHoe(TRToolTeir.RUBY, "gemRuby"), "ruby_hoe"));

			RebornRegistry.registerItem(TRContent.RUBY_HELMET = InitUtils.setup(new ItemTRArmour(TRArmorMaterial.RUBY, EntityEquipmentSlot.HEAD, "gemRuby"), "ruby_helmet"));
			RebornRegistry.registerItem(TRContent.RUBY_CHESTPLATE = InitUtils.setup(new ItemTRArmour(TRArmorMaterial.RUBY, EntityEquipmentSlot.CHEST, "gemRuby"), "ruby_chestplate"));
			RebornRegistry.registerItem(TRContent.RUBY_LEGGINGS = InitUtils.setup(new ItemTRArmour(TRArmorMaterial.RUBY, EntityEquipmentSlot.LEGS, "gemRuby"), "ruby_leggings"));
			RebornRegistry.registerItem(TRContent.RUBY_BOOTS = InitUtils.setup(new ItemTRArmour(TRArmorMaterial.RUBY, EntityEquipmentSlot.FEET, "gemRuby"), "ruby_boots"));

			RebornRegistry.registerItem(TRContent.SAPPHIRE_SWORD = InitUtils.setup(new ItemTRSword(TRToolTeir.SAPPHIRE, "gemSapphire"), "sapphire_sword"));
			RebornRegistry.registerItem(TRContent.SAPPHIRE_PICKAXE = InitUtils.setup(new ItemTRSword(TRToolTeir.SAPPHIRE, "gemSapphire"), "sapphire_pickaxe"));
			RebornRegistry.registerItem(TRContent.SAPPHIRE_SPADE = InitUtils.setup(new ItemTRSpade(TRToolTeir.SAPPHIRE, "gemSapphire"), "sapphire_spade"));
			RebornRegistry.registerItem(TRContent.SAPPHIRE_AXE = InitUtils.setup(new ItemTRAxe(TRToolTeir.SAPPHIRE, "gemSapphire"), "sapphire_axe"));
			RebornRegistry.registerItem(TRContent.SAPPHIRE_HOE = InitUtils.setup(new ItemTRHoe(TRToolTeir.SAPPHIRE, "gemSapphire"), "sapphire_hoe"));

			RebornRegistry.registerItem(TRContent.SAPPHIRE_HELMET = InitUtils.setup(new ItemTRArmour(TRArmorMaterial.SAPPHIRE, EntityEquipmentSlot.HEAD, "gemSapphire"), "sapphire_helmet"));
			RebornRegistry.registerItem(TRContent.SAPPHIRE_CHESTPLATE = InitUtils.setup(new ItemTRArmour(TRArmorMaterial.SAPPHIRE, EntityEquipmentSlot.CHEST, "gemSapphire"), "sapphire_chestplate"));
			RebornRegistry.registerItem(TRContent.SAPPHIRE_LEGGINGS = InitUtils.setup(new ItemTRArmour(TRArmorMaterial.SAPPHIRE, EntityEquipmentSlot.LEGS, "gemSapphire"), "sapphire_leggings"));
			RebornRegistry.registerItem(TRContent.SAPPHIRE_BOOTS = InitUtils.setup(new ItemTRArmour(TRArmorMaterial.SAPPHIRE, EntityEquipmentSlot.FEET, "gemSapphire"), "sapphire_boots"));

			RebornRegistry.registerItem(TRContent.PERIDOT_SWORD = InitUtils.setup(new ItemTRSword(TRToolTeir.PERIDOT, "gemPeridot"), "peridot_sword"));
			RebornRegistry.registerItem(TRContent.PERIDOT_PICKAXE = InitUtils.setup(new ItemTRSword(TRToolTeir.PERIDOT, "gemPeridot"), "peridot_pickaxe"));
			RebornRegistry.registerItem(TRContent.PERIDOT_SPADE = InitUtils.setup(new ItemTRSpade(TRToolTeir.PERIDOT, "gemPeridot"), "peridot_spade"));
			RebornRegistry.registerItem(TRContent.PERIDOT_AXE = InitUtils.setup(new ItemTRAxe(TRToolTeir.PERIDOT, "gemPeridot"), "peridot_axe"));
			RebornRegistry.registerItem(TRContent.PERIDOT_HOE = InitUtils.setup(new ItemTRHoe(TRToolTeir.PERIDOT, "gemPeridot"), "peridot_hoe"));

			RebornRegistry.registerItem(TRContent.PERIDOT_HELMET = InitUtils.setup(new ItemTRArmour(TRArmorMaterial.PERIDOT, EntityEquipmentSlot.HEAD, "gemPeridot"), "peridot_helmet"));
			RebornRegistry.registerItem(TRContent.PERIDOT_CHESTPLATE = InitUtils.setup(new ItemTRArmour(TRArmorMaterial.PERIDOT, EntityEquipmentSlot.CHEST, "gemPeridot"), "peridot_chestplate"));
			RebornRegistry.registerItem(TRContent.PERIDOT_LEGGINGS = InitUtils.setup(new ItemTRArmour(TRArmorMaterial.PERIDOT, EntityEquipmentSlot.LEGS, "gemPeridot"), "peridot_leggings"));
			RebornRegistry.registerItem(TRContent.PERIDOT_BOOTS = InitUtils.setup(new ItemTRArmour(TRArmorMaterial.PERIDOT, EntityEquipmentSlot.FEET, "gemPeridot"), "peridot_boots"));
		}

		// Battery
		RebornRegistry.registerItem(TRContent.RED_CELL_BATTERY = InitUtils.setup(new ItemRedCellBattery(), "red_cell_battery"));
		RebornRegistry.registerItem(TRContent.LITHIUM_ION_BATTERY = InitUtils.setup(new ItemLithiumIonBattery(), "lithium_ion_battery"));
		RebornRegistry.registerItem(TRContent.LITHIUM_ION_BATPACK = InitUtils.setup(new ItemLithiumIonBatpack(), "lithium_ion_batpack"));
		RebornRegistry.registerItem(TRContent.ENERGY_CRYSTAL = InitUtils.setup(new ItemEnergyCrystal(), "energy_crystal"));
		RebornRegistry.registerItem(TRContent.LAPOTRON_CRYSTAL = InitUtils.setup(new ItemLapotronCrystal(), "lapotron_crystal"));
		RebornRegistry.registerItem(TRContent.LAPOTRONIC_ORB = InitUtils.setup(new ItemLapotronicOrb(), "lapotronic_orb"));
		RebornRegistry.registerItem(TRContent.LAPOTRONIC_ORBPACK = InitUtils.setup(new ItemLapotronicOrbpack(), "lapotronic_orbpack"));

		// Tools
		RebornRegistry.registerItem(TRContent.TREE_TAP = InitUtils.setup(new ItemTreeTap(), "treetap"));
		RebornRegistry.registerItem(TRContent.WRENCH = InitUtils.setup(new ItemWrench(), "wrench"));

		RebornRegistry.registerItem(TRContent.BASIC_DRILL = InitUtils.setup(new ItemBasicDrill(), "basic_drill"));
		RebornRegistry.registerItem(TRContent.BASIC_CHAINSAW = InitUtils.setup(new ItemBasicChainsaw(), "basic_chainsaw"));
		RebornRegistry.registerItem(TRContent.BASIC_JACKHAMMER = InitUtils.setup(new ItemBasicJackhammer(), "basic_jackhammer"));
		RebornRegistry.registerItem(TRContent.ELECTRIC_TREE_TAP = InitUtils.setup(new ItemElectricTreetap(), "electric_treetap"));

		RebornRegistry.registerItem(TRContent.ADVANCED_DRILL = InitUtils.setup(new ItemAdvancedDrill(), "advanced_drill"));
		RebornRegistry.registerItem(TRContent.ADVANCED_CHAINSAW = InitUtils.setup(new ItemAdvancedChainsaw(), "advanced_chainsaw"));
		RebornRegistry.registerItem(TRContent.ADVANCED_JACKHAMMER = InitUtils.setup(new ItemAdvancedJackhammer(), "advanced_jackhammer"));
		RebornRegistry.registerItem(TRContent.ROCK_CUTTER = InitUtils.setup(new ItemRockCutter(), "rock_cutter"));

		RebornRegistry.registerItem(TRContent.INDUSTRIAL_DRILL = InitUtils.setup(new ItemIndustrialDrill(), "industrial_drill"));
		RebornRegistry.registerItem(TRContent.INDUSTRIAL_CHAINSAW = InitUtils.setup(new ItemIndustrialChainsaw(), "industrial_chainsaw"));
		RebornRegistry.registerItem(TRContent.INDUSTRIAL_JACKHAMMER = InitUtils.setup(new ItemIndustrialJackhammer(), "industrial_jackhammer"));
		RebornRegistry.registerItem(TRContent.NANOSABER = InitUtils.setup(new ItemNanosaber(), "nanosaber"));
		RebornRegistry.registerItem(TRContent.OMNI_TOOL = InitUtils.setup(new ItemOmniTool(), "omni_tool"));

		// Armor
		RebornRegistry.registerItem(TRContent.CLOAKING_DEVICE = InitUtils.setup(new ItemCloakingDevice(), "cloaking_device"));

		// Other
		RebornRegistry.registerItem(TRContent.FREQUENCY_TRANSMITTER = InitUtils.setup(new ItemFrequencyTransmitter(), "frequency_transmitter"));
		RebornRegistry.registerItem(TRContent.SCRAP_BOX = InitUtils.setup(new ItemScrapBox(), "scrap_box"));
		RebornRegistry.registerItem(TRContent.MANUAL = InitUtils.setup(new ItemManual(), "manual"));
		RebornRegistry.registerItem(TRContent.DEBUG_TOOL = InitUtils.setup(new ItemDebugTool(), "debug_tool"));
		RebornRegistry.registerItem(TRContent.CELL = InitUtils.setup(new DynamicCell(), "cell"));
		MinecraftForge.EVENT_BUS.register(BucketHandler.INSTANCE);

		// TODO: do we need this at all?
		BlockMachineBase.advancedFrameStack = new ItemStack(TRContent.MachineBlocks.ADVANCED.getFrame());
		BlockMachineBase.basicFrameStack = new ItemStack(TRContent.MachineBlocks.BASIC.getFrame());
		
		TechReborn.LOGGER.debug("TechReborns Items Loaded");
	}
	
	@SubscribeEvent(priority = EventPriority.LOW)//LOW is used as we want it to load as late as possible, but before crafttweaker
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
		//Register ModRecipes
		ModRecipes.init();
	}

}

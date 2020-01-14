/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package techreborn.init;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import reborncore.RebornRegistry;
import reborncore.common.util.OreUtil;
import reborncore.common.util.StringUtils;
import techreborn.Core;
import techreborn.blocks.*;
import techreborn.blocks.cable.BlockCable;
import techreborn.blocks.generator.*;
import techreborn.blocks.generator.solarpanel.BlockSolarPanel;
import techreborn.blocks.lighting.BlockLamp;
import techreborn.blocks.processing.*;
import techreborn.blocks.storage.*;
import techreborn.blocks.tier0.BlockIronAlloyFurnace;
import techreborn.blocks.tier0.BlockIronFurnace;
import techreborn.blocks.tier1.*;
import techreborn.blocks.tier2.*;
import techreborn.blocks.tier3.*;
import techreborn.blocks.transformers.BlockHVTransformer;
import techreborn.blocks.transformers.BlockLVTransformer;
import techreborn.blocks.transformers.BlockMVTransformer;
import techreborn.itemblocks.*;
import techreborn.lib.ModInfo;

/**
 * Registers all TR blocks
 */
public class ModBlocks {

	public static Block THERMAL_GENERATOR;
	public static Block QUANTUM_TANK;
	public static Block CREATIVE_QUANTUM_TANK;
	public static Block QUANTUM_CHEST;
	public static Block CREATIVE_QUANTUM_CHEST;
	public static Block DIGITAL_CHEST;
	public static Block INDUSTRIAL_CENTRIFUGE;
	public static Block ROLLING_MACHINE;
	public static Block MACHINE_CASINGS;
	public static Block INDUSTRIAL_BLAST_FURNACE;
	public static Block ALLOY_SMELTER;
	public static Block INDUSTRIAL_GRINDER;
	public static Block IMPLOSION_COMPRESSOR;
	public static Block MATTER_FABRICATOR;
	public static Block CHUNK_LOADER;
	public static Block DRAGON_EGG_SYPHON;
	public static Block MAGIC_ENERGY_CONVERTER;
	public static Block ASSEMBLING_MACHINE;
	public static Block DIESEL_GENERATOR;
	public static Block INDUSTRIAL_ELECTROLYZER;
	public static Block MAGICAL_ABSORBER;
	public static Block SEMI_FLUID_GENERATOR;
	public static Block GAS_TURBINE;
	public static Block IRON_ALLOY_FURNACE;
	public static Block CHEMICAL_REACTOR;
	public static Block INTERDIMENSIONAL_SU;
	public static Block ADJUSTABLE_SU;
	public static Block LAPOTRONIC_SU;
	public static Block LSU_STORAGE;
	public static Block DISTILLATION_TOWER;
	public static Block VACUUM_FREEZER;
	public static Block FUSION_CONTROL_COMPUTER;
	public static Block FUSION_COIL;
	public static Block LIGHTNING_ROD;
	public static Block INDUSTRIAL_SAWMILL;
	public static Block CHARGE_O_MAT;
	public static Block PLAYER_DETECTOR;
	public static Block GRINDER;
	public static Block SOLID_FUEL_GENEREATOR;
	public static Block COMPRESSOR;
	public static Block EXTRACTOR;
	public static Block ELECTRIC_FURNACE;
	public static Block SOLAR_PANEL;
	public static Block CREATIVE_SOLAR_PANEL;
	public static Block WATER_MILL;
	public static Block WIND_MILL;
	public static Block RECYCLER;
	public static Block LOW_VOLTAGE_SU;
	public static Block MEDIUM_VOLTAGE_SU;
	public static Block HIGH_VOLTAGE_SU;
	public static Block SCRAPBOXINATOR;
	public static Block LV_TRANSFORMER;
	public static Block MV_TRANSFORMER;
	public static Block HV_TRANSFORMER;
	public static Block EV_TRANSFORMER;
	public static Block AUTO_CRAFTING_TABLE;
	public static Block PUMP;
	public static Block PLATE_BENDING_MACHINE;
	public static Block SOLID_CANNING_MACHINE;
	public static Block WIRE_MILL;

	public static BlockOre ORE;
	public static BlockOre2 ORE2;
	public static Block STORAGE;
	public static Block STORAGE2;
	public static Block MACHINE_FRAMES;
	public static Block REINFORCED_GLASS;
	public static Block IRON_FURNACE;
	public static Block NUKE;

	public static Block RUBBER_LOG;
	public static Block RUBBER_LOG_SLAB_HALF;
	public static Block RUBBER_LOG_SLAB_DOUBLE;
	public static Block RUBBER_LOG_STAIR;
	public static Block RUBBER_LEAVES;
	public static Block RUBBER_SAPLING;
	public static Block RUBBER_PLANKS;

	public static Block REFINED_IRON_FENCE;
	public static Block FLARE;
	public static Block CABLE;

	public static Block COMPUTER_CUBE;
	public static Block PLASMA_GENERATOR;

	public static Block LAMP_INCANDESCENT;
	public static Block LAMP_LED;
	public static Block ALARM;
	public static Block FLUID_REPLICATOR;

	/**
	 * Register blocks
	 */
	public static void init() {
		THERMAL_GENERATOR = new BlockThermalGenerator();
		registerBlock(THERMAL_GENERATOR, "thermal_generator");

		QUANTUM_TANK = new BlockQuantumTank();
		registerBlock(QUANTUM_TANK, ItemBlockQuantumTank.class, "quantum_tank");

		CREATIVE_QUANTUM_TANK = new BlockCreativeQuantumTank();
		registerBlock(CREATIVE_QUANTUM_TANK, ItemBlockQuantumTank.class, "creative_quantum_tank");

		QUANTUM_CHEST = new BlockQuantumChest();
		registerBlock(QUANTUM_CHEST, ItemBlockQuantumChest.class, "quantum_chest");

		CREATIVE_QUANTUM_CHEST = new BlockCreativeQuantumChest();
		registerBlock(CREATIVE_QUANTUM_CHEST, ItemBlockQuantumChest.class, "creative_quantum_chest");

		DIGITAL_CHEST = new BlockDigitalChest();
		registerBlock(DIGITAL_CHEST, ItemBlockDigitalChest.class, "digital_chest");

		INDUSTRIAL_CENTRIFUGE = new BlockIndustrialCentrifuge();
		registerBlock(INDUSTRIAL_CENTRIFUGE, "industrial_centrifuge");

		ROLLING_MACHINE = new BlockRollingMachine();
		registerBlock(ROLLING_MACHINE, "rolling_machine");

		INDUSTRIAL_BLAST_FURNACE = new BlockIndustrialBlastFurnace();
		registerBlock(INDUSTRIAL_BLAST_FURNACE, ItemBlockIndustrialBlastFurnace.class, "industrial_blast_furnace");

		ALLOY_SMELTER = new BlockAlloySmelter();
		registerBlock(ALLOY_SMELTER, "alloy_smelter");

		INDUSTRIAL_GRINDER = new BlockIndustrialGrinder();
		registerBlock(INDUSTRIAL_GRINDER, "industrial_grinder");

		IMPLOSION_COMPRESSOR = new BlockImplosionCompressor();
		registerBlock(IMPLOSION_COMPRESSOR, "implosion_compressor");

		MATTER_FABRICATOR = new BlockMatterFabricator();
		registerBlock(MATTER_FABRICATOR, "matter_fabricator");

		CHUNK_LOADER = new BlockChunkLoader();
		registerBlock(CHUNK_LOADER, "chunk_loader");

		CHARGE_O_MAT = new BlockChargeOMat();
		registerBlock(CHARGE_O_MAT, "charge_o_mat");

		PLAYER_DETECTOR = new BlockPlayerDetector();
		registerBlock(PLAYER_DETECTOR, ItemBlockPlayerDetector.class, "player_detector");

		CABLE = new BlockCable();
		registerBlock(CABLE, ItemBlockCable.class, "cable");

		MACHINE_CASINGS = new BlockMachineCasing();
		registerBlock(MACHINE_CASINGS, ItemBlockMachineCasing.class, "machine_casing");

		ORE = new BlockOre();
		registerBlock(ORE, ItemBlockOre.class, "ore");

		ORE2 = new BlockOre2();
		registerBlock(ORE2, ItemBlockOre2.class, "ore2");

		STORAGE = new BlockStorage();
		registerBlock(STORAGE, ItemBlockStorage.class, "storage");

		STORAGE2 = new BlockStorage2();
		registerBlock(STORAGE2, ItemBlockStorage2.class, "storage2");

		DRAGON_EGG_SYPHON = new BlockDragonEggSyphon();
		registerBlock(DRAGON_EGG_SYPHON, "dragon_egg_syphon");

		MAGIC_ENERGY_CONVERTER = new BlockMagicEnergyConverter();
		registerBlock(MAGIC_ENERGY_CONVERTER, "magic_energy_converter");

		ASSEMBLING_MACHINE = new BlockAssemblingMachine();
		registerBlock(ASSEMBLING_MACHINE, "assembling_machine");

		DIESEL_GENERATOR = new BlockDieselGenerator();
		registerBlock(DIESEL_GENERATOR, "diesel_generator");

		INDUSTRIAL_ELECTROLYZER = new BlockIndustrialElectrolyzer();
		registerBlock(INDUSTRIAL_ELECTROLYZER, "industrial_electrolyzer");

		MAGICAL_ABSORBER = new BlockMagicEnergyAbsorber();
		registerBlock(MAGICAL_ABSORBER, "magic_energy_absorber");

		SEMI_FLUID_GENERATOR = new BlockSemiFluidGenerator();
		registerBlock(SEMI_FLUID_GENERATOR, "semi_fluid_generator");

		GAS_TURBINE = new BlockGasTurbine();
		registerBlock(GAS_TURBINE, "gas_turbine");

		IRON_ALLOY_FURNACE = new BlockIronAlloyFurnace();
		registerBlock(IRON_ALLOY_FURNACE, "iron_alloy_furnace");

		CHEMICAL_REACTOR = new BlockChemicalReactor();
		registerBlock(CHEMICAL_REACTOR, "chemical_reactor");

		INTERDIMENSIONAL_SU = new BlockInterdimensionalSU();
		registerBlock(INTERDIMENSIONAL_SU, "interdimensional_su");

		ADJUSTABLE_SU = new BlockAdjustableSU();
		registerBlock(ADJUSTABLE_SU, ItemBlockAdjustableSU.class, "adjustable_su");

		LAPOTRONIC_SU = new BlockLapotronicSU();
		registerBlock(LAPOTRONIC_SU, "lapotronic_su");

		LSU_STORAGE = new BlockLSUStorage();
		registerBlock(LSU_STORAGE, "lsu_storage");

		DISTILLATION_TOWER = new BlockDistillationTower();
		registerBlock(DISTILLATION_TOWER, "distillation_tower");

		VACUUM_FREEZER = new BlockVacuumFreezer();
		registerBlock(VACUUM_FREEZER, "vacuum_freezer");

		FUSION_CONTROL_COMPUTER = new BlockFusionControlComputer();
		registerBlock(FUSION_CONTROL_COMPUTER, "fusion_control_computer");

		FUSION_COIL = new BlockFusionCoil();
		registerBlock(FUSION_COIL, "fusion_coil");

		LIGHTNING_ROD = new BlockLightningRod();
		registerBlock(LIGHTNING_ROD, "lightning_rod");

		INDUSTRIAL_SAWMILL = new BlockIndustrialSawmill();
		registerBlock(INDUSTRIAL_SAWMILL, "industrial_sawmill");

		MACHINE_FRAMES = new BlockMachineFrames();
		registerBlock(MACHINE_FRAMES, ItemBlockMachineFrames.class, "machine_frame");

		GRINDER = new BlockGrinder();
		registerBlock(GRINDER, "grinder");

		SOLID_FUEL_GENEREATOR = new BlockSolidFuelGenerator();
		registerBlock(SOLID_FUEL_GENEREATOR, "solid_fuel_generator");

		EXTRACTOR = new BlockExtractor();
		registerBlock(EXTRACTOR, "extractor");

		COMPRESSOR = new BlockCompressor();
		registerBlock(COMPRESSOR, "compressor");

		PLATE_BENDING_MACHINE = new BlockPlateBendingMachine();
		registerBlock(PLATE_BENDING_MACHINE, "plate_bending_machine");

		ELECTRIC_FURNACE = new BlockElectricFurnace();
		registerBlock(ELECTRIC_FURNACE, "electric_furnace");

		SOLAR_PANEL = new BlockSolarPanel();
		registerBlock(SOLAR_PANEL, ItemBlockSolarPanel.class,  "solar_panel");

		CREATIVE_SOLAR_PANEL = new BlockCreativeSolarPanel();
		registerBlock(CREATIVE_SOLAR_PANEL, "creative_solar_panel");

		SOLID_CANNING_MACHINE = new BlockSolidCanningMachine();
		registerBlock(SOLID_CANNING_MACHINE, "solid_canning_machine");

		WATER_MILL = new BlockWaterMill();
		registerBlock(WATER_MILL, "water_mill");

		WIND_MILL = new BlockWindMill();
		registerBlock(WIND_MILL, "wind_mill");

		WIRE_MILL = new BlockWireMill();
		registerBlock(WIRE_MILL, "wire_mill");

		RUBBER_LOG = new BlockRubberLog();
		registerBlock(RUBBER_LOG, "rubber_log");

		RUBBER_PLANKS = new BlockRubberPlank();
		registerBlock(RUBBER_PLANKS, "rubber_planks");

		RUBBER_LOG_SLAB_HALF = new BlockRubberPlankSlab.BlockHalf("rubber_plank");
		registerBlockNoItem(RUBBER_LOG_SLAB_HALF, "rubber_plank_slab");

		RUBBER_LOG_SLAB_DOUBLE = new BlockRubberPlankSlab.BlockDouble("rubber_plank", RUBBER_LOG_SLAB_HALF);
		registerBlock(RUBBER_LOG_SLAB_DOUBLE, new ItemSlab(RUBBER_LOG_SLAB_HALF, (BlockSlab) RUBBER_LOG_SLAB_HALF, (BlockSlab) RUBBER_LOG_SLAB_DOUBLE) , "rubber_plank_double_slab");

		RUBBER_LOG_STAIR = new BlockRubberPlankStair(RUBBER_LOG.getDefaultState(), "rubber_plank");
		registerBlock(RUBBER_LOG_STAIR, "rubber_plank_stair");

		RUBBER_LEAVES = new BlockRubberLeaves();
		registerBlock(RUBBER_LEAVES, "rubber_leaves");

		RUBBER_SAPLING = new BlockRubberSapling();
		registerBlock(RUBBER_SAPLING, ItemBlockRubberSapling.class, "rubber_sapling");

		REFINED_IRON_FENCE = new BlockRefinedIronFence();
		registerBlock(REFINED_IRON_FENCE, "refined_iron_fence");

		REINFORCED_GLASS = new BlockReinforcedGlass();
		registerBlock(REINFORCED_GLASS, "reinforced_glass");

		RECYCLER = new BlockRecycler();
		registerBlock(RECYCLER, "recycler");

		LOW_VOLTAGE_SU = new BlockLowVoltageSU();
		registerBlock(LOW_VOLTAGE_SU, "low_voltage_su");

		MEDIUM_VOLTAGE_SU = new BlockMediumVoltageSU();
		registerBlock(MEDIUM_VOLTAGE_SU, "medium_voltage_su");

		HIGH_VOLTAGE_SU = new BlockHighVoltageSU();
		registerBlock(HIGH_VOLTAGE_SU, "high_voltage_su");

		LV_TRANSFORMER = new BlockLVTransformer();
		registerBlock(LV_TRANSFORMER, "lv_transformer");

		MV_TRANSFORMER = new BlockMVTransformer();
		registerBlock(MV_TRANSFORMER, "mv_transformer");

		HV_TRANSFORMER = new BlockHVTransformer();
		registerBlock(HV_TRANSFORMER, "hv_transformer");

		EV_TRANSFORMER = new BlockHVTransformer();
		registerBlock(EV_TRANSFORMER, "ev_transformer");

		AUTO_CRAFTING_TABLE = new BlockAutoCraftingTable();
		registerBlock(AUTO_CRAFTING_TABLE, "auto_crafting_table");

		PUMP = new BlockPump();
		registerBlock(PUMP, "pump");

		IRON_FURNACE = new BlockIronFurnace();
		registerBlock(IRON_FURNACE, "iron_furnace");

		NUKE = new BlockNuke();
		registerBlock(NUKE, "nuke");

		SCRAPBOXINATOR = new BlockScrapboxinator();
		registerBlock(SCRAPBOXINATOR, "scrapboxinator");

		COMPUTER_CUBE = new BlockComputerCube();
		registerBlock(COMPUTER_CUBE, "computer_cube");
		
		PLASMA_GENERATOR = new BlockPlasmaGenerator();
		registerBlock(PLASMA_GENERATOR, "plasma_generator");

		LAMP_INCANDESCENT = new BlockLamp( 14, 4, 0.625, 0.25);
		registerBlock(LAMP_INCANDESCENT, "lamp_incandescent");

		LAMP_LED = new BlockLamp( 15, 1, 0.0625, 0.125);
		registerBlock(LAMP_LED, "lamp_led");

		ALARM = new BlockAlarm();
		registerBlock(ALARM, "alarm");
		
		FLUID_REPLICATOR = new BlockFluidReplicator();
		registerBlock(FLUID_REPLICATOR, "fluid_replicator");

		//TODO enable when done
		//		flare = new BlockFlare();
		//		registerBlock(flare, "flare");
		//		ItemBlock itemBlock = new ItemColored(flare, true);
		//		itemBlock.setRegistryName("flareItemBlock");
		//		itemBlock.setCreativeTab(TechRebornCreativeTabMisc.instance);
		//		GameRegistry.register(itemBlock);
		//		GameRegistry.registerTileEntity(TileEntityFlare.class, "TileEntityFlareTR");

		registerOreDict();
		Core.logHelper.info("TechReborns Blocks Loaded");
	}

	/**
	 * Wrapper method for RebornRegistry
	 * @param block Block to register
	 * @param name Name of block to register
	 */
	public static void registerBlock(Block block, String name) {
		name = name.toLowerCase();
		block.setTranslationKey(ModInfo.MOD_ID + "." + name);
		RebornRegistry.registerBlock(block, new ResourceLocation(ModInfo.MOD_ID, name));
	}

	/**
	 * Wrapper method for RebornRegistry
	 * @param block Block to Register
	 * @param itemclass Itemblock of block to register
	 * @param name Name of block to register
	 */
	public static void registerBlock(Block block, Class<? extends ItemBlock> itemclass, String name) {
		name = name.toLowerCase();
		block.setTranslationKey(ModInfo.MOD_ID + "." + name);
		RebornRegistry.registerBlock(block, itemclass, new ResourceLocation(ModInfo.MOD_ID, name));
	}

	public static void registerBlock(Block block, ItemBlock itemBlock, String name) {
		name = name.toLowerCase();
		block.setTranslationKey(ModInfo.MOD_ID + "." + name);
		RebornRegistry.registerBlock(block, itemBlock, new ResourceLocation(ModInfo.MOD_ID, name));
	}

	public static void registerBlockNoItem(Block block, String name) {
		name = name.toLowerCase();
		block.setTranslationKey(ModInfo.MOD_ID + "." + name);
		RebornRegistry.registerBlockNoItem(block,  new ResourceLocation(ModInfo.MOD_ID, name));
	}

	/**
	 * Register ores and ore blocks
	 */
	public static void registerOreDict() {
		for (String ore : BlockOre.ores) {
			OreUtil.registerOre("ore" + StringUtils.toFirstCapital(ore), BlockOre.getOreByName(ore));
		}

		for (String ore : BlockOre2.ores) {
			OreUtil.registerOre("ore" + StringUtils.toFirstCapital(ore), BlockOre2.getOreByName(ore));
		}

		OreUtil.registerOre("blockSilver", BlockStorage.getStorageBlockByName("silver"));
		OreUtil.registerOre("blockAluminum", BlockStorage.getStorageBlockByName("aluminum"));
		OreUtil.registerOre("blockAluminium", BlockStorage.getStorageBlockByName("aluminum"));
		OreUtil.registerOre("blockTitanium", BlockStorage.getStorageBlockByName("titanium"));
		OreUtil.registerOre("blockChrome", BlockStorage.getStorageBlockByName("chrome"));
		OreUtil.registerOre("blockSteel", BlockStorage.getStorageBlockByName("steel"));
		OreUtil.registerOre("blockBrass", BlockStorage.getStorageBlockByName("brass"));
		OreUtil.registerOre("blockLead", BlockStorage.getStorageBlockByName("lead"));
		OreUtil.registerOre("blockElectrum", BlockStorage.getStorageBlockByName("electrum"));
		OreUtil.registerOre("blockZinc", BlockStorage.getStorageBlockByName("zinc"));
		OreUtil.registerOre("blockPlatinum", BlockStorage.getStorageBlockByName("platinum"));
		OreUtil.registerOre("blockTungsten", BlockStorage.getStorageBlockByName("tungsten"));
		OreUtil.registerOre("blockNickel", BlockStorage.getStorageBlockByName("nickel"));
		OreUtil.registerOre("blockInvar", BlockStorage.getStorageBlockByName("invar"));
		OreUtil.registerOre("blockIridium", BlockStorage.getStorageBlockByName("iridium"));
		OreUtil.registerOre("blockBronze", BlockStorage.getStorageBlockByName("bronze"));
		OreUtil.registerOre("blockCopper", BlockStorage2.getStorageBlockByName("copper", 1));
		OreUtil.registerOre("blockTin", BlockStorage2.getStorageBlockByName("tin", 1));
		OreUtil.registerOre("blockTungstensteel", BlockStorage2.getStorageBlockByName("tungstensteel", 1));
		OreUtil.registerOre("blockRuby", BlockStorage2.getStorageBlockByName("ruby", 1));
		OreUtil.registerOre("blockSapphire", BlockStorage2.getStorageBlockByName("sapphire", 1));
		OreUtil.registerOre("blockPeridot", BlockStorage2.getStorageBlockByName("peridot", 1));
		OreUtil.registerOre("blockYellowGarnet", BlockStorage2.getStorageBlockByName("yellowGarnet", 1));
		OreUtil.registerOre("blockRedGarnet", BlockStorage2.getStorageBlockByName("redGarnet", 1));

		OreUtil.registerOre("craftingPiston", Blocks.PISTON);
		OreUtil.registerOre("craftingPiston", Blocks.STICKY_PISTON);
		OreUtil.registerOre("crafterWood", Blocks.CRAFTING_TABLE);
		OreUtil.registerOre("machineBasic", new ItemStack(MACHINE_FRAMES, 1));

		OreUtil.registerOre("treeSapling", RUBBER_SAPLING);
		OreUtil.registerOre("saplingRubber", RUBBER_SAPLING);
		OreUtil.registerOre("logWood", new ItemStack(RUBBER_LOG, 1, OreDictionary.WILDCARD_VALUE));
		OreUtil.registerOre("logRubber", new ItemStack(RUBBER_LOG, 1, OreDictionary.WILDCARD_VALUE));
		OreUtil.registerOre("plankWood", new ItemStack(RUBBER_PLANKS, 1, OreDictionary.WILDCARD_VALUE));
		OreUtil.registerOre("slabWood", new ItemStack(RUBBER_LOG_SLAB_HALF, 1, OreDictionary.WILDCARD_VALUE));
		OreUtil.registerOre("stairWood", new ItemStack(RUBBER_LOG_STAIR, 1, OreDictionary.WILDCARD_VALUE));
		OreUtil.registerOre("plankRubber", new ItemStack(RUBBER_PLANKS, 1, OreDictionary.WILDCARD_VALUE));
		OreUtil.registerOre("treeLeaves", new ItemStack(RUBBER_LEAVES, 1, OreDictionary.WILDCARD_VALUE));
		OreUtil.registerOre("leavesRubber", new ItemStack(RUBBER_LEAVES, 1, OreDictionary.WILDCARD_VALUE));

		OreUtil.registerOre("fenceIron", REFINED_IRON_FENCE);

		OreUtil.registerOre("machineBlockBasic", BlockMachineFrames.getFrameByName("basic", 1));
		OreUtil.registerOre("machineBlockAdvanced", BlockMachineFrames.getFrameByName("advanced", 1));
		OreUtil.registerOre("machineBlockHighlyAdvanced", BlockMachineFrames.getFrameByName("highly_advanced", 1));

	}

}

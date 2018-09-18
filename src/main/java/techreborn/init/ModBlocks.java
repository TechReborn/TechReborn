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
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSlab;
import net.minecraft.util.ResourceLocation;
import reborncore.RebornRegistry;
import techreborn.TechReborn;
import techreborn.blocks.*;
import techreborn.blocks.cable.BlockCable;
import techreborn.blocks.generator.*;
import techreborn.blocks.generator.solarpanel.BlockSolarPanel;
import techreborn.blocks.lighting.BlockLamp;
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

/**
 * Registers all TR blocks
 */
public class ModBlocks {

	// Misc Blocks
	public static Block CABLE;
	public static Block COMPUTER_CUBE;
	public static Block FLARE;
	public static Block MACHINE_CASINGS_ADVANCED;
	public static Block MACHINE_CASINGS_REINFORCED;
	public static Block MACHINE_CASINGS_STANDARD;
	public static Block MACHINE_BLOCK_ADVANCED;
	public static Block MACHINE_BLOCK_BASIC;
	public static Block MACHINE_BLOCK_ELITE;
	public static Block NUKE;
	public static Block REFINED_IRON_FENCE;
	public static Block REINFORCED_GLASS;
	public static Block RUBBER_LEAVES;
	public static Block RUBBER_LOG;
	public static Block RUBBER_LOG_SLAB_HALF;
	public static Block RUBBER_LOG_SLAB_DOUBLE;
	public static Block RUBBER_LOG_STAIR;
	public static Block RUBBER_PLANKS;
	public static Block RUBBER_SAPLING;

	// Machines - machines
	public static Block ALLOY_SMELTER;
	public static Block ASSEMBLY_MACHINE;
	public static Block AUTO_CRAFTING_TABLE;
	public static Block CHEMICAL_REACTOR;
	public static Block COMPRESSOR;
	public static Block DISTILLATION_TOWER;
	public static Block ELECTRIC_FURNACE;
	public static Block EXTRACTOR;
	public static Block FLUID_REPLICATOR;
	public static Block GRINDER;
	public static Block IMPLOSION_COMPRESSOR;
	public static Block INDUSTRIAL_BLAST_FURNACE;
	public static Block INDUSTRIAL_CENTRIFUGE;
	public static Block INDUSTRIAL_ELECTROLYZER;
	public static Block INDUSTRIAL_GRINDER;
	public static Block INDUSTRIAL_SAWMILL;
	public static Block IRON_ALLOY_FURNACE;
	public static Block IRON_FURNACE;
	public static Block MATTER_FABRICATOR;
	public static Block RECYCLER;
	public static Block ROLLING_MACHINE;
	public static Block SCRAPBOXINATOR;
	public static Block VACUUM_FREEZER;

	// Machines - generators
	public static Block DIESEL_GENERATOR;
	public static Block DRAGON_EGG_SYPHON;
	public static Block FUSION_COIL;
	public static Block FUSION_CONTROL_COMPUTER;
	public static Block GAS_TURBINE;
	public static Block LIGHTNING_ROD;
	public static Block PLASMA_GENERATOR;
	public static Block SEMI_FLUID_GENERATOR;
	public static Block SOLAR_PANEL_BASIC;
	public static Block SOLAR_PANEL_ADVANCED;
	public static Block SOLAR_PANEL_INDUSTRIAL;
	public static Block SOLAR_PANEL_ULTIMATE;
	public static Block SOLAR_PANEL_QUANTUM;
	public static Block SOLAR_PANEL_CREATIVE;
	
	public static Block SOLID_FUEL_GENEREATOR;
	public static Block THERMAL_GENERATOR;
	public static Block WATER_MILL;
	public static Block WIND_MILL;

	// Machines - storage
	public static Block CREATIVE_QUANTUM_CHEST;
	public static Block CREATIVE_QUANTUM_TANK;
	public static Block DIGITAL_CHEST;
	public static Block QUANTUM_CHEST;
	public static Block QUANTUM_TANK;

	// Machines - energy storage & transformers
	public static Block ADJUSTABLE_SU;
	public static Block CHARGE_O_MAT;
	public static Block INTERDIMENSIONAL_SU;
	public static Block LAPOTRONIC_SU;
	public static Block LSU_STORAGE;
	public static Block LOW_VOLTAGE_SU;
	public static Block MEDIUM_VOLTAGE_SU;
	public static Block HIGH_VOLTAGE_SU;
	public static Block LV_TRANSFORMER;
	public static Block MV_TRANSFORMER;
	public static Block HV_TRANSFORMER;

	// Machines - misc
	public static Block ALARM;
	public static Block CHUNK_LOADER;
	public static Block LAMP_INCANDESCENT;
	public static Block LAMP_LED;
	public static Block MAGICAL_ABSORBER;
	public static Block MAGIC_ENERGY_CONVERTER;
	public static Block PLAYER_DETECTOR;

	/**
	 * Register blocks
	 */
	public static void init() {
		TRContent.registerBlocks();

		// Misc. blocks
		COMPUTER_CUBE = new BlockComputerCube();
		registerBlock(COMPUTER_CUBE, "computer_cube");

		CABLE = new BlockCable();
		registerBlock(CABLE, ItemBlockCable.class, "cable");

		NUKE = new BlockNuke();
		registerBlock(NUKE, "nuke");

		REFINED_IRON_FENCE = new BlockRefinedIronFence();
		registerBlock(REFINED_IRON_FENCE, "refined_iron_fence");

		REINFORCED_GLASS = new BlockReinforcedGlass();
		registerBlock(REINFORCED_GLASS, "reinforced_glass");

		RUBBER_LEAVES = new BlockRubberLeaves();
		registerBlock(RUBBER_LEAVES, "rubber_leaves");

		RUBBER_LOG = new BlockRubberLog();
		registerBlock(RUBBER_LOG, "rubber_log");

		RUBBER_LOG_SLAB_HALF = new BlockRubberPlankSlab.BlockHalf("rubber_plank");
		registerBlockNoItem(RUBBER_LOG_SLAB_HALF, "rubber_plank_slab");

		RUBBER_LOG_SLAB_DOUBLE = new BlockRubberPlankSlab.BlockDouble("rubber_plank", RUBBER_LOG_SLAB_HALF);
		registerBlock(RUBBER_LOG_SLAB_DOUBLE, new ItemSlab(RUBBER_LOG_SLAB_HALF, (BlockSlab) RUBBER_LOG_SLAB_HALF, (BlockSlab) RUBBER_LOG_SLAB_DOUBLE) , "rubber_plank_double_slab");

		RUBBER_LOG_STAIR = new BlockRubberPlankStair(RUBBER_LOG.getDefaultState(), "rubber_plank");
		registerBlock(RUBBER_LOG_STAIR, "rubber_plank_stair");

		RUBBER_PLANKS = new BlockRubberPlank();
		registerBlock(RUBBER_PLANKS, "rubber_planks");

		RUBBER_SAPLING = new BlockRubberSapling();
		registerBlock(RUBBER_SAPLING, ItemBlockRubberSapling.class, "rubber_sapling");

		// Machines - machines
		ALLOY_SMELTER = new BlockAlloySmelter();
		registerBlock(ALLOY_SMELTER, "alloy_smelter");

		ASSEMBLY_MACHINE = new BlockAssemblingMachine();
		registerBlock(ASSEMBLY_MACHINE, "assembly_machine");

		AUTO_CRAFTING_TABLE = new BlockAutoCraftingTable();
		registerBlock(AUTO_CRAFTING_TABLE, "auto_crafting_table");

		COMPRESSOR = new BlockCompressor();
		registerBlock(COMPRESSOR, "compressor");

		CHEMICAL_REACTOR = new BlockChemicalReactor();
		registerBlock(CHEMICAL_REACTOR, "chemical_reactor");

		DISTILLATION_TOWER = new BlockDistillationTower();
		registerBlock(DISTILLATION_TOWER, "distillation_tower");

		ELECTRIC_FURNACE = new BlockElectricFurnace();
		registerBlock(ELECTRIC_FURNACE, "electric_furnace");

		EXTRACTOR = new BlockExtractor();
		registerBlock(EXTRACTOR, "extractor");

		FLUID_REPLICATOR = new BlockFluidReplicator();
		registerBlock(FLUID_REPLICATOR, "fluid_replicator");

		GRINDER = new BlockGrinder();
		registerBlock(GRINDER, "grinder");

		IMPLOSION_COMPRESSOR = new BlockImplosionCompressor();
		registerBlock(IMPLOSION_COMPRESSOR, "implosion_compressor");

		INDUSTRIAL_BLAST_FURNACE = new BlockIndustrialBlastFurnace();
		registerBlock(INDUSTRIAL_BLAST_FURNACE, "industrial_blast_furnace");

		INDUSTRIAL_CENTRIFUGE = new BlockIndustrialCentrifuge();
		registerBlock(INDUSTRIAL_CENTRIFUGE, "industrial_centrifuge");

		INDUSTRIAL_ELECTROLYZER = new BlockIndustrialElectrolyzer();
		registerBlock(INDUSTRIAL_ELECTROLYZER, "industrial_electrolyzer");

		INDUSTRIAL_GRINDER = new BlockIndustrialGrinder();
		registerBlock(INDUSTRIAL_GRINDER, "industrial_grinder");

		INDUSTRIAL_SAWMILL = new BlockIndustrialSawmill();
		registerBlock(INDUSTRIAL_SAWMILL, "industrial_sawmill");

		IRON_ALLOY_FURNACE = new BlockIronAlloyFurnace();
		registerBlock(IRON_ALLOY_FURNACE, "iron_alloy_furnace");

		IRON_FURNACE = new BlockIronFurnace();
		registerBlock(IRON_FURNACE, "iron_furnace");

		MATTER_FABRICATOR = new BlockMatterFabricator();
		registerBlock(MATTER_FABRICATOR, "matter_fabricator");

		RECYCLER = new BlockRecycler();
		registerBlock(RECYCLER, "recycler");

		ROLLING_MACHINE = new BlockRollingMachine();
		registerBlock(ROLLING_MACHINE, "rolling_machine");

		SCRAPBOXINATOR = new BlockScrapboxinator();
		registerBlock(SCRAPBOXINATOR, "scrapboxinator");

		VACUUM_FREEZER = new BlockVacuumFreezer();
		registerBlock(VACUUM_FREEZER, "vacuum_freezer");

		// Machines - generators
		DIESEL_GENERATOR = new BlockDieselGenerator();
		registerBlock(DIESEL_GENERATOR, "diesel_generator");

		DRAGON_EGG_SYPHON = new BlockDragonEggSyphon();
		registerBlock(DRAGON_EGG_SYPHON, "dragon_egg_syphon");

		FUSION_COIL = new BlockFusionCoil();
		registerBlock(FUSION_COIL, "fusion_coil");

		FUSION_CONTROL_COMPUTER = new BlockFusionControlComputer();
		registerBlock(FUSION_CONTROL_COMPUTER, "fusion_control_computer");

		GAS_TURBINE = new BlockGasTurbine();
		registerBlock(GAS_TURBINE, "gas_turbine");

		LIGHTNING_ROD = new BlockLightningRod();
		registerBlock(LIGHTNING_ROD, "lightning_rod");

		PLASMA_GENERATOR = new BlockPlasmaGenerator();
		registerBlock(PLASMA_GENERATOR, "plasma_generator");

		SEMI_FLUID_GENERATOR = new BlockSemiFluidGenerator();
		registerBlock(SEMI_FLUID_GENERATOR, "semi_fluid_generator");

		SOLAR_PANEL_BASIC = new BlockSolarPanel();
		registerBlock(SOLAR_PANEL_BASIC, "solar_panel_basic");

		SOLAR_PANEL_ADVANCED = new BlockSolarPanel();
		registerBlock(SOLAR_PANEL_ADVANCED, "solar_panel_advanced");
		
		SOLAR_PANEL_INDUSTRIAL = new BlockSolarPanel();
		registerBlock(SOLAR_PANEL_INDUSTRIAL, "solar_panel_industrial");
		
		SOLAR_PANEL_ULTIMATE = new BlockSolarPanel();
		registerBlock(SOLAR_PANEL_ULTIMATE, "solar_panel_ultimate");
		
		SOLAR_PANEL_QUANTUM = new BlockSolarPanel();
		registerBlock(SOLAR_PANEL_QUANTUM, "solar_panel_quantum");
		
		SOLAR_PANEL_CREATIVE = new BlockSolarPanel();
		registerBlock(SOLAR_PANEL_CREATIVE, "solar_panel_creative");

		SOLID_FUEL_GENEREATOR = new BlockSolidFuelGenerator();
		registerBlock(SOLID_FUEL_GENEREATOR, "solid_fuel_generator");

		THERMAL_GENERATOR = new BlockThermalGenerator();
		registerBlock(THERMAL_GENERATOR, "thermal_generator");

		WATER_MILL = new BlockWaterMill();
		registerBlock(WATER_MILL, "water_mill");

		WIND_MILL = new BlockWindMill();
		registerBlock(WIND_MILL, "wind_mill");

		// Machines - storage
		CREATIVE_QUANTUM_CHEST = new BlockCreativeQuantumChest();
		registerBlock(CREATIVE_QUANTUM_CHEST, ItemBlockQuantumChest.class, "creative_quantum_chest");

		CREATIVE_QUANTUM_TANK = new BlockCreativeQuantumTank();
		registerBlock(CREATIVE_QUANTUM_TANK, ItemBlockQuantumTank.class, "creative_quantum_tank");

		DIGITAL_CHEST = new BlockDigitalChest();
		registerBlock(DIGITAL_CHEST, ItemBlockDigitalChest.class, "digital_chest");

		QUANTUM_CHEST = new BlockQuantumChest();
		registerBlock(QUANTUM_CHEST, ItemBlockQuantumChest.class, "quantum_chest");

		QUANTUM_TANK = new BlockQuantumTank();
		registerBlock(QUANTUM_TANK, ItemBlockQuantumTank.class, "quantum_tank");

		// Machines - energy storage & transformers
		ADJUSTABLE_SU = new BlockAdjustableSU();
		registerBlock(ADJUSTABLE_SU, ItemBlockAdjustableSU.class, "adjustable_su");

		CHARGE_O_MAT = new BlockChargeOMat();
		registerBlock(CHARGE_O_MAT, "charge_o_mat");

		INTERDIMENSIONAL_SU = new BlockInterdimensionalSU();
		registerBlock(INTERDIMENSIONAL_SU, "interdimensional_su");

		LAPOTRONIC_SU = new BlockLapotronicSU();
		registerBlock(LAPOTRONIC_SU, "lapotronic_su");

		LSU_STORAGE = new BlockLSUStorage();
		registerBlock(LSU_STORAGE, "lsu_storage");

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

		// Machines - misc
		ALARM = new BlockAlarm();
		registerBlock(ALARM, "alarm");

		CHUNK_LOADER = new BlockChunkLoader();
		registerBlock(CHUNK_LOADER, "chunk_loader");

		LAMP_INCANDESCENT = new BlockLamp( 14, 4, 0.625, 0.25);
		registerBlock(LAMP_INCANDESCENT, "lamp_incandescent");

		LAMP_LED = new BlockLamp( 15, 1, 0.0625, 0.125);
		registerBlock(LAMP_LED, "lamp_led");

		MAGICAL_ABSORBER = new BlockMagicEnergyAbsorber();
		registerBlock(MAGICAL_ABSORBER, "magic_energy_absorber");

		MAGIC_ENERGY_CONVERTER = new BlockMagicEnergyConverter();
		registerBlock(MAGIC_ENERGY_CONVERTER, "magic_energy_converter");

		PLAYER_DETECTOR = new BlockPlayerDetector();
		registerBlock(PLAYER_DETECTOR, ItemBlockPlayerDetector.class, "player_detector");



		//TODO enable when done
		//		flare = new BlockFlare();
		//		registerBlock(flare, "flare");
		//		ItemBlock itemBlock = new ItemColored(flare, true);
		//		itemBlock.setRegistryName("flareItemBlock");
		//		itemBlock.setCreativeTab(TechRebornCreativeTabMisc.instance);
		//		GameRegistry.register(itemBlock);
		//		GameRegistry.registerTileEntity(TileEntityFlare.class, "TileEntityFlareTR");

		TechReborn.LOGGER.info("TechReborns Blocks Loaded");
	}

	/**
	 * Wrapper method for RebornRegistry
	 * @param block Block to register
	 * @param name Name of block to register
	 */
	public static void registerBlock(Block block, String name) {
		name = name.toLowerCase();
		block.setTranslationKey(TechReborn.MOD_ID + "." + name);
		RebornRegistry.registerBlock(block, new ResourceLocation(TechReborn.MOD_ID, name));
	}

	/**
	 * Wrapper method for RebornRegistry
	 * @param block Block to Register
	 * @param itemclass Itemblock of block to register
	 * @param name Name of block to register
	 */
	public static void registerBlock(Block block, Class<? extends ItemBlock> itemclass, String name) {
		name = name.toLowerCase();
		block.setTranslationKey(TechReborn.MOD_ID + "." + name);
		RebornRegistry.registerBlock(block, itemclass, new ResourceLocation(TechReborn.MOD_ID, name));
	}

	public static void registerBlock(Block block, ItemBlock itemBlock, String name) {
		name = name.toLowerCase();
		block.setTranslationKey(TechReborn.MOD_ID + "." + name);
		RebornRegistry.registerBlock(block, itemBlock, new ResourceLocation(TechReborn.MOD_ID, name));
	}

	public static void registerBlockNoItem(Block block, String name) {
		name = name.toLowerCase();
		block.setTranslationKey(TechReborn.MOD_ID + "." + name);
		RebornRegistry.registerBlockNoItem(block,  new ResourceLocation(TechReborn.MOD_ID, name));
	}
}

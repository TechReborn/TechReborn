/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 TechReborn
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
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import reborncore.common.tile.TileMachineBase;
import reborncore.common.util.StringUtils;
import techreborn.Core;
import techreborn.blocks.*;
import techreborn.blocks.advanced_machine.*;
import techreborn.blocks.cable.BlockCable;
import techreborn.blocks.generator.*;
import techreborn.blocks.iron_machines.BlockAlloyFurnace;
import techreborn.blocks.iron_machines.BlockIronFurnace;
import techreborn.blocks.machine.*;
import techreborn.blocks.storage.*;
import techreborn.blocks.tier1.*;
import techreborn.blocks.transformers.BlockHVTransformer;
import techreborn.blocks.transformers.BlockLVTransformer;
import techreborn.blocks.transformers.BlockMVTransformer;
import techreborn.itemblocks.*;
import techreborn.tiles.*;
import techreborn.tiles.cable.TileCable;
import techreborn.tiles.fusionReactor.TileEntityFusionController;
import techreborn.tiles.generator.*;
import techreborn.tiles.idsu.TileIDSU;
import techreborn.tiles.lesu.TileLesu;
import techreborn.tiles.lesu.TileLesuStorage;
import techreborn.tiles.multiblock.*;
import techreborn.tiles.storage.TileBatBox;
import techreborn.tiles.storage.TileMFE;
import techreborn.tiles.storage.TileMFSU;
import techreborn.tiles.teir1.*;
import techreborn.tiles.transformers.TileHVTransformer;
import techreborn.tiles.transformers.TileLVTransformer;
import techreborn.tiles.transformers.TileMVTransformer;

import java.lang.reflect.InvocationTargetException;

public class ModBlocks {

	public static Block THERMAL_GENERATOR;
	public static Block QUANTUM_TANK;
	public static Block QUANTUM_CHEST;
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
	public static Block DRAGON_EGG_SIPHONER;
	public static Block MAGIC_ENERGY_CONVERTER;
	public static Block ASSEMBLY_MACHINE;
	public static Block DIESEL_GENERATOR;
	public static Block INDUSTRIAL_ELECTROLYZER;
	public static Block MAGICAL_ABSORBER;
	public static Block SEMIFLUID_GENERATOR;
	public static Block GAS_TURBINE;
	public static Block IRON_ALLOY_FURNACE;
	public static Block CHEMICAL_REACTOR;
	public static Block INTERDIMENSIONAL_SU;
	public static Block ADJUSTABLE_SU;
	public static Block LAPOTRONIC_SU;
	public static Block LSU_STORAGE_BLOCK;
	public static Block DISTILLATION_TOWER;
	public static Block VACUUM_FREEZER;
	public static Block FUSION_CONTROL_COMPUTER;
	public static Block FUSION_COIL;
	public static Block LIGHTNING_ROD;
	public static Block HEAT_GENERATOR;
	public static Block INDUSTRIAL_SAWMILL;
	public static Block CHARGE_O_MAT;
	public static Block PLAYER_DETECTOR;
	public static Block GRINDER;
	public static Block SOLID_FUEL_GENEREATOR;
	public static Block COMPRESSOR;
	public static Block EXTRACTOR;
	public static Block ELECTRIC_FURNACE;
	public static Block SOLAR_PANEL;
	public static Block CREATIVE_PANEL;
	public static Block WATER_MILL;
	public static Block WIND_MILL;
	public static Block RECYCLER;
	public static Block BATTERY_BOX;
	public static Block MVSU;
	public static Block HVSU;
	public static Block SCRAPBOXINATOR;
	public static Block LV_TRANSFORMER;
	public static Block MV_TRANSFORMER;
	public static Block HV_TRANSFORMER;

	public static BlockOre ORE;
	public static BlockOre2 ORE2;
	public static Block STORAGE;
	public static Block STORAGE2;
	public static Block MACHINE_FRAMES;
	public static Block REINFORCED_GLASS;
	public static Block IRON_FURNACE;
	public static Block NUKE;

	public static Block RUBBER_LOG;
	public static Block RUBBER_LEAVES;
	public static Block RUBBER_SAPLING;
	public static Block RUBBER_PLANKS;

	public static Block REFINED_IRON_FENCE;
	public static Block FLARE;
	public static Block CABLE;

	public static void init() {
		THERMAL_GENERATOR = new BlockThermalGenerator();
		registerBlock(THERMAL_GENERATOR, "techreborn.thermalGenerator");
		GameRegistry.registerTileEntity(TileThermalGenerator.class, "TileThermalGeneratorTR");
		Core.proxy.registerCustomBlockStateLocation(THERMAL_GENERATOR, "machines/generators/thermal_generator");

		QUANTUM_TANK = new BlockQuantumTank();
		registerBlock(QUANTUM_TANK, ItemBlockQuantumTank.class, "techreborn.quantumTank");
		GameRegistry.registerTileEntity(TileQuantumTank.class, "TileQuantumTankTR");
		Core.proxy.registerCustomBlockStateLocation(QUANTUM_TANK, "machines/tier3_machines/quantum_tank");

		QUANTUM_CHEST = new BlockQuantumChest();
		registerBlock(QUANTUM_CHEST, ItemBlockQuantumChest.class, "techreborn.quantumChest");
		GameRegistry.registerTileEntity(TileQuantumChest.class, "TileQuantumChestTR");
		Core.proxy.registerCustomBlockStateLocation(QUANTUM_CHEST, "machines/tier3_machines/quantum_chest");

		DIGITAL_CHEST = new BlockDigitalChest();
		registerBlock(DIGITAL_CHEST, ItemBlockDigitalChest.class, "techreborn.digitalChest");
		GameRegistry.registerTileEntity(TileDigitalChest.class, "TileDigitalChestTR");
		Core.proxy.registerCustomBlockStateLocation(DIGITAL_CHEST, "machines/tier2_machines/digital_chest");

		INDUSTRIAL_CENTRIFUGE = new BlockCentrifuge();
		registerBlock(INDUSTRIAL_CENTRIFUGE, "techreborn.centrifuge");
		GameRegistry.registerTileEntity(TileCentrifuge.class, "TileCentrifugeTR");
		Core.proxy.registerCustomBlockStateLocation(INDUSTRIAL_CENTRIFUGE, "machines/tier2_machines/industrial_centrifuge");

		ROLLING_MACHINE = new BlockRollingMachine(Material.ROCK);
		registerBlock(ROLLING_MACHINE, "rollingmachine");
		GameRegistry.registerTileEntity(TileRollingMachine.class, "TileRollingMachineTR");
		Core.proxy.registerCustomBlockStateLocation(ROLLING_MACHINE, "machines/tier1_machines/rolling_machine");

		INDUSTRIAL_BLAST_FURNACE = new BlockBlastFurnace(Material.ROCK);
		registerBlock(INDUSTRIAL_BLAST_FURNACE, "blastFurnace");
		GameRegistry.registerTileEntity(TileBlastFurnace.class, "TileBlastFurnaceTR");
		Core.proxy.registerCustomBlockStateLocation(INDUSTRIAL_BLAST_FURNACE, "machines/tier2_machines/industrial_blast_furnace");

		ALLOY_SMELTER = new BlockAlloySmelter(Material.ROCK);
		registerBlock(ALLOY_SMELTER, "alloySmelter");
		GameRegistry.registerTileEntity(TileAlloySmelter.class, "TileAlloySmalterTR");
		Core.proxy.registerCustomBlockStateLocation(ALLOY_SMELTER, "machines/tier1_machines/electric_alloy_smelter");

		INDUSTRIAL_GRINDER = new BlockIndustrialGrinder(Material.ROCK);
		registerBlock(INDUSTRIAL_GRINDER, "grinder");
		GameRegistry.registerTileEntity(TileIndustrialGrinder.class, "TileIndustrialGrinderTR");
		Core.proxy.registerCustomBlockStateLocation(INDUSTRIAL_GRINDER, "machines/tier2_machines/industrial_grinder");

		IMPLOSION_COMPRESSOR = new BlockImplosionCompressor(Material.ROCK);
		registerBlock(IMPLOSION_COMPRESSOR, "implosioncompressor");
		GameRegistry.registerTileEntity(TileImplosionCompressor.class, "TileImplosionCompressorTR");
		Core.proxy.registerCustomBlockStateLocation(IMPLOSION_COMPRESSOR, "machines/tier2_machines/implosion_compressor");

		MATTER_FABRICATOR = new BlockMatterFabricator(Material.ROCK);
		registerBlock(MATTER_FABRICATOR, "matterfabricator");
		GameRegistry.registerTileEntity(TileMatterFabricator.class, "TileMatterFabricatorTR");
		Core.proxy.registerCustomBlockStateLocation(MATTER_FABRICATOR, "machines/tier3_machines/matter_fabricator");

		CHUNK_LOADER = new BlockChunkLoader(Material.ROCK);
		registerBlock(CHUNK_LOADER, "chunkloader");
		GameRegistry.registerTileEntity(TileChunkLoader.class, "TileChunkLoaderTR");
		Core.proxy.registerCustomBlockStateLocation(CHUNK_LOADER, "machines/tier3_machines/industrial_chunk_loader");

		CHARGE_O_MAT = new BlockChargeBench(Material.ROCK);
		registerBlock(CHARGE_O_MAT, "chargebench");
		GameRegistry.registerTileEntity(TileChargeBench.class, "TileChargeBench");
		Core.proxy.registerCustomBlockStateLocation(CHARGE_O_MAT, "machines/tier2_machines/charge_bench");

		PLAYER_DETECTOR = new BlockPlayerDetector();
		registerBlock(PLAYER_DETECTOR, ItemBlockPlayerDetector.class, "playerDetector");
		GameRegistry.registerTileEntity(TilePlayerDectector.class, "TilePlayerDectectorTR");

		CABLE = new BlockCable();
		registerBlock(CABLE,"cable");
		GameRegistry.registerTileEntity(TileCable.class, "TileCableTR");
		Core.proxy.registerCustomBlockStateLocation(CABLE, "cable");

		MACHINE_CASINGS = new BlockMachineCasing(Material.ROCK);
		registerBlock(MACHINE_CASINGS, ItemBlockMachineCasing.class, "machinecasing");
		GameRegistry.registerTileEntity(TileMachineCasing.class, "TileMachineCasingTR");
		if (Core.proxy.isCTMAvailable()) {
			Core.proxy.registerCustomBlockStateLocation(MACHINE_CASINGS, "machines/structure/machine_casing_ctm");
		} else {
			Core.proxy.registerCustomBlockStateLocation(MACHINE_CASINGS, "machines/structure/machine_casing");
		}

		ORE = new BlockOre(Material.ROCK);
		registerBlock(ORE, ItemBlockOre.class, "techreborn.ore");
		for (int i = 0; i < BlockOre.ores.length; i++) {
			Core.proxy.registerSubBlockInventoryLocation(ORE, i, "storage/ores", BlockOre.ores[i]);
		}

		ORE2 = new BlockOre2(Material.ROCK);
		registerBlock(ORE2, ItemBlockOre2.class, "techreborn.ore2");
		for (int i = 0; i < BlockOre2.ores.length; i++) {
			Core.proxy.registerSubBlockInventoryLocation(ORE2, i, "storage/ores", BlockOre2.ores[i]);
		}

		STORAGE = new BlockStorage(Material.IRON);
		registerBlock(STORAGE, ItemBlockStorage.class, "techreborn.storage");
		for (int i = 0; i < BlockStorage.types.length; i++) {
			Core.proxy.registerSubBlockInventoryLocation(STORAGE, i, "storage/storage", BlockStorage.types[i]);
		}

		STORAGE2 = new BlockStorage2(Material.IRON);
		registerBlock(STORAGE2, ItemBlockStorage2.class, "techreborn.storage2");
		for (int i = 0; i < BlockStorage2.types.length; i++) {
			Core.proxy.registerSubBlockInventoryLocation(STORAGE2, i, "storage/storage", BlockStorage2.types[i]);
		}

		DRAGON_EGG_SIPHONER = new BlockDragonEggSiphoner(Material.ROCK);
		registerBlock(DRAGON_EGG_SIPHONER, "dragoneggenergsiphon");
		GameRegistry.registerTileEntity(TileDragonEggSiphoner.class, "TileDragonEggSiphonerTR");
		Core.proxy.registerCustomBlockStateLocation(DRAGON_EGG_SIPHONER, "machines/generators/dragon_egg_syphon");

		MAGIC_ENERGY_CONVERTER = new BlockMagicEnergyConverter(Material.ROCK);
		registerBlock(MAGIC_ENERGY_CONVERTER, "magicenergyconverter");
		Core.proxy.registerCustomBlockStateLocation(MAGIC_ENERGY_CONVERTER, "machines/generators/magic_energy_converter");

		ASSEMBLY_MACHINE = new BlockAssemblingMachine(Material.ROCK);
		registerBlock(ASSEMBLY_MACHINE, "assemblymachine");
		GameRegistry.registerTileEntity(TileAssemblingMachine.class, "TileAssemblyMachineTR");
		Core.proxy.registerCustomBlockStateLocation(ASSEMBLY_MACHINE, "machines/tier1_machines/assembly_machine");

		DIESEL_GENERATOR = new BlockDieselGenerator(Material.ROCK);
		registerBlock(DIESEL_GENERATOR, "dieselgenerator");
		GameRegistry.registerTileEntity(TileDieselGenerator.class, "TileDieselGeneratorTR");
		Core.proxy.registerCustomBlockStateLocation(DIESEL_GENERATOR, "machines/generators/diesel_generator");

		INDUSTRIAL_ELECTROLYZER = new BlockIndustrialElectrolyzer(Material.ROCK);
		registerBlock(INDUSTRIAL_ELECTROLYZER, "industrialelectrolyzer");
		GameRegistry.registerTileEntity(TileIndustrialElectrolyzer.class, "TileIndustrialElectrolyzerTR");
		Core.proxy.registerCustomBlockStateLocation(INDUSTRIAL_ELECTROLYZER, "machines/tier1_machines/industrial_electrolyzer");

		MAGICAL_ABSORBER = new BlockMagicEnergyAbsorber(Material.ROCK);
		registerBlock(MAGICAL_ABSORBER, "magicrnergyabsorber");
		Core.proxy.registerCustomBlockStateLocation(MAGICAL_ABSORBER, "machines/generators/magic_energy_absorber");

		SEMIFLUID_GENERATOR = new BlockSemiFluidGenerator(Material.ROCK);
		registerBlock(SEMIFLUID_GENERATOR, "semifluidgenerator");
		GameRegistry.registerTileEntity(TileSemifluidGenerator.class, "TileSemifluidGeneratorTR");
		Core.proxy.registerCustomBlockStateLocation(SEMIFLUID_GENERATOR, "machines/generators/semi_fluid_generator");

		GAS_TURBINE = new BlockGasTurbine(Material.ROCK);
		registerBlock(GAS_TURBINE, "gasturbine");
		GameRegistry.registerTileEntity(TileGasTurbine.class, "TileGassTurbineTR");
		Core.proxy.registerCustomBlockStateLocation(GAS_TURBINE, "machines/generators/gas_turbine");

		IRON_ALLOY_FURNACE = new BlockAlloyFurnace(Material.ROCK);
		registerBlock(IRON_ALLOY_FURNACE, "alloyfurnace");
		GameRegistry.registerTileEntity(TileAlloyFurnace.class, "TileAlloyFurnaceTR");
		Core.proxy.registerCustomBlockStateLocation(IRON_ALLOY_FURNACE, "machines/tier0_machines/alloy_furnace");

		CHEMICAL_REACTOR = new BlockChemicalReactor(Material.ROCK);
		registerBlock(CHEMICAL_REACTOR, "chemicalreactor");
		GameRegistry.registerTileEntity(TileChemicalReactor.class, "TileChemicalReactorTR");
		Core.proxy.registerCustomBlockStateLocation(CHEMICAL_REACTOR, "machines/tier1_machines/chemical_reactor");

		INTERDIMENSIONAL_SU = new BlockIDSU();
		registerBlock(INTERDIMENSIONAL_SU, "idsu");
		GameRegistry.registerTileEntity(TileIDSU.class, "TileIDSUTR");

		ADJUSTABLE_SU = new BlockAESU();
		registerBlock(ADJUSTABLE_SU, ItemBlockAesu.class, "aesu");
		GameRegistry.registerTileEntity(TileAesu.class, "TileAesuTR");

		LAPOTRONIC_SU = new BlockLESU();
		registerBlock(LAPOTRONIC_SU, "lesu");
		GameRegistry.registerTileEntity(TileLesu.class, "TileLesuTR");

		LSU_STORAGE_BLOCK = new BlockLESUStorage(Material.ROCK);
		registerBlock(LSU_STORAGE_BLOCK, "lesustorage");
		GameRegistry.registerTileEntity(TileLesuStorage.class, "TileLesuStorageTR");
		if (Core.proxy.isCTMAvailable()) {
			Core.proxy.registerCustomBlockStateLocation(LSU_STORAGE_BLOCK, "machines/energy/ev_multi_storage_ctm");
		} else {
			Core.proxy.registerCustomBlockStateLocation(LSU_STORAGE_BLOCK, "machines/energy/ev_multi_storage");
		}

		DISTILLATION_TOWER = new BlockDistillationTower(Material.ROCK);
		registerBlock(DISTILLATION_TOWER, "distillationtower");
		Core.proxy.registerCustomBlockStateLocation(DISTILLATION_TOWER, "machines/tier2_machines/distillation_tower");

		VACUUM_FREEZER = new BlockVacuumFreezer(Material.ROCK);
		registerBlock(VACUUM_FREEZER, "vacuumfreezer");
		GameRegistry.registerTileEntity(TileVacuumFreezer.class, "TileVacuumFreezerTR");
		Core.proxy.registerCustomBlockStateLocation(VACUUM_FREEZER, "machines/tier2_machines/vacuum_freezer");

		FUSION_CONTROL_COMPUTER = new BlockFusionControlComputer(Material.ROCK);
		registerBlock(FUSION_CONTROL_COMPUTER, "fusioncontrolcomputer");
		GameRegistry.registerTileEntity(TileEntityFusionController.class, "TileEntityFustionControllerTR");
		Core.proxy.registerCustomBlockStateLocation(FUSION_CONTROL_COMPUTER, "machines/generators/fusion_reactor");

		FUSION_COIL = new BlockFusionCoil(Material.ROCK);
		registerBlock(FUSION_COIL, "fusioncoil");
		Core.proxy.registerCustomBlockStateLocation(FUSION_COIL, "machines/generators/fusion_coil");

		LIGHTNING_ROD = new BlockLightningRod(Material.ROCK);
		registerBlock(LIGHTNING_ROD, "lightningrod");
		GameRegistry.registerTileEntity(TileLightningRod.class, "TileLightningRodTR");
		Core.proxy.registerCustomBlockStateLocation(LIGHTNING_ROD, "machines/generators/lightning_rod");

		HEAT_GENERATOR = new BlockHeatGenerator(Material.ROCK);
		registerBlock(HEAT_GENERATOR, "heatgenerator");
		GameRegistry.registerTileEntity(TileHeatGenerator.class, "TileHeatGeneratorTR");

		INDUSTRIAL_SAWMILL = new BlockIndustrialSawmill(Material.ROCK);
		registerBlock(INDUSTRIAL_SAWMILL, "industrialSawmill");
		GameRegistry.registerTileEntity(TileIndustrialSawmill.class, "TileIndustrialSawmillTR");
		Core.proxy.registerCustomBlockStateLocation(INDUSTRIAL_SAWMILL, "machines/tier2_machines/industrial_saw_mill");

		MACHINE_FRAMES = new BlockMachineFrame(Material.IRON);
		registerBlock(MACHINE_FRAMES, ItemBlockMachineFrame.class, "techreborn.machineFrame");
		Core.proxy.registerCustomBlockStateLocation(MACHINE_FRAMES, "machines/storage/machine_blocks");

		GRINDER = new BlockGrinder(Material.IRON);
		registerBlock(GRINDER, "techreborn.grinder");
		GameRegistry.registerTileEntity(TileGrinder.class, "TileGrinderTR");
		Core.proxy.registerCustomBlockStateLocation(GRINDER, "machines/tier1_machines/grinder");

		SOLID_FUEL_GENEREATOR = new BlockGenerator();
		registerBlock(SOLID_FUEL_GENEREATOR, "techreborn.generator");
		GameRegistry.registerTileEntity(TileGenerator.class, "TileGeneratorTR");
		Core.proxy.registerCustomBlockStateLocation(SOLID_FUEL_GENEREATOR, "machines/generators/generator");

		EXTRACTOR = new BlockExtractor(Material.IRON);
		registerBlock(EXTRACTOR, "techreborn.extractor");
		GameRegistry.registerTileEntity(TileExtractor.class, "TileExtractorTR");
		Core.proxy.registerCustomBlockStateLocation(EXTRACTOR, "machines/tier1_machines/extractor");

		COMPRESSOR = new BlockCompressor(Material.IRON);
		registerBlock(COMPRESSOR, "techreborn.compressor");
		GameRegistry.registerTileEntity(TileCompressor.class, "TileCompressorTR");
		Core.proxy.registerCustomBlockStateLocation(COMPRESSOR, "machines/tier1_machines/compressor");

		ELECTRIC_FURNACE = new BlockElectricFurnace(Material.IRON);
		registerBlock(ELECTRIC_FURNACE, "techreborn.electricfurnace");
		GameRegistry.registerTileEntity(TileElectricFurnace.class, "TileElectricFurnaceTR");
		Core.proxy.registerCustomBlockStateLocation(ELECTRIC_FURNACE, "machines/tier1_machines/electric_furnace");

		SOLAR_PANEL = new BlockSolarPanel();
		registerBlock(SOLAR_PANEL, "techreborn.solarpanel");
		GameRegistry.registerTileEntity(TileSolarPanel.class, "TileSolarPanel");
		Core.proxy.registerCustomBlockStateLocation(SOLAR_PANEL, "machines/generators/solar_panel");

		CREATIVE_PANEL = new BlockCreativePanel();
		registerBlock(CREATIVE_PANEL, "techreborn.creativepanel");
		GameRegistry.registerTileEntity(TileCreativePanel.class, "TileCreativePanel");
		Core.proxy.registerCustomBlockStateLocation(CREATIVE_PANEL, "machines/generators/creative_panel");

		WATER_MILL = new BlockWaterMill();
		registerBlock(WATER_MILL, "techreborn.watermill");
		GameRegistry.registerTileEntity(TileWaterMill.class, "TileWaterMill");
		Core.proxy.registerCustomBlockStateLocation(WATER_MILL, "machines/generators/water_mill");

		WIND_MILL = new BlockWindMill();
		registerBlock(WIND_MILL, "techreborn.windmill");
		GameRegistry.registerTileEntity(TileWindMill.class, "TileWindMill");
		Core.proxy.registerCustomBlockStateLocation(WIND_MILL, "machines/generators/wind_mill");

		GameRegistry.registerTileEntity(TileMachineBase.class, "TileMachineBaseTR");

		RUBBER_LOG = new BlockRubberLog();
		registerBlock(RUBBER_LOG, "rubberLog");

		RUBBER_PLANKS = new BlockRubberPlank();
		registerBlock(RUBBER_PLANKS, "rubberPlanks");

		RUBBER_LEAVES = new BlockRubberLeaves();
		registerBlock(RUBBER_LEAVES, "rubberLeaves");

		RUBBER_SAPLING = new BlockRubberSapling();
		registerBlock(RUBBER_SAPLING, ItemBlockRubberSapling.class, "rubberSapling");
		Core.proxy.registerCustomBlockStateLocation(RUBBER_SAPLING, "rubbersapling");

		REFINED_IRON_FENCE = new BlockIronFence();
		registerBlock(REFINED_IRON_FENCE, "ironFence");

		REINFORCED_GLASS = new BlockReinforcedGlass(Material.GLASS);
		registerBlock(REINFORCED_GLASS, "reinforcedglass");

		RECYCLER = new BlockRecycler(Material.IRON);
		registerBlock(RECYCLER, "recycler");
		GameRegistry.registerTileEntity(TileRecycler.class, "TileRecyclerTR");
		Core.proxy.registerCustomBlockStateLocation(RECYCLER, "machines/tier1_machines/recycler");

		BATTERY_BOX = new BlockBatBox();
		registerBlock(BATTERY_BOX, "batBox");
		GameRegistry.registerTileEntity(TileBatBox.class, "TileBatBox");

		MVSU = new BlockMFE();
		registerBlock(MVSU, "MFE");
		GameRegistry.registerTileEntity(TileMFE.class, "TileMFE");

		HVSU = new BlockMFSU();
		registerBlock(HVSU, "MFSU");
		GameRegistry.registerTileEntity(TileMFSU.class, "TileMFSU");

		LV_TRANSFORMER = new BlockLVTransformer();
		registerBlock(LV_TRANSFORMER, "LVT");
		GameRegistry.registerTileEntity(TileLVTransformer.class, "TileLVTransformer");

		MV_TRANSFORMER = new BlockMVTransformer();
		registerBlock(MV_TRANSFORMER, "MVT");
		GameRegistry.registerTileEntity(TileMVTransformer.class, "TileMVTransformer");

		HV_TRANSFORMER = new BlockHVTransformer();
		registerBlock(HV_TRANSFORMER, "HVT");
		GameRegistry.registerTileEntity(TileHVTransformer.class, "TileHVTransformer");

		IRON_FURNACE = new BlockIronFurnace();
		registerBlock(IRON_FURNACE, "ironfurnace");
		GameRegistry.registerTileEntity(TileIronFurnace.class, "TileIronFurnaceTR");
		Core.proxy.registerCustomBlockStateLocation(IRON_FURNACE, "machines/tier0_machines/furnace");

		NUKE = new BlockNuke();
		registerBlock(NUKE, "nuke");

		SCRAPBOXINATOR = new BlockScrapboxinator(Material.IRON);
		registerBlock(SCRAPBOXINATOR, "scrapboxinator");
		GameRegistry.registerTileEntity(TileScrapboxinator.class, "TileScrapboxinatorTR");
		Core.proxy.registerCustomBlockStateLocation(SCRAPBOXINATOR, "machines/tier1_machines/scrapboxinator");

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

	public static void registerBlock(Block block, String name) {
		name = name.toLowerCase();
		block.setRegistryName(name);
		GameRegistry.register(block);
		GameRegistry.register(new ItemBlock(block), block.getRegistryName());
	}

	public static void registerBlock(Block block, Class<? extends ItemBlock> itemclass, String name) {
		name = name.toLowerCase();
		block.setRegistryName(name);
		GameRegistry.register(block);
		try {
			ItemBlock itemBlock = itemclass.getConstructor(Block.class).newInstance(block);
			itemBlock.setRegistryName(name);
			GameRegistry.register(itemBlock);
		} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			e.printStackTrace();
		}

	}

	public static void registerOreDict() {
		for (String ore : BlockOre.ores) {
			OreDictionary.registerOre("ore" + StringUtils.toFirstCapital(ore), BlockOre.getOreByName(ore));
		}

		for (String ore : BlockOre2.ores) {
			OreDictionary.registerOre("ore" + StringUtils.toFirstCapital(ore), BlockOre2.getOreByName(ore));
		}

		OreDictionary.registerOre("blockSilver", BlockStorage.getStorageBlockByName("silver"));
		OreDictionary.registerOre("blockAluminum", BlockStorage.getStorageBlockByName("aluminum"));
		OreDictionary.registerOre("blockAluminium", BlockStorage.getStorageBlockByName("aluminum"));
		OreDictionary.registerOre("blockTitanium", BlockStorage.getStorageBlockByName("titanium"));
		OreDictionary.registerOre("blockChrome", BlockStorage.getStorageBlockByName("chrome"));
		OreDictionary.registerOre("blockSteel", BlockStorage.getStorageBlockByName("steel"));
		OreDictionary.registerOre("blockBrass", BlockStorage.getStorageBlockByName("brass"));
		OreDictionary.registerOre("blockLead", BlockStorage.getStorageBlockByName("lead"));
		OreDictionary.registerOre("blockElectrum", BlockStorage.getStorageBlockByName("electrum"));
		OreDictionary.registerOre("blockZinc", BlockStorage.getStorageBlockByName("zinc"));
		OreDictionary.registerOre("blockPlatinum", BlockStorage.getStorageBlockByName("platinum"));
		OreDictionary.registerOre("blockTungsten", BlockStorage.getStorageBlockByName("tungsten"));
		OreDictionary.registerOre("blockNickel", BlockStorage.getStorageBlockByName("nickel"));
		OreDictionary.registerOre("blockInvar", BlockStorage.getStorageBlockByName("invar"));
		OreDictionary.registerOre("blockIridium", BlockStorage.getStorageBlockByName("iridium"));
		OreDictionary.registerOre("blockCopper", BlockStorage2.getStorageBlockByName("copper", 1));
		OreDictionary.registerOre("blockTin", BlockStorage2.getStorageBlockByName("tin", 1));

		OreDictionary.registerOre("blockTungstensteel", BlockStorage2.getStorageBlockByName("tungstensteel", 1));
		OreDictionary.registerOre("blockRuby", BlockStorage2.getStorageBlockByName("ruby", 1));
		OreDictionary.registerOre("blockSapphire", BlockStorage2.getStorageBlockByName("sapphire", 1));
		OreDictionary.registerOre("blockPeridot", BlockStorage2.getStorageBlockByName("peridot", 1));
		OreDictionary.registerOre("blockYellowGarnet", BlockStorage2.getStorageBlockByName("yellowGarnet", 1));
		OreDictionary.registerOre("blockRedGarnet", BlockStorage2.getStorageBlockByName("redGarnet", 1));

		OreDictionary.registerOre("craftingPiston", Blocks.PISTON);
		OreDictionary.registerOre("craftingPiston", Blocks.STICKY_PISTON);
		OreDictionary.registerOre("crafterWood", Blocks.CRAFTING_TABLE);
		OreDictionary.registerOre("machineBasic", new ItemStack(MACHINE_FRAMES, 1));

		OreDictionary.registerOre("treeSapling", RUBBER_SAPLING);
		OreDictionary.registerOre("saplingRubber", RUBBER_SAPLING);
		OreDictionary.registerOre("logWood", new ItemStack(RUBBER_LOG, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("logRubber", new ItemStack(RUBBER_LOG, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("plankWood", new ItemStack(RUBBER_PLANKS, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("plankRubber", new ItemStack(RUBBER_PLANKS, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("treeLeaves", new ItemStack(RUBBER_LEAVES, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("leavesRubber", new ItemStack(RUBBER_LEAVES, 1, OreDictionary.WILDCARD_VALUE));

		OreDictionary.registerOre("fenceIron", REFINED_IRON_FENCE);

		OreDictionary.registerOre("machineBlockBasic", BlockMachineFrame.getFrameByName("machine", 1));
		OreDictionary.registerOre("machineBlockAdvanced", BlockMachineFrame.getFrameByName("advancedMachine", 1));
		OreDictionary.registerOre("machineBlockHighlyAdvanced", BlockMachineFrame.getFrameByName("highlyAdvancedMachine", 1));

	}

}

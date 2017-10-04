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
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import reborncore.RebornRegistry;
import reborncore.common.tile.TileMachineBase;
import reborncore.common.util.OreUtil;
import reborncore.common.util.StringUtils;
import techreborn.Core;
import techreborn.blocks.*;
import techreborn.blocks.advanced_machine.*;
import techreborn.blocks.cable.BlockCable;
import techreborn.blocks.generator.*;
import techreborn.blocks.generator.solarpanel.BlockSolarPanel;
import techreborn.blocks.iron_machines.BlockIronAlloyFurnace;
import techreborn.blocks.iron_machines.BlockIronFurnace;
import techreborn.blocks.machine.*;
import techreborn.blocks.storage.*;
import techreborn.blocks.tier1.*;
import techreborn.blocks.transformers.BlockHVTransformer;
import techreborn.blocks.transformers.BlockLVTransformer;
import techreborn.blocks.transformers.BlockMVTransformer;
import techreborn.itemblocks.*;
import techreborn.lib.ModInfo;
import techreborn.tiles.*;
import techreborn.tiles.cable.TileCable;
import techreborn.tiles.fusionReactor.TileFusionControlComputer;
import techreborn.tiles.generator.*;
import techreborn.tiles.idsu.TileInterdimensionalSU;
import techreborn.tiles.lesu.TileLSUStorage;
import techreborn.tiles.lesu.TileLapotronicSU;
import techreborn.tiles.multiblock.*;
import techreborn.tiles.storage.TileHighVoltageSU;
import techreborn.tiles.storage.TileLowVoltageSU;
import techreborn.tiles.storage.TileMediumVoltageSU;
import techreborn.tiles.storage.TileAdjustableSU;
import techreborn.tiles.teir1.*;
import techreborn.tiles.transformers.TileHVTransformer;
import techreborn.tiles.transformers.TileLVTransformer;
import techreborn.tiles.transformers.TileMVTransformer;

/**
 * Registers all TR blocks
 */
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
	public static Block DRAGON_EGG_SYPHON;
	public static Block MAGIC_ENERGY_CONVERTER;
	public static Block ASSEMBLY_MACHINE;
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
	public static Block AUTO_CRAFTING_TABLE;

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

	public static Block COMPUTER_CUBE;

	/**
	 * Register blocks
	 */
	public static void init() {
		THERMAL_GENERATOR = new BlockThermalGenerator();
		registerBlock(THERMAL_GENERATOR, "thermal_generator");
		GameRegistry.registerTileEntity(TileThermalGenerator.class, "TileThermalGeneratorTR");

		QUANTUM_TANK = new BlockQuantumTank();
		registerBlock(QUANTUM_TANK, ItemBlockQuantumTank.class, "quantum_tank");
		GameRegistry.registerTileEntity(TileQuantumTank.class, "TileQuantumTankTR");

		QUANTUM_CHEST = new BlockQuantumChest();
		registerBlock(QUANTUM_CHEST, ItemBlockQuantumChest.class, "quantum_chest");
		GameRegistry.registerTileEntity(TileQuantumChest.class, "TileQuantumChestTR");

		DIGITAL_CHEST = new BlockDigitalChest();
		registerBlock(DIGITAL_CHEST, ItemBlockDigitalChest.class, "digital_chest");
		GameRegistry.registerTileEntity(TileDigitalChest.class, "TileDigitalChestTR");

		INDUSTRIAL_CENTRIFUGE = new BlockIndustrialCentrifuge();
		registerBlock(INDUSTRIAL_CENTRIFUGE, "industrial_centrifuge");
		GameRegistry.registerTileEntity(TileIndustrialCentrifuge.class, "TileIndustrialCentrifugeTR");

		ROLLING_MACHINE = new BlockRollingMachine();
		registerBlock(ROLLING_MACHINE, "rolling_machine");
		GameRegistry.registerTileEntity(TileRollingMachine.class, "TileRollingMachineTR");

		INDUSTRIAL_BLAST_FURNACE = new BlockIndustrialBlastFurnace();
		registerBlock(INDUSTRIAL_BLAST_FURNACE, "industrial_blast_furnace");
		GameRegistry.registerTileEntity(TileIndustrialBlastFurnace.class, "TileIndustrialBlastFurnaceTR");

		ALLOY_SMELTER = new BlockAlloySmelter();
		registerBlock(ALLOY_SMELTER, "alloy_smelter");
		GameRegistry.registerTileEntity(TileAlloySmelter.class, "TileAlloySmalterTR");

		INDUSTRIAL_GRINDER = new BlockIndustrialGrinder();
		registerBlock(INDUSTRIAL_GRINDER, "industrial_grinder");
		GameRegistry.registerTileEntity(TileIndustrialGrinder.class, "TileIndustrialGrinderTR");

		IMPLOSION_COMPRESSOR = new BlockImplosionCompressor();
		registerBlock(IMPLOSION_COMPRESSOR, "implosion_compressor");
		GameRegistry.registerTileEntity(TileImplosionCompressor.class, "TileImplosionCompressorTR");

		MATTER_FABRICATOR = new BlockMatterFabricator();
		registerBlock(MATTER_FABRICATOR, "matter_fabricator");
		GameRegistry.registerTileEntity(TileMatterFabricator.class, "TileMatterFabricatorTR");

		CHUNK_LOADER = new BlockChunkLoader();
		registerBlock(CHUNK_LOADER, "chunk_loader");
		GameRegistry.registerTileEntity(TileChunkLoader.class, "TileChunkLoaderTR");

		CHARGE_O_MAT = new BlockChargeOMat();
		registerBlock(CHARGE_O_MAT, "charge_o_mat");
		GameRegistry.registerTileEntity(TileChargeOMat.class, "TileChargeOMatTR");

		PLAYER_DETECTOR = new BlockPlayerDetector();
		registerBlock(PLAYER_DETECTOR, ItemBlockPlayerDetector.class, "player_detector");
		GameRegistry.registerTileEntity(TilePlayerDectector.class, "TilePlayerDectectorTR");

		CABLE = new BlockCable();
		registerBlock(CABLE, ItemBlockCable.class, "cable");
		GameRegistry.registerTileEntity(TileCable.class, "TileCableTR");

		MACHINE_CASINGS = new BlockMachineCasing();
		registerBlock(MACHINE_CASINGS, ItemBlockMachineCasing.class, "machine_casing");
		GameRegistry.registerTileEntity(TileMachineCasing.class, "TileMachineCasingTR");

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
		GameRegistry.registerTileEntity(TileDragonEggSyphon.class, "TileDragonEggSyphonTR");

		MAGIC_ENERGY_CONVERTER = new BlockMagicEnergyConverter();
		registerBlock(MAGIC_ENERGY_CONVERTER, "magic_energy_converter");

		ASSEMBLY_MACHINE = new BlockAssemblingMachine();
		registerBlock(ASSEMBLY_MACHINE, "assembly_machine");
		GameRegistry.registerTileEntity(TileAssemblingMachine.class, "TileAssemblyMachineTR");

		DIESEL_GENERATOR = new BlockDieselGenerator();
		registerBlock(DIESEL_GENERATOR, "diesel_generator");
		GameRegistry.registerTileEntity(TileDieselGenerator.class, "TileDieselGeneratorTR");

		INDUSTRIAL_ELECTROLYZER = new BlockIndustrialElectrolyzer();
		registerBlock(INDUSTRIAL_ELECTROLYZER, "industrial_electrolyzer");
		GameRegistry.registerTileEntity(TileIndustrialElectrolyzer.class, "TileIndustrialElectrolyzerTR");

		MAGICAL_ABSORBER = new BlockMagicEnergyAbsorber();
		registerBlock(MAGICAL_ABSORBER, "magic_energy_absorber");

		SEMI_FLUID_GENERATOR = new BlockSemiFluidGenerator();
		registerBlock(SEMI_FLUID_GENERATOR, "semi_fluid_generator");
		GameRegistry.registerTileEntity(TileSemiFluidGenerator.class, "TileSemiFluidGeneratorTR");

		GAS_TURBINE = new BlockGasTurbine();
		registerBlock(GAS_TURBINE, "gas_turbine");
		GameRegistry.registerTileEntity(TileGasTurbine.class, "TileGasTurbineTR");

		IRON_ALLOY_FURNACE = new BlockIronAlloyFurnace();
		registerBlock(IRON_ALLOY_FURNACE, "iron_alloy_furnace");
		GameRegistry.registerTileEntity(TileIronAlloyFurnace.class, "TileIronAlloyFurnaceTR");

		CHEMICAL_REACTOR = new BlockChemicalReactor();
		registerBlock(CHEMICAL_REACTOR, "chemical_reactor");
		GameRegistry.registerTileEntity(TileChemicalReactor.class, "TileChemicalReactorTR");

		INTERDIMENSIONAL_SU = new BlockInterdimensionalSU();
		registerBlock(INTERDIMENSIONAL_SU, "interdimensional_su");
		GameRegistry.registerTileEntity(TileInterdimensionalSU.class, "TileInterdimensionalSUTR");

		ADJUSTABLE_SU = new BlockAdjustableSU();
		registerBlock(ADJUSTABLE_SU, ItemBlockAdjustableSU.class, "adjustable_su");
		GameRegistry.registerTileEntity(TileAdjustableSU.class, "TileAdjustableSUTR");

		LAPOTRONIC_SU = new BlockLapotronicSU();
		registerBlock(LAPOTRONIC_SU, "lapotronic_su");
		GameRegistry.registerTileEntity(TileLapotronicSU.class, "TileLapotronicSUTR");

		LSU_STORAGE = new BlockLSUStorage();
		registerBlock(LSU_STORAGE, "lsu_storage");
		GameRegistry.registerTileEntity(TileLSUStorage.class, "TileLSUStorageTR");

		DISTILLATION_TOWER = new BlockDistillationTower();
		registerBlock(DISTILLATION_TOWER, "distillation_tower");

		VACUUM_FREEZER = new BlockVacuumFreezer();
		registerBlock(VACUUM_FREEZER, "vacuum_freezer");
		GameRegistry.registerTileEntity(TileVacuumFreezer.class, "TileVacuumFreezerTR");

		FUSION_CONTROL_COMPUTER = new BlockFusionControlComputer();
		registerBlock(FUSION_CONTROL_COMPUTER, "fusion_control_computer");
		GameRegistry.registerTileEntity(TileFusionControlComputer.class, "TileFusionControlComputerTR");

		FUSION_COIL = new BlockFusionCoil();
		registerBlock(FUSION_COIL, "fusion_coil");

		LIGHTNING_ROD = new BlockLightningRod();
		registerBlock(LIGHTNING_ROD, "lightning_rod");
		GameRegistry.registerTileEntity(TileLightningRod.class, "TileLightningRodTR");

		INDUSTRIAL_SAWMILL = new BlockIndustrialSawmill();
		registerBlock(INDUSTRIAL_SAWMILL, "industrial_sawmill");
		GameRegistry.registerTileEntity(TileIndustrialSawmill.class, "TileIndustrialSawmillTR");

		MACHINE_FRAMES = new BlockMachineFrames();
		registerBlock(MACHINE_FRAMES, ItemBlockMachineFrames.class, "machine_frame");

		GRINDER = new BlockGrinder();
		registerBlock(GRINDER, "grinder");
		GameRegistry.registerTileEntity(TileGrinder.class, "TileGrinderTR");

		SOLID_FUEL_GENEREATOR = new BlockSolidFuelGenerator();
		registerBlock(SOLID_FUEL_GENEREATOR, "solid_fuel_generator");
		GameRegistry.registerTileEntity(TileSolidFuelGenerator.class, "TileSolidFuelGeneratorTR");

		EXTRACTOR = new BlockExtractor();
		registerBlock(EXTRACTOR, "extractor");
		GameRegistry.registerTileEntity(TileExtractor.class, "TileExtractorTR");

		COMPRESSOR = new BlockCompressor();
		registerBlock(COMPRESSOR, "compressor");
		GameRegistry.registerTileEntity(TileCompressor.class, "TileCompressorTR");

		ELECTRIC_FURNACE = new BlockElectricFurnace();
		registerBlock(ELECTRIC_FURNACE, "electric_furnace");
		GameRegistry.registerTileEntity(TileElectricFurnace.class, "TileElectricFurnaceTR");

		SOLAR_PANEL = new BlockSolarPanel();
		registerBlock(SOLAR_PANEL, ItemBlockSolarPanel.class,  "solar_panel");
		GameRegistry.registerTileEntity(TileSolarPanel.class, "TileSolarPanelTR");

		CREATIVE_SOLAR_PANEL = new BlockCreativeSolarPanel();
		registerBlock(CREATIVE_SOLAR_PANEL, "creative_solar_panel");
		GameRegistry.registerTileEntity(TileCreativeSolarPanel.class, "TileCreativeSolarPanelTR");

		WATER_MILL = new BlockWaterMill();
		registerBlock(WATER_MILL, "water_mill");
		GameRegistry.registerTileEntity(TileWaterMill.class, "TileWaterMillTR");

		WIND_MILL = new BlockWindMill();
		registerBlock(WIND_MILL, "wind_mill");
		GameRegistry.registerTileEntity(TileWindMill.class, "TileWindMillTR");

		GameRegistry.registerTileEntity(TileMachineBase.class, "TileMachineBaseTR");

		RUBBER_LOG = new BlockRubberLog();
		registerBlock(RUBBER_LOG, "rubber_log");

		RUBBER_PLANKS = new BlockRubberPlank();
		registerBlock(RUBBER_PLANKS, "rubber_planks");

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
		GameRegistry.registerTileEntity(TileRecycler.class, "TileRecyclerTR");

		LOW_VOLTAGE_SU = new BlockLowVoltageSU();
		registerBlock(LOW_VOLTAGE_SU, "low_voltage_su");
		GameRegistry.registerTileEntity(TileLowVoltageSU.class, "TileLowVoltageSUTR");

		MEDIUM_VOLTAGE_SU = new BlockMediumVoltageSU();
		registerBlock(MEDIUM_VOLTAGE_SU, "medium_voltage_su");
		GameRegistry.registerTileEntity(TileMediumVoltageSU.class, "TileMediumVoltageSUTR");

		HIGH_VOLTAGE_SU = new BlockHighVoltageSU();
		registerBlock(HIGH_VOLTAGE_SU, "high_voltage_su");
		GameRegistry.registerTileEntity(TileHighVoltageSU.class, "TileHighVoltageSUTR");

		LV_TRANSFORMER = new BlockLVTransformer();
		registerBlock(LV_TRANSFORMER, "lv_transformer");
		GameRegistry.registerTileEntity(TileLVTransformer.class, "TileLVTransformerTR");

		MV_TRANSFORMER = new BlockMVTransformer();
		registerBlock(MV_TRANSFORMER, "mv_transformer");
		GameRegistry.registerTileEntity(TileMVTransformer.class, "TileMVTransformerTR");

		HV_TRANSFORMER = new BlockHVTransformer();
		registerBlock(HV_TRANSFORMER, "hv_transformer");
		GameRegistry.registerTileEntity(TileHVTransformer.class, "TileHVTransformerTR");

		AUTO_CRAFTING_TABLE = new BlockAutoCraftingTable();
		registerBlock(AUTO_CRAFTING_TABLE, "auto_crafting_table");
		GameRegistry.registerTileEntity(TileAutoCraftingTable.class, "TileAutoCraftingTableTR");

		IRON_FURNACE = new BlockIronFurnace();
		registerBlock(IRON_FURNACE, "iron_furnace");
		GameRegistry.registerTileEntity(TileIronFurnace.class, "TileIronFurnaceTR");

		NUKE = new BlockNuke();
		registerBlock(NUKE, "nuke");

		SCRAPBOXINATOR = new BlockScrapboxinator();
		registerBlock(SCRAPBOXINATOR, "scrapboxinator");
		GameRegistry.registerTileEntity(TileScrapboxinator.class, "TileScrapboxinatorTR");

		COMPUTER_CUBE = new BlockComputerCube();
		registerBlock(COMPUTER_CUBE, "computer_cube");

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
		block.setUnlocalizedName(ModInfo.MOD_ID + ":" + name);
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
		block.setUnlocalizedName(ModInfo.MOD_ID + ":" + name);
		RebornRegistry.registerBlock(block, itemclass, new ResourceLocation(ModInfo.MOD_ID, name));
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
		OreUtil.registerOre("plankRubber", new ItemStack(RUBBER_PLANKS, 1, OreDictionary.WILDCARD_VALUE));
		OreUtil.registerOre("treeLeaves", new ItemStack(RUBBER_LEAVES, 1, OreDictionary.WILDCARD_VALUE));
		OreUtil.registerOre("leavesRubber", new ItemStack(RUBBER_LEAVES, 1, OreDictionary.WILDCARD_VALUE));

		OreUtil.registerOre("fenceIron", REFINED_IRON_FENCE);

		OreUtil.registerOre("machineBlockBasic", BlockMachineFrames.getFrameByName("basic", 1));
		OreUtil.registerOre("machineBlockAdvanced", BlockMachineFrames.getFrameByName("advanced", 1));
		OreUtil.registerOre("machineBlockHighlyAdvanced", BlockMachineFrames.getFrameByName("highly_advanced", 1));

	}

}

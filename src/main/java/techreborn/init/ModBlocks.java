package techreborn.init;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.ArrayUtils;
import reborncore.common.tile.TileMachineBase;
import reborncore.common.util.StringUtils;
import techreborn.Core;
import techreborn.blocks.*;
import techreborn.blocks.advanced_machine.BlockBlastFurnace;
import techreborn.blocks.advanced_machine.BlockCentrifuge;
import techreborn.blocks.advanced_machine.BlockDistillationTower;
import techreborn.blocks.advanced_machine.BlockImplosionCompressor;
import techreborn.blocks.advanced_machine.BlockIndustrialElectrolyzer;
import techreborn.blocks.advanced_machine.BlockIndustrialGrinder;
import techreborn.blocks.advanced_machine.BlockIndustrialSawmill;
import techreborn.blocks.generator.BlockDieselGenerator;
import techreborn.blocks.generator.BlockDragonEggSiphoner;
import techreborn.blocks.generator.BlockGasTurbine;
import techreborn.blocks.generator.BlockGenerator;
import techreborn.blocks.generator.BlockLightningRod;
import techreborn.blocks.generator.BlockMagicEnergyAbsorber;
import techreborn.blocks.generator.BlockMagicEnergyConverter;
import techreborn.blocks.generator.BlockPlasmaGenerator;
import techreborn.blocks.generator.BlockSemiFluidGenerator;
import techreborn.blocks.generator.BlockSolarPanel;
import techreborn.blocks.generator.BlockThermalGenerator;
import techreborn.blocks.generator.BlockWaterMill;
import techreborn.blocks.generator.BlockWindMill;
import techreborn.blocks.iron_machines.BlockAlloyFurnace;
import techreborn.blocks.iron_machines.BlockIronFurnace;
import techreborn.blocks.machine.BlockAssemblingMachine;
import techreborn.blocks.machine.BlockChargeBench;
import techreborn.blocks.machine.BlockChemicalReactor;
import techreborn.blocks.machine.BlockChunkLoader;
import techreborn.blocks.machine.BlockMatterFabricator;
import techreborn.blocks.machine.BlockRollingMachine;
import techreborn.blocks.machine.BlockScrapboxinator;
import techreborn.blocks.machine.BlockVacuumFreezer;
import techreborn.blocks.storage.BlockAESU;
import techreborn.blocks.storage.BlockBatBox;
import techreborn.blocks.storage.BlockIDSU;
import techreborn.blocks.storage.BlockLESU;
import techreborn.blocks.storage.BlockLESUStorage;
import techreborn.blocks.storage.BlockMFE;
import techreborn.blocks.storage.BlockMFSU;
import techreborn.blocks.tier1.BlockAlloySmelter;
import techreborn.blocks.tier1.BlockCompressor;
import techreborn.blocks.tier1.BlockElectricFurnace;
import techreborn.blocks.tier1.BlockExtractor;
import techreborn.blocks.tier1.BlockGrinder;
import techreborn.blocks.tier1.BlockRecycler;
import techreborn.blocks.transformers.BlockHVTransformer;
import techreborn.blocks.transformers.BlockLVTransformer;
import techreborn.blocks.transformers.BlockMVTransformer;
import techreborn.itemblocks.ItemBlockAesu;
import techreborn.itemblocks.ItemBlockDigitalChest;
import techreborn.itemblocks.ItemBlockMachineCasing;
import techreborn.itemblocks.ItemBlockMachineFrame;
import techreborn.itemblocks.ItemBlockOre;
import techreborn.itemblocks.ItemBlockOre2;
import techreborn.itemblocks.ItemBlockPlayerDetector;
import techreborn.itemblocks.ItemBlockQuantumChest;
import techreborn.itemblocks.ItemBlockQuantumTank;
import techreborn.itemblocks.ItemBlockRubberSapling;
import techreborn.itemblocks.ItemBlockStorage;
import techreborn.itemblocks.ItemBlockStorage2;
import techreborn.tiles.*;
import techreborn.tiles.fusionReactor.TileEntityFusionController;
import techreborn.tiles.generator.TileDieselGenerator;
import techreborn.tiles.generator.TileDragonEggSiphoner;
import techreborn.tiles.generator.TileGasTurbine;
import techreborn.tiles.generator.TileGenerator;
import techreborn.tiles.generator.TileSemifluidGenerator;
import techreborn.tiles.generator.TileSolarPanel;
import techreborn.tiles.generator.TileThermalGenerator;
import techreborn.tiles.generator.TileWaterMill;
import techreborn.tiles.generator.TileWindMill;
import techreborn.tiles.idsu.TileIDSU;
import techreborn.tiles.lesu.TileLesu;
import techreborn.tiles.lesu.TileLesuStorage;
import techreborn.tiles.storage.TileBatBox;
import techreborn.tiles.storage.TileMFE;
import techreborn.tiles.storage.TileMFSU;
import techreborn.tiles.teir1.TileCompressor;
import techreborn.tiles.teir1.TileElectricFurnace;
import techreborn.tiles.teir1.TileExtractor;
import techreborn.tiles.teir1.TileGrinder;
import techreborn.tiles.teir1.TileRecycler;
import techreborn.tiles.transformers.TileHVTransformer;
import techreborn.tiles.transformers.TileLVTransformer;
import techreborn.tiles.transformers.TileMVTransformer;

import java.lang.reflect.InvocationTargetException;

public class ModBlocks
{

	public static Block thermalGenerator;
	public static Block quantumTank;
	public static Block quantumChest;
	public static Block digitalChest;
	public static Block centrifuge;
	public static Block RollingMachine;
	public static Block MachineCasing;
	public static Block BlastFurnace;
	public static Block AlloySmelter;
	public static Block IndustrialGrinder;
	public static Block ImplosionCompressor;
	public static Block MatterFabricator;
	public static Block ChunkLoader;
	public static Block Dragoneggenergysiphoner;
	public static Block Magicenergeyconverter;
	public static Block AssemblyMachine;
	public static Block DieselGenerator;
	public static Block IndustrialElectrolyzer;
	public static Block MagicalAbsorber;
	public static Block Semifluidgenerator;
	public static Block Gasturbine;
	public static Block AlloyFurnace;
	public static Block ChemicalReactor;
	public static Block Idsu;
	public static Block Aesu;
	public static Block Lesu;
	public static Block Supercondensator;
	public static Block LesuStorage;
	public static Block Distillationtower;
	public static Block ElectricCraftingTable;
	public static Block VacuumFreezer;
	public static Block PlasmaGenerator;
	public static Block FusionControlComputer;
	public static Block ComputerCube;
	public static Block FusionCoil;
	public static Block LightningRod;
	public static Block industrialSawmill;
	public static Block chargeBench;
	public static Block playerDetector;
	public static Block Grinder;
	public static Block Generator;
	public static Block Compressor;
	public static Block Extractor;
	public static Block ElectricFurnace;
	public static Block solarPanel;
	public static Block waterMill;
	public static Block windMill;
	public static Block recycler;
	public static Block batBox;
	public static Block mfe;
	public static Block mfsu;
	public static Block scrapboxinator;
	public static Block lvt;
	public static Block mvt;
	public static Block hvt;
	public static Block pump;

	public static BlockOre ore;
	public static BlockOre2 ore2;
	public static Block storage;
	public static Block storage2;
	public static Block machineframe;
	public static Block reinforcedglass;
	public static Block ironFurnace;
	public static Block nuke;

	public static Block rubberLog;
	public static Block rubberLeaves;
	public static Block rubberSapling;
	public static Block rubberPlanks;

	public static Block ironFence;
	public static Block distributor;

	public static void init()
	{
		thermalGenerator = new BlockThermalGenerator();
		registerBlock(thermalGenerator, "techreborn.thermalGenerator");
		GameRegistry.registerTileEntity(TileThermalGenerator.class, "TileThermalGeneratorTR");
		Core.proxy.registerCustomBlockStateLocation(thermalGenerator, "machines/generators/thermal_generator");

		quantumTank = new BlockQuantumTank();
		registerBlock(quantumTank, ItemBlockQuantumTank.class, "techreborn.quantumTank");
		GameRegistry.registerTileEntity(TileQuantumTank.class, "TileQuantumTankTR");
		Core.proxy.registerCustomBlockStateLocation(quantumTank, "machines/tier3_machines/quantum_tank");

		quantumChest = new BlockQuantumChest();
		registerBlock(quantumChest, ItemBlockQuantumChest.class, "techreborn.quantumChest");
		GameRegistry.registerTileEntity(TileQuantumChest.class, "TileQuantumChestTR");
		Core.proxy.registerCustomBlockStateLocation(quantumChest, "machines/tier3_machines/quantum_chest");


		digitalChest = new BlockDigitalChest();
		registerBlock(digitalChest, ItemBlockDigitalChest.class, "techreborn.digitalChest");
		GameRegistry.registerTileEntity(TileDigitalChest.class, "TileDigitalChestTR");
		Core.proxy.registerCustomBlockStateLocation(digitalChest, "machines/tier2_machines/digital_chest");


		centrifuge = new BlockCentrifuge();
		registerBlock(centrifuge, "techreborn.centrifuge");
		GameRegistry.registerTileEntity(TileCentrifuge.class, "TileCentrifugeTR");
		Core.proxy.registerCustomBlockStateLocation(centrifuge, "machines/tier2_machines/centrifuge");

		RollingMachine = new BlockRollingMachine(Material.ROCK);
		registerBlock(RollingMachine, "rollingmachine");
		GameRegistry.registerTileEntity(TileRollingMachine.class, "TileRollingMachineTR");
		Core.proxy.registerCustomBlockStateLocation(RollingMachine, "machines/tier1_machines/rolling_machine");


		BlastFurnace = new BlockBlastFurnace(Material.ROCK);
		registerBlock(BlastFurnace, "blastFurnace");
		GameRegistry.registerTileEntity(TileBlastFurnace.class, "TileBlastFurnaceTR");
		Core.proxy.registerCustomBlockStateLocation(BlastFurnace, "machines/tier2_machines/industrial_blast_furnace");


		AlloySmelter = new BlockAlloySmelter(Material.ROCK);
		registerBlock(AlloySmelter, "alloySmelter");
		GameRegistry.registerTileEntity(TileAlloySmelter.class, "TileAlloySmalterTR");
		Core.proxy.registerCustomBlockStateLocation(AlloySmelter, "machines/tier1_machines/electric_alloy_smelter");

		IndustrialGrinder = new BlockIndustrialGrinder(Material.ROCK);
		registerBlock(IndustrialGrinder, "grinder");
		GameRegistry.registerTileEntity(TileIndustrialGrinder.class, "TileIndustrialGrinderTR");
		Core.proxy.registerCustomBlockStateLocation(IndustrialGrinder, "machines/tier2_machines/industrial_grinder");


		ImplosionCompressor = new BlockImplosionCompressor(Material.ROCK);
		registerBlock(ImplosionCompressor, "implosioncompressor");
		GameRegistry.registerTileEntity(TileImplosionCompressor.class, "TileImplosionCompressorTR");
		Core.proxy.registerCustomBlockStateLocation(ImplosionCompressor, "machines/tier2_machines/implosion_compressor");

		MatterFabricator = new BlockMatterFabricator(Material.ROCK);
		registerBlock(MatterFabricator, "matterfabricator");
		GameRegistry.registerTileEntity(TileMatterFabricator.class, "TileMatterFabricatorTR");
		Core.proxy.registerCustomBlockStateLocation(MatterFabricator, "machines/tier3_machines/matter_fabricator");


		ChunkLoader = new BlockChunkLoader(Material.ROCK);
		registerBlock(ChunkLoader, "chunkloader");
		GameRegistry.registerTileEntity(TileChunkLoader.class, "TileChunkLoaderTR");
		Core.proxy.registerCustomBlockStateLocation(ChunkLoader, "machines/tier3_machines/industrial_chunk_loader");


		chargeBench = new BlockChargeBench(Material.ROCK);
		registerBlock(chargeBench, "chargebench");
		GameRegistry.registerTileEntity(TileChargeBench.class, "TileChargeBench");

		playerDetector = new BlockPlayerDetector();
		registerBlock(playerDetector, ItemBlockPlayerDetector.class, "playerDetector");
		GameRegistry.registerTileEntity(TilePlayerDectector.class, "TilePlayerDectectorTR");

		MachineCasing = new BlockMachineCasing(Material.ROCK);
		registerBlock(MachineCasing, ItemBlockMachineCasing.class, "machinecasing");
		GameRegistry.registerTileEntity(TileMachineCasing.class, "TileMachineCasingTR");

		ore = new BlockOre(Material.ROCK);
		registerBlock(ore, ItemBlockOre.class, "techreborn.ore");
		Core.proxy.registerCustomBlockStateLocation(ore, "storage/ores");
		for (int i = 0; i < BlockOre.ores.length; i++) {
			Core.proxy.registerSubBlockInventoryLocation(ore, i, "storage/ores", BlockOre.ores[i]);
		}

		ore2 = new BlockOre2(Material.ROCK);
		registerBlock(ore2, ItemBlockOre2.class, "techreborn.ore2");
		Core.proxy.registerCustomBlockStateLocation(ore2, "storage/ores");
		for (int i = 0; i < BlockOre2.ores.length; i++) {
			Core.proxy.registerSubBlockInventoryLocation(ore2, i, "storage/ores", BlockOre2.ores[i]);
		}

		storage = new BlockStorage(Material.IRON);
		registerBlock(storage, ItemBlockStorage.class, "techreborn.storage");
		Core.proxy.registerCustomBlockStateLocation(storage, "storage/storage");
		for (int i = 0; i < BlockStorage.types.length; i++) {
			Core.proxy.registerSubBlockInventoryLocation(storage, i, "storage/storage", BlockStorage.types[i]);
		}

		storage2 = new BlockStorage2(Material.IRON);
		registerBlock(storage2, ItemBlockStorage2.class, "techreborn.storage2");
		Core.proxy.registerCustomBlockStateLocation(storage2, "storage/storage");
		for (int i = 0; i < BlockStorage2.types.length; i++) {
			Core.proxy.registerSubBlockInventoryLocation(storage2, i, "storage/storage", BlockStorage2.types[i]);
		}

		Dragoneggenergysiphoner = new BlockDragonEggSiphoner(Material.ROCK);
		registerBlock(Dragoneggenergysiphoner, "dragoneggenergsiphon");
		GameRegistry.registerTileEntity(TileDragonEggSiphoner.class, "TileDragonEggSiphonerTR");
		Core.proxy.registerCustomBlockStateLocation(Dragoneggenergysiphoner, "machines/generators/dragon_egg_syphon");


		Magicenergeyconverter = new BlockMagicEnergyConverter(Material.ROCK);
		registerBlock(Magicenergeyconverter, "magicenergyconverter");
		Core.proxy.registerCustomBlockStateLocation(Magicenergeyconverter, "machines/generators/magic_energy_converter");


		AssemblyMachine = new BlockAssemblingMachine(Material.ROCK);
		registerBlock(AssemblyMachine, "assemblymachine");
		GameRegistry.registerTileEntity(TileAssemblingMachine.class, "TileAssemblyMachineTR");
		Core.proxy.registerCustomBlockStateLocation(AssemblyMachine, "machines/tier1_machines/assembly_machine");


		DieselGenerator = new BlockDieselGenerator(Material.ROCK);
		registerBlock(DieselGenerator, "dieselgenerator");
		GameRegistry.registerTileEntity(TileDieselGenerator.class, "TileDieselGeneratorTR");
		Core.proxy.registerCustomBlockStateLocation(DieselGenerator, "machines/generators/diesel_generator");


		IndustrialElectrolyzer = new BlockIndustrialElectrolyzer(Material.ROCK);
		registerBlock(IndustrialElectrolyzer, "industrialelectrolyzer");
		GameRegistry.registerTileEntity(TileIndustrialElectrolyzer.class, "TileIndustrialElectrolyzerTR");
		Core.proxy.registerCustomBlockStateLocation(IndustrialElectrolyzer, "machines/tier1_machines/industrial_electrolyzer");


		MagicalAbsorber = new BlockMagicEnergyAbsorber(Material.ROCK);
		registerBlock(MagicalAbsorber, "magicrnergyabsorber");
		Core.proxy.registerCustomBlockStateLocation(MagicalAbsorber, "machines/generators/magic_energy_absorber");


		Semifluidgenerator = new BlockSemiFluidGenerator(Material.ROCK);
		registerBlock(Semifluidgenerator, "semifluidgenerator");
		GameRegistry.registerTileEntity(TileSemifluidGenerator.class, "TileSemifluidGeneratorTR");
		Core.proxy.registerCustomBlockStateLocation(Semifluidgenerator, "machines/generators/semi_fluid_generator");


		Gasturbine = new BlockGasTurbine(Material.ROCK);
		registerBlock(Gasturbine, "gasturbine");
		GameRegistry.registerTileEntity(TileGasTurbine.class, "TileGassTurbineTR");
		Core.proxy.registerCustomBlockStateLocation(Gasturbine, "machines/generators/gas_turbine");


		AlloyFurnace = new BlockAlloyFurnace(Material.ROCK);
		registerBlock(AlloyFurnace, "alloyfurnace");
		GameRegistry.registerTileEntity(TileAlloyFurnace.class, "TileAlloyFurnaceTR");
		Core.proxy.registerCustomBlockStateLocation(AlloyFurnace, "machines/tier0_machines/alloy_furnace");


		ChemicalReactor = new BlockChemicalReactor(Material.ROCK);
		registerBlock(ChemicalReactor, "chemicalreactor");
		GameRegistry.registerTileEntity(TileChemicalReactor.class, "TileChemicalReactorTR");
		Core.proxy.registerCustomBlockStateLocation(ChemicalReactor, "machines/tier1_machines/chemical_reactor");


		Idsu = new BlockIDSU();
		registerBlock(Idsu, "idsu");
		GameRegistry.registerTileEntity(TileIDSU.class, "TileIDSUTR");
		Core.proxy.registerCustomBlockStateLocation(Idsu, "machines/energy/ev_storage_transmitter");


		Aesu = new BlockAESU();
		registerBlock(Aesu, ItemBlockAesu.class, "aesu");
		GameRegistry.registerTileEntity(TileAesu.class, "TileAesuTR");
		Core.proxy.registerCustomBlockStateLocation(Aesu, "machines/energy/ev_storage_adjust");


		Lesu = new BlockLESU();
		registerBlock(Lesu, "lesu");
		GameRegistry.registerTileEntity(TileLesu.class, "TileLesuTR");
		Core.proxy.registerCustomBlockStateLocation(Lesu, "machines/energy/ev_storage_multi");


		Supercondensator = new BlockSupercondensator(Material.ROCK);
		registerBlock(Supercondensator, "supercondensator");


		LesuStorage = new BlockLESUStorage(Material.ROCK);
		registerBlock(LesuStorage, "lesustorage");
		GameRegistry.registerTileEntity(TileLesuStorage.class, "TileLesuStorageTR");
		Core.proxy.registerCustomBlockStateLocation(Lesu, "machines/energy/ev_storage_multi_storage");


		Distillationtower = new BlockDistillationTower(Material.ROCK);
		registerBlock(Distillationtower, "distillationtower");
		Core.proxy.registerCustomBlockStateLocation(Distillationtower, "machines/tier2_machines/distillation_tower");


		ElectricCraftingTable = new BlockElectricCraftingTable(Material.ROCK);
		registerBlock(ElectricCraftingTable, "electriccraftingtable");
		Core.proxy.registerCustomBlockStateLocation(ElectricCraftingTable, "machines/tier1_machines/electric_crafting_table");


		VacuumFreezer = new BlockVacuumFreezer(Material.ROCK);
		registerBlock(VacuumFreezer, "vacuumfreezer");
		GameRegistry.registerTileEntity(TileVacuumFreezer.class, "TileVacuumFreezerTR");
		Core.proxy.registerCustomBlockStateLocation(VacuumFreezer, "machines/tier2_machines/vacuum_freezer");

		PlasmaGenerator = new BlockPlasmaGenerator(Material.ROCK);
		registerBlock(PlasmaGenerator, "plasmagenerator");
		Core.proxy.registerCustomBlockStateLocation(PlasmaGenerator, "machines/generators/plasma_generator");


		ComputerCube = new BlockComputerCube(Material.ROCK);
		registerBlock(ComputerCube, "computercube");
		Core.proxy.registerCustomBlockStateLocation(ComputerCube, "machines/machines/tier2_machines/computer_cube");

		FusionControlComputer = new BlockFusionControlComputer(Material.ROCK);
		registerBlock(FusionControlComputer, "fusioncontrolcomputer");
		GameRegistry.registerTileEntity(TileEntityFusionController.class, "TileEntityFustionControllerTR");

		FusionCoil = new BlockFusionCoil(Material.ROCK);
		registerBlock(FusionCoil, "fusioncoil");

		LightningRod = new BlockLightningRod(Material.ROCK);
		registerBlock(LightningRod, "lightningrod");
		Core.proxy.registerCustomBlockStateLocation(LightningRod, "machines/generators/lightning_rod");


		industrialSawmill = new BlockIndustrialSawmill(Material.ROCK);
		registerBlock(industrialSawmill, "industrialSawmill");
		GameRegistry.registerTileEntity(TileIndustrialSawmill.class, "TileIndustrialSawmillTR");
		Core.proxy.registerCustomBlockStateLocation(industrialSawmill, "machines/tier2_machines/industrial_saw_mill");


		machineframe = new BlockMachineFrame(Material.IRON);
		registerBlock(machineframe, ItemBlockMachineFrame.class, "techreborn.machineFrame");

		Grinder = new BlockGrinder(Material.IRON);
		registerBlock(Grinder, "techreborn.grinder");
		GameRegistry.registerTileEntity(TileGrinder.class, "TileGrinderTR");
		Core.proxy.registerCustomBlockStateLocation(Grinder, "machines/tier1_machines/grinder");


		Generator = new BlockGenerator();
		registerBlock(Generator, "techreborn.generator");
		GameRegistry.registerTileEntity(TileGenerator.class, "TileGeneratorTR");
		Core.proxy.registerCustomBlockStateLocation(Generator, "machines/generators/generator");


		Extractor = new BlockExtractor(Material.IRON);
		registerBlock(Extractor, "techreborn.extractor");
		GameRegistry.registerTileEntity(TileExtractor.class, "TileExtractorTR");
		Core.proxy.registerCustomBlockStateLocation(Extractor, "machines/tier1_machines/extractor");


		Compressor = new BlockCompressor(Material.IRON);
		registerBlock(Compressor, "techreborn.compressor");
		GameRegistry.registerTileEntity(TileCompressor.class, "TileCompressorTR");
		Core.proxy.registerCustomBlockStateLocation(Compressor, "machines/tier1_machines/compressor");


		ElectricFurnace = new BlockElectricFurnace(Material.IRON);
		registerBlock(ElectricFurnace, "techreborn.electricfurnace");
		GameRegistry.registerTileEntity(TileElectricFurnace.class, "TileElectricFurnaceTR");
		Core.proxy.registerCustomBlockStateLocation(ElectricFurnace, "machines/tier1_machines/electric_furnace");


		solarPanel = new BlockSolarPanel();
		registerBlock(solarPanel, "techreborn.solarpanel");
		GameRegistry.registerTileEntity(TileSolarPanel.class, "TileSolarPanel");
		Core.proxy.registerCustomBlockStateLocation(solarPanel, "machines/generators/solar_panel");


		waterMill = new BlockWaterMill();
		registerBlock(waterMill, "techreborn.watermill");
		GameRegistry.registerTileEntity(TileWaterMill.class, "TileWaterMill");
		Core.proxy.registerCustomBlockStateLocation(waterMill, "machines/generators/water_mill");


		windMill = new BlockWindMill();
		registerBlock(windMill, "techreborn.windmill");
		GameRegistry.registerTileEntity(TileWindMill.class, "TileWindMill");
		GameRegistry.registerTileEntity(TileMachineBase.class, "TileMachineBaseTR");
		Core.proxy.registerCustomBlockStateLocation(windMill, "machines/generators/wind_mill");


		rubberLog = new BlockRubberLog();
		registerBlock(rubberLog, "rubberLog");

		rubberPlanks = new BlockRubberPlank();
		registerBlock(rubberPlanks, "rubberPlanks");

		rubberLeaves = new BlockRubberLeaves();
		registerBlock(rubberLeaves, "rubberLeaves");

		rubberSapling = new BlockRubberSapling();
		registerBlock(rubberSapling, ItemBlockRubberSapling.class, "rubberSapling");

		ironFence = new BlockIronFence();
		registerBlock(ironFence, "ironFence");

		reinforcedglass = new BlockReinforcedGlass(Material.GLASS);
		registerBlock(reinforcedglass, "reinforcedglass");

		recycler = new BlockRecycler(Material.IRON);
		registerBlock(recycler, "recycler");
		GameRegistry.registerTileEntity(TileRecycler.class, "TileRecyclerTR");
		Core.proxy.registerCustomBlockStateLocation(recycler, "machines/tier1_machines/recycler");


		batBox = new BlockBatBox();
		registerBlock(batBox, "batBox");
		GameRegistry.registerTileEntity(TileBatBox.class, "TileBatBox");
		Core.proxy.registerCustomBlockStateLocation(batBox, "machines/energy/lv_storage");


		mfe = new BlockMFE();
		registerBlock(mfe, "mfe");
		GameRegistry.registerTileEntity(TileMFE.class, "TileMFE");
		Core.proxy.registerCustomBlockStateLocation(mfe, "machines/energy/mv_storage");


		mfsu = new BlockMFSU();
		registerBlock(mfsu, "mfsu");
		GameRegistry.registerTileEntity(TileMFSU.class, "TileMFSU");
		Core.proxy.registerCustomBlockStateLocation(mfsu, "machines/energy/hv_storage");


		lvt = new BlockLVTransformer();
		registerBlock(lvt, "lvt");
		GameRegistry.registerTileEntity(TileLVTransformer.class, "TileLVTransformer");
		Core.proxy.registerCustomBlockStateLocation(lvt, "machines/energy/lv_transformer");


		mvt = new BlockMVTransformer();
		registerBlock(mvt, "mvt");
		GameRegistry.registerTileEntity(TileMVTransformer.class, "TileMVTransformer");
		Core.proxy.registerCustomBlockStateLocation(mvt, "machines/energy/mv_transformer");


		hvt = new BlockHVTransformer();
		registerBlock(hvt, "hvt");
		GameRegistry.registerTileEntity(TileHVTransformer.class, "TileHVTransformer");
		Core.proxy.registerCustomBlockStateLocation(hvt, "machines/energy/hv_transformer");


		pump = new BlockPump();
		registerBlock(pump, "pump");
		GameRegistry.registerTileEntity(TilePump.class, "TilePump");

		ironFurnace = new BlockIronFurnace();
		registerBlock(ironFurnace, "ironfurnace");
		GameRegistry.registerTileEntity(TileIronFurnace.class, "TileIronFurnaceTR");
		Core.proxy.registerCustomBlockStateLocation(hvt, "machines/tier0_machines/furnace");


		nuke = new BlockNuke();
		registerBlock(nuke, "nuke");
		Core.proxy.registerCustomBlockStateLocation(nuke, "nuke");


		scrapboxinator = new BlockScrapboxinator(Material.IRON);
		registerBlock(scrapboxinator, "scrapboxinator");
		GameRegistry.registerTileEntity(TileScrapboxinator.class, "TileScrapboxinatorTR");
		Core.proxy.registerCustomBlockStateLocation(scrapboxinator, "machines/tier1_machines/scrapboxinator");


		distributor = new BlockDistributor();
		registerBlock(distributor, "distributor");

		registerOreDict();
		Core.logHelper.info("TechReborns Blocks Loaded");
	}

	public static void registerBlock(Block block, String name){
		block.setRegistryName(name);
		GameRegistry.register(block);
		GameRegistry.register(new ItemBlock(block), block.getRegistryName());
	}

	public static void registerBlock(Block block, Class<? extends ItemBlock> itemclass, String name){
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

	public static void registerOreDict()
	{
		for(String ore : BlockOre.ores){
			OreDictionary.registerOre("ore" + StringUtils.toFirstCapital(ore), BlockOre.getOreByName(ore));
		}

		for(String ore : BlockOre2.ores){
			OreDictionary.registerOre("ore" + StringUtils.toFirstCapital(ore), BlockOre2.getOreByName(ore));
		}

		for(String name : ArrayUtils.addAll(BlockStorage.types, BlockStorage2.types)){
			OreDictionary.registerOre("block" + StringUtils.toFirstCapital(name), BlockStorage.getStorageBlockByName(name));
		}

		OreDictionary.registerOre("craftingPiston", Blocks.PISTON);
		OreDictionary.registerOre("craftingPiston", Blocks.STICKY_PISTON);
		OreDictionary.registerOre("crafterWood", Blocks.CRAFTING_TABLE);
		OreDictionary.registerOre("machineBasic", new ItemStack(machineframe, 1));

		OreDictionary.registerOre("treeSapling", rubberSapling);
		OreDictionary.registerOre("saplingRubber", rubberSapling);
		OreDictionary.registerOre("logWood", new ItemStack(rubberLog, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("logRubber", new ItemStack(rubberLog, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("plankWood", new ItemStack(rubberPlanks, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("plankRubber", new ItemStack(rubberPlanks, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("treeLeaves", new ItemStack(rubberLeaves, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("leavesRubber", new ItemStack(rubberLeaves, 1, OreDictionary.WILDCARD_VALUE));

		OreDictionary.registerOre("fenceIron", ironFence);
		
		OreDictionary.registerOre("machineBlockBasic", BlockMachineFrame.getFrameByName("machine", 1));
		OreDictionary.registerOre("machineBlockAdvanced", BlockMachineFrame.getFrameByName("advancedMachine", 1));
		OreDictionary.registerOre("machineBlockHighlyAdvanced", BlockMachineFrame.getFrameByName("highlyAdvancedMachine", 1));
	}

}

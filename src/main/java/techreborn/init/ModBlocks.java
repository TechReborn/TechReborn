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

	public static Block thermalGenerator;
	public static Block quantumTank;
	public static Block quantumChest;
	public static Block digitalChest;
	public static Block centrifuge;
	public static Block rollingMachine;
	public static Block machineCasing;
	public static Block blastFurnace;
	public static Block alloySmelter;
	public static Block industrialGrinder;
	public static Block implosionCompressor;
	public static Block matterFabricator;
	public static Block chunkLoader;
	public static Block dragonEggEnergySiphoner;
	public static Block magicEnergeyConverter;
	public static Block assemblyMachine;
	public static Block dieselGenerator;
	public static Block industrialElectrolyzer;
	public static Block magicalAbsorber;
	public static Block semifluidGenerator;
	public static Block gasTurbine;
	public static Block alloyFurnace;
	public static Block chemicalReactor;
	public static Block idsu;
	public static Block aesu;
	public static Block lesu;
	@Deprecated
	public static Block supercondensator;
	public static Block lesuStorage;
	public static Block distillationTower;
	@Deprecated
	public static Block electricCraftingTable;
	public static Block vacuumFreezer;
	public static Block plasmaGenerator;
	public static Block fusionControlComputer;
	@Deprecated
	public static Block computerCube;
	public static Block fusionCoil;
	public static Block lightningRod;
	@Deprecated
	public static Block heatGenerator;
	public static Block industrialSawmill;
	public static Block chargeBench;
	public static Block playerDetector;
	public static Block grinder;
	public static Block generator;
	public static Block compressor;
	public static Block extractor;
	public static Block electricFurnace;
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
	@Deprecated
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

	public static void init() {
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
		Core.proxy.registerCustomBlockStateLocation(centrifuge, "machines/tier2_machines/industrial_centrifuge");

		rollingMachine = new BlockRollingMachine(Material.ROCK);
		registerBlock(rollingMachine, "rollingmachine");
		GameRegistry.registerTileEntity(TileRollingMachine.class, "TileRollingMachineTR");
		Core.proxy.registerCustomBlockStateLocation(rollingMachine, "machines/tier1_machines/rolling_machine");

		blastFurnace = new BlockBlastFurnace(Material.ROCK);
		registerBlock(blastFurnace, "blastFurnace");
		GameRegistry.registerTileEntity(TileBlastFurnace.class, "TileBlastFurnaceTR");
		Core.proxy.registerCustomBlockStateLocation(blastFurnace, "machines/tier2_machines/industrial_blast_furnace");

		alloySmelter = new BlockAlloySmelter(Material.ROCK);
		registerBlock(alloySmelter, "alloySmelter");
		GameRegistry.registerTileEntity(TileAlloySmelter.class, "TileAlloySmalterTR");
		Core.proxy.registerCustomBlockStateLocation(alloySmelter, "machines/tier1_machines/electric_alloy_smelter");

		industrialGrinder = new BlockIndustrialGrinder(Material.ROCK);
		registerBlock(industrialGrinder, "grinder");
		GameRegistry.registerTileEntity(TileIndustrialGrinder.class, "TileIndustrialGrinderTR");
		Core.proxy.registerCustomBlockStateLocation(industrialGrinder, "machines/tier2_machines/industrial_grinder");

		implosionCompressor = new BlockImplosionCompressor(Material.ROCK);
		registerBlock(implosionCompressor, "implosioncompressor");
		GameRegistry.registerTileEntity(TileImplosionCompressor.class, "TileImplosionCompressorTR");
		Core.proxy.registerCustomBlockStateLocation(implosionCompressor, "machines/tier2_machines/implosion_compressor");

		matterFabricator = new BlockMatterFabricator(Material.ROCK);
		registerBlock(matterFabricator, "matterfabricator");
		GameRegistry.registerTileEntity(TileMatterFabricator.class, "TileMatterFabricatorTR");
		Core.proxy.registerCustomBlockStateLocation(matterFabricator, "machines/tier3_machines/matter_fabricator");

		chunkLoader = new BlockChunkLoader(Material.ROCK);
		registerBlock(chunkLoader, "chunkloader");
		GameRegistry.registerTileEntity(TileChunkLoader.class, "TileChunkLoaderTR");
		Core.proxy.registerCustomBlockStateLocation(chunkLoader, "machines/tier3_machines/industrial_chunk_loader");

		chargeBench = new BlockChargeBench(Material.ROCK);
		registerBlock(chargeBench, "chargebench");
		GameRegistry.registerTileEntity(TileChargeBench.class, "TileChargeBench");

		playerDetector = new BlockPlayerDetector();
		registerBlock(playerDetector, ItemBlockPlayerDetector.class, "playerDetector");
		GameRegistry.registerTileEntity(TilePlayerDectector.class, "TilePlayerDectectorTR");

		machineCasing = new BlockMachineCasing(Material.ROCK);
		registerBlock(machineCasing, ItemBlockMachineCasing.class, "machinecasing");
		GameRegistry.registerTileEntity(TileMachineCasing.class, "TileMachineCasingTR");

		ore = new BlockOre(Material.ROCK);
		registerBlock(ore, ItemBlockOre.class, "techreborn.ore");
		for (int i = 0; i < BlockOre.ores.length; i++) {
			Core.proxy.registerSubBlockInventoryLocation(ore, i, "storage/ores", BlockOre.ores[i]);
		}

		ore2 = new BlockOre2(Material.ROCK);
		registerBlock(ore2, ItemBlockOre2.class, "techreborn.ore2");
		for (int i = 0; i < BlockOre2.ores.length; i++) {
			Core.proxy.registerSubBlockInventoryLocation(ore2, i, "storage/ores", BlockOre2.ores[i]);
		}

		storage = new BlockStorage(Material.IRON);
		registerBlock(storage, ItemBlockStorage.class, "techreborn.storage");
		for (int i = 0; i < BlockStorage.types.length; i++) {
			Core.proxy.registerSubBlockInventoryLocation(storage, i, "storage/storage", BlockStorage.types[i]);
		}

		storage2 = new BlockStorage2(Material.IRON);
		registerBlock(storage2, ItemBlockStorage2.class, "techreborn.storage2");
		for (int i = 0; i < BlockStorage2.types.length; i++) {
			Core.proxy.registerSubBlockInventoryLocation(storage2, i, "storage/storage", BlockStorage2.types[i]);
		}

		dragonEggEnergySiphoner = new BlockDragonEggSiphoner(Material.ROCK);
		registerBlock(dragonEggEnergySiphoner, "dragoneggenergsiphon");
		GameRegistry.registerTileEntity(TileDragonEggSiphoner.class, "TileDragonEggSiphonerTR");
		Core.proxy.registerCustomBlockStateLocation(dragonEggEnergySiphoner, "machines/generators/dragon_egg_syphon");

		magicEnergeyConverter = new BlockMagicEnergyConverter(Material.ROCK);
		registerBlock(magicEnergeyConverter, "magicenergyconverter");
		Core.proxy.registerCustomBlockStateLocation(magicEnergeyConverter, "machines/generators/magic_energy_converter");

		assemblyMachine = new BlockAssemblingMachine(Material.ROCK);
		registerBlock(assemblyMachine, "assemblymachine");
		GameRegistry.registerTileEntity(TileAssemblingMachine.class, "TileAssemblyMachineTR");
		Core.proxy.registerCustomBlockStateLocation(assemblyMachine, "machines/tier1_machines/assembly_machine");

		dieselGenerator = new BlockDieselGenerator(Material.ROCK);
		registerBlock(dieselGenerator, "dieselgenerator");
		GameRegistry.registerTileEntity(TileDieselGenerator.class, "TileDieselGeneratorTR");
		Core.proxy.registerCustomBlockStateLocation(dieselGenerator, "machines/generators/diesel_generator");

		industrialElectrolyzer = new BlockIndustrialElectrolyzer(Material.ROCK);
		registerBlock(industrialElectrolyzer, "industrialelectrolyzer");
		GameRegistry.registerTileEntity(TileIndustrialElectrolyzer.class, "TileIndustrialElectrolyzerTR");
		Core.proxy.registerCustomBlockStateLocation(industrialElectrolyzer, "machines/tier1_machines/industrial_electrolyzer");

		magicalAbsorber = new BlockMagicEnergyAbsorber(Material.ROCK);
		registerBlock(magicalAbsorber, "magicrnergyabsorber");
		Core.proxy.registerCustomBlockStateLocation(magicalAbsorber, "machines/generators/magic_energy_absorber");

		semifluidGenerator = new BlockSemiFluidGenerator(Material.ROCK);
		registerBlock(semifluidGenerator, "semifluidgenerator");
		GameRegistry.registerTileEntity(TileSemifluidGenerator.class, "TileSemifluidGeneratorTR");
		Core.proxy.registerCustomBlockStateLocation(semifluidGenerator, "machines/generators/semi_fluid_generator");

		gasTurbine = new BlockGasTurbine(Material.ROCK);
		registerBlock(gasTurbine, "gasturbine");
		GameRegistry.registerTileEntity(TileGasTurbine.class, "TileGassTurbineTR");
		Core.proxy.registerCustomBlockStateLocation(gasTurbine, "machines/generators/gas_turbine");

		alloyFurnace = new BlockAlloyFurnace(Material.ROCK);
		registerBlock(alloyFurnace, "alloyfurnace");
		GameRegistry.registerTileEntity(TileAlloyFurnace.class, "TileAlloyFurnaceTR");
		Core.proxy.registerCustomBlockStateLocation(alloyFurnace, "machines/tier0_machines/alloy_furnace");

		chemicalReactor = new BlockChemicalReactor(Material.ROCK);
		registerBlock(chemicalReactor, "chemicalreactor");
		GameRegistry.registerTileEntity(TileChemicalReactor.class, "TileChemicalReactorTR");
		Core.proxy.registerCustomBlockStateLocation(chemicalReactor, "machines/tier1_machines/chemical_reactor");

		idsu = new BlockIDSU();
		registerBlock(idsu, "idsu");
		GameRegistry.registerTileEntity(TileIDSU.class, "TileIDSUTR");

		aesu = new BlockAESU();
		registerBlock(aesu, ItemBlockAesu.class, "aesu");
		GameRegistry.registerTileEntity(TileAesu.class, "TileAesuTR");

		lesu = new BlockLESU();
		registerBlock(lesu, "lesu");
		GameRegistry.registerTileEntity(TileLesu.class, "TileLesuTR");

		supercondensator = new BlockSupercondensator(Material.ROCK);
		registerBlock(supercondensator, "supercondensator");

		lesuStorage = new BlockLESUStorage(Material.ROCK);
		registerBlock(lesuStorage, "lesustorage");
		GameRegistry.registerTileEntity(TileLesuStorage.class, "TileLesuStorageTR");
		if(Core.proxy.isCTMAvailable()){
			Core.proxy.registerCustomBlockStateLocation(lesuStorage, "machines/energy/ev_multi_storage_ctm");
		} else {
			Core.proxy.registerCustomBlockStateLocation(lesuStorage, "machines/energy/ev_multi_storage");
		}

		distillationTower = new BlockDistillationTower(Material.ROCK);
		registerBlock(distillationTower, "distillationtower");
		Core.proxy.registerCustomBlockStateLocation(distillationTower, "machines/tier2_machines/distillation_tower");

		electricCraftingTable = new BlockElectricCraftingTable(Material.ROCK);
		registerBlock(electricCraftingTable, "electriccraftingtable");
		Core.proxy.registerCustomBlockStateLocation(electricCraftingTable, "machines/tier1_machines/electric_crafting_table");

		vacuumFreezer = new BlockVacuumFreezer(Material.ROCK);
		registerBlock(vacuumFreezer, "vacuumfreezer");
		GameRegistry.registerTileEntity(TileVacuumFreezer.class, "TileVacuumFreezerTR");
		Core.proxy.registerCustomBlockStateLocation(vacuumFreezer, "machines/tier2_machines/vacuum_freezer");

		plasmaGenerator = new BlockPlasmaGenerator(Material.ROCK);
		registerBlock(plasmaGenerator, "plasmagenerator");
		GameRegistry.registerTileEntity(TilePlasmaGenerator.class, "TilePlasmaGeneratorTR");

		computerCube = new BlockComputerCube(Material.ROCK);
		registerBlock(computerCube, "computercube");
		Core.proxy.registerCustomBlockStateLocation(computerCube, "machines/machines/tier2_machines/computer_cube");

		fusionControlComputer = new BlockFusionControlComputer(Material.ROCK);
		registerBlock(fusionControlComputer, "fusioncontrolcomputer");
		GameRegistry.registerTileEntity(TileEntityFusionController.class, "TileEntityFustionControllerTR");
		Core.proxy.registerCustomBlockStateLocation(fusionControlComputer, "machines/generators/fusion_reactor");

		fusionCoil = new BlockFusionCoil(Material.ROCK);
		registerBlock(fusionCoil, "fusioncoil");
		Core.proxy.registerCustomBlockStateLocation(fusionCoil, "machines/generators/fusion_coil");

		lightningRod = new BlockLightningRod(Material.ROCK);
		registerBlock(lightningRod, "lightningrod");
		GameRegistry.registerTileEntity(TileLightningRod.class, "TileLightningRodTR");
		Core.proxy.registerCustomBlockStateLocation(lightningRod, "machines/generators/lightning_rod");

		heatGenerator = new BlockHeatGenerator(Material.ROCK);
		registerBlock(heatGenerator, "heatgenerator");
		GameRegistry.registerTileEntity(TileHeatGenerator.class, "TileHeatGeneratorTR");

		industrialSawmill = new BlockIndustrialSawmill(Material.ROCK);
		registerBlock(industrialSawmill, "industrialSawmill");
		GameRegistry.registerTileEntity(TileIndustrialSawmill.class, "TileIndustrialSawmillTR");
		Core.proxy.registerCustomBlockStateLocation(industrialSawmill, "machines/tier2_machines/industrial_saw_mill");

		machineframe = new BlockMachineFrame(Material.IRON);
		registerBlock(machineframe, ItemBlockMachineFrame.class, "techreborn.machineFrame");

		grinder = new BlockGrinder(Material.IRON);
		registerBlock(grinder, "techreborn.grinder");
		GameRegistry.registerTileEntity(TileGrinder.class, "TileGrinderTR");
		Core.proxy.registerCustomBlockStateLocation(grinder, "machines/tier1_machines/grinder");

		generator = new BlockGenerator();
		registerBlock(generator, "techreborn.generator");
		GameRegistry.registerTileEntity(TileGenerator.class, "TileGeneratorTR");
		Core.proxy.registerCustomBlockStateLocation(generator, "machines/generators/generator");

		extractor = new BlockExtractor(Material.IRON);
		registerBlock(extractor, "techreborn.extractor");
		GameRegistry.registerTileEntity(TileExtractor.class, "TileExtractorTR");
		Core.proxy.registerCustomBlockStateLocation(extractor, "machines/tier1_machines/extractor");

		compressor = new BlockCompressor(Material.IRON);
		registerBlock(compressor, "techreborn.compressor");
		GameRegistry.registerTileEntity(TileCompressor.class, "TileCompressorTR");
		Core.proxy.registerCustomBlockStateLocation(compressor, "machines/tier1_machines/compressor");

		electricFurnace = new BlockElectricFurnace(Material.IRON);
		registerBlock(electricFurnace, "techreborn.electricfurnace");
		GameRegistry.registerTileEntity(TileElectricFurnace.class, "TileElectricFurnaceTR");
		Core.proxy.registerCustomBlockStateLocation(electricFurnace, "machines/tier1_machines/electric_furnace");

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
		Core.proxy.registerCustomBlockStateLocation(windMill, "machines/generators/wind_mill");

		GameRegistry.registerTileEntity(TileMachineBase.class, "TileMachineBaseTR");

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

		mfe = new BlockMFE();
		registerBlock(mfe, "mfe");
		GameRegistry.registerTileEntity(TileMFE.class, "TileMFE");

		mfsu = new BlockMFSU();
		registerBlock(mfsu, "mfsu");
		GameRegistry.registerTileEntity(TileMFSU.class, "TileMFSU");

		lvt = new BlockLVTransformer();
		registerBlock(lvt, "lvt");
		GameRegistry.registerTileEntity(TileLVTransformer.class, "TileLVTransformer");

		mvt = new BlockMVTransformer();
		registerBlock(mvt, "mvt");
		GameRegistry.registerTileEntity(TileMVTransformer.class, "TileMVTransformer");

		hvt = new BlockHVTransformer();
		registerBlock(hvt, "hvt");
		GameRegistry.registerTileEntity(TileHVTransformer.class, "TileHVTransformer");

		pump = new BlockPump();
		registerBlock(pump, "pump");
		GameRegistry.registerTileEntity(TilePump.class, "TilePump");


		ironFurnace = new BlockIronFurnace();
		registerBlock(ironFurnace, "ironfurnace");
		GameRegistry.registerTileEntity(TileIronFurnace.class, "TileIronFurnaceTR");
		Core.proxy.registerCustomBlockStateLocation(ironFurnace, "machines/tier0_machines/furnace");

		nuke = new BlockNuke();
		registerBlock(nuke, "nuke");

		scrapboxinator = new BlockScrapboxinator(Material.IRON);
		registerBlock(scrapboxinator, "scrapboxinator");
		GameRegistry.registerTileEntity(TileScrapboxinator.class, "TileScrapboxinatorTR");
		Core.proxy.registerCustomBlockStateLocation(scrapboxinator, "machines/tier1_machines/scrapboxinator");

		registerOreDict();
		Core.logHelper.info("TechReborns Blocks Loaded");
	}

	public static void registerBlock(Block block, String name) {
		block.setRegistryName(name);
		GameRegistry.register(block);
		GameRegistry.register(new ItemBlock(block), block.getRegistryName());
	}

	public static void registerBlock(Block block, Class<? extends ItemBlock> itemclass, String name) {
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

		OreDictionary.registerOre("blockSilver", new ItemStack(storage, 1, 0));
		OreDictionary.registerOre("blockAluminum", new ItemStack(storage, 1, 1));
		OreDictionary.registerOre("blockTitanium", new ItemStack(storage, 1, 2));
		OreDictionary.registerOre("blockChrome", new ItemStack(storage, 1, 3));
		OreDictionary.registerOre("blockSteel", new ItemStack(storage, 1, 4));
		OreDictionary.registerOre("blockBrass", new ItemStack(storage, 1, 5));
		OreDictionary.registerOre("blockLead", new ItemStack(storage, 1, 6));
		OreDictionary.registerOre("blockElectrum", new ItemStack(storage, 1, 7));
		OreDictionary.registerOre("blockZinc", new ItemStack(storage, 1, 8));
		OreDictionary.registerOre("blockPlatinum", new ItemStack(storage, 1, 9));
		OreDictionary.registerOre("blockTungsten", new ItemStack(storage, 1, 10));
		OreDictionary.registerOre("blockNickel", new ItemStack(storage, 1, 11));
		OreDictionary.registerOre("blockInvar", new ItemStack(storage, 1, 12));
		//OreDictionary.registerOre("blockOsmium", new ItemStack(storage, 1, 13));	No osmium, stolen by Mekanism
		OreDictionary.registerOre("blockIridium", new ItemStack(storage, 1, 13));
		OreDictionary.registerOre("blockCopper", BlockStorage2.getStorageBlockByName("copper", 1));
		OreDictionary.registerOre("blockTin", BlockStorage2.getStorageBlockByName("tin", 1));

		OreDictionary.registerOre("blockTungstensteel", new ItemStack(storage2, 1, 0));
		OreDictionary.registerOre("blockLodestone", new ItemStack(storage2, 1, 1));
		OreDictionary.registerOre("blockTellurium", new ItemStack(storage2, 1, 2));
		OreDictionary.registerOre("blockRuby", new ItemStack(storage2, 1, 5));
		OreDictionary.registerOre("blockSapphire", new ItemStack(storage2, 1, 6));
		OreDictionary.registerOre("blockPeridot", new ItemStack(storage2, 1, 7));
		OreDictionary.registerOre("blockYellowGarnet", new ItemStack(storage2, 1, 8));
		OreDictionary.registerOre("blockRedGarnet", new ItemStack(storage2, 1, 9));

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

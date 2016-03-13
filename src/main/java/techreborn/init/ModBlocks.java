package techreborn.init;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import reborncore.common.tile.TileMachineBase;
import techreborn.Core;
import techreborn.blocks.*;
import techreborn.blocks.generator.BlockDieselGenerator;
import techreborn.blocks.generator.BlockDragonEggSiphoner;
import techreborn.blocks.generator.BlockGasTurbine;
import techreborn.blocks.generator.BlockGenerator;
import techreborn.blocks.generator.BlockHeatGenerator;
import techreborn.blocks.generator.BlockLightningRod;
import techreborn.blocks.generator.BlockMagicEnergyAbsorber;
import techreborn.blocks.generator.BlockMagicEnergyConverter;
import techreborn.blocks.generator.BlockPlasmaGenerator;
import techreborn.blocks.generator.BlockSemiFluidGenerator;
import techreborn.blocks.generator.BlockSolarPanel;
import techreborn.blocks.generator.BlockThermalGenerator;
import techreborn.blocks.generator.BlockWaterMill;
import techreborn.blocks.generator.BlockWindMill;
import techreborn.blocks.machine.BlockAlloyFurnace;
import techreborn.blocks.machine.BlockAlloySmelter;
import techreborn.blocks.machine.BlockAssemblingMachine;
import techreborn.blocks.machine.BlockBlastFurnace;
import techreborn.blocks.machine.BlockCentrifuge;
import techreborn.blocks.machine.BlockChargeBench;
import techreborn.blocks.machine.BlockChemicalReactor;
import techreborn.blocks.machine.BlockDistillationTower;
import techreborn.blocks.machine.BlockImplosionCompressor;
import techreborn.blocks.machine.BlockIndustrialElectrolyzer;
import techreborn.blocks.machine.BlockIndustrialGrinder;
import techreborn.blocks.machine.BlockIndustrialSawmill;
import techreborn.blocks.machine.BlockMatterFabricator;
import techreborn.blocks.machine.BlockRollingMachine;
import techreborn.blocks.machine.BlockVacuumFreezer;
import techreborn.blocks.storage.BlockAesu;
import techreborn.blocks.storage.BlockIDSU;
import techreborn.blocks.storage.BlockLesu;
import techreborn.blocks.storage.BlockLesuStorage;
import techreborn.blocks.teir1.BlockCompressor;
import techreborn.blocks.teir1.BlockElectricFurnace;
import techreborn.blocks.teir1.BlockExtractor;
import techreborn.blocks.teir1.BlockGrinder;
import techreborn.blocks.teir1.BlockRecycler;
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
import techreborn.tiles.generator.*;
import techreborn.tiles.idsu.TileIDSU;
import techreborn.tiles.lesu.TileLesu;
import techreborn.tiles.lesu.TileLesuStorage;
import techreborn.tiles.teir1.TileCompressor;
import techreborn.tiles.teir1.TileElectricFurnace;
import techreborn.tiles.teir1.TileExtractor;
import techreborn.tiles.teir1.TileGrinder;
import techreborn.tiles.teir1.TileRecycler;

public class ModBlocks {

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
    public static Block HighAdvancedMachineBlock;
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
    public static Block heatGenerator;
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
        GameRegistry.registerBlock(thermalGenerator, "techreborn.thermalGenerator");
        GameRegistry.registerTileEntity(TileThermalGenerator.class, "TileThermalGeneratorTR");

        quantumTank = new BlockQuantumTank();
        GameRegistry.registerBlock(quantumTank, ItemBlockQuantumTank.class, "techreborn.quantumTank");
        GameRegistry.registerTileEntity(TileQuantumTank.class, "TileQuantumTankTR");

        quantumChest = new BlockQuantumChest();
        GameRegistry.registerBlock(quantumChest, ItemBlockQuantumChest.class, "techreborn.quantumChest");
        GameRegistry.registerTileEntity(TileQuantumChest.class, "TileQuantumChestTR");

        digitalChest = new BlockDigitalChest();
        GameRegistry.registerBlock(digitalChest, ItemBlockDigitalChest.class, "techreborn.digitalChest");
        GameRegistry.registerTileEntity(TileDigitalChest.class, "TileDigitalChestTR");

        centrifuge = new BlockCentrifuge();
        GameRegistry.registerBlock(centrifuge, "techreborn.centrifuge");
        GameRegistry.registerTileEntity(TileCentrifuge.class, "TileCentrifugeTR");

        RollingMachine = new BlockRollingMachine(Material.rock);
        GameRegistry.registerBlock(RollingMachine, "rollingmachine");
        GameRegistry.registerTileEntity(TileRollingMachine.class, "TileRollingMachineTR");

        BlastFurnace = new BlockBlastFurnace(Material.rock);
        GameRegistry.registerBlock(BlastFurnace, "blastFurnace");
        GameRegistry.registerTileEntity(TileBlastFurnace.class, "TileBlastFurnaceTR");

        AlloySmelter = new BlockAlloySmelter(Material.rock);
        GameRegistry.registerBlock(AlloySmelter, "alloySmelter");
        GameRegistry.registerTileEntity(TileAlloySmelter.class, "TileAlloySmalterTR");

        IndustrialGrinder = new BlockIndustrialGrinder(Material.rock);
        GameRegistry.registerBlock(IndustrialGrinder, "grinder");
        GameRegistry.registerTileEntity(TileIndustrialGrinder.class, "TileIndustrialGrinderTR");

        ImplosionCompressor = new BlockImplosionCompressor(Material.rock);
        GameRegistry.registerBlock(ImplosionCompressor, "implosioncompressor");
        GameRegistry.registerTileEntity(TileImplosionCompressor.class, "TileImplosionCompressorTR");

        MatterFabricator = new BlockMatterFabricator(Material.rock);
        GameRegistry.registerBlock(MatterFabricator, "matterfabricator");
        GameRegistry.registerTileEntity(TileMatterFabricator.class, "TileMatterFabricatorTR");

        ChunkLoader = new BlockChunkLoader(Material.rock);
        GameRegistry.registerBlock(ChunkLoader, "chunkloader");
        GameRegistry.registerTileEntity(TileChunkLoader.class, "TileChunkLoaderTR");

        chargeBench = new BlockChargeBench(Material.rock);
        GameRegistry.registerBlock(chargeBench, "chargebench");
        GameRegistry.registerTileEntity(TileChargeBench.class, "TileChargeBench");

        playerDetector = new BlockPlayerDetector();
        GameRegistry.registerBlock(playerDetector, ItemBlockPlayerDetector.class, "playerDetector");
        GameRegistry.registerTileEntity(TilePlayerDectector.class, "TilePlayerDectectorTR");

        MachineCasing = new BlockMachineCasing(Material.rock);
        GameRegistry.registerBlock(MachineCasing, ItemBlockMachineCasing.class, "machinecasing");
        GameRegistry.registerTileEntity(TileMachineCasing.class, "TileMachineCasingTR");

        ore = new BlockOre(Material.rock);
        GameRegistry.registerBlock(ore, ItemBlockOre.class, "techreborn.ore");
        
        ore2 = new BlockOre2(Material.rock);
        GameRegistry.registerBlock(ore2, ItemBlockOre2.class, "techreborn.ore2");

        storage = new BlockStorage(Material.iron);
        GameRegistry.registerBlock(storage, ItemBlockStorage.class, "techreborn.storage");

        storage2 = new BlockStorage2(Material.iron);
        GameRegistry.registerBlock(storage2, ItemBlockStorage2.class, "techreborn.storage2");

        HighAdvancedMachineBlock = new BlockHighlyAdvancedMachine(Material.rock);
        GameRegistry.registerBlock(HighAdvancedMachineBlock, "highlyadvancedmachine");

        Dragoneggenergysiphoner = new BlockDragonEggSiphoner(Material.rock);
        GameRegistry.registerBlock(Dragoneggenergysiphoner, "dragoneggenergsiphon");
        GameRegistry.registerTileEntity(TileDragonEggSiphoner.class, "TileDragonEggSiphonerTR");

        Magicenergeyconverter = new BlockMagicEnergyConverter(Material.rock);
        GameRegistry.registerBlock(Magicenergeyconverter, "magicenergyconverter");

        AssemblyMachine = new BlockAssemblingMachine(Material.rock);
        GameRegistry.registerBlock(AssemblyMachine, "assemblymachine");
        GameRegistry.registerTileEntity(TileAssemblingMachine.class, "TileAssemblyMachineTR");

        DieselGenerator = new BlockDieselGenerator(Material.rock);
        GameRegistry.registerBlock(DieselGenerator, "dieselgenerator");
        GameRegistry.registerTileEntity(TileDieselGenerator.class, "TileDieselGeneratorTR");

        IndustrialElectrolyzer = new BlockIndustrialElectrolyzer(Material.rock);
        GameRegistry.registerBlock(IndustrialElectrolyzer, "industrialelectrolyzer");
        GameRegistry.registerTileEntity(TileIndustrialElectrolyzer.class, "TileIndustrialElectrolyzerTR");

        MagicalAbsorber = new BlockMagicEnergyAbsorber(Material.rock);
        GameRegistry.registerBlock(MagicalAbsorber, "magicrnergyabsorber");

        Semifluidgenerator = new BlockSemiFluidGenerator(Material.rock);
        GameRegistry.registerBlock(Semifluidgenerator, "semifluidgenerator");
        GameRegistry.registerTileEntity(TileSemifluidGenerator.class, "TileSemifluidGeneratorTR");

        Gasturbine = new BlockGasTurbine(Material.rock);
        GameRegistry.registerBlock(Gasturbine, "gasturbine");
        GameRegistry.registerTileEntity(TileGasTurbine.class, "TileGassTurbineTR");

        AlloyFurnace = new BlockAlloyFurnace(Material.rock);
        GameRegistry.registerBlock(AlloyFurnace, "alloyfurnace");
        GameRegistry.registerTileEntity(TileAlloyFurnace.class, "TileAlloyFurnaceTR");

        ChemicalReactor = new BlockChemicalReactor(Material.rock);
        GameRegistry.registerBlock(ChemicalReactor, "chemicalreactor");
        GameRegistry.registerTileEntity(TileChemicalReactor.class, "TileChemicalReactorTR");

        Idsu = new BlockIDSU(Material.rock);
        GameRegistry.registerBlock(Idsu, "idsu");
        GameRegistry.registerTileEntity(TileIDSU.class, "TileIDSUTR");

        Aesu = new BlockAesu(Material.rock);
        GameRegistry.registerBlock(Aesu, ItemBlockAesu.class, "aesu");
        GameRegistry.registerTileEntity(TileAesu.class, "TileAesuTR");

        Lesu = new BlockLesu(Material.rock);
        GameRegistry.registerBlock(Lesu, "lesu");
        GameRegistry.registerTileEntity(TileLesu.class, "TileLesuTR");

        Supercondensator = new BlockSupercondensator(Material.rock);
        GameRegistry.registerBlock(Supercondensator, "supercondensator");

        LesuStorage = new BlockLesuStorage(Material.rock);
        GameRegistry.registerBlock(LesuStorage, "lesustorage");
        GameRegistry.registerTileEntity(TileLesuStorage.class, "TileLesuStorageTR");

        Distillationtower = new BlockDistillationTower(Material.rock);
        GameRegistry.registerBlock(Distillationtower, "distillationtower");

        ElectricCraftingTable = new BlockElectricCraftingTable(Material.rock);
        GameRegistry.registerBlock(ElectricCraftingTable, "electriccraftingtable");

        VacuumFreezer = new BlockVacuumFreezer(Material.rock);
        GameRegistry.registerBlock(VacuumFreezer, "vacuumfreezer");
        GameRegistry.registerTileEntity(TileVacuumFreezer.class, "TileVacuumFreezerTR");

        PlasmaGenerator = new BlockPlasmaGenerator(Material.rock);
        GameRegistry.registerBlock(PlasmaGenerator, "plasmagenerator");

        ComputerCube = new BlockComputerCube(Material.rock);
        GameRegistry.registerBlock(ComputerCube, "computercube");

        FusionControlComputer = new BlockFusionControlComputer(Material.rock);
        GameRegistry.registerBlock(FusionControlComputer, "fusioncontrolcomputer");
        GameRegistry.registerTileEntity(TileEntityFusionController.class, "TileEntityFustionControllerTR");

        FusionCoil = new BlockFusionCoil(Material.rock);
        GameRegistry.registerBlock(FusionCoil, "fusioncoil");

        LightningRod = new BlockLightningRod(Material.rock);
        GameRegistry.registerBlock(LightningRod, "lightningrod");

        heatGenerator = new BlockHeatGenerator(Material.rock);
        GameRegistry.registerBlock(heatGenerator, "heatgenerator");
        GameRegistry.registerTileEntity(TileHeatGenerator.class, "TileHeatGeneratorTR");

        industrialSawmill = new BlockIndustrialSawmill(Material.rock);
        GameRegistry.registerBlock(industrialSawmill, "industrialSawmill");
        GameRegistry.registerTileEntity(TileIndustrialSawmill.class, "TileIndustrialSawmillTR");

        machineframe = new BlockMachineFrame(Material.iron);
        GameRegistry.registerBlock(machineframe, ItemBlockMachineFrame.class, "techreborn.machineFrame");
        
        Grinder = new BlockGrinder(Material.iron);
        GameRegistry.registerBlock(Grinder, "techreborn.grinder");
        GameRegistry.registerTileEntity(TileGrinder.class, "TileGrinderTR");
        
        Generator = new BlockGenerator();
        GameRegistry.registerBlock(Generator, "techreborn.generator");
        GameRegistry.registerTileEntity(TileGenerator.class, "TileGeneratorTR");
        
        Extractor = new BlockExtractor(Material.iron);
        GameRegistry.registerBlock(Extractor, "techreborn.extractor");
        GameRegistry.registerTileEntity(TileExtractor.class, "TileExtractorTR");
        
        Compressor = new BlockCompressor(Material.iron);
        GameRegistry.registerBlock(Compressor, "techreborn.compressor");
        GameRegistry.registerTileEntity(TileCompressor.class, "TileCompressorTR");
        
        ElectricFurnace = new BlockElectricFurnace(Material.iron);
        GameRegistry.registerBlock(ElectricFurnace, "techreborn.electricfurnace");
        GameRegistry.registerTileEntity(TileElectricFurnace.class, "TileElectricFurnaceTR");

        solarPanel = new BlockSolarPanel();
        GameRegistry.registerBlock(solarPanel, "techreborn.solarpanel");
        GameRegistry.registerTileEntity(TileSolarPanel.class, "TileSolarPanel");

        waterMill = new BlockWaterMill();
        GameRegistry.registerBlock(waterMill, "techreborn.watermill");
        GameRegistry.registerTileEntity(TileWaterMill.class, "TileWaterMill");

        windMill = new BlockWindMill();
        GameRegistry.registerBlock(windMill, "techreborn.windmill");
        GameRegistry.registerTileEntity(TileWindMill.class, "TileWindMill");

        GameRegistry.registerTileEntity(TileMachineBase.class, "TileMachineBaseTR");

        rubberLog = new BlockRubberLog();
        GameRegistry.registerBlock(rubberLog, "rubberLog");

        rubberPlanks = new BlockRubberPlank();
        GameRegistry.registerBlock(rubberPlanks, "rubberPlanks");

        rubberLeaves = new BlockRubberLeaves();
        GameRegistry.registerBlock(rubberLeaves, "rubberLeaves");

        rubberSapling = new BlockRubberSapling();
        GameRegistry.registerBlock(rubberSapling, ItemBlockRubberSapling.class, "rubberSapling");

        ironFence = new BlockIronFence();
        GameRegistry.registerBlock(ironFence, "ironFence");
        
        reinforcedglass = new BlockReinforcedGlass(Material.glass);
        GameRegistry.registerBlock(reinforcedglass, "reinforcedglass");
        
        recycler = new BlockRecycler(Material.iron);
        GameRegistry.registerBlock(recycler, "recycler");
        GameRegistry.registerTileEntity(TileRecycler.class, "TileRecyclerTR");
        
        ironFurnace = new BlockIronFurnace();
        GameRegistry.registerBlock(ironFurnace, "ironfurnace");
        GameRegistry.registerTileEntity(TileIronFurnace.class, "TileIronFurnaceTR");

        nuke = new BlockNuke();
        GameRegistry.registerBlock(nuke, "nuke");

        registerOreDict();
        Core.logHelper.info("TechReborns Blocks Loaded");
    }

    public static void registerOreDict() {
        OreDictionary.registerOre("oreGalena", new ItemStack(ore, 1, 0));
        OreDictionary.registerOre("oreIridium", new ItemStack(ore, 1, 1));
        OreDictionary.registerOre("oreRuby", new ItemStack(ore, 1, 2));
        OreDictionary.registerOre("oreSapphire", new ItemStack(ore, 1, 3));
        OreDictionary.registerOre("oreBauxite", new ItemStack(ore, 1, 4));
        OreDictionary.registerOre("orePyrite", new ItemStack(ore, 1, 5));
        OreDictionary.registerOre("oreCinnabar", new ItemStack(ore, 1, 6));
        OreDictionary.registerOre("oreSphalerite", new ItemStack(ore, 1, 7));
        OreDictionary.registerOre("oreTungsten", new ItemStack(ore, 1, 8));
        OreDictionary.registerOre("oreSheldonite", new ItemStack(ore, 1, 9));
        OreDictionary.registerOre("orePlatinum", new ItemStack(ore, 1, 9));
        OreDictionary.registerOre("orePeridot", new ItemStack(ore, 1, 10));
        OreDictionary.registerOre("oreSodalite", new ItemStack(ore, 1, 11));
        OreDictionary.registerOre("oreTetrahedrite", new ItemStack(ore, 1, 12));
        OreDictionary.registerOre("oreTin", new ItemStack(ore, 1, 13));
        OreDictionary.registerOre("oreLead", new ItemStack(ore, 1, 14));
        OreDictionary.registerOre("oreSilver", new ItemStack(ore, 1, 15));

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
        OreDictionary.registerOre("blockOsmium", new ItemStack(storage, 1, 13));
        OreDictionary.registerOre("blockIridium", new ItemStack(storage, 1, 14));

        OreDictionary.registerOre("blockTungstensteel", new ItemStack(storage2, 1, 0));
        OreDictionary.registerOre("blockLodestone", new ItemStack(storage2, 1, 1));
        OreDictionary.registerOre("blockTellurium", new ItemStack(storage2, 1, 2));
        OreDictionary.registerOre("blockRuby", new ItemStack(storage2, 1, 5));
        OreDictionary.registerOre("blockSapphire", new ItemStack(storage2, 1, 6));
        OreDictionary.registerOre("blockPeridot", new ItemStack(storage2, 1, 7));
        OreDictionary.registerOre("blockYellowGarnet", new ItemStack(storage2, 1, 8));
        OreDictionary.registerOre("blockRedGarnet", new ItemStack(storage2, 1, 9));


        OreDictionary.registerOre("craftingPiston", Blocks.piston);
        OreDictionary.registerOre("craftingPiston", Blocks.sticky_piston);
        OreDictionary.registerOre("crafterWood", Blocks.crafting_table);
        OreDictionary.registerOre("machineBasic", new ItemStack(machineframe, 1));

        OreDictionary.registerOre("treeSapling", rubberSapling);
        OreDictionary.registerOre("saplingRubber", rubberSapling);
        OreDictionary.registerOre("logWood", new ItemStack(rubberLog, 1 , OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("logRubber", new ItemStack(rubberLog, 1 , OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("plankWood", new ItemStack(rubberPlanks, 1 , OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("plankRubber", new ItemStack(rubberPlanks, 1 , OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("treeLeaves", new ItemStack(rubberLeaves, 1 , OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("leavesRubber", new ItemStack(rubberLeaves, 1 , OreDictionary.WILDCARD_VALUE));

        OreDictionary.registerOre("fenceIron", ironFence);
    }

}

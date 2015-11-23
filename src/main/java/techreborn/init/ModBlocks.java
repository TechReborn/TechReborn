package techreborn.init;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import techreborn.Core;
import techreborn.blocks.*;
import techreborn.blocks.generator.*;
import techreborn.blocks.machine.*;
import techreborn.blocks.storage.BlockAesu;
import techreborn.blocks.storage.BlockIDSU;
import techreborn.blocks.storage.BlockLesu;
import techreborn.blocks.storage.BlockLesuStorage;
import techreborn.itemblocks.*;
import techreborn.tiles.*;
import techreborn.tiles.fusionReactor.TileEntityFusionController;
import techreborn.tiles.idsu.TileIDSU;
import techreborn.tiles.lesu.TileLesu;
import techreborn.tiles.lesu.TileLesuStorage;

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
    public static Block Grinder;
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

    public static Block ore;
    public static Block storage;
    public static Block storage2;
    public static Block machineframe;

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

        Grinder = new BlockGrinder(Material.rock);
        GameRegistry.registerBlock(Grinder, "grinder");
        GameRegistry.registerTileEntity(TileGrinder.class, "TileGrinderTR");

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


        GameRegistry.registerTileEntity(TileMachineBase.class, "TileMachineBaseTR");

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

    }

}

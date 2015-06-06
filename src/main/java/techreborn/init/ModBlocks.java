package techreborn.init;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import techreborn.blocks.*;
import techreborn.blocks.generator.BlockDieselGenerator;
import techreborn.blocks.generator.BlockDragonEggSiphoner;
import techreborn.blocks.generator.BlockHeatGenerator;
import techreborn.blocks.generator.BlockLightningRod;
import techreborn.blocks.generator.BlockMagicEnergyAbsorber;
import techreborn.blocks.generator.BlockMagicEnergyConverter;
import techreborn.blocks.generator.BlockPlasmaGenerator;
import techreborn.blocks.generator.BlockSemiFluidGenerator;
import techreborn.blocks.generator.BlockThermalGenerator;
import techreborn.blocks.generator.BlockGasTurbine;
import techreborn.blocks.machine.BlockAlloyFurnace;
import techreborn.blocks.machine.BlockAlloySmelter;
import techreborn.blocks.machine.BlockAssemblingMachine;
import techreborn.blocks.machine.BlockBlastFurnace;
import techreborn.blocks.machine.BlockCentrifuge;
import techreborn.blocks.machine.BlockChemicalReactor;
import techreborn.blocks.machine.BlockDistillationTower;
import techreborn.blocks.machine.BlockGrinder;
import techreborn.blocks.machine.BlockImplosionCompressor;
import techreborn.blocks.machine.BlockIndustrialElectrolyzer;
import techreborn.blocks.machine.BlockIndustrialSawmill;
import techreborn.blocks.machine.BlockLathe;
import techreborn.blocks.machine.BlockMatterFabricator;
import techreborn.blocks.machine.BlockPlateCuttingMachine;
import techreborn.blocks.machine.BlockRollingMachine;
import techreborn.blocks.machine.BlockVacuumFreezer;
import techreborn.blocks.storage.BlockAesu;
import techreborn.blocks.storage.BlockIDSU;
import techreborn.blocks.storage.BlockLesu;
import techreborn.blocks.storage.BlockLesuStorage;
import techreborn.itemblocks.*;
import techreborn.tiles.TileAesu;
import techreborn.tiles.TileAlloyFurnace;
import techreborn.tiles.TileAlloySmelter;
import techreborn.tiles.TileAssemblingMachine;
import techreborn.tiles.TileBlastFurnace;
import techreborn.tiles.TileCentrifuge;
import techreborn.tiles.TileChemicalReactor;
import techreborn.tiles.TileChunkLoader;
import techreborn.tiles.TileDieselGenerator;
import techreborn.tiles.TileDragonEggSiphoner;
import techreborn.tiles.TileGrinder;
import techreborn.tiles.TileHeatGenerator;
import techreborn.tiles.TileImplosionCompressor;
import techreborn.tiles.TileIndustrialElectrolyzer;
import techreborn.tiles.TileIndustrialSawmill;
import techreborn.tiles.TileLathe;
import techreborn.tiles.TileMachineCasing;
import techreborn.tiles.TileMatterFabricator;
import techreborn.tiles.TilePlateCuttingMachine;
import techreborn.tiles.TileQuantumChest;
import techreborn.tiles.TileQuantumTank;
import techreborn.tiles.TileRollingMachine;
import techreborn.tiles.TileThermalGenerator;
import techreborn.tiles.TileSemifluidGenerator;
import techreborn.tiles.TileGasTurbine;
import techreborn.util.LogHelper;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModBlocks {

	public static Block thermalGenerator;
	public static Block quantumTank;
	public static Block quantumChest;
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
	public static Block lathe;
	public static Block platecuttingmachine;
	public static Block Idsu;
	public static Block Aesu;
	public static Block Lesu;
	public static Block Supercondensator;
	public static Block Woodenshelf;
	public static Block Metalshelf;
	public static Block LesuStorage;
	public static Block Distillationtower;
	public static Block ElectricCraftingTable;
	public static Block VacuumFreezer;
	public static Block PlasmaGenerator;
	public static Block FusionControlComputer;
	public static Block FusionCoil;
	public static Block LightningRod;
	public static Block heatGenerator;
	public static Block industrialSawmill;	
	
	public static Block ore;
	public static Block storage;
    public static Block storage2;
	public static Block machineframe;

	public static void init(){
		thermalGenerator = new BlockThermalGenerator();
		GameRegistry.registerBlock(thermalGenerator, "techreborn.thermalGenerator");
		GameRegistry.registerTileEntity(TileThermalGenerator.class, "TileThermalGeneratorTR");

		quantumTank = new BlockQuantumTank();
		GameRegistry.registerBlock(quantumTank, ItemBlockQuantumTank.class, "techreborn.quantumTank");
		GameRegistry.registerTileEntity(TileQuantumTank.class, "TileQuantumTankTR");

		quantumChest = new BlockQuantumChest();
		GameRegistry.registerBlock(quantumChest, ItemBlockQuantumChest.class, "techreborn.quantumChest");
		GameRegistry.registerTileEntity(TileQuantumChest.class, "TileQuantumChestTR");

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
		GameRegistry.registerTileEntity(TileGrinder.class, "TileGrinder");
		
		ImplosionCompressor = new BlockImplosionCompressor(Material.rock);
		GameRegistry.registerBlock(ImplosionCompressor, "implosioncompressor");
		GameRegistry.registerTileEntity(TileImplosionCompressor.class, "TileImplosionCompressorTR");
		
		MatterFabricator = new BlockMatterFabricator(Material.rock);
		GameRegistry.registerBlock(MatterFabricator, "matterfabricator");
		GameRegistry.registerTileEntity(TileMatterFabricator.class, "TileMatterFabricatorTR");
		
		ChunkLoader = new BlockChunkLoader(Material.rock);
		GameRegistry.registerBlock(ChunkLoader, "chunkloader");
		GameRegistry.registerTileEntity(TileChunkLoader.class, "TileChunkLoaderTR");

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
		GameRegistry.registerTileEntity(TileDieselGenerator.class, "TileDieselGenerator");
		
		IndustrialElectrolyzer = new BlockIndustrialElectrolyzer(Material.rock);
		GameRegistry.registerBlock(IndustrialElectrolyzer, "industrialelectrolyzer");
		GameRegistry.registerTileEntity(TileIndustrialElectrolyzer.class, "TileIndustrialElectrolyzerTR");
		
		MagicalAbsorber = new BlockMagicEnergyAbsorber(Material.rock);
		GameRegistry.registerBlock(MagicalAbsorber, "magicrnergyabsorber");
		
		Semifluidgenerator = new BlockSemiFluidGenerator(Material.rock);
		GameRegistry.registerBlock(Semifluidgenerator, "semifluidgenerator");
		GameRegistry.registerTileEntity(TileSemifluidGenerator.class, "TileSemifluidGenerator");
		
		Gasturbine = new BlockGasTurbine(Material.rock);
		GameRegistry.registerBlock(Gasturbine, "gasturbine");
		GameRegistry.registerTileEntity(TileGasTurbine.class, "TileGassTurbine");
		
		AlloyFurnace = new BlockAlloyFurnace(Material.rock);
		GameRegistry.registerBlock(AlloyFurnace, "alloyfurnace");
		GameRegistry.registerTileEntity(TileAlloyFurnace.class, "TileAlloyFurnace");
		
		ChemicalReactor = new BlockChemicalReactor(Material.rock);
		GameRegistry.registerBlock(ChemicalReactor, "chemicalreactor");
		GameRegistry.registerTileEntity(TileChemicalReactor.class, "TileChemicalReactorTR");
		
		lathe = new BlockLathe(Material.rock);
		GameRegistry.registerBlock(lathe, "lathe");
		GameRegistry.registerTileEntity(TileLathe.class, "TileLathe");
		
		platecuttingmachine = new BlockPlateCuttingMachine(Material.rock);
		GameRegistry.registerBlock(platecuttingmachine, "platecuttingmachine");
		GameRegistry.registerTileEntity(TilePlateCuttingMachine.class, "TilePlateCuttingMachineTR");
		
		Idsu = new BlockIDSU(Material.rock);
		GameRegistry.registerBlock(Idsu, "idsu");
		
		Aesu = new BlockAesu(Material.rock);
		GameRegistry.registerBlock(Aesu, "aesu");
		GameRegistry.registerTileEntity(TileAesu.class, "TileAesuTR");
		
		Lesu = new BlockLesu(Material.rock);
		GameRegistry.registerBlock(Lesu, "lesu");
		
		Supercondensator = new BlockSupercondensator(Material.rock);
		GameRegistry.registerBlock(Supercondensator, "supercondensator");
		
		Woodenshelf = new BlockWoodenshelf(Material.wood);
		GameRegistry.registerBlock(Woodenshelf, "woodenshelf");
		
		Metalshelf = new BlockMetalShelf(Material.rock);
		GameRegistry.registerBlock(Metalshelf, "metalshelf");
		
		LesuStorage = new BlockLesuStorage(Material.rock);
		GameRegistry.registerBlock(LesuStorage, "lesustorage");
		
		Distillationtower = new BlockDistillationTower(Material.rock);
		GameRegistry.registerBlock(Distillationtower, "distillationtower");
		
		ElectricCraftingTable = new BlockElectricCraftingTable(Material.rock);
		GameRegistry.registerBlock(ElectricCraftingTable, "electriccraftingtable");
		
		VacuumFreezer = new BlockVacuumFreezer(Material.rock);
		GameRegistry.registerBlock(VacuumFreezer, "vacuumfreezer");
		
		PlasmaGenerator = new BlockPlasmaGenerator(Material.rock);
		GameRegistry.registerBlock(PlasmaGenerator, "plasmagenerator");
		
		FusionControlComputer = new BlockFusionControlComputer(Material.rock);
		GameRegistry.registerBlock(FusionControlComputer, "fusioncontrolcomputer");
		
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
		LogHelper.info("TechReborns Blocks Loaded");


		registerOreDict();
	}

	public static void registerOreDict(){
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

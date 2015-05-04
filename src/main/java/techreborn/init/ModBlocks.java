package techreborn.init;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import techreborn.blocks.BlockAesu;
import techreborn.blocks.BlockAlloyFurnace;
import techreborn.blocks.BlockAlloySmelter;
import techreborn.blocks.BlockAssemblingMachine;
import techreborn.blocks.BlockBlastFurnace;
import techreborn.blocks.BlockCentrifuge;
import techreborn.blocks.BlockChemicalReactor;
import techreborn.blocks.BlockChunkLoader;
import techreborn.blocks.BlockDieselGenerator;
import techreborn.blocks.BlockDistillationTower;
import techreborn.blocks.BlockDragonEggSiphoner;
import techreborn.blocks.BlockElectricCraftingTable;
import techreborn.blocks.BlockGrinder;
import techreborn.blocks.BlockHighlyAdvancedMachine;
import techreborn.blocks.BlockIDSU;
import techreborn.blocks.BlockImplosionCompressor;
import techreborn.blocks.BlockIndustrialElectrolyzer;
import techreborn.blocks.BlockLathe;
import techreborn.blocks.BlockLesu;
import techreborn.blocks.BlockLesuStorage;
import techreborn.blocks.BlockMachineCasing;
import techreborn.blocks.BlockMagicEnergyAbsorber;
import techreborn.blocks.BlockMagicEnergyConverter;
import techreborn.blocks.BlockMatterFabricator;
import techreborn.blocks.BlockMetalShelf;
import techreborn.blocks.BlockOre;
import techreborn.blocks.BlockPlateCuttingMachine;
import techreborn.blocks.BlockQuantumChest;
import techreborn.blocks.BlockQuantumTank;
import techreborn.blocks.BlockRollingMachine;
import techreborn.blocks.BlockSemiFluidGenerator;
import techreborn.blocks.BlockStorage;
import techreborn.blocks.BlockSupercondensator;
import techreborn.blocks.BlockThermalGenerator;
import techreborn.blocks.BlockVacuumFreezer;
import techreborn.blocks.BlockWoodenshelf;
import techreborn.client.TechRebornCreativeTab;
import techreborn.itemblocks.ItemBlockMachineCasing;
import techreborn.itemblocks.ItemBlockOre;
import techreborn.itemblocks.ItemBlockQuantumChest;
import techreborn.itemblocks.ItemBlockQuantumTank;
import techreborn.itemblocks.ItemBlockStorage;
import techreborn.tiles.TileAlloySmelter;
import techreborn.tiles.TileBlastFurnace;
import techreborn.tiles.TileCentrifuge;
import techreborn.tiles.TileChunkLoader;
import techreborn.tiles.TileGrinder;
import techreborn.tiles.TileImplosionCompressor;
import techreborn.tiles.TileMachineCasing;
import techreborn.tiles.TileMatterFabricator;
import techreborn.tiles.TileQuantumChest;
import techreborn.tiles.TileQuantumTank;
import techreborn.tiles.TileRollingMachine;
import techreborn.tiles.TileThermalGenerator;
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

	public static Block ore;
	public static Block storage;

	public static void init()
	{
		thermalGenerator = new BlockThermalGenerator()
				.setBlockName("techreborn.thermalGenerator")
				.setBlockTextureName("techreborn:ThermalGenerator_other")
				.setCreativeTab(TechRebornCreativeTab.instance);
		GameRegistry.registerBlock(thermalGenerator, "techreborn.thermalGenerator");
		GameRegistry.registerTileEntity(TileThermalGenerator.class, "TileThermalGenerator");

		quantumTank = new BlockQuantumTank()
				.setBlockName("techreborn.quantumTank")
				.setBlockTextureName("techreborn:quantumTank")
				.setCreativeTab(TechRebornCreativeTab.instance);
		GameRegistry.registerBlock(quantumTank, ItemBlockQuantumTank.class, "techreborn.quantumTank");
		GameRegistry.registerTileEntity(TileQuantumTank.class, "TileQuantumTank");

		quantumChest = new BlockQuantumChest()
				.setBlockName("techreborn.quantumChest")
				.setBlockTextureName("techreborn:quantumChest")
				.setCreativeTab(TechRebornCreativeTab.instance);
		GameRegistry.registerBlock(quantumChest, ItemBlockQuantumChest.class, "techreborn.quantumChest");
		GameRegistry.registerTileEntity(TileQuantumChest.class, "TileQuantumChest");

		centrifuge = new BlockCentrifuge()
				.setBlockName("techreborn.centrifuge")
				.setBlockTextureName("techreborn:centrifuge")
				.setCreativeTab(TechRebornCreativeTab.instance);
		
		GameRegistry.registerBlock(centrifuge, "techreborn.centrifuge");
		GameRegistry.registerTileEntity(TileCentrifuge.class, "TileCentrifuge");

		RollingMachine = new BlockRollingMachine(Material.rock);
		GameRegistry.registerBlock(RollingMachine, "rollingmachine");
		GameRegistry.registerTileEntity(TileRollingMachine.class, "TileRollingMachine");

		BlastFurnace = new BlockBlastFurnace(Material.rock);
		GameRegistry.registerBlock(BlastFurnace, "blastFurnace");
		GameRegistry.registerTileEntity(TileBlastFurnace.class, "TileBlastFurnace");
		
		AlloySmelter = new BlockAlloySmelter(Material.rock);
		GameRegistry.registerBlock(AlloySmelter, "alloySmelter");
		GameRegistry.registerTileEntity(TileAlloySmelter.class, "TileAlloySmalter");
		
		Grinder = new BlockGrinder(Material.rock);
		GameRegistry.registerBlock(Grinder, "grinder");
		GameRegistry.registerTileEntity(TileGrinder.class, "TileGrinder");
		
		ImplosionCompressor = new BlockImplosionCompressor(Material.rock);
		GameRegistry.registerBlock(ImplosionCompressor, "implosioncompressor");
		GameRegistry.registerTileEntity(TileImplosionCompressor.class, "TileImplosionCompressor");
		
		MatterFabricator = new BlockMatterFabricator(Material.rock);
		GameRegistry.registerBlock(MatterFabricator, "matterfabricator");
		GameRegistry.registerTileEntity(TileMatterFabricator.class, "TileMatterFabricator");
		
		ChunkLoader = new BlockChunkLoader(Material.rock);
		GameRegistry.registerBlock(ChunkLoader, "chunkloader");
		GameRegistry.registerTileEntity(TileChunkLoader.class, "TileChunkLoader");

		MachineCasing = new BlockMachineCasing(Material.rock);
		GameRegistry.registerBlock(MachineCasing, ItemBlockMachineCasing.class, "machinecasing");
		GameRegistry.registerTileEntity(TileMachineCasing.class, "TileMachineCasing");

		ore = new BlockOre(Material.rock);
		GameRegistry.registerBlock(ore, ItemBlockOre.class, "techreborn.ore");
		LogHelper.info("TechReborns Blocks Loaded");

		storage = new BlockStorage(Material.rock);
		GameRegistry.registerBlock(storage, ItemBlockStorage.class, "techreborn.storage");
		LogHelper.info("TechReborns Blocks Loaded");
		
		HighAdvancedMachineBlock = new BlockHighlyAdvancedMachine(Material.rock);
		GameRegistry.registerBlock(HighAdvancedMachineBlock, "highlyadvancedmachine");
		
		Dragoneggenergysiphoner = new BlockDragonEggSiphoner(Material.rock);
		GameRegistry.registerBlock(Dragoneggenergysiphoner, "dragoneggenergsiphon");
		
		Magicenergeyconverter = new BlockMagicEnergyConverter(Material.rock);
		GameRegistry.registerBlock(Magicenergeyconverter, "magicenergyconverter");
		
		AssemblyMachine = new BlockAssemblingMachine(Material.rock);
		GameRegistry.registerBlock(AssemblyMachine, "assemblymachine");
		
		DieselGenerator = new BlockDieselGenerator(Material.rock);
		GameRegistry.registerBlock(DieselGenerator, "dieselgenerator");
		
		IndustrialElectrolyzer = new BlockIndustrialElectrolyzer(Material.rock);
		GameRegistry.registerBlock(IndustrialElectrolyzer, "industrialelectrolyzer");
		
		MagicalAbsorber = new BlockMagicEnergyAbsorber(Material.rock);
		GameRegistry.registerBlock(MagicalAbsorber, "magicrnergyabsorber");
		
		Semifluidgenerator = new BlockSemiFluidGenerator(Material.rock);
		GameRegistry.registerBlock(Semifluidgenerator, "semifluidgenerator");
		
		AlloyFurnace = new BlockAlloyFurnace(Material.rock);
		GameRegistry.registerBlock(AlloyFurnace, "alloyfurnace");
		
		ChemicalReactor = new BlockChemicalReactor(Material.rock);
		GameRegistry.registerBlock(ChemicalReactor, "chemicalreactor");
		
		lathe = new BlockLathe(Material.rock);
		GameRegistry.registerBlock(lathe, "lathe");
		
		platecuttingmachine = new BlockPlateCuttingMachine(Material.rock);
		GameRegistry.registerBlock(platecuttingmachine, "platecuttingmachine");
		
		Idsu = new BlockIDSU(Material.rock);
		GameRegistry.registerBlock(Idsu, "idsu");
		
		Aesu = new BlockAesu(Material.rock);
		GameRegistry.registerBlock(Aesu, "aesu");
		
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

		registerOreDict();
	}

	public static void registerOreDict()
	{
		OreDictionary.registerOre("oreGalena", new ItemStack(ore, 1, 0));
		OreDictionary.registerOre("oreIridium", new ItemStack(ore, 1, 1));
		OreDictionary.registerOre("oreRuby", new ItemStack(ore, 1, 2));
		OreDictionary.registerOre("oreSapphire", new ItemStack(ore, 1, 3));
		OreDictionary.registerOre("oreBauxite", new ItemStack(ore, 1, 4));
		OreDictionary.registerOre("orePyrite", new ItemStack(ore, 1, 5));
		OreDictionary.registerOre("oreCinnabar", new ItemStack(ore, 1, 6));
		OreDictionary.registerOre("oreSphalerite", new ItemStack(ore, 1, 7));
		OreDictionary.registerOre("oreTungston", new ItemStack(ore, 1, 8));
		OreDictionary.registerOre("oreSheldonite", new ItemStack(ore, 1, 9));
		OreDictionary.registerOre("oreOlivine", new ItemStack(ore, 1, 10));
		OreDictionary.registerOre("oreSodalite", new ItemStack(ore, 1, 11));

		OreDictionary.registerOre("blockSilver", new ItemStack(storage, 1, 0));
		OreDictionary.registerOre("blockAluminium",
				new ItemStack(storage, 1, 1));
		OreDictionary
				.registerOre("blockTitanium", new ItemStack(storage, 1, 2));
		OreDictionary
				.registerOre("blockSapphire", new ItemStack(storage, 1, 3));
		OreDictionary.registerOre("blockRuby", new ItemStack(storage, 1, 4));
		OreDictionary.registerOre("blockGreenSapphire", new ItemStack(storage,
				1, 5));
		OreDictionary.registerOre("blockChrome", new ItemStack(storage, 1, 6));
		OreDictionary
				.registerOre("blockElectrum", new ItemStack(storage, 1, 7));
		OreDictionary
				.registerOre("blockTungsten", new ItemStack(storage, 1, 8));
		OreDictionary.registerOre("blockLead", new ItemStack(storage, 1, 9));
		OreDictionary.registerOre("blockZinc", new ItemStack(storage, 1, 10));
		OreDictionary.registerOre("blockBrass", new ItemStack(storage, 1, 11));
		OreDictionary.registerOre("blockSteel", new ItemStack(storage, 1, 12));
		OreDictionary.registerOre("blockPlatinum",
				new ItemStack(storage, 1, 13));
		OreDictionary.registerOre("blockNickel", new ItemStack(storage, 1, 14));
		OreDictionary.registerOre("blockInvar", new ItemStack(storage, 1, 15));

	}

}

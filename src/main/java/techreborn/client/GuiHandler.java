package techreborn.client;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import techreborn.client.container.*;
import techreborn.client.gui.*;
import techreborn.manual.GuiManual;
import techreborn.tiles.*;
import techreborn.tiles.fusionReactor.TileEntityFusionController;
import techreborn.tiles.generator.*;
import techreborn.tiles.idsu.TileIDSU;
import techreborn.tiles.lesu.TileLesu;
import techreborn.tiles.storage.TileBatBox;
import techreborn.tiles.storage.TileMFE;
import techreborn.tiles.storage.TileMFSU;
import techreborn.tiles.teir1.*;

public class GuiHandler implements IGuiHandler
{

	public static final int thermalGeneratorID = 0;
	public static final int quantumTankID = 1;
	public static final int quantumChestID = 2;
	public static final int centrifugeID = 3;
	public static final int rollingMachineID = 4;
	public static final int blastFurnaceID = 5;
	public static final int alloySmelterID = 6;
	public static final int industrialGrinderID = 7;
	public static final int implosionCompresserID = 8;
	public static final int matterfabID = 9;
	public static final int manuelID = 10;
	public static final int chunkloaderID = 11;
	public static final int assemblingmachineID = 12;
	public static final int dieselGeneratorID = 15;
	public static final int industrialElectrolyzerID = 16;
	public static final int aesuID = 17;
	public static final int alloyFurnaceID = 18;
	public static final int sawMillID = 19;
	public static final int chemicalReactorID = 20;
	public static final int semifluidGeneratorID = 21;
	public static final int gasTurbineID = 22;
	public static final int digitalChestID = 23;
	public static final int destructoPackID = 25;
	public static final int lesuID = 26;
	public static final int idsuID = 27;
	public static final int chargeBench = 28;
	public static final int fusionID = 29;
	public static final int vacuumFreezerID = 30;
	public static final int grinderID = 31;
	public static final int generatorID = 32;
	public static final int extractorID = 33;
	public static final int compressorID = 34;
	public static final int electricFurnaceID = 35;
	public static final int ironFurnace = 36;
	public static final int recyclerID = 37;
	public static final int scrapboxinatorID = 38;
	public static final int batboxID = 39;
	public static final int mfsuID = 40;
	public static final int mfeID = 41;
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (ID == thermalGeneratorID)
		{
			return new ContainerThermalGenerator((TileThermalGenerator) world.getTileEntity(new BlockPos(x, y, z)),
					player);
		} else if (ID == semifluidGeneratorID)
		{
			return new ContainerSemifluidGenerator((TileSemifluidGenerator) world.getTileEntity(new BlockPos(x, y, z)),
					player);
		} else if (ID == gasTurbineID)
		{
			return new ContainerGasTurbine((TileGasTurbine) world.getTileEntity(new BlockPos(x, y, z)), player);
		} else if (ID == quantumTankID)
		{
			return new ContainerQuantumTank((TileQuantumTank) world.getTileEntity(new BlockPos(x, y, z)), player);
		} else if (ID == digitalChestID)
		{
			return new ContainerDigitalChest((TileDigitalChest) world.getTileEntity(new BlockPos(x, y, z)), player);
		} else if (ID == quantumChestID)
		{
			return new ContainerQuantumChest((TileQuantumChest) world.getTileEntity(new BlockPos(x, y, z)), player);
		} else if (ID == centrifugeID)
		{
			return new ContainerCentrifuge((TileCentrifuge) world.getTileEntity(new BlockPos(x, y, z)), player);
		} else if (ID == rollingMachineID)
		{
			return new ContainerRollingMachine((TileRollingMachine) world.getTileEntity(new BlockPos(x, y, z)), player);
		} else if (ID == blastFurnaceID)
		{
			return new ContainerBlastFurnace((TileBlastFurnace) world.getTileEntity(new BlockPos(x, y, z)), player);
		} else if (ID == alloySmelterID)
		{
			return new ContainerAlloySmelter((TileAlloySmelter) world.getTileEntity(new BlockPos(x, y, z)), player);
		} else if (ID == industrialGrinderID)
		{
			return new ContainerIndustrialGrinder((TileIndustrialGrinder) world.getTileEntity(new BlockPos(x, y, z)),
					player);
		} else if (ID == implosionCompresserID)
		{
			return new ContainerImplosionCompressor(
					(TileImplosionCompressor) world.getTileEntity(new BlockPos(x, y, z)), player);
		} else if (ID == matterfabID)
		{
			return new ContainerMatterFabricator((TileMatterFabricator) world.getTileEntity(new BlockPos(x, y, z)),
					player);
		} else if (ID == chunkloaderID)
		{
			return new ContainerChunkloader((TileChunkLoader) world.getTileEntity(new BlockPos(x, y, z)), player);
		} else if (ID == assemblingmachineID)
		{
			return new ContainerAssemblingMachine((TileAssemblingMachine) world.getTileEntity(new BlockPos(x, y, z)),
					player);
		} else if (ID == dieselGeneratorID)
		{
			return new ContainerDieselGenerator((TileDieselGenerator) world.getTileEntity(new BlockPos(x, y, z)),
					player);
		} else if (ID == industrialElectrolyzerID)
		{
			return new ContainerIndustrialElectrolyzer(
					(TileIndustrialElectrolyzer) world.getTileEntity(new BlockPos(x, y, z)), player);
		} else if (ID == aesuID)
		{
			return new ContainerAesu((TileAesu) world.getTileEntity(new BlockPos(x, y, z)), player);
		} else if (ID == alloyFurnaceID)
		{
			return new ContainerAlloyFurnace((TileAlloyFurnace) world.getTileEntity(new BlockPos(x, y, z)), player);
		} else if (ID == sawMillID)
		{
			return new ContainerIndustrialSawmill((TileIndustrialSawmill) world.getTileEntity(new BlockPos(x, y, z)),
					player);
		} else if (ID == chemicalReactorID)
		{
			return new ContainerChemicalReactor((TileChemicalReactor) world.getTileEntity(new BlockPos(x, y, z)),
					player);
		} else if (ID == manuelID)
		{
			return null;
		} else if (ID == destructoPackID)
		{
			return new ContainerDestructoPack(player);
		} else if (ID == lesuID)
		{
			return new ContainerLesu((TileLesu) world.getTileEntity(new BlockPos(x, y, z)), player);
		} else if (ID == idsuID)
		{
			return new ContainerIDSU((TileIDSU) world.getTileEntity(new BlockPos(x, y, z)), player);
		} else if (ID == chargeBench)
		{
			return new ContainerChargeBench((TileChargeBench) world.getTileEntity(new BlockPos(x, y, z)), player);
		} else if (ID == fusionID)
		{
			return new ContainerFusionReactor((TileEntityFusionController) world.getTileEntity(new BlockPos(x, y, z)),
					player);
		} else if (ID == vacuumFreezerID)
		{
			return new ContainerVacuumFreezer((TileVacuumFreezer) world.getTileEntity(new BlockPos(x, y, z)), player);
		} else if (ID == grinderID)
		{
			return new ContainerGrinder((TileGrinder) world.getTileEntity(new BlockPos(x, y, z)), player);
		} else if (ID == generatorID)
		{
			return new ContainerGenerator((TileGenerator) world.getTileEntity(new BlockPos(x, y, z)), player);
		} else if (ID == extractorID)
		{
			return new ContainerExtractor((TileExtractor) world.getTileEntity(new BlockPos(x, y, z)), player);
		} else if (ID == compressorID)
		{
			return new ContainerCompressor((TileCompressor) world.getTileEntity(new BlockPos(x, y, z)), player);
		} else if (ID == electricFurnaceID)
		{
			return new ContainerElectricFurnace((TileElectricFurnace) world.getTileEntity(new BlockPos(x, y, z)),
					player);
		} else if (ID == ironFurnace)
		{
			return new ContainerIronFurnace((TileIronFurnace) world.getTileEntity(new BlockPos(x, y, z)), player);
		} else if (ID == recyclerID)
		{
			return new ContainerRecycler((TileRecycler) world.getTileEntity(new BlockPos(x, y, z)), player);
		} else if (ID == scrapboxinatorID)
		{
			return new ContainerScrapboxinator((TileScrapboxinator) world.getTileEntity(new BlockPos(x, y, z)), player);
		} else if (ID == batboxID)
		{
			return new ContainerBatbox((TileBatBox) world.getTileEntity(new BlockPos(x, y, z)), player);
		}else if (ID == mfsuID){
			return new ContainerMFSU((TileMFSU) world.getTileEntity(new BlockPos(x, y, z)), player);
		}else if (ID == mfeID){
			return new ContainerMFE((TileMFE) world.getTileEntity(new BlockPos(x, y, z)), player);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (ID == thermalGeneratorID)
		{
			return new GuiThermalGenerator(player, (TileThermalGenerator) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == semifluidGeneratorID)
		{
			return new GuiSemifluidGenerator(player,
					(TileSemifluidGenerator) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == gasTurbineID)
		{
			return new GuiGasTurbine(player, (TileGasTurbine) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == quantumTankID)
		{
			return new GuiQuantumTank(player, (TileQuantumTank) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == digitalChestID)
		{
			return new GuiDigitalChest(player, (TileDigitalChest) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == quantumChestID)
		{
			return new GuiQuantumChest(player, (TileQuantumChest) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == centrifugeID)
		{
			return new GuiCentrifuge(player, (TileCentrifuge) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == rollingMachineID)
		{
			return new GuiRollingMachine(player, (TileRollingMachine) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == blastFurnaceID)
		{
			return new GuiBlastFurnace(player, (TileBlastFurnace) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == alloySmelterID)
		{
			return new GuiAlloySmelter(player, (TileAlloySmelter) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == industrialGrinderID)
		{
			return new GuiIndustrialGrinder(player, (TileIndustrialGrinder) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == implosionCompresserID)
		{
			return new GuiImplosionCompressor(player,
					(TileImplosionCompressor) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == matterfabID)
		{
			return new GuiMatterFabricator(player, (TileMatterFabricator) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == chunkloaderID)
		{
			return new GuiChunkLoader(player, (TileChunkLoader) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == assemblingmachineID)
		{
			return new GuiAssemblingMachine(player, (TileAssemblingMachine) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == dieselGeneratorID)
		{
			return new GuiDieselGenerator(player, (TileDieselGenerator) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == industrialElectrolyzerID)
		{
			return new GuiIndustrialElectrolyzer(player,
					(TileIndustrialElectrolyzer) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == aesuID)
		{
			return new GuiAesu(player, (TileAesu) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == alloyFurnaceID)
		{
			return new GuiAlloyFurnace(player, (TileAlloyFurnace) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == sawMillID)
		{
			return new GuiIndustrialSawmill(player, (TileIndustrialSawmill) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == chemicalReactorID)
		{
			return new GuiChemicalReactor(player, (TileChemicalReactor) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == manuelID)
		{
			return new GuiManual();
		} else if (ID == destructoPackID)
		{
			return new GuiDestructoPack(new ContainerDestructoPack(player));
		} else if (ID == lesuID)
		{
			return new GuiLesu(player, (TileLesu) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == idsuID)
		{
			return new GuiIDSU(player, (TileIDSU) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == chargeBench)
		{
			return new GuiChargeBench(player, (TileChargeBench) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == fusionID)
		{
			return new GuiFusionReactor(player,
					(TileEntityFusionController) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == vacuumFreezerID)
		{
			return new GuiVacuumFreezer(player, (TileVacuumFreezer) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == grinderID)
		{
			return new GuiGrinder(player, (TileGrinder) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == generatorID)
		{
			return new GuiGenerator(player, (TileGenerator) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == extractorID)
		{
			return new GuiExtractor(player, (TileExtractor) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == compressorID)
		{
			return new GuiCompressor(player, (TileCompressor) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == electricFurnaceID)
		{
			return new GuiElectricFurnace(player, (TileElectricFurnace) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == ironFurnace)
		{
			return new GuiIronFurnace(player, (TileIronFurnace) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == recyclerID)
		{
			return new GuiRecycler(player, (TileRecycler) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == scrapboxinatorID)
		{
			return new GuiScrapboxinator(player, (TileScrapboxinator) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == batboxID)
		{
			return new GuiBatbox(player, (TileBatBox) world.getTileEntity(new BlockPos(x, y, z)));
		}
		else if (ID == mfsuID)
		{
			return new GuiMFSU(player, (TileMFSU) world.getTileEntity(new BlockPos(x, y, z)));
		}
		else if (ID == mfeID)
		{
			return new GuiMFE(player, (TileMFE) world.getTileEntity(new BlockPos(x, y, z)));
		}
		return null;
	}
}

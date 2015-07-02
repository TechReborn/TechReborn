package techreborn.client;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import techreborn.client.container.ContainerAesu;
import techreborn.client.container.ContainerAlloyFurnace;
import techreborn.client.container.ContainerAlloySmelter;
import techreborn.client.container.ContainerAssemblingMachine;
import techreborn.client.container.ContainerBlastFurnace;
import techreborn.client.container.ContainerCentrifuge;
import techreborn.client.container.ContainerChargeBench;
import techreborn.client.container.ContainerChemicalReactor;
import techreborn.client.container.ContainerChunkloader;
import techreborn.client.container.ContainerDestructoPack;
import techreborn.client.container.ContainerDieselGenerator;
import techreborn.client.container.ContainerDigitalChest;
import techreborn.client.container.ContainerGasTurbine;
import techreborn.client.container.ContainerGrinder;
import techreborn.client.container.ContainerIDSU;
import techreborn.client.container.ContainerImplosionCompressor;
import techreborn.client.container.ContainerIndustrialElectrolyzer;
import techreborn.client.container.ContainerIndustrialSawmill;
import techreborn.client.container.ContainerLathe;
import techreborn.client.container.ContainerLesu;
import techreborn.client.container.ContainerMatterFabricator;
import techreborn.client.container.ContainerPda;
import techreborn.client.container.ContainerPlateCuttingMachine;
import techreborn.client.container.ContainerQuantumChest;
import techreborn.client.container.ContainerQuantumTank;
import techreborn.client.container.ContainerRollingMachine;
import techreborn.client.container.ContainerSemifluidGenerator;
import techreborn.client.container.ContainerThermalGenerator;
import techreborn.client.gui.GuiAesu;
import techreborn.client.gui.GuiAlloyFurnace;
import techreborn.client.gui.GuiAlloySmelter;
import techreborn.client.gui.GuiAssemblingMachine;
import techreborn.client.gui.GuiBlastFurnace;
import techreborn.client.gui.GuiCentrifuge;
import techreborn.client.gui.GuiChargeBench;
import techreborn.client.gui.GuiChemicalReactor;
import techreborn.client.gui.GuiChunkLoader;
import techreborn.client.gui.GuiDestructoPack;
import techreborn.client.gui.GuiDieselGenerator;
import techreborn.client.gui.GuiDigitalChest;
import techreborn.client.gui.GuiGasTurbine;
import techreborn.client.gui.GuiGrinder;
import techreborn.client.gui.GuiIDSU;
import techreborn.client.gui.GuiImplosionCompressor;
import techreborn.client.gui.GuiIndustrialElectrolyzer;
import techreborn.client.gui.GuiIndustrialSawmill;
import techreborn.client.gui.GuiLathe;
import techreborn.client.gui.GuiLesu;
import techreborn.client.gui.GuiMatterFabricator;
import techreborn.client.gui.GuiPlateCuttingMachine;
import techreborn.client.gui.GuiQuantumChest;
import techreborn.client.gui.GuiQuantumTank;
import techreborn.client.gui.GuiRollingMachine;
import techreborn.client.gui.GuiSemifluidGenerator;
import techreborn.client.gui.GuiThermalGenerator;
import techreborn.pda.GuiPda;
import techreborn.tiles.TileAesu;
import techreborn.tiles.TileAlloyFurnace;
import techreborn.tiles.TileAlloySmelter;
import techreborn.tiles.TileAssemblingMachine;
import techreborn.tiles.TileBlastFurnace;
import techreborn.tiles.TileCentrifuge;
import techreborn.tiles.TileChargeBench;
import techreborn.tiles.TileChemicalReactor;
import techreborn.tiles.TileChunkLoader;
import techreborn.tiles.TileDieselGenerator;
import techreborn.tiles.TileDigitalChest;
import techreborn.tiles.TileGasTurbine;
import techreborn.tiles.TileGrinder;
import techreborn.tiles.TileImplosionCompressor;
import techreborn.tiles.TileIndustrialElectrolyzer;
import techreborn.tiles.TileIndustrialSawmill;
import techreborn.tiles.TileLathe;
import techreborn.tiles.TileMatterFabricator;
import techreborn.tiles.TilePlateCuttingMachine;
import techreborn.tiles.TileQuantumChest;
import techreborn.tiles.TileQuantumTank;
import techreborn.tiles.TileRollingMachine;
import techreborn.tiles.TileSemifluidGenerator;
import techreborn.tiles.TileThermalGenerator;
import techreborn.tiles.idsu.TileIDSU;
import techreborn.tiles.lesu.TileLesu;

public class GuiHandler implements IGuiHandler {

	public static final int thermalGeneratorID = 0;
	public static final int quantumTankID = 1;
	public static final int quantumChestID = 2;
	public static final int centrifugeID = 3;
	public static final int rollingMachineID = 4;
	public static final int blastFurnaceID = 5;
	public static final int alloySmelterID = 6;
	public static final int grinderID = 7;
	public static final int compresserID = 8;
	public static final int matterfabID = 9;
	public static final int pdaID = 10;
	public static final int chunkloaderID = 11;
	public static final int assemblingmachineID = 12;
	public static final int latheID = 13;
	public static final int platecuttingmachineID = 14;
	public static final int dieselGeneratorID = 15;
	public static final int industrialElectrolyzerID = 16;
	public static final int aesuID =17;
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
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z)
	{
		if (ID == thermalGeneratorID)
		{
			return new ContainerThermalGenerator(
					(TileThermalGenerator) world.getTileEntity(x, y, z), player);
		} else if (ID == semifluidGeneratorID)
		{
			return new ContainerSemifluidGenerator(
				(TileSemifluidGenerator) world.getTileEntity(x, y, z), player);			
		} else if (ID == gasTurbineID)
		{
			return new ContainerGasTurbine(
				(TileGasTurbine) world.getTileEntity(x, y, z), player);			
		} else if (ID == quantumTankID)
		{
			return new ContainerQuantumTank(
					(TileQuantumTank) world.getTileEntity(x, y, z), player);
		} else if (ID == digitalChestID)
		{
			return new ContainerDigitalChest(
					(TileDigitalChest) world.getTileEntity(x, y, z), player);
		} else if (ID == quantumChestID)
		{
			return new ContainerQuantumChest(
					(TileQuantumChest) world.getTileEntity(x, y, z), player);
		} else if (ID == centrifugeID)
		{
			return new ContainerCentrifuge(
					(TileCentrifuge) world.getTileEntity(x, y, z), player);
		} else if (ID == rollingMachineID)
		{
			return new ContainerRollingMachine(
					(TileRollingMachine) world.getTileEntity(x, y, z), player);
		} else if (ID == blastFurnaceID)
		{
			return new ContainerBlastFurnace(
					(TileBlastFurnace) world.getTileEntity(x, y, z), player);
		} else if (ID == alloySmelterID)
		{
			return new ContainerAlloySmelter(
					(TileAlloySmelter) world.getTileEntity(x, y, z), player);
		} else if (ID == grinderID)
		{
			return new ContainerGrinder(
					(TileGrinder) world.getTileEntity(x, y, z), player);
		} else if (ID == compresserID)
		{
			return new ContainerImplosionCompressor(
					(TileImplosionCompressor) world.getTileEntity(x, y, z), player);
		} else if (ID == matterfabID)
		{
			return new ContainerMatterFabricator(
					(TileMatterFabricator) world.getTileEntity(x, y, z), player);
		} else if (ID == chunkloaderID)
		{
			return new ContainerChunkloader(
					(TileChunkLoader) world.getTileEntity(x, y, z), player);
		} else if (ID == assemblingmachineID)
		{
			return new ContainerAssemblingMachine(
					(TileAssemblingMachine) world.getTileEntity(x, y, z), player);
		} else if (ID == latheID)
		{
			return new ContainerLathe(
					(TileLathe) world.getTileEntity(x, y, z), player);
		} else if (ID == platecuttingmachineID)
		{
			return new ContainerPlateCuttingMachine(
					(TilePlateCuttingMachine) world.getTileEntity(x, y, z), player);
		} else if (ID == dieselGeneratorID)
		{
			return new ContainerDieselGenerator(
					(TileDieselGenerator) world.getTileEntity(x, y, z), player);
		} else if (ID == industrialElectrolyzerID)
		{
			return new ContainerIndustrialElectrolyzer(
					(TileIndustrialElectrolyzer) world.getTileEntity(x, y, z), player);
		} else if (ID == aesuID)
		{
			return new ContainerAesu(
					(TileAesu) world.getTileEntity(x, y, z), player);
		} else if (ID == alloyFurnaceID)
		{
			return new ContainerAlloyFurnace(
					(TileAlloyFurnace) world.getTileEntity(x, y, z), player);
		} else if (ID == sawMillID)
		{
			return new ContainerIndustrialSawmill(
					(TileIndustrialSawmill) world.getTileEntity(x, y, z), player);	
		} else if (ID == chemicalReactorID)
		{
			return new ContainerChemicalReactor(
					(TileChemicalReactor) world.getTileEntity(x, y, z), player);	
		} else if (ID == pdaID)
		{
			return null;
		} else if (ID == destructoPackID) {
			return new ContainerDestructoPack(player);
		} else if (ID == lesuID) {
            return new ContainerLesu((TileLesu) world.getTileEntity(x, y, z), player);
        } else if (ID == idsuID) {
			return new ContainerIDSU((TileIDSU) world.getTileEntity(x, y, z), player);
        } else if (ID == chargeBench) {
			return new ContainerChargeBench((TileChargeBench) world.getTileEntity(x, y, z), player);
		}
		

		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z)
	{
		if (ID == thermalGeneratorID)
		{
			return new GuiThermalGenerator(player,
					(TileThermalGenerator) world.getTileEntity(x, y, z));
		} else if (ID == semifluidGeneratorID)
		{
			return new GuiSemifluidGenerator(player,
				(TileSemifluidGenerator) world.getTileEntity(x, y, z));			
		} else if (ID == gasTurbineID)
		{
			return new GuiGasTurbine(player,
				(TileGasTurbine) world.getTileEntity(x, y, z));			
		} else if (ID == quantumTankID)
		{
			return new GuiQuantumTank(player,
					(TileQuantumTank) world.getTileEntity(x, y, z));
		} else if (ID == digitalChestID)
		{
			return new GuiDigitalChest(player,
					(TileDigitalChest) world.getTileEntity(x, y, z));
		} else if (ID == quantumChestID)
		{
			return new GuiQuantumChest(player,
					(TileQuantumChest) world.getTileEntity(x, y, z));
		} else if (ID == centrifugeID)
		{
			return new GuiCentrifuge(player,
					(TileCentrifuge) world.getTileEntity(x, y, z));
		} else if (ID == rollingMachineID)
		{
			return new GuiRollingMachine(player,
					(TileRollingMachine) world.getTileEntity(x, y, z));
		} else if (ID == blastFurnaceID)
		{
			return new GuiBlastFurnace(player,
					(TileBlastFurnace) world.getTileEntity(x, y, z));
		} else if (ID == alloySmelterID)
		{
			return new GuiAlloySmelter(player,
					(TileAlloySmelter) world.getTileEntity(x, y, z));
		} else if (ID == grinderID)
		{
			return new GuiGrinder(player,
					(TileGrinder) world.getTileEntity(x, y, z));
		} else if (ID == compresserID)
		{
			return new GuiImplosionCompressor(player,
					(TileImplosionCompressor) world.getTileEntity(x, y, z));
		} else if (ID == matterfabID)
		{
			return new GuiMatterFabricator(player,
					(TileMatterFabricator) world.getTileEntity(x, y, z));
		} else if (ID == chunkloaderID)
		{
			return new GuiChunkLoader(player,
					(TileChunkLoader) world.getTileEntity(x, y, z));	
		} else if (ID == assemblingmachineID)
		{
			return new GuiAssemblingMachine(player,
					(TileAssemblingMachine) world.getTileEntity(x, y, z));	
		} else if (ID == latheID)
		{
			return new GuiLathe(player,
					(TileLathe) world.getTileEntity(x, y, z));	
		} else if (ID == platecuttingmachineID)
		{
			return new GuiPlateCuttingMachine(player,
					(TilePlateCuttingMachine) world.getTileEntity(x, y, z));	
		} else if (ID == dieselGeneratorID)
		{
			return new GuiDieselGenerator(player,
					(TileDieselGenerator) world.getTileEntity(x, y, z));	
		} else if (ID == industrialElectrolyzerID)
		{
			return new GuiIndustrialElectrolyzer(player,
					(TileIndustrialElectrolyzer) world.getTileEntity(x, y, z));	
		} else if (ID == aesuID)
		{
			return new GuiAesu(player,
					(TileAesu) world.getTileEntity(x, y, z));	
		} else if (ID == alloyFurnaceID)
		{
			return new GuiAlloyFurnace(player,
					(TileAlloyFurnace) world.getTileEntity(x, y, z));	
		} else if (ID == sawMillID)
		{
			return new GuiIndustrialSawmill(player,
					(TileIndustrialSawmill) world.getTileEntity(x, y, z));	
		} else if (ID == chemicalReactorID)
		{
			return new GuiChemicalReactor(player,
					(TileChemicalReactor) world.getTileEntity(x, y, z));	
		} else if (ID == pdaID)
		{
			return new GuiPda(player, new ContainerPda(player));
		} else if (ID == destructoPackID) {
			return new GuiDestructoPack(new ContainerDestructoPack(player));
		} else if (ID == lesuID) {
            return new GuiLesu(player, (TileLesu)world.getTileEntity(x, y, z));
        } else if (ID == idsuID) {
			return new GuiIDSU(player, (TileIDSU)world.getTileEntity(x, y, z));
        } else if (ID == chargeBench) {
			return new GuiChargeBench(player, (TileChargeBench)world.getTileEntity(x, y, z));
		}
		return null;
	}
}

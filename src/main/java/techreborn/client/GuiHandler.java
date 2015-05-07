package techreborn.client;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import techreborn.client.container.ContainerAlloySmelter;
import techreborn.client.container.ContainerAssemblingMachine;
import techreborn.client.container.ContainerBlastFurnace;
import techreborn.client.container.ContainerCentrifuge;
import techreborn.client.container.ContainerChunkloader;
import techreborn.client.container.ContainerGrinder;
import techreborn.client.container.ContainerImplosionCompressor;
import techreborn.client.container.ContainerMatterFabricator;
import techreborn.client.container.ContainerQuantumChest;
import techreborn.client.container.ContainerQuantumTank;
import techreborn.client.container.ContainerRollingMachine;
import techreborn.client.container.ContainerThermalGenerator;
import techreborn.client.gui.GuiAlloySmelter;
import techreborn.client.gui.GuiAssemblingMachine;
import techreborn.client.gui.GuiBlastFurnace;
import techreborn.client.gui.GuiCentrifuge;
import techreborn.client.gui.GuiChunkLoader;
import techreborn.client.gui.GuiGrinder;
import techreborn.client.gui.GuiImplosionCompressor;
import techreborn.client.gui.GuiMatterFabricator;
import techreborn.client.gui.GuiQuantumChest;
import techreborn.client.gui.GuiQuantumTank;
import techreborn.client.gui.GuiRollingMachine;
import techreborn.client.gui.GuiThermalGenerator;
import techreborn.pda.GuiPda;
import techreborn.tiles.TileAlloySmelter;
import techreborn.tiles.TileAssemblingMachine;
import techreborn.tiles.TileBlastFurnace;
import techreborn.tiles.TileCentrifuge;
import techreborn.tiles.TileChunkLoader;
import techreborn.tiles.TileGrinder;
import techreborn.tiles.TileImplosionCompressor;
import techreborn.tiles.TileMatterFabricator;
import techreborn.tiles.TileQuantumChest;
import techreborn.tiles.TileQuantumTank;
import techreborn.tiles.TileRollingMachine;
import techreborn.tiles.TileThermalGenerator;
import cpw.mods.fml.common.network.IGuiHandler;

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

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z)
	{
		if (ID == thermalGeneratorID)
		{
			return new ContainerThermalGenerator(
					(TileThermalGenerator) world.getTileEntity(x, y, z), player);
		} else if (ID == quantumTankID)
		{
			return new ContainerQuantumTank(
					(TileQuantumTank) world.getTileEntity(x, y, z), player);
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
		} else if (ID == pdaID)
		{
			return null;
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
		} else if (ID == quantumTankID)
		{
			return new GuiQuantumTank(player,
					(TileQuantumTank) world.getTileEntity(x, y, z));
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
		} else if (ID == pdaID)
		{
			return new GuiPda(player);
		}
		return null;
	}
}

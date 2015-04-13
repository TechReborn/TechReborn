package techreborn.client;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import techreborn.client.container.ContainerCentrifuge;
import techreborn.client.container.ContainerQuantumChest;
import techreborn.client.container.ContainerQuantumTank;
import techreborn.client.container.ContainerRollingMachine;
import techreborn.client.container.ContainerThermalGenerator;
import techreborn.client.gui.GuiCentrifuge;
import techreborn.client.gui.GuiQuantumChest;
import techreborn.client.gui.GuiQuantumTank;
import techreborn.client.gui.GuiRollingMachine;
import techreborn.client.gui.GuiThermalGenerator;
import techreborn.tiles.TileCentrifuge;
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
    public static final int rollingMachineID =4;


    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if(ID == thermalGeneratorID){
            return new ContainerThermalGenerator((TileThermalGenerator) world.getTileEntity(x, y, z), player);
        } else if(ID == quantumTankID){
            return new ContainerQuantumTank((TileQuantumTank) world.getTileEntity(x, y, z), player);
        } else if(ID == quantumChestID){
            return new ContainerQuantumChest((TileQuantumChest) world.getTileEntity(x, y, z), player);
        } else if(ID == centrifugeID){
            return new ContainerCentrifuge((TileCentrifuge) world.getTileEntity(x, y, z), player);
        } else if(ID == rollingMachineID){
            return new ContainerRollingMachine((TileRollingMachine) world.getTileEntity(x, y, z), player);
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if(ID == thermalGeneratorID){
          return new GuiThermalGenerator(player, (TileThermalGenerator)world.getTileEntity(x, y, z));
        } else if(ID == quantumTankID){
            return new GuiQuantumTank(player, (TileQuantumTank)world.getTileEntity(x, y, z));
        } else if(ID == quantumChestID){
            return new GuiQuantumChest(player, (TileQuantumChest)world.getTileEntity(x, y, z));
        } else if(ID == centrifugeID){
            return new GuiCentrifuge(player, (TileCentrifuge)world.getTileEntity(x, y, z));
        } else if(ID == rollingMachineID){
            return new GuiRollingMachine(player, (TileRollingMachine)world.getTileEntity(x, y, z));
        }
        return null;
    }
}

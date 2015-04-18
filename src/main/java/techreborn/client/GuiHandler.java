package techreborn.client;


import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import techreborn.client.container.*;
import techreborn.client.gui.*;
import techreborn.pda.GuiPda;
import techreborn.tiles.*;

public class GuiHandler implements IGuiHandler {

    public static final int thermalGeneratorID = 0;
    public static final int quantumTankID = 1;
    public static final int quantumChestID = 2;
    public static final int centrifugeID = 3;
    public static final int rollingMachineID = 4;
    public static final int blastFurnaceID = 5;
    public static final int pdaID = 6;


    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == thermalGeneratorID) {
            return new ContainerThermalGenerator((TileThermalGenerator) world.getTileEntity(x, y, z), player);
        } else if (ID == quantumTankID) {
            return new ContainerQuantumTank((TileQuantumTank) world.getTileEntity(x, y, z), player);
        } else if (ID == quantumChestID) {
            return new ContainerQuantumChest((TileQuantumChest) world.getTileEntity(x, y, z), player);
        } else if (ID == centrifugeID) {
            return new ContainerCentrifuge((TileCentrifuge) world.getTileEntity(x, y, z), player);
        } else if (ID == rollingMachineID) {
            return new ContainerRollingMachine((TileRollingMachine) world.getTileEntity(x, y, z), player);
        } else if (ID == blastFurnaceID) {
            return new ContainerBlastFurnace((TileBlastFurnace) world.getTileEntity(x, y, z), player);
        } else if (ID == pdaID) {
            return null;
        }
        
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == thermalGeneratorID) {
            return new GuiThermalGenerator(player, (TileThermalGenerator) world.getTileEntity(x, y, z));
        } else if (ID == quantumTankID) {
            return new GuiQuantumTank(player, (TileQuantumTank) world.getTileEntity(x, y, z));
        } else if (ID == quantumChestID) {
            return new GuiQuantumChest(player, (TileQuantumChest) world.getTileEntity(x, y, z));
        } else if (ID == centrifugeID) {
            return new GuiCentrifuge(player, (TileCentrifuge) world.getTileEntity(x, y, z));
        } else if (ID == rollingMachineID) {
            return new GuiRollingMachine(player, (TileRollingMachine) world.getTileEntity(x, y, z));
        } else if (ID == blastFurnaceID) {
            return new GuiBlastFurnace(player, (TileBlastFurnace) world.getTileEntity(x, y, z));
        } else if (ID == pdaID) {
            return new GuiPda(player);
        }
        return null;
    }
}

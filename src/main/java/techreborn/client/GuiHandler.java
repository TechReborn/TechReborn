package techreborn.client;


import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import techreborn.client.container.ContainerThermalGenerator;
import techreborn.client.gui.GuiThermalGenerator;
import techreborn.tiles.TileThermalGenerator;

public class GuiHandler implements IGuiHandler {

    public static final int thermalGeneratorID = 0;
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if(ID == thermalGeneratorID){
            return new ContainerThermalGenerator((TileThermalGenerator) world.getTileEntity(x, y, z), player);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if(ID == thermalGeneratorID){
          return new GuiThermalGenerator(player, (TileThermalGenerator)world.getTileEntity(x, y, z));
        }
        return null;
    }
}

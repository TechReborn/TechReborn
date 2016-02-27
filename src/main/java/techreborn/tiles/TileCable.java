package techreborn.tiles;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import techreborn.powernet.IPowerCableContainer;
import techreborn.powernet.PowerCable;
import techreborn.powernet.PowerEvent;
import techreborn.powernet.PowerNetwork;


public class TileCable extends TileEntity implements IPowerCableContainer, ITickable{

    PowerCable cable = null;


    @Override
    public boolean canConnectTo(EnumFacing facing) {
        return true;
    }

    @Override
    public PowerCable getPowerCable() {
        return cable;
    }

    @Override
    public void update() {
        if(cable == null){
            cable = new PowerCable(this);
        } else {
            cable.checkNodes();
        }
    }


    public void createCable(){
        if(cable != null){
            cable.getNetwork().removeCable(cable);
            cable = null;
        }
        PowerNetwork network = new PowerNetwork();
        cable = new PowerCable(this);
        cable.setNetwork(network);
        network.addCable(cable);
    }
}

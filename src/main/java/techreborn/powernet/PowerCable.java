package techreborn.powernet;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import reborncore.api.power.IEnergyInterfaceTile;

import java.util.ArrayList;
import java.util.List;

public class PowerCable {

    List<PowerNode> nodes = new ArrayList<PowerNode>();

    IPowerCableContainer container;

    PowerNetwork network;

    public PowerCable(IPowerCableContainer container) {
        this.container = container;
    }

    public void blockUpdate(){
        checkNodes();
    }

    public void checkNodes(){
        nodes.clear();
        for(EnumFacing dir : EnumFacing.VALUES){
            if(!container.canConnectTo(dir)){
                continue;
            }
            BlockPos blockPos = container.getPos().offset(dir);
            if(container.getWorld().isBlockLoaded(blockPos)){
                TileEntity tileEntity = container.getWorld().getTileEntity(blockPos);
                if(network != null){
                    if(tileEntity instanceof IPowerCableContainer){
                        IPowerCableContainer powerCableContainer = (IPowerCableContainer) tileEntity;
                        if(powerCableContainer.getPowerCable() != null && powerCableContainer.getPowerCable().network != network){
                            network.merge(powerCableContainer.getPowerCable().network);
                        }
                    }
                }
                if(!(tileEntity instanceof IEnergyInterfaceTile)){
                    continue;
                }
                IEnergyInterfaceTile energyInterfaceTile = (IEnergyInterfaceTile) tileEntity;
                PowerNode node = new PowerNode(blockPos, tileEntity, dir);
                if(energyInterfaceTile.canAcceptEnergy(dir)){
                    // This tile will be classed as a receiver
                    node.type = PowerType.RECEIVER;
                } else if(energyInterfaceTile.canProvideEnergy(dir)){
                    //This tile can then provide
                    node.type = PowerType.COLLECTOR;
                }
            }
        }
    }

    public PowerNetwork getNetwork() {
        return network;
    }

    public void setNetwork(PowerNetwork network) {
        this.network = network;
    }

    public void removeCable(){
        network.removeCable(this);
        this.network = null;
    }
}

package techreborn.powernet;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import reborncore.api.power.IEnergyInterfaceTile;

import java.util.ArrayList;
import java.util.List;

public class PowerNetwork {

    List<PowerCable> cables = new ArrayList<PowerCable>();

    int networkPower;

    double maxTransfur;

    public PowerNetwork merge(PowerNetwork network){
        cables.addAll(network.cables);
        for(PowerCable cable : network.cables){
            cable.setNetwork(this);
        }
        return this;
    }


    public void addCable(PowerCable cable){
        cables.add(cable);
        cable.setNetwork(this);
    }

    public void removeCable(PowerCable cable){
        cables.remove(cable);
        cable.setNetwork(null);
    }

    public void tick(World world){
        for(PowerCable cable : cables){
            if(cable.container.getWorld() == world){
                for(PowerNode node : cable.nodes){
                    TileEntity tileEntity = node.nodeTile;
                    if(!(tileEntity instanceof IEnergyInterfaceTile)){
                        continue;
                    }
                    IEnergyInterfaceTile iEnergyInterfaceTile = (IEnergyInterfaceTile) tileEntity;
                    switch (node.type){
                        case COLLECTOR:
                            if(iEnergyInterfaceTile.canProvideEnergy(node.facingFromCable)){
                                double transfur = Math.min(maxTransfur, Math.min(iEnergyInterfaceTile.getMaxOutput(), iEnergyInterfaceTile.getEnergy()));
                                networkPower += iEnergyInterfaceTile.useEnergy(transfur);
                            }
                            break;
                        case RECEIVER:
                            if(iEnergyInterfaceTile.canAcceptEnergy(node.facingFromCable)){
                                double transfur = Math.min(maxTransfur, Math.min(iEnergyInterfaceTile.getMaxOutput(), iEnergyInterfaceTile.getEnergy()));
                                networkPower -= iEnergyInterfaceTile.addEnergy(transfur);
                            }
                            break;
                    }
                }
            }
        }
    }

}

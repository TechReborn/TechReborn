package techreborn.tiles.lesu;


import net.minecraftforge.common.util.ForgeDirection;
import techreborn.tiles.TileAesu;

import java.util.ArrayList;

public class TileLesu extends TileAesu {

    public int baseEU = 1000000000;
    public int storgeBlockSize = 100000;

    private ArrayList<LesuNetwork> countedNetworks = new ArrayList<LesuNetwork>();
    public int connectedBlocks = 0;
    public int currentBlocks = 0;

    @Override
    public void updateEntity() {
        if(worldObj.isRemote){
            return;
        }
        countedNetworks.clear();
        connectedBlocks = 0;
        for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS){
            if(worldObj.getTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ) instanceof TileLesuStorage){
                if(((TileLesuStorage) worldObj.getTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ)).network != null){
                    LesuNetwork network = ((TileLesuStorage) worldObj.getTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ)).network;
                    if(!countedNetworks.contains(network)){
                        if(network.master == null || network.master == this){
                            connectedBlocks += network.storages.size();
                            countedNetworks.add(network);
                            network.master = this;
                        }
                    }
                }
            }
        }
        if(currentBlocks != connectedBlocks){
            maxStorage = (connectedBlocks * storgeBlockSize) + baseEU;
        }
    }
}

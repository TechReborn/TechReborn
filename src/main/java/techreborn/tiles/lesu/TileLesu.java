package techreborn.tiles.lesu;


import ic2.api.tile.IWrenchable;
import net.minecraftforge.common.util.ForgeDirection;
import techreborn.blocks.storage.EUStorageTile;
import techreborn.config.ConfigTechReborn;
import techreborn.util.Inventory;

import java.util.ArrayList;

public class TileLesu extends EUStorageTile implements IWrenchable {

    private ArrayList<LesuNetwork> countedNetworks = new ArrayList<LesuNetwork>();
    public int connectedBlocks = 0;
    public int currentBlocks = 0;

    private double euLastTick = 0;
    private double euChange;
    private int ticks;

    public Inventory inventory = new Inventory(2, "TileAesu", 64);

    public TileLesu() {
        super(5, 0, 0);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
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
            maxStorage = ((connectedBlocks + 1) * ConfigTechReborn.lesuStoragePerBlock);
            output = (connectedBlocks * ConfigTechReborn.extraOutputPerLesuBlock) + ConfigTechReborn.baseLesuOutput;
        }

        if(ticks == ConfigTechReborn.aveargeEuOutTickTime){
            euChange = -1;
            ticks = 0;
        } else {
            ticks ++;
            if(euChange == -1){
                euChange = 0;
            }
            euChange += energy - euLastTick;
            if(euLastTick == energy){
                euChange = 0;
            }
        }

        euLastTick = energy;
    }

    @Override
    public String getInventoryName() {
        return "Lesu";
    }

    public double getEuChange(){
        if(euChange == -1){
            return 0;
        }
        return (euChange / ticks);
    }
}

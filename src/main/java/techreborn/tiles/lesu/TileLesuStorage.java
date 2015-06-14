package techreborn.tiles.lesu;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import techreborn.tiles.TileMachineBase;

public class TileLesuStorage extends TileMachineBase {

    public LesuNetwork network;

    @Override
    public void updateEntity() {
        super.updateEntity();
        if(network == null){
            findAndJoinNetwork(worldObj, xCoord, yCoord, zCoord);
        } else {
            //network.printInfo();
        }
    }

    public final void findAndJoinNetwork(World world, int x, int y, int z) {
        network = new LesuNetwork();
        network.addElement(this);
        for(ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS){
            if(world.getTileEntity(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ) instanceof TileLesuStorage){
                TileLesuStorage lesu = (TileLesuStorage) world.getTileEntity(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ);
                if(lesu.network != null){
                    lesu.network.merge(network);
                }
            }
        }
    }

    public final void setNetwork(LesuNetwork n) {
        if (n == null) {
        } else {
            network = n;
            network.addElement(this);
        }
    }

    public final void resetNetwork() {
        network = null;
    }

    public final void removeFromNetwork() {
        if (network == null) {
        } else
            network.removeElement(this);
    }

    public final void rebuildNetwork() {
        this.removeFromNetwork();
        this.resetNetwork();
        this.findAndJoinNetwork(worldObj, xCoord, yCoord, zCoord);
    }
}

package techreborn.tiles;

import ic2.api.energy.prefab.BasicSink;
import net.minecraft.nbt.NBTTagCompound;
import techreborn.util.Inventory;

public class TileCentrifuge extends TileMachineBase {

    public BasicSink energy;
    public Inventory inventory = new Inventory(6, "TileCentrifuge", 64);

    public TileCentrifuge() {
        //TODO check values, + config
        energy = new BasicSink(this, 100000, 1);
    }


    @Override
    public void updateEntity() {
        super.updateEntity();
        energy.updateEntity();
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        inventory.readFromNBT(tagCompound);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        inventory.writeToNBT(tagCompound);
    }
}

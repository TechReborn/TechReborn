package techreborn.tiles;

import net.minecraft.nbt.NBTTagCompound;
import techreborn.api.farm.IFarmLogicContainer;
import techreborn.api.farm.IFarmLogicDevice;
import techreborn.farm.FarmTree;
import techreborn.util.Inventory;

public class TileFarm extends TileMachineBase {

    public Inventory inventory= new Inventory(14, "TileFarm", 64);

    IFarmLogicDevice logicDevice;

    public TileFarm() {
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        inventory.readFromNBT(tagCompound);
        super.readFromNBT(tagCompound);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        inventory.writeToNBT(tagCompound);
        super.writeToNBT(tagCompound);
    }

    @Override
    public void updateEntity() {
        if(inventory.hasChanged){
            if(inventory.getStackInSlot(0) != null && inventory.getStackInSlot(0).getItem() instanceof IFarmLogicContainer){
                IFarmLogicContainer device = (IFarmLogicContainer) inventory.getStackInSlot(0).getItem();
                logicDevice = device.getLogicFromStack(inventory.getStackInSlot(0));
            } else {
                logicDevice = null;
            }
        }
        if(logicDevice != null){
            logicDevice.tick(this);
        }
        super.updateEntity();
    }
}

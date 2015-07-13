package techreborn.api.farm;

import net.minecraft.item.ItemStack;

public interface IFarmLogicContainer {

    public IFarmLogicDevice getLogicFromStack(ItemStack stack);

}

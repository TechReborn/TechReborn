package techreborn.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import techreborn.api.farm.IFarmLogicContainer;
import techreborn.api.farm.IFarmLogicDevice;
import techreborn.client.TechRebornCreativeTabMisc;
import techreborn.farm.FarmTree;

public class ItemFarmPatten extends Item implements IFarmLogicContainer {


    public ItemFarmPatten() {
        setCreativeTab(TechRebornCreativeTabMisc.instance);
        setUnlocalizedName("techreborn.farmPatten");
    }

    @Override
    public IFarmLogicDevice getLogicFromStack(ItemStack stack) {
        return new FarmTree();
    }
}

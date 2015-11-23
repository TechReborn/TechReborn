package techreborn.items;

import net.minecraft.item.Item;
import techreborn.client.TechRebornCreativeTab;

public class ItemTR extends Item {

    public ItemTR() {
        setNoRepair();
        setCreativeTab(TechRebornCreativeTab.instance);
    }

}

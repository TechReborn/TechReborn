package techreborn.items;

import reborncore.api.TextureRegistry;
import reborncore.jsonDestroyers.item.BaseItem;
import techreborn.client.TechRebornCreativeTab;

public class ItemTR extends BaseItem {

    public ItemTR() {
        setNoRepair();
        setCreativeTab(TechRebornCreativeTab.instance);
        TextureRegistry.registerItem(this);
    }

}

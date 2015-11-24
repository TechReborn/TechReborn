package techreborn.items;

import net.minecraft.item.Item;
import reborncore.api.TextureRegistry;
import techreborn.client.TechRebornCreativeTab;

public class ItemTR extends Item {

    public ItemTR() {
        setNoRepair();
        setCreativeTab(TechRebornCreativeTab.instance);
        TextureRegistry.registerItem(this);
    }

}

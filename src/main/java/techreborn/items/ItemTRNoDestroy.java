package techreborn.items;

import net.minecraft.item.Item;
import techreborn.client.TechRebornCreativeTab;

public class ItemTRNoDestroy extends Item {

	public ItemTRNoDestroy() {
		setNoRepair();
		setCreativeTab(TechRebornCreativeTab.instance);
	}

}

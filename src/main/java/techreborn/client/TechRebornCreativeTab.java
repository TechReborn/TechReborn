package techreborn.client;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import techreborn.Core;

public class TechRebornCreativeTab extends CreativeTabs {

	public static TechRebornCreativeTab instance = new TechRebornCreativeTab();

	public TechRebornCreativeTab() {
		super("techreborn");
	}

	@Override
	public Item getTabIconItem() {
		return Item.getItemFromBlock(Core.thermalGenerator);
	}
}

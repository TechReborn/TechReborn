package techreborn.client;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import techreborn.init.ModItems;

public class TechRebornCreativeTabMisc extends CreativeTabs {

	public static TechRebornCreativeTabMisc instance = new TechRebornCreativeTabMisc();

	public TechRebornCreativeTabMisc() {
		super("techreborn");
	}

	@Override
	public Item getTabIconItem() {
		return ModItems.dynamicCell;
	}

}

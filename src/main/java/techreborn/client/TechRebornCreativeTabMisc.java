package techreborn.client;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import techreborn.init.ModItems;

public class TechRebornCreativeTabMisc extends CreativeTabs {

	public static TechRebornCreativeTabMisc instance = new TechRebornCreativeTabMisc();

	public TechRebornCreativeTabMisc() {
		super("techreborn");
	}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(ModItems.dynamicCell);
	}

}

package techreborn.client;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import techreborn.init.ModBlocks;

public class TechRebornCreativeTab extends CreativeTabs {

	public static TechRebornCreativeTab instance = new TechRebornCreativeTab();

	public TechRebornCreativeTab() {
		super("techreborn");
	}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(ModBlocks.thermalGenerator);
	}
}

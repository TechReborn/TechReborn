package techreborn.client;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import techreborn.init.ModItems;
import techreborn.items.DynamicCell;

import java.util.Random;

public class TechRebornCreativeTabMisc extends CreativeTabs
{

	public static TechRebornCreativeTabMisc instance = new TechRebornCreativeTabMisc();

	public TechRebornCreativeTabMisc()
	{
		super("techreborn");
	}

	@Override
	public Item getTabIconItem()
	{
		return ModItems.dynamicCell;
	}

}

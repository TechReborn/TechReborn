package techreborn.items;

import techreborn.client.TechRebornCreativeTab;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

public class ItemTR extends Item{
	
	public ItemTR()
	{
		setNoRepair();
		setCreativeTab(TechRebornCreativeTab.instance);
	}
	
	@Override
	public void registerIcons(IIconRegister iconRegister) 
	{
		itemIcon = iconRegister.registerIcon("techreborn" + ":" + getUnlocalizedName().toLowerCase().substring(5));
	}

}

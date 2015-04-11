package techreborn.items;

import techreborn.client.TechRebornCreativeTab;
import techreborn.lib.ModInfo;
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
		itemIcon = iconRegister.registerIcon(ModInfo.MOD_ID + ":" + getUnlocalizedName().toLowerCase().substring(5));
	}

}

package techreborn.items;

import techreborn.client.TechRebornCreativeTab;
import techreborn.lib.ModInfo;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

public class ItemTechManuel extends Item{
	
	public ItemTechManuel()
	{
		setCreativeTab(TechRebornCreativeTab.instance);
		setUnlocalizedName("techreborn.manuel");
		setMaxStackSize(1);
	}
	
    @Override
    public void registerIcons(IIconRegister iconRegister)
    {
        itemIcon = iconRegister.registerIcon("techreborn:" + "tool/manuel");
    }

}

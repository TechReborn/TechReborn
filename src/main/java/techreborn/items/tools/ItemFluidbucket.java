package techreborn.items.tools;

import techreborn.lib.ModInfo;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemBucket;

public class ItemFluidbucket extends ItemBucket{
	
	public ItemFluidbucket(Block block)
	{
		super(block);
		setUnlocalizedName("techreborn.fluidbucket");
	}
	
	@Override
	public void registerIcons(IIconRegister iconRegister) 
	{
		itemIcon = iconRegister.registerIcon(ModInfo.MOD_ID.toLowerCase() + ":"
				+ getUnlocalizedName());
	}

}

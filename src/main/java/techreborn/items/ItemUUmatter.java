package techreborn.items;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import techreborn.client.TechRebornCreativeTabMisc;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemUUmatter extends Item{
	
	public ItemUUmatter()
	{
		setCreativeTab(TechRebornCreativeTabMisc.instance);
		setUnlocalizedName("techreborn.uumattaer");
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister iconRegister)
	{
		this.itemIcon = iconRegister.registerIcon("techreborn:"+ "misc/itemMatter");
	}

}

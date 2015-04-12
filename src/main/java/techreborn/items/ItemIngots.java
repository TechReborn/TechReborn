package techreborn.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import techreborn.client.TechRebornCreativeTab;

public class ItemIngots extends Item{
	public static final String[] types = new String[] 
	{
		"IridiumAlloy", "HotTungstenSteel", "TungstenSteel", "Iridium", "Silver", "Aluminium", "Titanium", "Chrome",
		"Electrum","Tungsten", "Lead", "Zinc", "Brass", "Steel", "Platinum", "Nickel", "Invar", 
	};

	private IIcon[] textures;
	
	public ItemIngots()
	{
		setCreativeTab(TechRebornCreativeTab.instance);
		setHasSubtypes(true);
		setUnlocalizedName("techreborn.ingot");
	}
	
	@Override
	// Registers Textures For All Dusts
	public void registerIcons(IIconRegister iconRegister)
	{
		textures = new IIcon[types.length];

		for (int i = 0; i < types.length; ++i) 
		{
			textures[i] = iconRegister.registerIcon("techreborn:" + "ingot"+types[i]);
		}
	}
	
	@Override
	// Adds Texture what match's meta data
	public IIcon getIconFromDamage(int meta)
	{
		if (meta < 0 || meta >= textures.length) 
		{
			meta = 0;
		}

		return textures[meta];
	}

	@Override
	// gets Unlocalized Name depending on meta data
	public String getUnlocalizedName(ItemStack itemStack)
	{
		int meta = itemStack.getItemDamage();
		if (meta < 0 || meta >= types.length) 
		{
			meta = 0;
		}

		return super.getUnlocalizedName() + "." + types[meta];
	}

	// Adds Dusts SubItems To Creative Tab
	public void getSubItems(Item item, CreativeTabs creativeTabs, List list)
	{
		for (int meta = 0; meta < types.length; ++meta) 
		{
			list.add(new ItemStack(item, 1, meta));
		}
	}
	
	@Override
	public EnumRarity getRarity(ItemStack itemstack) 
	{
		return EnumRarity.uncommon;
	}

}
